package models.bcv.intervencion_lecturaa;

import java.io.FileInputStream;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import megasoft.DataSet;
import megasoft.Logger;
import megasoft.db;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import sun.misc.Regexp;

import com.bancovenezuela.comun.util.Utilitario;
import com.bdv.infi.dao.IntervencionDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.OrdenesCrucesDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.UsuarioDAO;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.logic.Transaccion;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

public class LeerarchivoMenudeo extends Transaccion {

	OrdenesCrucesDAO ordenesCrucesDAO;
	private ProcesosDAO procesosDAO;
	private IntervencionDAO inter;
	private Proceso proceso;
	private int secuenciaProcesos = 0;
	String tipoTransaccion = TransaccionNegocio.INTERVENCION_LECTURA_ARCHIVO;
	int idUsuario = 0;
	int noRegistrada = 0;
	public String Errores = "";
	private DataSet datos = new DataSet();
	String nombreStr = "";

	HSSFCell cedulaRif = null;
	HSSFCell nacionalidad = null;
	HSSFCell nombre = null;
	HSSFCell montoDivisass = null;
	HSSFCell tipoCambio = null;
	HSSFCell cuentaDivisas = null;
	HSSFCell cuentaBs = null;
	HSSFCell monedaIso = null;

	public void execute() throws Exception {

		leer();
		datos.append("texto", java.sql.Types.VARCHAR);
		datos.addNew();
		datos.setValue("texto", this.Errores);
		storeDataSet("datos", datos);

	}

	private void leer() {
		try {
			iniciarProceso();
			ordenesCrucesDAO = new OrdenesCrucesDAO(_dso);
			inter = new IntervencionDAO(_dso);
			String contenidoDocumento = getSessionObject("contenidoDocumento").toString();
			FileInputStream documento = new FileInputStream(contenidoDocumento);
			HSSFWorkbook libro = new HSSFWorkbook(documento);
			HSSFSheet hoja = libro.getSheetAt(0);
			int numFila = hoja.getLastRowNum();
			// numFila = numFila - 1;
			HSSFRow fila;
			for (int a = 4; a <= numFila; a++) {
				fila = hoja.getRow(a);
				if (fila != null) {

					nacionalidad = fila.getCell((short) 0);
					cedulaRif = fila.getCell((short) 1);
					nombre = fila.getCell((short) 2);
					montoDivisass = fila.getCell((short) 3);
					tipoCambio = fila.getCell((short) 4);
					cuentaBs = fila.getCell((short) 5);
					cuentaDivisas = fila.getCell((short) 6);
					monedaIso = fila.getCell((short) 7);
					

					boolean valido = validacionDeCampos(cedulaRif.toString(), nacionalidad.toString(), nombre.toString(), montoDivisass.toString(), tipoCambio.toString(), cuentaDivisas.toString(), cuentaBs.toString(), monedaIso.toString());

					if (valido) {

						try {

							String insert = inter.insertarTransaccion(cedulaRif.toString().trim(), nacionalidad.toString(), nombre.toString().replace(".", ""), montoDivisass.toString(), tipoCambio.toString(), cuentaDivisas.toString().trim(), cuentaBs.toString().trim(), monedaIso.toString().trim());
							db.exec(_dso, insert.toString());
						} catch (Exception e) {
							capturaError(e);
							continue;
						}
					}

				} else {
					break;
				}

			}
			documento.close();
			
			if (noRegistrada == 0) {
				Errores = "Todas las operaciones fueron agregadas";
				proceso.agregarDescripcionError("Todas las operaciones fueron agregada");
			} else {
				proceso.agregarDescripcionError("Operaciones fallidas :" + noRegistrada + " : " + Errores);
			}

		} catch (Exception e) {
			System.out.println("LeerarchivoIntervencion : leer() " + e);
			Logger.error(this, "LeerarchivoIntervencion : leer() " + e);

		} finally {
			finalizarProceso();

		}
	}

	/**
	 * metodo para crear un ciclo con fecha y usuario ejecutando el proceso.
	 * 
	 * @throws Exception
	 */
	protected void iniciarProceso() throws Exception {

		UsuarioDAO usuarioDAO = new UsuarioDAO(_dso);
		idUsuario = Integer.parseInt((usuarioDAO.idUserSession(getUserName())));
		Logger.info(this, "INICIO DE PROCESO");
		procesosDAO = new ProcesosDAO(_dso);
		proceso = new Proceso();
		secuenciaProcesos = Integer.parseInt(OrdenDAO.dbGetSequence(_dso, com.bdv.infi.logic.interfaces.ConstantesGenerales.SECUENCIA_PROCESOS));
		proceso.setEjecucionId(secuenciaProcesos);
		proceso.setFechaInicio(new Date());
		proceso.setFechaValor(new Date());
		proceso.setTransaId(tipoTransaccion);
		proceso.setUsuarioId(this.idUsuario);

		String queryProceso = procesosDAO.insertar(proceso);
		db.exec(_dso, queryProceso);
	}

	private void finalizarProceso() {
		try {
			String queryProcesoCerrar = procesosDAO.modificar(proceso);
			db.exec(_dso, queryProcesoCerrar);
			Logger.info(this, "FIN DE PROCESO: " + new Date());

		} catch (Exception e) {
			Logger.error(this, "LeerarchivoIntervencion : finalizarProceso()" + e);
			System.out.println("LeerarchivoIntervencion : finalizarProceso()" + e);

		}

	}

	private void capturaError(Exception e) {
		String err = "";
		if (e.getMessage().contains("unique constraint")) {
			err = "Ya fue registrado la operacion :" + nacionalidad + "-" + cedulaRif;
		} else if (e.getMessage().contains("too many values")) {
			err = "Favor volver a cargar esta operacion y cambiar las celdas que tengas comas por punto :" + nacionalidad + "-" + cedulaRif;
		} else {
			err = e.toString().substring(64, 200) + " Id  : " + cedulaRif;
		}
		Errores += err + "\n";
		noRegistrada++;
	}

	private boolean validacionDeCampos(String cedulaRif, String nacionalidad, String nombre, String montoDivisass, String tipoCambio, String cuentaDivisas, String cuentaBs, String monedaIso) {
		boolean validar = true;
		if (cedulaRif.equals("") || cedulaRif.equals(null)) {
			Errores += "Esta vacio el campo Cedula, id : " + cedulaRif.toString() + "\n";
			validar = false;
		}
		
		if (nacionalidad.equals("") || nacionalidad.equals(null)) {
			Errores += "Esta vacio el campo nacionalidad, id : " + cedulaRif.toString() + "\n";
			validar = false;
		}
		if (nombre.equals("") || nombre.equals(null)) {
			Errores += "Esta vacio el campo nombre, id : " + cedulaRif.toString() + "\n";
			validar = false;
		}
		boolean validarCaracter = verificarCaracteres(nombre);
		if (validarCaracter) {

			Errores += "El nombre tiene caracteres especiales, id : " + cedulaRif.toString() + "\n";
			noRegistrada++;
			validar = false;
		}else if(nombre.contains(",")){
			Errores += "El nombre tiene caracteres especiales, id : " + cedulaRif.toString() + "\n";
			noRegistrada++;
			validar = false;
		}
	
		if (montoDivisass.equals("") || montoDivisass.equals(null)) {
			Errores += "Esta vacio el campo Monto Divisas, id : " + cedulaRif.toString() + "\n";
			validar = false;
		}
		if (tipoCambio.equals("") || tipoCambio.equals(null)) {
			Errores += "Esta vacio el campo Tipo Cambio, id : " + cedulaRif.toString() + "\n";
			validar = false;
		}
		if (monedaIso.equals("") || monedaIso.equals(null)) {
			Errores += "Esta vacio el campo Moneda ISO, id : " + cedulaRif.toString() + "\n";
			validar = false;
		}
		if (cuentaDivisas.equals("") || cuentaDivisas.equals(null)) {
			Errores += "Esta vacio el campo Cuenta Divisas, id : " + cedulaRif.toString() + "\n";
			validar = false;
		}else if (cuentaDivisas.length()!=20){
			Errores += "La Cuenta Divisas no puede ser diferente a 20 Digitos, id : " + cedulaRif.toString() + "\n";
			validar = false;
		}
		
		if (cuentaBs.equals("") || cuentaBs.equals(null)) {
			Errores += "Esta vacio el campo Cuenta Bs, id : " + cedulaRif.toString() + "\n";
			validar = false;
		}else if (cuentaBs.length()!=20){
			Errores += "La Cuenta Bs no puede ser diferente a 20 Digitos, id : " + cedulaRif.toString() + "\n";
			validar = false;
		}

		return validar;

	}

	public boolean verificarCaracteres(String string){

		// expresión regular que revisa si tiene alguno de los siguientes caracteres

		String REG_EXP = "\\¿+|\\?+|\\°+|\\¬+|\\|+|\\!+|\\#+|\\$+|" +
		"\\%+|\\&+|\\+|\\=+|\\’+|\\¡+|\\++|\\*+|\\~+|\\[+|\\]" +
		"|\\{+|\\}+|\\^+|\\<+|\\>+|\\\"";
		Pattern pattern = Pattern.compile(REG_EXP);
		Matcher matcher = pattern.matcher(string);
//	   System.out.println("probando patten " + matcher.find()); //imprime true si tiene alguno de los caracteres anteriores o false si no tiene ninguno
		return matcher.find();
	}
	
}

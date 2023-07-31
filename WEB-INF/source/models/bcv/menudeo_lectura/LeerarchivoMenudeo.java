package models.bcv.menudeo_lectura;

import java.io.FileInputStream;
import java.util.Date;
import java.util.HashMap;

import javax.sql.DataSource;

import megasoft.DataSet;
import megasoft.Logger;
import megasoft.db;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.OrdenesCrucesDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.UsuarioDAO;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.logic.Transaccion;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.model.menudeo.Anular;

public class LeerarchivoMenudeo extends Transaccion {

	OrdenesCrucesDAO ordenesCrucesDAO;
	private ProcesosDAO procesosDAO;
	private Proceso proceso;
	private int secuenciaProcesos = 0;
	String tipoTransaccion = TransaccionNegocio.MENUDEO_LECTURA_ARCHIVO;
	protected HashMap<String, String> parametrosMenudeBCV;
	int idUsuario = 0;
	int noRegistrada = 0;
	public String Errores = "";
	private DataSet datos = new DataSet();

	HSSFCell operacion = null;// columna compra o venta en codigo
	HSSFCell estatusOperacion = null; // puede ser reversado o origional (R o O)
	HSSFCell idMonedaExtranjera = null;// id de operacion de mainframe
	HSSFCell montoBolivares = null; // monto en bolivares
	HSSFCell montoComision = null;// monto de la comision(hasta el momento no piden comision)
	HSSFCell conceptoEstadistica = null;// concep estadistico, por este campo podemos saber si es una operacion de compra de combustible
	HSSFCell codigoOficiona = null;// codigo de la oficiona donde ocurre la compra o venta
	HSSFCell mto_divisas = null;// columna monto divisas
	HSSFCell tasa_cambio = null;// columna tasa de cambio
	HSSFCell nacialidad = null;// columna nacionalidad
	HSSFCell ced_rif = null;// columna cedula o rif
	HSSFCell nom_clien = null;// columna nombre de cliente
	HSSFCell cuent_clien = null;// columna cuenta del cliente
	HSSFCell co_divisas = null;// columna codigo divisas
	HSSFCell email_clien = null;// columna correo del cliente
	HSSFCell telefono = null;// columna telefono
	
	HSSFCell idBcv = null;// columna id de bcv para el archvio de anulacion 

	public void execute() throws Exception {
//		System.out.println("nombreDocumento : " + getSessionObject("nombreDocumento").toString());
		String nombreDocumento = getSessionObject("nombreDocumento").toString();
		if(nombreDocumento.equalsIgnoreCase("Plantilla_Anulacion_MENUDEO.xls")){
			leerAnular();
		}else{
			leer();
		}
		datos.append("texto", java.sql.Types.VARCHAR);
		datos.addNew();
		datos.setValue("texto", this.Errores);
		storeDataSet("datos", datos);

	}

	private void leer() {
		try {
			iniciarProceso();
			obtenerParametros(_dso);
			ordenesCrucesDAO = new OrdenesCrucesDAO(_dso);
			String contenidoDocumento = getSessionObject("contenidoDocumento").toString();
			FileInputStream documento = new FileInputStream(contenidoDocumento);
			HSSFWorkbook libro = new HSSFWorkbook(documento);
			HSSFSheet hoja = libro.getSheetAt(0);
			int numFila = hoja.getLastRowNum();
			// numFila = numFila - 1;
			HSSFRow fila;
			Integer idContadorMe = 0;
			double montoDivisas = 0;
			String maxPorOperacionTexto = parametrosMenudeBCV.get(ConstantesGenerales.MONTO_MAXIMO_MENUDEO);
			double maxPorOperacion = Double.parseDouble(maxPorOperacionTexto);
			for (int a = 4; a <= numFila; a++) {

				fila = hoja.getRow(a);
				if (fila != null) {

					operacion = fila.getCell((short) 0); // columna compra o venta en codigo
					idMonedaExtranjera = fila.getCell((short) 1); // Codigo de mainframe unico
					estatusOperacion = fila.getCell((short) 2); // O para original R para reversado
					conceptoEstadistica = fila.getCell((short) 3); // Indiciar de que tipo de compra es Ej 100 para compra de combustible
					mto_divisas = fila.getCell((short) 4);
					montoBolivares = fila.getCell((short) 5);
					montoComision = fila.getCell((short) 6);
					codigoOficiona = fila.getCell((short) 7);
					tasa_cambio = fila.getCell((short) 8);// columna tasa de cambio
					nacialidad = fila.getCell((short) 9);// columna nacionalidad
					ced_rif = fila.getCell((short) 10);// columna cedula o rif
					nom_clien = fila.getCell((short) 11);// columna nombre de cliente
					cuent_clien = fila.getCell((short) 12);// columna cuenta del cliente
					co_divisas = fila.getCell((short) 13);// columna codigo divisas
					email_clien = fila.getCell((short) 14);// columna correo del cliente
					telefono = fila.getCell((short) 15);// columna telefono
					// System.out.println("operacion : " + operacion);
					// System.out.println("idMonedaExtranjera : " + idMonedaExtranjera);
					// System.out.println("estatusOperacion : " + estatusOperacion);
					// System.out.println("conceptoEstadistica : " + conceptoEstadistica);
					// System.out.println("mto_divisas : " + mto_divisas);
					// System.out.println("montoBolivares : " + montoBolivares);
					// System.out.println("montoComision : " + montoComision);
					// System.out.println("codigoOficiona : " + codigoOficiona);
					// System.out.println("tasa_cambio : " + tasa_cambio);
					// System.out.println("nacialidad : " + nacialidad);
					// System.out.println("ced_rif : " + ced_rif);
					// System.out.println("nom_clien : " + nom_clien);
					// System.out.println("cuent_clien : " + cuent_clien);
					// System.out.println("co_divisas : " + co_divisas);
					// System.out.println("email_clien : " + email_clien);
					// System.out.println("telefono : " + telefono);
					boolean valido = validacionDeCampos(operacion.toString(), idContadorMe.toString(), estatusOperacion.toString(), montoComision.toString(), conceptoEstadistica.toString(), codigoOficiona.toString(), String.valueOf(maxPorOperacion), montoBolivares.toString(), tasa_cambio.toString(), nacialidad.toString(), ced_rif.toString(), nom_clien.toString().trim(), cuent_clien.toString()
							.replaceAll(" ", "").trim(), co_divisas.toString(), email_clien.toString().replaceAll(" ", "").trim(), telefono.toString());
					
					if (valido) {

						Integer count = 1;
						montoDivisas = Double.parseDouble(mto_divisas.toString());

						if (Integer.parseInt(idMonedaExtranjera.toString()) <= idContadorMe) {

							idContadorMe++;
							
						} else {
							idContadorMe = Integer.parseInt(idMonedaExtranjera.toString());
						}
						try {
							while (montoDivisas > maxPorOperacion) {

								ordenesCrucesDAO.insertar_lectura_menudeo(operacion.toString(), idContadorMe.toString(), estatusOperacion.toString(), montoComision.toString(), conceptoEstadistica.toString(), codigoOficiona.toString(), String.valueOf(maxPorOperacion), montoBolivares.toString(), tasa_cambio.toString(), nacialidad.toString(), ced_rif.toString(), nom_clien.toString().trim(),
										cuent_clien.toString().replaceAll(" ", "").trim(), co_divisas.toString(), email_clien.toString().replaceAll(" ", "").trim(), telefono.toString());

								montoDivisas = montoDivisas - maxPorOperacion;

						
								count++;
								idContadorMe++;
							}

							ordenesCrucesDAO.insertar_lectura_menudeo(operacion.toString(), idContadorMe.toString(), estatusOperacion.toString(), montoComision.toString(), conceptoEstadistica.toString(), codigoOficiona.toString(), String.valueOf(montoDivisas), montoBolivares.toString(), tasa_cambio.toString(), nacialidad.toString(), ced_rif.toString(), nom_clien.toString().trim(), cuent_clien
									.toString().replaceAll(" ", "").trim(), co_divisas.toString(), email_clien.toString().replaceAll(" ", "").trim(), telefono.toString());

						} catch (Exception e) {
							capturaError(e);
							continue;
						}
					}

				} else {
					break;
				}

			}
			Errores += "Quedo en el id Numero : " + idContadorMe + " Favor continuar en el siguiente numero";
			documento.close();
			Logger.info(this, "Contador :" + idContadorMe);
			if (noRegistrada == 0) {

				proceso.agregarDescripcionError("Todo las operaciones fueron agregada fue agregado");
			} else {
				proceso.agregarDescripcionError("Operaciones fallidas :" + noRegistrada + " : " + Errores);
			}

		} catch (Exception e) {
			System.out.println("LeerarchivoMenudeo : leer() " + e);
			Logger.error(this, "LeerarchivoMenudeo : leer() " + e);

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
		proceso.setUsuarioId(idUsuario);

		String queryProceso = procesosDAO.insertar(proceso);
		db.exec(_dso, queryProceso);
	}

	private void finalizarProceso() {
		try {
			String queryProcesoCerrar = procesosDAO.modificar(proceso);
			db.exec(_dso, queryProcesoCerrar);
			Logger.info(this, "FIN DE PROCESO: " + new Date());

		} catch (Exception e) {
			Logger.error(this, "OficinasIntervencion : finalizarProceso()" + e);
			System.out.println("OficinasIntervencion : finalizarProceso()" + e);

		}

	}

	private void capturaError(Exception e) {
		String err = "";
		if (e.getMessage().contains("unique constraint")) {
			err = "Ya fue registrado la operacion :" + nacialidad + "-" + ced_rif + ", Id :" + idMonedaExtranjera;
		} else if (e.getMessage().contains("too many values")) {
			err = "Favor volver a cargar esta operacion y cambiar las celdas que tengas comas por punto :" + nacialidad + "-" + ced_rif + ", Id Moneda extranjera :" + idMonedaExtranjera;
		} else {
			err = e.toString().substring(64, 200) + "Id Moneda extranjera : " + idMonedaExtranjera;
			// err = e.toString()+ "Id Moneda extranjera : " + idMonedaExtranjera;

		}

		Errores += err + "\n";
		noRegistrada++;
	}

	private boolean validacionDeCampos(String operCV, String id, String estatus, String estadistica, String montoDivisas, String montoBs, String montoComision, String codigoOficina, String tasaCambio, String nacionalidad, String cedulaRif, String nombre, String cuenta, String codigoDivisas, String correo, String telefono) {

		boolean validar = true;
		if (operCV.equals("") || operCV.equals(null)) {
			Errores += "Esta vacio el campo Operacion, id : " + idMonedaExtranjera.toString() + "\n";
			validar = false;
		}
		if (id.equals("") || id.equals(null)) {
			Errores += "Esta vacio el campo id, id : " + idMonedaExtranjera.toString() + "\n";
			validar = false;
		}
		if (estatus.equals("") || estatus.equals(null)) {
			Errores += "Esta vacio el campo estatus, id : " + idMonedaExtranjera.toString() + "\n";
			validar = false;
		}
		if (estadistica.equals("") || estadistica.equals(null)) {
			Errores += "Esta vacio el campo estadistica, id : " + idMonedaExtranjera.toString()+ "\n";
			validar = false;
		}
		if (montoDivisas.equals("") || montoDivisas.equals(null)) {
			Errores += "Esta vacio el campo montoDivisas, id : " + idMonedaExtranjera.toString()+ "\n";
			validar = false;
		}
		if (montoBs.equals("") || montoBs.equals(null)) {
			Errores += "Esta vacio el campo montoBs, id : " + idMonedaExtranjera.toString()+ "\n";
			validar = false;
		}
		if (montoComision.equals("") || montoComision.equals(null)) {
			Errores += "Esta vacio el campo Operacion, id : " + idMonedaExtranjera.toString()+ "\n";
			validar = false;
		}
		if (codigoOficina.equals("") || codigoOficina.equals(null)) {
			Errores += "Esta vacio el campo codigo Oficina, id : " + idMonedaExtranjera.toString()+ "\n";
			validar = false;
		}
		if (tasaCambio.equals("") || tasaCambio.equals(null)) {
			Errores += "Esta vacio el campo tasaCambio, id : " + idMonedaExtranjera.toString()+ "\n";
			validar = false;
		}
		if (nacionalidad.equals("") || nacionalidad.equals(null)) {
			Errores += "Esta vacio el campo nacionalidad, id : " + idMonedaExtranjera.toString()+ "\n";
			validar = false;
		}
		if (cedulaRif.equals("") || cedulaRif.equals(null)) {
			Errores += "Esta vacio el campo cedulaRif, id : " + idMonedaExtranjera.toString()+ "\n";
			validar = false;
		}
		if (nombre.equals("") || nombre.equals(null)) {
			Errores += "Esta vacio el campo nombre, id : " + idMonedaExtranjera.toString()+ "\n";
			validar = false;
		}
		if (cuenta.equals("") || cuenta.equals(null)) {
			Errores += "Esta vacio el campo cuenta, id : " + idMonedaExtranjera.toString()+ "\n";
			validar = false;
		}
		if (codigoDivisas.equals("") || codigoDivisas.equals(null)) {
			Errores += "Esta vacio el campo codigoDivisas, id : " + idMonedaExtranjera.toString()+ "\n";
			validar = false;
		}
		if (correo.equals("") || correo.equals(null)) {
			Errores += "Esta vacio el campo correo, id : " + idMonedaExtranjera.toString()+ "\n";
			validar = false;
		}
		if (telefono.equals("") || telefono.equals(null)) {
			Errores += "Esta vacio el campo telefono, id : " + idMonedaExtranjera.toString()+ "\n";
			validar = false;
		}
		return validar;

	}
	
	private void leerAnular() {
		try {
			iniciarProceso();
			String contenidoDocumento = getSessionObject("contenidoDocumento").toString();
			FileInputStream documento = new FileInputStream(contenidoDocumento);
			HSSFWorkbook libro = new HSSFWorkbook(documento);
			HSSFSheet hoja = libro.getSheetAt(0);
			int numFila = hoja.getLastRowNum();
			Boolean validar = true;
			HSSFRow fila;
			Integer idContadorMe = 0;
			String comentario = "Anulacion por archivo";
			for (int a = 4; a <= numFila; a++) {

				fila = hoja.getRow(a);
				if (fila != null) {

					idBcv = fila.getCell((short) 0); // columna compra o venta en codigo
					
					if (idBcv.equals("") || idBcv.equals(null)) {
						Errores += "Esta vacio el campo id, id : " + idMonedaExtranjera.toString() + "\n";
						validar = false;
					}
					
					if (validar) {
						System.out.println("paso validar");
						Anular anu = new Anular();
						String codigoAnulacion = anu.ProcesarAnuladas(idBcv.toString(), comentario);
						idContadorMe++;
						Errores += "Resumen de la operacion  ID " + idBcv +": Respuesta :" + codigoAnulacion + "\n";
					}

				} else {
					break;
				}

			}
			documento.close();
			Logger.info(this, "Contador :" + idContadorMe);
			if (noRegistrada == 0) {
				proceso.agregarDescripcionError("Todo las operaciones fueron anuladas");
			} else {
				proceso.agregarDescripcionError("Operaciones fallidas :" + noRegistrada + " : " + Errores);
			}

		} catch (Exception e) {
			System.out.println("LeerarchivoMenudeo : leerAnular() " + e);
			Logger.error(this, "LeerarchivoMenudeo : leerAnular() " + e);
			noRegistrada++;
		} finally {
			finalizarProceso();

		}
	}
	
	
	protected void obtenerParametros(DataSource _dso) throws Exception {
		ParametrosDAO parametrosDAO = new ParametrosDAO(_dso);
		parametrosMenudeBCV = parametrosDAO.buscarParametros(ParametrosSistema.INTERFACE_MENUDEO);
	}
}

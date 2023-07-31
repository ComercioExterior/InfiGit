package models.bcv.mesas_lectura_anulacion;

import java.io.FileInputStream;
import java.util.Date;
import megasoft.DataSet;
import megasoft.Logger;
import megasoft.db;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.OrdenesCrucesDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.UsuarioDAO;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.logic.Transaccion;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.model.mesaCambio.Anular;

public class LeerarchivoMenudeo extends Transaccion {

	OrdenesCrucesDAO ordenesCrucesDAO;
	private ProcesosDAO procesosDAO;
	private Proceso proceso;
	private int secuenciaProcesos = 0;
	String tipoTransaccion = TransaccionNegocio.WS_BCV_MESAS_A;
	int idUsuario = 0;
	int noRegistrada = 0;
	public String Errores = "";
	private DataSet datos = new DataSet();
	HSSFCell idBcv = null;// columna id de bcv para el archvio de anulacion

	public void execute() throws Exception {

		leerAnular();
		datos.append("texto", java.sql.Types.VARCHAR);
		datos.addNew();
		datos.setValue("texto", this.Errores);
		storeDataSet("datos", datos);

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
			Logger.error(this, "LeerAnulacionMesa : finalizarProceso()" + e);
			System.out.println("LeerAnulacionMesa : finalizarProceso()" + e);

		}

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
			Anular anu = new Anular();
			for (int a = 4; a <= numFila; a++) {

				fila = hoja.getRow(a);
				if (fila != null) {

					idBcv = fila.getCell((short) 0); // columna compra o venta en codigo

					if (idBcv.equals("") || idBcv.equals(null)) {
						Errores += "Esta vacio el campo id, id : " + idBcv.toString() + "\n";
						validar = false;
					}

					if (validar) {

						String codigoAnulacion = anu.ProcesarAnuladas(idBcv.toString());
						if (codigoAnulacion.equalsIgnoreCase("2"))
							noRegistrada++;
						idContadorMe++;
						Errores += "Resumen de la operacion  ID " + idBcv + ": Respuesta :" + codigoAnulacion + "\n";
					}

				}

			}
			documento.close();
			Logger.info(this, "Contador :" + idContadorMe);
			System.out.println("Contador :" + idContadorMe);
			if (noRegistrada == 0) {
				proceso.agregarDescripcionError("Todo las operaciones fueron anuladas");
				Errores = "Todas las operaciones fueron anuladas";
			} else {
				proceso.agregarDescripcionError("Operaciones fallidas :" + noRegistrada + " : " + Errores);
			}

		} catch (Exception e) {
			System.out.println("LeerAnulacionMesa : leerAnular() " + e);
			Logger.error(this, "LeerAnulacionMesa : leerAnular() " + e);
			noRegistrada++;
		} finally {
			finalizarProceso();

		}
	}

}

package models.intercambio.consultas.ciclos;

import java.sql.ResultSet;
import java.sql.Statement;

import models.exportable.ExportableOutputStream;

import org.apache.log4j.Logger;

import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.dao.Transaccion;

public class ConsultaCicloDetalle extends ExportableOutputStream {
	
	Logger logger = null;
	protected String separador = "\t|";
	ControlArchivoDAO controlArchivoDAO = null;
	protected Statement statement = null;
	protected ResultSet operaciones = null;	
	
	public void execute() throws Exception {
		Transaccion transaccion = new Transaccion(this._dso);
		controlArchivoDAO = new ControlArchivoDAO(_dso);

		try {
			transaccion.begin();
			statement = transaccion.getConnection().createStatement();
			getOperaciones();
			registrarInicio("detalle" + getFechaHora() + ".txt");
			crearCabecera();
			while (operaciones.next()) {
				escribir(operaciones.getString("client_nombre"), 35);
				escribir(separador);
				escribir(operaciones.getString("tipper_id") + "-" + operaciones.getString("client_cedrif"), 15);
				escribir(separador);
				escribir(operaciones.getString("ctecta_numero"), 25);
				escribir(separador);
				escribir(operaciones.getString("ordene_id"), 10);
				escribir(separador);
				escribir(operaciones.getString("ordene_id_relacion"), 15);
				escribir(separador);
				escribir(operaciones.getString("trnf_tipo") + "-" + (operaciones.getString("in_comision").equals("1")?"Comisión":""), 15);
				escribir(separador);
				escribir(operaciones.getString("codigo_operacion"), 20);
				escribir(separador);
				escribir(operaciones.getString("numero_retencion"), 20);
				escribir(separador);
				escribir(operaciones.getDouble("monto_operacion"), 20);
				escribir(separador);
				escribir(operaciones.getString("status"), 25);
				escribir(separador);
				escribir(operaciones.getString("undinv_nombre"), 40);
				escribir(separador);
				escribir(operaciones.getString("error"), 40);
				escribir("\r\n");				
			}
			registrarFin();
			obtenerSalida();
		} catch (Exception ex) {
			//System.out.println("Error en el proceso de generación archivo batch para adjudicación tipo subasta. " + ex.getMessage());
			logger.error("Error en el proceso de generación archivo batch para adjudicación tipo subasta. " + ex.getMessage(), ex);
		} finally {
			try {
				if (operaciones != null) {
					operaciones.close();
				}
				if (statement != null) {
					statement.close();
				}
				transaccion.closeConnection();
			} catch (Exception e) {
				logger.error("Error efectuando modificación al proceso. " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * Obtiene las operaciones a procesar
	 * @throws Exception en caso de error
	 */
	protected void getOperaciones() throws Exception{
		operaciones = statement.executeQuery(controlArchivoDAO.listarDetalleDelCiclo(Integer.parseInt(_record.getValue("idEjecucion")),getCiclo()));
	}

	protected void crearCabecera() throws Exception {
		escribir("CLIENTE", 35);
		escribir(separador);
		escribir("CÉDULA", 15);
		escribir(separador);
		escribir("NRO DE CUENTA", 25);
		escribir(separador);
		escribir("ORDEN", 10);
		escribir(separador);
		escribir("ORDEN ASOCIADA", 15);						
		escribir(separador);
		escribir("TIPO", 15);
		escribir(separador);
		escribir("COD. OPERACIÓN", 20);
		escribir(separador);
		escribir("Nro. RETENCIÓN", 20);
		escribir(separador);
		escribir("MONTO", 20);
		escribir(separador);
		escribir("STATUS", 25);
		escribir(separador);
		escribir("UNIDAD DE INVERSIÓN", 40);
		escribir(separador);
		escribir("ERROR\r\n");
		escribir("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\r\n");
	}	
	
	protected String getCiclo() throws Exception{
		return null;
	}
	
	protected Logger getLogger(){
		return Logger.getLogger(ConsultaCicloDetalle.class);
	}
}

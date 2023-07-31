package models.intercambio.consultas.detalle;

import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.bdv.infi.dao.Transaccion;
import com.bdv.infi.dao.UnidadInversionDAO;

import models.exportable.ExportableOutputStream;

public class DetalleDeOperacionesAbonoMonedaExtranjera extends ExportableOutputStream{


	Logger logger = Logger.getLogger(DetalleDeOperacionesAbonoMonedaExtranjera.class);
	/**
	 * Ejecuta la transaccion del modelo buscando las unidades de inversión
	 */
	protected String separador = "|";
	protected UnidadInversionDAO inversionDAO = null;
	protected Statement statement = null;
	protected ResultSet operaciones = null;	
	
	public void execute() throws Exception {
		Transaccion transaccion = new Transaccion(this._dso);
		inversionDAO = new UnidadInversionDAO(_dso);
		try {
			transaccion.begin();
			statement = transaccion.getConnection().createStatement();
			getOperaciones();
			registrarInicio("detalle" + getFechaHora() + ".txt");
			crearCabecera();
			while (operaciones.next()) {
				escribir(operaciones.getString("client_nombre"), 50);
				escribir(separador);
				escribir(operaciones.getString("tipper_id") + "-" + operaciones.getString("client_cedrif"), 15);
				escribir(separador);
				escribir(operaciones.getString("ctecta_numero"), 25);
				escribir(separador);				
				escribir(operaciones.getString("ordene_id_relacion"), 10);
				escribir(separador);
				escribir(operaciones.getString("trnf_tipo"), 5);
				escribir(separador);
				escribir(operaciones.getString("codigo_operacion"), 20);
				escribir(separador);
				escribir(operaciones.getString("numero_retencion"), 20);
				escribir(separador);
				escribir(operaciones.getDouble("monto_operacion"), 20);
				escribir(separador);
				escribir(operaciones.getString("status_operacion"), 25);
				escribir(separador);
				escribir(operaciones.getString("undinv_nombre"), 40);
				escribir(separador);
				escribir("\r\n");

			}
			registrarFin();
			obtenerSalida();
		} catch (Exception ex) {
			logger.error("Error en el proceso de generación archivo batch para Abono cuenta en Dolares tipo Sitme. " + ex.getMessage(), ex);
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
		operaciones = statement.executeQuery(inversionDAO.detalleDeAbonoCuentaDolares(Integer.parseInt(_record.getValue("undinv_id")),null, true));
	}

	protected void crearCabecera() throws Exception {
		escribir("CLIENTE", 50);
		escribir(separador);
		escribir("CÉDULA", 15);
		escribir(separador);
		escribir("NRO DE CUENTA", 25);
		escribir(separador);
		escribir("ORDEN", 10);
		escribir(separador);
		escribir("TIPO", 5);
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
}

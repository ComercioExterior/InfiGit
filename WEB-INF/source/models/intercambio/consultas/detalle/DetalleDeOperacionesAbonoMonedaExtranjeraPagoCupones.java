package models.intercambio.consultas.detalle;

import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.bdv.infi.dao.Transaccion;
import com.bdv.infi.dao.OrdenDAO;

import models.exportable.ExportableOutputStream;

public class DetalleDeOperacionesAbonoMonedaExtranjeraPagoCupones extends ExportableOutputStream{


	Logger logger = Logger.getLogger(DetalleDeOperacionesAbonoMonedaExtranjeraPagoCupones.class);
	/**
	 * Ejecuta la transaccion del modelo buscando las unidades de inversi�n
	 */
	protected String separador = "|";
	protected OrdenDAO ordenDAO = null;
	protected Statement statement = null;
	protected ResultSet operaciones = null;	
	
	public void execute() throws Exception {
	
		Transaccion transaccion = new Transaccion(this._dso);
		ordenDAO = new OrdenDAO(_dso);
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
				escribir(operaciones.getString("ordene_id"), 10);
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
				escribir("\r\n");
			}
			registrarFin();
			obtenerSalida();
		} catch (Exception ex) {
			logger.error("Error en el proceso de generaci�n archivo batch para Abono Cuenta en Dolares Pago Cupones. " + ex.getMessage(), ex);
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
				logger.error("Error efectuando modificaci�n al proceso. " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * Obtiene las operaciones a procesar
	 * @throws Exception en caso de error
	 */
	protected void getOperaciones() throws Exception{
		operaciones = statement.executeQuery(ordenDAO.detalleDeAbonoCuentaDolaresPagoCupones());
	}

	protected void crearCabecera() throws Exception {
		escribir("CLIENTE", 50);
		escribir(separador);
		escribir("C�DULA", 15);
		escribir(separador);
		escribir("NRO DE CUENTA", 25);
		escribir(separador);
		escribir("ORDEN", 10);
		escribir(separador);
		escribir("TIPO", 5);
		escribir(separador);
		escribir("COD. OPERACI�N", 20);
		escribir(separador);
		escribir("Nro. RETENCI�N", 20);
		escribir(separador);
		escribir("MONTO", 20);
		escribir(separador);
		escribir("STATUS", 25);		
		escribir(separador);		
		escribir("ERROR\r\n");
		escribir("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\r\n");
	}
}

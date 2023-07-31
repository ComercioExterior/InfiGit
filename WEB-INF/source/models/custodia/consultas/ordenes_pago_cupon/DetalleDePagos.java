package models.custodia.consultas.ordenes_pago_cupon;

import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.bdv.infi.dao.Transaccion;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

import models.exportable.ExportableOutputStream;

public class DetalleDePagos extends ExportableOutputStream{


	Logger logger = Logger.getLogger(DetalleDePagos.class);
	/**
	 * Ejecuta la transaccion del modelo buscando las unidades de inversión
	 */
	protected String separador = ";";
	protected OrdenDAO ordenDAO = null;
	protected Statement statement = null;
	protected ResultSet operaciones = null;	
	protected ResultSet operacionesAsociadas = null;
	
	public void execute() throws Exception {
		Transaccion transaccion = new Transaccion(this._dso);
		ordenDAO = new OrdenDAO(_dso);
		try {
			transaccion.begin();
			statement = transaccion.getConnection().createStatement();
			getOperaciones();
			registrarInicio("detallePagos" + getFechaHora() + ".csv");
			crearCabecera();
			
			while (operaciones.next()) {			
				
				escribir(operaciones.getString("ordene_id"));
				escribir(separador);
				escribir(operaciones.getString("tipper_id") + "-" + operaciones.getString("client_cedrif"));
				escribir(separador);
				escribir(operaciones.getString("client_nombre"));
				escribir(separador);				
				escribir(operaciones.getString("transa_id"));
				escribir(separador);
				escribir(operaciones.getString("monto_operacion"));
				escribir(separador);
				escribir(operaciones.getString("status_operacion"));
				escribir(separador);
				escribir(operaciones.getString("moneda_id"));
				escribir(separador);
				escribir(operaciones.getString("instruccion_pago"));
				escribir(separador);
				escribir(operaciones.getString("banco_intermediario"));
				escribir(separador);
				escribir(operaciones.getString("fecha_aplicar"));
				escribir(separador);
				
				try {
					statement = transaccion.getConnection().createStatement();
					operacionesAsociadas = statement.executeQuery(ordenDAO.listarDetalleOrdenPagoAsociada(operaciones.getString("ordene_id")));
					while (operacionesAsociadas.next()) {						
						escribir("\r\n"+separador+separador+separador+separador+separador+separador+separador+separador+separador+separador);
						
						escribir(operacionesAsociadas.getString("ordene_id"));
						escribir(separador);
						escribir(operacionesAsociadas.getString("titulo_id"));
						escribir(separador);
						escribir(operacionesAsociadas.getString("transa_id"));
						escribir(separador);
						escribir(operacionesAsociadas.getString("monto_operacion"));
						escribir(separador);
						escribir(operacionesAsociadas.getString("status_operacion"));
						escribir(separador);						
					}
				
				} catch (RuntimeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				escribir("\r\n");

			}
			registrarFin();
			obtenerSalida();
		} catch (Exception ex) {
			logger.error("Error en el proceso de generación archivo batch para Abono Cuenta en Dolares Venta Títulos. " + ex.getMessage(), ex);
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
		operaciones = statement.executeQuery(ordenDAO.listarDetalleOrdenPago(_req.getParameter("client_id"),null,_req.getParameter("fe_transac_desde"),_req.getParameter("fe_transac_hasta"),_req.getParameter("status_operacion"),TransaccionNegocio.ORDEN_PAGO));
	}

	protected void crearCabecera() throws Exception {
		escribir("ID ORDEN PAGO");
		escribir(separador);
		escribir("CÉDULA-RIF");
		escribir(separador);
		escribir("MOMBRE DEL CLIENTE");
		escribir(separador);
		escribir("TRANSACCIÓN");
		escribir(separador);
		escribir("MONTO");
		escribir(separador);
		escribir("ESTATUS");
		escribir(separador);
		escribir("MONEDA");
		escribir(separador);
		escribir("INSTRUCCION DE PAGO");
		escribir(separador);
		escribir("BANCO INTERMEDIARIO");
		escribir(separador);
		escribir("FECHA VALOR");
		escribir(separador);
		escribir("ID ORDEN CUPÓN");
		escribir(separador);
		escribir("TÍTULO");
		escribir(separador);
		escribir("TRANSACCIÓN");
		escribir(separador);
		escribir("MONTO");
		escribir(separador);
		escribir("ESTATUS");
		escribir(separador+"\r\n");
		
	}
}

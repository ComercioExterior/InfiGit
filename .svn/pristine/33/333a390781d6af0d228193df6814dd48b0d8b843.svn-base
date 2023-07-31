package models.intercambio.recepcion.lectura_archivo_subasta_divisas;

import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import megasoft.DataSet;
import megasoft.Logger;
import models.exportable.ExportableOutputStream;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.Transaccion;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

/**
 * Clase que exporta a excel la ordenes que furon adjudicadas o quedaron en proceso de adjudicacion
 * 
 * @author jvillegas
 */
public class InformeExcel extends ExportableOutputStream {

	DataSet camposDinamicos = new DataSet();
	DataSet titulos = new DataSet();

	public void execute() throws Exception {

		Transaccion transaccion = new Transaccion(_dso);
		Statement statement = null;
		ResultSet _exportar = null;
		OrdenDAO ordenDAO = new OrdenDAO(_dso);
		String idEjecucion = "";

		if (getSessionObject("id_ejecucion_subasta_divisas") != null && getSessionObject("id_ejecucion_subasta_divisas") != "") {
			idEjecucion = getSessionObject("id_ejecucion_subasta_divisas").toString();

		}

		System.out.println("********************* ID EJECUCION: " + idEjecucion + " ****************************");
		SimpleDateFormat formato = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA3);

		try {

			transaccion.begin();
			statement = transaccion.getConnection().createStatement();

			if (idEjecucion != "" && !idEjecucion.equals("")) {

				_exportar = statement.executeQuery(ordenDAO.listarOrdenesPorIdEjecucion(idEjecucion));
			}
			registrarInicio(obtenerNombreArchivo("InformeAdjSubastaDivisas"));
			crearCabecera();
			while (_exportar.next()) {
				escribir(_exportar.getString("ordene_id"));
				escribir(";");
				escribir(_exportar.getString("undinv_nombre"));
				escribir(";");
				escribir(_exportar.getString("client_nombre"));
				escribir(";");
				escribir(_exportar.getString("ordsta_nombre"));
				escribir(";");
				escribir(formato.format(_exportar.getDate("ordene_ped_fe_orden")));
				escribir(";");
				escribir(_exportar.getDouble("ordene_ped_monto"));
				escribir(";");
				escribir(_exportar.getDouble("ordene_ped_precio"));
				escribir(";");
				escribir(_exportar.getDouble("ordene_adj_monto"));
				escribir(";");
				escribir(_exportar.getDouble("ordene_tasa_pool"));
				escribir(";");
				escribir(_exportar.getDouble("ordene_tasa_cambio"));
				escribir(";");
				if (_exportar.getDate("ordene_fecha_adjudicacion") != null) {
					escribir(formato.format(_exportar.getDate("ordene_fecha_adjudicacion")));
				} else {
					escribir("");
				}
				escribir(";\r\n");
			}
			registrarFin();
			obtenerSalida();

		} catch (Exception e) {
			Logger.error(this, "Error en la generación del informe de excel de adjudicación subasta divisas", e);
			throw new Exception("Error en la generación del informe de excel de adjudicación subasta divisas", e);
		} finally {
			if (_exportar != null) {
				_exportar.close();
			}
			if (statement != null) {
				statement.close();
			}
			if (transaccion != null) {
				transaccion.closeConnection();
			}
		}
	}

	protected void crearCabecera() throws Exception {
		escribir("Orden;Unidad de Inversión;Cliente;Status de la Orden;Fecha Orden;Monto Pedido;Precio Compra;Monto Adjudicado;Tasa Cambio Propuesta;Tasa Cambio Adjudicada;Fecha Adjudicacion;\r\n".toUpperCase());
	}

	protected void crearCabeceraSitmeClaveNet() throws Exception {
		escribir("Orden;Unidad de Inversión;Cliente;Status de la Orden;Fecha Orden;Monto Pedido;Precio Compra;Monto Adjudicado;Fecha Adjudicacion;Intereses Caidos USD;Precio Negociado;Fecha Valor;Nro Ticket;\r\n".toUpperCase());
	}
}
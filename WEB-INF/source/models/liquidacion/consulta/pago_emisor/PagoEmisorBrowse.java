package models.liquidacion.consulta.pago_emisor;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import megasoft.DataSet;
import megasoft.Logger;
import models.msc_utilitys.*;

import com.bdv.infi.dao.Transaccion;
import com.bdv.infi.dao.UnidadInversionDAO;

/**
 * 
 * @author elaucho
 * 
 */
public class PagoEmisorBrowse extends MSCModelExtend {

	// Variables para totalizar las ordenes por cobrado,pendiente,ordenes y monto

	BigDecimal totalAdjudicado = new BigDecimal(0);
	BigDecimal totalComision = new BigDecimal(0);
	BigDecimal totalCobrado = new BigDecimal(0);
	BigDecimal totalPendiente = new BigDecimal(0);
	BigDecimal totalFinanciamiento = new BigDecimal(0);
	DecimalFormat dFormato1 = new DecimalFormat("###,###,###,##0.00");
	Statement statement = null;
	ResultSet ordenes = null;

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		Transaccion transaccion = new Transaccion(this._dso);
		
		// Variables a utilizar
		String blotter = null;
		long unidadInversion = 0;

		// DAO a utilizar
		UnidadInversionDAO unidadInversionDAO = new UnidadInversionDAO(_dso);

		// Verificamos los valores que vienen por el request
		if (_req.getParameter("unidad_inversion") != null)
			unidadInversion = Long.parseLong(_req.getParameter("unidad_inversion"));

		if (_req.getParameter("blotter") != null && !_req.getParameter("blotter").equals(""))
			blotter = _req.getParameter("blotter");

		try {
			transaccion.begin();
			// Listamos los datos
			statement = transaccion.getConnection().createStatement();
			ordenes = statement.executeQuery(unidadInversionDAO.listarConciliacionCobranza(unidadInversion, blotter));


			// Publicamos el DataSet
			storeDataSet("conciliacion", unidadInversionDAO.getDataSet());
			storeDataSet("request", getDataSetFromRequest());

			// Se buscan los totales
			DataSet _totales = new DataSet();
			_totales = this.totales();

			// Publicamos el DataSet
			storeDataSet("totales", _totales);

			unidadInversionDAO.listadescripcion(unidadInversion);
			storeDataSet("nombre_unidad", unidadInversionDAO.getDataSet());
		} catch (Exception e) {
			Logger.error(this, "Error procesando la consulta de conciliación", e);
			throw new Exception("Error procesando la consulta de conciliación");
		} finally {
			if (ordenes != null) {
				ordenes.close();
			}
			if (statement != null) {
				statement.close();
			}
			if (transaccion != null) {
				transaccion.closeConnection();
			}
		}

	}// fin execute

	/**
	 * Crea un dataset con totales
	 * 
	 * @param DataSet
	 *            ordenes
	 * @return DataSet
	 * @throws Exception
	 *             en caso de haber problemas
	 */
	public DataSet totales() throws Exception {

		boolean hayRegistros = false;

		// Dataset a retornar
		DataSet totales = new DataSet();
		while (ordenes.next()) {
			hayRegistros = true;
			// Se totalizan los campos de la consulta especificada
			totalAdjudicado = totalAdjudicado.add(ordenes.getBigDecimal("adjudicado") == null ? new BigDecimal(0) : new BigDecimal(ordenes.getDouble("adjudicado")));
			totalComision = totalComision.add(ordenes.getBigDecimal("comision") == null ? new BigDecimal(0) : new BigDecimal(ordenes.getDouble("comision")));
			totalCobrado = totalCobrado.add(ordenes.getBigDecimal("cobrado") == null ? new BigDecimal(0) : new BigDecimal(ordenes.getDouble("cobrado")));
			totalPendiente = totalPendiente.add(ordenes.getBigDecimal("pendiente") == null ? new BigDecimal(0) : new BigDecimal(ordenes.getDouble("pendiente")));
			totalFinanciamiento = totalFinanciamiento.add(ordenes.getBigDecimal("financiado") == null ? new BigDecimal(0) : new BigDecimal(ordenes.getDouble("financiado")));
		}// fin while

		if (hayRegistros) {
			// Se crea e insertan valores en el dataset
			totales.append("total_adjudicado", java.sql.Types.VARCHAR);
			totales.append("total_comision", java.sql.Types.VARCHAR);
			totales.append("total_cobrado", java.sql.Types.VARCHAR);
			totales.append("total_pendiente", java.sql.Types.VARCHAR);
			totales.append("total_financiado", java.sql.Types.VARCHAR);
			totales.addNew();

			totales.setValue("total_adjudicado", "<b>" + String.valueOf(dFormato1.format(totalAdjudicado.setScale(3, BigDecimal.ROUND_HALF_EVEN)) + "</b>"));
			totales.setValue("total_comision", "<b>" + String.valueOf(dFormato1.format(totalComision.setScale(3, BigDecimal.ROUND_HALF_EVEN)) + "</b>"));
			totales.setValue("total_cobrado", "<b>" + String.valueOf(dFormato1.format(totalCobrado.setScale(3, BigDecimal.ROUND_HALF_EVEN)) + "</b>"));
			totales.setValue("total_pendiente", "<b>" + String.valueOf(dFormato1.format(totalPendiente.setScale(3, BigDecimal.ROUND_HALF_EVEN)) + "</b>"));
			totales.setValue("total_financiado", "<b>" + String.valueOf(dFormato1.format(totalFinanciamiento.setScale(3, BigDecimal.ROUND_HALF_EVEN)) + "</b>"));
		} else {
			totales.append("total_adjudicado", java.sql.Types.VARCHAR);
			totales.append("total_comision", java.sql.Types.VARCHAR);
			totales.append("total_cobrado", java.sql.Types.VARCHAR);
			totales.append("total_pendiente", java.sql.Types.VARCHAR);
			totales.append("total_financiado", java.sql.Types.VARCHAR);
			totales.addNew();

			totales.setValue("total_adjudicado", "");
			totales.setValue("total_comision", "");
			totales.setValue("total_cobrado", "");
			totales.setValue("total_pendiente", "");
			totales.setValue("total_financiado", "");

		}

		return totales;
	}// fin metodo
	
}// fin PagoEmisorBrowse
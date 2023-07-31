package com.bdv.infi.model.conciliacion;

import javax.sql.DataSource;
import megasoft.AppProperties;
import megasoft.Logger;
import megasoft.db;
import com.bdv.infi.dao.ConciliacionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.model.menudeo.Anular;
import megasoft.DataSet;
import java.util.List;

public class Operaciones {

	protected DataSource _dso;

	public Operaciones() {

		try {
			_dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));
		} catch (Exception e) {
			System.out.println("Operaciones : Operaciones() " + e);
			Logger.error(this, "Operaciones : Operaciones() " + e);
		}
	}

	/**
	 * Registar las operaciones anuladas por venta y compra de divisas Individual
	 * 
	 * @param lst
	 *            List<String>
	 * @return
	 */
	public boolean Salvar(List<String> lst) {
		try {
			ConciliacionDAO conc = new ConciliacionDAO(_dso);
			StringBuffer sql = new StringBuffer();
			sql.append(conc.insertarLote(lst));
			System.out.println("ConciliacionDAO:insertarLote() ");

			db.exec(_dso, sql.toString());

		} catch (Exception e) {

			System.out.println("Operaciones : Salvar() " + e);
			Logger.error(this, "Operaciones : Salvar() " + e);
		}

		return true;
	}

	public void AnularPorItem(String fecha, String id) {
System.out.println("llego AnularPorItem");
		String Comentario = "Anulacion por conciliacion";
		String IdOperacionBCV = "";
		String IdOperacion = "";

		ConciliacionDAO conc = new ConciliacionDAO(_dso);

		if (id != null) {
			conc.listarOrdenesPorEnviarAnuladar(id);
		} else {
			System.out.println("fecha : " + fecha);
			conc.listarOrdenesAnuladasMenudeoLote(fecha);
		}

		DataSet _ordenes = conc.getDataSet();
		Anular anular = new Anular();

		try {

			while (_ordenes.next()) {

				IdOperacionBCV = _ordenes.getValue("ID_OPERACION");
				IdOperacion = _ordenes.getValue("ID");
				String IdRechazo = anular.ProcesarAnuladas(IdOperacionBCV, Comentario);
				System.out.println("IdRechazoIdRechazo : " + IdRechazo);
				conc.ModificarOrdenesAnuladasMenudeo("1", IdRechazo, IdOperacion); // 1 Estatus Enviado

			}
		} catch (Exception e) {
			conc.ModificarOrdenesAnuladasMenudeo("4", "", IdOperacion); // 4 Estatus Rechazada
			System.out.println("Operaciones : AnularPorItem() " + e);
			Logger.error(this, "Operaciones : AnularPorItem() " + e);

		}

	}
}
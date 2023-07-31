package models.custodia.transacciones.bloqueo_titulos;

import com.bdv.infi.dao.BeneficiarioDAO;
import com.bdv.infi.dao.CustodiaDAO;
import com.bdv.infi.dao.TipoBloqueoDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

import megasoft.AbstractModel;
import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

/**
 * Clase encargada de recuperar datos necesarios para el Bloqueo de T&iacute;tulos.
 * 
 * @author Erika Valerio, Megasoft Computaci&oacute;n, Nelson Visbal
 */
public class Bloquear extends AbstractModel {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		DataSet _dsparam = getDataSetFromRequest();
		DataSet _datos = new DataSet();// dataSet para Datos especiales
		_datos.append("fecha_hoy", java.sql.Types.VARCHAR);
		_datos.append("tipo_producto", java.sql.Types.VARCHAR);

		MSCModelExtend me = new MSCModelExtend();
		TipoBloqueoDAO tipoBloqueoDAO = new TipoBloqueoDAO(_dso);
		BeneficiarioDAO beneficiarioDAO = new BeneficiarioDAO(_dso);
		CustodiaDAO custodiaDAO = new CustodiaDAO(_dso);
		tipoBloqueoDAO.listarPorStatus(String.valueOf(ConstantesGenerales.STATUS_ACTIVO));
		beneficiarioDAO.listar();
		custodiaDAO.listarTitulos(Long.parseLong(_req.getParameter("client_id")), _req.getParameter("titulo_id"), _req.getParameter("tipo_producto"));
		_datos.addNew();
		_datos.setValue("fecha_hoy", me.getFechaHoyFormateada(ConstantesGenerales.FORMATO_FECHA));
		_datos.setValue("tipo_producto", _req.getParameter("tipo_producto"));
		// registrar los datasets exportados por este modelo
		storeDataSet("tipo_bloq", tipoBloqueoDAO.getDataSet());
		storeDataSet("beneficiarios", beneficiarioDAO.getDataSet());
		storeDataSet("ds_titulo_custodia", custodiaDAO.getDataSet());
		storeDataSet("datos", _datos);
		storeDataSet("dsparam", _dsparam);
	}

	@Override
	public boolean isValid() throws Exception {
		boolean valido = true;
		if (_req.getParameter("tipo_producto") == null) {
			_record.addError("Tipo de producto", "No se está recibiendo información del tipo de producto");
			valido = false;
		}
		if (_req.getParameter("client_id") == null) {
			_record.addError("Cliente", "No se está recibiendo información del cliente");
			valido = false;
		}
		if (_req.getParameter("titulo_id") == null) {
			_record.addError("Título", "No se está recibiendo información del título");
			valido = false;
		}
		return valido;
	}
}

package models.custodia.transacciones.desbloqueo_titulos;

import megasoft.AbstractModel;
import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import org.apache.log4j.Logger;

import com.bdv.infi.dao.TitulosBloqueoDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TipoBloqueos;

/**
 * Clase encargada de recuperar datos necesarios para el Desbloqueo de T&iacute;tulos.
 * 
 * @author Erika Valerio, Megasoft Computaci&oacute;n
 */
public class Desbloquear extends AbstractModel {
	private Logger logger = Logger.getLogger(Desbloquear.class);
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		try{
			DataSet _datos = new DataSet();// dataSet para Datos especiales
			_datos.append("fecha_hoy", java.sql.Types.VARCHAR);

			MSCModelExtend me = new MSCModelExtend();
			DataSet _dsparam = getDataSetFromRequest();

			TitulosBloqueoDAO titulosBloqueoDAO = new TitulosBloqueoDAO(_dso);	
			titulosBloqueoDAO.obtenerBloqueo(_record.getValue("titulo_id"), Long.parseLong(_record.getValue("client_id")), _record.getValue("tipblo_id"), _record.getValue("beneficiario_id"), _record.getValue("tipo_producto"));
			_datos.addNew();
	
			_datos.setValue("fecha_hoy", me.getFechaHoyFormateada(ConstantesGenerales.FORMATO_FECHA));
			storeDataSet("datos_bloqueo", titulosBloqueoDAO.getDataSet());
			storeDataSet("dsparam", _dsparam);
			storeDataSet("datos", _datos);
		}catch(Exception e){
			logger.error("Error: "+e.getMessage());
			throw e;
		}

	}

	/**
	 * Validaciones Basicas del action
	 * 
	 * @return true si la clase y sus par&aacute;metros son v&aacute;lidos, false en caso contrario
	 * @throws Exception
	 */
	public boolean isValid() throws Exception {
		boolean flag = super.isValid();

		if (flag) {
			if (_req.getParameter("tipblo_id").equals(TipoBloqueos.BLOQUEO_FINANCIAMIENTO)) {
				_record.addError("Custodia / Transacciones / Desbloqueo de T&iacute;tulos", "No es posible desbloquear el T&iacute;tulo " + _req.getParameter("titulo_id") + ", puesto que ha sido bloqueado por financiamiento");
				flag = false;
			}

		}
		return flag;
	}

}

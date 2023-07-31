package models.ordenes.cancelacion;

import com.bdv.infi.dao.OrdenDAO;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

public class Confirm extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		OrdenDAO ordenDAO = new OrdenDAO(_dso);		
		DataSet _filter = getDataSetFromRequest();
		DataSet _datos = new DataSet();
		
		_datos.append("cancelacion_posible", java.sql.Types.VARCHAR);
		_datos.addNew();
		
		/* Valida que la cancelación se haga el mismo día de la toma de orden, en el rango de horarios y fecha configurados 
		 * a unidades de inversión en estatus 'PUBLICADA' o por algún usuario especial. NM25287 
		 */	
		if(!ordenDAO.validarCancelacionOrden(_filter.getValue("ordene_id"), _filter.getValue("undinv_id"), getUserName())){ 
			_datos.setValue("cancelacion_posible", "false");		
		}else{
			_datos.setValue("cancelacion_posible", "true");			
		}			
		
		//registrar los datasets exportados por este modelo
		storeDataSet("filter", _filter);
		storeDataSet("validacion", _datos);
	}
}

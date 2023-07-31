package models.carga_final_clientes_titulos;

import megasoft.AbstractModel;
import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

public class AjaxTable extends AbstractModel {
	
	/**DataSets del Modelo*/
	private DataSet _datos = null;
	
	
	public void execute() throws Exception {	
		
		MSCModelExtend mscME = new MSCModelExtend();
		String fecha_inicio ="";
		
		_datos = new DataSet();
		_datos.append("fecha_inicio", java.sql.Types.VARCHAR);			
		
		fecha_inicio = mscME.getFechaHoyFormateada("dd/MM/yyyy - HH:mm:ss");
		
		_datos.addNew();
		_datos.setValue("fecha_inicio", fecha_inicio);		
		
		_req.getSession().setAttribute("fecha_inicio", fecha_inicio);
		
		
		storeDataSet("datos", _datos);
		
	}

}

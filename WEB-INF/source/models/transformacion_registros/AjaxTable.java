package models.transformacion_registros;

import megasoft.AbstractModel;
import megasoft.DataSet;
import megasoft.db;

public class AjaxTable extends AbstractModel {
	
	/**DataSets del Modelo*/
	private DataSet _datos = null;
	private DataSet _aux = null;
	
	public void execute() throws Exception {	
		
		String fecha_inicio ="";		
		Procesar lar = new Procesar();
		String cod_mapa = "00", numero_proceso = "00";
		
		_datos = new DataSet();
		_datos.append("fecha_inicio", java.sql.Types.VARCHAR);
		_datos.append("num_campos", java.sql.Types.VARCHAR);

		if(_req.getParameter("cod_proceso")!=null & !_req.getParameter("cod_proceso").equals(""))
			numero_proceso = _req.getParameter("cod_proceso");
		
		String sql = "select count(*) as conteo, to_char(sysdate, 'dd/mm/yyyy hh24:mi:ss') as fecha from INFI_TB_Z12_REGISTROS where Z11_COD_PROCESO="+ numero_proceso;
		_aux = db.get(_dso, sql);
		
		//fecha_inicio = lar.getFechaHoyFormateada("dd/MM/yyyy - HH:mm:ss");
		
		_datos.addNew();
		//_datos.setValue("fecha_inicio", fecha_inicio);		
		 
		//_req.getSession().setAttribute("fecha_inicio", fecha_inicio);
		
		if(_aux.next())
			_datos.setValue("num_campos", _aux.getValue("conteo"));
			_datos.setValue("fecha_inicio", _aux.getValue("fecha"));
		storeDataSet("datos", _datos);
		
	}

}

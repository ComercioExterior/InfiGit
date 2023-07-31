package models.carga_inicial;

import megasoft.AbstractModel;
import megasoft.DataSet;
import megasoft.db;

public class AjaxTable extends AbstractModel {
	
	/**DataSets del Modelo*/
	private DataSet _datos = null;
	private DataSet _aux = null;
	
	public void execute() throws Exception {	
		
		String fecha_inicio ="";		
		LeerArchivo lar = new LeerArchivo();
		String cod_mapa = "00";
		
		_datos = new DataSet();
		_datos.append("fecha_inicio", java.sql.Types.VARCHAR);
		_datos.append("num_campos", java.sql.Types.VARCHAR);

		if(_req.getParameter("cod_mapa")!=null & !_req.getParameter("cod_mapa").equals(""))
			cod_mapa = _req.getParameter("cod_mapa"); 
		
		String sql = "select z00_num_campos from infi_tb_z00_mapas where z00_cod_mapa="+ cod_mapa;
		_aux = db.get(_dso, sql);
		
		fecha_inicio = lar.getFechaHoyFormateada("dd/MM/yyyy - HH:mm:ss");
		
		_datos.addNew();
		_datos.setValue("fecha_inicio", fecha_inicio);		
		
		_req.getSession().setAttribute("fecha_inicio", fecha_inicio);
		
		if(_aux.next())
			_datos.setValue("num_campos", _aux.getValue("z00_num_campos"));
		
		storeDataSet("datos", _datos);
		
	}

}

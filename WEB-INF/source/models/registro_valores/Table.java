package models.registro_valores;

import megasoft.*;
import models.msc_utilitys.*;

public class Table extends MSCModelExtend
{

	/** DataSet del modelo */    
	private DataSet _archivos = null;
	private DataSet _gruposvalores = null;
	private DataSet _conteogruposvalores = null;
	

	public void execute() throws Exception
	{
		String sql = "", sqlgrupo="", sqlconteo=""; 
		sql = getResource("select_grupo.sql");
		_archivos = db.get(_dso, sql);
		
		_gruposvalores = new DataSet();
		_conteogruposvalores = new DataSet();
		
		if(_req.getParameter("band")!=null ){
			
			if (!_req.getParameter("grupo_id").equals("TODOS")){
				sqlconteo = getResource("select_conteo_grupo.sql");
				sqlconteo = Util.replace(sqlconteo, "@grupo@", _req.getParameter("grupo_id"));
				_conteogruposvalores = db.get(_dso, sqlconteo);
				
				sqlgrupo = getResource("select_grupo_detalle.sql");
				sqlgrupo = Util.replace(sqlgrupo, "@grupo@", _req.getParameter("grupo_id"));
				_gruposvalores = db.get(_dso, sqlgrupo);
			}	
			else{
				sqlconteo = getResource("select_conteo_grupo1.sql");
				_conteogruposvalores = db.get(_dso, sqlconteo);
				
				sqlgrupo = getResource("select_grupo_total.sql");
				_gruposvalores = db.get(_dso, sqlgrupo);
			}
		}
		 
		storeDataSet( "archivos", _archivos );
		storeDataSet( "gruposvalores", _gruposvalores );
		storeDataSet( "conteovalores", _conteogruposvalores );
		storeDataSet( "record", _record );
	}
	public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();
			
		if (flag)
		{
			if(_req.getParameter("band")!=null ){
				if (_req.getParameter("grupo_id")== null || _req.getParameter("grupo_id").equals("")){
					_record.addError("MSG-CD100-0001", " Seleccione los parametros requeridos (grupo)");
					 flag = false;
				}
			}	
		 
		}
		return flag;	
	}
}
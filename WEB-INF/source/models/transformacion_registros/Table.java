package models.transformacion_registros;

import megasoft.*;
import models.msc_utilitys.*;

public class Table extends MSCModelExtend
{

	/** DataSet del modelo */    
	private DataSet _archivos = null;
	private DataSet _mapas = null;
	private DataSet _procesos = null;
	private DataSet _entidad = null;
	
	/**
	 * Esta clase esta asociada a la accion INFICD100-FILTRO.
	 * Esta clase permite recuperar toda la informaci&oacute;n necesaria para los combos que alimentan la busqueda. 
	 * Recupera informacion de la tabla INFI_TB_Z10_ARCHIVOS, INFI_TB_Z00_MAPAS, INFI_TB_Z11_PROCESOS,
	 * INFI_TB_Z01_ENTIDAD. 
	 * 
	 */
	public void execute() throws Exception
	{
		String sql = "", sqlmapas = "", sqlproceso = "", sqlentidad = ""; 
		sql = getResource("select_archivo.sql");
		_archivos = db.get(_dso, sql);
		storeDataSet( "archivos", _archivos );
		
		_mapas = new DataSet();
		_procesos = new DataSet();
		_entidad = new DataSet();
		

		if(_req.getParameter("band")!=null && _record.getValue("cod_archivo")!=null){
			sqlmapas = getResource("select_mapa.sql");
			sqlmapas = Util.replace(sqlmapas, "@cod_archivo@", _record.getValue("cod_archivo"));
			_mapas = db.get(_dso, sqlmapas);
			
			sqlproceso = getResource("select_proceso.sql"); 
			sqlproceso = Util.replace(sqlproceso, "@cod_archivo@", _record.getValue("cod_archivo"));
			_procesos = db.get(_dso, sqlproceso);
			
			
			sqlentidad = getResource("select_entidad.sql");
			sqlentidad = Util.replace(sqlentidad, "@cod_archivo@", _record.getValue("cod_archivo"));
			_entidad = db.get(_dso, sqlentidad);
		}
		
		storeDataSet("mapas", _mapas);
		storeDataSet("procesos", _procesos);
		storeDataSet("entidad", _entidad);
		storeDataSet( "record", _record );
	}
	
}
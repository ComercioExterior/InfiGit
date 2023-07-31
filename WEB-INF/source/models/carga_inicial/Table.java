package models.carga_inicial;

import megasoft.*;import models.msc_utilitys.*;

public class Table extends MSCModelExtend
{

	/** DataSet del modelo */    
	private DataSet _archivos = null;
	private DataSet _mapa = null;
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{
		String sql = "select z10_no_nombre_archivo, z10_co_codigo_archivo as cod_archivo from INFI_TB_Z10_ARCHIVOS";
		_archivos = db.get(_dso, sql);
	
		storeDataSet( "archivos", _archivos );
		
		_mapa = new DataSet();
		
		if(_req.getParameter("band")!=null && _record.getValue("num_archivo")!=null){
			sql = "select z00_nom_mapa, z00_cod_mapa as cod_mapa from INFI_TB_Z00_MAPAS where z10_co_codigo_archivo="+_record.getValue("num_archivo");
			
			_mapa = db.get(_dso, sql);
		}
		
		storeDataSet( "mapas", _mapa );
		storeDataSet( "record", _record );		

	}
}
package models.consultas_carga_inicial.carga_validacion;

import megasoft.*;import models.msc_utilitys.*;

public class Filter extends MSCModelExtend
{

	/** DataSet del modelo */    
	private DataSet _archivos = null;
	private DataSet _procesos = new DataSet();	
	private DataSet _mapas = new DataSet();	
	
	/** 
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{
		String sql = "select A.Z10_CO_CODIGO_ARCHIVO as cod_archivo, a.Z10_NO_NOMBRE_ARCHIVO as nom_archivo from INFI_TB_Z10_ARCHIVOS A ORDER BY A.Z10_CO_CODIGO_ARCHIVO ";
		String sql2 ="";
		
		_archivos = db.get(_dso, sql);	
		
		 
		if(_req.getParameter("band")!=null){
			sql = "select Z11_COD_PROCESO as id_proceso, Z11_NU_NUMERO_PROCESO, Z11_FE_FECHA_INICIO AS fec_inicio from INFI_TB_Z11_PROCESOS where Z10_CO_CODIGO_ARCHIVO="+_record.getValue("num_archivo")+"ORDER BY Z11_FE_FECHA_INICIO";
			sql2 = "select z00_nom_mapa as nom_mapa, z00_cod_mapa as cod_mapa from infi_tb_z00_mapas where z10_co_codigo_archivo ="+_record.getValue("num_archivo");
		}else{
			sql = "select Z11_COD_PROCESO as id_proceso, Z11_NU_NUMERO_PROCESO, Z11_FE_FECHA_INICIO AS fec_inicio from INFI_TB_Z11_PROCESOS where Z10_CO_CODIGO_ARCHIVO is null" ;
			sql2 = "select z00_nom_mapa as nom_mapa, z00_cod_mapa as cod_mapa from infi_tb_z00_mapas where z10_co_codigo_archivo is null";
		}
	
		_procesos = db.get(_dso, sql);
		_mapas = db.get(_dso, sql2);
		
		storeDataSet( "archivos", _archivos );
		storeDataSet( "procesos", _procesos );   
		storeDataSet( "mapas", _mapas );   
		storeDataSet("record", _record);

	} 

}
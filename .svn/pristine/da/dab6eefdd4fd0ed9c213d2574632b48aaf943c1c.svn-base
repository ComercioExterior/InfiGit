package models.exportacion_excel;

import megasoft.*;import models.msc_utilitys.*;

public class Table extends MSCModelExtend
{

	/** DataSet del modelo */    
	private DataSet _archivos = null;
	private DataSet _mapa = null;
	private DataSet _registro = null;
	private DataSet _conteo = null;
	
	/** 
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{
		String sql = "select A.Z10_CO_CODIGO_ARCHIVO as cod_archivo, a.Z10_NO_NOMBRE_ARCHIVO as nom_archivo from INFI_TB_Z10_ARCHIVOS A ORDER BY A.Z10_CO_CODIGO_ARCHIVO ";
		String sql2="", sqlConteo="";
		
		_archivos = db.get(_dso, sql);	
		
		 
		if(_record.getValue("band")!=null){
			sql = "select Z11_COD_PROCESO as id_proceso, Z11_FE_FECHA_INICIO AS fec_inicio from INFI_TB_Z11_PROCESOS where Z10_CO_CODIGO_ARCHIVO="+_record.getValue("num_archivo")+"ORDER BY Z11_FE_FECHA_INICIO";
		} 
		else{
			sql = "select Z11_COD_PROCESO as id_proceso, Z11_FE_FECHA_INICIO AS fec_inicio from INFI_TB_Z11_PROCESOS WHERE Z10_CO_CODIGO_ARCHIVO IS NULL";
		}
		if(_record.getValue("reporte")!=null){
			sql2 = "select a.Z12_NU_NUMERO_REGISTRO AS ID_REGISTRO_1, a.Z12_CAMPO1_VALOR AS VALOR_1, a.Z12_CAMPO1_RESULTADO AS RESULTADO_1, a.Z12_CAMPO1_MENSAJE as MENSAJE_1, " +
				   "a.Z12_NU_NUMERO_REGISTRO AS ID_REGISTRO_2, a.Z12_CAMPO2_VALOR AS VALOR_2, a.Z12_CAMPO2_RESULTADO AS RESULTADO_2, a.Z12_CAMPO2_MENSAJE as MENSAJE_2, "+
				   "a.Z12_NU_NUMERO_REGISTRO AS ID_REGISTRO_3, a.Z12_CAMPO3_VALOR AS VALOR_3, a.Z12_CAMPO3_RESULTADO AS RESULTADO_3, a.Z12_CAMPO3_MENSAJE as MENSAJE_3, "+
				   "a.Z12_NU_NUMERO_REGISTRO AS ID_REGISTRO_4, a.Z12_CAMPO4_VALOR AS VALOR_4, a.Z12_CAMPO4_RESULTADO AS RESULTADO_4, a.Z12_CAMPO4_MENSAJE as MENSAJE_4, "+
				   "a.Z12_NU_NUMERO_REGISTRO AS ID_REGISTRO_5, a.Z12_CAMPO5_VALOR AS VALOR_5, a.Z12_CAMPO5_RESULTADO AS RESULTADO_5, a.Z12_CAMPO5_MENSAJE as MENSAJE_5, "+
				   "a.Z12_NU_NUMERO_REGISTRO AS ID_REGISTRO_6, a.Z12_CAMPO6_VALOR AS VALOR_6, a.Z12_CAMPO6_RESULTADO AS RESULTADO_6, a.Z12_CAMPO6_MENSAJE as MENSAJE_6, "+
				   "a.Z12_NU_NUMERO_REGISTRO AS ID_REGISTRO_7, a.Z12_CAMPO7_VALOR AS VALOR_7, a.Z12_CAMPO7_RESULTADO AS RESULTADO_7, a.Z12_CAMPO7_MENSAJE as MENSAJE_7, "+
				   "a.Z12_NU_NUMERO_REGISTRO AS ID_REGISTRO_8, a.Z12_CAMPO8_VALOR AS VALOR_8, a.Z12_CAMPO8_RESULTADO AS RESULTADO_8, a.Z12_CAMPO8_MENSAJE as MENSAJE_8 " +
				   "from INFI_TB_Z12_REGISTROS a where Z11_COD_PROCESO="+_record.getValue("cod_mapa")+" ORDER BY a.Z12_NU_NUMERO_REGISTRO ASC";
 
			sqlConteo = "select COUNT(*) CONTEO_REGISTROS " +
			   " from INFI_TB_Z12_REGISTROS a where Z11_COD_PROCESO="+_record.getValue("cod_mapa");
					
		
		}else{	
		
			sql2 = "select a.Z12_NU_NUMERO_REGISTRO AS ID_REGISTRO_1, a.Z12_CAMPO1_VALOR AS VALOR_1, a.Z12_CAMPO1_RESULTADO AS RESULTADO_1, a.Z12_CAMPO1_MENSAJE as MENSAJE_1, " +
				   "a.Z12_NU_NUMERO_REGISTRO AS ID_REGISTRO_2, a.Z12_CAMPO2_VALOR AS VALOR_2, a.Z12_CAMPO2_RESULTADO AS RESULTADO_2, a.Z12_CAMPO2_MENSAJE as MENSAJE_2, "+
				   "a.Z12_NU_NUMERO_REGISTRO AS ID_REGISTRO_3, a.Z12_CAMPO3_VALOR AS VALOR_3, a.Z12_CAMPO3_RESULTADO AS RESULTADO_3, a.Z12_CAMPO3_MENSAJE as MENSAJE_3, "+
				   "a.Z12_NU_NUMERO_REGISTRO AS ID_REGISTRO_4, a.Z12_CAMPO4_VALOR AS VALOR_4, a.Z12_CAMPO4_RESULTADO AS RESULTADO_4, a.Z12_CAMPO4_MENSAJE as MENSAJE_4, "+
				   "a.Z12_NU_NUMERO_REGISTRO AS ID_REGISTRO_5, a.Z12_CAMPO5_VALOR AS VALOR_5, a.Z12_CAMPO5_RESULTADO AS RESULTADO_5, a.Z12_CAMPO5_MENSAJE as MENSAJE_5, "+
				   "a.Z12_NU_NUMERO_REGISTRO AS ID_REGISTRO_6, a.Z12_CAMPO6_VALOR AS VALOR_6, a.Z12_CAMPO6_RESULTADO AS RESULTADO_6, a.Z12_CAMPO6_MENSAJE as MENSAJE_6, "+
				   "a.Z12_NU_NUMERO_REGISTRO AS ID_REGISTRO_7, a.Z12_CAMPO7_VALOR AS VALOR_7, a.Z12_CAMPO7_RESULTADO AS RESULTADO_7, a.Z12_CAMPO7_MENSAJE as MENSAJE_7, "+
				   "a.Z12_NU_NUMERO_REGISTRO AS ID_REGISTRO_8, a.Z12_CAMPO8_VALOR AS VALOR_8, a.Z12_CAMPO8_RESULTADO AS RESULTADO_8, a.Z12_CAMPO8_MENSAJE as MENSAJE_8 " +
				   "  from INFI_TB_Z12_REGISTROS a where a.Z12_CO_CODIGO_REGISTRO=9999999999  ORDER BY a.Z12_NU_NUMERO_REGISTRO ASC" ;
			
			sqlConteo = "select 0 as CONTEO_REGISTROS from dual "; 			
		
		}
		
		_mapa = db.get(_dso, sql);
		_registro = db.get(_dso, sql2);
		_conteo = db.get(_dso, sqlConteo);
		
		storeDataSet( "archivos", _archivos );
		storeDataSet( "mapas", _mapa );   
		storeDataSet("registro",_registro);
		storeDataSet("conteo",_conteo);
		storeDataSet( "record", _record ); 

	} 
	
	public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();
	
		if (flag)
		{
			
			if (_record.getValue("band") != null) {
				
				if (_record.getValue("num_archivo")== null){
					
					_record.addError("MSG9001", " Debe seleccionar un archivo de la lista");
					flag = false;
				}
				if(_record.getValue("reporte")!=null){
					if (_record.getValue("cod_mapa")== null){
						_record.addError("MSG9002", "Debe seleccionar un proceso de la lista.");
						flag = false;
					}
				} 
			}	
		 
		}
		return flag;	
	}
}
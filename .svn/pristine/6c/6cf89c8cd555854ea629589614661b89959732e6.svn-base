package models.consultas_carga_inicial.carga_final_datos;

import megasoft.*;import models.msc_utilitys.*;

public class Table extends MSCModelExtend
{

	/** DataSet del modelo */    	
	private DataSet _registrosGrabados = null;
	private DataSet _registrosNoGrabados = null;
	private DataSet _aux = null;
	private int num_campos = 0;
	private DataSet camposMapa = new DataSet();
	
	/** 
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{
		_registrosNoGrabados = new DataSet();
		_registrosNoGrabados.append("registro", java.sql.Types.VARCHAR);
		_registrosNoGrabados.append("resultado", java.sql.Types.VARCHAR);

		_registrosGrabados = new DataSet();
		_registrosGrabados.append("registro", java.sql.Types.VARCHAR);
		_registrosGrabados.append("resultado", java.sql.Types.VARCHAR);

		StringBuffer sql = new StringBuffer();
			
		//armarNombresCampos();
		
		armarRegistrosNoGrabados();
		
		armarRegistrosGrabados();
		
		_record.setValue("total_registros_no_ok", String.valueOf(_registrosNoGrabados.count()));
		_record.setValue("total_registros_ok", String.valueOf(_registrosGrabados.count()));
				
		
		while(_registrosNoGrabados.next()){
			_registrosNoGrabados.setValue("z51_monto_adjudicado", _registrosNoGrabados.getValue("z51_monto_adjudicado").replace(".0", ""));
		}
		
		while(_registrosGrabados.next()){
			_registrosGrabados.setValue("z51_monto_adjudicado", _registrosGrabados.getValue("z51_monto_adjudicado").replace(".0", ""));
		}

		
		storeDataSet( "registros_no_grabados", _registrosNoGrabados);	
		storeDataSet( "registros_grabados", _registrosGrabados);	
		
		storeDataSet( "record", _record ); 

	}


	private void armarRegistrosNoGrabados() throws Exception {

		
		StringBuffer sql = new StringBuffer();
		//buscar registros no grabados 
		sql.append("select * from infi_tb_z51_titulos where z51_estatus = 0 ");
		
		if(_record.getValue("titulo_id")!=null){
			sql.append(" and z51_titulo_id = '").append(_record.getValue("titulo_id")).append("'");
		}
		
		sql.append(" order by z51_titulo_id, z51_cliente  ");
	
		_registrosNoGrabados = db.get(_dso, sql.toString());
		
			
	} 
	
	private void armarRegistrosGrabados() throws Exception {

		StringBuffer sql = new StringBuffer();
		//buscar registros no grabados 
		sql.append("select * from infi_tb_z51_titulos where (z51_estatus = 1 or z51_estatus = 2) ");
		
		if(_record.getValue("titulo_id")!=null){
			sql.append(" and z51_titulo_id = '").append(_record.getValue("titulo_id")).append("'");
		}

		sql.append(" order by z51_titulo_id, z51_cliente ");
		
		_registrosGrabados = db.get(_dso, sql.toString());
		
		
	}


}
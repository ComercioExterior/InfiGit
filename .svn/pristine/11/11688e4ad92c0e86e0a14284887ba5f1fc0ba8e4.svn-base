package models.exportacion_excel;

import megasoft.*;import models.msc_utilitys.*;

public class CancelarProceso extends MSCModelExtend
{	
	/**
	 * Ejecuta la transaccion del modelo
	 */	
	
	
	/*public CancelarProceso(String num_proc){
		this.num_proc = num_proc;
	}*/
	
	public void execute() throws Exception
	{ 
		
		if(_req.getParameter("ejecucion_proc").equals("1")){
			
			DataSet _aux = null;
			//Buscar ultimo proceso ejecutado
			String sql = "select next_id from sequence_numbers where table_name ='INFI_TB_Z11_PROCESOS'";
			_aux = db.get(_dso, sql);
			
			if(_aux.next()){
				//Actualizar el estatus del proceso a 3= Ejecuci&oacute;n cancelada
				sql = "update INFI_TB_Z11_PROCESOS set z11_de_descripcion_estado = '3' where z11_cod_proceso="+_aux.getValue("next_id");
				
				
				//ejecutar query
				//db.exec( _dso, sql);
			}
			
		}
	


	}

		

}
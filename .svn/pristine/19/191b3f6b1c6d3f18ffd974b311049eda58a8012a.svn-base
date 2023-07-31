package models.carga_inicial;

import megasoft.*;import models.msc_utilitys.*;

public class CancelarProceso extends MSCModelExtend
{	
	/**
	 * Ejecuta la transaccion del modelo
	 */			
	
	public void execute() throws Exception
	{
						
		//obtener de la sesion el id del proceso que se est&aacute; ejecutando
		if(_req.getSession().getAttribute("id_proceso_actual")!=null){
		
			//Actualizar el estatus del proceso a 3= Ejecuci&oacute;n cancelada
			//si el proceso ya ha sido insertado
			String sql = "update INFI_TB_Z11_PROCESOS set z11_de_descripcion_estado = '3' where z11_cod_proceso="+_req.getSession().getAttribute("id_proceso_actual");
					
			//ejecutar query
			db.exec( _dso, sql);
		}

	}

		

}

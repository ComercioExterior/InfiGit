package models.conversion_data;

import java.util.Vector;

import megasoft.*;
import models.msc_utilitys.*;

public class Procesar_Error extends MSCModelExtend
{

	/** DataSet del modelo */    
	private DataSet _archivos = null;
	private DataSet _entidad = null;
	private DataSet _insercion = null;
	private DataSet _columna = null;
	private DataSet _registro = null;
	private Vector lista_valores = new Vector();
	 
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{
			

		storeDataSet( "archivos", _archivos );
		storeDataSet( "record", _record );
		
	}
	public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();
		String sql = "";
			
		if (flag)
		{
			 
			if (_req.getParameter("band") != null) {
				
				if (_req.getParameter("cod_archivo")== null || _req.getParameter("cod_archivo").equals("")){
					_record.addError("MSG-CD100-0001", " Seleccione los parametros requeridos (archivo)");
					 flag = false;
				}else{
					if (_req.getParameter("cod_mapa")== null || _req.getParameter("cod_mapa").equals("")){
					      _record.addError("MSG-CD100-0001", " Seleccione los parametros requeridos (mapa)");
					      flag = false;
				        }
					
				    else{
				       if (_req.getParameter("cod_proceso")== null || _req.getParameter("cod_proceso").equals("")){
						      _record.addError("MSG-CD100-0001", " Seleccione los parametros requeridos (proceso)");
						      flag = false;
					 }
				      else{
				    	  
				    	 if (_req.getParameter("entidad_archivo")== null || _req.getParameter("entidad_archivo").equals("")){
								_record.addError("MSG-CD100-0001", " Seleccione los parametros requeridos (entidad)");
							    flag = false;
				    	  
					      }
				    	 
				    else{
				    	
		    				 sql = "";
		    				 sql = getResource("conteo_proceso.sql");
							 sql = Util.replace(sql, "@cod_proceso@", _req.getParameter("cod_proceso"));
								_archivos = db.get(_dso, sql);
								
								while (_archivos.next()) {
									if (_archivos.getValue("CONTEO").equals("0")){
										_record.addError("MSG-CD100-002", " No existen datos a procesar");
										flag = false;
									}
									else{
										flag = true;
									}
								}
						   //}
		    		   //}
		    		 }
			      } 
			   }
			  }
			}	
			
			 
			
		 
		}
		return flag;	
	}
}

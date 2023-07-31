package models.configuracion.generales.usuariosespeciales; 

import com.bdv.infi.dao.UsuariosEspecialesDAO;

import megasoft.*;


public class Table extends AbstractModel
{  
 
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{			
		UsuariosEspecialesDAO usuariosEspecialesDAO = new UsuariosEspecialesDAO(_dso);			
		
		//Realizar consulta
		usuariosEspecialesDAO.listar();		
		
		DataSet DatosUsersEspeciales = new DataSet();
		DatosUsersEspeciales = usuariosEspecialesDAO.getDataSet();	
		
		while(DatosUsersEspeciales.next()){
			
			if(DatosUsersEspeciales.getValue("USRESP_CAMBIO_COMISION")!=null && DatosUsersEspeciales.getValue("USRESP_CAMBIO_COMISION").equals("1")){
				DatosUsersEspeciales.setValue("USRESP_CAMBIO_COMISION", "SI");
			}else{
				DatosUsersEspeciales.setValue("USRESP_CAMBIO_COMISION", "NO");
			}	
			
			if(DatosUsersEspeciales.getValue("USRESP_CAMBIO_VEHICULO")!=null && DatosUsersEspeciales.getValue("USRESP_CAMBIO_VEHICULO").equals("1")){
				DatosUsersEspeciales.setValue("USRESP_CAMBIO_VEHICULO", "SI");
			}else{
				DatosUsersEspeciales.setValue("USRESP_CAMBIO_VEHICULO", "NO");
			}			
			
			if(DatosUsersEspeciales.getValue("USRESP_MULTIBLOTTER")!=null && DatosUsersEspeciales.getValue("USRESP_MULTIBLOTTER").equals("1")){
				DatosUsersEspeciales.setValue("USRESP_MULTIBLOTTER", "SI");
			}else{
				DatosUsersEspeciales.setValue("USRESP_MULTIBLOTTER", "NO");
			}			
			
			if(DatosUsersEspeciales.getValue("USRESP_FINANCIAMIENTO")!=null && DatosUsersEspeciales.getValue("USRESP_FINANCIAMIENTO").equals("1")){
				DatosUsersEspeciales.setValue("USRESP_FINANCIAMIENTO", "SI");
			}else{
				DatosUsersEspeciales.setValue("USRESP_FINANCIAMIENTO", "NO");
			}		
			
			if(DatosUsersEspeciales.getValue("USRESP_PRECIO_VENTA_TITULOS")!=null && DatosUsersEspeciales.getValue("USRESP_PRECIO_VENTA_TITULOS").equals("1")){
				DatosUsersEspeciales.setValue("USRESP_PRECIO_VENTA_TITULOS", "SI");
			}else{
				DatosUsersEspeciales.setValue("USRESP_PRECIO_VENTA_TITULOS", "NO");
			}

		}
		
		//registrar los datasets exportados por este modelo
		storeDataSet("table", DatosUsersEspeciales);
		storeDataSet("total", usuariosEspecialesDAO.getTotalRegistros());
				
		
	}

}


package models.configuracion.plantillas_correo;

import megasoft.AbstractModel;
import megasoft.Logger;

import com.bdv.infi.dao.EventoMailDao;
import com.bdv.infi.util.Utilitario;

/**
 * Clase Gen&eacute;rica que dado un c&oacute;digo espec&iacute;fico permite invocar un m&eacute;todo para una funci&oacute;n ajax dentro de una vista
 * @author NM29643 infi_TTS_466 09/07/2014
 */
public class AjaxFilterEventos extends AbstractModel {
			
	
	public void execute() throws Exception {
				
		try {
			
			this.ajaxListarEventos();
			
		} catch (Exception e) {
			Logger.error(this,e.getMessage()+" "+Utilitario.stackTraceException(e));
			throw new Exception(e);			
		}
		
	}
		
	/**
	 * Permite buscar los datos necesarios para armar una nueva area para una plantilla de correo espec&iacute;fica
	 * @throws Exception
	 */
	void ajaxListarEventos() throws Exception{
		
		EventoMailDao eventoMailDao = new EventoMailDao(_dso);
		
		//Recuperar el producto seleccionado
		String productoId = String.valueOf(_req.getParameter("prod"));
//		System.out.println("idProdAjax: "+productoId);
		
		if(productoId!=null && productoId.length()>0){
			eventoMailDao.listarEventosDelProd(productoId); //Obtiene los eventos de envio de correo relacionados con el producto
		}
		
		storeDataSet("eventos", eventoMailDao.getDataSet()); //Registra el dataset exportado por este modelo
		
	}

}

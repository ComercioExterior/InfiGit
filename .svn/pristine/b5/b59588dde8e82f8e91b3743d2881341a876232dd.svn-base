package models.utilitarios.envio_correos;

import megasoft.AbstractModel;
import megasoft.Logger;

import com.bancovenezuela.comun.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.dao.PlantillasMailDAO;
import com.bdv.infi.data.PlantillaMail;
import com.bdv.infi.util.Utilitario;

/**
 * Clase Gen&eacute;rica que dado un c&oacute;digo espec&iacute;fico permite invocar un m&eacute;todo para una funci&oacute;n ajax dentro de una vista
 * @author NM29643 infi_TTS_466 09/07/2014
 */
public class AjaxPlantilla extends AbstractModel {
			
	
	public void execute() throws Exception {
				
		try {
			
			this.ajaxListarPlantilla();
			
		} catch (Exception e) {
			Logger.error(this,e.getMessage()+" "+Utilitario.stackTraceException(e));
			throw new Exception(e);			
		}
		
	}
		
	/**
	 * Permite buscar una plantilla acorde a los datos especificados
	 * @throws Exception
	 */
	void ajaxListarPlantilla() throws Exception{
		
		PlantillasMailDAO plantMailDao = new PlantillasMailDAO(_dso);
		
		//Recuperar el producto seleccionado
		String eventoId = String.valueOf(_req.getParameter("even"));
		String dest = String.valueOf(_req.getParameter("dest"));
		System.out.println("eventoId: "+eventoId+" dest: "+dest);
		
		if(eventoId!=null && eventoId.length()>0){
			PlantillaMail pm = new PlantillaMail();
			pm.setEventoId(eventoId);
			pm.setTipoDestinatario(dest);
			pm.setEstatusActivacion(String.valueOf(ConstantesGenerales.VERDADERO));
			plantMailDao.listarPlantillasMail(pm, null, null, null, null); //Obtiene los eventos de envio de correo relacionados con el producto
		}
		
		storeDataSet("plantilla", plantMailDao.getDataSet()); //Registra el dataset exportado por este modelo
		
	}

}

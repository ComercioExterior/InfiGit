package models.configuracion.plantillas_correo;

import megasoft.AbstractModel;
import megasoft.Logger;

import com.bdv.infi.dao.PlantillasMailDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.util.Utilitario;

/**
 * Clase Gen&eacute;rica que dado un c&oacute;digo espec&iacute;fico permite invocar un m&eacute;todo para una funci&oacute;n ajax dentro de una vista
 * @author NM29643 infi_TTS_466 09/07/2014
 */
public class AjaxFilterAreas extends AbstractModel {
			
	
	public void execute() throws Exception {
				
		try {
			
			this.ajaxListarAreas();
			
		} catch (Exception e) {
			Logger.error(this,e.getMessage()+" "+Utilitario.stackTraceException(e));
			throw new Exception(e);			
		}
		
	}
		
	/**
	 * Permite buscar los datos necesarios para armar una nueva area para una plantilla de correo espec&iacute;fica
	 * @throws Exception
	 */
	void ajaxListarAreas() throws Exception{
		
		PlantillasMailDAO plantMailDao = new PlantillasMailDAO(_dso);
		
		plantMailDao.listarAreasPlantillasMail(null, String.valueOf(ConstantesGenerales.VERDADERO));
		
		storeDataSet("areas", plantMailDao.getDataSet()); //Registra el dataset exportado por este modelo
		
	}

}

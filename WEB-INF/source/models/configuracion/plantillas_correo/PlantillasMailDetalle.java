package models.configuracion.plantillas_correo;

import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.PlantillasMailDAO;

public class PlantillasMailDetalle extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
				
		PlantillasMailDAO plantillasMailDAO = new PlantillasMailDAO(_dso);
				
		//Buscar plantilla
		plantillasMailDAO.listarPlantillasMailPorId(_req.getParameter("plantilla_mail_id"), true);		
		storeDataSet("datos_plantilla", plantillasMailDAO.getDataSet());
		
		//Buscar áreas
		plantillasMailDAO.listarAreasPlantillasMail(_req.getParameter("plantilla_mail_id"), null);
		storeDataSet("areas_plantilla", plantillasMailDAO.getDataSet());		
			
	}
}
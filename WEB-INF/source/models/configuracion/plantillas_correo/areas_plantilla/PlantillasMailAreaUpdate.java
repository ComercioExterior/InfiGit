package models.configuracion.plantillas_correo.areas_plantilla;

import java.util.ArrayList;

import megasoft.db;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.PlantillasMailDAO;
import com.bdv.infi.data.PlantillaMail;
import com.bdv.infi.data.PlantillaMailArea;

public class PlantillasMailAreaUpdate extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		PlantillaMail plantillaMail = new PlantillaMail();
		PlantillasMailDAO plantillasMailDAO = new PlantillasMailDAO(_dso);
		
		plantillaMail.setPlantillaMailId(Integer.parseInt(_req.getParameter("plantilla_mail_id")));
		plantillaMail.setUsuarioUltModificacion(getUserName());
		
		//--obtener areas agregadas---		
		if(_req.getSession().getAttribute("lista_areas")!=null){
			ArrayList<PlantillaMailArea> listaAreas = (ArrayList<PlantillaMailArea>) _req.getSession().getAttribute("lista_areas");
			//agregar lista de areas al objeto plantilla
			plantillaMail.setListaPlantillaMailArea(listaAreas);
		}
					
		String[] consulta = plantillasMailDAO.actualizarAreas(plantillaMail);
	
		db.execBatch(_dso,consulta);
	}	
	
}
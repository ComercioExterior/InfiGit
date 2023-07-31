package models.configuracion.plantillas_correo;

import java.util.ArrayList;

import megasoft.Logger;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.PlantillasMailDAO;
import com.bdv.infi.data.PlantillaMail;

public class PlantillasMailAprobar extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		PlantillaMail plantillaMail = new PlantillaMail();
		PlantillasMailDAO plantillasMailDAO = new PlantillasMailDAO(_dso);
		ArrayList<String> updates = new ArrayList<String>();
		
		plantillaMail.setUsuarioUltModificacion(getUserName());
		plantillaMail.setEventoId(_req.getParameter("evento_id"));
//		Logger.debug(this, "eventooooooooo-------: "+plantillaMail.getEventoId());
		updates.add(plantillasMailDAO.inactivarPorEvento(plantillaMail));
		
		plantillaMail.setPlantillaMailId(Integer.parseInt(_req.getParameter("plantilla_mail_id")));
		plantillaMail.setUsuarioAprobacion(getUserName());
		updates.add(plantillasMailDAO.aprobar(plantillaMail));
		
		plantillasMailDAO.ejecutarStatementsBatch(updates);
		
	}
	
}
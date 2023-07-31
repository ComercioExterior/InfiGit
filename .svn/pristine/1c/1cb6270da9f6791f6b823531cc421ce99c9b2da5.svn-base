package models.configuracion.plantillas_correo;

import megasoft.db;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.PlantillasMailDAO;
import com.bdv.infi.data.PlantillaMail;

public class PlantillasMailInactivar extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		PlantillaMail plantillaMail = new PlantillaMail();
		PlantillasMailDAO plantillasMailDAO = new PlantillasMailDAO(_dso);
		
		plantillaMail.setPlantillaMailId(Integer.parseInt(_req.getParameter("plantilla_mail_id")));
		plantillaMail.setUsuarioUltModificacion(getUserName());
		
		String sql = plantillasMailDAO.inactivar(plantillaMail);
	
		db.exec(_dso,sql);
	}
	
}
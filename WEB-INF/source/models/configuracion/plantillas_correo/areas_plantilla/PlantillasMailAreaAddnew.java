package models.configuracion.plantillas_correo.areas_plantilla;

import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.PlantillasMailDAO;

public class PlantillasMailAreaAddnew extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
				
		PlantillasMailDAO plantillasMailDAO = new PlantillasMailDAO(_dso);
				
		//Buscar plantilla
		plantillasMailDAO.listarPlantillasMailPorId(_req.getParameter("plantilla_mail_id"), true);		
		storeDataSet("datos_plantilla", plantillasMailDAO.getDataSet());
		
		//Inicializar valores de sesion para el caso de agregar areas en plantilla funcional 
		_req.getSession().removeAttribute("num_area");
		_req.getSession().setAttribute("num_area", "0");
			
	}
}
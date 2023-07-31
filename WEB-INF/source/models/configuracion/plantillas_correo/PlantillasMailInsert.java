package models.configuracion.plantillas_correo;

import megasoft.db;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.PlantillasMailDAO;
import com.bdv.infi.data.PlantillaMail;

public class PlantillasMailInsert extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		PlantillaMail plantillaMail = new PlantillaMail();
		PlantillasMailDAO plantillasMailDAO = new PlantillasMailDAO(_dso);
		
		plantillaMail.setPlantillaMailName(_record.getValue("plantilla_mail_name"));
		plantillaMail.setEventoId(_record.getValue("evento"));
		plantillaMail.setTipoDestinatario(_record.getValue("tipo_destinatario"));
		plantillaMail.setRemitente(_record.getValue("remitente"));
		plantillaMail.setAsunto(_record.getValue("asunto"));		
		plantillaMail.setCuerpo(_record.getValue("cuerpo"));
		plantillaMail.setBloqueIterado(_record.getValue("bloque"));
		plantillaMail.setUsuarioRegistro(getUserName());
		
		//Insertar Plantilla			
		String[] consulta = plantillasMailDAO.insertar(plantillaMail);;
	
		db.execBatch(_dso,consulta);
	}
	
}
package models.configuracion.plantillas_correo;

import java.util.ArrayList;

import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.PlantillasMailDAO;
import com.bdv.infi.data.PlantillaMail;
import com.bdv.infi.data.PlantillaMailArea;

public class PlantillasMailBrowse extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {

//		System.out.println("evento: "+_record.getValue("evento"));
//		System.out.println("area: "+_record.getValue("area"));
//		System.out.println("tipo_destinatario: "+_record.getValue("tipo_destinatario"));
		
		PlantillasMailDAO plantillasMailDAO = new PlantillasMailDAO(_dso);
		ArrayList<PlantillaMailArea> plantillaMailAreas;
		
		PlantillaMail plantillaMail = new PlantillaMail();
		//NM29643 infi_TTS_466 09/07/2014
		plantillaMail.setProductoId(_record.getValue("producto"));
		plantillaMail.setEventoId(_record.getValue("evento"));
		System.out.println("Producto: "+plantillaMail.getProductoId()+" Evento: "+plantillaMail.getEventoId());
//		plantillaMail.setStatusOrdenId(_record.getValue("status_orden_plant"));
//		plantillaMail.setTransaccionId(_record.getValue("transac_plant"));
//System.out.println("TRANSACCION_ID: "+plantillaMail.getTransaccionId());
		plantillaMail.setPlantillaMailName(_record.getValue("plant_mail_name"));
		plantillaMail.setTipoDestinatario(_record.getValue("tipo_destinatario"));
		plantillaMail.setRemitente(_record.getValue("remitente"));
		plantillaMail.setAsunto(_record.getValue("asunto"));
		plantillaMail.setEstatus(_record.getValue("estatus"));
		plantillaMail.setEstatusActivacion(_record.getValue("estatus_activacion"));
		
		//NM29643 infi_TTS_466 09/07/2014
//		if(_record.getValue("plant_mail_area_name")!=null){
		if(_record.getValue("area")!=null){
			plantillaMailAreas = new ArrayList<PlantillaMailArea>();
			PlantillaMailArea area = new PlantillaMailArea();
			area.setPlantMailAreaId(Integer.parseInt(_record.getValue("area")));
			plantillaMailAreas.add(area);
			plantillaMail.setListaPlantillaMailArea(plantillaMailAreas);
		}
		
		plantillasMailDAO.listarPlantillasMail(plantillaMail, _record.getValue("fecha_aprobacion_desde"), _record.getValue("fecha_aprobacion_hasta"),  _record.getValue("fecha_registro_desde"), _record.getValue("fecha_registro_hasta"));
			
		//registrar los datasets exportados por este modelo
		storeDataSet("table", plantillasMailDAO.getDataSet());
		storeDataSet("datos", plantillasMailDAO.getTotalRegistros());
	}
}
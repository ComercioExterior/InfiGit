package models.configuracion.plantillas_correo;

import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.PlantillasMailDAO;
import com.bdv.infi.logic.interfaces.PlantillasMailTipos;

/**
 * Clase que confirma si se desea ejecutar la transacción, publica el dataset del request
 * @author elaucho
 */
public class PlantillasMailConfirmInactivar extends MSCModelExtend{
	
	String eventoName = "";
	
	@Override
	public void execute() throws Exception {		
				
//		PlantillasMailDAO plantillasMailDAO = new PlantillasMailDAO(_dso);
//				
//		//Colocar nombre del proceso o transaccion para la cual se crea la plantilla
//		plantillasMailDAO.obtenerTiposPlantilla(_record.getValue("plantilla_mail_name"));
//		if(plantillasMailDAO.getDataSet().next()){
//			_record.setValue("tipo_proceso_plantilla", plantillasMailDAO.getDataSet().getValue("tipo_plantilla"));
//		}
//		
		//Colocar nombre del tipo de destinatario dependiendo del codigo C o F (Cliente o Funcional)
		if(_record.getValue("tipo_destinatario").equals(PlantillasMailTipos.TIPO_DEST_CLIENTE_COD)){
			_record.setValue("nombre_tipo_destinatario", PlantillasMailTipos.TIPO_DEST_CLIENTE);
		}else{
			_record.setValue("nombre_tipo_destinatario", PlantillasMailTipos.TIPO_DEST_FUNCIONAL);
		}
		
		_record.setValue("evento_nombre", eventoName);
				
		storeDataSet("filter",_record);
	}//fin execute
	
	/**
	 * Validaciones generales de la clase
	 */
	public boolean isValid() throws Exception {

		boolean flag = super.isValid();
		if(flag){
			
			PlantillasMailDAO plantillasMailDAO = new PlantillasMailDAO(_dso);
			String activacion; 
			//Buscar plantilla
			plantillasMailDAO.listarPlantillasMailPorId(_req.getParameter("plantilla_mail_id"), true);		
			plantillasMailDAO.getDataSet().next();			
			activacion = plantillasMailDAO.getDataSet().getValue("ESTATUS_ACTIVACION");
			eventoName = plantillasMailDAO.getDataSet().getValue("EVENTO_NAME");
			
			if(activacion.equals("0")){
				_record.addError("Inactivaci&oacute;n","La plantilla ya se encuentra inactiva.");
				flag = false;
			}			
		}
		
		return flag;		
	}

}

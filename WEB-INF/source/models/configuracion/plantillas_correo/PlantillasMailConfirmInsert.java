package models.configuracion.plantillas_correo;

import megasoft.Logger;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.PlantillasMailDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.PlantillasMailTipos;

/**
 * Clase que confirma si se desea ejecutar la transacción, publica el dataset del request
 * @author elaucho
 */
public class PlantillasMailConfirmInsert extends MSCModelExtend{
	
	@Override
	public void execute() throws Exception {		
				
		//Colocar nombre del tipo de destinatario dependiendo del codigo C o F (Cliente o Funcional)
		if(_record.getValue("tipo_destinatario").equals(PlantillasMailTipos.TIPO_DEST_CLIENTE_COD)){
			_record.setValue("nombre_tipo_destinatario", PlantillasMailTipos.TIPO_DEST_CLIENTE);
		}else{
			_record.setValue("nombre_tipo_destinatario", PlantillasMailTipos.TIPO_DEST_FUNCIONAL);
		}
		
		storeDataSet("filter",_record);
		
	}//fin execute
	
	/**
	 * Validaciones generales de la clase
	 */
	public boolean isValid() throws Exception {

		boolean flag = super.isValid();
		if(flag){			
			PlantillasMailDAO plantillasMailDAO = new PlantillasMailDAO(_dso);
			String fechaAprobacion;
			String estatusActivacion;
			
			Logger.debug(this, "-----------nameEtiqueta: "+_record.getValue("name_etiqueta"));
			if(_record.getValue("evento").equals(ConstantesGenerales.CRUCE) ||	_record.getValue("evento").equals(ConstantesGenerales.RECEPCION_TITULO)	){
				if(_record.getValue("cuerpo")!=null && _record.getValue("cuerpo").indexOf("@"+_record.getValue("name_etiqueta")+"@")==-1){
					_record.addError("Cuerpo de Plantilla Incorrecto","El cuerpo de la plantilla para este tipo de evento debe contener la etiqueta @"+_record.getValue("name_etiqueta")+"@.");
					flag = false;
				}else{
					//Verificar si existe una plantilla del mismo evento, tipo de destinatario y que se encuentre aprobada y activa ó en estatus registrada
					//si ya existe una plantilla registrada del mismo tipo, no se permite agregar otra, ya que esta puede editarse
					//si existe una plantilla aprobada y activa tampoco se permitirá agregar otra del mismo tipo, hasta que la existente sea desactivada. Una vez desactivada, la misma tampoco podrá editarse, con el fin de que quede el registro en la historia. Es entonces cuando podrá agregarse una nueva del mismo tipo			
					boolean existePlantilla = plantillasMailDAO.verificarPlantillaExistente(_record.getValue("evento"), _record.getValue("tipo_destinatario"));
					
					if (existePlantilla){//aprobada y activa
						
						plantillasMailDAO.getDataSet().next();
						fechaAprobacion = plantillasMailDAO.getDataSet().getValue("FECHA_APROBACION");
						estatusActivacion = plantillasMailDAO.getDataSet().getValue("ESTATUS_ACTIVACION");	

						if(fechaAprobacion!=null && estatusActivacion.equals("1")){
							_record.addError("Plantilla Existente","No es posible realizar la inserci&oacute;n ya que existe una plantilla asociada al mismo estatus de orden, transacci&oacute;n y tipo de destinatario que se encuentra aprobada y activa. Para agregar una nueva, desactive la plantilla existente.");
							flag = false;
						}else{
							 //Registrada
							_record.addError("Plantilla Existente","No es posible realizar la inserci&oacute;n ya que existe una plantilla en estatus registrada, asociada al mismo evento y tipo de destinatario. Para realizar cambios, edite la planilla existente.");
							flag = false;
						}
					}
				}
			}
		}
		
		return flag;		
	}
}

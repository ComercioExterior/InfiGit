package models.configuracion.plantillas_correo.areas_plantilla;

import java.util.ArrayList;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.data.PlantillaMailArea;

/**
 * Clase que confirma si se desea ejecutar la transacción, publica el dataset del request
 * @author elaucho
 */
public class PlantillasMailAreaConfirmInsert extends MSCModelExtend{
	
	String[] areas;
	
	public void execute() throws Exception {		
		
		//--obtener areas agregadas---	
		obtenerAreas();
		
		storeDataSet("filter",_record);
	}

	/**
	 * Obtiene las areas que se hayan agragado a la plantilla 
	 */
	private void obtenerAreas() throws Exception {		
		
		if(areas!=null){
			ArrayList<PlantillaMailArea> listaAreas = new ArrayList<PlantillaMailArea>();
			
			for(int i=0; i< areas.length; i++){
				
				PlantillaMailArea plantillaMailArea = new PlantillaMailArea();
				plantillaMailArea.setPlantMailAreaName(_req.getParameter("plant_mail_area_name_"+areas[i]));
				plantillaMailArea.setDestinatario(_req.getParameter("destinatario_"+areas[i]));
				plantillaMailArea.setEstatusActivacion(Integer.parseInt(_req.getParameter("estatus_activacion_"+areas[i])));
				plantillaMailArea.setPlantillaMailId(Integer.parseInt(_req.getParameter("plantilla_mail_id")));
				//agregar objeto area a la lista				
				listaAreas.add(plantillaMailArea);
			}			
			//Agregar la lista de objetos de areas a la sesion
			//_req.getSession().removeAttribute("lista_areas");
			_req.getSession().setAttribute("lista_areas", listaAreas);
		}		
	}
	
	/**
	 * Validaciones generales de la clase
	 */
	public boolean isValid() throws Exception {

		boolean flag = super.isValid();
		if(flag){
			areas = _req.getParameterValues("areas_arreglo");//obtener areas	
			///Validar los campos obligatorios de cada una de las areas
			if(areas!=null){
				boolean nombreEvaluado = false;
				boolean destEvaluado = false;
				
				for(int i=0; i< areas.length; i++){
					
					if(_req.getParameter("plant_mail_area_name_"+areas[i])==null || _req.getParameter("plant_mail_area_name_"+areas[i]).trim().equals("") && !nombreEvaluado){				
						_record.addError("Nombre de Area","Este campo es obligatorio para todas las areas");
						nombreEvaluado = true;
						flag = false;					
					}
					
					if(_req.getParameter("destinatario_"+areas[i])==null || _req.getParameter("destinatario_"+areas[i]).trim().equals("") && !destEvaluado){				
						_record.addError("Destinatario","Este campo es obligatorio para todas las areas");
						destEvaluado = true;
						flag = false;					
					}
										
				}		
			}
					
		}
		
		return flag;		
	}

	
}

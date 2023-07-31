package models.configuracion.plantillas_correo.areas_plantilla;

import megasoft.DataSet;
import megasoft.Page;
import megasoft.Util;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.PlantillasMailDAO;
import com.bdv.infi.logic.interfaces.PlantillasMailTipos;

public class PlantillasMailAreaEdit extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
				
		PlantillasMailDAO plantillasMailDAO = new PlantillasMailDAO(_dso);
		String tipoDestinatarioPlantilla;
		
		//Verificar eliminación de area
		if(_req.getParameter("eliminar_area")!=null && _req.getParameter("eliminar_area").equals("1")){
			String[] sqlEliminarArea = plantillasMailDAO.eliminarAreaPlantilla(_req.getParameter("plant_mail_area_id"), _req.getParameter("plantilla_mail_id"), getUserName());
			db.execBatch(_dso, sqlEliminarArea);
		}
				
		//Buscar plantilla
		plantillasMailDAO.listarPlantillasMailPorId(_req.getParameter("plantilla_mail_id"),true);
		//Guardar tipo de destinatario de plantilla
		plantillasMailDAO.getDataSet().next();
		tipoDestinatarioPlantilla = plantillasMailDAO.getDataSet().getValue("tipo_destinatario");
		storeDataSet("datos_plantilla", plantillasMailDAO.getDataSet());				
	
		armarAreasAsociadas(tipoDestinatarioPlantilla);		
			
	}

	/**
	 * Arma el html con las areas asociadas a la plantilla de correo
	 * @param tipoDestinatarioPlantilla
	 * @throws Exception
	 */
	private void armarAreasAsociadas(String tipoDestinatarioPlantilla) throws Exception {
		
		PlantillasMailDAO plantillasMailDAO = new PlantillasMailDAO(_dso);
		DataSet _areas = new DataSet();					
		int n_area = 0;
		String areas_htm = "";
		String aux = "";		
			
		//DataSet para almacenar areas con sus datos
		DataSet _areas_replace = new DataSet();
		_areas_replace.append("area_plantilla", java.sql.Types.VARCHAR);

		//---Obtener areas asociadas a la plantilla en caso de que esta sea de tipo funcional
		if(tipoDestinatarioPlantilla.equals(PlantillasMailTipos.TIPO_DEST_FUNCIONAL_COD)){
			//Listar areas
			plantillasMailDAO.listarAreasPlantillasMail(_req.getParameter("plantilla_mail_id"), null);
			_areas = plantillasMailDAO.getDataSet();
			
			//obtener template para areas
			aux = getResource("areas_editar.htm");
			
			//Recorrer cada una de las areas asociadas a la plantilla
			while(_areas.next()){				
			
				n_area++; //numero de regla
				areas_htm = aux;//inicializar html	para la regla					
			
				//Realizar reemplazos simples en htm
				areas_htm = Util.replace(areas_htm, "@num_area@", String.valueOf(n_area));
				areas_htm = Util.replace(areas_htm, "@plant_mail_area_name@", _areas.getValue("plant_mail_area_name"));
				areas_htm = Util.replace(areas_htm, "@destinatario@", _areas.getValue("destinatario"));
				areas_htm = Util.replace(areas_htm, "@plant_mail_area_id@", _areas.getValue("plant_mail_area_id"));
														
				//Crear objeto Page para ejecutar el bind de sincronizaci&oacute;n del campo de selección Activación
				Page pag = new Page(areas_htm);						
								
				//sincronizar valores de los controles de selecci&oacute;n con el valor asociado a la regla				
				pag.selectItem("estatus_activacion_"+n_area, _areas.getValue("estatus_activacion"));
							
				_areas_replace.addNew();
				_areas_replace.setValue("area_plantilla", pag.toString());
									
			}			
			
		}
				
		//colocar numero de areas en la sesion
		_req.getSession().removeAttribute("num_area");
		_req.getSession().setAttribute("num_area", String.valueOf(_areas.count()));
		
		//registrar dataset de areas
		storeDataSet("areas", _areas_replace);
		
	}
}
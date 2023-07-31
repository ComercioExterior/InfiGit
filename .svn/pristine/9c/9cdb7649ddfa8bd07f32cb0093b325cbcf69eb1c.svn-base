package models.unidad_inversion.configuracion_jornada;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.UnidadInversionDAO;

public class Confirm extends MSCModelExtend {

	public void execute() throws Exception {	
		String nroJornada=null;
		DataSet confirm= new DataSet();
		confirm.append("mostrar_btn_procesar", java.sql.Types.VARCHAR);
		confirm.append("mostrar_btn_procesar_manual", java.sql.Types.VARCHAR);
		confirm.append("mostrar_txt_jornada", java.sql.Types.VARCHAR);
		confirm.append("mensaje", java.sql.Types.VARCHAR);
		confirm.addNew();	
		
		UnidadInversionDAO unidadInversionDAO = new UnidadInversionDAO(_dso);
		
		if(unidadInversionDAO.validarUltimoDiaUI(Long.parseLong(_record.getValue("ui_id")))){			
			nroJornada=_record.getValue("nro_jornada");			
			if(!unidadInversionDAO.existenOrdenesEnviadasBCV(Long.parseLong(_record.getValue("ui_id")))||(nroJornada==null||nroJornada.length()==0)){
				if (_record.getValue("parametro_bcv_online").equalsIgnoreCase("0")&&(nroJornada==null||nroJornada.length()==0)) {
					confirm.setValue("mostrar_btn_procesar", "none");
					confirm.setValue("mostrar_btn_procesar_manual", "block");
					confirm.setValue("mostrar_txt_jornada", "block");
					confirm.setValue("mensaje", "¿Desea actualizar de manera 'MANUAL' el identificador de Jornada BCV a la unidad de inversión " + _record.getValue("undinv_nombre") + "?");
				}else
					if(_record.getValue("parametro_bcv_online").equalsIgnoreCase("0")){
						confirm.setValue("mostrar_btn_procesar", "none");
						confirm.setValue("mostrar_btn_procesar_manual", "none");
						confirm.setValue("mostrar_txt_jornada", "none");
						confirm.setValue("mensaje", "No se permite la actualización del identificador de Jornada BCV a la unidad de inversión " + _record.getValue("undinv_nombre") + "<br> debido a que el parámetro TRANSF_BCV_ONLINE se encuentra inactivo y la jornada ya fue asignada");
					}else
						{					
							confirm.setValue("mostrar_btn_procesar", "block");
							confirm.setValue("mostrar_btn_procesar_manual", "none");
							confirm.setValue("mostrar_txt_jornada", "none");
							confirm.setValue("mensaje", "¿Desea actualizar el identificador de Jornada BCV a la unidad de inversión " + _record.getValue("undinv_nombre") + "?");
						}
			}else{
				confirm.setValue("mostrar_btn_procesar", "none");
				confirm.setValue("mostrar_btn_procesar_manual", "none");
				confirm.setValue("mostrar_txt_jornada", "none");
				confirm.setValue("mensaje","Existen ordenes que ya han sido enviadas al BCV con el identificador de jornada actual, no se puede actualizar");
			}			
		}else{
			confirm.setValue("mostrar_btn_procesar", "none");
			confirm.setValue("mostrar_btn_procesar_manual", "none");
			confirm.setValue("mostrar_txt_jornada", "none");
			confirm.setValue("mensaje","La actualización del identificador de Jornada BCV se debe realizar desde el último día de toma de ordenes de la Unidad de Inversion");
		}
		storeDataSet("configuracion", confirm);	
		storeDataSet("record", _record);		
	}

	
}

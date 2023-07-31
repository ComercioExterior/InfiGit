package models.utilitarios.envio_correos;

import megasoft.Logger;
import models.msc_utilitys.MSCModelExtend;
//import com.bancovenezuela.comun.logic.interfaces.ConstantesGenerales;

public class EnvioCorreosConfirm extends MSCModelExtend {
	
	String[] correosIds;
	String idCorreos, seleccion;
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		String mensConfirm = "&iquest;Confirma que desea enviar ";
		if(!seleccion.equalsIgnoreCase("todos")){ //NO se seleccionaron todos los correos filtrados
			if(correosIds.length==1) mensConfirm += "el correo seleccionado?";
			else mensConfirm += "los correos seleccionados?";
		}else{
			mensConfirm += "todos los correos filtrados?";
		}
		_record.setValue("mensaje_confirmacion", mensConfirm);
		
		//storeDataSet("datos", datos); //Registra el dataset exportado por este modelo
		storeDataSet("record", _record); //Registra el dataset exportado por este modelo
		
	}
	
	
	
	public boolean isValid() throws Exception {
		
		boolean valido = true;
		String tipoFiltro = "";
		
		seleccion = "";
		idCorreos = "";
		
		String cicloId = _req.getParameter("ciclo").toString();
		String plantillaId = _req.getParameter("plantilla_id").toString();
//		String tipoDest = _req.getParameter("tipo_destinatario").toString();
		seleccion = _req.getParameter("seleccion").toString();
Logger.debug(this, "seleccion---------: "+seleccion);
Logger.debug(this, "cicloId---------: "+cicloId);
Logger.debug(this, "plantillaId---------: "+plantillaId);
//Logger.debug(this, "tipoDest---------: "+tipoDest);
		
		//Se preserva en _record valor de variable "seleccion" (indica si se seleccionaron todos los correos)
		_record.setValue("todos_val", seleccion);
		//Se preserva en _record valor de variable "cicloId"
		_record.setValue("ciclo", cicloId);
		//Se preserva en _record valor de variable "plantillaId"
		_record.setValue("plantilla_id", plantillaId);
		//Se preserva en _record valor de variable "tipoDest"
//		_record.setValue("tipo_destinatario", tipoDest);
		
Logger.debug(this, "ciclo record: "+_record.getValue("ciclo"));
//Logger.debug(this, "tipo_destinatario record: "+_record.getValue("tipo_destinatario"));
Logger.debug(this, "plantilla_id record: "+_record.getValue("plantilla_id"));
Logger.debug(this, "todos_val record: "+_record.getValue("todos_val"));

		if(!seleccion.equalsIgnoreCase("todos")){ //NO se seleccionaron todos los correos filtrados
			tipoFiltro = _req.getParameter("tipoFiltro").toString();
			idCorreos = _req.getParameter("correo_ids").toString();
			//Se preserva en _record valor de variable "tipoFiltro"
			_record.setValue("tipo_filtro", tipoFiltro);
			//Se preserva en _record valor de variable "tipoFiltro"
			_record.setValue("correo_ids_concat", idCorreos);
			
			correosIds = idCorreos.split(",");
			
			if(idCorreos==null || idCorreos.length()<=0){
				valido = false;
				_record.addError("Correos", "Debe seleccionar al menos un correo");
			}else{
				_record.setValue("correo_ids_concat", idCorreos);
			}
			
		}
		
Logger.debug(this, "tipoFiltro---------: "+tipoFiltro);
Logger.debug(this, "correo_ids---------: "+idCorreos);
		
		
		
		return valido;
	}
}
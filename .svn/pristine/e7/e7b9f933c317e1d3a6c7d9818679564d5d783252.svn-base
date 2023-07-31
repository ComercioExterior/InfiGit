package models.utilitarios.envio_correos;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Hashtable;

import megasoft.DataSet;
import megasoft.Logger;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.ControlCicloDAO;
import com.bdv.infi.dao.EnvioMailDAO;
import com.bdv.infi.dao.PlantillasMailDAO;
import com.bdv.infi.data.EnvioMail;
import com.bdv.infi.data.Orden;
import com.bdv.infi.data.PlantillaMail;
import com.bdv.infi.data.PlantillaMailArea;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.PlantillasMailTipos;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaz_varias.EnvioCorreos;
//import com.bancovenezuela.comun.logic.interfaces.ConstantesGenerales;

public class EnvioCorreosConfirmacion extends MSCModelExtend {
	
	PlantillasMailDAO plantillasMailDAO;
	DataSet infoClientArea, datos, plantilla;
	String[] areas_req;
	String[] codOrdenes;
	Hashtable<String,PlantillaMailArea> areasInfo = new Hashtable<String,PlantillaMailArea>();
	Hashtable<String,Orden> ordenesInfo = new Hashtable<String,Orden>();
	ArrayList<EnvioMail> listaCorreosFiltrados;
	PlantillaMail pm;
	ControlCicloDAO controlCicloDAO;
	EnvioMailDAO emDAO;
	long ciclo_id;
	long idCorreoIni;
	long idCorreoIncrem;
	EnvioCorreos ec;
	String mensError;
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		try{
		
		datos.setValue("t_registros", infoClientArea.count()+"");
		datos.setValue("mens_error", mensError);
		if(infoClientArea.count()==0){
			datos.setValue("table_display", "none");
			datos.setValue("enviar_display", "none");
		}else{
			datos.setValue("enviar_display", "inline");
		}
		
Logger.debug(this, "infoClientArea Tam (Nro. Correos): "+infoClientArea.count());

//Logger.debug(this, "RECORD\n "+_record);
//Logger.debug(this, "infoClientArea\n "+infoClientArea);
		
		
		}catch (Exception e) {
			Logger.error(this,ec.getProceso().getTransaId()+": Ha ocurrido un error inesperado durante en el proceso de tipo "+ec.getProceso().getTransaId()+". Estatus Ciclo: "+ec.getCiclo().getStatus()+". Mensaje de la Excepcion: "+e.getMessage());
			mensError = "Ha ocurrido un error inesperado mientras se consultaban los correos por confirmar";
			
		}
		
		datos.setValue("mens_error", mensError);
		
		if(!mensError.equals("")){
			datos.setValue("table_display", "none");
			datos.setValue("enviar_display", "none");
		}

		storeDataSet("datos", datos); //Registra el dataset exportado por este modelo
		storeDataSet("info_correos", infoClientArea); //Registra el dataset exportado por este modelo
		storeDataSet("record", _record); //Registra el dataset exportado por este modelo
		
		Logger.error(this,"Record\n"+_record);
	}
	
	
	
	public boolean isValid() throws Exception {
		
		boolean valido = true;
		mensError = "";
		
		//Se inicializa el dataSet con datos para la vista
		datos = new DataSet();
		datos.append("ciclo_id", java.sql.Types.VARCHAR);
		datos.append("cliente_display", java.sql.Types.VARCHAR);
		datos.append("funcional_display", java.sql.Types.VARCHAR);
		datos.append("table_display", java.sql.Types.VARCHAR);
		datos.append("colspan", java.sql.Types.VARCHAR);
		datos.append("colspan_todos", java.sql.Types.VARCHAR);
		datos.append("columnas", java.sql.Types.VARCHAR);
		datos.append("t_registros", java.sql.Types.VARCHAR);
		datos.append("mens_error", java.sql.Types.VARCHAR);
		datos.append("enviar_display", java.sql.Types.VARCHAR);
		
		try{
		
		datos.addNew();
		datos.setValue("cliente_display", "none");
		datos.setValue("funcional_display", "none");
		datos.setValue("table_display", "block");
		
		//Se inicializa dataSet con informacion a mostrar en tabla de la vista
		infoClientArea = new DataSet();
		
		//Se define el campo fijo (independientemente del tipo de destinatario)
		infoClientArea.append("correo_id", java.sql.Types.VARCHAR);
		
		//Creacion de objeto EnvioCorreos
		InetAddress direccion = InetAddress.getLocalHost();
    	String direccionIpstr = direccion.getHostAddress();
		ec = new EnvioCorreos(_dso, null, direccionIpstr);
		//Se inicializan los parametros relacionados con el envio de correos
		ec.initParamEnvio();
				
		//Devuelve 0 si no hay ciclo abierto de este tipo
		ciclo_id = ec.verificarCicloExistente(TransaccionNegocio.ENVIO_CORREOS, ConstantesGenerales.STATUS_ENVIO_CORREO_PREINICIADO); 
		Logger.debug(this, "----CICLO EXISTENTE ID: "+ciclo_id);
		//Se preserva el id del ciclo
		datos.setValue("ciclo_id", ciclo_id+"");
		if(ciclo_id <= 0){
			_record.addError("Ciclo "+TransaccionNegocio.ENVIO_CORREOS.replaceAll("_", " "), "No existe ning&uacute;n ciclo de env&iacute;o de correos abierto por confirmar");
			valido = false;
		}else{
			emDAO = new EnvioMailDAO(_dso);
			
			Logger.debug(this, "----Pre consulta de correos del ciclo: "+ciclo_id);
			
			String destCliente = PlantillasMailTipos.TIPO_DEST_CLIENTE_COD;
			//Obtiene los correos del ciclo en estatus precargado
			emDAO.listarCorreos(null, false, destCliente, ciclo_id+"", ec.parametros.get(ParametrosSistema.STATUS_PRECARGADO));
			DataSet correos = emDAO.getDataSet();
			
			if(correos.count()>0){ //Hay correos filtrados cargasdos para el ciclo
				
				correos.first();
				
				
				for(int i=0; i<correos.count(); i++){
					correos.next();
					
					if(i==0){ //Primera vez
						//Se llena info de plantilla
						_record.setValue("plantilla_id", correos.getValue("PLANTILLA_MAIL_ID"));
						_record.setValue("plant_mail_name", correos.getValue("PLANTILLA_MAIL_NAME"));
//						_record.setValue("status_orden", correos.getValue("ORDSTA_ID"));
//						_record.setValue("name_status_orden_plant", correos.getValue("ORDSTA_NOMBRE"));
//						_record.setValue("transaccion", correos.getValue("TRANSA_ID"));
//						_record.setValue("name_status_orden_plant", correos.getValue("TRANSA_DESCRIPCION"));
//						
//						//Info del destinatario
//						_record.setValue("tipo_destinatario", correos.getValue("TIPO_DESTINATARIO"));
//						_record.setValue("name_dest", correos.getValue("TIPO_DEST_NAME"));
						_record.setValue("tipo_destinatario", destCliente);
						
						//Info UI y ordenes
						_record.setValue("ui_id", "");
						_record.setValue("ui_txt", "");
						_record.setValue("id_ordenes", "");
					}
					
					if(_record.getValue("tipo_destinatario").equalsIgnoreCase(PlantillasMailTipos.TIPO_DEST_FUNCIONAL_COD)){
						//FUNCIONAL
						
						datos.setValue("funcional_display", "block");
						datos.setValue("colspan", "3");
						datos.setValue("colspan_todos", "2");
						datos.setValue("columnas", "<th>Area</th><th>Destinatario</th>");
						
						//Se define estructura del dataSet para caso Funcional
						infoClientArea.append("area_name", java.sql.Types.VARCHAR);
						infoClientArea.append("destinatario", java.sql.Types.VARCHAR);
						
						//Informacion para la vista
						infoClientArea.addNew();
						infoClientArea.setValue("correo_id", correos.getValue("CORREO_ID"));
						infoClientArea.setValue("area_name", correos.getValue("PLANT_MAIL_AREA_NAME"));
						infoClientArea.setValue("destinatario", correos.getValue("DESTINATARIO"));
					}else{
						if(_record.getValue("tipo_destinatario").equalsIgnoreCase(PlantillasMailTipos.TIPO_DEST_CLIENTE_COD)){
							//CLIENTE
							
							datos.setValue("cliente_display", "block");
							datos.setValue("colspan", "6");
							datos.setValue("colspan_todos", "5");
							datos.setValue("columnas", "<th>CI/RIF</th><th>Nombre</th><th>Correo</th><th>Nro. Orden</th><th>Unidad de Inversi&oacute;n</th>");
							
							//Se define estructura del dataSet para caso Cliente
							infoClientArea.append("ci_rif", java.sql.Types.VARCHAR);
							infoClientArea.append("nombre", java.sql.Types.VARCHAR);
							infoClientArea.append("correo", java.sql.Types.VARCHAR);
							infoClientArea.append("orden_id", java.sql.Types.VARCHAR);
							infoClientArea.append("ui_name", java.sql.Types.VARCHAR);
							
							//Informacion para la vista
							infoClientArea.addNew();
							infoClientArea.setValue("correo_id", correos.getValue("CORREO_ID"));
							infoClientArea.setValue("ci_rif", correos.getValue("TIPPER_ID")+"-"+correos.getValue("CLIENT_CEDRIF"));
							infoClientArea.setValue("nombre", correos.getValue("CLIENT_NOMBRE"));
							infoClientArea.setValue("correo", correos.getValue("CLIENT_CORREO_ELECTRONICO"));
							infoClientArea.setValue("orden_id", correos.getValue("ORDENE_ID"));
							infoClientArea.setValue("ui_name", correos.getValue("UNDINV_NOMBRE"));
							
						}//CLIENTE
					}//NO FUNCIONAL
					
				}//for
				
			}else{ //No hay correos filtrados cargados para el ciclo
				_record.addError("Ciclo "+TransaccionNegocio.ENVIO_CORREOS.replaceAll("_", " "), "No existe ning&uacute;n correo precargado por confirmar para el ciclo, el mismo fue terminado.");
				valido = false;
				ec.terminarCiclo(ciclo_id, null);
			}
			
		}//Si existe ciclo abierto
		
		}catch(Exception e){
			Logger.error(this, "ENVIO CORREOS: Ha ocurrido un error inesperado mientras se consultaban los correos por confirmar del ciclo ID "+ciclo_id+": "+e.getMessage());
			mensError = "Ha ocurrido un error inesperado mientras se consultaban los correos por confirmar";
			
		}
		
		return valido;
	}
}
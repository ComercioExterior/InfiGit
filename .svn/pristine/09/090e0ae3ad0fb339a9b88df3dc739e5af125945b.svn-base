package models.utilitarios.envio_correos;

import megasoft.Logger;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.logic.interfaces.PlantillasMailTipos;

public class InvocacionEnvioCorreos extends MSCModelExtend {
	private String cicloId;
	long ciclo_id;
	private String plantillaId; 
	private String correo_ids_concat;
	private String tipoFiltro;
	private String todos_val;
	private String tipoDest;
	
	
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		//Se setea tipo de destinatario a cliente
		tipoDest = PlantillasMailTipos.TIPO_DEST_CLIENTE_COD;
		
		EnvioCorreosEnviar envioCorreos = new EnvioCorreosEnviar(ciclo_id,plantillaId,correo_ids_concat,tipoFiltro,todos_val,tipoDest,getUserName(),super._dso);
		Thread hilo=new Thread(envioCorreos);		
		hilo.start();
		
	}
	
	public boolean isValid() throws Exception {
		
		System.out.println("DATASOURSE ---------> " + _dso);
		cicloId =_req.getParameter("ciclo").toString();
		ciclo_id = Long.parseLong(cicloId);
		plantillaId = _req.getParameter("plantilla_id").toString();
		correo_ids_concat = _req.getParameter("correo_ids_concat").toString();
		tipoFiltro = _req.getParameter("tipo_filtro").toString();
		todos_val = _req.getParameter("todos_val").toString();
//		tipoDest = _req.getParameter("tipo_destinatario").toString();
		
		//Logger.debug(this, "todos val: "+_record.getValue("todos_val"));
		//Logger.debug(this, "correo_ids_concat: "+_record.getValue("correo_ids_concat"));
		//Logger.debug(this, "ciclo en enviar: "+_record.getValue("ciclo"));
		//Logger.debug(this, "plantilla_id: "+_record.getValue("plantilla_id"));
		//Logger.debug(this, "tipo_destinatario: "+_record.getValue("tipo_destinatario"));
		
		//this.cicloId = _req.getParameter("ciclo").toString();			   
		//this.plantillaId = _req.getParameter("plantilla_id").toString();
		//correo_ids_concat = _req.getParameter("correo_ids_concat").toString();
		//tipoFiltro = _req.getParameter("tipo_filtro").toString();
		//todos_val = _req.getParameter("todos_val").toString();
		//String tipoDest = _req.getParameter("tipo_destinatario").toString();

		Logger.debug(this, "cicloId: "+cicloId);
		Logger.debug(this, "plantillaId: "+plantillaId);
		Logger.debug(this, "correo_ids_concat: "+correo_ids_concat);
		Logger.debug(this, "tipoFiltro: "+tipoFiltro);
		Logger.debug(this, "todos_val: "+todos_val);
//		Logger.debug(this, "tipoDest: "+tipoDest);
		
		//Se preserva el ID del Ciclo y el tipo de destinatario para la generaciond el informe
		//_record.setValue("ciclo", cicloId);
		//_record.setValue("tipo_destinatario", tipoDest);
		
		Logger.debug(this, "cicloId RECORD: "+_record.getValue("ciclo"));
//		Logger.debug(this, "tipo_destinatario: "+_record.getValue("tipo_destinatario"));
								
		boolean valido = true;											
											
		return valido;
			
	}
	
	
	
}
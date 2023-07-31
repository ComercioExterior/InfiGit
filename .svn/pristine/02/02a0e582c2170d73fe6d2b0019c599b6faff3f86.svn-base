package models.utilitarios.envio_correos;

import java.net.InetAddress;

import megasoft.Logger;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaz_varias.EnvioCorreos;
//import com.bancovenezuela.comun.logic.interfaces.ConstantesGenerales;

public class EnvioCorreosDescartar extends MSCModelExtend {
	
	String cicloId;
	EnvioCorreos ec;
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		String mensConfirm = "";
		
		try{
			
		//Se ELIMINAN los correos no seleccionados de la tabla de envio
		if(!ec.borrarCorreos(Long.parseLong(cicloId), ec.parametros.get(ParametrosSistema.STATUS_PRECARGADO), false)){
			mensConfirm = "No se pudieron descartar los correos filtrados. Intente de nuevo mediante la opci&oacute;n Confirmaci&oacute;n";
		}else{
			mensConfirm = "Se descartaron los correos filtrados";
			ec.terminarCiclo(Long.parseLong(cicloId), ConstantesGenerales.STATUS_ENVIO_CORREO_DESCARTADO);
		}
		
		}catch(Exception e){
			mensConfirm = "Ha ocurrido un error inesperado al intentar descartar los correos por confirmar del ciclo";
			Logger.error(this, "ENVIO CORREOS: Ha ocurrido un error inesperado al intentar descartar los correos por confirmar del ciclo ID "+cicloId+": "+e.getMessage());
		}
		
		_record.setValue("mensaje_confirmacion", mensConfirm);
		
		storeDataSet("record", _record); //Registra el dataset exportado por este modelo
		
	}
	
	
	
	public boolean isValid() throws Exception {
		
		boolean valido = true;
		
		cicloId = _req.getParameter("ciclo").toString();

Logger.debug(this, "cicloId---------: "+cicloId);
		
		if(cicloId==null || cicloId.length()<=0){
			valido = false;
			_record.addError("Descartar Correos", "No se obtuvo Nro. de ciclo a descartar. Intente de nuevo mediante la opci&oacute;n Confirmaci&oacute;n");
		}else{
			//Creacion de objeto EnvioCorreos
			InetAddress direccion = InetAddress.getLocalHost();
	    	String direccionIpstr = direccion.getHostAddress();
			ec = new EnvioCorreos(_dso, null, direccionIpstr);
			//Se inicializan los parametros relacionados con el envio de correos
			ec.initParamEnvio();
		}
		
		return valido;
	}
}
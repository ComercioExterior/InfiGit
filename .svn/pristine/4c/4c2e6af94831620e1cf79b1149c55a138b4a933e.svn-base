package com.bdv.infi.logic.interfaz_varias;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.StatusOrden;

/**
 * Clase NO UTILIZADA EN LA APLICACION (Ya el proceso de envio de correo no se realizar por programador de tareas, motivo por el cual la presente clase queda sin uso en la aplicacion)
 * Clase encargada de ejecutar el proceso de Envio de Correos, dependiendo de como este configurado en el SCHEDULER
 * @author Dayana Torres
 *  
 */

public class EnvioCorreosCall {//implements Job{
	private Logger logger = Logger.getLogger(EnvioCorreosCall.class);
	
	public void execute(String tipoDest, String area, EnvioCorreos ec, String idUnidadInversion, String idProducto, String statusOrden, int idEjecucion) throws Exception { //JobExecutionContext arg0) throws JobExecutionException {
		
		File dir = null;
		try{
			dir = new File(ec.parametros.get(ParametrosSistema.DESTINATION_PATH));
		}catch(Exception e){
			logger.info("No se pudo realizar el proceso de envio de correos puesto que la siguiente ruta configurada como directorio destino no existe:\n"+ParametrosSistema.DESTINATION_PATH, e);
		}
		
		try {
	    	
			if (dir.exists()) {
		    	boolean colaCreada = false;
		    	ec.fechaFile = ec.getCurrentFormatedDate(new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA_FILE)); //Fecha actual formateada
	            ec.rutaFileCola = ec.parametros.get(ParametrosSistema.DESTINATION_PATH) + File.separator + ec.parametros.get(ParametrosSistema.DIR_COLA_CORREOS) + File.separator + ec.parametros.get(ParametrosSistema.DIR_COLA_CORREOS) + "_" +  ec.fechaFile + ConstantesGenerales.EXTENSION_DOC_TXT;
	            
	            if( tipoDest.equals(ec.parametros.get(ParametrosSistema.DEST_CLIENTE)) ) { //Destinatario de tipo Cliente
	            	ArrayList<String[]> emails = new ArrayList<String[]>();
	            	//ArrayList<String[]> emails = ec.getEmailsCtes(idUnidadInversion, idProducto, statusOrden, idEjecucion);
	            	logger.info("EMAILS CANT: "+emails.size()+"\nEMAILS:\n");
	            	String arr[] = new String[2];
	            	for(int i=0; i<emails.size(); i++){
	            		arr = emails.get(i);
	            		logger.info(arr[0]+" - "+arr[1]+"\n");
	            	}
		            if(emails.size()>0) { //Si se obtienen los correos de los clientes
		            	if (ec.crearArchColaCorreos(ec.rutaFileCola, statusOrden, ec.parametros.get(ParametrosSistema.DEST_CLIENTE), emails )){ //Se creo/actualizo el archivo con la cola de correos a enviar
		            		colaCreada = true;	
		            		logger.info("Se creo el archivo cola de correos en la ruta:\n"+ec.rutaFileCola);
		                }else{
		                	logger.info("Ocurrieron problemas durante la creacion del archivo cola de correos en la ruta:\n"+ec.rutaFileCola);
		                }
		            }else{
		            	logger.info("No se hallaron clientes con órdenes en estatus '"+statusOrden+"' del producto '"+idProducto+"' con correos válidos");
		            }
	            }else{
	            	if( tipoDest.equals(ec.parametros.get(ParametrosSistema.DEST_FUNCIONAL)) ) { //Destinatario de tipo Funcional
	            		ArrayList<String[]> areaFunc = new ArrayList<String[]>();
	            		String emailNameCte[] = new String[2]; //Pos 0: Email Cliente; Pos 1: Nombre Cliente
	            		emailNameCte[0] = area;
	            		emailNameCte[1] = "";
	            		areaFunc.add(emailNameCte);
	            		if ( ec.crearArchColaCorreos(ec.rutaFileCola, statusOrden, ec.parametros.get(ParametrosSistema.DEST_FUNCIONAL), areaFunc ) ){ //Se creo/actualizo el archivo con la cola de correos a enviar
	            			colaCreada = true;	
	            			logger.info("Se creo el archivo cola de correos en la ruta:\n"+ec.rutaFileCola);
	            		}else{
		                	logger.info("No se pudo crear el archivo cola de correos en la ruta:\n"+ec.rutaFileCola);
		                }
	            	}
	            }
	            
		    	if(colaCreada) { //Si se creo el archivo con la cola de correos a enviar
					
//		    		Thread t = new Thread(ec); //Ejecucion del hilo que envia los correos
//		    		t.start();
					//NM29643 - infi_SICAD_II: Se comenta el join() para que el proceso que llama no se quede esperando por el proceso de envio de correos
		    		//t.join();
					
		    	}else logger.info("No se realizo el envio de correos");
			}else logger.info("No se pudo realizar el proceso de envio de correos puesto que la siguiente ruta configurada como directorio destino no existe:\n"+ec.parametros.get(ParametrosSistema.DESTINATION_PATH));
			
		} catch (Exception e) {
			logger.error(e);
			throw new Exception("Ocurrio un error en proceso de envio de correos " + e);
		}
	}
}
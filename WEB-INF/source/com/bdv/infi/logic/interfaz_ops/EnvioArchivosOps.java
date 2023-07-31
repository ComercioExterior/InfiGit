package com.bdv.infi.logic.interfaz_ops;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.sql.DataSource;

import megasoft.DataSet;

import org.apache.log4j.Logger;

import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.logic.interfaces.ControlProcesosOps;
import com.bdv.infi.util.FileUtil;

public class EnvioArchivosOps extends ProcesadorArchivosOps implements Runnable {
	
	static final Logger logger = Logger.getLogger(ProcesadorArchivosOps.class);	
	//HashMap<String, String> parametrosOPS;
	//HashMap<String, String> parametrosFTP;
	protected ProcesosDAO procesoDAO;
	protected Proceso proceso;
	protected DataSource _dso;
	protected int usuarioId;
	protected String parametroSistema;
	protected String nombreArchivoMainframe;	
	protected String consultaRegistros;
	protected DataSet _registros;
	
	boolean indProcesoEnvio=true;
	public EnvioArchivosOps(DataSource _dso,String grupoParamOps,String grupoParamFtp,int usuarioId,String userName,String query,ControlProcesosOps controlProc){		
		super(_dso,grupoParamOps,grupoParamFtp,usuarioId,userName,query,controlProc);
		
	}
	
	public void run() {
		FileUtil fileUtil=new FileUtil();
		
		try {
			if (verificarCiclo(this.controlProceso,indProcesoEnvio) &&//VERIFICAR CICLO
					comenzarProceso(this.controlProceso,indProcesoEnvio)) {//VERIFICAR PROCESO				
								
				parametrosFTP=obtenerParametros(this.grupoParamFtp);
				parametrosOPS=obtenerParametros(this.grupoParamOps);
				
				final File archivo = fileUtil.getArchivo(controlProceso.getNombreRutaEnvio(),this.parametrosOPS);	
				
				verificarArchivoFinal(archivo);
				envioArchivo();
				
				respaldarArchivo(archivo,this.controlProceso,indProcesoEnvio,true);
				
			}			
		} catch (Exception e) {
			//System.out.println("Exception --> " + e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {			
			terminarProceso();
			logger.info("Terminado el proceso de generación de archivo batch para adjudicación tipo subasta... ");
		}
		

		
		//VERIFICACION SI ARCHIVO EXISTE
		
		//ENVIO ARCHIVO
		
		//RESPALDO ARCHIVO
		
	}
	
	protected void procesar(){
		
	}

	/*protected void envioArchivo(){
		
	}*/
	
	protected void respaldarArchivo(){
		
	}


	
	protected String getNombreArchivo() {
		return this.controlProceso.getNombreArchivo();
	}
	
	protected String getDestinoFinal(){

		return parametrosOPS.get(getNombreArchivo());
        //return new File(archivoFinal);
	}
	
	
	/*protected void respaldarArchivo(File archivo,ControlProcesosOps controlOps, boolean borrarArchivo) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("carpetaRespaldo: " + getCarpetaRespaldo(controlOps.getNombreRutaRespaldo()));
		}
		logger.info("PROCESO DE RESPALDO DE ACHIVO ----------->");
		final String carpeta = getCarpetaRespaldo(controlOps.getNombreRutaRespaldo());

		final File carpetaRespaldo = new File(carpeta);
		carpetaRespaldo.mkdirs();

		// agregar fecha/hora al nombre del archivo respaldo
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(archivo.getName().substring(0, archivo.getName().length() - 4)); // nombre

			stringBuilder.append(controlOps.getProcesoEnvio());
		
		stringBuilder.append(sdfArchivoRespaldo.format(new Date()));
		stringBuilder.append(archivo.getName().substring(archivo.getName().length() - 4)); // extension

		File destino = new File(carpeta.concat(stringBuilder.toString()));

		logger.info("Respaldar: " + archivo.getAbsolutePath() + " a: " + destino.getAbsolutePath());

		FileUtil.copiarArchivo(archivo, destino);
		// if (archivo.renameTo(destino)) {
		if (borrarArchivo) {
			archivo.delete();
		}
		// }
	}*/

	protected String getCarpetaRespaldo(String rutaRespaldo) {
		String carpeta = parametrosOPS.get(rutaRespaldo);
		if(!carpeta.endsWith(File.separator)){
			carpeta = carpeta.concat(File.separator);
		}
		return carpeta;
	}	
	
	
	
	
}

package com.bdv.infi.logic.cierre_sistema;

import java.util.Date;

import javax.sql.DataSource;

import megasoft.db;

import org.apache.log4j.Logger;

import com.bdv.infi.dao.CierreSistemaDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.UsuarioDAO;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.util.Utilitario;

public class CierreSistema implements Runnable {
//extends Liquidacion 

	private Logger logger = Logger.getLogger(CierreSistema.class);
	private DataSource _dso;
	private String ip;
	private Proceso proceso;
	private ProcesoCierreSistema procesoCierreSistema;
	private String userName;
	private String tipoPeticion;
	private String tipoEjecucion;
	
	public CierreSistema(DataSource datasource, String ip, String usuario, String tipoPeticion,String tipoEjecucion) {
		this._dso = datasource;
		this.ip = ip;
		this.userName = usuario;
		this.tipoPeticion = tipoPeticion;	
		this.tipoEjecucion=tipoEjecucion;
	}
	
	public void run() {
		
		logger.info(" ************ Iniciando el proceso de Cierre de Sistema ************");
		String tipoProceso = TransaccionNegocio.PROC_CIERRE_SISTEMA; //tipo proceso cierre de sistema
				
		try {
			CierreSistemaDAO cierreSistemaDAO = new CierreSistemaDAO(_dso);
			//Validar si el cierre de sistema esta activo y sin fallas
			
			if (!cierreSistemaDAO.isProcesoCierreActivo()){					
				logger.info("************* El PROCESO DE CIERRE SISTEMA no se encuentra activo *************");	
			}else if(cierreSistemaDAO.existeFallaProcesoCierre()){
				logger.info("************* Existe un PROCESO DE CIERRE SISTEMA activo con fallas por lo cual no es posible iniciar un nuevo cierre de sistema *************");
			}
					
			
			//Llamando a los procesos que conforman el cierre de sistema...
			procesoCierreSistema = new ProcesoCierreSistema(this._dso, this.ip, userName, tipoPeticion); //El usuario 
			procesoCierreSistema.procesarCierre(tipoEjecucion);
								
				/*
				 ****Modificacion incidencia Calidad SICAD_2 NM26659
				  EL CONTROL DEL PROCESO DE CIERRE SISTEMA SE LLEVA A CABO DESDE EL PROCEDURE****
				 **/
				
				//Verificar si no existe un proceso de cierre en ejecución
				/*if(!Utilitario.procesoEnEjecución(tipoProceso, _dso)){
					logger.info("Comenzando el proceso tipo " + tipoProceso + " -------------------> ");
					//Crear Registro de proceso	
					iniciarProceso(tipoProceso);		
										
					//Llamando a los procesos que conforman el cierre de sistema...
					procesoCierreSistema = new ProcesoCierreSistema(this._dso, this.ip, userName, tipoPeticion); //El usuario 
					procesoCierreSistema.procesarCierre();
				}else{
					logger.info("Proceso: " + tipoProceso + " ya se encuentra esta en ejecución.");
				}*/
										
		} catch (Exception ex){
			logger.error(" Error en el proceso de cierre de sistema: ", ex);			
			if(proceso != null) {
				proceso.setDescripcionError(ex.getMessage());
			}			 
			
		/*} finally {				
			try {
				terminarProceso();
			} catch (Exception e) {			
				logger.error("Error Terminando el proceso de cierre de sistema", e);				
			}			
			logger.info("Terminado el proceso de cierre de sistema");		
		}*/
		
	
		}
	}
	/**
	 * Escribe el archivo log del cierre del sistema en una ruta especificada
	 */
	/*private void escribirArchivoLogCierreSistema() {
		
		String separador = String.valueOf(File.separatorChar);
		Date hoy = new Date();		
		String fechaHora = String.valueOf(hoy.getTime());
		String rutaArchivo = get.getRealPath("WEB-INF") + separador + "logs" + separador + "cierre_log"+ fechaHora + ".log" ;
		FileUtil.put(rutaArchivo, archivoLog, false);
	}*/

	private void terminarProceso() throws Exception {
		ProcesosDAO procesoDAO = new ProcesosDAO(_dso);
		if(proceso != null) {
			proceso.setFechaFin(new Date());
			if (proceso.getDescripcionError() == null) {
				proceso.setDescripcionError("");
			}				
			db.exec(this._dso, procesoDAO.modificar(proceso));	
		}
	}

	private void iniciarProceso(String tipoProceso) throws Exception {
		
		ProcesosDAO procesoDAO = new ProcesosDAO(_dso);
		int usuarioId = 0;
		UsuarioDAO usuarioDAO = new UsuarioDAO(_dso);
		
		if(userName!=null){
			usuarioId = Integer.parseInt(usuarioDAO.idUserSession(userName));
		}
		
		proceso = new Proceso();
		proceso.setTransaId(tipoProceso);
		proceso.setUsuarioId(usuarioId);
		proceso.setFechaInicio(new Date());
		proceso.setFechaValor(new Date());
		
		procesoDAO.insertar(proceso);
		/* Primero creamos el proceso */
		db.exec(_dso, procesoDAO.insertar(proceso));		
	}
}


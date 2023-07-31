package models.intercambio.transferencia.generar_archivo_subasta_divisas;


import java.util.ArrayList;
import java.util.Date;

import javax.sql.DataSource;

import megasoft.db;

import org.apache.log4j.Logger;

import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.data.Archivo;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.PlantillasMailTipos;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaz_varias.CallEnvioCorreos;

public class ActualizacionOrdenesInfi implements Runnable{

	
	Logger logger = Logger.getLogger(ActualizacionOrdenesInfi.class);
	DataSource _dso;
	int idUnidadInversion;
	int usuarioId;
	//NM29643 INFI_TTS_466 Actualizacion de proceso de envio de correos 21/07/2014
	String user;
	Proceso proceso;
	private Archivo archivoProcesar;
	private boolean actualizarSolicitudSitme;
	private boolean llamarEnvioCorreos;
	
	//NM29643 INFI_TTS_466 Actualizacion de proceso de envio de correos 21/07/2014
	public ActualizacionOrdenesInfi(DataSource datasource, int idUnidadInversion, int usuarioId, String user, Archivo archivo, boolean updSolicitudSitme, boolean llamarEnvioCorreos) {
		this._dso = datasource;
		this.idUnidadInversion = idUnidadInversion;
		this.usuarioId = usuarioId;
		this.user = user;
		this.archivoProcesar = archivo;
		this.actualizarSolicitudSitme = updSolicitudSitme;
		this.llamarEnvioCorreos = llamarEnvioCorreos;
	}
	
	public void run() {
		ControlArchivoDAO controlArchivoDAO = null;
		OrdenDAO ordenDAO = null;
		logger.info(" ************ Iniciando el proceso de Actualización de Ordenes en INFI (Ordenes enviadas) ************");
		String tipoProceso = TransaccionNegocio.PROC_ACT_CLIENTES_OPICS;
		ArrayList<String> querysEjecutar = new ArrayList<String>();			
		try {
			controlArchivoDAO = new ControlArchivoDAO(this._dso);
			
			//Crear Registro de proceso	
			logger.info("Comenzando el proceso " + tipoProceso + " -------------------> ");
			
			iniciarProceso(tipoProceso);		
			
			//NM29643 infi_TTS_466
			archivoProcesar.setIdEjecucion(proceso.getEjecucionId());
			
			//Llamar a la actualización de las ordenes en INFI
			querysEjecutar.add(controlArchivoDAO.insertarArchivoTransf(archivoProcesar));
			
			controlArchivoDAO.modificarStatusOrdenesIN(archivoProcesar, querysEjecutar);
			
			modificarOrdenesSolicitudesSitme(StatusOrden.REGISTRADA,archivoProcesar, querysEjecutar);
			
			controlArchivoDAO.ejecutarStatementsBatch(querysEjecutar);
						
		} catch (Exception ex){
			logger.error(" Error en el proceso de actualización de las ordenes: ", ex);
			if(proceso != null) {
				proceso.setDescripcionError(ex.getMessage());
			}			 
			
		} finally {				
			try {
				terminarProceso();
				
				if(llamarEnvioCorreos){
					//NM29643 infi_TTS_466 17/07/2014: Actualizacion del proceso de envio de correos
					logger.debug("UI ID en ActualizacionOrdenesInfi (Exportacion RED) - Llamada a ENVIO CORREOS -----------------: "+idUnidadInversion);
					logger.debug("EJECUCION_ID de las ordenes a considerar - Llamada a ENVIO CORREOS -----------------: "+String.valueOf(archivoProcesar.getIdEjecucion()));
					//*************LLAMADA AL PROCESO DE ENVIO DE CORREOS (Desde Aqui)**************
					CallEnvioCorreos callEnvio = new CallEnvioCorreos(ConstantesGenerales.ENVIO_BCV_RED, PlantillasMailTipos.TIPO_DEST_CLIENTE_COD, user, String.valueOf(idUnidadInversion), _dso, String.valueOf(archivoProcesar.getIdEjecucion()), null, null);
					Thread t = new Thread(callEnvio); //Ejecucion del hilo que envia los correos
					t.start();
					//*************LLAMADA AL PROCESO DE ENVIO DE CORREOS (Hasta Aqui)**************
				}
				
			} catch (Exception e) {			
				logger.error("Error Terminando el proceso de actualización de Ordenes en INFI (Ordenes enviadas)", e);
			}			
			logger.info("Terminado el proceso de actualización de Ordenes en INFI (Ordenes enviadas)");
		}

	}

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
		proceso = new Proceso();
		proceso.setEjecucionId(Integer.parseInt(procesoDAO.dbGetSequence(_dso, ConstantesGenerales.SECUENCIA_PROCESOS)));
		proceso.setTransaId(tipoProceso);
		proceso.setUsuarioId(usuarioId);
		proceso.setFechaInicio(new Date());
		proceso.setFechaValor(new Date());
		
		procesoDAO.insertar(proceso);
		/* Primero creamos el proceso */
		db.exec(_dso, procesoDAO.insertar(proceso));		
	}
	
	private void modificarOrdenesSolicitudesSitme(String estatusAnterior,Archivo archivoProcesar, ArrayList querysEjecutar)throws Exception {		
		if (actualizarSolicitudSitme) {
			OrdenDAO ordenDAO = new OrdenDAO(_dso);
			ordenDAO.actualizarSolicitudesSitmePorLotes(estatusAnterior, archivoProcesar, querysEjecutar);
		}		
	}
}

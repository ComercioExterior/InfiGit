package models.intercambio.transferencia.generar_archivo_subasta_divisas;


import java.util.Date;
import javax.sql.DataSource;
import megasoft.db;
import org.apache.log4j.Logger;
import com.bdv.infi.dao.ActualizacionClientesOpicsDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

public class ActualizacionClientesOpics implements Runnable{

	
	Logger logger = Logger.getLogger(ActualizacionClientesOpics.class);
	DataSource _dso;
	int idUnidadInversion;
	int usuarioId;
	Proceso proceso;
	
	public ActualizacionClientesOpics(DataSource datasource, int idUnidadInversion, int usuarioId) {
		this._dso = datasource;
		this.idUnidadInversion = idUnidadInversion;
		this.usuarioId = usuarioId;
	}
	
	public void run() {
		
		logger.info(" ************ Iniciando el proceso de Actualización de Clientes en Opics para las órdenes enviadas ************");
		String tipoProceso = TransaccionNegocio.PROC_ACT_CLIENTES_OPICS;
				
		try {
			//Crear Registro de proceso						
			
			logger.info("Comenzando el proceso " + tipoProceso + " -------------------> ");
			
			iniciarProceso(tipoProceso);		
			
			//Llamar a la actualización de clientes
			ActualizacionClientesOpicsDAO actualizacionClientesOpicsDAO = new ActualizacionClientesOpicsDAO(_dso);
			actualizacionClientesOpicsDAO.actualizarClientesOpics(idUnidadInversion, StatusOrden.ENVIADA);
			
		} catch (Exception ex){
			logger.error(" Error en el proceso de actualización de clientes en Opics para las órdenes enviadas: ", ex);
			if(proceso != null) {
				proceso.setDescripcionError(ex.getMessage());
			}			 
			
		} finally {				
			try {
				terminarProceso();
			} catch (Exception e) {			
				logger.error("Error Terminando el proceso de actualización de clientes en Opics para las órdenes enviadas", e);
			}			
			logger.info("Terminado el proceso de actualización de clientes en Opics para las órdenes enviadas");
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
		proceso.setTransaId(tipoProceso);
		proceso.setUsuarioId(usuarioId);
		proceso.setFechaInicio(new Date());
		proceso.setFechaValor(new Date());
		
		procesoDAO.insertar(proceso);
		/* Primero creamos el proceso */
		db.exec(_dso, procesoDAO.insertar(proceso));		
	}
}

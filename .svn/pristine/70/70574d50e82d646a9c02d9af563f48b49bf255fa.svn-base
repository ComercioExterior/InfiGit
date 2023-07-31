package com.bdv.infi.logic.interfaz_ops;

import java.io.File;

import javax.sql.DataSource;

import megasoft.DataSet;

import org.apache.log4j.Logger;

import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

public class LiquidacionRecepcionSitme extends Liquidacion implements Runnable {
	static final Logger logger = Logger.getLogger(LiquidacionRecepcionSitme.class);

	public LiquidacionRecepcionSitme(DataSource datasource) {
		super();

		this._dso = datasource;
	}	

	public void run() {
		try {			
			logger.info("Iniciando el proceso de recepción de archivo batch para liquidación tipo sitme... ");
			obtenerParametros();			
			final File archivo = getArchivoRecepcionLiquidacionSitme();
			if (archivo.exists()){				
				if (verificarCiclo("'"+TransaccionNegocio.CICLO_BATCH_SITME+"'", "") &&
				comenzarProceso(TransaccionNegocio.PROC_BATCH_LIQ_SITME_RECEP)) {
	
					//Verifica permisos
					tienePermisosLectura(archivo);
	
					recepcionArchivo(archivo);
				}
			}else{
				logger.info("El archivo de recepcion no existe: " + archivo.getAbsolutePath());
			}
		} catch (Exception ex) {
			logger.error("Error en el proceso de recepción archivo batch para liquidación tipo sitme. ", ex);

			if(proceso != null) {
				proceso.setDescripcionError(ex.getMessage());
			}
		} finally {			
			terminarProceso();
			logger.info("Terminado el proceso de recepción de archivo batch para liquidación tipo sitme... ");
		}
	}
	/*
	 * (non-Javadoc)
	 * @see com.bdv.infi.logic.interfaz_ops.BatchOps#actualizarUnidadInversion()
	 * Metodo vacio para no actualizar la unidad de inversion
	 */
	protected void actualizarUnidadInversion() {
		
	}
	
	/**
	 * Obtiene la carpeta de respaldo
	 */
	protected String getCarpetaRespaldo() {
		String carpeta = parametrosOPS.get(ParametrosSistema.RUTA_SITME_ADJ_RESPALDO);
		if(!carpeta.endsWith(File.separator)){
			carpeta = carpeta.concat(File.separator);
		}
		return carpeta;
	}
	
	/**
	 * Verifica si puede ejecutar la generación del archivo. Para ello debe existir ciclo abierto
	 */
	protected boolean verificarCiclo(String ciclo, String status) throws Exception {
		boolean puedeEjecutar = false;
		
		// si estado de la unidad de inversion es adecuado para la liquidacion
		ControlArchivoDAO controlArchivoDAO = new ControlArchivoDAO(_dso);

		controlArchivoDAO.listarEnvioPorRecepcionBatch(ciclo);
		DataSet dataset = controlArchivoDAO.getDataSet();

		if(dataset.count() > 0 ) {
			dataset.first();
			dataset.next();
			//Verifica si el valor es de ADJUDICACIÓN
			if (dataset.getValue("in_recepcion").equals(TIPO_LIQUIDACION)){
				puedeEjecutar = true;
			}else{
				logger.info("No se espera archivo de recepción de liquidación de SITME");	
			}
		}
		return puedeEjecutar;
	}	
}

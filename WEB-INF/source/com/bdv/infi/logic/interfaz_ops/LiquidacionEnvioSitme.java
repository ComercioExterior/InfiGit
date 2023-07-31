package com.bdv.infi.logic.interfaz_ops;

import java.io.File;

import javax.sql.DataSource;

import megasoft.DataSet;

import org.apache.log4j.Logger;

import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

public class LiquidacionEnvioSitme extends Liquidacion implements Runnable {

	Logger logger = Logger.getLogger(LiquidacionEnvioSitme.class);

    
	public LiquidacionEnvioSitme(DataSource datasource, int usuarioId,
			String nmUsuario, int idUnidadInversion) {
		this._dso = datasource;
		this.usuarioId = usuarioId;
		this.nmUsuario = nmUsuario;
		this.idUnidadInversion = idUnidadInversion;
	}
	
	public void run() {		
		///
		try {
			logger.info("Iniciando el proceso de generación de archivo batch para liquidación tipo sitme... ");

			if (verificarCiclo("'"+TransaccionNegocio.CICLO_BATCH_SITME+"'", "") &&			
			comenzarProceso(TransaccionNegocio.PROC_BATCH_LIQ_SITME_ENVIO)) {
  
				obtenerParametros();

				final File archivo = getArchivoEnvioLiquidacionSitme();

				//Verifica permisos
				tienePermisosEscritura(archivo);
				//tienePermisosEscritura(new File(getArchivoTemporal(getNombreArchivo())));
				verificarArchivoFinal(archivo);
				
				envioArchivo(ConstantesGenerales.OPS_MONEDA_NACIONAL);
				respaldarArchivo(archivo,true);
			}
		} catch (Exception ex) {
			logger.error("Error en el proceso de generación de archivo batch para liquidación tipo sitme. ", ex);

			if(proceso != null) {
				proceso.setDescripcionError(ex.getMessage());
			}
		} finally {			
			terminarProceso();
			logger.info("Terminado el proceso de generación de archivo batch para liquidación tipo sitme... ");
		}
	}
	
	protected String getRegistrosAProcesar() throws Exception{
		inversionDAO = new UnidadInversionDAO(this._dso);
		return inversionDAO.detalleDeOperacionBatchPorUnidadLiquidacion(idUnidadInversion,true);		
	}
	
	protected String getDestinoFinal(){
		//Directorio temp para guardar los archivos
//		String archivoFinal = "";
//        if (parametrosOPS.get(ParametrosSistema.RUTA_SITME_ADJ_ENVIO).endsWith(File.separator)){
//        	archivoFinal = parametrosOPS.get(ParametrosSistema.RUTA_SITME_ADJ_ENVIO) + parametrosOPS.get(getNombreArchivo()); 
//        }else{
//        	archivoFinal = parametrosOPS.get(ParametrosSistema.RUTA_SITME_ADJ_ENVIO) + File.separator + parametrosOPS.get(getNombreArchivo());
//        }
//        return new File(archivoFinal);
		return parametrosOPS.get(getNombreArchivo());
	}
	
	protected String getCiclo(){
		return TransaccionNegocio.CICLO_BATCH_SITME;
	}
	
	protected String getNombreArchivo(){
		return ParametrosSistema.NOMBRE_ARCH_SITME_ADJ;
	}
	
	/**
	 * Verifica si puede ejecutar la generación del archivo. Para ello no debe existir ciclo abierto
	 * y la unidad de inversión debe estar CERRADA
	 */	
	protected boolean verificarCiclo(String ciclo, String status) throws Exception {
		boolean puedeEjecutar = false;
		
		// si estado de la unidad de inversion es adecuado para la liquidacion
		ControlArchivoDAO controlArchivoDAO = new ControlArchivoDAO(_dso);

		controlArchivoDAO.listarEnvioPorRecepcionBatch(ciclo);
		DataSet dataset = controlArchivoDAO.getDataSet();

		if(dataset.count() == 0 ) {
			puedeEjecutar = true;
		}
		return puedeEjecutar;
	}
	
	protected void obtenerArchivoTemporal(){
		//Directorio temp para guardar los archivos
        archivoTemporal = parametrosOPS.get(ParametrosSistema.RUTA_SITME_ADJ_ENVIO);
	}
	
	protected String getCarpetaRespaldo() {
		String carpeta = parametrosOPS.get(ParametrosSistema.RUTA_SITME_ADJ_RESPALDO);
		if(!carpeta.endsWith(File.separator)){
			carpeta = carpeta.concat(File.separator);
		}
		return carpeta;
	}		
}

package com.bdv.infi.logic.interfaz_ops;

import java.io.File;

import javax.sql.DataSource;

import megasoft.DataSet;
import megasoft.db;

import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.PlantillasMailTipos;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaz_varias.CallEnvioCorreos;

public class AbonoRecepcionSitmeCuentaNacionalMonedaExtranjera extends CuentaNacionalMonedaExtranjera implements Runnable{

	//NM29643 INFI_TTS_466
	private String userNM = null;
	
	public AbonoRecepcionSitmeCuentaNacionalMonedaExtranjera(DataSource datasource){
		super();

		this._dso = datasource;
		
	}	

	public void run() {
		
		//NM29643 - infi_TTS_466
		String idEjecucion = "";
		try {			
			logger.info("Iniciando el proceso de recepción de archivo batch para abono cuenta nacional en moneda extranjera sitme... ");
			
			obtenerParametros();			
			final File archivo = getArchivoRecepcionMonedaExtranjeraSitme();
			logger.debug("RESULTADO EXISTS DEL ARCHIVO DE ABONOO CTA EN DOLARES---------------"+archivo.exists());
			if (archivo.exists()){
				logger.debug("EXISTE EL ARCHIVO!!-------------");
				if (verificarCiclo("'"+TransaccionNegocio.CICLO_BATCH_CTA_NACIONAL_MONEDA_EXT_SUB_DIV_P+"'", "") &&
				comenzarProceso(TransaccionNegocio.PROC_BATCH_CTA_NACIONAL_MONEDA_EXT_SUBV_P_RECEP)) {
	
					logger.debug("Ciclo abiertoooooo!! y Proceso "+proceso.getEjecucionId()+" iniciado-------------");
					//Verifica permisos
					tienePermisosLectura(archivo);
					this.idTipoMoneda=ConstantesGenerales.OPS_MONEDA_EXTRANJERA;
					idEjecucion = recepcionArchivo(archivo);
				//	actualizarOrdenes();
					
					logger.debug("EJECUCION_ID de las ordenes a considerar - Llamada a ENVIO CORREOS -----------------: "+idEjecucion);
					//*************LLAMADA AL PROCESO DE ENVIO DE CORREOS (Desde Aqui)**************
					CallEnvioCorreos callEnvio = new CallEnvioCorreos(ConstantesGenerales.LIQUIDACION_EFECTIVO, PlantillasMailTipos.TIPO_DEST_CLIENTE_COD, userNM, null, _dso, idEjecucion, null, null);
					Thread t = new Thread(callEnvio); //Ejecucion del hilo que envia los correos
					t.start();
					//*************LLAMADA AL PROCESO DE ENVIO DE CORREOS (Hasta Aqui)**************
					
				}
			}else{
				logger.info("El archivo de recepcion no existe: " + archivo.getAbsolutePath());
			}
		} catch (Exception ex) {
			logger.error("Error en el proceso de recepción archivo batch para abono cuenta nacional en moneda extranjera sitme. ", ex);

			if(proceso != null) {
				proceso.setDescripcionError(ex.getMessage());
			}
		} finally {			
			terminarProceso();
			logger.info("Terminado el proceso de recepción de archivo batch para abono cuenta nacional en moneda extranjera sitme... ");
				
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
		String carpeta = parametrosOPS.get(ParametrosSistema.RUTA_SITME_MONEDA_EXT_RESP);
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
			logger.info(" VALOR DE IN_COMISION " + dataset.getValue("in_recepcion"));
			if (dataset.getValue("in_recepcion").equals(TIPO_ABONO_CUENTA_DOLARES)){//Validacion de tipo de transaccion nueva Abono de cuenta nacional en dolares
				puedeEjecutar = true;
			}else{
				logger.info("No se espera archivo de recepción de abono cuenta nacional en moneda extranjera SITME");	
			}
		}
		return puedeEjecutar;
	}	
	
	/**
	 * Actualiza las ordenes si todas sus operaciones están APLICADAS 
	 * @param unidadInversion unidad de inversión para filtrar las ordenes que se deben actualizar
	 * @throws Exception en caso de error
	 */
	/*protected void actualizarOrdenes() throws Exception{
		String[] sql = this.ordenDAO.actualizarOrdenesRecepcionSitmeCuentaDolares();
		db.execBatch(_dso, sql);
	}*/

}

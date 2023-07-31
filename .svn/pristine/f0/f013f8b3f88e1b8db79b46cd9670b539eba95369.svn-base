package com.bdv.infi.logic.interfaz_ops;

import java.io.File;

import javax.sql.DataSource;

import megasoft.DataSet;

import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.data.TransaccionFija;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import static com.bdv.infi.logic.interfaces.ConstantesGenerales.*;
import static com.bancovenezuela.comun.logic.interfaces.ConstantesGenerales.*;


public class AbonoRecepcionCuentaNacionalMonedaExtranjeraPagoCupones extends CuentaNacionalMonedaExtranjeraPagoCupones implements Runnable{
	

	public AbonoRecepcionCuentaNacionalMonedaExtranjeraPagoCupones(DataSource datasource) {
		super();

		this._dso = datasource;
	}	

	public void run() {
		
		try {			
			logger.info("Iniciando el proceso de recepción de archivo batch para abono cuenta nacional en moneda extranjera sitme... ");
			
			obtenerParametros();			
			final File archivo = getArchivoRecepcionMonedaExtranjeraVentaTitulo();
			if (archivo.exists()){				
				if (verificarCiclo("'"+TransaccionNegocio.CICLO_BATCH_CTA_NACIONAL_MONEDA_EXT_PAGO_CUPON+"'", "") &&
				comenzarProceso(TransaccionNegocio.PROC_BATCH_CTA_NAC_MON_EXT_CUPON_RECEP)) {
	
					//Verifica permisos
					tienePermisosLectura(archivo);
					this.idTipoMoneda=ConstantesGenerales.OPS_MONEDA_EXTRANJERA;
					recepcionArchivo(archivo,TRANSACCION_ORDEN_PAGO);
				//	actualizarOrdenes();
				}
			}else{
				logger.info("El archivo de recepcion no existe: " + archivo.getAbsolutePath());
			}
		} catch (Exception ex) {
			logger.error("Error en el proceso de recepción archivo batch para abono cuenta nacional en moneda extranjera Pago Cupón... ", ex);

			if(proceso != null) {
				proceso.setDescripcionError(ex.getMessage());
			}
		} finally {			
			terminarProceso();
			logger.info("Terminado el proceso de recepción de archivo batch para abono cuenta nacional en moneda extranjera Pago Cupón... ");
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
		String carpeta = parametrosOPS.get(ParametrosSistema.RUTA_CUPON_MONEDA_EXT_RESP);
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
			if (dataset.getValue("in_recepcion").equals(TIPO_ABONO_CUENTA_DOLARES_CUPON)){//Validacion de tipo de transaccion nueva Abono de cuenta nacional en dolares
				puedeEjecutar = true;
			}else{
				logger.info("No se espera archivo de recepción de abono cuenta nacional en moneda extranjera Pago Cupon");	
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

package com.bdv.infi.logic.interfaz_ops;


import java.io.File;

import javax.sql.DataSource;

import megasoft.DataSet;

import org.apache.log4j.Logger;

import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

public class AbonoCuentaNacionalMonedaExtranjeraVentaTitulo extends CuentaNacionalMonedaExtranjeraVentaTitulo implements Runnable{

	
	Logger logger = Logger.getLogger(AbonoCuentaNacionalMonedaExtranjera.class);
	
	public AbonoCuentaNacionalMonedaExtranjeraVentaTitulo(DataSource datasource, int usuarioId,
			String nmUsuario, String idTitulo) {
		this._dso = datasource;
		this.usuarioId = usuarioId;
		this.nmUsuario = nmUsuario;
		this.idTitulo = idTitulo;
	}
	
	public void run() {
		
		logger.info(" ************ Iniciando el proceso de generación de archivo batch para Abono en cuenta nacional de moneda Extranjera tipo VENTA TITULO ************");
		
		try {
			
			if (verificarCiclo("'"+TransaccionNegocio.CICLO_BATCH_CTA_NACIONAL_MONEDA_EXT_VENTA_TITULO+"'", "") &&								
					comenzarProceso(TransaccionNegocio.PROC_BATCH_CTA_NAC_MON_EXT_VENTA_ENVIO)) {	
				
				obtenerParametros();						
				final File archivo = getArchivoEnvioMonedaExtranjeraVentaTitulo();
						
				//verificarArchivoFinal(archivo);					
				envioArchivo(ConstantesGenerales.OPS_MONEDA_EXTRANJERA);			
				respaldarArchivo(archivo,true);
			} else {				
				logger.info("Existe un ciclo de abono cuenta nacional en dolares para Venta de Títulos Activo o Error en registro de proceso de Abono en moneda extranjera Venta Título");
			}
			
		} catch (Exception ex){
			logger.error(" Error en el proceso de generacion de archivo de abono cuenta nacional en moneda extranjera Venta Título ----> "  + ex.getMessage());
			if(proceso != null) {
				proceso.setDescripcionError(ex.getMessage());
			}
		} finally {			
			terminarProceso();
			logger.info("Terminado el proceso de generación de archivo batch para Abono cuenta nacional moneda extranjera Venta Título... ");
		}
	}
	
	
	protected String getRegistrosAProcesar() throws Exception{
		ordenDAO = new OrdenDAO(_dso);
		return ordenDAO.detalleDeAbonoCuentaDolaresVentaTitulo(idTitulo);		
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
		return TransaccionNegocio.CICLO_BATCH_CTA_NACIONAL_MONEDA_EXT_VENTA_TITULO;
	}
	
	protected String getNombreArchivo(){
		return ParametrosSistema.NOMBRE_ARCH_VENTA_MONEDA_EXT;
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
        archivoTemporal = parametrosOPS.get(ParametrosSistema.RUTA_VENTA_MONEDA_EXT_ENVIO);
	}
	
	protected String getCarpetaRespaldo() {
		String carpeta = parametrosOPS.get(ParametrosSistema.RUTA_VENTA_MONEDA_EXT_RESP);
		if(!carpeta.endsWith(File.separator)){
			carpeta = carpeta.concat(File.separator);
		}
		return carpeta;
	}		
	

}

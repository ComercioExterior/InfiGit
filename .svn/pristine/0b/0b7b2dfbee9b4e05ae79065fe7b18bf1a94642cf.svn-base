package com.bdv.infi.logic.interfaz_ops;

import java.io.File;
import java.util.ArrayList;

import javax.sql.DataSource;

import megasoft.DataSet;

import org.apache.log4j.Logger;

import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

public class LiquidacionEnvioSubasta extends Liquidacion implements Runnable {

	Logger logger = Logger.getLogger(LiquidacionEnvioSubasta.class);
	private ArrayList<String> statusUnidadInv=new ArrayList<String>();
    
	public LiquidacionEnvioSubasta(DataSource datasource, int usuarioId,
			String nmUsuario, int idUnidadInversion,String procesoEjecutar,String tipoCiclo) {
		this._dso = datasource;
		this.usuarioId = usuarioId;
		this.nmUsuario = nmUsuario;
		statusUnidadInv.add(UnidadInversionConstantes.UISTATUS_LIQUIDADA);
		this.idUnidadInversion = idUnidadInversion;	
		this.setProcesoEjecucion(procesoEjecutar);
		this.setTipoCiclo(tipoCiclo);//
	}
	
	public void run() {		
		///
		try {
			logger.info("Iniciando el proceso de generación de archivo batch para liquidación tipo subasta... ");
			//System.out.println("Iniciando el proceso de generación de archivo batch para liquidación tipo subasta... ");
			//if (verificarCiclo("'"+TransaccionNegocio.CICLO_BATCH_SUBASTA+"'", UnidadInversionConstantes.UISTATUS_LIQUIDADA) &&	
			if (verificarCiclo(statusUnidadInv,TransaccionNegocio.CICLO_BATCH_SUBASTA,TransaccionNegocio.CICLO_BATCH_SUBASTA_DIVISAS,TransaccionNegocio.CICLO_BATCH_SUBASTA_DIVISAS_PERSONAL) &&
					comenzarProceso(procesoEjecucion,TransaccionNegocio.PROC_BATCH_LIQ_SUBASTA_ENVIO,TransaccionNegocio.PROC_BATCH_LIQ_SUBASTA_DIVISAS_PERSONAL_ENVIO)){
					//comenzarProceso(TransaccionNegocio.PROC_BATCH_LIQ_SUBASTA_ENVIO)) {
				
				obtenerParametros();				
				final File archivo = getArchivoEnvioLiquidacionSubasta();
				
				//Verifica permisos
				tienePermisosEscritura(archivo);
				//tienePermisosEscritura(new File(getArchivoTemporal(getNombreArchivo())));
				verificarArchivoFinal(archivo);
				
				envioArchivo(ConstantesGenerales.OPS_MONEDA_NACIONAL);				
				respaldarArchivo(archivo,true);
				
			}
		} catch (Exception ex) {
			logger.error("Error en el proceso de generación de archivo batch para liquidación tipo subasta. ", ex);

			if(proceso != null) {
				proceso.setDescripcionError(ex.getMessage());
			}
		} finally {			
			terminarProceso();
			logger.info("Terminado el proceso de generación de archivo batch para liquidación tipo subasta... ");
		}
	}
	
	protected String getCarpetaRespaldo() {
		String carpeta = parametrosOPS.get(ParametrosSistema.RUTA_SUBASTA_ADJ_RESPALDO);
		if(!carpeta.endsWith(File.separator)){
			carpeta = carpeta.concat(File.separator);
		}
		return carpeta;
	}
	
	protected String getRegistrosAProcesar() throws Exception{
		inversionDAO = new UnidadInversionDAO(this._dso);
		return inversionDAO.detalleDeOperacionBatchPorUnidadLiquidacion(idUnidadInversion,false);		
	}
	
	protected String getDestinoFinal(){
		//Directorio temp para guardar los archivos
//		String archivoFinal = "";
//        if (parametrosOPS.get(ParametrosSistema.RUTA_SUBASTA_ADJ_ENVIO).endsWith(File.separator)){
//        	archivoFinal = parametrosOPS.get(ParametrosSistema.RUTA_SUBASTA_ADJ_ENVIO) + parametrosOPS.get(getNombreArchivo()); 
//        }else{
//        	archivoFinal = parametrosOPS.get(ParametrosSistema.RUTA_SUBASTA_ADJ_ENVIO) + File.separator + parametrosOPS.get(getNombreArchivo());
//        }
        return parametrosOPS.get(getNombreArchivo());
        //return new File(archivoFinal);
	}
	
	protected String getCiclo(){
		//return TransaccionNegocio.CICLO_BATCH_SUBASTA;
		return super.getTipoCiclo();
	}
	
	protected String getNombreArchivo(){
		return ParametrosSistema.NOMBRE_ARCH_SUBASTA_ADJ;
	}
	
	/**
	 * Verifica si puede ejecutar la generación del archivo. Para ello no debe existir ciclo abierto
	 * y la unidad de inversión debe estar CERRADA
	 */	
	protected boolean verificarCiclo(String ciclo, String status) throws Exception {
		boolean puedeEjecutar = false;
		
		// si estado de la unidad de inversion es adecuado para la liquidacion
		ControlArchivoDAO controlArchivoDAO = new ControlArchivoDAO(_dso);
		UnidadInversionDAO inversionDAO = new UnidadInversionDAO(_dso);

		controlArchivoDAO.listarEnvioPorRecepcionBatch(ciclo);
		DataSet dataset = controlArchivoDAO.getDataSet();

		if(dataset.count() == 0 ) {
				inversionDAO.listarPorId(idUnidadInversion);
							
				DataSet dataset2 = inversionDAO.getDataSet();
				
				if(dataset2.count() > 0 ) {
					dataset2.next();
					puedeEjecutar = dataset2.getValue("UNDINV_STATUS").equals(status);
				}
		}
		
		return puedeEjecutar;
	}
	
	//Metodo incluido en desarrollo requerimiento TTS-401 NM26659 
	protected boolean verificarCiclo(ArrayList<String> status,String... ciclos) throws Exception {
		logger.info("******** VERIFICACION DE CICLOS ********");
		StringBuffer ciclosVerificar=new StringBuffer();
		boolean puedeEjecutar = false;
		
		// si estado de la unidad de inversion es adecuado para la liquidacion
		ControlArchivoDAO controlArchivoDAO = new ControlArchivoDAO(_dso);
		UnidadInversionDAO inversionDAO = new UnidadInversionDAO(_dso);
		
		int count=0;
		for (String element : ciclos) {
			if(count>0){
				ciclosVerificar.append(",");
			}
			ciclosVerificar.append("'"+element+"'");
			++count;
		}
		
		
		controlArchivoDAO.listarEnvioPorRecepcionBatchSubastaDivisas(ciclosVerificar.toString());
		DataSet dataset = controlArchivoDAO.getDataSet();

		if(dataset.count() == 0 ) {
			inversionDAO.listarPorId(idUnidadInversion);
							
			DataSet dataset2 = inversionDAO.getDataSet();
				
			if(dataset2.count() > 0 ) {
				dataset2.next();
				if(status.contains(dataset2.getValue("UNDINV_STATUS"))){
					puedeEjecutar = true;
				}
				//puedeEjecutar = dataset2.getValue("UNDINV_STATUS").equals(status);
			}
		}		
		logger.info("******** PUEDE REALIZAR LA EJECUCION ********  " + puedeEjecutar);
		return puedeEjecutar;
	}

	protected void obtenerArchivoTemporal(){
		//Directorio temp para guardar los archivos
		archivoTemporal = parametrosOPS.get(ParametrosSistema.RUTA_SUBASTA_ADJ_ENVIO);
	}	
}

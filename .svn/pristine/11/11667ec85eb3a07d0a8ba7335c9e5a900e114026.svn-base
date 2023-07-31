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
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

public class LiquidacionEnvioSubastaDivisas extends Liquidacion implements Runnable {

	Logger logger = Logger.getLogger(LiquidacionEnvioSubasta.class);
	private ArrayList<String> statusUnidadInv=new ArrayList<String>();
	private String idOrdenesSeleccionadas;
	private String [] Transaccion ={TransaccionNegocio.TOMA_DE_ORDEN,TransaccionNegocio.LIQUIDACION};

	public LiquidacionEnvioSubastaDivisas(DataSource datasource, int usuarioId,
			String nmUsuario, int idUnidadInversion,String idOrdenes,String procesoEjecutar,String tipoCiclo,ArrayList<String> statusUI) {
		this._dso = datasource;
		this.usuarioId = usuarioId;
		this.nmUsuario = nmUsuario;
		this.idOrdenesSeleccionadas=idOrdenes;
		this.idUnidadInversion = idUnidadInversion;
		statusUnidadInv.addAll(statusUI);	
		this.setProcesoEjecucion(procesoEjecutar);
		this.setTipoCiclo(tipoCiclo);//
	}
	
	public void run() {		
		///
		try {
			
			logger.info("Iniciando el proceso de generación de archivo batch para liquidación tipo subasta Divisas canal Clavenet Personal... ");
			//System.out.println("Iniciando el proceso de generación de archivo batch para liquidación tipo subasta Divisas canal Clavenet Personal... ");

			if (verificarCiclo(statusUnidadInv,TransaccionNegocio.CICLO_BATCH_SUBASTA_DIVISAS,TransaccionNegocio.CICLO_BATCH_SUBASTA,TransaccionNegocio.CICLO_BATCH_SUBASTA_DIVISAS_PERSONAL,TransaccionNegocio.CICLO_BATCH_SICADII_CLAVENET_PERSONAL,TransaccionNegocio.CICLO_BATCH_SICADII_RED_COMERCIAL) &&	
			comenzarProceso(procesoEjecucion,TransaccionNegocio.PROC_BATCH_LIQ_SUBASTA_ENVIO,TransaccionNegocio.PROC_BATCH_LIQ_SUBASTA_DIVISAS_PERSONAL_ENVIO,TransaccionNegocio.PROC_BATCH_COBRO_LIQ_SICAD_II_CLAVENET_PERSONAL_ENVIO,TransaccionNegocio.PROC_BATCH_COBRO_LIQ_SICAD_II_RED_COMERCIAL_ENVIO)) {
  
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
		//logger.info("QUERY METOTO getRegistrosAProcesar ----> " + inversionDAO.detalleDeOperacionBatchPorUnidadLiquidacion(idOrdenesSeleccionadas,String.valueOf(idUnidadInversion)));
		//return inversionDAO.detalleDeOperacionBatchPorUnidadLiquidacion(idOrdenesSeleccionadas,String.valueOf(idUnidadInversion));
		return inversionDAO.previoDeOperacionBatchPorIdOrden(idOrdenesSeleccionadas,String.valueOf(idUnidadInversion),Transaccion,StatusOrden.LIQUIDADA,StatusOrden.REGISTRADA,StatusOrden.NO_ADJUDICADA_INFI,StatusOrden.NO_CRUZADA);

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
		return super.getTipoCiclo();
	}
	
	protected String getNombreArchivo(){
		return ParametrosSistema.NOMBRE_ARCH_SUBASTA_ADJ;
	}
	
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
				//NM25287 SICAD II. 07/04/2014. Los estatus de la unidad de inversion se establecen como parámetro en el constructor
				/*if(dataset2.getValue("TIPO_PRODUCTO_ID").equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL)){
					status.add(UnidadInversionConstantes.UISTATUS_PUBLICADA);
					status.add(UnidadInversionConstantes.UISTATUS_CERRADA);	
					//status.add(UnidadInversionConstantes.UISTATUS_ADJUDICADA);	
					
					logger.info("-------"+ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL);
					logger.info("-------"+dataset2.getValue("UNDINV_STATUS"));
				}*/
				if(status.contains(dataset2.getValue("UNDINV_STATUS"))){
					puedeEjecutar = true;
				}
				//puedeEjecutar = dataset2.getValue("UNDINV_STATUS").equals(status);
			}
		}		
		logger.info("******** PUEDE REALIZAR LA EJECUCION ********  " + puedeEjecutar);
		return puedeEjecutar;
	}
	/**
	 * Verifica si puede ejecutar la generación del archivo. Para ello no debe existir ciclo abierto
	 * y la unidad de inversión debe estar CERRADA
	 */	
	/*protected boolean verificarCiclo(String ciclo, String status) throws Exception {
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
	}*/
	
	protected void obtenerArchivoTemporal(){
		//Directorio temp para guardar los archivos
		archivoTemporal = parametrosOPS.get(ParametrosSistema.RUTA_SUBASTA_ADJ_ENVIO);
	}	
}

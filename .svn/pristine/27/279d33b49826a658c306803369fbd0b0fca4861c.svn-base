package com.bdv.infi.logic.interfaz_ops;

import java.io.File;
import java.util.ArrayList;

import javax.sql.DataSource;

import megasoft.DataSet;

import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

public class AdjudicacionEnvioSubastaDivisas extends AdjudicacionSubastaDivisas implements Runnable{

	private String idOrdenesSeleccionadas;
	private ArrayList<String> statusUnidadInv=new ArrayList<String>();
	private String [] Transaccion ={TransaccionNegocio.TOMA_DE_ORDEN};
	
	public AdjudicacionEnvioSubastaDivisas(DataSource datasource, int usuarioId,
			String nmUsuario,int IdUnidad,String idOrdenes,String procesoEjecutar,String tipoCiclo,ArrayList<String> statusUI) {
		this._dso = datasource;
		this.usuarioId = usuarioId;
		this.nmUsuario = nmUsuario;		
		this.idOrdenesSeleccionadas=idOrdenes;
		this.idUnidadInversion=IdUnidad;
		this.statusUnidadInv.addAll(statusUI);
		//statusUnidadInv.add(UnidadInversionConstantes.UISTATUS_CERRADA);
		//statusUnidadInv.add(UnidadInversionConstantes.UISTATUS_ADJUDICADA);		
		this.setTipoCiclo(tipoCiclo);//
		this.procesoEjecucion=procesoEjecutar;//Variable donde se guarda el proceso que se va a ejecutar (BATCH_SUB_DIVISAS_ENVIO o BATCH_SUB_DIV_PER_ENVIO)
		
	}
	
	public void run() {
		try {
			logger.info("Iniciando el proceso de generación de archivo batch para adjudicación tipo SUBASTA DIVISAS... ");
			//System.out.println("Iniciando el proceso de generación de archivo batch para adjudicación tipo SUBASTA DIVISAS... ");

			if (verificarCiclo(statusUnidadInv,TransaccionNegocio.CICLO_BATCH_SUBASTA_DIVISAS,TransaccionNegocio.CICLO_BATCH_SUBASTA,TransaccionNegocio.CICLO_BATCH_SUBASTA_DIVISAS_PERSONAL,TransaccionNegocio.CICLO_BATCH_SICADII_CLAVENET_PERSONAL,TransaccionNegocio.CICLO_BATCH_SICADII_RED_COMERCIAL) &&
			comenzarProceso(procesoEjecucion,TransaccionNegocio.PROC_BATCH_COBRO_ADJ_SUBASTA_DIVISAS_ENVIO,TransaccionNegocio.PROC_BATCH_ADJ_SUBASTA_ENVIO,TransaccionNegocio.PROC_BATCH_COBRO_ADJ_SUBASTA_DIVISAS_PERSONAL_ENVIO,TransaccionNegocio.PROC_BATCH_COBRO_ADJ_SICAD_II_CLAVENET_PERSONAL_ENVIO,TransaccionNegocio.PROC_BATCH_COBRO_ADJ_SICAD_II_RED_COMERCIAL_ENVIO)) {
				//System.out.println("COMPROBACION PARA EJECUCION DE PROCESO TRUE");
  
				obtenerParametros();

				final File archivo = getArchivoEnvioAdjudicacionSubastaSivisas();

				//Verifica permisos
				tienePermisosEscritura(archivo);
				//tienePermisosEscritura(new File(getArchivoTemporal(getNombreArchivo())));
				verificarArchivoFinal(archivo);
				
				envioArchivo(ConstantesGenerales.OPS_MONEDA_NACIONAL);
				respaldarArchivo(archivo,true);
			}else {
				//System.out.println("COMPROBACION PARA EJECUCION DE PROCESO false");
			}
		} catch (Exception ex) {
			logger.error("Error en el proceso de generación de archivo batch para adjudicación tipo SUBASTA DIVISAS. ", ex);
			//System.out.println("Error en el proceso de generación de archivo batch para adjudicación tipo SUBASTA DIVISAS. " +  ex);
			if(proceso != null) {
				proceso.setDescripcionError(ex.getMessage());
			}
		} finally {			
			terminarProceso();
			logger.info("Terminado el proceso de generación de archivo batch para adjudicación tipo SUBASTA DIVISAS... ");
			//System.out.println("Terminado el proceso de generación de archivo batch para adjudicación tipo SUBASTA DIVISAS... ");
		}
		
	}
	
	protected String getRegistrosAProcesar() throws Exception{
		inversionDAO = new UnidadInversionDAO(this._dso);
		return inversionDAO.previoDeOperacionBatchPorIdOrden(idOrdenesSeleccionadas,String.valueOf(idUnidadInversion),Transaccion,StatusOrden.ADJUDICADA,StatusOrden.NO_ADJUDICADA_INFI,StatusOrden.CRUZADA,StatusOrden.NO_CRUZADA);
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
	
	protected String getCarpetaRespaldo() {
		String carpeta = parametrosOPS.get(ParametrosSistema.RUTA_SUBASTA_ADJ_RESPALDO);
		if(!carpeta.endsWith(File.separator)){
			carpeta = carpeta.concat(File.separator);
		}
		return carpeta;
	}	
	//Metodo Modificado en requerimiento TTS-401 nm26659
	protected String getCiclo(){
		logger.info("getCiclo -----> " + this.getTipoCiclo());
		System.out.println("getCiclo -----> " + this.getTipoCiclo());
		return this.getTipoCiclo();
		//return TransaccionNegocio.CICLO_BATCH_SUBASTA_DIVISAS;
	}
	
	
	protected String getNombreArchivo(){
		return ParametrosSistema.NOMBRE_ARCH_SUBASTA_ADJ;
	}
	
	/**
	 * Verifica si puede ejecutar la generación del archivo. Para ello no debe existir ciclo abierto
	 * y la unidad de inversión debe estar CERRADA
	 */
	protected boolean verificarCiclo(ArrayList<String> status,String... ciclos) throws Exception {
		
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
				
				//Modificacion en requerimiento SICAD 2 _NM26659_15/03/2014		
				//NM25287 SICAD II. 07/04/2014. Los estatus de la unidad de inversion se establecen como parámetro en el constructor
				/*if(dataset2.getValue("tipo_producto_id").equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL)){
					status.add(UnidadInversionConstantes.UISTATUS_PUBLICADA);
				}*/
				
				if(status.contains(dataset2.getValue("UNDINV_STATUS"))){
					puedeEjecutar = true;
				}
				//puedeEjecutar = dataset2.getValue("UNDINV_STATUS").equals(status);
			}
		}
		
		return puedeEjecutar;
	}
	
	protected void obtenerArchivoTemporal(){
		//Directorio temp para guardar los archivos
        archivoTemporal = parametrosOPS.get(ParametrosSistema.RUTA_SUBASTA_ADJ_ENVIO);
	}
}

package com.bdv.infi.logic.interfaz_ops;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import javax.sql.DataSource;

import megasoft.DataSet;
import megasoft.db;

import org.apache.log4j.Logger;

import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;
import com.bdv.infi.util.Utilitario;
import static com.bdv.infi.logic.interfaces.ConstantesGenerales.*;

public class AdjudicacionRecepcionSubasta extends Adjudicacion implements Runnable {
	static final Logger logger = Logger.getLogger(AdjudicacionRecepcionSubasta.class);
	
	//Variables incluidas en desarrollo SICAD nm26659
	private String procesoBatchEnEjecucion;
	private ArrayList<String> statusUnidadInv=new ArrayList<String>();
	private UnidadInversionDAO uiDAO;
	public AdjudicacionRecepcionSubasta(DataSource datasource) {
		super();
		statusUnidadInv.add(UnidadInversionConstantes.UISTATUS_CERRADA);
		statusUnidadInv.add(UnidadInversionConstantes.UISTATUS_ADJUDICADA);
		this._dso = datasource;
		
	}	
	
	public void run() {
		try {			
			logger.info("Iniciando el proceso de recepción de archivo batch para adjudicación tipo subasta... ");
			obtenerParametros();
			final File archivo = getArchivoRecepcionAdjudicacionSubasta();
			if (archivo.exists()){
				if (verificarCiclo(statusUnidadInv,TransaccionNegocio.CICLO_BATCH_SUBASTA,TransaccionNegocio.CICLO_BATCH_SUBASTA_DIVISAS,TransaccionNegocio.CICLO_BATCH_SUBASTA_DIVISAS_PERSONAL,TransaccionNegocio.CICLO_BATCH_SICADII_CLAVENET_PERSONAL,TransaccionNegocio.CICLO_BATCH_SICADII_RED_COMERCIAL) && 
					comenzarProceso(procesoBatchEnEjecucion)) {	
					//Verifica permisos
					tienePermisosLectura(archivo);
					recepcionArchivo(archivo);
					actualizarOrdenes(idUnidadInversion);
					//Modificacion en requerimiento TTS_441 NM26659_18/03/2014 (SICAD_2)
					uiDAO=new UnidadInversionDAO(_dso);
					uiDAO.listarProductoUnidadPorId(idUnidadInversion);
					
					if(uiDAO.getDataSet().count()>0){
						uiDAO.getDataSet().first();
						if(uiDAO.getDataSet().next()){
							String tipoProduct=uiDAO.getDataSet().getValue("TIPO_PRODUCTO_ID");
							if(!tipoProduct.equals(ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL) && !tipoProduct.equals(ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL) && !tipoProduct.equals(ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL)){							
								actualizarUnidadInversion();
							}
						}
						
					}
									
					
				}
			}else{
				logger.info("El archivo de recepcion no existe: " + archivo.getAbsolutePath());
			}
		} catch (Exception ex) {
			logger.error("Error en el proceso de recepción archivo batch para adjudicación tipo subasta. ", ex);		

			if(proceso != null) {
				proceso.setDescripcionError(ex.getMessage());
			}
		} finally {			
			terminarProceso();
			logger.info("Terminado el proceso de recepción de archivo batch para adjudicación tipo subasta... ");
		}
	}
	
	/**
	 * Actualiza la unidad de inversión si todas las ordenes están adjudicadas
	 * @throws Exception en caso de error
	 */
	protected void actualizarUnidadInversion() throws Exception {		
		//Verifica las ordenes asociadas a la unidad de inversión
		if (ordenDAO.existeOrdenesPorAdjudicar(String.valueOf(idUnidadInversion))==0){
			inversionDAO.modificarStatus(idUnidadInversion,UnidadInversionConstantes.UISTATUS_ADJUDICADA);
		}
	}

	/**
	 * Actualiza las ordenes si todas sus operaciones están APLICADAS 
	 * @param unidadInversion unidad de inversión para filtrar las ordenes que se deben actuaalizar
	 * @throws Exception en caso de error
	 */
	protected void actualizarOrdenes(int unidadInversion) throws Exception{
		/*Verifica si la fecha de liquidación se ha alcanzado, de ser cierto todas las ordenes presentarán
		un estatus de adjudicada*/
		String sql = "";
		UnidadInversionDAO uiDao = new UnidadInversionDAO(_dso);
		uiDao.listarPorId(idUnidadInversion);
		DataSet ds = uiDao.getDataSet();
		Date fechaActual = new Date();
		if (ds.next()){
			Date fechaLiq = Utilitario.StringToDate(ds.getValue("undinv_fe_liquidacion"),ConstantesGenerales.FORMATO_FECHA3);
			if (fechaLiq!=null && (fechaActual.compareTo(fechaLiq) == 0 || fechaActual.after(fechaLiq))){
				//Son iguales o diferentes, se compara si la fecha actual es mayor a la de liquidación de la unidad
				sql = this.ordenDAO.actualizarStatus(StatusOrden.ADJUDICADA,idUnidadInversion,TransaccionNegocio.TOMA_DE_ORDEN,StatusOrden.PROCESO_ADJUDICACION);				
			}
		}		

		//Si la fecha no se ha alcanzado sólo se modifican las que cumplan la condición de pago completo
		if (sql.equals("")){
			sql = this.ordenDAO.actualizarOrdenesRecepcionSubastaBatch(unidadInversion);	
		}
		
		db.exec(this._dso, sql);
	}
	
	/**
	 * Obtiene la carpeta de respaldo
	 */
	protected String getCarpetaRespaldo() {
		String carpeta = parametrosOPS.get(ParametrosSistema.RUTA_SUBASTA_ADJ_RESPALDO);
		if(!carpeta.endsWith(File.separator)){
			carpeta = carpeta.concat(File.separator);
		}
		return carpeta;
	}
	
	
	/**
	 * Verifica si puede ejecutar la generación del archivo. Para ello debe existir ciclo abierto
	 */
	//Metodo creado en requerimiento SICAD nm26659
	protected boolean verificarCiclo(ArrayList<String> status,String... ciclo) throws Exception {
		boolean puedeEjecutar = false;
		StringBuffer ciclosVerificar=new StringBuffer();		
		String cicloEnEjecucion=null;
		int count=0;
		if(ciclo.length>0){
			for (String element : ciclo) {
				if(count>0){
					ciclosVerificar.append(",");
				}
				ciclosVerificar.append("'"+element+"'");
				++count;
			}
			
			
		}
		// si estado de la unidad de inversion es adecuado para la liquidacion
		ControlArchivoDAO controlArchivoDAO = new ControlArchivoDAO(_dso);
		UnidadInversionDAO inversionDAO = new UnidadInversionDAO(_dso);

		controlArchivoDAO.listarEnvioPorRecepcionBatchSubastaDivisas(ciclosVerificar.toString());
		DataSet dataset = controlArchivoDAO.getDataSet();

		if (dataset.count() > 0) {
			dataset.first();
			dataset.next();
			
			cicloEnEjecucion=dataset.getValue("status");
			
			if(cicloEnEjecucion.equals(TransaccionNegocio.CICLO_BATCH_SUBASTA)){
				procesoBatchEnEjecucion=TransaccionNegocio.PROC_BATCH_ADJ_SUBASTA_RECEP;
			}else if(cicloEnEjecucion.equals(TransaccionNegocio.CICLO_BATCH_SUBASTA_DIVISAS)){
				procesoBatchEnEjecucion=TransaccionNegocio.PROC_BATCH_COBRO_ADJ_SUBASTA_DIVISAS_RECEP;
			}else if(cicloEnEjecucion.equals(TransaccionNegocio.CICLO_BATCH_SUBASTA_DIVISAS_PERSONAL)){
				//Inclusion de ESTATUS Publicada para la busqueda de operaciones asociadas a la unidad "Generica" de sicad 2 
				status.add(UnidadInversionConstantes.UISTATUS_PUBLICADA);
				procesoBatchEnEjecucion=TransaccionNegocio.PROC_BATCH_COBRO_ADJ_SUBASTA_DIVISAS_PERSONAL_RECEP;
			}else if(cicloEnEjecucion.equals(TransaccionNegocio.CICLO_BATCH_SICADII_CLAVENET_PERSONAL)){
				procesoBatchEnEjecucion=TransaccionNegocio.PROC_BATCH_COBRO_ADJ_SICAD_II_CLAVENET_PERSONAL_RECEP;
			}else if(cicloEnEjecucion.equals(TransaccionNegocio.CICLO_BATCH_SICADII_RED_COMERCIAL)){
				procesoBatchEnEjecucion=TransaccionNegocio.PROC_BATCH_COBRO_ADJ_SICAD_II_RED_COMERCIAL_RECEP;
			}
			
			//Verifica si el valor es de ADJUDICACIÓN
			if (dataset.getValue("in_recepcion").equals(TIPO_ADJUDICACION)){			
				dataset.next();
				idUnidadInversion = Integer.valueOf(dataset.getValue("UNDINV_ID"));
				inversionDAO.listarPorId(idUnidadInversion);
	
				DataSet dataset2 = inversionDAO.getDataSet();
	
				if (dataset2.count() > 0) {
					dataset2.next();
					if(status.contains(dataset2.getValue("UNDINV_STATUS"))){
						puedeEjecutar = true;
					}
					//puedeEjecutar = dataset2.getValue("UNDINV_STATUS").equals(status);
				}
			}else{
				logger.info("No se espera archivo de recepción de adjudicación de SUBASTA");	
			}
		}
		return puedeEjecutar;
	}
	/**
	 * Verifica si puede ejecutar la generación del archivo. Para ello debe existir ciclo abierto
	 */
	/*protected boolean verificarCiclo(String ciclo, String status) throws Exception {
		boolean puedeEjecutar = false;

		// si estado de la unidad de inversion es adecuado para la liquidacion
		ControlArchivoDAO controlArchivoDAO = new ControlArchivoDAO(_dso);
		UnidadInversionDAO inversionDAO = new UnidadInversionDAO(_dso);

		controlArchivoDAO.listarEnvioPorRecepcionBatch(ciclo);
		DataSet dataset = controlArchivoDAO.getDataSet();

		if (dataset.count() > 0) {
			dataset.first();
			dataset.next();
			//Verifica si el valor es de ADJUDICACIÓN
			if (dataset.getValue("in_recepcion").equals(TIPO_ADJUDICACION)){			
				dataset.next();
				idUnidadInversion = Integer.valueOf(dataset.getValue("UNDINV_ID"));
				inversionDAO.listarPorId(idUnidadInversion);
	
				DataSet dataset2 = inversionDAO.getDataSet();
	
				if (dataset2.count() > 0) {
					dataset2.next();
					puedeEjecutar = dataset2.getValue("UNDINV_STATUS").equals(status);
				}
			}else{
				logger.info("No se espera archivo de recepción de adjudicación de SUBASTA");	
			}
		}
		return puedeEjecutar;
	}*/
}

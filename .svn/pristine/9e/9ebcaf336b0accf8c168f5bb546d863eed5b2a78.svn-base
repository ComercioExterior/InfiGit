package com.bdv.infi.logic.interfaz_ops;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import javax.sql.DataSource;

import megasoft.DataSet;
import megasoft.db;

import com.bdv.infi.dao.ArchivoRetencionDAO;
import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.dao.TransaccionFijaDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.dao.VehiculoDAO;
import com.bdv.infi.data.Archivo;
import com.bdv.infi.data.FormatoConciliacionRetencion;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.TransaccionFija;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;
import com.bdv.infi.util.FileUtil;

public class ConciliacionRetencionEnvio extends BatchOps implements Runnable{
	private ArrayList<String> statusUnidadInv=new ArrayList<String>();
	FormatoConciliacionRetencion formatoArchivoRecepcion=new FormatoConciliacionRetencion();
	VehiculoDAO vehiculoDAO;
	ControlArchivoDAO controlArchivoDAO;
	TransaccionFijaDAO transaccionFijaDAO;
	private ArchivoRetencionDAO archivoRetencionDAO;
	String transaccionNegocio ="";
	String cicloEnvio ="";	
	String idEjecucion;
		
	public void run() {
		try {
			logger.info("Iniciando el proceso de generación de archivo batch CONCILIACION DE RETENCIONES ENVIO... ");
			transaccionNegocio = TransaccionNegocio.PROC_BATCH_CONCILIACION_RETENCION_ENVIO;
			cicloEnvio =  TransaccionNegocio.CICLO_BATCH_CONCILIACION_RETENCION;
						
			if (verificarCiclo(statusUnidadInv,TransaccionNegocio.CICLO_BATCH_CONCILIACION_RETENCION,TransaccionNegocio.CICLO_BATCH_SUBASTA_DIVISAS,TransaccionNegocio.CICLO_BATCH_SUBASTA,TransaccionNegocio.CICLO_BATCH_SUBASTA_DIVISAS_PERSONAL,TransaccionNegocio.CICLO_BATCH_SICADII_CLAVENET_PERSONAL,TransaccionNegocio.CICLO_BATCH_SICADII_RED_COMERCIAL) &&
					comenzarProceso(transaccionNegocio,TransaccionNegocio.PROC_BATCH_COBRO_ADJ_SUBASTA_DIVISAS_ENVIO,TransaccionNegocio.PROC_BATCH_ADJ_SUBASTA_ENVIO,TransaccionNegocio.PROC_BATCH_COBRO_ADJ_SUBASTA_DIVISAS_PERSONAL_ENVIO,TransaccionNegocio.PROC_BATCH_COBRO_ADJ_SICAD_II_CLAVENET_PERSONAL_ENVIO,TransaccionNegocio.PROC_BATCH_COBRO_ADJ_SICAD_II_RED_COMERCIAL_ENVIO)) {
				logger.info("EJECUCION DE PROCESO CONCILIACION RETENCION TRUE");
								
				obtenerParametros();

				final File archivo = getArchivoEnvioConciliacionRetencion();

				//Verifica permisos
				tienePermisosEscritura(archivo);
				
				verificarArchivoFinal(archivo);
				
				envioArchivo(ConstantesGenerales.OPS_MONEDA_NACIONAL);
				
				respaldarArchivo(archivo,true);
			}else {				
				logger.info("CONCILIACION RETENCION: No se puede procesar la solicitud porque otra persona realizó esta acción y esta actualmente activa o un ciclo de envío y recepción no ha finalizado. ");

			}
		} catch (Exception ex) {
			logger.error("Error en el proceso de generación de archivo batch CONCILIACION DE RETENCIONES ENVIO... ", ex);
			//System.out.println("Error en el proceso de generación de archivo batch para adjudicación tipo SUBASTA DIVISAS. " +  ex);
			if(proceso != null) {
				proceso.setDescripcionError(ex.getMessage());
			}
		} finally {			
			terminarProceso();
			logger.info("Terminado el proceso de generación de archivo batch CONCILIACION DE RETENCIONES ENVIO... ");
			//System.out.println("Terminado el proceso de generación de archivo batch para adjudicación tipo SUBASTA DIVISASñ... ");
		}
		
	}
	
	protected void envioArchivo(String tipoMoneda) throws Exception {
		logger.info("*** Ejecucion envioArchivo de la OPS BatchOps ****");
		Archivo archivo = new Archivo();
		ArrayList<String> registrosDeArchivoDefinitivo = new ArrayList<String>();
		String vehiculoGeneral="";
		String instFinanciero="";
		String codOperacionBloqueo="";
		String fechaInicioUnidad="";
		String fechaFinUnidad="";
		try {
			//BORRAR TABLA DE RETENCIONES
			archivoRetencionDAO.borrarTablaRetencion();
			
			//OBTENER VEHICULO BDV			
			vehiculoDAO.obtenerVehiculoBDV();			
			if (vehiculoDAO.getDataSet()!=null&& vehiculoDAO.getDataSet().count()>0) {
				vehiculoDAO.getDataSet().next();
				vehiculoGeneral = vehiculoDAO.getDataSet().getValue("vehicu_id");
			}
			
			//OBTENER FECHAS INICIO Y FIN EN LA UNIDAD DE INVERSION
			inversionDAO.obtenerDatosUIporId("to_char(UNDINV_FE_EMISION,'YYYY-MM-dd') UNDINV_FE_EMISION,to_char(UNDINV_FE_CIERRE,'YYYY-MM-dd') UNDINV_FE_CIERRE, INSFIN_ID", String.valueOf(idUnidadInversion));
			if (inversionDAO.getDataSet()!=null&& inversionDAO.getDataSet().count()>0) {
				inversionDAO.getDataSet().next();
				fechaInicioUnidad = inversionDAO.getDataSet().getValue("UNDINV_FE_EMISION");
				fechaFinUnidad = inversionDAO.getDataSet().getValue("UNDINV_FE_CIERRE");
				instFinanciero = inversionDAO.getDataSet().getValue("INSFIN_ID");
			}
			
			//OBTENER CODIGO DE OPERACION
			transaccionFijaDAO.listarCodOperacionTransaccionFija("COD_OPERACION_CTE_BLO",instFinanciero,vehiculoGeneral,TransaccionFija.GENERAL_CAPITAL_SIN_IDB_MANEJO_MIXTO);
			if (transaccionFijaDAO.getDataSet()!=null&& transaccionFijaDAO.getDataSet().count()>0) {
				transaccionFijaDAO.getDataSet().next();				
				codOperacionBloqueo = transaccionFijaDAO.getDataSet().getValue("COD_OPERACION_CTE_BLO");
			}
			
			//VALORES PARA INSERCION EN CONTROL DE ARCHIVOS
			idEjecucion = controlArchivoDAO.obtenerNumeroDeSecuencia();
			archivo.setIdEjecucion(Integer.parseInt(idEjecucion));
			archivo.setNombreArchivo(parametrosOPS.get(getNombreArchivo()));
			archivo.setUnidadInv(idUnidadInversion);
			archivo.setUsuario(nmUsuario);
			archivo.setFechaGeneracion(new Date());
			archivo.setStatus(cicloEnvio);
			archivo.setVehiculoId(vehiculoGeneral);			
			archivo.setInRecepcion(1);
			
			//CREAR ARCHIVO A ENVIAR
			formatoArchivoRecepcion.setFechaInicio(fechaInicioUnidad);
			formatoArchivoRecepcion.setFechaFin(fechaFinUnidad);
			formatoArchivoRecepcion.setCodOperacion(codOperacionBloqueo);
			formatoArchivoRecepcion.setIdCiclo(idEjecucion);
			
			registrosDeArchivoDefinitivo.add(formatoArchivoRecepcion.crearCabecera());
			
			obtenerArchivoTemporal();
			logger.info("Escribiendo en archivo temporal " + archivoTemporal);
			FileUtil.put(archivoTemporal, registrosDeArchivoDefinitivo, true);
			
			transferirArchivo(archivoTemporal, getDestinoFinal());
			logger.info("Actualizando base de datos... ");
			
			db.execBatch(this._dso, controlArchivoDAO.insertarArchivoRecepcion(archivo));
			
		} catch (Exception ex) {
			try {
				logger.error("Error en el proceso ", ex);
				FileUtil.delete(archivoTemporal);
			} catch (Exception e) {
				logger.error("No fue posible borrar el archivo " + archivoTemporal);
			}
			throw ex;
		}
	}
	
	protected boolean verificarCiclo(ArrayList<String> status,String... ciclos) throws Exception {
		
		StringBuffer ciclosVerificar=new StringBuffer();
		boolean puedeEjecutar = false;
		
		// si estado de la unidad de inversion es adecuado para la liquidacion			
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
			
			inversionDAO.obtenerDatosUIporId("UNDINV_STATUS",String.valueOf(idUnidadInversion));

			DataSet dataset2 = inversionDAO.getDataSet();
			
			if(dataset2.count() > 0 ) {
				dataset2.next();
								
				if(status.contains(dataset2.getValue("UNDINV_STATUS"))){
					puedeEjecutar = true;
				}else{
					logger.debug("La unidad de inversión se encuentra en un estatus no permitido para el proceso");
				}
			}
		}		
		return puedeEjecutar;
	}

	
	public ConciliacionRetencionEnvio(DataSource datasource, int usuarioId,String nmUsuario, int idUnidadInversion) throws Exception {
		super();
		this._dso = datasource;
		this.usuarioId = usuarioId;
		this.nmUsuario = nmUsuario;
		this.idUnidadInversion = idUnidadInversion;
		
		//statusUnidadInv.add(UnidadInversionConstantes.UISTATUS_PUBLICADA); //TODO QUITAR ESTE ESTATUS
		statusUnidadInv.add(UnidadInversionConstantes.UISTATUS_CERRADA);
		statusUnidadInv.add(UnidadInversionConstantes.UISTATUS_ADJUDICADA);
		statusUnidadInv.add(UnidadInversionConstantes.UISTATUS_LIQUIDADA);

		this._dso = datasource;
		inversionDAO= new UnidadInversionDAO(_dso);
		vehiculoDAO = new VehiculoDAO(_dso);
		controlArchivoDAO = new ControlArchivoDAO(_dso);	
		transaccionFijaDAO = new TransaccionFijaDAO(_dso);
		archivoRetencionDAO = new ArchivoRetencionDAO(_dso);
	}
	
	protected File getArchivoEnvioConciliacionRetencion() {
		return getArchivo(ParametrosSistema.RUTA_CONC_RETENCION_ENVIO, ParametrosSistema.NOMBRE_ARCH_CONC_RET);
	}

	@Override
	protected boolean verificarCiclo(String ciclo, String status) throws Exception {
		return false;
	}
	
	protected void obtenerArchivoTemporal(){
		//Directorio temp para guardar los archivos
        archivoTemporal = parametrosOPS.get(ParametrosSistema.RUTA_CONC_RETENCION_ENVIO);
	}
	protected String getDestinoFinal(){	
		return parametrosOPS.get(getNombreArchivo());
	}
	/**
	 * Obtiene la carpeta de respaldo
	 */
	protected String getCarpetaRespaldo() {
		String carpeta = parametrosOPS.get(ParametrosSistema.RUTA_CONC_RETENCION_RESP);
		if(!carpeta.endsWith(File.separator)){
			carpeta = carpeta.concat(File.separator);
		}
		return carpeta;
	}
	protected String getNombreArchivo(){
		return ParametrosSistema.NOMBRE_ARCH_CONC_RET;
	}
}
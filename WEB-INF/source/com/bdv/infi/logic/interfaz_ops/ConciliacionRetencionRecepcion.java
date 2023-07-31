package com.bdv.infi.logic.interfaz_ops;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

import javax.sql.DataSource;

import megasoft.DataSet;
import megasoft.Logger;

import com.bdv.infi.dao.ArchivoRetencionDAO;
import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.dao.OperacionDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.data.FormatoConciliacionRetencion;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;


public class ConciliacionRetencionRecepcion extends BatchOps implements Runnable{
	private ArrayList<String> statusUnidadInv=new ArrayList<String>();
	private ArchivoRetencionDAO archivoRetencionDAO;
	private OperacionDAO operacionDAO;
	protected String procesoEjecucion;
	protected String tipoCiclo;
	protected String tipoProducto;
	ArrayList<String> querysEjecutar=new ArrayList<String>();	
	protected int idCiclo=0;
	FormatoConciliacionRetencion formatoArchivoRecepcion=new FormatoConciliacionRetencion();
	private int cantidadTotalOrdenes=0;
	private int cantidadTotalOrdenesProcesadas=0;
	private int cantidadLoteOrdenesProcesadas=0;	
	
	public void run() {
		try {			
			logger.info("Iniciando el proceso de recepción de archivo batch para CONCILIACION DE RETENCIONES...//////*******/////// ");
			obtenerParametros();
			final File archivo = getArchivoRecepcionConciliacionRecepcion();
			logger.info("archivo.getAbsolutePath(): "+archivo.getAbsolutePath());
			if (archivo.exists()){
				if (verificarCiclo(statusUnidadInv,"'"+TransaccionNegocio.CICLO_BATCH_CONCILIACION_RETENCION+"'") && 
					comenzarProceso(TransaccionNegocio.PROC_BATCH_CONCILIACION_RETENCION_RECEP)) {					
					recepcionArchivoRetencion(archivo);					
				}
			}else{
				logger.info("El archivo de recepcion CONCILIACION DE RETENCIONES no existe: " + archivo.getAbsolutePath());
			}
		} catch (Exception ex) {
			logger.error("Error en el proceso de recepción archivo batch para CONCILIACION DE RETENCIONES. ", ex);		

			if(proceso != null) {
				proceso.setDescripcionError(ex.getMessage());
			}
		} finally {			
			terminarProceso();
			logger.info("Terminado el proceso de recepción de archivo batch para CONCILIACION DE RETENCIONES... ");
		}
	}
	
	protected File getArchivoRecepcionConciliacionRecepcion() {
		return getArchivo(ParametrosSistema.RUTA_CONC_RETENCION_RECEP, ParametrosSistema.NOMBRE_ARCH_CONC_RET);
	}
	
	protected boolean verificarCiclo(ArrayList<String> status,String ciclo) throws Exception {
		boolean puedeEjecutar = false;
		
		// si estado de la unidad de inversion es adecuado 
		ControlArchivoDAO controlArchivoDAO = new ControlArchivoDAO(_dso);
		UnidadInversionDAO inversionDAO = new UnidadInversionDAO(_dso);
	
		controlArchivoDAO.listarEnvioPorRecepcionBatch(ciclo);	
		DataSet dataset = controlArchivoDAO.getDataSet();
		logger.info("ConciliacionRetencionRecepcion.verificarCiclo(). listarEnvioPorRecepcionBatchSubastaDivisas: "+dataset);
		 
		if(dataset.next()) {	
			idUnidadInversion=Integer.parseInt(dataset.getValue("UNDINV_ID"));
			logger.info("ConciliacionRetencionRecepcion.verificarCiclo(). idUnidadInversion: " + idUnidadInversion);
			inversionDAO.listarPorId(idUnidadInversion);
			
			DataSet dataset2 = inversionDAO.getDataSet();
			if (dataset2.count() > 0) {
				dataset2.next();				
				if (status.contains(dataset2.getValue("UNDINV_STATUS"))) {
					puedeEjecutar = true;
				}
			}					
		}		
		logger.info("******** PUEDE REALIZAR LA EJECUCION ********  " + puedeEjecutar);
		return puedeEjecutar;
	}
	
	protected void recepcionArchivoRetencion(File archivo) throws FileNotFoundException, IOException, Exception {
				
		if (!archivo.exists()) {
			logger.info("El archivo de recepcion no existe: " + archivo.getAbsolutePath());
			throw new Exception("El archivo de recepcion no existe: " + archivo.getAbsolutePath());
		} else {
						
			procesarArchivo(archivo);

			respaldarArchivo(archivo);
			
			actualizarRetencionOperaciones();

			cerrarCiclo();
		}
	}
	
	protected void procesarArchivo(File archivo) throws Exception {
		//PROCESAR CABECERA
		ArchivoRecepcion archivoRecepcion = new ArchivoRecepcion(archivo);
		String linea = archivoRecepcion.leerLinea();		
		formatoArchivoRecepcion.cargarCabecera(linea);
		
		//VALIDAR CICLO
		procesarCabecera(formatoArchivoRecepcion);
		
		if (formatoArchivoRecepcion.esValidoFormatoRecepcion()) {
			linea = archivoRecepcion.leerLinea();
			while (linea != null) {
				if (linea.length() > 0) {
					System.out.println("linea: " + linea);
					formatoArchivoRecepcion.cargarCuerpo(linea);

					//PROCESAR DETALLE
					procesarDetalle(formatoArchivoRecepcion);
				}
				linea = archivoRecepcion.leerLinea();
				
				//INSERTAR REGISTROS EN TABLA INFI_TB_908_ARCHIVO_RETENCION
				procesarQuerysPorLote(querysEjecutar);
			}
		}else{
			logger.info("El archivo de recepcion indica el siguiente error: "+formatoArchivoRecepcion.getRespuestaArchivo());
		}
		
		//INSERTAR REGISTROS EN TABLA INFI_TB_908_ARCHIVO_RETENCION
		archivoRetencionDAO.ejecutarStatementsBatch(querysEjecutar);

	}
		
	protected void procesarCabecera(FormatoConciliacionRetencion formatoRecepcion) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("FECHA INICIO: "+formatoRecepcion.getFechaInicio());
			logger.debug("FECHA FIN: "+formatoRecepcion.getFechaFin());
			logger.debug("CODIGO DE OPERACION: "+formatoRecepcion.getCodOperacion());
			logger.debug("NUMERO PROCESO (CICLO): "+formatoRecepcion.getIdCiclo());
		}
		
		idCiclo = Integer.valueOf(formatoRecepcion.getIdCiclo());
		
		//VERIFICA SI EL CICLO ESTA ABIERTO, DE NO SER ASÍ NO SE PROCESA EL ARCHIVO
		controlArchivoDAO.listarCiclosAbiertos(idCiclo);
		if (controlArchivoDAO.getDataSet().count() == 0) {
			throw new Exception("El ciclo correspondiente al número " + idCiclo + " no se encuentra abierto, por lo tanto no es posible continuar con el proceso");
		}

		logger.info("Procesando ciclo: " + idCiclo);

	}
	
	protected void procesarDetalle(FormatoConciliacionRetencion registroRetencion) throws Exception {		
		//CARGAR REGISTRO DE RETENCION
		querysEjecutar.add(archivoRetencionDAO.insertarRetencion(registroRetencion));
	}
	
	protected void actualizarRetencionOperaciones() throws Exception {
		DataSet operaciones;
		querysEjecutar=new ArrayList<String>();
		//OBTENER OPERACIONES 
		archivoRetencionDAO.obtenerOperacionesSinRetencion(formatoArchivoRecepcion.getFechaInicio(),formatoArchivoRecepcion.getFechaFin(),0,this.tipoProducto,false);
		operaciones=archivoRetencionDAO.getDataSet();
		
		if (operaciones!=null && operaciones.count()>0) 
		{
			while(operaciones.next()){				
				//CONSULTAR REGISTRO DE RETENCION ENVIADO POR ALTAIR QUE HAGA MATCH CON LA OPERACION (INFI_TB_908_ARCHIVO_RETENCION)
				archivoRetencionDAO.obtenerRetencionOperacion(operaciones.getValue("CTECTA_NUMERO"), operaciones.getValue("CODIGO_OPERACION"), operaciones.getValue("MONTO_OPERACION"),false);
				
				if (archivoRetencionDAO.getDataSet() != null && archivoRetencionDAO.getDataSet().count()==0) {
					//SI NO EXISTEN COINCIDENCIAS EXACTAS SE INTENTA CONSULTANDO EL MONTO TRUNCADO
					archivoRetencionDAO.obtenerRetencionOperacion(operaciones.getValue("CTECTA_NUMERO"), operaciones.getValue("CODIGO_OPERACION"), operaciones.getValue("MONTO_OPERACION"),true);
				}
				
				if (archivoRetencionDAO.getDataSet() != null && archivoRetencionDAO.getDataSet().count()>0) {
					if (archivoRetencionDAO.getDataSet().count() == 1) {
						//ACTUALIZAR CÓDIGOS DE RETENCION DE OPERACIONES
						archivoRetencionDAO.getDataSet().next();
						querysEjecutar.add(archivoRetencionDAO.actualizarRetencionOperacion(operaciones.getValue("ORDENE_ID"), archivoRetencionDAO.getDataSet().getValue("NUMERO_RETENCION")));
						if(!archivoRetencionDAO.getDataSet().getValue("ESTADO_RETENCION").equalsIgnoreCase("1")){
							querysEjecutar.add(archivoRetencionDAO.aplicarOperacionDesbloqueo(operaciones.getValue("ORDENE_ID")));
						}

					} else {
						//ESCRIBIR MENSAJE EN LOG 
						logger.info("Existe mas de una coincidencia en CUENTA: " + operaciones.getValue("CTECTA_NUMERO") + ", MONTO: " + operaciones.getValue("MONTO_OPERACION") + ",CODIGO_OPERACION: " + operaciones.getValue("CODIGO_OPERACION") + " en el archivo de bloqueo");

					}
				}	else {					
					//ESCRIBIR MENSAJE EN LOG 
					logger.info(" No existen coincidencias con CUENTA: " + operaciones.getValue("CTECTA_NUMERO") + ", MONTO: " + operaciones.getValue("MONTO_OPERACION") + ",CODIGO_OPERACION: " + operaciones.getValue("CODIGO_OPERACION") + " en el archivo de bloqueo");
				
					//COLOCAR APLICADAS LAS OPERACIONES DE BLOQUEO Y DESBLOQUEO A LAS ORDENES NO CRUZADAS
					if (operaciones.getValue("ORDSTA_ID").equalsIgnoreCase(StatusOrden.NO_CRUZADA)){
						querysEjecutar.add(operacionDAO.actualizarEstatusOperacionesBloqueo(operaciones.getValue("ORDENE_ID"), ConstantesGenerales.STATUS_APLICADA));
					}					
				}
				
				//ACTUALIZAR CÓDIGOS DE RETENCION DE OPERACIONES
				procesarQuerysPorLote(querysEjecutar);
				//querysEjecutar.clear(); NM25287 14/08/2014 -- LINEA COMENTADA POR INCIDENCIA EN CALIDAD
			}
		}
		
		//ACTUALIZAR CÓDIGOS DE RETENCION DE OPERACIONES
		archivoRetencionDAO.ejecutarStatementsBatch(querysEjecutar);
		//archivoRetencionDAO.borrarTablaRetencion(); NM25287 14/08/2014 -- LINEA COMENTADA POR INCIDENCIA EN CALIDAD
	}

	public ConciliacionRetencionRecepcion(DataSource datasource) {
		super();
		//statusUnidadInv.add(UnidadInversionConstantes.UISTATUS_PUBLICADA); //TODO QUITAR ESTE ESTATUS
		statusUnidadInv.add(UnidadInversionConstantes.UISTATUS_CERRADA);
		statusUnidadInv.add(UnidadInversionConstantes.UISTATUS_ADJUDICADA);
		statusUnidadInv.add(UnidadInversionConstantes.UISTATUS_LIQUIDADA);
		this._dso = datasource;
		archivoRetencionDAO = new ArchivoRetencionDAO(_dso);	
		operacionDAO = new OperacionDAO(_dso);
	}

	@Override
	protected boolean verificarCiclo(String ciclo, String status) throws Exception {
		// TODO Auto-generated method stub
		return false;
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
	protected void cerrarCiclo() throws ParseException, Exception {	
		logger.info("Cerrando ciclo: " + idCiclo);

		controlArchivoDAO.cerrarCiclo(idCiclo);
	}
	/**
	 * Método que maneja la ejeción por lotes de los querys del proceso de liquidación
	 * @author nm25287
	 * @throws SQLException
	 */
	protected void procesarQuerysPorLote(ArrayList<String> sentencias) throws SQLException{
		//EJECUCIÓN DE QUERYS
		++cantidadTotalOrdenes;
		++cantidadLoteOrdenesProcesadas;
		if (ConstantesGenerales.COMMIT_REGISTROS_LIQ == cantidadLoteOrdenesProcesadas) {
			cantidadTotalOrdenesProcesadas = cantidadTotalOrdenesProcesadas + cantidadLoteOrdenesProcesadas;
			ordenDAO.ejecutarStatementsBatchBool(sentencias);
			sentencias.clear();			
			Logger.info(this, "Ordenes enviadas por COMMIT en proceso de CONCILIACION DE RETENCIONES : " + cantidadLoteOrdenesProcesadas);
			cantidadLoteOrdenesProcesadas = 0;
		}
		//Logger.info(this, "Realizacion de commit al numero de registro N° " + cantidadTotalOrdenesProcesadas);
	}

}
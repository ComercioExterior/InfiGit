package com.bdv.infi.logic.transform;

import com.bdv.infi.dao.MensajeOpicsDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.ftp.FTPUtil;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaz_opics.message.MensajeOpics;
import com.bdv.infi.logic.interfaz_opics.message.MensajeOpicsDetalle;
import com.bdv.infi.logic.interfaz_opics.message.MensajeOpicsOperacionCambio;
import com.bdv.infi.logic.interfaz_opics.message.MensajeOpicsRentaFija;
import com.bdv.infi.util.FileUtil;
import com.bdv.infi.util.Utilitario;
import megasoft.db;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import javax.sql.DataSource;

/**
 * Clase que busca en bases de datos los deal que tenga estatus no enviado,para crear el archivo TXT y luego actualiza el estatus a ENVIADO
 * 
 * @author elaucho
 */
public class GeneracionArchivoOpics {

	/*** Variable de gestion de logs */
	private Logger logger = Logger.getLogger(GeneracionArchivoOpics.class);

	/*** HashMap que contiene los parametros a utilizar en el proceso de Generación de datos hacia opics */
	@SuppressWarnings("unused")
	private HashMap<String, String> parametros = new HashMap<String, String>();

	/*** DataSource */
	private DataSource _dso;

	/*** Usuario */
	private int usuarioId;

	private ProcesosDAO procesosDAO = null;
	private Proceso proceso = null;

	private String fechaDesde,fechaHasta;
	
	private String tipoMensaje;
	/*** Vehiculos asociados a los deal */
	private String[] vehiculos;

	/*** Constructor* @param DataSource _dso */
	public GeneracionArchivoOpics(DataSource _dso, String[] vehiculos) {
		this._dso = _dso;
		this.vehiculos = vehiculos;
	}

	public GeneracionArchivoOpics(DataSource _dso, String[] vehiculos,String fechaDesde,String fechaHasta,String tipoMensaje) {
		this._dso = _dso;
		this.vehiculos = vehiculos;
		this.fechaDesde=fechaDesde;
		this.fechaHasta=fechaHasta;
		this.tipoMensaje=tipoMensaje;
	}
	/**
	 * Genera los archivos de Renta Fija y Operacion de Cambio Envia los archivos a sus respectivas colas v&iacute;a FTP
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	public void generarArchivoOpics() throws Exception {
		procesosDAO = new ProcesosDAO(this._dso);

		if (!existeUnProcesoActivo()) {

			MensajeOpicsDAO mensajeOpicsDAO = new MensajeOpicsDAO(this._dso);
			ParametrosDAO parametrosDAO = new ParametrosDAO(this._dso);

			ArrayList<String> sqlsOperacionCambio = new ArrayList<String>();
			ArrayList<String> sqlsOperacionRentaFija = new ArrayList<String>();
			Utilitario utilitario = new Utilitario();

			try {
				// Se buscan los parametros en base de datos
				parametros = parametrosDAO.buscarParametros(ConstantesGenerales.INTERFACE_OPICS);

				// Directorio temp para guardar los archivos
				String directorioTemporal = parametros.get(ParametrosSistema.TEMP_DIRECTORY);

				// ArrayList que contendra los registros a enviar via FTP
				ArrayList<String> registrosOpicsRentaFija = new ArrayList<String>();
				ArrayList<String> registrosOpicsOperacionCambio = new ArrayList<String>();

				// Se listan los mensajes por enviar y se arma el String
				MensajeOpicsDetalle mensajeOpicsDetalle = null;
				MensajeOpics mensajeOpics = null;
				OrdenDAO ordenDAO = new OrdenDAO(_dso);

				// **************************************************************************************************
				for (int i = 0; i < this.vehiculos.length; i++) {
					//Cambio realizado en requerimiento TTS_443 NM26659_23/05/2014
					//if (mensajeOpicsDAO.listarMensajesPorEnviar(Long.parseLong(this.vehiculos[i]))) {
					if (mensajeOpicsDAO.listarMensajesPorEnviar(this.tipoMensaje,fechaDesde,fechaHasta)) {
						logger.info("Listando mensajes por enviar del vehiculo [" + vehiculos[i] + "]");
						while ((mensajeOpics = (MensajeOpics) mensajeOpicsDAO.moveNext()) != null) {
							//Si existen registros se inicia el proceso
							iniciarProceso();
							boolean operacionCambio = false;
							ArrayList<MensajeOpicsDetalle> mensajeOpicsDetalleArrayList = mensajeOpics.getMensajesDetalles();
							Iterator<MensajeOpicsDetalle> iterator = mensajeOpicsDetalleArrayList.iterator();

							while (iterator.hasNext()) {
								mensajeOpicsDetalle = (MensajeOpicsDetalle) iterator.next();

								if (mensajeOpicsDetalle instanceof MensajeOpicsOperacionCambio) {
									//System.out.println("MENSAJE DE OPERACION DE CAMBIO");
									registrosOpicsOperacionCambio.add(mensajeOpicsDetalle.generarRegistro());
									logger.info("Generando registro para operación de cambio");
									operacionCambio = true;

								} else if (mensajeOpicsDetalle instanceof MensajeOpicsRentaFija) {
									//System.out.println("MENSAJE DE RENTA FIJA");
									logger.info("Generando registro para Renta Fija");
									registrosOpicsRentaFija.add(mensajeOpicsDetalle.generarRegistro());
								}
							} // fin while iterator

							// guardamos lo sqls en su arrayList especifico para luego ser ejecutados
							//System.out.println("ES OPERACION DE CAMBIO ? " + operacionCambio);
							if (operacionCambio) {
								sqlsOperacionCambio.add(mensajeOpicsDAO.marcarMensajeEnviado(mensajeOpics.getIdOpics()));
								sqlsOperacionCambio.add(ordenDAO.actualizarEjecucionId(proceso.getEjecucionId(), mensajeOpics.getIdOpics()));

							} else {

								sqlsOperacionRentaFija.add(mensajeOpicsDAO.marcarMensajeEnviado(mensajeOpics.getIdOpics()));
								sqlsOperacionRentaFija.add(ordenDAO.actualizarEjecucionId(proceso.getEjecucionId(), mensajeOpics.getIdOpics()));
							}
						} // fin while

						// Cerramos las conexiones
						mensajeOpicsDAO.closeResources();
						mensajeOpicsDAO.cerrarConexion();

						// Se envian los archivos generados vía FTP
						String archivoOperacionCambio = "";
						String archivoOperacionRentaFija = "";

						// Se envian ambos archivos vía FTP
						FTPUtil ftpUtil = new FTPUtil(parametros.get(ParametrosSistema.DIRECCION_SERVIDOR_FTP), this._dso);

						// ------------------------------------------ARCHIVO OPERACION CAMBIO-----------------------------------------------
						if (!registrosOpicsOperacionCambio.isEmpty()) {
							archivoOperacionCambio = directorioTemporal + parametros.get(ParametrosSistema.OPICS_ARCHIVO_NOMBRE);
							registrosOpicsOperacionCambio.add("\0");
							
							//05-06-2014 NM26659 Modificado en requerimiento TTS-443 SICAD 2  
							//Modificacion de metodo para que los archivos anviados al servidor Windows tengan caracter de salta de line \r 
							FileUtil.put(archivoOperacionCambio, registrosOpicsOperacionCambio, true,ConstantesGenerales.INTERFACE_OPICS);
							logger.info("Generado archivo temporal para operaciones de cambio Opics en Directorio Temporal" + archivoOperacionCambio);
						}

						// Envio archivo Operacion Cambio
						if (!registrosOpicsOperacionCambio.isEmpty()) {
							try {
								ftpUtil.putFTP(archivoOperacionCambio, ((parametros.get(ParametrosSistema.OPICS_ARCHIVO_RUTA).equals("/") || parametros.get(ParametrosSistema.OPICS_ARCHIVO_RUTA).equals("\\")) ? "" : parametros.get(ParametrosSistema.OPICS_ARCHIVO_RUTA)).concat(parametros.get(ParametrosSistema.OPICS_ARCHIVO_NOMBRE)), parametros.get(ParametrosSistema.OPICS_ARCHIVO_RUTA), true);
								db.execBatch(this._dso, (String[]) sqlsOperacionCambio.toArray(new String[sqlsOperacionCambio.size()]));
								logger.info("El archivo temporal para operaciones de Operaciones de Cambio Opics fue enviado v&iacute;a FTP ruta: " + archivoOperacionRentaFija);

							} catch (Exception e) {
								proceso.setDescripcionError(e.getMessage());
								logger.error(utilitario.stackTraceException(e));
							}
						}

						// Eliminamos los archivos temporales
						if (!registrosOpicsOperacionCambio.isEmpty()) {
							FTPUtil.delete(archivoOperacionCambio);
							logger.info("Se elimina archivo operacion cambio temporal " + archivoOperacionCambio);
						}

						// ------------------------------------------ARCHIVO OPERACION RENTA FIJA-------------------------------------------

						// Se crea Temporal
						if (!registrosOpicsRentaFija.isEmpty()) {
							archivoOperacionRentaFija = directorioTemporal + vehiculos[i] + parametros.get(ParametrosSistema.NOMBRE_ARCHIVO_RENTA_FIJA);
							registrosOpicsRentaFija.add("\0");
							
							//05-06-2014 NM26659 Modificado en requerimiento TTS-443 SICAD 2 
							//Modificacion de metodo para que los archivos anviados al servidor Windows tengan caracter de salta de line \r
							FileUtil.put(archivoOperacionRentaFija, registrosOpicsRentaFija, true,ConstantesGenerales.INTERFACE_OPICS);
							logger.info("Generado archivo temporal para operaciones de Renta Fija Opics en " + archivoOperacionRentaFija);
						}

						// Envio archivo Operacion Renta Fija
						if (!registrosOpicsRentaFija.isEmpty()) {
							try {
								ftpUtil.putFTP(archivoOperacionRentaFija, ((parametros.get(ParametrosSistema.OPICS_ARCHIVO_RUTA).equals("/") || parametros.get(ParametrosSistema.OPICS_ARCHIVO_RUTA).equals("\\")) ? "" : parametros.get(ParametrosSistema.OPICS_ARCHIVO_RUTA)).concat(vehiculos[i] + parametros.get(ParametrosSistema.NOMBRE_ARCHIVO_RENTA_FIJA)), parametros
										.get(ParametrosSistema.OPICS_ARCHIVO_RUTA), true);
								db.execBatch(this._dso, (String[]) sqlsOperacionRentaFija.toArray(new String[sqlsOperacionRentaFija.size()]));
								logger.info("El archivo temporal para operaciones de Renta Fija Opics fue enviado v&iacute;a FTP ruta: " + archivoOperacionRentaFija);

							} catch (Exception e) {
								proceso.setDescripcionError(e.getMessage());
								logger.error(e.getMessage() + utilitario.stackTraceException(e));
							}
						}

						if (!registrosOpicsRentaFija.isEmpty()) {

							FTPUtil.delete(archivoOperacionRentaFija);
							logger.info("Se elimina archivo Operacion RentaFija Temporal " + archivoOperacionRentaFija);
						}

						// ----------------------------------------------------------------------------------------------------------------
					} // fin if existem mensajes por enviar
					else {

						logger.info("No existen registros a ser enviados");

						// throw new Exception("No existen registros a ser enviados");
					}
				} // FIN FOR
			} catch (Exception e) {
				proceso.setDescripcionError(e.getMessage());
				logger.error(e.getMessage() + utilitario.stackTraceException(e));
				throw e;
			} finally {
				mensajeOpicsDAO.closeResources();
				mensajeOpicsDAO.cerrarConexion();
				logger.info("El envio de datos OPICS ha culminado...");
				try {
					finalizarProceso();
				} catch (Exception e) {
					logger.error(e.getMessage() + utilitario.stackTraceException(e));
					throw e;
				}
			} // fin finally
		}
	} // fin generarArchivoOpics

	/**
	 * Inicia el proceso de envío de mensajes OPICS sólo si existen mensajes por enviar
	 * 
	 * @throws Exception
	 *             en caso de error
	 */
	private void iniciarProceso() throws Exception {
		if (proceso == null) {
			logger.info("Se inicia el proceso de Generación Datos Opics...");
			proceso = new Proceso();
			Date fechaInicio = new Date();
			proceso.setFechaInicio(fechaInicio);
			proceso.setFechaValor(fechaInicio);
			proceso.setTransaId(ConstantesGenerales.INTERFACE_OPICS_ENVIO);
			proceso.setUsuarioId(usuarioId);
			String sql = procesosDAO.insertar(proceso);
			db.exec(this._dso, sql);
		}
	}

	/**
	 * Finaliza el proceso de envío de mensajes OPICS
	 * 
	 * @throws Exception
	 *             en caso de error
	 */
	private void finalizarProceso() throws Exception {
		if (proceso != null) {
			Date fechaFin = new Date();
			proceso.setFechaFin(fechaFin);
			db.exec(_dso, procesosDAO.modificar(proceso));
		}
	}

	/**
	 * Verifica si existe un proceso activo.
	 * 
	 * @return verdadero en caso de estar ejecutandose un proceso
	 * @throws Exception
	 *             en caso de error
	 */
	private boolean existeUnProcesoActivo() throws Exception {
		procesosDAO.listarPorTransaccionActiva(ConstantesGenerales.INTERFACE_OPICS_ENVIO);
		return procesosDAO.getDataSet().count() > 0 ? true:false;
	}
} // fin clase

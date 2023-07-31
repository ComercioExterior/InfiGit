package com.bdv.infi.logic.cruces_ordenes;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import megasoft.DataSet;
import megasoft.Logger;
import megasoft.db;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.bdv.infi.dao.AuditoriaDAO;
import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.DocumentoDefinicionDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.OrdenesCrucesDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.SolicitudesSitmeDAO;
import com.bdv.infi.dao.TitulosDAO;
import com.bdv.infi.data.Auditoria;
import com.bdv.infi.data.Orden;
import com.bdv.infi.data.OrdenesCruce;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.util.Utilitario;
import com.bdv.infi_toma_orden.data.OrdenTitulo;

/**
 * Clase encargada de realizar la carga de los cruces/no_cruces de las ordenes mediante un archivo excel El formato usado para la carga ordenes es: Unidad Inversion Código orden Rif/cedula Mto Solicitado $ Mto Adjudicado $ Tasa cambio Asignada Fecha Valor
 * 
 * @author nm29643
 * 
 */
public class BeanLogicCargaCruces {
	private String idUiFiltered;
	private String nameUiFiltered;
	private String contenidoDocumento;
	private String nombreDocumento;
	private String identificadorPlantilla;

	private OrdenesCruce cruce;
	private Orden orden = new Orden();
	private String ip;
	private String nombreUsuario;

	// TTS-491 NM26659_03/03/2015 - Validacion Web Service BCV
	private String parametroValidacionBCV;
	// TTS-491 NM26659_03/03/2015 - Validacion Web Service BCV
	private boolean validacionBcvEnLinea = false;
	//TTS-491 NM26659_20/03/2015 - Validacion Unidad de Menudeo
	private boolean validacionUnidadMenudeo = false;

	// DAOS
	private OrdenDAO ordenDAO;
	private ClienteDAO clienteDAO;
	private ProcesosDAO procesoDAO;
	private OrdenesCrucesDAO ordenesCrucesDAO;
	private SolicitudesSitmeDAO solicitudesSitmeDAO;
	private DataSource dataSource;
	// NM26659_19/09/2014 TTS_466 validacion de Documentos asociados a la Unidad de Inversion
	private DocumentoDefinicionDAO documentoDefinicionDAO;

	private Proceso proceso;

	private DataSet _resgitrosCruces;
	String descripcionError = "";
	String errorProceso = "";
	String mensajeError = "";
	private int contadorInvalid = 0;	
	private int contadorValid = 0;
	
	
	//private int contadorAdvertencia = 0;

	Hashtable<String, BigDecimal> ordenesCruces = new Hashtable<String, BigDecimal>();
	Hashtable<String, String> nrosOperacion = new Hashtable<String, String>();
	HashMap<String, String> nrosCotizacion = new HashMap<String, String>();
	Hashtable<String, String> fValorMap = new Hashtable<String, String>();
	private Hashtable<String, String> ordenesToUpdate = new Hashtable<String, String>();	
	Hashtable<String, String> idsTitulosOrdenes = new Hashtable<String, String>();
	// NM26659_19/09/2014 TTS_466 validacion de Documentos asociados a la Unidad de Inversion
	ArrayList<String> documentosTipPersona = new ArrayList<String>();

	// ******** Bloque de atributos pertenecientes archivo excel de carga *******/
	HSSFCell uiCell = null;// columna Unidad de Inversion
	HSSFCell cedulaCell = null;// columna cedula
	HSSFCell codOrdenInfiCell = null;// columna codigo de orden INFI
	HSSFCell codOrdenBcvCell = null;// columna codigo de orden BCV
	HSSFCell nroCotizacionCell = null;// columna nro cotizacion
	HSSFCell nroOperacionCell = null;// columna nro operacion
	HSSFCell mtoSolicitadoCell = null;// columna monto solicitado
	HSSFCell tasaCell = null;// columna que indica la tasa de cambio propuesta
	HSSFCell contraparteCell = null;// columna contraparte
	HSSFCell mtoCruceCell = null;// columna monto cruce
	HSSFCell isinCell = null;// columna codigo isin
	HSSFCell precioTitCell = null;// columna precio titulo
	// HSSFCell valorEfectivoTitCell = null;//columna valor efectivo titulo
	HSSFCell fechaValorCell = null;// columna fecha valor
	// ******* Bloque de atributos pertenecientes archivo excel de carga *******/

	// Modificaciones para calculo de Valor Nominal segun nuevas reglas NM26659 _24/04/2014
	private com.bdv.infi_toma_orden.dao.TitulosDAO titulosOrdenDAO;
	private DateFormat formatter;
	private Date dateCupon, dateValor;

	private com.bdv.infi_toma_orden.data.OrdenTitulo beanTitulo;

	/**
	 * Método encargado de la carga de los archivos de ordenes a registrar en la tabla INFI_TB_226_ORDENES_APROBACION
	 * 
	 * @return boolean indicando si toda la data a cargar es valida
	 * @throws IOException
	 */
	public boolean cargarCruces() throws Exception {

		ordenDAO = new OrdenDAO(dataSource);
		clienteDAO = new ClienteDAO(dataSource);
		procesoDAO = new ProcesosDAO(dataSource);
		ordenesCrucesDAO = new OrdenesCrucesDAO(dataSource);
		solicitudesSitmeDAO = new SolicitudesSitmeDAO(dataSource);
		titulosOrdenDAO = new com.bdv.infi_toma_orden.dao.TitulosDAO(dataSource.toString(), dataSource);// dataSource);
		// Modificaciones para calculo de Valor Nominal segun nuevas reglas NM26659 _24/04/2014
		formatter = new SimpleDateFormat("dd-MM-yy");
		beanTitulo = new OrdenTitulo();
		boolean validData = true;
		boolean procesoCreado = false;

		try {

			FileInputStream documento = new FileInputStream(contenidoDocumento);
			HSSFWorkbook libro = new HSSFWorkbook(documento);
			HSSFSheet hoja = null;// hoja
			HSSFRow row = null; // fila
			HSSFCell celdaControl = null;// primera celda o columna
			/*
			 * HSSFRow rowNext = null; //fila siguiente HSSFCell celdaControlNext = null;//celda o columna siguiente int tipoNext;
			 */
			int tipo = -1;

			hoja = libro.getSheet(libro.getSheetName(0));// objeto hoja
			HSSFCell identificadorArchivo = null;// 1RA columna de Identificacion del tipo de Archivo

			ArrayList<String> querysEjecutar = new ArrayList<String>();

			// SE INICIA EL PROCESO DE CARGA DE CRUCES
			procesoCreado = comenzarProceso();

			if (procesoCreado) { // PROCESO UNICIADO

				for (int fila = 0; fila < hoja.getPhysicalNumberOfRows(); fila++) { // RECORRIDO POR FILAS

					Logger.debug(this, "Fila: " + fila);
					descripcionError = "";

					try {
						row = hoja.getRow(fila);// Fila actual
						Logger.debug(this, "Obtiene row " + fila);
						celdaControl = row.getCell((short) 0); // Primera celda de la fila actual
						Logger.debug(this, "Obtiene celda control de fila " + fila);
						if (celdaControl == null)
							Logger.debug(this, "Celda Control es NULL");
						if (celdaControl != null) {
							tipo = celdaControl.getCellType(); // Tipo de la primera celda de la fila actual
							Logger.debug(this, "Obtiene el tipo de celda control de fila " + fila + ": " + tipo);
						}
						if (celdaControl == null || tipo == HSSFCell.CELL_TYPE_BLANK) {// Se rompe la lectura de filas al encontrar la primera celda vacia (fin de registros)
							Logger.debug(this, "Celda control VACIA");
							if (fila >= 0 && fila <= 2) {
								fila = hoja.getPhysicalNumberOfRows();
								proceso.setDescripcionError("Archivo sin registros para procesar o mal formado");
								setMensajeError(getMensajeError() + "Archivo sin registros para procesar o mal formado, verifique e intente nuevamente<br/>");
								validData = false;
								Logger.info(this, "Archivo sin registros para procesar o mal formado");
								fila = hoja.getPhysicalNumberOfRows();
								break;
							}
							if (fila > 2) {
								Logger.info(this, "Fin de lectura de registros exitoso");
								fila = hoja.getPhysicalNumberOfRows();
								break;
							}
						} else { // Si la celda no es NULL
							// SE VALIDA FORMATO DE LA PLANTILLA DE CRUCE / NO CRUCE
							if (fila == 0) {
								identificadorArchivo = row.getCell((short) 0);
								Logger.debug(this, "identificadorArchivo: " + identificadorArchivo.toString() + ", identificadorPlantilla: " + identificadorPlantilla);
								if (!identificadorArchivo.toString().contains(identificadorPlantilla)) {
									proceso.setDescripcionError("Se ha ingresado un archivo  cuyo encabezado que no corresponde al proceso " + proceso.getTransaId());
									setMensajeError(getMensajeError() + "Se ha ingresado un archivo cuyo encabezado que no corresponde al proceso de cruce/no cruce de &oacute;rdenes a realizar, por favor verifique la plantilla utilizada<br/>");
									validData = false;
									Logger.info(this, "Se ha ingresado un archivo que no corresponde al proceso " + proceso.getTransaId());
									fila = hoja.getPhysicalNumberOfRows();
									break;
								}
							} else if (fila >= 2) {
								Logger.debug(this, "Fila mayor o igual a 2");

								// Se crea el objeto de tipo OrdenesCruce
								cruce = new OrdenesCruce();

								cruce.setIdEjecucion(proceso.getEjecucionId());

								// OBTENER CAMPOS DE LA PLANTILLA
								uiCell = row.getCell((short) 0);
								cruce.setNameUI(uiCell.toString());

								cedulaCell = row.getCell((short) 1);
								cruce.setCiRif(cedulaCell.toString());

								codOrdenInfiCell = row.getCell((short) 2);
								if (codOrdenInfiCell != null && !codOrdenInfiCell.toString().equals("")) {
									cruce.setIdOrdenInfiString(codOrdenInfiCell.toString().replaceAll("\\.0", ""));
								}

								nroCotizacionCell = row.getCell((short) 3);								
									
									
										if(nroCotizacionCell!=null && nroCotizacionCell.toString().length()>0) {
											cruce.setNroCotizacionString(cientificaToString(nroCotizacionCell).replaceAll("\\.0", ""));
										} else {
											cruce.setNroCotizacionString("");
										}											
									
//										else {										
//										//cruce.setNroCotizacionString(nroCotizacionCell.toString());
//										cruce.setNroCotizacionString("");
//									}

								

								Logger.debug(this, "COD ORDEN INFI CELL------" + codOrdenInfiCell.toString());
								Logger.debug(this, "CEDULA CELL------" + cedulaCell.toString());
								Logger.debug(this, "CRUCE COD ORDEN INFI------" + cruce.getIdOrdenInfiString());
								Logger.debug(this, "CRUCE CEDULA------" + cruce.getCiRif());

								// Si se trata de la plantilla de CRUCE
								if (identificadorPlantilla.equals(ConstantesGenerales.TITULO_TEMPLATE_CRUCE)) {
									// Se setean los valores correspondientes a la planilla de CRUCE
									nroOperacionCell = row.getCell((short) 4);
									Logger.debug(this, "NRO OPERACION CELL (TEXTO)------" + nroOperacionCell.toString());
									if (nroOperacionCell != null && !nroOperacionCell.toString().equals("")) {
										// cruce.setNroOperacionString(cientificaToString(nroOperacionCell).replaceAll("\\.0", ""));
										cruce.setNroOperacionString(nroOperacionCell.toString());
									} else {
										cruce.setNroOperacionString("");
									}
									// System.out.println("CRUCE NRO OPERACION------"+cruce.getNroOperacionString());
									Logger.debug(this, "CRUCE NRO OPERACION------" + cruce.getNroOperacionString());

									mtoSolicitadoCell = row.getCell((short) 5);
									cruce.setMontoSolicitadoString(mtoSolicitadoCell.toString());

									tasaCell = row.getCell((short) 6);
									cruce.setTasaString(tasaCell.toString());

									contraparteCell = row.getCell((short) 7);
									cruce.setContraparte(contraparteCell.toString());

									mtoCruceCell = row.getCell((short) 8);
									cruce.setMontoOperacionString(mtoCruceCell.toString());

									fechaValorCell = row.getCell((short) 9);
									cruce.setFechaValor(fechaValorCell.toString());

									isinCell = row.getCell((short) 10);

									// Si se trata de un cruce de titulo
									if (isinCell != null && !isinCell.toString().equals("")) {
										cruce.setIndicadorTitulo(ConstantesGenerales.VERDADERO);
										cruce.setIsinString(isinCell.toString());

										precioTitCell = row.getCell((short) 11);
										cruce.setPrecioTituloString(precioTitCell.toString());
									} else {
										cruce.setIndicadorTitulo(ConstantesGenerales.FALSO);
									}
								} else {
									if (identificadorPlantilla.equals(ConstantesGenerales.TITULO_TEMPLATE_NO_CRUCE)) {
										// Se setean los valores correspondientes a la plantilla NO CRUCE
										mtoSolicitadoCell = row.getCell((short) 4);
										cruce.setMontoSolicitadoString(mtoSolicitadoCell.toString());

										tasaCell = row.getCell((short) 5);
										cruce.setTasaString(tasaCell.toString());

										mtoCruceCell = row.getCell((short) 6);
										cruce.setMontoOperacionString(mtoCruceCell.toString());

										// Se setean como tipo divisas los NO CRUCES
										cruce.setIndicadorTitulo(ConstantesGenerales.FALSO);
									}
								}

								try {
									
									// SE VALIDAN LOS DATOS DE ORDEN A CANCELAR
									if (validarCamposCruce(fila + 1)) { // Todos los campos son validos

										Logger.info(this, "CAMPOS VALIDOS! Fila: " + (fila + 1));

										// Se incrementa el contador de cruces validos
										setContadorValid(getContadorValid() + 1);

										if (identificadorPlantilla.equals(ConstantesGenerales.TITULO_TEMPLATE_CRUCE)) {
											cruce.setEstatus(ConstantesGenerales.STATUS_CRUZADA);
										} else {
											if (identificadorPlantilla.equals(ConstantesGenerales.TITULO_TEMPLATE_NO_CRUCE)) {
												cruce.setEstatus(ConstantesGenerales.STATUS_NO_CRUZADA);
											}
										}

										// SE INSERTA ORDEN INFI A ACTUALIZAR STATUS A PROCESO_CRUCE
										if (!ordenesToUpdate.containsKey(cruce.getIdOrdenInfiString())) {
											ordenesToUpdate.put(cruce.getIdOrdenInfiString(), "");
											Logger.debug(this, "GET HASHTABLE ordenesToUpdate " + cruce.getIdOrdenInfiString() + ": " + ordenesToUpdate.get(cruce.getIdOrdenInfiString()));
										}

										// Si es un cruce se preserva el nro de operacion (unico por cruce)
										if (identificadorPlantilla.equals(ConstantesGenerales.TITULO_TEMPLATE_CRUCE)) {
											nrosOperacion.put(cruce.getNroOperacionString(), "");
											nrosCotizacion.put(cruce.getIdOrdenInfiString(), cruce.getNroCotizacionString());
										}
										// System.out.println("CONTENIDO DE HASH-TABLE ---> " + ordenesCruces);
										// System.out.println("ORDEN INFI ---> " + cruce.getIdOrdenInfiString());
										// Si no existe el idOrden como clave en el hashtable se agrega idOrden valido
										if (!ordenesCruces.containsKey(cruce.getIdOrdenInfiString())) {
											// SE PRESERVA EL MONTO CRUZADO PARA LA ORDEN
											if (identificadorPlantilla.equals(ConstantesGenerales.TITULO_TEMPLATE_CRUCE)) {
												ordenesCruces.put(cruce.getIdOrdenInfiString(), cruce.getMontoOperacion());
											} else {
												// MONTO EN CERO INDICA NO CRUCE PARA LA ORDEN
												if (identificadorPlantilla.equals(ConstantesGenerales.TITULO_TEMPLATE_NO_CRUCE)) {
													ordenesCruces.put(cruce.getIdOrdenInfiString(), new BigDecimal(0));
												}
											}
											Logger.debug(this, "GET HASHTABLE ordenesCruces " + cruce.getIdOrdenInfiString() + ": " + ordenesCruces.get(cruce.getIdOrdenInfiString()));
										} else { // Ya existe un cruce para la orden en el lote de cruces a cargar
											// Se suma al total cruzado en este lote el monto del cruce actual
											ordenesCruces.put(cruce.getIdOrdenInfiString(), ordenesCruces.get(cruce.getIdOrdenInfiString()).add(cruce.getMontoOperacion()));
											Logger.debug(this, "GET HASHTABLE ordenesCruces " + cruce.getIdOrdenInfiString() + ": " + ordenesCruces.get(cruce.getIdOrdenInfiString()));
										}

										// SE INSERTA CLAVE ID_TITULO+ID_ORDEN INDICANDO EL PRECIO DEL TITULO
										if (cruce.getPrecioTitulo() != null) { // Precio de titulo seteado
											if (!idsTitulosOrdenes.containsKey(cruce.getIdTitulo() + "|" + cruce.getIdOrdenInfiString())) {
												idsTitulosOrdenes.put(cruce.getIdTitulo() + "|" + cruce.getIdOrdenInfiString(), cruce.getPrecioTitulo() + "|" + cruce.getFechaValor());
												Logger.debug(this, "GET HASHTABLE idsTitulosOrdenes: " + idsTitulosOrdenes.get(cruce.getIdTitulo() + "|" + cruce.getIdOrdenInfiString()));

											}
										}// Precio de titulo seteado
										cruce.setObservacion(descripcionError);										
										
										//Si parametro validacion en linea es FALSE y la orden es verificada correctamente se actualiza el campo ORDENE_ESTATUS_BCV de tabla INFI_TB_204_ORDENES
										//NM26659 - 23/03/2015 Vefiricacion que la orden no fue aprobada o rechazada por BCV
										if(!validacionBcvEnLinea && !orden.getStatusVerificacionBCV().equals(ConstantesGenerales.VERIFICADA_APROBADA) && !orden.getStatusVerificacionBCV().equals(ConstantesGenerales.VERIFICADA_RECHAZADA)){//VERIFICACION MANUAL 
												if(cruce.getEstatus().equalsIgnoreCase(ConstantesGenerales.STATUS_CRUZADA)){
													orden.setStatusVerificacionBCV(ConstantesGenerales.VERIFCADA_MANUAL_APROBADA);													
												}else if(cruce.getEstatus().equalsIgnoreCase(ConstantesGenerales.STATUS_NO_CRUZADA)){
													orden.setStatusVerificacionBCV(ConstantesGenerales.VERIFIDA_MANUAL_RECHAZADA);
												}
												
												querysEjecutar.add(ordenDAO.actualizarEstatusOrdenBcvIn(String.valueOf(orden.getIdOrden()),orden.getStatusVerificacionBCV()));
										}
										
									} else {
										Logger.debug(this, "Fila con campos invalidos!");
										validData = false;
										// Se incrementa el contador de cruces invalidos
										contadorInvalid++;
										cruce.setEstatus(ConstantesGenerales.STATUS_INVALIDA);
										cruce.setObservacion(descripcionError);
									}

									errorProceso += descripcionError;

									// PRESERVA LOS QUERYS PARA INSERCION DE CRUCES EN INFI_TB_229_ORDENES_CRUCES
									querysEjecutar.add(ordenesCrucesDAO.insertarCruce(cruce,false));
									
									// EJECUCIÓN DE QUERYS DE REGISTRO DE ORDENES EN LA TABLA TEMPORAL INFI_TB_227_TMP_ORD_APROBACION
									// if((fila>=2 && fila%11==0)||(fila==hoja.getPhysicalNumberOfRows()-1)){ //Cada 250 cancelaciones de inserta en la tabla temporal o si es la ultima fila con registros
									// if((fila>=2 && fila%11==0)||(tipoNext==HSSFCell.CELL_TYPE_BLANK)){ //Cada 250 cancelaciones de inserta en la tabla temporal o si es la ultima fila con registros
									if ((fila >= 2 && fila % (ConstantesGenerales.COMMIT_REGISTROS_CARGA_CRUCES + 2) == 0)) { // Cada 250 cancelaciones de inserta en la tabla temporal (si son menos de 250 o restan se insertan al final del ciclo)
										if (ordenDAO.ejecutarStatementsBatchBool(querysEjecutar)) {
											querysEjecutar.clear();
											querysEjecutar = new ArrayList<String>();
											Logger.debug(this, "Ejecuto inserts en 229!");
										} else {
											validData = false;
											fila = hoja.getPhysicalNumberOfRows(); // para que no vuelva a entrar al ciclo
											querysEjecutar.clear();
											querysEjecutar = new ArrayList<String>();
											proceso.setDescripcionError("Error al insertar en la tabla en el proceso de carga de cruces" + proceso.getTransaId());
											Logger.error(this, "Error al insertar en la tabla en el proceso de carga de cruces " + proceso.getTransaId());
											setMensajeError("Error al insertar en la tabla en el proceso de carga de cruces<br/>");
										}
									}

								} catch (Exception e) {
									proceso.setDescripcionError("Ocurrio una excepcion al validar los campos de la Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " e insertarla durante el proceso de carga de cruces " + proceso.getTransaId());
									setMensajeError("Ocurrio una excepcion al validar los campos de la Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " e insertarla en la tabla temporal durante el proceso de carga de cruces " + proceso.getTransaId());
									Logger.error(this, "Ocurrio una excepcion al validar los campos de la Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " e insertarla en la tabla temporal durante el proceso de carga de cruces " + proceso.getTransaId() + ": " + e.getMessage());
									setMensajeError("Ocurrio una excepcion al validar los campos de la Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " e insertarla en la tabla temporal durante el proceso de carga de cruces<br/>");
									validData = false;
									fila = hoja.getPhysicalNumberOfRows(); // para que no vuelva a entrar al ciclo
								}
							}
						}

						tipo = -1; // Se limpia la variable

					} catch (Exception e) {
						proceso.setDescripcionError("Ha ocurrido una excepcion en el proceso de carga de cruces " + proceso.getTransaId());
						setMensajeError("Ha ocurrido una excepcion en el proceso de carga de cruces<br/>");
						Logger.error(this, "Ha ocurrido una excepcion en el proceso de carga de cruces " + proceso.getTransaId() + ": " + e.getMessage());
						validData = false;
						fila = hoja.getPhysicalNumberOfRows(); // para que no vuelva a entrar al ciclo
						break;
					}

				}// FILAS

			} else { // PROCESO NO UNICIADO
				Logger.error(this, "No se pudo iniciar el proceso de carga de cruces puesto que ya existe un proceso en ejecución");
				proceso.setDescripcionError("No se pudo iniciar el proceso de carga de cruces " + proceso.getTransaId() + " puesto que ya existe un proceso en ejecución");
				setMensajeError("Disculpe, no se pudo iniciar el proceso de carga de cruces puesto que ya existe un proceso en ejecuci&oacute;n. Intente nuevamente.<br/>");
				validData = false;
			}
			/*
			 * for (String string : querysEjecutar) { //System.out.println("SENTENCIAS ----> " + string); }
			 */
			// INSERCION EN TABLA INFI_TB_229_ORDENES_CRUCES DE LAS RESTANTES (Dentro del ciclo se insertan cada 250)
			if (querysEjecutar.size() > 0) {
				if (ordenDAO.ejecutarStatementsBatchBool(querysEjecutar)) {
					querysEjecutar.clear();
					Logger.debug(this, "Ejecuto inserts en 229 (fuera ciclo)!");
				} else {
					proceso.setDescripcionError("Error al insertar en la tabla en el proceso de carga de cruces " + proceso.getTransaId());
					Logger.error(this, "Error al insertar en la tabla en el proceso de carga de cruces " + proceso.getTransaId());
					setMensajeError("Error al insertar en base de datos durante el proceso de carga de cruces<br/>");
					validData = false;
				}
			}

			if (contadorInvalid > 0) {
				if (contadorInvalid == 1)
					setMensajeError(getMensajeError() + "<br/>Se encontr&oacute; " + contadorInvalid + " registro inv&aacute;lido<br/>");
				else
					setMensajeError(getMensajeError() + "<br/>Se encontraron " + contadorInvalid + " registros inv&aacute;lidos<br/>");									
			}
			
			/*if(contadorCruceAdvertencias>0){	
				//setContadorCruceAdvertencia(contadorCruceAdvertencias);				
			}*/			
		} catch (Exception e) {
			Logger.error(this, "Ha ocurrido un error en el proceso de carga de cruces " + proceso.getTransaId() + ": " + e.getMessage());
			proceso.setDescripcionError("Error inesperado en el proceso de cruces " + proceso.getTransaId());
			setMensajeError("Error inesperado en el proceso de carga de cruces<br/>");
			ArrayList<String> query = new ArrayList<String>();
			query.add(procesoDAO.modificar(proceso));
			if (!procesoDAO.ejecutarStatementsBatchBool(query)) { // Si no se cerro el ciclo
				Logger.error(this, "No se pudo cerrar el proceso de carga de ordenes de tipo " + proceso.getTransaId());
				proceso.setDescripcionError(proceso.getDescripcionError() == null ? "" : proceso.getDescripcionError() + "No se pudo cerrar el proceso de carga de ordenes de tipo " + proceso.getTransaId());
			}
			validData = false;

		} finally {

			if (procesoCreado) { // Si se creo el proceso
				// TERMINAR PROCESO
				terminarProceso();
			}

			// REGISTRAR AUDITORIA
			registrarAuditoria();
		}

		return validData;

	}

	// NM29643: Retorna boolean para indicar si los campos fueron validos
	/**
	 * Realiza las validaciones de los campos de la plantilla de cruce/no cruce de ordenes a registrar en la tabla de cruces
	 * 
	 * @throws ParseException
	 * @throws Exception
	 */
	public boolean validarCamposCruce(int nroFila) throws ParseException, Exception {

		boolean validCampos = true, titulo = false;
		Pattern patRifCi = Pattern.compile("^[JjVvEeGgIi][0-9]{1,10}$");
		Pattern patAlfaNum = Pattern.compile("^[a-zA-ZáéíóúüÁÉÍÓÚÜ0-9 _\\-,.]+$");
		Pattern patNum = Pattern.compile("^[0-9]*$");
		Pattern patFecha = Pattern.compile("^((0[1-9])|([12][0-9])|(3[01]))[/\\-]((0[1-9])|(1[0-2]))[/\\-]([1-9][0-9][0-9][0-9])$");
		Matcher m; // Matcher que verifica que se cumpla con el patron
		int compare;
		BigDecimal sumCruces = new BigDecimal(0); // Sumatoria de los cruces de la orden
		descripcionError = "";
		String precioFecha = "";
		DataSet crucesMismaOrdenTitulo = new DataSet();
		;
		try {
	
			// VALIDACION DE CAMPO CODIGO ORDEN INFI (FORMA)
			if (codOrdenInfiCell.toString() != null && codOrdenInfiCell.toString().length() > 10) { // SE VALIDA QUE NO EXCEDA EL TAM MAX DEL CAMPO EN BD
				// Se trunca el valor a 10 caracteres
				cruce.setIdOrdenInfiString(codOrdenInfiCell.toString().substring(0, 10));
				validCampos = false;
				descripcionError += "Fila " + nroFila + " - ";
				if (codOrdenInfiCell != null)
					descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
				descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN + "': Formato invalido: No debe exceder de 10 digitos | ";
				Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN + "': Formato invalido: No debe exceder de 10 digitos");
			}
			// VALIDACION DE CAMPO CODIGO ORDEN INFI (FORMA)
			try {
				cruce.setIdOrdenINFI(Long.parseLong(codOrdenInfiCell.toString().replaceAll("\\.0", "")));
				Logger.debug(this, "Se setea el ID orden: " + cruce.getIdOrdenINFI());
			} catch (NumberFormatException ex) {
				validCampos = false;
				// if(codOrdenInfiCell==null || codOrdenInfiCell.toString().equals(""))cruce.setIdOrdenINFI((long)(-1)); //Se setea un valor para indicar que la orden era invalida
				cruce.setIdOrdenINFI((long) (-1)); // Se setea un valor para indicar que la orden era invalida
				descripcionError += "Fila " + nroFila + " - ";
				if (codOrdenInfiCell != null)
					descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
				descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN + "': El valor ingresado no es de tipo numerico (" + codOrdenInfiCell.toString() + ") | ";
				Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN + "': El valor ingresado no es de tipo numerico (" + codOrdenInfiCell.toString() + ")");
				// throw new Exception("Orden "+codOrdenCell.toString().replaceAll("\\.0", "")+" - CAMPO INVALIDO: "+ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN);
			}
			if (cruce.getIdOrdenINFI() > 0) { // Se seteo el ID Orden traido desde el archivo			
				Logger.debug(this, "ID ORDEN INFI LONG SETEADO");
				// VERIFICA CRUCES DEL MISMO LOTE DE CARGA PARA DICHA ORDEN
				// Si no existe el idOrden como clave en el hashtable se agrega idOrden valido
				if (ordenesCruces.containsKey(cruce.getIdOrdenInfiString())) {
					// Ya hay un cruce/no cruce asociado a la orden en el lote de cruces
					compare = ordenesCruces.get(cruce.getIdOrdenInfiString()).compareTo(new BigDecimal(0));
					if (compare == 0) { // Ya existe un No Cruce
						validCampos = false;
						descripcionError += "Fila " + nroFila + " - ";
						if (codOrdenInfiCell != null)
							descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
						descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN + "': No puede realizar un cruce/no cruce a la orden puesto que ya existe un registro de no cruce por cargar asociado a la misma | ";
						Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN + "': No puede realizar un cruce/no cruce a la orden puesto que ya existe un registro de no cruce por cargar asociado a la misma");
					} else {
						if (compare > 0) { // Ya existe un Cruce
							if (identificadorPlantilla.equals(ConstantesGenerales.TITULO_TEMPLATE_NO_CRUCE)) { // NO CRUCE
								validCampos = false;
								descripcionError += "Fila " + nroFila + " - ";
								if (codOrdenInfiCell != null)
									descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
								descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN + "': No puede realizar un no cruce a la orden puesto que ya existe al menos un registro de cruce por cargar asociado a la misma | ";
								Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN + "': No puede realizar un no cruce a la orden puesto que ya existe al menos un registro de cruce por cargar asociado a la misma");
							} else {
								if (identificadorPlantilla.equals(ConstantesGenerales.TITULO_TEMPLATE_CRUCE)) { // CRUCE
									sumCruces = ordenesCruces.get(cruce.getIdOrdenInfiString());
								}
							}
						}
					}
				}// Ya existen cruces/no cruces en el lote de carga para la orden
				// VERIFICA LOS REGISTROS EN LA TABLA DE CRUCES PARA DICHA ORDEN
				// FLAG BUSQUEDA 1
				// ordenesCrucesDAO.listarCrucesPorIdOrdenInfi(cruce.getIdOrdenINFI(), null, StatusOrden.NO_CRUZADA);
				// NM26659 Modificacion de codigo para resumir cantidad de busquedas a la base de datos.
				ordenesCrucesDAO.listarCrucesPorIdOrdenInfi(cruce.getIdOrdenINFI(), null, StatusOrden.NO_CRUZADA, StatusOrden.CRUZADA);
				_resgitrosCruces = ordenesCrucesDAO.getDataSet();
				if (_resgitrosCruces.count() > 0) {
					_resgitrosCruces.first();
					_resgitrosCruces.next();
					if (_resgitrosCruces.getValue("ESTATUS").equals(StatusOrden.NO_CRUZADA)) {
						validCampos = false;
						descripcionError += "Fila " + nroFila + " - ";
						if (codOrdenInfiCell != null)
							descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
						descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN + "': No puede realizar un cruce/no cruce a la orden puesto que ya existe un registro de no cruce asociado a la misma | ";
						Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN + "': No puede realizar un cruce/no cruce a la orden puesto que ya existe un registro de no cruce asociado a la misma");
					} else if (_resgitrosCruces.getValue("ESTATUS").equals(StatusOrden.CRUZADA)) { // No existe registro no cruzado para la orden
						// NM26659 Modificacion de codigo para resumir cantidad de busquedas a la base de datos.
						// Se verifica si existen cruces asociados a la orden
						// ordenesCrucesDAO.listarCrucesPorIdOrdenInfi(cruce.getIdOrdenINFI(), null, StatusOrden.CRUZADA);
						// if(ordenesCrucesDAO.getDataSet().count()>0){ //Existen cruces anteriores para la Orden
						if (identificadorPlantilla.equals(ConstantesGenerales.TITULO_TEMPLATE_NO_CRUCE)) { // NO CRUCE
							validCampos = false;
							descripcionError += "Fila " + nroFila + " - ";
							if (codOrdenInfiCell != null)
								descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
							descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN + "': No puede realizar un no cruce a la orden puesto que ya existe al menos un registro de cruce asociado a la misma | ";
							Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN + "': No puede realizar un no cruce a la orden puesto que ya existe al menos un registro de cruce asociado a la misma");
						} else { // ES UN CRUCE
							// ordenesCrucesDAO.getDataSet().first();
							_resgitrosCruces.first();
							// for(int i=0; i<ordenesCrucesDAO.getDataSet().count(); i++){
							for (int i = 0; i < _resgitrosCruces.count(); i++) {
								// ordenesCrucesDAO.getDataSet().next();
								_resgitrosCruces.next();
								// sumCruces = sumCruces.add(new BigDecimal(ordenesCrucesDAO.getDataSet().getValue("MONTO_OPERACION")));
								sumCruces = sumCruces.add(new BigDecimal(_resgitrosCruces.getValue("MONTO_OPERACION")));
							}
							sumCruces = sumCruces.setScale(2, BigDecimal.ROUND_HALF_UP);
							Logger.info(this, "sumCruces-------------" + sumCruces);
						}
					}
				}
				// SE TRAE LA INFORMACION DE LA ORDEN
				orden = ordenDAO.listarOrden(cruce.getIdOrdenINFI(), false, false, false, true, false);
				
				//VALIDACION SI LA UNIDAD DE INVERSION ES DE TIPO MENUDEO
				if(isValidacionUnidadMenudeo()) {				
					//VALIDACION SI BCV EN LINEA LA ORDEN DEBE ESTAR VERIFICADA
					if(validacionBcvEnLinea){																			
						if(orden.getStatusVerificacionBCV().equalsIgnoreCase(ConstantesGenerales.SIN_VERIFICAR)){						
							validCampos = false;
							descripcionError += "Fila " + nroFila + " - ";
							if (codOrdenInfiCell != null)
								descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
							descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN + "': Paramatro Validacion BCV en Linea se encuentra activo y la orden no ha sido verificada por el BCV ";
							Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN + "': : Paramatro Validacion BCV en Linea se encutra activo y la orden no ha sido verificada por el BCV ");							
					}																			
				}		
			}
				if (orden.getIdOrden() != 0) { // La orden existe
					Logger.debug(this, "Datos de la orden: " + orden);
					orden.setIdEjecucion(proceso.getEjecucionId());
					// SE RESTRINGEN LOS CRUCES A ORDENES DE LA UNIDAD DE INVERSION SELECCIONADA
					if (orden.getIdUnidadInversion() != Integer.parseInt(idUiFiltered)) { // Si no coincide con la UI seleccionada
						validCampos = false;
						descripcionError += "Fila " + nroFila + " - ";
						if (codOrdenInfiCell != null)
							descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
						descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN + "': La orden debe estar asociada a la Unidad de Inversion seleccionada (" + nameUiFiltered + ") | ";
						Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + ": La orden debe estar asociada a la Unidad de Inversion seleccionada (" + nameUiFiltered + ")");
					} else {
						cruce.setIdUI(Long.parseLong(idUiFiltered));
					}
					// SE RESTRINGEN LOS CRUCES SOLO para los tipos de producto SICAD2PER y SICAD2RED segun corresponda
					if (orden.getTipoProducto() != null && !orden.getTipoProducto().equals("")) {
						if (proceso.getTransaId().equals(TransaccionNegocio.CRUCE_SICAD2_CLAVE_CARGA)) {
							if (!orden.getTipoProducto().equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL)) {
								validCampos = false;
								descripcionError += "Fila " + nroFila + " - ";
								if (codOrdenInfiCell != null)
									descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
								descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN + "': La orden debe estar asociada al tipo de producto 'SICAD II CLAVENET PERSONAL' | ";
								Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + ": La orden debe estar asociada al tipo de producto 'SICAD II CLAVENET PERSONAL'");
							}
							// VALIDA QUE LA ORDEN INFI ESTE ASOCIADA A UNA SOLICITUD CLAVENET
							// NM26659 08/09/2014 Modificacion de consulta (Reemplazo de consulta extensa por busqueda si existe el registro)
							// ArrayList<SolicitudClavenet> arrSolic = solicitudesSitmeDAO.getSolicitudes(0, orden.getIdOrden(), null, null, null, null, null, null, null, false, false);
							// if(arrSolic.size()<=0){
							if (!solicitudesSitmeDAO.exiteOrden(orden.getIdOrden())) {
								validCampos = false;
								descripcionError += "Fila " + nroFila + " - ";
								if (codOrdenInfiCell != null)
									descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
								descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN + "': La orden INFI debe estar asociada a alguna solicitud Clavenet | ";
								Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + ": La orden INFI debe estar asociada a alguna solicitud Clavenet");
							}
						} else {
							
							if (proceso.getTransaId().equals(TransaccionNegocio.CRUCE_SICAD2_RED_CARGA) && !orden.getTipoProducto().equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL)) {
								validCampos = false;
								descripcionError += "Fila " + nroFila + " - ";
								if (codOrdenInfiCell != null)
									descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
								descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN + "': La orden debe estar asociada al tipo de producto 'SICAD II RED COMERCIAL' | ";
								Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + ": La orden debe estar asociada al tipo de producto 'SICAD II RED COMERCIAL'");
							}
						}
					}
				} else { // NO SE TRAJO INFO DE LA ORDEN
					validCampos = false;
					descripcionError += "Fila " + nroFila + " - ";
					if (codOrdenInfiCell != null)
						descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
					descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN + "': La orden no aparece en la busqueda. Puede que dicha orden no se haya enviado a BCV o ya haya sido procesada | ";
					Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN + "': La orden no aparece en la búsqueda. Puede que dicha orden no se haya enviado a BCV o que ya haya sido procesada");
				}

			}// Se seteo el ID Orden traido desde el archivo
			else
				Logger.debug(this, "ID ORDEN INFI LONG NO SETEADO");
			// VALIDACION UNIDAD DE INVERSION (CONTRA UI SELECCIONADA)
			if (uiCell.toString() != null && !uiCell.toString().trim().equals("")) {
				if (!(uiCell.toString().toUpperCase().trim()).equals(nameUiFiltered.toUpperCase())) {
					validCampos = false;
					descripcionError += "Fila " + nroFila + " - ";
					if (codOrdenInfiCell != null)
						descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
					descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_UI + "': La Unidad de Inversión ingresada (" + uiCell.toString().trim() + ") no corresponde a la seleccionada (" + nameUiFiltered + ") | ";
					Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_UI + "': La Unidad de Inversión ingresada (" + uiCell.toString().trim() + ") no corresponde a la seleccionada (" + nameUiFiltered + ")");
				}
				// //VALIDACION UNIDAD DE INVERSION (CONTRA TABLA INFI_TB_106_UNIDAD_INVERSION)
				// if(unidadInvDAO.getUiPorNombre(uiCell.toString()) == 0){
				// validCampos = false;
				// descripcionError += "Fila "+nroFila+" - ";
				// if(codOrdenInfiCell!=null) descripcionError += "Orden "+codOrdenInfiCell.toString().replaceAll("\\.0", "")+" - ";
				// descripcionError += "Campo '"+ConstantesGenerales.CAMPO_PLANTILLA_UI+"': No existe una Unidad de Inversión con ese nombre en base de datos ("+uiCell.toString()+") | ";
				// Logger.info(this,"Error al procesar archivo de cruces '"+getNombreDocumento()+"' en la Fila "+nroFila+" - Campo '"+ConstantesGenerales.CAMPO_PLANTILLA_UI+"': No existe una Unidad de Inversión con ese nombre en base de datos ("+uiCell.toString()+")");
				// }else{ //Existe la UI
				// unidadInvDAO.getDataSet().first();
				// unidadInvDAO.getDataSet().next();
				// //Se setean los datos relacionados con la UI
				// cruce.setIdUI(Long.parseLong(unidadInvDAO.getDataSet().getValue("UNDINV_ID")));
				//  				
				// if(orden.getIdOrden()!=0){ //La orden existe
				// //VALIDA LA UI (CONTRA LA ORDEN)
				// if((long)(orden.getIdUnidadInversion())!=cruce.getIdUI()){ //Si no coincide con la UI de la orden
				// validCampos = false;
				// descripcionError += "Fila "+nroFila+" - ";
				// if(codOrdenInfiCell!=null) descripcionError += "Orden "+codOrdenInfiCell.toString().replaceAll("\\.0", "")+" - ";
				// descripcionError += "Campo '"+ConstantesGenerales.CAMPO_PLANTILLA_UI+"': La Unidad de Inversion no coincide concuerda con la de la orden indicada | ";
				// Logger.info(this,"Error al procesar archivo de cruce de ordenes '"+getNombreDocumento()+"' en la Fila "+nroFila+" - Campo '"+ConstantesGenerales.CAMPO_PLANTILLA_UI+"': La Unidad de Inversion no coincide concuerda con la de la orden indicada");
				// }
				// }
				// }
			} else {
				validCampos = false;
				descripcionError += "Fila " + nroFila + " - ";
				if (codOrdenInfiCell != null)
					descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
				descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_UI + "': El campo es vacio | ";
				Logger.info(this, "Error al procesar archivo de cruce de ordenes '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_UI + "': El campo es vacio");
			}
			Logger.debug(this, "PASA VALIDACION UI");
			// VALIDACION RIF/CI (FORMA)
			m = patRifCi.matcher(cedulaCell.toString());
			if (!m.find()) { // Si el registro no coincide con el formato correspondiente
				validCampos = false;
				descripcionError += "Fila " + nroFila + " - ";
				if (codOrdenInfiCell != null)
					descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
				descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_RIFCI + "': Formato invalido | ";
				Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_RIFCI + "': Formato invalido");
			} else {
				// VALIDACION RIF/CI (CONTRA ORDEN)
				if (orden.getIdOrden() != 0) { // La orden existe

					// VALIDACION CLIENTE (CONTRA TABLA INFI_TB_201_CTES)
					String ced_rif = "", tipper_id = "";
					try {
						tipper_id = cedulaCell.toString().substring(0, 1);
						ced_rif = cedulaCell.toString().substring(1);
						Logger.debug(this, "ced_rif: " + ced_rif + ", tipper_id: " + tipper_id);
						cruce.setCiRif(cedulaCell.toString());
					} catch (Exception e) {
						validCampos = false;
						descripcionError += "Fila " + nroFila + " - ";
						if (codOrdenInfiCell != null)
							descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
						descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_RIFCI + "': Campo vacio o de longitud erronea | ";
						Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_RIFCI + "': Campo vacio o de longitud erronea");
					}

					// BUSCA LA INFO DEL CLIENTE
					clienteDAO.listarCliente(0, tipper_id, Long.parseLong(ced_rif));
					if (!clienteDAO.getDataSet().next()) {
						validCampos = false;
						descripcionError += "Fila " + nroFila + " - ";
						if (codOrdenInfiCell != null)
							descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
						descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_RIFCI + "': No se encuentra registrado en el sistema | ";
						Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_RIFCI + "': No se encuentra registrado en el sistema");
					} else { // Se encontro el cliente en bd
						Logger.debug(this, "Cliente encontrado");

						// VALIDACION CLIENTE (CONTRA ORDEN)
						if (!(orden.getIdCliente() + "").equals(clienteDAO.getDataSet().getValue("CLIENT_ID"))) {
							validCampos = false;
							descripcionError += "Fila " + nroFila + " - ";
							if (codOrdenInfiCell != null)
								descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
							descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_RIFCI + "': No se corresponde con el cliente de la orden | ";
							Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_RIFCI + "': No se corresponde con el cliente de la orden");
						} else {
							// Se seta Id de Cliente a al cruce
							cruce.setIdCliente(orden.getIdCliente());

							// NM26659_19/08/2014 Cambio en version TTS_466 Validacion de Blotters - Documentos para la orden
							// Valida si el Blotter de la unidad es el mismo con el que se tomo la orden y tipo de persona asociado a la orden estan configurados
							String tipoPersona = clienteDAO.getDataSet().getValue("tipper_id");
							String clave = tipoPersona.concat(",").concat((orden.getIdBloter()).toUpperCase());
							// NM26659_26/01/2015 Correcion indicencia ITS-2449 PRODUCCION
							if (!documentosTipPersona.contains(clave)) {

								boolean contieneTipPersona = false;
								String[] arrayTipBlotter;
								String tipPersona = null;

								for (String element : documentosTipPersona) {
									tipPersona = null;

									arrayTipBlotter = element.split(",");

									tipPersona = arrayTipBlotter[0];
									if (tipPersona.equalsIgnoreCase(tipoPersona)) {
										contieneTipPersona = true;
										break;
									}

								}
								if (!contieneTipPersona) {
									validCampos = false;
									descripcionError += "Fila " + nroFila + " - ";
									if (codOrdenInfiCell != null)
										descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
									descripcionError += " El tipo de persona no se encuentra configurado para los blotter asociados a la unidad de inversion o no han sido cargados los documentos del blotter | ";
									Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - : El tipo de persona no se encuentra configurado para los blotter asociados a la unidad de inversion o no han sido cargados los documentos del blotter");
								} else {
									validCampos = false;
									descripcionError += "Fila " + nroFila + " - ";
									if (codOrdenInfiCell != null)
										descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
									descripcionError += " No existe el blotter asociado al tipo de persona o no han sido cargados los documentos del blotter | ";
									Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - : No existe el blotter asociado al tipo de persona o no han sido cargados los documentos del blotter  ");

								}
							}
						}
					}// Se encontro el cliente en bd

				}// La orden existe
				else
					Logger.debug(this, "NO SE TRAJO INFO DE LA ORDEN DE BD");

			}// Pasa Validacion RIF/CI (FORMA)
			Logger.debug(this, "PASA VALIDACION RIF CI");
			//VALIDACION SI LA UNIDAD DE INVERSION ES DE TIPO MENUDEO
			if(isValidacionUnidadMenudeo()){//UNIDAD INVERSION 	MENUDEO
				// TTS-491 Modificacion para notificacion BCV via Web Service NM26659 03/03/2015
				if (!validacionBcvEnLinea) {//SI EL PROCESO ESTA CONFIGURACION COMO VERIFICACION MANUAL
					if (identificadorPlantilla.equals(ConstantesGenerales.TITULO_TEMPLATE_CRUCE)) {							
						//VERIFICACION SI PROCESAMIENTO ES MANUAL Y LA ORDEN YA FUE VERIFICADA VIA BCV
						if(orden.getStatusVerificacionBCV().equalsIgnoreCase(ConstantesGenerales.VERIFICADA_APROBADA) && (cruce.getNroCotizacionString().length()>0)){						
							validCampos = false;
							descripcionError += "Fila " + nroFila + " - ";
							if (codOrdenInfiCell != null)
								descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
							descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_NRO_COTIZACION + "': La orden fue verificada via BCV en linea por lo que no puede colocar un n&uacute;mero de cotizaci&oacute;n | ";
							Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_NRO_COTIZACION + "': La orden fue verificada via BCV en linea por lo que no puede colocar un n&uacute;mero de cotizaci&oacute;n | ");
						}				
						if(orden.getStatusVerificacionBCV().equalsIgnoreCase(ConstantesGenerales.SIN_VERIFICAR)){						
							
							// VALIDACION NUMERO COTIZACION (FORMA)						
							if (nroCotizacionCell.toString().equals("") || nroCotizacionCell.toString().length()==0) {							
								validCampos = false;
								descripcionError += "Fila " + nroFila + " - ";
								if (codOrdenInfiCell != null)
									descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
								descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_NRO_COTIZACION + "': No se ha ingresado valor en el campo Numero Cotizacion ";
								Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_NRO_COTIZACION + "': El valor ingresado no es de tipo numerico (" + nroCotizacionCell.toString() + ")");
							} 
							// VALIDACION NRO COTIZACION (FORMA)						
							if (cruce.getNroCotizacionString() != null && cruce.getNroCotizacionString().length() > 20) { // SE VALIDA QUE NO EXCEDA EL TAM MAX DEL CAMPO EN BD
								// Se trunca el valor a 10 caracteres
								cruce.setNroCotizacionString(cruce.getNroCotizacionString().substring(0, 10));
								validCampos = false;
								descripcionError += "Fila " + nroFila + " - ";
								if (codOrdenInfiCell != null)
									descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
								descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_NRO_COTIZACION + "': Formato invalido: No debe exceder de 20 caracteres | ";
								Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_NRO_COTIZACION + "': Formato invalido: No debe exceder de 10 digitos");
							}					
							} else if(orden.getStatusVerificacionBCV().equalsIgnoreCase(ConstantesGenerales.VERIFICADA_APROBADA)){
								//configuracion del numero de orden BCV en el registro de cruce
								cruce.setNroCotizacionString(orden.getIdOrdeneBCV());
							}
							// VALIDACION NRO COTIZACION (REPETIDO EN OTRA ORDEN)
							Logger.debug(this, "nroCotizacionCell : " + nroCotizacionCell.toString());
							Logger.debug(this, "getNroCotizacionString : " + cruce.getNroCotizacionString());	
					}
				} else {//SI EL PROCESO ESTA CONFIGURACION COMO VALIDACION BCV EN LINEA 
									
					if (identificadorPlantilla.equals(ConstantesGenerales.TITULO_TEMPLATE_CRUCE)) {										
						/*System.out.println("FLAG 35 ----> " + cruce.getNroCotizacionString());
						if (cruce.getNroCotizacionString()!=null || cruce.getNroCotizacionString().length()>0){
							System.out.println("****************** TIPO DE INGRESO -----> TRUE ");
						}else {
							System.out.println("****************** TIPO DE INGRESO -----> FALSE ");
						}*/
						if (cruce.getNroCotizacionString().length()>0 && !cruce.getNroCotizacionString().equals("")){//ERROR - SI CAMPO DE ORDEN BCV EN PLANTILLA NO ES NULL
							validCampos = false;
							descripcionError += "Fila " + nroFila + " - ";
							if (codOrdenInfiCell != null)
								descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
							descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_NRO_COTIZACION + "': El parametro de verificacion BCV en linea se encuentra activo, no debe ingresar ningun valor en el campo numero cotizacion | ";
							Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_NRO_COTIZACION + "': El parametro de verificacion BCV en linea se encuentra activo, no debe ingresar ningun valor en el campo numero cotizacion");
						}
						cruce.setNroCotizacionString(orden.getIdOrdeneBCV());
						/*System.out.println("ESTATUS ORDEN BCV ---> " + orden.getStatusVerificacionBCV());
						System.out.println("NUMERO ORDEN EN 204 ---> " + orden.getIdOrdeneBCV());
						//Se configura el valor de numero de cotizacion a partir de la informacion guardada en la tabla INFI_TB_204_ORDENES
						if(orden.getStatusVerificacionBCV().equalsIgnoreCase(ConstantesGenerales.VERIFICADA_APROBADA)){
							cruce.setNroCotizacionString(orden.getIdOrdeneBCV());
						}*/
						/* else {//SI CAMPO DE ORDEN BCV EN PLANTILLA ES IGUAL A NULL
							if(orden.getStatusVerificacionBCV().equals(ConstantesGenerales.SIN_VERIFICAR)){//ERROR - SI LA ORDEN NO HA SIDO VERIFICADA EN PARAMETRO VALIDACION BCV EN LINEA ESTA ACTIVO 
								validCampos = false;
								descripcionError += "Fila " + nroFila + " - ";
								if (codOrdenInfiCell != null)
									descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
								descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_NRO_COTIZACION + "': El parametro de verificacion BCV en linea se encuentra activo, no debe ingresar ningun valor en el campo numero cotizacion | ";
								Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_NRO_COTIZACION + "': El parametro de verificacion BCV en linea se encuentra activo, no debe ingresar ningun valor en el campo numero cotizacion");
							} else {
								cruce.setNroCotizacionString(orden.getIdOrdeneBCV());	
							}																	
						}*/
					}			
				}		
			} else {//UNIDAD INVERSION 	ALTO VALOR
				if (cruce.getNroCotizacionString().length()>0 && !cruce.getNroCotizacionString().equals("")){//ERROR - SI CAMPO DE ORDEN BCV EN PLANTILLA NO ES NULL
					validCampos = false;
					descripcionError += "Fila " + nroFila + " - ";
					if (codOrdenInfiCell != null)
						descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
					descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_NRO_COTIZACION + "': La unidad de inversion a procesar no es de tipo MENUDEO, no debe ingresar ningun valor en el campo numero cotizacion | ";
					Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_NRO_COTIZACION + "': La unidad de inversion a procesar no es de tipo MENUDEO, no debe ingresar ningun valor en el campo numero cotizacion");
				}
			}
			
			Logger.debug(this, "PASA VALIDACION NRO COTIZACION");

			// VALIDACION MONTO SOLICITADO (FORMA Y CONTRA ORDEN)
			try {
				cruce.setMontoSolicitado(new BigDecimal(Double.parseDouble(mtoSolicitadoCell.toString())).setScale(2, BigDecimal.ROUND_HALF_UP));
				Logger.debug(this, "MONTO --------SOLIC: " + cruce.getMontoSolicitado());

				if (orden.getIdOrden() != 0) { // La orden existe
					// VALIDACION DE CAMPO MONTO_SOLICITADO (CONTRA ORDEN)
					compare = cruce.getMontoSolicitado().compareTo(new BigDecimal(orden.getMonto()).setScale(2, BigDecimal.ROUND_HALF_UP));
					if (compare != 0) {
						validCampos = false;
						cruce.setMontoSolicitado(null);
						if (compare > 0) {
							descripcionError += "Fila " + nroFila + " - ";
							if (codOrdenInfiCell != null)
								descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
							descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_MONTO_SOLIC + "': El valor ingresado (" + mtoSolicitadoCell.toString() + ") no coincide con el de la orden (" + orden.getMonto() + "), ya que es mayor al mismo. Se redondea a 2 decimales | ";
							Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_MONTO_SOLIC + "': El valor ingresado (" + mtoSolicitadoCell.toString() + ") no coincide con el de la orden (" + orden.getMonto() + "), ya que es mayor al mismo. Se redondea a 2 decimales");
						} else {
							descripcionError += "Fila " + nroFila + " - ";
							if (codOrdenInfiCell != null)
								descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
							descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_MONTO_SOLIC + "': El valor ingresado (" + mtoSolicitadoCell.toString() + ") no coincide con el de la orden (" + orden.getMonto() + "), ya que es menor al mismo. Se redondea a 2 decimales | ";
							Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Orden " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_MONTO_SOLIC + "': El valor ingresado (" + mtoSolicitadoCell.toString() + ") no coincide con el de la orden (" + orden.getMonto() + "), ya que es menor al mismo. Se redondea a 2 decimales");
						}
					} else {
						compare = cruce.getMontoSolicitado().compareTo(new BigDecimal(0));
						if (compare <= 0) {
							validCampos = false;
							cruce.setMontoSolicitado(null);
							descripcionError += "Fila " + nroFila + " - ";
							if (codOrdenInfiCell != null)
								descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
							descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_MONTO_SOLIC + "': El valor ingresado es menor o igual a cero (" + mtoSolicitadoCell.toString() + ") | ";
							Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - campo '" + ConstantesGenerales.CAMPO_PLANTILLA_MONTO_SOLIC + "': El valor ingresado es menor o igual a cero (" + mtoSolicitadoCell.toString() + ")");
						}
					}
				}// La ordene xiste
			} catch (NumberFormatException ex) {
				cruce.setMontoSolicitado(null);
				validCampos = false;
				descripcionError += "Fila " + nroFila + " - ";
				if (codOrdenInfiCell != null)
					descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
				descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_MONTO_SOLIC + "': El valor ingresado no es de tipo numerico (" + mtoSolicitadoCell.toString() + ") | ";
				Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Orden " + nroFila + " - campo '" + ConstantesGenerales.CAMPO_PLANTILLA_MONTO_SOLIC + "': El valor ingresado no es de tipo numerico (" + mtoSolicitadoCell.toString() + ")");
			}
			Logger.debug(this, "PASA VALIDACION MONTO SOLICITADO");

			// VALIDACION MONTO CRUCE (FORMA)
			if (mtoCruceCell.toString() != null && mtoCruceCell.toString().length() > 20) { // SE VALIDA QUE NO EXCEDA EL TAM MAX DEL CAMPO EN BD
				// Se trunca el valor a 20 caracteres
				cruce.setMontoOperacionString(mtoCruceCell.toString().substring(0, 20));
				validCampos = false;
				descripcionError += "Fila " + nroFila + " - ";
				if (codOrdenInfiCell != null)
					descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
				descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_MONTO_CRUCE + "': Formato invalido: No debe exceder de 20 digitos | ";
				Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_MONTO_CRUCE + "': Formato invalido: No debe exceder de 20 digitos");
			}

			// VALIDACION MONTO CRUCE (FORMA Y CONTRA ORDEN)
			try {
				cruce.setMontoOperacion(new BigDecimal(Double.parseDouble(mtoCruceCell.toString())).setScale(2, BigDecimal.ROUND_HALF_UP));
				Logger.info(this, "MONTO CRUCE-------" + cruce.getMontoOperacion());

				if (orden.getIdOrden() != 0) { // La orden existe
					// Se CALCULA EL MONTO DISPONIBLE PARA CRUCE (sumCruces = Sumatoria de los cruces ya realizados a la orden, de haberlos)
					Logger.debug(this, "SUM----CRUCESSS: " + sumCruces);
					BigDecimal mtoDisponibleCruce = (new BigDecimal(orden.getMonto()).setScale(2, BigDecimal.ROUND_HALF_UP)).subtract(sumCruces).setScale(2, BigDecimal.ROUND_HALF_UP);
					Logger.info(this, "MONTO DISPONIBLE CRUCE-------" + mtoDisponibleCruce);
					compare = cruce.getMontoOperacion().compareTo(mtoDisponibleCruce);
					if (compare > 0) {
						validCampos = false;
						descripcionError += "Fila " + nroFila + " - ";
						if (codOrdenInfiCell != null)
							descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
						descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_MONTO_CRUCE + "': El valor ingresado (" + mtoCruceCell.toString() + ") supera el monto de cruce disponible (" + mtoDisponibleCruce + "). Se redondea a 2 decimales. | ";
						Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_MONTO_CRUCE + "': El valor ingresado (" + mtoCruceCell.toString() + ") supera el monto de cruce disponible (" + mtoDisponibleCruce + "). Se redondea a 2 decimales");
					}
				}// La orden existe
			} catch (NumberFormatException ex) {
				cruce.setMontoOperacion(null);
				validCampos = false;
				descripcionError += "Fila " + nroFila + " - ";
				if (codOrdenInfiCell != null)
					descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
				descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_MONTO_CRUCE + "': El valor ingresado no es de tipo numerico (" + mtoCruceCell.toString() + ") | ";
				Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Orden " + nroFila + " - campo '" + ConstantesGenerales.CAMPO_PLANTILLA_MONTO_CRUCE + "': El valor ingresado no es de tipo numerico (" + mtoCruceCell.toString() + ")");
			}
			Logger.debug(this, "PASA VALIDACION MONTO CRUCE");

			// VALIDACION TASA (FORMA)
			if (tasaCell.toString() != null && tasaCell.toString().length() > 15) { // SE VALIDA QUE NO EXCEDA EL TAM MAX DEL CAMPO EN BD
				// Se trunca el valor a 20 caracteres
				cruce.setTasaString(tasaCell.toString().substring(0, 15));
				validCampos = false;
				descripcionError += "Fila " + nroFila + " - ";
				if (codOrdenInfiCell != null)
					descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
				descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_TASA + "': Formato invalido: No debe exceder de 15 digitos | ";
				Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_TASA + "': Formato invalido: No debe exceder de 15 digitos");
			}

			// VALIDACION DE CAMPO TASA (FORMA)
			try {
				double tasa = Double.parseDouble(tasaCell.toString());
				cruce.setTasa(new BigDecimal(tasa).setScale(4, BigDecimal.ROUND_HALF_UP));
				// cruce.setTasa(new BigDecimal(Double.parseDouble(tasaCell.toString())).setScale(2, BigDecimal.ROUND_HALF_UP));
				Logger.debug(this, "Se setea la Tasa: " + cruce.getTasa());

				// //VALIDACION DE CAMPO TASA (CONTRA ORDEN)
				// if(orden.getIdOrden()!=0){ //La orden existe
				// compare = cruce.getTasa().compareTo(orden.getTasaPool());
				// if(compare!=0){
				// validCampos = false;
				// descripcionError += "Fila "+nroFila+" - ";
				// if(codOrdenInfiCell!=null) descripcionError += "Orden "+codOrdenInfiCell.toString().replaceAll("\\.0", "")+" - ";
				// descripcionError += "Campo '"+ConstantesGenerales.CAMPO_PLANTILLA_TASA+"': El valor ingresado ("+tasaCell.toString()+") no coincide con la tasa ingresada en la orden ("+orden.getTasaPool()+") | ";
				// Logger.info(this,"Error al procesar archivo de cruces '"+getNombreDocumento()+"' en la Fila "+nroFila+" - Campo '"+ConstantesGenerales.CAMPO_PLANTILLA_TASA+"': El valor ingresado ("+tasaCell.toString()+") no coincide con la tasa ingresada en la orden ("+orden.getTasaPool()+")");
				// }
				// }//La orden existe
			} catch (Exception ex) {
				validCampos = false;
				descripcionError += "Fila " + nroFila + " - ";
				if (codOrdenInfiCell != null)
					descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
				descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_TASA + "': El valor ingresado no es de tipo numerico (" + tasaCell.toString() + ") | ";
				Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_TASA + "': El valor ingresado no es de tipo numerico (" + tasaCell.toString() + ")");
			}
			Logger.debug(this, "PASA VALIDACION TASA");

			// VALIDACIONES PARA PLANTILLA DE CRUCE UNICAMENTE
			if (identificadorPlantilla.equals(ConstantesGenerales.TITULO_TEMPLATE_CRUCE)) {

				// VALIDACION MONTO OPERACION
				if (cruce.getMontoOperacion() != null && cruce.getMontoOperacion().compareTo(new BigDecimal(0)) == 0) {
					validCampos = false;
					descripcionError += "Fila " + nroFila + " - ";
					if (codOrdenInfiCell != null)
						descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
					descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_MONTO_CRUCE + "': El valor del monto a cruzar no puede ser cero | ";
					Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_MONTO_CRUCE + "': El valor del monto a cruzar no puede ser cero");
				}
				Logger.debug(this, "PASA VALIDACION MONTO OPERACION NO CERO PARA CRUCE");

				//flag daniel				
				if(cruce.getNroOperacionString().length()>0){				
					validCampos = false;
					descripcionError += "Fila " + nroFila + " - ";
					if (codOrdenInfiCell != null)
						descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
					descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_NRO_OPERACION + "': Formato invalido: El campo no debe ser llenado | ";
					Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_NRO_OPERACION + "': Formato invalido: El campo no debe ser llenado ");
				}
				// VALIDACION NRO OPERACION (FORMA)
				// System.out.println("CRUCE NRO OPERACION------"+cruce.getNroOperacionString());
				/*if (cruce.getNroOperacionString() != null && cruce.getNroOperacionString().length() > 10) { // SE VALIDA QUE NO EXCEDA EL TAM MAX DEL CAMPO EN BD
					// Se trunca el valor a 10 caracteres
					cruce.setNroOperacionString(cruce.getNroOperacionString().substring(0, 10));
					validCampos = false;
					descripcionError += "Fila " + nroFila + " - ";
					if (codOrdenInfiCell != null)
						descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
					descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_NRO_OPERACION + "': Formato invalido: No debe exceder de 10 digitos | ";
					Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_NRO_OPERACION + "': Formato invalido: No debe exceder de 10 digitos");
				}*/

				// VALIDACION NRO OPERACION (FORMA)
			/*	Logger.debug(this, "NRO OPERACION----A VALIDAR----" + cruce.getNroOperacionString());
				m = patNum.matcher(cruce.getNroOperacionString());
				if (!m.find()) { // Si el registro no coincide con el formato correspondiente
					validCampos = false;
					descripcionError += "Fila " + nroFila + " - ";
					if (codOrdenInfiCell != null)
						descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
					descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_NRO_OPERACION + "': Formato invalido | ";
					Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_NRO_OPERACION + "': Formato invalido");
				} else {
					Logger.debug(this, "NRO OPERACION PASA VALIDACION FORMA REGEX");
					// VALIDACION NRO OPERACION (LOTE A CARGAR)
					if (nrosOperacion.containsKey(cruce.getNroOperacionString())) {
						validCampos = false;
						descripcionError += "Fila " + nroFila + " - ";
						if (codOrdenInfiCell != null)
							descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
						descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_NRO_OPERACION + "': Ya existe un registro con el Nro de Operacion " + cruce.getNroOperacionString() + " en el lote de cruces a cargar | ";
						Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_NRO_OPERACION + "': Ya existe un registro con el Nro de Operacion " + cruce.getNroOperacionString() + " en el lote de cruces a cargar");
					} else {
						// VALIDACION NRO OPERACION (CONTRA CRUCES EN BD)
						ordenesCrucesDAO.getByNroOperacion(cruce.getNroOperacionString(), StatusOrden.CRUZADA, StatusOrden.NO_CRUZADA);
						if (ordenesCrucesDAO.getDataSet().count() > 0) {
							validCampos = false;
							descripcionError += "Fila " + nroFila + " - ";
							if (codOrdenInfiCell != null)
								descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
							descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_NRO_OPERACION + "': Ya existe un registro cargado con el Nro de Operacion " + cruce.getNroOperacionString() + " | ";
							Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_NRO_OPERACION + "': Ya existe un registro cargado con el Nro de Operacion " + cruce.getNroOperacionString());
						}
					}
				}*/
				Logger.debug(this, "PASA VALIDACION NRO OPERACION");

				// VALIDACION CONTRAPARTE (FORMA)
				if (contraparteCell.toString() != null && contraparteCell.toString().length() > 100) { // SE VALIDA QUE NO EXCEDA EL TAM MAX DEL CAMPO EN BD
					// Se trunca el valor a 100 caracteres
					cruce.setContraparte(contraparteCell.toString().substring(0, 100));
					validCampos = false;
					descripcionError += "Fila " + nroFila + " - ";
					if (codOrdenInfiCell != null)
						descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
					descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_CONTRAPARTE + "': Formato invalido: No debe exceder de 100 caracteres | ";
					Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_CONTRAPARTE + "': Formato invalido: No debe exceder de 100 caracteres");
				}

				// VALIDACION CONTRAPARTE (FORMA)
				m = patAlfaNum.matcher(contraparteCell.toString());
				if (!m.find()) { // Si el registro no coincide con el formato correspondiente
					validCampos = false;
					descripcionError += "Fila " + nroFila + " - ";
					if (codOrdenInfiCell != null)
						descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
					descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_CONTRAPARTE + "': Formato invalido ------ | ";
					Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_CONTRAPARTE + "': Formato invalido");
				}
				Logger.debug(this, "PASA VALIDACION CONTRAPARTE");

				// VALIDACION FECHA VALOR (FORMA)
				if (fechaValorCell.toString() != null && fechaValorCell.toString().length() > 10) { // SE VALIDA QUE NO EXCEDA EL TAM MAX DEL CAMPO EN BD
					// Se trunca el valor a 10 caracteres
					cruce.setFechaValor(fechaValorCell.toString().substring(0, 10));
					validCampos = false;
					descripcionError += "Fila " + nroFila + " - ";
					if (codOrdenInfiCell != null)
						descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
					descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_FECHA_VALOR + "': Formato invalido: No debe exceder de 10 caracteres | ";
					Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_FECHA_VALOR + "': Formato invalido: No debe exceder de 10 caracteres");
				}

				// VALIDACION ISIN (FORMA)
				if (isinCell != null && !isinCell.toString().trim().equals("")) { // Si es cruce de un TITULO
					titulo = true;

					// VALIDACION ISIN (FORMA)
					if (isinCell.toString() != null && isinCell.toString().length() > 25) { // SE VALIDA QUE NO EXCEDA EL TAM MAX DEL CAMPO EN BD
						// Se trunca el valor a 25 caracteres
						cruce.setIsinString(isinCell.toString().substring(0, 25));
						validCampos = false;
						descripcionError += "Fila " + nroFila + " - ";
						if (codOrdenInfiCell != null)
							descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
						descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_ISIN + "': Formato invalido: No debe exceder de 25 caracteres | ";
						Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_ISIN + "': Formato invalido: No debe exceder de 25 caracteres");
					}

					// VALIDACION ISIN (FORMA)
					m = patAlfaNum.matcher(isinCell.toString());
					if (!m.find()) { // Si el registro no coincide con el formato correspondiente
						validCampos = false;
						descripcionError += "Fila " + nroFila + " - ";
						if (codOrdenInfiCell != null)
							descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
						descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_ISIN + "': Formato invalido | ";
						Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_ISIN + "': Formato invalido");
					} else { // VALIDACION ISIN (CONTRA TITULOS OPICS)
						TitulosDAO titulosDAO = new TitulosDAO(dataSource);
						titulosDAO.verifIsinTitulo(cruce.getIsinString());
						DataSet titulos = titulosDAO.getDataSet();
						if (titulos.count() > 0) { // Si encontro Titulo con dicho ISIN
							titulos.first();
							titulos.next();
							// SE SETEA EL ID DEL TITULO
							cruce.setIdTitulo(titulos.getValue("SECID").trim());
							String incrementalTitulo = titulos.getValue("incremental") == null ? "" : titulos.getValue("incremental").trim();// ct19940 ajustes para version CALIDAD
							// VALIDACION DE VALOR DE CAMPO INCREMENTAL (MININCREMENT) DE TABLA SECM DE OPICS
							if (incrementalTitulo == null || incrementalTitulo.equals("")) {
								validCampos = false;
								descripcionError += "Fila " + nroFila + " - : El Titulo que se esta procesando no tiene el valor de la tasa incremental registrado";// ct19940 ajustes para version CALIDAD
								Logger.info(this, "Error al procesar carga archivo '" + getNombreDocumento() + "' en la Fila " + nroFila + ": El Titulo que se esta procesando no tiene el valor de la tasa incremental registrado");
							} else {
								// Configuracion de monto de incremental asignado al titulo
								cruce.setIncrementalTitulo(Long.parseLong(incrementalTitulo));

							}

							Logger.debug(this, "TITULO ID EN BD----------" + cruce.getIdTitulo());

						} else {
							validCampos = false;
							descripcionError += "Fila " + nroFila + " - ";
							if (codOrdenInfiCell != null)
								descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
							descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_ISIN + "': El ISIN indicado no corresponde a ningun Titulo | ";
							Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_ISIN + "': El ISIN indicado no corresponde a ningun Titulo");
						}
					}
				}
				Logger.debug(this, "PASA VALIDACION ISIN");

				if (titulo) {// Si es cruce de un TITULO

					// VALIDACION PRECIO TITULO (FORMA)
					if (precioTitCell.toString() != null && precioTitCell.toString().length() > 15) { // SE VALIDA QUE NO EXCEDA EL TAM MAX DEL CAMPO EN BD
						// Se trunca el valor a 15 caracteres
						cruce.setPrecioTituloString(precioTitCell.toString().substring(0, 15));
						validCampos = false;
						descripcionError += "Fila " + nroFila + " - ";
						if (codOrdenInfiCell != null)
							descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
						descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_PRECIO_TITULO + "': Formato invalido: No debe exceder de 15 caracteres | ";
						Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_PRECIO_TITULO + "': Formato invalido: No debe exceder de 15 caracteres");
					}

					// VALIDACION DE CAMPO PRECIO TITULO (FORMA)
					try {
						cruce.setPrecioTitulo(new BigDecimal(Double.parseDouble(precioTitCell.toString())).setScale(2, BigDecimal.ROUND_HALF_UP));
						Logger.debug(this, "Se setea el Precio del Titulo: " + cruce.getPrecioTitulo());

						if (cruce.getIdTitulo() != null) {

							Logger.debug(this, "Titulo Id distinto a null");
							Logger.debug(this, "idsTitulosOrdenes: " + idsTitulosOrdenes);

							// VALIDACION PRECIO IGUAL PARA CRUCES DEL MISMO TITULO (LOTE A CARGAR)
							if (idsTitulosOrdenes.containsKey(cruce.getIdTitulo() + "|" + cruce.getIdOrdenInfiString())) {
								Logger.debug(this, "Hashtable contiene clave titulo+orden");
								precioFecha = idsTitulosOrdenes.get(cruce.getIdTitulo() + "|" + cruce.getIdOrdenInfiString());
								Logger.debug(this, "Precio Titulo en Lote: " + precioFecha.substring(0, precioFecha.indexOf("|")));
								if (new BigDecimal(precioFecha.substring(0, precioFecha.indexOf("|"))).compareTo(cruce.getPrecioTitulo()) != 0) {
									validCampos = false;
									descripcionError += "Fila " + nroFila + " - ";
									if (codOrdenInfiCell != null)
										descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
									descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_PRECIO_TITULO + "': El valor no se corresponde con el seteado para un cruce de la misma orden (" + idsTitulosOrdenes.get(cruce.getIdTitulo() + "|" + cruce.getIdOrdenInfiString()) + ") incluido en el lote de carga | ";
									Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_PRECIO_TITULO + "': El valor no se corresponde con el seteado para un cruce de la misma orden (" + idsTitulosOrdenes.get(cruce.getIdTitulo() + "|" + cruce.getIdOrdenInfiString())
											+ ") incluido en el lote de carga");
								} else
									Logger.debug(this, "Precio Titulo IGUAL");
							} else
								Logger.debug(this, "Hashtable no contiene clave titulo+orden");

							// VALIDACION PRECIO IGUAL PARA CRUCES DEL MISMO TITULO (CONTRA BD)
							// FLAG BUSQUEDA 8
							// NM26659 Cambio para mejora de Performace de procesos
							// ordenesCrucesDAO.listarCrucesPorIdOrdenInfi(cruce.getIdOrdenINFI(), cruce.getIdTitulo(), StatusOrden.CRUZADA);
							// crucesMismaOrdenTitulo = ordenesCrucesDAO.getDataSet();
							if (_resgitrosCruces.count() > 0) {
								_resgitrosCruces.first();
								_resgitrosCruces.next();
								if (_resgitrosCruces.getValue("TITULO_ID") != null && !_resgitrosCruces.getValue("TITULO_ID").equals("")) {
									if (_resgitrosCruces.getValue("TITULO_ID").equalsIgnoreCase(cruce.getIdTitulo()) && _resgitrosCruces.getValue("ESTATUS").equalsIgnoreCase(StatusOrden.CRUZADA)) {
										if ((new BigDecimal(_resgitrosCruces.getValue("PRECIO_TITULO")).setScale(2, BigDecimal.ROUND_HALF_UP)).compareTo(cruce.getPrecioTitulo()) != 0) {
											validCampos = false;
											descripcionError += "Fila " + nroFila + " - ";
											if (codOrdenInfiCell != null)
												descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
											descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_PRECIO_TITULO + "': El valor no se corresponde con el seteado para un cruce de la misma orden (" + crucesMismaOrdenTitulo.getValue("PRECIO_TITULO") + ") cargado previamente | ";
											Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_PRECIO_TITULO + "': El valor no se corresponde con el seteado para un cruce de la misma orden (" + crucesMismaOrdenTitulo.getValue("PRECIO_TITULO") + ") cargado previamente");
										}
									}
								} else {
									Logger.debug(this, "NO Se obtienen cruces en bd para la orden con el mismo titulo");

								}

							}
						}

						// }//ID Titulo seteado

					} catch (NumberFormatException ex) {
						validCampos = false;
						descripcionError += "Fila " + nroFila + " - ";
						if (codOrdenInfiCell != null)
							descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
						descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_PRECIO_TITULO + "': El valor ingresado no es de tipo numerico (" + precioTitCell.toString() + ") | ";
						Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_PRECIO_TITULO + "': El valor ingresado no es de tipo numerico (" + precioTitCell.toString() + ")");
					}
					// TODO VERIFICAR SI HAY QUE VALIDAR EL PRECIO DEL TITULO CONTRA ALGUN VALOR EN BD
					Logger.debug(this, "PASA VALIDACION PRECIO TIT");

					// VALIDACION DE CAMPO FECHA VALOR
					try {
						boolean fValRep = false;
						String dia = "", mes = "", anio = "";
						m = patFecha.matcher(fechaValorCell.toString());
						if (m.find()) { // Si el registro coincide con el formato correspondiente
							dia = fechaValorCell.toString().substring(0, 2);
							mes = fechaValorCell.toString().substring(3, 5);
							anio = fechaValorCell.toString().substring(6, 10);
							// Setea la fecha valor a la orden aprobacion
							cruce.setFechaValor(dia + "/" + mes + "/" + anio);
							cruce.setFechaValorGuion(dia + "-" + mes + "-" + anio);

							/*
							 * //System.out.println("idsTitulosOrdenes ----" + fValorMap.toString()); //System.out.println("idsTitulosOrdenes GET ----" + fValorMap.get(cruce.getIdTitulo()+"|"+cruce.getIdOrdenInfiString())); //System.out.println("FECHA VALOR ----" + cruce.getFechaValor());
							 */

							// System.out.println("FECHA VALOR GETSub----" + idsTitulosOrdenes.get(cruce.getIdTitulo()+"|"+cruce.getIdOrdenInfiString()).substring(idsTitulosOrdenes.get(cruce.getIdTitulo()+"|"+cruce.getIdOrdenInfiString()).indexOf("|")+1));
							// VALIDACION FECHA VALOR IGUAL PARA CRUCES DEL MISMO TITULO y ORDEN (LOTE A CARGAR)
							// String fechaValor = cruce.getFechaValor();
							if (fValorMap.containsKey(cruce.getIdTitulo() + "|" + cruce.getIdOrdenInfiString())) {
								// System.out.println("fValorMap GET ----" + fValorMap.get(cruce.getIdTitulo()+"|"+cruce.getIdOrdenInfiString()));
								// System.out.println("FECHA VALOR ----" + cruce.getFechaValor());
								if (!fValorMap.get(cruce.getIdTitulo() + "|" + cruce.getIdOrdenInfiString()).equalsIgnoreCase(cruce.getFechaValor())) {

									fValRep = true;
								}
							} else {
								fValorMap.put(cruce.getIdTitulo() + "|" + cruce.getIdOrdenInfiString(), cruce.getFechaValor());
							}
							// NM26659 Cambio para mejora de Performace de procesos
							// System.out.println("FLAG VERIFICAR 9");
							if (_resgitrosCruces.count() > 0) {
								_resgitrosCruces.first();
								_resgitrosCruces.next();
								if (_resgitrosCruces.getValue("TITULO_ID") != null && !_resgitrosCruces.getValue("TITULO_ID").equals("")) {
									if (_resgitrosCruces.getValue("TITULO_ID").equalsIgnoreCase(cruce.getIdTitulo()) && _resgitrosCruces.getValue("ESTATUS").equalsIgnoreCase(StatusOrden.CRUZADA)) {
										Logger.debug(this, "Fecha Valor Titulo BD: " + _resgitrosCruces.getValue("fecha_valor"));
										// System.out.println("Fecha Valor Titulo BD: "+_resgitrosCruces.getValue("fecha_valor"));
										if (com.bdv.infi.util.Utilitario.StringToDate(_resgitrosCruces.getValue("fecha_valor"), ConstantesGenerales.FORMATO_FECHA2).compareTo(com.bdv.infi.util.Utilitario.StringToDate(cruce.getFechaValor(), ConstantesGenerales.FORMATO_FECHA2)) != 0) {
											fValRep = true;
										}
									}
								}
							}

							if (fValRep) {

								// System.out.println("Fecha INVALIDA");
								validCampos = false;
								descripcionError += "Fila " + nroFila + " - ";
								if (codOrdenInfiCell != null)
									descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
								descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_FECHA_VALOR + "': El valor no se corresponde con el seteado para un cruce de la misma orden (" + cruce.getFechaValor() + ") cargado previamente | ";
								Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_FECHA_VALOR + "': El valor no se corresponde con el seteado para un cruce de la misma orden (" + cruce.getFechaValor() + ") cargado previamente");
							}

						} else {
							validCampos = false;
							descripcionError += "Fila " + nroFila + " - ";
							if (codOrdenInfiCell != null)
								descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
							descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_FECHA_VALOR + "': Verifique que el formato sea DD/MM/YYYY | ";
							Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_FECHA_VALOR + "': Verifique que el formato sea DD/MM/YYYY");
						}
					} catch (Exception e) {
						validCampos = false;
						e.getStackTrace();
						descripcionError += "Fila " + nroFila + " - ";
						if (codOrdenInfiCell != null)
							descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
						descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_FECHA_VALOR + "': Formato invalido excepcion| " + e.getMessage() + e.getStackTrace();
						Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_FECHA_VALOR + "': Formato invalido");
					}
					Logger.debug(this, "PASA FECHA VALOR");
					// VALIDACION DE CAMPO VALOR EFECTIVO TITULO
					// try{
					// cruce.setValorEfectivo(new BigDecimal(Double.parseDouble(valorEfectivoTitCell.toString().replaceAll("\\.0", ""))).setScale(2, BigDecimal.ROUND_HALF_UP));
					// Logger.debug(this,"Se setea el Valor Efectivo del Titulo: "+cruce.getValorEfectivo());
					//					
					// //VALIDAR QUE EL VALOR EFECTIVO SEA IGUAL AL MONTO CRUCE
					// compare = cruce.getValorEfectivo().compareTo(cruce.getMontoOperacion());
					// if(compare!=0){ //Valor efectivo no coincide con el monto cruce
					// validCampos = false;
					// descripcionError += "Fila "+nroFila+" - ";
					// if(codOrdenInfiCell!=null) descripcionError += "Orden "+codOrdenInfiCell.toString().replaceAll("\\.0", "")+" - ";
					// descripcionError += "Campo '"+ConstantesGenerales.CAMPO_PLANTILLA_VALOR_EFECTIVO_TIT+"': El valor ingresado ("+valorEfectivoTitCell.toString()+") debe coincidir con el monto del cruce ("+mtoCruceCell.toString()+") | ";
					// Logger.info(this,"Error al procesar archivo de cruces '"+getNombreDocumento()+"' en la Fila "+nroFila+" - Campo '"+ConstantesGenerales.CAMPO_PLANTILLA_VALOR_EFECTIVO_TIT+"': El valor ingresado ("+valorEfectivoTitCell.toString()+") debe coincidir con el monto del cruce ("+mtoCruceCell.toString()+")");
					// }
					// }catch(NumberFormatException ex){
					// validCampos = false;
					// descripcionError += "Fila "+nroFila+" - ";
					// if(codOrdenInfiCell!=null) descripcionError += "Orden "+codOrdenInfiCell.toString().replaceAll("\\.0", "")+" - ";
					// descripcionError += "Campo '"+ConstantesGenerales.CAMPO_PLANTILLA_VALOR_EFECTIVO_TIT+"': El valor ingresado no es de tipo numerico ("+valorEfectivoTitCell.toString()+") | ";
					// Logger.info(this,"Error al procesar archivo de cruces '"+getNombreDocumento()+"' en la Fila "+nroFila+" - Campo '"+ConstantesGenerales.CAMPO_PLANTILLA_VALOR_EFECTIVO_TIT+"': El valor ingresado no es de tipo numerico ("+valorEfectivoTitCell.toString()+")");
					// }

					// CALCULO DE LOS INTERESES CAIDOS DEL TITULO
					if (cruce.getIdTitulo() != null && !cruce.getIdTitulo().equals("") && orden.getIdOrden() != 0) {
						try {
							cruce.setMtoInteresesCaidosTitulo(com.bdv.infi_toma_orden.util.Utilitario.calcularInteresesCaidos(cruce.getMontoOperacion(), cruce.getIdTitulo(), cruce.getFechaValorGuion(), dataSource.toString(), dataSource, beanTitulo).setScale(2, BigDecimal.ROUND_HALF_EVEN));
						} catch (Throwable e) {
							validCampos = false;
							descripcionError += "Fila " + nroFila + ": Excepcion ocurrida al Calcular los intereses Caidos |" + e.getMessage();
							Logger.error(this, "Fila " + nroFila + ": Excepcion ocurrida al Calcular los intereses Caidos : " + e.getMessage());
						}
						Logger.debug(this, "INTERESES CAIDOS TITULO CALCULADOS----------" + cruce.getMtoInteresesCaidosTitulo());
					} else {
						validCampos = false;
						descripcionError += "Fila " + nroFila + " - ";
						if (codOrdenInfiCell != null)
							descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
						descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_ISIN + "': No se pudieron calcular los intereses caidos para el titulo de ISIN (" + isinCell.toString() + ") | ";
						Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_ISIN + "': No se pudieron calcular los intereses caidos para el titulo de ISIN (" + isinCell.toString() + ")");
					}
					Logger.debug(this, "PASA CALCULO INTERESES CAIDOS");
					// System.out.println("cruce.getIndicadorTitulo(): "+cruce.getIndicadorTitulo());

					if (cruce.getIncrementalTitulo() > 0) {
						// *****Calculo de Valor Nominal Adjudicado
						cruce.setValorNominal(calculoValorNominalSICAD2(cruce, beanTitulo).doubleValue());
						// System.out.println(" *************** CONTRAVALOR BOLIVARES *********** " + calculoContravalorBolivaresSICAD2(cruce));
						// *****Calculo de Contravalor en Bolivares por CAPITAL
						cruce.setContravalorBolivaresCapital(calculoContravalorBolivaresSICAD2(cruce));
					}

					if (cruce.getValorNominal() == 0) {
						validCampos = false;
						descripcionError += "Fila " + nroFila + " - : El titulo asignado al cliente posee un monto nominal inferior al valor incremental del titulo";
						Logger.info(this, "Fila " + nroFila + " - : El titulo asignado al cliente posee un monto nominal inferior al valor incremental del titulo");
					}

					// CALCULO DEL VALOR NOMINAL DEL TITULO //TODO REASIGNACION DE VALOR NOMINAL
					/*
					 * if(cruce.getMtoInteresesCaidosTitulo()!=null && orden.getIdOrden()!=0){ //TODO VERIFICAR CALCULO DE VALOR NOMINAL cruce.setValorNominal((cruce.getMontoOperacion().subtract(cruce.getMtoInteresesCaidosTitulo())).setScale(2,BigDecimal.ROUND_HALF_EVEN).doubleValue()); }
					 */

					Logger.debug(this, "PASA CALCULO VALOR NOMINAL");

				}// Si es cruce de un TITULO
				else { // Si es cruce de un DIVISA
					BigDecimal montoContravalorBolivaresCapital = new BigDecimal(0);
					montoContravalorBolivaresCapital = cruce.getMontoOperacion().multiply(cruce.getTasa()).setScale(2, BigDecimal.ROUND_HALF_EVEN);
					cruce.setContravalorBolivaresCapital(montoContravalorBolivaresCapital);

					// VALIDACION DE CAMPO FECHA VALOR
					try {
						boolean fValRep = false;
						String dia = "", mes = "", anio = "";
						m = patFecha.matcher(fechaValorCell.toString());

						if (m.find()) { // Si el registro coincide con el formato correspondiente
							dia = fechaValorCell.toString().substring(0, 2);
							mes = fechaValorCell.toString().substring(3, 5);
							anio = fechaValorCell.toString().substring(6, 10);
							// Setea la fecha valor a la orden aprobacion
							cruce.setFechaValor(dia + "/" + mes + "/" + anio);
							cruce.setFechaValorGuion(dia + "-" + mes + "-" + anio);

							// System.out.println("idsTitulosOrdenes ----" + fValorMap.toString());
							// System.out.println("idsTitulosOrdenes GET ----" + fValorMap.get(cruce.getIndicadorTitulo()+"|"+cruce.getIdOrdenInfiString()));
							// System.out.println("FECHA VALOR ----" + cruce.getFechaValor());

							// VALIDACION FECHA VALOR IGUAL PARA CRUCES DEL MISMO TITULO y ORDEN (LOTE A CARGAR)
							// String fechaValor = cruce.getFechaValor();
							// if(fValorMap.containsKey(cruce.getIndicadorTitulo()+"|"+cruce.getIdOrdenInfiString())){
							if (fValorMap.containsKey("D" + "|" + cruce.getIdOrdenInfiString())) {
								// System.out.println("fValorMap.containsKey 1----" + fValorMap.toString());
								// System.out.println("fValorMap GET ----" + fValorMap.get(cruce.getIdTitulo()+"|"+cruce.getIdOrdenInfiString()));
								// System.out.println("FECHA VALOR ----" + cruce.getFechaValor());
								if (!fValorMap.get("D" + "|" + cruce.getIdOrdenInfiString()).equalsIgnoreCase(cruce.getFechaValor())) {
									// System.out.println("fValRep True----" + fValorMap.toString());
									fValRep = true;
								}
							} else {
								fValorMap.put("D" + "|" + cruce.getIdOrdenInfiString(), cruce.getFechaValor());
							}

							// NM26659 - 05/09/2014 _ Cambio para la optimizacion de procesos de Carga y Cierre de Cruce
							if (_resgitrosCruces.count() > 0) {
								_resgitrosCruces.first();
								_resgitrosCruces.next();

								if (com.bdv.infi.util.Utilitario.StringToDate(_resgitrosCruces.getValue("fecha_valor"), ConstantesGenerales.FORMATO_FECHA2).compareTo(com.bdv.infi.util.Utilitario.StringToDate(cruce.getFechaValor(), ConstantesGenerales.FORMATO_FECHA2)) != 0) {
									fValRep = true;
								}
							}

							if (fValRep) {
								// System.out.println("Fecha INVALIDA");
								validCampos = false;
								descripcionError += "Fila " + nroFila + " - ";
								if (codOrdenInfiCell != null)
									descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
								descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_FECHA_VALOR + "': El valor no se corresponde con el seteado para un cruce de la misma orden (" + cruce.getFechaValor() + ") cargado previamente | ";
								Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_FECHA_VALOR + "': El valor no se corresponde con el seteado para un cruce de la misma orden (" + cruce.getFechaValor() + ") cargado previamente");
							}

						} else {

							validCampos = false;
							descripcionError += "Fila " + nroFila + " - ";
							if (codOrdenInfiCell != null)
								descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
							descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_FECHA_VALOR + "': Verifique que el formato sea DD/MM/YYYY | ";
							Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_FECHA_VALOR + "': Verifique que el formato sea DD/MM/YYYY");
						}
					} catch (Exception e) {
						validCampos = false;
						e.getStackTrace();
						descripcionError += "Fila " + nroFila + " - ";
						if (codOrdenInfiCell != null)
							descripcionError += "Orden " + codOrdenInfiCell.toString().replaceAll("\\.0", "") + " - ";
						descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_FECHA_VALOR + "': Formato invalido excepcion| " + e.getMessage() + e.getStackTrace();
						Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_FECHA_VALOR + "': Formato invalido");
					}
					Logger.debug(this, "PASA FECHA VALOR");

				}

			}// PLANTILLA CRUCE

				
				if (identificadorPlantilla.equals(ConstantesGenerales.TITULO_TEMPLATE_CRUCE) && orden.getStatusVerificacionBCV().equalsIgnoreCase(ConstantesGenerales.VERIFICADA_RECHAZADA)) {
					validCampos = false;
					descripcionError +=  ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN + "': La orden fue verificada como Rechazada por BCV y se esta cargando como CRUZADA en la aplicacion | ";
					Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN + "': La orden fue verificada como Rechazada por BCV y se esta cargando como CRUZADA en la aplicacion ");					
				}
				
				if (identificadorPlantilla.equals(ConstantesGenerales.TITULO_TEMPLATE_NO_CRUCE) && orden.getStatusVerificacionBCV().equalsIgnoreCase(ConstantesGenerales.VERIFICADA_APROBADA)) {
					validCampos = false;
					descripcionError += ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN + "': La orden fue verificada como APROBADA por BCV y se esta cargando como NO CRUZADA en la aplicacion | ";
					Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN + "': La orden fue verificada como APROBADA por BCV y se esta cargando como NO CRUZADA en la aplicacion ");					
				}
				
		} catch (Exception ex) {
			validCampos = false;
			descripcionError += "Fila " + nroFila + ": Excepcion ocurrida al validar los campos | ";
			System.out.println("Fila " + nroFila + ": Excepcion ocurrida al validar los campos: " + ex.getMessage());
			Logger.error(this, "Fila " + nroFila + ": Excepcion ocurrida al validar los campos: " + ex.getMessage());
		}

		return validCampos;
	}

	/**
	 * Crea un nuevo proceso del tipo de transaccion especificada
	 * 
	 * @param proceso
	 * @return boolean indicando si se inserto el proceso de carga de cruces
	 * @throws Exception
	 */
	public boolean insertarProcesoCargaCruces(Proceso proceso) throws Exception {

		ProcesosDAO procesoDAO = new ProcesosDAO(dataSource);
		ArrayList<String> querysEjecutar = new ArrayList<String>();

		// Se crea el query de insercion del proceso
		querysEjecutar.add(procesoDAO.insertar(proceso));

		boolean insertado = procesoDAO.ejecutarStatementsBatchBool(querysEjecutar);

		if (insertado) {
			Logger.info(this, "Comenzó proceso: " + proceso.getTransaId());
		} else {
			Logger.error(this, "Error al crear el proceso de tipo: " + proceso.getTransaId());
		}
		return insertado;

	}

	/**
	 * Registra el inicio de un proceso en la tabla 807
	 * 
	 * @return
	 * @throws Exception
	 */
	protected boolean comenzarProceso() throws Exception {
		procesoDAO.listarPorTransaccionActiva(proceso.getTransaId());
		if (procesoDAO.getDataSet().count() > 0) {
			Logger.info(this, "Proceso: " + proceso.getTransaId() + " ya esta en ejecución.");
			proceso.setDescripcionError(proceso.getDescripcionError() == null ? "" : proceso.getDescripcionError() + " | Proceso: " + proceso.getTransaId() + " ya esta en ejecución.");
			setMensajeError(getMensajeError() + "Existe un proceso de carga de cruces en ejecuci&oacute;n, debe esperar a que el mismo finalice<br/>");
			return false;
		}
		try {
			// Logger.debug(this, "-----NO ENCUENTRA PROCESO EN EJECUCION");
			ArrayList<String> querysEjecutar = new ArrayList<String>();
			// Se crea el query de insercion del proceso
			querysEjecutar.add(procesoDAO.insertar(proceso));
			boolean insertado = procesoDAO.ejecutarStatementsBatchBool(querysEjecutar);
			if (insertado) {
				Logger.info(this, "Comenzó proceso: " + proceso.getTransaId());
			} else {
				Logger.error(this, "Error al crear el proceso de tipo: " + proceso.getTransaId());
				proceso.setDescripcionError(proceso.getDescripcionError() == null ? "" : proceso.getDescripcionError() + " | Error al crear el proceso de tipo: " + proceso.getTransaId());
				setMensajeError(getMensajeError() + "Ocurri&oacute; un error al intentar iniciar el proceso de cruce de &oacute;rdenes<br/>");
				return false;
			}
		} catch (Exception e) {
			Logger.error(this, "Error al crear el proceso de tipo: " + proceso.getTransaId());
			proceso.setDescripcionError(proceso.getDescripcionError() == null ? "" : proceso.getDescripcionError() + " | Error al crear el proceso de tipo: " + proceso.getTransaId());
			setMensajeError(getMensajeError() + "Ocurri&oacute; un error al intentar iniciar el proceso de cruce de &oacute;rdenes<br/>");
			return false;
		}
		return true;
	}

	/**
	 * Registra el fin de un proceso en la tabla 807
	 */
	protected void terminarProceso() {
		try {
			if (proceso != null) {
				proceso.setFechaFin(new Date());
				if (proceso.getDescripcionError() == null) {
					proceso.setDescripcionError("");
				}
				proceso.setDescripcionError(proceso.getDescripcionError() == null ? "" : proceso.getDescripcionError() + errorProceso);
				db.exec(this.dataSource, procesoDAO.modificar(proceso));
			}
			if (procesoDAO != null) {
				procesoDAO.cerrarConexion();
			}
		} catch (Exception e) {
			Logger.error(this, "Excepcion ocurrida al cerrar el proceso de tipo " + proceso.getTransaId() + ": " + e.getMessage());
		} finally {
			if (proceso != null) {
				if (proceso.getFechaInicio() != null && proceso.getFechaFin() != null) {
					final long duracion = proceso.getFechaFin().getTime() - proceso.getFechaInicio().getTime();
					Logger.info(this, "Termino proceso: " + proceso.getTransaId() + ", duracion: " + (duracion / 1000) + " secs.");
				}
			}
		}
	}

	/**
	 * Registra la auditoria del proceso de carga
	 * 
	 * @throws Exception
	 * @throws Exception
	 */
	private void registrarAuditoria() {
		com.bdv.infi.dao.Transaccion transaccion = new com.bdv.infi.dao.Transaccion(dataSource);
		ArrayList<String> querys = new ArrayList<String>();
		AuditoriaDAO auditoriaDAO = new AuditoriaDAO(dataSource);

		Logger.info(this, "Registrando auditoria del proceso de carrga de cruces...");
		try {
			// /********REGISTRAR LA AUDITORIA DE LA PETICIÓN DE LLAMADA AL REPROCESO DEL CIERRE DEL SISTEMA****///
			// Configuracion del objeto para el proceso de auditoria
			Auditoria auditoria = new Auditoria();
			auditoria.setDireccionIp(ip);
			auditoria.setFechaAuditoria(Utilitario.DateToString(new Date(), ConstantesGenerales.FORMATO_FECHA));
			auditoria.setUsuario(nombreUsuario);
			auditoria.setPeticion(proceso.getTransaId());
			auditoria.setDetalle(proceso.getDescripcionError() == null ? "" : proceso.getDescripcionError());

			querys.add(auditoriaDAO.insertRegistroAuditoria(auditoria));
			auditoriaDAO.ejecutarStatementsBatch(querys);

		} catch (Exception ex) {
			try {
				transaccion.rollback();
			} catch (Exception e) {
				Logger.error(this, "Ha ocurrido un error registrando la auditoría del proceso de carga de cruces : " + ex.getMessage());
			}
		} finally {
			try {
				if (transaccion.getConnection() != null) {
					transaccion.getConnection().close();
				}
			} catch (Exception e) {
				Logger.error(this, "Ha ocurrido un error registrando la auditoría del proceso de carga de cruces : " + e.getMessage());
			}
		}
	}

	public String getContenidoDocumento() {
		return contenidoDocumento;
	}

	public void setContenidoDocumento(String contenidoDocumento) {
		this.contenidoDocumento = contenidoDocumento;
	}

	public String getNombreDocumento() {
		return nombreDocumento;
	}

	public void setNombreDocumento(String nombreDocumento) {
		this.nombreDocumento = nombreDocumento;
	}

	public String getIdentificadorPlantilla() {
		return identificadorPlantilla;
	}

	public void setIdentificadorPlantilla(String identificadorPlantilla) {
		this.identificadorPlantilla = identificadorPlantilla;
	}

	private BigDecimal calculoValorNominalSICAD2(OrdenesCruce cruce, OrdenTitulo ordenTitulo) throws Exception {
		// System.out.println("************* calculoValorNominalSICAD2 *************");
		int precisionDigitosDecimales = 13;
		BigDecimal tasaCupon = new BigDecimal(0);
		BigDecimal poctCupon = new BigDecimal(0);
		BigDecimal diasDevengados = new BigDecimal(0);

		BigDecimal factorCalculo = new BigDecimal(0);
		BigDecimal precionSucio = new BigDecimal(0);
		BigDecimal pctPrecionSucio = new BigDecimal(0);
		BigDecimal valorNominalBruto = new BigDecimal(0);

		BigDecimal valorNominalAdj = new BigDecimal(0);
		ArrayList retornoValores = titulosOrdenDAO.listarFechaCupon(cruce.getIdTitulo(), cruce.getFechaValorGuion());
		if (!retornoValores.isEmpty()) {

			dateCupon = formatter.parse(retornoValores.get(0).toString());
			dateValor = formatter.parse(cruce.getFechaValorGuion());

			tasaCupon = new BigDecimal(retornoValores.get(1).toString());
			// System.out.println("Tasa Cupon -------> " + tasaCupon);
			// poctCupon=tasaCupon.divide(new BigDecimal(100));
			// System.out.println("Fecha Cupon ------> " + dateCupon);
			// System.out.println("Fecha Valor ------> " + dateValor);
			// Dias Devengados = Dias transcurridos desde inicio de Cupon vigente - fecha Valor
			diasDevengados = new BigDecimal(ordenTitulo.getDiferenciaDias());
			// new BigDecimal(com.bdv.infi_toma_orden.util.Utilitario.caculadorDiferenciaDiasDeFechas(dateCupon,dateValor));
			// System.out.println("Dias Devengados ----------> " + diasDevengados);
			// Factor de Calculo = (Dias Devengados + Cupon)
			// factorCalculo=diasDevengados.divide(new BigDecimal(360),precisionDigitosDecimales,RoundingMode.HALF_UP).multiply(tasaCupon).setScale(precisionDigitosDecimales,BigDecimal.ROUND_HALF_EVEN);
			factorCalculo = ordenTitulo.getFactorCalculo().multiply(tasaCupon).setScale(precisionDigitosDecimales, BigDecimal.ROUND_HALF_EVEN);
			// System.out.println("FACTOR CALCULO ----------> " + factorCalculo);
			// Precion Sucion = (Precio BCV + Factor Calculo)
			precionSucio = cruce.getPrecioTitulo().add(factorCalculo);
			cruce.setPrecioSucio(precionSucio.doubleValue());

			pctPrecionSucio = precionSucio.divide(new BigDecimal(100)).setScale(precisionDigitosDecimales, BigDecimal.ROUND_HALF_EVEN);
			// System.out.println("PRECIO SUCIO --------------> " + pctPrecionSucio);
			// Nominal Bruto = Valor Efectivo / precio sucio %
			valorNominalBruto = cruce.getMontoOperacion().divide(pctPrecionSucio, precisionDigitosDecimales, RoundingMode.HALF_UP).setScale(2, BigDecimal.ROUND_HALF_EVEN);
			// System.out.println("Valor nominal BRUTO ----> " + valorNominalBruto);
			// Valor Nominal Adjudicado
			// System.out.println("Valor Incremental ---> " + cruce.getIncrementalTitulo());
			long enteroIncremental = valorNominalBruto.divide(new BigDecimal(cruce.getIncrementalTitulo())).longValue();
			// System.out.println("Entero Incremental ---> " + enteroIncremental);
			valorNominalAdj = new BigDecimal(enteroIncremental).multiply(new BigDecimal(cruce.getIncrementalTitulo()));
			// System.out.println("VALOR NOMINAL NETO ------> " + valorNominalAdj);
		}
		return valorNominalAdj;
	}

	private BigDecimal calculoContravalorBolivaresSICAD2(OrdenesCruce cruce) throws Exception {
		// System.out.println("************* calculoContravalorBolivaresSICAD2 *************");
		BigDecimal contravalorBolivares = new BigDecimal(0);
		// Valor Nominal Neto (O Valor Nominal Adj) * Precio Sucio * Tasa Cambio
		// System.out.println("Valor Nominal ---> " + cruce.getValorNominal());
		// System.out.println("Precio Sucio ---> " + cruce.getPrecioSucio());
		// System.out.println("Tasa Cambio ---> " + cruce.getTasa());
		BigDecimal precioSucion = new BigDecimal(0);
		precioSucion = new BigDecimal(cruce.getPrecioSucio()).divide(new BigDecimal(100));
		// contravalorBolivares=new BigDecimal(cruce.getValorNominal()).multiply(new BigDecimal(cruce.getPrecioSucio())).multiply(cruce.getTasa());
		contravalorBolivares = new BigDecimal(cruce.getValorNominal()).multiply(precioSucion).multiply(cruce.getTasa());
		contravalorBolivares = contravalorBolivares.setScale(2, BigDecimal.ROUND_HALF_EVEN);
		// System.out.println("contravalorBolivares ---> " + contravalorBolivares);
		return contravalorBolivares;
	}

	public String cientificaToString(HSSFCell celda) throws Exception {
		String valorCelda = null;
		try {
			double cellDouble = Double.parseDouble(celda.toString());
			Long cellLong = (long) cellDouble;
			valorCelda = cellLong.toString();
			return valorCelda;
		} catch (NumberFormatException e) {
			return celda.toString();
		}
	}

	// NM26659 TTS_466 (Inclusion de DAO para la validacion de Documentos asociados a la Unidad de Inversion)
	// NM26659_Modificado_26/01/2015 Correcion indicencia ITS-2449-PRODUCCION
	public void cargarDocumentosUnidadInv() throws Exception {

		DataSet documentos = null;
		documentoDefinicionDAO = new DocumentoDefinicionDAO(dataSource);
		documentoDefinicionDAO.validacionDocumentos(Long.parseLong(getIdUiFiltered()), TransaccionNegocio.ADJUDICACION);
		documentos = documentoDefinicionDAO.getDataSet();
		if (documentos.count() > 0) {
			documentos.first();
			while (documentos.next()) {
				documentosTipPersona.add(documentos.getValue("tipper_id").concat(",").concat(documentos.getValue("BLOTER_ID")));
			}
		} else {
			throw new Exception(" La unidad de inversion a procesar no contiene documentos asociados ");
		}
	}

	public Proceso getProceso() {
		return proceso;
	}

	public void setProceso(Proceso proceso) {
		this.proceso = proceso;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getMensajeError() {
		return mensajeError;
	}

	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}

	public String getIdUiFiltered() {
		return idUiFiltered;
	}

	public void setIdUiFiltered(String idUiFiltered) {
		this.idUiFiltered = idUiFiltered;
	}

	public String getNameUiFiltered() {
		return nameUiFiltered;
	}

	public void setNameUiFiltered(String nameUiFiltered) {
		this.nameUiFiltered = nameUiFiltered;
	}

	public int getContadorValid() {
		return contadorValid;
	}

	public void setContadorValid(int contadorValid) {
		this.contadorValid = contadorValid;
	}

	public Hashtable<String, String> getOrdenesToUpdate() {
		return ordenesToUpdate;
	}

	public void setOrdenesToUpdate(Hashtable<String, String> ordenesToUpdate) {
		this.ordenesToUpdate = ordenesToUpdate;
	}

	public String getParametroValidacionBCV() {
		return parametroValidacionBCV;
	}

	public void setParametroValidacionBCV(String parametroValidacionBCV) {
		this.parametroValidacionBCV = parametroValidacionBCV;
	}

	public boolean isValidacionBcvEnLinea() {
		return validacionBcvEnLinea;
	}

	public void setValidacionBcvEnLinea(boolean validacionBcvEnLinea) {
		this.validacionBcvEnLinea = validacionBcvEnLinea;
	}



	public int getContadorInvalid() {
		return contadorInvalid;
	}

	public void setContadorInvalid(int contadorInvalid) {
		this.contadorInvalid = contadorInvalid;
	}

	public boolean isValidacionUnidadMenudeo() {
		return validacionUnidadMenudeo;
	}

	public void setValidacionUnidadMenudeo(boolean validacionUnidadMenudeo) {
		this.validacionUnidadMenudeo = validacionUnidadMenudeo;
	}

}

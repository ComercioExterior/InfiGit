package com.bdv.infi.logic.cruces_ordenes;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
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
import com.bdv.infi.dao.OfertaDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.OrdenesCrucesDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.SolicitudesSitmeDAO;
import com.bdv.infi.data.Auditoria;
import com.bdv.infi.data.Orden;
import com.bdv.infi.data.OrdenOferta;
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
/**
 * @author NM26659
 *
 */
public class BeanLogicCargaCrucesMenudeo {
	
	private String idUiFiltered;
	private String nameUiFiltered;
	private String contenidoDocumento;
	private String nombreDocumento;
	private String identificadorPlantilla;

	private OrdenesCruce cruce;
	private Orden orden = new Orden();
	private OrdenOferta ordenOferta=new OrdenOferta();
	private String ip;
	private String nombreUsuario;

	private String parametroValidacionBCV;
	private boolean validacionBcvEnLinea = false;
	private boolean validacionUnidadMenudeo = false;
	private boolean plantillaCruce=false;
	//DAOS
	private OrdenDAO ordenDAO;
	private ClienteDAO clienteDAO;
	private ProcesosDAO procesoDAO;
	private OrdenesCrucesDAO ordenesCrucesDAO;
	private SolicitudesSitmeDAO solicitudesSitmeDAO;
	private DataSource dataSource;
	
	private OfertaDAO ofertaDAO;

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
	Hashtable<String, BigDecimal> ordenesCrucesOfertas = new Hashtable<String, BigDecimal>();
	Hashtable<String, String> idBcvOrdenesCruces = new Hashtable<String, String>();
	Hashtable<String, String> nrosOperacion = new Hashtable<String, String>();
	HashMap<String, String> nrosCotizacion = new HashMap<String, String>();
	Hashtable<String, String> fValorMap = new Hashtable<String, String>();
	private Hashtable<String, String> ordenesToUpdate = new Hashtable<String, String>();	
	Hashtable<String, String> idsTitulosOrdenes = new Hashtable<String, String>();
	ArrayList<String> documentosTipPersona = new ArrayList<String>();

	// ******** Bloque de atributos pertenecientes archivo excel de carga *******/
	HSSFCell uiCell = null;// columna Unidad de Inversion
	HSSFCell cedulaCell = null;// columna cedula
	HSSFCell codOrdenDemandaCell = null;// columna codigo de orden de la demanda			
	HSSFCell codOrdenBcvDemandaCell = null;// columna codigo de orden BCV para la demanda
	HSSFCell codOrdenOfertaCell = null;// columna codigo de orden de Oferta
	HSSFCell codOrdenBcvOfertaCell = null;// columna codigo de orden BCV de Oferta		
	
	HSSFCell mtoSolicitadoCell = null;// columna monto solicitado por el demandante
	HSSFCell tasaCell = null;// columna que indica la tasa de cambio propuesta por el demandante
	HSSFCell contraparteCell = null;// columna contraparte
	HSSFCell mtoCruceCell = null;// columna monto cruce
	HSSFCell fechaValorCell = null;// columna fecha valor


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
		ofertaDAO= new OfertaDAO(dataSource);
		ordenDAO = new OrdenDAO(dataSource);
		clienteDAO = new ClienteDAO(dataSource);
		procesoDAO = new ProcesosDAO(dataSource);
		ordenesCrucesDAO = new OrdenesCrucesDAO(dataSource);
		solicitudesSitmeDAO = new SolicitudesSitmeDAO(dataSource);
		titulosOrdenDAO = new com.bdv.infi_toma_orden.dao.TitulosDAO(dataSource.toString(), dataSource);// dataSource);		
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
								if(cedulaCell!=null){
									cruce.setCiRif(cedulaCell.toString());
								}
								codOrdenDemandaCell = row.getCell((short) 2);								
								
								if (codOrdenDemandaCell != null && !codOrdenDemandaCell.toString().equals("")) {
									cruce.setIdOrdenInfiString(codOrdenDemandaCell.toString().replaceAll("\\.0", ""));
								}
																		
								if (identificadorPlantilla.equals(ConstantesGenerales.TITULO_TEMPLATE_CRUCE_SIMADI_MENUDEO)) {
									cruce.setEstatus(ConstantesGenerales.STATUS_CRUZADA);
									
									plantillaCruce=true;
									
									codOrdenBcvDemandaCell = row.getCell((short) 3);																																			
									if(codOrdenBcvDemandaCell!=null && codOrdenBcvDemandaCell.toString().length()>0) {											
										cruce.setNroBCVOrdenDemanda(codOrdenBcvDemandaCell.toString());										
									} else {											
										cruce.setNroBCVOrdenDemanda("");										
									}											
																				
									//ID DE ORDEN DE OFERTA											
									/*codOrdenOfertaCell = row.getCell((short) 4);										
									if(codOrdenOfertaCell!= null && !codOrdenOfertaCell.toString().equals("")){											
										cruce.setIdOrdenOfertaString(codOrdenOfertaCell.toString());										
									}else {
										cruce.setIdOrdenOfertaString("");
									}
																			
									//ID BCV PARA LA OFERTA  										
									codOrdenBcvOfertaCell = row.getCell((short) 5);										
									if(codOrdenBcvOfertaCell!= null && !codOrdenBcvOfertaCell.toString().equals("")){											
										cruce.setNroBCVOrdenOferta(codOrdenBcvOfertaCell.toString());										
									}else {
										cruce.setNroBCVOrdenOferta("");
									}*/
									
									mtoSolicitadoCell = row.getCell((short) 4);
									if(mtoSolicitadoCell!=null){
										cruce.setMontoSolicitadoString(mtoSolicitadoCell.toString());
									}

									tasaCell = row.getCell((short) 5);
									if(tasaCell!=null){
										cruce.setTasaString(tasaCell.toString());
									}

									contraparteCell = row.getCell((short) 6);
									if(contraparteCell!=null){
										cruce.setContraparte(contraparteCell.toString());
									}

									mtoCruceCell = row.getCell((short) 7);
									if(mtoCruceCell!=null){
										cruce.setMontoOperacionString(mtoCruceCell.toString());
									}
									
									fechaValorCell = row.getCell((short) 8);
									if(fechaValorCell!=null){
										cruce.setFechaValor(fechaValorCell.toString());
									}
								} else if (identificadorPlantilla.equals(ConstantesGenerales.TITULO_TEMPLATE_NO_CRUCE_SIMADI_MENUDEO)) {
									cruce.setEstatus(ConstantesGenerales.STATUS_NO_CRUZADA);
									
									mtoSolicitadoCell = row.getCell((short) 3);	
									if(mtoSolicitadoCell!=null){									
										cruce.setMontoSolicitadoString(mtoSolicitadoCell.toString());
									}
									
									tasaCell = row.getCell((short) 4);									
									if(tasaCell!=null){
										cruce.setTasaString(tasaCell.toString());
									}
									
									mtoCruceCell = row.getCell((short) 5);
									if(mtoCruceCell!=null){
										cruce.setMontoOperacionString(mtoCruceCell.toString());
									}

								}
								//Id de Orden BCV para la demanda										

								Logger.debug(this, "COD ORDEN INFI CELL------" + codOrdenDemandaCell.toString());
								Logger.debug(this, "CEDULA CELL------" + cedulaCell.toString());
								Logger.debug(this, "CRUCE COD ORDEN INFI------" + cruce.getIdOrdenInfiString());
								Logger.debug(this, "CRUCE CEDULA------" + cruce.getCiRif());
							
								try {
									//ITS-2912_02/12/2015 - NM26659 La seccion de codigo se valida en la seccion superior
									/*if (identificadorPlantilla.equals(ConstantesGenerales.TITULO_TEMPLATE_CRUCE_SIMADI_MENUDEO)) {
										plantillaCruce=true;
									} */
									
									// SE VALIDAN LOS DATOS DE ORDEN A CANCELAR
									if (validarCamposCruce((fila + 1),plantillaCruce)) { // Todos los campos son validos

										Logger.info(this, "CAMPOS VALIDOS! Fila: " + (fila + 1));

										// Se incrementa el contador de cruces validos
										setContadorValid(getContadorValid() + 1);
										//ITS-2912_02/12/2015 - NM26659 La seccion de codigo se valida en la seccion superior
									/*	if (identificadorPlantilla.equals(ConstantesGenerales.TITULO_TEMPLATE_CRUCE_SIMADI_MENUDEO)) {
											cruce.setEstatus(ConstantesGenerales.STATUS_CRUZADA);
										} else {
											if (identificadorPlantilla.equals(ConstantesGenerales.TITULO_TEMPLATE_NO_CRUCE_SIMADI_MENUDEO)) {
												cruce.setEstatus(ConstantesGenerales.STATUS_NO_CRUZADA);
											}
										}*/

										// SE INSERTA ORDEN INFI A ACTUALIZAR STATUS A PROCESO_CRUCE
										if (!ordenesToUpdate.containsKey(cruce.getIdOrdenInfiString())) {
											ordenesToUpdate.put(cruce.getIdOrdenInfiString(), "");
											Logger.debug(this, "GET HASHTABLE ordenesToUpdate " + cruce.getIdOrdenInfiString() + ": " + ordenesToUpdate.get(cruce.getIdOrdenInfiString()));
										}

										// Si es un cruce se preserva el nro de operacion (unico por cruce)
										/*if (identificadorPlantilla.equals(ConstantesGenerales.TITULO_TEMPLATE_CRUCE_SIMADI_MENUDEO)) {
											nrosOperacion.put(cruce.getNroOperacionString(), "");
											nrosCotizacion.put(cruce.getIdOrdenInfiString(), cruce.getNroCotizacionString());
										}*/

										// Si no existe el idOrden como clave en el hashtable se agrega idOrden valido
										if (!ordenesCruces.containsKey(cruce.getIdOrdenInfiString())) {
											// SE PRESERVA EL MONTO CRUZADO PARA LA ORDEN
											if (identificadorPlantilla.equals(ConstantesGenerales.TITULO_TEMPLATE_CRUCE_SIMADI_MENUDEO)) {
												ordenesCruces.put(cruce.getIdOrdenInfiString(), cruce.getMontoOperacion());
											} else {
												// MONTO EN CERO INDICA NO CRUCE PARA LA ORDEN
												if (identificadorPlantilla.equals(ConstantesGenerales.TITULO_TEMPLATE_NO_CRUCE_SIMADI_MENUDEO)) {
													ordenesCruces.put(cruce.getIdOrdenInfiString(), new BigDecimal(0));
												}
											}
											Logger.debug(this, "GET HASHTABLE ordenesCruces " + cruce.getIdOrdenInfiString() + ": " + ordenesCruces.get(cruce.getIdOrdenInfiString()));
										} else { // Ya existe un cruce para la orden en el lote de cruces a cargar
											// Se suma al total cruzado en este lote el monto del cruce actual
											ordenesCruces.put(cruce.getIdOrdenInfiString(), ordenesCruces.get(cruce.getIdOrdenInfiString()).add(cruce.getMontoOperacion()));
											Logger.debug(this, "GET HASHTABLE ordenesCruces " + cruce.getIdOrdenInfiString() + ": " + ordenesCruces.get(cruce.getIdOrdenInfiString()));
										}
										
									 //SECCION COMENTADA POR QUE ACTULAMENTE EN MENUDEO NO SE PROCESA LA OFERTA
									/*	if(cruce.getIdOrdenOfertaString()!=null){
											if (!ordenesCrucesOfertas.containsKey(cruce.getIdOrdenOfertaString())) {
												// SE PRESERVA EL MONTO CRUZADO PARA LA ORDEN
												if (identificadorPlantilla.equals(ConstantesGenerales.TITULO_TEMPLATE_CRUCE_SIMADI_MENUDEO)) {
													ordenesCrucesOfertas.put(cruce.getIdOrdenOfertaString(), cruce.getMontoOperacion());
												} else {
													// MONTO EN CERO INDICA NO CRUCE PARA LA ORDEN
													if (identificadorPlantilla.equals(ConstantesGenerales.TITULO_TEMPLATE_NO_CRUCE_SIMADI_MENUDEO)) {
														ordenesCrucesOfertas.put(cruce.getIdOrdenOfertaString(), new BigDecimal(0));
													}
												}
												Logger.debug(this, "GET HASHTABLE ordenesCruces " + cruce.getIdOrdenInfiString() + ": " + ordenesCruces.get(cruce.getIdOrdenInfiString()));
											} else { // Ya existe un cruce para la orden en el lote de cruces a cargar
												// Se suma al total cruzado en este lote el monto del cruce actual
												ordenesCrucesOfertas.put(cruce.getIdOrdenOfertaString(), ordenesCrucesOfertas.get(cruce.getIdOrdenOfertaString()).add(cruce.getMontoOperacion()));
												Logger.debug(this, "GET HASHTABLE ordenesCrucesOfertas " + cruce.getIdOrdenOfertaString() + ": " + ordenesCrucesOfertas.get(cruce.getIdOrdenOfertaString()));
											}
										}*/
										// SE INSERTA CLAVE ID_TITULO+ID_ORDEN INDICANDO EL PRECIO DEL TITULO
										
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
									querysEjecutar.add(ordenesCrucesDAO.insertarCruce(cruce,true));
									
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
									proceso.setDescripcionError("Ocurrio una excepcion al validar los campos de la Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " e insertarla durante el proceso de carga de cruces " + proceso.getTransaId());
									setMensajeError("Ocurrio una excepcion al validar los campos de la Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " e insertarla en la tabla temporal durante el proceso de carga de cruces " + proceso.getTransaId());
									Logger.error(this, "Ocurrio una excepcion al validar los campos de la Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " e insertarla en la tabla temporal durante el proceso de carga de cruces " + proceso.getTransaId() + ": " + e.getMessage());
									setMensajeError("Ocurrio una excepcion al validar los campos de la Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " e insertarla en la tabla temporal durante el proceso de carga de cruces<br/>");
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
	public boolean validarCamposCruce(int nroFila,boolean plantillaCruce) throws ParseException, Exception {
		int contadorError=0;
		boolean validCampos = true;		
		//Pattern patRifCi = Pattern.compile("^[JjVvEeGgIi][0-9]{1,10}$");
		Pattern patAlfaNum = Pattern.compile("^[a-zA-ZáéíóúüÁÉÍÓÚÜ0-9 _\\-,.]+$");
		//Pattern patNum = Pattern.compile("^[0-9]*$");
		//Pattern patFecha = Pattern.compile("^((0[1-9])|([12][0-9])|(3[01]))[/\\-]((0[1-9])|(1[0-2]))[/\\-]([1-9][0-9][0-9][0-9])$");
		Matcher m; // Matcher que verifica que se cumpla con el patron
		
		BigDecimal sumCrucesDemanda = new BigDecimal(0); // Sumatoria de los cruces de la orden
		//BigDecimal sumCrucesOferta = new BigDecimal(0); // Sumatoria de los cruces de la orden
		descripcionError = "";		
		
		try {
				
			//Validacion Orden Demanda
			validCampos=validacionOrdenDemanda(nroFila,codOrdenDemandaCell,codOrdenBcvDemandaCell);
			if(!validCampos)++contadorError;
			
			//SECCION COMENTADA POR QUE ACTULAMENTE EN MENUDEO NO SE PROCESA LA OFERTA
			/*if(plantillaCruce){				
			//Validacion Orden Oferta	
			validCampos=validacionOrdenOferta(nroFila,codOrdenOfertaCell,codOrdenBcvOfertaCell);
			if(!validCampos)++contadorError;			
			}*/						
			
			//Validacion cliente Demandante
			validCampos=validacionClienteDemanda(nroFila,orden);
			if(!validCampos)++contadorError;			
			//Validacion Unidad de Inversion 
			validCampos=validacionUnidadInversion(nroFila,uiCell);
			if(!validCampos)++contadorError;			
			//Validacion Monto Solicitado
			validCampos=validacionMontoSolicitado(nroFila,mtoSolicitadoCell);
			if(!validCampos)++contadorError;			
			//Validacion Monto Cruce
			validCampos=validacionMontoCruce(nroFila,sumCrucesDemanda,cruce,mtoCruceCell);
			if(!validCampos)++contadorError;		
			//Validacion de Tasa			
			validCampos=validacionTasaDemanda(nroFila, tasaCell, cruce);
			if(!validCampos)++contadorError;	
			//Validacion de Cruces Demanda
			//NM25287/ - 13/05/2015 Correccion de incidencia (Permite Cargar Registros de Cruce y No Cruce asociado a una misma Orden)
			validCampos=validacionCruceDemanda(nroFila, sumCrucesDemanda, cruce);
			if(!validCampos)++contadorError;	
			
			if(plantillaCruce){				
				//Validacion Contraparte			
				validCampos=validacionContraparte(nroFila, contraparteCell, cruce);
				if(!validCampos)++contadorError;	

				//SECCION COMENTADA POR QUE ACTULAMENTE EN MENUDEO NO SE PROCESA LA OFERTA
				//Validacion de Cruces Oferta						
				/*validCampos=validacionCruceOferta(nroFila, sumCrucesOferta, cruce);
				if(!validCampos)++contadorError;*/	
				//Validacion Fecha Valor			
				validCampos=validacionFechaValor(nroFila,fechaValorCell);
				if(!validCampos)++contadorError;
			}					
		} catch (Exception ex) {
			validCampos = false;
			descripcionError += "Fila " + nroFila + ": Excepcion ocurrida al validar los campos | ";
			//System.out.println("Fila " + nroFila + ": Excepcion ocurrida al validar los campos: " + ex.getMessage());
			Logger.error(this, "Fila " + nroFila + ": Excepcion ocurrida al validar los campos: " + ex.getMessage());
		}
		if(contadorError>0){
			validCampos=false;
		}			
		return validCampos;
	}
	
	/**
	 * Motodo de Validacion de orden de Demanda
	 * @param nroFila
	 * @param orden
	 * @param codOrdenDemandaCell
	 * @param codOrdenBcvDemandaCell
	 * @return
	 * @throws Exception
	 * @author NM26659
	 */
	private boolean validacionOrdenDemanda(int nroFila,HSSFCell codOrdenDemandaCell,HSSFCell codOrdenBcvDemandaCell) throws Exception{
		boolean validCampos=true;
		
		boolean ordenExiste=false;
		boolean plantillaCruce=false;
		boolean plantillaNoCruce=false;
		
		if (identificadorPlantilla.equals(ConstantesGenerales.TITULO_TEMPLATE_CRUCE_SIMADI_MENUDEO)) {
			plantillaCruce=true;
		}else {
			plantillaNoCruce=false;
		}
		//VALIDACION SI EL ID ES NULO
		if (codOrdenDemandaCell.toString() == null){
			validCampos=false;
			//descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
			descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_DEMANDA + "': El campo Codigo de Orden no puede estar vacio | ";
			Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_DEMANDA + "': El campo Codigo de Orden no puede estar vacio");
		}
		
		//VALIDACION SI EL ID ES MAYOR A 10 DIGITOS				
		if (codOrdenDemandaCell.toString() != null && codOrdenDemandaCell.toString().length() > 10) { // SE VALIDA QUE NO EXCEDA EL TAM MAX DEL CAMPO EN BD
			// Se trunca el valor a 10 caracteres
			cruce.setIdOrdenInfiString(codOrdenDemandaCell.toString().substring(0, 10));
			validCampos = false;
			descripcionError += "Fila " + nroFila + " - ";
			if (codOrdenDemandaCell != null)
				descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
			descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_DEMANDA + "': Formato invalido: No debe exceder de 10 digitos | ";
			Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_DEMANDA + "': Formato invalido: No debe exceder de 10 digitos");
		}
		
		try {//VALIDACION SI EL ID DE TIPO NUMERICO
			cruce.setIdOrdenINFI(Long.parseLong(codOrdenDemandaCell.toString().replaceAll("\\.0", "")));
			Logger.debug(this, "Se setea el ID orden: " + cruce.getIdOrdenINFI());
		} catch (NumberFormatException ex) {
			validCampos = false;
			// if(codOrdenDemandaCell==null || codOrdenDemandaCell.toString().equals(""))cruce.setIdOrdenINFI((long)(-1)); //Se setea un valor para indicar que la orden era invalida
			cruce.setIdOrdenINFI((long) (-1)); // Se setea un valor para indicar que la orden era invalida
			descripcionError += "Fila " + nroFila + " - ";
			if (codOrdenDemandaCell != null)
				descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
			descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_DEMANDA + "': El valor ingresado no es de tipo numerico (" + codOrdenDemandaCell.toString() + ") | ";
			Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_DEMANDA + "': El valor ingresado no es de tipo numerico (" + codOrdenDemandaCell.toString() + ")");
			// throw new Exception("Orden "+codOrdenCell.toString().replaceAll("\\.0", "")+" - CAMPO INVALIDO: "+ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_DEMANDA);
		}
		if(cruce.getIdOrdenINFI()>0){		
			orden = ordenDAO.listarOrden(cruce.getIdOrdenINFI(), false, false, false, true, false);
			if (orden.getIdOrden() != 0) {	
				ordenExiste=true;
			}
		}
		

		if (ordenExiste) {	//SI LA ORDEN EXISTE EN BASE DE DATOS
			
			if (plantillaCruce) {	
				if(orden.getStatusVerificacionBCV().equalsIgnoreCase(ConstantesGenerales.VERIFICADA_APROBADA) || orden.getStatusVerificacionBCV().equalsIgnoreCase(ConstantesGenerales.VERIFICADA_RECHAZADA)){
					validCampos = false;				
					descripcionError += "Fila " + nroFila + " - ";			
					if (codOrdenDemandaCell != null) {											
						descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";													
						descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_DEMANDA + "': La orden ya ha sido verificada por BCV, no se puede cargar la orden como CRUZADA ";				
						Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_DEMANDA + "': La orden ya ha sido verificada por BCV, no se puede cargar la orden como CRUZADA ");							
					}														
				}
			}
					
				if (plantillaCruce) {													
					//if(orden.getStatusVerificacionBCV().equalsIgnoreCase(ConstantesGenerales.SIN_VERIFICAR)){						
						
					if(!validacionBcvEnLinea){//BCV FUERA DE LINEA
							if (cruce.getNroBCVOrdenDemanda() == null || cruce.getNroBCVOrdenDemanda().length() == 0) { // SE VALIDA QUE NO EXCEDA EL TAM MAX DEL CAMPO EN BD)
								validCampos = false;
								descripcionError += "Fila " + nroFila + " - ";
								if (codOrdenDemandaCell != null)
									descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
								descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_BCV_DEMANDA + "': El par&aacute;metro BCV en linea no se encuentra activo por lo que debe ingresar el numero de orden BCV asignado a la orden | ";
								Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_BCV_DEMANDA + "': El par&aacute;metro BCV en linea no se encuentra activo por lo que debe ingresar el numero de orden BCV asignado a la orden");
							} else if (cruce.getNroBCVOrdenDemanda() != null && cruce.getNroBCVOrdenDemanda().length() > 20) { // SE VALIDA QUE NO EXCEDA EL TAM MAX DEL CAMPO EN BD
								// Se trunca el valor a 10 caracteres
								cruce.setNroBCVOrdenDemanda(cruce.getNroBCVOrdenDemanda().substring(0, 10));
								validCampos = false;
								descripcionError += "Fila " + nroFila + " - ";
								if (codOrdenDemandaCell != null)
									descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
								descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_BCV_DEMANDA + "': Formato invalido: No debe exceder de 20 caracteres | ";
								Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_BCV_DEMANDA + "': Formato invalido: No debe exceder de 10 digitos");						
							}
						} else {//BCV EN LINEA
							if (cruce.getNroBCVOrdenDemanda() != null && cruce.getNroBCVOrdenDemanda().length() > 0) {
								validCampos = false;
								descripcionError += "Fila " + nroFila + " - ";
								if (codOrdenDemandaCell != null)
									descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
								descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_BCV_DEMANDA + "': El parametro BCV en linea se encuentra activo, no debe ingresar el numero de Id BCV | ";
								Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_BCV_DEMANDA + "': El parametro BCV en linea se encuentra activo, no debe ingresar el numero de Id BCV ");
							}
						}
						// VALIDACION NRO COTIZACION (REPETIDO EN OTRA ORDEN)							
						Logger.debug(this, "getNroCotizacionString : " + cruce.getNroCotizacionString());	
				}
			//}FIN BCV FUERA DE LINEA
			
			//VALIDACION ESTATUS BCV
			if (plantillaCruce && orden.getStatusVerificacionBCV().equalsIgnoreCase(ConstantesGenerales.VERIFICADA_RECHAZADA)) {
				validCampos = false;
				descripcionError +=  ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_DEMANDA + "': La orden fue verificada como Rechazada por BCV y se esta cargando como CRUZADA en la aplicacion | ";
				Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_DEMANDA + "': La orden fue verificada como Rechazada por BCV y se esta cargando como CRUZADA en la aplicacion ");					
			}
			
			 if (plantillaNoCruce && orden.getStatusVerificacionBCV().equalsIgnoreCase(ConstantesGenerales.VERIFICADA_APROBADA)) {
				validCampos = false;
				descripcionError += ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_DEMANDA + "': La orden fue verificada como APROBADA por BCV y se esta cargando como NO CRUZADA en la aplicacion | ";
				Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_DEMANDA + "': La orden fue verificada como APROBADA por BCV y se esta cargando como NO CRUZADA en la aplicacion ");					
			}
			
			orden.setIdEjecucion(proceso.getEjecucionId());
			// SE RESTRINGEN LOS CRUCES A ORDENES DE LA UNIDAD DE INVERSION SELECCIONADA
			if (orden.getIdUnidadInversion() != Integer.parseInt(idUiFiltered)) { // Si no coincide con la UI seleccionada
				validCampos = false;
				descripcionError += "Fila " + nroFila + " - ";
				if (codOrdenDemandaCell != null)
					descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
				descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_DEMANDA + "': La orden debe estar asociada a la Unidad de Inversion seleccionada (" + nameUiFiltered + ") | ";
				Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + ": La orden debe estar asociada a la Unidad de Inversion seleccionada (" + nameUiFiltered + ")");
			} else {
				cruce.setIdUI(Long.parseLong(idUiFiltered));
			}
			
			//VALIDACION DE TIPO DE PRODUCTO
			if (orden.getTipoProducto() != null && !orden.getTipoProducto().equals("")) {
				if (proceso.getTransaId().equals(TransaccionNegocio.CRUCE_SIMADI_MENUDEO_NATURAL_CARGA)) {
					if (!orden.getTipoProducto().equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL)) {
						validCampos = false;
						descripcionError += "Fila " + nroFila + " - ";
						if (codOrdenDemandaCell != null)
							descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
						descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_DEMANDA + "': La orden debe estar asociada al tipo de producto 'SICAD II CLAVENET PERSONAL' | ";
						Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + ": La orden debe estar asociada al tipo de producto 'SICAD II CLAVENET PERSONAL'");
					}
					// VALIDA QUE LA ORDEN INFI ESTE ASOCIADA A UNA SOLICITUD CLAVENET
					// NM26659 08/09/2014 Modificacion de consulta (Reemplazo de consulta extensa por busqueda si existe el registro)
					// ArrayList<SolicitudClavenet> arrSolic = solicitudesSitmeDAO.getSolicitudes(0, orden.getIdOrden(), null, null, null, null, null, null, null, false, false);
					// if(arrSolic.size()<=0){
					if (!solicitudesSitmeDAO.exiteOrden(orden.getIdOrden())) {
						validCampos = false;
						descripcionError += "Fila " + nroFila + " - ";
						if (codOrdenDemandaCell != null)
							descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
						descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_DEMANDA + "': La orden INFI debe estar asociada a alguna solicitud Clavenet | ";
						Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + ": La orden INFI debe estar asociada a alguna solicitud Clavenet");
					}
				} else {
					
					if (proceso.getTransaId().equals(TransaccionNegocio.CRUCE_SIMADI_MENUDEO_JURIDICO_CARGA) && !orden.getTipoProducto().equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL)) {
						validCampos = false;
						descripcionError += "Fila " + nroFila + " - ";
						if (codOrdenDemandaCell != null)
							descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
						descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_DEMANDA + "': La orden debe estar asociada al tipo de producto 'SICAD II RED COMERCIAL' | ";
						Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + ": La orden debe estar asociada al tipo de producto 'SICAD II RED COMERCIAL'");
					}
				}
			}
		} else {// LA ORDEN NO SE ENCUENTRA EN LA BASE DE DATOS
			validCampos = false;
			descripcionError += "Fila " + nroFila + " - ";
			if (codOrdenDemandaCell != null)
				descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
			descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_DEMANDA + "': La orden no aparece en la busqueda. Puede que dicha orden no se haya enviado a BCV o ya haya sido procesada | ";
			Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_DEMANDA + "': La orden no aparece en la búsqueda. Puede que dicha orden no se haya enviado a BCV o que ya haya sido procesada");
		}
		return validCampos;
	}
		
	/**
	 * Motodo de Validacion de orden de Oferta
	 * @param nroFila
	 * @param ordenOferta
	 * @param codOrdenOfertaCell
	 * @param codOrdenBcvOfertaCell
	 * @return
	 * @throws Exception
	 * @author NM26659
	 */
	private boolean validacionOrdenOferta(int nroFila,HSSFCell codOrdenOfertaCell,HSSFCell codOrdenBcvOfertaCell)throws Exception {
		boolean validCampos=true;		
		
		boolean ordenExiste=false;			
		//VALIDACION SI EL ID ES NULO
		if (codOrdenOfertaCell.toString() == null){
			validCampos=false;
			//descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
			descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_OFERTA + "': El campo Codigo de Orden no puede estar vacio | ";
			Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_OFERTA + "': El campo Codigo de Orden no puede estar vacio");
		}
		//VALIDACION SI EL ID ES MAYOR A 10 DIGITOS				
		if (codOrdenOfertaCell.toString() != null && codOrdenOfertaCell.toString().length() > 10) { // SE VALIDA QUE NO EXCEDA EL TAM MAX DEL CAMPO EN BD
			//System.out.println("OFERTA 4");
			// Se trunca el valor a 10 caracteres
			cruce.setIdOrdenOfertaString(codOrdenOfertaCell.toString().substring(0, 10));
			validCampos = false;
			descripcionError += "Fila " + nroFila + " - ";
			if (codOrdenOfertaCell != null)
				descripcionError += "Orden " + codOrdenOfertaCell.toString().replaceAll("\\.0", "") + " - ";
			descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_OFERTA + "': Formato invalido: No debe exceder de 10 digitos | ";
			Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_OFERTA + "': Formato invalido: No debe exceder de 10 digitos");
		}

		try {
			//VERFICACION FORMATO TIPO NUMERICO
			cruce.setIdOrdenOferta(Long.parseLong(codOrdenOfertaCell.toString().replaceAll("\\.0", "")));
			Logger.debug(this, "Se setea el ID orden: " + cruce.getIdOrdenINFI());
		} catch (NumberFormatException ex) {
			validCampos = false;
			// if(codOrdenDemandaCell==null || codOrdenDemandaCell.toString().equals(""))cruce.setIdOrdenINFI((long)(-1)); //Se setea un valor para indicar que la orden era invalida
			cruce.setIdOrdenOferta((long) (-1)); // Se setea un valor para indicar que la orden era invalida
			descripcionError += "Fila " + nroFila + " - ";
			if (codOrdenOfertaCell != null)
				descripcionError += "Orden " + codOrdenOfertaCell.toString().replaceAll("\\.0", "") + " - ";
			descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_OFERTA + "': El valor ingresado no es de tipo numerico (" + codOrdenOfertaCell.toString() + ") | ";
			Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_OFERTA + "': El valor ingresado no es de tipo numerico (" + codOrdenOfertaCell.toString() + ")");
			// throw new Exception("Orden "+codOrdenCell.toString().replaceAll("\\.0", "")+" - CAMPO INVALIDO: "+ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_DEMANDA);
		}

		if(cruce.getIdOrdenOferta()>0){									
				ofertaDAO.listarOrdenesPorEnviarBCV(0,0,0,0,0,0,false,false,0,0,false,true,cruce.getIdOrdenOfertaString(),null,null,null,null);				
				if(ofertaDAO.getDataSet().count()!=0){					
					ofertaDAO.getDataSet().first();
					ofertaDAO.getDataSet().next();
					ordenExiste=true;
				}
				if(ordenExiste) {				
					ordenOferta=new OrdenOferta(); 					
					ordenOferta.setIdOrdenOferta(cruce.getIdOrdenOferta());
									
					String idBcvOferta=ofertaDAO.getDataSet().getValue("ORDENE_ID_BCV");
						if(idBcvOferta!=null && !idBcvOferta.equals("") && !idBcvOferta.equals("0")){
							ordenOferta.setIdBcvOferta(idBcvOferta);
						}				
						
					String estatusBcvOferta=ofertaDAO.getDataSet().getValue("ORDENE_ESTATUS_BCV");
					if(estatusBcvOferta!=null && !estatusBcvOferta.equals("")){					
						ordenOferta.setEstatusBcvOferta(estatusBcvOferta);
					}					
					String totalOfertado=ofertaDAO.getDataSet().getValue("ORDENE_MONTO_OFERTADO");
					if(totalOfertado!=null && !totalOfertado.equals("")){						
						ordenOferta.setTotalOfertado(new BigDecimal(totalOfertado));
					}										 
					String tasaOfertada=ofertaDAO.getDataSet().getValue("ORDENE_TASA_CAMBIO");
					if(tasaOfertada!=null && !tasaOfertada.equals("")){						
						ordenOferta.setTasaOfertada(new BigDecimal(tasaOfertada));
					}					

					String nroJornada=ofertaDAO.getDataSet().getValue("NRO_JORNADA");
					if(nroJornada!=null && !nroJornada.equals("")){						
						ordenOferta.setNroJornada(nroJornada);
					}
					String codInstitucion=ofertaDAO.getDataSet().getValue("COD_INSTITUCION");
					if(codInstitucion!=null && !codInstitucion.equals("")){						
						ordenOferta.setCodInstitucion(codInstitucion);
					}					
					String statusCruce=ofertaDAO.getDataSet().getValue("ESTATUS_CRUCE");
					if(statusCruce!=null && !statusCruce.equals("")){						
						ordenOferta.setStatusCruce(statusCruce);
					}

					//Se verifica si la orden ya fue cruzada
						if(ordenOferta.getStatusCruce().equalsIgnoreCase(ConstantesGenerales.ESTATUS_ORDEN_CRUZADA) || ordenOferta.getStatusCruce().equalsIgnoreCase(ConstantesGenerales.ESTATUS_ORDEN_NO_CRUZADA)){	

						validCampos = false;											
						descripcionError += "Fila " + nroFila + " - ";													
						if (codOrdenOfertaCell != null) {																			
							descripcionError += "Orden " + codOrdenOfertaCell.toString().replaceAll("\\.0", "") + " - ";																					
							descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_OFERTA + "': La orden de Oferta que intenta procesar ya fue Cruzada por favor verifique ";											
							Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_OFERTA + "': La orden de Oferta que intenta procesar ya fue Cruzada por favor verifique ");													
						}																		
					}

					if(validacionBcvEnLinea){ // SI EL PARAMETRO VALIDACION BCV EN LINEA ESTA ACTIVO		
						if(ordenOferta.getEstatusBcvOferta().equalsIgnoreCase(ConstantesGenerales.SIN_VERIFICAR)){//ORDEN SIN VERIFICAR POR EL BCV										
							validCampos = false;				
							descripcionError += "Fila " + nroFila + " - ";						
							if (codOrdenOfertaCell != null) {											
								descripcionError += "Orden " + codOrdenOfertaCell.toString().replaceAll("\\.0", "") + " - ";													
								descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_OFERTA + "': Paramatro Validacion BCV en Linea se encuentra activo y la orden no ha sido verificada por el BCV ";				
								Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_OFERTA + "': : Paramatro Validacion BCV en Linea se encutra activo y la orden no ha sido verificada por el BCV ");							
							}

						}	

						//Verificacion del codigo de Orden BCV
						if(!cruce.getNroBCVOrdenOferta().equals("")){
							validCampos = false;
							descripcionError += "Fila " + nroFila + " - ";
							if (codOrdenBcvOfertaCell != null)
								descripcionError += "Codigo BCV Oferta " + codOrdenBcvOfertaCell.toString()+ " - ";
							descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_BCV_OFERTA + "': El parametro de verificacion BCV en linea se encuentra activo, no debe ingresar ningun valor en el campo  | ";
							Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_BCV_OFERTA + "': El parametro de verificacion BCV en linea se encuentra activo, no debe ingresar ningun valor en el campo ");
						} else {		
							if(ordenOferta.getIdBcvOferta()!=null && !ordenOferta.getIdBcvOferta().equals("") && !ordenOferta.getIdBcvOferta().equals("0")){
								cruce.setNroBCVOrdenOferta(ordenOferta.getIdBcvOferta());	
							}
							
						}
						
					}//FINAL VALIDACION BCV EN LINEA				
					else {//DESACTIVO VALIDACION EN LINEA BCV  
						if(ordenOferta.getEstatusBcvOferta().equalsIgnoreCase(ConstantesGenerales.SIN_VERIFICAR) && cruce.getNroBCVOrdenOferta().equals("")){//ORDEN SIN VERIFICAR POR EL BCV										
							validCampos = false;				
							descripcionError += "Fila " + nroFila + " - ";						
							if (codOrdenBcvOfertaCell != null) {											
								descripcionError += "Orden " + codOrdenBcvOfertaCell.toString() + " - ";													
								descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_BCV_OFERTA + "': No se ha ingresado valor de orden de Oferta y la orden no ha sido verificada por el BCV |";				
								Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_BCV_OFERTA + "': No se ha ingresado un numero de orden de Oferta y la orden no ha sido verificada por el BCV ");							
							}
						}else if(ordenOferta.getEstatusBcvOferta().equalsIgnoreCase(ConstantesGenerales.VERIFICADA_APROBADA) && cruce.getNroBCVOrdenOferta().length()>0){						
							validCampos = false;
							descripcionError += "Fila " + nroFila + " - ";
							if (codOrdenBcvOfertaCell != null)
								descripcionError += "Orden " + codOrdenBcvOfertaCell.toString()+ " - ";
							descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_BCV_OFERTA + "': La orden fue verificada via BCV en linea por lo que no puede colocar un n&uacute;mero de Id BCV | ";
							Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_BCV_OFERTA + "': La orden fue verificada via BCV en linea por lo que no puede colocar un n&uacute;mero de Id BCV  | ");
						}
								
						//Configuracion Id Orden BCV Oferta para orden Verificada cuando parametro BCV en Linea este apagado
						if(ordenOferta.getEstatusBcvOferta().equalsIgnoreCase(ConstantesGenerales.VERIFICADA_APROBADA)){						
							if(ordenOferta.getIdBcvOferta()!=null && !ordenOferta.getIdBcvOferta().equals("") && !ordenOferta.getIdBcvOferta().equals("0")){
								cruce.setNroBCVOrdenOferta(ordenOferta.getIdBcvOferta());	
							}
						}
					}
					
					//ORDEN OFERTA 
					if (identificadorPlantilla.equals(ConstantesGenerales.TITULO_TEMPLATE_CRUCE_SIMADI_MENUDEO) && ordenOferta.getEstatusBcvOferta().equalsIgnoreCase(ConstantesGenerales.VERIFICADA_RECHAZADA)) {
						validCampos = false;
						descripcionError +=  ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_DEMANDA + "': La orden fue verificada como Rechazada por BCV y se esta cargando como CRUZADA en la aplicacion | ";
						Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_OFERTA + "': La orden fue verificada como Rechazada por BCV y se esta cargando como CRUZADA en la aplicacion ");					
					}
					
					/* NM25287 13/04/2015 Se comenta para permitir el No cruce de operaciones aprobadas por BCV
					 * if (identificadorPlantilla.equals(ConstantesGenerales.TITULO_TEMPLATE_NO_CRUCE_SIMADI_MENUDEO) && ordenOferta.getEstatusBcvOferta().equalsIgnoreCase(ConstantesGenerales.VERIFICADA_APROBADA)) {
						validCampos = false;
						descripcionError += ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_DEMANDA + "': La orden fue verificada como APROBADA por BCV y se esta cargando como NO CRUZADA en la aplicacion | ";
						Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_OFERTA + "': La orden fue verificada como APROBADA por BCV y se esta cargando como NO CRUZADA en la aplicacion ");					
					}*/
					
				} else {											
					validCampos = false;											
					descripcionError += "Fila " + nroFila + " - ";					
					if (codOrdenOfertaCell != null){							
						descripcionError += "Orden " + codOrdenOfertaCell.toString().replaceAll("\\.0", "") + " - ";
					}					
					descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_OFERTA + "': El numero de orden ingresado no se encuentra registrado en la aplicacion por favor verifique " + codOrdenOfertaCell.toString() + " | ";					
					Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_OFERTA + "': El numero de orden ingresado no se encuentra registrado en la aplicacion por favor verifique " + codOrdenOfertaCell.toString());								
				}
			
		}		
		return validCampos;
	}
	
	/**
	 * Motodo de Validacion del cliente asociado a la ordenes de Demanda
	 * @param nroFila
	 * @param orden
	 * @return
	 * @throws Exception
	 * @author NM26659
	 */
	private boolean validacionClienteDemanda(int nroFila,Orden orden)throws Exception {
		boolean validCampos=true;	
		Pattern patRifCi = Pattern.compile("^[JjVvEeGgIi][0-9]{1,10}$");
		Matcher m; // Matcher que verifica que se cumpla con el patron
		m = patRifCi.matcher(cedulaCell.toString());
		if (!m.find()) { // Si el registro no coincide con el formato correspondiente
			validCampos = false;
			descripcionError += "Fila " + nroFila + " - ";
			if (codOrdenDemandaCell != null)
				descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
			descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_RIFCI + "': Formato invalido | ";
			Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_RIFCI + "': Formato invalido");
		} else {			
			// VALIDACION RIF/CI (CONTRA ORDEN)
			if (orden.getIdOrden() != 0) { // La orden existe				
				// VALIDACION CLIENTE (CONTRA TABLA INFI_TB_201_CTES)
				String ced_rif = "", tipper_id = "";
				try {					
					tipper_id = cedulaCell.toString().substring(0, 1);
					//NM26659 TTS-491 WEB SERVICE ALTO VALOR (Siempre se transforma el tipo de persona en mayuscula)
					tipper_id=tipper_id.toUpperCase();
					
					ced_rif = cedulaCell.toString().substring(1);
					Logger.debug(this, "ced_rif: " + ced_rif + ", tipper_id: " + tipper_id);
					cruce.setCiRif(cedulaCell.toString());
				} catch (Exception e) {					
					validCampos = false;
					descripcionError += "Fila " + nroFila + " - ";
					if (codOrdenDemandaCell != null)
						descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
					descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_RIFCI + "': Campo vacio o de longitud erronea | ";
					Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_RIFCI + "': Campo vacio o de longitud erronea");
				}				
				// BUSCA LA INFO DEL CLIENTE
				clienteDAO.listarCliente(0, tipper_id, Long.parseLong(ced_rif));				
				if (!clienteDAO.getDataSet().next()) {					
					validCampos = false;
					descripcionError += "Fila " + nroFila + " - ";
					if (codOrdenDemandaCell != null)
						descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
					descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_RIFCI + "': No se encuentra registrado en el sistema | ";
					Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_RIFCI + "': No se encuentra registrado en el sistema");
				} else { // Se encontro el cliente en bd					
					Logger.debug(this, "Cliente encontrado");

					// VALIDACION CLIENTE (CONTRA ORDEN)
					if (!(orden.getIdCliente() + "").equals(clienteDAO.getDataSet().getValue("CLIENT_ID"))) {
						validCampos = false;
						descripcionError += "Fila " + nroFila + " - ";
						if (codOrdenDemandaCell != null)
							descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
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
								if (codOrdenDemandaCell != null)
									descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
								descripcionError += " El tipo de persona no se encuentra configurado para los blotter asociados a la unidad de inversion o no han sido cargados los documentos del blotter | ";
								Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - : El tipo de persona no se encuentra configurado para los blotter asociados a la unidad de inversion o no han sido cargados los documentos del blotter");
							} else {								
								validCampos = false;
								descripcionError += "Fila " + nroFila + " - ";
								if (codOrdenDemandaCell != null)
									descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
								descripcionError += " No existe el blotter asociado al tipo de persona o no han sido cargados los documentos del blotter | ";
								Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - : No existe el blotter asociado al tipo de persona o no han sido cargados los documentos del blotter  ");

							}
						}
					}
				}// Se encontro el cliente en bd

			}// La orden existe
			else
				Logger.debug(this, "NO SE TRAJO INFO DE LA ORDEN DE BD");

		}		
		return validCampos;
	}
		
	/**
	 * Motodo de Validacion de la Unidad de inversion asociada a las ordenes de Demanda
	 * @param nroFila
	 * @param uiCell
	 * @return
	 * @throws Exception
	 * @author NM26659
	 */
	private boolean validacionUnidadInversion(int nroFila,HSSFCell uiCell)throws Exception {
		boolean validCampos=true;
		
//		 VALIDACION UNIDAD DE INVERSION (CONTRA UI SELECCIONADA)
		if (uiCell.toString() != null && !uiCell.toString().trim().equals("")) {
			if (!(uiCell.toString().toUpperCase().trim()).equals(nameUiFiltered.toUpperCase())) {
				validCampos = false;
				descripcionError += "Fila " + nroFila + " - ";
				if (codOrdenDemandaCell != null)
					descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
				descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_UI + "': La Unidad de Inversión ingresada (" + uiCell.toString().trim() + ") no corresponde a la seleccionada (" + nameUiFiltered + ") | ";
				Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_UI + "': La Unidad de Inversión ingresada (" + uiCell.toString().trim() + ") no corresponde a la seleccionada (" + nameUiFiltered + ")");
			}
			
		} else {
			validCampos = false;
			descripcionError += "Fila " + nroFila + " - ";
			if (codOrdenDemandaCell != null)
				descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
			descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_UI + "': El campo es vacio | ";
			Logger.info(this, "Error al procesar archivo de cruce de ordenes '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_UI + "': El campo es vacio");
		}
		return validCampos;
	}
	
	/**
	 * @param nroFila
	 * @param mtoSolicitadoCell
	 * @return
	 * @throws Exception
	 * @author NM26659
	 */
	private boolean validacionMontoSolicitado(int nroFila,HSSFCell mtoSolicitadoCell)throws Exception{
		boolean validCampos=true;
		int compare;
		
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
						if (codOrdenDemandaCell != null)
							descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
						descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_MONTO_SOLIC + "': El valor ingresado (" + mtoSolicitadoCell.toString() + ") no coincide con el de la orden (" + orden.getMonto() + "), ya que es mayor al mismo. Se redondea a 2 decimales | ";
						Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_MONTO_SOLIC + "': El valor ingresado (" + mtoSolicitadoCell.toString() + ") no coincide con el de la orden (" + orden.getMonto() + "), ya que es mayor al mismo. Se redondea a 2 decimales");
					} else {
						descripcionError += "Fila " + nroFila + " - ";
						if (codOrdenDemandaCell != null)
							descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
						descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_MONTO_SOLIC + "': El valor ingresado (" + mtoSolicitadoCell.toString() + ") no coincide con el de la orden (" + orden.getMonto() + "), ya que es menor al mismo. Se redondea a 2 decimales | ";
						Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Orden " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_MONTO_SOLIC + "': El valor ingresado (" + mtoSolicitadoCell.toString() + ") no coincide con el de la orden (" + orden.getMonto() + "), ya que es menor al mismo. Se redondea a 2 decimales");
					}
				} else {
					compare = cruce.getMontoSolicitado().compareTo(new BigDecimal(0));
					if (compare <= 0) {
						validCampos = false;
						cruce.setMontoSolicitado(null);
						descripcionError += "Fila " + nroFila + " - ";
						if (codOrdenDemandaCell != null)
							descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
						descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_MONTO_SOLIC + "': El valor ingresado es menor o igual a cero (" + mtoSolicitadoCell.toString() + ") | ";
						Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - campo '" + ConstantesGenerales.CAMPO_PLANTILLA_MONTO_SOLIC + "': El valor ingresado es menor o igual a cero (" + mtoSolicitadoCell.toString() + ")");
					}
				}
			}// La ordene xiste
		} catch (NumberFormatException ex) {
			cruce.setMontoSolicitado(null);
			validCampos = false;
			descripcionError += "Fila " + nroFila + " - ";
			if (codOrdenDemandaCell != null)
				descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
			descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_MONTO_SOLIC + "': El valor ingresado no es de tipo numerico (" + mtoSolicitadoCell.toString() + ") | ";
			Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Orden " + nroFila + " - campo '" + ConstantesGenerales.CAMPO_PLANTILLA_MONTO_SOLIC + "': El valor ingresado no es de tipo numerico (" + mtoSolicitadoCell.toString() + ")");
		}
		return validCampos;
	}
	
	/**
	 * @param nroFila
	 * @param sumCrucesDemanda
	 * @param cruce
	 * @param mtoCruceCell
	 * @return
	 * @throws Exception
	 * @author NM26659
	 */
	private boolean validacionMontoCruce(int nroFila,BigDecimal sumCrucesDemanda,OrdenesCruce cruce,HSSFCell mtoCruceCell)throws Exception{
		boolean validCampos=true;
		int compare;
		
		if (identificadorPlantilla.equals(ConstantesGenerales.TITULO_TEMPLATE_CRUCE_SIMADI_MENUDEO)) {			
			// VALIDACION MONTO OPERACION
			//NM26659 - 11/12/2015_Se comenta seccion de codigo por correccion incidencia (permite ingreso de monto cero en plantilla de adjudicacion)
			/*if (cruce.getMontoOperacion() != null && cruce.getMontoOperacion().compareTo(new BigDecimal(0)) == 0) {
				validCampos = false;
				descripcionError += "Fila " + nroFila + " - ";
				if (codOrdenDemandaCell != null)
					descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
				descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_MONTO_CRUCE + "': El valor del monto a cruzar no puede ser cero | ";
				Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_MONTO_CRUCE + "': El valor del monto a cruzar no puede ser cero");
			}*/
			
			if (mtoCruceCell == null ||(mtoCruceCell.toString() != null && new BigDecimal(mtoCruceCell.toString()).compareTo(new BigDecimal(0)) == 0)) {
				validCampos = false;
				descripcionError += "Fila " + nroFila + " - ";
				if (codOrdenDemandaCell != null)
					descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
				descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_MONTO_CRUCE + "': Debe ingresar un valor diferente a cero en el campo  | ";//del monto a cruzar no puede ser cero
				Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_MONTO_CRUCE + "': El valor del monto a cruzar no puede ser cero");
			}
		}
			
		if (mtoCruceCell.toString() != null && mtoCruceCell.toString().length() > 20) { // SE VALIDA QUE NO EXCEDA EL TAM MAX DEL CAMPO EN BD				
			//Se trunca el valor a 20 caracteres				
			cruce.setMontoOperacionString(mtoCruceCell.toString().substring(0, 20));				
			validCampos = false;			
			descripcionError += "Fila " + nroFila + " - ";
				
			if (codOrdenDemandaCell != null){				
				descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
			}
			descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_MONTO_CRUCE + "': Formato invalido: No debe exceder de 20 digitos | ";			
			Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_MONTO_CRUCE + "': Formato invalido: No debe exceder de 20 digitos");		
		}
	
			// VALIDACION MONTO CRUCE (FORMA Y CONTRA ORDEN)
			try {
				cruce.setMontoOperacion(new BigDecimal(Double.parseDouble(mtoCruceCell.toString())).setScale(2, BigDecimal.ROUND_HALF_UP));
				Logger.info(this, "MONTO CRUCE-------" + cruce.getMontoOperacion());
	
				if (orden.getIdOrden() != 0) { // La orden existe
					// Se CALCULA EL MONTO DISPONIBLE PARA CRUCE (sumCrucesDemanda = Sumatoria de los cruces ya realizados a la orden, de haberlos)
					Logger.debug(this, "SUM----CRUCESSS: " + sumCrucesDemanda);
					BigDecimal mtoDisponibleCruce = (new BigDecimal(orden.getMonto()).setScale(2, BigDecimal.ROUND_HALF_UP)).subtract(sumCrucesDemanda).setScale(2, BigDecimal.ROUND_HALF_UP);
					Logger.info(this, "MONTO DISPONIBLE CRUCE-------" + mtoDisponibleCruce);
					compare = cruce.getMontoOperacion().compareTo(mtoDisponibleCruce);
					if (compare > 0) {
						validCampos = false;
						descripcionError += "Fila " + nroFila + " - ";
						if (codOrdenDemandaCell != null)
							descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
						descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_MONTO_CRUCE + "': El valor ingresado (" + mtoCruceCell.toString() + ") supera el monto de cruce disponible (" + mtoDisponibleCruce + "). Se redondea a 2 decimales. | ";
						Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_MONTO_CRUCE + "': El valor ingresado (" + mtoCruceCell.toString() + ") supera el monto de cruce disponible (" + mtoDisponibleCruce + "). Se redondea a 2 decimales");
					}
				}// La orden existe
			} catch (NumberFormatException ex) {
				cruce.setMontoOperacion(null);
				validCampos = false;
				descripcionError += "Fila " + nroFila + " - ";
				if (codOrdenDemandaCell != null)
					descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
				descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_MONTO_CRUCE + "': El valor ingresado no es de tipo numerico (" + mtoCruceCell.toString() + ") | ";
				Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Orden " + nroFila + " - campo '" + ConstantesGenerales.CAMPO_PLANTILLA_MONTO_CRUCE + "': El valor ingresado no es de tipo numerico (" + mtoCruceCell.toString() + ")");
			
			}
		
		return validCampos;
	}

	/**
	 * @param nroFila
	 * @param tasaCell
	 * @param cruce
	 * @return
	 * @throws Exception
	 * @author NM26659
	 */
	private boolean validacionTasaDemanda(int nroFila,HSSFCell tasaCell,OrdenesCruce cruce)throws Exception{
		boolean validCampos=true;
		int compare;
		
		if (tasaCell.toString() != null && tasaCell.toString().length() > 15) { // SE VALIDA QUE NO EXCEDA EL TAM MAX DEL CAMPO EN BD

			// Se trunca el valor a 20 caracteres
			cruce.setTasaString(tasaCell.toString().substring(0, 15));
			validCampos = false;
			descripcionError += "Fila " + nroFila + " - ";
			if (codOrdenDemandaCell != null)
				descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
			descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_TASA + "': Formato invalido: No debe exceder de 15 digitos | ";
			Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_TASA + "': Formato invalido: No debe exceder de 15 digitos");
		}

		// VALIDACION DE CAMPO TASA (FORMA)
		try {
			double tasa = Double.parseDouble(tasaCell.toString());
			cruce.setTasa(new BigDecimal(tasa).setScale(4, BigDecimal.ROUND_HALF_UP));
			// cruce.setTasa(new BigDecimal(Double.parseDouble(tasaCell.toString())).setScale(2, BigDecimal.ROUND_HALF_UP));
			Logger.debug(this, "Se setea la Tasa: " + cruce.getTasa());

		} catch (Exception ex) {
			validCampos = false;
			descripcionError += "Fila " + nroFila + " - ";
			if (codOrdenDemandaCell != null)
				descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
			descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_TASA + "': El valor ingresado no es de tipo numerico (" + tasaCell.toString() + ") | ";
			Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_TASA + "': El valor ingresado no es de tipo numerico (" + tasaCell.toString() + ")");
		}
		if(plantillaCruce){						
		
			/*compare=ordenOferta.getTasaOfertada().compareTo(cruce.getTasa());				
			if(compare!=0){		
				validCampos = false;
				descripcionError += "Fila " + nroFila + " - ";
				if (codOrdenDemandaCell != null)
					descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
				descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_TASA + "': El monto de tasa ingresado para la demanda no coinciden con la oferta asociada: Tasa Demanda -> " + tasaCell.toString() + " Tasa Oferta -> " + ordenOferta.getTasaOfertada() +" | ";
				Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_TASA + "': : El monto de tasa ingresado para la demanda no coinciden con la oferta asociada: Tasa Demanda -> " + tasaCell.toString() + " Tasa Oferta -> " + ordenOferta.getTasaOfertada());
			}
			 */
			
			compare= orden.getTasaPool().compareTo(cruce.getTasa());			
			if(compare!=0){
				validCampos = false;
				descripcionError += "Fila " + nroFila + " - ";
				if (codOrdenDemandaCell != null)
					descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
				descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_TASA + "': El monto de tasa ingresado para la demanda no coincide con el monto original de la operacion: Tasa Demanda -> " + tasaCell.toString() + " Tasa Original Operacion -> " + orden.getTasaPool() +" | ";
				Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_TASA + "': El monto de tasa ingresado para la demanda no coincide con el monto original de la operacion: Tasa Demanda ->  " + tasaCell.toString() + " Tasa Original Operacion -> " + orden.getTasaPool());
			}
			
		}
		return validCampos;
		
	}
	
	/**
	 * @param nroFila
	 * @param contraparteCell
	 * @param cruce
	 * @return
	 * @throws Exception
	 * @author NM26659
	 */
	private boolean validacionContraparte(int nroFila,HSSFCell contraparteCell,OrdenesCruce cruce)throws Exception{
		boolean validCampos=true;
		
		Pattern patAlfaNum = Pattern.compile("^[a-zA-ZáéíóúüÁÉÍÓÚÜ0-9 _\\-,.]+$");
		Matcher m; // Matcher que verifica que se cumpla con el patron
		
		if (identificadorPlantilla.equals(ConstantesGenerales.TITULO_TEMPLATE_CRUCE_SIMADI_MENUDEO)) {		
			// VALIDACION CONTRAPARTE (FORMA)
			if (contraparteCell.toString() != null && contraparteCell.toString().length() > 100) { // SE VALIDA QUE NO EXCEDA EL TAM MAX DEL CAMPO EN BD
				// Se trunca el valor a 100 caracteres
				cruce.setContraparte(contraparteCell.toString().substring(0, 100));
				validCampos = false;
				descripcionError += "Fila " + nroFila + " - ";
				if (codOrdenDemandaCell != null)
					descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
				descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_CONTRAPARTE + "': Formato invalido: No debe exceder de 100 caracteres | ";
				Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_CONTRAPARTE + "': Formato invalido: No debe exceder de 100 caracteres");
			}

			// VALIDACION CONTRAPARTE (FORMA)
			m = patAlfaNum.matcher(contraparteCell.toString());
			if (!m.find()) { // Si el registro no coincide con el formato correspondiente
				validCampos = false;
				descripcionError += "Fila " + nroFila + " - ";
				if (codOrdenDemandaCell != null)
					descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
				descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_CONTRAPARTE + "': Formato invalido ------ | ";
				Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_CONTRAPARTE + "': Formato invalido");
			}
		
		}
		return validCampos;
	}
		
	/**
	 * @param nroFila
	 * @param fechaValorCell
	 * @return
	 * @throws Exception
	 * @author NM26659
	 */
	private boolean validacionFechaValor(int nroFila,HSSFCell fechaValorCell)throws Exception {
		boolean validCampos=true;
		
		Pattern patFecha = Pattern.compile("^((0[1-9])|([12][0-9])|(3[01]))[/\\-]((0[1-9])|(1[0-2]))[/\\-]([1-9][0-9][0-9][0-9])$");
		Matcher m;
		
		
		if (fechaValorCell.toString() != null && fechaValorCell.toString().length() > 10) { // SE VALIDA QUE NO EXCEDA EL TAM MAX DEL CAMPO EN BD
			// Se trunca el valor a 10 caracteres		
			cruce.setFechaValor(fechaValorCell.toString().substring(0, 10));
			validCampos = false;
			descripcionError += "Fila " + nroFila + " - ";
			if (codOrdenDemandaCell != null)
				descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
			descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_FECHA_VALOR + "': Formato invalido: No debe exceder de 10 caracteres | ";
			Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_FECHA_VALOR + "': Formato invalido: No debe exceder de 10 caracteres");
		}
		//VALIDACION DE CAMPO FECHA VALOR
		try {
			boolean fValRep = false;
			String dia = "", mes = " ", anio = "";		
			m = patFecha.matcher(fechaValorCell.toString());
			if (m.find()) { // Si el registro coincide con el formato correspondiente
				dia = fechaValorCell.toString().substring(0, 2);
				mes = fechaValorCell.toString().substring(3, 5);
				anio = fechaValorCell.toString().substring(6, 10);
				// Setea la fecha valor a la orden aprobacion
				cruce.setFechaValor(dia + "/" + mes + "/" + anio);
				cruce.setFechaValorGuion(dia + "-" + mes + "-" + anio);
				if (fValorMap.containsKey("D" + "|" + cruce.getIdOrdenInfiString())) {
					if (!fValorMap.get("D" + "|" + cruce.getIdOrdenInfiString()).equalsIgnoreCase(cruce.getFechaValor())) {
						fValRep = true;
					}
				} else {
					fValorMap.put("D" + "|" + cruce.getIdOrdenInfiString(), cruce.getFechaValor());
				}
				if (_resgitrosCruces.count() > 0) {
					_resgitrosCruces.first();
					_resgitrosCruces.next();
					if (_resgitrosCruces.getValue("ESTATUS").equals(StatusOrden.CRUZADA)) {
						if (com.bdv.infi.util.Utilitario.StringToDate(_resgitrosCruces.getValue("fecha_valor"), ConstantesGenerales.FORMATO_FECHA2).compareTo(com.bdv.infi.util.Utilitario.StringToDate(cruce.getFechaValor(), ConstantesGenerales.FORMATO_FECHA2)) != 0) {
							fValRep = true;
						}
					}
					
				}
				if (fValRep) {
					validCampos = false;
					descripcionError += "Fila " + nroFila + " - ";
					if (codOrdenDemandaCell != null)
						descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
					descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_FECHA_VALOR + "': El valor no se corresponde con el seteado para un cruce de la misma orden (" + cruce.getFechaValor() + ") cargado previamente | ";
					Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_FECHA_VALOR + "': El valor no se corresponde con el seteado para un cruce de la misma orden (" + cruce.getFechaValor() + ") cargado previamente");
				}
			} else {
				validCampos = false;
				descripcionError += "Fila " + nroFila + " - ";
				if (codOrdenDemandaCell != null)
					descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
				descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_FECHA_VALOR + "': Verifique que el formato sea DD/MM/YYYY | ";
				Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_FECHA_VALOR + "': Verifique que el formato sea DD/MM/YYYY");
			}
		} catch (Exception e) {
			validCampos = false;
			e.getStackTrace();
			descripcionError += "Fila " + nroFila + " - ";
			if (codOrdenDemandaCell != null)
				descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
			descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_FECHA_VALOR + "': Formato invalido excepcion| " + e.getMessage() + e.getStackTrace();
			Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_FECHA_VALOR + "': Formato invalido");
		}
		if (identificadorPlantilla.equals(ConstantesGenerales.TITULO_TEMPLATE_CRUCE_SIMADI_MENUDEO)) {				
			//VALIDACION FECHA VALOR (FORMA)
			if (fechaValorCell.toString() != null && fechaValorCell.toString().length() > 10) { // SE VALIDA QUE NO EXCEDA EL TAM MAX DEL CAMPO EN BD
				// Se trunca el valor a 10 caracteres
				cruce.setFechaValor(fechaValorCell.toString().substring(0, 10));
				validCampos = false;
				descripcionError += "Fila " + nroFila + " - ";
				if (codOrdenDemandaCell != null)
					descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
				descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_FECHA_VALOR + "': Formato invalido: No debe exceder de 10 caracteres | ";
				Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_FECHA_VALOR + "': Formato invalido: No debe exceder de 10 caracteres");
			}
		}
		
		
		return validCampos;
	}
		
	/**
	 * @param nroFila
	 * @param sumCrucesDemanda
	 * @param cruce
	 * @return
	 * @throws Exception
	 * @author NM26659
	 *///TODO REVISAR METODO PARA PASAR INVOCACION FUERA DEL BLOQUE DE PROCESAMIENTO DE CRUCE
	private boolean validacionCruceDemanda(int nroFila,BigDecimal sumCrucesDemanda,OrdenesCruce cruce)throws Exception{
		boolean validCampos=true;
		int compare;
		
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
					if (codOrdenDemandaCell != null)
						descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
					descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_DEMANDA + "': No puede realizar un cruce/no cruce a la orden puesto que ya existe un registro de no cruce por cargar asociado a la misma | ";
					Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_DEMANDA + "': No puede realizar un cruce/no cruce a la orden puesto que ya existe un registro de no cruce por cargar asociado a la misma");
				} else {
					if (compare > 0) { // Ya existe un Cruce
						if (identificadorPlantilla.equals(ConstantesGenerales.TITULO_TEMPLATE_NO_CRUCE_SIMADI_MENUDEO)) { // NO CRUCE
							validCampos = false;
							descripcionError += "Fila " + nroFila + " - ";
							if (codOrdenDemandaCell != null)
								descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
							descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_DEMANDA + "': No puede realizar un no cruce a la orden puesto que ya existe al menos un registro de cruce por cargar asociado a la misma | ";
							Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_DEMANDA + "': No puede realizar un no cruce a la orden puesto que ya existe al menos un registro de cruce por cargar asociado a la misma");
						} else {
							if (identificadorPlantilla.equals(ConstantesGenerales.TITULO_TEMPLATE_CRUCE_SIMADI_MENUDEO)) { // CRUCE
								sumCrucesDemanda = ordenesCruces.get(cruce.getIdOrdenInfiString());
							}
						}
					}
				}
			}// Ya existen cruces/no cruces en el lote de carga para la orden
							
			ordenesCrucesDAO.listarCrucesPorIdOrdenInfi(cruce.getIdOrdenINFI(), null, StatusOrden.NO_CRUZADA, StatusOrden.CRUZADA);
			_resgitrosCruces = ordenesCrucesDAO.getDataSet();
			if (_resgitrosCruces.count() > 0) {				
					_resgitrosCruces.first();				
					_resgitrosCruces.next();
				if (_resgitrosCruces.getValue("ESTATUS").equals(StatusOrden.NO_CRUZADA)) {
					validCampos = false;
					descripcionError += "Fila " + nroFila + " - ";
					if (codOrdenDemandaCell != null)
						descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
					descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_DEMANDA + "': No puede realizar un cruce/no cruce a la orden puesto que ya existe un registro de no cruce asociado a la misma | ";
					Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_DEMANDA + "': No puede realizar un cruce/no cruce a la orden puesto que ya existe un registro de no cruce asociado a la misma");
				} else if (_resgitrosCruces.getValue("ESTATUS").equals(StatusOrden.CRUZADA)) { // No existe registro no cruzado para la orden
					// NM26659 Modificacion de codigo para resumir cantidad de busquedas a la base de datos.

					if (identificadorPlantilla.equals(ConstantesGenerales.TITULO_TEMPLATE_NO_CRUCE_SIMADI_MENUDEO)) { // NO CRUCE
						validCampos = false;
						descripcionError += "Fila " + nroFila + " - ";
						if (codOrdenDemandaCell != null)
							descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
						descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_DEMANDA + "': No puede realizar un no cruce a la orden puesto que ya existe al menos un registro de cruce asociado a la misma | ";
						Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_DEMANDA + "': No puede realizar un no cruce a la orden puesto que ya existe al menos un registro de cruce asociado a la misma");
					} else { // ES UN CRUCE
						// ordenesCrucesDAO.getDataSet().first();
						_resgitrosCruces.first();
						// for(int i=0; i<ordenesCrucesDAO.getDataSet().count(); i++){
						for (int i = 0; i < _resgitrosCruces.count(); i++) {
							// ordenesCrucesDAO.getDataSet().next();
							_resgitrosCruces.next();
							// sumCrucesDemanda = sumCrucesDemanda.add(new BigDecimal(ordenesCrucesDAO.getDataSet().getValue("MONTO_OPERACION")));
							sumCrucesDemanda = sumCrucesDemanda.add(new BigDecimal(_resgitrosCruces.getValue("MONTO_OPERACION")));
						}
						sumCrucesDemanda = sumCrucesDemanda.setScale(2, BigDecimal.ROUND_HALF_UP);
						Logger.info(this, "sumCrucesDemanda-------------" + sumCrucesDemanda);
					}
				}
				

				//ITS-2912_09/12/2015 - NM26659 (Se desplaza seccion de codigo fuera de validacion de regitros de base de datos) //Correccion de error "Plantilla de cruce permite Cruzar montos mayores al solicitado
			//Validacion Monto Cruce No exceda monto disponible de cruce de la Demanda
			/*	BigDecimal mtoDisponible=new BigDecimal(orden.getMonto()).subtract(sumCrucesDemanda);
				compare=mtoDisponible.compareTo(cruce.getMontoOperacion());				
				if(compare<0){
					validCampos = false;
					descripcionError += "Fila " + nroFila + " - ";
					if (codOrdenDemandaCell != null)
						descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
					descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_DEMANDA + "': El monto de cruce ('"+ cruce.getMontoOperacion() +  "') excede el monto disponible ('"+ mtoDisponible +"') para el cruce de la Demanda  | ";
					Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_DEMANDA + "': : El monto de cruce ('"+ cruce.getMontoOperacion() +  "') excede el monto disponible ('"+ mtoDisponible +"') para el cruce de la Demanda  ");
				}*/
				
				//VERIFICACION SI ID ORDEN BCV INGRESADO ES DIFERENTE A ALGUNO YA EXISTENTE	EN TABLA DE CRUCES				
				if(!validacionBcvEnLinea && !_resgitrosCruces.getValue("ESTATUS").equals(StatusOrden.NO_CRUZADA)) {//BCV FUERA DE LINEA 										
					_resgitrosCruces.first();				
					while(_resgitrosCruces.next()){//Valida que el ID de BCV ingresado (Procesamiento Manual) sea igual al de los cruces ya ingresados 
						String idBcv="";
						idBcv=_resgitrosCruces.getValue("ORDENE_ID_BCV");											
						if((idBcv!=null && !idBcv.equals("")) && (cruce.getNroBCVOrdenDemanda()!=null && !cruce.getNroBCVOrdenDemanda().equals(""))) {
							if(!idBcv.equals(cruce.getNroBCVOrdenDemanda())){
								validCampos = false;
								descripcionError += "Fila " + nroFila + " - ";
								if (codOrdenDemandaCell != null)
									descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
								descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_BCV_DEMANDA + "': El numero de Orden BCV ingresado es diferente a algun registro de cruce ya ingresado, por favor verifique | ";
								Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_BCV_DEMANDA + "': El numero de Orden BCV ingresado es diferente a algun registro de cruce ya ingresado, por favor verifique");								
							}
						}					
					}				
				}
			
			}

			//ITS-2912_09/12/2015 - NM26659 Correccion de error "Plantilla de cruce permite Cruzar montos mayores al solicitado
			//Validacion Monto Cruce No exceda monto disponible de cruce de la Demanda
			BigDecimal mtoDisponible=new BigDecimal(orden.getMonto()).subtract(sumCrucesDemanda);
			compare=mtoDisponible.compareTo(cruce.getMontoOperacion());
			
			if(compare<0){
				validCampos = false;
				descripcionError += "Fila " + nroFila + " - ";
				if (codOrdenDemandaCell != null)
					descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
				descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_DEMANDA + "': El monto de cruce ('"+ cruce.getMontoOperacion() +  "') excede el monto disponible ('"+ mtoDisponible +"') para el cruce de la Demanda  | ";
				Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_DEMANDA + "': : El monto de cruce ('"+ cruce.getMontoOperacion() +  "') excede el monto disponible ('"+ mtoDisponible +"') para el cruce de la Demanda  ");
			}
			
			try{
			//VERIFICACION SI ID ORDEN BCV INGRESADO ES DIFERENTE A EL ID BCV QUE SE ENCUENTRA ANTES EN LA PLANTILLA
		
				if(!validacionBcvEnLinea && cruce.getNroBCVOrdenDemanda()!=null && !cruce.getNroBCVOrdenDemanda().equals("")){				
				
					if(validCampos){//Si todos los filtros anteriores son validos
						if(idBcvOrdenesCruces.contains(cruce.getIdOrdenINFI())){ 
							if(!idBcvOrdenesCruces.get(cruce.getIdOrdenINFI()).equals(cruce.getNroBCVOrdenDemanda())){
								validCampos = false;
								descripcionError += "Fila " + nroFila + " - ";
								if (codOrdenDemandaCell != null)
									descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
								descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_BCV_DEMANDA + "': El numero de Orden BCV ingresado no concuerda con que se encuentra en proceso de carga actualmente, por favor verifique | ";
								Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_BCV_DEMANDA + "': El numero de Orden BCV ingresado es diferente a algun registro de cruce ya ingresado, por favor verifique");
							}					
						}else {
							idBcvOrdenesCruces.put(String.valueOf(cruce.getIdOrdenINFI()),cruce.getNroBCVOrdenDemanda());
						}			
					}
		}
			
			}catch(Exception e){
				//System.out.println("ERROR ---> " + e.getMessage());
				e.printStackTrace();
			}
			cruce.setContravalorBolivaresCapital(cruce.getMontoOperacion().multiply(cruce.getTasa()));

		}
		return validCampos;
	}
	
	/**
	 * @param nroFila
	 * @param sumCrucesOferta
	 * @param cruce
	 * @return
	 * @throws Exception
	 * @author NM26659
	 */
	private boolean validacionCruceOferta(int nroFila,BigDecimal sumCrucesOferta,OrdenesCruce cruce)throws Exception{
		boolean validCampos=true;
		int compare;
		
		if (cruce.getIdOrdenOferta() > 0) { // Se seteo el ID Orden traido desde el archivo		
			//Verificacion de monto de la Oferta sobre lo que esta cargado en la plantilla
			Logger.debug(this, "ID ORDEN INFI LONG SETEADO");						
			if (ordenesCrucesOfertas.containsKey(cruce.getIdOrdenOfertaString())) {
				// Ya hay un cruce/no cruce asociado a la orden en el lote de cruces
				compare = ordenesCrucesOfertas.get(cruce.getIdOrdenOfertaString()).compareTo(new BigDecimal(0));
				if (compare == 0) { // Ya existe un No Cruce
					validCampos = false;
					descripcionError += "Fila " + nroFila + " - ";
					if (codOrdenDemandaCell != null)
						descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
					descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_OFERTA + "': No puede realizar un cruce/no cruce a la orden puesto que ya existe un registro de no cruce por cargar asociado a la misma | ";
					Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_OFERTA + "': No puede realizar un cruce/no cruce a la orden puesto que ya existe un registro de no cruce por cargar asociado a la misma");
				} else {
					if (compare > 0) { // Ya existe un Cruce
						if (identificadorPlantilla.equals(ConstantesGenerales.TITULO_TEMPLATE_NO_CRUCE_SIMADI_MENUDEO)) { // NO CRUCE
							validCampos = false;
							descripcionError += "Fila " + nroFila + " - ";
							if (codOrdenDemandaCell != null)
								descripcionError += "Orden " + codOrdenDemandaCell.toString().replaceAll("\\.0", "") + " - ";
							descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_OFERTA + "': No puede realizar un no cruce a la orden puesto que ya existe al menos un registro de cruce por cargar asociado a la misma | ";
							Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_OFERTA + "': No puede realizar un no cruce a la orden puesto que ya existe al menos un registro de cruce por cargar asociado a la misma");
						} else {
							if (identificadorPlantilla.equals(ConstantesGenerales.TITULO_TEMPLATE_CRUCE_SIMADI_MENUDEO)) { // CRUCE
								sumCrucesOferta = ordenesCrucesOfertas.get(cruce.getIdOrdenOfertaString());
							}
						}
					}
				}
			}// Ya existen cruces/no cruces en el lote de carga para la orden
			
			ordenesCrucesDAO.listarCrucesPorIdOrdenOfertaInfi(cruce.getIdOrdenOferta());
			_resgitrosCruces = ordenesCrucesDAO.getDataSet();
			if (_resgitrosCruces.count() > 0) {
				_resgitrosCruces.first();
				_resgitrosCruces.next();				
				 // ES UN CRUCE
						// ordenesCrucesDAO.getDataSet().first();
						_resgitrosCruces.first();
						// for(int i=0; i<ordenesCrucesDAO.getDataSet().count(); i++){
						for (int i = 0; i < _resgitrosCruces.count(); i++) {
							// ordenesCrucesDAO.getDataSet().next();
							_resgitrosCruces.next();
							// sumCrucesDemanda = sumCrucesDemanda.add(new BigDecimal(ordenesCrucesDAO.getDataSet().getValue("MONTO_OPERACION")));
							sumCrucesOferta = sumCrucesOferta.add(new BigDecimal(_resgitrosCruces.getValue("MONTO_OPERACION")));						
						}
						sumCrucesOferta = sumCrucesOferta.setScale(2, BigDecimal.ROUND_HALF_UP);
						Logger.info(this, "sumCrucesOferta-------------" + sumCrucesOferta);												
			}
			
			compare=(cruce.getMontoOperacion().add(sumCrucesOferta)).compareTo(ordenOferta.getTotalOfertado());
			if(compare>0){
				validCampos = false;
				descripcionError += "Fila " + nroFila + " - ";
				if (codOrdenOfertaCell != null)
					descripcionError += "Orden Oferta" + codOrdenOfertaCell.toString().replaceAll("\\.0", "") + " - ";
				descripcionError += "Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_OFERTA + "': El total de cruce acumulado sobrepasa el monto  el total ofertado | ";
				Logger.info(this, "Error al procesar archivo de cruces '" + getNombreDocumento() + "' en la Fila " + nroFila + " - Campo '" + ConstantesGenerales.CAMPO_PLANTILLA_COD_ORDEN_OFERTA + "': El total de cruce acumulado sobrepasa el monto  el total ofertado  ");	
			}
		}
		
		
		return validCampos;
	}
	
	/**
	 * Crea un nuevo proceso del tipo de transaccion especificada
	 * 
	 * @param proceso
	 * @return boolean indicando si se inserto el proceso de carga de cruces
	 * @throws Exception
	 * 
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

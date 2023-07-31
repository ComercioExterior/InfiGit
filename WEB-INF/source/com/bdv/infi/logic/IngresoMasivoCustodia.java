package com.bdv.infi.logic;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import megasoft.DataSet;
import megasoft.db;

import com.bdv.infi.dao.CustodiaDAO;
import com.bdv.infi.dao.FechasCierresDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.TitulosBloqueoDAO;
import com.bdv.infi.dao.TitulosDAO;
import com.bdv.infi.data.Custodia;
import com.bdv.infi.data.TituloBloqueo;
import com.bdv.infi.logic.interfaces.Beneficiarios;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TipoBloqueos;
import com.bdv.infi.util.Utilitario;

/**
 * Clase que contiene la lógica para llevar los títulos de una orden hacia
 * custodia}
 * elaucho,jvillegas,nvisbal
 */
public class IngresoMasivoCustodia {

	/**
	 * Variable de gestion de logs
	 */
		private Logger logger = Logger.getLogger(IngresoMasivoCustodia.class);
	/*
	 * Definicion de objetos y variables
	 */
	OrdenDAO ordenDAO;
	int financiado;
	TitulosDAO tituloDAO;
	CustodiaDAO custodiaDAO;
	FechasCierresDAO fechasCierresDAO;
	TitulosBloqueoDAO titulosBloqueoDAO;
	Custodia custodia;
	String idCliente;
	String tituloId;
	String tipoProductoId;
	DataSource dataSource;
	Utilitario utilitario = new Utilitario();
	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
	
	//Objetos globales
	DataSet _titulos;
	double pct_recompra; //procentaje de recompra del título
	ArrayList<String> listaSQL = null;
	boolean cierreEfectuado = false;
	boolean bloqueoPorRecompra = false;


	public IngresoMasivoCustodia(DataSource ds) throws Exception {
        this.dataSource = ds;
		ordenDAO = new OrdenDAO(dataSource);
		tituloDAO = new TitulosDAO(dataSource);
		custodiaDAO = new CustodiaDAO(dataSource);
		fechasCierresDAO = new FechasCierresDAO(dataSource);
		titulosBloqueoDAO = new TitulosBloqueoDAO(dataSource);
		custodia = new Custodia();
	}

	/**
	 * Función que ingresa a custodia los títulos de la orden recibida
	 * 
	 * @param idOrden id de la orden que se desea enviar a custodia
	 * @return Array de consultas SQL que se deben ejecutar
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	public String[] ingresarCustodia(long idOrden, boolean cobroEnLinea, boolean hayOperacionesPendientes)throws Exception{
		bloqueoPorRecompra = false;
		financiado = 0;
		listaSQL = new ArrayList<String>();
		if (!cierreEfectuado){
			//Insertamos en la tabla de fechas de registro
			String consultasFechaCierre[] = fechasCierresDAO.insertarRegistro();
			
			//Ejecutamos las consultas
			if(consultasFechaCierre!=null){
				db.execBatch(dataSource, consultasFechaCierre);
				logger.debug("Insertada fecha de cierre...");
			}
			cierreEfectuado = true;
		}

		int cantidadTitulo = 0;
		TituloBloqueo tituloBloqueo = new TituloBloqueo();
		
		logger.info("Ingresando a custodia orden número" + idOrden);
		
		/*
		 * Buscamos todos los valores de la orden
		 */
		//long inicioLD = System.currentTimeMillis();
		ordenDAO.listarDatosOrden(idOrden);
		/*if (logger.isDebugEnabled()){
			logger.debug("Tiempo de procesamiento de listadoDeOrden " + (System.currentTimeMillis() - inicioLD) + " miliseg ");
		}*/
		
		DataSet _orden = ordenDAO.getDataSet();
		if(_orden.count()>0){
			_orden.first(); 
			_orden.next();
			/*
			 * Se verifica si la orden es financiada
			 */
		  	financiado = Integer.parseInt(_orden.getValue("ordene_financiado"));
		  	idCliente = _orden.getValue("client_id");
			
			/*
			 * Buscamos los titulos asociados a la orden
			 */
		  	//long inicioLTD = System.currentTimeMillis();
			tituloDAO.listarDatosTituloOrden(String.valueOf(idOrden));
			/*if (logger.isDebugEnabled()){
				logger.debug("Tiempo de procesamiento de listadoDeTitulosOrden " + (System.currentTimeMillis() - inicioLTD) + " miliseg ");
			}*/

			_titulos = tituloDAO.getDataSet();			
			if(_titulos.count()>0){
				_titulos.first();
				
				try{				
					while(_titulos.next()){
						pct_recompra = 0;
						if (_titulos.getValue("titulo_pct_recompra")!=null && !_titulos.getValue("titulo_pct_recompra").equals("")){
							pct_recompra = Double.parseDouble(_titulos.getValue("titulo_pct_recompra"));
						}
						tituloId = _titulos.getValue("titulo_id");
						tipoProductoId = _titulos.getValue("tipo_producto_id");
						String fechaUltPagoCupon = utilitario.obtenerFechaUltimoPagoCuponAmortizacion(tituloId,dataSource);
						cantidadTitulo = Integer.parseInt(_titulos.getValue("titulo_unidades"));//titulo orden					
						
						custodia.setIdCliente(Long.parseLong(idCliente));
						custodia.setIdTitulo(tituloId);
						custodia.setTipoProductoId(tipoProductoId);
						
						//Este if debe ser eliminado
						if(fechaUltPagoCupon==null || fechaUltPagoCupon.equals(""))
							fechaUltPagoCupon = simpleDateFormat.format(new Date());
							
						custodia.setFechaUltimoCupon(simpleDateFormat.parse(fechaUltPagoCupon));
						custodia.setFechaUltimaAmortizacion(simpleDateFormat.parse(fechaUltPagoCupon));
						
						// Verificamos si el cliente posee el título en custodia
						//long inicioVrificar = System.currentTimeMillis();
						custodiaDAO.verificarTitulo(idCliente,tituloId,tipoProductoId);
						/*if (logger.isDebugEnabled()){
							logger.debug("Tiempo de procesamiento de verificarTitulo " + (System.currentTimeMillis() - inicioVrificar) + " miliseg ");
						}*/
						if (custodiaDAO.getDataSet().count()>0){
							//Registramos la entra de los títulos a custodia sólo si no hay pacto de recompra

							if (pct_recompra == 0 || (pct_recompra > 0 && hayOperacionesPendientes)){
								custodiaDAO.getDataSet().first();
								custodiaDAO.getDataSet().next();
								logger.info("Cantidad que posee en custodia:"+custodiaDAO.getDataSet().getValue("TITCUS_CANTIDAD"));
								//set la cantidad que posee en custodia
								custodia.setCantidad(Long.parseLong(custodiaDAO.getDataSet().getValue("TITCUS_CANTIDAD")));
								logger.info("Modificando en custodia título " + tituloId );
								logger.info("Cantidad agregada:"+cantidadTitulo);
								custodia.agregarCantidad(cantidadTitulo);
								//long inicioModif = System.currentTimeMillis();
								listaSQL.add(custodiaDAO.modificar(custodia));
								/*if (logger.isDebugEnabled()){
									logger.debug("Tiempo de procesamiento de efectuandoModificacionCustodia " + (System.currentTimeMillis() - inicioModif) + " miliseg ");
								}*/
							}
								
							//Verificamos si tiene recompra para efectuar la salida
							verificarRecompra(cantidadTitulo,hayOperacionesPendientes);	
							
						}else{
							//Registramos la entrada del título
							custodia.setCantidad(0); //Marcamos en 0 porque se usa un solo objeto para todos los clientes
							logger.info("Insertando a custodia título " + tituloId );
							
							if (pct_recompra > 0 && !hayOperacionesPendientes){
								cantidadTitulo = 0;
							}
							
							custodia.agregarCantidad(cantidadTitulo);
							listaSQL.add(custodiaDAO.insertar(custodia));
							
							//Verificamos si tiene recompra para efectuar la salida
							verificarRecompra(cantidadTitulo,hayOperacionesPendientes);
						}
						
			
						// Si la orden es financiado buscamos los títulos y les
						// agregamos la cantidad, sino lo insertamos siempre y cuando el título no tenga recompra
						if(financiado==ConstantesGenerales.VERDADERO){// si es financiada
								// Buscamos los titulos en bloqueo a ver si coinciden
								// con el tipo de bloqueo a establecer y con el tipo de
								// beneficiario
							tituloBloqueo = titulosBloqueoDAO.listarBloqueo(tituloId, Long.parseLong(idCliente), TipoBloqueos.BLOQUEO_FINANCIAMIENTO, Beneficiarios.BENEFICIARIO_DEFECTO,tipoProductoId);							
							if (tituloBloqueo.getTipoBloqueo() == null){
								tituloBloqueo.setBeneficiario(Beneficiarios.BENEFICIARIO_DEFECTO);
								tituloBloqueo.setCliente(Long.parseLong(idCliente));
								tituloBloqueo.setTitulo(tituloId);
								tituloBloqueo.setTituloCustodiaCantidad(cantidadTitulo);								
								tituloBloqueo.setTipoBloqueo(TipoBloqueos.BLOQUEO_FINANCIAMIENTO);
								tituloBloqueo.setTipoProducto(tipoProductoId);
								listaSQL.add(titulosBloqueoDAO.insertar(tituloBloqueo));
							} else{
								logger.info("Modificando bloqueo de título " + tituloId );							
								// Existe y lo agrega
								tituloBloqueo.setTipoBloqueo(tituloBloqueo.getTipoBloqueo());
								tituloBloqueo.agregarCantidad(cantidadTitulo);
								if (tituloBloqueo.getTituloCustodiaCantidad()>0){
									listaSQL.add(titulosBloqueoDAO.modificar(tituloBloqueo));
								}
							}
						}else if (!cobroEnLinea && hayOperacionesPendientes){
							//long inicioListarBloqueo = System.currentTimeMillis();
							//Es cobro en batch. Se verifica si ya se bloqueó por recompra
							if (!bloqueoPorRecompra){
								logger.info("Bloqueando títulos del cliente por pago ");
								tituloBloqueo = titulosBloqueoDAO.listarBloqueo(tituloId, Long.parseLong(idCliente), TipoBloqueos.BLOQUEO_POR_PAGO, Beneficiarios.BENEFICIARIO_DEFECTO,tipoProductoId);
								
								if (tituloBloqueo.getTipoBloqueo() == null){
									tituloBloqueo.setBeneficiario(Beneficiarios.BENEFICIARIO_DEFECTO);
									tituloBloqueo.setCliente(Long.parseLong(idCliente));
									tituloBloqueo.setTitulo(tituloId);
									tituloBloqueo.setTituloCustodiaCantidad(cantidadTitulo);
									tituloBloqueo.setTipoBloqueo(TipoBloqueos.BLOQUEO_POR_PAGO);
									tituloBloqueo.setTipoProducto(tipoProductoId);
									listaSQL.add(titulosBloqueoDAO.insertar(tituloBloqueo));
								}else{
									logger.info("Modificando por pago bloqueo de título " + tituloId );	
									tituloBloqueo.agregarCantidad(cantidadTitulo);
									if (tituloBloqueo.getTituloCustodiaCantidad()>0){
										listaSQL.add(titulosBloqueoDAO.modificar(tituloBloqueo));
									}
								}
							}
							/*if (logger.isDebugEnabled()){
								logger.debug("Tiempo de procesamiento de listarBloqueo " + (System.currentTimeMillis() - inicioListarBloqueo) + " miliseg ");
							}*/
						}
					}
				} catch (Exception e){
					throw e;
				} finally{
					titulosBloqueoDAO.closeResources();
					titulosBloqueoDAO.cerrarConexion();					
				}
			}
		}
		
		// Creando resultado de consulta final
		// Prepara el retorno
		String[] retorno = new String[listaSQL.size()];
		
		// Recorre la lista y crea un string de consyltas
		for (int i=0; i < listaSQL.size(); i++){
			retorno[i] = listaSQL.get(i).toString();
		}		
		return retorno;		
	}
	
	/**Verifica la recompra*/
	@SuppressWarnings("static-access")
	private void verificarRecompra(int cantidadTitulo, boolean hayOperacionesPendientes) throws Exception{
		//long inicioVerificar = System.currentTimeMillis();
		//Si hay pacto de recompra la cantidad debe ser 0
		if (pct_recompra > 0 && hayOperacionesPendientes){
			logger.info( "Bloqueando título del cliente por recompra");
			//Bloquea los títulos por pacto de recompra
			TituloBloqueo tituloBloqueo = new TituloBloqueo();
			
			tituloBloqueo = titulosBloqueoDAO.listarBloqueo(tituloId, Long.parseLong(idCliente), TipoBloqueos.BLOQUEO_POR_RECOMPRA, Beneficiarios.BENEFICIARIO_DEFECTO,tipoProductoId);				
			if (tituloBloqueo.getTipoBloqueo() == null){
				tituloBloqueo.setBeneficiario(Beneficiarios.BENEFICIARIO_DEFECTO);
				tituloBloqueo.setCliente(Long.parseLong(idCliente));
				tituloBloqueo.setTitulo(tituloId);
				tituloBloqueo.setTituloCustodiaCantidad(cantidadTitulo);								
				tituloBloqueo.setTipoBloqueo(TipoBloqueos.BLOQUEO_POR_RECOMPRA);
				tituloBloqueo.setTipoProducto(tipoProductoId);
				listaSQL.add(titulosBloqueoDAO.insertar(tituloBloqueo));
			}else{
				tituloBloqueo.agregarCantidad(cantidadTitulo);
				if (tituloBloqueo.getTituloCustodiaCantidad()>0){
					listaSQL.add(titulosBloqueoDAO.modificar(tituloBloqueo));
				}
			}
			bloqueoPorRecompra = true;
		}
		/*if (logger.isDebugEnabled()){
			logger.debug("Tiempo de procesamiento de verificarRecompra " + (System.currentTimeMillis() - inicioVerificar) + " miliseg ");
		}*/
	}	
	/**
	 * Función que ingresa a custodia los títulos de la orden recibida
	 * 
	 * @param idOrden id de la orden que se desea enviar a custodia
	 * @return Array de consultas SQL que se deben ejecutar
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	public String[] ingresarCustodia(String idTitulo, long idOrden, boolean cobroEnLinea, boolean hayOperacionesPendientes)throws Exception{
		bloqueoPorRecompra = false;
		financiado = 0;
		listaSQL = new ArrayList<String>();
		if (!cierreEfectuado){//TODO VERIFICAR SI SIEMPRE Q SE USE ESTE METODO YA EL CIERRE SE HABRA EFECTUADO 
			//Insertamos en la tabla de fechas de registro
			String consultasFechaCierre[] = fechasCierresDAO.insertarRegistro();
			
			//Ejecutamos las consultas
			if(consultasFechaCierre!=null){
				db.execBatch(dataSource, consultasFechaCierre);
				logger.debug("Insertada fecha de cierre...");
			}
			cierreEfectuado = true;
		}//TODO DE SER ASÍ ELIMINAR ESTE PASO

		int cantidadTitulo = 0;
		TituloBloqueo tituloBloqueo = new TituloBloqueo();
		
		logger.info("Ingresando a custodia orden número" + idOrden);
		
		/*
		 * Buscamos todos los valores de la orden
		 */
		//long inicioLD = System.currentTimeMillis();
		ordenDAO.listarDatosOrden(idOrden);
		/*if (logger.isDebugEnabled()){
			logger.debug("Tiempo de procesamiento de listadoDeOrden " + (System.currentTimeMillis() - inicioLD) + " miliseg ");
		}*/
		
		DataSet _orden = ordenDAO.getDataSet();
		if(_orden.count()>0){
			_orden.first(); 
			_orden.next();
			/*
			 * Se verifica si la orden es financiada
			 */
		  	financiado = Integer.parseInt(_orden.getValue("ordene_financiado"));
		  	idCliente = _orden.getValue("client_id");
			
			/*
			 * Buscamos los titulos asociados a la orden
			 */
		  	//long inicioLTD = System.currentTimeMillis();
			tituloDAO.listarDatosTituloOrden(String.valueOf(idOrden));
			/*if (logger.isDebugEnabled()){
				logger.debug("Tiempo de procesamiento de listadoDeTitulosOrden " + (System.currentTimeMillis() - inicioLTD) + " miliseg ");
			}*/
			_titulos = tituloDAO.getDataSet();			
			if(_titulos.count()>0){
				_titulos.first();
				try{				
					while(_titulos.next()){
						if(idTitulo.equalsIgnoreCase(_titulos.getValue("titulo_id"))){
							pct_recompra = 0;
							if (_titulos.getValue("titulo_pct_recompra")!=null && !_titulos.getValue("titulo_pct_recompra").equals("")){
								pct_recompra = Double.parseDouble(_titulos.getValue("titulo_pct_recompra"));
							}
							tituloId = _titulos.getValue("titulo_id");
							tipoProductoId = _titulos.getValue("tipo_producto_id");
							String fechaUltPagoCupon = utilitario.obtenerFechaUltimoPagoCuponAmortizacion(tituloId,dataSource);
							cantidadTitulo = Integer.parseInt(_titulos.getValue("titulo_unidades"));//titulo orden					
							
							custodia.setIdCliente(Long.parseLong(idCliente));
							custodia.setIdTitulo(tituloId);
							custodia.setTipoProductoId(tipoProductoId);
							
							//Este if debe ser eliminado
							if(fechaUltPagoCupon==null || fechaUltPagoCupon.equals(""))
								fechaUltPagoCupon = simpleDateFormat.format(new Date());
								
							custodia.setFechaUltimoCupon(simpleDateFormat.parse(fechaUltPagoCupon));
							custodia.setFechaUltimaAmortizacion(simpleDateFormat.parse(fechaUltPagoCupon));
							
							// Verificamos si el cliente posee el título en custodia
							//long inicioVrificar = System.currentTimeMillis();
							custodiaDAO.verificarTitulo(idCliente,tituloId,tipoProductoId);
							/*if (logger.isDebugEnabled()){
								logger.debug("Tiempo de procesamiento de verificarTitulo " + (System.currentTimeMillis() - inicioVrificar) + " miliseg ");
							}*/
							if (custodiaDAO.getDataSet().count()>0){
								//Registramos la entra de los títulos a custodia sólo si no hay pacto de recompra
	
								if (pct_recompra == 0 || (pct_recompra > 0 && hayOperacionesPendientes)){
									custodiaDAO.getDataSet().first();
									custodiaDAO.getDataSet().next();
									logger.info("Cantidad que posee en custodia:"+custodiaDAO.getDataSet().getValue("TITCUS_CANTIDAD"));
									//set la cantidad que posee en custodia
									custodia.setCantidad(Long.parseLong(custodiaDAO.getDataSet().getValue("TITCUS_CANTIDAD")));
									logger.info("Modificando en custodia título " + tituloId );
									logger.info("Cantidad agregada:"+cantidadTitulo);
									custodia.agregarCantidad(cantidadTitulo);
									//long inicioModif = System.currentTimeMillis();
									listaSQL.add(custodiaDAO.modificar(custodia));
									/*if (logger.isDebugEnabled()){
										logger.debug("Tiempo de procesamiento de efectuandoModificacionCustodia " + (System.currentTimeMillis() - inicioModif) + " miliseg ");
									}*/
								}
									
								//Verificamos si tiene recompra para efectuar la salida
								verificarRecompra(cantidadTitulo,hayOperacionesPendientes);	
								
							}else{
								//Registramos la entrada del título
								custodia.setCantidad(0); //Marcamos en 0 porque se usa un solo objeto para todos los clientes
								logger.info("Insertando a custodia título " + tituloId );
								
								if (pct_recompra > 0 && !hayOperacionesPendientes){
									cantidadTitulo = 0;
								}
								
								custodia.agregarCantidad(cantidadTitulo);
								listaSQL.add(custodiaDAO.insertar(custodia));
								
								//Verificamos si tiene recompra para efectuar la salida
								verificarRecompra(cantidadTitulo,hayOperacionesPendientes);
							}
							
				
							// Si la orden es financiado buscamos los títulos y les
							// agregamos la cantidad, sino lo insertamos siempre y cuando el título no tenga recompra
							if(financiado==ConstantesGenerales.VERDADERO){// si es financiada
									// Buscamos los titulos en bloqueo a ver si coinciden
									// con el tipo de bloqueo a establecer y con el tipo de
									// beneficiario
								tituloBloqueo = titulosBloqueoDAO.listarBloqueo(tituloId, Long.parseLong(idCliente), TipoBloqueos.BLOQUEO_FINANCIAMIENTO, Beneficiarios.BENEFICIARIO_DEFECTO,tipoProductoId);							
								if (tituloBloqueo.getTipoBloqueo() == null){
									tituloBloqueo.setBeneficiario(Beneficiarios.BENEFICIARIO_DEFECTO);
									tituloBloqueo.setCliente(Long.parseLong(idCliente));
									tituloBloqueo.setTitulo(tituloId);
									tituloBloqueo.setTituloCustodiaCantidad(cantidadTitulo);								
									tituloBloqueo.setTipoBloqueo(TipoBloqueos.BLOQUEO_FINANCIAMIENTO);
									tituloBloqueo.setTipoProducto(tipoProductoId);
									listaSQL.add(titulosBloqueoDAO.insertar(tituloBloqueo));
								} else{
									logger.info("Modificando bloqueo de título " + tituloId );							
									// Existe y lo agrega
									tituloBloqueo.setTipoBloqueo(tituloBloqueo.getTipoBloqueo());
									tituloBloqueo.agregarCantidad(cantidadTitulo);
									if (tituloBloqueo.getTituloCustodiaCantidad()>0){
										listaSQL.add(titulosBloqueoDAO.modificar(tituloBloqueo));
									}
								}
							}else if (!cobroEnLinea && hayOperacionesPendientes){
								//long inicioListarBloqueo = System.currentTimeMillis();
								//Es cobro en batch. Se verifica si ya se bloqueó por recompra
								if (!bloqueoPorRecompra){
									logger.info("Bloqueando títulos del cliente por pago ");
									tituloBloqueo = titulosBloqueoDAO.listarBloqueo(tituloId, Long.parseLong(idCliente), TipoBloqueos.BLOQUEO_POR_PAGO, Beneficiarios.BENEFICIARIO_DEFECTO,tipoProductoId);
									
									if (tituloBloqueo.getTipoBloqueo() == null){
										tituloBloqueo.setBeneficiario(Beneficiarios.BENEFICIARIO_DEFECTO);
										tituloBloqueo.setCliente(Long.parseLong(idCliente));
										tituloBloqueo.setTitulo(tituloId);
										tituloBloqueo.setTituloCustodiaCantidad(cantidadTitulo);
										tituloBloqueo.setTipoBloqueo(TipoBloqueos.BLOQUEO_POR_PAGO);
										tituloBloqueo.setTipoProducto(tipoProductoId);
										listaSQL.add(titulosBloqueoDAO.insertar(tituloBloqueo));
									}else{
										logger.info("Modificando por pago bloqueo de título " + tituloId );	
										tituloBloqueo.agregarCantidad(cantidadTitulo);
										if (tituloBloqueo.getTituloCustodiaCantidad()>0){
											listaSQL.add(titulosBloqueoDAO.modificar(tituloBloqueo));
										}
									}
								}
								/*if (logger.isDebugEnabled()){
									logger.debug("Tiempo de procesamiento de listarBloqueo " + (System.currentTimeMillis() - inicioListarBloqueo) + " miliseg ");
								}*/
							}
						}//Fin si no es el mismo titulo
					}//Fin While
				} catch (Exception e){
					throw e;
				} finally{
					titulosBloqueoDAO.closeResources();
					titulosBloqueoDAO.cerrarConexion();					
				}
			}
		}
		
		// Creando resultado de consulta final
		// Prepara el retorno
		String[] retorno = new String[listaSQL.size()];
		
		// Recorre la lista y crea un string de consyltas
		for (int i=0; i < listaSQL.size(); i++){
			retorno[i] = listaSQL.get(i).toString();
		}		
		return retorno;		
	}
}

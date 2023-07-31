package com.bdv.infi.logic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import com.bancovenezuela.comun.data.OrdenOperacion;
import com.bdv.infi.dao.MensajeDAO;
import com.bdv.infi.dao.MonedaDAO;
import com.bdv.infi.dao.OperacionDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.TransaccionFijaDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.dao.VehiculoDAO;
import com.bdv.infi.data.Moneda;
import com.bdv.infi.data.Orden;
import com.bdv.infi.data.TransaccionFija;
import com.bdv.infi.data.UnidadInversion;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TransaccionFinanciera;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaz_altair.FactoryAltair;
import com.bdv.infi.util.Utilitario;
import megasoft.AbstractModel;
import megasoft.DataSet;
import megasoft.Logger;
import models.security.userInfo.SecurityDBUserInfo;

/** 
 * Clase que se encarga de la operaci&oacute;n de una transacci&oacute;n espec&iacute;fica
 */
public abstract class CancelacionOrden extends AbstractModel {

	private boolean vehiculoTomadorBDV = false;
	
	private Orden ordenVehiculo = new Orden();	
		
	private boolean ejecutarAltair = false; //Indica si se debe o no invocar a ALTAIR			
	
	private com.bdv.infi.dao.Transaccion transaccion = null;
	
	/**Indica si se aplico el commit a la transacci&oacute;n cuando el reverso no aplica*/
	private boolean aplicoCommit = false;
	
	public boolean cancelarOrden(Orden ordenCancelar) throws Exception{
		
		boolean OK = true;		
		
		if(!ordenCancelar.getStatus().equals(StatusOrden.CANCELADA)){
			
			try {
				transaccion = new com.bdv.infi.dao.Transaccion(_dso);
				transaccion.begin();			
				Logger.info(this,"EMPEZANDO EL PROCESO DE CANCELACIÓN DE LA OPRDEN NRO "+ ordenCancelar.getIdOrden());
				OperacionDAO operacionDAO = new OperacionDAO(_dso);
				OrdenDAO ordenDAO = new OrdenDAO(_dso);
				UnidadInversionDAO unidadDAO = new UnidadInversionDAO(_dso);
				Orden orden = new Orden();	
				VehiculoDAO vehDAO = new VehiculoDAO(_dso);				
				ArrayList<OrdenOperacion> operacionesOrdenCancelar = new ArrayList<OrdenOperacion>();
				operacionesOrdenCancelar = ordenCancelar.getOperacion();
				
				//Verificar si vehiculo tomador de la orden a cancelar es BDV o es una orden de Cartera propia (no aplican operaciones al vehiculo)--
				if(vehDAO.vehiculoEsBDV(ordenCancelar.getVehiculoTomador()) || ordenCancelar.isCarteraPropia()){
					vehiculoTomadorBDV = true;				
				}else{					
					//obtener la orden del vehiculo tomador de la orden a cancelar
					ordenVehiculo = obtenerOrdenVehiculoTomador(ordenCancelar);
					Logger.info(this,"ORDEN DEL VEHICULO OBTENIDA NRO: "+ordenVehiculo.getIdOrden());
				}
				
				Logger.info(this,"¿VEHICULO BDV?: "+vehiculoTomadorBDV);
				Logger.info(this,"¿ORDEN DE CARTERA PROPIA?: "+ ordenCancelar.isCarteraPropia());
				//---------------------------------------------------------------
				
				UnidadInversion unidad = new UnidadInversion();				
			
				String status = null;//estatus de la operacion
				String transaccionFinanciera = null;//Tipo de transaccion financiera					
				
				if(!operacionesOrdenCancelar.isEmpty()){					
					//Obtenemos la orden						
				   	//Recorro DATASET de operaciones de la orden encontradas
					for(int i = 0; i<operacionesOrdenCancelar.size(); i++){					
						OrdenOperacion operacionCancelar = operacionesOrdenCancelar.get(i);
						OrdenOperacion operacion = new OrdenOperacion();
						
						String oper= String.valueOf(operacionCancelar.getIdOperacion());//String.valueOf(_ordenOperacion.getValue("ordene_operacion_id"));
						status = operacionCancelar.getStatusOperacion(); ///String.valueOf(_ordenOperacion.getValue("status_operacion"));
						
						if(status.equals(ConstantesGenerales.STATUS_EN_ESPERA) || status.equals(ConstantesGenerales.STATUS_RECHAZADA)){
							operacionDAO.modificarOperacion(ConstantesGenerales.STATUS_CANCELADA, Long.parseLong(oper));
						}else if(status.equals(ConstantesGenerales.STATUS_APLICADA)){
							transaccionFinanciera = operacionCancelar.getTipoTransaccionFinanc(); //_ordenOperacion.getValue("trnf_tipo");																
													
							if(transaccionFinanciera.equals(TransaccionFinanciera.DEBITO)){
								Logger.info(this,"ENCONTRADA OPERACION DE DEBITO PARA EL CLIENTE EN LA ORDEN A CANCELAR...");
								//se acumula el monto debitado para post5eriormente crear la operacion de reintegro de dinero (credito)
								crearOperacionReintegro(orden, operacionCancelar, ordenCancelar);
								ejecutarAltair = true;			
								
							}else if (transaccionFinanciera.equals(TransaccionFinanciera.BLOQUEO)){
								Logger.info(this,"ENCONTRADA OPERACION DE BLOQUEO PARA EL CLIENTE EN LA ORDEN A CANCELAR...");
								//Genereamos una nueva operacion con los mismos datos... solo se modificaran el status en espera, la fecha de la operacion a sysdate y el tipo de transaccion sera Desbloqueo
								this.crearOperacion(operacion, operacionCancelar);
								Logger.info(this,"CREADA OPERACION DE DESBLOQUEO PARA EL CLIENTE..");
								operacion.setTipoTransaccionFinanc(TransaccionFinanciera.DESBLOQUEO);
								operacion.setNumeroRetencion(operacionCancelar.getNumeroRetencion());													
								orden.agregarOperacion(operacion);
								ejecutarAltair = true;
							}//fin if transaccion
						}//fin else if estatus
					}//fin for count operaciones			
				}//fin if count _ordenOperacion
		
				//----Obtener parametros de archivo de inicio guardado en sesion
				String nroOficina = null;
				if(_req.getSession().getAttribute(ConstantesGenerales.CODIGO_SUCURSAL)!=null){
					nroOficina = (String)_req.getSession().getAttribute(ConstantesGenerales.CODIGO_SUCURSAL);
				}else{
					Logger.info(this, "NO SE ENCUENTRA EL NUMERO DE OFICINA EN LA SESION");
					throw new Exception("No se encuentra el n&uacute;mero de oficina");									
				}
				//--------------------------------------------------------------------
				
				orden.setStatus(StatusOrden.REGISTRADA);
				orden.setIdOrdenRelacionada(ordenCancelar.getIdOrden());
				orden.setFechaCancelacion(new Date());	
				orden.setIdTransaccion(TransaccionNegocio.CANCELACION_ORDEN);
				orden.setIdBloter(this.buscarIdBlotterUsuario());
				orden.setTerminal(_req.getRemoteAddr());
				orden.setIdCliente(ordenCancelar.getIdCliente());
				orden.setIdUnidadInversion(ordenCancelar.getIdUnidadInversion());
				orden.setNombreUsuario(getUserName());
				orden.setFechaValor(new Date());
				orden.setSucursal(nroOficina);
				orden.setVehiculoColocador(ordenCancelar.getVehiculoColocador());
				orden.setVehiculoRecompra(ordenCancelar.getVehiculoRecompra());
				orden.setVehiculoTomador(ordenCancelar.getVehiculoTomador());
				
				orden.setOrdenTitulo(ordenCancelar.getOrdenTitulo());
											
				String[] consulta = ordenDAO.insertar(orden);
				ordenCancelar.setIdOrdenRelacionada(orden.getIdOrden());									
				//---Ejecutar querys de inserción de la orden de cancelacion
				transaccion.ejecutarConsultas(consulta);
				//---------------------------------------------				
				
				Logger.info(this,"CANTIDAD DE OPERACIONES EN LA ORDEN DE CANCELACION : "+ orden.getOperacion().size());

				Logger.info(this,"CANTIDAD DE OPERACIONES NUEVAS EN LA ORDEN DEL VEHICULO : "+ ordenVehiculo.getOperacion().size());
				
				//cambiar el estatus a la orden q se esta cancelando y colocar fecha de cancelacion
				ordenCancelar.setStatus(StatusOrden.CANCELADA);
				ordenCancelar.setFechaCancelacion(new Date());
				String[] updatesOrdenCancelar = ordenDAO.modificar(ordenCancelar);				
				//---Ejecutar querys de modificacion de la de la orden cancelada
				transaccion.ejecutarConsultas(updatesOrdenCancelar);
				//---------------------------------------------
								
				//--Eliminar Mensajes que puedan estar asociados a la orden Y que NO hayan sido enviados---
				eliminarMensajesInterfaces(ordenCancelar);
				//-----------------------------------------------------------------------------------------
				
				//---Ejecutar querys de inserción de nuevas operaciones de debito  a la orden del vehiculo
				if(!vehiculoTomadorBDV){
					
					//--Crear operaciones de debito para el vehiculo en caso de ser necesario
					Logger.info(this,"CREANDO OPERACIONES DE DEBITO PARA EL VEHICULO : "+ ordenCancelar.getVehiculoTomador());
					
					crearDebitosVehiculoTomador(orden, ordenCancelar);
		
					if(!ordenVehiculo.getOperacion().isEmpty()){
						Logger.info(this,"INSERTANDO OPERACIONES DE DEBITO PARA EL VEHICULO TOMADOR");
						String sentenciasOperVehiculo[] = ordenDAO.modificar(ordenVehiculo);					
						transaccion.ejecutarConsultas(sentenciasOperVehiculo);
					}
					//---------------------------------------------
				}
				
				//proceso para verificar si la Unidad de inversion asociada a la orden es de tipo inventario
				BigDecimal monto_pedido = new BigDecimal(ordenCancelar.getMonto());
				unidadDAO.listarPorId(ordenCancelar.getIdUnidadInversion());
				DataSet _inf = unidadDAO.getDataSet();
				if(_inf.next()){
					String instrumento = _inf.getValue("insfin_forma_orden");
					String disponible = (String)_inf.getValue("undinv_umi_inv_disponible");
					BigDecimal monto_disponible = new BigDecimal(disponible);
					if(instrumento.equals(ConstantesGenerales.INST_TIPO_INVENTARIO) || instrumento.equals(ConstantesGenerales.INST_TIPO_INVENTARIO_CON_PRECIO)){
						BigDecimal nuevoMontoDisponible= monto_pedido.add(monto_disponible);
						unidad.setTotalInversion(nuevoMontoDisponible);
						unidad.setIdUnidadInversion(ordenCancelar.getIdUnidadInversion());
						String query = unidadDAO.modificarMontoDisponible(unidad);
						transaccion.ejecutarConsultas(query);
					}
				}
				
				//Aplicar transacciones financieras de la nueva orden
														
			    try {
	
					//Busca los documentos asociados a la cancelacion
					//orden = ordenDAO.listarOrden(orden.getIdOrden());
					ProcesarDocumentos procesarDocumentos = new ProcesarDocumentos(_dso);
					orden.setIdTransaccion(TransaccionNegocio.CANCELACION_ORDEN);
					procesarDocumentos.procesar(orden,this._app,_req.getRemoteAddr());
					String documentosCancelacion[] = ordenDAO.insertarDocumentosSQL(orden);
					if(documentosCancelacion!=null){
						transaccion.ejecutarConsultas(documentosCancelacion);
			    	}			
										
				} catch (Exception e3) {	
					Logger.error(this,"ERROR: "+e3.getMessage()+" "+ Utilitario.stackTraceException(e3));
					transaccion.rollback();
					throw new Exception("Error insertando los documentos de cancelaci&oacute;n de la orden n&uacute;mero "+ ordenCancelar.getIdOrden());		
				}

				if (ejecutarAltair){					
					FactoryAltair factoryAltair = new FactoryAltair(transaccion, _app, true);
					factoryAltair.nombreUsuario = getUserName();
					factoryAltair.ipTerminal = _req.getRemoteAddr();					

					try {
						//Ejecutar en altair las operaciones del cliente para la orden de cancelacion
						Logger.info(this,"LLAMANDO AL PROCESO DE ENVIO DE OPERACIONES DEL CLIENTE A ALTAIR PARA LA ORDEN DE CANCELACION NRO "+orden.getIdOrden());
						Logger.info(this,"CANTIDAD DE OPERACIONES EN LA ORDEN DE CANCELACION : "+ orden.getOperacion().size());
						factoryAltair.aplicarOrdenes(orden.getOperacion());
						
						//Ejecutar operaciones del vehiculo
						if(!vehiculoTomadorBDV && !ordenVehiculo.getOperacion().isEmpty()){
							Logger.info(this,"LLAMANDO AL PROCESO DE ENVIO DE OPERACIONES DEL VEHICULO A ALTAIR PARA LA ORDEN DEL VEHICULO NRO "+ordenVehiculo.getIdOrden());
							factoryAltair.aplicarOrdenes(ordenVehiculo.getOperacion());
						}
					} catch (Exception e) {	
						Logger.error(this,"ERROR: "+e.getMessage()+" "+ Utilitario.stackTraceException(e));
					} catch (Throwable e) {	
						Logger.error(this,"ERROR: "+e.getMessage()+" "+ Utilitario.stackTraceException(e));
					}										
					
					//--Si alguna de las operaciones es rechazada o quedo en espera... eliminar orden
					if(operacionesRechazadas(orden.getOperacion()) || ((!vehiculoTomadorBDV) && operacionesRechazadas(ordenVehiculo.getOperacion())) || operacionesEnEspera(orden.getOperacion()) || operacionesEnEspera(ordenVehiculo.getOperacion())){
						
						//Reverso de operaciones del vehiculo en caso de ser rechazada alguna
						if(!vehiculoTomadorBDV && operacionesRechazadas(ordenVehiculo.getOperacion())){
							Logger.info(this,"HAY OPERACIONES DEL VEHICULO RECHAZADAS...");
							try{
								Logger.info(this,"VERIFICANDO SI SE DEBE HACER REVERSO OPERACIONES VEHICULO...");
								//--Hacer el reverso a todas la operaciones que hayan sido aplicadas en altair						
								hacerReversoOperaciones(ordenVehiculo.getOperacion(), factoryAltair);
							} catch (Exception e){
								Logger.error(this,"ERROR: "+e.getMessage()+" "+ Utilitario.stackTraceException(e));
								transaccion.end(); //Se hace commit
								aplicoCommit = true;								
								throw new Exception("Error aplicando el reverso de las operaciones de d&eacute;bito del veh&iacute;culo.");								
							
							} catch (Throwable e){
								Logger.error(this,"ERROR: "+e.getMessage()+" "+ Utilitario.stackTraceException(e));
								transaccion.end(); //Se hace commit
								aplicoCommit = true;								
								throw new Exception("Error aplicando el reverso de las operaciones de d&eacute;bito del veh&iacute;culo.");								
							}


						}
						
						//Hacer reverso de operaciones de la orden en caso 
						//de rechazo de operaciones del cliente o del vehiculo					
						try{
							Logger.info(this,"VERIFICANDO SI SE DEBE HACER REVERSO OPERACIONES DEL CLIENTE PARA LA ORDEN DE CANCELACION...");
							//--Hacer el reverso a todas la operaciones que hayan sido aplicadas en altair						
							hacerReversoOperaciones(orden.getOperacion(), factoryAltair);
						} catch (Exception e){
							Logger.error(this,"ERROR: "+e.getMessage()+" "+ Utilitario.stackTraceException(e));
							transaccion.end(); //Se hace commit
							aplicoCommit = true;
							throw new Exception("Error aplicando el reverso de las operaciones de cr&eacute;dito del cliente para la cancelaci&oacute;n de la orden n&uacute;mero "+ ordenCancelar.getIdOrden());
						} catch (Throwable e){
							Logger.error(this,"ERROR: "+e.getMessage()+" "+ Utilitario.stackTraceException(e));
							transaccion.end(); //Se hace commit
							aplicoCommit = true;
							throw new Exception("Error aplicando el reverso de las operaciones de cr&eacute;dito del cliente para la cancelaci&oacute;n de la orden n&uacute;mero "+ ordenCancelar.getIdOrden());
						}	
						
						Logger.info(this,"OPERACIONES RECHAZADAS O EN ESPERA..LANZANDO EXCEPCION DE ERROR DE OPERACIONES A ALTAIR..."); 
						throw new Exception("Error aplicando las operaciones en ALTAIR para la cancelaci&oacute;n de la orden.");						
					}else{
						Logger.info(this,"CANCELADA CON EXITO LA ORDEN NRO "+ordenCancelar.getIdOrden()+". ORDEN DE CANCELACION RELACIONADA FUE INGRESADA CORRECTAMENTE BAJO EN NRO "+orden.getIdOrden());
						transaccion.end(); //se hace commit
						aplicoCommit = true;
					}							

				}else{
					//Cancelacion de ordenes con operaciones miscelaneas: no se ejutan transacciones en altair
					Logger.info(this,"CANCELADA CON EXITO LA ORDEN NRO "+ordenCancelar.getIdOrden()+". ORDEN DE CANCELACION RELACIONADA FUE INGRESADA CORRECTAMENTE BAJO EN NRO "+orden.getIdOrden());
					transaccion.end(); //se hace commit
					aplicoCommit = true;
				}
				
			} catch (Exception e) {
				OK = false;
				Logger.error(this,"ERROR: "+e.getMessage()+" "+ Utilitario.stackTraceException(e));
				
				//Verifica si aplico commit para efectuar roolback
				if (!aplicoCommit){
					transaccion.rollback();
				}				
				throw e;			
			
			}finally{
				if(transaccion!=null){
					transaccion.closeConnection();
				}
			}
			
		}else{
			throw new Exception("La orden n&uacute;mero " +ordenCancelar.getIdOrden()+ " ya ha sido cancelada.");
		}		
		
		return OK;		
	}


	/**
	 * Verifica si se deben crear operaciones de debito para el vehiculo tomador y las agrega a la orden del vehiculo
	 * @param orden
	 * @param ordenCancelar
	 * @throws Exception
	 */
	private void crearDebitosVehiculoTomador(Orden orden, Orden ordenCancelar) throws Exception {
		
		ArrayList<OrdenOperacion> listaOperacionesOrdenCancelacion = orden.getOperacion();
		//Logger.info(this, "VERIFICANDO SI SE DEBEN CREAR OPERACIONES DE DEBITO PARA EL VEHICULO TOMADOR");
		for(int i=0; i< listaOperacionesOrdenCancelacion.size(); i++){
			OrdenOperacion operacionCancelacion = listaOperacionesOrdenCancelacion.get(i);
			
			if(operacionCancelacion.getTipoTransaccionFinanc().equals(TransaccionFinanciera.CREDITO)){
				//Logger.info(this, "CREANDO OPERACION DE DEBITO PARA EL VEHICULO TOMADOR");
				//----Crear operacion de debito para el vehiculo en caso de no ser BDV					
				crearOperacionDebitoVehiculo(operacionCancelacion, ordenCancelar);			
				//----------------------------------------------------------------
			}
		}		
		
	}

	/**
	 * Obtiene la orden asociada del veh&iacute;culo tomador de la orden a cancelar
	 * @param ordenCancelar
	 * @return
	 * @throws Exception
	 */
	private Orden obtenerOrdenVehiculoTomador(Orden ordenCancelar) throws Exception {
	
		OrdenDAO ordenDAO = new OrdenDAO(_dso);
		VehiculoDAO vehiculoDAO = new VehiculoDAO(_dso);
		com.bdv.infi.dao.ClienteDAO clienteDAO = new com.bdv.infi.dao.ClienteDAO(_dso);
		long idClienteVehiculo = -1;
		Orden ordenVeh = new Orden();
		
		//---obtener el rif del Vehiculo Tomador de la orden--------------
		String rifVehiculo = vehiculoDAO.obtenerRifVehiculo(ordenCancelar.getVehiculoTomador());
		//Obtener rif Numerico
		String rifNumerico = Utilitario.obtenerNumeroRifCI(rifVehiculo);	
		rifVehiculo = rifNumerico;

		//----Obtener idCliente del Vehiculo		
		try{
			clienteDAO.listarPorCedRif(Long.parseLong(rifVehiculo));		
		}catch(NumberFormatException nfe){	
			throw new Exception("El rif del veh&iacute;culo tomador encontrado ('"+ rifVehiculo +"') no es un n&uacute;mero valido.");
		}		
		if(clienteDAO.getDataSet().next())			
			idClienteVehiculo = Long.parseLong(clienteDAO.getDataSet().getValue("client_id"));
		//---------------------------------------------------------------------------------------

		//--Obtener la orden del vehiculo para la unidad de inversion seleccionada
		long idOrdenVehiculo = ordenDAO.verificarOrdenVehiculoUI(idClienteVehiculo, ordenCancelar.getIdUnidadInversion());
		
		//Listar orden vehiculo
		ordenVeh = ordenDAO.listarOrden(idOrdenVehiculo, false, false, false, false, false);	
		//Limpiar lista de operaciones ejecutadas anteriormente para el vehiculo y UI
		//Guardar solo las nuevas operaciones a ejecutar
		ArrayList<OrdenOperacion> operacionesVeh = new ArrayList<OrdenOperacion>();
		ordenVeh.setOperacion(operacionesVeh);		
		
		return ordenVeh;
	}

	/**
	 * Verifica si quedaron operaciones en espera para la orden
	 * @param listaOperaciones
	 * @return true si hay operaciones rechazadas, false en caso
	 */
	private boolean operacionesEnEspera(ArrayList<OrdenOperacion> listaOperaciones) {
		
		if(listaOperaciones!=null && !listaOperaciones.isEmpty()){	
			for(int k = 0; k < listaOperaciones.size(); k++){
				OrdenOperacion operacion = (OrdenOperacion) listaOperaciones.get(k);
				
				if(operacion.getStatusOperacion().equals(ConstantesGenerales.STATUS_EN_ESPERA)){
					Logger.info(this,"ENCONTRADA UNA OPERACION EN ESPERA... DE TIPO: "+operacion.getTipoTransaccionFinanc());
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * Verifica si hay operaciones rechazadas en el intento de aplicar las operaciones en altair
	 * @param listaOperaciones
	 * @return true si hay operaciones rechazadas, false en caso
	 */
	private boolean operacionesRechazadas(ArrayList<OrdenOperacion> listaOperaciones) {
		
		if(listaOperaciones!=null && !listaOperaciones.isEmpty()){	
			for(int k = 0; k < listaOperaciones.size(); k++){
				OrdenOperacion operacion = (OrdenOperacion) listaOperaciones.get(k);
				
				if(operacion.getStatusOperacion().equals(ConstantesGenerales.STATUS_RECHAZADA)){
					Logger.info(this,"ENCONTRADA UNA OPERACION RECHAZADA... DE TIPO: "+operacion.getTipoTransaccionFinanc());
					return true;
				}
			}
		}
		return false;
	}
	
	
	/**
	 * Hacer el reverso de las operaciones fallidas 
	 * @param listaOperaciones
	 * @param factoryAltair
	 * @throws Throwable
	 */
	private void hacerReversoOperaciones(ArrayList<OrdenOperacion> listaOperaciones, FactoryAltair factoryAltair) throws Throwable {
		Logger.info(this,"Entrando al reverso...");		
		//verificar operaciones fallidas de la orden
		//verificar operaciones aplicadas
		if(listaOperaciones!=null && !listaOperaciones.isEmpty()){
			for(int k = 0; k < listaOperaciones.size(); k++){
				OrdenOperacion operacion = (OrdenOperacion) listaOperaciones.get(k);
				//--si la operacion fue aplicada
				Logger.info(this,"Status de la operación con código " + operacion.getCodigoOperacion());
				Logger.info(this,"NIO " + operacion.getNumeroMovimiento());
				Logger.info(this,"TIPO " + operacion.getTipoTransaccionFinanc());			
				Logger.info(this,"Status " + operacion.getStatusOperacion());
				if(operacion.getStatusOperacion().equals(ConstantesGenerales.STATUS_APLICADA)){				
					factoryAltair.aplicarReverso(operacion);//--aplicar reverso de operacion aplicada
					
				}
			}	
		}
	}

	
	/**
	 * Crea una operacion de reintegro por cancelaci&oacute;n de la orden
	 * @param orden
	 * @param montoAcreditado
	 * @throws Exception
	 */
	private void crearOperacionReintegro(Orden orden, OrdenOperacion operacionCancelar, Orden ordenCancelar) throws Exception {
	
		OrdenOperacion operacionCredCliente = new OrdenOperacion();		
		TransaccionFija transaccionFija = new TransaccionFija();
		TransaccionFijaDAO trnfFijaDAO = new TransaccionFijaDAO(_dso);
	
		//obtener la transaccion fija para el reintegro
		transaccionFija = trnfFijaDAO.obtenerTransaccion(Integer.parseInt(operacionCancelar.getIdTransaccionFinanciera()), ordenCancelar.getVehiculoTomador(),ordenCancelar.getInstrumentoId());

		operacionCredCliente.setMontoOperacion(operacionCancelar.getMontoOperacion());
		operacionCredCliente.setTasa(new BigDecimal(100));
		//Operacion de Reintegro
		operacionCredCliente.setInComision(0);
		operacionCredCliente.setNombreOperacion(transaccionFija.getNombreTransaccion());
		operacionCredCliente.setTipoTransaccionFinanc(TransaccionFinanciera.CREDITO);
		operacionCredCliente.setStatusOperacion(ConstantesGenerales.STATUS_EN_ESPERA);
		operacionCredCliente.setNumeroCuenta(operacionCancelar.getNumeroCuenta());
		operacionCredCliente.setIdMoneda(operacionCancelar.getIdMoneda());
		operacionCredCliente.setSiglasMoneda(operacionCancelar.getIdMoneda());
		operacionCredCliente.setCodigoOperacion(transaccionFija.getCodigoOperacionCteCre());
		operacionCredCliente.setIdTransaccionFinanciera(operacionCancelar.getIdTransaccionFinanciera());
		//agregar la operacion a la orden
		
		//Logger.info(this,"GENERANDO OPERACION DE CREDITO PARA EL CLIENTE..");
		orden.agregarOperacion(operacionCredCliente);		
	}

	private void crearOperacionDebitoVehiculo(OrdenOperacion operacion, Orden ordenCancelar) throws Exception {
		
		OrdenOperacion operacionDebitoVehiculo = new OrdenOperacion();			
		TransaccionFija transaccionFija = new TransaccionFija();
		TransaccionFijaDAO trnfFijaDAO = new TransaccionFijaDAO(_dso);

		//obtener la transaccion fija para el reintegro
		transaccionFija = trnfFijaDAO.obtenerTransaccion(Integer.parseInt(operacion.getIdTransaccionFinanciera()), ordenCancelar.getVehiculoTomador(),ordenCancelar.getInstrumentoId());

		//Crear operacion de debito para el vehiculo
		operacionDebitoVehiculo.setMontoOperacion(operacion.getMontoOperacion());
		operacionDebitoVehiculo.setTasa(new BigDecimal(100));
		operacionDebitoVehiculo.setInComision(0);
		operacionDebitoVehiculo.setNombreOperacion(transaccionFija.getNombreTransaccion());
		operacionDebitoVehiculo.setTipoTransaccionFinanc(TransaccionFinanciera.DEBITO);
		operacionDebitoVehiculo.setStatusOperacion(ConstantesGenerales.STATUS_EN_ESPERA);		
		operacionDebitoVehiculo.setIdMoneda(operacion.getIdMoneda());
		operacionDebitoVehiculo.setSiglasMoneda(operacion.getIdMoneda());
		operacionDebitoVehiculo.setCodigoOperacion(transaccionFija.getCodigoOperacionVehDeb());	
		operacionDebitoVehiculo.setIdTransaccionFinanciera(operacion.getIdTransaccionFinanciera());
		operacionDebitoVehiculo.setIdOperacionRelacion(operacion.getIdOperacion());
		
		
		//---Buscar numero de cuenta vehiculo
		VehiculoDAO  vehiculoDAO = new VehiculoDAO(_dso);
		vehiculoDAO.listarPorId(ordenCancelar.getVehiculoTomador());
		String numeroCuentaVehiculo = null;
		
		if(vehiculoDAO.getDataSet().next()){
			numeroCuentaVehiculo = vehiculoDAO.getDataSet().getValue("vehicu_numero_cuenta");
		}

		if(numeroCuentaVehiculo==null){
			throw new Exception("No se encuentra el n&uacute;mero de cuenta asociado al veh&iacute;culo tomador de la orden.");
		}
		//--------------------------------
	
		operacionDebitoVehiculo.setNumeroCuenta(numeroCuentaVehiculo);
		
		//-------------------------------------------------------------------
						
		Logger.info(this,"AGREGANDO OPERACION DE DEBITO PARA EL VEHICULO...");
		//agregar operacion nueva de Debito a la orden del vehiculo
		ordenVehiculo.agregarOperacion(operacionDebitoVehiculo);
		
	}
	
	/**
	 * Busca el blotter asociado al usuario conectado y coloca los valores a mostrar el la vista
	 * @throws Exception
	 */
	public String buscarIdBlotterUsuario() throws Exception{
		
		SecurityDBUserInfo userInfo = new SecurityDBUserInfo(_dso);
		String idBlotter = userInfo.getUserBlotterId(getUserName());
		
		return idBlotter;

	}
	/**
	 * Crea una nueva operacion para la orden de cancelaci&oacute;n
	 * @param operacion
	 * @param _ordenOperacion
	 * @throws Exception
	 */
	public void crearOperacion(OrdenOperacion operacion, OrdenOperacion operacionCancelar) throws Exception{
		
		MonedaDAO monedaDao = new MonedaDAO(_dso);
		Moneda moneda = null;		
		
		try{ 
			operacion.setIdTransaccionFinanciera(operacionCancelar.getIdTransaccionFinanciera());
			operacion.setStatusOperacion(ConstantesGenerales.STATUS_EN_ESPERA);
			operacion.setAplicaReverso(operacionCancelar.isAplicaReverso());
			operacion.setMontoOperacion(operacionCancelar.getMontoOperacion());
			operacion.setTasa(operacionCancelar.getTasa());
			operacion.setErrorNoAplicacion(operacionCancelar.getErrorNoAplicacion());
			operacion.setSerialContable(operacionCancelar.getSerialContable());
			operacion.setInComision(operacionCancelar.getInComision());
			operacion.setIdMoneda(operacionCancelar.getIdMoneda());
			operacion.setNombreOperacion(operacionCancelar.getNombreOperacion());
			
			//Busca las siglas de la moneda				
			if (monedaDao.listarPorId(operacion.getIdMoneda())){
				moneda = (Moneda) monedaDao.moveNext();
				operacion.setSiglasMoneda(moneda.getSiglas());
			}else{
				throw new Exception("No hay tasa de cambio definida para la moneda");
			}		
			
			operacion.setNumeroCuenta(operacionCancelar.getNumeroCuenta());
			operacion.setNombreReferenciaCuenta(operacionCancelar.getNombreReferenciaCuenta());
			operacion.setNombreBanco(operacionCancelar.getNombreBanco());
			operacion.setDireccionBanco(operacionCancelar.getDireccionBanco());
			operacion.setCodigoSwiftBanco(operacionCancelar.getCodigoSwiftBanco());
			operacion.setCodigoBicBanco(operacionCancelar.getCodigoBicBanco());
			operacion.setTelefonoBanco(operacionCancelar.getTelefonoBanco());
			operacion.setCodigoABA(operacionCancelar.getCodigoABA());
			operacion.setPaisBancoCuenta(operacionCancelar.getPaisBancoCuenta());
			operacion.setNombreBancoIntermediario(operacionCancelar.getNombreBancoIntermediario());
			operacion.setDireccionBancoIntermediario(operacionCancelar.getDireccionBancoIntermediario());
			operacion.setCodigoSwiftBancoIntermediario(operacionCancelar.getCodigoSwiftBancoIntermediario());
			operacion.setCodigoBicBancoIntermediario(operacionCancelar.getCodigoBicBancoIntermediario());
			operacion.setTelefonoBancoIntermediario(operacionCancelar.getTelefonoBancoIntermediario());
			operacion.setCodigoABAIntermediario(operacionCancelar.getCodigoABAIntermediario());
			operacion.setPaisBancoIntermediario(operacionCancelar.getPaisBancoIntermediario());
			operacion.setFechaProcesada(new Date());
		} catch (Exception e) {
			throw e;			
		} finally{
			monedaDao.closeResources();
			monedaDao.cerrarConexion();
		}

	}
	
	/**
	 * Ejecuta las sentencias para eliminar los mensajes de interfaces con los sistemas 
	 * Estadística, Carmen o Bcv que se hayan generado y NO hayan sido enviados
	 * @param orden
	 * @throws Exception
	 */
	public void eliminarMensajesInterfaces(Orden orden) throws Exception{
			
		MensajeDAO mensajeDAO = new MensajeDAO(_dso);		
		String sentenciaElim = mensajeDAO.eliminarMensajesPorIdOrden(Integer.parseInt(String.valueOf(orden.getIdOrden())));
		
		transaccion.ejecutarConsultas(sentenciaElim);
	}

}
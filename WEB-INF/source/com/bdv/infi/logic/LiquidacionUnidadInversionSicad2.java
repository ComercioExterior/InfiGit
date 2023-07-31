package com.bdv.infi.logic;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import javax.sql.DataSource;

import megasoft.DataSet;
import megasoft.Logger;

import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.OrdenesCrucesDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.dao.VehiculoDAO;
import com.bdv.infi.data.Orden;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.PlantillasMailTipos;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaz_varias.CallEnvioCorreos;
import com.bdv.infi.logic.interfaz_varias.MensajeCarmen;
import com.bdv.infi.util.Utilitario;

public class LiquidacionUnidadInversionSicad2  extends AbstractLiquidacion implements Runnable {
	private VehiculoDAO vehiculoDAO;
	private UnidadInversionDAO unidadInversionDAO;
	private String unidadInversionId;
	private String tipoProducto;
	private IngresoMasivoCustodia ingresoCustodia;
	private OrdenesCrucesDAO ordenesCrucesDAO;
	
	public LiquidacionUnidadInversionSicad2(DataSource _dso, int usuario, String ipEquipo, String sucursal, String[] unidades, String nmUsuario, String producto) throws Exception {
		super(_dso, usuario, ipEquipo, sucursal, nmUsuario);
		unidadInversionId=unidades[0];
		vehiculoDAO = new VehiculoDAO(_dso);
		unidadInversionDAO = new UnidadInversionDAO(_dso);
		ingresoCustodia = new IngresoMasivoCustodia(_dso);
		ordenesCrucesDAO = new OrdenesCrucesDAO(_dso);
		ordenDAO = new OrdenDAO(_dso);
		tipoProducto = producto;		
	}

	@Override
	void liquidarUnidadInversion() throws Exception {
		String vehiculoID=null;
		Logger.info(this,"*************** INICIO DE PROCESO DE LIQUIDACION "+tipoProducto+"***************");
		
		//INCIAR PROCESO
		iniciarProceso();
		
		//VALIDAR FECHA DE LIQUIDACIÓN
		SimpleDateFormat formato = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA_LIQUIDACION);
		unidadInversionDAO = new UnidadInversionDAO(_dso);
		unidadInversionDAO.listarDatosUIPorId(Long.parseLong(unidadInversionId));
		if (unidadInversionDAO.getDataSet().next()) {			
			Date unidinvFeLiquidacionHora1 = formato.parse(unidadInversionDAO.getDataSet().getValue("fecha1"));
			Logger.debug(this,"fecha liquidacion: "+unidinvFeLiquidacionHora1+", fecha actual: "+fechaActual);		
			
			if(unidinvFeLiquidacionHora1.compareTo(fechaActual)<=0){
				//OBTENER VEHICULO				
				vehiculoDAO.obtenerVehiculoBDV();	
				if(vehiculoDAO.getDataSet().count()>0){
					vehiculoDAO.getDataSet().first();
					vehiculoDAO.getDataSet().next();
					vehiculoID=vehiculoDAO.getDataSet().getValue("VEHICU_ID");
				}
				
				//OBTENER CODIGOS DE OPERACION (TRANSACCIONES FIJAS)	
				if(obtenerCodigosOperacion(vehiculoID,unidadInversionId)){
					
					//CREAR OPERACION DE CREDITO AL BCV
					//crearOrdenCreditoBCV(unidadInversionId,tipoProducto);
					
					//LIQUIDAR ORDENES ()
					liquidarOrdenes(StatusOrden.CRUZADA,unidadInversionId,tipoProducto, true);
					
					//LIQUIDAR UNIDAD DE INVERSION
					cambioEstatusUnidadInversion(StatusOrden.CRUZADA,unidadInversionId,tipoProducto);
				}else{
					proceso.setDescripcionError("No se encuentran configurados los códigos de operación COD_OPERACION_CTE_CRE para el vehículo e instrumento financiero (TOMA_ORDEN_CTA_DOLARES,DEPOSITO_BCV)");
					Logger.info(this, "No se encuentran configurados los códigos de operación COD_OPERACION_CTE_CRE para el vehículo e instrumento financiero (TOMA_ORDEN_CTA_DOLARES,DEPOSITO_BCV)");
				}
				
			}else
			{
				proceso.setDescripcionError("La fecha de liquidación de la unidad de inversion es mayor a la fecha actual");
				Logger.info(this, "La fecha de liquidación de la unidad de inversion es mayor a la fecha actual");
			}		
		}	
		
		//FINALIZAR PROCESO		
		finalizarProceso();		
		
	}
	void liquidarOrdenes(String estatusOrdenLiquidar,String unidadInversion, String tipoProducto, boolean custodiaTitulos) throws Exception {	
		Date fechaValor=null;
		Date fechaTitulo=null;
		String fechaValorString=null;
		BigDecimal montoOperacion;
		DataSet ordenesLiquidar=null;
		DataSet titulosProcesar=null;
		MensajeCarmen mensajeCarmen = new MensajeCarmen();
		ArrayList<String> querysEjecutarOrden=new ArrayList<String>();	
		HashMap<String, String> valoresPorDefecto = null;		
		int contraparteBDV=0;
		int countCruces = 0;
//		int countOrdenes = 0;
		boolean flag = true;
//		boolean flagOrd = true;
		String crucesId = "";
//		String correoEventoId = "";
//		String ordenesIds = "";
		
		try {
//			if (tipoProducto == ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL){
//				correoEventoId = ConstantesGenerales.LIQUIDACION_EFECTIVO_PER;
//				}
//			else { correoEventoId = ConstantesGenerales.LIQUIDACION_EFECTIVO_RED; }
//			correoEventoId = ConstantesGenerales.LIQUIDACION_EFECTIVO;
			
			Logger.debug(this, "LIQUIDAR ORDENES "+tipoProducto);			
			ordenDAO.listarOrdenesPorUnidadInversionDataSet(estatusOrdenLiquidar, true, unidadInversion, tipoProducto,false);
			Logger.debug(this, "CANTIDAD DE ORDENES POR UNIDAD DE INVERSION: "+ordenDAO.getDataSet().count());
			
			Logger.debug(this, "--- OBTENER CÓDIGO DE CONTRAPARTE BDV (MENSAJE CARMEN)");
			contraparteBDV=mensajeCarmen.obtenerCodigoClienteContraparteBDV(_dso);
			
			Logger.debug(this, "--- OBTENER VALORES POR DEFECTO (MENSAJE CARMEN)");
			valoresPorDefecto=mensajeCarmen.obtenerValoresPorDefecto(_dso, mensajeCarmen.getTipoMensaje());
			
			Logger.debug(this, "PROCESAR ORDENES :"+ordenDAO.getDataSet());	
			ordenesLiquidar = ordenDAO.getDataSet();
			if (ordenesLiquidar.count()>0) {
				ordenesLiquidar.first();
				while (ordenesLiquidar.next()) {
					try {
						//CALCULAR MONTO ADJUDICADO EN DIVISAS (TABLA INFI_TB_229_ORDENES_CRUCES)						
						montoOperacion = ordenesCrucesDAO.montoCrucePorTipoCruce(ordenesLiquidar.getValue("ordene_id"),0,ConstantesGenerales.STATUS_CRUZADA);
						if(montoOperacion!=null){
							
							fechaValorString=ordenesCrucesDAO.fechaValorOrdenCruce(ordenesLiquidar.getValue("ordene_id"),0);
									
							fechaValor = Utilitario.StringToDate(fechaValorString, ConstantesGenerales.FORMATO_FECHA3);
							Logger.debug(this, "Orden: " +ordenesLiquidar.getValue("ordene_id") + ", montoOperacion: " + montoOperacion + ", fechaValor: " + fechaValor);
							
							//CREAR ORDEN DE PAGO 			
							crearOrdenPago(ordenesLiquidar.getValue("ordene_id"), 
														fechaValor, 
														Long.parseLong(ordenesLiquidar.getValue("client_id")), 
														ordenesLiquidar.getValue("moneda_id"), 
														tipoProducto, 
														montoOperacion, 
														unidadInversion, 
														ordenesLiquidar.getValue("sistema_id"), 
														ordenesLiquidar.getValue("empres_id"), 
														ordenesLiquidar.getValue("CTECTA_NUMERO"), 
														ordenesLiquidar.getValue("CTA_ABONO"), 
														ordenesLiquidar.getValue("ordene_veh_tom"),
														querysEjecutarOrden);
							
						}
						System.out.println("--- custodiaTitulos");
						if(custodiaTitulos){
							//TODO CALCULAR MONTO ADJUDICADO EN TITULOS (TABLA INFI_TB_229_ORDENES_CRUCES)						
							montoOperacion = ordenesCrucesDAO.montoCrucePorTipoCruce(ordenesLiquidar.getValue("ordene_id"),1,ConstantesGenerales.STATUS_CRUZADA);
							System.out.println("---montoOperacion" + montoOperacion);
							if (montoOperacion!=null) {
								
								
								
								ordenesCrucesDAO.consultarTitulosPorFechaValor(ordenesLiquidar.getValue("ordene_id"),0);
								titulosProcesar = ordenesCrucesDAO.getDataSet();
								
								if (titulosProcesar.count()>0) {
									titulosProcesar.first();
									while (titulosProcesar.next()) {
								
										 fechaTitulo =  Utilitario.StringToDate(titulosProcesar.getValue("FECHA_VALOR"), ConstantesGenerales.FORMATO_FECHA3);//(fechaActual)titulosProcesar.getValue("FECHA_VALOR");
										 System.out.println("Compara Actual --- " + new Date() +  " vs " + fechaTitulo + " del titulo " + titulosProcesar.getValue("TITULO_ID"));
										//if (((Utilitario.getHoraActual(ConstantesGenerales.FORMATO_FECHA).compareTo(titulosProcesar.getValue("FECHA_VALOR"))) >= 0) || ((Utilitario.getHoraActual(ConstantesGenerales.FORMATO_FECHA2).compareTo(titulosProcesar.getValue("FECHA_VALOR"))>=0))){
										 if (Utilitario.compareDates(new Date() ,fechaTitulo) >= 0){
											//REGISTRAR CUSTODIA DE TITULOS
											System.out.println("--- getHoraActual 1" + new Date() +  " vs" + fechaTitulo + " del titulo " + titulosProcesar.getValue("TITULO_ID"));
											boolean existe = ordenDAO.listarOperacionesLiquidacion(Long.parseLong(titulosProcesar.getValue("ORDENE_ID")));
											String[] consultas = ingresoCustodia.ingresarCustodia(titulosProcesar.getValue("TITULO_ID"),Long.parseLong(titulosProcesar.getValue("ORDENE_ID")), false, existe);
											querysEjecutarOrden.addAll(Arrays.asList(consultas));
											//System.out.println( "--- getHoraActual 2");
											//QUERY QUE ACTUALIZA EL CAMPO IND_TIT_PROCESADO SEGUN ORDEN E IND TIT
											querysEjecutarOrden.add(ordenesCrucesDAO.actualizarTitulosProcesados(Long.parseLong(titulosProcesar.getValue("ORDENE_ID")),titulosProcesar.getValue("TITULO_ID")));
											//System.out.println("--- getHoraActual 3");
											if(flag){
												crucesId+=titulosProcesar.getValue("ID_CRUCE");
												flag = false;
											}else{
												crucesId+=","+titulosProcesar.getValue("ID_CRUCE");
											}
											countCruces++;
											if(countCruces == ConstantesGenerales.COMMIT_REGISTROS_LIQ){
												//*************LLAMADA AL PROCESO DE ENVIO DE CORREOS PARA TITULOS**************//
												CallEnvioCorreos callEnvio = new CallEnvioCorreos(ConstantesGenerales.RECEPCION_TITULO, PlantillasMailTipos.TIPO_DEST_CLIENTE_COD, null, unidadInversion, _dso, null,null, crucesId);
												Thread t = new Thread(callEnvio); //Ejecucion del hilo que envia los correos
												t.start();
												t.join();
												crucesId = "";
												flag = true;
												countCruces = 0;
											}
										}
									}
								}
								//System.out.println("--- getHoraActual 4");
								querysEjecutarOrden.add(ordenesCrucesDAO.actualizarOrdenesLiquidadas(Long.parseLong(ordenesLiquidar.getValue("ordene_id"))));
								//OBTENER TITULOS DE LA ORDEN
								//System.out.println("--- getHoraActual 5");
								Orden orden =ordenDAO.listarOrden(Long.parseLong(ordenesLiquidar.getValue("ordene_id")), false, true, false, false, false);

								//GENERAR MENSAJE CARMEN
								//System.out.println("--- getHoraActual 6");
								querysEjecutarOrden.addAll(mensajeCarmen.generarMensajeCarmen(orden, nmUsuario, _dso, contraparteBDV, valoresPorDefecto));
							}							
						}
						//System.out.println("--- getHoraActual 7");
						//LIQUIDAR ORDEN						
						querysEjecutarOrden.add(ordenDAO.liquidarOrden(secuenciaProcesos, StatusOrden.LIQUIDADA, Long.parseLong(ordenesLiquidar.getValue("ordene_id"))));
						//System.out.println("--- getHoraActual 8");
						querysEjecutar.addAll(querysEjecutarOrden);
						querysEjecutarOrden.clear();
					} catch (Exception e) {
						Logger.error(this, "Error en la ejecución de la orden:" +ordenesLiquidar.getValue("ordene_id") + ". Error: " + e.getMessage());
						querysEjecutarOrden.clear();
					}
					//System.out.println("--- getHoraActual 9");
					//EJECUCIÓN DE QUERYS DE ACUERDO A LA CANTIDAD DE ORDENES A PROCESAR POR LOTES
					procesarQuerysPorLote(querysEjecutar);	
					
				}//END WHILE ordenesLiquidar
				System.out.println("--- getHoraActual 10");
				//EJECUCIÓN DE QUERYS DEL ÚLTIMO LOTE
				ordenDAO.ejecutarStatementsBatch(querysEjecutar);
				
				//*************LLAMADA AL PROCESO DE ENVIO DE CORREOS PARA TITULOS **************//
				Logger.debug(this, "LLAMADA AL PROCESO DE ENVIO DE CORREOS - RECEPCION TITULOS LIQUIDACION ---------- crucesIds: "+crucesId);
				CallEnvioCorreos callEnvio2 = new CallEnvioCorreos(ConstantesGenerales.RECEPCION_TITULO, PlantillasMailTipos.TIPO_DEST_CLIENTE_COD, null, unidadInversion, _dso, null,null, crucesId);
				Thread t2 = new Thread(callEnvio2); //Ejecucion del hilo que envia los correos
				t2.start();
				System.out.println("--- getHoraActual 11");
			}//END IF ordenesLiquidar.count			
		} catch (Exception e) {
			Logger.error(this, "Ha ocurrido un error en el proceso de liquidación "+e.getMessage());
		}finally{					
			try {			
				if (ordenDAO != null) {
					ordenDAO.cerrarConexion();
				}
			} catch (Exception e) {
				Logger.error(this, "Ha ocurrido un error al cerrar las conexiones en el proceso de LIQUIDACION "+tipoProducto+": "+e.getMessage());
			}											
		}		
	}	

	public void run() {
		try {
			liquidarUnidadInversion();
		} catch (Exception e) {
			proceso.setDescripcionError(e.getMessage());
			Logger.info(this,"ERROR EN EL PROCESO DE LIQUIDACION "+tipoProducto);
		} catch (Throwable e) {
			Logger.info(this,"ERROR EN EL PROCESO DE LIQUIDACION "+tipoProducto);
		}
		
	}
}

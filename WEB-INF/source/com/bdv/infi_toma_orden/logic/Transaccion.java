package com.bdv.infi_toma_orden.logic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import javax.sql.DataSource;

import megasoft.DataSet;
import megasoft.Logger;

import com.bancovenezuela.comun.data.OrdenOperacion;
import com.bdv.infi.dao.TransaccionFijaDAO;
import com.bdv.infi.dao.UIComisionDAO;
import com.bdv.infi.data.ReglaUIComision;
import com.bdv.infi.data.UIComision;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionFija;
import com.bdv.infi_toma_orden.data.TomaOrdenSimulada;

/**
 * Clase que se encarga de la operación de una transacción específica
 */
public class Transaccion {
	
	/**
	 * Nombre del DataSource
	 */
	private String nombreDataSource;
	/**
	 * DataSource a utilizar si el cliente no es un WebService
	 */
	private DataSource dso;
	
	/**
	 * Objeto OperacionRespuesta
	 */
	private OrdenOperacion beanOperacion;
	
	private HashMap<String,DataSet> cacheTransacciones = null;

	/**
	 * Constructor de la clase
	 * @param nombreDataSource : nombre que se obtiene del ambiente de ejecucion de los WebService
	 * @param dso : DataSource instanciado por clases que se ejecutan en ambientes Web
	 */
	public Transaccion (String nombreDataSource, DataSource dso) {
		this.nombreDataSource = nombreDataSource;
		this.dso = dso;
	}


	/**
	 * Proceso que busca las transacciones y comisiones que se deben aplicar a una orden según la transacción. <br> 
	 * Este proceso genera las transacciones financieras relacionadas a comisión según la transacción
	 * 
	 * @param beanOrdenSimulada <br>
	 *            orden a la que se le deben aplicar las transacciones. Lanza
	 *            una excepción en caso de fallar en el cálculo
	 */
	//Modificado Requerimiento TTS_443 SICAD 2 NM26659_28/05/2014
	public boolean aplicarTransacciones(TomaOrdenSimulada beanOrdenSimulada, String tipoPersona, String idMoneda, String transaccionFinanciera, int manejoProductoInstFin, boolean cobraIGTF,Double porcentajeCobroIGTF) throws Exception {
		String transaccionFinancieraOrgi = transaccionFinanciera;
		
		if (cacheTransacciones == null){
			Logger.info(this, "Creando cacheTransacciones");
			cacheTransacciones = new HashMap<String,DataSet>();	
		}
		
		ArrayList<ReglaUIComision> listaReglas;
		//Busca las transacciones financieras según las condiciones establecidas
		UIComisionDAO uiComisionDAO = new UIComisionDAO(dso);
		UIComision uiComision = new UIComision();
		ReglaUIComision reglaUIComision = new ReglaUIComision();
		String idTransaccionFinanciera = "";
		DataSet transaccion = new DataSet();
		
		ArrayList<UIComision> ArrComisiones = new ArrayList<UIComision>();
		
		ArrComisiones =	uiComisionDAO.obtenerComisionesUIAplicar(beanOrdenSimulada.getIdUnidadInversion(), beanOrdenSimulada.getIdBlotter(), tipoPersona,  beanOrdenSimulada.getMontoPedido().doubleValue());
		
			
		if(ArrComisiones== null || ArrComisiones.isEmpty()){
			return false;
		}
			
		// Arreglo para retornar valores de tasa y monto a aplicar en la operación		
		BigDecimal valores[] = new BigDecimal[2];
		
		
		//TODO Verificar validacion de tipo producto
		//NM29643 21/08/2013 Validacion para que no genere operaciones por las comisiones para el tipo producto sub div personal
		System.out.println("TIPO PRODUCTO EN TRANSACCIONNNNNNNNN---"+beanOrdenSimulada.getTipoProductoId());
		
		if( beanOrdenSimulada.getTipoProductoId()!=null && !beanOrdenSimulada.getTipoProductoId().equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL) ){
			System.out.println("ENTRA A COMISIONES EN TRANSACCIONNNNNNNN---");		
			
			//obtener la lista de transacciones financieras con reglas para toma de orden
			//listaTF = transaccionFinancieraDAO.getListaTF(); 
			for (int i=0; i<ArrComisiones.size(); i++) {
				uiComision = (UIComision) ArrComisiones.get(i);
				//NM32454 SI LA COMISION ES DE EFECTIVO SE DEBE 
				if(uiComision.getTipoComision() == Integer.parseInt(ConstantesGenerales.BLOTTER_TIPO_OPERACION_EFEC)){
					transaccionFinanciera = "BLO"; 
				}else {
					transaccionFinanciera = transaccionFinancieraOrgi;
				}
				
				//Modificacion para adaptar al tipo de manejo de instrumento (Mixto o unico) en las diferentes tomas de ordenes
				//if(manejoProductoInstFin!=null && manejoProductoInstFin.length>0){
					//if(beanOrdenSimulada.getTipoProductoId().equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL) || beanOrdenSimulada.getTipoProductoId().equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL)){
					if(manejoProductoInstFin==1){
							System.out.println("------MANEJO DE PRODUCTO 1 en Comisiones");
						if(uiComision.getPorcentaje()==0){ //COMISION ES FIJA
							//TODO Validar que sea el ID de TransaccionFija correcto
							transaccion = obtenerTransaccionFijaCache(TransaccionFija.COBRO_COMISION_INVARIABLE_MANEJO_MIXTO, beanOrdenSimulada.getVehiculoTomador(),beanOrdenSimulada.getInstrumentoId());
						}else{ //COMISION ES VARIABLE
							//TODO Validar que sea el ID de TransaccionFija correcto
							transaccion = obtenerTransaccionFijaCache(TransaccionFija.GENERAL_COMISION_DEB_MANEJO_MIXTO, beanOrdenSimulada.getVehiculoTomador(),beanOrdenSimulada.getInstrumentoId());
						}
					}else{
						System.out.println("------MANEJO DE PRODUCTO 0 en Comisiones");
						transaccion = obtenerTransaccionFijaCache(TransaccionFija.COMISION_DEB, beanOrdenSimulada.getVehiculoTomador(),beanOrdenSimulada.getInstrumentoId());
					}
				//}
				
				String codigoOperacion = "0";
				if(transaccion.count()>0){
					transaccion.first();
					transaccion.next();
					//Asignar codigo de operacion
					if(transaccionFinanciera.equals(com.bdv.infi.logic.interfaces.TransaccionFinanciera.BLOQUEO)){
						codigoOperacion = transaccion.getValue("cod_operacion_cte_blo");
						
						if(codigoOperacion==null || codigoOperacion.equals("")){
							throw new Exception("No se ha definido el c&oacute;digo de operaci&oacute;n de comisi&oacute;n para bloqueo asociado al veh&iacute;culo tomador de la orden.");
						}

					}else if(transaccionFinanciera.equals(com.bdv.infi.logic.interfaces.TransaccionFinanciera.DEBITO)){
						codigoOperacion = transaccion.getValue("cod_operacion_cte_deb");
						
						if(codigoOperacion==null || codigoOperacion.equals("")){
							throw new Exception("No se ha definido el c&oacute;digo de operaci&oacute;n de comisi&oacute;n para d&eacute;bito asociado al veh&iacute;culo tomador de la orden.");
						}

					}
					
					idTransaccionFinanciera = transaccion.getValue("trnfin_id");
				
				}else{
					throw new Exception("No se ha definido una transacci&oacute;n de comisi&oacute;n para el veh&iacute;culo tomador.");
				}
				
				//Buscar las reglas asociadas a la transacción financiera
				listaReglas = new ArrayList<ReglaUIComision>();
				listaReglas = uiComision.getReglas();//obtener las reglas de la comision a aplicar
				
				
				if(!listaReglas.isEmpty()){
					
					for(int k =0; k<listaReglas.size(); k++){
						
						reglaUIComision = new ReglaUIComision();
						
						reglaUIComision = (ReglaUIComision) listaReglas.get(k);
						
							
						valores = calcularMontoTasaOperacion(
								new BigDecimal(reglaUIComision.getMonto()), new BigDecimal(reglaUIComision.getPorcentaje()), 
								new BigDecimal(uiComision.getMontoFijo()), new BigDecimal(uiComision.getPorcentaje()), 
								beanOrdenSimulada.getMontoInversion());
											
						valores[0]= valores[0].setScale(2, BigDecimal.ROUND_HALF_EVEN);					
						
						beanOperacion = new OrdenOperacion();
						beanOperacion.setIdTransaccionFinanciera(String.valueOf(uiComision.getIdComision()));
						beanOperacion.setDescripcionTransaccion(uiComision.getNombre());
						beanOperacion.setMontoOperacion(valores[0]);
						beanOperacion.setTasa(valores[1]);	
						
						beanOperacion.setInComision(1);
						
						beanOperacion.setNombreOperacion(uiComision.getNombre());
						beanOperacion.setTipoTransaccionFinanc(transaccionFinanciera);
						
						beanOperacion.setCodigoOperacion(codigoOperacion);	
						
						//Se asignara temporalmente en el campo idOperacion al OBJETO de operacion de comision SIMULADO
						//el id de comision UI 
						beanOperacion.setIdOperacion(uiComision.getIdComision());
						
						beanOperacion.setIdTransaccionFinanciera(idTransaccionFinanciera);
										
						beanOrdenSimulada.addListaOperaciones(beanOperacion);
					
						
					}
					
				}else{//en caso de no encontrar reglas debe aplicar los valores de la transaccion general general generando una operación
					beanOperacion = new OrdenOperacion();
					
					//NM32454 SIMADI_TAQUILLAL
					//SI LA COMISION ES DE TIPO EFECTIVO
					if(uiComision.getTipoComision() == Integer.parseInt(ConstantesGenerales.BLOTTER_TIPO_OPERACION_EFEC)){
						//establecer valores de la Transaccion general
						valores = establecerValoresTransaccionGenerica(uiComision, beanOrdenSimulada.getMontoInversionEfectivo());
						beanOrdenSimulada.setMontoComisionesEfectivo(valores[0]);
						//SE CREA LA OPERACION CON EL TIPO DE EFECTIVO
						beanOperacion.setTipoOperacion(ConstantesGenerales.BLOTTER_TIPO_OPERACION_EFEC);
					}else {
						//SE CREA LA OPERACION CON EL TIPO ELECTRONICO
						beanOperacion.setTipoOperacion(ConstantesGenerales.BLOTTER_TIPO_OPERACION_ELEC);
						//establecer valores de la Transaccion general
						valores = establecerValoresTransaccionGenerica(uiComision, beanOrdenSimulada.getMontoInversion());
					}
					
					valores[0]= valores[0].setScale(2, BigDecimal.ROUND_HALF_EVEN);				
					beanOperacion.setIdTransaccionFinanciera(String.valueOf(uiComision.getIdComision()));
					beanOperacion.setDescripcionTransaccion(uiComision.getNombre());
					
					if(cobraIGTF){ //NM32454 CAMBIO PARA SABER SI EL CLIENTE SE LE VA A COBRAR O NO IGTF
						//SE SUMA EL PORCENTAJE DE COMISION AL MONTO DE CAPITAL CON IDB
						BigDecimal montoOperacion = valores[0]; 
						montoOperacion = montoOperacion.add(montoOperacion.multiply(new BigDecimal(porcentajeCobroIGTF)).divide(new BigDecimal(100)));
						beanOperacion.setMontoOperacion(montoOperacion);
					}else { //COMPORTAMIENTO ORIGINAL
						beanOperacion.setMontoOperacion(valores[0]);
					}
					
					beanOperacion.setTasa(valores[1]);
					beanOperacion.setInComision(1);				
					beanOperacion.setNombreOperacion(uiComision.getNombre());
					beanOperacion.setTipoTransaccionFinanc(transaccionFinanciera);				
					beanOperacion.setCodigoOperacion(codigoOperacion);			
					beanOperacion.setIdTransaccionFinanciera(idTransaccionFinanciera);
					//Se asignara temporalmente en el campo idOperacion al OBJETO de operacion de comision SIMULADO
					//el id de comision UI 
					beanOperacion.setIdOperacion(uiComision.getIdComision());
					
					//NM32454 SIMADI_TAQUILLA
					//SI TENGO COMISION POR EFECTIVO PERO NO TENGO MONTO POR EFECTIVO EN LA OPERACION NO LO AGREGO
					if(beanOrdenSimulada.getMontoPedidoEfectivo().doubleValue() > 0 & beanOperacion.getTipoOperacion().equals(ConstantesGenerales.BLOTTER_TIPO_OPERACION_EFEC)){
						beanOrdenSimulada.addListaOperaciones(beanOperacion);
					}
					//SI TENGO COMISION POR ELECTRONICO PERO NO TENGO MONTO DE ELECTRONICO NO LO AGREGO A LA OPERACION
					else if(beanOrdenSimulada.getMontoPedido().doubleValue() > 0 & beanOperacion.getTipoOperacion().equals(ConstantesGenerales.BLOTTER_TIPO_OPERACION_ELEC))  {
						beanOrdenSimulada.addListaOperaciones(beanOperacion);
					}
					
				}
			}
		
		}//Si no es tipo producto SUBASTA DIVISAS PERSONAL
		else	System.out.println("NOOOOOOOOOOO ENTRA A COMISIONES EN TRANSACCIONNNNNNNN---");
		
		return true;
	}

	/**
	 * Establece los los valores de la operaci&oacute;n de acuerdo a la transacci&oacute;n gen&eacute;rica
	 * @param transaccionFinanciera
	 * @param montoOrden
	 * @return arreglo con los valores de monto calculado y tasa
	 */
	private BigDecimal[] establecerValoresTransaccionGenerica(UIComision uiComision, BigDecimal montoOrden) {
		BigDecimal valores[] = new BigDecimal[2];
		valores[0] = new BigDecimal (0);
		valores[1] = new BigDecimal (0);

		//if (transaccionFinanciera.getTipoAplicacion().equals("M")){// si la aplicación de la
		if(uiComision.getMontoFijo() > 0){
						// transaccion general es
						// Monto Fijo
			valores[0] = new BigDecimal(uiComision.getMontoFijo());//valor fijo, la tasa es 0
			
		}else{// si es porcentaje
			
			BigDecimal porcentaje = new BigDecimal(uiComision.getPorcentaje());
			valores[0] = porcentaje.multiply(montoOrden);
			valores[0] = valores[0].divide(new BigDecimal(100));//resultado
			valores[1] = porcentaje;//tasa*/

		}

		return valores;
	}
	
	
	/*private BigDecimal[] establecerValoresTransaccionGenerica(TransaccionFinanciera transaccionFinanciera, BigDecimal montoOrden) {
		BigDecimal valores[] = new BigDecimal[2];
		valores[0] = new BigDecimal (0);
		valores[1] = new BigDecimal (0);

		if (transaccionFinanciera.getTipoAplicacion().equals("M")){// si la aplicación de la
						// transaccion general es
						// Monto Fijo
			valores[0] = transaccionFinanciera.getValor();//valor fijo, la tasa es 0
			
		}else {// si es porcentaje
			valores[0] = transaccionFinanciera.getValor().multiply(montoOrden);
			valores[0] = valores[0].divide(new BigDecimal(100));//resultado
			valores[1] = transaccionFinanciera.getValor();//tasa

		}

		return valores;
	}
*/


	/**
	 * Calcula le monto y la tasa aplicadas a la operación
	 * 
	 * @param montoRegla
	 * @param porcentajeRegla
	 * @param valorTrnFinanc
	 * @param aplicacionTrnFinanc
	 * @param funcionRegla
	 * @param tasaOperacion
	 * @param montoOperacion
	 * @param montoOrden
	 * @return Bigdecimal[] arreglo de dos posiciones con la tasa y el monto a  aplicar
	 */
	private BigDecimal[] calcularMontoTasaOperacion(BigDecimal montoRegla, BigDecimal porcentajeRegla, BigDecimal montoFijoComision, BigDecimal porcentajeComision, 
													BigDecimal montoOrden) throws Exception {

		BigDecimal valores[] = new BigDecimal[2];
		valores[0] = new BigDecimal (0);
		valores[1] = new BigDecimal (0);
		
		// si existen valores definidos en la regla
		if (montoRegla.doubleValue() != 0 || porcentajeRegla.doubleValue() != 0) {
			// si existe un monto fijo, aplicarlo
			if (montoRegla.doubleValue() != 0) {
				valores[0] = montoRegla;
			} else {
				//	 si es porcentaje calcularlo
				valores[0] = porcentajeRegla.multiply(montoOrden);
				valores[0] = valores[0].divide(new BigDecimal(100));
				valores[1] = porcentajeRegla;				
			}
		} else {
			// si no esxisten valores en la regla, tomar valor de nivel
			// superior (Transacción Financiera)
			
			// si la aplicación de la comision es monto fijo
			if(montoFijoComision.doubleValue() > 0){								
										
				valores[0] = montoFijoComision;
				
			}else {// si es porcentaje
				valores[0] = porcentajeComision.multiply(montoOrden);
				valores[0] = valores[0].divide(new BigDecimal(100));
				valores[1] = porcentajeComision;
				
			}
		}
		return valores;
	}

	/**
	 * Obtiene la transaccion en cache. Si no existe la busca en base de datos y la crea
	 * @param tipoTransaccion tipo de transacción a buscar
	 * @param vehiculoTomador id del vehiculo tomador
	 * @param idInstrumento id del instrumento
	 * @return un dataset con la informacion solicitada
	 * @throws Exception en caso de error
	 */
	private DataSet obtenerTransaccionFijaCache(int tipoTransaccion, String vehiculoTomador, String idInstrumento) throws Exception{
		String llave = String.valueOf(tipoTransaccion) + vehiculoTomador + idInstrumento;
		DataSet ds = null;
		if (cacheTransacciones.containsKey(llave)){
			Logger.debug(this, "Obteniendo del cache la transaccion. " + llave);
			ds = cacheTransacciones.get(llave);
		}else{
			TransaccionFijaDAO transaccionFijaDAO = new TransaccionFijaDAO(dso);
			transaccionFijaDAO.listar(tipoTransaccion,vehiculoTomador,idInstrumento);		
			ds  = transaccionFijaDAO.getDataSet();
			Logger.debug(this, "Obteniendo de la base de datos la transaccion. " + llave);
			cacheTransacciones.put(llave, ds);
		}
		return ds;
	}

}
package com.bdv.infi.logic.cierre_sistema;

import java.util.ArrayList;
import java.util.HashMap;

import javax.sql.DataSource;

import megasoft.DataSet;
import megasoft.Logger;

import com.bdv.infi.dao.CierreSistemaDAO;
import com.bdv.infi.dao.ConfiguracionTasaDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.TransaccionFijaDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionFija;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.util.Utilitario;


/**
 * Clase encargada de realizar los calculos para la venta de un t&iacute;tulo en custodia por un cliente en particular. 
 * @author Erika Valerio, Megasoft Computaci&oacute;n
 */
  

public class ValidadorCierreSistema
{
	public HashMap<String, Object> parametrosEntrada = new HashMap<String, Object>();
	public ArrayList<String> ListaMensajes = new ArrayList<String>();
	private DataSource dso;
	private final String DESACTIVADO="0";
	private final String ACTIVADO="1";
	boolean estatusCierreActivo;
	boolean cierreConFallas;

	
	/**
	 * Constructor de la clase
	 * @param dso :DatSource a utilizar para acceder a la base de datos
	 */
	public ValidadorCierreSistema (DataSource dso) {
		this.dso = dso;
	}
	
	public HashMap<String, Object> getParametrosEntrada() {
		return parametrosEntrada;
	}

	public void setParametrosEntrada(HashMap<String, Object> parametrosEntrada) {
		this.parametrosEntrada = parametrosEntrada;
	}
	
	//-----------Constantes de nombres de parametros-------------------//
	//Indicadores de validación de cada proceso
	public static String VALIDAR_BUEN_VALOR= "validarBuenValor"; //indicador de validación de proceso Buen Valor
	public static String VALIDAR_CONTABILIDAD= "validarContabilidad"; //indicador de validación de proceso de Contabilidad
	
	//Variables
	public static String ACCION= "accion";//Activar/Desactivar	
	public static String FECHA_SISTEMA= "fechaSistema";//Activar/Desactivar	
	public static String FECHA_PRECIERRE= "fechaPrecierre";//Activar/Desactivar	
	
		
	/**
	 * Validador de Cierre de Sistema
	 * @return
	 * @throws Exception
	 */
	public ArrayList<String> validar() throws Exception{
		
		Logger.info(this, "-------Comenzando Validaciones para Activación de Cierre de Sistema---------------");
		
		String tipoProceso = TransaccionNegocio.PROC_CIERRE_SISTEMA;
		CierreSistemaDAO cierreSistemaDAO = new CierreSistemaDAO(dso);
		estatusCierreActivo = cierreSistemaDAO.isProcesoCierreActivo();
		cierreConFallas = cierreSistemaDAO.existeFallaProcesoCierre();
		
		//Validación de Proceso en Ejecución
		if(Utilitario.procesoEnEjecución(tipoProceso, dso)){
			ListaMensajes.add("No es posible realizar modificaciones ya que el proceso de cierre de sistema se est&aacute; ejecutando en este momento.");
			
		}else{		
			//Si la acción a realizar es desactivar
			if(parametrosEntrada.get(ACCION).toString().equals(DESACTIVADO)){	
				//validaciones de desactivación de cierre
				validarDesactivacionCierre();
			
			}else if(parametrosEntrada.get(ACCION).toString().equals(ACTIVADO)){
				
				//Validar procesos para activación de cierre
				validarActivacionCierre();				
			}			
			
		}		
		
		if(ListaMensajes.size()>0)
			Logger.info(this, "Se se encontraron problemas para la activación del cierre de sistema... Retornando lista de mensajes");		
		else
			Logger.info(this, "No se encontraron problemas para la activación del cierre de sistema...");
		
		Logger.info(this, "--------Fin Validaciones para la Activación del Cierre de Sistema---------------------");
		
		return ListaMensajes;

	}	
	
	/**
	 * Realiza las validaciones correspondientes a una desactivación de cierre de sistema
	 */
	private void validarDesactivacionCierre() {
		Logger.info(this, "Ejecutando Validaciones para Desactivación de Cierre..");
		
		if(!estatusCierreActivo){
			ListaMensajes.add("El proceso de cierre de sistema ya se encuentra DESACTIVADO");
		
		}else if(estatusCierreActivo && cierreConFallas){//verificar que no exista un cierre activo con fallas, este no se debe desactivar
			ListaMensajes.add("El proceso de cierre activo contiene fallas por lo cual no puede ser desactivado");
		}				
	}

	/**
	 * Realiza las validaciones correspondientes a una activación de cierre de sistema
	 * @throws Exception
	 */
	private void validarActivacionCierre() throws Exception {
		Logger.info(this, "Ejecutando Validaciones para Activación de Cierre..");
		
		if(estatusCierreActivo){
			ListaMensajes.add("El proceso de cierre ya se encuentra activo, si desea modificar la fecha pre-cierrre debe ingresar al modulo Cierre Sistema / Configuraci&oacute;n cierre sistema ");
		}			
		//System.out.println("parametrosEntrada.get(FECHA_SISTEMA) "+parametrosEntrada.get(FECHA_SISTEMA));;
		//System.out.println("parametrosEntrada.get(FECHA_PRECIERRE) "+ parametrosEntrada.get(FECHA_PRECIERRE));
		if(parametrosEntrada.get(FECHA_SISTEMA)!=null && parametrosEntrada.get(FECHA_PRECIERRE)!=null){
			if(Utilitario.StringToDate((String)parametrosEntrada.get(FECHA_SISTEMA),ConstantesGenerales.FORMATO_FECHA).compareTo(Utilitario.StringToDate((String)parametrosEntrada.get(FECHA_PRECIERRE),ConstantesGenerales.FORMATO_FECHA))>=0){
				ListaMensajes.add("La fecha Pre-Cierre no ha sido configurada. Por favor ingrese al m&oacute;dulo Cierre Sistema / Configuraci&oacute;n cierre sistema para realizar la configuraci&oacute;n");
			}
		}else{
			ListaMensajes.add("No se han configurado las fechas de sistema y pre-cierre");
		}
		
		if(parametrosEntrada.get(VALIDAR_BUEN_VALOR)!=null && (Boolean)parametrosEntrada.get(VALIDAR_BUEN_VALOR)){
			validarBuenValor();		
		}
	}

	/**
	 * Realiza las validaciones correspondientes para la ejecución del proceso de cálculo Buen Valor
	 * @throws Exception
	 */
	private void validarBuenValor() throws Exception {
		Logger.info(this, "Ejecutando Validaciones para Buen Valor..");
		
		//Verificar si existe creada la transacción fija BUEN VALOR
		TransaccionFijaDAO transaccionFijaDAO = new TransaccionFijaDAO(dso);
		transaccionFijaDAO.listar(TransaccionNegocio.COMISION_BUEN_VALOR); 
		if(transaccionFijaDAO.getDataSet().count()==0){
			ListaMensajes.add("No existe registro de transacción fija para "+TransaccionNegocio.COMISION_BUEN_VALOR);
		}else{	
			
			//Verificar si se encuentran registrados los códigos de operación (DEBITO) para la transacción Buen Valor
			//y para cada par vehículo, instrumento de las ordenes que serán procesadas
			OrdenDAO ordenDAO = new OrdenDAO(dso);
			ordenDAO.listarOrdenesConDebitoCapitalPendiente(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA);
			if(ordenDAO.getDataSet().count()>0){				
				//Verificar si los códigos están creados
				validarCodigosOperacionBuenValor(ordenDAO.getDataSet());		
			}	
			
		}
		
		//***Validar que existan las distintas configuración para poder activar el cierre del sistema
		//Validar existencia de Tasa Buen Valor apropaba para la fecha de sistema
		ConfiguracionTasaDAO configuracionTasaDAO = new ConfiguracionTasaDAO(dso);
		String tipoProducto = ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA;
		if(!configuracionTasaDAO.existeTasaActualPorFechaSistema(tipoProducto, TransaccionNegocio.COMISION_BUEN_VALOR)){
			ListaMensajes.add("No es posible activar el cierre de sistema ya que no existe una tasa buen valor configurada para la fecha de sistema en estatus aprobada. Verifique.");
			
		}

	}

	/**
	 * Verifica si existen los códigos de operación de débito creados por cada vehículo, instrumento financiero 
	 * de las órdenes con débito capital pendiente
	 * @param ordenesPendientes
	 * @return
	 * @throws Exception
	 */
	private void validarCodigosOperacionBuenValor(DataSet ordenesPendientes) throws Exception {		
		while(ordenesPendientes.next()){//Verificar codigos de transaccion por cada vehiculo, instrumento de las ordenes
			
			TransaccionFijaDAO transaccionFijaDAO = new TransaccionFijaDAO(dso);
			transaccionFijaDAO.listar(TransaccionFija.DEB_COMISION_BUEN_VALOR, ordenesPendientes.getValue("ordene_veh_tom"), ordenesPendientes.getValue("insfin_id"));
			if(transaccionFijaDAO.getDataSet().count()>0){
				System.out.println("validarCodigosOperacionBuenValor >0");
				if(transaccionFijaDAO.getDataSet().next()){
					if(transaccionFijaDAO.getDataSet().getValue("cod_operacion_cte_deb")==null
						|| transaccionFijaDAO.getDataSet().getValue("cod_operacion_cte_deb").trim().equals("")){
						
						ListaMensajes.add("No se han creado los c&oacute;digos de operaci&oacute;n de d&eacute;bito de "+TransaccionNegocio.COMISION_BUEN_VALOR+ " para el instrumento financiero " +transaccionFijaDAO.getDataSet().getValue("insfin_descripcion")+ " y veh&iacute;culo "+  transaccionFijaDAO.getDataSet().getValue("vehicu_nombre"));
					}
				}
			}else{				
				ListaMensajes.add("No se ha realizado la asignaci&oacute;n de la transacci&oacute;n "+TransaccionNegocio.COMISION_BUEN_VALOR+ " a alguno de los instrumento financiero que contiene operaciones de cobro pendientes, por favor verifique");
			}
		}		
	}

}

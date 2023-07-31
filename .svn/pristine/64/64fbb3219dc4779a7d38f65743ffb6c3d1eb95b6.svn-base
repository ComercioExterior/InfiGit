package com.bdv.infi.logic;

import java.util.ArrayList;
import java.util.HashMap;

import javax.sql.DataSource;

import megasoft.Logger;

import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.TipoInstruccion;
import com.bdv.infi.util.Utilitario;


/**
 * Clase encargada de realizar los calculos para la venta de un t&iacute;tulo en custodia por un cliente en particular. 
 * @author Erika Valerio, Megasoft Computaci&oacute;n
 */
  

public class ValidacionInstruccionesPago
{
	public HashMap<String, String> parametrosEntrada = new HashMap<String, String>();
	private String controlCambio ="";
	public ArrayList<String> ListaMensajes = new ArrayList<String>();
	private DataSource dso;
	
	/**
	 * Constructor de la clase
	 * @param dso :DatSource a utilizar para acceder a la base de datos
	 */
	public ValidacionInstruccionesPago (DataSource dso) {
		this.dso = dso;
	}
	
	public HashMap<String, String> getParametrosEntrada() {
		return parametrosEntrada;
	}

	public void setParametrosEntrada(HashMap<String, String> parametrosEntrada) {
		this.parametrosEntrada = parametrosEntrada;
	}
	
	//Constantes de nombres de parametros

	public static String TIPO_INSTRUCCION= "tipoInstruccion";
	public static String NUMERO_CUENTA_NACIONAL= "numeroCuentaNacional";
	public static String NUMERO_CUENTA_EXTRANJERA= "numeroCuentaExtranjera";
	public static String NOMBRE_BANCO_EXTRANJERO= "nombreBancoExtranjero";
	public static String ABA_BANCO= "abaBancoExtranjero";
	public static String BIC_BANCO= "bicBancoExtranjero";
	public static String INDICADOR_BANCO_INTERMEDIARIO= "indicadorBancoIntermediario";
	
	public static String NOMBRE_BANCO_INTERMEDIARIO= "nombreBancoIntermediario";
	public static String CUENTA_ENBCO_INTERMEDIARIO= "cuentaEnBcoIntermediario";
	public static String ABA_BCO_INTERMEDIARIO= "abaBancoIntermediario";
	public static String BIC_BCO_INTERMEDIARIO= "bicBancoIntermediario";
	
	public static String VALIDAR_DATOS_BENEFICIARIO= "validarDatosBeneficiario";
	public static String CEDULA_BENEFICIARIO= "cedulaBeneficiario";
	public static String NOMBRE_BENEFICIARIO= "nombreBeneficiario";
	public static String CUENTA_BENEFICIARIO= "cuentaBeneficiario";
	public static String DIRECCION_BANCO_CLIENTE= "direccionBancoCliente";
	public static String DIRECCION_BANCO_INTERMEDIARIO= "direccionBancoIntermediario";
	public static String TELEFONO_BANCO_CLIENTE= "telefonoBancoCliente";
	public static String TELEFONO_BANCO_INTERMEDIARIO= "telefonoBancoIntermediario";
	public static String ESTADO= "estado";
	public static String CIUDAD= "ciudad";
		
	public ArrayList<String> validador() throws Exception{
		
		Logger.info(this, "-------Comenzando Validaciones de instrucciones de Pago---------------");
		Logger.info(this, "Tipo de Intrucción: "+ parametrosEntrada.get(TIPO_INSTRUCCION));
		//Verificar control de cambio para validadciones de beneficiarios
		controlCambio=ParametrosDAO.listarParametros(ParametrosSistema.CONTROL_DE_CAMBIO, dso);
			
		//listar datos de la moneda seleccionada para el t&iacute;tulo
		if(parametrosEntrada.get(TIPO_INSTRUCCION)!=null){
			//si es instruccion para moneda nacional
			if(parametrosEntrada.get(TIPO_INSTRUCCION).equals(String.valueOf(TipoInstruccion.CUENTA_NACIONAL))){
				
				if(parametrosEntrada.get(NUMERO_CUENTA_NACIONAL)==null || (parametrosEntrada.get(NUMERO_CUENTA_NACIONAL).trim().equals(""))){
					ListaMensajes.add("Debe seleccionar el n&uacute;mero de cuenta nacional.");					
				}
			//Si es instruccion para moneda internacional
			}else{
				//Si es una transaferencia a cuenta internacional
				if(parametrosEntrada.get(TIPO_INSTRUCCION).equals(String.valueOf(TipoInstruccion.CUENTA_SWIFT))){
		
					//validar numero de cuenta
					if(parametrosEntrada.get(NUMERO_CUENTA_EXTRANJERA)==null || Utilitario.cadenaVacia(parametrosEntrada.get(NUMERO_CUENTA_EXTRANJERA))){
						ListaMensajes.add("Debe ingresar el n&uacute;mero de cuenta extranjera para transferencias a cuentas internacionales.");
						
					}	
					
					//validar nombre del banco
					if(parametrosEntrada.get(NOMBRE_BANCO_EXTRANJERO)==null || Utilitario.cadenaVacia(parametrosEntrada.get(NOMBRE_BANCO_EXTRANJERO))){
						ListaMensajes.add("Debe ingresar el nombre del banco para transferencias a cuentas internacionales.");
						
					}		
					//validar dirección del banco
					if(parametrosEntrada.get(DIRECCION_BANCO_CLIENTE)==null || Utilitario.cadenaVacia(parametrosEntrada.get(DIRECCION_BANCO_CLIENTE))){
						ListaMensajes.add("Debe ingresar la dirección del banco para transferencias a cuentas internacionales.");
						
					}	
					//validar telefono del banco
					if(parametrosEntrada.get(TELEFONO_BANCO_CLIENTE)==null || Utilitario.cadenaVacia(parametrosEntrada.get(TELEFONO_BANCO_CLIENTE))){
						ListaMensajes.add("Debe ingresar el tel&eacute;fono del banco para transferencias a cuentas internacionales.");
						
					}
					if((parametrosEntrada.get(BIC_BANCO)!=null && !parametrosEntrada.get(BIC_BANCO).trim().equals("")) && 
							(parametrosEntrada.get(ABA_BANCO)!=null && !parametrosEntrada.get(ABA_BANCO).trim().equals(""))){
									
						ListaMensajes.add( "Debe indicar c&oacute;digo ABA o BIC del banco donde se posee la cuenta, pero no ambos c&oacute;digos.");
						
					}	
					
					///Validar que se ingrese el código BIC/SWIFT del banco destino.
					if((parametrosEntrada.get(BIC_BANCO)==null || parametrosEntrada.get(BIC_BANCO).trim().equals(""))){
						ListaMensajes.add("Debe indicar el c&oacute;digo BIC del banco destino.");
						
					}

					if(parametrosEntrada.get(NOMBRE_BENEFICIARIO)==null || Utilitario.cadenaVacia(parametrosEntrada.get(NOMBRE_BENEFICIARIO))) {									
						ListaMensajes.add( "Debe indicar el nombre del beneficiario.");						
					}
					if(parametrosEntrada.get(CIUDAD)==null || Utilitario.cadenaVacia(parametrosEntrada.get(CIUDAD))) {									
						ListaMensajes.add( "Debe indicar una ciudad.");						
					}
					
					///SI HAY BANCO INTERMEDIARIO					
					if(parametrosEntrada.get(INDICADOR_BANCO_INTERMEDIARIO)!=null && parametrosEntrada.get(INDICADOR_BANCO_INTERMEDIARIO).equals("1")){
						
						//validar nombre del banco
						if(parametrosEntrada.get(NOMBRE_BANCO_INTERMEDIARIO)==null || Utilitario.cadenaVacia(parametrosEntrada.get(NOMBRE_BANCO_INTERMEDIARIO))){
							ListaMensajes.add("Debe ingresar el nombre del banco intermediario para transaferencias a cuentas internacionales.");
							
						}
						
						//validar dirección del banco
						if(parametrosEntrada.get(DIRECCION_BANCO_INTERMEDIARIO)==null || Utilitario.cadenaVacia(parametrosEntrada.get(DIRECCION_BANCO_INTERMEDIARIO))){
							ListaMensajes.add("Debe ingresar la dirección del banco intermediario para transferencias a cuentas internacionales.");
							
						}
						//validar telefono del banco						
						if(parametrosEntrada.get(TELEFONO_BANCO_INTERMEDIARIO)==null || Utilitario.cadenaVacia(parametrosEntrada.get(TELEFONO_BANCO_INTERMEDIARIO))){
							ListaMensajes.add("Debe ingresar el tel&eacute;fono del banco intermediario para transferencias a cuentas internacionales.");
							
						}
					
						if ((parametrosEntrada.get(CUENTA_ENBCO_INTERMEDIARIO)==null || parametrosEntrada.get(CUENTA_ENBCO_INTERMEDIARIO).trim().equals("") ) &&
								(parametrosEntrada.get(BIC_BANCO)==null || parametrosEntrada.get(BIC_BANCO).trim().equals("")) && 
								(parametrosEntrada.get(ABA_BANCO)==null || parametrosEntrada.get(ABA_BANCO).trim().equals("")))
						{  
							ListaMensajes.add("Debe indicar el n&uacute;mero de cuenta del banco destino en el banco intermediario o en su defecto el c&oacute;digo BIC o ABA del banco donde posee la cuenta el cliente.");
						
						}

						/* QUITAR VALIDACION DE ABA
						//Validar que se indique el Codigo ABA o en su defecto el codigo BIC o SWIFT
						if((parametrosEntrada.get(BIC_BCO_INTERMEDIARIO)==null || parametrosEntrada.get(BIC_BCO_INTERMEDIARIO).equals("")) && 
						  (parametrosEntrada.get(ABA_BCO_INTERMEDIARIO)==null || parametrosEntrada.get(ABA_BCO_INTERMEDIARIO).equals(""))){
								
							ListaMensajes.add("Debe indicar un c&oacute;digo ABA del banco intermediario. O en su defecto debe indicar el c&oacute;digo BIC.");
							
						}*/
							
						//Validar que se indique el Codigo BIC/SWIFT
						if((parametrosEntrada.get(BIC_BCO_INTERMEDIARIO)==null || parametrosEntrada.get(BIC_BCO_INTERMEDIARIO).trim().equals(""))){
								
							ListaMensajes.add("Debe indicar el c&oacute;digo BIC del banco intermediario.");
							
						}
						
						//Validar que no se introduzcan ambos codigos: BIC y ABA
						if((parametrosEntrada.get(BIC_BCO_INTERMEDIARIO)!=null && !parametrosEntrada.get(BIC_BCO_INTERMEDIARIO).equals("")) && 
							(parametrosEntrada.get(ABA_BCO_INTERMEDIARIO)!=null && !parametrosEntrada.get(ABA_BCO_INTERMEDIARIO).equals(""))){
						
							ListaMensajes.add("Debe indicar c&oacute;digo ABA o BIC del banco intermediario pero no ambos c&oacute;digos.");
							
						}


					}else{
						
						/* QUITAR VALIDACION SI SE INGRESA INTERMEDIARIO, SE DEBE VALIDAR SIEMPRE
						///SI NO HAY BANCO INTERMEDIARIO VALIDAR CODIGO SWIFT Y EL BIC
						if((parametrosEntrada.get(BIC_BANCO)==null || parametrosEntrada.get(BIC_BANCO).equals("")) && 
							(parametrosEntrada.get(ABA_BANCO)==null || parametrosEntrada.get(ABA_BANCO).equals(""))){
							ListaMensajes.add("Debe indicar un c&oacute;digo ABA del banco donde se posee la cuenta. O en su defecto debe indicar el c&oacute;digo BIC.");
							
						}*/

					}				
				}else{
					if(parametrosEntrada.get(TIPO_INSTRUCCION).equals(String.valueOf(TipoInstruccion.OPERACION_DE_CAMBIO))){
						if(parametrosEntrada.get(NUMERO_CUENTA_NACIONAL)==null || (parametrosEntrada.get(NUMERO_CUENTA_NACIONAL).equals(""))){
							ListaMensajes.add("Debe seleccionar el n&uacute;mero de cuenta nacional.");					
						}
					}
				}
			
			}
			
			if(parametrosEntrada.get(VALIDAR_DATOS_BENEFICIARIO)!=null){
				//si NO hay control de cambio validar que se hayan introducido los datos del beneficiario 
				if(controlCambio!=null && controlCambio.equals("0")){
					
					if(parametrosEntrada.get(CEDULA_BENEFICIARIO)==null || parametrosEntrada.get(CEDULA_BENEFICIARIO).equals("")){
						ListaMensajes.add("Debe ingresar el n&uacute;mero de c&eacute;dula o rif del beneficiario.");								
					}
					
					if(parametrosEntrada.get(NOMBRE_BENEFICIARIO)==null || parametrosEntrada.get(NOMBRE_BENEFICIARIO).equals("")){
						ListaMensajes.add("Debe ingresar el nombre del beneficiario.");							
					}
					
				}
			}

		}else{
			ListaMensajes.add("Debe seleccionar el tipo de instrucci&oacute;n de pago");
		}		
	
		if(ListaMensajes.size()>0)
			Logger.info(this, "Se encontraron errores...");		
		else
			Logger.info(this, "No se encontraron errores en las instrucciones de pago...");
		
		Logger.info(this, "--------Fin Validaciones de Instrucciones de Pago.. Retornando lista de mensajes-----------------");
		
		return ListaMensajes;

	}	
	
public ArrayList<String> validadorMensajeSWIFT() throws Exception{
		
		Logger.info(this, "-------Comenzando Validaciones de instrucciones de Pago SWIFT---------------");
		Logger.info(this, "Tipo de Intrucción: "+ parametrosEntrada.get(TIPO_INSTRUCCION));
		
		//Si es una transaferencia a cuenta internacional
		if(parametrosEntrada.get(TIPO_INSTRUCCION).equals(String.valueOf(TipoInstruccion.CUENTA_SWIFT))){

			//validar numero de cuenta
			if(parametrosEntrada.get(NUMERO_CUENTA_EXTRANJERA)==null || Utilitario.cadenaVacia(parametrosEntrada.get(NUMERO_CUENTA_EXTRANJERA))){
				ListaMensajes.add("Indique el n&uacute;mero de cuenta extranjera para transferencias a cuentas internacionales.");	
			}
			
			//validar nombre del banco
			if(parametrosEntrada.get(NOMBRE_BANCO_EXTRANJERO)==null || Utilitario.cadenaVacia(parametrosEntrada.get(NOMBRE_BANCO_EXTRANJERO))){
				ListaMensajes.add("Indique el nombre del banco para transferencias a cuentas internacionales.");	
			}		
			if((parametrosEntrada.get(BIC_BANCO)!=null && !parametrosEntrada.get(BIC_BANCO).trim().equals("")) && 
					(parametrosEntrada.get(ABA_BANCO)!=null && !parametrosEntrada.get(ABA_BANCO).trim().equals(""))){							
				ListaMensajes.add( "Indique c&oacute;digo ABA o BIC del banco donde se posee la cuenta, pero no ambos c&oacute;digos.");

			}						
			if(parametrosEntrada.get(NOMBRE_BENEFICIARIO)==null || Utilitario.cadenaVacia(parametrosEntrada.get(NOMBRE_BENEFICIARIO))) {									
				ListaMensajes.add( "Indique el nombre del beneficiario.");						
			}			
			if(parametrosEntrada.get(CIUDAD)==null || Utilitario.cadenaVacia(parametrosEntrada.get(CIUDAD))) {									
				ListaMensajes.add( "Indique ciudad de origen");						
			}			
			if(parametrosEntrada.get(ESTADO)==null || Utilitario.cadenaVacia(parametrosEntrada.get(ESTADO))) {									
				ListaMensajes.add( "Indique estado de origen");						
			}							
		}
			
		if(ListaMensajes.size()>0)
			Logger.info(this, "Se encontraron errores...");		
		else
			Logger.info(this, "No se encontraron errores en las instrucciones de pago...");
		
		Logger.info(this, "--------Fin Validaciones de Instrucciones de Pago.. Retornando lista de mensajes SWIFT-----------------");
		
		return ListaMensajes;

	}	
	
}

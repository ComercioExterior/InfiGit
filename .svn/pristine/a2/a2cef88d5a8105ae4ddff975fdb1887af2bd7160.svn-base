package com.bdv.infi.logic.interfaz_altair;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import megasoft.Logger;

import com.bancovenezuela.comun.data.OrdenOperacion;
import com.bdv.infi.dao.Transaccion;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaz_altair.transaction.GenericaTransaccion;
import com.bdv.infi.webservices.beans.B501;
import com.bdv.infi.webservices.beans.B501Respuesta;
import com.bdv.infi.webservices.beans.B706;
import com.bdv.infi.webservices.beans.B731;
import com.bdv.infi.webservices.beans.B731Respuesta;
import com.bdv.infi.webservices.beans.B756;
import com.bdv.infi.webservices.beans.B756Respuesta;
import com.bdv.infi.webservices.beans.BGM7061;
import com.bdv.infi.webservices.beans.BGMCAB;
import com.bdv.infi.webservices.beans.BGMCRI;
import com.bdv.infi.webservices.beans.BV61;
import com.bdv.infi.webservices.beans.BV61Respuesta;
import com.bdv.infi.webservices.client.ClienteWs;

/**
 * Clase que contiene la l&oacute;gica para el proceso de las transacciones financieras
 * que contiene una orden o una toma de orden. <br>
 * Es la encargada de invocar los conectores para el proceso de las transacciones
 * financieras que deben ir a cada sistema. <br>
 * Adem&aacute;s es la responsable de almacenar los intentos de transacci&oacute;n en las tablas.
 **/
public class AbstractFactoryAltair {

	/**
	 * Cliente WebServices
	 */
	protected ClienteWs clienteWs = null;	
	/**
	 * Contexto de la aplicacion
	 */
	protected ServletContext contexto = null;
	/**
	 * DataSource a utilizar si el cliente no es un WebService
	 */
	protected DataSource dso;
	
	protected Transaccion transaccion;
	
	/**Nombre del usuario para la conexión al servicio*/
	public String nombreUsuario = "";	
	
	/**ip*/
	public String ipTerminal = "";	
	
	/**Indicador que si falla una operación no intente aplicar las demás*/
	boolean indicadorFalla = false;
	
	/**
	 * Clase que encapsula la funcionalidad de las Transacciones de ALTAIR
	 */
	protected GenericaTransaccion classAltair = null;
	
	public static final String ERROR_RETENCION_CANCELADA = "BGE0263";

	/**
	 * Constructor de la clase
	 * @param dso :DatSource a utilizar para acceder a la base de datos
	 */
	public AbstractFactoryAltair (DataSource dso, ServletContext contexto){
		this.dso = dso;
		this.contexto = contexto;
	}
	
	/**
	 * Constructor de la clase
	 * @param transaccion :Objeto transaccion con una conexion a base de datos abierta
	 */
	public AbstractFactoryAltair (Transaccion transaccion, ServletContext contexto) {
		this.transaccion = transaccion;
		this.contexto = contexto;
		this.dso = transaccion.getDataSource();
	}
	
	/**
	 * Constructor de la clase
	 * @param transaccion :Objeto transaccion con una conexion a base de datos abierta
	 */
	public AbstractFactoryAltair (Transaccion transaccion, ServletContext contexto, boolean indicadorFalla) {
		this.transaccion = transaccion;
		this.contexto = contexto;
		this.dso = transaccion.getDataSource();
		this.indicadorFalla = indicadorFalla;
	}

	
	/**
	 * Constructor de la clase
	 * @param dso :DatSource a utilizar para acceder a la base de datos
	 */
	public AbstractFactoryAltair (DataSource dso, ServletContext contexto, boolean indicadorFalla){
		this.dso = dso;
		this.contexto = contexto;
		this.indicadorFalla = indicadorFalla;
	}	
	

	/**
	 * Metodo que permite ejecutar operaciones contra ALTAIR
	 * @throws Throwable
	 */
	protected void ejecutarOperacion (OrdenOperacion beanOperacion, Object operacion) throws Throwable {		
		
		/*Logger.info(this,"RECIBIENDO OPERACION PARA SER ENVIADA A ALTAIR de TIPO: " +beanOperacion.getTipoTransaccionFinanc() +"...");
		Logger.info(this,"Numero retención ALTAIR" + beanOperacion.getNumeroRetencion());
		Logger.info(this,"USUARIO ENVIADO ES: '"+nombreUsuario+"'");*/
		 
		if (operacion instanceof B501) { //Debito	
		   //Logger.info(this,"APLICANDO DEBITO..");
		   clienteWs = ClienteWs.crear("getB501", contexto);
		   B501Respuesta respuesta = (B501Respuesta) clienteWs.enviarYRecibir(
				   operacion, B501.class, B501Respuesta.class, nombreUsuario, ipTerminal);
		   
		   //Se verifica si hay respuesta de número de movimiento por parte del servicios ALTAIR
		   if (respuesta.getBGMCAB() == null){
			   throw new Exception ("Objeto BGMCAB null");
		   } else{
			   if (Long.parseLong(respuesta.getBGMCAB().getNumeroDeMovimiento())<=0){
				   throw new Exception ("No existe número de movimiento asociado a la respuesta ");
			   }
		   }		   
		   beanOperacion.setStatusOperacion(ConstantesGenerales.STATUS_APLICADA);		   		   
		   beanOperacion.setNumeroMovimiento(obtenerNumeroNio(respuesta.getBGMCAB()));
		} else if(operacion instanceof BV61){ //Credito
		   //Logger.info(this,"APLICANDO CREDITO..");
		   clienteWs = ClienteWs.crear("getBV61", contexto);
		   BV61Respuesta respuesta = (BV61Respuesta) clienteWs.enviarYRecibir(
				   operacion, BV61.class, BV61Respuesta.class, nombreUsuario, ipTerminal);
		   //Se verifica si hay respuesta de número de movimiento por parte del servicios ALTAIR
		   if (respuesta.getBGMCAB() == null){
			   throw new Exception ("Objeto BGMCAB null");
		   } else{
			   if (Long.parseLong(respuesta.getBGMCAB().getNumeroDeMovimiento())<=0){
				   throw new Exception ("No existe número de movimiento asociado a la respuesta ");
			   }
		   }		   
		   beanOperacion.setStatusOperacion(ConstantesGenerales.STATUS_APLICADA);		   
		   beanOperacion.setNumeroMovimiento(obtenerNumeroNio(respuesta.getBGMCAB()));
		} else if (operacion instanceof B706){ //Bloqueo
			//Logger.info(this,"APLICANDO BLOQUEO..");
		   clienteWs = ClienteWs.crear("getB706", contexto);
		   BGM7061 respuesta = (BGM7061) clienteWs.enviarYRecibir(
				   operacion, B706.class, BGM7061.class, nombreUsuario, ipTerminal);
		   beanOperacion.setStatusOperacion(ConstantesGenerales.STATUS_APLICADA);
		   beanOperacion.setNumeroRetencion(respuesta.getNumeroRetencion());
		} else if (operacion instanceof B756){ //Desbloqueo		
			try {
				//Logger.info(this,"APLICANDO DESBLOQUEO..");
				//NM25287 ITS-3080 Error en Cancelación de ordenes - BGE0263: LA RETENCION YA ESTA CANCELADA.IMPOSIBLE CANCELAR.
				clienteWs = ClienteWs.crear("getB756", contexto);
				B756Respuesta respuesta = (B756Respuesta) clienteWs.enviarYRecibir(operacion, B756.class, B756Respuesta.class, nombreUsuario, ipTerminal);
				beanOperacion.setStatusOperacion(ConstantesGenerales.STATUS_APLICADA);
			} catch (Exception e) {
				if(e.getMessage().contains(ERROR_RETENCION_CANCELADA)){
					Logger.info(this,"BLOQUEO NO ESTA ACTIVO -->"+beanOperacion.getIdOrden()+" , ERROR: "+e.getMessage());
					System.out.println("BLOQUEO NO ESTA ACTIVO -->"+beanOperacion.getIdOrden()+" , ERROR: "+e.getMessage());
					beanOperacion.setStatusOperacion(ConstantesGenerales.STATUS_APLICADA);
				}else{
					throw e;
				}				
			}
		} else if (operacion instanceof B731){ //Reverso
			boolean bgmcabNulo = false;
			//Logger.info(this,"APLICANDO REVERSO..");
			clienteWs = ClienteWs.crear("getB731", contexto);
			B731Respuesta respuesta = (B731Respuesta) clienteWs.enviarYRecibir(
					operacion, B731.class, B731Respuesta.class, nombreUsuario, ipTerminal);
			
			
			if (respuesta.getBGMCRI()==null){
				throw new Exception ("Objeto BGMCRI nulo para el reverso de operaciones");	
			}else{
				if (respuesta.getBGMCAB()==null){
					bgmcabNulo = true;	
				}else{
					Logger.info(this,"Número de movimiento: " +  respuesta.getBGMCAB().get(0).getNumeroDeMovimiento());
				}
				//Recorremos el array para buscar objetos de tipo BGMCAB
				for(Object obj: respuesta.getBGMCRI()){
					if (obj instanceof BGMCAB){
						bgmcabNulo = false;
						BGMCAB objBgmcab = (BGMCAB) obj;
						Logger.info(this,"Número de movimiento: " +  objBgmcab.getNumeroDeMovimiento());
						break;
					}
				}
				if (bgmcabNulo){
					throw new Exception ("Objeto BGMCAB nulo para el reverso de operaciones");	
				}	
			}						
			beanOperacion.setStatusOperacion(ConstantesGenerales.STATUS_APLICADA);	
		}
		
		
		//Logger.info(this,"TERMINADA LA EJECUCION DE OPERACION "+beanOperacion.getTipoTransaccionFinanc());
	}
	
	/**Obtiene y arma el número NIO de la respuesta recibida de altair al aplicar una operación de débito o crédito
	 * @param respuesta respuesta arrojada por ALTAIR
	 * @return una cadena con el código NIO*/
	public String obtenerNumeroNio(BGMCAB respuesta){
		String numeroNio = "";
		if (respuesta != null){
			numeroNio  = respuesta.getCodAplNio()+respuesta.getTerminalNio()+respuesta.getFechaNio()+respuesta.getHoraNio();
			//numeroNio = respuesta.getNumeroDeMovimiento();
		}
		return numeroNio;
		
	}
}
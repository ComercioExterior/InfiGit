package com.bdv.infi.logic.interfaz_altair;

import java.math.BigDecimal;
import java.util.ArrayList;
import javax.servlet.ServletContext;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import megasoft.db;
import com.bancovenezuela.comun.data.OrdenOperacion;
import com.bdv.infi.dao.IntentoOperacionDAO;
import com.bdv.infi.dao.OperacionDAO;
import com.bdv.infi.dao.Transaccion;
import com.bdv.infi.data.OperacionIntento;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionFinanciera;
import com.bdv.infi.logic.interfaz_altair.transaction.GenericaTransaccion;
import com.bdv.infi.util.Utilitario;
import com.bdv.infi.webservices.beans.B756;
import com.bdv.infi.webservices.beans.B731;

/**
 * Clase que contiene la l&oacute;gica para el proceso de las transacciones financieras
 * que contiene una toma de orden. <br>
 * Es la encargada de invocar los conectores para el proceso de las transacciones
 * financieras que deben ir a cada sistema. <br>
 * Adem&aacute;s es la responsable de almacenar los intentos de transacci&oacute;n en las tablas.
 **/
public class FactoryAltair extends AbstractFactoryAltair  {

	private Logger logger = Logger.getLogger(FactoryAltair.class);	
	String mensaje = "";
	String error = ""; //Mensaje de error obtenido del bus de servicio
	
	/**
	 * Constructor de la clase
	 * @param dso :DatSource a utilizar para acceder a la base de datos
	 */
	public FactoryAltair (DataSource dso, ServletContext contexto) {
		super(dso, contexto);		
	}
	
	/**
	 * Constructor de la clase
	 * @param dso :DatSource a utilizar para acceder a la base de datos
	 */
	public FactoryAltair (Transaccion transaccion, ServletContext contexto) {
		super(transaccion, contexto);
	}
	
	public FactoryAltair (Transaccion transaccion, ServletContext contexto, boolean indicadorFalla) {
		super(transaccion, contexto, indicadorFalla);
	}

	
	/**
	 * Constructor de la clase
	 * @param dso :DatSource a utilizar para acceder a la base de datos
	 * @param contexto contexto de la aplicación
	 * @param indicadorFalla debe enviarse verdadero si se desea detener la ejecución cuando una operación hacia
	 * ALTAIR falle
	 */
	public FactoryAltair (DataSource dso, ServletContext contexto, boolean indicadorFalla){
		super(dso, contexto,indicadorFalla);
	}		


	public void aplicarOrdenes(ArrayList<OrdenOperacion> listaOperaciones) throws Throwable {
		//nombreUsuario ="";
		IntentoOperacionDAO intOperacionDao = new IntentoOperacionDAO(this.dso);
		OperacionDAO operacionDao = new OperacionDAO(this.dso);
		ArrayList<String> consultas = new ArrayList<String>();
		boolean bolPaso = false; //Indica si entró el for de operaciones
		
		// 1.- Verificar si la lista de Ordenes tien informacion 			
		if (listaOperaciones.size() == 0) {
			throw new Exception("No hay operaciones para ejecutar");
		}
		
		try{
		
		// 2.-	Procesar la lista de Operaciones
		//		2.1.-	Armar la informacion de ejecucion de la operacion
		//		2.2.-	Armar los beanAltair
		//	3.-	Por cada Operacion: ejecutar el Servicio:
		//		3.1.- Preparar el servicio
		//		3.2.- Invocar el Servicio
		//		3.3.- Recibir la Respuesta
		//			- Si la respuesta es una excepcion de Read_timeout: rejecutar
		//			- Si la respuesta es una excepcion de ejecucion : marcar como fallida
		//			- Si la respuesta es un beanAltair-respuesta: marcar como ejecutada	
		//	4.-	Por cada Operacion: actualizar el Status
		//		analizar el resultado del intento de ejecucion	
		//	5.-	Por cada Orden: actualizar el Status
		//		analizar las operaciones asociadas:
		//		-	todas aplicadas 		--> PROCESADA
		//		-	al menos una fallida 	--> PENDIENTE
		for (OrdenOperacion beanOperacion : listaOperaciones) {
			
			if(beanOperacion.getStatusOperacion().equals(ConstantesGenerales.STATUS_APLICADA)){
				if (logger.isDebugEnabled()){
					logger.debug("OPERACION DE "+beanOperacion.getTipoTransaccionFinanc()+" EN STATUS APLICADA" );
					logger.debug("POR MONTO DE: "+beanOperacion.getMontoOperacion());
				}
				continue;
			}
			
			OperacionIntento beanIntento = new OperacionIntento();
			BigDecimal bdAux;
			bolPaso = true;
			
			//Arma el intento de operación
			beanIntento.setIdOperacion(beanOperacion.getIdOperacion());
			beanIntento.setIdOrden(beanOperacion.getIdOrden());			

			/*Logger.info(this,"idOrden       "+beanOperacion.getIdOrden());
			Logger.info(this,"idOperacion   "+beanOperacion.getIdOperacion());
			Logger.info(this,"TipoOperacion "+beanOperacion.getTipoTransaccionFinanc());			
			Logger.info(this,"CódigoOperacion "+beanOperacion.getCodigoOperacion());
			Logger.info(this,"TransaccionFinanciera "+beanOperacion.getIdTransaccionFinanciera());
			Logger.info(this,"NombreTransaccionFinanciera "+beanOperacion.getNombreOperacion());
			Logger.info(this,"MontoOperacion "+beanOperacion.getMontoOperacion());
			Logger.info(this,"RECIBIENDO OPERACION DE TIPO "+beanOperacion.getTipoTransaccionFinanc());
			Logger.info(this,"CHEQUEANDO OPERACION PARA VERIFICAR SI SE ENVIA A ALTAIR, TIPO: "+beanOperacion.getTipoTransaccionFinanc());*/
			if (!beanOperacion.getTipoTransaccionFinanc().equalsIgnoreCase(TransaccionFinanciera.DEBITO) && 
					!beanOperacion.getTipoTransaccionFinanc().equalsIgnoreCase(TransaccionFinanciera.CREDITO) &&
					!beanOperacion.getTipoTransaccionFinanc().equalsIgnoreCase(TransaccionFinanciera.BLOQUEO) &&
					!beanOperacion.getTipoTransaccionFinanc().equalsIgnoreCase(TransaccionFinanciera.DESBLOQUEO)) {
				logger.info("OPERACION DE "+beanOperacion.getTipoTransaccionFinanc()+". CONTINUAR CON LA SIGUIENTE OPERACION... ");
				continue;
			}else logger.info("OPERACION ES DE " +beanOperacion.getTipoTransaccionFinanc()+".. ENVIAR A ALTAIR...");

			//beanIntento = boOIntento.armarBean(beanOperacion, null);
			//listaIntentos.add(beanIntento);
		   //Logger.info(this,"PREPARANDO OPERACION PARA SER ENVIADA A ALTAIR... "+ beanOperacion.getTipoTransaccionFinanc());
			String nbModel = getClass().getPackage().getName();
			if (beanOperacion.getTipoTransaccionFinanc().equalsIgnoreCase(TransaccionFinanciera.DEBITO)) {
				nbModel = nbModel+".transaction.Debito";logger.info("OPER: "+beanOperacion.getTipoTransaccionFinanc());
			 }	else  if (beanOperacion.getTipoTransaccionFinanc().equalsIgnoreCase(TransaccionFinanciera.CREDITO)) {
				 nbModel = nbModel+".transaction.Credito";logger.info("OPER: "+beanOperacion.getTipoTransaccionFinanc());
			 } 	else  if (beanOperacion.getTipoTransaccionFinanc().equalsIgnoreCase(TransaccionFinanciera.BLOQUEO)) {
				 nbModel = nbModel+".transaction.Bloqueo";logger.info("OPER: "+beanOperacion.getTipoTransaccionFinanc());
			 } else {
				 nbModel = nbModel+".transaction.Desbloqueo";logger.info("OPER: "+beanOperacion.getTipoTransaccionFinanc());
			 } 
			 classAltair = (GenericaTransaccion) Thread.currentThread().getContextClassLoader().loadClass(nbModel).newInstance();
			 classAltair.setNumeroCuenta(beanOperacion.getNumeroCuenta());
			 classAltair.setSerialContable(beanOperacion.getSerialContable());
			 //classAltair.setFechaAplicacion(beanOOperacion.getFechaAplicar());
			 bdAux = beanOperacion.getMontoOperacion().setScale(2,BigDecimal.ROUND_HALF_EVEN);			 
			 classAltair.setMontoOperacion(eliminarPunto(bdAux.toString()));
			 classAltair.setSiglasMoneda(beanOperacion.getIdMoneda());
			 classAltair.setCodigoOperacion(beanOperacion.getCodigoOperacion());
			 classAltair.setCentroContable(beanOperacion.getCentroContable());
			 classAltair.setNumeroRetencion(beanOperacion.getNumeroRetencion());
			 
			 Object objAux =  classAltair.execute();
			 Class classAux = objAux.getClass();
			 //Logger.info(this,"beanAltair "+classAux.getName());
			 
			 //Ejecuta la operación según su tipo
			 try{
			   //Logger.info(this,"ENVIANDO OPERACION A ALTAIR... " +beanOperacion.getTipoTransaccionFinanc());
			   ejecutarOperacion(beanOperacion,objAux);
			   beanIntento.setIndicadorAplicado(1);	logger.info("OPERACION ENVIADA A ALTAIR DE TIPO: "+beanOperacion.getTipoTransaccionFinanc());		   
			   //Logger.info(this,"----------------------------------------------------------------------------------");
			 }catch (Throwable e){
				logger.error(e.getMessage()+ Utilitario.stackTraceException(e));
				beanIntento.setTextoError(e.getMessage());
				error = e.getMessage();
				beanOperacion.setStatusOperacion(ConstantesGenerales.STATUS_RECHAZADA);logger.info("OPERACION EN ESTATUS RECHAZADA.. "+beanOperacion.getTipoTransaccionFinanc());
			 }
			
			 //Guarda el intento de operación
			 consultas.add(intOperacionDao.insertar(beanIntento));
			 consultas.add(operacionDao.modificarOperacionStatusMovimiento(beanOperacion));
			 
			 if (this.indicadorFalla){
				 if (beanOperacion.getStatusOperacion().equals(ConstantesGenerales.STATUS_RECHAZADA)){
					 mensaje = "Error aplicando la transacción financiera y detenido el proceso de ejecución de operaciones";
					 logger.info("OPERACION DE: " +beanOperacion.getTipoTransaccionFinanc()+" RECHAZADA, DETENIDO EL PROCESO...LANZA EXCEPCION: "+mensaje);
					 throw new Exception(mensaje);
				 }
			 }
			 
		 }
		} catch (Exception e){
			logger.error("ERROR DENTRO DEL CICLO DE RECORRIDO DE OPERACIONES A SER ENVIADAS A ALTAIR.. ERROR:--> "+e.getMessage()+ Utilitario.stackTraceException(e));
			throw new Exception("Error al ejecutar la operación financiera contra ALTAIR. " + e.getMessage());
		} finally{
		   //Si ejecutó alguna operación ejecuta los querys generados
           if (bolPaso){
      		 //Arma el String de consultas a ejecutar
      		 String[] consultasFinales = new String[consultas.size()];
      		 int contador = 0;	 
      		 for (String consulta: consultas){
      			 consultasFinales[contador++] = consulta;      			 
      		 }
      		 
      		 //Ejecuta las instrucciones sql si la conexión es por transacción
      		 if (this.transaccion !=null){
      			 this.transaccion.ejecutarConsultas(consultasFinales);
      		 } else {
        		 db.execBatch(this.dso, consultasFinales);      			 
      		 }
           }           
		}
	}
	
	/**Aplica el reverso de la operación indicada
	 * @param beanOperacion Orden operación a la que se desea aplicar el reverso
	 * @throws Throwable Lanza una excepcion en caso de error*/
	public void aplicarReverso(OrdenOperacion beanOperacion) throws Throwable{
		//Verifica si es una operación de bloqueo la que se quiere reversar
	    if (beanOperacion.getTipoTransaccionFinanc().equalsIgnoreCase(TransaccionFinanciera.BLOQUEO)) {
	        //Aplica el desbloqueo de la operación
	    	B756 b756 = new B756();
	    	b756.setCodigoCtaCliente(beanOperacion.getNumeroCuenta());
	    	b756.setNumeroDeRetencion(beanOperacion.getNumeroRetencion());
	    	b756.setDivisa(beanOperacion.getIdMoneda());
	    	logger.info("Aplicando reverso por bloqueo con número de retención " + beanOperacion.getNumeroRetencion() + " a cuenta " + beanOperacion.getNumeroCuenta());	    	
	    	ejecutarOperacion(beanOperacion,b756);
	    } else if (beanOperacion.getTipoTransaccionFinanc().equalsIgnoreCase(TransaccionFinanciera.DEBITO) || beanOperacion.getTipoTransaccionFinanc().equalsIgnoreCase(TransaccionFinanciera.CREDITO)){
	    	B731 b731 = new B731(); //SE HACE AJUSTE Y SE INSTANCIA A LA CLASE DENTRO DEL MISMO IF NM32545
	    	b731.setCodigoCtaCliente(beanOperacion.getNumeroCuenta());
	    	b731.setDivisa(beanOperacion.getIdMoneda());
	    	BigDecimal bdAux = beanOperacion.getMontoOperacion().setScale(2,BigDecimal.ROUND_HALF_EVEN);
	    	String monto = eliminarPunto(bdAux.toString());	    	
	    	b731.setImporte(Utilitario.rellenarCaracteres(monto.toString(), '0', 15, false));
	    	//Debe crearse un atributo para esto NIO
	    	//b731.setNumeroDeMovimiento(beanOperacion.getNumeroMovimiento());
	    	b731.setNio(beanOperacion.getNumeroMovimiento());
	    	logger.info("Importe " + b731.getImporte());
	    	logger.info("Aplicando reverso por monto " + bdAux.doubleValue() + " y número de movimiento " + beanOperacion.getNumeroMovimiento() + " a tipo de " + beanOperacion.getTipoTransaccionFinanc());
	    	ejecutarOperacion(beanOperacion,b731);
		}
	}
	    	
	
	/**Elimina el . en la cadena de n&uacute;meros*/
	private String eliminarPunto(String cadena){
		int ocurrencia = cadena.indexOf(".");
		String resultado = cadena;
		if (ocurrencia > -1 ){
			resultado = cadena.substring(0,ocurrencia) + cadena.substring(ocurrencia+1);
		}
		return resultado;
	}
	
	/**Obtiene el último mensaje de error al aplicar operaciones contra ALTAIR*/
	public String getError(){
		return this.error;
	}
}
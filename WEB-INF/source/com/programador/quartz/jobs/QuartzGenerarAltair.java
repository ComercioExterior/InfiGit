package com.programador.quartz.jobs;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.sql.DataSource;

import megasoft.AppProperties;
import megasoft.db;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.bancovenezuela.comun.data.OrdenOperacion;
import com.bdv.infi.dao.OperacionDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.QuartzSchedulerDAO;
import com.bdv.infi.dao.UsuarioSeguridadDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaz_altair.FactoryAltair;
import com.bdv.infi.util.Utilitario;


/**
 * Clase encargada de ejecutar el proceso de ALTAIR automaticamente, dependiendo de como este configurado en el SCHEDULER
 * @author elaucho
 */
public class QuartzGenerarAltair implements Job {
	
	 /*** Variable de gestion de logs*/
	private Logger logger = Logger.getLogger(QuartzGenerarAltair.class);
    
    @SuppressWarnings("static-access")
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
    	
    	logger.info("Inicio proceso automático ALTAIR...");
    	
        try {
        	//Datasource principal de la aplicación
            DataSource dso = db.getDataSource(AppProperties.getProperty(
            		ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));
            
            //Datasource de seguridad
            DataSource dataSourceSeguridad = db.getDataSource(AppProperties.getProperty(
            		ConstantesGenerales.DATASOURCE_SEGURIDAD_SEPA));

            //DAO a utilizar
            QuartzSchedulerDAO quartzSchedulerDAO = new QuartzSchedulerDAO(dso);
            SimpleDateFormat formatofecha = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
            
            //ArrayList que contendra el id de las operaciones  que se enviaran hacia ALTAIR
            ArrayList<Long> idOperacionesArraylist = new ArrayList<Long>();
            OperacionDAO operacionDAO = new OperacionDAO(dso);
            
            //Objetos y variables a utilizar
            OrdenOperacion ordenOperacion;
            ArrayList<OrdenOperacion> ordenOperacionArrayList = new ArrayList<OrdenOperacion>();
            /*
             * Buscamos las ordenes que contengan operaciones financieras con los siguientes criterios:
             * status_operacion en espera o rechazada,moneda local y que su fecha valor sea igual al sysdate
             * No se buscan operaciones de cambio
             */
            quartzSchedulerDAO.listarOrdenesOperacionAltair();
            
            logger.info("Operaciones encontradas " + quartzSchedulerDAO.getDataSet().count());
            if (quartzSchedulerDAO.getDataSet().count() > 0) {
                quartzSchedulerDAO.getDataSet().first();                
                
                while (quartzSchedulerDAO.getDataSet().next()) {                	
                	if(quartzSchedulerDAO.getDataSet().getValue("cuenta")== null || 
                			quartzSchedulerDAO.getDataSet().getValue("cuenta").equals("") || 
                			quartzSchedulerDAO.getDataSet().getValue("codigo_operacion") == null || 
                			quartzSchedulerDAO.getDataSet().getValue("codigo_operacion").equals("0") || 
                			quartzSchedulerDAO.getDataSet().getValue("codigo_operacion").equals("")
                			){
                		logger.warn("La operación " + quartzSchedulerDAO.getDataSet().getValue("ORDENE_OPERACION_ID") 
                				+ " no presenta los datos completos para su envío. Verifique número de cuenta y código de operación");                		
	                    logger.warn("Operación número: " + quartzSchedulerDAO.getDataSet().getValue("ORDENE_OPERACION_ID"));	                    
	                    logger.warn("Cuenta: " + quartzSchedulerDAO.getDataSet().getValue("cuenta"));
	                    logger.warn("Código de Operación: " + quartzSchedulerDAO.getDataSet().getValue("codigo_operacion"));
	                    logger.warn(" ");
                	}else{	                	
	                    ordenOperacion = new OrdenOperacion();
	                    ordenOperacion.setAplicaReverso(quartzSchedulerDAO.getDataSet().getValue("APLICA_REVERSO").equals("1")? true : false);
	                    ordenOperacion.setFechaAplicar(formatofecha.parse(quartzSchedulerDAO.getDataSet().getValue("FECHA_APLICAR")));
	                    ordenOperacion.setIdMoneda(quartzSchedulerDAO.getDataSet().getValue("MONEDA"));
	                    ordenOperacion.setIdOperacion(Long.parseLong(quartzSchedulerDAO.getDataSet().getValue("ORDENE_OPERACION_ID")));
	                    ordenOperacion.setIdOrden(Long.parseLong(quartzSchedulerDAO.getDataSet().getValue("ORDENE_ID")));
	                    ordenOperacion.setIdTitulo(quartzSchedulerDAO.getDataSet().getValue("TITULO_ID"));
	                    ordenOperacion.setIdTransaccionFinanciera(quartzSchedulerDAO.getDataSet().getValue("TRNFIN_ID"));
	                    ordenOperacion.setInComision(Integer.parseInt(quartzSchedulerDAO.getDataSet().getValue("IN_COMISION")));
	                    ordenOperacion.setMontoOperacion(new BigDecimal(quartzSchedulerDAO.getDataSet().getValue("MONTO_OPERACION")));
	                    ordenOperacion.setSerialContable(quartzSchedulerDAO.getDataSet().getValue("SERIAL"));
	                    ordenOperacion.setStatusOperacion(quartzSchedulerDAO.getDataSet().getValue("STATUS_OPERACION"));
	                    ordenOperacion.setTasa(new BigDecimal(quartzSchedulerDAO.getDataSet().getValue("TASA")));
	                    ordenOperacion.setTipoTransaccionFinanc(quartzSchedulerDAO.getDataSet().getValue("TRNF_TIPO"));
	                    ordenOperacion.setNumeroCuenta(quartzSchedulerDAO.getDataSet().getValue("cuenta"));
	                    ordenOperacion.setCodigoOperacion(quartzSchedulerDAO.getDataSet().getValue("codigo_operacion"));
	
	                    logger.info("Enviando operación número: " + ordenOperacion.getIdOperacion());
	                    logger.info("Monto enviado: " + ordenOperacion.getMontoOperacion());
	                    logger.info("Cuenta: " + ordenOperacion.getNumeroCuenta());
	                    logger.info("------");
	                    
	                    ordenOperacionArrayList.add(ordenOperacion);
	                    
	                    //Se agrega la operacion financiera que se enviara hacia altair al arraylist
	                    idOperacionesArraylist.add(ordenOperacion.getIdOperacion());
                	}
                } //FIn While             
            }
            
            logger.info("Operaciones correctas " + ordenOperacionArrayList.size());
            //Solo va contra ALTAIR si hay operaciones por enviar
            if(ordenOperacionArrayList.size()>0){

	            //Obtener direccion ip de la maquina donde corre la aplicacion (Servidor)
	            InetAddress direccion = InetAddress.getLocalHost();
	            String direccionIpstr = direccion.getHostAddress();
	
	            FactoryAltair factoryAltair = new FactoryAltair(dso, null);
	            factoryAltair.ipTerminal = direccionIpstr;
	            factoryAltair.nombreUsuario = ParametrosDAO.listarParametros(ParametrosSistema.USUARIO_WEB_SERVICES,dso);
	            
	            //DAO a utilizar
	            UsuarioSeguridadDAO usuarioSeguridadDAO = new UsuarioSeguridadDAO(dataSourceSeguridad);
	            
	            //Validamos que el NM exista en sepa y este asociado a INFI
	            if(!usuarioSeguridadDAO.esUsuarioValidoSepa(factoryAltair.nombreUsuario))
	            {
	            	throw new Exception(ConstantesGenerales.MENSAJE+"["+factoryAltair.nombreUsuario+"]");
	            }
	            	
	            //Se aplican las ordenes
	            factoryAltair.aplicarOrdenes(ordenOperacionArrayList);
	            
	            //Recorremos las operaciones enviadas, para de haber quedado aplicadas se actualizan sus operaciones relacionadas
	            for(int i=0;i<idOperacionesArraylist.size();i++)
	            {
	            	//Si la operacion financiera queda aplicada, se actualizan sus operaciones relacionadas|
	            	operacionDAO.isAplicada(idOperacionesArraylist.get(i));
	            }
            }            
        } catch (Exception e) {
        	try {
        		logger.error(e.getMessage()+ Utilitario.stackTraceException(e));
			} catch (Exception e1) {
				e.printStackTrace();
			}
        	
        	if(e.getMessage().indexOf(ConstantesGenerales.MENSAJE)>-1)
        		throw new JobExecutionException(e);
        		
            
            logger.error(e.getMessage());
            
        } catch (Throwable e) {
        	try {
        		logger.error(e.getMessage()+ Utilitario.stackTraceException(e));
			} catch (Exception e1) {
				e.printStackTrace();
			}
        	if(e.getMessage().indexOf(ConstantesGenerales.MENSAJE)>-1)
        		throw new JobExecutionException(e.getMessage());
        	
            
        } finally {
        	
        	logger.info("Finalizado proceso automático ALTAIR...");
        } //FIN FINALLY
    } //FIN EXECUTE
} //FIN CLASE

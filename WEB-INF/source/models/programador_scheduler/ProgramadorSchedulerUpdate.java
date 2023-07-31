package models.programador_scheduler;


//import java.util.ArrayList;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import com.bdv.infi.dao.QuartzSchedulerDAO;
/*import com.bdv.infi.data.Cron;
import com.bdv.infi.data.Job;
import com.bdv.infi.data.JobDetail;
import com.bdv.infi.data.Quartz;
import com.bdv.infi.data.Trigger;*/
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.util.Utilitario;
/*import com.programador.quartz.jobs.QuartzGenerarAltair;
import com.programador.quartz.jobs.QuartzGenerarLiquidacion;
import com.programador.quartz.jobs.QuartzGenerarOpics;
import com.programador.quartz.jobs.QuartzGenerarSwift;
import com.programador.quartz.jobs.QuartzPruebaGerardo;*/
import megasoft.AppProperties;
import megasoft.DataSet;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;

/**
 * Clase que actualiza como y cuando se desea ejecutar las tareas del SCHEDULER<br>
 * Al modificar cuando y como se desea ejecutar la tarea, se buscan aquellas que se encuentren activas,
 * para procesar dichas tareas.<br>
 * De igual forma se limpia la fabrica de horarios para al actualizar tiempos se genere un nuevo SchedulerFactory y
 * asi se limita a que no corran otros hilos fuera del Factory<br>
 * @author elaucho
 */
public class ProgramadorSchedulerUpdate extends MSCModelExtend{
	
	
	/*** Logger APACHE*/
	private Logger logger = Logger.getLogger(ProgramadorSchedulerUpdate.class);
	
	@Override
	public void execute() throws Exception {
		QuartzSchedulerDAO schedulerDAO = new QuartzSchedulerDAO(_dso);
		
		//Obtiene los identificadores de las tareas cuyos horarios de ejecución deben actualizarse
		String concatIdTareas = _req.getParameter("id_tareas");
		String[] arrayIdTareas = concatIdTareas.split(",");
						
		//Se actualiza el horario de ejecución de cada una de las tareas
		for(int i=0;i<arrayIdTareas.length;i++){
			//Obtiene el ID de la tarea cuyo horario se va a actualizar
			String idTarea = arrayIdTareas[i];
			
			//Obtiene los campos que definen el horario de ejecución de la tarea actual
			String dia = _req.getParameter("cmb_dia_" + idTarea);
			String hora = _req.getParameter("cmb_hora_" + idTarea);
			String minuto = _req.getParameter("cmb_minuto_" + idTarea);
			String horaDesde = _req.getParameter("cmb_hora_desde_" + idTarea);
			String horaHasta = _req.getParameter("cmb_hora_hasta_" + idTarea);
			String cadaMinuto = _req.getParameter("cmb_cada_minuto_" + idTarea);
			String estado = _req.getParameter("cmb_estado_" + idTarea);		
			
			//Se analizan las combinaciones de los campos para determinar el horario de 
			//ejecución de la tarea y ejecutar la actualización en BD
			StringBuffer cronExpresion = new StringBuffer();
			StringBuffer valueScreen = new StringBuffer();
			
			//Si el usuario definio los campos "hora desde", "hora hasta" y "cada minuto", entonces
			//la tarea se ejecutará de Lunes a Viernes en el rango de horas cada 'n' minutos. Ejemplo
			//Hora Desde = 07:00 am   Hora Hasta = 05:00 pm  Cada Minuto = 15  ; Entonces 
			//	La tarea se ejecutará de Lunes a Viernes de 07:00 am hasta 05:00 pm cada 15 minutos
			//  cronExpresion = 0 0/15 7-17 * * ?
			//	valueScreen = 0/0/0/7/17/15			
			if(horaDesde!=null && !horaDesde.equals("0") && horaHasta!=null && !horaHasta.equals("0") && cadaMinuto!=null && !cadaMinuto.equals("0")){
				//Define la expresion a actualizar en el campo cron_expresion 
				cronExpresion.append("0 0/").append(cadaMinuto).append(" ").append(horaDesde).append("-");
				cronExpresion.append(horaHasta).append(" * * ?");
	
				//Define el valor a actualizar en el campo value_screen 
				//NOTA: Esta valor siempre es ordenado por dia/hora/minuto/hora_desde_/hora_hasta/cada_minuto				
				valueScreen.append("0/0/0/").append(horaDesde).append("/");
				valueScreen.append(horaHasta).append("/").append(cadaMinuto);
				
				//Actualiza la tarea actual en la base de datos y continua con la siguiente iteración
				schedulerDAO.updateProcesoScheduler(Long.parseLong(idTarea), cronExpresion.toString(), estado,valueScreen.toString());
				continue;			
			}
			
			//Si el usuario definió los campos "día", "hora" y "minuto", entonces
			//la tarea se ejecutará un determinado día de la semana a una hora y minutos determinados. Ejemplo:
			//Dia = Lunes   Hora= 10 am   Minuto= 30   ; Entonces
			//  La tarea se ejecutara los días Lunes a las 10:30 am
			//  cronExpresion = 0 30 10 ? * MON
			//	valueScreen = MON/10/30/0/0/0			
			if(dia!=null && !dia.equals("0") && hora!=null && !hora.equals("0") && minuto!=null && !minuto.equals("0")){
				//Define la expresion a actualizar en el campo cron_expresion
				cronExpresion.append("0 ").append(minuto).append(" ").append(hora);
				cronExpresion.append(" ? * ").append(dia);

				//Define el valor a actualizar en el campo value_screen 
				//NOTA: Esta valor siempre es ordenado por dia/hora/minuto/hora_desde_/hora_hasta/cada_minuto
				valueScreen.append(dia).append("/").append(hora).append("/").append(minuto);
				valueScreen.append("/0/0/0");
				
				//Actualiza la tarea actual en la base de datos y continua con la siguiente iteración
				schedulerDAO.updateProcesoScheduler(Long.parseLong(idTarea), cronExpresion.toString(), estado,valueScreen.toString());
				continue;
			}
			
			//Si el usuario definió los campos "día" y "hora", entonces
			//la tarea se ejecutará un determinado día de la semana a una hora determinada. Ejemplo:
			//Dia = Lunes   Hora= 10 am   ; Entonces
			//  La tarea se ejecutara los días Lunes a las 10:00 am
			//  cronExpresion = 0 0 10 ? * MON
			//	valueScreen = MON/22/0/0/0/0			
			if(dia!=null && !dia.equals("0") && hora!=null && !hora.equals("0") && (minuto==null || minuto.equals("0")) ){
				//Define la expresion a actualizar en el campo cron_expresion
				cronExpresion.append("0 0 ").append(hora).append(" ? * ").append(dia);

				//Define el valor a actualizar en el campo value_screen 
				//NOTA: Esta valor siempre es ordenado por dia/hora/minuto/hora_desde_/hora_hasta/cada_minuto
				valueScreen.append(dia).append("/").append(hora).append("/0/0/0/0");
				
				//Actualiza la tarea actual en la base de datos y continua con la siguiente iteración
				schedulerDAO.updateProcesoScheduler(Long.parseLong(idTarea), cronExpresion.toString(), estado,valueScreen.toString());
				continue;
			}

			//Si el usuario definió los campos "día" y "minuto", entonces
			//la tarea se ejecutará un determinado día de la semana cada 'n' minutos. Ejemplo:
			//Dia = Lunes   Minuto= 30   ; Entonces
			//  La tarea se ejecutara los días Lunes cada 30 minutos
			//  cronExpresion = 0 0/30 * ? * MON
			//	valueScreen = MON/0/30/0/0/0			
			if(dia!=null && !dia.equals("0") && minuto!=null && !minuto.equals("0") && (hora==null || hora.equals("0"))){
				//Define la expresion a actualizar en el campo cron_expresion
				cronExpresion.append("0 0/").append(minuto).append(" * ? * ").append(dia);
				
				//Define el valor a actualizar en el campo value_screen 
				//NOTA: Esta valor siempre es ordenado por dia/hora/minuto/hora_desde_/hora_hasta/cada_minuto
				valueScreen.append(dia).append("/0/").append(minuto).append("/0/0/0");
				
				//Actualiza la tarea actual en la base de datos y continua con la siguiente iteración
				schedulerDAO.updateProcesoScheduler(Long.parseLong(idTarea), cronExpresion.toString(), estado,valueScreen.toString());
				continue;			
			}
			
			//Si el usuario definió los campos "hora" y "minuto", entonces 
			//la tarea se ejecutará todos los dias de la semana a una hora y minutos determinados. Ejemplo:
			//Hora = 03 pm   Minuto= 15   ; Entonces
			//  La tarea se ejecutara los días a las 03:15 pm
			//  cronExpresion = 0 15 15 ? * *
			//	valueScreen = 0/15/15/0/0/0			
			if(minuto!=null && !minuto.equals("0") && hora!=null && !hora.equals("0") && (dia==null || dia.equals("0"))){
				//Define la expresion a actualizar en el campo cron_expresion
				cronExpresion.append("0 ").append(minuto).append(" ").append(hora).append(" ? * *");

				//Define el valor a actualizar en el campo value_screen 
				//NOTA: Esta valor siempre es ordenado por dia/hora/minuto/hora_desde_/hora_hasta/cada_minuto
				valueScreen.append("0/").append(hora).append("/").append(minuto).append("/0/0/0");
					
				//Actualiza la tarea actual en la base de datos y continua con la siguiente iteración
				schedulerDAO.updateProcesoScheduler(Long.parseLong(idTarea), cronExpresion.toString(), estado,valueScreen.toString());
				continue;
			}
			
			//Si el usuario definió solamente el campo "dia", entonces 
			//la tarea se ejecutará un determinado día de la semana cada 3 horas (por defecto). Ejemplo:
			//Día = Lunes  ; Entonces
			//  La tarea se ejecutará los días Lunes cada 3 horas
			//  cronExpresion = 0 * 0/3 ? * MON
			//	valueScreen = MON/0/0/0/0/0
			if(dia!=null && !dia.equals("0") && (minuto==null || minuto.equals("0")) && (hora==null || hora.equals("0")) ){
				//Define la expresion a actualizar en el campo cron_expresion
				cronExpresion.append("0 * 0/3 ? * ").append(dia);

				//Define el valor a actualizar en el campo value_screen 
				//NOTA: Esta valor siempre es ordenado por dia/hora/minuto/hora_desde_/hora_hasta/cada_minuto
				valueScreen.append(dia).append("/0/0/0/0/0");
				
				//Actualiza la tarea actual en la base de datos y continua con la siguiente iteración
				schedulerDAO.updateProcesoScheduler(Long.parseLong(idTarea), cronExpresion.toString(), estado,valueScreen.toString());
				continue;
			}
			
			//Si el usuario definió solamente el campo "hora", entonces 
			//la tarea se ejecutará todos los días a una hora determinada. Ejemplo:
			//Hora = 12 pm  ; Entonces
			//  La tarea se ejecutará todos los días a las 12:00 pm
			//  cronExpresion = 0 0 12 ? * *
			//	valueScreen = 0/12/0/0/0/0			 
			if(hora!=null && !hora.equals("0") && (minuto==null || minuto.equals("0")) && (dia==null || dia.equals("0")) ){
				//Define la expresion a actualizar en el campo cron_expresion
				cronExpresion.append("0 0 ").append(hora).append(" ? * *");

				//Define el valor a actualizar en el campo value_screen 
				//NOTA: Esta valor siempre es ordenado por dia/hora/minuto/hora_desde_/hora_hasta/cada_minuto
				valueScreen.append("0/").append(hora).append("/0/0/0/0");
				
				//Actualiza la tarea actual en la base de datos y continua con la siguiente iteración
				schedulerDAO.updateProcesoScheduler(Long.parseLong(idTarea), cronExpresion.toString(), estado,valueScreen.toString());
				continue;
			}
			
			//Si el usuario definió solamente el campo "minuto", entonces 
			//la tarea se ejecutará todos los días cada 'n' minutos. Ejemplo:
			//Minuto = 30  ; Entonces
			//  La tarea se ejecutará todos los días cada 30 minutos
			//  cronExpresion = 0 0/30 * * * ?
			//	valueScreen = 0/0/30/0/0/0			 
			if(minuto!=null && !minuto.equals("0") && (hora==null || hora.equals("0")) && (dia==null || dia.equals("0")) ){
				//Define la expresion a actualizar en el campo cron_expresion
				cronExpresion.append("0 0/").append(minuto).append(" * * * ?");

				//Define el valor a actualizar en el campo value_screen 
				//NOTA: Esta valor siempre es ordenado por dia/hora/minuto/hora_desde_/hora_hasta/cada_minuto
				valueScreen.append("0/0/").append(minuto).append("/0/0/0");	
					
				//Actualiza la tarea actual en la base de datos y continua con la siguiente iteración
				schedulerDAO.updateProcesoScheduler(Long.parseLong(idTarea), cronExpresion.toString(), estado,valueScreen.toString());
				continue;
			}
			
			//Si el usuario dejó en blanco los campos "dia", "hora", "minuto", "hora desdes", 
			//"hora hasta" y "cada minuto", entonces
			//Se define la expresión a actualizar en el campo cron_expresión que indica que la
			//tarea se ejecutará todos los días cada 2 horas.
			//  cronExpresion = * * 0/2 * * *
			//	valueScreen = 0/0/0/0/0/0
			if( (dia==null || dia.equals("0")) && (hora==null || hora.equals("0")) && (minuto==null || minuto.equals("0")) && 
				(horaDesde==null || horaDesde.equals("0")) && (horaHasta==null || horaHasta.equals("0")) && (cadaMinuto==null || cadaMinuto.equals("0")) ){
				//Define la expresion a actualizar en el campo cron_expresion
				cronExpresion.append("* * 0/2 * * *");

				//Define el valor a actualizar en el campo value_screen 
				//NOTA: Esta valor siempre es ordenado por dia/hora/minuto/hora_desde_/hora_hasta/cada_minuto
				valueScreen.append("0/0/0/0/0/0");	
					
				//Actualiza la tarea actual en la base de datos y continua con la siguiente iteración
				schedulerDAO.updateProcesoScheduler(Long.parseLong(idTarea), cronExpresion.toString(), estado,valueScreen.toString());
				continue;
			}
			
		}
		
		//Inicializa el scheduler con los nuevos horarios de las tareas
		this.inicializarScheduler();
				
		
	}//Fin execute

	/*public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();
			
		if (flag)
		{
			String horaDesdeArray[]   = _req.getParameterValues("hora_desde");
			String horaHastaArray[]	  = _req.getParameterValues("hora_hasta");
			String cadaMinutoArray[]  = _req.getParameterValues("cada_minuto");	

			for(int i = 0;i<horaDesdeArray.length;i++)
			{
					int registro = 1+i;
				if (horaDesdeArray[i]!=null && !horaDesdeArray[i].equalsIgnoreCase("0") || horaHastaArray[i]!=null && !horaHastaArray[i].equalsIgnoreCase("0") || cadaMinutoArray[i]!=null && !cadaMinutoArray[i].equalsIgnoreCase("0"))		
				{	
					if (horaDesdeArray[i]==null || horaDesdeArray[i].equalsIgnoreCase("0"))
					{  
						_record.addError("Hora/Desde","Este campo es obligatorio para procesar el formulario para el registro "+registro);
							flag = false;
					}
					if (horaHastaArray[i]==null || horaHastaArray[i].equalsIgnoreCase("0"))
					{  
						_record.addError("Hora/Hasta","Este campo es obligatorio para procesar el formulario para el registro "+registro);
							flag = false;
					}	
					if (cadaMinutoArray[i]==null || cadaMinutoArray[i].equalsIgnoreCase("0"))
					{  
						_record.addError("Cada/Minuto","Este campo es obligatorio para procesar el formulario para el registro "+registro);
							flag = false;
					}	
				}
			}//fin for			
		}//fin if(flag)		
		return flag;
	}//fin isValid*/
	
	/**
	 * Inicializa las tareas para el programador de tareas
	 * @throws Exception
	 */
	public void inicializarScheduler()throws Exception{
		
		DataSource _dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));
		QuartzSchedulerDAO quartzSchedulerDAO = new QuartzSchedulerDAO(_dso);
		//Quartz quartz 						  = new Quartz();
				
		try {
			//Crea una instancia de la clase SchedulerFactory, que representa una fabrica de objetos
			//de la clase Scheduler			
			SchedulerFactory schedulerFactory = new org.quartz.impl.StdSchedulerFactory();

			//Crea el objeto que representa el scheduler que se encarga de ejecutar las tareas programadas			
			Scheduler scheduler = schedulerFactory.getScheduler();

			//Elimina las tareas programadas para luego crearlas de nuevo
			String[] groups = scheduler.getTriggerGroupNames();
			//GRUPOS
	        for (int i = 0; i < groups.length; i++) {
	            String[] names = scheduler.getTriggerNames(groups[i]);
	            for (int j = 0; j < names.length; j++)
	            	scheduler.unscheduleJob(names[j], groups[i]);
	        }//fin for
	        //TAREAS
	        groups = scheduler.getJobGroupNames();
	        for (int i = 0; i < groups.length; i++) {
	            String[] names = scheduler.getJobNames(groups[i]);
	            for (int j = 0; j < names.length; j++)
	            	scheduler.deleteJob(names[j], groups[i]);
	        }//fin for
	        
	        //Se inicia el Scheduler
			scheduler.start();
			
			//Obtiene el DataSet con la información de las tareas activas
			quartzSchedulerDAO.listarTareasActivasScheduler();
			DataSet _tareasActivas = quartzSchedulerDAO.getDataSet();
			
			//Programa la ejecución de cada una de las tareas activas en el Scheduler
			while (_tareasActivas.next()){
				
				String nombreTarea = _tareasActivas.getValue("NAME");
				String grupoTarea = _tareasActivas.getValue("GRUPO");
				String jobClass = _tareasActivas.getValue("JOB_CLASS");
				//String nombreTrigger;
				//String grupoTrigger;
				//String nombreJob;
				//String grupoJob;
				String cronExpression = _tareasActivas.getValue("CRON_EXPRESSION");
				//String valueScreen;
				String descripcion = _tareasActivas.getValue("DESCRIPTION");
				
				Class<?> claseTarea = Class.forName(jobClass);
					
				org.quartz.JobDetail jobDetail = new org.quartz.JobDetail(nombreTarea,null,claseTarea);
				CronTrigger cronTrigger = new CronTrigger(grupoTarea,Scheduler.DEFAULT_GROUP);
				cronTrigger.setCronExpression(cronExpression);
					
				//Conecta disparador(Trigger) con tarea (JobDetail)
				scheduler.scheduleJob(jobDetail, cronTrigger);	
				
				logger.info("Scheduler " + descripcion + " tarea activada");				
			}
			
					
		//Crea tarea QuartzPruebaGerardo
		/*org.quartz.JobDetail jobDetail = new org.quartz.JobDetail("QuartzPruebaGerardo",null,QuartzPruebaGerardo.class);
		CronTrigger cronTrigger = new CronTrigger("QuartzPruebaGerardoGroup",Scheduler.DEFAULT_GROUP);
		cronTrigger.setCronExpression("0 0/15 14-17 * * ?");

		//Conecta disparador(Trigger) con tarea (JobDetail)
		scheduler.scheduleJob(jobDetail, cronTrigger);*/
		
		
		

	} catch (Exception e) {
		try {
			logger.error(e.getMessage()+" "+Utilitario.stackTraceException(e));
		} catch (Exception e1) {
			e.printStackTrace();
		}
		throw e;
	}
  }//fin inicializarScheduler
}//fin clase
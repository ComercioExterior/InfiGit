package models.programador_scheduler;

import org.apache.log4j.Logger;

import com.bdv.infi.dao.QuartzSchedulerDAO;
import com.bdv.infi.dao.UsuarioDAO;

import megasoft.DataSet;
import megasoft.Util;
import models.msc_utilitys.MSCModelExtend;
/**
 * Clase que muestra las tareas que pueden ser programadas para ser ejecutadas por el SCHEDULER
 * @author elaucho.
 */
public class ProgramadorSchedulerBrowse extends MSCModelExtend{

	/*** Logger APACHE*/
	private Logger logger = Logger.getLogger(ProgramadorSchedulerBrowse.class);
	
	@Override
	public void execute() throws Exception {
		System.out.println("llego a programador de tarea");
		UsuarioDAO usu = new UsuarioDAO(_dso);
		DataSet _concat_id_tareas = new DataSet();
		_concat_id_tareas.append("identificadores_tareas", java.sql.Types.VARCHAR);
		String concatIdTareas = "";
		
		//Dataset que publicara los cronExpression en html para mantenerlos en los controles
		DataSet _valueScreen = new DataSet();
		_valueScreen.append("id_tarea",java.sql.Types.VARCHAR);
		_valueScreen.append("dia",java.sql.Types.VARCHAR);
		_valueScreen.append("hora",java.sql.Types.VARCHAR);
		_valueScreen.append("minuto",java.sql.Types.VARCHAR);
		_valueScreen.append("hora_desde",java.sql.Types.VARCHAR);
		_valueScreen.append("hora_hasta",java.sql.Types.VARCHAR);
		_valueScreen.append("cada_minuto",java.sql.Types.VARCHAR);
		
		//DAO para listar los procesos		 
		QuartzSchedulerDAO quartzSchedulerDAO = new QuartzSchedulerDAO(_dso);
		System.out.println("");
		//Metodo que lista los procesos
		quartzSchedulerDAO.listarTareasScheduler(0L,usu.idUserSession(getUserName()));
		DataSet _tareas = quartzSchedulerDAO.getDataSet();
			
		while(_tareas.next()){			
			//Agregamos un fila al DataSet
			_valueScreen.addNew();
			
			logger.info("Tarea: " + _tareas.getValue("DESCRIPTION"));
			
			concatIdTareas += _tareas.getValue("id") + ",";
			
			String valueScreenArray[] = Util.split(_tareas.getValue("value_screen"), "/");
			
			_valueScreen.setValue("id_tarea",_tareas.getValue("id"));
			_valueScreen.setValue("dia",valueScreenArray[0]);
			_valueScreen.setValue("hora",valueScreenArray[1]);
			_valueScreen.setValue("minuto",valueScreenArray[2]);
			_valueScreen.setValue("hora_desde",valueScreenArray[3]);
			_valueScreen.setValue("hora_hasta",valueScreenArray[4]);
			_valueScreen.setValue("cada_minuto",valueScreenArray[5]);
			
		}
				
		if(!concatIdTareas.equals("")){
			concatIdTareas = concatIdTareas.substring(0,concatIdTareas.length()-1);
			_concat_id_tareas.addNew();
			_concat_id_tareas.setValue("identificadores_tareas", concatIdTareas);
		}
				
		logger.info("Id's tareas a mostrar: " + concatIdTareas);
					
		/*
		 * Publicacion del Dataset
		 */
		storeDataSet("concat_id_tareas",_concat_id_tareas);
		storeDataSet("tareas", _tareas);
		storeDataSet("valueScreen", _valueScreen);
		DataSet _estado = quartzSchedulerDAO.indicador();
		storeDataSet("estado", _estado);
		
		logger.info("DataSet's publicados...");
		
	}//FIN EXECUTE
}//FIN CLASE

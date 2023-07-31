package models.programador_scheduler;

import com.bdv.infi.dao.QuartzSchedulerDAO;
import com.bdv.infi.dao.UsuarioDAO;

import models.msc_utilitys.MSCModelExtend;

/**
 * Clase que muestra los procesos en el filtro de busqueda
 * @author elaucho
 */
public class ProgramadorSchedulerFilter extends MSCModelExtend{
	
	@Override
	public void execute() throws Exception {
		
		UsuarioDAO usu = new UsuarioDAO(_dso);
		/*
		 * DAO para listar las tareas
		 */
		QuartzSchedulerDAO quartzSchedulerDAO = new QuartzSchedulerDAO(_dso);
		/*
		 * Metodo que busca las tareas
		 */
		long idTarea = 0;
		quartzSchedulerDAO.listarTareasScheduler(idTarea,usu.idUserSession(getUserName()));
		/*
		 * Publicacion del dataset
		 */
		storeDataSet("tareas", quartzSchedulerDAO.getDataSet());
	}
}

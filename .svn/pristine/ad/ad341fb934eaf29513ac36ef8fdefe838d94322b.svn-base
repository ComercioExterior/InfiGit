package models.usuario_sesiones;

import com.bdv.infi.dao.UsuarioDAO;

import megasoft.Logger;
import models.msc_utilitys.MSCModelExtend;

/**
 * Clase que muestra las sesiones activas de la tabla active_session
 * @author elaucho
 */
public class UsuarioSesionesBrowse extends MSCModelExtend{
	
	@Override
	public void execute() throws Exception {
		
		String userid="";

		//DAO a utilizar
		UsuarioDAO usuarioDAO = new UsuarioDAO(_dso);
		//Se listan los registros de la tabla active sessions
		
		try {
			
			userid=_req.getParameter("userid").trim().toUpperCase();
			
		} catch (Exception e) {
			Logger.error("_req", "null");
		}
		
		usuarioDAO.listarSesionesActivas(userid);
		
		//Se publica el dataset
		storeDataSet("sesiones",usuarioDAO.getDataSet());
		
	}//fin execute
	
}//Fin clase

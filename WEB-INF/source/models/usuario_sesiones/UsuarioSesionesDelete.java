package models.usuario_sesiones;

import com.bdv.infi.dao.UsuarioDAO;
import models.msc_utilitys.MSCModelExtend;
/**
 * Elimina un registro de la tabla active session
 * @author elaucho
 */
public class UsuarioSesionesDelete extends MSCModelExtend{
	
	@Override
	public void execute() throws Exception {
		
		//DAO a utilizar
		UsuarioDAO usuarioDAO = new UsuarioDAO(_dso);
		
		//Se elimina el registro de base de datos
		usuarioDAO.eliminarUsuarioActivo(_req.getParameter("userid"));
	}

}

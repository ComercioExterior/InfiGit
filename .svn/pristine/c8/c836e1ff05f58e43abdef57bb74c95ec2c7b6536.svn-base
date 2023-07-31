package models.configuracion.usuario_blotter;

import com.bdv.infi.dao.UsuarioDAO;
import models.msc_utilitys.MSCModelExtend;
/**
 *	Clase encargada de insertar en 104 (Usuario Blotter)
 */
public class Insert extends MSCModelExtend {
	public void execute()throws Exception{
		String idBlotter	= _record.getValue("blotter_id");
		String idUser		=_record.getValue("userid");
		UsuarioDAO usuarioDAO=new UsuarioDAO(_dso);
		usuarioDAO.insertarUsuarioBlotter(idUser, idBlotter);
	}
}

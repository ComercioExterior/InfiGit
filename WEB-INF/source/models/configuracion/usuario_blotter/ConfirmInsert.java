package models.configuracion.usuario_blotter;

import com.bdv.infi.dao.UsuarioDAO;

import models.msc_utilitys.MSCModelExtend;

public class ConfirmInsert extends MSCModelExtend {
		public void execute()throws Exception{
			storeDataSet("datos",_record);
		}
		@Override
		public boolean isValid() throws Exception {
		//Definicion de variables
			boolean flag 		= super.isValid();
			String idBlotter	= _record.getValue("blotter_id");
			String idUser		=_record.getValue("userid");
			UsuarioDAO usuario	= new UsuarioDAO(_dso);
			boolean usuarioBloterExiste = usuario.listarUsuarioBlotter(idUser, idBlotter);
			if (flag)
			{
				if (usuarioBloterExiste)
				{  
					_record.addError("Usuario","El usuario ya se encuentra asociado a un Blotter");
					flag = false;
				}//fin if
			
			}//fin if externo
			return flag;
		}
}

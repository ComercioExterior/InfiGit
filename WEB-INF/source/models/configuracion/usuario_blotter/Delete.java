package models.configuracion.usuario_blotter;

import com.bdv.infi.dao.BlotterDAO;
import com.bdv.infi.dao.UsuarioDAO;
import com.bdv.infi.data.BloterDefinicion;

import models.msc_utilitys.MSCModelExtend;

public class Delete extends MSCModelExtend {
	public void execute()throws Exception{
		BlotterDAO blotterDAO=new BlotterDAO(_dso);
		String filtro="";
			filtro+=" userid='"+_record.getValue("userid")+"'";			
			filtro+=" and bloter_id='"+_record.getValue("bloter_id")+"'";			
		blotterDAO.eliminarUsuarioBlotter(filtro);
	}
}

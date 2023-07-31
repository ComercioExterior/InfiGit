package models.configuracion.usuario_blotter;

import com.bdv.infi.dao.BlotterDAO;
import com.bdv.infi.dao.UsuarioDAO;

import models.msc_utilitys.MSCModelExtend;

public class Edit extends MSCModelExtend {
	public void execute()throws Exception{
		BlotterDAO blotterDAO=new BlotterDAO(_dso);
		String filtro="";
		filtro +=" i104.USERID='"+_req.getParameter("userid")+"' ";
		filtro +=" and i104.BLOTER_ID='"+_req.getParameter("bloter_id")+"' ";
		blotterDAO.listarUsuariosBlotter(filtro);
		storeDataSet("tabla",blotterDAO.getDataSet());
	}
}

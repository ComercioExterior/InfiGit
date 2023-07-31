package models.configuracion.usuario_blotter;

import com.bdv.infi.dao.BlotterDAO;

import models.msc_utilitys.MSCModelExtend;

public class Update extends MSCModelExtend {
	public void execute()throws Exception{
		BlotterDAO blotterDAO=new BlotterDAO(_dso);
		String filtro="";
		filtro +=" USERID='"+_record.getValue("userid_viejo")+"' ";
		filtro +=" and BLOTER_ID='"+_record.getValue("blotterid_viejo")+"' ";
		blotterDAO.actualizarUsuariosBlotter(_record.getValue("userid"),_record.getValue("bloter_id"),filtro);
		storeDataSet("tabla",blotterDAO.getDataSet());
	}
}

package models.configuracion.usuario_blotter;

import com.bdv.infi.dao.BlotterDAO;
import com.bdv.infi.dao.UsuarioDAO;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

public class Browse extends MSCModelExtend {
	public void execute()throws Exception{
		BlotterDAO blotterDAO=new BlotterDAO(_dso);
		String filtro=" 1=1 ";
		if(_record.getValue("userid")!=null && !_record.getValue("userid").equals("")){
			filtro+=" and i104.userid='"+_record.getValue("userid")+"'";			
		}
		if(_record.getValue("blotter_id")!=null && !_record.getValue("blotter_id").equals("")){
			filtro+=" and i104.bloter_id='"+_record.getValue("blotter_id")+"'";			
		}
		blotterDAO.listarUsuariosBlotter(filtro);
		DataSet cant=new DataSet();
		cant.append("cantidad",java.sql.Types.VARCHAR);
		cant.addNew();
		cant.setValue("cantidad",String.valueOf(blotterDAO.getDataSet().count()));
		storeDataSet("tabla",blotterDAO.getDataSet());
		storeDataSet("cantidad",cant);
	}
}

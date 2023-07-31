package models.configuracion.usuario_blotter;

import com.bdv.infi.dao.BlotterDAO;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

public class Confirm extends MSCModelExtend {
	public void execute()throws Exception{
		DataSet table=new DataSet();
		table.append("userid",java.sql.Types.VARCHAR);
		table.append("bloter_id", java.sql.Types.VARCHAR);
		table.addNew();
		table.setValue("userid",_req.getParameter("userid"));
		table.setValue("bloter_id",_req.getParameter("bloter_id"));
		storeDataSet("tabla",table);
	}
}

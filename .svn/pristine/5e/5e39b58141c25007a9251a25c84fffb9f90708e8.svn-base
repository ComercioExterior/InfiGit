package models.configuracion.auditoria_actions;

import com.bdv.infi.dao.AuditoriaUrlLogConfigDAO;
import models.msc_utilitys.MSCModelExtend;

public class Table extends MSCModelExtend {
	
	public void execute() throws Exception{		
		
		String url = null;
		String enable = null;
		AuditoriaUrlLogConfigDAO auditaDAO = new AuditoriaUrlLogConfigDAO(_dso);	
		
		if(_record.getValue("url")!=null){
			url=_record.getValue("url");
		}
		if(_record.getValue("enable")!=null){
			enable=_record.getValue("enable");
		}
		
		//Realizar consulta
		auditaDAO.listar(url, enable);	
		storeDataSet("table", auditaDAO.getDataSet());
		storeDataSet("total", auditaDAO.getTotalRegistros());
		storeDataSet("indicador", auditaDAO.indicador());
	}
}
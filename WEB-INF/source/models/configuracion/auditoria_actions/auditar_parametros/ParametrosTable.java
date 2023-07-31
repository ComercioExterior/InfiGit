package models.configuracion.auditoria_actions.auditar_parametros;

import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.AuditoriaUrlParametersLogConfig;

public class ParametrosTable extends MSCModelExtend {
	
	public void execute() throws Exception{		
		
		String idConfig = null;
		AuditoriaUrlParametersLogConfig parametrosDAO = new AuditoriaUrlParametersLogConfig(_dso);	
		
		if(_record.getValue("id_config")!=null){
			idConfig=_record.getValue("id_config");
		}else{
			idConfig=getSessionObject("id_config").toString();
		}
		
		//Realizar consulta
		parametrosDAO.listarPorId(idConfig);	
		storeDataSet("table", parametrosDAO.getDataSet());
		
		_req.getSession().setAttribute("id_config", idConfig);
	}
}
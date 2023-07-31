package models.configuracion.auditoria_actions;

import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.AuditoriaUrlLogConfigDAO;

public class Edit extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		AuditoriaUrlLogConfigDAO auditaDAO = new AuditoriaUrlLogConfigDAO(_dso);
		
		String idConfig=null;
	
		if(_req.getParameter("id_config")!=null){
			idConfig = _req.getParameter("id_config");
		}
		
		//Realizar consulta
		auditaDAO.listarPorId(idConfig);
		//registrar los datasets exportados por este modelo
		storeDataSet("table", auditaDAO.getDataSet());
		storeDataSet("indicador",auditaDAO.indicador());
	}
}

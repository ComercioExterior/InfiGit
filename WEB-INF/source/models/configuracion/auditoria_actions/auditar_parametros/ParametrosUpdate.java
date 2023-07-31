package models.configuracion.auditoria_actions.auditar_parametros;

import megasoft.db;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.AuditoriaUrlParametersLogConfig;
import com.bdv.infi.data.UrlParametersLogConfig;

public class ParametrosUpdate extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		String sql ="";
		
		AuditoriaUrlParametersLogConfig parametroDAO = new AuditoriaUrlParametersLogConfig(_dso);
		UrlParametersLogConfig urlParametersLogConfig = new UrlParametersLogConfig();
		
		urlParametersLogConfig.setId_config(Integer.parseInt(_req.getParameter("id_config")));
		urlParametersLogConfig.setParametro(_req.getParameter("name1"));
		urlParametersLogConfig.setName(_req.getParameter("name"));
				
		//ensamblar SQL
		sql=parametroDAO.modificar(urlParametersLogConfig);
		//ejecutar query
		db.exec(_dso,sql);
	}
}

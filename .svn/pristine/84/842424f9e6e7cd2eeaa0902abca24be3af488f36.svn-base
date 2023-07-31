package models.configuracion.auditoria_actions.auditar_parametros;

import megasoft.db;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.AuditoriaUrlParametersLogConfig;
import com.bdv.infi.data.UrlParametersLogConfig;

public class ParametrosInsert extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		String sql ="";
		
		AuditoriaUrlParametersLogConfig auditaDAO = new AuditoriaUrlParametersLogConfig(_dso);
		UrlParametersLogConfig urlParametersLogConfig = new UrlParametersLogConfig();
		
		urlParametersLogConfig.setParametro(_req.getParameter("name"));
		urlParametersLogConfig.setId_config(Integer.parseInt(getSessionObject("id_config").toString()));
				
		//ensamblar SQL
		sql=auditaDAO.insertar(urlParametersLogConfig);
		//ejecutar query
		db.exec(_dso,sql);
	}
}

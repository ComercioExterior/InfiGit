package models.configuracion.auditoria_actions.auditar_parametros;

import megasoft.db;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.AuditoriaUrlParametersLogConfig;
import com.bdv.infi.data.UrlParametersLogConfig;

public class ParametrosDelete extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		AuditoriaUrlParametersLogConfig parametroDAO = new AuditoriaUrlParametersLogConfig(_dso);
		UrlParametersLogConfig urlParametersLogConfig = new UrlParametersLogConfig();
		String sql ="";
		
		urlParametersLogConfig.setId_config(Integer.parseInt(_req.getParameter("id_config")));
		urlParametersLogConfig.setParametro(_req.getParameter("name"));
		
		sql=parametroDAO.eliminar(urlParametersLogConfig);
		db.exec(_dso, sql);
	}
}

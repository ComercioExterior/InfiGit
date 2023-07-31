package models.configuracion.auditoria_actions;

import megasoft.db;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.AuditoriaUrlLogConfigDAO;
import com.bdv.infi.data.UrlLogConfig;

public class Delete extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		AuditoriaUrlLogConfigDAO auditaDAO = new AuditoriaUrlLogConfigDAO(_dso);
		UrlLogConfig urlLogConfig = new UrlLogConfig();
		String sql ="";
		
		urlLogConfig.setId_config(Integer.parseInt(_req.getParameter("id_config")));
		
		sql=auditaDAO.eliminar(urlLogConfig);
		db.exec(_dso, sql);
	}
}

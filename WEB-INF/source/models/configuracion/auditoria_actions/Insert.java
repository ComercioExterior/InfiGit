package models.configuracion.auditoria_actions;

import megasoft.db;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.AuditoriaUrlLogConfigDAO;
import com.bdv.infi.dao.TipoBloqueoDAO;
import com.bdv.infi.data.TipoBloqueo;
import com.bdv.infi.data.UrlLogConfig;

public class Insert extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		String sql ="";
		
		AuditoriaUrlLogConfigDAO auditaDAO = new AuditoriaUrlLogConfigDAO(_dso);
		UrlLogConfig urlLogConfig = new UrlLogConfig();
		
		urlLogConfig.setUrl(_req.getParameter("url"));
		urlLogConfig.setEnable(Integer.parseInt(_req.getParameter("enable")));
				
		//ensamblar SQL
		sql=auditaDAO.insertar(urlLogConfig);
		//ejecutar query
		db.exec(_dso,sql);
	}
}

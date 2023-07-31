package models.configuracion.auditoria_actions;

import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.AuditoriaUrlLogConfigDAO;

public class Addnew extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		AuditoriaUrlLogConfigDAO auditaDAO = new AuditoriaUrlLogConfigDAO(_dso);
		
		storeDataSet("indicador",auditaDAO.indicador());
	}
}

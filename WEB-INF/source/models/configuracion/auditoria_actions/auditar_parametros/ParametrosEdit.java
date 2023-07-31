package models.configuracion.auditoria_actions.auditar_parametros;

import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.AuditoriaUrlParametersLogConfig;

public class ParametrosEdit extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		AuditoriaUrlParametersLogConfig parametroDAO = new AuditoriaUrlParametersLogConfig(_dso);
		
		String parametro=null;
	
		if(_req.getParameter("name")!=null){
			parametro = _req.getParameter("name");
		}
		
		//Realizar consulta
		parametroDAO.listarPorNombre(parametro);
		//registrar los datasets exportados por este modelo
		storeDataSet("table", parametroDAO.getDataSet());
	}
}

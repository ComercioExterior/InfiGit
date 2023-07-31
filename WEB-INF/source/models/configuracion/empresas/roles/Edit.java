package models.configuracion.empresas.roles;

import models.msc_utilitys.*;
import com.bdv.infi.dao.EmpresaRolesDAO;

public class Edit extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		EmpresaRolesDAO confiD = new EmpresaRolesDAO(_dso);
		
		String roles_id=null;
	
		if(_req.getParameter("roles_id")!=null){
			roles_id = _req.getParameter("roles_id");
		}
		
		//Realizar consulta
		confiD.listar(roles_id);		
		//registrar los datasets exportados por este modelo
		storeDataSet("table", confiD.getDataSet());
		storeDataSet("indicador",confiD.indicador());
		storeDataSet("status",confiD.status());	
	}
}
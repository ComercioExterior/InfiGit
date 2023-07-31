package models.configuracion.empresas.roles;

import models.msc_utilitys.*;
import com.bdv.infi.dao.EmpresaRolesDAO;

public class Browse extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		EmpresaRolesDAO confiD = new EmpresaRolesDAO(_dso);
	
		String roles_descripcion=null;
		String roles_status=null;
		_req.getSession().removeAttribute("empresas_roles-browse.framework.page.record");
		if(_record.getValue("roles_descripcion")!=null || _record.getValue("roles_status")!=null){
			if(_record.getValue("roles_descripcion")!=null){
				roles_descripcion = _record.getValue("roles_descripcion");
			}
			if(_record.getValue("roles_status")!=null){
				roles_status = _record.getValue("roles_status");
			}
			//Realizar consulta
		}
		confiD.listar(roles_descripcion, roles_status);
		//registrar los datasets exportados por este modelo
		storeDataSet("table", confiD.getDataSet());
		storeDataSet("total", confiD.getTotalRegistros());
	}

}
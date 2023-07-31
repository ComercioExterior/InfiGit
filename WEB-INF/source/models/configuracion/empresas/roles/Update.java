package models.configuracion.empresas.roles;

import megasoft.db;
import models.msc_utilitys.*;
import com.bdv.infi.dao.EmpresaRolesDAO;
import com.bdv.infi.data.EmpresaRoles;

public class Update extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		EmpresaRolesDAO confiD = new EmpresaRolesDAO(_dso);
		EmpresaRoles empresaRoles = new EmpresaRoles();

		String sql ="";
		
		empresaRoles.setRoles_id(_req.getParameter("roles_id"));
		empresaRoles.setRoles_descripcion(_req.getParameter("roles_descripcion"));
		empresaRoles.setRoles_in_tomador(Integer.parseInt(_req.getParameter("roles_in_tomador")));
		empresaRoles.setRoles_in_colocador(Integer.parseInt(_req.getParameter("roles_in_colocador")));
		empresaRoles.setRoles_in_recomprador(Integer.parseInt(_req.getParameter("roles_in_recomprador")));
		empresaRoles.setRoles_status(Integer.parseInt(_req.getParameter("roles_status")));
		
		sql=confiD.modificar(empresaRoles);
		db.exec(_dso, sql);
	}
}
package models.configuracion.empresas.roles;

import com.bdv.infi.dao.EmpresaRolesDAO;
import com.bdv.infi.data.EmpresaRoles;
import megasoft.*;
import models.msc_utilitys.*;

public class Insert extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		String sql ="";
		
		EmpresaRolesDAO confiD = new EmpresaRolesDAO(_dso);
		EmpresaRoles empresaRoles = new EmpresaRoles();
		
		empresaRoles.setRoles_descripcion(_req.getParameter("roles_descripcion"));
		empresaRoles.setRoles_in_tomador(Integer.parseInt(_req.getParameter("roles_in_tomador")));
		empresaRoles.setRoles_in_colocador(Integer.parseInt(_req.getParameter("roles_in_colocador")));
		empresaRoles.setRoles_in_recomprador(Integer.parseInt(_req.getParameter("roles_in_recomprador")));
		empresaRoles.setRoles_status(Integer.parseInt(_req.getParameter("roles_status")));
				
		//ensamblar SQL
		sql=confiD.insertar(empresaRoles);
		
		//ejecutar query
		db.exec( _dso, sql);

	}
	
	
	

}

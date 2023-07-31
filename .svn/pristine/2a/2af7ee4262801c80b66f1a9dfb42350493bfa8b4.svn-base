package models.configuracion.empresas.roles;

import megasoft.db;
import models.msc_utilitys.*;

import com.bdv.infi.dao.EmpresaDefinicionDAO;
import com.bdv.infi.dao.EmpresaRolesDAO;
import com.bdv.infi.data.EmpresaDefinicion;
import com.bdv.infi.data.EmpresaRoles;

public class Delete extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		EmpresaRolesDAO confiD = new EmpresaRolesDAO(_dso);
		EmpresaRoles empresaRoles = new EmpresaRoles();
		
		String sql ="";
		
		empresaRoles.setRoles_id(_req.getParameter("roles_id"));
		
		sql=confiD.eliminar(empresaRoles);
		db.exec(_dso, sql);
	}
	
	public boolean isValid() throws Exception {

		boolean flag = super.isValid();
		
		EmpresaRolesDAO confiD = new EmpresaRolesDAO(_dso);
		EmpresaRoles empresaRoles = new EmpresaRoles();
		empresaRoles.setRoles_id(_req.getParameter("roles_id"));

		confiD.verificar(empresaRoles);
		if (confiD.getDataSet().count()>0){
			_record.addError("Rol Empresa","No se puede eliminar el Registro. Esta siendo utilizado como referencia en otras transacciones. Error de Integridad Referencial.");
			flag = false;
		}
		return flag;		
	}
	
}
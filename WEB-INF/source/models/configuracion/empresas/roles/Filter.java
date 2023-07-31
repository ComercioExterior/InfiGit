package models.configuracion.empresas.roles;

import models.msc_utilitys.*;
import com.bdv.infi.dao.EmpresaRolesDAO;

public class Filter extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		EmpresaRolesDAO confiD = new EmpresaRolesDAO(_dso);
		confiD.listar();
		
		storeDataSet("roles",confiD.getDataSet());
		storeDataSet("status",confiD.status());
	}
}
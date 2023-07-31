package models.configuracion.empresas.roles;

import com.bdv.infi.dao.EmpresaRolesDAO;
import models.msc_utilitys.*;

public class Addnew extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		EmpresaRolesDAO confiD = new EmpresaRolesDAO(_dso);
		
		storeDataSet("indicador",confiD.indicador());
		storeDataSet("status",confiD.status());	
	}
}
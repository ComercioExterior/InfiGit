package models.configuracion.empresas.definicion;

import models.msc_utilitys.*;
import com.bdv.infi.dao.EmpresaDefinicionDAO;

public class Filter extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		EmpresaDefinicionDAO confiD = new EmpresaDefinicionDAO(_dso);
		
		storeDataSet("status",confiD.status());	
	}
}
package models.configuracion.empresas.vehiculos.roles;

import models.msc_utilitys.*;
import com.bdv.infi.dao.EmpresaDefinicionDAO;
import com.bdv.infi.dao.VehiculoDAO;

public class Addnew extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		VehiculoDAO vehiD = new VehiculoDAO(_dso);
		vehiD.listarTodos();
		storeDataSet("vehiculo",vehiD.getDataSet());
		storeDataSet("indicador",vehiD.indicador());
	}
}
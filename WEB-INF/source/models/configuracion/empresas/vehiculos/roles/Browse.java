package models.configuracion.empresas.vehiculos.roles;

import models.msc_utilitys.*;
import com.bdv.infi.dao.RolesVehiculoDAO;

public class Browse extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		RolesVehiculoDAO confiD = new RolesVehiculoDAO(_dso);
			
		//Realizar consulta
		confiD.listar();
		//registrar los datasets exportados por este modelo
		storeDataSet("table", confiD.getDataSet());
		storeDataSet("datos", confiD.getTotalRegistros());
	}

}
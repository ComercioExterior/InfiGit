package models.configuracion.empresas.vehiculos.roles;

import megasoft.db;
import models.msc_utilitys.*;
import com.bdv.infi.dao.RolesVehiculoDAO;
import com.bdv.infi.data.RolesVehiculo;

public class Delete extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		RolesVehiculoDAO confiD = new RolesVehiculoDAO(_dso);
		RolesVehiculo rolesVehiculo = new RolesVehiculo();
		
		String sql ="";
		
		rolesVehiculo.setIdRoles(Integer.parseInt(_req.getParameter("vehicu_rol_id")));
		
		sql=confiD.eliminar(rolesVehiculo);
		db.exec(_dso, sql);
	}
}
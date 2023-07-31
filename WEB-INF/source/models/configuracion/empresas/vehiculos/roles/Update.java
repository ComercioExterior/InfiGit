package models.configuracion.empresas.vehiculos.roles;

import megasoft.db;
import models.msc_utilitys.*;
import com.bdv.infi.dao.RolesVehiculoDAO;
import com.bdv.infi.data.RolesVehiculo;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

public class Update extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		RolesVehiculoDAO confiD = new RolesVehiculoDAO(_dso);
		RolesVehiculo rolesVehiculo = new RolesVehiculo();
		
		String sql ="";
		rolesVehiculo.setTomador(_req.getParameter("vehicu_rol_tomador"));
		rolesVehiculo.setColocador(_req.getParameter("vehicu_rol_colocador"));
		rolesVehiculo.setRecompra(_req.getParameter("vehicu_rol_recompra"));
		rolesVehiculo.setPorDefecto(Integer.parseInt(_req.getParameter("vehicu_rol_in_defecto")));
		rolesVehiculo.setIdRoles(Integer.parseInt(_req.getParameter("vehicu_rol_id")));
		
		if (Integer.parseInt(_req.getParameter("vehicu_rol_in_defecto"))==ConstantesGenerales.VERDADERO){
			 String sql1= confiD.modificarIndicadorPorDefecto();
			 db.exec(_dso,sql1);
		}
		
		sql=confiD.modificar(rolesVehiculo);
		db.exec(_dso, sql);
	}
}
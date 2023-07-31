package models.configuracion.empresas.vehiculos.roles;

import com.bdv.infi.dao.RolesVehiculoDAO;
import com.bdv.infi.data.RolesVehiculo;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

import megasoft.*;
import models.msc_utilitys.*;

public class Insert extends MSCModelExtend {

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
		
		if (Integer.parseInt(_req.getParameter("vehicu_rol_in_defecto"))==ConstantesGenerales.VERDADERO){
			 String sql1= confiD.modificarIndicadorPorDefecto();
			 db.exec(_dso,sql1);
		}
		//ensamblar SQL
		sql=confiD.insertar(rolesVehiculo);
		//ejecutar query
		db.exec(_dso,sql);
	}
}
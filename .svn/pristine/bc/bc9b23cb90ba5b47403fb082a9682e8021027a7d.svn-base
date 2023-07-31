package models.configuracion.empresas.vehiculos.roles;

import models.msc_utilitys.*;
import com.bdv.infi.dao.RolesVehiculoDAO;
import com.bdv.infi.dao.VehiculoDAO;

public class Edit extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		RolesVehiculoDAO confiD = new RolesVehiculoDAO(_dso);
		VehiculoDAO vehiD = new VehiculoDAO(_dso);
		
		String idRol=null;
	
		if(_req.getParameter("vehicu_rol_id")!=null){
			idRol = _req.getParameter("vehicu_rol_id");
		}
		
		//Realizar consulta
		confiD.listarRoles(Integer.parseInt(idRol));
		vehiD.listarTodos();
		//registrar los datasets exportados por este modelo
		storeDataSet("table", confiD.getDataSet());
		storeDataSet("vehiculo",vehiD.getDataSet());
		storeDataSet("indicador",confiD.indicador());	
	}
}
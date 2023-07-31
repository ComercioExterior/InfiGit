package models.configuracion.empresas.vehiculos.roles;

import com.bdv.infi.dao.RolesVehiculoDAO;
import com.bdv.infi.data.RolesVehiculo;

import megasoft.DataSet;
import models.msc_utilitys.*;

public class ConfirmInsert extends MSCModelExtend {
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
			
		//	crear dataset
		DataSet _filter = getDataSetFromRequest();
		//registrar los datasets exportados por este modelo
		storeDataSet("filter", _filter);
	}
	
	public boolean isValid() throws Exception {
		RolesVehiculoDAO confiD = new RolesVehiculoDAO(_dso);
		RolesVehiculo rolesVehiculo= new RolesVehiculo();
	
		boolean flag = super.isValid();		
		
		rolesVehiculo.setTomador(_record.getValue("vehicu_rol_tomador"));
		rolesVehiculo.setColocador(_record.getValue("vehicu_rol_colocador"));
		rolesVehiculo.setRecompra(_record.getValue("vehicu_rol_recompra"));
		
		confiD.verificarDuplicado(rolesVehiculo);
		if(confiD.getDataSet().count()>0){
			_record.addError("Registro Duplicado","Ya existe un registro con estos Roles");
			flag = false;		
		}
		return flag;
	}
}

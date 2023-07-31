package models.configuracion.empresas.vehiculos.roles;

import com.bdv.infi.dao.RolesVehiculoDAO;
import com.bdv.infi.data.RolesVehiculo;

import megasoft.DataSet;
import models.msc_utilitys.*;

public class ConfirmUpdate extends MSCModelExtend {
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
				
		//	crear dataset
		DataSet _filter = getDataSetFromRequest();
		//registrar los datasets exportados por este modelo
		storeDataSet("filter", _filter);
		storeDataSet("record", _record);
	}
	
	public boolean isValid() throws Exception {
		RolesVehiculoDAO confiD = new RolesVehiculoDAO(_dso);
		RolesVehiculo rolesVehiculo= new RolesVehiculo();
		
		boolean flag = super.isValid();
		
		//Valores que llegaron al formulario
		String antiguoRecompra = _req.getParameter("recompra");
		String antiguoColocador = _req.getParameter("colocador");
		String antiguoTomador = _req.getParameter("tomador");
		
		//valores que asiganamos (actualizamos)
		String nuevoRecompra = _record.getValue("vehicu_rol_recompra");
		String nuevoColocador = _record.getValue("vehicu_rol_colocador");
		String nuevoTomador = _record.getValue("vehicu_rol_tomador");
		if((!antiguoRecompra.equals(nuevoRecompra))||(!antiguoColocador.equals(nuevoColocador))||(!antiguoTomador.equals(nuevoTomador))){
			rolesVehiculo.setTomador(_record.getValue("vehicu_rol_tomador"));
			rolesVehiculo.setColocador(_record.getValue("vehicu_rol_colocador"));
			rolesVehiculo.setRecompra(_record.getValue("vehicu_rol_recompra"));
			
			confiD.verificarDuplicado(rolesVehiculo);
			if(confiD.getDataSet().count()>0){
				_record.addError("Registro Duplicado","Ya existe un registro con estos Roles");
				flag = false;		
			}
		}
		
		//para validar si ya existe un registro con el indicador en si (1)
		/*String status= _record.getValue("vehicu_rol_in_defecto");
		if(Integer.parseInt(status)==1){
			confiD.verificar();
			DataSet data=confiD.getDataSet();
			for(int i=0;i<data.count();i++){
				for(int j=1;j<data.colCount();j++){
					if (Integer.parseInt(data.getData()[i][j])>=1){
						_record.addError("Por Defecto","No puede asignar este Rol por defecto porque ya existe uno");
						flag = false;			
					}
				}
			}
		}*/
		
		return flag;
	}
}

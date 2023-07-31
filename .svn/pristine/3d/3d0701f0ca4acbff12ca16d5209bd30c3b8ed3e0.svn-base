package models.configuracion.empresas.vehiculos.definicion;

import megasoft.db;
import models.msc_utilitys.*;
import com.bdv.infi.dao.EmpresaVehiculoDefinicionDAO;
import com.bdv.infi.data.VehiculoDefinicion;

public class Delete extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		EmpresaVehiculoDefinicionDAO confiD = new EmpresaVehiculoDefinicionDAO(_dso);
		VehiculoDefinicion vehiculoDefinicion = new VehiculoDefinicion();
		
		String sql ="";
		
		vehiculoDefinicion.setVehicu_id(_req.getParameter("vehicu_id"));
		
		sql=confiD.eliminar(vehiculoDefinicion);
		db.exec(_dso, sql);
	}
	
	public boolean isValid() throws Exception {

		boolean flag = super.isValid();
		
		EmpresaVehiculoDefinicionDAO confiD = new EmpresaVehiculoDefinicionDAO(_dso);
		VehiculoDefinicion vehiculoDefinicion = new VehiculoDefinicion();
		vehiculoDefinicion.setVehicu_id(_req.getParameter("vehicu_id"));

		confiD.verificarVehiculoColocador(vehiculoDefinicion);
		if (confiD.getDataSet().count()>0){
			_record.addError("Vehiculo","No se puede eliminar el Registro. Esta siendo utilizado como referencia en otras transacciones. Error de Integridad Referencial.");
			flag = false;
		}else if (confiD.getDataSet().count()<=0){
			confiD.verificarVehiculoRecompra(vehiculoDefinicion);
			if (confiD.getDataSet().count()>0){
				_record.addError("Vehiculo","No se puede eliminar el Registro. Esta siendo utilizado como referencia en otras transacciones. Error de Integridad Referencial.");
				flag = false;
			}else if (confiD.getDataSet().count()<=0){
				confiD.verificarVehiculoTomador(vehiculoDefinicion);
				if (confiD.getDataSet().count()>0){
					_record.addError("Vehiculo","No se puede eliminar el Registro. Esta siendo utilizado como referencia en otras transacciones. Error de Integridad Referencial.");
					flag = false;
				}
			}			
		}
		return flag;		
	}
	
}
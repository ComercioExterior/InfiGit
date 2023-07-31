package models.configuracion.empresas.vehiculos.definicion;

import com.bdv.infi.dao.VehiculoDAO;
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
	}
	
	public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();		
	
		if (flag)
		{		
			//verificar si el vehiculo buscado es BDV
			VehiculoDAO vehDAO = new VehiculoDAO(_dso);
	
			//si no es BDV, validar que se haya indicado la cuenta del vehiculo
			if(!vehDAO.vehiculoEsBDV(_req.getParameter("vehicu_id"))){
				
				if(_record.getValue("vehicu_numero_cuenta")==null){
					_record.addError("Nro. de Cuenta Vehiculo", "Debe indicar el n&uacute;mero de cuenta del veh&iacute;culo " + _record.getValue("vehicu_nombre"));
					flag = false;
				}
			}
		
		}

		return flag;
	}
}

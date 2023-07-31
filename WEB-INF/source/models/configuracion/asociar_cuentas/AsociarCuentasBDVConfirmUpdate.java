package models.configuracion.asociar_cuentas;

import models.msc_utilitys.MSCModelExtend;
/**
 * Confirma si se desea actualizar la asociación de una cuenta a una transacción
 * @author elaucho
 */
public class AsociarCuentasBDVConfirmUpdate extends MSCModelExtend{

	
	public void execute() throws Exception {
		
		//Se publican los datos que vienen del request
		storeDataSet("request",getDataSetFromRequest());
	}
}

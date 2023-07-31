package models.configuracion.cuentas_bdv;

import models.msc_utilitys.MSCModelExtend;
/**
 * Confirma la actualizacion de un resgistro de las cuentas asociadas al BDV
 * @author elaucho
 */
public class CuentasBDVConfirmUpdate extends MSCModelExtend{

	@Override
	public void execute() throws Exception {
		
		//Se publican los datos que vienen por el request
		storeDataSet("request",getDataSetFromRequest());
	}//fin execute
}//fin clase

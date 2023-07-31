package models.oficina;

import models.msc_utilitys.MSCModelExtend;
/**
 * Clase que confirma si se desea eliminar un registro de oficina
 * @author elaucho
 */
public class OficinaConfirmDelete extends MSCModelExtend{
	
	@Override
	public void execute() throws Exception {
		
		//Se publica el dataset del request
		storeDataSet("request",getDataSetFromRequest());
		
	}//fin execute

}

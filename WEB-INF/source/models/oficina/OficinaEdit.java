package models.oficina;

import models.msc_utilitys.MSCModelExtend;
/**
 * Clase que muestra el registro a editar
 * @author elaucho
 */
public class OficinaEdit extends MSCModelExtend{

	@Override
	public void execute() throws Exception {
		
		//Publicamos el dataset del request
		storeDataSet("request",getDataSetFromRequest());
		
	}//fin execute
}//fin clase

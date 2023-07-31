package models.bcv.configuracion;

import models.msc_utilitys.MSCModelExtend;

public class Confirm  extends MSCModelExtend {
	@Override
	public void execute() throws Exception {
		
		//Se publica el dataset del request
		storeDataSet("request",getDataSetFromRequest());
		
	}//fin execute
}

package models.configuracion.cuentasCliente;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

public class Confirm extends MSCModelExtend {
	public void execute()throws Exception{
		DataSet _table=getDataSetFromRequest();
		storeDataSet("table",_table);
	}
}
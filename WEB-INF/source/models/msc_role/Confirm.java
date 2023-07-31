package models.msc_role;

import megasoft.*;import models.msc_utilitys.*;

public class Confirm extends MSCModelExtend
{

	/** DataSet del modelo */
	private DataSet _filter = null;

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{
		
		//crear dataset
		_filter = getDataSetFromRequest();

		//registrar los datasets exportados por este modelo
		storeDataSet("filter", _filter);
		
	}

}

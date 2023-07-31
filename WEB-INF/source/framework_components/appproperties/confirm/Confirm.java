package framework_components.appproperties.confirm;

import megasoft.*;

public class Confirm extends AbstractModel
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

package models.carga_inicial;

import megasoft.AbstractModel;

public class UploadArchivo extends AbstractModel
{
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{
		storeDataSet( "record", _record );		

	}
}

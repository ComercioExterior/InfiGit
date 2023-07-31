package models.custodia.transacciones.pago_cupones;

import megasoft.DataSet;
import models.msc_utilitys.*;
public class Confirm extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		
		//crear dataset
		DataSet _filter = getDataSetFromRequest();
		//registrar los datasets exportados por este modelo
		storeDataSet("filter", _filter);
		
		
		DataSet _titulos = new DataSet();
		_titulos.append("titulos",java.sql.Types.VARCHAR);
		
		//
		for(int i =0;i<_req.getParameterValues("titulos").length;i++)
		{
			_titulos.addNew();
			_titulos.setValue("titulos", _req.getParameterValues("titulos")[i]);
		}
		//
		storeDataSet("titulos", _titulos);
		
	}
	
	/**
	 * Validaciones Basicas del action
	 * @return true si la clase y sus par&aacute;metros son v&aacute;lidos, false en caso contrario 
	 * @throws Exception
	 */	
	public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();
		
		if (flag){
			//Se verifica que se haya seleccionado algún título
			if (_req.getParameterValues("titulos")== null){				
				_record.addError("T&iacute;tulos", "Debe seleccionar alg&uacute;n t&iacute;tulo para la generaci&oacute;n de cupones.");
				flag = false;					
			}		
		}
		return flag;	
	}	
}
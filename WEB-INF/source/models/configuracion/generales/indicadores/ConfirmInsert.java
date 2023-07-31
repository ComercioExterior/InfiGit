package models.configuracion.generales.indicadores;

import com.bdv.infi.dao.EmpresaDefinicionDAO;
import com.bdv.infi.dao.IndicadoresDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

import megasoft.DataSet;
import models.msc_utilitys.*;

public class ConfirmInsert extends MSCModelExtend {
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
			
		//	crear dataset
		DataSet _filter = getDataSetFromRequest();
		//registrar los datasets exportados por este modelo
		storeDataSet("filter", _filter);
		storeDataSet("record", _record);
	}
	
	public boolean isValid() throws Exception
	{
		IndicadoresDAO indicadoresDao=new IndicadoresDAO(_dso);
		boolean indicadores=indicadoresDao.verificarIndicadorDescripcionExiste(_record.getValue("indica_descripcion"));
		boolean flag = super.isValid();		
			if(indicadores) {	
				_record.addError("Nombre","El dato que intento ingresar ya existe");
				flag=false;
			}		
		return flag;
	}
}

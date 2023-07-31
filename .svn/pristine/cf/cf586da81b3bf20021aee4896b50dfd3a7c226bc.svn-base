package models.configuracion.generales.tipo_garantias;

import com.bdv.infi.dao.TipoBloqueoDAO;
import com.bdv.infi.dao.TipoGarantiaDAO;
import com.bdv.infi.data.TipoGarantia;

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
		TipoGarantiaDAO confiD = new TipoGarantiaDAO(_dso);
		boolean existeNombre=confiD.verificarDescripcionTipoGarantiaExiste(_record.getValue("tipgar_descripcion"));
		boolean flag = super.isValid();		
			if(existeNombre) {	
				_record.addError("Descripci&oacute;n","El dato que intento ingresar ya existe");
				flag=false;
			}		
		return flag;
	}
}

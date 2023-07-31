package models.configuracion.generales.instrumentos_financieros;

import com.bdv.infi.dao.InstrumentoFinancieroDAO;
import com.bdv.infi.dao.PaisesDAO;

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
	}
	public boolean isValid() throws Exception
	{
		InstrumentoFinancieroDAO confiD = new InstrumentoFinancieroDAO(_dso);
		boolean existeNombre=confiD.verificarNombreInstrumentoExiste(_record.getValue("insfin_descripcion"));
		boolean flag = super.isValid();		
			if(existeNombre) {	
				_record.addError("Descripci&oacute;n","El dato que intento ingresar ya existe");
				flag=false;
			}		
		return flag;
	}
}

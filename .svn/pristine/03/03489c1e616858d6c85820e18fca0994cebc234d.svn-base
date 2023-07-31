package models.configuracion.generales.tipo_bloqueo;
import com.bdv.infi.dao.TipoBloqueoDAO;
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
		TipoBloqueoDAO confiD = new TipoBloqueoDAO(_dso);
		boolean existeNombre=confiD.verificarDescripcionBloqueoExiste(_record.getValue("tipblo_descripcion"));
		boolean flag = super.isValid();		
			if(existeNombre) {	
				_record.addError("Descripci&oacute;n","El dato que intento ingresar ya existe");
				flag=false;
			}		
		return flag;
	}
}

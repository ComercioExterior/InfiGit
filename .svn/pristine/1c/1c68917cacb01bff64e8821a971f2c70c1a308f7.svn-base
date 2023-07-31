package models.configuracion.documentos.definicion;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
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
	
	public boolean isValid() throws Exception {

		boolean flag = super.isValid();
		
		String transa=_record.getValue("transa_id");
		String nombreDocumeno= _record.getValue("ruta_documento.filename");
		int unidad=Integer.parseInt(_record.getValue("undinv_id"));
		
		if (transa.equalsIgnoreCase(TransaccionNegocio.TOMA_DE_ORDEN) || transa.equalsIgnoreCase(TransaccionNegocio.ADJUDICACION) || transa.equalsIgnoreCase(TransaccionNegocio.CANCELACION_ORDEN)){
			if(unidad==0){
				_record.addError("Unidad de Inversión","Este campo es obligatorio.");
				flag = false;
			}
		}
		if(!nombreDocumeno.endsWith(ConstantesGenerales.EXTENSION_DOC_HTM) && !nombreDocumeno.endsWith(ConstantesGenerales.EXTENSION_DOC_HTML)){
			_record.addError("Documento","La extension del archivo que intenta ingresar es incorrecta. Verifique que sea "+ConstantesGenerales.EXTENSION_DOC_HTM+" &oacute; "+ConstantesGenerales.EXTENSION_DOC_HTML+" e intente de nuevo");
			flag = false;
		}
		return flag;		
	}
	
}

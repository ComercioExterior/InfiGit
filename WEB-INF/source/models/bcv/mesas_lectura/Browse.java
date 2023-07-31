package models.bcv.mesas_lectura;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import models.msc_utilitys.MSCModelExtend;

public class Browse extends MSCModelExtend{

	public void execute() throws Exception {
		
		_req.getSession().setAttribute("contenidoDocumento", _record.getValue("archivo.tempfile"));
		_req.getSession().setAttribute("nombreDocumento", _record.getValue("archivo.filename"));

	}
	
	

/**
 * metodo del framework, si el metodo devuelve false manda a una pantalla de error.
 * 
 */
	public boolean isValid() throws Exception {
		
		boolean flag = super.isValid();
		
		if(flag){
			
			if(!_record.getValue("archivo.filename").endsWith(ConstantesGenerales.EXTENSION_DOC_XLS)){
				_record.addError("Documentos","La extension del archivo que ingreso es incorrecta. Verifique que sea .xls e intente de nuevo");
				flag = false;
			}
		}	

		return flag;
	}
}

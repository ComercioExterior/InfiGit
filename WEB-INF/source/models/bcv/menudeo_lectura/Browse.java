package models.bcv.menudeo_lectura;

import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

public class Browse extends MSCModelExtend{

	public void execute() throws Exception {
		
		OrdenDAO ordenDAO = new OrdenDAO(_dso);
		DataSet _data= new DataSet();
		DataSet _visible= new DataSet();
		DataSet datosEntrada=new DataSet();
		
		_visible.append("visible", java.sql.Types.VARCHAR);
		
		_req.getSession().setAttribute("contenidoDocumento", _record.getValue("archivo.tempfile"));
		_req.getSession().setAttribute("nombreDocumento", _record.getValue("archivo.filename"));
		datosEntrada.addNew();
		
		storeDataSet("table", _data);
		storeDataSet("datos_intrada",datosEntrada);
		storeDataSet("total", ordenDAO.getTotalRegistros());
		storeDataSet("visible", _visible);
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

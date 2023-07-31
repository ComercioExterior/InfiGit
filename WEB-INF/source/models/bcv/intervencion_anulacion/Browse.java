package models.bcv.intervencion_anulacion;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.enterprisedt.util.debug.Logger;
import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

public class Browse extends MSCModelExtend{
	private Logger logger = Logger.getLogger(Browse.class);
	public void execute() throws Exception {

		DataSet _data= new DataSet();
		DataSet _visible= new DataSet();
		DataSet datosEntrada=new DataSet();
		try{
			_visible.append("visible", java.sql.Types.VARCHAR);
			
			_req.getSession().setAttribute("contenidoDocumento", _record.getValue("archivo.tempfile"));
			_req.getSession().setAttribute("nombreDocumento", _record.getValue("archivo.filename"));
			datosEntrada.addNew();
			
			storeDataSet("table", _data);
			storeDataSet("datos_intrada",datosEntrada);
			storeDataSet("visible", _visible);	
		}catch (Exception e) {
			// TODO: handle exception
			logger.error("Browse : execute() Anulacion Intervencion "+e);
			System.out.println("Browse : execute() Anulacion Intervencion "+e);
		}
		
	}
	
	public boolean isValid() throws Exception {
			
		boolean flag = super.isValid();
		
		if(flag)
		{
			if(!_record.getValue("archivo.filename").endsWith(ConstantesGenerales.EXTENSION_DOC_XLS)){
				_record.addError("Documentos","La extension del archivo que ingreso es incorrecta. Verifique que sea .xls e intente de nuevo");
				flag = false;
			}	
		}	

		return flag;
	}
}

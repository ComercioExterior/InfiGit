package models.intercambio.recepcion.lectura_archivo;

import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.InstrumentoFinancieroDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

public class Browse extends MSCModelExtend{

	public void execute() throws Exception {

		InstrumentoFinancieroDAO instDAO=new InstrumentoFinancieroDAO(_dso);
		OrdenDAO ordenDAO = new OrdenDAO(_dso);
		DataSet _data= new DataSet();
		DataSet _visible= new DataSet();
		DataSet _ui= new DataSet();
		String instrumFID=null;
		_visible.append("visible", java.sql.Types.VARCHAR);
		
		_req.getSession().setAttribute("contenidoDocumento", _record.getValue("archivo.tempfile"));
		_req.getSession().setAttribute("nombreDocumento", _record.getValue("archivo.filename"));
		_req.getSession().setAttribute("unidadInversion", _record.getValue("undinv_id"));
		
		instrumFID=instDAO.getInstrumentoFinancieroPorUI(Long.parseLong(_record.getValue("undinv_id")));
		
		if ((_record.getValue("undinv_id").equals("0")) ){	
			ordenDAO.insfin();
			ordenDAO.getDataSet().next();
			_ui=ordenDAO.getDataSet();
			_visible.addNew();
			_visible.setValue("visible"," style='visibility: hidden'");
			_record.setValue("undinv_id","");
			storeDataSet("ui",_ui);
		}else
		if (instrumFID.equalsIgnoreCase(ConstantesGenerales.INST_FINANCIERO_SITME_CLAVENET_P)){
			instDAO.getDataSet().next();
			_ui=instDAO.getDataSet();
			_visible.addNew();
			_visible.setValue("visible"," style='visibility: hidden'");
			_record.setValue("undinv_id","");
			storeDataSet("ui",_ui);
		}
		else{
			ordenDAO.listarOrdenesPorAdjudicar(_record.getValue("undinv_id"));
			_visible.addNew();
			_visible.setValue("visible"," border='0' cellspacing='1' cellpadding='2' width='100%' class='datatable' TABLE-LAYOUT: auto;");
			_data=ordenDAO.getDataSet();
			instDAO.getDataSet().next();
			_ui=instDAO.getDataSet();
			storeDataSet("ui",_ui);
			
		}
		
		storeDataSet("table", _data);
		//storeDataSet("ui",_ui);
		storeDataSet("total", ordenDAO.getTotalRegistros());
		storeDataSet("visible", _visible);
	}
	
	public boolean isValid() throws Exception {

		boolean flag = super.isValid();
				
		if(flag)
		{
			if(!_record.getValue("archivo.filename").endsWith(ConstantesGenerales.EXTENSION_DOC_XLS)){
				_record.addError("Documentos","La extension del archivo que ingreso es incorrecta. Verifique que sea .xls e intente de nuevo");
				flag = false;
			}//fin if
			
		}//fin if

		return flag;
	}
}

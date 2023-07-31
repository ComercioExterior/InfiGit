package models.intercambio.recepcion.clavenet_personal.adjudicacion_subasta_divisas;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.dao.InstrumentoFinancieroDAO;
import com.bdv.infi.dao.OrdenDAO;

public class Browse extends MSCModelExtend{

	DataSet _data= new DataSet();
	OrdenDAO ordenDAO;
	
	public void execute() throws Exception {

		InstrumentoFinancieroDAO instDAO=new InstrumentoFinancieroDAO(_dso);
		
		
		DataSet _visible= new DataSet();
		DataSet _ui= new DataSet();
		DataSet datosEntrada=new DataSet();
		String instrumFID=null;
		_visible.append("visible", java.sql.Types.VARCHAR);
		
		_req.getSession().setAttribute("contenidoDocumento", _record.getValue("archivo.tempfile"));
		_req.getSession().setAttribute("nombreDocumento", _record.getValue("archivo.filename"));
		_req.getSession().setAttribute("unidadInversion", _record.getValue("undinv_id"));
		
		instrumFID=instDAO.getInstrumentoFinancieroPorUI(Long.parseLong(_record.getValue("undinv_id")));
		datosEntrada.addNew();
		
		datosEntrada.append("tipo_operacion",java.sql.Types.VARCHAR);
		datosEntrada.append("unidad_inversion",java.sql.Types.VARCHAR);
				
		datosEntrada.setValue("unidad_inversion",_record.getValue("undinv_id"));
		
		
		if ((_record.getValue("undinv_id").equals("0")) ){	
			ordenDAO.insfin();
			ordenDAO.getDataSet().next();
			_ui=ordenDAO.getDataSet();
			_visible.addNew();
			_visible.setValue("visible"," style='visibility: hidden'");
			_record.setValue("undinv_id","");
			storeDataSet("ui",_ui);
		} else {
			
			_visible.addNew();
			_visible.setValue("visible"," border='0' cellspacing='1' cellpadding='2' width='100%' class='datatable' TABLE-LAYOUT: auto;");
			
			instDAO.getDataSet().next();
			_ui=instDAO.getDataSet();
			storeDataSet("ui",_ui);					
		}
		
		storeDataSet("table", _data);
		storeDataSet("datos_intrada",datosEntrada);
		storeDataSet("total", ordenDAO.getTotalRegistros());
		storeDataSet("visible", _visible);
	}
	
	public boolean isValid() throws Exception {
		ordenDAO = new OrdenDAO(_dso);
		boolean flag = super.isValid();
		
		if(flag)
		{			
			if(!_record.getValue("archivo.filename").endsWith(ConstantesGenerales.EXTENSION_DOC_XLS)){
				_record.addError("Documentos","La extension del archivo que ingreso es incorrecta. Verifique que sea .xls e intente de nuevo");
				flag = false;
			}//fin if
			
			/*if(_record.getValue("tipo_operacion")==null || _record.getValue("tipo_operacion").equals("")){
				_record.addError("Tipo Operaci&oacute;n","Debe seleccionar un tipo de operaci&oacute;n a realizar");
				flag = false;
			}*/
			
			if(_record.getValue("undinv_id")==null || _record.getValue("undinv_id").equals("")){
				_record.addError("Unidad Inversi&oacute;n","Debe ingresar una Unidad de Inversi&oacute;n");
				return false;
			}
			
			ordenDAO.listarOrdenesPorAdjudicarByUnidadInversionID(_record.getValue("undinv_id"));
			if(ordenDAO.getDataSet().count()==0){
				_record.addError("Para su informaci&oacute;n","No se han encontrado registros asociados a la unidad de inversion ingresada");
				return false;
			}
			_data=ordenDAO.getDataSet();
		}	

		return flag;
	}
}

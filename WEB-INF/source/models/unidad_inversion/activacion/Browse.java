package models.unidad_inversion.activacion;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.BlotterDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;


public class Browse extends MSCModelExtend{
	
	private String idUnidadInversion;
	private String undinvIdEstatus;
	private String undinvEstatus;
	
	
	private DataSet _unidadInv= new DataSet();
	
	
	UnidadInversionDAO unidadInversionDAO;
	BlotterDAO blotterDAO;

	
	public void execute() throws Exception {
							
		_unidadInv=unidadInversionDAO.getDataSet();
						
		unidadInversionDAO.getDataSet().first();
		while(unidadInversionDAO.getDataSet().next()){
			undinvIdEstatus=unidadInversionDAO.getDataSet().getValue("undinv_active");
			if(undinvIdEstatus.equals(String.valueOf(ConstantesGenerales.STATUS_ACTIVO))){
				undinvEstatus="ACTIVA";
			}else {
				undinvEstatus="DESACTIVADA";
			}		
		}
		
		DataSet _datos= new DataSet();
		_datos.append("estado_unidad",java.sql.Types.VARCHAR);
		_datos.append("ui_id",java.sql.Types.VARCHAR);		
		_datos.addNew();		
		_datos.setValue("estado_unidad", undinvEstatus);
		_datos.setValue("ui_id", idUnidadInversion);
		
		storeDataSet("datos", _datos);
		storeDataSet("unidad_inversion", _unidadInv);
	}
	
	public boolean isValid() throws Exception {
		String tipoNegocio=null;
		boolean flag=true;
		blotterDAO=new BlotterDAO(_dso) ;
		
		idUnidadInversion=_req.getParameter("undinv_id");
		if(idUnidadInversion!=null && !idUnidadInversion.equals("")){					
			unidadInversionDAO=new UnidadInversionDAO(_dso);
			unidadInversionDAO.listarPorId(Long.parseLong(idUnidadInversion));
			
			if(unidadInversionDAO.getDataSet().count()>0){				
				unidadInversionDAO.getDataSet().first();
				unidadInversionDAO.getDataSet().next();				
				tipoNegocio=unidadInversionDAO.getDataSet().getValue("tipo_negocio");
				
				if(tipoNegocio==null || Long.parseLong(tipoNegocio)==0){					
					_record.addError("Para su informaci&oacute;n", " La unidad de inversi&oacute;n seleccionada no posee el manejo de tipo de negocio correcto (Alto Valor o Bajo Valor) por favor verifique ");
					flag=false;
				}			
			}
		} else {
			_record.addError("Para su informacion","Debe seleccionar una Unidad de Inversion");
			flag= false;
		}
		
		blotterDAO.listarBlotterCanalPorUiId(idUnidadInversion);
		
		if(!(blotterDAO.getDataSet().count()>0)){
			_record.addError("Para su informacion","La unidad de inversi&oacute;n seleccionada no posee la configuraci&oacute;n conrrecta de blotter para el proces de Activaci&oacute;n de Unidad de Inversi&oacute;n");
			flag= false;
		} else {
			boolean canalClavenet=false;
			blotterDAO.getDataSet().first();
			while(blotterDAO.getDataSet().next()){
				if(blotterDAO.getDataSet().getValue("ID_CANAL").equalsIgnoreCase(ConstantesGenerales.ID_CANAL_CLAVENET_PERSONAL)){
					canalClavenet=true;
					break;
				}
			}
			
			if(!canalClavenet){
				_record.addError("Para su informacion","La unidad de inversi&oacute;n seleccionada no tiene asociado un blotter con canal Clavenet Personal, por favor verifique");
				flag= false;	
			}
		}
		
		return flag;
	}
}

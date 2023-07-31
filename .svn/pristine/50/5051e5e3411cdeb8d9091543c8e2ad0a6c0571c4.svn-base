package models.intercambio.transferencia.generar_archivo;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.StatusOrden;

public class Browse extends MSCModelExtend {

	private String fechaProductoCLaveNet=null;
	private UnidadInversionDAO unidad = null;//new UnidadInversionDAO(_dso);
	private DataSet _data=null;
	private String unidad_inversion = null;
	private String instrFinanciero=null;
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {		
		UnidadInversionDAO unidad = new UnidadInversionDAO(_dso);
		

		if (_record.getValue("undinv_id")!=null&&!_record.getValue("undinv_id").equals(null)){
			System.out.println(" UNIDAD DE INVERSION " + _record.getValue("undinv_id"));
			unidad_inversion= _record.getValue("undinv_id");
			_req.getSession().setAttribute("unidadInversion", unidad_inversion);
		}  
				
		fechaProductoCLaveNet= _record.getValue("fechaAdjudicacionUI");
		
		instrFinanciero=unidad.getInstrumentoFinancieroPorUI(Long.parseLong(unidad_inversion));


		_req.getSession().setAttribute("fechaProductoCLaveNet", fechaProductoCLaveNet);
		if((instrFinanciero.equals(ConstantesGenerales.INST_FINANCIERO_SITME_CLAVENET_E)) || (instrFinanciero.equals(ConstantesGenerales.INST_FINANCIERO_SITME_CLAVENET_P))){			
						
			unidad.listarOrdenesClaveNet(Long.parseLong(unidad_inversion),StatusOrden.REGISTRADA,fechaProductoCLaveNet, null);
			DataSet _data=unidad.getDataSet();
			
			if(_data.count()>0){
				_data.first();
				while(_data.next()){				

					//Montar en session la unidad de inversion y el vehiculo, para usar en detalle
					_req.getSession().setAttribute("unidad_vehiculo", unidad.getDataSet());
					
				}
			}
			
			storeDataSet("table", _data);
			storeDataSet("datos", unidad.getTotalRegistros());
			// registrar los datasets exportados por este modelo
		}else{
		// Realizar consulta
		unidad.listarVehiculo(unidad_inversion);
		DataSet _data=unidad.getDataSet();
		if(_data.count()>0){
			_data.first();
			while(_data.next()){
				int total_ordenes=Integer.parseInt(_data.getValue("ordenes"));
				int enviadas = Integer.parseInt(_data.getValue("enviadas"));
				int por_eviar= total_ordenes - enviadas;
				_data.setValue("para_enviar",String.valueOf(por_eviar));
			}
		}
		// registrar los datasets exportados por este modelo
		storeDataSet("table", _data);
		storeDataSet("datos", unidad.getTotalRegistros());
		
		
		//Montar en session la unidad de inversion y el vehiculo, para usar en detalle
		_req.getSession().setAttribute("unidad_vehiculo", unidad.getDataSet());
	
		}		
	}
	
	public boolean isValid() throws Exception {
	 
		if (_record.getValue("undinv_id")!=null&&!_record.getValue("undinv_id").equals(null)){			
	   	    unidad_inversion= _record.getValue("undinv_id");			
		}		
		
		fechaProductoCLaveNet= _record.getValue("fechaAdjudicacionUI");
		unidad = new UnidadInversionDAO(_dso);		
		instrFinanciero = unidad.getInstrumentoFinancieroPorUI(Long.parseLong(unidad_inversion));
		
		boolean flag = super.isValid();
		
		if((instrFinanciero.equals(ConstantesGenerales.INST_FINANCIERO_SITME_CLAVENET_E)) || (instrFinanciero.equals(ConstantesGenerales.INST_FINANCIERO_SITME_CLAVENET_P))){		
			if(fechaProductoCLaveNet==null || fechaProductoCLaveNet.equals("")){
				_record.addError("fechaAdjudicacionUI","Estimado usuario, El campo fecha no puede estar vacio debido a que ha seleccionado una Unidad de Inversion asociado a un instrumento financiero CLAVENET_P o CLAVENET_E");
				flag=false;
			}					
			//llama realiza el llamado al metodo que se encarga de llenar la informacion desde la tabla de clavenet			
		} else {			
			if(fechaProductoCLaveNet!=null){			
				_record.addError("fechaAdjudicacionUI","Estimado usuario, el campo de fecha solo aplica para unidades de inversion con instrumentos financieros de tipo CLAVENET_E o CLAVENET_P");
				_record.setValue("fechaAdjudicacionUI",null);
				flag=false;			
			}						
		}		
		return flag;	
	}
}

/**/
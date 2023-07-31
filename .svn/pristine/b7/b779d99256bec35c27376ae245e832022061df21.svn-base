package models.custodia.estructura_tarifaria;

import megasoft.DataSet;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.CustodiaEstructuraTarifariaDAO;
import com.bdv.infi.data.CustodiaComision;
import com.bdv.infi.data.CustodiaComisionTransaccion;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

public class TransaccionesInsert extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		String sql ="";
		
		CustodiaEstructuraTarifariaDAO confiD = new CustodiaEstructuraTarifariaDAO(_dso);
		CustodiaComisionTransaccion custodiaComisionTransaccion = new CustodiaComisionTransaccion();
		
		custodiaComisionTransaccion.setIdComision(Integer.parseInt(_record.getValue("comision_id")));
		String monto= String.valueOf(ConstantesGenerales.VERDADERO);

		//Salida Interna, seleccionado Radio Button de Monto  
		if(_record.getValue("ind_salida_interna").equals(monto)){
			custodiaComisionTransaccion.setMontoTransaccionInterna(Double.parseDouble(_record.getValue("mto_trans_interna")));
			custodiaComisionTransaccion.setMonedaTransaccionInterna(_record.getValue("moneda_trans_interna"));
			custodiaComisionTransaccion.setPctTransaccionInterna(0);
		}else{//Salida Interna, seleccionado Radio Button de Porcentaje
			custodiaComisionTransaccion.setPctTransaccionInterna(Double.parseDouble(_record.getValue("pct_trans_interna")));
			custodiaComisionTransaccion.setMontoTransaccionInterna(0);
			custodiaComisionTransaccion.setMonedaTransaccionInterna("");
		}
		//Salida Externa, seleccionado Radio Button de Monto  
		if(_record.getValue("ind_salida_externa").equals(monto)){
			custodiaComisionTransaccion.setMontoTransaccionExterna(Double.parseDouble(_record.getValue("mto_trans_externa")));
			custodiaComisionTransaccion.setMonedaTransaccionExterna(_record.getValue("moneda_trans_externa"));
			custodiaComisionTransaccion.setPctTransaccionExterna(0);
		}else{//Salida Externa, seleccionado Radio Button de Porcentaje
			custodiaComisionTransaccion.setPctTransaccionExterna(Double.parseDouble(_record.getValue("pct_trans_externa")));
			custodiaComisionTransaccion.setMontoTransaccionExterna(0);
			custodiaComisionTransaccion.setMonedaTransaccionExterna("");
		}
		//Anual Nacional, seleccionado Radio Button de Monto  
		if(_record.getValue("ind_anual_nac").equals(monto)){
			custodiaComisionTransaccion.setMontoAnualNacional(Double.parseDouble(_record.getValue("mto_anual_nacional")));
			custodiaComisionTransaccion.setMonedaAnualNacional(_record.getValue("moneda_anual_nacional"));
			custodiaComisionTransaccion.setPctAnualNacional(0);
		}else{//Anual Nacional, seleccionado Radio Button de Porcentaje
			custodiaComisionTransaccion.setPctAnualNacional(Double.parseDouble(_record.getValue("pct_anual_nacional")));
			custodiaComisionTransaccion.setMontoAnualNacional(0);
			custodiaComisionTransaccion.setMonedaAnualNacional("");
		}
		//Anual Extranjera, seleccionado Radio Button de Monto  
		if(_record.getValue("ind_anual_ext").equals(monto)){
			custodiaComisionTransaccion.setMontoAnualExtranjera(Double.parseDouble(_record.getValue("mto_anual_extranjera")));
			custodiaComisionTransaccion.setMonedaAnualExtranjera(_record.getValue("moneda_anual_extranjera"));
			custodiaComisionTransaccion.setPctAnualNacional(0);
		}else{//Anual Extranjera, seleccionado Radio Button de Porcentaje
			custodiaComisionTransaccion.setPctAnualExtranjera(Double.parseDouble(_record.getValue("pct_anual_extranjera")));
			custodiaComisionTransaccion.setMontoAnualExtranjera(0);
			custodiaComisionTransaccion.setMonedaAnualExtranjera("");
		}
		
		confiD.listarTransacciones(_record.getValue("comision_id"));
		DataSet _data=confiD.getDataSet();
		if(_data.count()<=0){
			sql=confiD.insertarTransaccion(custodiaComisionTransaccion);
		}else{
			sql=confiD.modificarTransaccion(custodiaComisionTransaccion);
		}
					
		//ejecutar query
		db.exec( _dso, sql);
		
		_config.nextAction="estructura_tarifaria-transacciones?comision_id="+custodiaComisionTransaccion.getIdComision();
	}

	@Override
	public boolean isValid() throws Exception {
		boolean flag = true;
		if (super.isValid()){
			//Se valida la pantalla y se muestran los mensajes respectivos
			//Salida Interna, seleccionado Radio Button de Monto  
			if(_record.getValue("ind_salida_interna").equals("1")){
				if (_record.getValue("mto_trans_interna") ==null){
					_record.addError("Salida Interna", "Debe indicar el monto fijo");
					flag = false;
				}
			}else{//Salida Interna, seleccionado Radio Button de Porcentaje
				if (_record.getValue("pct_trans_interna")==null){
					_record.addError("Salida Interna", "Debe indicar el porcentaje");
					flag = false;					
				}
			}
			//Salida Externa, seleccionado Radio Button de Monto  
			if(_record.getValue("ind_salida_externa").equals("1")){
				if (_record.getValue("mto_trans_externa") ==null){
					_record.addError("Salida Externa", "Debe indicar el monto fijo");
					flag = false;					
				}
			}else{//Salida Interna, seleccionado Radio Button de Porcentaje
				if (_record.getValue("pct_trans_externa")==null){
					_record.addError("Salida Externa", "Debe indicar el porcentaje");
					flag = false;					
				}
			}//Anual Nacional, seleccionado Radio Button de Monto  
			if(_record.getValue("ind_anual_nac").equals("1")){
				if(_record.getValue("mto_anual_nacional")==null){
					_record.addError("Anual Nacional", "Debe indicar el monto fijo");
					flag = false;					
				}
			}else{//Anual Nacional, seleccionado Radio Button de Porcentaje
				if(_record.getValue("pct_anual_nacional")==null){
					_record.addError("Anual Nacional", "Debe indicar el porcentaje");
					flag = false;					
				}
			}
			//Anual Extranjera, seleccionado Radio Button de Monto  
			if(_record.getValue("ind_anual_ext").equals("1")){
				if (_record.getValue("mto_anual_extranjera")==null){
					_record.addError("Anual Extranjera", "Debe indicar el monto");
					flag = false;					
				}
			}else{//Anual Extranjera, seleccionado Radio Button de Porcentaje
				if (_record.getValue("pct_anual_extranjera")==null){
					_record.addError("Anual Extranjera", "Debe indicar el porcentaje");
					flag = false;					
				}
			}
		}
		return flag;
	}
	
	
}

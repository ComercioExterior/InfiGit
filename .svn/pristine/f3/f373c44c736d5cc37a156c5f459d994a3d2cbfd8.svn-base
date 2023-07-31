package models.custodia.estructura_tarifaria;

import megasoft.db;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.CustodiaEstructuraTarifariaDAO;
import com.bdv.infi.data.CustodiaComisionTitulo;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

public class TituloUpdate extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		String sql ="";
		String idComision=null;
		String idTitulo=null;
		
		if(_record.getValue("comision_id")!=null){
			idComision=_record.getValue("comision_id");
		}
		if(_record.getValue("titulo_id")!=null){
			idTitulo=_record.getValue("titulo_id");
		}
		
		String monto= String.valueOf(ConstantesGenerales.VERDADERO);
		
		CustodiaEstructuraTarifariaDAO confiD = new CustodiaEstructuraTarifariaDAO(_dso);
		CustodiaComisionTitulo custodiaComisionTitulo = new CustodiaComisionTitulo();
		
		if(_record.getValue("ind_comision").equals(monto)){
			custodiaComisionTitulo.setMontoComision(Double.parseDouble(_record.getValue("mto_comision")));
			custodiaComisionTitulo.setMonedaComision(_record.getValue("moneda_comision"));
			custodiaComisionTitulo.setPctComision(0);
		}else{//Anual Extranjera, seleccionado Radio Button de Porcentaje
			custodiaComisionTitulo.setPctComision(Double.parseDouble(_record.getValue("pct_comision")));
			custodiaComisionTitulo.setMontoComision(0);
			custodiaComisionTitulo.setMonedaComision("");
		}
		
		custodiaComisionTitulo.setIdComision(Integer.parseInt(idComision));
		custodiaComisionTitulo.setIdTitulo(idTitulo);
		
		//ensamblar SQL
		sql=confiD.modificarTitulo(custodiaComisionTitulo);
		
		//ejecutar query
		db.exec( _dso, sql);
		
		_config.nextAction="estructura_tarifaria-titulos?comision_id="+custodiaComisionTitulo.getIdComision();
	}
}

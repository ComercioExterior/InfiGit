package models.custodia.estructura_tarifaria;

import megasoft.db;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.CustodiaEstructuraTarifariaDAO;
import com.bdv.infi.data.CustodiaComisionDepositario;

public class DepositarioCentralUpdate extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		String sql ="";
		String idComision=null;
		String idEmpresa=null;
		
		if(_record.getValue("comision_id")!=null){
			idComision=_record.getValue("comision_id");
		}
		if(_record.getValue("empres_id")!=null){
			idEmpresa=_record.getValue("empres_id");
		}
		CustodiaEstructuraTarifariaDAO confiD = new CustodiaEstructuraTarifariaDAO(_dso);
		CustodiaComisionDepositario custodiaComisionDepositario = new CustodiaComisionDepositario();

		custodiaComisionDepositario.setIdComision(Integer.parseInt(idComision));
		custodiaComisionDepositario.setIdEmpresa(idEmpresa);
		custodiaComisionDepositario.setMontoComision(Double.parseDouble(_record.getValue("mto_comision")));
		custodiaComisionDepositario.setMonedaComision(_record.getValue("moneda_comision"));
		//ensamblar SQL
		sql=confiD.modificarDepositario(custodiaComisionDepositario);
		
		//ejecutar query
		db.exec( _dso, sql);
		
		_config.nextAction="estructura_tarifaria-depositarios_centrales?comision_id="+custodiaComisionDepositario.getIdComision();
	}
}

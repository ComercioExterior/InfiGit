package models.custodia.estructura_tarifaria;

import megasoft.db;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.CustodiaEstructuraTarifariaDAO;
import com.bdv.infi.data.CustodiaComisionDepositario;

public class DepositarioCentralDelete extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		String sql ="";
		String idComision=getSessionObject("comision_id").toString();
		String idEmpresa=_record.getValue("empres_id");
		
		CustodiaEstructuraTarifariaDAO confiD = new CustodiaEstructuraTarifariaDAO(_dso);
		CustodiaComisionDepositario custodiaComisionDepositario = new CustodiaComisionDepositario();
		
//		 campos obligatorios recuperados de la pagina
		custodiaComisionDepositario.setIdEmpresa(idEmpresa);
		custodiaComisionDepositario.setMonedaComision(null);
		custodiaComisionDepositario.setIdComision(Integer.parseInt(idComision));
		sql=confiD.eliminarDepositarioCentral(custodiaComisionDepositario);
		db.exec( _dso, sql);	
	
		_config.nextAction="estructura_tarifaria-depositarios_centrales?comision_id="+custodiaComisionDepositario.getIdComision();
	}
}

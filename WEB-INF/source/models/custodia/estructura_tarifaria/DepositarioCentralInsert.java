package models.custodia.estructura_tarifaria;

import megasoft.db;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.CustodiaEstructuraTarifariaDAO;
import com.bdv.infi.data.CustodiaComisionDepositario;

public class DepositarioCentralInsert extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		String sql ="";
		String idComision=getSessionObject("comision_id").toString();
		
		CustodiaEstructuraTarifariaDAO confiD = new CustodiaEstructuraTarifariaDAO(_dso);
		CustodiaComisionDepositario custodiaComisionDepositario = new CustodiaComisionDepositario();
		
//		 campos obligatorios recuperados de la pagina
		String [] idEmpresa = _req.getParameterValues("idEmpresa");
		int ca = idEmpresa.length -1;
		if (ca == 0)
			return;
		
		// Aplicar la persistencia
		custodiaComisionDepositario.setIdComision(Integer.parseInt(idComision));
		for (int i=0; i< ca; i++) {	
			if (idEmpresa[i].equals("0")) {
				continue;
			}
			custodiaComisionDepositario.setIdEmpresa(idEmpresa[i]);
			custodiaComisionDepositario.setMonedaComision(null);
			custodiaComisionDepositario.setMontoComision(0);
			sql=confiD.insertarDepositarioCentral(custodiaComisionDepositario);
			db.exec( _dso, sql);
		}		
		_config.nextAction="estructura_tarifaria-select_depositarios?comision_id="+custodiaComisionDepositario.getIdComision();
	}
}

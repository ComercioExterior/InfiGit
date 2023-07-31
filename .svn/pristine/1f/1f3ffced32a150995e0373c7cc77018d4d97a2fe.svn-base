package models.custodia.estructura_tarifaria;

import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.CustodiaEstructuraTarifariaDAO;
import com.bdv.infi.dao.MonedaDAO;
import com.bdv.infi.data.CustodiaComisionDepositario;

public class DepositarioCentralEdit extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		CustodiaEstructuraTarifariaDAO estructuraTarifaria = new CustodiaEstructuraTarifariaDAO(_dso);
		MonedaDAO monedaDAO = new MonedaDAO(_dso);
		
		CustodiaComisionDepositario custodiaComisionDepositario= new CustodiaComisionDepositario();
		String idComision=null;
		String idEmpresa=null;
		
		if(_record.getValue("comision_id")!=null){
			idComision=_record.getValue("comision_id");
		}	
		if(_record.getValue("empres_id")!=null){
			idEmpresa=_record.getValue("empres_id");
		}	
		custodiaComisionDepositario.setIdComision(Integer.parseInt(idComision));
		custodiaComisionDepositario.setIdEmpresa(idEmpresa);
		// Realizar consulta
		estructuraTarifaria.listarDepositario(custodiaComisionDepositario);
		storeDataSet("depositario", estructuraTarifaria.getDataSet());
		monedaDAO.listarMonedasActivas();
		
		storeDataSet("moneda", monedaDAO.getDataSet());
	}
}

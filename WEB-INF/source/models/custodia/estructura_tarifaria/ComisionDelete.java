package models.custodia.estructura_tarifaria;

import megasoft.db;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.CustodiaEstructuraTarifariaDAO;
import com.bdv.infi.data.CustodiaComision;

public class ComisionDelete extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		String idComision=_record.getValue("comision_id");
		String comision=null;
		String cliente=null;
		
		if (_record.getValue("comision_nombre")!=null){
			comision= _record.getValue("comision_nombre");
		}
		if (_record.getValue("client_id")!=null){
			cliente= _record.getValue("client_id");
		}
		
		CustodiaEstructuraTarifariaDAO confiD = new CustodiaEstructuraTarifariaDAO(_dso);
		CustodiaComision custodiaComision = new CustodiaComision();
		
		custodiaComision.setIdComision(Integer.parseInt(idComision));
		String[] sql =confiD.eliminarComision(custodiaComision);
		db.execBatch( _dso, sql);	
	
		_config.nextAction="estructura_tarifaria-browse?comision_nombre="+comision+"&client_id="+cliente;
	}
}

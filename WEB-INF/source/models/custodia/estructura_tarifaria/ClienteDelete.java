package models.custodia.estructura_tarifaria;

import megasoft.db;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.CustodiaEstructuraTarifariaDAO;
import com.bdv.infi.data.CustodiaComisionCliente;

public class ClienteDelete extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		String sql ="";
		String idComision=getSessionObject("comision_id").toString();
		String idCliente=_record.getValue("client_id");
		
		CustodiaEstructuraTarifariaDAO confiD = new CustodiaEstructuraTarifariaDAO(_dso);
		CustodiaComisionCliente custodiaComisionCliente = new CustodiaComisionCliente();
		
//		 campos obligatorios recuperados de la pagina
		custodiaComisionCliente.setIdCliente(Integer.parseInt(idCliente));
		custodiaComisionCliente.setIdComision(Integer.parseInt(idComision));
		sql=confiD.eliminarCliente(custodiaComisionCliente);
		db.exec( _dso, sql);	
	
		_config.nextAction="estructura_tarifaria-clientes?comision_id="+custodiaComisionCliente.getIdComision();
	}
}

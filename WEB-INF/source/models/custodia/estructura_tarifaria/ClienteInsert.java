package models.custodia.estructura_tarifaria;

import megasoft.db;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.CustodiaEstructuraTarifariaDAO;
import com.bdv.infi.data.CustodiaComisionCliente;

public class ClienteInsert extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		String sql ="";
		String idComision=getSessionObject("comision_id").toString();
		
		CustodiaEstructuraTarifariaDAO confiD = new CustodiaEstructuraTarifariaDAO(_dso);
		CustodiaComisionCliente custodiaComisionCliente = new CustodiaComisionCliente();
		
//		 campos obligatorios recuperados de la pagina
		String [] idCliente = _req.getParameterValues("idCliente");
		int ca = idCliente.length -1;
		if (ca == 0)
			return;
		
		// Aplicar la persistencia
		custodiaComisionCliente.setIdComision(Integer.parseInt(idComision));
		for (int i=0; i< ca; i++) {	
			if (idCliente[i].equals("0")) {
				continue;
			}
			custodiaComisionCliente.setIdCliente(Integer.parseInt(idCliente[i]));

			sql=confiD.insertarCliente(custodiaComisionCliente);
			db.exec( _dso, sql);
		}		
		_config.nextAction="estructura_tarifaria-select_clientes?comision_id="+custodiaComisionCliente.getIdComision();
	}
}

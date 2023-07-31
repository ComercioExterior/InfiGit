package models.custodia.estructura_tarifaria;

import megasoft.db;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.CustodiaEstructuraTarifariaDAO;
import com.bdv.infi.data.CustodiaComisionTitulo;

public class TituloInsert extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		String sql ="";
		String idComision=getSessionObject("comision_id").toString();
		
		CustodiaEstructuraTarifariaDAO confiD = new CustodiaEstructuraTarifariaDAO(_dso);
		CustodiaComisionTitulo custodiaComisionTitulo = new CustodiaComisionTitulo();
		
		//campos obligatorios recuperados de la pagina
		String [] idTitulo = _req.getParameterValues("idTitulo");
		int ca = idTitulo.length -1;
		if (ca == 0)
			return;
		
		// Aplicar la persistencia
		custodiaComisionTitulo.setIdComision(Integer.parseInt(idComision));
		for (int i=0; i< ca; i++) {	
			if (idTitulo[i].equals("0")) {
				continue;
			}
			custodiaComisionTitulo.setIdTitulo(idTitulo[i]);
			custodiaComisionTitulo.setMonedaComision(null);
			custodiaComisionTitulo.setMontoComision(0);
			custodiaComisionTitulo.setPctComision(0);
			sql=confiD.insertarTitulo(custodiaComisionTitulo);
			db.exec( _dso, sql);
		}		
		_config.nextAction="estructura_tarifaria-select_titulos?comision_id="+custodiaComisionTitulo.getIdComision();
	}
}

package models.custodia.estructura_tarifaria;

import megasoft.db;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.CustodiaEstructuraTarifariaDAO;
import com.bdv.infi.data.CustodiaComisionTitulo;

public class TituloDelete extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		String sql ="";
		String idComision=getSessionObject("comision_id").toString();
		String idTitulo=_record.getValue("titulo_id");
		
		CustodiaEstructuraTarifariaDAO confiD = new CustodiaEstructuraTarifariaDAO(_dso);
		CustodiaComisionTitulo custodiaComisionTitulo = new CustodiaComisionTitulo();
		
//		 campos obligatorios recuperados de la pagina
		custodiaComisionTitulo.setIdTitulo(idTitulo);
		custodiaComisionTitulo.setPctComision(0);
		custodiaComisionTitulo.setMontoComision(0);
		custodiaComisionTitulo.setMonedaComision(null);
		custodiaComisionTitulo.setIdComision(Integer.parseInt(idComision));
		sql=confiD.eliminarTitulo(custodiaComisionTitulo);
		db.exec( _dso, sql);	
	
		_config.nextAction="estructura_tarifaria-titulos?comision_id="+custodiaComisionTitulo.getIdComision();
	}
}

package models.custodia.estructura_tarifaria;

import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.CustodiaEstructuraTarifariaDAO;
import com.bdv.infi.dao.MonedaDAO;
import com.bdv.infi.data.CustodiaComisionTitulo;

public class TituloEdit extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		CustodiaEstructuraTarifariaDAO estructuraTarifaria = new CustodiaEstructuraTarifariaDAO(_dso);
		MonedaDAO monedaDAO = new MonedaDAO(_dso);
		
		CustodiaComisionTitulo custodiaComisionTitulo= new CustodiaComisionTitulo();
		String idComision=null;
		String idTitulo=null;
		
		if(_record.getValue("comision_id")!=null){
			idComision=_record.getValue("comision_id");
		}	
		if(_record.getValue("titulo_id")!=null){
			idTitulo=_record.getValue("titulo_id");
		}	
		custodiaComisionTitulo.setIdComision(Integer.parseInt(idComision));
		custodiaComisionTitulo.setIdTitulo(idTitulo);
		// Realizar consulta
		estructuraTarifaria.listarTitulo(custodiaComisionTitulo);
		storeDataSet("titulo", estructuraTarifaria.getDataSet());
		monedaDAO.listarMonedasActivas();
		
		storeDataSet("moneda", monedaDAO.getDataSet());
	}
}

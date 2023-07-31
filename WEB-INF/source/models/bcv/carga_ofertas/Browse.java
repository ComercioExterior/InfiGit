package models.bcv.carga_ofertas;

import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.OfertaDAO;

public class Browse extends MSCModelExtend {
		
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		OfertaDAO ofertaDAO = new OfertaDAO(_dso);
		String fecha   = (String) _record.getValue("fecha");
		String statusP   = (String) _record.getValue("statusP");
		String origen   = (String) _record.getValue("origen");
		
		ofertaDAO.listarOrdenesPorEnviarBCV(0,0,0,0,0,0,false,false,0,0,true,true,null,statusP,fecha, origen,"");		
		storeDataSet("datos", ofertaDAO.getDataSet());
		storeDataSet("datos2", _record);
	}
}
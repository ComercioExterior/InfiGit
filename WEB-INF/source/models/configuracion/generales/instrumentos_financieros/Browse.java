package models.configuracion.generales.instrumentos_financieros;

import models.msc_utilitys.*;


import com.bdv.infi.dao.InstrumentoFinancieroDAO;

public class Browse extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		InstrumentoFinancieroDAO insFinDAO = new InstrumentoFinancieroDAO(_dso);			
		
		String insfin_descripcion = null;

		if ((_record.getValue("insfin_descripcion")!=null)){
			insfin_descripcion= _record.getValue("insfin_descripcion");
		} 
		//Realizar consulta
		insFinDAO.listarporfiltro(insfin_descripcion);
		//registrar los datasets exportados por este modelo
		storeDataSet("table", insFinDAO.getDataSet());
		storeDataSet("total", insFinDAO.getTotalRegistros());
		
		
	}

}
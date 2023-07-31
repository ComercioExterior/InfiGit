package models.configuracion.generales.tipo_garantias;

import models.msc_utilitys.*;
import com.bdv.infi.dao.TipoGarantiaDAO;

public class Edit extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		TipoGarantiaDAO confiD = new TipoGarantiaDAO(_dso);
		
		String idGarantia=null;
	
		if(_req.getParameter("tipgar_id")!=null){
			idGarantia = _req.getParameter("tipgar_id");
		}
		
		//Realizar consulta
		confiD.listarPorId(idGarantia);
		//registrar los datasets exportados por este modelo
		storeDataSet("table", confiD.getDataSet());
		
		storeDataSet("status",confiD.status());
	}
}
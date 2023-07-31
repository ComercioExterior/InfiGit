package models.ordenes.cancelacion;

import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

public class Filter extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		//ITS-2374 - NM26659 Resolucion incidencia NO se pueden cancelar ordenes pendientes de unidades cerradas
		String status[] = {
		UnidadInversionConstantes.UISTATUS_PUBLICADA,UnidadInversionConstantes.UISTATUS_CERRADA,UnidadInversionConstantes.UISTATUS_ADJUDICADA,UnidadInversionConstantes.UISTATUS_LIQUIDADA
			    };
		
		UnidadInversionDAO confiD = new UnidadInversionDAO(_dso);
		confiD.listaPorStatus(status,null);
		storeDataSet("uniInverPublicadas",confiD.getDataSet());
	}
}

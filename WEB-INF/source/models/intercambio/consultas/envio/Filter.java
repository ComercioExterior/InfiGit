package models.intercambio.consultas.envio;

import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

public class Filter extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		String tipoInstrumento[] = {
				ConstantesGenerales.INST_TIPO_SUBASTA,
				ConstantesGenerales.INST_TIPO_SUBASTA_COMPETITIVA
			    };
		
		String status[] = {
				UnidadInversionConstantes.UISTATUS_PUBLICADA,
				UnidadInversionConstantes.UISTATUS_ADJUDICADA,
				UnidadInversionConstantes.UISTATUS_CERRADA,
				UnidadInversionConstantes.UISTATUS_LIQUIDADA
			    };
		
		UnidadInversionDAO unIn = new UnidadInversionDAO(_dso);
		ControlArchivoDAO conA = new ControlArchivoDAO(_dso);
		conA.fechaHoy();
		unIn.listaPorStatus(status,tipoInstrumento);
		storeDataSet("uniInverPublicadas",unIn.getDataSet());
		storeDataSet("fecha",conA.getDataSet());
	}
}

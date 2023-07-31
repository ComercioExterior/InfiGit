package models.intercambio.transferencia.generar_archivo;

import models.msc_utilitys.MSCModelExtend;
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
				UnidadInversionConstantes.UISTATUS_PUBLICADA
			    };
		
		_req.getSession().removeAttribute("unidad_vehiculo");
		_req.getSession().removeAttribute("fechaAdjudicacionUI");
		_req.getSession().removeAttribute("generar_archivo-browse.framework.page.record");
		
		UnidadInversionDAO confiD = new UnidadInversionDAO(_dso);
		confiD.listaPorStatus(status,tipoInstrumento,ConstantesGenerales.INST_TIPO_SUBASTA,ConstantesGenerales.ID_TIPO_PRODUCTO_SITME);
		storeDataSet("uniInverPublicadas",confiD.getDataSet());
	}
}

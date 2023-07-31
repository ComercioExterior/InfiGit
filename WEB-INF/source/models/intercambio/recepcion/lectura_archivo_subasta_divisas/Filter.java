package models.intercambio.recepcion.lectura_archivo_subasta_divisas;

import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

import static com.bdv.infi.logic.interfaces.ConstantesGenerales.*;
public class Filter extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		UnidadInversionDAO confiD = new UnidadInversionDAO(_dso);
		confiD.listaUnidadesAdjudicarPorTipoProductoStatusInstrumentoFinanciero(ID_TIPO_PRODUCTO_SUBASTA_DIVISA,UnidadInversionConstantes.UISTATUS_CERRADA);
		storeDataSet("uniInverPublicadas",confiD.getDataSet());
	}//UnidadInversionConstantes.UISTATUS_CERRADA
}

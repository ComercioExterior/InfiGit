package models.intercambio.operaciones_DICOM.operaciones_ofertas.operaciones_abonos.envio_archivo;

import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

public class Filter extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo buscando las unidades de inversión
	 */
	public void execute() throws Exception {
		
		_record.setValue("tipo_prod",ConstantesGenerales.ID_TIPO_PRODUCTO_DICOM);

		_record.setValue("estatus_ui",UnidadInversionConstantes.UISTATUS_PUBLICADA);
		//
		
		_record.setValue("tipo_solicitud",String.valueOf(ConstantesGenerales.UI_OFERTA));
		
		storeDataSet("record", _record);
	}
}

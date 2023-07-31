package models.unidad_inversion.configuracion_jornada;

import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

public class Filter extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo buscando las unidades de inversión
	 */
	public void execute() throws Exception {

		_record.setValue("tipo_prod",ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL+","+ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL);
		_record.setValue("estatus_ui",UnidadInversionConstantes.UISTATUS_REGISTRADA+","+UnidadInversionConstantes.UISTATUS_PUBLICADA);

		_record.setValue("uni_id","");
		storeDataSet("record", _record);
	}
}

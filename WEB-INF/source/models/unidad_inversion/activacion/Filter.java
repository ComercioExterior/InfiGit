package models.unidad_inversion.activacion;

import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

public class Filter extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo buscando las unidades de inversión
	 */
	public void execute() throws Exception {
		//_record.setValue("filtro",ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL); //NM29643 - INFI_SICAD_II
		
		//NM25287 - INFI_SICAD_II	
		//Adaptacion para tipo de producto SUBASTA_DIVISA_PERSONAL en el flujo de SICAD 2 nm26659 06/06/2014
		_record.setValue("tipo_prod",ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL);
		//_record.setValue("tipo_prod",ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL);
		_record.setValue("estatus_ui",UnidadInversionConstantes.UISTATUS_REGISTRADA+","+UnidadInversionConstantes.UISTATUS_PUBLICADA);

		_record.setValue("uni_id","");
		storeDataSet("record", _record);
	}
}

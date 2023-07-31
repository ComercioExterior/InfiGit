package models.intercambio.batch_liquidacion.enviar_archivo.clavenet_personal.subasta_divisas_personal;

import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;

public class Filter extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo buscando las unidades de inversión
	 */
	public void execute() throws Exception {
				
		//NM25287 - INFI_SICAD_II	
		_record.setValue("tipo_prod",ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL+","+ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL);
		storeDataSet("record", _record);
		
	}
}

package models.intercambio.transferencia.cierre_proceso_subasta_divisas_personal;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

public class Filter extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		UnidadInversionDAO confiD = new UnidadInversionDAO(_dso);
		confiD.listarFeCierreSubastaDivisas(UnidadInversionConstantes.UISTATUS_PUBLICADA,ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL);
		
		DataSet unidad = confiD.getDataSet();
		if(unidad.count()<=0){
			unidad.addNew();
			unidad.setValue("undinv_id", "0");
			unidad.setValue("undinv_nombre", null);
			unidad.setValue("undinv_serie", null);
		}
		//else
		{
			//Validar si aún el mercado se encuentra abierto para recepción de ordenes (PARAMETROS SITME)
			if(!confiD.validarMercadoAbiertoParametrosSitme(true,ConstantesGenerales.ESTATUS_ORDEN_RECIBIDA)){
				throw new Exception("El mercado aún se encuentra abierto para recepción de ordenes en Clavenet Personal o existen operaciones que aún no han sido registradas en INFI");
			}
		}
		storeDataSet("uniInver",unidad);
	}
}

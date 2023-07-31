package models.intercambio.batch_abono_cuenta_dolares.enviar_archivo.clavenet_personal.subasta_divisas_personal;

import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

import models.msc_utilitys.MSCModelExtend;

public class Filter extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo buscando las unidades de inversión
	 */
	public void execute() throws Exception {
		
		System.out.println("filter execute");
		UnidadInversionDAO unIn = new UnidadInversionDAO(_dso);
		unIn.listarUnidadesParaAbonoCuentaDolaresPorTransaccionId(TransaccionNegocio.ORDEN_PAGO);	
		storeDataSet("unidadesInversion",unIn.getDataSet());
	}

}

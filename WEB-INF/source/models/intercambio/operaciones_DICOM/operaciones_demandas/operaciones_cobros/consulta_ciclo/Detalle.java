package models.intercambio.operaciones_DICOM.operaciones_demandas.operaciones_cobros.consulta_ciclo;

import models.intercambio.consultas.ciclos.ConsultaCicloDetalle;

import com.bdv.infi.logic.interfaces.ControlProcesosOpsMonedaLocal;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
public class Detalle extends ConsultaCicloDetalle{
	
	/**
	 * Ejecuta la transaccion del modelo buscando las unidades de inversión
	 */
	public void execute() throws Exception {
		super.execute();
	}
	
	protected String getCiclo() throws Exception{
		return ControlProcesosOpsMonedaLocal.PROCESO_DEMANDA_COBRO_DICOM.getCiclo();//TODO Colocar el tipo de ciclo correcto
	}	
}

package models.intercambio.batch_abono_cuenta_dolares.consulta.venta_titulo;

import models.intercambio.consultas.ciclos.ConsultaCicloDetalleVentas;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

public class Detalle extends ConsultaCicloDetalleVentas{
	
	/**
	 * Ejecuta la transaccion del modelo buscando las unidades de inversión
	 */
	public void execute() throws Exception {
		super.execute();
	}
	
	protected String getCiclo() throws Exception{
		return TransaccionNegocio.CICLO_BATCH_CTA_NACIONAL_MONEDA_EXT_VENTA_TITULO;
	}	
}

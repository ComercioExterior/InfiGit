package models.intercambio.batch_abono_cuenta_dolares.enviar_archivo.cupones;

import models.intercambio.consultas.detalle.DetalleDeOperacionesAbonoMonedaExtranjera;
import models.intercambio.consultas.detalle.DetalleDeOperacionesAbonoMonedaExtranjeraPagoCupones;

import org.apache.log4j.Logger;

public class Detalle extends DetalleDeOperacionesAbonoMonedaExtranjeraPagoCupones {
	
	Logger logger = Logger.getLogger(Detalle.class);
	
	public void execute() throws Exception {
		super.execute();		
	}
	
	protected void getOperaciones() throws Exception{
		operaciones = statement.executeQuery(ordenDAO.detalleDeAbonoCuentaDolaresPagoCupones());
	}
}

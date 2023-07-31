package models.intercambio.batch_abono_cuenta_dolares.enviar_archivo.sitme;

import models.intercambio.consultas.detalle.DetalleDeOperacionesAbonoMonedaExtranjera;

import org.apache.log4j.Logger;

public class Detalle extends DetalleDeOperacionesAbonoMonedaExtranjera {
	
	Logger logger = Logger.getLogger(Detalle.class);
	
	public void execute() throws Exception {
		super.execute();		
	}
	
	protected void getOperaciones() throws Exception{
		operaciones = statement.executeQuery(inversionDAO.detalleDeAbonoCuentaDolares(Integer.parseInt(_record.getValue("undinv_id")),null,true));
	}
}

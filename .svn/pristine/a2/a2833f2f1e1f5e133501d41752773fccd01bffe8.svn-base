package models.intercambio.batch_liquidacion.enviar_archivo.sitme;

import models.intercambio.consultas.detalle.DetalleDeOperaciones;

import org.apache.log4j.Logger;

public class Detalle extends DetalleDeOperaciones {
	
	Logger logger = Logger.getLogger(Detalle.class);
	
	public void execute() throws Exception {
		super.execute();		
	}
	
	protected void getOperaciones() throws Exception{
		operaciones = statement.executeQuery(inversionDAO.detalleDeOperacionBatchPorUnidadLiquidacion(Integer.parseInt(_record.getValue("undinv_id")),true));
	}
}

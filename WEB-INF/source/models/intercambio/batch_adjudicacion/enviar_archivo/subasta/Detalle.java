package models.intercambio.batch_adjudicacion.enviar_archivo.subasta;

import models.intercambio.consultas.detalle.DetalleDeOperaciones;

import org.apache.log4j.Logger;

public class Detalle extends DetalleDeOperaciones {
	
	Logger logger = Logger.getLogger(Detalle.class);
	
	public void execute() throws Exception {
		super.execute();		
	}
	
	protected void getOperaciones() throws Exception{
		operaciones = statement.executeQuery(inversionDAO.detalleDeOperacionBatchPorUnidad(Integer.parseInt(_record.getValue("undinv_id")),false));
	}
}

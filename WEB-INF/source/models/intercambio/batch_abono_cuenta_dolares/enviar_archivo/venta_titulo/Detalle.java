package models.intercambio.batch_abono_cuenta_dolares.enviar_archivo.venta_titulo;

import models.intercambio.consultas.detalle.DetalleDeOperacionesAbonoMonedaExtranjeraVentaTitulo;
import org.apache.log4j.Logger;

public class Detalle extends DetalleDeOperacionesAbonoMonedaExtranjeraVentaTitulo {
	
	Logger logger = Logger.getLogger(Detalle.class);
	
	public void execute() throws Exception {	
		super.execute();		
	}
	
	protected void getOperaciones() throws Exception{
		operaciones = statement.executeQuery(ordenDAO.detalleDeAbonoCuentaDolaresVentaTitulo(_record.getValue("idtitulo")));
	}
}

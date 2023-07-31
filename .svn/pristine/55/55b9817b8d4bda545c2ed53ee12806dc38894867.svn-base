package models.generar_opics;

import com.bdv.infi.dao.MensajeOpicsDAO;
import models.msc_utilitys.MSCModelExtend;
/**
 * Clase encargada de mostrar el detalle de los deal ticket asociados a una orden
 * @author elaucho
 */
public class DetallesDealTicketOpics extends MSCModelExtend{

	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub
		
		//DAO a utilizar
		MensajeOpicsDAO mensajeOpics = new MensajeOpicsDAO(_dso);
		
		//NM25287 SICAD 2. Modificación para registro de id_orden en Deal Opics
		mensajeOpics.listarDetallesDeal(Long.parseLong(_req.getParameter("opics_id")));
		
		//mensajeOpics.listarDetallesDeal(Long.parseLong(_req.getParameter("ordene_id")));
		
		//Se publica el dataset
		storeDataSet("deals",mensajeOpics.getDataSet());
	}//fin execute
}//Fin clase

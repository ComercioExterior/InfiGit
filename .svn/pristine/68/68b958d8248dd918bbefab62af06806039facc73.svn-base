package models.actualizar_cheque;

import com.bdv.infi.dao.GestionPagoDAO;
import com.bdv.infi.dao.OrdenDAO;

import models.msc_utilitys.MSCModelExtend;
/**
 * Clase que muestra los procesos relacionados a pago con cheques de un cliente en especifico
 * @author elaucho
 */
public class ActualizarChequeBrowse extends MSCModelExtend{

	@Override
	public void execute() throws Exception {
		
		//DAO a utilizar
		GestionPagoDAO gestionPagoDAO = new GestionPagoDAO(_dso);
		gestionPagoDAO.listarInstruccionesCheque(Long.parseLong(_req.getParameter("client_id")));
		
		//Se publican los dataset
		storeDataSet("procesos", gestionPagoDAO.getDataSet());
		storeDataSet("request",getDataSetFromRequest());
			
		//DAO a utilizar
		OrdenDAO ordenDAO = new OrdenDAO(_dso);
		
		//Mostramos las ordenes de recompra, a las cuales se le haya asociado una instrucción de pago CHEQUE
		ordenDAO.listarRecompraInstruccionCheque(Long.parseLong(_req.getParameter("client_id")));
		
		//Se publica el DataSet
		storeDataSet("recompra",ordenDAO.getDataSet());
		
	}//fin execute
	
}//fin clase

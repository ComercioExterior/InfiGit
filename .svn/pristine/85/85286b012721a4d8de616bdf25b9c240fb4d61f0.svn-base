package models.gestion_pago_cheque;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.GestionPagoDAO;

/**
 * Modelo que muestra las operaciones financieras involucradas por moneda y cliente
 * @author elaucho
 * @version 1.0
 */
public class GestionDetalleOperaciones extends MSCModelExtend{
	
	@Override
	public void execute() throws Exception {
		
	//Objeto DAO de Operacion
		GestionPagoDAO gestionPagoDAO = new GestionPagoDAO(_dso);
		
	//Cliente de sesion
		DataSet _cliente =(DataSet)_req.getSession().getAttribute("gestion_pago_cheque-browse.framework.page.record");
		long cliente = 0;
		
		if(_cliente!=null &&_cliente.count()>0)
		{
			_cliente.first();
			_cliente.next();
			
			cliente = Long.parseLong(_cliente.getValue("client_id"));
		}else{
			 cliente = Long.parseLong(_req.getSession().getAttribute("client_id").toString());
		}
		
		
	//Metodo que listara las operaciones	
		gestionPagoDAO.listarDetalleOperacionesPagoCupones(cliente,_req.getParameter("tipoProducto"), _req.getParameter("moneda"));
		
	//Se publica el dataset para mostrarlo en el html
		storeDataSet("operaciones", gestionPagoDAO.getDataSet());
		
	}

}
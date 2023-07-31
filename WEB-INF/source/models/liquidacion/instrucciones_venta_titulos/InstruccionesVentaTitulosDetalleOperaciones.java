package models.liquidacion.instrucciones_venta_titulos;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.OperacionDAO;
/**
 * 
 * @author CT09153
 *
 */
public class InstruccionesVentaTitulosDetalleOperaciones extends MSCModelExtend{
	
	@Override
	public void execute() throws Exception {
		
	//Objeto DAO de Operacion
		OperacionDAO operacionDAO = new OperacionDAO(_dso);
		
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
		operacionDAO.listarOperacionesSinIntruccionesVentaTitulos(cliente, _req.getParameter("moneda"),_req.getParameter("instruccion"));
		
	//Se publica el dataset para mostrarlo en el html
		storeDataSet("operaciones", operacionDAO.getDataSet());
		
	}

}

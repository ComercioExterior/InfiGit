package models.gestion_pago_cheque;

import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.GestionPagoDAO;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

/**
 * Clase encargada de mostrar al cliente especificado aquellas operaciones que no tengan instruccion de pago,
 * para las cuales podria indicar si desea que le sean canceladas en cheque o en trasferencias inclusive ambas.
 * @author elaucho
 */
public class GestionPagoBrowse extends MSCModelExtend{

	@Override
	public void execute() throws Exception {
		DataSet _nombreCliente = new DataSet();
		String nombreCliente = null;
		long idCliente = Long.parseLong(_req.getParameter("client_id")!=null?_req.getParameter("client_id"):_req.getSession().getAttribute("client_id").toString());
			
		//CONSULTA DE OPERACIONES DE PAGO PENDIENTES
		GestionPagoDAO gestionPagoDAO = new GestionPagoDAO(_dso);
		gestionPagoDAO.listarDetalleOperacionesPagoCupones(idCliente,_record.getValue("tipo_producto"),_req.getParameter("monedaPago"));		//gestionPagoDAO.listarOperacionesMonedas(idCliente);
		
		storeDataSet("operaciones", gestionPagoDAO.getDataSet());
		
		//CONSULTA DE DATOS DEL CLIENTE
		ClienteDAO clienteDAO = new ClienteDAO(_dso);
		clienteDAO.listarPorId(String.valueOf(idCliente));
		clienteDAO.getDataSet().first();
		clienteDAO.getDataSet().next();
		nombreCliente = clienteDAO.getDataSet().getValue("client_nombre");
		
		_req.getSession().setAttribute("nombre", nombreCliente);
		_req.getSession().setAttribute("client_id",idCliente);	
		
		_nombreCliente.append("nombre",java.sql.Types.VARCHAR);
		_nombreCliente.addNew();
		_nombreCliente.setValue("nombre", nombreCliente);
		storeDataSet("nombre",_nombreCliente);
		
		// REMOVER DATOS DE SESION		
		_req.getSession().removeAttribute("seleccion");
		_req.getSession().removeAttribute("moneda");
			
		//Se busca si el cliente tiene instrucciones de pago para mostrarlas 
		gestionPagoDAO.listarProcesosPorCliente(idCliente);
		System.out.println("procesos: "+gestionPagoDAO.getDataSet());
		storeDataSet("procesos", gestionPagoDAO.getDataSet());
		
	}//Fin execute
}//Fin GestionPagoBrowse

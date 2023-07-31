package models.liquidacion.instrucciones_venta_titulos;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.GestionPagoDAO;
/**
 * Clase que muestra los procesos a los cuales se les ha asociado una instruccion de pago
 * Muestra las operaciones financieras pendientes por pagar a las cuales no se les haya asociado una
 * instrucción de pago
 * @author elaucho
 */
public class InstruccionesVentaTitulosBrowse extends MSCModelExtend{

	@Override
	public void execute() throws Exception {
		
		/*
		 * Se recibe el id del cliente, para mostrar el monto de las operaciones pendientes por pagar
		 * sin instrucciones de pago.
		 */		
		long idCliente = Long.parseLong(_req.getParameter("client_id")!=null?_req.getParameter("client_id"):_req.getSession().getAttribute("client_id").toString());
		

		_req.getSession().setAttribute("client_id",idCliente);
		
		//DAO que contiene el metodo que nos traera las operaciones financieras
		GestionPagoDAO gestionPagoDAO = new GestionPagoDAO(_dso);
		gestionPagoDAO.listarOperacionesSinInstruccionVentaTitulos(idCliente);

		//Se publica el DataSet
		storeDataSet("operaciones", gestionPagoDAO.getDataSet());
		
		//Se monta en sesion el nombre del usuario
		ClienteDAO clienteDAO = new ClienteDAO(_dso);
		clienteDAO.listarPorId(String.valueOf(idCliente));
		clienteDAO.getDataSet().first();
		clienteDAO.getDataSet().next();
		String nombreCliente = clienteDAO.getDataSet().getValue("client_nombre");
		_req.getSession().setAttribute("nombre", nombreCliente);
		
		//Se publica el nombre del usuario	
		DataSet _nombreCliente = new DataSet();
		_nombreCliente.append("nombre",java.sql.Types.VARCHAR);
		_nombreCliente.addNew();
		_nombreCliente.setValue("nombre", nombreCliente);
		storeDataSet("nombre",_nombreCliente);
		
		//Removemos datos de sesion
		_req.getSession().removeAttribute("infi.banco.instrucciones");
		_req.getSession().removeAttribute("infi.cheques");
		_req.getSession().removeAttribute("infi.transferencias");
		_req.getSession().removeAttribute("infi.transferencias.internacionales");
		_req.getSession().removeAttribute("infi.cheques.sesion");
		_req.getSession().removeAttribute("seleccion");
		_req.getSession().removeAttribute("moneda");
		_req.getSession().removeAttribute("numero_transferencias_nacionales");
		_req.getSession().removeAttribute("numero_cheques");
		_req.getSession().removeAttribute("numero_transferencias_internacionales");

		//Se busca si el cliente tiene instrucciones de pago para mostrarlas
		gestionPagoDAO.listarProcesosPorCliente(idCliente);
		storeDataSet("procesos", gestionPagoDAO.getDataSet());
	}//Fin execute
	
	
	@Override
	public boolean isValid() throws Exception {

		//Definicion de variables
		boolean flag 				= super.isValid();
		
		if (flag)
		{

			//Validaciones de campos null para banco sin intermediario
			
			if (_req.getParameter("client_id")==null &&  _req.getSession().getAttribute("client_id")==null)
			{  
				_record.addError("Cliente","Este campo es obligatorio para procesar el formularios");
					flag = false;
			}
			

		}//FIN IF flag

		return flag;
	 
		
	}
}

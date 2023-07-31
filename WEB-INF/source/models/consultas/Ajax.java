package models.consultas;

import com.bdv.infi.dao.BlotterDAO;
import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.CustodiaDAO;
import com.bdv.infi.dao.EmpresaDefinicionDAO;
import com.bdv.infi.dao.MonedaDAO;
import com.bdv.infi.dao.TipoPersonaDAO;
import com.bdv.infi.dao.TitulosDAO;
import com.bdv.infi.dao.TransaccionDAO;
import com.bdv.infi.dao.UnidadInversionDAO;

import megasoft.AbstractModel;
import megasoft.DataSet;

/**
 * Clase Gen&eacute;rica que dado un c&oacute;digo espec&iacute;fico permite invocar un m&eacute;todo para una funci&oacute;n ajax dentro de una vista
 * @author Megasoft Computaci&oacute;n
 */
public class Ajax extends AbstractModel {
			
	
	public void execute() throws Exception {
		
		
		
			try {
				//codigo de AjaxAddnew a ejecutar
				int cod_ajax = Integer.parseInt(_req.getParameter("cod_ajax"));
								
				switch (cod_ajax) {
				case 1: 
					this.ajaxTitulosPorCliente();	
				break;
				
				case 2:	
					this.ajaxListarTitulos();
				break;
				
				case 3:
					this.ajaxAgregarReglaTransaccionFinanciera();
				break;

				}
				
			} catch (RuntimeException e) {				
				e.printStackTrace();
			}
			

		
		/*if(_req.getParameter("cod_ajax")!=null){ 
			
			if(_req.getParameter("cod_ajax").equals("1"))//C&oacute;digo de AjaxAddnew 1
				this.ajaxTitulosPorCliente();	
			else{
				
				if(_req.getParameter("cod_ajax").equals("2"))//C&oacute;digo de AjaxAddnew 2
					this.ajaxListarTitulos();
					
			}//else //Preguntar por c&oacute;digo de otras funciones
		}	*/				
		
		
	}
	
	/**
	 * M&eacute;todo que ejecuta la consulta de titulos por cliente para la funci&oacute;n ajax con c&oacute;digo = 1 (cod_ajax).
	 * Exporta los datos necesarios para llenar el combo de selecci&oacute;n de t&iacute;tulos en custodia de un cliente espec&iacute;fico.
	 * @author Erika Valerio, Megasoft Computaci&oacute;n
	 * @throws Exception
	 */
	void ajaxTitulosPorCliente() throws Exception{
		
		CustodiaDAO custDAO = new CustodiaDAO(_dso);
		ClienteDAO clienteDAO = new ClienteDAO(_dso);
		DataSet titClientes = new DataSet();
		DataSet datCliente = new DataSet();
		titClientes.append("titulo_id", java.sql.Types.VARCHAR);
		datCliente.append("tipper_id", java.sql.Types.VARCHAR);
		datCliente.addNew();
		datCliente.setValue("tipper_id", "");
		
		long idCliente =-1;
		
		if(_req.getParameter("id_cliente")!=null && !_req.getParameter("id_cliente").equals("")){
			idCliente = Long.parseLong(_req.getParameter("id_cliente"));
			custDAO.listarTitulosPorClientes(idCliente);
			titClientes = custDAO.getDataSet();
			//buscar datos del Cliente
			clienteDAO.listarPorId(String.valueOf(idCliente));
			datCliente = clienteDAO.getDataSet();
		}
		
		storeDataSet("datos", titClientes);		
		storeDataSet("datos_cliente", datCliente);		
	}
	
	/**
	 * M&eacute;todo que realiza una consulta de todos los t&iacute;tulos existentes (est&eacute;n o no en custodia)
	 * para la funci&oacute;n ajax con c&oacute;digo = 2 (cod_ajax)
	 * @author Erika Valerio, Megasoft Computaci&oacute;n
	 * @throws Exception
	 */
	void ajaxListarTitulos() throws Exception{
		TitulosDAO titDAO = new TitulosDAO(_dso);		
		titDAO.listarTitulos();
		storeDataSet("datos", titDAO.getDataSet());	
	}
	
	/**
	 * Permite buscar los datos necesarios para armar una nueva regla aplicable a una transacci&oacute;n espec&iacute;fica
	 * @throws Exception
	 */
	void ajaxAgregarReglaTransaccionFinanciera() throws Exception{
		
		DataSet _datos = new DataSet();//dataSet para Datos especiales
		_datos.append("num_regla_transac", java.sql.Types.VARCHAR);

		//Recuperar el numero de regla de transacci&oacute;n en la cual va		
		int num_regla_transac = Integer.parseInt(String.valueOf(_req.getSession().getAttribute("num_regla_transac")));
		num_regla_transac = num_regla_transac + 1;
		
		//colocar en sesion el numero de regla de transacci&oacute;n
		_req.getSession().setAttribute("num_regla_transac", String.valueOf(num_regla_transac));
		
		TransaccionDAO transacDAO = new TransaccionDAO(_dso);
		MonedaDAO monedaDAO = new MonedaDAO(_dso);
		BlotterDAO blotterDAO = new BlotterDAO(_dso);
		TipoPersonaDAO tipoPersonaDAO = new TipoPersonaDAO(_dso);
		UnidadInversionDAO unidadInversionDAO = new UnidadInversionDAO(_dso);	
		EmpresaDefinicionDAO empresaDAO = new EmpresaDefinicionDAO(_dso);
	
		//Listar transacciones de Negocio
		transacDAO.listar(null);		
		//Listar monedas
		monedaDAO.listar();		
		//Listar blotters
		blotterDAO.listar();		
		//listar tipos de persona
		tipoPersonaDAO.listarTodos();		
		//listar todas las Unidades de Inversi&oacute;n
		unidadInversionDAO.listar();		
		//listar Empresas 
		empresaDAO.listarTodas();
		
		_datos.addNew();		
		_datos.setValue("num_regla_transac", String.valueOf(num_regla_transac));		
				
		//registrar los datasets exportados por este modelo
		storeDataSet("transac_negocio", transacDAO.getDataSet());		
		storeDataSet("monedas", monedaDAO.getDataSet());		
		storeDataSet("blotters", blotterDAO.getDataSet());
		storeDataSet("tipo_pers", tipoPersonaDAO.getDataSet());
		storeDataSet("unid_inversion", unidadInversionDAO.getDataSet());		
		storeDataSet("empresas", empresaDAO.getDataSet());
		storeDataSet("datos", _datos);
		
		
	}

}

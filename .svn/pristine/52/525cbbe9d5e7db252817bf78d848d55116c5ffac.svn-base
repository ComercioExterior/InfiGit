package models.picklist.pick_clientes_multiple;

import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.TipoPersonaDAO;
import megasoft.*;

/**
 * Clase encargada de construir el picklist de clientes para selecci&oacute;n multiple. Realiza la b&uacute;squeda de los clientes seg&uacute;n los criterios del filtro
 * y guarda los datos que ser&aacute;n retornados al campo de clientes que hizo la llamada al picklist.
 * @author Erika valerio, Megasoft Computaci&oacute;n 
 */
public class PickClienteMultiple extends AbstractModel
{

	/**
	* Ejecuta la transaccion del modelo
	*/
	public void execute() throws Exception
	{
		ClienteDAO clienteDAO = new ClienteDAO(_dso);
		TipoPersonaDAO tipoPersDAO = new TipoPersonaDAO(_dso);
		DataSet _dsParam = null;//DataSet para almacenar los par&aacute;metros del request
		long cedRif = 0;		
				
		//Recuperar en un dataSet los par&aacute;metros del request para exportarlos al picklist
		_dsParam = getDataSetFromRequest();
		
		//guardar en Session variables del request:
		//NECESARIO PARA FUNCIONAMIENTO DEL PICKLIST
		if (_req.getParameter("name_id") != null)
			_req.getSession().setAttribute("datasetParam",_dsParam);
		else
		{
			_dsParam = (DataSet) _req.getSession().getAttribute("datasetParam");
		}
		////////////////////////////////////////////////////////////////////

		////***********Implementaci&oacute;n del picklist***////////////////////////////////////
		
		//Buscar los tipos de personas
		tipoPersDAO.listarTodos();		
		
		storeDataSet("tipoPers", tipoPersDAO.getDataSet());
	
		if(_req.getParameter("buscar")!=null){//si se oprimio el boton buscar 
			
			if(_req.getParameter("client_cedrif")!=null && !_req.getParameter("client_cedrif").equals(""))
				cedRif = Long.parseLong(_req.getParameter("client_cedrif")); 
			clienteDAO.listar(0, _req.getParameter("tipper_id"), cedRif, _req.getParameter("client_nombre"), 0, null);
						
		}
				
		storeDataSet("table", clienteDAO.getDataSet());
		
		////////////////////************************////////////////////////////////////////
		
		storeDataSet("dsparam",_dsParam);
	}
}
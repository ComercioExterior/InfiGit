package models.picklist.pick_contraparte;

import megasoft.AbstractModel;
import megasoft.DataSet;

import com.bdv.infi.dao.ClienteDAO;

/**
 * Clase encargada de construir el picklist de contrapartes contra CARMEN. 
 * @author Nelson Visbal, Megasoft Computaci&oacute;n *
 */
public class PickContraparte extends AbstractModel{
	
	/**
	* Ejecuta la transaccion del modelo
	*/
	@SuppressWarnings("static-access")
	public void execute() throws Exception {
		ClienteDAO clienteDAO = new ClienteDAO(_dso);
		DataSet _dsParam = null;//DataSet para almacenar los par&aacute;metros del request
		
		//Recuperar en un dataSet los par&aacute;metros del request para exportarlos al picklist
		_dsParam = getDataSetFromRequest();
			
		//NECESARIO PARA FUNCIONAMIENTO DEL PICKLIST
		if (_req.getParameter("name_id") != null){
			_req.getSession().setAttribute("datasetParam",_dsParam);
		}else {
			_dsParam = (DataSet) _req.getSession().getAttribute("datasetParam");
		}
	
		////***********Implementaci&oacute;n del picklist***////////////////////////////////////
		if(_req.getParameter("buscar")!=null){//si se oprimio el boton buscar 
			String codigo = _req.getParameter("codigo") != null? _req.getParameter("codigo").trim() : "";
			String nombre = _req.getParameter("nombre") != null? _req.getParameter("nombre").trim() : "";
			clienteDAO.buscarContrapartesCarmen(codigo, nombre);					
		}
				
		storeDataSet("table", clienteDAO.getDataSet());
		
		////////////////////************************////////////////////////////////////////
		
		storeDataSet("dsparam",_dsParam);
	}
}
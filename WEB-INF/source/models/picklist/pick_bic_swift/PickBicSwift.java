package models.picklist.pick_bic_swift;

import megasoft.AbstractModel;
import megasoft.DataSet;
import com.bdv.infi.dao.ClienteCuentasDAO;

/**
 * Clase asociada al picklist para códigos BIC/SWIFT
 * @author Erika Valerio
 *
 */
public class PickBicSwift extends AbstractModel {
	/**
	* Ejecuta la transaccion del modelo
	*/
	public void execute() throws Exception {
		ClienteCuentasDAO clienteCuentasDAO = new ClienteCuentasDAO(_dso);		
		DataSet _dsParam = null;//DataSet para almacenar los par&aacute;metros del request
		
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
	
		////***********Implementaci&oacute;n del picklist***////////////////////////////////////
		if(_req.getParameter("buscar")!=null){//si se oprimio el boton buscar 
				
			clienteCuentasDAO.listarCodigosBicOPICS(_req.getParameter("bic"), _req.getParameter("nombre_banco"));			
						
		}
		
		DataSet _filter = getDataSetFromRequest();
		//registrar los datasets exportados por este modelo
		storeDataSet("filter", _filter);
				
		storeDataSet("table", clienteCuentasDAO.getDataSet());
	
		storeDataSet("dsparam",_dsParam);	
		
	}
	
	/**
	 * Validaciones generales de la clase
	 */
	public boolean isValid() throws Exception {			
		
		boolean flag 	= super.isValid();
		
		if (flag)
		{
			//Validar cantidad de caracteres ingresados para la búsqueda por código y nombres
			
			if (_req.getParameter("bic")!=null && !_req.getParameter("bic").trim().equals("") && _req.getParameter("bic").trim().length()<3)
			{  
				_record.addError("BIC/SWIFT","Ingrese al menos 3 caracteres para la búsqueda de c&oacute;digos BIC");
				flag = false;
			}
			
			if (_req.getParameter("nombre_banco")!=null && !_req.getParameter("nombre_banco").trim().equals("") && _req.getParameter("nombre_banco").trim().length()<3)
			{  
				_record.addError("Nombre del Banco","Ingrese al menos 3 caracteres para la búsqueda de c&oacute;digos BIC");
				flag = false;
			}

			
		}//fin if flag
			return flag;
	}
}

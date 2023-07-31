package models.picklist.pick_usuarios;

import com.bdv.infi.dao.UsuarioSeguridadDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

import megasoft.*;

/**
 * Clase encargada de construir el picklist de clientes. Realiza la b&uacute;squeda de los clientes seg&uacute;n los criterios del filtro
 * y guarda los datos que ser&aacute;n retornados al campo de cliente que hizo la llamada al picklist.
 * @author Erika valerio, Megasoft Computaci&oacute;n *
 */
public class PickUsuario extends AbstractModel
{
	/**
	* Ejecuta la transaccion del modelo
	*/
	public void execute() throws Exception
	{
		//obtener datasource para accesar a las tablas de seguridad
		_dso = db.getDataSource( _app.getInitParameter(ConstantesGenerales.DATASOURCE_SEGURIDAD_SEPA));
		
		UsuarioSeguridadDAO usuarioSeguridadDAO = new UsuarioSeguridadDAO(_dso);
		DataSet _dsParam = null;//DataSet para almacenar los par&aacute;metros del request
		String ced= null;		
		String nombre=null;
		String apellido=null;
		String nick=null;
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
	
		if(_req.getParameter("buscar")!=null){//si se oprimio el boton buscar 
			
			if(_req.getParameter("ci")!=null && !_req.getParameter("ci").equals(""))
				ced=_req.getParameter("ci"); 
			if(_req.getParameter("fullname")!=null && !_req.getParameter("fullname").equals(""))
				nombre=_req.getParameter("fullname");
			if(_req.getParameter("lastname")!=null && !_req.getParameter("lastname").equals(""))
				apellido=_req.getParameter("lastname");
			if(_req.getParameter("userid")!=null && !_req.getParameter("userid").equals(""))
				nick=_req.getParameter("userid");
			usuarioSeguridadDAO.listar(nick,nombre,apellido);
						
		}
				
		storeDataSet("table", usuarioSeguridadDAO.getDataSet());
		
		////////////////////************************////////////////////////////////////////
		
		storeDataSet("dsparam",_dsParam);
	}
}
package models.picklist.enter_orden;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import megasoft.AbstractModel;
import megasoft.DataSet;

import com.bdv.infi.dao.SolicitudesSitmeDAO;

/**
 * Clase encargada de construir el picklist de clientes. Realiza la b&uacute;squeda de los clientes seg&uacute;n los criterios del filtro
 * y guarda los datos que ser&aacute;n retornados al campo de cliente que hizo la llamada al picklist.
 * @author Erika valerio, Megasoft Computaci&oacute;n *
 */
public class EnterOrden extends AbstractModel
{
	/*** Logger APACHE*/
	//private Logger logger = Logger.getLogger(EnterOrden.class);
	Pattern pat_nro_orden = Pattern.compile("^[0-9]{1,12}$");
	Matcher m; //Matcher que verifica que se cumpla con el patron
	DataSet _mensaje = new DataSet();
	DataSet _visibility = new DataSet();
	String nro_orden = "";
	boolean valid = true;
	
	/**
	* Ejecuta la transaccion del modelo
	*/
	@SuppressWarnings("static-access")
	public void execute() throws Exception
	{
		SolicitudesSitmeDAO solSitme = new SolicitudesSitmeDAO(_dso);
		
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
		
		if(_req.getParameter("buscar")!=null && valid){//si se oprimio el boton buscar
			//System.out.println("Se oprimio buscar");
			solSitme.getSolicitudes(Long.parseLong(nro_orden), 0, null, null, null, null, null, null, null, false, false);
			if(solSitme.getDataSet().count() == 0){
				_mensaje.setValue("mensaje", "No se encontr&oacute; ninguna orden con el Nro. "+nro_orden);
			}else{
				_visibility.setValue("visibility", "block");
			}
		}else{
			//if(!valid) System.out.println("Orden No valida");
			//if(_req.getParameter("buscar")==null) System.out.println("No se oprimio buscar");
		}
		
		storeDataSet("mensaje", _mensaje);
		storeDataSet("table", solSitme.getDataSet());
		storeDataSet("dsparam",_dsParam);
		storeDataSet("visibility", _visibility);
		
	}
	
	/**
	 * Validaciones Basicas del action
	 * @return true si la clase y sus par&aacute;metros son v&aacute;lidos, false en caso contrario 
	 * @throws Exception
	 */	
	public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();
		
		//Para setear el valor del mensaje
		_mensaje.append("mensaje", java.sql.Types.VARCHAR);
		_mensaje.addNew();
		_mensaje.setValue("mensaje", "");
		
		//Para setear el valor de display de la tabla
		_visibility.append("visibility", java.sql.Types.VARCHAR);
		_visibility.addNew();
		_visibility.setValue("visibility", "none");
		
		nro_orden = _req.getParameter("nro_orden");//.trim();
		//System.out.println("ORDEN: "+nro_orden);

		if(_req.getParameter("buscar")!=null){//si se oprimio el boton buscar 
			if(nro_orden.equals("") || nro_orden==null){//nro de orden vacio
				_mensaje.setValue("mensaje", "El Nro. de Orden no puede ser vac&iacute;o");
				valid = false;
				//_record.addError("Nro. de Orden", "El Nro. de Orden no puede ser vac&iacute;o");
				//flag = false;
			}else{
				nro_orden = nro_orden.trim();
				m = pat_nro_orden.matcher(nro_orden); //Compara contra el patron
				if(!m.find()){ //No coincide con el patron
					//System.out.println("ORDEN invalida");
					_mensaje.setValue("mensaje", "El Nro. de Orden debe constar &uacute;nicamente de d&iacute;gitos (hasta 12)");
					valid = false;
					//_record.addError("Nro. de Orden", "El Nro. de Orden debe constar de hasta 12 n&uacute;meros");
					//flag = false;
				}
			}
		}
		
		return flag;	
	}
}
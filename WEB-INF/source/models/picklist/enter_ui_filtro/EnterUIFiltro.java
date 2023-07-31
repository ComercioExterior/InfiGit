package models.picklist.enter_ui_filtro;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import megasoft.AbstractModel;
import megasoft.DataSet;

import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

/**
 * Clase encargada de construir el picklist de consulta de unidades de inversion
 * tomando en cuenta los filtros de b˙squeda de estatus y tipo de producto
 * @author NM25287
 */
public class EnterUIFiltro extends AbstractModel
{
	/*** Logger APACHE*/
	//private Logger logger = Logger.getLogger(EnterOrden.class);
	Pattern pat_name_ui = Pattern.compile("^[0-9a-zA-Z¡…Õ”⁄·ÈÌÛ˙—Ò_\\- ]{1,100}$");
	Matcher m; //Matcher que verifica que se cumpla con el patron
	DataSet _mensaje = new DataSet();
	DataSet _visibility = new DataSet();
	String name_ui = "";
	String estatus_ui = "";
	String tipo_prod_ui = "";
	String tipo_negocio = null;
	String formaOrden = null;
	boolean ventaTaquilla = false;
	String tipoSolicitud = null; //Indica si la solicitud es de oferta o Demanda
	boolean tipoSolicitudOferta = false;
	String nombreTipoNegocio="";
	boolean valid = true;
	
	/**
	* Ejecuta la transaccion del modelo
	*/
	public void execute() throws Exception
	{		
		//SolicitudesSitmeDAO solSitme = new SolicitudesSitmeDAO(_dso);
		UnidadInversionDAO uiDAO = new UnidadInversionDAO(_dso);
		
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
			uiDAO.listarUnidadesPorProductoStatusNombre(tipo_prod_ui, estatus_ui, name_ui,tipo_negocio,ventaTaquilla,tipoSolicitudOferta);
			if(uiDAO.getDataSet().count() == 0){
				_mensaje.setValue("mensaje", "No se encontr&oacute; ninguna Unidad de Inversi&oacute;n de nombre '"+name_ui + 
						(tipo_prod_ui!=null&&tipo_prod_ui!=""?("' asociado al tipo de producto '"+ tipo_prod_ui):"") + 
						(estatus_ui!=null&&estatus_ui!=""?("' con el estatus '"+ estatus_ui+"'"):"") +
						(tipo_negocio!=null&&tipo_negocio!=""?("' de Tipo de negocio " +nombreTipoNegocio):""));
			}else{
				_visibility.setValue("visibility", "block");
			}
		}else{
			//if(!valid) System.out.println("Orden No valida");
			//if(_req.getParameter("buscar")==null) System.out.println("No se oprimio buscar");
		}
		
		storeDataSet("mensaje", _mensaje);
		storeDataSet("table", uiDAO.getDataSet());
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
		
		name_ui =      _req.getParameter("name_ui");//.trim();
		estatus_ui =   _req.getParameter("estatus");
		tipo_prod_ui = _req.getParameter("tipo_prod");		
		tipo_negocio=  _req.getParameter("tipo_negocio");
		
		formaOrden= _req.getParameter("forma_orden");
		
		if(formaOrden!=null && formaOrden.equalsIgnoreCase(ConstantesGenerales.INST_FINANCIERO_SIMADI_TAQUILLA)){			
			ventaTaquilla=true;
		}
		
		tipoSolicitud= _req.getParameter("tipo_solicitud");
		if(tipoSolicitud!=null && tipoSolicitud.equalsIgnoreCase(String.valueOf(ConstantesGenerales.UI_OFERTA))){			
			tipoSolicitudOferta=true;
		}
		//NM26659 31/03/2015 TTS_491 Web Service Alto Valor
		//Validacion para configuracion de parametro cuando la pantalla que invoca no utiliza valor tipo de negocio
		if(tipo_negocio!=null && tipo_negocio.equals("@tipo_negocio@")){
			tipo_negocio=null;
		} else if(tipo_negocio!=null){
			if(tipo_negocio.equals(ConstantesGenerales.TIPO_NEGOCIO_ALTO_VALOR)){
				nombreTipoNegocio="Alto Valor";
			}else if(tipo_negocio.equals(ConstantesGenerales.TIPO_NEGOCIO_BAJO_VALOR)){
				nombreTipoNegocio="Bajo Valor";
			}
		}
		//
		
		if(_req.getParameter("buscar")!=null){//si se oprimio el boton buscar 
			if(name_ui.equals("") || name_ui==null){//nombre ui vacio
				_mensaje.setValue("mensaje", "El nombre de la Unidad de Inversi&oacute;n no puede ser vac&iacute;o");
				valid = false;
				//_record.addError("Nro. de Orden", "El Nro. de Orden no puede ser vac&iacute;o");
				//flag = false;
			}else{
				name_ui = name_ui.trim();
				m = pat_name_ui.matcher(name_ui); //Compara contra el patron
				if(!m.find()){ //No coincide con el patron
					//System.out.println("ORDEN invalida");
					_mensaje.setValue("mensaje", "El nombre de la Unidad de Inversi&oacute;n debe constar &uacute;nicamente de letras y/o guiones (hasta 100)");
					valid = false;
					//_record.addError("Nro. de Orden", "El Nro. de Orden debe constar de hasta 12 n&uacute;meros");
					//flag = false;
				}
			}
		}
		
		return flag;	
	}
}
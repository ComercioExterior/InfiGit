package models.picklist.enter_ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import megasoft.AbstractModel;
import megasoft.DataSet;

import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

/**
 * Clase encargada de construir el picklist de clientes. Realiza la b&uacute;squeda de los clientes seg&uacute;n los criterios del filtro
 * y guarda los datos que ser&aacute;n retornados al campo de cliente que hizo la llamada al picklist.
 * @author Erika valerio, Megasoft Computaci&oacute;n *
 */
public class EnterUI extends AbstractModel
{
	/*** Logger APACHE*/
	//private Logger logger = Logger.getLogger(EnterOrden.class);
	Pattern pat_name_ui = Pattern.compile("^[0-9a-zA-ZÁÉÍÓÚáéíóúÑñ_\\- ]{1,100}$");
	Matcher m; //Matcher que verifica que se cumpla con el patron
	DataSet _mensaje = new DataSet();
	DataSet _visibility = new DataSet();
	String name_ui = "";
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
System.out.println("parametros---------: "+_dsParam);
		
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
			String msg = "No se encontr&oacute; ninguna Unidad de Inversi&oacute;n de nombre '"+name_ui+"'";
			//System.out.println("filtro -------: "+_req.getParameter("filtro"));
			//Se verifica si el tipo de consulta de UI a utilizar segun el filtro pasado (de pasarse)
			if (_record.getValue("filtro")!= null && !_record.getValue("filtro").equals("")){
				//FILTRO PARA COBRO DE ADJUDICACION - SICAD II
				if(_req.getParameter("filtro").equals("sicad2_cobroadj_enviar_filter")){
					uiDAO.listarUnidadesParaCobroAdjBatchSicadII(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL, name_ui);
					if(uiDAO.getDataSet().count() == 0){
						msg += " para el tipo de producto SICAD 2";
					}
				}else if(_req.getParameter("filtro").equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL)){
						uiDAO.listarUnidadesParaCobroAdjBatchSicadII(_req.getParameter("filtro"), name_ui);
						if(uiDAO.getDataSet().count() == 0){
							msg += " para el tipo de producto SICAD 2";
						}
				}else{
					uiDAO.getUiPorNombre(name_ui);
				}
			}			
			else{ //Filtra solo por el nombre introducido
				uiDAO.getUiPorNombre(name_ui);
			}
			if(uiDAO.getDataSet().count() == 0){
				_mensaje.setValue("mensaje", msg);
			}else{
				_visibility.setValue("visibility", "block");
			}
		}else{
			System.out.println("********* NO SE PUEDE EJECUTAR *********");
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
		
		name_ui = _req.getParameter("name_ui");//.trim();
		//System.out.println("NAME_UI: "+name_ui);

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
		System.out.println("SE PUEDE EJECUTAR ------> " + flag);
		return flag;	
	}
}
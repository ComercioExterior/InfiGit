package models.custodia.estructura_tarifaria;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.CustodiaEstructuraTarifariaDAO;
import com.bdv.infi.dao.TipoPersonaDAO;
import com.bdv.infi.dao.TitulosDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

public class ClienteSelect extends MSCModelExtend {
	
	/**
	 * Criterios de busqueda
	 */
	private DataSet dsCriteriosCliente = new DataSet();
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		TipoPersonaDAO tipoPersDAO = new TipoPersonaDAO(_dso);
		
		String comision=null;
		String cliente=null;
		String cedrif=null;
		String tipoPersona=null;
		
		DataSet _dsCliente = new DataSet();
		DataSet totalRegistros= new DataSet();
		DataSet dsApoyo = new DataSet();
		dsApoyo.append("boton_grabar_ini", java.sql.Types.VARCHAR);
		dsApoyo.append("boton_grabar_fin", java.sql.Types.VARCHAR);	
		String entry = _req.getParameter("entry");	
		if (entry == null){
			entry = "insert";
		}
		
		dsApoyo.addNew();
		// Si ya se tienen los filtros buscar la informacion
		if (entry.equalsIgnoreCase("estructuratarifaria")) {
			dsApoyo.setValue("boton_grabar_ini", "<!----");
			dsApoyo.setValue("boton_grabar_fin", "--->");	
			_req.getSession().removeAttribute("dsCriteriosCliente");
		} else {
			CustodiaEstructuraTarifariaDAO estructuraTarifaria = new CustodiaEstructuraTarifariaDAO(_dso);
			if (entry.equalsIgnoreCase("insert")) {
				dsCriteriosCliente = (DataSet)_req.getSession().getAttribute("dsCriteriosCliente");
			}else {
				dsCriteriosCliente.append("nombrecliente",java.sql.Types.VARCHAR);				
				dsCriteriosCliente.append("cedrifcliente",java.sql.Types.VARCHAR);
				dsCriteriosCliente.append("tipopersona",java.sql.Types.VARCHAR);
				dsCriteriosCliente.addNew();
				if (_record.getValue("client_nombre") != null && !_record.getValue("client_nombre").equals("")) {
					dsCriteriosCliente.setValue("nombrecliente", _record.getValue("client_nombre"));
				}
				if (_record.getValue("client_cedrif") != null && !_record.getValue("client_cedrif").equals("")) {
					dsCriteriosCliente.setValue("cedrifcliente", _record.getValue("client_cedrif"));
				}
				if (_record.getValue("tipper_id") != null && !_record.getValue("tipper_id").equals("")) {
					dsCriteriosCliente.setValue("tipopersona", _record.getValue("tipper_id"));
				}
				_req.getSession().removeAttribute("dsCriteriosCliente");
				_req.getSession().setAttribute("dsCriteriosCliente",dsCriteriosCliente);
			}
			dsCriteriosCliente.next();
			// se busca por los campos que esten llenos (diferentes de null)
			if (_record.getValue("comision_id")!=null){
				comision=_record.getValue("comision_id");
			}else{
				comision= getSessionObject("comision_id").toString();
			}
			if (dsCriteriosCliente.getValue("nombrecliente") != null) {
				cliente=dsCriteriosCliente.getValue("nombrecliente"); 
			} 
			if (dsCriteriosCliente.getValue("cedrifcliente") != null) {
				cedrif=dsCriteriosCliente.getValue("cedrifcliente"); 
			}
			if (dsCriteriosCliente.getValue("tipopersona") != null) {
				tipoPersona=dsCriteriosCliente.getValue("tipopersona"); 
			}
			estructuraTarifaria.listarTodosClientes(comision, cliente, cedrif, tipoPersona);
			_dsCliente= estructuraTarifaria.getDataSet();
			totalRegistros = estructuraTarifaria.getTotalRegistros();
			if (_dsCliente.count() == 0){
				dsApoyo.setValue("boton_grabar_ini", "<!----");
				dsApoyo.setValue("boton_grabar_fin", "--->");	
				_req.getSession().removeAttribute("dsCriteriosCliente");
			} else {				
				dsApoyo.setValue("boton_grabar_ini", "");
				dsApoyo.setValue("boton_grabar_fin", "");
				_dsCliente.first();
				while (_dsCliente.next()){
					StringBuffer html = new StringBuffer();
					if(Integer.parseInt(_dsCliente.getValue("html"))==ConstantesGenerales.FALSO){
						html.append("<INPUT TYPE='CHECKBOX' NAME='selecc' VALUE='"+_dsCliente.getValue("_row")+"' />");
						html.append("<input type='hidden' name='idCliente' value='"+_dsCliente.getValue("client_id")+"' />");
						html.append("<input type='hidden' name='client_cedrif' value='"+_dsCliente.getValue("client_cedrif")+"' /> ");
						html.append("<input type='hidden' name='client_nombre' value='"+_dsCliente.getValue("client_nombre")+"' />");
						_dsCliente.setValue("html",html.toString());
					}else{
						html.append("<IMG SRC='../images/infi_gn_roles_seguridad.gif' WIDTH='15' ALIGN='top' HEIGHT='15' TITLE=\"Cliente Asociado a la Comision: "+_dsCliente.getValue("comision_nombre")+"\" BORDER='0' style='cursor:hand'/>");
						_dsCliente.setValue("html",html.toString()); 
					}
				}
			}
		}
		
		tipoPersDAO.listarTodos();
		
		storeDataSet("tipoPers", tipoPersDAO.getDataSet());
		storeDataSet("clientes", _dsCliente);
		storeDataSet("total", totalRegistros);
		storeDataSet("dsApoyo", dsApoyo);
		
//		crear dataset
		DataSet _filter = getDataSetFromRequest();
		_filter.append("comision_id", java.sql.Types.VARCHAR);
		_filter.addNew();
		_filter.setValue("comision_id", comision);
		//registrar los datasets exportados por este modelo
		storeDataSet("filter", _filter);
	}
}

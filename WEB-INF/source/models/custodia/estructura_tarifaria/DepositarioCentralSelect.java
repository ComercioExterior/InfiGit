package models.custodia.estructura_tarifaria;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bancovenezuela.comun.dao.GenericoDAO;
import com.bdv.infi.dao.CustodiaEstructuraTarifariaDAO;

public class DepositarioCentralSelect extends MSCModelExtend {

	/**
	 * Criterios de busqueda
	 */
	private DataSet dsCriteriosDepositario = new DataSet();
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
			
		String comision=null;
		String empresa=null;
		String rif=null;
		String tipoPersona=null;
		
		DataSet _dsDepositario = new DataSet();
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
			_req.getSession().removeAttribute("dsCriteriosDepositario");
		} else {
			CustodiaEstructuraTarifariaDAO estructuraTarifaria = new CustodiaEstructuraTarifariaDAO(_dso);
			if (entry.equalsIgnoreCase("insert")) {
				dsCriteriosDepositario = (DataSet)_req.getSession().getAttribute("dsCriteriosDepositario");
			}else {
				dsCriteriosDepositario.append("nombreempresa",java.sql.Types.VARCHAR);				
				dsCriteriosDepositario.append("rifempresa",java.sql.Types.VARCHAR);
				dsCriteriosDepositario.append("tipopersona",java.sql.Types.VARCHAR);
				dsCriteriosDepositario.addNew();
				if (_record.getValue("empres_nombre") != null && !_record.getValue("empres_nombre").equals("")) {
					dsCriteriosDepositario.setValue("nombreempresa", _record.getValue("empres_nombre"));
				}
				if (_record.getValue("empres_rif") != null && !_record.getValue("empres_rif").equals("")) {
					dsCriteriosDepositario.setValue("rifempresa", _record.getValue("empres_rif"));
				}
				if (_record.getValue("tipper_id") != null && !_record.getValue("tipper_id").equals("")) {
					dsCriteriosDepositario.setValue("tipopersona", _record.getValue("tipper_id"));
				}
				_req.getSession().removeAttribute("dsCriteriosDepositario");
				_req.getSession().setAttribute("dsCriteriosDepositario",dsCriteriosDepositario);
			}
			dsCriteriosDepositario.next();
			// se busca por los campos que esten llenos (diferentes de null)
			if (_record.getValue("comision_id")!=null){
				comision=_record.getValue("comision_id");
			}else{
				comision= getSessionObject("comision_id").toString();
			}
			if (dsCriteriosDepositario.getValue("nombreempresa") != null) {
				empresa=dsCriteriosDepositario.getValue("nombreempresa"); 
			} 
			if (dsCriteriosDepositario.getValue("rifempresa") != null) {
				rif=dsCriteriosDepositario.getValue("rifempresa"); 
			}
			if (dsCriteriosDepositario.getValue("tipopersona") != null) {
				tipoPersona=dsCriteriosDepositario.getValue("tipopersona"); 
			}
			estructuraTarifaria.listarTodosDepositarios(comision, empresa, rif, tipoPersona);
			_dsDepositario= estructuraTarifaria.getDataSet();
			totalRegistros = estructuraTarifaria.getTotalRegistros();
			if (_dsDepositario.count() == 0){
				dsApoyo.setValue("boton_grabar_ini", "<!----");
				dsApoyo.setValue("boton_grabar_fin", "--->");	
				_req.getSession().removeAttribute("dsCriteriosDepositario");
			} else {				
				dsApoyo.setValue("boton_grabar_ini", "");
				dsApoyo.setValue("boton_grabar_fin", "");
			}
		}
		//GenericoDAO
		DataSet _tipoPersona=new DataSet();
		_tipoPersona.append("tipper_id",java.sql.Types.INTEGER);
		_tipoPersona.append("tipper_nombre",java.sql.Types.VARCHAR);
		_tipoPersona.addNew();
		_tipoPersona.setValue("tipper_id","J");
		_tipoPersona.setValue("tipper_nombre","Jurídico");
		_tipoPersona.addNew();
		_tipoPersona.setValue("tipper_id","G");
		_tipoPersona.setValue("tipper_nombre","Gubernamental");
		
		storeDataSet("tipoPers", _tipoPersona);
		storeDataSet("depositarios", _dsDepositario);
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

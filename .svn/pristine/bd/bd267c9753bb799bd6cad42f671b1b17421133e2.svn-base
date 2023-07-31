package models.custodia.estructura_tarifaria;

import java.util.Date;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.CustodiaEstructuraTarifariaDAO;
import com.bdv.infi.dao.MonedaDAO;
import com.bdv.infi.dao.TitulosDAO;
import com.bdv.infi.dao.UIEmpresaDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

public class TituloSelect extends MSCModelExtend {

	/**
	 * Criterios de busqueda
	 */
	private DataSet dsCriteriosTitulo = new DataSet();
	
	/**
	 * Ejecuta la transaccion del modelo
	 */	
	public void execute() throws Exception {
		
		CustodiaEstructuraTarifariaDAO estructuraTarifaria = new CustodiaEstructuraTarifariaDAO(_dso);
		MSCModelExtend me = new MSCModelExtend();
		MonedaDAO monedaDAO = new MonedaDAO(_dso);
		
		String comision=null;
		String descripcion=null;
		String id= null;
		String moneda= null;
		Date fechaEmDesde = null;
		Date fechaEmHasta = null;
		Date fechaVencDesde = null;
		Date fechaVencHasta = null;
		
		DataSet _dsTitulo = new DataSet();
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
			_req.getSession().removeAttribute("dsCriteriosTitulo");
		} else {
			TitulosDAO titulosDAO = new TitulosDAO(_dso);
			if (entry.equalsIgnoreCase("insert")) {
				dsCriteriosTitulo = (DataSet)_req.getSession().getAttribute("dsCriteriosTitulo");
			}else {
				dsCriteriosTitulo.append("idTitulo",java.sql.Types.VARCHAR);				
				dsCriteriosTitulo.append("descripcion",java.sql.Types.VARCHAR);
				dsCriteriosTitulo.append("idMoneda",java.sql.Types.VARCHAR);
				dsCriteriosTitulo.append("fechaEmisionDesde",java.sql.Types.VARCHAR);
				dsCriteriosTitulo.append("fechaEmisionHasta",java.sql.Types.VARCHAR);
				dsCriteriosTitulo.append("fechaVencimientoDesde",java.sql.Types.VARCHAR);
				dsCriteriosTitulo.append("fechaVencimientoHasta",java.sql.Types.VARCHAR);
				dsCriteriosTitulo.addNew();
				if (_req.getParameter("titulo_id") != null && !_req.getParameter("titulo_id").equals("")) {
					dsCriteriosTitulo.setValue("idTitulo", _req.getParameter("titulo_id"));
				}
				if (_req.getParameter("titulo_descripcion") != null && !_req.getParameter("titulo_descripcion").equals("")) {
					dsCriteriosTitulo.setValue("descripcion", _req.getParameter("titulo_descripcion"));
				}
				if (_req.getParameter("moneda_id") != null && !_req.getParameter("moneda_id").equals("")) {
					dsCriteriosTitulo.setValue("idMoneda", _req.getParameter("moneda_id"));
				}
				if(_req.getParameter("fe_em_desde")!=null && !_req.getParameter("fe_em_desde").equals("")){
					dsCriteriosTitulo.setValue("fechaEmisionDesde",_req.getParameter("fe_em_desde"));
				}			
				if(_req.getParameter("fe_em_hasta")!=null && !_req.getParameter("fe_em_hasta").equals("")){
					dsCriteriosTitulo.setValue("fechaEmisionHasta",_req.getParameter("fe_em_hasta"));
				}
				if(_req.getParameter("fe_venc_desde")!=null && !_req.getParameter("fe_venc_desde").equals("")){
					dsCriteriosTitulo.setValue("fechaVencimientoDesde",_req.getParameter("fe_venc_desde"));
				}
				if(_req.getParameter("fe_venc_hasta")!=null && !_req.getParameter("fe_venc_hasta").equals("")){
					dsCriteriosTitulo.setValue("fechaVencimientoHasta",_req.getParameter("fe_venc_hasta"));
				}
				_req.getSession().removeAttribute("dsCriteriosTitulo");
				_req.getSession().setAttribute("dsCriteriosTitulo",dsCriteriosTitulo);
			}
			dsCriteriosTitulo.next();
			// se busca por los campos que esten llenos (diferentes de null)
			if (_record.getValue("comision_id")!=null){
				comision=_record.getValue("comision_id");
			}else{
				comision= getSessionObject("comision_id").toString();
			}
			if (dsCriteriosTitulo.getValue("idTitulo") != null) {
				id=dsCriteriosTitulo.getValue("idTitulo"); 
			} 
			if (dsCriteriosTitulo.getValue("descripcion") != null) {
				descripcion=dsCriteriosTitulo.getValue("descripcion"); 
			}
			if (dsCriteriosTitulo.getValue("idMoneda") != null) {
				moneda=dsCriteriosTitulo.getValue("idMoneda"); 
			}
			if (dsCriteriosTitulo.getValue("fechaEmisionDesde") != null) {
				fechaEmDesde = me.StringToDate(dsCriteriosTitulo.getValue("fechaEmisionDesde"), ConstantesGenerales.FORMATO_FECHA);
			}
			if (dsCriteriosTitulo.getValue("fechaEmisionHasta") != null) {
				fechaEmHasta = me.StringToDate(dsCriteriosTitulo.getValue("fechaEmisionHasta"), ConstantesGenerales.FORMATO_FECHA);
			}
			if (dsCriteriosTitulo.getValue("fechaVencimientoDesde") != null) {
				fechaVencDesde = me.StringToDate(dsCriteriosTitulo.getValue("fechaVencimientoDesde"),ConstantesGenerales.FORMATO_FECHA);
			}
			if (dsCriteriosTitulo.getValue("fechaVencimientoHasta") != null) {
				fechaVencHasta = me.StringToDate(dsCriteriosTitulo.getValue("fechaVencimientoHasta"), ConstantesGenerales.FORMATO_FECHA);
			}
			
			titulosDAO.listar(id,descripcion, moneda, fechaEmDesde, fechaEmHasta, fechaVencDesde, fechaVencHasta,comision);
			_dsTitulo= titulosDAO.getDataSet();
			totalRegistros = titulosDAO.getTotalRegistros();
			if (_dsTitulo.count() == 0){
				dsApoyo.setValue("boton_grabar_ini", "<!----");
				dsApoyo.setValue("boton_grabar_fin", "--->");	
				_req.getSession().removeAttribute("dsCriteriosTitulo");
			} else {				
				dsApoyo.setValue("boton_grabar_ini", "");
				dsApoyo.setValue("boton_grabar_fin", "");
			}
		}
		
		storeDataSet("titulos", _dsTitulo);		
		storeDataSet("total", totalRegistros);
		storeDataSet("dsApoyo", dsApoyo);	
		
		//crear dataset
		DataSet _filter = getDataSetFromRequest();
		_filter.append("comision_id", java.sql.Types.VARCHAR);
		_filter.addNew();
		_filter.setValue("comision_id", comision);
		//registrar los datasets exportados por este modelo
		storeDataSet("filter", _filter);
		
		monedaDAO.listar(); 
		storeDataSet( "moneda", monedaDAO.getDataSet());	
	}
}

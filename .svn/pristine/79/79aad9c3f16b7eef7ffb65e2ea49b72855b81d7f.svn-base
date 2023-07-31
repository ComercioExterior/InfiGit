package models.picklist.pick_titulos;

import java.util.Date;

import megasoft.AbstractModel;
import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.MonedaDAO;
import com.bdv.infi.dao.TitulosDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

public class PickTitulo extends AbstractModel {
	/**
	* Ejecuta la transaccion del modelo
	*/
	public void execute() throws Exception {
		TitulosDAO usuarioDAO = new TitulosDAO(_dso);
		MSCModelExtend me = new MSCModelExtend();
		MonedaDAO monedaDAO = new MonedaDAO(_dso);
		DataSet _dsParam = null;//DataSet para almacenar los par&aacute;metros del request
		String id= null;		
		String descripcion=null;
		Date fechaEmDesde = null;
		Date fechaEmHasta = null;
		Date fechaVencDesde = null;
		Date fechaVencHasta = null;

		
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
			
			//---Recuperar fechas
			//Conversion de fechas
			if(_req.getParameter("fe_em_desde")!=null && !_req.getParameter("fe_em_desde").equals("")){
				fechaEmDesde = me.StringToDate(_req.getParameter("fe_em_desde"), ConstantesGenerales.FORMATO_FECHA);
			}			
			if(_req.getParameter("fe_em_hasta")!=null && !_req.getParameter("fe_em_hasta").equals("")){
				fechaEmHasta = me.StringToDate(_req.getParameter("fe_em_hasta"), ConstantesGenerales.FORMATO_FECHA);
			}
			if(_req.getParameter("fe_venc_desde")!=null && !_req.getParameter("fe_venc_desde").equals("")){
				fechaVencDesde = me.StringToDate(_req.getParameter("fe_venc_desde"),ConstantesGenerales.FORMATO_FECHA);
			}
			if(_req.getParameter("fe_venc_hasta")!=null && !_req.getParameter("fe_venc_hasta").equals("")){
				fechaVencHasta = me.StringToDate(_req.getParameter("fe_venc_hasta"), ConstantesGenerales.FORMATO_FECHA);
			}

			
			if(_req.getParameter("titulo_id")!=null)
				id=_req.getParameter("titulo_id"); 
			if(_req.getParameter("titulo_descripcion")!=null)
				descripcion=_req.getParameter("titulo_descripcion");
			//condicion que registringe el picklist a buscar solo los titulos que no tenga configurado precios
			// si no se desea condicionar el picklist pasar por request el indicador(in_precio) en cualquier valor distinto de 1
			if((_req.getParameter("in_precio")=="1")||(Integer.parseInt(_req.getParameter("in_precio"))==1)){
				usuarioDAO.listarSiTienePrecio(id,descripcion, _req.getParameter("moneda_id"), fechaEmDesde, fechaEmHasta, fechaVencDesde, fechaVencHasta);
			}else{
				usuarioDAO.listar(id,descripcion, _req.getParameter("moneda_id"), fechaEmDesde, fechaEmHasta, fechaVencDesde, fechaVencHasta,null);    
			}
						
		}
		DataSet _filter = getDataSetFromRequest();
		//registrar los datasets exportados por este modelo
		storeDataSet("filter", _filter);
				
		storeDataSet("table", usuarioDAO.getDataSet());
	
		storeDataSet("dsparam",_dsParam);
		
		monedaDAO.listar(); 
		storeDataSet( "moneda", monedaDAO.getDataSet());		
	}
}

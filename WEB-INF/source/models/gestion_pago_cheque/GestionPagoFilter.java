package models.gestion_pago_cheque;

import com.bdv.infi.dao.UnidadInversionDAO;

import models.msc_utilitys.MSCModelExtend;
/**
 * Clase que remueve todos los datos de sesion subidos durante el proceso de instrucciones de pago
 * @author elaucho
 */
public class GestionPagoFilter extends MSCModelExtend{
	
	@Override
	public void execute() throws Exception {
		UnidadInversionDAO confiD = new UnidadInversionDAO(_dso);
		
		confiD.listarTipoProducto();		
		storeDataSet("productoTipo",confiD.getDataSet());
		
		/*
		 * Removemos datos de sesion
		 */
		_req.getSession().removeAttribute("gestion_pago_cheque-browse.framework.page.using-page");
		_req.getSession().removeAttribute("gestion_pago_cheque-browse.framework.page.record");
		_req.getSession().removeAttribute("gestion_pago_cheque-browse.framework.page.current-page");
		_req.getSession().removeAttribute("infi.operaciones.procesos");
		_req.getSession().removeAttribute("infi.monto.operaciones");
		_req.getSession().removeAttribute("gestion_pago_cheque-browse.framework.page.query-string");
		_req.getSession().removeAttribute("client_id");
		_req.getSession().removeAttribute("datasetParam");
		_req.getSession().removeAttribute("seleccion");
		_req.getSession().removeAttribute("infi.monto.operaciones.cambio");
		_req.getSession().removeAttribute("moneda");
		_req.getSession().removeAttribute("nombre");
		_req.getSession().removeAttribute("operacion_cambio");
		_req.getSession().removeAttribute("tasa_servicio");		
		_req.getSession().removeAttribute("tipo_producto_id");
		
		

	}//fin execute

}//fin clase

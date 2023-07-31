package models.liquidacion.instrucciones_venta_titulos;

import megasoft.AbstractModel;

/**
 * Clase filtro para limpiar datos de session para InstruccionesVentaTitulos
 */
public class InstruccionesVentaTitulosFilter extends AbstractModel{

	@Override
	public void execute() throws Exception {

		//Removemos datos de sesion
		_req.getSession().removeAttribute("gestion_pago_cheque-browse.framework.page.using-page");
		_req.getSession().removeAttribute("gestion_pago_cheque-browse.framework.page.record");
		_req.getSession().removeAttribute("gestion_pago_cheque-browse.framework.page.current-page");
		_req.getSession().removeAttribute("infi.operaciones.procesos");
		_req.getSession().removeAttribute("infi.monto.operaciones");
		_req.getSession().removeAttribute("gestion_pago_cheque-browse.framework.page.query-string");
		_req.getSession().removeAttribute("client_id");
		_req.getSession().removeAttribute("datasetParam");
		_req.getSession().removeAttribute("infi.banco.instrucciones");
		_req.getSession().removeAttribute("infi.cheques");
		_req.getSession().removeAttribute("infi.transferencias");
		_req.getSession().removeAttribute("infi.transferencias.internacionales");
		_req.getSession().removeAttribute("infi.cheques.sesion");
		_req.getSession().removeAttribute("seleccion");
		_req.getSession().removeAttribute("infi.monto.operaciones.cambio");
		_req.getSession().removeAttribute("moneda");
		_req.getSession().removeAttribute("nombre");
		_req.getSession().removeAttribute("operacion_cambio");
		_req.getSession().removeAttribute("tasa_servicio");		
		_req.getSession().removeAttribute("display");
		_req.getSession().removeAttribute("instruccion_pago");

	}
	

	

}

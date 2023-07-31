package models.bcv.mesas_cambios_inter;

import megasoft.Logger;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.UsuarioDAO;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

public class Procesar extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {

		String statusP = _req.getParameter("statusp");
		String statusE = _req.getParameter("statusE");
		String Tipo = _req.getParameter("Tipo");
		String idOrdenes  = _req.getParameter("idOrdenes");
//		String seleccion  = (String)_req.getParameter("seleccion");
		String fecha = _req.getParameter("fecha");
		String tipoMovimiento = _req.getParameter("tipomovimiento");

		System.out.println("tipoMovimientotipoMovimientotipoMovimiento : "  + tipoMovimiento);
		
		boolean todos= false;
//		if(seleccion.equalsIgnoreCase("todos")){
//			todos = true;
//		}else{
//			todos = false;
//			
//		}
		if (idOrdenes == null) {
			idOrdenes="";
		}
//		UsuarioDAO usuarioDAO = new UsuarioDAO(_dso);		
//		int idUsuario = Integer.parseInt((usuarioDAO.idUserSession(getUserName())));

		try {
			System.out.println("LLEGO AL ENVIO DE INTERBANCARIO MESA");
			EnvioOperaciones notificacion = new EnvioOperaciones(getNumeroDePagina(), getPageSize(), todos,idOrdenes, statusP, _dso, 1, fecha,statusE,Tipo,tipoMovimiento);
			Thread t = new Thread(notificacion);
			t.start();
		
		} catch (Exception e) {
			Logger.error(this,e.toString(),e);
		}
	}
	
	
	public boolean isValid() throws Exception {
		boolean valido = true;
//		ProcesosDAO procesosDAO = new ProcesosDAO(_dso);
//		
//		procesosDAO
//				.listarPorTransaccionActiva(TransaccionNegocio.WS_BCV_MESAS);
//		if (procesosDAO.getDataSet().count() > 0) {
//			_record
//					.addError(
//							"Envio de Operaciones al BCV",
//							"No se puede procesar la solicitud porque otra "
//									+ "persona realizó esta acción y esta actualmente activa");
//			valido = false;
//		}
//		if (_req.getParameter("jornada").equalsIgnoreCase("No existe jornada")) {
//			_record
//			.addError(
//					"Envio de Operaciones al BCV",
//					"No se puede procesar la solicitud ya que no tienen jornada para notificar");
//	valido = false;
//		}
		
		return valido;
	}
}
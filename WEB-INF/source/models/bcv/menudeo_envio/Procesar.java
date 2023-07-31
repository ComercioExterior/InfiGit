package models.bcv.menudeo_envio;

import megasoft.Logger;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.ParametrosDAO;
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
		String combustible = _req.getParameter("combustiblee");
//		System.out.println("combustible : : " + combustible);
		String idOrdenes  = _req.getParameter("idOrdenes");
		String seleccion  = (String)_req.getParameter("seleccion");
		String urlInvocacion = _req.getPathInfo();
		String fecha = _req.getParameter("fecha");
		Integer clienteID      = Integer.parseInt(_req.getParameter("cliente_id") == null ? "0" : _req.getParameter("cliente_id"));
		boolean todos= false;
		if(seleccion.equalsIgnoreCase("todos")){
			todos = true;
		}else{
			todos = false;
			
		}

		UsuarioDAO usuarioDAO = new UsuarioDAO(_dso);		
		int idUsuario = Integer.parseInt((usuarioDAO.idUserSession(getUserName())));
		try {
			EnvioBCVWSMenudeo envioBCVWSMenudeo = new EnvioBCVWSMenudeo(clienteID,getNumeroDePagina(), getPageSize(), todos,idOrdenes, statusP, urlInvocacion, _dso, idUsuario, fecha,statusE,Tipo,combustible);
			Thread t = new Thread(envioBCVWSMenudeo);
			t.start();
		
		} catch (Exception e) {
			Logger.error(this,e.toString(),e);
		}
	}
	
	
	public boolean isValid() throws Exception {
		boolean valido = true;
		ProcesosDAO procesosDAO = new ProcesosDAO(_dso);
		
		procesosDAO
				.listarPorTransaccionActiva(TransaccionNegocio.WS_BCV_MENUDEO);
		if (procesosDAO.getDataSet().count() > 0) {
			_record
					.addError(
							"Envio de Operaciones al BCV",
							"No se puede procesar la solicitud porque otra "
									+ "persona realiz� esta acci�n y esta actualmente activa");
			valido = false;
		}
		
		String paranBCV = ParametrosDAO.listarParametros("TRANSF_BCV_ONLINE",_dso);
		if(!paranBCV.equalsIgnoreCase("1")){
			_record
			.addError(
					"Envio de Operaciones al BCV",
					"No se puede procesar la solicitud ya que el parametro 'TRANSF_BCV_ONLINE' no se encuentra activo");
			valido = false;
		}
		
		return valido;
	}
}
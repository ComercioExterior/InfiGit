package models.bcv.pacto;
import megasoft.Logger;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.config.Parametros;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.UsuarioDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

public class Procesar extends MSCModelExtend {
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		long idUnidad = 0;
		String statusP    = _req.getParameter("statusp");
		String idOrdenes  = _req.getParameter("idOrdenes");
		String seleccion  = (String)_req.getParameter("seleccion");
		String tipoFiltro = _req.getParameter("tipoFiltro");
		String urlInvocacion = _req.getPathInfo();
		String fecha         = _req.getParameter("fecha");
		Integer tasaMinima   = Integer.parseInt(_req.getParameter("tasa_minima") == null ? "0" : _req.getParameter("tasa_minima")); 
		Integer tasaMaxima   = Integer.parseInt(_req.getParameter("tasa_maxima") == null ? "0" : _req.getParameter("tasa_maxima"));
		Integer montoMinimo  = Integer.parseInt(_req.getParameter("monto_minimo") == null ? "0" : _req.getParameter("monto_minimo"));
		Integer montoMaximo  = Integer.parseInt(_req.getParameter("monto_maximo") == null ? "0" : _req.getParameter("monto_maximo"));
		Integer clienteID      = Integer.parseInt(_req.getParameter("cliente_id")        == null ? "0" : _req.getParameter("cliente_id"));
		Integer incluirCliente = Integer.parseInt(_req.getParameter("incluir_cliente")   == null ? "0" : _req.getParameter("incluir_cliente"));
		
		boolean todos, incluir = false;
		if(seleccion.equalsIgnoreCase("todos")){
			todos = true;
		}else{
			todos = false;
			if(tipoFiltro.equals("INCLUIR")){
				incluir = true;
			}else {
				incluir = false;
			}
		}
		
		UsuarioDAO usuarioDAO 	= new UsuarioDAO(_dso);		
		int idUsuario = Integer.parseInt((usuarioDAO.idUserSession(getUserName())));	
		
		try {
			EnvioBCVWSPacto envioBCVWSMenudeo = new EnvioBCVWSPacto(incluirCliente,clienteID,tasaMinima,tasaMaxima,montoMinimo,montoMaximo,idUnidad, getNumeroDePagina(), getPageSize(), todos, incluir, idOrdenes, statusP, urlInvocacion, _dso, idUsuario, fecha);
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
				.listarPorTransaccionActiva(TransaccionNegocio.WS_BCV_PACTO);
		if (procesosDAO.getDataSet().count() > 0) {
			_record
					.addError(
							"Envio de Operaciones al BCV",
							"No se puede procesar la solicitud porque otra "
									+ "persona realizó esta acción y esta actualmente activa");
			valido = false;
		}
		
		String paranBCV = ParametrosDAO.listarParametros(ConstantesGenerales.TRANSF_BCV_ONLINE_AVALOR,_dso);
		if(!paranBCV.equalsIgnoreCase("1")){
			_record
			.addError(
					"Envio de Operaciones al BCV",
					"No se puede procesar la solicitud ya que el parametro 'TRANSF_BCV_ONLINE_ALTO_VALOR' no se encuentra activo");
			valido = false;
		}
		
		return valido;
	}
}
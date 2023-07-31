package models.bcv.intervencion;

import megasoft.DataSet;
import megasoft.Logger;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.UsuarioDAO;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

public class Procesar extends MSCModelExtend {

	String estatusEnvio = "";
	String idOrdenes = "";
	String seleccion = "";
	String urlInvocacion = "";
	String fecha = "";
	String jornada = "";
	String moneda = "";
	boolean todos = false;

	public void execute() throws Exception {

		capturarValores();
		System.out.println("todos es : " + seleccion);

		if (seleccion.equalsIgnoreCase("todos")) {
			todos = true;
		} else {
			todos = false;
		}
		System.out.println("fecha:" + fecha);
		int idUsuario = capturarUsuario();
		System.out.println("todos : " + todos);
		try {
			EnvioBCVWSIntervencion envioBCVWSIntervencion = new EnvioBCVWSIntervencion(getNumeroDePagina(), getPageSize(), todos, idOrdenes, urlInvocacion, _dso, idUsuario, fecha, estatusEnvio, jornada, moneda);
			Thread t = new Thread(envioBCVWSIntervencion);
			t.start();
		} catch (Exception e) {
			Logger.error(this, "Procesar : execute() " + e);
			System.out.println("Procesar : execute() " + e);
		}
	}

	public void capturarValores() throws Exception {
		this.estatusEnvio = _req.getParameter("statusE");
		this.moneda = _req.getParameter("moneda");
		System.out.println("moneda : " + moneda);
		this.idOrdenes = _req.getParameter("idOrdenes");
		this.seleccion = (String) _req.getParameter("seleccion");
		this.urlInvocacion = _req.getPathInfo();
		this.fecha = _req.getParameter("fecha");
		this.jornada = _req.getParameter("jornada");
	}

	public int capturarUsuario() {
		try {
			UsuarioDAO usuarioDAO = new UsuarioDAO(_dso);
			return Integer.parseInt((usuarioDAO.idUserSession(getUserName())));

		} catch (Exception e) {
			Logger.error(this, "Procesar : capturarUsuario() " + e);
			System.out.println("Procesar : capturarUsuario() " + e);
			return 0;
		}
	}

	public boolean isValid() throws Exception {
		boolean valido = true;
		ProcesosDAO procesosDAO = new ProcesosDAO(_dso);
		procesosDAO.listarPorTransaccionActiva(TransaccionNegocio.WS_BCV_INTERVENCION_CLIENTE);

		if (procesosDAO.getDataSet().count() > 0) {
			_record.addError("Envio de Operaciones al BCV", "No se puede procesar la solicitud porque otra " + "persona realiz� esta acci�n y esta actualmente activa");
			valido = false;
		}

		String paranBCV = ParametrosDAO.listarParametros("TRANSF_BCV_ONLINE", _dso);

		if (!paranBCV.equalsIgnoreCase("1")) {
			_record.addError("Envio de Operaciones al BCV", "No se puede procesar la solicitud ya que el parametro 'TRANSF_BCV_ONLINE' no se encuentra activo");
			valido = false;
		}
		
		if (_req.getParameter("jornada").equalsIgnoreCase("0")) {
			_record.addError("Envio de Operaciones al BCV", "No puedes enviar operaciones si no hay jornadas activas.");
			valido = false;
		}

		return valido;
	}
}
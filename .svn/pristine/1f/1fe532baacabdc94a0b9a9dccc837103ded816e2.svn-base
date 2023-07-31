package models.bcv.intervencion_operacion_consulta;

import java.util.HashMap;
import javax.sql.DataSource;
import megasoft.Logger;
import models.bcv.intervencion_operacion.EnvioBCVWSIntervencionOperacion;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.IntervencionDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

public class Procesar extends MSCModelExtend {
	protected HashMap<String, String> parametrosIntervenBCV;
	
	public void execute() throws Exception {
		String estatus =  _record.getValue("statusE");
		String fecha = (String) _record.getValue("fecha");
		String idOrdenes = _req.getParameter("idOrdenes");
		String seleccion = (String) _req.getParameter("seleccion");
		boolean todos = false;
		if (seleccion.equalsIgnoreCase("todos")) {
			todos = true;
		} else {
			todos = false;
		}

		try {
			String usuario = getUserName();
			 EnvioBCVWSIntervencionOperacion envioBCVWSIntervencion = new EnvioBCVWSIntervencionOperacion("", _dso, fecha, estatus, usuario, idOrdenes,todos);
			 Thread t = new Thread(envioBCVWSIntervencion);
			 t.start();
		} catch (Exception e) {
			Logger.error(this, e.toString(), e);
			System.out.println("Procesar : execute() : " + e.toString());
		}
	}
	
	protected void obtenerParametros(DataSource _dso) throws Exception {
		ParametrosDAO parametrosDAO = new ParametrosDAO(_dso);
		parametrosIntervenBCV = parametrosDAO.buscarParametros(ParametrosSistema.INTERFACE_INTERVENCION_BANCARIA);
	}

	public boolean isValid() throws Exception {

		boolean valido = true;
		ProcesosDAO procesosDAO = new ProcesosDAO(_dso);
		obtenerParametros(_dso);
		IntervencionDAO intervencionDao = new IntervencionDAO(_dso);
		procesosDAO.listarPorTransaccionActiva(TransaccionNegocio.WS_BCV_INTERVENCION);

		if (procesosDAO.getDataSet().count() > 0) {
			_record.addError("Envio de Operaciones al BCV", "No se puede procesar la solicitud porque otra " + "persona realizó esta acción y esta actualmente activa");
			valido = false;
		}
		if (parametrosIntervenBCV.get(ParametrosSistema.VALIDA_USUARIO_INTERVEN).equalsIgnoreCase("1")) {
		if (intervencionDao.verificarUsuarioRegistro((String) _record.getValue("fecha"),getUserName())) {
			_record.addError("Envio de Operaciones al BCV", "No se puede procesar la solicitud porque el mismo usuario que registro la operacion la esta notificando");
			valido = false;
		}
		}
		return valido;
	}
}
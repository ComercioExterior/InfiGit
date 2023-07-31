package models.detalles_entidades.detalles_requisito;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import megasoft.db;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.OrdenRequisitoDAO;
import com.bdv.infi.data.OrdenRequisito;

/**
 * Actualiza los requisitos de una orden
 *
 */
public class ActualizarRequisitos extends MSCModelExtend {
	
	public void execute() throws Exception {
		String[] consultasFinal = null;
		Logger logger = Logger.getLogger("ActualizarRequisitos");
		try {
			OrdenRequisitoDAO ordenRequisitoDAO = new OrdenRequisitoDAO(_dso);
			OrdenDAO ordenDAO = new OrdenDAO(_dso);
			ArrayList<OrdenRequisito> listaRequisitos = new ArrayList<OrdenRequisito>();
			if (_req.getParameter("ord_id") != null) {// viene de consulta			
				String[] parametros = _req.getParameterValues("pendientes");
				if (parametros != null){
					for (int i = 0; i < parametros.length; i++) {
						OrdenRequisito requisito = new OrdenRequisito();
						requisito.setId(Integer.parseInt(parametros[i]));
						requisito.setOrdeneId(Integer.parseInt(_req.getParameter("ord_id")));
						requisito.setUsuarioRecepcion(getUserName());
						listaRequisitos.add(requisito);
					}
				}
			}
			String[] consultas = ordenRequisitoDAO.actualizar(listaRequisitos);
			consultasFinal = new String[consultas.length+1];
			for (int i = 0; i < consultas.length; i++) {
				consultasFinal[i] = consultas[i];
			}
			consultasFinal[(consultasFinal.length-1)] = ordenDAO.actualizarObservaciones(Integer.parseInt(_req.getParameter("ord_id")), _req.getParameter("observaciones"));
			db.execBatch(_dso, consultasFinal);
		} catch (Exception e) {
			logger.error("Error en la actualización de los requisitos",e);
			_record.addError("Error", "Error en la actualización de los requisitos");
		}
	}
}


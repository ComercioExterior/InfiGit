package models.intercambio.consultas.ciclos;

import java.util.Date;

import megasoft.db;
import models.msc_utilitys.MSCModelExtend;

import org.apache.log4j.Logger;

import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.UsuarioDAO;
import com.bdv.infi.data.Proceso; 
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

public class CierreCiclo extends MSCModelExtend {
	
	public void execute() throws Exception {
		
		Logger logger = Logger.getLogger(CierreCiclo.class);
		logger.debug("Cerrando ciclo " + _record.getValue("idEjecucion"));
		ControlArchivoDAO controlArchivoDAO = new ControlArchivoDAO(_dso);
		ProcesosDAO procesosDAO=new ProcesosDAO(_dso);
		UsuarioDAO usuarioDAO = new UsuarioDAO(_dso);
		Proceso proceso=new Proceso();
		proceso.setCicloEjecucionId(Integer.parseInt(_record.getValue("idEjecucion")));
		Date fechaInicio = new Date();
		proceso.setFechaInicio(fechaInicio);
		proceso.setFechaValor(fechaInicio);
		proceso.setFechaFin(fechaInicio);
		proceso.setUsuarioId(Integer.parseInt(usuarioDAO.idUserSession(getUserName())));		
		proceso.setDescripcionError("Proceso de  FORCE_CIERRE_CICLO ejecutado por el usuario "  + getUserName());
		
		proceso.setTransaId(TransaccionNegocio.FORCE_CIERRE_CICLO); //Crear una nueva transaccion (nuevo registro  en la tabla 012) de tipo FORCE_CIERRE_CICLO para registrar en 807 un proceso de tipo FORCE_CIERRE_CICLO
		//al momento de realizar el Forzar Cierre.
		String queryProceso = procesosDAO.insertar(proceso,TransaccionNegocio.FORCE_CIERRE_CICLO);
		db.exec(_dso, queryProceso);
		
		controlArchivoDAO.cerrarCiclo(Integer.parseInt(_record.getValue("idEjecucion")));
	}	
}

package com.bdv.infi.logic.interfaz_operaciones_INTERVENCION;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.sql.DataSource;
import megasoft.db;
import org.apache.log4j.Logger;

import com.bdv.infi.dao.IntervencionDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.TasaCambioDAO;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.model.intervencion.Inventario;
import com.bdv.infi.model.inventariodivisas.Configuracion;
import com.bdv.infi.model.menudeo.Monedas;
import com.bdv.infi.webservices.beans.CredencialesDeUsuario;
import com.bdv.infi.webservices.beans.InventarioIntervencionDTO;

public class LecturaIntervencion implements Runnable {

	private ProcesosDAO procesosDAO;
	private Proceso proceso;
	List<Monedas> listaTasas;
	OrdenDAO ordenDAO = null;
	TasaCambioDAO tasasDao = null;
	DataSource _dso;
	int idUsuario;
	static final Logger logger = Logger.getLogger(LecturaIntervencion.class);

	public LecturaIntervencion(DataSource datasource, String usuarioGenerico) throws Exception {
		super();
		_dso = datasource;
	}

	public void run() {
		try {

			obetenerProceso();

			if (getProceso() != null) {
				
				
				
				
				
				
//				Configuracion config = new Configuracion(_dso);
//				config.Escribir();
//
//				if (config.Escribir()) {
//					logger.info("envio ftp");
//					proceso.agregarDescripcionError("Envio de archivo Satisfactorio. ");
//				} else {
//					proceso.agregarDescripcionError("Envio fallido");
//				}

			} else {
				logger.info("Lectura Intervencion : Ya existe un proceso en ejecucion o ha ocurrido un error al intentar registrar el proceso");
				System.out.println("Lectura Intervencion : Ya existe un proceso en ejecucion o ha ocurrido un error al intentar registrar el proceso");
			}
		} catch (Exception e) {
			logger.error("LecturaIntervencion : run() " + e);
			System.out.println("LecturaIntervencion : run() " + e);

			if (proceso != null) {
				proceso.agregarDescripcionErrorTrunc(e.getMessage(), true);
			}

		} finally {
			finalizarProceso();
			logger.info("Terminado el proceso de envio inventario intervencion... ");
			System.out.println("Terminado el proceso de envio inventario intervencion... ");
		}
	}

	/**
	 * metodo para crear un ciclo con fecha y usuario ejecutando el proceso.
	 * 
	 * @throws Exception
	 */
	protected Proceso obetenerProceso() {

		procesosDAO = new ProcesosDAO(_dso);
		proceso = new Proceso();
		proceso.setFechaInicio(new Date());
		proceso.setFechaValor(new Date());
		proceso.setTransaId(TransaccionNegocio.LECTURA_INTERVENCION);
		proceso.setUsuarioId(this.idUsuario);

		try {
			int secuenciaProcesos = Integer.parseInt(OrdenDAO.dbGetSequence(_dso, com.bdv.infi.logic.interfaces.ConstantesGenerales.SECUENCIA_PROCESOS));
			proceso.setEjecucionId(secuenciaProcesos);
			String queryProceso = procesosDAO.insertar(proceso);
			db.exec(_dso, queryProceso);

		} catch (Exception e) {
			logger.error("LecturaIntervencion : obetenerProceso()" + e);
			System.out.println("LecturaIntervencion : obetenerProceso()" + e);

		}

		return proceso;
	}

	/**
	 * metodo para finalizar los proceso y asignar la fecha de cierre
	 * 
	 * @throws Exception
	 */
	private void finalizarProceso() {
		try {
			String queryProcesoCerrar = procesosDAO.modificar(proceso);
			db.exec(_dso, queryProcesoCerrar);
			logger.info("FIN DE PROCESO: " + new Date());

		} catch (Exception e) {
			logger.error("LecturaIntervencion : finalizarProceso()" + e);
			System.out.println("LecturaIntervencion : finalizarProceso()" + e);

		}
	}

	public Proceso getProceso() {
		return proceso;
	}

}

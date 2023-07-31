package com.bdv.infi.logic.interfaz_conciliacion_MENUDEO_BCV;

import java.util.Date;
import javax.sql.DataSource;
import megasoft.db;
import org.apache.log4j.Logger;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.model.conciliacion.Conciliacion;

public class ConciliacionMenudeo implements Runnable {
	
	private String fecha = "";
	private String idOperacion = null;
	private int indicador;
	private ProcesosDAO procesosDAO;
	private Proceso proceso;
	OrdenDAO ordenDAO = null;
	DataSource _dso;
	int idUsuario;
	static final Logger logger = Logger.getLogger(ConciliacionMenudeo.class);

	public ConciliacionMenudeo(DataSource _dso, int idUsuario, String fecha, int indicador) {
		this._dso = _dso;
		this.idUsuario = idUsuario;
		this.fecha = fecha;
		this.indicador = indicador;
	}

	public ConciliacionMenudeo(DataSource datasource, String usuarioGenerico, String fecha, String idOperacion, int indicador) throws Exception {
		super();
		_dso = datasource;
		this.fecha = fecha;
		this.idOperacion = idOperacion;
		this.indicador = indicador;

	}

	public void run() {

		try {

			if (iniciarProceso() != null) {
				Conciliacion ccn = new Conciliacion();
				if (indicador == 1) {
					ccn.CargarDiferencia();
				} else {
					ccn.Fecha = fecha;
					ccn.Conciliar();
				}

				System.out.println("fecha : " + fecha);

				proceso.agregarDescripcionErrorTrunc("se ha anulado las operaciones ", true);

			} else {
				logger.warn("ConciliacionMenudeo : run() : Ya existe un proceso en ejecucion");
				System.out.println("ConciliacionMenudeo : run() : Ya existe un proceso en ejecucion");

			}

		} catch (Exception e) {
			logger.error("ConciliacionMenudeo: run()", e);

			if (proceso != null) {
				proceso.agregarDescripcionErrorTrunc(e.getMessage(), true);
			}

		} finally {
			finalizarProceso();
			logger.info("**Terminado el proceso de Conciliacion Menudeo**");
			System.out.println("**Terminado el proceso de Conciliacion**");
			
		}
	}

	/**
	 * metodo para crear un ciclo con fecha y usuario ejecutando el proceso.
	 * 
	 * @throws Exception
	 */
	protected Proceso iniciarProceso() {
		try {

			logger.info("INICIO DE PROCESO");
			procesosDAO = new ProcesosDAO(_dso);
			proceso = new Proceso();
			int secuenciaProcesos = Integer.parseInt(OrdenDAO.dbGetSequence(_dso, com.bdv.infi.logic.interfaces.ConstantesGenerales.SECUENCIA_PROCESOS));
			proceso.setEjecucionId(secuenciaProcesos);
			proceso.setFechaInicio(new Date());
			proceso.setFechaValor(new Date());
			proceso.setTransaId(TransaccionNegocio.CONCILIACION_MENUDEO);
			// proceso.setTransaId(tipoTransaccion);
			proceso.setUsuarioId(this.idUsuario);
			String queryProceso = procesosDAO.insertar(proceso);
			db.exec(_dso, queryProceso);

		} catch (Exception e) {
			logger.info("ConciliacionMenudeo : iniciarProceso() " + e);
			System.out.println("ConciliacionMenudeo : iniciarProceso() " + e);

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
			logger.info("ConciliacionMenudeo : finalizarProceso() " + e);
			System.out.println("ConciliacionMenudeo : finalizarProceso() " + e);

		}
	}
}

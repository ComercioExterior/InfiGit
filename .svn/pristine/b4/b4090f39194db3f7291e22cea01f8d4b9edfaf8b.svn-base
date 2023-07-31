package com.bdv.infi.logic.interfaz_tasas_BCV;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import javax.sql.DataSource;
import megasoft.db;
import org.apache.log4j.Logger;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.TasaCambioDAO;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaz_ops.BatchOps;
import com.bdv.infi.model.menudeo.Monedas;
import com.bdv.infi.model.menudeo.TasaCambio;

public class ConsultaTasasBCV extends BatchOps implements Runnable {

	private ProcesosDAO procesosDAO;
	private Proceso proceso;
	List<Monedas> listaTasas;
	OrdenDAO ordenDAO = null;
	TasaCambioDAO tasasDao = null;
	DataSource _dso;
	String tipoTransaccion = TransaccionNegocio.WS_BCV_TASA;
	int idUsuario;
	static final Logger logger = Logger.getLogger(ConsultaTasasBCV.class);

	public ConsultaTasasBCV(DataSource datasource, String usuarioGenerico) throws Exception {
		super();
		_dso = datasource;
	}

	public void run() {
		logger.info("ConsultaTasasBCV : run() Inciando proceso actualizacion de tasas");
		try {
			if (obetenerProceso() != null) {
				TasaCambio tsc = new TasaCambio();
				Method Fn = TasaCambio.class.getMethod("lecturaBcv");
				tsc.procesar(tsc, Fn);
				this.listaTasas = tsc.ListarMoneda();
				this.tasasDao = new TasaCambioDAO(_dso);
				if (tasasDao.Cantidad() == 0) {
					guardarTasas();
				} else {
					tasasDao.Upsert(this.listaTasas);
				}
			} else {
				logger.info("BusquedaTasaBCV-> Ya existe un proceso en ejecucion o ha ocurrido un error al intentar registrar el proceso");
			}
		} catch (Exception ex) {
			logger.error("Error en el proceso de envio menudeo a bcv. ", ex);
			if (proceso != null) {
				proceso.agregarDescripcionErrorTrunc(ex.getMessage(), true);
			}
		} finally {
			finalizarProceso();
			logger.info("Terminado el proceso de envio BusquedaTasaBCV... ");
			System.out.println("Terminado el proceso de envio BusquedaTasaBCV... ");
		}
	}

	/**
	 * metodo para crear un ciclo con fecha y usuario ejecutando el proceso.
	 * 
	 * @throws Exception
	 */
	protected Proceso obetenerProceso() throws Exception {
		logger.info("INICIO DE PROCESO");
		procesosDAO = new ProcesosDAO(_dso);
		proceso = new Proceso();
		int secuenciaProcesos = Integer.parseInt(OrdenDAO.dbGetSequence(_dso, com.bdv.infi.logic.interfaces.ConstantesGenerales.SECUENCIA_PROCESOS));
		proceso.setEjecucionId(secuenciaProcesos);
		proceso.setFechaInicio(new Date());
		proceso.setFechaValor(new Date());
		proceso.setTransaId(tipoTransaccion);
		proceso.setUsuarioId(this.idUsuario);
		String queryProceso = procesosDAO.insertar(proceso);
		db.exec(_dso, queryProceso);
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
			e.printStackTrace();
		}

	}

	/**
	 * metodo para guardar las tasas en base de datos
	 */
	public void guardarTasas() {

		this.tasasDao = new TasaCambioDAO(_dso);
		try {
			System.out.println("Cantidad Monedas : " + this.listaTasas.size());
			tasasDao.insertarTasaCambio(this.listaTasas);
			proceso.agregarDescripcionErrorTrunc("se ha guardado la tasa de manera exitosa ", true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("Ha ocurrido un error al momento de guardar la tasa" + "." + e.toString());
			proceso.agregarDescripcionErrorTrunc("fallo al momento de guardar la tasa", true);
		}

	}

	public static void main(String[] args) {
		String a = "999902102017000000500000000250000000000125000000000002000000001000000000000500000003";
		String b = a.substring(0, 82);
		System.out.println(" Linea " + b);
	}

	@Override
	protected boolean verificarCiclo(String ciclo, String status) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}

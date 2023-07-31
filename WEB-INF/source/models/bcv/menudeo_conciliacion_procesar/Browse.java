package models.bcv.menudeo_conciliacion_procesar;


import java.text.SimpleDateFormat;
import java.util.Date;
import megasoft.Logger;
import megasoft.db;
import models.msc_utilitys.*;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.model.conciliacion.Conciliacion;

public class Browse extends MSCModelExtend {

	private ProcesosDAO procesosDAO;
	private Proceso proceso;
	private int secuenciaProcesos = 0;
	String fecha = "";

	public void execute() throws Exception {
		Date fechaSistema = new Date();
		SimpleDateFormat formato = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
		this.fecha = formato.format(fechaSistema);

		iniciarProceso();
		if (iniciarProceso() != null) {
			Conciliacion ccn = new Conciliacion();
			ccn.CargarDiferencia();
			proceso.agregarDescripcionErrorTrunc("Conciliacion Ejectada con exito", true);
			finalizarProceso();

		} else {
			Logger.warning(this, "ConciliacionMenudeo : run() : Ya existe un proceso en ejecucion");
			System.out.println("ConciliacionMenudeo : run() : Ya existe un proceso en ejecucion");
		}
	}

	/**
	 * metodo para crear un ciclo con fecha y usuario ejecutando el proceso.
	 * 
	 * @throws Exception
	 */
	protected Proceso iniciarProceso() {

		Logger.info(this, "INICIO DE PROCESO");
		procesosDAO = new ProcesosDAO(_dso);
		proceso = new Proceso();
		proceso.setFechaInicio(new Date());
		proceso.setFechaValor(new Date());
		proceso.setTransaId(TransaccionNegocio.CONCILIACION_MENUDEO);
		proceso.setUsuarioId(1);

		try {
			secuenciaProcesos = Integer.parseInt(OrdenDAO.dbGetSequence(_dso, com.bdv.infi.logic.interfaces.ConstantesGenerales.SECUENCIA_PROCESOS));
			proceso.setEjecucionId(secuenciaProcesos);
			String queryProceso = procesosDAO.insertar(proceso);
			db.exec(_dso, queryProceso);

		} catch (Exception e) {
			System.out.println("Browse Conciliacion Procesar : iniciarProceso()");
			Logger.error(this, "Browse Conciliacion Procesar : iniciarProceso()");

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
			Logger.info(this, "FIN DE PROCESO: " + new Date());

		} catch (Exception e) {
			System.out.println("Browse Conciliacion Procesar : finalizarProceso()");
			Logger.error(this, "Browse Conciliacion Procesar : finalizarProceso()");

		}
	}
}
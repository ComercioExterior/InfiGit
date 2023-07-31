package com.bdv.infi.logic.interfaz_operaciones_INTERVENCION;

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
import com.bdv.infi.model.inventariodivisas.Configuracion;
import com.bdv.infi.model.menudeo.Monedas;

public class InventarioIntervencion implements Runnable {

	private ProcesosDAO procesosDAO;
	private Proceso proceso;
	List<Monedas> listaTasas;
	OrdenDAO ordenDAO = null;
	TasaCambioDAO tasasDao = null;
	DataSource _dso;
	int idUsuario;
	static final Logger logger = Logger.getLogger(InventarioIntervencion.class);

	public InventarioIntervencion(DataSource datasource, String usuarioGenerico) throws Exception {
		super();
		_dso = datasource;
	}

	public void run() {
		try {
			obetenerProceso();
			if (getProceso() != null) {
				Configuracion config = new Configuracion(_dso);
				
				if (config.Escribir()){
					logger.info("envio ftp");
					config.respaldarArchivo();
					proceso.agregarDescripcionError("Envio de archivo Satisfactorio. ");
				}else{
					config.eliminarArchivo();
					proceso.agregarDescripcionError("Envio fallido");
				}
			
			} else {
				logger.info("Inventario Intervencion : Ya existe un proceso en ejecucion o ha ocurrido un error al intentar registrar el proceso");
				System.out.println("Inventario Intervencion : Ya existe un proceso en ejecucion o ha ocurrido un error al intentar registrar el proceso");
			}
		} catch (Exception e) {
			logger.error("InventarioIntervencion : run() " + e);
			System.out.println("InventarioIntervencion : run() " + e);

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
		proceso.setTransaId(TransaccionNegocio.LECTURA_INVENTARIO);
		proceso.setUsuarioId(this.idUsuario);

		try {
			int secuenciaProcesos = Integer.parseInt(OrdenDAO.dbGetSequence(_dso, com.bdv.infi.logic.interfaces.ConstantesGenerales.SECUENCIA_PROCESOS));
			proceso.setEjecucionId(secuenciaProcesos);
			String queryProceso = procesosDAO.insertar(proceso);
			db.exec(_dso, queryProceso);

		} catch (Exception e) {
			logger.error("InventarioIntervencion : obetenerProceso()" + e);
			System.out.println("InventarioIntervencion : obetenerProceso()" + e);

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
			logger.error("InventarioIntervencion : finalizarProceso()" + e);
			System.out.println("InventarioIntervencion : finalizarProceso()" + e);

		}
	}

	public Proceso getProceso() {
		return proceso;
	}

}

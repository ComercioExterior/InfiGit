package models.bcv.mesas_cambios;

import java.util.Date;
import javax.sql.DataSource;
import megasoft.AbstractModel;
import megasoft.Logger;
import megasoft.db;
import com.bdv.infi.config.Propiedades;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.model.mesaCambio.Mesa;

public class EnvioOperaciones extends AbstractModel implements Runnable {
	private ProcesosDAO procesosDAO;
	Propiedades propiedades;
	private Proceso proceso;
	private int secuenciaProcesos = 0;
	long idUnidad;
	int numeroDePagina;
	int pageSize;
	boolean todos;
	String idOrdenes;
	String statusP;
	String statusE;
	String Tipo;
	int idUsuario;
	String fecha;

	public EnvioOperaciones(int numeroDePagina, int pageSize, boolean todos, String idOrdenes, String statusP, DataSource _dso, int idUsuario, String fecha, String statusE, String Tipo) {

		this.numeroDePagina = numeroDePagina;
		this.pageSize = pageSize;
		this.todos = todos;
		this.idOrdenes = idOrdenes;
		this.statusP = statusP;
		this.statusE = statusE;
		this.Tipo = Tipo;
		this._dso = _dso;
		this.idUsuario = idUsuario;
		this.fecha = fecha;
	}

	public EnvioOperaciones(boolean todos, String idOrdenes, String statusP, DataSource _dso, int idUsuario, String fecha, String statusE, String Tipo) {
		this.todos = todos;
		this.idOrdenes = idOrdenes;
		this.statusP = statusP;
		this.statusE = statusE;
		this.Tipo = Tipo;
		this._dso = _dso;
		this.idUsuario = idUsuario;
		this.fecha = fecha;
	}

	public void run() {
		// INCIAR PROCESO
		try {
			Logger.info(this,"EnvioOperaciones : **Comienzo de envio de operaciones Mesa de Cambio**");
			
			if(verificarCiclo(TransaccionNegocio.WS_BCV_MESAS)){
				iniciarProceso();
				Mesa procesoMesa = new Mesa(_dso);
				
				switch (procesoMesa.Iniciar(Tipo,fecha,statusE,statusP,idOrdenes)) {
				case 0:
					proceso.agregarDescripcionErrorTrunc("No hay operaciones por notificar.", true);
					break;
				case 1:
					proceso.agregarDescripcionErrorTrunc("Todas las operaciones fueron enviadas.", true);
					break;
				case 2:
					proceso.agregarDescripcionErrorTrunc("Verificar operaciones. ", true);
					break;
				}
				
			}else{
				Logger.info(this,"EnvioOperaciones : **Ya se encuentra registrado un ciclo en ejecución**");
				System.out.println("EnvioOperaciones : **Ya se encuentra registrado un ciclo en ejecución**");
			}
			
		} catch (Exception e) {
			Logger.error(this, "Ha ocurrido un error en el proceso de envio de orden al BCV " + e.toString());
			proceso.agregarDescripcionErrorTrunc("Error al enviar las operaciones", true);
			 e.printStackTrace();
		}finally{
			finalizarProceso();
		}
	}

	/**
	 * metodo para crear un ciclo con fecha y usuario ejecutando el proceso.
	 * 
	 * @throws Exception
	 */
	protected void iniciarProceso() throws Exception {
		Logger.info(this, "INICIO DE PROCESO");
		procesosDAO = new ProcesosDAO(_dso);
		proceso = new Proceso();
		secuenciaProcesos = Integer.parseInt(OrdenDAO.dbGetSequence(_dso, com.bdv.infi.logic.interfaces.ConstantesGenerales.SECUENCIA_PROCESOS));
		proceso.setEjecucionId(secuenciaProcesos);
		proceso.setFechaInicio(new Date());
		proceso.setFechaValor(new Date());
		proceso.setTransaId(TransaccionNegocio.WS_BCV_MESAS);
		proceso.setUsuarioId(this.idUsuario);

		String queryProceso = procesosDAO.insertar(proceso);
		db.exec(_dso, queryProceso);
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
			Logger.info(this, "EnvioOperaciones : finalizarProceso()");
		}

	}

	public boolean verificarCiclo(String ciclos) throws Exception {
		boolean valido = true;
		ProcesosDAO procesosDAO = new ProcesosDAO(_dso);

		procesosDAO.listarPorTransaccionActiva(ciclos);
		if (procesosDAO.getDataSet().count() > 0) {

			valido = false;
		}

		return valido;
	}

	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub

	}

}

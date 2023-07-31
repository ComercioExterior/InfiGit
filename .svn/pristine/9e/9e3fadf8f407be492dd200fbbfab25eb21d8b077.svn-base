package models.bcv.mesas_cambios_inter;

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
import com.bdv.infi.model.mesaCambio.MesaIntercambio;
import com.bdv.infi.model.mesaCambio.Pactos;

public class EnvioOperacionesInterbancaria extends AbstractModel implements Runnable {
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
	String idOperacion;
	String jornada;
	String monto;
	String montoBase;
	String contravalor;
	String tasa;
	String tipoPacto;
	String codDemand;
	String codOferta;

	public EnvioOperacionesInterbancaria(String id, String jornada, String monto, String montoBase, String contravalor, String tasa, String tipoPacto, String codDemand, String codOferta, DataSource _dso) {

		this.idOperacion = id;
		this.jornada = jornada;
		this.monto = monto;
		this.montoBase = montoBase;
		this.contravalor = contravalor;
		this.tasa = tasa;
		this.tipoPacto = tipoPacto;
		this.codDemand = codDemand;
		this.codOferta = codOferta;
		this._dso = _dso;
	}

	public void run() {
		// INCIAR PROCESO
		try {
			Logger.info(this,"EnvioOperaciones Interbancarias pacto : **Comienzo de envio de operaciones Mesa de Cambio**");
			System.out.println("EnvioOperaciones Interbancarias pacto : **Comienzo de envio de operaciones Mesa de Cambio**");
			if(verificarCiclo(TransaccionNegocio.WS_BCV_PMESAS)){
				iniciarProceso();
				Pactos procesarPacto = new Pactos(_dso);
				
				switch (procesarPacto.Iniciar(idOperacion,jornada,monto,montoBase,
						contravalor,tasa,tipoPacto,codDemand,codOferta)) {
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
				Logger.info(this,"EnvioOperaciones mesa entre bancos: **Ya se encuentra registrado un ciclo en ejecución**");
				System.out.println("EnvioOperaciones mesa entre bancos : **Ya se encuentra registrado un ciclo en ejecución**");
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
		proceso.setTransaId(TransaccionNegocio.WS_BCV_PMESAS);
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
		System.out.println("ciclos : " + ciclos);
		procesosDAO.listarPorTransaccionActiva(ciclos);
		System.out.println("ciclos");
		if (procesosDAO.getDataSet().count() > 0) {
			System.out.println("ciclos1");
			valido = false;
		}

		return valido;
	}

	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub

	}

}

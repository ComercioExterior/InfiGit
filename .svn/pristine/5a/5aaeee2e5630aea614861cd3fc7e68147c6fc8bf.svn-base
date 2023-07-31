package models.bcv.intervencion_operacion;

import java.util.Date;
import javax.sql.DataSource;
import org.bcv.serviceINTERVENCION.Exceptione;
import megasoft.AbstractModel;
import megasoft.DataSet;
import megasoft.Logger;
import megasoft.db;
import com.bdv.infi.dao.IntervencionDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.model.intervencion.Interbancaria;
import com.bdv.infi.model.intervencion.Sesion;
import com.google.gson.Gson;

public class EnvioBCVWSIntervencionOperacion extends AbstractModel implements Runnable {
	private ProcesosDAO procesosDAO;
	private Proceso proceso;
	private int secuenciaProcesos = 0;
	int idUsuario;
	DataSet _ordenes = new DataSet();

	String id;
	String fecha;
	String estatus;
	DataSource _dso;
	String usuario;
	String idOrdenes;
	boolean seleccion;

	public EnvioBCVWSIntervencionOperacion(String urlInvocacion, DataSource _dso, String fecha, String estatus, String usuario, String idOrdenes, boolean seleccion) {
		this.fecha = fecha;
		this.estatus = estatus;
		this._dso = _dso;
		this.usuario = usuario;
		this.idOrdenes = idOrdenes;
		this.seleccion = seleccion;
	}

	public void run() {
		// INCIAR PROCESO
		try {
			iniciarProceso();
			enviarOperacionesBCV();
			finalizarProceso();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void finalizarProceso() throws Exception {

		String queryProcesoCerrar = procesosDAO.modificar(proceso);
		db.exec(_dso, queryProcesoCerrar);
		Logger.info(this, "FIN DE PROCESO: " + new Date());
	}

	private void enviarOperacionesBCV() {

		try {
			Logger.info(this, "SE INICIA EL HILO PARA ENVIO DE OPERACIONES AL BCV");
			IntervencionDAO intervencionDao = new IntervencionDAO(_dso);
			intervencionDao.listarInterbancarioSinPaginador(fecha, estatus, idOrdenes, seleccion);
			_ordenes = intervencionDao.getDataSet();
			Gson gson = new Gson();
			Exceptione exception = null;

			while (_ordenes.next()) {
				try {
					id = _ordenes.getValue("ID");
					String operaciones = _ordenes.getValue("OPERACION");
					String codigoBanco = _ordenes.getValue("CODIGO_BANCO");
					String codigoDivisas = _ordenes.getValue("CODIGO_MONEDA");
					String montoDivisas = _ordenes.getValue("MONTO");
					String fecha = _ordenes.getValue("FECHA");
					String jornada = _ordenes.getValue("JORNADA");

					String fechaValor1 = fecha.replaceAll("-", "/");
					Sesion login = new Sesion();
					Interbancaria bancariaa = new Interbancaria();
					String token = login.ObtenerToken(ConstantesGenerales.INICIO_SESSION);
					String notificacionBCV = bancariaa.postBancaria(ConstantesGenerales.OPERACION_ENTRE_BANCOS, token, operaciones, codigoBanco, codigoDivisas, Integer.valueOf(jornada), fechaValor1, montoDivisas);

					if (notificacionBCV.contains("{")) {
						exception = gson.fromJson(notificacionBCV.toString(), Exceptione.class);
						intervencionDao.modificarInterbancario(id, usuario, "4", exception.getErrorMessageKey(), "");
						proceso.agregarDescripcionErrorTrunc("Ha ocurrido el siguiente error : " + exception.getErrorMessageKey(), true);
					} else {
						intervencionDao.modificarInterbancario(id, usuario, "1", "", notificacionBCV);
						proceso.agregarDescripcionErrorTrunc("la operacion fue ejecutada con exito, codigo : " + notificacionBCV, true);
					}

				} catch (Exception e) {
					System.out.println("error al notificar operacion interbancario : " + e.toString());
					intervencionDao.modificarInterbancario(id, usuario, "4", "Error no controlado ", "");
					proceso.agregarDescripcionErrorTrunc("Ha ocurrido un error al momento de enviar, revise los datos : " + e.getLocalizedMessage(), true);
					Logger.error(this, "Ha ocurrido un error al momento de enviar, revise los datos" + e.toString());
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			Logger.error(this, "Ha ocurrido un error en el proceso de envio de orden al BCV " + e.toString());
		} finally {
			Logger.info(this, "FINALIZA EL HILO PARA ENVIO DE OPERACIONES AL BCV");
		}
	}

	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub

	}

	protected void iniciarProceso() throws Exception {
		Logger.info(this, "INICIO DE PROCESO");
		procesosDAO = new ProcesosDAO(_dso);
		proceso = new Proceso();
		secuenciaProcesos = Integer.parseInt(OrdenDAO.dbGetSequence(_dso, com.bdv.infi.logic.interfaces.ConstantesGenerales.SECUENCIA_PROCESOS));
		proceso.setEjecucionId(secuenciaProcesos);
		proceso.setFechaInicio(new Date());
		proceso.setFechaValor(new Date());
		proceso.setTransaId(TransaccionNegocio.INTERVENCION_INTERBAN);
		proceso.setUsuarioId(this.idUsuario);
		String queryProceso = procesosDAO.insertar(proceso);
		db.exec(_dso, queryProceso);
	}
}

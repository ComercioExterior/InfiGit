package models.liquidacion.proceso_blotter;

import java.net.InetAddress;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.dao.UsuarioSeguridadDAO;
import com.bdv.infi.logic.LiquidacionUnidadInversion;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.util.Utilitario;

import megasoft.AppProperties;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;

/**
 * Esta clase se encarga de buscar las unidades de inversion que se encuentren adjudicadas.<br>
 * Verifica con la hora de primer intento de liquidación de la unidad, para intentar ser liquidada.<br>
 * Solo se permite ejecutar un proceso de liquidacion a la vez. Esta clase la ejecuta <b>QuartzGenerarLiquidacion.<b>
 * 
 * @author elaucho
 */
public class LiquidacionScheduler extends MSCModelExtend {

	private Logger logger = Logger.getLogger(LiquidacionScheduler.class);

	@Override
	public void execute() throws Exception {

		// Se busca el datasource
		this._dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));
		DataSource dataSourceSeguridad = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_SEGURIDAD_SEPA));

		// Obtener direccion ip de la maquina donde corre la aplicacion (Servidor)
		InetAddress direccion = InetAddress.getLocalHost();
		String direccionIpstr = direccion.getHostAddress();
		String ipTerminal = direccionIpstr;
		String nombreUsuario = ParametrosDAO.listarParametros(ParametrosSistema.USUARIO_WEB_SERVICES, _dso);

		// DAO a utilizar
		UsuarioSeguridadDAO usuarioSeguridadDAO = new UsuarioSeguridadDAO(dataSourceSeguridad);

		// Validamos que el NM exista en sepa y este asociado a INFI
		if (!usuarioSeguridadDAO.esUsuarioValidoSepa(nombreUsuario)) {
			throw new Exception(ConstantesGenerales.MENSAJE + "[" + nombreUsuario + "]");
		}

		// ID DEL USUARIO
		int usuario = usuarioSeguridadDAO.listarId(nombreUsuario);

		// Se buscan las unidades de inversion
		UnidadInversionDAO unidadInversionDAO = new UnidadInversionDAO(_dso);
		unidadInversionDAO.listarUnidadesParaLiquidar();

		LiquidacionUnidadInversion liquidacionUnidadInversion = new LiquidacionUnidadInversion(_dso, usuario, _app, ipTerminal, nombreUsuario, "Proceso Automatico","",0);
		ProcesosDAO procesosDAO = new ProcesosDAO(_dso);

		if (unidadInversionDAO.getDataSet().count() > 0) {

			while (unidadInversionDAO.getDataSet().next()) {
				// Verifica si la liquidación es en línea
				if (unidadInversionDAO.getDataSet().getValue("IN_COBRO_BATCH_LIQ").equals("0")) {
					Calendar calendario = new GregorianCalendar();
					int hora, minutos;
					hora = calendario.get(Calendar.HOUR_OF_DAY);
					minutos = calendario.get(Calendar.MINUTE);
					if (Integer.parseInt(unidadInversionDAO.getDataSet().getValue("hora")) <= hora) {
						if (Integer.parseInt(unidadInversionDAO.getDataSet().getValue("minuto")) <= minutos) {
							procesosDAO.listarPorTransaccionActiva(TransaccionNegocio.LIQUIDACION);
							if (procesosDAO.getDataSet().count() == 0) {
								try {
									logger.info("Iniciando la liquidación en línea de la unidad de inversión " + unidadInversionDAO.getDataSet().getValue("UNDINV_ID"));
									liquidacionUnidadInversion.liquidarUnidadInversion(Long.parseLong(unidadInversionDAO.getDataSet().getValue("UNDINV_ID")));
								} catch (Exception e) {
									logger.error(e.getMessage(),e);
								} catch (Throwable e) {
									logger.error(e.getMessage(),e);
								}// fin catch
							}// fin if
						}// fin if
					}// fin if
				}
			}// fin while
		}// fin if
	}// fin execute
}// fin clase

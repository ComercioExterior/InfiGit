package models.bcv.intervencion_operacion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.bcv.serviceINTERVENCION.IntervencionActiva;
import org.bcv.serviceINTERVENCION.TipoCambioIntervencion;
import megasoft.DataSet;
import megasoft.Logger;
import models.msc_utilitys.*;
import com.bdv.infi.dao.CredencialesDAO;
import com.bdv.infi.dao.IntervencionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.model.intervencion.IntervencionActivas;
import com.bdv.infi.model.intervencion.Sesion;

public class Browse extends MSCModelExtend {

	private DataSet datosFilter = new DataSet();
	CredencialesDAO credencialesDAO = null;
	DataSet _credenciales = new DataSet();
	String codBcv1 = "";
	String tasaCambio1 = "";
	String saldoDisponible1 = "";
	IntervencionDAO intervencionDao;
	String saldoDisponible = "";
	String codBcv = "";
	String tipoIntervencion = "";
	String tasaCambioo = "";
	String moneda = "";
	public void execute() throws Exception {

		String fecha = null;
		String codBcv = null;
		String tasaCambio = null;
		String saldoDisponible = null;
		fecha = (String) _record.getValue("fecha");
		iniciar_datos_sql();
		datosFilter.addNew();
		datosFilter.setValue("fecha", fecha);
		consultarJornada();
		Calendar fecha2 = Calendar.getInstance();
		int año = fecha2.get(Calendar.YEAR);
		int mes = fecha2.get(Calendar.MONTH);
		int dia = fecha2.get(Calendar.DAY_OF_MONTH);
		String pruebafecha = dia + "/" + (mes + 1) + "/" + año;

		Date fechahoy = ConstantesGenerales.dateFormatter.parse(ConstantesGenerales.dateFormatter.format(new Date()));

		try {
			if (codBcv.equals(null)) {
				codBcv = "No tienes Intervencion Activa";
			}
			this.intervencionDao = new IntervencionDAO(_dso);
			intervencionDao.listarMonedas();
			storeDataSet("moneda", intervencionDao.getDataSet());

			datosFilter.setValue("codbcv", String.valueOf(codBcv));
			datosFilter.setValue("tasacambio", String.valueOf(tasaCambio));
			datosFilter.setValue("saldodisponible", String.valueOf(saldoDisponible));
			datosFilter.setValue("monedaaa", String.valueOf(moneda));
			storeDataSet("datosFilter", datosFilter);
		} catch (Exception e) {
			datosFilter.setValue("codbcv", String.valueOf("Intervencion no activa"));
			datosFilter.setValue("tasacambio", String.valueOf("0"));
			datosFilter.setValue("saldodisponible", String.valueOf("0"));
			datosFilter.setValue("monedaaa", String.valueOf("840"));
			storeDataSet("datosFilter", datosFilter);
			this.intervencionDao = new IntervencionDAO(_dso);
			intervencionDao.listarMonedas();
			storeDataSet("moneda", intervencionDao.getDataSet());
			System.out.println("Ha ocurrido un error al momento de enviar, revise los datos " + e.getLocalizedMessage());
			Logger.error(this, "Ha ocurrido un error al momento de enviar, revise los datos " + e.getLocalizedMessage());
		}

	}

	public String[] campos_datos() {
		String Campos[] = { "fecha", "codbcv", "tasacambio", "saldodisponible", "jornada", "tipo", "saldo", "tasacambioo", "moneda", "monedaaa" };
		return Campos;
	}

	public void iniciar_datos_sql() {
		String Campos[] = campos_datos();
		int cant = Campos.length;
		for (int i = 0; i < cant; i++) {
			datosFilter.append(Campos[i], java.sql.Types.VARCHAR);
		}
	}
	
	private void consultarJornada() {

		try {
//
			Sesion login = new Sesion();
			String token = login.ObtenerToken(ConstantesGenerales.INICIO_SESSION);
			IntervencionActivas ac = new IntervencionActivas();
			IntervencionActiva[] lcs = ac.Jornadas("", token);

			List<String> codigoBcv = new ArrayList<String>();
			List<String> saldoDisponibleList = new ArrayList<String>();
			List<String> tipoIntervencionList = new ArrayList<String>();
			List<String> tasaCambioList = new ArrayList<String>();
			List<String> monedaa = new ArrayList<String>();

//			 codBcv = String.valueOf("212451");
//			 saldoDisponible = String.valueOf("10000");
//			 tipoIntervencion = String.valueOf("ORDINARIO");
//			https://marketplace.eclipse.org/marketplace-client-intro?mpc_install=5573556
//			 codigoBcv.add(codBcv);
//			 saldoDisponibleList.add(saldoDisponible);
//			 tipoIntervencionList.add(tipoIntervencion);
//			
//			 codBcv = String.valueOf("212452");
//			 saldoDisponible = String.valueOf("11000");
//			 tipoIntervencion = String.valueOf("CARTA");
//			
//			 codigoBcv.add(codBcv);
//			 saldoDisponibleList.add(saldoDisponible);
//			 tipoIntervencionList.add(tipoIntervencion);
//			
//			 codBcv = String.valueOf("212453");
//			 saldoDisponible = String.valueOf("12000");
//			 tipoIntervencion = String.valueOf("TARJETA");
//			
//			 codigoBcv.add(codBcv);
//			 saldoDisponibleList.add(saldoDisponible);
//			 tipoIntervencionList.add(tipoIntervencion);

			for (IntervencionActiva intervencionActiva : lcs) {
				//
				codBcv = String.valueOf(intervencionActiva.getCoVentaBCV());
				saldoDisponible = String.valueOf(intervencionActiva.getSaldoDisponible());
				tipoIntervencion = String.valueOf(intervencionActiva.getTipoIntervencion().getNombreTipoIntervencion());
				List<TipoCambioIntervencion> listCambio = intervencionActiva.getTipoCambioIntervencion();
				moneda = String.valueOf(intervencionActiva.getCodigoIsoDivisa().getCodigoIsoDivisa());
				System.out.println("monedaaaaaa : " + moneda);
				for (TipoCambioIntervencion lstcmb: listCambio) {
//					System.out.println("lstcmb : " + lstcmb.getTipoCambio());
					tasaCambioo = String.valueOf(lstcmb.getTipoCambio());
				}
				codigoBcv.add(codBcv);
				saldoDisponibleList.add(saldoDisponible);
				tipoIntervencionList.add(tipoIntervencion);
				tasaCambioList.add(tasaCambioo);
				monedaa.add(moneda);

			}
			System.out.println("lista probar : " + codigoBcv.size());
			datosFilter.setValue("jornada", codigoBcv.toString().replace("[", "").replace("]", ""));
			datosFilter.setValue("saldo", saldoDisponibleList.toString().replace("[", "").replace("]", ""));
			datosFilter.setValue("tipo", tipoIntervencionList.toString().replace("[", "").replace("]", ""));
			datosFilter.setValue("tasacambioo", tasaCambioList.toString().replace("[", "").replace("]", ""));
			datosFilter.setValue("moneda", monedaa.toString().replace("[", "").replace("]", ""));
		

		} catch (Exception e) {
			try {
				datosFilter.setValue("jornada", "0");
				datosFilter.setValue("saldo", "0");
				datosFilter.setValue("tipo", "No existe");
				datosFilter.setValue("tasacambioo", "0");
				System.out.println(" Browse : consultarJornada() : " + e);
				Logger.info(this," Browse : consultarJornada() : " + e);
			} catch (Exception e2) {
			}

		}

	}
}
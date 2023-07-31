package models.bcv.intervencion;

import java.util.ArrayList;
import java.util.List;

import org.bcv.serviceINTERVENCION.IntervencionActiva;

import megasoft.DataSet;
import models.msc_utilitys.*;
import com.bdv.infi.dao.IntervencionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.model.intervencion.IntervencionActivas;
import com.bdv.infi.model.intervencion.Sesion;
import com.enterprisedt.util.debug.Logger;

public class Browse extends MSCModelExtend {

	private Logger logger = Logger.getLogger(Browse.class);

	private DataSet datosFilter = new DataSet();
	private DataSet _intervenciones = new DataSet();
	private DataSet _cantidad = new DataSet();
	private DataSet _jornada = new DataSet();
	private String fecha = "";
	private String estatusEnvio = "";
	private String nacionalidad = "";
	private String moneda = "";
	private String cedulaRif = "";
	IntervencionDAO intervencionDao;
	String saldoDisponible = "";
	String codBcv = "";
	String tipoIntervencion = "";

	public void execute() throws Exception {
		iniciar_datos_sql();
		consultarJornada();
		llenarVariables();
		llenarDataSet();
		operacionesIntervencion();
		publicarInformacion();

	}

	public String[] campos_datos() {
		String Campos[] = { "fecha", "statusE", "boton_procesar", "moneda" , "total", "cantidad_operaciones"};
		return Campos;
	}

	public void iniciar_datos_sql() {
		String Campos[] = campos_datos();
		int cant = Campos.length;
		for (int i = 0; i < cant; i++) {
			datosFilter.append(Campos[i], java.sql.Types.VARCHAR);
		}
	}

	public void llenarVariables() {
		try {
			estatusEnvio = _record.getValue("statusE");
			fecha = (String) _record.getValue("fecha");
			nacionalidad = _record.getValue("nacionalidad");
			moneda = _record.getValue("moneda");
			cedulaRif = _record.getValue("rif");
			if (cedulaRif == null) {
				cedulaRif = "";
			}
			if (nacionalidad == null) {
				nacionalidad = "";
			}
			System.out.println("nacionalidad : " + nacionalidad);
			System.out.println("rif : " + cedulaRif);
			System.out.println("moneda : " + moneda);
		} catch (Exception e) {
			logger.error("Browse : llenarVariables()" + e);
			System.out.println("Browse : llenarVariables()" + e);

		}
	}

	private void llenarDataSet() {
		try {
			datosFilter.addNew();
			datosFilter.setValue("fecha", fecha);
			datosFilter.setValue("statusE", estatusEnvio);
			datosFilter.setValue("moneda", moneda);
			// datosFilter.setValue("boton_procesar", " <button id='btnProcesar' name='btnProcesar' onclick='procesar()'>Procesar</button>&nbsp;");
			if (estatusEnvio.equals(ConstantesGenerales.ENVIO_MENUDEO_FALTANTES)) {
				datosFilter.setValue("boton_procesar", " <button id='btnProcesar' name='btnProcesar' onclick='procesar()'>Procesar</button>&nbsp;");
			} else if (estatusEnvio.equals(ConstantesGenerales.ENVIO_MENUDEO_TODAS) || estatusEnvio.equals(ConstantesGenerales.ENVIO_MENUDEO_MANUAL)) {
				datosFilter.setValue("boton_procesar", " <button id='btnbloquear' name='btnProcesar' onclick='bloquearlotodo()'>Procesar</button>&nbsp;");
			} else {
				datosFilter.setValue("boton_procesar", " <button id='btnProcesar' name='btnProcesar' onclick='procesados()' >Procesar</button>&nbsp;");
			}
		} catch (Exception e) {
			logger.error("Browse : llenarDataSet()" + e);
			System.out.println("Browse : llenarDataSet()" + e);

		}

	}

	private void operacionesIntervencion() {
		try {
			this.intervencionDao = new IntervencionDAO(_dso);
			intervencionDao.listarOrdenesIntervencion(true, getNumeroDePagina(), getPageSize(), fecha, estatusEnvio, nacionalidad, cedulaRif, moneda);
			_intervenciones = intervencionDao.getDataSet();
			intervencionDao.listarTotalesCantidadOrdenesIntervencion( fecha, estatusEnvio, nacionalidad, cedulaRif, moneda);
			_cantidad = intervencionDao.getDataSet();
			_cantidad.next();
			datosFilter.setValue("cantidad_operaciones", String.valueOf(_cantidad.getValue("cant")));
			datosFilter.setValue("total", String.valueOf(_cantidad.getValue("total")));
			System.out.println("cantidad " + _cantidad.getValue("cant"));
			System.out.println("total " + _cantidad.getValue("total"));
			
		} catch (Exception e) {
			logger.error("Browse : operacionesIntervencion()" + e);
			System.out.println("Browse : operacionesIntervencion()" + e);

		}
	}

	private void publicarInformacion() {
		try {
			Integer cantidadOperaciones = _intervenciones.count();
			storeDataSet("datos", intervencionDao.getTotalRegistros());
			storeDataSet("rows", _intervenciones);
			storeDataSet("datosFilter", datosFilter);
			storeDataSet("jornada", _jornada);
			getSeccionPaginacion(intervencionDao.getTotalDeRegistros(), getPageSize(), getNumeroDePagina());

		} catch (Exception e) {
			logger.error("Browse : publicarInformacion()" + e);
			System.out.println("Browse : publicarInformacion()" + e);

		}
	}

	private void consultarJornada() {

		try {

			_jornada.append("jornada", java.sql.Types.VARCHAR);
			_jornada.append("saldo", java.sql.Types.VARCHAR);
			_jornada.append("tipo", java.sql.Types.VARCHAR);

			_jornada.addNew();
			Sesion login = new Sesion();
			String token = login.ObtenerToken(ConstantesGenerales.INICIO_SESSION);
			IntervencionActivas ac = new IntervencionActivas();
			IntervencionActiva[] lcs = ac.Jornadas("", token);

			List<String> codigoBcv = new ArrayList<String>();
			List<String> saldoDisponibleList = new ArrayList<String>();
			List<String> tipoIntervencionList = new ArrayList<String>();

//			 codBcv = String.valueOf("212451");
//			 saldoDisponible = String.valueOf("10000");
//			 tipoIntervencion = String.valueOf("ORDINARIO");
//			
//			 codigoBcv.add(codBcv);
//			 saldoDisponibleList.add(saldoDisponible);
//			 tipoIntervencionList.add(tipoIntervencion);
//			
//			 codBcv = String.valueOf("212452");
//			 saldoDisponible = String.valueOf("10000");
//			 tipoIntervencion = String.valueOf("ORDINARIO");
//			
//			 codigoBcv.add(codBcv);
//			 saldoDisponibleList.add(saldoDisponible);
//			 tipoIntervencionList.add(tipoIntervencion);
//			
//			 codBcv = String.valueOf("212453");
//			 saldoDisponible = String.valueOf("10000");
//			 tipoIntervencion = String.valueOf("ORDINARIO");
//			
//			 codigoBcv.add(codBcv);
//			 saldoDisponibleList.add(saldoDisponible);
//			 tipoIntervencionList.add(tipoIntervencion);

			for (IntervencionActiva intervencionActiva : lcs) {
				//
				codBcv = String.valueOf(intervencionActiva.getCoVentaBCV());
				saldoDisponible = String.valueOf(intervencionActiva.getSaldoDisponible());
				tipoIntervencion = String.valueOf(intervencionActiva.getTipoIntervencion().getNombreTipoIntervencion());

				codigoBcv.add(codBcv);
				saldoDisponibleList.add(saldoDisponible);
				tipoIntervencionList.add(tipoIntervencion);

			}
			System.out.println("lista probar : " + codigoBcv.size());
			_jornada.setValue("jornada", codigoBcv.toString().replace("[", "").replace("]", ""));
			_jornada.setValue("saldo", saldoDisponibleList.toString().replace("[", "").replace("]", ""));
			_jornada.setValue("tipo", tipoIntervencionList.toString().replace("[", "").replace("]", ""));
			// _jornada.setValue("saldodisponibleuno",saldoDisponibleList.toString().replace("[", "").replace("]", ""));
			// _jornada.setValue("tipointerven", tipoIntervencionList.toString().replace("[", "").replace("]", ""));
			//

		} catch (Exception e) {
			try {
				_jornada.setValue("jornada", "0");
				_jornada.setValue("saldo", "0");
				_jornada.setValue("tipo", "No existe");
				System.out.println(" Browse : consultarJornada() : " + e);
				logger.info(" Browse : consultarJornada() : " + e);
			} catch (Exception e2) {
			}

		}

	}
}
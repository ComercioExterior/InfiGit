package models.bcv.intervencion_jornada;

import java.util.ArrayList;
import java.util.List;
import org.bcv.serviceINTERVENCION.IntervencionActiva;
import megasoft.DataSet;
import megasoft.Logger;
import models.msc_utilitys.*;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.model.intervencion.IntervencionActivas;
import com.bdv.infi.model.intervencion.Sesion;

public class Browse extends MSCModelExtend {

	 private DataSet _jornada = new DataSet();

	public void execute() throws Exception {
		String saldoDisponible = "";
		String codBcv = "";
		String fechaInicio = "";
		String fechaFin = "";
		String moneda = "";
		String fechaValor = "";
		String tasaCambio = "";
		String tipoIntervencion = "";
		String numeroIntervencion = "";
	

		try {

			Sesion login = new Sesion();
			String token = login.ObtenerToken(ConstantesGenerales.INICIO_SESSION);
			IntervencionActivas ac = new IntervencionActivas();
			IntervencionActiva[] lcs = ac.Jornadas("", token);
			
			if (lcs != null) {
				iniciar_datos_sql();
			}
			
			List<String> codigoBcv = new ArrayList<String>();
			List<String> tasaCambioList = new ArrayList<String>();
			List<String> saldoDisponibleList = new ArrayList<String>();
			List<String> fechaInicioList = new ArrayList<String>();
			List<String> fechaFinList = new ArrayList<String>();
			List<String> monedaList = new ArrayList<String>();
			List<String> tipoIntervencionList = new ArrayList<String>();
			List<String> fechaValorList = new ArrayList<String>();
			List<String> numeroIntervencionList = new ArrayList<String>();

			for (IntervencionActiva intervencionActiva : lcs) {

				codBcv = String.valueOf(intervencionActiva.getCoVentaBCV());
				tasaCambio = String.valueOf(intervencionActiva.getTipoCambioIntervencion().get(0).getTipoCambio());
				saldoDisponible = String.valueOf(intervencionActiva.getSaldoDisponible());
				fechaInicio = String.valueOf(intervencionActiva.getFechaInicio());
				fechaFin = String.valueOf(intervencionActiva.getFechaFin());
				moneda = String.valueOf(intervencionActiva.getCodigoIsoDivisa().getCodigoIsoDivisa());
				tipoIntervencion = String.valueOf(intervencionActiva.getTipoIntervencion().getNombreTipoIntervencion());
				fechaValor = String.valueOf(intervencionActiva.getTipoCambioIntervencion().get(0).getFechaValor().toString());
				numeroIntervencion = String.valueOf(intervencionActiva.getNumeroIntervencion());
				
				codigoBcv.add(codBcv);
				tasaCambioList.add(tasaCambio);
				saldoDisponibleList.add(saldoDisponible);
				fechaInicioList.add(fechaInicio);
				fechaFinList.add(fechaFin);
				monedaList.add(moneda);
				tipoIntervencionList.add(tipoIntervencion);
				fechaValorList.add(fechaValor);
				numeroIntervencionList.add(numeroIntervencion);

			}
		
			_jornada.setValue("jornada",fechaValorList.toString().replace("[", ""));
			_jornada.setValue("codigo",codigoBcv.toString().replace("[", "").replace("]", ""));
			_jornada.setValue("tasacambiouno",tasaCambioList.toString().replace("[", "").replace("]", ""));
			_jornada.setValue("saldodisponibleuno",saldoDisponibleList.toString().replace("[", "").replace("]", ""));
			_jornada.setValue("fechaini",fechaInicioList.toString().replace("[", "").replace("]", ""));
			_jornada.setValue("fechafinuno",fechaFinList.toString().replace("[", "").replace("]", ""));
			_jornada.setValue("monedda", monedaList.toString().replace("[", "").replace("]", ""));
			_jornada.setValue("tipointerven", tipoIntervencionList.toString().replace("[", "").replace("]", ""));
			_jornada.setValue("fechavaloruno",fechaValorList.toString().replace("[", "").replace("]", ""));
			_jornada.setValue("Prueba",fechaValorList.toString().replace("[", "").replace("]", ""));
			_jornada.setValue("numerointerven",numeroIntervencionList.toString().replace("[", "").replace("]", ""));

			storeDataSet("jornada", _jornada);
			
		} catch (Exception e) {
			storeDataSet("jornada", _jornada);
			System.out.println("Browse : execute() Jornada Intervencion" + e);
			Logger.error(this, "Browse : execute() Jornada Intervencion" + e);
		}

	}

	public String[] campos_datos() {
		 
		String Campos[] = { "jornada", "codigo", "tasacambiouno", "saldodisponibleuno", "fechaini", "fechafinuno", "monedda", "tipointerven","fechavaloruno","prueba","numerointerven" };
		return Campos;
	}

	public void iniciar_datos_sql() {
		String Campos[] = campos_datos();
		int cant = Campos.length;
		for (int i = 0; i < cant; i++) {
			_jornada.append(Campos[i], java.sql.Types.VARCHAR);
		}
		try {
			_jornada.addNew();
		} catch (Exception e) {
			System.out.println("Browse : iniciar_datos_sql()" + e);
			Logger.error(this, "Browse : iniciar_datos_sql()" + e);
		}
	
	}
	public boolean pru () throws Exception {
		return false;
	}
}
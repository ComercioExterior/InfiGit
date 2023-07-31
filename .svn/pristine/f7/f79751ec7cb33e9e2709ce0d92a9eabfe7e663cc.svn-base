package models.bcv.intervencion_jornada;

import org.bcv.serviceINTERVENCION.IntervencionActiva;
import megasoft.Logger;
import models.exportable.ExportableOutputStream;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.model.intervencion.IntervencionActivas;
import com.bdv.infi.model.intervencion.Sesion;

public class ExportarCVS extends ExportableOutputStream {

	public void execute() throws Exception {

		Sesion login = new Sesion();
		String token = login.ObtenerToken(ConstantesGenerales.INICIO_SESSION);

		IntervencionActivas ac = new IntervencionActivas();
		IntervencionActiva[] lcs = ac.Jornadas("", token);

		try {

			registrarInicio("jornada.csv");

			crearCabecera("Tipo de intervencion; Codigo jornada; Fecha inicio; Fecha fin; Fecha valor; Tasa de cambio; Moneda; Saldo disponible");

			escribir("\r\n");
			for (IntervencionActiva intervencionActiva : lcs) {
				escribir(String.valueOf(intervencionActiva.getTipoIntervencion().getNombreTipoIntervencion()) == null ? " ;" : String.valueOf(intervencionActiva.getTipoIntervencion().getNombreTipoIntervencion()) + ";");
				escribir(String.valueOf(intervencionActiva.getCoVentaBCV()) == null ? " ;" : String.valueOf(intervencionActiva.getCoVentaBCV()) + ";");
				escribir(String.valueOf(intervencionActiva.getFechaInicio()) == null ? " ;" : String.valueOf(intervencionActiva.getFechaInicio()) + ";");
				escribir(String.valueOf(intervencionActiva.getFechaFin()) == null ? " ;" : String.valueOf(intervencionActiva.getFechaFin()) + ";");
				escribir(String.valueOf(intervencionActiva.getTipoCambioIntervencion().get(0).getFechaValor().toString()) == null ? " ;" : String.valueOf(intervencionActiva.getTipoCambioIntervencion().get(0).getFechaValor().toString()) + ";");
				escribir(String.valueOf(intervencionActiva.getTipoCambioIntervencion().get(0).getTipoCambio()) == null ? " ;" : String.valueOf(intervencionActiva.getTipoCambioIntervencion().get(0).getTipoCambio()) + ";");
				escribir(String.valueOf(intervencionActiva.getCodigoIsoDivisa().getCodigoIsoDivisa()) == null ? " ;" : String.valueOf(intervencionActiva.getCodigoIsoDivisa().getCodigoIsoDivisa()) + ";");
				escribir(String.valueOf(intervencionActiva.getSaldoDisponible()) == null ? " ;" : String.valueOf(intervencionActiva.getSaldoDisponible()) + ";");
				escribir("\r\n");
			}
			registrarFin();
			obtenerSalida();
		} catch (Exception e) {
			_record.addError("Nombre", "Error en la exportación del Excel" + "Error:" + e.getMessage());
			Logger.error(this, "Error en la exportación del Excel", e);
		}

	}

	protected void crearCabecera(String cabecera) throws Exception {
		escribir(cabecera.toUpperCase());
	}
}
package models.bcv.mesa_cambio_operaciones;

import org.bcv.serviceINTERVENCION.listOperaciones;

import megasoft.Logger;
import models.exportable.ExportableOutputStream;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.model.intervencion.ListarOperaciones;
import com.bdv.infi.model.intervencion.Sesion;

public class ExportarOperCVS extends ExportableOutputStream {

	public void execute() throws Exception {
		Sesion login = new Sesion();
		String token = login.ObtenerToken(ConstantesGenerales.INICIO_SESSION);
		ListarOperaciones ls = new ListarOperaciones();
		listOperaciones lcs = ls.ListaArchivo("", token, _record.getValue("codigo"));

		try {

			registrarInicio("operacionesIntervenvion.csv");
			crearCabecera("Codigo Cliente; Nombre; Fecha; Tipo Operacion; Monto Divisas; Tipo Cambio; Cuenta Divisas; Cuenta Bs; Codigo Iso; Jornada");
			escribir("\r\n");

			for (int i = 0; i < lcs.getOperaciones().size(); i++) {

				escribir(String.valueOf(lcs.getOperaciones().get(i).getCodigoCliente()) == null ? " ;" : String.valueOf(lcs.getOperaciones().get(i).getCodigoCliente()) + ";");
				escribir(String.valueOf(lcs.getOperaciones().get(i).getNombreCliente()) == null ? " ;" : String.valueOf(lcs.getOperaciones().get(i).getNombreCliente()) + ";");
				escribir(String.valueOf(lcs.getOperaciones().get(i).getFechaValor()) == null ? " ;" : String.valueOf(lcs.getOperaciones().get(i).getFechaValor()) + ";");
				escribir(String.valueOf(lcs.getOperaciones().get(i).getCodigoTipoOperacion()) == null ? " ;" : String.valueOf(lcs.getOperaciones().get(i).getCodigoTipoOperacion()) + ";");
				escribir(String.valueOf(lcs.getOperaciones().get(i).getMontoDivisa()) == null ? " ;" : String.valueOf(lcs.getOperaciones().get(i).getMontoDivisa()) + ";");
				escribir(String.valueOf(lcs.getOperaciones().get(i).getTipoCambio()).replace(".", ",") == null ? " ;" : String.valueOf(lcs.getOperaciones().get(i).getTipoCambio()).replace(".", ",") + ";");
				escribir(Long.valueOf(lcs.getOperaciones().get(i).getCodigoCuentaDivisa()) == null ? " ;" : Long.valueOf(lcs.getOperaciones().get(i).getCodigoCuentaDivisa()) + ";");
				escribir(Long.valueOf(lcs.getOperaciones().get(i).getCodigoCuentaBs()) == null ? " ;" : Long.valueOf(lcs.getOperaciones().get(i).getCodigoCuentaBs()) + ";");
				escribir(String.valueOf(lcs.getOperaciones().get(i).getCodigoIsoDivisa().getCodigoIsoDivisa()) == null ? " ;" : String.valueOf(lcs.getOperaciones().get(i).getCodigoIsoDivisa().getCodigoIsoDivisa()) + ";");
				escribir(String.valueOf(lcs.getOperaciones().get(i).getCodigoVentaBCV().getCoVentaBCV()) == null ? " ;" : String.valueOf(lcs.getOperaciones().get(i).getCodigoVentaBCV().getCoVentaBCV()) + ";");

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
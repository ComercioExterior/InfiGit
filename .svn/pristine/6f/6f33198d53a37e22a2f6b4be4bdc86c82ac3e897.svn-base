package models.bcv.mesa_cambio_operaciones;

import org.bcv.serviceINTERVENCION.ConsultaArchivoBcv;
import megasoft.Logger;
import models.exportable.ExportableOutputStream;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.model.intervencion.ListarCodigodArchivo;
import com.bdv.infi.model.intervencion.Sesion;

public class ExportarCVS extends ExportableOutputStream {

	public void execute() throws Exception {

		Sesion login = new Sesion();
		String token = login.ObtenerToken(ConstantesGenerales.INICIO_SESSION);
		ListarCodigodArchivo ls = new ListarCodigodArchivo();
		ConsultaArchivoBcv[] lcs = (ConsultaArchivoBcv[]) ls.ListaArchivo("", token, _record.getValue("fech"));
		

		try {

			registrarInicio("listaArchivos.csv");

			crearCabecera("Codigo Archivo; Fecha; Estatus, Observacion");

			escribir("\r\n");
			for (ConsultaArchivoBcv lst : lcs) {
				escribir(String.valueOf(lst.getNuVenta()) == null ? " ;" : String.valueOf(lst.getNuVenta()) + ";");
				escribir(String.valueOf(lst.getFechaRegistro()) == null ? " ;" : String.valueOf(lst.getFechaRegistro()) + ";");
				escribir(String.valueOf(lst.getEstatusArchivo()) == null ? " ;" : String.valueOf(lst.getEstatusArchivo()) + ";");
				escribir(String.valueOf(lst.getObservacion()) == null ? " ;" : String.valueOf(lst.getObservacion()) + ";");
				
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
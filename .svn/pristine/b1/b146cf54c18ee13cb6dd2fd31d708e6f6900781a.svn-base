package models.bcv.mesa_cambio_demanda;

import java.util.ArrayList;
import java.util.List;

import org.bcv.serviceINTERVENCION.ConsultaArchivoBcv;
import org.bcv.serviceINTERVENCION.IntervencionActiva;
import megasoft.DataSet;
import megasoft.Logger;
import models.msc_utilitys.*;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.model.intervencion.IntervencionActivas;
import com.bdv.infi.model.intervencion.ListarCodigodArchivo;
import com.bdv.infi.model.intervencion.Sesion;

public class Browse2 extends MSCModelExtend {

	private DataSet _jornada = new DataSet();

	public void execute() throws Exception {
		String numeroVenta = "";
		String fechaVenta = "";
		String estatus = "";
		String observacion = "";

		try {
			Sesion login = new Sesion();
			String token = login.ObtenerToken(ConstantesGenerales.INICIO_SESSION);
			ListarCodigodArchivo ls = new ListarCodigodArchivo();
			ConsultaArchivoBcv[] lcs = (ConsultaArchivoBcv[]) ls.ListaArchivo("", token, _record.getValue("fech"));
			if (lcs != null) {
				iniciar_datos_sql();
			}

			List<String> numeroVentaLst = new ArrayList<String>();
			List<String> fechaVentaLst = new ArrayList<String>();
			List<String> estatusLst = new ArrayList<String>();
			List<String> observacionLst = new ArrayList<String>();

			for (ConsultaArchivoBcv lista : lcs) {

				numeroVenta = String.valueOf(lista.getNuVenta());
				fechaVenta = String.valueOf(lista.getFechaRegistro());
				estatus = String.valueOf(lista.getEstatusArchivo());
				observacion = String.valueOf(lista.getObservacion());

				numeroVentaLst.add(numeroVenta);
				fechaVentaLst.add(fechaVenta);
				estatusLst.add(estatus);
				if (observacion == null || observacion.equalsIgnoreCase("null")) {
					observacionLst.add("Sin observaciones");	
				}else{
					observacionLst.add(observacion.replace(",", ""));	
				}
				
			}
			
			_jornada.setValue("codigo", numeroVentaLst.toString().replace("[", "").replace("]", ""));
			_jornada.setValue("fecha", fechaVentaLst.toString().replace("[", "").replace("]", ""));
			_jornada.setValue("estatus", estatusLst.toString().replace("[", "").replace("]", ""));
			_jornada.setValue("observacion", observacionLst.toString().replace("[", "").replace("]", ""));
			_jornada.setValue("fech", _record.getValue("fech"));
			storeDataSet("jornada", _jornada);

		} catch (Exception e) {
			storeDataSet("jornada", _jornada);
			System.out.println("Browse : execute() Jornada Intervencion" + e);
			Logger.error(this, "Browse : execute() Jornada Intervencion" + e);
		}

	}

	public String[] campos_datos() {

		String Campos[] = { "fecha", "codigo", "estatus", "fech", "observacion" };
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

	public boolean pru() throws Exception {
		return false;
	}
}
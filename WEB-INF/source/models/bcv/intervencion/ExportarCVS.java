package models.bcv.intervencion;

import megasoft.DataSet;
import megasoft.Logger;
import models.exportable.ExportableOutputStream;
import com.bdv.infi.dao.IntervencionDAO;


public class ExportarCVS extends ExportableOutputStream {

	String estatusEnvio = "";
	String fecha = "";
	String moneda = "";

	public void execute() throws Exception {
		DataSet _ordenes = new DataSet();
		capturarValorFilter();
		IntervencionDAO intervencionDao = new IntervencionDAO(_dso);
		intervencionDao.listarOrdenesReporte(fecha, estatusEnvio, moneda);
		_ordenes = intervencionDao.getDataSet();
		try {
			registrarInicio("ordenesintervencion.csv");

			crearCabecera("Id INFI; Nacionalidad;Cedula-Rif; Nombre; Fecha; Operacion; Monto; Tasa;Cuenta Bolivares; Cuenta Divisas;  Divisa; Estatus; Telefono; Correo; Jornada; Id Bcv;");
		
			escribir("\r\n");
			while (_ordenes.next()) {
				registroProcesado++;
				escribir(_ordenes.getValue("ID_OPER") == null ? " ;" : _ordenes.getValue("ID_OPER") + ";");
				escribir(_ordenes.getValue("NACIONALIDAD") == null ? " ;" : _ordenes.getValue("NACIONALIDAD") + ";");
				escribir(_ordenes.getValue("NRO_CED_RIF") == null ? " ;" : _ordenes.getValue("NRO_CED_RIF") + ";");
				escribir(_ordenes.getValue("NOM_CLIEN") == null ? " ;" : _ordenes.getValue("NOM_CLIEN") + ";");
				escribir(_ordenes.getValue("FECH_OPER") == null ? " ;" : _ordenes.getValue("FECH_OPER") + ";");
				escribir(_ordenes.getValue("OPERACION") == null ? " ;" : _ordenes.getValue("OPERACION") + ";");
				escribir(_ordenes.getValue("MTO_DIVISAS") == null ? " ;" : _ordenes.getValue("MTO_DIVISAS") + ";");
				escribir(_ordenes.getValue("TASA_CAMBIO") == null ? " ;" : _ordenes.getValue("TASA_CAMBIO") + ";");
				escribir(_ordenes.getValue("CTA_CLIEN") == null ? " ;" : _ordenes.getValue("CTA_CLIEN") + ";");
				escribir(_ordenes.getValue("CTA_CLIEN_DIVISAS") == null ? " ;" : _ordenes.getValue("CTA_CLIEN_DIVISAS") + ";");
				escribir(_ordenes.getValue("COD_DIVISAS") == null ? " ;" : _ordenes.getValue("COD_DIVISAS") + ";");
				escribir(_ordenes.getValue("STATUS_ENVIO") == null ? " ;" : _ordenes.getValue("STATUS_ENVIO") + ";");
				escribir(_ordenes.getValue("TEL_CLIEN") == null ? " ;" : _ordenes.getValue("TEL_CLIEN") + ";");
				escribir(_ordenes.getValue("EMAIL_CLIEN") == null ? " ;" : _ordenes.getValue("EMAIL_CLIEN") + ";");
				escribir(_ordenes.getValue("ID_JORNADA") == null ? " ;" : _ordenes.getValue("ID_JORNADA") + ";");
				escribir(_ordenes.getValue("ID_BCV") == null ? " ;" : _ordenes.getValue("ID_BCV") + ";");

				escribir("\r\n");
			}
			registrarFin();
			obtenerSalida();
		} catch (Exception e) {
			_record.addError("Nombre", "Error en la exportación del Excel" + "Error:" + e.getMessage());
			Logger.error(this, "Error en la exportación del Excel", e);
		}
	}

	private void capturarValorFilter() {

		try {
			estatusEnvio = _record.getValue("statusE");
			moneda = _record.getValue("moneda");
			System.out.println("moneda : " + moneda);
			fecha = (String) _record.getValue("fecha");

		} catch (Exception e) {
			Logger.error(this, "ExportarCVS : capturarValorFilter()" + e);
			System.out.println("ExportarCVS : capturarValorFilter()" + e);

		}
	}

	protected void crearCabecera(String cabecera) throws Exception {
		escribir(cabecera.toUpperCase());
	}
}
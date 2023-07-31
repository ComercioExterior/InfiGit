package models.bcv.intervencion_operacion_consulta;

import megasoft.DataSet;
import megasoft.Logger;
import models.exportable.ExportableOutputStream;
import com.bdv.infi.dao.IntervencionDAO;

public class ExportarCVS extends ExportableOutputStream {

	String estatusEnvio = "";
	String fecha = "";

	public void execute() throws Exception {

		DataSet _ordenes = new DataSet();
		capturarValorFilter();
		IntervencionDAO intervencionDao = new IntervencionDAO(_dso);
		intervencionDao.listarInterbancarioSinPaginador(fecha, estatusEnvio, "", true);
		_ordenes = intervencionDao.getDataSet();
		System.out.println("contador de transacciones : " + _ordenes.count());
		try {
			registrarInicio("ordenes3.xls");
			crearCabecera("ID; JORNADA; BANCO; MONEDA; OPERACION");
			escribir("\r\n");

			while (_ordenes.next()) {
				registroProcesado++;

				escribir(_ordenes.getValue("ID") == null ? " ;" : _ordenes.getValue("ID") + ";");
				escribir(_ordenes.getValue("JORNADA") == null ? " ;" : _ordenes.getValue("JORNADA") + ";");
				escribir(_ordenes.getValue("CODIGO_BANCO") == null ? " ;" : _ordenes.getValue("CODIGO_BANCO") + ";");
				escribir(_ordenes.getValue("CODIGO_MONEDA") == null ? " ;" : _ordenes.getValue("CODIGO_MONEDA") + ";");
				escribir(_ordenes.getValue("OPERACION") == null ? " ;" : _ordenes.getValue("OPERACION") + ";");
				escribir(_ordenes.getValue("MONTO") == null ? " ;" : _ordenes.getValue("MONTO") + ";");
				escribir(_ordenes.getValue("TASA") == null ? " ;" : _ordenes.getValue("TASA") + ";");
				escribir(_ordenes.getValue("FECHA") == null ? " ;" : _ordenes.getValue("FECHA") + ";");
				escribir(_ordenes.getValue("ESTATUS") == null ? " ;" : _ordenes.getValue("ESTATUS") + ";");
				escribir(_ordenes.getValue("USUARIO_REGISTRO") == null ? " ;" : _ordenes.getValue("USUARIO_REGISTRO") + ";");
				escribir(_ordenes.getValue("USUARIO_APROBACION") == null ? " ;" : _ordenes.getValue("USUARIO_APROBACION") + ";");
				escribir(_ordenes.getValue("USUARIO_ENVIO") == null ? " ;" : _ordenes.getValue("USUARIO_ENVIO") + ";");

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
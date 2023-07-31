package models.bcv.intervencion_oficina;

import megasoft.DataSet;
import megasoft.Logger;
import models.exportable.ExportableOutputStream;
import com.bdv.infi.model.inventariodivisas.Oficina;

public class ExportarCVS extends ExportableOutputStream {

	String moneda = "";
	String fecha = "";
	String estado = "";

	Oficina ofi;
	
	public void execute() {
		capturarValorFilter();
		DataSet _oficinas = new DataSet();
		
		this.ofi = new Oficina(_dso);
		ofi.Fecha = fecha;
		ofi.Moneda = this.moneda;
		ofi.Estado = this.estado;
		ofi.Estatus = "";
		ofi.ListarReporte();
		_oficinas = ofi.getDataSet();

		
		try {
			registrarInicio("Inventario.csv");
			crearCabecera("NRO OFICINA;DESCRIPCION;ESTADO;MUNICIPIO; ASIGNADO;PORCENTAJE;MONTO;DISPONIBLE;CONSUMIDO;DIAS ENTREGA;MONEDA");
			escribir("\r\n");
			
			while (_oficinas.next()) {
				registroProcesado++;

				escribir(_oficinas.getValue("NRO") == null ? " ;" : _oficinas.getValue("NRO") + ";");
				escribir(_oficinas.getValue("DESCRIPCION") == null ? " ;" : _oficinas.getValue("DESCRIPCION") + ";");
				escribir(_oficinas.getValue("ESTADO") == null ? " ;" : _oficinas.getValue("ESTADO") + ";");
				escribir(_oficinas.getValue("MUNICIPIO") == null ? " ;" : _oficinas.getValue("MUNICIPIO") + ";");
				escribir(_oficinas.getValue("ASIGNADO") == null ? " ;" : _oficinas.getValue("ASIGNADO") + ";");
				escribir(_oficinas.getValue("PORCENTAJE") == null ? " ;" : _oficinas.getValue("PORCENTAJE") + ";");
				escribir(_oficinas.getValue("MONTO") == null ? " ;" : _oficinas.getValue("MONTO") + ";");
				escribir(_oficinas.getValue("DISPONIBLE") == null ? " ;" : _oficinas.getValue("DISPONIBLE") + ";");
				escribir(_oficinas.getValue("CONSUMIDO") == null ? " ;" : _oficinas.getValue("CONSUMIDO") + ";");
				escribir(_oficinas.getValue("DIASENTREGA") == null ? " ;" : _oficinas.getValue("DIASENTREGA") + ";");
				escribir(_oficinas.getValue("MONEDA") == null ? " ;" : _oficinas.getValue("MONEDA") + ";");
				
				escribir("\r\n");
				
			}
			registrarFin();
			obtenerSalida();
			
		} catch (Exception e) {
			Logger.error(this, "ExportarCVS : execute()" + e);
			
		}

	}

	private void capturarValorFilter() {

		try {
			moneda = _record.getValue("moneda");
			fecha = (String) _record.getValue("fecha");
			estado = _record.getValue("estado");

		} catch (Exception e) {
			Logger.error(this, "ExportarCVS : capturarValorFilter()" + e);
			System.out.println("ExportarCVS : capturarValorFilter()" + e);

		}
	}

	protected void crearCabecera(String cabecera) throws Exception {
		escribir(cabecera.toUpperCase());
	}
}
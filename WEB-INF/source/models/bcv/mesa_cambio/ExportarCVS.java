package models.bcv.mesa_cambio;

import megasoft.DataSet;
import megasoft.Logger;
import models.exportable.ExportableOutputStream;
import com.bdv.infi.dao.MesaCambioDAO;

public class ExportarCVS extends ExportableOutputStream {

	public void execute() throws Exception {
		DataSet _ordenes = new DataSet();
		String statusP = _record.getValue("statusp");
		String statusE = _record.getValue("statuse");
		String Tipo = _record.getValue("tipo");
		String fecha = (String) _record.getValue("fecha");
		String nacionalidad = _record.getValue("nacionalidad");
		String cedulaRif = _record.getValue("rif");
		
		System.out.println("statusp : "+statusP);
		System.out.println("statuse : "+statusE);
		System.out.println("tipo : "+Tipo);
		System.out.println("fecha : "+fecha);
		
		if (cedulaRif == null) {
			cedulaRif = "";
		}
		if (nacionalidad == null) {
			nacionalidad = "";
		}
		System.out.println("cedula : "+cedulaRif);
		System.out.println("nacionalidad : "+nacionalidad);
		System.out.println(" llego exportar : ");
		MesaCambioDAO operaciones = new MesaCambioDAO(_dso);
		System.out.println(" llego exportar :1 ");
		operaciones.ListarExportar( fecha, statusE, Tipo, nacionalidad, cedulaRif,0);
		System.out.println(" llego exportar :2 ");
		_ordenes = operaciones.getDataSet();
		System.out.println(" Contador de ordenes : " + _ordenes.count());
		try {

			registrarInicio("ordenesMesaCambio.csv");

			crearCabecera("ID OPERACION ;ID OC ; TIPO OPERACION ; MONTO DIVISAS;MONTO BOLIVARES;TASA CAMBIO; MONTO COMISION;" + " TIPO OPERACION OFER; CED-RIF OFER; NOMBRE OFER; CUENTA OFER MN; CUENTA OFER ME; TIPO OPERACION DEMA; CED-RIF DEMA;" + " NOMBRE DEMA; CUENTA DEMA MN; CUENTA DEMA ME; FECHA OPER; COD DIVISAS; CONTRA VALOR DIVISAS; CONTRA VALOR BS; TASA BASE"
					+ " ESTATUS ENVIO; FECHA ENVIO; ID BCV; TIPO PACTO; INSTRUMENTO ; OBSERVACION");

			escribir("\r\n");

			while (_ordenes.next()) {
				registroProcesado++;
				escribir(_ordenes.getValue("ID_OPER") == null ? " ;" : _ordenes.getValue("ID_OPER") + ";");
				escribir(_ordenes.getValue("ID_OC") == null ? " ;" : _ordenes.getValue("ID_OC") + ";");
				escribir(_ordenes.getValue("PRODUCTO") == null ? " ;" : _ordenes.getValue("PRODUCTO") + ";");
				escribir(_ordenes.getValue("MTO_DIVISAS") == null ? " ;" : _ordenes.getValue("MTO_DIVISAS") + ";");
				escribir(_ordenes.getValue("MTO_BOLIVARES") == null ? " ;" : _ordenes.getValue("MTO_BOLIVARES") + ";");
				escribir(_ordenes.getValue("TASA_CAMBIO") == null ? " ;" : _ordenes.getValue("TASA_CAMBIO") + ";");
				escribir(_ordenes.getValue("MTO_COMI") == null ? " ;" : _ordenes.getValue("MTO_COMI") + ";");
				escribir(_ordenes.getValue("TIPO_PER_OFER") == null ? " ;" : _ordenes.getValue("TIPO_PER_OFER") + ";");
				escribir(_ordenes.getValue("CED_RIF_OFER") == null ? " ;" : _ordenes.getValue("CED_RIF_OFER") + ";");
				escribir(_ordenes.getValue("NOM_OFER") == null ? " ;" : _ordenes.getValue("NOM_OFER") + ";");
				escribir(_ordenes.getValue("CTA_OFER_MN") == null ? " ;" : _ordenes.getValue("CTA_OFER_MN") + ";");
				escribir(_ordenes.getValue("CTA_OFER_ME") == null ? " ;" : _ordenes.getValue("CTA_OFER_ME") + ";");
				escribir(_ordenes.getValue("TIPO_PER_DEMA") == null ? " ;" : _ordenes.getValue("TIPO_PER_DEMA") + ";");
				escribir(_ordenes.getValue("CED_RIF_DEMA") == null ? " ;" : _ordenes.getValue("CED_RIF_DEMA") + ";");
				escribir(_ordenes.getValue("NOM_DEMA") == null ? " ;" : _ordenes.getValue("NOM_DEMA") + ";");
				escribir(_ordenes.getValue("CTA_DEMA_MN") == null ? " ;" : _ordenes.getValue("CTA_DEMA_MN") + ";");
				escribir(_ordenes.getValue("CTA_DEMA_ME") == null ? " ;" : _ordenes.getValue("CTA_DEMA_ME") + ";");
				escribir(_ordenes.getValue("FECH_OPER") == null ? " ;" : _ordenes.getValue("FECH_OPER") + ";");
				escribir(_ordenes.getValue("COD_DIVISAS") == null ? " ;" : _ordenes.getValue("COD_DIVISAS") + ";");
				escribir(_ordenes.getValue("MTO_PACTOBASE") == null ? " ;" : _ordenes.getValue("MTO_PACTOBASE") + ";");
				escribir(_ordenes.getValue("MTO_CONTRAVALORBASE") == null ? " ;" : _ordenes.getValue("MTO_CONTRAVALORBASE") + ";");
				escribir(_ordenes.getValue("TASA_PACTOBASE") == null ? " ;" : _ordenes.getValue("TASA_PACTOBASE") + ";");
//				escribir(_ordenes.getValue("estadistica") == null ? " ;" : _ordenes.getValue("estadistica") + ";");
//				escribir(_ordenes.getValue("COD_OFI_ORI") == null ? " ;" : _ordenes.getValue("COD_OFI_ORI") + ";");
//				escribir(_ordenes.getValue("EMAIL_CLIEN") == null ? " ;" : _ordenes.getValue("EMAIL_CLIEN") + ";");
//				escribir(_ordenes.getValue("TEL_CLIEN") == null ? " ;" : _ordenes.getValue("TEL_CLIEN") + ";");
				escribir(_ordenes.getValue("Estatus") == null ? " ;" : _ordenes.getValue("Estatus") + ";");
				escribir(_ordenes.getValue("FECHA_ENVIO") == null ? " ;" : _ordenes.getValue("FECHA_ENVIO") + ";");
				
				escribir(_ordenes.getValue("ID_BCV") == null ? " ;" : _ordenes.getValue("ID_BCV") + ";");
				escribir(_ordenes.getValue("TIPO_PACTO") == null ? " ;" : _ordenes.getValue("TIPO_PACTO") + ";");
				escribir(_ordenes.getValue("TIPO_INSTRUM") == null ? " ;" : _ordenes.getValue("TIPO_INSTRUM") + ";");
				escribir(_ordenes.getValue("OBSERVACION") == null ? " ;" : _ordenes.getValue("OBSERVACION") + ";");
			
				escribir("\r\n");
			}
System.out.println("holaaaaaaaaaaaaa");
			registrarFin();
			obtenerSalida();
		} catch (Exception e) {
			_record.addError("Nombre", "Error en la exportación del Excel" + "Error:" + e.getMessage());
			Logger.error(this, "ExportarCVS : execute()", e);
		}

	}// fin execute

	protected void crearCabecera(String cabecera) throws Exception {
		escribir(cabecera.toUpperCase());

	}
}
package models.unidad_inversion.blotters;

import java.util.Calendar;

import megasoft.DataSet;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;

/**
 * Clase que valida la informacion de la relacion de un titulo con una unidad de inversion
 * @author Megasoft Computaci&oacute;n
 */
public class UIBlotterValidar{

	/**
	 * Calendarios para manejo de las fechas : validacion y conversion a Date
	 */	
	private Calendar periodoInicial = Calendar.getInstance();
	private Calendar periodoFinal = Calendar.getInstance();

	/**
	 * Validacion de los datos provenientes de la pagina
	 */
	public boolean isValid(DataSet record) throws Exception {

		boolean flag = true;
		record.next();
		
		boolean flagPD = true, flagPH = true;
		if (record.getValue("periodoInicial") == null || record.getValue("periodoInicial").trim().equals("")) {
			record.addError("Toma de Ordenes Desde","Este campo es requerido.");
			flag = false;
			flagPD = false;
		} else {
			String [] token =  record.getValue("periodoInicial").split(ConstantesGenerales.SEPARADOR_FECHA);
			periodoInicial.set(Calendar.YEAR, Integer.parseInt(token[2]));
			periodoInicial.set(Calendar.MONTH, Integer.parseInt(token[1]));
			periodoInicial.set(Calendar.DATE, Integer.parseInt(token[0]));
		}
		if (record.getValue("periodoFinal") == null || record.getValue("periodoFinal").trim().equals("")) {
			record.addError("Toma de Ordenes Hasta","Este campo es requerido.");
			flag = false;
		} else {	
			String [] token =  record.getValue("periodoFinal").split(ConstantesGenerales.SEPARADOR_FECHA);
			periodoFinal.set(Calendar.YEAR, Integer.parseInt(token[2]));
			periodoFinal.set(Calendar.MONTH, Integer.parseInt(token[1]));
			periodoFinal.set(Calendar.DATE, Integer.parseInt(token[0]));;
		}
		if (flagPD && flagPH) {
			if (periodoFinal.before(periodoInicial) ) {
				record.addError("Toma de Ordenes Hasta","La fecha debe ser mayor a la Toma de Ordenes Desde");
				flag = false;
			}
		}
		
		// Validar el horario :
		//	1.- Instanciar un Calendar con hoy + horarioDesde
		//	2.- Instanciar un Calendar con hoy + horarioHasta
		//	3.- Comparar ambos Calendar para garantizar que el horarioDesde <= horarioHasta
		flagPD = true; 	flagPH = true;

		if (record.getValue("horarioDesde") == null || record.getValue("horarioDesde").trim().equals("")) {
			record.addError("Horario Desde","Este campo es requerido.");
			flag = false;
			flagPD = false;
		} else {			
			try {			
				periodoInicial.set(Calendar.HOUR, new Integer(record.getValue("horarioDesde").substring(0,2)).intValue());
				periodoInicial.set(Calendar.MINUTE, new Integer(record.getValue("horarioDesde").substring(3,5)).intValue());
				if (record.getValue("horarioDesde").indexOf("AM") > -1) {
					periodoInicial.set(Calendar.AM_PM,Calendar.AM);	
				} else {
					periodoInicial.set(Calendar.AM_PM,Calendar.PM);	
				}
			} catch (Exception e) {
				record.addError("Horario Desde","El formato del campo debe ser: hh:mmAM/PM.");
				flag = false;
				flagPD = false;
			}
		}

		if (record.getValue("horarioHasta") == null || record.getValue("horarioHasta").trim().equals("")) {
			record.addError("Horario Desde","Este campo es requerido.");
			flag = false;
			flagPH = false;
		} else {			
			try {			
				periodoFinal.set(Calendar.HOUR, new Integer(record.getValue("horarioHasta").substring(0,2)).intValue());
				periodoFinal.set(Calendar.MINUTE, new Integer(record.getValue("horarioHasta").substring(3,5)).intValue());
				if (record.getValue("horarioHasta").indexOf("AM") > -1) {
					periodoFinal.set(Calendar.AM_PM,Calendar.AM);	
				} else {
					periodoFinal.set(Calendar.AM_PM,Calendar.PM);	
				}
			} catch (Exception e) {
				record.addError("Horario Desde","El formato del campo debe ser: hh:mmAM/PM.");
				flag = false;
				flagPH = false;
			}
		}
		if (flagPD && flagPH) {
			if (periodoFinal.before(periodoInicial) ) {
				record.addError("Horario Hasta","El horario Hasta debe ser mayor al Horario Desde");
				flag = false;
			}
		}
		
		boolean flagPDUD = true, flagPHUD = true;
		if (record.getValue("horarioDesdeUltDia") == null || record.getValue("horarioDesdeUltDia").trim().equals(":")) {
			record.setValue("horarioDesdeUltDia", record.getValue("horarioDesde"));
		} 			
		try {			
			periodoInicial.set(Calendar.HOUR, new Integer(record.getValue("horarioDesdeUltDia").substring(0,2)).intValue());
			periodoInicial.set(Calendar.MINUTE, new Integer(record.getValue("horarioDesdeUltDia").substring(3,5)).intValue());
			if (record.getValue("horarioDesde").indexOf("AM") > -1) {
				//periodoInicial.set(Calendar.AM_PM,Calendar.AM);
				periodoInicial.set(Calendar.HOUR, new Integer(record.getValue("horarioDesdeUltDia").substring(0,2)).intValue()+12);
			} else {
				//periodoInicial.set(Calendar.AM_PM,Calendar.PM);
				periodoInicial.set(Calendar.HOUR, new Integer(record.getValue("horarioDesdeUltDia").substring(0,2)).intValue()+12);
			}
		} catch (Exception e) {
			record.addError("Horario Desde del Ultimo dia","El formato del campo debe ser: hh:mmAM/PM.");
			flag = false;
			flagPDUD = false;
		}

		if (record.getValue("horarioHastaUltDia") == null || record.getValue("horarioHastaUltDia").trim().equals(":")) {
			record.setValue("horarioHastaUltDia", record.getValue("horarioHasta"));
		} 				
		try {			
			periodoFinal.set(Calendar.HOUR, new Integer(record.getValue("horarioHasta").substring(0,2)).intValue());
			periodoFinal.set(Calendar.MINUTE, new Integer(record.getValue("horarioHasta").substring(3,5)).intValue());
			if (record.getValue("horarioHasta").indexOf("AM") > -1) {
				//periodoFinal.set(Calendar.AM_PM,Calendar.AM);
				periodoFinal.set(Calendar.HOUR, new Integer(record.getValue("horarioHasta").substring(0,2)).intValue()+12);
			} else {
				//periodoFinal.set(Calendar.AM_PM,Calendar.PM);
				periodoFinal.set(Calendar.HOUR, new Integer(record.getValue("horarioHasta").substring(0,2)).intValue()+12);
			}
		} catch (Exception e) {
			record.addError("Horario Desde del Ultimo Dia","El formato del campo debe ser: hh:mmAM/PM.");
			flag = false;
			flagPHUD = false;
		}
		if (flagPDUD && flagPHUD) {
			if (periodoFinal.before(periodoInicial) ) {
				record.addError("Horario Hasta del Ultimo Dia","Debe ser mayor al Horario Desde del Ultimo Dia");
				flag = false;
			}
		}
		return flag;
	}
}

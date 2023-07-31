package com.bdv.infi_services.utilities;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ParseDate {
	public Date getFecha(String fechaSql, String formato) throws Exception {
		Date dt = null;
		SimpleDateFormat sdf = new SimpleDateFormat(formato);
		String[] fecha = new String[2];
		fecha = fechaSql.split(" ");
		dt = sdf.parse(fecha[0]);
		return dt;
	}

	public String parseFecha(Date fechaSql, String formato) throws Exception {

		String dt = null;
		SimpleDateFormat sdf = new SimpleDateFormat(formato);
		dt = sdf.format(fechaSql);
		return dt;
	}
}

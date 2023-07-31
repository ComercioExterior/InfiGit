package com.bdv.infi.webservices.logger;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.sql.DataSource;

import megasoft.db;

public class LoggerNavegacion{
	private DataSource ds;

	private static LoggerNavegacion INSTANCE = null;
    
    // Private constructor suppresses 
    private LoggerNavegacion(DataSource ds) {
    	this.ds = ds;
    }
 
    // creador sincronizado para protegerse de posibles problemas  multi-hilo
    // otra prueba para evitar instantiaci�n m�ltiple 
    private synchronized static void createInstance(DataSource ds) {
        if (INSTANCE == null) { 
            INSTANCE = new LoggerNavegacion(ds);
        }
    }
 
    public static LoggerNavegacion getInstance(DataSource ds) {
        if (INSTANCE == null) createInstance(ds);
        return INSTANCE;
    }

	public void log(String aplicacion, String usuario, String ip,
			String computerName, String tipoDeActividad, String subtipo,
			String mensaje, String cliente, String cuenta, String fechas,
			String libre) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		StringBuffer sql = new StringBuffer("INSERT INTO LOG_SESION(APLICACION, USUARIO, FECHA, IP, COMPUTER_NAME, " +
				"TIPO_DE_ACTIVIDAD, SUBTIPO, MENSAJE, CLIENTE, CUENTA, FECHAS, LIBRE) VALUES('").append(aplicacion)
				.append("','").append(usuario).append("',").append("TO_DATE('").append(sdf.format(new Date()))
				.append("','YYYY-MM-DD HH24:MI:SS'),'").append(ip).append("','")
				.append(computerName).append("','").append(tipoDeActividad).append("','").append(subtipo)
				.append("','").append(mensaje).append("','").append(cliente).append("','").append(cuenta)
				.append("','").append(fechas).append("','").append(libre).append("')");
		db.exec(ds, sql.toString());
	}
}

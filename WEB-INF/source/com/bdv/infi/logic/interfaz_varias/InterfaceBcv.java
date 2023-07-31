package com.bdv.infi.logic.interfaz_varias;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.sql.DataSource;

import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

/**
 * Clase encargada de generar el archivo txt que será enviado al sistema de BCV
 *
 */
public class InterfaceBcv extends Interface implements Runnable {
	
	public InterfaceBcv(DataSource dso) {
	  super(dso);
	}
	
	public InterfaceBcv(DataSource dso,String[] idMensajes) {
		super(dso,idMensajes);
	}		

	@Override
	protected String getTipoMensaje() {
		return MensajeBcv.TIPO_MENSAJE;
	}

	@Override
	protected String getTipoProceso() {
		return TransaccionNegocio.INTERFACE_BCV;
	}

	@Override
	protected void getDatosDeTransferencia() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); 
		String nombreyRutaFinal = "";
		datosTransferencia = new DatosTransferencia();
		HashMap<String,String> parametros = parametrosDAO.buscarParametros(ParametrosSistema.INTERFACE_BCV);
		nombreyRutaFinal = parametros.get(ParametrosSistema.RUTA_ARCHIVO);
		//Sustituye %fecha y %consecutivo por los valores correspondientes
		nombreyRutaFinal = nombreyRutaFinal.replaceAll("%fecha", sdf.format(new Date()));
		nombreyRutaFinal = nombreyRutaFinal.replaceAll("%consecutivo", "001"); //TODO buscar como se manejará el consecutivo 
		
		datosTransferencia.setRutaArchivoFinal(nombreyRutaFinal);
		datosTransferencia.setDirectorioFinalFtp(parametros.get(ParametrosSistema.RUTA_FTP));
		datosTransferencia.setIpServidor(parametros.get(ParametrosSistema.SERVIDOR));
	}
	
	public void run() {
		generar();
	}	
}

package com.bdv.infi.logic.interfaz_varias;

import java.util.HashMap;

import javax.sql.DataSource;

import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

/**
 * Clase encargada de generar el archivo txt que será enviado al sistema de ESTADISTICA
 *
 */
public class InterfaceEstadistica extends Interface implements Runnable {

	public InterfaceEstadistica(DataSource dso) {
		super(dso);		
	}
	
	public InterfaceEstadistica(DataSource dso,String[] idMensajes) {
		super(dso,idMensajes);
	}	

	public void run() {
		this.generar();
	}
	
	@Override
	protected void getDatosDeTransferencia() throws Exception {
		datosTransferencia = new DatosTransferencia();
		HashMap<String,String> parametros = parametrosDAO.buscarParametros(ParametrosSistema.INTERFACE_ESTADISTICA);
		datosTransferencia.setRutaArchivoFinal(parametros.get(ParametrosSistema.RUTA_ARCHIVO));
		datosTransferencia.setDirectorioFinalFtp(parametros.get(ParametrosSistema.RUTA_FTP));
		datosTransferencia.setIpServidor(parametros.get(ParametrosSistema.SERVIDOR));
	}
	
	@Override
	protected String getTipoMensaje() {		
		return MensajeEstadistica.TIPO_MENSAJE;
	}

	@Override
	protected String getTipoProceso() {
		return TransaccionNegocio.INTERFACE_ESTADISTICA;
	}
	
	@Override
	protected String getDestinoFinal(){
		return "'" + datosTransferencia.getDirectorioFinalFtp() + "'";
	}
}
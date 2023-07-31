package com.bdv.infi.logic.interfaz_varias;

import javax.sql.DataSource;

import com.bdv.infi.dao.MensajeDAO;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
/**
 * * Clase encargada de generar el archivo txt que será enviado al sistema de CARMEN
 *
 */
public class InterfaceCarmen extends Interface implements Runnable {
	private org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(InterfaceCarmen.class);
	/**
	 * Tipo de mensaje a generar
	 */
	private String tipoMensaje = "";
	
	/**
	 * id de mensajes que deben ser enviados
	 */
	private String idMensajes = "";	
	
	public InterfaceCarmen(DataSource dso) {
		super(dso);
	}
	
	public InterfaceCarmen(DataSource dso,String[] idMensajes) {
		super(dso,idMensajes);
	}
	
	public void run() {
		this.generar();
	}
	
	/**
	 * Establece el tipod e mensaje a generar
	 */
	public void setTipoMensaje(String tipoMensaje){
		this.tipoMensaje = tipoMensaje;
	}
	
	/**
	 * Establece los id de mensajes a enviar
	 */
	public void setIdMensajes(String idMensajes){
		this.idMensajes = idMensajes;
	}	
		
	protected void proceso() throws Exception {
		MensajeDAO mensajeDAO = new MensajeDAO(dataSource);
		long time =System.currentTimeMillis();
		logger.info("InterfaceCarmen.proceso-> inicio de proceso");
		
		if (tipoMensaje.equals("")){
			mensajeDAO.cargaTitulosCarmen(MensajeCarmen.TIPO_MENSAJE + MensajeCarmen.ENTRADA,null);
			mensajeDAO.cargaTitulosCarmen(MensajeCarmen.TIPO_MENSAJE + MensajeCarmen.SALIDA,null);
		}else{
			mensajeDAO.cargaTitulosCarmen(tipoMensaje,idMensajes);	
		}				
		logger.info("InterfaceCarmen.proceso-> fin de proceso. "+(System.currentTimeMillis()-time) +"mseg");
	}
	
	
	
//	/**
//	 * Obtiene los datos de transferencia por el tipo de operación
//	 * @param tipoOperacion tipo de operación ENTRADA o SALIDA
//	 * @throws Exception en caso de error
//	 */
//	protected void getDatosDeTransferencia(String tipoOperacion) throws Exception {
//		datosTransferencia = new DatosTransferencia();
//		HashMap<String,String> parametros = parametrosDAO.buscarParametros(ParametrosSistema.INTERFACE_CARMEN);
//		
//		if (tipoOperacion.equals(MensajeDAO.OPERACION_ENTRADA)){
//			datosTransferencia.setRutaArchivoFinal(parametros.get(ParametrosSistema.RUTA_ARCHIVO_ENTRADA));	
//		}else{
//			datosTransferencia.setRutaArchivoFinal(parametros.get(ParametrosSistema.RUTA_ARCHIVO_SALIDA));
//		}		
//		
//		datosTransferencia.setDirectorioFinalFtp(parametros.get(ParametrosSistema.RUTA_FTP));
//		datosTransferencia.setIpServidor(parametros.get(ParametrosSistema.SERVIDOR));
//	}	

	@Override
	protected String getTipoMensaje() {
		return MensajeCarmen.TIPO_MENSAJE;
	}

	@Override
	protected String getTipoProceso() {
		return TransaccionNegocio.INTERFACE_CARMEN;
	}

	@Override
	protected void getDatosDeTransferencia() throws Exception {		
	}	
}

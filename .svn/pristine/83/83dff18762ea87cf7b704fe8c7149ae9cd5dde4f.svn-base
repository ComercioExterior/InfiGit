package com.bdv.infi.logic.interfaz_varias;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import megasoft.Logger;

import com.bdv.infi.dao.MensajeDAO;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

/**
 * Clase encargada de generar el archivo txt que será enviado al sistema de BCV
 *
 */
public class InterfaceBcvTxt extends Interface {
	
	HttpServletResponse _res = null;
	
	public InterfaceBcvTxt(DataSource dso) {
	  super(dso);
	}
	
	public InterfaceBcvTxt(DataSource dso,String[] idMensajes) {
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
	
	public void run(HttpServletResponse _res) {
		this._res = _res;
		try {
			if (preProceso()){
				proceso();
				postProceso();			
			}
		} catch (Exception e) {
			Logger.error(this, "Error en la generación del archivo de tipo " + getTipoMensaje(),e);
			if (proceso != null){
				proceso.setDescripcionError(e.getMessage());
				try {
					//FileUtil.delete(archivoFinal.getPath());
					postProceso();
				} catch (Exception e1) {
					Logger.error(this,"Error en el cierre del proceso");
				}
			}
		}
	}
	
	/**
	 * Proceso encargado de generar el archivo plano y enviarlo vía FTP
	 * @throws Exception en caso de error
	 */
	protected void proceso() throws Exception {
		//Consultar ruta de respaldo
		//String ruta=ParametrosDAO.listarParametros("TEMP_DIRECTORY",this.dataSource);
		
		mensajeDAO = new MensajeDAO(dataSource);
		StringBuffer listaRegistros = new StringBuffer();
		//String archivoFinal =  ruta + "archivoBcv" + Math.random() + ".txt";
		
		if (idMensajes != null){
			listaMensajes = (ArrayList<Mensaje>) mensajeDAO.listarMensajesPorEnviar(getTipoMensaje(),idMensajes);
		}else{
			listaMensajes = (ArrayList<Mensaje>) mensajeDAO.listarMensajesPorEnviar(getTipoMensaje());
		}
		
		if (listaMensajes.size() == 0){
			throw new Exception("No hay mensajes por enviar");
		}
		
		for (Mensaje mensaje : listaMensajes) {
			listaRegistros.append(mensaje.obtenerLinea() + "\r\n");
		}
		//FileUtil.crearArchivo(listaRegistros, archivoFinal);
		
		_res.addHeader("Content-Disposition","attachment;filename=interfaceBcv.txt"); 
		_res.setContentType("application/x-download"); 
		ServletOutputStream os=_res.getOutputStream();	
		os.write(listaRegistros.toString().getBytes()); 		
		os.flush();	
		
		//marcarMensajesEnviados();		
	}	
}
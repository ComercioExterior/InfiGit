package com.bdv.infi.logic.interfaz_varias;

import java.io.File;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import javax.sql.DataSource;
import megasoft.Logger;
import megasoft.db;
import com.bdv.infi.dao.MensajeDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.ftp.FTPUtil;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.util.FileUtil;

public abstract class Interface {

	
	protected File archivoFinal;
	private Object usuarioId;
	protected Proceso proceso;
	private ProcesosDAO procesoDAO;
	protected ParametrosDAO parametrosDAO = null;
	protected String[] idMensajes = null;
	protected ArrayList<Mensaje> listaMensajes = null;
	protected MensajeDAO mensajeDAO = null;
	private boolean puedeIniciar = true;

	
	protected DataSource dataSource;
	protected DatosTransferencia datosTransferencia;
	

	public Interface(DataSource dso) {
		this.dataSource = dso;
		this.parametrosDAO = new ParametrosDAO(dso);
	}
	
	public Interface(DataSource dso, String[] idMensajes) {
		this.dataSource = dso;
		this.parametrosDAO = new ParametrosDAO(dso);
		this.idMensajes = idMensajes;
	}
	
	protected abstract String getTipoProceso();
	
	protected abstract String getTipoMensaje();
	
	/**
	 * Genera el archivo que debe ser transferido
	 * */
	protected void generar() {
		try {
			if (preProceso()){
				proceso();
				postProceso();			
			}
		} catch (Exception e) {
			borrarArchivoFinal();
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
	 * Transfiere el archivo vía FTP
	 * @param archivo archivo origen que debe ser transferido
	 * @throws Exception en caso de error
	 */
	protected void transferir() throws Exception {
		Logger.info(this,"Transfiriendo el archivo " + archivoFinal.getPath());
		Logger.debug(this,"Destino final " + datosTransferencia.getDirectorioFinalFtp());
		Logger.debug(this,"Archivo final " + getDestinoFinal());
		
		
		FTPUtil ftpUtil = new FTPUtil(datosTransferencia.getIpServidor(), this.dataSource);
		ftpUtil.putFTPAscci(archivoFinal.getPath(), getDestinoFinal(), "", false);
		borrarArchivoFinal();
	}
	
	/**
	 * Se ejecuta antes de iniciar el proceso de generación de archivo
	 * 
	 * @return devuelve verdadero si se genero el proceso
	 * @throws Exception en caso de error
	 */
	protected boolean preProceso() throws Exception {		
		procesoDAO = new ProcesosDAO(dataSource);
		procesoDAO.listarPorTransaccionActiva(getTipoProceso());
		if (procesoDAO.getDataSet().count() > 0) {
			Logger.info(this, "Proceso: " + getTipoProceso() + " ya esta en ejecución.");
			puedeIniciar = false;
		} else {
			Logger.info(this,"Iniciando proceso de " + getTipoProceso());
			proceso = new Proceso();
			proceso.setTransaId(getTipoProceso());
			proceso.setFechaInicio(new Date());
			proceso.setFechaValor(new Date());

			/* Primero creamos el proceso */
			db.exec(dataSource, procesoDAO.insertar(proceso));
			getDatosDeTransferencia();
		}
		return puedeIniciar;
	}

	/**
	 * Proceso encargado de generar el archivo plano y enviarlo vía FTP
	 * @throws Exception en caso de error
	 */
	protected void proceso() throws Exception {
		
		archivoFinal = new File(datosTransferencia.getRutaArchivoFinal());
		if (archivoFinal.exists()){
			throw new Exception("El archivo de destino existe, no es posible generar el archivo");
		}
		mensajeDAO = new MensajeDAO(dataSource);
		ArrayList<String> listaRegistros = new ArrayList<String>();
		
		if (idMensajes != null){
			listaMensajes = (ArrayList<Mensaje>) mensajeDAO.listarMensajesPorEnviar(getTipoMensaje(),idMensajes);
			
		}else{
			
			listaMensajes = (ArrayList<Mensaje>) mensajeDAO.listarMensajesPorEnviar(getTipoMensaje());
		}
		
		if (listaMensajes.size() == 0){
			throw new Exception("No hay mensajes por enviar");
		}
		
		
		for(int i=0;i<listaMensajes.size();++i) {
			
			//System.out.println(" Registro procesado " + i + " Orden: " + listaMensajes.get(i).getOrdeneId());
				String line=null;
			
			try {
					
				line=listaMensajes.get(i).obtenerLinea();												
				listaRegistros.add(line);
																				
			} catch (Exception e){
				
				//System.out.println("Se captura un error procesando la linea " + i +" Orden: "+ listaMensajes.get(i).getOrdeneId());
				MensajeDAO msjDAO=new MensajeDAO(dataSource);								
				com.bdv.infi.dao.Transaccion transaccion = new com.bdv.infi.dao.Transaccion(dataSource);
								
				transaccion.begin();
				
				Statement stm=transaccion.getConnection().createStatement();
				stm.execute(msjDAO.agregarObservacion(listaMensajes.get(i).getId(),e.getMessage(),MensajeEstadistica.TIPO_MENSAJE));
				//System.out.println("Se remueve el elemento " +i+ "  Orden: " + listaMensajes.get(i).getOrdeneId());
				listaMensajes.remove(i);				
				transaccion.getConnection().commit();		
				
			}				
		}
		
		if(listaRegistros.size()>0){
		
			FileUtil.put(archivoFinal.getAbsolutePath(), listaRegistros, true);
			transferir();			
			marcarMensajesEnviados();	
		}
					
	}
	
	/**
	 * Marca como enviado aquellos mensajes que se han ido en el archivo FTP 
	 * @throws Exception en caso de error
	 */
	protected void marcarMensajesEnviados() throws Exception{
		String consultas[] = mensajeDAO.marcarMensajesEnviados(listaMensajes,proceso.getEjecucionId());
		db.execBatch(dataSource, consultas);	
	}
	
	/**
	 * Acciones que deben ejecutarse al finalizar el proceso
	 * @throws Exception en caso de error
	 */
	protected void postProceso() throws Exception {
		if (proceso != null) {
			proceso.setFechaFin(new Date());
			if (proceso.getDescripcionError() == null) {
				proceso.setDescripcionError("");
			}
			db.exec(dataSource, procesoDAO.modificar(proceso));
		}

		if (procesoDAO != null) {
			procesoDAO.cerrarConexion();
		}
	}
	
	/**Obtiene el objeto que contiene los datos de transferencia y generación del archivo final
	 * @throws Exception */
	protected abstract void getDatosDeTransferencia() throws Exception;
	
	/**
	 * Borra el archivo final si hay un error o después de hacer la transferencia
	 */
	protected void borrarArchivoFinal(){
		if (archivoFinal != null && !archivoFinal.equals("")){
			Logger.info(this, "Borrando archivoFinal " + archivoFinal.getAbsolutePath());
			archivoFinal.delete();
		}
	}
	
	/**
	 * Obtiene la ruta para el archivo final.
	 */
	protected String getDestinoFinal(){
		String rutaFinal = datosTransferencia.getDirectorioFinalFtp();		
		if (!rutaFinal.endsWith("/")){
			rutaFinal = rutaFinal.concat("/");
		}
		rutaFinal = rutaFinal.concat(archivoFinal.getName());
		return rutaFinal; 
	}

	public boolean isPuedeIniciar() {
		return puedeIniciar;
	}

	public void setPuedeIniciar(boolean puedeIniciar) {
		this.puedeIniciar = puedeIniciar;
	}
}
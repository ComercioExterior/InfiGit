package com.bdv.infi.logic.interfaz_varias;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import megasoft.AppProperties;
import megasoft.DataSet;
import megasoft.db;

import org.apache.log4j.Logger;

import com.bdv.infi.dao.AuditoriaDAO;
import com.bdv.infi.dao.ControlCicloDAO;
import com.bdv.infi.dao.EnvioCorreosDAO;
import com.bdv.infi.dao.EnvioMailDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.OrdenesClienteDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.data.Auditoria;
import com.bdv.infi.data.Ciclo;
import com.bdv.infi.data.EnvioMail;
import com.bdv.infi.data.Orden;
import com.bdv.infi.data.OrdenDocumento;
import com.bdv.infi.data.PlantillaMail;
import com.bdv.infi.data.PlantillaMailArea;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.logic.ProcesarPlantillas;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ConstantesHilos;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.PlantillasMailTipos;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.util.FileUtil;
import com.bdv.infi.util.Utilitario;


public class EnvioCorreos {
	
	static final Logger logger = Logger.getLogger(EnvioCorreos.class);
	DataSource _dso;
	ServletContext _app;
	String ip;
	public String nombreUsuario;
	public HashMap<String,String> parametros; //HashMap con los parametros relacionados con el envio de correos
	ParametrosDAO paDAO; //DAO de parametros del sistema
	EnvioCorreosDAO ecDAO; //DAO de envio de correos
    ArrayList<String> correosPorEnviar; //Cola de correos por enviar
    ArrayList<String> arrTmp; //ArrayList para guardar info temporalmente
    //Expresion regular que valida la estructura de los registros del archivo con la cola de correos a enviar
    Pattern p = Pattern.compile("[1-9][0-9]*,((0[1-9])|([12][0-9])|(3[01]))/((0[1-9])|(1[0-2]))/[1-9][0-9]{3}\\s(([01][0-9])|(2[0-3])):[0-5][0-9]:[0-5][0-9],[a-zA-zÒ—·ÈÌÛ˙¸¡…Õ”⁄‹\\-_ ]+,[a-zA-zÒ—·ÈÌÛ˙¸¡…Õ”⁄‹\\-_., ]+,[a-zA-zÒ—·ÈÌÛ˙¸¡…Õ”⁄‹@.,\\-_]+");
    //Expresion regular que valida un email
    //Pattern pEmail = Pattern.compile("^([0-9a-zA-Z]([_\\-.w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-w]*[0-9a-zA-Z].)+([a-zA-Z]{2,9}.)+[a-zA-Z]{2,3})$");
    Pattern pEmail = Pattern.compile("^([0-9a-zA-Z][0-9a-zA-Z_\\-.]+@([0-9a-zA-Z_\\-]+[.]){1,2}[a-zA-Z]{2,3})$");
    Matcher m; //Matcher que verifica que se cumpla con el patron
    public String rutaFileCola = "", fechaFile = "";
    public ArrayList<String> ordenesIds = null;
    private Proceso proceso;
    private Ciclo ciclo;
    private ProcesosDAO procesoDAO;
    private DataSet infoCorreos;
    private ArrayList<EnvioMail> correos;
    private String tipoDestinatario;
    public String insertCiclo = "";
    public String eventoId;
    public String idsCruce;
    
    public EnvioCorreos(DataSource datasource, ServletContext _app, String ip) {
		this._dso = datasource;
		this._app = _app;
		this.ip = ip;
		this.procesoDAO = new ProcesosDAO(_dso);
	}
	
	public void execute(){
		
	}
	

	public void envioCorreos() {
		
		try {
			
			logger.info(proceso.getTransaId()+": Iniciando el proceso de envio de correos... ");
			logger.info(proceso.getTransaId()+": Hay "+this.getInfoCorreos().count()+" correo(s) por enviar del ciclo "+this.getCiclo().getCicloId());
			
			if ( this.getInfoCorreos().count() > 0 ) { //Si existen correos por enviar
				
				infoCorreos.first();
				
				for(int i=0; i<infoCorreos.count(); i++) { //Recorre los registros con la info de los correos por enviar
					
					infoCorreos.next();
						
	                enviarCorreos(i);
	            		
	            }
				
            }else{
            	proceso.setDescripcionError(proceso.getDescripcionError()+" No se hallaron correos por enviar.");
            	logger.info(this.ciclo.getTipo()+": No se hallaron correos por enviar.");
            }
            
		} catch (Exception ex) {
			logger.error(proceso.getTransaId()+": Excepcion ocurrida en el proceso de envio de correos. ", ex);
			if(proceso != null) {
				proceso.setDescripcionError(proceso.getDescripcionError()+" Excepcion ocurrida en el proceso de envio de correos: "+ex.getMessage());
			}
		} finally {
			if(this.proceso != null) {
				//TERMINA PROCESO
				terminarProceso();
			}
			//TERMINA CICLO
			if(this.ciclo != null) {
				logger.info("Ciclo distinto a null en EnvioCorreos");
				terminarCiclo(this.ciclo.getCicloId(), ConstantesGenerales.STATUS_ENVIO_CORREO_FINALIZADO);
			}
			logger.info(this.ciclo.getTipo()+": Terminado el proceso de envio de correos... ");
		}
	}
	
		
	/**
	 * Se inicializan variables relacionadas con el envio de correos
	 * 
	 * @return
	 */
	public void initParamEnvio() throws Exception {		
		_dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));
		ecDAO = new EnvioCorreosDAO(_dso);
		paDAO  = new ParametrosDAO(_dso);		
		parametros = paDAO.buscarParametros(ParametrosSistema.ENVIO_CORREOS);
	}
	
	/**
	 * Obtiene la fecha actual formateada
	 * @param sdf Formato de la fecha
	 * @return String con la fecha formateada
	 */
	public String getCurrentFormatedDate(SimpleDateFormat sdf) throws Exception {
		Calendar calendarNow = Calendar.getInstance(); //Fecha actual
		return sdf.format(calendarNow.getTime()); //Retorna la fecha actual formateada
	}
	
	
	/**
	 * Lee el archivo con la cola de correos por enviar
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected void leerCorreosPorEnviar(String fechaActual, String rutaFileCola) throws Exception {
		
		try{
			if ( new File(rutaFileCola).exists() ) {
				correosPorEnviar = FileUtil.get(rutaFileCola);
				logger.info("Se leyo el archivo con la cola de correos por enviar");
			}else{
				logger.info("No se pudo leer el archivo de correos por enviar. El archivo: '"+
				parametros.get(ParametrosSistema.DIR_COLA_CORREOS) + "_" + fechaActual + ConstantesGenerales.EXTENSION_DOC_TXT +
				"' no se encuentra en la ruta: " + parametros.get(ParametrosSistema.DESTINATION_PATH) + File.separator + 
				parametros.get(ParametrosSistema.DIR_COLA_CORREOS));
			}
		}catch(Exception e){
			logger.info("Error al intentar leer el archivo de correos por enviar: "+rutaFileCola+":\n"+e.getMessage());
		}
	}
	
	
	/**
	 * Obtiene la informacion del area de la plantilla
	 * @param idPlant : id de la plantilla
	 * @return Objeto del tipo PlantillaMailArea
	 */
	protected PlantillaMailArea getAreaInfo(int idPlant) throws Exception {
		ecDAO.getAreaFromIdPlant(idPlant);		
		DataSet _area = ecDAO.getDataSet(); //DataSet con la informacion del area determinada		
		_area.next();
		logger.info("Se obtuvo el area del correo a enviar");		
		//Se llena la informacion requerida del area a la que pertenece la plantilla
		PlantillaMailArea plantArea = new PlantillaMailArea();		
		if( (_area.getValue("PLANT_MAIL_AREA_ID")!=null) && (!_area.getValue("PLANT_MAIL_AREA_ID").equals("")) ) {
			plantArea.setPlantillaMailId(Integer.parseInt(_area.getValue("PLANT_MAIL_AREA_ID")));
		}else{
			logger.info("El ID del area es nulo (PLANT_MAIL_AREA_ID)");
		}
		plantArea.setPlantMailAreaName(_area.getValue("PLANT_MAIL_AREA_NAME"));
		plantArea.setDestinatario(_area.getValue("DESTINATARIO"));
		
		return plantArea;
	}
	
	
	/**
	 * Ejecuta el script de envio de correos
	 * @return
	 */
	protected void runScriptEnvioCorreo(EnvioMail em) throws Exception {
		//String fechaNow = getCurrentFormatedDate(new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA_REGISTRO)); //Fecha actual formateada
		
		//TODO NM29643 QUITAR ESTE COMENTARIO Y LA ASIGNACIONNNNNNN (solo prara prueba de volumen)
		//SE CORRE SCRIPT DE ENVIO DE CORREOS
		int codRetornoScript = Os.callDirectConnect(parametros.get(ParametrosSistema.DESTINATION_PATH)+File.separator+parametros.get(ParametrosSistema.DIR_SH), parametros.get(ParametrosSistema.SCRIPT_NAME));
//		int codRetornoScript = 0;
		logger.info(proceso.getTransaId()+": Correo ID "+em.getIdCorreo()+": Codigo de retorno script de envio de correos: " + codRetornoScript);
		
		if (codRetornoScript == 0) { //SE ENVIO EL CORREO
			if(em.getStatus().equalsIgnoreCase(this.parametros.get(ParametrosSistema.STATUS_INVALIDO))){
				logger.info(proceso.getTransaId()+": Correo ID "+em.getIdCorreo()+": INVALIDO ("+em.getDireccionCorreo()+"). Ejecucion del script de envio de correos correcta.");
			}else{
				logger.info(proceso.getTransaId()+": Correo ID "+em.getIdCorreo()+": ENVIADO ("+em.getDireccionCorreo()+"). Ejecucion del script de envio de correos correcta.");
				//Se cambia el estatus del correo a enviado
				em.setStatus(parametros.get(ParametrosSistema.STATUS_ENVIADO));
			}
		}else{ //NO SE ENVIO EL CORREO
			logger.error(proceso.getTransaId()+": Correo ID "+em.getIdCorreo()+": NO ENVIADO. Error en la ejecucion del script durante el envio del correo");
			//Se cambia el estatus del correo a no enviado
			em.setStatus(parametros.get(ParametrosSistema.STATUS_NO_ENVIADO));
		}
		//SE ACTUALIZA ESTATUS DE CORREO
		if(!actualizarCorreo(em, null, true, null)){
			proceso.setDescripcionError(proceso.getDescripcionError()+" | Correo ID "+em.getIdCorreo()+": No se pudo actualizar estatus a "+em.getStatus().toUpperCase()+" y su contenido");
			logger.error(proceso.getTransaId()+": Correo ID "+em.getIdCorreo()+": No se pudo actualizar estatus a "+em.getStatus().toUpperCase()+" y su contenido");
		}
	}
	
	/**
	 * Envia los correos registrados en la cola de correos por enviar
	 * @param i : indice del correo a enviar
	 */
	protected void enviarCorreos(int indice) throws Exception {
		
		boolean funcional = false;
    	String dest = "";
    	PlantillaMailArea plantArea;
    	
    	//Se obtiene el objeto con informacion del correo
    	EnvioMail em = getCorreos().get(indice);
    	
    	//Se llena la informacion requerida de la plantilla
		PlantillaMail plantilla = new PlantillaMail();						            				
		plantilla.setPlantillaMailId(Integer.parseInt(infoCorreos.getValue("PLANTILLA_MAIL_ID")));
		plantilla.setPlantillaMailName(infoCorreos.getValue("PLANTILLA_MAIL_NAME"));
		plantilla.setRemitente(infoCorreos.getValue("REMITENTE"));
		plantilla.setTipoDestinatario(infoCorreos.getValue("TIPO_DESTINATARIO"));
		plantilla.setAsunto(infoCorreos.getValue("ASUNTO"));
		plantilla.setCuerpo(infoCorreos.getValue("CUERPO"));
		plantilla.setEventoId(infoCorreos.getValue("EVENTO_ID"));
		eventoId = plantilla.getEventoId();
		plantilla.setBloqueIterado(infoCorreos.getValue("BLOQUE_ITERADO"));
		
		if(plantilla.getTipoDestinatario().equalsIgnoreCase(getTipoDestinatario())){
			
			if(plantilla.getTipoDestinatario().equalsIgnoreCase(PlantillasMailTipos.TIPO_DEST_FUNCIONAL_COD)){ //DESTINATARIO FUNCIONAL
				funcional = true;
				plantArea = new PlantillaMailArea();
				plantArea.setPlantMailAreaId(Integer.parseInt(infoCorreos.getValue("PLANT_MAIL_AREA_ID")));
				plantArea.setPlantMailAreaName(infoCorreos.getValue("PLANT_MAIL_AREA_NAME"));
				plantArea.setDestinatario(infoCorreos.getValue("DESTINATARIO"));
			}
			
			//Se valida correo malformado
			dest = em.getDireccionCorreo();
logger.debug("destinatario original: "+dest);
			boolean invalid = false;
			if(dest==null || dest.length()<=0 || dest.equals("")){
				dest = "";
				invalid = true;
			}
			if(!invalid){
				dest = dest.trim(); //Se quitan espacios
				em.setDireccionCorreo(dest);
				m = pEmail.matcher(dest); //Compara contra el patron de email
	            if (!m.find()) { //Si el campo NO coincide con el formato correspondiente al email
					//invalid = true;
	            	//Lo marca como invalido pero intento envio
	            	em.setStatus(this.parametros.get(ParametrosSistema.STATUS_INVALIDO));
					if(!actualizarCorreo(em, null, false, null)){
						proceso.setDescripcionError(proceso.getDescripcionError()+" | Correo ID "+em.getIdCorreo()+": No se pudo actualizar estatus a Invalido.");
						logger.error(proceso.getTransaId()+": Correo ID "+em.getIdCorreo()+": No se pudo actualizar estatus a Invalido.");
					}
	            	proceso.setDescripcionError(proceso.getDescripcionError()+" | Correo ID "+em.getIdCorreo()+": Email ("+em.getDireccionCorreo()+") no coincide con el patron. Se intentara su envio.");
					logger.info(proceso.getTransaId()+": Correo ID "+em.getIdCorreo()+": Email ("+em.getDireccionCorreo()+") no coincide con el patron. Se intentara su envio.");
				}
			}
//logger.debug("destinatario modif: "+dest);
logger.debug("destinatario modif: "+em.getDireccionCorreo());
			if(invalid){
				logger.info(proceso.getTransaId()+": Correo ID "+em.getIdCorreo()+": Direccion de correo invalida. El correo no se enviara y se marcara como invalido.");
				em.setStatus(this.parametros.get(ParametrosSistema.STATUS_INVALIDO));
				if(!actualizarCorreo(em, null, false, null)){
					proceso.setDescripcionError(proceso.getDescripcionError()+" | Correo ID "+em.getIdCorreo()+": No se pudo actualizar estatus a Invalido.");
					logger.error(proceso.getTransaId()+": Correo ID "+em.getIdCorreo()+": No se pudo actualizar estatus a Invalido.");
				}
			}else{
				//MAIL VALIDO
				
				//Se setea Contenido del email

				if(!funcional){ //CLIENTE
					
					//Se reemplazan las ETIQUETAS DEL CUERPO de la plantilla
					OrdenDocumento ordDoc = listarPlantilla(em.getIdOrden(), plantilla, _app, ip);
					
					em.setContenido(ordDoc.getContenido());
					
				}else{

					//Se envia el cuerpo de la plantilla sin sustitucion de etiquetas (son solo para tipo Cliente)
					em.setContenido(plantilla.getCuerpo());
					
				}
				
				//SE CREA EL SHELL
				crearShellEnvio(plantilla.getAsunto(), em.getContenido(), em.getDireccionCorreo(), plantilla.getRemitente());
				
				//SE GUARDA CUERPO EN ARCHIVO TEMPORAL
				arrTmp = new ArrayList<String>();
				arrTmp.add(em.getContenido()); //Se almacena el cuerpo del correo en el ArrayList
    			//Se crea archivo temporal con informacion del cuerpo requerida para el envio del correo
				//De modificar el nombre del archivo temporal con la informacion del cuerpo en la base de datos se debe modificar el mismo en el shell script (envio.sh)
    			FileUtil.put(parametros.get(ParametrosSistema.DESTINATION_PATH) + File.separator  + parametros.get(ParametrosSistema.DIR_TEMP) + File.separator + parametros.get(ParametrosSistema.ARCH_TMP_CUERPO) + ConstantesGenerales.EXTENSION_DOC_TXT, arrTmp, true);
    			logger.info(proceso.getTransaId()+": Correo ID "+em.getIdCorreo()+": Se creo el archivo temporal con el cuerpo del correo");
				
    			
    			//SE CORRE SCRIPT DE ENVIO DE CORREOS
    			runScriptEnvioCorreo(em);
    			
    			
			}//MAIL VALIDO
			
		}else{
			proceso.setDescripcionError("El tipo de destinatario seleccionado no coincide con el de la plantilla");
			terminarCiclo(this.ciclo.getCicloId(), null);
		}
		
		
	}
	
	
	/**
	 * Crea el archivo con la cola de correos por enviar
	 * @param rutaArch: ruta de creacion del archivo con la cola de correos a a enviar
	 * @param plantMailName: nombre de la plantilla a utilizar acorde al proceso que realiza el envio
	 * @param tipoDest: 'C'=Cliente o 'F'=Funcional(Area)
	 * @param destOarea: indica los correos de los destinatarios, si tipoDest=='C', o el area funcional, tipoDest=='F'
	 * @return boolean que indica si se efectuo la creaciond el archivo de cola de correos a enviar
	 */
	@SuppressWarnings("unchecked")
	public boolean crearArchColaCorreos(String rutaArch, String plantMailName, String tipoDest, ArrayList<String[]> destOarea) throws Exception{
		int idCorr = 1;
		String idCorreo = "1";
		String line = "";
		String fechaRegistroCorreo = getCurrentFormatedDate(new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA_REGISTRO)); //Fecha actual formateada para los registros de los correos a enviar
		ArrayList<String> registro = new ArrayList<String>();
		
		if( new File(rutaArch).exists() ){ //Se lee el id de ultimo registro de correo a enviar
			ArrayList<String> contenidoArchCola = (ArrayList<String>)FileUtil.get(rutaArch); //Se obtiene cada registro en el archivo en el ArrayList
			int sizeArch = contenidoArchCola.size();
			if(sizeArch > 0) { //Si no es vacio el archivo
				int i = sizeArch-1;
				line = contenidoArchCola.get(i);
				while( (line==null || line.equals("") || line.length()==0) && i>0){ //Recorre el archivo de lfinal a comienzo buscando la 1ra linea no vacia
					i--;
					line = contenidoArchCola.get(i);
				}
				if(line!=null && !line.equals("") && line.length()>0){ //Si se encontro un registro no vacio (ultimo)
					 m = p.matcher(line); //Valida si el registro cumple con la estructura debida
					 if(m.find()){ //Si el registro coincide con el formato correspondiente
						 logger.info("INDEX '"+parametros.get(ParametrosSistema.SEPARADOR_ARCH)+"' : "+line.indexOf(parametros.get(ParametrosSistema.SEPARADOR_ARCH)));
						 idCorr = Integer.parseInt(line.substring(0,line.indexOf(parametros.get(ParametrosSistema.SEPARADOR_ARCH)))); //ID del ultimo registro de correo por enviar
						 idCorr++;
						 logger.info("ID CORREO A AGREGAR: "+idCorr);
						 idCorreo = "\r\n" + idCorr; //Se incrementa en 1 el ID del proximo registro a insertar
					 }
				}
			}
		}
		
		String emailNameCte[] = new String[2]; //Pos 0: Email Cliente; Pos 1: Nombre Cliente
		
		for(int i=0; i<destOarea.size(); i++) { //por cada correo a enviar
			emailNameCte = destOarea.get(i);
			line = idCorreo + parametros.get(ParametrosSistema.SEPARADOR_ARCH) +
				fechaRegistroCorreo + parametros.get(ParametrosSistema.SEPARADOR_ARCH) +
				plantMailName + parametros.get(ParametrosSistema.SEPARADOR_ARCH);
			if(tipoDest.equals(parametros.get(ParametrosSistema.DEST_CLIENTE))) {
				line += emailNameCte[1]; //Nombre del cliente
			}else{
				if(tipoDest.equals(parametros.get(ParametrosSistema.DEST_FUNCIONAL))) {
					line += tipoDest;
				}
			}
			line += parametros.get(ParametrosSistema.SEPARADOR_ARCH) +
				emailNameCte[0];
			registro.add(line);
			idCorr++;
			idCorreo = idCorr+"";
			emailNameCte = new String[2];
		}		
		
		FileUtil.put(rutaArch, registro, true, true);
		
		return (new File(rutaArch).exists());
	}
	
	/**
	 * Respalda el archivo con la cola de correos por enviar
	 * @param rutaArch: ruta del archivo con la cola de correos por enviar
	 * @return boolean que indica si se efectuo el archivo de respaldo
	 */
	public boolean respaldarArchColaCorreos(String rutaOrig) throws Exception{
		boolean respaldado = false;
		String rutaResp = parametros.get(ParametrosSistema.DESTINATION_PATH) + File.separator + parametros.get(ParametrosSistema.DIR_COLA_RESPALDO) + File.separator + parametros.get(ParametrosSistema.DIR_COLA_CORREOS) + "_" +  fechaFile + ConstantesGenerales.EXTENSION_DOC_TXT;
		File origFile = new File(rutaOrig);
		File respFile = new File(rutaResp);
		FileUtil.copiarArchivo(origFile, respFile);
		if(respFile.exists()) respaldado = true;
		return respaldado;
	}
		
	/**
	 * Borra el archivo con la cola de correos por enviar
	 * @param rutaArch: ruta del archivo con la cola de correos por enviar
	 * @return boolean que indica si se borro el archivo
	 */
	public boolean borrarArchColaCorreos(String rutaOrig) throws Exception{
		boolean borrado = false;
		File origFile = new File(rutaOrig);
		FileUtil.delete(rutaOrig);
		if(!origFile.exists()) borrado = true;
		return borrado;
	}
	
	
	/**
	 * Obtiene los correos de los clientes con ordenes adjudicadas del producto subasta divisas
	 * @return ArrayList<String> con los correos de los clientes con ordenes adjudicadas del producto subasta divisas
	 */
	public ArrayList<String[]> getEmailsCtes(int idEjecucion, String idUnidadInversion) throws Exception{
		ArrayList<String[]> emails = new ArrayList<String[]>();
		String tmp="", fechaNow="", dest="";
		String emailNameCte[] = new String[2]; //Pos 0: Email Cliente; Pos 1: Nombre Cliente
		int cantInvalid = 0;
		//NM29643 26/06/2013 Validacion de correo null
		boolean invalid = false;
		OrdenesClienteDAO ordCtesDAO = new OrdenesClienteDAO(_dso);
		//ordCtesDAO.getCorreosCteOrden(StatusOrden. PROCESO_ADJUDICACION, ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA);
		ordCtesDAO.getCorreosCteOrden(idUnidadInversion, StatusOrden.ADJUDICADA, ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA, idEjecucion);		
		DataSet emailsOrdenesCtes = ordCtesDAO.getDataSet();
		logger.info("Cantidad de Emails de clientes: "+emailsOrdenesCtes.count());
		if(emailsOrdenesCtes.count()>0){
			emailsOrdenesCtes.first();
			fechaNow = getCurrentFormatedDate(new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA_REGISTRO)); //Fecha actual formateada
			arrTmp = new ArrayList<String>();
			this.ordenesIds = new ArrayList<String>(); //Se inicializa la lista con los ids de las ordenes a cuyos clientes se les enviara correo de adjudicacion
			dest = "Emails Ctes: ";
			for(int i=0; i<emailsOrdenesCtes.count(); i++){
				emailsOrdenesCtes.next();
				ordenesIds.add(emailsOrdenesCtes.getValue("ordene_id"));
				//NM29643 26/06/2013 Validacion de correo null
				invalid = false;
				tmp = emailsOrdenesCtes.getValue("correo");
				if(tmp==null || tmp.length()<=0){
					tmp = "";
					invalid = true;
				}else{
					tmp = emailsOrdenesCtes.getValue("correo").trim();
				}
				emailNameCte[0] = tmp; //Se almacena el email del cliente
				emailNameCte[1] =  emailsOrdenesCtes.getValue("name").replace(",", ""); //Se almacena el nombre del cliente
				logger.info("Cliente "+(i+1)+": "+emailNameCte[0]+" - "+emailNameCte[1]+"\n");
				m = pEmail.matcher(tmp); //Compara contra el patron de email
				if (!m.find()) { //Si el campo no coincide con el formato correspondiente al email
					invalid = true;
                }
				if(invalid){
					cantInvalid++;
                	logger.info("EMAIL INV¡LIDO. El email del cliente: '"+emailsOrdenesCtes.getValue("name").trim()+"' es invalido. No se le enviar· el correo de adjudicaciÛn correspondiente.");
                	//Se inserta el registro en el archivo de correos no enviados por ser invalidos
                	registrarEnLogs((i+1)+"", fechaNow, StatusOrden.ADJUDICADA, emailNameCte[1], tmp, ParametrosSistema.STATUS_INVALIDO);
				}
                emails.add(emailNameCte); //Se aÒade el email y nombre del cliente a la lista de destinatarios, asi sea invalido (para controlar que debio enviarse)
            	dest += tmp + ", ";
            	emailNameCte = new String[2];
			}
			logger.info(dest);
			logger.info("Cantidad de emails invalidos: "+cantInvalid);
		}else{
			logger.info("No hay clientes con ordenes en estatus: "+StatusOrden.ADJUDICADA+" asociadas al tipo de producto: "+ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA+" a los cuales enviarles correos");
		}		
		
		return emails;
	}
	
	/**
	 * AÒade un registro al log indicado segun el estatus pasado por parametro
	 * @param id: id del registro del correo
	 * @param fecha: fecha del registro del correo
	 * @param proceso: nombre del proceso vinculado al correo
	 * @param tipoDest: indica si el tipo de destinatario es funcional (F) o si es un cliente indica el nombre del mismo
	 * @param destArea: si se trata de un cliente indica el correo del mismo, si es un area indica el nombre de la misma
	 * @param estatus: indicador del log en el cual se insertara el registro del correo (enviado, no_enviado, no_enviado_invalido)
	 */
	public void registrarEnLogs(String id, String fecha, String proceso, String tipoDest, String destArea, String estatus) throws Exception{
		String tmp="";
		arrTmp = new ArrayList<String>();
		tmp = id + parametros.get(ParametrosSistema.SEPARADOR_ARCH) +
	    	fecha + parametros.get(ParametrosSistema.SEPARADOR_ARCH) +
	    	proceso + parametros.get(ParametrosSistema.SEPARADOR_ARCH) +
	    	tipoDest + parametros.get(ParametrosSistema.SEPARADOR_ARCH)+
	    	destArea + "\r\n"; //Se crea el registro del correo
		arrTmp.add(tmp); //Se almacena el registro en el ArrayList
		if(estatus.equals(ParametrosSistema.STATUS_ENVIADO)){
			tmp = parametros.get(ParametrosSistema.DESTINATION_PATH) + File.separator + parametros.get(ParametrosSistema.DIR_ENVIADOS) + File.separator + parametros.get(ParametrosSistema.DIR_ENVIADOS) + "_" + fechaFile  + ConstantesGenerales.EXTENSION_DOC_TXT;
		}else{
			if(estatus.equals(ParametrosSistema.STATUS_NO_ENVIADO)){
				tmp = parametros.get(ParametrosSistema.DESTINATION_PATH) + File.separator + parametros.get(ParametrosSistema.DIR_NO_ENVIADOS) + File.separator + parametros.get(ParametrosSistema.DIR_NO_ENVIADOS) + "_" + fechaFile  + ConstantesGenerales.EXTENSION_DOC_TXT;
			}else{
				if(estatus.equals(ParametrosSistema.STATUS_INVALIDO)){
					tmp = parametros.get(ParametrosSistema.DESTINATION_PATH) + File.separator + parametros.get(ParametrosSistema.DIR_NO_ENVIADOS) + File.separator + parametros.get(ParametrosSistema.DIR_NO_ENVIADOS) + "_invalido" + "_" + fechaFile  + ConstantesGenerales.EXTENSION_DOC_TXT;
				}
			}
		}
		FileUtil.put(tmp, arrTmp, true, true); //Almacena el registro del correo en el log correspondiente segun su estatus de envio
		logger.info("Se inserto el registro en el archivo de correos correspondiente al estatus: "+estatus);
	}
	
	/**
	 * Crea de manera din·mica el shell script de envÌo de correos acorde a los par·metro configurables de la aplicaciÛn
	 */
	public void crearShellEnvio(String asunto, String cuerpo, String dest, String rem) throws Exception {
		StringBuffer sh = new StringBuffer();
		sh.append("ASUNTO=\"").append(asunto).append("\"");
		sh.append("\n");
		sh.append("DEST=\"").append(dest).append("\"");
		sh.append("\n");
		sh.append("CUERPO_FILE=").append(parametros.get(ParametrosSistema.DESTINATION_PATH)).append("/").append(parametros.get(ParametrosSistema.DIR_TEMP)).append("/").append(parametros.get(ParametrosSistema.ARCH_TMP_CUERPO)).append(ConstantesGenerales.EXTENSION_DOC_TXT);
		sh.append("\n");
		sh.append("REM=\"").append(rem).append("\"");
		sh.append("\n");
		sh.append("mailx -s \"$ASUNTO\" -r \"$REM\" -b \"$DEST\" \"\" < $CUERPO_FILE");
		String rutaScript = parametros.get(ParametrosSistema.DESTINATION_PATH)+File.separator+parametros.get(ParametrosSistema.DIR_SH)+File.separator+parametros.get(ParametrosSistema.SCRIPT_NAME);
		arrTmp = new ArrayList<String>();
		arrTmp.add(sh.toString());
		//Se crea el archivo shell script de envio de correos
		FileUtil.put(rutaScript, arrTmp, false, false);
		File f = new File(parametros.get(ParametrosSistema.DESTINATION_PATH)+File.separator+parametros.get(ParametrosSistema.DIR_SH)+File.separator+parametros.get(ParametrosSistema.SCRIPT_NAME));
		if(f.exists()){
			//Se asignan permisos Usuario=lectura, escritura y ejecuciÛn GrupoyOtros=Lectura y EjecuciÛn
			if( Os.executeCommand("chmod 755 "+rutaScript) == 0 ) {
				logger.info("Se creÛ el script de envÌo de correo y se asginÛ la permisologÌa requerida.");
			}else{
				logger.info("OcurriÛ un error al cambiar la permisologÌa del script para el envio del correo destinado a: "+dest);
				logger.info("El correo no se enviar·.");
			}
		}else{
			logger.info("No se creo el script para el envio del correo destinado a: "+dest);
			logger.info("El correo no se enviar·.");
		}
	}
	
	
	/**Busca la plantilla asociada a una orden
	 * Arma el objeto completo para que pueda ser modificado cualquier valor
	 * */
	public OrdenDocumento listarPlantilla(long ordene_id, PlantillaMail plantilla, ServletContext contexto, String ip) throws Exception{
		
		OrdenDocumento docDef = new OrdenDocumento();
		ProcesarPlantillas procesarPlantillas = new ProcesarPlantillas(_dso);
		OrdenDAO ordDAO = new OrdenDAO(_dso);
		
		//Se obtiene el obj Orden
		Orden orden;
		if(getIdsCruce()!=null && !getIdsCruce().equals("")){
			//Solo se obtienen los cruces de titulos especificados para el caso de RECEPCION DE TITULOS
			orden = ordDAO.listarOrden(ordene_id, getIdsCruce());
			logger.debug("Cantidad de Cruces Tipo TITULO: "+orden.getOrdenesCrucesTitulos());
		}else{
			orden = ordDAO.listarOrden(ordene_id);
		}
		
		
		String[] contenidoBloq = new String[1];
		String[] contenido = new String[1];
		boolean bloque = false;
		
		//NM29643 - infi_TTS_466 Actualizacion de proceso de envio de correos (eventos)
		//Si se trata de un evento que admite bloque de titulos a iterar
		if( ( plantilla.getEventoId().equalsIgnoreCase(ConstantesGenerales.CRUCE) && orden.getOrdenesCrucesTitulos()!=null && orden.getOrdenesCrucesTitulos().size()>0 ) ||
			plantilla.getEventoId().equalsIgnoreCase(ConstantesGenerales.RECEPCION_TITULO)
		){
			if(plantilla.getBloqueIterado()!=null && !plantilla.getBloqueIterado().equals("")){ //Si se seteo contenido de bloque por reemplazar
				//Se reemplazan las etiquetas del bloque de texto a iterar por cada cruce
				bloque = true;
				contenidoBloq = procesarPlantillas.desplegarPlantilla(orden, plantilla, contexto, ip, bloque);
			}else{
				logger.info("La plantilla no tiene configurada la informacion para el bloque de cruces");
			}
		}
		
		//Se reemplazan las etiquetas del cuerpo de la plantilla
		contenido = procesarPlantillas.desplegarPlantilla(orden, plantilla, contexto, ip, false);
		
		if(bloque){ //Se reemplaza el bloque de texto transformado en el cuerpo de la plantilla tb
			contenido[0] = contenido[0]==null?"":contenido[0];
			contenido[0] = contenido[0].replaceAll("@"+this.parametros.get(ParametrosSistema.ETIQUETA_BLOQ_CRUCE)+"@", contenidoBloq[0]==null?"":contenidoBloq[0]);
		}else{
			contenido[0] = contenido[0].replaceAll("@"+this.parametros.get(ParametrosSistema.ETIQUETA_BLOQ_CRUCE)+"@", "No se asignaron tÌtulos");
		}
		
		
		if(contenido != null){
			docDef.setNombre(plantilla.getPlantillaMailName()+"_"+orden.getClienteRifCed());
			docDef.setContenido(contenido[0]);
			docDef.setContenidoBytes(contenido[0].getBytes());
			docDef.setIdDocumento(0);
			docDef.setIdOrden(ordene_id);
		}
		return docDef;
	}
	
	/**
	 * Crea un nuevo ciclo del tipo ENVIO_CORREOS 
	 * @param ciclo
	 * @return boolean indicando si se inserto el ciclo de envio de correos
	 * @throws Exception
	 */
	public void insertarCicloEnvio(Ciclo ciclo) throws Exception {
		
		ControlCicloDAO contCicloDAO = new ControlCicloDAO(_dso);
		//ArrayList<String> querysEjecutar = new ArrayList<String>();
		
		//Se abre un ciclo del tipo Envio_Correos
		this.insertCiclo = contCicloDAO.insertarCiclo(ciclo);
	}
	
	/**
	 * Crea un nuevo ciclo del tipo ENVIO_CORREOS 
	 * @param ciclo
	 * @return boolean indicando si se inserto el ciclo de envio de correos
	 * @throws Exception
	 */
	public boolean actualizarCicloEnvio(Ciclo ciclo) throws Exception {
		
		ControlCicloDAO contCicloDAO = new ControlCicloDAO(_dso);
		
		return contCicloDAO.actualizarCiclo(ciclo.getCicloId(), ciclo.getStatus());
	}
	
	
	/**
	 * Registra el fin de un ciclo en la tabla 228
	 */
	public void terminarCiclo(long cicloId, String statusCiclo) {
		String ini="", fin="";
		Date fechaIni=null, fechaFin=null;
		try {
			ControlCicloDAO cicloDAO = new ControlCicloDAO(_dso);
			String descError="";
			if(this.proceso!=null){
				descError = this.proceso.getDescripcionError();
			}
			db.exec(this._dso, cicloDAO.cierreCicloAbierto(cicloId, statusCiclo, descError));
			logger.debug("Finaliza Ciclo...");
			cicloDAO.listarCicloPorID(cicloId);
			if(cicloDAO.getDataSet().count()>0){
				cicloDAO.getDataSet().first();
				cicloDAO.getDataSet().next();
				ini=cicloDAO.getDataSet().getValue("fecha_ini");
				fin=cicloDAO.getDataSet().getValue("fecha_fin");
				SimpleDateFormat formatoDelTexto = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA_HORA_BD2);
				try{
					fechaIni = formatoDelTexto.parse(ini);
					fechaFin = formatoDelTexto.parse(fin);
				}catch(ParseException ex){
					logger.error(ex.getMessage());
					//ex.printStackTrace();
				}
			}
			if (cicloDAO != null) {
				cicloDAO.cerrarConexion();
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage());
		}finally{
			if (this.proceso != null && fechaIni!=null && fechaFin!=null) {
				long duracion = fechaFin.getTime() - fechaIni.getTime();
				logger.info("Termino ciclo de tipo: "+proceso.getTransaId()+", id: "+cicloId+", duracion: " + (duracion / 1000) + " secs.");
			}
		}
	}
	
	
	/**
	 * Verifica si existe un ciclo abierto del tipo ENVIO_CORREOS
	 * @param tipoCiclo
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public long verificarCicloExistente(String tipoCiclo, String status) throws Exception {
		ControlCicloDAO controlCicloDAO = new ControlCicloDAO(_dso);
		
		controlCicloDAO.listarCicloPorTipoStatus(tipoCiclo, status);	
		if(controlCicloDAO.getDataSet()!=null && controlCicloDAO.getDataSet().count()==0 ) {
			return (long)(controlCicloDAO.getDataSet().count());
		}else{
			controlCicloDAO.getDataSet().first();
			controlCicloDAO.getDataSet().next();
			return Long.parseLong(controlCicloDAO.getDataSet().getValue("CICLO_ID"));
		}
	}
	
	
	/**
	 * Registra el inicio de un proceso en la tabla 807
	 * @return
	 * @throws Exception
	 */
	public int comenzarProceso() throws Exception {
		//NM29643 - infi_TTS_466: Para permitir la ejecucion paralela de los hilos relacionados con el CRUCE y NO_CRUCE
		boolean running = true;
		while(running){
			procesoDAO.listarPorTransaccionActiva(proceso.getTransaId());
			if(procesoDAO.getDataSet().count() > 0) {
				logger.info("--------------------- Proceso: " + proceso.getTransaId() + " ya esta en ejecuciÛn. Intentara ejecutarse una vez finalizado el proceso en ejecucion.");
				//SE DUERME EL HILO
				try{
					
					logger.info("--------------------- Se pone en espera la ejecucion del proceso " + proceso.getTransaId()+".");
					
					synchronized(ConstantesHilos.HILO_ENVIO_CORREOS){
						//Se pone a la espera de la finalizacion del proceso de envio que se encuentra corriendo
						ConstantesHilos.HILO_ENVIO_CORREOS.wait();
					}
					
					//Thread.sleep(1000);  //El tiempo en milisegundos
					
				}catch(Exception e){
					logger.info("Error al dormir el hilo al intentar crear el proceso de tipo: " + proceso.getTransaId());
					logger.info("TRAZA: " + e.getMessage());
					return -1;
				}
			}else{
				running = false; //No hay procesos corriendo
			}
		}//while
		
		//Se ejecuta esto una vez que el hilo haya sido despertado
		try{
			//procesoDAO.insertar(proceso);
			logger.debug("-------------DESPIERTA HILO DE ENVIO DE CORREOS");
			db.exec(_dso, procesoDAO.insertar(proceso));
			logger.info("ComenzÛ proceso: " + proceso.getTransaId());	
		}catch(Exception e){
			logger.info("Error al crear el proceso de tipo: " + proceso.getTransaId());
			return -1;
		}
		return 1;
	}
	
	
	/**
	 * Registra el fin de un proceso en la tabla 807
	 */
	public void terminarProceso() {
		try {
			if (proceso != null) {
				proceso.setFechaFin(new Date());
				if (proceso.getDescripcionError() == null) {
					proceso.setDescripcionError("");
				}
				proceso.setDescripcionError(proceso.getDescripcionError());
				db.exec(this._dso, procesoDAO.modificar(proceso));
				
				//NM29643 - infi_TTS_466_Calidad 12/12/2014
				logger.debug("-------------SE DESPIERTA AL HILO DE ENVIO DE CORREOS");
				synchronized(ConstantesHilos.HILO_ENVIO_CORREOS){
					ConstantesHilos.HILO_ENVIO_CORREOS.notifyAll(); //Se despierta al hilo en espera para ejecutar el proceso de envio de correos
				}
			}
			if (procesoDAO != null) {
				procesoDAO.cerrarConexion();
			}
		} catch (Exception e) {
			logger.error("Excepcion ocurrida al cerrar el proceso de tipo "+proceso.getTransaId()+": "+e.getMessage());
		} finally {
			if (proceso != null) {
				final long duracion = proceso.getFechaFin().getTime() - proceso.getFechaInicio().getTime();
				logger.info("Termino proceso: " + proceso.getTransaId() + ", duracion: " + (duracion / 1000) + " secs.");
			}
		}
	}
	
	
	/**
	 * Inserta correos en la tabla INFI_TB_228_ENVIO_MAIL 
	 * @param correos: Lista de correos a insertar
	 * @return boolean indicando si se insertaron los correos
	 * @throws Exception
	 */
	public synchronized boolean insertarCorreos(ArrayList<EnvioMail> correos) throws Exception {
		
		boolean insert = false;
		EnvioMailDAO emDAO = new EnvioMailDAO(_dso);
		ArrayList<String> querysEjecutar = new ArrayList<String>();
		
		for(int i=0; i<correos.size(); i++){
			
			//SE INSERTA EL CICLO
			if(i==0){ //Si se trata del primer correo
				querysEjecutar.add(insertCiclo); //Se agrega la insercion del ciclo al arreglo de inserciones en la tabla de correos
//logger.debug("SQL CORREOS INSERCION:\n"+querysEjecutar.get(i));
			}
			
			//Se agrega el script de insercion
			querysEjecutar.add(emDAO.insertarCorreos(correos.get(i)));
			
			if( ((i+1)%100==0) || (i==correos.size()-1 && querysEjecutar.size()>1) ){ //Cada 100 correos se inserta en la tabla, si son menos de 100 o si son las restantes
				//Se inserta en la tabla 228
				if(emDAO.ejecutarStatementsBatchBool(querysEjecutar)){
					querysEjecutar.clear();
					querysEjecutar = new ArrayList<String>();
					insert = true;
logger.debug("Ejecuto inserts en 228!");
				}else{
					i = correos.size(); //para que no vuelva a entrar al ciclo
					querysEjecutar.clear();
					proceso.setDescripcionError("Error al insertar los correos en la base de datos");
					logger.error(proceso.getTransaId()+": Error al insertar los correos en la base de datos");
				}
			}
		}//for
		
		return insert;
	}
	
	/**
	 * Elimina correos de la tabla INFI_TB_228_ENVIO_MAIL 
	 * @param ciclo: ID del ciclo asociado al correo
	 * @param estatus: Estatus de correos a borrar
	 * @param idCorreos: ID de los correos a borrar
	 * @return boolean indicando si se insertaron los correos
	 * @throws Exception
	 */
	public boolean borrarCorreos(long cicloId, String status, boolean NOT, String... idCorreos) throws Exception {
		
		boolean deleted = true;
		EnvioMailDAO emDAO = new EnvioMailDAO(_dso);
		ArrayList<String> querysEjecutar = new ArrayList<String>();
		
		querysEjecutar.add(emDAO.eliminarCorreos(cicloId, status, NOT, idCorreos));
		
		if(!emDAO.ejecutarStatementsBatchBool(querysEjecutar)){
			deleted = false;
			proceso.setDescripcionError(proceso.getDescripcionError()+" Error al eliminar los correos en la base de datos");
			logger.error(proceso.getTransaId()+": Error al eliminar los correos en la base de datos");
		}
		
		return deleted;
	}
	
	/**
	 * Actualiza correos en la tabla INFI_TB_228_ENVIO_MAIL 
	 * @param em: Correos a actualizar
	 * @return boolean indicando si se actualizo el correo
	 * @throws Exception
	 */
	public synchronized boolean actualizarCorreo(EnvioMail correo, ArrayList<EnvioMail> correos, boolean fechaEnvio, String statusAsetear) throws Exception {
		
		boolean update = true;
		EnvioMailDAO emDAO = new EnvioMailDAO(_dso);
		ArrayList<String> querysEjecutar = new ArrayList<String>();
		
		if(correos!=null){
			for(int i=0; i<correos.size(); i++){
				//Se agrega el script de actualizacion
				querysEjecutar.add(emDAO.updateStatusCorreo(correos.get(i),fechaEnvio,statusAsetear));
			}
		}else{
			if(correo!=null){
				//Se agrega el script de actualizacion
				querysEjecutar.add(emDAO.updateStatusCorreo(correo,fechaEnvio,statusAsetear));
			}
		}
		
		//Se actualiza en la tabla 228
		if(emDAO.ejecutarStatementsBatchBool(querysEjecutar)){
			querysEjecutar.clear();
			querysEjecutar = new ArrayList<String>();
logger.debug("Ejecuto update en 228!");
		}else{
			update = false;
			if(correo!=null){
				proceso.setDescripcionError("Error al actualizar correo (ID "+correo.getIdCorreo()+") al estatus "+statusAsetear+" en la base de datos");
				logger.error(proceso.getTransaId()+": Error al actualizar correo (ID "+correo.getIdCorreo()+") al estatus "+statusAsetear+" en la base de datos");
			}else{
				proceso.setDescripcionError("Error al actualizar correos en la base de datos");
				logger.error(proceso.getTransaId()+": Error al actualizar correos en la base de datos");
			}
		}
		
		return update;
	}
	
	/**
	 * Registra la auditoria del proceso de carga
	 * @throws Exception 
	 * @throws Exception 
	 */
	public void registrarAuditoria(Proceso proc, Ciclo cic) {
		com.bdv.infi.dao.Transaccion transaccion = new com.bdv.infi.dao.Transaccion(_dso);
		ArrayList<String> querys=new ArrayList<String>();
		AuditoriaDAO auditoriaDAO = new AuditoriaDAO(_dso);
		
		logger.info("Registrando auditoria del proceso de envio de correos...");
			try {
				///********REGISTRAR LA AUDITORIA DE LA PETICI”N DE LLAMADA AL REPROCESO DEL CIERRE DEL SISTEMA****///
				//Configuracion del objeto para el proceso de auditoria
				Auditoria auditoria = new Auditoria();
				auditoria.setDireccionIp(ip);
				auditoria.setFechaAuditoria(Utilitario.DateToString(new Date(),ConstantesGenerales.FORMATO_FECHA));
				
//				logger.debug("Ciclo: "+cic+" Proceso: "+proc);
				if(cic!=null) auditoria.setUsuario(cic.getUsuarioRegistro());			
				if(proc!=null) auditoria.setPeticion(proc.getTransaId());
				if(proc!=null) auditoria.setDetalle(proc.getDescripcionError());	
				
				querys.add(auditoriaDAO.insertRegistroAuditoria(auditoria));
				auditoriaDAO.ejecutarStatementsBatch(querys);
				
				logger.debug("Ejecuta registro de auditoria");
				
			}catch(Exception ex){
				try {
					transaccion.rollback();
				} catch (Exception e) {
					logger.error("Ha ocurrido un error registrando la auditorÌa del proceso de cierre : " + ex.getMessage());
				}
			}finally{	
				try{
					if(transaccion.getConnection()!=null){
						transaccion.getConnection().close();
					}
				}catch (Exception e) {
					logger.error("Ha ocurrido un error registrando la auditorÌa del proceso de cierre : " + e.getMessage());
				}
			}	
	}
	
	
	public Proceso getProceso() {
		return proceso;
	}

	public void setProceso(Proceso proceso) {
		this.proceso = proceso;
	}
	
	public Ciclo getCiclo() {
		return ciclo;
	}

	public void setCiclo(Ciclo ciclo) {
		this.ciclo = ciclo;
	}
	
	public DataSet getInfoCorreos(){
		return infoCorreos;
	}
	
	public void setInfoCorreos(DataSet infoCorreos) {
		this.infoCorreos = infoCorreos;
	}
	
	public String getTipoDestinatario() {
		return tipoDestinatario;
	}

	public void setTipoDestinatario(String tipoDestinatario) {
		this.tipoDestinatario = tipoDestinatario;
	}
	
	public ArrayList<EnvioMail> getCorreos() {
		return correos;
	}

	public void setCorreos(ArrayList<EnvioMail> correos) {
		this.correos = correos;
	}
	
	public String getIdsCruce(){
    	return this.idsCruce;
    }
    
    public void setIdsCruce(String idsCruce){
    	this.idsCruce = idsCruce;
    }
    
	
}


package models.intercambio.operaciones_DICOM.operaciones_liquidadas;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;

import javax.sql.DataSource;

import megasoft.AppProperties;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;

import org.apache.log4j.Logger;
//import org.bcv.serviceDICOMInterbancario.OperCamFileTransferPortBindingStub;
import org.bcv.serviceDicomMulti.OperCamFileTransferPortBindingStub;

import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.UsuariosEspecialesDAO;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaz_operaciones_DICOM.EnvioOperacionesPreaprobadasDICOM;
import com.bdv.infi.util.FileUtil;
import com.bdv.infi.util.Utilitario;

public class Enviar extends MSCModelExtend {

	private String nroJornada;
	private String nombreArchivo;
	private String rutaRespaldoArchivo;
	private String transaccionEjecutar;
	private static final Logger logger = Logger.getLogger(Enviar.class);
	private ProcesosDAO procesoDAO;
	private Proceso proceso;
	private String resultadoEnvio="";
	private String TRAN="";
	public String monedaSubasta="";
	public String codi_moneda="";
	
	protected HashMap<String, String> parametrosRecepcionDICOM;
	int idUser=1;
	int monedaInt;
	int constatemoneda;
	String modenaSubastaDicom="";	
	//protected HashMap<String, String> parametrosRecepcionDICOM;

	public void execute() throws Exception {
		
		File archivoEnviado=null;
		try {
			monedaSubasta=parametrosRecepcionDICOM.get(ParametrosSistema.MONEDA_SUBASTA_DICOM);//nm36635
			monedaInt= Integer.parseInt(monedaSubasta.trim());
			constatemoneda= Integer.parseInt(ConstantesGenerales.CODIGO_MONEDA_ISO_USD_DICOM);
			codi_moneda=parametrosRecepcionDICOM.get(monedaSubasta);
			
			getUserName();
			transaccionEjecutar = getTransaccionNegocio();
			System.out.println("transaccionEjecutar--->"+transaccionEjecutar);
			comenzarProceso(idUser,transaccionEjecutar);
			if (proceso!=null) {
		    	DataSource dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));
				transaccionEjecutar = getTransaccionNegocio();
		    	obtenerParametros(dso);
				OperCamFileTransferPortBindingStub stub = new OperCamFileTransferPortBindingStub(null,_dso);
				byte[] _bytes = Utilitario.fileToByte(nombreArchivo);
				System.out.println("cargarRespuestaComprobacion " + "Jornada=" + nroJornada + "Moneda=" + modenaSubastaDicom);				
				resultadoEnvio=stub.cargarFinalOperaciones(_bytes, nroJornada, /*modenaSubastaDicom<---revisar*/monedaSubasta);
				
				archivoEnviado=new File(nombreArchivo);
				
				respaldarArchivo(archivoEnviado, true, rutaRespaldoArchivo+getNombreArchivoLiquidacionRespaldo(nroJornada));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resultadoEnvio="Resultado Envio BCV: "+resultadoEnvio+", Error en envio cargarFinalOperaciones "+e.getMessage();
		}finally {			
			terminarProceso();
			logger.info("Terminado el proceso de envio de archivo a BCV via WS cargarFinalOperaciones ... ");
		}
		
	}

	public boolean isValid() throws Exception {
		boolean valido = true;
		
		try {
			UsuariosEspecialesDAO userEspecialDAO = new UsuariosEspecialesDAO(_dso);	
			idUser=Integer.parseInt((userEspecialDAO.idUserSession(getUserName())));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.info("Falla en la consulta del id de usuario ");
			e.printStackTrace();
		}
				
		nroJornada=_record.getValue("nro_jornada");
		nombreArchivo=_record.getValue("nombre_archivo");
		rutaRespaldoArchivo=_record.getValue("ruta_respaldo_archivo");
		
		if(nroJornada==null||nroJornada.length()==0){
			_record.addError("Numero de Jornada", "Se requiere el numero de jornada " );
			valido = false;
		}
		
		if(nombreArchivo==null||nombreArchivo.length()==0){
			_record.addError("Nombre de Archivo", "Se requiere el nombre del archivo" );
			valido = false;
		}
		
		if(rutaRespaldoArchivo==null||rutaRespaldoArchivo.length()==0){
			_record.addError("Ruta de Respaldo de Archivo", "Se requiere la ruta de respaldo del archivo" );
			valido = false;
		}
		
		return valido;
	}
	
	protected File getArchivo(String ruta) {
		return new File(ruta);
	}
	
	protected String getTransaccionNegocio(){
		String transsaciones="";
		if(monedaInt==constatemoneda){
			transsaciones = TransaccionNegocio.PROC_ENVIO_DICOM;
		}else{
			transsaciones =TransaccionNegocio.PROC_ENVIO_DICOM+codi_moneda;
		}
		
		return transsaciones;	
	}
	
	public Proceso comenzarProceso(int usuarioId,String... tipoProceso) throws Exception {	
		proceso= new Proceso();
		String procesoEjecucion=new String();		
		StringBuffer procesoValidacion=new StringBuffer();		
		procesoDAO = new ProcesosDAO(_dso);
		
		if(tipoProceso.length>0){
			procesoEjecucion=tipoProceso[0];//El primer parametro que se pasa es del proceso que se esta ejecutando			
			int count=0;
			for (String element : tipoProceso) {
				if(count>0){
					procesoValidacion.append(",");									
				}							
				procesoValidacion.append("'"+element+"'");
				++count;
			}
		}
		
		procesoDAO.listarPorTransaccionActivaSubastaDivisasSubastaTitulo(procesoValidacion.toString());

		if (procesoDAO.getDataSet().count() > 0) {
			logger.info("Proceso: " + tipoProceso + " ya esta en ejecución.");
			proceso=null;
			return null;
		}
		logger.info("TIPO PROCESO EN EJECUCION -----------------> " + procesoEjecucion + " -------------------> ");
		proceso = new Proceso();
		proceso.setTransaId(procesoEjecucion);
		proceso.setUsuarioId(usuarioId);
		proceso.setFechaInicio(new Date());
		proceso.setFechaValor(new Date());
		
		logger.info("TIPO PROCESO INSERTADO: " + proceso.getTransaId() + " -------------------> ");	
		db.exec(_dso, procesoDAO.insertar(proceso));

		logger.info("Comenzó proceso: " + tipoProceso);
		return proceso;
	}

	protected void terminarProceso() {
		resultadoEnvio="ENVIO BCV cargarFinalOperaciones RESULTADO: "+resultadoEnvio;
		
		try {
			if (proceso != null) {
				proceso.setFechaFin(new Date());

				if (resultadoEnvio!= null) {
					proceso.setDescripcionError(resultadoEnvio);
				}else{
					proceso.setDescripcionError("");
				}
				db.exec(this._dso, procesoDAO.modificar(proceso));

				procesoDAO.cerrarConexion();
			}
		} catch (Exception e) {
			logger.error(e);
		} finally {
			if (proceso != null) {
				final long duracion = proceso.getFechaFin().getTime() - proceso.getFechaInicio().getTime();
				logger.info("Termino proceso: " + proceso.getTransaId() + ", duracion: " + (duracion / 1000) + " secs.");
			}
		}
	}
	
	protected String getFechaHora() throws ParseException{		
		return Utilitario.DateToString(new Date(), "ddMMyyyyhhmmss");
	}
	
	protected String getNombreArchivoLiquidacionRespaldo(String jornada) throws ParseException{
		
		String nombreo="";
		if(monedaInt==constatemoneda){
		nombreo="liquidacion_envio_"+jornada+"_"+getFechaHora()+".txt";
		}else{
		nombreo="liquidacion_envio_"+jornada+"_"+getFechaHora()+codi_moneda+".txt";
		}
		System.out.println("nombreo----->"+nombreo);
		return nombreo;
		//return "liquidacion_envio_"+jornada+"_"+getFechaHora()+".txt";
	}
	
	public void respaldarArchivo(File archivo, boolean borrarArchivo, String rutaArchivoRespaldo) throws IOException{
		File destino = new File(rutaArchivoRespaldo);
		FileUtil.copiarArchivo(archivo, destino);
		if (borrarArchivo) {
			archivo.delete();
		}
	}
	/**
	 * Metodo de busqueda de parametros asociados a interfaz DICOM
	 * */
	protected void obtenerParametros(DataSource _dso) throws Exception {		
		ParametrosDAO parametrosDAO = new ParametrosDAO(_dso);		  
		parametrosRecepcionDICOM=parametrosDAO.buscarParametros(ParametrosSistema.INTERFACE_DICOM);
	}		

	
}

package models.intercambio.operaciones_DICOM.operaciones_verificadas;

import java.io.File;
import java.util.Date;
import java.util.HashMap;

import javax.sql.DataSource;

import megasoft.AppProperties;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;

import org.apache.log4j.Logger;
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
	private String transaccionEjecutar;
	private static final Logger logger = Logger.getLogger(Enviar.class);
	private ProcesosDAO procesoDAO;
	private Proceso proceso;
	private String resultadoEnvio="";
	int idUser=1;
	String modenaSubastaDicom="";	
	protected HashMap<String, String> parametrosRecepcionDICOM;
	public void execute() throws Exception {
		try {
	    	DataSource dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));
			transaccionEjecutar = TransaccionNegocio.PROC_ENVIO_DICOM;
			comenzarProceso(1,transaccionEjecutar);
			if (proceso!=null) {
		    	obtenerParametros(dso);
				modenaSubastaDicom=parametrosRecepcionDICOM.get(ParametrosSistema.MONEDA_SUBASTA_DICOM);
				OperCamFileTransferPortBindingStub stub = new OperCamFileTransferPortBindingStub(null,_dso);
				byte[] _bytes = Utilitario.fileToByte(FileUtil.getRootWebApplicationPath() + nombreArchivo);				
				//logger.info("_bytes: "+Utilitario.openFileToString(_bytes));
			System.out.println("cargarRespuestaComprobacion " + "Jornada=" + nroJornada + "Moneda=" + modenaSubastaDicom);
				//resultadoEnvio=stub.cargarResultadoComprobacion(_bytes, nroJornada);
				resultadoEnvio=stub.cargarRespuestaComprobacion(_bytes, nroJornada, modenaSubastaDicom); 
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resultadoEnvio="Error en envio stub.cargarResultadoComprobacion"+e.getMessage();
		}finally {			
			terminarProceso();
			logger.info("Terminado el proceso de envio de archivo a BCV via WS cargarResultadoComprobacion ... ");
		}
		
	}
	
	/**
	 * Metodo de busqueda de parametros asociados a interfaz DICOM
	 * */
	protected void obtenerParametros(DataSource _dso) throws Exception {		
		ParametrosDAO parametrosDAO = new ParametrosDAO(_dso);		  
		parametrosRecepcionDICOM=parametrosDAO.buscarParametros(ParametrosSistema.INTERFACE_DICOM);
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
		
		if(nroJornada==null||nroJornada.length()==0){
			_record.addError("Numero de Jornada", "Se requiere el numero de jornada " );
			valido = false;
		}
		
		if(nombreArchivo==null||nombreArchivo.length()==0){
			_record.addError("Nombre de Archivo", "Se requiere el nombre del archivo" );
			valido = false;
		}
		
		return valido;
	}
	
	protected File getArchivo(String ruta) {
		return new File(ruta);
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
		
		resultadoEnvio="ENVIO BCV cargarResultadoComprobacion RESULTADO: "+resultadoEnvio;
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
	
}

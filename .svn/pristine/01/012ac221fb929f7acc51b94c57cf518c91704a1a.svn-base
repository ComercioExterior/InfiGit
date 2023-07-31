package models.intercambio.certificados_ORO.operaciones_interbancarias;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;
import org.apache.log4j.Logger;
import org.bcv.serviceDICOMInterbancario.OperCamFileTransferPortBindingStub;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.util.FileUtil;
import com.bdv.infi.util.Utilitario;

public class Browse extends MSCModelExtend {
	static final Logger logger = Logger.getLogger(Browse.class);
	private String nroJornada;
	//private String coMoneda = "840";
	public String monedaSubasta="";
	File archivoRecibido=null;
	//private String tipoTransferencia;
	private String rutaRecepcion="";
	ParametrosDAO parametrosDAO;
	protected HashMap<String, String> parametrosRecepcionORO;
	UnidadInversionDAO unidadInvDAO;
	
	File archivo;
	String mensajeResultado=null;
	String displayMessage="none";
	String displayButton="block";
	public final SimpleDateFormat sdfArchivoRespaldo = new SimpleDateFormat("ddMMyyyyHHmmss");
	DataSet parametros=new DataSet();
	public void execute() throws Exception {
						
		try {
			// NM11383 Busqueda de los parametros de DICOM
			obtenerParametros();
			rutaRecepcion = parametrosRecepcionORO.get(ParametrosSistema.RUTA_OP_INTERBANCARIAS_ORO_RECEP);
			monedaSubasta=parametrosRecepcionORO.get(ParametrosSistema.MONEDA_SUBASTA_ORO);
			rutaRecepcion=rutaRecepcion+getNombreArchivoRecepcion(nroJornada);
			
			archivoRecibido=getArchivo(rutaRecepcion);			
						
			if (archivoRecibido.exists()) {
				respaldarArchivo(archivoRecibido,true);				
			} 			
			parametros.append("display_message", java.sql.Types.VARCHAR);
			parametros.append("display_button", java.sql.Types.VARCHAR);
			parametros.append("nombre_archivo", java.sql.Types.VARCHAR);				
			parametros.append("nro_jornada", java.sql.Types.VARCHAR);
			// NM11383
			parametros.append("co_moneda", java.sql.Types.VARCHAR);			
			parametros.append("mensaje_resultado", java.sql.Types.VARCHAR);
			parametros.addNew();
			parametros.setValue("display_message",displayMessage);
			parametros.setValue("display_button",displayButton);
			parametros.setValue("nombre_archivo", rutaRecepcion);			
			//parametros.setValue("nro_jornada", nroJornada);
			//parametros.setValue("co_moneda", coMoneda);			
			parametros.setValue("mensaje_resultado","");
			
			OperCamFileTransferPortBindingStub stub = new OperCamFileTransferPortBindingStub(null,_dso);				 
			byte[] _bytes;										
			_bytes=stub.bajarOrdenTransferenciasInterbancarias(nroJornada,monedaSubasta);							
			logger.info("OPERACIONES INTERBANCARIAS -> Despues de Consumir el servicio bajarOrdenTransferenciasInterbancarias");							
			//SE ESCRIBE EL ARCHIVO PROVISIONALMENTE EN EL SERVIDOR 														
			Utilitario.byteToFile(_bytes,rutaRecepcion);			
				
			archivo=getArchivo(rutaRecepcion);
			
			//logger.info("OPERACIONES INTERBANCARIAS -> Antes de Consumir el servicio bajarOrdenTransferenciasInterbancarias. Jornada: "+		nroJornada);
		}catch (Exception ex){
			displayMessage="block";
			displayButton="none";
			mensajeResultado="Ha ocurrido un error en el proceso de Exportacion de Operaciones Interbancarias. Por favor verifique la Jornada seleccionada ";
			parametros.setValue("mensaje_resultado", mensajeResultado);
			parametros.setValue("display_message",displayMessage);
			parametros.setValue("display_button",displayButton);
			
			logger.error("Error ocurrido en proceso de exportacion de archivo OPERACIONES INTERBANCARIAS ---> " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			storeDataSet("parametros", parametros);
		}
	}

	protected String getNombreArchivoRecepcion(String jornada) throws ParseException{		
		return "operacionesInterbancarias"+jornada+".txt";
	}
	
	/**
	 * Metodo de busqueda de parametros asociados a interfaz con Moneda Extranjera
	 * */
	protected void obtenerParametros() throws Exception {
		//super.obtenerParametros();
		parametrosDAO = new ParametrosDAO(_dso);		
		//TTS-537 Busqueda de parametros de transacciones a moneda extranjera para cancelacion de operaciones vencidas de Convenio 36 NM26659 28-12-2016  
		parametrosRecepcionORO=parametrosDAO.buscarParametros(ParametrosSistema.INTERFACE_ORO);
		
	}
	
	protected File getArchivo(String ruta) {
		return new File(ruta);
	}
	
	public boolean isValid() throws Exception {
		boolean valido = true;
				
		nroJornada=_record.getValue("nro_jornada");				
		if(nroJornada==null||nroJornada.length()==0){
			_record.addError("Numero de Jornada", "Debe ingresar el numero de jornada " );
			valido = false;
		}					
		
		/*unidadInvDAO=new UnidadInversionDAO(_dso);
		unidadInvDAO.listaUnidadInversionPorIdJornada(nroJornada);		
		if(unidadInvDAO.getDataSet().count()==0){//No existen unidades de inversion asociadas a la jornada ingresada			
			_record.addError("Jornada ingresada no se encuentra registrada en el sistema", "Estimado usuario no hay informacion asociada al numero de jornada ingresada" );
			valido = false;
		}*/
		return valido;
	}
	
	protected void respaldarArchivo(File archivo, boolean borrarArchivo) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("carpetaRespaldo: " + getCarpetaRespaldo());
		}
		logger.info("PROCESO DE RESPALDO DE ACHIVO ----------->");
		final String carpeta = getCarpetaRespaldo();

		final File carpetaRespaldo = new File(carpeta);
		carpetaRespaldo.mkdirs();

		// agregar fecha/hora al nombre del archivo respaldo
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(archivo.getName().substring(0, archivo.getName().length() - 4)); // nombre

		stringBuilder.append(sdfArchivoRespaldo.format(new Date()));
		stringBuilder.append(archivo.getName().substring(archivo.getName().length() - 4)); // extension

		File destino = new File(carpeta.concat(stringBuilder.toString()));

		logger.info("Respaldar: " + archivo.getAbsolutePath() + " a: " + destino.getAbsolutePath());

		FileUtil.copiarArchivo(archivo, destino);
		// if (archivo.renameTo(destino)) {
		if (borrarArchivo) {
			archivo.delete();
		}
		// }
	}
	
	protected String getCarpetaRespaldo() {
		String carpeta = parametrosRecepcionORO.get(ParametrosSistema.RUTA_OP_INTERBANCARIAS_ORO_RESP);
		if(!carpeta.endsWith(File.separator)){
			carpeta = carpeta.concat(File.separator);
		}
		return carpeta;
	}
}

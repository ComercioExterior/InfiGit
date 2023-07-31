package models.intercambio.operaciones_DICOM.operaciones_liquidadas;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import org.apache.log4j.Logger;

import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.logic.interfaces.ParametrosSistema;

public class Previo2 extends MSCModelExtend {
	static final Logger logger = Logger.getLogger(Previo.class);
	private String nroJornada;
	private String cod_moneda;
	
	public void execute() throws Exception {
		String nombreArchivo=null;
		String rutaRespaldoArchivo=null;
		DataSet datosArchivo = new DataSet();
		String mensajeError = " ";		
		
		ParametrosDAO parametrosDAO = new ParametrosDAO(_dso);
		/*FormatoOperacionesDICOM formatoArchivoRecepcion=new FormatoOperacionesDICOM();
		SolicitudesDICOMDAO solicitudesDICOMDAO=new SolicitudesDICOMDAO(_dso);
		ArrayList<String> registrosDeArchivoDefinitivo = new ArrayList<String>();
			
		//CONSULTAR OPERACIONES VERIFICADAS
		solicitudesDICOMDAO.listarSolicitudesVerificadasDICOM(nroJornada, "NRO_SOLICITUD,CODIGO_RESPUESTA",ConstantesGenerales.ESTATUS_REGISTRO_PROCESADO_DICOM);
		
		//GENERAR ARCHIVO DE OPERACIONES DICOM VERIFICADAS		
		if(solicitudesDICOMDAO.getDataSet().count()>0){
			solicitudesDICOMDAO.getDataSet().first();
			while (solicitudesDICOMDAO.getDataSet().next()) {
				formatoArchivoRecepcion.setIdOperacion((solicitudesDICOMDAO.getDataSet().getValue("NRO_SOLICITUD")));
				formatoArchivoRecepcion.setRespuestaArchivo(solicitudesDICOMDAO.getDataSet().getValue("CODIGO_RESPUESTA"));
				registrosDeArchivoDefinitivo.add(formatoArchivoRecepcion.crearCuerpoOpVerificadas());
			}
		}		
		
		//CREAR ARCHIVO EN RUTA PROVISIONAL		
		logger.info("Escribiendo en archivo " + nombreArchivo);		 
		FileUtil.put(FileUtil.getRootWebApplicationPath()+nombreArchivo, registrosDeArchivoDefinitivo, true);*/
		nombreArchivo=parametrosDAO.listarParametrosReturn(ParametrosSistema.RUTA_DICOM_LIQUIDAC_ENVIO, ParametrosSistema.INTERFACE_DICOM_ID);
		//rutaRespaldoArchivo=parametrosDAO.listarParametrosReturn(ParametrosSistema.RUTA_DICOM_RESP, ParametrosSistema.INTERFACE_DICOM_ID);
		rutaRespaldoArchivo=parametrosDAO.listarParametrosReturn(ParametrosSistema.RUTA_DICOM_LIQUIDAC_RESP, ParametrosSistema.INTERFACE_DICOM_ID);
System.out.println("Archivo=" + nombreArchivo + "  RutaResp = " + rutaRespaldoArchivo);		
		//nombreArchivo="liquidadas"+Utilitario.DateToString(new Date(), ConstantesGenerales.FORMATO_FECHA_FILE)+"_"+nroJornada+".txt";
		
		datosArchivo.append("ruta_respaldo_archivo", java.sql.Types.VARCHAR);
		datosArchivo.append("nombre_archivo", java.sql.Types.VARCHAR);
		datosArchivo.append("display", java.sql.Types.VARCHAR);
		datosArchivo.append("msg", java.sql.Types.VARCHAR);
		datosArchivo.append("nro_jornada", java.sql.Types.VARCHAR);
		datosArchivo.append("cod_moneda", java.sql.Types.VARCHAR);
		datosArchivo.addNew();
		datosArchivo.setValue("ruta_respaldo_archivo", rutaRespaldoArchivo);
		datosArchivo.setValue("nombre_archivo", nombreArchivo);
		datosArchivo.setValue("display", "block");
		datosArchivo.setValue("nro_jornada", nroJornada);
		datosArchivo.setValue("cod_moneda", cod_moneda);
		
		mensajeError="Generacion de Archivo Exitosa";
		
		datosArchivo.setValue("msg", mensajeError);
		storeDataSet("datosArchivo", datosArchivo);
	}

	public boolean isValid() throws Exception {
		boolean valido = true;
		nroJornada=_record.getValue("nro_jornada");
		cod_moneda=_record.getValue("cod_moneda");
		if(nroJornada==null||nroJornada.length()==0){
			_record.addError("Numero de Jornada", "Debe ingresar el numero de jornada " );
			valido = false;
		}
		
		return valido;
	}
}

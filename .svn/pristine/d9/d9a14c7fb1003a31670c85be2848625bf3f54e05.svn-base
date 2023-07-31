package models.intercambio.certificados_ORO.operaciones_verificadas;

import java.util.ArrayList;
import java.util.Date;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import org.apache.log4j.Logger;

import com.bdv.infi.dao.SolicitudesDICOMDAO;
import com.bdv.infi.dao.SolicitudesORODAO;
import com.bdv.infi.data.FormatoOperacionesDICOM;
import com.bdv.infi.data.FormatoOperacionesORO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.util.FileUtil;
import com.bdv.infi_toma_orden.util.Utilitario;

public class Previo extends MSCModelExtend {
	static final Logger logger = Logger.getLogger(Previo.class);
	private String nroJornada;
	public void execute() throws Exception {
		
		FormatoOperacionesORO formatoArchivoRecepcion=new FormatoOperacionesORO();
		SolicitudesORODAO solicitudesORODAO=new SolicitudesORODAO(_dso);
		ArrayList<String> registrosDeArchivoDefinitivo = new ArrayList<String>();
		String mensajeError = " ";		
		String nombreArchivo=null;
		DataSet datosArchivo = new DataSet();
		
		//CONSULTAR OPERACIONES VERIFICADAS
		//**************************************************REVISAR SI HAY QUE AGREGAR LA MONEDA PARA FILTRAR 
		solicitudesORODAO.listarSolicitudesVerificadasORO(nroJornada, "NRO_SOLICITUD,CODIGO_RESPUESTA",ConstantesGenerales.ESTATUS_REGISTRO_PROCESADO_ORO);
		
		//GENERAR ARCHIVO DE OPERACIONES ORO VERIFICADAS
		
		if(solicitudesORODAO.getDataSet().count()>0){
			solicitudesORODAO.getDataSet().first();
			while (solicitudesORODAO.getDataSet().next()) {
				formatoArchivoRecepcion.setIdOperacion((solicitudesORODAO.getDataSet().getValue("NRO_SOLICITUD")));
				formatoArchivoRecepcion.setRespuestaArchivo(solicitudesORODAO.getDataSet().getValue("CODIGO_RESPUESTA"));
				registrosDeArchivoDefinitivo.add(formatoArchivoRecepcion.crearCuerpoOpVerificadas());
			}
		}		
		
		//CREAR ARCHIVO EN RUTA PROVISIONAL
		nombreArchivo="preaprobadas"+Utilitario.DateToString(new Date(), ConstantesGenerales.FORMATO_FECHA_FILE)+"_"+nroJornada+".txt";
		

		logger.info("Escribiendo en archivo " + nombreArchivo);		 
		FileUtil.put(FileUtil.getRootWebApplicationPath()+nombreArchivo, registrosDeArchivoDefinitivo, true);
				
		datosArchivo.append("nombre_archivo", java.sql.Types.VARCHAR);
		datosArchivo.append("display", java.sql.Types.VARCHAR);
		datosArchivo.append("msg", java.sql.Types.VARCHAR);
		datosArchivo.append("nro_jornada", java.sql.Types.VARCHAR);
		datosArchivo.addNew();
		datosArchivo.setValue("nombre_archivo", nombreArchivo);
		datosArchivo.setValue("display", "block");
		datosArchivo.setValue("nro_jornada", nroJornada);
		
		mensajeError="Generacion de Archivo Exitosa";
		
		datosArchivo.setValue("msg", mensajeError);
		storeDataSet("datosArchivo", datosArchivo);
	}

	public boolean isValid() throws Exception {
		boolean valido = true;
		nroJornada=_record.getValue("nro_jornada");
		
		if(nroJornada==null||nroJornada.length()==0){
			_record.addError("Numero de Jornada", "Debe ingresar el numero de jornada " );
			valido = false;
		}
		
		return valido;
	}
}

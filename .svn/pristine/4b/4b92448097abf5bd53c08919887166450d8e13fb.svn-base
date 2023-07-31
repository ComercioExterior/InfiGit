package models.intercambio.operaciones_DICOM.operaciones_liquidadas;
import java.util.ArrayList;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import org.apache.log4j.Logger;

import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.SolicitudesDICOMDAO;
import com.bdv.infi.data.FormatoOperacionesDICOM;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.util.FileUtil;

public class Previo extends MSCModelExtend {
	static final Logger logger = Logger.getLogger(Previo.class);
	private String nroJornada;
	private boolean soloOpLiquidadas;
	//private String tipoTransferencia;
	
	public void execute() throws Exception {
		String nombreArchivo=null;
		String rutaRespaldoArchivo=null;
		DataSet datosArchivo = new DataSet();
		String mensajeError = " ";	
		String display= "block";
		
		ParametrosDAO parametrosDAO = new ParametrosDAO(_dso);
		FormatoOperacionesDICOM formatoArchivoRecepcion=new FormatoOperacionesDICOM();
		SolicitudesDICOMDAO solicitudesDICOMDAO=new SolicitudesDICOMDAO(_dso);
		ArrayList<String> registrosDeArchivoDefinitivo = new ArrayList<String>();
			
		//TODO CONSULTA DEFINITIVA PARA LIQUIDACION DESDE INFI
		//CONSULTAR OPERACIONES LIQUIDADAS
		solicitudesDICOMDAO.listarSolicitudesLiquidadasDICOM(nroJornada, "NRO_SOLICITUD,NRO_OPE_DEBITO,NRO_OPE_CREDITO",ConstantesGenerales.ESTATUS_REGISTRO_PROCESADO_DICOM,soloOpLiquidadas);
		
		//GENERAR ARCHIVO DE OPERACIONES DICOM LIQUIDADAS		
		if(solicitudesDICOMDAO.getDataSet().count()>0){
			solicitudesDICOMDAO.getDataSet().first();
			while (solicitudesDICOMDAO.getDataSet().next()) {
				formatoArchivoRecepcion.setIdOperacion(solicitudesDICOMDAO.getDataSet().getValue("NRO_SOLICITUD"));
				formatoArchivoRecepcion.setNroOperacionDebito(solicitudesDICOMDAO.getDataSet().getValue("NRO_OPE_DEBITO"));
				formatoArchivoRecepcion.setNroOperacionCredito(solicitudesDICOMDAO.getDataSet().getValue("NRO_OPE_CREDITO"));
				registrosDeArchivoDefinitivo.add(formatoArchivoRecepcion.crearCuerpoOpLiquidadas());
			}
			
			//CREAR ARCHIVO EN RUTA DE ENVIO				
			nombreArchivo=parametrosDAO.listarParametrosReturn(ParametrosSistema.RUTA_DICOM_LIQUIDAC_ENVIO, ParametrosSistema.INTERFACE_DICOM_ID);
			rutaRespaldoArchivo=parametrosDAO.listarParametrosReturn(ParametrosSistema.RUTA_DICOM_LIQUIDAC_RESP, ParametrosSistema.INTERFACE_DICOM_ID);
			
			logger.info("Escribiendo en archivo " + nombreArchivo);		
			FileUtil.put(nombreArchivo, registrosDeArchivoDefinitivo, true);
			mensajeError="Generacion de Archivo Exitosa";
		}else{
			mensajeError="No existen registros asociados a los filtros indicados";
			display="none";
		}
		
		mensajeError="Verifique el archivo a enviar";
		//TODO **** HASTA AQUI CONTINGENCIA LIQUIDACION
		
		datosArchivo.append("ruta_respaldo_archivo", java.sql.Types.VARCHAR);
		datosArchivo.append("nombre_archivo", java.sql.Types.VARCHAR);
		datosArchivo.append("display", java.sql.Types.VARCHAR);
		datosArchivo.append("msg", java.sql.Types.VARCHAR);
		datosArchivo.append("nro_jornada", java.sql.Types.VARCHAR);
		datosArchivo.append("tipo_transferencia", java.sql.Types.VARCHAR);
		datosArchivo.addNew();
		datosArchivo.setValue("ruta_respaldo_archivo", rutaRespaldoArchivo);
		datosArchivo.setValue("nombre_archivo", nombreArchivo);
		datosArchivo.setValue("display", display);
		datosArchivo.setValue("nro_jornada", nroJornada);
		
		datosArchivo.setValue("msg", mensajeError);
		storeDataSet("datosArchivo", datosArchivo);
	}

	public boolean isValid() throws Exception {
		boolean valido = true;
		String soloOpLiquidadasString;
		
		nroJornada=_record.getValue("nro_jornada");
		soloOpLiquidadasString=_record.getValue("solo_op_liquidadas");
		
		if(nroJornada==null||nroJornada.length()==0){
			_record.addError("Numero de Jornada", "Debe ingresar el numero de jornada " );
			valido = false;
		}
		if(soloOpLiquidadasString==null||soloOpLiquidadasString.length()==0){
			_record.addError("Operaciones a enviar", "Debe ingresar el tipo de operacion " );
			valido = false;
		}	else
		{
			soloOpLiquidadas=_record.getValue("solo_op_liquidadas").equals("1")?true:false;
		}
						
		
		return valido;
	}
}

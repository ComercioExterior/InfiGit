package models.intercambio.operaciones_DICOM.operaciones_interbancarias;
import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import megasoft.DataSet;
import models.exportable.ExportableOutputStream;
import models.intercambio.consultas.detalle.DetalleDeOperaciones;
import models.msc_utilitys.MSCModelExtend;

import org.apache.log4j.Logger;
import org.bcv.serviceDICOM.OperCamFileTransferPortBindingStub;

import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.SolicitudesDICOMDAO;
import com.bdv.infi.data.FormatoOperacionesDICOM;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaz_ops.ArchivoRecepcion;
import com.bdv.infi.util.FileUtil;
import com.bdv.infi.util.Utilitario;

public class Descargar extends ExportableOutputStream {
	static final Logger logger = Logger.getLogger(Descargar.class);
	private String nroJornada;
	File archivoRecibido=null;
	//private String tipoTransferencia;
	private String rutaRecepcion="";
	ParametrosDAO parametrosDAO;
	protected HashMap<String, String> parametrosRecepcionDICOM;
	DataSet parametros;
	ArchivoRecepcion archivoRecepcion;// = new ArchivoRecepcion(archivo);
	File archivo;
	String linea;
	public void execute() throws Exception {
		
		
		
		try {
			
			obtenerParametros();
			rutaRecepcion = parametrosRecepcionDICOM.get(ParametrosSistema.RUTA_CRUCE_DICOM_ENVIO);			
			archivo=getArchivo(rutaRecepcion);			
			archivoRecepcion= new ArchivoRecepcion(archivo);
			
			linea = archivoRecepcion.leerLinea();
			while(linea!=null){
				escribir(linea);
				escribir("\r\n");
				linea = archivoRecepcion.leerLinea();
			}
			//logger.error("EnvioOperacionesDICOM-> "+Utilitario.openFileToString(_bytes));
			
			parametros=new DataSet();
			parametros.append("nombre_archivo", java.sql.Types.VARCHAR);
			parametros.append("nro_jornada", java.sql.Types.VARCHAR);
			parametros.addNew();
			parametros.setValue("nombre_archivo", rutaRecepcion);
			parametros.setValue("nro_jornada", nroJornada);
			storeDataSet("param", parametros);
		}catch (Exception ex){
			
		}
		
	}

	protected String getNombreArchivoRecepcion(String jornada) throws ParseException{		
		return "operaciones_interbancarias_"+jornada+".txt";
	}
	
	/**
	 * Metodo de busqueda de parametros asociados a interfaz con Moneda Extranjera
	 * */

	protected void obtenerParametros() throws Exception {
		//super.obtenerParametros();
		parametrosDAO = new ParametrosDAO(_dso);		
		//TTS-537 Busqueda de parametros de transacciones a moneda extranjera para cancelacion de operaciones vencidas de Convenio 36 NM26659 28-12-2016  
		parametrosRecepcionDICOM=parametrosDAO.buscarParametros(ParametrosSistema.INTERFACE_DICOM);
		
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
		
						
		
		return valido;
	}
}

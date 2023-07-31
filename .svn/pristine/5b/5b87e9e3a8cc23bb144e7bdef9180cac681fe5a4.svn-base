package models.intercambio.operaciones_DICOM.operaciones_recibido;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionException;

import com.bdv.infi.dao.FechasCierresDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.UsuarioDAO;
import com.bdv.infi.data.FechasCierre;
import com.bdv.infi.logic.GenerarComisiones;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaz_operaciones_DICOM.EnvioOperacionesPreaprobadasDICOM;
import com.bdv.infi.logic.interfaz_operaciones_DICOM.RecepcionOperacionesVerificadasDICOM;
import com.bdv.infi.logic.interfaz_operaciones_ORO.RecepcionOperacionesVerificadasORO;
import com.programador.quartz.jobs.QuartzRecepcionOperacionesDICOM;

import megasoft.AppProperties;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;
/**
 * 
 * Clase encargada de genarar las comisiones. 
 * Verifica si existe un proceso de comisiones que se este ejecutando. 
 * Verifica si existen comisiones de meses anteriores que no han sido cerradas.
 *
 */


public class Browse extends MSCModelExtend {

	//private Logger logger = Logger.getLogger(QuartzRecepcionOperacionesORO.class);
	protected HashMap<String, String> parametrosRecepcionORO;
	/*String jornadaORO="";
	String modenaSubastaORO="";
	String jornadafechaInicio="";
	String jornadafechaFin="";
	String monedaSubasta="";*/
	public void execute()throws Exception{
		System.out.println("paso");
		DataSource dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));
		System.out.println("paso1");
		/*jornadaORO = parametrosRecepcionORO.get(ParametrosSistema.JORNADA_DICOM);
	modenaSubastaORO=parametrosRecepcionORO.get(ParametrosSistema.MONEDA_SUBASTA_DICOM);
	jornadafechaInicio = parametrosRecepcionORO.get(ParametrosSistema.JORNADA_FECHA_INICIO);
	monedaSubasta=parametrosRecepcionORO.get(ParametrosSistema.MONEDA_SUBASTA_DICOM);*/
	System.out.println("paso2");
	Thread t = new Thread(new RecepcionOperacionesVerificadasORO(dso));
	System.out.println("paso3");
	t.start();
	System.out.println("paso4");
	t.join();
	System.out.println("paso5");
	}
	/*MSCModelExtend me = new MSCModelExtend();
	
	Date fechaDesde = null;
	Date fechaHasta = null;*/
	
	//public void execute()throws Exception{
	
	/*	public boolean isValid()throws Exception{
	
		
			boolean valido=super.isValid();
		
			if(valido){
			if(jornadaORO.length()>3){
			try {
		//	logger.debug("Entrando a ejecucion de tareas programadas ORO");
	    	DataSource dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));
	    	
			jornadaORO = parametrosRecepcionORO.get(ParametrosSistema.JORNADA_ORO);
	    	modenaSubastaORO=parametrosRecepcionORO.get(ParametrosSistema.MONEDA_SUBASTA_ORO);
	    	jornadafechaInicio = parametrosRecepcionORO.get(ParametrosSistema.JORNADA_FECHA_INICIO);
	    	monedaSubasta=parametrosRecepcionORO.get(ParametrosSistema.MONEDA_SUBASTA_ORO);
	    	
	    	
			Thread t = new Thread(new RecepcionOperacionesVerificadasORO(dso));
			t.start();
			t.join();
		} catch (Exception e) {
		//	logger.error(e);
			throw new JobExecutionException(e);
		}
		}else{
			_record.addError("Error al enviar","Puede que el parametro este mal o " +
	         "tenga valores nulo");
			valido=false;
		}
	}
		return valido;
		
	}	*/
}
//fin de la clase

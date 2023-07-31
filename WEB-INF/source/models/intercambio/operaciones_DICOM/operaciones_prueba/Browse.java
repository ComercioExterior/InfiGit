package models.intercambio.operaciones_DICOM.operaciones_prueba;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.sql.DataSource;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
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
import com.programador.quartz.jobs.QuartzRecepcionOperacionesDICOM;

import megasoft.AppProperties;
import megasoft.DataSet;
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
	private Logger logger = Logger.getLogger(QuartzRecepcionOperacionesDICOM.class);
	protected HashMap<String, String> parametrosRecepcionDICOM;

	
	
	public void execute()throws Exception{
		/*String jornadaDicom="";
		String modenaSubastaDicom="";
		String jornadafechaInicio="";
		String jornadafechaFin="";
		String monedaSubasta="";
		try{
			logger.debug("Entrando a ejecucion de tareas programadas Envio  Operaciones DICOM INTERBANCARIO");
	    	DataSource dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));
	    	ParametrosDAO parametrosDAO = new ParametrosDAO(dso);
	    	String usuarioGenerico=parametrosDAO.listarParametros(ConstantesGenerales.USUARIO_WEB_SERVICES,dso);
	    	
	    	obtenerParametros(dso);
	    	jornadaDicom = parametrosRecepcionDICOM.get(ParametrosSistema.JORNADA_DICOM);
	    	modenaSubastaDicom=parametrosRecepcionDICOM.get(ParametrosSistema.MONEDA_SUBASTA_DICOM);
	    	jornadafechaInicio = parametrosRecepcionDICOM.get(ParametrosSistema.JORNADA_FECHA_INICIO);
	    	monedaSubasta=parametrosRecepcionDICOM.get(ParametrosSistema.MONEDA_SUBASTA_DICOM);
			//jornadafechaFin = parametrosRecepcionDICOM.get(ParametrosSistema.JORNADA_FECHA_FIN);
	    	
	    	if(jornadaDicom.length()>3 && jornadaDicom!=null && jornadafechaInicio!=null && jornadafechaFin!=null && monedaSubasta!=null){//nm36635
	    	Thread t = new Thread(new EnvioOperacionesPreaprobadasDICOM(dso,usuarioGenerico));
			t.start();
			t.join();
	    	}else{
	    		System.out.println("No paso");
	    		//JOptionPane.showMessageDialog(null, "tiene algun valor null o con menor longitud");
	    	}
		}catch(Exception e){
			logger.error(e);
			//throw new JobExecutionException(e);
		}*/
		
		
		
		
		/*// TODO Auto-generated method stub
		UsuarioDAO usu				= new UsuarioDAO(_dso);
		Runnable generarComisiones= new GenerarComisiones(_dso,fechaDesde,fechaHasta,Integer.parseInt(usu.idUserSession(getUserName())),this.getUserName(),(String)_req.getSession().getAttribute(ConstantesGenerales.CODIGO_SUCURSAL));
		new Thread(generarComisiones).start();*/
	}
	public boolean isValid()throws Exception{
		String jornadaDicom="";
		String modenaSubastaDicom="";
		String jornadafechaInicio="";
		String jornadafechaFin="";
		String monedaSubasta="";
		
		logger.debug("Entrando a ejecucion de tareas programadas Envio  Operaciones DICOM INTERBANCARIO");
    	DataSource dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));
    	ParametrosDAO parametrosDAO = new ParametrosDAO(dso);
    	String usuarioGenerico=parametrosDAO.listarParametros(ConstantesGenerales.USUARIO_WEB_SERVICES,dso);
    	
    	obtenerParametros(dso);
		
		jornadaDicom = parametrosRecepcionDICOM.get(ParametrosSistema.JORNADA_DICOM);
    	modenaSubastaDicom=parametrosRecepcionDICOM.get(ParametrosSistema.MONEDA_SUBASTA_DICOM);
    	jornadafechaInicio = parametrosRecepcionDICOM.get(ParametrosSistema.JORNADA_FECHA_INICIO);
    	monedaSubasta=parametrosRecepcionDICOM.get(ParametrosSistema.MONEDA_SUBASTA_DICOM);
		boolean valido=super.isValid();
		if(valido){
			if(jornadaDicom!=null){
				Thread t = new Thread(new EnvioOperacionesPreaprobadasDICOM(dso,usuarioGenerico));
				t.start();
				t.join();
			}else{
				_record.addError("Error al enviar","Puede que el parametro este mal o " +
		         "tenga valores nulo");
				valido=false;
			}
		
		}
		return valido;
	}
	protected void obtenerParametros(DataSource _dso) throws Exception {		
		ParametrosDAO parametrosDAO = new ParametrosDAO(_dso);		  
		parametrosRecepcionDICOM=parametrosDAO.buscarParametros(ParametrosSistema.INTERFACE_DICOM);
	}
	/**
	 * Verificar si existe un proceso de comisiones que se este ejecutando tambien, si existen comisiones de meses anteriores que no han sido cerradas
	 */
	/*public boolean isValid()throws Exception
	{
		boolean valido=super.isValid();
		FechasCierresDAO fechaCierreDAO= new FechasCierresDAO(_dso);
		FechasCierre fechasCierre= fechaCierreDAO.obtenerFechas();
		
		this.fechaHasta=fechasCierre.getFechaCierreProximo();
		//
		this.fechaDesde= new Date(fechaHasta.getYear(),fechaHasta.getMonth(),1);
		String mes;
		 Date hoy = new Date();
		 SimpleDateFormat sdf = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
			//Se convierte un string en date
			Date dateHoy = me.StringToDate(sdf.format(hoy), ConstantesGenerales.FORMATO_FECHA);
			
			if(valido)
			{
				ProcesosDAO procesosDAO=new ProcesosDAO(_dso);
				procesosDAO.listarPorTransaccionActiva(TransaccionNegocio.CUSTODIA_COMISIONES);
				if(procesosDAO.getDataSet().count()>0)				{
					_record.addError("Generación de Comisiones","No se puede procesar la solicitud porque otra " +
							         "persona realizó esta acción y esta actualmente activa");
					valido=false;
				}else{
					if(dateHoy.compareTo(fechaHasta)<=0){						
						_record.addError("Generación de Comisiones","No se puede procesar la solicitud porque" +
										 " no ha finalizado el mes");
						valido = false;
					}//fin del if
					
				}//fin del else				
			}//fin del if
		
			return valido;
	}//fin isValid*/
}//fin de la clase

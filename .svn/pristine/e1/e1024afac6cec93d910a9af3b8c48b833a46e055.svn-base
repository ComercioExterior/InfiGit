package models.intercambio.certificados_ORO.operaciones_envio;

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
import com.bdv.infi.logic.interfaz_operaciones_ORO.EnvioOperacionesORO;
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
	//private Logger logger = Logger.getLogger(QuartzRecepcionOperacionesORO.class);
	protected HashMap<String, String> parametrosRecepcionORO;
	public void execute()throws Exception{
		DataSource dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));
    	ParametrosDAO parametrosDAO = new ParametrosDAO(dso);
    	String usuarioGenerico=parametrosDAO.listarParametros(ConstantesGenerales.USUARIO_WEB_SERVICES,dso);
    	
		Thread t = new Thread(new EnvioOperacionesORO(dso,usuarioGenerico));
		t.start();
		t.join();
	}
}

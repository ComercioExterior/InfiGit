package models.intercambio.operaciones_DICOM.operaciones_recibido;

import java.util.HashMap;

import javax.sql.DataSource;

import org.quartz.JobExecutionException;

import megasoft.AppProperties;
import megasoft.DataSet;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;
import com.bdv.infi.logic.interfaz_operaciones_DICOM.RecepcionOperacionesVerificadasDICOM;

public class Filter extends MSCModelExtend {
	protected HashMap<String, String> parametrosNumeroORO;

	
	/**
	 * Ejecuta la transaccion del modelo buscando las unidades de inversión
	 */
	public void execute() throws Exception {
		ParametrosDAO parametrosDAO = new ParametrosDAO(_dso);		  
		parametrosNumeroORO=parametrosDAO.buscarParametros(ParametrosSistema.INTERFACE_ORO);
		
		
		DataSet _param1=new DataSet();
		
		_param1.append("valor", java.sql.Types.VARCHAR);
		_param1.append("mensaje", java.sql.Types.VARCHAR);
		
		_param1.addNew();
		
		_param1.setValue("valor",String.valueOf(parametrosNumeroORO.get(ParametrosSistema.JORNADA_ORO)));
		_param1.setValue("mensaje", "Los montos generados ser&aacute;n cobrados el d&iacute;a ");
		
		storeDataSet("valorjornada",_param1);
	
			}
	
	}

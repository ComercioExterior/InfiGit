package models.intercambio.operaciones_DICOM.operaciones_envio;

import java.util.HashMap;

import javax.sql.DataSource;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

public class Filter extends MSCModelExtend {
	protected HashMap<String, String> parametrosNumeroORO;
	//protected String parametrosNumeroDICOM1;
	
	/**
	 * Ejecuta la transaccion del modelo buscando las unidades de inversión
	 */
	public void execute() throws Exception {		
			ParametrosDAO parametrosDAO = new ParametrosDAO(_dso);		  
			parametrosNumeroORO=parametrosDAO.buscarParametros(ParametrosSistema.INTERFACE_ORO);
			
			
			DataSet _param=new DataSet();
			
			_param.append("valor", java.sql.Types.VARCHAR);
			_param.append("mensaje", java.sql.Types.VARCHAR);
			
			_param.addNew();
			
			_param.setValue("valor",String.valueOf(parametrosNumeroORO.get(ParametrosSistema.JORNADA_ORO)));
			_param.setValue("mensaje", "Los montos generados ser&aacute;n cobrados el d&iacute;a ");
			
			storeDataSet("valorjornada",_param);
			}
	
	}

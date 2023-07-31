package models.intercambio.operaciones_DICOM.operaciones_prueba;

import java.util.HashMap;

import javax.sql.DataSource;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

public class Filter extends MSCModelExtend {
	protected HashMap<String, String> parametrosNumeroDICOM;
	protected String parametrosNumeroDICOM1;
	
	/**
	 * Ejecuta la transaccion del modelo buscando las unidades de inversión
	 */
	public void execute() throws Exception {		
			ParametrosDAO parametrosDAO = new ParametrosDAO(_dso);		  
			parametrosNumeroDICOM=parametrosDAO.buscarParametros(ParametrosSistema.INTERFACE_DICOM);
			
			
			DataSet _param=new DataSet();
			
			_param.append("valor", java.sql.Types.VARCHAR);
			_param.append("mensaje", java.sql.Types.VARCHAR);
			
			_param.addNew();
			
			_param.setValue("valor",String.valueOf(parametrosNumeroDICOM.get(ParametrosSistema.JORNADA_DICOM)));
			_param.setValue("mensaje", "Los montos generados ser&aacute;n cobrados el d&iacute;a ");
			
			storeDataSet("valorjornada",_param);
			/*parametrosNumeroDICOM1=parametrosDAO.listarParametros(ParametrosSistema.JORNADA_DICOM,DataSource);
			_param.setValue("valor1",String.valueOf(parametrosNumeroDICOM1.getBytes(ParametrosSistema.JORNADA_DICOM)));*/
			}
	
	}

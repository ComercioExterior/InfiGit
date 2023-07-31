package models.intercambio.operaciones_DICOM.operaciones_verificadas;

import java.util.HashMap;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

public class Filter extends MSCModelExtend {
	protected HashMap<String, String> parametrosRecepcionDICOM;
	/**
	 * Ejecuta la transaccion del modelo buscando las unidades de inversión
	 */
	public void execute() throws Exception {
		
		ParametrosDAO parametrosDAO = new ParametrosDAO(_dso);		  
		parametrosRecepcionDICOM=parametrosDAO.buscarParametros(ParametrosSistema.INTERFACE_DICOM);
		
		
		DataSet _param=new DataSet();
		
		_param.append("cod_moneda", java.sql.Types.VARCHAR);
		//_param.append("mensaje", java.sql.Types.VARCHAR);
		
		_param.addNew();
		
		_param.setValue("cod_moneda",String.valueOf(parametrosRecepcionDICOM.get(ParametrosSistema.MONEDA_SUBASTA_DICOM)));
		//_param.setValue("mensaje", "Los montos generados ser&aacute;n cobrados el d&iacute;a ");
		
		storeDataSet("valorjornada",_param);
		//NM25287 - DICOM
		/*_record.setValue("tipo_prod",ConstantesGenerales.ID_TIPO_PRODUCTO_DICOM);
		_record.setValue("estatus",UnidadInversionConstantes.UISTATUS_PUBLICADA);

		storeDataSet("record", _record);*/
		
	}
}

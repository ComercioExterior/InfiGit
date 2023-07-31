package models.bcv.taquilla_aereopuerto;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

public class Filter extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		//OrdenDAO ordsta = new OrdenDAO(_dso);
		//UnidadInversionDAO uiDAO = new UnidadInversionDAO(_dso);
		Integer tipoNegocio = Integer.parseInt(ConstantesGenerales.TIPO_NEGOCIO_NORMAL);
				
		
		Calendar fechaHoy = Calendar.getInstance();
		SimpleDateFormat sdIO = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
		String hoy = sdIO.format(fechaHoy.getTime());
		
		DataSet dsFecha = new DataSet();		
		dsFecha.append("fechahoy",java.sql.Types.VARCHAR);
		dsFecha.addNew();
		dsFecha.setValue("fechahoy",hoy);

		
		_record.setValue("tipo_prod",ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL);
		_record.setValue("estatus",UnidadInversionConstantes.UISTATUS_PUBLICADA.concat(",").concat(UnidadInversionConstantes.UISTATUS_LIQUIDADA));		
		_record.setValue("forma_orden",ConstantesGenerales.INST_FINANCIERO_SIMADI_TAQUILLA);
		
		storeDataSet("record", _record);		
		storeDataSet("fechas", dsFecha);
	
	}
	
	/*public boolean isValid() throws Exception {
		boolean flag = super.isValid();	
		
		String urlInvocacion = _req.getPathInfo();//.substring(0, _req.getPathInfo().lastIndexOf("-"));		
	
		return flag;
	}*/
}

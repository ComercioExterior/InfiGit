package models.bcv.carga_ofertas;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

public class Filter extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		Calendar fechaHoy = Calendar.getInstance();
		SimpleDateFormat sdIO = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
		String hoy = sdIO.format(fechaHoy.getTime());
		
		DataSet dsFecha = new DataSet();		
		dsFecha.append("fechahoy",java.sql.Types.VARCHAR);
		dsFecha.append("tipo_prod",java.sql.Types.VARCHAR);
		dsFecha.append("estatus_ui",java.sql.Types.VARCHAR);
		dsFecha.append("tipo_negocio",java.sql.Types.VARCHAR);
		dsFecha.addNew();
		dsFecha.setValue("fechahoy",hoy);
		dsFecha.setValue("tipo_prod",ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL+","+ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL);
		dsFecha.setValue("estatus_ui",UnidadInversionConstantes.UISTATUS_PUBLICADA);
		dsFecha.setValue("tipo_negocio",ConstantesGenerales.TIPO_NEGOCIO_ALTO_VALOR);
				
		storeDataSet("record", _record);
		
		storeDataSet("fechas", dsFecha);
		
	}
	
	public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();	
		return flag;
	}
}

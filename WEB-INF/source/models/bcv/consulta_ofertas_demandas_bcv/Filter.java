package models.bcv.consulta_ofertas_demandas_bcv;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.BlotterDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

public class Filter extends MSCModelExtend {
	private DataSet datosFilter;
	private String idProducto;
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		UnidadInversionDAO uiDAO = new UnidadInversionDAO(_dso);
		BlotterDAO blotterDAO = new BlotterDAO(_dso);
		
		int tipoNegocio = Integer.parseInt(ConstantesGenerales.TIPO_NEGOCIO_ALTO_VALOR);
		uiDAO.listaUnidadesAdjudicarPorTipoProductoStatus(idProducto,tipoNegocio, StatusOrden.PROCESO_CRUCE,StatusOrden.ENVIADA);
 		storeDataSet("uniInverPublicadas", uiDAO.getDataSet());
		
 		blotterDAO.listar();
 		storeDataSet("bloter", blotterDAO.getDataSet());
 		
		Calendar fechaHoy = Calendar.getInstance();
		SimpleDateFormat sdIO = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
		String hoy = sdIO.format(fechaHoy.getTime());
		
		DataSet dsFecha = new DataSet();		
		dsFecha.append("fechahoy",java.sql.Types.VARCHAR);
		dsFecha.addNew();
		dsFecha.setValue("fechahoy",hoy);
		
		_record.setValue("tipo_prod","");
		_record.setValue("estatus","");

		storeDataSet("record", _record);
		storeDataSet("fechas", dsFecha);
		
	}
	
	public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();	
		
		datosFilter = new DataSet();
		datosFilter.append("menu_migaja", java.sql.Types.VARCHAR);
		datosFilter.append("tipo_prod", java.sql.Types.VARCHAR);
		datosFilter.append("estatus", java.sql.Types.VARCHAR);
		datosFilter.addNew();
		datosFilter.setValue("estatus", "");
		
		idProducto = "'"+ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL+"'";
		datosFilter.setValue("tipo_prod", idProducto);

		return flag;
	}
}


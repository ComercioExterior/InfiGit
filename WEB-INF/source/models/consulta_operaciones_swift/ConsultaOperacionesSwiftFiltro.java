package models.consulta_operaciones_swift;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.dao.TransaccionDAO;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
/**
 * Clase filtro que muestra las unidades de inversi&oacute;n
 *@author elaucho
 */
public class ConsultaOperacionesSwiftFiltro extends MSCModelExtend {

	public void execute() throws Exception {
/*
 * Se publican las fechas por defecto en el filtro de busqueda
 */
		OrdenDAO ordenDAO = new OrdenDAO(_dso);
		UnidadInversionDAO confiD = new UnidadInversionDAO(_dso);
		TransaccionDAO transDAO = new TransaccionDAO(_dso);
		DataSet _fechas   = ordenDAO.mostrarFechas();
		confiD.listarTipoProducto();
		transDAO.listarTransaccionesPorId(TransaccionNegocio.VENTA_TITULOS,TransaccionNegocio.ORDEN_PAGO,TransaccionNegocio.PACTO_RECOMPRA);
		
		storeDataSet("productoTipo",confiD.getDataSet());		
		storeDataSet("fechas",_fechas);
		storeDataSet("transaccion",transDAO.getDataSet());
		//System.out.println("confiD.getDataSet(): "+transDAO.getDataSet());
		
	}//FIN EXECUTE
}//FIN CLASE
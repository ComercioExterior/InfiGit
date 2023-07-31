package models.configuracion.generales.configuracion_tasas;

import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.dao.CierreSistemaDAO;
import com.bdv.infi.dao.TransaccionDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

public class Filter extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
							
		TransaccionDAO transaccionDAO=new TransaccionDAO(_dso);
		CierreSistemaDAO cierreSistemaDAO=new CierreSistemaDAO(_dso);
		UnidadInversionDAO unidadInversionDAO=new UnidadInversionDAO(_dso);
		
		//Busqueda de tipo de transaccion
		transaccionDAO.listarTransaccionesPorId(TransaccionNegocio.COMISION_BUEN_VALOR);
				
				
		//Resolucion incidencia Calidad SICAD_2
		//unidadInversionDAO.listarTipoProducto();
		//Busqueda de tipo de Producto
		unidadInversionDAO.listarTipoProductoPorId(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA);
		
		cierreSistemaDAO.listarFechaSistema();
		 
		storeDataSet("transacciones", transaccionDAO.getDataSet());
		storeDataSet("fecha", cierreSistemaDAO.getDataSet());
				
		//registrar los datasets exportados por este modelo
		storeDataSet("productos", unidadInversionDAO.getDataSet());		
	}
}
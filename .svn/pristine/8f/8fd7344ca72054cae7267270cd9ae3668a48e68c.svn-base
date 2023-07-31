package models.intercambio.operaciones_DICOM.operaciones_demandas.operaciones_abonos.envio_archivo;

import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.UnidadInversionDAO;

public class Detalle extends MSCModelExtend {
	
	private long ordenId;
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
				
		ordenId=Long.parseLong(_req.getParameter("ordene_id"));
				
		UnidadInversionDAO inversionOrdenDAO = new UnidadInversionDAO(_dso);
		UnidadInversionDAO inversionOperacionesDAO = new UnidadInversionDAO(_dso);
		//inversionDAO.resumenAbonoCuentaDolaresSitme(unidadInversionId,fechaDesde,fechaHasta);
		
		inversionOrdenDAO.detalleCobroSubastaDivisasPorIdOrden(ordenId);
		//Configuracion de parametros de entrada																		
		storeDataSet("orden_detalle", inversionOrdenDAO.getDataSet());
		
		inversionOperacionesDAO.detalleOperacionBatchPorIdOrden(ordenId);		
		storeDataSet("operaciones_detalle", inversionOperacionesDAO.getDataSet());
				
	}
			
}

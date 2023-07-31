package models.configuracion.plantillas_correo;

import models.msc_utilitys.MSCModelExtend;
import models.msc_utilitys.Utilitys;

import com.bdv.infi.dao.PlantillasMailDAO;
import com.bdv.infi.dao.TipoProductoDao;

public class PlantillasMailFilter extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {		
		
		Utilitys.limpiarSesion(_req);		
		
		PlantillasMailDAO plantillasMailDAO = new PlantillasMailDAO(_dso);
		//NM29643 infi_TTS_466 09/07/2014
		TipoProductoDao tipoProdDao = new TipoProductoDao(_dso);
		
		tipoProdDao.listarProductosConEventos(); //Obtiene los tipos de productos con eventos de envio de correos asociados
		storeDataSet("productos", tipoProdDao.getDataSet()); //Registra el dataset exportado por este modelo
		
		plantillasMailDAO.obtenerTiposDestinatario(); //Obtiene los distintos tipos de destinatarios
		storeDataSet("tipos_dest", plantillasMailDAO.getDataSet()); //Registra el dataset exportado por este modelo
		
	}
}
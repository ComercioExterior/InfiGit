package models.configuracion.generales.precios_recompra;

import com.bdv.infi.dao.PrecioRecompraDAO;
import com.bdv.infi.data.PrecioRecompra;

import megasoft.DataSet;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;
/**
 * Clase que realiza la transaccion de buscar los precios de recompra por titulo especificado
 */
public class PreciosRecompraAprobar extends MSCModelExtend{

	public void execute() throws Exception {
		
		PrecioRecompraDAO precioRecompraDAO = new PrecioRecompraDAO(_dso);
		
		//Creacion del objeto precioRecompra
		PrecioRecompra precioRecompra = new PrecioRecompra();
		//precioRecompra.setFecha_act(fechaActual);
		precioRecompra.setTituloId(_record.getValue("titulo_id"));
		precioRecompra.setUsuarioAprobador(getUserName());
		precioRecompra.setTipoProductoId(_record.getValue("tipo_producto_id"));
		
		String[] sentencias = precioRecompraDAO.aprobar(precioRecompra);	
		
		db.execBatch(_dso,sentencias);
		
		//Exportar datos del titulo aprobado
		DataSet _filter = getDataSetFromRequest();		
		storeDataSet("filter", _filter);				
	
	}
}

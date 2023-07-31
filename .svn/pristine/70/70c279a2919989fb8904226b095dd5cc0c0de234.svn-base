package models.configuracion.generales.precios_recompra;

import com.bdv.infi.dao.PrecioRecompraDAO;
import com.bdv.infi.util.helper.Html;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;
/**
 * Clase que realiza la transaccion de buscar los precios de recompra por titulo especificado
 */
public class PreciosRecompraFilter extends MSCModelExtend{

	public void execute() throws Exception {
		
		PrecioRecompraDAO preciosRecompra = new PrecioRecompraDAO(_dso);
		preciosRecompra.listaStatusPreciosTitulos();
		storeDataSet("status", preciosRecompra.getDataSet());
		
		//Armar combo de tipo de producto
		DataSet _datos = new DataSet();
		_datos.append("combo_tipo_producto", java.sql.Types.VARCHAR);
		_datos.addNew();
		//llamar al método para armar combo con opción "Todos" para el filtro de búsqueda
		_datos.setValue("combo_tipo_producto", Html.getSelectTipoProductoTodos(_dso));
		
		storeDataSet("datos", _datos);

	
	}
}

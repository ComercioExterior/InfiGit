package models.configuracion.generales.precios_titulos;

import megasoft.DataSet;
import models.msc_utilitys.*;
import com.bdv.infi.dao.PreciosTitulosDAO;
import com.bdv.infi.util.helper.Html;

public class Filter extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		DataSet _datos = new DataSet();
		_datos.append("combo_tipo_producto", java.sql.Types.VARCHAR);
		_datos.addNew();
		//Armar combo de tipo de producto
		_datos.setValue("combo_tipo_producto", Html.getSelectTipoProductoTodos(_dso));
		
		storeDataSet("datos", _datos);

		PreciosTitulosDAO preciosTitulosDAO = new PreciosTitulosDAO(_dso);		
		//Crear lista de status de precios de titulos
		preciosTitulosDAO.listaStatusPreciosTitulos();
		
		//registrar los datasets exportados por este modelo
		storeDataSet("status", preciosTitulosDAO.getDataSet());		
	}
}
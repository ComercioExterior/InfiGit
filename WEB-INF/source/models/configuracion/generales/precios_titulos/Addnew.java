package models.configuracion.generales.precios_titulos;

import com.bdv.infi.util.helper.Html;

import megasoft.AbstractModel;
import megasoft.DataSet;

public class Addnew extends AbstractModel {
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		DataSet _datos = new DataSet();
		_datos.append("combo_tipo_producto", java.sql.Types.VARCHAR);
		_datos.addNew();
		//Armar combo de tipo de producto
		_datos.setValue("combo_tipo_producto", Html.getSelectTipoProducto(_dso));
		
		storeDataSet("datos", _datos);
	}
}

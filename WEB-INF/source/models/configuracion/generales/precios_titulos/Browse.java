package models.configuracion.generales.precios_titulos;

import models.msc_utilitys.*;
import com.bdv.infi.dao.PreciosTitulosDAO;
import com.bdv.infi.data.TitulosPrecios;

public class Browse extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		PreciosTitulosDAO confiD = new PreciosTitulosDAO(_dso);

		TitulosPrecios titulosPrecios = new TitulosPrecios();
		//LLenar objeto con los parametros del filtro
		titulosPrecios.setIdTitulo(_record.getValue("titulo_id"));
		titulosPrecios.setTipoProductoId(_record.getValue("tipo_producto_id"));		
			
		//Realizar consulta
		confiD.listarPreciosTitulos(titulosPrecios, _record.getValue("status_id"));
		//registrar los datasets exportados por este modelo
		storeDataSet("table", confiD.getDataSet());
		storeDataSet("datos", confiD.getTotalRegistros());
	}
}
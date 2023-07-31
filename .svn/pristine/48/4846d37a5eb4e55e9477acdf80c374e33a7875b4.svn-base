package models.configuracion.generales.precios_titulos;

import models.msc_utilitys.*;
import com.bdv.infi.dao.PreciosTitulosDAO;
import com.bdv.infi.data.TitulosPrecios;

public class Detalle extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		PreciosTitulosDAO confiD = new PreciosTitulosDAO(_dso);

		TitulosPrecios titulosPrecios = new TitulosPrecios();
		//LLenar objeto con parametros
		//Buscar el detalle sólo por id de titulo y se verá el historial para todos los
		//tipos de producto
		titulosPrecios.setIdTitulo(_req.getParameter("titulo_id"));
		
		//Realizar consulta
		confiD.detallePrecioTitulo(titulosPrecios);
		
		//registrar los datasets exportados por este modelo
		storeDataSet("table", confiD.getDataSet());
		storeDataSet("datos", confiD.getTotalRegistros());
	}
}
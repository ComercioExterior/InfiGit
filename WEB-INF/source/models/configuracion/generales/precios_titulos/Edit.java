package models.configuracion.generales.precios_titulos;

import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.PreciosTitulosDAO;
import com.bdv.infi.data.TitulosPrecios;

public class Edit extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		PreciosTitulosDAO confiD = new PreciosTitulosDAO(_dso);
		
		TitulosPrecios titulosPrecios = new TitulosPrecios();
		//LLenar objeto con parametros
		titulosPrecios.setIdTitulo(_req.getParameter("titulo_id"));
		titulosPrecios.setTipoProductoId(_req.getParameter("tipo_producto_id"));		
				
		//Realizar consulta
		confiD.listarPreciosTitulos(titulosPrecios, null);
		//registrar los datasets exportados por este modelo
		storeDataSet("table", confiD.getDataSet());
	}
}
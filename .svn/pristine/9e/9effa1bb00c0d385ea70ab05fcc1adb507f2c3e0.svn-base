package models.configuracion.generales.precios_titulos;


import megasoft.db;
import models.msc_utilitys.*;
import com.bdv.infi.dao.PreciosTitulosDAO;
import com.bdv.infi.data.TitulosPrecios;

public class Update extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		PreciosTitulosDAO confiD = new PreciosTitulosDAO(_dso);
		TitulosPrecios titulosPrecios = new TitulosPrecios();
		titulosPrecios.setIdTitulo(_req.getParameter("titulo_id"));
		titulosPrecios.setPNominal(_req.getParameter("titulos_precio_nominal"));
		titulosPrecios.setPMercado(_req.getParameter("titulos_precio_mercado"));
		titulosPrecios.setPRecompra(_req.getParameter("titulos_precio_recompra"));
		titulosPrecios.setTipoProductoId(_req.getParameter("tipo_producto_id"));
		
		//Nombre del usuario que efectuó la modificación
		titulosPrecios.setInUsuarioNombre(this.getUserName());
		
		String[] consulta =confiD.modificar(titulosPrecios);		
		
		db.execBatch(_dso,consulta);
	}
}
package models.configuracion.generales.precios_titulos;

import com.bdv.infi.dao.PreciosTitulosDAO;
import com.bdv.infi.data.TitulosPrecios;
import megasoft.*;
import models.msc_utilitys.*;

public class Aprobar extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		PreciosTitulosDAO preciosTitulosDAO = new PreciosTitulosDAO(_dso);
		TitulosPrecios titulosPrecios = new TitulosPrecios();
		
		titulosPrecios.setIdTitulo(_req.getParameter("titulo_id"));
		titulosPrecios.setUsuarioAprobador(getUserName());		
		titulosPrecios.setTipoProductoId(_req.getParameter("tipo_producto_id"));
		
		//Aprobar el precio del titulo actualizando el ultimo registro 
		//en el historico colocando el usuario que esta aprobando y la fecha de aprobacion (actual)
		//Y actualizando el usuario aprobador y fecha en el refistro de la tabla principal del precio
		String[] sqlFinales = preciosTitulosDAO.aprobar(titulosPrecios);
			
		db.execBatch(_dso,sqlFinales);
		
		//Exportar datos del titulo aprobado
		DataSet _filter = getDataSetFromRequest();		
		storeDataSet("filter", _filter);		

	}	
	
}
package models.configuracion.fecha_valor;

import megasoft.AbstractModel;
import com.bdv.infi.dao.FechaValorDAO;

/**
 * Clase encargada de recuperar datos necesarios para la construcci&oacute;n del fitro de busqueda
 */
public class Filter extends AbstractModel
{
	 
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{
		String nombreFechaValor="";
		FechaValorDAO fechaValorDAO= new FechaValorDAO(_dso);
		fechaValorDAO.listarFechaValor(nombreFechaValor);
		//registrar los datasets exportados por este modelo
		storeDataSet("fecha_valor", fechaValorDAO.getDataSet());
				
	}	
	

}

package models.configuracion.generales.indicadores; 

import com.bdv.infi.dao.IndicadoresDAO;

import megasoft.*;


/**
 * Clase encargada de ejecutar la consulta de Indicadores registrados.
 */
public class Edit extends AbstractModel
{  
 
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{			
		IndicadoresDAO indicadoresDAO = new IndicadoresDAO(_dso);			
		
		//Busca el registro a editar
		indicadoresDAO.listar(_req.getParameter("indica_id"));		
		
		//registrar los datasets exportados por este modelo
		storeDataSet("table", indicadoresDAO.getDataSet());
		
		
		
	}

}

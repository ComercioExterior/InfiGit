package models.configuracion.generales.calendario; 

import com.bdv.infi.dao.CalendarioDAO;

import megasoft.*;



public class Table extends AbstractModel
{  
 
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{			
		CalendarioDAO calendarioDAO = new CalendarioDAO(_dso);	
		
		//Realizar consulta
		calendarioDAO.listarTodos();		
		
		//registrar los datasets exportados por este modelo
		storeDataSet("table", calendarioDAO.getDataSet());
		storeDataSet("registros", calendarioDAO.getTotalRegistros());
		
		
		
	}

}

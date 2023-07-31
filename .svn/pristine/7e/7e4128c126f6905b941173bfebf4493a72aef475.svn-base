package models.configuracion.generales.paises; 

import com.bdv.infi.dao.PaisesDAO;
import megasoft.*;



public class Table extends AbstractModel
{  
 
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception{		
		PaisesDAO paisesDAO = new PaisesDAO(_dso);	
		paisesDAO.listarPaises(_record.getValue("TAB_DESCRIPCION"));
		storeDataSet("table", paisesDAO.getDataSet());
		storeDataSet("datos", paisesDAO.getTotalRegistros());
		
	}
}

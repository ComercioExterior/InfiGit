package models.configuracion.generales.blotter; 

import megasoft.*;
import com.bdv.infi.dao.BlotterDAO;

/**
 * Clase encargada de ejecutar la consulta de Bloter registrados.
 */
public class Table extends AbstractModel
{  
 
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{			
		_req.getSession().removeAttribute("bloter-find.framework.page.record");
		
		BlotterDAO bloterDAO = new BlotterDAO(_dso);	
				
		String bloter_descripcion = null;
		String restringido = null;
		String cartera_propia = null;
		
		 if(_record.getValue("bloter_descripcion")!=null){
			 bloter_descripcion=_record.getValue("bloter_descripcion");
		 }
		 if(_record.getValue("bloter_in_restringido")!=null){
			 restringido=_record.getValue("bloter_in_restringido");
		 }
		 if(_record.getValue("bloter_in_cartera_propia")!=null){
			 cartera_propia=_record.getValue("bloter_in_cartera_propia");
		 }
		//Realizar consulta
		bloterDAO.listarporfiltro(bloter_descripcion,restringido,cartera_propia);
		//registrar los datasets exportados por este modelo
		storeDataSet("table", bloterDAO.getDataSet());
		storeDataSet("total", bloterDAO.getTotalRegistros());
		

		
		
		
	}

}

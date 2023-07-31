package models.oficina;

import com.bdv.infi.dao.OficinaDAO;
import models.msc_utilitys.MSCModelExtend;

/**
 * Clase encargada de buscar las oficinas que se encuentran registradas en base de datos
 * @author elaucho
 */
public class OficinaBrowse extends MSCModelExtend{
	
	@Override
	public void execute() throws Exception {
		
		//DAO a utilizar
		OficinaDAO oficinaDAO = new OficinaDAO(_dso);
				
		//Listamos las oficinas registradas en base de datos
		oficinaDAO.listar();
		
		//Publicamos el dataset
		storeDataSet("oficinas",oficinaDAO.getDataSet());
		
	}//fin execute

}//Fin clase

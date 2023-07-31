package models.bcv.configuracion;

import com.bdv.infi.dao.CredencialesDAO;
import com.bdv.infi.dao.OficinaDAO;

import models.msc_utilitys.MSCModelExtend;

public class Browse extends MSCModelExtend {

	@Override
	public void execute() throws Exception {
		
		//DAO a utilizar
		CredencialesDAO credencialesDAO = new CredencialesDAO(_dso);
				
		//Listamos las oficinas registradas en base de datos
		credencialesDAO.listarCredencialesPorTipo("");
		
		//Publicamos el dataset
		storeDataSet("credenciales",credencialesDAO.getDataSet());
		
	}//fin execute
}

package models.custodia.informes.bloqueo_garantias;

import com.bdv.infi.dao.TipoBloqueoDAO;
import megasoft.AbstractModel;


public class Filter extends AbstractModel {


	public void execute() throws Exception {
		// TODO Auto-generated method stub
		TipoBloqueoDAO tipoBloDAO = new TipoBloqueoDAO(_dso);
	
		tipoBloDAO.listar();
		storeDataSet("tipoBlo", tipoBloDAO.getDataSet());		
		;	
							
	}

}
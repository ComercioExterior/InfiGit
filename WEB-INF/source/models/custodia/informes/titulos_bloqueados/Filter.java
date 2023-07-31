package models.custodia.informes.titulos_bloqueados;


import megasoft.AbstractModel;
import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.TitulosDAO;
public class Filter extends AbstractModel {

	@Override
	public void execute() throws Exception {
		ClienteDAO clien = new ClienteDAO(_dso);
		TitulosDAO titDAO = new TitulosDAO(_dso);
		clien.fechaHoy();
		storeDataSet("fecha",clien.getDataSet());
		storeDataSet( "cliente", clien.getDataSet());					
	}

}

package models.custodia.informes.valores_liberados;


import megasoft.AbstractModel;
import com.bdv.infi.dao.ClienteDAO;
public class Filter extends AbstractModel {

	@Override
	public void execute() throws Exception {
		ClienteDAO clien = new ClienteDAO(_dso);
		clien.fechaHoy();
		storeDataSet("fecha",clien.getDataSet());
		clien.listar();
		storeDataSet( "cliente", clien.getDataSet());
							
	}

}

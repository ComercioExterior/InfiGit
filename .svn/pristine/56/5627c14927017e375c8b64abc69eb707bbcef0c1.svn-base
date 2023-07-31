package models.custodia.informes.vencimiento_intereses;
import com.bdv.infi.dao.ClienteDAO;
import megasoft.AbstractModel;
public class Filter extends AbstractModel {

	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub
	
		
		ClienteDAO clien = new ClienteDAO(_dso);
		
		clien.fechaHoy();
		storeDataSet("fecha",clien.getDataSet());
		clien.listar();
		storeDataSet( "cliente", clien.getDataSet());
							
	}

}

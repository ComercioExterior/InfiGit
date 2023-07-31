package models.custodia.informes.valores_custodia;
import com.bdv.infi.dao.TransaccionDAO;
import com.bdv.infi.logic.interfaces.GrupoTransaccionNegocio;

import megasoft.AbstractModel;

public class Filter extends AbstractModel {

	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub
	
		TransaccionDAO traD = new TransaccionDAO(_dso);
		traD.fechaHoy();
		storeDataSet("fecha",traD.getDataSet());
		traD.listar(GrupoTransaccionNegocio.CUSTODIA);
		storeDataSet( "trans", traD.getDataSet());
							
	}

}

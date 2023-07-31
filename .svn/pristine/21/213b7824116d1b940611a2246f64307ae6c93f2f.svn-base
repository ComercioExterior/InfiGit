package models.custodia.informes.lista_clientes;

//import com.bdv.infi.dao.DocumentoDefinicionDAO;
import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.TipoPersonaDAO;
//import com.bdv.infi.dao.UnidadInversionDAO;
//import com.bdv.infi.logic.interfaces.ConstantesGenerales;
//import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

import megasoft.AbstractModel;
//import megasoft.DataSet;
import megasoft.DataSet;

public class Filter extends AbstractModel {

	@Override
	public void execute() throws Exception {
		TipoPersonaDAO tipoPerDAO = new TipoPersonaDAO(_dso);
		tipoPerDAO.fechaHoy();
		storeDataSet("fecha",tipoPerDAO.getDataSet());
		tipoPerDAO.listarTodos();
		storeDataSet("tipoPer", tipoPerDAO.getDataSet());
		
		DataSet tipoProducto = new DataSet();
		tipoProducto.append("tipo", java.sql.Types.VARCHAR);
		tipoProducto.addNew();
		tipoProducto.setValue("tipo", com.bdv.infi.util.helper.Html.getSelectTipoProductoTodos(_dso));
		storeDataSet("tipoProducto", tipoProducto);							
	}

}

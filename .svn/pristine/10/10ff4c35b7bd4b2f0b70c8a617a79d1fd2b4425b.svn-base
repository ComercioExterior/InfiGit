package models.custodia.informes.valores_garantias;
import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.TipoBloqueoDAO;
import megasoft.AbstractModel;
import megasoft.DataSet;

public class Filter extends AbstractModel {

	@Override
	public void execute() throws Exception {
		TipoBloqueoDAO tipoBloDAO = new TipoBloqueoDAO(_dso);	
		tipoBloDAO.fechaHoy();
		storeDataSet("fecha",tipoBloDAO.getDataSet());
		tipoBloDAO.listar();
		storeDataSet("tipoBlo", tipoBloDAO.getDataSet());	
		
		DataSet tipoProducto = new DataSet();
		tipoProducto.append("tipo", java.sql.Types.VARCHAR);
		tipoProducto.addNew();
		tipoProducto.setValue("tipo", com.bdv.infi.util.helper.Html.getSelectTipoProductoTodos(_dso));
		storeDataSet("tipoProducto", tipoProducto);		
	}
}

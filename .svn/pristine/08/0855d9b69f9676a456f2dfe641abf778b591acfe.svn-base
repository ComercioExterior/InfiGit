package models.custodia.informes.transacciones_liquidadas;
import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.dao.TransaccionDAO;
import megasoft.AbstractModel;
/**
 * Clase filtro para mostrar fecha de hoy y lista de transacciones a consultar
 * @author ct09153
 */
public class Filter extends AbstractModel {

	@Override
	public void execute() throws Exception {
		// TODO Auto-generated method stub
	
		TransaccionDAO transaccionDAO = new TransaccionDAO(_dso);
		ClienteDAO clienteDAO = new ClienteDAO(_dso);
		
		//Fecha de hoy
		transaccionDAO.fechaHoy();
		storeDataSet("fecha",transaccionDAO.getDataSet());
		
		//Listar transacciones
		transaccionDAO.listarFiltroTransaccionesLiquidadas(null);
		storeDataSet( "trans", transaccionDAO.getDataSet());
		
		//Listar cliente
		clienteDAO.listar();
		storeDataSet( "cliente", clienteDAO.getDataSet());
							
	}

}

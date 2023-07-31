package models.bcv.intervencion;

import com.bdv.infi.dao.IntervencionDAO;
import com.bdv.infi.data.ClienteIntervencion;
import com.enterprisedt.util.debug.Logger;
import megasoft.AbstractModel;
import megasoft.db;

public class Update extends AbstractModel {
	private Logger logger = Logger.getLogger(Update.class);

	public void execute() throws Exception {
		try {
			IntervencionDAO intervencionDao = new IntervencionDAO(_dso);
			ClienteIntervencion intervencionCliente = new ClienteIntervencion();
			intervencionCliente.setRifCliente(_record.getValue("rif"));
			intervencionCliente.setNombreCliente(_record.getValue("nombre"));
			intervencionCliente.setFechaValor(_record.getValue("fecha"));
			intervencionCliente.setMontoDivisa(_record.getValue("monto"));
			intervencionCliente.setCuentaDivisas(_record.getValue("cuentadivisas"));
			intervencionCliente.setCuentaBolivares(_record.getValue("cuentabolivares"));
			intervencionCliente.setMoneda(_record.getValue("moneda"));
			intervencionCliente.setIdOperacion(_record.getValue("operacion"));
			
			System.out.println("operacion :" + intervencionCliente.getIdOperacion());

			String sql = intervencionDao.modificarCliente(intervencionCliente);
			db.exec(_dso, sql);
			
		} catch (Exception e) {
			logger.error("Update : execute() " + e);
			System.out.println("Update : execute() " + e);
			
		}
	}
}

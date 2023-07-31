package models.configuracion.transacciones_fijas;

import megasoft.*;
import com.bdv.infi.dao.TransaccionFijaDAO;

public class Edit extends AbstractModel{
 
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {	
		
		String vehiculo= getSessionObject("vehiculo").toString();		
		TransaccionFijaDAO transaccionDAO = new TransaccionFijaDAO(_dso);
		
		if(!vehiculo.equals("0")){
			transaccionDAO.listar(Integer.parseInt(_req.getParameter("trnfin_id")),vehiculo,_req.getParameter("instrumento_id"));
			_config.template = "form_vehicu.htm";
		}else{
			transaccionDAO.listar(Integer.parseInt(_req.getParameter("trnfin_id")));
		}
		
		storeDataSet("table",transaccionDAO.getDataSet());
	}
}

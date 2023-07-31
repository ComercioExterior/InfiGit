package models.bcv.menudeo_envio;

import megasoft.*;
import com.bdv.infi.dao.ParametrosDAO;
import com.enterprisedt.util.debug.Logger;



public class Edit extends AbstractModel
{
	private Logger logger = Logger.getLogger(Edit.class);
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{	
		try{
			
			ParametrosDAO parametrosDAO= new ParametrosDAO(_dso);
			
			parametrosDAO.listarClienteMenudeo(_req.getParameter("idOrdenes"));
			storeDataSet("table", parametrosDAO.getDataSet());
			
		}catch (Exception e) {
			// TODO: handle exception
			logger.error("Edit (Menudeo) : execute() "+e);
			System.out.println("Edit (Menudeo) : execute() "+e);
		}
		
	}

}

package models.bcv.mesas_cambios_inter;

import megasoft.*;
import com.bdv.infi.dao.MesaCambioDAO;
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
			
//			ParametrosDAO parametrosDAO= new ParametrosDAO(_dso);
			MesaCambioDAO operaciones = new MesaCambioDAO(_dso);
			operaciones.BusquedaClienteParaPacto(_req.getParameter("idOrdenes"));
			storeDataSet("table", operaciones.getDataSet());
			
		}catch (Exception e) {
			// TODO: handle exception
			logger.error("Error : "+e);
			System.out.println("Error : "+e);
		}
		
	}

}

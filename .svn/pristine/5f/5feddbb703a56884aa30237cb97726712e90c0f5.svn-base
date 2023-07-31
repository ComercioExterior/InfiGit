package models.liquidacion.proceso_blotter;

import com.bdv.infi.dao.OperacionDAO;
import models.msc_utilitys.MSCModelExtend;

/**
 * Muestra los intentos por cada operacion financiera
 * elaucho
 */
public class LiquidacionStatusOperacion extends MSCModelExtend{

	@Override
	public void execute() throws Exception {
		
		//Objeto DAO
		OperacionDAO operacionDAO = new OperacionDAO(_dso);
		
		//Se listan los intentos por orden (Operaciones financieras)
		operacionDAO.listarIntentosOperacion(Long.parseLong(_req.getParameter("ordene_id")));
		
		//Se publica dataset
		storeDataSet("intentos", operacionDAO.getDataSet());
		
	}//fin execute
}//fin clase

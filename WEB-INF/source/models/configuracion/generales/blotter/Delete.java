package models.configuracion.generales.blotter;

import megasoft.db;
import models.msc_utilitys.*;
import com.bdv.infi.dao.BlotterDAO;
import com.bdv.infi.data.BloterDefinicion;

public class Delete extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		BlotterDAO bloterDAO = new BlotterDAO(_dso);
		BloterDefinicion bloterDefinicion = new BloterDefinicion();
		
		String sql ="";
		
		bloterDefinicion.setBloter_id(_req.getParameter("bloter_id"));
		
		sql=bloterDAO.eliminar(bloterDefinicion);
		db.exec(_dso, sql);
	}
	
	public boolean isValid() throws Exception {

		boolean flag = super.isValid();
		
		BlotterDAO bloterDAO = new BlotterDAO(_dso);
		BloterDefinicion bloter = new BloterDefinicion();
		bloter.setBloter_id(_req.getParameter("bloter_id"));

		bloterDAO.verificar(bloter);
		if (bloterDAO.getDataSet().count()>0){
			_record.addError("Blotter","No se puede eliminar el Registro. Esta siendo utilizado como referencia en otras transacciones. Error de Integridad Referencial.");
			flag = false;
		}
		return flag;		
	}
	
}
package models.liquidacion.cancelacion_operaciones;

import megasoft.db;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.data.Orden;
import com.bdv.infi.logic.CancelacionOrden;
import com.bdv.infi.logic.interfaces.StatusOrden;


public class Procesar extends CancelacionOrden {
	
	String idOrden = null;
	Orden orden = new Orden();
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		OrdenDAO ordenDAO = new OrdenDAO(_dso);
		//Ejecutar cancelación de las operaciones (Rechazadas y En Espera) de la Orden
		String sql = ordenDAO.cancelarOperacionesOrden(Long.parseLong(idOrden), getUserName());
		
		db.exec(_dso ,sql);		
		
		storeDataSet("record", _record);
	}	
	
	/**
	 * Validaciones generales del modelo.
	 */
	public boolean isValid() throws Exception {

		boolean flag = super.isValid();
		
		if(flag){
			OrdenDAO ordenDAO = new OrdenDAO(_dso);
			
			idOrden = _record.getValue("ordene_id");
			orden = (Orden) ordenDAO.listarOrden(Long.parseLong(idOrden), false, false, false, false, false);
	
			if (!orden.getStatus().equals(StatusOrden.LIQUIDADA)){  
				_record.addError("Status Orden","La Orden Nro. "+idOrden+ " No se encuentra en status Liquidada.");
				flag = false;
			}				
		}
		
		return flag;
	}

}

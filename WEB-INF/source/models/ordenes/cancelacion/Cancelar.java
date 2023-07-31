package models.ordenes.cancelacion;

import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.data.Orden;
import com.bdv.infi.logic.CancelacionOrden;


public class Cancelar extends CancelacionOrden {
	
	String idOrden = null;
	String idUnidadInversion = null;
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		OrdenDAO ordenDAO = new OrdenDAO(_dso);
		
		//Obtener id de la  orden a cancelar
		idOrden = _req.getParameter("ordene_id");
		//crear objeto con la orden a cancelar
		Orden ordenCancelar = (Orden) ordenDAO.listarOrden(Long.parseLong(idOrden), true, true, true, true, false);			
			
		//cancelar orden
		this.cancelarOrden(ordenCancelar);
		//en este caso el id de la orden relacionada es el ID de la orden que se genero para la cancelacion
		_config.nextAction="cancelacion_orden-documento?ordene_id="+ordenCancelar.getIdOrdenRelacionada();
	}	
	
}

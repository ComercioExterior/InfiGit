/**
 * 
 */
package models.custodia.transacciones.pago_cupones.consulta;

import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.OrdenDAO;

/**
 * @author eel
 *
 */
public class PagoCuponesConsultaDetallesProceso extends MSCModelExtend{

	public void execute() throws Exception {
		String procesoId	=_req.getParameter("ord_id");
		OrdenDAO orden		=new OrdenDAO(_dso);
		orden.listarOrdenesProceso(Long.parseLong(procesoId.substring(0, 2)));
		storeDataSet("proceso", orden.getDataSet());
	}
}

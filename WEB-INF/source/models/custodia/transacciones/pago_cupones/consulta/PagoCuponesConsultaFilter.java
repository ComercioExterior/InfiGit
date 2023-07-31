/**
 * 
 */
package models.custodia.transacciones.pago_cupones.consulta;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.bdv.infi.dao.OrdenesClienteDAO;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

/**
 * @author eel
 *
 */
public class PagoCuponesConsultaFilter extends MSCModelExtend{

	public void execute() throws Exception {
		
		//Busqueda de las fechas correspondientes al filtro
		OrdenesClienteDAO fechas	=new OrdenesClienteDAO(_dso);
		DataSet _fechasFiltro		= fechas.mostrarFechasFilterCupones();
		storeDataSet("_fechasFiltro", _fechasFiltro);
	}
}

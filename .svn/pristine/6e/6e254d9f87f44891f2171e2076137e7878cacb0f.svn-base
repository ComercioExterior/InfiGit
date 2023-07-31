package models.detalles_entidades.detalles_operacion;
import com.bancovenezuela.comun.data.OrdenOperacion;
import com.bdv.infi.dao.OperacionDAO;
import megasoft.AbstractModel;


/**
 * Clase encargada de recuperar detalles de un t&iacute;tulo determinado. 
 * @author elaucho
 */
public class DetalleOperaciones extends AbstractModel
{
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{
		OperacionDAO orden	=new OperacionDAO(_dso);
		OrdenOperacion ordenOperacion	=new OrdenOperacion();
		ordenOperacion.setIdOperacion(Long.parseLong(_req.getParameter("id_operacion")));
		//Listar las operaciones financieras
		orden.listarDetallesOperacionFinanciera(ordenOperacion);
		storeDataSet("operaciones", orden.getDataSet());
	}

}

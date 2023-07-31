package models.detalles_entidades.detalle_orden_transacciones;

import com.bdv.infi.dao.OrdenDAO;
import megasoft.AbstractModel;
import megasoft.DataSet;

/**
 * Clase encargada de recuperar detalles de un t&iacute;tulo determinado. 
 * @author elaucho
 */
public class DetalleOrdenTransacciones extends AbstractModel
{
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{
		
		 OrdenDAO orden_detalle_t = new OrdenDAO(_dso);
		 String orden=_req.getParameter("ord_id");
		 DataSet _ordene_id=new DataSet();
			_ordene_id.append("ordene_id",java.sql.Types.VARCHAR);
			_ordene_id.addNew();
			_ordene_id.setValue("ordene_id",_req.getParameter("ord_id"));
			storeDataSet( "ordene_id", _ordene_id);
		 int orden1=Integer.parseInt(orden);
		//Obtener detalles de la orden
		 orden_detalle_t.listarDetallesTransacciones(orden1);
		//Exportar dataset con datos recuperados
		storeDataSet( "detalle_orden_transacciones", orden_detalle_t.getDataSet());		
				
	}

}

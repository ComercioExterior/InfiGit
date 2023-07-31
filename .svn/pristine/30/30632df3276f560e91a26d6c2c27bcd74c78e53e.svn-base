package models.liquidacion.informes.ordenes_venta_titulo;

import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

import megasoft.*;



/**
 * Clase encargada de ejecutar la consulta de Movimientos (transacciones) registrados para los Clientes.
 * @author Erika Valerio, Megasoft Computaci&oacute;n
 */
public class Table extends AbstractModel
{
 
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{			
		OrdenDAO ordenDAO = new OrdenDAO(_dso);
		String idCliente = null;
		String idTitulo = null;
		 
		DataSet estatusSeleccionado=new DataSet();
		DataSet tituloSeleccionado=new DataSet();
		//Conversion de fechas
		String fechaTransacDesde = null;
		String fechaTransacHasta = null ;
		String status = null ;
		//String transaccion = null ;
						
		if(_record.getValue("client_id")!=null)
			idCliente = _record.getValue("client_id");
		
		if(_record.getValue("titulo_id")!=null)
			idTitulo = _record.getValue("titulo_id");
		
		if(_record.getValue("fe_transac_desde")!=null)
			fechaTransacDesde = _record.getValue("fe_transac_desde");
		
		if(_record.getValue("fe_transac_hasta")!=null)
			fechaTransacHasta = _record.getValue("fe_transac_hasta");
		
		if(_record.getValue("status_operacion")!=null)
			status = _record.getValue("status_operacion");
		
		/*if(_record.getValue("transa_id")!=null)
			transaccion = _record.getValue("transa_id");*/
		
		estatusSeleccionado.addNew();
		estatusSeleccionado.append("status",java.sql.Types.VARCHAR);
		estatusSeleccionado.setValue("status",status);
		
		tituloSeleccionado.addNew();
		tituloSeleccionado.append("titulo",java.sql.Types.VARCHAR);
		tituloSeleccionado.setValue("titulo",idTitulo);
		//Realizar consulta
		
		ordenDAO.listarMovimientosFinancieros(idCliente, idTitulo, fechaTransacDesde, fechaTransacHasta, status, TransaccionNegocio.VENTA_TITULOS);
		
		//registrar los datasets exportados por este modelo
		storeDataSet("table", ordenDAO.getDataSet());
		storeDataSet("_record", _record);
		storeDataSet("datos", ordenDAO.getTotalRegistros());
		
		//DataSet para marcar el valor del estatus seleccionado
		storeDataSet("estatus_seleccionado", estatusSeleccionado);
		
		//DataSet para marcar el valor del titulo seleccionado
		storeDataSet("titulo_seleccionado", tituloSeleccionado);
		
	}

}

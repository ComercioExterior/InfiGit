package models.custodia.transacciones.desbloqueo_titulos;

import com.bdv.infi.dao.TitulosBloqueoDAO;

import megasoft.*;

/**
 * Clase encargada de ejecutar la consulta de T&iacute;tulos bloqueados de un cliente en particular que pueden ser desbloqueados
 * @author Erika Valerio, Megasoft Computaci&oacute;n
 */
public class Table extends AbstractModel
{
 
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{			
		TitulosBloqueoDAO titulosBloqueoDAO = new TitulosBloqueoDAO(_dso);
		long idCliente = 0;
		DataSet _dsparam = getDataSetFromRequest();
	
		if(_record.getValue("client_id")!=null)
			idCliente = Long.parseLong(_record.getValue("client_id"));
		
		//Realizar consulta
		titulosBloqueoDAO.listarTitulosBloqueados(idCliente);
		
		//registrar los datasets exportados por este modelo
		storeDataSet("table", titulosBloqueoDAO.getDataSet());
		
		storeDataSet("datos", titulosBloqueoDAO.getTotalRegistros());
		storeDataSet("dsparam", _dsparam);
		
		
	}

}

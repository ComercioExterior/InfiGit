package models.configuracion.fecha_valor;

import megasoft.*;
import com.bdv.infi.dao.FechaValorDAO;


/**
 */
public class Table extends AbstractModel
{
 
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{			
		

		//Realizar consulta
		FechaValorDAO fechaValorDAO= new FechaValorDAO(_dso);
		fechaValorDAO.listarFechaValor(_record.getValue("fecha_valor_nombre"));
		//registrar los datasets exportados por este modelo
		storeDataSet("table", fechaValorDAO.getDataSet());
		
		storeDataSet("datos", fechaValorDAO.getTotalRegistros());
		
		
	}
}

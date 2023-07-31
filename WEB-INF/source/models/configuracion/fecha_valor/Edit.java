package models.configuracion.fecha_valor;

import megasoft.*;
import com.bdv.infi.dao.FechaValorDAO;


public class Edit extends AbstractModel
{
 
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{	
		FechaValorDAO fechaValorDAO= new FechaValorDAO(_dso);
		fechaValorDAO.listarFecha(Integer.parseInt(_req.getParameter("fecha_valor_id")));
		storeDataSet("table",fechaValorDAO.getDataSet());	
	}

}

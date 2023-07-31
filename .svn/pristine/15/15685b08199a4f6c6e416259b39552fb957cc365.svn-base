package models.utilitarios.mensajes_interfaces;

import megasoft.AbstractModel;
import com.bdv.infi.dao.MensajeDAO;

/**
 * Clase que muestra el detalle para una operación SWIFT
 */
public class DetalleMensaje extends AbstractModel{	
	
	public void execute() throws Exception {
		
		MensajeDAO mensajeDAO = new MensajeDAO(_dso);
		long mensajeId = Long.parseLong(_record.getValue("mensaje_id"));
		
		mensajeDAO.listarDetalleMensaje(mensajeId);
		
		//Publicamos datasets
		storeDataSet("registros",mensajeDAO.getDataSet());
		mensajeDAO.listarMensaje(mensajeId);
		storeDataSet("mensaje",mensajeDAO.getDataSet());
		storeDataSet("total",mensajeDAO.getTotalRegistros());
		storeDataSet("record", _record);
		storeDataSet("request",getDataSetFromRequest());
		
	}
}

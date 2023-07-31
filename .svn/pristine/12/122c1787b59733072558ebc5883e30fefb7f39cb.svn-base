package models.utilitarios.mensajes_interfaces;

import megasoft.AbstractModel;
import megasoft.DataSet;
import com.bdv.infi.dao.MensajeDAO;
import com.bdv.infi.dao.OrdenDAO;
/**
 * Clase filtro que muestra las unidades de inversi&oacute;n
 *@author elaucho
 */
public class FiltroMensajes extends AbstractModel {

	public void execute() throws Exception {

		OrdenDAO ordenDAO = new OrdenDAO(_dso);
		MensajeDAO mensajeDAO = new MensajeDAO(_dso);
		
		// fechas por defecto en el filtro de busqueda
		DataSet _fechas   = ordenDAO.mostrarFechas();
		
		storeDataSet("fechas",_fechas);		
		storeDataSet("tipos_mensaje", mensajeDAO.listarTiposMensaje());
		
	}
}
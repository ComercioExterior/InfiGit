package models.generar_opics;

import megasoft.DataSet;
import models.msc_utilitys.*;

import com.bdv.infi.dao.MensajeOpicsDAO;
import com.bdv.infi.dao.OrdenDAO;
/**
 * Clase filtro que muestra las unidades de inversi&oacute;n
 *@author elaucho
 */
public class GenerarDatosOpicsFilter extends MSCModelExtend {

	public void execute() throws Exception {
/*
 * Se publican las fechas por defecto en el filtro de busqueda
 */
		OrdenDAO ordenDAO = new OrdenDAO(_dso);
		DataSet _fechas   = ordenDAO.mostrarFechas();
		
		MensajeOpicsDAO mensajeOpicsDAO=new MensajeOpicsDAO(_dso);
		
		//Busqueda de los tipos de mensajes Opics disponibles
		mensajeOpicsDAO.listarTipoMensajesOpics();
		DataSet _tiposMensajes=mensajeOpicsDAO.getDataSet();
		
		storeDataSet("fechas",_fechas);
		storeDataSet("tipo_mensajes",_tiposMensajes);
	}//FIN EXECUTE
}//FIN CLASE
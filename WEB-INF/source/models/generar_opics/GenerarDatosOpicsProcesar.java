package models.generar_opics;

import com.bdv.infi.dao.MensajeOpicsDAO;
import com.bdv.infi.logic.transform.GeneracionArchivoOpics;
import com.bdv.infi.util.Utilitario;

import megasoft.DataSet;
import megasoft.Logger;
import models.msc_utilitys.*;
/**
 * Esta clase invoca al metodo generarArchivoOpics, el cual lo genera y es enviado vía FTP
 *@author elaucho
 */
public class GenerarDatosOpicsProcesar extends MSCModelExtend{
	private String fechaDesde,fechaHasta;
	private String tipoMensaje;
	@Override
	public void execute() throws Exception {
	
		DataSet _error = new DataSet();
		
		try {
			fechaDesde=_req.getParameter("fecha_desde");
			//System.out.println("FECHA DESDE ----> "  + fechaDesde);
			fechaHasta=_req.getParameter("fecha_hasta");
			//System.out.println("FECHA HASTA ----> "  + fechaHasta);
			tipoMensaje=_req.getParameter("tipo_mensaje");
//			System.out.println("TIPO MENSAJE ---> " + tipoMensaje);
			
			//Buscamos los id de los vehiculos asociados a los deal que no hayan sido enviados a OPICS
			MensajeOpicsDAO mensajeOpicsDAO = new MensajeOpicsDAO(_dso);
			String vehiculos[] = mensajeOpicsDAO.listarMensajesPorVehiculo();
			
			//Objeto que contiene la logica para el envio del archivo
			GeneracionArchivoOpics generacionArchivoOpics = new GeneracionArchivoOpics(_dso,vehiculos,fechaDesde,fechaHasta,tipoMensaje);
			generacionArchivoOpics.generarArchivoOpics();
			
			storeDataSet("error",_error);
			
		} catch (Exception e) {
			
			Utilitario utilitario = new Utilitario();
			Logger.error(this, "Se ha presentado un problema en el proceso de generación del archivo OPICS "+utilitario.stackTraceException(e));
			
			//DataSet de Error
			_error.append("error", java.sql.Types.VARCHAR);
			_error.addNew();
			_error.setValue("error","Se ha interrumpido el proceso de generación de datos opics Ver Log "+ e.getMessage());
			
			storeDataSet("error",_error);
			_config.template="error.htm";
		}//fin catch
	}//fin execute
}//Fin Clase

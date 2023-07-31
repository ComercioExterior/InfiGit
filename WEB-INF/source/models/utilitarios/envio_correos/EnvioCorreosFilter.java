package models.utilitarios.envio_correos;

import java.net.InetAddress;

import megasoft.AbstractModel;
import megasoft.DataSet;
import megasoft.Logger;

import com.bdv.infi.dao.PlantillasMailDAO;
import com.bdv.infi.dao.TipoProductoDao;
import com.bdv.infi.data.PlantillaMail;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.PlantillasMailTipos;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaz_varias.EnvioCorreos;

public class EnvioCorreosFilter extends AbstractModel {
	
	PlantillasMailDAO plantillasMailDAO;
	DataSet datos, areas;
	String mensError = "", eventoId="";
	PlantillaMail pm;
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		
		storeDataSet("datos", datos); //Registra el dataset exportado por este modelo
//		storeDataSet("areas", areas); //Registra el dataset exportado por este modelo
		
//		plantillasMailDAO.getStatusOrdenPlantilla(); //Obtiene los estatus de orden que pueden asociarse a una plantilla
//		storeDataSet("status_orden_plant", plantillasMailDAO.getDataSet()); //Registra el dataset exportado por este modelo
//		
//		plantillasMailDAO.getTransaccionesPlantilla(); //Obtiene las transacciones que pueden asociarse a una plantilla
//		storeDataSet("transac_plant", plantillasMailDAO.getDataSet()); //Registra el dataset exportado por este modelo
		
		//NM29643 infi_TTS_466 09/07/2014
		TipoProductoDao tipoProdDao = new TipoProductoDao(_dso);
		
		tipoProdDao.listarProductosConEventos(); //Obtiene los tipos de productos con eventos de envio de correos asociados
		storeDataSet("productos", tipoProdDao.getDataSet()); //Registra el dataset exportado por este modelo
		
//		plantillasMailDAO.obtenerTiposDestinatario(); //Obtiene los distintos tipos de destinatarios
//		storeDataSet("tipos_dest", plantillasMailDAO.getDataSet()); //Registra el dataset exportado por este modelo
		
		//System.out.println("RECORD\n "+_record);
		storeDataSet("record", _record);
	}
	
	
	public boolean isValid() throws Exception {
		
		boolean valido = true;
		
		eventoId = _req.getParameter("evento");
		String eventoName = _req.getParameter("evento_name")==null?"":_req.getParameter("evento_name");
		String producto = _req.getParameter("producto");
		
		_record.setValue("evento", eventoId);
		_record.setValue("evento_name", eventoName);

Logger.debug(this, "Evento RECORD: "+_record.getValue("evento"));
Logger.debug(this, "EventoName RECORD: "+_record.getValue("evento_name"));
Logger.debug(this, "Evento REQUEST: "+eventoId+" - Producto: "+producto);
Logger.debug(this, "EventoName REQUEST: "+eventoName);
		
		plantillasMailDAO = new PlantillasMailDAO(_dso);
		datos = new DataSet();
		areas = new DataSet();
		datos.append("plantilla_id", java.sql.Types.VARCHAR);
		datos.append("plantilla_name", java.sql.Types.VARCHAR);
		datos.append("areas_display", java.sql.Types.VARCHAR);
		datos.append("campos_cliente_display", java.sql.Types.VARCHAR);
		datos.append("mens_error", java.sql.Types.VARCHAR);
		datos.append("producto_display", java.sql.Types.VARCHAR);
		datos.append("evento_display", java.sql.Types.VARCHAR);
		datos.append("evento_name", java.sql.Types.VARCHAR);
		datos.addNew();
		datos.setValue("plantilla_id", "");
		datos.setValue("plantilla_name", "");
		datos.setValue("areas_display", "none");
		datos.setValue("campos_cliente_display", "none");
		datos.setValue("mens_error", mensError);
		datos.setValue("producto_display", "block");
		datos.setValue("evento_display", "none");
		datos.setValue("evento_name", eventoName);
		
		//Se verifica que no exista ciclo de este tipo abierto
		//Creacion de objeto EnvioCorreos
		InetAddress direccion = InetAddress.getLocalHost();
    	String direccionIpstr = direccion.getHostAddress();
    	EnvioCorreos ec = new EnvioCorreos(_dso, null, direccionIpstr);
		//Se inicializan los parametros relacionados con el envio de correos
		ec.initParamEnvio();
		
		//Devuelve 0 si no hay ciclo abierto de este tipo
		if(ec.verificarCicloExistente(TransaccionNegocio.ENVIO_CORREOS, null) > 0){
			_record.addError("Ciclo "+TransaccionNegocio.ENVIO_CORREOS.replaceAll("_", " "), "No se puede iniciar un ciclo de env&iacute;o de correos puesto que ya existe un ciclo de este tipo abierto. Para cerrarlo acceda a la opci&oacute;n 'Confirmaci&oacute;n' y confirme/descarte los correos filtrados en dicho ciclo.");
			valido = false;
		}
		
		if(valido && eventoId!=null && !eventoId.equals("")){
Logger.debug(this, "Evento seteado");
			datos.setValue("producto_display", "none");
			pm = new PlantillaMail();
			pm.setEventoId(eventoId);
			pm.setEstatusActivacion(String.valueOf(ConstantesGenerales.VERDADERO));
			plantillasMailDAO.listarPlantillasMail(pm, null, null, null, null);
			plantillasMailDAO.getDataSet();
			if(plantillasMailDAO.getDataSet().count()>0){
Logger.debug(this, "Encontro datos plantilla");
				datos.setValue("evento_display", "block");
				plantillasMailDAO.getDataSet().first();
				plantillasMailDAO.getDataSet().next();
				datos.setValue("plantilla_id", plantillasMailDAO.getDataSet().getValue("PLANTILLA_MAIL_ID"));
				datos.setValue("plantilla_name", plantillasMailDAO.getDataSet().getValue("PLANTILLA_MAIL_NAME"));
				if(plantillasMailDAO.getDataSet().getValue("TIPO_DESTINATARIO").equalsIgnoreCase(PlantillasMailTipos.TIPO_DEST_FUNCIONAL_COD)){//Tipo de destinatario Funcional
Logger.debug(this, "Funcional");
					plantillasMailDAO.listarAreasPlantillasMail(datos.getValue("plantilla_id"), ConstantesGenerales.VERDADERO+"");
					if(plantillasMailDAO.getDataSet().count()>0){
Logger.debug(this, "Encuentra areas");
						areas = plantillasMailDAO.getDataSet();
						datos.setValue("areas_display", "block");
					}else{
						mensError = "<span style=\"color:red;\">Error:</span> No se encontr&oacute; ningun &aacute;rea activa asociada a la plantilla configurada para el estatus de orden: "+_req.getParameter("name_status_orden_plant")+", la transacci&oacute;n: "+_req.getParameter("name_transac_plant")+" y el tipo de destinatario: "+_req.getParameter("name_dest");
						Logger.info(this, "ENVIO CORREOS: No se encontro ningun area activa asociada a la plantilla configurada para el estatus de orden: "+_req.getParameter("name_status_orden_plant")+", la transacci&oacute;n: "+_req.getParameter("name_transac_plant")+" y el tipo de destinatario: "+_req.getParameter("name_dest"));
						//valido = false;
						//_record.addError("Plantilla &Aacute;reas", "No se encontr&oacute; ninguna &aacute;rea asociada a la plantilla configurada para el estatus de orden: "+_req.getParameter("name_status_orden_plant")+", la transacci&oacute;n: "+_req.getParameter("name_transac_plant")+" y el tipo de destinatario: "+_req.getParameter("name_dest"));
					}
				}else{ //Destinatario Cliente
Logger.debug(this, "Cliente");
					datos.setValue("campos_cliente_display", "block");
				}
			}else{
				mensError = "<span style=\"color:red;\">Error:</span> No se encontr&oacute; ninguna plantilla activa para el evento: "+eventoId;
				Logger.info(this, "ENVIO CORREOS: No se encontr&oacute; ninguna plantilla activa para el evento: "+eventoId);
				//valido = false;
				//_record.addError("Plantilla", "No se encontr&oacute; ninguna plantilla para el estatus de orden: "+_req.getParameter("name_status_orden_plant")+", la transacci&oacute;n: "+_req.getParameter("name_transac_plant")+" y el tipo de destinatario: "+_req.getParameter("name_dest"));
			}
		}else Logger.debug(this, "Datos para busqueda plantilla faltantes");
		
		datos.setValue("mens_error", mensError);
		
		return valido;
	}
}
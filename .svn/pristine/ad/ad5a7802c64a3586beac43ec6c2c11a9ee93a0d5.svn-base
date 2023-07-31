package models.utilitarios.mensajes_interfaces;

import megasoft.AbstractModel;
import megasoft.DataSet;

import org.apache.log4j.Logger;

import com.bdv.infi.logic.interfaz_varias.InterfaceBcvTxt;
import com.bdv.infi.logic.interfaz_varias.InterfaceCarmen;
import com.bdv.infi.logic.interfaz_varias.InterfaceEstadistica;
import com.bdv.infi.logic.interfaz_varias.MensajeBcv;
import com.bdv.infi.logic.interfaz_varias.MensajeCarmen;
import com.bdv.infi.logic.interfaz_varias.MensajeEstadistica;
import com.bdv.infi.util.Utilitario;

/**
 * Clase encargada de armar las ordenes con sus respectivas operaciones para ser enviadas hacia <b>SWIFT</b><br>
 */

public class ProcesarEnvio extends AbstractModel {

	private Logger logger = Logger.getLogger(ProcesarEnvio.class);
	private String[] mensajesEnviar;

	@Override
	public void execute() throws Exception {
		logger.debug("Procesando Envío de Mensajes para Interfaces..");

		DataSet _datos = new DataSet();
		_datos.append("error", java.sql.Types.VARCHAR);
		_datos.append("en_proceso", java.sql.Types.VARCHAR);
		_datos.addNew();
		_datos.setValue("error", "");
		_datos.setValue("en_proceso", "");

		InterfaceBcvTxt interfaceBcvTxt = new InterfaceBcvTxt(_dso, mensajesEnviar);
		InterfaceCarmen interfaceCarmen = new InterfaceCarmen(_dso, mensajesEnviar);
		InterfaceEstadistica interfaceEstadistica = new InterfaceEstadistica(_dso, mensajesEnviar);

		try {
			// --Procesar envio de mensajes seleccionados----------------------
			if (_record.getValue("tipo_mensaje").equals(MensajeBcv.TIPO_MENSAJE)) {
				// Borramos el template
				_config.template = null;
				_config.viewClass = null;
				interfaceBcvTxt.run(_res);
			} else if (_record.getValue("tipo_mensaje").equals(MensajeCarmen.TIPO_MENSAJE + MensajeCarmen.ENTRADA)) {
				interfaceCarmen.setTipoMensaje(MensajeCarmen.TIPO_MENSAJE + MensajeCarmen.ENTRADA);
				interfaceCarmen.setIdMensajes(idMensajesToString());
				interfaceCarmen.run();
			} else if (_record.getValue("tipo_mensaje").equals(MensajeCarmen.TIPO_MENSAJE + MensajeCarmen.SALIDA)) {
				interfaceCarmen.setTipoMensaje(MensajeCarmen.TIPO_MENSAJE + MensajeCarmen.SALIDA);
				interfaceCarmen.setIdMensajes(idMensajesToString());
				interfaceCarmen.run();				
			} else if (_record.getValue("tipo_mensaje").equals(MensajeEstadistica.TIPO_MENSAJE)) {
				interfaceEstadistica.run();
			}
			//----------------------------------------------------------------
			if((!interfaceEstadistica.isPuedeIniciar()||!interfaceCarmen.isPuedeIniciar()||!interfaceBcvTxt.isPuedeIniciar())){
				_datos.setValue("error", "<font color='red'> No se pudo iniciar el proceso, ya existe uno en ejecuci&oacute;n.</font></br> Por favor consulte la sección <a href='consulta_procesos-filter'>Consulta de Procesos</a>");
			}else			
				_datos.setValue("en_proceso", "El proceso de env&iacute;o de mensajes se ha iniciado. Por favor consulte la sección <a href='consulta_procesos-filter'>Consulta de Procesos</a>");

		} catch (Exception e) {
			e.printStackTrace();
			_datos.setValue("error", "Ha ocurrido un error en el procesamiento de env&iacute;o de Mensajes " + e.getMessage());
			logger.error(e.getMessage() + Utilitario.stackTraceException(e));
		}

		storeDataSet("datos", _datos);

	}

	/**
	 * Validaciones generales del modelo
	 */
	public boolean isValid() throws Exception {
		boolean flag = super.isValid();

		if (flag) {
			mensajesEnviar = _req.getParameterValues("mensajes");

			if (mensajesEnviar == null) {
				_record.addError("Mensajes a Enviar", "Debe seleccionar al menos un mensaje para procesar el envío");
				return false;
			}
		}
		return flag;
	}

	/**
	 * Convierte el array de mensajes a una cadena separadas por ","
	 * @return cadena de id de mensajes separados por ","
	 */
	private String idMensajesToString(){
		StringBuilder sb = new StringBuilder(); 
		for (int i = 0; i < mensajesEnviar.length; i++) {
			sb.append(mensajesEnviar[i]);
			if (i != mensajesEnviar.length -1){
				sb.append(",");
			}
		}
		if (logger.isDebugEnabled()){
			logger.info("Id Mensajes " + sb.toString());	
		}		
		return sb.toString();
	}

}

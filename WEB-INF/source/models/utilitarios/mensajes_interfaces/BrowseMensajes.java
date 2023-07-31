package models.utilitarios.mensajes_interfaces;
import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.MensajeDAO;
import com.bdv.infi.logic.interfaz_varias.MensajeBcv;
/**
 * Clase encargada de mostrar las operaciones enviadas o no V&iacute;a interfaz SWIFT
 */
public class BrowseMensajes extends MSCModelExtend{
	
	private DataSet _datos = new DataSet();
	private String hiddenStream = "\n<input type='hidden' name='framework.controller.outputstream.open' value='false'/>";
	
	@Override
	public void execute() throws Exception {
			
		MensajeDAO mensajeDAO = new MensajeDAO(_dso);
		
		_datos.append("colum_check_todos", java.sql.Types.VARCHAR);//columna para checkbox de envío "Todos"
		_datos.append("colum_check_individual", java.sql.Types.VARCHAR);//columna para checkbox por cada mensaje
		_datos.append("boton_envio", java.sql.Types.VARCHAR);//boton para procesar Envio
		
		armarCamposEnvioMensaje();
				
		//listar mensajes 
		mensajeDAO.listarMensajes(_record.getValue("fecha_desde"), _record.getValue("fecha_hasta"), _record.getValue("enviado"), _record.getValue("tipo_mensaje"),getNumeroDePagina(),getPageSize());
		
		//Arma la sección de paginación
		getSeccionPaginacion(mensajeDAO.getTotalDeRegistros(), getPageSize(), getNumeroDePagina());
			
		//Publicamos el dataset
		storeDataSet("registros",mensajeDAO.getDataSet());
		storeDataSet("total", mensajeDAO.getTotalRegistros(false));
		storeDataSet("request",getDataSetFromRequest());
		storeDataSet("record",_record);		
		storeDataSet("datos",_datos);		
	
	}
	
	
	private void armarCamposEnvioMensaje() throws Exception {
		//Armar checkbox para seleccionar mensajes a enviar
		//si se esta buscando mensjes enviados, no colocar checkbox
		_datos.addNew();
		if(_record.getValue("enviado")!=null && _record.getValue("enviado").equals("1")){
			
			_datos.setValue("colum_check_todos", "");
			_datos.setValue("colum_check_individual", "");		
			_datos.setValue("boton_envio", "");		
			
		}else{			
			_datos.setValue("colum_check_todos", "<th><input type=\"checkbox\" id=\"todos\" name=\"todos\" onclick=\"seleccionarTodos()\"/></th>");
			_datos.setValue("colum_check_individual", "<td nowrap align=\"center\"><input type=\"checkbox\" @envio@ name=\"mensajes\" id=\"@mensaje_id@\" value=\"@mensaje_id@\" onclick=\"validarSeleccion(this)\"/></td>");
					
			if (_record.getValue("tipo_mensaje").equals(MensajeBcv.TIPO_MENSAJE)){
				_datos.setValue("boton_envio", "<button  type=\"button\" onclick=\"procesar('seleccion',this);\">Procesar Envío</button>"+hiddenStream);	
			}else{
				_datos.setValue("boton_envio", "<button  type=\"button\" onclick=\"procesar('seleccion',this);\">Procesar Envío</button>");
			}
		}	
	}
}

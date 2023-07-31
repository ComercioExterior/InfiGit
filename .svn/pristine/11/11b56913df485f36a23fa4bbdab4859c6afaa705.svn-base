package models.cierre_sistema.activacion_cierre;

import java.util.ArrayList;
import java.util.HashMap;

import megasoft.AbstractModel;
import megasoft.DataSet;
import org.apache.log4j.Logger;

import com.bdv.infi.dao.CierreSistemaDAO;
import com.bdv.infi.dao.ConfiguracionTasaDAO;
import com.bdv.infi.logic.cierre_sistema.ValidadorCierreSistema;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.util.Utilitario;

/**
 * Clase encargada de recuperar datos necesarios para la construcci&oacute;n del fitro de busqueda de la Consulta de Titulos en Custodia de los Clientes
 * 
 * @author Erika Valerio, Megasoft Computaci&oacute;n, nvisbal
 */
public class Confirm extends AbstractModel {
	
	private String accionSeleccionada;
	private String estatusCierre;
	private String fechaPrecierre;
	private String fechaSistema;	
	private String descripcionAccionSeleccionada;
	private final String DESACTIVADO="0";
	private final String ACTIVADO="1";
	
	private DataSet _activacionCierre;
	
	private Logger logger = Logger.getLogger(Confirm.class);
	private ValidadorCierreSistema valCierreSistema;
	private HashMap<String, Object> parametrosEntrada;
	private ArrayList listaMensajes = new ArrayList();
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	
	public void execute() throws Exception {
		
		if(accionSeleccionada.equals(ACTIVADO)){//Activar
			descripcionAccionSeleccionada="Activación de Cierre Sistema";
		}else{//Desactivar
			descripcionAccionSeleccionada="Desactivación de Cierre Sistema";	
		}
		
		_activacionCierre=new DataSet();		
		_activacionCierre.append("fecha_sistema",java.sql.Types.VARCHAR);
		_activacionCierre.append("fecha_precierre",java.sql.Types.VARCHAR);
		_activacionCierre.append("accion_solicitada",java.sql.Types.VARCHAR);
		_activacionCierre.append("usuario",java.sql.Types.VARCHAR);
		_activacionCierre.append("action",java.sql.Types.VARCHAR);
		_activacionCierre.addNew();
		_activacionCierre.setValue("fecha_sistema",fechaSistema);
		_activacionCierre.setValue("fecha_precierre",fechaPrecierre);			
		_activacionCierre.setValue("accion_solicitada",descripcionAccionSeleccionada);
		_activacionCierre.setValue("usuario",this.getUserName());
		_activacionCierre.setValue("action",accionSeleccionada);
				
		storeDataSet("activacion_cierre",_activacionCierre);									
	}

	public boolean isValid()throws Exception{		
		
		boolean flag = true;
		valCierreSistema = new ValidadorCierreSistema(_dso);
		parametrosEntrada = new HashMap<String, Object>();
		
		//--Asignaciones Previas:		
		if(_req.getParameter("accion")!=null && !_req.getParameter("accion").equals("")){
			accionSeleccionada= _req.getParameter("accion");	
			parametrosEntrada.put(ValidadorCierreSistema.ACCION, accionSeleccionada);
			
		}			
		if(_req.getParameter("fecha_pre_cierre")!=null && !_req.getParameter("fecha_pre_cierre").equals("")){
			fechaPrecierre=_req.getParameter("fecha_pre_cierre");	
			parametrosEntrada.put(ValidadorCierreSistema.FECHA_PRECIERRE, fechaPrecierre);
		}		
		if(_req.getParameter("fecha_sistema")!=null && !_req.getParameter("fecha_sistema").equals("")){
			fechaSistema=_req.getParameter("fecha_sistema");	
			parametrosEntrada.put(ValidadorCierreSistema.FECHA_SISTEMA, fechaSistema);
		}
		
		//Inicializar por defecto validaciones en true de cada proceso (Pueden ser cambiados al setear los parametros en las llamadas al validador)
		parametrosEntrada.put(ValidadorCierreSistema.VALIDAR_BUEN_VALOR, true);
		parametrosEntrada.put(ValidadorCierreSistema.VALIDAR_CONTABILIDAD, true);

		
		//--LLamar al validador		
		valCierreSistema.setParametrosEntrada(parametrosEntrada);
		listaMensajes = valCierreSistema.validar();
				
		//Verificar si se retornaron mensajes de validación
		if (listaMensajes!=null && listaMensajes.size() > 0) {
			for (int i=0; i<listaMensajes.size(); i++) {
				_record.addError("Para su informaci&oacute;n", (String)listaMensajes.get(i));
			}
			flag = false;
		}				
					
		return flag;
	}	
	
}

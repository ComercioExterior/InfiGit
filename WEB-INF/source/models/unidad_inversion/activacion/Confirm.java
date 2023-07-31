package models.unidad_inversion.activacion;

import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;


/**
 * Clase que publica una pagina con la Unidad de Inversion para obtener la confirmacion de aplicar una eliminacion
 * @author Megasoft Computaci&oacute;n
 */
public class Confirm extends MSCModelExtend {
	 
	
	private String idUndInvProcesar;
	private String nombreUndInvProcesar;
	private String idUndInvActiva;
	private String nombreUndInvActiva;
	private String condicionActivacion;
	private String tipoManejoNecogio;//Manejo Alto Valor o Bajo Valor
	
	private String nombreCondicionActivacion;
	
	
	private UnidadInversionDAO undInversionDAO;
	private DataSet _configuracion=new DataSet();
	private DataSet _resumenUnidad; 
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {

		_configuracion=new DataSet();		
		_configuracion.append("mensaje",java.sql.Types.VARCHAR);
		_configuracion.append("undv_procesar",java.sql.Types.VARCHAR);
		_configuracion.append("condicion_activacion",java.sql.Types.VARCHAR);
		_configuracion.append("id_undv_procesar",java.sql.Types.VARCHAR);	
		_configuracion.append("nombre_condicion_activacion",java.sql.Types.VARCHAR);
		_configuracion.addNew();	
		
		_configuracion.setValue("undv_procesar",nombreUndInvProcesar);
		_configuracion.setValue("id_undv_procesar",idUndInvProcesar);
		_configuracion.setValue("condicion_activacion",condicionActivacion);
		
		if(condicionActivacion.equals(String.valueOf(ConstantesGenerales.STATUS_INACTIVO))){
			_configuracion.setValue("mensaje","Por favor confirme si desea realizar el proceso de <font color=red>DESACTIVACION</font> de la Unidad de inversion " + nombreUndInvProcesar);
			nombreCondicionActivacion="DESACTIVACION";
			
		} else if(condicionActivacion.equals(String.valueOf(ConstantesGenerales.STATUS_ACTIVO))){					
			nombreCondicionActivacion="ACTIVACION";
					
				_configuracion.setValue("mensaje","Por favor confirme si desea realizar el proceso de <font color=red>ACTIVACION</font> de la Unidad de inversion " + nombreUndInvProcesar);
			
		}						
		_configuracion.setValue("nombre_condicion_activacion",nombreCondicionActivacion);
		storeDataSet("configuracion",_configuracion);
	}
	
	public boolean isValid()throws Exception{
		boolean flag=true;
		
		if (_record.getValue("check_undinvact") == null) {			
			condicionActivacion="0";
		} else {
			condicionActivacion="1";	
		}		
		idUndInvProcesar=_req.getParameter("ui_id");
		nombreUndInvProcesar=_req.getParameter("undinv_nombre");
		
		tipoManejoNecogio=_req.getParameter("tipo_negocio");		
		undInversionDAO=new UnidadInversionDAO(_dso);
		
		undInversionDAO.listarUnidadesActivas(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL,null,ConstantesGenerales.STATUS_ACTIVO,Long.parseLong(tipoManejoNecogio));							
		if(undInversionDAO.getDataSet().count()>0){
			undInversionDAO.getDataSet().first();
			if(undInversionDAO.getDataSet().next()){
				idUndInvActiva=undInversionDAO.getDataSet().getValue("undinv_id");
				nombreUndInvActiva=undInversionDAO.getDataSet().getValue("undinv_nombre");
			}		



			
			if(!idUndInvActiva.equals(idUndInvProcesar) && condicionActivacion.equals(String.valueOf(ConstantesGenerales.STATUS_ACTIVO))){
				_record.addError("Para su Informaci&oacute;n","La unidad de inversion  " + nombreUndInvActiva + " se encuentra ACTIVA, debe desactivarla para poder realizar el proceso de Activacion de la Unidad "+ nombreUndInvProcesar );
				//_configuracion.setValue("mensaje","La unidad de inversion  " + nombreUndInvActiva + " se encuentra ACTIVA, ¿Desea <font color=red>DESACTIVARLA</font> para realizar la activacion de la unidad de Inversion  " + nombreUndInvProcesar + " ? ");
				return false;
			}
		}
		return flag;
	
	}
	

	
}

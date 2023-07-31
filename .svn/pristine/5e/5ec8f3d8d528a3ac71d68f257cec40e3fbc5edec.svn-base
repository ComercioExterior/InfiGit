package models.unidad_inversion.activacion;

import megasoft.AbstractModel;
import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

/**
 * Clase que recupera los datos de la pagina de Ingreso de una Unidad de Inversion y los registra en la base de datos
 * @author Megasoft Computaci&oacute;n
 */
public class Update extends MSCModelExtend {
	
	UnidadInversionDAO unidadInversionDAO=null;
	
	/**
	  * Nombre de la unidad de inversion 
	  * */
	private String idUndInvProcesar;
	private String nombreUndInvProcesar;
	
	private String condicionActivacion;	
	private String nombreCondicionActivacion;
	
	private DataSet _configuracion;
	
	public void execute() throws Exception {		
		
		unidadInversionDAO=new UnidadInversionDAO(_dso);
		
		idUndInvProcesar=_req.getParameter("ui_id");
		condicionActivacion=_req.getParameter("condicion_activacion");
		
		nombreUndInvProcesar=_req.getParameter("undv_procesar");				
		nombreCondicionActivacion=_req.getParameter("nombre_condicion_activacion");
		

		_configuracion=new DataSet();
		_configuracion.append("unidad_inversion",java.sql.Types.VARCHAR);
		_configuracion.append("condicion_activacion",java.sql.Types.VARCHAR);		
		_configuracion.addNew();
		_configuracion.setValue("unidad_inversion", nombreUndInvProcesar);
		_configuracion.setValue("condicion_activacion",nombreCondicionActivacion);
		
		//try {						
			unidadInversionDAO.activacionUnidaInv(idUndInvProcesar,condicionActivacion);			
		//}catch(Exception e){
			//throw new Exception("Ha ocurrido un error en el proceso de " + nombreCondicionActivacion + " de la unidad de inversion  " + nombreUndInvProcesar);
		//}
		
		storeDataSet("configuracion",_configuracion);
	}

}

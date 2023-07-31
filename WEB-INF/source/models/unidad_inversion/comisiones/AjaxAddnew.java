package models.unidad_inversion.comisiones;

import com.bdv.infi.dao.TipoPersonaDAO;
import com.bdv.infi.dao.UIBlotterDAO;
import com.bdv.infi.util.Utilitario;

import megasoft.AbstractModel;
import megasoft.DataSet;
import megasoft.Logger;

/**
 * Clase Gen&eacute;rica que dado un c&oacute;digo espec&iacute;fico permite invocar un m&eacute;todo para una funci&oacute;n ajax dentro de una vista
 * @author Megasoft Computaci&oacute;n
 */
public class AjaxAddnew extends AbstractModel {
			
	
	public void execute() throws Exception {
				
		try {
			
			this.ajaxAgregarReglaComisionUI();
			
		} catch (Exception e) {
			Logger.error(this,e.getMessage()+" "+Utilitario.stackTraceException(e));
			throw new Exception(e);			
		}
		
	}
		
	/**
	 * Permite buscar los datos necesarios para armar una nueva regla aplicable a una transacci&oacute;n espec&iacute;fica
	 * @throws Exception
	 */
	void ajaxAgregarReglaComisionUI() throws Exception{
		
		DataSet _datos = new DataSet();//dataSet para Datos especiales
		_datos.append("num_regla_transac", java.sql.Types.VARCHAR);

		//Recuperar el numero de regla de transacci&oacute;n en la cual va		
		int num_regla_transac = Integer.parseInt(String.valueOf(_req.getSession().getAttribute("num_regla_transac")));
		num_regla_transac = num_regla_transac + 1;
		
		//colocar en sesion el numero de regla de transacci&oacute;n
		_req.getSession().setAttribute("num_regla_transac", String.valueOf(num_regla_transac));		
		
		UIBlotterDAO uiblotterDAO = new UIBlotterDAO(_dso);
		TipoPersonaDAO tipoPersonaDAO = new TipoPersonaDAO(_dso);
	
		//Obtener unidad de inversión
		String idUnidadInversion = (String)_req.getSession().getAttribute("idUnidadInversion");
		
		uiblotterDAO.listarBlottersUI(Long.parseLong(idUnidadInversion));
		DataSet _blotters = uiblotterDAO.getDataSet();
		_blotters.first();
		while (_blotters.next()){
			if(_blotters.getValue("bloter_id").equals(_req.getParameter("blotter_id"))){	
				_blotters.setValue("selected", "selected");
			}else{	
				_blotters.setValue("selected", null);
			}
		}
		
		
		//listar tipos de persona
		if(_req.getParameter("blotter_id")!=null && !_req.getParameter("blotter_id").equals("-1")){
			tipoPersonaDAO.listarPorBlotterUnidad(idUnidadInversion, _req.getParameter("blotter_id"));
		}else{
			tipoPersonaDAO.listarPorBlotterUnidad(idUnidadInversion, null);
		}
	
		
		_datos.addNew();		
		_datos.setValue("num_regla_transac", String.valueOf(num_regla_transac));		
				
	
		storeDataSet("blotters", _blotters);
		storeDataSet("tipo_pers", tipoPersonaDAO.getDataSet());
		storeDataSet("datos", _datos);		
		
	}

}

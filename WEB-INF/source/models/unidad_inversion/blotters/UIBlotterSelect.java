package models.unidad_inversion.blotters;

import megasoft.AbstractModel;
import megasoft.DataSet;

import com.bdv.infi.dao.UIBlotterDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;


/**
 * Clase que publica una pagina con los Blotters que no estan asociados a una Unidad de Inversion editada
 * @author Megasoft Computaci&oacute;n
 */
public class UIBlotterSelect extends AbstractModel implements UnidadInversionConstantes{

	/**
	 * Criterios de busqueda
	 */
	private DataSet dsCriteriosUIBlotters = new DataSet();
	/**
	 * Identificador del registro a modificar
	 */
	private long idUnidadInversion = 0;
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {	
		
		//Recuperamos de session el numero de la accion y la mandamos a la vista
		String accion= getSessionObject("accion").toString();
		DataSet _accion=new DataSet();
		_accion.append("accion",java.sql.Types.VARCHAR);
		_accion.addNew();
		_accion.setValue("accion",accion);
		storeDataSet("accion", _accion);
		//fin de recuperacion y de envio a la vista
		
		String strIdUnidadInversion = (String)_req.getSession().getAttribute("idUnidadInversion");
		if (strIdUnidadInversion == null) {
			return;
		}
		idUnidadInversion = Long.parseLong(strIdUnidadInversion);

		DataSet dsBlotters = new DataSet();
		
		DataSet dsApoyo = new DataSet();
		dsApoyo.append("idUnidadInversion", java.sql.Types.VARCHAR);
		dsApoyo.append("boton_grabar_ini", java.sql.Types.VARCHAR);
		dsApoyo.append("boton_grabar_fin", java.sql.Types.VARCHAR);	
		dsApoyo.append("total_records", java.sql.Types.VARCHAR);			

		String entry = _req.getParameter("entry");	
		if (entry == null)
			entry = "insert";
				
		dsApoyo.addNew();
		dsApoyo.setValue("idUnidadInversion", idUnidadInversion+"");	
		
		// Si ya se tienen los filtros buscar la informacion
		if (entry.equalsIgnoreCase("unidadInversion")) {
			dsApoyo.setValue("boton_grabar_ini", "<!----");
			dsApoyo.setValue("boton_grabar_fin", "--->");	
			_req.getSession().removeAttribute("dsCriteriosUIBlotters");
		} else {
			UIBlotterDAO boBlotters = new UIBlotterDAO(_dso);
			if (entry.equalsIgnoreCase("insert")) {
				dsCriteriosUIBlotters = (DataSet)_req.getSession().getAttribute("dsCriteriosUIBlotters");
			} else {
				dsCriteriosUIBlotters.append("descripcionBlotter",java.sql.Types.VARCHAR);				
				dsCriteriosUIBlotters.addNew();
				if (_req.getParameter("descripcionBlotter") != null)
					dsCriteriosUIBlotters.setValue("descripcionBlotter", _req.getParameter("descripcionBlotter"));
				_req.getSession().removeAttribute("dsCriteriosUIBlotters");
				_req.getSession().setAttribute("dsCriteriosUIBlotters",dsCriteriosUIBlotters);
			}
			dsCriteriosUIBlotters.next();
			boBlotters.listarBlotterDescripcion(dsCriteriosUIBlotters.getValue("descripcionBlotter"), idUnidadInversion);
			dsBlotters = boBlotters.getDataSet();
			if (dsBlotters.count() == 0){
				dsApoyo.setValue("boton_grabar_ini", "<!----");
				dsApoyo.setValue("boton_grabar_fin", "--->");	
				_req.getSession().removeAttribute("dsCriteriosUIBlotters");
			} else {		
				dsApoyo.setValue("total_records", String.valueOf(dsBlotters.count()));
				dsApoyo.setValue("boton_grabar_ini", "");
				dsApoyo.setValue("boton_grabar_fin", "");
			}
			
			UnidadInversionDAO boUI = new UnidadInversionDAO(_dso);
			boUI.listarPorId(idUnidadInversion);	
	
		}

		//registrar los datasets exportados por este modelo
		storeDataSet("dsBlotters", dsBlotters);			
		storeDataSet("dsApoyo", dsApoyo);	
	}
}

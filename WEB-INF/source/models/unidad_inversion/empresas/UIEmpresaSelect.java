package models.unidad_inversion.empresas;

import megasoft.AbstractModel;
import megasoft.DataSet;

import com.bdv.infi.dao.UIEmpresaDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;


/**
 * Clase que publica una pagina con las Empresas que no estan asociados a una Unidad de Inversion editada
 * @author Megasoft Computaci&oacute;n
 */
public class UIEmpresaSelect extends AbstractModel implements UnidadInversionConstantes{

	/**
	 * Criterios de busqueda
	 */
	private DataSet dsCriteriosUIEmpresas = new DataSet();
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

		DataSet dsEmpresas = new DataSet();
		
		DataSet dsApoyo = new DataSet();
		dsApoyo.append("boton_grabar_ini", java.sql.Types.VARCHAR);
		dsApoyo.append("boton_grabar_fin", java.sql.Types.VARCHAR);	

		String entry = _req.getParameter("entry");	
		if (entry == null){
			entry = "insert";
		}

		UnidadInversionDAO boUI = new UnidadInversionDAO(_dso);
		int cant = boUI.listarPorId(idUnidadInversion);	
		if(cant == 0) {
			_record.addError("Para su informacion", "No hay Unidades de Inversion con los criterios dados");
			return;
		}		
		DataSet dsUI = boUI.getDataSet();
		dsUI.next();
		if (dsUI.getValue("undinv_status").equalsIgnoreCase(UISTATUS_CERRADA)){
			_record.addError("Para su informacion", "La Unidad de Inversion no esta en codiciones de ser modificada");
			return;
		}				
		dsApoyo.addNew();		
		// Si ya se tienen los filtros buscar la informacion
		if (entry.equalsIgnoreCase("unidadInversion")) {
			dsApoyo.setValue("boton_grabar_ini", "<!----");
			dsApoyo.setValue("boton_grabar_fin", "--->");	
			_req.getSession().removeAttribute("dsCriteriosUIEmpresas");
		} else {
			UIEmpresaDAO boEmpresas = new UIEmpresaDAO(_dso);
			if (entry.equalsIgnoreCase("insert")) {
				dsCriteriosUIEmpresas = (DataSet)_req.getSession().getAttribute("dsCriteriosUIEmpresas");
			} else {
				dsCriteriosUIEmpresas.append("nombreEmpresa",java.sql.Types.VARCHAR);				
				dsCriteriosUIEmpresas.append("rifEmpresa",java.sql.Types.VARCHAR);
				dsCriteriosUIEmpresas.addNew();
				if (_req.getParameter("nombreEmpresa") != null && !_req.getParameter("nombreEmpresa").equals("")) {
					dsCriteriosUIEmpresas.setValue("nombreEmpresa", _req.getParameter("nombreEmpresa"));
				}
				else if (_req.getParameter("rifEmpresa") != null && !_req.getParameter("rifEmpresa").equals("")) {
						dsCriteriosUIEmpresas.setValue("rifEmpresa", _req.getParameter("rifEmpresa"));
				} 
				_req.getSession().removeAttribute("dsCriteriosUIEmpresas");
				_req.getSession().setAttribute("dsCriteriosUIEmpresas",dsCriteriosUIEmpresas);
			}
			dsCriteriosUIEmpresas.next();
			// Si solo hay un campo se busca por descripcion
			if (dsCriteriosUIEmpresas.getValue("nombreEmpresa") != null) {
				boEmpresas.listarEmpresasNombre(dsCriteriosUIEmpresas.getValue("nombreEmpresa"), idUnidadInversion);
			} else if (dsCriteriosUIEmpresas.getValue("rifEmpresa") != null) {
				boEmpresas.listarEmpresasRIF(dsCriteriosUIEmpresas.getValue("rifEmpresa"), idUnidadInversion);
			} else {
				boEmpresas.listarEmpresas(idUnidadInversion);
			}
			dsEmpresas = boEmpresas.getDataSet();
			if (dsEmpresas.count() == 0){
				dsApoyo.setValue("boton_grabar_ini", "<!----");
				dsApoyo.setValue("boton_grabar_fin", "--->");	
				_req.getSession().removeAttribute("dsCriteriosUIEmpresas");
			} else {				
				dsApoyo.setValue("boton_grabar_ini", "");
				dsApoyo.setValue("boton_grabar_fin", "");
			}
		}
		//registrar los datasets exportados por este modelo
		storeDataSet("dsUnidadInversion", dsUI);			
		storeDataSet("dsEmpresas", dsEmpresas);			
		storeDataSet("dsApoyo", dsApoyo);	
	}
}

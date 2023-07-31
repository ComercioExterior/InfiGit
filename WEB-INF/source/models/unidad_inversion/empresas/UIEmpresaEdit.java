package models.unidad_inversion.empresas;

import java.util.ArrayList;

import megasoft.AbstractModel;
import megasoft.DataSet;

import com.bdv.infi.dao.UIEmpresaDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;


/**
 * Clase que publica una pagina con las Empresas de una Unidad de Inversion editada
 * @author Megasoft Computaci&oacute;n
 */
public class UIEmpresaEdit extends AbstractModel implements UnidadInversionConstantes {
	 
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
		
		idUnidadInversion = Long.parseLong(_req.getParameter("idUnidadInversion"));
		String idEmpresa = _req.getParameter("idEmpresa");
		
		UnidadInversionDAO boUI = new UnidadInversionDAO(_dso);
		boUI.listarPorId(idUnidadInversion);			
		DataSet dsUI = boUI.getDataSet();
		dsUI.next();
		if (dsUI.getValue("undinv_status").equalsIgnoreCase(UISTATUS_CERRADA)){
			_record.addError("Para su informacion", "La Unidad de Inversion no esta en codiciones de ser modificada");
			return;
		}
		
		// Buscar el Empresa a procesar
		UIEmpresaDAO boUIEmpresa = new UIEmpresaDAO(_dso);
		boUIEmpresa.listarPorId(idUnidadInversion, idEmpresa);
		if ( boUIEmpresa.getDataSet().count() == 0){
			_record.addError("Para su informacion", "La Unidad de Inversion no esta en codiciones de ser modificada");
			return;
		}

		// Obtener los valores de las tablas asociada
		ArrayList arregloDataSet = new ArrayList();
		UIEmpresaFK classFk = new UIEmpresaFK(_dso);
		Object objAux = classFk.execute();	
		if (objAux instanceof String) {
			throw new Exception((String) objAux);			
		}  else {
			arregloDataSet = (ArrayList)objAux;
		}
		//registrar los datasets exportados por este modelo
		storeDataSet("dsUnidadInversion", dsUI);			
		storeDataSet("dsUIEmpresa", boUIEmpresa.getDataSet());			
		storeDataSet("dsRoles", (DataSet)arregloDataSet.get(0));	
	}
}

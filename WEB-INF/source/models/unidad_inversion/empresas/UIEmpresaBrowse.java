package models.unidad_inversion.empresas;

import megasoft.AbstractModel;
import megasoft.DataSet;

import com.bdv.infi.dao.UnidadInversionDAO;


/**
 * Clase que publica una pagina con las Empresas de una Unidad de Inversion editada
 * @author Megasoft Computaci&oacute;n
 */
public class UIEmpresaBrowse extends AbstractModel {
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
		
		UnidadInversionDAO boUI = new UnidadInversionDAO(_dso);
		int cant = boUI.listarPorId(idUnidadInversion);	
		if(cant == 0) {
			_record.addError("Para su informacion", "No hay Unidades de Inversion con los criterios dados");
			return;
		}
		DataSet dsUnidadInversion = boUI.getDataSet();
		boUI.listarEmpresasPorUI(idUnidadInversion);	
		
		DataSet dsApoyo = new DataSet();
		dsApoyo.append("total_records", java.sql.Types.VARCHAR);
		dsApoyo.addNew();
		dsApoyo.setValue("total_records", "("+boUI.getDataSet().count()+")");
		
		//registrar los datasets exportados por este modelo
		storeDataSet("dsUnidadInversion", dsUnidadInversion);			
		storeDataSet("dsUIEmpresas", boUI.getDataSet());
		storeDataSet("dsApoyo", dsApoyo);
	}
}

package models.unidad_inversion.documentos;

import com.bdv.infi.dao.BlotterDAO;
import com.bdv.infi.dao.UIDocumentosDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import megasoft.AbstractModel;
import megasoft.DataSet;

public class UIDocumentosBrowse extends AbstractModel {
	
	/**
	 * Identificador del registro a modificar
	 */
	private long idUnidadInversion = 0;
	private String unidad = null;
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		//	Recuperamos de session el numero de la accion y la mandamos a la vista
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
		BlotterDAO uiBlo = new BlotterDAO(_dso);
		int cant = boUI.listarPorId(idUnidadInversion);	
		if(cant == 0) {
			_record.addError("Para su informacion", "No hay Unidades de Inversion con los criterios dados");
			return;
		}
		DataSet dsUnidadInversion = boUI.getDataSet();
		if(dsUnidadInversion.count()>0){
			while(dsUnidadInversion.next())
				unidad= dsUnidadInversion.getValue("undinv_nombre");
		}
		uiBlo.listarBlotters(String.valueOf(idUnidadInversion));
		
		DataSet dsApoyo = new DataSet();
		dsApoyo.append("unidad", java.sql.Types.VARCHAR);
		dsApoyo.append("boton_grabar_ini", java.sql.Types.VARCHAR);
		dsApoyo.append("boton_grabar_fin", java.sql.Types.VARCHAR);	
		dsApoyo.addNew();
		dsApoyo.setValue("unidad", unidad);
		if (boUI.getDataSet().count() == 0) {
			dsApoyo.setValue("boton_grabar_ini", "<!----");
			dsApoyo.setValue("boton_grabar_fin", "--->");	
		} else {
			dsApoyo.setValue("boton_grabar_ini", "");
			dsApoyo.setValue("boton_grabar_fin", "");	
		}
		
		//registrar los datasets exportados por este modelo
		storeDataSet("dsUnidadInversion", dsUnidadInversion);			
		storeDataSet("dsUIDocumentos", uiBlo.getDataSet());
		storeDataSet("datos", uiBlo.getTotalRegistros());
		storeDataSet("dsApoyo", dsApoyo);	
	}
}

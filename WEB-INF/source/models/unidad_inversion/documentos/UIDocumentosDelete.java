package models.unidad_inversion.documentos;

import megasoft.AbstractModel;
import megasoft.DataSet;
import megasoft.db;
import com.bdv.infi.dao.UIDocumentosDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

public class UIDocumentosDelete extends AbstractModel {

	/**
	 * Identificador del registro a modificar
	 */
	private int idUnidadInversion = 0;
	
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {	
		
		// Buscar la Unidad de Inversion para verificar si esta condiciones de ser modificada
		idUnidadInversion = Integer.parseInt((String)_req.getSession().getAttribute("idUnidadInversion"));
		
		UnidadInversionDAO boUI = new UnidadInversionDAO(_dso);
		int cant = boUI.listarPorId(idUnidadInversion);	

		if(cant == 0) {
			_record.addError("Para su informacion", "No hay Unidades de Inversion con los criterios dados");
			return;
		}
		
		DataSet ds = boUI.getDataSet();
		ds.next();
		if (ds.getValue("undinv_status").equalsIgnoreCase(UnidadInversionConstantes.UISTATUS_CERRADA)){
			_record.addError("Para su informacion", "La Unidad de Inversion no esta en codiciones de ser modificada");
			return;
		}
		
		
		// Buscar el Documento a procesar
		UIDocumentosDAO boUIDocumento = new UIDocumentosDAO(_dso);
		boUIDocumento.listar(String.valueOf(idUnidadInversion), _req.getParameter("documento_id"));
		if ( boUIDocumento.getDataSet().count() == 0){
			_record.addError("Para su informacion", "La Unidad de Inversion no esta en codiciones de ser modificada");
			return;
		}

		//  Aplicar persistencia
		String sql ="";
		sql= boUIDocumento.eliminar(_req.getParameter("id"));
		db.exec(_dso, sql);
	}
	
	/**
	 * Completar el URL a enviar con la clave del registro a recuperar
	 */
	public String getRedirectParameters() throws Exception  {
		return "idUnidadInversion=" + idUnidadInversion;
	}
}

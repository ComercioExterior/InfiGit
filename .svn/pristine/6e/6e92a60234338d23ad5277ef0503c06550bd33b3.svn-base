package models.unidad_inversion.documentos;

import megasoft.AbstractModel;
import megasoft.DataSet;
import com.bdv.infi.dao.UIDocumentosDAO;

public class UIDocumentosEdit extends AbstractModel {
	
	/**
	 * Identificador del registro a modificar
	 */
	private long idUnidadInversion = 0;
	private String unidad = null;
	private String idBloter = null;
	private String bloter = null;
	private String idPersona = null;
	private String persona = null;
	/**
	 * Ejecuta la transaccion del modelo
	 */
	
	public void execute() throws Exception {	
		
		unidad=_req.getParameter("unidad");	
		idBloter=_req.getParameter("id_blotter");
		bloter=_req.getParameter("blotter");
		idPersona=_req.getParameter("id_tipper");
		persona=_req.getParameter("persona");	
		
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
		
		DataSet dsApoyo = new DataSet();
		dsApoyo.append("idUnidadInversion", java.sql.Types.VARCHAR);
		dsApoyo.append("unidad", java.sql.Types.VARCHAR);
		dsApoyo.append("id_blotter", java.sql.Types.VARCHAR);
		dsApoyo.append("blotter", java.sql.Types.VARCHAR);
		dsApoyo.append("idpersona", java.sql.Types.VARCHAR);
		dsApoyo.append("persona", java.sql.Types.VARCHAR);			

		dsApoyo.addNew();
		dsApoyo.setValue("idUnidadInversion", idUnidadInversion+"");
		dsApoyo.setValue("unidad", unidad);
		dsApoyo.setValue("id_blotter", idBloter);
		dsApoyo.setValue("blotter", bloter);
		dsApoyo.setValue("idpersona", idPersona);
		dsApoyo.setValue("persona", persona);
		
		
		UIDocumentosDAO uIDocumentosDAO =new UIDocumentosDAO(_dso);
		
		uIDocumentosDAO.listarNombreDoc(idBloter, idPersona, String.valueOf(idUnidadInversion));
		DataSet nameDoc= uIDocumentosDAO.getDataSet();
		
		if(nameDoc.count()<=0){
			_config.template="table_no_data.htm";
		}
		storeDataSet("cartasDocumentos", nameDoc);
		storeDataSet("dsApoyo", dsApoyo);
	}	
}
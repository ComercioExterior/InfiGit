package models.unidad_inversion.documentos;

import com.bdv.infi.dao.TipoPersonaDAO;

import megasoft.AbstractModel;
import megasoft.DataSet;

public class UIDocumentosBrowsePersonas extends AbstractModel {

	/**
	 * Identificador del registro a modificar
	 */
	private long idUnidadInversion = 0;
	private String unidad = null;
	private String idBloter = null;
	private String bloter = null;
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	
	public void execute() throws Exception {	
		
		idBloter=_record.getValue("id_blotter");
		bloter=_record.getValue("blotter");
		unidad=_record.getValue("unidad");
		
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
		dsApoyo.append("boton_grabar_ini", java.sql.Types.VARCHAR);
		dsApoyo.append("boton_grabar_fin", java.sql.Types.VARCHAR);	
		dsApoyo.append("total_records", java.sql.Types.VARCHAR);			

		dsApoyo.addNew();
		dsApoyo.setValue("idUnidadInversion", idUnidadInversion+"");
		dsApoyo.setValue("unidad", unidad);	
		dsApoyo.setValue("id_blotter", idBloter);
		dsApoyo.setValue("blotter", bloter);
		
		TipoPersonaDAO tiPer = new TipoPersonaDAO(_dso);
		tiPer.listarPorBlotterUnidad(String.valueOf(idUnidadInversion), idBloter);
		
		if(tiPer.getDataSet().count()<=0){
			_config.template="table_no_data.htm";
		}
		storeDataSet("personas", tiPer.getDataSet());
		storeDataSet("dsApoyo", dsApoyo);
		_req.getSession().setAttribute("unidad", unidad);
		_req.getSession().setAttribute("bloter", bloter);
	}
	
}

package models.configuracion.documentos.definicion;

import java.text.DateFormat;
import java.util.Date;
import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.DocumentoDefinicionDAO;
import com.bdv.infi.data.DocumentoDefinicion;

public class Update extends MSCModelExtend {

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
		
		String usuario=getSessionObject("framework.user.principal").toString();
		
		DocumentoDefinicionDAO confiD = new DocumentoDefinicionDAO(_dso);
		DocumentoDefinicion documentoDefinicion = new DocumentoDefinicion();
		
		String user=confiD.idUserSession(usuario);
		documentoDefinicion.setIdUnidadInversion(Integer.parseInt(_req.getParameter("undinv_id")));
		documentoDefinicion.setTransaId(_req.getParameter("transa_id"));
		documentoDefinicion.setDocumentoId(Integer.parseInt(_req.getParameter("documento_id")));
		documentoDefinicion.setRutaDocumento(_req.getParameter("nombre_doc.tempfile"));
		documentoDefinicion.setNombreDoc(_req.getParameter("nombre_doc.filename"));
		documentoDefinicion.setModificarDocumento(Short.parseShort(_req.getParameter("archivo")));
		documentoDefinicion.setStatusDocumento(_req.getParameter("status_documento"));
		documentoDefinicion.setAproUsuarioUserid(user);
		documentoDefinicion.setTipoPersona(_req.getParameter("tipper_id"));
		
    	Date fechaActual = new Date();
   		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
        Date fecha = df.parse(df.format(fechaActual));
 
		documentoDefinicion.setAproFecha(fecha);
				
		confiD.modificar(documentoDefinicion);
		
	}
}
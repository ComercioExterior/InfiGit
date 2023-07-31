package models.configuracion.documentos.definicion;

import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.DocumentoDefinicionDAO;
import com.bdv.infi.data.DocumentoDefinicion;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

public class Insert extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
public void execute() throws Exception {
		
		String usuario=getSessionObject("framework.user.principal").toString();
		
		DocumentoDefinicionDAO confiD = new DocumentoDefinicionDAO(_dso);
		DocumentoDefinicion documentoDefinicion = new DocumentoDefinicion();
		
		String user=confiD.idUserSession(usuario);
		documentoDefinicion.setCreUsuarioUserid(user);
		documentoDefinicion.setIdUnidadInversion(Integer.parseInt(_req.getParameter("undinv_id")));
		documentoDefinicion.setTransaId(_req.getParameter("transa_id"));
		documentoDefinicion.setRutaDocumento(_req.getParameter("ruta_documento.tempfile"));
		documentoDefinicion.setNombreDoc(_req.getParameter("ruta_documento.filename"));
		documentoDefinicion.setTipoPersona(_req.getParameter("tipper_id"));
		documentoDefinicion.setStatusDocumento(ConstantesGenerales.STATUS_REGISTRADO);
		confiD.insertar(documentoDefinicion);
		
	}
}
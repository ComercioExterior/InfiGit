package models.configuracion.documentos.definicion;

import megasoft.db;
import models.msc_utilitys.*;
import com.bdv.infi.dao.DocumentoDefinicionDAO;
import com.bdv.infi.data.DocumentoDefinicion;

public class Delete extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		DocumentoDefinicionDAO confiD = new DocumentoDefinicionDAO(_dso);
		DocumentoDefinicion documentoDefinicion = new DocumentoDefinicion();
		
		String sql ="";
		
		documentoDefinicion.setDocumentoId(Integer.parseInt(_req.getParameter("documento_id")));
		
		sql=confiD.eliminar(documentoDefinicion);
		db.exec(_dso, sql);
	}
}
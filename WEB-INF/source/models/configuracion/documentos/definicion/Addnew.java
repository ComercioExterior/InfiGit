package models.configuracion.documentos.definicion;

import models.msc_utilitys.*;
import com.bdv.infi.dao.DocumentoDefinicionDAO;
import com.bdv.infi.dao.TipoPersonaDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

public class Addnew extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		DocumentoDefinicionDAO confiD = new DocumentoDefinicionDAO(_dso);
		UnidadInversionDAO undinv = new UnidadInversionDAO(_dso);
		TipoPersonaDAO tipoPerDAO = new TipoPersonaDAO(_dso);
		
		String status[] = {
				UnidadInversionConstantes.UISTATUS_PUBLICADA
			    };
		
		//Buscar los tipos de personas
		confiD.transacciones();
		undinv.listaPorStatus(status,null);
		tipoPerDAO.listarTodos();
		storeDataSet("tipoPer", tipoPerDAO.getDataSet());
		storeDataSet("transacciones",confiD.getDataSet());
		storeDataSet("unidad", undinv.getDataSet());
	}
}
package models.configuracion.documentos.definicion;

import megasoft.DataSet;
import models.msc_utilitys.*;
import com.bdv.infi.dao.DocumentoDefinicionDAO;
import com.bdv.infi.dao.TipoPersonaDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

public class Edit extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		String status[] = {
				UnidadInversionConstantes.UISTATUS_PUBLICADA
			    };
	
		//Recuperamos de session el numero de la accion y la mandamos a la vista
		String accion= getSessionObject("accion").toString();
		DataSet _accion=new DataSet();
		_accion.append("accion",java.sql.Types.VARCHAR);
		_accion.addNew();
		_accion.setValue("accion",accion);
		storeDataSet("accion", _accion);
		//fin de recuperacion y de envio a la vista
		
		DocumentoDefinicionDAO confiD = new DocumentoDefinicionDAO(_dso);
		UnidadInversionDAO undinv = new UnidadInversionDAO(_dso);
		TipoPersonaDAO tipoPerDAO = new TipoPersonaDAO(_dso);
		
		String documento_id=null;
	
		if(_req.getParameter("documento_id")!=null){
			documento_id = _req.getParameter("documento_id");
		}

		//Realizar consulta
		confiD.listar(documento_id);
		undinv.listaPorStatus(status,null);
		//registrar los datasets exportados por este modelo
		storeDataSet("table", confiD.getDataSet());
		storeDataSet("unidad", undinv.getDataSet());
		confiD.transacciones();
		storeDataSet("transacciones",confiD.getDataSet());
		confiD.listarEstatus();
		storeDataSet("status",confiD.getDataSet());
		tipoPerDAO.listarTodos();
		storeDataSet("tipoPer", tipoPerDAO.getDataSet());
	}
}
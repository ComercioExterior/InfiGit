package models.configuracion.documentos.definicion;

import megasoft.DataSet;
import models.msc_utilitys.*;

import com.bdv.infi.dao.DocumentoDefinicionDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

public class Filter extends MSCModelExtend {
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		String status[] = {
				UnidadInversionConstantes.UISTATUS_PUBLICADA
			    };
		
		Utilitys.limpiarSesion(_req);
		//Buscamos el action para saber en que opcion estamos y saber a donde navegar (definicion documento o aprobacion de documentos)
		String action = getActionID();
		String definicion = "documentos_definicion-filter";
		String aprobacion = "documentos_aprobacion-filter";
		if (action.equals(definicion)){
			_req.getSession().setAttribute("accion", 1);
		}else if (action.equals(aprobacion)){
			_req.getSession().setAttribute("accion", 2);
		}
		
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
		
		confiD.listarEstatus();
		undinv.listaPorStatus(status,null);
		
		//Registrar el dataset exportados por este modelo
		storeDataSet("accion", _accion);
		storeDataSet("status",confiD.getDataSet());	
		storeDataSet("unidad",undinv.getDataSet());
		
		//obtener transacciones
		confiD.transacciones();
		storeDataSet("transacciones",confiD.getDataSet());
		
	}
}
package models.ordenes.cancelacion;

import java.text.SimpleDateFormat;
import java.util.Date;
import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

public class Documento extends MSCModelExtend {

	String idCliente= null; 
	String idOrden = null;
	String fecha_desde = null;
	String fecha_hasta = null;
	String unidad_inversion = null;
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		OrdenDAO confiD = new OrdenDAO(_dso);
		//Obtener id de la  orden
		idOrden = _req.getParameter("ordene_id");		
		confiD.listarDocumentosOrden(Long.parseLong(idOrden));
		storeDataSet("table", confiD.getDataSet());
		
		DataSet _parametrosRequest = getDataSetFromRequest();
		//registrar los datasets exportados por este modelo
		storeDataSet("params", _parametrosRequest);		
		
	}
}

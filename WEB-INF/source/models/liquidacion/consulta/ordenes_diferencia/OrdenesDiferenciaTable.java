 
package models.liquidacion.consulta.ordenes_diferencia;

/**
 * @author eel
 *
 */
import models.msc_utilitys.*;

import com.bdv.infi.dao.OrdenDAO;

public class OrdenesDiferenciaTable extends MSCModelExtend {
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		OrdenDAO confiD = new OrdenDAO(_dso);
	
		String unidad_inversion = null;
		
		if (_record.getValue("undinv_id")!=null)
			unidad_inversion= _record.getValue("undinv_id");

		//Realizar consulta
		confiD.listarOrdenesUniv(Long.parseLong(unidad_inversion),true,getNumeroDePagina(),getPageSize());
		//registrar los datasets exportados por este modelo
		storeDataSet("table", confiD.getDataSet());
		storeDataSet("total", confiD.getTotalRegistros(false));
		
		getSeccionPaginacion(confiD.getTotalDeRegistros(), getPageSize(), getNumeroDePagina());
	}
}
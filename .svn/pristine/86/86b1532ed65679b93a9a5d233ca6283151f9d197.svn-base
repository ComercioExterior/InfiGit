/**
 * 
 */
package models.utilitarios.consulta_procesos;
import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.ProcesosDAO;

/**
 * @author nev
 *
 */
public class ProcesosBrowse extends MSCModelExtend{

	public void execute() throws Exception {
		ProcesosDAO procesosDAO = new ProcesosDAO(_dso);
		String transaId = "";
		DataSet lista ;
		boolean enEjecucion = false;
		String nm = null;
		
		if (_record.getValue("enEjecucion") != null){
			enEjecucion = true;
		}
		
		if (_record.getValue("usuario") != null){
			nm = _record.getValue("usuario");
		}
//		if (_record.getValue("transaccion") != null){
//			transaId = _record.getValue("transaccion");
//		}
		procesosDAO.listarProcesoRangoFecha(_record.getValue("fe_desde"),_record.getValue("fe_hasta"),enEjecucion,null,nm);
		lista = procesosDAO.getDataSet();
//		DataSet _datos = new DataSet();
//		_datos.append("actualizar",java.sql.Types.VARCHAR);
//		_datos.addNew();
//		_datos.setValue("actualizar","/consulta_procesos-actualizar");
		storeDataSet("_lista", lista);
	}
}

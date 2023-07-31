/**
 * 
 */
package models.utilitarios.consulta_procesos;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.data.Proceso;

/**
 * @author nev
 *
 */
public class ProcesosActualizar extends MSCModelExtend{

	public void execute() throws Exception {
		ProcesosDAO procesosDAO = new ProcesosDAO(_dso);
		Proceso proceso = new Proceso();
		proceso.setDescripcionError("Cerrado por usuario " + getUserName());
		proceso.setEjecucionId(Integer.parseInt(_record.getValue("idejecucion")));
		db.exec(_dso, procesosDAO.modificar(proceso));
	}
}

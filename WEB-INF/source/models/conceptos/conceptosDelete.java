package models.conceptos;

import megasoft.db;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.ConceptosDAO;
/**
 * Clase que elimina un registro de oficina
 * @author elaucho
 */
public class conceptosDelete extends MSCModelExtend{
	public void execute() throws Exception {

		//DAO a utilizar
		ConceptosDAO conceptos = new ConceptosDAO(_dso);
		
		//Se ejcuta la transacción
		db.exec(_dso, conceptos.eliminar(_req.getParameter("codigo_id")));
	}//fin execute	
}//fin clase

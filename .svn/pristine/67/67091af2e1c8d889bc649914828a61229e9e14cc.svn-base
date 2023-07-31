package models.conceptos;

import com.bdv.infi.dao.ConceptosDAO;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;
/**
 * Clase encargada de actualizar un registro de oficina
 * @author elaucho
 */
public class conceptosUpdate extends MSCModelExtend{
	
	@Override
	public void execute() throws Exception {

		//DAO a utilizar
		ConceptosDAO conceptos = new ConceptosDAO(_dso);
		
		//Se ejcuta la transacción
		db.exec(_dso, conceptos.modificar(_req.getParameter("codigo_id"),_req.getParameter("concepto"),_req.getParameter("numeral")));
	}//fin execute	
}//fin clase

package models.conceptos;

import com.bdv.infi.dao.ConceptosDAO;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;
public class conceptosInsert extends MSCModelExtend{
	public void execute() throws Exception {
		
		//DAO a utilizar
		ConceptosDAO conceptos = new ConceptosDAO(_dso);

		//Se ejecuta el query
		
		try{
			//Se ejecuta el query
			db.exec(_dso, conceptos.insertar(_req.getParameter("codigo_id"),_req.getParameter("concepto"),_req.getParameter("numeral")));
			
		}catch (Exception e) {
			throw new Exception("Codigo del Concepto ya existe en Base de Datos.");
		}
		
		
	}//fin execute
}//Fin clase

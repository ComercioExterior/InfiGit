package models.conceptos;

import com.bdv.infi.dao.ConceptosDAO;
import models.msc_utilitys.MSCModelExtend;

/**
 * Clase encargada de buscar las oficinas que se encuentran registradas en base de datos
 * @author elaucho
 */
public class conceptosBrowse extends MSCModelExtend{
	
	@Override
	public void execute() throws Exception {
		
		//DAO a utilizar
		ConceptosDAO conceptos = new ConceptosDAO(_dso);
				
		//Listamos las oficinas registradas en base de datos
		conceptos.listar();
		
		//Publicamos el dataset
		storeDataSet("conceptos",conceptos.getDataSet());
		
	}//fin execute

}//Fin clase

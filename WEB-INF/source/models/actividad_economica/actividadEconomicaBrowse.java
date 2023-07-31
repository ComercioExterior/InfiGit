package models.actividad_economica;

import com.bdv.infi.dao.ActividadEconomicaDAO;
import models.msc_utilitys.MSCModelExtend;

/**
 * Clase encargada de buscar las oficinas que se encuentran registradas en base de datos
 * @author elaucho
 */
public class actividadEconomicaBrowse extends MSCModelExtend{
	
	@Override
	public void execute() throws Exception {
		
		//DAO a utilizar
		ActividadEconomicaDAO actividadEconomica = new ActividadEconomicaDAO(_dso);
				
		//Listamos las oficinas registradas en base de datos
		actividadEconomica.listar();
		
		//Publicamos el dataset
		storeDataSet("actividadeconomica",actividadEconomica.getDataSet());
		
	}//fin execute

}//Fin clase

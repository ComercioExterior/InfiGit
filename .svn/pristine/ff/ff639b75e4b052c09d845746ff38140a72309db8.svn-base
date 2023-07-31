package models.sector_productivo;

import com.bdv.infi.dao.SectorProductivoDAO;
import models.msc_utilitys.MSCModelExtend;

/**
 * Clase encargada de buscar las oficinas que se encuentran registradas en base de datos
 * @author elaucho
 */
public class sectorProductivoBrowse extends MSCModelExtend{
	
	@Override
	public void execute() throws Exception {
		
		//DAO a utilizar
		SectorProductivoDAO sectorProductivo = new SectorProductivoDAO(_dso);
				
		//Listamos las oficinas registradas en base de datos
		sectorProductivo.listar();
		
		//Publicamos el dataset
		storeDataSet("sectorproductivo",sectorProductivo.getDataSet());
		
	}//fin execute

}//Fin clase

package models.configuracion.generales.indicadores; 

import com.bdv.infi.dao.IndicadoresDAO;

import megasoft.*;


public class Table extends AbstractModel
{  
 
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{			
		IndicadoresDAO indicadoresDAO = new IndicadoresDAO(_dso);			
		
		//Realizar consulta
		//indicadoresDAO.listar();		
		
		//registrar los datasets exportados por este modelo
		//storeDataSet("table", indicadoresDAO.getDataSet());
		
		
		String indica_descripcion = _record.getValue("indica_descripcion");
		DataSet _page=new DataSet();
		//Realizar consulta
		indicadoresDAO.listarporfiltro(indica_descripcion);
		//codigo innecesario si se usa esto: storeDataSet("total", indicadoresDAO.getTotalRegistros());
		/*if(indicadoresDAO.getDataSet().count()==0){
			
			_page.append("page_total_records",java.sql.Types.VARCHAR);
			_page.addNew();
			_page.setValue("page_total_records","");
			
		}*/
		storeDataSet("page", _page);
		//registrar los datasets exportados por este modelo
		storeDataSet("table", indicadoresDAO.getDataSet());
		storeDataSet("total", indicadoresDAO.getTotalRegistros());

		
	}

}


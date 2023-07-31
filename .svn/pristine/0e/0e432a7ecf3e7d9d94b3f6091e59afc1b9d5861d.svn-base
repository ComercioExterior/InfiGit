package models.configuracion.generales.segmentacion; 

import com.bdv.infi.dao.InstrumentoFinancieroDAO;
import com.bdv.infi.dao.SegmentacionDAO;

import megasoft.*;


public class Table extends AbstractModel
{  
 
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{			
		SegmentacionDAO segmentacionDAO = new SegmentacionDAO(_dso);			
		
		String cteseg_descripcion = null;

		if ((_req.getParameter("cteseg_descripcion")!=null) || (_req.getParameter("cteseg_descripcion")!=""))
			cteseg_descripcion= _req.getParameter("cteseg_descripcion");		 
		
		//Realizar consulta
		segmentacionDAO.listarporfiltro(cteseg_descripcion);		
		
		//registrar los datasets exportados por este modelo
		storeDataSet("table", segmentacionDAO.getDataSet());
		
		


		
		
	}

}

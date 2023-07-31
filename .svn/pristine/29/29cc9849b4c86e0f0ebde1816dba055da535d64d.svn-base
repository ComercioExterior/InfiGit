package models.configuracion.generales.segmentacion; 

import com.bdv.infi.dao.SegmentacionDAO;

import megasoft.*;


public class Edit extends AbstractModel
{  
 
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{			
		SegmentacionDAO segmentacionDAO = new SegmentacionDAO(_dso);			
		
		//Busca el registro a editar
		segmentacionDAO.listar(_req.getParameter("cteseg_id"));		
		
		//registrar los datasets exportados por este modelo
		storeDataSet("table", segmentacionDAO.getDataSet());
		
		
		
	}

}

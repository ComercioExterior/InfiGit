package models.configuracion.portafolio_equivalencia; 
import com.bdv.infi.dao.EquivalenciaPortafolioDAO;
import megasoft.*;


public class Edit extends AbstractModel
{  
 
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{	
		EquivalenciaPortafolioDAO equivalenciaPortafolioDAO= new EquivalenciaPortafolioDAO(_dso);
		//Busca el registro a editar
		equivalenciaPortafolioDAO.listarEquivalenciaPortafolio(_req.getParameter("segmento_id"));
		//registrar los datasets exportados por este modelo
		storeDataSet("table", equivalenciaPortafolioDAO.getDataSet());
		
	}

}

package models.custodia.consultas.clientes_titulos_exportar;
import com.bdv.infi.dao.CustodiaDAO;
import megasoft.AbstractModel;
/**
 * 
 * @author elaucho
 */
public class Filter extends AbstractModel
{	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{
		//Obtener lista de todos los T&iacute;tulos existentes
		CustodiaDAO custDAO = new CustodiaDAO(_dso);	  	
		custDAO.listarTitulosEnCustodiaPorClienteAjax(0,false);	
		//registrar los datasets exportados por este modelo			
		storeDataSet("titulos", custDAO.getDataSet());
	}
}

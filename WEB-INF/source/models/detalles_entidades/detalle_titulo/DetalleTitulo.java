package models.detalles_entidades.detalle_titulo;

import com.bdv.infi.dao.TitulosDAO;
import megasoft.AbstractModel;

/**
 * Clase encargada de recuperar detalles de un t&iacute;tulo determinado. 
 * @author Erika Valerio, Megasoft Computaci&oacute;n
 */
public class DetalleTitulo extends AbstractModel
{
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{
		
		TitulosDAO titDAO = new TitulosDAO(_dso);
			
		//Obtener detalles del t&iacute;tulo
		titDAO.detallesTitulo(_req.getParameter("titulo_id"));
		
		//Exportar dataset con datos recuperados
		storeDataSet( "detalles", titDAO.getDataSet());
			
				
	}

}

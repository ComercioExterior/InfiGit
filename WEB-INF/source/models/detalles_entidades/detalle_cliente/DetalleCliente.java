package models.detalles_entidades.detalle_cliente;

import com.bdv.infi.dao.ClienteDAO;

import megasoft.AbstractModel;

/**
 * Clase encargada de recuperar detalles de un t&iacute;tulo determinado. 
 * @author Erika Valerio, Megasoft Computaci&oacute;n
 */
public class DetalleCliente extends AbstractModel
{
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{
		
		ClienteDAO clienteDAO = new ClienteDAO(_dso);
			
		//Obtener detalles del t&iacute;tulo
		clienteDAO.detallesCliente(_req.getParameter("client_id"));
		
		//Exportar dataset con datos recuperados
		storeDataSet( "detalles", clienteDAO.getDataSet());			
				
	}

}

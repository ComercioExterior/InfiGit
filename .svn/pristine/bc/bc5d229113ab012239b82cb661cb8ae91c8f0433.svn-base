package models.detalles_entidades.detalles_vehiculo_rol;

import com.bdv.infi.dao.VehiculoRolDAO;
import megasoft.AbstractModel;
import megasoft.DataSet;
import megasoft.Util;

/**
 * Clase encargada de recuperar detalles de un t&iacute;tulo determinado. 
 * @author Erika Valerio, Megasoft Computaci&oacute;n
 */
public class DetalleVehiculoRol extends AbstractModel
{
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{
		DataSet datalles = new DataSet();		
		String aux;
		VehiculoRolDAO vehiculoRolDAO = new VehiculoRolDAO(_dso);
		aux = _req.getParameter("vehiculo_rol_id");
						
		if(aux!=null && !aux.equals("")){
				
			//Obtener detalles
			vehiculoRolDAO.listarVehiculoRolPorId(Long.parseLong(aux));
			datalles = vehiculoRolDAO.getDataSet();
		
		}
		
		//Exportar dataset con datos recuperados
		storeDataSet( "detalles", datalles);
			
				
	}

}

package models.detalles_entidades.detalle_operacion_intentos;
import com.bancovenezuela.comun.data.OrdenOperacion;
import com.bdv.infi.dao.OperacionDAO;
import com.bdv.infi.dao.OrdenDAO;

import megasoft.AbstractModel;
import megasoft.DataSet;

/**
 * Clase encargada de recuperar detalles de un t&iacute;tulo determinado. 
 * @author elaucho
 */
public class DetalleOperacionIntentos extends AbstractModel
{
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{
		OperacionDAO operacion	=new OperacionDAO(_dso);
		OrdenOperacion operacion2	=new OrdenOperacion();
		operacion2.setIdOperacion(Long.parseLong(_req.getParameter("id_operacion")));
		
//		Listar las operaciones financieras
		operacion.listarIntentosOperacion(operacion2);
		
		DataSet _operaciones=new DataSet();
		_operaciones.append("operacion_intento_id",java.sql.Types.VARCHAR);
		_operaciones.append("fecha",java.sql.Types.VARCHAR);
		_operaciones.append("aplica",java.sql.Types.VARCHAR);
		_operaciones.append("error_desc",java.sql.Types.VARCHAR);
		_operaciones.append("color",java.sql.Types.VARCHAR);
		_operaciones.append("ordene_id",java.sql.Types.VARCHAR);
		_operaciones.append("ordene_operacion_id",java.sql.Types.VARCHAR);
		
		if(operacion.getDataSet().count()>0){
			operacion.getDataSet().first();
			while(operacion.getDataSet().next()){
				_operaciones.addNew();
				_operaciones.setValue("operacion_intento_id",operacion.getDataSet().getValue("operacion_intento_id"));
				_operaciones.setValue("fecha",operacion.getDataSet().getValue("fecha"));
				_operaciones.setValue("aplica",operacion.getDataSet().getValue("aplica"));
				_operaciones.setValue("error_desc",operacion.getDataSet().getValue("error_desc"));
				_operaciones.setValue("ordene_id",operacion.getDataSet().getValue("ordene_id"));
				_operaciones.setValue("ordene_operacion_id",operacion.getDataSet().getValue("ordene_operacion_id"));
				String color="";
				if(operacion.getDataSet().getValue("aplica").equalsIgnoreCase("No")){color="red";}
				_operaciones.setValue("color",color);
			}//fin while
		}//fin if
		
		//Publicacion del dataset
		storeDataSet("operaciones",_operaciones);
		
	}

}

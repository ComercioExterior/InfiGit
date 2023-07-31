/**
 * 
 */
package models.detalles_entidades.detalle_titulos_orden;

import javax.sql.DataSource;

import megasoft.AbstractModel;
import megasoft.DataSet;
import megasoft.db;

import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.TitulosDAO;
import com.bdv.infi.dao.UsuariosEspecialesDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

/**
 * Clase encargada de recuperar detalles de un t&iacute;tulo determinado. 
 * @author elaucho
 */
public class DetalleTitulosOrden extends AbstractModel
{
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{
		TitulosDAO titDAO = new TitulosDAO(_dso);
		DataSource _dsoSeguridad = db.getDataSource( _app.getInitParameter(ConstantesGenerales.DATASOURCE_SEGURIDAD_SEPA));
		UsuariosEspecialesDAO userEspecialDAO = new UsuariosEspecialesDAO(_dsoSeguridad);	
//		Datos Adicionales
		DataSet _datos = new DataSet();
		_datos.append("visibilidad_tasa_cambio",java.sql.Types.VARCHAR); //campo para visualizar o no la tasa de cambio dependiendo de si se trata del tipo de producto subasta de divisas
		_datos.addNew();
		_datos.append("visibilidad_tasa_pool",java.sql.Types.VARCHAR); //campo para visualizar o no la tasa pool (Mercado) dependiendo de si es o no un usuario especial
		_datos.addNew();
		
		//si es usuario especial mostrar tasa pool (mercado)
		boolean specialUser = false;
		if( userEspecialDAO.esUsuarioEspecial(getUserName()) ){
			specialUser = true;
		}
		
		//Se obtiene el tipo de producto asociado a la orden
		String ordenId = _req.getParameter("ord_id");
		OrdenDAO ordDAO = new OrdenDAO(_dso);
		ordDAO.listarOrdenPorId(Long.valueOf(ordenId), null);
		DataSet ordenDetalle = ordDAO.getDataSet();
		if(ordenDetalle.count()>0) {
			ordenDetalle.first();
			ordenDetalle.next();
			//Se obtienen los titulos segun el tipo de producto asociado a la orden
			if(ordenDetalle.getValue("TIPO_PRODUCTO_ID").equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA)) { //Query especifico para traer el detalle de los titulos asociados a subasta de divisas
				titDAO.listarTitulosFromOrdenSubDiv(ordenId);
				_datos.setValue("visibilidad_tasa_cambio", "display:block"); //Se muestra el campo tasa de cambio en el detalle
				_datos.setValue("visibilidad_tasa_pool", "display:none");
			}else{ //Para las ordenes no asociadas al producto subasta de divisas
				titDAO.listarTitulosOrden(ordenId); //Obtener detalles del t&iacute;tulo
				_datos.setValue("visibilidad_tasa_cambio", "display:none"); //No se muestra el campo tasa de cambio en el detalle
				if(specialUser) _datos.setValue("visibilidad_tasa_pool", "display:block");
			}
		}
		
		DataSet _ordene_id=new DataSet();
		_ordene_id.append("ordene_id",java.sql.Types.VARCHAR);
		_ordene_id.addNew();
		_ordene_id.setValue("ordene_id", ordenId);
		storeDataSet( "ordene_id", _ordene_id);
		
		/*_datos.append("visibilidad_tasa_pool",java.sql.Types.VARCHAR);//campo para visualizar o no la tasa pool (Mercado) dependiendo de si es o no un usuario especial
		_datos.addNew();*/
		
		//si es usuario especial mostrar tasa pool (mercado)
		/*if( userEspecialDAO.esUsuarioEspecial(getUserName()) && !ordenDetalle.getValue("TIPO_PRODUCTO_ID").equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA) ){
			_datos.setValue("visibilidad_tasa_pool", "display:block");
		}else{//si no ocultar tasa pool
			_datos.setValue("visibilidad_tasa_pool", "display:none");
		}*/
		
		//Exportar dataset con datos recuperados
		storeDataSet( "detalles", titDAO.getDataSet());
		storeDataSet( "datos", _datos);
	}

}

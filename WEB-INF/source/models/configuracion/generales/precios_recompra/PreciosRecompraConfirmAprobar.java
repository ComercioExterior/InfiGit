package models.configuracion.generales.precios_recompra;

import com.bdv.infi.dao.PrecioRecompraDAO;
import com.bdv.infi.data.PrecioRecompra;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;
/**
 * Clase que realiza la transaccion de buscar los precios de recompra por titulo especificado
 */
public class PreciosRecompraConfirmAprobar extends MSCModelExtend{

	public void execute() throws Exception {
		
		DataSet _filter = getDataSetFromRequest();
		//registrar los datasets exportados por este modelo
		storeDataSet("filter", _filter);		
	
	}
	
	public boolean isValid() throws Exception {	
		
		boolean flag = super.isValid();
	
		if(flag){
			PrecioRecompraDAO preciosRecompraDAO = new PrecioRecompraDAO(_dso);
			//llenar objeto con parametros
			PrecioRecompra precioRecompraData = new PrecioRecompra();
			precioRecompraData.setTituloId(_record.getValue("titulo_id"));
			precioRecompraData.setTipoProductoId(_record.getValue("tipo_producto_id"));

			if(preciosRecompraDAO.esAprobadoPrecioRecompraTitulo(precioRecompraData)){
				_record.addError("Precios Recompra","El precio de recompra actual del t&iacute;tulo "+_record.getValue("titulo_id")+ " y tipo de producto " +_record.getValue("tipo_producto_id")+" ya se encuentra aprobado.");
				flag = false;
			}else{//Verificar que el usuario aprobador no sea el mismo que ingreso el precio
				//obtener id numerico del usuario conectado
				String idUsuarioConectado = preciosRecompraDAO.idUserSession(getUserName());
				preciosRecompraDAO.listarPreciosRecompraTitulo(precioRecompraData);
				if(preciosRecompraDAO.getDataSet().next()){
					if(preciosRecompraDAO.getDataSet().getValue("usuario_id_act")!=null && preciosRecompraDAO.getDataSet().getValue("usuario_id_act").equals(idUsuarioConectado)){
						_record.addError("Precios Recompra","El usuario " +getUserName()+ " no puede aprobar el precio de recompra actual del t&iacute;tulo "+_record.getValue("titulo_id")+ " y tipo de producto " +_record.getValue("tipo_producto_id")+ ", puesto que se encuentra registrado como el usuario ingresador.");
						flag = false;
					}
				}
			}
		}
		return flag;
	}

}

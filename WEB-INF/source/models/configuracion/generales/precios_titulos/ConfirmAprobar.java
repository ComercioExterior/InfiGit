package models.configuracion.generales.precios_titulos;

import com.bdv.infi.dao.PreciosTitulosDAO;
import com.bdv.infi.data.TitulosPrecios;

import megasoft.DataSet;
import models.msc_utilitys.*;

public class ConfirmAprobar extends MSCModelExtend {
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		DataSet _filter = getDataSetFromRequest();
		//registrar los datasets exportados por este modelo
		storeDataSet("filter", _filter);		
	}
	
	public boolean isValid() throws Exception {	
		
		boolean flag = super.isValid();
	
		if(flag){
			PreciosTitulosDAO preciosTitulosDAO = new PreciosTitulosDAO(_dso);
			TitulosPrecios titulosPrecios = new TitulosPrecios();
			//LLenar objeto con parametros
			titulosPrecios.setIdTitulo(_record.getValue("titulo_id"));
			titulosPrecios.setTipoProductoId(_record.getValue("tipo_producto_id"));	
					
			if(preciosTitulosDAO.esAprobadoPrecioTitulo(titulosPrecios)){
				_record.addError("Precios Titulos","El precio actual del t&iacute;tulo "+_record.getValue("titulo_id")+ " y tipo de producto " +_record.getValue("tipo_producto_id")+ " ya se encuentra aprobado.");
				flag = false;
			}else{
				preciosTitulosDAO.detallePrecioTitulo(titulosPrecios);
				if(preciosTitulosDAO.getDataSet().next()){
					if(preciosTitulosDAO.getDataSet().getValue("usuario")!=null && preciosTitulosDAO.getDataSet().getValue("usuario").equals(getUserName())){
						_record.addError("Precios Titulos","El usuario " +getUserName()+ " no puede aprobar el precio actual del t&iacute;tulo "+_record.getValue("titulo_id")+ " y tipo de producto " +_record.getValue("tipo_producto_id")+", puesto que se encuentra registrado como el usuario ingresador.");
						flag = false;
					}
				}				
				
			}
		}
		return flag;
	}
}

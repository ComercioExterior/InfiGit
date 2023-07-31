package com.bdv.infi.logic.function.document;

import java.util.Map;

import javax.sql.DataSource;

import megasoft.DataSet;

import com.bdv.infi.dao.VehiculoDAO;
import com.bdv.infi.data.Orden;

/**Clase destinada a busca los datos de los vehículos asociados a una orden
 * Adiciona en el mapa los valores encontrados*/
public class DatosVehiculo extends DatosGenerales{

	public DatosVehiculo(DataSource ds, Map<String, String> mapa){
		super(ds,mapa);
	}
	
	/**Busca los datos del vehículo asociado a la orden*/
	public void buscarDatos(Object orden) throws Exception{
		Map<String, String> mapa = this.getMapa();
		Orden ObjOrden = (Orden) orden;
		
	    mapa.put("vehiculo_nombre","");
	    mapa.put("vehiculo_rif","");
		
		if (ObjOrden.getVehiculoTomador()!=null && !ObjOrden.getVehiculoTomador().equals("")){
			VehiculoDAO vehiculoDAO = new VehiculoDAO(this.getDataSource());
			vehiculoDAO.listarPorId(ObjOrden.getVehiculoTomador());
			DataSet dsVehiculo = vehiculoDAO.getDataSet();
			if (dsVehiculo.count()>0){
				dsVehiculo.first();
				dsVehiculo.next();			
			   mapa.put("vehiculo_nombre",dsVehiculo.getValue("vehicu_nombre"));
			   mapa.put("vehiculo_rif",dsVehiculo.getValue("vehicu_rif"));
			}
		}		
	}
}

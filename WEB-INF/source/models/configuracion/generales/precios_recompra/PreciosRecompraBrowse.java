
package models.configuracion.generales.precios_recompra;

import java.math.BigDecimal;
import com.bdv.infi.dao.MonedaDAO;
import com.bdv.infi.dao.PrecioRecompraDAO;
import com.bdv.infi.data.PrecioRecompra;
import com.bdv.infi.util.helper.Html;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;
/**
 * Clase que realiza la transaccion de buscar los precios de recompra por titulo especificado
 */
public class PreciosRecompraBrowse extends MSCModelExtend{
@Override
	public void execute() throws Exception {
	//Declaracion de Objeto
		String titulo_id = null;
		
		if (_record.getValue("titulo_id")!=null){
			titulo_id= _record.getValue("titulo_id");
		}
		
		PrecioRecompraDAO preciosRecompra = new PrecioRecompraDAO(_dso);
		
		//llenar objeto con parametros
		PrecioRecompra precioRecompraData = new PrecioRecompra();
		precioRecompraData.setTituloId(titulo_id);
		precioRecompraData.setTipoProductoId(_record.getValue("tipo_producto_id"));
		
		preciosRecompra.listarPreciosRecompra(precioRecompraData, _record.getValue("status_id"));
		
		//Se recorre el dataset para concatenar las monedas y via javascript seleccionarlas en el template
		String monedas="";
		if(preciosRecompra.getDataSet().count()>0){
			
			preciosRecompra.getDataSet().first();
			int j=1;
			while(preciosRecompra.getDataSet().next()){

				BigDecimal precioR = new BigDecimal(preciosRecompra.getDataSet().getValue("titulos_precio_recompra"));
				BigDecimal tasaPool= new BigDecimal(preciosRecompra.getDataSet().getValue("titulos_precio_pool"));
				preciosRecompra.getDataSet().setValue("precio_recompra", precioR.setScale(6,BigDecimal.ROUND_HALF_EVEN).toString());
				preciosRecompra.getDataSet().setValue("tasa_pool", tasaPool.setScale(6,BigDecimal.ROUND_HALF_EVEN).toString());
				
				//Si solo existe un registro sera igual a el, de lo contrario se concatenan
				monedas+=(preciosRecompra.getDataSet().count()==j)?preciosRecompra.getDataSet().getValue("moneda_id"):preciosRecompra.getDataSet().getValue("moneda_id")+"-";
				j++;
			}//fin while
		}//fin if
	
	//Se crea un dataset para mostrar las monedas por cada registro
		DataSet _moneda = new DataSet();
		_moneda.append("monedas",java.sql.Types.VARCHAR);
		_moneda.addNew();
		_moneda.setValue("monedas",monedas);
		//Listamos todas las monedas
		MonedaDAO monedaDao = new MonedaDAO(_dso);
		monedaDao.listar();
		
	//Se publica el dataset
		storeDataSet("precios_recompra", preciosRecompra.getDataSet());
		String botonProcesar = preciosRecompra.getDataSet().count()==0?"disabled":"enabled";
		storeDataSet("monedas", _moneda);
		storeDataSet("lista_monedas", monedaDao.getDataSet());
		storeDataSet("record", _record);
		storeDataSet("registros", preciosRecompra.getTotalRegistros());
		
	//Buscamos la descripcion del titulo para mostrarlo en caso de que no tenga registros
		String descripcion   = preciosRecompra.tituloDescripcion(titulo_id);
		DataSet _titulo_descripcion = new DataSet();
		_titulo_descripcion.append("titulo_descripcion",java.sql.Types.VARCHAR);
		_titulo_descripcion.append("deshabilitar_procesar",java.sql.Types.VARCHAR);
		_titulo_descripcion.addNew();
		_titulo_descripcion.setValue("titulo_descripcion", descripcion);
		_titulo_descripcion.setValue("deshabilitar_procesar",botonProcesar);
		storeDataSet("titulo_descripcion", _titulo_descripcion);
				
	}//fin execute
}

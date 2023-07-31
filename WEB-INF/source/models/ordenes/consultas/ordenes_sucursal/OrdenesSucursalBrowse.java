package models.ordenes.consultas.ordenes_sucursal;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

import models.msc_utilitys.MSCModelExtend;

public class OrdenesSucursalBrowse extends MSCModelExtend{

	@Override
	public void execute() throws Exception {
		OrdenDAO orden 			= new OrdenDAO(_dso);
		String sucursal			= null;
		String status			= null;
		String fechaDesde		= null;
		String fechaHasta		= null;
		//Sucursal
		if(!_record.getValue("sucursal").equals("all")){
			sucursal=_record.getValue("sucursal");
		}
		//Fechas
		if(_record.getValue("fe_ord_desde")!=null && _record.getValue("fe_ord_hasta")!=null){
			fechaDesde=_record.getValue("fe_ord_desde");
			fechaHasta=_record.getValue("fe_ord_hasta");	
		}
		//Status
		if(_record.getValue("ordsta_id")!=null && !_record.getValue("ordsta_id").equals("all")){
			status=_record.getValue("ordsta_id");
		}
		//Publicacion DataSet
		orden.listarMovimientosPorSucursal(sucursal,fechaDesde,fechaHasta,status,_record.getValue("transaccion"),true,getNumeroDePagina(),getPageSize());
		storeDataSet("sucursales", orden.getDataSet());
		storeDataSet("registros", orden.getTotalRegistros(false));
		
		//Arma la sección de paginación
		getSeccionPaginacion(orden.getTotalDeRegistros(), getPageSize(), getNumeroDePagina());
	}
	public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();
		
		String fecha_desde=_record.getValue("fe_ord_desde");
		String fecha_hasta=_record.getValue("fe_ord_hasta");
		java.util.Date fecha = new Date();
		java.text.SimpleDateFormat formato = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
		
		if (flag)
		{
			if ((fecha_desde!=null)&&(fecha_hasta!=null))		
			{	
				Date fecha_1=formato.parse(fecha_desde);
				Date fecha_2=formato.parse(fecha_hasta);
				
				if (fecha_1.compareTo(fecha_2) >0)
				{  
					_record.addError("Fecha hasta","Este campo debe tener una fecha posterior o igual al de Fecha Desde");
						flag = false;
				}
				
				if (fecha_1.compareTo(fecha) >0)
				{  
					_record.addError("Fecha Desde","Este campo debe ser menor a la fecha actual:"+fecha);
						flag = false;
				}
				
				if (fecha_2.compareTo(fecha) >0)
				{  
					_record.addError("Fecha Hasta*","Este campo debe ser menor a la fecha actual:"+fecha);
						flag = false;
				}			
			}
	}
		return flag;
	}
}

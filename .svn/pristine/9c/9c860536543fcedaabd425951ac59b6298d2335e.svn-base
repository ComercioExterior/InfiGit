package models.ordenes.consultas.ordenes_vehiculo;

import java.text.SimpleDateFormat;
import java.util.Date;
import models.msc_utilitys.*;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

public class Browse extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		OrdenDAO confiD = new OrdenDAO(_dso);
		
		//Definicion de variables
		long undinv_id 	= 0;
		String vehiculoTomador 		= "";
		String vehiculoRec			="";
		String vehiculoColocador	="";
		String fe_ord_desde 		= "";
		String fe_ord_hasta 		= "";
		String status 				= "";
		
		//Creacion del filtro de busqueda
		if (_record.getValue("undinv_id")!=null && _record.getValue("undinv_id")!="")
			undinv_id=Long.parseLong(_record.getValue("undinv_id"));
		if (_record.getValue("vehiculo_tomador_id")!=null && _record.getValue("vehiculo_tomador_id")!="")
			vehiculoTomador= _record.getValue("vehiculo_tomador_id");
		if (_record.getValue("vehiculo_recompra_id")!=null && _record.getValue("vehiculo_recompra_id")!="")
			vehiculoRec= _record.getValue("vehiculo_recompra_id");
		if (_record.getValue("vehiculo_colocador_id")!=null && _record.getValue("vehiculo_colocador_id")!="")
			vehiculoColocador= _record.getValue("vehiculo_colocador_id");
		if (_record.getValue("fe_ord_desde")!=null && _record.getValue("fe_ord_desde")!="")
			fe_ord_desde= _record.getValue("fe_ord_desde");
		if (_record.getValue("fe_ord_hasta")!=null && _record.getValue("fe_ord_hasta")!="")
			fe_ord_hasta= _record.getValue("fe_ord_hasta");
		if ( _record.getValue("status")!="")
			status= _record.getValue("status");

		confiD.listarOrdenesVehiculo(undinv_id, vehiculoTomador, vehiculoRec, vehiculoColocador, status, fe_ord_desde, fe_ord_hasta,true,getNumeroDePagina(),getPageSize());
		//registrar los datasets exportados por este modelo
		storeDataSet("table", confiD.getDataSet());
		storeDataSet("fechas", _record);
		storeDataSet("registros", confiD.getTotalRegistros(false));
		storeDataSet("record",_record);
		
		//Arma la sección de paginación
		getSeccionPaginacion(confiD.getTotalDeRegistros(), getPageSize(), getNumeroDePagina());
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
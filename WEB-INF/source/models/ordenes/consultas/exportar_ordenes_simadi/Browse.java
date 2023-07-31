package models.ordenes.consultas.exportar_ordenes_simadi;

import java.text.SimpleDateFormat;
import java.util.Date;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

public class Browse extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 * Modificación 23/03/2015 NM25287 ITS-2531 Reporte Solicitudes SIMADI no muestra filtro de Ofertas y Demandas
	 */
	public void execute() throws Exception {
		OrdenDAO confiD = new OrdenDAO(_dso);
		this.setSessionObject("ejecucion_id", null);
		String fe_ord_desde 	= "";
		String fe_ord_hasta 	= "";
		String tipo_producto_id = "";
		String tipo_negocio = "";
		DataSet _display = new DataSet();		
		_display.append("display",java.sql.Types.VARCHAR);
	
		_display.addNew();

	
		if (_record.getValue("fe_ord_desde")!=null && _record.getValue("fe_ord_desde")!="")
			fe_ord_desde= _record.getValue("fe_ord_desde");
		if (_record.getValue("fe_ord_hasta")!=null && _record.getValue("fe_ord_hasta")!="")
			fe_ord_hasta= _record.getValue("fe_ord_hasta");
		if (_record.getValue("tipo_producto_id")!=null && _record.getValue("tipo_producto_id")!="")
			tipo_producto_id= _record.getValue("tipo_producto_id");
		if (_record.getValue("tipo_negocio")!=null && _record.getValue("tipo_negocio")!="")
			tipo_negocio= _record.getValue("tipo_negocio");
		
		confiD.listarSolicitudesSimadi(fe_ord_desde, fe_ord_hasta,true, true, getNumeroDePagina(),getPageSize(),tipo_producto_id,tipo_negocio);
		//Arma la sección de paginación
		getSeccionPaginacion(confiD.getTotalDeRegistros(), getPageSize(), getNumeroDePagina());
		
		_display.setValue("display", "visible");
		_display.setValue("display", "block");

		
		//registrar los datasets exportados por este modelo
		storeDataSet("table", confiD.getDataSet());
		storeDataSet("fechas", _record);
		storeDataSet("registros", confiD.getTotalRegistros(false));
		storeDataSet("request",getDataSetFromRequest());
		storeDataSet("record",_record);
		storeDataSet("display",_display);
				
		_req.getSession().setAttribute("exportar_ordenes_simadi-browse.framework.page.record",_record);
				
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
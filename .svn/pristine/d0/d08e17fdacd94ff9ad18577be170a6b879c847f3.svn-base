package models.ordenes.consultas.ordenes_blotter;

import java.text.SimpleDateFormat;
import java.util.Date;

import megasoft.DataSet;
import models.msc_utilitys.*;

import com.bdv.infi.dao.BlotterDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

public class Browse extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		OrdenDAO confiD = new OrdenDAO(_dso);
		BlotterDAO blotterDAO = new BlotterDAO(_dso);
		UnidadInversionDAO unidadInversionDAO  = new UnidadInversionDAO(_dso);
		
		//Definicion de variables
		long unidad_inversion 	= 0;
		String blotter 			= "";
		String nombreBlotter="";
		String fe_ord_desde 	= "";
		String fe_ord_hasta 	= "";
		String status 			= "";
		
		DataSet _display=new DataSet();		
		_display.append("display",java.sql.Types.VARCHAR);
		_display.append("display_detalle_blot",java.sql.Types.VARCHAR);
		_display.append("bloter_descrip",java.sql.Types.VARCHAR);
		_display.addNew();

		
		//Creacion del filtro de busqueda
		if (_record.getValue("unidad_inversion")!=null && _record.getValue("unidad_inversion")!="")
			unidad_inversion=Long.parseLong(_record.getValue("unidad_inversion"));
		
		if (_record.getValue("blotter")!=null && _record.getValue("blotter")!=""){
			blotter= _record.getValue("blotter");
			blotterDAO.listar(blotter);
			if(blotterDAO.getDataSet().next()){
				nombreBlotter = blotterDAO.getDataSet().getValue("BLOTER_DESCRIPCION");
			}
		}
		
		if (_record.getValue("fe_ord_desde")!=null && _record.getValue("fe_ord_desde")!="")
			fe_ord_desde= _record.getValue("fe_ord_desde");
		
		if (_record.getValue("fe_ord_hasta")!=null && _record.getValue("fe_ord_hasta")!="")
			fe_ord_hasta= _record.getValue("fe_ord_hasta");
		
		if ( _record.getValue("status")!="")
			status= _record.getValue("status");

		//Para obtener el total de registros sin dataset
		confiD.listarMovimientosPorUbs(unidad_inversion, blotter, status, fe_ord_desde, fe_ord_hasta,false, null);
		
		//Obtiene el dataSetPaginado
		confiD.listarMovimientosPorUbs(unidad_inversion, blotter, status, fe_ord_desde, fe_ord_hasta,true, null, getNumeroDePagina(),getPageSize());
		if(confiD.getDataSet().next()){
			_display.setValue("display_detalle_blot","visible");
		}else _display.setValue("display_detalle_blot","hidden");	
		
		//Buscar datos unidad de inverion seleccionada
		unidadInversionDAO.listadescripcion(unidad_inversion);
		
		//registrar los datasets exportados por este modelo
		storeDataSet("valores", confiD.getDataSet());
		storeDataSet("record", _record);
		storeDataSet("datos_unidad", unidadInversionDAO.getDataSet());
		storeDataSet("registros", confiD.getTotalRegistros(false));
		
		//Arma la sección de paginación
		getSeccionPaginacion(confiD.getTotalDeRegistros(), getPageSize(), getNumeroDePagina());
		
		_display.setValue("bloter_descrip", nombreBlotter);

		if(blotter=="" || blotter==null || nombreBlotter==""){
			_display.setValue("display","none");		
			storeDataSet("display", _display);
		}else{			
			_display.setValue("display","block");			
			storeDataSet("display", _display);
		}
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
	}//fin isValid
	
	/**
   	 * Controla el proceso de paginación y navegación del dataset correspondiente.
   	 * @throws Exception Excepción General.
   	 */
	public void pagingHandler () throws Exception{
	}
}
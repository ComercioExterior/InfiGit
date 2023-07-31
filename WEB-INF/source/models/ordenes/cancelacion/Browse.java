package models.ordenes.cancelacion;

import java.text.SimpleDateFormat;
import java.util.Date;

import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

public class Browse extends MSCModelExtend {

	String idCliente= null; 
	String idOrden = null;
	String fecha_desde = null;
	String fecha_hasta = null;
	String unidad_inversion = null;
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		OrdenDAO confiD = new OrdenDAO(_dso);
	
		if (_record.getValue("orden_id")!=null){
			idOrden = _record.getValue("orden_id");
		}
		if (_record.getValue("client_id")!=null){
			idCliente = _record.getValue("client_id");
		}
		if (_record.getValue("undinv_id")!=null){
			unidad_inversion = _record.getValue("undinv_id");
		}
		if (_record.getValue("fe_ord_desde")!=null){
			fecha_desde = _record.getValue("fe_ord_desde");
		}
		if (_record.getValue("fe_ord_hasta")!=null){
			fecha_hasta = _record.getValue("fe_ord_hasta");
		}
		confiD.listarOrdenesParaCancelar(idOrden, idCliente, unidad_inversion, fecha_desde, fecha_hasta);
		storeDataSet("table", confiD.getDataSet());
		storeDataSet("datos", confiD.getTotalRegistros());
		
		
	}	
	public boolean isValid() throws Exception {

		boolean flag = super.isValid();
		String fecha_desde=_record.getValue("fe_ord_desde");
		String fecha_hasta=_record.getValue("fe_ord_hasta");
		String idCliente= _record.getValue("client_id"); 
		String idOrden = _record.getValue("orden_id");
		java.util.Date fecha = new Date();
		java.text.SimpleDateFormat formato = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
		if ((fecha_desde!=null)&&(fecha_hasta!=null)){	
			Date fecha_1=formato.parse(fecha_desde);
			Date fecha_2=formato.parse(fecha_hasta);
			if (fecha_1.compareTo(fecha_2) >0){  
				_record.addError("Fecha Hasta","Este campo debe tener una fecha posterior o igual al de Fecha Desde");
				flag = false;
			}
			if (fecha_1.compareTo(fecha) >0){  
				_record.addError("Fecha Desde","Este campo debe ser menor a la fecha actual:"+fecha.getDate()+"-"+fecha.getMonth()+"-"+(1900+fecha.getYear()));
				flag = false;
			}
			if (fecha_2.compareTo(fecha) >0){  
				_record.addError("Fecha Hasta*","Este campo debe ser menor a la fecha actual:"+fecha.getDate()+"-"+fecha.getMonth()+"-"+(1900+fecha.getYear()));
				flag = false;
			}
		}
		if ((idOrden==null)&&(idCliente==null)){
			_record.addError("Cliente","Este campo es obligatorio si no especifica n&uacute;mero de orden:");
			flag = false;
		}
		return flag;
	}
}

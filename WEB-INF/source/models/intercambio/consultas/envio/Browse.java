package models.intercambio.consultas.envio;

import java.text.SimpleDateFormat;
import java.util.Date;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

public class Browse extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		ControlArchivoDAO confiD = new ControlArchivoDAO(_dso);
				
		String unidad_inversion = null;
		String fechaDesde = null;
		String fechaHasta = null;
		int indicador = ConstantesGenerales.FALSO;//el indicador asignado para Envio es 0 y para Recepcion es 1
		
		if (_record.getValue("undinv_id")!=null)
			unidad_inversion= _record.getValue("undinv_id");
		if (_record.getValue("fe_desde")!=null)
			fechaDesde= _record.getValue("fe_desde");
		if (_record.getValue("fe_hasta")!=null)
			fechaHasta= _record.getValue("fe_hasta");
		// Realizar consulta
		confiD.listar(fechaDesde, fechaHasta, unidad_inversion, indicador, true,getNumeroDePagina(),getPageSize());
		// registrar los datasets exportados por este modelo
		storeDataSet("table", confiD.getDataSet());
		storeDataSet("datos", confiD.getTotalRegistros(false));
		
		getSeccionPaginacion(confiD.getTotalDeRegistros(), getPageSize(), getNumeroDePagina());
		
//		Montar en session la unidad de inversion y el vehiculo, para usar en detalle
		
	}
	
	public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();
		String var1 = _record.getValue("fe_desde");
		String var2 = _record.getValue("fe_hasta");
		
		Date fecha = new Date();
		
		java.text.SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
		Date fecha1 = formato.parse(var1);
		Date fecha2 = formato.parse(var2);
			
		//Valida que la fecha creacion  hasta no sea menor a la fecha desde y no sea mayor al dia de hoy
		if ((var1!=null)&&(var2!=null)){						
			if (fecha1.compareTo(fecha2)>0){  
				_record.addError("Fecha Hasta","Este campo no debe tener una fecha anterior a la del campo Fecha Desde");
				flag = false;
			}
			if (fecha2.compareTo(fecha)>0){  
				_record.addError("Fecha","No debe colocar una fecha mayor a la de Hoy");
				flag = false;
			}
			if (fecha1.compareTo(fecha)>0){  
				_record.addError("Fecha","No debe colocar una fecha mayor a la de Hoy");
				flag = false;
			}
			
		}
		return flag;
	}	
}

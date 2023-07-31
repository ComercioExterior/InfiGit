package models.intercambio.consultas.recepcion;

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
		int indicador = ConstantesGenerales.VERDADERO;

		if (_record.getValue("undinv_id")!=null)
			unidad_inversion= _record.getValue("undinv_id");
		if (_record.getValue("fe_desde")!=null)
			fechaDesde= _record.getValue("fe_desde");
		if (_record.getValue("fe_hasta")!=null)
			fechaHasta= _record.getValue("fe_hasta");
		// Realizar consulta
		confiD.listar(fechaDesde, fechaHasta, unidad_inversion, indicador,true,getNumeroDePagina(),getPageSize());
		// registrar los datasets exportados por este modelo
		storeDataSet("table", confiD.getDataSet());
		storeDataSet("datos", confiD.getTotalRegistros(false));
		getSeccionPaginacion(confiD.getTotalDeRegistros(), getPageSize(), getNumeroDePagina());
	}
	
	public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();
				
		Date fecha = new Date();
		java.text.SimpleDateFormat formato = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
		Date fechaDesde = formato.parse(_record.getValue("fe_desde"));
		Date fechahasta = formato.parse(_record.getValue("fe_hasta"));
		
		if (flag)
		{
			//Valida que la fecha creacion  hasta no sea menor a la fecha desde y no sea mayor al dia de hoy
			if ((fechaDesde!=null)&&(fechahasta!=null)){						
				if (fechaDesde.compareTo(fechahasta)>0){  
					_record.addError("Fecha Hasta","Este campo no debe tener una fecha anterior a la del campo Fecha Desde");
					flag = false;
				}
				if ((fechaDesde.compareTo(fecha)>0)){  
					_record.addError("Fecha Desde","No debe colocar una fecha mayor a la de Hoy");
					flag = false;
				}
				if (fechahasta.compareTo(fecha)>0){  
					_record.addError("Fecha Hasta","No debe colocar una fecha mayor a la de Hoy");
					flag = false;
				}
				
			}
		}
		return flag;
	}	
}

package models.custodia.transacciones.pago_cupones.consulta;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.util.Utilitario;

import megasoft.DataSet;
import megasoft.Logger;
import models.msc_utilitys.MSCModelExtend;

/**
 * Muestra los ultimo 20 procesos que sean de :
 * <li>Cupones</li>
 * <li>Comisiones</li>
 * <li>Amortización</li>
 * @author eel
 */
public class PagoCuponesProcesos extends MSCModelExtend{

	public void execute() throws Exception {
		
		try {
			
			ProcesosDAO cupones			=new ProcesosDAO(_dso);
			
			//Buscamos los procesos ejecutados en una rango de fecha para Pago de Cupones
			cupones.listarProcesoRangoFecha(_record.getValue("fe_ord_desde"),_record.getValue("fe_ord_hasta"));
			
			//Clonamos el DataSet
			DataSet _procesos = cupones.getDataSet();
			
			//Recorremos el mismo para anexarle un nuevo campo
			if(_procesos.count()>0)
			{
				//_procesos.removeRow(1);
				_procesos.first();
				
				while(_procesos.next())
				{

					if(_procesos.getValue("fecha_fin")==null || _procesos.getValue("fecha_fin").equalsIgnoreCase("null")){
						
						_procesos.setValue("fecha_fin", "");
					}
					if(_procesos.getValue("desc_error")==null || _procesos.getValue("desc_error").equalsIgnoreCase("null")){
						
						_procesos.setValue("desc_error", "");
					}
					if(_procesos.getValue("color").equalsIgnoreCase("BLUE")){
						
						_procesos.setValue("estatus_proceso", "EJECUTANDO");
		
					}else if(_procesos.getValue("color").equalsIgnoreCase("GREEN"))
					{
						_procesos.setValue("estatus_proceso", "FINALIZADO");
						
					}else if(_procesos.getValue("color").equalsIgnoreCase("RED"))
					{
						_procesos.setValue("estatus_proceso", "FINALIZADO CON ERRORES");
						
					}
				}//fin while

			}//fin if
			else{
				//_procesos.first();
				//_procesos.removeRow(1);
			}
			storeDataSet("procesos",_procesos);
			storeDataSet("total",cupones.getTotalRegistros());
		} catch (Exception e) {
			Logger.error(this,e.getMessage()+" "+Utilitario.stackTraceException(e));
			throw e;
		}
		
	}
	public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();
		
		String fecha_desde=_record.getValue("fe_ord_desde");
		String fecha_hasta=_record.getValue("fe_ord_hasta");
		java.util.Date fecha = new Date();
		java.text.SimpleDateFormat formato = new SimpleDateFormat(com.bdv.infi.logic.interfaces.ConstantesGenerales.FORMATO_FECHA2);
		
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
					_record.addError("Fecha Desde","Este campo debe ser menor a la fecha actual");
						flag = false;
				}
				
				if (fecha_2.compareTo(fecha) >0)
				{  
					_record.addError("Fecha Hasta*","Este campo debe ser menor a la fecha actual");
						flag = false;
				}			
			}
	}
		return flag;
	}
}

package models.configuracion.generales.blotter;

import com.bdv.infi.dao.BlotterDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import megasoft.DataSet;
import models.msc_utilitys.*;

public class ConfirmUpdate extends MSCModelExtend {
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
				
		//	crear dataset
		DataSet _filter = getDataSetFromRequest();
		//registrar los datasets exportados por este modelo
		_filter.setValue("bloter_horario_desde",_record.getValue("bloter_horario_desde")+" "+_record.getValue("hh_desde")
				+":"+_record.getValue("mm_desde")+":00 "+_record.getValue("ap_desde"));
		_filter.setValue("bloter_horario_hasta",_record.getValue("bloter_horario_hasta")+" "+_record.getValue("hh_hasta")
				+":"+_record.getValue("mm_hasta")+":00 "+_record.getValue("ap_hasta"));
		storeDataSet("filter", _filter);
	}
	public boolean isValid()throws Exception{
		boolean valido=super.isValid();
		
		if(valido)
		{
			String var1           = _req.getParameter("bloter_horario_desde");
			String var2           = _req.getParameter("bloter_horario_hasta");
			String cuentaRed      = _req.getParameter("bloter_in_red")==null?"":_req.getParameter("bloter_in_red");
			BlotterDAO bloterDAO  = new BlotterDAO(_dso);
			boolean 	existe	  = false;
			if(cuentaRed!=null && cuentaRed.equals("1")){
				
				existe   = bloterDAO.listarBloterRed(_req.getParameter("bloter_id"));
			}
			
			MSCModelExtend me = new MSCModelExtend();		
			//Valida que la fecha creacion  hasta no sea menor a la fecha desde y no sea mayor al dia de hoy
			if ((var1!=null)&&(var2!=null)){						
				if(me.StringToDate(var1, ConstantesGenerales.FORMATO_FECHA).after(me.StringToDate(var2, ConstantesGenerales.FORMATO_FECHA))){
				//if (var1.compareTo(var2)>0){  
					_record.addError("Fecha Desde","La Fecha Desde no puede ser mayor a la Fecha Hasta");
					valido = false;
				}
							
			}
			if (existe)
			{  
				_record.addError("Cuenta Red","Existe un bloter con una red asociada");
				valido = false;
			}//fin if
			
			if(_record.getValue("hh_hasta").equals("12")&&_record.getValue("ap_hasta").equals("AM")){
				_record.addError("Hora Hasta","No debe ser mayor a las 11:59 PM");
				valido = false;
			}
			
			if(_req.getParameter("canal_id").equalsIgnoreCase(ConstantesGenerales.ID_CANAL_CLAVENET_PERSONAL)){
				if(bloterDAO.cantidadBlottersPorCanal(ConstantesGenerales.ID_CANAL_CLAVENET_PERSONAL,_req.getParameter("bloter_id"))>0){				
					_record.addError("Canal","Ya existe un blotter configurado para este canal");
					valido = false;
				}			
			}
		}
		
		
		return valido;
	}
}

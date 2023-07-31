package models.configuracion.transacciones_fijas;

import megasoft.DataSet;
import models.msc_utilitys.*;
public class Confirm extends MSCModelExtend {

	private String vehiculo="0";
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		
		
		//crear dataset
		DataSet _filter = getDataSetFromRequest();
		//registrar los datasets exportados por este modelo
		storeDataSet("filter", _filter);
		storeDataSet("record", _record);
	}
	
	/**
	 * Validaciones Basicas del action
	 * @return true si la clase y sus par&aacute;metros son v&aacute;lidos, false en caso contrario 
	 * @throws Exception
	 */	
	public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();
		
		if (flag)
		{	
			
			vehiculo= getSessionObject("vehiculo").toString();
		
			if(vehiculo.equals("0")){//transaccion fija	
				if(_record.getValue("codigo_operacion")==null){
					_record.addError("C&oacute;digo Operaci&oacute;n", "Este campo es obligatorio.");
					flag = false;
				}
			}else{//Transaccion Fija de Vehiculo
				
				//Validar Codigos Cliente
				if(_record.getValue("cod_operacion_cte_deb")==null){
					_record.addError("C&oacute;digo Oper. D&eacute;bito (Cliente)", "Este campo es obligatorio.");
					flag = false;
				}
				if(_record.getValue("cod_operacion_cte_cre")==null){
					_record.addError("C&oacute;digo Oper. Cr&eacute;dito (Cliente)", "Este campo es obligatorio.");
					flag = false;
				}

				if(_record.getValue("cod_operacion_cte_blo")==null){
					_record.addError("C&oacute;digo Oper. Bloqueo (Cliente)", "Este campo es obligatorio.");
					flag = false;
				}

				//validar Codigos Vehiculo
				if(_record.getValue("cod_operacion_veh_deb")==null){
					_record.addError("C&oacute;digo Oper. D&eacute;bito (Veh&iacute;culo)", "Este campo es obligatorio.");
					flag = false;
				}

				if(_record.getValue("cod_operacion_veh_cre")==null){
					_record.addError("C&oacute;digo Oper. Cr&eacute;dito (Veh&iacute;culo)", "Este campo es obligatorio.");
					flag = false;
				}

			}
				
		}
		
		return flag;	
	}
}
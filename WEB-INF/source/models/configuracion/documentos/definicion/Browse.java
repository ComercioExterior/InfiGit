package models.configuracion.documentos.definicion;

import java.util.Date;
import java.text.*;

import megasoft.DataSet;
import models.msc_utilitys.*;
import com.bdv.infi.dao.DocumentoDefinicionDAO;

public class Browse extends MSCModelExtend {

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		DocumentoDefinicionDAO confiD = new DocumentoDefinicionDAO(_dso);
	
		//Recuperamos de session el numero de la accion y la mandamos a la vista
		String accion= getSessionObject("accion").toString();
		DataSet _accion=new DataSet();
		_accion.append("accion",java.sql.Types.VARCHAR);
		_accion.addNew();
		_accion.setValue("accion",accion);
		storeDataSet("accion", _accion);
		//fin de recuperacion y de envio a la vista
		
		String unidad = null;
		String status_documento = null;
		String cre_usuario_userid = null;
		String cre_desde = null;
		String cre_hasta = null;
		String apro_usuario_userid = null;
		String apro_desde = null;
		String apro_hasta = null;
		String transa_id = null;
		
		if (_record.getValue("undinv_id")!=null)
			unidad= _record.getValue("undinv_id");
		if (_record.getValue("status_documento")!=null)
			status_documento= _record.getValue("status_documento");
		if (_record.getValue("msc_user_id")!=null)
			cre_usuario_userid= _record.getValue("msc_user_id");
		if (_record.getValue("cre_desde")!=null)
			cre_desde= _record.getValue("cre_desde");
		if (_record.getValue("cre_hasta")!=null)
			cre_hasta= _record.getValue("cre_hasta");
		if (_record.getValue("mscuser_id")!=null)
			apro_usuario_userid= _record.getValue("mscuser_id");
		if (_record.getValue("apro_desde")!=null)
			apro_desde= _record.getValue("apro_desde");
		if (_record.getValue("apro_hasta")!=null)
			apro_hasta= _record.getValue("apro_hasta");
		
		if (_record.getValue("transa_id")!=null)
			transa_id= _record.getValue("transa_id");

		
		//Realizar consulta
		confiD.listar(cre_usuario_userid, cre_desde,cre_hasta,apro_usuario_userid,apro_desde,apro_hasta,status_documento,unidad, transa_id);
		//registrar los datasets exportados por este modelo
		storeDataSet("table", confiD.getDataSet());
		storeDataSet("datos", confiD.getTotalRegistros());
	}
	
	public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();
		String var1 = _record.getValue("cre_desde");
		String var2 = _record.getValue("cre_hasta");
		String var3 = _record.getValue("apro_desde");
		String var4 = _record.getValue("apro_hasta");
			
		Date fecha = new Date();
		java.text.SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		String var9 = formato.format(fecha);
				
		// Lanza un error si los 2 campos de fecha creacion no vienen llenos
		if (((var1==null)&& (var2!=null))||((var1!=null)&& (var2==null))){
			_record.addError("Fecha Desde / Fecha Hasta","Ambos campos deben venir llenos si la b&uacute;squeda es por Fecha de Creacion");
			flag = false;
		}
		
		// Lanza un error si los 2 campos de fecha aprobacion no vienen llenos
		if (((var3==null)&& (var4!=null))||((var3!=null)&& (var4==null))){
			_record.addError("Fecha Desde / Fecha Hasta","Ambos campos deben venir llenos si la b&uacute;squeda es por Fecha de Aprobacion");
			flag = false;
		}
		
		//Valida que la fecha creacion  hasta no sea menor a la fecha desde y no sea mayor al dia de hoy
		if ((var1!=null)&&(var2!=null)){						
			if (var1.compareTo(var2)>0){  
				_record.addError("Fecha Hasta","Este campo no debe tener una fecha anterior a la del campo Fecha Desde");
				flag = false;
			}
			if ((var1.compareTo(var9)>0)||(var2.compareTo(var9)>0)){  
				_record.addError("Fecha","No debe colocar una fecha mayor a la de Hoy");
				flag = false;
			}
			
		}
		
		//	Valida que la fecha aprobacion hasta no sea menor a la fecha desde y no sea mayor al dia de hoy
		if ((var3!=null)&&(var4!=null)){
			if (var3.compareTo(var4)>0){  
				_record.addError("Fecha Hasta","Este campo no debe tener una fecha anterior a la del campo Fecha Desde");
				flag = false;
			}
			if ((var3.compareTo(var9)>0)||(var4.compareTo(var9)>0)){  
				_record.addError("Fecha","No debe colocar una fecha mayor a la de Hoy");
				flag = false;
			}
			
		}		
		return flag;
	}	
}
package models.transformacion_registros;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import com.bdv.infi.util.Utilitario;

import megasoft.*;import models.msc_utilitys.*;

public class Procesar extends MSCModelExtend
{

	/** DataSet del modelo */    
	private DataSet _archivos = null;
	private DataSet _registro = null;
	private DataSet _registrans = null;
	private DataSet _fecha = null;
	private Vector lista_valores = new Vector();
	private String fecha_comienzo = "";
	private String fecha_fin ="";
	private String conteoTotal = "";
	private double porcentaje = 0;
	private double porcentaje_error = 0;
	
	/** 
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{
		String sql="", sql2="", sqlinsert = "", sqlFechaFin = "";
		String V_VALOR = "", V_RESULTADO="", V_MENSAJE="";
		String entidad_archivo = "", cod_archivo= "",cod_mapa="", cod_proceso = "", usuario="" ;
		int registros_error = 0, registros_insertados = 0, registros_leidos = 0;
		 
		cod_archivo =_req.getParameter("cod_archivo");
		cod_mapa = _req.getParameter("cod_mapa");
		cod_proceso = _req.getParameter("cod_proceso");
		
		registros_error = 0;
		registros_insertados = 0;
		registros_leidos = 0;
		
		 sql = "SELECT Z12_CO_CODIGO_REGISTRO, Z11_COD_PROCESO,Z12_NU_NUMERO_REGISTRO,Z12_ESTATUS ";
		 sql = sql + "FROM INFI_TB_Z12_REGISTROS ";
		 sql = sql + " WHERE Z12_ESTATUS = 1 ";
		 sql = sql + " AND Z11_COD_PROCESO = "+cod_proceso;
		 sql = sql + " ORDER BY Z12_CO_CODIGO_REGISTRO ASC, Z11_COD_PROCESO ASC";
		 _registrans = db.get(_dso, sql);
		
		while (_registrans.next()){
				for (int i=1; i<=25; i++){
			        V_VALOR 	   = "Z12_CAMPO"+i+"_VALOR";
					V_RESULTADO    = "Z12_CAMPO"+i+"_RESULTADO";
				    V_MENSAJE      = "Z12_CAMPO"+i+"_MENSAJE";
					
				    
				    sql2 = "SELECT "+ V_VALOR + ","+V_RESULTADO+","+V_MENSAJE;
				    sql2 = sql2 + " FROM INFI_TB_Z12_REGISTROS ";
				    sql2 = sql2 + " WHERE Z12_CO_CODIGO_REGISTRO = " + _registrans.getValue("Z12_CO_CODIGO_REGISTRO");
				    sql2 = sql2 + " AND Z11_COD_PROCESO = " + _registrans.getValue("Z11_COD_PROCESO");
				    sql2 = sql2 + " AND Z12_NU_NUMERO_REGISTRO = " + _registrans.getValue("Z12_NU_NUMERO_REGISTRO") ;
				    sql2 = sql2 + " AND Z12_ESTATUS = " + _registrans.getValue("Z12_ESTATUS");
				    //sql2 = sql2 + " AND " + V_VALOR + " IS NOT NULL";
				    //sql2 = sql2 + " AND " + V_RESULTADO + " IS NOT NULL";
				    //sql2 = sql2 + " AND " + V_MENSAJE + " IS NOT NULL";

				    _registro = db.get(_dso, sql2);
				    
				    while(_registro.next()){
				    	if (_registro.getValue(""+V_VALOR+"") != null || _registro.getValue(""+V_MENSAJE+"") != null){
				    		registros_leidos = registros_leidos + 1;	
				    	try{	
				    		sqlinsert = "INSERT INTO INFI_TB_Z15_REGISTROS_VAL (Z15_CO_CODIGO_REGISTRO, Z11_COD_PROCESO, Z15_NU_NUMERO_REGISTRO, Z15_CAMPO_VALOR, Z15_CAMPO_MENSAJE, Z15_CAMPO_RESULTADO, Z15_CAMPO_NUMERO)";
				    		sqlinsert = sqlinsert + " VALUES("+_registrans.getValue("Z12_CO_CODIGO_REGISTRO")+", "+_registrans.getValue("Z11_COD_PROCESO")+", "+_registrans.getValue("Z12_NU_NUMERO_REGISTRO")+", '"+_registro.getValue(""+V_VALOR+"")+"', '"+_registro.getValue(""+V_MENSAJE+"")+"', "+_registro.getValue(""+V_RESULTADO+"")+", "+i+")";
				    		db.exec(_dso,sqlinsert);
				    		registros_insertados = registros_insertados + 1;
				    	}
				    	catch(Exception e){
				    		Logger.error(this,e.getMessage()+" "+Utilitario.stackTraceException(e));
							registros_error = registros_error + 1;
				    	  }
				    	}
				    }
				    _record.setValue("registros_ok", String.valueOf(registros_insertados));
					_record.setValue("registros_error", String.valueOf(registros_error));
					porcentaje = this.getPorcentaje(registros_insertados, registros_leidos);
					porcentaje_error = this.getPorcentaje(registros_error, registros_leidos);
					_record.setValue("porcentaje", String.valueOf(porcentaje));
					_record.setValue("porcentaje_error", String.valueOf(porcentaje_error));
					_record.setValue("registros_leidos", String.valueOf(registros_leidos));
					sqlFechaFin = getResource("select_fechafin.sql");
				    _fecha = db.get(_dso, sqlFechaFin);
				    
				    
				    if(_fecha.next()){
				    
				    	fecha_fin = _fecha.getValue("fecha_fin");

				    	
				    	_record.setValue("tiempo_duracion", this.getDiferenciaHoras(fecha_comienzo, fecha_fin, "dd/MM/yyyy - HH:mm:ss"));
				    	_record.setValue("fecha_fin", fecha_fin);
				    }
				    
				}
		}
		
	
		storeDataSet( "archivos", _archivos ); 
		storeDataSet( "record", _record ); 
		storeDataSet("registro",_registro);

	} 
public String getDiferenciaHoras(String t1, String t2, String formato) throws ParseException{
		
		SimpleDateFormat sdf = new SimpleDateFormat(formato);
		long horas, minutos, segundos;
		long restohora, restominuto;
		String hrs, min, seg;

		java.util.Date hora1 = sdf.parse(t1); 
		java.util.Date hora2 = sdf.parse(t2); 
		
		//obtener diferencia en milisegundos
		long lantes = hora1.getTime(); 
		long lahora = hora2.getTime(); 
		long dif_miliseg = lahora - lantes; 	

		//transformar milisegundos para obtener la cantidad en horas, minutos y segundos
		horas = dif_miliseg/3600000;
		restohora = dif_miliseg%3600000;
		
		minutos = restohora/60000;
		restominuto = restohora%60000;
		
		segundos = restominuto/1000;
		
		//colocar en formato hh:mm:ss
		if(horas<10) hrs = "0" + String.valueOf(horas);
		else hrs = String.valueOf(horas);
		
		if(minutos<10) min = "0" + String.valueOf(minutos);
		else min = String.valueOf(minutos);

		if(segundos<10) seg = "0" + String.valueOf(segundos);
		else seg = String.valueOf(segundos);
		
		String duracion = hrs +":"+ min +":"+ seg;		
	
		return String.valueOf(duracion);
	}
	
	public String getFechaHoyFormateada(String formato) throws ParseException{
		
		SimpleDateFormat sdf = new SimpleDateFormat(formato);		
		//////////////Fecha actual///////////////////////
	    Calendar fechaHoy = Calendar.getInstance();						
		Date aux = fechaHoy.getTime();
		String fhoy = sdf.format(aux); ////darle formato			
	    ////////////////////////////////////////////////////
		
		return fhoy;
	}
	/**
	 * M&eacute;todo para obtener la representaci&oacute;n en porcentaje de una cantidad respecto a un total
	 * @param cantidad
	 * @param total
	 * @return double con el porcentaje representativo
	 */
	public double getPorcentaje(double cantidad, double total){
		double porc=0;
		
		if(total!=0){
			porc = (cantidad * 100)/total;		
			//redondear porcentaje con 2 decimales
			porc = Math.rint(porc *100)/100;
		}
		
		return porc;
	}
	public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();
		String sql = "";
			
		if (flag)
		{
			
			if (_req.getParameter("band") != null) {
				
				if (_req.getParameter("cod_archivo")== null || _req.getParameter("cod_archivo").equals("")){
					_record.addError("MSG-CD100-0001", " Seleccione los parametros requeridos (archivo)");
					 flag = false;
				}else{
					if (_req.getParameter("cod_mapa")== null || _req.getParameter("cod_mapa").equals("")){
					      _record.addError("MSG-CD100-0001", " Seleccione los parametros requeridos (mapa)");
					      flag = false;
				        }
					
				    else{
				       if (_req.getParameter("cod_proceso")== null || _req.getParameter("cod_proceso").equals("")){
						      _record.addError("MSG-CD100-0001", " Seleccione los parametros requeridos (proceso)");
						      flag = false;
					 }
				      else{
		    				 sql = "";
		    				 sql = getResource("conteo_proceso.sql");
							 sql = Util.replace(sql, "@cod_proceso@", _req.getParameter("cod_proceso"));
							 _archivos = db.get(_dso, sql);
								
								while (_archivos.next()) {
									conteoTotal =_archivos.getValue("CONTEO");
									fecha_comienzo = _archivos.getValue("FECHA");
									if (_archivos.getValue("CONTEO").equals("0")){
										_record.addError("MSG-CD100-002", " No existen datos a procesar");
										flag = false;
									}
									else{
										flag = true;
									}
								} 
						   //}
		    		   //}
			      } 
			   }
			  }
			}	
			
			 
			
		 
		}
		return flag;	
	}
}
package models.conversion_data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import megasoft.*;
import models.msc_utilitys.*;

public class Procesar extends MSCModelExtend
{

	/** DataSet del modelo */    
	private DataSet _archivos = null;
	private DataSet _fecha = null;
	private DataSet _insercion = null; 
	private DataSet _columna = null;
	private DataSet _registro = null;
	private DataSet _constante = null;
	private DataSet _secuencia = null;
	private DataSet _datosProceso = null;
	private Vector lista_valores = new Vector();
	private String fecha_comienzo = "";
	private String fecha_fin ="";
	private String conteoTotal = "";
	private double porcentaje = 0;
	private double porcentaje_error = 0;
	private int numeroCampos = 0;
	
	/**
	 * Esta clase esta asociada con la accion INFICD100-PROCESAR.
	 * Permite validar todos los campos requeridos de la consulta INFICD100-FILTRO.
	 * Consulta fecha inicio y fecha fin para ejecutar el proceso.
	 * Esta clase esta manejada de forma dinamica utilizando una tabla propia de Oracle,
	 * para identenficar los campos que se van a consultar y almacenar en la tabla destino.
	 * Inserta valores en la tabla destino, si no existe algun objeto de base de datos activa la accion INFICD100-PROCESAR-ERROR
	 * INFICD100-PROCESAR-ERROR: Muestra el error: El objeto no existe.
	 * 
	 */
	public void execute() throws Exception
	{
		String sqlBusqueda = "", sqlCampos = "", sqlDetalle = "", sqlinsert ="", sqlFechaFin="", sqlinsert_reg="", sqlinsert_err="", sqlupdate="";
		String sqlConstante = "", sqlSecuencia = "", sqlTablaSecuencia="";
		String entidad_archivo = "", cod_archivo= "",cod_mapa="", cod_proceso = "", usuario="" ;
		String campo = "",  campo_out = "",  posicion = "", concampos = "", concampos_valores = "", concampos2 = "";
		String constante = "", secuencia = "", bandera = "";
		String columnas_insert= "", valores_insert = "";
		String mensaje="NINGUNO", mensaje_insertar="NINGUNO";
		int contador = 0, conteo_campo = 0, registros_error = 0, registros_insertados = 0;
		
		sqlBusqueda = getResource("select_insercion.sql");
		sqlBusqueda = Util.replace(sqlBusqueda, "@cod_archivo@", _req.getParameter("cod_archivo"));
		sqlBusqueda = Util.replace(sqlBusqueda, "@cod_mapa@", _req.getParameter("cod_mapa"));

		_insercion = db.get(_dso, sqlBusqueda);
		
		cod_archivo =_req.getParameter("cod_archivo");
		cod_mapa = _req.getParameter("cod_mapa");
		cod_proceso = _req.getParameter("cod_proceso");
		entidad_archivo = _req.getParameter("entidad_archivo");
		usuario = _req.getParameter("usuario");
		
		/* Manejo de Fechas */
		//fecha_fin = this.getFechaHoyFormateada("dd/MM/yyyy - HH:mm:ss");
		
		String sql = "select * from infi_tb_z11_procesos where z11_cod_proceso = "+ _req.getParameter("cod_proceso");
		_datosProceso = db.get(_dso, sql);		
		if(_datosProceso.next()){
			numeroCampos = Integer.parseInt(_datosProceso.getValue("z11_nu_numero_campos"));
		}
		
		while (_insercion.next()) { 
			/*posicion = _insercion.getValue("POSICION");
			sqlCampos = getResource("select_columnas.sql");
			sqlCampos = Util.replace(sqlCampos, "@posicion@", "'Z12_CAMPO"+posicion+"_VALOR'");
			_columna = db.get(_dso, sqlCampos);
			
			
			while (_columna.next()){
				
				campo_out = _columna.getValue("COLUMN_NAME"); 
				contador = contador + 1; 
				if (contador == 1){			
					concampos2 = "D." + campo_out;
				}
				else{
					concampos2 = concampos2 + ", " + "D." +campo_out ;
				}
			}*/
			
				contador = contador + 1; 
				campo = "";
			
				campo = _insercion.getValue("CAMPO_OUT");
				if (contador == 1){
				concampos = campo;
				}
				else{
					concampos = concampos + ", " + campo;
				}
		}

		sqlDetalle = getResource("select_detalle.sql");	
		sqlDetalle = Util.replace(sqlDetalle, "@cod_archivo@", _req.getParameter("cod_archivo"));
		sqlDetalle = Util.replace(sqlDetalle, "@cod_proceso@", _req.getParameter("cod_proceso"));
		_registro = db.get(_dso, sqlDetalle);
		
		//Contador para registros
		_insercion.first();
		//_columna.first();
		
		
		
		/*posicion = ""; sqlCampos = ""; campo_out = "";
		while (_insercion.next()){
			posicion = _insercion.getValue("POSICION");
			sqlCampos = getResource("select_columnas.sql");
			sqlCampos = Util.replace(sqlCampos, "@posicion@", "'Z12_CAMPO"+posicion+"_VALOR'");

			_columna = db.get(_dso, sqlCampos);
			
			while (_columna.next()){
				campo_out = _columna.getValue("COLUMN_NAME");
				lista_valores.add(campo_out);
			}
		}*/
				
				registros_error = 0;
				registros_insertados = 0;
				while (_registro.next()){
					valores_insert = "";
					conteo_campo = 0;
					for (int i=1; i<= numeroCampos; i++){
						constante = "";
						secuencia = "";
						
						columnas_insert = " "+entidad_archivo+" ("+ concampos;
						conteo_campo = conteo_campo + 1;
						
						sqlConstante = getResource("select_constantes.sql");
						sqlConstante = Util.replace(sqlConstante,"@cod_archivo@",_req.getParameter("cod_archivo"));
						sqlConstante = Util.replace(sqlConstante,"@secuencia@",String.valueOf(i));
						_constante = db.get(_dso, sqlConstante);
						
						sqlSecuencia = getResource("select_secuencia.sql");
						sqlSecuencia = Util.replace(sqlSecuencia,"@cod_archivo@",_req.getParameter("cod_archivo"));
						sqlSecuencia = Util.replace(sqlSecuencia,"@secuencia@",String.valueOf(i));
						_secuencia = db.get(_dso, sqlSecuencia);
						
						if ((_registro.getValue("Z12_CAMPO"+i+"_VALOR")==null) || (_registro.getValue("Z12_CAMPO"+i+"_VALOR").equals("null")))
						{
							
							/*
							 * Validar que los campos vacios no sean campos obligatorios o sean secuencias 
							 * Alimentar las variables que se insertaran en campos obligatorios o valores por default
							 */
							//select del mapeo para recuperar valor con la posicion de la lista de valores condicion = _req.getParameter("cod_archivo")
							
							
							while(_constante.next()){
								constante=_constante.getValue("CONSTANTE");
							}
						
							
							if (!constante.equals("")){
								
								if (conteo_campo == 1){
									valores_insert = constante;
								}
								else{
									valores_insert = valores_insert + " ," + constante;
								}
							}
							else{
								while(_secuencia.next()){
										secuencia=dbGetSequence(_dso, _secuencia.getValue("SECUENCIA"));
								} 
								if (!secuencia.equals("")){
									if (conteo_campo == 1){
										valores_insert = secuencia;
									}
									else{
										valores_insert = valores_insert + " ," + secuencia;
									}
								}
								else{
									if (conteo_campo == 1){
										valores_insert = "''";
									}
									else{
										valores_insert = valores_insert + " ," + "''";
									}
								}
							}
						}else{
							
							while(_constante.next()){
								constante=_constante.getValue("CONSTANTE");
							}
							
							
							if (!constante.equals("")){
								if (conteo_campo == 1){
									valores_insert = constante;
								}
								else{
									valores_insert = valores_insert + " ," + constante;
								}
							}
							else{
								while(_secuencia.next()){
									secuencia=dbGetSequence(_dso, _secuencia.getValue("SECUENCIA"));
								}
								if (!secuencia.equals("")){
									if (conteo_campo == 1){
										valores_insert = secuencia;
									} 
									else{
										valores_insert = valores_insert + " ," + secuencia;
									}
								}
								else{
									if (conteo_campo == 1){
										valores_insert = "'"+_registro.getValue("Z12_CAMPO"+i+"_VALOR")+"'";
									}
									else{
										valores_insert = valores_insert + " ," + "'" + _registro.getValue("Z12_CAMPO"+i+"_VALOR") + "'";
									}
								}		
						   }
						}
					}
					
				    
					try{
						sqlinsert = getResource("insert_columns.sql");
						sqlinsert = Util.replace(sqlinsert, "@columnas_insert@", columnas_insert);
						sqlinsert = Util.replace(sqlinsert, "@valores_insert@", valores_insert);
						db.exec(_dso,sqlinsert);
					    
					    registros_insertados = registros_insertados + 1; 
					}
					/*catch(SQLException e){
						e.printStackTrace();
						
						for (int i=0; i<e.getStackTrace().length; i++){ 

							}
							registros_error = registros_error + 1;
							mensaje =  "Error: No existe el objeto de base de datos";
							_record.setValue("mensaje", mensaje);
					}*/
					catch(Exception e){
						e.printStackTrace();
						/*for (int i=0; i<e.getStackTrace().length; i++){ 
							mensaje_insertar = e.getStackTrace()[i].toString();
						}*/
						mensaje_insertar = e.getMessage().substring(0, 100);
						registros_error = registros_error + 1;
						mensaje =  "Error: Valide los objetos de la base de datos";
						_record.setValue("mensaje", mensaje);
						
						//_record.setValue("registros_error", String.valueOf(registros_error));
						//this.setNextAction("export_data-Error?band=1&cod_archivo="+cod_archivo+"&cod_mapa="+cod_mapa+"&cod_proceso="+cod_proceso+"&entidad_archivo="+entidad_archivo, "redirect");
						//this.setNextAction("export_data-procesar?band=1&error=1", "redirect");
						
						sqlinsert_err=getResource("insert_registro_error.sql");
						sqlinsert_err = Util.replace(sqlinsert_err,"@cod_entidad@",entidad_archivo);
						sqlinsert_err = Util.replace(sqlinsert_err,"@cod_proceso@",cod_proceso);
						sqlinsert_err = Util.replace(sqlinsert_err,"@registros_error@",String.valueOf(registros_error));
						sqlinsert_err = Util.replace(sqlinsert_err,"@mensaje@",mensaje_insertar);
						db.exec(_dso,sqlinsert_err);
					}
					
					
					_record.setValue("registros_ok", String.valueOf(registros_insertados));
					_record.setValue("registros_error", String.valueOf(registros_error));
					porcentaje = this.getPorcentaje(registros_insertados, Integer.parseInt(conteoTotal));
					porcentaje_error = this.getPorcentaje(registros_error, Integer.parseInt(conteoTotal));
					_record.setValue("porcentaje", String.valueOf(porcentaje));
					_record.setValue("porcentaje_error", String.valueOf(porcentaje_error));
					
					sqlFechaFin = getResource("select_fechafin.sql");
				    _fecha = db.get(_dso, sqlFechaFin);
				    
				    
				    if(_fecha.next()){
				    
				    	fecha_fin = _fecha.getValue("fecha_fin");
				    	
				    	_record.setValue("tiempo_duracion", this.getDiferenciaHoras(fecha_comienzo, fecha_fin, "dd/MM/yyyy - HH:mm:ss"));
				    	_record.setValue("fecha_fin", fecha_fin);
				    }
				}
				
				sqlinsert_reg=getResource("insert_registro_estatus.sql");
			    sqlinsert_reg = Util.replace(sqlinsert_reg,"@cod_entidad@",entidad_archivo);
			    sqlinsert_reg = Util.replace(sqlinsert_reg,"@cod_proceso@",cod_proceso);
			    sqlinsert_reg = Util.replace(sqlinsert_reg,"@registros_insertados@",String.valueOf(registros_insertados));
			    sqlinsert_reg = Util.replace(sqlinsert_reg,"@conteoTotal@",conteoTotal);
			    if (usuario.equals("") || usuario == null){
			    	usuario = "USUARIO NO DEFINIDO";
			    	sqlinsert_reg = Util.replace(sqlinsert_reg,"@usuario@",usuario);
			    }else{
			    	sqlinsert_reg = Util.replace(sqlinsert_reg,"@usuario@",usuario);
			    }
			    db.exec(_dso,sqlinsert_reg);
			    
				
				sqlupdate = getResource("update_entidad.sql");
				sqlupdate = Util.replace(sqlupdate, "@entidad@", entidad_archivo);
				sqlupdate = Util.replace(sqlupdate, "@registros_insertados@", String.valueOf(registros_insertados));
				//sqlupdate = Util.replace(sqlupdate, "@conteoTotal@", conteoTotal);
				db.exec(_dso,sqlupdate);
				

		storeDataSet( "archivos", _archivos );
		storeDataSet( "insercion", _insercion );
		storeDataSet( "record", _record );
		
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
				    	  
				    	 if (_req.getParameter("entidad_archivo")== null || _req.getParameter("entidad_archivo").equals("")){
								_record.addError("MSG-CD100-0001", " Seleccione los parametros requeridos (entidad)");
							    flag = false;
				    	  
					      }
				    	 
				    else{
				    	
		    		/* sql = getResource("busca_tabla.sql");
		    		 sql = Util.replace(sql, "@tabla@", _req.getParameter("entidad_archivo"));
		    		 _entidad = db.get(_dso, sql);
		    		 
		    		 while (_entidad.next()){
		    			 if (Integer.parseInt(_entidad.getValue("EXISTE")) < 1){
		    				 _record.addError("MSG-000-000", " Tabla no existe ");
								flag = false;
		    			 }
		    			 else{*/
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
			
			 
			
		 
		}
		return flag;	
	}
}

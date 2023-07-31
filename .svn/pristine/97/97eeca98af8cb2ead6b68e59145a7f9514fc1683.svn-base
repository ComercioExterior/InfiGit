package models.consultas_carga_inicial.carga_validacion;

import megasoft.*;import models.msc_utilitys.*;

public class Table extends MSCModelExtend
{

	/** DataSet del modelo */    
	private DataSet _datosProceso = null;
	private DataSet _registrosValidos = null;
	private DataSet _registrosInvalidos = null;
	private DataSet _aux = null;
	private DataSet _conteo = null;
	private int num_campos = 0;
	private DataSet camposMapa = new DataSet();
	
	/** 
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{
		_registrosInvalidos = new DataSet();
		_registrosInvalidos.append("registro", java.sql.Types.VARCHAR);
		_registrosInvalidos.append("resultado", java.sql.Types.VARCHAR);

		_registrosValidos = new DataSet();
		_registrosValidos.append("registro", java.sql.Types.VARCHAR);
		_registrosValidos.append("resultado", java.sql.Types.VARCHAR);

		StringBuffer sql = new StringBuffer();
		
		//Buscar los datos 
		sql.append("select p.*, to_char(p.z11_fe_fecha_inicio, 'dd-MM-yyyy hh:mm:ss AM') as fe_inicio, to_char(p.z11_fe_fecha_final, 'dd-MM-yyyy hh:mm:ss AM') as fe_final ,(a.z10_co_codigo_archivo || ' - ' || a.z10_no_nombre_archivo) as archivo from infi_tb_z11_procesos p, infi_tb_z10_archivos a where p.z10_co_codigo_archivo = a.z10_co_codigo_archivo and z11_cod_proceso = ").append(_record.getValue("cod_proceso"));
		_datosProceso = db.get(_dso, sql.toString());
				
		if(_datosProceso.next()){
			num_campos = Integer.parseInt(_datosProceso.getValue("Z11_NU_NUMERO_CAMPOS"));
		}
	
		armarNombresCampos();
		
		armarRegistrosInvalidos();
		
		armarRegistrosValidos();
		
		_record.setValue("total_registros_inval", String.valueOf(_registrosInvalidos.count()));
		_record.setValue("total_registros_val", String.valueOf(_registrosValidos.count()));
		
		storeDataSet( "datos_proceso", _datosProceso);
		
		storeDataSet( "registros_invalidos", _registrosInvalidos);	
		storeDataSet( "registros_validos", _registrosValidos);	
		
		storeDataSet( "record", _record ); 

	}


	private void armarNombresCampos() throws Exception {
		StringBuffer sql = new StringBuffer();
		
		sql.append("select * from infi_tb_z02_campos_mapa where z00_cod_mapa = ");
		sql.append(_record.getValue("cod_mapa"));
		
		System.out.println("armarNombresCampos: "+sql.toString());
		camposMapa = db.get(_dso, sql.toString());	
		
	}
	
	private String obtenerNombreCampo(String numCampo) throws Exception{
		String nombreCampo ="";
		
		if(camposMapa.count()>0){
			camposMapa.first();
			while(camposMapa.next()){
				if(camposMapa.getValue("z02_num_posicion_secuencia").equals(numCampo)){
					nombreCampo = camposMapa.getValue("z02_no_nombre_campo") + ": ";
					break;
				}
			}		
		}
		
		return nombreCampo;
	}

	private void armarRegistrosInvalidos() throws Exception {

		//buscar registros asociados al proceso
		StringBuffer sql = new StringBuffer();
		//buscar registros validos 
		sql.append("select * from infi_tb_z12_registros where z11_cod_proceso = ").append(_record.getValue("cod_proceso"));
		sql.append(" and z12_estatus = 0");
	
		_aux = db.get(_dso, sql.toString());
		
		int numRegistro = 1;
		while(_aux.next()){
			StringBuffer registro= new StringBuffer();
			String resultCampo ="";
			String valorCampo;
			
			if(_aux.getValue("z12_campo1_valor")!=null  && _aux.getValue("z12_campo1_valor").equals("null"))
				valorCampo = " ";
			else
				valorCampo = _aux.getValue("z12_campo1_valor");

			
			numRegistro = Integer.parseInt(_aux.getValue("z12_nu_numero_registro")) + 1;
			registro.append(" <table border='0' cellspacing='1' cellpadding='2' width='100%' class='datatable'>");
			
			registro.append("<tr><td colspan='2'>").append("<b>REGISTRO " +(numRegistro) +" EN ARCHIVO</b>").append("</td></tr>");
			
			/*registro.append("<tr>");
			registro.append("<th>").append("Valor").append("</th>");
			registro.append("<th>").append("Resultado").append("</th>");
			registro.append("</tr>");*/
			registro.append("<tr><td>").append(this.obtenerNombreCampo("1")).append(valorCampo).append("</td>");

			if(_aux.getValue("z12_campo1_resultado")!=null && _aux.getValue("z12_campo1_resultado").equals("0")){
				resultCampo = "Incorrecto - "+_aux.getValue("z12_campo1_mensaje");
			}/*else{
				resultCampo = "Correcto";
			}*/
			
			registro.append("<td>").append(resultCampo).append("</td>");
			registro.append("</tr>");
			registro.append("<tr>");
			registro.append("<td colspan='2'>").append("<a style='cursor:hand' href='javascript:mostrarCamposReg("+numRegistro+")'>M&aacute;s campos</a>").append("</td>");
			
			registro.append("</table>");
			registro.append("<table border='0' cellspacing='1' cellpadding='2' width='100%' class='datatable' style='display:none' id='registro_"+numRegistro+"'>");
			
			
			for(int i=2; i<=num_campos; i++){
				
				resultCampo =" ";
				
				if(_aux.getValue("z12_campo"+i+"_valor")!=null && _aux.getValue("z12_campo"+i+"_valor").equals("null"))
					valorCampo = "";
				else
					valorCampo = _aux.getValue("z12_campo"+i+"_valor");
				
				registro.append("<tr><td>").append(this.obtenerNombreCampo(String.valueOf(i))).append(valorCampo).append("</td>");
				
				if(_aux.getValue("z12_campo"+i+"_resultado")!=null && _aux.getValue("z12_campo"+i+"_resultado").equals("0")){
					resultCampo = "Incorrecto - "+_aux.getValue("z12_campo"+i+"_mensaje");
				}
				
				registro.append("<td>").append(resultCampo).append("</td>");
				registro.append("</tr>");
			}		
			
					
			registro.append("</table><br>");
			
			_registrosInvalidos.addNew();
			_registrosInvalidos.setValue("registro", registro.toString());
		}

		
	} 
	
	private void armarRegistrosValidos() throws Exception {

		//buscar registros asociados al proceso
		StringBuffer sql = new StringBuffer();
		//buscar registros validos 
		sql.append("select * from infi_tb_z12_registros where z11_cod_proceso = ").append(_record.getValue("cod_proceso"));
		sql.append(" and z12_estatus = 1");
	
		_aux = db.get(_dso, sql.toString());
		
		int numRegistro = 1;
		while(_aux.next()){
			StringBuffer registro= new StringBuffer();			
			String valorCampo;
			
			if(_aux.getValue("z12_campo1_valor")!=null  && _aux.getValue("z12_campo1_valor").equals("null"))
				valorCampo = " ";
			else
				valorCampo = _aux.getValue("z12_campo1_valor");

			
			numRegistro = Integer.parseInt(_aux.getValue("z12_nu_numero_registro")) + 1;
			registro.append(" <table border='0' cellspacing='1' cellpadding='2' width='100%' class='datatable'>");
			
			registro.append("<tr><td>").append("<b>REGISTRO " +(numRegistro) +" EN ARCHIVO</b>").append("</td></tr>");
			
			/*registro.append("<tr>");
			registro.append("<th>").append("Valor").append("</th>");
			registro.append("<th>").append("Resultado").append("</th>");
			registro.append("</tr>");*/
			registro.append("<tr><td>").append(this.obtenerNombreCampo("1")).append(valorCampo).append("</td>");
		
			registro.append("</tr>");
			registro.append("<tr>");
			registro.append("<td>").append("<a style='cursor:hand' href='javascript:mostrarCamposReg("+numRegistro+")'>M&aacute;s campos</a>").append("</td>");
			
			registro.append("</table>");
			registro.append("<table border='0' cellspacing='1' cellpadding='2' width='100%' class='datatable' style='display:none' id='registro_"+numRegistro+"'>");
						
			for(int i=2; i<=num_campos; i++){			
				
				if(_aux.getValue("z12_campo"+i+"_valor")!=null && _aux.getValue("z12_campo"+i+"_valor").equals("null"))
					valorCampo = "";
				else
					valorCampo = _aux.getValue("z12_campo"+i+"_valor");
				
				registro.append("<tr><td>").append(this.obtenerNombreCampo(String.valueOf(i))).append(valorCampo).append("</td>");
							
				registro.append("</tr>");
			}		
			
					
			registro.append("</table><br>");
			
			_registrosValidos.addNew();
			_registrosValidos.setValue("registro", registro.toString());
		}

		
	}


}
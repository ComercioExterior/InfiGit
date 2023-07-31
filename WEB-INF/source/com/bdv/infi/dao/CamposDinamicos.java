	/**
 * 
 */
package com.bdv.infi.dao;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import javax.sql.DataSource;

import megasoft.DataSet;
import megasoft.db;

import com.bdv.infi.data.CampoDinamico;
import com.bdv.infi.logic.interfaces.CamposDinamicosConstantes;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
/**
 * @author eel
 *
 */
public class CamposDinamicos extends GenericoDAO{
	
	public CamposDinamicos(DataSource ds) {
		super(ds);
		// TODO Auto-generated constructor stub
	}
	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}	
	
	/**Busca los campos dinamicos exitentes y retorna un dataset
	 * @param campos todos los campos dinamicos asociados a una unidad de inversion*/
	//Metodo modificado en requerimiento TTS_414 NM26659
	//Metodo modificado en requerimiento TTS_437 NM26659
	public void listar() throws Exception {
		StringBuffer sb = new StringBuffer();
		//sb.append("select campo_id, campo_nombre, campo_descripcion, campo_tipo, DECODE(campo_tipo, '1', 'General', 'Venta') as desc_campo_tipo from INFI_TB_036_CAMPOS_DINAMICOS order by campo_nombre");
		//NM29643 infi_TTS_466
		sb.append("SELECT   campo_id, campo_nombre, campo_descripcion, campo_tipo, CASE campo_tipo when '1' then 'General' when '2' then 'venta' when '3' then 'Fecha con Rango' when '4' then 'Fecha sin Validacion' when '5' then 'Fecha Mayor a' when '6' then 'Fecha Menor a' when '7' then 'Pais' WHEN '8' THEN 'Lista Dinamica' END as desc_campo_tipo, 'cd_'||lower(replace(campo_nombre,' ','_')) as nombre_reemplazo ");
		sb.append("FROM infi_tb_036_campos_dinamicos ORDER BY campo_nombre ");
		
		dataSet = db.get(dataSource, sb.toString());
		
	}
	
	/**
	 * Busca los campos dinamicos exitentes por tipo (General o Venta)
	 * @param tipo
	 * @throws Exception
	 */
	public void listarPorTipo(String tipo) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("select campo_id, campo_nombre, campo_descripcion, campo_tipo, DECODE(campo_tipo, '1', 'General', 'Venta') as desc_campo_tipo, 'cd_'||lower(replace(campo_nombre,' ','_')) as nombre_reemplazo ");
		sb.append(" from INFI_TB_036_CAMPOS_DINAMICOS");
		sb.append(" where campo_tipo = '").append(tipo).append("'");
		sb.append(" order by campo_nombre");
//		System.out.println("listarPorTipo -------> " + sb.toString());
		dataSet = db.get(dataSource, sb.toString());
		
	}

	
	/**Busca los los campo dinamico exitentes y retorna un dataset
	 * @param campos todos los campos dinamicos asociados a una unidad de inversion*/
	public void listarPorId(String campo) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("select campo_id, campo_nombre, campo_descripcion, campo_tipo from INFI_TB_036_CAMPOS_DINAMICOS");
		sb.append(" where campo_id=").append(campo);
		
		dataSet = db.get(dataSource, sb.toString());
		
	}
	
	/**
	 * Modifica el registro de una tabla 
	*/
	public String modificar(CampoDinamico campoDinamico){
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		sql.append("update INFI_TB_036_CAMPOS_DINAMICOS set ");
		//String tipoDatoString="";
		filtro.append(" campo_descripcion='").append(campoDinamico.getDescripcion()).append("',");
		filtro.append(" campo_nombre='").append(campoDinamico.getValor()).append("',");
		
		/*if(campoDinamico.getTipoDato()==1)
			tipoDatoString="Gen";
		else{
			if(campoDinamico.getTipoDato()==2)
				tipoDatoString="Vent";
		}*/
				
		filtro.append(" campo_tipo='").append(campoDinamico.getTipoDato()).append("'");
		filtro.append(" where campo_id=").append(campoDinamico.getIdCampo());
		sql.append(filtro);		
		return(sql.toString());
	}


	/**
	 * Inserta el registro en la tabla  
	 */
	public String insertar(CampoDinamico campoDinamico) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("insert into INFI_TB_036_CAMPOS_DINAMICOS (campo_id, campo_nombre, campo_descripcion, campo_tipo)values (");
		String idCampo = dbGetSequence(dataSource, ConstantesGenerales.SECUENCIA_CAMPOS_DINAMICOS);
		campoDinamico.setIdCampo(Integer.parseInt(idCampo));
		//String tipoDatoString="";
		sql.append(idCampo).append(",");
		sql.append("'").append(campoDinamico.getValor()).append("',");
		sql.append("'").append(campoDinamico.getDescripcion()).append("',");
		
		/*if(campoDinamico.getTipoDato()==1)
			tipoDatoString="Gen";
		else{
			if(campoDinamico.getTipoDato()==2)
				tipoDatoString="Vent";
		}*/
				
		sql.append("'").append(campoDinamico.getTipoDato()).append("')");
		return(sql.toString());
	}
	
	/**
	 * Elimina el registro en la tabla. 
	*/
	public String eliminar(CampoDinamico campoDinamico){
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		sql.append("delete from INFI_TB_036_CAMPOS_DINAMICOS where");
		
		filtro.append(" campo_id=").append(campoDinamico.getIdCampo());
		sql.append(filtro);			
		return(sql.toString());
	}
	
	/**
	 * Lista los campos dinamicos asociados a una unidad de inversion.  
	 * @param long unInvId (id de la unidad de inversion)
	 * @param int htmltrue 1 si se quiere el dataset con el html, 0 si se quiere el dataset con los campos dinamicos
	 * @return Dataset
	 * @throws Exception 
	*/
	public DataSet listarCamposDinamicosUnidadInversion(long unInvId,int htmltrue, String... tipoProd) throws Exception{
		StringBuffer sb = new StringBuffer();
		int contDatepickers = 0, contPaises = 0;
		DataSet htmlDinamic=new DataSet();
		htmlDinamic.append("trtd", java.sql.Types.VARCHAR);
		htmlDinamic.append("func_datepicker", java.sql.Types.VARCHAR);
		sb.append("select '' as valor,INFI_TB_036_CAMPOS_DINAMICOS.CAMPO_ID,INFI_TB_114_UI_CAMPOS_DINAMIC.UNDINV_ID,INFI_TB_036_CAMPOS_DINAMICOS.* " +
				" from INFI_TB_114_UI_CAMPOS_DINAMIC" +
				" left join INFI_TB_036_CAMPOS_DINAMICOS on INFI_TB_114_UI_CAMPOS_DINAMIC.CAMPO_ID=" +
				"INFI_TB_036_CAMPOS_DINAMICOS.CAMPO_ID where INFI_TB_114_UI_CAMPOS_DINAMIC.UNDINV_ID=");
		sb.append(unInvId);		
		dataSet = db.get(dataSource, sb.toString());//Dataset Principal
		if(htmltrue==1){
		if(dataSet.count()>0){
			dataSet.first();
			//Se verifica cuantos campos de tipo pais hay
			while(dataSet.next()){
				if(dataSet.getValue("campo_tipo").equals(CamposDinamicosConstantes.TIPO_PAIS))	contPaises++;
			}
			//System.out.println("CONTADOR PAISES: "+contPaises);
			//Se vuelve a la primera posicion del dataSet
			dataSet.first();
			StringBuffer html = new StringBuffer();
			StringBuffer html_pais = new StringBuffer();
			StringBuffer html_fechas = new StringBuffer();
			StringBuffer jsDatepicker = new StringBuffer();
			while(dataSet.next()){//Se arma el html
				//Dataset con los valores de campos dinamicos
				/*StringBuffer html = new StringBuffer();
				StringBuffer html_pais = new StringBuffer();
				StringBuffer html_fechas = new StringBuffer();*/
				//Verificar longitud pendiente igualmente tipo de dato a introducir en el campo
				//html.append("<tr><td>");
				/*if( (dataSet.getValue("campo_tipo").equals(CamposDinamicosConstantes.TIPO_FECHA_RANGO) || 
					dataSet.getValue("campo_tipo").equals(CamposDinamicosConstantes.TIPO_FECHA_MENOR) || 
					dataSet.getValue("campo_tipo").equals(CamposDinamicosConstantes.TIPO_FECHA_MAYOR) //|| 
					//dataSet.getValue("campo_tipo").equals(CamposDinamicosConstantes.TIPO_FECHA_IGUAL)
					) && 
					!dataSet.getValue("campo_descripcion").equals("") && dataSet.getValue("campo_descripcion").length()>0 ){
					html.append(dataSet.getValue("campo_descripcion"));
				}else{*/
				//	html.append(dataSet.getValue("campo_nombre"));
				//}
				//html.append(":&nbsp;</td><td>");
				
				//html.append("<tr><td>").append(dataSet.getValue("campo_nombre")).append(":&nbsp;</td><td>");
				
				if(dataSet.getValue("campo_tipo").equals(CamposDinamicosConstantes.TIPO_FECHA) || 
					dataSet.getValue("campo_tipo").equals(CamposDinamicosConstantes.TIPO_FECHA_RANGO) || 
					dataSet.getValue("campo_tipo").equals(CamposDinamicosConstantes.TIPO_FECHA_MENOR) || 
					dataSet.getValue("campo_tipo").equals(CamposDinamicosConstantes.TIPO_FECHA_MAYOR) //|| 
					//dataSet.getValue("campo_tipo").equals(CamposDinamicosConstantes.TIPO_FECHA_IGUAL) 
					){ //Si es algun tipo que requiera calendario
					html_fechas.append("<tr><td>").append(dataSet.getValue("campo_nombre")).append(":&nbsp;</td><td>");
					contDatepickers++; //Incrementa el contador de calendarios
					html_fechas.append("<INPUT id=\"datepicker").append(contDatepickers).append("\" TYPE='TEXT' readonly='readonly' SIZE='10' MAXLENGTH='50' NAME='campo_dinamico_").append(dataSet.getValue("campo_id")).append("'>");
					//html.append("<INPUT id=\"datepicker").append(contDatepickers).append("\" style=\"left:300px; position:absolute; bottom:0\" type=\"text\" NAME='campo_dinamico_").append(dataSet.getValue("campo_id")).append("'>");
//					html.append("<INPUT NAME='campo_dinamico_").append(dataSet.getValue("campo_id")).append("' TYPE='hidden' >"); //SIZE='10' MAXLENGTH='50'
//					html.append("<div id=\"datepicker").append(contDatepickers).append("\" >");
					html_fechas.append("</td></tr>");
				}else{
					if(dataSet.getValue("campo_tipo").equals(CamposDinamicosConstantes.TIPO_PAIS)){
						html_pais.append("<tr><td>").append(dataSet.getValue("campo_nombre")).append(":&nbsp;</td><td>");
						PaisDAO pDAO = new PaisDAO(dataSource);
						pDAO.consultarPaises();
						if(pDAO.getDataSet().count()>0){
							pDAO.getDataSet().first();
							html_pais.append("<SELECT NAME='campo_dinamico_").append(dataSet.getValue("campo_id")).append("' style=\"position:relative; z-index:0;\">");
							for(int i=0; i<pDAO.getDataSet().count(); i++){
								pDAO.getDataSet().next();
								html_pais.append("<OPTION value='").append(pDAO.getDataSet().getValue("cod_pais")).append("-").append(pDAO.getDataSet().getValue("desc_pais")).append("'>").append(pDAO.getDataSet().getValue("desc_pais")).append("</OPTION>");
							}
							html_pais.append("</SELECT>");
						}
						html_pais.append("</td></tr>");
					}else if (dataSet.getValue("campo_tipo").equals(CamposDinamicosConstantes.TIPO_LISTA_DINAMICA)){
						
						html_pais.append("<tr><td>").append(dataSet.getValue("campo_nombre")).append(":&nbsp;</td><td>");
						ListasDinamicas listasDinamicas = new ListasDinamicas(dataSource);
						
						listasDinamicas.listarPorId(dataSet.getValue("CAMPO_ID"));
						if(listasDinamicas.getDataSet().count()>0){
							listasDinamicas.getDataSet().first();
							html_pais.append("<SELECT NAME='campo_dinamico_").append(dataSet.getValue("campo_id")).append("' style=\"position:relative; z-index:0;\">");
							for(int i=0; i<listasDinamicas.getDataSet().count(); i++){
								listasDinamicas.getDataSet().next();
								html_pais.append("<OPTION value='").append(listasDinamicas.getDataSet().getValue("DESCRIPCION")).append("'>").append(listasDinamicas.getDataSet().getValue("ID_ITEM")).append("</OPTION>");
							}
							html_pais.append("</SELECT>");
						}
						html_pais.append("</td></tr>");
					
					}else{
					
						html.append("<tr><td>").append(dataSet.getValue("campo_nombre")).append(":&nbsp;</td><td>");
						//Tipo General y Venta (1 y 2)
						html.append("<INPUT TYPE='TEXT' SIZE='12' MAXLENGTH='50'");
						html.append("VALUE='' NAME='campo_dinamico_").append(dataSet.getValue("campo_id")).append("'>");
						html.append("</td></tr>");
					}
				}
				//html.append("</td></tr>");
				
				/*htmlDinamic.addNew();
				htmlDinamic.setValue("trtd", (html_pais.append(html_fechas.toString()).append(html.toString())).toString() );
				System.out.println("COD HTMLLLLLLLL\n"+(html_pais.append(html_fechas.toString()).append(html.toString())).toString());
				*/
				
				HashMap<String,String> params;
				//StringBuffer jsDatepicker = new StringBuffer();
				jsDatepicker.append("");
				StringBuffer rango = new StringBuffer();
				ParametrosDAO paramDAO = null;
				paramDAO = new ParametrosDAO(dataSource);
				
				
				if(dataSet.getValue("campo_tipo").equals(CamposDinamicosConstantes.TIPO_FECHA) || 
						dataSet.getValue("campo_tipo").equals(CamposDinamicosConstantes.TIPO_FECHA_RANGO) || 
						dataSet.getValue("campo_tipo").equals(CamposDinamicosConstantes.TIPO_FECHA_MAYOR) || 
						dataSet.getValue("campo_tipo").equals(CamposDinamicosConstantes.TIPO_FECHA_MENOR) //|| 
						//dataSet.getValue("campo_tipo").equals(CamposDinamicosConstantes.TIPO_FECHA_IGUAL) 
				){ //Si es alguno de los tipos que requiere calendario se arma el codigo de llamada js al datepicker
				
					if(paramDAO!=null && dataSet.getValue("campo_tipo").equals(CamposDinamicosConstantes.TIPO_FECHA_RANGO)){
						params = paramDAO.buscarParametros(ParametrosSistema.FECHAS_CAMPOS_DINAMICOS, dataSet.getValue("campo_nombre"));
						if(params.size()>0){
							String[] values = params.get(dataSet.getValue("campo_nombre")).split(";");
							if(values.length>1 && !values[0].equals("") && values[0]!=null && values[0].length()>0){
								rango.append("minDate: \"").append(values[0]).append("\", ");
								rango.append("maxDate: \"").append(values[1]).append("\", ");
							}
						}
					}else{
						if(dataSet.getValue("campo_tipo").equals(CamposDinamicosConstantes.TIPO_FECHA_MAYOR) || 
							dataSet.getValue("campo_tipo").equals(CamposDinamicosConstantes.TIPO_FECHA_MENOR) //|| 
							//dataSet.getValue("campo_tipo").equals(CamposDinamicosConstantes.TIPO_FECHA_IGUAL) 
							){
							params = paramDAO.buscarParametros(ParametrosSistema.FECHAS_CAMPOS_DINAMICOS, dataSet.getValue("campo_nombre"));
							if(params.size()>0){
								String[] values = params.get(dataSet.getValue("campo_nombre")).split(";");
								if(values.length>1){								
									if(dataSet.getValue("campo_tipo").equals(CamposDinamicosConstantes.TIPO_FECHA_MAYOR)){
										rango.append("minDate: \"").append(values[1]).append("\", ");
										rango.append("maxDate: null, ");
									}else{
										if(dataSet.getValue("campo_tipo").equals(CamposDinamicosConstantes.TIPO_FECHA_MENOR)){
											rango.append("minDate: null, ");
											rango.append("maxDate: \"").append(values[1]).append("\", ");
										}/*else{ //IGUAL
											rango.append("minDate: ").append(values[1]).append(", ");
											rango.append("maxDate: ").append(values[1]).append(", ");
										}*/
									}
								}
							}
						}else{ //TIPO_FECHA
							rango.append("");
						}
					}
				
					jsDatepicker.append("$(function() {");
					jsDatepicker.append("$( \"#datepicker").append(contDatepickers).append("\" ).datepicker({");
					jsDatepicker.append("dateFormat: \"dd-mm-yy\", ");
					jsDatepicker.append(rango.toString());
					jsDatepicker.append("monthNames: [ \"Enero\", \"Febrero\", \"Marzo\", \"Abril\", \"Mayo\", \"Junio\", \"Julio\", \"Agosto\", \"Septiembre\", \"Octubre\", \"Noviembre\", \"Diciembre\" ], ");
					jsDatepicker.append("monthNamesShort: [ \"Enero\", \"Febrero\", \"Marzo\", \"Abril\", \"Mayo\", \"Junio\", \"Julio\", \"Agosto\", \"Septiembre\", \"Octubre\", \"Noviembre\", \"Diciembre\" ], ");
					jsDatepicker.append("dayNamesMin: [ \"Do\", \"Lu\", \"Ma\", \"Mi\", \"Ju\", \"Vi\", \"Sa\" ], ");
					jsDatepicker.append("changeMonth: true, ");
					jsDatepicker.append("changeYear: true, ");
					jsDatepicker.append("showOn: \"button\", ");
					jsDatepicker.append("buttonImage: \"../images/infi_gn_calendar.gif\", ");
					jsDatepicker.append("buttonImageOnly: true, ");
					jsDatepicker.append("showAnim: \"slideDown\", ");
					//Funciona pero no esta cableada la posicion del div del calendario
					//jsDatepicker.append("beforeShow: function(input) { var x = 90; var y = 0; field = $(input); left = field.position().left + x; bottom = field.position().bottom + y; setTimeout(function(){ $('#ui-datepicker-div').css({'top': '").append(1000+(30*contDatepickers)).append("px', 'bottom':'', 'left': left + 'px'}); },1)} ");
					jsDatepicker.append("beforeShow: function(input) { var x = 90; var y = 0; field = $(input); left = field.position().left + x; bottom = field.position().bottom + y; setTimeout(function(){ $('#ui-datepicker-div').css({'top': '");
					if(tipoProd.length>0 && tipoProd[0]!=null){ //Si se paso el tipo de producto
						if(tipoProd[0].equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL)){
							jsDatepicker.append(974+(23*contPaises)+(26*contDatepickers));
						}else{
							if(tipoProd[0].equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA)){
								jsDatepicker.append(522+(23*contPaises)+(26*contDatepickers));
							}else{
								if(tipoProd[0].equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA)){
									jsDatepicker.append(478+(23*contPaises)+(26*contDatepickers));
								}else{
									jsDatepicker.append(478+(23*contPaises)+(26*contDatepickers));
								}
							}
						}
					}else{
						jsDatepicker.append(473+(30*contDatepickers)); //Si no se le indico el tipo de producto
					}
					jsDatepicker.append("px', 'bottom':'', 'left': left + 'px', 'position': 'absolute', 'z-index': '2'}); },1)} ");
					//jsDatepicker.append(1000+(30*contDatepickers)).append("px', 'bottom':'', 'left': left + 'px'}); },1)} ");
					//jsDatepicker.append("beforeShow: function(input) { field = $(input); setTimeout(function(){ $('#ui-datepicker-div').css({'position': 'static'}); },1)} ");
					//jsDatepicker.append("beforeShow: function(input) { var x = 90; var y = 0; field = $(input); left = field.position().left + x; bottom = field.position().bottom + y; setTimeout(function(){ $('#ui-datepicker-div').css({'position':'static', 'top':'', 'bottom':'', 'left':''}); },1)} ");
					//jsDatepicker.append("beforeShow: function(input) { var x = 100; var y = 100; field = $(input); left = field.position().left + x; bottom = field.position().bottom + y; setTimeout(function(){ $('#ui-datepicker-div').css({'top':'', 'bottom':bottom + 'px', 'left': left + 'px'}); },1)} ");
					//jsDatepicker.append("beforeShow: function(input) { var x = 100; var y = 0; field = $(input); left = field.position().left + x; bottom = field.position().bottom + y; setTimeout(function(){ $('#ui-datepicker-div').css({'top':bottom + 'px', 'bottom':'', 'left': left + 'px'}); },1)} ");
					//jsDatepicker.append("inline: false, ");
					//jsDatepicker.append("altField: '#campo_dinamico_").append(dataSet.getValue("campo_id")).append("', ");
					//jsDatepicker.append("showOptions: { direction: \"down\" }, ");
					//jsDatepicker.append("changeMonth: true, changeYear: true, ");
					//jsDatepicker.append("showOn: \"both\", ");
					//jsDatepicker.append("onSelect: function(dateText, inst) { $(this).blur().change(); }, ");
					jsDatepicker.append("});");
					jsDatepicker.append("});").append("\n");
					
				}//Algun tipo fecha
				
				//htmlDinamic.setValue("func_datepicker", jsDatepicker.toString());
				
			}//fin while
			
			htmlDinamic.addNew();
			htmlDinamic.setValue("trtd", (html_pais.append(html_fechas.toString()).append(html.toString())).toString() );
			//System.out.println("COD HTMLLLLLLL NEWWWWWWWW\n"+htmlDinamic.getValue("trtd"));
			
			htmlDinamic.setValue("func_datepicker", jsDatepicker.toString());
			
		}//fin if
		return htmlDinamic;
	}else{
		return dataSet;
	}
	}//fin listarCamposDinamicosUnidadInversion
	
	
	/**
	 * Lista los campos dinamicos y sus valores asociados a una orden especifica  
	 * @param long unInvId (id de la unidad de inversion)
	 * @param int htmltrue 1 si se quiere el dataset con el html, 0 si se quiere el dataset con los campos dinamicos
	 * @return Dataset
	 * @throws Exception 
	*/
	public DataSet listarCamposDinamicosOrdenes(long ordId,int htmltrue) throws Exception{
		StringBuffer sb = new StringBuffer();
		DataSet htmlDinamic=new DataSet();
		htmlDinamic.append("trtd",java.sql.Types.VARCHAR);
		sb.append("select cd.CAMPO_DESCRIPCION,cd.CAMPO_NOMBRE,o.ordene_id,ocd.* from infi_tb_204_ordenes o left join INFI_TB_205_ORDENES_CAMP_DIN ocd on o.ordene_id=ocd.ORDENE_ID left join INFI_TB_036_CAMPOS_DINAMICOS cd on ocd.CAMPO_ID=cd.CAMPO_ID where ocd.ordene_id=");
		sb.append(ordId);//cambiar
		sb.append(" order by cd.CAMPO_ID");
		//System.out.println("CamposDinamicos.listarCamposDinamicosOrdenes: Exporta orden por blotter a excel");//+sb.toString()
		dataSet = db.get(dataSource, sb.toString());//Dataset Principal
		if(htmltrue==1){
		if(dataSet.count()>0){
			dataSet.first();
			while(dataSet.next()){//Se arma el html
				//Dataset con los valores de campos dinamicos
				StringBuffer html = new StringBuffer();
				//Verificar longitud pendiente igualmente tipo de dato a introducir en el campo
				html.append("<tr><td>").append(dataSet.getValue("campo_nombre")).append(":&nbsp;</td><td>");
				html.append(dataSet.getValue("campo_valor")).append("</td></tr>");
				htmlDinamic.addNew();
				htmlDinamic.setValue("trtd",html.toString());
			}//fin while	
		}//fin if
		return htmlDinamic;
	}else{
		return dataSet;
	}
	}
	
	/**
	 * Devuelve el sql necesario para consultar los campos dinámicos de una orden. El sql es estilo preparedStament cuyo valor de condición es el número de orden  
	 * @param long ordId id de la orden a consultar
	 * @return consulta que debe ejecutarse
	 * @throws Exception en caso de error
	*/
	public String listarCamposDinamicosOrdenes() throws Exception{
		String sql = "select cd.CAMPO_DESCRIPCION,cd.CAMPO_NOMBRE,ocd.* from " +
				" infi_tb_204_ordenes o, INFI_TB_205_ORDENES_CAMP_DIN ocd,  INFI_TB_036_CAMPOS_DINAMICOS cd " +
				" where o.ordene_id=ocd.ORDENE_ID and ocd.CAMPO_ID=cd.CAMPO_ID and ocd.ordene_id=? order by cd.CAMPO_ID";
		return sql;
	}
		
	
	/**Busca los datos del campo dnamico y retorna un dataset
	 * @param campos todos los campos dinamicos asociados a una unidad de inversion*/
	public void listarCamposDinamico(String campos, long orden) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("select cd.campo_nombre, oc.campo_valor from INFI_TB_036_CAMPOS_DINAMICOS cd, INFI_TB_205_ORDENES_CAMP_DIN oc, INFI_TB_204_ORDENES o where oc.campo_id=cd.campo_id and oc.ordene_id=o.ordene_id and oc.ordene_id=").append(orden).append(" and oc.campo_id in (").append(campos).append(")");
		dataSet = db.get(dataSource, sb.toString());
		if(dataSet.count()>0){
			dataSet.next();
		}
	}
	
	/**Metodo que genera un dataset
	 * para mostrar el indicador Si (1) o No (0)
	 * Util para combos en filtros
	 */
	public DataSet tipo() throws Exception {		
		
		DataSet _tipo=new DataSet();
		_tipo.append("num",java.sql.Types.VARCHAR);
		_tipo.append("tipo",java.sql.Types.VARCHAR);
		_tipo.addNew();
		_tipo.setValue("num",CamposDinamicosConstantes.TIPO_GENERAL);
		_tipo.setValue("tipo","General");
		_tipo.addNew();
		_tipo.setValue("num",CamposDinamicosConstantes.TIPO_VENTA);
		_tipo.setValue("tipo","Venta");
		_tipo.addNew();
		_tipo.setValue("num",CamposDinamicosConstantes.TIPO_FECHA);
		_tipo.setValue("tipo","Fecha");
		_tipo.addNew();
		_tipo.setValue("num",CamposDinamicosConstantes.TIPO_FECHA_RANGO);
		_tipo.setValue("tipo","Fecha Entre Rango");
		_tipo.addNew();
		_tipo.setValue("num",CamposDinamicosConstantes.TIPO_FECHA_MAYOR);
		_tipo.setValue("tipo","Fecha Mayor a");
		_tipo.addNew();
		_tipo.setValue("num",CamposDinamicosConstantes.TIPO_FECHA_MENOR);
		_tipo.setValue("tipo","Fecha Menor a");
		/*_tipo.addNew();
		_tipo.setValue("num",CamposDinamicosConstantes.TIPO_FECHA_IGUAL);
		_tipo.setValue("tipo","Fecha Igual a");*/
		_tipo.addNew();
		_tipo.setValue("num",CamposDinamicosConstantes.TIPO_LISTA_DINAMICA);
		_tipo.setValue("tipo","Lista Dinamica");
		_tipo.addNew();
		_tipo.setValue("num",CamposDinamicosConstantes.TIPO_PAIS);
		_tipo.setValue("tipo","Pais");
							
		return _tipo;
	}
	
	/**Verifica si el Id esta siendo referenciado por la tabla INFI_TB_205_ORDENES_CAMP_DIN
	 */
	public void verificar(CampoDinamico campoDinamico) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select campo_id from INFI_TB_205_ORDENES_CAMP_DIN where");
		sql.append(" campo_id=").append(campoDinamico.getIdCampo());

		dataSet =db.get(dataSource,sql.toString());
	}
	
	/**Verifica si el Id esta siendo referenciado por la tabla INFI_TB_114_UI_CAMPOS_DINAMIC
	 */
	public void verificar1(CampoDinamico campoDinamico) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select campo_id from INFI_TB_114_UI_CAMPOS_DINAMIC where");
		sql.append(" campo_id=").append(campoDinamico.getIdCampo());

		dataSet =db.get(dataSource,sql.toString());
	}
	
	/**Metodo que verifica si existe un registro con el mismo nombre del campo dinamico en base de datos
	*  @param String valorCampo
	*  @throws Exception lanza una excepci&ooacute;n si hay un error en la operaci&oacute;n
	*/
	public  boolean verificarNombreCampoDinamicoExiste(String valorCampo) throws Exception{
		boolean encontro=false;
		StringBuffer sb=new StringBuffer();
		sb.append("select * from ");
		sb.append("INFI_TB_036_CAMPOS_DINAMICOS");
		sb.append(" where initCap(");
		sb.append("CAMPO_NOMBRE");
		sb.append(") = initCap('");
		sb.append(valorCampo);
		sb.append("')");

		DataSet ds=db.get(dataSource,sb.toString());
		if (ds.count()>0)
			encontro=true;
		
		return encontro;
	}
	
	/**
	 * Lista los campos dinamicos asociados a una unidad de inversion 
	 * @param String unidadInversion (id de la unidad de inversion)
	 * @param DataSource _dso data source	
	 * @return ArrayList<CampoDinamico>
	 * @throws Exception 
	*/
	
	public ArrayList<CampoDinamico> listarCamposDinamicosUnidadInversion(String unidadInversion,DataSource _dso) throws Exception{
        Transaccion transaccion 			= new Transaccion(_dso);
        Statement statement 				= null;
        ResultSet campos 					= null;
		ArrayList<CampoDinamico> camposD	= null;
		CampoDinamico 	campo 				= null;
		StringBuffer sb						= new StringBuffer();
		
		try {
			transaccion = new Transaccion(_dso);
			transaccion.begin();
			statement = transaccion.getConnection().createStatement();
			sb.append("select distinct(cd.CAMPO_NOMBRE),cd.CAMPO_ID from INFI_TB_036_CAMPOS_DINAMICOS cd,INFI_TB_205_ORDENES_CAMP_DIN ocd");
			sb.append(" where ocd.ordene_id in (select ordene_id from infi_tb_204_ordenes o where UNIINV_ID=")
					.append(unidadInversion).append(") and ");
			sb.append(" ocd.CAMPO_ID=cd.CAMPO_ID order by cd.CAMPO_ID");
			campos = statement.executeQuery(sb.toString());
			camposD = new ArrayList<CampoDinamico>();
			while (campos.next()) {
				campo = new CampoDinamico();
				campo.setCampoNombre(campos.getString("campo_nombre"));
				campo.setIdCampo(campos.getInt("CAMPO_ID"));
				camposD.add(campo);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {			
			try {
				if (campos != null){
					campos.close();
				}
				if (statement != null){
					statement.close();
				}
				transaccion.closeConnection();
			} catch (Exception e) {							
				e.printStackTrace();
			}
		}					
		return camposD;
	}
	
	/**
	 * Lista los campos dinamicos y sus valores asociados a una orden especifica  
	 * ordenado segun los titulos de la unidad de inversion. (PARA EL REPORTE)
	 * @param String ordId (id de la orden)
	 * @param DataSource _dso data source
	 * @param ArrayList<CampoDinamico> camposD arreglo de campos dinamicos de la unidad de inversion
	 * @return ArrayList<CampoDinamico>
	 * @throws Exception 
	*/
	public ArrayList<CampoDinamico> listarCamposDinamicosOrdenes(String ordId,DataSource _dso, ArrayList<CampoDinamico> camposD) throws Exception{
		StringBuffer sb 	= new StringBuffer();
		ResultSet campos 	= 	null;
		CampoDinamico campo	= null;	
		ArrayList<CampoDinamico> listaCampos=new ArrayList<CampoDinamico>();
		try {
			transaccion= new Transaccion(_dso);
			transaccion.begin();
			statement = transaccion.getConnection().createStatement();
			//sb.append("select cd.CAMPO_DESCRIPCION,cd.CAMPO_NOMBRE,o.ordene_id,ocd.* from infi_tb_204_ordenes o left join INFI_TB_205_ORDENES_CAMP_DIN ocd on o.ordene_id=ocd.ORDENE_ID left join INFI_TB_036_CAMPOS_DINAMICOS cd on ocd.CAMPO_ID=cd.CAMPO_ID where ocd.ordene_id=");
			sb.append("select cd.CAMPO_DESCRIPCION,cd.CAMPO_NOMBRE,o.ordene_id,ocd.* from infi_tb_204_ordenes o,  INFI_TB_205_ORDENES_CAMP_DIN ocd,INFI_TB_036_CAMPOS_DINAMICOS cd where o.ordene_id=ocd.ORDENE_ID and  ocd.CAMPO_ID=cd.CAMPO_ID and ocd.ordene_id=");
			sb.append(ordId);
			sb.append(" order by cd.CAMPO_ID");
			
			campos = statement.executeQuery(sb.toString());
			
			try {
				if (campos.next()) {
					for (int i = 0; i <= camposD.size(); i++) {
						{ 
							if (campos.getString("campo_nombre").equalsIgnoreCase(camposD.get(i).getCampoNombre())) {
								campo = new CampoDinamico();
								campo.setCampoNombre(campos.getString("campo_nombre"));
								campo.setValor(campos.getString("campo_valor"));								
								campos.next();
								//if (!campos.next()) break; System.out.println(campos.getString("campo_nombre"));
							} else {
								campo = new CampoDinamico();
								campo.setCampoNombre("");
								campo.setValor("");
							}
							listaCampos.add(campo);
						}
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}			
			//Completar campos sin data
			if(listaCampos.size()<camposD.size()) {				
				for (int i=0; i<camposD.size()-listaCampos.size();i++){						
					campo=new CampoDinamico();
					campo.setCampoNombre(" ");
					listaCampos.add(campo);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {			
			try {
				if (campos != null){
					campos.close();
				}
				if (statement != null){
					statement.close();
				}
				transaccion.closeConnection();
			} catch (Exception e) {							
				e.printStackTrace();
			}
		}			
		return listaCampos;
		
	}
	
	/**
	 * Lista los campos dinamicos asociados a una orden especifica  
	 * @param long ordenId (id de la orden)	
	 * @throws Exception 
	*/
	public void consultarCamposDinamicosOrden(long ordenId) throws Exception{
		StringBuffer sb = new StringBuffer();		
		sb.append(" select cd.CAMPO_ID,cd.CAMPO_VALOR from INFI_TB_205_ORDENES_CAMP_DIN cd");
		sb.append(" where cd.ORDENE_ID=");
		sb.append(ordenId);
		sb.append(" order by cd.CAMPO_ID");
		
		dataSet = db.get(dataSource, sb.toString());//Dataset Principal
		
	}
	
	/**Busca los campos de listas dinamicas exitentes y retorna un dataset*/
	//Metodo modificado en requerimiento TTS_437 CT19349
	public void listaDinamica(int id) throws Exception {
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("SELECT   id_item, descripcion ");
		sb.append("FROM infi_tb_037_listas_dinamicas ");
		sb.append("WHERE campo_dinamico_id = '").append(id).append("' ");
		sb.append("ORDER BY id_item ");
		
		
		dataSet = db.get(dataSource, sb.toString());
		
	}	
	
	/**Metodo que verifica si una lista dinamica esta asociada a una unidad de inversion publicada*/
	//Metodo modificado en requerimiento TTS_437 CT19349
	public void verificarUnidadPublicada(int id) throws Exception {
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("SELECT ui.Undinv_Status, Inter.Campo_Id ");
		sb.append("FROM Infi_Tb_106_Unidad_Inversion Ui ");
		sb.append("Inner Join Infi_Tb_114_Ui_Campos_Dinamic Inter ");
		sb.append("ON Inter.Undinv_Id = Ui.Undinv_Id ");
		sb.append("INNER Join Infi_Tb_036_Campos_Dinamicos Cd ");
		sb.append("ON Cd.Campo_Id = Inter.Campo_Id ");
		sb.append("WHERE Cd.Campo_Id = '").append(id).append("' ");
		sb.append("AND Ui.Undinv_Status = 'PUBLICADA' ");
		
//		System.out.println(sb.toString());
		
		dataSet = db.get(dataSource, sb.toString());
		
	}
	
	
	
}//fin de la clase

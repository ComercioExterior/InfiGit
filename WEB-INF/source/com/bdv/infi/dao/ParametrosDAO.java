package com.bdv.infi.dao;

import java.util.HashMap;
import javax.sql.DataSource;

import com.bdv.infi.data.ClienteIntervencion;
import com.bdv.infi.data.GrupoOrdenesBloqueo;
import com.bdv.infi.data.Parametros;
import com.bdv.infi.data.GrupoParametros;
import megasoft.DataSet;
import megasoft.db;


/**Clase destinada para la obtenci&oacute;n de los par&aacute;metros registrados en el sistema*/
public class ParametrosDAO extends GenericoDAO{
	
	public ParametrosDAO(DataSource _dso) {
		super(_dso);
	}

	/** Busca el valor de un parametro dado un nombre espec&iacute;fico de par&aacute;metro
	 * @return String con el valor del par&aacute;metro solicitado
	 * @throws Exception arroja una excepci&oacute;n en caso de un error
	 * */
	public static String listarParametros(String nameParameter,DataSource dataSource) throws Exception{
		DataSet dataSet;
		StringBuffer sb=new StringBuffer();
		sb.append("select PARVAL_VALOR from infi_tb_002_param_tipos");
		String parameter="";
		sb.append(" where infi_tb_002_param_tipos.PARTIP_NOMBRE_PARAMETRO='").append(nameParameter).append("'");	
		dataSet=db.get(dataSource, sb.toString());
		
			if(dataSet.count()>0)
			{
				dataSet.first();
				dataSet.next();
				parameter=dataSet.getValue("PARVAL_VALOR");
			}//fin if
			System.out.println("listarParametros---> "+sb);
		return parameter;
	}//fin metodo

	@Override
	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Modifica el valor de un parametro espec&iacute;fico del sistema
	 * @param parametro
	 * @param nombreParametro
	 * @throws Exception
	 */
	public void modificar(Parametros parametro, String nombreParametro) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("UPDATE infi_tb_002_param_tipos set PARVAL_VALOR = '").append(parametro.getValor(nombreParametro)).append("'");
		sql.append(" where PARTIP_NOMBRE_PARAMETRO = '").append(nombreParametro).append("'");
		
		db.exec(dataSource, sql.toString());
		
	}
	
	public void modificarParametroPorGrupo(String parametro, String codigoGrupo,String nombreParametro) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("UPDATE infi_tb_002_param_tipos set PARVAL_VALOR = '").append(parametro).append("'");
		sql.append(" where PARTIP_NOMBRE_PARAMETRO = '").append(nombreParametro.trim()).append("'").append(" AND PARGRP_ID = '").append(codigoGrupo).append("'");
		System.out.println("modificooooooooooooooooooooooooo : " + sql.toString());
		db.exec(dataSource, sql.toString());
		
	}
	/**
	 * lista todos los datos de un parametro específico y los almacena en un dataset 
	 * @param parametro nombre del parámetro
	 * @param grupo ide del grupo a que pertenece el parámetro
	 * @throws Exception
	 */
	public void listarParametros(String parametro, String grupo) throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT *  FROM INFI_TB_002_PARAM_TIPOS");
		sql.append(" where infi_tb_002_param_tipos.PARTIP_NOMBRE_PARAMETRO='").append(parametro).append("'");
		sql.append(" and pargrp_id='" + grupo + "'");
		dataSet = db.get(dataSource, sql.toString());
	}
	
//		public void listarCliente(String idOperacion) throws Exception{
//		StringBuffer sql= new StringBuffer();
//		sql.append("SELECT COD_CLIENT,NOMBRE_CLIENT,FECHA_VALOR,MTO_DIVISAS,COD_CUENTA_DIVISAS,COD_CUENTA_BS,COD_MONEDA_ISO,ID_OPER FROM INFI_TB_235_INTERVENCION");
//		sql.append(" where ID_OPER='").append(idOperacion).append("'");
//		System.out.println("sql listarCliente->"+sql);
//		
//		dataSet = db.get(dataSource, sql.toString());
//		System.out.println("dataSet-->"+dataSet.count());
//	}
	public void listarCliente(String idOperacion) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT NRO_CED_RIF,NOM_CLIEN,FECH_OPER,MTO_DIVISAS,CTA_CLIEN_DIVISAS,CTA_CLIEN,COD_DIVISAS,ID_OPER FROM INFI_TB_235_INTERVENCION");
		sql.append(" where ID_OPER='").append(idOperacion).append("'");
		System.out.println("sql listarCliente :" + sql);

		dataSet = db.get(dataSource, sql.toString());
		System.out.println("dataSet : " + dataSet.count());
	}
		
		public void listarClienteMenudeo(String idOperacion) throws Exception{
			StringBuffer sql= new StringBuffer();
			sql.append("select ID_OPER,ID_OC,OPERACION,STATUS_OPER,MTO_DIVISAS,MTO_BOLIVARES,TASA_CAMBIO,NACIONALIDAD,NRO_CED_RIF,NOM_CLIEN,CTA_CLIEN,FECH_OPER,CON_ESTADIS,COD_DIVISAS,EMAIL_CLIEN,TEL_CLIEN,MTO_DIVISAS_TRANS from INFI_TB_234_VC_DIVISAS");
			sql.append(" where ID_OPER='").append(idOperacion).append("'");
			System.out.println("sql listarClienteMENUDEO->"+sql);
			
			dataSet = db.get(dataSource, sql.toString());
			System.out.println(" ParametrosDAO : listarClienteMenudeo() "+dataSet.count());
		}
	
	
	public String listarParametrosReturn (String parametro, String grupo) throws Exception{
		String param=null;
		listarParametros(parametro, grupo);
		if (dataSet!=null&&dataSet.count()>0){
			dataSet.first();
			dataSet.next();
			param=dataSet.getValue("PARVAL_VALOR");
		}
		return param;
	}
	/**
	 * Actualiza el valor del parametro que coincida con el nombre del parametro
	 * @param grupoParametros
	 * @return
	 */
		public String modificarParametros(GrupoParametros grupoParametros){
		StringBuffer sql =new StringBuffer();
		sql.append("UPDATE INFI_TB_002_PARAM_TIPOS SET PARVAL_VALOR='").append(grupoParametros.getValorParametro()).append("'");
		sql.append(" WHERE PARTIP_NOMBRE_PARAMETRO='").append(grupoParametros.getNombreParametro()).append("'");
		sql.append(" and pargrp_id='").append(grupoParametros.getIdParametro()).append("'");
		return (sql.toString());
		}
		
			public String modificarCliente(ClienteIntervencion intervencionCliente){
			StringBuffer sql =new StringBuffer();
			sql.append("UPDATE INFI_TB_235_INTERVENCION SET COD_CLIENT='").append(intervencionCliente.getRifCliente()).append("'");
			sql.append(" ,NOMBRE_CLIENT='").append(intervencionCliente.getNombreCliente()).append("'");
			sql.append(" ,FECHA_VALOR='").append(intervencionCliente.getFechaValor()).append("'");
			sql.append(" ,COD_CUENTA_DIVISAS='").append(intervencionCliente.getCuentaDivisas()).append("'");
			sql.append(" ,COD_CUENTA_BS='").append(intervencionCliente.getCuentaBolivares()).append("'");
			sql.append(" ,MTO_DIVISAS='").append(intervencionCliente.getMontoDivisa()).append("'");
			sql.append(" WHERE ID_OPER='").append(intervencionCliente.getIdOperacion()).append("'");
			System.out.println("modificarCliente-->"+sql);
			return (sql.toString());
			}
			
			public String modificarClienteMenudeo(String idOperacion,String statusOperacion,String operacion,String nacionalidad,String rifOcedula,String nombreCliente,
					String fecha,String montoBs,String montoDivisas,String tasaCambio,String conceptoEstadistico,String codigoDivisas,String correoCliente,String telefonoCliente,String contraValorUsd){
				StringBuffer sql =new StringBuffer();
				sql.append("UPDATE INFI_TB_234_VC_DIVISAS SET OPERACION='").append(operacion).append("'");
				sql.append(" ,STATUS_OPER='").append(statusOperacion).append("'");
				sql.append(" ,NACIONALIDAD='").append(nacionalidad).append("'");
				sql.append(" ,NRO_CED_RIF=").append(rifOcedula).append("");
				sql.append(" ,NOM_CLIEN='").append(nombreCliente).append("'");
				sql.append(" ,FECH_OPER='").append(fecha).append("'");
				sql.append(" ,MTO_BOLIVARES=").append(montoBs).append("");
				sql.append(" ,MTO_DIVISAS=").append(montoDivisas).append("");
				sql.append(" ,TASA_CAMBIO=").append(tasaCambio).append("");
				sql.append(" ,CON_ESTADIS='").append(conceptoEstadistico).append("'");
				sql.append(" ,COD_DIVISAS='").append(codigoDivisas).append("'");
				sql.append(" ,EMAIL_CLIEN='").append(correoCliente).append("'");
				sql.append(" ,TEL_CLIEN='").append(telefonoCliente).append("'");
				sql.append(" ,MTO_DIVISAS_TRANS=").append(contraValorUsd).append("");
				sql.append(" WHERE ID_OC=").append(idOperacion).append("");
//				sql.append(" AND ID_OPER=").append(idOperacion).append("");
				System.out.println("modificarCliente-->"+sql);
				return (sql.toString());
				}
	/**
	 * Muestra todos los parametros asociados a un grupo específico
	 * @param proceso
	 * @return hashMap con los parámetros del grupo
	 * @throws Throwable
	 */
	public HashMap<String,String> buscarParametros(String transaccion, String... nombre) throws Exception { 
		
		HashMap<String, String> parametros = new HashMap<String, String>();
		//NM29643 - INFI_TTS_423: Se comenta consulta a BD duplicada
		/*StringBuffer sql = new StringBuffer();
		sql.append("select partip_nombre_parametro, parval_valor ");
		sql.append("from INFI_TB_001_PARAM_GRUPO pgrupo ");
		sql.append("inner join INFI_TB_002_PARAM_TIPOS ptipos on ptipos.pargrp_id = pgrupo.pargrp_id ");
		sql.append("where pgrupo.pargrp_nombre ='").append(transaccion).append("'"); 
		sql.append(" order by partip_nombre_parametro ");
		dataSet = db.get(dataSource, sql.toString());
		*/
		StringBuffer sqlSB = new StringBuffer();
		
		sqlSB.append("select partip_nombre_parametro, parval_valor, partip_valor_defecto ");
		sqlSB.append("from INFI_TB_001_PARAM_GRUPO pgrupo ");
		sqlSB.append("inner join INFI_TB_002_PARAM_TIPOS ptipos on ptipos.pargrp_id = pgrupo.pargrp_id ");
		sqlSB.append("where pgrupo.pargrp_nombre ='").append(transaccion).append("'"); 
		if(nombre.length>0 && nombre[0]!=null){ //Si se le pasaron nombres por parametro
			
			sqlSB.append(" and ptipos.partip_nombre_parametro IN (");
			for(int i=0; i<nombre.length; i++){
				sqlSB.append("'").append(nombre[i]).append("'");
				if(i<nombre.length-1) sqlSB.append(", ");
			}
			sqlSB.append(")");
			
		}
		// Manejo de las tablas de Moneda para los procesos de SUBASTA DICOM NM11383 28-03-2018  
		if (transaccion.equals("DICOM")){
			
			sqlSB.append(" UNION ");
			sqlSB.append("SELECT '0'||DIV_CDDIVBE MONNUM,"); // Código ISO númerico
			sqlSB.append("to_char(DIV_CDDIVISS) MONALF,");       // Código ISO alfanúmerico --
			sqlSB.append("to_char(DECODE (DIV_CDDIVISS, 'VEF', 1, 0)) AS MONEDA_IN_LOCAL ");
			sqlSB.append("FROM    REN_DIVISA ");
			sqlSB.append("WHERE DIV_INDCOTDI = 'S' ");
			sqlSB.append("order by 1 ");
		} else {
			sqlSB.append(" order by partip_nombre_parametro ");
			
		}
		
		dataSet = db.get(dataSource, sqlSB.toString());
		if(dataSet.count()>0){
			
			dataSet.first();
			while (dataSet.next()) {
				if(nombre.length>0 && nombre[0]!=null){ //Si se le pasaron nombres por parametro
					parametros.put(dataSet.getValue("partip_nombre_parametro"), dataSet.getValue("partip_valor_defecto")+";"+dataSet.getValue("parval_valor"));
				}else{
					parametros.put(dataSet.getValue("partip_nombre_parametro"), dataSet.getValue("parval_valor"));
				}
			}//fin while
			
		}//fin if
		//System.out.println("sqlSB--->"+sqlSB);
		System.out.println("buscarParametros "+sqlSB.toString());
		return parametros;
	}//fin metodo
	
	/**
	 * Muestra todos los grupo parametros por nombre
	 * @param proceso
	 * @return hashMap con los parámetros del grupo
	 * @throws Throwable
	 */
	//METODO AGREGARDO REQUERIMIENTO TTS_414 NM26659
	public void buscarGrupoParametro(String transaccion) throws Exception { 
						
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT   PARGRP_ID,PARGRP_NOMBRE,PARGRP_DESCRIPCION FROM infi_tb_001_param_grupo pgrupo ");		
		if(transaccion!=null && !transaccion.equals("")){
			sql.append("where pgrupo.pargrp_nombre ='").append(transaccion).append("' ");	
		}		
		
		dataSet = db.get(dataSource, sql.toString());				
	
	}//fin metodo
	
	/**
	 * Muestra todos los grupo parametros por nombre
	 * @param proceso
	 * @return hashMap con los parámetros del grupo
	 * @throws Throwable
	 */
	//METODO AGREGARDO REQUERIMIENTO TTS_414 NM26659
	public void buscarPorGrupoNombreParametro(String transaccion,String... nombreParam) throws Exception { 
						
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM INFI_TB_001_PARAM_GRUPO PGRUPO,INFI_TB_002_PARAM_TIPOS PARAMTIP WHERE PGRUPO.PARGRP_ID=PARAMTIP.PARGRP_ID ");		
		if(transaccion!=null && !transaccion.equals("")){
			sql.append(" AND PGRUPO.PARGRP_NOMBRE='").append(transaccion).append("' ");	
		}
		
		if(nombreParam.length>0 && nombreParam[0]!=null){
			sql.append(" AND UPPER(PARAMTIP.PARTIP_NOMBRE_PARAMETRO) IN (");			
			int count=0;
			
			for (String elem : nombreParam) {
				if(count>0){
					sql.append(",");
				}				
				sql.append("UPPER('"+elem+"') ");
				++count;
			}
			
			sql.append(") ");
			
		}
		//System.out.println("buscarPorGrupoNombreParametro ---> " + sql.toString());
		dataSet = db.get(dataSource, sql.toString());				
	
	}//fin metodo
	/**
	 * Actualiza el valor del parametro que coincida con el nombre del parametro
	 * @param grupoParametros
	 * @return
	 */
	//METODO AGREGARDO REQUERIMIENTO TTS_414 NM26659
	public String insertarParametro(GrupoParametros grupoParametros){
		
		StringBuffer sql =new StringBuffer();
		sql.append("INSERT INTO INFI_TB_002_PARAM_TIPOS (PARGRP_ID,PARTIP_NOMBRE_PARAMETRO,PARTIP_DESCRIPCION,PARTIP_VALOR_DEFECTO,PARVAL_VALOR) VALUES (");
		sql.append("'").append(grupoParametros.getIdParametro()).append("',");
		sql.append("'").append(grupoParametros.getNombreParametro()).append("',");
		sql.append("'").append(grupoParametros.getDescripcionParametro()).append("',");		
		
		sql.append(grupoParametros.getIdParametro()==null?null:"'"+grupoParametros.getValorDefectoParametro()+"',");
		sql.append(grupoParametros.getIdParametro()==null?null:"'"+grupoParametros.getValorParametro()+"'");
		
		sql.append(")");
			return (sql.toString());
		}
	
	/**
	 * Modifica el valor de un parametro espec&iacute;fico del sistema
	 * @param parametro
	 * @param nombreParametro
	 * @throws Exception
	 */
	//METODO CREADO EN REQUERIMIENTO TTS_414 NM26659
	public String modificar(GrupoParametros parametroGrupo) throws Exception{
		
		StringBuffer sql = new StringBuffer();		
		sql.append("UPDATE INFI_TB_002_PARAM_TIPOS PARAMTIP SET PARAMTIP.PARTIP_VALOR_DEFECTO='").append(parametroGrupo.getValorDefectoParametro()).append("',PARAMTIP.PARVAL_VALOR='").append(parametroGrupo.getValorParametro()).append("' ");
		sql.append("WHERE PARAMTIP.PARGRP_ID='").append(parametroGrupo.getIdParametro()).append("' AND PARAMTIP.PARTIP_NOMBRE_PARAMETRO='").append(parametroGrupo.getNombreParametro()).append("'");
		
		return sql.toString();
		//db.exec(dataSource, sql.toString());	
	}
	
	/**
	 * Elimina el registro del parametros dado su grupo id y nombre de parametro
	 * @param parametro
	 * @param nombreParametro
	 * @throws Exception
	 */
	//METODO CREADO EN REQUERIMIENTO TTS_414 NM26659
	public String eliminarParametro(GrupoParametros parametroGrupo) throws Exception{
		
		StringBuffer sql = new StringBuffer();		
		sql.append("DELETE INFI_TB_002_PARAM_TIPOS PARAMTIP ");
		sql.append("WHERE PARAMTIP.PARGRP_ID='").append(parametroGrupo.getIdParametro()).append("' AND PARAMTIP.PARTIP_NOMBRE_PARAMETRO='").append(parametroGrupo.getNombreParametro()).append("'");
		
		return sql.toString();
		//db.exec(dataSource, sql.toString());	
	}
}//fin clase

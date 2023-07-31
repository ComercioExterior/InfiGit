/**
 * 
 */
package com.bdv.infi.dao;

import javax.sql.DataSource;

import megasoft.db;

/**
 * @author Megasoft 
 *
 */
public class GrupoParametrosDAO extends com.bdv.infi.dao.GenericoDAO{
	public GrupoParametrosDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
	}
	public GrupoParametrosDAO(DataSource ds) {
		super(ds);
	}
	public Object moveNext() throws Exception {
		return null;
	}
	/**
	 * Lista los tipo de grupos que coincidan con el parametro enviado
	 * @param nombreParametro
	 * @throws Exception
	 */
public void listarGrupoParametros(String idGrupo) throws Exception{	
		
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT b.pargrp_id,a.pargrp_nombre,b.parval_valor,b.partip_descripcion," +
				" b.partip_nombre_parametro FROM INFI_TB_001_PARAM_GRUPO a, INFI_TB_002_PARAM_TIPOS b " +
				" WHERE a.pargrp_id=b.pargrp_id ");
				if(idGrupo!=null && !idGrupo.equals("")){
					sb.append(" and a.pargrp_id ='" + idGrupo + "'");	
				}
		sb.append(" ORDER BY PARTIP_NOMBRE_PARAMETRO");
		dataSet = db.get(dataSource, sb.toString());		
	}
/**
 * Lista el Parametro dependiendo del nombre del parametro que recibe
 * @param nombresParametros
 * @throws Exception
 */

public void listaParametros(String ... parametrosExcluir)throws Exception{
	StringBuffer sql =new StringBuffer();
	sql.append("SELECT * FROM INFI_TB_001_PARAM_GRUPO");
	
	if(parametrosExcluir.length>0){
		sql.append(" WHERE PARGRP_NOMBRE NOT IN ('");
		for (int element=0;element<parametrosExcluir.length;element++){
			if(element>0){
				sql.append("','");
			}
			sql.append(parametrosExcluir[element]);
		}
		sql.append("')");
	}	
	
	sql.append(" ORDER BY INFI_TB_001_PARAM_GRUPO.PARGRP_NOMBRE");
	dataSet =db.get(dataSource, sql.toString());
}
}

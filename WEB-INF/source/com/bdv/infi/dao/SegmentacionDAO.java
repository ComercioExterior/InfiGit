package com.bdv.infi.dao;

import java.util.ArrayList;

import javax.sql.DataSource;

import megasoft.db;

import com.bdv.infi.data.SegmentacionDefinicion;



public class SegmentacionDAO extends com.bdv.infi.dao.GenericoDAO {

	
	public SegmentacionDAO(DataSource _dso) throws Exception {
		super(_dso);
	}


	public void listar() throws Exception{	
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT CTESEG_ID, CTESEG_DESCRIPCION, CTESEG_ALTAIR_BANCO, CTESEG_ALTAIR_SEGMENTO, CTESEG_ALTAIR_SUBSEGMENTO FROM INFI_TB_015_CTES_SEGMENTOS");
		dataSet = db.get(dataSource, sb.toString());		
	}
	
	public void listar(String id_cteseg) throws Exception{

		StringBuffer sb = new StringBuffer();
		sb.append("SELECT CTESEG_ID, CTESEG_DESCRIPCION, CTESEG_ALTAIR_BANCO, CTESEG_ALTAIR_SEGMENTO, CTESEG_ALTAIR_SUBSEGMENTO FROM INFI_TB_015_CTES_SEGMENTOS WHERE CTESEG_ID ='").append(id_cteseg).append("'");
		dataSet = db.get(dataSource, sb.toString());
		
				
	}
	
	public void listarporfiltro(String cteseg_descripcion) throws Exception{

		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		sql.append("SELECT CTESEG_ID, CTESEG_DESCRIPCION, CTESEG_ALTAIR_BANCO, CTESEG_ALTAIR_SEGMENTO, CTESEG_ALTAIR_SUBSEGMENTO FROM INFI_TB_015_CTES_SEGMENTOS WHERE 1=1 ");
		
		if((cteseg_descripcion!=null) || (cteseg_descripcion!="")){
			filtro.append(" AND UPPER(CTESEG_DESCRIPCION) LIKE UPPER('%").append(cteseg_descripcion).append("%')");			
		}
		sql.append(filtro);
		sql.append(" ORDER BY CTESEG_ID");
		dataSet = db.get(dataSource, sql.toString());
		
				
	}			
	
	public String insertar(SegmentacionDefinicion segmentacionDefinicion) throws Exception  {
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
				
		sql.append("INSERT INTO INFI_TB_015_CTES_SEGMENTOS ( CTESEG_ID, CTESEG_DESCRIPCION, CTESEG_ALTAIR_BANCO, CTESEG_ALTAIR_SEGMENTO, CTESEG_ALTAIR_SUBSEGMENTO) VALUES (");
				
		filtro.append("'").append(segmentacionDefinicion.getCteseg_id()).append("',");
		filtro.append("'").append(segmentacionDefinicion.getCteseg_descripcion()).append("',");
		filtro.append("'").append(segmentacionDefinicion.getCteseg_altair_banco()).append("',");
		filtro.append("'").append(segmentacionDefinicion.getCteseg_altair_segmento()).append("',");
		filtro.append("'").append(segmentacionDefinicion.getCteseg_altair_subsegmento()).append("')");

		sql.append(filtro);		
		return(sql.toString());
	}

	public String modificar(SegmentacionDefinicion segmentacionDefinicion) throws Exception {
		
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
	    	
		
		sql.append("UPDATE INFI_TB_015_CTES_SEGMENTOS SET ");
		filtro.append(" CTESEG_DESCRIPCION='").append(segmentacionDefinicion.getCteseg_descripcion()).append("',");
		filtro.append(" CTESEG_ALTAIR_BANCO='").append(segmentacionDefinicion.getCteseg_altair_banco()).append("',");
		filtro.append(" CTESEG_ALTAIR_SEGMENTO='").append(segmentacionDefinicion.getCteseg_altair_segmento()).append("',");
		filtro.append(" CTESEG_ALTAIR_SUBSEGMENTO='").append(segmentacionDefinicion.getCteseg_altair_subsegmento()).append("'");
		filtro.append(" WHERE CTESEG_ID ='").append(segmentacionDefinicion.getCteseg_id()).append("'");
		sql.append(filtro);			
		return(sql.toString());				

	}


	public String eliminar(SegmentacionDefinicion segmentacionDefinicion) throws Exception {
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		
		sql.append("DELETE FROM INFI_TB_015_CTES_SEGMENTOS WHERE");
		
		filtro.append(" cteseg_id='").append(segmentacionDefinicion.getCteseg_id()).append("'");
		sql.append(filtro);			
		return(sql.toString());
	}
		
	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}

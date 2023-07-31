package com.bdv.infi.dao;

import javax.sql.DataSource;

import megasoft.db;

import com.bdv.infi.data.SegmentacionDefinicion;



public class TransaccionCampoDocDAO extends com.bdv.infi.dao.GenericoDAO {

	
	public TransaccionCampoDocDAO(DataSource _dso) throws Exception {
		super(_dso);
	}


	public void listarTodos() throws Exception{	
		
	}
	
	public void listar(String id_transa, String...filtro) throws Exception{

		StringBuffer sb = new StringBuffer();
		sb.append("SELECT TRANSA_ID, CAMPO_ID, DESCRIPCION_CAMPO, NOMBRE_CAMPO FROM INFI_TB_026_TRANSACCION_CAMDOC WHERE TRANSA_ID='").append(id_transa).append("'");
		//NM29643 Infi_TTS_466 Actualizacion proceso envio de correos
		if(filtro!=null && filtro.length>0){
			if(filtro[0].equals("cruce")){
				sb.append(" AND NOMBRE_CAMPO LIKE 'cruce_%'");
			}else{
				if(filtro[0].equals("sin_cruce")){
					sb.append(" AND NOMBRE_CAMPO NOT LIKE 'cruce_%'");
				}
			}
		}
		sb.append(" order by NOMBRE_CAMPO");
		dataSet = db.get(dataSource, sb.toString());
		
	}
			
	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}

package com.bdv.infi.dao;

import java.util.ArrayList;

import javax.sql.DataSource;

import megasoft.db;

import com.bdv.infi.webservices.beans.TCMGen1;


public class PaisDAO extends com.bdv.infi.dao.GenericoDAO {

	
	public PaisDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
		// TODO Auto-generated constructor stub
	}
	
	public PaisDAO(DataSource ds) throws Exception {
		super(ds);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**Inserción de la data de la tabla INFI_TB_905_PAIS
	 * @param idCliente cliente
	 * @throws Exception
	 */	
	public ArrayList<String> insertarPaises(ArrayList<TCMGen1> paises) throws Exception{		
		ArrayList<String> sentencias= new ArrayList<String>();
		String descPais="";
		
		for (TCMGen1 pais : paises){		
			
			//System.out.println("pais.getDatos1TablGeneral(): '"+pais.getDatos1TablGeneral()+"'");
			//System.out.println("pais.getClave(): '"+pais.getClave()+"'");
			
			if(pais.getDatos1TablGeneral()!=null&&pais.getDatos1TablGeneral().length()>0&&pais.getClave()!=null&&pais.getClave().trim().length()>0){
				descPais=pais.getDatos1TablGeneral().substring(0,40).trim().replace("'", "''");
				sentencias.add("INSERT INTO INFI_TB_905_PAIS (COD_PAIS,DESC_PAIS) VALUES ('"+pais.getClave().trim()+"','"+descPais+"')");
			}
		}
		return sentencias;	
	}
	
	/**Elimina la data de la tabla INFI_TB_905_PAIS
	 * @param idCliente cliente
	 * @throws Exception
	 */	
	public String deletePaises() throws Exception{		
		return "DELETE FROM INFI_TB_905_PAIS";	
	}
	
	/**Consulta los paises
	 * @throws Exception
	 */		
	public void consultarPaises (String... idPais)throws Exception{		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COD_PAIS,DESC_PAIS FROM INFI_TB_905_PAIS");
		if(idPais.length>0 && idPais[0]!=null){
			sql.append(" WHERE COD_PAIS='"+idPais+"'");
		}
		sql.append(" ORDER BY DESC_PAIS");
		dataSet = db.get(dataSource, sql.toString());
	
	}
	
	/**
	 * Lista la información del país en la tabla 905 por descripción exacta
	 * @param descripcion
	 * @throws Exception
	 */
	public void listarPais(String descripcion) throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT * FROM  INFI_TB_905_PAIS");
		if(descripcion!=null && !descripcion.equals("")){
			sql.append(" WHERE UPPER (DESC_PAIS) = UPPER('").append(descripcion).append("')");			
		}
		dataSet =db.get(dataSource, sql.toString());	
	}
	
	/**METODO PROVISIONAL: se usa la tabla INFI_TB_015_CTES_SEGMENTOS por motivos de tiempo
	 * Inserción de la data de la tabla INFI_TB_015_CTES_SEGMENTOS
	 * @param idCliente cliente
	 * @throws Exception
	 */
	public ArrayList<String> insertarPaisesProvisional(ArrayList<TCMGen1> paises) throws Exception{		
		ArrayList<String> sentencias= new ArrayList<String>();
		String descPais="";
		
		for (TCMGen1 pais : paises){		
			descPais=pais.getDatos1TablGeneral().substring(0,40).trim().replace("'", "''");
			
			if(pais.getDatos1TablGeneral()!=null&&pais.getDatos1TablGeneral().length()>0&&pais.getClave()!=null&&pais.getClave().length()>0){
				sentencias.add("INSERT INTO INFI_TB_015_CTES_SEGMENTOS (CTESEG_ID,CTESEG_DESCRIPCION,CTESEG_ALTAIR_BANCO,CTESEG_ALTAIR_SEGMENTO,CTESEG_ALTAIR_SUBSEGMENTO) VALUES ('"+pais.getClave().trim()+"','"+descPais+"',' ',' ',' ')");
			}
		}
		return sentencias;	
	}
	
	/**METODO PROVISIONAL: se usa la tabla INFI_TB_015_CTES_SEGMENTOS por motivos de tiempo
	 * *Elimina la data de la tabla INFI_TB_015_CTES_SEGMENTOS
	 * @param idCliente cliente
	 * @throws Exception
	 */	
	public String deletePaisesProvisional() throws Exception{		
		return "DELETE FROM INFI_TB_015_CTES_SEGMENTOS";	
	}
	
	/**METODO PROVISIONAL: se usa la tabla INFI_TB_015_CTES_SEGMENTOS por motivos de tiempo
	 * *Consulta los paises
	 * @throws Exception
	 */		
	public void consultarPaisesProvisional (String idPais)throws Exception{		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT CTESEG_ID,CTESEG_DESCRIPCION FROM INFI_TB_015_CTES_SEGMENTOS WHERE CTESEG_ID='"+idPais+"' ORDER BY CTESEG_DESCRIPCION");
		dataSet = db.get(dataSource, sql.toString());
	
	}	
	
}

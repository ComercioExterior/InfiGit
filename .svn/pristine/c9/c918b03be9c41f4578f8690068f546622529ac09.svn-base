package com.bdv.infi.dao;

import java.util.ArrayList;

import javax.sql.DataSource;

import megasoft.db;

import com.bdv.infi.webservices.beans.TCMGen1;


public class CiudadDAO extends com.bdv.infi.dao.GenericoDAO {

	
	public CiudadDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
		// TODO Auto-generated constructor stub
	}
	
	public CiudadDAO(DataSource ds) throws Exception {
		super(ds);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public ArrayList<String> insertarCiudades(ArrayList<TCMGen1> ciudades) throws Exception{		
		ArrayList<String> sentencias= new ArrayList<String>();
		String idCiudad,idEstado;
		
		for (TCMGen1 estado : ciudades){			
			idEstado=estado.getClave().substring(0,2).trim();
			idCiudad=estado.getClave().trim();
			System.out.println("idEstado="+idEstado+",idCiudad="+idCiudad);
			System.out.println(estado.getDatos1TablGeneral().substring(0,40).trim());
			if(idEstado!=null&&idEstado.length()>0&&idCiudad!=null&&idCiudad.length()>0){
				sentencias.add("INSERT INTO INFI_TB_CIUDAD (COD_CIUDAD,DESC_CIUDAD,COD_ESTADO) VALUES ('"+idCiudad+"','"+estado.getDatos1TablGeneral().substring(0,40).trim()+"','"+idEstado+"')");
			}
		}
		return sentencias;	
	}
	
	/**Elimina la data de la tabla infi_tb_ciudades
	 * @param idCliente cliente
	 * @throws Exception
	 */	
	public String deleteCiudades() throws Exception{		
		return "DELETE FROM INFI_TB_CIUDAD";	
	}
	
	/**Consulta los estados
	 * @throws Exception
	 */		
	public void consultarCiudades (String idEstado)throws Exception{		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COD_CIUDAD,DESC_CIUDAD,COD_ESTADO FROM INFI_TB_CIUDAD WHERE COD_ESTADO='"+idEstado+"' ORDER BY DESC_CIUDAD");
		dataSet = db.get(dataSource, sql.toString());
	
	}
	
		
	
}

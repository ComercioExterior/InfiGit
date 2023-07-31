/**
 * 
 */
package com.bdv.infi.dao;
import javax.sql.DataSource;

import megasoft.db;

import com.bdv.infi.data.CampoDinamico;
/**
 * @author eel
 *
 */
public class ListasDinamicas extends GenericoDAO{
	
	public ListasDinamicas(DataSource ds) {
		super(ds);
		// TODO Auto-generated constructor stub
	}
	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}	
	
	/**
	 * Metodo de insercion de lista dinamica
	 * */
	public String insertar(String idCampoDinamico,String idItem,String valorCampo){
		StringBuffer sql=new StringBuffer();
		
		sql.append("INSERT INTO INFI_TB_037_LISTAS_DINAMICAS (CAMPO_DINAMICO_ID,ID_ITEM,DESCRIPCION)VALUES (");
		sql.append(idCampoDinamico);
		sql.append(",'");
		sql.append(idItem);
		sql.append("','");
		sql.append(valorCampo);
		sql.append("')");
		return  sql.toString();
	}
	
	/**Busca de Listas Dinamicas por el id del campo dinamicos 
	 * @param Id de campo dinamica*/
	public void listarPorId(String idCampoDinamico) throws Exception {
		StringBuffer sb = new StringBuffer();
		//sb.append("select campo_id, campo_nombre, campo_descripcion, campo_tipo, DECODE(campo_tipo, '1', 'General', 'Venta') as desc_campo_tipo from INFI_TB_036_CAMPOS_DINAMICOS order by campo_nombre");
		
		sb.append("SELECT * FROM INFI_TB_037_LISTAS_DINAMICAS LD ");
		if(idCampoDinamico!=null && !idCampoDinamico.equals("")){
			sb.append("WHERE LD.CAMPO_DINAMICO_ID=").append("'"+idCampoDinamico+"'");
		}		
		System.out.println("listarPorId -------> " + sb.toString());
		dataSet = db.get(dataSource, sb.toString());
		
	}
	
	
	/**
	 * Elimina el registro en la tabla. 
	*/
	public String eliminar(CampoDinamico campoDinamico){
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		sql.append("delete from INFI_TB_037_LISTAS_DINAMICAS where");
		
		filtro.append(" CAMPO_DINAMICO_ID=").append(campoDinamico.getIdCampo());
		sql.append(filtro);			
		return(sql.toString());
	}
	

	
	
}//fin de la clase

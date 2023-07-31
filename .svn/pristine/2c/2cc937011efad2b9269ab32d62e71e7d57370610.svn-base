
package com.bdv.infi.dao;

import javax.sql.DataSource;
import com.bdv.infi.data.EquivalenciaPortafolio;
import megasoft.db;

/**Clase en la que se maneja los registros de la tabla INFI_TB_047_EQUIV_PORTAFOLIO
 * @author Megasoft Computacion
 *
 */
public class EquivalenciaPortafolioDAO extends com.bdv.infi.dao.GenericoDAO{
	
	public EquivalenciaPortafolioDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
		// TODO Auto-generated constructor stub
	}
	public EquivalenciaPortafolioDAO(DataSource ds) {
		super(ds);
		// TODO Auto-generated constructor stub
	}
	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**Lista las equivalencia de portafolio que coincidan con parte del nombre.Crea un dataSet con los registros encontrados
	 * @param nombre parte del nombre de la equivalencia de portafolio que se desea buscar
	 * @throws excepcion en caso de error*/
	public void listar(String nombre) throws Exception{	
		
		StringBuffer sb = new StringBuffer();
		StringBuffer filtro= new StringBuffer("");
		
		sb.append("SELECT * FROM INFI_TB_047_EQUIV_PORTAFOLIO");
		if(nombre!=null && !nombre.equals(""))
			filtro.append(" where upper (INFI_TB_047_EQUIV_PORTAFOLIO.SEGMENTO_DESCRIPCION)like upper('%").append(nombre).append("%')");
		sb.append(filtro);		
		dataSet = db.get(dataSource, sb.toString());		
	}
	

	/**
	 * Inserta el registro en la tabla INFI_TB_047_EQUIV_PORTAFOLIO 
	 */
	public String insertar(EquivalenciaPortafolio equivalenciaPortafolio) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO INFI_TB_047_EQUIV_PORTAFOLIO (SEGMENTO_ID,SEGMENTO_DESCRIPCION,PORTAFOLIO,USUARIO_ID,FECHA_ULT_ACT) VALUES (");
		sql.append("'").append(equivalenciaPortafolio.getIdSegmento()).append("',");
		sql.append("'").append(equivalenciaPortafolio.getDescripcionSegmento()).append("',");
		sql.append("'").append(equivalenciaPortafolio.getPortafolio()).append("',");
		sql.append(equivalenciaPortafolio.getIdUsuario()).append(",");
		sql.append(this.formatearFechaBD(equivalenciaPortafolio.getFechaActualizacion())).append(")");
		return(sql.toString());
		
	}
	
	/**
	 * Modifica el registro de una tabla INFI_TB_047_EQUIV_PORTAFOLIO
	*/
	public String modificar(EquivalenciaPortafolio equivalenciaPortafolio){
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE INFI_TB_047_EQUIV_PORTAFOLIO  set ");
		sql.append("SEGMENTO_ID='").append(equivalenciaPortafolio.getIdSegmento()).append("',");
		sql.append("SEGMENTO_DESCRIPCION='").append(equivalenciaPortafolio.getDescripcionSegmento()).append("',");
		sql.append("PORTAFOLIO='").append(equivalenciaPortafolio.getPortafolio()).append("',");
		sql.append("usuario_id=").append(equivalenciaPortafolio.getIdUsuario()).append(",");
		sql.append("fecha_ult_act=").append(formatearFechaBD(equivalenciaPortafolio.getFechaActualizacion()));
		sql.append(" where segmento_id='").append(equivalenciaPortafolio.getIdSegmentoAnterior()).append("'");
		return(sql.toString());
	}
	
	/**
	 * Elimina el registro en la tabla INFI_TB_047_EQUIV_PORTAFOLIO. 
	*/
	public String eliminar(EquivalenciaPortafolio equivalenciaPortafolio){
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM INFI_TB_047_EQUIV_PORTAFOLIO  WHERE");
		sql.append(" SEGMENTO_ID='").append(equivalenciaPortafolio.getIdSegmento()).append("'");		
		return(sql.toString());
	}


	/**
	 * Lista la Equivalencia de Portafolio encontrada según el id recibido. Genera un dataSet
	 * @param idSegmento id del segmento que debe buscar
	 * @throws Lanza una Exception en caso de error
	 */
	public void listarEquivalenciaPortafolio(String idSegmento) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT SEGMENTO_ID,SEGMENTO_DESCRIPCION,PORTAFOLIO,USUARIO_ID,FECHA_ULT_ACT");
		sql.append(" FROM INFI_TB_047_EQUIV_PORTAFOLIO where SEGMENTO_ID='").append(idSegmento).append("'");
		dataSet = db.get(dataSource, sql.toString());
	}	

}

package com.bdv.infi.dao;

import java.util.List;

import javax.sql.DataSource;

import megasoft.db;

import com.bdv.infi.data.OrdenRequisito;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

/**
 * Clase encargada de la persistencia de los requisitos asociados a una orden
 */
public class OrdenRequisitoDAO extends GenericoDAO {
	
	public OrdenRequisitoDAO(DataSource ds) {
		super(ds);
	}

	/**
	 * Inserta nuevos requisitos en la base de datos asociados a una orden
	 * @param requisitos lista de requisitos
	 * @return Array de consultas sql que se deben ejecutar
	 * @throws Exception en caso de error
	 */
	public String[] insertar(List<OrdenRequisito> requisitos) throws Exception {
		int secuencia;
		String[] consultas = new String[requisitos.size()]; 
		StringBuilder sql = new StringBuilder("INSERT ITNO INFI_TB_210_ORDENES_REQUISITO (ORDENE_REQUISITO_ID,ORDENE_ID,FECHA_RECEPCION,RECEPCION_USUARIO_NM,INDICA_ID) " +
		" VALUES(");
		int i=0;
		String fechaRecepcion=null;
		
		for (OrdenRequisito ordenRequisito : requisitos) {
			fechaRecepcion = null;
			secuencia = Integer.valueOf(dbGetSequence(this.dataSource, ConstantesGenerales.SECUENCIA_ORDENES_REQUISITO));
			ordenRequisito.setId(secuencia);
			sql.append(ordenRequisito.getId()).append(",").append(ordenRequisito.getOrdeneId()).append(",");			
			if (ordenRequisito.getFechaRecepcion() != null){
				fechaRecepcion = this.formatearFechaBD(ordenRequisito.getFechaRecepcion());
			}
			sql.append(fechaRecepcion).append(",'").append(ordenRequisito.getUsuarioRecepcion()).append("','").append(ordenRequisito.getIndicaId()).append("')");
			consultas[i++] = sql.toString();
		}
		return consultas;
	}
	
	/**
	 * Actualiza los requisitos asociados a una orden
	 * @param requisitos una colección de objetos OrdenRequisito que se deben actualizar
	 * @return un array de consultas que se deben actualizar
	 */
    public String[] actualizar(List<OrdenRequisito> requisitos) {
    	String[] consultas = new String[requisitos.size()];
    	int i=0;
    	for (OrdenRequisito ordenRequisito : requisitos) {
    		consultas[i++] = "update INFI_TB_210_ORDENES_REQUISITO set FECHA_RECEPCION=sysdate,RECEPCION_USUARIO_NM='" + 
    		ordenRequisito.getUsuarioRecepcion() + "' where ORDENE_REQUISITO_ID=" + ordenRequisito.getId() + " and ordene_id=" + ordenRequisito.getOrdeneId(); 
    	}
    	return consultas;
    }
	
	/**
	 * Busca los requisitos asociados a una orden
	 * @param ordenId id de la orden que se desea consultar
	 * @throws Exception en caso de error
	 */
	public void listarRequisitosPorOrdenEntregados(int ordenId) throws Exception {
		String sql = "SELECT A.ORDENE_REQUISITO_ID,A.ORDENE_ID,A.FECHA_RECEPCION,A.RECEPCION_USUARIO_NM,A.INDICA_ID,B.INDICA_DESCRIPCION " + 
                     " FROM INFI_TB_210_ORDENES_REQUISITO A, INFI_TB_011_INDICADORES B WHERE A.INDICA_ID = B.INDICA_ID AND FECHA_RECEPCION IS NOT NULL AND A.ORDENE_ID=" + ordenId;
		dataSet = db.get(dataSource, sql);
	}
	
	/**
	 * Lista los requisitos pendientes para la orden
	 * @param ordenId id de la orden que se desea consultar
	 * @throws Exception en caso de error
	 */
	public void listarRequisitosPorOrdenPendientes(int ordenId) throws Exception {
		String sql = "SELECT A.ORDENE_REQUISITO_ID,A.ORDENE_ID,A.FECHA_RECEPCION,A.RECEPCION_USUARIO_NM,A.INDICA_ID,B.INDICA_DESCRIPCION " + 
        " FROM INFI_TB_210_ORDENES_REQUISITO A, INFI_TB_011_INDICADORES B WHERE A.INDICA_ID = B.INDICA_ID AND FECHA_RECEPCION IS NULL AND A.ORDENE_ID=" + ordenId;
        dataSet = db.get(dataSource, sql);
	}	
	
	/**Lista las observaciones de la orden*/
	public void listarObservaciones(int ordenId) throws Exception {
		String sql = "SELECT ORDENE_OBSERVACION " + 
        " FROM INFI_TB_204_ORDENES WHERE ORDENE_ID=" + ordenId;
        dataSet = db.get(dataSource, sql);
	}	
	
//	public List<OrdenRequisito> obtenerRequisitos(int ordenId) {
//	
//	}
//	
//	public String[] actualizar(List<OrdenRequisito> requisitos) {
//	
//	}

	@Override
	public Object moveNext() throws Exception {
		return null;
	}
}

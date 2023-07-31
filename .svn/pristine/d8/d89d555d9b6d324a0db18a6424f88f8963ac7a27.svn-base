package com.bdv.infi.dao;

import java.util.Date;

import javax.sql.DataSource;

import megasoft.DataSet;
import megasoft.db;

public class OrdenTaquillaDAO extends com.bdv.infi.dao.GenericoDAO {

	public OrdenTaquillaDAO(DataSource ds) {
		super(ds);
	}

	@Override
	public Object moveNext() throws Exception {
		return null;
	}
	
	/**
	 * Lista todas las ordenes de taquilla registradas
	 * 
	 * @param idCliente id del cliente. -1 o 0 Cuando se desean todos	  
	 * @param idTitulo id del t&iacute;tulo involucrado en una orden. 
	 * @param idTransaccion id de la transacci&oacute;n espec&iacute;fica. Si es  
	 * @param fechaDesde Fecha de inicio de la creaci&oacute;n de la orden
	 * @param fechaHasta Fecha de fin de la creaci&oacute;n de la orden
	 * @param desplegarOperaciones Indica si se deben buscar las transacciones financieras asociadas a las ordenes
	 * @throws Exception 
	 * */	
	@SuppressWarnings("deprecation")
	public void listarOrdenesTaquilla(long idCliente, Date fechaDesde, Date fechaHasta, String estatus) throws Exception{
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		
		sql.append("SELECT * FROM INFI_TB_232_ORDENES_TAQUILLA TAQ WHERE ");
		filtro.append(" TAQ.ORDENE_TAQ_CLIENT_ID = ").append(idCliente);
		
		if(fechaDesde!=null){
			filtro.append(" AND TAQ.ORDENE_TAQ_FE_REGISTRO >= to_date('").append(fechaDesde.getDate()).append("/").append(fechaDesde.getMonth()+1).append("/").append(fechaDesde.getYear()+1900).append("',").append("'dd/mm/yyyy'").append(")");
		}
		
		if(fechaHasta!=null){
			filtro.append(" AND TAQ.ORDENE_TAQ_FE_REGISTRO <= to_date('").append(fechaHasta.getDate()).append("/").append(fechaHasta.getMonth()+1).append("/").append(fechaHasta.getYear()+1900).append("',").append("'dd/mm/yyyy'").append(")");
		}
			
		if(estatus!= null){
			filtro.append(" AND TAQ.ORDENE_TAQ_ESTATUS IN (").append(estatus).append(")");
		}
		sql.append(filtro);
		
		dataSet = db.get(dataSource, sql.toString());
	}
	
	/** Devuelve el dataset */
	public DataSet getDataSet() {
		return dataSet;
	}
}

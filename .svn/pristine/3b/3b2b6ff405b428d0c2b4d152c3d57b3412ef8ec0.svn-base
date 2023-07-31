package com.bdv.infi.dao;

import javax.sql.DataSource;

import com.bdv.infi.data.IndicadoresDefinicion;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

import megasoft.DataSet;
import megasoft.db;

/** 
 * Clase para buscar, agregar, modificar y eliminar los indicadores registrados en la base de datos;
 */
public class IndicadoresDAO extends com.bdv.infi.dao.GenericoDAO {

	  
	public IndicadoresDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
		// TODO Auto-generated constructor stub
	}
	
	
	public IndicadoresDAO(DataSource ds) {
		super(ds);
		// TODO Auto-generated constructor stub
	}



	
	/**
	 * Busca los indicadores en la base de datos por el id recibido.  
	 * @throws Exception 
	*/
	public void listar(String indica_id) throws Exception{

		StringBuffer sb = new StringBuffer();
		sb.append("SELECT INDICA_ID, INDICA_DESCRIPCION, INDICA_IN_REQUERIDO,INDICA_IN_REQUISITO FROM INFI_TB_011_INDICADORES WHERE INDICA_ID ='").append(indica_id).append("'");
		dataSet = db.get(dataSource, sb.toString());
		
				
	}
	
	public void listarporfiltro(String indica_descripcion) throws Exception{

		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		sql.append("SELECT INDICA_ID, INDICA_DESCRIPCION, CASE WHEN INDICA_IN_REQUERIDO=1 THEN 'Si' WHEN INDICA_IN_REQUERIDO=0 THEN 'No' END INDICA_IN_REQUERIDO, CASE WHEN INDICA_IN_REQUISITO=1 THEN 'Si' WHEN INDICA_IN_REQUISITO=0 THEN 'No' END INDICA_IN_REQUISITO FROM INFI_TB_011_INDICADORES WHERE 1=1 ");
		
		if((indica_descripcion!=null) && (indica_descripcion!="")){
			filtro.append(" AND UPPER(INDICA_DESCRIPCION) LIKE UPPER('%").append(indica_descripcion).append("%')");			
		}
		sql.append(filtro);
		sql.append(" ORDER BY INDICA_ID");
		dataSet = db.get(dataSource, sql.toString());
		
				
	}		
	
	public String insertar(IndicadoresDefinicion indicadoresDefinicion) throws Exception  {
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		
		sql.append("INSERT INTO INFI_TB_011_INDICADORES (INDICA_ID, INDICA_DESCRIPCION, INDICA_IN_REQUERIDO,INDICA_IN_REQUISITO) VALUES (");
		String indica_id = dbGetSequence(dataSource, ConstantesGenerales.SECUENCIA_INDICADORES);
				
		filtro.append("'").append(indica_id).append("',");
		filtro.append("'").append(indicadoresDefinicion.getIndica_descripcion().toUpperCase()).append("',");
		filtro.append("'").append(indicadoresDefinicion.getIndica_in_requerido()).append("',");
		filtro.append("").append(indicadoresDefinicion.getIndica_in_requisito()).append(")");

		sql.append(filtro);		
		return(sql.toString());
	}

	public String modificar(IndicadoresDefinicion indicadoresDefinicion) throws Exception {
		
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		
		sql.append("UPDATE INFI_TB_011_INDICADORES SET ");
		filtro.append(" INDICA_DESCRIPCION='").append(indicadoresDefinicion.getIndica_descripcion().toUpperCase()).append("',");
		filtro.append(" INDICA_IN_REQUERIDO=").append(indicadoresDefinicion.getIndica_in_requerido()).append(",");
		filtro.append(" INDICA_IN_REQUISITO=").append(indicadoresDefinicion.getIndica_in_requisito()).append("");

		filtro.append(" WHERE INDICA_ID ='").append(indicadoresDefinicion.getIndica_id()).append("'");
		sql.append(filtro);			
		return(sql.toString());				

	}


	public String eliminar(IndicadoresDefinicion indicadoresDefinicion) throws Exception {
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		
		sql.append("DELETE FROM INFI_TB_011_INDICADORES WHERE");
		
		filtro.append(" INDICA_ID='").append(indicadoresDefinicion.getIndica_id()).append("'");
		sql.append(filtro);			
		return(sql.toString());
	}


	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	  
	public void buscarIndicadorenUI(String indica_id) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT INDICA_ID FROM INFI_TB_110_UI_INDICADORES WHERE INDICA_ID ='").append(indica_id).append("'");
	
		dataSet = db.get(dataSource, sql.toString());

	}
	
	/**Metodo que verifica si existe un registro con el mismo descripcionindicador en base de datos
	*  @param String valorCampo
	*  @throws Exception lanza una excepci&ooacute;n si hay un error en la operaci&oacute;n
	*/
	public  boolean verificarIndicadorDescripcionExiste(String valorCampo) throws Exception{
		boolean encontro=false;
		StringBuffer sb=new StringBuffer();
		sb.append("select * from ");
		sb.append("INFI_TB_011_INDICADORES");
		sb.append(" where initCap(");
		sb.append("INDICA_DESCRIPCION");
		sb.append(") = initCap('");
		sb.append(valorCampo);
		sb.append("')");

		DataSet ds=db.get(dataSource,sb.toString());
		if (ds.count()>0)
			encontro=true;
		
		return encontro;
	}

}   

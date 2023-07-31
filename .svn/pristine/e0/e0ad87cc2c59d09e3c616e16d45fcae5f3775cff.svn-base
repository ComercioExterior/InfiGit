package com.bdv.infi.dao;

import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.bdv.infi.logic.interfaces.TransaccionNegocio;

import megasoft.db;

/** 
 * Clase para buscar las transacciones registradas en la base de datos;
 * Cuando se habla de una transacci&oacute;n se refiere a Toma de orden, liquidaci&oacute;n, pagos, entre otras;
 * Cada una de estas transacciones tienen documentos y funciones asociadas
 */
public class TransaccionDAO extends com.bdv.infi.dao.GenericoDAO {

	
	public TransaccionDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
		// TODO Auto-generated constructor stub
	}
	
	

	
	public TransaccionDAO(DataSource ds) {
		super(ds);
		// TODO Auto-generated constructor stub
	}

	  
	    public static DataSource getDataSource(String dsName) throws Exception
	    {

	        DataSource source;

	        try
	        {
	        
				if (dsName==null)  throw new Exception("DataSource's Name NULL; Please the for datasource configuration parameters.");
	        
	            Context ctx = new InitialContext();
	        	source = (DataSource) ctx.lookup( dsName );
	            if (null == source)
	            {
	                throw new Exception("JDBC DataSource not found; JNDI returned NULL");
	            }
	        }
	        catch ( Exception e )
	        {
	    	   throw new Exception(e.getMessage() + " [" + dsName + "]");
	        }

	        return source;

	    }


	/**
	 * Busca todas las transacciones registradas en la base de datos. 
	 * @throws Exception 
	*/
	public void listar(String grupoTransaccion) throws Exception{	
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT tr.* FROM INFI_TB_012_TRANSACCIONES tr ");
		
		if(grupoTransaccion!=null){
			sb.append(" inner join INFI_TB_003_GRP_TRANSACCIONES grup on (grup.GRUPO_ID = tr.GRUPO_ID " );
			sb.append(" and grup.DESC_GRUPO = '").append(grupoTransaccion).append("')");
		}		
		
		sb.append(" WHERE tr.TRANSA_IN_MOSTRAR = 1 ");
		
		sb.append(" order by tr.transa_descripcion ");	
	
		dataSet = db.get(dataSource, sb.toString());
	}
		
	/**
	 * Busca las transacci&oacute;n en la base de datos por el id recibido. Carga las funciones asociadas a la misma, los documentos y los campos. 
	 * @throws Exception 
	*/
	public boolean listarPorId(String idTransaccion) throws Exception{
		boolean bolOk = false;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM INFI_TB_012_TRANSACCIONES WHERE TRANSA_ID='").append(idTransaccion).append("'");
		try {
			conn = dataSource.getConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql.toString());
		} catch (Exception e) {
			throw new Exception("Error en la b&uacute;squeda del cliente por su id " + e.getMessage());
		}

		if (resultSet.next()) {
			bolOk = true;
		}
		return bolOk;
	}	
	
	public int insertar(Object objeto) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int modificar(Object objeto) {
		// TODO Auto-generated method stub
		return 0;
	}


	/**Recorre la consulta y devuelve un objeto tipo Transaccion*/
	public Object moveNext() throws Exception {
		boolean bolPaso = false;
        com.bdv.infi.data.Transaccion tran = new com.bdv.infi.data.Transaccion();
        try {
            //Si no es ultimo registro arma el objeto            
            if ((resultSet!=null)&&(!resultSet.isAfterLast())){            	
                bolPaso = true;
                tran.setIdTransaccion(resultSet.getString("TRANSA_ID"));
                tran.setDescripcion(resultSet.getString("TRANSA_DESCRIPCION"));
                tran.setFuncionAsociada(resultSet.getString("TRANSA_DESC_FUNCION"));
                resultSet.next();
            }
        } catch (SQLException e) {
            super.closeResources();
            throw new Exception("Error al intentar crear el objeto transacci&oacute;n ");
        }
        if (bolPaso) {
            return tran;
        } else {
            super.closeResources();
            return null;
        }
	}
	
	/**
	 * Lista las transaciones que se despliegarán en el combo para la consulta de cupones o comisiones de custodia
	 * @throws Exception
	 */
	public void listarTransaccion()throws Exception
	{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM INFI_TB_012_TRANSACCIONES WHERE TRANSA_ID IN ").append("('").append(TransaccionNegocio.CUSTODIA_COMISIONES).append("'").append(',').append("'").append(TransaccionNegocio.PAGO_CUPON).append("'").append(")");
		dataSet = db.get(dataSource, sql.toString());
	}
	
	/**Busca los documentos asociados a la transacci&oacute;n */
	private void buscarDocumentos(){
		
	}
	/**
	 * Lista todas las transacciones existentes en 012
	 * @throws Exception
	 */
	public void listarTransacciones()throws Exception{
		
		StringBuffer sql = new StringBuffer();
		sql.append("select * from INFI_TB_012_TRANSACCIONES  left join infi_tb_008_ctas_bic_transa on INFI_TB_012_TRANSACCIONES.transa_id=infi_tb_008_ctas_bic_transa.transa_id left join infi_tb_007_ctas_bic_banco on infi_tb_008_ctas_bic_transa.id_cuenta=infi_tb_007_ctas_bic_banco.id_cuenta ");
		sql.append("where INFI_TB_012_TRANSACCIONES.transa_id in('");
		sql.append(TransaccionNegocio.VENTA_TITULOS).append("','");
		sql.append(TransaccionNegocio.PACTO_RECOMPRA).append("','");
		sql.append(TransaccionNegocio.PAGO_CUPON).append("','");
		sql.append(TransaccionNegocio.CUSTODIA_COMISIONES).append("','");
		sql.append(TransaccionNegocio.ORDEN_PAGO).append("') ");
		sql.append("order by INFI_TB_012_TRANSACCIONES.transa_id");

		dataSet = db.get(dataSource,sql.toString());
	}
	/**
	 * Metodo que lista el filtro para las transacciones liquidadas
	 * @param grupoTransaccion
	 * @throws Exception
	 */
	public void listarFiltroTransaccionesLiquidadas(String grupoTransaccion) throws Exception{	
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT tr.transa_id,tr.transa_descripcion FROM INFI_TB_012_TRANSACCIONES tr  WHERE ");
		sb.append("tr.TRANSA_ID in ('");
		sb.append(TransaccionNegocio.SALIDA_EXTERNA).append("','").append(TransaccionNegocio.SALIDA_INTERNA).append("','");
		sb.append(TransaccionNegocio.ENTRADA_DE_TITULOS).append("','").append(TransaccionNegocio.PACTO_RECOMPRA);
		sb.append("') order by tr.transa_descripcion ");

		dataSet = db.get(dataSource, sb.toString());
	}
	
	public void listarTransaccionesPorId(String ... IdTransacciones)throws Exception {
		
		StringBuffer sql=new StringBuffer();
		
		sql.append("SELECT TR.TRANSA_ID AS transaid,transa_descripcion FROM INFI_TB_012_TRANSACCIONES TR ");
		
		if(IdTransacciones.length>0){
			sql.append(" WHERE TR.TRANSA_ID IN ('");
			for (int element=0;element<IdTransacciones.length;element++){
				if(element>0){
					sql.append("','");
				}
				sql.append(IdTransacciones[element]);
			}
			sql.append("')");
		}
		//System.out.println(sql);
		dataSet = db.get(dataSource, sql.toString());
	}
	
}

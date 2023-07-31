package com.bdv.infi_toma_orden.dao;

import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.bdv.infi.util.DB;


/** 
 * Clase para buscar las transacciones registradas en la base de datos;
 * Cuando se habla de una transacci&oacute;n se refiere a Toma de orden, liquidaci&oacute;n, pagos, entre otras;
 * Cada una de estas transacciones tienen documentos y funciones asociadas
 */
public class TransaccionDAO extends com.bdv.infi_toma_orden.dao.GenericoDAO {

	
	/*public TransaccionDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
		// TODO Auto-generated constructor stub
	}*/
	
	
	public TransaccionDAO (String nombreDataSource, DataSource dso) {
		super(nombreDataSource, dso);
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
	/*public void listar() throws Exception{	
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM INFI_TB_012_TRANSACCIONES");
		dataSet = db.get(dso, sb.toString());		
	}*/	
	
	/**Recorre la consulta y devuelve un objeto tipo Transaccion*/
	public Object moveNext() throws Exception {
		boolean bolPaso = false;
        com.bdv.infi_toma_orden.data.Transaccion tran = new com.bdv.infi_toma_orden.data.Transaccion();
        try {
            //Si no es ultimo registro arma el objeto            
            if ((resultQuery!=null)&&(!resultQuery.isAfterLast())){            	
                bolPaso = true;
                tran.setIdTransaccion(resultQuery.getString("TRANSA_ID"));
                tran.setDescripcion(resultQuery.getString("TRANSA_DESCRIPCION"));
                tran.setFuncionAsociada(resultQuery.getString("TRANSA_DESC_FUNCION"));
                resultQuery.next();
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
	
}

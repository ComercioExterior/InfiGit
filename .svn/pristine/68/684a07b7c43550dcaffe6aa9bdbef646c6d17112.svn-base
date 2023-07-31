package com.bdv.infi.dao;

import java.sql.SQLException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.bdv.infi.data.TasaCambioCierre;
import com.bdv.infi.util.Utilitario;

/**Busca las tasas de cambio del último cierre efectuado*/
public class TasaCambioCierreDAO extends GenericoDAO{
		
	
	private Logger logger = Logger.getLogger(TasaCambioCierreDAO.class);
	
	
	public TasaCambioCierreDAO(DataSource ds) {
		super(ds);
	}

	public TasaCambioCierreDAO(Transaccion transanccion) throws Exception {
		super(transanccion);
	}

	/**Busca las tasas de cambio de cierre para un determinado día
	 * @param codigoDivisa código de la moneda a buscar
	 * @param fechaInicio fecha de inicio para la búsqueda de tasas
	 * @param fechaFin fecha de fin para la búsqueda de tasas
	 * @return devuelve el objeto que contiene las tasas de cambio o nulo en caso de no encontrar ningún registro
	 * @throws lanza una excepción en caso de error*/
	public TasaCambioCierre listarTasas(String codigoDivisa) throws Exception{
		TasaCambioCierre TasaCambioCierre = null;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TCC_FECHA_CAMBIO, TCC_CODIGO_DIVISA, TCC_TASA_CAMBIO,TCC_TASA_CAMBIO_COMPRA ");
		sql.append(" FROM INFI_VI_TASA_CAM_CIERRE_DIARIO WHERE TCC_CODIGO_DIVISA='").append(codigoDivisa).append("'");
		
		try {
			conn = this.dataSource.getConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql.toString());
			
			if (resultSet.next()){
				TasaCambioCierre = new TasaCambioCierre(resultSet.getDate("TCC_FECHA_CAMBIO"),
						resultSet.getDouble("TCC_TASA_CAMBIO"),
						resultSet.getDouble("TCC_TASA_CAMBIO_COMPRA"),
						resultSet.getString("TCC_CODIGO_DIVISA")
						);				
			}
		} catch (SQLException e) {
			logger.error(e.getMessage()+ Utilitario.stackTraceException(e));
			throw new Exception("Error en la consulta de tasas de cambio: "+e.getMessage());
		} finally{
			
			this.closeResources();
			this.cerrarConexion();
		}
		System.out.println("TasaCambioCierre--->"+sql);
		return TasaCambioCierre;
		
	}

	@Override
	public Object moveNext() throws Exception {
		return null;
	}
	
	

}

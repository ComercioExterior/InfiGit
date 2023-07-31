package com.bdv.infi.model.mesaCambio;

import java.math.BigDecimal;
import java.util.List;
import javax.sql.DataSource;
import com.bdv.infi.data.ClientesMesa;

/**
 * Permite establecer los reportes generales de las compras y ventas
 * @author CT24667
 *
 */
public interface Notificar {
	
	
	/**
	 * 
	 * @param lst
	 * @return 
	 * @throws Exception 
	 */
	public Boolean Reportar(List<ClientesMesa> lst,DataSource ds) throws Exception;	
	
	/**
	 * 
	 */
	public void Rechazados();
	
	/**
	 * 
	 * @return
	 */
	public boolean Enviadas();
	
	/**
	 * 
	 * @param err
	 */
	public void FallaConexion(String err);
	
	/**
	 * Permite convertir valores en el caso de los euros
	 * @param tipoMoneda EUR - USD - VES
	 * @param valor 0.00
	 * @return
	 */
	public void Conversion(String tipoMoneda, BigDecimal valor, BigDecimal tasapacto);
	
	
//	public Monedas obtenerMoneda(String tipoModena);
}
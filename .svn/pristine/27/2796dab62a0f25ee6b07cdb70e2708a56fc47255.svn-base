package com.bdv.infi.model.menudeo;

import java.math.BigDecimal;
import java.util.List;

import javax.sql.DataSource;

import com.bdv.infi.data.ClienteMenudeo;

/**
 * Permite establecer los reportes generales de las compras y ventas
 * @author CT24667
 *
 */
public interface Notificar {
	
	
	/**
	 * 
	 * @param lst
	 * @throws Exception 
	 */
	public void Reportar(List<ClienteMenudeo> lst,DataSource ds) throws Exception;	
	
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
	public BigDecimal Conversion(String tipoMoneda, BigDecimal valor);
	
	
	public Monedas obtenerMoneda(String tipoModena);
}
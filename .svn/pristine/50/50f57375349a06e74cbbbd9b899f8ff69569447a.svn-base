package com.bdv.infi.logic.interfaz_altair.consult;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.jibx.runtime.JiBXException;

import com.bdv.infi.dao.ClienteDAO;
import com.bdv.infi.util.Utilitario;
import com.bdv.infi.webservices.beans.Cliente;
import com.bdv.infi.webservices.beans.Cuenta;
import com.bdv.infi.webservices.manager.ManejadorDeClientes;

/**Clase destinada a la recuperación de información de las cuentas de un cliente en ALTAIR*/
public class ManejoDeClientes {
	
	private Logger logger = Logger.getLogger(ManejoDeClientes.class);
	/**DataSource de INFI*/
	DataSource dataSource;
	
	public ManejoDeClientes(DataSource dataSource){
		this.dataSource = dataSource;
	}
	
	/**
	 * Busca las cuentas de un cliente determinado en altair
	 * @param ciRifCliente
	 * @param _app
	 * @param ip
	 * @return listaCuentas
	 * @throws IOException
	 * @throws JiBXException
	 * @throws Exception
	 */
	public ArrayList<Cuenta> buscarCuentasAltair(String ciRifCliente, String tipoPer, ServletContext _app, String ip, String userName) throws IOException, JiBXException, Exception{
		
		ManejadorDeClientes manejadorDeClientes = new ManejadorDeClientes(_app, null);
									
		//buscar cuentas asociadas al cliente
		ArrayList<Cuenta> listaCuentas = manejadorDeClientes.listaDeCuentas(ciRifCliente, tipoPer, userName, ip); 		
		return listaCuentas;
	}
	
	/**Obtiene el saldo de un número de cuenta particular
	 * @param ciRifCliente cédula o rif del cliente
	 * @param numeroCuenta número de cuenta que se desea buscar
	 * @param _app contexto de la aplicación
	 * @param ip dirección ip desde donde se está conectando el usuario
	 * @throws lanza una excepción si la cuenta del cliente no fue encontrada o si hay un error en la búsqueda
	 * @return BigDecimal con el saldo disponible de la cuenta*/
	public BigDecimal ObtenerSaldoCuenta(String ciRifCliente, String tipoPer, String numeroCuenta, ServletContext _app, String ip, String userName) throws Exception{
		BigDecimal saldoCuenta = new BigDecimal(0);
		boolean encontrada = false;
		try{
		   ArrayList<Cuenta> cuentas = buscarCuentasAltair(ciRifCliente, tipoPer, _app, ip, userName);
		   //Busca la cuenta, si no la consulta lanza una excepcion
		   for (Cuenta cuenta: cuentas){
			   if (cuenta.getNumero().equals(numeroCuenta)){
				   saldoCuenta = cuenta.getSaldoDisponible();
				   encontrada = true;
				   break;
			   }
		   }
		   //Verifica si la cuenta especificada fue encontrada
		   if (!encontrada){
			   throw new Exception("Cuenta del cliente no encontrada");
		   }
		} catch (Exception e) {
			logger.error(e.getMessage()+ Utilitario.stackTraceException(e));
			throw new Exception("Error consultando el saldo de la cuenta del cliente");
		}
		return saldoCuenta;
	}
	
	/**Obtiene el saldo de un número de cuenta particular
	 * @param ciRifCliente cédula o rif del cliente
	 * @param numeroCuenta número de cuenta que se desea buscar
	 * @param _app contexto de la aplicación
	 * @param ip dirección ip desde donde se está conectando el usuario
	 * @throws lanza una excepción si la cuenta del cliente no fue encontrada o si hay un error en la búsqueda
	 * @return Cuenta del cliente*/	
	public Cuenta ObtenerCuenta(String ciRifCliente, String tipoPer, String numeroCuenta, ServletContext _app, String ip, String userName) throws Exception{
		boolean encontrada = false;
		Cuenta cuentaCliente = null;
		try{
		   ArrayList<Cuenta> cuentas = buscarCuentasAltair(ciRifCliente, tipoPer, _app, ip, userName);
		   //Busca la cuenta, si no la consulta lanza una excepcion
		   for (Cuenta cuenta: cuentas){
			   if (cuenta.getNumero().equals(numeroCuenta)){
				   encontrada = true;
				   cuentaCliente = cuenta;
				   break;
			   }
		   }
		   //Verifica si la cuenta especificada fue encontrada
		   if (!encontrada){
			   throw new Exception("Cuenta del cliente no encontrada");
		   }
		} catch (Exception e) {
			logger.error(e.getMessage()+ Utilitario.stackTraceException(e));
			throw new Exception("Error consultando el saldo de la cuenta del cliente");
		}
		return cuentaCliente;
	}
	
	/**Clase para buscar el cliente en ALTAIR, de encontrarse lo registra en INFI*/	
	private void almacenarClienteInfi(com.bdv.infi.data.Cliente cliente) throws Exception{
		
		ClienteDAO clienteDAO = new ClienteDAO(this.dataSource);
		
		try {
			//Se busca primero el cliente en la base de datos local, de no encontrarse se buscar&aacute; por la c&eacute;dula
			//si ha sido ingresada
			clienteDAO.buscarPorCedRif(cliente.getRifCedula());	

			//Si no consigue registros lo almacena en INFI
			if (clienteDAO.getDataSet().count()==0){
				   clienteDAO.insertar(cliente);
				   clienteDAO.cerrarConexion();
			}
		} catch (Exception e) {
			logger.error(e.getMessage()+ Utilitario.stackTraceException(e));
			throw new Exception("Error registrando el cliente en INFI ");
		}		
	}
	
	/**Clase para buscar el cliente en ALTAIR 
	 * @param cedRif cédula o rif del cliente a consultar en ALTAIR
	 * @param ip dirección ip del usuario que consulta
	 * @param guardarInfi verdadero si se desea guardar el cliente en INFI encontrado en ALTAIR
	 * @param datosJuridicosPE55 verdadero si se desean buscar datos adicionales cuando el cliente es jurídico.
	 * @param datosAdicionalesPEV7 verdadero si se desean obtener datos adicionales del cliente
	 * @param datosAdicionalesPE68 verdadero si se desea buscar la nacionalidad, sexo y fecha de nacimiento del cliente 
	*/
	public Cliente obtenerClienteAltair(String cedRif, String tipoPer, String ip, ServletContext servletContext, boolean guardarInfi, boolean datosJuridicosPE55, boolean datosAdicionalesPEV7, boolean datosAdicionalesPE68, String userName) throws Exception{
		Cliente clienteWS = null;
		try {
			ManejadorDeClientes mdc = new ManejadorDeClientes(servletContext,null);
			
			//Debe estar en false,true para que pueda buscar el segmento al que pertenece el cliente
			clienteWS = mdc.getCliente(String.valueOf(cedRif), tipoPer, userName, ip ,datosJuridicosPE55,datosAdicionalesPEV7,datosAdicionalesPE68,false);
			
			//Si se desea guarda en INFI se busca si el cliente existe en INFI
			if (guardarInfi){
				//Si lo encuentra lo almacena en la tabla de cliente
				com.bdv.infi.data.Cliente clienteNuevo = new com.bdv.infi.data.Cliente();
				//clienteNuevo.setRifCedula(Long.parseLong(clienteWS.getCi().replaceAll("^0+", "")));
				clienteNuevo.setRifCedula(Long.parseLong(clienteWS.getCi()));
				clienteNuevo.setNombre(clienteWS.getNombreCompleto().replaceAll("\\s\\s+", " "));
				clienteNuevo.setTipoPersona(clienteWS.getTipoDocumento().trim());
				//clienteNuevo.setCodigoSegmento(clienteWS.getPEM1403().getSegmento().trim());
				almacenarClienteInfi(clienteNuevo);
			}
		} finally {
			
		}
		return clienteWS;		
	}	
}

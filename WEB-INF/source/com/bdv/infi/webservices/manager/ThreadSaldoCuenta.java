package com.bdv.infi.webservices.manager;

import java.util.Date;
import javax.servlet.ServletContext;
import com.bdv.infi.webservices.beans.Cliente;
import com.bdv.infi.webservices.beans.Cuenta;
import com.bdv.infi.webservices.beans.consult.ConsultaSaldoCuentaAEA;
import com.bdv.infi.webservices.client.ClienteWs;

public class ThreadSaldoCuenta extends Thread {
	private Cuenta c;
	private Cliente cliente;
	private ServletContext contexto;
	private String username="";
	
	private String ip="";
	
	public ThreadSaldoCuenta(Cuenta c, Cliente cli, ServletContext s, String username, String ip) {
		this.c = c;
		this.cliente = cli;
		this.contexto = s;
		this.username = username;
		this.ip= ip;
	}

	@Override
	public void run() {
		/*
		 * primero intenta traerlos de host, si falla, se los intentara traer
		 * desde D-1
		 */
		try {
			ConsultaSaldoCuentaAEA consultaAea = new ConsultaSaldoCuentaAEA();
			consultaAea.setNumeroCuenta(c.getNumero());
			consultaAea.setCredenciales(cliente.getCredenciales());
			consultaAea.setDivisa("VEF");
			consultaAea.setComision(false);

			ClienteWs clienteWs = new ClienteWs();
			clienteWs = ClienteWs.crear("saldoCuentaEnLinea", contexto);

			Cuenta c3 = (Cuenta) clienteWs.enviarYRecibir(
					consultaAea, ConsultaSaldoCuentaAEA.class, Cuenta.class , username, ip);

			c.setSaldoTotal(c3.getSaldoTotal());
			c.setSaldoDisponible(c3.getSaldoDisponible());
			c.setSaldoBloqueado(c3.getSaldoBloqueado());
			c.setSaldoDiferido(c3.getSaldoDiferido());
			c.setLimiteDeCredito(c3.getLimiteDeCredito());
			c.setEsSaldoEnLinea(true);

			c.setFechaDeSaldo(new Date());
			c.setSaldoPromedio(c.getSaldoTotal());
			c.setFechaDeSaldoPromedio(c.getFechaDeSaldo());
				
		} catch (Exception ex) {
			/*
			 * fallo al traer los datos de host. ahora va a intentar traer los
			 * datos desde D-1
			 */
			ex.printStackTrace();

			/*try {
				ClienteWs clienteWs = ClienteWs.crear("saldoCuentaFueraLinea",
						contexto);
				Cuenta c2 = (Cuenta) clienteWs.enviarYRecibir(c, Cuenta.class,
						Cuenta.class , username, ip);

				c.setFechaDeSaldo(c2.getFechaDeSaldo());
				c.setSaldoTotal(c2.getSaldoTotal());
				c.setSaldoDisponible(c2.getSaldoDisponible());
				c.setSaldoBloqueado(c2.getSaldoBloqueado());
				c.setSaldoDiferido(c2.getSaldoDiferido());
				c.setLimiteDeCredito(c2.getLimiteDeCredito());
				c.setSaldoPromedio(c2.getSaldoPromedio());
				c.setFechaDeSaldoPromedio(c2.getFechaDeSaldoPromedio());
				c.setEsSaldoEnLinea(false);*/
			//} catch (Exception ex2) {
				/*
				 * fallo tambien al traer los datos desde D-1. Para que no falle
				 * en la interfaz, se colocan todos los saldos en cero
				 */
				/*ex2.printStackTrace();

				c.setFechaDeSaldo(new Date());
				c.setSaldoTotal(new BigDecimal(0));
				c.setSaldoDisponible(c.getSaldoTotal());
				c.setSaldoBloqueado(c.getSaldoTotal());
				c.setSaldoDiferido(c.getSaldoTotal());
				c.setLimiteDeCredito(c.getSaldoTotal());
				c.setSaldoPromedio(c.getSaldoTotal());
				c.setFechaDeSaldoPromedio(c.getFechaDeSaldo());
				c.setEsSaldoEnLinea(false);
			}*/
		}
	}

}

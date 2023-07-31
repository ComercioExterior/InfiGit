package com.bdv.infi.model.mesaCambio;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.axis.AxisFault;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import megasoft.Logger;

import com.bdv.infi.config.Propiedades;
import com.bdv.infi.dao.MesaCambioDAO;
import com.bdv.infi.data.ClientesMesa;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.model.menudeo.Monedas;
import com.bdv.infi.model.menudeo.TasaCambio;
import com.sun.org.apache.xml.internal.utils.SuballocatedByteVector;

import electric.xml.Node;

public class Comprar extends MesaStub implements Notificar {

	public Comprar() throws Exception {
		super();
	}

	public ConstantesGenerales cG = new ConstantesGenerales();
	public BigDecimal TasaDolares = new BigDecimal(0.00);
	public BigDecimal TasaEuro = new BigDecimal(0.00);
	public BigDecimal TasaPacto = new BigDecimal(0.00);
	public String ContenidoCorreo = "";
	List<Monedas> listaTasas;

	public BigDecimal montoDivision = new BigDecimal(0);
	public BigDecimal montoContraValor = new BigDecimal(0);
	public BigDecimal factor = new BigDecimal(0);

	/**
	 * @throws Exception
	 * 
	 */
	public Boolean Reportar(List<ClientesMesa> lst, DataSource ds) throws Exception {
		MesaCambioDAO dao = new MesaCambioDAO(ds);
		long InicioFnx = System.nanoTime();
		double[] OperarFn = new double[lst.size()];
		double[] FallasFn = new double[lst.size()];
		int i = 0;
		Boolean validar = true;
		
		TasaCambio tsc = new TasaCambio();
		Method Fn = TasaCambio.class.getMethod("lecturaBcv");
		tsc.procesar(tsc, Fn);
		this.listaTasas = tsc.ListarMoneda();
		for (Monedas tasa : listaTasas) {
			System.out.println("siglas : " + tasa.getSiglas());
			System.out.println("tasas : " + tasa.getCompra());
			
			if (tasa.getSiglas().equalsIgnoreCase("USD")) {
				TasaDolares = tasa.getCompra();

			}
			if (tasa.getSiglas().equalsIgnoreCase("EUR")) {
				TasaEuro = tasa.getCompra();

			}
			tasa.getCompra();
		}

		for (ClientesMesa clt : lst) {
			long IFnx = System.nanoTime();

			try {
				Conversion(clt.MonedaBase, clt.MontoBaseDivisas, clt.TasaCambio);
				// System.out.println(" Operaciones Enviada : "+ clt.CedulaRifOferta+" - "+ clt.NombreOferta+" - "+ clt.MonedaBase+" - "+ clt.MontoBaseDivisas+" - "+ clt.TasaCambio+"  -  cl"+ clt.CuentaOfertaMe +" - "+ clt.CuentaOfertaMn+" - "+ "Instrumento - " + clt.CedulaRifDemanda+" - "+ clt.NombreDemanda+" - "+ clt.CuentaDemandaMe+" - "+ clt.CuentaDemandaMn+" - "+ clt.MontoBaseDivisas+" - "+
				// clt.MontoBaseBolivares+" - "+ clt.TasaCambio +" - "+ clt.TipoPacto);
				System.out.println(" Operaciones Enviada : " + clt.NacionalidadOferta + clt.CedulaRifOferta + " - " + clt.NombreOferta + " - " + clt.MonedaBase + " - " + clt.MontoBaseDivisas + " - " + clt.TasaCambio + " - " + clt.codigoInstitucion + " - " + clt.CuentaOfertaMe + " - " + clt.CuentaOfertaMn + " - " + clt.Instrumento + " - " + clt.NacionalidadDemanda + clt.CedulaRifDemanda + " - "
						+ clt.NombreDemanda + " - " + clt.CuentaDemandaMe + " - " + clt.CuentaDemandaMn + " - " + this.montoDivision + " - " + this.montoContraValor + " - " + TasaDolares + " - " + clt.TipoPacto);
				// Logger.info(this," Operaciones EnviadaXXX : "+ clt.NacionalidadOferta+clt.CedulaRifOferta+ clt.NombreOferta+ clt.MonedaBase+ clt.MontoBaseDivisas+ clt.TasaCambio+ "102"+ clt.CuentaOfertaMe+ clt.CuentaOfertaMn+ clt.Instrumento+ clt.NacionalidadDemanda+clt.CedulaRifDemanda+ clt.NombreDemanda+ clt.CuentaDemandaMe+ clt.CuentaDemandaMn+ clt.MontoBaseDivisas+ clt.MontoBaseBolivares+
				// clt.TasaCambio+clt.TipoPacto);
				String ordenBCV = Stub.pactodirecto(clt.NacionalidadOferta + clt.CedulaRifOferta, clt.NombreOferta, clt.MonedaBase, clt.MontoBaseDivisas, clt.TasaCambio, clt.codigoInstitucion, clt.CuentaOfertaMe, clt.CuentaOfertaMn, clt.Instrumento, clt.NacionalidadDemanda + clt.CedulaRifDemanda, clt.NombreDemanda, clt.CuentaDemandaMe, clt.CuentaDemandaMn, this.montoDivision,
						clt.MontoBaseBolivares, TasaDolares, clt.TipoPacto);
				dao.modificar(clt.IdOrdenes, ordenBCV, "Envio a BCV ejecutado con exito", "1", this.montoContraValor, this.montoDivision, TasaDolares);
				long FFnx = System.nanoTime();
				OperarFn[i] = (IFnx - FFnx) / 1e6;
				System.out.println("ordenBCV : " + ordenBCV);
				System.out.println("OperarFn[i] : " + OperarFn[i]);
			} catch (Exception e) {
				validar = false;
				String error = "";
				if (e.toString().length() > 200) {
					error = e.toString().substring(0, 199);
				} else {
					error = e.toString();
				}
				Logger.info(this, " Operaciones EnviadaXXX : " + clt.NacionalidadOferta + clt.CedulaRifOferta + clt.NombreOferta + clt.MonedaBase + clt.MontoBaseDivisas + clt.TasaCambio + "102" + clt.CuentaOfertaMe + clt.CuentaOfertaMn + clt.Instrumento + clt.NacionalidadDemanda + clt.CedulaRifDemanda + clt.NombreDemanda + clt.CuentaDemandaMe + clt.CuentaDemandaMn + clt.MontoBaseDivisas
						+ clt.MontoBaseBolivares + clt.TasaCambio + clt.TipoPacto);
				dao.modificar(clt.IdOrdenes, "", error, "4", clt.MontoBaseBolivares, this.montoDivision, TasaDolares);
				Logger.error(this, "Ha ocurrido un error al momento de enviar la orden al BCV ORDENE_ID: " + clt.IdOrdenes + " - " + e.toString());
				System.out.println("Ha ocurrido un error al momento de enviar la orden al BCV ORDENE_ID: " + clt.IdOrdenes + " - " + e.toString());
				long FFnx = System.nanoTime();
				FallasFn[i] = (IFnx - FFnx) / 1e6;
				System.out.println("OperarFn[i] : " + OperarFn[i]);
			}
		}

		this.ResumenOperaciones(InicioFnx, OperarFn, FallasFn, "");
		return validar;

	}

	public void ResumenOperaciones(double InicioFnx, double[] OperarFn, double[] FallasFn, String msj) {

		long FinFnx = System.nanoTime(); // Fin de la funcion 1e6
		double TotalFnx = (InicioFnx - FinFnx) / 1e6;
		System.out.println("TIEMPO TOTAL DE EJECUCION Comprar:Reportar() " + TotalFnx + " ms.");
		this.ContenidoCorreo = "TIEMPO TOTAL DE EJECUCION Comprar:Reportar() " + TotalFnx + " ms. \n";

		this.ContenidoCorreo += "\n\n";
		this.ContenidoCorreo += "PORCENTAJES DE CONCILIACION EXITOSO \n";
		System.out.println("PORCENTAJES DE CONCILIACION EXITOSO \n");
		this.ContenidoCorreo += this.promedioFnx(OperarFn);

		this.ContenidoCorreo += "PORCENTAJES DE CONCILIACION FALLIDO \n";
		System.out.println("PORCENTAJES DE CONCILIACION FALLIDO \n");

		this.ContenidoCorreo += this.promedioFnx(FallasFn);
		this.ContenidoCorreo += "\n\n";
		this.ContenidoCorreo += "--------------------------------------- \n";
		this.ContenidoCorreo += "BANCO DE VENEZUELA \n";
		this.ContenidoCorreo += "--------------------------------------- \n";

		// Correo correo = new Correo("RESUMEN DE OPERACIONES", "infi.bdv.soporte@bdv.enlinea.com", "infi.bdv.soporte@bdv.enlinea.com", _dso);
		// correo.Enviar(contenido);
	}

	/**
	 * Evaluacion de AVERAGE En funcion
	 * 
	 * @param Fnx
	 */
	public String promedioFnx(double[] Fnx) {
		String contenido = "";

		int cant = Fnx.length;
		int max = 0;
		int min = 0;
		for (int i = 0; i < cant; i++) {
			if ((Fnx[i] / 1000) > cG.MAXIMO_TIEMPO_ITERACION) {
				max++;
			} else {
				min++;
			}
		}
		double maximo = (max * 100) / cant; // APLICAR PORCENTAJE
		double minimo = (min * 100) / cant;
		System.out.println("RESUMEN DE TRANSACCIONES \n");
		System.out.println("CANTIDAD MAXIMA DE OPERACIONES ENVIADAS " + max + "\n");
		System.out.println("CANTIDAD MINIMA DE OPERACIONES ENVIADAS " + min + "\n");
		System.out.println("PORCENTAJE: ~MAXIMO (+) " + cG.MAXIMO_TIEMPO_ITERACION + " seg. " + maximo + "% \n");
		System.out.println("PORCENTAJE: ~MINIMO (-)  " + cG.MINIMO_TIEMPO_ITERACION + " seg. " + minimo + " % \n");
		contenido = "RESUMEN DE TRANSACCIONES \n";
		contenido += "CANTIDAD MAXIMA DE OPERACIONES ENVIADAS " + max + "\n";
		contenido += "CANTIDAD MINIMA DE OPERACIONES ENVIADAS " + min + "\n";
		contenido += "PORCENTAJE: ~MAXIMO (+) " + cG.MAXIMO_TIEMPO_ITERACION + " seg. " + maximo + "% \n";
		contenido += "PORCENTAJE: ~MINIMO (-)  " + cG.MINIMO_TIEMPO_ITERACION + " seg. " + minimo + " % \n";

		return contenido;
	}

	/**
	 * Metodo para llamar los servicios de menudeo de banco central.
	 * 
	 * @param enpPoint
	 * @throws AxisFault
	 * @throws MalformedURLException
	 */
	// public void serviciosMenudeoBancoCentral(String enpPoint) throws AxisFault, MalformedURLException {
	//
	// this.stub = new AutorizacionPortBindingStub(new URL(propiedades.getProperty(enpPoint)), null);
	// }

	public void Rechazados() {
		// TODO Auto-generated method stub

	}

	public boolean Enviadas() {
		// TODO Auto-generated method stub
		return true;
	}

	public void FallaConexion(String err) {
		// TODO Auto-generated method stub
		Logger.error(this, err);

	}

	public void Conversion(String tipoMoneda, BigDecimal valor, BigDecimal tasapacto) {
		try {
			
			if (tipoMoneda.equalsIgnoreCase("840")) {
				System.out.println("llego 840");
				this.montoDivision = valor;
				TasaDolares = tasapacto;
				
			}else{
			this.factor = TasaEuro.divide(this.TasaDolares, 18, RoundingMode.DOWN);
			this.montoDivision = factor.multiply(valor);
			
			System.out.println("montoDivisionnnnnnnnnnnnn : " + montoDivision);
			System.out.println("tassaaaaaaaaaaaaaaaaaaaaa : " + TasaDolares);
			this.montoContraValor = montoDivision.multiply(TasaDolares);
			System.out.println("montoContraValorrrrrrrrrrrrrrr : " + montoContraValor);
			TasaDolares = TasaDolares.setScale(4, RoundingMode.DOWN);
			montoDivision = montoDivision.setScale(2, RoundingMode.DOWN);
			montoContraValor = montoContraValor.setScale(2, RoundingMode.DOWN);
			}
			
		} catch (Exception e) {
			
		}

	}
}
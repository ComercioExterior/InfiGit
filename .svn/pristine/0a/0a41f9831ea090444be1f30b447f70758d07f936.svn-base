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
import com.bdv.infi.data.ClientesMesaBancario;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.model.menudeo.Monedas;
import com.bdv.infi.model.menudeo.TasaCambio;
import com.sun.org.apache.xml.internal.utils.SuballocatedByteVector;
import electric.xml.Node;

public class NotificarOD extends MesaStub {

	public NotificarOD() throws Exception {
		super();
	}

	public ConstantesGenerales cG = new ConstantesGenerales();
	public BigDecimal TasaDolares = new BigDecimal(0.00);
	public BigDecimal TasaPacto = new BigDecimal(0.00);
	public String ContenidoCorreo = "";
	List<Monedas> listaTasas;
	public BigDecimal montoDivision = new BigDecimal(0);
	public BigDecimal montoContraValor = new BigDecimal(0);

	/**
	 * @throws Exception
	 * 
	 */
	public Boolean Reportar(List<ClientesMesaBancario> lst, DataSource ds) throws Exception {
		MesaCambioDAO dao = new MesaCambioDAO(ds);
		long InicioFnx = System.nanoTime();
		double[] OperarFn = new double[lst.size()];
		double[] FallasFn = new double[lst.size()];
		int i = 0;
		Boolean validar = true;

		for (ClientesMesaBancario clt : lst) {
			long IFnx = System.nanoTime();
			String ordenBCV ="";
			try {
//				Conversion("", clt.MontoBaseDivisas, clt.TasaCambio);
				// System.out.println(" Operaciones Enviada : "+ clt.CedulaRifOferta+" - "+ clt.NombreOferta+" - "+ clt.MonedaBase+" - "+ clt.MontoBaseDivisas+" - "+ clt.TasaCambio+"  -  cl"+ clt.CuentaOfertaMe +" - "+ clt.CuentaOfertaMn+" - "+ "Instrumento - " + clt.CedulaRifDemanda+" - "+ clt.NombreDemanda+" - "+ clt.CuentaDemandaMe+" - "+ clt.CuentaDemandaMn+" - "+ clt.MontoBaseDivisas+" - "+
				// clt.MontoBaseBolivares+" - "+ clt.TasaCambio +" - "+ clt.TipoPacto);
				System.out.println(" Operaciones interbancaria  Enviada : " + clt.TipoCliente+clt.RifCliente+"-"+clt.NombreCliente+"-"+ clt.CodigoMoneda+"-"+clt.Monto+"-"+clt.TasaCambio+"-"+clt.CodigoInstitucion+"-"+ clt.IdJornada+"-"+ clt.CuentaMe+"-"+ clt.CuentaMn+"-"+ clt.TipoInstrumento);
				// Logger.info(this," Operaciones EnviadaXXX : "+ clt.NacionalidadOferta+clt.CedulaRifOferta+ clt.NombreOferta+ clt.MonedaBase+ clt.MontoBaseDivisas+ clt.TasaCambio+ "102"+ clt.CuentaOfertaMe+ clt.CuentaOfertaMn+ clt.Instrumento+ clt.NacionalidadDemanda+clt.CedulaRifDemanda+ clt.NombreDemanda+ clt.CuentaDemandaMe+ clt.CuentaDemandaMn+ clt.MontoBaseDivisas+ clt.MontoBaseBolivares+
				// clt.TasaCambio+clt.TipoPacto);
				if (clt.TipoOperacion.equalsIgnoreCase("O")) {
					ordenBCV = Stub.oferta(clt.TipoCliente+clt.RifCliente,clt.NombreCliente, "840",clt.Monto,clt.TasaCambio,"102", clt.IdJornada, clt.CuentaMe, clt.CuentaMn, clt.TipoInstrumento);
				}else{
					ordenBCV = Stub.demanda(clt.TipoCliente+clt.RifCliente,clt.NombreCliente, "840",clt.Monto,clt.TasaCambio,"102", clt.IdJornada, clt.CuentaMe, clt.CuentaMn);
				}
					
				dao.modificarInterbancario(clt.Id, ordenBCV, "Envio a BCV ejecutado con exito", "1", this.montoContraValor, this.montoDivision, TasaDolares);
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
				System.out.println(" Operaciones interbancaria  Enviada : " + clt.TipoCliente+clt.RifCliente+"-"+clt.NombreCliente+"-"+ clt.CodigoMoneda+"-"+clt.Monto+"-"+clt.TasaCambio+"-"+clt.CodigoInstitucion+"-"+ clt.IdJornada+"-"+ clt.CuentaMe+"-"+ clt.CuentaMn+"-"+ clt.TipoInstrumento);
				dao.modificarInterbancario(clt.Id, "", error, "4", this.montoContraValor, this.montoDivision, TasaDolares);
								
				Logger.error(this, "Ha ocurrido un error al momento de enviar la orden al BCV ID: " + clt.Id + " - " + e.toString());
				System.out.println("Ha ocurrido un error al momento de enviar la orden al BCV ID: " + clt.Id + " - " + e.toString());
				long FFnx = System.nanoTime();
				FallasFn[i] = (IFnx - FFnx) / 1e6;
				System.out.println("OperarFn[i] : " + OperarFn[i]);
			}
		}

		this.ResumenOperaciones(InicioFnx, OperarFn, FallasFn, "");
		return validar;

	}
	
	public Boolean ReportarPacto(String idOperacion, String jornada, String monto, 
			String montoBase, String contravalor, String tasa , 
			String tipoPacto, String codDemand, String codOferta, DataSource ds) throws Exception {
		MesaCambioDAO dao = new MesaCambioDAO(ds);
		long InicioFnx = System.nanoTime();
		BigDecimal montoOriginal = new BigDecimal(monto);
		BigDecimal montoBas = new BigDecimal(monto);
		BigDecimal montoContra= new BigDecimal(monto);
		BigDecimal tas= new BigDecimal(tasa);
//		double[] OperarFn = new double[lst.size()];
//		double[] FallasFn = new double[lst.size()];
		int i = 0;
		Boolean validar = true;

//		for (ClientesMesaBancario clt : lst) {
			long IFnx = System.nanoTime();
			String ordenBCV ="";
			try {
//				Conversion("", clt.MontoBaseDivisas, clt.TasaCambio);
				// System.out.println(" Operaciones Enviada : "+ clt.CedulaRifOferta+" - "+ clt.NombreOferta+" - "+ clt.MonedaBase+" - "+ clt.MontoBaseDivisas+" - "+ clt.TasaCambio+"  -  cl"+ clt.CuentaOfertaMe +" - "+ clt.CuentaOfertaMn+" - "+ "Instrumento - " + clt.CedulaRifDemanda+" - "+ clt.NombreDemanda+" - "+ clt.CuentaDemandaMe+" - "+ clt.CuentaDemandaMn+" - "+ clt.MontoBaseDivisas+" - "+
				// clt.MontoBaseBolivares+" - "+ clt.TasaCambio +" - "+ clt.TipoPacto);
//				System.out.println(" Operaciones interbancaria  Enviada : " + clt.TipoCliente+clt.RifCliente+"-"+clt.NombreCliente+"-"+ clt.CodigoMoneda+"-"+clt.Monto+"-"+clt.TasaCambio+"-"+clt.CodigoInstitucion+"-"+ clt.IdJornada+"-"+ clt.CuentaMe+"-"+ clt.CuentaMn+"-"+ clt.TipoInstrumento);
				// Logger.info(this," Operaciones EnviadaXXX : "+ clt.NacionalidadOferta+clt.CedulaRifOferta+ clt.NombreOferta+ clt.MonedaBase+ clt.MontoBaseDivisas+ clt.TasaCambio+ "102"+ clt.CuentaOfertaMe+ clt.CuentaOfertaMn+ clt.Instrumento+ clt.NacionalidadDemanda+clt.CedulaRifDemanda+ clt.NombreDemanda+ clt.CuentaDemandaMe+ clt.CuentaDemandaMn+ clt.MontoBaseDivisas+ clt.MontoBaseBolivares+
				// clt.TasaCambio+clt.TipoPacto);
//				BigDecimal montoOriginal = new BigDecimal(monto);
//				BigDecimal montoBas = new BigDecimal(monto);
//				BigDecimal montoContra= new BigDecimal(monto);
//				BigDecimal tas= new BigDecimal(tasa);
				System.out.println(" Operaciones interbancaria  Enviada jeje: " + jornada+" - "+ codOferta+" - "+ codDemand+" - "+ montoOriginal+" - "+ montoBas+" - "+ montoContra+" - "+ tas+" - "+ tipoPacto);
				ordenBCV = Stub.pacto(jornada, codOferta, codDemand, montoOriginal, montoBas, montoContra, tas, tipoPacto);
					
//				dao.modificarInterbancario(idOperacion, ordenBCV, "Envio a BCV ejecutado con exito", "1", this.montoContraValor, this.montoDivision, TasaDolares);
				dao.InsertarPacto(idOperacion, jornada, montoOriginal, montoBas, montoContra, tas, tipoPacto, codDemand, codOferta, ordenBCV, "Se ha notificado el pacto", "1");
				long FFnx = System.nanoTime();
//				OperarFn[i] = (IFnx - FFnx) / 1e6;
				System.out.println("ordenBCV : " + ordenBCV);
//				System.out.println("OperarFn[i] : " + OperarFn[i]);
			} catch (Exception e) {
				validar = false;
				String error = "";
				if (e.toString().length() > 200) {
					error = e.toString().substring(0, 199);
				} else {
					error = e.toString();
				}
////				System.out.println(" Operaciones interbancaria  Enviada : " + clt.TipoCliente+clt.RifCliente+"-"+clt.NombreCliente+"-"+ clt.CodigoMoneda+"-"+clt.Monto+"-"+clt.TasaCambio+"-"+clt.CodigoInstitucion+"-"+ clt.IdJornada+"-"+ clt.CuentaMe+"-"+ clt.CuentaMn+"-"+ clt.TipoInstrumento);
//				dao.modificarInterbancario(idOperacion, "", error, "4", this.montoContraValor, this.montoDivision, TasaDolares);
				dao.InsertarPacto(idOperacion, jornada, montoOriginal, montoBas, montoContra, tas, tipoPacto, codDemand, codOferta, "", error, "4");				
				Logger.error(this, "Ha ocurrido un error al momento de enviar la orden al BCV ID: " + idOperacion + " - " + e.toString());
				System.out.println("Ha ocurrido un error al momento de enviar la orden al BCV ID: " + idOperacion + " - " + e.toString());
				long FFnx = System.nanoTime();
//				FallasFn[i] = (IFnx - FFnx) / 1e6;
//				System.out.println("OperarFn[i] : " + OperarFn[i]);
			}
//		}

//		this.ResumenOperaciones(InicioFnx, OperarFn, FallasFn, "");
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
			TasaCambio tsc = new TasaCambio();
			Method Fn = TasaCambio.class.getMethod("lecturaBcv");
			tsc.procesar(tsc, Fn);
			this.listaTasas = tsc.ListarMoneda();
			for (Monedas tasa : listaTasas) {
				if (tasa.getSiglas().equalsIgnoreCase("USD")) {
					TasaDolares = tasa.getCompra();

				}
				tasa.getCompra();
			}
			this.montoDivision = tasapacto.divide(this.TasaDolares, 13, RoundingMode.DOWN).multiply(valor);
			System.out.println("montoDivisionnnnnnnnnnnnn : " + montoDivision);
			System.out.println("tassaaaaaaaaaaaaaaaaaaaaa : " + TasaDolares);
			this.montoContraValor = montoDivision.multiply(TasaDolares);
			System.out.println("montoContraValorrrrrrrrrrrrrrr : " + montoContraValor);
			TasaDolares = TasaDolares.setScale(4, RoundingMode.DOWN);
			montoDivision = montoDivision.setScale(2, RoundingMode.DOWN);
			montoContraValor = montoContraValor.setScale(2, RoundingMode.DOWN);
			// return montoDivision.setScale(4, RoundingMode.DOWN);
			// }
			// return valor;
		} catch (Exception e) {
			// TODO: handle exception

			// return valor;
		}

	}
}
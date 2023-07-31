package com.bdv.infi.model.menudeo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.apache.axis.AxisFault;
import megasoft.Logger;
import models.correo.Correo;
import com.bdv.infi.dao.ClienteMenudeoDao;
import com.bdv.infi.data.ClienteMenudeo;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

public class Comprar extends AutorizacionStub implements Notificar {

	public Comprar() throws Exception {
		super();
	}

	public List<Monedas> LstMonedas = new ArrayList<Monedas>();
	String ordenBCV;
	public Monedas monedas;
	public ConstantesGenerales cG = new ConstantesGenerales();
	public BigDecimal TasaDolares = new BigDecimal(0.00);
	public BigDecimal TasaEuros = new BigDecimal(0.00);
	public String ContenidoCorreo = "";

	/**
	 * @throws Exception
	 * 
	 */
	public void Reportar(List<ClienteMenudeo> lst, DataSource ds) throws Exception {

		ClienteMenudeoDao dao = new ClienteMenudeoDao(ds);
		System.out.println("llegoo reportar");
		long InicioFnx = System.nanoTime();
		double[] OperarFn = new double[lst.size()];
		double[] FallasFn = new double[lst.size()];
		int i = 0;
		this.TasaDolares = this.obtenerMoneda("USD").Compra;
		this.TasaEuros = this.obtenerMoneda("EUR").Compra;
		int iError = 0;
		
		for (ClienteMenudeo clt : lst) {
			long IFnx = System.nanoTime();
			System.out.println(clt.Cedula);
			String tipoMovimiento = "";
			int movimiento = 1;
			BigDecimal montoOperacion = this.Conversion(clt.MonedaBase, clt.MontoBase);

			switch (clt.IntNacionalidad) {
			case 1: // V
				tipoMovimiento = ConstantesGenerales.TIPO_DE_MOVIMIENTO_EFEC_VEN;
				break;
			case 2: // E
				tipoMovimiento = ConstantesGenerales.TIPO_DE_MOVIMIENTO_EFEC_EXTRAN;
				break;
			case 3: // J
				tipoMovimiento = ConstantesGenerales.TIPO_DE_MOVIMIENTO_EFEC_JUR;
				break;
			case 4: // P
				tipoMovimiento = ConstantesGenerales.TIPO_DE_MOVIMIENTO_COMPRA_EFEC_PASAPORTE;
				movimiento = 2;
				break;
			case 5: // G
				tipoMovimiento = ConstantesGenerales.TIPO_DE_MOVIMIENTO_COMPRA_EFEC_GOBIERNO;
				movimiento = 12;
				break;
			default:
				tipoMovimiento = ConstantesGenerales.TIPO_DE_MOVIMIENTO_EFEC_VEN;
				break;
			}

			if (clt.ConceptoEstadistica.equals("100")) {

				movimiento = 18;
				if (clt.IntNacionalidad == 3) {
					tipoMovimiento = ConstantesGenerales.TIPO_DE_MOVIMIENTO_EFEC_JUR;
				} else {
					tipoMovimiento = ConstantesGenerales.TIPO_DE_MOVIMIENTO_COMPRA_EFEC_GOBIERNO;
				}
			}
			try {
				// System.out.println(" datos : movimiento :" + tipoMovimiento +" cedula rif :"+ clt.Rif+" Nombre: " +clt.Nombre+ " Monto Operacion :"+montoOperacion+ " Tasa : "+this.TasaDolares+" Moneda ISO :"+ clt.CodigoMonedaIso+" Monto Base : " +clt.MontoBase+ " Movimiento :"+movimiento+ ""+" Telefono : "+ clt.Telefono+ clt.Correo+ "");
				ordenBCV = Stub.COMPRADIV(tipoMovimiento, clt.Rif, clt.Nombre, montoOperacion, this.TasaDolares, clt.CodigoMonedaIso, clt.MontoBase, movimiento, clt.CtaConvenio20, clt.Telefono, clt.Correo, "");
				dao.modificar(clt, ordenBCV, "Envio a BCV ejecutado con exito","1");
				long FFnx = System.nanoTime();
				OperarFn[i] = (IFnx - FFnx) / 1e6;
				System.out.println("ordenBCV : " + ordenBCV);
			} catch (Exception e) {
				System.out.println("Error con la operacion : " + e);
				dao.modificar(clt, ordenBCV, e.toString(),"4");
				long FFnx = System.nanoTime();
				FallasFn[i] = (IFnx - FFnx) / 1e6;
				String error = e.toString();

				if (error.contains("USUARIO Y PASSWORD"))
					iError++;
				if (iError > cG.MAXIMO_ERROR_ITERACION) {
					// Logger
					// CAMBIAR EL ESTATUS DEL PROGRAMADOR
					this.ResumenOperaciones(InicioFnx, OperarFn, FallasFn, "EL USUARIO Y LA CLAVE ESTAN FALLANDO");
					return;
				}
			}
		}

		this.ResumenOperaciones(InicioFnx, OperarFn, FallasFn, "");

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

//		 Correo correo = new Correo("RESUMEN DE OPERACIONES", "infi.bdv.soporte@bdv.enlinea.com", "infi.bdv.soporte@bdv.enlinea.com", _dso);
//		 correo.Enviar(contenido);
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

	public BigDecimal Conversion(String tipoMoneda, BigDecimal valor) {

		if (tipoMoneda == "EUR") {
			BigDecimal montoDivision = this.TasaEuros.divide(this.TasaDolares, 8, RoundingMode.DOWN).multiply(valor);
			return montoDivision.setScale(4, RoundingMode.DOWN);
		}
		return valor;
	}

	public Monedas obtenerMoneda(String tipoModena) {
		for (Monedas mnd : this.LstMonedas) {
			if (mnd.Siglas.equals(tipoModena))
				return mnd;
		}
		return null;
	}

}
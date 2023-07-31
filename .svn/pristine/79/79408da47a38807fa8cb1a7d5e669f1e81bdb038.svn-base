package com.bdv.infi.model.mesaCambio;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import megasoft.DataSet;
import megasoft.Logger;
import com.bdv.infi.config.Propiedades;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import electric.xml.Node;

public class ReporteOperaciones extends MesaStub {

	String tipoMovimiento = "";
	int movimiento = 0;
	String ordenBCV = "";
	BigDecimal monto_trans_big_decimal;
	BigDecimal montoBase = new BigDecimal(0);
	BigDecimal tasaCambio;
	String statusE;
	String tasa_cambio_ws = "";
	BigDecimal monto_tasa_division;
	String tasa_cambio_wss = "";
	public static final String CODIGO_COMBUSTIBLE = "100";
	HashMap<String, String> parametros;

	public ReporteOperaciones() throws Exception {
		super();

	}

	// definicion de tipo de objetos, para los parametros.
	public String Procesar(String codigoCliente, String nombreCliente, BigDecimal monto_trans_big_decimal, BigDecimal tasaCambio, String codMonedaIso, BigDecimal montoTransaccion, String telefono, String email) {

		try {
//			ordenBCV = Stub.COMPRADIV(tipoMovimiento, codigoCliente, nombreCliente, monto_trans_big_decimal, tasaCambio, codMonedaIso, montoTransaccion, movimiento, "", telefono, email, "");
		} catch (Exception e) {

			System.out.println("ReporteOperaciones : Procesar() " + e);
		}

		return ordenBCV;
	}

	public void ConfiguracionTipoCLiente(String idCliente, String codigoCombustible) {

		// reflexion

		switch (TipoCliente.valueOf(idCliente)) {

		case J:
			tipoMovimiento = ConstantesGenerales.TIPO_DE_MOVIMIENTO_EFEC_JUR;
			movimiento = 11;
		case E:
			tipoMovimiento = ConstantesGenerales.TIPO_DE_MOVIMIENTO_EFEC_EXTRAN;
		case V:
			tipoMovimiento = ConstantesGenerales.TIPO_DE_MOVIMIENTO_EFEC_VEN;
		case P:
			tipoMovimiento = ConstantesGenerales.TIPO_DE_MOVIMIENTO_COMPRA_EFEC_PASAPORTE;
			movimiento = 2;
		case G:
			tipoMovimiento = ConstantesGenerales.TIPO_DE_MOVIMIENTO_COMPRA_EFEC_GOBIERNO;
			movimiento = 12;
		default:
			tipoMovimiento = ConstantesGenerales.TIPO_DE_MOVIMIENTO_EFEC_VEN;

		}

		if (codigoCombustible.equals(CODIGO_COMBUSTIBLE)) {

			movimiento = 18;
			if (idCliente.equals(ConstantesGenerales.TIPPER_ID_PJ)) {
				tipoMovimiento = ConstantesGenerales.TIPO_DE_MOVIMIENTO_EFEC_JUR;
			} else {
				tipoMovimiento = ConstantesGenerales.TIPO_DE_MOVIMIENTO_COMPRA_EFEC_GOBIERNO;
			}
		}
	}

	public void CalculoPorMoneda(String codMonedaIso, BigDecimal montoTransaccion, DataSet _ordenes, String codigoOperacion) {
		try {

			tasa_cambio_ws = parametros.get(ConstantesGenerales.SIGLAS_MONEDA_DOLAR + codigoOperacion.toString());
			tasa_cambio_wss = parametros.get(ConstantesGenerales.SIGLAS_MONEDA_EURO + codigoOperacion.toString());

			if (codMonedaIso.equals(ConstantesGenerales.SIGLAS_MONEDA_DOLAR)) {

				codMonedaIso = ConstantesGenerales.CODIGO_MONEDA_ISO_USD;
				monto_trans_big_decimal = montoTransaccion;

				if (statusE.equals("4")) {
					tasaCambio = new BigDecimal(_ordenes.getValue("TASA_CAMBIO"));
				} else {
					tasaCambio = new BigDecimal(tasa_cambio_ws);
				}

			} else {

				codMonedaIso = ConstantesGenerales.CODIGO_MONEDA_ISO_EUR;

				if (statusE.equals("4")) {

					montoBase = new BigDecimal((_ordenes.getValue("MTO_DIVISAS_TRANS")));
					monto_trans_big_decimal = new BigDecimal(montoBase.toString());
					tasaCambio = new BigDecimal(_ordenes.getValue("TASA_CAMBIO"));
				} else {

					tasaCambio = new BigDecimal(tasa_cambio_wss);
					BigDecimal tasa_cambio_ws_bg = new BigDecimal(tasa_cambio_ws);
					BigDecimal tasa_cambio_wss_bg = new BigDecimal(tasa_cambio_wss);
					// monto_tasa_division = tasa_cambio_wss_bg.divide(tasa_cambio_ws_bg, 8, RoundingMode.DOWN).multiply(montoTransaccion);
					// monto_tasa_division = monto_tasa_division.setScale(4, RoundingMode.DOWN);
					// monto_trans_big_decimal = monto_tasa_division;
					BigDecimal montoFinal = calcularContraValorEurosADolares(tasa_cambio_ws_bg, tasa_cambio_wss_bg, montoTransaccion);
				}

			}
		} catch (Exception e) {
			System.out.println("ReporteOperaciones : CalculoPorMoneda() " + e);
		}

	}

	public BigDecimal calcularContraValorEurosADolares(BigDecimal tasa_cambio_ws_bg, BigDecimal tasa_cambio_wss_bg, BigDecimal montoTransaccion) {

		monto_tasa_division = tasa_cambio_wss_bg.divide(tasa_cambio_ws_bg, 8, RoundingMode.DOWN).multiply(montoTransaccion);
		monto_tasa_division = monto_tasa_division.setScale(4, RoundingMode.DOWN);
		monto_trans_big_decimal = monto_tasa_division;

		return monto_trans_big_decimal;

	}

	enum TipoCliente {
		V, E, J, G, P;
	}

}

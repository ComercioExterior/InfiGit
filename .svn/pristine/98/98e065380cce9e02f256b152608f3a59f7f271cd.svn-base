package com.bdv.infi.logic.interfaz_altair.transaction;

import com.bdv.infi.util.Utilitario;
import com.bdv.infi.webservices.beans.B501;

/**
 * Realiza la operación de débito hacia ALTAIR
 **/
public class Debito extends GenericaTransaccion {

	public Object execute() throws Exception {
		B501 beanAltair = new B501();
		beanAltair.setCodigoCtaCliente(numeroCuenta);
		beanAltair.setImporte(Utilitario.rellenarCaracteres(montoOperacion.toString(), '0', 15, false));
		beanAltair.setCodigoOperacion(codigoOperacion);		
		//beanAltair.setCodigoOperacion("6975");
		beanAltair.setNumeroCheque("000000000");
		beanAltair.setFechaValor("          ");
		beanAltair.setCarta("0");
		beanAltair.setAutorizar("       ");
		beanAltair.setGuardarAnotado(" ");
		beanAltair.setDivisaDeCargo(siglasMoneda);
		beanAltair.setNumeroDeRetencion(Utilitario.rellenarCaracteres(this.numeroRetencion, '0', 5, false));
		beanAltair.setFechaHoraOperOff("           S");
		beanAltair.setFechaDeGiro("         ");
		beanAltair.setLiteral("                    ");
		beanAltair.setNumeroDeLibreta("000000000");
		beanAltair.setSerial(serialContable);
		beanAltair.setNumeroDePlanilla("0000000000000");
		beanAltair.setIndicadorTitular(" ");
		return beanAltair;
	}
}

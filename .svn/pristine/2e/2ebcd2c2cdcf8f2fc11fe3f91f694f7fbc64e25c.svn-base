package com.bdv.infi.logic.interfaz_altair.transaction;

import com.bdv.infi.util.Utilitario;
import com.bdv.infi.webservices.beans.B706;

public class Bloqueo extends GenericaTransaccion {

	public Object execute() throws Exception {
		
		B706 beanAltair = new B706();
		beanAltair.setCodigoCtaCliente(numeroCuenta);
		beanAltair.setImporteRetencion(Utilitario.rellenarCaracteres(montoOperacion.toString(), '0', 15, false));
		beanAltair.setCodigoOperacion(codigoOperacion);		
		//beanAltair.setCodigoOperacion("1501");
		beanAltair.setObservaciones("                                        ");
		beanAltair.setDiasDeRetencion("018");
		beanAltair.setAfectaLiquidacion(" ");
		beanAltair.setFechaVencimiento("          ");
		beanAltair.setFechaVtoBco("          ");
		beanAltair.setDivisa(siglasMoneda);
		
		return beanAltair;
	}

}

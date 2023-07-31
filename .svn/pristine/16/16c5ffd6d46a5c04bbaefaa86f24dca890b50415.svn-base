package com.bdv.infi.logic.interfaz_altair.transaction;

import com.bdv.infi.util.Utilitario;
import com.bdv.infi.webservices.beans.B756;

public class Desbloqueo  extends GenericaTransaccion {

	public Object execute() throws Exception {
	
		B756 beanAltair = new B756();
		beanAltair.setCodigoCtaCliente(numeroCuenta);	
		beanAltair.setDivisa(siglasMoneda);
		beanAltair.setNumeroDeRetencion(numeroRetencion);
		beanAltair.setFechaVencimiento("          ");
		beanAltair.setImporte(Utilitario.rellenarCaracteres(montoOperacion.toString(), '0', 15, false));
		beanAltair.setQuitarEnParte(" ");
		
		return beanAltair;
	}

}

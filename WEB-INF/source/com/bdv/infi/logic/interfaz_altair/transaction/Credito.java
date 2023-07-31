package com.bdv.infi.logic.interfaz_altair.transaction;

import com.bdv.infi.util.Utilitario;
import com.bdv.infi.webservices.beans.BV61;

public class Credito extends GenericaTransaccion {


	public Object execute() throws Exception {
		
		BV61 beanAltair = new BV61();
		beanAltair.setCodigoCtaCliente(numeroCuenta);
		beanAltair.setImporte(Utilitario.rellenarCaracteres(montoOperacion.toString(), '0', 15, false));
		beanAltair.setImportePlanilla(Utilitario.rellenarCaracteres(montoOperacion.toString(), '0', 15, false));
		beanAltair.setImporteEnCheqBV("000000000000000");
		beanAltair.setImporteEnCheqOB("000000000000000");
		beanAltair.setNumeroPlanilla("0000000000000");
		beanAltair.setCodigoOperacion(codigoOperacion);		
		//beanAltair.setCodigoOperacion("2484");
		beanAltair.setFechaValor("          ");
		beanAltair.setCarta("0");
		beanAltair.setAutorizar("       ");
		beanAltair.setGuardarAnotado("N");
		beanAltair.setFechaHoraOperOff("           3");
		beanAltair.setDivisaDeAbono(siglasMoneda);
		beanAltair.setDiasDeRetencion("   ");
		beanAltair.setPorcentajeRetencion("000000");
		beanAltair.setAfectaLiquidacion(" ");
		beanAltair.setSerialCmc7(serialContable);
		beanAltair.setObservaciones("             ");
		beanAltair.setNumeroDeLibreta("000000000");
		return beanAltair;
	}
}

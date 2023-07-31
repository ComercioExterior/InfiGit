package com.bdv.infi.logic.transaction;

import org.apache.axis2.AxisFault;
import org.apache.log4j.Logger;
import org.jibx.runtime.JiBXException;

import com.bancovenezuela.comun.data.OrdenOperacion;
import com.bdv.infi.util.Utilitario;
import com.bdv.infi.webservices.beans.B501;
import com.bdv.infi.webservices.beans.B501Respuesta;
import com.bdv.infi.webservices.beans.BGM501;
import com.bdv.infi.webservices.client.ClienteWs;

/**Realiza la operación de débito hacia ALTAIR*/
public class Debito extends Generica{

	/*** Logger APACHE */
	private Logger logger = Logger.getLogger(Debito.class);
	
	public void procesar(OrdenOperacion ordenOperacion) throws AxisFault,JiBXException{
		B501 debito = new B501();
		debito.setCodigoCtaCliente(ordenOperacion.getNumeroCuenta());
		debito.setImporte(Utilitario.rellenarCaracteres(String.valueOf(ordenOperacion.getMontoOperacion().doubleValue()), '0', 15, false));
		//entrada.setCodigoOperacion("5108");
		debito.setCodigoOperacion("6975");
		debito.setNumeroCheque("000000000");
		debito.setFechaValor("          ");
		debito.setCarta("0");
		debito.setAutorizar("       ");
		debito.setGuardarAnotado(" ");
		debito.setDivisaDeCargo(ordenOperacion.getIdMoneda());
		debito.setNumeroDeRetencion("00000");
		debito.setFechaHoraOperOff("            ");
		debito.setFechaDeGiro("         ");
		debito.setLiteral("                    ");
		debito.setNumeroDeLibreta("000000000");
		//entrada.setSerial("093100379");
		debito.setSerial("000000403");
		debito.setNumeroDePlanilla("0000000000000");
		debito.setIndicadorTitular(" ");

		
		ClienteWs c = new ClienteWs();
		//c.nnamespace = "http://www.bancovenezuela.com/ws/B501/xsd";
		//c.operacion = "getB501";
		//c.urlDelWs = "http://180.183.194.41:34567/infiservices/services/B501";
		
		B501Respuesta salida = (B501Respuesta) c.enviarYRecibir(debito, B501.class, B501Respuesta.class, "NM05679", "12.32.12.43");

		String campo;
		
		try {
			BGM501 bgm501 = salida.getBGM501();
			campo = bgm501.getCodigoCtaCliente();
			logger.info("Campo CodigoCtaCliente:" + campo);
			campo = bgm501.getImporte();
			logger.info("Campo Importe:" + campo);
			campo = bgm501.getCodigoOperacion();
			logger.info("Campo CodigoOperacion:" + campo);
			campo = bgm501.getNumeroCheque();
			logger.info("Campo NumeroCheque:" + campo);
			campo = bgm501.getFechaValor();
			logger.info("Campo FechaValor:" + campo);
			campo = bgm501.getCarta();
			logger.info("Campo Carta:" + campo);
			campo = bgm501.getAutorizar();
			logger.info("Campo Autorizar:" + campo);
			campo = bgm501.getGuardarAnotado();
			logger.info("Campo GuardarAnotado:" + campo);
			campo = bgm501.getDivisaDeCargo();
			logger.info("Campo DivisaDeCargo:" + campo);
			campo = bgm501.getNumeroDeRetencion();
			logger.info("Campo NumeroDeRetencion:" + campo);
			campo = bgm501.getFechaHoraOperOff();
			logger.info("Campo FechaHoraOperOff:" + campo);
			campo = bgm501.getFechaDeGiro();
			logger.info("Campo FechaDeGiro:" + campo);
			campo = bgm501.getLiteral();
			logger.info("Campo Literal:" + campo);
			campo = bgm501.getNumeroDeLibreta();
			logger.info("Campo NumeroDeLibreta:" + campo);
			campo = bgm501.getSerial();
			logger.info("Campo Serial:" + campo);
			campo = bgm501.getNumeroDePlanilla();
			logger.info("Campo NumeroDePlanilla:" + campo);
			campo = bgm501.getIndicadorTitular();
			logger.info("Campo IndicadorTitular:" + campo);
		} catch (Exception e) {
			logger.error("No se pudo obtener la respuesta correctamente: BGM501");
			e.printStackTrace();
		}
	}
}

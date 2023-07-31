package com.bdv.infi.data.beans_swift;

import com.bdv.infi.logic.ValidacionSwift;

/**
 * Cuerpo del mensaje SWIFT 110 con intermediario
 * @author elaucho
 *
 */
public class SwiftConIntermediarioMT110 extends AbstractSwifLT{


	/**
	 * Permite armar el registro
	 * @return
	 * @throws Exception
	 */
	public String getBodyLT() throws Exception {
		ValidacionSwift validacionSwift = new ValidacionSwift();

		bodyLT.append(messageTypes1Header).append(validacionSwift.validarCodigoBIC12X(this.codSwiftBDV)).append(messageTypes1Foot);
		bodyLT.append(messageTypes2HeaderMt110).append(validacionSwift.validarCodigoBIC12X(this.codBancoIntermediario)).append(messageTypes2Foot);
		
		bodyLT.append(messageTypes4Header).append(SALTO_LINEA);	
		
		establecerCampo20();
		
		messageTypes4Body.append(FIELD_STRUCTURE_21).append(this.numeroCheque).append(SALTO_LINEA);
		messageTypes4Body.append(FIELD_STRUCTURE_30).append(this.fechaOperacion).append(SALTO_LINEA);
		messageTypes4Body.append(FIELD_STRUCTURE_32B).append(this.siglasMoneda).append(this.montoOperacion).append(SALTO_LINEA);
		messageTypes4Body.append(FIELD_STRUCTURE_52D).append(this.codBancoIntermediario).append(SALTO_LINEA);
		messageTypes4Body.append(FIELD_STRUCTURE_59).append(this.nombreCliente).append(SALTO_LINEA);	
		
		bodyLT.append(messageTypes4Body.toString());
		bodyLT.append(messageTypes4Foot).append(SALTO_LINEA);
		
		bodyLT.append(caracterEnd).append(SALTO_LINEA);
		
		return bodyLT.toString();
	}

}

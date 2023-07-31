package com.bdv.infi.data.beans_swift;

import com.bdv.infi.logic.ValidacionSwift;

public class SwiftMT110 extends AbstractSwifLT{


	/**
	 * Permite armar el registro
	 * @return
	 * @throws Exception
	 */
	public String getBodyLT() throws Exception {
		//
		ValidacionSwift validacionSwift = new ValidacionSwift();
		
		//System.out.println("Paso la clase SwiftMT110.java, metodo getBodyLT . . . . .");
			
		bodyLT.append(messageTypes1Header).append(validacionSwift.validarCodigoBIC12X(this.codSwiftBDV)).append(messageTypes1Foot);
		bodyLT.append(messageTypes2HeaderMt110).append(validacionSwift.validarCodigoBIC12X(this.headerCodBIC)).append(PRIOTITY).append(messageTypes2Foot);		
		bodyLT.append(messageTypes4Header).append(SALTO_LINEA);	
		
		establecerCampo20();
		messageTypes4Body.append(FIELD_STRUCTURE_72).append(validacionSwift.validar6x35X(BNF72.concat(headerNumeroCuenta))).append(SALTO_LINEA);
		messageTypes4Body.append(FIELD_STRUCTURE_21).append(validacionSwift.validar16X(this.numeroCheque)).append(SALTO_LINEA);
		messageTypes4Body.append(FIELD_STRUCTURE_30).append(this.fechaOperacion).append(SALTO_LINEA);
		messageTypes4Body.append(FIELD_STRUCTURE_32B).append(this.siglasMoneda).append(this.montoOperacion).append(SALTO_LINEA);
		messageTypes4Body.append(FIELD_STRUCTURE_52D).append(validacionSwift.validar35x(validacionSwift.validar6x35X("/"+headerDescripcion.toUpperCase()))).append(SALTO_LINEA);
		messageTypes4Body.append(FIELD_STRUCTURE_59).append(validacionSwift.validar34X(this.nombreCliente.toUpperCase())).append(SALTO_LINEA);
		messageTypes4Body.append(validacionSwift.validar4x35X(direccionCliente[0].toUpperCase())).append(SALTO_LINEA);
		
		bodyLT.append(messageTypes4Body.toString());
		bodyLT.append(messageTypes4Foot).append(SALTO_LINEA);
		
		bodyLT.append(caracterEnd);
		
		return bodyLT.toString();
	}

}

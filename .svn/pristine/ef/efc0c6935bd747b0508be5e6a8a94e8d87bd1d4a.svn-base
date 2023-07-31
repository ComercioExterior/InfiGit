package com.bdv.infi.data.beans_swift;

import megasoft.Logger;
import megasoft.Util;

import com.bdv.infi.logic.ValidacionSwift;

public class SwiftSinIntermedioBIC extends AbstractSwifLT {
	
	/**
	 * Permite armar el registro
	 * @return
	 * @throws Exception
	 */
	public String getBodyLT() throws Exception {
		Logger.debug(this,"SwiftSinIntermedioBIC.getBodyLT");
		ValidacionSwift validacionSwift = new ValidacionSwift();
				
		bodyLT.append(messageTypes1Header).append(validacionSwift.validarCodigoBIC12X(this.codSwiftBDV)).append(messageTypes1Foot);
		bodyLT.append(messageTypes2HeaderMt103).append(validacionSwift.validarCodigoBIC12X(this.headerCodBIC)).append(PRIOTITY).append(messageTypes2Foot);
		bodyLT.append(messageTypes4Header).append(SALTO_LINEA);	
		establecerCampo20();
		messageTypes4Body.append(FIELD_STRUCTURE_23B).append(SALTO_LINEA);
		messageTypes4Body.append(FIELD_STRUCTURE_32A).append(fechaOperacion).append(siglasMoneda).append(montoOperacion).append(SALTO_LINEA);
		
		if(BENOUR.equals("BEN")){
			messageTypes4Body.append(FIELD_STRUCTURE_33B).append(siglasMoneda).append(String.valueOf(currencyInstructedAmount).indexOf(".")>-1?Util.replace(String.valueOf(currencyInstructedAmount), ".", ","):String.valueOf(currencyInstructedAmount).concat(",")).append(SALTO_LINEA);
		}	
		Logger.debug(this,cuentaCliente+nombreCliente+ciudadResideCliente+estadoResideCliente+cedulaCliente);
		//TTS-335: cambio del campo 50K por 50F
		messageTypes4Body.append(FIELD_STRUCTURE_50F).append(validacionSwift.validar34X(cuentaCliente)).append(SALTO_LINEA);
		messageTypes4Body.append("1/").append(validacionSwift.validar34X(nombreCliente.toUpperCase())).append(SALTO_LINEA);
		messageTypes4Body.append("2/").append(validacionSwift.validar34X(ciudadResideCliente)).append(SALTO_LINEA);
		messageTypes4Body.append("3/"+codPaisResideCliente+"/").append(validacionSwift.validar34X(estadoResideCliente)).append(SALTO_LINEA);
		messageTypes4Body.append("7/"+codPaisResideCliente+"/").append(validacionSwift.validar34X(cedulaCliente)).append(SALTO_LINEA);

		//------------------------------------------------------------------------------------------------------
		messageTypes4Body.append(FIELD_STRUCTURE_53B).append(validacionSwift.validar34X(headerNumeroCuenta)).append(SALTO_LINEA);
		messageTypes4Body.append(headerDescripcion).append(SALTO_LINEA);
		//------------------------------------------------------------------------------------------------------

		messageTypes4Body.append(FIELD_STRUCTURE_57A).append(validacionSwift.validar34X(codBancoDestino.toUpperCase())).append(SALTO_LINEA);
		messageTypes4Body.append(FIELD_STRUCTURE_59).append(validacionSwift.validar34X(cuentaDestinatario)).append(SALTO_LINEA);
		messageTypes4Body.append(validacionSwift.validar4x35X(nombreDestinatario.toUpperCase())).append(SALTO_LINEA);
		
		String comisionString = String.valueOf(comision);
		if(String.valueOf(comision).indexOf(".")>-1){comisionString = Util.replace(comisionString, ".", ",");
		}else{comisionString = comisionString.concat(",");}
		
		//TTS-335: cambio del campo 72 por 70
		if(referencia!=null && referencia.length()>1){
			messageTypes4Body.append(FIELD_STRUCTURE_70).append(validacionSwift.validar6x35X(BNF72.concat(referencia.trim()))).append(SALTO_LINEA);
			if(unidadInversion!=null && !unidadInversion.trim().equals("") && unidadInversion.length()>1){
				messageTypes4Body.append("//").append(unidadInversion).append(SALTO_LINEA);
			}
		}else{
			messageTypes4Body.append(FIELD_STRUCTURE_70).append(validacionSwift.validar6x35X(BNF72)).append(SALTO_LINEA);
		}
				
		messageTypes4Body.append(FIELD_STRUCTURE_71A).append(BENOUR).append(SALTO_LINEA);
		
				
		bodyLT.append(messageTypes4Body.toString());
		bodyLT.append(messageTypes4Foot).append(SALTO_LINEA);		
		bodyLT.append(caracterEnd);
				
		return bodyLT.toString();
	}
}
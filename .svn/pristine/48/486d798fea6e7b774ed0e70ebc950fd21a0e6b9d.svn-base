package com.bdv.infi.data.beans_swift;

import java.math.BigDecimal;
import megasoft.Util;
import com.bdv.infi.logic.ValidacionSwift;

public class SwiftConIntermedioSwift extends AbstractSwifLT {

	/**
	 * Permite armar el registro
	 * @return
	 * @throws Exception
	 * 
	 */
	
	//Objeto validacion swift
	private ValidacionSwift validacionSwift = new ValidacionSwift();
	
	public String getBodyLT() throws Exception {
			
		bodyLT.append(messageTypes1Header).append(validacionSwift.validarCodigoBIC12X(this.codSwiftBDV)).append(messageTypes1Foot);
		bodyLT.append(messageTypes2HeaderMt103).append(validacionSwift.validarCodigoBIC12X(this.headerCodBIC)).append(PRIOTITY).append(messageTypes2Foot);
		bodyLT.append(messageTypes4Header).append(SALTO_LINEA);		
		
		//messageTypes4Body.append(FIELD_STRUCTURE_20).append(validacionSwift.validar16X(INFI.concat(String.valueOf(obtenerNumeroAleatorio())))).append(SALTO_LINEA);
		
		establecerCampo20();
		
		messageTypes4Body.append(FIELD_STRUCTURE_23B).append(SALTO_LINEA);
		messageTypes4Body.append(FIELD_STRUCTURE_32A).append(fechaOperacion).append(siglasMoneda).append(montoOperacion).append(SALTO_LINEA);
		
		if(BENOUR.equals("BEN")){
			messageTypes4Body.append(FIELD_STRUCTURE_33B).append(siglasMoneda).append(String.valueOf(currencyInstructedAmount).indexOf(".")>-1?Util.replace(String.valueOf(currencyInstructedAmount), ".", ","):String.valueOf(currencyInstructedAmount).concat(",")).append(SALTO_LINEA);
		}	
		
		/*messageTypes4Body.append(FIELD_STRUCTURE_50F).append(validacionSwift.validar34X(cuentaCliente)).append(SALTO_LINEA);
		messageTypes4Body.append(validacionSwift.validar35x("1/"+nombreCliente.toUpperCase())).append(SALTO_LINEA);
		messageTypes4Body.append(validacionSwift.validar35x("2/"+direccionCliente[0].toUpperCase())).append(SALTO_LINEA);;
		messageTypes4Body.append(validacionSwift.validar35x("3/"+paisResideCliente.toUpperCase())).append(SALTO_LINEA);
		messageTypes4Body.append(validacionSwift.validar35x("7/"+nacionalidadCliente.toUpperCase()+"/"+armarRifCedula(cedulaCliente))).append(SALTO_LINEA);*/
		
		//TTS-368: cambio del campo 50K por 50F (Viene del cambio hecho en TTS-335)
		messageTypes4Body.append(FIELD_STRUCTURE_50F).append(validacionSwift.validar34X(cuentaCliente)).append(SALTO_LINEA);
		messageTypes4Body.append("1/").append(validacionSwift.validar34X(nombreCliente.toUpperCase())).append(SALTO_LINEA);
		messageTypes4Body.append("2/").append(validacionSwift.validar34X(ciudadResideCliente)).append(SALTO_LINEA);
		messageTypes4Body.append("3/"+codPaisResideCliente+"/").append(validacionSwift.validar34X(estadoResideCliente)).append(SALTO_LINEA);
		messageTypes4Body.append("7/"+codPaisResideCliente+"/").append(validacionSwift.validar34X(cedulaCliente)).append(SALTO_LINEA);
		/*Campo 50K
		messageTypes4Body.append(FIELD_STRUCTURE_50K).append(validacionSwift.validar34X(cuentaCliente)).append(SALTO_LINEA);
		messageTypes4Body.append(validacionSwift.validar4x35X(nombreCliente.toUpperCase()+" "+cedulaCliente+" "+direccionCliente[0].toUpperCase())).append(SALTO_LINEA);;
		*/	
		
		//messageTypes4Body.append(validacionSwift.validar4x35X(direccionCliente[0].toUpperCase())).append(SALTO_LINEA);;
		//messageTypes4Body.append(FIELD_STRUCTURE_53D).append(validacionSwift.validar34X(headerNumeroCuenta)).append(SALTO_LINEA);
		//messageTypes4Body.append(validacionSwift.validar4x35X(headerDescripcion.toUpperCase())).append(SALTO_LINEA);
		//------------------------------------------------------------------------------------------------------
		messageTypes4Body.append(FIELD_STRUCTURE_53A).append(validacionSwift.validar34X(headerNumeroCuenta)).append(SALTO_LINEA);
		messageTypes4Body.append(validacionSwift.validarCodigoBIC11X(this.headerCodBIC)).append(SALTO_LINEA);
		//------------------------------------------------------------------------------------------------------
		messageTypes4Body.append(FIELD_STRUCTURE_56A).append(codBancoIntermediario).append(SALTO_LINEA);
		
		//Si el banco intermediario no tiene BIC
		/*if(codBancoIntermediario==null || codBancoIntermediario.trim().equals("")){
			messageTypes4Body.append(FIELD_STRUCTURE_57D).append(validacionSwift.validar34X(numeroCuentaBancoEnIntermediario)).append(SALTO_LINEA);
			messageTypes4Body.append(validacionSwift.validar4x35X(nombreBancoDestino.toUpperCase().concat(" ").concat(direccionBancoDestino!=null?direccionBancoDestino.toUpperCase():""))).append(SALTO_LINEA);
		}*/
		
		armarCampo57A();			
		
		messageTypes4Body.append(FIELD_STRUCTURE_59).append(validacionSwift.validar34X(cuentaDestinatario)).append(SALTO_LINEA);
		messageTypes4Body.append(validacionSwift.validar4x35X(nombreDestinatario)).append(SALTO_LINEA);
		
		String comisionString = String.valueOf(comision);
		if(String.valueOf(comision).indexOf(".")>-1){comisionString = Util.replace(comisionString, ".", ",");
		}else{comisionString = comisionString.concat(",");}
				
		//TTS-368: cambio del campo 72 por 70 (Viene del cambio hecho en TTS-335)
		if(referencia!=null && referencia.length()>1){
			messageTypes4Body.append(FIELD_STRUCTURE_70).append(validacionSwift.validar6x35X(BNF72.concat(referencia.trim()))).append(SALTO_LINEA);
			if(unidadInversion!=null && !unidadInversion.trim().equals("") && unidadInversion.length()>1){
				messageTypes4Body.append("//").append(unidadInversion).append(SALTO_LINEA);
			}
		}else{
			messageTypes4Body.append(FIELD_STRUCTURE_70).append(validacionSwift.validar6x35X(BNF72)).append(SALTO_LINEA);
		}
		
		messageTypes4Body.append(FIELD_STRUCTURE_71A).append(BENOUR).append(SALTO_LINEA);
		if(BENOUR.equals("BEN")){
			messageTypes4Body.append(FIELD_STRUCTURE_71).append(siglasMonedaComision).append(comision==new BigDecimal(0)?"":comisionString).append(SALTO_LINEA);
		}				

		/*TTS-368: Quitar campo 72
		messageTypes4Body.append(FIELD_STRUCTURE_72).append(validacionSwift.validar6x35X(BNF72.concat(referencia))).append(SALTO_LINEA);*/
		
		bodyLT.append(messageTypes4Body.toString());
		bodyLT.append(messageTypes4Foot).append(SALTO_LINEA);
		bodyLT.append(caracterEnd);
		
		return bodyLT.toString();
	}

	/**
	 * Arma el valor que debe estar en el campo 57A
	 * @throws Exception
	 */
	private void armarCampo57A() throws Exception {
		//Al existir el campo 56A (con intermediario) armamos un campo 57A
		
		//si existe el numero de cuenta de banco destino En Banco intermediario se coloca esta cuenta
		//antecedida de una barra,
		//SI este numero de cuenta no esta especificado se coloca solo el codigo BIC del Banco Intermediario
//		if(numeroCuentaBancoEnIntermediario!=null && numeroCuentaBancoEnIntermediario.length()>1){			
//			//se valida a 34 caracteres 
//			messageTypes4Body.append(FIELD_STRUCTURE_57A).append("/"+validacionSwift.validar34X(numeroCuentaBancoEnIntermediario)).append(SALTO_LINEA);
//			messageTypes4Body.append(codBancoIntermediario).append(SALTO_LINEA);
//		}else{			
//			messageTypes4Body.append(FIELD_STRUCTURE_57A).append(codBancoIntermediario).append(SALTO_LINEA);			
//		}
		if(numeroCuentaBancoEnIntermediario!=null && numeroCuentaBancoEnIntermediario.length()>1){			
			//se valida a 34 caracteres 
			messageTypes4Body.append(FIELD_STRUCTURE_57A).append("/"+validacionSwift.validar34X(numeroCuentaBancoEnIntermediario)).append(SALTO_LINEA);
			messageTypes4Body.append(this.codBancoDestino).append(SALTO_LINEA);
		}else{			
			messageTypes4Body.append(FIELD_STRUCTURE_57A).append(codBancoDestino).append(SALTO_LINEA);			
		}
	}

}
package com.bdv.infi.logic.interfaz_ops;

import java.io.File;

import com.bdv.infi.logic.interfaces.ParametrosSistema;


public class CuentaNacionalMonedaExtranjera extends BatchOps{


	public CuentaNacionalMonedaExtranjera() {
		super();
	} 
	
	@Override
	protected boolean verificarCiclo(String ciclo, String status) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}
	
	protected String getCarpetaRespaldo() {
		String carpeta = parametrosOPS.get(ParametrosSistema.RUTA_SITME_MONEDA_EXT_RESP);
		if(!carpeta.endsWith(File.separator)){
			carpeta = carpeta.concat(File.separator);
		}
		return carpeta;
	}
	
	protected File getArchivoRecepcionMonedaExtranjeraSitme() {
		return getArchivo(ParametrosSistema.RUTA_SITME_MONEDA_EXT_RECEP, ParametrosSistema.NOMBRE_ARCH_SITME_MONEDA_EXT);
	}
	
	protected File getArchivoEnvioMonedaExtranjeraSitme() {
		return getArchivo(ParametrosSistema.RUTA_SITME_MONEDA_EXT_ENVIO, ParametrosSistema.NOMBRE_ARCH_SITME_MONEDA_EXT);
	}
	
	/*protected File getArchivoRecepcionMonedaExtranjeraSubasta() {
		return getArchivo(ParametrosSistema.RUTA_VENTA_MONEDA_EXT_RECEP, ParametrosSistema.NOMBRE_ARCH_VENTA_MONEDA_EXT);
	}
	protected File getArchivoEnvioMonedaExtranjeraSubasta() {
		return getArchivo(ParametrosSistema.RUTA_VENTA_MONEDA_EXT_ENVIO, ParametrosSistema.NOMBRE_ARCH_VENTA_MONEDA_EXT);
	}	*/
	
	

}

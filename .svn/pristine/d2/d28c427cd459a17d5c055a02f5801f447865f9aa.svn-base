package com.bdv.infi.logic.interfaz_ops;

import java.io.File;

import com.bdv.infi.logic.interfaces.ParametrosSistema;

public class Adjudicacion extends BatchOps {

	public Adjudicacion() {
		super();
	}

	protected File getArchivoRecepcionAdjudicacionSitme() {
		return getArchivo(ParametrosSistema.RUTA_SITME_ADJ_RECEP, ParametrosSistema.NOMBRE_ARCH_SITME_ADJ);
	}
	protected File getArchivoRecepcionAdjudicacionSubasta() {
		return getArchivo(ParametrosSistema.RUTA_SUBASTA_ADJ_RECEP, ParametrosSistema.NOMBRE_ARCH_SUBASTA_ADJ);
	}
	protected File getArchivoEnvioAdjudicacionSubasta() {
		return getArchivo(ParametrosSistema.RUTA_SUBASTA_ADJ_ENVIO, ParametrosSistema.NOMBRE_ARCH_SUBASTA_ADJ);
	}	
	protected File getArchivoEnvioAdjudicacionSitme() {
		return getArchivo(ParametrosSistema.RUTA_SITME_ADJ_ENVIO, ParametrosSistema.NOMBRE_ARCH_SITME_ADJ);
	}
	
	protected boolean verificarCiclo(String ciclo, String status) throws Exception{
		return false;
	}
}

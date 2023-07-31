package com.bdv.infi.logic.interfaz_ops;

import java.io.File;

import com.bdv.infi.logic.interfaces.ParametrosSistema;

public class Liquidacion extends BatchOps {

	protected String procesoEjecucion;
	protected String tipoCiclo;
	
	public Liquidacion() {
		super();
	} 
	protected String getCarpetaRespaldo() {
		String carpeta = parametrosOPS.get(ParametrosSistema.RUTA_SUBASTA_ADJ_RESPALDO);
		if(!carpeta.endsWith(File.separator)){
			carpeta = carpeta.concat(File.separator);
		}
		return carpeta;
	}

	protected File getArchivoRecepcionLiquidacionSitme() {
		return getArchivo(ParametrosSistema.RUTA_SITME_ADJ_RECEP, ParametrosSistema.NOMBRE_ARCH_SITME_ADJ);
	}
	
	protected File getArchivoRecepcionLiquidacionSubasta() {
		return getArchivo(ParametrosSistema.RUTA_SUBASTA_ADJ_RECEP, ParametrosSistema.NOMBRE_ARCH_SUBASTA_ADJ);
	}
	protected File getArchivoEnvioLiquidacionSubasta() {
		return getArchivo(ParametrosSistema.RUTA_SUBASTA_ADJ_ENVIO, ParametrosSistema.NOMBRE_ARCH_SUBASTA_ADJ);
	}	
	protected File getArchivoEnvioLiquidacionSitme() {
		return getArchivo(ParametrosSistema.RUTA_SITME_ADJ_ENVIO, ParametrosSistema.NOMBRE_ARCH_SITME_ADJ);
	}
	
	protected boolean verificarCiclo(String ciclo, String status) throws Exception{
		return false;
	}
	public String getProcesoEjecucion() {
		return procesoEjecucion;
	}
	public void setProcesoEjecucion(String pasoEjecucion) {
		this.procesoEjecucion = pasoEjecucion;
	}
	public String getTipoCiclo() {
		return tipoCiclo;
	}
	public void setTipoCiclo(String tipoCiclo) {
		this.tipoCiclo = tipoCiclo;
	}
}

package com.bdv.infi.logic.interfaz_ops;

import java.io.File;

import com.bdv.infi.logic.interfaces.ControlProcesosOpsMonedaExtranjera;
import com.bdv.infi.logic.interfaces.ParametrosSistema;

public class AdjudicacionSubastaDivisas extends BatchOps {
	
	protected String tipoCiclo; 
	protected String procesoEjecucion;
	
	public String getTipoCiclo() {
		return tipoCiclo;
	}

	public void setTipoCiclo(String tipoCiclo) {
		this.tipoCiclo = tipoCiclo;
	}

	public AdjudicacionSubastaDivisas() {
		super();
	}

	protected File getArchivoRecepcionAdjudicacionSubastaDivisas() {
		return getArchivo(ParametrosSistema.RUTA_SUBASTA_ADJ_RECEP, ParametrosSistema.NOMBRE_ARCH_SUBASTA_ADJ);
	}
	
	protected File getArchivoEnvioAdjudicacionSubastaSivisas() {
		return getArchivo(ParametrosSistema.RUTA_SUBASTA_ADJ_ENVIO, ParametrosSistema.NOMBRE_ARCH_SUBASTA_ADJ);
	}	
		
	protected boolean verificarCiclo(String ciclo, String status) throws Exception{
		return false;
	}

	public String getProcesoEjecucion() {
		return procesoEjecucion;
	}

	public void setProcesoEjecucion(String procesoEjecucion) {
		this.procesoEjecucion = procesoEjecucion;
	}

}

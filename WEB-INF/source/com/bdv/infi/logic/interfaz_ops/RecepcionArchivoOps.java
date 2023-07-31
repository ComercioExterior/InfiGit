package com.bdv.infi.logic.interfaz_ops;

import java.io.File;

import javax.sql.DataSource;

import com.bdv.infi.logic.interfaces.ControlProcesosOps;
import com.bdv.infi.util.FileUtil;

public class RecepcionArchivoOps extends ProcesadorArchivosOps implements Runnable {
	boolean indProcesoEnvio=false;
	public RecepcionArchivoOps(DataSource _dso, String grupoParamOps, String grupoParamFtp, ControlProcesosOps controlProceso) {
		super(_dso, grupoParamOps, grupoParamFtp,controlProceso);
	}

	public void run() {
		FileUtil fileUtil=new FileUtil();
		
		try {
			parametrosOPS=obtenerParametros(this.grupoParamOps);		
			final File archivo = fileUtil.getArchivo(controlProceso.getNombreRutaRecepcion(),this.parametrosOPS);
			if (archivo.exists()){							
				if (verificarCiclo(this.controlProceso,indProcesoEnvio) &&//VERIFICAR CICLO //TODO tiene que existir un ciclo abierto
						comenzarProceso(this.controlProceso,indProcesoEnvio)) {//TODO Se registra un proceso de Recepcion de Archivo
					
					//parametrosFTP=obtenerParametros(this.grupoParamFtp);
							
					
					
					//RECEPCION ARCHIVO						
					recepcionArchivo(archivo);				
						

					respaldarArchivo(archivo,this.controlProceso,indProcesoEnvio,true);

					cerrarCiclo();
				}
			}else {
				logger.info("Proceso: "+this.controlProceso.getProcesoRecepcion()+" El archivo de recepcion no existe: " + archivo.getAbsolutePath());
			}
		} catch (Exception e) {
			e.printStackTrace();
			proceso.setDescripcionError(e.getMessage());
			try {
				procesoDAO.modificar(proceso);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	@Override
	protected void procesar() {
		// TODO Auto-generated method stub
		
	}
	
}

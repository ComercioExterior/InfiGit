package com.bdv.infi.model.mesaCambio;

import java.util.List;
import megasoft.Logger;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class Anular extends MesaStub {
	String contenido = "";
	String codigoRechazo = "";
	String comentario = "";

	public Anular(){
		super();
	}

	public boolean EjecutarAnular(List<String> operaciones) {
		boolean bandera = false;
		comentario = "Anular por conciliacion";
		int max = operaciones.size();
		for (int i = 0; i < max; i++) {
	
			ProcesarAnuladas(comentario);
			bandera = true;
			
		}
		CrearArchivo(operaciones);
		return bandera;
	}
  
	public void CrearArchivo(List<String> con) {
		try {
			System.out.println("llego CrearArchivo");

			String ruta = "D:/anuladas.txt";
			for (String Contenido : con) {
				System.out.println("Contenido-->" + Contenido);
				this.contenido += Contenido + "\n";
			}

			File file = new File(ruta);
			// Si el archivo no existe es creado
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(this.contenido);
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Anular : CrearArchivo()" + e);
		}
	}

	public String ProcesarAnuladas(String codigoPacto) {

		try {
			long tiempoInicial = System.currentTimeMillis();
			codigoRechazo = Stub.ANULAPACTO(codigoPacto);
			System.out.println("codigoRechazo :" + codigoRechazo);
			long tiempoFinal = System.currentTimeMillis();
			Logger.info("Notificacion", "Ejecutando Anulacion (MESA DE CAMBIO), Tiempo de ejecucion" + (new Double((tiempoFinal - tiempoInicial)) / 1000) + " seg");
			return codigoRechazo;
		} catch (Exception e) {
			System.out.println("Anular : ProcesarAnuladas()" + e);
			Logger.error(this, "Anular : ProcesarAnuladas()" + e);
			return "2";
		}
		
	}

}

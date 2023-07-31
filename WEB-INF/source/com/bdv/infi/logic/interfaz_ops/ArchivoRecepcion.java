package com.bdv.infi.logic.interfaz_ops;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ArchivoRecepcion {
	final File archivo;
	BufferedReader br; 
	
	public ArchivoRecepcion(File archivo) {
		this.archivo = archivo;
	}
	
	public String leerLinea() throws FileNotFoundException, IOException
	{
		if (br == null) {
			br = new BufferedReader(new FileReader(archivo));
		}
		String linea = br.readLine();
		
		// cerrar si no hay mas lineas
		if(linea == null) {
			cerrar();
		}
		
		return linea;
	}

	public void cerrar() throws IOException {
		if( br != null)
			br.close();
	}
}


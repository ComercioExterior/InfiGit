package com.bdv.infi.model.conciliacion;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import javax.sql.DataSource;
import megasoft.AppProperties;
import megasoft.db;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;

public class Archivo {
	protected HashMap<String, String> variablesEntorno;
	public String rOrigen; // ruta
	public String Contenido;
	List<String> list = new ArrayList<String>();
	protected File archivo;
	protected File respaldo;
	protected BufferedReader br;

	public Archivo() {
		iniciarVariables();
	}

	protected void iniciarVariables() {
		try {
			DataSource dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));
			ParametrosDAO parametrosDAO = new ParametrosDAO(dso);
			variablesEntorno = parametrosDAO.buscarParametros(ParametrosSistema.INTERFACE_MENUDEO);
			rOrigen = variablesEntorno.get(ParametrosSistema.RUTA_MENUDEO_CONCILIACION);

		} catch (Exception e) {

		}

	}

	public boolean Verificar(String ruta) {
		archivo = new File(rOrigen + "/" + ruta + ".txt");
		if (!archivo.exists())
			return false;

		return true;
	}
	
	public boolean VerificarGenerico(String ruta) {
		System.out.println("ruta : " + ruta);
		archivo = new File(ruta + ".txt");
		
		if (!archivo.exists())
			return false;

		return true;
	}

	public boolean Escribir(List<String> Listado, String ruta) {
		File TXT = new File(rOrigen + "/" + ruta + ".txt");
		try {
			BufferedWriter wr = new BufferedWriter(new FileWriter(TXT));
			int cant = Listado.size();
			for (int i = 0; i < cant; i++) {
				wr.write(Listado.get(i) + "\n");
				// System.out.println(Listado.get(i));
			}
			wr.close();

		} catch (Exception e) {
			System.out.println("Error al cargar el archivo " + ruta + ".txt");
		}

		return true;
	}
	
	public boolean EscribirGenerico(List<String> Listado, String ruta) {
		File TXT = new File(ruta + ".txt");
		try {
			BufferedWriter wr = new BufferedWriter(new FileWriter(TXT));
			int cant = Listado.size();
			for (int i = 0; i < cant; i++) {
				wr.write(Listado.get(i) + "\n");
				// System.out.println(Listado.get(i));
			}
			wr.close();

		} catch (Exception e) {
			System.out.println("Error al cargar el archivo " + ruta + ".txt");
		}

		return true;
	}

	public List<String> LeerGenerico(String ruta) {
		File TXT = new File(ruta + ".txt");
		System.out.println("LLego");
		System.out.println("Ruta String bdvenlinea : " + ruta);
		System.out.println("TXT : "  + TXT.getAbsolutePath());
		try {
			
			BufferedReader obj = new BufferedReader(new FileReader(TXT));
			  String strng = "";
			  while ((strng = obj.readLine()) != null){
				  System.out.println("LLego1");
			    System.out.println(strng);
			    list.add(strng);
			  }

		} catch (Exception e) {
			System.out.println("Error al leer el archivo " + ruta + ".txt");
		}

		return list;
	}

	/**
	 * Recorre el archivo buscando linea por linea
	 * 
	 * @return List<String>
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public List<String> Listado(String ruta) throws FileNotFoundException, IOException {
		File TXT = new File(rOrigen + "/" + ruta + ".txt");
		BufferedReader br = new BufferedReader(new FileReader(TXT));
		String linea = "";
		while ((linea = br.readLine()) != null) {
			list.add(linea);
		}
		br.close();
		return list;
	}
	
	public List<String> ListadoGenerico(String ruta) throws FileNotFoundException, IOException {
		File TXT = new File(ruta + ".txt");
		BufferedReader br = new BufferedReader(new FileReader(TXT));
		String linea = "";
		while ((linea = br.readLine()) != null) {
			list.add(linea);
		}
		br.close();
		return list;
	}

	public void cerrar() throws IOException {
		if (br != null)
			br.close();
	}
}
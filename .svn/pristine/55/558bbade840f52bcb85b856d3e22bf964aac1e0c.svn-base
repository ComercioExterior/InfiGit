package com.bdv.infi.model.conciliacion;

import java.util.ArrayList;
import java.util.List;

/**
 * @todo Archivo de moneda mediante un XML(WEB SERVICE) del Banco de Venezuela, https
 * 
 * @author nm11383
 * 
 */
public class BDV extends Thread {
	List<String> listaClientesMenudeoBeta;
	List<String> lst = new ArrayList<String>();

	@Override
	public void run() {

			Archivo archivo = new Archivo();
			System.out.println("Llego al hilo BDV");
			if (archivo.VerificarGenerico("D:/pruebaConciliacion/bdvDigital/archivo")) { // Verificar Existencia Archivo
				//archivo.leerLinea();
				System.out.println("Llego al hilo BDV1");
				lst = archivo.LeerGenerico("D:/pruebaConciliacion/bdvDigital/archivo");
				System.out.println("Archivo Creado");
//				simularDatos();
			}else{
				System.out.println("Creando simulacion de datos BDV creado");
//				simularDatos();
			}
			
		
	}

	

	public List<String> Lista() {
		return lst;
	}

	
	public void simularDatos(){
		
		this.lst.add("202109201433052VWEW8;V16854463;0;1000;20-09-2021");
		this.lst.add("20210818090010C53V7B;V17522252;20210818090010C53V8R;10.30;20-09-2021");
		this.lst.add("20210818090010C53V7C;V17522253;20210818090010C53V9R;10.20;20-09-2021");
		this.lst.add("20210818090010C53V7D;V17522254;20210818090010C53V6R;1.10;20-09-2021");
//		this.lst.add("22045795");
//		this.lst.add("22045796");		
		
	}
}

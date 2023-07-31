package com.bdv.infi.model.inventariodivisas;

import javax.sql.DataSource;

public class Movimiento extends Oficina{

	public String id;
	public String fecha;
	public String monto;
	public String responsable;
	
	
	public Movimiento(DataSource ds) {
		super(ds);
		// TODO Auto-generated constructor stub
	}

	
	
	
	
	public void Listarr() {
		
	}
	
	
	//Registar un movimiento
	public void Registrar(){
		
	}
	
	
	
}

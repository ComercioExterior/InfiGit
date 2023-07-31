package com.bdv.infi.model.menudeo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import megasoft.Logger;

import com.bdv.infi.data.ClienteMenudeo;

public class Vender implements Notificar{
	
	public List<Monedas> LstMonedas = new ArrayList<Monedas>();
	
	public void Reportar(List<ClienteMenudeo> lst,DataSource ds) {
		for (ClienteMenudeo clt : lst) {
			System.out.println(clt.Cedula);
			boolean err = false;
			if (!err) this.FallaConexion("Err.#001 ");
		}

	}

	public void Rechazados() {
		// TODO Auto-generated method stub
		
	}

	public boolean Enviadas() {
		// TODO Auto-generated method stub
		return true;
	}

	public void FallaConexion(String err) {
		// TODO Auto-generated method stub
		Logger.error(this, err);
		
	}

	public BigDecimal Conversion(String tipoMoneda, BigDecimal valor) {
		// TODO Auto-generated method stub
		return null;
	}

	public Monedas obtenerMoneda(String tipoModena) {
		// TODO Auto-generated method stub
		return null;
	}
}
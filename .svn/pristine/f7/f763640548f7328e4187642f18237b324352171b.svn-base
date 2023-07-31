package com.bdv.infi.model.mesaCambio;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import megasoft.Logger;
import com.bdv.infi.data.ClientesMesa;

public class Vender implements Notificar{
	
//	public List<Monedas> LstMonedas = new ArrayList<Monedas>();
	
	public Boolean Reportar(List<ClientesMesa> lst,DataSource ds) {
		for (ClientesMesa clt : lst) {
			System.out.println(clt.CedulaRifDemanda);
			boolean err = false;
			if (!err) this.FallaConexion("Err.#001 ");
		}
		return null;

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

	public void Conversion(String tipoMoneda, BigDecimal valor) {
		// TODO Auto-generated method stub
//		return null;
	}

	public void Conversion(String tipoMoneda, BigDecimal valor, BigDecimal tasapacto) {
		// TODO Auto-generated method stub
		
	}

//	public Monedas obtenerMoneda(String tipoModena) {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
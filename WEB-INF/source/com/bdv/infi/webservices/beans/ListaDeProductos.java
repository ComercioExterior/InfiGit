package com.bdv.infi.webservices.beans;

import java.util.ArrayList;

public class ListaDeProductos {
	private ListaDeCuentas listaDeCuentas = null;
	private ListaDeTarjetasDeCredito listaDeTarjetasDeCredito = null;
	private ListaDeCreditos listaDeCreditos = null;
	
	public ListaDeProductos() {
		this.listaDeCuentas = new ListaDeCuentas();
		this.listaDeCuentas.setCuentas(new ArrayList<Cuenta>());
		
		this.listaDeTarjetasDeCredito = new ListaDeTarjetasDeCredito();
		this.listaDeTarjetasDeCredito.setTarjetas(new ArrayList<TarjetaDeCredito>());
		
		this.listaDeCreditos = new ListaDeCreditos();
		this.listaDeCreditos.setCreditos(new ArrayList<Credito>());
	}

	public ListaDeCreditos getListaDeCreditos() {
		return listaDeCreditos;
	}

	public void setListaDeCreditos(ListaDeCreditos listaDeCreditos) {
		this.listaDeCreditos = listaDeCreditos;
	}

	public ListaDeCuentas getListaDeCuentas() {
		return listaDeCuentas;
	}

	public void setListaDeCuentas(ListaDeCuentas listaDeCuentas) {
		this.listaDeCuentas = listaDeCuentas;
	}

	public ListaDeTarjetasDeCredito getListaDeTarjetasDeCredito() {
		return listaDeTarjetasDeCredito;
	}

	public void setListaDeTarjetasDeCredito(
			ListaDeTarjetasDeCredito listaDeTarjetasDeCredito) {
		this.listaDeTarjetasDeCredito = listaDeTarjetasDeCredito;
	}
}

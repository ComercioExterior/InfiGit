package com.bdv.infi.model.inventariodivisas;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import megasoft.Logger;

import com.bdv.infi.dao.InventarioDivisasDAO;
import com.bdv.infi.webservices.beans.InventarioEntrada;
import com.bdv.infi.webservices.beans.InventarioIntervencionDTO;
import com.bdv.infi.webservices.beans.InventarioRespuesta;
import com.bdv.infi.webservices.beans.InventarioRespuestaList;
import com.bdv.infi.webservices.client.ClienteWs;

class Inventario extends Oficina {
	private ServletContext contexto = null;
	ArrayList<InventarioIntervencionDTO> listaInventario;
	
	public Inventario(DataSource ds) {
		super(ds);
		// TODO Auto-generated constructor stub
	}


	//CARGA DEL SOAP MODULO Y REGISTRAR EN BASE DE DATOS
	public void Iniciar(String username, String ip) {
		this.listaInventario = new ArrayList<InventarioIntervencionDTO>();
		ArrayList<InventarioRespuesta> lst = null;
		InventarioRespuestaList respuesta = null;
		InventarioEntrada entrada = new InventarioEntrada();
		try {			
			ClienteWs cliente = ClienteWs.crear("getINVENTARIO", contexto);
			respuesta = (InventarioRespuestaList) cliente.enviarYRecibir(entrada, InventarioEntrada.class, InventarioRespuestaList.class, username, ip);
			lst = respuesta.getList();
			
			Iterator iteracion = lst.iterator();

			while (iteracion.hasNext()) {
				InventarioIntervencionDTO inventarioIntervencion = new InventarioIntervencionDTO();
				//Insertar con el Objeto General
				this.Id = "";
				this.Fecha = "";
				this.Monto = new BigDecimal(0.00);
				this.RegistarInventario();
			}
		} catch (Exception e) {
			System.out.println("Inventario : BuscarInventario() " + e);
			Logger.error(this, "Inventario : BuscarInventario() " + e);

		}
	}
	
}
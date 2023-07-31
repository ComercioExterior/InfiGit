package com.bdv.infi.model.intervencion;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletContext;
import javax.sql.DataSource;
import megasoft.AppProperties;
import megasoft.Logger;
import megasoft.db;
import com.bdv.infi.dao.IntervencionDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.ftp.FTPUtil;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.webservices.beans.CredencialesDeUsuario;
import com.bdv.infi.webservices.beans.InventarioEntrada;
import com.bdv.infi.webservices.beans.InventarioIntervencionDTO;
import com.bdv.infi.webservices.beans.InventarioRespuesta;
import com.bdv.infi.webservices.beans.InventarioRespuestaList;
import com.bdv.infi.webservices.client.ClienteWs;

public class Inventario {

	private ServletContext contexto = null;
	private CredencialesDeUsuario credenciales = null;
	ArrayList<InventarioIntervencionDTO> listaInventario;
	DataSource dso;
	HashMap<String, String> parametrosIntervencion = new HashMap<String, String>();

	public Inventario(ServletContext cont, CredencialesDeUsuario credenciales) {
		this.contexto = cont;
		this.credenciales = credenciales;
		
		try {
			this.dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));
			
		} catch (Exception e) {
			System.out.println("Inventario : Inventario() " + e);
			Logger.error(this, "Inventario : Inventario() " + e);
			
		}		
	}

	public ArrayList<InventarioIntervencionDTO> BuscarInventario(String username, String ip) {

		this.listaInventario = new ArrayList<InventarioIntervencionDTO>();
		InventarioRespuestaList respuesta = null;
		ArrayList<InventarioRespuesta> lst = null;
		InventarioEntrada entrada = new InventarioEntrada();
		InventarioRespuesta inventarioRespuesta = null;

		try {
			
			ClienteWs cliente = ClienteWs.crear("getINVENTARIO", contexto);
			respuesta = (InventarioRespuestaList) cliente.enviarYRecibir(entrada, InventarioEntrada.class, InventarioRespuestaList.class, username, ip);
			lst = respuesta.getList();

			Iterator iteracion = lst.iterator();

			while (iteracion.hasNext()) {
				InventarioIntervencionDTO inventarioIntervencion = new InventarioIntervencionDTO();

				inventarioIntervencion.setOficinaId("1");
				inventarioIntervencion.setInventarioId("1");
				inventarioIntervencion.setFecha("18-10-2021");
				inventarioIntervencion.setMoneda("USD");
				inventarioIntervencion.setMontoAsignado("100000");
				inventarioIntervencion.setMontoPorcentaje("80");
				inventarioIntervencion.setMontoOriginal("1000000");
				inventarioIntervencion.setMontoConsumido("1000");
				inventarioIntervencion.setMontoDisponible("10000");
				inventarioIntervencion.setEstatus("1");
				listaInventario.add(inventarioIntervencion);

			}
		} catch (Exception e) {
			System.out.println("Inventario : BuscarInventario() " + e);
			Logger.error(this, "Inventario : BuscarInventario() " + e);

		}
		return listaInventario;
	}

	public void guardarInventario() {
		try {
			
			IntervencionDAO intervencionDao = new IntervencionDAO(dso);
			ArrayList<InventarioIntervencionDTO> inventarioInsert = BuscarInventario("", "");
			for (InventarioIntervencionDTO lst : inventarioInsert) {

				String sql = intervencionDao.guardarInventario(lst.getOficinaId(), lst.getInventarioId(), lst.getFecha(), 
						lst.getMoneda(), lst.getMontoAsignado(), lst.getMontoPorcentaje(), lst.getMontoOriginal(), lst.getMontoConsumido(), 
						lst.getMontoDisponible(), lst.getEstatus());
				
				db.exec(dso, sql.toString());

			}

		} catch (Exception e) {
			System.out.println("Inventario : guardarInventario() " + e);
			Logger.error(this, "Inventario : guardarInventario() " + e);

		}
	}

	public void envioFpt(String archivoOriginal, String archivoFinal) {

		try {
			obtenerParametros();
			archivoFinal = "'" + archivoFinal + "'";
			Logger.info(this, "Archivo origen " + archivoOriginal + " - " + "Archivo destino " + archivoFinal);
			
			FTPUtil ftpUtil = new FTPUtil(parametrosIntervencion.get(ParametrosSistema.DIRECCION_FTP_INTER), this.dso);
			ftpUtil.putFTPAscci(archivoOriginal, archivoFinal, "", false);

		} catch (Exception e) {
			System.out.println("Inventario : envioFpt() " + e);
			Logger.error(this, "Inventario : envioFpt() " + e);
			
		}
	}
	
	
	public boolean Escribir(List<InventarioIntervencionDTO> Listado, String ruta ) {
//		File TXT = new File(rOrigen + "/" + ruta + ".txt");
		File TXT = new File(parametrosIntervencion.get(ParametrosSistema.RUTA_ARCHIVO_FPT));
		try {
			BufferedWriter wr = new BufferedWriter(new FileWriter(TXT));
			
			for (InventarioIntervencionDTO inventario : Listado) {
				wr.write(inventario.getOficinaId()+";"+inventario.getInventarioId()+";"+inventario.getMoneda()+";"+inventario.getEstatus()+";"
						+inventario.getFecha()+";"+inventario.getMontoAsignado()+";"+inventario.getMontoConsumido()+";"+inventario.getMontoDisponible()+";"
						+inventario.getMontoOriginal()+";"+inventario.getMontoPorcentaje()+ "\n");
				
			}
			wr.close();
			
		} catch (Exception e) {
			System.out.println("Error al cargar el archivo " + ruta + ".txt");
		}

		return true;
	}

	
	protected void obtenerParametros() throws Exception {
		ParametrosDAO parametrosDAO = new ParametrosDAO(dso);
		parametrosIntervencion = parametrosDAO.buscarParametros(ParametrosSistema.INTERFACE_INTERVENCION_BANCARIA);
	}
}

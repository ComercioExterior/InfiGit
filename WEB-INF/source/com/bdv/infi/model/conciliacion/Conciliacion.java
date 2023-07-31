package com.bdv.infi.model.conciliacion;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import java.io.File;
import java.io.IOException;
//import java.nio.file.FileSystems;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.StandardCopyOption;
import java.sql.SQLException;

import javax.sql.DataSource;

import megasoft.AppProperties;
import megasoft.Logger;
import megasoft.db;
import models.correo.Correo;

import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.util.FileUtil;

/**
 * Conciliar archivos de terceros para luego hallar diferencias externas, luego comparar la estructura con moneda extrajera(INFI)
 * 
 * @author nm11383
 */
public class Conciliacion {

	List<String> lstBcv;
	List<String> lstBdv;
	List<String> lstMe;

	List<String> lstDiferencia = new ArrayList<String>();
	List<String> lstIguales = new ArrayList<String>();
	boolean generarArchivo = false;
	boolean intentosComparacion = true;
	protected HashMap<String, String> parametrosMenudeBCV;
	protected Archivo archivo;
	public String Fecha = "";
	DataSource dso;
	public String ContenidoCorreo = "";
	ParametrosDAO parametrosDAO;

	public Conciliacion() {
		this.archivo = new Archivo();
		try {
			dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));
			obtenerParametros();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void CargarDiferencia() throws Exception {

		BCV Bcv = new BCV();
		ME Me = new ME();
		Bcv.start(); // iniciando los hilos
		Me.start();
		while (Bcv.isAlive() || Me.isAlive()) {
			// Esperando Finalizar
		}
		this.lstBcv = Bcv.Lista();
		this.lstMe = Me.Lista();

		generarArchivo = true;
		Diferencia(lstBcv, lstMe);

	}

	public void impimirLista(List<String> lst) {
		int cant = lst.size();
		for (int i = 0; i < cant; i++) {
			System.out.println(lst.get(i));
		}
	}

	/**
	 * Evalua el tamaño de las listas para establecer el orden de lectura en la matriz I,J
	 * 
	 * @param XH
	 *            List<String>
	 * @param XI
	 *            List<String>
	 * @throws Exception
	 */
	public void Diferencia(List<String> XH, List<String> XI) throws Exception {
		int a = XH.size();
		int b = XI.size();
		int indicadorBCV = 1;
		int indicadorInfi = 2;
		if (a > b) {
			execComparacion(XI, XH, b, a, indicadorInfi, indicadorBCV);

		} else {
			execComparacion(XH, XI, a, b, indicadorBCV, indicadorInfi);
		}
	}

	/**
	 * Permite ejecutar las comparaciones de los arreglos teniendo en cuenta el mayor de los dos al mismo tiempo que crea una lista de diferencia y de iguales
	 * 
	 * @param min
	 *            List<String>
	 * @param max
	 *            List<String>
	 * @param iCant
	 *            int
	 * @param jCant
	 *            int
	 * @throws Exception
	 */
	public void execComparacion(List<String> min, List<String> max, int iCant, int jCant, int indicadorMin, int indicadorMax) throws Exception {
		System.out.println("");
		this.lstDiferencia = new ArrayList<String>();
		for (int i = 0; i < iCant; i++) {
			boolean existe = false;
			int jMax = max.size();
			for (int j = 0; j < jMax; j++) {
				if (min.get(i).equals(max.get(j))) { // El elemento A[i] Se encontro en B[j]
					max.remove(j);
					if (this.intentosComparacion)
						this.lstIguales.add(min.get(i));
					existe = true;
					break;
				}
			}// fin J
			if (!existe && this.intentosComparacion)
				lstDiferencia.add(min.get(i) + ";" + indicadorMin);
		}// fin I
			// marcamos los elementos sin coincidencias de la lista mas grande
		for (int j = 0; j < max.size(); j++) {
			lstDiferencia.add(max.get(j) + ";" + indicadorMax);
		}

		if (generarArchivo) {
			this.archivo.EscribirGenerico(this.lstDiferencia, parametrosMenudeBCV.get(ConstantesGenerales.RUTA_MENUDEO_CON) + "diferencia");
			this.archivo.EscribirGenerico(this.lstIguales, parametrosMenudeBCV.get(ConstantesGenerales.RUTA_MENUDEO_CON) + "iguales");
		}
		Operaciones operacion = new Operaciones();
		operacion.Salvar(this.lstDiferencia);
		respaldarArchivo();
		modificarParametro();
		envioCorreo();
	}

	/**
	 * Generar la ultima comparacion de dos listas la del BDV con el producto de las diferencias mas el factor de los iguales
	 * 
	 * @throws Exception
	 * 
	 */
	public void Conciliar() throws Exception {
		Operaciones operacion = new Operaciones();
		operacion.AnularPorItem(this.Fecha, null);
	}

	public List<String> ListaDiff() {
		return this.lstDiferencia;
	}

	/**
	 * 
	 */
	public void ReportarAnulaciones(String fecha, String id) {
		Operaciones operacion = new Operaciones();
		operacion.AnularPorItem(fecha, id);
	}

	public void respaldarArchivo() throws Exception {

		Date fechaSistema = new Date();
		SimpleDateFormat formato = new SimpleDateFormat("ddMMyyyy-HHmmss");
		String fecha = formato.format(fechaSistema);
		
		File pruba = new File(parametrosMenudeBCV.get(ConstantesGenerales.RUTA_MENUDEO_CON) + "iguales.txt");
		File prubaRes = new File(parametrosMenudeBCV.get(ConstantesGenerales.RUTA_MENUDEO_CONR) + "iguales" + fecha + ".txt");
		File pruba1 = new File(parametrosMenudeBCV.get(ConstantesGenerales.RUTA_MENUDEO_CON) + "diferencia.txt");
		File prubaRes1 = new File(parametrosMenudeBCV.get(ConstantesGenerales.RUTA_MENUDEO_CONR) + "diferencia" + fecha + ".txt");
		Logger.info(this, "pruba" + pruba);
		Logger.info(this, "prubaRes" + prubaRes);
		
		try {
			
			FileUtil.copiarArchivo(pruba, prubaRes);
			FileUtil.delete(parametrosMenudeBCV.get(ConstantesGenerales.RUTA_MENUDEO_CON) + "iguales.txt");
			FileUtil.copiarArchivo(pruba1, prubaRes1);
			FileUtil.delete(parametrosMenudeBCV.get(ConstantesGenerales.RUTA_MENUDEO_CON) + "diferencia.txt");
		} catch (IOException e) {
			
			System.out.println("Configuracion : respaldarArchivo() " + e);
			Logger.error(this, "Configuracion :  respaldarArchivo() " + e);
		}

	}

	public void envioCorreo() {

		this.ContenidoCorreo = "TIEMPO TOTAL DE EJECUCION Comprar:Reportar()  ms. \n";

		this.ContenidoCorreo += "\n\n";
		this.ContenidoCorreo += "PORCENTAJES DE CONCILIACION EXITOSO \n";
		System.out.println("PORCENTAJES DE CONCILIACION EXITOSO \n");

		this.ContenidoCorreo += "PORCENTAJES DE CONCILIACION FALLIDO \n";
		System.out.println("PORCENTAJES DE CONCILIACION FALLIDO \n");

		this.ContenidoCorreo += "\n\n";
		this.ContenidoCorreo += "--------------------------------------- \n";
		this.ContenidoCorreo += "BANCO DE VENEZUELA \n";
		this.ContenidoCorreo += "--------------------------------------- \n";

		Correo correo = new Correo(ContenidoCorreo, "RESUMEN DE CONCILIACION", "infi.bdv.soporte@bdv.enlinea.com", "angel_jesus_herrera@banvenez.com", dso);
		try {
			correo.Enviar();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	public void modificarParametro() throws Exception {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		String formattedDate = formatter.format(date);
		this.parametrosDAO.modificarParametroPorGrupo(formattedDate, "135", "FECHA_LECTURA ");
	}

	protected void obtenerParametros() {

		try {

			this.parametrosDAO = new ParametrosDAO(dso);
			parametrosMenudeBCV = parametrosDAO.buscarParametros(ParametrosSistema.INTERFACE_MENUDEO);

		} catch (Exception e) {

			System.out.println("Conciliacion : obtenerParametros() " + e);
		}

	}
}

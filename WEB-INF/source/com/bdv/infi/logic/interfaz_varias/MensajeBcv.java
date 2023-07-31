package com.bdv.infi.logic.interfaz_varias;

import javax.sql.DataSource;

import megasoft.DataSet;

import com.bdv.infi.dao.TitulosDAO;
import com.bdv.infi.util.Utilitario;

/**
 * Mensaje con los datos que se deben mandar a la interfaz de bcv
 * @author nvisbal
 *
 */public class MensajeBcv extends Mensaje {
	public final String SECUENCIA = "SECUENCIA";
	public final String NOMBRE_RAZON_SOCIAL = "NOMBRE_RAZON_SOCIAL";
	public final String RIF_LETRA = "RIF_LETRA";
	public final String RIF_NUMERO = "RIF_NUMERO";
	public final String TITULO = "TITULO";
	public final String BROKER = "BROKER";
	public final String OPERADOR_COMPRA = "OPERADOR_COMPRA";
	public final String OPERADOR_VENTA = "OPERADOR_VENTA";
	public final String FECHA_OPERACION = "FECHA_OPERACION";
	public final String FECHA_VALOR = "FECHA_VALOR";
	public final String PRECIO_TITULO = "PRECIO_TITULO";
	public final String MONTO_DOLARES = "MONTO_DOLARES";
	public final String CONCEPTO = "CONCEPTO";
	public final String SECTOR = "SECTOR";
	public static final String TIPO_MENSAJE = "MENSAJE_BCV";
	
	public MensajeBcv() {
		super(TIPO_MENSAJE);
		this.set(SECUENCIA, VALOR_VACIO);
		this.set(NOMBRE_RAZON_SOCIAL, VALOR_VACIO);
		this.set(RIF_LETRA, VALOR_VACIO);
		this.set(RIF_NUMERO, VALOR_VACIO);
		this.set(TITULO, VALOR_VACIO);
		this.set(BROKER, VALOR_VACIO);
		this.set(OPERADOR_COMPRA, VALOR_VACIO);
		this.set(OPERADOR_VENTA, VALOR_VACIO);
		this.set(FECHA_OPERACION, VALOR_VACIO);
		this.set(FECHA_VALOR, VALOR_VACIO);
		this.set(PRECIO_TITULO, VALOR_VACIO);
		this.set(MONTO_DOLARES, VALOR_VACIO);
		this.set(CONCEPTO, VALOR_VACIO);
		this.set(SECTOR, VALOR_VACIO);
	}	

	public void formatearCampos() {
		this.set(SECUENCIA, Utilitario.rellenarCaracteres(this.get(SECUENCIA), '0', 5, false));
		this.set(NOMBRE_RAZON_SOCIAL, Utilitario.rellenarCaracteres(this.get(NOMBRE_RAZON_SOCIAL), ' ', 40, true, false));
		this.set(RIF_LETRA, Utilitario.rellenarCaracteres(this.get(RIF_LETRA), ' ', 1, false));
		this.set(RIF_NUMERO, Utilitario.rellenarCaracteres(this.get(RIF_NUMERO), '0', 14, false));
		this.set(TITULO, Utilitario.rellenarCaracteres(this.get(TITULO).trim(), ' ', 25, false,true));
		this.set(BROKER, Utilitario.rellenarCaracteres(this.get(BROKER), ' ', 4, false));
		this.set(OPERADOR_COMPRA, Utilitario.rellenarCaracteres(this.get(OPERADOR_COMPRA), ' ', 3, false));
		this.set(OPERADOR_VENTA, Utilitario.rellenarCaracteres(this.get(OPERADOR_VENTA), ' ', 3, false));
		this.set(FECHA_OPERACION, Utilitario.rellenarCaracteres(this.get(FECHA_OPERACION), '0', 8, false));
		this.set(FECHA_VALOR, Utilitario.rellenarCaracteres(this.get(FECHA_VALOR), '0', 8, false));
		this.set(PRECIO_TITULO, Utilitario.rellenarCaracteres(formatearNumeroConReemplazo(this.get(PRECIO_TITULO)), '0', 8, false));
		this.set(MONTO_DOLARES, Utilitario.rellenarCaracteres(formatearNumeroConReemplazo(this.get(MONTO_DOLARES)), '0', 16, false));
		this.set(CONCEPTO, Utilitario.rellenarCaracteres(this.get(CONCEPTO), '0', 3, false));
		this.set(SECTOR, Utilitario.rellenarCaracteres(this.get(SECTOR), '0', 4, false));		
	}
	
	public String obtenerLinea() {
		StringBuilder sb = new StringBuilder();
		formatearCampos();
		for (String clave : this.getDetalle().keySet()) {
			sb.append(this.get(clave));
		}
		return sb.toString();
	}
	
	/**
	 * Obtiene el valor final del campo TITULO  
	 * @param _dso datasource
	 * @param idTitulo id del título a buscar
	 * @return el valor final que debe ir en el campo TITULO
	 * @throws Exception en caso de error
	 */
	public String obtenerValorFinalTitulo(DataSource _dso, String idTitulo) throws Exception{
		String valorFinal = "";
		TitulosDAO titulosDao = new TitulosDAO(_dso);
		titulosDao.listarTituloEquivalenciaBCV(idTitulo);
		DataSet ds = titulosDao.getDataSet();
		if (ds.count()> 0){
			ds.next();
			valorFinal = ds.getValue("nombre_bcv");	
		}else{
			valorFinal = idTitulo;
		}
		return valorFinal;
	}	
}
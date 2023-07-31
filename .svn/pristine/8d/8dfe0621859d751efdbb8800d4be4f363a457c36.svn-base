package com.bdv.infi.logic.interfaz_varias;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import javax.sql.DataSource;

import megasoft.DataSet;

import org.apache.log4j.Logger;

import com.bdv.infi.dao.ClienteCuentasDAO;
import com.bdv.infi.dao.MensajeDAO;
import com.bdv.infi.data.Orden;
import com.bdv.infi.data.OrdenTitulo;


/**
 * Mensaje con los datos que se deben mandar a la interface con carmen
 * @author nvisbal
 *
 */
public class MensajeCarmen extends Mensaje {
		
	public final String CODIGO_CLIENTE = "CODIGO_CLIENTE";
	public final String CODIGO_CUENTA = "CODIGO_CUENTA";
	public final String CLAVE_VALOR = "CLAVE_VALOR";
	public final String CANTIDAD = "CANTIDAD";
	public final String FECHA_OPERACION = "FECHA_OPERACION";
	public final String FECHA_LIQUIDACION = "FECHA_LIQUIDACION";
	public final String CONTRAPARTE = "CONTRAPARTE";
	public static final String TIPO_MENSAJE = "MENSAJE_CARMEN";
	public static final String ENTRADA = "_ENTRADA";
	public static final String SALIDA = "_SALIDA";
	
	private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
	private DecimalFormat nf = new DecimalFormat("###.##");
	private Logger logger = Logger.getLogger(MensajeCarmen.class);
	
	public MensajeCarmen() {
		super(TIPO_MENSAJE);
		this.set(CODIGO_CLIENTE, VALOR_VACIO);
		this.set(CODIGO_CUENTA, VALOR_VACIO);
		this.set(CLAVE_VALOR, VALOR_VACIO);
		this.set(CANTIDAD, VALOR_VACIO);
		this.set(FECHA_OPERACION, VALOR_VACIO);
		this.set(FECHA_LIQUIDACION, VALOR_VACIO);
		this.set(CONTRAPARTE, VALOR_VACIO);
	}
	
	@Override
	protected void formatearCampos() {		
		if (this.get(CLAVE_VALOR).indexOf(" ")>0){
			this.set(CLAVE_VALOR, "'" + this.get(CLAVE_VALOR) + "'");	
		}
	}
	
	/**
	 * Asigna a la clave una fecha con el formato DD/MM/YY
	 * @param clave parámetro
	 * @param fecha fecha que se asignará al parámetro
	 */
	public void set(String clave, Date fecha) {
		this.valores.put(clave, sdf.format(fecha));
	}
	
	public void set(String clave, Double numero) {
		this.valores.put(clave, String.valueOf(nf.format(numero)));
	}
	
	/**Obtiene los campos en la forma requerida por la interfaz*/
	public String obtenerLinea(){
		StringBuilder sb = new StringBuilder();
		formatearCampos();
		for (String clave : this.getDetalle().keySet()) {
			sb.append(this.get(clave)).append(" ");
		}
		return sb.toString().substring(0,sb.length()-1);
	}
	
	/**
	 * Genera el mensaje para Carmen
	 * @throws Exception en caso de error
	 */
	@SuppressWarnings("unused")
	public ArrayList<String> generarMensajeCarmen(Orden orden, String usuario, DataSource dso, int contraparteBDV, HashMap<String, String> valoresPorDefecto) throws Exception {
		ClienteCuentasDAO cuentaCustodiaDAO = new ClienteCuentasDAO(dso);
		MensajeDAO mensajeDAO = new MensajeDAO(dso);
		DataSet _cuentaCustodia = null;
		ArrayList<String> querys = new ArrayList<String>();
		
		logger.info("generarMensajeCarmen---> antes de obtener cuenta custodia");
		//OBTENER CUENTA CUSTODIA
		_cuentaCustodia = cuentaCustodiaDAO.getCuentaCustodia(orden.getIdCliente());
		if (_cuentaCustodia.count() > 0) {
			_cuentaCustodia.first();
			_cuentaCustodia.next();
			logger.info("generarMensajeCarmen---> despues de obtener cuenta custodia");
			ArrayList<OrdenTitulo> listaTitulos = orden.getOrdenTitulo();
			for (OrdenTitulo ordenTitulo : listaTitulos) {
				MensajeCarmen mensajeCarmen = new MensajeCarmen();
				mensajeCarmen.set(mensajeCarmen.CODIGO_CLIENTE, _cuentaCustodia.getValue("ID_cliente"));//CODIGO DE CLIENTE EN CARMEN
				mensajeCarmen.set(mensajeCarmen.CODIGO_CUENTA, _cuentaCustodia.getValue("Cuenta_custodia"));//CODIGO DE CUENTA EN CARMEN
				mensajeCarmen.set(mensajeCarmen.CLAVE_VALOR, ordenTitulo.getTituloId());
				mensajeCarmen.set(mensajeCarmen.CANTIDAD, ordenTitulo.getUnidades());
				mensajeCarmen.set(mensajeCarmen.FECHA_OPERACION, orden.getFechaOrden());// fecha orden ORDENE_PED_FE_ORDEN cambio 29/12/2011 CE
				mensajeCarmen.set(mensajeCarmen.FECHA_LIQUIDACION, orden.getFechaValor());// fecha valor
				mensajeCarmen.set(mensajeCarmen.CONTRAPARTE, contraparteBDV);
				mensajeCarmen.setUsuarioNM(usuario);
				mensajeCarmen.setOrdeneId(Integer.parseInt(String.valueOf(orden.getIdOrden())));

				// ESTABLECER VALORES POR DEFECTO
				mensajeCarmen.setHashMap(valoresPorDefecto);

				String[] sentenciasMje = mensajeDAO.ingresar(mensajeCarmen);
				
				//AGREGAR QUERYS A RETORNAR
				querys.addAll(Arrays.asList(sentenciasMje));
			}
		} else {
			logger.info("No se genero el mensaje CARMEN - El cliente " + orden.getIdCliente() + " no posee cuenta custodia");
		}
		return querys;
	}
	
		
}

package com.bdv.infi.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import megasoft.db;
import org.apache.log4j.Logger;

import com.bdv.infi.data.TasaCambioBCV;
import com.bdv.infi.model.menudeo.Monedas;

/**
 * Clase usada para la l&oacute;gica de inserci&oacute;n, modificaci&oacute;n y recuperaci&oacute;n de las ordenes en la base de datos
 */
public class TasaCambioDAO extends com.bdv.infi.dao.GenericoDAO {

	private Logger logger = Logger.getLogger(TasaCambioDAO.class);

	public TasaCambioDAO(DataSource ds) {
		super(ds);
	}

	@Override
	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public void insertarTasaCambio(List<Monedas> listaMoneda) throws Exception {
		int i = 0;
		String strSQL = "INSERT ALL INTO ADM_INFI.INFI_TB_237_TASASDIVISAS (FECHA, MONEDA,TASAVENTA, TASACOMPRA  ) VALUES ";
		String coma = "";
		for (Monedas Moneda : listaMoneda) {

			if (i >= 1) {
				coma = " INTO ADM_INFI.INFI_TB_237_TASASDIVISAS (FECHA, MONEDA,TASAVENTA, TASACOMPRA  ) VALUES  ";
				strSQL += coma + "(to_char(trunc(sysdate),'DD-MM-YYYY'), '" + Moneda.getSiglas() + "'," + Moneda.getVenta() + "," + Moneda.getCompra() + ")";
			} else {

				strSQL += " (to_char(trunc(sysdate),'DD-MM-YYYY'), '" + Moneda.getSiglas() + "'," + Moneda.getVenta() + "," + Moneda.getCompra() + ")";
			}
			i++;

		}
		strSQL += " SELECT * FROM dual";

		StringBuffer sql = new StringBuffer();
		sql.append(strSQL);
		System.out.println("sql :" + sql);
		try {
			db.exec(dataSource, sql.toString());

		} catch (Exception e) {
			logger.error("insertarTasaCambio :" + e);
			System.out.println("insertarTasaCambio :" + e);
		}

	}

	public void editarTasaCabmio(List<TasaCambioBCV> tasaCambio) {

		for (TasaCambioBCV tasaCambioBCV : tasaCambio) {
			try {
				StringBuffer sql = new StringBuffer();

				sql.append("UPDATE INFI_TB_237_TASASDIVISAS set ");
				sql.append("TASAVENTA=").append(tasaCambioBCV.getTasaVenta()).append(", ");
				sql.append("TASACOMPRA=").append(tasaCambioBCV.getTasaCompra()).append(" ");
				sql.append("WHERE ");
				sql.append("MONEDA=").append(tasaCambioBCV.getMoneda()).append("', ");

				db.exec(dataSource, sql.toString());

			} catch (Exception e) {
				logger.error("Error al momento de modificar las tasas-->" + e);
				System.out.println("error--->" + e);
			}

		}
	}

	public int Cantidad() throws Exception {
		int valor = 0;
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT COUNT(*) AS CANT FROM ADM_INFI.INFI_TB_237_TASASDIVISAS WHERE FECHA = to_char(trunc(sysdate),'DD-MM-YYYY')");

		dataSet = db.get(dataSource, sql.toString());

		while (dataSet.next()) {
			valor = Integer.parseInt(dataSet.getValue("CANT"));
		}

		return valor;
	}

	// Inserta o actualiza
	public void Upsert(List<Monedas> listaTasas) {

		for (Monedas moneda : listaTasas) {
			StringBuffer sql = new StringBuffer();

			sql.append("begin insert into ADM_INFI.INFI_TB_237_TASASDIVISAS (FECHA,MONEDA,TASAVENTA,TASACOMPRA) values ");
			sql.append("(to_char(trunc(sysdate),'DD-MM-YYYY'),'" + moneda.getSiglas() + "'," + moneda.getVenta() + "," + moneda.getCompra() + "); ");
			sql.append("exception when dup_val_on_index then UPDATE ADM_INFI.INFI_TB_237_TASASDIVISAS SET ");
			sql.append("TASAVENTA = " + moneda.getVenta() + ", TASACOMPRA = " + moneda.getCompra() + "");
			sql.append(" WHERE  FECHA = to_char(trunc(sysdate),'DD-MM-YYYY') and MONEDA = '" + moneda.getSiglas() + "'; ");
			sql.append("end;");

			System.out.println("Upsert : " + sql);
			
			try {
				db.exec(dataSource, sql.toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Permite obtener el valor de la moneda actual
	 * 
	 * @param siglas
	 *            EUR - USD - VES
	 * @param fecha
	 *            DD-MM-AAAA
	 * @return {@link BigDecimal}
	 */
	public List<Monedas>  consultarMoneda(String fecha) {
		StringBuilder sql = new StringBuilder();
		List<Monedas> lstMnd = new ArrayList<Monedas>();
		
		sql.append("SELECT * FROM ADM_INFI.INFI_TB_237_TASASDIVISAS WHERE FECHA = to_char(trunc(sysdate),'DD-MM-YYYY')");
		try {
			dataSet = db.get(dataSource, sql.toString());
			while (dataSet.next()) {
				Monedas mnd = new Monedas();
				mnd.Siglas = dataSet.getValue("MONEDA");
				mnd.Compra = new BigDecimal(dataSet.getValue("TASACOMPRA"));
				mnd.Venta = new BigDecimal(dataSet.getValue("TASAVENTA"));
				lstMnd.add(mnd);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return lstMnd;
	}
}

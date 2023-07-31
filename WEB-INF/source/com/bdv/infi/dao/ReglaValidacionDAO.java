package com.bdv.infi.dao;

import javax.sql.DataSource;

import megasoft.db;

public class ReglaValidacionDAO extends GenericoDAO {

	public ReglaValidacionDAO(DataSource ds) {
		super(ds);
	}

	public static String PERIOCIDAD_DIARIA = "DIARIO";
	public static String PERIOCIDAD_MENSUAL = "MENSUAL";
	public static String PERIOCIDAD_ANUAL = "ANUAL";

	public static String TIPO_ADQUISICION_DIVISAS = "ADQUISICION_DIVISAS";

	/**
	 * Busca las reglas que se deben aplicar sobre la compra que está realizando un cliente pero sin incluir el concepto de compra involucrado
	 * @param tipoPersona tipo de persona
	 * @param tipo tipo de operación ADQUISICION_DIVISAS, COMPRAS, VALIDACION_CLIENTE
	 * @param periocidad periocidad configurada (DIARIO,MENSUAL,ANUAL)
	 */
	public void obtenerReglas(String tipoPersona, String tipo, String periocidad) throws Exception {
		String sql = "select regla_periocidad,regla_valor,regla_mensaje from INFI_TB_902_REGLA_VALIDACION where " + " tipo_persona='" + tipoPersona + "' and regla_tipo='"
				+ tipo + "' and regla_periocidad='" + periocidad + "' and regla_validacion_id not in( select REGLA_VALIDACION_ID from INFI_TB_903_REGLA_CONCEPTO) order by regla_periocidad";
		this.dataSet = db.get(this.dataSource, sql);
	}

	/**
	 * Busca las reglas que se deben aplicar sobre la compra que está realizando un cliente pero sin incluir el concepto de compra involucrado
	 * @param tipoPersona tipo de persona
	 * @param tipo tipo de operación ADQUISICION_DIVISAS, COMPRAS, VALIDACION_CLIENTE
	 */	
	public void obtenerReglas(String tipoPersona, String tipo) throws Exception {
		String sql = "select regla_periocidad,regla_valor,regla_mensaje from INFI_TB_902_REGLA_VALIDACION where " + " tipo_persona='" + tipoPersona + "' and regla_tipo='"
		+ tipo + "' and regla_validacion_id not in( select REGLA_VALIDACION_ID from INFI_TB_903_REGLA_CONCEPTO) order by regla_periocidad";
        this.dataSet = db.get(this.dataSource, sql);
	}

	/**
	 * Busca las reglas de validación por concepto de compra
	 * @param tipoPersona tipo de persona a buscar
	 * @param tipo tipo de operación ADQUISICION_DIVISAS, COMPRAS, VALIDACION_CLIENTE
	 * @param periocidad periocidad configurada (DIARIO,MENSUAL,ANUAL) 
	 * @param idConcepto id del concepto
	 * @throws Exception en caso de error
	 */
	public void obtenerReglasPorConcepto(String tipoPersona, String tipo, String idConcepto) throws Exception{
		String sql = "select regla_periocidad,regla_valor,regla_mensaje from INFI_TB_902_REGLA_VALIDACION a, INFI_TB_903_REGLA_CONCEPTO b " +  
		" where tipo_persona='" + tipoPersona + "' and regla_tipo='" + tipo + "' " +
		" and a.regla_validacion_id = b.regla_validacion_id	and b.concepto_id=" + idConcepto;
		this.dataSet = db.get(this.dataSource, sql);
	}
	

	@Override
	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}

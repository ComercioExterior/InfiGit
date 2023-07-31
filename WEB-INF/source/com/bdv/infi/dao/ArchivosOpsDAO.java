package com.bdv.infi.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.sql.DataSource;

import megasoft.db;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TransaccionFinanciera;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
/**
 * Clase destinada para el manejo l&oacute;gico de inserci&oacute;n, recuperaci&oacute;n y listado de unidades de inversi&oacute;n almacenados en las tablas de trabajo
 */
public class ArchivosOpsDAO extends com.bdv.infi.dao.GenericoDAO {

	/**
	 * Formatos de Date y Time
	 */
	private SimpleDateFormat sdIODate = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);

	/**
	 * Formatos de Date, Hours and Minutes
	 */
	private SimpleDateFormat sdIOHours = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

	/**
	 * Constructor de la clase. Permite inicializar el DataSource para los accesos a la base de datos
	 * 
	 * @param ds
	 *            : DataSource
	 * @throws Exception
	 */
	public ArchivosOpsDAO(DataSource ds) throws Exception {
		super(ds);
	}

	public ArchivosOpsDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
	}

	@Override
	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public String getRegistrosSubastaOps(int idUnidad){
		StringBuilder sql = new StringBuilder();
		sql.append("select a.codigo_operacion,a.numero_retencion,to_char(a.fecha_aplicar,'YYYYMMDD') fecha_operacion,a.ordene_relac_operacion_id, d.tipper_id,d.client_cedrif,d.client_nombre,b.ordene_id as orden_id_referencia,b.ordene_id as orden_id_proceso, a.ordene_operacion_id,a.codigo_operacion,a.numero_retencion," + "a.ctecta_numero,c.undinv_id, c.undinv_nombre, a.status_operacion, a.monto_operacion,a.trnf_tipo,b.ordene_veh_tom, b.ordene_id_relacion,a.moneda_id,a.TRNFIN_ID "
				+ " from infi_tb_207_ordenes_operacion a, infi_tb_204_ordenes b, infi_tb_106_unidad_inversion c, " + "infi_tb_201_ctes d, infi_tb_101_inst_financieros e where a.ordene_id = b.ordene_id and c.insfin_id = e.insfin_id " + "and b.uniinv_id = c.undinv_id and a.status_operacion in('" + ConstantesGenerales.STATUS_EN_ESPERA + "','" + ConstantesGenerales.STATUS_RECHAZADA
				+ "') and a.trnf_tipo in('" + TransaccionFinanciera.DEBITO + "','" + TransaccionFinanciera.CREDITO + "') and a.ordene_id in (select ordene_id from infi_tb_204_ordenes " + " where ");
		if (idUnidad != 0) {
			sql.append("uniinv_id=").append(idUnidad).append(" and ");
		} else {
			if (idUnidad > 0) {
				sql.append("uniinv_id=").append(idUnidad).append(" and ");
			}
			sql.append(" e.TIPO_PRODUCTO_ID = '").append(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME).append("' and ");
		}

		sql.append(" ordsta_id='" + StatusOrden.PROCESO_ADJUDICACION + "')" + " and b.client_id=d.client_id and b.transa_id in('" + TransaccionNegocio.TOMA_DE_ORDEN + "')" + " and c.IN_COBRO_BATCH_ADJ=1 " + " union "
				+ "select a.codigo_operacion,a.numero_retencion,to_char(a.fecha_aplicar,'YYYYMMDD') fecha_operacion,a.ordene_relac_operacion_id,d.tipper_id,d.client_cedrif,d.client_nombre,b.ordene_id  as orden_id_referencia,b.ordene_id as orden_id_proceso, a.ordene_operacion_id,a.codigo_operacion,a.numero_retencion," + "a.ctecta_numero,c.undinv_id, c.undinv_nombre, a.status_operacion, a.monto_operacion,a.trnf_tipo,b.ordene_veh_tom, b.ordene_id_relacion,a.moneda_id,a.TRNFIN_ID "
				+ " from infi_tb_207_ordenes_operacion a, infi_tb_204_ordenes b, infi_tb_106_unidad_inversion c, infi_tb_201_ctes d, infi_tb_101_inst_financieros e" + " where a.ordene_id = b.ordene_id and c.insfin_id = e.insfin_id " + "and b.uniinv_id = c.undinv_id and a.status_operacion in('" + ConstantesGenerales.STATUS_EN_ESPERA + "','" + ConstantesGenerales.STATUS_RECHAZADA
				+ "')	and a.ordene_id in (select ordene_id from infi_tb_204_ordenes " + " where ");
		if (idUnidad != 0) {
			sql.append("uniinv_id=").append(idUnidad).append(" and ");
		} else {
			if (idUnidad > 0) {
				sql.append("uniinv_id=").append(idUnidad).append(" and ");
			}
			sql.append(" e.TIPO_PRODUCTO_ID = '").append(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME).append("' and ");
		}

		sql.append(" ordsta_id='" + StatusOrden.REGISTRADA + "')" + " and b.transa_id='" + TransaccionNegocio.ORDEN_VEHICULO + "' and b.client_id=d.client_id " + " and c.IN_COBRO_BATCH_ADJ=1 ");

		// Para las de desbloqueo
		sql.append(" union select a.codigo_operacion,a.numero_retencion,to_char(f.fecha_aplicar,'YYYYMMDD') fecha_operacion,a.ordene_relac_operacion_id, d.tipper_id,d.client_cedrif,d.client_nombre,b.ordene_id  as orden_id_referencia,b.ordene_id as orden_id_proceso, a.ordene_operacion_id,a.codigo_operacion,a.numero_retencion," + "a.ctecta_numero,c.undinv_id, c.undinv_nombre, a.status_operacion, a.monto_operacion,a.trnf_tipo,b.ordene_veh_tom, b.ordene_id_relacion,a.moneda_id,a.TRNFIN_ID "
				+ " from infi_tb_207_ordenes_operacion a, infi_tb_204_ordenes b, infi_tb_106_unidad_inversion c, " + "infi_tb_201_ctes d, infi_tb_101_inst_financieros e,infi_tb_207_ordenes_operacion f where a.ordene_id = b.ordene_id and c.insfin_id = e.insfin_id " + "and b.uniinv_id = c.undinv_id and a.status_operacion in('" + ConstantesGenerales.STATUS_EN_ESPERA + "','"
				+ ConstantesGenerales.STATUS_RECHAZADA + "') and a.trnf_tipo in('" + TransaccionFinanciera.DESBLOQUEO + "') and a.ordene_id in (select ordene_id from infi_tb_204_ordenes " + " where a.ordene_id = f.ordene_id and a.ordene_relac_operacion_id = f.ordene_operacion_id and ");
		if (idUnidad != 0) {
			sql.append("uniinv_id=").append(idUnidad).append(" and ");
		} else {
			if (idUnidad > 0) {
				sql.append("uniinv_id=").append(idUnidad).append(" and ");
			}
			sql.append(" e.TIPO_PRODUCTO_ID = '").append(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME).append("' and ");
		}

		sql.append(" ordsta_id='" + StatusOrden.PROCESO_ADJUDICACION + "')" + " and b.client_id=d.client_id and b.transa_id in('" + TransaccionNegocio.TOMA_DE_ORDEN + "','" + TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA + "')" + " and c.IN_COBRO_BATCH_ADJ=1 "
		+ " order by ordene_operacion_id");
		System.out.println("getRegistrosOpsBolivares --> " + sql.toString());
		//CONSULTA DE REGISTROS OPS
		return sql.toString();
	}
	
	//TTS-541 Desarrollo Liquidacion producto DICOM NM26659_15/11/2017
	public String envioOperacionesDICOM(String idOrdenes,String idUnidadInv,String transaccion,String statusOrden,long trnfinId,String[] trnfTipo) throws Exception {
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT   b.ordene_id,a.codigo_operacion, a.numero_retencion, TO_CHAR (a.fecha_aplicar, 'YYYYMMDD') fecha_operacion,a.ordene_relac_operacion_id,");
		sql.append("d.tipper_id, d.client_cedrif,d.client_nombre,a.ordene_operacion_id, b.ordene_id_relacion,a.ctecta_numero, c.undinv_id, c.undinv_nombre,");
		sql.append(" a.status_operacion,a.monto_operacion,a.trnf_tipo, b.ordene_veh_tom, b.ordene_id_relacion,a.moneda_id,a.trnfin_id,b.ordene_id AS orden_id_proceso,");
		if(transaccion.equals(TransaccionNegocio.ORDEN_PAGO)){
			sql.append("	b.ordene_id_relacion AS orden_id_referencia ");
		}else {
			sql.append("	b.ordene_id AS orden_id_referencia ");
		}

		sql.append("FROM infi_tb_207_ordenes_operacion a,infi_tb_204_ordenes b,infi_tb_106_unidad_inversion c,infi_tb_201_ctes d ");
		sql.append("WHERE a.ordene_id = b.ordene_id AND b.uniinv_id = c.undinv_id and d.CLIENT_ID=b.CLIENT_ID ");
		sql.append("and a.status_operacion in('" + ConstantesGenerales.STATUS_EN_ESPERA + "','" + ConstantesGenerales.STATUS_RECHAZADA+ "') ");
				
		if(idOrdenes!=null && !idOrdenes.equals("")){
			sql.append(" and b.ordene_id in (").append(idOrdenes).append(") "); 
		}
		
		if(idUnidadInv!=null && !idUnidadInv.equals("")){
			sql.append(" and c.undinv_id=").append(idUnidadInv).append(" ");
		}

		
		if(transaccion!=null && !transaccion.equals("")){
			int count=0;
			sql.append(" and b.transa_id ='").append(transaccion).append("' ");
			
		}
		
		if(statusOrden!=null && !statusOrden.equals("")){
			sql.append(" AND b.ORDSTA_ID ='").append(statusOrden).append("' ");									
		}
				
		if(trnfinId!=0) {
			sql.append(" AND a.TRNFIN_ID=").append(trnfinId).append(" ");				
		}
		
		if(trnfTipo!=null && trnfTipo[0]!=null){
			int count=0;
			sql.append("AND a.trnf_tipo IN (");	
			for (String element : trnfTipo) {
				if(count>0){
					sql.append(",");
				}
				sql.append("'"+element+"'");
				++count;
			}			
			sql.append(") ");
		}
		
		if(transaccion.equals(TransaccionNegocio.ORDEN_PAGO)){
			sql.append(" AND NOT EXISTS (SELECT op.ordene_id FROM infi_tb_207_ordenes_operacion op WHERE op.ordene_id = b.ordene_id_relacion ");
			sql.append(" AND op.trnf_tipo <> '").append(TransaccionFinanciera.DESBLOQUEO).append("' AND op.status_operacion IN ('").append(ConstantesGenerales.STATUS_EN_ESPERA).append("', '").append(ConstantesGenerales.STATUS_RECHAZADA).append("')) ");
		}
		
		sql.append("  ORDER BY a.ORDENE_ID,a.TRNF_TIPO ASC ");
		
		System.out.println("envioOperacionesDICOM ------> " + sql.toString());
		return sql.toString();
	}
	
	
	public String envioOperacionesORO(String idOrdenes,String idUnidadInv,String transaccion,String statusOrden,long trnfinId,String[] trnfTipo) throws Exception {
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT   b.ordene_id,a.codigo_operacion, a.numero_retencion, TO_CHAR (a.fecha_aplicar, 'YYYYMMDD') fecha_operacion,a.ordene_relac_operacion_id,");
		sql.append("d.tipper_id, d.client_cedrif,d.client_nombre,a.ordene_operacion_id, b.ordene_id_relacion,a.ctecta_numero, c.undinv_id, c.undinv_nombre,");
		sql.append(" a.status_operacion,a.monto_operacion,a.trnf_tipo, b.ordene_veh_tom, b.ordene_id_relacion,a.moneda_id,a.trnfin_id,b.ordene_id AS orden_id_proceso,");
		if(transaccion.equals(TransaccionNegocio.ORDEN_PAGO)){
			sql.append("	b.ordene_id_relacion AS orden_id_referencia ");
		}else {
			sql.append("	b.ordene_id AS orden_id_referencia ");
		}

		sql.append("FROM infi_tb_207_ordenes_operacion a,infi_tb_204_ordenes b,infi_tb_106_unidad_inversion c,infi_tb_201_ctes d ");
		sql.append("WHERE a.ordene_id = b.ordene_id AND b.uniinv_id = c.undinv_id and d.CLIENT_ID=b.CLIENT_ID ");
		sql.append("and a.status_operacion in('" + ConstantesGenerales.STATUS_EN_ESPERA + "','" + ConstantesGenerales.STATUS_RECHAZADA+ "') ");
				
		if(idOrdenes!=null && !idOrdenes.equals("")){
			sql.append(" and b.ordene_id in (").append(idOrdenes).append(") "); 
		}
		
		if(idUnidadInv!=null && !idUnidadInv.equals("")){
			sql.append(" and c.undinv_id=").append(idUnidadInv).append(" ");
		}

		
		if(transaccion!=null && !transaccion.equals("")){
			int count=0;
			sql.append(" and b.transa_id ='").append(transaccion).append("' ");
			
		}
		
		if(statusOrden!=null && !statusOrden.equals("")){
			sql.append(" AND b.ORDSTA_ID ='").append(statusOrden).append("' ");									
		}
				
		if(trnfinId!=0) {
			sql.append(" AND a.TRNFIN_ID=").append(trnfinId).append(" ");				
		}
		
		if(trnfTipo!=null && trnfTipo[0]!=null){
			int count=0;
			sql.append("AND a.trnf_tipo IN (");	
			for (String element : trnfTipo) {
				if(count>0){
					sql.append(",");
				}
				sql.append("'"+element+"'");
				++count;
			}			
			sql.append(") ");
		}
		
		if(transaccion.equals(TransaccionNegocio.ORDEN_PAGO)){
			sql.append(" AND NOT EXISTS (SELECT op.ordene_id FROM infi_tb_207_ordenes_operacion op WHERE op.ordene_id = b.ordene_id_relacion ");
			sql.append(" AND op.trnf_tipo <> '").append(TransaccionFinanciera.DESBLOQUEO).append("' AND op.status_operacion IN ('").append(ConstantesGenerales.STATUS_EN_ESPERA).append("', '").append(ConstantesGenerales.STATUS_RECHAZADA).append("')) ");
		}
		
		sql.append("  ORDER BY a.ORDENE_ID,a.TRNF_TIPO ASC ");
		
		System.out.println("envioOperacionesDICOM ------> " + sql.toString());
		return sql.toString();
	}
	

	public void getOperacionesDicomDetalle(String idUnidadInv,String transaccion,String statusOrden) throws Exception {
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT DISTINCT b.ordene_id,NVL (b.ordene_ped_comisiones, 0) AS monto_comision,d.tipper_id, ");		
		sql.append("d.client_cedrif, d.client_nombre, b.ordene_ped_fe_valor,b.ordene_veh_tom, b.ordsta_id, c.undinv_nombre, ");
		sql.append(" b.ordene_ped_fe_valor, b.ordene_veh_tom, b.ordsta_id,c.undinv_nombre, a.MONEDA_ID, ");
		if(transaccion.equals(TransaccionNegocio.ORDEN_PAGO)){
			sql.append(" b.ordene_ped_total AS monto_capital, ");
			sql.append(" b.ordene_id_relacion AS orden_id_referencia ");
		}else {
			sql.append(" DECODE (b.ordene_ped_total,0, b.ordene_ped_total,b.ordene_ped_total - b.ordene_ped_comisiones) AS monto_capital, "); 
			sql.append(" b.ordene_id AS orden_id_referencia ");
		}
		
		sql.append("FROM infi_tb_207_ordenes_operacion a,infi_tb_204_ordenes b,infi_tb_106_unidad_inversion c,infi_tb_201_ctes d ");
		sql.append("WHERE a.ordene_id = b.ordene_id AND b.uniinv_id = c.undinv_id and d.CLIENT_ID=b.CLIENT_ID ");
		sql.append("and a.status_operacion in('" + ConstantesGenerales.STATUS_EN_ESPERA + "','" + ConstantesGenerales.STATUS_RECHAZADA+ "') ");
				
//		if(idOrdenes!=null && !idOrdenes.equals("")){
//			sql.append(" and b.ordene_id in (").append(idOrdenes).append(") "); 
//		}
		
		if(idUnidadInv!=null && !idUnidadInv.equals("")){
			sql.append(" and c.undinv_id=").append(idUnidadInv).append(" ");
		}

		
		if(transaccion!=null && !transaccion.equals("")){
			int count=0;
			sql.append(" and b.transa_id ='").append(transaccion).append("' ");
			
		}
		
		if(statusOrden!=null && !statusOrden.equals("")){
			sql.append(" AND b.ORDSTA_ID ='").append(statusOrden).append("' ");									
		}
					
		
		if(transaccion.equals(TransaccionNegocio.ORDEN_PAGO)){
			sql.append(" AND NOT EXISTS (SELECT op.ordene_id FROM infi_tb_207_ordenes_operacion op WHERE op.ordene_id = b.ordene_id_relacion ");
			sql.append(" AND op.trnf_tipo <> '").append(TransaccionFinanciera.DESBLOQUEO).append("' AND op.status_operacion IN ('").append(ConstantesGenerales.STATUS_EN_ESPERA).append("', '").append(ConstantesGenerales.STATUS_RECHAZADA).append("')) ");
		}
		
		sql.append("  ORDER BY b.ORDENE_ID ");
		
		System.out.println("getOperacionesDicomDetalle ------> " + sql.toString());
		dataSet = db.get(dataSource, sql.toString());
	}
	
	public void getOperacionesORODetalle(String idUnidadInv,String transaccion,String statusOrden) throws Exception {
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT DISTINCT b.ordene_id,NVL (b.ordene_ped_comisiones, 0) AS monto_comision,d.tipper_id, ");		
		sql.append("d.client_cedrif, d.client_nombre, b.ordene_ped_fe_valor,b.ordene_veh_tom, b.ordsta_id, c.undinv_nombre, ");
		sql.append(" b.ordene_ped_fe_valor, b.ordene_veh_tom, b.ordsta_id,c.undinv_nombre, a.MONEDA_ID, ");
		if(transaccion.equals(TransaccionNegocio.ORDEN_PAGO)){
			sql.append(" b.ordene_ped_total AS monto_capital, ");
			sql.append(" b.ordene_id_relacion AS orden_id_referencia ");
		}else {
			sql.append(" DECODE (b.ordene_ped_total,0, b.ordene_ped_total,b.ordene_ped_total - b.ordene_ped_comisiones) AS monto_capital, "); 
			sql.append(" b.ordene_id AS orden_id_referencia ");
		}
		
		sql.append("FROM infi_tb_207_ordenes_operacion a,infi_tb_204_ordenes b,infi_tb_106_unidad_inversion c,infi_tb_201_ctes d ");
		sql.append("WHERE a.ordene_id = b.ordene_id AND b.uniinv_id = c.undinv_id and d.CLIENT_ID=b.CLIENT_ID ");
		sql.append("and a.status_operacion in('" + ConstantesGenerales.STATUS_EN_ESPERA + "','" + ConstantesGenerales.STATUS_RECHAZADA+ "') ");
				
//		if(idOrdenes!=null && !idOrdenes.equals("")){
//			sql.append(" and b.ordene_id in (").append(idOrdenes).append(") "); 
//		}
		
		if(idUnidadInv!=null && !idUnidadInv.equals("")){
			sql.append(" and c.undinv_id=").append(idUnidadInv).append(" ");
		}

		
		if(transaccion!=null && !transaccion.equals("")){
			int count=0;
			sql.append(" and b.transa_id ='").append(transaccion).append("' ");
			
		}
		
		if(statusOrden!=null && !statusOrden.equals("")){
			sql.append(" AND b.ORDSTA_ID ='").append(statusOrden).append("' ");									
		}
					
		
		if(transaccion.equals(TransaccionNegocio.ORDEN_PAGO)){
			sql.append(" AND NOT EXISTS (SELECT op.ordene_id FROM infi_tb_207_ordenes_operacion op WHERE op.ordene_id = b.ordene_id_relacion ");
			sql.append(" AND op.trnf_tipo <> '").append(TransaccionFinanciera.DESBLOQUEO).append("' AND op.status_operacion IN ('").append(ConstantesGenerales.STATUS_EN_ESPERA).append("', '").append(ConstantesGenerales.STATUS_RECHAZADA).append("')) ");
		}
		
		sql.append("  ORDER BY b.ORDENE_ID ");
		
		System.out.println("getOperacionesDicomDetalle ------> " + sql.toString());
		dataSet = db.get(dataSource, sql.toString());
	}
	
	public void getOperacionesDicomResumen(long idUnidad,String transaId,String statusOrden) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT  CASE A.TRNF_TIPO WHEN 'CRE' THEN 'CREDITO' WHEN 'DEB' THEN 'DEBITO' WHEN 'DES' THEN 'DESBLOQUEO' END AS TIPO_OPERACION,");
		sql.append("a.status_operacion,SUM (a.monto_operacion) monto_operacion, COUNT (a.ordene_id) cantidad_operaciones ");
		sql.append("FROM infi_tb_207_ordenes_operacion a,infi_tb_204_ordenes b,infi_tb_106_unidad_inversion c ");
		sql.append("WHERE a.ordene_id = b.ordene_id AND b.uniinv_id = c.undinv_id ");
		sql.append("and a.status_operacion in('" + ConstantesGenerales.STATUS_EN_ESPERA + "','" + ConstantesGenerales.STATUS_RECHAZADA + "') ");		
		if(idUnidad>0){
			sql.append(" AND uniinv_id =").append(idUnidad).append(" ");
		}							
		sql.append(" and b.transa_id='").append(transaId).append("' ");
		sql.append(" and b.ordsta_id='").append(statusOrden).append("' ");
		
		sql.append(" AND a.trnf_tipo IN ('").append(TransaccionFinanciera.DEBITO).append("', '").append(TransaccionFinanciera.DESBLOQUEO).append("', '").append(TransaccionFinanciera.CREDITO).append("') ");
		
		if(transaId.equals(TransaccionNegocio.ORDEN_PAGO)){		
			sql.append(" AND NOT EXISTS (SELECT op.ordene_id FROM infi_tb_207_ordenes_operacion op WHERE op.ordene_id = b.ordene_id_relacion ");		
			sql.append(" AND op.trnf_tipo <> '").append(TransaccionFinanciera.DESBLOQUEO).append("' AND op.status_operacion IN ('").append(ConstantesGenerales.STATUS_EN_ESPERA).append("', '").append(ConstantesGenerales.STATUS_RECHAZADA).append("')) ");
		}
		sql.append(" GROUP BY a.status_operacion,a.trnf_tipo ");

		System.out.println("getOperacionesDicomResumen ----> "  + sql.toString());
		dataSet = db.get(dataSource, sql.toString());
	}
	
	
	
	
	public void getOperacionesOROResumen(long idUnidad,String transaId,String statusOrden) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT  CASE A.TRNF_TIPO WHEN 'CRE' THEN 'CREDITO' WHEN 'DEB' THEN 'DEBITO' WHEN 'DES' THEN 'DESBLOQUEO' END AS TIPO_OPERACION,");
		sql.append("a.status_operacion,SUM (a.monto_operacion) monto_operacion, COUNT (a.ordene_id) cantidad_operaciones ");
		sql.append("FROM infi_tb_207_ordenes_operacion a,infi_tb_204_ordenes b,infi_tb_106_unidad_inversion c ");
		sql.append("WHERE a.ordene_id = b.ordene_id AND b.uniinv_id = c.undinv_id ");
		sql.append("and a.status_operacion in('" + ConstantesGenerales.STATUS_EN_ESPERA + "','" + ConstantesGenerales.STATUS_RECHAZADA + "') ");		
		if(idUnidad>0){
			sql.append(" AND uniinv_id =").append(idUnidad).append(" ");
		}							
		sql.append(" and b.transa_id='").append(transaId).append("' ");
		sql.append(" and b.ordsta_id='").append(statusOrden).append("' ");
		
		sql.append(" AND a.trnf_tipo IN ('").append(TransaccionFinanciera.DEBITO).append("', '").append(TransaccionFinanciera.DESBLOQUEO).append("', '").append(TransaccionFinanciera.CREDITO).append("') ");
		
		if(transaId.equals(TransaccionNegocio.ORDEN_PAGO)){		
			sql.append(" AND NOT EXISTS (SELECT op.ordene_id FROM infi_tb_207_ordenes_operacion op WHERE op.ordene_id = b.ordene_id_relacion ");		
			sql.append(" AND op.trnf_tipo <> '").append(TransaccionFinanciera.DESBLOQUEO).append("' AND op.status_operacion IN ('").append(ConstantesGenerales.STATUS_EN_ESPERA).append("', '").append(ConstantesGenerales.STATUS_RECHAZADA).append("')) ");
		}
		sql.append(" GROUP BY a.status_operacion,a.trnf_tipo ");

		System.out.println("getOperacionesOROResumen ----> "  + sql.toString());
		dataSet = db.get(dataSource, sql.toString());
	}
	
}

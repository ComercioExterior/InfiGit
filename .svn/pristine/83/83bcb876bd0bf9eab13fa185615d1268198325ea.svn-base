package com.bdv.infi.dao;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.sql.DataSource;

import megasoft.db;

import com.bancovenezuela.comun.data.OrdenOperacion;
import com.bdv.infi.data.SolicitudClavenet;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.util.Utilitario;
public class SolicitudesSitmeDAO extends com.bdv.infi.dao.GenericoDAO {
	
	public SolicitudesSitmeDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
	}
	public SolicitudesSitmeDAO(DataSource ds) {
		super(ds);
	}
	
	/*
	 * Obtiene las solicitudes sitme filtradas por diferentes campos
	 * */
	public ArrayList<SolicitudClavenet> getSolicitudes(long id_orden, long nro_orden_infi, String name_client, String ci_rif_client, String nro_rusitme, String nro_bloqueo, String status, String nro_cta, String nro_pasaporte, boolean truncCedRif, boolean like, String... fechaRegistro) throws Exception {				
		ArrayList<SolicitudClavenet> arrSc = new ArrayList<SolicitudClavenet>();
		SolicitudClavenet sc = null;
		SimpleDateFormat sdf = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA2);
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ss.ID_ORDEN, ");
		sql.append("case when ss.NUMERO_ORDEN_INFI IS NULL then 0 when ss.NUMERO_ORDEN_INFI IS NOT NULL then ss.NUMERO_ORDEN_INFI end NUMERO_ORDEN_INFI, ");
		sql.append("case when ss.NOMBRE_CLIENTE IS NULL then ' ' when ss.NOMBRE_CLIENTE IS NOT NULL then UPPER(ss.NOMBRE_CLIENTE) end NOMBRE_CLIENTE, ");
		sql.append("case when ss.CED_RIF_CLIENTE IS NULL then ' ' when ss.CED_RIF_CLIENTE IS NOT NULL then UPPER(ss.CED_RIF_CLIENTE) end CED_RIF_CLIENTE, ");
		sql.append("ss.FECHA_VENC_RIF, ");
		sql.append("case when ss.NUM_RUSITME IS NULL then ' ' when ss.NUM_RUSITME IS NOT NULL then UPPER(ss.NUM_RUSITME) end NUM_RUSITME, ");
		sql.append("case when ss.EMAIL_CLIENTE IS NULL then ' ' when ss.EMAIL_CLIENTE IS NOT NULL then LOWER(ss.EMAIL_CLIENTE) end EMAIL_CLIENTE, ");
		sql.append("case when ss.CED_RIF_CONTRAPARTE IS NULL then ' ' when ss.CED_RIF_CONTRAPARTE IS NOT NULL then UPPER(ss.CED_RIF_CONTRAPARTE) end CED_RIF_CONTRAPARTE, ");
		sql.append("case when ss.MONTO_SOLICITADO IS NULL then 0 when ss.MONTO_SOLICITADO IS NOT NULL then ss.MONTO_SOLICITADO end MONTO_SOLICITADO, ");
		sql.append("case when ss.MONTO_COMISION IS NULL then 0 when ss.MONTO_COMISION IS NOT NULL then ss.MONTO_COMISION end MONTO_COMISION, ");
		sql.append("case when ss.PORC_COMISION IS NULL then 0 when ss.PORC_COMISION IS NOT NULL then ss.PORC_COMISION end PORC_COMISION, ");
		sql.append("case when ss.NUM_BLOQUEO IS NULL then ' ' when ss.NUM_BLOQUEO IS NOT NULL then ss.NUM_BLOQUEO end NUM_BLOQUEO, ");
		sql.append("case when ss.MONTO_ADJUDICADO IS NULL then 0 when ss.MONTO_ADJUDICADO IS NOT NULL then ss.MONTO_ADJUDICADO end MONTO_ADJUDICADO, ");
		sql.append("case when ss.ID_CONCEPTO IS NULL then 0 when ss.ID_CONCEPTO IS NOT NULL then ss.ID_CONCEPTO end ID_CONCEPTO, ");
		sql.append("case when ss.SECTOR_PRODUCTIVO IS NULL then 0 when ss.SECTOR_PRODUCTIVO IS NOT NULL then ss.SECTOR_PRODUCTIVO end SECTOR_PRODUCTIVO, ");
		sql.append("case when ss.SECTOR_ACT_ECONOM IS NULL then 0 when ss.SECTOR_ACT_ECONOM IS NOT NULL then ss.SECTOR_ACT_ECONOM end SECTOR_ACT_ECONOM, ");
		sql.append("case when ss.GRUPO_ECONOMICO IS NULL then ' ' when ss.GRUPO_ECONOMICO IS NOT NULL then ss.GRUPO_ECONOMICO end GRUPO_ECONOMICO, ");
		sql.append("case when ss.ESTATUS IS NULL then ' ' when ss.ESTATUS IS NOT NULL then UPPER(ss.ESTATUS) end ESTATUS, ");
		sql.append("ss.FECHA_REGISTRO, ");
		sql.append("case when ss.HORA_REGISTRO IS NULL then ' ' when ss.HORA_REGISTRO IS NOT NULL then ss.HORA_REGISTRO end HORA_REGISTRO, ");
		//sql.append("ss.FECHA_TRAMITE, FECHA_ADJUDICACION, FECHA_COBRO, FECHA_ENV_SWIFT, FECHA_RES_SWIFT, ");
		sql.append("TO_CHAR(ss.FECHA_TRAMITE, '").append(ConstantesGenerales.FORMATO_FECHA).append("') AS FECHA_TRAMITE, ");
		sql.append("TO_CHAR(ss.FECHA_ADJUDICACION, '").append(ConstantesGenerales.FORMATO_FECHA).append("') AS FECHA_ADJUDICACION, ");
		sql.append("TO_CHAR(ss.FECHA_COBRO, '").append(ConstantesGenerales.FORMATO_FECHA).append("') AS FECHA_COBRO, ");
		sql.append("TO_CHAR(ss.FECHA_ENV_SWIFT, '").append(ConstantesGenerales.FORMATO_FECHA).append("') AS FECHA_ENV_SWIFT, ");
		sql.append("TO_CHAR(ss.FECHA_RES_SWIFT, '").append(ConstantesGenerales.FORMATO_FECHA).append("') AS FECHA_RES_SWIFT, ");
		sql.append("case when ss.TITULO_ADJUDICADO IS NULL then ' ' when ss.TITULO_ADJUDICADO IS NOT NULL then ss.TITULO_ADJUDICADO end TITULO_ADJUDICADO, ");
		sql.append("case when ss.PRECIO_ADJUDICACION IS NULL then 0 when ss.PRECIO_ADJUDICACION IS NOT NULL then ss.PRECIO_ADJUDICACION end PRECIO_ADJUDICACION, ");
		sql.append("case when ss.VALOR_EFECTIVO_USD IS NULL then 0 when ss.VALOR_EFECTIVO_USD IS NOT NULL then ss.VALOR_EFECTIVO_USD end VALOR_EFECTIVO_USD, ");
		sql.append("case when ss.VALOR_NOMINAL_USD IS NULL then 0 when ss.VALOR_NOMINAL_USD IS NOT NULL then ss.VALOR_NOMINAL_USD end VALOR_NOMINAL_USD, ");
		sql.append("case when ss.VALOR_EFECTIVO IS NULL then 0 when ss.VALOR_EFECTIVO IS NOT NULL then ss.VALOR_EFECTIVO end VALOR_EFECTIVO, ");
		sql.append("case when ss.VALOR_NOMINAL IS NULL then 0 when ss.VALOR_NOMINAL IS NOT NULL then ss.VALOR_NOMINAL end VALOR_NOMINAL, ");
		sql.append("case when ss.CUENTA_BSF_O IS NULL then ' ' when ss.CUENTA_BSF_O IS NOT NULL then ss.CUENTA_BSF_O end CUENTA_BSF_O, ");
		sql.append("case when ss.NOMBRE_BENEFICIARIO IS NULL then ' ' when ss.NOMBRE_BENEFICIARIO IS NOT NULL then ss.NOMBRE_BENEFICIARIO end NOMBRE_BENEFICIARIO, ");
		sql.append("case when ss.CTA_NUMERO IS NULL then ' ' when ss.CTA_NUMERO IS NOT NULL then ss.CTA_NUMERO end CTA_NUMERO, ");
		sql.append("case when ss.CTA_BANCO IS NULL then ' ' when ss.CTA_BANCO IS NOT NULL then ss.CTA_BANCO end CTA_BANCO, ");
		sql.append("case when ss.CTA_DIRECCION IS NULL then ' ' when ss.CTA_DIRECCION IS NOT NULL then ss.CTA_DIRECCION end CTA_DIRECCION, ");
		sql.append("case when ss.CTA_BIC_SWIFT IS NULL then ' ' when ss.CTA_BIC_SWIFT IS NOT NULL then ss.CTA_BIC_SWIFT end CTA_BIC_SWIFT, ");
		sql.append("case when ss.CTA_TELEFONO IS NULL then ' ' when ss.CTA_TELEFONO IS NOT NULL then ss.CTA_TELEFONO end CTA_TELEFONO, ");
		sql.append("case when ss.CTA_TELEFONO_2 IS NULL then ' ' when ss.CTA_TELEFONO_2 IS NOT NULL then ss.CTA_TELEFONO_2 end CTA_TELEFONO_2, ");
		sql.append("case when ss.CTA_TELEFONO_3 IS NULL then ' ' when ss.CTA_TELEFONO_3 IS NOT NULL then ss.CTA_TELEFONO_3 end CTA_TELEFONO_3, ");
		sql.append("case when ss.CTA_ABA IS NULL then ' ' when ss.CTA_ABA IS NOT NULL then ss.CTA_ABA end CTA_ABA, ");
		sql.append("case when ss.CTA_DIRECCION_C IS NULL then ' ' when ss.CTA_DIRECCION_C IS NOT NULL then ss.CTA_DIRECCION_C end CTA_DIRECCION_C, ");
		sql.append("case when ss.CTA_IBAN IS NULL then ' ' when ss.CTA_IBAN IS NOT NULL then ss.CTA_IBAN end CTA_IBAN, ");
		sql.append("case when ss.CTA_INT_NUMERO IS NULL then ' ' when ss.CTA_INT_NUMERO IS NOT NULL then ss.CTA_INT_NUMERO end CTA_INT_NUMERO, ");
		sql.append("case when ss.CTA_INT_BANCO IS NULL then ' ' when ss.CTA_INT_BANCO IS NOT NULL then ss.CTA_INT_BANCO end CTA_INT_BANCO, ");
		sql.append("case when ss.CTA_INT_DIRECCION IS NULL then ' ' when ss.CTA_INT_DIRECCION IS NOT NULL then ss.CTA_INT_DIRECCION end CTA_INT_DIRECCION, ");
		sql.append("case when ss.CTA_INT_BIC_SWIFT IS NULL then ' ' when ss.CTA_INT_BIC_SWIFT IS NOT NULL then ss.CTA_INT_BIC_SWIFT end CTA_INT_BIC_SWIFT, ");
		sql.append("case when ss.CTA_INT_TELEFONO IS NULL then ' ' when ss.CTA_INT_TELEFONO IS NOT NULL then ss.CTA_INT_TELEFONO end CTA_INT_TELEFONO, ");
		sql.append("case when ss.CTA_INT_ABA IS NULL then ' ' when ss.CTA_INT_ABA IS NOT NULL then ss.CTA_INT_ABA end CTA_INT_ABA, ");
		sql.append("case when ss.CONTADOR_ENV_SWIFT IS NULL then 0 when ss.CONTADOR_ENV_SWIFT IS NOT NULL then ss.CONTADOR_ENV_SWIFT end CONTADOR_ENV_SWIFT, ");
		sql.append("case when ss.ID_CANAL IS NULL then ' ' when ss.ID_CANAL IS NOT NULL then ss.ID_CANAL end ID_CANAL, ");
		sql.append("ss.FECHA_MODIFICACION, ss.FECHA_SALIDA_VIAJE, ss.FECHA_RETORNO_VIAJE, ");
		sql.append("case when ss.COD_PAIS_ORIGEN IS NULL then ' ' when ss.COD_PAIS_ORIGEN IS NOT NULL then ss.COD_PAIS_ORIGEN end COD_PAIS_ORIGEN, ");
		sql.append("case when ss.DESC_PAIS_ORIGEN IS NULL then ' ' when ss.DESC_PAIS_ORIGEN IS NOT NULL then ss.DESC_PAIS_ORIGEN end DESC_PAIS_ORIGEN, ");
		sql.append("case when ss.COD_ESTADO_ORIGEN IS NULL then ' ' when ss.COD_ESTADO_ORIGEN IS NOT NULL then ss.COD_ESTADO_ORIGEN end COD_ESTADO_ORIGEN, ");
		sql.append("case when ss.DESC_ESTADO_ORIGEN IS NULL then ' ' when ss.DESC_ESTADO_ORIGEN IS NOT NULL then ss.DESC_ESTADO_ORIGEN end DESC_ESTADO_ORIGEN, ");
		sql.append("case when ss.COD_CIUDAD_ORIGEN IS NULL then ' ' when ss.COD_CIUDAD_ORIGEN IS NOT NULL then ss.COD_CIUDAD_ORIGEN end COD_CIUDAD_ORIGEN, ");
		sql.append("case when ss.DESC_CIUDAD_ORIGEN IS NULL then ' ' when ss.DESC_CIUDAD_ORIGEN IS NOT NULL then ss.DESC_CIUDAD_ORIGEN end DESC_CIUDAD_ORIGEN, ");
		sql.append("case when ss.COD_PAIS_DESTINO IS NULL then ' ' when ss.COD_PAIS_DESTINO IS NOT NULL then ss.COD_PAIS_DESTINO end COD_PAIS_DESTINO, ");
		sql.append("case when ss.DESC_PAIS_DESTINO IS NULL then ' ' when ss.DESC_PAIS_DESTINO IS NOT NULL then ss.DESC_PAIS_DESTINO end DESC_PAIS_DESTINO, ");
		sql.append("case when ss.NUM_PASAPORTE IS NULL then ' ' when ss.NUM_PASAPORTE IS NOT NULL then ss.NUM_PASAPORTE end NUM_PASAPORTE, ");
		sql.append("case when ss.NUM_BOLETO IS NULL then ' ' when ss.NUM_BOLETO IS NOT NULL then ss.NUM_BOLETO end NUM_BOLETO, ");
		sql.append("case when ss.CTA_ABONO IS NULL then ' ' when ss.CTA_ABONO IS NOT NULL then ss.CTA_ABONO end CTA_ABONO, ");
		sql.append("case when ss.CTA_ABONO_MONEDA IS NULL then ' ' when ss.CTA_ABONO_MONEDA IS NOT NULL then ss.CTA_ABONO_MONEDA end CTA_ABONO_MONEDA, ");
		sql.append("case when ss.NUM_BOLETO_VUELTA IS NULL then ' ' when ss.NUM_BOLETO_VUELTA IS NOT NULL then ss.NUM_BOLETO_VUELTA end NUM_BOLETO_VUELTA, ");
		sql.append("case when ss.ID_PRODUCTO IS NULL then 0 when ss.ID_PRODUCTO IS NOT NULL then ss.ID_PRODUCTO end ID_PRODUCTO, ");
		sql.append("case when ss.TASA_CAMBIO IS NULL then 0 when ss.TASA_CAMBIO IS NOT NULL then ss.TASA_CAMBIO end TASA_CAMBIO, ");
		sql.append("'' AS CEDRIF_NRO_CLI, '' AS CEDRIF_TIP_CLI, '' AS CEDRIF_NRO_CONTRA, '' AS CEDRIF_TIP_CONTRA ");
		sql.append("FROM solicitudes_sitme ss where 1=1");
		
		if(id_orden>0){
			sql.append(" and ss.ID_ORDEN=").append(id_orden);
		}
		if(nro_orden_infi>0){
			sql.append(" and ss.NUMERO_ORDEN_INFI=").append(nro_orden_infi);
		}
		if(name_client!=null && !name_client.equals("")){
			if(like) {
				String name[] = name_client.toUpperCase().split(" ");
				sql.append(" and ss.NOMBRE_CLIENTE like '%");//.append(name_client.toUpperCase()).append("%'");
				for(int i=0; i<name.length; i++){
					sql.append(name[i].trim()).append("%");
				}
				sql.append("'");
			}
			else sql.append(" and ss.NOMBRE_CLIENTE='").append(name_client.toUpperCase()).append("'");
		}
		if(ci_rif_client!=null && !ci_rif_client.equals("")){
			if(truncCedRif){
				if(like) sql.append(" and substr(ss.CED_RIF_CLIENTE, 1, length(ss.CED_RIF_CLIENTE)-1) like '%").append(ci_rif_client).append("%'");
				else sql.append(" and substr(ss.CED_RIF_CLIENTE, 1, length(ss.CED_RIF_CLIENTE)-1)='").append(ci_rif_client).append("'");
			}else{
				if(like) sql.append(" and ss.CED_RIF_CLIENTE like '%").append(ci_rif_client).append("%'");
				else sql.append(" and ss.CED_RIF_CLIENTE='").append(ci_rif_client).append("'");
			}
		}
		if(nro_rusitme!=null && !nro_rusitme.equals("")){
			if(like) sql.append(" and ss.NUM_RUSITME like '%").append(nro_rusitme).append("%'");
			else sql.append(" and ss.NUM_RUSITME='").append(nro_rusitme).append("'");
		}
		if(nro_bloqueo!=null && !nro_bloqueo.equals("")){
			if(like) sql.append(" and ss.NUM_BLOQUEO like '%").append(nro_bloqueo).append("%'");
			else sql.append(" and ss.NUM_BLOQUEO='").append(nro_bloqueo).append("'");
		}
		if(status!=null && !status.equals("")){
			if(like) sql.append(" and ss.ESTATUS like '%").append(status.toUpperCase()).append("%'");
			else{
				sql.append(" and ss.ESTATUS IN(");
				String arrStatus[] = status.split(",");
				for(int i=0; i<arrStatus.length; i++){
					sql.append("'").append(arrStatus[i].toUpperCase()).append("'");
					if(i<arrStatus.length-1) sql.append(", ");
				}
				sql.append(")");
			}
		}
		if(nro_cta!=null && !nro_cta.equals("")){
			if(like) sql.append(" and ss.CTA_NUMERO like '%").append(nro_cta).append("%'");
			else sql.append(" and ss.CTA_NUMERO='").append(nro_cta).append("'");
		}
		if(nro_pasaporte!=null && !nro_pasaporte.equals("")){
			if(like) sql.append(" and ss.NUM_PASAPORTE like '%").append(nro_pasaporte).append("%'");
			else sql.append(" and ss.NUM_PASAPORTE='").append(nro_pasaporte).append("'");
		}
		if(fechaRegistro.length>1 && fechaRegistro[0]!=null && fechaRegistro[1]!=null){
			sql.append(" and ss.FECHA_RECEPCION_REC BETWEEN to_date('").append(fechaRegistro[0]).append("','").append(ConstantesGenerales.FORMATO_FECHA3).append("')");
			sql.append(" AND to_date('").append(fechaRegistro[1]).append("','").append(ConstantesGenerales.FORMATO_FECHA3).append("')");
		}
		//System.out.println("SELECT Solicitudes SITME: \n"+sql);
		dataSet = db.get(dataSource, sql.toString());
		
		if(dataSet.count() > 0){ //Si se hallo al menos un registro que cumpla con las restricciones
			//arrSc = new ArrayList<SolicitudClavenet>();
			String arrTipNro[] = new String[2];
			dataSet.first();
			for(int i=0; i<dataSet.count(); i++){
				dataSet.next();
				sc = new SolicitudClavenet();
				sc.setIdOrden(Long.parseLong(dataSet.getValue("ID_ORDEN")));
				sc.setIdOrdenInfi(Long.parseLong(dataSet.getValue("NUMERO_ORDEN_INFI")));
				//TODO dar formato de Titulo al nombre cliente
				sc.setNombreCliente((dataSet.getValue("NOMBRE_CLIENTE").trim()));
				dataSet.setValue("NOMBRE_CLIENTE", dataSet.getValue("NOMBRE_CLIENTE").trim());
				sc.setCedRifCliente(dataSet.getValue("CED_RIF_CLIENTE").trim());
				dataSet.setValue("CED_RIF_CLIENTE", dataSet.getValue("CED_RIF_CLIENTE").trim());
				arrTipNro = sc.getTipNroFromCedRif(sc.getCedRifCliente());
				sc.setTipPerCli(arrTipNro[0]);
//System.out.println("TIP_PER CLIENTE: "+sc.getTipPerCli());
				dataSet.setValue("CEDRIF_TIP_CLI", sc.getTipPerCli());
//System.out.println("CEDRIF_TIP_CLI: "+dataSet.getValue("CEDRIF_TIP_CLI"));
				sc.setCedRifNroCli(arrTipNro[1]);
//System.out.println("CED_NRO CLIENTE: "+sc.getCedRifNroCli());
				dataSet.setValue("CEDRIF_NRO_CLI", sc.getCedRifNroCli());
//System.out.println("CEDRIF_NRO_CLI: "+dataSet.getValue("CEDRIF_NRO_CLI"));
				if(dataSet.getValue("FECHA_VENC_RIF")!=null && dataSet.getValue("FECHA_VENC_RIF").length()>0){
					try{
						sc.setFechaVencRif(sdf.parse(dataSet.getValue("FECHA_VENC_RIF")));
						//dataSet.setValue("FECHA_VENC_RIF", sdf.format(sc.getFechaVencRif()));
					}catch (ParseException ex) {
						ex.printStackTrace();
					}
				}else{
					sc.setFechaVencRif(null);
					dataSet.setValue("FECHA_VENC_RIF", "");
				}
//System.out.println("FechaVencRif: "+dataSet.getValue("FECHA_VENC_RIF"));
				sc.setNumRuSitme(dataSet.getValue("NUM_RUSITME").trim());
				dataSet.setValue("NUM_RUSITME", dataSet.getValue("NUM_RUSITME").trim());
				sc.setEmailCliente(dataSet.getValue("EMAIL_CLIENTE").trim());
				dataSet.setValue("EMAIL_CLIENTE", dataSet.getValue("EMAIL_CLIENTE").trim());
				sc.setCedRifContraparte(dataSet.getValue("CED_RIF_CONTRAPARTE").trim());
				dataSet.setValue("CED_RIF_CONTRAPARTE", dataSet.getValue("CED_RIF_CONTRAPARTE").trim());
				arrTipNro = sc.getTipNroFromCedRif(sc.getCedRifContraparte());
				sc.setTipPerContra(arrTipNro[0]);
//System.out.println("TIP_PER Contra: "+sc.getTipPerContra());
				dataSet.setValue("CEDRIF_TIP_CONTRA", sc.getTipPerContra());
				sc.setCedRifNroContra(arrTipNro[1]);
//System.out.println("CED_NRO Contra: "+sc.getCedRifNroContra());
				dataSet.setValue("CEDRIF_NRO_CONTRA", sc.getCedRifNroContra());
				sc.setMontoSolicitado(new BigDecimal(dataSet.getValue("MONTO_SOLICITADO")).setScale(2, BigDecimal.ROUND_HALF_EVEN));
				dataSet.setValue("MONTO_SOLICITADO", sc.getMontoSolicitado().toString());
//System.out.println("Monto Solic: "+sc.getMontoSolicitado());
				sc.setMontoComision(new BigDecimal(dataSet.getValue("MONTO_COMISION")).setScale(2, BigDecimal.ROUND_HALF_EVEN));
				dataSet.setValue("MONTO_COMISION", sc.getMontoComision().toString());
//System.out.println("Monto Comis: "+sc.getMontoComision());
				//sc.setPorcentajeComision(Double.parseDouble(dataSet.getValue("PORC_COMISION")));
				sc.setPorcentajeComision(new BigDecimal(dataSet.getValue("PORC_COMISION")).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue());
				dataSet.setValue("PORC_COMISION", sc.getPorcentajeComision()+"");
//System.out.println("Porc Comision: "+sc.getPorcentajeComision());
				sc.setNumeroBloqueo(dataSet.getValue("NUM_BLOQUEO").trim());
				dataSet.setValue("NUM_BLOQUEO", dataSet.getValue("NUM_BLOQUEO").trim());
				sc.setMontoAdjudicado(new BigDecimal(dataSet.getValue("MONTO_ADJUDICADO")).setScale(2, BigDecimal.ROUND_HALF_EVEN));
				dataSet.setValue("MONTO_ADJUDICADO", sc.getMontoAdjudicado().toString());
				sc.setIdConcepto(Long.parseLong(dataSet.getValue("ID_CONCEPTO")));
				sc.setSectorProductivo(Long.parseLong(dataSet.getValue("SECTOR_PRODUCTIVO")));
				sc.setSectorActividadEconomica(Long.parseLong(dataSet.getValue("SECTOR_ACT_ECONOM")));
				sc.setGrupoEconomico(dataSet.getValue("GRUPO_ECONOMICO").trim());
				dataSet.setValue("GRUPO_ECONOMICO", dataSet.getValue("GRUPO_ECONOMICO").trim());
				sc.setEstatus(dataSet.getValue("ESTATUS").trim());
				dataSet.setValue("ESTATUS", dataSet.getValue("ESTATUS").trim());
				if(dataSet.getValue("FECHA_REGISTRO")!=null && dataSet.getValue("FECHA_REGISTRO").length()>0){
					sc.setFechaRegistro(dataSet.getValue("FECHA_REGISTRO").trim());
				}else{
					sc.setFechaRegistro("");
					dataSet.setValue("FECHA_REGISTRO", "");
				}
				sc.setHoraRegistro(dataSet.getValue("HORA_REGISTRO").trim());
				dataSet.setValue("HORA_REGISTRO", dataSet.getValue("HORA_REGISTRO").trim());
				if(dataSet.getValue("FECHA_TRAMITE")!=null && dataSet.getValue("FECHA_TRAMITE").length()>0){
					sc.setFechaTramite(dataSet.getValue("FECHA_TRAMITE").trim());
				}else{
					sc.setFechaTramite("");
					dataSet.setValue("FECHA_TRAMITE", "");
				}
				if(dataSet.getValue("FECHA_ADJUDICACION")!=null && dataSet.getValue("FECHA_ADJUDICACION").length()>0){
					sc.setFechaAdjudicacion(dataSet.getValue("FECHA_ADJUDICACION").trim());
				}else{
					sc.setFechaAdjudicacion("");
					dataSet.setValue("FECHA_ADJUDICACION", "");
				}
				if(dataSet.getValue("FECHA_COBRO")!=null && dataSet.getValue("FECHA_COBRO").length()>0){
					sc.setFechaCobro(dataSet.getValue("FECHA_COBRO").trim());
				}else{
					sc.setFechaCobro("");
					dataSet.setValue("FECHA_COBRO", "");
				}
				if(dataSet.getValue("FECHA_ENV_SWIFT")!=null && dataSet.getValue("FECHA_ENV_SWIFT").length()>0){
					sc.setFechaEnvioSwift(dataSet.getValue("FECHA_ENV_SWIFT").trim());
				}else{
					sc.setFechaEnvioSwift("");
					dataSet.setValue("FECHA_ENV_SWIFT", "");
				}
				if(dataSet.getValue("FECHA_RES_SWIFT")!=null && dataSet.getValue("FECHA_RES_SWIFT").length()>0){
					sc.setFechaRecepSwift(dataSet.getValue("FECHA_RES_SWIFT").trim());
				}else{
					sc.setFechaEnvioSwift("");
					dataSet.setValue("FECHA_RES_SWIFT", "");
				}
				sc.setTituloAdjudicado(dataSet.getValue("TITULO_ADJUDICADO").trim());
				dataSet.setValue("TITULO_ADJUDICADO", dataSet.getValue("TITULO_ADJUDICADO").trim());
				sc.setPrecioAdjudicacion(Double.parseDouble(dataSet.getValue("MONTO_ADJUDICADO")));
				dataSet.setValue("MONTO_ADJUDICADO", sc.getPrecioAdjudicacion()+"");
				sc.setValorEfectivoUSD(new BigDecimal(dataSet.getValue("VALOR_EFECTIVO_USD")).setScale(2, BigDecimal.ROUND_HALF_EVEN));
				dataSet.setValue("VALOR_EFECTIVO_USD", sc.getValorEfectivoUSD().toString());
				sc.setValorNominalUSD(new BigDecimal(dataSet.getValue("VALOR_NOMINAL_USD")).setScale(2, BigDecimal.ROUND_HALF_EVEN));
				dataSet.setValue("VALOR_NOMINAL_USD", sc.getValorNominalUSD().toString());
				sc.setValorEfectivo(new BigDecimal(dataSet.getValue("VALOR_EFECTIVO")).setScale(2, BigDecimal.ROUND_HALF_EVEN));
				dataSet.setValue("VALOR_EFECTIVO", sc.getValorEfectivo().toString());
				sc.setValorNominal(new BigDecimal(dataSet.getValue("VALOR_NOMINAL")).setScale(2, BigDecimal.ROUND_HALF_EVEN));
				dataSet.setValue("VALOR_NOMINAL", sc.getValorNominal().toString());
				sc.setCuentaBsO(dataSet.getValue("CUENTA_BSF_O").trim());
				dataSet.setValue("CUENTA_BSF_O", dataSet.getValue("CUENTA_BSF_O").trim());
				sc.setNombreBeneficiario(dataSet.getValue("NOMBRE_BENEFICIARIO").trim());
				dataSet.setValue("NOMBRE_BENEFICIARIO", dataSet.getValue("NOMBRE_BENEFICIARIO").trim());
				sc.setCtaNro(dataSet.getValue("CTA_NUMERO").trim());
				dataSet.setValue("CTA_NUMERO", dataSet.getValue("CTA_NUMERO").trim());
				sc.setCtaBco(dataSet.getValue("CTA_BANCO").trim());
				dataSet.setValue("CTA_BANCO", dataSet.getValue("CTA_BANCO").trim());
				sc.setCtaDireccion(dataSet.getValue("CTA_DIRECCION").trim());
				dataSet.setValue("CTA_DIRECCION", dataSet.getValue("CTA_DIRECCION").trim());
				sc.setCtaBicSwift(dataSet.getValue("CTA_BIC_SWIFT").trim());
				dataSet.setValue("CTA_BIC_SWIFT", dataSet.getValue("CTA_BIC_SWIFT").trim());
				sc.setCtaTlf(dataSet.getValue("CTA_TELEFONO").trim());
				dataSet.setValue("CTA_TELEFONO", dataSet.getValue("CTA_TELEFONO").trim());
				sc.setCtaTlf2(dataSet.getValue("CTA_TELEFONO_2").trim());
				dataSet.setValue("CTA_TELEFONO_2", dataSet.getValue("CTA_TELEFONO_2").trim());
				sc.setCtaTlf3(dataSet.getValue("CTA_TELEFONO_3").trim());
				dataSet.setValue("CTA_TELEFONO_3", dataSet.getValue("CTA_TELEFONO_3").trim());
				sc.setCtaAba(dataSet.getValue("CTA_ABA").trim());
				dataSet.setValue("CTA_ABA", dataSet.getValue("CTA_ABA").trim());
				sc.setCtaDireccionC(dataSet.getValue("CTA_DIRECCION_C").trim());
				dataSet.setValue("CTA_DIRECCION_C", dataSet.getValue("CTA_DIRECCION_C").trim());
				sc.setCtaIban(dataSet.getValue("CTA_IBAN").trim());
				dataSet.setValue("CTA_IBAN", dataSet.getValue("CTA_IBAN").trim());
				sc.setCtaIntNro(dataSet.getValue("CTA_INT_NUMERO").trim());
				dataSet.setValue("CTA_INT_NUMERO", dataSet.getValue("CTA_INT_NUMERO").trim());
				sc.setCtaIntBco(dataSet.getValue("CTA_INT_BANCO").trim());
				dataSet.setValue("CTA_INT_BANCO", dataSet.getValue("CTA_INT_BANCO").trim());
				sc.setCtaIntDireccion(dataSet.getValue("CTA_INT_DIRECCION").trim());
				dataSet.setValue("CTA_INT_DIRECCION", dataSet.getValue("CTA_INT_DIRECCION").trim());
				sc.setCtaIntBicSwift(dataSet.getValue("CTA_INT_BIC_SWIFT").trim());
				dataSet.setValue("CTA_INT_BIC_SWIFT", dataSet.getValue("CTA_INT_BIC_SWIFT").trim());
				sc.setCtaIntTlf(dataSet.getValue("CTA_INT_TELEFONO").trim());
				dataSet.setValue("CTA_INT_TELEFONO", dataSet.getValue("CTA_INT_TELEFONO").trim());
				sc.setCtaIntAba(dataSet.getValue("CTA_INT_ABA").trim());
				dataSet.setValue("CTA_INT_ABA", dataSet.getValue("CTA_INT_ABA").trim());
				sc.setContadorEnviosSwift(Long.parseLong(dataSet.getValue("CONTADOR_ENV_SWIFT")));
				sc.setIdCanal(Long.parseLong(dataSet.getValue("ID_CANAL").trim()));
				if(dataSet.getValue("FECHA_MODIFICACION")!=null && dataSet.getValue("FECHA_MODIFICACION").length()>0){
					try{
						sc.setFechaModificacion(sdf.parse(dataSet.getValue("FECHA_MODIFICACION")));
					}catch (ParseException ex) {
						ex.printStackTrace();
					}
				}else{
					sc.setFechaModificacion(null);
					dataSet.setValue("FECHA_MODIFICACION", "");
				}
				if(dataSet.getValue("FECHA_SALIDA_VIAJE")!=null && dataSet.getValue("FECHA_SALIDA_VIAJE").length()>0){
					try{
						sc.setFechaSalidaViaje(sdf.parse(dataSet.getValue("FECHA_SALIDA_VIAJE")));
//System.out.println("----------/////////--------FECHA SALIDA DAO: "+sdf.format(sc.getFechaSalidaViaje()));
					}catch (ParseException ex) {
						ex.printStackTrace();
					}
				}else{
					sc.setFechaSalidaViaje(null);
					dataSet.setValue("FECHA_SALIDA_VIAJE", "");
				}
				if(dataSet.getValue("FECHA_RETORNO_VIAJE")!=null && dataSet.getValue("FECHA_RETORNO_VIAJE").length()>0){
					try{
						sc.setFechaRetornoViaje(sdf.parse(dataSet.getValue("FECHA_RETORNO_VIAJE")));
//System.out.println("----------/////////--------FECHA RETORNO DAO: "+sdf.format(sc.getFechaRetornoViaje()));
					}catch (ParseException ex) {
						ex.printStackTrace();
					}
				}else{
					sc.setFechaRetornoViaje(null);
					dataSet.setValue("FECHA_RETORNO_VIAJE", "");
				}
				sc.setCodigoPaisOrigen(dataSet.getValue("COD_PAIS_ORIGEN").trim());
				dataSet.setValue("COD_PAIS_ORIGEN", dataSet.getValue("COD_PAIS_ORIGEN").trim());
				sc.setDescPaisOrigen(dataSet.getValue("DESC_PAIS_ORIGEN").trim());
				dataSet.setValue("DESC_PAIS_ORIGEN", dataSet.getValue("DESC_PAIS_ORIGEN").trim());
				sc.setCodigoEdoOrigen(dataSet.getValue("COD_ESTADO_ORIGEN").trim());
				dataSet.setValue("COD_ESTADO_ORIGEN", dataSet.getValue("COD_ESTADO_ORIGEN").trim());
				sc.setDescEdoOrigen(dataSet.getValue("DESC_ESTADO_ORIGEN").trim());
				dataSet.setValue("DESC_ESTADO_ORIGEN", dataSet.getValue("DESC_ESTADO_ORIGEN").trim());
				sc.setCodigoCiudadOrigen(dataSet.getValue("COD_CIUDAD_ORIGEN").trim());
				dataSet.setValue("COD_CIUDAD_ORIGEN", dataSet.getValue("COD_CIUDAD_ORIGEN").trim());
				sc.setDescCiudadOrigen(dataSet.getValue("DESC_CIUDAD_ORIGEN").trim());
				dataSet.setValue("DESC_CIUDAD_ORIGEN", dataSet.getValue("DESC_CIUDAD_ORIGEN").trim());
				sc.setCodigoPaisDestino(dataSet.getValue("COD_PAIS_DESTINO").trim());
				dataSet.setValue("COD_PAIS_DESTINO", dataSet.getValue("COD_PAIS_DESTINO").trim());
				sc.setDescPaisDestino(dataSet.getValue("DESC_PAIS_DESTINO").trim());
				dataSet.setValue("DESC_PAIS_DESTINO", dataSet.getValue("DESC_PAIS_DESTINO").trim());
				sc.setNroPasaporte(dataSet.getValue("NUM_PASAPORTE").trim());
				dataSet.setValue("NUM_PASAPORTE", dataSet.getValue("NUM_PASAPORTE").trim());
				sc.setNroBoleto(dataSet.getValue("NUM_BOLETO").trim());
				dataSet.setValue("NUM_BOLETO", dataSet.getValue("NUM_BOLETO").trim());
				if(!dataSet.getValue("CTA_ABONO").trim().equals("") && dataSet.getValue("CTA_ABONO").trim()!=null){
					sc.setCtaAbono(Integer.parseInt(dataSet.getValue("CTA_ABONO").trim()));
				}else{
					sc.setCtaAbono(0);
				}
				dataSet.setValue("CTA_ABONO", dataSet.getValue("CTA_ABONO").trim());
				sc.setCtaAbonoMoneda(dataSet.getValue("CTA_ABONO_MONEDA").trim());
				dataSet.setValue("CTA_ABONO_MONEDA", dataSet.getValue("CTA_ABONO_MONEDA").trim());
				sc.setNroBoletoVuelta(dataSet.getValue("NUM_BOLETO_VUELTA").trim());
				dataSet.setValue("NUM_BOLETO_VUELTA", dataSet.getValue("NUM_BOLETO_VUELTA").trim());
				sc.setIdProducto(Integer.parseInt(dataSet.getValue("ID_PRODUCTO")));
				dataSet.setValue("ID_PRODUCTO", dataSet.getValue("ID_PRODUCTO").trim());
				sc.setTasaCambio(Double.parseDouble(dataSet.getValue("TASA_CAMBIO")));
				dataSet.setValue("TASA_CAMBIO", sc.getTasaCambio()+"");
				
				arrSc.add(sc);
				
			}//for
		}//if
		
		return arrSc;
	}
	
	
	/*
	 * Obtiene el tamaño del campo indicado de la tabla
	 * */
	public int getTamCampo(String campoName) throws Exception {				
		int tam = 0;
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT MAX(length(").append(campoName).append(")) AS tam FROM solicitudes_sitme");
		//System.out.println("SELECT getTamCampo: \n"+sql.toString());
		dataSet = db.get(dataSource, sql.toString());
		
		if(dataSet.count() > 0){ //Se obtuvo el tamaño
			dataSet.first();
			dataSet.next();
			if(dataSet.getValue("tam")!=null && !dataSet.getValue("tam").equals("")){
				tam = Integer.parseInt(dataSet.getValue("tam"));
			}
		}
		return tam;
	}
	
	
	/**
	 * Actualiza solicitudes sitme filtradas por su id de orden (setea la fecha y hora de registro y la fecha de modificación a la del sysdate)
	 * @param id_orden: ID de la solicitud en solicitudes_sitme
	 * @param nro_orden_infi: ID de la orden INFI
	 * @param status: Estatus al que se actualizara la orden
	 * */
	public void updateSolicitud(long id_orden, long nro_orden_infi, String status) throws Exception {
		
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE solicitudes_sitme ss SET ");
		sql.append("ss.NUMERO_ORDEN_INFI=").append(nro_orden_infi).append(", ");
		sql.append("ss.ESTATUS='").append(status).append("', ");
		sql.append("ss.FECHA_REGISTRO=TO_DATE(SYSDATE,'").append(ConstantesGenerales.FORMATO_FECHA_SYSDATE).append("'), ");
		sql.append("ss.HORA_REGISTRO=TO_CHAR(SYSDATE,'").append(ConstantesGenerales.FORMATO_HORA_24).append("'), ");
		sql.append("ss.FECHA_MODIFICACION=TO_DATE(SYSDATE,'").append(ConstantesGenerales.FORMATO_FECHA_SYSDATE).append("') ");
		sql.append("WHERE ss.ID_ORDEN=").append(id_orden);	
		
//System.out.println("UPDATEEEEEEEEEEEEEEEEEEEEEEEEE:\n"+sql.toString());
		
		db.exec(dataSource, sql.toString());
		
	}
		
	/*Metodo creado en Proceso Optimizacion Carga y Cierre de Cruce
	 * NM26659 08/09/2014
	 * */
	public boolean exiteOrden(long idOrdenInfi)throws Exception{
		boolean flag=false;
		StringBuffer sql=new StringBuffer();
		
		sql.append("SELECT ID_ORDEN FROM SOLICITUDES_SITME SS WHERE SS.NUMERO_ORDEN_INFI =").append(idOrdenInfi);
		
		
		dataSet = db.get(dataSource, sql.toString());
		System.out.println("exiteOrden ---> " + sql.toString());
		if(dataSet.count()>0){
			System.out.println("TIENE CONDICION");
			flag=true;
		}else {
			System.out.println("NO TIENE CONDICION");
		}
			
		
		return flag;
	}
	
	public Object moveNext() throws Exception {
		return null;
	}
	
	/**Lista las ordenes asociadas a una Unidad de Inversi&oacute;n espec&iacute;fica
	 * @param idUnidadInversion id de la unidad de inversión
	 * @throws Exception en caso de error NM32454 INFI_TTS_491_WEB_SERVICE 27/02/2014
	 */
	public void listarOrdenesPorEnviarBCV(Integer incluirCliente, String cedRifCliente,Integer tasaMinima, Integer tasaMaxima, Integer montoMinimo, Integer montoMaximo,boolean totales,long idUnidadInversion,boolean paginado, int paginaAMostrar, int registroPorPagina, boolean todos, boolean incluir, String ordenesSeleccionadas, String ordeneEstatusBCV, String fecha) throws Exception{
		String condicionUI = "";
		String condicionTasa = "";
		String condicionMonto = "";
		String condicionCliente = "";
		StringBuilder sql = new StringBuilder();
		
		if(idUnidadInversion != 0){
			condicionUI = "and ui.undinv_id="+idUnidadInversion;
		}
		
		if((tasaMinima != null && tasaMinima != 0)){
			condicionTasa = " AND s.TASA_CAMBIO >= "+tasaMinima;
		}
		
		if((tasaMaxima != null && tasaMaxima != 0)){
			condicionTasa += " AND s.TASA_CAMBIO <= "+tasaMaxima;
		}
		
		if((montoMinimo != null && montoMinimo != 0)){
			condicionMonto = " AND s.MONTO_SOLICITADO >= "+montoMinimo;
		}
		
		if((montoMaximo != null && montoMaximo != 0)){
			condicionMonto += " AND s.MONTO_SOLICITADO <= "+montoMaximo;
		}
		
		if((cedRifCliente != null && !cedRifCliente.equals("0"))){
			if(incluirCliente.equals(1)){ //SE INCLUYE AL CLIENTE
				condicionCliente = " AND s.CED_RIF_CLIENTE = '"+cedRifCliente+"'";
			}else { //SE EXCLUYE AL CLIENTE
				condicionCliente = " AND s.CED_RIF_CLIENTE <> '"+cedRifCliente+"'";
			}
		}
		
		if(!totales){
			sql.append("SELECT s.OBSERVACION, s.ID_ORDEN, s.NOMBRE_CLIENTE,  s.CED_RIF_CLIENTE, s.MONTO_SOLICITADO,  " )
		    .append(" ui.UNDINV_NOMBRE, s.TASA_CAMBIO, s.CTA_NUMERO, s.EMAIL_CLIENTE,  " )
		    .append("CASE s.ID_BCV WHEN '0' THEN '' ELSE s.ID_BCV END ID_BCV,  " )
		    .append("CASE s.estatus_bcv ")
		    .append("   WHEN 0 THEN 'Sin Verificar' ")
		    .append("   WHEN 1 THEN 'Verificada Aprobada' ")
		    .append("   WHEN 2 THEN 'Verificada Rechazada' ")
		    .append(" END estatus_string ");
		}else {
			sql.append("SELECT SUM(s.MONTO_SOLICITADO) monto_operacion, COUNT(1) cantidad_operaciones ");
		}
		
	    sql.append(" FROM infi_tb_106_unidad_inversion ui," )
	    .append("  solicitudes_sitme s   ")
	    .append(" WHERE ui.UNDINV_ID        = s.UNDINV_ID ")
	    .append(condicionUI)
	    .append(" AND ui.TIPO_NEGOCIO = ").append(ConstantesGenerales.TIPO_NEGOCIO_BAJO_VALOR) //TIPO DE NEGOCIO PARA UI DE BAJO VALOR
	    .append(" AND s.ID_PRODUCTO   = ").append(ConstantesGenerales.PRODUCTO_OFERTA) //ID PRODUCTO PARA LA OFERTA
	    .append(" AND TO_DATE(s.FECHA_REGISTRO,'DD/MM/RRRR') = TO_DATE('").append(fecha).append("', 'DD/MM/RRRR')")
		.append(" AND s.ESTATUS_BCV IN (").append(ordeneEstatusBCV).append(")") //ESTATUS DE LA ORDEN BCV, SIN VERIFICAR, VERIFICADA O RECHZADA
	    .append(condicionMonto)
		.append(condicionTasa)
		.append(condicionCliente);
	    
		if(!todos){
			if(incluir){
		    	sql.append(" AND s.ID_ORDEN in("+ordenesSeleccionadas+")");
		    } else {
		    	sql.append(" AND s.ID_ORDEN not in("+ordenesSeleccionadas+")");
		    }
		}
		
		if (paginado && paginaAMostrar>0 && registroPorPagina> 0){			
			dataSet = obtenerDataSetPaginado(sql.toString(),paginaAMostrar,registroPorPagina);
		}else{
			dataSet = db.get(dataSource, sql.toString());
		}
		System.out.println("listarOrdenesPorEnviarBCV--> "+sql);
	}
	
	//NM32454 INFI_TTS_491_WS_BCV 03/03/2015
	public void actualizarOrdenBCV(String ordeneID, String ordenBCV, String estatus, String observacion) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE SOLICITUDES_SITME SET ESTATUS_BCV = ").append(estatus);
		
		if(ordenBCV != null){
			sql.append(" ,  ID_BCV = '").append(ordenBCV).append("'");
		}
		
		if(observacion !=null ){
			if(observacion.length() > 1000){
				observacion = observacion.substring(0,999);
			}
			sql.append(" ,  OBSERVACION = '").append(observacion).append("'");
		}
		sql.append(" WHERE ID_ORDEN =").append(ordeneID);
		db.exec(dataSource, sql.toString());
		System.out.println("actualizarOrdenBCV--->"+sql);
	}
	public void listarOrdenesMenudeoPorEnviarBCV() throws Exception{
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT VC.NRO_CEDULA_RIF, ");
		sql.append("VC.NACIONALIDAD, ");
		sql.append("VC.NOM_CLIEN, ");
		sql.append("VC.TASA_CAMBIO, ");
		sql.append("VC.COD_DIVISA");
		sql.append("VC.CTA_CLIEN");
		sql.append("VC.OPERACION");
		sql.append("VC.NACIONALIDAD");
		sql.append("VC.ID_OPER");
		sql.append("VC.CLIENT_CORREO_ELECTRONICO");
		sql.append("VC.CLIENT_TELEFONO");
		/////////////////////////////////////
		sql.append("FROM INFI_TB_234_VC_DIVISAS VC, ");
		sql.append(" ORDER by VC.ID_OPER ");
	}
}   

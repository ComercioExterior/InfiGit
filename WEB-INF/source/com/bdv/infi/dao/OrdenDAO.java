package com.bdv.infi.dao;    

import static com.bancovenezuela.comun.logic.interfaces.ConstantesGenerales.STATUS_EN_ESPERA;
import static com.bancovenezuela.comun.logic.interfaces.ConstantesGenerales.STATUS_RECHAZADA;
import static com.bancovenezuela.comun.logic.interfaces.ConstantesGenerales.TRANSACCION_PACTO_RECOMPRA;
import static com.bancovenezuela.comun.logic.interfaces.ConstantesGenerales.TRANSACCION_TOMA_ORDEN;
import static com.bancovenezuela.comun.logic.interfaces.ConstantesGenerales.TRANSACCION_VENTA_TITULOS;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import megasoft.DataSet;
import megasoft.db;

import org.apache.log4j.Logger;

import com.bancovenezuela.comun.data.OrdenOperacion;
import com.bdv.infi.data.Archivo;
import com.bdv.infi.data.CampoDinamico;
import com.bdv.infi.data.Detalle;
import com.bdv.infi.data.DocumentoDefinicion;
import com.bdv.infi.data.Orden;
import com.bdv.infi.data.OrdenDataExt;
import com.bdv.infi.data.OrdenDetalle;
import com.bdv.infi.data.OrdenDocumento;
import com.bdv.infi.data.OrdenTitulo;
import com.bdv.infi.data.OrdenesCruce;
import com.bdv.infi.data.SolicitudClavenet;
import com.bdv.infi.logic.ProcesarDocumentos;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.DataExtendida;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TipoInstruccion;
import com.bdv.infi.logic.interfaces.TransaccionFinanciera;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;
import com.bdv.infi.logic.interfaces.UsoCuentas;
import com.bdv.infi.util.Utilitario;

/** 
 * Clase usada para la l&oacute;gica de inserci&oacute;n, modificaci&oacute;n y recuperaci&oacute;n de las ordenes en la base de datos
 */
@SuppressWarnings({"rawtypes"})
public class OrdenDAO extends com.bdv.infi.dao.GenericoDAO {

	
	private Logger logger = Logger.getLogger(OrdenDAO.class);
	
	public OrdenDAO(DataSource ds) {
		super(ds);
	}
	
	public Object moveNext() throws Exception {
		return null;
	}
	/**
	 * Query para buscar las ordenes con todos sus campos
	 */
	private String sqlOrdenes="select transa_descripcion, (select INFI_TB_018_VEHICULOS.VEHICU_NOMBRE from INFI_TB_018_VEHICULOS where INFI_TB_018_VEHICULOS.VEHICU_ID=infi_tb_204_ordenes.ORDENE_VEH_TOM)as tomador,(select INFI_TB_018_VEHICULOS.VEHICU_NOMBRE from INFI_TB_018_VEHICULOS where INFI_TB_018_VEHICULOS.VEHICU_ID=infi_tb_204_ordenes.ORDENE_VEH_REC)as recompra_vec,(select INFI_TB_018_VEHICULOS.VEHICU_NOMBRE from INFI_TB_018_VEHICULOS where INFI_TB_018_VEHICULOS.VEHICU_ID=infi_tb_204_ordenes.ORDENE_VEH_COL)as colocador,INFI_TB_200_TIPO_PERSONAS.TIPPER_NOMBRE,case when infi_tb_204_ordenes.ENVIADO=0 then 'No' when infi_tb_204_ordenes.ENVIADO=1 then 'Si' end enviado_,case when infi_tb_204_ordenes.ORDENE_PED_IN_BDV=0 then 'No' when infi_tb_204_ordenes.ORDENE_PED_IN_BDV=1 then 'Si' end cartera_propia,case when infi_tb_204_ordenes.ORDENE_FINANCIADO=1 then 'Si' when infi_tb_204_ordenes.ORDENE_FINANCIADO=0 then 'No' end financiado,INFI_TB_016_EMPRESAS.EMPRES_NOMBRE,INFI_TB_802_SISTEMA.SISTEMA_NOMBRE,infi_tb_204_ordenes.*,infi_tb_203_ordenes_status.ordsta_nombre,infi_tb_201_ctes.CLIENT_NOMBRE,infi_tb_106_unidad_inversion.UNDINV_NOMBRE as unidad,infi_tb_102_bloter.BLOTER_DESCRIPCION,infi_tb_117_undinv_status.UNDINV_STATUS_DESCRIPCION from infi_tb_204_ordenes left join infi_tb_201_ctes on infi_tb_204_ordenes.CLIENT_ID= infi_tb_201_ctes.CLIENT_ID left join infi_tb_203_ordenes_status on infi_tb_204_ordenes.ordsta_id=infi_tb_203_ordenes_status.ordsta_id left join INFI_TB_802_SISTEMA on infi_tb_204_ordenes.SISTEMA_ID=INFI_TB_802_SISTEMA.SISTEMA_ID left join INFI_TB_200_TIPO_PERSONAS on infi_tb_201_ctes.TIPPER_ID=INFI_TB_200_TIPO_PERSONAS.TIPPER_ID left join INFI_TB_016_EMPRESAS on infi_tb_204_ordenes.EMPRES_ID=INFI_TB_016_EMPRESAS.EMPRES_ID left join infi_tb_106_unidad_inversion on infi_tb_204_ordenes.UNIINV_ID=infi_tb_106_unidad_inversion.UNDINV_ID left join infi_tb_102_bloter on infi_tb_204_ordenes.BLOTER_ID=infi_tb_102_bloter.BLOTER_ID left join infi_tb_117_undinv_status on infi_tb_106_unidad_inversion.UNDINV_STATUS=infi_tb_117_undinv_status.UNDINV_STATUS left join INFI_TB_012_TRANSACCIONES trans_neg on infi_tb_204_ordenes.transa_id = trans_neg.transa_id where 1=1";
	/**
	 * Lista todas las ordenes registradas por un status especifico y aquellas que hayan sido creadas
	 * entre dos fechas dadas. Aquellos par&aacute;metros recibidos en nulo no ser&aacute;n tomados en cuenta.
	 * @param cedulaRif C&eacute;dula o rif de un cliente	  
	 * @param status array con los diferentes status de la orden. Se pueden verificar de la interfaz StatusOrden  
	 * @param fechaDesde Fecha de inicio de la creaci&oacute;n de la orden
	 * @param fechaHasta Fecha de fin de la creaci&oacute;n de la orden
	 * */
	public void listar(String[] status, String cedulaRif,Date fechaDesde, Date fechaHasta ){	
	}

	/**
	 * Lista todas las ordenes registradas seg&uacute;n los par&aacute;metros recibidos. 
	 * Aquellos valores nulos se tomar&aacute;n como "todos"
	 * @param idCliente id del cliente. -1 o 0 Cuando se desean todos	  
	 * @param idTitulo id del t&iacute;tulo involucrado en una orden. 
	 * @param idTransaccion id de la transacci&oacute;n espec&iacute;fica. Si es  
	 * @param fechaDesde Fecha de inicio de la creaci&oacute;n de la orden
	 * @param fechaHasta Fecha de fin de la creaci&oacute;n de la orden
	 * @param desplegarOperaciones Indica si se deben buscar las transacciones financieras asociadas a las ordenes
	 * @throws Exception 
	 * */	
	@SuppressWarnings("deprecation")
	public void listarMovimientos(long idCliente, String idTitulo, String idTransaccion, Date fechaDesde, Date fechaHasta, boolean desplegarOperaciones) throws Exception{
		//Se debe usar la clase CustodiaDetalle e incorporarle CustodiaBloqueo
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		
		sql.append("SELECT ord.*, cl.client_nombre, tr.transa_descripcion FROM INFI_TB_204_ORDENES ord, INFI_TB_012_TRANSACCIONES tr, INFI_TB_201_CTES cl WHERE ord.transa_id = tr.transa_id AND ord.client_id = cl.client_id");
				
		if(idCliente!=0)
			filtro.append(" AND ord.client_id = ").append(idCliente);
		
		if(idTitulo!=null)
			filtro.append(" AND ('").append(idTitulo).append("'").append(" IN (SELECT ot.titulo_id from INFI_TB_206_ORDENES_TITULOS ot WHERE ot.ordene_id = ord.ordene_id))");
		
		if(fechaDesde!=null)
			filtro.append(" AND ord.ordene_ped_fe_orden >= to_date('").append(fechaDesde.getDate()).append("/").append(fechaDesde.getMonth()+1).append("/").append(fechaDesde.getYear()+1900).append("',").append("'dd/mm/yyyy'").append(")");
			
		if(fechaHasta!=null)
			filtro.append(" AND ord.ordene_ped_fe_orden <= to_date('").append(fechaHasta.getDate()).append("/").append(fechaHasta.getMonth()+1).append("/").append(fechaHasta.getYear()+1900).append("',").append("'dd/mm/yyyy'").append(")");
					
		sql.append(filtro);
		sql.append(" ORDER BY cl.client_nombre, ord.ordene_ped_fe_orden ");
		
		dataSet = db.get(dataSource, sql.toString());
	}
	
	//NM32454 SIMADI TAQUILLA
	@SuppressWarnings("deprecation")
	public void listarOrdenes(long idCliente, Date fechaDesde, Date fechaHasta, String formaOrden) throws Exception{
		//Se debe usar la clase CustodiaDetalle e incorporarle CustodiaBloqueo
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		
		sql.append("SELECT ord.ORDENE_ID, ord.ORDENE_ADJ_MONTO ");
		sql.append("FROM infi_tb_204_ordenes ord, ");
		sql.append("infi_tb_106_unidad_inversion u, ");
		sql.append("infi_tb_101_inst_financieros insf ");
		sql.append("WHERE ord.UNIINV_ID  = u.UNDINV_ID ");
		sql.append(" AND insf.INSFIN_ID = u.INSFIN_ID ");
		
		if(idCliente!=0){
			filtro.append(" AND ord.client_id = ").append(idCliente);
		}
		
		if(fechaDesde!=null){
			filtro.append(" AND ord.ordene_ped_fe_orden >= to_date('").append(fechaDesde.getDate()).append("/").append(fechaDesde.getMonth()+1).append("/").append(fechaDesde.getYear()+1900).append("',").append("'dd/mm/yyyy'").append(")");
		}
		
		if(fechaHasta!=null){
			filtro.append(" AND ord.ordene_ped_fe_orden <= to_date('").append(fechaHasta.getDate()).append("/").append(fechaHasta.getMonth()+1).append("/").append(fechaHasta.getYear()+1900).append("',").append("'dd/mm/yyyy'").append(")");
		}
		
		if(formaOrden!=null){
			filtro.append(" AND insf.INSFIN_FORMA_ORDEN = '").append(formaOrden).append("'");
		}
		
		sql.append(filtro);
		dataSet = db.get(dataSource, sql.toString());
	}	
	
	public void listarMovimientosFinancieros(String idCliente, String idTitulo, String fechaDesde, String fechaHasta, String statusOperacion, String transaccion) throws Exception{
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		
		sql.append("select t.titulo_id, to_char(o.ordene_ped_fe_orden,'").append(ConstantesGenerales.FORMATO_FECHA).append("') as ordene_ped_fe_orden," ).append(" op.ordene_operacion_id, op.status_operacion,op.ordene_id, c.client_nombre, t.titulo_descripcion, tr.transa_descripcion,DECODE (op.moneda_id,'VEF',fvj8ocon0(op.monto_operacion,'20180503'),op.monto_operacion) monto_operacion,decode(op.moneda_id, FVJ8ODIV0('BFE_SWIFT_OLD'),FVJ8ODIV0('BFE_SWIFT'),op.moneda_id) moneda_siglas,to_char(op.fecha_aplicar,'").append(ConstantesGenerales.FORMATO_FECHA).append("') as fecha_aplicar," ).append("tt.trnfin_tipo_descripcion, to_char (op.fecha_procesada,'").append(ConstantesGenerales.FORMATO_FECHA).append("') as fecha_procesada" ).append(" from INFI_TB_207_ORDENES_OPERACION op, INFI_TB_201_CTES c, INFI_TB_204_ORDENES o, INFI_TB_100_TITULOS t, INFI_TB_012_TRANSACCIONES tr, INFI_TB_203_ORDENES_STATUS os, INFI_TB_805_TRNFIN_TIPO tt where op.ordene_id=o.ordene_id and c.client_id=o.client_id and trim(t.titulo_id(+)) = trim(op.titulo_id) and o.transa_id=tr.transa_id and o.ordsta_id=os.ordsta_id and tt.trnfin_tipo(+)=op.trnf_tipo ");
				
		if(idCliente!=null)
			filtro.append(" and o.client_id = ").append(idCliente);
		
		if(idTitulo!=null)
			filtro.append(" and trim(op.titulo_id)=trim('").append(idTitulo).append("')");
		
		if((fechaDesde!=null)&&(fechaHasta!=null))
			filtro.append(" and to_date(o.ordene_ped_fe_orden,'").append(ConstantesGenerales.FORMATO_FECHA_RRRR).append("') between to_date('").append(fechaDesde).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("') and to_date('").append(fechaHasta).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("')");
		
		if(statusOperacion.equals("1"))
			filtro.append(" and op.status_operacion='").append(ConstantesGenerales.STATUS_APLICADA).append("'");
		
		if(statusOperacion.equals("2"))
			filtro.append(" and (op.status_operacion='").append(ConstantesGenerales.STATUS_RECHAZADA).append("' OR op.status_operacion='").append(ConstantesGenerales.STATUS_EN_ESPERA).append("')");
		
		if(transaccion!=null){
			filtro.append(" and o.transa_id='").append(transaccion).append("'");
		}else{
			filtro.append(" and o.transa_id in('").append(TransaccionNegocio.BLOQUEO_TITULOS).append("','").append(TransaccionNegocio.CUSTODIA_COMISIONES).append("','").append(TransaccionNegocio.DESBLOQUEO_TITULOS).append("','").append(TransaccionNegocio.ENTRADA_DE_TITULOS).append("','").append(TransaccionNegocio.PAGO_CUPON).append("','").append(TransaccionNegocio.CUSTODIA_AMORTIZACION).append("','").append(TransaccionNegocio.SALIDA_INTERNA).append("','").append(TransaccionNegocio.SALIDA_EXTERNA).append("')");
		}
					
		sql.append(filtro);
		sql.append(" order by o.ordene_ped_fe_orden, t.titulo_descripcion");
				//System.out.println("listarMovimientosFinancieros ---> " + sql.toString());
		dataSet = db.get(dataSource, sql.toString());
		System.out.println("listarMovimientosFinancieros "+sql);
	}
	/**
	 * Lista el resumen por unidad de inversión indicando por bloter
	 * número de ordenes, monto solicitado,monto adjudicado.
	 * @param unidadInversion
	 * @param fechaDesde
	 * @param fechaHasta
	 * @throws Exception
	 */
	public void listarResumenUnidadInversion(long unidadInversion,Date fechaDesde,Date fechaHasta) throws Exception{
	
		StringBuffer sql = new StringBuffer();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
		
		sql.append("select distinct infi_tb_204_ordenes.bloter_id,'' as monto_capital,'' as porcentaje,INFI_TB_102_BLOTER.bloter_descripcion,");
		
		sql.append("(select count(ordene_id) from infi_tb_204_ordenes a where a.transa_id in('");
		sql.append(TransaccionNegocio.TOMA_DE_ORDEN).append("','").append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA); 
		sql.append("') and a.uniinv_id=").append(unidadInversion).append(" and a.ordsta_id not in('").append(StatusOrden.CANCELADA).append("','").append(StatusOrden.PENDIENTE).append("') "); 
		sql.append(" and a.bloter_id=infi_tb_204_ordenes.bloter_id ");
		sql.append("and (ORDENE_PED_FE_ORDEN>=to_date('").append(simpleDateFormat.format(fechaDesde)).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("') and ORDENE_PED_FE_ORDEN<=to_date('").append(simpleDateFormat.format(fechaHasta)).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("'))");
		sql.append(")orden_por_bloter,");
		
		sql.append("(select undinv_tasa_cambio from infi_tb_106_unidad_inversion where infi_tb_106_unidad_inversion.undinv_id=infi_tb_204_ordenes.uniinv_id) tasa_de_cambio,");
		
		sql.append("(select nvl(sum(ordene_ped_comisiones),0) from infi_tb_204_ordenes a where a.transa_id in('");
		sql.append(TransaccionNegocio.TOMA_DE_ORDEN).append("','").append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA);
		sql.append("') and a.uniinv_id=").append(unidadInversion);
		sql.append(" and a.ordsta_id not in('").append(StatusOrden.CANCELADA).append("','").append(StatusOrden.PENDIENTE).append("')");
		sql.append(" and a.bloter_id=infi_tb_204_ordenes.bloter_id ");
		sql.append("and (ORDENE_PED_FE_ORDEN>=to_date('").append(simpleDateFormat.format(fechaDesde)).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("') and ORDENE_PED_FE_ORDEN<=to_date('").append(simpleDateFormat.format(fechaHasta)).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("'))");
		sql.append(") as monto_comision,");
		
		sql.append("(select nvl(sum(ordene_ped_total),0) from infi_tb_204_ordenes a where a.transa_id in('");
		sql.append(TransaccionNegocio.TOMA_DE_ORDEN).append("','").append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA);
		sql.append("') and a.uniinv_id=").append(unidadInversion);
		sql.append(" and a.ordsta_id not in('").append(StatusOrden.CANCELADA).append("','").append(StatusOrden.PENDIENTE).append("')");
		sql.append(" and a.bloter_id=infi_tb_204_ordenes.bloter_id ");
		sql.append("and (ORDENE_PED_FE_ORDEN>=to_date('").append(simpleDateFormat.format(fechaDesde)).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("') and ORDENE_PED_FE_ORDEN<=to_date('").append(simpleDateFormat.format(fechaHasta)).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("'))");
		sql.append(") as monto_ordenes,");
		
		sql.append("(select nvl(sum(ordene_ped_monto),0) from infi_tb_204_ordenes a where a.transa_id in('");
		sql.append(TransaccionNegocio.TOMA_DE_ORDEN).append("','").append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA);
		sql.append("') and a.uniinv_id=").append(unidadInversion);
		sql.append(" and a.ordsta_id not in('").append(StatusOrden.CANCELADA).append("','").append(StatusOrden.PENDIENTE).append("')");
		sql.append(" and a.bloter_id=infi_tb_204_ordenes.bloter_id ");
		sql.append("and (ORDENE_PED_FE_ORDEN>=to_date('").append(simpleDateFormat.format(fechaDesde)).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("') and ORDENE_PED_FE_ORDEN<=to_date('").append(simpleDateFormat.format(fechaHasta)).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("'))");
		sql.append(") as monto_solicitado,");
		
		sql.append("(select nvl(sum(ordene_ped_monto),0) from infi_tb_204_ordenes a where a.transa_id in('");
		sql.append(TransaccionNegocio.TOMA_DE_ORDEN).append("','").append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA);
		sql.append("') and a.uniinv_id=").append(unidadInversion);
		sql.append(" and a.ordsta_id not in('").append(StatusOrden.CANCELADA).append("','").append(StatusOrden.PENDIENTE).append("')");
		sql.append(" and a.bloter_id=infi_tb_204_ordenes.bloter_id ");
		sql.append("and (ORDENE_PED_FE_ORDEN>=to_date('").append(simpleDateFormat.format(fechaDesde)).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("') and ORDENE_PED_FE_ORDEN<=to_date('").append(simpleDateFormat.format(fechaHasta)).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("'))");
		sql.append(") * infi_tb_106_unidad_inversion.undinv_tasa_cambio as monto_solicitado_bs,");
		
		sql.append("(select nvl(sum(ordene_ped_int_caidos),0) from infi_tb_204_ordenes a where a.transa_id in('");
		sql.append(TransaccionNegocio.TOMA_DE_ORDEN).append("','").append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA);
		sql.append("') and a.uniinv_id=").append(unidadInversion);
		sql.append(" and a.ordsta_id not in('").append(StatusOrden.CANCELADA).append("','").append(StatusOrden.PENDIENTE).append("')");
		sql.append(" and a.bloter_id=infi_tb_204_ordenes.bloter_id ");
		sql.append("and (ORDENE_PED_FE_ORDEN>=to_date('").append(simpleDateFormat.format(fechaDesde)).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("') and ORDENE_PED_FE_ORDEN<=to_date('").append(simpleDateFormat.format(fechaHasta)).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("'))");
		sql.append(") as monto_interesescaidos,");
		
		sql.append("(select decode(sum(ordene_adj_monto),null,0,sum(ordene_adj_monto)) from infi_tb_204_ordenes a where a.transa_id in('");
		sql.append(TransaccionNegocio.TOMA_DE_ORDEN).append("','").append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA).append("') and a.uniinv_id=");
		sql.append(unidadInversion).append(" and a.ordsta_id not in('").append(StatusOrden.CANCELADA).append("','").append(StatusOrden.PENDIENTE).append("')");
		sql.append(" and a.bloter_id=infi_tb_204_ordenes.bloter_id ");
		sql.append("and (ORDENE_PED_FE_ORDEN>=to_date('").append(simpleDateFormat.format(fechaDesde)).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("') and ORDENE_PED_FE_ORDEN<=to_date('").append(simpleDateFormat.format(fechaHasta)).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("'))");
		sql.append(") as monto_adjudicado,");
		
		sql.append("(select decode(sum(ordene_adj_monto),null,0,sum(ordene_adj_monto)) from infi_tb_204_ordenes a where a.transa_id in('");
		sql.append(TransaccionNegocio.TOMA_DE_ORDEN).append("','").append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA).append("') and a.uniinv_id=");
		sql.append(unidadInversion).append(" and a.ordsta_id not in('").append(StatusOrden.CANCELADA).append("','").append(StatusOrden.PENDIENTE).append("')");
		sql.append(" and a.bloter_id=infi_tb_204_ordenes.bloter_id ");
		sql.append("and (ORDENE_PED_FE_ORDEN>=to_date('").append(simpleDateFormat.format(fechaDesde)).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("') and ORDENE_PED_FE_ORDEN<=to_date('").append(simpleDateFormat.format(fechaHasta)).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("'))");
		sql.append(") * infi_tb_106_unidad_inversion.undinv_tasa_cambio as monto_adjudicado_bs,");
		
		sql.append("(select decode(sum(ordene_adj_monto),null,0,sum(ordene_adj_monto)) from infi_tb_204_ordenes a where a.transa_id in('");
		sql.append(TransaccionNegocio.TOMA_DE_ORDEN).append("','").append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA).append("') and a.uniinv_id=");
		sql.append(unidadInversion).append(" and a.ordsta_id not in('").append(StatusOrden.CANCELADA).append("','").append(StatusOrden.PENDIENTE).append("')");
		sql.append(" and a.bloter_id=infi_tb_204_ordenes.bloter_id ");
		sql.append("and (ORDENE_FECHA_LIQUIDACION>=to_date('").append(simpleDateFormat.format(fechaDesde)).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("') and ORDENE_FECHA_LIQUIDACION<=to_date('").append(simpleDateFormat.format(fechaHasta)).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("'))");
		sql.append(") as monto_liquidado");
		
		sql.append(" from ").append("infi_tb_204_ordenes ");
		sql.append("left join INFI_TB_102_BLOTER on infi_tb_204_ordenes.bloter_id=INFI_TB_102_BLOTER.bloter_id,infi_tb_106_unidad_inversion ");
		sql.append("where uniinv_id=").append(unidadInversion).append(" and infi_tb_204_ordenes.uniinv_id = infi_tb_106_unidad_inversion.undinv_id");
		sql.append(" and infi_tb_204_ordenes.bloter_id is not null ");
		sql.append("and (ORDENE_PED_FE_ORDEN>=to_date('").append(simpleDateFormat.format(fechaDesde)).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("') and ORDENE_PED_FE_ORDEN<=to_date('").append(simpleDateFormat.format(fechaHasta)).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("')) ");
		sql.append("and infi_tb_204_ordenes.ordsta_id not in('").append(StatusOrden.CANCELADA).append("','").append(StatusOrden.PENDIENTE).append("') ");
		
		dataSet = db.get(dataSource, sql.toString());
	}
	
	/**
	 * Lista el resumen por unidad de inversión indicando por bloter
	 * número de ordenes, monto solicitado,monto adjudicado por tipo de Producto
	 * @param unidadInversion
	 * @param fechaDesde
	 * @param fechaHasta
	 * @param tipoProducto
	 * @throws Exception
	 */
	public void listarResumenUnidadInversionPorProducto(long unidadInversion,Date fechaDesde,Date fechaHasta,String tipoProducto) throws Exception{
		StringBuffer sql = new StringBuffer();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
		
		sql.append("select b.BLOTER_DESCRIPCION,");
		sql.append("suma_tasas / orden_por_bloter AS tasa_de_cambio,");
		sql.append("0 AS porcentaje,");
		sql.append("resumen.*,");      
		sql.append("	(SELECT DECODE (SUM (ordene_adj_monto),NULL, 0,SUM (ordene_adj_monto))");     
		sql.append("	FROM infi_tb_204_ordenes o");  
		sql.append("	WHERE o.uniinv_id=").append(unidadInversion);
		sql.append("	AND o.transa_id IN('").append(TransaccionNegocio.TOMA_DE_ORDEN).append("','").append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA).append("')"); 
		sql.append("	AND o.ordene_fecha_liquidacion >=to_date('").append(simpleDateFormat.format(fechaDesde)).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("')"); 
		sql.append("	AND o.ordene_fecha_liquidacion <=to_date('").append(simpleDateFormat.format(fechaHasta)).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("')"); 
		sql.append("	AND O.BLOTER_ID=resumen.BLOTER_ID");
		sql.append("	AND o.ordsta_id NOT IN ('").append(StatusOrden.CANCELADA).append("','").append(StatusOrden.PENDIENTE).append("')"); 
		sql.append("	AND o.TIPO_PRODUCTO_ID='").append(tipoProducto).append("') AS monto_liquidado from "); 
		sql.append("		(SELECT  o.BLOTER_ID,"); 
		sql.append("		SUM (DECODE (ui.tipo_solicitud,1, ordene_ped_monto / (NVL (o.ordene_tasa_cambio, 0)),ordene_ped_monto * (NVL (o.ordene_tasa_cambio, 0))) )AS monto_solicitado_bs,");
		sql.append("		count (ordene_id) orden_por_bloter, ");
		sql.append("		SUM (NVL(ordene_ped_monto, 0)) AS monto_solicitado,");
		sql.append("		DECODE (SUM (ordene_adj_monto),NULL, 0,SUM (ordene_adj_monto)) AS monto_adjudicado,");
		sql.append("		SUM (DECODE (ui.tipo_solicitud,1, ordene_adj_monto / (NVL (o.ordene_tasa_cambio, 0)),ordene_adj_monto * (NVL (o.ordene_tasa_cambio, 0))) )AS monto_adjudicado_bs,");
		sql.append("		SUM (NVL(ordene_ped_total, 0)) monto_ordenes,");
		sql.append("		SUM (NVL(ordene_ped_comisiones,0)) AS monto_comision,");
		sql.append("		SUM (NVL(ordene_ped_int_caidos,0)) AS monto_interesescaidos,");
		sql.append("		SUM (NVL(o.ORDENE_TASA_CAMBIO, 0)) AS suma_tasas,");
		sql.append("		SUM (DECODE (ui.tipo_solicitud,1,(NVL (ordene_ped_total, 0)-NVL (ordene_ped_comisiones, 0))-NVL (ordene_ped_int_caidos, 0),ordene_ped_total)) AS monto_capital ");
		sql.append("		FROM infi_tb_204_ordenes o,infi_tb_106_unidad_inversion ui");
		sql.append("		WHERE o.uniinv_id = ").append(unidadInversion);
		sql.append("		AND ui.UNDINV_ID=o.uniinv_id");
		sql.append("		AND o.transa_id IN('").append(TransaccionNegocio.TOMA_DE_ORDEN).append("','").append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA).append("')");
		sql.append("		AND o.ordene_ped_fe_orden >=to_date('").append(simpleDateFormat.format(fechaDesde)).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("')");
		sql.append("		AND o.ordene_ped_fe_orden <=to_date('").append(simpleDateFormat.format(fechaHasta)).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("')");
		sql.append("		AND o.ordsta_id NOT IN ('").append(StatusOrden.CANCELADA).append("','").append(StatusOrden.PENDIENTE).append("')");
		sql.append("		AND o.TIPO_PRODUCTO_ID='").append(tipoProducto).append("'");
		sql.append("		group by o.BLOTER_ID) resumen, INFI_TB_102_BLOTER b");
		sql.append("		WHERE b.BLOTER_ID=resumen.BLOTER_ID");
		
		System.out.println("listarResumenUnidadInversionPorProducto: "+sql.toString());
		dataSet = db.get(dataSource, sql.toString());
	}
	
	/**
	 * Muestra los nombres de la unidad de inversion y la descripcion del bloter
	 * */	
	public void listarResumenUninvBlotter(long unidad_inversion,String blotter) throws Exception{
	
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		sql.append("select unique infi_tb_102_bloter.BLOTER_DESCRIPCION,infi_tb_106_unidad_inversion.UNDINV_NOMBRE,infi_tb_106_unidad_inversion.moneda_id" +
				" from infi_tb_204_ordenes" +
				" left join infi_tb_106_unidad_inversion on infi_tb_204_ordenes.UNIINV_ID=infi_tb_106_unidad_inversion.UNDINV_ID" +
				" left join  infi_tb_102_bloter on infi_tb_204_ordenes.BLOTER_ID=infi_tb_102_bloter.BLOTER_ID where 1 =1");	
		if(unidad_inversion!=0)
			filtro.append(" AND infi_tb_204_ordenes.UNIINV_ID=").append(unidad_inversion);
		if(blotter!="")
			filtro.append(" AND infi_tb_204_ordenes.BLOTER_ID='").append(blotter).append("'");
		sql.append(filtro);
		
		dataSet = db.get(dataSource, sql.toString());
	}
	/**
	 * Lista sólo la cantidad de registros configurados así la consulta devuelva 30.000 todas las ordenes registradas seg&uacute;n los par&aacute;metros recibidos. 
	 * @param idUnidad id de la unidad de inversi&oacute;n (-1 o 0 indican filtrar por todos)	  
	 * @param idBlotter id del blotter.  (-1 o 0 indican filtrar por todos)
	 * @param status Array de los status que se desean consultar (si se recibe null se buscan por todos los status)  
	 * @param fechaDesde Fecha de inicio de la creaci&oacute;n de la orden
	 * @param fechaHasta Fecha de fin de la creaci&oacute;n de la orden
	 * @param indicaOrden n&uacute;mero que indica el campo de ordenaci&oacute;n. A continuaci&oacute;n la lista de posibles valores de ordenaci&oacute;n:
	 * @return el sql ejecutado
	 * 
	 * */	
	public String listarMovimientosPorUbs(long idUnidad, String idBlotter, String status, String fechaDesde, String fechaHasta) throws Exception{
		return listarMovimientosPorUbsPrivado(idUnidad, idBlotter, status, fechaDesde, fechaHasta, false, 0, 0, true);
	}
	
	/**
	 * Lista las ordenes por los parámetros recibidos. 
	 * @param idUnidad id de la unidad de inversi&oacute;n (-1 o 0 indican filtrar por todos)	  
	 * @param idBlotter id del blotter.  (-1 o 0 indican filtrar por todos)
	 * @param status Array de los status que se desean consultar (si se recibe null se buscan por todos los status)  
	 * @param fechaDesde Fecha de inicio de la creaci&oacute;n de la orden
	 * @param fechaHasta Fecha de fin de la creaci&oacute;n de la orden
	 * @param indicaOrden n&uacute;mero que indica el campo de ordenaci&oacute;n. A continuaci&oacute;n la lista de posibles valores de ordenaci&oacute;n:
	 * @param generarDataSet verdadero si se quiere obtener el dataSet de la consulta, falso en caso de que sólo se desee ejecutar la consulta para obtener el total de registros
	 * @return el sql ejecutado
	 * */	
	public String listarMovimientosPorUbs(long idUnidad, String idBlotter, String status, String fechaDesde, String fechaHasta, boolean generarDataSet, String tipoProductoId) throws Exception{
		return listarMovimientosPorUbsPrivado(idUnidad, idBlotter, status, fechaDesde, fechaHasta, false, 0, 0, generarDataSet, tipoProductoId);
	}
	
	/**
	 * Lista las ordenes por los parámetros recibidos. 
	 * @param idUnidad id de la unidad de inversi&oacute;n (-1 o 0 indican filtrar por todos)	  
	 * @param idBlotter id del blotter.  (-1 o 0 indican filtrar por todos)
	 * @param status Array de los status que se desean consultar (si se recibe null se buscan por todos los status)  
	 * @param fechaDesde Fecha de inicio de la creaci&oacute;n de la orden
	 * @param fechaHasta Fecha de fin de la creaci&oacute;n de la orden
	 * @param paginado indica si la consulta debe ser paginada
	 * @param paginaAMostrar página que se desea mostrar, este valor debe enviarse con el valor de paginado en true
	 * @param generarDataSet verdadero si se quiere obtener el dataSet de la consulta, falso en caso de que sólo se desee ejecutar la consulta para obtener el total de registros
	 * @param registroPorPagina registros por página a mostrar
	 * */	
	public String listarMovimientosPorUbs(long idUnidad, String idBlotter, String status, String fechaDesde, String fechaHasta, boolean paginado, String tipoProductoId, int paginaAMostrar, int registroPorPagina) throws Exception{
		return listarMovimientosPorUbsPrivado(idUnidad, idBlotter, status, fechaDesde, fechaHasta, paginado, paginaAMostrar, registroPorPagina, true, tipoProductoId);
	}
	//MODIFICADO PARA INFI_SICAD_ENT_2, método sobrecargado, CT15821
	private String listarMovimientosPorUbsPrivado1(long idUnidad, String idBlotter, String status, String fechaDesde, String fechaHasta, boolean paginado, int paginaAMostrar, int registroPorPagina, boolean generarDataSet, String... tipoProductoId) throws Exception{
		SimpleDateFormat formato_fecha = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);//nm36635
		String fecha_reme = fechaHasta; //nm33635
		Date fecha_remediacion = null;
		fecha_remediacion = formato_fecha.parse(fecha_reme);
		SimpleDateFormat formatter = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA_SIN_SEPARADOR_2);
		String fecha_funcion = formatter.format(fecha_remediacion);
		String fecha_re= ConstantesGenerales.FECHA_RECONVERSION;
		int fecha_reconversion= Integer.parseInt(fecha_re);
		int fecha_sistema= Integer.parseInt(fecha_funcion);
		System.out.println(fecha_reme);
		System.out.println(fecha_remediacion);
		System.out.println(fecha_funcion);
		System.out.println(fecha_sistema+" "+fecha_reconversion);
		//if(fecha_sistema >= fecha_reconversion){
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		sql.append("select infi_tb_106_unidad_inversion.undinv_tasa_cambio, (infi_tb_201_ctes.tipper_id || '-' || infi_tb_201_ctes.client_cedrif) client_cedrif, transa_descripcion,infi_tb_106_unidad_inversion.moneda_id, (select INFI_TB_018_VEHICULOS.VEHICU_NOMBRE from INFI_TB_018_VEHICULOS where INFI_TB_018_VEHICULOS.VEHICU_ID=infi_tb_204_ordenes.ORDENE_VEH_TOM)as tomador,(select INFI_TB_018_VEHICULOS.VEHICU_NOMBRE from INFI_TB_018_VEHICULOS where INFI_TB_018_VEHICULOS.VEHICU_ID=infi_tb_204_ordenes.ORDENE_VEH_REC)as recompra_vec,(select INFI_TB_018_VEHICULOS.VEHICU_NOMBRE from INFI_TB_018_VEHICULOS where INFI_TB_018_VEHICULOS.VEHICU_ID=infi_tb_204_ordenes.ORDENE_VEH_COL)as colocador,INFI_TB_200_TIPO_PERSONAS.TIPPER_NOMBRE,case when infi_tb_204_ordenes.ENVIADO=0 then 'No' when infi_tb_204_ordenes.ENVIADO=1 then 'Si' end enviado_,case when infi_tb_204_ordenes.ORDENE_PED_IN_BDV=0 then 'No' when infi_tb_204_ordenes.ORDENE_PED_IN_BDV=1 then 'Si' end cartera_propia,case when infi_tb_204_ordenes.ORDENE_FINANCIADO=1 then 'Si' when infi_tb_204_ordenes.ORDENE_FINANCIADO=0 then 'No' end financiado,INFI_TB_016_EMPRESAS.EMPRES_NOMBRE,infi_tb_204_ordenes.ordene_id," +
				" infi_tb_204_ordenes.uniinv_id,infi_tb_204_ordenes.ordsta_id,infi_tb_204_ordenes.transa_id,infi_tb_204_ordenes.ordene_cte_seg_bco," +
				" infi_tb_204_ordenes.ordene_ped_comisiones ,infi_tb_204_ordenes.ordene_comision_operacion ,infi_tb_204_ordenes.ordene_comision_oficina ,infi_tb_204_ordenes.ordene_ganancia_red ,infi_tb_204_ordenes.ordene_comision_emisor ,infi_tb_204_ordenes.ordene_tasa_pool ,infi_tb_204_ordenes.ordene_usr_cen_contable ,infi_tb_204_ordenes.ordene_ped_rcp_precio ,infi_tb_204_ordenes.ordene_ped_rendimiento, infi_tb_204_ordenes.contraparte, infi_tb_204_ordenes.ordene_cte_seg_infi,infi_tb_204_ordenes.ordene_ped_fe_orden,infi_tb_204_ordenes.ordene_ped_fe_valor," +
				" infi_tb_204_ordenes.ordene_fecha_adjudicacion,infi_tb_204_ordenes.ordene_fecha_liquidacion,infi_tb_204_ordenes.ordene_fecha_custodia," +
				" case when '"+fecha_sistema+"' >= FVJ8ODIV0('BFE_FECHA_VIGENCIA') then DECODE (infi_tb_106_unidad_inversion.moneda_id,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),fvj8ocon0(infi_tb_204_ordenes.ordene_ped_monto,FVJ8ODIV0('BFE_FECHA_VIGENCIA')),infi_tb_204_ordenes.ordene_ped_monto) when '"+fecha_sistema+"' < FVJ8ODIV0('BFE_FECHA_VIGENCIA') then infi_tb_204_ordenes.ordene_ped_monto end ordene_ped_monto,case when '"+fecha_sistema+"' >= FVJ8ODIV0('BFE_FECHA_VIGENCIA') then DECODE (infi_tb_106_unidad_inversion.moneda_id,(SELECT fvj8odiv0('BFE_SWIFT') FROM DUAL),fvj8ocon0(infi_tb_204_ordenes.ordene_ped_total_pend,FVJ8ODIV0('BFE_FECHA_VIGENCIA')),infi_tb_204_ordenes.ordene_ped_total_pend) when '"+fecha_sistema+"' < FVJ8ODIV0('BFE_FECHA_VIGENCIA') then infi_tb_204_ordenes.ordene_ped_total_pend end ordene_ped_total_pend,infi_tb_204_ordenes.ordene_ped_total," +//fvj8ocon0(infi_tb_204_ordenes.ordene_ped_total,'"+fecha_re+"') ordene_ped_total
				" infi_tb_204_ordenes.ordene_ped_int_caidos,infi_tb_204_ordenes.ctecta_numero,infi_tb_204_ordenes.ordene_ped_precio," +
				" infi_tb_204_ordenes.ordene_adj_monto,infi_tb_204_ordenes.ordene_usr_nombre,infi_tb_204_ordenes.ordene_usr_sucursal, infi_tb_204_ordenes.tipo_producto_id, " +
				" infi_tb_204_ordenes.ordene_usr_terminal,infi_tb_203_ordenes_status.ordsta_nombre,infi_tb_201_ctes.CLIENT_NOMBRE,infi_tb_106_unidad_inversion.UNDINV_NOMBRE as unidad,infi_tb_102_bloter.BLOTER_DESCRIPCION,infi_tb_117_undinv_status.UNDINV_STATUS_DESCRIPCION, "+
				" infi_tb_204_ordenes.ordene_tasa_cambio");		
		
		sql.append(" from infi_tb_204_ordenes inner join infi_tb_201_ctes on infi_tb_204_ordenes.CLIENT_ID= infi_tb_201_ctes.CLIENT_ID inner join infi_tb_203_ordenes_status on infi_tb_204_ordenes.ordsta_id=infi_tb_203_ordenes_status.ordsta_id inner join INFI_TB_200_TIPO_PERSONAS on infi_tb_201_ctes.TIPPER_ID=INFI_TB_200_TIPO_PERSONAS.TIPPER_ID inner join INFI_TB_016_EMPRESAS on infi_tb_204_ordenes.EMPRES_ID=INFI_TB_016_EMPRESAS.EMPRES_ID inner join infi_tb_106_unidad_inversion on infi_tb_204_ordenes.UNIINV_ID=infi_tb_106_unidad_inversion.UNDINV_ID inner join infi_tb_102_bloter on infi_tb_204_ordenes.BLOTER_ID=infi_tb_102_bloter.BLOTER_ID inner join infi_tb_117_undinv_status on infi_tb_106_unidad_inversion.UNDINV_STATUS=infi_tb_117_undinv_status.UNDINV_STATUS inner join INFI_TB_012_TRANSACCIONES trans_neg on infi_tb_204_ordenes.transa_id = trans_neg.transa_id where 1=1 ");
		if(idUnidad!=0){
			filtro.append(" and infi_tb_204_ordenes.UNIINV_ID=").append(idUnidad);
		}
		if(idBlotter!=null && !idBlotter.equalsIgnoreCase("")){
			filtro.append(" and infi_tb_204_ordenes.BLOTER_ID='").append(idBlotter).append("'");
		}
		if(status!=null){
			filtro.append(" and infi_tb_204_ordenes.ordsta_id='").append(status).append("'");
		}
		if(fechaHasta!=null && fechaDesde!=null){
			filtro.append(" and TO_DATE(infi_tb_204_ordenes.ordene_ped_fe_orden,'" + ConstantesGenerales.FORMATO_FECHA_RRRR + "') between to_date('").append(fechaDesde).append("','" + ConstantesGenerales.FORMATO_FECHA_RRRR + "') and to_date('").append(fechaHasta).append("','" + ConstantesGenerales.FORMATO_FECHA_RRRR + "')");
		}
		
		if(tipoProductoId!=null && tipoProductoId.length > 0 && tipoProductoId[0]!=null){
			filtro.append(" and infi_tb_204_ordenes.tipo_producto_id = '").append(tipoProductoId[0]).append("'");
		}
		
		filtro.append(" and infi_tb_204_ordenes.transa_id in('").append(TransaccionNegocio.TOMA_DE_ORDEN).append("'");
		filtro.append(" , '" ).append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA).append("') ");
		sql.append(filtro);
		sql.append(" ORDER BY infi_tb_204_ordenes.ordene_id desc");
		//Si no debe crear el dataSet consulta el total de registros
		if (!generarDataSet){
			getTotalDeRegistros(sql.toString());
		}else{
			if (paginado && paginaAMostrar>0 && registroPorPagina> 0){			
				dataSet = obtenerDataSetPaginado(sql.toString(),paginaAMostrar,registroPorPagina);
			}else{
				dataSet = db.get(dataSource, sql.toString());
			}	
		}
		System.out.println("listarMovimientosPorUbsPrivado "+sql);
		return sql.toString();
	/*	}else{
			StringBuffer sql = new StringBuffer();
			StringBuffer filtro = new StringBuffer("");
			sql.append("select infi_tb_106_unidad_inversion.undinv_tasa_cambio, (infi_tb_201_ctes.tipper_id || '-' || infi_tb_201_ctes.client_cedrif) client_cedrif, transa_descripcion, (select INFI_TB_018_VEHICULOS.VEHICU_NOMBRE from INFI_TB_018_VEHICULOS where INFI_TB_018_VEHICULOS.VEHICU_ID=infi_tb_204_ordenes.ORDENE_VEH_TOM)as tomador,(select INFI_TB_018_VEHICULOS.VEHICU_NOMBRE from INFI_TB_018_VEHICULOS where INFI_TB_018_VEHICULOS.VEHICU_ID=infi_tb_204_ordenes.ORDENE_VEH_REC)as recompra_vec,(select INFI_TB_018_VEHICULOS.VEHICU_NOMBRE from INFI_TB_018_VEHICULOS where INFI_TB_018_VEHICULOS.VEHICU_ID=infi_tb_204_ordenes.ORDENE_VEH_COL)as colocador,INFI_TB_200_TIPO_PERSONAS.TIPPER_NOMBRE,case when infi_tb_204_ordenes.ENVIADO=0 then 'No' when infi_tb_204_ordenes.ENVIADO=1 then 'Si' end enviado_,case when infi_tb_204_ordenes.ORDENE_PED_IN_BDV=0 then 'No' when infi_tb_204_ordenes.ORDENE_PED_IN_BDV=1 then 'Si' end cartera_propia,case when infi_tb_204_ordenes.ORDENE_FINANCIADO=1 then 'Si' when infi_tb_204_ordenes.ORDENE_FINANCIADO=0 then 'No' end financiado,INFI_TB_016_EMPRESAS.EMPRES_NOMBRE,infi_tb_204_ordenes.ordene_id," +
					" infi_tb_204_ordenes.uniinv_id,infi_tb_204_ordenes.ordsta_id,infi_tb_204_ordenes.transa_id,infi_tb_204_ordenes.ordene_cte_seg_bco," +
					" infi_tb_204_ordenes.ordene_ped_comisiones ,infi_tb_204_ordenes.ordene_comision_operacion ,infi_tb_204_ordenes.ordene_comision_oficina ,infi_tb_204_ordenes.ordene_ganancia_red ,infi_tb_204_ordenes.ordene_comision_emisor ,infi_tb_204_ordenes.ordene_tasa_pool ,infi_tb_204_ordenes.ordene_usr_cen_contable ,infi_tb_204_ordenes.ordene_ped_rcp_precio ,infi_tb_204_ordenes.ordene_ped_rendimiento, infi_tb_204_ordenes.contraparte, infi_tb_204_ordenes.ordene_cte_seg_infi,infi_tb_204_ordenes.ordene_ped_fe_orden,infi_tb_204_ordenes.ordene_ped_fe_valor," +
					" infi_tb_204_ordenes.ordene_fecha_adjudicacion,infi_tb_204_ordenes.ordene_fecha_liquidacion,infi_tb_204_ordenes.ordene_fecha_custodia," +
					" infi_tb_204_ordenes.ordene_ped_monto,infi_tb_204_ordenes.ordene_ped_total_pend,infi_tb_204_ordenes.ordene_ped_total," +
					" infi_tb_204_ordenes.ordene_ped_int_caidos,infi_tb_204_ordenes.ctecta_numero,infi_tb_204_ordenes.ordene_ped_precio," +
					" infi_tb_204_ordenes.ordene_adj_monto,infi_tb_204_ordenes.ordene_usr_nombre,infi_tb_204_ordenes.ordene_usr_sucursal, infi_tb_204_ordenes.tipo_producto_id, " +
					" infi_tb_204_ordenes.ordene_usr_terminal,infi_tb_203_ordenes_status.ordsta_nombre,infi_tb_201_ctes.CLIENT_NOMBRE,infi_tb_106_unidad_inversion.UNDINV_NOMBRE as unidad,infi_tb_102_bloter.BLOTER_DESCRIPCION,infi_tb_117_undinv_status.UNDINV_STATUS_DESCRIPCION, "+
					" infi_tb_204_ordenes.ordene_tasa_cambio ");		
			
			sql.append(" from infi_tb_204_ordenes inner join infi_tb_201_ctes on infi_tb_204_ordenes.CLIENT_ID= infi_tb_201_ctes.CLIENT_ID inner join infi_tb_203_ordenes_status on infi_tb_204_ordenes.ordsta_id=infi_tb_203_ordenes_status.ordsta_id inner join INFI_TB_200_TIPO_PERSONAS on infi_tb_201_ctes.TIPPER_ID=INFI_TB_200_TIPO_PERSONAS.TIPPER_ID inner join INFI_TB_016_EMPRESAS on infi_tb_204_ordenes.EMPRES_ID=INFI_TB_016_EMPRESAS.EMPRES_ID inner join infi_tb_106_unidad_inversion on infi_tb_204_ordenes.UNIINV_ID=infi_tb_106_unidad_inversion.UNDINV_ID inner join infi_tb_102_bloter on infi_tb_204_ordenes.BLOTER_ID=infi_tb_102_bloter.BLOTER_ID inner join infi_tb_117_undinv_status on infi_tb_106_unidad_inversion.UNDINV_STATUS=infi_tb_117_undinv_status.UNDINV_STATUS inner join INFI_TB_012_TRANSACCIONES trans_neg on infi_tb_204_ordenes.transa_id = trans_neg.transa_id where 1=1 ");
			if(idUnidad!=0){
				filtro.append(" and infi_tb_204_ordenes.UNIINV_ID=").append(idUnidad);
			}
			if(idBlotter!=null && !idBlotter.equalsIgnoreCase("")){
				filtro.append(" and infi_tb_204_ordenes.BLOTER_ID='").append(idBlotter).append("'");
			}
			if(status!=null){
				filtro.append(" and infi_tb_204_ordenes.ordsta_id='").append(status).append("'");
			}
			if(fechaHasta!=null && fechaDesde!=null){
				filtro.append(" and TO_DATE(infi_tb_204_ordenes.ordene_ped_fe_orden,'" + ConstantesGenerales.FORMATO_FECHA_RRRR + "') between to_date('").append(fechaDesde).append("','" + ConstantesGenerales.FORMATO_FECHA_RRRR + "') and to_date('").append(fechaHasta).append("','" + ConstantesGenerales.FORMATO_FECHA_RRRR + "')");
			}
			
			if(tipoProductoId!=null && tipoProductoId.length > 0 && tipoProductoId[0]!=null){
				filtro.append(" and infi_tb_204_ordenes.tipo_producto_id = '").append(tipoProductoId[0]).append("'");
			}
			
			filtro.append(" and infi_tb_204_ordenes.transa_id in('").append(TransaccionNegocio.TOMA_DE_ORDEN).append("'");
			filtro.append(" , '" ).append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA).append("') ");
			sql.append(filtro);
			sql.append(" ORDER BY infi_tb_204_ordenes.ordene_id desc");
			//Si no debe crear el dataSet consulta el total de registros
			if (!generarDataSet){
				getTotalDeRegistros(sql.toString());
			}else{
				if (paginado && paginaAMostrar>0 && registroPorPagina> 0){			
					dataSet = obtenerDataSetPaginado(sql.toString(),paginaAMostrar,registroPorPagina);
				}else{
					dataSet = db.get(dataSource, sql.toString());
				}	
			}
			System.out.println("listarMovimientosPorUbsPrivado "+sql);
			return sql.toString();
		}*/
	}
	
private String listarMovimientosPorUbsPrivado(long idUnidad, String idBlotter, String status, String fechaDesde, String fechaHasta, boolean paginado, int paginaAMostrar, int registroPorPagina, boolean generarDataSet, String... tipoProductoId) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		sql.append("select infi_tb_106_unidad_inversion.undinv_tasa_cambio, (infi_tb_201_ctes.tipper_id || '-' || infi_tb_201_ctes.client_cedrif) client_cedrif, transa_descripcion, (select INFI_TB_018_VEHICULOS.VEHICU_NOMBRE from INFI_TB_018_VEHICULOS where INFI_TB_018_VEHICULOS.VEHICU_ID=infi_tb_204_ordenes.ORDENE_VEH_TOM)as tomador,(select INFI_TB_018_VEHICULOS.VEHICU_NOMBRE from INFI_TB_018_VEHICULOS where INFI_TB_018_VEHICULOS.VEHICU_ID=infi_tb_204_ordenes.ORDENE_VEH_REC)as recompra_vec,(select INFI_TB_018_VEHICULOS.VEHICU_NOMBRE from INFI_TB_018_VEHICULOS where INFI_TB_018_VEHICULOS.VEHICU_ID=infi_tb_204_ordenes.ORDENE_VEH_COL)as colocador,INFI_TB_200_TIPO_PERSONAS.TIPPER_NOMBRE,case when infi_tb_204_ordenes.ENVIADO=0 then 'No' when infi_tb_204_ordenes.ENVIADO=1 then 'Si' end enviado_,case when infi_tb_204_ordenes.ORDENE_PED_IN_BDV=0 then 'No' when infi_tb_204_ordenes.ORDENE_PED_IN_BDV=1 then 'Si' end cartera_propia,case when infi_tb_204_ordenes.ORDENE_FINANCIADO=1 then 'Si' when infi_tb_204_ordenes.ORDENE_FINANCIADO=0 then 'No' end financiado,INFI_TB_016_EMPRESAS.EMPRES_NOMBRE,infi_tb_204_ordenes.ordene_id," +
				" infi_tb_204_ordenes.uniinv_id,infi_tb_204_ordenes.ordsta_id,infi_tb_204_ordenes.transa_id,infi_tb_204_ordenes.ordene_cte_seg_bco," +
				" infi_tb_204_ordenes.ordene_ped_comisiones ,infi_tb_204_ordenes.ordene_comision_operacion ,infi_tb_204_ordenes.ordene_comision_oficina ,infi_tb_204_ordenes.ordene_ganancia_red ,infi_tb_204_ordenes.ordene_comision_emisor ,infi_tb_204_ordenes.ordene_tasa_pool ,infi_tb_204_ordenes.ordene_usr_cen_contable ,infi_tb_204_ordenes.ordene_ped_rcp_precio ,infi_tb_204_ordenes.ordene_ped_rendimiento, infi_tb_204_ordenes.contraparte, infi_tb_204_ordenes.ordene_cte_seg_infi,infi_tb_204_ordenes.ordene_ped_fe_orden,infi_tb_204_ordenes.ordene_ped_fe_valor," +
				" infi_tb_204_ordenes.ordene_fecha_adjudicacion,infi_tb_204_ordenes.ordene_fecha_liquidacion,infi_tb_204_ordenes.ordene_fecha_custodia," +
				" infi_tb_204_ordenes.ordene_ped_monto,infi_tb_204_ordenes.ordene_ped_total_pend,infi_tb_204_ordenes.ordene_ped_total," +
				" infi_tb_204_ordenes.ordene_ped_int_caidos,infi_tb_204_ordenes.ctecta_numero,infi_tb_204_ordenes.ordene_ped_precio," +
				" infi_tb_204_ordenes.ordene_adj_monto,infi_tb_204_ordenes.ordene_usr_nombre,infi_tb_204_ordenes.ordene_usr_sucursal, infi_tb_204_ordenes.tipo_producto_id, " +
				" infi_tb_204_ordenes.ordene_usr_terminal,infi_tb_203_ordenes_status.ordsta_nombre,infi_tb_201_ctes.CLIENT_NOMBRE,infi_tb_106_unidad_inversion.UNDINV_NOMBRE as unidad,infi_tb_102_bloter.BLOTER_DESCRIPCION,infi_tb_117_undinv_status.UNDINV_STATUS_DESCRIPCION, "+
				" infi_tb_204_ordenes.ordene_tasa_cambio ");		
		
		sql.append(" from infi_tb_204_ordenes inner join infi_tb_201_ctes on infi_tb_204_ordenes.CLIENT_ID= infi_tb_201_ctes.CLIENT_ID inner join infi_tb_203_ordenes_status on infi_tb_204_ordenes.ordsta_id=infi_tb_203_ordenes_status.ordsta_id inner join INFI_TB_200_TIPO_PERSONAS on infi_tb_201_ctes.TIPPER_ID=INFI_TB_200_TIPO_PERSONAS.TIPPER_ID inner join INFI_TB_016_EMPRESAS on infi_tb_204_ordenes.EMPRES_ID=INFI_TB_016_EMPRESAS.EMPRES_ID inner join infi_tb_106_unidad_inversion on infi_tb_204_ordenes.UNIINV_ID=infi_tb_106_unidad_inversion.UNDINV_ID inner join infi_tb_102_bloter on infi_tb_204_ordenes.BLOTER_ID=infi_tb_102_bloter.BLOTER_ID inner join infi_tb_117_undinv_status on infi_tb_106_unidad_inversion.UNDINV_STATUS=infi_tb_117_undinv_status.UNDINV_STATUS inner join INFI_TB_012_TRANSACCIONES trans_neg on infi_tb_204_ordenes.transa_id = trans_neg.transa_id where 1=1 ");
		if(idUnidad!=0){
			filtro.append(" and infi_tb_204_ordenes.UNIINV_ID=").append(idUnidad);
		}
		if(idBlotter!=null && !idBlotter.equalsIgnoreCase("")){
			filtro.append(" and infi_tb_204_ordenes.BLOTER_ID='").append(idBlotter).append("'");
		}
		if(status!=null){
			filtro.append(" and infi_tb_204_ordenes.ordsta_id='").append(status).append("'");
		}
		if(fechaHasta!=null && fechaDesde!=null){
			filtro.append(" and TO_DATE(infi_tb_204_ordenes.ordene_ped_fe_orden,'" + ConstantesGenerales.FORMATO_FECHA_RRRR + "') between to_date('").append(fechaDesde).append("','" + ConstantesGenerales.FORMATO_FECHA_RRRR + "') and to_date('").append(fechaHasta).append("','" + ConstantesGenerales.FORMATO_FECHA_RRRR + "')");
		}
		
		if(tipoProductoId!=null && tipoProductoId.length > 0 && tipoProductoId[0]!=null){
			filtro.append(" and infi_tb_204_ordenes.tipo_producto_id = '").append(tipoProductoId[0]).append("'");
		}
		
		filtro.append(" and infi_tb_204_ordenes.transa_id in('").append(TransaccionNegocio.TOMA_DE_ORDEN).append("'");
		filtro.append(" , '" ).append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA).append("') ");
		sql.append(filtro);
		sql.append(" ORDER BY infi_tb_204_ordenes.ordene_id desc");
		//Si no debe crear el dataSet consulta el total de registros
		if (!generarDataSet){
			getTotalDeRegistros(sql.toString());
		}else{
			if (paginado && paginaAMostrar>0 && registroPorPagina> 0){			
				dataSet = obtenerDataSetPaginado(sql.toString(),paginaAMostrar,registroPorPagina);
			}else{
				dataSet = db.get(dataSource, sql.toString());
			}	
		}
		return sql.toString();
	}
	
public void listarMovimientosPorUbsRecompra(long idUnidad, String idBlotter, String status, String fechaDesde, String fechaHasta) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		sql.append("select infi_tb_106_unidad_inversion.undinv_tasa_cambio, (infi_tb_201_ctes.tipper_id || '-' || infi_tb_201_ctes.client_cedrif) client_cedrif, transa_descripcion, (select INFI_TB_018_VEHICULOS.VEHICU_NOMBRE from INFI_TB_018_VEHICULOS where INFI_TB_018_VEHICULOS.VEHICU_ID=infi_tb_204_ordenes.ORDENE_VEH_TOM)as tomador,(select INFI_TB_018_VEHICULOS.VEHICU_NOMBRE from INFI_TB_018_VEHICULOS where INFI_TB_018_VEHICULOS.VEHICU_ID=infi_tb_204_ordenes.ORDENE_VEH_REC)as recompra_vec,(select INFI_TB_018_VEHICULOS.VEHICU_NOMBRE from INFI_TB_018_VEHICULOS where INFI_TB_018_VEHICULOS.VEHICU_ID=infi_tb_204_ordenes.ORDENE_VEH_COL)as colocador,INFI_TB_200_TIPO_PERSONAS.TIPPER_NOMBRE,case when infi_tb_204_ordenes.ENVIADO=0 then 'No' when infi_tb_204_ordenes.ENVIADO=1 then 'Si' end enviado_,case when infi_tb_204_ordenes.ORDENE_PED_IN_BDV=0 then 'No' when infi_tb_204_ordenes.ORDENE_PED_IN_BDV=1 then 'Si' end cartera_propia,case when infi_tb_204_ordenes.ORDENE_FINANCIADO=1 then 'Si' when infi_tb_204_ordenes.ORDENE_FINANCIADO=0 then 'No' end financiado,INFI_TB_016_EMPRESAS.EMPRES_NOMBRE,infi_tb_204_ordenes.*,infi_tb_203_ordenes_status.ordsta_nombre,infi_tb_201_ctes.CLIENT_NOMBRE,infi_tb_106_unidad_inversion.UNDINV_NOMBRE as unidad,infi_tb_102_bloter.BLOTER_DESCRIPCION,infi_tb_117_undinv_status.UNDINV_STATUS_DESCRIPCION from infi_tb_204_ordenes left join infi_tb_201_ctes on infi_tb_204_ordenes.CLIENT_ID= infi_tb_201_ctes.CLIENT_ID left join infi_tb_203_ordenes_status on infi_tb_204_ordenes.ordsta_id=infi_tb_203_ordenes_status.ordsta_id left join INFI_TB_200_TIPO_PERSONAS on infi_tb_201_ctes.TIPPER_ID=INFI_TB_200_TIPO_PERSONAS.TIPPER_ID left join INFI_TB_016_EMPRESAS on infi_tb_204_ordenes.EMPRES_ID=INFI_TB_016_EMPRESAS.EMPRES_ID left join infi_tb_106_unidad_inversion on infi_tb_204_ordenes.UNIINV_ID=infi_tb_106_unidad_inversion.UNDINV_ID left join infi_tb_102_bloter on infi_tb_204_ordenes.BLOTER_ID=infi_tb_102_bloter.BLOTER_ID left join infi_tb_117_undinv_status on infi_tb_106_unidad_inversion.UNDINV_STATUS=infi_tb_117_undinv_status.UNDINV_STATUS left join INFI_TB_012_TRANSACCIONES trans_neg on infi_tb_204_ordenes.transa_id = trans_neg.transa_id where 1=1 ");
		if(idUnidad!=0){
			filtro.append(" and infi_tb_204_ordenes.UNIINV_ID=").append(idUnidad);
		}
		if(idBlotter!=null && !idBlotter.equalsIgnoreCase("")){
			filtro.append(" and infi_tb_204_ordenes.BLOTER_ID='").append(idBlotter).append("'");
		}
		if(status==null){
			filtro.append(" and infi_tb_204_ordenes.transa_id='PACTO_RECOMPRA'");
		}
		if(fechaHasta!=null && fechaDesde!=null){
			filtro.append(" and infi_tb_204_ordenes.ordene_ped_fe_orden between to_date('").append(fechaDesde).append("','" + ConstantesGenerales.FORMATO_FECHA + "') and to_date('").append(fechaHasta).append("','" + ConstantesGenerales.FORMATO_FECHA + "')");
		}
		filtro.append(" and infi_tb_204_ordenes.transa_id in('").append(TransaccionNegocio.TOMA_DE_ORDEN).append("'");
		filtro.append(" , '" ).append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA).append("') ");
		sql.append(filtro);
		sql.append(" ORDER BY infi_tb_204_ordenes.ordene_id desc");
		
		dataSet = db.get(dataSource, sql.toString());
	}
	
	
	/**Listas los status definidos de una Unidad de Inversion
	 * */
	public void listarStatus() throws Exception{
		
		String sql ="select INFI_TB_117_UNDINV_STATUS.undinv_status,INFI_TB_117_UNDINV_STATUS.undinv_status_descripcion from INFI_TB_117_UNDINV_STATUS";
		dataSet = db.get(dataSource, sql);
		
	}
	/**Listas los status de una orden
	 * */
	public void listarStatusOrden() throws Exception{
		String sql ="select * from INFI_TB_203_ORDENES_STATUS  order by ordsta_id desc";
		dataSet = db.get(dataSource, sql);	
	}
	
	/**
	 * Lista los depositarios existentes en base de datos
	 * */
	public void listarDepositario() throws Exception{	
		
		StringBuffer sql = new StringBuffer();
		sql.append(" select empres_id,empres_nombre from infi_tb_016_empresas where EMPRES_IN_DEPOSITARIO_CENTRAL=1 order by empres_nombre");
		dataSet = db.get(dataSource, sql.toString());
		
	}
	/**
	 * Lista los depositarios existentes en base de datos
	 * */
	public void listarDepositario(String empresId) throws Exception{	
		
		StringBuffer sql = new StringBuffer();
		sql.append(" select empres_id,empres_nombre from infi_tb_016_empresas where 1=1");
		sql.append(" and empres_id='").append(empresId).append("'");
		dataSet = db.get(dataSource, sql.toString());
		
	}
	
	/**
	 * Ordenes que le hayan sido adjudicados menos del monto pedido
	 * */
	public void listarOrdenesUniv(long uninvId,boolean paginado, int paginaAMostrar, int registroPorPagina) throws Exception{	
		
		StringBuffer sql = new StringBuffer();
		sql.append(" select infi_tb_201_ctes.CLIENT_ID,infi_tb_201_ctes.CLIENT_NOMBRE,infi_tb_204_ordenes.ORDENE_ID,infi_tb_204_ordenes.ORDENE_PED_MONTO,infi_tb_204_ordenes.ORDENE_ADJ_MONTO,infi_tb_102_bloter.BLOTER_ID,infi_tb_102_bloter.BLOTER_DESCRIPCION" +
				" from infi_tb_204_ordenes left join infi_tb_102_bloter on infi_tb_204_ordenes.BLOTER_ID=infi_tb_102_bloter.BLOTER_ID" +
				" left join infi_tb_201_ctes on  infi_tb_204_ordenes.CLIENT_ID=infi_tb_201_ctes.CLIENT_ID where ");
		sql.append(" infi_tb_204_ordenes.UNIINV_ID=").append(uninvId);
		sql.append(" and infi_tb_204_ordenes.ORDENE_ADJ_MONTO<infi_tb_204_ordenes.ORDENE_PED_MONTO");
		sql.append(" order by infi_tb_102_bloter.bloter_descripcion");
		if (paginado && paginaAMostrar>0 && registroPorPagina> 0){			
			dataSet = obtenerDataSetPaginado(sql.toString(),paginaAMostrar,registroPorPagina);
		}else{
			dataSet = db.get(dataSource, sql.toString());
		}	
	}
		
	/**Lista las ordenes asociadas a una Unidad de Inversi&oacute;n espec&iacute;fica
	 * @param idUnidadInversion id de la unidad de inversión
	 * @throws Exception en caso de error NM32454 INFI_TTS_491_WEB_SERVICE 27/02/2014
	 */
	public void listarOrdenesPorEnviarBCV(String tipoProductoId, Integer incluirCliente, Integer clienteID, Integer tasaMinima, Integer tasaMaxima, Integer montoMinimo, Integer montoMaximo,boolean totales, String tipoNegocio, long idUnidadInversion,boolean paginado, int paginaAMostrar, int registroPorPagina, boolean todos, boolean incluir, String ordenesSeleccionadas, String ordeneEstatusBCV, String fecha, Integer blotterID) throws Exception{
		String condicionUI = "";
		String condicionTasa = "";
		String condicionMonto = "";
		String condicionCliente = "";
		String condicionBlotter = "";
		
		if(blotterID != 0){
			condicionBlotter = "and o.bloter_id="+blotterID;
		}

		if(idUnidadInversion != 0){
			condicionUI = "and o.uniinv_id="+idUnidadInversion;
		}
		
		if((tasaMinima != null && tasaMinima != 0)){
			condicionTasa = " AND o.ORDENE_TASA_POOL >= "+tasaMinima;
		}
		
		if((tasaMaxima != null && tasaMaxima != 0)){
			condicionTasa += " AND o.ORDENE_TASA_POOL <= "+tasaMaxima;
		}
		
		if((montoMinimo != null && montoMinimo != 0)){
			condicionMonto = " AND o.ordene_ped_monto >= "+montoMinimo;
		}
		
		if((montoMaximo != null && montoMaximo != 0)){
			condicionMonto += " AND o.ordene_ped_monto <= "+montoMaximo;
		}
		
		if((clienteID != null && clienteID != 0)){
			if(incluirCliente.equals(1)){ //SE INCLUYE AL CLIENTE
				condicionCliente = " AND c.client_id IN ("+clienteID+")";
			}else { //SE EXCLUYE AL CLIENTE
				condicionCliente = " AND c.client_id NOT IN ("+clienteID+")";
			}
			
		}
		
		
		StringBuilder sql = new StringBuilder();
		
		if(!totales){//NO SE VAN A CONSULTAR LOS TOTALES
			sql.append(" SELECT o.ordene_observacion, o.ordene_id,o.ordene_ped_monto,c.client_nombre,u.undinv_nombre, c.CLIENT_CEDRIF, c.TIPPER_ID, o.ORDENE_TASA_POOL, " )
		    .append(" s.cta_numero, c.client_correo_electronico, c.client_telefono, u.nro_jornada, " )
		    .append(" CASE o.ordene_id_bcv WHEN '0' THEN '' ELSE o.ordene_id_bcv END ordene_id_bcv, ")
		    .append(" CASE o.ordene_estatus_bcv ") 
		    .append("    WHEN 0 THEN 'Sin Verificar'")
		    .append("    WHEN 1 THEN 'Verificada Aprobada BCV'")
		    .append("    WHEN 2 THEN 'Verificada Rechazada BCV'")
		    .append("    WHEN 3 THEN 'Verificada Aprobada Manual' ")
		    .append("    WHEN 4 THEN 'Verificada Rechazada Manual' ")
		    .append("    WHEN 5 THEN 'Anulada BCV'")
		    .append(" END estatus_string,  ")
			.append(" o.ordsta_id  ");
		}else { //SE VAN A CONSULTAR LOS TOTALES
			sql.append("SELECT SUM(o.ordene_ped_monto) monto_operacion, COUNT(1) cantidad_operaciones ");
		}
	    
		sql.append(" FROM INFI_TB_204_ORDENES o, INFI_TB_201_CTES c, INFI_TB_106_UNIDAD_INVERSION u," )
	    .append(" SOLICITUDES_SITME s  ")
	    .append(" WHERE o.client_id=c.client_id and 	o.uniinv_id=u.undinv_id ")
	    .append(condicionUI)
	    .append(" AND s.numero_orden_infi = o.ORDENE_ID ")
	    .append(" AND o.transa_id='").append(TransaccionNegocio.TOMA_DE_ORDEN).append("' ")
	    .append(" AND o.ORDSTA_ID <>'").append(StatusOrden.CANCELADA).append("' ")//NM26659 14/04/2016 ITS-3082 Correccion incidencias Envio WS Alto Valor trae ordenes canceladas
	    .append(" AND u.TIPO_NEGOCIO = ").append(tipoNegocio) 
	    .append(" AND TO_DATE(o.ordene_ped_fe_orden,'DD/MM/RRRR') = TO_DATE('").append(fecha).append("', 'DD/MM/RRRR')")
		.append(" AND o.ordene_estatus_bcv IN (").append(ordeneEstatusBCV).append(")") //ESTATUS DE LA ORDEN BCV, SIN VERIFICAR, VERIFICADA O RECHZADA
		.append(condicionMonto)
		.append(condicionTasa)
		.append(condicionCliente)
		.append(condicionBlotter);
		
		if(!todos){
			if(incluir){
		    	sql.append(" AND o.ordene_id in("+ordenesSeleccionadas+")");
		    } else {
		    	sql.append(" AND o.ordene_id not in("+ordenesSeleccionadas+")");
		    }
		}
		
		if(tipoNegocio.equals(ConstantesGenerales.TIPO_NEGOCIO_ALTO_VALOR)){ //TIPO_NEGOCIO_ALTO VALOR
			sql.append(" UNION ");
			
			if(!totales){//NO SE VAN A CONSULTAR LOS TOTALES
				sql.append(" SELECT o.ordene_observacion, o.ordene_id,o.ordene_ped_monto,c.client_nombre,u.undinv_nombre, c.CLIENT_CEDRIF, c.TIPPER_ID, o.ORDENE_TASA_POOL, " )
			    .append(" ctescuentas.ctecta_numero cta_numero, c.client_correo_electronico, c.client_telefono, u.nro_jornada, " )
			    .append(" CASE o.ordene_id_bcv WHEN '0' THEN '' ELSE o.ordene_id_bcv END ordene_id_bcv, ")
			    .append(" CASE o.ordene_estatus_bcv ") 
			    .append("    WHEN 0 THEN 'Sin Verificar'")
			    .append("    WHEN 1 THEN 'Verificada Aprobada BCV'")
			    .append("    WHEN 2 THEN 'Verificada Rechazada BCV'")
			    .append("    WHEN 3 THEN 'Verificada Aprobada Manual' ")
		        .append("    WHEN 4 THEN 'Verificada Rechazada Manual' ")
			    .append("    WHEN 5 THEN 'Anulada BCV'")
			    .append(" END estatus_string,  ")
				.append(" o.ordsta_id  ");
			}else{ //SE VAN A CONSULTAR LOS TOTALES
				sql.append("SELECT SUM(o.ordene_ped_monto) monto_operacion, COUNT(1) cantidad_operaciones ");
			} 

			sql.append("FROM infi_tb_204_ordenes o, ")
			.append(" infi_tb_201_ctes c, ")
			.append(" infi_tb_106_unidad_inversion u, ")
			.append(" infi_tb_217_ctes_cuentas_ord ctescord, ")
			.append(" infi_tb_202_ctes_cuentas     ctescuentas ")
			.append("WHERE o.client_id = c.client_id ")
			.append(condicionUI)
			.append(" AND o.uniinv_id = u.undinv_id ")
			.append(" AND o.ordene_id = ctescord.ordene_id ")
			.append(" AND ctescord.ctes_cuentas_id = ctescuentas.ctes_cuentas_id ")
	        .append(" AND o.transa_id='").append(TransaccionNegocio.TOMA_DE_ORDEN).append("' ")
	        .append(" AND o.ORDSTA_ID <>'").append(StatusOrden.CANCELADA).append("' ")//NM26659 14/04/2016 ITS-3082 Correccion incidencias Envio WS Alto Valor trae ordenes canceladas
			.append(" AND u.TIPO_NEGOCIO = ").append(tipoNegocio) 
			.append(" AND TO_DATE(o.ordene_ped_fe_orden,'DD/MM/RRRR') = TO_DATE('").append(fecha).append("', 'DD/MM/RRRR')")
			.append(" AND o.ordene_estatus_bcv IN (").append(ordeneEstatusBCV).append(")")//ESTATUS DE LA ORDEN BCV, SIN VERIFICAR, VERIFICADA O RECHZADA			
			.append(" AND o.tipo_producto_id IN ('").append(tipoProductoId).append("')") //TIPO PRODUCTO ID
			.append(condicionMonto)
			.append(condicionTasa)
			.append(condicionCliente)
			.append(condicionBlotter);
			
			if(!todos){
				if(incluir){
			    	sql.append(" AND o.ordene_id in("+ordenesSeleccionadas+")");
			    } else {
			    	sql.append(" AND o.ordene_id not in("+ordenesSeleccionadas+")");
			    }
			}
		}

		

	    //sql.append(" ORDER by o.ordene_ped_monto");   
		System.out.println("listarOrdenesPorEnviarBCV ---> " + sql.toString());
		if (paginado && paginaAMostrar>0 && registroPorPagina> 0){			
			dataSet = obtenerDataSetPaginado(sql.toString(),paginaAMostrar,registroPorPagina);
		}else{
			dataSet = db.get(dataSource, sql.toString());
		}
	}
	
	/**
	 * Lista todas las ordenes registradas seg&uacute;n los par&aacute;metros recibidos.
	 * Busca por t&iacute;tulo y despliega los siguientes campos: Fecha, N&uacute;mero de orden, Unidad de inversi&oacute;n, Status  
	 * @param idCliente id del cliente. -1 o 0 Cuando se desean todos  
	 * @param fechaDesde Fecha de inicio de la creaci&oacute;n de la orden
	 * @param fechaHasta Fecha de fin de la creaci&oacute;n de la orden
	 * @param indicaOrden n&uacute;mero que indica el campo de ordenaci&oacute;n. A continuaci&oacute;n la lista de posibles valores de ordenaci&oacute;n:
	 * 			<p>1. Fecha (por defecto o cuando el n&uacute;mero enviado no coincida)</p>
	 * 			<p>2. N&uacute;mero de orden</p>
	 * 			<p>3. Unidad de inversi&oacute;n</p>	 
	 * 			<p>4. Status</p>  
	 * */
	public void listarMovimientosPorCliente(long idCliente, Date fechaDesde, Date fechaHasta, int indicaOrden){		
	}
/**
 * Lista las ordenes por sucursal
 * @param String sucursal
 * @param String fechaDesde
 * @param String fechaHasta
 * @param String status
 * @param String transaccion
 * @throws Exception
 */
	public void listarMovimientosPorSucursal(String sucursal,String fechaDesde,String fechaHasta,String status, String transaccion,boolean paginado, int paginaAMostrar, int registroPorPagina)throws Exception{		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT transa_descripcion, ordene_usr_sucursal,infi_tb_201_ctes.CLIENT_NOMBRE,ordene_id,ORDENE_PED_FE_ORDEN,infi_tb_201_ctes.CLIENT_CEDRIF,infi_tb_204_ordenes.ORDENE_PED_TOTAL,infi_tb_203_ordenes_status.ORDSTA_NOMBRE,INFI_TB_106_UNIDAD_INVERSION.UNDINV_NOMBRE ");
		sql.append(" FROM infi_tb_204_ordenes left join infi_tb_201_ctes on infi_tb_204_ordenes.CLIENT_ID=infi_tb_201_ctes.CLIENT_ID ");
		sql.append(" left join infi_tb_203_ordenes_status ON infi_tb_204_ordenes.ORDSTA_ID = infi_tb_203_ordenes_status.ORDSTA_ID ");
		sql.append(" left join  INFI_TB_106_UNIDAD_INVERSION ON infi_tb_204_ordenes.UNIINV_ID = INFI_TB_106_UNIDAD_INVERSION.UNDINV_ID ");
		sql.append(" left join INFI_TB_012_TRANSACCIONES trans_neg on infi_tb_204_ordenes.transa_id = trans_neg.transa_id WHERE ordene_usr_sucursal IS NOT null ");
		if(sucursal!=null)
		sql.append(" and ordene_usr_sucursal='").append(sucursal).append("'");
		if(fechaDesde!=null && fechaHasta!=null){
			sql.append(" and ordene_ped_fe_orden between to_date('");
			sql.append(fechaDesde);
			sql.append("','");
			sql.append(com.bdv.infi.logic.interfaces.ConstantesGenerales.FORMATO_FECHA);
			sql.append("') and to_date('");
			sql.append(fechaHasta);
			sql.append("','");
			sql.append(com.bdv.infi.logic.interfaces.ConstantesGenerales.FORMATO_FECHA);
			sql.append("')");
		}
		if(status!=null)
			sql.append(" and ordsta_id='").append(status).append("'");
		
		//Si se selecciona una transacción se busca sino todas las de tesorería
		//Busca sólo por la transacción seleccionada
		if (!transaccion.equals("-1") && !transaccion.equals("")){
			sql.append(" and infi_tb_204_ordenes.TRANSA_ID = '").append(transaccion).append("'");
		}else{
			//Solo transacciones del proceso de Toma de ordenes
			sql.append(" and infi_tb_204_ordenes.transa_id IN ('");
			sql.append(TransaccionNegocio.CANCELACION_ORDEN).append("','");
			sql.append(TransaccionNegocio.COBRO_FINANCIAMIENTO).append("','");
			sql.append(TransaccionNegocio.ORDEN_VEHICULO).append("','");
			sql.append(TransaccionNegocio.PACTO_RECOMPRA).append("','");
			sql.append(TransaccionNegocio.TOMA_DE_ORDEN).append("','");
			sql.append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA).append("','");
			sql.append(TransaccionNegocio.VENTA_TITULOS).append("') ");			
		}
				
		sql.append(" GROUP BY transa_descripcion, ordene_usr_sucursal,infi_tb_201_ctes.CLIENT_NOMBRE,ordene_id,ORDENE_PED_FE_ORDEN,infi_tb_201_ctes.CLIENT_CEDRIF,infi_tb_204_ordenes.ORDENE_PED_TOTAL,infi_tb_203_ordenes_status.ORDSTA_NOMBRE,INFI_TB_106_UNIDAD_INVERSION.UNDINV_NOMBRE");
		sql.append(" order by infi_tb_204_ordenes.ordene_id desc");
		if (paginado && paginaAMostrar>0 && registroPorPagina> 0){			
			dataSet = obtenerDataSetPaginado(sql.toString(),paginaAMostrar,registroPorPagina);
		}else{
			dataSet = db.get(dataSource, sql.toString());
		}
		System.out.println("listarMovimientosPorSucursal "+sql);
	}
	
	/**
	 * Lista todas las ordenes registradas seg&uacute;n los par&aacute;metros recibidos.
	 * Busca por veh&iacute;culo de la orden  
	 * @param idUnidad unidad de inversi&oacute;n  
	 * @param idVehiculo id del vehiculo asociado a la orden (si el valor es null se buscan por todos los veh&iacute;culos)
	 * @param status Array de los status que se desean consultar  
	 * @param fechaDesde Fecha de inicio de la creaci&oacute;n de la orden
	 * @param fechaHasta Fecha de fin de la creaci&oacute;n de la orden
	 * @param indicaOrden n&uacute;mero que indica el campo de ordenaci&oacute;n. A continuaci&oacute;n la lista de posibles valores de ordenaci&oacute;n:
	 * 			<p>1. Vehiculo (por defecto o cuando el n&uacute;mero enviado no coincida)</p>
	 * 			<p>2. Cliente</p>	 * 
	 * 			<p>3. N&uacute;mero de orden</p>
	 * 			<p>4. Unidad de inversi&oacute;n</p>	 
	 * 			<p>5. Status</p>   
	 * */
	public void listarMovimientosPorVehiculo(long idUnidad, String idVehiculo, String[] status, Date fechaDesde, Date fechaHasta,  int indicaOrden){		
	}

	/**
	 * Lista de forma resumida y sumaria los movimientos por veh&iacute;culo
	 * @param idUnidad unidad de inversi&oacute;n  
	 * @param idVehiculo id del vehiculo asociado a la orden
	 * @param status Array de los status que se desean consultar  
	 * @param fechaDesde Fecha de inicio de la creaci&oacute;n de la orden
	 * @param fechaHasta Fecha de fin de la creaci&oacute;n de la orden
	 * @param indicaOrden n&uacute;mero que indica el campo de ordenaci&oacute;n. A continuaci&oacute;n la lista de posibles valores de ordenaci&oacute;n:
	 * 			<p>1. Vehiculo (por defecto o cuando el n&uacute;mero enviado no coincida)</p>
	 * 			<p>2. Cliente</p>	 * 
	 * 			<p>3. N&uacute;mero de orden</p>
	 * 			<p>4. Unidad de inversi&oacute;n</p>	 
	 * 			<p>5. Status</p>   
	 * */	
	public void listarResumenPorVehiculo(long idUnidad, String idVehiculo, String[] status, Date fechaDesde, Date fechaHasta,  int indicaOrden){		
	}
	
	/**Marca las ordenes como enviadas
	 * @param lista lista de n&uacute;meros de orden que se marcaran como enviadas
	 * @return El n&uacute;mero de filas afectadas*/	 
	public int marcarEnviadas(ArrayList lista){
		return 0;
	}	

	/**
	 * inserta una orden en la base de datos
	 * @param orden a ser insertada
	 * @throws Exception lanza una excepci&oacute;n si hay un error
	*/
	@SuppressWarnings("unchecked")
	public String[] insertar(Orden orden) throws Exception{		
		
		ArrayList consultas = new ArrayList();
		//Almacena la orden en la base de datos
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO INFI_TB_204_ORDENES (ORDENE_ID,UNIINV_ID,CLIENT_ID,ORDSTA_ID,");
		sql.append("SISTEMA_ID,EMPRES_ID,CONTRAPARTE,TRANSA_ID,ENVIADO,ORDENE_CTE_SEG_BCO,ORDENE_CTE_SEG_SEG,");
		sql.append("ORDENE_CTE_SEG_SUB,ORDENE_CTE_SEG_INFI,ORDENE_PED_FE_ORDEN,ORDENE_PED_FE_VALOR,");
		sql.append("ORDENE_PED_MONTO,ORDENE_PED_TOTAL_PEND,ORDENE_PED_TOTAL,ORDENE_PED_INT_CAIDOS,BLOTER_ID,");
		sql.append("ORDENE_PED_CTA_BSNRO,ORDENE_PED_CTA_BSTIP,CTECTA_NUMERO,ORDENE_PED_PRECIO,");
		sql.append("ORDENE_PED_RENDIMIENTO,ORDENE_PED_IN_BDV,MONEDA_ID,ORDENE_PED_RCP_PRECIO,ORDENE_ADJ_MONTO,");
		sql.append("ORDENE_USR_NOMBRE,ORDENE_USR_CEN_CONTABLE,ORDENE_USR_SUCURSAL,ORDENE_USR_TERMINAL,");
		sql.append("ORDENE_VEH_TOM,ORDENE_VEH_COL,ORDENE_VEH_REC,ORDENE_EJEC_ID,ORDENE_FE_ULT_ACT,ORDENE_FINANCIADO,EJECUCION_ID, ORDENE_ID_RELACION, ORDENE_TASA_CAMBIO,ORDENE_OBSERVACION,TIPO_PRODUCTO_ID,CTA_ABONO) ");
		sql.append("VALUES(");
		
		//Obtiene el id de la secuencia
	    long secuencia = Long.valueOf(dbGetSequence(this.dataSource, ConstantesGenerales.SECUENCIA_ORDENES)).longValue();
		sql.append(secuencia).append(",");
		
		//Agrega el c&oacute;digo a la orden
		orden.setIdOrden(secuencia);
		
		//Verifica si la unidad de inversi&oacute;n es 0 para enviar nulo
		if(orden.getIdUnidadInversion()==0){
			sql.append("NULL,");
		} else{
			sql.append(orden.getIdUnidadInversion()).append(",");
		}
		
		sql.append(orden.getIdCliente()).append(",");
		sql.append("'").append(orden.getStatus()).append("',");
		
		//Verifica si el id de sistema es 0 para colocar nulo
		if (orden.getIdSistema()==0){
			sql.append("NULL,");
		}else{
			sql.append(orden.getIdSistema()).append(",");
		}
		
		sql.append(orden.getIdEmpresa()==null?"NULL,":"'"+orden.getIdEmpresa()+"',");
		sql.append(orden.getContraparte()==null?"NULL,":"'"+orden.getContraparte()+"',");
		sql.append("'"+orden.getIdTransaccion()+"',");
		sql.append(orden.isEnviado()?ConstantesGenerales.VERDADERO:ConstantesGenerales.FALSO).append(",");
		sql.append(orden.getSegmentoBanco()==null?"NULL,":"'"+orden.getSegmentoBanco()+"',");
		sql.append(orden.getSegmentoSegmento()==null?"NULL,":"'"+orden.getSegmentoSegmento()+"',");
		sql.append(orden.getSegmentoSubSegmento()==null?"NULL,":"'"+orden.getSegmentoSubSegmento()+"',");
		sql.append(orden.getSegmentoInfi()==null?"NULL,":"'"+orden.getSegmentoInfi()+"',");
		sql.append(formatearFechaHoraBDActual()).append(",");
		sql.append(this.formatearFechaBD(orden.getFechaValor())).append(",");
		sql.append(orden.getMonto()).append(",");
		sql.append(orden.getMontoPendiente()).append(",");
		sql.append(orden.getMontoTotal()).append(",");
		sql.append(orden.getMontoPendiente()).append(",");
		
		//Verifica el blotter
		if (orden.getIdBloter()==null){
			sql.append("NULL,");
		}else{
			sql.append("'").append(orden.getIdBloter()).append("',");
		}
		
		//sql.append(orden.getCuentaCliente()==null?"NULL,":"'"+orden.getCuentaCliente()+"',"); //TODO Validar si el campo ORDENE_PED_CTA_BSNRO debe llenarse obligatoriamente
		sql.append("NULL,");
		sql.append(orden.getTipoCuenta()==null?"NULL,":"'"+orden.getTipoCuenta()+"',");
		sql.append(orden.getCuentaCliente()==null?"NULL,":"'"+orden.getCuentaCliente()+"',");//CtaNumero

		//Verifica precio de compra
		if (orden.getPrecioCompra()==0){
			sql.append("NULL,");
		}else{
			sql.append(orden.getPrecioCompra()).append(",");
		}
		
		//Verifica rendimiento
		if (orden.getRendimiento()==0){
			sql.append("NULL,");
		}else{
			sql.append(orden.getRendimiento()).append(",");
		}
		
		sql.append(orden.isCarteraPropia()?ConstantesGenerales.VERDADERO:ConstantesGenerales.FALSO).append(",");
		
		sql.append(orden.getIdMoneda()==null?"NULL,":"'"+orden.getIdMoneda()+"',");		
		
		//Verifica precio de recompra
		if (orden.getPrecioRecompra()==0){
			sql.append("NULL,");
		}else{
			sql.append(orden.getPrecioRecompra()).append(",");
		}
		
		//Verifica monto adjudicado
		if (orden.getMontoAdjudicado()==0){
			sql.append("NULL,");
		}else{
			sql.append(orden.getMontoAdjudicado()).append(",");
		}
		
		sql.append(orden.getNombreUsuario()==null?"NULL,":"'"+orden.getNombreUsuario()+"',");
		sql.append(orden.getCentroContable()==null?"NULL,":"'"+orden.getCentroContable()+"',");		
		sql.append(orden.getSucursal()==null?"NULL,":"'"+orden.getSucursal()+"',");
		sql.append(orden.getTerminal()==null?"NULL,":"'"+orden.getTerminal()+"',");
		
		sql.append(orden.getVehiculoTomador()==null?"NULL,":"'"+orden.getVehiculoTomador()+"',");
		sql.append(orden.getVehiculoColocador()==null?"NULL,":"'"+orden.getVehiculoColocador()+"',");
		sql.append(orden.getVehiculoRecompra()==null?"NULL,":"'"+orden.getVehiculoRecompra()+"',");
		
		sql.append("NULL,");
		sql.append("NULL,");
		
		sql.append(orden.isFinanciada()?ConstantesGenerales.VERDADERO:ConstantesGenerales.FALSO).append(",");
		sql.append(orden.getIdEjecucion()).append(",");
		sql.append(orden.getIdOrdenRelacionada()).append(",");
		sql.append(orden.getTasaCambio()).append(",");
		sql.append(orden.getObservaciones()==null?"NULL,":"'"+orden.getObservaciones()+"',");
		sql.append(orden.getTipoProducto()==null?"NULL,":"'"+orden.getTipoProducto()+"',");
		sql.append(orden.getTipoCuentaAbono());
		sql.append(")");
		
		//Guarda la consulta construida de la orden
		consultas.add(sql.toString());
		
		guardarTitulos(orden,consultas); //Inserta los t&iacute;tulos
		guardarOperaciones(orden, consultas); //Inserta las operaciones financieras
		insertarDocumentos(orden, consultas); //Inserta los documentos
		guardarDataExt(orden, consultas); //Inserta la data extendida
		guardarCamposDinamicos(orden, consultas);
		
		//Prepara el retorno
		String[] retorno = new String[consultas.size()];
		
		//Recorre la lista y crea un string de consultas
		for (int i=0; i < consultas.size(); i++){
			retorno[i] = consultas.get(i).toString();
		}
  System.out.println("insertar "+sql.toString());
		
     	return retorno;	
	}
	
	//NUEVO VICTOR GONCALVES se usa
	/**
	 * inserta una orden en la base de datos
	 * @param orden a ser insertada
	 * @throws Exception lanza una excepci&oacute;n si hay un error
	*/
	@SuppressWarnings("unchecked")
	public String[] insertarSolicitudSITME(Orden orden) throws Exception{		

		try{
		ArrayList consultas = new ArrayList();
		//Almacena la orden en la base de datos
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO INFI_TB_204_ORDENES (ORDENE_ID,UNIINV_ID,CLIENT_ID,ORDSTA_ID,");
		sql.append("SISTEMA_ID,EMPRES_ID,CONTRAPARTE,TRANSA_ID,ENVIADO,ORDENE_CTE_SEG_BCO,ORDENE_CTE_SEG_SEG,");
		sql.append("ORDENE_CTE_SEG_SUB,ORDENE_CTE_SEG_INFI,ORDENE_PED_FE_ORDEN,ORDENE_PED_FE_VALOR,");
		sql.append("ORDENE_PED_MONTO,ORDENE_PED_TOTAL_PEND,ORDENE_PED_TOTAL,ORDENE_PED_INT_CAIDOS,BLOTER_ID,");
		sql.append("ORDENE_PED_CTA_BSNRO,ORDENE_PED_CTA_BSTIP,CTECTA_NUMERO,ORDENE_PED_PRECIO,");
		sql.append("ORDENE_PED_RENDIMIENTO,ORDENE_PED_IN_BDV,MONEDA_ID,ORDENE_PED_RCP_PRECIO,ORDENE_ADJ_MONTO,");
		sql.append("ORDENE_USR_NOMBRE,ORDENE_USR_CEN_CONTABLE,ORDENE_USR_SUCURSAL,ORDENE_USR_TERMINAL,");
		sql.append("ORDENE_VEH_TOM,ORDENE_VEH_COL,ORDENE_VEH_REC,ORDENE_EJEC_ID,ORDENE_FE_ULT_ACT,ORDENE_PED_COMISIONES,ORDENE_FINANCIADO,EJECUCION_ID,ORDENE_TASA_POOL,ORDENE_GANANCIA_RED, ORDENE_COMISION_OFICINA, ORDENE_COMISION_OPERACION, ORDENE_OPERAC_PEND, ORDENE_ID_RELACION, ORDENE_TASA_CAMBIO,CONCEPTO_ID,ORDENE_OBSERVACION,TIPO_PRODUCTO_ID,CTA_ABONO) ");
		sql.append("VALUES(");
		

		
		//Obtiene el id de la secuencia
	    long secuencia = Long.valueOf(dbGetSequence(this.dataSource, ConstantesGenerales.SECUENCIA_ORDENES)).longValue();	
	    
	    sql.append(secuencia).append(","); //ORDENE_ID	
	    
		//Agrega el c&oacute;digo a la orden
		orden.setIdOrden(secuencia);	
		//Verifica si la unidad de inversi&oacute;n es 0 para enviar nulo
		if(orden.getIdUnidadInversion()==0){ 
			sql.append("NULL,");
		} else{
			sql.append(orden.getIdUnidadInversion()).append(","); //UNIINV_ID
		}		
		sql.append(orden.getIdCliente()).append(","); //CLIENT_ID			
		sql.append("'").append(orden.getStatus()).append("',"); //ORDSTA_ID		
		//Verifica si el id de sistema es 0 para colocar nulo
		if (orden.getIdSistema()==0){
			sql.append("NULL,");
		}else{
			sql.append(orden.getIdSistema()).append(","); //SISTEMA_ID
		}				
		sql.append(orden.getIdEmpresa()==null?"NULL,":"'"+orden.getIdEmpresa()+"',"); //EMPRES_ID		
		sql.append("NULL,");//CONTRAPARTE		
		sql.append("'"+orden.getIdTransaccion()+"',"); //TRANSA_ID		
		sql.append(orden.isEnviado()?ConstantesGenerales.VERDADERO:ConstantesGenerales.FALSO).append(","); //ENVIADO		
		sql.append(orden.getSegmentoBanco()==null?"NULL,":"'"+orden.getSegmentoBanco()+"',"); //ORDENE_CTE_SEG_BCO		
		sql.append(orden.getSegmentoSegmento()==null?"NULL,":"'"+orden.getSegmentoSegmento()+"',"); //ORDENE_CTE_SEG_SEG		
		sql.append(orden.getSegmentoSubSegmento()==null?"NULL,":"'"+orden.getSegmentoSubSegmento()+"',"); //ORDENE_CTE_SEG_SUB		
		sql.append(orden.getSegmentoInfi()==null?"NULL,":"'"+orden.getSegmentoInfi()+"',"); //ORDENE_CTE_SEG_INFI		
		//TTS-491 NM26659_24/02/2015 _ Cambio Inclusion hora:minuto:segundo en ORDENE_PED_FE_ORDEN		
		sql.append(this.formatearFechaBDHora(orden.getFechaOrden())).append(","); //ORDENE_PED_FE_ORDEN				
		sql.append("SYSDATE").append(","); //ORDENE_PED_FE_VALOR		
		sql.append(orden.getMonto()).append(","); //ORDENE_PED_MONTO		
		sql.append(orden.getMontoPendiente()).append(","); //ORDENE_PED_TOTAL_PEND		
		sql.append(orden.getMontoTotal()).append(","); //ORDENE_PED_TOTAL		
		sql.append(orden.getMontoPendiente()).append(","); //ORDENE_PED_INT_CAIDOS		
		//Verifica el blotter
		if (orden.getIdBloter()==null){
			sql.append("NULL,");
		}else{
			sql.append("'").append(orden.getIdBloter()).append("',"); //BLOTER_ID
		}		
		sql.append("NULL,"); //ORDENE_PED_CTA_BSNRO		
		sql.append(orden.getTipoCuenta()==null?"NULL,":"'"+orden.getTipoCuenta()+"',"); //ORDENE_PED_CTA_BSTIP		
		sql.append(orden.getCuentaCliente()==null?"NULL,":"'"+orden.getCuentaCliente()+"',"); //CTECTA_NUMERO		
		//Verifica precio de compra
		if (orden.getPrecioCompra()==0){
			sql.append("NULL,");
		}else{
			sql.append(orden.getPrecioCompra()).append(","); //ORDENE_PED_PRECIO
		}		
		//Verifica rendimiento
		if (orden.getRendimiento()==0){
			sql.append("NULL,");
		}else{
			sql.append(orden.getRendimiento()).append(","); //ORDENE_PED_RENDIMIENTO
		}		
		sql.append(orden.isCarteraPropia()?ConstantesGenerales.VERDADERO:ConstantesGenerales.FALSO).append(","); //ORDENE_PED_IN_BDV		
		sql.append(orden.getIdMoneda()==null?"NULL,":"'"+orden.getIdMoneda()+"',");	//MONEDA_ID	 		
		//Verifica precio de recompra
		if (orden.getPrecioRecompra()==0){
			sql.append("NULL,");
		}else{
			sql.append(orden.getPrecioRecompra()).append(","); //ORDENE_PED_RCP_PRECIO
		}		
		//Verifica monto adjudicado
		if (orden.getMontoAdjudicado()==0){
			sql.append("NULL,");
		}else{
			sql.append(orden.getMontoAdjudicado()).append(","); //ORDENE_ADJ_MONTO
		}		
		sql.append(orden.getNombreUsuario()==null?"NULL,":"'"+orden.getNombreUsuario()+"',"); //ORDENE_USR_NOMBRE		
		sql.append(orden.getCentroContable()==null?"NULL,":"'"+orden.getCentroContable()+"',");	//ORDENE_USR_CEN_CONTABLE		
		sql.append(orden.getSucursal()==null?"NULL,":"'"+orden.getSucursal()+"',"); //ORDENE_USR_SUCURSAL		
		sql.append(orden.getTerminal()==null?"NULL,":"'"+orden.getTerminal()+"',"); //ORDENE_USR_TERMINAL		
		sql.append(orden.getVehiculoTomador()==null?"NULL,":"'"+orden.getVehiculoTomador()+"',"); //ORDENE_VEH_TOM		
		sql.append(orden.getVehiculoColocador()==null?"NULL,":"'"+orden.getVehiculoColocador()+"',"); //ORDENE_VEH_COL		
		sql.append(orden.getVehiculoRecompra()==null?"NULL,":"'"+orden.getVehiculoRecompra()+"',"); //ORDENE_VEH_REC		
		sql.append("NULL,"); //ORDENE_EJEC_ID		
		sql.append("NULL,"); //ORDENE_FE_ULT_ACT		
		sql.append(orden.getMontoComisionOrden()).append(","); //ORDENE_PED_COMISIONES		
		sql.append(orden.isFinanciada()?ConstantesGenerales.VERDADERO:ConstantesGenerales.FALSO).append(","); //ORDENE_FINANCIADO	
		if (orden.getIdEjecucion()==0){
			sql.append("NULL,");
		}else{
			sql.append(orden.getIdEjecucion()).append(","); //EJECUCION_ID
		}		
		sql.append(orden.getTasaPool()).append(","); //ORDENE_TASA_POOL		
		sql.append(orden.getGananciaRed()).append(","); //ORDENE_GANANCIA_RED		
		sql.append(orden.getComisionOficina()).append(","); //ORDENE_COMISION_OFICINA		
		sql.append(orden.getComisionOperacion()).append(","); //ORDENE_COMISION_OPERACION		
		sql.append(ConstantesGenerales.FALSO).append(","); //ORDENE_OPERAC_PEND		
		sql.append("NULL").append(",");//ORDENE_ID_RELACION		
		sql.append(orden.getTasaCambio()).append(","); //ORDENE_TASA_CAMBIO		
		sql.append(orden.getConceptoId()==null?"NULL,":Long.valueOf(orden.getConceptoId())+","); //CONCEPTO_ID		
		sql.append(orden.getObservaciones()==null?"NULL,":"'"+orden.getObservaciones()+"',"); //ORDENE_OBSERVACION		
		sql.append(orden.getTipoProducto()==null?"NULL":"'"+orden.getTipoProducto()+"',"); //TIPO_PRODUCTO_ID	
		sql.append(orden.getTipoCuentaAbono());//CTA_ABONO
		sql.append(")");			
		//Guarda la consulta construida de la orden
		consultas.add(sql.toString());
		
		//NUEVO
		guardarDataExtSITME(orden, consultas); //Inserta la data extendida
		
		guardarOperaciones(orden, consultas); //Inserta las operaciones financieras
		
		//NM25287 TTS-441(SICAD II) Inclusion de campos ORIGEN_FONDOS y DESTINO FONDOS 21/03/2014
		guardarDataExt(orden, consultas); //Inserta la data extendida
		
		//Prepara el retorno
		String[] retorno = new String[consultas.size()];
		
		//Recorre la lista y crea un string de consultas
		for (int i=0; i < consultas.size(); i++){
			retorno[i] = consultas.get(i).toString();
		}
		
     	return retorno;	
     	
		}catch (Exception e) {
			//System.out.println("ERROR insertar_SITME "+e.getMessage());
			throw e;
		}
	}
	
	/**Inserta los t&iacute;tulos de la orden*/
	@SuppressWarnings("unchecked")
	private void guardarTitulos(Orden orden, ArrayList consultas) throws Exception{
		//Objeto para el query de titulos
		StringBuffer sqlTitulos = new StringBuffer();
		sqlTitulos.append("INSERT INTO INFI_TB_206_ORDENES_TITULOS");
		sqlTitulos.append(" (ORDENE_ID,TITULO_ID,TITULO_PCT,TITULO_MONTO,TITULO_UNIDADES, TITULO_PCT_RECOMPRA, TITULO_PRECIO_MERCADO, TITULO_MTO_INT_CAIDOS,TITULO_MTO_NETEO)");
		sqlTitulos.append(" VALUES(");
		
		//Busca los t&iacute;tulos asociados a la orden
		if (!orden.isOrdenTituloVacio()){
			ArrayList listaTitulos = orden.getOrdenTitulo();
			//Se recorren los t&iacute;tulos y se almacenan en la base de datos			
			for (Iterator iter = listaTitulos.iterator(); iter.hasNext(); ) {
				StringBuffer sqlTituloValues = new StringBuffer();
				sqlTituloValues.append(sqlTitulos); //Almacena el insert
				
				OrdenTitulo titulo = (OrdenTitulo) iter.next();
				sqlTituloValues.append(orden.getIdOrden()).append(",");
				sqlTituloValues.append("'").append(titulo.getTituloId()).append("',");
				sqlTituloValues.append(titulo.getPorcentaje()).append(",");
				sqlTituloValues.append(titulo.getMonto()).append(",");
				sqlTituloValues.append(titulo.getUnidades()).append(",");
				sqlTituloValues.append(titulo.getPorcentajeRecompra()).append(",");
				sqlTituloValues.append(titulo.getPrecioMercado()).append(",");
				sqlTituloValues.append(titulo.getMontoIntCaidos()).append(",");									
						
				sqlTituloValues.append(titulo.getMontoNeteo());		
					
					
					
				sqlTituloValues.append(")");
				
				//Almacena la consulta generada
				consultas.add(sqlTituloValues.toString());
			}
		}		
	}
	
	
	
	/**Inserta los campos dinamicos en la orden*/
	@SuppressWarnings("unchecked")
	private void guardarCamposDinamicos(Orden orden, ArrayList consultas) throws Exception{

		StringBuffer sqlCampos = new StringBuffer();
		sqlCampos.append("insert into INFI_TB_205_ORDENES_CAMP_DIN(ordene_id,campo_id,campo_valor)");
		sqlCampos.append(" VALUES(");
		//Busca los Campos dinamicos asociados a la orden
		if (!orden.isCampoDinamicoisEmpty()){
			ArrayList camposDinamicos = orden.getCamposDinamicos();
			//Se recorren campos dinamicos se almacenan en la base de datos			
			for (Iterator iter = camposDinamicos.iterator(); iter.hasNext(); ) {			
				StringBuffer sqlcamposDinamicos = new StringBuffer();
				sqlcamposDinamicos.append(sqlCampos); //Almacena el insert
				CampoDinamico campoDinamico 	=	(CampoDinamico) iter.next();
				sqlcamposDinamicos.append(orden.getIdOrden()).append(",");
				sqlcamposDinamicos.append(campoDinamico.getIdCampo()).append(",'");
				sqlcamposDinamicos.append(campoDinamico.getValor()).append("')");
				
				//Almacena la consulta generada
				consultas.add(sqlcamposDinamicos.toString());
				
			}
		}		
	}
	
	/**Inserta o modifica las operaciones financieras de la orden*/
	@SuppressWarnings("unchecked")
	private void guardarOperaciones(Orden orden, ArrayList consultas) throws Exception{
		//Objeto para el query		
		
		//Almacena las transacciones financieras asociadas a la orden
		if (!orden.isOperacionVacio()){
		
			ArrayList listaOperaciones = orden.getOperacion();
			//Se recorre la lista y se almacenan en la base de datos			
			for (Iterator iter = listaOperaciones.iterator(); iter.hasNext(); ) {
				StringBuffer sqlOperaciones = new StringBuffer();
				StringBuffer sqlOperacionesValues = new StringBuffer();				
				OrdenOperacion ordenOperacion =	(OrdenOperacion) iter.next();
				if (ordenOperacion.getIdOperacion()>0){
					
					//Se modifica los datos de la operaci&oacute;n
					sqlOperaciones.append("UPDATE INFI_TB_207_ORDENES_OPERACION ");
					sqlOperaciones.append("SET FECHA_PROCESADA=").append(this.formatearFechaBD(ordenOperacion.getFechaProcesada())).append(",");
					sqlOperaciones.append("STATUS_OPERACION='").append(ordenOperacion.getStatusOperacion()).append("', ");
					sqlOperaciones.append("SERIAL='").append(ordenOperacion.getSerialContable()).append("', ");					
					sqlOperaciones.append("CODIGO_OPERACION='").append(ordenOperacion.getCodigoOperacion()).append("' ");					
					sqlOperaciones.append(" WHERE ORDENE_ID=").append(orden.getIdOrden());
					sqlOperaciones.append(" AND ORDENE_OPERACION_ID=").append(ordenOperacion.getIdOperacion());
					sqlOperacionesValues.append(sqlOperaciones); //Almacena el update
				} else{
					
					//Hace el insert
					sqlOperaciones.append("INSERT INTO INFI_TB_207_ORDENES_OPERACION (ORDENE_ID,ORDENE_OPERACION_ID,");
					sqlOperaciones.append("TRNFIN_ID,STATUS_OPERACION,");
					sqlOperaciones.append("APLICA_REVERSO,MONTO_OPERACION,TASA,FECHA_APLICAR,FECHA_PROCESADA,");
					sqlOperaciones.append("ORDENE_OPERACION_ERROR,SERIAL,IN_COMISION,MONEDA_ID,");					
					sqlOperaciones.append("CTECTA_NUMERO, OPERACION_NOMBRE, CTECTA_NOMBRE,CTECTA_BCOCTA_BCO,CTECTA_BCOCTA_DIRECCION,CTECTA_BCOCTA_SWIFT,");
					sqlOperaciones.append("CTECTA_BCOCTA_BIC,CTECTA_BCOCTA_TELEFONO,CTECTA_BCOCTA_ABA,CTECTA_BCOCTA_PAIS,CTECTA_BCOINT_BCO,");
					sqlOperaciones.append("CTECTA_BCOINT_DIRECCION,CTECTA_BCOINT_SWIFT,CTECTA_BCOINT_BIC,CTECTA_BCOINT_TELEFONO,CTECTA_BCOINT_ABA,");   
					sqlOperaciones.append("CTECTA_BCOINT_PAIS,TRNF_TIPO,TITULO_ID,CODIGO_OPERACION,NUMERO_RETENCION,ORDENE_RELAC_OPERACION_ID,FECHA_INICIO_CP,FECHA_FIN_CP,IN_COMISION_INVARIABLE)");					
					sqlOperaciones.append(" VALUES(");
					
					sqlOperacionesValues.append(sqlOperaciones); //Almacena el insert					
					long idOperacion = Long.valueOf(dbGetSequence(this.dataSource, ConstantesGenerales.SECUENCIA_ORDENES_OPERACION)).longValue();
					ordenOperacion.setIdOperacion(idOperacion);
					sqlOperacionesValues.append(orden.getIdOrden()).append(",");
					sqlOperacionesValues.append(idOperacion).append(",");
					sqlOperacionesValues.append(ordenOperacion.getIdTransaccionFinanciera()==null?"NULL":"'"+ordenOperacion.getIdTransaccionFinanciera()+"'").append(",");
					sqlOperacionesValues.append("'").append(ordenOperacion.getStatusOperacion()).append("',");
					sqlOperacionesValues.append(ConstantesGenerales.FALSO).append(",");
					sqlOperacionesValues.append(ordenOperacion.getMontoOperacion().doubleValue()).append(",");
					sqlOperacionesValues.append(ordenOperacion.getTasa()).append(",");
					sqlOperacionesValues.append(ordenOperacion.getFechaAplicar()==null?formatearFechaBDActual():this.formatearFechaBD(ordenOperacion.getFechaAplicar())).append(",");
					sqlOperacionesValues.append(ordenOperacion.getFechaProcesada()==null?"NULL":formatearFechaBD(ordenOperacion.getFechaProcesada())).append(",");
					sqlOperacionesValues.append(ordenOperacion.getErrorNoAplicacion()==null?"NULL":"'"+ordenOperacion.getErrorNoAplicacion()+"'").append(",");
					sqlOperacionesValues.append((ordenOperacion.getSerialContable()==null)||(ordenOperacion.getSerialContable()=="")?"NULL":"'"+ordenOperacion.getSerialContable()+"'").append(",");
					sqlOperacionesValues.append(ordenOperacion.getInComision()).append(",");					
					sqlOperacionesValues.append(ordenOperacion.getIdMoneda()==null?"NULL":"'"+ordenOperacion.getIdMoneda()+"'").append(",");
					sqlOperacionesValues.append(ordenOperacion.getNumeroCuenta()==null?"NULL":"'"+ordenOperacion.getNumeroCuenta()+"'").append(",");
					sqlOperacionesValues.append(ordenOperacion.getNombreOperacion()==null?"NULL":"'"+ordenOperacion.getNombreOperacion()+"'").append(",");
					sqlOperacionesValues.append(ordenOperacion.getNombreReferenciaCuenta()==null?"NULL":"'"+ordenOperacion.getNombreReferenciaCuenta()+"'").append(",");
					sqlOperacionesValues.append(ordenOperacion.getNombreBanco()==null?"NULL":"'"+ordenOperacion.getNombreBanco()+"'").append(",");
					sqlOperacionesValues.append(ordenOperacion.getDireccionBanco()==null?"NULL":"'"+ordenOperacion.getDireccionBanco()+"'").append(",");
					sqlOperacionesValues.append(ordenOperacion.getCodigoSwiftBanco()==null?"NULL":"'"+ordenOperacion.getCodigoSwiftBanco()+"'").append(",");
					sqlOperacionesValues.append(ordenOperacion.getCodigoBicBanco()==null?"NULL":"'"+ordenOperacion.getCodigoBicBanco()+"'").append(",");
					sqlOperacionesValues.append(ordenOperacion.getTelefonoBanco()==null?"NULL":"'"+ordenOperacion.getTelefonoBanco()+"'").append(",");
					sqlOperacionesValues.append(ordenOperacion.getCodigoABA()==null?"NULL":"'"+ordenOperacion.getCodigoABA()+"'").append(",");	
					sqlOperacionesValues.append(ordenOperacion.getPaisBancoCuenta()==null?"NULL":"'"+ordenOperacion.getPaisBancoCuenta()+"'").append(",");
					sqlOperacionesValues.append(ordenOperacion.getNombreBancoIntermediario()==null?"NULL":"'"+ordenOperacion.getNombreBancoIntermediario()+"'").append(",");
					sqlOperacionesValues.append(ordenOperacion.getDireccionBancoIntermediario()==null?"NULL":"'"+ordenOperacion.getDireccionBancoIntermediario()+"'").append(",");
					sqlOperacionesValues.append(ordenOperacion.getCodigoSwiftBancoIntermediario()==null?"NULL":"'"+ordenOperacion.getCodigoSwiftBancoIntermediario()+"'").append(",");	
					sqlOperacionesValues.append(ordenOperacion.getCodigoBicBancoIntermediario()==null?"NULL":"'"+ordenOperacion.getCodigoBicBancoIntermediario()+"'").append(",");
					sqlOperacionesValues.append(ordenOperacion.getTelefonoBancoIntermediario()==null?"NULL":"'"+ordenOperacion.getTelefonoBancoIntermediario()+"'").append(",");
					sqlOperacionesValues.append(ordenOperacion.getCodigoABAIntermediario()==null?"NULL":"'"+ordenOperacion.getCodigoABAIntermediario()+"'").append(",");	
					sqlOperacionesValues.append(ordenOperacion.getPaisBancoIntermediario()==null?"NULL":"'"+ordenOperacion.getPaisBancoIntermediario()+"'").append(",");
					sqlOperacionesValues.append(ordenOperacion.getTipoTransaccionFinanc()==null?"NULL":"'"+ordenOperacion.getTipoTransaccionFinanc()+"'").append(",");
					sqlOperacionesValues.append(ordenOperacion.getIdTitulo()==null?"NULL":"'"+ordenOperacion.getIdTitulo()+"'").append(",");
					sqlOperacionesValues.append(ordenOperacion.getCodigoOperacion()==null?"NULL":"'"+ordenOperacion.getCodigoOperacion()+"'").append(",");	
					sqlOperacionesValues.append(ordenOperacion.getNumeroRetencion()==null?"NULL":"'"+ordenOperacion.getNumeroRetencion()+"'").append(",");
					sqlOperacionesValues.append(ordenOperacion.getIdOperacionRelacion()).append(",");
					sqlOperacionesValues.append(ordenOperacion.getFechaInicioCP()==null?"NULL":this.formatearFechaBD(ordenOperacion.getFechaInicioCP())).append(",");
					sqlOperacionesValues.append(ordenOperacion.getFechaFinCP()==null?"NULL":this.formatearFechaBD(ordenOperacion.getFechaFinCP())).append(",");
					sqlOperacionesValues.append(ordenOperacion.getIndicadorComisionInvariable());
					sqlOperacionesValues.append(")");	
						
				}	
				
				//setear nuevamente la lista de operaciones de la orden ya que los objetos de operacion fueron modificados seteandoseles el id consecutivo correspondiente
				orden.setOperacion(listaOperaciones);
				
				//Almacena la consulta generada
				consultas.add(sqlOperacionesValues.toString());
			}
		}
	}
	
	

	/**Inserta los documentos asociados a la orden*/
	@SuppressWarnings("unchecked")
	private void insertarDocumentos(Orden orden, ArrayList consultas) throws Exception{
		//Objeto para el query
		StringBuffer sqlDocumentos = new StringBuffer();
		sqlDocumentos.append("INSERT INTO INFI_TB_208_ORDENES_DOCUMENTOS (ORDENE_ID,ORDENE_DOC_ID, NOMBRE)");
		sqlDocumentos.append(" VALUES(");
		
		//Almacena los documentos asociados a la orden
		if (!orden.isDocumentosVacio()){
			ArrayList listaDocumentos = orden.getDocumentos();
			//Se recorre la lista y se almacenan en la base de datos			
			for (Iterator iter = listaDocumentos.iterator(); iter.hasNext(); ) {
				StringBuffer sqlDocumentosValues = new StringBuffer();
				sqlDocumentosValues.append(sqlDocumentos); //Almacena el insert
				
				OrdenDocumento documento =	(OrdenDocumento) iter.next();
				
				//Verifica si el id del documento es 0 para insertarlo
				if (documento.getIdDocumento()==0){				
					sqlDocumentosValues.append(orden.getIdOrden()).append(", ");
					sqlDocumentosValues.append(documento.getIdDocumento()).append(", ");
					sqlDocumentosValues.append("'").append(documento.getNombre()).append("'");					
					sqlDocumentosValues.append(")");
					
					//Almacena la consulta generada
					consultas.add(sqlDocumentosValues.toString());					
				}				
			}
		}	
	}
	
	/**Inserta los documentos asociados a la orden*/
	public void insertarDocumentos(Orden orden) throws Exception{
		//Objeto para el query
		StringBuffer sqlDocumentos = new StringBuffer();
		sqlDocumentos.append("INSERT INTO INFI_TB_208_ORDENES_DOCUMENTOS (ORDENE_ID,ORDENE_DOC_ID, NOMBRE)");
		sqlDocumentos.append(" VALUES(");
		
		//Almacena los documentos asociados a la orden
		if (!orden.isDocumentosVacio()){
			ArrayList listaDocumentos = orden.getDocumentos();
			String[] st = new String[listaDocumentos.size()];
			int contador = 0;
			//Se recorre la lista y se almacenan en la base de datos			
			for (Iterator iter = listaDocumentos.iterator(); iter.hasNext(); ) {
				StringBuffer sqlDocumentosValues = new StringBuffer();
				sqlDocumentosValues.append(sqlDocumentos); //Almacena el insert
				
				OrdenDocumento documento =	(OrdenDocumento) iter.next();
				
				//Verifica si el id del documento es 0 para insertarlo
				//if (documento.getIdDocumento()==0){				
					documento.setIdOrden(orden.getIdOrden());
					sqlDocumentosValues.append(orden.getIdOrden()).append(", ");
					sqlDocumentosValues.append(documento.getIdDocumento()).append(", ");
					sqlDocumentosValues.append("'").append(documento.getNombre()).append("'");
					sqlDocumentosValues.append(")");
					
					//Almacena la consulta generada
					st[contador++] = sqlDocumentosValues.toString();
				//}				
			}
			if (st.length > 0){
				db.execBatch(this.dataSource, st);
			}
		}	
	}
	
	/**Inserta los documentos asociados a la orden pero devuleve los SQL involucrados*/
	public String[] insertarDocumentosSQL(Orden orden) throws Exception{
		//Objeto para el query
		String[] st = null;
		StringBuffer sqlDocumentos = new StringBuffer();
		sqlDocumentos.append("INSERT INTO INFI_TB_208_ORDENES_DOCUMENTOS (ORDENE_ID,ORDENE_DOC_ID, NOMBRE)");
		sqlDocumentos.append(" VALUES(");
		
		//Almacena los documentos asociados a la orden
		if (!orden.isDocumentosVacio()){
			ArrayList listaDocumentos = orden.getDocumentos();
			st = new String[listaDocumentos.size()];
			int contador = 0;
			//Se recorre la lista y se almacenan en la base de datos			
			for (Iterator iter = listaDocumentos.iterator(); iter.hasNext(); ) {
				StringBuffer sqlDocumentosValues = new StringBuffer();
				sqlDocumentosValues.append(sqlDocumentos); //Almacena el insert
				
				OrdenDocumento documento =	(OrdenDocumento) iter.next();
				
				//Verifica si el id del documento es 0 para insertarlo
				//if (documento.getIdDocumento()==0){				
					documento.setIdOrden(orden.getIdOrden());
					sqlDocumentosValues.append(orden.getIdOrden()).append(", ");
					sqlDocumentosValues.append(documento.getIdDocumento()).append(", ");
					sqlDocumentosValues.append("'").append(documento.getNombre()).append("'");
					sqlDocumentosValues.append(")");
					
					//Almacena la consulta generada
					st[contador++] = sqlDocumentosValues.toString();
				//}				
			}
		}
		return st;
	}	
	
	/**Inserta o modifica la data extendida asociada a la orden si existe*/
	@SuppressWarnings("unchecked")
	private void guardarDataExt(Orden orden, ArrayList consultas) throws Exception{
		//Objeto para el query
		StringBuffer sqlDataExt = new StringBuffer();

		//Almacena data extendida en de la orden
		if (!orden.isOrdenDataExtVacio()){
			ArrayList listaDataExt = orden.getOrdenDataExt();
			//Se recorre la lista y se almacenan en la base de datos			
			for (Iterator ite = listaDataExt.iterator(); ite.hasNext(); ) {
				StringBuffer sqlDataExtValues = new StringBuffer();		
				sqlDataExt = new StringBuffer();
				OrdenDataExt dataExt =	(OrdenDataExt) ite.next();
				
				//Verifica si se modifica o se inserta seg&uacute;n venga lleno el campo ordenId
				if (dataExt.getIdOrden()>0){
					sqlDataExt.append("UPDATE INFI_TB_212_ORDENES_DATAEXT ");					
					sqlDataExtValues.append(sqlDataExt); //Almacena el update
					
					sqlDataExtValues.append(" SET DTAEXT_VALOR='").append(dataExt.getValor()).append("'");
					sqlDataExtValues.append(" WHERE ORDENE_ID=").append(dataExt.getIdOrden());
					sqlDataExtValues.append(" AND DTAEXT_ID='").append(dataExt.getIdData()).append("'");					
				} else{
					sqlDataExt.append("INSERT INTO INFI_TB_212_ORDENES_DATAEXT (ORDENE_ID,DTAEXT_ID,DTAEXT_VALOR)");
					sqlDataExt.append(" VALUES(");
					
					sqlDataExtValues.append(sqlDataExt); //Almacena el insert
					sqlDataExtValues.append(orden.getIdOrden()).append(",");
					sqlDataExtValues.append("'"+dataExt.getIdData()+"',");
					sqlDataExtValues.append("'"+dataExt.getValor()+"'");
					sqlDataExtValues.append(")");
				}
								
				//Almacena la consulta generada
				consultas.add(sqlDataExtValues.toString());				
			}
		}
	}
	
	//NUEVO VICTOR GONCALVES se usa
	/**Inserta o modifica la data extendida asociada a la orden si existe*/
	@SuppressWarnings("unchecked")
	private void guardarDataExtSITME(Orden orden, ArrayList consultas) throws Exception{
		//Objeto para el query
		
	
		StringBuffer sqlDataExt = new StringBuffer();
		sqlDataExt.append("INSERT INTO INFI_TB_212_ORDENES_DATAEXT (ORDENE_ID,DTAEXT_ID,DTAEXT_VALOR)");
		sqlDataExt.append(" VALUES(");
		sqlDataExt.append(orden.getIdOrden()).append(",");
		sqlDataExt.append("'"+DataExtendida.NRO_TICKET+"',");
		sqlDataExt.append(orden.getContraparte());
		sqlDataExt.append(")");
		//Almacena la consulta generada
		consultas.add(sqlDataExt.toString());	
	
		StringBuffer sqlDataExt2 = new StringBuffer();
		sqlDataExt2.append("INSERT INTO INFI_TB_212_ORDENES_DATAEXT (ORDENE_ID,DTAEXT_ID,DTAEXT_VALOR)");
		sqlDataExt2.append(" VALUES(");
		sqlDataExt2.append(orden.getIdOrden()).append(",");
		sqlDataExt2.append("'"+DataExtendida.COMISION_CLAVENET+"',");
		sqlDataExt2.append(orden.getComisionOperacion().toString());
		sqlDataExt2.append(")");
		//Almacena la consulta generada
		consultas.add(sqlDataExt2.toString());	

		
	}

	/**
	 * modifica los datos de una orden en la base de datos
	 * @param orden a ser insertada
	 * @throws Exception lanza una excepci&oacute;n si hay un error
	*/	
	//Modificacion de metodo en desarrollo SICAD nm26659
	@SuppressWarnings("unchecked")
	public String[] modificar(Orden orden) throws Exception {
		ArrayList consultas = new ArrayList();
		
		//Modifica la orden en la base de datos
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE INFI_TB_204_ORDENES SET ");
		
		//Verifica si la orden tiene ID
		if (orden.getIdOrden()==0){
			throw new Exception("Error, la orden enviada no tiene id v&aacute;lido");
		}
		
		if (orden.getContraparte()==null){
			sql.append("CONTRAPARTE=null,");
		}else{
			sql.append("CONTRAPARTE='").append(orden.getContraparte()).append("', ");	
		}		
		
		sql.append("SISTEMA_ID='").append(orden.getIdSistema()).append("', ");
		sql.append("ENVIADO=").append(orden.isEnviado()?ConstantesGenerales.VERDADERO:ConstantesGenerales.FALSO).append(" ").append(", ");
		sql.append("ORDENE_PED_TOTAL_PEND=").append(orden.getMontoPendiente());
		if(orden.getSegmentoBanco()!=null){
			sql.append(",ORDENE_CTE_SEG_BCO='").append(orden.getSegmentoBanco()).append("'");		
		}
		if(orden.getSegmentoSegmento()!=null){
			sql.append(",ORDENE_CTE_SEG_SEG='").append(orden.getSegmentoSegmento()).append("'");			
		}
		if(orden.getSegmentoSubSegmento()!=null){
			sql.append(",ORDENE_CTE_SEG_SUB='").append(orden.getSegmentoSubSegmento()).append("'");			
		}
		if(orden.getSegmentoInfi()!=null){
			sql.append(",ORDENE_CTE_SEG_INFI='").append(orden.getSegmentoInfi()).append("'");			
		}
		if(orden.getCuentaCliente()!=null){
			sql.append(",ORDENE_PED_CTA_BSNRO='").append(orden.getCuentaCliente()).append("'");			
		}
		if(orden.getTipoCuenta()!=null){
			sql.append(",ORDENE_PED_CTA_BSTIP='").append(orden.getTipoCuenta()).append("'");			
		}
		if (orden.getPrecioRecompra()!=new BigDecimal(0).doubleValue()){
			sql.append(", ").append("ORDENE_PED_RCP_PRECIO=").append(orden.getPrecioRecompra());
		}
		if (orden.getMontoInteresCaidos()!=new BigDecimal(0).doubleValue()){
			sql.append(", ").append("ORDENE_PED_INT_CAIDOS=").append(orden.getMontoInteresCaidos());
		}
		if (orden.getMonto()!=new BigDecimal(0).doubleValue()){
			sql.append(", ").append("ORDENE_PED_MONTO=").append(orden.getMonto());
		}
		if (orden.getMontoTotal()!=new BigDecimal(0).doubleValue()){
			/*
			 * Si el monto tal es = a -1 el monto se actualiza a 0
			 * Esto ocurre cuando a una orden le adjudican 0 por lo cual el monto total
			 * debe ser actualizado
			 */
			if(orden.getMontoTotal()==-1)
			{
				sql.append(", ").append("ORDENE_PED_TOTAL=").append(0);
			/*
			 * En caso contrario el monto a actualizar es el mismo que le sea seteado al objeto orden
			 */	
			}else{
				sql.append(", ").append("ORDENE_PED_TOTAL=").append(orden.getMontoTotal());
			}
		}
		if(!orden.getStatus().equals(null) && !orden.getStatus().equals("")){
			sql.append(", ").append("ORDSTA_ID='").append(orden.getStatus()).append("'");
		}
															//Modificacion version ITS-638
		if((orden.getMontoComisionOrden().intValue()!=0) || (orden.getMontoComisionOrden().intValue()==0 && orden.getComisionCero())){
			sql.append(", ").append("ORDENE_PED_COMISIONES=").append(orden.getMontoComisionOrden());
		}
		if(orden.getComisionOperacion().intValue()!=0){
			sql.append(", ").append("ORDENE_COMISION_OPERACION=").append(orden.getComisionOperacion());
		}
		if (orden.getFechaAdjudicacion()!=null){
			sql.append(", ").append("ORDENE_FECHA_ADJUDICACION=").append(this.formatearFechaBD(orden.getFechaAdjudicacion()));
			sql.append(", ").append("ORDENE_ADJ_MONTO=").append(new BigDecimal(orden.getMontoAdjudicado()));
		}
		if (orden.getFechaLiquidacion()!=null){
			sql.append(", ").append("ORDENE_FECHA_LIQUIDACION=").append(this.formatearFechaBD(orden.getFechaLiquidacion()));	
		}
		if (orden.getFechaCustodia()!=null){
			sql.append(", ").append("ORDENE_FECHA_CUSTODIA=").append(this.formatearFechaBD(orden.getFechaCustodia()));	
		}
		if (orden.getFechaCancelacion()!=null){
			sql.append(", ").append("ORDENE_FE_CANCELACION=").append(this.formatearFechaBD(orden.getFechaCancelacion()));	
		}
		if (orden.getPrecioCompra()!=new BigDecimal(0).doubleValue()){
			sql.append(", ").append("ORDENE_PED_PRECIO=").append(orden.getPrecioCompra());
		}
		
		if(orden.getTasaCambio()>0){
			sql.append(", ").append("ORDENE_TASA_CAMBIO=").append(orden.getTasaCambio());
		}
		
		if(orden.getIdEjecucion()>0){
			sql.append(", ").append("EJECUCION_ID=").append(orden.getIdEjecucion());
		}
		
		if(orden.getFechaValorString()!=null){
			sql.append(", ").append("ORDENE_PED_FE_VALOR=").append("TO_DATE('").append(orden.getFechaValorString()).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("')");
		}
		
		if(orden.getFechaUltActualizacion()!=null){
			sql.append(",ORDENE_FE_ULT_ACT=").append(this.formatearFechaBD(orden.getFechaUltActualizacion())).append(" ");
		}
			
		//Se incorpora el where de la consulta
		sql.append(" WHERE ORDENE_ID=").append(orden.getIdOrden());
		//Guarda la consulta construida de la orden
		consultas.add(sql.toString());
		
		guardarOperaciones(orden, consultas); //Inserta las operaciones financieras
		guardarDataExt(orden, consultas); //Inserta la data extendida		
		
		//Prepara el retorno
		String[] retorno = new String[consultas.size()];
		
		//Recorre la lista y crea un string de consyltas
		for (int i=0; i < consultas.size(); i++){
			retorno[i] = consultas.get(i).toString();
		}
     	return retorno;	

	}
	
	/**
	 * Actualiza un registro de bloqueo de t&iacute;tulo de un cliente en particular, luego de un desbloqueo.
	 * @param tituloBloqueo objeto con los datos del bloqueo
	 * @return String con sentencia sql
	 */
	public String modificarStatus(Orden orden){
		
		StringBuffer sql = new StringBuffer();		
		
		sql.append("UPDATE INFI_TB_204_ORDENES SET ");		
		sql.append(" ordsta_id = '").append(orden.getStatus()).append("'");		
		sql.append(" WHERE ordene_id = ").append(orden.getIdOrden());
		
		return(sql.toString());
	}
	
	public void actualizarStatus(Orden orden) throws Exception{
		
		StringBuffer sql = new StringBuffer();		
		
		sql.append("UPDATE INFI_TB_204_ORDENES SET ");		
		sql.append(" ordsta_id = '").append(orden.getStatus()).append("'");		
		sql.append(" WHERE ordene_id = ").append(orden.getIdOrden());
		db.exec(dataSource, sql.toString());
		//return(sql.toString());
	}

	//
	/**
	 * Modifica el status de una orden
	 * @param status el estado de la orden
	 * @param idOrden el id de la orden
	 * @return String con sentencia sql
	 */
	public String modificarStatus(String status, long idOrden){
		
		StringBuffer sql = new StringBuffer();		
		
		sql.append("UPDATE INFI_TB_204_ORDENES SET ");		
		sql.append(" ordsta_id = '").append(status).append("'");		
		sql.append(" WHERE ordene_id = ").append(idOrden);
		sql.append(" and transa_id in(' "  + TransaccionNegocio.TOMA_DE_ORDEN + "','" + TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA + "')");
		
		return(sql.toString());
	}
	
	//NUEVO VICTOR GONCALVES
	/**Busca la orden por el id de orden
	 * Arma el objeto completo para que pueda ser modificado cualquier valor
	 * */
	@SuppressWarnings("unchecked")
	public Orden listarOrdenSITME(long idOrden) throws Exception{
		
		Orden objOrden				=new Orden();
		OrdenDAO ordenDao			=new OrdenDAO(dataSource);
		SimpleDateFormat formato	=new SimpleDateFormat("dd-MM-yyyy");
		
			try {
				//Busca los campos dinamicos asociados a la orden
				CamposDinamicos camposDinamicos=new CamposDinamicos(dataSource);
				DataSet _campos		=camposDinamicos.listarCamposDinamicosOrdenes(idOrden,0);	
				if(_campos.count()>0){
					_campos.first();
					ArrayList camposDinamicosArr=new ArrayList();			
					while(_campos.next()){
						com.bdv.infi.data.CampoDinamico campoDin= new com.bdv.infi.data.CampoDinamico();
						campoDin.setIdCampo(Integer.parseInt(_campos.getValue("campo_id")));
						campoDin.setValor(_campos.getValue("campo_valor"));
						camposDinamicosArr.add(campoDin);
					}
					objOrden.setCamposDinamicos(camposDinamicosArr);//Se agregan los campos dinamicos a la orden
				}
				
				//Buscar los titulos asociados
				TitulosDAO titulo=new TitulosDAO(dataSource);
				titulo.listarDatosTituloOrden(String.valueOf(idOrden));
				if(titulo.getDataSet().count()>0){
					titulo.getDataSet().first();
					ArrayList titulosArr=new ArrayList();
					while(titulo.getDataSet().next()){
						OrdenTitulo tituloOrden=new OrdenTitulo();
						tituloOrden.setTituloId(titulo.getDataSet().getValue("titulo_id"));
						tituloOrden.setMonto(Double.parseDouble(titulo.getDataSet().getValue("titulo_monto")));
						tituloOrden.setPorcentaje(titulo.getDataSet().getValue("titulo_pct"));
						tituloOrden.setUnidades(Double.parseDouble(titulo.getDataSet().getValue("titulo_unidades")));
						tituloOrden.setMontoIntCaidos(new BigDecimal(titulo.getDataSet().getValue("titulo_mto_int_caidos")));
						tituloOrden.setPorcentajeRecompra(Double.parseDouble(titulo.getDataSet().getValue("titulo_pct_recompra")));
						tituloOrden.setPrecioMercado(Double.parseDouble(titulo.getDataSet().getValue("titulo_precio_mercado")));
						titulosArr.add(tituloOrden);
					}
					objOrden.agregarOrdenTitulo(titulosArr);//se agregan los titulos a la orden
				}
				//Documentos asociados a la orden
				ordenDao.consultarDocumentosOrden(idOrden);
				if(ordenDao.getDataSet().count()>0){
					ordenDao.getDataSet().first();
					ArrayList documentosArr=new ArrayList();
					while(ordenDao.getDataSet().next()){
						OrdenDocumento doc = new OrdenDocumento();
						doc.setIdDocumento(Long.parseLong(ordenDao.getDataSet().getValue("ordene_doc_id")));
						doc.setIdOrden(idOrden);
						doc.setNombre(ordenDao.getDataSet().getValue("nombre"));
						documentosArr.add(doc);
					}
					objOrden.setDocumentos(documentosArr);// Agregan los documentos asociados a la orden
				}
				//
				//buscar las operaciones aplicadas a la orden
				ordenDao.listarDetallesOrdenOperacion(idOrden);
				if(ordenDao.getDataSet().count()>0){
					ordenDao.getDataSet().first();
					ArrayList operacionesArr=new ArrayList();
					while(ordenDao.getDataSet().next()){ 
						OrdenOperacion ordenOperacion=new OrdenOperacion();
						if(ordenDao.getDataSet().getValue("aplica_reverso")!=null){ordenOperacion.setAplicaReverso(Boolean.parseBoolean(ordenDao.getDataSet().getValue("aplica_reverso")));}
						if(ordenDao.getDataSet().getValue("moneda_id")!="null"){ordenOperacion.setIdMoneda(ordenDao.getDataSet().getValue("moneda_id"));}
						if(ordenDao.getDataSet().getValue("ordene_operacion_id")!=null){ordenOperacion.setIdOperacion(Long.parseLong(ordenDao.getDataSet().getValue("ordene_operacion_id")));}
						if(ordenDao.getDataSet().getValue("ordene_relac_operacion_id")!=null){ordenOperacion.setIdOperacionRelacion(Long.parseLong(ordenDao.getDataSet().getValue("ordene_relac_operacion_id")));}else ordenOperacion.setIdOperacionRelacion(0);
						if(ordenDao.getDataSet().getValue("codigo_operacion")!=null){ordenOperacion.setCodigoOperacion(ordenDao.getDataSet().getValue("codigo_operacion"));}
						if(ordenDao.getDataSet().getValue("trnfin_id")!=null){ordenOperacion.setIdTransaccionFinanciera(ordenDao.getDataSet().getValue("trnfin_id"));}
						if(ordenDao.getDataSet().getValue("status_operacion")!=null){ordenOperacion.setStatusOperacion(ordenDao.getDataSet().getValue("status_operacion"));}				
						if(ordenDao.getDataSet().getValue("monto_operacion")!=null){ordenOperacion.setMontoOperacion(new BigDecimal(Double.parseDouble(ordenDao.getDataSet().getValue("monto_operacion"))));}
						if(ordenDao.getDataSet().getValue("tasa")!=null){ordenOperacion.setTasa(new BigDecimal(ordenDao.getDataSet().getValue("tasa")));}			
						if(ordenDao.getDataSet().getValue("fecha_aplicar")!=null){ordenOperacion.setFechaProcesada(formato.parse(ordenDao.getDataSet().getValue("fecha_aplicar")));}			
						if(ordenDao.getDataSet().getValue("fecha_procesada")!=null){ordenOperacion.setFechaAplicar(formato.parse(ordenDao.getDataSet().getValue("fecha_procesada")));}
						if(ordenDao.getDataSet().getValue("OPERACION_NOMBRE")!=null){ordenOperacion.setNombreOperacion(ordenDao.getDataSet().getValue("OPERACION_NOMBRE"));}
						if(ordenDao.getDataSet().getValue("ordene_operacion_error")!=null){ordenOperacion.setErrorNoAplicacion(ordenDao.getDataSet().getValue("ordene_operacion_error"));}
						//if(ordenDao.getDataSet().getValue("moneda_id")!=null){operacion.setError(ordenDao.getDataSet().getValue(""));}
						if(ordenDao.getDataSet().getValue("serial")!=null){ordenOperacion.setSerialContable(ordenDao.getDataSet().getValue("serial"));}
						if(ordenDao.getDataSet().getValue("in_comision")!=null){ordenOperacion.setInComision(Integer.parseInt(ordenDao.getDataSet().getValue("in_comision")));}
						if(ordenDao.getDataSet().getValue("ctecta_numero")!=null){ordenOperacion.setNumeroCuenta(ordenDao.getDataSet().getValue("ctecta_numero"));}
						if(ordenDao.getDataSet().getValue("numero_retencion")!=null){ordenOperacion.setNumeroRetencion(ordenDao.getDataSet().getValue("numero_retencion"));}
						if(ordenDao.getDataSet().getValue("ctecta_nombre")!=null){ordenOperacion.setNombreReferenciaCuenta(ordenDao.getDataSet().getValue("ctecta_nombre"));}
						if(ordenDao.getDataSet().getValue("ctecta_bcocta_bco")!=null){ordenOperacion.setNombreBanco(ordenDao.getDataSet().getValue("ctecta_bcocta_bco"));}
						if(ordenDao.getDataSet().getValue("ctecta_bcocta_direccion")!=null){ordenOperacion.setDireccionBanco(ordenDao.getDataSet().getValue("ctecta_bcocta_direccion"));}
						if(ordenDao.getDataSet().getValue("ctecta_bcocta_swift")!=null){ordenOperacion.setCodigoSwiftBanco(ordenDao.getDataSet().getValue("ctecta_bcocta_swift"));}
						if(ordenDao.getDataSet().getValue("ctecta_bcocta_telefono")!=null){ordenOperacion.setTelefonoBanco(ordenDao.getDataSet().getValue("ctecta_bcocta_telefono"));}
						if(ordenDao.getDataSet().getValue("ctecta_bcocta_aba")!=null){ordenOperacion.setCodigoABA(ordenDao.getDataSet().getValue("ctecta_bcocta_aba"));}
						if(ordenDao.getDataSet().getValue("ctecta_bcocta_pais")!=null){ordenOperacion.setPaisBancoCuenta(ordenDao.getDataSet().getValue("ctecta_bcocta_pais"));}
						if(ordenDao.getDataSet().getValue("ctecta_bcoint_bco")!=null){ordenOperacion.setNombreBancoIntermediario(ordenDao.getDataSet().getValue("ctecta_bcoint_bco"));}
						if(ordenDao.getDataSet().getValue("ctecta_bcoint_direccion")!=null){ordenOperacion.setDireccionBancoIntermediario(ordenDao.getDataSet().getValue("ctecta_bcoint_direccion"));}
						if(ordenDao.getDataSet().getValue("ctecta_bcoint_swift")!=null){ordenOperacion.setCodigoSwiftBancoIntermediario(ordenDao.getDataSet().getValue("ctecta_bcoint_swift"));}
						if(ordenDao.getDataSet().getValue("ctecta_bcoint_bic")!=null){ordenOperacion.setCodigoBicBancoIntermediario(ordenDao.getDataSet().getValue("ctecta_bcoint_bic"));}
						if(ordenDao.getDataSet().getValue("ctecta_bcoint_telefono")!=null){ordenOperacion.setTelefonoBancoIntermediario(ordenDao.getDataSet().getValue("ctecta_bcoint_telefono"));}
						if(ordenDao.getDataSet().getValue("ctecta_bcoint_aba")!=null){ordenOperacion.setCodigoABAIntermediario(ordenDao.getDataSet().getValue("ctecta_bcoint_aba"));}
						if(ordenDao.getDataSet().getValue("ctecta_bcoint_pais")!=null){ordenOperacion.setPaisBancoIntermediario(ordenDao.getDataSet().getValue("ctecta_bcoint_pais"));}
						if(ordenDao.getDataSet().getValue("trnf_tipo")!=null){ordenOperacion.setTipoTransaccionFinanc(ordenDao.getDataSet().getValue("trnf_tipo"));}

						operacionesArr.add(ordenOperacion);
					}
					objOrden.setOperacion(operacionesArr);// Se agregan las operaciones asociadas a la orden
				}
				ordenDao.listarDetallesOrden(idOrden);
				//Setear los valores de la orden
				if(ordenDao.getDataSet().count()>0){
					ordenDao.getDataSet().first();
					ordenDao.getDataSet().next();
					if(ordenDao.getDataSet().getValue("ordene_id")!=null){objOrden.setIdOrden(Long.parseLong(ordenDao.getDataSet().getValue("ordene_id")));}
					if(ordenDao.getDataSet().getValue("uniinv_id")!=null){objOrden.setIdUnidadInversion(Integer.parseInt(ordenDao.getDataSet().getValue("uniinv_id")));}	
					if(ordenDao.getDataSet().getValue("client_id")!=null){objOrden.setIdCliente(Long.parseLong(ordenDao.getDataSet().getValue("client_id")));}
					if(ordenDao.getDataSet().getValue("ordsta_id")!=null){objOrden.setStatus(ordenDao.getDataSet().getValue("ordsta_id"));}
					if(ordenDao.getDataSet().getValue("sistema_id")!=null){objOrden.setIdSistema(Integer.parseInt(ordenDao.getDataSet().getValue("sistema_id")));}
					if(ordenDao.getDataSet().getValue("empres_id")!=null){objOrden.setIdEmpresa(ordenDao.getDataSet().getValue("empres_id"));}
					if(ordenDao.getDataSet().getValue("contraparte")!=null){objOrden.setContraparte(ordenDao.getDataSet().getValue("contraparte"));}
					if(ordenDao.getDataSet().getValue("transa_id")!=null){objOrden.setIdTransaccion(ordenDao.getDataSet().getValue("transa_id"));}
					if(ordenDao.getDataSet().getValue("enviado")!=null){objOrden.setEnviado(Boolean.parseBoolean(ordenDao.getDataSet().getValue("enviado")));}
					if(ordenDao.getDataSet().getValue("ordene_cte_seg_bco")!=null){objOrden.setSegmentoBanco(ordenDao.getDataSet().getValue("ordene_cte_seg_bco"));}
					if(ordenDao.getDataSet().getValue("ordene_cte_seg_seg")!=null){objOrden.setSegmentoSegmento(ordenDao.getDataSet().getValue("ordene_cte_seg_seg"));}
					if(ordenDao.getDataSet().getValue("ordene_cte_seg_sub")!=null){objOrden.setSegmentoSubSegmento(ordenDao.getDataSet().getValue("ordene_cte_seg_sub"));}
					if(ordenDao.getDataSet().getValue("ordene_cte_seg_infi")!=null){objOrden.setSegmentoInfi(ordenDao.getDataSet().getValue("ordene_cte_seg_infi"));}
					if(ordenDao.getDataSet().getValue("ordene_ped_fe_orden")!=null){objOrden.setFechaOrden(formato.parse(ordenDao.getDataSet().getValue("fecha_orden")));}
					if(ordenDao.getDataSet().getValue("ordene_ped_fe_valor")!=null){objOrden.setFechaValor(formato.parse(ordenDao.getDataSet().getValue("fecha_valor_orden")));}
					if(ordenDao.getDataSet().getValue("ordene_ped_monto")!=null){objOrden.setMonto(Double.parseDouble(ordenDao.getDataSet().getValue("ordene_ped_monto")));}
					if(ordenDao.getDataSet().getValue("ordene_ped_total_pend")!=null){objOrden.setMontoPendiente(Double.parseDouble(ordenDao.getDataSet().getValue("ordene_ped_total_pend")));}
					if(ordenDao.getDataSet().getValue("ordene_ped_total")!=null){objOrden.setMontoTotal(Double.parseDouble(ordenDao.getDataSet().getValue("ordene_ped_total")));}
					if(ordenDao.getDataSet().getValue("ordene_ped_int_caidos")!=null){objOrden.setMontoInteresCaidos(Double.parseDouble(ordenDao.getDataSet().getValue("ordene_ped_int_caidos")));}
					if(ordenDao.getDataSet().getValue("bloter_id")!=null){objOrden.setIdBloter(ordenDao.getDataSet().getValue("bloter_id"));}
					//if(ordenDao.getDataSet().getValue("ordene_ped_cta_bsnro")!=null){objOrden.set;}
					if(ordenDao.getDataSet().getValue("ordene_ped_cta_bstip")!=null){objOrden.setTipoCuenta(ordenDao.getDataSet().getValue("ordene_ped_cta_bstip"));}
					if(ordenDao.getDataSet().getValue("ctecta_numero")!=null){objOrden.setCuentaCliente(ordenDao.getDataSet().getValue("ctecta_numero"));}
					if(ordenDao.getDataSet().getValue("ordene_ped_precio")!=null){objOrden.setPrecioCompra(Double.parseDouble(ordenDao.getDataSet().getValue("ordene_ped_precio")));}
					if(ordenDao.getDataSet().getValue("ordene_ped_rendimiento")!=null){objOrden.setRendimiento(Double.parseDouble(ordenDao.getDataSet().getValue("ordene_ped_rendimiento")));}
					if(ordenDao.getDataSet().getValue("ordene_ped_in_bdv")!=null){objOrden.setCarteraPropia(Boolean.parseBoolean(ordenDao.getDataSet().getValue("ordene_ped_in_bdv")));}
					if(ordenDao.getDataSet().getValue("moneda_id")!=null){objOrden.setIdMoneda(ordenDao.getDataSet().getValue("moneda_id"));}
					if(ordenDao.getDataSet().getValue("ordene_ped_rcp_precio")!=null){objOrden.setPrecioRecompra(Double.parseDouble(ordenDao.getDataSet().getValue("ordene_ped_rcp_precio")));}
					if(ordenDao.getDataSet().getValue("ordene_adj_monto")!=null){objOrden.setMontoAdjudicado(Double.parseDouble(ordenDao.getDataSet().getValue("ordene_adj_monto")));}
					if(ordenDao.getDataSet().getValue("ordene_usr_nombre")!=null){objOrden.setNombreUsuario(ordenDao.getDataSet().getValue("ordene_usr_nombre"));}
					if(ordenDao.getDataSet().getValue("ordene_usr_cen_contable")!=null){objOrden.setCentroContable(ordenDao.getDataSet().getValue("ordene_usr_cen_contable"));}
					if(ordenDao.getDataSet().getValue("ordene_usr_sucursal")!=null){objOrden.setSucursal(ordenDao.getDataSet().getValue("ordene_usr_sucursal"));}
					if(ordenDao.getDataSet().getValue("ordene_usr_terminal")!=null){objOrden.setTerminal(ordenDao.getDataSet().getValue("ordene_usr_terminal"));}
					if(ordenDao.getDataSet().getValue("ordene_veh_tom")!=null){objOrden.setVehiculoTomador(ordenDao.getDataSet().getValue("ordene_veh_tom"));}
					if(ordenDao.getDataSet().getValue("ordene_veh_col")!=null){objOrden.setVehiculoColocador(ordenDao.getDataSet().getValue("ordene_veh_col"));}
					if(ordenDao.getDataSet().getValue("ordene_veh_rec")!=null){objOrden.setVehiculoRecompra(ordenDao.getDataSet().getValue("ordene_veh_rec"));}
					if(ordenDao.getDataSet().getValue("ordene_ejec_id")!=null){objOrden.setIdEjecucion(Long.parseLong(ordenDao.getDataSet().getValue("ordene_veh_col")));}
					if(ordenDao.getDataSet().getValue("fecha_recompra")!=null){objOrden.setFechaPactoRecompra(formato.parse(ordenDao.getDataSet().getValue("fecha_recompra")));}
					if(ordenDao.getDataSet().getValue("ordene_ped_comisiones")!=null){objOrden.setMontoComisionOrden(new BigDecimal(ordenDao.getDataSet().getValue("ordene_ped_comisiones")));}
					if(ordenDao.getDataSet().getValue("ordene_fecha_adjudicacion")!=null){objOrden.setFechaAdjudicacion(formato.parse(ordenDao.getDataSet().getValue("ordene_fecha_adjudicacion")));}
					if(ordenDao.getDataSet().getValue("ordene_fecha_liquidacion")!=null){objOrden.setFechaLiquidacion(formato.parse(ordenDao.getDataSet().getValue("ordene_fecha_liquidacion")));}
					if(ordenDao.getDataSet().getValue("ordene_fecha_custodia")!=null){objOrden.setFechaCustodia(formato.parse(ordenDao.getDataSet().getValue("ordene_fecha_custodia")));}
					if(ordenDao.getDataSet().getValue("ordene_financiado")!=null){objOrden.setFinanciada(Boolean.parseBoolean(ordenDao.getDataSet().getValue("ordene_financiado")));}
					//if(ordenDao.getDataSet().getValue("id_opics")!=null){objOrden.set(ordenDao.getDataSet().getValue("id_opics"));}			
					//if(ordenDao.getDataSet().getValue("ejecucion_id")!=null){objOrden.set(ordenDao.getDataSet().getValue("ejecucion_id"));}
					if(ordenDao.getDataSet().getValue("ordene_comision_emisor")!=null){objOrden.setComisionEmisor(new BigDecimal(ordenDao.getDataSet().getValue("ordene_comision_emisor")));}
					if(ordenDao.getDataSet().getValue("ordene_tasa_pool")!=null){objOrden.setTasaPool(new BigDecimal(ordenDao.getDataSet().getValue("ordene_tasa_pool")));}
					if(ordenDao.getDataSet().getValue("ordene_ganancia_red")!=null){objOrden.setGananciaRed(new BigDecimal(ordenDao.getDataSet().getValue("ordene_ganancia_red")));}
					if(ordenDao.getDataSet().getValue("ordene_comision_oficina")!=null){objOrden.setComisionOficina(new BigDecimal(ordenDao.getDataSet().getValue("ordene_comision_oficina")));}
					if(ordenDao.getDataSet().getValue("ordene_comision_operacion")!=null){objOrden.setComisionOperacion(new BigDecimal(ordenDao.getDataSet().getValue("ordene_comision_operacion")));}
					if(ordenDao.getDataSet().getValue("tipper_id")!=null){objOrden.setIdTipoPersona(ordenDao.getDataSet().getValue("tipper_id"));}
					if(ordenDao.getDataSet().getValue("tipo_producto_id")!=null){objOrden.setTipoProducto(ordenDao.getDataSet().getValue("tipo_producto_id"));}
					if(ordenDao.getDataSet().getValue("concepto_id")!=null){objOrden.setConceptoId(ordenDao.getDataSet().getValue("concepto_id"));}
					if(ordenDao.getDataSet().getValue("sector_id")!=null){objOrden.setSectorProductivoId(ordenDao.getDataSet().getValue("sector_id"));}					
					if(ordenDao.getDataSet().getValue("cod_actividad_ec")!=null){objOrden.setActividadEconomicaId(ordenDao.getDataSet().getValue("cod_actividad_ec"));}
				}
				
				//Buscar la Data Extendida asociada
				
				listarRegistrosDataExtendida(idOrden);//return dataSet
				if(dataSet.count()>0){
					dataSet.first();
					ArrayList dataExtendidArr=new ArrayList();
					while(dataSet.next()){
						OrdenDataExt ordenDataExt=new OrdenDataExt();
						ordenDataExt.setIdData(dataSet.getValue("dtaext_id"));
						ordenDataExt.setIdOrden(Long.parseLong(dataSet.getValue("ordene_id")));
						ordenDataExt.setValor(dataSet.getValue("dtaext_valor"));
						dataExtendidArr.add(ordenDataExt);
						objOrden.agregarOrdenDataExt(ordenDataExt);//se agregan la data extendida a la orden
					}
				}
				
			} catch (Exception e) {				
				logger.error(e.getMessage()+ Utilitario.stackTraceException(e));
				throw new Exception ("Error obteniendo los datos de la orden guardada con el n&uacute;mero "+ idOrden);
			}
		
		return objOrden;
	}
	
	
	
	
	
	
	
	
	
	/**Busca la orden por el id de orden
	 * Arma el objeto completo para que pueda ser modificado cualquier valor
	 * */
	@SuppressWarnings("unchecked")
	public Orden listarOrden(long idOrden, String... idsCruce) throws Exception{
		
		Orden objOrden				=new Orden();
		OrdenDAO ordenDao			=new OrdenDAO(dataSource);
		SimpleDateFormat formato	=new SimpleDateFormat("dd-MM-yyyy");
		
			try {
				//Busca los campos dinamicos asociados a la orden
				CamposDinamicos camposDinamicos=new CamposDinamicos(dataSource);
				DataSet _campos		=camposDinamicos.listarCamposDinamicosOrdenes(idOrden,0);	
				if(_campos.count()>0){
					_campos.first();
					ArrayList camposDinamicosArr=new ArrayList();			
					while(_campos.next()){
						com.bdv.infi.data.CampoDinamico campoDin= new com.bdv.infi.data.CampoDinamico();
						campoDin.setIdCampo(Integer.parseInt(_campos.getValue("campo_id")));
						campoDin.setValor(_campos.getValue("campo_valor"));
						camposDinamicosArr.add(campoDin);
					}
					objOrden.setCamposDinamicos(camposDinamicosArr);//Se agregan los campos dinamicos a la orden
				}
				
				//Buscar los titulos asociados
				TitulosDAO titulo=new TitulosDAO(dataSource);
				titulo.listarDatosTituloOrden(String.valueOf(idOrden));
				if(titulo.getDataSet().count()>0){
					titulo.getDataSet().first();
					ArrayList titulosArr=new ArrayList();
					while(titulo.getDataSet().next()){
						OrdenTitulo tituloOrden=new OrdenTitulo();
						tituloOrden.setTituloId(titulo.getDataSet().getValue("titulo_id"));
						tituloOrden.setMonto(Double.parseDouble(titulo.getDataSet().getValue("titulo_monto")));
						tituloOrden.setPorcentaje(titulo.getDataSet().getValue("titulo_pct"));
						tituloOrden.setUnidades(Double.parseDouble(titulo.getDataSet().getValue("titulo_unidades")));
						tituloOrden.setMontoIntCaidos(new BigDecimal(titulo.getDataSet().getValue("titulo_mto_int_caidos")));
						tituloOrden.setPorcentajeRecompra(Double.parseDouble(titulo.getDataSet().getValue("titulo_pct_recompra")));
						tituloOrden.setPrecioMercado(Double.parseDouble(titulo.getDataSet().getValue("titulo_precio_mercado")));
						titulosArr.add(tituloOrden);
					}
					objOrden.agregarOrdenTitulo(titulosArr);//se agregan los titulos a la orden
				}
				//Documentos asociados a la orden
				ordenDao.consultarDocumentosOrden(idOrden);
				if(ordenDao.getDataSet().count()>0){
					ordenDao.getDataSet().first();
					ArrayList documentosArr=new ArrayList();
					while(ordenDao.getDataSet().next()){
						OrdenDocumento doc = new OrdenDocumento();
						doc.setIdDocumento(Long.parseLong(ordenDao.getDataSet().getValue("ordene_doc_id")));
						doc.setIdOrden(idOrden);
						doc.setNombre(ordenDao.getDataSet().getValue("nombre"));
						documentosArr.add(doc);
					}
					objOrden.setDocumentos(documentosArr);// Agregan los documentos asociados a la orden
				}
				//
				//buscar las operaciones aplicadas a la orden
				ordenDao.listarDetallesOrdenOperacion(idOrden);
				if(ordenDao.getDataSet().count()>0){
					ordenDao.getDataSet().first();
					ArrayList operacionesArr=new ArrayList();
					while(ordenDao.getDataSet().next()){ 
						OrdenOperacion ordenOperacion=new OrdenOperacion();
						if(ordenDao.getDataSet().getValue("aplica_reverso")!=null){ordenOperacion.setAplicaReverso(Boolean.parseBoolean(ordenDao.getDataSet().getValue("aplica_reverso")));}
						if(ordenDao.getDataSet().getValue("moneda_id")!="null"){ordenOperacion.setIdMoneda(ordenDao.getDataSet().getValue("moneda_id"));}
						if(ordenDao.getDataSet().getValue("ordene_operacion_id")!=null){ordenOperacion.setIdOperacion(Long.parseLong(ordenDao.getDataSet().getValue("ordene_operacion_id")));}
						if(ordenDao.getDataSet().getValue("ordene_relac_operacion_id")!=null){ordenOperacion.setIdOperacionRelacion(Long.parseLong(ordenDao.getDataSet().getValue("ordene_relac_operacion_id")));}else ordenOperacion.setIdOperacionRelacion(0);
						if(ordenDao.getDataSet().getValue("codigo_operacion")!=null){ordenOperacion.setCodigoOperacion(ordenDao.getDataSet().getValue("codigo_operacion"));}
						if(ordenDao.getDataSet().getValue("trnfin_id")!=null){ordenOperacion.setIdTransaccionFinanciera(ordenDao.getDataSet().getValue("trnfin_id"));}
						if(ordenDao.getDataSet().getValue("status_operacion")!=null){ordenOperacion.setStatusOperacion(ordenDao.getDataSet().getValue("status_operacion"));}				
						if(ordenDao.getDataSet().getValue("monto_operacion")!=null){ordenOperacion.setMontoOperacion(new BigDecimal(Double.parseDouble(ordenDao.getDataSet().getValue("monto_operacion"))));}
						if(ordenDao.getDataSet().getValue("tasa")!=null){ordenOperacion.setTasa(new BigDecimal(ordenDao.getDataSet().getValue("tasa")));}			
						if(ordenDao.getDataSet().getValue("fecha_aplicar")!=null){ordenOperacion.setFechaProcesada(formato.parse(ordenDao.getDataSet().getValue("fecha_aplicar")));}			
						if(ordenDao.getDataSet().getValue("fecha_procesada")!=null){ordenOperacion.setFechaAplicar(formato.parse(ordenDao.getDataSet().getValue("fecha_procesada")));}
						if(ordenDao.getDataSet().getValue("OPERACION_NOMBRE")!=null){ordenOperacion.setNombreOperacion(ordenDao.getDataSet().getValue("OPERACION_NOMBRE"));}
						if(ordenDao.getDataSet().getValue("ordene_operacion_error")!=null){ordenOperacion.setErrorNoAplicacion(ordenDao.getDataSet().getValue("ordene_operacion_error"));}
						//if(ordenDao.getDataSet().getValue("moneda_id")!=null){operacion.setError(ordenDao.getDataSet().getValue(""));}
						if(ordenDao.getDataSet().getValue("serial")!=null){ordenOperacion.setSerialContable(ordenDao.getDataSet().getValue("serial"));}
						if(ordenDao.getDataSet().getValue("in_comision")!=null){ordenOperacion.setInComision(Integer.parseInt(ordenDao.getDataSet().getValue("in_comision")));}
						if(ordenDao.getDataSet().getValue("ctecta_numero")!=null){ordenOperacion.setNumeroCuenta(ordenDao.getDataSet().getValue("ctecta_numero"));}
						if(ordenDao.getDataSet().getValue("numero_retencion")!=null){ordenOperacion.setNumeroRetencion(ordenDao.getDataSet().getValue("numero_retencion"));}
						if(ordenDao.getDataSet().getValue("ctecta_nombre")!=null){ordenOperacion.setNombreReferenciaCuenta(ordenDao.getDataSet().getValue("ctecta_nombre"));}
						if(ordenDao.getDataSet().getValue("ctecta_bcocta_bco")!=null){ordenOperacion.setNombreBanco(ordenDao.getDataSet().getValue("ctecta_bcocta_bco"));}
						if(ordenDao.getDataSet().getValue("ctecta_bcocta_direccion")!=null){ordenOperacion.setDireccionBanco(ordenDao.getDataSet().getValue("ctecta_bcocta_direccion"));}
						if(ordenDao.getDataSet().getValue("ctecta_bcocta_swift")!=null){ordenOperacion.setCodigoSwiftBanco(ordenDao.getDataSet().getValue("ctecta_bcocta_swift"));}
						if(ordenDao.getDataSet().getValue("ctecta_bcocta_telefono")!=null){ordenOperacion.setTelefonoBanco(ordenDao.getDataSet().getValue("ctecta_bcocta_telefono"));}
						if(ordenDao.getDataSet().getValue("ctecta_bcocta_aba")!=null){ordenOperacion.setCodigoABA(ordenDao.getDataSet().getValue("ctecta_bcocta_aba"));}
						if(ordenDao.getDataSet().getValue("ctecta_bcocta_pais")!=null){ordenOperacion.setPaisBancoCuenta(ordenDao.getDataSet().getValue("ctecta_bcocta_pais"));}
						if(ordenDao.getDataSet().getValue("ctecta_bcoint_bco")!=null){ordenOperacion.setNombreBancoIntermediario(ordenDao.getDataSet().getValue("ctecta_bcoint_bco"));}
						if(ordenDao.getDataSet().getValue("ctecta_bcoint_direccion")!=null){ordenOperacion.setDireccionBancoIntermediario(ordenDao.getDataSet().getValue("ctecta_bcoint_direccion"));}
						if(ordenDao.getDataSet().getValue("ctecta_bcoint_swift")!=null){ordenOperacion.setCodigoSwiftBancoIntermediario(ordenDao.getDataSet().getValue("ctecta_bcoint_swift"));}
						if(ordenDao.getDataSet().getValue("ctecta_bcoint_bic")!=null){ordenOperacion.setCodigoBicBancoIntermediario(ordenDao.getDataSet().getValue("ctecta_bcoint_bic"));}
						if(ordenDao.getDataSet().getValue("ctecta_bcoint_telefono")!=null){ordenOperacion.setTelefonoBancoIntermediario(ordenDao.getDataSet().getValue("ctecta_bcoint_telefono"));}
						if(ordenDao.getDataSet().getValue("ctecta_bcoint_aba")!=null){ordenOperacion.setCodigoABAIntermediario(ordenDao.getDataSet().getValue("ctecta_bcoint_aba"));}
						if(ordenDao.getDataSet().getValue("ctecta_bcoint_pais")!=null){ordenOperacion.setPaisBancoIntermediario(ordenDao.getDataSet().getValue("ctecta_bcoint_pais"));}
						if(ordenDao.getDataSet().getValue("trnf_tipo")!=null){ordenOperacion.setTipoTransaccionFinanc(ordenDao.getDataSet().getValue("trnf_tipo"));}

						operacionesArr.add(ordenOperacion);
					}
					objOrden.setOperacion(operacionesArr);// Se agregan las operaciones asociadas a la orden
				}
				ordenDao.listarDetallesOrden(idOrden);
				//Setear los valores de la orden
				if(ordenDao.getDataSet().count()>0){
					ordenDao.getDataSet().first();
					ordenDao.getDataSet().next();
					if(ordenDao.getDataSet().getValue("ordene_id")!=null){objOrden.setIdOrden(Long.parseLong(ordenDao.getDataSet().getValue("ordene_id")));}
					if(ordenDao.getDataSet().getValue("uniinv_id")!=null){objOrden.setIdUnidadInversion(Integer.parseInt(ordenDao.getDataSet().getValue("uniinv_id")));}	
					if(ordenDao.getDataSet().getValue("client_id")!=null){objOrden.setIdCliente(Long.parseLong(ordenDao.getDataSet().getValue("client_id")));}
					if(ordenDao.getDataSet().getValue("ordsta_id")!=null){objOrden.setStatus(ordenDao.getDataSet().getValue("ordsta_id"));}
					if(ordenDao.getDataSet().getValue("sistema_id")!=null){objOrden.setIdSistema(Integer.parseInt(ordenDao.getDataSet().getValue("sistema_id")));}
					if(ordenDao.getDataSet().getValue("empres_id")!=null){objOrden.setIdEmpresa(ordenDao.getDataSet().getValue("empres_id"));}
					if(ordenDao.getDataSet().getValue("contraparte")!=null){objOrden.setContraparte(ordenDao.getDataSet().getValue("contraparte"));}
					if(ordenDao.getDataSet().getValue("transa_id")!=null){objOrden.setIdTransaccion(ordenDao.getDataSet().getValue("transa_id"));}
					if(ordenDao.getDataSet().getValue("enviado")!=null){objOrden.setEnviado(Boolean.parseBoolean(ordenDao.getDataSet().getValue("enviado")));}
					if(ordenDao.getDataSet().getValue("ordene_cte_seg_bco")!=null){objOrden.setSegmentoBanco(ordenDao.getDataSet().getValue("ordene_cte_seg_bco"));}
					if(ordenDao.getDataSet().getValue("ordene_cte_seg_seg")!=null){objOrden.setSegmentoSegmento(ordenDao.getDataSet().getValue("ordene_cte_seg_seg"));}
					if(ordenDao.getDataSet().getValue("ordene_cte_seg_sub")!=null){objOrden.setSegmentoSubSegmento(ordenDao.getDataSet().getValue("ordene_cte_seg_sub"));}
					if(ordenDao.getDataSet().getValue("ordene_cte_seg_infi")!=null){objOrden.setSegmentoInfi(ordenDao.getDataSet().getValue("ordene_cte_seg_infi"));}
					if(ordenDao.getDataSet().getValue("ordene_ped_fe_orden")!=null){objOrden.setFechaOrden(formato.parse(ordenDao.getDataSet().getValue("fecha_orden")));}
					if(ordenDao.getDataSet().getValue("ordene_ped_fe_valor")!=null){objOrden.setFechaValor(formato.parse(ordenDao.getDataSet().getValue("fecha_valor_orden")));}
					if(ordenDao.getDataSet().getValue("ordene_ped_monto")!=null){objOrden.setMonto(Double.parseDouble(ordenDao.getDataSet().getValue("ordene_ped_monto")));}
					if(ordenDao.getDataSet().getValue("ordene_ped_total_pend")!=null){objOrden.setMontoPendiente(Double.parseDouble(ordenDao.getDataSet().getValue("ordene_ped_total_pend")));}
					if(ordenDao.getDataSet().getValue("ordene_ped_total")!=null){objOrden.setMontoTotal(Double.parseDouble(ordenDao.getDataSet().getValue("ordene_ped_total")));}
					if(ordenDao.getDataSet().getValue("ordene_ped_int_caidos")!=null){objOrden.setMontoInteresCaidos(Double.parseDouble(ordenDao.getDataSet().getValue("ordene_ped_int_caidos")));}
					if(ordenDao.getDataSet().getValue("bloter_id")!=null){objOrden.setIdBloter(ordenDao.getDataSet().getValue("bloter_id"));}
					//if(ordenDao.getDataSet().getValue("ordene_ped_cta_bsnro")!=null){objOrden.set;}
					if(ordenDao.getDataSet().getValue("ordene_ped_cta_bstip")!=null){objOrden.setTipoCuenta(ordenDao.getDataSet().getValue("ordene_ped_cta_bstip"));}
					if(ordenDao.getDataSet().getValue("ctecta_numero")!=null){objOrden.setCuentaCliente(ordenDao.getDataSet().getValue("ctecta_numero"));}
					if(ordenDao.getDataSet().getValue("ordene_ped_precio")!=null){objOrden.setPrecioCompra(Double.parseDouble(ordenDao.getDataSet().getValue("ordene_ped_precio")));}
					if(ordenDao.getDataSet().getValue("ordene_ped_rendimiento")!=null){objOrden.setRendimiento(Double.parseDouble(ordenDao.getDataSet().getValue("ordene_ped_rendimiento")));}
					if(ordenDao.getDataSet().getValue("ordene_ped_in_bdv")!=null){objOrden.setCarteraPropia(Boolean.parseBoolean(ordenDao.getDataSet().getValue("ordene_ped_in_bdv")));}
					if(ordenDao.getDataSet().getValue("moneda_id")!=null){objOrden.setIdMoneda(ordenDao.getDataSet().getValue("moneda_id"));}
					if(ordenDao.getDataSet().getValue("ordene_ped_rcp_precio")!=null){objOrden.setPrecioRecompra(Double.parseDouble(ordenDao.getDataSet().getValue("ordene_ped_rcp_precio")));}
					if(ordenDao.getDataSet().getValue("ordene_adj_monto")!=null){objOrden.setMontoAdjudicado(Double.parseDouble(ordenDao.getDataSet().getValue("ordene_adj_monto")));}
					if(ordenDao.getDataSet().getValue("ordene_usr_nombre")!=null){objOrden.setNombreUsuario(ordenDao.getDataSet().getValue("ordene_usr_nombre"));}
					if(ordenDao.getDataSet().getValue("ordene_usr_cen_contable")!=null){objOrden.setCentroContable(ordenDao.getDataSet().getValue("ordene_usr_cen_contable"));}
					if(ordenDao.getDataSet().getValue("ordene_usr_sucursal")!=null){objOrden.setSucursal(ordenDao.getDataSet().getValue("ordene_usr_sucursal"));}
					if(ordenDao.getDataSet().getValue("ordene_usr_terminal")!=null){objOrden.setTerminal(ordenDao.getDataSet().getValue("ordene_usr_terminal"));}
					if(ordenDao.getDataSet().getValue("ordene_veh_tom")!=null){objOrden.setVehiculoTomador(ordenDao.getDataSet().getValue("ordene_veh_tom"));}
					if(ordenDao.getDataSet().getValue("ordene_veh_col")!=null){objOrden.setVehiculoColocador(ordenDao.getDataSet().getValue("ordene_veh_col"));}
					if(ordenDao.getDataSet().getValue("ordene_veh_rec")!=null){objOrden.setVehiculoRecompra(ordenDao.getDataSet().getValue("ordene_veh_rec"));}
					if(ordenDao.getDataSet().getValue("ordene_ejec_id")!=null){objOrden.setIdEjecucion(Long.parseLong(ordenDao.getDataSet().getValue("ordene_veh_col")));}
					if(ordenDao.getDataSet().getValue("fecha_recompra")!=null){objOrden.setFechaPactoRecompra(formato.parse(ordenDao.getDataSet().getValue("fecha_recompra")));}
					if(ordenDao.getDataSet().getValue("ordene_ped_comisiones")!=null){objOrden.setMontoComisionOrden(new BigDecimal(ordenDao.getDataSet().getValue("ordene_ped_comisiones")));}
					if(ordenDao.getDataSet().getValue("ordene_fecha_adjudicacion")!=null){objOrden.setFechaAdjudicacion(formato.parse(ordenDao.getDataSet().getValue("ordene_fecha_adjudicacion")));}
					if(ordenDao.getDataSet().getValue("ordene_fecha_liquidacion")!=null){objOrden.setFechaLiquidacion(formato.parse(ordenDao.getDataSet().getValue("ordene_fecha_liquidacion")));}
					if(ordenDao.getDataSet().getValue("ordene_fecha_custodia")!=null){objOrden.setFechaCustodia(formato.parse(ordenDao.getDataSet().getValue("ordene_fecha_custodia")));}
					if(ordenDao.getDataSet().getValue("ordene_financiado")!=null){objOrden.setFinanciada(Boolean.parseBoolean(ordenDao.getDataSet().getValue("ordene_financiado")));}
					//if(ordenDao.getDataSet().getValue("id_opics")!=null){objOrden.set(ordenDao.getDataSet().getValue("id_opics"));}			
					//if(ordenDao.getDataSet().getValue("ejecucion_id")!=null){objOrden.set(ordenDao.getDataSet().getValue("ejecucion_id"));}
					if(ordenDao.getDataSet().getValue("ordene_comision_emisor")!=null){objOrden.setComisionEmisor(new BigDecimal(ordenDao.getDataSet().getValue("ordene_comision_emisor")));}
					if(ordenDao.getDataSet().getValue("ordene_tasa_pool")!=null){objOrden.setTasaPool(new BigDecimal(ordenDao.getDataSet().getValue("ordene_tasa_pool")));}
					if(ordenDao.getDataSet().getValue("ordene_ganancia_red")!=null){objOrden.setGananciaRed(new BigDecimal(ordenDao.getDataSet().getValue("ordene_ganancia_red")));}
					if(ordenDao.getDataSet().getValue("ordene_comision_oficina")!=null){objOrden.setComisionOficina(new BigDecimal(ordenDao.getDataSet().getValue("ordene_comision_oficina")));}
					if(ordenDao.getDataSet().getValue("ordene_comision_operacion")!=null){objOrden.setComisionOperacion(new BigDecimal(ordenDao.getDataSet().getValue("ordene_comision_operacion")));}
					if(ordenDao.getDataSet().getValue("tipper_id")!=null){objOrden.setIdTipoPersona(ordenDao.getDataSet().getValue("tipper_id"));}
					if(ordenDao.getDataSet().getValue("tipo_producto_id")!=null){objOrden.setTipoProducto(ordenDao.getDataSet().getValue("tipo_producto_id"));}
					if(ordenDao.getDataSet().getValue("concepto_id")!=null){objOrden.setConceptoId(ordenDao.getDataSet().getValue("concepto_id"));}
					if(ordenDao.getDataSet().getValue("sector_id")!=null){objOrden.setSectorProductivoId(ordenDao.getDataSet().getValue("sector_id"));}					
					if(ordenDao.getDataSet().getValue("cod_actividad_ec")!=null){objOrden.setActividadEconomicaId(ordenDao.getDataSet().getValue("cod_actividad_ec"));}
					//NM29643 - INFI_TTS_466
					if(ordenDao.getDataSet().getValue("dias_interes_transcurridos")!=null){objOrden.setDiasIntCaidos(ordenDao.getDataSet().getValue("dias_interes_transcurridos"));}
				}
				
				//Buscar la Data Extendida asociada
				listarRegistrosDataExtendida(idOrden);//return dataSet
				if(dataSet.count()>0){
					dataSet.first();
					ArrayList dataExtendidArr=new ArrayList();
					while(dataSet.next()){
						OrdenDataExt ordenDataExt=new OrdenDataExt();
						ordenDataExt.setIdData(dataSet.getValue("dtaext_id"));
						ordenDataExt.setIdOrden(Long.parseLong(dataSet.getValue("ordene_id")));
						ordenDataExt.setValor(dataSet.getValue("dtaext_valor"));
						dataExtendidArr.add(ordenDataExt);
						objOrden.agregarOrdenDataExt(ordenDataExt);//se agregan la data extendida a la orden
					}
				}
				
				//NM29643 - infi_TTS_466 20/07/2014
				//Busca los cruces tipo Titulo asociados a la orden
				OrdenesCrucesDAO ordCruceDAO = new OrdenesCrucesDAO(dataSource);
				
				if(idsCruce!=null && idsCruce.length>0 && idsCruce[0]!=null && !idsCruce[0].equals("")){
					ordCruceDAO.listarCrucesPorIdOrdenTipo(idOrden, String.valueOf(ConstantesGenerales.VERDADERO), idsCruce[0]);
				}else{
					ordCruceDAO.listarCrucesPorIdOrdenTipo(idOrden, String.valueOf(ConstantesGenerales.VERDADERO));
				}
				DataSet _ordCruces = ordCruceDAO.getDataSet();	
				if(_ordCruces.count()>0){
					_ordCruces.first();
					ArrayList ordCrucesArr = new ArrayList();			
					while(_ordCruces.next()){
						//Se crea un objeto Cruce por cada cruce titpo Titulo de la orden
						OrdenesCruce ordCruce = new OrdenesCruce();
						ordCruce.setIdCruce(Long.parseLong(_ordCruces.getValue("ID_CRUCE")==null?"0":_ordCruces.getValue("ID_CRUCE")));
						ordCruce.setIdOrdenINFI(Long.parseLong(_ordCruces.getValue("ORDENE_ID")==null?"0":_ordCruces.getValue("ORDENE_ID")));
						ordCruce.setIdOrdenInfiString(_ordCruces.getValue("ORDENE_ID")==null?"":_ordCruces.getValue("ORDENE_ID"));
						ordCruce.setIdUI(Long.parseLong(_ordCruces.getValue("UNDINV_ID")==null?"0":_ordCruces.getValue("UNDINV_ID")));
						//ordCruce.setIdOrdenBCV(Long.parseLong(_ordCruces.getValue("ORDENE_ID_BCV")==null?"0":_ordCruces.getValue("ORDENE_ID_BCV")));
						ordCruce.setIdOrdenBcvString(_ordCruces.getValue("ORDENE_ID_BCV")==null?"":_ordCruces.getValue("ORDENE_ID_BCV"));
						ordCruce.setNroOperacion(Long.parseLong(_ordCruces.getValue("NRO_OPERACION")==null?"0":_ordCruces.getValue("NRO_OPERACION")));
						ordCruce.setNroOperacionString(_ordCruces.getValue("NRO_OPERACION")==null?"":_ordCruces.getValue("NRO_OPERACION"));
						ordCruce.setMontoOperacion(new BigDecimal(_ordCruces.getValue("MONTO_OPERACION")==null?"0":_ordCruces.getValue("MONTO_OPERACION")).setScale(2, BigDecimal.ROUND_HALF_EVEN));
						ordCruce.setMontoOperacionString(_ordCruces.getValue("MONTO_OPERACION")==null?"":_ordCruces.getValue("MONTO_OPERACION"));
						ordCruce.setTasa(new BigDecimal(_ordCruces.getValue("TASA")==null?"0":_ordCruces.getValue("TASA")).setScale(2, BigDecimal.ROUND_HALF_EVEN));
						ordCruce.setTasaString(_ordCruces.getValue("TASA")==null?"":_ordCruces.getValue("TASA"));
						ordCruce.setPrecioTitulo(new BigDecimal(_ordCruces.getValue("PRECIO_TITULO")==null?"0":_ordCruces.getValue("PRECIO_TITULO")).setScale(2, BigDecimal.ROUND_HALF_EVEN));
						ordCruce.setPrecioTituloString(_ordCruces.getValue("PRECIO_TITULO")==null?"0":_ordCruces.getValue("PRECIO_TITULO"));
						ordCruce.setValorNominal(Double.parseDouble(_ordCruces.getValue("VALOR_NOMINAL")==null?"0":_ordCruces.getValue("VALOR_NOMINAL")));
						ordCruce.setIsinString(_ordCruces.getValue("ISIN")==null?"":_ordCruces.getValue("ISIN"));
						ordCruce.setContraparte(_ordCruces.getValue("CONTRAPARTE")==null?"":_ordCruces.getValue("CONTRAPARTE"));
						ordCruce.setEstatus(_ordCruces.getValue("ESTATUS")==null?"":_ordCruces.getValue("ESTATUS"));
						ordCruce.setObservacion(_ordCruces.getValue("OBSERVACION")==null?"":_ordCruces.getValue("OBSERVACION"));
						ordCruce.setIdEjecucion(Long.parseLong(_ordCruces.getValue("ID_EJECUCION")==null?"0":_ordCruces.getValue("ID_EJECUCION")));
						ordCruce.setIndicadorTitulo(Integer.parseInt(_ordCruces.getValue("INDICADOR_TITULO")==null?"0":_ordCruces.getValue("INDICADOR_TITULO")));
						ordCruce.setIdCliente(Long.parseLong(_ordCruces.getValue("CLIENT_ID")==null?"0":_ordCruces.getValue("CLIENT_ID")));
						ordCruce.setIdTitulo(_ordCruces.getValue("TITULO_ID")==null?"":_ordCruces.getValue("TITULO_ID"));
						ordCruce.setMtoInteresesCaidosTitulo(new BigDecimal(_ordCruces.getValue("TITULO_MTO_INT_CAIDOS")==null?"0":_ordCruces.getValue("TITULO_MTO_INT_CAIDOS")).setScale(2, BigDecimal.ROUND_HALF_EVEN));
						ordCruce.setFechaValor(_ordCruces.getValue("FECHA_VALOR")==null?"":_ordCruces.getValue("FECHA_VALOR"));
						ordCruce.setCruceProcesadoString(_ordCruces.getValue("CRUCE_PROCESADO")==null?"":_ordCruces.getValue("CRUCE_PROCESADO"));
						ordCruce.setLiquidacionProcesadaString(_ordCruces.getValue("LIQUIDACION_PROCESADA")==null?"":_ordCruces.getValue("LIQUIDACION_PROCESADA"));
						ordCruce.setContravalorBolivaresCapital(new BigDecimal(_ordCruces.getValue("CONTRAVALOR_BOLIVARES_CAPITAL")==null?"0":_ordCruces.getValue("CONTRAVALOR_BOLIVARES_CAPITAL")).setScale(2, BigDecimal.ROUND_HALF_EVEN));
						ordCruce.setIndTituloProcesado(_ordCruces.getValue("IND_TITULO_PROCESADO")==null?"":_ordCruces.getValue("IND_TITULO_PROCESADO"));
						
						ordCrucesArr.add(ordCruce);
					}
					objOrden.setOrdenesCrucesTitulos(ordCrucesArr);//Se agregan los campos dinamicos a la orden
				}
				
				ordCruceDAO.listarCrucesPorIdOrdenTipo(idOrden, String.valueOf(ConstantesGenerales.FALSO));
				_ordCruces = ordCruceDAO.getDataSet();	
				if(_ordCruces.count()>0){
					_ordCruces.first();
					ArrayList ordCrucesArr = new ArrayList();			
					while(_ordCruces.next()){
						//Se crea un objeto Cruce por cada cruce titpo Titulo de la orden
						OrdenesCruce ordCruce = new OrdenesCruce();
						ordCruce.setIdCruce(Long.parseLong(_ordCruces.getValue("ID_CRUCE")==null?"0":_ordCruces.getValue("ID_CRUCE")));
						ordCruce.setIdOrdenINFI(Long.parseLong(_ordCruces.getValue("ORDENE_ID")==null?"0":_ordCruces.getValue("ORDENE_ID")));
						ordCruce.setIdOrdenInfiString(_ordCruces.getValue("ORDENE_ID")==null?"":_ordCruces.getValue("ORDENE_ID"));
						ordCruce.setIdUI(Long.parseLong(_ordCruces.getValue("UNDINV_ID")==null?"0":_ordCruces.getValue("UNDINV_ID")));
						//ordCruce.setIdOrdenBCV(Long.parseLong(_ordCruces.getValue("ORDENE_ID_BCV")==null?"0":_ordCruces.getValue("ORDENE_ID_BCV")));
						ordCruce.setIdOrdenBcvString(_ordCruces.getValue("ORDENE_ID_BCV")==null?"":_ordCruces.getValue("ORDENE_ID_BCV"));
						ordCruce.setNroOperacion(Long.parseLong(_ordCruces.getValue("NRO_OPERACION")==null?"0":_ordCruces.getValue("NRO_OPERACION")));
						ordCruce.setNroOperacionString(_ordCruces.getValue("NRO_OPERACION")==null?"":_ordCruces.getValue("NRO_OPERACION"));
						ordCruce.setMontoOperacion(new BigDecimal(_ordCruces.getValue("MONTO_OPERACION")==null?"0":_ordCruces.getValue("MONTO_OPERACION")).setScale(2, BigDecimal.ROUND_HALF_EVEN));
						ordCruce.setMontoOperacionString(_ordCruces.getValue("MONTO_OPERACION")==null?"":_ordCruces.getValue("MONTO_OPERACION"));
						ordCruce.setTasa(new BigDecimal(_ordCruces.getValue("TASA")==null?"0":_ordCruces.getValue("TASA")).setScale(2, BigDecimal.ROUND_HALF_EVEN));
						ordCruce.setTasaString(_ordCruces.getValue("TASA")==null?"":_ordCruces.getValue("TASA"));
						ordCruce.setContraparte(_ordCruces.getValue("CONTRAPARTE")==null?"":_ordCruces.getValue("CONTRAPARTE"));
						ordCruce.setEstatus(_ordCruces.getValue("ESTATUS")==null?"":_ordCruces.getValue("ESTATUS"));
						ordCruce.setObservacion(_ordCruces.getValue("OBSERVACION")==null?"":_ordCruces.getValue("OBSERVACION"));
						ordCruce.setIdEjecucion(Long.parseLong(_ordCruces.getValue("ID_EJECUCION")==null?"0":_ordCruces.getValue("ID_EJECUCION")));
						ordCruce.setIndicadorTitulo(Integer.parseInt(_ordCruces.getValue("INDICADOR_TITULO")==null?"0":_ordCruces.getValue("INDICADOR_TITULO")));
						ordCruce.setIdCliente(Long.parseLong(_ordCruces.getValue("CLIENT_ID")==null?"0":_ordCruces.getValue("CLIENT_ID")));
						ordCruce.setFechaValor(_ordCruces.getValue("FECHA_VALOR")==null?"":_ordCruces.getValue("FECHA_VALOR"));
						ordCruce.setCruceProcesadoString(_ordCruces.getValue("CRUCE_PROCESADO")==null?"":_ordCruces.getValue("CRUCE_PROCESADO"));
						ordCruce.setLiquidacionProcesadaString(_ordCruces.getValue("LIQUIDACION_PROCESADA")==null?"":_ordCruces.getValue("LIQUIDACION_PROCESADA"));
						ordCruce.setContravalorBolivaresCapital(new BigDecimal(_ordCruces.getValue("CONTRAVALOR_BOLIVARES_CAPITAL")==null?"0":_ordCruces.getValue("CONTRAVALOR_BOLIVARES_CAPITAL")).setScale(2, BigDecimal.ROUND_HALF_EVEN));
						
						ordCrucesArr.add(ordCruce);
					}
					objOrden.setOrdenesCrucesDivisas(ordCrucesArr);//Se agregan los campos dinamicos a la orden
				}
				
			}catch (Exception e) {				
				logger.error(e.getMessage()+ Utilitario.stackTraceException(e));
				throw new Exception ("Error obteniendo los datos de la orden guardada con el n&uacute;mero "+ idOrden);
			}
		
		return objOrden;
	}
	
	/**Busca la orden por el id de orden y los datos asociados a ella seg&uacute;n los par&aacute;metros necesarios
	 * Arma el objeto completo para que pueda ser modificado cualquier valor
	 * *///TTS-491 Modificacion para notificacion BCV via Web Service NM26659 03/03/2015
	@SuppressWarnings("unchecked")
	public Orden listarOrden(long idOrden, boolean camDinamicos, boolean titulos, boolean documentos, boolean operaciones, boolean dataExtendida) throws Exception{
		
		Orden objOrden				=new Orden();
		OrdenDAO ordenDao			=new OrdenDAO(dataSource);
		SimpleDateFormat formato	=new SimpleDateFormat("dd-MM-yyyy");
		
			try {

				//Busca los campos dinamicos asociados a la orden				
				if (camDinamicos){				
					CamposDinamicos camposDinamicos=new CamposDinamicos(dataSource);
					camposDinamicos.consultarCamposDinamicosOrden(idOrden);
					DataSet _campos	= camposDinamicos.getDataSet();		
					if(_campos.count()>0){
						_campos.first();
						ArrayList camposDinamicosArr=new ArrayList();			
						while(_campos.next()){
							com.bdv.infi.data.CampoDinamico campoDin= new com.bdv.infi.data.CampoDinamico();
							campoDin.setIdCampo(Integer.parseInt(_campos.getValue("campo_id")));
							campoDin.setValor(_campos.getValue("campo_valor"));
							camposDinamicosArr.add(campoDin);
						}
						objOrden.setCamposDinamicos(camposDinamicosArr);//Se agregan los campos dinamicos a la orden
					}
				}
				
				//Buscar los titulos asociados
				if (titulos){				
					TitulosDAO titulo=new TitulosDAO(dataSource);
					titulo.listarDatosTituloOrden(String.valueOf(idOrden));
					if(titulo.getDataSet().count()>0){
						titulo.getDataSet().first();
						ArrayList titulosArr=new ArrayList();
						while(titulo.getDataSet().next()){
							OrdenTitulo tituloOrden=new OrdenTitulo();
							tituloOrden.setTituloId(titulo.getDataSet().getValue("titulo_id"));
							tituloOrden.setMonto(Double.parseDouble(titulo.getDataSet().getValue("titulo_monto")));
							tituloOrden.setPorcentaje(titulo.getDataSet().getValue("titulo_pct"));
							tituloOrden.setUnidades(Double.parseDouble(titulo.getDataSet().getValue("titulo_unidades")));
							tituloOrden.setMontoIntCaidos(new BigDecimal(titulo.getDataSet().getValue("titulo_mto_int_caidos")));
							tituloOrden.setPorcentajeRecompra(Double.parseDouble(titulo.getDataSet().getValue("titulo_pct_recompra")));
							tituloOrden.setPrecioMercado(Double.parseDouble(titulo.getDataSet().getValue("titulo_precio_mercado")));
							titulosArr.add(tituloOrden);
						}
						objOrden.agregarOrdenTitulo(titulosArr);//se agregan los titulos a la orden
					}
				}
								
				//Documentos asociados a la orden
				if (documentos){
					ordenDao.consultarDocumentosOrden(idOrden);
					if(ordenDao.getDataSet().count()>0){
						ordenDao.getDataSet().first();
						ArrayList documentosArr=new ArrayList();
						while(ordenDao.getDataSet().next()){
							OrdenDocumento doc = new OrdenDocumento();
							doc.setIdDocumento(Long.parseLong(ordenDao.getDataSet().getValue("ordene_doc_id")));
							doc.setIdOrden(idOrden);
							doc.setNombre(ordenDao.getDataSet().getValue("nombre"));
							documentosArr.add(doc);
						}
						objOrden.setDocumentos(documentosArr);// Agregan los documentos asociados a la orden
					}
				}
				
				//
				//buscar las operaciones aplicadas a la orden
				if (operaciones){
					ordenDao.listarDetallesOrdenOperacion(idOrden);
					if(ordenDao.getDataSet().count()>0){
						ordenDao.getDataSet().first();
						ArrayList operacionesArr=new ArrayList();
						while(ordenDao.getDataSet().next()){ 
							OrdenOperacion ordenOperacion=new OrdenOperacion();
							if(ordenDao.getDataSet().getValue("aplica_reverso")!=null){ordenOperacion.setAplicaReverso(Boolean.parseBoolean(ordenDao.getDataSet().getValue("aplica_reverso")));}
							if(ordenDao.getDataSet().getValue("moneda_id")!="null"){ordenOperacion.setIdMoneda(ordenDao.getDataSet().getValue("moneda_id"));}
							if(ordenDao.getDataSet().getValue("ordene_operacion_id")!=null){ordenOperacion.setIdOperacion(Long.parseLong(ordenDao.getDataSet().getValue("ordene_operacion_id")));}
							if(ordenDao.getDataSet().getValue("ordene_relac_operacion_id")!=null){ordenOperacion.setIdOperacionRelacion(Long.parseLong(ordenDao.getDataSet().getValue("ordene_relac_operacion_id")));}else ordenOperacion.setIdOperacionRelacion(0);
							if(ordenDao.getDataSet().getValue("codigo_operacion")!=null){ordenOperacion.setCodigoOperacion(ordenDao.getDataSet().getValue("codigo_operacion"));}
							if(ordenDao.getDataSet().getValue("trnfin_id")!=null){ordenOperacion.setIdTransaccionFinanciera(ordenDao.getDataSet().getValue("trnfin_id"));}
							if(ordenDao.getDataSet().getValue("status_operacion")!=null){ordenOperacion.setStatusOperacion(ordenDao.getDataSet().getValue("status_operacion"));}				
							if(ordenDao.getDataSet().getValue("monto_operacion")!=null){ordenOperacion.setMontoOperacion(new BigDecimal(Double.parseDouble(ordenDao.getDataSet().getValue("monto_operacion"))));}
							if(ordenDao.getDataSet().getValue("tasa")!=null){ordenOperacion.setTasa(new BigDecimal(ordenDao.getDataSet().getValue("tasa")));}			
							if(ordenDao.getDataSet().getValue("fecha_aplicar")!=null){ordenOperacion.setFechaProcesada(formato.parse(ordenDao.getDataSet().getValue("fecha_aplicar")));}			
							if(ordenDao.getDataSet().getValue("fecha_procesada")!=null){ordenOperacion.setFechaAplicar(formato.parse(ordenDao.getDataSet().getValue("fecha_procesada")));}
							if(ordenDao.getDataSet().getValue("OPERACION_NOMBRE")!=null){ordenOperacion.setNombreOperacion(ordenDao.getDataSet().getValue("OPERACION_NOMBRE"));}
							if(ordenDao.getDataSet().getValue("ordene_operacion_error")!=null){ordenOperacion.setErrorNoAplicacion(ordenDao.getDataSet().getValue("ordene_operacion_error"));}
							//if(ordenDao.getDataSet().getValue("moneda_id")!=null){operacion.setError(ordenDao.getDataSet().getValue(""));}
							if(ordenDao.getDataSet().getValue("serial")!=null){ordenOperacion.setSerialContable(ordenDao.getDataSet().getValue("serial"));}
							if(ordenDao.getDataSet().getValue("in_comision")!=null){ordenOperacion.setInComision(Integer.parseInt(ordenDao.getDataSet().getValue("in_comision")));}
							if(ordenDao.getDataSet().getValue("ctecta_numero")!=null){ordenOperacion.setNumeroCuenta(ordenDao.getDataSet().getValue("ctecta_numero"));}
							if(ordenDao.getDataSet().getValue("numero_retencion")!=null){ordenOperacion.setNumeroRetencion(ordenDao.getDataSet().getValue("numero_retencion"));}
							if(ordenDao.getDataSet().getValue("ctecta_nombre")!=null){ordenOperacion.setNombreReferenciaCuenta(ordenDao.getDataSet().getValue("ctecta_nombre"));}
							if(ordenDao.getDataSet().getValue("ctecta_bcocta_bco")!=null){ordenOperacion.setNombreBanco(ordenDao.getDataSet().getValue("ctecta_bcocta_bco"));}
							if(ordenDao.getDataSet().getValue("ctecta_bcocta_direccion")!=null){ordenOperacion.setDireccionBanco(ordenDao.getDataSet().getValue("ctecta_bcocta_direccion"));}
							if(ordenDao.getDataSet().getValue("ctecta_bcocta_swift")!=null){ordenOperacion.setCodigoSwiftBanco(ordenDao.getDataSet().getValue("ctecta_bcocta_swift"));}
							if(ordenDao.getDataSet().getValue("ctecta_bcocta_telefono")!=null){ordenOperacion.setTelefonoBanco(ordenDao.getDataSet().getValue("ctecta_bcocta_telefono"));}
							if(ordenDao.getDataSet().getValue("ctecta_bcocta_aba")!=null){ordenOperacion.setCodigoABA(ordenDao.getDataSet().getValue("ctecta_bcocta_aba"));}
							if(ordenDao.getDataSet().getValue("ctecta_bcocta_pais")!=null){ordenOperacion.setPaisBancoCuenta(ordenDao.getDataSet().getValue("ctecta_bcocta_pais"));}
							if(ordenDao.getDataSet().getValue("ctecta_bcoint_bco")!=null){ordenOperacion.setNombreBancoIntermediario(ordenDao.getDataSet().getValue("ctecta_bcoint_bco"));}
							if(ordenDao.getDataSet().getValue("ctecta_bcoint_direccion")!=null){ordenOperacion.setDireccionBancoIntermediario(ordenDao.getDataSet().getValue("ctecta_bcoint_direccion"));}
							if(ordenDao.getDataSet().getValue("ctecta_bcoint_swift")!=null){ordenOperacion.setCodigoSwiftBancoIntermediario(ordenDao.getDataSet().getValue("ctecta_bcoint_swift"));}
							if(ordenDao.getDataSet().getValue("ctecta_bcoint_bic")!=null){ordenOperacion.setCodigoBicBancoIntermediario(ordenDao.getDataSet().getValue("ctecta_bcoint_bic"));}
							if(ordenDao.getDataSet().getValue("ctecta_bcoint_telefono")!=null){ordenOperacion.setTelefonoBancoIntermediario(ordenDao.getDataSet().getValue("ctecta_bcoint_telefono"));}
							if(ordenDao.getDataSet().getValue("ctecta_bcoint_aba")!=null){ordenOperacion.setCodigoABAIntermediario(ordenDao.getDataSet().getValue("ctecta_bcoint_aba"));}
							if(ordenDao.getDataSet().getValue("ctecta_bcoint_pais")!=null){ordenOperacion.setPaisBancoIntermediario(ordenDao.getDataSet().getValue("ctecta_bcoint_pais"));}
							if(ordenDao.getDataSet().getValue("trnf_tipo")!=null){ordenOperacion.setTipoTransaccionFinanc(ordenDao.getDataSet().getValue("trnf_tipo"));}
	
							operacionesArr.add(ordenOperacion);
						}
						objOrden.setOperacion(operacionesArr);// Se agregan las operaciones asociadas a la orden
					}
				}
				
				ordenDao.listarDetallesOrden(idOrden);
				//System.out.println("listarDetallesOrden: "+ordenDao.getDataSet());
				//Setear los valores de la orden
				if(ordenDao.getDataSet().count()>0){
					ordenDao.getDataSet().first();
					ordenDao.getDataSet().next();
					if(ordenDao.getDataSet().getValue("ordene_id")!=null){objOrden.setIdOrden(Long.parseLong(ordenDao.getDataSet().getValue("ordene_id")));}
					if(ordenDao.getDataSet().getValue("ordene_observacion")!=null){objOrden.setObservaciones(ordenDao.getDataSet().getValue("ordene_observacion"));}
					if(ordenDao.getDataSet().getValue("uniinv_id")!=null){objOrden.setIdUnidadInversion(Integer.parseInt(ordenDao.getDataSet().getValue("uniinv_id")));}	
					if(ordenDao.getDataSet().getValue("client_id")!=null){objOrden.setIdCliente(Long.parseLong(ordenDao.getDataSet().getValue("client_id")));}
					if(ordenDao.getDataSet().getValue("ordsta_id")!=null){objOrden.setStatus(ordenDao.getDataSet().getValue("ordsta_id"));}
					if(ordenDao.getDataSet().getValue("sistema_id")!=null){objOrden.setIdSistema(Integer.parseInt(ordenDao.getDataSet().getValue("sistema_id")));}
					if(ordenDao.getDataSet().getValue("empres_id")!=null){objOrden.setIdEmpresa(ordenDao.getDataSet().getValue("empres_id"));}
					if(ordenDao.getDataSet().getValue("contraparte")!=null){objOrden.setContraparte(ordenDao.getDataSet().getValue("contraparte"));}
					if(ordenDao.getDataSet().getValue("transa_id")!=null){objOrden.setIdTransaccion(ordenDao.getDataSet().getValue("transa_id"));}
					if(ordenDao.getDataSet().getValue("enviado")!=null){objOrden.setEnviado(Boolean.parseBoolean(ordenDao.getDataSet().getValue("enviado")));}
					if(ordenDao.getDataSet().getValue("ordene_cte_seg_bco")!=null){objOrden.setSegmentoBanco(ordenDao.getDataSet().getValue("ordene_cte_seg_bco"));}
					if(ordenDao.getDataSet().getValue("ordene_cte_seg_seg")!=null){objOrden.setSegmentoSegmento(ordenDao.getDataSet().getValue("ordene_cte_seg_seg"));}
					if(ordenDao.getDataSet().getValue("ordene_cte_seg_sub")!=null){objOrden.setSegmentoSubSegmento(ordenDao.getDataSet().getValue("ordene_cte_seg_sub"));}
					if(ordenDao.getDataSet().getValue("ordene_cte_seg_infi")!=null){objOrden.setSegmentoInfi(ordenDao.getDataSet().getValue("ordene_cte_seg_infi"));}
					if(ordenDao.getDataSet().getValue("ordene_ped_fe_orden")!=null){objOrden.setFechaOrden(formato.parse(ordenDao.getDataSet().getValue("fecha_orden")));}
					if(ordenDao.getDataSet().getValue("ordene_ped_fe_valor")!=null){objOrden.setFechaValor(formato.parse(ordenDao.getDataSet().getValue("fecha_valor_orden")));}
					if(ordenDao.getDataSet().getValue("ordene_ped_monto")!=null){objOrden.setMonto(Double.parseDouble(ordenDao.getDataSet().getValue("ordene_ped_monto")));}
					if(ordenDao.getDataSet().getValue("ordene_ped_total_pend")!=null){objOrden.setMontoPendiente(Double.parseDouble(ordenDao.getDataSet().getValue("ordene_ped_total_pend")));}
					if(ordenDao.getDataSet().getValue("ordene_ped_total")!=null){objOrden.setMontoTotal(Double.parseDouble(ordenDao.getDataSet().getValue("ordene_ped_total")));}
					if(ordenDao.getDataSet().getValue("ordene_ped_int_caidos")!=null){objOrden.setMontoInteresCaidos(Double.parseDouble(ordenDao.getDataSet().getValue("ordene_ped_int_caidos")));}
					if(ordenDao.getDataSet().getValue("bloter_id")!=null){objOrden.setIdBloter(ordenDao.getDataSet().getValue("bloter_id"));}
					//if(ordenDao.getDataSet().getValue("ordene_ped_cta_bsnro")!=null){objOrden.set;}
					if(ordenDao.getDataSet().getValue("ordene_ped_cta_bstip")!=null){objOrden.setTipoCuenta(ordenDao.getDataSet().getValue("ordene_ped_cta_bstip"));}
					if(ordenDao.getDataSet().getValue("ctecta_numero")!=null){objOrden.setCuentaCliente(ordenDao.getDataSet().getValue("ctecta_numero"));}
					if(ordenDao.getDataSet().getValue("ordene_ped_precio")!=null){objOrden.setPrecioCompra(Double.parseDouble(ordenDao.getDataSet().getValue("ordene_ped_precio")));}
					if(ordenDao.getDataSet().getValue("ordene_ped_rendimiento")!=null){objOrden.setRendimiento(Double.parseDouble(ordenDao.getDataSet().getValue("ordene_ped_rendimiento")));}
					if(ordenDao.getDataSet().getValue("ordene_ped_in_bdv")!=null){objOrden.setCarteraPropia(Boolean.parseBoolean(ordenDao.getDataSet().getValue("ordene_ped_in_bdv")));}
					if(ordenDao.getDataSet().getValue("moneda_id")!=null){objOrden.setIdMoneda(ordenDao.getDataSet().getValue("moneda_id"));}
					if(ordenDao.getDataSet().getValue("ordene_ped_rcp_precio")!=null){objOrden.setPrecioRecompra(Double.parseDouble(ordenDao.getDataSet().getValue("ordene_ped_rcp_precio")));}
					if(ordenDao.getDataSet().getValue("ordene_adj_monto")!=null){objOrden.setMontoAdjudicado(Double.parseDouble(ordenDao.getDataSet().getValue("ordene_adj_monto")));}
					if(ordenDao.getDataSet().getValue("ordene_usr_nombre")!=null){objOrden.setNombreUsuario(ordenDao.getDataSet().getValue("ordene_usr_nombre"));}
					if(ordenDao.getDataSet().getValue("ordene_usr_cen_contable")!=null){objOrden.setCentroContable(ordenDao.getDataSet().getValue("ordene_usr_cen_contable"));}
					if(ordenDao.getDataSet().getValue("ordene_usr_sucursal")!=null){objOrden.setSucursal(ordenDao.getDataSet().getValue("ordene_usr_sucursal"));}
					if(ordenDao.getDataSet().getValue("ordene_usr_terminal")!=null){objOrden.setTerminal(ordenDao.getDataSet().getValue("ordene_usr_terminal"));}
					if(ordenDao.getDataSet().getValue("ordene_veh_tom")!=null){objOrden.setVehiculoTomador(ordenDao.getDataSet().getValue("ordene_veh_tom"));}
					if(ordenDao.getDataSet().getValue("ordene_veh_col")!=null){objOrden.setVehiculoColocador(ordenDao.getDataSet().getValue("ordene_veh_col"));}
					if(ordenDao.getDataSet().getValue("ordene_veh_rec")!=null){objOrden.setVehiculoRecompra(ordenDao.getDataSet().getValue("ordene_veh_rec"));}
					if(ordenDao.getDataSet().getValue("ordene_ejec_id")!=null){objOrden.setIdEjecucion(Long.parseLong(ordenDao.getDataSet().getValue("ordene_veh_col")));}
					if(ordenDao.getDataSet().getValue("fecha_recompra")!=null){objOrden.setFechaPactoRecompra(formato.parse(ordenDao.getDataSet().getValue("fecha_recompra")));}
					if(ordenDao.getDataSet().getValue("ordene_ped_comisiones")!=null){objOrden.setMontoComisionOrden(new BigDecimal(ordenDao.getDataSet().getValue("ordene_ped_comisiones")));}
					if(ordenDao.getDataSet().getValue("ordene_fecha_adjudicacion")!=null){objOrden.setFechaAdjudicacion(formato.parse(ordenDao.getDataSet().getValue("ordene_fecha_adjudicacion")));}
					if(ordenDao.getDataSet().getValue("ordene_fecha_liquidacion")!=null){objOrden.setFechaLiquidacion(formato.parse(ordenDao.getDataSet().getValue("ordene_fecha_liquidacion")));}
					if(ordenDao.getDataSet().getValue("ordene_fecha_custodia")!=null){objOrden.setFechaCustodia(formato.parse(ordenDao.getDataSet().getValue("ordene_fecha_custodia")));}
					if(ordenDao.getDataSet().getValue("ordene_financiado")!=null){objOrden.setFinanciada(Boolean.parseBoolean(ordenDao.getDataSet().getValue("ordene_financiado")));}
					//if(ordenDao.getDataSet().getValue("id_opics")!=null){objOrden.set(ordenDao.getDataSet().getValue("id_opics"));}			
					//if(ordenDao.getDataSet().getValue("ejecucion_id")!=null){objOrden.set(ordenDao.getDataSet().getValue("ejecucion_id"));}
					if(ordenDao.getDataSet().getValue("ordene_comision_emisor")!=null){objOrden.setComisionEmisor(new BigDecimal(ordenDao.getDataSet().getValue("ordene_comision_emisor")));}
					if(ordenDao.getDataSet().getValue("ordene_tasa_pool")!=null){objOrden.setTasaPool(new BigDecimal(ordenDao.getDataSet().getValue("ordene_tasa_pool")));}
					if(ordenDao.getDataSet().getValue("ordene_ganancia_red")!=null){objOrden.setGananciaRed(new BigDecimal(ordenDao.getDataSet().getValue("ordene_ganancia_red")));}
					if(ordenDao.getDataSet().getValue("ordene_comision_oficina")!=null){objOrden.setComisionOficina(new BigDecimal(ordenDao.getDataSet().getValue("ordene_comision_oficina")));}
					if(ordenDao.getDataSet().getValue("ordene_comision_operacion")!=null){objOrden.setComisionOperacion(new BigDecimal(ordenDao.getDataSet().getValue("ordene_comision_operacion")));}
					if(ordenDao.getDataSet().getValue("tipper_id")!=null){objOrden.setIdTipoPersona(ordenDao.getDataSet().getValue("tipper_id"));}
					if(ordenDao.getDataSet().getValue("ordene_id_relacion")!=null){objOrden.setIdOrdenRelacionada(Long.parseLong(ordenDao.getDataSet().getValue("ordene_id_relacion")));}
					if(ordenDao.getDataSet().getValue("insfin_id")!=null){objOrden.setInstrumentoId(ordenDao.getDataSet().getValue("insfin_id"));}
					if(ordenDao.getDataSet().getValue("tipo_producto_id")!=null){objOrden.setTipoProducto(ordenDao.getDataSet().getValue("tipo_producto_id"));}
					if(ordenDao.getDataSet().getValue("concepto_id")!=null){objOrden.setConceptoId(ordenDao.getDataSet().getValue("concepto_id"));}
					if(ordenDao.getDataSet().getValue("sector_id")!=null){objOrden.setSectorProductivoId(ordenDao.getDataSet().getValue("sector_id"));}
					if(ordenDao.getDataSet().getValue("cod_actividad_ec")!=null){objOrden.setActividadEconomicaId(ordenDao.getDataSet().getValue("cod_actividad_ec"));}
				    //TTS-491 Modificacion para notificacion BCV via Web Service NM26659 03/03/2015
					if(ordenDao.getDataSet().getValue("ordene_estatus_bcv")!=null){objOrden.setStatusVerificacionBCV(ordenDao.getDataSet().getValue("ordene_estatus_bcv"));}
					if(ordenDao.getDataSet().getValue("ordene_id_bcv")!=null){objOrden.setIdOrdeneBCV(ordenDao.getDataSet().getValue("ordene_id_bcv"));}
					
				}
				
				//Buscar la Data Extendida asociada
				if (dataExtendida){
					listarRegistrosDataExtendida(idOrden);//return dataSet
					if(dataSet.count()>0){
						dataSet.first();
						ArrayList dataExtendidArr=new ArrayList();
						while(dataSet.next()){
							OrdenDataExt ordenDataExt=new OrdenDataExt();
							ordenDataExt.setIdData(dataSet.getValue("dtaext_id"));
							ordenDataExt.setIdOrden(Long.parseLong(dataSet.getValue("ordene_id")));
							ordenDataExt.setValor(dataSet.getValue("dtaext_valor"));
							dataExtendidArr.add(ordenDataExt);
							objOrden.agregarOrdenDataExt(ordenDataExt);//se agregan la data extendida a la orden
						}
					}
				}				
			} catch (Exception e) {				
				logger.error(e.getMessage(),e);
				throw new Exception ("Error obteniendo los datos de la orden guardada con el n&uacute;mero "+ idOrden);
			}
		
		return objOrden;
	}	
	
	/**Busca los documentos asociados a una orden
	 * Arma el objeto completo para que pueda ser modificado cualquier valor
	 * */
	public OrdenDocumento listarDocumento(long ordene_id,long ordene_doc_id, ServletContext contexto, String ip,String userName) throws Exception{
		StringBuffer sql = new StringBuffer();
		OrdenDocumento docDef = new OrdenDocumento();

		Orden orden = this.listarOrden(ordene_id);
		orden.setNombreUsuario(userName);
		
		//Cargar la Orden completa para poder mostrarla
		ProcesarDocumentos procesarDocumentos = new ProcesarDocumentos(this.dataSource);
		String[] contenido = procesarDocumentos.desplegarDocumento(orden, ordene_doc_id, contexto, ip);
			
		
		if (contenido != null){
			//Busca el nombre del documento
			sql.append("select nombre from INFI_TB_208_ORDENES_DOCUMENTOS where ");
			sql.append("INFI_TB_208_ORDENES_DOCUMENTOS.ORDENE_ID=");
			sql.append(ordene_id);
			sql.append(" and INFI_TB_208_ORDENES_DOCUMENTOS.ordene_doc_id=");
			sql.append(ordene_doc_id);			

			try {
				conn = this.dataSource.getConnection();
				statement = conn.createStatement();
				resultSet = statement.executeQuery(sql.toString());
				if (resultSet.next()) {
					docDef.setNombre(resultSet.getString("NOMBRE"));								
				}
			} catch (Exception e) {
				throw new Exception("Error al retornar el documento de la tabla: " + e.getMessage());
			} finally {
				this.closeResources();
				this.cerrarConexion();
			}			
			
			docDef.setContenido(contenido[0]);
			docDef.setContenidoBytes(contenido[0].getBytes());
			docDef.setIdDocumento(ordene_doc_id);
			docDef.setIdOrden(ordene_id);
		}
		return docDef;
	}
	
	/**Lista los datos contenidos en la la tabla 204 para una orden especifica
	 * @param idOrden
	 * @throws Exception
	 */
	public void listarOrden1(long idOrden) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		sql.append("select ordene_id,client_id,ordene_financiado from infi_tb_204_ordenes where ordene_id=");
		sql.append(idOrden);
		dataSet = db.get(dataSource, sql.toString());
	}
	
	/**busca a traves del ordene_id los detalles de la orden a un cliente especifico
	 * 
	 * */
	public void listarDetalles(long idOrden) throws Exception{
		
//		Date fecha1 = new Date(Calendar.getInstance().getTimeInMillis());
//		SimpleDateFormat formatter = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA_SIN_SEPARADOR_2);
//		String fecha_funcion = formatter.format(fecha1);
//		String fecha_re= ConstantesGenerales.FECHA_RECONVERSION;
//		int fecha_reconversion= Integer.parseInt(fecha_re);
//		int fecha_sistema= Integer.parseInt(fecha_funcion);
//		System.out.println(fecha_funcion);
//		System.out.println(fecha_sistema+" "+fecha_reconversion);
//		System.out.println(fecha1);
//		
//		//if(fecha_sistema>=fecha_reconversion){
//		StringBuffer sql = new StringBuffer();
//		sql.append("select ct.tipper_id,ct.client_cedrif,ordene_observacion,initcap(to_char(ordene_ped_fe_orden,'yyyy')) as o_anio, initcap(to_char(ordene_ped_fe_orden,'mm')) as o_mes, initcap(to_char(ordene_ped_fe_orden,'month')) as o_nombre_mes, initcap(to_char(ordene_ped_fe_orden,'dd')) as o_dia,initcap(to_char(ordene_ped_fe_valor,'yyyy')) as v_anio, initcap(to_char(ordene_ped_fe_valor,'mm')) as v_mes, initcap(to_char(ordene_ped_fe_valor,'month')) as v_nombre_mes, initcap(to_char(ordene_ped_fe_valor,'dd')) as v_dia," +
//				"initcap(to_char(ordene_fecha_adjudicacion,'yyyy')) as a_anio, initcap(to_char(ordene_fecha_adjudicacion,'mm')) as a_mes, initcap(to_char(ordene_fecha_adjudicacion,'month')) as a_nombre_mes, initcap(to_char(ordene_fecha_adjudicacion,'dd')) as a_dia, initcap(to_char(ordene_fecha_liquidacion,'yyyy')) as l_anio, initcap(to_char(ordene_fecha_liquidacion,'mm')) as l_mes, initcap(to_char(ordene_fecha_liquidacion,'month')) as l_nombre_mes, initcap(to_char(ordene_fecha_liquidacion,'dd')) as l_dia,"+
//				"case when ord.ORDENE_PED_IN_BDV="+ConstantesGenerales.FALSO+" then 'No' when ord.ORDENE_PED_IN_BDV="+ConstantesGenerales.VERDADERO+" then 'Si' end cartera_propia,case when ord.ENVIADO="+ConstantesGenerales.FALSO+" then 'No' when ord.ENVIADO="+ConstantesGenerales.VERDADERO+" then 'Si' end enviado," +
//				"ord.ORDENE_ID,ord.UNIINV_ID,ord.CLIENT_ID,ord.ORDSTA_ID,ord.SISTEMA_ID,ord.EMPRES_ID,ord.CONTRAPARTE,ord.TRANSA_ID,ENVIADO,ord.ORDENE_CTE_SEG_BCO," +
//				"ord.ORDENE_CTE_SEG_SEG,ord.ORDENE_CTE_SEG_SUB,ord.ORDENE_CTE_SEG_INFI,ord.ORDENE_PED_FE_ORDEN,ord.ORDENE_PED_FE_VALOR,ord.ORDENE_PED_MONTO,ord.ORDENE_PED_TOTAL_PEND," +
//				"case when '"+fecha_sistema+"' >= FVJ8ODIV0('BFE_FECHA_VIGENCIA') then DECODE(ord.MONEDA_ID,(SELECT fvj8odiv0('BFE_SWIFT_OLD') FROM DUAL),fvj8ocon0(ord.ORDENE_PED_TOTAL,FVJ8ODIV0('BFE_FECHA_VIGENCIA')),ord.ORDENE_PED_TOTAL) when '"+fecha_sistema+"' < FVJ8ODIV0('BFE_FECHA_VIGENCIA') then ord.ORDENE_PED_TOTAL end ORDENE_PED_TOTAL,ord.ORDENE_PED_INT_CAIDOS,ord.BLOTER_ID,ord.ORDENE_PED_CTA_BSNRO,ord.ORDENE_PED_CTA_BSTIP,ord.CTECTA_NUMERO,ord.ORDENE_PED_PRECIO,ord.ORDENE_PED_RENDIMIENTO," +
//				"ord.ORDENE_PED_IN_BDV,case when '"+fecha_sistema+"' >= FVJ8ODIV0('BFE_FECHA_VIGENCIA') then  DECODE(ord.MONEDA_ID,(SELECT fvj8odiv0('BFE_SWIFT_OLD') FROM DUAL),DECODE(ord.MONEDA_ID,(SELECT fvj8odiv0('BFE_SWIFT_OLD') FROM DUAL),FVJ8ODIV0('BFE_SWIFT'),FVJ8ODIV0('BFE_SWIFT_OLD')),ord.MONEDA_ID)  when '"+fecha_sistema+"' < FVJ8ODIV0('BFE_FECHA_VIGENCIA') then DECODE(ord.MONEDA_ID,(SELECT fvj8odiv0('BFE_SWIFT_OLD') FROM DUAL),DECODE(ord.MONEDA_ID,(SELECT fvj8odiv0('BFE_SWIFT_OLD') FROM DUAL),FVJ8ODIV0('BFE_SWIFT_OLD'),FVJ8ODIV0('BFE_SWIFT')),ord.MONEDA_ID) end MONEDA_ID,ord.ORDENE_PED_RCP_PRECIO,ord.ORDENE_ADJ_MONTO,ord.ORDENE_USR_NOMBRE,ord.ORDENE_USR_CEN_CONTABLE,ord.ORDENE_USR_SUCURSAL,ord.ORDENE_USR_TERMINAL," +
//				"ord.ORDENE_VEH_TOM,ord.ORDENE_VEH_COL,ord.ORDENE_VEH_REC,ord.ORDENE_EJEC_ID,ord.ORDENE_FE_ULT_ACT,case when '"+fecha_sistema+"' >= FVJ8ODIV0('BFE_FECHA_VIGENCIA') then DECODE(ord.MONEDA_ID,(SELECT fvj8odiv0('BFE_SWIFT_OLD') FROM DUAL),fvj8ocon0(ord.ORDENE_PED_COMISIONES,FVJ8ODIV0('BFE_FECHA_VIGENCIA')),ord.ORDENE_PED_COMISIONES) when '"+fecha_sistema+"' < FVJ8ODIV0('BFE_FECHA_VIGENCIA') then ord.ORDENE_PED_COMISIONES end ORDENE_PED_COMISIONES,ord.ORDENE_FECHA_ADJUDICACION," +
//				"ord.ORDENE_FECHA_LIQUIDACION,ord.ORDENE_FECHA_CUSTODIA,ord.ORDENE_FINANCIADO,ord.ID_OPICS,ord.EJECUCION_ID,ord.ORDENE_COMISION_EMISOR,ord.ORDENE_TASA_POOL," +
//				"ord.ORDENE_GANANCIA_RED,ord.ORDENE_COMISION_OFICINA,ord.ORDENE_COMISION_OPERACION,ord.ORDENE_OPERAC_PEND,ord.ORDENE_FE_CANCELACION,ord.ORDENE_ID_RELACION,ord.ORDENE_TASA_CAMBIO," +
//				"ord.SECTOR_ID,ord.CODIGO_ID,ord.CONCEPTO_ID,ord.ORDENE_OBSERVACION,ord.TIPO_PRODUCTO_ID,ord.NUMERO_GRUPO,ord.CTA_ABONO,ord.ORDENE_ESTATUS_BCV,ord.ORDENE_ID_BCV,ord.ORDENE_PED_PORC_COMISION_IGTF,ord.ORDENE_PED_COMISION_IGTF," +
//				"ct.client_nombre,tp.TIPPER_ID,os.ORDSTA_NOMBRE,tr.TRANSA_DESCRIPCION, (select vehicu_nombre from INFI_TB_018_VEHICULOS where vehicu_id=ord.ORDENE_VEH_TOM)as tomador, " +
//				"(select vehicu_nombre from INFI_TB_018_VEHICULOS where vehicu_id=ord.ORDENE_VEH_COL)as colocador,(select vehicu_nombre from INFI_TB_018_VEHICULOS where vehicu_id=ord.ORDENE_VEH_REC)as recompra,ve.VEHICU_NOMBRE,em.EMPRES_NOMBRE," +
//				"u.UNDINV_NOMBRE,si.SISTEMA_NOMBRE,bl.BLOTER_DESCRIPCION, inf.insfin_id,inf.INSFIN_FORMA_ORDEN, to_char(ord.ordene_ped_fe_orden, '"+ConstantesGenerales.FORMATO_FECHA+ "') as fecha_orden, to_char(ord.ordene_ped_fe_valor, '"+ConstantesGenerales.FORMATO_FECHA+ "') as fecha_valor_orden, to_char(ord.ordene_fe_ult_act, '"+ConstantesGenerales.FORMATO_FECHA+ "') as fecha_recompra, inf.insfin_descripcion, conc.concepto, ord.codigo_id as cod_actividad_ec, activ.sector as nombre_actividad_ec, sect.descripcion as sector_descripcion, tprod.nombre as nombre_tipo_producto " +
//				",(SELECT DISTINCT(a.CTECTA_NUMERO)FROM INFI_TB_202_CTES_CUENTAS a, INFI_TB_217_CTES_CUENTAS_ORD b WHERE a.CTES_CUENTAS_ID = b.CTES_CUENTAS_ID AND b.ORDENE_ID = "+ idOrden +") as cuenta_divisa " +
//				"from infi_tb_201_ctes ct " +
//				"left join infi_tb_204_ordenes ord on ct.CLIENT_ID=ord.CLIENT_ID " +
//				"left join infi_tb_200_tipo_personas tp on ct.TIPPER_ID=tp.TIPPER_ID " +
//				"left join infi_tb_203_ordenes_status os on os.ORDSTA_ID=ord.ORDSTA_ID " +
//				"left join infi_tb_106_unidad_inversion u on u.UNDINV_ID=ord.UNIINV_ID " +
//				"left join infi_tb_802_sistema si on si.SISTEMA_ID=ord.SISTEMA_ID " +
//				"left join infi_tb_102_bloter bl on bl.BLOTER_ID=ord.BLOTER_ID " +
//				"left join INFI_TB_016_EMPRESAS em on ord.EMPRES_ID=em.EMPRES_ID " +
//				"left join INFI_TB_012_TRANSACCIONES tr on ord.TRANSA_ID=tr.TRANSA_ID " +
//				"left join INFI_TB_018_VEHICULOS ve on ord.ORDENE_VEH_TOM=ve.VEHICU_ID " +
//				"left join INFI_TB_101_INST_FINANCIEROS inf on inf.insfin_id=u.insfin_id " + 
//				"left join INFI_TB_050_CONCEPTOS conc on ord.concepto_id = conc.codigo_id " + 
//				"left join INFI_TB_049_ACTIVIDAD activ on ord.codigo_id = activ.codigo_id " +
//				"left join INFI_TB_048_SECTOR_PRODUCTIVO sect on ord.sector_id = sect.sector_id " +
//				"left join INFI_TB_019_TIPO_DE_PRODUCTO tprod on ord.tipo_producto_id = tprod.tipo_producto_id " +
//				"where ord.ordene_id=").append(idOrden);
//		sql.append(" ORDER BY ct.CLIENT_CTA_CUSTOD_FECHA");
//		//System.out.println("listarDetalles -----> "+sql);
//		dataSet = db.get(dataSource, sql.toString());
//		System.out.println("listarDetalles"+sql);
//		/*}else{
			StringBuffer sql = new StringBuffer();
			sql.append("select ct.tipper_id,ct.client_cedrif,ordene_observacion,initcap(to_char(ordene_ped_fe_orden,'yyyy')) as o_anio, initcap(to_char(ordene_ped_fe_orden,'mm')) as o_mes, initcap(to_char(ordene_ped_fe_orden,'month')) as o_nombre_mes, initcap(to_char(ordene_ped_fe_orden,'dd')) as o_dia,initcap(to_char(ordene_ped_fe_valor,'yyyy')) as v_anio, initcap(to_char(ordene_ped_fe_valor,'mm')) as v_mes, initcap(to_char(ordene_ped_fe_valor,'month')) as v_nombre_mes, initcap(to_char(ordene_ped_fe_valor,'dd')) as v_dia," +
					"initcap(to_char(ordene_fecha_adjudicacion,'yyyy')) as a_anio, initcap(to_char(ordene_fecha_adjudicacion,'mm')) as a_mes, initcap(to_char(ordene_fecha_adjudicacion,'month')) as a_nombre_mes, initcap(to_char(ordene_fecha_adjudicacion,'dd')) as a_dia, initcap(to_char(ordene_fecha_liquidacion,'yyyy')) as l_anio, initcap(to_char(ordene_fecha_liquidacion,'mm')) as l_mes, initcap(to_char(ordene_fecha_liquidacion,'month')) as l_nombre_mes, initcap(to_char(ordene_fecha_liquidacion,'dd')) as l_dia,"+
					"case when ord.ORDENE_PED_IN_BDV="+ConstantesGenerales.FALSO+" then 'No' when ord.ORDENE_PED_IN_BDV="+ConstantesGenerales.VERDADERO+" then 'Si' end cartera_propia,case when ord.ENVIADO="+ConstantesGenerales.FALSO+" then 'No' when ord.ENVIADO="+ConstantesGenerales.VERDADERO+" then 'Si' end enviado," +
					"ord.ORDENE_ID,ord.UNIINV_ID,ord.CLIENT_ID,ord.ORDSTA_ID,ord.SISTEMA_ID,ord.EMPRES_ID,ord.CONTRAPARTE,ord.TRANSA_ID,ENVIADO,ord.ORDENE_CTE_SEG_BCO," +
					"ord.ORDENE_CTE_SEG_SEG,ord.ORDENE_CTE_SEG_SUB,ord.ORDENE_CTE_SEG_INFI,ord.ORDENE_PED_FE_ORDEN,ord.ORDENE_PED_FE_VALOR,ord.ORDENE_PED_MONTO,ord.ORDENE_PED_TOTAL_PEND," +
					"ord.ORDENE_PED_TOTAL,ord.ORDENE_PED_INT_CAIDOS,ord.BLOTER_ID,ord.ORDENE_PED_CTA_BSNRO,ord.ORDENE_PED_CTA_BSTIP,ord.CTECTA_NUMERO,ord.ORDENE_PED_PRECIO,ord.ORDENE_PED_RENDIMIENTO," +
					"ord.ORDENE_PED_IN_BDV,decode(ord.MONEDA_ID, FVJ8ODIV0('BFE_SWIFT_OLD'), FVJ8ODIV0('BFE_SWIFT'),ord.MONEDA_ID) MONEDA_ID,ord.ORDENE_PED_RCP_PRECIO,ord.ORDENE_ADJ_MONTO,ord.ORDENE_USR_NOMBRE,ord.ORDENE_USR_CEN_CONTABLE,ord.ORDENE_USR_SUCURSAL,ord.ORDENE_USR_TERMINAL," +
					"ord.ORDENE_VEH_TOM,ord.ORDENE_VEH_COL,ord.ORDENE_VEH_REC,ord.ORDENE_EJEC_ID,ord.ORDENE_FE_ULT_ACT,ord.ORDENE_PED_COMISIONES,ord.ORDENE_FECHA_ADJUDICACION," +
					"ord.ORDENE_FECHA_LIQUIDACION,ord.ORDENE_FECHA_CUSTODIA,ord.ORDENE_FINANCIADO,ord.ID_OPICS,ord.EJECUCION_ID,ord.ORDENE_COMISION_EMISOR,ord.ORDENE_TASA_POOL," +
					"ord.ORDENE_GANANCIA_RED,ord.ORDENE_COMISION_OFICINA,ord.ORDENE_COMISION_OPERACION,ord.ORDENE_OPERAC_PEND,ord.ORDENE_FE_CANCELACION,ord.ORDENE_ID_RELACION,ord.ORDENE_TASA_CAMBIO," +
					"ord.SECTOR_ID,ord.CODIGO_ID,ord.CONCEPTO_ID,ord.ORDENE_OBSERVACION,ord.TIPO_PRODUCTO_ID,ord.NUMERO_GRUPO,ord.CTA_ABONO,ord.ORDENE_ESTATUS_BCV,ord.ORDENE_ID_BCV,ord.ORDENE_PED_PORC_COMISION_IGTF,ord.ORDENE_PED_COMISION_IGTF," +
					"ct.client_nombre,tp.TIPPER_ID,os.ORDSTA_NOMBRE,tr.TRANSA_DESCRIPCION, (select vehicu_nombre from INFI_TB_018_VEHICULOS where vehicu_id=ord.ORDENE_VEH_TOM)as tomador, " +
					"(select vehicu_nombre from INFI_TB_018_VEHICULOS where vehicu_id=ord.ORDENE_VEH_COL)as colocador,(select vehicu_nombre from INFI_TB_018_VEHICULOS where vehicu_id=ord.ORDENE_VEH_REC)as recompra,ve.VEHICU_NOMBRE,em.EMPRES_NOMBRE," +
					"u.UNDINV_NOMBRE,si.SISTEMA_NOMBRE,bl.BLOTER_DESCRIPCION, inf.insfin_id,inf.INSFIN_FORMA_ORDEN, to_char(ord.ordene_ped_fe_orden, '"+ConstantesGenerales.FORMATO_FECHA+ "') as fecha_orden, to_char(ord.ordene_ped_fe_valor, '"+ConstantesGenerales.FORMATO_FECHA+ "') as fecha_valor_orden, to_char(ord.ordene_fe_ult_act, '"+ConstantesGenerales.FORMATO_FECHA+ "') as fecha_recompra, inf.insfin_descripcion, conc.concepto, ord.codigo_id as cod_actividad_ec, activ.sector as nombre_actividad_ec, sect.descripcion as sector_descripcion, tprod.nombre as nombre_tipo_producto " +
					",(SELECT DISTINCT(a.CTECTA_NUMERO)FROM INFI_TB_202_CTES_CUENTAS a, INFI_TB_217_CTES_CUENTAS_ORD b WHERE a.CTES_CUENTAS_ID = b.CTES_CUENTAS_ID AND b.ORDENE_ID = "+ idOrden +") as cuenta_divisa " +
					"from infi_tb_201_ctes ct " +
					"left join infi_tb_204_ordenes ord on ct.CLIENT_ID=ord.CLIENT_ID " +
					"left join infi_tb_200_tipo_personas tp on ct.TIPPER_ID=tp.TIPPER_ID " +
					"left join infi_tb_203_ordenes_status os on os.ORDSTA_ID=ord.ORDSTA_ID " +
					"left join infi_tb_106_unidad_inversion u on u.UNDINV_ID=ord.UNIINV_ID " +
					"left join infi_tb_802_sistema si on si.SISTEMA_ID=ord.SISTEMA_ID " +
					"left join infi_tb_102_bloter bl on bl.BLOTER_ID=ord.BLOTER_ID " +
					"left join INFI_TB_016_EMPRESAS em on ord.EMPRES_ID=em.EMPRES_ID " +
					"left join INFI_TB_012_TRANSACCIONES tr on ord.TRANSA_ID=tr.TRANSA_ID " +
					"left join INFI_TB_018_VEHICULOS ve on ord.ORDENE_VEH_TOM=ve.VEHICU_ID " +
					"left join INFI_TB_101_INST_FINANCIEROS inf on inf.insfin_id=u.insfin_id " + 
					"left join INFI_TB_050_CONCEPTOS conc on ord.concepto_id = conc.codigo_id " + 
					"left join INFI_TB_049_ACTIVIDAD activ on ord.codigo_id = activ.codigo_id " +
					"left join INFI_TB_048_SECTOR_PRODUCTIVO sect on ord.sector_id = sect.sector_id " +
					"left join INFI_TB_019_TIPO_DE_PRODUCTO tprod on ord.tipo_producto_id = tprod.tipo_producto_id " +
					"where ord.ordene_id=").append(idOrden);
			sql.append(" ORDER BY ct.CLIENT_CTA_CUSTOD_FECHA");
			//System.out.println("listarDetalles -----> "+sql);
			dataSet = db.get(dataSource, sql.toString());
			System.out.println("listarDetalles1 : "+sql);	
//		}*/
	}
	
public void listarDetalles2(long idOrden) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		sql.append("select ct.tipper_id,ct.client_cedrif,ordene_observacion,initcap(to_char(ordene_ped_fe_orden,'yyyy')) as o_anio, initcap(to_char(ordene_ped_fe_orden,'mm')) as o_mes, initcap(to_char(ordene_ped_fe_orden,'month')) as o_nombre_mes, initcap(to_char(ordene_ped_fe_orden,'dd')) as o_dia,initcap(to_char(ordene_ped_fe_valor,'yyyy')) as v_anio, initcap(to_char(ordene_ped_fe_valor,'mm')) as v_mes, initcap(to_char(ordene_ped_fe_valor,'month')) as v_nombre_mes, initcap(to_char(ordene_ped_fe_valor,'dd')) as v_dia," +
				"initcap(to_char(ordene_fecha_adjudicacion,'yyyy')) as a_anio, initcap(to_char(ordene_fecha_adjudicacion,'mm')) as a_mes, initcap(to_char(ordene_fecha_adjudicacion,'month')) as a_nombre_mes, initcap(to_char(ordene_fecha_adjudicacion,'dd')) as a_dia, initcap(to_char(ordene_fecha_liquidacion,'yyyy')) as l_anio, initcap(to_char(ordene_fecha_liquidacion,'mm')) as l_mes, initcap(to_char(ordene_fecha_liquidacion,'month')) as l_nombre_mes, initcap(to_char(ordene_fecha_liquidacion,'dd')) as l_dia,"+
				"case when ord.ORDENE_PED_IN_BDV="+ConstantesGenerales.FALSO+" then 'No' when ord.ORDENE_PED_IN_BDV="+ConstantesGenerales.VERDADERO+" then 'Si' end cartera_propia,case when ord.ENVIADO="+ConstantesGenerales.FALSO+" then 'No' when ord.ENVIADO="+ConstantesGenerales.VERDADERO+" then 'Si' end enviado," +
				"ord.*,ct.client_nombre,tp.TIPPER_ID,os.ORDSTA_NOMBRE,tr.TRANSA_DESCRIPCION, (select vehicu_nombre from INFI_TB_018_VEHICULOS where vehicu_id=ord.ORDENE_VEH_TOM)as tomador, " +
				"(select vehicu_nombre from INFI_TB_018_VEHICULOS where vehicu_id=ord.ORDENE_VEH_COL)as colocador,(select vehicu_nombre from INFI_TB_018_VEHICULOS where vehicu_id=ord.ORDENE_VEH_REC)as recompra,ve.VEHICU_NOMBRE,em.EMPRES_NOMBRE," +
				"u.UNDINV_NOMBRE,si.SISTEMA_NOMBRE,bl.BLOTER_DESCRIPCION, inf.insfin_id,inf.INSFIN_FORMA_ORDEN, to_char(ord.ordene_ped_fe_orden, '"+ConstantesGenerales.FORMATO_FECHA+ "') as fecha_orden, to_char(ord.ordene_ped_fe_valor, '"+ConstantesGenerales.FORMATO_FECHA+ "') as fecha_valor_orden, to_char(ord.ordene_fe_ult_act, '"+ConstantesGenerales.FORMATO_FECHA+ "') as fecha_recompra, inf.insfin_descripcion, conc.concepto, ord.codigo_id as cod_actividad_ec, activ.sector as nombre_actividad_ec, sect.descripcion as sector_descripcion, tprod.nombre as nombre_tipo_producto " +
				",(SELECT DISTINCT(a.CTECTA_NUMERO)FROM INFI_TB_202_CTES_CUENTAS a, INFI_TB_217_CTES_CUENTAS_ORD b WHERE a.CTES_CUENTAS_ID = b.CTES_CUENTAS_ID AND b.ORDENE_ID = "+ idOrden +") as cuenta_divisa " +
				"from infi_tb_201_ctes ct " +
				"left join infi_tb_204_ordenes ord on ct.CLIENT_ID=ord.CLIENT_ID " +
				"left join infi_tb_200_tipo_personas tp on ct.TIPPER_ID=tp.TIPPER_ID " +
				"left join infi_tb_203_ordenes_status os on os.ORDSTA_ID=ord.ORDSTA_ID " +
				"left join infi_tb_106_unidad_inversion u on u.UNDINV_ID=ord.UNIINV_ID " +
				"left join infi_tb_802_sistema si on si.SISTEMA_ID=ord.SISTEMA_ID " +
				"left join infi_tb_102_bloter bl on bl.BLOTER_ID=ord.BLOTER_ID " +
				"left join INFI_TB_016_EMPRESAS em on ord.EMPRES_ID=em.EMPRES_ID " +
				"left join INFI_TB_012_TRANSACCIONES tr on ord.TRANSA_ID=tr.TRANSA_ID " +
				"left join INFI_TB_018_VEHICULOS ve on ord.ORDENE_VEH_TOM=ve.VEHICU_ID " +
				"left join INFI_TB_101_INST_FINANCIEROS inf on inf.insfin_id=u.insfin_id " + 
				"left join INFI_TB_050_CONCEPTOS conc on ord.concepto_id = conc.codigo_id " + 
				"left join INFI_TB_049_ACTIVIDAD activ on ord.codigo_id = activ.codigo_id " +
				"left join INFI_TB_048_SECTOR_PRODUCTIVO sect on ord.sector_id = sect.sector_id " +
				"left join INFI_TB_019_TIPO_DE_PRODUCTO tprod on ord.tipo_producto_id = tprod.tipo_producto_id " +
				"where ord.ordene_id=").append(idOrden);
		sql.append(" ORDER BY ct.CLIENT_CTA_CUSTOD_FECHA");
		System.out.println("listarDetalles : "+sql);
		dataSet = db.get(dataSource, sql.toString());
	}
	
	/**Busca a través del ordene_id los detalles de la orden que seran invocados mediante etiquetas
	 * 
	 * */
	public void listarDetallesEtiquetas(long idOrden) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append("ord.ORDENE_ID, ord.ORDENE_OBSERVACION, ord.ORDENE_PED_TOTAL, ord.ORDENE_PED_MONTO, ord.ORDENE_PED_INT_CAIDOS, ");
		sql.append("ord.ORDENE_PED_COMISIONES, ord.ORDENE_ADJ_MONTO, ord.ORDENE_PED_PRECIO, ord.ORDENE_USR_SUCURSAL, ");
		sql.append("to_char(ord.ordene_ped_fe_orden, '"+ConstantesGenerales.FORMATO_FECHA+ "') as fecha_orden, ");
		sql.append("initcap(to_char(ord.ordene_ped_fe_orden,'yyyy')) as o_anio, initcap(to_char(ord.ordene_ped_fe_orden,'mm')) as o_mes, ");
		sql.append("initcap(to_char(ord.ordene_ped_fe_orden,'month','NLS_DATE_LANGUAGE=spanish')) as o_nombre_mes, initcap(to_char(ord.ordene_ped_fe_orden,'dd')) as o_dia, ");
		sql.append("to_char(ord.ordene_ped_fe_valor, '"+ConstantesGenerales.FORMATO_FECHA+ "') as fecha_valor_orden, ");
		sql.append("initcap(to_char(ord.ordene_ped_fe_valor,'yyyy')) as v_anio, initcap(to_char(ord.ordene_ped_fe_valor,'mm')) as v_mes, ");
		sql.append("initcap(to_char(ord.ordene_ped_fe_valor,'month','NLS_DATE_LANGUAGE=spanish')) as v_nombre_mes, initcap(to_char(ord.ordene_ped_fe_valor,'dd')) as v_dia, ");
		sql.append("initcap(to_char(ord.ordene_fecha_adjudicacion,'yyyy')) as a_anio, initcap(to_char(ord.ordene_fecha_adjudicacion,'mm')) as a_mes, ");
		sql.append("initcap(to_char(ord.ordene_fecha_adjudicacion,'month','NLS_DATE_LANGUAGE=spanish')) as a_nombre_mes, initcap(to_char(ord.ordene_fecha_adjudicacion,'dd')) as a_dia, ");
		sql.append("initcap(to_char(ord.ordene_fecha_liquidacion,'yyyy')) as l_anio, initcap(to_char(ord.ordene_fecha_liquidacion,'mm')) as l_mes, ");
		sql.append("initcap(to_char(ord.ordene_fecha_liquidacion,'month','NLS_DATE_LANGUAGE=spanish')) as l_nombre_mes, initcap(to_char(ord.ordene_fecha_liquidacion,'dd')) as l_dia, ");
		sql.append("to_char(ord.ordene_fe_ult_act, '"+ConstantesGenerales.FORMATO_FECHA+ "') as fecha_recompra, ");
		sql.append("case when ord.ORDENE_PED_IN_BDV="+ConstantesGenerales.FALSO+" then 'No' when ord.ORDENE_PED_IN_BDV="+ConstantesGenerales.VERDADERO+" then 'Si' end cartera_propia, ");
		sql.append("case when ord.ENVIADO="+ConstantesGenerales.FALSO+" then 'No' when ord.ENVIADO="+ConstantesGenerales.VERDADERO+" then 'Si' end enviado, ");
		sql.append("ord.codigo_id as cod_actividad_ec, ord.CTECTA_NUMERO, ord.ORDENE_TASA_POOL, ");
		/*
 		sql.append("ord.TRANSA_ID, ord.ORDENE_CTE_SEG_BCO, ord.ORDENE_CTE_SEG_SEG, ord.ORDENE_CTE_SEG_SUB, ord.ORDENE_CTE_SEG_INFI, ");
		sql.append("ord.CONTRAPARTE, ord.ORDENE_PED_TOTAL_PEND, "); 
		sql.append("ord.BLOTER_ID, ord.ORDENE_PED_CTA_BSNRO, ord.ORDENE_PED_CTA_BSTIP, ord.CTECTA_NUMERO, ");
		sql.append("ord.ORDENE_PED_RENDIMIENTO, ord.MONEDA_ID, ord.ORDENE_PED_RCP_PRECIO, ");
		sql.append("ord.ORDENE_USR_NOMBRE, ord.ORDENE_USR_CEN_CONTABLE, ord.ORDENE_USR_TERMINAL, ");
		sql.append("ord.ORDENE_VEH_TOM, ord.ORDENE_VEH_COL, ord.ORDENE_VEH_REC, ord.ORDENE_EJEC_ID, ");
		sql.append("ord.ORDENE_FECHA_ADJUDICACION, ord.ORDENE_FECHA_LIQUIDACION, ord.ORDENE_FECHA_CUSTODIA, ord.ORDENE_FINANCIADO, ord.ID_OPICS, ");
		sql.append("ord.EJECUCION_ID, ord.ORDENE_COMISION_EMISOR, ord.ORDENE_TASA_POOL, ord.ORDENE_GANANCIA_RED, ord.ORDENE_COMISION_OFICINA, ");
		sql.append("ord.ORDENE_COMISION_OPERACION, ord.ORDENE_OPERAC_PEND, ord.ORDENE_FE_CANCELACION, ord.ORDENE_ID_RELACION, ord.ORDENE_TASA_CAMBIO, ");
		sql.append("ord.SECTOR_ID, ord.CODIGO_ID, ord.CONCEPTO_ID, ord.TIPO_PRODUCTO_ID, ord.NUMERO_GRUPO, ord.CTA_ABONO, ");
		//"ord.*, "
		*/
		
		sql.append("uni.UNDINV_NOMBRE, uni.UNDINV_EMISION, uni.UNDINV_SERIE, uni.UNDINV_RENDIMIENTO, uni.MONEDA_ID, ");
		sql.append("initcap(to_char(uni.UNDINV_FE_EMISION,'yyyy')) as e_anio, initcap(to_char(uni.UNDINV_FE_EMISION,'mm')) as e_mes, ");
		sql.append("initcap(to_char(uni.UNDINV_FE_EMISION,'month','NLS_DATE_LANGUAGE=spanish')) as e_nombre_mes, initcap(to_char(uni.UNDINV_FE_EMISION,'dd')) as e_dia, ");
		sql.append("initcap(to_char(uni.UNDINV_FE_ADJUDICACION,'yyyy')) as a_anio, initcap(to_char(uni.UNDINV_FE_ADJUDICACION,'mm')) as a_mes, ");
		sql.append("initcap(to_char(uni.UNDINV_FE_ADJUDICACION,'month','NLS_DATE_LANGUAGE=spanish')) as a_nombre_mes, initcap(to_char(uni.UNDINV_FE_ADJUDICACION,'dd')) as a_dia, ");
		sql.append("initcap(to_char(uni.UNDINV_FE_LIQUIDACION,'yyyy')) as uni_l_anio, initcap(to_char(uni.UNDINV_FE_LIQUIDACION,'mm')) as uni_l_mes, ");
		sql.append("initcap(to_char(uni.UNDINV_FE_LIQUIDACION,'month','NLS_DATE_LANGUAGE=spanish')) as uni_l_nombre_mes, initcap(to_char(uni.UNDINV_FE_LIQUIDACION,'dd')) as uni_l_dia, ");
		sql.append("initcap(to_char(uni.UNDINV_FE_CIERRE,'yyyy')) as c_anio, initcap(to_char(uni.UNDINV_FE_CIERRE,'mm')) as c_mes, ");
		sql.append("initcap(to_char(uni.UNDINV_FE_CIERRE,'month','NLS_DATE_LANGUAGE=spanish')) as c_nombre_mes, initcap(to_char(uni.UNDINV_FE_CIERRE,'dd')) as c_dia, ");
		/*
		sql.append("uni.UNDINV_DESCRIPCION, uni.INSFIN_ID, uni.UNDINV_TASA_CAMBIO, ");
		sql.append(", uni.UNDINV_IN_VTA_EMPLEADOS, uni.TPPEVA_ID, uni.UNDINV_PRECIO_MINIMO, uni.UNDINV_COMENTARIOS, uni.UNDINV_DOC_BDV, ");
		sql.append("uni.UNDINV_DOC_EMISOR, uni.UNDINV_IN_RECOMPRA_NETEO, ");
		sql.append("uni.MONEDA_ID, uni.UNDINV_UMI_INV_TOTAL, uni.UNDINV_UMI_INV_MTO_MIN, uni.UNDINV_UMI_INV_MTO_MAX, uni.UNDINV_UMI_UM_CANT_MIN, ");
		sql.append("uni.UNDINV_UMI_UM_CANT_MAX, uni.UNDINV_STATUS, uni.AUT_USUARIO_USERID, uni.AUT_USUARIO_NOMBRE, uni.AUT_USUARIO_ROL_NOMBRE, ");
		sql.append("uni.AUT_ESTACION, uni.AUT_IP, uni.AUT_FECHA, uni.UPD_USUARIO_USERID, uni.UPD_USUARIO_NOMBRE, uni.UPD_USUARIO_ROL_NOMBRE, ");
		sql.append("uni.UPD_ESTACION, uni.UPD_IP, uni.UPD_FECHA, uni.EMPRES_ID, uni.UNDINV_UMI_INV_DISPONIBLE, uni.UNDINV_UMI_UNIDAD, ");
		sql.append("uni.UNDINV_PCT_MAX_FINAN, uni.UNDINV_PRECIO_MAXIMO, uni.UNDINV_MULTIPLOS, uni.UNDINV_IN_PEDIDO_MONTO, uni.UNDINV_TASA_POOL, ");
		sql.append("uni.UNDINV_MERCADO, uni.UNDINV_IN_PRECIO_SUCIO, uni.UNDINV_FE_LIQUIDACION_HORA1, ");
		sql.append("uni.UNDINV_FE_LIQUIDACION_HORA2, uni.PAGO_IN_BCV, uni.IN_COBRO_BATCH_ADJ, uni.IN_COBRO_BATCH_LIQ, uni.DIAS_APERTURA_CUENTA, ");
		*/
		
		sql.append("cte.TIPPER_ID, cte.CLIENT_CEDRIF, cte.CLIENT_NOMBRE, cte.CLIENT_DIRECCION, cte.CLIENT_TELEFONO, cte.CLIENT_TIPO, cte.CLIENT_CORREO_ELECTRONICO, ");
		sql.append("DECODE(cte.CLIENT_EMPLEADO, 1, 'Si', 'No') as CLIENT_EMPLEADO, ");
		
		//sql.append("uit.UNDINV_ID, uit.TITULO_ID, uit.UITITU_PORCENTAJE, uit.UITITU_VALOR_EQUIVALENTE, uit.UITITU_IN_CONIDB, ");
		
		sql.append("inf.INSFIN_ID, inf.INSFIN_FORMA_ORDEN, inf.INSFIN_DESCRIPCION, ");
		sql.append("(select vehicu_nombre from INFI_TB_018_VEHICULOS where vehicu_id=ord.ORDENE_VEH_TOM) as TOMADOR, ");
		sql.append("(select vehicu_nombre from INFI_TB_018_VEHICULOS where vehicu_id=ord.ORDENE_VEH_COL) as COLOCADOR, ");
		sql.append("(select vehicu_nombre from INFI_TB_018_VEHICULOS where vehicu_id=ord.ORDENE_VEH_REC) as RECOMPRA, ");
		sql.append("tip.TIPPER_NOMBRE, tip.TIPPER_ID, os.ORDSTA_NOMBRE, si.SISTEMA_NOMBRE, em.EMPRES_NOMBRE, tr.TRANSA_DESCRIPCION, ");
		sql.append("ve.VEHICU_NOMBRE, ve.VEHICU_RIF, bl.BLOTER_DESCRIPCION, conc.CONCEPTO as CONCEPTO, activ.sector as NOMBRE_ACTIVIDAD_EC, ");
		sql.append("sect.descripcion as SECTOR_DESCRIPCION, tprod.nombre as NOMBRE_TIPO_PRODUCTO, ");
		//NM29643 infi_TTS_466 Actualizacion del proceso de envio de correos
		sql.append("com.COMISION_PCT, com.COMISION_MONTO_FIJO ");
		
		/*sql.append("FROM ADM_INFI.INFI_TB_204_ORDENES ord, ADM_INFI.INFI_TB_106_UNIDAD_INVERSION uni, ADM_INFI.INFI_TB_201_CTES cte, ");
		sql.append("ADM_INFI.INFI_TB_108_UI_TITULOS uit, ADM_INFI.INFI_TB_200_TIPO_PERSONAS tip, ADM_INFI.infi_tb_203_ordenes_status os, ");
		sql.append("ADM_INFI.infi_tb_802_sistema si, ADM_INFI.INFI_TB_016_EMPRESAS em, ADM_INFI.INFI_TB_012_TRANSACCIONES tr, ");
		sql.append("ADM_INFI.INFI_TB_018_VEHICULOS ve, ADM_INFI.infi_tb_102_bloter bl, ADM_INFI.INFI_TB_050_CONCEPTOS conc, ");
		sql.append("ADM_INFI.INFI_TB_101_INST_FINANCIEROS inf, ADM_INFI.INFI_TB_049_ACTIVIDAD activ, ADM_INFI.INFI_TB_048_SECTOR_PRODUCTIVO sect, ");
		sql.append("ADM_INFI.INFI_TB_019_TIPO_DE_PRODUCTO tprod ");
		sql.append("WHERE ord.UNIINV_ID=uni.UNDINV_ID ");
		sql.append("AND uni.INSFIN_ID=inf.INSFIN_ID ");
		sql.append("AND ord.CLIENT_ID=cte.CLIENT_ID ");
		sql.append("AND cte.TIPPER_ID=tip.TIPPER_ID ");
		sql.append("AND ord.UNIINV_ID=uit.UNDINV_ID ");
		sql.append("AND ord.ORDSTA_ID=os.ORDSTA_ID ");
		sql.append("AND ord.SISTEMA_ID=si.SISTEMA_ID ");
		sql.append("AND ord.EMPRES_ID=em.EMPRES_ID ");
		sql.append("AND ord.TRANSA_ID=tr.TRANSA_ID ");
		sql.append("AND ord.ORDENE_VEH_TOM=ve.VEHICU_ID ");
		sql.append("AND ord.BLOTER_ID=bl.BLOTER_ID ");
		sql.append("AND ord.CONCEPTO_ID=conc.CODIGO_ID ");
		sql.append("AND ord.CODIGO_ID=activ.CODIGO_ID ");
		sql.append("AND ord.SECTOR_ID=sect.SECTOR_ID ");
		sql.append("AND ord.TIPO_PRODUCTO_ID=tprod.TIPO_PRODUCTO_ID ");
		sql.append("AND ord.ORDENE_ID=").append(idOrden);*/
		
		sql.append("FROM infi_tb_201_ctes cte ");
		sql.append("left join ADM_INFI.infi_tb_204_ordenes ord on cte.CLIENT_ID=ord.CLIENT_ID ");
		sql.append("left join ADM_INFI.infi_tb_200_tipo_personas tip on cte.TIPPER_ID=tip.TIPPER_ID ");
		sql.append("left join ADM_INFI.infi_tb_203_ordenes_status os on os.ORDSTA_ID=ord.ORDSTA_ID ");
		sql.append("left join ADM_INFI.infi_tb_106_unidad_inversion uni on uni.UNDINV_ID=ord.UNIINV_ID ");
		sql.append("left join ADM_INFI.infi_tb_802_sistema si on si.SISTEMA_ID=ord.SISTEMA_ID ");
		sql.append("left join ADM_INFI.infi_tb_102_bloter bl on bl.BLOTER_ID=ord.BLOTER_ID ");
		sql.append("left join ADM_INFI.infi_TB_016_EMPRESAS em on ord.EMPRES_ID=em.EMPRES_ID ");
		sql.append("left join ADM_INFI.infi_TB_012_TRANSACCIONES tr on ord.TRANSA_ID=tr.TRANSA_ID ");
		sql.append("left join ADM_INFI.infi_TB_018_VEHICULOS ve on ord.ORDENE_VEH_TOM=ve.VEHICU_ID ");
		sql.append("left join ADM_INFI.infi_TB_101_INST_FINANCIEROS inf on inf.insfin_id=uni.insfin_id "); 
		sql.append("left join ADM_INFI.infi_TB_050_CONCEPTOS conc on ord.concepto_id = conc.codigo_id "); 
		sql.append("left join ADM_INFI.infi_TB_049_ACTIVIDAD activ on ord.codigo_id = activ.codigo_id ");
		sql.append("left join ADM_INFI.infi_TB_048_SECTOR_PRODUCTIVO sect on ord.sector_id = sect.sector_id ");
		sql.append("left join ADM_INFI.infi_TB_019_TIPO_DE_PRODUCTO tprod on ord.tipo_producto_id = tprod.tipo_producto_id ");
		//NM29643 infi_TTS_466 Actualizacion del proceso de envio de correos
		sql.append("left join ADM_INFI.INFI_TB_112_UI_COMISION com ON uni.UNDINV_ID = com.UNDINV_ID ");
		sql.append(" WHERE ord.ordene_id=").append(idOrden);
		
		//System.out.println("DETALEEEEEE ETIQUETAS:\n"+sql);
		
		dataSet = db.get(dataSource, sql.toString());
		
	}
	
	/**Busca a través del ordene_id los detalles de la orden que seran invocados mediante etiquetas
	 * NM29643 - infi_TTS_466 14/10/2014
	 * */
	public void getPorcentajeComision(long idOrden) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		sql.append("com.COMISION_PCT, com.COMISION_MONTO_FIJO ");
		sql.append("FROM infi_tb_204_ordenes ord, infi_tb_106_unidad_inversion uni, INFI_TB_112_UI_COMISION com ");
		sql.append("WHERE ord.UNIINV_ID=uni.UNDINV_ID AND uni.UNDINV_ID=com.UNDINV_ID ");
		sql.append("AND ord.ordene_id=").append(idOrden);
		
		dataSet = db.get(dataSource, sql.toString());
		
	}
	
	/**busca a traves del ordene_id los detalles de la orden a un cliente especifico
	 * 
	 * */
	public void listarDetalles1(long idOrden) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		sql.append("select infi_tb_201_ctes.client_nombre,infi_tb_204_ordenes.ordene_id,infi_tb_204_ordenes.ORDENE_PED_FE_ORDEN,infi_tb_204_ordenes.UNIINV_ID,infi_tb_204_ordenes.ORDSTA_ID,infi_tb_203_ordenes_status.ORDSTA_NOMBRE,infi_tb_204_ordenes.SISTEMA_ID,infi_tb_204_ordenes.CONTRAPARTE,infi_tb_204_ordenes.TRANSA_ID,INFI_TB_012_TRANSACCIONES.TRANSA_DESCRIPCION,infi_tb_204_ordenes.ORDENE_PED_TOTAL,infi_tb_204_ordenes.BLOTER_ID,infi_tb_204_ordenes.ORDENE_VEH_TOM,INFI_TB_018_VEHICULOS.VEHICU_NOMBRE,infi_tb_204_ordenes.EMPRES_ID,INFI_TB_016_EMPRESAS.EMPRES_NOMBRE,infi_tb_204_ordenes.ENVIADO,infi_tb_106_unidad_inversion.UNDINV_NOMBRE,infi_tb_802_sistema.SISTEMA_NOMBRE,infi_tb_102_bloter.BLOTER_DESCRIPCION from infi_tb_201_ctes left join infi_tb_204_ordenes on infi_tb_201_ctes.CLIENT_ID=infi_tb_204_ordenes.CLIENT_ID left join infi_tb_203_ordenes_status on infi_tb_203_ordenes_status.ORDSTA_ID=infi_tb_204_ordenes.ORDSTA_ID left join infi_tb_106_unidad_inversion on infi_tb_106_unidad_inversion.UNDINV_ID=infi_tb_204_ordenes.UNIINV_ID left join infi_tb_802_sistema on infi_tb_802_sistema.SISTEMA_ID=infi_tb_204_ordenes.SISTEMA_ID left join infi_tb_102_bloter on infi_tb_102_bloter.BLOTER_ID=infi_tb_204_ordenes.BLOTER_ID left join INFI_TB_016_EMPRESAS on infi_tb_204_ordenes.EMPRES_ID=INFI_TB_016_EMPRESAS.EMPRES_ID left join INFI_TB_012_TRANSACCIONES on infi_tb_204_ordenes.TRANSA_ID=INFI_TB_012_TRANSACCIONES.TRANSA_ID left join INFI_TB_018_VEHICULOS on infi_tb_204_ordenes.ORDENE_VEH_TOM=INFI_TB_018_VEHICULOS.VEHICU_ID where 1=1");
		sql.append(" and infi_tb_204_ordenes.ordene_id=").append(idOrden);
		sql.append(" ORDER BY infi_tb_201_ctes.CLIENT_CTA_CUSTOD_FECHA");
		dataSet = db.get(dataSource, sql.toString());
		
	}
	
	/**Listar los documentos asociados a un ordene_id
	 * @param Long idOrden
	 * */
	public void listarDocumentosOrden(long idOrden) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		sql.append("select INFI_TB_208_ORDENES_DOCUMENTOS.* from INFI_TB_208_ORDENES_DOCUMENTOS where INFI_TB_208_ORDENES_DOCUMENTOS.ORDENE_ID=");
		sql.append(idOrden);
		dataSet = db.get(dataSource, sql.toString());
	}
	
	/**Listar los documentos asociados a un ordene_id
	 * @param Long idOrden
	 * */
	public int cantidadDocumentosOrdenPorTransacion(long idOrden, String idTransaccion) throws Exception{
		StringBuffer sql = new StringBuffer();
		int registros=0;
		sql.append("select count(*) as registros from INFI_TB_208_ORDENES_DOCUMENTOS od, INFI_TB_024_TRANSACCION_DOCS td where od.ordene_doc_id=td.documento_id and od.ORDENE_ID=");
		sql.append(idOrden);
		sql.append(" and transa_id='");
		sql.append(idTransaccion);
		sql.append("'");
		dataSet = db.get(dataSource, sql.toString());
		
		if(dataSet.count()>0){
			dataSet.first();
			dataSet.next();
			registros=  Integer.parseInt(dataSet.getValue("registros"));
		}	
		
		return registros;
	}
	
	/**Listar los documentos asociados a un ordene_id
	 * @param Long idOrden
	 * */
	public void listarDocumentosOrden(long idOrden,long ordene_doc_id) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		sql.append("select INFI_TB_208_ORDENES_DOCUMENTOS.* from INFI_TB_208_ORDENES_DOCUMENTOS where INFI_TB_208_ORDENES_DOCUMENTOS.ORDENE_ID=");
		sql.append(7);
		sql.append(" and INFI_TB_208_ORDENES_DOCUMENTOS.ordene_doc_id=");
		sql.append(ordene_doc_id);
		dataSet = db.get(dataSource, sql.toString());
	}
	/**busca a traves del ordene_id los detalles de la transaccion
	 * 
	 * */
	public void listarDetallesTransacciones1(long idOrden) throws Exception{
		Date fecha1 = new Date(Calendar.getInstance().getTimeInMillis());
		SimpleDateFormat formatter = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA_SIN_SEPARADOR_2);
		String fecha_funcion = formatter.format(fecha1);
		String fecha_re= ConstantesGenerales.FECHA_RECONVERSION;
		int fecha_reconversion= Integer.parseInt(fecha_re);
		int fecha_sistema= Integer.parseInt(fecha_funcion);
		System.out.println(fecha_funcion);
		System.out.println(fecha_sistema+" "+fecha_reconversion);
		System.out.println(fecha1);
		
		if(fecha_sistema>=fecha_reconversion){
		
		StringBuffer sql = new StringBuffer();
		/*ITS-3227 Incidencia servidor de Rentabilidad caido 01-Jun-16. Código comentado para permitir la continuidad de la operativa NM25287*/
		sql.append("select INFI_TB_805_TRNFIN_TIPO.TRNFIN_TIPO_DESCRIPCION,INFI_TB_800_TRN_FINANCIERAS.trnfin_nombre,infi_tb_100_titulos.TITULO_DESCRIPCION,decode(infi_tb_207_ordenes_operacion.moneda_id, FVJ8ODIV0('BFE_SWIFT_OLD'), FVJ8ODIV0('BFE_SWIFT'),infi_tb_207_ordenes_operacion.moneda_id) MONEDA_ID,decode(infi_tb_207_ordenes_operacion.moneda_id,FVJ8ODIV0('BFE_SWIFT_OLD'), FVJ8ODIV0('BFE_SWIFT'),infi_tb_207_ordenes_operacion.moneda_id) moneda_descripcion,infi_tb_204_ordenes.ordene_id,infi_tb_207_ordenes_operacion.ORDENE_ID,infi_tb_207_ordenes_operacion.ORDENE_OPERACION_ID,infi_tb_207_ordenes_operacion.TRNFIN_ID,infi_tb_207_ordenes_operacion.STATUS_OPERACION,infi_tb_207_ordenes_operacion.CTA_ORIGEN,CTA_DESTINO,infi_tb_207_ordenes_operacion.APLICA_REVERSO,fvj8ocon0(infi_tb_207_ordenes_operacion.MONTO_OPERACION,'"+fecha_reconversion+"') MONTO_OPERACION," +
				"infi_tb_207_ordenes_operacion.TASA,infi_tb_207_ordenes_operacion.FECHA_APLICAR,infi_tb_207_ordenes_operacion.FECHA_PROCESADA,infi_tb_207_ordenes_operacion.ORDENE_OPERACION_ERROR,infi_tb_207_ordenes_operacion.SERIAL,infi_tb_207_ordenes_operacion.IN_COMISION,infi_tb_207_ordenes_operacion.CTECTA_NUMERO,infi_tb_207_ordenes_operacion.CTECTA_NOMBRE,infi_tb_207_ordenes_operacion.CTECTA_BCOCTA_BCO," +
				"infi_tb_207_ordenes_operacion.CTECTA_BCOCTA_DIRECCION,infi_tb_207_ordenes_operacion.CTECTA_BCOCTA_SWIFT,infi_tb_207_ordenes_operacion.CTECTA_BCOCTA_BIC,infi_tb_207_ordenes_operacion.CTECTA_BCOCTA_TELEFONO,infi_tb_207_ordenes_operacion.CTECTA_BCOCTA_ABA,infi_tb_207_ordenes_operacion.CTECTA_BCOCTA_PAIS,infi_tb_207_ordenes_operacion.CTECTA_BCOINT_BCO," +
				"infi_tb_207_ordenes_operacion.CTECTA_BCOINT_DIRECCION,infi_tb_207_ordenes_operacion.CTECTA_BCOINT_SWIFT,infi_tb_207_ordenes_operacion.CTECTA_BCOINT_BIC,infi_tb_207_ordenes_operacion.CTECTA_BCOINT_TELEFONO,infi_tb_207_ordenes_operacion.CTECTA_BCOINT_ABA,infi_tb_207_ordenes_operacion.CTECTA_BCOINT_PAIS,infi_tb_207_ordenes_operacion.TRNF_TIPO,infi_tb_207_ordenes_operacion.TITULO_ID," +
				"infi_tb_207_ordenes_operacion.NUMERO_RETENCION,infi_tb_207_ordenes_operacion.ORDENE_RELAC_OPERACION_ID,infi_tb_207_ordenes_operacion.CHEQUE_NUMERO,infi_tb_207_ordenes_operacion.FECHA_PAGO_CHEQUE,infi_tb_207_ordenes_operacion.CODIGO_OPERACION,infi_tb_207_ordenes_operacion.FECHA_INICIO_CP,infi_tb_207_ordenes_operacion.FECHA_FIN_CP,infi_tb_207_ordenes_operacion.IN_COMISION_INVARIABLE," +
				"infi_tb_207_ordenes_operacion.DIAS_OPERACION,infi_tb_207_ordenes_operacion.OPERACION_NOMBRE,infi_tb_207_ordenes_operacion.NUMERO_MOVIMIENTO,case when infi_tb_207_ordenes_operacion.in_comision=1 then 'Si' when infi_tb_207_ordenes_operacion.in_comision=0 then 'No' end comisiones,case when infi_tb_207_ordenes_operacion.aplica_reverso=1 then 'Si' when infi_tb_207_ordenes_operacion.aplica_reverso=0 then 'No' end reverso,INFI_TB_033_OPERACION_STATUS.STATUS_OPERACION_NOMB,INFI_TB_800_TRN_FINANCIERAS.TRNFIN_DESCRIPCION " );
		sql.append("from infi_tb_204_ordenes left join infi_tb_207_ordenes_operacion on infi_tb_207_ordenes_operacion.ORDENE_ID=infi_tb_204_ordenes.ORDENE_ID left join INFI_TB_033_OPERACION_STATUS on INFI_TB_033_OPERACION_STATUS.STATUS_OPERACION=infi_tb_207_ordenes_operacion.STATUS_OPERACION left join infi_tb_100_titulos on infi_tb_207_ordenes_operacion.TITULO_ID=infi_tb_100_titulos.TITULO_ID left join INFI_TB_800_TRN_FINANCIERAS on INFI_TB_800_TRN_FINANCIERAS.TRNFIN_ID=infi_tb_207_ordenes_operacion.TRNFIN_ID left join INFI_TB_805_TRNFIN_TIPO on INFI_TB_207_ORDENES_OPERACION.TRNF_TIPO=INFI_TB_805_TRNFIN_TIPO.TRNFIN_TIPO where 1= 1");
		sql.append(" and infi_tb_207_ordenes_operacion.ordene_id=").append(idOrden);
		sql.append(" ORDER BY ordene_operacion_id");
		dataSet = db.get(dataSource, sql.toString());
		System.out.println("listarDetallesTransacciones "+sql);
		}else{
			StringBuffer sql = new StringBuffer();
			/*ITS-3227 Incidencia servidor de Rentabilidad caido 01-Jun-16. Código comentado para permitir la continuidad de la operativa NM25287*/
			sql.append("select INFI_TB_805_TRNFIN_TIPO.TRNFIN_TIPO_DESCRIPCION,INFI_TB_800_TRN_FINANCIERAS.trnfin_nombre,infi_tb_100_titulos.TITULO_DESCRIPCION,infi_tb_207_ordenes_operacion.moneda_id,infi_tb_207_ordenes_operacion.moneda_id moneda_descripcion,infi_tb_204_ordenes.ordene_id,infi_tb_207_ordenes_operacion.ORDENE_ID,infi_tb_207_ordenes_operacion.ORDENE_OPERACION_ID,infi_tb_207_ordenes_operacion.TRNFIN_ID,infi_tb_207_ordenes_operacion.STATUS_OPERACION,infi_tb_207_ordenes_operacion.CTA_ORIGEN,CTA_DESTINO,infi_tb_207_ordenes_operacion.APLICA_REVERSO,infi_tb_207_ordenes_operacion.MONTO_OPERACION," +
					"infi_tb_207_ordenes_operacion.TASA,infi_tb_207_ordenes_operacion.FECHA_APLICAR,infi_tb_207_ordenes_operacion.FECHA_PROCESADA,infi_tb_207_ordenes_operacion.ORDENE_OPERACION_ERROR,infi_tb_207_ordenes_operacion.SERIAL,infi_tb_207_ordenes_operacion.IN_COMISION,infi_tb_207_ordenes_operacion.MONEDA_ID,infi_tb_207_ordenes_operacion.CTECTA_NUMERO,infi_tb_207_ordenes_operacion.CTECTA_NOMBRE,infi_tb_207_ordenes_operacion.CTECTA_BCOCTA_BCO," +
					"infi_tb_207_ordenes_operacion.CTECTA_BCOCTA_DIRECCION,infi_tb_207_ordenes_operacion.CTECTA_BCOCTA_SWIFT,infi_tb_207_ordenes_operacion.CTECTA_BCOCTA_BIC,infi_tb_207_ordenes_operacion.CTECTA_BCOCTA_TELEFONO,infi_tb_207_ordenes_operacion.CTECTA_BCOCTA_ABA,infi_tb_207_ordenes_operacion.CTECTA_BCOCTA_PAIS,infi_tb_207_ordenes_operacion.CTECTA_BCOINT_BCO," +
					"infi_tb_207_ordenes_operacion.CTECTA_BCOINT_DIRECCION,infi_tb_207_ordenes_operacion.CTECTA_BCOINT_SWIFT,infi_tb_207_ordenes_operacion.CTECTA_BCOINT_BIC,infi_tb_207_ordenes_operacion.CTECTA_BCOINT_TELEFONO,infi_tb_207_ordenes_operacion.CTECTA_BCOINT_ABA,infi_tb_207_ordenes_operacion.CTECTA_BCOINT_PAIS,infi_tb_207_ordenes_operacion.TRNF_TIPO,infi_tb_207_ordenes_operacion.TITULO_ID," +
					"infi_tb_207_ordenes_operacion.NUMERO_RETENCION,infi_tb_207_ordenes_operacion.ORDENE_RELAC_OPERACION_ID,infi_tb_207_ordenes_operacion.CHEQUE_NUMERO,infi_tb_207_ordenes_operacion.FECHA_PAGO_CHEQUE,infi_tb_207_ordenes_operacion.CODIGO_OPERACION,infi_tb_207_ordenes_operacion.FECHA_INICIO_CP,infi_tb_207_ordenes_operacion.FECHA_FIN_CP,infi_tb_207_ordenes_operacion.IN_COMISION_INVARIABLE," +
					"infi_tb_207_ordenes_operacion.DIAS_OPERACION,infi_tb_207_ordenes_operacion.OPERACION_NOMBRE,infi_tb_207_ordenes_operacion.NUMERO_MOVIMIENTO,case when infi_tb_207_ordenes_operacion.in_comision=1 then 'Si' when infi_tb_207_ordenes_operacion.in_comision=0 then 'No' end comisiones,case when infi_tb_207_ordenes_operacion.aplica_reverso=1 then 'Si' when infi_tb_207_ordenes_operacion.aplica_reverso=0 then 'No' end reverso,INFI_TB_033_OPERACION_STATUS.STATUS_OPERACION_NOMB,INFI_TB_800_TRN_FINANCIERAS.TRNFIN_DESCRIPCION " );
			sql.append("from infi_tb_204_ordenes left join infi_tb_207_ordenes_operacion on infi_tb_207_ordenes_operacion.ORDENE_ID=infi_tb_204_ordenes.ORDENE_ID left join INFI_TB_033_OPERACION_STATUS on INFI_TB_033_OPERACION_STATUS.STATUS_OPERACION=infi_tb_207_ordenes_operacion.STATUS_OPERACION left join infi_tb_100_titulos on infi_tb_207_ordenes_operacion.TITULO_ID=infi_tb_100_titulos.TITULO_ID left join INFI_TB_800_TRN_FINANCIERAS on INFI_TB_800_TRN_FINANCIERAS.TRNFIN_ID=infi_tb_207_ordenes_operacion.TRNFIN_ID left join INFI_TB_805_TRNFIN_TIPO on INFI_TB_207_ORDENES_OPERACION.TRNF_TIPO=INFI_TB_805_TRNFIN_TIPO.TRNFIN_TIPO where 1= 1");
			sql.append(" and infi_tb_207_ordenes_operacion.ordene_id=").append(idOrden);
			sql.append(" ORDER BY ordene_operacion_id");
			dataSet = db.get(dataSource, sql.toString());
			System.out.println("listarDetallesTransacciones "+sql);	
		}
		
	}
	
	
public void listarDetallesTransacciones(long idOrden) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		/*ITS-3227 Incidencia servidor de Rentabilidad caido 01-Jun-16. Código comentado para permitir la continuidad de la operativa NM25287*/
		sql.append("select INFI_TB_805_TRNFIN_TIPO.TRNFIN_TIPO_DESCRIPCION,INFI_TB_800_TRN_FINANCIERAS.trnfin_nombre,infi_tb_100_titulos.TITULO_DESCRIPCION,infi_tb_207_ordenes_operacion.moneda_id,infi_tb_207_ordenes_operacion.moneda_id moneda_descripcion,infi_tb_204_ordenes.ordene_id,infi_tb_207_ordenes_operacion.*,case when infi_tb_207_ordenes_operacion.in_comision=1 then 'Si' when infi_tb_207_ordenes_operacion.in_comision=0 then 'No' end comisiones,case when infi_tb_207_ordenes_operacion.aplica_reverso=1 then 'Si' when infi_tb_207_ordenes_operacion.aplica_reverso=0 then 'No' end reverso,INFI_TB_033_OPERACION_STATUS.STATUS_OPERACION_NOMB,INFI_TB_800_TRN_FINANCIERAS.TRNFIN_DESCRIPCION " );
		sql.append("from infi_tb_204_ordenes left join infi_tb_207_ordenes_operacion on infi_tb_207_ordenes_operacion.ORDENE_ID=infi_tb_204_ordenes.ORDENE_ID left join INFI_TB_033_OPERACION_STATUS on INFI_TB_033_OPERACION_STATUS.STATUS_OPERACION=infi_tb_207_ordenes_operacion.STATUS_OPERACION left join infi_tb_100_titulos on infi_tb_207_ordenes_operacion.TITULO_ID=infi_tb_100_titulos.TITULO_ID left join INFI_TB_800_TRN_FINANCIERAS on INFI_TB_800_TRN_FINANCIERAS.TRNFIN_ID=infi_tb_207_ordenes_operacion.TRNFIN_ID left join INFI_TB_805_TRNFIN_TIPO on INFI_TB_207_ORDENES_OPERACION.TRNF_TIPO=INFI_TB_805_TRNFIN_TIPO.TRNFIN_TIPO where 1= 1");
		sql.append(" and infi_tb_207_ordenes_operacion.ordene_id=").append(idOrden);
		sql.append(" ORDER BY ordene_operacion_id");
		dataSet = db.get(dataSource, sql.toString());
		
	}
	/**
	 * Lista la transacciones para pago de cupon que sean de cre, en espera o rechazada
	 * @param long idOrden
	 * @throws Exception
	 */
	public void listarTransaccionePagoCupon(long idOrden) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * from infi_tb_207_ordenes_operacion where ordene_id=");
		sql.append(idOrden);
		sql.append(" and trnfin_id=107 and trnf_tipo='").append(com.bdv.infi.logic.interfaces.TransaccionFinanciera.CREDITO);
		sql.append("' and ((status_operacion='").append(com.bdv.infi.logic.interfaces.ConstantesGenerales.STATUS_EN_ESPERA);
		sql.append("') or (status_operacion='").append(com.bdv.infi.logic.interfaces.ConstantesGenerales.STATUS_RECHAZADA).append("'))");
		dataSet = db.get(dataSource, sql.toString());
	}
	
	/**
	 * Lista la transacciones para pago de cupon que sean de cre, en espera o rechazada
	 * @param long idOrden
	 * @throws Exception
	 */
	public void listarTransaccioneCobroComisionAltair(long idOrden) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * from infi_tb_207_ordenes_operacion where ordene_id=");
		sql.append(idOrden);
		sql.append(" and trnfin_id=86 and trnf_tipo='").append(com.bdv.infi.logic.interfaces.TransaccionFinanciera.DEBITO);
		sql.append("' and ((status_operacion='").append(com.bdv.infi.logic.interfaces.ConstantesGenerales.STATUS_EN_ESPERA);
		sql.append("') or (status_operacion='").append(com.bdv.infi.logic.interfaces.ConstantesGenerales.STATUS_RECHAZADA).append("'))");
		dataSet = db.get(dataSource, sql.toString());
	}
	/**Crea un dataset con las fechas requeridas para el filtro de busqueda 
	 * Fecha actual y Fecha - 60 days
	 * */
	public DataSet mostrarFechas() throws Exception { 
		
		Date date = new Date(); 
		DateFormat dateFormat = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA); 
		String fecha = dateFormat.format(date); //te devuelve la fecha en string con el formato dd/MM/yyyy
		Calendar calendar =Calendar.getInstance(); //obtiene la fecha de hoy 
		calendar.add(Calendar.DATE, -60); //el -60 indica que se le restaran 60 dias
		String fecha_less60=dateFormat.format(calendar.getTime());
		//Se coloca la informacion en el dataset para ser retornado
		DataSet _fecha_serv=new DataSet();
		_fecha_serv.append("fecha_actual",java.sql.Types.VARCHAR);
		_fecha_serv.append("fecha_less60",java.sql.Types.VARCHAR);
		_fecha_serv.addNew();
		_fecha_serv.setValue("fecha_actual",fecha);
		_fecha_serv.setValue("fecha_less60",fecha_less60);
		
		return _fecha_serv;
	}
	
	/**Recibe el uniinv_id, blotter_id y retorna el nombre de la unidad de inversion y la descripcion del blotter
	 * 
	 * */
	public DataSet listarPorcentajeOrdenesResumen(long unidad_inversion,String blotter,Date fe_ord_desde,Date fe_ord_hasta) throws Exception {
		
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		int total=0;//totales de ordenes por unidad de inversion
		int total1=0;//totales de ordenes por el filtro recibido como parametro
		sql.append("select ordene_id from infi_tb_204_ordenes where ");
		sql.append(" infi_tb_204_ordenes.transa_id in('").append(TransaccionNegocio.TOMA_DE_ORDEN).append("','").append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA).append("')");
		
		if(unidad_inversion!=0)
			filtro.append(" AND infi_tb_204_ordenes.UNIINV_ID=").append(unidad_inversion);
		sql.append(filtro);
		//Contar el total de registros por unidad de inversion
		DataSet _table =db.get(dataSource, sql.toString());
		if(_table.count()>0){
			_table.first();
			while(_table.next())
			{
				total++;
			}
		}
		if(blotter!="")
			filtro.append(" AND infi_tb_204_ordenes.BLOTER_ID='").append(blotter).append("'");
		if(fe_ord_hasta!=null && fe_ord_desde!=null){
			filtro.append(" and infi_tb_204_ordenes.ordene_ped_fe_orden between ").append(this.formatearFechaBD(fe_ord_desde)).append(" and ").append(this.formatearFechaBD(fe_ord_hasta));
		}
		sql.append(filtro);
		//Contar los registros con el filtro especificado por el usuario
		DataSet _table1 =db.get(dataSource, sql.toString());
		if(_table1.count()>0){
			_table1.first();
				while(_table1.next())
				{
					total1++;
				}
		}
			//Regla de tres para mostrar el porcentaje, se parsea a String para poder manipular el dataset
			BigDecimal cien = new BigDecimal(100);
			BigDecimal divisor =  new BigDecimal(total);
			BigDecimal monto =  new BigDecimal(total1);
			BigDecimal multiplicacion =  monto.multiply(cien);
			BigDecimal porcentaje =  multiplicacion.divide(divisor,2,BigDecimal.ROUND_HALF_EVEN);
			
			String str=String.valueOf(porcentaje);
			
			//Creacion del dataset manual
			DataSet _porcentaje=new DataSet();
			_porcentaje.append("porcentaje",java.sql.Types.VARCHAR);
			_porcentaje.addNew();
			_porcentaje.setValue("porcentaje",str);
			
			return _porcentaje;
	}
	
	public void listarTitulosDeCliente(long idCliente) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select infi_tb_704_titulos_bloqueo.tipgar_id,infi_tb_700_tipo_bloqueo.tipblo_id,infi_tb_700_tipo_bloqueo.TIPBLO_DESCRIPCION,infi_tb_201_ctes.TIPPER_ID,infi_tb_201_ctes.CLIENT_CEDRIF,infi_tb_201_ctes.CLIENT_NOMBRE,infi_tb_701_titulos.CLIENT_ID,infi_tb_701_titulos.TITCUS_CANTIDAD,infi_tb_701_titulos.TITULO_FE_INGRESO_CUSTODIA,infi_tb_701_titulos.TITULO_ID,infi_tb_100_titulos.TITULOS_PRECIO_NOMINAL,infi_tb_100_titulos.TITULO_DESCRIPCION,infi_tb_100_titulos.TITULO_ID,infi_tb_100_titulos.TITULO_MONEDA_DEN,infi_tb_100_titulos.TITULO_VALOR_NOMINAL,(infi_tb_701_titulos.titcus_cantidad * infi_tb_100_titulos.titulo_valor_nominal) as valor_total from infi_tb_701_titulos left join infi_tb_100_titulos on infi_tb_701_titulos.TITULO_ID=infi_tb_100_titulos.TITULO_ID left join infi_tb_201_ctes on infi_tb_201_ctes.CLIENT_ID=infi_tb_701_titulos.CLIENT_ID left join infi_tb_704_titulos_bloqueo on infi_tb_701_titulos.TITULO_ID=infi_tb_704_titulos_bloqueo.TITULO_ID left join infi_tb_700_tipo_bloqueo on infi_tb_704_titulos_bloqueo.TIPBLO_ID=infi_tb_700_tipo_bloqueo.TIPBLO_ID and infi_tb_704_titulos_bloqueo.CLIENT_ID=infi_tb_701_titulos.CLIENT_ID");
		sql.append(" and infi_tb_201_ctes.client_id =").append(idCliente);
		sql.append("order by infi_tb_100_titulos.TITULO_DESCRIPCION");
		dataSet = db.get(dataSource, sql.toString());
	}
	
	public void listarTitulosDeCliente(long idCliente,String titulo) throws Exception{
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select infi_tb_201_ctes.client_cta_custod_fecha,infi_tb_201_ctes.client_cta_custod_id,infi_tb_700_tipo_bloqueo.TIPBLO_ID,infi_tb_701_titulos.CLIENT_ID,infi_tb_701_titulos.TITCUS_CANTIDAD," +
		"(infi_tb_701_titulos.titcus_cantidad * infi_tb_100_titulos.titulo_valor_nominal) as valor_total," +
		"infi_tb_701_titulos.TITULO_FE_INGRESO_CUSTODIA,infi_tb_701_titulos.TITULO_ID,infi_tb_201_ctes.CLIENT_NOMBRE," +
		"infi_tb_100_titulos.TITULO_DESCRIPCION,infi_tb_100_titulos.TITULO_MONEDA_DEN," +
		"infi_tb_100_titulos.TITULO_VALOR_NOMINAL,infi_tb_704_titulos_bloqueo.TITCUS_CANTIDAD as cantidad_bloqueada," +
		"infi_tb_700_tipo_bloqueo.TIPBLO_DESCRIPCION,(infi_tb_701_titulos.TITCUS_CANTIDAD-infi_tb_704_titulos_bloqueo.TITCUS_CANTIDAD)as cantidad_desbloqueada " +
		" from infi_tb_701_titulos " +
		" left join infi_tb_201_ctes on infi_tb_701_titulos.CLIENT_ID=infi_tb_201_ctes.CLIENT_ID " +
		" left join infi_tb_100_titulos on infi_tb_701_titulos.TITULO_ID=infi_tb_100_titulos.TITULO_ID " +
		" left join infi_tb_704_titulos_bloqueo on infi_tb_701_titulos.CLIENT_ID=infi_tb_704_titulos_bloqueo.CLIENT_ID" +
		" left join infi_tb_700_tipo_bloqueo on infi_tb_704_titulos_bloqueo.TIPBLO_ID=infi_tb_700_tipo_bloqueo.TIPBLO_ID " +
		" where 1 = 1");
		sql.append(" and infi_tb_201_ctes.client_id =").append(idCliente);
		sql.append(" and infi_tb_701_titulos.titulo_id='").append(titulo).append("'");
		dataSet = db.get(dataSource, sql.toString());
		if(dataSet.count()>0)
		dataSet.next();
	}
	
	/**
	 * Lista las ordenes asociadas a un Cliente sobre una Unidad de Inversi&oacute;n espec&iacute;fica y/o un estatus determinado
	 * @param idUnidadInversion
	 * @param idCliente
	 * @throws Exception
	 */
	public void listarOrdenesPorClienteUnidadInversion(long idUnidadInversion, long idCliente, String idStatusOrden, String idTransaccionNegocio) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT * from INFI_TB_204_ORDENES where uniinv_id = ").append(idUnidadInversion);
		sql.append(" and client_id = ").append(idCliente);
		sql.append(" and transa_id = '").append(idTransaccionNegocio).append("'");
		
		//si se busca por un estatus espec&iacute;fico
		if(idStatusOrden!=null){
			sql.append(" and ordsta_id = '").append(idStatusOrden).append("'");
		}		
		
		dataSet = db.get(dataSource, sql.toString());
		
	}
	
	/**
	 * Lista todas las ordenes de un cliente para una unidad de inversi&oacute;n especifica, que no se encuentren canceladas
	 * @param idUnidadInversion
	 * @param idCliente
	 * @param idTransaccionNegocio
	 * @throws Exception
	 */
	public boolean existenOrdenesNoCanceladasClienteUI(long idUnidadInversion, long idCliente, String idTransaccionNegocio) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		boolean existen = false;
		
		sql.append("SELECT * from INFI_TB_204_ORDENES where uniinv_id = ").append(idUnidadInversion);
		sql.append(" and client_id = ").append(idCliente);
		sql.append(" and transa_id = '").append(idTransaccionNegocio).append("'");
		sql.append(" and ordsta_id not in ('").append(StatusOrden.CANCELADA).append("','").append(StatusOrden.PENDIENTE).append("')");
				
		dataSet = db.get(dataSource, sql.toString());
		
		if(dataSet.next()){
			existen = true;
		}
		
		return existen;
	}

	
	/**Lista las ordenes asociadas a una Unidad de Inversi&oacute;n espec&iacute;fica
	 * @param idUnidadInversion id de la unidad de inversión
	 * @throws Exception en caso de error
	 */
	//NM26659 02/09/2015 Modificacion de metodo para inclusion de campo ordene_estatus_bcv a la consulta y manejo de estatus de envio a BCV 
	public void listarOrdenesPorUnidadInversion(long idUnidadInversion,boolean paginado, int paginaAMostrar, int registroPorPagina,String... statusNotificacionBcv) throws Exception{
		StringBuilder sql = new StringBuilder();		
		sql.append("select o.ordene_id,o.ordsta_id ordsta_nombre,o.ordene_ped_precio,o.ordene_ped_monto,o.ordene_ped_fe_orden,o.ordene_adj_monto,o.ordene_fecha_adjudicacion,c.client_nombre,u.undinv_nombre, o.ORDENE_ESTATUS_BCV " +
				   " from INFI_TB_204_ORDENES o, INFI_TB_201_CTES c, INFI_TB_106_UNIDAD_INVERSION u" +
				   " where o.client_id=c.client_id and 	o.uniinv_id=u.undinv_id and o.uniinv_id=").append(idUnidadInversion)
				   .append(" and (o.transa_id='").append(TransaccionNegocio.TOMA_DE_ORDEN).append("' OR o.transa_id='").append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA).append("')")
				   .append(" and o.ordsta_id not in('").append(StatusOrden.CANCELADA).append("',").append("'").append(StatusOrden.PENDIENTE).append("') ");
		
		if(statusNotificacionBcv.length>0 && statusNotificacionBcv[0]!=null){
			sql.append(" and o.ordene_estatus_bcv in (");
			int count=0;
			for (String status : statusNotificacionBcv) {
				if(count>0){
					sql.append(",");
				}
				sql.append(status);
				++count;
			}
			sql.append(") ");
		}
		sql.append("order by ordsta_nombre,o.ordene_id ");
		
		//System.out.println("listarOrdenesPorUnidadInversion ---> " + sql.toString());
		if (paginado && paginaAMostrar>0 && registroPorPagina> 0){			
			dataSet = obtenerDataSetPaginado(sql.toString(),paginaAMostrar,registroPorPagina);
		}else{
			dataSet = db.get(dataSource, sql.toString());
		}
	}
	
/**
 * Lista todas las ordenes con status especifico para una unidad de inversión
 * @param idUnidadInversion
 * @param status
 * @return
 * @throws Exception
 */
	public String listarOrdenesPorUnidadInversion(long idUnidadInversion,String status,boolean transaccion, int numeroGrupo) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT o.ORDENE_ID,o.ORDENE_PED_PRECIO,o.ORDENE_PED_TOTAL,o.ORDENE_PED_FE_VALOR,o.ORDENE_PED_FE_ORDEN,o.ORDENE_VEH_TOM,o.ORDENE_USR_CEN_CONTABLE,o.CLIENT_ID,o.TRANSA_ID,to_char(ordene_fe_ult_act,'dd/mm/yyyy') fecha_valor_recompra, o.tipo_producto_id ");
		sql.append(" from infi_tb_204_ordenes o");
		sql.append(" where uniinv_id = ").append(idUnidadInversion);
		sql.append(" and ordsta_id='").append(status).append("'");
		
		if(transaccion){
			sql.append(" and transa_id not in ('ORDEN_VEHICULO','LIQUIDACION')");
		}
		
		if (numeroGrupo > 0){
			sql.append(" and numero_grupo=" + numeroGrupo);
		}
		
		return sql.toString();
	}	
	
	/**Lista las ordenes asociadas a una Unidad de Inversi&oacute;n espec&iacute;fica
	 * @param idUnidadInversion id de la unidad de inversión
	 * @throws Exception en caso de error
	 */
	public String listarOrdenesPorUnidadInversion(long idUnidadInversion) throws Exception{
		StringBuilder sql = new StringBuilder();		
		sql.append("select o.ordene_id,o.ordsta_id ordsta_nombre,o.ordene_ped_precio,o.ordene_ped_monto,o.ordene_ped_fe_orden,o.ordene_adj_monto,o.ordene_fecha_adjudicacion,c.client_nombre,u.undinv_nombre " +
				   " from INFI_TB_204_ORDENES o, INFI_TB_201_CTES c, INFI_TB_106_UNIDAD_INVERSION u" +
				   " where o.client_id=c.client_id and 	o.uniinv_id=u.undinv_id and o.uniinv_id=").append(idUnidadInversion)
				   .append(" and (o.transa_id='").append(TransaccionNegocio.TOMA_DE_ORDEN).append("' OR o.transa_id='").append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA).append("')")
				   .append(" and o.ordsta_id not in('").append(StatusOrden.CANCELADA).append("',").append("'").append(StatusOrden.PENDIENTE).append("') order by ordsta_nombre,o.ordene_id");
		return sql.toString();
	}
	
	/**Lista las ordenes de tipo SITME
	 * @throws Exception en caso de error
	 */
	public String listarOrdenesPorUnidadInversionSitme() throws Exception{
		StringBuilder sql = new StringBuilder();		
		sql.append("select o.ordene_id,o.ordsta_id ordsta_nombre,o.ordene_ped_precio,o.ordene_ped_monto,o.ordene_ped_fe_orden,o.ordene_adj_monto,o.ordene_fecha_adjudicacion,c.client_nombre,u.undinv_nombre " +
				   " from INFI_TB_204_ORDENES o, INFI_TB_201_CTES c, INFI_TB_106_UNIDAD_INVERSION u, INFI_TB_101_INST_FINANCIEROS if" +
				   " where o.client_id=c.client_id and 	o.uniinv_id=u.undinv_id and u.insfin_id = if.insfin_id and if.tipo_producto_id='" + ConstantesGenerales.ID_TIPO_PRODUCTO_SITME + "' and (o.transa_id='").append(TransaccionNegocio.TOMA_DE_ORDEN).append("' OR o.transa_id='").append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA).append("')")
				   .append(" and o.ordsta_id not in('").append(StatusOrden.CANCELADA).append("',").append("'").append(StatusOrden.PENDIENTE).append("') order by ordsta_nombre,o.ordene_id");
		return sql.toString();
	}	
	
    public String listarOrdenesPorUnidadInversion(String status,boolean transaccion, String idUnidadInversion,String tipoProducto, boolean isCount) throws Exception{
		StringBuilder sql = new StringBuilder();
		if(isCount){
			sql.append("SELECT count(*) cantOrdenes");
		}else{
			sql.append("SELECT ordene_id,uniinv_id,client_id,ordsta_id,transa_id,ordene_adj_monto,ordene_ped_total,ordene_veh_tom,CTA_ABONO,MONEDA_ID,ordene_ped_fe_valor,sistema_id,empres_id,CTECTA_NUMERO ");
		}
		
		sql.append(" from INFI_TB_204_ORDENES where ordsta_id='" + status + "' and tipo_producto_id='" + tipoProducto + "' and uniinv_id=" + idUnidadInversion);
		if(transaccion){
			sql.append(" and transa_id not in ('"+TransaccionNegocio.ORDEN_VEHICULO+"','"+TransaccionNegocio.LIQUIDACION+"','"+TransaccionNegocio.ORDEN_PAGO+"')");
		}		
		//System.out.println("listarOrdenesPorUnidadInversion; "+sql.toString());
		return sql.toString();
	}   
    
    public void listarOrdenesPorUnidadInversionDataSet (String status,boolean transaccion, String idUnidadInversion,String tipoProducto, boolean isCount) throws Exception{
    	dataSet = db.get(dataSource, listarOrdenesPorUnidadInversion(status,transaccion,idUnidadInversion,tipoProducto,isCount));
    }
	
	/**
	 * Lista ordenes que pueden ser canceladas dada una unidad de inversi&oacute;n
	 * @param idUnidadInversion
	 * @param status
	 * @throws Exception
	 */
	public void listarOrdenesCancelar(long idUnidadInversion, String status) throws Exception{
		StringBuffer sql = new StringBuffer();
		UnidadInversionDAO undidaInversionDAO = new UnidadInversionDAO(dataSource);
		boolean esInventario;
		
		sql.append("SELECT ord.*, c.client_nombre, b.bloter_descripcion from INFI_TB_204_ORDENES ord, ");
		sql.append(" INFI_TB_102_BLOTER b, INFI_TB_201_CTES c ");
		sql.append(" where uniinv_id = ").append(idUnidadInversion);
		sql.append(" and ord.bloter_id = b.bloter_id");
		sql.append(" and ord.client_id = c.client_id");
		sql.append(" and ord.ordsta_id = '").append(status).append("'");
		sql.append(" and ord.transa_id = '").append(TransaccionNegocio.TOMA_DE_ORDEN).append("'");
		
		String tipoInstrumento[] = new String[1];
		tipoInstrumento[0] = ConstantesGenerales.INST_TIPO_INVENTARIO;
		
		esInventario = undidaInversionDAO.esTipoInstrumento(idUnidadInversion,tipoInstrumento);
		if(esInventario){
			sql.append(" and ord.ordene_ped_fe_orden = ").append(formatearFechaBDActual()).append("");
		}
		
		dataSet = db.get(dataSource, sql.toString());
		
	}

	
	/**
	 * Lista los T&iacute;tulos asociados a una orden espec&iacute;fica.
	 * @param idOrden
	 * @throws Exception
	 */
	public void listarTitulosOrden(long idOrden) throws Exception{		
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT ord.client_id,ord.tipo_producto_id,ordtit.*, '' as campo_adicional, '0' as precio_recompra " +
				" from INFI_TB_204_ORDENES ord,	INFI_TB_206_ORDENES_TITULOS ordtit where " +
				" ord.ordene_id =  ordtit.ordene_id and ordtit.ordene_id = " + idOrden); 
		/*sql.append("SELECT ordtit.*, tit.*, '' as campo_adicional, '0' as precio_recompra from INFI_TB_206_ORDENES_TITULOS ordtit, INFI_TB_100_TITULOS tit where ordtit.ordene_id = ").append(idOrden);
		sql.append(" and ordtit.titulo_id = tit.titulo_id");*/
		dataSet = db.get(dataSource, sql.toString());
		
	}
	
	/**
	 * Lista los t&iacute;tulos asociados a una orden que posean pacto de recompra
	 * @param idOrden
	 * @throws Exception
	 */
	public void listarTitulosOrdenConRecompra(long idOrden) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT ordtit.*, tit.*, '' as campo_adicional, '0' as precio_recompra from INFI_TB_206_ORDENES_TITULOS ordtit, INFI_TB_100_TITULOS tit where ordtit.ordene_id = ").append(idOrden);
		sql.append(" and ordtit.titulo_id = tit.titulo_id");
		sql.append(" and titulo_pct_recompra<>0 and titulo_pct_recompra is not NULL");
		dataSet = db.get(dataSource, sql.toString());
		
	}

	/** 
	 * Lista los detalles de un Titulo espec&iacute;fico asociado a una orden.
	 * @param idOrden
	 * @param idTitulo
	 * @throws Exception
	 */
	public void listarDetallesTituloOrden(long idOrden, String idTitulo) throws Exception{
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT ordtit.*, tit.* from INFI_TB_206_ORDENES_TITULOS ordtit, INFI_TB_100_TITULOS tit where ordtit.ordene_id = ").append(idOrden);
		sql.append(" and ordtit.titulo_id = tit.titulo_id");
		sql.append(" and ordtit.titulo_id = '").append(idTitulo).append("'");
		dataSet = db.get(dataSource, sql.toString());
		
	}
	
	/**Lista el id de una orden 
	 * para transacciones de tipo: Toma de orden y Toma de Orden Cartera Propia
	 * en estatus Enviada, debe haber sido enviada
	 * @param idCliente
	 * @param idUnidadInversion
	 * @param montoPedido
	 * @param precioPedido
	 * */
	public void listarOrdenRecibida(String ordene_id, int idUnidadInv,boolean multiplesUI) throws Exception{
		StringBuffer sql = new StringBuffer();
		//sql.append("select ordene_id,client_id, ordene_ped_fe_orden,ordene_ped_monto,TO_CHAR(ordene_ped_fe_valor,'dd-MM-yyyy') ordene_ped_fe_valor,TO_CHAR(ordene_fe_ult_act,'dd-MM-yyyy') ordene_fe_ult_act,ordene_ped_total, ordene_ped_total_pend, ordsta_id, transa_id from INFI_TB_204_ORDENES where ordene_id=");
		sql.append("SELECT ORDENE_ID,ORDENE_VEH_TOM,ORDENE_VEH_COL,ORDENE_VEH_REC,EMPRES_ID,BLOTER_ID,MONEDA_ID,ORDENE_PED_PRECIO,");
		sql.append(" ORDENE_GANANCIA_RED,ORDENE_FINANCIADO,ORDENE_PED_FE_ORDEN,ORDENE_PED_MONTO,TO_CHAR(ordene_ped_fe_valor,'dd-MM-yyyy') ordene_ped_fe_valor,");
		sql.append(" TO_CHAR(ordene_fe_ult_act,'dd-MM-yyyy') ordene_fe_ult_act,ordene_ped_total, ordene_ped_total_pend, ordsta_id, transa_id,client_id");
		sql.append(" from INFI_TB_204_ORDENES  o where ordene_id=");
		sql.append(ordene_id);
		sql.append(" and uniinv_id=");
		sql.append(idUnidadInv);
		sql.append(" and (ordsta_id='");
		if (multiplesUI==false){
			sql.append(StatusOrden.ENVIADA).append("' OR ordsta_id='").append(StatusOrden.PROCESO_ADJUDICACION).append("')");
		}
		if (multiplesUI==true){
			sql.append(StatusOrden.REGISTRADA).append("' OR ordsta_id='").append(StatusOrden.ENVIADA).append("' OR ordsta_id='").append(StatusOrden.PROCESO_ADJUDICACION).append("')");
		}
		dataSet = db.get(dataSource, sql.toString());		
	}

	/**Lista el id de una orden 
	 * para transacciones de tipo: Toma de orden y Toma de Orden Cartera Propia
	 * en estatus ADJUDICADA, debe haber sido ADJUDICADA
	 * @param idCliente
	 * @param idUnidadInversion
	 * @param montoPedido
	 * @param precioPedido
	 * */
	public void listarExisteOrdenAdjudicada(String idCliente, BigDecimal montoPedido, BigDecimal precioPedido, int idUnidadInv) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select ordene_id, ordene_ped_fe_orden, ordene_ped_total, ordene_ped_total_pend, ordsta_id from INFI_TB_204_ORDENES where client_id=");
		sql.append(idCliente);
		sql.append(" and ordene_ped_monto=");
		sql.append(montoPedido);
		sql.append(" and ordene_ped_precio=");
		sql.append(precioPedido);
		sql.append(" and uniinv_id=");
		sql.append(idUnidadInv);
		sql.append(" and ordsta_id='");
		sql.append(StatusOrden.ADJUDICADA).append("'");
		
		dataSet = db.get(dataSource, sql.toString());		
	}	
	
	/***
	 * Lista clientes de la TB ordenes, obtiene uin objeto de tipo ArrayList los ejecucion_id
	 * @param ArrayList EjecucionIds
	 * **/
	public void listarClientesComisionesMes(ArrayList EjecucionIds)throws Exception{
			String valor="";
			for(int i=0;i<EjecucionIds.size();i++){
				if(i==(EjecucionIds.size()-1)){
					valor+=EjecucionIds.get(i);
				}else{
					valor+=EjecucionIds.get(i)+",";
				}
			}
			String sql="select * from infi_tb_204_ordenes i204 "+
				"left join infi_tb_201_ctes i201 on i204.CLIENT_ID=i201.CLIENT_ID "+
				"left join infi_tb_207_ordenes_operacion i207 on i204.ORDENE_ID=i207.ORDENE_ID "+
				"left join infi_tb_701_titulos i701 on i204.CLIENT_ID=i701.CLIENT_ID "+
				" where i204.EJECUCION_ID in ("+valor+") and i207.STATUS_OPERACION in " +
				"('"+ConstantesGenerales.STATUS_EN_ESPERA+"','"+ConstantesGenerales.STATUS_RECHAZADA+"') "+
				" and i207.titulo_id=i701.TITULO_ID";
			dataSet=db.get(dataSource,sql);
	}
	
	
	/**
	 * Lista registro de la TB Orden_Operacion recibe el idOperacion
	 *  @param String idOrdeOperacion
	 * */
	public void listarOrdenOperacion(String idOrdenOperacion)throws Exception{
		String sql="SELECT  ordene_operacion_id,titulo_id,tasa,monto_operacion,ordene_id,status_operacion FROM INFI_TB_207_ORDENES_OPERACION WHERE ORDENE_OPERACION_ID=";
		sql+=idOrdenOperacion;
		dataSet=db.get(this.dataSource,sql);
	}

	

	/**Lista las ordenes asociadas a un ejecucion Id 
	 * @param ejecucionId
	 * */
	public void listarOrdenesProceso(long ejecucionId) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select * from infi_tb_204_ordenes where infi_tb_204_ordenes.EJECUCION_ID=");
		sql.append(ejecucionId);
		dataSet = db.get(dataSource, sql.toString());		
	}
	
	/**Lista la informacion de la orden, muestra toda la data de la tabla 204  
	 * @param ordenId, ID de la orden a buscar
	 * */
	public void listarDatosOrden(long ordenId) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select o.ORDENE_FINANCIADO, o.CLIENT_ID from infi_tb_204_ordenes o where ordene_id=");
		sql.append(ordenId);
		dataSet = db.get(dataSource, sql.toString());		
	}
	
	/**Lista toda la data extendida asociada a una orden
	 * @param ejecucionId
	 * */
	public void listarRegistrosDataExtendida(long ordeneId) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select ode.DTAEXT_ID,ode.ORDENE_ID,ode.DTAEXT_VALOR,de.DTAEXT_DESCRIPCION from INFI_TB_212_ORDENES_DATAEXT ode,");
		sql.append("INFI_TB_027_DATAEXT de where ode.DTAEXT_ID=de.DTAEXT_ID and ode.ORDENE_ID=");
		sql.append(ordeneId);
		dataSet = db.get(dataSource, sql.toString());		
	}

	/**
	 * Lista los registros de data extendida de una orden tomando en cuenta un determinado tipo de data extendida (idDataExtendida)
	 * @param ordeneId
	 * @param idDataExtendida
	 * @throws Exception
	 */
	public void listarRegistrosDataExtendida(long ordeneId, String idDataExtendida) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select ORDENE_ID,DTAEXT_ID,DTAEXT_VALOR from INFI_TB_212_ORDENES_DATAEXT where ordene_id=");
		sql.append(ordeneId);
		sql.append(" AND DTAEXT_ID = '").append(idDataExtendida).append("'");
		dataSet = db.get(dataSource, sql.toString());		
	}

	
	/**Lista las sucursales que se encuentran registradas en la tabla 204 (Prueba mientras se espera como manejar la sucursal) 
	 * */
	public void listarSucursal() throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select unique ORDENE_USR_SUCURSAL from infi_tb_204_ordenes where ORDENE_USR_SUCURSAL is not null order by ORDENE_USR_SUCURSAL");
		dataSet = db.get(dataSource, sql.toString());		
	}
	
/**
 * Lista las ordenes por usuario si se especifica	
 * @param idUnidad
 * @param usuario
 * @param status
 * @param fechaDesde
 * @param fechaHasta
 * @param transaccion
 * @param paginado
 * @param paginaAMostrar
 * @param registroPorPagina
 * @throws Exception
 */
public void listarOrdenesUsuario(long idUnidad, String usuario, String status, String fechaDesde, String fechaHasta, String transaccion,boolean paginado, int paginaAMostrar, int registroPorPagina) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		
		sql.append("SELECT ord.*, trans_neg.transa_descripcion, cli.client_nombre, ui.undinv_nombre as unidad, stat.ordsta_nombre, blot.bloter_descripcion ");
		sql.append(" FROM INFI_TB_204_ORDENES ord, INFI_TB_106_UNIDAD_INVERSION ui, INFI_TB_102_BLOTER blot, INFI_TB_201_CTES cli, INFI_TB_203_ORDENES_STATUS stat, INFI_TB_012_TRANSACCIONES trans_neg");
		sql.append(" WHERE ord.uniinv_id = ui.undinv_id(+) ");
		sql.append(" and ord.bloter_id = blot.bloter_id(+) ");
		sql.append(" and ord.client_id = cli.client_id ");
		sql.append(" and ord.ordsta_id = stat.ordsta_id ");
		sql.append(" and ord.transa_id = trans_neg.transa_id ");
		
		//Busca sólo por la transacción seleccionada
		if (!transaccion.equals("-1") && !transaccion.equals("")){
			sql.append(" and ord.TRANSA_ID ='").append(transaccion).append("'");
		}else{
			//Solo transacciones del proceso de Toma de ordenes
			sql.append(" and ord.transa_id IN ('");
			sql.append(TransaccionNegocio.CANCELACION_ORDEN).append("','");
			sql.append(TransaccionNegocio.COBRO_FINANCIAMIENTO).append("','");
			sql.append(TransaccionNegocio.ORDEN_VEHICULO).append("','");
			sql.append(TransaccionNegocio.PACTO_RECOMPRA).append("','");
			sql.append(TransaccionNegocio.TOMA_DE_ORDEN).append("','");
			sql.append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA).append("','");
			sql.append(TransaccionNegocio.VENTA_TITULOS).append("') ");
		}
		
		if(idUnidad!=0){
			filtro.append(" and ord.UNIINV_ID=").append(idUnidad);
		}
		if(usuario!=null && !usuario.equalsIgnoreCase("")){
			filtro.append(" and ord.ordene_usr_nombre='").append(usuario).append("'");
		}		
		if(status!=null){
			filtro.append(" and ord.ordsta_id='").append(status).append("'");
		}
		if(fechaHasta!=null && fechaDesde!=null){
			filtro.append(" and trunc(ord.ordene_ped_fe_orden) between to_date('").append(fechaDesde).append("','" + ConstantesGenerales.FORMATO_FECHA + "') and to_date('").append(fechaHasta).append("','" + ConstantesGenerales.FORMATO_FECHA + "')");
		}
		sql.append(filtro);		
		sql.append(" ORDER BY ord.ordene_usr_nombre, ord.ordene_id desc");
		if (paginado && paginaAMostrar>0 && registroPorPagina> 0){			
			dataSet = obtenerDataSetPaginado(sql.toString(),paginaAMostrar,registroPorPagina);
		}else{
			dataSet = db.get(dataSource, sql.toString());
		}
		System.out.println("listarOrdenesUsuario "+sql);
	}
/**
 * Lista las ordenes segun los criterios enviados del filtro y por vehiculo
 * @param idUnidad
 * @param vehiculoTomador
 * @param vehiculoRec
 * @param vehiculoColocador
 * @param status
 * @param fechaDesde
 * @param fechaHasta
 * @throws Exception
 */
public void listarOrdenesVehiculo(long idUnidad, String vehiculoTomador,String vehiculoRec,String vehiculoColocador,String status, String fechaDesde, String fechaHasta, boolean paginado, int paginaAMostrar, int registroPorPagina) throws Exception{
	
	StringBuffer sql = new StringBuffer();
	StringBuffer filtro = new StringBuffer("");
	sql.append(sqlOrdenes);
		
	if(idUnidad!=0){
		filtro.append(" and infi_tb_204_ordenes.UNIINV_ID=").append(idUnidad);
	}
	if(vehiculoTomador!=null && !vehiculoTomador.equalsIgnoreCase("")){
		filtro.append(" and infi_tb_204_ordenes.ORDENE_VEH_TOM='").append(vehiculoTomador).append("'");
	}
	if(vehiculoRec!=null && !vehiculoRec.equalsIgnoreCase("")){
		filtro.append(" and infi_tb_204_ordenes.ORDENE_VEH_REC='").append(vehiculoRec).append("'");
	}
	if(vehiculoColocador!=null && !vehiculoColocador.equalsIgnoreCase("")){
		filtro.append(" and infi_tb_204_ordenes.ORDENE_VEH_COL='").append(vehiculoColocador).append("'");
	}
	if(status!=null){
		filtro.append(" and infi_tb_204_ordenes.ordsta_id='").append(status).append("'");
	}
	if(fechaHasta!=null && fechaDesde!=null){
		filtro.append(" and trunc(infi_tb_204_ordenes.ordene_ped_fe_orden) between to_date('").append(fechaDesde).append("','" + ConstantesGenerales.FORMATO_FECHA + "') and to_date('").append(fechaHasta).append("','" + ConstantesGenerales.FORMATO_FECHA + "')");
	}
	sql.append(filtro);
	sql.append(" ORDER BY infi_tb_204_ordenes.ordene_id desc");
	
	if (paginado && paginaAMostrar>0 && registroPorPagina> 0){			
		dataSet = obtenerDataSetPaginado(sql.toString(),paginaAMostrar,registroPorPagina);
	}else{
		dataSet = db.get(dataSource, sql.toString());
	}
	System.out.println("listarOrdenesVehiculo "+sql);
}
/**
 * 
 * @param idUnidad
 * @param vehiculoTomador
 * @param vehiculoRec
 * @param vehiculoColocador
 * @param status
 * @param fechaDesde
 * @param fechaHasta
 * @throws Exception
 */
public void contarOrdenesVehiculoUnidadInversion(long idUnidad, String vehiculoTomador,String vehiculoRec,String vehiculoColocador,String status, String fechaDesde, String fechaHasta) throws Exception{
	
	StringBuffer sql = new StringBuffer();
	StringBuffer filtro = new StringBuffer("");
	sql.append("select count(*)as ordenes,decode(sum(ordene_ped_total),null,0,sum(ordene_ped_total))as total from infi_tb_204_ordenes where 1=1 ");
	if(idUnidad!=0){
		filtro.append(" and infi_tb_204_ordenes.UNIINV_ID=").append(idUnidad);
	}
	if(vehiculoTomador!=null && !vehiculoTomador.equalsIgnoreCase("")){
		filtro.append(" and infi_tb_204_ordenes.ORDENE_VEH_TOM='").append(vehiculoTomador).append("'");
	}
	if(vehiculoRec!=null && !vehiculoRec.equalsIgnoreCase("")){
		filtro.append(" and infi_tb_204_ordenes.ORDENE_VEH_REC='").append(vehiculoRec).append("'");
	}
	if(vehiculoColocador!=null && !vehiculoColocador.equalsIgnoreCase("")){
		filtro.append(" and infi_tb_204_ordenes.ORDENE_VEH_COL='").append(vehiculoColocador).append("'");
	}
	if(status!=null){
		filtro.append(" and infi_tb_204_ordenes.ordsta_id='").append(status).append("'");
	}else{
		filtro.append(" and infi_tb_204_ordenes.ordsta_id not in('").append(StatusOrden.CANCELADA).append("','").append(StatusOrden.PENDIENTE).append("')");
	}
	if(fechaHasta!=null && fechaDesde!=null){
		filtro.append(" and infi_tb_204_ordenes.ordene_ped_fe_orden between to_date('").append(fechaDesde).append("','" + ConstantesGenerales.FORMATO_FECHA + "') and to_date('").append(fechaHasta).append("','" + ConstantesGenerales.FORMATO_FECHA + "')");
	}
	sql.append(filtro);
	sql.append(" and transa_id in ('").append(TransaccionNegocio.TOMA_DE_ORDEN).append("','");
	sql.append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA).append("')");

	dataSet = db.get(dataSource, sql.toString());
}

/**Lista las ordenes asociadas a una Unidad de Inversi&oacute;n espec&iacute;fica
 * @param idUnidadInversion
 * @throws Exception
 */
public void listarOrdenesPorUnidadInversionCount(long idUnidadInversion) throws Exception{
	StringBuffer sql = new StringBuffer();
	sql.append("SELECT count(*)as total_unidad_ordenes,sum(ordene_ped_total)as total from INFI_TB_204_ORDENES where uniinv_id = ").append(idUnidadInversion);
	sql.append(" and transa_id in('").append(TransaccionNegocio.TOMA_DE_ORDEN).append("','");
	sql.append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA).append("')");
	
	dataSet = db.get(dataSource, sql.toString());
	
	
}
/**
 * Muestra los totales por ordenes,monto(ordene_ped_monto) y vehiculos de una unidad de inversi&oacute;n
 * @param idUnidadInversion
 * @param vehiculoTom
 * @throws Exception
 */
public void resumenTotalesUnidadInversionVehiculo(long idUnidadInversion,String vehiculoTom) throws Exception{
	StringBuffer sql = new StringBuffer();
	sql.append("select unique a.ordene_veh_tom,INFI_TB_018_VEHICULOS.VEHICU_NOMBRE,INFI_TB_018_VEHICULOS.VEHICU_RIF,");
			sql.append(("(select count(*) from infi_tb_204_ordenes where ordene_veh_tom=a.ordene_veh_tom and uniinv_id="));
			sql.append(idUnidadInversion).append(")as ordenes,");
			sql.append("(select sum(ordene_ped_total) from infi_tb_204_ordenes where ordene_veh_tom=a.ordene_veh_tom and uniinv_id=");
			sql.append(idUnidadInversion);
			sql.append(")as monto_total ");
			sql.append("from infi_tb_204_ordenes a ");
			sql.append("left join INFI_TB_018_VEHICULOS on a.ordene_veh_tom=INFI_TB_018_VEHICULOS.VEHICU_ID ");
			sql.append("where a.ordene_veh_tom is not null and a.ordene_veh_tom<>'");
			sql.append(vehiculoTom);
			sql.append("' and a.uniinv_id=");
			sql.append(idUnidadInversion);
			sql.append("");
	dataSet = db.get(dataSource, sql.toString());
	
	}
/**
 * Muestra las ordenes en status adjudicadas y liquidadas de una unidad de inversi&oacute;n y blotter especifico
 * Modificaciones Inclusión de filtro por producto TTS-401. NM25287
 * @param long idUnidadInversion
 * @param String blotter
 * @throws Exception
 */
public void listarOrdenesLiquidacionBlotter(long idUnidadInversion,String []status,String tipoProducto) throws Exception{
	StringBuffer sql = new StringBuffer();
	Date fecha1 = new Date(Calendar.getInstance().getTimeInMillis());
	SimpleDateFormat formatter = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA_SIN_SEPARADOR_2);
	String fecha_funcion = formatter.format(fecha1);
	String fecha_re= ConstantesGenerales.FECHA_RECONVERSION;
	int fecha_reconversion= Integer.parseInt(fecha_re);
	int fecha_sistema= Integer.parseInt(fecha_funcion);
	System.out.println(fecha_funcion);
	System.out.println(fecha_sistema+" "+fecha_reconversion);
	System.out.println(fecha1);
	
//	if(fecha_sistema>=fecha_reconversion){
	
	
	/*sql.append("select ORDSTA_ID,count(ORDSTA_ID) ordenes,sum(ORDENE_PED_TOTAL) TOTAL,sum(monto_pendiente) MONTO_PENDIENTE,");
	sql.append("sum(monto_cobrado) MONTO_COBRADO from (select a.ordene_id ,ORDSTA_ID,a.ORDENE_PED_TOTAL,");
	sql.append("(select decode(sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION),null,0,sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION))as monto_operacion ");
	sql.append("from infi_tb_207_ordenes_operacion where infi_tb_207_ordenes_operacion.STATUS_OPERACION in('EN ESPERA','RECHAZADA') ");
	sql.append("and trnf_tipo='DEB' and infi_tb_207_ordenes_operacion.ordene_id=a.ordene_id) as monto_pendiente, ");
	sql.append("((select decode(sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION),null,0,sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION)) as monto_operacion ");
	sql.append("from infi_tb_207_ordenes_operacion where infi_tb_207_ordenes_operacion.STATUS_OPERACION in('APLICADA') and trnf_tipo='DEB' ");
	sql.append("and infi_tb_207_ordenes_operacion.ordene_id = a.ordene_id)-(select decode(sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION),null,0,");
	sql.append("sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION))as monto_operacion from infi_tb_207_ordenes_operacion ");
	sql.append("where infi_tb_207_ordenes_operacion.STATUS_OPERACION in('APLICADA') and trnf_tipo='CRE' and infi_tb_207_ordenes_operacion.ordene_id = a.ordene_id)) as monto_cobrado ");
	*/
	sql.append("SELECT ORDSTA_ID,COUNT (ORDSTA_ID) ordenes,SUM (MONTO_PENDIENTE+MONTO_COBRADO) TOTAL,SUM (monto_pendiente) MONTO_PENDIENTE,SUM (monto_cobrado) MONTO_COBRADO  ");
	sql.append("FROM INFI_TB_207_ORDENES_OPERACION,(SELECT a.ordene_id, ORDSTA_ID,(SELECT DECODE (SUM (infi_tb_207_ordenes_operacion.MONTO_OPERACION),NULL, 0,SUM (infi_tb_207_ordenes_operacion.MONTO_OPERACION)) AS monto_operacion FROM infi_tb_207_ordenes_operacion WHERE infi_tb_207_ordenes_operacion.STATUS_OPERACION IN ('EN ESPERA', 'RECHAZADA') AND trnf_tipo = 'DEB' AND infi_tb_207_ordenes_operacion.ordene_id =a.ordene_id) AS monto_pendiente, ");
	sql.append("((SELECT DECODE (SUM (infi_tb_207_ordenes_operacion.MONTO_OPERACION),NULL, 0,SUM (infi_tb_207_ordenes_operacion.MONTO_OPERACION))AS monto_operacion FROM infi_tb_207_ordenes_operacion WHERE infi_tb_207_ordenes_operacion.STATUS_OPERACION IN ('APLICADA') AND trnf_tipo = 'DEB' AND infi_tb_207_ordenes_operacion.ordene_id =a.ordene_id) ");
	sql.append("- (SELECT DECODE (SUM (infi_tb_207_ordenes_operacion.MONTO_OPERACION),NULL, 0,SUM (infi_tb_207_ordenes_operacion.MONTO_OPERACION)) AS monto_operacion FROM infi_tb_207_ordenes_operacion WHERE infi_tb_207_ordenes_operacion.STATUS_OPERACION IN ('APLICADA') AND trnf_tipo = 'CRE' AND infi_tb_207_ordenes_operacion.ordene_id =a.ordene_id)) AS monto_cobrado ");
	
	
	sql.append("from infi_tb_204_ordenes a where ORDSTA_ID in(");
	
	//Set de status al query
	for(int i=0;i<status.length;i++){

		if(i+1==status.length)
			sql.append("'").append(status[i]).append("')");
		else
			sql.append("'").append(status[i]).append("',");
	}

	sql.append(" and a.uniinv_id=").append(idUnidadInversion);
	sql.append(" and transa_id not in ('").append(TransaccionNegocio.ORDEN_VEHICULO).append("','");
	sql.append(TransaccionNegocio.LIQUIDACION).append("','").append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA);
	sql.append("') and tipo_producto_id ='").append(tipoProducto);
	sql.append("') group by ORDSTA_ID");
	
	System.out.println("listarOrdenesLiquidacionBlotter: "+sql.toString());
	dataSet = db.get(dataSource, sql.toString());
/*	}else{
		sql.append("SELECT ORDSTA_ID,COUNT (ORDSTA_ID) ordenes,SUM (MONTO_PENDIENTE+MONTO_COBRADO) TOTAL,SUM (monto_pendiente) MONTO_PENDIENTE,SUM (monto_cobrado) MONTO_COBRADO ");
		sql.append("FROM (SELECT a.ordene_id, ORDSTA_ID,(SELECT DECODE (SUM (infi_tb_207_ordenes_operacion.MONTO_OPERACION),NULL, 0,SUM (infi_tb_207_ordenes_operacion.MONTO_OPERACION)) AS monto_operacion FROM infi_tb_207_ordenes_operacion WHERE infi_tb_207_ordenes_operacion.STATUS_OPERACION IN ('EN ESPERA', 'RECHAZADA') AND trnf_tipo = 'DEB' AND infi_tb_207_ordenes_operacion.ordene_id =a.ordene_id) AS monto_pendiente, ");
		sql.append("((SELECT DECODE (SUM (infi_tb_207_ordenes_operacion.MONTO_OPERACION),NULL, 0,SUM (infi_tb_207_ordenes_operacion.MONTO_OPERACION))AS monto_operacion FROM infi_tb_207_ordenes_operacion WHERE infi_tb_207_ordenes_operacion.STATUS_OPERACION IN ('APLICADA') AND trnf_tipo = 'DEB' AND infi_tb_207_ordenes_operacion.ordene_id =a.ordene_id) ");
		sql.append("- (SELECT DECODE (SUM (infi_tb_207_ordenes_operacion.MONTO_OPERACION),NULL, 0,SUM (infi_tb_207_ordenes_operacion.MONTO_OPERACION)) AS monto_operacion FROM infi_tb_207_ordenes_operacion WHERE infi_tb_207_ordenes_operacion.STATUS_OPERACION IN ('APLICADA') AND trnf_tipo = 'CRE' AND infi_tb_207_ordenes_operacion.ordene_id =a.ordene_id)) AS monto_cobrado ");
		
		
		sql.append("from infi_tb_204_ordenes a where ORDSTA_ID in(");
		
		//Set de status al query
		for(int i=0;i<status.length;i++){

			if(i+1==status.length)
				sql.append("'").append(status[i]).append("')");
			else
				sql.append("'").append(status[i]).append("',");
		}

		sql.append(" and a.uniinv_id=").append(idUnidadInversion);
		sql.append(" and transa_id not in ('").append(TransaccionNegocio.ORDEN_VEHICULO).append("','");
		sql.append(TransaccionNegocio.LIQUIDACION).append("','").append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA);
		sql.append("') and tipo_producto_id ='").append(tipoProducto);
		sql.append("') group by ORDSTA_ID");
		
		System.out.println("listarOrdenesLiquidacionBlotter: "+sql.toString());
		dataSet = db.get(dataSource, sql.toString());	
	}*/
    }

/**
 * Muestra las ordenes en status adjudicadas y liquidadas de unidades de inversión SITME 
 * @throws Exception en caso de error 
 */
  public void listarOrdenesLiquidacion(long unidadInversion, String tipoProducto, String estatusOrdenes) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select undinv_id unidad_inversion_id,undinv_nombre,ORDSTA_ID,count(ORDSTA_ID) ordenes,sum(ORDENE_PED_TOTAL) TOTAL,sum(monto_pendiente) " +
				"MONTO_PENDIENTE,sum(monto_cobrado) MONTO_COBRADO,tipo_producto_id from (select c.undinv_id,c.undinv_nombre,a.ordene_id ,ORDSTA_ID," +
				"a.ORDENE_ADJ_MONTO,a.ORDENE_PED_TOTAL,(select decode(sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION)," +
				"null,0,sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION))as monto_operacion " +
				"from infi_tb_207_ordenes_operacion where " +
				"infi_tb_207_ordenes_operacion.STATUS_OPERACION in('" + ConstantesGenerales.STATUS_EN_ESPERA+ "','" + ConstantesGenerales.STATUS_RECHAZADA + "') " +
				"and trnf_tipo='"+  TransaccionFinanciera.DEBITO + "' and infi_tb_207_ordenes_operacion.ordene_id=a.ordene_id) + a.ORDENE_PED_TOTAL_PEND " +
				"as monto_pendiente, ((select decode(sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION)," +
				"null,0,sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION)) as monto_operacion " +
				"from infi_tb_207_ordenes_operacion where infi_tb_207_ordenes_operacion.STATUS_OPERACION in('" + ConstantesGenerales.STATUS_APLICADA + "') " +
				"and trnf_tipo='" + TransaccionFinanciera.DEBITO + "' and infi_tb_207_ordenes_operacion.ordene_id = a.ordene_id)-" +
				"(select decode(sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION),null,0," +
				"sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION))as monto_operacion from infi_tb_207_ordenes_operacion " +
				"where infi_tb_207_ordenes_operacion.STATUS_OPERACION in('" + ConstantesGenerales.STATUS_APLICADA + "') and trnf_tipo='" + TransaccionFinanciera.CREDITO + 
				"' and infi_tb_207_ordenes_operacion.ordene_id = a.ordene_id)) as monto_cobrado,d.tipo_producto_id " +
				"from infi_tb_204_ordenes a, infi_tb_201_ctes b, infi_tb_106_unidad_inversion c, infi_tb_101_inst_financieros d " +
				"where a.client_id = b.client_id and ORDSTA_ID ='" + estatusOrdenes+ "'" +
				
				
				
			" and a.uniinv_id = c.undinv_id and c.insfin_id = d.insfin_id " );
				
		System.out.println(sql);
		
				if (tipoProducto!=null) {
					sql.append("and d.tipo_producto_id='" +  tipoProducto + "'");
					if (tipoProducto.equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME)) {
						sql.append(" AND d.INSFIN_DESCRIPCION NOT IN ('" + ConstantesGenerales.INST_FINANCIERO_SITME_CLAVENET_P + "','" + ConstantesGenerales.INST_FINANCIERO_SITME_CLAVENET_E + "')");
					}
				}				
				if (unidadInversion > 0){
					sql.append(" and a.uniinv_id =" + unidadInversion);
				}
				sql.append(" and transa_id not in ('").append(TransaccionNegocio.ORDEN_VEHICULO).append("','");
				sql.append(TransaccionNegocio.LIQUIDACION).append("')) group by tipo_producto_id,undinv_id,undinv_nombre,ORDSTA_ID order by ORDSTA_ID");
				System.out.println("listarOrdenesLiquidacionSitme:  "+sql.toString());
				dataSet = db.get(dataSource, sql.toString());
	    }



/**
 * Lista las ordenes del proceso liquidaci&oacute;n por blotter que tenga status,unidad inversi&oacute;n y bloter especifico
 * @param String status
 * @param long idUnidadInversion
 * @param String blotter
 * @throws Exception
 */
public void listarOrdenesLiquidacionBlotterDetalles(String status,long idUnidadInversion,boolean paginado, int paginaAMostrar, int registroPorPagina) throws Exception{
		StringBuffer sql = new StringBuffer();
		Date fecha1 = new Date(Calendar.getInstance().getTimeInMillis());
		SimpleDateFormat formatter = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA_SIN_SEPARADOR_2);
		String fecha_funcion = formatter.format(fecha1);
		String fecha_re= ConstantesGenerales.FECHA_RECONVERSION;
		int fecha_reconversion= Integer.parseInt(fecha_re);
		int fecha_sistema= Integer.parseInt(fecha_funcion);
		System.out.println(fecha_funcion);
		System.out.println(fecha_sistema+" "+fecha_reconversion);
		System.out.println(fecha1);
		
		// SQL CON LA RECONVERSION MALA 
			/*sql.append("select a.ORDENE_PED_TOTAL_PEND,a.ordene_id , b.client_nombre, fvj8ocon0(a.ORDENE_ADJ_MONTO,'"+fecha_reconversion+"')ORDENE_ADJ_MONTO, fvj8ocon0(a.ORDENE_PED_TOTAL,'"+fecha_reconversion+"')ORDENE_PED_TOTAL,fvj8ocon0((select decode(sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION),null,0,");
			sql.append("sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION))as monto_operacion from infi_tb_207_ordenes_operacion where infi_tb_207_ordenes_operacion.STATUS_OPERACION in('EN ESPERA LIQUIDACION','EN ESPERA','RECHAZADA')");
			sql.append("and trnf_tipo='DEB' and infi_tb_207_ordenes_operacion.ordene_id=a.ordene_id) + a.ORDENE_PED_TOTAL_PEND,'20180512')monto_pendiente,");
			sql.append("fvj8ocon0(((select decode(sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION),null,0,sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION)) as monto_operacion from infi_tb_207_ordenes_operacion ");
			sql.append("where infi_tb_207_ordenes_operacion.STATUS_OPERACION in('APLICADA') and trnf_tipo='DEB' and infi_tb_207_ordenes_operacion.ordene_id = a.ordene_id)");
			sql.append("-(select decode(sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION),null,0,sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION))as monto_operacion ");
			sql.append("from infi_tb_207_ordenes_operacion where infi_tb_207_ordenes_operacion.STATUS_OPERACION in('APLICADA') ");
			sql.append("and trnf_tipo='CRE' and infi_tb_207_ordenes_operacion.ordene_id = a.ordene_id)),'"+fecha_reconversion+"') cobrado from infi_tb_204_ordenes a,infi_tb_201_ctes b  ");
			sql.append("where a.client_id = b.client_id and ORDSTA_ID='");
			sql.append(status);
			sql.append("' and a.uniinv_id=");
			sql.append(idUnidadInversion).append(" and transa_id !='").append(TransaccionNegocio.ORDEN_VEHICULO).append("'");
			if (paginado && paginaAMostrar>0 && registroPorPagina> 0){			
				dataSet = obtenerDataSetPaginado(sql.toString(),paginaAMostrar,registroPorPagina);
			}else{
				dataSet = db.get(dataSource, sql.toString());
			}*/
		//StringBuffer sql = new StringBuffer();  
		sql.append("select a.ORDENE_PED_TOTAL_PEND,a.ordene_id , b.client_nombre, a.ORDENE_ADJ_MONTO, a.ORDENE_PED_TOTAL,(select decode(sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION),null,0,sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION))as monto_operacion ");
		sql.append("from infi_tb_207_ordenes_operacion where infi_tb_207_ordenes_operacion.STATUS_OPERACION ");
		sql.append("in('EN ESPERA LIQUIDACION','EN ESPERA','RECHAZADA') and trnf_tipo='DEB' and infi_tb_207_ordenes_operacion.ordene_id=a.ordene_id) + a.ORDENE_PED_TOTAL_PEND as monto_pendiente, ");
		sql.append("((select decode(sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION),null,0,sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION)) as monto_operacion from infi_tb_207_ordenes_operacion ");
		sql.append("where infi_tb_207_ordenes_operacion.STATUS_OPERACION in('APLICADA') and trnf_tipo='DEB' and infi_tb_207_ordenes_operacion.ordene_id = a.ordene_id)");
		sql.append("-(select decode(sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION),null,0,sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION))as monto_operacion ");
		sql.append("from infi_tb_207_ordenes_operacion where infi_tb_207_ordenes_operacion.STATUS_OPERACION in('APLICADA') ");
		sql.append("and trnf_tipo='CRE' and infi_tb_207_ordenes_operacion.ordene_id = a.ordene_id))as cobrado from infi_tb_204_ordenes a,infi_tb_201_ctes b  ");
		sql.append("where a.client_id = b.client_id and ORDSTA_ID='");
		sql.append(status);
		sql.append("' and a.uniinv_id=");
		sql.append(idUnidadInversion).append(" and transa_id !='").append(TransaccionNegocio.ORDEN_VEHICULO).append("'");
		if (paginado && paginaAMostrar>0 && registroPorPagina> 0){			
			dataSet = obtenerDataSetPaginado(sql.toString(),paginaAMostrar,registroPorPagina);
		}else{
			dataSet = db.get(dataSource, sql.toString());
		}
			System.out.println("listarOrdenesLiquidacionBlotterDetalles "+sql);	
	
    }
/**
 * Actualiza la orden a status Liquidada
 * @param long ordenId
 * @throws Exception
 */
public void updateOrdenLiquidada(long ordenId) throws Exception{
	StringBuffer sql = new StringBuffer();
	sql.append("update infi_tb_204_ordenes set ordsta_id='").append(StatusOrden.LIQUIDADA).append("' ");
	sql.append("where ordene_id=").append(ordenId);
	db.exec(dataSource, sql.toString());
    }
/**
 * Query para saber el total pendiente por pagar para las ordenes por medio de las operaciones financieras
 * Es la suma de todas las operaciones financieras que cumplan con lo siguiente:STATUS_OPERACION(Espera o Rechazada)Debito
 * STATUS_OPERACION(Espera,aplicada o Rechazada)Bloqueo
 * @param long ordenId
 * @throws Exception
 * @RETURN BigDecimal montoOperacion
 */
public BigDecimal listarMontoTotalOperacionesPorOrden(long ordenId) throws Exception{
	StringBuffer sql = new StringBuffer();
	BigDecimal montoOperacion=new BigDecimal(0);
	sql.append("select (select sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION)as monto_operacion from infi_tb_207_ordenes_operacion where 1=1  ");
	sql.append("and((infi_tb_207_ordenes_operacion.STATUS_OPERACION='EN ESPERA LIQUIDACION')");
	sql.append("or (infi_tb_207_ordenes_operacion.STATUS_OPERACION='RECHAZADA')");
	sql.append("or (infi_tb_207_ordenes_operacion.STATUS_OPERACION='APLICADA')) ");
	sql.append("and infi_tb_207_ordenes_operacion.TRNF_TIPO='DEB' ");
	sql.append("and infi_tb_207_ordenes_operacion.ORDENE_ID=").append(ordenId);
	sql.append(") -(select ");
	sql.append("sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION)as credito ");
	sql.append("from infi_tb_207_ordenes_operacion where 1=1  ");
	sql.append("and infi_tb_207_ordenes_operacion.STATUS_OPERACION='APLICADA' ");
	sql.append("and infi_tb_207_ordenes_operacion.TRNF_TIPO='CRE' ");
	sql.append("and infi_tb_207_ordenes_operacion.ORDENE_ID=").append(ordenId);
	sql.append(")as monto_operacion ");
	sql.append("from infi_tb_204_ordenes ");
	sql.append("where infi_tb_204_ordenes.ORDENE_ID=").append(ordenId);

			dataSet=db.get(dataSource, sql.toString());
			if(dataSet.count()>0){
				dataSet.first();
				dataSet.next();
				if(dataSet.getValue("monto_operacion")!=null)
				montoOperacion = new BigDecimal(dataSet.getValue("monto_operacion"));
			}
			return montoOperacion;
    }

public BigDecimal listarMontoTotalOperacionesPorUnidad(long unidadInversionId) throws Exception{
	StringBuffer sql = new StringBuffer();
	BigDecimal montoOperacion=new BigDecimal(0);
	sql.append("select sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION)as monto_operacion from infi_tb_204_ordenes left join infi_tb_207_ordenes_operacion on infi_tb_207_ordenes_operacion.ordene_id = infi_tb_204_ordenes.ordene_id where 1=1 ");
			sql.append("and ((infi_tb_207_ordenes_operacion.STATUS_OPERACION='EN ESPERA')or (infi_tb_207_ordenes_operacion.STATUS_OPERACION='RECHAZADA'))");
			sql.append("and infi_tb_207_ordenes_operacion.TRNF_TIPO='DEB' ");
			sql.append("and infi_tb_204_ordenes.UNIINV_ID=").append(unidadInversionId).append(" or ");
			sql.append("((infi_tb_207_ordenes_operacion.STATUS_OPERACION='EN ESPERA')or ");
			sql.append("(infi_tb_207_ordenes_operacion.STATUS_OPERACION='RECHAZADA')or ");
			sql.append("(infi_tb_207_ordenes_operacion.STATUS_OPERACION='APLICADA')) ");
			sql.append("and infi_tb_207_ordenes_operacion.TRNF_TIPO='BLO' ");
			sql.append("and infi_tb_204_ordenes.UNIINV_ID=").append(unidadInversionId);

			dataSet=db.get(dataSource, sql.toString());
			if(dataSet.count()>0){
				dataSet.first();
				dataSet.next();
				if(dataSet.getValue("monto_operacion")!=null)
				montoOperacion = new BigDecimal(dataSet.getValue("monto_operacion"));
			}
			return montoOperacion;
    }
/**
 * Lista las ordenes que tengan monto pendiente por cobrar o financiadas con monto pendiente de una unidad de inversion
 * @param long unidadInversion
 * @throws Exception
 */
public void  listarOrdenesStatusCobranza(long unidadInversion,boolean paginado, int paginaAMostrar, int registroPorPagina) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select infi_tb_204_ordenes.BLOTER_ID,infi_tb_204_ordenes.ORDENE_ID,infi_tb_204_ordenes.CLIENT_ID,infi_tb_204_ordenes.ORDENE_ADJ_MONTO,infi_tb_204_ordenes.ORDENE_PED_TOTAL_PEND,");
		sql.append("(select decode(sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION),null,0,sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION))as monto_operacion from infi_tb_207_ordenes_operacion where ((infi_tb_207_ordenes_operacion.STATUS_OPERACION='EN ESPERA')or (infi_tb_207_ordenes_operacion.STATUS_OPERACION='RECHAZADA'))and infi_tb_207_ordenes_operacion.TRNF_TIPO='DEB' and infi_tb_207_ordenes_operacion.ORDENE_ID=infi_tb_204_ordenes.ORDENE_ID or ((infi_tb_207_ordenes_operacion.STATUS_OPERACION='EN ESPERA')or (infi_tb_207_ordenes_operacion.STATUS_OPERACION='RECHAZADA')or (infi_tb_207_ordenes_operacion.STATUS_OPERACION='APLICADA')) and infi_tb_207_ordenes_operacion.TRNF_TIPO='BLO' and infi_tb_207_ordenes_operacion.ORDENE_ID=infi_tb_204_ordenes.ORDENE_ID) as pendiente,infi_tb_201_ctes.CLIENT_NOMBRE,infi_tb_102_bloter.BLOTER_DESCRIPCION from infi_tb_204_ordenes ");
		sql.append("left join infi_tb_201_ctes on infi_tb_204_ordenes.CLIENT_ID=infi_tb_201_ctes.CLIENT_ID ");
		sql.append("left join infi_tb_102_bloter on infi_tb_204_ordenes.BLOTER_ID=infi_tb_102_bloter.BLOTER_ID ");
		sql.append("where infi_tb_204_ordenes.UNIINV_ID=");
		sql.append(unidadInversion);
		sql.append(" and infi_tb_204_ordenes.transa_id not in('ORDEN_VEHICULO','LIQUIDACION')");
		sql.append(" order by infi_tb_102_bloter.BLOTER_DESCRIPCION,infi_tb_204_ordenes.ORDENE_ID desc");
		if (paginado && paginaAMostrar>0 && registroPorPagina> 0){			
			dataSet = obtenerDataSetPaginado(sql.toString(),paginaAMostrar,registroPorPagina);
		}else{
			dataSet = db.get(dataSource, sql.toString());
		}
    }

public void  listarOrdenesPorCobrarComision(long unidadInversion, String blotter) throws Exception{
	StringBuffer sql = new StringBuffer();
	long tiempoEjec=System.currentTimeMillis();
	
	/*sql.append("select distinct(a.ORDENE_ID), a.client_id, CONVERTIR_CUENTA_20A12(a.ctecta_numero) cuenta,");
	sql.append(" ORDENE_PED_COMISIONES monto_operacion ");
	sql.append(" from infi_tb_204_ordenes a, infi_tb_106_unidad_inversion c");
	sql.append(" where a.UNIINV_ID = c.UNDINV_ID and   a.UNIINV_ID =" +unidadInversion+	" and a.transa_id in('"+com.bancovenezuela.comun.logic.interfaces.ConstantesGenerales.TRANSACCION_TOMA_ORDEN+"','"+com.bancovenezuela.comun.logic.interfaces.ConstantesGenerales.TOMA_ORDEN_CARTERA_PROPIA+"')");
	sql.append(" and   a.ordsta_id ='"+ConstantesGenerales.UNINV_ADJUDICADA+"' and   nvl(a.ORDENE_ADJ_MONTO,0)>0 ");
	sql.append(" and   a.ordene_id in (select distinct b.ordene_id");
	sql.append(" from   infi_tb_207_ordenes_operacion b");
	sql.append(" where  b.ordene_id = a.ordene_id");
	sql.append(" and    b.trnf_tipo != '"+TransaccionFinanciera.BLOQUEO+"'");
	sql.append(" and    b.ordene_relac_operacion_id is null)");*/
	
	//ITS-617
	sql.append("SELECT DISTINCT (a.ORDENE_ID),a.client_id,CONVERTIR_CUENTA_20A12 (a.ctecta_numero) cuenta,ORDENE_PED_COMISIONES monto_operacion ");
	sql.append("FROM infi_tb_204_ordenes a WHERE     a.UNIINV_ID =").append(unidadInversion).append(" ");
	sql.append("AND a.transa_id ='").append(com.bancovenezuela.comun.logic.interfaces.ConstantesGenerales.TRANSACCION_TOMA_ORDEN).append("' AND a.ordsta_id IN ('").append(StatusOrden.ADJUDICADA).append("','").append(StatusOrden.PROCESO_ADJUDICACION).append("') ");
	sql.append("AND a.ordene_id IN (SELECT DISTINCT b.ordene_id FROM infi_tb_207_ordenes_operacion b WHERE     b.ordene_id = a.ordene_id ");
	sql.append("AND B.STATUS_OPERACION <> '").append(com.bancovenezuela.comun.logic.interfaces.ConstantesGenerales.STATUS_APLICADA).append("' AND B.TRNFIN_ID = 11 AND B.TRNF_TIPO = 'DEB') ");
	
	if(!blotter.equals("-1")){
		sql.append(" and a.bloter_id='").append(blotter).append("'");
	}	
	
	
	sql.append(" ORDER BY A.ORDENE_ID ");
	//System.out.println("listarOrdenesPorCobrarComision " + sql.toString());
	
	this.dataSet =db.get(dataSource, sql.toString());
	
	logger.info("listarOrdenesPorCobrarComision: tiempo de ejecución="+(System.currentTimeMillis()-tiempoEjec)+"mseg ."+sql);
}

public void  listarOrdenesPorCobrarCapital(long unidadInversion, String blotter) throws Exception{
	StringBuffer sql = new StringBuffer();
	long tiempoEjec=System.currentTimeMillis();
	
	
	/*sql.append("select distinct(a.ORDENE_ID), a.client_id, CONVERTIR_CUENTA_20A12(a.ctecta_numero) cuenta,");
	sql.append(" ((ORDENE_PED_PRECIO/100)*(c.UNDINV_TASA_CAMBIO*nvl(ORDENE_ADJ_MONTO,0))) monto_operacion");
	sql.append(" from infi_tb_204_ordenes a, infi_tb_106_unidad_inversion c");
	sql.append(" where a.UNIINV_ID = c.UNDINV_ID and   a.UNIINV_ID =" +unidadInversion+	" and a.transa_id in('"+com.bancovenezuela.comun.logic.interfaces.ConstantesGenerales.TRANSACCION_TOMA_ORDEN+"','"+com.bancovenezuela.comun.logic.interfaces.ConstantesGenerales.TOMA_ORDEN_CARTERA_PROPIA+"')");
	sql.append(" and   a.ordsta_id ='"+ConstantesGenerales.UNINV_ADJUDICADA+"' and   nvl(a.ORDENE_ADJ_MONTO,0)>0 ");
	sql.append(" and   a.ordene_id in (select distinct b.ordene_id");
	sql.append(" from   infi_tb_207_ordenes_operacion b");
	sql.append(" where  b.ordene_id = a.ordene_id");
	sql.append(" and    b.trnf_tipo != '"+TransaccionFinanciera.BLOQUEO+"'");
	sql.append(" and    b.ordene_relac_operacion_id is null)");*/

	//ITS-617
	sql.append("SELECT DISTINCT (a.ORDENE_ID), a.client_id, A.ORDSTA_ID,CONVERTIR_CUENTA_20A12 (a.ctecta_numero) cuenta, ");
	sql.append("((ORDENE_PED_PRECIO / 100)* (c.UNDINV_TASA_CAMBIO * NVL (ORDENE_ADJ_MONTO, 0))) monto_operacion ");
	sql.append("FROM infi_tb_204_ordenes a, infi_tb_106_unidad_inversion c WHERE     a.UNIINV_ID = c.UNDINV_ID ");
	sql.append("AND a.UNIINV_ID =").append(unidadInversion).append(" AND a.transa_id = '").append(com.bancovenezuela.comun.logic.interfaces.ConstantesGenerales.TRANSACCION_TOMA_ORDEN).append("' ");
	sql.append("AND a.ORDSTA_ID IN ('").append(StatusOrden.ADJUDICADA).append("','").append(StatusOrden.PROCESO_ADJUDICACION).append("') ");
	sql.append("AND NVL (a.ORDENE_ADJ_MONTO, 0) > 0 AND a.ordene_id IN (");
	sql.append("SELECT DISTINCT b.ordene_id FROM infi_tb_207_ordenes_operacion b WHERE     b.ordene_id = a.ordene_id ");
	sql.append("AND b.trnf_tipo ='DEB'  AND b.TRNFIN_ID IN (1,2) AND B.STATUS_OPERACION <> '").append(com.bancovenezuela.comun.logic.interfaces.ConstantesGenerales.STATUS_APLICADA).append("')");
	
	if(!blotter.equals("-1")){
		sql.append(" and a.bloter_id='").append(blotter).append("'");
	}	
	sql.append(" ORDER BY a.ORDENE_ID ");
	//System.out.println("listarOrdenesPorCobrarCapital " + sql.toString());
	dataSet=db.get(dataSource, sql.toString());
	logger.info("listarOrdenesPorCobrarCapital: tiempo de ejecución="+(System.currentTimeMillis()-tiempoEjec)+"mseg ."+sql);//+sql
		
}

public void listarOrdenesTipo(long unidadInversion, String blotter, String tipoCuenta, String status) throws Exception{
	StringBuffer sql = new StringBuffer();
	long tiempoEjec=System.currentTimeMillis();
	
	sql.append("select o.ORDENE_ID, substr(CONVERTIR_CUENTA_20A12(o.CTECTA_NUMERO),3,10) CTECTA_NUMERO , ui.UNDINV_NOMBRE,decode(substr(o.CTECTA_NUMERO,11,2),'00','CORRIENTE','AHORRO') as tipo_cuenta,");
	sql.append(" case ");
	sql.append(" when (o.ORDENE_ADJ_MONTO=0 or (o.ORDENE_ADJ_MONTO>0 and o.ORDENE_ADJ_MONTO<o.ORDENE_PED_MONTO)) then (select sum(oo.MONTO_OPERACION) from infi_tb_207_ordenes_operacion oo where oo.ORDENE_ID=o.ORDENE_ID and oo.TRNF_TIPO='BLO') ");
	sql.append(" when (o.ORDENE_ADJ_MONTO>0 and o.ORDENE_ADJ_MONTO=o.ORDENE_PED_MONTO) then  (((o.ORDENE_PED_PRECIO/100)*(ui.UNDINV_TASA_CAMBIO*nvl(o.ORDENE_ADJ_MONTO,0)))+o.ORDENE_PED_COMISIONES) ");
	sql.append(" end as monto_desbloqueo, ");
	sql.append(" case ");
	sql.append(" when (o.ORDENE_ADJ_MONTO=0) then 0 ");
	sql.append(" when (o.ORDENE_ADJ_MONTO>0) then ((o.ORDENE_PED_PRECIO/100)*(ui.UNDINV_TASA_CAMBIO*nvl(o.ORDENE_ADJ_MONTO,0))) ");
	sql.append(" end as monto_capital, ");
	sql.append(" case ");
	sql.append(" when (o.ORDENE_ADJ_MONTO=0) then 0 ");
	sql.append(" when (o.ORDENE_ADJ_MONTO>0)  then o.ORDENE_PED_COMISIONES ");
	sql.append(" end as monto_comision, ");
	sql.append(" '"+TransaccionFinanciera.DEBITO+"' as tipo_operacion  ");
	sql.append(" from infi_tb_111_ui_blotter_rangos br, infi_tb_204_ordenes o, infi_tb_106_unidad_inversion ui, infi_tb_201_ctes ct");
	sql.append(" where ui.UNDINV_ID = br.UNDINV_ID and o.BLOTER_ID = br.BLOTER_ID   ");
	//sql.append(" and ct.TIPPER_ID = br.TIPPER_ID and o.transa_id in ('"+com.bancovenezuela.comun.logic.interfaces.ConstantesGenerales.TRANSACCION_TOMA_ORDEN+"','"+com.bancovenezuela.comun.logic.interfaces.ConstantesGenerales.TOMA_ORDEN_CARTERA_PROPIA+"') ");
	sql.append(" and ct.TIPPER_ID = br.TIPPER_ID and o.transa_id = '"+com.bancovenezuela.comun.logic.interfaces.ConstantesGenerales.TRANSACCION_TOMA_ORDEN+"' ");
	//sql.append(" and o.UNIINV_ID = ui.UNDINV_ID and ct.CLIENT_ID = o.CLIENT_ID and o.ordsta_id = '"+ConstantesGenerales.UNINV_ADJUDICADA+"' and o.ORDENE_ADJ_MONTO is not null");
	sql.append(" and o.UNIINV_ID = ui.UNDINV_ID and ct.CLIENT_ID = o.CLIENT_ID and o.ordsta_id IN ('"+StatusOrden.ADJUDICADA+"','").append(StatusOrden.PROCESO_ADJUDICACION).append("')").append(" and o.ORDENE_ADJ_MONTO is not null"); 
	sql.append(" and br.UIBLOT_TRNFIN_TIPO='"+status+"' and br.UNDINV_ID="+unidadInversion+" and  substr(o.CTECTA_NUMERO,11,2)="+tipoCuenta);
	if(!blotter.equals("-1")){
		sql.append(" and o.bloter_id='").append(blotter).append("'");
	}
		//System.out.println("listarOrdenesTipo " + sql.toString());
		
	dataSet=db.get(dataSource, sql.toString());
		//System.out.println("DATA SET " + dataSet) ;
	logger.info("listarOrdenesBloqueo: tiempo de ejecución="+(System.currentTimeMillis()-tiempoEjec)+"mseg ."+sql);//+sql
}

public void  listarOrdenesPorCobrarBloqueo(long unidadInversion, String blotter) throws Exception{
	StringBuffer sql = new StringBuffer();
	long tiempoEjec=System.currentTimeMillis();
	sql.append(" select o.CTECTA_NUMERO,");
	sql.append(" (ct.TIPPER_ID||lpad(ct.CLIENT_CEDRIF,8,'0')) AS CEDULA_RIF,br.UIBLOT_TRNFIN_TIPO,");
	sql.append(" case "); //MONTO DE DESBLOQUEO 
	sql.append(" when ((br.UIBLOT_TRNFIN_TIPO='MIS' or br.UIBLOT_TRNFIN_TIPO='DEB')) then 0 ");
	sql.append(" when ((br.UIBLOT_TRNFIN_TIPO='BLO' and o.ORDENE_ADJ_MONTO=0) or (br.UIBLOT_TRNFIN_TIPO='BLO' and (o.ORDENE_ADJ_MONTO>0 and o.ORDENE_ADJ_MONTO<o.ORDENE_PED_MONTO)) ) then"); 
	sql.append(" (select sum(oo.MONTO_OPERACION) from infi_tb_207_ordenes_operacion oo where oo.ORDENE_ID=o.ORDENE_ID and oo.TRNF_TIPO='BLO') ");
	sql.append(" when (br.UIBLOT_TRNFIN_TIPO='BLO' and o.ORDENE_ADJ_MONTO>0 and o.ORDENE_ADJ_MONTO=o.ORDENE_PED_MONTO) then ");
	sql.append(" (((o.ORDENE_PED_PRECIO/100)*(ui.UNDINV_TASA_CAMBIO*nvl(o.ORDENE_ADJ_MONTO,0)))+o.ORDENE_PED_COMISIONES) ");
	sql.append(" end as monto_desbloqueo");
	sql.append(", case ");//MONTO DE CAPITAL
	sql.append(" when ((o.ORDENE_ADJ_MONTO=0 and br.UIBLOT_TRNFIN_TIPO !='DEB') or (br.UIBLOT_TRNFIN_TIPO ='DEB' and o.ORDENE_ADJ_MONTO>0 and o.ORDENE_ADJ_MONTO=o.ORDENE_PED_MONTO)) then 0");
	sql.append(" when (o.ORDENE_ADJ_MONTO=0 and (br.UIBLOT_TRNFIN_TIPO ='DEB')) then o.ORDENE_PED_MONTO ");
	sql.append(" when (o.ORDENE_ADJ_MONTO>0) then ((o.ORDENE_PED_PRECIO/100)*(ui.UNDINV_TASA_CAMBIO*nvl(o.ORDENE_ADJ_MONTO,0)))");
	sql.append(" when (br.UIBLOT_TRNFIN_TIPO ='DEB' and o.ORDENE_ADJ_MONTO>0 and o.ORDENE_ADJ_MONTO<o.ORDENE_PED_MONTO) then ");
	sql.append(" (select sum(oo.MONTO_OPERACION) from infi_tb_207_ordenes_operacion oo where oo.ORDENE_ID=o.ORDENE_ID and oo.TRNF_TIPO='CRD' and oo.IN_COMISION=0)"); 
	sql.append(" end as monto_capital,");
	sql.append(" case ");//MONTO DE COMISION
	sql.append(" when ((o.ORDENE_ADJ_MONTO=0 and br.UIBLOT_TRNFIN_TIPO !='DEB') or (br.UIBLOT_TRNFIN_TIPO ='DEB' and o.ORDENE_ADJ_MONTO>0 and o.ORDENE_ADJ_MONTO=o.ORDENE_PED_MONTO)) then 0");
	sql.append(" when ((o.ORDENE_ADJ_MONTO>0 and br.UIBLOT_TRNFIN_TIPO !='DEB') or (br.UIBLOT_TRNFIN_TIPO ='DEB' and o.ORDENE_ADJ_MONTO=0)) then o.ORDENE_PED_COMISIONES");
	sql.append(" when (br.UIBLOT_TRNFIN_TIPO ='DEB' and o.ORDENE_ADJ_MONTO>0 and o.ORDENE_ADJ_MONTO<o.ORDENE_PED_MONTO) then ");
	sql.append(" (select sum(oo.MONTO_OPERACION) from infi_tb_207_ordenes_operacion oo where oo.ORDENE_ID=o.ORDENE_ID and oo.TRNF_TIPO='CRD' and oo.IN_COMISION=1)");	
	sql.append(" end as monto_comision,");
	sql.append(" case ");//TIPO OPERACION
	sql.append(" when br.UIBLOT_TRNFIN_TIPO ='DEB' then 'CRD'");
	sql.append(" else 'DEB' ");
	sql.append(" end as tipo_operacion");
	sql.append(" from infi_tb_111_ui_blotter_rangos br,");
	sql.append(" infi_tb_204_ordenes o, infi_tb_106_unidad_inversion ui, infi_tb_201_ctes ct");
	sql.append(" where ui.UNDINV_ID = br.UNDINV_ID and o.BLOTER_ID = br.BLOTER_ID ");
	sql.append(" and ct.TIPPER_ID = br.TIPPER_ID and o.transa_id in ('TOMA_ORDEN','TOMA_ORDEN_CARTERA_PROPIA') ");
	sql.append(" and o.UNIINV_ID = ui.UNDINV_ID and ct.CLIENT_ID = o.CLIENT_ID and o.ordsta_id = 'ADJUDICADA' ");
	sql.append(" and br.UNDINV_ID=").append(unidadInversion);	
	if(!blotter.equals("-1")){
		sql.append(" and o.bloter_id='").append(blotter).append("'");
	}
	sql.append(" order by br.UIBLOT_TRNFIN_TIPO");

	dataSet=db.get(dataSource, sql.toString());
	logger.info("listarOrdenesPorCobrarBloqueo: tiempo de ejecución="+(System.currentTimeMillis()-tiempoEjec)+"mseg .");//+sql
}


public void  listarOrdenesPorPagar(int unidadInversion, String tipoTRN, String tipoOpe, String fecha) throws Exception{
	StringBuffer sql = new StringBuffer();
	sql.append("{call BDV_DEBCRDMAV("+unidadInversion+",'"+tipoTRN+"','"+tipoOpe+"',"+fecha+"}");
	try {
		conn = dataSource.getConnection();		
		CallableStatement callStmt = conn.prepareCall(sql.toString());
		callStmt.execute();
				
	}catch(Exception e){
		e.printStackTrace();
	}
	//dataSet=db.get(dataSource, sql.toString());
}

/**
 * Lista los montos cobrados por capital y comision de las ordenes por unidad de inversion y bloter
 * @param long unidadInversion
 * @param String bloter
 * @throws Exception
 */
public void  listarOrdenesBlotterUnidadInversion(long unidadInversion,String bloter) throws Exception{
	StringBuffer sql = new StringBuffer();
	sql.append("select a.ordene_id," +
			"(select sum(monto_operacion) from infi_tb_207_ordenes_operacion where infi_tb_207_ordenes_operacion.ORDENE_ID=a.ordene_id and infi_tb_207_ordenes_operacion.IN_COMISION=1 and infi_tb_207_ordenes_operacion.STATUS_OPERACION='APLICADA' and infi_tb_207_ordenes_operacion.TRNF_TIPO='DEB')as monto_comision," +
			"(select sum(monto_operacion) from infi_tb_207_ordenes_operacion where infi_tb_207_ordenes_operacion.ORDENE_ID=a.ordene_id and infi_tb_207_ordenes_operacion.IN_COMISION<>1 and infi_tb_207_ordenes_operacion.STATUS_OPERACION='APLICADA' and infi_tb_207_ordenes_operacion.TRNF_TIPO='DEB')as capital " +
			"from infi_tb_204_ordenes a where 1=1 and a.bloter_id is not null ");
		if(unidadInversion!=0)
			sql.append(" and a.UNIINV_ID=").append(unidadInversion);
		if(bloter!=null)
			sql.append(" and infi_tb_204_ordenes.bloter_id='").append(bloter).append("'");
	dataSet=db.get(dataSource, sql.toString());
	}
	/**
	 * Lista los totales de ordenes y monto mas comision de un blotter especifico
	 * @param String bloter
	 * @throws Exception
	 */
	public void  listarMontoOrdenesTotalBlotter(String bloter,long unidadInversion) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select count(infi_tb_204_ordenes.ordene_id)as ordenes,sum(infi_tb_204_ordenes.ORDENE_PED_TOTAL)as monto from infi_tb_204_ordenes where infi_tb_204_ordenes.BLOTER_ID='");
		sql.append(bloter).append("'").append(" and UNIINV_ID=").append(unidadInversion);
		dataSet=db.get(dataSource, sql.toString());
	}
	
	/**Metodo que lista las ordenes que pueden ser canceladas
	 * @param orden, Numero de la orden
	 * @param cliente, Id del cliente
	 * @param unidad_inversion, ID de la unidad de inversion
	 * @param fecha desde, fecha en que se realizo la orden
	 * @param fecha hasta, fecha en que se realizo la orden
	 * */	
	public void listarOrdenesParaCancelar(String orden,String cliente,String unidad_inversion,String fe_ord_desde,String fe_ord_hasta) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		if(orden!=null){
			filtro.append(" and o.ordene_id = ").append(orden);
		}
		if(cliente!=null){
			filtro.append(" and c.client_id = ").append(cliente);
		}
		if(unidad_inversion!=null){
			filtro.append(" and o.uniinv_id = ").append(unidad_inversion);
		}
		if(fe_ord_hasta!=null && fe_ord_desde!=null){
			filtro.append(" and o.ordene_ped_fe_orden between to_date('").append(fe_ord_desde).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("') and to_date('").append(fe_ord_hasta).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("')");
		}
		sql.append("select fo.insfin_forma_orden, o.ordene_id, o.ordene_ped_fe_orden, c.client_nombre, u.undinv_id, u.undinv_nombre, u.undinv_serie, o.ordene_ped_monto  from INFI_TB_204_ORDENES o, INFI_TB_106_UNIDAD_INVERSION u, INFI_TB_101_INST_FINANCIEROS fi, INFI_TB_038_INST_FORMA_ORDEN fo, INFI_TB_201_CTES c where o.transa_id IN ('").append(TransaccionNegocio.TOMA_DE_ORDEN).append("','").append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA).append("')").append(" and ordsta_id IN('").append(StatusOrden.REGISTRADA).append("', '").append(StatusOrden.ENVIADA).append("','").append(StatusOrden.PENDIENTE).append("') and o.uniinv_id=u.undinv_id and u.insfin_id=fi.insfin_id and fi.insfin_forma_orden=fo.insfin_forma_orden and (fo.insfin_forma_orden='").append(ConstantesGenerales.INST_TIPO_SUBASTA).append("' or fo.insfin_forma_orden='").append(ConstantesGenerales.INST_TIPO_SUBASTA_COMPETITIVA).append("') and c.client_id=o.client_id");	
		sql.append(filtro);
		sql.append(" UNION ");
		sql.append("select fo.insfin_forma_orden, o.ordene_id, o.ordene_ped_fe_orden, c.client_nombre, u.undinv_id , u.undinv_nombre, u.undinv_serie, o.ordene_ped_monto from INFI_TB_204_ORDENES o, INFI_TB_106_UNIDAD_INVERSION u, INFI_TB_101_INST_FINANCIEROS fi, INFI_TB_038_INST_FORMA_ORDEN fo, INFI_TB_201_CTES c where o.transa_id IN ('").append(TransaccionNegocio.TOMA_DE_ORDEN).append("','").append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA).append("')").append(" and ordsta_id IN('").append(StatusOrden.REGISTRADA).append("','").append(StatusOrden.PENDIENTE).append("') and o.uniinv_id=u.undinv_id and u.insfin_id=fi.insfin_id and fi.insfin_forma_orden=fo.insfin_forma_orden and (fo.insfin_forma_orden='").append(ConstantesGenerales.INST_TIPO_INVENTARIO).append("' or fo.insfin_forma_orden='").append(ConstantesGenerales.INST_TIPO_INVENTARIO_CON_PRECIO).append("') and o.ordene_ped_fe_orden = to_date(sysdate,'").append(ConstantesGenerales.FORMATO_FECHA).append("') and c.client_id=o.client_id");
		sql.append(filtro);
		sql.append(" order by 3 desc, 1 asc");
		dataSet = db.get(dataSource, sql.toString());
	}

	
/**
 * Lista las ordenes asociadas a una unidad de inversión, con los parametros especificados
 * @param unidad_inversion
 * @param blotter
 * @param fecha_desde
 * @param fecha_hasta
 * @throws Exception
 */
/*public void listarOrdenesPorBloter(long unidad_inversion,String blotter,String fecha_desde, String fecha_hasta)throws Exception{
	StringBuffer sb= new StringBuffer();
	StringBuffer filtro = new StringBuffer("");
	sb.append("select  infi_tb_101_inst_financieros.INSFIN_DESCRIPCION, infi_tb_106_unidad_inversion.UNDINV_EMISION,infi_tb_106_unidad_inversion.UNDINV_NOMBRE,infi_tb_106_unidad_inversion.UNDINV_SERIE, ");
	sb.append("infi_tb_204_ordenes.ORDENE_ID, infi_tb_201_ctes.CLIENT_CEDRIF, 		infi_tb_201_ctes.CLIENT_NOMBRE,infi_tb_102_bloter.BLOTER_DESCRIPCION,");
	sb.append("infi_tb_204_ordenes.ORDENE_PED_PRECIO,infi_tb_204_ordenes.ORDENE_FINANCIADO,");
	sb.append("infi_tb_204_ordenes.ORDENE_PED_TOTAL_PEND,infi_tb_201_ctes.CLIENT_CTA_CUSTOD_ID, ");
	sb.append("to_char(infi_tb_204_ordenes.ORDENE_PED_FE_ORDEN, '").append(ConstantesGenerales.FORMATO_FECHA_HORA_BD).append("') as ORDENE_PED_FE_ORDEN, to_char(infi_tb_204_ordenes.ORDENE_PED_FE_VALOR,'").append(ConstantesGenerales.FORMATO_FECHA_HORA_BD).append("') as ORDENE_PED_FE_VALOR, ");
	sb.append("infi_tb_204_ordenes.ORDENE_USR_SUCURSAL,infi_tb_201_ctes.CLIENT_DIRECCION,infi_tb_201_ctes.CLIENT_TELEFONO,");
	sb.append("infi_tb_204_ordenes.ORDENE_USR_NOMBRE,infi_tb_204_ordenes.ORDENE_PED_MONTO,");
	sb.append("(infi_tb_204_ordenes.ORDENE_PED_MONTO*infi_tb_106_unidad_inversion.UNDINV_TASA_CAMBIO)as VALOR_NOMINAL_BOLIVARES,");
	sb.append("(select count(*) from infi_tb_207_ordenes_operacion where ordene_id=infi_tb_204_ordenes.ordene_id)as operaciones, 		(select sum (infi_tb_207_ordenes_operacion.MONTO_OPERACION)  from infi_tb_204_ordenes od ");
	sb.append("left join  infi_tb_207_ordenes_operacion on od.ORDENE_ID=infi_tb_207_ordenes_operacion.ORDENE_ID  where od.ORDENE_PED_FE_VALOR is not null and od.BLOTER_ID is not null ");
	sb.append("and infi_tb_204_ordenes.ORDENE_ID=od.ORDENE_ID)as total ");
	sb.append("from infi_tb_204_ordenes ");
	sb.append("left join  infi_tb_201_ctes on infi_tb_201_ctes.CLIENT_ID=infi_tb_204_ordenes.CLIENT_ID ");
	sb.append("left join infi_tb_701_titulos on infi_tb_204_ordenes.CLIENT_ID=infi_tb_701_titulos.CLIENT_ID ");
	sb.append("left join infi_tb_100_titulos on trim (infi_tb_701_titulos.TITULO_ID)=trim (infi_tb_100_titulos.TITULO_ID) ");
	sb.append("left join infi_tb_106_unidad_inversion on infi_tb_204_ordenes.UNIINV_ID=infi_tb_106_unidad_inversion.UNDINV_ID ");
	sb.append("left join infi_tb_101_inst_financieros on 		infi_tb_106_unidad_inversion.INSFIN_ID=infi_tb_101_inst_financieros.INSFIN_ID ");
	sb.append("left join  infi_tb_102_bloter on infi_tb_204_ordenes.BLOTER_ID=infi_tb_102_bloter.BLOTER_ID ");
	sb.append("where infi_tb_204_ordenes.ORDENE_PED_FE_VALOR is not null and infi_tb_204_ordenes.BLOTER_ID is not null");
    sb.append(" and ordsta_id not in('").append(StatusOrden.CANCELADA).append("','").append(StatusOrden.PENDIENTE).append("')");
   if(unidad_inversion!=0){
		filtro.append(" and infi_tb_204_ordenes.UNIINV_ID=").append(unidad_inversion);
	}
	if(blotter!=null && !blotter.equalsIgnoreCase("")){
		filtro.append(" and infi_tb_204_ordenes.BLOTER_ID='").append(blotter).append("' ");
	}
	if(fecha_hasta!=null && fecha_desde!=null){
		filtro.append(" and infi_tb_204_ordenes.ordene_ped_fe_orden between to_date('").append(fecha_desde).append("','" + ConstantesGenerales.FORMATO_FECHA + "') and to_date('").append(fecha_hasta).append("','" + ConstantesGenerales.FORMATO_FECHA + "') ");
	}
	sb.append(filtro);
	sb.append(" group by infi_tb_101_inst_financieros.INSFIN_DESCRIPCION,infi_tb_106_unidad_inversion.UNDINV_EMISION,infi_tb_106_unidad_inversion.UNDINV_NOMBRE,infi_tb_106_unidad_inversion.UNDINV_SERIE,infi_tb_204_ordenes.ORDENE_ID,infi_tb_201_ctes.CLIENT_CEDRIF,infi_tb_201_ctes.CLIENT_NOMBRE,infi_tb_102_bloter.BLOTER_DESCRIPCION,infi_tb_204_ordenes.ORDENE_PED_PRECIO,infi_tb_204_ordenes.ORDENE_FINANCIADO,infi_tb_204_ordenes.ORDENE_PED_TOTAL_PEND,infi_tb_201_ctes.CLIENT_CTA_CUSTOD_ID,infi_tb_204_ordenes.ORDENE_PED_FE_ORDEN,infi_tb_204_ordenes.ORDENE_PED_FE_VALOR,infi_tb_201_ctes.CLIENT_DIRECCION,infi_tb_201_ctes.CLIENT_TELEFONO,infi_tb_204_ordenes.ORDENE_USR_NOMBRE,infi_tb_204_ordenes.ORDENE_PED_MONTO,infi_tb_106_unidad_inversion.UNDINV_TASA_CAMBIO,infi_tb_204_ordenes.ORDENE_USR_SUCURSAL ");	
	sb.append(" order by infi_tb_204_ordenes.ordene_id desc");
	//System.out.println("SQL Exportar: "+sb.toString());
	dataSet= db.get(dataSource, sb.toString());
	
}*/


	public void listarOrdenesPorBloter(long unidad_inversion,String blotter,String fecha_desde, String fecha_hasta, boolean paginado, int paginaAMostrar, int registroPorPagina)throws Exception{
		StringBuffer sb= new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		sb.append("select distinct infi_tb_101_inst_financieros.INSFIN_DESCRIPCION, infi_tb_106_unidad_inversion.UNDINV_EMISION,infi_tb_106_unidad_inversion.UNDINV_NOMBRE,infi_tb_106_unidad_inversion.UNDINV_SERIE, infi_tb_106_unidad_inversion.UNDINV_TASA_CAMBIO,");
		sb.append("infi_tb_204_ordenes.ORDENE_ID, infi_tb_201_ctes.CLIENT_CEDRIF,infi_tb_201_ctes.tipper_id, infi_tb_201_ctes.CLIENT_NOMBRE,infi_tb_102_bloter.BLOTER_DESCRIPCION,");
		sb.append("infi_tb_204_ordenes.ORDENE_PED_PRECIO,infi_tb_204_ordenes.ORDENE_FINANCIADO,");
		sb.append("infi_tb_204_ordenes.ORDENE_PED_TOTAL_PEND,infi_tb_201_ctes.CLIENT_CTA_CUSTOD_ID, ");
		sb.append("to_char(infi_tb_204_ordenes.ORDENE_PED_FE_ORDEN, '").append(ConstantesGenerales.FORMATO_FECHA_HORA_BD).append("') as ORDENE_PED_FE_ORDEN, to_char(infi_tb_204_ordenes.ORDENE_PED_FE_VALOR,'").append(ConstantesGenerales.FORMATO_FECHA_HORA_BD).append("') as ORDENE_PED_FE_VALOR, ");
		
		sb.append("infi_tb_204_ordenes.ORDENE_USR_SUCURSAL,infi_tb_201_ctes.CLIENT_DIRECCION,infi_tb_201_ctes.CLIENT_TELEFONO,");
		sb.append("infi_tb_204_ordenes.ORDENE_USR_NOMBRE,infi_tb_204_ordenes.ORDENE_PED_MONTO,infi_tb_204_ordenes.ORDENE_ADJ_MONTO,");
		sb.append("infi_tb_204_ordenes.ordene_ped_int_caidos,infi_tb_204_ordenes.ordene_ped_comisiones,");
		sb.append("infi_tb_204_ordenes.ordene_ped_total,infi_tb_204_ordenes.ctecta_numero,");
		sb.append("infi_tb_204_ordenes.sector_id,infi_tb_204_ordenes.codigo_id,");
		sb.append("infi_tb_204_ordenes.concepto_id,");
		
		sb.append("(infi_tb_204_ordenes.ORDENE_PED_MONTO*infi_tb_106_unidad_inversion.UNDINV_TASA_CAMBIO)as VALOR_NOMINAL_BOLIVARES,");
		sb.append("(select count(*) from infi_tb_207_ordenes_operacion where ordene_id=infi_tb_204_ordenes.ordene_id) as operaciones,");
		sb.append("(infi_tb_204_ordenes.ordene_ped_total+infi_tb_204_ordenes.ordene_ped_int_caidos+infi_tb_204_ordenes.ordene_ped_comisiones) total, ");
		
		sb.append("(select a.descripcion from infi_tb_048_sector_productivo a	where a.sector_id = infi_tb_204_ordenes.sector_id) des_sector,");
		sb.append("(select a.sector from infi_tb_049_actividad a where a.codigo_id = infi_tb_204_ordenes.codigo_id) des_actividad,");
		sb.append("(select a.concepto from infi_tb_050_conceptos a where a.codigo_id = infi_tb_204_ordenes.concepto_id) des_conceptos ");
		
		sb.append("from infi_tb_204_ordenes inner join infi_tb_201_ctes ");
		sb.append("on infi_tb_201_ctes.CLIENT_ID=infi_tb_204_ordenes.CLIENT_ID left join infi_tb_701_titulos  ");
		sb.append("on infi_tb_204_ordenes.CLIENT_ID=infi_tb_701_titulos.CLIENT_ID inner join infi_tb_106_unidad_inversion ");
		sb.append("on infi_tb_204_ordenes.UNIINV_ID=infi_tb_106_unidad_inversion.UNDINV_ID left join infi_tb_101_inst_financieros ");
		sb.append("on infi_tb_106_unidad_inversion.INSFIN_ID=infi_tb_101_inst_financieros.INSFIN_ID left join infi_tb_102_bloter ");
		sb.append("on infi_tb_204_ordenes.BLOTER_ID=infi_tb_102_bloter.BLOTER_ID  ");
		sb.append("where infi_tb_204_ordenes.ORDENE_PED_FE_VALOR is not null and infi_tb_204_ordenes.BLOTER_ID is not null ");
	    sb.append(" and ordsta_id not in('").append(StatusOrden.CANCELADA).append("','").append(StatusOrden.PENDIENTE).append("')");
	   if(unidad_inversion!=0){
			filtro.append(" and infi_tb_204_ordenes.UNIINV_ID=").append(unidad_inversion);
		}
		if(blotter!=null && !blotter.equalsIgnoreCase("")){
			filtro.append(" and infi_tb_204_ordenes.BLOTER_ID='").append(blotter).append("' ");
		}
		if(fecha_hasta!=null && fecha_desde!=null){
			filtro.append(" and infi_tb_204_ordenes.ordene_ped_fe_orden between to_date('").append(fecha_desde).append("','" + ConstantesGenerales.FORMATO_FECHA + "') and to_date('").append(fecha_hasta).append("','" + ConstantesGenerales.FORMATO_FECHA + "') ");
		}
		sb.append(filtro);
		if (paginado && paginaAMostrar>0 && registroPorPagina> 0){			
			dataSet = obtenerDataSetPaginado(sb.toString(),paginaAMostrar,registroPorPagina);
		}else{
			dataSet = db.get(dataSource, sb.toString());
		}
	}

	public void listarOrdenesConRecompraPorCliente(long idUnidadInversion, long idCliente, String idStatusOrden, String idTransaccionNegocio) throws Exception {

		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT ord.* from INFI_TB_204_ORDENES ord where ord.uniinv_id = ").append(idUnidadInversion);
		sql.append(" and ord.client_id = ").append(idCliente);
		sql.append(" and ord.transa_id = '").append(idTransaccionNegocio).append("'");
		sql.append(" and (select count(*) from infi_tb_206_ordenes_titulos where ordene_id = ord.ordene_id and titulo_pct_recompra<>0 and titulo_pct_recompra is not NULL) > 0");
		
		//si se busca por un estatus espec&iacute;fico
		if(idStatusOrden!=null){
			sql.append(" and ordsta_id = '").append(idStatusOrden).append("'");
		}		
		
		dataSet = db.get(dataSource, sql.toString());

		
	}
	
	/**
	 * Arma el query para actualizar la orden y agregarla al execbacth
	 * @param ordenId
	 * @param status
	 * @param ejecucionId
	 * @return
	 * @throws Exception
	 */
	public String armarQueryUpdateOrden(long ordenId,String status,long ejecucionId) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("update infi_tb_204_ordenes set ejecucion_id=").append(ejecucionId); 
		sql.append(",ordsta_id='").append(status);
		sql.append("' where ordene_id=").append(ordenId);
		
		return sql.toString();
	}
	/**
	 * Lista las operaciones rechazadas o en espera por liquidacion para dicho proceso
	 * @param long ordenId
	 * @return boolean existe
	 * @throws Exception
	 */
	public boolean listarOperacionesLiquidacion(long ordenId)throws Exception{
		
		boolean existe = false;
		StringBuffer sb = new StringBuffer();
		
		sb.append("select op1.ORDENE_ID,op1.ORDENE_OPERACION_ID,op1.TRNF_TIPO,op1.CTECTA_NUMERO,op1.SERIAL,op1.MONTO_OPERACION,op1.MONEDA_ID,op1.CODIGO_OPERACION,op1.STATUS_OPERACION");
		sb.append(" from infi_tb_207_ordenes_operacion op1 where op1.ORDENE_ID=").append(ordenId);
		sb.append(" and op1.ORDENE_RELAC_OPERACION_ID is not null and op1.STATUS_OPERACION != '").append(ConstantesGenerales.STATUS_APLICADA).append("'");
				
		dataSet = db.get(dataSource,sb.toString());		
		if(dataSet.count()>0){
			existe=true;
		}
		
		return existe;
	}
/**
 * Busca la orden creada con la operacion financiera para el banco central de venezuela
 * @param unidadInversion
 * @param transaccionNegocio
 * @throws Exception
 */	
	public void listarOrdenBCV(long unidadInversion,String transaccionNegocio,String vehiculoTomador)throws Exception{
		
		StringBuffer sb = new StringBuffer();		  
		sb.append("select op.STATUS_OPERACION, op.MONTO_OPERACION, op.CTECTA_NUMERO, op.SERIAL, op.CODIGO_OPERACION, op.OPERACION_NOMBRE,");
		sb.append("op.TRNF_TIPO, op.MONEDA_ID, op.ORDENE_ID, op.ORDENE_OPERACION_ID from INFI_TB_204_ORDENES o, infi_tb_207_ordenes_operacion op");
		sb.append(" where o.ordene_id = op.ordene_id and TRANSA_ID='").append(transaccionNegocio).append("' and ordene_veh_tom=").append(vehiculoTomador).append(" and o.UNIINV_ID =").append(unidadInversion);  	
		dataSet = db.get(dataSource, sb.toString());

	}
	
	/**Lista una orden especifica con sus datos de financiamiento
	 * @param ordenId
	 * @throws Exception
	 */
	public void ordenFinanciada(String ordenId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select o.ordene_id,o.ordene_ped_total_pend,o.uniinv_id,o.ordsta_id,o.ordene_veh_tom,o.tipo_producto_id,dtaext_valor,o.client_id,c.client_cedrif,c.tipper_id,c.client_nombre from INFI_TB_204_ORDENES o, INFI_TB_212_ORDENES_DATAEXT d, INFI_TB_201_CTES c, INFI_TB_203_ORDENES_STATUS s where ordene_financiado=").append(ConstantesGenerales.VERDADERO).append(" and ordene_ped_total_pend>").append(ConstantesGenerales.FALSO).append(" and o.ordene_id=d.ordene_id and o.client_id=c.client_id and o.ordsta_id=s.ordsta_id and o.ordsta_id not in('").append(StatusOrden.CANCELADA).append("','").append(StatusOrden.PENDIENTE).append("') and dtaext_id='").append(DataExtendida.PCT_FINANCIAMIENTO).append("' and o.ordene_id=").append(ordenId);
		sql.append(" order by o.ordene_id asc");
		dataSet= db.get(dataSource,sql.toString());
	}
	
	/**Lista todas las ordenes que se enuentren financiadas, excluye las canceladas
	 * Se puede fltrar por cliente
	 * @param clientId identificador del cliente que tomo la orden
	 * @throws Exception
	 */
	public void ordenesFinanciadas(String clientId) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from INFI_TB_204_ORDENES o, INFI_TB_212_ORDENES_DATAEXT d, INFI_TB_201_CTES c, INFI_TB_203_ORDENES_STATUS s where ordene_financiado=").append(ConstantesGenerales.VERDADERO).append(" and ordene_ped_total_pend>").append(ConstantesGenerales.FALSO).append(" and o.ordene_id=d.ordene_id and o.client_id=c.client_id and o.ordsta_id=s.ordsta_id and o.ordsta_id not in('").append(StatusOrden.CANCELADA).append("','").append(StatusOrden.PENDIENTE).append("') and dtaext_id='").append(DataExtendida.PCT_FINANCIAMIENTO).append("' ");
		if (clientId!=null){
			sql.append(" and o.client_id=").append(clientId); 
		}
		sql.append(" order by o.ordene_id asc");
		dataSet= db.get(dataSource,sql.toString());
	}
	
	/**
 * Arma el query para liquidar la orden
 * @param long ejecucionId
 * @param String statusOrden
 * @param long ordeneId
 * @return
 * @throws Exception
 */	
	public String liquidarOrden(long ejecucionId,String statusOrden,long ordeneId)throws Exception {
		
		StringBuffer sb = new StringBuffer("update infi_tb_204_ordenes set ejecucion_id="); 
		sb.append(ejecucionId); 
		sb.append(",ordsta_id='"); 
		sb.append(statusOrden).append("',ordene_fecha_liquidacion=").append(formatearFechaBDActual()); 
		sb.append(",ORDENE_FE_ULT_ACT= TO_DATE(SYSDATE,'dd-MM-RRRR')"); 		
		sb.append(" where ordene_id= "); 
		sb.append(ordeneId);
		
		return sb.toString();
	}
	
	/**Busca las ordenes que correspondan con las transacciones de negocio y las fechas dadas. Genera un dataSet
	 * @param idTransacciones Vector que contiene los id de las transacciones de negocio a buscar
	 * @param fechaDesde fecha inicial de la consulta para buscar las ordenes que esten dentro del rango de fechas
	 * @param fechaHasta fecha inicial de la consulta para buscar las ordenes que esten dentro del rango de fechas
	 * @throws lanza una excepción en caso de error*/	
	public void listarOrdenes(String[] idTransacciones, Date fechaDesde, Date fechaHasta) throws Exception {
		StringBuffer sb = new StringBuffer();
		StringBuffer idTransaccionesNegocio = new StringBuffer();
		for (int i=0; i < idTransacciones.length; i++) {			
			idTransaccionesNegocio.append("'").append(idTransacciones[i]).append("'");
			if (i != idTransacciones.length -1){
				idTransaccionesNegocio.append(",");
			}
		} 		
		sb.append("SELECT * FROM INFI_TB_204_ORDENES WHERE TRANSA_ID IN(")
		  .append(idTransaccionesNegocio).append(")");		
		sb.append(" AND ORDENE_PED_FE_VALOR BETWEEN ").append(this.formatearFechaBD(fechaDesde));
		sb.append(" AND ").append(this.formatearFechaBD(fechaHasta));
		sb.append("AND ORDSTA_ID='").append(StatusOrden.REGISTRADA).append("' ");
		sb.append(" ORDER BY CLIENT_ID,TRANSA_ID ");
		db.exec(this.dataSource, sb.toString());
	}
/**
 * Actualiza el campo de id_opics de la orden
 * @param idOpics
 * @param ordeneId
 * @return String de la consulta para ser ejecutada en el proceso que lo llama
 * @throws Exception
 */	
	public String actualizarCampoOpics(long idOpics,long ordeneId)throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("update infi_tb_204_ordenes set id_opics =").append(idOpics);
		sql.append(" where ordene_id=").append(ordeneId); 
		
		return sql.toString();
	}
	
	/**
 * Actualiza el campo de ejecuci&oacute;n id seg&uacute;n el id opics dado
 * @param ejecucionId
 * @param opicsId
 * @return
 * @throws Exception
 */
	public String actualizarEjecucionId(long ejecucionId, long opicsId)throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("update infi_tb_204_ordenes set ejecucion_id=").append(ejecucionId);
		sb.append(" where id_opics=").append(opicsId);
		
		return sb.toString();
	}
	
	/** Metodo que retorna una lista de clientes con el monto total de los titulos según la transaccion de negocio (Salida Interna o externa)
	 * @param idCliente puede enviarse null si se desea obtener las operaciones para todos los clientes o enviar el 
	 * código del cliente al que se le desea obtener los movimientos entre determinadas fechas
	 * @param fechaInicio fecha de inicio para la búsqueda de las operaciones de salida interna y externa
	 * @param fechaFin fecha de fin para la búsqueda de las operaciones de salida interna y externa
	 * @throws Exception
	 */
	public void listarMontosTitulos(String idCliente, Date fechaInicio, Date fechaFin) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select c.client_id,c.TRANSA_ID,count(a.titulo_id) as cantidad,sum(a.titulo_unidades)monto,b.titulo_moneda_den from infi_tb_206_ordenes_titulos a, infi_tb_100_titulos b, infi_tb_204_ordenes c where a.titulo_id = b.titulo_id and a.ordene_id = c.ordene_id and  c.TRANSA_ID in('").append(TransaccionNegocio.SALIDA_INTERNA).append("' , '").append(TransaccionNegocio.SALIDA_EXTERNA).append("')");
		sql.append(" and ordene_ped_fe_valor between ").append(this.formatearFechaBD(fechaInicio));				
		sql.append(" and ").append(this.formatearFechaBD(fechaFin));
		
		if (idCliente != null){
			sql.append(" and c.client_id = " + idCliente);
		}
		sql.append(" group by c.client_id,c.transa_id,b.titulo_moneda_den order by c.client_id,c.TRANSA_ID,b.titulo_moneda_den");
		dataSet= db.get(dataSource,sql.toString());
	}
	
	/** Metodo que retorna una lista de clientes con la cantidad de operaciones realizadas para la transaccion de negocio (Salida Interna o externa)
	 * @param fechaInicio fecha de inicio para la búsqueda de las operaciones
	 * @param fechaFin fecha de fin para la búsqueda de las operaciones
	 * @throws Exception
	 */
	public void listarCantidadOperaciones(String cliente, Date fechaInicio, Date fechaFin) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select c.client_id,c.TRANSA_ID,count(c.TRANSA_ID) as cant_operaciones from infi_tb_204_ordenes c where c.TRANSA_ID in('").append(TransaccionNegocio.SALIDA_INTERNA).append("' , '").append(TransaccionNegocio.SALIDA_EXTERNA).append("')");
		sql.append(" and ordene_ped_fe_valor between ").append(this.formatearFechaBD(fechaInicio));
		sql.append(" and ").append(this.formatearFechaBD(fechaFin));
		sql.append(" and client_id=").append(cliente);
		sql.append(" group by c.client_id,c.transa_id order by c.client_id");
		
		dataSet= db.get(dataSource,sql.toString());
	}
	
	/** Metodo que retorna una lista de clientes con la cantidad de operaciones realizadas para un depositario especifico para las transacciones de negocio (Salida Interna o externa)
	 * @param fechaInicio fecha de inicio para la búsqueda de las operaciones por depositarios
	 * @param fechaFin fecha de fin para la búsqueda de las operaciones por depositarios
	 * @throws Exception
	 */
	public void listarTotalOperacionesDepositarios(String cliente, Date fechaInicio, Date fechaFin) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select c.client_id,c.empres_id,b.empres_siglas,count(c.empres_id) as total_operaciones from infi_tb_204_ordenes c, infi_tb_016_empresas b where c.TRANSA_ID in('").append(TransaccionNegocio.SALIDA_INTERNA).append("' , '").append(TransaccionNegocio.SALIDA_EXTERNA).append("')");
		sql.append(" and ordene_ped_fe_valor between ").append(this.formatearFechaBD(fechaInicio));
		sql.append(" and ").append(this.formatearFechaBD(fechaFin));
		sql.append(" and client_id=").append(cliente);
		sql.append(" and c.empres_id = b.empres_id ");
		sql.append(" group by c.client_id,c.empres_id,b.empres_siglas order by c.client_id");
		dataSet= db.get(dataSource,sql.toString());
	}
	
	/**
	 * Lista el vehiculo asociado a una unidad de inversion 
	 * que sean estatus enviada o adjudicada y donde 
	 * la transaccion sea toma de orden o toma de orden cartera propia
	 * @param idUnidad unidad de inversi&oacute;n
	 * */
	public void listarOrdenesPorAdjudicar(String unidad) throws Exception{
		StringBuffer sql = new StringBuffer();
		
		sql.append("select u.undinv_nombre,o.ordene_veh_col,v.vehicu_nombre,count(o.ordene_id) as ordenes, (count(oporadj.ordene_id)+count(oen.ordene_id)) as por_adjudicar, count(oadj.ordene_id) as adjudicadas,count(oen.ordene_id) as enviadas,");
		sql.append(" case when t.transa_id='");
		sql.append(TransaccionNegocio.TOMA_DE_ORDEN);
		sql.append("' then 'No' when t.transa_id='");
		sql.append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA);
		sql.append("' then 'Si' end orden_propia,o.uniinv_id from INFI_TB_204_ORDENES o inner join INFI_TB_012_TRANSACCIONES t on t.transa_id=o.transa_id inner join INFI_TB_106_UNIDAD_INVERSION u on o.uniinv_id=u.undinv_id inner join INFI_TB_018_VEHICULOS v on v.vehicu_id=o.ordene_veh_col left join INFI_TB_204_ORDENES oadj on o.ordene_id = oadj.ordene_id and oadj.ordsta_id='");
		sql.append(StatusOrden.ADJUDICADA);
		sql.append("' left join INFI_TB_204_ORDENES oen on o.ordene_id = oen.ordene_id and oen.ordsta_id='");
		sql.append(StatusOrden.ENVIADA);
		sql.append("' left join INFI_TB_204_ORDENES oporadj on o.ordene_id = oporadj.ordene_id and oporadj.ordsta_id='");
		sql.append(StatusOrden.PROCESO_ADJUDICACION);
		sql.append("' where(t.transa_id='");
		sql.append(TransaccionNegocio.TOMA_DE_ORDEN);
		sql.append("' OR t.transa_id='");
		sql.append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA);
		
		if (unidad!=null){
			sql.append("') and o.uniinv_id=").append(unidad);
		}
		else
		sql.append("')");
		
		sql.append(" GROUP BY o.ordene_veh_col,v.vehicu_nombre,o.uniinv_id,t.transa_id,u.undinv_nombre");
		System.out.println("listarOrdenesPorAdjudicar : " + sql.toString());
		dataSet = db.get(dataSource, sql.toString());
	}
	
	//NM32454 INFI_TTS_491_WS_BCV 03/03/2015
	public void actualizarOrdenBCV(String ordeneID, String ordenBCV, String estatus, String observacion) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE INFI_TB_204_ORDENES SET ORDENE_ESTATUS_BCV = ").append(estatus);
		if(ordenBCV != null && !ordenBCV.equals("")){
			sql.append(" ,  ORDENE_ID_BCV = '").append(ordenBCV).append("'");
		}
		
		if(observacion != null){
			if(observacion.length() > 1000){
				observacion.substring(0, 999);
			}
			sql.append(" ,  ORDENE_OBSERVACION = '").append(observacion).append("'");
		}
		
		sql.append(" WHERE ORDENE_ID =").append(ordeneID);
		db.exec(dataSource, sql.toString());
		System.out.println("actualizarOrdenBCV----> "+sql);
	}
	
	public void actualizarOrdenBCVMenudeo(String ordeneID, String estatus, String observacion) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE INFI_TB_204_ORDENES SET ORDENE_ESTATUS_BCV = ").append(estatus);
		if(observacion != null){
			if(observacion.length() > 1000){
				observacion.substring(0, 999);
			}
			sql.append(" ,  ORDENE_OBSERVACION = '").append(observacion).append("'");
		}
		
		sql.append(" WHERE ORDENE_ID =").append(ordeneID);
		db.exec(dataSource, sql.toString());
		System.out.println("actualizarOrdenBCV----> "+sql);
	}
	
	public void actualizarOrdenBCVMenudeoM(String ordeneID, String observacion, String id_bcv,String estatus,BigDecimal montoTransaccion,BigDecimal tasaCambio) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE INFI_TB_234_VC_DIVISAS SET OBSERVACION ='").append(observacion).append("'");
		sql.append(",ID_BCV ='").append(id_bcv).append("'");
		
		if (montoTransaccion!=null) {
			sql.append(",TASA_CAMBIO =").append(tasaCambio).append("");
			sql.append(",MTO_DIVISAS_TRANS =").append(montoTransaccion).append("");
		}
		sql.append(",STATUS_ENVIO ='").append(estatus).append("'");
		sql.append(" WHERE ID_OPER =").append(ordeneID);
		
		db.exec(dataSource, sql.toString());
	}
	
	public void actualizarOrdenBCVMenudeoM(String ordeneID,String fecha, String observacion, String id_bcv,String estatus,BigDecimal montoTransaccion,BigDecimal tasaCambio) throws Exception{
		System.out.println("montoTransaccion-->"+montoTransaccion);
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE INFI_TB_234_VC_DIVISAS SET OBSERVACION ='").append(observacion).append("'");
		sql.append(",ID_BCV ='").append(id_bcv).append("'");
		if (montoTransaccion!=null) {
			sql.append(",TASA_CAMBIO =").append(tasaCambio).append("");
		}
		sql.append(",STATUS_ENVIO ='").append(estatus).append("'");
		if (montoTransaccion!=null) {
			sql.append(",MTO_DIVISAS_TRANS =").append(montoTransaccion).append("");
		}
		sql.append(" WHERE ID_OPER =").append(ordeneID);
		sql.append(" AND TO_DATE(FECH_OPER,'DD/MM/RRRR') = TO_DATE('").append(fecha).append("', 'DD/MM/RRRR')");
		db.exec(dataSource, sql.toString());
		System.out.println("actualizarOrdenBCV----> "+sql);
	}
		
	public void actualizarOrdenBCVMesaDeCambio(String ordeneID,String fecha, String observacion, String id_bcv,String estatus) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE INFI_TB_236_MESA_CAMBIO SET OBSERVACION ='").append(observacion).append("'");
		sql.append(",ID_BCV ='").append(id_bcv).append("'");
		sql.append(",STATUS_ENVIO ='").append(estatus).append("'");
		sql.append(" WHERE ID_OPER =").append(ordeneID);
		sql.append(" AND TO_DATE(FECH_OPER,'DD/MM/RRRR') = TO_DATE('").append(fecha).append("', 'DD/MM/RRRR')");
		db.exec(dataSource, sql.toString());
		System.out.println("actualizarOrdenBCVMesaDeCambio----> "+sql);
	}
	
	public void actualizarOrdenBCVMesaDeCambioPacto(String idDemanda,String idOferta, String idPacto) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE INFI_TB_236_MESA_CAMBIO SET ID_PACTO ='").append(idPacto).append("'");
		sql.append(" WHERE ID_BCV =").append(idDemanda);
		sql.append(" OR ID_BCV =").append(idOferta);
//		sql.append(" AND TO_DATE(FECH_OPER,'DD/MM/RRRR') = TO_DATE('").append(fecha).append("', 'DD/MM/RRRR')");
		db.exec(dataSource, sql.toString());
		System.out.println("actualizarOrdenBCVMesaDeCambio----> "+sql);
	}
	
	public void actualizarOrdenBCVIntervencion(String ordeneID,String fecha, String observacion, String id_bcv,String estatus) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE INFI_TB_235_INTERVENCION SET OBSERVACION ='").append(observacion).append("'");
		sql.append(",STATUS_ENVIO ='").append(id_bcv).append("'");
		sql.append(" WHERE ID_OPER =").append(ordeneID);
		sql.append(" AND TO_DATE(FECH_OPER,'DD/MM/RRRR') = TO_DATE('").append(fecha).append("', 'DD/MM/RRRR')");
		db.exec(dataSource, sql.toString());
		System.out.println("actualizarOrdenBCV----> "+sql);
	}
	
	public void actualizarEstatusBCVMenudeo(String ordeneID,String fecha, String Status) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE INFI_TB_234_VC_DIVISAS SET STATUS_ENVIO ='").append(Status).append("'");
		sql.append(" WHERE ID_OPER =").append(ordeneID);
		sql.append(" AND TO_DATE(FECH_OPER,'DD/MM/RRRR') = TO_DATE('").append(fecha).append("', 'DD/MM/RRRR')");
		db.exec(dataSource, sql.toString());
		System.out.println("actualizarOrdenBCV----> "+sql);
	}
	
	public void insfin() throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select insfin_id,INSFIN_DESCRIPCION from infi_tb_101_inst_financieros where INSFIN_DESCRIPCION='TITULOS_SITME'");
		//sql.append("select insfin_id,INSFIN_DESCRIPCION from infi_tb_101_inst_financieros where TIPO_PRODUCTO_ID='"+ConstantesGenerales.ID_TIPO_PRODUCTO_SITME+"'");
		
		dataSet = db.get(dataSource, sql.toString());
	}
	
	/**
	 * Metodo que retorna el documento asociado a la orden dado el id del documento
	 * @param documento
	 * @return DocumentoDefinicion
	 * @throws Exception
	 */
	public DocumentoDefinicion listarDocumento(String documento) throws Exception{
		StringBuffer sql = new StringBuffer();
		DocumentoDefinicion docDef = new DocumentoDefinicion();
		
		sql.append("select * from INFI_TB_208_ORDENES_DOCUMENTOS where ORDENE_DOC_ID = " +
				documento + " for update");
		
		try {
			conn = this.dataSource.getConnection();
			conn.setAutoCommit(false);
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql.toString());
			conn.commit();
			if (resultSet.next()) {
				Blob doc = resultSet.getBlob(3);				
				byte[] bufer = doc.getBytes(1, (int) doc.length());
				docDef.setContenido(bufer);
				docDef.setNombreDoc(resultSet.getString(4));								
			}
			return docDef;
		} catch (Exception e) {
			logger.error(e.getMessage()+ Utilitario.stackTraceException(e));
			throw new Exception("Error al retornar el documento de la tabla: " + e.getMessage());
		} finally {
			this.closeResources();
			this.cerrarConexion();
		}		
	}
	
	/**
	 * Verifica si existe un registro de bloqueo con un tipo de garant&iacute;a, tipo de bloqueo, titulo y cliente en particular 
	 * retornando la cantidad bloqueada   
	 * @param tituloBloqueo
	 * @return int con la cantidad bloqueada, 0 en caso de no existir el bloqueo
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	public int existeOrdenesPorAdjudicar(String unidadInversion) throws NumberFormatException, Exception{
		
		StringBuffer sql = new StringBuffer();		
		
		sql.append("select COUNT(*) as total from INFI_TB_204_ORDENES where transa_id in ('");
		sql.append(TransaccionNegocio.TOMA_DE_ORDEN);
		sql.append("','");
		sql.append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA);
		sql.append("') AND ordsta_id IN ('");												     //RESOLUCION INCIDENCIA CAMBIO ESTATUS EN UNIDAD DE INVERSION NM26659 
		//sql.append(StatusOrden.ENVIADA).append("','").append(StatusOrden.REGISTRADA).append("'");//,'").append(StatusOrden.PROCESO_ADJUDICACION
		sql.append(StatusOrden.ENVIADA).append("','").append(StatusOrden.REGISTRADA).append("','").append(StatusOrden.PROCESO_ADJUDICACION);		
		sql.append("') and uniinv_id=");
		sql.append(unidadInversion);

		dataSet = db.get(dataSource, sql.toString());
		
		if(dataSet.next())
			return Integer.parseInt(dataSet.getValue("total"));
		else
			return 0;
		
	}

	/**
	 * Verifica si existen ordenes que no han sido enviadas al BDV.
	 * @param unidadInversion id de la unidad de inversión a consultar
	 * @return número de órdenes encontradas
	 * @throws NumberFormatException en caso de error
	 * @throws Exception en caso de error
	 */
	public int existeOrdenesPorEnviar(String unidadInversion) throws NumberFormatException, Exception{
		
		StringBuffer sql = new StringBuffer();		
		
		sql.append("select COUNT(*) as total from INFI_TB_204_ORDENES where transa_id in ('");
		sql.append(TransaccionNegocio.TOMA_DE_ORDEN);
		sql.append("','");
		sql.append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA);
		sql.append("') AND ordsta_id IN ('");
		sql.append(StatusOrden.REGISTRADA).append("') and uniinv_id=");
		sql.append(unidadInversion);

		dataSet = db.get(dataSource, sql.toString());
		
		if(dataSet.next())
			return Integer.parseInt(dataSet.getValue("total"));
		else
			return 0;
		
	}	
	
	/**
	 * Elimina una orden de la base de datos, sus operaciones, titulos y todos los datos asociados a ella.
	 * @param beanTOSimulada
	 * @throws SQLException 
	 */
	public void eliminarOrden(Orden orden) throws Exception {
		
		try {
			conn = dataSource.getConnection();			
			conn.setAutoCommit(false);
			statement = conn.createStatement();
						
			//--1. Eliminar operaciones
			StringBuffer sql = new StringBuffer();
			sql.append("DELETE FROM INFI_TB_207_ORDENES_OPERACION WHERE ORDENE_ID = ").append(orden.getIdOrden());	
			statement.executeUpdate(sql.toString());
			//--2. Eliminar intentos de operaciones
			sql = new StringBuffer();
			sql.append("DELETE FROM INFI_TB_209_ORDENES_OPERAC_INT WHERE ORDENE_ID = ").append(orden.getIdOrden());
			statement.executeUpdate(sql.toString());
			//--3. Eliminar documentos
			sql = new StringBuffer();
			sql.append("DELETE FROM INFI_TB_208_ORDENES_DOCUMENTOS WHERE ORDENE_ID = ").append(orden.getIdOrden());	
			statement.executeUpdate(sql.toString());
			//--4. Eliminar titulos
			sql = new StringBuffer();
			sql.append("DELETE FROM INFI_TB_206_ORDENES_TITULOS WHERE ORDENE_ID = ").append(orden.getIdOrden());	
			statement.executeUpdate(sql.toString());
			//--5. Eliminar Campos dinamicos
			sql = new StringBuffer();
			sql.append("DELETE FROM INFI_TB_205_ORDENES_CAMP_DIN WHERE ORDENE_ID = ").append(orden.getIdOrden());	
			statement.executeUpdate(sql.toString());
			//--6. Eliminar Data extendida
			sql = new StringBuffer();
			sql.append("DELETE FROM INFI_TB_212_ORDENES_DATAEXT WHERE ORDENE_ID = ").append(orden.getIdOrden());	
			statement.executeUpdate(sql.toString());
			//--7. Eliminar Instrucccion de pago
			sql = new StringBuffer();
			sql.append("DELETE FROM INFI_TB_217_CTES_CUENTAS_ORD WHERE ORDENE_ID = ").append(orden.getIdOrden());	
			statement.executeUpdate(sql.toString());
			//--8. Eliminar la orden
			sql = new StringBuffer();
			sql.append("DELETE FROM INFI_TB_204_ORDENES WHERE ORDENE_ID = ").append(orden.getIdOrden());	
			statement.executeUpdate(sql.toString());

			conn.commit();
		
		} catch (Exception e) {
			conn.rollback();			
			logger.error(e.getMessage()+ Utilitario.stackTraceException(e));
			throw new Exception("Error elimimando la orden");			
		}finally{					
			if(statement!=null)
				statement.close();
			
			if(conn!=null)
				conn.close();
		}
			
	}

/**
 * Lista las ordenes de una unidad de inversión especifica que tengan status adjudicadas y
 * sin instrucciones de pago
 * @param unidadInversion
 * @throws Exception
 */
	public void listarOrdenesSinInstruccionPago(long unidadInversion, long ordenId)throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select unique(a.ordene_id),INFI_TB_217_CTES_CUENTAS_ORD.*,b.client_id,a.ORDENE_PED_TOTAL_PEND,b.client_nombre, a.ORDENE_ADJ_MONTO, a.ORDENE_PED_TOTAL," +
				"(select sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION)as monto_operacion from infi_tb_207_ordenes_operacion where infi_tb_207_ordenes_operacion.STATUS_OPERACION in('EN ESPERA LIQUIDACION','EN ESPERA','RECHAZADA') " +
				"and trnf_tipo='DEB' and infi_tb_207_ordenes_operacion.ordene_id=a.ordene_id) + a.ORDENE_PED_TOTAL_PEND as monto_pendiente, ((select sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION) as monto_operacion " +
				"from infi_tb_207_ordenes_operacion where infi_tb_207_ordenes_operacion.STATUS_OPERACION in('APLICADA') " +
				"and trnf_tipo='DEB' and infi_tb_207_ordenes_operacion.ordene_id = " +
				"a.ordene_id)- decode((select sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION)as monto_operacion from infi_tb_207_ordenes_operacion where infi_tb_207_ordenes_operacion.STATUS_OPERACION in('APLICADA') and trnf_tipo='CRE' and infi_tb_207_ordenes_operacion.ordene_id = a.ordene_id),null,0," +
				"(select sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION)as monto_operacion from infi_tb_207_ordenes_operacion where infi_tb_207_ordenes_operacion.STATUS_OPERACION in('APLICADA') and trnf_tipo='CRE' and infi_tb_207_ordenes_operacion.ordene_id = a.ordene_id)))as cobrado  " +
				"from infi_tb_204_ordenes a " +
				"left join infi_tb_201_ctes b on a.client_id = b.client_id " +
				"left join INFI_TB_217_CTES_CUENTAS_ORD on a.ordene_id=INFI_TB_217_CTES_CUENTAS_ORD.ordene_id " +
				"left join INFI_TB_206_ORDENES_TITULOS on a.ordene_id=INFI_TB_206_ORDENES_TITULOS.ordene_id " +
				"where a.uniinv_id=" +
				unidadInversion +
				" and INFI_TB_206_ORDENES_TITULOS.TITULO_PCT_RECOMPRA >0 " +
				"and INFI_TB_206_ORDENES_TITULOS.TITULO_MTO_NETEO=0 " +
				"and a.ordsta_id='" +
				StatusOrden.ADJUDICADA +
				"' and INFI_TB_217_CTES_CUENTAS_ORD.CTES_CUENTAS_ID is null and TITULO_PCT_RECOMPRA>0 and (TITULO_MTO_NETEO is null or TITULO_MTO_NETEO=0) and ordene_adj_monto>0");
		if(ordenId!=0)
		{
			sql.append("and a.ordene_id=").append(ordenId);
		}
		dataSet = db.get(dataSource, sql.toString());}//fin metodo
	
	/**
	 * Lista las ordenes registradas de tipo inventario con fecha menor a la actual, listas para ser pasadas a custodia
	 * @throws Exception
	 */
	public void listarOrdenesInventarioACustodia() throws Exception{
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("select or.*, inst.INSFIN_FORMA_ORDEN, ui.UNDINV_NOMBRE from INFI_TB_204_ORDENES or, INFI_TB_106_UNIDAD_INVERSION ui, INFI_TB_101_INST_FINANCIEROS inst " );
		sql.append("WHERE or.UNIINV_ID = ui.UNDINV_ID and ui.INSFIN_ID = inst.INSFIN_ID and ");
		sql.append("(inst.INSFIN_FORMA_ORDEN =  '");
		sql.append(ConstantesGenerales.INST_TIPO_INVENTARIO).append("'");
		sql.append("OR inst.INSFIN_FORMA_ORDEN =  '");
		sql.append(ConstantesGenerales.INST_TIPO_INVENTARIO_CON_PRECIO).append("')");
		sql.append("and to_date(or.ORDENE_PED_FE_ORDEN, '"+ConstantesGenerales.FORMATO_FECHA+"') < "+this.formatearFechaBDActual());
		
		dataSet = db.get(dataSource,sql.toString());
	}
	/**
	 * Cuenta el número de ordenes que no poseen instrucciones de pago definidas
	 * @param long unidadInversion
	 * @throws Exception
	 */
	public void ordenesSinInstruccionesCount(long unidadInversion)throws Exception{
		
		/*StringBuffer sql = new StringBuffer();
		sql.append("select count(distinct(ordene_id))cuenta from infi_tb_204_ordenes ");
		sql.append("left join INFI_TB_217_CTES_CUENTAS_ORD on infi_tb_204_ordenes.ordene_id=INFI_TB_217_CTES_CUENTAS_ORD.ordene_id ");
		sql.append("left join infi_tb_206_ordenes_titulos on infi_tb_204_ordenes.ordene_id=infi_tb_206_ordenes_titulos.ordene_id ");
		sql.append("where UNIINV_ID=");
		sql.append(unidadInversion);
		sql.append(" and ordsta_id='");
		sql.append(ConstantesGenerales.UNINV_ADJUDICADA);
		sql.append("' and ctes_cuentas_id is null and TITULO_PCT_RECOMPRA>0 and (TITULO_MTO_NETEO is null or TITULO_MTO_NETEO=0) and ordene_adj_monto>0");*/
		
		StringBuffer sql = new StringBuffer();
		sql.append("select count(ord.ordene_id) cuenta from infi_tb_204_ordenes ord,infi_tb_217_ctes_cuentas_ord ctas_ord, infi_tb_206_ordenes_titulos ord_tit ");
		sql.append(" where ctas_ord.ctes_cuentas_id IS NULL AND ord_tit.titulo_pct_recompra > 0 AND (ord_tit.titulo_mto_neteo IS NULL OR ord_tit.titulo_mto_neteo = 0)");
		sql.append(" AND ord.ORDENE_ID=ord_tit.ORDENE_ID AND ord.ordene_adj_monto > 0 ");	
		sql.append("AND ord.ordsta_id = '").append(ConstantesGenerales.UNINV_ADJUDICADA);
		sql.append("' AND ord.uniinv_id = ").append(unidadInversion);
		dataSet = db.get(dataSource,sql.toString());
		System.out.println("ordenesSinInstruccionesCount "+sql);
	}
	/**
	 * Este metodo, realiza la agrupacion por vehiculo y muestra total del monto involucrado,<br>
	 * cobrado,pendiente,cantidad de ordenes y al igual los datos correspondientes al vehiculo
	 * @param long unidadInversionId
	 * @throws Exception
	 */
	public void listarCreditoPorVehiculo(long unidadInversionId,String status[])throws Exception{
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("select distinct(ORDENE_VEH_TOM),INFI_TB_018_VEHICULOS.VEHICU_ID,INFI_TB_018_VEHICULOS.VEHICU_NOMBRE,INFI_TB_018_VEHICULOS.VEHICU_RIF,INFI_TB_018_VEHICULOS.VEHICU_SIGLAS,INFI_TB_018_VEHICULOS.VEHICU_BRANCH,INFI_TB_018_VEHICULOS.VEHICU_NUMERO_CUENTA_BCV,decode(INFI_TB_018_VEHICULOS.VEHICU_NUMERO_CUENTA,'null','Sin registrar',INFI_TB_018_VEHICULOS.VEHICU_NUMERO_CUENTA)as VEHICU_NUMERO_CUENTA,");
		sql.append("(select count(ordene_id) from infi_tb_204_ordenes a where a.ORDENE_VEH_TOM=infi_tb_204_ordenes.ORDENE_VEH_TOM and uniinv_id=").append(unidadInversionId).append(" and a.transa_id not in ('");
		sql.append(TransaccionNegocio.ORDEN_VEHICULO).append("','").append(TransaccionNegocio.LIQUIDACION);
		sql.append("') and (a.ordsta_id=");
		sql.append("'").append(status[0]).append("' or a.ordsta_id='").append(status[1]);
		sql.append("'))cantidad,");
		sql.append("(select sum(ordene_ped_total) from infi_tb_204_ordenes a where a.ORDENE_VEH_TOM=infi_tb_204_ordenes.ORDENE_VEH_TOM and uniinv_id=").append(unidadInversionId).append(" and  a.transa_id not in ('ORDEN_VEHICULO','CANCELACION_ORDEN','LIQUIDACION')and ordsta_id not in('CANCELADA','PENDIENTE'))total,");
		sql.append("((select decode(sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION),null,0,sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION)) from infi_tb_204_ordenes a inner join infi_tb_207_ordenes_operacion on a.ordene_id=infi_tb_207_ordenes_operacion.ordene_id where infi_tb_207_ordenes_operacion.STATUS_OPERACION in('EN ESPERA','RECHAZADA') and trnf_tipo='DEB' and infi_tb_207_ordenes_operacion.ordene_id=a.ordene_id and uniinv_id=").append(unidadInversionId);
		sql.append(" and a.ORDENE_VEH_TOM=infi_tb_204_ordenes.ORDENE_VEH_TOM and (a.ordsta_id='ADJUDICADA' or a.ordsta_id='LIQUIDADA'))+ (select decode(sum(a.ORDENE_PED_TOTAL_PEND),null,0,sum(a.ORDENE_PED_TOTAL_PEND)) from infi_tb_204_ordenes a where uniinv_id=").append(unidadInversionId);
		sql.append(" and (a.ordsta_id='ADJUDICADA' or a.ordsta_id='LIQUIDADA') and a.ORDENE_VEH_TOM=infi_tb_204_ordenes.ORDENE_VEH_TOM))as monto_pendiente,");
		sql.append("((select decode(sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION),null,0,sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION)) from infi_tb_204_ordenes a inner join infi_tb_207_ordenes_operacion on a.ordene_id=infi_tb_207_ordenes_operacion.ordene_id where uniinv_id=").append(unidadInversionId);
		sql.append(" and a.ORDENE_VEH_TOM=infi_tb_204_ordenes.ORDENE_VEH_TOM and infi_tb_207_ordenes_operacion.STATUS_OPERACION in('APLICADA') and trnf_tipo='DEB' and (a.ordsta_id='ADJUDICADA' or a.ordsta_id='LIQUIDADA'))");
		sql.append("-(select decode(sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION),null,0,sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION)) from infi_tb_204_ordenes a inner join infi_tb_207_ordenes_operacion on a.ordene_id=infi_tb_207_ordenes_operacion.ordene_id where uniinv_id=").append(unidadInversionId);
		sql.append(" and a.ORDENE_VEH_TOM=infi_tb_204_ordenes.ORDENE_VEH_TOM and infi_tb_207_ordenes_operacion.STATUS_OPERACION in('APLICADA') and trnf_tipo='CRE' and (a.ordsta_id='ADJUDICADA' or a.ordsta_id='LIQUIDADA')))as monto_cobrado ");
		sql.append("from infi_tb_204_ordenes, INFI_TB_018_VEHICULOS where ORDENE_VEH_TOM=VEHICU_ID and ORDENE_VEH_TOM is not null and uniinv_id=").append(unidadInversionId);
		//Realizamos la consulta
		dataSet = db.get(dataSource,sql.toString());
		System.out.println("listarCreditoPorVehiculo "+sql);
	}
	
	/**
	 * Este metodo, realiza la agrupacion por vehiculo y muestra el total del monto involucrado 
	 * en comisiones para que sea restado del monto principal
	 * @param long unidadInversionId unidad de inversión
	 * @throws Exception
	 */
	public BigDecimal listarCreditoPorVehiculoComision(long unidadInversionId)throws Exception{
		StringBuffer sql = new StringBuffer();
		BigDecimal montoOperacion = BigDecimal.ZERO;
		sql.append("select sum(monto_operacion) monto_operacion from ( ");
		sql.append("	select sum(monto_operacion) monto_operacion from infi_tb_207_ordenes_operacion where ordene_id in(");
		sql.append("	  select ordene_id from infi_tb_204_ordenes where uniinv_id=").append(unidadInversionId).append(" and transa_id='").append(TransaccionNegocio.TOMA_DE_ORDEN).append("' and ordsta_id NOT IN ('").append(StatusOrden.CANCELADA).append("','").append(StatusOrden.PENDIENTE).append("'");
		sql.append("	)) and trnf_tipo='").append(TransaccionFinanciera.DEBITO).append("' and in_comision=1 ");
		sql.append("	union ");
		sql.append("	select sum(monto_operacion) * -1 monto_operacion from infi_tb_207_ordenes_operacion where ordene_id in( ");
		sql.append("	  select ordene_id from infi_tb_204_ordenes where uniinv_id=").append(unidadInversionId).append(" and transa_id='").append(TransaccionNegocio.TOMA_DE_ORDEN).append("' and ordsta_id NOT IN ('").append(StatusOrden.CANCELADA).append("','").append(StatusOrden.PENDIENTE).append("'");
		sql.append("	)) and trnf_tipo='").append(TransaccionFinanciera.CREDITO).append("' and in_comision=1 ");
		sql.append("	) ");
		//System.out.println("*****************listarCreditoPorVehiculoComision*****************  " + sql.toString());
		dataSet = db.get(dataSource,sql.toString());
		if (dataSet.count()>0){
			dataSet.next();
			if (dataSet.getValue("monto_operacion")!=null)
			{
				montoOperacion = new BigDecimal(dataSet.getValue("monto_operacion"));
			}
			if (dataSet.getValue("monto_operacion")==null){
				montoOperacion=new BigDecimal(0);
			}
		}	
		return montoOperacion;
	}
	
	public long verificarOrdenVehiculoUI(long idClienteVehiculo, long idUnidadInversion) throws Exception{
		
		long numeroOrden = -1;
		
		StringBuffer sql = new StringBuffer();		
		
		sql.append("select ordene_id from INFI_TB_204_ORDENES ");
		sql.append("where transa_id = '").append(TransaccionNegocio.ORDEN_VEHICULO).append("' ");
		sql.append(" and client_id = ").append(idClienteVehiculo);
		sql.append(" and uniinv_id = ").append(idUnidadInversion);
		
		dataSet = db.get(dataSource,sql.toString());
		
		if(dataSet.next()){			
			numeroOrden = Long.parseLong(dataSet.getValue("ordene_id"));
		}
		
		return numeroOrden;
	}
/**
 * Lista los totales por vehiculo en unidades de inversion tipo instrumento INVENTARIO
 * Los totales pertenecen a aquellas ordenes que se encuentren registradas y que no sean de transa_id:
 * <li>Liquidacion</li>
 * <li>Orden Vehiculo</li>
 * @param unidadInversion
 * @throws Exception
 */
	public void listarOrdenesPorVehiculoInventario(long unidadInversion,String status)throws Exception{
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("select distinct(ORDENE_VEH_TOM),INFI_TB_016_EMPRESAS.empres_rif,INFI_TB_016_EMPRESAS.EMPRES_CUENTA,INFI_TB_109_UI_EMPRESAS.EMPRES_ID,INFI_TB_018_VEHICULOS.VEHICU_ID,INFI_TB_018_VEHICULOS.VEHICU_NOMBRE,INFI_TB_018_VEHICULOS.VEHICU_RIF,INFI_TB_018_VEHICULOS.VEHICU_SIGLAS,INFI_TB_018_VEHICULOS.VEHICU_BRANCH,INFI_TB_018_VEHICULOS.VEHICU_NUMERO_CUENTA_BCV,decode(INFI_TB_018_VEHICULOS.VEHICU_NUMERO_CUENTA,'null','Sin registrar',INFI_TB_018_VEHICULOS.VEHICU_NUMERO_CUENTA)as VEHICU_NUMERO_CUENTA,");
		sql.append("(select count(ordene_id) from infi_tb_204_ordenes a where a.ORDENE_VEH_TOM=infi_tb_204_ordenes.ORDENE_VEH_TOM and uniinv_id=").append(unidadInversion);
		sql.append(" and a.ordsta_id='").append(status); 
		sql.append("' and a.transa_id not in ('").append(TransaccionNegocio.LIQUIDACION).append("','");  
		sql.append(TransaccionNegocio.ORDEN_VEHICULO).append("'))cantidad,"); 
		sql.append("(select decode(sum(ordene_ped_total),null,0,sum(ordene_ped_total)) from infi_tb_204_ordenes a where a.ORDENE_VEH_TOM=infi_tb_204_ordenes.ORDENE_VEH_TOM and uniinv_id=").append(unidadInversion);
		sql.append(" and a.ordsta_id='").append(status).append("' and a.transa_id not in ('");
		sql.append(TransaccionNegocio.LIQUIDACION).append("','").append(TransaccionNegocio.ORDEN_VEHICULO).append("'))total,");
		sql.append("((select decode(sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION),null,0,sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION)) from infi_tb_204_ordenes a left join infi_tb_207_ordenes_operacion on a.ordene_id=infi_tb_207_ordenes_operacion.ordene_id where infi_tb_207_ordenes_operacion.STATUS_OPERACION in('EN ESPERA','RECHAZADA') and trnf_tipo='DEB' and infi_tb_207_ordenes_operacion.ordene_id=a.ordene_id and uniinv_id=").append(unidadInversion).append(" ");
		sql.append("and a.ORDENE_VEH_TOM=infi_tb_204_ordenes.ORDENE_VEH_TOM and a.ordsta_id='").append(status);
		sql.append("' and a.transa_id not in ('").append(TransaccionNegocio.LIQUIDACION);
		sql.append("','").append(TransaccionNegocio.ORDEN_VEHICULO).append("'))+ ");
		sql.append("(select decode(sum(a.ORDENE_PED_TOTAL_PEND),null,0,sum(a.ORDENE_PED_TOTAL_PEND)) from infi_tb_204_ordenes a where uniinv_id=").append(unidadInversion);
		sql.append(" and a.ordsta_id='").append(status);
		sql.append("' and a.transa_id not in ('").append(TransaccionNegocio.LIQUIDACION).append("','").append(TransaccionNegocio.ORDEN_VEHICULO);
		sql.append("') and a.ORDENE_VEH_TOM=infi_tb_204_ordenes.ORDENE_VEH_TOM))as monto_pendiente,");
		sql.append("((select decode(sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION),null,0,sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION)) from infi_tb_204_ordenes a left join infi_tb_207_ordenes_operacion on a.ordene_id=infi_tb_207_ordenes_operacion.ordene_id where uniinv_id=").append(unidadInversion);
		sql.append(" and a.ORDENE_VEH_TOM=infi_tb_204_ordenes.ORDENE_VEH_TOM and infi_tb_207_ordenes_operacion.STATUS_OPERACION in('APLICADA') and trnf_tipo='DEB' and a.ordsta_id='");
		sql.append(status).append("' and a.transa_id not in ('").append(TransaccionNegocio.LIQUIDACION).append("','");
		sql.append(TransaccionNegocio.ORDEN_VEHICULO).append("'))");
		sql.append("-(select decode(sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION),null,0,sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION)) from infi_tb_204_ordenes a left join infi_tb_207_ordenes_operacion on a.ordene_id=infi_tb_207_ordenes_operacion.ordene_id where uniinv_id=").append(unidadInversion);
		sql.append(" and a.ORDENE_VEH_TOM=infi_tb_204_ordenes.ORDENE_VEH_TOM and infi_tb_207_ordenes_operacion.STATUS_OPERACION in('APLICADA') and trnf_tipo='CRE' and a.ordsta_id='");
		sql.append(status).append("' and a.transa_id not in ('");
		sql.append(TransaccionNegocio.LIQUIDACION).append("','").append(TransaccionNegocio.ORDEN_VEHICULO).append("')))as monto_cobrado ");
		sql.append("from infi_tb_204_ordenes left join INFI_TB_109_UI_EMPRESAS on infi_tb_204_ordenes.EMPRES_ID=INFI_TB_109_UI_EMPRESAS.EMPRES_ID left join INFI_TB_016_EMPRESAS on INFI_TB_109_UI_EMPRESAS.EMPRES_ID=INFI_TB_016_EMPRESAS.EMPRES_ID left join INFI_TB_018_VEHICULOS on ORDENE_VEH_TOM=VEHICU_ID where ORDENE_VEH_TOM is not null and uniinv_id=").append(unidadInversion);
		sql.append(" and infi_tb_204_ordenes.transa_id not in ('").append(TransaccionNegocio.LIQUIDACION).append("','");
		sql.append(TransaccionNegocio.ORDEN_VEHICULO).append("')");
		
		//Se ejecuta la consulta
		dataSet = db.get(dataSource,sql.toString());

	}
	
	/**Lista las ordenes por vehiculo y unidad de inversion en status registrada
	 * @param idVehiculo
	 * @param unidadInversionId
	 * @throws Exception
	 */
	public void listarOrdenesTitulosInventario (String idVehiculo,long unidadInversionId)throws Exception{
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("select * from infi_tb_204_ordenes ");
		sql.append("left join infi_tb_206_ordenes_titulos on  infi_tb_204_ordenes.ordene_id=infi_tb_206_ordenes_titulos.ordene_id ");
		sql.append("where uniinv_id=").append(unidadInversionId);
		sql.append(" and ordene_veh_tom=").append(idVehiculo);
		sql.append(" and ordsta_id='").append(StatusOrden.REGISTRADA).append("'");
		sql.append(" and transa_id not in ('");
		sql.append(TransaccionNegocio.ORDEN_VEHICULO).append("','");
		sql.append(TransaccionNegocio.LIQUIDACION);
		sql.append("') order by infi_tb_204_ordenes.ordene_id asc");
		
		
		//Se ejecuta la consulta
		dataSet = db.get(dataSource,sql.toString());
	}
	/**
	 * Lista todas las operaciones asociadas a un proceso de Comisiones
	 * @param ejecucionId
	 * @throws Exception
	 */
	public void ListarComisionesProceso(long ejecucionId)throws Exception{
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("select infi_tb_207_ordenes_operacion.moneda_id,INFI_TB_807_PROCESOS.fecha_inicio,operacion_nombre,ejecucion_id,infi_tb_201_ctes.client_nombre,titulo_id,infi_tb_207_ordenes_operacion.ordene_operacion_id,infi_tb_207_ordenes_operacion.monto_operacion,");
		sql.append("infi_tb_207_ordenes_operacion.status_operacion,''instruccion,infi_tb_207_ordenes_operacion.ctecta_numero from ");
		sql.append("infi_tb_204_ordenes ");
		sql.append("left join infi_tb_207_ordenes_operacion on infi_tb_204_ordenes.ordene_id=infi_tb_207_ordenes_operacion.ordene_id ");
		sql.append("left join infi_tb_201_ctes on infi_tb_204_ordenes.client_id=infi_tb_201_ctes.client_id left join INFI_TB_807_PROCESOS on infi_tb_204_ordenes.ejecucion_id=INFI_TB_807_PROCESOS.ejecucion_id");
		sql.append(" where transa_id='");
		sql.append(TransaccionNegocio.CUSTODIA_COMISIONES);
		sql.append("' AND ejecucion_id=");
		sql.append(ejecucionId);
		sql.append(" ORDER BY ejecucion_id,infi_tb_201_ctes.client_nombre,ordene_operacion_id DESC");
		
		
		//Se guarda consulta en dataset
		dataSet = db.get(dataSource,sql.toString());
		
	}//fin ListarComisionesProceso

	/**
	 * Lista las ordenes de recompra que tengan como instrucción de pago CHEQUE
	 * @param long clienteId
	 * @throws Exception
	 */
	public void listarRecompraInstruccionCheque(long clienteId) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT ORDENE_ID,TRANSA_ID,ORDENE_USR_NOMBRE,ORDENE_OPERACION_ID,STATUS_OPERACION ,MONTO_OPERACION,INFI_TB_207_ORDENES_OPERACION.MONEDA_ID FROM  INFI_TB_204_ORDENES ");
		sql.append("LEFT JOIN INFI_TB_212_ORDENES_DATAEXT ON INFI_TB_204_ORDENES.ORDENE_ID=INFI_TB_212_ORDENES_DATAEXT.ORDENE_ID ");
		sql.append("LEFT JOIN INFI_TB_207_ORDENES_OPERACION ON infi_tb_204_ordenes.ORDENE_ID=INFI_TB_207_ORDENES_OPERACION.ORDENE_ID ");
		sql.append("WHERE TRANSA_ID IN('").append(TransaccionNegocio.PACTO_RECOMPRA).append("')");
		//sql.append(",'").append(TransaccionNegocio.VENTA_TITULOS).append("'");
		//sql.append(",'").append(TransaccionNegocio.ORDEN_PAGO).append("'");
		//sql.append(",'").append(TransaccionNegocio.PAGO_CUPON).append("')");
		sql.append("AND DTAEXT_VALOR ='").append(TipoInstruccion.CHEQUE).append("' ");
		sql.append("AND CHEQUE_NUMERO IS NULL");
		sql.append(" AND CLIENT_ID=").append(clienteId);
		
		//Se ejecuta la consulta
		dataSet = db.get(dataSource,sql.toString());
	}
	
	/**
	 * Lista las ordenes de recompra que tengan como instrucción de pago CHEQUE
	 * @param long clienteId
	 * @throws Exception
	 */
	public void listarRecompraInstruccionChequeOrden(long ordeneId) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT ORDENE_ID,TRANSA_ID,ORDENE_USR_NOMBRE,ORDENE_OPERACION_ID,STATUS_OPERACION ,MONTO_OPERACION,INFI_TB_207_ORDENES_OPERACION.MONEDA_ID FROM  INFI_TB_204_ORDENES ");
		sql.append("LEFT JOIN INFI_TB_212_ORDENES_DATAEXT ON INFI_TB_204_ORDENES.ORDENE_ID=INFI_TB_212_ORDENES_DATAEXT.ORDENE_ID ");
		sql.append("LEFT JOIN INFI_TB_207_ORDENES_OPERACION ON infi_tb_204_ordenes.ORDENE_ID=INFI_TB_207_ORDENES_OPERACION.ORDENE_ID ");
		sql.append("WHERE TRANSA_ID IN('").append(TransaccionNegocio.PACTO_RECOMPRA).append("')");
		//sql.append(",'").append(TransaccionNegocio.VENTA_TITULOS).append("'");
		//sql.append(",'").append(TransaccionNegocio.ORDEN_PAGO).append("'");
		//sql.append(",'").append(TransaccionNegocio.PAGO_CUPON).append("')");
		sql.append("AND DTAEXT_VALOR ='").append(TipoInstruccion.CHEQUE).append("' ");
		sql.append("AND INFI_TB_204_ORDENES.ordene_id=").append(ordeneId);
		
		//Se ejecuta la consulta
		dataSet = db.get(dataSource,sql.toString());
	}
	
	/**
	 * Muestra un resumen de recompra para la unidad de inversión
	 * @param unidadInversion
	 * @throws Exception
	 */
	public void resumenRecompra(long unidadInversion) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("select sum(total_ordenes)total_ordenes,titulo_id,titulo_pct_recompra,sum(monto_recompra) monto_recompra from ( ");
		sql.append("select count(a.ORDENE_ID) total_ordenes, b.titulo_id, b.TITULO_PCT_RECOMPRA, ((b.TITULO_PCT_RECOMPRA * b.TITULO_UNIDADES)/100) monto_recompra  from infi_tb_204_ordenes a,  infi_tb_206_ordenes_titulos b ");
		sql.append("where a.ORDENE_ID=b.ORDENE_ID and a.ORDSTA_ID not in('CANCELADA') and a.UNIINV_ID=");
		sql.append(unidadInversion);
		sql.append(" and b.titulo_pct_recompra > 0 and a.transa_id in ('");
		sql.append(TransaccionNegocio.TOMA_DE_ORDEN);
		sql.append("','");
		sql.append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA);
		sql.append("')");
		sql.append("group by b.titulo_id, b.TITULO_PCT_RECOMPRA, ((b.TITULO_PCT_RECOMPRA * b.TITULO_UNIDADES)/100)");
		sql.append(") group by titulo_id,titulo_pct_recompra order by titulo_pct_recompra asc");
		
	//Se ejecuta la consulta
		dataSet = db.get(dataSource,sql.toString());
	}
	
	/**
	 * Resumen por precio para una unidad de inversion especifica
	 * @param unidadInversion
	 * @throws Exception
	 */
	public void resumenPorPrecio(long unidadInversion,Date fechaDesde,Date fechaHasta)throws Exception{
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
		StringBuffer sql = new StringBuffer();
		
		sql.append("select ordene_ped_precio, count(ordene_id) ordenes, sum(ordene_ped_monto) monto_resumen_precio,  nvl(sum(ordene_ped_monto),0) monto_solicitado ,nvl(sum(ordene_adj_monto),0) monto_adjudicado from infi_tb_204_ordenes a");
		sql.append(" where a.UNIINV_ID=").append(unidadInversion).append(" and a.transa_id in ('");
		sql.append(TransaccionNegocio.TOMA_DE_ORDEN).append("','").append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA);
		sql.append("') and a.ordsta_id not in('").append(StatusOrden.CANCELADA).append("','").append(StatusOrden.PENDIENTE).append("') ");
		sql.append("and (ORDENE_PED_FE_ORDEN>=to_date('").append(simpleDateFormat.format(fechaDesde)).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("') and ORDENE_PED_FE_ORDEN<=to_date('").append(simpleDateFormat.format(fechaHasta)).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("'))");
		sql.append(" group by  ordene_ped_precio order by ordene_ped_precio asc");
		//Se ejecuta la consulta
		dataSet = db.get(dataSource,sql.toString());
	}
	
	/**
	 * Busca el o los números de orden que tenga relación con el id enviado
	 * @param idOrdenRelacion número de orden a buscar por el campo de relación
	 * @return Array de números de orden si encuentra. Devuelve null en caso contrario.
	 */
	public long[] listarNumeroOrdenIdRelacion(long idOrdenRelacion) throws Exception{
		StringBuffer sql = new StringBuffer();
		long[] numerosOrdenes = null;
		sql.append("select ordene_id from infi_tb_204_ordenes where ordene_id_relacion =").append(idOrdenRelacion);
		//Se ejecuta la consulta
		DataSet dataSet = db.get(dataSource,sql.toString());
		if (dataSet.count()>0){
			dataSet.first();
			numerosOrdenes = new long[dataSet.count()];
			int i=0;
			while (dataSet.next()){
				numerosOrdenes[i] = Long.parseLong(dataSet.getValue("ordene_id"));
				i++;
			}
		}
		return numerosOrdenes;
	}
	
	/**
	 * Valida si existen operaciones pendientes por debitar al vehiculo involucrado en caso de ser diferente al BDV
	 * @param unidadInversion
	 * @throws Exception
	 */
	public void validarOperacionesVehiculo(long unidadInversion)throws Exception{
		
		StringBuffer sql = new StringBuffer();	
		sql.append("select op.STATUS_OPERACION,op.ORDENE_OPERACION_ID,op.ORDENE_ID,op.TRNF_TIPO,op.CTECTA_NUMERO,op.SERIAL,op.MONTO_OPERACION,op.MONEDA_ID,op.CODIGO_OPERACION,op.STATUS_OPERACION" +
				" from infi_tb_207_ordenes_operacion op where ordene_id=(select ordene_id from infi_tb_204_ordenes where uniinv_id=").append(unidadInversion).append(" and transa_id='").append(TransaccionNegocio.ORDEN_VEHICULO).append("')");
		//Se guarda la info en el dataset
		dataSet = db.get(dataSource,sql.toString());
	}
	
	/**
	 * Actualiza las ordenes del proceso de recepción batch
	 * @throws Exception en caso de error
	 */
	public String actualizarOrdenesRecepcionSubastaBatch(int unidadInversion) throws Exception{
		String sql = " update infi_tb_204_ordenes set ordsta_id='" + StatusOrden.ADJUDICADA + "' " +
		" where ordene_id not in (" +
		" select b.ordene_id from infi_tb_207_ordenes_operacion a, infi_tb_204_ordenes b " +
		" where a.ordene_id = b.ordene_id and b.uniinv_id=" + unidadInversion + " and " +
		" status_operacion in('" + ConstantesGenerales.STATUS_EN_ESPERA + "','" + ConstantesGenerales.STATUS_RECHAZADA + "') " +
		" and b.ordsta_id in('" + StatusOrden.PROCESO_ADJUDICACION + "','" + StatusOrden.REGISTRADA + "')) " +
		" and ordsta_id in('" + StatusOrden.PROCESO_ADJUDICACION + "','" + StatusOrden.REGISTRADA + "') and uniinv_id=" + unidadInversion;
		return sql;
	}

	/**
	 * Actualiza las ordenes del proceso sitme batch
	 * @param numeroProceso número de proceso que se está calculando
	 * @return Arreglo de consultas sql a ejecutar.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public String[] actualizarOrdenesRecepcionSitmeBatch(int numeroProceso) throws Exception{
		ArrayList consultasSQL = new ArrayList();
		String sql = null;		
		sql = "select uniinv_id, count(b.ordene_id) cuenta from infi_tb_207_ordenes_operacion a, "
				+ " infi_tb_204_ordenes b, infi_tb_106_unidad_inversion c, infi_tb_101_inst_financieros d, "
				+ " infi_tb_804_control_arch_det e where a.ordene_id = b.ordene_id and b.uniinv_id=c.undinv_id and "
				+ " c.insfin_id = d.insfin_id and d.TIPO_PRODUCTO_ID = '"
				+ ConstantesGenerales.ID_TIPO_PRODUCTO_SITME
				+ "' and "
				+ " e.ejecucion_id = "
				+ numeroProceso
				+ " and e.ordene_id = a.ordene_id and "
				+ " e.ordene_operacion_id = a.ordene_operacion_id group by uniinv_id ";

		DataSet unidades = db.get(this.dataSource, sql);
		while (unidades.next()) {
			consultasSQL.add(actualizarOrdenesRecepcionSitme(Integer.parseInt(unidades.getValue("uniinv_id"))));			
		}
		String[] sqls = new String[consultasSQL.size()];
		for (int i=0; i < consultasSQL.size(); i++ ){
			sqls[i] = consultasSQL.get(i).toString();
		}
		return sqls;
	}

		 
	/**
	 * Recibe un conjunto de unidades de inversión a ejecutar
	 * @param unidadDeInversion id de la unidad a actualizar
	 * @return instrucción sql a ejecutar
	 * @throws Exception en caso de error
	 */
	 private String actualizarOrdenesRecepcionSitme(int unidadDeInversion) throws Exception{
		String sql = " update infi_tb_204_ordenes set ordsta_id='" + StatusOrden.ADJUDICADA + "' " +
				" where ordene_id not in (" +
				" select b.ordene_id from infi_tb_207_ordenes_operacion a, infi_tb_204_ordenes b, infi_tb_106_unidad_inversion c, infi_tb_101_inst_financieros d " +
				" where a.ordene_id = b.ordene_id and b.uniinv_id in(" + unidadDeInversion + ") and b.uniinv_id=c.undinv_id and c.insfin_id = d.insfin_id and d.TIPO_PRODUCTO_ID ='" + ConstantesGenerales.ID_TIPO_PRODUCTO_SITME + "' and " +
				" status_operacion in('" + ConstantesGenerales.STATUS_EN_ESPERA + "','" + ConstantesGenerales.STATUS_RECHAZADA + "') " +
				" and b.ordsta_id in('" + StatusOrden.PROCESO_ADJUDICACION + "','" + StatusOrden.REGISTRADA + "')) " +
				" and ordsta_id in('" + StatusOrden.PROCESO_ADJUDICACION + "','" + StatusOrden.REGISTRADA + "') and uniinv_id in(" + unidadDeInversion + ")";
		return sql;		 
	 }
	 

	 /**
	  * Actualiza el estatus de todas las ordenes asociadas a una unidad de inversión y a una transacción	  
	  * específica 
	  * @param estatus estatus en que debe quedar la orden
	  * @param unidadInversion sólo se actualizarán las ordenes que pertenezcan a esta unidad de inversión
	  * @param transaccion  sólo se actualizarán las ordenes que pertenezcan a esta transacción
	  * @param estatusActual sólo se actualizarán las ordenes que posean este estatus
	  * @return consulta sql que se debe ejecutar
	  */
	 public String actualizarStatus(String estatus, int unidadInversion, String transaccion, String estatusActual){
		 String sql = "update infi_tb_204_ordenes set ordsta_id='" + estatus + "' where uniinv_id=" + unidadInversion + 
		 " and transa_id='" + transaccion + "' and ordsta_id='" + estatusActual + "'";
		 return sql;
	 }
	 
	 /**
	  * Obtiene los consumos realizados por el cliente
	  * @param idCliente id del cliente a consultar
	  * @param idMoneda id de la moneda a consultar
	  * @param idConcepto id del concepto para traer sólo las ordenes asociadas al mismo, puede ser null y busca todas
	  * @throws Exception en caso de error
	  */
	 public void obtenerMontoSolicitudOrdenesSitme(long idCliente, String idMoneda, String idConcepto) throws Exception{
		 StringBuilder sql = new StringBuilder("	 select decode(sum(a.ordene_ped_monto),null,0,sum(a.ordene_ped_monto)) monto, 'DIARIO' as periocidad from infi_tb_204_ordenes a, infi_tb_106_unidad_inversion b, infi_tb_101_inst_financieros c where " + 
		 " a.uniinv_id = b.undinv_id and " + 
		 " b.insfin_id = c.insfin_id and c.tipo_producto_id = '" + 
		 ConstantesGenerales.ID_TIPO_PRODUCTO_SITME + "' " + 
		 " and a.client_id=" + idCliente + " and a.ordsta_id in('" + 
		 StatusOrden.REGISTRADA + "','" + StatusOrden.ENVIADA + "','" + 
		 StatusOrden.PROCESO_ADJUDICACION + "','" + StatusOrden.ADJUDICADA + "','" + StatusOrden.LIQUIDADA + "')" +
		 " and a.transa_id='" + TransaccionNegocio.TOMA_DE_ORDEN + "' and b.moneda_id='" + idMoneda + "' " +
		 " and trunc(a.ordene_ped_fe_orden) = trunc(sysdate) ");
		 
		 if (idConcepto != null && !idConcepto.equals("")){
			 sql.append(" and concepto_id=" + idConcepto);
		 }
		 
		 sql.append(" union " +
		 " select decode(sum(a.ordene_ped_monto),null,0,sum(a.ordene_ped_monto)) monto, 'MENSUAL' as periocidad from infi_tb_204_ordenes a, infi_tb_106_unidad_inversion b, infi_tb_101_inst_financieros c where " +
		 " a.uniinv_id = b.undinv_id and " + 
		 " b.insfin_id = c.insfin_id and c.tipo_producto_id = '" + 
		 ConstantesGenerales.ID_TIPO_PRODUCTO_SITME + "' " + 
		 " and a.client_id=" + idCliente + " and a.ordsta_id in('" + 
		 StatusOrden.REGISTRADA + "','" + StatusOrden.ENVIADA + "','" + 
		 StatusOrden.PROCESO_ADJUDICACION + "','" + StatusOrden.ADJUDICADA + "','" + StatusOrden.LIQUIDADA + "')" +
		 " and a.transa_id='" + TransaccionNegocio.TOMA_DE_ORDEN + "' and b.moneda_id='" + idMoneda + "' " +
		 " and trunc(a.ordene_ped_fe_orden) between to_date('01' || '/' || to_char(sysdate, 'MM') || '/' || to_char(sysdate, 'YYYY'),'dd/mm/yyyy') " +
		 " and  to_date(to_char(trunc(last_day(sysdate)),'dd/mm/yyyy'), 'dd/mm/yyyy')");
		 
		 if (idConcepto != null && !idConcepto.equals("")){
			 sql.append(" and concepto_id=" + idConcepto);
		 }
		 
		 sql.append(" union " +
		 " select decode(sum(a.ordene_ped_monto),null,0,sum(a.ordene_ped_monto)) monto, 'ANUAL' as periocidad from infi_tb_204_ordenes a, infi_tb_106_unidad_inversion b, infi_tb_101_inst_financieros c where " +
		 " a.uniinv_id = b.undinv_id and " + 
		 " b.insfin_id = c.insfin_id and c.tipo_producto_id = '" + 
		 ConstantesGenerales.ID_TIPO_PRODUCTO_SITME + "' " + 
		 " and a.client_id=" + idCliente + " and a.ordsta_id in('" + 
		 StatusOrden.REGISTRADA + "','" + StatusOrden.ENVIADA + "','" + 
		 StatusOrden.PROCESO_ADJUDICACION + "','" + StatusOrden.ADJUDICADA + "','" + StatusOrden.LIQUIDADA + "')" +
		 " and a.transa_id='" + TransaccionNegocio.TOMA_DE_ORDEN + "' and b.moneda_id='" + idMoneda + "' " +
		 " and trunc(a.ordene_ped_fe_orden) between to_date('01/01/' || to_char(sysdate, 'YYYY'),'dd/mm/yyyy') " +
		 " and  to_date('31/12/' || to_char(sysdate, 'YYYY'),'dd/mm/yyyy') ");
		 
		 if (idConcepto != null && !idConcepto.equals("")){
			 sql.append(" and concepto_id=" + idConcepto);
		 }
		
		 dataSet = db.get(dataSource, sql.toString());
	 }
	 
	 /**
	  * Lista las ordenes que deben ser liquidadas para SITME
	  * @throws Exception en caso de error
	  */
	 public String listarMontosSitmeParaLiquidacion(String unidadInversionId) throws Exception{
		 String sql = "select insfin_id,empres_id,empres_rif, empres_cuenta, vehicu_id,vehicu_rif, vehicu_numero_cuenta,sum(monto) monto from(" +
		 		" select e.empres_id,e.empres_nombre, empres_rif, " +
		 		"empres_cuenta, f.vehicu_id,f.vehicu_rif, f.vehicu_numero_cuenta, " +
		 		"(a.ordene_ped_total - a.ordene_ped_comisiones) monto,d.insfin_id " +
		 		"from infi_tb_204_ordenes a , infi_tb_106_unidad_inversion c,infi_tb_101_inst_financieros d, " +
		 		"infi_tb_016_empresas e, infi_tb_018_vehiculos f " +
		 		"where a.uniinv_id = c.undinv_id and c.insfin_id = d.insfin_id and a.contraparte = e.empres_id " +
		 		"and d.TIPO_PRODUCTO_ID = '" + ConstantesGenerales.ID_TIPO_PRODUCTO_SITME +"' and transa_id not in " +
		 		"('" + TransaccionNegocio.ORDEN_VEHICULO + "','" + TransaccionNegocio.LIQUIDACION + "')" +
		 		" and ordene_adj_monto > 0 and ordsta_id in('" + StatusOrden.ADJUDICADA + "') and a.ordene_veh_tom = f.vehicu_id and a.ordene_id_relacion is null and a.uniinv_id=" + unidadInversionId + 
		 		") group by insfin_id,empres_id,empres_rif, empres_cuenta, vehicu_id,vehicu_rif, vehicu_numero_cuenta";
		 return sql;
	 }
	 
	 /**
	  * Construye el SQL para la actualazión masiva de las ordenes relacionadas a la creación del deposito de la contraparte 
	  * @param idRelacion id de la orden relacionada
	  * @param idContraparte id de la empresa o contraparte
	  * @return un sql que debe ejecutar el modelo
	  */
	 public String actualizarOrdenesIdRelacion(long idRelacion, long idEmpresa, String unidadInversion,String tipoProducto){
		 String sql = "update infi_tb_204_ordenes set ordene_id_relacion=" + idRelacion + " where ordene_id in(" +
		 		"select ordene_id from infi_tb_204_ordenes a where empres_id=" + idEmpresa + 
		 		" and transa_id in('" + TransaccionNegocio.TOMA_DE_ORDEN + "','" + TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA + "') " +
		 		" and tipo_producto_id='" + tipoProducto + "' and uniinv_id=" + unidadInversion + " " +
		 		" and ordsta_id in('" + StatusOrden.ADJUDICADA + "') and ordene_id_relacion is null) ";
		 return sql;
	 }
	 
	 /**Listar los documentos asociados a un ordene_id
		 * @param Long idOrden
		 * */
		public void consultarDocumentosOrden(long idOrden) throws Exception{
			
			StringBuffer sql = new StringBuffer();
			sql.append("select d.ORDENE_DOC_ID, d.NOMBRE from INFI_TB_208_ORDENES_DOCUMENTOS d");
			sql.append(" where d.ORDENE_ID=");
			sql.append(idOrden);
			dataSet = db.get(dataSource, sql.toString());
		}
		
		/**Busca los detalles de las operaciones asociadas a una orden
		 * 
		 * */
		public void listarDetallesOrdenOperacion(long idOrden) throws Exception{
			
			StringBuffer sql = new StringBuffer();
			sql.append("select aplica_reverso,ordene_operacion_id,ordene_relac_operacion_id,ordene_operacion_error,OPERACION_NOMBRE,trnfin_id,");
			sql.append("trnf_tipo,tasa,status_operacion,serial,monto_operacion,moneda_id,numero_retencion,fecha_aplicar,fecha_procesada,");
			sql.append("in_comision,codigo_operacion,ctecta_numero,ctecta_nombre,ctecta_bcocta_bco,ctecta_bcocta_direccion,ctecta_bcocta_swift,");
			sql.append("ctecta_bcocta_telefono,ctecta_bcocta_aba,ctecta_bcocta_pais,ctecta_bcoint_bco,ctecta_bcoint_direccion,ctecta_bcoint_swift,");
			sql.append("ctecta_bcoint_bic,ctecta_bcoint_telefono,ctecta_bcoint_aba,ctecta_bcoint_pais from INFI_TB_207_ORDENES_OPERACION  op where op.ORDENE_ID=").append(idOrden);
			sql.append(" ORDER BY ordene_operacion_id");
			dataSet = db.get(dataSource, sql.toString());
			
		}
		
		/**busca a traves del ordene_id los detalles de la orden a un cliente especifico
		 * 
		 * */
		public void listarDetallesOrden(long idOrden) throws Exception{
			
			StringBuffer sql = new StringBuffer();
			sql.append("select ordene_id,ordene_id_relacion,to_char(ord.ordene_fe_ult_act, '"+ConstantesGenerales.FORMATO_FECHA+ "') as fecha_recompra,ordene_observacion,ord.tipo_producto_id,uniinv_id,ord.client_id,ord.ORDSTA_ID,sistema_id,ord.empres_id,contraparte,transa_id,");
			sql.append("case when ord.ENVIADO=0 then 'No' when ord.ENVIADO=1 then 'Si' end enviado,ordene_cte_seg_bco,ordene_cte_seg_seg,u.insfin_id, ord.sector_id, ord.concepto_id, ord.codigo_id as cod_actividad_ec,");
			sql.append("ordene_cte_seg_sub,ordene_cte_seg_infi,ordene_ped_fe_orden,ordene_ped_fe_valor,ordene_ped_monto,ordene_ped_total_pend,");
			sql.append("ordene_ped_total,ordene_ped_int_caidos,bloter_id,ordene_ped_cta_bstip,ctecta_numero,ordene_ped_precio,ordene_ped_rendimiento,");
			sql.append("ordene_ped_in_bdv,ord.moneda_id,ordene_ped_rcp_precio,ordene_adj_monto,ordene_usr_nombre,ordene_usr_cen_contable,ordene_usr_sucursal,");
			sql.append("ordene_usr_terminal,ordene_veh_tom,ordene_veh_col,ordene_veh_rec,ordene_ejec_id,ordene_ped_comisiones,ordene_fecha_adjudicacion,");
			sql.append("ordene_fecha_liquidacion,ordene_fecha_custodia,ordene_financiado,ordene_comision_emisor,ordene_tasa_pool,ordene_ganancia_red,");
			sql.append("to_char(ord.ordene_ped_fe_orden, '"+ConstantesGenerales.FORMATO_FECHA+"') as fecha_orden, to_char(ord.ordene_ped_fe_valor, '"+ConstantesGenerales.FORMATO_FECHA+ "') as fecha_valor_orden,");
			//NM29643 infi_TTS_466
			sql.append("((round(ord.ordene_ped_fe_orden,'DAY'))-(round(u.undinv_fe_emision,'DAY'))) as dias_interes_transcurridos, "); 
			sql.append("ordene_comision_oficina,ordene_comision_operacion,ct.TIPPER_ID,inf.insfin_id,ord.ORDENE_ESTATUS_BCV,ord.ORDENE_ID_BCV from infi_tb_201_ctes ct, infi_tb_204_ordenes ord, infi_tb_106_unidad_inversion u, infi_tb_101_inst_financieros inf");
			sql.append(" where ord.UNIINV_ID=u.UNDINV_ID(+) and u.insfin_id=inf.insfin_id(+) and ct.CLIENT_ID=ord.CLIENT_ID and ord.ordene_id=").append(idOrden);
			dataSet = db.get(dataSource, sql.toString());
		}	 
		
		/**
		 * Actualiza la fecha de la orden de recompra con la de la orden original del cliente
		 * @param idOrden id de la orden original
		 * @return un array de consultas sql de actualización
		 */
		public String[] actualizarOrdenDeRecompra(long idOrden){
			String[] actualizaciones = new String[2];
			actualizaciones[0] = "update infi_tb_204_ordenes set ordene_ped_fe_valor=(select ordene_fe_ult_act from infi_tb_204_ordenes where ordene_id=" + idOrden + ")" +
					" where ordene_id_relacion = " + idOrden;
			actualizaciones[1] = " update infi_tb_207_ordenes_operacion set fecha_aplicar=(select ordene_fe_ult_act from infi_tb_204_ordenes where ordene_id= " + idOrden + ")" +
                    " where ordene_id = (select ordene_id from infi_tb_204_ordenes  where ordene_id_relacion = " + idOrden + ")";
			return actualizaciones;
		}
		
		/**
		 * Busca las ordenes de compra y recompra en un rango de fechas indicado
		 * @param fechaDesde fecha desde en formato dd/mm/yyyy
		 * @param fechaHasta fecha hasta en formato dd/mm/yyyy
		 * @throws Exception en caso de error
		 */
		public String listarOrdenesDeRecompra(String fechaDesde, String fechaHasta, String tipoProductoId) throws Exception{
			String sql = "select d.undinv_nombre,a.tipo_producto_id, 'P' as operacion, a.ordsta_id,a.ordene_id, to_char(a.ordene_ped_fe_valor,'dd/mm/yyyy') ordene_ped_fe_valor," +
					" b.client_nombre, c.titulo_id, c.titulo_unidades valor_nominal, " +
					" (c.titulo_unidades*c.titulo_pct_recompra/100 +c.titulo_mto_int_caidos) valor_efectivo, " +
					" c.titulo_pct_recompra,c.titulo_mto_int_caidos from infi_tb_204_ordenes a, " +
					" infi_tb_201_ctes b, infi_tb_206_ordenes_titulos c, infi_tb_106_unidad_inversion d where a.client_id = b.client_id and " +
					" a.ordene_id = c.ordene_id and a.transa_id in('" + TransaccionNegocio.TOMA_DE_ORDEN + "','" + TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA + "') " +
					" and a.ordsta_id != '" + StatusOrden.CANCELADA + "' and a.ordene_ped_fe_valor between to_date('" + fechaDesde + "','dd//mm/yyyy') and to_date('" + fechaHasta + "','dd//mm/yyyy') and a.uniinv_id = d.undinv_id and c.TITULO_PCT_RECOMPRA > 0 and a.tipo_producto_id='" + tipoProductoId + "' " +
					" union " +
					" select d.undinv_nombre,a.tipo_producto_id, 'S' as operacion, a.ordsta_id,a.ordene_id, to_char(a.ordene_ped_fe_valor,'dd/mm/yyyy') ordene_ped_fe_valor," +
					" b.client_nombre, c.titulo_id, c.titulo_unidades valor_nominal, " +
					" (c.titulo_unidades*c.titulo_pct_recompra/100 +c.titulo_mto_int_caidos) valor_efectivo, " +
					" c.titulo_pct_recompra,c.titulo_mto_int_caidos from infi_tb_204_ordenes a, " +
					" infi_tb_201_ctes b, infi_tb_206_ordenes_titulos c, infi_tb_106_unidad_inversion d where a.client_id = b.client_id and " +
					" a.ordene_id = c.ordene_id and a.transa_id in('" + TransaccionNegocio.PACTO_RECOMPRA + "') and a.ordene_ped_fe_valor between to_date('" + fechaDesde + "','dd//mm/yyyy') and to_date('" + fechaHasta + "','dd//mm/yyyy') and a.uniinv_id = d.undinv_id and a.tipo_producto_id='" + tipoProductoId + "'" +
					" order by client_nombre";
			return sql;
		}
						
		/**
		 * Actualiza las observaciones
		 * @param idOrden id de la orden 
		 * @param observaciones observaciones que se desean actualizar
		 * @return sql para actualizar
		 */
		public String actualizarObservaciones(long idOrden,String observaciones){
			String sql = "update infi_tb_204_ordenes set ordene_observacion='" + observaciones + "'" + 
			" where ordene_id=" + idOrden;
			return sql;
		}
		
		
		/**
		 * Metodo que lista las ordenes liquidadas que contienen operaciones rechazadas
		 * @param idOrden
		 * @param idCliente
		 * @param idUnidadInversion
		 * @param feOrdenDesde
		 * @param feOrdenHasta
		 * @throws Exception
		 */
		public void listarOrdenesOperacionesRechadasLiquid(String idOrden,String idCliente, String feOrdenDesde,String feOrdenHasta) throws Exception{
			
			StringBuffer sql = new StringBuffer();
			StringBuffer filtro = new StringBuffer("");
			
			sql.append("SELECT o.ordene_id, o.ordene_ped_fe_orden, o.ordene_ped_monto, ui.undinv_nombre, ui.undinv_serie, c.client_nombre FROM INFI_TB_204_ORDENES o, infi_tb_106_unidad_inversion ui, INFI_TB_201_CTES c ");
			sql.append(" where o.ordsta_id  = '").append(StatusOrden.LIQUIDADA).append("' ");
			sql.append(" and o.ordene_id IN (select ordene_id from infi_tb_207_ordenes_operacion where status_operacion = '").append(ConstantesGenerales.STATUS_RECHAZADA).append("') ");
			sql.append(" and o.uniinv_id = ui.undinv_id(+) ");
			sql.append(" and o.client_id = c.client_id ");

			if(idOrden!=null){
				filtro.append(" and o.ordene_id = ").append(idOrden);
			}
			if(idCliente!=null){
				filtro.append(" and o.client_id = ").append(idCliente);
			}
			if(feOrdenHasta!=null && feOrdenDesde!=null){
				filtro.append(" and o.ordene_ped_fe_orden between to_date('").append(feOrdenDesde).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("') and to_date('").append(feOrdenHasta).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("')");
			}
			sql.append(filtro);			
			sql.append(" order by ordene_id");
			dataSet = db.get(dataSource, sql.toString());
		}

		/**
		 * Retorna una sentencia sql para actualizar el status de las Operaciones Rechazadas o En espera de una orden Especifica (liquidada)
		 * @param idOrden
		 * @return sentencia sql para actualizar las operaciones
		 * @throws Exception
		 */
		public String cancelarOperacionesOrden(long idOrden, String usuarioCancela)throws Exception{
			
			StringBuffer sql=new StringBuffer();
			
			sql.append("UPDATE  INFI_TB_207_ORDENES_OPERACION ");
			sql.append("SET  STATUS_OPERACION='").append(ConstantesGenerales.STATUS_CANCELADA).append("', ");
			sql.append(" SERIAL = '").append(Utilitario.DateToString(new Date(), "ddMMyyyy")).append("-").append(usuarioCancela).append("' ");
			sql.append(" WHERE ORDENE_ID = ").append(idOrden);
			sql.append(" AND STATUS_OPERACION IN ('").append(ConstantesGenerales.STATUS_RECHAZADA).append("','").append(ConstantesGenerales.STATUS_EN_ESPERA).append("') ");
			
			return sql.toString();
		}
		
		/**
		 * Busca las cuentas con más de una aparición en el conjunto de ordenes de una unidad de inversión específica
		 * @param idUnidadInversion
		 * @throws Exception 
		 */
		public String cuentasRepetidasOperacionesOrden (long idUnidadInversion) throws Exception{
			
			StringBuilder sql = new StringBuilder();
			sql.append(" select ctecta_numero, count(ctecta_numero) apariciones from ( select distinct ord.ordene_id, ope.ctecta_numero from infi_tb_204_ordenes ord, infi_tb_207_ordenes_operacion ope " +
					" where ord.ordene_id = ope.ordene_id and ord.uniinv_id= " + idUnidadInversion+
					" and ord.ordsta_id in ('ADJUDICADA','LIQUIDADA', 'PROCESO_ADJUDICACION')) " +
					" group by ctecta_numero having count(ctecta_numero) > 1");   
			return sql.toString();
		}
		
		/**
		 * Consulta el resumen agrupado por estatus de una unidad de inversión
		 * @param idUnidadInversion unidad de inversión
		 * @throws Exception en caso de error
		 */
		public void resumenOrdenes(long idUnidadInversion) throws Exception{
			String sql = "select ordsta_id estatus,count(ordsta_id) total from infi_tb_204_ordenes where uniinv_id=" + idUnidadInversion+" group by ordsta_id";
			dataSet = db.get(dataSource, sql);
		}
		
		//Metodo modificado en requerimiento TTS_443 NM26659_07/04
		public int listarDatosBasicosOrdenPorStatus(long orden_id,String... status) throws Exception{

			StringBuffer sql=new StringBuffer();
			
			sql.append("SELECT ORD.ORDENE_ID,ORD.ORDENE_PED_MONTO AS ORDENE_PED_MONTO,ORD.CTECTA_NUMERO,ORD.TIPO_PRODUCTO_ID, ");
			sql.append("ORD.ORDENE_COMISION_OPERACION,ORD.ORDSTA_ID,ORDENE_PED_TOTAL,ORD.ORDENE_PED_COMISIONES,ORD.ORDENE_PED_FE_VALOR, ");
			sql.append("ORD.UNIINV_ID, ORD.ORDENE_PED_FE_ORDEN, ORD.CLIENT_ID,CONCEPTO_ID,CTA_ABONO ");			
			sql.append("FROM INFI_TB_204_ORDENES ORD ");
			sql.append("WHERE ORD.ORDENE_ID =" );			
			sql.append(orden_id);
			
			if(status.length>0 && status[0]!=null){
				sql.append(" AND ORD.ORDSTA_ID IN (");
				int count=0;
				for (String string : status) {
					if(count>0){
						sql.append(",");
					}
					sql.append("'"+string+"'");
					++count;
				}
					sql.append(") ");
			}
						
			//sql.append(status);
			//sql.append("'");
			
			//System.out.println("BUSQUEDA DE ORDENES: " + sql.toString());
			dataSet = db.get(dataSource, sql.toString());
			return dataSet.count();
			
		}
				
		
		/**
		 * Valida si la orden registrada fue tomada por otro canal (CLAVENET) y retorna el id de ticket asociado a la orden original en CLAVE
		 * @param idOrden id de orden
		 * @throws Exception en caso de error
		 */
		public String validarOrdenClave(long idOrden) throws Exception{
			String sql = "SELECT DTAEXT_VALOR FROM INFI_TB_212_ORDENES_DATAEXT D WHERE D.DTAEXT_ID='NRO_TICKET' AND D.ORDENE_ID="+idOrden;
			dataSet = db.get(dataSource, sql);
			if(dataSet.count()>0){
				//La orden fue tomada desde CLAVENET
				dataSet.first();
				dataSet.next();
				return dataSet.getValue("DTAEXT_VALOR");
			}
			return null; //La orden fue tomada desde INFI
		}
		
		
		public String modificarValorDataExtendida(long orden,String dataExtTipo,String valor) throws Exception{
			StringBuffer sql=new StringBuffer();
			
			sql.append("UPDATE INFI_TB_212_ORDENES_DATAEXT DEXT SET ");
			sql.append("DEXT.DTAEXT_VALOR = '");
			sql.append(valor);
			sql.append("' ");
			sql.append(" WHERE DEXT.ORDENE_ID=");
			sql.append(orden);
			sql.append(" AND DEXT.DTAEXT_ID='");
			sql.append(dataExtTipo);
			sql.append("'");
			//System.out.println("ACTUALIZACION DATAEXTENDIDA: " + sql.toString());
			return sql.toString();
			
		}
		
		public String insertarValoresDataExtendida(long orden,String dataExtTipo,String valor)throws Exception{
			
			StringBuffer sql=new StringBuffer();
			
			sql.append("INSERT INTO INFI_TB_212_ORDENES_DATAEXT (ORDENE_ID,DTAEXT_ID,DTAEXT_VALOR) VALUES (");
			sql.append(orden);
			sql.append(",");
			sql.append("'").append(dataExtTipo).append("'");
			sql.append(",");
			sql.append("'").append(valor).append("'");
			sql.append(")");
			
				//db.exec(dataSource,sql.toString());
			return sql.toString();
			
		}

		/**
		 * Valida si la orden fue adjudicada y tomada desde CLAVENET, actualiza su estatus (BD OPICS)
		 * @param idOrden id de orden
		 * @throws Exception en caso de error
		 */
		public void actualizarEstatusOrdenClave(long idOrden, String estatus, String fechaCobro)throws Exception{			
			try {
				String sql = null;
				String idOrdenClave = validarOrdenClaveAdjudicada(idOrden);
				
				logger.info("orden infi: "+idOrden+", orden clave: "+idOrdenClave + " Estatus " + estatus);
				SolicitudClavenet solicitud = null;
				if (idOrdenClave != null) {
					solicitud = new SolicitudClavenet();
					solicitud.setIdOrden(Long.valueOf(idOrdenClave));
					solicitud.setEstatus(estatus);
					solicitud.setFechaCobro(fechaCobro);
					solicitud.setFechaEnvioSwift(fechaCobro);
					sql = actualizarSolicitudClavenet(solicitud, null);
					//sql.append("UPDATE SOLICITUDES_SITME SET ESTATUS='").append(estatus).append("' WHERE  ID_ORDEN=").append(idOrdenClave);			
					db.exec(dataSource, sql);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}			
		}
		
		/**
		 * Actualiza la orden registrada desde Clavenet en la BD OPICS
		 * @param idOrden id de orden
		 * @throws Exception en caso de error
		 */
		public String actualizarSolicitudClavenet(SolicitudClavenet solicitud, String estatusAnterior)throws Exception{
			StringBuffer sqlupdate = new StringBuffer();			
			logger.info("actualizarSolicitudClavenet "+solicitud.getIdOrden());		
			
			sqlupdate.append("UPDATE SOLICITUDES_SITME SET ");
			sqlupdate.append(" FECHA_MODIFICACION= SYSDATE");
			if (solicitud.getIdOrdenInfi()>0) {
				sqlupdate.append(",NUMERO_ORDEN_INFI= " + solicitud.getIdOrdenInfi());
			}
			if (solicitud.getCedRifContraparte()!=null && solicitud.getCedRifContraparte().length()>0) {
				sqlupdate.append(",CED_RIF_CONTRAPARTE= '" + solicitud.getCedRifContraparte()+"'");
			}			
			if (solicitud.getMontoSolicitado()!=null) {
				sqlupdate.append(",MONTO_SOLICITADO = " + solicitud.getMontoSolicitado());
			}			
			if (solicitud.getMontoComision()!=null) {
				sqlupdate.append(",MONTO_COMISION = " + solicitud.getMontoComision());
			}			
			if (solicitud.getPorcentajeComision()>0) {//TODO VALIDAR
				sqlupdate.append(",PORC_COMISION = " + solicitud.getPorcentajeComision());
			}			
			if (solicitud.getNumeroBloqueo()!=null && solicitud.getNumeroBloqueo().length()>0) {
				sqlupdate.append(",NUM_BLOQUEO = '" + solicitud.getNumeroBloqueo()+"'");
			}			
			if (solicitud.getMontoAdjudicado()!=null) {
				//System.out.println("COLOCA EL MONTO ADJUDICADO: " + solicitud.getMontoAdjudicado());
				sqlupdate.append(",MONTO_ADJUDICADO= " + solicitud.getMontoAdjudicado());
			}			
			if (solicitud.getEstatus()!=null && solicitud.getEstatus().length()>0) {
				sqlupdate.append(",ESTATUS = '" + solicitud.getEstatus()+"'");
			}			
			if (solicitud.getFechaRegistro()!=null) {
				sqlupdate.append(",FECHA_REGISTRO = TO_DATE('" + solicitud.getFechaRegistro()+"','DD/MM/YYYY')");
			}			
			if (solicitud.getHoraRegistro()!=null && solicitud.getHoraRegistro().length()>0) {
				sqlupdate.append(",HORA_REGISTRO = '" + solicitud.getHoraRegistro()+"'");
			}			
			if (solicitud.isFechaActualTramite()) {
				sqlupdate.append(",FECHA_TRAMITE = SYSDATE");
			}		
			
			if(solicitud.getFechaAdjudicacion()!=null){
				sqlupdate.append(",FECHA_ADJUDICACION = TO_DATE('" + solicitud.getFechaAdjudicacion()+"','DD/MM/YYYY')");
			}
			
			if (solicitud.getFechaCobro()!=null) {
				sqlupdate.append(",FECHA_COBRO = TO_DATE('" + solicitud.getFechaCobro()+"','DD/MM/YYYY')");
			}			
			if (solicitud.getFechaEnvioSwift()!=null) {
				sqlupdate.append(",FECHA_ENV_SWIFT = TO_DATE('" + solicitud.getFechaEnvioSwift()+"','DD/MM/YYYY')");
			}			
			if (solicitud.getFechaRecepSwift()!=null) {
				sqlupdate.append(",FECHA_RES_SWIFT = TO_DATE('" + solicitud.getFechaRecepSwift()+"','DD/MM/YYYY')");
			}			
			if (solicitud.getTituloAdjudicado()!=null && solicitud.getTituloAdjudicado().length()>0) {
				sqlupdate.append(",TITULO_ADJUDICADO = '" + solicitud.getTituloAdjudicado()+"'");
			}			
			if (solicitud.getPrecioAdjudicacion()>0) {
				sqlupdate.append(",PRECIO_ADJUDICACION = " + solicitud.getPrecioAdjudicacion());
			}			
			if (solicitud.getValorEfectivoUSD()!=null) {
				sqlupdate.append(",VALOR_EFECTIVO_USD = " + solicitud.getValorEfectivoUSD());
			}			
			if (solicitud.getValorNominalUSD()!=null) {
				sqlupdate.append(",VALOR_NOMINAL_USD = " + solicitud.getValorNominalUSD());
			}			
			if (solicitud.getValorEfectivo()!=null) {
				sqlupdate.append(",VALOR_EFECTIVO = " + solicitud.getValorEfectivo());
			}			
			if (solicitud.getValorNominal()!=null) {
				sqlupdate.append(",VALOR_NOMINAL = " + solicitud.getValorNominal());
			}	
			if (solicitud.getContadorEnviosSwift()>0) {
				sqlupdate.append(",CONTADOR_ENV_SWIFT = " + solicitud.getContadorEnviosSwift());
			}			
			if (solicitud.getIdCanal()>0) {
				sqlupdate.append(",ID_CANAL = " + solicitud.getIdCanal());
			}
			
			if(solicitud.getIdOrden()>0){
				sqlupdate.append(" WHERE ID_ORDEN= "+solicitud.getIdOrden());
			} else if(solicitud.getIdOrdenInfi()>0){
				sqlupdate.append(" WHERE NUMERO_ORDEN_INFI= " + solicitud.getIdOrdenInfi());
			}
		
			if (estatusAnterior!=null && estatusAnterior.length()>0) {
				sqlupdate.append(" AND ESTATUS= '" + estatusAnterior+"'" );
			}			
				logger.info("ACTUALIZACION DE SOLICITUDES CLAVENET ----> " + sqlupdate.toString());
			
			//System.out.println("ACTUALIZACION DE SOLICITUDES CLAVENET ----> " + sqlupdate.toString());
			return sqlupdate.toString();				
		}				
		
		/**
		 * Actualiza la orden registrada desde Clavenet en la BD OPICS
		 * @param idOrden id de orden
		 * @throws Exception en caso de error
		 */
		@SuppressWarnings("unchecked")
		public void actualizarSolicitudesSitmePorLotes(String estatusAnterior, Archivo archivo, ArrayList consultas)throws Exception{
			StringBuffer sqlupdate = new StringBuffer();		
			long contadorOrdenes=0;
			long totalOrdenes=0;
			StringBuffer idsOrdenes=new StringBuffer();
			SolicitudClavenet solicitud = new SolicitudClavenet();
			logger.info("actualizarSolicitudClavenet ");		
			solicitud = new SolicitudClavenet();
			solicitud.setEstatus(StatusOrden.EN_TRAMITE); 			
			solicitud.setFechaActualTramite(true); 	
			
			if (!archivo.isDetalleEmpty()){
				ArrayList detalles = archivo.getDetalle();
				//Se recorren las ordenes y se almacenan en la base de datos			
				for (Iterator iter = detalles.iterator(); iter.hasNext(); ) {
			
					if(ConstantesGenerales.MAX_VALUES_IN_CLAUSE_ORACLE<=contadorOrdenes||totalOrdenes==detalles.size()){
						sqlupdate.append("UPDATE SOLICITUDES_SITME SET ");
						sqlupdate.append(" FECHA_MODIFICACION= SYSDATE");
						if (solicitud.getEstatus() != null && solicitud.getEstatus().length() > 0) {
							sqlupdate.append(",ESTATUS = '" + solicitud.getEstatus() + "'");
						}
						if (solicitud.isFechaActualTramite()) {
							sqlupdate.append(",FECHA_TRAMITE = SYSDATE");
						}
						sqlupdate.append(" WHERE NUMERO_ORDEN_INFI IN (").append(idsOrdenes.substring(0,idsOrdenes.length()-1)).append(")");
						if (estatusAnterior != null && estatusAnterior.length() > 0) {
							sqlupdate.append(" AND ESTATUS= '" + estatusAnterior + "'");
						}
						consultas.add(sqlupdate);
						sqlupdate= new StringBuffer();
						idsOrdenes = new StringBuffer();
						contadorOrdenes=0;
					}else{
						Detalle detalle = (Detalle) iter.next();
						contadorOrdenes++;
						totalOrdenes++;
						idsOrdenes.append(detalle.getIdOrden()).append(",");		
					}
				}
				if(contadorOrdenes>0){
					sqlupdate.append("UPDATE SOLICITUDES_SITME SET ");
					sqlupdate.append(" FECHA_MODIFICACION= SYSDATE");
					if (solicitud.getEstatus() != null && solicitud.getEstatus().length() > 0) {
						sqlupdate.append(",ESTATUS = '" + solicitud.getEstatus() + "'");
					}
					if (solicitud.isFechaActualTramite()) {
						sqlupdate.append(",FECHA_TRAMITE = SYSDATE");
					}
					sqlupdate.append(" WHERE NUMERO_ORDEN_INFI IN (").append(idsOrdenes.substring(0,idsOrdenes.length()-1)).append(")");
					if (estatusAnterior != null && estatusAnterior.length() > 0) {
						sqlupdate.append(" AND ESTATUS= '" + estatusAnterior + "'");
					}
					consultas.add(sqlupdate.toString());
				}
				
			}else
			{
				logger.info("No existen ordenes a actualizar");			
			}
			logger.info("ACTUALIZACION DE SOLICITUDES CLAVENET ----> " + sqlupdate.toString());			
			System.out.println("ACTUALIZACION DE SOLICITUDES CLAVENET ----> " + sqlupdate.toString());						
		}				

		
		public int mostrarSolicitudesClaveNet(SolicitudClavenet solicitud)throws Exception {
			
			StringBuffer sql=new StringBuffer();
			  sql.append("SELECT NUM_BLOQUEO,FECHA_SALIDA_VIAJE,FECHA_RETORNO_VIAJE,COD_PAIS_DESTINO,DESC_PAIS_DESTINO,");
			  sql.append("COD_PAIS_ORIGEN,DESC_PAIS_ORIGEN,COD_CIUDAD_ORIGEN,DESC_CIUDAD_ORIGEN,COD_ESTADO_ORIGEN, ");
			  sql.append("DESC_ESTADO_ORIGEN,NUM_PASAPORTE,ID_ORDEN,CTA_NUMERO,NOMBRE_BENEFICIARIO,CTA_BANCO,CTA_DIRECCION_C,");
			  sql.append("CTA_BIC_SWIFT,CTA_TELEFONO_3,CTA_ABA,CED_RIF_CLIENTE,NOMBRE_CLIENTE, ");
			  sql.append("CTA_INT_BIC_SWIFT, CTA_INT_ABA, CTA_INT_BANCO, CTA_INT_DIRECCION, CTA_INT_NUMERO, CTA_INT_TELEFONO,CTA_ABONO,PORC_COMISION,MONTO_COMISION");
			  sql.append(" FROM SOLICITUDES_SITME "); 			
			if(solicitud.getIdOrden()>0){
				sql.append(" WHERE ID_ORDEN= "+solicitud.getIdOrden());
			} else if(solicitud.getIdOrdenInfi()>0){
				sql.append(" WHERE NUMERO_ORDEN_INFI= " + solicitud.getIdOrdenInfi());
			}
		
			//System.out.println("BUSCAR NUM BLOQUEO SOLICITUDES_SITME " + sql.toString());
			dataSet = db.get(dataSource, sql.toString());
			return dataSet.count();
		}
		
		@SuppressWarnings("static-access")
		public String modificarOrdenesClaveNet(Orden orden, String... status) throws Exception {
		
		StringBuffer sql = new StringBuffer();

		double monto_total_operacion = 0.0;
		monto_total_operacion = orden.getMonto() + orden.getMontoComisionOrden().doubleValue() + orden.getMontoInteresCaidos();

		BigDecimal montoTotal=new BigDecimal(0);
		montoTotal=montoTotal.valueOf(monto_total_operacion).setScale(2,BigDecimal.ROUND_HALF_EVEN);
		
		
		
		sql.append("UPDATE INFI_TB_204_ORDENES ORD SET ");

		sql.append("ORD.ORDENE_ADJ_MONTO= ");
		sql.append((long) orden.getMontoAdjudicado());

		if (orden.getStatus() != null) {
			//System.out.println("FLAG ENTRA MODIFICAR STATUS");
			sql.append(", ");
			sql.append("ORD.ORDSTA_ID= ");
			sql.append("'");
			sql.append(orden.getStatus());
			sql.append("' ");
		}

		if (montoTotal.doubleValue() > 0) {

			sql.append(", ");
			sql.append("ORD.ORDENE_PED_TOTAL= ");
			sql.append(montoTotal);
		}

		if (orden.getIdUnidadInversion() != 0) {
			sql.append(", ");
			sql.append("ORD.UNIINV_ID= ");
			sql.append(orden.getIdUnidadInversion());
		}

		if(orden.getContraparte()!=null){
			sql.append(", ");
			sql.append("ORD.CONTRAPARTE='");
			sql.append(orden.getContraparte()).append("'");
		}
			
		if (orden.getMontoInteresCaidos() != 0) {
			sql.append(", ");
			sql.append("ORD.ORDENE_PED_INT_CAIDOS= ");
			sql.append(orden.getMontoInteresCaidos());
		}

		if ((orden.getMontoComisionOrden().doubleValue() != 0)||(orden.getMontoComisionOrden().doubleValue() == 0 && orden.getComisionCero())) {
			sql.append(", ");
			sql.append("ORD.ORDENE_PED_COMISIONES= ");
			sql.append(orden.getMontoComisionOrden());
		}

		if (orden.getIdEmpresa() != null && (!orden.getIdEmpresa().equals(""))) {
			sql.append(", ");
			sql.append("ORD.EMPRES_ID= ");
			sql.append(orden.getIdEmpresa());
		}

		if (orden.getIdBloter() != null) {
			sql.append(", ");
			sql.append("ORD.BLOTER_ID= ");
			sql.append(orden.getIdBloter());
		}

		if (orden.getPrecioCompra() != 0) {
			sql.append(", ");
			sql.append("ORD.ORDENE_PED_PRECIO= ");
			sql.append(orden.getPrecioCompra());
		}

		if (orden.getTasaPool().doubleValue() != 0) {
			sql.append(", ");
			sql.append("ORD.ORDENE_TASA_POOL= ");
			sql.append(orden.getTasaPool().doubleValue());
		}

		if (orden.getFechaValorString() != null) {

			sql.append(", ");
			sql.append("ORD.ORDENE_PED_FE_VALOR= ");
			sql.append("TO_DATE('");
			sql.append(orden.getFechaValorString());
			sql.append("','DD-MM-YYYY') ");
			sql.append(", ORD.ORDENE_FE_ULT_ACT=");
			sql.append("TO_DATE('");
			sql.append(orden.getFechaValorString());
			sql.append("','DD-MM-YYYY') ");

		}
		
		// Configurar ORDENE_FECHA_ADJUDICACION
		if(orden.getFechaAdjudicacion()!=null){

			sql.append(",");
			sql.append("ORD.ORDENE_FECHA_ADJUDICACION=TO_DATE('");
			sql.append(Utilitario.DateToString(orden.getFechaAdjudicacion(),"dd-MM-yyyy"));
			sql.append("','DD-MM-YYYY')");
		}

		if (orden.getTasaCambio() != 0) {
			sql.append(", ");
			sql.append("ORD.ORDENE_TASA_CAMBIO= ");
			sql.append(orden.getTasaCambio());
			sql.append(" ");
		}
		
		if(orden.getIdEjecucion()!=0){
			sql.append(", ");
			sql.append("ORD.EJECUCION_ID=");
			sql.append(orden.getIdEjecucion());
			sql.append(" ");
		}
		
		if(orden.getActividadEconomicaId()!=null && (!orden.getActividadEconomicaId().equals(""))){		
			sql.append(", ");
			sql.append("ORD.CODIGO_ID=");
			sql.append(Long.parseLong(orden.getActividadEconomicaId()));
			sql.append(" ");
		}
		
		if(orden.getSectorProductivoId()!=null && (!orden.getSectorProductivoId().equals(""))){
			sql.append(", ");
			sql.append("ORD.SECTOR_ID=");
			sql.append(Long.parseLong(orden.getSectorProductivoId()));
			sql.append(" ");
		}
		
		sql.append(" WHERE ORD.ORDENE_ID =");
		sql.append(orden.getIdOrden());

		if (status.length > 0) {

			sql.append(" AND ORD.ORDSTA_ID IN (");
			int count = status.length;

			for (String element : status) {
				sql.append("'").append(element).append("' ");
				--count;
				if (count != 0) {
					sql.append(",");
				}

			}
		}
		sql.append(")");
		//System.out.println("METODO modificarOrdenesClaveNet Query: " + sql.toString());



		return sql.toString();
	}
		

		/**
		 * Valida si la orden fue adjudicada y registrada fue tomada por otro canal (CLAVENET), retorna el id de ticket asociado a la orden original en CLAVE
		 * @param idOrden id de orden
		 * @throws Exception en caso de error
		 */
		public String validarOrdenClaveAdjudicada(long idOrden) throws Exception{
			//String sql = "SELECT DTAEXT_VALOR FROM INFI_TB_204_ORDENES O, INFI_TB_212_ORDENES_DATAEXT D WHERE D.DTAEXT_ID='NRO_TICKET' AND O.ORDENE_ADJ_MONTO>0 AND O.ORDENE_ID=D.ORDENE_ID AND D.ORDENE_ID="+idOrden;
			String sql = "SELECT ID_TICKET_CLAVENET FROM INFI_TB_204_ORDENES ORD, INFI_TB_220_ORDENE_DETALLES OD WHERE ORD.ORDENE_ID=OD.ORDENE_ID AND OD.ID_TICKET_CLAVENET IS NOT NULL AND ORD.ORDENE_ADJ_MONTO>0 AND ORD.ORDENE_ID="+idOrden;
			dataSet = db.get(dataSource, sql);
			if(dataSet.count()>0){
				//La orden fue tomada desde CLAVENET
				dataSet.first();
				dataSet.next();
				return dataSet.getValue("ID_TICKET_CLAVENET");
			}
			return null; //La orden fue tomada desde INFI
		}
		
		
		/**Lista las ordenes dado un id de ejecucion para el informe de Adjudicacion
		 * @throws Exception en caso de error
		 */
		public String listarOrdenesPorIdEjecucion(String idEjecucion) throws Exception{
			StringBuffer sql = new StringBuffer();
			//System.out.println("METODO listarOrdenesPorIdEjecucion " );
			sql.append("SELECT o.ordene_id, o.ordsta_id ordsta_nombre,o.ordene_ped_precio,o.ordene_ped_monto,");
			sql.append("o.ordene_ped_fe_orden,o.ordene_adj_monto,o.ordene_fecha_adjudicacion, c.client_nombre,u.undinv_nombre,");
			sql.append("ot.TITULO_MTO_INT_CAIDOS,ot.TITULO_PCT_RECOMPRA,o.ORDENE_PED_FE_VALOR, o.ORDENE_TASA_POOL,o.ORDENE_TASA_CAMBIO,");
			sql.append("(SELECT DEXT.DTAEXT_VALOR FROM INFI_TB_212_ORDENES_DATAEXT DEXT WHERE DEXT.ORDENE_ID = o.ordene_id AND DEXT.DTAEXT_ID ='");
			sql.append(DataExtendida.NRO_TICKET).append("') AS NRO_TICKET ");
			sql.append("FROM INFI_TB_204_ORDENES o,INFI_TB_201_CTES c,INFI_TB_106_UNIDAD_INVERSION u,INFI_TB_206_ORDENES_TITULOS ot ");
			sql.append("WHERE o.client_id = c.client_id AND o.uniinv_id = u.undinv_id AND o.transa_id =");
			sql.append("'"+com.bancovenezuela.comun.logic.interfaces.ConstantesGenerales.TRANSACCION_TOMA_ORDEN).append("' ");
			sql.append("AND o.ordsta_id NOT IN ('").append(StatusOrden.CANCELADA).append("','").append(StatusOrden.PENDIENTE).append("')");
			sql.append("AND o.EJECUCION_ID="+Long.parseLong(idEjecucion)).append(" ");
			sql.append("AND ot.ORDENE_ID(+)= O.ORDENE_ID").append(" ");
			sql.append("order by ordsta_nombre,o.ordene_id"); 
			
			//System.out.println(" QUERY: " + sql.toString());
		
			System.out.println("listarOrdenesPorIdEjecucion: "+sql.toString());
			return sql.toString();
		}	
		
		/**Lista las ordenes tomadas por clavenet filtradas fecha para el informe de Adjudicacion 
		 * @throws Exception en caso de error
		 */
		public String listarOrdenesClavenetPorFecha(String fechaDesde, String fechaHasta) throws Exception{
			StringBuffer sql = new StringBuffer();
			//System.out.println("METODO listarOrdenesClavenetPorFecha ");
			sql.append("select o.ordene_id,o.ordsta_id ordsta_nombre,o.ordene_ped_precio,o.ordene_ped_monto,o.ordene_ped_fe_orden,o.ordene_adj_monto,o.ordene_fecha_adjudicacion,o.ORDENE_PED_FE_VALOR,c.client_nombre,u.undinv_nombre," +
					   " ot.TITULO_MTO_INT_CAIDOS,ot.TITULO_PCT_RECOMPRA, (SELECT DEXT.DTAEXT_VALOR FROM INFI_TB_212_ORDENES_DATAEXT DEXT WHERE DEXT.ORDENE_ID = o.ordene_id AND DEXT.DTAEXT_ID ='NRO_TICKET') AS NRO_TICKET " +
					   " from INFI_TB_204_ORDENES o, INFI_TB_201_CTES c, INFI_TB_106_UNIDAD_INVERSION u, INFI_TB_206_ORDENES_TITULOS ot " +
					   " where o.client_id=c.client_id and o.uniinv_id=u.undinv_id and o.transa_id='TOMA_ORDEN' "+
					   " and o.ordsta_id not in('"+StatusOrden.CANCELADA+"','"+StatusOrden.PENDIENTE+"') ");
			sql.append(" and o.ORDENE_FECHA_ADJUDICACION>= TO_DATE('"+fechaDesde+"','DD/MM/YYYY') AND o.ORDENE_FECHA_ADJUDICACION<= TO_DATE('"+fechaHasta+"','DD/MM/YYYY')");
			sql.append(" and  o.ordene_id in (select ORDENE_ID from INFI_TB_212_ORDENES_DATAEXT dex where  dex.DTAEXT_ID='"+DataExtendida.NRO_TICKET+"') ");
			sql.append(" and ot.ordene_id(+)=o.ordene_id");
			sql.append(" order by ordsta_nombre,o.ordene_id"); 
			//System.out.println("listarOrdenesClavenetPorFecha: "+sql.toString());
			
			return sql.toString();
		}	
		
		/** 
		 * Consulta los datos adicionales de la Solicitud Sitme realizada por clavenet Personal
		 * @throws Exception 
		 */
		public void consultarDetallesOrdeneClavenet(long ordeneId, long idTicketClave) throws Exception{
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT ORDEN_DETALLE_ID,ORDENE_ID,FECHA_VIAJE,FECHA_RETORNO,COD_PAIS_DESTINO,DESC_PAIS_DESTINO,");
			sql.append("COD_PAIS_ORIGEN,DESC_PAIS_ORIGEN,COD_CIUDAD_ORIGEN,DESC_CIUDAD_ORIGEN,COD_ESTADO_ORIGEN,");
			sql.append("DESC_ESTADO_ORIGEN,NUMERO_PASAPORTE,ID_TICKET_CLAVENET,ULT_FECHA_ACTUALIZACION");
			sql.append(" FROM INFI_TB_220_ORDENE_DETALLES WHERE ");
			if (ordeneId>0) {
				sql.append("ORDENE_ID=").append(ordeneId);
			}			
			if (idTicketClave>0) {
				if (ordeneId>0) sql.append(" AND");
				sql.append("ID_TICKET_CLAVENET=").append(idTicketClave);
			}	
			//System.out.println("consultarDetalleOrdenesClavenet: "+sql.toString() );
			dataSet = db.get(dataSource, sql.toString());
		}
		public String insertarOrdenDetalle(OrdenDetalle ordenDetalle)throws Exception{
		
		
			
			StringBuffer sqlCampos=new StringBuffer();
			StringBuffer sqlValores=new StringBuffer();
			StringBuffer sql=new StringBuffer();
			
			sqlCampos.append("INSERT INTO INFI_TB_220_ORDENE_DETALLES (ORDEN_DETALLE_ID");
			sqlValores.append(" VALUES (INFI_TB_220.NEXTVAL");
			
					if(ordenDetalle.getIdOrden()>0){
						sqlCampos.append(",ORDENE_ID");
						sqlValores.append(",").append(ordenDetalle.getIdOrden());
					}
					
					if(ordenDetalle.getFechaSalidaViaje()!=null && (!ordenDetalle.getFechaSalidaViaje().equals(""))){
						sqlCampos.append(",FECHA_VIAJE");
						sqlValores.append(",TO_DATE('").append(ordenDetalle.getFechaSalidaViaje()).append("','YYYY-MM-DD')");
						
					}
					
					if(ordenDetalle.getFechaRetornoViaje()!=null && (!ordenDetalle.getFechaRetornoViaje().equals(""))){
						sqlCampos.append(",FECHA_RETORNO");
						sqlValores.append(",TO_DATE('").append(ordenDetalle.getFechaRetornoViaje()).append("','YYYY-MM-DD')");
					}
																							
					
					if(ordenDetalle.getNumeroPasaporte()!=null && (!ordenDetalle.getNumeroPasaporte().equals(""))){
						sqlCampos.append(",NUMERO_PASAPORTE");
						sqlValores.append(",'"+ordenDetalle.getNumeroPasaporte()+"'");
					}
					
					if(ordenDetalle.getNumeroTicketClavenet()>0){
						sqlCampos.append(",ID_TICKET_CLAVENET");
						sqlValores.append(",").append(ordenDetalle.getNumeroTicketClavenet());
					}
					
					if(ordenDetalle.getObservaciones()!=null){
						sqlCampos.append(",OBSERVACION");
						sqlValores.append(",'").append(ordenDetalle.getObservaciones()).append("'");
					}
					
					sqlCampos.append(",ULT_FECHA_ACTUALIZACION");
					sqlValores.append(",SYSDATE");
					
					sqlCampos.append(")");
					sqlValores.append(")");
					
					sql.append(sqlCampos.toString());
					sql.append(sqlValores.toString());
					
			return sql.toString();
		}
		
		//NM29643
		public void insertOrdenDetalle(OrdenDetalle ordenDetalle)throws Exception{
		
			StringBuffer sqlCampos=new StringBuffer();
			StringBuffer sqlValores=new StringBuffer();
			StringBuffer sql=new StringBuffer();
			
			sqlCampos.append("INSERT INTO INFI_TB_220_ORDENE_DETALLES (ORDEN_DETALLE_ID");
			sqlValores.append(" VALUES (INFI_TB_220.NEXTVAL");
			
					if(ordenDetalle.getIdOrden()>0){
						sqlCampos.append(",ORDENE_ID");
						sqlValores.append(",").append(ordenDetalle.getIdOrden());
					}
					
					if(ordenDetalle.getFechaSalidaViaje()!=null && (!ordenDetalle.getFechaSalidaViaje().equals(""))){
						sqlCampos.append(",FECHA_VIAJE");
						sqlValores.append(",TO_DATE('").append(ordenDetalle.getFechaSalidaViaje()).append("', '").append(ConstantesGenerales.FORMATO_FECHA3).append("')");
						
					}
					
					if(ordenDetalle.getFechaRetornoViaje()!=null && (!ordenDetalle.getFechaRetornoViaje().equals(""))){
						sqlCampos.append(",FECHA_RETORNO");
						sqlValores.append(",TO_DATE('").append(ordenDetalle.getFechaRetornoViaje()).append("', '").append(ConstantesGenerales.FORMATO_FECHA3).append("')");
					}
																							
					
					if(ordenDetalle.getNumeroPasaporte()!=null && (!ordenDetalle.getNumeroPasaporte().equals(""))){
						sqlCampos.append(",NUMERO_PASAPORTE");
						sqlValores.append(",'"+ordenDetalle.getNumeroPasaporte()+"'");
					}
					
					if(ordenDetalle.getNumeroTicketClavenet()>0){
						sqlCampos.append(",ID_TICKET_CLAVENET");
						sqlValores.append(",").append(ordenDetalle.getNumeroTicketClavenet());
					}
					
					if(ordenDetalle.getObservaciones()!=null){
						sqlCampos.append(",OBSERVACION");
						sqlValores.append(",'").append(ordenDetalle.getObservaciones()).append("'");
					}
					
					sqlCampos.append(",ULT_FECHA_ACTUALIZACION");
					sqlValores.append(",SYSDATE");
					
					sqlCampos.append(")");
					sqlValores.append(")");
					
					sql.append(sqlCampos.toString());
					sql.append(sqlValores.toString());
					
					//System.out.println("SQL DETALLES-------------\n"+sql.toString());
					db.exec(dataSource, sql.toString());
		}
		
		
		public void detalleOrdenesCompraClavenet(String feDesde,String feHasta,String IDuinv, String idCliente,String transaccion)throws Exception {
			
			
			StringBuffer sqlPactoRecompra=new StringBuffer();
			StringBuffer sqlPactoVentaTitulo=new StringBuffer();
			StringBuffer sql=null;
									
				sqlPactoRecompra.append("SELECT ORD.ORDENE_ID_RELACION AS ORDENE_ID,TO_CHAR(ORD.ORDENE_ID) AS ID_RECOMPRA,CC.CTECTA_NOMBRE AS CLIENT_NOMBRE, ");
				sqlPactoRecompra.append("CTES.CLIENT_CEDRIF AS CLIENT_CEDRIF,ORD.ORDENE_PED_FE_VALOR AS FECHA_VALOR,ORD.TRANSA_ID AS TRANSA_ID,ORD.ORDSTA_ID AS ORDSTA_ID ");
				sqlPactoRecompra.append("FROM INFI_TB_204_ORDENES ORD,INFI_TB_202_CTES_CUENTAS CC,INFI_TB_217_CTES_CUENTAS_ORD CCO,INFI_TB_201_CTES CTES ");
				sqlPactoRecompra.append("WHERE ORD.ORDENE_ID_RELACION=CCO.ORDENE_ID ");
				sqlPactoRecompra.append("AND CCO.CTES_CUENTAS_ID=CC.CTES_CUENTAS_ID AND ORD.CLIENT_ID=CTES.CLIENT_ID ");				
				sqlPactoRecompra.append("AND  ORD.ORDENE_PED_FE_VALOR>=TO_DATE ('"+feDesde+"','DD/MM/YYYY') AND ORD.ORDENE_PED_FE_VALOR <= TO_DATE ('"+feHasta+"', 'DD/MM/YYYY') ");
				sqlPactoRecompra.append("AND  CC.CTECTA_USO='").append(UsoCuentas.RECOMPRA).append("' ");				
				////System.out.println("QUERY PACTO_RECOMPRA: " + sqlPactoRecompra.toString());
				
				sqlPactoVentaTitulo.append("SELECT ORD.ORDENE_ID AS ORDENE_ID, '0' AS ID_RECOMPRA,CC.CTECTA_NOMBRE AS CLIENT_NOMBRE,CTES.CLIENT_CEDRIF AS CLIENT_CEDRIF,");
				sqlPactoVentaTitulo.append("ORD.ORDENE_FE_ULT_ACT AS FECHA_VALOR,ORD.TRANSA_ID AS TRANSA_ID,ORD.ORDSTA_ID AS ORDSTA_ID ");
				sqlPactoVentaTitulo.append("FROM INFI_TB_204_ORDENES ORD,INFI_TB_201_CTES CTES,INFI_TB_202_CTES_CUENTAS CC,INFI_TB_217_CTES_CUENTAS_ORD CCO ");
				sqlPactoVentaTitulo.append("WHERE     CCO.ORDENE_ID = ORD.ORDENE_ID AND CCO.CTES_CUENTAS_ID = CC.CTES_CUENTAS_ID ");
				sqlPactoVentaTitulo.append("AND CC.CLIENT_ID = CTES.CLIENT_ID AND ORD.CLIENT_ID = CTES.CLIENT_ID ");
				sqlPactoVentaTitulo.append("AND ORD.ORDENE_PED_FE_VALOR >= TO_DATE ('").append(feDesde).append("', 'DD/MM/YYYY') ");
				sqlPactoVentaTitulo.append("AND ORD.ORDENE_PED_FE_VALOR <= TO_DATE ('").append(feHasta).append("', 'DD/MM/YYYY') ");
				sqlPactoVentaTitulo.append("AND  CC.CTECTA_USO IN ('").append(UsoCuentas.RECOMPRA).append("','").append(UsoCuentas.PAGO_DE_CUPONES).append("') ");
				////System.out.println("QUERY VENTA_TITULOS: " + sqlPactoVentaTitulo.toString());
				
				if(transaccion==null){
					
					sqlPactoRecompra.append(" AND ORD.TIPO_PRODUCTO_ID='").append(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME).append("' ");					
					sqlPactoVentaTitulo.append(" AND ORD.TIPO_PRODUCTO_ID='").append(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME).append("' ");
					if(IDuinv!=null && (!IDuinv.equals(""))){			
						sqlPactoRecompra.append(" AND ORD.UNIINV_ID = ").append(Long.parseLong(IDuinv)).append(" ");
						sqlPactoVentaTitulo.append(" AND ORD.UNIINV_ID = ").append(Long.parseLong(IDuinv)).append(" ");
					}
					
					if(idCliente!=null && (!idCliente.equals(""))){			
						sqlPactoRecompra.append(" AND ORD.CLIENT_ID = ").append(Long.parseLong(idCliente));
						sqlPactoVentaTitulo.append(" AND ORD.CLIENT_ID = ").append(Long.parseLong(idCliente));
					}		
					
					sqlPactoRecompra.append(" AND ORD.TRANSA_ID ='").append(TRANSACCION_PACTO_RECOMPRA).append("' ");
					sqlPactoVentaTitulo.append(" AND ORD.TRANSA_ID ='").append(TRANSACCION_VENTA_TITULOS).append("' ");
					//System.out.println("QUERY PACTO_RECOMPRA: " + sqlPactoRecompra.toString());
					//System.out.println("********************************************************");
					//System.out.println("QUERY VENTA_TITULOS: " + sqlPactoVentaTitulo.toString());
				}
				
				if((transaccion!=null)&& transaccion.equalsIgnoreCase(TRANSACCION_PACTO_RECOMPRA)){
					sql=sqlPactoRecompra;
					//System.out.println("*************** SENTENCIA PACTO_RECOMPRA ***************");
					//System.out.println(sql.toString());
					//System.out.println("*************** SENTENCIA PACTO_RECOMPRA ***************");
				} else if((transaccion!=null)&& transaccion.equalsIgnoreCase(TRANSACCION_VENTA_TITULOS)){					
					sql=sqlPactoVentaTitulo;
					//System.out.println("*************** SENTENCIA VENTA_TITULOS ***************");
					//System.out.println(sql.toString());
					//System.out.println("*************** SENTENCIA VENTA_TITULOS ***************");
				} else {
					//System.out.println("*************** SENTENCIA UNION ***************");
					sql=new StringBuffer();
					sql.append(sqlPactoRecompra.toString()).append(" UNION ").append(sqlPactoVentaTitulo.toString());
					//System.out.println(sql.toString());
					//System.out.println("*************** SENTENCIA UNION ***************");
				}
								
				if(transaccion!=null && (!transaccion.equals(""))){
								
					sql.append(" AND ORD.TIPO_PRODUCTO_ID='").append(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME).append("' ");
							
					if(IDuinv!=null && (!IDuinv.equals(""))){									
						sql.append("AND ORD.UNIINV_ID = ").append(Long.parseLong(IDuinv)).append(" ");			
					}										
					sql.append("AND ORD.TRANSA_ID ='").append(transaccion).append("' ");
									
					if(idCliente!=null && (!idCliente.equals(""))){						
						sql.append("AND ORD.CLIENT_ID = ").append(Long.parseLong(idCliente));			
					}									
				}
				//System.out.println("Sentencia SQL: " + sql.toString());			
				dataSet = db.get(dataSource, sql.toString());		
		}
		
		public void listarOrdenPorId(long ordeneID,String[] listIdOrdenes) throws Exception{
			
			StringBuffer sql=new StringBuffer();
			
				
			//sql.append("SELECT ORD.ORDENE_ID AS ORDENE_ID, TO_CHAR(ORD.ORDENE_PED_FE_VALOR,'DD/MM/YYYY') AS fecha_valor,ORD.CLIENT_ID AS CLIENT_ID,ORD.UNIINV_ID AS UNIINV_ID,ORD.ORDENE_ADJ_MONTO AS ORDENE_ADJ_MONTO ");
			sql.append("SELECT ORD.ORDENE_ID AS ORDENE_ID, TO_CHAR(ORD.ORDENE_PED_FE_VALOR,'DD/MM/YYYY') AS fecha_valor,ORD.CLIENT_ID AS CLIENT_ID,ORD.UNIINV_ID AS UNIINV_ID,ORD.ORDENE_ADJ_MONTO AS ORDENE_ADJ_MONTO, ORD.TIPO_PRODUCTO_ID ");
			sql.append("FROM INFI_TB_204_ORDENES ORD WHERE ");
			
			if(listIdOrdenes==null){					
				sql.append("ORD.ORDENE_ID=").append(ordeneID);		
			} else {
				sql.append("ORD.ORDENE_ID IN (");							
				for(int i=0;i<listIdOrdenes.length;++i ){
					if(i>0){
						sql.append(",");
					}
					sql.append(listIdOrdenes[i]);
				}
				
				sql.append(")");
			}	
			//System.out.println(" query: " + sql.toString());
			dataSet = db.get(dataSource, sql.toString());
		}
		/*
		 * Consulta las ordenes registradas desde Clavenet en la tabla SOLICITUDES_SITME
		 * Victor Goncalves
		 */		
		public String listarSolicitudesSITME(String fecha) throws Exception{		
			StringBuffer sql = new StringBuffer();				
			sql.append("SELECT A.ID_ORDEN, A.NOMBRE_CLIENTE, A.CED_RIF_CLIENTE, A.CED_RIF_CONTRAPARTE, A.MONTO_SOLICITADO,");
			sql.append(" A.MONTO_COMISION, A.PORC_COMISION, A.ID_CONCEPTO, A.FECHA_REGISTRO, A.VALOR_NOMINAL, A.CUENTA_BSF_O,A.CTA_DIRECCION_C,A.CTA_TELEFONO,A.EMAIL_CLIENTE,A.CTA_ABONO_MONEDA,A.CTA_ABONO ");
			sql.append(" FROM SOLICITUDES_SITME A"); 
			sql.append(" WHERE TO_CHAR(A.FECHA_REGISTRO, 'YYYY-MM-DD') = '").append(fecha).append("'");		
			sql.append(" AND A.ESTATUS = '");
			sql.append(StatusOrden.REGISTRADA);
			sql.append("' ");
			sql.append(" ORDER BY A.HORA_REGISTRO ASC");			
			dataSet = db.get(dataSource, sql.toString());
			return sql.toString();		
		}
		
//		NM25287 SICAD II. Se solicitó NO actualizar el estatus en SOLICITUDES SITME en la exportacion de ordenes. 27/03/2014
//		NM25287 CONTINGENCIA SIMADI 11/02/2015. Filtro por oferta/demanda y cambio de validacion de orden registrada por FECHA_TRAMITE
		public void listarSolicitudesSITME(String estatus,String fechaRegistro,boolean registradaEnInfi, String ultimoReg, String idProducto,long... uniId) throws Exception{		
			StringBuffer sql = new StringBuffer();				
			sql.append("SELECT A.ID_ORDEN, A.NOMBRE_CLIENTE, A.CED_RIF_CLIENTE, A.CED_RIF_CONTRAPARTE, A.MONTO_SOLICITADO, A.FECHA_MODIFICACION,A.NUMERO_ORDEN_INFI,NUM_BLOQUEO,TASA_CAMBIO,A.ORIGEN_FONDOS,A.DESTINO_FONDOS,");
			sql.append(" A.MONTO_COMISION, A.PORC_COMISION, A.ID_CONCEPTO, A.FECHA_REGISTRO,A.VALOR_NOMINAL, A.CUENTA_BSF_O,A.CTA_DIRECCION_C,A.CTA_TELEFONO,A.EMAIL_CLIENTE,A.CTA_ABONO_MONEDA,A.CTA_ABONO,A.CTA_NUMERO,A.HORA_REGISTRO");
			//sql.append(" , DECODE(NUMERO_ORDEN_INFI,null,'SI','NO') as REGISTRAR_ORDEN_INFI,");
			sql.append(" ,CASE WHEN NUMERO_ORDEN_INFI IS NULL AND ID_PRODUCTO=6 THEN 'SI' ELSE 'NO' END as REGISTRAR_ORDEN_INFI");
			sql.append(" FROM SOLICITUDES_SITME A");
			sql.append(" WHERE A.ESTATUS = '").append(estatus).append("' ");
			if (fechaRegistro!=null) {
				sql.append(" AND Trunc(A.FECHA_REGISTRO) = TO_DATE('").append(fechaRegistro).append("', 'DD-MM-YYYY')");
			}
			if(registradaEnInfi){
				sql.append(" AND A.FECHA_TRAMITE IS NOT NULL");
			}else{
				sql.append(" AND A.FECHA_TRAMITE IS NULL"); 
			}
			if (ultimoReg!=null&&ultimoReg!="") {
				sql.append(" AND ID_ORDEN <= ").append(ultimoReg);
			}			
			if (idProducto!=null&&idProducto!="") {
				sql.append(" AND ID_PRODUCTO = ").append(idProducto);
			}
			if(uniId.length>0 && uniId[0]!=0){
				 sql.append(" AND UNDINV_ID = ").append(uniId[0]);
			}
			sql.append(" ORDER BY A.HORA_REGISTRO ASC");			
			dataSet = db.get(dataSource, sql.toString());
			System.out.println("listarSolicitudesSITME: "+sql.toString());
		}
		
		/**
		 * Listar data extendida por un valor especifico
		 * Se usa para verificar si en data_extendida ya se encuentra registrado un id dado (ticket clavenet)
		 * @return
		 * @throws Exception
		 */
		public void listarDataExtPorIdTicket(String idOrden)throws Exception{	
			String sql="SELECT ORDENE_ID,DTAEXT_ID,DTAEXT_VALOR FROM INFI_TB_212_ORDENES_DATAEXT A WHERE A.DTAEXT_ID='"+DataExtendida.NRO_TICKET+"' AND A.DTAEXT_VALOR = '"+idOrden+"'";
			dataSet=db.get(dataSource,sql);
		}
		public String listarOrdenesClavenetPorFechaTomaOrden(String fechaDesde, String fechaHasta,long unidadInv) throws Exception{
		
			
			StringBuffer sql = new StringBuffer();
			//System.out.println("METODO listarOrdenesClavenetPorFechaTomaOrden ");
			sql.append("select o.ordene_id,o.ordsta_id ordsta_nombre,o.ordene_ped_precio,o.ordene_ped_monto,o.ordene_ped_fe_orden,o.ordene_adj_monto,o.ordene_fecha_adjudicacion,o.ORDENE_PED_FE_VALOR,c.client_nombre,u.undinv_nombre," +
					   " ot.TITULO_MTO_INT_CAIDOS,ot.TITULO_PCT_RECOMPRA, (SELECT DEXT.DTAEXT_VALOR FROM INFI_TB_212_ORDENES_DATAEXT DEXT WHERE DEXT.ORDENE_ID = o.ordene_id AND DEXT.DTAEXT_ID ='NRO_TICKET') AS NRO_TICKET " +
					   " from INFI_TB_204_ORDENES o, INFI_TB_201_CTES c, INFI_TB_106_UNIDAD_INVERSION u, INFI_TB_206_ORDENES_TITULOS ot " +
					   " where o.client_id=c.client_id and o.uniinv_id=u.undinv_id and o.transa_id='TOMA_ORDEN' "+
					   " and o.ordsta_id not in('"+StatusOrden.CANCELADA+"','"+StatusOrden.PENDIENTE+"') ");
			sql.append(" and o.ORDENE_PED_FE_ORDEN>= TO_DATE('"+fechaDesde+"','DD/MM/YYYY') AND o.ORDENE_PED_FE_ORDEN<= TO_DATE('"+fechaHasta+"','DD/MM/YYYY')");
			sql.append(" and  o.ordene_id in (select ORDENE_ID from INFI_TB_212_ORDENES_DATAEXT dex where  dex.DTAEXT_ID='"+DataExtendida.NRO_TICKET+"') ");
			sql.append(" and ot.ordene_id(+)=o.ordene_id");
			if(unidadInv>0){
				sql.append(" and u.UNDINV_ID=").append(unidadInv);
			}
			sql.append(" order by ordsta_nombre,o.ordene_id"); 
			
			//System.out.println("listarOrdenesClavenetPorFecha: "+sql.toString());
			
			return sql.toString();
		}
		
		//MODIFICADO PARA LA VERSION INFI_SICAD_ENT_2, CT15821
		/**
		 * Lista ordenes para los filtros con rango de fechas, unidad de inversión, estatus y tipo de producto
		 * @param fechaDesde
		 * @param fechaHasta
		 * @param unidadInv
		 * @param status
		 * @param tipoProductoId
		 * @return
		 * @throws Exception
		 */
			public String listarOrdenesPorFechaTomaOrdenEstatus(String fechaDesde, String fechaHasta,long unidadInv,String status, String tipoProductoId) throws Exception{								StringBuffer sql = new StringBuffer();			//System.out.println("METODO listarOrdenesClavenetPorFechaTomaOrden ");			sql.append("select o.ordene_id,o.ordsta_id ordsta_nombre,o.ordene_ped_precio,o.ordene_ped_monto,o.ordene_ped_fe_orden,o.ordene_adj_monto,o.ordene_fecha_adjudicacion,o.ORDENE_PED_FE_VALOR, o.ordene_tasa_cambio, c.client_nombre,u.undinv_nombre," +					   " ot.TITULO_MTO_INT_CAIDOS,ot.TITULO_PCT_RECOMPRA, (SELECT DEXT.DTAEXT_VALOR FROM INFI_TB_212_ORDENES_DATAEXT DEXT WHERE DEXT.ORDENE_ID = o.ordene_id AND DEXT.DTAEXT_ID ='NRO_TICKET') AS NRO_TICKET " +					   " from INFI_TB_204_ORDENES o, INFI_TB_201_CTES c, INFI_TB_106_UNIDAD_INVERSION u, INFI_TB_206_ORDENES_TITULOS ot " +					   " where o.client_id=c.client_id and o.uniinv_id=u.undinv_id and o.transa_id='TOMA_ORDEN' ");//+					   //" and o.ordsta_id not in('"+StatusOrden.CANCELADA+"','"+StatusOrden.PENDIENTE+"') ");			sql.append(" and o.ORDENE_PED_FE_ORDEN>= TO_DATE('"+fechaDesde+"','DD/MM/YYYY') AND o.ORDENE_PED_FE_ORDEN<= TO_DATE('"+fechaHasta+"','DD/MM/YYYY')");			//sql.append(" and  o.ordene_id in (select ORDENE_ID from INFI_TB_212_ORDENES_DATAEXT dex where  dex.DTAEXT_ID='"+DataExtendida.NRO_TICKET+"') ");			sql.append(" and ot.ordene_id(+)=o.ordene_id");			if(unidadInv>0){				sql.append(" and u.UNDINV_ID=").append(unidadInv);			}			if(status!=null && (!status.equals(""))){				sql.append(" and o.ORDSTA_ID=").append("'"+status+"'").append(" ");			}
			
			if(tipoProductoId!=null && !tipoProductoId.trim().equals("")){
				sql.append(" and o.TIPO_PRODUCTO_ID=").append("'"+tipoProductoId+"' ");
			}
						sql.append(" order by ordsta_nombre,o.ordene_id"); 						//System.out.println("listarOrdenesClavenetPorFecha: "+sql.toString());						return sql.toString();		}	
		 /**
		  * Lista el total de las ordenes para generar las operaciones de credito a BCV
		  * @throws Exception en caso de error
		  */
		 public String listarMontosGeneracionOrdenBCV(String unidadInversionId) throws Exception{
			 StringBuffer sql=new StringBuffer();
			 sql.append("SELECT insfin_id,empres_id,empres_rif,empres_cuenta,vehicu_id, vehicu_rif,vehicu_numero_cuenta, ");
		     sql.append("SUM (monto) monto,VEHICU_NUMERO_CUENTA_BCV,  bloter_id ");
		     sql.append("FROM (SELECT e.empres_id,e.empres_nombre,empres_rif,empres_cuenta,f.vehicu_id,f.vehicu_rif,f.vehicu_numero_cuenta, ");
		     sql.append("(a.ordene_ped_total - a.ordene_ped_comisiones) monto, d.insfin_id, F.VEHICU_NUMERO_CUENTA_BCV,bloter_id ");
		     sql.append("FROM infi_tb_204_ordenes a,infi_tb_106_unidad_inversion c,infi_tb_101_inst_financieros d, ");
		     sql.append("infi_tb_016_empresas e,infi_tb_018_vehiculos f WHERE     a.uniinv_id = c.undinv_id ");
		     sql.append("AND c.insfin_id = d.insfin_id AND a.empres_id = e.empres_id ");
		     sql.append("AND d.TIPO_PRODUCTO_ID = '").append(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL).append("' AND transa_id NOT IN ('").append(TransaccionNegocio.ORDEN_VEHICULO).append("','").append(TransaccionNegocio.LIQUIDACION).append("') ");
		     sql.append("AND ordene_adj_monto > 0 AND ordsta_id IN ('").append(StatusOrden.ADJUDICADA).append("') ");
		     sql.append("AND a.ordene_veh_tom = f.vehicu_id AND a.ordene_id_relacion IS NULL AND a.uniinv_id =").append(unidadInversionId).append(") ");
		     sql.append("GROUP BY insfin_id,empres_id,empres_rif, empres_cuenta, ");
		     sql.append("vehicu_id,vehicu_rif,vehicu_numero_cuenta,VEHICU_NUMERO_CUENTA_BCV,bloter_id");
			 
			 System.out.println("listarMontosGeneracionOrdenBCV: " + sql.toString());
			 
		 return sql.toString();
	 }   		/**		 * Metodo para realizar la busqueda del monto para la orden de pago a BCV 		 * */		//ITS-635		public String listarMontoOrdenBCVSubasta(long unidadInv)throws Exception {			StringBuffer sql=new StringBuffer();			String value=null;			/*sql.append("SELECT ABS(SUM(MONTO_OPERACION)) as MONTO_OPERACION  FROM (SELECT MONTO_OPERACION FROM INFI_TB_207_ORDENES_OPERACION OO,INFI_TB_204_ORDENES ORD ");			sql.append("WHERE ORD.ORDENE_ID=OO.ORDENE_ID AND ORD.UNIINV_ID=").append(unidadInv).append("AND ORD.ORDSTA_ID NOT IN('").append(StatusOrden.PENDIENTE).append("','").append(StatusOrden.CANCELADA).append("') ");			sql.append("AND OO.IN_COMISION=0 AND ORD.TRANSA_ID='").append(TRANSACCION_TOMA_ORDEN).append("' AND OO.TRNF_TIPO='DEB' ");			sql.append(" UNION ");			sql.append("SELECT MONTO_OPERACION*-1 FROM INFI_TB_207_ORDENES_OPERACION OO,INFI_TB_204_ORDENES ORD WHERE ORD.ORDENE_ID=OO.ORDENE_ID AND ORD.UNIINV_ID=").append(unidadInv).append(" ");			sql.append("AND ORD.ORDSTA_ID NOT IN('").append(StatusOrden.PENDIENTE).append("','").append(StatusOrden.CANCELADA).append("') AND ORD.TRANSA_ID='").append(TRANSACCION_TOMA_ORDEN).append("' AND OO.IN_COMISION=0 AND OO.TRNF_TIPO='CRE')");*/			
			sql.append("SELECT ABS(NVL(SUM(DECODE(OO.TRNF_TIPO,'DEB',SUM(OO.MONTO_OPERACION),'CRE',SUM(OO.MONTO_OPERACION)*-1)),0)) AS MONTO_OPERACION ");
			sql.append("  FROM  INFI_TB_207_ORDENES_OPERACION OO,INFI_TB_204_ORDENES ORD ");
			sql.append(" WHERE   OO.ORDENE_ID = ORD.ORDENE_ID AND ORD.UNIINV_ID =").append(unidadInv).append(" ");
			sql.append(" AND ORD.ORDSTA_ID NOT IN ('").append(StatusOrden.PENDIENTE).append("','").append(StatusOrden.CANCELADA).append("') ");
			sql.append(" AND ORD.TRANSA_ID ='").append(TRANSACCION_TOMA_ORDEN).append("' AND OO.IN_COMISION = 0 AND ORD.ORDENE_ADJ_MONTO > 0 GROUP BY OO.TRNF_TIPO");
			 //System.out.println(" listarMontoOrdenBCVSubasta " + sql.toString());			dataSet = db.get(dataSource, sql.toString());						if(dataSet.count()>0){				dataSet.first();				dataSet.next();							 value=dataSet.getValue("MONTO_OPERACION");				 if(value==null || value.equals("")){					 value="0";				 }			}						return value;				}
		
		/**
		 * Este metodo, realiza la agrupacion por vehiculo y muestra total del monto involucrado,<br>
		 * cobrado,pendiente,cantidad de ordenes y al igual los datos correspondientes al vehiculo para la generacion de la Orden a BCV
		 * los montos no reflejan las comisiones
		 * @param long unidadInversionId
		 * @throws Exception
		 */
		public void listarCreditoBCVPorVehiculoSubasta(long unidadInversionId,String status[])throws Exception{
			
			StringBuffer sql = new StringBuffer();
			Date fecha1 = new Date(Calendar.getInstance().getTimeInMillis());
			SimpleDateFormat formatter = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA_SIN_SEPARADOR_2);
			String fecha_funcion = formatter.format(fecha1);
			String fecha_re= ConstantesGenerales.FECHA_RECONVERSION;
			int fecha_reconversion= Integer.parseInt(fecha_re);
			int fecha_sistema= Integer.parseInt(fecha_funcion);
			System.out.println(fecha_funcion);
			System.out.println(fecha_sistema+" "+fecha_reconversion);
			System.out.println(fecha1);
			
			//if(fecha_sistema>=fecha_reconversion){
			int count=0;
			
			sql.append("SELECT T.vehicu_nombre, T.vehicu_numero_cuenta, T.vehicu_numero_cuenta_bcv, T.cantidad,SUM (T.monto_cobrado + T.monto_pendiente) total,T.monto_cobrado,T.monto_pendiente ");
			sql.append("FROM infi_tb_204_ordenes,(SELECT DISTINCT VEH.VEHICU_NOMBRE AS vehicu_nombre,DECODE (VEH.VEHICU_NUMERO_CUENTA,'null', 'Sin registrar',VEH.VEHICU_NUMERO_CUENTA) AS vehicu_numero_cuenta,VEH.VEHICU_NUMERO_CUENTA_BCV AS vehicu_numero_cuenta_bcv,");
			sql.append("TO_CHAR((SELECT COUNT (ordene_id) FROM infi_tb_204_ordenes a WHERE a.ORDENE_VEH_TOM = ORD.ORDENE_VEH_TOM AND uniinv_id =").append(unidadInversionId).append(" AND a.transa_id NOT IN ('");
			sql.append(TransaccionNegocio.ORDEN_VEHICULO).append("','").append(TransaccionNegocio.LIQUIDACION).append("') ");
			
			if(status!=null){				
				for(String element:status){
					if(count==0){
						sql.append("AND a.ordsta_id IN ('").append(element).append("'");
						++count;
					} else {
						sql.append(",'").append(element).append("'");
					}					
				}
				sql.append("))) AS cantidad, ");				
			}
			sql.append("(SELECT ABS (NVL (SUM (DECODE (OO2.TRNF_TIPO,'DEB', SUM (OO2.MONTO_OPERACION),'CRE', SUM (OO2.MONTO_OPERACION) * -1)),0))AS MONTO_OPERACION ");
			sql.append("FROM INFI_TB_207_ORDENES_OPERACION OO2, INFI_TB_204_ORDENES ORD2 WHERE     OO2.ORDENE_ID = ORD2.ORDENE_ID ");
			sql.append("AND ORD2.UNIINV_ID = ").append(unidadInversionId).append(" AND ORD2.ORDSTA_ID NOT IN ('").append(StatusOrden.PENDIENTE).append("','").append(StatusOrden.CANCELADA).append("') ");
			sql.append("AND ORD2.TRANSA_ID = '").append(TRANSACCION_TOMA_ORDEN).append("' ");
			sql.append("AND OO2.IN_COMISION = 0 AND ORD2.ORDENE_ADJ_MONTO > 0 AND OO2.STATUS_OPERACION NOT IN ('").append(STATUS_EN_ESPERA).append("','").append(STATUS_RECHAZADA).append("') GROUP BY OO2.TRNF_TIPO) AS monto_cobrado, ");
			sql.append("(SELECT ABS (NVL (SUM (DECODE (OO3.TRNF_TIPO,'DEB', SUM (OO3.MONTO_OPERACION),'CRE', SUM (OO3.MONTO_OPERACION) * -1)),0)) AS MONTO_OPERACION ");
			sql.append("FROM INFI_TB_207_ORDENES_OPERACION OO3,INFI_TB_204_ORDENES ORD3 WHERE     OO3.ORDENE_ID = ORD3.ORDENE_ID ");
			sql.append("AND ORD3.UNIINV_ID =").append(unidadInversionId).append(" AND ORD3.ORDSTA_ID NOT IN ('").append(StatusOrden.PENDIENTE).append("','").append(StatusOrden.CANCELADA).append("') ");
			sql.append("AND ORD3.TRANSA_ID = '").append(TRANSACCION_TOMA_ORDEN).append("' AND OO3.IN_COMISION = 0 AND ORD3.ORDENE_ADJ_MONTO > 0 AND OO3.STATUS_OPERACION IN ('");
			sql.append(STATUS_EN_ESPERA).append("','").append(STATUS_RECHAZADA).append("') GROUP BY OO3.TRNF_TIPO) AS monto_pendiente ");
			sql.append("FROM INFI_TB_018_VEHICULOS VEH, INFI_TB_204_ORDENES ORD WHERE VEH.VEHICU_ID = ORD.ORDENE_VEH_TOM AND ORD.UNIINV_ID =").append(unidadInversionId).append(") T ");
			sql.append("GROUP BY T.vehicu_nombre,T.vehicu_numero_cuenta,T.vehicu_numero_cuenta_bcv,T.cantidad,T.monto_cobrado,T.monto_pendiente ");
			//System.out.println("METODO NUEVO: " + sql.toString());
			dataSet = db.get(dataSource,sql.toString());
			System.out.println("listarCreditoBCVPorVehiculoSubasta "+sql);
		/*	}else{
				int count=0;
				
				sql.append("SELECT T.vehicu_nombre, T.vehicu_numero_cuenta, T.vehicu_numero_cuenta_bcv, T.cantidad, SUM (T.monto_cobrado + T.monto_pendiente) AS total,T.monto_cobrado,T.monto_pendiente ");
				sql.append("FROM (SELECT DISTINCT VEH.VEHICU_NOMBRE AS vehicu_nombre,DECODE (VEH.VEHICU_NUMERO_CUENTA,'null', 'Sin registrar',VEH.VEHICU_NUMERO_CUENTA) AS vehicu_numero_cuenta,VEH.VEHICU_NUMERO_CUENTA_BCV AS vehicu_numero_cuenta_bcv,");
				sql.append("TO_CHAR((SELECT COUNT (ordene_id) FROM infi_tb_204_ordenes a WHERE a.ORDENE_VEH_TOM = ORD.ORDENE_VEH_TOM AND uniinv_id =").append(unidadInversionId).append(" AND a.transa_id NOT IN ('");
				sql.append(TransaccionNegocio.ORDEN_VEHICULO).append("','").append(TransaccionNegocio.LIQUIDACION).append("') ");
				
				if(status!=null){				
					for(String element:status){
						if(count==0){
							sql.append("AND a.ordsta_id IN ('").append(element).append("'");
							++count;
						} else {
							sql.append(",'").append(element).append("'");
						}					
					}
					sql.append("))) AS cantidad, ");				
				}
				sql.append("(SELECT ABS (NVL (SUM (DECODE (OO2.TRNF_TIPO,'DEB', SUM (OO2.MONTO_OPERACION),'CRE', SUM (OO2.MONTO_OPERACION) * -1)),0))AS MONTO_OPERACION ");
				sql.append("FROM INFI_TB_207_ORDENES_OPERACION OO2, INFI_TB_204_ORDENES ORD2 WHERE     OO2.ORDENE_ID = ORD2.ORDENE_ID ");
				sql.append("AND ORD2.UNIINV_ID = ").append(unidadInversionId).append(" AND ORD2.ORDSTA_ID NOT IN ('").append(StatusOrden.PENDIENTE).append("','").append(StatusOrden.CANCELADA).append("') ");
				sql.append("AND ORD2.TRANSA_ID = '").append(TRANSACCION_TOMA_ORDEN).append("' ");
				sql.append("AND OO2.IN_COMISION = 0 AND ORD2.ORDENE_ADJ_MONTO > 0 AND OO2.STATUS_OPERACION NOT IN ('").append(STATUS_EN_ESPERA).append("','").append(STATUS_RECHAZADA).append("') GROUP BY OO2.TRNF_TIPO) AS monto_cobrado, ");
				sql.append("(SELECT ABS (NVL (SUM (DECODE (OO3.TRNF_TIPO,'DEB', SUM (OO3.MONTO_OPERACION),'CRE', SUM (OO3.MONTO_OPERACION) * -1)),0)) AS MONTO_OPERACION ");
				sql.append("FROM INFI_TB_207_ORDENES_OPERACION OO3,INFI_TB_204_ORDENES ORD3 WHERE     OO3.ORDENE_ID = ORD3.ORDENE_ID ");
				sql.append("AND ORD3.UNIINV_ID =").append(unidadInversionId).append(" AND ORD3.ORDSTA_ID NOT IN ('").append(StatusOrden.PENDIENTE).append("','").append(StatusOrden.CANCELADA).append("') ");
				sql.append("AND ORD3.TRANSA_ID = '").append(TRANSACCION_TOMA_ORDEN).append("' AND OO3.IN_COMISION = 0 AND ORD3.ORDENE_ADJ_MONTO > 0 AND OO3.STATUS_OPERACION IN ('");
				sql.append(STATUS_EN_ESPERA).append("','").append(STATUS_RECHAZADA).append("') GROUP BY OO3.TRNF_TIPO) AS monto_pendiente ");
				sql.append("FROM INFI_TB_018_VEHICULOS VEH, INFI_TB_204_ORDENES ORD WHERE VEH.VEHICU_ID = ORD.ORDENE_VEH_TOM AND ORD.UNIINV_ID =").append(unidadInversionId).append(") T ");
				sql.append("GROUP BY T.vehicu_nombre,T.vehicu_numero_cuenta,T.vehicu_numero_cuenta_bcv,T.cantidad,T.monto_cobrado,T.monto_pendiente ");
				//System.out.println("METODO NUEVO: " + sql.toString());
				dataSet = db.get(dataSource,sql.toString());
				System.out.println("listarCreditoBCVPorVehiculoSubasta "+sql);	
			}*/
		}
		
		/***
		 * Metodo para la busqueda de las operaciones de creditos pendientes por capital
		 * */
		//ITS-617
		public void  listarOrdenesPorAbonarCapital(long unidadInversion, String blotter) throws Exception{
			StringBuffer sql = new StringBuffer();
			long tiempoEjec=System.currentTimeMillis();

			sql.append("SELECT OO.ORDENE_ID,ORD.client_id,ORD.ORDSTA_ID,CONVERTIR_CUENTA_20A12 (ORD.ctecta_numero) cuenta,OO.MONTO_OPERACION ");
			sql.append("FROM INFI_TB_207_ORDENES_OPERACION OO,INFI_TB_204_ORDENES ORD WHERE ");			
			sql.append("ORD.UNIINV_ID =").append(unidadInversion).append(" AND ORD.transa_id = '").append(com.bancovenezuela.comun.logic.interfaces.ConstantesGenerales.TRANSACCION_TOMA_ORDEN).append("' ");
			sql.append("AND ORD.ORDSTA_ID IN ('").append(StatusOrden.ADJUDICADA).append("','").append(StatusOrden.PROCESO_ADJUDICACION).append("') ");
			sql.append("AND ORD.ORDENE_ID=OO.ORDENE_ID AND OO.TRNFIN_ID IN (1,2)  AND OO.TRNF_TIPO='CRE' ");
			sql.append("AND OO.STATUS_OPERACION <>'").append(com.bancovenezuela.comun.logic.interfaces.ConstantesGenerales.STATUS_APLICADA).append("'");
						
			if(!blotter.equals("-1")){
				sql.append(" and ORD.bloter_id='").append(blotter).append("'");
			}	
			sql.append(" ORDER BY OO.ORDENE_ID ");
			//System.out.println("listarOrdenesPorPagarCapital " + sql.toString());
			dataSet=db.get(dataSource, sql.toString());
			logger.info("listarOrdenesPorCobrarCapital: tiempo de ejecución="+(System.currentTimeMillis()-tiempoEjec)+"mseg ."+sql);//+sql
				
		}
		/***
		 * Metodo para la busqueda de las operaciones de creditos pendientes por comision
		 * */
		//ITS-617
		public void  listarOrdenesPorAbonarComision(long unidadInversion, String blotter) throws Exception{
			StringBuffer sql = new StringBuffer();
			long tiempoEjec=System.currentTimeMillis();
						
			sql.append("SELECT OO.ORDENE_ID,ORD.client_id,ORD.ORDSTA_ID,CONVERTIR_CUENTA_20A12 (ORD.ctecta_numero) cuenta,OO.MONTO_OPERACION ");
			sql.append("FROM INFI_TB_207_ORDENES_OPERACION OO, INFI_TB_204_ORDENES ORD WHERE     ORD.UNIINV_ID = ").append(unidadInversion).append(" ");
			sql.append("AND ORD.transa_id = '").append(TRANSACCION_TOMA_ORDEN).append("' ");
			sql.append("AND ORD.ORDSTA_ID IN ('").append(StatusOrden.ADJUDICADA).append("','").append(StatusOrden.PROCESO_ADJUDICACION).append("') ");
			sql.append("AND ORD.ORDENE_ID=OO.ORDENE_ID AND OO.TRNFIN_ID=11  AND OO.TRNF_TIPO='CRE' AND OO.STATUS_OPERACION <> '").append(com.bancovenezuela.comun.logic.interfaces.ConstantesGenerales.STATUS_APLICADA).append("' ");
			if(!blotter.equals("-1")){
				sql.append(" and ORD.bloter_id='").append(blotter).append("'");
			}	
			//sql.append(" and a.ORDENE_PED_COMISIONES>0");
			//System.out.println("listarOrdenesPorCobrarComision " + sql.toString());
			
			sql.append(" ORDER BY OO.ORDENE_ID ");
			this.dataSet =db.get(dataSource, sql.toString());
			
			logger.info("listarOrdenesPorCobrarComision: tiempo de ejecución="+(System.currentTimeMillis()-tiempoEjec)+"mseg ."+sql);
		}
				/**
		 * Recibe un conjunto de unidades de inversión a ejecutar
		 * @param unidadDeInversion id de la unidad a actualizar
		 * @return instrucción sql a ejecutar
		 * @throws Exception en caso de error
		 */
		 public void actualizarOrdenesRecepcionSitmeCuentaDolares(long idOrden) throws Exception{
			StringBuffer sql = new StringBuffer();
			//NM29643 infi_TTS_466 06/08/2014 Actualizacion Envio Correos
			sql.append("UPDATE INFI_TB_204_ORDENES ORD SET ORD.ORDSTA_ID='").append(StatusOrden.PROCESADA).append("' WHERE ORD.TRANSA_ID IN('").append(TRANSACCION_PACTO_RECOMPRA).append("', '").append(TransaccionNegocio.ORDEN_PAGO).append("') AND ORD.ORDSTA_ID='").append(StatusOrden.REGISTRADA).append("' AND ORD.ORDENE_ID=").append(idOrden); 			
			db.exec(dataSource, sql.toString());
				 
		 }
		 
		 //NM29643 INFI_TTS_466 25/07/2014
		 /** Marca el campo ORDENE_EJEC_ID de una orden con un id de ejecucion
		 * @param idOrdenTomaOrden: Id de la orden a marcar
		 * @paran ejecucionId: Id de ejecucion del proceso a actualizar en la orden
		 * @throws Exception en caso de error
		 */
		 public void actualizarOrdenesTomaOrdenRecepcionSitmeCuentaDolares(long idOrdenTomaOrden, String ejecucionId) throws Exception{
			StringBuffer sql = new StringBuffer();
			sql.append("UPDATE INFI_TB_204_ORDENES ORD SET ORD.ORDENE_EJEC_ID=").append(ejecucionId).append(" WHERE ORD.ordene_id=").append(idOrdenTomaOrden); 			
			db.exec(dataSource, sql.toString());
				 
		 }
			
		/**
		 * Metodo de busqueda de ID de orden Pacto Recompra basado en ID de orden TOMA_ORDEN (Orden Original)
		 * @param idOrden id de orden
		 * @throws Exception en caso de error
		 */
		public Long buscarDatosOrdenRelacion(Long idOrden,String transId) throws Exception{
			if(idOrden!=null){				
				String sql = "SELECT ORD.ORDENE_ID_RELACION FROM INFI_TB_204_ORDENES ORD WHERE ORD.ORDENE_ID="+idOrden;
				if(transId!=null){
					sql.concat(" AND ORD.TRANSA_ID='").concat(transId).concat("'");
				}
				
				dataSet = db.get(dataSource, sql);
				if(dataSet.count()>0){				
					dataSet.first();
					dataSet.next();
					return (dataSet.getValue("ORDENE_ID_RELACION")==null?null:Long.parseLong(dataSet.getValue("ORDENE_ID_RELACION")));
				}
			}
			
			return null; //La orden fue tomada desde INFI
		}
		
		public String insertCtesCtasOrdenes(String idOrden,String idInstruccionPago){
			StringBuffer sql=new StringBuffer();			
			sql.append("INSERT INTO INFI_TB_217_CTES_CUENTAS_ORD (ORDENE_ID,CTES_CUENTAS_ID) VALUES (").append(idOrden).append(",").append(idInstruccionPago).append(")") ;
			return sql.toString();
		}
		
		public String actualizarEstatusOrdenesIn(String idsOrdenes,String estatusOrd, long ordRelacId){
			StringBuffer sql = new StringBuffer();
			sql.append(" UPDATE INFI_TB_204_ORDENES SET ORDSTA_ID='"+estatusOrd+"'");
			if(ordRelacId>0){
				sql.append(", ORDENE_ID_RELACION=").append(ordRelacId);	
			}
			sql.append(" WHERE ORDENE_ID IN ("+idsOrdenes+")");		
			
			return sql.toString();		
			
		}	
		
		public void ListarTitulosAbonoCuentaDolaresVentas()throws Exception {					
			StringBuffer sql=new StringBuffer();
					
			sql.append("SELECT DISTINCT OT.TITULO_ID FROM INFI_TB_206_ORDENES_TITULOS OT,INFI_TB_204_ORDENES ORD,INFI_TB_207_ORDENES_OPERACION OO ");		
			sql.append(" WHERE     OO.ORDENE_ID = ORD.ORDENE_ID AND ORD.ORDENE_ID = OT.ORDENE_ID AND ORD.TRANSA_ID = '").append(TRANSACCION_VENTA_TITULOS).append("' ") ;		
			sql.append(" AND ORD.CTA_ABONO = ").append(ConstantesGenerales.ABONO_CUENTA_NACIONAL).append(" AND OO.STATUS_OPERACION IN ('").append(ConstantesGenerales.STATUS_EN_ESPERA).append("','").append(ConstantesGenerales.STATUS_RECHAZADA).append("') ");		
			sql.append("AND ORD.ORDSTA_ID = '").append(StatusOrden.REGISTRADA).append("' AND ORD.ORDENE_PED_FE_VALOR<=SYSDATE");						
			this.dataSet =db.get(dataSource, sql.toString());
		}
		
		public void resumenAbonoCuentaDolaresVentaTitulo(String idTitulo)throws Exception {
			StringBuffer sql=new StringBuffer();
						
			sql.append("SELECT '").append(idTitulo).append("' AS idTitulo,").append(" OT.TITULO_ID, OO.STATUS_OPERACION AS status_operacion,SUM (OO.MONTO_OPERACION) AS monto_operacion,OO.TRNF_TIPO AS trnf_tipo,COUNT (ORD.ORDENE_ID) AS TOTAL ");
			sql.append("FROM INFI_TB_204_ORDENES ORD,INFI_TB_207_ORDENES_OPERACION OO,INFI_TB_206_ORDENES_TITULOS OT ");
			sql.append("WHERE ORD.ORDENE_ID = OO.ORDENE_ID AND ORD.ORDENE_ID = OT.ORDENE_ID AND ORD.TRANSA_ID='").append(TRANSACCION_VENTA_TITULOS).append("' ");
			sql.append("AND ORD.ORDSTA_ID='").append(StatusOrden.REGISTRADA).append("'").append(" AND ORD.CTA_ABONO =").append(ConstantesGenerales.ABONO_CUENTA_NACIONAL).append(" AND ORD.ORDENE_PED_FE_VALOR<=SYSDATE ");
			
			if(idTitulo!=null && !idTitulo.equalsIgnoreCase("0")){
				sql.append("AND OT.TITULO_ID='").append(idTitulo).append("' ");
			}
			sql.append("GROUP BY OT.TITULO_ID, OO.STATUS_OPERACION, OO.TRNF_TIPO");
			//System.out.println("RESUMENTO ABONO VENTAS -------------> " + sql.toString());
			this.dataSet =db.get(dataSource, sql.toString());
		}
		
		public String detalleDeAbonoCuentaDolaresVentaTitulo(String idTitulo)throws Exception {
			StringBuffer sql=new StringBuffer();
			
			sql.append("SELECT OT.TITULO_ID,CTES.tipper_id,CTES.client_cedrif,CTES.client_nombre,ORD.ordene_id, ");
			sql.append("ORD.ORDENE_ID_RELACION,OO.ordene_operacion_id,OO.codigo_operacion,OO.numero_retencion,OO.ctecta_numero, ");
			sql.append("OO.status_operacion,OO.monto_operacion,OO.trnf_tipo,ORD.ordene_veh_tom,ORD.MONEDA_ID,ORD.CTA_ABONO ");
			sql.append("FROM INFI_TB_204_ORDENES ORD,INFI_TB_207_ORDENES_OPERACION OO,INFI_TB_201_CTES CTES,INFI_TB_206_ORDENES_TITULOS OT ");
			sql.append("WHERE     ORD.ORDENE_ID = OO.ORDENE_ID AND ORD.CLIENT_ID = CTES.CLIENT_ID AND OT.ORDENE_ID = ORD.ORDENE_ID ");
			sql.append("AND ORD.TRANSA_ID = '").append(TRANSACCION_VENTA_TITULOS).append("' AND ORD.ORDSTA_ID = '").append(StatusOrden.REGISTRADA).append("' ");
			sql.append("AND OO.STATUS_OPERACION IN ('").append(ConstantesGenerales.STATUS_EN_ESPERA).append("','").append(ConstantesGenerales.STATUS_RECHAZADA).append("') ");
			sql.append("AND ORD.CTA_ABONO =").append(ConstantesGenerales.ABONO_CUENTA_NACIONAL).append(" AND ORD.ORDENE_PED_FE_VALOR <= SYSDATE ");
			
			if(idTitulo!=null && !idTitulo.equalsIgnoreCase("0")){
				sql.append("AND OT.TITULO_ID='").append(idTitulo).append("' ");
			}
			//System.out.println("detalleDeAbonoCuentaDolaresVentaTitulo --------> " + sql.toString());
			return sql.toString();
		}
		
		/**
		 * Recibe el id de la orden y el tipo de transaccion de la orden
		 * @param idOrden id de la orden a actualizar
		 * @param transaId id del tipo de transaccion de la orden
		 * @throws Exception en caso de error
		 */
		 public void actualizarOrdenesRecepcionCuentaNacionalDolares(long idOrden,String transaId) throws Exception{
			StringBuffer sql = new StringBuffer();
			sql.append("UPDATE INFI_TB_204_ORDENES ORD SET ORD.ORDSTA_ID='").append(StatusOrden.PROCESADA).append("' WHERE ORD.TRANSA_ID='").append(transaId).append("' AND ORD.ORDSTA_ID='").append(StatusOrden.REGISTRADA).append("' AND ORD.ORDENE_ID=").append(idOrden); 			
			db.exec(dataSource, sql.toString());
				 
		 }
		 
		 /**
		  * Metodo que permite realizar la consulta del detalle de operaciones asociadas a ordenes de pago cupones
		  * */
		 public void resumenAbonoCuentaDolaresPagoCupones()throws Exception{
			StringBuffer sql=new StringBuffer();
			
			sql.append("SELECT OO.STATUS_OPERACION AS status_operacion,SUM (OO.MONTO_OPERACION) AS monto_operacion,OO.TRNF_TIPO AS trnf_tipo,COUNT (ORD.ORDENE_ID) AS TOTAL ");
			sql.append("FROM INFI_TB_204_ORDENES ORD, INFI_TB_207_ORDENES_OPERACION OO WHERE  OO.ORDENE_ID = ORD.ORDENE_ID ");
			sql.append("AND  ORD.TRANSA_ID = '").append(TransaccionNegocio.ORDEN_PAGO).append("' AND ORD.CTA_ABONO =").append(ConstantesGenerales.ABONO_CUENTA_NACIONAL).append(" ");
			sql.append("AND ORD.ORDSTA_ID='").append(StatusOrden.REGISTRADA).append("' AND ORD.ORDENE_PED_FE_VALOR <= SYSDATE ");
			sql.append("GROUP BY OO.STATUS_OPERACION,OO.TRNF_TIPO");
			this.dataSet =db.get(dataSource, sql.toString());
		 }
		 
		 /**
		  * Metodo que permite generar archivo txt exportable con el detalle de las operaciones de pago de cupones
		  * */
		 public String detalleDeAbonoCuentaDolaresPagoCupones()throws Exception{
			 StringBuffer sql=new StringBuffer();
				
				sql.append("SELECT CTES.tipper_id,CTES.client_cedrif,CTES.client_nombre,ORD.ordene_id,ORD.ORDENE_ID_RELACION, ");		 
				sql.append("OO.ordene_operacion_id,OO.codigo_operacion,OO.numero_retencion,OO.ctecta_numero,OO.status_operacion, ");
				sql.append("OO.monto_operacion,OO.trnf_tipo,ORD.ordene_veh_tom,ORD.MONEDA_ID,ORD.CTA_ABONO ");
				sql.append("FROM INFI_TB_204_ORDENES ORD,INFI_TB_207_ORDENES_OPERACION OO,INFI_TB_201_CTES CTES ");
				sql.append("WHERE     ORD.ORDENE_ID = OO.ORDENE_ID AND ORD.CLIENT_ID = CTES.CLIENT_ID ");				
				sql.append("AND  ORD.TRANSA_ID = '").append(TransaccionNegocio.ORDEN_PAGO).append("' AND ORD.CTA_ABONO =").append(ConstantesGenerales.ABONO_CUENTA_NACIONAL).append(" ");
				sql.append("AND ORD.ORDSTA_ID='").append(StatusOrden.REGISTRADA).append("'").append(" AND OO.STATUS_OPERACION IN ('").append(ConstantesGenerales.STATUS_EN_ESPERA).append("','").append(ConstantesGenerales.STATUS_RECHAZADA).append("') AND ORD.ORDENE_PED_FE_VALOR <= SYSDATE ");
				
				return sql.toString(); 
		 }
		 
		 //Método modificado para la versión infi_tts_368
		 /**
		  * Arma el query para obtener el detalle de las ordenes de pago asociadas a los criterios de búsqueda
		  * @param idCliente
		  * @param idTitulo
		  * @param fechaDesde
		  * @param fechaHasta
		  * @param statusOperacion
		  * @param transaccion
		  * @return String con el query a ejecutar
		  * @throws Exception
		  */
		 public String listarDetalleOrdenPago(String idCliente, String idTitulo, String fechaDesde, String fechaHasta, String statusOperacion, String transaccion) throws Exception{
			 StringBuffer sql=new StringBuffer();
			
			 
			 sql.append("SELECT O.ORDENE_ID, CL.TIPPER_ID,CL.CLIENT_CEDRIF,CL.CLIENT_NOMBRE,O.TRANSA_ID,OP.MONTO_OPERACION,OP.STATUS_OPERACION, OP.MONEDA_ID,to_char(OP.FECHA_APLICAR,'").append(ConstantesGenerales.FORMATO_FECHA).append("') FECHA_APLICAR,");
			 sql.append(" CASE WHEN O.CTA_ABONO="+ConstantesGenerales.ABONO_CUENTA_EXTRANJERO+" THEN (SELECT '(CTA SWIFT) NRO:'|| CC.CTECTA_NUMERO|| ', BANCO BENEFICIARIO: '|| CC.CTECTA_BCOCTA_BCO || ', BIC: ' || cc.CTECTA_BCOCTA_BIC FROM INFI_TB_202_CTES_CUENTAS CC, INFI_TB_217_CTES_CUENTAS_ORD CO WHERE CC.CTES_CUENTAS_ID=CO.CTES_CUENTAS_ID AND CO.ORDENE_ID=O.ORDENE_ID)");
			 sql.append(" WHEN O.CTA_ABONO="+ConstantesGenerales.ABONO_CUENTA_NACIONAL+" THEN ('(CTA NACIONAL EN DOLARES) NRO:'||OP.CTECTA_NUMERO ) END INSTRUCCION_PAGO, ");
			 sql.append(" CASE WHEN O.CTA_ABONO="+ConstantesGenerales.ABONO_CUENTA_EXTRANJERO+" THEN (SELECT DECODE(cc.CTECTA_BCOINT_BIC,NULL,'', 'BANCO: ' || cc.CTECTA_BCOINT_BCO || ', BIC: ' || cc.CTECTA_BCOINT_BIC || DECODE(cc.CTECTA_BCOINT_SWIFT, NULL, '', ', CUENTA NRO: ' || cc.CTECTA_BCOINT_SWIFT)) FROM INFI_TB_202_CTES_CUENTAS CC, INFI_TB_217_CTES_CUENTAS_ORD CO WHERE CC.CTES_CUENTAS_ID=CO.CTES_CUENTAS_ID AND CO.ORDENE_ID=O.ORDENE_ID) END BANCO_INTERMEDIARIO ");
			 sql.append(" FROM INFI_TB_204_ORDENES O, INFI_TB_207_ORDENES_OPERACION OP, INFI_TB_201_CTES CL ");
			 sql.append(" WHERE O.ORDENE_ID=OP.ORDENE_ID");
			 sql.append(" AND O.CLIENT_ID=CL.CLIENT_ID");
			 if(fechaDesde!=null &&fechaHasta!=null){
				 sql.append(" AND TO_DATE(O.ORDENE_PED_FE_ORDEN,'").append(ConstantesGenerales.FORMATO_FECHA_RRRR).append("') BETWEEN TO_DATE('").append(fechaDesde).append("','").append(ConstantesGenerales.FORMATO_FECHA_RRRR).append("') AND TO_DATE('").append(fechaHasta).append("','").append(ConstantesGenerales.FORMATO_FECHA_RRRR).append("')"); 
			 }
			 if(idTitulo!=null && !idTitulo.equals("")){
				 sql.append(" AND OP.TITULO_ID='"+idTitulo+"'"); 
			 }
			 if(statusOperacion!=null){
				 if(statusOperacion.equals("1")){
					 sql.append(" AND OP.STATUS_OPERACION='"+ConstantesGenerales.STATUS_APLICADA+"'");	 
				 }else if(statusOperacion.equals("2")){
					 sql.append(" AND OP.STATUS_OPERACION IN ('"+ConstantesGenerales.STATUS_RECHAZADA+"','").append(ConstantesGenerales.STATUS_EN_ESPERA).append("') ");
				 }
				 //sql.append(" AND OP.STATUS_OPERACION='"+statusOperacion+"'"); 
			 }			 
			 if(transaccion!=null){
				 sql.append(" AND O.TRANSA_ID='"+transaccion+"'");
			 }
			 if(idCliente!=null&&idCliente.length()>0){
				 sql.append(" AND O.CLIENT_ID="+idCliente);	
			 }	
			 sql.append(" ORDER BY O.ORDENE_ID");	
			 //System.out.println("listarDetalleOrdenPago ------"+sql.toString());
			 return sql.toString(); 
			
		 }
		 
		 public String listarDetalleOrdenPagoAsociada(String idOrdenRelac) throws Exception{
			 StringBuffer sql=new StringBuffer();			 
			 sql.append("SELECT ORD.TRANSA_ID, ORD.TIPO_PRODUCTO_ID, OP.MONEDA_ID,ORD.ORDENE_ID,OP.ORDENE_OPERACION_ID,OT.TITULO_ID,OP.STATUS_OPERACION,OP.MONTO_OPERACION");
			 sql.append(" FROM INFI_TB_204_ORDENES ORD,INFI_TB_207_ORDENES_OPERACION OP,INFI_TB_206_ORDENES_TITULOS OT");
			 sql.append(" WHERE ORD.ORDENE_ID=OP.ORDENE_ID AND ORD.ORDENE_ID=OT.ORDENE_ID ");
			 sql.append(" AND ORD.ORDENE_ID_RELACION=").append(idOrdenRelac);		 
			 return sql.toString(); 
			
		 }
		 
		 /**
			 * Lista el vehiculo asociado a una unidad de inversion 
			 * que sean estatus enviada o adjudicada y donde 
			 * la transaccion sea toma de orden o toma de orden cartera propia
			 * @param idUnidad unidad de inversi&oacute;n
			 * */
			public void listarOrdenesPorAdjudicarByUnidadInversionID(String unidad) throws Exception{
				StringBuffer sql = new StringBuffer();
				
				sql.append("select u.undinv_nombre,o.ordene_veh_col,v.vehicu_nombre,count(o.ordene_id) as ordenes, (count(oporadj.ordene_id)+count(oen.ordene_id)) as por_adjudicar, count(oadj.ordene_id) as adjudicadas,count(oen.ordene_id) as enviadas,");
				
				sql.append("o.uniinv_id from INFI_TB_204_ORDENES o inner join INFI_TB_012_TRANSACCIONES t on t.transa_id=o.transa_id inner join INFI_TB_106_UNIDAD_INVERSION u on o.uniinv_id=u.undinv_id inner join INFI_TB_018_VEHICULOS v on v.vehicu_id=o.ordene_veh_col left join INFI_TB_204_ORDENES oadj on o.ordene_id = oadj.ordene_id and oadj.ordsta_id='");
				sql.append(StatusOrden.ADJUDICADA);
				sql.append("' left join INFI_TB_204_ORDENES oen on o.ordene_id = oen.ordene_id and oen.ordsta_id='");
				sql.append(StatusOrden.ENVIADA);
				sql.append("' left join INFI_TB_204_ORDENES oporadj on o.ordene_id = oporadj.ordene_id and oporadj.ordsta_id='");
				sql.append(StatusOrden.PROCESO_ADJUDICACION);
				sql.append("' where(t.transa_id='");
				sql.append(TransaccionNegocio.TOMA_DE_ORDEN).append("')");
								
				if (unidad!=null){
					sql.append(" and o.uniinv_id=").append(unidad);
				}
				else
				sql.append("')");
				
				sql.append(" GROUP BY o.ordene_veh_col,v.vehicu_nombre,o.uniinv_id,t.transa_id,u.undinv_nombre");
				//System.out.println("listarOrdenesPorAdjudicarByUnidadInversionID -------> " + sql.toString());
				dataSet = db.get(dataSource, sql.toString());
			}
			
			//Motodo Desarrollado requerimiento SICAD nm26659
			public String actualizarMontoOrdenTitulo(OrdenTitulo ordenTitulo){
				
				StringBuffer sql=new StringBuffer();
				
				sql.append("UPDATE INFI_TB_206_ORDENES_TITULOS OT SET OT.TITULO_MONTO=").append(ordenTitulo.getMonto()).append(" WHERE OT.TITULO_ID='").append(ordenTitulo.getTituloId()).append("'  AND  OT.ORDENE_ID=").append(ordenTitulo.getIdOrden());
				
				return sql.toString();
			}
			
			//Motodo Desarrollado requerimiento SICAD nm26659
			public void ordenesPorAdjudicarByIdUnidad(long idUnidad)throws Exception {
				StringBuffer sql=new StringBuffer();				
				sql.append("SELECT ORD.ORDENE_ID from INFI_TB_204_ORDENES ORD WHERE ORD.ORDSTA_ID='").append(StatusOrden.ENVIADA).append("' and ord.UNIINV_ID=").append(idUnidad).append(" AND ORD.TRANSA_ID='").append(TransaccionNegocio.TOMA_DE_ORDEN).append("'");
				dataSet = db.get(dataSource, sql.toString());
			}
			
			/**
			 * Metodo de insercion de documentos asociados al proceso de adjudicacion
			 * */
			//Metodo creado en requerimiento SICAD_Ent_2 
			public String insertarDocumentosTransaccion(Orden orden) throws Exception{
				//Objeto para el query
				StringBuffer sqlDocumentos = new StringBuffer();
				sqlDocumentos.append("INSERT INTO INFI_TB_208_ORDENES_DOCUMENTOS (ORDENE_ID,ORDENE_DOC_ID, NOMBRE)");
				sqlDocumentos.append(" VALUES(");
				
				String query=null;
				
				//Almacena los documentos asociados a la orden
				if (!orden.isDocumentosVacio()){
					ArrayList listaDocumentos = orden.getDocumentos();
					String[] st = new String[listaDocumentos.size()];
					int contador = 0;
					//Se recorre la lista y se almacenan en la base de datos			
					for (Iterator iter = listaDocumentos.iterator(); iter.hasNext(); ) {
						StringBuffer sqlDocumentosValues = new StringBuffer();
						sqlDocumentosValues.append(sqlDocumentos); //Almacena el insert
						
						OrdenDocumento documento =	(OrdenDocumento) iter.next();
						
						//Verifica si el id del documento es 0 para insertarlo
						//if (documento.getIdDocumento()==0){				
							documento.setIdOrden(orden.getIdOrden());
							sqlDocumentosValues.append(orden.getIdOrden()).append(", ");
							sqlDocumentosValues.append(documento.getIdDocumento()).append(", ");
							sqlDocumentosValues.append("'").append(documento.getNombre()).append("'");
							sqlDocumentosValues.append(")");
							
							//Almacena la consulta generada
							st[contador++] = sqlDocumentosValues.toString();
							query=sqlDocumentosValues.toString();
						//}				
					}
				
				}
				return query;
			}
			
			/**
			 * Lista las ordenes que contengan operaciones de débito capital pendientes por cobrar 
			 * y a las cuales no se les haya generado una operación de débido de comisión Buen Valor
			 * @param tipoProductoId
			 * @throws Exception
			 */
			public void listarOrdenesConDebitoCapitalPendiente(String tipoProductoId) throws Exception{
				
				StringBuffer sb = new StringBuffer();
				
				sb.append("SELECT distinct ord.ordene_veh_tom, ui.insfin_id ");
				sb.append("FROM infi_tb_207_ordenes_operacion oo, infi_tb_204_ordenes ord, infi_tb_106_unidad_inversion ui ");
				sb.append("WHERE oo.ordene_id = ord.ordene_id ");
				sb.append("AND ord.UNIINV_ID = ui.undinv_id ");
				sb.append("AND oo.status_operacion IN ('").append(ConstantesGenerales.STATUS_EN_ESPERA).append("', '").append(ConstantesGenerales.STATUS_RECHAZADA).append("') ");
				sb.append("AND oo.trnf_tipo = '").append(com.bdv.infi.logic.interfaces.TransaccionFinanciera.DEBITO).append("' ");
				sb.append("AND oo.trnfin_id = 1 ");
				sb.append("AND ord.tipo_producto_id = '").append(tipoProductoId).append("' ");
				sb.append("AND ord.ORDENE_ID NOT IN (select o.ordene_id from infi_tb_204_ordenes o where o.TIPO_PRODUCTO_ID = '").append(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA).append("' and o.ordene_id IN (select oo.ordene_id from infi_tb_207_ordenes_operacion oo where oo.TRNFIN_ID = 18 and oo.TRNF_TIPO = '").append(com.bdv.infi.logic.interfaces.TransaccionFinanciera.DEBITO).append("')) "); 
				sb.append("ORDER BY ord.ordene_veh_tom, ui.insfin_id ASC");
				
				System.out.println("ORDENES PENDIENTES DEBITO CAPITAL "+ sb.toString());
				
				dataSet = db.get(dataSource, sb.toString());			
			}
			
			/**
			 * NM29643
			 * Actualiza diversos campos de la orden
			 * @param long ordenId
			 * @param int cta_abono
			 * @param String contraparte
			 * @throws Exception
			 */
			public void updateCtaAbonoOrden(long ordenId, int cta_abono, String contraparte) throws Exception{
				StringBuffer sql = new StringBuffer();
				sql.append("UPDATE infi_tb_204_ordenes SET cta_abono=").append(cta_abono).append(", ");
				sql.append("CONTRAPARTE='").append(contraparte).append("' ");
				//sql.append("ORDENE_PED_CTA_BSNRO='").append(cta_bs).append("', ");
				//sql.append("CTECTA_NUMERO='").append(cta_swift_nacDol).append("' ");
				sql.append("where ordene_id=").append(ordenId);
				db.exec(dataSource, sql.toString());
			}
			
			/**
			 * NM29643
			 * Actualiza la cta en bolivares (ORDENE_PED_CTA_BSNRO) y la cuenta nac en $ o SWIFT del cliente(CTECTA_NUMERO)
			 * @param long ordenId
			 * @param String cta_bs
			 * @param String cta_swift_nacDol
			 * @throws Exception
			 */
			/*public void updateCtasOrden(long ordenId, String cta_bs, String cta_swift_nacDol) throws Exception{
				StringBuffer sql = new StringBuffer();
				sql.append("update infi_tb_204_ordenes set ORDENE_PED_CTA_BSNRO='").append(cta_bs).append("', ");
				sql.append("CTECTA_NUMERO='").append(cta_swift_nacDol).append("' ");
				sql.append("where ordene_id=").append(ordenId);
				db.exec(dataSource, sql.toString());
			}*/
			
			/**NM29643
			 * Obtiene el detalle de una orden dado el id de solicitud clavenet (ID_TICKET_CLAVENET) 
			 * @param nroSolicitud
			 * @throws Exception
			 */
			public void getDetallesOrdenFromNroSolicitud(long nroSolicitud) throws Exception{
				
				StringBuffer sb = new StringBuffer();
				
				sb.append("SELECT od.ORDENE_ID, od.ID_TICKET_CLAVENET, od.ULT_FECHA_ACTUALIZACION ");
				sb.append("FROM infi_tb_220_ordene_detalles od WHERE od.ID_TICKET_CLAVENET=").append(nroSolicitud);
				//System.out.println("ORDENES DETALLES SELECT:\n"+ sb.toString());
				
				dataSet = db.get(dataSource, sb.toString());			
			}
			
			
			/**
			 * Realiza la llamada al stored procedure que valida la creacion de nuevas ordenes segun estatus y tipo de producto
			 * @param cedRif
			 * @param canal
			 * @param estatus
			 * @throws Exception
			 */
			public String[] callSolicitudCanal(String cedRif, String canal, String fechaInicio, String fechaFin) throws Exception{
				
				String result[] = {"", "", "", ""};
				SimpleDateFormat sdf = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA3);
				Date fechaRegistro = null;
				//Date dateIni = null, dateFin = null;
				//java.sql.Date sqlDateIni = null, sqlDateFin = null;
				String nroSolicitud = "";
				String existe = "";
				String error = "";
				/*try{
					dateIni = sdf.parse(fechaInicio);
					sqlDateIni = new java.sql.Date(dateIni.getTime());
					dateFin = sdf.parse(fechaFin);
					sqlDateFin = new java.sql.Date(dateFin.getTime());
				} catch (ParseException ex) {
					ex.printStackTrace();
				}*/


				
				try {
					//conn = this.dataSource.getConnection();
					conn = conn==null?this.dataSource.getConnection():conn;	
					
					CallableStatement procedimientoAlmacenado = conn.prepareCall("{ call VALIDACION.SOLICITUD_CANAL(?,?,?,?,?,?,?,?) }");
					// cargar parametros al SP
					procedimientoAlmacenado.setString(1, cedRif);
					procedimientoAlmacenado.setString(2, canal);
					//procedimientoAlmacenado.setDate(3, sqlDateIni);
					//procedimientoAlmacenado.setDate(4, sqlDateFin);
					procedimientoAlmacenado.setString(3, fechaInicio);
					procedimientoAlmacenado.setString(4, fechaFin);
					procedimientoAlmacenado.registerOutParameter(5,java.sql.Types.DATE);
					procedimientoAlmacenado.registerOutParameter(6,java.sql.Types.VARCHAR);
					procedimientoAlmacenado.registerOutParameter(7,java.sql.Types.VARCHAR);
					procedimientoAlmacenado.registerOutParameter(8,java.sql.Types.VARCHAR);
					
					//Ejecutar el StoredProcedure
					procedimientoAlmacenado.execute();
					
					//Obtiene las variables resultado
					fechaRegistro = procedimientoAlmacenado.getDate(5);
					nroSolicitud = procedimientoAlmacenado.getString(6);
					existe = procedimientoAlmacenado.getString(7);
					error = procedimientoAlmacenado.getString(8);

					if(existe.equals("0")){//Si no se encontro una orden coincidente
						result[2] = existe;
						//{"", "", existe, ""};
					}else{
						if(existe.equals("1")){//Si se encontro una orden coincidente
							result[0] = sdf.format(fechaRegistro);
							result[1] = nroSolicitud;
							result[2] = existe;
							result[3] = error;
							//{sdf.format(fechaRegistro), nroSolicitud, existe, error};
						}else{
							if(existe.equals("2")){//Si ocurrio un error
								result[2] = existe;
								result[3] = error;
								//{"", "", existe, error};
							}
						}
					}
					
				} catch (Exception e) {
					logger.error("Error llamando al stored procedure SOLICITUD_CANAL de validacion para la creacion de nuevas ordenes", e);
					//throw new Exception("Error llamando al stored procedure SOLICITUD_CANAL de validacion para la creacion de nuevas ordenes" + e);
				} finally {
					this.closeResources();
					this.cerrarConexion();
				}	
				
				return result;
			}
			
			/**
			 * @param orden
			 * @param dataExtTipo
			 * @param valor
			 */
			public void insertValoresDataExtendida(long orden, String dataExtTipo, String valor) throws Exception{
				
				StringBuffer sql=new StringBuffer();
				
				sql.append("INSERT INTO INFI_TB_212_ORDENES_DATAEXT (ORDENE_ID,DTAEXT_ID,DTAEXT_VALOR) VALUES (");
				sql.append(orden);
				sql.append(",");
				sql.append("'").append(dataExtTipo).append("'");
				sql.append(",");
				sql.append("'").append(valor).append("'");
				sql.append(")");
				
				db.exec(dataSource,sql.toString());
				
			}

			 /**
			 *  Valida que la cancelación se haga el mismo día de la toma de orden, en el rango de horarios y fecha configurados 
			 *  a unidades de inversión en estatus 'PUBLICADA' o por algún usuario especial. NM25287 
			 *  @param ordeneId 	Id de orden
			 *  @param unidadInvId	Id de Unidad de Inversion
			 *  @param usuarioId	Id de usuario
			 *  @author nm25287
			 * @throws Exception
			 */
			public boolean validarCancelacionOrden(String ordeneId, String unidadInvId,String usuarioId)throws Exception{
				StringBuffer sb = new StringBuffer();
				sb.append("SELECT COUNT(*) NUM FROM INFI_TB_204_ORDENES O, INFI_TB_106_UNIDAD_INVERSION UI");
				sb.append(" WHERE");
				sb.append(" (");
				sb.append("   ((SELECT COUNT (*) FROM infi_tb_107_ui_blotter uib");
				sb.append("   WHERE");
				//				VALIDACION DE HORARIO DE TOMA DE ORDENES - ULTIMO DIA
				sb.append("   (( uiblot_pedido_fe_fin =TO_DATE (TO_CHAR(SYSDATE,'").append(ConstantesGenerales.FORMATO_FECHA_SYSDATE_2).append("'), '").append(ConstantesGenerales.FORMATO_FECHA_SYSDATE_2).append("')");
				sb.append("   AND TO_DATE(TO_CHAR (SYSDATE, '").append(ConstantesGenerales.FORMATO_FECHA_SYSDATE_2).append("')||''||SUBSTR(TO_CHAR(uiblot_horario_desde_ult_dia, '").append(ConstantesGenerales.FORMATO_FECHA_SYSDATE_3).append("'),11,19) , '").append(ConstantesGenerales.FORMATO_FECHA_SYSDATE_3).append("') <=SYSDATE");
				sb.append("   AND TO_DATE(TO_CHAR (SYSDATE, '").append(ConstantesGenerales.FORMATO_FECHA_SYSDATE_2).append("')||''||SUBSTR(TO_CHAR(uiblot_horario_hasta_ult_dia, '").append(ConstantesGenerales.FORMATO_FECHA_SYSDATE_3).append("'),11,19) , '").append(ConstantesGenerales.FORMATO_FECHA_SYSDATE_3).append("') >=SYSDATE");
				sb.append("   )");
				//				VALIDACION DE HORARIO DE TOMA DE ORDENES - PRIMEROS DIAS 
				sb.append("   OR ( uiblot_pedido_fe_fin >TO_DATE (TO_CHAR(SYSDATE,'").append(ConstantesGenerales.FORMATO_FECHA_SYSDATE_2).append("'), '").append(ConstantesGenerales.FORMATO_FECHA_SYSDATE_2).append("')");
				sb.append("   AND TO_DATE(TO_CHAR (SYSDATE, '").append(ConstantesGenerales.FORMATO_FECHA_SYSDATE_2).append("')||''||SUBSTR(TO_CHAR(uiblot_horario_desde, '").append(ConstantesGenerales.FORMATO_FECHA_SYSDATE_3).append("'),11,19) , '").append(ConstantesGenerales.FORMATO_FECHA_SYSDATE_3).append("') <=SYSDATE");
				sb.append("   AND TO_DATE(TO_CHAR (SYSDATE, '").append(ConstantesGenerales.FORMATO_FECHA_SYSDATE_2).append("')||''||SUBSTR(TO_CHAR(uiblot_horario_hasta, '").append(ConstantesGenerales.FORMATO_FECHA_SYSDATE_3).append("'),11,19) , '").append(ConstantesGenerales.FORMATO_FECHA_SYSDATE_3).append("') >=SYSDATE");
				sb.append("   ))");
				sb.append("   AND uib.bloter_id = O.BLOTER_ID");
				sb.append("   AND uib.undinv_id = ").append(unidadInvId);
				sb.append("	)>0");
				sb.append("	AND (TO_DATE(SYSDATE, '").append(ConstantesGenerales.FORMATO_FECHA_SYSDATE_2).append("')=TO_DATE(O.ORDENE_PED_FE_ORDEN, '").append(ConstantesGenerales.FORMATO_FECHA_SYSDATE_2).append("'))   ");
				sb.append(" AND UI.INDC_PERMITE_CANCELACION=1");
				sb.append(" )");
				sb.append(" OR (SELECT COUNT(*) FROM INFI_TB_104_BLOTER_USUARIOS  BR WHERE UPPER(BR.USERID)=UPPER('").append(usuarioId).append("'))>0");
				sb.append(")");
				//ITS-2374 - NM26659 Resolucion incidencia NO se pueden cancelar ordenes pendientes de unidades cerradas
				sb.append("AND (UI.UNDINV_STATUS='").append(UnidadInversionConstantes.UISTATUS_PUBLICADA).append("' ");
				sb.append("OR O.ORDSTA_ID=").append("'").append(StatusOrden.PENDIENTE).append("') ");
				sb.append("AND UI.UNDINV_ID=O.UNIINV_ID ");
				sb.append("AND O.ORDENE_ID=").append(ordeneId).append(" ");
				sb.append("AND O.UNIINV_ID=").append(unidadInvId).append(" ");
				//NM25287	25/03/2015 VALIDAR QUE LA OPERACION NO SE HAYA ENVIADO AL BCV. TTS-491 WS ALTO VALOR
				sb.append("AND ORDENE_ESTATUS_BCV NOT IN (").append(ConstantesGenerales.VERIFICADA_APROBADA).append(",").append(ConstantesGenerales.VERIFCADA_MANUAL_APROBADA).append(") ");				
								 
				//System.out.println("validarCancelacionOrden: "+ sb.toString());
				
				dataSet = db.get(dataSource, sb.toString());
				
				if(dataSet.count()>0){				
					dataSet.first();
					dataSet.next();
					if(Integer.parseInt(dataSet.getValue("NUM"))>0)
						return true;
				}				
				return false;
			}
			
			/** NM29643
			 * Obtiene ordenes de un cliente asociadas a una unidad de inversion
			 * @param transaccion Transaccion asociada a las ordenes a buscar
			 * @param tipCiCliente fecha inicial de la consulta para buscar las ordenes que esten dentro del rango de fechas
			 * @param estatus Estatus de las ordenes a buscar
			 * @param idUnidInv Id de la unidad de inversion asociada a las ordenes a buscar
			 * @param tipProd Tipo de producto asociado a las ordenes a buscar
			 * @throws lanza una excepción en caso de error*/	
			public void listarOrdenesClienteUI(String transaccion, String tipCiCliente, String idUnidInv, String tipProd, String... estatus) throws Exception {
				StringBuffer sb = new StringBuffer();
				sb.append("SELECT o.ordene_id, to_char(o.ordene_ped_fe_orden, '").append(ConstantesGenerales.FORMATO_FECHA).append("') as ordene_ped_fe_orden, ui.undinv_fe_emision, ui.undinv_fe_cierre, c.client_id, c.CLIENT_NOMBRE");
				sb.append(" FROM INFI_TB_204_ORDENES o, INFI_TB_106_UNIDAD_INVERSION ui, INFI_TB_201_CTES c WHERE ");
				sb.append(" o.UNIINV_ID = ui.UNDINV_ID ");
				sb.append(" AND o.UNIINV_ID = ").append(idUnidInv);
				sb.append(" AND o.CLIENT_ID = c.CLIENT_ID ");
				sb.append(" AND o.CLIENT_ID = (SELECT client_id FROM INFI_TB_201_CTES WHERE TIPPER_ID='").append(tipCiCliente.charAt(0)).append("' AND CLIENT_CEDRIF=").append(tipCiCliente.substring(1,tipCiCliente.length())).append(")");
				sb.append(" AND o.transa_id = '").append(transaccion).append("'");
				sb.append(" AND o.tipo_producto_id = '").append(tipProd).append("'");
				if(estatus.length>0 && estatus[0]!=null){
					sb.append(" AND o.ordsta_id IN (");
					for(int i=0; i<estatus.length; i++){
						sb.append("'").append(estatus[i]).append("'");
						if(i < estatus.length-1) sb.append(", ");
					}
					sb.append(")");
				}
				//sb.append(" AND o.client_id = ( SELECT C.client_id, C.CLIENT_NOMBRE FROM INFI_TB_201_CTES C WHERE C.tipper_id = substr('").append(tipCiCliente).append("',1,1) AND C.client_cedrif = to_number(substr('").append(tipCiCliente).append("',2)) )");
				sb.append(" AND o.ordene_ped_fe_orden BETWEEN ui.undinv_fe_emision AND ui.undinv_fe_cierre");
				sb.append(" ORDER BY o.ordene_id");
//System.out.println("SQLLLLLLLLLLLLLL listarOrdenesClienteUI----------\n"+sb.toString());
				
				dataSet = db.get(dataSource, sb.toString());
			}
			//NM25287 _ 25/03/2014   
			public void consultarDataExtendidaOrden(String ordeneId, String... dataExtIds) throws Exception{
				StringBuffer sb = new StringBuffer();
				sb.append("SELECT ORDENE_ID,DTAEXT_ID,DTAEXT_VALOR FROM INFI_TB_212_ORDENES_DATAEXT WHERE ORDENE_ID=").append(ordeneId);
							
				if(dataExtIds.length>0){			
					sb.append(" AND DTAEXT_ID IN ").append("(");				
					for(int i=0;i<dataExtIds.length;++i){					
						sb.append("'").append(dataExtIds[i]).append("'");
						if(i!=dataExtIds.length-1){
							sb.append(",");
						}					
					}
					sb.append(")");
					sb.append(" ORDER BY DTAEXT_ID");
				}
				//System.out.println("consultarDataExtendidaOrden: "+sb.toString());
				dataSet = db.get(dataSource, sb.toString());
				
			}
			
     		//NM25287 _ 25/03/2014   
			public void consultarDataExtendidaUI(String unidadInvId) throws Exception{
				StringBuffer sb = new StringBuffer();
				sb.append("SELECT DISTINCT(DTAEXT_ID) FROM INFI_TB_212_ORDENES_DATAEXT DE WHERE DE.ORDENE_ID IN ");
				sb.append("(SELECT ORDENE_ID FROM INFI_TB_204_ORDENES O WHERE UNIINV_ID=").append(unidadInvId).append(")");
				sb.append(" ORDER BY DTAEXT_ID");
				//System.out.println("consultarDataExtendidaUI: "+sb.toString());
				dataSet = db.get(dataSource, sb.toString());
		
			}
			
			
			/**
			 * @param idUnidad
			 * @throws Exception
			 * @author NM26659
			 * Metodo de busqueda de los vehiculos que tomaron las ordenes asociadas a una Unidad de Inversion  
			 */
			//Agregado en Requerimiento TTS_443 nm26659_11/04/2014
			public void listarVehuculosPorIdUnidad(long idUnidad)throws Exception{
				StringBuffer sql=new StringBuffer();
				sql.append("SELECT DISTINCT ORD.ORDENE_VEH_TOM FROM infi_tb_204_ordenes ord, infi_tb_106_unidad_inversion ui ");
				sql.append(" WHERE ord.uniinv_id = ui.undinv_id ");
				sql.append(" AND UI.UNDINV_ID=").append(idUnidad);
				sql.append(" and ord.ordene_veh_tom is not null");
				System.out.println("listarVehuculosPorIdUnidad ---> " + sql.toString());
				dataSet = db.get(dataSource, sql.toString());
			}
			
			//Metodo Agregado en requerimiento TTS_443 NM26659_07/04
			public int listarOrdenesPorUnidadInvStatusOrden(long idUnidadInv,String... status) throws Exception{

				int arg=0;
				StringBuffer sql=new StringBuffer();
				
				sql.append("SELECT ORD.ORDENE_ID,ORD.ORDENE_PED_MONTO AS ORDENE_PED_MONTO,ORD.CTECTA_NUMERO,ORD.TIPO_PRODUCTO_ID, ");
				sql.append("ORD.ORDENE_COMISION_OPERACION,ORD.ORDSTA_ID,ORDENE_PED_TOTAL,ORD.ORDENE_PED_COMISIONES,ORD.ORDENE_PED_FE_VALOR, ");
				sql.append("ORD.UNIINV_ID, ORD.ORDENE_PED_FE_ORDEN, ORD.CLIENT_ID,CONCEPTO_ID,CTA_ABONO ");			
				sql.append("FROM INFI_TB_204_ORDENES ORD ");
				sql.append("WHERE " );
				
				if(idUnidadInv>0){
					++arg;
					sql.append("ORD.UNIINV_ID =").append(idUnidadInv).append(" ");
				}
				
				
				
				if(status.length>0 && status[0]!=null){
					if(arg>0){
						sql.append(" AND ");	
					}
					sql.append(" ORD.ORDSTA_ID IN (");
					int count=0;
					for (String string : status) {
						if(count>0){
							sql.append(",");
						}
						sql.append("'"+string+"'");
						++count;
					}
						sql.append(") ");
				}
							
				//sql.append(status);
				//sql.append("'");
				
				//System.out.println("BUSQUEDA DE ORDENES: " + sql.toString());
				dataSet = db.get(dataSource, sql.toString());
				return dataSet.count();
				
			}

			/**
			 * Metodo de busqueda de informacion de la orden por id de Orden 
			 * */
			//Metodo creado en requerimiento TTS-423 NM26659
			//Metodo modificado en requerimiento TTS-423 NM29643
			public void listarDetalleDeOrdenPorId(long idOrden)throws Exception{			
				StringBuffer sql=new StringBuffer();
				
				sql.append("SELECT ord.ordene_id, ord.ORDENE_PED_FE_ORDEN, ord.ordsta_id, ord.TRANSA_ID, ord.UNIINV_ID, ord.CLIENT_ID, ord.TIPO_PRODUCTO_ID, ord.ORDENE_PED_MONTO, NVL (ord.ordene_adj_monto,0) AS ordene_adj_monto, ord.ORDENE_TASA_POOL, ord.ORDENE_TASA_CAMBIO, ");
				sql.append("ctes.tipper_id, ctes.client_cedrif, ctes.client_nombre, ctes.CLIENT_CORREO_ELECTRONICO, ");
				sql.append("ui.undinv_nombre, os.ORDSTA_NOMBRE, tr.TRANSA_DESCRIPCION ");
				sql.append("FROM infi_tb_204_ordenes ord, infi_tb_201_ctes ctes, infi_tb_106_unidad_inversion ui, INFI_TB_203_ORDENES_STATUS os, INFI_TB_012_TRANSACCIONES tr ");
				sql.append("WHERE ord.client_id = ctes.client_id AND ui.undinv_id = ord.uniinv_id AND ord.ordsta_id = os.ORDSTA_ID AND ord.TRANSA_ID = tr.TRANSA_ID AND ord.ordene_id =").append(idOrden);
				//System.out.println("listarDetalleDeOrdenPorId ----> " + sql.toString());
				dataSet = db.get(dataSource, sql.toString());
			}
			
			/**
			 * Metodo de busqueda de informacion de la orden por id de Orden 
			 * */
			//Metodo creado en requerimiento TTS-423 NM26659
			//Metodo modificado en requerimiento TTS-423 NM29643
			//Metodo modificado en mejora de performance en procesos masivos  NM26659 - 28/01/2014
			//Metodo modificado en requerimiento TTS-466 NM29643
			public void listarOrdenEnvioCorreos(String ejecucionId,String... statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct)throws Exception{			
				StringBuffer sql=new StringBuffer();
				sql.append("SELECT ");
				if(statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct.length>=10 && statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[9]!=null && statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[9].equals("1")){
					sql.append("DISTINCT(ord.ordene_id), ");
				}else{
					sql.append("ord.ordene_id, ");
				}
				sql.append("ord.ORDENE_PED_FE_ORDEN, ord.ordsta_id, ord.TRANSA_ID, ord.UNIINV_ID, ord.CLIENT_ID, ord.TIPO_PRODUCTO_ID, ord.ORDENE_PED_MONTO, NVL (ord.ordene_adj_monto,0) AS ordene_adj_monto, ord.ORDENE_TASA_POOL, ord.ORDENE_TASA_CAMBIO, ");
				sql.append("ctes.tipper_id, ctes.client_cedrif, ctes.client_nombre, ctes.CLIENT_CORREO_ELECTRONICO, ");
				sql.append("ui.undinv_nombre ");
				sql.append("FROM infi_tb_204_ordenes ord, infi_tb_201_ctes ctes, infi_tb_106_unidad_inversion ui");
				if( statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct!=null && ( (statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct.length>=6 && statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[5]!=null) || (statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct.length>=7 && statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[6]!=null) 
						 || (statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct.length>=8&&statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[7]!=null) ) )
					sql.append(", INFI_TB_229_ORDENES_CRUCES oc");
				sql.append(" WHERE ord.client_id = ctes.client_id AND ui.undinv_id = ord.uniinv_id ");
				if(statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct!=null && ( (statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct.length>6 && statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[5]!=null) || (statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct.length>7 && statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[6]!=null) ) )
					sql.append(" AND TO_NUMBER(oc.ORDENE_ID)=ord.ORDENE_ID ");
				if(ejecucionId!=null && !ejecucionId.equals("")){
					if(statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct!=null && statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct.length>=6 && statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[5]!=null && statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[5].equals(String.valueOf(ConstantesGenerales.FALSO))){
						sql.append(" AND ORD.ORDENE_EJEC_ID=").append(ejecucionId);
					}else{
						sql.append(" AND ORD.EJECUCION_ID=").append(ejecucionId);
					}
				}
				if(statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct!=null && statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct.length>0){
					if(statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct.length>=1 && statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[0]!=null) sql.append(" AND ord.ordsta_id = '").append(statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[0]).append("'");
					if(statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct.length>=2 && statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[1]!=null) sql.append(" AND ord.TRANSA_ID = '").append(statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[1]).append("'");
					if(statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct.length>=3 && statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[2]!=null) sql.append(" AND ord.UNIINV_ID = ").append(statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[2]);
					if(statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct.length>=4 && statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[3]!=null) sql.append(" AND ord.ordene_id IN (").append(statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[3]).append(")");
					if(statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct.length>=5 && statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[4]!=null) sql.append(" AND ord.TIPO_PRODUCTO_ID IN ('").append(statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[4]).append("')");
					boolean cruce=false;
					if(statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct.length>=6 && statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[5]!=null) {
						sql.append(" AND oc.INDICADOR_TITULO='").append(statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[5]).append("'");
						cruce=true;
					}
					if(statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct.length>=7 && statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[6]!=null){
						sql.append(" AND oc.ID_CRUCE IN (").append(statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[6]).append(")");
						cruce=true;
					}
					if(statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct.length>=8 && statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[7]!=null){
						sql.append(" AND oc.CRUCE_PROCESADO=").append(statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[7]);
						cruce=true;
					}
					if(cruce) sql.append(" AND oc.UNDINV_ID = ui.UNDINV_ID");
//					if(statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct.length>=9 && statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[8]!=null) sql.append(" AND (SELECT ORDSTA_ID FROM INFI_TB_204_ORDENES WHERE ORDENE_ID_RELACION=ord.ordene_id AND TRANSA_ID='").append(TransaccionNegocio.ORDEN_PAGO).append("')='").append(statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[8]).append("'");
					//TODO Transaccion es PACTO_RECOMPRA u ORDEN_PAGO?????
					if(statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct.length>=9 && statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[8]!=null) sql.append(" AND EXISTS (SELECT ORDSTA_ID FROM INFI_TB_204_ORDENES WHERE ORDENE_ID_RELACION=ord.ordene_id AND TRANSA_ID='").append(TransaccionNegocio.ORDEN_PAGO).append("' AND ordsta_id='").append(statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[8]).append("')");
//					if(statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct.length>=9 && statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[8]!=null) sql.append(" AND (SELECT ORDSTA_ID FROM INFI_TB_204_ORDENES WHERE ORDENE_ID=ord.ORDENE_ID_RELACION AND TRANSA_ID='").append(TransaccionNegocio.ORDEN_PAGO).append("')='").append(statusTransaUIOrdenesProdTitCrucesCruzadoLiquidadoDistinct[8]).append("'");
				}
				logger.debug("listarOrdenEnvioCorreos ----> " + sql.toString());
				dataSet = db.get(dataSource, sql.toString());
			}
			
			
			/**
			 * Metodo de busqueda de informacion de la orden por idEjecucion y Unidad de Inversion 
			 * */
			//Metodo creado en requerimiento TTS-466 NM29643
			public void listarOrdenByEjecucionIdUI(String ejecucionId, String uniInvId)throws Exception{			
				StringBuffer sql=new StringBuffer();
				
				sql.append("SELECT ord.ordene_id, ord.ORDENE_PED_FE_ORDEN, ord.ordsta_id, ord.TRANSA_ID, ord.UNIINV_ID, ord.CLIENT_ID, ord.TIPO_PRODUCTO_ID, ord.ORDENE_PED_MONTO, NVL (ord.ordene_adj_monto,0) AS ordene_adj_monto, ord.ORDENE_TASA_POOL, ord.ORDENE_TASA_CAMBIO, ");
				sql.append("ctes.tipper_id, ctes.client_cedrif, ctes.client_nombre, ctes.CLIENT_CORREO_ELECTRONICO, ui.undinv_nombre ");
				sql.append("FROM infi_tb_204_ordenes ord, infi_tb_201_ctes ctes, infi_tb_106_unidad_inversion ui ");
				sql.append("WHERE ord.client_id = ctes.client_id AND ui.undinv_id = ord.uniinv_id ");
				if(ejecucionId!=null){
					sql.append(" AND ord.EJECUCION_ID=").append(ejecucionId);
				}
				if(uniInvId!=null){
					sql.append(" AND ui.undinv_id=").append(uniInvId);
				}
				logger.debug("listarOrdenByEjecucionIdUI ----> " + sql.toString());
				dataSet = db.get(dataSource, sql.toString());
			}
			
			
			public void resumenMontoAjudicadoPorUnidadInversion(long unidadInv, String estatusOrden) throws Exception{
				StringBuffer sql=new StringBuffer();
				sql.append("select count(ordene_id) cantidad, sum(ord.ORDENE_ADJ_MONTO) ordene_adj_monto , '"+estatusOrden+"' estatus_orden ");
				sql.append(" from infi_tb_204_ordenes ord");
				sql.append(" where ord.UNIINV_ID=").append(unidadInv);
				sql.append(" and ord.ORDSTA_ID='").append(estatusOrden).append("'");		
				System.out.println("resumenMontoAjudicadoPorUnidadInversion: "+sql.toString());
				dataSet = db.get(dataSource, sql.toString());
				System.out.println("resumenMontoAjudicadoPorUnidadInversion: "+dataSource);
				System.out.println(sql);
			}
			
			
			/**
			 * Metodo de busqueda de Ordenes duplicadas asociadas a una Unidad de Inversion 
			 * */
			//Metodo creado en requerimiento TTS-466_Calidad NM26659 (Solucion temporal de problema Duplicidad de ordenes en Exportacion de Ordenes)
			public boolean ordenesduplicadasSICAD2Personal(String uniInvId)throws Exception{			
				boolean flag=false;
				String[]idCliente=null;
				String[]idOrdenes=null;
				StringBuffer sql=new StringBuffer();
				
				//Busqueda de ID de clientes con con mas de una solicitud 
				sql.append("SELECT   ord.client_id, COUNT (ord.client_id) FROM infi_tb_204_ordenes ord ");								
				sql.append("WHERE ORD.TRANSA_ID='").append(TransaccionNegocio.TOMA_DE_ORDEN).append("' ");  
				sql.append(" AND ord.ordsta_id not in('").append(StatusOrden.CANCELADA).append("','").append(StatusOrden.PENDIENTE).append("') ");
				sql.append(" AND ord.uniinv_id=").append(uniInvId).append(" ");
				sql.append("GROUP BY ord.client_id HAVING COUNT (ord.client_id) > 1 ");				
				
				//System.out.println(" Busqueda de Clientes con Ordenes Duplicadas ---> " + sql.toString());
				dataSet = db.get(dataSource, sql.toString());				
				if(dataSet.count()>0){
					dataSet.first();
					idCliente=new String[dataSet.count()];
					int count=0;
					while(dataSet.next()){
						idCliente[count++]=dataSet.getValue("client_id");						
					}
										
					//Busqueda de Id de Ordenes basados en Unidad de Inversion y id Clientes
					sql=new StringBuffer();					
					sql.append("SELECT ORDENE_ID FROM INFI_TB_204_ORDENES ORD WHERE ORD.UNIINV_ID=").append(uniInvId).append(" ");
					sql.append("AND ORD.CLIENT_ID IN (");					
						if(idCliente.length>0){
							count=0;
							for (String cliente : idCliente) {
								if(count>0){
									sql.append(",");
								}
								sql.append(cliente);
								
								++count;
							}							
						}
						sql.append(") ");
									
						//System.out.println(" Busqueda de Id Ordenes por Unidad de Inversion y Id de Clientes ---> " + sql.toString());
						dataSet = db.get(dataSource, sql.toString());
						
						if(dataSet.count()>0){
							count=0;
							idOrdenes=new String[dataSet.count()];
							while(dataSet.next()){
								idOrdenes[count++]=dataSet.getValue("ORDENE_ID");						
							}
						}
						
						sql=new StringBuffer();	
						sql.append("SELECT ordene_id FROM infi_tb_204_ordenes ord WHERE ord.ordene_id IN (");						
						//if(idOrdenes.length>0){							
								count=0;
								for (String ordenes : idOrdenes) {
									if(count>0){
										sql.append(",");
									}
									sql.append(ordenes);
									
									++count;
								}							
						//	}
							sql.append(") ");
							
							sql.append(" AND ord.ordene_id not in (SELECT numero_orden_infi FROM solicitudes_sitme ss WHERE ss.numero_orden_infi IN (");
							count=0;
							for (String ordenes : idOrdenes) {
								if(count>0){
									sql.append(",");
								}
								sql.append(ordenes);
								
								++count;
							}							
					//	}
						sql.append(")) ");
						
						//System.out.println(" Busqueda de las ordenes que se Deben Cancelar ---->  " + sql.toString());
						//}
						dataSet = db.get(dataSource, sql.toString());
						
						//NM26659_12/02/2015 Cambio para el manejo de producto SIMADI
						if(dataSet.count()>0){
							flag=true;	
						}
						
				}
			
				
				return flag;
			}
			
			//CT20315 - 15-02-2015 TTS_491_CONTINGENCIA SIMADI
			//Modificación 23/03/2015 NM25287 ITS-2531 Reporte Solicitudes SIMADI no muestra filtro de Ofertas y Demandas
			/**
			 * Método que permite consultar las solicitudes de OFERTA y DEMANDA del SIMADI filtradas por fecha 
			 * @param 
			 * @throws Exception
			 * @author CT20315
			 * */
			public String listarSolicitudesSimadi(String fechaDesde, String fechaHasta, boolean generarDataSet, boolean paginado, int paginaAMostrar, int registroPorPagina, String tipo_producto_id, String tipo_negocio) throws Exception {
				StringBuffer sql = new StringBuffer();
				StringBuffer filtro = new StringBuffer("");
				sql.append("SELECT u.UNDINV_NOMBRE,id_orden, numero_orden_infi, nombre_cliente, ced_rif_cliente, email_cliente," +
							"monto_solicitado,DECODE (id_producto, 6, monto_adjudicado||'', 'NO APLICA') AS monto_adjudicado, monto_comision, porc_comision, tasa_cambio, "+
							"cuenta_bsf_o as cuenta_bs, cta_numero as cuenta_en_dolares, estatus, fecha_registro, "+
							"hora_registro, fecha_tramite, num_bloqueo, cta_abono, "+
							"cta_abono_moneda, nombre_subasta, id_producto, "+
							"DECODE (id_producto, 6, 'DEMANDA', 'OFERTA') as tipo_solicitud ");
				sql.append(" FROM INFI_TB_106_UNIDAD_INVERSION u,solicitudes_sitme ss ");
				
				if(fechaHasta!=null && fechaDesde!=null){
					filtro.append("WHERE ss.FECHA_REGISTRO between to_date('").append(fechaDesde).append("','" + ConstantesGenerales.FORMATO_FECHA + "') and to_date('").append(fechaHasta).append("','" + ConstantesGenerales.FORMATO_FECHA + "')");
					sql.append(filtro);
				}
				
				if((tipo_producto_id != null) && (tipo_producto_id != "")){
					sql.append(" AND ss.id_producto = ").append(tipo_producto_id);				
				}
				
				if((tipo_negocio != null) && (tipo_negocio != "")){
					sql.append(" AND u.TIPO_NEGOCIO=").append(tipo_negocio);				
				}
				
				sql.append(" AND u.UNDINV_ID=ss.UNDINV_ID");
				
				//sql.append(" ORDER BY infi_tb_204_ordenes.ordene_id desc");
				//Si no debe crear el dataSet consulta el total de registros
				if (!generarDataSet){
					getTotalDeRegistros(sql.toString());
				}else{
					if (paginado && paginaAMostrar>0 && registroPorPagina> 0){			
						dataSet = obtenerDataSetPaginado(sql.toString(),paginaAMostrar,registroPorPagina);
					}else{
						dataSet = db.get(dataSource, sql.toString());
					}	
				}
				System.out.println("listarSolicitudesSimadi: "+sql);
				return sql.toString();				
			}
				
			/**
			 * Valida si la unidad de inversión debe controlar el disponible de divisas y si hay disponible para permitir la toma de orden
			 * @param unidadInv
			 * @param porcentajeTolerancia
			 * @param montoSolicitado
			 * @return
			 * @throws Exception
			 */
			public boolean validarMontoDisponibleSIMADI(long unidadInv, double porcentajeTolerancia, BigDecimal montoSolicitado) throws Exception{
				StringBuffer sql=new StringBuffer();
				StringBuffer sql1=new StringBuffer();
				boolean result=true;
				
				sql.append("SELECT 1 CONTROL_ACTIVO FROM INFI_TB_108_UI_TITULOS UT WHERE UT.UITITU_IN_CONTROL_DISPONIBLE=1 AND UT.UNDINV_ID=").append(unidadInv);
				//System.out.println("validarMontoDisponibleSIMADI - 1: "+sql.toString());
				dataSet = db.get(dataSource, sql.toString());
				
				if(dataSet.count()>0){
				
					sql1.append("SELECT 1 VALIDO FROM INFI_TB_108_UI_TITULOS UIT,INFI_TB_106_UNIDAD_INVERSION UI ");
					sql1.append(" WHERE (UIT.UITITU_VALOR_EQUIVALENTE-(UIT.UITITU_VALOR_EQUIVALENTE*"+porcentajeTolerancia+"))>=UI.MONTO_ACUMULADO_SOLIC+"+montoSolicitado);
					sql1.append(" AND UI.UNDINV_ID=UIT.UNDINV_ID");
					sql1.append(" AND UI.UNDINV_ID="+unidadInv);
				
					//System.out.println("validarMontoDisponibleSIMADI - 2: "+sql1.toString());
					dataSet = db.get(dataSource, sql1.toString());
					
					if(dataSet.count()==0){
						 result=false;
					}					
				}
				return result;
			}
			
			/**
			 * Actualiza el monto acumulado de tomas de ordenes en la unidad de inversion para control de disponibilidad
			 * @param unidadInv
			 * @param montoSolicitado
			 * @throws Exception
			 */
			public synchronized void sumarMontoAcumuladoSIMADI (long unidadInv, BigDecimal montoSolicitado) throws Exception{
				StringBuffer sql=new StringBuffer();
				sql.append("UPDATE INFI_TB_106_UNIDAD_INVERSION UI SET UI.MONTO_ACUMULADO_SOLIC=UI.MONTO_ACUMULADO_SOLIC+"+montoSolicitado+" WHERE UNDINV_ID=").append(unidadInv);
					
				db.exec(dataSource, sql.toString());
				//System.out.println("sumarMontoAcumuladoSIMADI: "+sql.toString());
			}
			
			/**
			 *  Consulta de cantidad de ordenes asociadas a la unidad de inversion
			 * @param uniInvId
			 * @return
			 * @throws Exception
			 */
			public int cantidadOrdenesUnidadInversion(int uniInvId)throws Exception{	
				StringBuffer sql=new StringBuffer();
				int cantidadOrdenes=0;
				sql.append("select count(ordene_id) CANT from INFI_TB_204_ORDENES WHERE UNIINV_ID=").append(uniInvId);
				dataSet = db.get(dataSource, sql.toString());
				
				if(dataSet.count()>0){
					dataSet.first();
					
					if (dataSet.next()) {
						cantidadOrdenes = Integer.parseInt(dataSet.getValue("CANT"));
					}
				}
				return cantidadOrdenes;
			}
			
			/**
			 * Metodo de actualizacion de estatus de verificacion BCV (ORDENE_ESTATUS_BCV) en la orden 
			 * */
			//TTS_491 Web Service BCV NM26659_13/03/2015
			public String actualizarEstatusOrdenBcvIn(String idsOrdenes,String estatusOrd){
				StringBuffer sql = new StringBuffer();

				sql.append(" UPDATE INFI_TB_204_ORDENES SET ORDENE_ESTATUS_BCV="+estatusOrd+" ");
				sql.append(" WHERE ORDENE_ID IN ("+idsOrdenes+")"); 

				return sql.toString(); 

				}
			/**
			 * Metodo de actualizacion de estatus de verificacion BCV (ORDENE_ESTATUS_BCV) en la orden 
			 * */
			//TTS_491 Web Service BCV NM26659_20/03/2015 //Modificacion de estatus ORDENE_ESTATUS_BCV al eliminar los cruces
			public void actualizarEstatusOrdenBcvIn(long idUnidad, String status, long idCliente, String idOrden, String statusOrden, String idEjecucion, String indTitulo)throws Exception {
				StringBuffer sql = new StringBuffer();

				sql.append(" UPDATE INFI_TB_204_ORDENES ORD SET ORDENE_ESTATUS_BCV=").append(ConstantesGenerales.SIN_VERIFICAR).append("WHERE ORDENE_ESTATUS_BCV IN (").append(ConstantesGenerales.VERIFCADA_MANUAL_APROBADA).append(",").append(ConstantesGenerales.VERIFIDA_MANUAL_RECHAZADA).append(")" );
				sql.append(" AND ORD.ORDENE_ID IN (");
				
				sql.append("SELECT ORDENE_ID FROM INFI_TB_229_ORDENES_CRUCES OC WHERE OC.CRUCE_PROCESADO = 0 ");
				
				if(idUnidad>0){
					sql.append("AND OC.Undinv_Id = '").append(idUnidad).append("' ");
				}
				
				if(idCliente>0){
					sql.append("AND OC.CLIENT_ID='").append(idCliente).append("' ");
				}
				
				if(idOrden!=null && !idOrden.equals("")){
					sql.append("AND OC.ORDENE_ID IN ('").append(idOrden.replace(",","','")).append("') ");
				}
				
				if(idEjecucion!=null && !idEjecucion.equals("")){
					sql.append("AND OC.ID_EJECUCION IN (").append(idEjecucion).append(") ");
				}

				if(status!=null && !status.equals("")){
					sql.append("AND OC.ESTATUS='").append(status).append("' ");
				}
				
				if(indTitulo!=null && !indTitulo.equals("")){
					sql.append("AND OC.INDICADOR_TITULO='").append(indTitulo).append("' ");
				}
			
				sql.append(") ");
				 

				db.exec(dataSource,sql.toString());

				}
			
			public void actualizarEstatusOrdenBcvIn(String cruces)throws Exception {
				StringBuffer sql = new StringBuffer();
				sql.append(" UPDATE INFI_TB_204_ORDENES SET ORDENE_ESTATUS_BCV=0 WHERE ORDENE_ESTATUS_BCV IN (").append(ConstantesGenerales.VERIFCADA_MANUAL_APROBADA).append(",").append(ConstantesGenerales.VERIFIDA_MANUAL_RECHAZADA).append(")" );
				sql.append(" AND ORDENE_ID IN (");				
				sql.append("SELECT ORDENE_ID from INFI_TB_229_ORDENES_CRUCES where ID_CRUCE IN ('"+cruces.replace(",", "','")+"') AND CRUCE_PROCESADO = 0");
				sql.append(")");			
				db.exec(dataSource,sql.toString());
			}
			
			/**
			 * Metodo para bsuqueda de ordenes de SIMADI Venta en Aereopuerto 
			 * @param tipoNegocio
			 * @param incluirCliente
			 * @param clienteID
			 * @param tasaMinima
			 * @param tasaMaxima
			 * @param montoMinimo
			 * @param montoMaximo
			 * @param totales
			 * @param idUnidadInversion
			 * @param paginado
			 * @param paginaAMostrar
			 * @param registroPorPagina
			 * @param todos
			 * @param incluir
			 * @param ordenesSeleccionadas
			 * @param ordeneEstatusBCV
			 * @param fecha
			 * @param ordeneEstatusINFI
			 * @param anulacionEfectivo NM25287 TTS-504 Modificación para usar en anulacion de operaciones de taquilla
			 * @throws Exception
			 * @author NM26659
			 */
			//NM26659 - 26/08/2015 Metodo creado en requerimiento SIMADI Venta en Taquilla de AereoPuerto
			public void listarOrdenesPorEnviarBCVPorVentaTaquilla(String tipoNegocio, Integer incluirCliente, Integer clienteID,Integer tasaMinima, Integer tasaMaxima, Integer montoMinimo, Integer montoMaximo,boolean totales, long idUnidadInversion,boolean paginado, int paginaAMostrar, int registroPorPagina, boolean todos, boolean incluir, String ordenesSeleccionadas, String ordeneEstatusBCV, String fecha, String ordeneEstatusINFI, boolean anulacionEfectivo) throws Exception{
				//anulacionEfectivo=false;
				StringBuffer sqlElectronico=new StringBuffer();
				StringBuffer sqlEfectivo=new StringBuffer();
				StringBuffer sql=new StringBuffer();				
				sql.append(" UNION ");
				
				StringBuffer sqlFinal=new StringBuffer();
								
				String condicionCliente = "";
				sqlEfectivo.append("SELECT TO_CHAR(OT.ORDENE_ID) AS ORDENE_ID, OT.ORDENE_TAQ_CLIENT_CERIF as client_cedrif,OT.ORDENE_TAQ_TIPPER_ID as tipper_id, OT.ORDENE_TAQ_CLIENT_NOMBRE as client_nombre, ");
				sqlEfectivo.append("OT.ORDENE_TAQ_CORREO_CLIENT as client_correo_electronico, OT.ORDENE_TAQ_TEL_CLIENT as client_telefono,OT.ORDENE_TAQ_TASA_CAMBIO as tasa_cambio, OT.ORDENE_TAQ_MONTO_CAP_DIVISA as monto_solicitado, ");
				sqlEfectivo.append("ui.undinv_nombre, ui.nro_jornada,(NVL (ORDENE_TAQ_MONTO_CAP_DIVISA, 0)) as monto_adj, ");
				sqlEfectivo.append("CASE OT.ORDENE_TAQ_ESTATUS_BCV WHEN 0 THEN 'Sin Verificar' WHEN 1 THEN 'Verificada Aprobada BCV' WHEN 2 THEN 'Verificada Rechazada BCV' WHEN 3 THEN 'Verificada Aprobada Manual' WHEN 4 THEN 'Verificada Rechazada Manual' END estatus, ");
				sqlEfectivo.append("NULL AS cta_numero,OT.ORDENE_TAQ_OBSERVACION as observacion,ot.ORDENE_TAQ_ORDENE_ID_BCV as ORDENE_ID_BCV,'EFECTIVO' as tipo_operacion ");
				sqlEfectivo.append("FROM INFI_TB_232_ORDENES_TAQUILLA OT,infi_tb_106_unidad_inversion ui,infi_tb_101_inst_financieros inst,infi_tb_038_inst_forma_orden fo WHERE ");
				sqlEfectivo.append("ot.ordene_taq_undinv_id = ui.undinv_id ");
				sqlEfectivo.append("AND fo.insfin_forma_orden = inst.insfin_forma_orden ");
				sqlEfectivo.append("AND fo.INSFIN_FORMA_ORDEN='").append(ConstantesGenerales.INST_FINANCIERO_SIMADI_TAQUILLA).append("' ");
				sqlEfectivo.append("AND ui.tipo_negocio =").append(tipoNegocio).append(" ");
				sqlEfectivo.append("AND TO_DATE(TO_CHAR(ot.ordene_taq_fe_registro,'").append(ConstantesGenerales.FORMATO_FECHA2).append("'),'").append(ConstantesGenerales.FORMATO_FECHA2).append("') = TO_DATE ('").append(fecha).append("','").append(ConstantesGenerales.FORMATO_FECHA2).append("') ");
				
				if(montoMinimo!=null && montoMinimo!=0) {
					sqlEfectivo.append("AND  ot.ordene_taq_monto_cap_divisa>=").append(montoMinimo).append(" ");
				
				}
				
				 if(montoMaximo!=null && montoMaximo!=0){
					 sqlEfectivo.append("AND  ot.ordene_taq_monto_cap_divisa<=").append(montoMaximo).append(" ");
				}
				 
				 if(tasaMinima!=null && tasaMinima!=0) {
					 sqlEfectivo.append("AND  ot.ordene_taq_tasa_cambio>=").append(tasaMinima).append(" ");
					
					}
					
					 if(tasaMaxima!=null && tasaMaxima!=0){
						 sqlEfectivo.append("AND  ot.ordene_taq_tasa_cambio<=").append(tasaMaxima).append(" ");
					}
					 
					 if(clienteID!=null && clienteID!=0){							
						 if(incluirCliente.equals(1)){ //SE INCLUYE AL CLIENTE								
							 condicionCliente = " AND ot.ORDENE_TAQ_CLIENT_ID IN ("+clienteID+")";							
						 }else { //SE EXCLUYE AL CLIENTE								
							 condicionCliente = " AND ot.ORDENE_TAQ_CLIENT_ID NOT IN ("+clienteID+")";													
						 }												 
						 //sqlElectronico.append("AND ctes.client_id =").append(clienteID).append(" ");						
						 sqlEfectivo.append(condicionCliente.toString());										 
					 }
					 
					 if(idUnidadInversion!=0){
						 sqlEfectivo.append("AND ot.ORDENE_TAQ_UNDINV_ID =").append(idUnidadInversion).append(" ");
					 }
					 
					//NM25287 15/09/2015 Se filtran las operaciones de efectivo anuladas
					 if(anulacionEfectivo){
						 sqlEfectivo.append("AND ot.ordene_taq_estatus = ").append(ConstantesGenerales.STATUS_TAQ_RESERVADA_X_TF).append(" "); 
						// sqlEfectivo.append("AND (ot.ordene_taq_ordene_id_bcv is not null AND ot.ordene_taq_ordene_id_bcv<>0) "); 
						
					 }else{
						 sqlEfectivo.append("AND ot.ordene_taq_estatus <> ").append(ConstantesGenerales.STATUS_TAQ_RESERVADA_X_TF).append(" "); 
					 }
					 if(ordeneEstatusBCV!=null){
						 sqlEfectivo.append("AND ot.ordene_taq_estatus_bcv= ").append(ordeneEstatusBCV).append(" ");
					 }
					 
					 if(!todos){
							if(incluir){
								sqlEfectivo.append(" AND ot.ordene_id in("+ordenesSeleccionadas+")");
						    } else {
						    	sqlEfectivo.append(" AND ot.ordene_id not in("+ordenesSeleccionadas+") ");
						    }
						}
				sqlFinal.append(sqlEfectivo.toString());	
				
				if(!anulacionEfectivo){
					sqlElectronico.append("SELECT TO_CHAR(O.ORDENE_ID) AS ORDENE_ID, ctes.client_cedrif as client_cedrif,ctes.tipper_id as tipper_id, ctes.client_nombre as client_nombre, ctes.client_correo_electronico as client_correo_electronico, ctes.client_telefono as client_telefono,");
					sqlElectronico.append("o.ordene_tasa_pool as tasa_cambio, o.ordene_ped_monto as monto_solicitado, ui.undinv_nombre, ui.nro_jornada, NVL (o.ordene_adj_monto, 0) as  monto_adj, ");
					sqlElectronico.append("CASE o.ordene_estatus_bcv WHEN 0 THEN 'Sin Verificar' WHEN 1 THEN 'Verificada Aprobada BCV' WHEN 2 THEN 'Verificada Rechazada BCV' WHEN 3 THEN 'Verificada Aprobada Manual' WHEN 4 THEN 'Verificada Rechazada Manual' END estatus, ");
					sqlElectronico.append("(SELECT cc.ctecta_numero FROM infi_tb_217_ctes_cuentas_ord cco, infi_tb_202_ctes_cuentas cc WHERE cc.ctes_cuentas_id = cco.ctes_cuentas_id AND cco.ordene_id = o.ordene_id) AS cta_numero, ");
					sqlElectronico.append("o.ordene_observacion AS observacion,o.ORDENE_ID_BCV as ORDENE_ID_BCV,'ELECTRONICO' as tipo_operacion ");				
					sqlElectronico.append("FROM infi_tb_201_ctes ctes,infi_tb_204_ordenes o, infi_tb_106_unidad_inversion ui, infi_tb_101_inst_financieros inst, infi_tb_038_inst_forma_orden fo ");
					sqlElectronico.append("WHERE o.uniinv_id = ui.undinv_id ");
					sqlElectronico.append("AND ui.tipo_negocio =").append(tipoNegocio).append(" ");
					sqlElectronico.append("AND ctes.client_id = o.client_id ");
					sqlElectronico.append("AND TO_DATE(TO_CHAR(o.ordene_ped_fe_orden,'").append(ConstantesGenerales.FORMATO_FECHA2).append("'),'").append(ConstantesGenerales.FORMATO_FECHA2).append("') = TO_DATE ('").append(fecha).append("','").append(ConstantesGenerales.FORMATO_FECHA2).append("') ");
					sqlElectronico.append("AND INST.INSFIN_ID=UI.INSFIN_ID ");
					sqlElectronico.append("AND fo.INSFIN_FORMA_ORDEN=inst.INSFIN_FORMA_ORDEN ");
					sqlElectronico.append("AND fo.INSFIN_FORMA_ORDEN='").append(ConstantesGenerales.INST_FINANCIERO_SIMADI_TAQUILLA).append("' ");
					sqlElectronico.append("AND O.ORDSTA_ID='").append(ordeneEstatusINFI).append("' ");
					sqlElectronico.append("AND NVL(o.ordene_adj_monto,0)>0 ");
					if(montoMinimo!=null && montoMinimo!=0) {
						sqlElectronico.append("AND o.ORDENE_PED_MONTO>=").append(montoMinimo).append(" ");
					
					}
					
					 if(montoMaximo!=null && montoMaximo!=0){
						 sqlElectronico.append("AND o.ORDENE_PED_MONTO<=").append(montoMaximo).append(" ");
					}
				 
					 if(tasaMinima!=null && tasaMinima!=0) {
						sqlElectronico.append("AND o.ordene_tasa_pool>=").append(tasaMinima).append(" ");
					
					 }
					
					 if(tasaMaxima!=null && tasaMaxima!=0){
						 sqlElectronico.append("AND o.ordene_tasa_pool<=").append(tasaMaxima).append(" ");
					}
						
					if(clienteID!=null && clienteID!=0){
						if(incluirCliente.equals(1)){ //SE INCLUYE AL CLIENTE
							condicionCliente = " AND CTES.client_id IN ("+clienteID+")";
						}else { //SE EXCLUYE AL CLIENTE
							condicionCliente = " AND CTES.client_id NOT IN ("+clienteID+")";
						}
				
						 //sqlElectronico.append("AND ctes.client_id =").append(clienteID).append(" ");
						sqlElectronico.append(condicionCliente.toString());
					 }
					 
					if(idUnidadInversion!=0){
						 sqlElectronico.append("AND ui.undinv_id =").append(idUnidadInversion).append(" ");
					}
					 
					if(ordeneEstatusBCV!=null){
						 sqlElectronico.append("AND O.ORDENE_ESTATUS_BCV= ").append(ordeneEstatusBCV).append(" ");
					}
					 
					if(!todos){
						if(incluir){
					    	sqlElectronico.append(" AND o.ordene_id in("+ordenesSeleccionadas+")");
					    } else {
					    	sqlElectronico.append(" AND o.ordene_id not in("+ordenesSeleccionadas+")");
					    }
					}
						
					sqlFinal.append(sql.toString());
					sqlFinal.append(sqlElectronico.toString());	
				}				
				 
				System.out.println("listarOrdenesPorEnviarBCVPorVentaTaquilla ---> " + sqlFinal.toString());
				
				if (paginado && paginaAMostrar>0 && registroPorPagina> 0){			
					dataSet = obtenerDataSetPaginado(sqlFinal.toString(),paginaAMostrar,registroPorPagina);
				}else{						
					
					dataSet = db.get(dataSource, sqlFinal.toString());
				}		
			}
			
			/**
			 * Metodo para bsuqueda de ordenes de SIMADI Venta en Aereopuerto 
			 * @param tipoNegocio
			 * @param incluirCliente
			 * @param clienteID
			 * @param tasaMinima
			 * @param tasaMaxima
			 * @param montoMinimo
			 * @param montoMaximo
			 * @param totales
			 * @param idUnidadInversion
			 * @param paginado
			 * @param paginaAMostrar
			 * @param registroPorPagina
			 * @param todos
			 * @param incluir
			 * @param ordenesSeleccionadas
			 * @param ordeneEstatusBCV
			 * @param fecha
			 * @param ordeneEstatusINFI
			 * @throws Exception
			 * @author NM26659
			 */
			//NM26659 - 26/08/2015 Metodo creado en requerimiento SIMADI Venta en Taquilla de AereoPuerto
			public void listarOrdenesPorEnviarBCVPorVentaTaquilla(String tipoNegocio, long idUnidadInversion,String ordeneEstatusBCV,String ordeneEstatusINFI) throws Exception{
				
				StringBuffer sqlEfectivo=new StringBuffer();
				StringBuffer sqlElectronico=new StringBuffer();
				StringBuffer sql=new StringBuffer();			
				
				sql.append(" UNION ");
				
				StringBuffer sqlFinal=new StringBuffer();
								
				String condicionCliente = "";
				
				sqlEfectivo.append("SELECT TO_CHAR(OT.ORDENE_ID) AS ORDENE_ID,ot.ORDENE_TAQ_ESTATUS_BCV as ORDENE_ESTATUS_BCV,'EFECTIVO' as tipo_operacion ");
				sqlEfectivo.append("FROM INFI_TB_232_ORDENES_TAQUILLA OT,infi_tb_106_unidad_inversion ui,infi_tb_101_inst_financieros inst,infi_tb_038_inst_forma_orden fo WHERE ");
				sqlEfectivo.append("ot.ordene_taq_undinv_id = ui.undinv_id ");
				sqlEfectivo.append("AND fo.insfin_forma_orden = inst.insfin_forma_orden ");
				sqlEfectivo.append("AND fo.INSFIN_FORMA_ORDEN='").append(ConstantesGenerales.INST_FINANCIERO_SIMADI_TAQUILLA).append("' ");
				sqlEfectivo.append("AND ui.tipo_negocio =").append(tipoNegocio).append(" ");
																		 
				if(idUnidadInversion!=0){						 
					sqlEfectivo.append("AND ot.ORDENE_TAQ_UNDINV_ID =").append(idUnidadInversion).append(" ");					
				}
					 
				if(ordeneEstatusBCV!=null){
					sqlEfectivo.append("AND ot.ordene_taq_estatus_bcv= ").append(ordeneEstatusBCV).append(" ");	 
				}
				//NM25287 15/09/2015 Se filtran las operaciones de efectivo anuladas
				sqlEfectivo.append("AND ot.ordene_taq_estatus <> ").append(ConstantesGenerales.STATUS_TAQ_RESERVADA_X_TF).append(" "); 
				sqlElectronico.append("SELECT TO_CHAR(O.ORDENE_ID) AS ORDENE_ID, o.ORDENE_ESTATUS_BCV as ORDENE_ESTATUS_BCV,'ELECTRONICO' as tipo_operacion ");		
				sqlElectronico.append("FROM infi_tb_204_ordenes o, infi_tb_106_unidad_inversion ui, infi_tb_101_inst_financieros inst, infi_tb_038_inst_forma_orden fo ");
				//sqlElectronico.append("FROM infi_tb_201_ctes ctes,infi_tb_204_ordenes o, infi_tb_106_unidad_inversion ui, infi_tb_101_inst_financieros inst, infi_tb_038_inst_forma_orden fo "); NM25287 15/09/2015 Se elimina la tabla 201 ya que no se usa en el query
				sqlElectronico.append("WHERE o.uniinv_id = ui.undinv_id ");
				sqlElectronico.append("AND ui.tipo_negocio =").append(tipoNegocio).append(" ");				
				sqlElectronico.append("AND INST.INSFIN_ID=UI.INSFIN_ID ");
				sqlElectronico.append("AND fo.INSFIN_FORMA_ORDEN=inst.INSFIN_FORMA_ORDEN ");
				sqlElectronico.append("AND fo.INSFIN_FORMA_ORDEN='").append(ConstantesGenerales.INST_FINANCIERO_SIMADI_TAQUILLA).append("' ");
				sqlElectronico.append("AND O.ORDSTA_ID='").append(ordeneEstatusINFI).append("' ");
				sqlElectronico.append("AND NVL(o.ordene_adj_monto,0)>0 ");
				
				if(idUnidadInversion!=0){						 
					sqlElectronico.append("AND ui.undinv_id =").append(idUnidadInversion).append(" ");				
				}
					 					 
				if(ordeneEstatusBCV!=null){						 
					sqlElectronico.append("AND O.ORDENE_ESTATUS_BCV= ").append(ordeneEstatusBCV).append(" ");					
				}
																												
				sqlFinal.append(sqlEfectivo.toString());						
				sqlFinal.append(sql.toString());												
				sqlFinal.append(sqlElectronico.toString());
						
				System.out.println("listarOrdenesPorEnviarBCVPorVentaTaquilla Sobrecargado---> " + sqlFinal.toString());
	
				dataSet = db.get(dataSource, sqlFinal.toString());									
			}
			
			public void listarEstatusBCVPorUnidadInv()throws Exception{
				StringBuffer sql=new StringBuffer();
				
			}
			
			
			public void actualizarEstatusTaquilla(String ordeneID, String ordenBCV, String estatus, String observacion)throws Exception {
				StringBuffer sql=new StringBuffer();
					
					sql.append("UPDATE INFI_TB_232_ORDENES_TAQUILLA OT SET OT.ORDENE_TAQ_ESTATUS_BCV = ").append(estatus).append(" ");
					
					if(ordenBCV != null){
						sql.append(" ,  ORDENE_TAQ_ORDENE_ID_BCV = '").append(ordenBCV).append("'");
					}
					
					if(observacion != null){
						if(observacion.length() > 1000){
							observacion.substring(0, 999);
						}
						sql.append(" ,  ORDENE_TAQ_OBSERVACION = '").append(observacion).append("'");
					}
					
					sql.append(" WHERE OT.ORDENE_ID =").append(ordeneID);
					
					db.exec(dataSource, sql.toString());
					
			}
			//NM25287 TTS-504 Se sobreescribe método para usar en anulacion de operaciones de taquilla
			public void actualizarEstatusTaquilla(String ordeneID, String ordenBCV, String estatusBCV, String estatusTaq, String observacion)throws Exception {
				StringBuffer sql=new StringBuffer();
					
					sql.append("UPDATE INFI_TB_232_ORDENES_TAQUILLA OT SET OT.ORDENE_TAQ_ESTATUS = ").append(estatusTaq).append(" ");
					
					if(ordenBCV != null){
						sql.append(" ,  ORDENE_TAQ_ORDENE_ID_BCV = '").append(ordenBCV).append("'");
					}
					
					if(estatusBCV != null){
						sql.append(" ,  OT.ORDENE_TAQ_ESTATUS_BCV = '").append(estatusBCV).append("'");
					}
					
					if(observacion != null){
						if(observacion.length() > 1000){
							observacion.substring(0, 999);
						}
						sql.append(" ,  ORDENE_TAQ_OBSERVACION = '").append(observacion).append("'");
					}
					
					sql.append(" WHERE OT.ORDENE_ID =").append(ordeneID);
					
					db.exec(dataSource, sql.toString());
					
			}
			
			public DataSet listarSolicitudesDicom(long idUnidad, String idBlotter, String status, String fechaDesde, String fechaHasta, String tipoProductoId)throws Exception{

				StringBuffer sql=new StringBuffer();
				
				sql.append("SELECT ord.ordene_id, ui.undinv_nombre, ui.nro_jornada, ctes.client_nombre,ctes.client_cedrif, ord.ctecta_numero, ord.ordene_ped_monto, ");
				sql.append("ord.ordene_ped_comisiones, ord.ordene_ped_fe_orden,ord.ordene_ped_fe_valor,ORD.ORDENE_TASA_CAMBIO, ");
				sql.append(" (SELECT SUM (oo.monto_operacion) FROM infi_tb_207_ordenes_operacion oo WHERE oo.trnf_tipo = '").append(com.bdv.infi.logic.interfaces.TransaccionFinanciera.DEBITO).append("' AND OO.TRNFIN_ID=1 AND oo.ordene_id = ord.ordene_id) AS contravalor, ORDENE_PED_PORC_COMISION_IGTF AS PORC_COMISION_IGTF, ORDENE_PED_COMISION_IGTF AS MONTO_COMISION_IGTF ");
				sql.append("FROM infi_tb_106_unidad_inversion ui,infi_tb_201_ctes ctes,infi_tb_204_ordenes ord ");
				sql.append("WHERE ui.undinv_id = ord.uniinv_id AND ord.client_id = ctes.client_id AND ORD.TRANSA_ID='").append(TransaccionNegocio.TOMA_DE_ORDEN).append("' ");
				
				if(idUnidad!=0){
					sql.append(" AND ui.undinv_id = ").append(idUnidad).append(" ");
				}
				
				if(tipoProductoId!=null){
					sql.append(" AND ORD.TIPO_PRODUCTO_ID='").append(tipoProductoId).append("' ");
				}
				
				if(idBlotter!=null){
					sql.append(" AND ORD.BLOTER_ID = ").append(idBlotter).append(" ");
				}
				
				if(status!=null){
					sql.append(" AND ORD.ORDSTA_ID= '").append(status).append("' ");
				}
								
				if(fechaDesde!=null){
					sql.append(" AND ORD.ORDENE_PED_FE_ORDEN >=TO_DATE('").append(fechaDesde).append("','").append(ConstantesGenerales.FORMATO_FECHA2).append("') ");
				}
								
				if(fechaHasta!=null){
					sql.append(" AND ORD.ORDENE_PED_FE_ORDEN <=TO_DATE('").append(fechaHasta).append("','").append(ConstantesGenerales.FORMATO_FECHA2).append("') ");
				}
				System.out.println("listarSolicitudesDicom --> " + sql.toString());
				dataSet = db.get(dataSource, sql.toString()); 
				return dataSet;
			}
}


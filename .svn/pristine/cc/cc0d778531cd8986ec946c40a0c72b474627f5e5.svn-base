package com.bdv.infi.dao;

import static com.bancovenezuela.comun.logic.interfaces.ConstantesGenerales.STATUS_EN_ESPERA;
import static com.bancovenezuela.comun.logic.interfaces.ConstantesGenerales.STATUS_RECHAZADA;
import static com.bancovenezuela.comun.logic.interfaces.ConstantesGenerales.TRANSACCION_PACTO_RECOMPRA;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.*;

import javax.sql.DataSource;

import megasoft.DataSet;
import megasoft.db;

import com.bdv.infi.data.DataRegistro;
import com.bdv.infi.data.UnidadInversion;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TransaccionFinanciera;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;
import com.bdv.infi_services.beans.entidades.mensajes_peticion.ParametrosTomaDeOrden;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleTypes;
 import oracle.sql.*;
import static com.bancovenezuela.comun.logic.interfaces.ConstantesGenerales.*;
/**
 * Clase destinada para el manejo l&oacute;gico de inserci&oacute;n, recuperaci&oacute;n y listado de unidades de inversi&oacute;n almacenados en las tablas de trabajo
 */
public class UnidadInversionDAO extends com.bdv.infi.dao.GenericoDAO {

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
	public UnidadInversionDAO(DataSource ds) throws Exception {
		super(ds);
	}

	public UnidadInversionDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
	}

	/**
	 * Eliminar una unidad de inversion de la base de datos
	 * 
	 * @param idUnidadInversion
	 *            : identificador de la Unidad de Inversion
	 * @throws Exception
	 */
	public int eliminar(long idUnidadInversion) throws Exception {

		StringBuffer sqlSB = new StringBuffer("delete from INFI_TB_106_UNIDAD_INVERSION ");
		sqlSB.append("where undinv_id = ");
		sqlSB.append(idUnidadInversion + "");

		db.exec(dataSource, sqlSB.toString());
		return 0;
	}

	/**
	 * Ingresar una unidad de inversion en la base de datos
	 * 
	 * @param beanUI
	 *            : bean Unidad de Inversion con la data a registrar
	 * @throws Exception
	 */
	public int insertar(UnidadInversion beanUI) throws Exception {

		StringBuffer sqlAttr = new StringBuffer();
		StringBuffer sqlValue = new StringBuffer();

		beanUI.setIdUnidadInversion(Integer.parseInt(dbGetSequence(dataSource, "INFI_TB_106_UNIDAD_INVERSION")));

		sqlAttr.append("insert into INFI_TB_106_UNIDAD_INVERSION (");
		sqlAttr.append("undinv_id, insfin_id, tppeva_id, moneda_id, undinv_status, empres_id, ");
		sqlAttr.append("undinv_tasa_cambio, undinv_in_pedido_monto, undinv_multiplos, undinv_in_precio_sucio, undinv_in_vta_empleados, undinv_in_recompra_neteo, ");
		sqlAttr.append("undinv_fe_emision, undinv_fe_cierre, undinv_nombre, undinv_tasa_pool, undinv_rendimiento, undinv_mercado, ");
		sqlAttr.append("undinv_umi_inv_total, undinv_umi_inv_disponible, undinv_umi_unidad, in_cobro_batch_adj, in_cobro_batch_liq , dias_apertura_cuenta, INDC_PERMITE_CANCELACION,TIPO_NEGOCIO  ");

		sqlValue.append("values (").append(beanUI.getIdUnidadInversion()).append(", ");
		sqlValue.append("'").append(beanUI.getIdInstrumentoFinanciero()).append("', ");
		sqlValue.append("' ', ");
		sqlValue.append("'").append(beanUI.getIdMoneda()).append("', ");
		sqlValue.append("'").append(beanUI.getIdUIStatus()).append("', ");
		sqlValue.append("'").append(beanUI.getIdEmpresaEmisor()).append("', ");
		sqlValue.append(beanUI.getTasaCambio().toString()).append(", ");
		sqlValue.append(beanUI.getIndicaPedidoMonto()).append(", ");
		sqlValue.append(beanUI.getMontoMultiplos()).append(", ");
		sqlValue.append(beanUI.getIndicadorPrecioSucio()).append(", ");
		sqlValue.append(beanUI.getIndicaVtaEmpleados()).append(", ");
		sqlValue.append(beanUI.getIndicaRecompraNeteo()).append(", ");
		sqlValue.append("to_date('").append(sdIODate.format(beanUI.getFechaEmisionUI())).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("'), ");
		sqlValue.append("to_date('").append(sdIODate.format(beanUI.getFechaCierreUI())).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("'), ");
		sqlValue.append("'").append(beanUI.getNombreUnidadInversion().toUpperCase()).append("', ");
		sqlValue.append(beanUI.getTasaPool()).append(", ");
		sqlValue.append(beanUI.getPctRendimiento()).append(", ");
		sqlValue.append("'").append(beanUI.getTipoMercado()).append("', ");
		sqlValue.append(beanUI.getTotalInversion().toString()).append(", ");
		sqlValue.append(beanUI.getTotalInversion().toString()).append(", ");
		sqlValue.append("").append(beanUI.getMontoMinimoInversionSubasta()).append(", ");

		// Cobro adjudicación y liquidación
		sqlValue.append("").append(beanUI.getInCobroAdjudicacion()).append(", ");
		sqlValue.append("").append(beanUI.getInCobroLiquidacion()).append(", ");
		sqlValue.append(beanUI.getDiasAperturaDeCuenta()).append(", ");
//		NM25287 SICAD2. Inclusión de campo que determina si la unidad permite cancelacion.INDC_PERMITE_CANCELACION  25/03/2014
		sqlValue.append("").append(beanUI.getPermiteCancelacion()).append(", ");
//		NM25287 SIMADI Contingencia. Inclusión de campo que determina si la unidad permite manejo de Alto Valor y Bajo Valor.TIPO_NEGOCIO  18/02/2015
		sqlValue.append("").append(beanUI.getPermiteAltoValor());
		
		if (beanUI.getEmisionUnidadInversion() != null && !beanUI.getEmisionUnidadInversion().equals("")) {
			sqlAttr.append(", undinv_emision");
			sqlValue.append(", '").append(beanUI.getEmisionUnidadInversion().toUpperCase()).append("'");
		}
		if (beanUI.getSerieUnidadInversion() != null && !beanUI.getSerieUnidadInversion().equals("")) {
			sqlAttr.append(", undinv_serie");
			sqlValue.append(", '").append(beanUI.getSerieUnidadInversion().toUpperCase()).append("'");
		}

		if (beanUI.getDescrUnidadInversion() != null && !beanUI.getDescrUnidadInversion().equals("")) {
			sqlAttr.append(", undinv_descripcion");
			sqlValue.append(", '").append(beanUI.getDescrUnidadInversion().toUpperCase()).append("'");
		}
		sqlAttr.append(", undinv_fe_liquidacion");
		if (beanUI.getFechaLiquidaUI() != null && !beanUI.getFechaLiquidaUI().equals("")) {
			sqlValue.append(", to_date('").append(sdIODate.format(beanUI.getFechaLiquidaUI())).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("') ");
			sqlAttr.append(", undinv_fe_liquidacion_hora1");
			sqlValue.append(", to_date('").append(sdIOHours.format(beanUI.getFechaLiquidaUIHora1())).append("','").append("dd/MM/yyyy hh24:mi:ss").append("') ");
			sqlAttr.append(", undinv_fe_liquidacion_hora2");
			sqlValue.append(", to_date('").append(sdIOHours.format(beanUI.getFechaLiquidaUIHora2())).append("','").append("dd/MM/yyyy hh24:mi:ss").append("') ");
		} else {
			sqlValue.append(", null ");
			sqlAttr.append(", undinv_fe_liquidacion_hora1");
			sqlValue.append(", null ");
			sqlAttr.append(", undinv_fe_liquidacion_hora2");
			sqlValue.append(", null ");
		}
		sqlAttr.append(", undinv_fe_adjudicacion");
		if (beanUI.getFechaAdjudicacionUI() != null && !beanUI.getFechaAdjudicacionUI().equals("")) {
			sqlValue.append(", to_date('").append(sdIODate.format(beanUI.getFechaAdjudicacionUI())).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("')");
		} else {
			sqlValue.append(", ").append("NULL");
		}

		if (beanUI.getComentarios() != null && !beanUI.getComentarios().equals("")) {
			sqlAttr.append(", undinv_comentarios ");
			sqlValue.append(", '").append(beanUI.getComentarios()).append("'");
		}
		
		//TTS-504-SIMADI Efectivo Taquilla NM25287 21/08/2015
		if (beanUI.getPeriodicidadVenta() != -1 ) {
			sqlAttr.append(", UNDINV_PERIODO_VENTA ");
			sqlValue.append(", ").append(beanUI.getPeriodicidadVenta()).append("");
		}
		if (beanUI.getMontoMaximoInversionSubasta().intValue()!=-1) {
			sqlAttr.append(", UNDINV_UMAX_UNIDAD ");
			sqlValue.append(", ").append(beanUI.getMontoMaximoInversionSubasta()).append("");
		}
		if (beanUI.getMontoMultiplosEfectivo().intValue()!=-1) {
			sqlAttr.append(",  UNDINV_MULTIPLOS_EFECTIVO");
			sqlValue.append(", ").append(beanUI.getMontoMultiplosEfectivo()).append("");
		}

		// datos de auditoria
		DataRegistro credenciales = beanUI.getCredenciales();
		sqlAttr.append(", upd_usuario_userid, upd_usuario_nombre, upd_usuario_rol_nombre, upd_estacion, upd_ip, upd_fecha");
		sqlValue.append(", '").append(credenciales.getActUsuarioId()).append("'");
		sqlValue.append(", '").append(credenciales.getActUsuarioNombre()).append("'");
		sqlValue.append(", '").append(credenciales.getActUsuarioRolNombre()).append("'");
		sqlValue.append(", ").append(credenciales.getActEstacion() == null ? "NULL" : "'" + credenciales.getActEstacion() + "'");
		sqlValue.append(", '").append(credenciales.getActIp()).append("'");
		sqlValue.append(", ").append(formatearFechaBDHora(credenciales.getActFechaHora()));
		
		
		//TASA DE LA OFERTA
		//ITS-2961 Error al realizar OFERTA de SIMADI
		sqlAttr.append(", UNDINV_TASA_CAMBIO_OFER");
		sqlValue.append(", ").append(beanUI.getTasaCambioOferta());
		
		
		//Comision por IGTF
		//Desarrollo de requerimiento Mandatorio NM26659 29/01/2016 
		sqlAttr.append(", COMISION_IGTF");
		sqlValue.append(", ").append(beanUI.getComisionIGTF());
		
		sqlAttr.append(") ");
		sqlValue.append(")");

		sqlAttr.append(sqlValue);
		
		System.out.println("Insertar Unidad Inversion: "+sqlAttr.toString());
		db.exec(dataSource, sqlAttr.toString());
		return 0;
	}

	/**
	 * Lista una unidad de inversion registrada en la base de datos
	 * Modificación: NM25287 26/03/2015 Inclusion de nro_jornada TTS-491 WS ALTO VALOR
	 * @param idUnidadInversion identificador de la Unidad de Inversion
	 * @throws Exception
	 */
	public int listarPorId(long idUnidadInversion) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT to_char(ui.undinv_fe_liquidacion_hora1,'hh12') as hora1_liquidacion, to_char(ui.undinv_fe_liquidacion_hora1,'mi') as min1_liquidacion, to_char(ui.undinv_fe_liquidacion_hora1,'AM') as meridiano1_liquidacion, to_char(ui.undinv_fe_liquidacion_hora2,'hh12') as hora2_liquidacion, to_char(ui.undinv_fe_liquidacion_hora2,'mi') as min2_liquidacion, to_char(ui.undinv_fe_liquidacion_hora2,'AM') as meridiano2_liquidacion, ui.*, if.insfin_forma_orden, '' as instrumentoFinanciero, 0 as totalPorcentaje, emp.empres_nombre, to_char(UNDINV_FE_LIQUIDACION_HORA1,'dd-MM-yyyy hh:mm:ss')fecha1,to_char(UNDINV_FE_LIQUIDACION_HORA1,'dd-MM-yyyy hh:mm:ss')fecha2,");
		sql.append(" ui.undinv_nombre, ui.undinv_status, insfin_descripcion, ifo.insfin_forma_orden_desc as insfin_tipo, ui.moneda_id || ';' || moneda_id as monedaNegocio, if.insfin_multiples_ordenes, UNDINV_IN_RECOMPRA_NETEO, ui.moneda_id as moneda_id_ui, if.tipo_producto_id, ui.INDC_PERMITE_CANCELACION , ui.undinv_active,ui.tipo_negocio, case tipo_negocio when 0 then 'N/A' when 1 then 'Alto Valor' when 2 then 'Bajo Valor' end as desc_tipo_negocio,ui.nro_jornada,ui.UNDINV_PERIODO_VENTA,ui.UNDINV_MULTIPLOS_EFECTIVO,ui.UNDINV_UMAX_UNIDAD,ui.insfin_id, ui.UNDINV_TASA_CAMBIO_OFER,COMISION_IGTF ");
		//sql.append(" ui.undinv_nombre, ui.undinv_status, insfin_descripcion, ifo.insfin_forma_orden_desc as insfin_tipo, ui.moneda_id || ';' || moneda_id as monedaNegocio, if.insfin_multiples_ordenes, UNDINV_IN_RECOMPRA_NETEO, ui.moneda_id as moneda_id_ui, if.tipo_producto_id, '0,00' as undinv_pct_max ");
		sql.append("from INFI_TB_106_UNIDAD_INVERSION ui, INFI_TB_101_INST_FINANCIEROS if, INFI_TB_016_EMPRESAS emp, INFI_TB_038_INST_FORMA_ORDEN ifo ");
		sql.append("where ui.undinv_id = ").append(idUnidadInversion);
		sql.append(" and ui.insfin_id = if.insfin_id");		
		sql.append(" and ui.empres_id = emp.empres_id(+)");		
		sql.append(" and if.insfin_forma_orden = ifo.insfin_forma_orden(+)");
			System.out.println("listarPorId ----------> " + sql.toString());
		dataSet = db.get(dataSource, sql.toString());

		return dataSet.count();
	}

	public boolean ordenesPorId(long idUnidadInversion) throws Exception {
		StringBuffer sql = new StringBuffer();
		boolean retorno = true;
		sql.append("SELECT ordene_id from INFI_TB_204_ORDENES ");
		sql.append("where rownum < 2 and uniinv_id = ").append(idUnidadInversion);
		dataSet = db.get(dataSource, sql.toString());
		if (dataSet.count() > 0) {
			retorno = false;
		}
		return retorno;
	}

	/**
	 * Lista las unidades de inversi&oacute;n encontradas en la base de datos por el id recibido
	 * 
	 * @param idUnidad
	 *            id de la unidad de inversi&oacute;n
	 * @return lista de objetos con las unidades de inversi&oacute;n encontradas
	 */
	public UnidadInversion listarPorIdObj(int idUnidad) {
		return null;
	}

	public void listadescripcion(long idUnidadInversion) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("select distinct ui.undinv_descripcion,ui.moneda_id, ui.undinv_nombre from INFI_TB_106_UNIDAD_INVERSION ui, INFI_TB_204_ORDENES o where ui.undinv_id=o.uniinv_id(+) and ui.undinv_id = ");
		sql.append(idUnidadInversion);
		
		dataSet = db.get(dataSource, sql.toString());
		System.out.println("listadescripcion "+sql);
	}

	/**
	 * Lista las unidades de inversi&oacute;n encontradas en la base de datos por el bloter y por id de la transaccion.
	 * 
	 * @param idBloter
	 *            id bloter
	 * @param idTransaccion
	 *            id de la transaccion
	 * @return lista de objetos con las unidades de inversi&oacute;n encontradas
	 */
	public void listarPorIdBloterTransaccion(int idBloter, int idTransaccion) {
	}

	/**
	 * Lista los blotter asociados a una unidad de inversi&oacute;n
	 * 
	 * @param idUnidad
	 *            id de la unidad de inversi&oacute;n
	 * @return lista de objetos con los blotter encontrados para una unidad de inversi&oacute;n
	 */
	public void listarBloterPorUi() throws Exception {
		String sql = "select infi_tb_102_bloter.bloter_id,infi_tb_102_bloter.bloter_descripcion from infi_tb_102_bloter order by infi_tb_102_bloter.BLOTER_ID";
		dataSet = db.get(dataSource, sql);
	}

	/**
	 * Lista los blotter asociados a una unidad de inversi&oacute;n
	 * 
	 * @param idUnidad
	 *            id de la unidad de inversi&oacute;n
	 * @return lista de objetos con los blotter encontrados para una unidad de inversi&oacute;n
	 */
	public void listarBloterPorUi(long unInv) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select infi_tb_102_bloter.id_agrupacion,infi_tb_102_bloter.BLOTER_DESCRIPCION,infi_tb_107_ui_blotter.* from  infi_tb_107_ui_blotter left join infi_tb_102_bloter on infi_tb_107_ui_blotter.BLOTER_ID=infi_tb_102_bloter.BLOTER_ID where infi_tb_107_ui_blotter.UNDINV_ID=");
		sql.append(unInv);
		sql.append(" order by bloter_descripcion");
		dataSet = db.get(dataSource, sql.toString());
	}

	/**
	 * Lista los status asociados a la unidad de inversi&oacute;n
	 * 
	 * @return lista de objetos con los status encontrados para una unidad de inversi&oacute;n
	 * @throws Exception
	 */
	public void listarStatus() throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT undinv_status as idUIStatus, undinv_status_descripcion as descrUIStatus ");
		sql.append("from INFI_TB_117_UNDINV_STATUS ");
		sql.append("order by undinv_status_descripcion ");

		dataSet = db.get(dataSource, sql.toString());
	}

	/**
	 * Lista las unidades de inversi&oacute;n
	 * 
	 * @param idEmpresaEmisor
	 *            : Empresa Emisor
	 * @param fechaEmisionDesde
	 *            : Fecha desde de emision
	 * @param fechaEmisionHasta
	 *            : Fecha hasta de de emision
	 * @param status
	 *            : Array de status relacionados a la unidad de inversi&oacute;n
	 * @return cantidad de registros recuperados
	 * @throws Exception
	 */
	public int listarPorEmisor(String idEmpresaEmisor, Date fechaEmisionDesde, Date fechaEmisionHasta, String[] status) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT undinv_id as idUnidadInversion, undinv_nombre as nombreUnidadInversion, undinv_fe_emision as fechaEmisionUI, undinv_fe_liquidacion as fechaLiquidaUI, undinv_fe_cierre as fechaCierreUI,");
		sql.append("undinv_status_descripcion as descrUIStatus, insfin_descripcion as descrInstrumento, insfin_forma_orden as tipoInstrumento, emp.empres_nombre as nombreEmpresa ");
		sql.append("from INFI_TB_106_UNIDAD_INVERSION ui ");
		sql.append("inner join INFI_TB_117_UNDINV_STATUS uista on uista.undinv_status = ui.undinv_status ");
		sql.append("inner join INFI_TB_101_INST_FINANCIEROS if on if.insfin_id = ui.insfin_id ");
		sql.append("inner join INFI_TB_016_EMPRESAS emp on emp.empres_id = ui.empres_id ");
		sql.append("where ui.undinv_fe_emision between ");
		sql.append("to_date('").append(sdIODate.format(fechaEmisionDesde)).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("') ");
		sql.append("and ");
		sql.append("to_date('").append(sdIODate.format(fechaEmisionHasta)).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("') ");
		sql.append("and lower(ui.empres_id) = '");
		sql.append(idEmpresaEmisor.toLowerCase());
		sql.append("' ");
		if (status != null) {
			sql.append("and ui.undinv_status in ");
			sql.append("(select undinv_status from INFI_TB_117_UNDINV_STATUS where undinv_status in (");
			sql.append("'").append(status[0]).append("' ");
			for (int i = 1; i < status.length; i++) {
				sql.append(", ").append("'").append(status[i]).append("' ");
			}
			sql.append("))");
		}
		sql.append(" order by undinv_nombre asc");
		dataSet = db.get(dataSource, sql.toString());
		System.out.println("listarPorEmisor"+sql);
		return dataSet.count();
	}

	/**
	 * Lista las unidades de inversi&oacute;n
	 * 
	 * @param fechaEmisionDesde
	 *            : Fecha desde de emision
	 * @param fechaEmisionHasta
	 *            : Fecha hasta de de emision
	 * @param status
	 *            : Array de status relacionados a la unidad de inversi&oacute;n
	 * @return cantidad de registros recuperados
	 * @throws Exception
	 */
	public int listarPorFechaEmision(Date fechaEmisionDesde, Date fechaEmisionHasta, String[] status) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT undinv_id as idUnidadInversion, undinv_nombre as nombreUnidadInversion, undinv_fe_emision as fechaEmisionUI, undinv_fe_liquidacion as fechaLiquidaUI, undinv_fe_cierre as fechaCierreUI,");
		sql.append("undinv_status_descripcion as descrUIStatus, insfin_descripcion as descrInstrumento, insfin_forma_orden as tipoInstrumento, emp.empres_nombre as nombreEmpresa ");
		sql.append("from INFI_TB_106_UNIDAD_INVERSION ui ");
		sql.append("inner join INFI_TB_117_UNDINV_STATUS uista on uista.undinv_status = ui.undinv_status ");
		sql.append("inner join INFI_TB_101_INST_FINANCIEROS if on if.insfin_id = ui.insfin_id ");
		sql.append("inner join INFI_TB_016_EMPRESAS emp on emp.empres_id = ui.empres_id ");
		sql.append("where ui.undinv_fe_emision between ");
		sql.append("to_date('").append(sdIODate.format(fechaEmisionDesde)).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("') ");
		sql.append("and ");
		sql.append("to_date('").append(sdIODate.format(fechaEmisionHasta)).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("') ");
		if (status != null) {
			sql.append("and ui.undinv_status in ");
			sql.append("(select undinv_status from INFI_TB_117_UNDINV_STATUS where undinv_status in (");
			sql.append("'").append(status[0]).append("' ");
			for (int i = 1; i < status.length; i++) {
				sql.append(", '").append(status[i]).append("' ");
			}
			sql.append("))");
		}
		// //System.out.println("Query de Lista UI: "+sql.toString());
		sql.append(" order by undinv_nombre asc");
		dataSet = db.get(dataSource, sql.toString());
		System.out.println("listarPorFechaEmision "+sql);
		return dataSet.count();
	}

	/**
	 * Lista las unidades de inversi&oacute;n
	 * 
	 * @param idInstrumentoFinanciero
	 *            : Instrumento financiero
	 * @param fechaEmisionDesde
	 *            : Fecha desde de emision
	 * @param fechaEmisionHasta
	 *            : Fecha hasta de de emision
	 * @param status
	 *            : Array de status relacionados a la unidad de inversi&oacute;n
	 * @return cantidad de registros recuperados
	 * @throws Exception
	 */
	public int listarPorInstFinanciero(String idInstrumentoFinanciero, Date fechaEmisionDesde, Date fechaEmisionHasta, String[] status) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT undinv_id as idUnidadInversion, undinv_nombre as nombreUnidadInversion, undinv_fe_emision as fechaEmisionUI, undinv_fe_liquidacion as fechaLiquidaUI, undinv_fe_cierre as fechaCierreUI,");
		sql.append("undinv_status_descripcion as descrUIStatus, insfin_descripcion as descrInstrumento, insfin_forma_orden as tipoInstrumento, emp.empres_nombre as nombreEmpresa ");
		sql.append("from INFI_TB_106_UNIDAD_INVERSION ui ");
		sql.append("inner join INFI_TB_117_UNDINV_STATUS uista on uista.undinv_status = ui.undinv_status ");
		sql.append("inner join INFI_TB_101_INST_FINANCIEROS if on if.insfin_id = ui.insfin_id ");
		sql.append("inner join INFI_TB_016_EMPRESAS emp on emp.empres_id = ui.empres_id ");
		sql.append("where ui.undinv_fe_emision between ");
		sql.append("to_date('").append(sdIODate.format(fechaEmisionDesde)).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("') ");
		sql.append("and ");
		sql.append("to_date('").append(sdIODate.format(fechaEmisionHasta)).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("') ");
		sql.append("and lower(ui.insfin_id) = '");
		sql.append(idInstrumentoFinanciero.toLowerCase());
		sql.append("' ");
		if (status != null) {
			sql.append("and ui.undinv_status in ");
			sql.append("(select undinv_status from INFI_TB_117_UNDINV_STATUS where undinv_status in (");
			sql.append("'").append(status[0]).append("' ");
			for (int i = 1; i < status.length; i++) {
				sql.append(", ").append("'").append(status[i]).append("' ");
			}
			sql.append("))");
		}
		sql.append(" order by undinv_nombre asc");
		dataSet = db.get(dataSource, sql.toString());
		System.out.println("listarPorInstFinanciero "+sql);
		return dataSet.count();

	}

	/**
	 * lista todas las unidades de inversi&oacute;n almacenadas en la base de datos
	 * 
	 * @throws Exception
	 */
	public void listar() throws Exception {

		String sql = "SELECT * FROM INFI_TB_106_UNIDAD_INVERSION order by undinv_id DESC"; // NM25287 Se agrega order by para facilitar la busqueda 12/07/2016

		dataSet = db.get(dataSource, sql);

	}

	/**
	 * lista todas las unidades de inversi&oacute;n tipo Producto Subasta almacenadas en la base de datos donde la fecha de hoy es mayor a la fecha de cierre y se les haya generar archivo y este no este cerrado
	 * 
	 * @throws Exception
	 */
	public void listarFeCierre(String status) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT distinct u.undinv_id, u.undinv_nombre, u.undinv_serie ");
		sql.append(" FROM INFI_TB_106_UNIDAD_INVERSION u , INFI_TB_803_CONTROL_ARCHIVOS c, INFI_TB_101_INST_FINANCIEROS insf ");
		sql.append(" where insf.insfin_id = u.insfin_id and insf.TIPO_PRODUCTO_ID='" + ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA + "'");
		sql.append(" and undinv_fe_cierre<=sysdate and c.undinv_id=u.undinv_id and undinv_status='" + status + "'");
		// //System.out.println("SQL (listar UI para cierre de proceso): "+sql.toString());
		dataSet = db.get(dataSource, sql.toString());

	}
	
	/**
	 * lista todas las unidades de inversi&oacute;n tipo producto Subasta Divisas almacenadas en la base de datos donde la fecha de hoy es mayor a la fecha de cierre y se les haya generar archivo y este no este cerrado
	 * 
	 * @throws Exception
	 */
	public void listarFeCierreSubastaDivisas(String status,String tipoProducto) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT distinct u.undinv_id, u.undinv_nombre, u.undinv_serie ");
		sql.append(" FROM INFI_TB_106_UNIDAD_INVERSION u , INFI_TB_803_CONTROL_ARCHIVOS c, INFI_TB_101_INST_FINANCIEROS insf ");
		sql.append(" where insf.insfin_id = u.insfin_id and insf.TIPO_PRODUCTO_ID='" + tipoProducto + "'");
		sql.append(" and undinv_fe_cierre<=sysdate and c.undinv_id=u.undinv_id and undinv_status='" + status + "'");
//		System.out.println("listarFeCierreSubastaDivisas ----> " + sql.toString());
		dataSet = db.get(dataSource, sql.toString());

	}


	/**
	 * lista todas las unidades de inversi&oacute;n almacenadas en la base de datos donde la fecha de hoy es mayor a la fecha de cierre y se les haya REALIZADO LA RECEPCION DEL archivo
	 * 
	 * @throws Exception
	 */
	public void listarRecepcion() throws Exception {
		String sql = "SELECT distinct u.undinv_id, u.undinv_nombre, u.undinv_serie FROM INFI_TB_106_UNIDAD_INVERSION u , INFI_TB_803_CONTROL_ARCHIVOS c where u.undinv_fe_cierre<=sysdate and c.undinv_id=u.undinv_id and u.undinv_status='" + UnidadInversionConstantes.UISTATUS_ADJUDICADA + "'";
		// //System.out.println("SQL (listarRecepcion): "+sql);
		dataSet = db.get(dataSource, sql);

	}

	/**
	 * lista todas las unidades de inversi&oacute;n almacenadas en la base de datos con status especifico
	 * 
	 * @throws Exception
	 */
	public void listar(String status) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * FROM INFI_TB_106_UNIDAD_INVERSION where undinv_status='");
		sql.append(status).append("' order by undinv_descripcion");

		dataSet = db.get(dataSource, sql.toString());

	}

	/**
	 * Lista las unidades de inversión de acuerdo al blotter del usuario conectado (si aplica), el cual debe estar asociado a cada una de las unidades de inversión a listar. Igualmente se listan de acuerdo a la fecha actual, la cual debe encontrarse entre la fecha de emisión y la fecha de cierre de la unidad de inversión.
	 * 
	 * @param idBlotter
	 * @throws Exception
	 */
	public void listarUnidadesTomaOrden(String idBlotter) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ui.* FROM INFI_TB_106_UNIDAD_INVERSION ui where ui.undinv_status='");
		sql.append(UnidadInversionConstantes.UISTATUS_PUBLICADA).append("'");

		// si se filtra por blotter conectado
		if (idBlotter != null && !idBlotter.equals("")) {
			sql.append(" and '").append(idBlotter).append("' IN (select uiblot.bloter_id from infi_tb_107_ui_blotter uiblot where undinv_id = ui.undinv_id) ");
		}

		sql.append(" and (").append(formatearFechaBDActual()).append(" between ui.undinv_fe_emision and undinv_fe_cierre ) ");
		sql.append(" order by undinv_nombre");
		
		dataSet = db.get(dataSource, sql.toString());

	}
	
	/**
	 * Lista las unidades de inversión de acuerdo al blotter del usuario conectado (si aplica), el cual debe estar asociado a cada una de las unidades de inversión a listar. Igualmente se listan de acuerdo a la fecha actual, la cual debe encontrarse entre la fecha de emisión y la fecha de cierre de la unidad de inversión.
	 * 
	 * @param idBlotter
	 * @throws Exception
	 */
	public void listarUnidadesTomaOrden(String idBlotter, String idTipoProd, boolean igualTipoProd) throws Exception {
		StringBuffer sql = new StringBuffer();
		String condicionIN="";
		
		if(!igualTipoProd){
			condicionIN=" NOT ";
		}
		
		sql.append(" SELECT ui.* FROM INFI_TB_106_UNIDAD_INVERSION ui where ui.undinv_status='");
		sql.append(UnidadInversionConstantes.UISTATUS_PUBLICADA).append("'");

		// si se filtra por blotter conectado
		if (idBlotter != null && !idBlotter.equals("")) {
			sql.append(" and '").append(idBlotter).append("' IN (select uiblot.bloter_id from infi_tb_107_ui_blotter uiblot where undinv_id = ui.undinv_id) ");
		}
		
		//Si se filtra por instrumento financiero
		if (idTipoProd != null && !idTipoProd.equals("")) {
			sql.append(" and ui.insfin_id "+condicionIN+"IN (select if.insfin_id from INFI_TB_101_INST_FINANCIEROS if where if.tipo_producto_id='").append(idTipoProd).append("') ");
		}

		sql.append(" and (").append(formatearFechaBDActual()).append(" between ui.undinv_fe_emision and undinv_fe_cierre ) ");
		//sql.append(" order by undinv_descripcion");
		sql.append(" order by undinv_nombre");
		System.out.println("listarUnidadesInv:\n"+sql);
		dataSet = db.get(dataSource, sql.toString());
		
	}
	
	/**
	 * Lista las unidades de inversión de acuerdo al blotter del usuario conectado (si aplica), el cual debe estar asociado a cada una de las unidades de inversión a listar. Igualmente se listan de acuerdo a la fecha actual, la cual debe encontrarse entre la fecha de emisión y la fecha de cierre de la unidad de inversión.
	 * 
	 * @param idBlotter
	 * @throws Exception
	 */
	public void listarUnidadesTomaOrden(String idBlotter,boolean igualTipoProd,String... idTipoProd) throws Exception {
		StringBuffer sql = new StringBuffer();
		String condicionIN="";
		
		if(!igualTipoProd){
			condicionIN=" NOT ";
		}
		
		sql.append(" SELECT ui.* FROM INFI_TB_106_UNIDAD_INVERSION ui where ui.undinv_status='");
		sql.append(UnidadInversionConstantes.UISTATUS_PUBLICADA).append("'");

		// si se filtra por blotter conectado
		if (idBlotter != null && !idBlotter.equals("")) {
			//sql.append(" and '").append(idBlotter).append("' EXISTS (select uiblot.bloter_id from infi_tb_107_ui_blotter uiblot where undinv_id = ui.undinv_id) ");
			sql.append(" AND EXISTS (select uiblot.bloter_id from infi_tb_107_ui_blotter uiblot where undinv_id = ui.undinv_id) ");
		}
		
		//Si se filtra por instrumento financiero
		if (idTipoProd.length>0 && !idTipoProd[0].equals("")) {
			sql.append(" and  "+condicionIN+" EXISTS  (select if.insfin_id from INFI_TB_101_INST_FINANCIEROS if where  IF.insfin_id = ui.insfin_id AND if.tipo_producto_id IN (");
			int count=0;
			for (String element : idTipoProd) {
				if(count>0){
					sql.append(",");
				}
				sql.append("'"+element+"'");
				++count;
			}
			sql.append(")) ");
		}

		sql.append(" and (").append(formatearFechaBDActual()).append(" between ui.undinv_fe_emision and undinv_fe_cierre ) ");
		//sql.append(" order by undinv_descripcion");
		sql.append(" order by undinv_nombre");
		//System.out.println("listarUnidadesInv SOBRECARGA:\n"+sql);
		dataSet = db.get(dataSource, sql.toString());
		
	}
	
	/**
	 * Lista las unidades de inversión de acuerdo al blotter del usuario conectado (si aplica), el cual debe estar asociado a cada una de las unidades de inversión a listar. Igualmente se listan de acuerdo a la fecha actual, la cual debe encontrarse entre la fecha de emisión y la fecha de cierre de la unidad de inversión.
	 * 
	 * @param idBlotter
	 * @throws Exception
	 */
	/*public void listarUnidadesTomaOrden(String idBlotter, String idInstFin) throws Exception {
		//System.out.println("idBlotter: "+idBlotter+" idInstFin: "+idInstFin);
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ui.* FROM INFI_TB_106_UNIDAD_INVERSION ui where ui.undinv_status='");
		sql.append(UnidadInversionConstantes.UISTATUS_PUBLICADA).append("'");

		// si se filtra por blotter conectado
		if (idBlotter != null && !idBlotter.equals("")) {
			sql.append(" and '").append(idBlotter).append("' IN (select uiblot.bloter_id from infi_tb_107_ui_blotter uiblot where undinv_id = ui.undinv_id) ");
		}
		
		//Si se filtra por instrumento financiero
		if (idInstFin != null && !idInstFin.equals("")) {
			sql.append(" and ui.insfin_id IN (select if.insfin_id from INFI_TB_101_INST_FINANCIEROS if where if.insfin_id=").append(idInstFin.toString()).append(") ");
		}

		sql.append(" and (").append(formatearFechaBDActual()).append(" between ui.undinv_fe_emision and undinv_fe_cierre ) ");
		//sql.append(" order by undinv_descripcion");
		sql.append(" order by undinv_nombre");
		//System.out.println("listarUnidadesInv:\n"+sql);
		dataSet = db.get(dataSource, sql.toString());
		
	}*/
	
	/**
	 * Lista las unidades de inversi&oacute;n y sus ordenes relacionadas
	 * 
	 * @param nombreUnidadInversion
	 *            : prefijo del nombre de la unidad de inversion
	 * @return cantidad de registros recuperados
	 * @throws Exception
	 */
	public int listarPorNombre(String nombreUnidadInversion, Date fechaEmisionDesde, Date fechaEmisionHasta, String[] status) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT undinv_id as idUnidadInversion, undinv_nombre as nombreUnidadInversion, undinv_fe_emision as fechaEmisionUI, undinv_fe_liquidacion as fechaLiquidaUI, undinv_fe_cierre as fechaCierreUI,");
		sql.append("undinv_status_descripcion as descrUIStatus, insfin_descripcion as descrInstrumento, insfin_forma_orden as tipoInstrumento, emp.empres_nombre as nombreEmpresa ");
		sql.append("from INFI_TB_106_UNIDAD_INVERSION ui ");
		sql.append("inner join INFI_TB_117_UNDINV_STATUS uista on uista.undinv_status = ui.undinv_status ");
		sql.append("inner join INFI_TB_101_INST_FINANCIEROS if on if.insfin_id = ui.insfin_id ");
		sql.append("inner join INFI_TB_016_EMPRESAS emp on emp.empres_id = ui.empres_id ");
		sql.append("where lower(ui.undinv_nombre) like '%");
		sql.append(nombreUnidadInversion.toLowerCase());
		sql.append("%' ");
		sql.append(" and ui.undinv_fe_emision between ");
		sql.append("to_date('").append(sdIODate.format(fechaEmisionDesde)).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("') ");
		sql.append("and ");
		sql.append("to_date('").append(sdIODate.format(fechaEmisionHasta)).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("') ");
		if (status != null) {
			sql.append(" and ui.undinv_status in ");
			sql.append("(select undinv_status from INFI_TB_117_UNDINV_STATUS where undinv_status in (");
			sql.append("'").append(status[0]).append("' ");
			for (int i = 1; i < status.length; i++) {
				sql.append(", ").append("'").append(status[i]).append("' ");
			}
			sql.append("))");
		}
		sql.append(" order by undinv_nombre asc");
		dataSet = db.get(dataSource, sql.toString());
		System.out.println("listarPorNombre"+sql);
		return dataSet.count();
		
	}

	public int listarPorNombre(String nombreUnidadInversion,String tipoProducto,String[] status) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT UI.UNDINV_ID,UI.UNDINV_NOMBRE,UI.UNDINV_TASA_CAMBIO,UI.EMPRES_ID,");
		sql.append("UI.UNDINV_TASA_POOL,TO_CHAR (UI.UNDINV_FE_LIQUIDACION, 'DD-MM-YYYY') AS UNDINV_FE_LIQUIDACION ");
		sql.append("from INFI_TB_106_UNIDAD_INVERSION UI,INFI_TB_101_INST_FINANCIEROS INSF  ");
		sql.append("WHERE ui.undinv_nombre =upper('");
		sql.append(nombreUnidadInversion.toLowerCase());		
		sql.append("') ");	
		
		sql.append(" AND  UI.INSFIN_ID=INSF.INSFIN_ID  AND INSF.TIPO_PRODUCTO_ID='").append(tipoProducto).append("'");
		
		if(status!=null){
			sql.append(" AND UI.UNDINV_STATUS IN ('");
			sql.append(status[0]);
			sql.append("'");
			
			if (status.length>1) {										
				for(String element:status){
					sql.append(", ").append("'").append(element).append("' ");
				}							
			}
			sql.append(")");
		}		
		dataSet = db.get(dataSource, sql.toString());
		System.out.println("listarPorNombre"+sql);
		return dataSet.count();
	}
	/**
	 * Modificar los datos generales de una unidad de inversion
	 * 
	 * @param beanUI
	 *            : bean Unidad de Inversion con la data a registrar
	 * @return codigo de retorno
	 * @throws Exception
	 */
	public int modificar(UnidadInversion beanUI) throws Exception {

		StringBuffer sqlSB = new StringBuffer();

		sqlSB.append("update INFI_TB_106_UNIDAD_INVERSION set ");
		sqlSB.append("insfin_id = ");
		sqlSB.append("'").append(beanUI.getIdInstrumentoFinanciero()).append("', ");
		sqlSB.append("moneda_id =");
		sqlSB.append("'").append(beanUI.getIdMoneda()).append("', ");
		sqlSB.append("empres_id = ");
		sqlSB.append("'").append(beanUI.getIdEmpresaEmisor()).append("', ");
		sqlSB.append("undinv_tasa_cambio =");
		sqlSB.append(beanUI.getTasaCambio().toString()).append(", ");
		sqlSB.append("undinv_in_pedido_monto =");
		sqlSB.append(beanUI.getIndicaPedidoMonto()).append(", ");
		sqlSB.append("undinv_in_vta_empleados =");
		sqlSB.append(beanUI.getIndicaVtaEmpleados()).append(", ");
		sqlSB.append("undinv_in_recompra_neteo = ");
		sqlSB.append(beanUI.getIndicaRecompraNeteo()).append(", ");
		sqlSB.append("undinv_fe_emision = ");
		sqlSB.append("to_date('").append(sdIODate.format(beanUI.getFechaEmisionUI())).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("'), ");
		sqlSB.append("undinv_fe_cierre = ");
		sqlSB.append("to_date('").append(sdIODate.format(beanUI.getFechaCierreUI())).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("'), ");
		sqlSB.append("undinv_nombre = ");
		sqlSB.append("'").append(beanUI.getNombreUnidadInversion().toUpperCase()).append("', ");

		sqlSB.append("undinv_tasa_pool = ");
		sqlSB.append(beanUI.getTasaPool()).append(", ");
		sqlSB.append("undinv_rendimiento = ");
		sqlSB.append(beanUI.getPctRendimiento()).append(", ");
		sqlSB.append("undinv_mercado = '");
		sqlSB.append(beanUI.getTipoMercado()).append("', ");

		sqlSB.append("undinv_umi_inv_total =");
		sqlSB.append(beanUI.getTotalInversion().toString()).append(", ");
		sqlSB.append("undinv_umi_inv_disponible =");
		sqlSB.append(beanUI.getTotalInversion().toString()).append(", ");
		sqlSB.append("undinv_umi_unidad =");
		sqlSB.append(beanUI.getMontoMinimoInversionSubasta().toString()).append(", ");
		sqlSB.append("undinv_multiplos =");
		sqlSB.append(beanUI.getMontoMultiplos().toString()).append(", ");

		// Cobro batch
		sqlSB.append("in_cobro_batch_adj= ").append(beanUI.getInCobroAdjudicacion()).append(",");
		sqlSB.append("in_cobro_batch_liq= ").append(beanUI.getInCobroLiquidacion()).append(",");

		sqlSB.append("undinv_in_precio_sucio= ");
		sqlSB.append(beanUI.getIndicadorPrecioSucio()).append(" ");

		// campos opcionales
		if (beanUI.getEmisionUnidadInversion() == null || beanUI.getEmisionUnidadInversion().equals("")) {
			sqlSB.append(", undinv_emision = null");
		} else {
			sqlSB.append(", undinv_emision =");
			sqlSB.append("'").append(beanUI.getEmisionUnidadInversion().toUpperCase()).append("' ");
		}
		if (beanUI.getSerieUnidadInversion() == null || beanUI.getSerieUnidadInversion().equals("")) {
			sqlSB.append(", undinv_serie = null");
		} else {
			sqlSB.append(", undinv_serie = ");
			sqlSB.append("'").append(beanUI.getSerieUnidadInversion().toUpperCase()).append("' ");
		}
		if (beanUI.getDescrUnidadInversion() == null || beanUI.getDescrUnidadInversion().equals("")) {
			sqlSB.append(", undinv_descripcion = null");
		} else {
			sqlSB.append(", undinv_descripcion = ");
			sqlSB.append("'").append(beanUI.getDescrUnidadInversion()).append("' ");
		}
		if (beanUI.getFechaLiquidaUI() == null || beanUI.getFechaLiquidaUI().equals("")) {
			sqlSB.append(", undinv_fe_liquidacion = null ");
			if (beanUI.getFechaLiquidaUIHora1() == null || beanUI.getFechaLiquidaUIHora1().equals("")) {
				sqlSB.append(", undinv_fe_liquidacion_hora1 = null ");
			} else {
				sqlSB.append(", undinv_fe_liquidacion_hora1 = ");
				sqlSB.append("to_date('").append(sdIOHours.format(beanUI.getFechaLiquidaUIHora1())).append("','").append("dd/MM/yyyy hh24:mi:ss").append("') ");
			}
			if (beanUI.getFechaLiquidaUIHora2() == null || beanUI.getFechaLiquidaUIHora2().equals("")) {
				sqlSB.append(", undinv_fe_liquidacion_hora2 = null ");
			} else {
				sqlSB.append(", undinv_fe_liquidacion_hora2 = ");
				sqlSB.append("to_date('").append(sdIOHours.format(beanUI.getFechaLiquidaUIHora2())).append("','").append("dd/MM/yyyy hh24:mi:ss").append("') ");
			}
		} else {
			sqlSB.append(", undinv_fe_liquidacion = ");
			sqlSB.append("to_date('").append(sdIODate.format(beanUI.getFechaLiquidaUI())).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("') ");
			sqlSB.append(", undinv_fe_liquidacion_hora1 = ");
			sqlSB.append("to_date('").append(sdIOHours.format(beanUI.getFechaLiquidaUIHora1())).append("','").append("dd/MM/yyyy hh24:mi:ss").append("') ");
			sqlSB.append(", undinv_fe_liquidacion_hora2 = ");
			sqlSB.append("to_date('").append(sdIOHours.format(beanUI.getFechaLiquidaUIHora2())).append("','").append("dd/MM/yyyy hh24:mi:ss").append("') ");
		}
		if (beanUI.getFechaAdjudicacionUI() == null || beanUI.getFechaAdjudicacionUI().equals("")) {
			sqlSB.append(", undinv_fe_adjudicacion = null");
		} else {
			sqlSB.append(", undinv_fe_adjudicacion =");
			sqlSB.append("to_date('").append(sdIODate.format(beanUI.getFechaAdjudicacionUI())).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("') ");
		}
		if (beanUI.getComentarios() == null || beanUI.getComentarios().equals("")) {
			sqlSB.append(", undinv_comentarios = null");
		} else {
			sqlSB.append(", undinv_comentarios = ");
			sqlSB.append("'").append(beanUI.getComentarios()).append("' ");
		}

		if (beanUI.getInPagoBCV() != null && !beanUI.getInPagoBCV().equals("")) {
			sqlSB.append(", in_pago_bcv = ");
			sqlSB.append("'").append(beanUI.getInPagoBCV()).append("' ");
		}

		sqlSB.append(", dias_apertura_cuenta=");
		sqlSB.append(beanUI.getDiasAperturaDeCuenta());

		// datos de auditoria
		DataRegistro credenciales = beanUI.getCredenciales();
		sqlSB.append(", upd_usuario_userid = ");
		sqlSB.append(" '").append(credenciales.getActUsuarioId()).append("', ");
		sqlSB.append("upd_usuario_nombre = ");
		sqlSB.append(" '").append(credenciales.getActUsuarioNombre()).append("', ");
		sqlSB.append("upd_usuario_rol_nombre = ");
		sqlSB.append(" '").append(credenciales.getActUsuarioRolNombre()).append("', ");
		sqlSB.append("upd_estacion = ");
		sqlSB.append(credenciales.getActEstacion() == null ? "NULL," : "'" + credenciales.getActEstacion() + "', ");
		sqlSB.append("upd_ip = ");
		sqlSB.append(" '").append(credenciales.getActIp()).append("', ");
		sqlSB.append("upd_fecha = ").append(formatearFechaBDHora(credenciales.getActFechaHora())+ ", ");
		//NM25287 SICAD2. Inclusión de campo que determina si la unidad permite cancelacion. INDC_PERMITE_CANCELACION 25/03/2014
		sqlSB.append("INDC_PERMITE_CANCELACION = ").append(beanUI.getPermiteCancelacion()+ ", "); 
		//NM25287 TTS-491 SIMADI. Inclusión de campo que determina si la unidad permite manejo de alto valor y bajo valor. TIPO_NEGOCIO 18/02/2015
		sqlSB.append("TIPO_NEGOCIO = ").append(beanUI.getPermiteAltoValor()+ ", ");		
		
		//TTS-504-SIMADI Efectivo Taquilla NM25287 21/08/2015. Validar campos para manejo de efectivo
		sqlSB.append("UNDINV_PERIODO_VENTA = ").append(beanUI.getPeriodicidadVenta()+ ", ");	
		sqlSB.append("UNDINV_MULTIPLOS_EFECTIVO = ").append(beanUI.getMontoMultiplosEfectivo()+ ", ");	
		sqlSB.append("UNDINV_UMAX_UNIDAD = ").append(beanUI.getMontoMaximoInversionSubasta()+ ", ");	
		sqlSB.append("UNDINV_TASA_CAMBIO_OFER = ").append(beanUI.getTasaCambioOferta()+ ", ");

		//Desarrollo requerimiento mandatorio IGTF NM26659 29/01/2016  
		sqlSB.append("COMISION_IGTF = ").append(beanUI.getComisionIGTF());
		
		sqlSB.append(" where undinv_id = ");
		sqlSB.append(beanUI.getIdUnidadInversion());

		System.out.println("-----modificarUI: "+sqlSB.toString());
		db.exec(dataSource, sqlSB.toString());
		return 0;
	}

	/**
	 * Modificar los datos necesarios para indicar que la unidad de inversion fue publicada
	 * 
	 * @param beanUI
	 *            : bean Unidad de Inversion con la data a registrar
	 * @return codigo de retorno
	 * @throws Exception
	 */
	public String ingresarUsuarioPublica(UnidadInversion beanUI) throws Exception {

		StringBuffer sqlSB = new StringBuffer();

		sqlSB.append("update INFI_TB_106_UNIDAD_INVERSION set ");
		sqlSB.append("undinv_status = '").append(beanUI.getIdUIStatus()).append("' ");

		// datos de auditoria PUBLICACION
		DataRegistro credenciales = beanUI.getCredenciales();
		sqlSB.append(", aut_usuario_userid = ");
		sqlSB.append(" '").append(credenciales.getActUsuarioId()).append("', ");
		sqlSB.append("aut_usuario_nombre = ");
		sqlSB.append(" '").append(credenciales.getActUsuarioNombre()).append("', ");
		sqlSB.append("aut_usuario_rol_nombre = ");
		sqlSB.append(" '").append(credenciales.getActUsuarioRolNombre()).append("', ");
		sqlSB.append("aut_estacion = ");
		sqlSB.append(credenciales.getActEstacion() == null ? "NULL," : "'" + credenciales.getActEstacion() + "',");
		sqlSB.append("aut_ip = ");
		sqlSB.append(" '").append(credenciales.getActIp()).append("', ");
		sqlSB.append("aut_fecha = ").append(formatearFechaBDHora(credenciales.getActFechaHora()));
		;

		sqlSB.append("where undinv_id = ");
		sqlSB.append(beanUI.getIdUnidadInversion());

		return sqlSB.toString();
	}

	/**
	 * 
	 * @param idUnidadInversion
	 * @param status
	 * @throws Exception
	 */
	public int modificarStatus(long idUnidadInversion, String status) throws Exception {

		StringBuffer sqlSB = new StringBuffer();

		sqlSB.append("update INFI_TB_106_UNIDAD_INVERSION set ");
		sqlSB.append("undinv_status = '").append(status).append("' ");
		sqlSB.append("where undinv_id = ");
		sqlSB.append(idUnidadInversion);
		db.exec(dataSource, sqlSB.toString());
		return 0;
	}

	/**
	 * Query para modificar status de la unidad de inversion
	 * 
	 * @param idUnidadInversion
	 * @param status
	 * @return
	 * @throws Exception
	 */
	public String modificarStatusQuery(long idUnidadInversion, String status) throws Exception {

		StringBuffer sqlSB = new StringBuffer();

		sqlSB.append("update INFI_TB_106_UNIDAD_INVERSION set ");
		sqlSB.append("undinv_status = '").append(status).append("' ");
		sqlSB.append("where undinv_id = ");
		sqlSB.append(idUnidadInversion);
		return sqlSB.toString();
	}

	/**
	 */
	public String modificarMontoDisponible(UnidadInversion unidadInversion) throws Exception {

		StringBuffer sql = new StringBuffer();
		//StringBuffer filtro = new StringBuffer("");

		sql.append("update INFI_TB_106_UNIDAD_INVERSION set ");
		sql.append("undinv_umi_inv_disponible=").append(unidadInversion.getTotalInversion());
		sql.append(" where undinv_id=").append(unidadInversion.getIdUnidadInversion());
		return (sql.toString());
	}

	/**
	 * 
	 * @param idUnidadInversion
	 * @param umiUnidadInversion
	 * @return
	 * @throws Exception
	 */
	public int modificarUMI(long idUnidadInversion, BigDecimal umiUnidadInversion) throws Exception {

		StringBuffer sqlSB = new StringBuffer();

		sqlSB.append("update INFI_TB_106_UNIDAD_INVERSION set ");
		sqlSB.append("undinv_umi_unidad = ");
		sqlSB.append(umiUnidadInversion.toString()).append(" ");
		sqlSB.append("where undinv_id = ");
		sqlSB.append(idUnidadInversion);

		db.exec(dataSource, sqlSB.toString());
		return 0;
	}

	/**
	 * Lista los blotters configurados por unidad de inversi&oacute;n
	 * 
	 * @param idUnidad
	 *            unidad de inversi&oacute;n
	 * */
	public void listarBlotterPorUI(long idUnidadInversion) throws Exception {

		UIBlotterDAO boUIBlotter = new UIBlotterDAO(dataSource);
		boUIBlotter.listarBlottersPorID(idUnidadInversion);
		dataSet = boUIBlotter.getDataSet();
	}

	/**
	 * Lista las empresas asociados por unidad de inversi&oacute;n
	 * 
	 * @param idUnidad
	 *            unidad de inversi&oacute;n
	 * */
	public void listarEmpresasPorUI(long idUnidadInversion) throws Exception {

		UIEmpresaDAO boUIEmpresa = new UIEmpresaDAO(dataSource);
		boUIEmpresa.listarEmpresasPorID(idUnidadInversion);
		dataSet = boUIEmpresa.getDataSet();
	}

	/**
	 * Lista los indicadores configurados por unidad de inversi&oacute;n
	 * 
	 * @param idUnidad
	 *            unidad de inversi&oacute;n
	 * */
	public void listarIndicadoresPorUI(long idUnidadInversion) throws Exception {

		UIIndicadoresDAO boUIIndicadores = new UIIndicadoresDAO(dataSource);
		boUIIndicadores.listarIndicadoresPorID(idUnidadInversion);
		dataSet = boUIIndicadores.getDataSet();
	}

	/**
	 * Lista los t&iacute;tulos configurados por unidad de inversi&oacute;n
	 * Modificado por NM25287. Inclusión de tipos de producto SICAD2PER y SICAD2RED 20/03/2014
	 * @param idUnidad
	 *            unidad de inversi&oacute;n
	 * */
	public void listarTitulosPorUI(long idUnidadInversion,String ...tipoProductoId) throws Exception {

		UITitulosDAO boUITitulos = new UITitulosDAO(dataSource);
		if (tipoProductoId.length>0&&(tipoProductoId[0].equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA)
				||tipoProductoId[0].equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL)
					||tipoProductoId[0].equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL)
						||tipoProductoId[0].equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL))){
			boUITitulos.listarSubastas(idUnidadInversion);
		}else{
			boUITitulos.listarTitulosPorID(idUnidadInversion);
		}
		
		dataSet = boUITitulos.getDataSet();
	}

	//MODIFICADO para versión infi_SICAD 26032013, CT09153
	/**
	 * Lista las unidades de inversión pque cumplan con el status indicado y por los tipos de instrumento específicos
	 * 
	 * @param status
	 *            status de la unidad de inversión. Estan definidas en la constantes UnidadInversionConstantes
	 * @param tipoInstrumento
	 *            tipos de instrumentos que serán filtrados (SUBASTA,SUBASTA_C,INVENTARIO,INVENTARIO_C) ConstantesGenerales. Si se envia null no se toma en cuenta el tipo de instrumento
	 * */
	public void listaPorStatus(String[] status, String[] tipoInstrumento, String... tipoProducto) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("select s.undinv_status, u.undinv_id, undinv_nombre, undinv_serie from INFI_TB_106_UNIDAD_INVERSION u,INFI_TB_117_UNDINV_STATUS s, INFI_TB_101_INST_FINANCIEROS inf ");
		sql.append("where u.undinv_status=s.undinv_status and u.undinv_status in (");
		for (int i = 0; i < status.length; i++) {
			sql.append("'").append(status[i]).append("'");
			if (i < status.length - 1)
				sql.append(", ");
		}
		sql.append(") ");
		sql.append("and u.insfin_id = inf.insfin_id ");

		// Verifica si se envian instrumentos financieros específicos
		if (tipoInstrumento != null) {
			sql.append("and inf.INSFIN_FORMA_ORDEN in(");
			for (int i = 0; i < tipoInstrumento.length; i++) {
				sql.append("'").append(tipoInstrumento[i]).append("'");
				if (i < tipoInstrumento.length - 1)
					sql.append(", ");
			}
			sql.append(") ");
		}
		
		//Verifica por tipo de producto
		if(tipoProducto!=null && tipoProducto.length > 0){
			
			sql.append("and inf.TIPO_PRODUCTO_ID in (");
			for (int i = 0; i < tipoProducto.length; i++) {
				sql.append("'").append(tipoProducto[i]).append("'");
				if (i < tipoProducto.length - 1)
					sql.append(", ");
			}
			sql.append(") ");
		}
		
		sql.append("order by undinv_nombre");
		
		dataSet = db.get(dataSource, sql.toString());
	}

	/**
	 * Buscamos la fecha de liquidacion, cierre, emision y adjudicacion correspondientes a una unidad de inversion especifica
	 * 
	 * @param idUnidadInversion
	 * @throws Exception
	 */
	public void listaTodasFechas(long idUnidadInversion) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql
				.append(
						"select initcap(to_char(undinv_fe_emision,'yyyy')) as e_anio, initcap(to_char(undinv_fe_emision,'mm')) as e_mes, initcap(to_char(undinv_fe_emision,'month')) as e_nombre_mes, initcap(to_char(undinv_fe_emision,'dd')) as e_dia,initcap(to_char(undinv_fe_adjudicacion,'yyyy')) as a_anio, initcap(to_char(undinv_fe_adjudicacion,'mm')) as a_mes, initcap(to_char(undinv_fe_adjudicacion,'month')) as a_nombre_mes, initcap(to_char(undinv_fe_adjudicacion,'dd')) as a_dia,initcap(to_char(undinv_fe_liquidacion,'yyyy')) as l_anio, initcap(to_char(undinv_fe_liquidacion,'mm')) as l_mes, initcap(to_char(undinv_fe_liquidacion,'month')) as l_nombre_mes, initcap(to_char(undinv_fe_liquidacion,'dd')) as l_dia,initcap(to_char(undinv_fe_cierre,'yyyy')) as c_anio, initcap(to_char(undinv_fe_cierre,'mm')) as c_mes, initcap(to_char(undinv_fe_cierre,'month')) as c_nombre_mes, initcap(to_char(undinv_fe_cierre,'dd')) as c_dia from INFI_TB_106_UNIDAD_INVERSION where undinv_id=")
				.append(idUnidadInversion);

		dataSet = db.get(dataSource, sql.toString());
	}

	/**
	 * Lista el vehiculo, monto y precio asociado a una unidad de inversion que sean estatus enviada o registrada y donde la transaccion sea toma de orden o toma de orden cartera propia
	 * 
	 * @param idUnidad
	 *            unidad de inversi&oacute;n
	 * */
	//Cambio de consulta en Requerimiento SICAD_2 NM11383
	public void listarVehiculo(String unidad) throws Exception {
		StringBuffer sql = new StringBuffer();
		//StringBuffer filtro = new StringBuffer("");

		/*sql.append("select o.ordene_veh_col,v.vehicu_nombre,count(o.ordene_id) as ordenes, ");
		sql.append(" '' as para_enviar ,count(oen.ordene_id) as enviadas, o.uniinv_id, ");
		sql.append(" sum(o.ORDENE_PED_MONTO) as ordene_ped_monto ");
		sql.append(" from INFI_TB_204_ORDENES o inner join INFI_TB_012_TRANSACCIONES t on t.transa_id=o.transa_id ");
		sql.append(" inner join INFI_TB_018_VEHICULOS v on v.vehicu_id=o.ordene_veh_col ");
		sql.append(" left join INFI_TB_204_ORDENES oen on o.ordene_id = oen.ordene_id and oen.ordsta_id='");
		sql.append(StatusOrden.ENVIADA);
		sql.append("' where (t.transa_id='");
		sql.append(TransaccionNegocio.TOMA_DE_ORDEN);
		sql.append("' OR t.transa_id='");
		sql.append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA);
		sql.append("') and o.uniinv_id=").append(unidad);
		sql.append(" and o.ordsta_id not in('").append(StatusOrden.CANCELADA).append("','").append(StatusOrden.PENDIENTE).append("')");
		sql.append(" group by o.ordene_veh_col,v.vehicu_nombre,o.uniinv_id ");*/
		
		sql.append("select o.ordene_veh_col,v.vehicu_nombre,count(o.ordene_id) as ordenes,'' as para_enviar, ");
		sql.append("nvl((select decode(b.ordsta_id,'ENVIADA',count(1),0) from   INFI_TB_204_ORDENES b ");
		sql.append("where  b.uniinv_id = o.uniinv_id and    b.ordsta_id ='").append(StatusOrden.ENVIADA).append("'  group by b.ordsta_id),0) as enviadas,");
		sql.append("o.uniinv_id,sum(o.ORDENE_PED_MONTO) as ordene_ped_monto ");
		sql.append("from INFI_TB_204_ORDENES o,INFI_TB_012_TRANSACCIONES t,INFI_TB_018_VEHICULOS v ");
		sql.append("where t.transa_id=o.transa_id  and   v.vehicu_id=o.ordene_veh_col ");
		sql.append("and (t.transa_id='").append(TransaccionNegocio.TOMA_DE_ORDEN).append("' OR t.transa_id='").append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA).append("')");
		sql.append("and o.uniinv_id=").append(unidad);
		sql.append(" and o.ordsta_id not in('").append(StatusOrden.CANCELADA).append("','").append(StatusOrden.PENDIENTE).append("')");
		sql.append("group by o.ordene_veh_col,v.vehicu_nombre,o.uniinv_id");
//		System.out.println("sql ordenes ui "+ sql);
		dataSet = db.get(dataSource, sql.toString());
		System.out.println(sql);
	}

	/**
	 * Lista el vehiculo, monto y precio asociado a una unidad de inversion que sean estatus enviada o registrada y donde la transaccion sea toma de orden o toma de orden cartera propia
	 * 
	 * @param idUnidad
	 *            unidad de inversi&oacute;n
	 * */
	public void listarUnidadAdjudicar(String unidad) throws Exception {
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		sql.append("select v.vehicu_nombre,o.ordene_veh_col,o.uniinv_id,SUM(o.ordene_ped_monto) as ordene_ped_monto, SUM(o.ordene_ped_precio) as ordene_ped_precio,count(o.ordene_id) as ordenes,(select count(cad.ordene_id) from INFI_TB_804_CONTROL_ARCH_DET cad, INFI_TB_803_CONTROL_ARCHIVOS ca where cad.ejecucion_id=ca.ejecucion_id and ca.undinv_id=").append(unidad).append(
				" and ca.ejecucion_id in (select max(ca.ejecucion_id) from INFI_TB_803_CONTROL_ARCHIVOS ca where ca.undinv_id=").append(unidad).append(" and in_recepcion=0)) as enviadas, '' as para_enviar from INFI_TB_204_ORDENES o, INFI_TB_012_TRANSACCIONES t, INFI_TB_018_VEHICULOS v WHERE (ordsta_id='");
		sql.append(StatusOrden.ENVIADA);
		sql.append("' OR ordsta_id='");
		sql.append(StatusOrden.REGISTRADA);
		sql.append("') and t.transa_id=o.transa_id and (t.transa_id='");
		sql.append(TransaccionNegocio.TOMA_DE_ORDEN);
		sql.append("' OR t.transa_id='");
		sql.append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA);
		sql.append("') and v.vehicu_id=o.ordene_veh_col ");
		if (unidad != null) {
			filtro.append(" and o.uniinv_id=").append(unidad);
		}
		sql.append(filtro);
		sql.append(" GROUP BY v.vehicu_nombre, o.ordene_veh_col, o.uniinv_id");
		dataSet = db.get(dataSource, sql.toString());
	}

	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Permite verificar si las asociaciones han sido registradas correctamente; 1.- Al menos un Titulo, donde la suma de los porcentajes = UMI de la Unidad de Inversion 2.- Al menos un Blotter: a.- seriales contables registrados b.- periodos en el rango establecido por feEmision y feCierre 3:- Al menos una Empresa, con el rol y los datos del Contacto registrados
	 * Modificado TTS-504-SIMADI Efectivo Taquilla NM25287 21/08/2015. Validar campos para manejo de efectivo
	 * @param idUnidadInversion
	 *            : identificador de la Unidad de Inversion
	 * @return true o false si se cumplieron las condiciones
	 * @throws Exception
	 */
	public boolean dataCompleta(long idUnidadInversion,String... invocacion) throws Exception {

		StringBuffer sqlSB = new StringBuffer();
		String instFinanciero=getInstrumentoFinancieroPorUI(idUnidadInversion);
		String localizaionInv="";

		sqlSB.append("select ");
		sqlSB.append("(select count(*) from INFI_TB_108_UI_TITULOS tit where tit.undinv_id = undinv.undinv_id) as cantidad_titulos, ");
		sqlSB.append("(select count(*) from INFI_TB_107_UI_BLOTTER blo where blo.undinv_id = undinv.undinv_id) as cantidad_blotters, ");
		sqlSB.append("(select count(*) from INFI_TB_109_UI_EMPRESAS empres where empres.undinv_id = undinv.undinv_id) as cantidad_empresas, ");
		sqlSB.append("(select count(*) from INFI_TB_107_UI_BLOTTER blo where blo.undinv_id = undinv.undinv_id ");
		sqlSB.append("and uiblot_pedido_fe_ini between undinv_fe_emision and undinv_fe_cierre ");
		sqlSB.append("and uiblot_pedido_fe_fin between undinv_fe_emision and undinv_fe_cierre) as cantidad_blotter_fecha, ");
		sqlSB.append("(select count(*) from( ");
		sqlSB.append("select case when roles_id is not null and uiempr_contacto_nom is not null and uiempr_contacto_tlf is not null then 1 else 0 end as valores ");
		sqlSB.append("from INFI_TB_109_UI_EMPRESAS empres where empres.undinv_id = ").append(idUnidadInversion + " ").append(")) as cantidad_empresa_completas, ");
		sqlSB.append("(select sum(uititu_porcentaje) from INFI_TB_108_UI_TITULOS tit where tit.undinv_id = undinv.undinv_id) as porcentaje_registrado, ");
		sqlSB.append("(select count(*) from INFI_TB_111_UI_BLOTTER_RANGOS uiblotr where uiblotr.undinv_id = undinv.undinv_id) as indicador_parametros, ");
		sqlSB.append("((select case when insfin_forma_orden='").append(ConstantesGenerales.INST_TIPO_SUBASTA).append("' OR insfin_forma_orden='").append(ConstantesGenerales.INST_TIPO_SUBASTA_COMPETITIVA).append("' OR insfin_forma_orden='").append(ConstantesGenerales.INST_TIPO_SITME).append("' OR insfin_forma_orden='").append(ConstantesGenerales.INST_FINANCIERO_SIMADI_TAQUILLA);
		sqlSB.append("' then '7' when insfin_forma_orden='").append(ConstantesGenerales.INST_TIPO_INVENTARIO).append("' OR insfin_forma_orden='").append(ConstantesGenerales.INST_TIPO_INVENTARIO_CON_PRECIO).append(
				"' then '2' end valor from INFI_TB_101_INST_FINANCIEROS fi where undinv.insfin_id=fi.insfin_id)*(select count(distinct(p.tipper_id)) from INFI_TB_200_TIPO_PERSONAS p, INFI_TB_111_UI_BLOTTER_RANGOS br where p.tipper_id=br.tipper_id and undinv.undinv_id=br.undinv_id)) as cantidad_doc_asociar, ");
		sqlSB.append("(select count(uidoc_unico) from INFI_TB_115_UI_DOC uo where undinv.undinv_id=uo.undinv_id) as cantidad_doc_asociados ");
		sqlSB.append("from INFI_TB_106_UNIDAD_INVERSION undinv ");
		sqlSB.append("where undinv_id = ");
		sqlSB.append(idUnidadInversion + " ");

		//System.out.println("SQL dataCompleta: "+sqlSB.toString());

		dataSet = db.get(dataSource, sqlSB.toString());
		dataSet.next();

		int iTitulos = new Integer(dataSet.getValue("cantidad_titulos")).intValue();
		int iBlotter = new Integer(dataSet.getValue("cantidad_blotters")).intValue();
		int iBlotterParametros = new Integer(dataSet.getValue("indicador_parametros")).intValue();
		int iEmpresas = new Integer(dataSet.getValue("cantidad_empresas")).intValue();
		int iTotalDocumentos = new Integer(dataSet.getValue("cantidad_doc_asociar")).intValue();
		// int iTotalDocumentos=1;
		int iDocAsociados = new Integer(dataSet.getValue("cantidad_doc_asociados")).intValue();

		BigDecimal porcentajeRegistrado = new BigDecimal(100);
		if (dataSet.getValue("porcentaje_registrado") != null)
			porcentajeRegistrado = new BigDecimal(dataSet.getValue("porcentaje_registrado")).setScale(0, BigDecimal.ROUND_UP);
		int iBlotterFechas = new Integer(dataSet.getValue("cantidad_blotter_fecha")).intValue();
		// int iBlotterDefecto= new
		// Integer(dataSet.getValue("cantidad_blotter_defecto")).intValue();
		int iEmpresasCompletas = new Integer(dataSet.getValue("cantidad_empresa_completas")).intValue();

		dataSet = new DataSet();
		dataSet.append("campo_mensaje", java.sql.Types.VARCHAR);
		dataSet.append("mensaje", java.sql.Types.VARCHAR);
		for (String element : invocacion){
			if(element.equals(ConstantesGenerales.LOCALIZACION_INVOCACION_EDIT_UI)){
				localizaionInv=element;
			}
		}
		// Asociaciones requeridas : Tiulos, Blotters y Empresas
		if (iTitulos == 0) {
			if(localizaionInv.equals(ConstantesGenerales.LOCALIZACION_INVOCACION_EDIT_UI) && ((!(instFinanciero.equals(ConstantesGenerales.INST_FINANCIERO_SITME_CLAVENET_E)))&& (!(instFinanciero.equals(ConstantesGenerales.INST_FINANCIERO_SITME_CLAVENET_P))))){
			dataSet.addNew();
			dataSet.setValue("campo_mensaje", "Titulos / Subastas");
			dataSet.setValue("mensaje", "La Unidad de Inversion requiere de al menos uno");
			}
		}
		if (iBlotter == 0) {
			dataSet.addNew();
			dataSet.setValue("campo_mensaje", "Blotters");
			dataSet.setValue("mensaje", "La Unidad de Inversion requiere de al menos uno");
		}
		if (iEmpresas == 0) {
			dataSet.addNew();
			dataSet.setValue("campo_mensaje", "Empresas");
			dataSet.setValue("mensaje", "La Unidad de Inversion requiere de al menos una");
		}
		if (iDocAsociados == 0) {
			if(localizaionInv.equals(ConstantesGenerales.LOCALIZACION_INVOCACION_EDIT_UI) && ((!(instFinanciero.equals(ConstantesGenerales.INST_FINANCIERO_SITME_CLAVENET_E)))&& (!(instFinanciero.equals(ConstantesGenerales.INST_FINANCIERO_SITME_CLAVENET_P))))){
			dataSet.addNew();
			dataSet.setValue("campo_mensaje", "Documentos");
			dataSet.setValue("mensaje", "La Unidad de Inversion requiere la asociacion de Documentos");
			}
		}

		// Asociaciones completas
		if (iTitulos > 0) {
			if (porcentajeRegistrado.doubleValue() != 100) {
				dataSet.addNew();
				dataSet.setValue("campo_mensaje", "Titulos");
				dataSet.setValue("mensaje", "Los porcentajes de las Titulos asociados no suman 100%");
			}
		}
		if (iBlotter > 0) {
			if (iBlotter != iBlotterFechas) {
				dataSet.addNew();
				dataSet.setValue("campo_mensaje", "Blotters");
				dataSet.setValue("mensaje", "Hay Blotters asociados cuyos Periodos de Toma de Ordenes estan fuera del rango de fecha de la Unidad de Inversion");
			}
			if (iBlotterParametros == 0) {
				dataSet.addNew();
				dataSet.setValue("campo_mensaje", "Blotters");
				dataSet.setValue("mensaje", "Debe registrar los Parametros por Tipo de Persona para los Blotter");
			}
			if (iDocAsociados < iTotalDocumentos && iDocAsociados > ConstantesGenerales.FALSO) {
				dataSet.addNew();
				dataSet.setValue("campo_mensaje", "Documentos");
				dataSet.setValue("mensaje", "Debe asociar los Documentos por Tipo de Persona para los Blotter");
			}
		}
		if (iEmpresas > 0) {
			if (iEmpresas != iEmpresasCompletas) {
				dataSet.addNew();
				dataSet.setValue("campo_mensaje", "Empresas");
				dataSet.setValue("mensaje", "Hay Empresas asociadas que le falta informacion");
			}
		}
		
		if (dataSet.count() == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Filtra las unidades de inverison si el argumento opcion es 1 muesdtra solo las registradas si es 2 muestra todas este metodo lo usamos para consulta y publicacion de unidades de inversion
	 */
	public void listarPorFiltro(String idInstrumentoFinanciero, Date fechaEmisionDesde, Date fechaEmisionHasta, String idEmisor, String UnidadInversion, int opcion) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT undinv_id as idUnidadInversion, undinv_nombre as nombreUnidadInversion, undinv_fe_emision as fechaEmisionUI, undinv_fe_liquidacion as fechaLiquidaUI, undinv_fe_cierre as fechaCierreUI,");
		sql.append("undinv_status_descripcion as descrUIStatus, insfin_descripcion as descrInstrumento, insfin_forma_orden as tipoInstrumento, emp.empres_nombre ");
		sql.append("from INFI_TB_106_UNIDAD_INVERSION ui ");
		sql.append("inner join INFI_TB_117_UNDINV_STATUS uista on uista.undinv_status = ui.undinv_status ");
		sql.append("inner join INFI_TB_101_INST_FINANCIEROS if on if.insfin_id = ui.insfin_id ");
		sql.append("inner join INFI_TB_016_EMPRESAS emp on emp.empres_id = ui.empres_id ");
		sql.append("where ui.undinv_fe_emision between to_date('");
		sql.append(sdIODate.format(fechaEmisionDesde)).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("') ");
		sql.append("and to_date('");
		sql.append(sdIODate.format(fechaEmisionHasta)).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("') ");
		if (opcion == 1) {
			sql.append(" and ui.undinv_status='").append(UnidadInversionConstantes.UISTATUS_REGISTRADA).append("'");
		}
		if (idInstrumentoFinanciero != null) {
			sql.append("and upper(ui.insfin_id) = upper('").append(idInstrumentoFinanciero).append("') ");
		}
		if (idEmisor != null) {
			sql.append("and upper(ui.empres_id) = upper('").append(idEmisor).append("') ");
		}
		if (UnidadInversion != null) {
			sql.append("and ui.undinv_id=").append(UnidadInversion);
		}
		dataSet = db.get(dataSource, sql.toString());

	}

	/**
	 * Modifica el status de una unidad al estatus que traiga el objeto
	 * 
	 * @param idUnidadInversion
	 * @param status
	 * @throws Exception
	 */
	public void modificarStatus(UnidadInversion unidadInversion) throws Exception {

		StringBuffer sqlSB = new StringBuffer();

		sqlSB.append("update INFI_TB_106_UNIDAD_INVERSION set ");
		sqlSB.append("undinv_status = '").append(unidadInversion.getIdUIStatus()).append("' ");
		sqlSB.append("where undinv_id = ");
		sqlSB.append(unidadInversion.getIdUnidadInversion());

		db.exec(dataSource, sqlSB.toString());
	}

	/**
	 * Modifica el status de una unidad al estatus que traiga el objeto
	 * 
	 * @param idUnidadInversion
	 * @param status
	 * @throws Exception
	 */
	public void listarUnidadPicklist(String filtro) throws Exception {

		StringBuffer sb = new StringBuffer();
		sb.append("select infi_tb_106_unidad_inversion.undinv_id,undinv_nombre,infi_tb_106_unidad_inversion.UNDINV_SERIE from infi_tb_106_unidad_inversion where 1=1 ");
		if (filtro != null) {
			sb.append(filtro);
		}
		sb.append(" order by undinv_nombre");
		dataSet = db.get(dataSource, sb.toString());
	}

	/**
	 * Lista los tipos de persona para una unidad de inversi&oacute;n y un blotter
	 * 
	 * @param unidadInversion
	 * @param bloter
	 * @throws Exception
	 */
	public void listarRangosTiposDePersonaWS(long unidadInversion, String bloter) throws Exception {

		StringBuffer sb = new StringBuffer();
		sb.append("select * from INFI_TB_111_UI_BLOTTER_RANGOS where undinv_id=");
		sb.append(unidadInversion);
		sb.append(" and bloter_id='").append(bloter).append("'");
		dataSet = db.get(dataSource, sb.toString());
	}

	/**
	 * Lista el blotter para un usuario / bloter asociado a una unidad de inversión
	 * 
	 * @param ParametrosTomaDeOrden
	 *            parametrosTomaOrden
	 * @return String bloterId
	 * @throws Exception
	 * @throws Throwable
	 */
	public String listarBloterRed(ParametrosTomaDeOrden parametrosTomaOrden) throws Exception, Throwable {
		StringBuffer sb = new StringBuffer();
		String bloterId = "";
		sb.append("select * from INFI_TB_107_UI_BLOTTER left join INFI_TB_102_BLOTER on INFI_TB_107_UI_BLOTTER.BLOTER_ID=INFI_TB_102_BLOTER.BLOTER_ID left join INFI_TB_104_BLOTER_USUARIOS on INFI_TB_102_BLOTER.BLOTER_ID=INFI_TB_104_BLOTER_USUARIOS.BLOTER_ID where INFI_TB_102_BLOTER.bloter_in_red=0 and UNDINV_ID=");
		sb.append(parametrosTomaOrden.getIdUnidadInversion());
		sb.append(" and bloter_id=").append(parametrosTomaOrden.getIdBlotter());
		sb.append(" and userid='").append(parametrosTomaOrden.getCredencialesDeUsuario().getUsuarioCanal()).append("'");

		dataSet = db.get(dataSource, sb.toString());

		if (dataSet.count() > 0) {
			dataSet.first();
			dataSet.next();
			bloterId = dataSet.getValue("bloter_id");
		}
		if (bloterId.equals("")) {
			throw new Throwable("El usuario no posee un bloter asociado para la Unidad de inversión a ser procesada");
		}
		return bloterId;

	}

	/**
	 * Lista la informacion de horarios desde hasta para el bloter especifico
	 * 
	 * @param unInv
	 * @throws Exception
	 */
	public void listarBloterPorUiWS(long unInv) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select infi_tb_102_bloter.id_agrupacion,infi_tb_102_bloter.BLOTER_DESCRIPCION,");
		sql.append("to_char(UIBLOT_PEDIDO_FE_INI,'").append(ConstantesGenerales.FORMATO_FECHA_WEB_SERVICES).append("')fecha_desde_toma_orden,");
		sql.append("to_char(UIBLOT_HORARIO_DESDE,'").append(ConstantesGenerales.FORMATO_HORA_WEB_SERVICES).append("')hora_desde_toma_orden,");
		sql.append("to_char(UIBLOT_PEDIDO_FE_FIN,'").append(ConstantesGenerales.FORMATO_FECHA_WEB_SERVICES).append("')fecha_hasta_toma_orden,");
		sql.append("to_char(UIBLOT_HORARIO_HASTA,'").append(ConstantesGenerales.FORMATO_HORA_WEB_SERVICES).append("')hora_hasta_toma_orden,");
		sql.append("to_char(UIBLOT_HORARIO_DESDE_ULT_DIA,'").append(ConstantesGenerales.FORMATO_HORA_WEB_SERVICES).append("')hora_desde_ultimo_dia,");
		sql.append("to_char(UIBLOT_HORARIO_HASTA_ULT_DIA,'").append(ConstantesGenerales.FORMATO_HORA_WEB_SERVICES).append("')hora_hasta_ultimo_dia ");
		sql.append("from  infi_tb_107_ui_blotter left join infi_tb_102_bloter on infi_tb_107_ui_blotter.BLOTER_ID=infi_tb_102_bloter.BLOTER_ID where infi_tb_107_ui_blotter.UNDINV_ID=");
		sql.append(unInv);
		sql.append(" order by bloter_descripcion");
		dataSet = db.get(dataSource, sql.toString());
	}

	/**
	 * Lista los titulo asociados a la unidad de inversi&oacute;n seleccionada para los servicios WEB
	 * 
	 * @param unInv
	 * @throws Exception
	 */
	public void listartitulosPorUiWS(long unInv) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select infi_tb_100_titulos.titulo_id,infi_tb_100_titulos.TITULO_MONEDA_DEN,");
		sql.append("to_char(infi_tb_100_titulos.TITULO_FE_EMISION,'").append(ConstantesGenerales.FORMATO_FECHA_WEB_SERVICES).append("')titulo_fe_emision,");
		sql.append("to_char(infi_tb_100_titulos.TITULO_FE_VENCIMIENTO,'").append(ConstantesGenerales.FORMATO_FECHA_WEB_SERVICES);
		sql.append("')titulo_fe_vencimiento,INFI_TB_108_UI_TITULOS.UITITU_PORCENTAJE from INFI_TB_108_UI_TITULOS left join infi_tb_100_titulos on trim(INFI_TB_108_UI_TITULOS.TITULO_ID)=trim(infi_tb_100_titulos.TITULO_ID) where undinv_id=");
		sql.append(unInv);

		dataSet = db.get(dataSource, sql.toString());
	}

	/**
	 * Lista las unidades de inversion asociadas a un bloter especifico, para los servicios web
	 * 
	 * @param bloterId
	 * @param statusUnidad
	 * @param monedaID
	 * @throws Throwable
	 */
	public void listarUnidadesBloterWS(String bloterId, String statusUnidad, String monedaID) throws Throwable {

		try {
			StringBuffer sql = new StringBuffer();
			sql
					.append("select distinct undinv.undinv_id,undinv.undinv_multiplos, uiblot.bloter_id,undinv_nombre, undinv_emision, undinv_serie, undinv_descripcion, undinv_tasa_cambio, undinv_precio_minimo, to_char(undinv_fe_emision, 'ddMMyyyy') as undinv_fe_emision, to_char(undinv_fe_adjudicacion, 'ddMMyyyy') as undinv_fe_adjudicacion, to_char(undinv_fe_liquidacion, 'ddMMyyyy') undinv_fe_liquidacion,UNDINV_UMI_INV_DISPONIBLE, undinv_in_vta_empleados, undinv_in_recompra_neteo, undinv_umi_inv_total, undinv_umi_inv_mto_min, undinv_umi_inv_mto_max, undinv_umi_um_cant_min, undinv_umi_um_cant_max, insfin_descripcion, undinv_status, undinv_status_descripcion, moneda_descripcion, tppeva_id,INSFIN_FORMA_ORDEN from INFI_TB_107_UI_BLOTTER uiblot left join INFI_TB_106_UNIDAD_INVERSION undinv on uiblot.undinv_id = undinv.undinv_id left join INFI_TB_101_INST_FINANCIEROS insfin on insfin.insfin_id = undinv.insfin_id left join INFI_TB_117_UNDINV_STATUS undsta on undsta.undinv_status = undinv.undinv_status left join INFI_VI_MONEDAS mon on mon.moneda_id = undinv.moneda_id left join INFI_TB_102_BLOTER blo on (blo.bloter_id = uiblot.bloter_id) ");
			sql.append("where 1=1");

			if (statusUnidad != null && !statusUnidad.equals(""))
				sql.append(" and lower(undinv.undinv_status) in ('").append(statusUnidad).append("')");
			if (monedaID != null && !monedaID.equals(""))
				sql.append(" and mon.moneda_id='").append(monedaID).append("'");

			sql.append(" and uiblot.bloter_id='").append(bloterId).append("' ");
			sql.append(" and (").append(formatearFechaBDActual()).append(" between undinv.undinv_fe_emision and undinv_fe_cierre ) ");

			dataSet = db.get(dataSource, sql.toString());

			if (dataSet.count() == 0)
				throw new Throwable("No existen unidades para el Bloter asociado al Usuario");
		} catch (Exception e) {
			throw e;
		}

	}// fin listarUnidadesBloterWS

	/**
	 * Verifica si una unidad de inversion es para un tipo de instrumento determinado
	 * 
	 * @param idUnidadInversion
	 * @param tipoInstrumento
	 * @return true si es el tipo de instrumento, false en caso contrario
	 * @throws Exception
	 */
	public boolean esTipoInstrumento(long idUnidadInversion, String[] tipoInstrumento) throws Exception {
		boolean es;

		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT undinv_id FROM INFI_TB_106_UNIDAD_INVERSION ui, INFI_TB_101_INST_FINANCIEROS ins where undinv_id=").append(idUnidadInversion);
		sql.append(" and ui.insfin_id = ins.insfin_id");
		sql.append(" and ins.insfin_forma_orden in (");

		for (int i = 0; i < tipoInstrumento.length; i++) {

			if (i + 1 == tipoInstrumento.length)
				sql.append("'").append(tipoInstrumento[i]).append("')");
			else
				sql.append("'").append(tipoInstrumento[i]).append("',");
		}

		dataSet = db.get(dataSource, sql.toString());

		if (dataSet.next()) {
			es = true;
		} else {
			es = false;
		}
		System.out.println("esTipoInstrumento "+sql);
		return es;

	}

	/**
	 * Verifica si la unidad de inversi&oacute;n posee ordenes adjudicadas
	 * 
	 * @param idUnidadInversion
	 * @return true si posee ordenes adjudicadas, false en caso contrario
	 * @throws Exception
	 */
	public boolean unidadConOrdenesAdjudicadas(long idUnidadInversion) throws Exception {
		boolean adjudicada;

		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ordene_id FROM INFI_TB_204_ORDENES where uniinv_id=").append(idUnidadInversion);
		sql.append(" and (ordsta_id = '").append(StatusOrden.ADJUDICADA).append("'");
		sql.append(" or ordsta_id = '").append(StatusOrden.ADJUDICACION_CERRADA).append("'");
		sql.append(" or ordsta_id = '").append(StatusOrden.ENVIADA).append("'");
		sql.append(" or ordsta_id = '").append(StatusOrden.ENVIO_CERRADO).append("'");
		sql.append(")");

		dataSet = db.get(dataSource, sql.toString());

		if (dataSet.next()) {
			adjudicada = true;
		} else {
			adjudicada = false;
		}

		return adjudicada;

	}

	/**
	 * Suma el monto de todas las ordenes pertenecientes a la unidad de inversión,<br>
	 * que se encuentren en status adjudicada y el monto otorgado mayor a 0
	 * 
	 * @param unidadInversion
	 * @return
	 * @throws Exception
	 */
	public BigDecimal montoTotalBCV(long unidadInversion) throws Exception {

		BigDecimal montoTotal = new BigDecimal(0);
		StringBuffer sb = new StringBuffer();
		sb.append("select sum(ordene_ped_total)monto from infi_tb_204_ordenes where UNIINV_ID=");
		sb.append(unidadInversion);
		sb.append(" and ordsta_id='").append(StatusOrden.ADJUDICADA).append("'");

		dataSet = db.get(dataSource, sb.toString());
		dataSet.first();
		dataSet.next();
		montoTotal = montoTotal.add(dataSet.getValue("monto") == null || dataSet.getValue("monto").equals("") ? new BigDecimal(0) : new BigDecimal(dataSet.getValue("monto")));

		return montoTotal;
	}

	/**
	 * 
	 * @param unidadInversion
	 * @throws Exception
	 */
	public void actualizarStatusUnidadInversionBCV(long unidadInversion, int pagoBcv) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("update INFI_TB_106_UNIDAD_INVERSION set PAGO_IN_BCV=");
		sb.append(pagoBcv);
		sb.append(" where UNDINV_ID=");
		sb.append(unidadInversion);

		db.exec(dataSource, sb.toString());
	}

	/** Lista todos los documento asociados a la unidad de inversion */
	public void listarDocumentosUnidad(Integer unidadInversion) throws Exception {
		StringBuffer sql = new StringBuffer();

		sql.append("select * from INFI_TB_115_UI_DOC where undinv_id=");
		sql.append(unidadInversion);
		dataSet = db.get(dataSource, sql.toString());
	}

	/**
	 * Conciliacion de unidad de inversion, mostrando los blotter asociados con sus transacciones
	 * 
	 * @param unidadInversion
	 * @param blotter
	 * @throws Exception
	 */
	public String  listarConciliacionCobranza(long unidadInversion, String blotter) throws Exception {

		StringBuilder sql = new StringBuilder();

		sql.append("select distinct(infi_tb_102_bloter.bloter_descripcion),(select sum(ordene_adj_monto) ");
		sql.append("from infi_tb_204_ordenes a where a.uniinv_id=").append(unidadInversion);
		sql.append(" AND A.ordsta_id in ('");
		sql.append(StatusOrden.ADJUDICADA);
		sql.append("','");
		sql.append(StatusOrden.LIQUIDADA);
		sql.append("','");
		sql.append(StatusOrden.PROCESO_ADJUDICACION);
		sql.append("') and a.bloter_id=infi_tb_204_ordenes.bloter_id)adjudicado,");
		sql.append("((select decode(sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION),null,0,");
		sql.append("sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION))as monto_operacion from infi_tb_207_ordenes_operacion ");
		sql.append("left join infi_tb_204_ordenes a on a.ordene_id=infi_tb_207_ordenes_operacion.ordene_id ");
		sql.append("where infi_tb_207_ordenes_operacion.STATUS_OPERACION in('").append(ConstantesGenerales.STATUS_APLICADA);
		sql.append("') and trnf_tipo='").append(TransaccionFinanciera.DEBITO);
		sql.append("' and ").append("a.bloter_id=infi_tb_204_ordenes.bloter_id and a.uniinv_id=").append(unidadInversion);
		sql.append(" AND A.ordsta_id in ('");
		sql.append(StatusOrden.ADJUDICADA);
		sql.append("','");
		sql.append(StatusOrden.LIQUIDADA);
		sql.append("','");
		sql.append(StatusOrden.PROCESO_ADJUDICACION);
		sql.append("'))-(select ").append("decode(sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION),null,0,sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION))as monto_operacion from infi_tb_207_ordenes_operacion ");
		sql.append("left join infi_tb_204_ordenes a on a.ordene_id=infi_tb_207_ordenes_operacion.ordene_id ");
		sql.append("where infi_tb_207_ordenes_operacion.STATUS_OPERACION in('").append(ConstantesGenerales.STATUS_APLICADA);
		sql.append("') and trnf_tipo='").append(TransaccionFinanciera.CREDITO);
		sql.append("' and ").append("a.bloter_id=infi_tb_204_ordenes.bloter_id and a.uniinv_id=");
		sql.append(unidadInversion).append(" AND A.ordsta_id in ('");
		sql.append(StatusOrden.ADJUDICADA);
		sql.append("','");
		sql.append(StatusOrden.LIQUIDADA);
		sql.append("','");
		sql.append(StatusOrden.PROCESO_ADJUDICACION);
		sql.append("')))cobrado,");
		sql.append("((select decode(sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION),null,0,sum(infi_tb_207_ordenes_operacion.MONTO_OPERACION))as monto_operacion from infi_tb_207_ordenes_operacion ");
		sql.append("left join infi_tb_204_ordenes a on a.ordene_id=infi_tb_207_ordenes_operacion.ordene_id ");
		sql.append("where infi_tb_207_ordenes_operacion.STATUS_OPERACION in('").append(ConstantesGenerales.STATUS_EN_ESPERA).append("','");
		sql.append(ConstantesGenerales.STATUS_RECHAZADA).append("') and trnf_tipo='").append(TransaccionFinanciera.DEBITO).append("' ");
		sql.append("and a.bloter_id=infi_tb_204_ordenes.bloter_id and a.uniinv_id=");
		sql.append(unidadInversion).append(" AND A.ordsta_id in ('");
		sql.append(StatusOrden.ADJUDICADA);
		sql.append("','");
		sql.append(StatusOrden.LIQUIDADA);
		sql.append("','");
		sql.append(StatusOrden.PROCESO_ADJUDICACION);
		sql.append("'))+(");
		sql.append("select decode(sum(a.ORDENE_PED_TOTAL_PEND),null,0,sum(a.ORDENE_PED_TOTAL_PEND))as financiado from infi_tb_204_ordenes ");
		sql.append("a where a.bloter_id=infi_tb_204_ordenes.bloter_id and a.uniinv_id=").append(unidadInversion).append(" AND A.ordsta_id in ('");
		sql.append(StatusOrden.ADJUDICADA);
		sql.append("','");
		sql.append(StatusOrden.LIQUIDADA);
		sql.append("','");
		sql.append(StatusOrden.PROCESO_ADJUDICACION);
		sql.append("')))pendiente,");
		sql.append("(select decode(sum(a.ORDENE_PED_TOTAL_PEND),null,0,sum(a.ORDENE_PED_TOTAL_PEND))as financiado from infi_tb_204_ordenes ");
		sql.append("a where a.bloter_id=infi_tb_204_ordenes.bloter_id and a.uniinv_id=").append(unidadInversion).append(" AND A.ordsta_id in ('");
		sql.append(StatusOrden.ADJUDICADA);
		sql.append("','");
		sql.append(StatusOrden.LIQUIDADA);
		sql.append("','");
		sql.append(StatusOrden.PROCESO_ADJUDICACION);
		sql.append("'))financiado,");
		sql.append("(select decode(sum(ordene_ped_comisiones),null,0,sum(a.ordene_ped_comisiones))as comision from infi_tb_204_ordenes a ");
		sql.append("where a.uniinv_id=").append(unidadInversion);
		sql.append(" AND A.ordsta_id in ('");
		sql.append(StatusOrden.ADJUDICADA);
		sql.append("','");
		sql.append(StatusOrden.LIQUIDADA);
		sql.append("','");
		sql.append(StatusOrden.PROCESO_ADJUDICACION);
		sql.append("') and a.bloter_id=infi_tb_204_ordenes.bloter_id)comision ").append("from infi_tb_204_ordenes ");
		sql.append("left join infi_tb_102_bloter on infi_tb_204_ordenes.bloter_id=infi_tb_102_bloter.bloter_id ");
		sql.append("where infi_tb_204_ordenes.bloter_id is not null  and infi_tb_204_ordenes.uniinv_id=").append(unidadInversion);

		if (blotter != null && !blotter.equalsIgnoreCase("todos"))
			sql.append(" and infi_tb_204_ordenes.bloter_id='").append(blotter).append("'");
		dataSet = db.get(dataSource, sql.toString());
		return sql.toString();
	}

	/**
	 * Lista las unidades de inversion que se encuentran en status adjudicadas o aquellas que sean de tipo inventario/inventario_cp que se encuentren publicadas
	 * 
	 * @throws Exception
	 */
	public void listarUnidadesLiquidacion() throws Exception {

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT UNDINV_ID,UNDINV_NOMBRE,UNDINV_SERIE FROM INFI_TB_106_UNIDAD_INVERSION ");
		sql.append("WHERE UNDINV_STATUS='").append(ConstantesGenerales.UNINV_ADJUDICADA).append("'");
		sql.append(" AND UNDINV_ID IN(SELECT UNIINV_ID FROM INFI_TB_204_ORDENES WHERE ORDSTA_ID='").append(StatusOrden.ADJUDICADA).append("' AND TIPO_PRODUCTO_ID='" + ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA + "')");
		sql.append(" UNION SELECT A.UNDINV_ID,A.UNDINV_NOMBRE,UNDINV_SERIE FROM INFI_TB_106_UNIDAD_INVERSION A, " + "INFI_TB_101_INST_FINANCIEROS B, INFI_TB_038_INST_FORMA_ORDEN C	WHERE " + "A.INSFIN_ID=B.INSFIN_ID AND B.INSFIN_FORMA_ORDEN = C.INSFIN_FORMA_ORDEN AND " + "C.INSFIN_FORMA_ORDEN IN ('" + ConstantesGenerales.INST_TIPO_INVENTARIO + "','"
				+ ConstantesGenerales.INST_TIPO_SUBASTA_COMPETITIVA + "','" + ConstantesGenerales.INST_TIPO_INVENTARIO_CON_PRECIO + "') AND UNDINV_STATUS='" + UnidadInversionConstantes.UISTATUS_PUBLICADA + "' " + "AND UNDINV_ID IN(SELECT UNIINV_ID FROM INFI_TB_204_ORDENES WHERE ORDSTA_ID='" + StatusOrden.ADJUDICADA + "' AND TIPO_PRODUCTO_ID='" + ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA + "')");
		dataSet = db.get(dataSource, sql.toString());

	}// fin listarUnidadesLiquidacion

	/**
	 * Lista las unidades de inversion sitme que se encuentran publicadas
	 * 
	 * @throws Exception
	 *             en caso de error
	 */
	public void listarUnidadesLiquidacionSitme() throws Exception {
		String sql = "select a.undinv_id, a.undinv_nombre, a.undinv_serie from " + "infi_tb_106_unidad_inversion a, infi_tb_101_inst_financieros b where a.insfin_id = " + "b.insfin_id and b.TIPO_PRODUCTO_ID = '" + ConstantesGenerales.ID_TIPO_PRODUCTO_SITME + "' and a.UNDINV_STATUS = '" + UnidadInversionConstantes.UISTATUS_PUBLICADA + "'"
				+ "AND b.INSFIN_DESCRIPCION NOT IN ('"+ConstantesGenerales.INST_FINANCIERO_SITME_CLAVENET_P+ "','"+ConstantesGenerales.INST_FINANCIERO_SITME_CLAVENET_E+"') AND UNDINV_ID IN(SELECT UNIINV_ID FROM INFI_TB_204_ORDENES WHERE ORDSTA_ID='" + StatusOrden.ADJUDICADA + "')";
		dataSet = db.get(dataSource, sql);

	}// fin listarUnidadesLiquidacion

	/**
	 * Verifica si la unidad de inversion es de Mercado Primario
	 * 
	 * @param unidadInversion
	 * @return
	 * @throws Exception
	 */
	public boolean esMercadoPrimario(long unidadInversion) throws Exception {

		boolean esPrimario = false;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT UNDINV_MERCADO FROM INFI_TB_106_UNIDAD_INVERSION WHERE UNDINV_ID=").append(unidadInversion);

		// Se ejecuta la consulta
		dataSet = db.get(dataSource, sql.toString());

		// Tomamos el registro del dataset
		dataSet.first();
		dataSet.next();

		// Realizamos la comparacion
		if (ConstantesGenerales.MERCADO_PRIMARIO.equalsIgnoreCase(dataSet.getValue("UNDINV_MERCADO"))) {
			esPrimario = true;
		}

		return esPrimario;
	}

	public Date fechaCierre(long unidadInversion) throws Exception {

		StringBuffer sql = new StringBuffer();
		Date fechaCierre = null;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);

		sql.append("select to_char(UNDINV_FE_CIERRE,'dd-mm-yyyy')fecha_cierre from INFI_TB_106_UNIDAD_INVERSION where UNDINV_ID=");
		sql.append(unidadInversion);
		sql.append("");

		dataSet = db.get(dataSource, sql.toString());

		if (dataSet.count() > 0) {
			dataSet.next();
			fechaCierre = simpleDateFormat.parse(dataSet.getValue("fecha_cierre"));
		}

		return fechaCierre;
	}

	/**
	 * Lista todas la unidades de inversion que deben liquidarse para que se envien al componente que las liquida
	 * 
	 * @throws Exception
	 */
	public void listarUnidadesParaLiquidar() throws Exception {

		StringBuffer sql = new StringBuffer();

		sql.append("select IN_COBRO_BATCH_LIQ,UNDINV_ID,UNDINV_NOMBRE,UNDINV_FE_LIQUIDACION,to_char(UNDINV_FE_LIQUIDACION_HORA1,'HH24')hora,to_char(UNDINV_FE_LIQUIDACION_HORA1,'MI')minuto from infi_tb_106_unidad_inversion ");
		sql.append("where UNDINV_STATUS='ADJUDICADA' and UNDINV_FE_LIQUIDACION<=sysdate");

		dataSet = db.get(dataSource, sql.toString());
	}

	// Método agregado por Gerardo Zúñiga
	// Fecha de Modificación: 11/08/2010
	/**
	 * Lista el ID y nombre de las unidades de inversión que pertenezcan a un instrumento financiero
	 * 
	 * @param descInstrumentoFinanciero
	 *            : Descripción del instrumento financiero sobre el que se buscaran las unidades de inversión
	 * 
	 * @return cantidad de registros recuperados
	 * @throws Exception
	 */

	public int listarUnidadesPorInstFinanciero(String INSFIN_ID) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ui.undinv_id AS idUnidadInversion, ui.undinv_nombre AS nombreUnidadInversion ");
		sql.append("from INFI_TB_106_UNIDAD_INVERSION ui ");
		sql.append("WHERE ui.INSFIN_ID = '").append(INSFIN_ID).append("'");
		sql.append("ORDER BY idUnidadInversion");
		dataSet = db.get(dataSource, sql.toString());
		return dataSet.count();
	}

	// Método agregado por Gerardo Zúñiga
	// Fecha de Modificación: 11/08/2010

	public String listarinsfinID(String descInstrumentoFinanciero) throws Exception {
		StringBuffer sql = new StringBuffer();
		String insfin_id = "";
		sql.append("SELECT INSFIN_ID ");
		sql.append("from INFI_TB_101_INST_FINANCIEROS ");
		sql.append("WHERE INSFIN_DESCRIPCION='").append(descInstrumentoFinanciero).append("'");
		dataSet = db.get(dataSource, sql.toString());
		if (dataSet.count() > 0) {
			dataSet.next();
			insfin_id = dataSet.getValue("INSFIN_ID");
		}
		return insfin_id;
	}

	/**
	 * Lista los datos básicos de la unidad de inversión
	 * 
	 * @param uiNombre
	 *            nombre de la unidad de inversión
	 * @throws Exception
	 */
	public void listarDatosBasicosPorNombreUI(String uiNombre) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT UNDINV_ID,UNDINV_NOMBRE,IN_COBRO_BATCH_ADJ,IN_COBRO_BATCH_LIQ,UNDINV_TASA_CAMBIO FROM INFI_TB_106_UNIDAD_INVERSION where UNDINV_NOMBRE='").append(uiNombre).append("' order by undinv_nombre");
		dataSet = db.get(dataSource, sql.toString());
	}

	/**
	 * Lista las unidades de inversión con estatus CERRADA pero que tengan ordenes asociadas con estatus en PROCESO DE ADJUDICACION
	 * 
	 * @throws Exception
	 *             en caso de error
	 */
	public void listarUnidadesParaCobroAdjBatch() throws Exception {
		String sql = "select undinv_id,undinv_nombre,undinv_serie from infi_tb_106_unidad_inversion where " + " undinv_status='" + UnidadInversionConstantes.UISTATUS_CERRADA + "' and undinv_id in " + " (select uniinv_id from infi_tb_204_ordenes where ordsta_id='" + StatusOrden.PROCESO_ADJUDICACION + "') " + " and in_cobro_batch_adj=1 ORDER BY UNDINV_NOMBRE";
		dataSet = db.get(dataSource, sql);
	}

	public void listarUnidadesParaCobroAdjBatchSitme() throws Exception {
		String sql = "select undinv_id,undinv_nombre,undinv_serie from infi_tb_106_unidad_inversion a," + " infi_tb_101_inst_financieros b where undinv_id in " + " (select uniinv_id from infi_tb_204_ordenes a, infi_tb_207_ordenes_operacion b where ordsta_id='" + StatusOrden.PROCESO_ADJUDICACION + "' and a.ordene_id = b.ordene_id and trunc(b.fecha_aplicar) <= trunc(sysdate)) "
				+ " and a.in_cobro_batch_adj=1 and a.insfin_id = b.insfin_id and b.TIPO_PRODUCTO_ID='" + ConstantesGenerales.ID_TIPO_PRODUCTO_SITME + "'";
		dataSet = db.get(dataSource, sql);
	}

	/**
	 * Lista las unidades de inversión con estatus ADJUDICADA
	 * 
	 * @throws Exception
	 *             en caso de error
	 */
	public void listarUnidadesParaCobroLiqBatch() throws Exception {
		String sql = "select undinv_id,undinv_nombre,undinv_serie from infi_tb_106_unidad_inversion a, infi_tb_101_inst_financieros b where " + " a.insfin_id = b.insfin_id and b.TIPO_PRODUCTO_ID <>'" + ConstantesGenerales.ID_TIPO_PRODUCTO_SITME + "' " + " and undinv_status='" + UnidadInversionConstantes.UISTATUS_LIQUIDADA + "' " + " and in_cobro_batch_liq=1 and undinv_id in( "
				+ " select uniinv_id from infi_tb_204_ordenes where ordsta_id='" + StatusOrden.LIQUIDADA + "' " + " and transa_id in('" + TransaccionNegocio.TOMA_DE_ORDEN + "','" + TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA + "') " + " and ordene_id in(select ordene_id from  infi_tb_207_ordenes_operacion where " + " status_operacion in('" + ConstantesGenerales.STATUS_EN_ESPERA + "','"
				+ ConstantesGenerales.STATUS_RECHAZADA + "'))" + " union " + " select uniinv_id from infi_tb_204_ordenes a, infi_tb_106_unidad_inversion b, infi_tb_101_inst_financieros c where ordsta_id='" + StatusOrden.REGISTRADA + "' " + " and b.insfin_id = c.insfin_id and c.TIPO_PRODUCTO_ID <>'" + ConstantesGenerales.ID_TIPO_PRODUCTO_SITME + "'" + " and transa_id in('"
				+ TransaccionNegocio.LIQUIDACION + "') " + " and ordene_id in(select ordene_id from  infi_tb_207_ordenes_operacion where " + " status_operacion in('" + ConstantesGenerales.STATUS_EN_ESPERA + "','" + ConstantesGenerales.STATUS_RECHAZADA + "'))" + ") ";
		System.out.println("listarUnidadesParaCobroLiqBatch : " + sql);
		dataSet = db.get(dataSource, sql);
	}

	public void listarUnidadesParaCobroLiqBatchSitme() throws Exception {
		String sql = "select undinv_id,undinv_nombre,undinv_serie from infi_tb_106_unidad_inversion a," + " infi_tb_101_inst_financieros b where undinv_id in " + " (select uniinv_id from infi_tb_204_ordenes where ordsta_id='" + StatusOrden.LIQUIDADA + "' " + " and transa_id in('" + TransaccionNegocio.TOMA_DE_ORDEN + "','" + TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA + "') "
				+ " and ordene_id in( " + " select ordene_id from  infi_tb_207_ordenes_operacion where status_operacion in('" + ConstantesGenerales.STATUS_EN_ESPERA + "','" + ConstantesGenerales.STATUS_RECHAZADA + "'))" + ") " + " and a.in_cobro_batch_liq=1 and a.insfin_id = b.insfin_id and b.TIPO_PRODUCTO_ID='" + ConstantesGenerales.ID_TIPO_PRODUCTO_SITME + "'" + " union "
				+ "select undinv_id,undinv_nombre,undinv_serie from infi_tb_106_unidad_inversion a," + " infi_tb_101_inst_financieros b where undinv_id in " + " (select uniinv_id from infi_tb_204_ordenes where ordsta_id='" + StatusOrden.REGISTRADA + "' " + " and transa_id in('" + TransaccionNegocio.LIQUIDACION + "') " + " and ordene_id in( "
				+ " select ordene_id from  infi_tb_207_ordenes_operacion where status_operacion in('" + ConstantesGenerales.STATUS_EN_ESPERA + "','" + ConstantesGenerales.STATUS_RECHAZADA + "'))" + ") and a.in_cobro_batch_liq=1 and a.insfin_id = b.insfin_id and b.TIPO_PRODUCTO_ID='" + ConstantesGenerales.ID_TIPO_PRODUCTO_SITME + "'";
		dataSet = db.get(dataSource, sql);
	}

	/**
	 * Resumen de la unidad de inversión. Muestra las operaciones de los vehículos involucrados
	 * 
	 * @param idUnidad
	 *            id de la unidad de inversión. SI unidad es 0 se asume que es sitme
	 * @throws Exception
	 *             en caso de error
	 */
	public void resumenParaCobroAdjBatch(int idUnidad, boolean sitme) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select c.undinv_id,c.undinv_nombre, a.status_operacion, sum(a.monto_operacion) monto_operacion," + "a.trnf_tipo, count(a.ordene_id) total from infi_tb_207_ordenes_operacion a, " + "infi_tb_204_ordenes b, infi_tb_106_unidad_inversion c, infi_tb_101_inst_financieros d where a.ordene_id = b.ordene_id and trunc(a.fecha_aplicar) <= trunc(sysdate) and c.insfin_id = d.insfin_id "
				+ "and b.uniinv_id = c.undinv_id and a.status_operacion in('" + ConstantesGenerales.STATUS_EN_ESPERA + "','" + ConstantesGenerales.STATUS_RECHAZADA + "')	and a.ordene_id in (select ordene_id from infi_tb_204_ordenes " + " where ");

		if (idUnidad != 0 && !sitme) {
			sql.append("uniinv_id=").append(idUnidad).append(" and c.undinv_status='" + UnidadInversionConstantes.UISTATUS_CERRADA + "' and ");
		} else {
			if (idUnidad > 0) {
				sql.append("uniinv_id=").append(idUnidad).append(" and ");
			}
			sql.append(" d.TIPO_PRODUCTO_ID = '").append(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME).append("' and ");
		}
		sql.append(" a.trnf_tipo IN ('").append(TransaccionFinanciera.DEBITO).append("', '").append(TransaccionFinanciera.DESBLOQUEO).append("', '").append(TransaccionFinanciera.CREDITO).append("') AND");
		sql.append(" ordsta_id='" + StatusOrden.PROCESO_ADJUDICACION + "')" + " and transa_id in('" + TransaccionNegocio.TOMA_DE_ORDEN + "','" + TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA + "')" + " and c.IN_COBRO_BATCH_ADJ=1" + "group by c.undinv_id,c.undinv_nombre, a.status_operacion, a.trnf_tipo " + " union "
				+ "select c.undinv_id,c.undinv_nombre, a.status_operacion, sum(a.monto_operacion) monto_operacion," + "a.trnf_tipo, count(a.ordene_id) total from infi_tb_207_ordenes_operacion a, " + "infi_tb_204_ordenes b, infi_tb_106_unidad_inversion c, infi_tb_101_inst_financieros d where a.ordene_id = b.ordene_id and trunc(a.fecha_aplicar) <= trunc(sysdate) and c.insfin_id = d.insfin_id "
				+ "and b.uniinv_id = c.undinv_id and a.status_operacion in('" + ConstantesGenerales.STATUS_EN_ESPERA + "','" + ConstantesGenerales.STATUS_RECHAZADA + "')	and a.ordene_id in (select ordene_id from infi_tb_204_ordenes " + " where ");

		if (idUnidad != 0 && !sitme) {
			sql.append("uniinv_id=").append(idUnidad).append(" and c.undinv_status='" + UnidadInversionConstantes.UISTATUS_CERRADA + "' and ");
		} else {
			if (idUnidad > 0) {
				sql.append("uniinv_id=").append(idUnidad).append(" and ");
			}
			sql.append(" d.TIPO_PRODUCTO_ID = '").append(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME).append("' and ");
		}

		sql.append(" ordsta_id='" + StatusOrden.REGISTRADA + "')" + " and b.transa_id='" + TransaccionNegocio.ORDEN_VEHICULO + "' " + " and c.IN_COBRO_BATCH_ADJ=1 " + " group by c.undinv_id,c.undinv_nombre, a.status_operacion, a.trnf_tipo ");
		System.out.println("resumenParaCobroAdjBatch : "+sql.toString() );
		dataSet = db.get(dataSource, sql.toString());
	}

	/**
	 * Resumen de la unidad de inversión. Muestra las operaciones de los vehículos involucrados
	 * 
	 * @param idUnidad
	 *            id de la unidad de inversión
	 * @throws Exception
	 *             en caso de error
	 */
	public void resumenParaCobroLiqBatch(int idUnidad, boolean sitme) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select c.undinv_id,c.undinv_nombre, a.status_operacion, sum(a.monto_operacion) monto_operacion," + "a.trnf_tipo, count(a.ordene_id) total from infi_tb_207_ordenes_operacion a, " + "infi_tb_204_ordenes b, infi_tb_106_unidad_inversion c, infi_tb_101_inst_financieros d where a.ordene_id = b.ordene_id and c.insfin_id = d.insfin_id "
				+ "and b.uniinv_id = c.undinv_id and a.status_operacion in('" + ConstantesGenerales.STATUS_EN_ESPERA + "','" + ConstantesGenerales.STATUS_RECHAZADA + "')	and a.ordene_id in (select ordene_id from infi_tb_204_ordenes " + " where ");

		if (idUnidad != 0 && !sitme) {
			sql.append("uniinv_id=").append(idUnidad).append(" and c.undinv_status='" + UnidadInversionConstantes.UISTATUS_LIQUIDADA + "') and ");
		} else {
			sql.append(" d.TIPO_PRODUCTO_ID = '").append(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME).append("') and ");
			if (idUnidad > 0) {
				sql.append(" uniinv_id=").append(idUnidad).append(" and ");
			}
		}

		sql.append(" ordsta_id in('" + StatusOrden.LIQUIDADA + "','" + StatusOrden.REGISTRADA + "')" + " and transa_id in('" + TransaccionNegocio.TOMA_DE_ORDEN + "','" + TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA + "','" + TransaccionNegocio.ORDEN_VEHICULO + "','" + TransaccionNegocio.LIQUIDACION + "')" + " and c.IN_COBRO_BATCH_LIQ=1 ");
		sql.append(" AND a.trnf_tipo IN ('").append(TransaccionFinanciera.DEBITO).append("', '").append(TransaccionFinanciera.DESBLOQUEO).append("', '").append(TransaccionFinanciera.CREDITO).append("')");		
		sql.append(" group by c.undinv_id,c.undinv_nombre, a.status_operacion, a.trnf_tipo ");
		
		System.out.println("QUERY resumenParaCobroLiqBatch: "  + sql.toString());
		dataSet = db.get(dataSource, sql.toString());
	}

	/**
	 * Obtiene un detalle de las operaciones financieras que van a ser enviadas en el archivo para el cobro batch
	 * 
	 * @param idUnidad
	 *            id de la unidad de inversión. Si la unidad es 0 el sistema asume que es de SITME
	 * @return la instrucción sql a ejecutar
	 * @throws Exception
	 */
	public String detalleDeOperacionBatchPorUnidadLiquidacion(int idUnidad, boolean sitme) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select a.ordene_relac_operacion_id, d.tipper_id,d.client_cedrif,d.client_nombre,b.ordene_id, a.ordene_operacion_id,a.codigo_operacion,a.numero_retencion," + "a.ctecta_numero,c.undinv_id, c.undinv_nombre, a.status_operacion, a.monto_operacion,a.trnf_tipo,b.ordene_veh_tom, b.ordene_id_relacion " );
		sql.append(" from infi_tb_207_ordenes_operacion a, infi_tb_204_ordenes b, infi_tb_106_unidad_inversion c, ");
		sql.append("infi_tb_201_ctes d, infi_tb_101_inst_financieros e where a.ordene_id = b.ordene_id and c.insfin_id = e.insfin_id " + "and b.uniinv_id = c.undinv_id and a.status_operacion in('" + ConstantesGenerales.STATUS_EN_ESPERA + "','" + ConstantesGenerales.STATUS_RECHAZADA + "')	and a.ordene_id in (select ordene_id from infi_tb_204_ordenes " + " where ");
		if (idUnidad != 0 && !sitme) {
			sql.append("uniinv_id=").append(idUnidad).append(" and c.undinv_status='" + UnidadInversionConstantes.UISTATUS_LIQUIDADA + "') and ");
		} else {
			sql.append(" e.TIPO_PRODUCTO_ID = '").append(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME).append("') and ");
			if (idUnidad > 0) {
				sql.append(" uniinv_id=").append(idUnidad).append(" and ");
			}
		}
		sql.append(" ordsta_id in('" + StatusOrden.LIQUIDADA + "','" + StatusOrden.REGISTRADA + "')" + " and b.client_id=d.client_id and b.transa_id in('" + TransaccionNegocio.TOMA_DE_ORDEN + "','" + TransaccionNegocio.ORDEN_VEHICULO + "','" + TransaccionNegocio.LIQUIDACION + "')" + " and c.IN_COBRO_BATCH_LIQ=1 ");
		sql.append(" order by ordene_operacion_id");
		
		System.out.println("QUERY detalleDeOperacionBatchPorUnidadLiquidacion - " + sql.toString());
		return sql.toString();
	}

	public String detalleDeOperacionBatchPorUnidadLiquidacion(String idOperaciones,String idUnidad) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select a.ordene_relac_operacion_id, d.tipper_id,d.client_cedrif,d.client_nombre,b.ordene_id, a.ordene_operacion_id,a.codigo_operacion,a.numero_retencion," + "a.ctecta_numero,c.undinv_id, c.undinv_nombre, a.status_operacion, a.monto_operacion,a.trnf_tipo,b.ordene_veh_tom " + " from infi_tb_207_ordenes_operacion a, infi_tb_204_ordenes b, infi_tb_106_unidad_inversion c, ");
		sql.append("infi_tb_201_ctes d, infi_tb_101_inst_financieros e where a.ordene_id = b.ordene_id and c.insfin_id = e.insfin_id " + "and b.uniinv_id = c.undinv_id and a.status_operacion in('" + ConstantesGenerales.STATUS_EN_ESPERA + "','" + ConstantesGenerales.STATUS_RECHAZADA + "')	and a.ordene_id in (select ordene_id from infi_tb_204_ordenes " + " where ");		
		sql.append("uniinv_id=").append(idUnidad).append(" and c.undinv_status='" + UnidadInversionConstantes.UISTATUS_LIQUIDADA + "') and ");		
		sql.append(" ordsta_id in('" + StatusOrden.LIQUIDADA + "','" + StatusOrden.REGISTRADA + "')" + " and b.client_id=d.client_id and b.transa_id in('" + TransaccionNegocio.TOMA_DE_ORDEN + "','" + TransaccionNegocio.ORDEN_VEHICULO + "','" + TransaccionNegocio.LIQUIDACION + "')" + " and c.IN_COBRO_BATCH_LIQ=1 ");
		
		if(idOperaciones!=null && !idOperaciones.equals("")){
			sql.append(" AND B.ORDENE_ID IN (").append(idOperaciones).append(")");
		}
		
		sql.append(" order by ordene_operacion_id");
		
		System.out.println("detalleDeOperacionBatchPorUnidadLiquidacion ------> " + sql.toString());
		return sql.toString();
	}
	/**
	 * Verifica que no existan ordenes con estatus enviada asociadas a la unidad de inversión
	 * 
	 * @param idUnidad
	 *            id de la unidad de inversión. Si la unidad es 0 el sistema asume que es de SITME
	 * @throws Exception
	 *             en caso de erro
	 */
	public void verificarOrdenesParaBacthSubasta(int idUnidad) throws Exception {
		String sql = " select ORDENE_ID,ORDSTA_ID from infi_tb_204_ordenes where uniinv_id in(" + idUnidad + ") AND ordsta_id in('" + StatusOrden.ENVIADA + "')";
		dataSet = db.get(dataSource, sql);
	}

	/**
	 * Obtiene un detalle de las operaciones financieras que van a ser enviadas en el archivo para el cobro batch
	 * 
	 * @param idUnidad
	 *            id de la unidad de inversión. Si la unidad es 0 el sistema asume que es de SITME
	 * @return la instrucción sql a ejecutar
	 * @throws Exception
	 */
	public String detalleDeOperacionBatchPorUnidad(int idUnidad, boolean sitme) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select a.codigo_operacion,a.numero_retencion,to_char(a.fecha_aplicar,'YYYYMMDD') fecha_operacion_padre,a.ordene_relac_operacion_id, d.tipper_id,d.client_cedrif,d.client_nombre,b.ordene_id, a.ordene_operacion_id,a.codigo_operacion,a.numero_retencion," + "a.ctecta_numero,c.undinv_id, c.undinv_nombre, a.status_operacion, a.monto_operacion,a.trnf_tipo,b.ordene_veh_tom, b.ordene_id_relacion "
				+ " from infi_tb_207_ordenes_operacion a, infi_tb_204_ordenes b, infi_tb_106_unidad_inversion c, " + "infi_tb_201_ctes d, infi_tb_101_inst_financieros e where a.ordene_id = b.ordene_id and c.insfin_id = e.insfin_id " + "and b.uniinv_id = c.undinv_id and a.status_operacion in('" + ConstantesGenerales.STATUS_EN_ESPERA + "','" + ConstantesGenerales.STATUS_RECHAZADA
				+ "') and a.trnf_tipo in('" + TransaccionFinanciera.DEBITO + "','" + TransaccionFinanciera.CREDITO + "') and a.ordene_id in (select ordene_id from infi_tb_204_ordenes " + " where ");
		if (idUnidad != 0 && !sitme) {
			sql.append("uniinv_id=").append(idUnidad).append(" and ");
		} else {
			if (idUnidad > 0) {
				sql.append("uniinv_id=").append(idUnidad).append(" and ");
			}
			sql.append(" e.TIPO_PRODUCTO_ID = '").append(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME).append("' and ");
		}

		sql.append(" ordsta_id='" + StatusOrden.PROCESO_ADJUDICACION + "')" + " and b.client_id=d.client_id and b.transa_id in('" + TransaccionNegocio.TOMA_DE_ORDEN + "')" + " and c.IN_COBRO_BATCH_ADJ=1 " + " union "
				+ "select a.codigo_operacion,a.numero_retencion,to_char(a.fecha_aplicar,'YYYYMMDD') fecha_operacion_padre,a.ordene_relac_operacion_id,d.tipper_id,d.client_cedrif,d.client_nombre,b.ordene_id, a.ordene_operacion_id,a.codigo_operacion,a.numero_retencion," + "a.ctecta_numero,c.undinv_id, c.undinv_nombre, a.status_operacion, a.monto_operacion,a.trnf_tipo,b.ordene_veh_tom, b.ordene_id_relacion "
				+ " from infi_tb_207_ordenes_operacion a, infi_tb_204_ordenes b, infi_tb_106_unidad_inversion c, infi_tb_201_ctes d, infi_tb_101_inst_financieros e" + " where a.ordene_id = b.ordene_id and c.insfin_id = e.insfin_id " + "and b.uniinv_id = c.undinv_id and a.status_operacion in('" + ConstantesGenerales.STATUS_EN_ESPERA + "','" + ConstantesGenerales.STATUS_RECHAZADA
				+ "')	and a.ordene_id in (select ordene_id from infi_tb_204_ordenes " + " where ");
		if (idUnidad != 0 && !sitme) {
			sql.append("uniinv_id=").append(idUnidad).append(" and ");
		} else {
			if (idUnidad > 0) {
				sql.append("uniinv_id=").append(idUnidad).append(" and ");
			}
			sql.append(" e.TIPO_PRODUCTO_ID = '").append(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME).append("' and ");
		}

		sql.append(" ordsta_id='" + StatusOrden.REGISTRADA + "')" + " and b.transa_id='" + TransaccionNegocio.ORDEN_VEHICULO + "' and b.client_id=d.client_id " + " and c.IN_COBRO_BATCH_ADJ=1 ");

		// Para las de desbloqueo
		sql.append(" union select a.codigo_operacion,a.numero_retencion,to_char(f.fecha_aplicar,'YYYYMMDD') fecha_operacion_padre,a.ordene_relac_operacion_id, d.tipper_id,d.client_cedrif,d.client_nombre,b.ordene_id, a.ordene_operacion_id,a.codigo_operacion,a.numero_retencion," + "a.ctecta_numero,c.undinv_id, c.undinv_nombre, a.status_operacion, a.monto_operacion,a.trnf_tipo,b.ordene_veh_tom, b.ordene_id_relacion "
				+ " from infi_tb_207_ordenes_operacion a, infi_tb_204_ordenes b, infi_tb_106_unidad_inversion c, " + "infi_tb_201_ctes d, infi_tb_101_inst_financieros e,infi_tb_207_ordenes_operacion f where a.ordene_id = b.ordene_id and c.insfin_id = e.insfin_id " + "and b.uniinv_id = c.undinv_id and a.status_operacion in('" + ConstantesGenerales.STATUS_EN_ESPERA + "','"
				+ ConstantesGenerales.STATUS_RECHAZADA + "') and a.trnf_tipo in('" + TransaccionFinanciera.DESBLOQUEO + "') and a.ordene_id in (select ordene_id from infi_tb_204_ordenes " + " where a.ordene_id = f.ordene_id and a.ordene_relac_operacion_id = f.ordene_operacion_id and ");
		if (idUnidad != 0 && !sitme) {
			sql.append("uniinv_id=").append(idUnidad).append(" and ");
		} else {
			if (idUnidad > 0) {
				sql.append("uniinv_id=").append(idUnidad).append(" and ");
			}
			sql.append(" e.TIPO_PRODUCTO_ID = '").append(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME).append("' and ");
		}

		sql.append(" ordsta_id='" + StatusOrden.PROCESO_ADJUDICACION + "')" + " and b.client_id=d.client_id and b.transa_id in('" + TransaccionNegocio.TOMA_DE_ORDEN + "','" + TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA + "')" + " and c.IN_COBRO_BATCH_ADJ=1 "
		+ " order by ordene_operacion_id");
		System.out.println("detalleDeOperacionBatchPorUnidad---****: "+sql.toString());
		return sql.toString();
	}
	
	/**
	 * Lista todas las unidad de inversión que contengan el título
	 * @param idTitulo id del título a consultar
	 * @throws Exception en caso de error
	 */
	public void listarUIPorTituloId(String idTitulo) throws Exception {
		String sql = "select  b.undinv_id,b.undinv_nombre,b.insfin_id,b.undinv_status,c.insfin_descripcion " +
				"from infi_tb_108_ui_titulos a, infi_tb_106_unidad_inversion b,infi_tb_101_inst_financieros c " + 
				" where a.undinv_id = b.undinv_id and a.titulo_id='" + idTitulo + "' and b.insfin_id = c.insfin_id";
		dataSet = db.get(this.dataSource, sql);
	}
	
	//NM26659 - TTS-491 30/03/2015 Modificado para incluir campo TIPO_NEGOCIO
		/**
	 * Listar datos de la unidad de inversion
	 * @param idUnidadInversion  : identificador de la Unidad de Inversion
	 * @throws Exception
	 */
	public void listarDatosUIPorId(long idUnidadInversion) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT if.insfin_id,if.INSFIN_DESCRIPCION,ui.IN_COBRO_BATCH_ADJ,ui.IN_COBRO_BATCH_LIQ, to_char(ui.UNDINV_FE_LIQUIDACION_HORA1,'dd-MM-yyyy hh:mm:ss') fecha1,to_char(ui.UNDINV_FE_LIQUIDACION_HORA1,'dd-MM-yyyy hh:mm:ss') fecha2, if.TIPO_PRODUCTO_ID, ui.TIPO_NEGOCIO ");	
		sql.append(" from INFI_TB_106_UNIDAD_INVERSION ui, INFI_TB_101_INST_FINANCIEROS if where ui.insfin_id = if.insfin_id and ui.undinv_id =").append(idUnidadInversion);
		dataSet = db.get(dataSource, sql.toString());
		System.out.println("listarDatosUIPorId "+sql);
	}
	
	/**
	 * Lista las unidades de inversión para conciliaciones de cobro, para subasta las que se encuentren adjudicadas y liquidadas 
	 * y para sitme las que se encuentren adjudicadas, liquidadas y publicadas (ya que siempre estan abiertas para la toma de ordenes)
	 * @throws Exception
	 */
	public void listarUIParaConciliaciones() throws Exception{
		
		StringBuilder sql = new StringBuilder();
		//UNIDADES SUBASTA:
		sql.append(" SELECT ui.undinv_status, ui.undinv_id, ui.undinv_nombre, ui.undinv_serie ");
		sql.append(" FROM INFI_TB_106_UNIDAD_INVERSION ui, INFI_TB_101_INST_FINANCIEROS inf ");
		sql.append(" WHERE ui.insfin_id = inf.insfin_id ");
		sql.append(" AND inf.tipo_producto_id = '").append(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA).append("' ");
		sql.append(" AND ui.undinv_status IN ('");
		sql.append(UnidadInversionConstantes.UISTATUS_ADJUDICADA).append("', '");
		sql.append(UnidadInversionConstantes.UISTATUS_LIQUIDADA).append("') ");
		sql.append(" UNION ");//UNIDADES SITME:
		sql.append(" SELECT ui.undinv_status, ui.undinv_id, ui.undinv_nombre, ui.undinv_serie ");
		sql.append(" FROM INFI_TB_106_UNIDAD_INVERSION ui, INFI_TB_101_INST_FINANCIEROS inf ");
		sql.append(" WHERE ui.insfin_id = inf.insfin_id ");
		sql.append(" AND inf.tipo_producto_id = '").append(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME).append("' ");
		sql.append(" AND ui.undinv_status IN ('");
		sql.append(UnidadInversionConstantes.UISTATUS_PUBLICADA).append("', '");
		sql.append(UnidadInversionConstantes.UISTATUS_ADJUDICADA).append("', '");
		sql.append(UnidadInversionConstantes.UISTATUS_LIQUIDADA).append("') ");
		
		dataSet = db.get(dataSource, sql.toString());			
	}
	public String getInstrumentoFinancieroPorUI(long idUnidadInversion) throws Exception {
		 
		DataSet datSet=new DataSet();
		StringBuffer sql=new StringBuffer();
		String insFinanciero=null;
		
		sql.append("SELECT INS.INSFIN_DESCRIPCION ");
		sql.append("FROM INFI_TB_106_UNIDAD_INVERSION UI, INFI_TB_101_INST_FINANCIEROS INS ");
		sql.append("WHERE UI.INSFIN_ID = INS.INSFIN_ID ");
		sql.append("AND UI.UNDINV_ID=").append(idUnidadInversion);
	
		datSet = db.get(dataSource, sql.toString());	
		if(datSet.next()){
			insFinanciero=datSet.getValue("INSFIN_DESCRIPCION").trim();
			System.out.println(sql);
		}else {
			insFinanciero="";
		}
		datSet.clear();
		datSet=null;
		
		return insFinanciero;
		
	}
	
	public void listarOrdenesClaveNet(long uniId,String status,String fechaRegistro,String fechaModificacion) throws Exception {
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT 'BANCO DE VENEZUELA' AS vehicu_nombre,");
		sql.append("SUM (SS.MONTO_SOLICITADO) AS ordene_ped_monto,COUNT (SS.ID_ORDEN) AS para_enviar,");
		sql.append("(SELECT COUNT (ID_ORDEN) FROM SOLICITUDES_SITME SS1 WHERE SS1.ESTATUS ='");
		sql.append(StatusOrden.EN_TRAMITE).append("'");
		if(fechaRegistro!=null){
			sql.append("AND SS1.FECHA_REGISTRO = TO_DATE ('").append(fechaRegistro).append("', 'YYYY-MM-DD'))").append(" AS enviadas,");
			sql.append("(SELECT COUNT (SS2.ID_ORDEN) FROM SOLICITUDES_SITME SS2 WHERE SS2.FECHA_REGISTRO = TO_DATE ('").append(fechaRegistro).append("', 'YYYY-MM-DD')");
		}else{
			sql.append("AND trunc(SS1.FECHA_REGISTRO) = TO_DATE ('").append(fechaModificacion).append("', 'DD-MM-YYYY'))").append(" AS enviadas,");
			sql.append("(SELECT COUNT (SS2.ID_ORDEN) FROM SOLICITUDES_SITME SS2 WHERE trunc(SS2.FECHA_REGISTRO) = TO_DATE ('").append(fechaModificacion).append("', 'DD-MM-YYYY')");
		}		
		sql.append(" AND SS2.ESTATUS IN('"+StatusOrden.EN_TRAMITE+"','"+StatusOrden.REGISTRADA+"')) AS ordenes,'' AS para_enviar,'1' AS flag,0 AS ordene_veh_col,");
		sql.append(uniId).append(" AS uniinv_id");
		sql.append(" FROM SOLICITUDES_SITME SS WHERE SS.ESTATUS ='").append(status).append("' ");
		if(fechaRegistro!=null){
			sql.append("AND SS.FECHA_REGISTRO = TO_DATE ('").append(fechaRegistro).append("','YYYY-MM-DD') ");
		}else{
			sql.append("AND trunc(SS.FECHA_REGISTRO) = TO_DATE ('").append(fechaModificacion).append("','DD-MM-YYYY') ");
		}
		
		sql.append("GROUP BY 'BANCO DE VENEZUELA'");	
		System.out.println("listarOrdenesClaveNet: "+sql.toString());
		dataSet = db.get(dataSource, sql.toString());
	}
	
//	NM25287 SICAD II. Se solicitó NO actualizar el estatus en SOLICITUDES SITME en la exportacion de ordenes. 27/03/2014
//	NM25287 SICAD II. Modificación por mejoras al proceso de exportacion de ordenes. 20/06/2014
//	NM25287 CONTINGENCIA SIMADI 11/02/2015. Filtro por oferta/demanda y cambio de validacion de orden registrada por FECHA_TRAMITE
	public void listarOrdenesClaveNet(long uniId,String status,String fechaRegistro,boolean registradaEnInfi,boolean obtenerUltimaSolcitudRegistrada, String idProducto) throws Exception {
		
		StringBuffer sql = new StringBuffer();
		
		if(!obtenerUltimaSolcitudRegistrada)
		{
			sql.append("SELECT 'BANCO DE VENEZUELA' AS vehicu_nombre,");
			sql.append("SUM (SS.MONTO_SOLICITADO) AS ordene_ped_monto,COUNT (SS.ID_ORDEN) AS para_enviar,");
			sql.append("(SELECT COUNT (ID_ORDEN) FROM SOLICITUDES_SITME SS1 WHERE SS1.ESTATUS ='");
			sql.append(StatusOrden.RECIBIDA).append("'");
			if(fechaRegistro!=null){
				sql.append(" AND trunc(SS1.FECHA_REGISTRO) = TO_DATE ('").append(fechaRegistro).append("', 'DD-MM-YYYY')");
				if (idProducto!=null&&idProducto!="") {
					sql.append(" AND ID_PRODUCTO = ").append(idProducto);
				}
				if (uniId>0) {
					sql.append(" AND ss1.UNDINV_ID=").append(uniId);
				}
				sql.append(") AS ordenes,");
			}
			sql.append("(SELECT COUNT (ID_ORDEN) FROM SOLICITUDES_SITME SS2 WHERE SS2.ESTATUS ='");
			sql.append(StatusOrden.RECIBIDA).append("' AND SS2.FECHA_TRAMITE IS NOT NULL");
			if(fechaRegistro!=null){
				sql.append(" AND trunc(SS2.FECHA_REGISTRO) = TO_DATE ('").append(fechaRegistro).append("', 'DD-MM-YYYY')");
				if (idProducto!=null&&idProducto!="") {
					sql.append(" AND ID_PRODUCTO = ").append(idProducto);
				}
				
				if (uniId>0) {
					sql.append(" AND ss2.UNDINV_ID=").append(uniId);
				}
				sql.append(") AS enviadas,");
			}
			sql.append("'1' AS flag,0 AS ordene_veh_col,");
			sql.append(uniId).append(" AS uniinv_id");		
		}else{
			sql.append("SELECT MAX(SS.ID_ORDEN) ULTIMO_REGISTRO");
		}
		
		sql.append(" FROM SOLICITUDES_SITME SS WHERE SS.ESTATUS ='").append(status).append("' ");
		if(fechaRegistro!=null){
			sql.append("AND trunc(SS.FECHA_REGISTRO) = TO_DATE ('").append(fechaRegistro).append("','DD-MM-YYYY') ");
		}
		if(registradaEnInfi){
			sql.append(" AND SS.FECHA_TRAMITE IS NOT NULL");
		}else{
			sql.append(" AND SS.FECHA_TRAMITE IS NULL");
		}
		
		if (idProducto!=null&&idProducto!="") {
			sql.append(" AND ID_PRODUCTO = ").append(idProducto);
		}
		
		if (uniId>0) {
			sql.append(" AND ss.UNDINV_ID=").append(uniId);
		}
		
		sql.append(" GROUP BY 'BANCO DE VENEZUELA'");	
		System.out.println("listarOrdenesClaveNet: "+sql.toString());
		dataSet = db.get(dataSource, sql.toString());
	}
	public void listaUnidadesAdjudicar() throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("select s.undinv_status, u.undinv_id, undinv_nombre, undinv_serie from INFI_TB_106_UNIDAD_INVERSION u,INFI_TB_117_UNDINV_STATUS s, INFI_TB_101_INST_FINANCIEROS inf ");
		sql.append("where u.undinv_status=s.undinv_status and ((u.undinv_status ='" + UnidadInversionConstantes.UISTATUS_CERRADA + "' and inf.tipo_producto_id= '" + ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA + "') ");
		sql.append("or (u.undinv_status ='" + UnidadInversionConstantes.UISTATUS_PUBLICADA + "') and inf.tipo_producto_id ='" + ConstantesGenerales.ID_TIPO_PRODUCTO_SITME + "') ");
		sql.append("and u.insfin_id = inf.insfin_id ");
		sql.append("order by undinv_nombre"); 		
		
		//System.out.println("listaUnidadesAdjudicar --> " + sql.toString());
		dataSet = db.get(dataSource, sql.toString());
		
	} 
	
	public void listarTipoProducto()throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TIPO_PRODUCTO_ID FROM INFI_TB_019_TIPO_DE_PRODUCTO ORDER BY TIPO_PRODUCTO_ID DESC");
		
		dataSet = db.get(dataSource, sql.toString());
	}
	
	//Metodo Agregado en el requerimiento TTS-385 Entregable_2 NM26659
	public void listarTipoProductoPorId(String... tipoProducto)throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT TIPO_PRODUCTO_ID FROM INFI_TB_019_TIPO_DE_PRODUCTO TP ");
		
		int count=0;
		if (tipoProducto!=null && tipoProducto.length>0){
			sql.append(" WHERE TP.TIPO_PRODUCTO_ID IN (");
			for (String element : tipoProducto) {
				if (count>0){
					sql.append(",");	
				}				
				sql.append("'"+element+"'");
				++count;
			}
			sql.append(") ");			
		}		
		sql.append(" ORDER BY TIPO_PRODUCTO_ID DESC");
		
		dataSet = db.get(dataSource, sql.toString());
	}
	
	public void listarUnidadInversionParaVentaTitClavenet()throws Exception {
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT UI.UNDINV_NOMBRE as unidadInversion,UI.UNDINV_ID AS UNDINV_ID FROM INFI_TB_106_UNIDAD_INVERSION UI, INFI_TB_101_INST_FINANCIEROS INS ");
		sql.append("WHERE UI.INSFIN_ID = INS.INSFIN_ID AND INS.TIPO_PRODUCTO_ID='"+ConstantesGenerales.ID_TIPO_PRODUCTO_SITME+"' ");
		sql.append("AND INS.INSFIN_DESCRIPCION NOT IN ('"+ConstantesGenerales.INST_FINANCIERO_SITME_CLAVENET_P+"','"+ConstantesGenerales.INST_FINANCIERO_SITME_CLAVENET_E+"') ");
		sql.append("AND EXISTS (SELECT * FROM INFI_TB_204_ORDENES ORD WHERE ORD.ORDSTA_ID NOT IN ('REGISTRADA', 'CANCELADA', 'ENVIADA') AND ORD.UNIINV_ID = UI.UNDINV_ID) ORDER BY UI.UNDINV_NOMBRE");
                                                           
		dataSet = db.get(dataSource, sql.toString());
	}
	public void obtenerDatosUIporId(String campos, String uniId) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ").append(campos);
		sql.append(" FROM INFI_TB_106_UNIDAD_INVERSION WHERE UNDINV_ID =").append(uniId);
		dataSet = db.get(dataSource, sql.toString());
		
	} 
	
	/**
	 * Metodo que permite realizar la consulta 
	 * */
	public void listarUnidadesParaAbonoCuentaDolaresSitme() throws Exception {
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT UI.UNDINV_NOMBRE,UI.UNDINV_ID FROM INFI_TB_106_UNIDAD_INVERSION UI WHERE UI.UNDINV_ID IN");
        sql.append("(SELECT ORD.UNIINV_ID FROM INFI_TB_204_ORDENES ORD WHERE ORD.TRANSA_ID = '").append(TRANSACCION_PACTO_RECOMPRA).append("' AND ORD.CTA_ABONO =").append(ConstantesGenerales.ABONO_CUENTA_NACIONAL).append(" AND ORD.ORDSTA_ID='").append(StatusOrden.REGISTRADA).append("'AND ORD.ORDENE_PED_FE_VALOR <= SYSDATE AND ORD.ORDENE_ID IN");
        sql.append("(SELECT OO.ORDENE_ID FROM INFI_TB_207_ORDENES_OPERACION OO WHERE OO.ORDENE_ID = ORD.ORDENE_ID AND OO.STATUS_OPERACION IN ('").append(STATUS_EN_ESPERA).append("','").append(STATUS_RECHAZADA).append("')))");
		dataSet = db.get(dataSource, sql.toString());
	}
	
	/**
	 * Metodo que realiza la busqueda de unidades de inversion por tipo de transaccion
	 * */
	public void listarUnidadesParaAbonoCuentaDolaresPorTransaccionId( String TRANSA_ID ) throws Exception {
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT UI.UNDINV_NOMBRE,UI.UNDINV_ID FROM INFI_TB_106_UNIDAD_INVERSION UI WHERE UI.UNDINV_ID IN");
        sql.append("(SELECT ORD.UNIINV_ID FROM INFI_TB_204_ORDENES ORD WHERE ORD.TRANSA_ID = '")
        .append(TRANSA_ID)
        .append("' AND ORD.CTA_ABONO =")
        .append(ConstantesGenerales.ABONO_CUENTA_NACIONAL)
        .append(" AND ORD.ORDSTA_ID='")
        .append(StatusOrden.REGISTRADA)
        .append("'AND trunc(ORD.ORDENE_PED_FE_VALOR) <= trunc(SYSDATE) AND ORD.ORDENE_ID IN");
        sql.append("(SELECT OO.ORDENE_ID FROM INFI_TB_207_ORDENES_OPERACION OO WHERE OO.ORDENE_ID = ORD.ORDENE_ID AND OO.STATUS_OPERACION IN ('").append(STATUS_EN_ESPERA).append("','").append(STATUS_RECHAZADA).append("')))");
        System.out.println("listarUnidadesParaAbonoCuentaDolaresPorTransaccionId ---> " + sql.toString());
		dataSet = db.get(dataSource, sql.toString());
	}	
	
	/**
	 * Metodo que permite realizar la busqueda del detalle de operaciones de abono para cuentas nacional en dolares
	 * */
	public void resumenAbonoCuentaDolares(int idUnidad, boolean sitme) throws Exception {
		
		StringBuilder sql = new StringBuilder();
		if(sitme){		
			sql.append("SELECT UI.UNDINV_NOMBRE as undinv_nombre,OO.STATUS_OPERACION as status_operacion,SUM (OO.MONTO_OPERACION) AS monto_operacion, OO.TRNF_TIPO as trnf_tipo, COUNT (ORD.ORDENE_ID) AS TOTAL ");
			sql.append("FROM INFI_TB_106_UNIDAD_INVERSION UI,INFI_TB_204_ORDENES ORD,INFI_TB_207_ORDENES_OPERACION OO ");
			sql.append("WHERE     UI.UNDINV_ID = ORD.UNIINV_ID AND ORD.ORDENE_ID = OO.ORDENE_ID AND ORD.TRANSA_ID ='").append(com.bancovenezuela.comun.logic.interfaces.ConstantesGenerales.TRANSACCION_PACTO_RECOMPRA).append("' ");
			sql.append("AND ORD.ORDSTA_ID='").append(StatusOrden.REGISTRADA).append("' AND ORD.CTA_ABONO =").append(ConstantesGenerales.ABONO_CUENTA_NACIONAL).append("  AND ORD.ORDENE_PED_FE_VALOR<=SYSDATE ");
			
			if(idUnidad>0){
				sql.append(" AND UI.UNDINV_ID=").append(idUnidad).append(" ");
			}
			sql.append(" GROUP BY UI.UNDINV_NOMBRE, OO.STATUS_OPERACION, OO.TRNF_TIPO");
		}
		dataSet = db.get(dataSource, sql.toString());
	}
	
	/**
	 * Metodo que permite realizar la busqueda del detalle de operaciones de abono para cuentas nacional en dolares
	 * */
	public void resumenAbonoCuentaDolaresPorUnidadYTipoTransaccion(int idUnidad, String TRANSA_ID) throws Exception {
		
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT ord.ordene_id,ctes.CLIENT_NOMBRE,ord.ordene_id_relacion,ui.undinv_nombre AS undinv_nombre,oo.monto_operacion AS monto_operacion,oo.status_operacion AS status_operacion,oo.trnf_tipo AS trnf_tipo,ord.ordene_ped_fe_valor,count(ord.ordene_id) as cantidad_operaciones "); 
		sql.append("FROM infi_tb_106_unidad_inversion ui,infi_tb_204_ordenes ord,infi_tb_207_ordenes_operacion oo,infi_tb_201_ctes ctes ");
		sql.append("WHERE ui.undinv_id = ord.uniinv_id AND ord.ordene_id = oo.ordene_id ");
		sql.append("AND ord.transa_id = '").append(TRANSA_ID).append("' ").append("AND ord.ordsta_id = '").append(StatusOrden.REGISTRADA).append("' ");
		sql.append("AND ord.cta_abono =").append(ConstantesGenerales.ABONO_CUENTA_NACIONAL).append(" AND ctes.CLIENT_ID=ord.CLIENT_ID");
		sql.append(" AND oo.status_operacion IN ('").append(STATUS_EN_ESPERA).append("','").append(STATUS_RECHAZADA).append("') ");
		
		if(idUnidad>0){
			sql.append(" AND UI.UNDINV_ID=").append(idUnidad).append(" ");
		}
		sql.append(" AND NOT EXISTS (SELECT OP.ORDENE_ID FROM infi_tb_207_ordenes_operacion op WHERE op.ordene_id = ord.ordene_id_relacion ");
		sql.append(" AND op.trnf_tipo ='").append(TransaccionFinanciera.DEBITO).append("'").append("  AND op.status_operacion IN ('").append(STATUS_EN_ESPERA).append("','").append(STATUS_RECHAZADA).append("')) ");
		sql.append(" GROUP BY ord.ordene_id,ctes.client_nombre, ord.ordene_id_relacion,ui.undinv_nombre,oo.monto_operacion,oo.status_operacion,oo.trnf_tipo,ord.ordene_ped_fe_valor ");
		sql.append(" ORDER BY ord.ordene_id ASC");
		//System.out.println("resumenAbonoCuentaDolaresPorUnidadYTipoTransaccion ----> " + sql.toString());
		dataSet = db.get(dataSource, sql.toString());
	}	
	
	/**
	 * Metodo que permite realizar la consulta del detalle de operaciones para el abono de cuenta en dolares
	 * */
	public String detalleDeAbonoCuentaDolares(int idUnidad, String idOrdenes, boolean sitme) throws Exception {

		StringBuilder sql = new StringBuilder();
		if (sitme) {

			sql.append("SELECT CTES.tipper_id,CTES.client_cedrif,CTES.client_nombre,ORD.ordene_id,ORD.ORDENE_ID_RELACION, ");
			sql.append("OO.ordene_operacion_id,OO.codigo_operacion,OO.numero_retencion,OO.ctecta_numero,UI.undinv_id, ");
			sql.append("UI.undinv_nombre,OO.status_operacion,OO.monto_operacion,OO.trnf_tipo,ORD.ordene_veh_tom,ORD.MONEDA_ID,ORD.CTA_ABONO ");
			sql.append("FROM INFI_TB_204_ORDENES ORD,INFI_TB_207_ORDENES_OPERACION OO,INFI_TB_201_CTES CTES,INFI_TB_106_UNIDAD_INVERSION UI ");
			sql.append("WHERE ORD.ORDENE_ID = OO.ORDENE_ID AND ORD.CLIENT_ID = CTES.CLIENT_ID AND UI.UNDINV_ID=ORD.UNIINV_ID ");
			sql.append("AND ORD.TRANSA_ID='").append(com.bancovenezuela.comun.logic.interfaces.ConstantesGenerales.TRANSACCION_PACTO_RECOMPRA).append("' ");
			sql.append("AND ORD.ORDSTA_ID='").append(StatusOrden.REGISTRADA).append("' AND  OO.STATUS_OPERACION IN ('").append(STATUS_EN_ESPERA).append("','").append(STATUS_RECHAZADA).append("') ");
			sql.append("AND ORD.CTA_ABONO=").append(ConstantesGenerales.ABONO_CUENTA_NACIONAL).append(" AND ORD.ORDENE_PED_FE_VALOR<=SYSDATE ");

			if (idOrdenes != null && !idOrdenes.equals("")) {
				sql.append(" AND ORD.ORDENE_ID IN (").append(idOrdenes).append(") ");
			}
		} else {

			sql.append("SELECT CTES.tipper_id,CTES.client_cedrif,CTES.client_nombre,ORD.ordene_id,ORD.ORDENE_ID_RELACION, ");
			sql.append("OO.ordene_operacion_id,OO.codigo_operacion,OO.numero_retencion,OO.ctecta_numero,UI.undinv_id, ");
			sql.append("UI.undinv_nombre,OO.status_operacion,OO.monto_operacion,OO.trnf_tipo,ORD.ordene_veh_tom,ORD.MONEDA_ID,ORD.CTA_ABONO ");
			sql.append("FROM INFI_TB_204_ORDENES ORD,INFI_TB_207_ORDENES_OPERACION OO,INFI_TB_201_CTES CTES,INFI_TB_106_UNIDAD_INVERSION UI ");
			sql.append("WHERE ORD.ORDENE_ID = OO.ORDENE_ID AND ORD.CLIENT_ID = CTES.CLIENT_ID AND UI.UNDINV_ID=ORD.UNIINV_ID ");
			sql.append("AND ORD.TRANSA_ID='").append(TransaccionNegocio.ORDEN_PAGO).append("' ");
			sql.append("AND ORD.ORDSTA_ID='").append(StatusOrden.REGISTRADA).append("' AND  OO.STATUS_OPERACION IN ('").append(STATUS_EN_ESPERA).append("','").append(STATUS_RECHAZADA).append("') ");
			sql.append("AND ORD.CTA_ABONO=").append(ConstantesGenerales.ABONO_CUENTA_NACIONAL).append(" AND ORD.ORDENE_PED_FE_VALOR<=SYSDATE ");

			if (idOrdenes != null && !idOrdenes.equals("")) {
				sql.append(" AND ORD.ORDENE_ID_RELACION IN (").append(idOrdenes).append(") ");
			}
		}

		if (idUnidad > 0) {
			sql.append(" AND ORD.UNIINV_ID=").append(idUnidad);
		}

		 //System.out.println("detalleDeAbonoCuentaDolares ----------> " + sql.toString());
		return sql.toString();
	}
	

	/**
	 * Metodo de busqueda de unidades de inversion dado el tipo de producto, estatus e instrumento financiero
	 * */
	//Metodo incluido en el desarrollo de proyecto SICAD NM26659 (Modificado en incidencia Calidad SICAD_2 NM26659)
	public void listaUnidadesAdjudicarPorTipoProductoStatusInstrumentoFinanciero(String tipoProducto,String status,String... instrumentoFinanciero) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT   u.undinv_status, u.undinv_id, undinv_nombre, undinv_serie FROM infi_tb_106_unidad_inversion u, infi_tb_101_inst_financieros inf ");
		sql.append("WHERE (SELECT COUNT (ordene_id) FROM infi_tb_204_ordenes ord WHERE ord.ordsta_id IN ('").append(StatusOrden.ENVIADA).append("','").append(StatusOrden.CANCELADA).append("') AND ord.uniinv_id = u.undinv_id) > 0 ");		
				
		if(status!=null && !status.equals("")){
			sql.append(" and u.undinv_status ='").append(status).append("' ");
		}
		
		if (tipoProducto!=null && !tipoProducto.equals("")) {
			sql.append(" and inf.tipo_producto_id='").append(tipoProducto).append("' ");
		}
		 
		
		int count=0;
		if(instrumentoFinanciero.length>0){
			sql.append(" and inf.INSFIN_DESCRIPCION IN (");
				for (String element : instrumentoFinanciero) {			
					if(count>0){
						sql.append(",");
					}
					sql.append("'"+element+"'");
				}
				sql.append(") ");
		}
		
		sql.append(" and u.insfin_id = inf.insfin_id ");
		sql.append(" order by undinv_nombre"); 
		System.out.println(" listaUnidadesAdjudicarPorTipoProductoStatusInstrumentoFinanciero -------------> " + sql.toString());
		dataSet = db.get(dataSource, sql.toString());
		
	} 
	
	/**
	 * Metodo de busqueda de unidades de inversion dado el tipo de producto y estatus
	 * */
	//NM26659 31/03/2015 TTS-491 WEB SERVICE ALTO VALOR (Inclusion parametro tipo de negocio)
	public void listaUnidadesAdjudicarPorTipoProductoStatus(String tipoProducto,int tipoNegocio,String... status) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT   u.undinv_status, u.undinv_id, undinv_nombre, undinv_serie FROM infi_tb_106_unidad_inversion u, infi_tb_101_inst_financieros inf ");
		sql.append("WHERE (SELECT COUNT (ordene_id) FROM infi_tb_204_ordenes ord WHERE ");		
				
		if(status!=null && !status.equals("")){
			int count=0;
			if(status.length>0){
				sql.append(" ord.ordsta_id IN (");
					for (String element : status) {			
						if(count>0){
							sql.append(",");
						}
						count++;
						sql.append("'"+element+"'");
					}
					sql.append(") ");					
			}
		}
		
		sql.append(" and ord.uniinv_id = u.undinv_id ) > 0");
		
		if (tipoProducto!=null && !tipoProducto.equals("")) {
			sql.append(" and inf.tipo_producto_id IN (").append(tipoProducto).append(") ");
		}
		
		if(tipoNegocio>0){
			sql.append("AND U.TIPO_NEGOCIO IN (").append(tipoNegocio).append(") ");
		}
		 		
		sql.append(" and u.insfin_id = inf.insfin_id ");
		sql.append(" order by undinv_nombre"); 
		//System.out.println(" listaUnidadesAdjudicarPorTipoProductoStatus -------------> " + sql.toString());
		dataSet = db.get(dataSource, sql.toString());
		
	} 
	
	/**
	 * Metodo de busqueda de unidades de inversion dado el tipo de producto y estatus de orden y Unidad
	 * */
	//NM26659 03/09/2015 Metodo creado en desarrollo SIMADI Venta en Taquilla
	public void listaUnidadesAdjudicarPorTipoProductoStatus(String tipoProducto,String estatusUnidadInv,int tipoNegocio,String... status) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT   u.undinv_status, u.undinv_id, undinv_nombre, undinv_serie FROM infi_tb_106_unidad_inversion u, infi_tb_101_inst_financieros inf ");
		sql.append("WHERE (SELECT COUNT (ordene_id) FROM infi_tb_204_ordenes ord WHERE ");		
				
		if(status!=null && !status.equals("")){
			int count=0;
			if(status.length>0){
				sql.append(" ord.ordsta_id IN (");
					for (String element : status) {			
						if(count>0){
							sql.append(",");
						}
						count++;
						sql.append("'"+element+"'");
					}
					sql.append(") ");					
			}
		}
		
		sql.append(" and ord.uniinv_id = u.undinv_id ) > 0");
		
		if (tipoProducto!=null && !tipoProducto.equals("")) {
			sql.append(" and inf.tipo_producto_id IN (").append(tipoProducto).append(") ");
		}
		
		if(tipoNegocio>0){
			sql.append("AND U.TIPO_NEGOCIO IN (").append(tipoNegocio).append(") ");
		}
		
		if(estatusUnidadInv!=null && !estatusUnidadInv.equals("")){
			sql.append(" AND u.undinv_status='").append(estatusUnidadInv).append("' ");
		}
		 		
		sql.append(" and u.insfin_id = inf.insfin_id ");		
		
		
		sql.append(" order by undinv_nombre"); 
		//System.out.println(" listaUnidadesAdjudicarPorTipoProductoStatus -------------> " + sql.toString());
		dataSet = db.get(dataSource, sql.toString());
		
	} 
	/**
	 * Lista las unidades de inversión con estatus CERRADA pero que tengan ordenes asociadas con estatus en PROCESO DE ADJUDICACION
	 * 
	 * @throws Exception
	 *             en caso de error
	 */
	//Metodo creado en Desarrollo Requerimiento SICAD nm26659
	public void listarUnidadesParaCobroAdjBatchSubastaDivisas(String tipoProducto) throws Exception {
		StringBuffer sql=new StringBuffer();
		
		sql.append("SELECT DISTINCT /*+ index(infi_tb_207_ordenes_operacion INDX_207_ID_IDOPE) */ ui.undinv_id, ui.undinv_nombre, ui.undinv_serie ");
		sql.append("FROM infi_tb_207_ordenes_operacion oo,infi_tb_204_ordenes ord, infi_tb_106_unidad_inversion ui,infi_tb_101_inst_financieros insf ");
		sql.append("WHERE oo.ordene_id = ord.ordene_id AND ord.uniinv_id = ui.undinv_id AND ui.insfin_id = insf.insfin_id ");		
		sql.append("AND OO.STATUS_OPERACION IN ('").append(STATUS_EN_ESPERA).append("','").append(STATUS_RECHAZADA).append("') AND OO.TRNF_TIPO !='").append(TransaccionFinanciera.MISCELANEO).append("' ");
		sql.append("AND oo.ordene_id = ord.ordene_id AND ord.uniinv_id = ui.undinv_id AND UNDINV_STATUS IN ('").append(UnidadInversionConstantes.UISTATUS_CERRADA).append("','"+UnidadInversionConstantes.UISTATUS_ADJUDICADA+"') ");
		
		if(tipoProducto!=null && !tipoProducto.equals("")){		
			sql.append("AND INSF.TIPO_PRODUCTO_ID='").append(tipoProducto).append("' ");
		}
		sql.append(" ORDER BY UI.UNDINV_NOMBRE");
		//System.out.println("listarUnidadesParaCobroAdjBatchSubastaDivisas ---> " + sql.toString());
				
		dataSet = db.get(dataSource, sql.toString());
	}
	
	/**
	 * Lista las unidades de inversión del producto especificado con estatus ADJUDICADA pero que tengan ordenes asociadas con operaciones EN ESPERA o RECHAZADAS
	 * 
	 * @throws Exception
	 *             en caso de error
	 */
	//Metodo creado en Desarrollo Requerimiento SICAD_II NM29643
	public void listarUnidadesParaCobroAdjBatchSicadII(String tipoProducto, String nombreUI) throws Exception {
		StringBuffer sql=new StringBuffer();
		
		sql.append("SELECT DISTINCT /*+ index(infi_tb_207_ordenes_operacion INDX_207_ID_IDOPE) */ ui.undinv_id, ui.undinv_nombre, ui.undinv_serie ");
		sql.append("FROM infi_tb_207_ordenes_operacion oo,infi_tb_204_ordenes ord, infi_tb_106_unidad_inversion ui,infi_tb_101_inst_financieros insf ");
		sql.append("WHERE oo.ordene_id = ord.ordene_id AND ord.uniinv_id = ui.undinv_id AND ui.insfin_id = insf.insfin_id ");		
		//sql.append("AND OO.STATUS_OPERACION IN ('").append(STATUS_EN_ESPERA).append("','").append(STATUS_RECHAZADA).append("') 
		sql.append("AND OO.TRNF_TIPO !='").append(TransaccionFinanciera.MISCELANEO).append("' ");
		//sql.append("AND UNDINV_STATUS NOT IN ('"+UnidadInversionConstantes.UISTATUS_CERRADA+"') ");
		sql.append("AND UPPER(ui.UNDINV_NOMBRE) like '%").append(nombreUI.toUpperCase()).append("%'");
		
		if(tipoProducto!=null && !tipoProducto.equals("")){		
			sql.append(" AND INSF.TIPO_PRODUCTO_ID='").append(tipoProducto).append("' ");
		}
		//System.out.println("listarUnidadesParaCobroAdjBatchSicadII ------->\n" + sql.toString());
				
		dataSet = db.get(dataSource, sql.toString());
	}
	
	/**
	 * Metodo que permite realizar la consulta del detalle de operaciones para el abono de cuenta en dolares
	 * */
	//Metodo creado en requerimiento SICAD nm26659
	public void detalleCobroSubastaDivisas(long idUnidad,String... statusOrden) throws Exception {
		
		StringBuilder sql = new StringBuilder();		
		
		sql.append("SELECT DISTINCT ord.ordene_id,nvl(ord.ORDENE_PED_COMISIONES,0) AS MONTO_COMISION, decode(ord.ordene_ped_total,0,ord.ordene_ped_total,ord.ordene_ped_total- ord.ordene_ped_comisiones) AS monto_capital, ");
		sql.append("ctes.tipper_id,ctes.client_cedrif,ctes.client_nombre, ord.ordene_ped_fe_valor,ord.ordene_veh_tom,ord.ordsta_id,ui.undinv_nombre, ");
		sql.append("NVL((SELECT SUM(oo.MONTO_OPERACION) FROM INFI_TB_207_ORDENES_OPERACION OO WHERE OO.ORDENE_ID=ord.ordene_id and oo.TRNF_TIPO='BLO'),0) as monto_bloqueo ");
		sql.append("FROM infi_tb_207_ordenes_operacion op,infi_tb_204_ordenes ord,infi_tb_201_ctes ctes,infi_tb_106_unidad_inversion ui ");
		sql.append("WHERE op.ordene_id = ord.ordene_id AND ord.client_id = ctes.client_id AND ui.undinv_id = ord.uniinv_id ");
		sql.append("AND  OP.STATUS_OPERACION IN ('").append(STATUS_EN_ESPERA).append("','").append(STATUS_RECHAZADA).append("') ");

			if(statusOrden.length>0 && statusOrden[0]!=null){
				int count=0;
				sql.append("AND ORD.ORDSTA_ID IN (");
				for (String element : statusOrden) {
					if(count>0){
						sql.append(",");
					}
					sql.append("'"+element+"'");
					++count;
				}
				//sql.append(" AND ORD.ORDSTA_ID='").append(statusOrden).append("' ");
				sql.append(") ");
			}
			/*sql.append("SELECT DISTINCT ord.ordene_id, ctes.tipper_id, ctes.client_cedrif,ctes.client_nombre, ord.ordene_ped_fe_valor, ");
			sql.append("ord.ordene_id_relacion, ui.undinv_id, ui.undinv_nombre,ord.ordene_veh_tom, ord.moneda_id, ord.cta_abono, ");
			sql.append("NVL(DECODE(DECODE(ord.ordsta_id,'"+StatusOrden.NO_ADJUDICADA_INFI+"', 0,ord.ordene_ped_total),0, ");
			sql.append("(SELECT oo.monto_operacion FROM infi_tb_207_ordenes_operacion oo WHERE oo.ordene_id = ord.ordene_id ");
			sql.append("AND ord.ordsta_id = '"+StatusOrden.NO_ADJUDICADA_INFI+"' AND oo.trnf_tipo = '"+TransaccionFinanciera.DEBITO+"' AND oo.status_operacion IN ('"+STATUS_EN_ESPERA+"','"+STATUS_RECHAZADA+"')), ");
			sql.append("ord.ordene_ped_total),0) AS monto, ord.ordsta_id ");
			sql.append("FROM infi_tb_207_ordenes_operacion op,infi_tb_204_ordenes ord,infi_tb_201_ctes ctes,infi_tb_106_unidad_inversion ui ");
			sql.append("WHERE op.ordene_id = ord.ordene_id AND ord.client_id = ctes.client_id AND ui.undinv_id = ord.uniinv_id ");			
			sql.append(" AND  OP.STATUS_OPERACION IN ('").append(STATUS_EN_ESPERA).append("','").append(STATUS_RECHAZADA).append("') ");*/
			
			if(idUnidad>0){
				sql.append(" AND UI.UNDINV_ID=").append(idUnidad);
			}			
			sql.append("  GROUP BY ord.ordene_id,ord.ORDENE_PED_COMISIONES, ctes.tipper_id, ctes.client_cedrif,ctes.client_nombre,ord.ordene_ped_fe_valor,ord.ordene_veh_tom, ord.ordsta_id, ord.ORDENE_PED_TOTAL,ui.undinv_nombre  ORDER BY ord.ordene_id ");			
			
			
			System.out.println(" detalleCobroSubastaDivisas -------> " + sql.toString());
			dataSet = db.get(dataSource, sql.toString());
			//return sql.toString();
	}
	
	public void detalleCobroSubastaDivisasPorIdOrden(long idOrden) throws Exception {
		
		StringBuilder sql = new StringBuilder();		
		
		sql.append("SELECT DISTINCT ord.ordene_id,nvl(ord.ORDENE_PED_COMISIONES,0) AS MONTO_COMISION, decode(ord.ordene_ped_total,0,ord.ordene_ped_total,ord.ordene_ped_total- nvl(ord.ordene_ped_comisiones,0)) AS monto_capital, ");
		sql.append("ctes.tipper_id,ctes.client_cedrif,ctes.client_nombre, ord.ordene_ped_fe_valor,ord.ordene_veh_tom,ord.ordsta_id,ui.undinv_nombre ");
		sql.append("FROM infi_tb_207_ordenes_operacion op,infi_tb_204_ordenes ord,infi_tb_201_ctes ctes,infi_tb_106_unidad_inversion ui ");
		sql.append("WHERE op.ordene_id = ord.ordene_id AND ord.client_id = ctes.client_id AND ui.undinv_id = ord.uniinv_id ");
		sql.append("AND  OP.STATUS_OPERACION IN ('").append(STATUS_EN_ESPERA).append("','").append(STATUS_RECHAZADA).append("') ");
				
			/*sql.append("SELECT DISTINCT ord.ordene_id, ctes.tipper_id, ctes.client_cedrif,ctes.client_nombre, ord.ordene_ped_fe_valor, ");
			sql.append("ord.ordene_id_relacion, ui.undinv_id, ui.undinv_nombre,ord.ordene_veh_tom, ord.moneda_id, ord.cta_abono, ");
			sql.append("NVL(DECODE(DECODE(ord.ordsta_id,'"+StatusOrden.NO_ADJUDICADA_INFI+"', 0,ord.ordene_ped_total),0, ");
			sql.append("(SELECT oo.monto_operacion FROM infi_tb_207_ordenes_operacion oo WHERE oo.ordene_id = ord.ordene_id ");
			sql.append("AND ord.ordsta_id = '"+StatusOrden.NO_ADJUDICADA_INFI+"' AND oo.trnf_tipo = '"+TransaccionFinanciera.DEBITO+"' AND oo.status_operacion IN ('"+STATUS_EN_ESPERA+"','"+STATUS_RECHAZADA+"')), ");
			sql.append("ord.ordene_ped_total),0) AS monto, ord.ordsta_id ");
			sql.append("FROM infi_tb_207_ordenes_operacion op,infi_tb_204_ordenes ord,infi_tb_201_ctes ctes,infi_tb_106_unidad_inversion ui ");
			sql.append("WHERE op.ordene_id = ord.ordene_id AND ord.client_id = ctes.client_id AND ui.undinv_id = ord.uniinv_id ");			
			sql.append(" AND  OP.STATUS_OPERACION IN ('").append(STATUS_EN_ESPERA).append("','").append(STATUS_RECHAZADA).append("') ");*/
			
			if(idOrden>0){
				sql.append("and ord.ORDENE_ID=").append(idOrden).append(" ");
			}			
			sql.append("  GROUP BY ord.ordene_id,ord.ORDENE_PED_COMISIONES, ctes.tipper_id, ctes.client_cedrif,ctes.client_nombre,ord.ordene_ped_fe_valor,ord.ordene_veh_tom, ord.ordsta_id, ord.ORDENE_PED_TOTAL,ui.undinv_nombre  ORDER BY ord.ordene_id ");			
			
			
			//System.out.println(" detalleCobroSubastaDivisas -------> " + sql.toString());
			dataSet = db.get(dataSource, sql.toString());
			//return sql.toString();
	}
	
	//TODO MODIFICAR PARA ENTRAGABLE 2
	//metodo creado en desarrollo requerimiento SICAD nm26659
	public String previoDeOperacionBatchPorIdOrden(String idOrdenes) throws Exception {
		StringBuffer sql=new StringBuffer();
		
		sql.append("SELECT   a.codigo_operacion, a.numero_retencion,TO_CHAR (a.fecha_aplicar, 'YYYYMMDD') fecha_operacion_padre, ");
		sql.append("a.ordene_relac_operacion_id, d.tipper_id, d.client_cedrif,d.client_nombre, b.ordene_id, a.ordene_operacion_id, ");
		sql.append("a.codigo_operacion, a.numero_retencion, a.ctecta_numero, c.undinv_id,c.undinv_nombre, a.status_operacion, a.monto_operacion, a.trnf_tipo,b.ordene_veh_tom ");
		sql.append("FROM infi_tb_207_ordenes_operacion a,infi_tb_204_ordenes b,infi_tb_106_unidad_inversion c,infi_tb_201_ctes d,infi_tb_101_inst_financieros e ");
		sql.append("WHERE a.ordene_id = b.ordene_id AND c.insfin_id = e.insfin_id AND b.uniinv_id = c.undinv_id and d.CLIENT_ID=b.CLIENT_ID ");
		sql.append("and a.status_operacion in('" + ConstantesGenerales.STATUS_EN_ESPERA + "','" + ConstantesGenerales.STATUS_RECHAZADA+ "') ");
		//sql.append("and a.trnf_tipo='"+ TransaccionFinanciera.DEBITO+"' ");
		
		if(idOrdenes!=null && !idOrdenes.equals("")){
			sql.append("and b.ordene_id in (").append(idOrdenes).append(")"); 
		}
		/*int count=0;
		if(idOrdenes!=null && idOrdenes.length>0){
			sql.append("and b.ordene_id in (");
			
			for (String idOrden : idOrdenes) {
				if(count>0){
					sql.append(",");
				}
				sql.append(idOrden);
				++count;
			}
			sql.append(")");
		}*/
		//System.out.println("previoDeOperacionBatchPorIdOrden ------> " + sql.toString());
		return sql.toString();
	}

//	TODO MODIFICAR PARA ENTRAGABLE 2
	//metodo creado en desarrollo requerimiento SICAD nm26659
	public String previoDeOperacionBatchPorIdOrden(String idOrdenes,String idUnidadInv,String[] transaccion,String... StatusOrden) throws Exception {
		StringBuffer sql=new StringBuffer();
		
		sql.append("SELECT TO_CHAR (a.fecha_aplicar, 'YYYYMMDD') fecha_operacion_padre, ");
		sql.append("a.ordene_relac_operacion_id, d.tipper_id, d.client_cedrif,d.client_nombre, b.ordene_id, b.ordene_id_relacion, a.ordene_operacion_id, ");
		sql.append("a.codigo_operacion, a.numero_retencion, a.ctecta_numero, c.undinv_id,c.undinv_nombre, a.status_operacion, a.monto_operacion, a.trnf_tipo,b.ordene_veh_tom ");
		sql.append("FROM infi_tb_207_ordenes_operacion a,infi_tb_204_ordenes b,infi_tb_106_unidad_inversion c,infi_tb_201_ctes d,infi_tb_101_inst_financieros e ");
		sql.append("WHERE a.ordene_id = b.ordene_id AND c.insfin_id = e.insfin_id AND b.uniinv_id = c.undinv_id and d.CLIENT_ID=b.CLIENT_ID ");
		sql.append("and a.status_operacion in('" + ConstantesGenerales.STATUS_EN_ESPERA + "','" + ConstantesGenerales.STATUS_RECHAZADA+ "') ");
		//sql.append("and a.trnf_tipo='"+ TransaccionFinanciera.DEBITO+"' ");
				
		if(idOrdenes!=null && !idOrdenes.equals("")){
			sql.append(" and b.ordene_id in (").append(idOrdenes).append(") "); 
		}
		
		if(idUnidadInv!=null && !idUnidadInv.equals("")){
			sql.append(" and c.undinv_id=").append(idUnidadInv).append(" ");
		}

		
		if(transaccion!=null && transaccion.length>0 && transaccion[0]!=null){
			int count=0;
			sql.append(" and b.transa_id in (");
			for (String element : transaccion) {
				if(count>0){
					sql.append(",");
				}
				sql.append("'"+element+"'");
				++count;
			}			
			sql.append(") ");
		}
		
		if(StatusOrden.length>0 && StatusOrden[0]!=null){
			sql.append(" AND b.ORDSTA_ID IN (");
			int count=0;
			
			for (String status : StatusOrden) {
				if(count>0){
					sql.append(",");
				}
				sql.append("'"+status+"'");
				++count;
			}
			
			sql.append(" ) ");
		}
		sql.append(" AND a.trnf_tipo IN ('").append(TransaccionFinanciera.DEBITO).append("', '").append(TransaccionFinanciera.DESBLOQUEO).append("', '").append(TransaccionFinanciera.CREDITO).append("')");
		sql.append("  ORDER BY ORDENE_ID DESC ");
		/*int count=0;
		if(idOrdenes!=null && idOrdenes.length>0){
			sql.append("and b.ordene_id in (");
			
			for (String idOrden : idOrdenes) {
				if(count>0){
					sql.append(",");
				}
				sql.append(idOrden);
				++count;
			}
			sql.append(")");
		}*/		
		System.out.println("previoDeOperacionBatchPorIdOrden ------> " + sql.toString());
		return sql.toString();
	}
	
	public String previoDeOperacionBatchAbonoCtaDolaresPorIdOrden(String idOrdenes,String idUnidadInv) throws Exception {
		StringBuffer sql=new StringBuffer();
		
		sql.append("SELECT   a.codigo_operacion, a.numero_retencion,TO_CHAR (a.fecha_aplicar, 'YYYYMMDD') fecha_operacion_padre, ");
		sql.append("a.ordene_relac_operacion_id, d.tipper_id, d.client_cedrif,d.client_nombre, b.ordene_id, b.ordene_id_relacion, a.ordene_operacion_id, ");
		sql.append("a.codigo_operacion, a.numero_retencion, a.ctecta_numero, c.undinv_id,c.undinv_nombre, a.status_operacion, a.monto_operacion, a.trnf_tipo,b.ordene_veh_tom ");
		sql.append("FROM infi_tb_207_ordenes_operacion a,infi_tb_204_ordenes b,infi_tb_106_unidad_inversion c,infi_tb_201_ctes d,infi_tb_101_inst_financieros e ");
		sql.append("WHERE a.ordene_id = b.ordene_id AND c.insfin_id = e.insfin_id AND b.uniinv_id = c.undinv_id and d.CLIENT_ID=b.CLIENT_ID ");
		sql.append("and a.status_operacion in('" + ConstantesGenerales.STATUS_EN_ESPERA + "','" + ConstantesGenerales.STATUS_RECHAZADA+ "') ");
		sql.append("AND b.TRANSA_ID='").append(TransaccionNegocio.ORDEN_PAGO).append("' ");
		sql.append("AND b.cta_abono =").append(ConstantesGenerales.ABONO_CUENTA_NACIONAL).append(" AND b.ordsta_id = '").append(StatusOrden.REGISTRADA).append("' ");
		sql.append(" AND NOT EXISTS (SELECT OP.ORDENE_ID  FROM infi_tb_207_ordenes_operacion op WHERE op.ordene_id = b.ordene_id_relacion AND op.trnf_tipo ='DEB' AND op.status_operacion IN ('EN ESPERA','RECHAZADA'))");
		//sql.append("and a.trnf_tipo='"+ TransaccionFinanciera.DEBITO+"' ");
		
		if(idOrdenes!=null && !idOrdenes.equals("")){
			sql.append(" and b.ordene_id_relacion in (").append(idOrdenes).append(") "); 
		}
		
		if(idUnidadInv!=null && !idUnidadInv.equals("")){
			sql.append(" and c.undinv_id=").append(idUnidadInv).append(" ");
		}
		/*int count=0;
		if(idOrdenes!=null && idOrdenes.length>0){
			sql.append("and b.ordene_id in (");
			
			for (String idOrden : idOrdenes) {
				if(count>0){
					sql.append(",");
				}
				sql.append(idOrden);
				++count;
			}
			sql.append(")");
		}*/		
		System.out.println("previoDeOperacionBatchAbonoCtaDolaresPorIdOrden ------> " + sql.toString());
		return sql.toString();
	}
	/**
	 * Metodo de busqueda de las operaciones relacionadas a una orden
	 * */
	public void detalleOperacionBatchPorIdOrden(long idOrdenes) throws Exception {
		StringBuffer sql=new StringBuffer();
		
		sql.append("SELECT   a.codigo_operacion, a.numero_retencion,TO_CHAR (a.fecha_aplicar, 'YYYYMMDD') fecha_operacion_padre, ");
		sql.append("a.ordene_relac_operacion_id, d.tipper_id, d.client_cedrif,d.client_nombre, b.ordene_id, a.ordene_operacion_id, ");
		sql.append("a.codigo_operacion, a.numero_retencion, a.ctecta_numero, c.undinv_id,c.undinv_nombre, a.status_operacion, a.monto_operacion, a.trnf_tipo,b.ordene_veh_tom,a.OPERACION_NOMBRE ");
		sql.append("FROM infi_tb_207_ordenes_operacion a,infi_tb_204_ordenes b,infi_tb_106_unidad_inversion c,infi_tb_201_ctes d,infi_tb_101_inst_financieros e ");
		sql.append("WHERE a.ordene_id = b.ordene_id AND c.insfin_id = e.insfin_id AND b.uniinv_id = c.undinv_id and d.CLIENT_ID=b.CLIENT_ID ");
		//sql.append("and a.status_operacion in('" + ConstantesGenerales.STATUS_EN_ESPERA + "','" + ConstantesGenerales.STATUS_RECHAZADA+ "') ");
		//sql.append("and a.trnf_tipo='"+ TransaccionFinanciera.DEBITO+"' ");
		
		if(idOrdenes>0){
			sql.append("and b.ordene_id =").append(idOrdenes); 
		}
		/*int count=0;
		if(idOrdenes!=null && idOrdenes.length>0){
			sql.append("and b.ordene_id in (");
			
			for (String idOrden : idOrdenes) {
				if(count>0){
					sql.append(",");
				}
				sql.append(idOrden);
				++count;
			}
			sql.append(")");
		}*/
		dataSet = db.get(dataSource, sql.toString());
		//System.out.println("detalleOperacionBatchPorIdOrden ------> " + sql.toString());
		
	}
	
	/**
	 * Lista las unidades de inversión asociadas a un tipo de producto
	 * Requerimiento TTS-401. NM25287
	 * @throws Exception
	 */
	public int cantidadUnidadesPorTipoProdEstatus(long unidadInversionID, String tipoProducto,String estatus) throws Exception {
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT COUNT(*) NUM FROM INFI_TB_106_UNIDAD_INVERSION UI WHERE UI.INSFIN_ID IN (");
		sql.append("SELECT INSFIN_ID FROM INFI_TB_101_INST_FINANCIEROS INF WHERE INF.TIPO_PRODUCTO_ID='"+tipoProducto+"'");
		sql.append("AND INF.TIPO_PRODUCTO_ID=(SELECT INF.TIPO_PRODUCTO_ID FROM INFI_TB_106_UNIDAD_INVERSION UI,INFI_TB_101_INST_FINANCIEROS INF WHERE ");
		sql.append("UI.INSFIN_ID=INF.INSFIN_ID AND UI.UNDINV_ID = "+unidadInversionID+"))");	
		if(estatus!=null&&estatus.length()>1){
			sql.append(" AND UI.UNDINV_STATUS='").append(estatus).append("'");		}
		
		//System.out.println("cantidadUnidadesPorTipoProdEstatus: "+sql.toString());
		dataSet = db.get(dataSource, sql.toString());		
		if(dataSet.count()>0){
			dataSet.first();
			dataSet.next();
			return Integer.parseInt(dataSet.getValue("NUM"));
		}
		return 0;
	}
	
	/**
	 * Valida si se encuentra activo el mercado 
	 * Retorna true si es posible cerrar la unidad de inversion en infi
	 * @throws Exception
	 */
	public boolean  validarMercadoAbiertoParametrosSitme(boolean validarSolicitudes, String estatusSolicitud) throws Exception {
		StringBuffer sql = new StringBuffer();
		//sql.append("SELECT 1 AS validacion FROM parametros_sitme WHERE id_parametro = 'HORARIO CIERRE' AND ((TO_DATE (TO_CHAR (SYSDATE, 'DD/MM/RRRR HH24:MI'), 'DD/MM/RRRR HH24:MI') < ");
		//sql.append("TO_DATE ((TO_CHAR (fecha_modificacion, 'DD/MM/RRRR') ||' '||RTRIM (valor)),'DD/MM/RRRR HH24:MI') OR (SELECT valor FROM parametros_sitme WHERE TRIM (id_parametro) = 'MERCADO ABIERTO') = 1)) ");
		sql.append("SELECT 1 AS validacion FROM DUAL WHERE");
		sql.append(" (SELECT valor FROM parametros_sitme WHERE TRIM (id_parametro) = 'MERCADO ABIERTO') = '0'");
		if(validarSolicitudes){
			sql.append(" AND (SELECT COUNT(*) FROM SOLICITUDES_SITME SS WHERE SS.NUMERO_ORDEN_INFI IS NULL AND SS.ESTATUS='"+estatusSolicitud+"')=0");
		}
				 
		//System.out.println("validarMercadoAbiertoParametrosSitme ---> "+sql.toString());
		dataSet = db.get(dataSource, sql.toString());		
		if(dataSet.count()>0){			
			return true;
		}
		return false;

	}
	
	//NM29643
	/**
	 * Lista la unidad de inversi&oacute;n activa en una fecha dada 
	 * 
	 * @param fecha
	 *            : Fecha que se quiere consultar
	 * @param status
	 *            : Estatus de la unidad de inversi&oacute;n
	 * @param tipo_producto
	 *            : Tipo de producto relacionado con la unidad de inversi&oacute;n
	 * @return cantidad de registros recuperados
	 * @throws Exception
	 */
	public int listarPorFechaYestatus(Date fecha, String status, String tipo_producto) throws Exception {
		//System.out.println("FECHA: "+sdIODate.format(fecha));
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ui.undinv_id, ui.undinv_nombre, ui.UNDINV_FE_EMISION, ui.UNDINV_FE_CIERRE ");
		sql.append("FROM INFI_TB_106_UNIDAD_INVERSION ui ");
		sql.append("WHERE to_date('").append(sdIODate.format(fecha)).append("','dd-MM-yyyy') ");
		sql.append("between ui.UNDINV_FE_EMISION and ui.UNDINV_FE_CIERRE ");
		sql.append("and ui.UNDINV_STATUS='").append(status).append("' ");
		sql.append("and ui.INSFIN_ID IN (select INSFIN_ID from INFI_TB_101_INST_FINANCIEROS where TIPO_PRODUCTO_ID='");
		sql.append(tipo_producto).append("')");
		
		dataSet = db.get(dataSource, sql.toString());
		//System.out.println("SELECT UIACTIVA\n"+sql.toString());
		return dataSet.count();
	}
	
	/**
	 * Lista las unidades de inversion por tipo de producto
	 * 
	 * @throws Exception
	 *             en caso de error
	 */
	public void listarUnidadesPorProductoStatus(String tipoProducto,String estatusOrden, String estatusUnidadInv ) throws Exception {
		StringBuffer sql=new StringBuffer();
		sql.append("select a.undinv_id, a.undinv_nombre, a.undinv_serie from infi_tb_106_unidad_inversion a, infi_tb_101_inst_financieros b where a.insfin_id = b.insfin_id ");
		if(tipoProducto!=null && !tipoProducto.equals("")){
			sql.append("and b.TIPO_PRODUCTO_ID = '" + tipoProducto + "' ");
		}
		
		if(estatusUnidadInv!=null && !estatusUnidadInv.equals("")){
			 sql.append("and a.UNDINV_STATUS = '" + estatusUnidadInv + "' "); 
		}
		
		
		 sql.append(" AND UNDINV_ID IN(SELECT UNIINV_ID FROM INFI_TB_204_ORDENES WHERE ORDSTA_ID='" + estatusOrden + "')");
		
		//System.out.println("listarUnidadesPorProductoStatus: "+sql.toString());
		dataSet = db.get(dataSource, sql.toString());

	}// fin listarUnidadesLiquidacion
	

	//Metodo creado en requerimiento TTS-401 NM26659
	public void listarUnidadesParaCobroLiqPorTipoProBatch(String tipoProducto) throws Exception {
				
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT undinv_id, undinv_nombre, undinv_serie FROM infi_tb_106_unidad_inversion a, infi_tb_101_inst_financieros b ");
		sql.append("WHERE a.insfin_id = b.insfin_id AND b.tipo_producto_id = '").append(tipoProducto).append("' ");// AND undinv_status = '").append(StatusOrden.LIQUIDADA).append("' ");
		sql.append("AND in_cobro_batch_liq = 1 AND undinv_id IN (SELECT uniinv_id FROM infi_tb_204_ordenes WHERE ordsta_id ='").append(StatusOrden.LIQUIDADA).append("' OR ordsta_id = '").append(StatusOrden.NO_ADJUDICADA_INFI).append("' ");
		sql.append("AND transa_id IN ('").append(TransaccionNegocio.TOMA_DE_ORDEN).append("','").append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA).append("') ");
		sql.append("AND ordene_id IN (SELECT ordene_id FROM infi_tb_207_ordenes_operacion WHERE status_operacion IN ('").append(STATUS_EN_ESPERA).append("','").append(STATUS_RECHAZADA).append("'))) ");
		sql.append(" UNION ");
		sql.append("SELECT uniinv_id,undinv_nombre, undinv_serie FROM infi_tb_204_ordenes a,infi_tb_106_unidad_inversion b,infi_tb_101_inst_financieros c ");
		sql.append("WHERE a.ordsta_id = '").append(StatusOrden.REGISTRADA).append("' ").append("AND A.UNIINV_ID=B.UNDINV_ID ").append(" AND b.insfin_id = c.insfin_id AND c.tipo_producto_id = '").append(tipoProducto).append("' ");
		sql.append("AND transa_id ='").append(TransaccionNegocio.LIQUIDACION).append("' AND ordene_id IN (SELECT ordene_id FROM infi_tb_207_ordenes_operacion WHERE status_operacion IN ('");
		sql.append(STATUS_EN_ESPERA).append("','").append(STATUS_RECHAZADA).append("'))");
		//System.out.println("listarUnidadesParaCobroLiqPorTipoProBatch ----> " + sql.toString());
		dataSet = db.get(dataSource, sql.toString());
	}
	
		/**
		 * Metodo que permite realizar la consulta del detalle de operaciones para el Cobro Liquidacion de operaciones pendientes
		 * */
		//Metodo creado en requerimiento TTS-401 NM26659
		public void detalleCobroLiquidacionSubastaDivisas(long idUnidad) throws Exception {
			
			StringBuilder sql = new StringBuilder();		
						
			sql.append("SELECT DISTINCT ord.ordene_id, NVL(ord.ordene_ped_comisiones,0) AS monto_comision,NVL(DECODE (ord.ordene_ped_total,0, ord.ordene_ped_total,ord.ordene_ped_total - NVL(ord.ordene_ped_comisiones,0)),0) AS monto_capital, ");
			sql.append("ctes.tipper_id, ctes.client_cedrif, ctes.client_nombre,ord.ordene_ped_fe_valor, ord.ordene_veh_tom, ord.ordsta_id,ui.undinv_nombre, ");
			sql.append("NVL((SELECT SUM (oo.monto_operacion) FROM infi_tb_207_ordenes_operacion oo WHERE oo.ordene_id = ord.ordene_id AND oo.trnf_tipo = 'BLO'),0) AS monto_bloqueo ");
			sql.append("FROM infi_tb_207_ordenes_operacion op,infi_tb_204_ordenes ord,infi_tb_201_ctes ctes,infi_tb_106_unidad_inversion ui ");
			sql.append("WHERE op.ordene_id = ord.ordene_id AND ord.client_id = ctes.client_id AND ui.undinv_id = ord.uniinv_id ");
			sql.append("AND  OP.STATUS_OPERACION IN ('").append(STATUS_EN_ESPERA).append("','").append(STATUS_RECHAZADA).append("') ");
			//sql.append("AND ui.UNDINV_STATUS='").append(StatusOrden.LIQUIDADA).append("' 
			sql.append("AND ((ORD.TRANSA_ID='").append(TransaccionNegocio.TOMA_DE_ORDEN).append("' AND").append(" ORD.ORDSTA_ID IN ('").append(StatusOrden.LIQUIDADA).append("','"+StatusOrden.NO_ADJUDICADA_INFI+"','"+StatusOrden.NO_CRUZADA+"'))  OR ").append("ORD.TRANSA_ID='").append(TransaccionNegocio.LIQUIDACION).append("' AND ord.ordsta_id = '").append(StatusOrden.REGISTRADA).append("' )");
			sql.append("AND ORD.transa_id IN ('").append(TransaccionNegocio.TOMA_DE_ORDEN).append("','").append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA).append("','").append(TransaccionNegocio.ORDEN_VEHICULO).append("','").append(TransaccionNegocio.LIQUIDACION).append("') ");
			sql.append("AND ui.in_cobro_batch_liq = 1 ");
			
			if(idUnidad>0){
				sql.append("AND ui.undinv_id =").append(idUnidad).append(" ");				
			}
			sql.append("GROUP BY ord.ordene_id,ord.ordene_ped_comisiones,ctes.tipper_id,ctes.client_cedrif,ctes.client_nombre, ");
			sql.append("ord.ordene_ped_fe_valor,ord.ordene_veh_tom,ord.ordsta_id,ord.ordene_ped_total,ui.undinv_nombre ORDER BY ord.ordene_id ");
													
			//System.out.println(" detalleCobroLiquidacionSubastaDivisas -------> " + sql.toString());			
			dataSet = db.get(dataSource, sql.toString());				
			//return sql.toString();
		}
		
		/**
		 * Resumen de la unidad de inversión. Muestra las operaciones de los vehículos involucrados
		 * 
		 * @param idUnidad
		 *            id de la unidad de inversión
		 * @throws Exception
		 *             en caso de error
		 */
		public void resumenParaCobroBatch(long idUnidad,String tipoProducto,ArrayList<String> transaId,String... statusOrden) throws Exception {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT  CASE A.TRNF_TIPO WHEN 'CRE' THEN 'CREDITO' WHEN 'DEB' THEN 'DEBITO' WHEN 'DES' THEN 'DESBLOQUEO' END AS TIPO_OPERACION,");
			sql.append("a.status_operacion,SUM (a.monto_operacion) monto_operacion, COUNT (a.ordene_id) cantidad_operaciones, D.TIPO_PRODUCTO_ID ");
			sql.append("FROM infi_tb_207_ordenes_operacion a,infi_tb_204_ordenes b,infi_tb_106_unidad_inversion c,infi_tb_101_inst_financieros d ");
			sql.append("WHERE a.ordene_id = b.ordene_id AND c.insfin_id = d.insfin_id AND b.uniinv_id = c.undinv_id ");
			sql.append("and a.status_operacion in('" + ConstantesGenerales.STATUS_EN_ESPERA + "','" + ConstantesGenerales.STATUS_RECHAZADA + "') ");

			if(transaId!=null && transaId.size()>0){
				int transCount=0;
				sql.append("and transa_id in(");
				for (String transaccion : transaId) {					
					if(transCount>0){
						sql.append(",");
					}
					sql.append("'"+transaccion+"'");					
					++transCount;
				}
				sql.append(") ");
			}
			
			
			if(tipoProducto!=null && !tipoProducto.equals("")){
				sql.append(" AND D.TIPO_PRODUCTO_ID='").append(tipoProducto).append("' ");	
			}
			
			if(idUnidad>0){
				sql.append(" AND uniinv_id =").append(idUnidad).append(" ");
			}
						
			if(statusOrden!=null && statusOrden.length>0 && statusOrden[0]!=null){
				int statusCount=0;
				sql.append(" AND ordsta_id IN (");
				for (String status : statusOrden) {					
					if(statusCount>0){
						sql.append(",");						
					} 
					sql.append("'"+status+"'");					
					++statusCount;
				}
				sql.append(") ");				
			}
			sql.append(" AND a.trnf_tipo IN ('").append(TransaccionFinanciera.DEBITO).append("', '").append(TransaccionFinanciera.DESBLOQUEO).append("', '").append(TransaccionFinanciera.CREDITO).append("')");
			sql.append(" GROUP BY a.status_operacion, a.trnf_tipo,D.TIPO_PRODUCTO_ID ");

			System.out.println("resumenParaCobroBatch ----> "  + sql.toString());
			dataSet = db.get(dataSource, sql.toString());
		}
		
		
		//NM29643 - INFI_TTS_423: Obtiene Unidad de Inversion consultando por su nombre
		/**
		 * 
		 * @param nombreUI
		 * @return count del DataSet
		 * @throws Exception
		 */
		public int getUiPorNombre(String nombreUI) throws Exception {
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT UI.*");
			sql.append(" FROM INFI_TB_106_UNIDAD_INVERSION UI");
			sql.append(" WHERE UPPER(UI.UNDINV_NOMBRE) like '%").append(nombreUI.toUpperCase()).append("%'");
			
			//System.out.println("listarPorNombre: "+sql.toString());
			dataSet = db.get(dataSource, sql.toString());
			return dataSet.count();
		}
		
		public void listarProductoUnidadPorId(long idUnidad)throws Exception {
			StringBuffer sql=new StringBuffer();
			sql.append("SELECT inst.tipo_producto_id FROM INFI_TB_106_UNIDAD_INVERSION UI,INFI_TB_101_INST_FINANCIEROS inst WHERE inst.INSFIN_ID=ui.INSFIN_ID and UI.UNDINV_ID=").append(idUnidad);
			
			dataSet = db.get(dataSource, sql.toString());
		}
			
		//Metodo creado en Desarrollo Requerimiento TTS_443 NM26659 
		public void listarUnidadesInversionPorNombreProducto(String tipoProducto, String nombreUI) throws Exception {
			StringBuffer sql=new StringBuffer();
			
			sql.append("SELECT DISTINCT /*+ index(infi_tb_207_ordenes_operacion INDX_207_ID_IDOPE) */ ui.undinv_id, ui.undinv_nombre, ui.undinv_serie ");
			sql.append("FROM infi_tb_207_ordenes_operacion oo,infi_tb_204_ordenes ord, infi_tb_106_unidad_inversion ui,infi_tb_101_inst_financieros insf ");
			sql.append("WHERE oo.ordene_id = ord.ordene_id AND ord.uniinv_id = ui.undinv_id AND ui.insfin_id = insf.insfin_id ");		
			//sql.append("AND OO.STATUS_OPERACION IN ('").append(STATUS_EN_ESPERA).append("','").append(STATUS_RECHAZADA).append("') 
			sql.append("AND OO.TRNF_TIPO !='").append(TransaccionFinanciera.MISCELANEO).append("' ");
			//sql.append("AND UNDINV_STATUS NOT IN ('"+UnidadInversionConstantes.UISTATUS_CERRADA+"') ");
			sql.append("AND UPPER(ui.UNDINV_NOMBRE) like '%").append(nombreUI.toUpperCase()).append("%'");
			
			if(tipoProducto!=null && !tipoProducto.equals("")){		
				sql.append(" AND INSF.TIPO_PRODUCTO_ID IN (").append(tipoProducto).append(")");
			}
			//System.out.println("listarUnidadesInversionPorNombreProducto ------->\n" + sql.toString());
					
			dataSet = db.get(dataSource, sql.toString());
		}
		
		/**
		 * Lista las unidades de inversion por tipo de producto,nombre y estatus
		 * 
		 * @throws Exception
		 *             en caso de error
		 */
		//NM26659 - 25/08/2015 Adecuacion para exclusion o inclusion de Unidades con forma orden TAQUILLA AEREOPUERTO
		//NM26659 - 27/05/2017 Se agrega argumento para el manejo de Unidades de Inversion de tipo Oferta
		public void listarUnidadesPorProductoStatusNombre(String tipoProducto, String estatusUnidadInv,String nombreUI,String tipoNegocio,boolean ventaTaquilla,boolean uiOferta) throws Exception {
			StringBuffer sql = new StringBuffer();
			String comparaFormaOrden=null;
			int tipoUnidadInv=0;//Por defecto valor cero (Demanda) 
			if(ventaTaquilla){
				comparaFormaOrden="=";
			} else {
				comparaFormaOrden="<>";
			}
			
			if(uiOferta){
				tipoUnidadInv=1;
			} 
						
			sql.append("select a.undinv_id, a.undinv_nombre, a.undinv_serie from infi_tb_106_unidad_inversion a, infi_tb_101_inst_financieros b,INFI_TB_038_INST_FORMA_ORDEN fo where a.insfin_id = b.insfin_id ");
			sql.append(" and  UPPER(a.UNDINV_NOMBRE) like '%").append(nombreUI.toUpperCase()).append("%' ");
			
			if(tipoProducto!=null && !tipoProducto.equals("")){				
				tipoProducto=tipoProducto.replaceAll(",", "','");
				sql.append("and b.TIPO_PRODUCTO_ID in ('").append(tipoProducto).append("') ");
			}
			//Adaptacion para tipo de producto SUBASTA_DIVISA_PERSONAL en el flujo de SICAD 2 nm26659 06/06/2014
			if(estatusUnidadInv!=null && !estatusUnidadInv.equals("")){
				estatusUnidadInv=estatusUnidadInv.replaceAll(",", "','");
				sql.append("and a.UNDINV_STATUS in ('").append(estatusUnidadInv).append("') ");
			}			 
			
			sql.append(" AND FO.INSFIN_FORMA_ORDEN=B.INSFIN_FORMA_ORDEN ");
			sql.append(" AND FO.INSFIN_FORMA_ORDEN").append(comparaFormaOrden).append("'"+ConstantesGenerales.INST_FINANCIERO_SIMADI_TAQUILLA+"'").append(" ");
			
			/*NM26659 - 25/08/2015 Adecuacion para exclusion o inclusion de Unidades con forma orden TAQUILLA AEREOPUERTO
			if(tipoNegocio.length>0 && tipoNegocio[0]!=null && !tipoNegocio[0].equals("")){				
				sql.append(" AND a.TIPO_NEGOCIO=").append( tipoNegocio[0]).append(" ");
			}*/
			if(tipoNegocio!=null && tipoNegocio.length()>0){
				sql.append(" AND a.TIPO_NEGOCIO=").append(tipoNegocio).append(" ");
			}
			
			sql.append(" AND  a.TIPO_SOLICITUD=").append(tipoUnidadInv).append(" ");
			//sql.append(" AND UNDINV_ID IN(SELECT UNIINV_ID FROM INFI_TB_204_ORDENES WHERE ORDSTA_ID='" + estatusOrden + "')");
			
			//System.out.println("listarUnidadesPorProductoStatusNombre: "+sql);
			dataSet = db.get(dataSource, sql.toString());

		}// 
	 	 
		public void consultarStatusUI(long ui) throws Exception {
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT undinv_status ");
			sql.append("FROM INFI_TB_106_UNIDAD_INVERSION ");
			sql.append("WHERE undinv_id = ").append(ui);
			

			dataSet = db.get(dataSource, sql.toString());
			System.out.println("consultarStatusUI---> "+sql);
		}

		//NM26659 TTS_491 WEB SERVICE ALTO VALOR (Inclusion campo TIPO_PRODUCTO_ID en la consulta)
		public void listarUnidadInversionPorId(long idUnidad)throws Exception {
			StringBuffer sql=new StringBuffer();
			sql.append("SELECT UI.UNDINV_STATUS,UI.INSFIN_ID,UIT.UITITU_IN_CONIDB,ins.TIPO_PRODUCTO_ID FROM INFI_TB_106_UNIDAD_INVERSION UI, INFI_TB_101_INST_FINANCIEROS INS,INFI_TB_108_UI_TITULOS UIT ");
			sql.append(" WHERE INS.INSFIN_ID = UI.INSFIN_ID ");
			sql.append(" AND UIT.UNDINV_ID=UI.UNDINV_ID ");
		
			if(idUnidad>0){
				sql.append(" AND UI.UNDINV_ID=").append(idUnidad);	
			}
			
			dataSet = db.get(dataSource, sql.toString());
		}
		
		/**
		 * Lista las unidades de inversión asociadas a un tipo de producto
		 * Requerimiento TTS-401. NM25287
		 * @throws Exception
		 */
		public int cantidadUnidadesActivas(String tipoProducto,String estatus,long activa) throws Exception {
			StringBuffer sql=new StringBuffer();
			sql.append("SELECT COUNT (*) AS num FROM infi_tb_106_unidad_inversion ui WHERE ui.insfin_id IN (");
			sql.append("SELECT inst.insfin_id FROM infi_tb_101_inst_financieros inst WHERE inst.tipo_producto_id = '");			
			sql.append(tipoProducto).append("') ");
			sql.append("AND ui.undinv_active = ").append(activa).append(" ");
			
			if(estatus!=null && !estatus.equals("")){
				sql.append(" AND UI.UNDINV_STATUS='").append(estatus).append("'");		}
			
			sql.append(" AND UI.UNDINV_ACTIVE=").append(activa).append(" ");
			System.out.println("cantidadUnidadesPorTipoProdEstatus: "+sql.toString());
			
			dataSet = db.get(dataSource, sql.toString());		
			if(dataSet.count()>0){
				dataSet.first();
				dataSet.next();
				return Integer.parseInt(dataSet.getValue("NUM"));
			}
			return 0;
		}
		
		/**
		 * Lista las unidades de inversión asociadas a un tipo de producto que se encuentran activas
		 * Requerimiento TTS-465. CT19940
		 * @throws Exception
		 */
 		//TTS-491_NM26659_18/02/2015
		public void listarUnidadesActivas(String tipoProducto,String estatus,long activa,long... tipoNegocio) throws Exception {
			StringBuffer sql=new StringBuffer();
			sql.append("SELECT ui.undinv_status, ui.undinv_id, ui.undinv_nombre, ui.undinv_serie num FROM infi_tb_106_unidad_inversion ui WHERE ui.insfin_id IN (");
			sql.append("SELECT inst.insfin_id FROM infi_tb_101_inst_financieros inst WHERE inst.tipo_producto_id = '");			
			sql.append(tipoProducto).append("') ");
			sql.append("AND ui.undinv_active = ").append(activa).append(" ");
			
			if(tipoNegocio.length>0 && tipoNegocio[0]!=0){
				int count=0;
				sql.append("AND UI.TIPO_NEGOCIO IN (");	
				for (long element : tipoNegocio) {
					if(count>0){
						sql.append(",");
					}
					sql.append(element);
					++count;
				}
				sql.append(") ");
			}
			//sql.append("AND UI.TIPO_NEGOCIO = ").append(tipoNegocio).append(" ");
			
			if(estatus!=null && !estatus.equals("")){
				sql.append(" AND UI.UNDINV_STATUS='").append(estatus).append("'");		}

			System.out.println("cantidadUnidadesPorTipoProdEstatus: "+sql.toString());
			
			dataSet = db.get(dataSource, sql.toString());		

		}
		
		//TTS-491_NM26659_18/02/2015
		public void activacionUnidaInv(String idUndInv,String accion)throws Exception{
			
			//StringBuffer desactivacionSql=new StringBuffer();
			StringBuffer actualizacionSql=new StringBuffer();
			ArrayList<String> querys=new ArrayList<String>();
			
			/*if(accion.equals(String.valueOf(ConstantesGenerales.STATUS_ACTIVO))){
				desactivacionSql.append("UPDATE INFI_TB_106_UNIDAD_INVERSION UI SET UI.UNDINV_ACTIVE=0 WHERE UI.UNDINV_ACTIVE=1");
				querys.add(desactivacionSql.toString());
			}*/
			
			actualizacionSql.append("UPDATE INFI_TB_106_UNIDAD_INVERSION UI SET UI.UNDINV_ACTIVE=").append(accion).append(" WHERE UI.UNDINV_ID=").append(idUndInv);
			querys.add(actualizacionSql.toString());
			
			for (String elemen : querys) {
				db.exec(dataSource,elemen);	
			}

		}
		
	public boolean isUnidaInvActiva(String idUndInv)throws Exception{
								
			StringBuffer sql=new StringBuffer();
			String undinvActive="";
			boolean flag=false;
			
			sql.append("SELECT ui.UNDINV_ACTIVE FROM INFI_TB_106_UNIDAD_INVERSION ui WHERE ui.undinv_id=").append(idUndInv);
			
			dataSet = db.get(dataSource, sql.toString());			
			if(dataSet.count()>0){
				dataSet.first();
				if(dataSet.next()){
					undinvActive=dataSet.getValue("undinv_active");					
					if(undinvActive.equals(String.valueOf(ConstantesGenerales.STATUS_ACTIVO))){
						flag=true;
					}else {
						flag=false;
					}
				}
			}
						
			return flag;

		}
	/**
	 * Metodo que permite realizar la busqueda las operaciones de abono para cuentas nacional en dolares pendientes por cobrar
	 * */
	public void pendientesAbonoCuentaDolaresPorUnidadYTipoTransaccion(int idUnidad) throws Exception {
		
		StringBuilder sql = new StringBuilder();
		
		/*sql.append("SELECT COUNT(DISTINCT(OP.ORDENE_ID))as CANTIDAD_OPERACIONES_PEND, SUM(OP.MONTO_OPERACION)as MONTO_OPERACIONES_PEND, OP.STATUS_OPERACION "); 
		sql.append("FROM INFI_TB_204_ORDENES ORD, INFI_TB_207_ORDENES_OPERACION OP ");
		sql.append("WHERE OP.ORDENE_ID = ORD.ORDENE_ID AND OP.TRNF_TIPO ='DEB' AND ORD.TRANSA_ID = '").append(TRANSA_ID).append("' AND ORD.ORDSTA_ID = '").append(StatusOrden.LIQUIDADA).append("' AND ORD.CTA_ABONO =").append(ConstantesGenerales.ABONO_CUENTA_NACIONAL).append("  AND OP.STATUS_OPERACION IN ('").append(STATUS_EN_ESPERA).append("','").append(STATUS_RECHAZADA).append("') ");*/
		sql.append("SELECT COUNT (DISTINCT (op_pg.ordene_id)) AS cantidad_operaciones_pend," +
				"	SUM (op_pg.monto_operacion) AS monto_operaciones_pend," +
				"	op_pg.status_operacion ");
		sql.append("FROM infi_tb_204_ordenes ord_pg, infi_tb_207_ordenes_operacion op_pg WHERE op_pg.ordene_id = ord_pg.ordene_id ");
		sql.append("AND op_pg.TRNF_TIPO ='CRE' AND ord_pg.TRANSA_ID = '").append(TransaccionNegocio.ORDEN_PAGO).append("' AND ord_pg.ORDSTA_ID = '").append(StatusOrden.REGISTRADA).append("' AND ord_pg.CTA_ABONO =").append(ConstantesGenerales.ABONO_CUENTA_NACIONAL).append("  AND op_pg.STATUS_OPERACION IN ('").append(STATUS_EN_ESPERA).append("','").append(STATUS_RECHAZADA).append("') ");
				
		
		if(idUnidad>0){
			sql.append(" AND ord_pg.UNIINV_ID=").append(idUnidad).append(" ");
		}
		sql.append("AND EXISTS (SELECT op.ordene_id FROM infi_tb_204_ordenes ord, infi_tb_207_ordenes_operacion op WHERE op.ordene_id = ord.ordene_id ");
		sql.append("AND op.trnf_tipo = 'DEB' AND ord.transa_id ='").append(TransaccionNegocio.TOMA_DE_ORDEN).append("' AND ord.ordsta_id ='").append(StatusOrden.LIQUIDADA).append("' ");
		sql.append("AND ord.cta_abono =").append(ConstantesGenerales.ABONO_CUENTA_NACIONAL).append(" AND op.status_operacion IN ('").append(STATUS_EN_ESPERA).append("','").append(STATUS_RECHAZADA).append("') ").append(" AND ord.ordene_id = ord_pg.ordene_id_relacion) ");
		sql.append(" GROUP BY op_pg.ORDENE_ID , op_pg.MONTO_OPERACION, op_pg.STATUS_OPERACION");
		
		//System.out.println("pendientesAbonoCuentaDolaresPorUnidadYTipoTransaccion ----> " + sql.toString());
		dataSet = db.get(dataSource, sql.toString());
	}	
	
	
	/**
	 * NM25287 26/03/2015 CONSULTAR ID DE JORNADA SIMADI. TTS-491 WS ALTO VALOR
	 * @param idUnidadInversion
	 * @param idJornada
	 * @return
	 * @throws Exception
	 */
	public int actualizarIdJornada(long idUnidadInversion,String idJornada) throws Exception {

		StringBuffer sqlSB = new StringBuffer("UPDATE INFI_TB_106_UNIDAD_INVERSION SET NRO_JORNADA='").append(idJornada);
		sqlSB.append("' where undinv_id = ");
		sqlSB.append(idUnidadInversion + "");

		db.exec(dataSource, sqlSB.toString());
		return 0;
	}
	
	/**
	 * NM25287 14/04/2015 Consulta la jornada asociada al ultimo dia de toma de ordenes de la unidad de inversion alto valor
	 * @param tipoNegocio
	 * @param fecha
	 * @return
	 * @throws Exception
	 */
	public String consultarJornada(int tipoNegocio,String fecha,String uiId) throws Exception {
		String idJornada=null;
		StringBuffer sqlSB = new StringBuffer("SELECT NRO_JORNADA FROM INFI_TB_106_UNIDAD_INVERSION U WHERE U.UNDINV_STATUS='").append(UnidadInversionConstantes.UISTATUS_PUBLICADA);
		sqlSB.append("' AND to_date('").append(fecha).append("','").append(ConstantesGenerales.FORMATO_FECHA_SYSDATE_2).append("')>=to_date((SELECT MAX(UIBLOT_PEDIDO_FE_FIN) FECHA FROM INFI_TB_107_UI_BLOTTER UIB WHERE UIB.UNDINV_ID=U.UNDINV_ID),'").append(ConstantesGenerales.FORMATO_FECHA_SYSDATE_2).append("')");
		sqlSB.append(" AND TIPO_NEGOCIO=").append(tipoNegocio);
		sqlSB.append(" AND UNDINV_ID=").append(uiId);
		
		//System.out.println("consultarJornada: "+sqlSB.toString());
		dataSet = db.get(dataSource, sqlSB.toString());			
		if(dataSet.count()>0){
			dataSet.first();
			if(dataSet.next()){
				idJornada=dataSet.getValue("NRO_JORNADA");
			}
		}
		return idJornada;
	}
	
	/**
	 * NM32454 15/05/2015 Consulta la jornada asociada a la unidad de inversion
	 * @param uiId
	 * @return String
	 * @throws Exception
	 */
	public DataSet consultarUI(Long uiId) throws Exception {
		StringBuffer sqlSB = new StringBuffer("SELECT * FROM INFI_TB_106_UNIDAD_INVERSION U WHERE UNDINV_ID=").append(uiId);
		return db.get(dataSource, sqlSB.toString());			
	}
	
	/**
	 * NM25287 07/04/2015 VALIDAR SI ES EL ULTIMO DIA DE TOMA DE ORDEN EN LA UNIDAD DE INVERSION. TTS-491 WS ALTO VALOR
	 * @param idUnidadInversion
	 * @return
	 * @throws Exception
	 */
	public boolean validarUltimoDiaUI(long idUnidadInversion) throws Exception {

		StringBuffer sqlSB = new StringBuffer("SELECT 1 FROM (SELECT MAX(UIBLOT_PEDIDO_FE_FIN) FECHA FROM INFI_TB_107_UI_BLOTTER UIB WHERE UIB.UNDINV_ID="+idUnidadInversion+") WHERE FECHA<=TO_DATE(SYSDATE,'").append(ConstantesGenerales.FORMATO_FECHA_SYSDATE_2).append("')");
	
		System.out.println("validarUltimoDiaUI: "+sqlSB.toString());
		dataSet = db.get(dataSource, sqlSB.toString());			
		if(dataSet.count()>0){
			dataSet.first();
			if(dataSet.next()){
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * NM25287 09/04/2015 VALIDAR SI EXISTEN OPERACIONES ENVIADAS AL BCV. TTS-491 WS ALTO VALOR
	 * @param idUnidadInversion
	 * @return
	 * @throws Exception
	 */
	public boolean existenOrdenesEnviadasBCV(long idUnidadInversion) throws Exception {

		StringBuffer sqlSB = new StringBuffer("SELECT 1 FROM DUAL WHERE ");	
		sqlSB.append("(SELECT COUNT(*) FROM INFI_TB_204_ORDENES O WHERE O.ORDENE_ESTATUS_BCV IN("+ConstantesGenerales.VERIFICADA_APROBADA+","+ConstantesGenerales.VERIFICADA_RECHAZADA+") AND O.UNIINV_ID=0)>0 ");
		sqlSB.append(" OR ");
		sqlSB.append("(SELECT COUNT(*) FROM INFI_TB_225_OFERTAS_SIMADI OS WHERE OS.ORDENE_ESTATUS_BCV IN("+ConstantesGenerales.VERIFICADA_APROBADA+","+ConstantesGenerales.VERIFICADA_RECHAZADA+") AND OS.FECHA_PACTO=TO_DATE(SYSDATE,'").append(ConstantesGenerales.FORMATO_FECHA_SYSDATE_2).append("'))>0");

		//System.out.println("validarOrdenesEnviadasBCV: "+sqlSB.toString());
		dataSet = db.get(dataSource, sqlSB.toString());			
		if(dataSet.count()>0){
			dataSet.first();
			if(dataSet.next()){
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * NM26659 28/01/2018 CONSULTAR UNIDAD DE INVERSION POR ID DE JORNADA. TTS-546 DESARROLLO DICOM INTERBANCARIO
	 * @param idJornada
	 * @return
	 * @throws Exception
	 */
	public void listaUnidadInversionPorIdJornada(String idJornada) throws Exception {

		StringBuffer sqlSB = new StringBuffer("SELECT * FROM INFI_TB_106_UNIDAD_INVERSION WHERE  NRO_JORNADA='").append(idJornada).append("'");
		//System.out.println("listaUnidadInversionPorIdJornada ---> " + sqlSB.toString());
		dataSet = db.get(dataSource, sqlSB.toString());
		
	}
		
}

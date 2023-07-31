package com.bdv.infi.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import com.bdv.infi.data.ClientesMesa;
import com.bdv.infi.data.ClientesMesaBancario;

import megasoft.db;

/**
 * Clase para buscar, agregar, modificar y eliminar las operaciones de mesa de cambio que van a permitir la validación por parte del usuario para la notificación
 */
public class MesaCambioDAO extends com.bdv.infi.dao.GenericoDAO {

	public String Dinamico = "";
	protected StringBuilder sql = new StringBuilder();
	List<ClientesMesa> lst = new ArrayList<ClientesMesa>();
	List<ClientesMesaBancario> lstI = new ArrayList<ClientesMesaBancario>();

	public MesaCambioDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
	}

	public MesaCambioDAO(DataSource ds) {
		super(ds);
		sql.append("SELECT ID_OPER, COD_FINSTITUCION, ID_OC, PRODUCTO, TIPO_PACTO, TIPO_INSTRUM, TIPO_OPER, MTO_DIVISAS, MTO_BOLIVARES, TASA_CAMBIO, MTO_COMI, " + "TIPO_PER_OFER, decode(TIPO_PER_OFER,'V',LPAD(CED_RIF_OFER,8,'0') ,'E',LPAD(CED_RIF_OFER,8,'0') ,LPAD(CED_RIF_OFER,9,'0')) as CED_RIF_OFER, NOM_OFER, CTA_OFER_MN, CTA_OFER_ME, TIPO_PER_DEMA, decode(TIPO_PER_DEMA,'V',LPAD(CED_RIF_DEMA,8,'0') ,'E',LPAD(CED_RIF_DEMA,8,'0') ,LPAD(CED_RIF_DEMA,9,'0')) as CED_RIF_DEMA, "
				+ "NOM_DEMA, CTA_DEMA_MN, CTA_DEMA_ME, FECH_OPER, COD_DIVISAS, decode(STATUS_ENVIO,'0','NO ENVIADO','1','ENVIADO','2','MANUAL','3','ANULADA','4','RECHAZADA') as Estatus, " + "FECHA_ENVIO, OBSERVACION, ID_BCV ");
		sql.append("FROM INFI_TB_236_OPER_DIVISAS ");
	}

	/**
	 * 
	 * 
	 * @throws Exception
	 */
	public void listar() throws Exception {

		StringBuffer sb = new StringBuffer();
		sb.append("select oficina_nro from infi_tb_034_oficina");
		dataSet = db.get(dataSource, sb.toString());
	}

	public void Listar(boolean paginado, int paginaAMostrar, int registroPorPagina, String tipo, String fecha, String estatusEnvio, String tipoMivimiento, String nacionalidad, String rifCedula, int movimiento) throws Exception {

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT ID_OPER, TIPO_PACTO, ID_OC,COD_FINSTITUCION, PRODUCTO, TIPO_OPER, MTO_DIVISAS, MTO_BOLIVARES, TASA_CAMBIO, MTO_COMI, " + "TIPO_PER_OFER, decode(TIPO_PER_OFER,'V',LPAD(CED_RIF_OFER,8,'0') ,'E',LPAD(CED_RIF_OFER,8,'0') ,LPAD(CED_RIF_OFER,9,'0')) as CED_RIF_OFER, NOM_OFER, CTA_OFER_MN, CTA_OFER_ME, TIPO_PER_DEMA, decode(TIPO_PER_DEMA,'V',LPAD(CED_RIF_DEMA,8,'0') ,'E',LPAD(CED_RIF_DEMA,8,'0') ,LPAD(CED_RIF_DEMA,9,'0')) as CED_RIF_DEMA, " + "NOM_DEMA, CTA_DEMA_MN, CTA_DEMA_ME, FECH_OPER, COD_DIVISAS, " + "decode(STATUS_ENVIO,'0','NO ENVIADO','1','ENVIADO','2','MANUAL','3','ANULADA','4','RECHAZADA') as Estatus, "
				+ "FECHA_ENVIO, OBSERVACION, ID_BCV, MTO_PACTOBASE, MTO_CONTRAVALORBASE, TASA_PACTOBASE, TIPO_INSTRUM ");
		sql.append("FROM INFI_TB_236_OPER_DIVISAS ");
		sql.append("WHERE ");
		sql.append(" TO_DATE(FECH_OPER,'DD/MM/YYYY') = TO_DATE('").append(fecha).append("', 'DD/MM/YYYY') ");

		if (movimiento == 1) {
			if (nacionalidad.length() > 0 || rifCedula.length() > 0) {
				sql.append(" AND TIPO_PER_DEMA='").append(nacionalidad).append("' ");
				sql.append(" AND CED_RIF_DEMA='").append(rifCedula).append("' ");
			}
		} else if (movimiento == 2) {
			if (nacionalidad.length() > 0 || rifCedula.length() > 0) {
				sql.append(" AND TIPO_PER_OFER='").append(nacionalidad).append("' ");
				sql.append(" AND CED_RIF_OFER='").append(rifCedula).append("' ");
			}
		}
		
		// 5 Es igual a decir todas las operaciones, se manda en el formulario filter
		if (!estatusEnvio.equalsIgnoreCase("5")) {
			sql.append("AND STATUS_ENVIO='").append(estatusEnvio).append("' ");
		}

		if (!tipoMivimiento.equalsIgnoreCase("1111")) {
			sql.append("AND TIPO_PACTO='").append(tipoMivimiento).append("' ");
		}

//		if (!tipo.equalsIgnoreCase("T")) {
//			sql.append("AND TIPO_NOTIFICACION='").append(tipo).append("' ");
//		}
		sql.append(" ORDER BY ID_OPER");
		System.out.println("Listar : " + sql);
		dataSet = obtenerDataSetPaginado(sql.toString(), paginaAMostrar, registroPorPagina);

	}
	
	public void ListarTotales(boolean paginado, int paginaAMostrar, int registroPorPagina, String fecha) throws Exception {

		StringBuilder sql = new StringBuilder();

		sql.append(" SELECT TO_CHAR (SUM (MTO_DIVISAS),'9,999,999,999,999.00','NLS_NUMERIC_CHARACTERS = '',.''') AS MONTO,DECODE (COD_DIVISAS,  '840', 'DOLARES',  '978', 'EUROS',  COD_DIVISAS) AS MONEDA,DECODE (TIPO_OPER,  'V', 'VENTA',  'C', 'COMPRA',  TIPO_OPER) AS OPERACION ");
		sql.append("FROM INFI_TB_236_OPER_DIVISAS ");
		sql.append("WHERE ");
		sql.append(" TO_DATE(FECH_OPER,'DD/MM/YYYY') = TO_DATE('").append(fecha).append("', 'DD/MM/YYYY') ");

		sql.append(" GROUP BY TIPO_OPER, COD_DIVISAS ORDER BY COD_DIVISAS");
		System.out.println("ListarTotales : " + sql);
		dataSet = obtenerDataSetPaginado(sql.toString(), paginaAMostrar, registroPorPagina);

	}
	
	
	public void ListarExportar( String fecha, String estatusEnvio, String tipoMivimiento, String nacionalidad, String rifCedula, int movimiento) throws Exception {

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT ID_OPER, TIPO_PACTO, ID_OC,COD_FINSTITUCION, PRODUCTO, TIPO_OPER, MTO_DIVISAS, MTO_BOLIVARES, TASA_CAMBIO, MTO_COMI, " + "TIPO_PER_OFER, CED_RIF_OFER, NOM_OFER, CTA_OFER_MN, CTA_OFER_ME, TIPO_PER_DEMA, CED_RIF_DEMA, " + "NOM_DEMA, CTA_DEMA_MN, CTA_DEMA_ME, FECH_OPER, COD_DIVISAS, " + "decode(STATUS_ENVIO,'0','NO ENVIADO','1','ENVIADO','2','MANUAL','3','ANULADA','4','RECHAZADA') as Estatus, "
				+ "FECHA_ENVIO, OBSERVACION, ID_BCV, TIPO_INSTRUM, MTO_PACTOBASE, MTO_CONTRAVALORBASE, TASA_PACTOBASE ");
		sql.append("FROM INFI_TB_236_OPER_DIVISAS ");
		sql.append("WHERE ");
		sql.append(" TO_DATE(FECH_OPER,'DD/MM/YYYY') = TO_DATE('").append(fecha).append("', 'DD/MM/YYYY') ");

		if (movimiento == 1) {
			if (nacionalidad.length() > 0 || rifCedula.length() > 0) {
				sql.append(" AND TIPO_PER_DEMA='").append(nacionalidad).append("' ");
				sql.append(" AND CED_RIF_DEMA='").append(rifCedula).append("' ");
			}
		} else if (movimiento == 2) {
			if (nacionalidad.length() > 0 || rifCedula.length() > 0) {
				sql.append(" AND TIPO_PER_OFER='").append(nacionalidad).append("' ");
				sql.append(" AND CED_RIF_OFER='").append(rifCedula).append("' ");
			}
		}
		
		
		
		// 5 Es igual a decir todas las operaciones, se manda en el formulario filter
		if (!estatusEnvio.equalsIgnoreCase("5")) {
			sql.append("AND STATUS_ENVIO='").append(estatusEnvio).append("' ");
		}

//		if (!tipoMivimiento.equalsIgnoreCase("1111")) {
//			sql.append("AND PRODUCTO='").append(tipoMivimiento).append("' ");
//		}
		if (!tipoMivimiento.equalsIgnoreCase("1111")) {
			sql.append("AND TIPO_PACTO='").append(tipoMivimiento).append("' ");
		}

//		if (!tipo.equalsIgnoreCase("T")) {
//			sql.append("AND TIPO_NOTIFICACION='").append(tipo).append("' ");
//		}
		sql.append(" ORDER BY ID_OPER");
		System.out.println("ListarExportar : " + sql);
		dataSet = db.get(dataSource, sql.toString());

	}
	
	public void Listar(String tipo, String fecha, String estatusEnvio, String tipoMivimiento, String nacionalidad, String rifCedula) throws Exception {

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT  COUNT(*) as cant,SUM(MTO_DIVISAS) as total ");
		sql.append("FROM INFI_TB_236_OPER_DIVISAS ");
		sql.append("WHERE ");
		sql.append(" TO_DATE(FECH_OPER,'DD/MM/YYYY') = TO_DATE('").append(fecha).append("', 'DD/MM/YYYY') ");

		
		if (nacionalidad.length() > 0 || rifCedula.length() > 0) {
			sql.append(" AND TIPO_PER_OFER='").append(nacionalidad).append("' ");
			sql.append(" AND CED_RIF_OFER='").append(rifCedula).append("' ");
		}
		// 5 Es igual a decir todas las operaciones, se manda en el formulario filter
		if (!estatusEnvio.equalsIgnoreCase("5")) {
			sql.append("AND STATUS_ENVIO='").append(estatusEnvio).append("' ");
		}

		if (!tipoMivimiento.equalsIgnoreCase("1111")) {
			sql.append("AND TIPO_PACTO='").append(tipoMivimiento).append("' ");
		}

//		if (!tipo.equalsIgnoreCase("T")) {
//			sql.append("AND TIPO_NOTIFICACION='").append(tipo).append("' ");
//		}
		sql.append(" ORDER BY ID_OPER");
		System.out.println("Listar : " + sql);
		dataSet = db.get(dataSource, sql.toString());

	}
	
	public void ListarInter(boolean paginado, int paginaAMostrar, int registroPorPagina, String fecha, String estatusEnvio,String movimiento) throws Exception {

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT ID, TIPO_OPER, RIF_CLIENTE, NOMBRE_CLIENTE, CODIGO_MONEDA, MONTO, TASA_CAMBIO, TO_CHAR(FECHA,'DD-MM-YYYY') FECHA, CODIGO_INSTITUCION, ID_JORNADA, CUENTA_ME, CUENTA_MN, TIPO_INSTRUMENTO, OBSERVACION, ESTATUS, ID_BCV, TIPO_CLIENTE");
		sql.append(" FROM INFI_TB_236_INTERBANCARIAS ");
		sql.append("WHERE ");
		sql.append(" TO_CHAR(FECHA,'DD-MM-YYYY') = '"+fecha+"' ");
		if (!estatusEnvio.equalsIgnoreCase("5")) {
			sql.append("AND ESTATUS='").append(estatusEnvio).append("' ");
		}
		sql.append("AND TIPO_OPER='").append(movimiento).append("' ");
		sql.append(" ORDER BY ID");
		System.out.println("ListarInter : " + sql);
		dataSet = obtenerDataSetPaginado(sql.toString(), paginaAMostrar, registroPorPagina);

	}
	
	public void ListarInterSinPaginador(String fecha, String estatusEnvio,String movimiento) throws Exception {

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT ID, TIPO_OPER, RIF_CLIENTE, NOMBRE_CLIENTE, CODIGO_MONEDA, MONTO, TASA_CAMBIO, TO_CHAR(FECHA,'DD-MM-YYYY') FECHA, CODIGO_INSTITUCION, ID_JORNADA, CUENTA_ME, CUENTA_MN, TIPO_INSTRUMENTO, OBSERVACION, ESTATUS, ID_BCV, TIPO_CLIENTE");
		sql.append(" FROM INFI_TB_236_INTERBANCARIAS ");
		sql.append("WHERE ");
		sql.append(" TO_CHAR(FECHA,'DD-MM-YYYY') = '"+fecha+"' ");
		if (!estatusEnvio.equalsIgnoreCase("5")) {
			sql.append("AND ESTATUS='").append(estatusEnvio).append("' ");
		}
		sql.append("AND TIPO_OPER='").append(movimiento).append("' ");
		sql.append(" ORDER BY ID");
		System.out.println("ListarInter : " + sql);
		dataSet = db.get(dataSource, sql.toString());

	}
	
	public void ListarPactos(boolean paginado, int paginaAMostrar, int registroPorPagina,String fecha) throws Exception {

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT  ID, ID_JORNADA, TIPO_OPER, CODIGO_OFERTA, CODIGO_DEMANDA, MONTO, TO_CHAR(FECHA,'DD-MM-YYYY') FECHA, MONTO_PACTO_BASE, MONTO_CONTRAVALOR_BASE, TASA_CAMBIO, TIPO_PACTO, OBSERVACION, ESTATUS, ID_BCV");
		sql.append("  FROM ADM_INFI.INFI_TB_236_PACTOS ");
		sql.append("WHERE ");
		sql.append(" TO_CHAR(FECHA,'DD-MM-YYYY') = '"+fecha+"'");
		
		sql.append(" ORDER BY ID");
		System.out.println("ListarPactos : " + sql);
		dataSet = obtenerDataSetPaginado(sql.toString(), paginaAMostrar, registroPorPagina);

	}
	public void ListarPactosSinPaginador(String fecha) throws Exception {

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT  ID, ID_JORNADA, TIPO_OPER, CODIGO_OFERTA, CODIGO_DEMANDA, MONTO, TO_CHAR(FECHA,'DD-MM-YYYY') FECHA, MONTO_PACTO_BASE, MONTO_CONTRAVALOR_BASE, TASA_CAMBIO, TIPO_PACTO, OBSERVACION, ESTATUS, ID_BCV");
		sql.append("  FROM ADM_INFI.INFI_TB_236_PACTOS ");
		sql.append("WHERE ");
		sql.append(" TO_CHAR(FECHA,'DD-MM-YYYY') = '"+fecha+"'");
		
		sql.append(" ORDER BY ID");
		System.out.println("ListarPactos : " + sql);
		dataSet = db.get(dataSource, sql.toString());

	}
	
	public void ListarCantidadOferta(String tipo, String fecha, String estatusEnvio, String tipoMivimiento, String nacionalidad, String rifCedula) throws Exception {

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT  SUM(MTO_DIVISAS) as montoOferta FROM INFI_TB_236_OPER_DIVISAS ");
		sql.append("FROM INFI_TB_236_OPER_DIVISAS ");
		sql.append("WHERE ");
		sql.append(" NOM_OFER = 'BANCO DE VENEZUELA SACA' AND ");
		sql.append(" TO_DATE(FECH_OPER,'DD/MM/YYYY') = TO_DATE('").append(fecha).append("', 'DD/MM/YYYY') ");

		
		if (nacionalidad.length() > 0 || rifCedula.length() > 0) {
			sql.append(" AND TIPO_PER_OFER='").append(nacionalidad).append("' ");
			sql.append(" AND CED_RIF_OFER='").append(rifCedula).append("' ");
		}
		// 5 Es igual a decir todas las operaciones, se manda en el formulario filter
		if (!estatusEnvio.equalsIgnoreCase("5")) {
			sql.append("AND STATUS_ENVIO='").append(estatusEnvio).append("' ");
		}

		if (!tipoMivimiento.equalsIgnoreCase("1111")) {
			sql.append("AND PRODUCTO='").append(tipoMivimiento).append("' ");
		}

		if (!tipo.equalsIgnoreCase("T")) {
			sql.append("AND TIPO_NOTIFICACION='").append(tipo).append("' ");
		}
		sql.append(" ORDER BY ID_OPER");
		System.out.println("ListarCantidadOferta : " + sql);
		dataSet = db.get(dataSource, sql.toString());

	}
	
	public void ListarCantidadDemanda(String tipo, String fecha, String estatusEnvio, String tipoMivimiento, String nacionalidad, String rifCedula) throws Exception {

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT  SUM(MTO_DIVISAS) as montoOferta FROM INFI_TB_236_OPER_DIVISAS ");
		sql.append("FROM INFI_TB_236_OPER_DIVISAS ");
		sql.append("WHERE ");
		sql.append(" NOM_DEMA = 'BANCO DE VENEZUELA SACA' AND ");
		sql.append(" TO_DATE(FECH_OPER,'DD/MM/YYYY') = TO_DATE('").append(fecha).append("', 'DD/MM/YYYY') ");

		
		if (nacionalidad.length() > 0 || rifCedula.length() > 0) {
			sql.append(" AND TIPO_PER_OFER='").append(nacionalidad).append("' ");
			sql.append(" AND CED_RIF_OFER='").append(rifCedula).append("' ");
		}
		// 5 Es igual a decir todas las operaciones, se manda en el formulario filter
		if (!estatusEnvio.equalsIgnoreCase("5")) {
			sql.append("AND STATUS_ENVIO='").append(estatusEnvio).append("' ");
		}

		if (!tipoMivimiento.equalsIgnoreCase("1111")) {
			sql.append("AND PRODUCTO='").append(tipoMivimiento).append("' ");
		}

		if (!tipo.equalsIgnoreCase("T")) {
			sql.append("AND TIPO_NOTIFICACION='").append(tipo).append("' ");
		}
		sql.append(" ORDER BY ID_OPER");
		System.out.println("ListarCantidadDemanda : " + sql);
		dataSet = db.get(dataSource, sql.toString());

	}

	public List<ClientesMesa> ListarEnvio(String tipo, String fecha, String estatusEnvio, String tipoMivimiento, String idOrdenes) throws Exception {

		sql.append("WHERE ");
		sql.append(" TO_DATE(FECH_OPER,'DD/MM/YYYY') = TO_DATE('").append(fecha).append("', 'DD/MM/YYYY') ");

		// 5 Es igual a decir todas las operaciones, se manda en el formulario filter
		if (!estatusEnvio.equalsIgnoreCase("1")) {
			if (estatusEnvio.equalsIgnoreCase("5")) {
				sql.append(" AND");
				sql.append(" (STATUS_ENVIO='").append("0").append("' ");
				sql.append(" OR ");
				sql.append("STATUS_ENVIO='").append("4").append("' ) ");
			} else {
				sql.append("AND STATUS_ENVIO='").append(estatusEnvio).append("' ");
			}

		}
//
//		if (!tipoMivimiento.equalsIgnoreCase("1111")) {
//			// sql.append("AND OPERACION='").append(tipoMivimiento).append("' ");
//		}
//
//		if (!tipo.equalsIgnoreCase("T")) {
//			// sql.append("AND STATUS_OPER='").append(tipo).append("' ");
//		}
//		System.out.println(" idOrdenes : : " + idOrdenes.isEmpty());
		if (!idOrdenes.equalsIgnoreCase("") || idOrdenes != "") {
			sql.append(" AND ID_OPER in(" + idOrdenes + ")");
		}

		// sql.append(" ORDER BY ID_OPER");
		System.out.println("ListarEnvio : " + sql);
		dataSet = db.get(dataSource, sql.toString());
		while (dataSet.next()) {

			ClientesMesa cl = new ClientesMesa();
			cl.IdOrdenes = dataSet.getValue("ID_OPER");
			cl.IdOc = dataSet.getValue("ID_OC");
			cl.Operacion = dataSet.getValue("PRODUCTO");
			cl.TipoOperacion = dataSet.getValue("TIPO_OPER");
			cl.MontoBaseDivisas = new BigDecimal(dataSet.getValue("MTO_DIVISAS"));
			cl.MontoBaseBolivares = new BigDecimal(dataSet.getValue("MTO_BOLIVARES"));
			cl.TasaCambio = new BigDecimal(dataSet.getValue("TASA_CAMBIO"));
			cl.MontoComision = new BigDecimal(dataSet.getValue("MTO_COMI"));
			cl.TipoOperacionOferta = dataSet.getValue("TIPO_PER_OFER");
			cl.CedulaRifOferta = dataSet.getValue("CED_RIF_OFER");
			cl.NombreOferta = dataSet.getValue("NOM_OFER");
			cl.CuentaOfertaMn = dataSet.getValue("CTA_OFER_MN");
			cl.CuentaOfertaMe = dataSet.getValue("CTA_OFER_ME");
			cl.TipoOperacionDemanda = dataSet.getValue("TIPO_PER_DEMA");
			cl.NombreDemanda = dataSet.getValue("NOM_DEMA");
			cl.CuentaDemandaMn = dataSet.getValue("CTA_DEMA_MN");
			cl.CuentaDemandaMe = dataSet.getValue("CTA_DEMA_ME");
			cl.CedulaRifDemanda = dataSet.getValue("CED_RIF_DEMA");
			cl.FechaOperacion = dataSet.getValue("FECH_OPER");
			cl.MonedaBase = dataSet.getValue("COD_DIVISAS");
			cl.NacionalidadDemanda = dataSet.getValue("TIPO_PER_DEMA");
			cl.NacionalidadOferta = dataSet.getValue("TIPO_PER_OFER");
			cl.Instrumento = dataSet.getValue("TIPO_INSTRUM");
			cl.codigoInstitucion = dataSet.getValue("COD_FINSTITUCION");
			// cl.ConceptoEstadistica = dataSet.getValue("estadistica");
			// cl.CodigoOficina = dataSet.getValue("COD_OFI_ORI");
			// cl.Correo = dataSet.getValue("EMAIL_CLIEN");
			// cl.Telefono = dataSet.getValue("TEL_CLIEN");
			cl.EstatusEnvio = dataSet.getValue("Estatus");
			cl.FechaEnvio = dataSet.getValue("FECHA_ENVIO");
			cl.TipoPacto = dataSet.getValue("TIPO_PACTO");
			cl.Observacion = dataSet.getValue("OBSERVACION");
			cl.IdBcv = dataSet.getValue("ID_BCV");
			// cl.MontoTransaccion = new BigDecimal(dataSet.getValue("MTO_DIVISAS_TRANS"));

			this.lst.add(cl);
		}
		return this.lst;
	}
	
	public List<ClientesMesaBancario> ListarEnvioInterbancario(String tipo, String fecha, String estatusEnvio, String tipoMivimiento, String idOrdenes, String tipoMovimientoo) throws Exception {

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT ID, TIPO_OPER, RIF_CLIENTE, NOMBRE_CLIENTE, CODIGO_MONEDA, MONTO, TASA_CAMBIO, TO_CHAR(FECHA,'DD-MM-YYYY') FECHA, CODIGO_INSTITUCION, ID_JORNADA, CUENTA_ME, CUENTA_MN, TIPO_INSTRUMENTO, OBSERVACION, ESTATUS, ID_BCV, TIPO_CLIENTE");
		sql.append(" FROM INFI_TB_236_INTERBANCARIAS ");
		sql.append("WHERE ");
		sql.append(" TO_CHAR(FECHA,'DD-MM-YYYY') = '"+fecha+"'");
		sql.append(" AND TIPO_OPER='").append(tipoMovimientoo).append("' ");
		if (!idOrdenes.equalsIgnoreCase("") || idOrdenes != "") {
			sql.append(" AND ID in(" + idOrdenes + ")");
		}
		sql.append(" ORDER BY ID");
		System.out.println("ListarInter : " + sql);

		System.out.println("ListarEnvioIntercambio : " + sql);
		dataSet = db.get(dataSource, sql.toString());
		while (dataSet.next()) {

			ClientesMesaBancario cl = new ClientesMesaBancario();
			cl.Id = dataSet.getValue("ID");
			cl.TipoOperacion = dataSet.getValue("TIPO_OPER");
			cl.TipoCliente = dataSet.getValue("TIPO_CLIENTE");
			cl.RifCliente = dataSet.getValue("RIF_CLIENTE");
			cl.NombreCliente = dataSet.getValue("NOMBRE_CLIENTE");
			cl.CodigoMoneda = dataSet.getValue("CODIGO_MONEDA");
			cl.Monto = new BigDecimal(dataSet.getValue("MONTO"));
			cl.TasaCambio = new BigDecimal(dataSet.getValue("TASA_CAMBIO"));
			cl.Fecha = dataSet.getValue("FECHA");
			cl.CodigoInstitucion = dataSet.getValue("CODIGO_INSTITUCION");
			cl.IdJornada = dataSet.getValue("ID_JORNADA");
			cl.CuentaMe = dataSet.getValue("CUENTA_ME");
			cl.CuentaMn = dataSet.getValue("CUENTA_MN");
			cl.TipoInstrumento = dataSet.getValue("TIPO_INSTRUMENTO");
			cl.Observacion = dataSet.getValue("OBSERVACION");
			cl.Estatus = dataSet.getValue("ESTATUS");
			cl.IdBcv = dataSet.getValue("ID_BCV");
			this.lstI.add(cl);
		}
		return this.lstI;
	}

	public void ListarEnvioAnular(String tipo, String fecha, String estatusEnvio, String tipoMivimiento, String idOrdenes) throws Exception {

		sql.append("WHERE ");
		sql.append(" TO_DATE(FECH_OPER,'DD/MM/YYYY') = TO_DATE('").append(fecha).append("', 'DD/MM/YYYY') ");

//		System.out.println(" idOrdenes : : " + idOrdenes.isEmpty());
		if (!idOrdenes.equalsIgnoreCase("") || idOrdenes !="") {
			sql.append(" AND ID_OPER in(" + idOrdenes + ")");
		}

		// sql.append(" ORDER BY ID_OPER");
		System.out.println("ListarEnvioAnular : " + sql);
		dataSet = db.get(dataSource, sql.toString());
	}

	public void modificar(String idOperacion, String idBcv, String mensaje, String estatusEnvio,BigDecimal montoContravalor,BigDecimal montoPactoBase,BigDecimal tasaBase) throws Exception {

		StringBuffer sql = new StringBuffer();

		sql.append(" UPDATE INFI_TB_236_OPER_DIVISAS SET OBSERVACION ='").append(mensaje).append("'");
		if (idBcv != null) {
			sql.append(",ID_BCV ='").append(idBcv).append("'");
		}
		sql.append(",STATUS_ENVIO ='").append(estatusEnvio).append("'");
		sql.append(",MTO_CONTRAVALORBASE =").append(montoContravalor).append("");
		sql.append(",TASA_PACTOBASE =").append(tasaBase).append("");
		sql.append(",MTO_PACTOBASE =").append(montoPactoBase).append("");
		//sql.append(",FECHA_ENVIO=SYS");
		sql.append(" WHERE ID_OPER =").append(idOperacion);
		System.out.println("modificar : " + sql);
		db.exec(dataSource, sql.toString());

	}
	
	public void modificarInterbancario(String idOperacion, String idBcv, String mensaje, String estatusEnvio,BigDecimal montoContravalor,BigDecimal montoPactoBase,BigDecimal tasaBase) throws Exception {

		StringBuffer sql = new StringBuffer();

		sql.append(" UPDATE INFI_TB_236_INTERBANCARIAS SET OBSERVACION ='").append(mensaje).append("'");
		if (idBcv != null) {
			sql.append(",ID_BCV ='").append(idBcv).append("'");
		}
		sql.append(",ESTATUS ='").append(estatusEnvio).append("'");
		sql.append(" WHERE ID =").append(idOperacion);
		System.out.println("modificarInterbancario : " + sql);
		db.exec(dataSource, sql.toString());

	}
	
	public void InsertarPacto(String idOperacion, String jornada, 
			BigDecimal monto, BigDecimal montoBase,BigDecimal contravalor,
			BigDecimal tasa,String tipoPacto, String codDemand, String codOferta, String idBcv, String observacion, String estatus) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO INFI_TB_236_PACTOS (ID, ID_JORNADA, TIPO_OPER, CODIGO_OFERTA, CODIGO_DEMANDA, MONTO, FECHA, MONTO_PACTO_BASE, MONTO_CONTRAVALOR_BASE, TASA_CAMBIO, TIPO_PACTO, OBSERVACION, ESTATUS, ID_BCV) VALUES (");
		sql.append("INFI_SQ_236.NEXTVAL,'"+jornada+"','P','"+codOferta+"','"+codDemand+"',"+monto+",SYSTIMESTAMP,"+montoBase+","+contravalor+","+tasa+",'"+tipoPacto+"','"+observacion+"','"+estatus+"','"+idBcv+"')");
		
		System.out.println("InsertarPacto : " + sql);
		db.exec(dataSource, sql.toString());

	}
	
	public void modificarEstatus(String idOperacion, String idBcv, String mensaje, String estatusEnvio) throws Exception {

		StringBuffer sql = new StringBuffer();

		sql.append(" UPDATE INFI_TB_236_OPER_DIVISAS SET OBSERVACION ='").append(mensaje).append("'");
		if (idBcv != null) {
			sql.append(",ID_BCV ='").append(idBcv).append("'");
		}
		sql.append(",STATUS_ENVIO ='").append(estatusEnvio).append("'");
		sql.append(" WHERE ID_OPER =").append(idOperacion);
		System.out.println("modificar : " + sql);
		db.exec(dataSource, sql.toString());

	}

	public void ActualziarEstatus(String id, String observacion) {

		String update = "UPDATE INFI_TB_236_OPER_DIVISAS SET OBSERVACION = '" + observacion + "' , STATUS_ENVIO='" + observacion + "' WHERE ID_OPER ='" + id + "'";
		try {
			db.exec(dataSource, update.toString());
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("ActualziarEstatus : " + e);
		}

	}
	
	public void listarClienteMesaCambio(String idOperacion) throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("select ID_OPER, ID_OC, PRODUCTO, TIPO_NOTIFICACION, TIPO_OPER, MTO_DIVISAS, MTO_BOLIVARES, TASA_CAMBIO, COD_FINSTITUCION, MTO_COMI, TIPO_PER_OFER, CED_RIF_OFER, NOM_OFER, CTA_OFER_MN, CTA_OFER_ME, TIPO_INSTRUM, TIPO_PER_DEMA, CED_RIF_DEMA, NOM_DEMA, CTA_DEMA_MN, CTA_DEMA_ME, FECH_OPER, COD_DIVISAS, MTO_PACTOBASE, MTO_CONTRAVALORBASE, TASA_PACTOBASE, TIPO_PACTO, STATUS_ENVIO from INFI_TB_236_OPER_DIVISAS");
		sql.append(" where ID_OPER='").append(idOperacion).append("'");
		System.out.println("sql listarClienteMesaCambio : "+sql);
		
		dataSet = db.get(dataSource, sql.toString());
		System.out.println("dataSet-->"+dataSet.count());
	}
	
	public void BusquedaClienteParaPacto(String idOperacion) throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT ID, ID_JORNADA, ID_BCV,  MONTO, TASA_CAMBIO FROM INFI_TB_236_INTERBANCARIAS WHERE ID = '"+idOperacion+"'");
		System.out.println("BusquedaClienteParaPacto : "+sql);
		
		dataSet = db.get(dataSource, sql.toString());
	}
	
	
	public String modificarClienteMesa(String montoDivisas, String montoBs, String tasaCambio, String tipoPersonaOfer, String cedulaOfer, 
			String nombreOfer, String cuentaOferMn, String cuentaOferMe, String tipoPersonaDema, String cedulaDema, String nombreDema, String cuentaDemaMn, 
			String cuentaDemaMe, String codigoDivisas,String tipoPacto, String idOperacion){
		StringBuffer sql =new StringBuffer();
		sql.append("UPDATE INFI_TB_236_OPER_DIVISAS SET MTO_DIVISAS=").append(montoDivisas).append("");
		sql.append(" ,MTO_BOLIVARES=").append(montoBs).append("");
		sql.append(" ,TASA_CAMBIO=").append(tasaCambio).append("");
		sql.append(" ,TIPO_PER_OFER='").append(tipoPersonaOfer).append("'");
		sql.append(" ,CED_RIF_OFER='").append(cedulaOfer).append("'");
		sql.append(" ,NOM_OFER='").append(nombreOfer).append("'");
		sql.append(" ,CTA_OFER_MN='").append(cuentaOferMn).append("'");
		sql.append(" ,CTA_OFER_ME='").append(cuentaOferMe).append("'");
		sql.append(" ,TIPO_PER_DEMA='").append(tipoPersonaDema).append("'");
		sql.append(" ,CED_RIF_DEMA='").append(cedulaDema).append("'");
		sql.append(" ,NOM_DEMA='").append(nombreDema).append("'");
		sql.append(" ,CTA_DEMA_MN='").append(cuentaDemaMn).append("'");
		sql.append(" ,CTA_DEMA_ME='").append(cuentaDemaMe).append("'");
		sql.append(" ,COD_DIVISAS='").append(codigoDivisas).append("'");
		sql.append(" ,MTO_PACTOBASE=").append("''").append("");
		sql.append(" ,MTO_CONTRAVALORBASE=").append("''").append("");
		sql.append(" ,TASA_PACTOBASE=").append("''").append("");
		sql.append(" ,TIPO_PACTO='").append(tipoPacto).append("'");	
		sql.append(" WHERE ID_OC='").append(idOperacion).append("'");
//		sql.append(" AND ID_OPER=").append(idOperacion).append("");
		System.out.println("modificarClienteMesa : "+sql);
		return (sql.toString());
		}
	
	public void InsertarTransacciones(String tipoOperacion,  String montoDivisas, String montoBs, String tasaCambio , 
			String tipoClienteOfer, String cedulaRifOfer, String nombreOfer, String cuentaOferMn, String cuentaOferMe, String tipoClienteDeman, 
			String cedulaRifDeman, String nombreDeman, String cuentaDemanMn, String cuentaDemanMe, String codigoDivisas, String montoPactoBase,String montoContraValor,
			String tipoPacto) {


//		String update = "UPDATE INFI_TB_236_OPER_DIVISAS SET OBSERVACION = '" + observacion + "' , STATUS_ENVIO='" + observacion + "' WHERE ID_OPER ='" + id + "'";
		String insert = "Insert into INFI_TB_236_OPER_DIVISAS"+
        "(ID_OPER,ID_OC,PRODUCTO,TIPO_NOTIFICACION,TIPO_OPER,MTO_DIVISAS,MTO_BOLIVARES,"+
         "TASA_CAMBIO,MTO_COMI,TIPO_PER_OFER,CED_RIF_OFER,NOM_OFER,"+
         "CTA_OFER_MN,CTA_OFER_ME,TIPO_PER_DEMA,CED_RIF_DEMA,NOM_DEMA,"+
         "CTA_DEMA_MN,CTA_DEMA_ME,FECH_OPER,COD_DIVISAS,MTO_PACTOBASE,MTO_CONTRAVALORBASE,"+
         "TASA_PACTOBASE,TIPO_PACTO,STATUS_ENVIO,FECHA_ENVIO,OBSERVACION,ID_BCV)"+
		"Values (INFI_SQ_236.nextval, INFI_SQ_236.nextval, 'MECA', 'PD','"+tipoOperacion+"',"+montoDivisas+","+montoBs+", "+tasaCambio+",0, '"+
		tipoClienteOfer+"','"+cedulaRifOfer+"','"+ nombreOfer+"','"+cuentaOferMn.trim()+"','"+cuentaOferMe.trim()+"','"+tipoClienteDeman+"','"+cedulaRifDeman+"','"+
		nombreDeman+"','"+cuentaDemanMn.trim()+"','"+cuentaDemanMe.trim()+"',TO_CHAR( sysdate, 'DD-MM-YYYY' ), '"+codigoDivisas+"',"+montoPactoBase+","+montoContraValor+","+tasaCambio+",'"+tipoPacto+"',"+
		"'0','','','')";
		
		System.out.println("Guardar : "+insert);
		try {
			
			db.exec(dataSource, insert.toString());
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("MesaCambioDAO : InsertarTransacciones() " + e);
		}

	}
	
	public String InsertarTransaccionesList(String tipoOperacion,  String montoDivisas, String montoBs, String tasaCambio , 
			String tipoClienteOfer, String cedulaRifOfer, String nombreOfer, String cuentaOferMn, String cuentaOferMe, String tipoClienteDeman, 
			String cedulaRifDeman, String nombreDeman, String cuentaDemanMn, String cuentaDemanMe, String codigoDivisas, String montoPactoBase,String montoContraValor,
			String tipoPacto,String instrumento) {


//		String update = "UPDATE INFI_TB_236_OPER_DIVISAS SET OBSERVACION = '" + observacion + "' , STATUS_ENVIO='" + observacion + "' WHERE ID_OPER ='" + id + "'";
		String insert = "Insert into INFI_TB_236_OPER_DIVISAS"+
        "(ID_OPER,ID_OC,PRODUCTO,TIPO_NOTIFICACION,TIPO_OPER,MTO_DIVISAS,MTO_BOLIVARES,"+
         "TASA_CAMBIO,MTO_COMI,TIPO_PER_OFER,CED_RIF_OFER,NOM_OFER,"+
         "CTA_OFER_MN,CTA_OFER_ME,TIPO_PER_DEMA,CED_RIF_DEMA,NOM_DEMA,"+
         "CTA_DEMA_MN,CTA_DEMA_ME,FECH_OPER,COD_DIVISAS,MTO_PACTOBASE,MTO_CONTRAVALORBASE,"+
         "TASA_PACTOBASE,TIPO_PACTO,STATUS_ENVIO,FECHA_ENVIO,OBSERVACION,ID_BCV,TIPO_INSTRUM)"+
		"Values (INFI_SQ_236.nextval, INFI_SQ_236.nextval, 'MECA', 'PD','"+tipoOperacion+"',"+montoDivisas+","+montoBs+", "+tasaCambio+",0, '"+
		tipoClienteOfer+"','"+cedulaRifOfer+"','"+ nombreOfer+"','"+cuentaOferMn.trim()+"','"+cuentaOferMe.trim()+"','"+tipoClienteDeman+"','"+cedulaRifDeman+"','"+
		nombreDeman+"','"+cuentaDemanMn.trim()+"','"+cuentaDemanMe.trim()+"',TO_CHAR( sysdate, 'DD-MM-YYYY' ), '"+codigoDivisas+"',"+montoPactoBase+","+montoContraValor+","+tasaCambio+",'"+tipoPacto+"',"+
		"'0','','','','"+instrumento.trim()+"')";
		
		System.out.println("Devolver arreglo : "+insert);
//		try {
//
//			db.exec(dataSource, insert.toString());
//		} catch (Exception e) {
//			// TODO: handle exception
//			System.out.println("MesaCambioDAO : InsertarTransacciones() " + e);
//		}
		return insert.toString();
	}

	public boolean verificarTransaccion(String id) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM INFI_TB_236_OPER_DIVISAS WHERE ID_OPER='").append(id).append("' AND STATUS_ENVIO ='0'");
		dataSet = db.get(dataSource, sql.toString());
		if (this.dataSet.count() > 0)
			return true;
		else
			return false;
	}
	
	public String guardarInterbancario(String tipoOperacion, String tipoCliente, String rif, String nombreCliente, String codigoMoneda, String monto, String tasa, String codigoBanco, String cuentame, String cuentamn, String tipoInstrumento, String estatus, String idJornada) {

		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO INFI_TB_236_INTERBANCARIAS " + "(ID, TIPO_OPER, TIPO_CLIENTE, RIF_CLIENTE, NOMBRE_CLIENTE, CODIGO_MONEDA, MONTO, TASA_CAMBIO, FECHA, CODIGO_INSTITUCION, CUENTA_ME, CUENTA_MN, " + "TIPO_INSTRUMENTO, ESTATUS, ID_JORNADA) " + "values(INFI_SQ_236.NEXTVAL,'").append(tipoOperacion).append("','").append(tipoCliente).append("','").append(rif).append("','").append(nombreCliente).append("','").append(codigoMoneda)
				.append("',").append(monto).append(",").append(tasa).append(",SYSTIMESTAMP,'").append(codigoBanco).append("','").append(cuentame).append("','").append(cuentamn).append("','").append(tipoInstrumento).append("','").append(estatus).append("','").append(idJornada).append("')");
		System.out.println("guardarInterbancarioMesa : " + sql.toString());
		return (sql.toString());
	}

	@Override
	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}

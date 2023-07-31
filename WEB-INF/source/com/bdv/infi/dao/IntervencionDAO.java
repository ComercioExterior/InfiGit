package com.bdv.infi.dao;

import java.util.List;

import javax.sql.DataSource;
import com.bdv.infi.data.ClienteIntervencion;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

import megasoft.db;

/**
 * clase para agregar,consultar,modificar,eliminar las operaciones de intervencion cambiaria
 * 
 * @author nm11383
 * 
 */
public class IntervencionDAO extends com.bdv.infi.dao.GenericoDAO {

	public IntervencionDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
	}

	public IntervencionDAO(DataSource ds) {
		super(ds);
	}

	/**
	 * Busca todas las operaciones registradas en la tabla INFI_TB_235_INTERVENCION
	 * 
	 * @throws Exception
	 */
	public void listar() {

		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM INFI_TB_235_INTERVENCION");

		try {
			dataSet = db.get(dataSource, sb.toString());

		} catch (Exception e) {
			System.out.println("IntervencionDAO : listar()" + e);

		}

	}

	// public void listarOrdenesIntervencion(boolean paginado, int paginaAMostrar, int registroPorPagina, String fecha, String estatusEnvio) {
	//
	// StringBuilder sql = new StringBuilder();
	//
	// sql.append("SELECT ID_OPER, ID_OC, OPERACION, STATUS_OPER, MTO_DIVISAS,MTO_BOLIVARES, TASA_CAMBIO, MTO_COMI, NACIONALIDAD, NRO_CED_RIF,NOM_CLIEN, CTA_CLIEN, FECH_OPER, CON_ESTADIS, COD_OFI_ORI, COD_DIVISAS, EMAIL_CLIEN, TEL_CLIEN, STATUS_ENVIO,ID_JORNADA,CTA_CLIEN_DIVISAS ");
	//
	// sql.append(" FROM INFI_TB_235_INTERVENCION ");
	// sql.append("WHERE ");
	// sql.append(" TO_DATE(FECH_OPER,'DD/MM/YYYY') = TO_DATE('").append(fecha).append("', 'DD/MM/YYYY') ");
	//
	// if (estatusEnvio.equals(ConstantesGenerales.ENVIO_MENUDEO) || estatusEnvio.equals(ConstantesGenerales.ENVIO_MENUDEO_FALTANTES) || estatusEnvio.equals(ConstantesGenerales.ENVIO_MENUDEO_MANUAL) || estatusEnvio.equals(ConstantesGenerales.ENVIO_MENUDEO_ANULADA) || estatusEnvio.equals(ConstantesGenerales.ENVIO_MENUDEO_RECHAZADA)) {
	// sql.append(" AND STATUS_ENVIO='").append(estatusEnvio).append("' ");
	//
	// }
	// sql.append(" ORDER BY ID_OPER");
	// System.out.println("listarOrdenesIntervencion : " + sql);
	//
	// try {
	// dataSet = obtenerDataSetPaginado(sql.toString(), paginaAMostrar, registroPorPagina);
	//
	// } catch (Exception e) {
	// System.out.println("IntervencionDAO : listarOrdenesIntervencion() " + e);
	//
	// }
	// }

	public void listarOrdenesIntervencion(boolean paginado, int paginaAMostrar, int registroPorPagina, String fecha, String estatusEnvio, String nacionalidad, String rif, String moneda) {

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT ID_OPER, ID_OC, OPERACION, STATUS_OPER, MTO_DIVISAS,MTO_BOLIVARES, TASA_CAMBIO, MTO_COMI, NACIONALIDAD, NRO_CED_RIF,NOM_CLIEN, CTA_CLIEN, FECH_OPER, CON_ESTADIS, COD_OFI_ORI, COD_DIVISAS, EMAIL_CLIEN, TEL_CLIEN, decode(STATUS_ENVIO,'0','NO ENVIADO','1','LOTE CERRADO','2','ANULADA') as Estatus,ID_JORNADA,CTA_CLIEN_DIVISAS ");

		sql.append(" FROM INFI_TB_235_INTERVENCION ");
		sql.append("WHERE ");
		sql.append(" FECH_OPER = TO_DATE('").append(fecha).append("', 'DD/MM/YYYY') ");

		if (nacionalidad.length() > 0 || rif.length() > 0) {
			sql.append(" AND NACIONALIDAD='").append(nacionalidad).append("' ");
			sql.append(" AND NRO_CED_RIF='").append(rif).append("' ");
		}
		if (estatusEnvio.equals(ConstantesGenerales.ENVIO_MENUDEO) || estatusEnvio.equals(ConstantesGenerales.ENVIO_MENUDEO_FALTANTES) || estatusEnvio.equals(ConstantesGenerales.ENVIO_MENUDEO_MANUAL) || estatusEnvio.equals(ConstantesGenerales.ENVIO_MENUDEO_ANULADA) || estatusEnvio.equals(ConstantesGenerales.ENVIO_MENUDEO_RECHAZADA)) {
			sql.append(" AND STATUS_ENVIO='").append(estatusEnvio).append("' ");

		}
		
		if (!moneda.equalsIgnoreCase("TODAS")) {
			sql.append(" AND COD_DIVISAS='").append(moneda).append("' ");

		}
		sql.append(" ORDER BY ID_OPER");
		System.out.println("listarOrdenesIntervencion : " + sql);

		try {
			dataSet = obtenerDataSetPaginado(sql.toString(), paginaAMostrar, registroPorPagina);

		} catch (Exception e) {
			System.out.println("IntervencionDAO : listarOrdenesIntervencion() " + e);

		}
	}
	
	public void listarTotalesCantidadOrdenesIntervencion(String fecha, String estatusEnvio, String nacionalidad, String rif, String moneda) {

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT  COUNT(*) as cant,SUM(MTO_DIVISAS) as total ");

		sql.append(" FROM INFI_TB_235_INTERVENCION ");
		sql.append("WHERE ");
		sql.append(" FECH_OPER = TO_DATE('").append(fecha).append("', 'DD/MM/YYYY') ");

		if (nacionalidad.length() > 0 || rif.length() > 0) {
			sql.append(" AND NACIONALIDAD='").append(nacionalidad).append("' ");
			sql.append(" AND NRO_CED_RIF='").append(rif).append("' ");
		}
		if (estatusEnvio.equals(ConstantesGenerales.ENVIO_MENUDEO) || estatusEnvio.equals(ConstantesGenerales.ENVIO_MENUDEO_FALTANTES) || estatusEnvio.equals(ConstantesGenerales.ENVIO_MENUDEO_MANUAL) || estatusEnvio.equals(ConstantesGenerales.ENVIO_MENUDEO_ANULADA) || estatusEnvio.equals(ConstantesGenerales.ENVIO_MENUDEO_RECHAZADA)) {
			sql.append(" AND STATUS_ENVIO='").append(estatusEnvio).append("' ");

		}
		
		if (!moneda.equalsIgnoreCase("TODAS")) {
			sql.append(" AND COD_DIVISAS='").append(moneda).append("' ");

		}
		sql.append(" ORDER BY ID_OPER");
		System.out.println("listarTotalesCantidadOrdenesIntervencion : " + sql);

		try {
			
			dataSet =  db.get(dataSource, sql.toString());

		} catch (Exception e) {
			System.out.println("IntervencionDAO : listarTotalesCantidadOrdenesIntervencion() " + e);

		}
	}

	public String modificarCliente(ClienteIntervencion intervencionCliente) {
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE INFI_TB_235_INTERVENCION SET NRO_CED_RIF='").append(intervencionCliente.getRifCliente()).append("'");
		sql.append(" ,NOM_CLIEN='").append(intervencionCliente.getNombreCliente()).append("'");
		sql.append(" ,COD_DIVISAS='").append(intervencionCliente.getMoneda()).append("'");
		sql.append(" ,FECH_OPER=TO_DATE('").append(intervencionCliente.getFechaValor()).append("','DD/MM/YY')");
		sql.append(" ,CTA_CLIEN_DIVISAS='").append(intervencionCliente.getCuentaDivisas()).append("'");
		sql.append(" ,CTA_CLIEN='").append(intervencionCliente.getCuentaBolivares()).append("'");
		sql.append(" ,MTO_DIVISAS='").append(intervencionCliente.getMontoDivisa()).append("'");
		sql.append(" WHERE ID_OPER='").append(intervencionCliente.getIdOperacion()).append("'");
		// System.out.println("modificarCliente : " + sql);
		return (sql.toString());
	}

	public String guardarInventario(String oficinaId, String inventarioId, String fecha, String moneda, String montoAsignado, String montoPorcentaje, String montoOriginal, String montoConsumido, String montoDisponible, String estatus) {

		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO INFI_TB_035_INVENTARIO_OFICINA (OFICINA_ID,INVENTARIO_ID,INVENTARIO_FECHA,INVENTARIO_MONEDA," + "INVENTARIO_ASIGNADO,INVENTARIO_PORCENTAJE,INVENTARIO_MONTO,INVENTARIO_CONSUMIDO,INVENTARIO_DISPONIBLE,INVENTARIO_ESTATUS) " + "values('").append(oficinaId).append("','").append(inventarioId).append("','").append(fecha).append("','").append(moneda).append("','")
				.append(montoAsignado).append("','").append(montoPorcentaje).append("','").append(montoOriginal).append("','").append(montoConsumido).append("','").append(montoDisponible).append("','").append(estatus).append("')");

		return (sql.toString());
	}
	
	public String guardarInterbancario(String jornada, String codigoBanco, String codigoMoneda, String operacion, 
			String monto, String tasa, String estatus, String usuarioRegistro) {

		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO INFI_TB_235_INTERVENCION_BAN " +
				"(ID, JORNADA, CODIGO_BANCO, CODIGO_MONEDA, OPERACION, MONTO, TASA, FECHA, ESTATUS, USUARIO_REGISTRO) " + 
				"values(INFI_SQ_236.NEXTVAL,'").append(jornada).append("','").append(codigoBanco).append("',").append(codigoMoneda).append(",'").append(operacion).append("',")
				.append(monto).append(",").append(tasa).append(",SYSTIMESTAMP,'").append(estatus).append("','").append(usuarioRegistro).append("')");

		return (sql.toString());
	}
	
	public void listarInterbancario(int paginaAMostrar, int registroPorPagina, String fecha, String estatusEnvio) {

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT ID, JORNADA, CODIGO_BANCO, CODIGO_MONEDA, OPERACION, MONTO, TASA, TO_CHAR (FECHA, 'DD-MM-YYYY') FECHA, decode(ESTATUS,'0','NO_ENVIADO','1','ENVIADO','2','MANUAL','3','ANULADA','4','RECHAZADA') as ESTATUS, USUARIO_REGISTRO, USUARIO_APROBACION, USUARIO_ENVIO, OBSERVACION, ID_BCV ");
		sql.append(" FROM INFI_TB_235_INTERVENCION_BAN ");
		sql.append("WHERE ");
		sql.append(" TO_CHAR(FECHA, 'DD-MM-YYYY') = '").append(fecha).append("' ");

		if (estatusEnvio.equals(ConstantesGenerales.ENVIO_MENUDEO) || estatusEnvio.equals(ConstantesGenerales.ENVIO_MENUDEO_FALTANTES) || estatusEnvio.equals(ConstantesGenerales.ENVIO_MENUDEO_MANUAL) || estatusEnvio.equals(ConstantesGenerales.ENVIO_MENUDEO_ANULADA) || estatusEnvio.equals(ConstantesGenerales.ENVIO_MENUDEO_RECHAZADA)) {
			sql.append(" AND ESTATUS='").append(estatusEnvio).append("' ");

		}
		sql.append(" ORDER BY ID");
		System.out.println("listarInterbancario : " + sql);

		try {
			dataSet = obtenerDataSetPaginado(sql.toString(), paginaAMostrar, registroPorPagina);

		} catch (Exception e) {
			System.out.println("IntervencionDAO : listarInterbancario() " + e);

		}
	}
	
	public void listarInterbancarioSinPaginador(String fecha, String estatusEnvio,String ordenesSeleccionadas, boolean todos) {

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT ID, JORNADA, CODIGO_BANCO, CODIGO_MONEDA, OPERACION, MONTO, TASA, TO_CHAR (FECHA, 'DD/MM/YYYY') FECHA, ESTATUS, USUARIO_REGISTRO, USUARIO_APROBACION, USUARIO_ENVIO ");
		sql.append(" FROM INFI_TB_235_INTERVENCION_BAN ");
		sql.append("WHERE ");
		sql.append(" TO_CHAR(FECHA, 'DD-MM-YYYY') = '").append(fecha).append("' ");

		if (estatusEnvio.equals(ConstantesGenerales.ENVIO_MENUDEO) || estatusEnvio.equals(ConstantesGenerales.ENVIO_MENUDEO_FALTANTES) || estatusEnvio.equals(ConstantesGenerales.ENVIO_MENUDEO_MANUAL) || estatusEnvio.equals(ConstantesGenerales.ENVIO_MENUDEO_ANULADA) || estatusEnvio.equals(ConstantesGenerales.ENVIO_MENUDEO_RECHAZADA)) {
			sql.append(" AND ESTATUS='").append(estatusEnvio).append("' ");

		}
		if (!todos) {
			sql.append(" AND ID in(" + ordenesSeleccionadas + ")");
		}
		sql.append(" ORDER BY ID");
		System.out.println("listarInterbancario : " + sql);

		try {
			dataSet = db.get(dataSource, sql.toString());
		} catch (Exception e) {
			System.out.println("IntervencionDAO : listarInterbancario() " + e);

		}
	}
	
	public void listarMonedas() {

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT MONEDA_ID,MONEDA_SIGLAS FROM ADM_INFI.INFI_TB_006_MONEDAS");
		sql.append(" ORDER BY MONEDA_ID");
		System.out.println("listarMonedas : " + sql);

		try {
			dataSet = db.get(dataSource, sql.toString());
		} catch (Exception e) {
			System.out.println("IntervencionDAO : listarMonedas() " + e);

		}
	}
	
	public void modificarInterbancario(String id, String usuario, String estatus, String observacion, String idBcv) {

		StringBuffer sql = new StringBuffer();

		sql.append(" UPDATE INFI_TB_235_INTERVENCION_BAN SET ");
		sql.append("ESTATUS ='").append(estatus).append("' ");
		sql.append(",OBSERVACION ='").append(observacion).append("' ");
		sql.append(",ID_BCV ='").append(idBcv).append("' ");
		sql.append(",USUARIO_ENVIO ='").append(usuario).append("' ");
		sql.append(" WHERE ID =").append(id);

		try {
			db.exec(dataSource, sql.toString());

		} catch (Exception e) {
			System.out.println("IntervencionDAO : modificarOrden() " + e);

		}

	}

	public void listarOrdenesIntervencion(String fecha, String estatusEnvio, boolean todos, String ordenesSeleccionadas, String moneda) {

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT ID_OPER, ID_OC, OPERACION, STATUS_OPER, MTO_DIVISAS, MTO_BOLIVARES, TASA_CAMBIO, MTO_COMI, NACIONALIDAD, decode(nacionalidad,'J',LPAD(NRO_CED_RIF,9,'0'),'G',LPAD(NRO_CED_RIF,9,'0'),LPAD(NRO_CED_RIF,8,'0')) as NRO_CED_RIF, NOM_CLIEN, CTA_CLIEN, to_char(FECH_OPER,'DD/MM/YYYY') FECH_OPER, CON_ESTADIS, COD_OFI_ORI, COD_DIVISAS as DIVISA, EMAIL_CLIEN, TEL_CLIEN, STATUS_ENVIO, ID_JORNADA, CTA_CLIEN_DIVISAS ");

		sql.append("FROM INFI_TB_235_INTERVENCION ");
		sql.append("WHERE ");
		sql.append("FECH_OPER = TO_DATE('").append(fecha).append("', 'DD/MM/YYYY') ");

		// if (estatusEnvio.equals(ConstantesGenerales.ENVIO_MENUDEO) || estatusEnvio.equals(ConstantesGenerales.ENVIO_MENUDEO_FALTANTES)) {
		
		sql.append("AND STATUS_ENVIO='0' ");
		
		sql.append("AND COD_DIVISAS='"+moneda+"' ");

		if (!todos) {
			sql.append(" AND ID_OPER in(" + ordenesSeleccionadas + ")");
		}

		System.out.println("listarOrdenesIntervencion : " + sql);

		try {
			dataSet = db.get(dataSource, sql.toString());

		} catch (Exception e) {
			System.out.println("IntervencionDAO : listarOrdenesIntervencion() " + e);

		}
	}

	public void listarOrdenesReporte(String fecha, String estatusEnvio, String moneda) {

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT ID_OPER,ID_OC,OPERACION,STATUS_OPER,MTO_DIVISAS,MTO_BOLIVARES,TASA_CAMBIO,MTO_COMI,NACIONALIDAD,NRO_CED_RIF,NOM_CLIEN,CTA_CLIEN,FECH_OPER,CON_ESTADIS,COD_OFI_ORI,COD_DIVISAS,EMAIL_CLIEN,TEL_CLIEN,decode(STATUS_ENVIO,'0','NO ENVIADO','1','ENVIADO','4','RECHAZADA') as STATUS_ENVIO,OBSERVACION,ID_BCV,MTO_DIVISAS_TRANS,ID_JORNADA,CTA_CLIEN_DIVISAS FROM INFI_TB_235_INTERVENCION ");
		sql.append("WHERE ");
		sql.append(" FECH_OPER = TO_DATE('").append(fecha).append("', 'DD/MM/YYYY') ");

		if (estatusEnvio.equals(ConstantesGenerales.ENVIO_MENUDEO) || estatusEnvio.equals(ConstantesGenerales.ENVIO_MENUDEO_FALTANTES) || estatusEnvio.equals(ConstantesGenerales.ENVIO_MENUDEO_MANUAL) || estatusEnvio.equals(ConstantesGenerales.ENVIO_MENUDEO_ANULADA) || estatusEnvio.equals(ConstantesGenerales.ENVIO_MENUDEO_RECHAZADA)) {
			sql.append(" AND STATUS_ENVIO='").append(estatusEnvio).append("' ");

		}
		if (!moneda.equalsIgnoreCase("TODAS")) {
			sql.append(" AND COD_DIVISAS='").append(moneda).append("' ");

		}
		
		sql.append(" ORDER BY ID_OPER");
		System.out.println("listarOrdenesReporte : " + sql);

		try {
			dataSet = db.get(dataSource, sql.toString());

		} catch (Exception e) {
			System.out.println("IntervencionDAO : listarOrdenesReporte() " + e);

		}
	}

	public void modificarOrden(String ordeneID, String id, String estatus) {

		StringBuffer sql = new StringBuffer();

		sql.append(" UPDATE INFI_TB_235_INTERVENCION SET ");
		sql.append("ID_BCV ='").append(id).append("'");
		sql.append(",STATUS_ENVIO ='").append(estatus).append("'");
		sql.append(" WHERE ID_OPER =").append(ordeneID);

		try {
			db.exec(dataSource, sql.toString());

		} catch (Exception e) {
			System.out.println("IntervencionDAO : modificarOrden() " + e);

		}

	}

	public void modificarOrdenLote(List<String> ordeneID, String COD_BCV) throws Exception {
		System.out.println("id contador : " + ordeneID);
		for (String ordeness : ordeneID) {

			StringBuffer sql = new StringBuffer();
			sql.append(" UPDATE INFI_TB_235_INTERVENCION SET  ");
			sql.append(" ID_BCV='").append(COD_BCV).append("'");
			sql.append(" WHERE ID_OPER ='").append(ordeness).append("'");

			// System.out.println("actualizarOrdenBCVMenudeo : " + sql.toString());

			db.exec(dataSource, sql.toString());

		}
	}

	public void modificarIntervencionOrden(String ordeneID, String fecha, String estatus, String COD_BCV) {

		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE INFI_TB_235_INTERVENCION SET STATUS_ENVIO = '").append(estatus).append("',");
		sql.append(" ID_BCV='").append(COD_BCV).append("'");
		sql.append(" WHERE ID_OPER ='").append(ordeneID).append("'");

		// System.out.println("actualizarOrdenBCVMenudeo : " + sql);
		try {
			db.exec(dataSource, sql.toString());

		} catch (Exception e) {
			System.out.println("IntervencionDAO : modificarIntervencionOrden() " + e);

		}
	}

	public void listarPorParametros() {

		StringBuffer sb = new StringBuffer();
		sb.append("select * from infi_tb_034_oficina");

		try {
			dataSet = db.get(dataSource, sb.toString());

		} catch (Exception e) {
			System.out.println("IntervencionDAO : listarPorParametros()" + e);

		}
	}

	/**
	 * Metodo para insertar las operaciones de intervencion cambiaria.
	 * 
	 * @param ObjetoOArreglo
	 * @return
	 */
	public String insertar(String ObjetoOArreglo) {// Conversar.
		StringBuffer sql = new StringBuffer();
		sql.append("Insert into ADM_INFI.INFI_TB_235_INTERVENCION (COD_CLIENT, NOMBRE_CLIENT, FECHA_VALOR, " + "TIPO_OPERACION, MTO_DIVISAS, TASA_CAMBIO, COD_CUENTA_DIVISAS, COD_CUENTA_BS, COD_MONEDA_ISO, " + "STATUS_ENVIO, ID_OPER, COMISION1, COMISION2, COMISION3, MTO_TOTAL_BS, MTO_BOLIVARES, OPERACION) " + "values('").append(ObjetoOArreglo).append("')");
		return (sql.toString());

	}

	public String insertarTransaccion(String cedulaRif, String nacionalidad, String nombre, String montoDivisas, String tipoCambio, String cuentaDivisas, String cuentaBs, String monedaIso) {// Conversar.

		StringBuffer sql = new StringBuffer();
		sql.append("Insert into INFI_TB_235_INTERVENCION (ID_OPER, ID_OC, OPERACION, STATUS_OPER, MTO_DIVISAS, MTO_BOLIVARES, TASA_CAMBIO, MTO_COMI, NACIONALIDAD, NRO_CED_RIF, NOM_CLIEN, CTA_CLIEN, FECH_OPER, COD_DIVISAS, STATUS_ENVIO, ID_JORNADA, CTA_CLIEN_DIVISAS) " + "values(INFI_SQ_236.NEXTVAL,INFI_SQ_236.NEXTVAL,'VOC','O',").append(montoDivisas).append(",0,").append(tipoCambio)
				.append(",0,'").append(nacionalidad).append("','").append(cedulaRif).append("','").append(nombre).append("','").append(cuentaBs).append("',TO_DATE(SYSDATE,'DD/MM/YY'),'").append(monedaIso).append("','0','20221224','").append(cuentaDivisas).append("')");

		System.out.println("Inserttt : " + sql);
		return (sql.toString());

	}

	public String modificarEstatusAnuacion(String id) {// Conversar.

		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE INFI_TB_235_INTERVENCION SET STATUS_ENVIO ='2' WHERE ID_OPER='" + id + "'");
		System.out.println("modificarEstatusAnuacion : " + sql);
		return (sql.toString());

	}

	public String modificarEstatusCerrarLote() {// Conversar.

		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE INFI_TB_235_INTERVENCION SET STATUS_ENVIO ='1' WHERE FECH_OPER = TO_DATE(SYSDATE, 'DD/MM/YY') AND STATUS_ENVIO ='0'");
		System.out.println("modificarEstatusAnuacion : " + sql);
		return (sql.toString());

	}

	/**
	 * Busca si la operacion se encuentra registrada en la tabla ADM_INFI.INFI_TB_235_INTERVENCION
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean verificarOperacion(String id) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM INFI_TB_235_INTERVENCION WHERE ID_OPER='").append(id).append("'");

		try {
			dataSet = db.get(dataSource, sql.toString());

		} catch (Exception e) {
			System.out.println("IntervencionDAO : verificarOperacion()" + e);

		}

		if (this.dataSet.count() > 0)
			return true;
		else
			return false;
	}

	public boolean verificarLoteCerrado() {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM INFI_TB_235_INTERVENCION WHERE FECH_OPER = TO_DATE(SYSDATE, 'DD/MM/YY') AND STATUS_ENVIO ='1'");

		try {
			dataSet = db.get(dataSource, sql.toString());

		} catch (Exception e) {
			System.out.println("IntervencionDAO : verificarOperacion()" + e);

		}

		if (this.dataSet.count() > 0)
			return true;
		else
			return false;
	}
	
	
	public boolean verificarUsuarioRegistro(String fecha, String usuarioRegistro) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM INFI_TB_235_INTERVENCION_BAN WHERE TO_CHAR(FECHA, 'DD-MM-YYYY')='"+fecha+"' AND USUARIO_REGISTRO ='"+usuarioRegistro+"'");

		try {
			dataSet = db.get(dataSource, sql.toString());

		} catch (Exception e) {
			System.out.println("IntervencionDAO : verificarOperacion()" + e);

		}

		if (this.dataSet.count() > 0)
			return true;
		else
			return false;
	}

	public boolean verificarEstatusAnulada(String id) {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM INFI_TB_235_INTERVENCION WHERE ID_OPER='").append(id).append("' AND (STATUS_ENVIO ='2' OR STATUS_ENVIO ='1')");

		try {
			System.out.println("verificarEstatusAnulada : " + sql.toString());
			dataSet = db.get(dataSource, sql.toString());

		} catch (Exception e) {
			System.out.println("IntervencionDAO : verificarOperacion()" + e);

		}

		if (this.dataSet.count() > 0)
			return false;
		else
			return true;
	}

	/**
	 * Eliminar la operacion por id
	 * 
	 * @param id
	 * @return
	 */
	public String eliminar(String id) {
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM INFI_TB_235_INTERVENCION WHERE ID_OPER='").append(id).append("'");
		return (sql.toString());
	}

	public Object moveNext() throws Exception {
		return null;
	}
}

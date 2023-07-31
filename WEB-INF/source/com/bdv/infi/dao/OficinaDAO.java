package com.bdv.infi.dao;

import javax.sql.DataSource;

import megasoft.Logger;
import megasoft.db;

/**
 * Clase para buscar, agregar, modificar y eliminar las oficinas que van a permitir la validación por parte del usuario para el ingreso al sistema
 */
public class OficinaDAO extends com.bdv.infi.dao.GenericoDAO {
	
	public String Dinamico = "";

	public OficinaDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
	}

	public OficinaDAO(DataSource ds) {
		super(ds);
	}

	/**
	 * Busca todas las oficinas registradas en la tabla INFI_TB_034_OFICINA
	 * 
	 * @throws Exception
	 */
	public void listar() throws Exception {

		StringBuffer sb = new StringBuffer();
		sb.append("select oficina_nro from infi_tb_034_oficina");
		dataSet = db.get(dataSource, sb.toString());
	}
	
	public void listarTotalesOficinaComercial() throws Exception {

		StringBuffer sb = new StringBuffer();
		sb.append("SELECT COUNT (*) AS total FROM INFI_TB_035_OFICINA_COMERCIAL");
		dataSet = db.get(dataSource, sb.toString());
	}
	public void listarTotalesOficinaComercialActiva() throws Exception {

		StringBuffer sb = new StringBuffer();
		sb.append("SELECT COUNT (*) AS activa FROM INFI_TB_035_OFICINA_COMERCIAL WHERE ESTATUS = '1'");
		dataSet = db.get(dataSource, sb.toString());
	}
	public void listarTotalesOficinaComercialInactiva() throws Exception {

		StringBuffer sb = new StringBuffer();
		sb.append("SELECT COUNT (*) AS inactiva FROM INFI_TB_035_OFICINA_COMERCIAL WHERE ESTATUS = '0'");
		dataSet = db.get(dataSource, sb.toString());
	}

	public void listarPorParametros(int paginaAMostrar, int registroPorPagina) {

		StringBuffer sb = new StringBuffer();
		sb.append("SELECT NRO,DESCRIPCION,MUNICIPIO,DIRECCION,ESTADO,decode(ESTATUS,'0','DESACTIVADO','1','ACTIVADO') as ESTATUS FROM INFI_TB_035_OFICINA_COMERCIAL ORDER BY to_number(NRO) asc ");
		
		System.out.println("listarPorParametros : " + sb.toString());
		try {
			dataSet = obtenerDataSetPaginado(sb.toString(), paginaAMostrar, registroPorPagina);

		} catch (Exception e) {
			System.out.println("OficinaDAO : listarPorParametros()" + e);
			Logger.error(this, "OficinaDAO : listarPorParametros()" + e);
		}

	}
	
	public void listarPorParametrosDinamicos(int paginaAMostrar, int registroPorPagina) {

		StringBuffer sb = new StringBuffer();
		sb.append("SELECT NRO,DESCRIPCION,MUNICIPIO,DIRECCION,ESTADO,decode(ESTATUS,'0','DESACTIVADO','1','ACTIVADO') as ESTATUS FROM INFI_TB_035_OFICINA_COMERCIAL ORDER BY " + this.Dinamico+" asc");
		
		System.out.println("listarPorParametros : " + sb.toString());
		try {
			dataSet = obtenerDataSetPaginado(sb.toString(), paginaAMostrar, registroPorPagina);

		} catch (Exception e) {
			System.out.println("OficinaDAO : listarPorParametros()" + e);
			Logger.error(this, "OficinaDAO : listarPorParametros()" + e);
		}

	}

	public void ListarEstado() {

		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ESTADO FROM INFI_TB_035_OFICINA_COMERCIAL GROUP BY ESTADO ORDER BY ESTADO ASC");

		try {
			dataSet = db.get(dataSource, sb.toString());

		} catch (Exception e) {
			System.out.println("OficinaDAO : ListarEstado()" + e);
			Logger.error(this, "OficinaDAO : ListarEstado()" + e);
		}
	}

	public void ListarMunicipio(String estado) {

		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ESTADO,MUNICIPIO FROM INFI_TB_035_OFICINA_COMERCIAL WHERE ESTADO LIKE '" + estado + "' GROUP BY ESTADO,MUNICIPIO ORDER BY MUNICIPIO ASC");

		try {
			dataSet = db.get(dataSource, sb.toString());

		} catch (Exception e) {
			System.out.println("OficinaDAO : ListarMunicipio()" + e);
			Logger.error(this, "OficinaDAO : ListarMunicipio()" + e);
		}
	}

	/**
	 * Inserta en la tabla el número de oficina
	 * 
	 * @param numeroOficina
	 * @return la consulta SQL para que sea ejecutada en el modelo
	 */
	public String insertar(String numeroOficina) {
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO INFI_TB_034_OFICINA (oficina_nro) values('").append(numeroOficina).append("')");
		return (sql.toString());
	}

	/**
	 * Modifica un número de oficina ya registrado
	 * 
	 * @param numeroOficinaAnterior
	 *            número de oficina que se desea modificar
	 * @param numeroOficinaNuevo
	 *            nuevo número de oficina a registrar
	 * @return instrucciones SQL para que sean ejecutadas en el modelo
	 */
	public String[] modificar(String numeroOficinaAnterior, String numeroOficinaNuevo) {

		String[] consultas = new String[2];
		consultas[0] = this.eliminar(numeroOficinaAnterior);
		consultas[1] = this.insertar(numeroOficinaNuevo);
		return (consultas);
	}

	/**
	 * Busca si la oficina se encuentra registrada en la tabla INFI_TB_034_OFICINA
	 * 
	 * @param numeroOficina
	 *            número de oficina que se desea buscar
	 * @return retorna verdadero en caso de existir registrada la oficina, falso en caso contrario
	 * @throws Exception
	 *             lanza una exception en caso de error
	 */
	public boolean verificarOficina(String numeroOficina) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM INFI_TB_034_OFICINA WHERE oficina_nro='").append(numeroOficina).append("'");
		dataSet = db.get(dataSource, sql.toString());
		if (this.dataSet.count() > 0)
			return true;
		else
			return false;
	}

	/**
	 * Elimina el número de oficina enviado
	 * 
	 * @param numeroOficina
	 *            número de oficina que se desea eliminar
	 * @return La instrucción SQL para que sea ejecutada en el modelo
	 */
	public String eliminar(String numeroOficina) {
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM INFI_TB_034_OFICINA WHERE OFICINA_NRO='").append(numeroOficina).append("'");
		return (sql.toString());
	}

	public Object moveNext() throws Exception {
		return null;
	}

	public void upSert(String numeroOficina, String descripcion, String direccion, String estado, String municipio) {
		
		StringBuffer sql = new StringBuffer();

		sql.append("begin insert into INFI_TB_035_OFICINA_COMERCIAL (NRO,DESCRIPCION,DIRECCION,ESTADO,MUNICIPIO,ESTATUS) ");
		sql.append("values('").append(numeroOficina).append("','").append(descripcion).append("','").append(direccion).append("','").append(estado).append("','").append(municipio).append("','1');");
		sql.append("exception when dup_val_on_index then UPDATE INFI_TB_035_OFICINA_COMERCIAL SET ");
		sql.append("DESCRIPCION = '" + descripcion + "', DIRECCION = '" + direccion + "', ESTADO = '" + estado + "', MUNICIPIO ='" + municipio + "'");
		sql.append(" WHERE NRO = '" + numeroOficina + "'; ");
		sql.append("end;");

		try {
			db.exec(dataSource, sql.toString());
		} catch (Exception e) {
			Logger.error(this, "OficinaDAO : upSert() " + e);
			System.out.println("OficinaDAO : upSert() " + e);
		}
		
	}

	public String eliminarComercial(String numeroOficina) {
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM INFI_TB_035_OFICINA_COMERCIAL WHERE NRO='").append(numeroOficina).append("'");
		return (sql.toString());
	}
	
	public String eliminarInventarioPorOficina(String numeroOficina) {
		StringBuffer sql = new StringBuffer();
		sql.append("DELETE FROM INFI_TB_030_INVENTARIO_OFICINA WHERE OFICINA_NRO='").append(numeroOficina).append("'");
		return (sql.toString());
	}

	public String insertar(String numeroOficina, String descripcion, String direccion, String estado, String municipio) {
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO INFI_TB_035_OFICINA_COMERCIAL (NRO,DESCRIPCION,DIRECCION,ESTADO,MUNICIPIO,ESTATUS) " + "values('").append(numeroOficina).append("','").append(descripcion).append("','").append(direccion).append("','").append(estado).append("','").append(municipio).append("','1')");

		System.out.println("insertar : " + sql.toString());
		return (sql.toString());
	}

	public String modificarComercial(String oficinaAnterior, String oficina, String descripcion, String direccion, String estado, String municipio, String estatus) {

		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE INFI_TB_035_OFICINA_COMERCIAL SET NRO ='").append(oficina).append("',DESCRIPCION ='").append(descripcion).append("',DIRECCION ='").append(direccion).append("',ESTADO ='").append(estado).append("',MUNICIPIO='").append(municipio).append("',ESTATUS='").append(estatus).append("' WHERE NRO = '").append(oficinaAnterior).append("'");

		return (sql.toString());
	}
	
	public String modificarEstatusPorLote(String oficinas, String estatus) {

		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE INFI_TB_035_OFICINA_COMERCIAL SET ESTATUS ='").append(estatus).append("' WHERE NRO in(").append(oficinas).append(")");
		System.out.println("modificarEstatusPorLote : " + sql);
		return (sql.toString());
	}
	
	public boolean verificarOficinaComercial(String numeroOficina) throws Exception {
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT OFICINA_NRO FROM INFI_TB_030_INVENTARIO_OFICINA WHERE OFICINA_NRO='").append(numeroOficina).append("'");
		dataSet = db.get(dataSource, sql.toString());
		if (this.dataSet.count() > 0)
			return true;
		else
			return false;
	}
}

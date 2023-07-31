package com.bdv.infi.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;

import javax.sql.DataSource;

import megasoft.db;

/**
 * Clase
 */
public class InventarioDivisasDAO extends com.bdv.infi.dao.GenericoDAO {
	public String Id;
	public String IdTabla;
	public String Fecha;
	public String Estado;
	public String Moneda;
	public String diaEntrega;
	public BigDecimal Monto;
	public BigDecimal MontoAsignado;
	public BigDecimal Consumido;
	public BigDecimal Porcentaje;
	public BigDecimal Disponible;
	public String Estatus;
	public String Donde = "";
	public int PaginaAMostrar = 1;
	public int RegistroPorPagina = 10;

	public InventarioDivisasDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
	}

	public InventarioDivisasDAO(DataSource ds) {
		super(ds);
	}

	@Override
	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public void ListarTodo() {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT OFC.NRO,OFC.DESCRIPCION,OFC.MUNICIPIO, OFC.DIRECCION," +
				"OFC.ESTADO,OFC.ESTATUS,INV.OFICINA_NRO,INV.ID,INV.FECHA,INV.MONEDA,INV.ASIGNADO," +
				"INV.PORCENTAJE,INV.MONTO,INV.CONSUMIDO,INV.DISPONIBLE,INV.DIASENTREGA,DECODE (INV.ESTATUS,  '0', 'DESACTIVADO',  '1', 'ACTIVADO') AS INVESTATUS " +
				"FROM INFI_TB_035_OFICINA_COMERCIAL OFC LEFT JOIN INFI_TB_030_INVENTARIO_OFICINA  INV ON OFC.NRO=INV.OFICINA_NRO ");
		sb.append(this.Donde);

		System.out.println("ListarTodo : " + sb.toString());
		try {
			dataSet = obtenerDataSetPaginado(sb.toString(), this.PaginaAMostrar, this.RegistroPorPagina);

		} catch (Exception e) {
			System.out.println("InventarioDivisasDAO : ListarTodo()" + e);
		}
	}
	
	public boolean verificarOficinaComercial(String numeroOficina) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM INFI_TB_035_OFICINA_COMERCIAL WHERE nro='").append(numeroOficina).append("'");
		dataSet = db.get(dataSource, sql.toString());
		if (this.dataSet.count() > 0)
			return true;
		else
			return false;
	}
	
	public boolean verificarInventario(String numeroOficina) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM INFI_TB_030_INVENTARIO_OFICINA WHERE OFICINA_NRO='").append(numeroOficina).append("'");
		dataSet = db.get(dataSource, sql.toString());
		if (this.dataSet.count() > 0)
			return true;
		else
			return false;
	}
	public boolean verificarInventario(String numeroOficina,String fecha) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM INFI_TB_030_INVENTARIO_OFICINA WHERE OFICINA_NRO='").append(numeroOficina).append("' AND TO_DATE(FECHA,'DD/MM/YY') = TO_DATE("+fecha+",'DD/MM/YY')");
		System.out.println("verificarInventario : " + sql.toString());
		dataSet = db.get(dataSource, sql.toString());
		if (this.dataSet.count() > 0)
			return true;
		else
			return false;
	}
	
	public void ListarTodoReporte() {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT * FROM INFI_TB_035_OFICINA_COMERCIAL OFC " + "LEFT JOIN INFI_TB_030_INVENTARIO_OFICINA  INV ON OFC.NRO=INV.OFICINA_NRO ");
		sb.append(this.Donde);

		System.out.println("ListarTodoReporte : " + sb.toString());
		try {
			dataSet = db.get(dataSource, sb.toString());

		} catch (Exception e) {
			System.out.println("InventarioDivisasDAO : ListarTodoReporte()" + e);
		}
	}

	public void ListarOficina() {

		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM INFI_TB_035_OFICINA_COMERCIAL OFC " + "LEFT JOIN INFI_TB_030_INVENTARIO_OFICINA INV " + "ON OFC.OFICINA_NRO = INV.OFICINA_NRO LEFT JOIN " + "(  SELECT ID_OFICINA, FECHA_SOLICITUD, SUM (MONTO) AS MONTO FROM INFI_TB_028_MOVIMIENTOS " + "GROUP BY ID_OFICINA, FECHA_SOLICITUD) MOV " + " ON OFC.OFICINA_NRO = MOV.ID_OFICINA ");
		sb.append(this.Donde);
		try {
			dataSet = obtenerDataSetPaginado(sb.toString(), this.PaginaAMostrar, this.RegistroPorPagina);

		} catch (Exception e) {
			System.out.println("OficinaDAO : listarPorParametros()" + e);

		}

	}

	public String RegistarInventario() {
		// sql insert Inventario
		String insert = "INSERT INTO INFI_TB_030_INVENTARIO_OFICINA " + "(OFICINA_NRO,ID,FECHA,MONEDA,ASIGNADO,PORCENTAJE,MONTO,CONSUMIDO,DISPONIBLE,DIASENTREGA,ESTATUS) VALUES(" + "'" + this.Id + "'," + this.IdTabla + "," + "to_date(SYSDATE,'DD/MM/YY')" + ",'" + this.Moneda + "'," + this.Monto + "," + this.Porcentaje + "," + this.MontoAsignado + "," + this.Consumido + "," + this.Disponible + "," + this.diaEntrega
				+ "," + this.Estatus + ")";
		 System.out.println("RegistarInventario :" + insert);
		return insert;
	}

	public String ModificarInventario() {
		String update = "UPDATE INFI_TB_030_INVENTARIO_OFICINA SET ASIGNADO = " + this.Monto + ", DIASENTREGA = " + this.diaEntrega + ",PORCENTAJE = " + this.Porcentaje + ", DISPONIBLE =" + this.Disponible + ",CONSUMIDO = " + this.Consumido + " ,ESTATUS ='"+ this.Estatus + "'  WHERE OFICINA_NRO = '" + this.Id + "' AND MONEDA='"+this.Moneda+"' AND TO_DATE(FECHA,'DD/MM/YY') = TO_DATE('"+this.Fecha+"','DD/MM/YYYY')";
//		String update = "UPDATE INFI_TB_030_INVENTARIO_OFICINA SET ASIGNADO = " + this.Monto + ", PORCENTAJE = " + this.Porcentaje + ", DISPONIBLE =" + this.Disponible + ",CONSUMIDO = " + this.Consumido + " WHERE OFICINA_NRO = " + this.Id + " AND MONEDA='"+this.Moneda+"'AND TO_DATE(FECHA,'DD/MM/YY') = TO_DATE('"+this.Fecha+"','DD/MM/YYYY')";
		System.out.println("ModificarInventario :" + update);
		return update.toString();
	}
	
	
	public String ModificarDiasPorLote() {
		String update = "UPDATE INFI_TB_030_INVENTARIO_OFICINA SET DIASENTREGA = " + this.diaEntrega + " WHERE OFICINA_NRO in (SELECT OFC.NRO FROM INFI_TB_035_OFICINA_COMERCIAL OFC LEFT JOIN INFI_TB_030_INVENTARIO_OFICINA INV ON OFC.NRO = INV.OFICINA_NRO WHERE INV.MONEDA = '"+this.Moneda+"' AND OFC.ESTADO = '"+this.Estado+"' "+this.Donde+" AND TO_DATE (INV.FECHA, 'DD/MM/YY') = TO_DATE ('"+this.Fecha+"', 'DD/MM/YYYY'))";
		System.out.println("ModificarInventario :" + update);
		return update.toString();
	}
	
//	public String InsertaInvetario() {
//		String update = "UPDATE INFI_TB_030_INVENTARIO_OFICINA SET ASIGNADO = " + this.Monto + ", PORCENTAJE = " + this.Porcentaje + ", DISPONIBLE =" + this.Disponible + ",CONSUMIDO = " + this.Consumido + " WHERE OFICINA_NRO = " + this.Id;
//		System.out.println("ModificarInventario :" + update);
//		return update.toString();
//	}

	public void OficnasSinDisponible() throws Exception {

		try {
			conn = conn == null ? this.dataSource.getConnection() : conn;
			CallableStatement procedimientoAlmacenado = conn.prepareCall("{ call ADM_INFI.intervencion.OficnasSinDisponible }");
			procedimientoAlmacenado.execute();
			System.out.println("Hola mundo");

		} catch (Exception e) {
			if (conn != null) {
				conn.rollback();
			}
			throw new Exception("Error en la actualización Oficina sin disponible: " + e.getMessage());
		} finally {
			this.closeResources();
			this.cerrarConexion();
		}
	}

	public void RegistarMovimiento() {
		// sql insert Movimiento

	}

	public void MontoDisponible() {

	}
}

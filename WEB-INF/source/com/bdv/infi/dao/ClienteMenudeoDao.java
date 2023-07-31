package com.bdv.infi.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.bdv.infi.data.ClienteMenudeo;

import megasoft.db;

public class ClienteMenudeoDao extends GenericoDAO {

	protected StringBuilder sql = new StringBuilder();
	List<ClienteMenudeo> lst = new ArrayList<ClienteMenudeo>();

	public ClienteMenudeoDao(DataSource ds) {
		super(ds);
		sql.append("SELECT ID_OPER, ");
		sql.append("ID_OC, ");
		sql.append("decode(substr(OPERACION,1,2),'52','VENTA','1203','COMPRA') as movimiento, ");
		sql.append("OPERACION, STATUS_OPER, MTO_DIVISAS, ");
		sql.append("MTO_BOLIVARES, TASA_CAMBIO, MTO_COMI,");
		sql.append("NACIONALIDAD,");
		sql.append("decode(NACIONALIDAD,'V',LPAD(NRO_CED_RIF,8,'0') ,'E',LPAD(NRO_CED_RIF,8,'0') ,LPAD(NRO_CED_RIF,9,'0')) as CED_RIF,");
		sql.append("NOM_CLIEN, CTA_CLIEN, FECH_OPER,");
		sql.append("nvl(CON_ESTADIS,'298') as estadistica,");
		sql.append("COD_OFI_ORI, DECODE (COD_DIVISAS,'USD','840','EUR','978') as COD_DIVISAS, EMAIL_CLIEN, TEL_CLIEN, ");
		sql.append("decode(STATUS_ENVIO,'0','NO_ENVIADO','1','ENVIADO','2','MANUAL','3','ANULADA','4','RECHAZADA') as Estatus, ");
		sql.append("replace(replace(OBSERVACION,chr(10),''),chr(13),'') as OBSERVACION, ");
		sql.append("ID_BCV, MTO_DIVISAS_TRANS, ");
		sql.append("CASE NACIONALIDAD  WHEN 'V' THEN 1 WHEN 'E' THEN 2  ELSE 3 END AS NAC");

		sql.append(" FROM INFI_TB_234_VC_DIVISAS ");
		// TODO Auto-generated constructor stub
	}

	public ClienteMenudeoDao(Transaccion transaccion) throws Exception {
		super(transaccion);
	}

	/**
	 * Permite listar las operaciones de INFI_TB_234_VC_DIVISAS
	 * 
	 * @param operacion
	 *            5204 Ventas - 1203 Compra
	 * @param fecha
	 *            DD-MM-AAAA
	 * @return List<ClienteMenudeo> ClienteMenudeo
	 */
	public List<ClienteMenudeo> listarComprasVentas(String operacion, String fecha) {
		try {
			sql.append("WHERE OPERACION = '" + operacion + "' AND ");
			sql.append(" STATUS_ENVIO IN ('4', '0') AND "); // Verificar IN
			sql.append(" TO_DATE(FECH_OPER,'DD/MM/RRRR') = TO_DATE('").append(fecha).append("', 'DD/MM/RRRR')");
//			sql.append(" ORDER BY COD_DIVISAS");

			System.out.println("listarComprasVentas : " + sql);

			dataSet = db.get(dataSource, sql.toString());

			while (dataSet.next()) {

				ClienteMenudeo cl = new ClienteMenudeo();
				cl.Cedula = dataSet.getValue("CED_RIF");
				cl.IdOrdenes = dataSet.getValue("ID_OPER");
				cl.Nacionalidad = dataSet.getValue("NACIONALIDAD");
				cl.IntNacionalidad = Integer.parseInt(dataSet.getValue("NAC"));

				cl.Rif = dataSet.getValue("NACIONALIDAD") + cl.Cedula;
				cl.Nombre = dataSet.getValue("NOM_CLIEN");
				cl.CodigoMonedaIso = dataSet.getValue("COD_DIVISAS");
				cl.Telefono = dataSet.getValue("TEL_CLIEN");
				System.out.println("Telefono : " + dataSet.getValue("TEL_CLIEN"));
				cl.Correo = dataSet.getValue("EMAIL_CLIEN");
				cl.CtaConvenio20 = dataSet.getValue("CTA_CLIEN");
				cl.MonedaBase = dataSet.getValue("COD_DIVISAS");
				System.out.println("MonedaBase : " + dataSet.getValue("COD_DIVISAS"));
				cl.TipoMovimiento = dataSet.getValue("movimiento");
				cl.Operacion = dataSet.getValue("OPERACION");
				cl.MontoBase = new BigDecimal(dataSet.getValue("MTO_DIVISAS"));
				cl.TasaCambio = new BigDecimal(dataSet.getValue("TASA_CAMBIO"));
				cl.ConceptoEstadistica = dataSet.getValue("estadistica");
				cl.Estatus = dataSet.getValue("Estatus");

				this.lst.add(cl);

			}

		} catch (Exception e) {
			// TODO: handle exception
		}

		return this.lst;
	}

	/**
	 * 
	 */
	public void insertar() {
	}

	/**
	 * @throws Exception
	 * 
	 */
	public void modificar(ClienteMenudeo clt, String idBcv, String mensaje, String estatusEnvio) throws Exception {

		StringBuffer sql = new StringBuffer();

		sql.append(" UPDATE INFI_TB_234_VC_DIVISAS SET OBSERVACION ='").append(mensaje).append("'");
		if (idBcv!= null) {
			sql.append(",ID_BCV ='").append(idBcv).append("'");
		}
		if (clt.TasaCambio != null) {
			sql.append(",TASA_CAMBIO =").append(clt.TasaCambio).append("");
			sql.append(",MTO_DIVISAS_TRANS =").append(clt.MontoTransaccion).append("");
		}
		sql.append(",STATUS_ENVIO ='").append(estatusEnvio).append("'");
		sql.append(" WHERE ID_OPER =").append(clt.IdOrdenes);
		System.out.println("modificar : " + sql);
		db.exec(dataSource, sql.toString());

	}

	@Override
	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
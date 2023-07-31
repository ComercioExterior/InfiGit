package com.bdv.infi.dao;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.sql.DataSource;

import megasoft.db;

import org.apache.log4j.Logger;

import com.bdv.infi.data.OrdenesCruce;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

/**
 * Clase usada para la l&oacute;gica de inserci&oacute;n, modificaci&oacute;n y recuperaci&oacute;n de las ordenes en la base de datos
 */
@SuppressWarnings({ "rawtypes" })
public class OrdenesCrucesDAO extends com.bdv.infi.dao.GenericoDAO {

	private Logger logger = Logger.getLogger(OrdenesCrucesDAO.class);

	public OrdenesCrucesDAO(DataSource ds) {
		super(ds);
	}

	public Object moveNext() throws Exception {
		return null;
	}

	// NM26659 08/09/2014: Motodo modificado en proceso de optimizacion de proceso de Carga y Cierre de Cruce (Eliminacion del uso de SELECT * FROM)
	// NM29643 INFI_TTS_443 31/03/2014: Se crea metodo para obtener cruce a partir de ID Orden INFI
	/*
	 * Metodo de busqueda de cruces por ID Orden INFI
	 */
	public void listarCrucesPorIdOrdenInfi(long idOrdenInfi, String idTitulo, String... status) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT OC.ESTATUS,OC.TITULO_ID,OC.FECHA_VALOR,OC.PRECIO_TITULO,OC.MONTO_OPERACION,ORDENE_ID_BCV ");
		sql.append(" FROM INFI_TB_229_ORDENES_CRUCES OC WHERE ");
		sql.append("OC.ORDENE_ID='").append(idOrdenInfi).append("'");
		sql.append("");
		if (idTitulo != null && !idTitulo.equals("")) {
			sql.append(" AND UPPER(OC.TITULO_ID)='").append(idTitulo.toUpperCase()).append("'");
		}
		if (status != null && status.length > 0) {
			sql.append(" AND OC.ESTATUS IN (");
			for (int i = 0; i < status.length; i++) {
				sql.append("'").append(status[i]).append("'");
				if (i < (status.length - 1))
					sql.append(", ");
			}
			sql.append(")");
		}
		// System.out.println("listarCrucesPorIdOrdenInfi --> " + sql.toString());
		dataSet = db.get(dataSource, sql.toString());
	}

	// NM29643 INFI_TTS_443 31/03/2014: Se crea metodo para obtener cruce a partir de ID Orden INFI
	/*
	 * Metodo de busqueda de cruces por ID Orden INFI
	 */
	public void listarCrucesPorIdOrdenTipo(long idOrdenInfi, String indicadorTitulo, String... filtros) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT OC.* FROM INFI_TB_229_ORDENES_CRUCES OC WHERE");
		sql.append(" OC.ORDENE_ID='").append(idOrdenInfi).append("'");
		sql.append(" AND OC.INDICADOR_TITULO='").append(indicadorTitulo).append("'");
		if (filtros != null && filtros.length > 0 && filtros[0] != null) {
			sql.append(" AND OC.ID_CRUCE IN (").append(filtros[0]).append(")");
		}
		System.out.println("listarCrucesPorIdOrdenTipo : " + sql.toString());
		dataSet = db.get(dataSource, sql.toString());
	}

	// NM29643 INFI_TTS_443 09/04/2014: Se crea metodo para obtener cruce a partir del Nro de Operacion
	/*
	 * Metodo de busqueda de cruces por ID Orden INFI
	 */
	public void getByNroOperacion(String nroOperacion, String... status) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT OC.ID_CRUCE FROM INFI_TB_229_ORDENES_CRUCES OC WHERE ");
		sql.append("OC.NRO_OPERACION='").append(nroOperacion).append("'");
		if (status != null && status.length > 0) {
			sql.append(" AND OC.ESTATUS IN (");
			for (int i = 0; i < status.length; i++) {
				sql.append("'").append(status[i]).append("'");
				if (i < (status.length - 1))
					sql.append(", ");
			}
			sql.append(")");
		}
		// System.out.println("getByNroOperacion --> " + sql.toString());
		dataSet = db.get(dataSource, sql.toString());
	}

	// NM29643 INFI_TTS_443 09/04/2014: Se crea metodo para obtener cruce a partir del Nro de Operacion
	/*
	 * Metodo de busqueda de cruces por ID Orden INFI
	 */
	public void getByNroCotizacion(String nroCotizacion, String ordenId, String... status) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT OC.ID_CRUCE FROM INFI_TB_229_ORDENES_CRUCES OC WHERE ");
		sql.append("OC.ORDENE_ID_BCV='").append(nroCotizacion).append("'");
		sql.append(" AND OC.ORDENE_ID<>'").append(ordenId).append("'");
		if (status != null && status.length > 0) {
			sql.append(" AND OC.ESTATUS IN (");
			for (int i = 0; i < status.length; i++) {
				sql.append("'").append(status[i]).append("'");
				if (i < (status.length - 1))
					sql.append(", ");
			}
			sql.append(")");
		}
		// System.out.println("getByNroOperacion --> " + sql.toString());
		dataSet = db.get(dataSource, sql.toString());
	}

	// NM29643 INFI_TTS_443 09/04/2014: Se crea metodo para obtener cruces a partir del ID de ejecucion
	// NM26659 INFI_TTS_491 18/03/2015: Reverso de modificaciones aplicadas en requerimiento Web Service
	/*
	 * Metodo de busqueda de cruces por ID Orden INFI
	 */
	public void listByIdEjecucion(String idEjecucion, boolean paginado, int paginaAMostrar, int registroPorPagina) throws Exception {
		StringBuffer sql = new StringBuffer();
		// sql.append("SELECT OC.*, CASE WHEN OC.OBSERVACION is NULL THEN '' WHEN OC.OBSERVACION is NOT NULL THEN '<a href=\"javascript:alert(\"'||OC.OBSERVACION||'\")\">Ver</a>' END OBSERV, ");
		sql.append("SELECT OC.*, CASE WHEN OC.OBSERVACION is NULL THEN 'Registro Valido' WHEN OC.OBSERVACION is NOT NULL THEN OC.OBSERVACION END OBSERV, ");
		sql.append("CASE WHEN CTE.CLIENT_CEDRIF is NULL THEN '' WHEN CTE.CLIENT_CEDRIF is NOT NULL THEN CTE.TIPPER_ID||'-'||CTE.CLIENT_CEDRIF END CI_RIF, ");
		sql.append("CTE.CLIENT_NOMBRE, UI.UNDINV_NOMBRE ");
		// sql.append(" CASE oc.estatus WHEN 'CRUZADA' THEN CASE WHEN SUBSTR (oc.observacion, 0, 11) = 'ADVERTENCIA' THEN 'CRUZADO CON ADVERTENCIA' ELSE oc.estatus END WHEN 'NO_CRUZADA' THEN CASE WHEN SUBSTR (oc.observacion, 0, 11) = 'ADVERTENCIA' THEN 'NO CRUZADO CON ADVERTENCIA' ELSE oc.estatus END ELSE  oc.estatus END AS ESTATUS_CRUCE ");//,
		// DECODE(SUBSTR(OC.OBSERVACION,0,11),'ADVERTENCIA','CRUZADO CON ADVERTENCIA',OC.ESTATUS) AS ESTATUS_CRUCE FROM ");
		sql.append("FROM INFI_TB_229_ORDENES_CRUCES OC, INFI_TB_201_CTES CTE, INFI_TB_106_UNIDAD_INVERSION UI WHERE ");
		sql.append("OC.CLIENT_ID = CTE.CLIENT_ID(+) AND OC.UNDINV_ID = UI.UNDINV_ID(+) AND OC.ID_EJECUCION='").append(idEjecucion).append("'");
		;
		// sql.append("Order By Oc.Id_Ejecucion Asc ");
		sql.append("Order By Oc.ORDENE_ID Asc ");
		// System.out.println("listByIdEjecucion --> " + sql.toString());

		if (paginado && paginaAMostrar > 0 && registroPorPagina > 0) {
			getTotalDeRegistros(sql.toString());
			dataSet = obtenerDataSetPaginado(sql.toString(), paginaAMostrar, registroPorPagina);
			// System.out.println("---------------------listByIdEjecucion " + sql.toString());
		} else {
			dataSet = db.get(dataSource, sql.toString());
			// System.out.println("---------------------listByIdEjecucion " + sql.toString());
		}
		System.out.println("listByIdEjecucion-->" + sql);
	}

	// NM29643 INFI_TTS_443 31/03/2014: Se crea metodo para Insertar la carga de cruces a partir de OrdenesCruce
	/*
	 * Metodo de Inserci�n de cruces por medio de la carga.
	 */
	public String insertarCruce(OrdenesCruce cruce, boolean simadiAltoValor) throws Exception {
		String retorno = "";
		StringBuffer sql = new StringBuffer();
		// Validar obligatorios � posibles vacios
		sql.append("INSERT INTO ADM_INFI.INFI_TB_229_ORDENES_CRUCES (VALOR_NOMINAL, UNDINV_ID, " + "TITULO_MTO_INT_CAIDOS, TITULO_ID, TASA, PRECIO_TITULO, ORDENE_ID_BCV, ORDENE_ID,  OBSERVACION, " + "NRO_OPERACION, MONTO_OPERACION, ISIN, INDICADOR_TITULO, ID_EJECUCION, ID_CRUCE, "
				+ "ESTATUS, CONTRAPARTE, CLIENT_ID, FECHA_VALOR , CONTRAVALOR_BOLIVARES_CAPITAL , ORDENE_ID_OFERTA,ORDENE_ID_BCV_OFERTA ) VALUES (");
		// System.out.println("----"+sql.toString());

		if (cruce.getValorNominal() > 0)
			sql.append("'").append(cruce.getValorNominal()).append("', ");
		else
			sql.append("NULL, ");
		// System.out.println("----"+sql.toString());

		if (cruce.getIdUI() > 0)
			sql.append("'").append(cruce.getIdUI()).append("', ");
		else
			sql.append("NULL, ");
		// System.out.println("----"+sql.toString());

		if (cruce.getMtoInteresesCaidosTitulo() != null)
			sql.append("'").append(cruce.getMtoInteresesCaidosTitulo()).append("', ");
		else
			sql.append("NULL, ");
		// System.out.println("----"+sql.toString());

		sql.append(cruce.getIdTitulo() == null ? "NULL," : "'" + cruce.getIdTitulo() + "', ");
		// System.out.println("----"+sql.toString());

		sql.append(cruce.getTasa() == null ? "NULL," : "'" + cruce.getTasa() + "', ");
		// System.out.println("----"+sql.toString());

		sql.append(cruce.getPrecioTitulo() == null ? "NULL," : "'" + cruce.getPrecioTitulo() + "', ");// ?**
		// System.out.println("----"+sql.toString());
		if (simadiAltoValor) {
			if (cruce.getNroBCVOrdenDemanda() != null && !cruce.getNroBCVOrdenDemanda().equals("")) {
				sql.append("'").append(cruce.getNroBCVOrdenDemanda()).append("',");
			} else {
				sql.append(" NULL,");
			}
		} else {
			sql.append(cruce.getNroCotizacionString() == null ? "NULL," : "'" + cruce.getNroCotizacionString() + "', ");// ?**
		}
		// System.out.println("----"+sql.toString());

		sql.append(cruce.getIdOrdenInfiString() == null ? "NULL," : "'" + cruce.getIdOrdenInfiString() + "', ");// ?**
		// System.out.println("----"+sql.toString());

		sql.append(cruce.getObservacion() == null ? "NULL," : "'" + cruce.getObservacion().replaceAll("'", "").replaceAll("\"", "") + "', ");
		// System.out.println("----"+sql.toString());

		sql.append(cruce.getNroOperacionString() == null ? "NULL," : "'" + cruce.getNroOperacionString() + "', ");// ?**
		// System.out.println("----"+sql.toString());

		sql.append(cruce.getMontoOperacion() == null ? "NULL," : "'" + cruce.getMontoOperacion() + "', ");// ?**
		// System.out.println("----"+sql.toString());

		sql.append(cruce.getIsinString() == null ? "NULL," : "'" + cruce.getIsinString() + "', ");// ?**

		if (cruce.getIndicadorTitulo() >= 0)
			sql.append("'").append(cruce.getIndicadorTitulo()).append("', ");
		else
			sql.append("NULL, ");
		// System.out.println("----"+sql.toString());

		if (cruce.getIdEjecucion() > 0)
			sql.append("'").append(cruce.getIdEjecucion()).append("', ");
		else
			sql.append("NULL, ");
		// System.out.println("----"+sql.toString());

		sql.append("INFI_SQ_229.NEXTVAL, ");
		// System.out.println("----"+sql.toString());

		sql.append(cruce.getEstatus() == null ? "NULL," : "'" + cruce.getEstatus() + "', ");
		// System.out.println("----"+sql.toString());

		sql.append(cruce.getContraparte() == null ? "NULL," : "'" + cruce.getContraparte() + "', ");
		// System.out.println("----"+sql.toString());

		if (cruce.getIdCliente() > 0)
			sql.append("'").append(cruce.getIdCliente()).append("', ");
		else
			sql.append("NULL, ");
		// System.out.println("----"+sql.toString());

		sql.append(cruce.getFechaValor() == null ? "NULL, " : "'" + cruce.getFechaValor() + "', ");
		// if(cruce.getFechaValor()!=null)
		// //sql.append("TO_DATE('").append(cruce.getFechaValor()).append("','"+ConstantesGenerales.FORMATO_FECHA1+"') ");
		// else
		// sql.append("NULL");
		// System.out.println("----"+sql.toString());

		sql.append(cruce.getContravalorBolivaresCapital() == null ? "0" : cruce.getContravalorBolivaresCapital());

		if (cruce.getIdOrdenOferta() > 0) {
			sql.append(",'").append(cruce.getIdOrdenOferta()).append("'");
		} else {
			sql.append(", null");
		}

		if (cruce.getNroBCVOrdenOferta() != null && !cruce.getNroBCVOrdenOferta().equals("")) {
			sql.append(",'").append(cruce.getNroBCVOrdenOferta()).append("'");
		} else {
			sql.append(", null");
		}

		sql.append(")");

		/*
		 * sql.append("OC.ORDENE_ID=").append(idOrdenInfi); if(status!=null && status.length>0){ sql.append(" AND OC.ESTATUS IN ("); for(int i=0; i<status.length; i++){ sql.append("'").append(status[i]).append("'"); if(i<(status.length-1)) sql.append(", "); } sql.append(")"); }
		 */
		retorno = sql.toString();
		// System.out.println("----------INSERT CRUCE: "+retorno);
		// dataSet = db.get(dataSource, sql.toString());
		return retorno;
	}

	/**
	 * Metodo de busqueda de cruces por unidad de inversion y estatus de movimiento de cruce
	 * 
	 * @param idUnidad
	 * @param indicadorTitulo
	 * @param procesada
	 * @param status
	 * @throws Exception
	 */
	// NM26659 TTS-491 WEB SERVICE ALTO VALOR 01/04/2015
	public void listarCrucesPorUnidadInversion(long idUnidad, boolean indicadorTitulo, String verificionBCV, int procesada, String... status) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT OC.ORDENE_ID,OC.FECHA_VALOR,OC.MONTO_OPERACION,OC.TASA,OC.ORDENE_ID_BCV,OC.CONTRAPARTE,OC.CONTRAVALOR_BOLIVARES_CAPITAL,OC.ESTATUS,OC.TITULO_ID,OC.VALOR_NOMINAL,OC.TITULO_MTO_INT_CAIDOS,OC.PRECIO_TITULO,ORDENE_ID_OFERTA ");
		sql.append(" FROM INFI_TB_229_ORDENES_CRUCES OC WHERE OC.UNDINV_ID='").append(idUnidad).append("' ");

		if (indicadorTitulo) {
			sql.append(" AND OC.INDICADOR_TITULO='").append(ConstantesGenerales.STATUS_ACTIVO).append("' ");
		} else {
			sql.append(" AND OC.INDICADOR_TITULO='").append(ConstantesGenerales.STATUS_INACTIVO).append("' ");
		}

		sql.append(" AND OC.CRUCE_PROCESADO=").append(procesada).append(" ");

		if (verificionBCV != null) {
			sql.append(" AND OC.ORDENE_ESTATUS_BCV=").append(verificionBCV).append(" ");
		}

		if (status.length > 0 && status[0] != null) {
			int count = 0;
			sql.append(" AND OC.ESTATUS IN (");
			for (String element : status) {
				if (count > 0) {
					sql.append(",");
				}
				sql.append("'" + element + "'");
				++count;
			}

			sql.append(" ) ");
		}

		sql.append(" ORDER BY OC.ORDENE_ID ASC ");
		// System.out.println("listarCrucesPorUunidadInversion --> " + sql.toString());
		dataSet = db.get(dataSource, sql.toString());
	}

	/*
	 * Metodo de busqueda de cruces por unidad de inversion y estatus de movimiento de cruce
	 */
	public void listarCrucesPorUunidadInversion(long idUnidad, String status) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM INFI_TB_229_ORDENES_CRUCES OC WHERE OC.UNDINV_ID='").append(idUnidad).append("' ");

		if (status != null && !status.equals("")) {
			sql.append(" AND OC.ESTATUS='").append(status).append("' ");
		}

		sql.append(" ORDER BY OC.ORDENE_ID ASC ");
		// System.out.println("listarCrucesPorUunidadInversion --> " + sql.toString());
		dataSet = db.get(dataSource, sql.toString());
	}

	// ITS-2487 NM26659 26/02/2015 //08/12/2015 Inclusion de metodo por conflicto en integracion
	public void resumenCrucesPorUnidadInversion(long idUnidad) throws Exception {
		StringBuffer sql = new StringBuffer();

		sql.append(" SELECT (SELECT COUNT (ordene_id) FROM infi_tb_229_ordenes_cruces oc WHERE oc.undinv_id =").append(idUnidad).append(" AND oc.estatus = '").append(ConstantesGenerales.STATUS_NO_CRUZADA).append("') AS no_cruzadas, ");
		sql.append("(SELECT COUNT (DISTINCT oc.ordene_id) FROM infi_tb_229_ordenes_cruces oc WHERE oc.undinv_id = ").append(idUnidad).append(" AND oc.estatus = '").append(ConstantesGenerales.STATUS_CRUZADA).append("') AS cruzadas, ");
		sql.append("(SELECT COUNT (*) FROM (SELECT ordene_id FROM infi_tb_204_ordenes ord WHERE ord.UNIINV_ID=").append(idUnidad).append("AND ord.ORDSTA_ID IN ('").append(StatusOrden.ENVIADA).append("','").append(StatusOrden.PROCESO_CRUCE).append("') ");
		sql.append(" MINUS ");
		sql.append("SELECT DISTINCT TO_NUMBER (ordene_id) FROM infi_tb_229_ordenes_cruces oc WHERE oc.undinv_id = TO_NUMBER (").append(idUnidad).append(") AND oc.estatus <> '").append(ConstantesGenerales.STATUS_INVALIDA).append("')) AS no_cargadas ");
		sql.append(" FROM DUAL ");

		// System.out.println("resumenCrucesPorUnidadInversion --> " + sql.toString());
		dataSet = db.get(dataSource, sql.toString());
	}

	public void resumenCrucesPorUunidadInversion(long idUnidad, String... statusOrden) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COUNT (1) cantidad_ordenes, ord.ordsta_id estatus FROM  infi_tb_204_ordenes ord,infi_tb_229_ordenes_cruces oc where ");

		sql.append(" TO_CHAR(ord.ordene_id) =oc.ordene_id(+) ");

		if (idUnidad > 0) {
			sql.append(" AND ORD.UNIINV_ID =").append(idUnidad).append(" ");
		}

		if (statusOrden.length > 0 && statusOrden[0] != null) {
			int count = 0;
			sql.append(" AND ord.ordsta_id IN (");
			for (String element : statusOrden) {
				if (count > 0) {
					sql.append(",");
				}
				sql.append("'" + element + "'");
				++count;
			}
			sql.append(") ");
		}

		sql.append(" GROUP BY ord.ordsta_id");
		// System.out.println("resumenCrucesPorUunidadInversion --> " + sql.toString());
		dataSet = db.get(dataSource, sql.toString());
	}

	/**
	 * Metodo para eliminar todos los registros invalidos de la tabla infi_tb_229_ordenes_cruces asociados a una Unidad de inversion
	 * */
	public String eliminarCrucesInvalidosPorUnidadId(long idUnidad) throws Exception {
		StringBuffer sql = new StringBuffer();

		sql.append("DELETE FROM infi_tb_229_ordenes_cruces oc WHERE ");
		sql.append(" AND oc.undinv_id ='").append(idUnidad).append("' ");
		sql.append(" AND oc.ESTATUS='").append(ConstantesGenerales.STATUS_INVALIDA).append("' ");

		return sql.toString();
	}

	public int eliminarCrucesInvalidosPorUnidadInv(long idUnidad) throws Exception {

		StringBuffer sql = new StringBuffer();

		sql.append("DELETE FROM infi_tb_229_ordenes_cruces oc WHERE ");
		sql.append(" oc.undinv_id ='").append(idUnidad).append("' ");
		sql.append(" AND oc.ESTATUS='").append(ConstantesGenerales.STATUS_INVALIDA).append("' ");

		db.exec(dataSource, sql.toString());
		return 0;
	}

	/**
	 * Metodo de busqueda de ordenes por unidad de inversion que no contienen movimientos en los cruces registrados
	 **/
	public void listarOrdenesSinMovimientoCrucePorIdUniInv(long idUnidad) throws Exception {
		StringBuffer sql = new StringBuffer();

		sql.append("SELECT TO_CHAR(ord.ordene_id) FROM infi_tb_204_ordenes ord WHERE ");
		sql.append(" ORD.TRANSA_ID='").append(TransaccionNegocio.TOMA_DE_ORDEN).append("' ");
		sql.append(" AND ord.ordsta_id not in('").append(StatusOrden.CANCELADA).append("','").append(StatusOrden.PENDIENTE).append("')");
		sql.append(" AND ord.uniinv_id =").append(idUnidad).append(" ");
		sql.append(" MINUS ");
		sql.append("SELECT DISTINCT oc.ordene_id FROM infi_tb_229_ordenes_cruces oc WHERE  oc.estatus <>'").append(ConstantesGenerales.STATUS_INVALIDA).append("' ");
		sql.append("AND oc.undinv_id ='").append(idUnidad).append("' ");

		System.out.println("listarOrdenesSinMovimientoCrucePorIdUniInv: " + sql.toString());
		dataSet = db.get(dataSource, sql.toString());
	}

	public void consultarCruces(long idUnidad, String status, long idCliente, String idOrden, String statusP, String idEjecucion, String indTitulo, boolean paginado, int paginaAMostrar, int registroPorPagina) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("Select Oc.*, Cl.Tipper_Id || '-' || Cl.Client_Cedrif As Cliente_Cedrif, Cl.Client_Nombre AS Client_Nombre, (Select Undinv_Nombre From Infi_Tb_106_Unidad_Inversion Where Undinv_Id = Oc.Undinv_Id) As Unidnv_Nombre, ");
		sql.append("CASE WHEN OC.CRUCE_PROCESADO = 0 THEN 'No' WHEN OC.CRUCE_PROCESADO = 1 Then 'Si' END PROC ");// ,OC.ESTATUS AS ESTATUS
		sql.append("FROM Infi_Tb_229_Ordenes_Cruces Oc, Infi_Tb_201_Ctes Cl, INFI_TB_106_UNIDAD_INVERSION UI WHERE Oc.Client_Id = Cl.Client_Id(+)AND OC.UNDINV_ID = UI.UNDINV_ID(+)");
		// sql.append("Select Oc.*, Cl.Client_Cedrif As Cliente_Cedrif, Cl.Client_Nombre As Client_Nombre From Infi_Tb_229_Ordenes_Cruces Oc, Infi_Tb_201_Ctes Cl, INFI_TB_106_UNIDAD_INVERSION UI Where Oc.Client_Id = Cl.Client_Id(+) AND OC.UNDINV_ID = UI.UNDINV_ID(+) ");

		if (idUnidad > 0)
			sql.append("AND Oc.Undinv_Id = '").append(idUnidad).append("' ");

		if (idCliente > 0)
			sql.append("AND OC.CLIENT_ID='").append(idCliente).append("' ");

		if (idOrden != null && !idOrden.equals(""))
			sql.append("AND OC.ORDENE_ID IN ('").append(idOrden.replace(",", "','")).append("') ");

		if (idEjecucion != null && !idEjecucion.equals(""))
			sql.append("AND OC.ID_EJECUCION IN (").append(idEjecucion).append(") ");
		// sql.append("AND OC.ID_EJECUCION='").append(idEjecucion).append("' ");

		if (status != null && !status.equals("")) {
			sql.append(" AND OC.ESTATUS='").append(status).append("' ");
		}

		if (statusP != null && !statusP.equals(""))
			sql.append("AND OC.CRUCE_PROCESADO='").append(statusP).append("' ");

		if (indTitulo != null && !indTitulo.equals(""))
			sql.append("AND OC.INDICADOR_TITULO='").append(indTitulo).append("' ");

		sql.append("Order By Oc.Id_Ejecucion Asc, Oc.Ordene_id asc");

		if (paginado && paginaAMostrar > 0 && registroPorPagina > 0) {
			getTotalDeRegistros(sql.toString());
			dataSet = obtenerDataSetPaginado(sql.toString(), paginaAMostrar, registroPorPagina);
			// System.out.println("---------------------consultarCruces " + sql.toString());
		} else {
			dataSet = db.get(dataSource, sql.toString());
			// System.out.println("---------------------consultarCruces " + sql.toString());
		}
		System.out.println(sql);
	}

	/**
	 * Metodo de consulta de cruces para validacion en la eliminacion
	 * 
	 * @param idUnidad
	 * @param status
	 * @param idCliente
	 * @param idOrden
	 * @param statusOrden
	 * @param idEjecucion
	 * @param indTitulo
	 * @throws Exception
	 */
	// NM26659 15/04/2015 TTS_491 WEB SERVICE ALTO VALOR (Inclusino campo oc.ORDENE_ESTATUS_BCV para no eliminar registros ya verificados y aceptados por BCV)
	public void consultarIdCruces(long idUnidad, String status, long idCliente, String idOrden, String statusOrden, String idEjecucion, String indTitulo) throws Exception {
		StringBuffer sql = new StringBuffer();

		sql.append("Select Oc.ID_CRUCE, Oc.CRUCE_PROCESADO, oc.ORDENE_ESTATUS_BCV From Infi_Tb_229_Ordenes_Cruces Oc, Infi_Tb_201_Ctes Cl, INFI_TB_106_UNIDAD_INVERSION UI Where Oc.Client_Id = Cl.Client_Id(+) AND OC.UNDINV_ID = UI.UNDINV_ID(+) ");
		// sql.append("Select Oc.*, Cl.Client_Cedrif As Cliente_Cedrif, Cl.Client_Nombre As Client_Nombre From Infi_Tb_229_Ordenes_Cruces Oc, Infi_Tb_201_Ctes Cl Where Oc.Undinv_Id = ").append(idUnidad).append(" ");

		if (idUnidad > 0)
			sql.append("AND Oc.Undinv_Id = '").append(idUnidad).append("' ");

		if (idCliente > 0)
			sql.append("AND OC.CLIENT_ID='").append(idCliente).append("' ");

		if (idOrden != null && !idOrden.equals(""))
			sql.append("AND OC.ORDENE_ID IN ('").append(idOrden.replace(",", "','")).append("') ");

		if (idEjecucion != null && !idEjecucion.equals(""))
			sql.append("AND OC.ID_EJECUCION IN (").append(idEjecucion).append(") ");

		if (status != null && !status.equals(""))
			sql.append("AND OC.ESTATUS='").append(status).append("' ");

		if (indTitulo != null && !indTitulo.equals(""))
			sql.append("AND OC.INDICADOR_TITULO='").append(indTitulo).append("' ");

		sql.append("Order By Oc.Id_Ejecucion Asc, Oc.Ordene_id asc");

		dataSet = db.get(dataSource, sql.toString());

	}

	/**
	 * Consulta resgistros en la tabla INFI_TB_229_ORDENES_CRUCES.
	 */
	public void consultarEjecuciones() throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("Select Id_Ejecucion From Infi_Tb_229_Ordenes_Cruces Oc, INFI_TB_807_PROCESOS Cp Where Cp.Ejecucion_Id = Oc.Id_Ejecucion GROUP BY ID_EJECUCION ORDER BY ID_EJECUCION asc");
		// TODO Descomentar al crear nuevas UI sql.append("Select Id_Ejecucion From Infi_Tb_229_Ordenes_Cruces Oc, Infi_Tb_803_Control_Archivos Ca Where Ca.Ejecucion_Id = Oc.Id_Ejecucion	And Ca.Fecha = To_Char(Sysdate,'DD/MM/YY')GROUP BY ID_EJECUCION ORDER BY ID_EJECUCION asc");
		dataSet = db.get(dataSource, sql.toString());

	}

	/**
	 * Elimina el registro en la tabla INFI_TB_229_ORDENES_CRUCES.
	 */
	public void eliminarCruce(String cruces) throws Exception {

		db.exec(dataSource, "delete from INFI_TB_229_ORDENES_CRUCES where ID_CRUCE IN ('" + cruces.replace(",", "','") + "') AND CRUCE_PROCESADO = 0");

	}

	/**
	 * Elimina registros en la tabla INFI_TB_229_ORDENES_CRUCES.
	 */
	public void eliminarCruce(long idUnidad, String status, long idCliente, String idOrden, String statusOrden, String idEjecucion, String indTitulo) throws Exception {
		StringBuffer sql = new StringBuffer();

		sql.append("delete from INFI_TB_229_ORDENES_CRUCES Oc where CRUCE_PROCESADO = 0 ");
		// Inclusion parametro
		sql.append("AND OC.ORDENE_ESTATUS_BCV <> ").append(ConstantesGenerales.VERIFICADA_APROBADA).append(" ");

		if (idUnidad > 0)
			sql.append("AND Oc.Undinv_Id = '").append(idUnidad).append("' ");

		if (idCliente > 0)
			sql.append("AND OC.CLIENT_ID='").append(idCliente).append("' ");

		if (idOrden != null && !idOrden.equals(""))
			sql.append("AND OC.ORDENE_ID IN ('").append(idOrden.replace(",", "','")).append("') ");

		if (idEjecucion != null && !idEjecucion.equals(""))
			sql.append("AND OC.ID_EJECUCION IN (").append(idEjecucion).append(") ");

		if (status != null && !status.equals(""))
			sql.append("AND OC.ESTATUS='").append(status).append("' ");

		if (indTitulo != null && !indTitulo.equals(""))
			sql.append("AND OC.INDICADOR_TITULO='").append(indTitulo).append("' ");

		System.out.println("eliminarCruce--> opcion TODOS: " + sql.toString());
		db.exec(dataSource, sql.toString());

	}

	/*
	 * Consulta el monto Total de cruce en divisas o t�tulos
	 * 
	 * @param ordenId id de orden INFI
	 * 
	 * @param divisa 0 para divisas, 1 para t�tulos
	 * 
	 * @author NM25287
	 */
	public BigDecimal montoCrucePorTipoCruce(String ordenId, long tipoCruce, String... status) throws Exception {
		StringBuffer sql = new StringBuffer();
		BigDecimal montoCruce = null;
		sql.append("SELECT SUM(TO_NUMBER(MONTO_OPERACION,'99999999999999999.99')) MONTO FROM INFI_TB_229_ORDENES_CRUCES OC WHERE OC.ORDENE_ID='").append(ordenId).append("'");
		sql.append(" AND INDICADOR_TITULO=").append(tipoCruce);

		int count = 0;
		if (status.length > 0 && status[0] != null) {
			sql.append(" AND ESTATUS IN (");
			for (String element : status) {
				if (count > 0) {
					sql.append(",");
				}
				sql.append("'" + element + "'");
				++count;
			}
			sql.append(") ");
		}

		dataSet = db.get(dataSource, sql.toString());
		if (dataSet.count() > 0 && dataSet.next()) {
			if (dataSet.getValue("MONTO") != null) {
				montoCruce = new BigDecimal(dataSet.getValue("MONTO"));
			}
		}
		// System.out.println("montoCrucePorTipoCruce: "+sql.toString());
		return montoCruce;
	}

	public String actualizarCrucesProcesados(long ordenId) {
		StringBuffer sql = new StringBuffer();

		sql.append("UPDATE INFI_TB_229_ORDENES_CRUCES OC SET OC.CRUCE_PROCESADO=1 WHERE OC.ORDENE_ID='").append(ordenId).append("'");
		System.out.println("actualizarTitulosProcesados" + sql.toString());
		return sql.toString();
	}

	public void totalMontosCruces(long idUnidad, String status, long idCliente, String idOrden, String idEjecucion) throws Exception {
		StringBuffer sql = new StringBuffer();

		sql.append("Select  Oc.Ordene_Id, Cl.Tipper_Id ||'-'|| Cl.Client_Cedrif AS Cliente_Cedrif, Cl.Client_Nombre As Client_Nombre, Oc.tasa,Od.ordene_ped_monto, ");
		sql.append(" (SELECT Undinv_Nombre  FROM Infi_Tb_106_Unidad_Inversion  WHERE Undinv_Id = Oc.Undinv_Id  ) AS Unidnv_Nombre, Oc.Estatus, CASE  WHEN OC.CRUCE_PROCESADO = 0 THEN 'No' WHEN OC.CRUCE_PROCESADO = 1 THEN 'Si' END PROC, Oc.Fecha_Valor, ");
		sql.append("  NVL((Select Sum(To_Number(Ocru.Monto_Operacion, Translate(Ocru.Monto_Operacion, '012345678', '999999999'), 'nls_numeric_characters=''.,''')) From Infi_Tb_229_Ordenes_Cruces Ocru WHERE ocru.ordene_id = Oc.Ordene_Id AND ocru.indicador_titulo = '0' AND ocru.estatus in('CRUZADA')),0) AS TOTAL_DIV_DOL, ");
		sql.append("  NVL((Select Sum(To_Number(Ocru.VALOR_NOMINAL, Translate(Ocru.VALOR_NOMINAL, '012345678', '999999999'), 'nls_numeric_characters=''.,''')) From Infi_Tb_229_Ordenes_Cruces Ocru WHERE ocru.ordene_id = Oc.Ordene_Id AND ocru.indicador_titulo = '1' AND ocru.estatus in('CRUZADA')),0) AS TOTAL_TIT_DOL, ");
		sql.append("  NVL((Select (COALESCE((select sum(To_Number(cru.Valor_Nominal, Translate(cru.Valor_Nominal, '012345678', '999999999'), 'nls_numeric_characters=''.,''')) From Infi_Tb_229_Ordenes_Cruces cru  Where cru.Ordene_Id = Oc.Ordene_Id  And cru.Indicador_Titulo = '1'),0)+COALESCE((select sum(To_Number(cru.Monto_Operacion, Translate(cru.Monto_Operacion, '012345678', '999999999'), 'nls_numeric_characters=''.,''')) From Infi_Tb_229_Ordenes_Cruces cru  Where cru.Ordene_Id = Oc.Ordene_Id  And cru.Indicador_Titulo = '0'),0)) From Infi_Tb_229_Ordenes_Cruces Ocru Where Ocru.Ordene_Id = Oc.Ordene_Id GROUP BY Ocru.Ordene_Id),0) AS TOTAL_DOL, ");
		sql.append("  NVL((Select Sum(To_Number(Ocru.CONTRAVALOR_BOLIVARES_CAPITAL, Translate(Ocru.CONTRAVALOR_BOLIVARES_CAPITAL, '012345678', '999999999'), 'nls_numeric_characters=''.,''')) From Infi_Tb_229_Ordenes_Cruces Ocru WHERE ocru.ordene_id = Oc.Ordene_Id AND ocru.indicador_titulo = '0' AND ocru.estatus in('CRUZADA')),0) AS TOTAL_DIV_BS, ");
		sql.append("  NVL((Select Sum(To_Number(Ocru.CONTRAVALOR_BOLIVARES_CAPITAL, Translate(Ocru.CONTRAVALOR_BOLIVARES_CAPITAL, '012345678', '999999999'), 'nls_numeric_characters=''.,''')) From Infi_Tb_229_Ordenes_Cruces Ocru WHERE ocru.ordene_id = Oc.Ordene_Id AND ocru.indicador_titulo = '1' AND ocru.estatus in('CRUZADA')),0) AS TOTAL_TIT_BS, ");
		sql.append("  NVL((Select Sum(To_Number(Ocru.CONTRAVALOR_BOLIVARES_CAPITAL, Translate(Ocru.CONTRAVALOR_BOLIVARES_CAPITAL, '012345678', '999999999'), 'nls_numeric_characters=''.,''')) From Infi_Tb_229_Ordenes_Cruces Ocru WHERE ocru.ordene_id = Oc.Ordene_Id AND ocru.estatus in('CRUZADA')),0) AS TOTAL_BS ");
		sql.append(" FROM Infi_Tb_229_Ordenes_Cruces Oc,  Infi_Tb_201_Ctes Cl,  INFI_TB_106_UNIDAD_INVERSION UI, Infi_Tb_204_Ordenes Od WHERE Oc.Client_Id = Cl.Client_Id(+) And Oc.Undinv_Id   = Ui.Undinv_Id(+) And Oc.Ordene_id   = Od.Ordene_id(+) and Oc.Estatus in('CRUZADA') ");

		sql.append(" ");
		if (idUnidad > 0)
			sql.append("AND Oc.Undinv_Id = '").append(idUnidad).append("' ");

		if (idCliente > 0)
			sql.append("AND OC.CLIENT_ID='").append(idCliente).append("' ");

		if (idOrden != null && !idOrden.equals(""))
			sql.append("AND OC.ORDENE_ID IN ('").append(idOrden.replace(",", "','")).append("') ");

		if (idEjecucion != null && !idEjecucion.equals(""))
			sql.append("AND OC.ID_EJECUCION IN (").append(idEjecucion).append(") ");

		sql.append(" Group By Oc.Estatus, Oc.Ordene_Id,  Oc.Fecha_Valor, Cl.Client_Cedrif, Cl.Tipper_Id, Cl.Client_Nombre, OC.CRUCE_PROCESADO, Oc.Undinv_Id,Oc.tasa, Od.ordene_ped_monto ");
		sql.append(" Order By Oc.Ordene_Id Asc, Cl.Client_Cedrif,  Oc.Undinv_Id ");

		dataSet = db.get(dataSource, sql.toString());
	}

	/*
	 * Consulta el monto Total de cruce en divisas o t�tulos
	 * 
	 * @param ordenId id de orden INFI
	 * 
	 * @param divisa 0 para divisas, 1 para t�tulos
	 * 
	 * @author NM25287
	 */
	public BigDecimal montoCrucePorTipoCruceUnidadInv(long unidadInvId, long tipoCruce, String estatusCruce) throws Exception {
		StringBuffer sql = new StringBuffer();
		BigDecimal montoCruce = null;
		sql.append("SELECT SUM(TO_NUMBER(MONTO_OPERACION,'99999999999999999.99')) MONTO, SUM(TO_NUMBER(VALOR_NOMINAL,'99999999999999999.99')) VALOR_NOMINAL");
		sql.append(" FROM INFI_TB_229_ORDENES_CRUCES OC WHERE OC.UNDINV_ID='").append(unidadInvId).append("'");
		sql.append(" AND INDICADOR_TITULO=").append(tipoCruce);
		sql.append(" AND ESTATUS='").append(estatusCruce).append("'");
		dataSet = db.get(dataSource, sql.toString());
		if (dataSet.count() > 0 && dataSet.next()) {
			if (dataSet.getValue("MONTO") != null) {
				montoCruce = new BigDecimal(dataSet.getValue("MONTO"));
			}
		}
		return montoCruce;
	}

	public String fechaValorOrdenCruce(String ordeneId, long tipoCruce) throws Exception {
		StringBuffer sql = new StringBuffer();
		String fechaValor = null;
		sql.append("SELECT max (to_date(FECHA_VALOR, 'DD/MM/YYYY')) FECHA_VALOR FROM INFI_TB_229_ORDENES_CRUCES WHERE ");
		// sql.append(" ESTATUS='").append(estatusCruce).append("' AND");
		sql.append(" INDICADOR_TITULO=").append(tipoCruce);
		sql.append(" AND ORDENE_ID='").append(ordeneId).append("'");

		System.out.println("fechaValorOrdenCruce: " + sql.toString());

		dataSet = db.get(dataSource, sql.toString());
		if (dataSet.count() > 0 && dataSet.next()) {
			if (dataSet.getValue("FECHA_VALOR") != null) {
				fechaValor = dataSet.getValue("FECHA_VALOR");
			}
		}
		return fechaValor;
	}

	/*
	 * Actualiza Titulos procesados
	 * 
	 * @param ordenId id de orden INFI -
	 * 
	 * @param tituloId id de orden INFI -
	 * 
	 * @author CT19940
	 */
	public String actualizarTitulosProcesados(long ordenId, String tituloId) {
		StringBuffer sql = new StringBuffer();

		sql.append("UPDATE INFI_TB_229_ORDENES_CRUCES OC SET OC.IND_TITULO_PROCESADO=1 WHERE OC.ORDENE_ID='").append(ordenId).append("' AND OC.TITULO_ID ='").append(tituloId).append("'");
		System.out.println("actualizarTitulosProcesados" + sql.toString());
		return sql.toString();
	}

	/*
	 * Consulta Titulos por procesar
	 * 
	 * @param ordenId id de orden INFI - enviar null para consultar TODOS los titulos pendientes
	 * 
	 * @param liquidacionProcesada 0 para cuando se invoca desde el proceso de liquidacion, 1 para cuando se invoca desde el scheduler.
	 * 
	 * @author CT19940
	 */
	public void consultarTitulosPorFechaValor(String ordenId, int liquidacionProcesada) throws Exception {
		StringBuffer sql = new StringBuffer();

		// sql.append("SELECT DISTINCT(TITULO_ID),  ORDENE_ID,  UNDINV_ID,  to_date(FECHA_VALOR, 'DD/MM/YYYY')FECHA_VALOR  FROM INFI_TB_229_ORDENES_CRUCES WHERE INDICADOR_TITULO    = 1  AND IND_TITULO_PROCESADO  = 0 AND FECHA_VALOR <= SYSDATE   AND ESTATUS = 'CRUZADA' AND LIQUIDACION_PROCESADA = ").append(liquidacionProcesada).append(" ");
		sql.append("SELECT DISTINCT(TITULO_ID),  ORDENE_ID,  UNDINV_ID, ID_CRUCE,  to_date(FECHA_VALOR, 'DD/MM/YYYY')FECHA_VALOR  FROM INFI_TB_229_ORDENES_CRUCES WHERE INDICADOR_TITULO    = 1  AND IND_TITULO_PROCESADO  = 0 AND TO_DATE(FECHA_VALOR, 'DD/MM/YYYY') <= SYSDATE  AND ESTATUS = 'CRUZADA' AND LIQUIDACION_PROCESADA = ").append(liquidacionProcesada).append(" ");
		if (ordenId != null && !ordenId.equals("")) {
			sql.append("AND ORDENE_ID ='").append(ordenId).append("' ");
		}
		sql.append("GROUP BY TITULO_ID,ORDENE_ID,UNDINV_ID,FECHA_VALOR, ID_CRUCE ");
		sql.append("ORDER BY ORDENE_ID ");
		dataSet = db.get(dataSource, sql.toString());
		System.out.println("consultarTitulosPorFechaValor-------" + sql.toString());

	}

	/*
	 * Actualiza Ordenes liquidadas
	 * 
	 * @param ordenId id de orden INFI -
	 * 
	 * @param liquidacionProcesada 1 para cuando se invoca desde el proceso de liquidacion
	 * 
	 * @author CT19940
	 */
	public String actualizarOrdenesLiquidadas(long ordenId) {
		StringBuffer sql = new StringBuffer();

		sql.append("UPDATE INFI_TB_229_ORDENES_CRUCES OC SET OC.LIQUIDACION_PROCESADA='1' WHERE OC.ORDENE_ID='").append(ordenId).append("' ");
		System.out.println("actualizarOrdenesLiquidadas" + sql.toString());
		return sql.toString();
	}

	/**
	 * Metodo de busqueda de cruces por ID Orden de Oferta INFI Requerimiento: TTS-491 version Web Service Alto Valor
	 * 
	 * @param idOrdenOferta
	 * @throws Exception
	 * @author NM26659
	 */
	public void listarCrucesPorIdOrdenOfertaInfi(long idOrdenOferta) throws Exception {
		StringBuffer sql = new StringBuffer();

		sql.append("SELECT oc.ordene_id_oferta,OC.ESTATUS,OC.TITULO_ID,OC.FECHA_VALOR,OC.PRECIO_TITULO,OC.MONTO_OPERACION ");
		sql.append(" FROM INFI_TB_229_ORDENES_CRUCES OC WHERE ");
		sql.append("OC.ORDENE_ID_OFERTA='").append(idOrdenOferta).append("'");
		sql.append(" AND OC.ESTATUS ='").append(ConstantesGenerales.STATUS_CRUZADA).append("'");

		// System.out.println("listarCrucesPorIdOrdenOfertaInfi --> " + sql.toString());
		dataSet = db.get(dataSource, sql.toString());
	}

	/*
	 * NM32454 30/03/2015 Lista las ordenes por enviar al bcv para pacto
	 */
	public void listarOrdenesPorEnviarBCV(String cruceProcesado, String tipoNegocio, Integer incluirCliente, Integer clienteID, Integer tasaMinima, Integer tasaMaxima, Integer montoMinimo, Integer montoMaximo, boolean totales, long idUnidadInversion, boolean paginado, int paginaAMostrar, int registroPorPagina, boolean todos, boolean incluir, String ordenesSeleccionadas, String ordeneEstatusBCV,
			String fecha, String ordeneEstatusINFI) throws Exception {
		String condicionUI = "";
		String condicionTasa = "";
		String condicionMonto = "";
		String condicionCliente = "";

		if (idUnidadInversion != 0) {
			condicionUI = "and o.uniinv_id=" + idUnidadInversion;
		}

		if ((tasaMinima != null && tasaMinima != 0)) {
			condicionTasa = " AND O.ORDENE_TASA_POOL >= " + tasaMinima;
		}

		if ((tasaMaxima != null && tasaMaxima != 0)) {
			condicionTasa += " AND O.ORDENE_TASA_POOL <= " + tasaMaxima;
		}

		if ((montoMinimo != null && montoMinimo != 0)) {
			condicionMonto = " AND O.ORDENE_ADJ_MONTO >= " + montoMinimo;
		}

		if ((montoMaximo != null && montoMaximo != 0)) {
			condicionMonto += " AND O.ORDENE_ADJ_MONTO <= " + montoMaximo;
		}

		if ((clienteID != null && clienteID != 0)) {
			if (incluirCliente.equals(1)) { // SE INCLUYE AL CLIENTE
				condicionCliente = " AND CTES.client_id IN (" + clienteID + ")";
			} else { // SE EXCLUYE AL CLIENTE
				condicionCliente = " AND CTES.client_id NOT IN (" + clienteID + ")";
			}

		}

		StringBuilder sql = new StringBuilder();

		if (!totales) { // NO SE VAN A CONSULTAR LOS TOTALES
			sql.append("SELECT CTES.CLIENT_CEDRIF CLIENT_CEDRIF_DE, ");
			sql.append("CTES.TIPPER_ID TIPPER_ID_DE, ");
			sql.append("CTES.CLIENT_NOMBRE CLIENT_NOMBRE_DE, ");
			sql.append("OFER.CLIENT_NOMBRE CLIENT_NOMBRE_OF, ");
			sql.append("OFER.TIPPER_ID TIPPER_ID_OF, ");
			sql.append("OFER.CLIENT_CEDRIF CLIENT_CEDRIF_OF, ");
			sql.append("OFER.ORDENE_MONTO_OFERTADO MONTO_OFERTADO, ");
			sql.append("OFER.ID_OFERTA ID_OFERTA, ");
			sql.append("CRU.ORDENE_ID_BCV ORDENE_ID_BCV_DE, ");
			sql.append("CRU.ORDENE_ID_BCV_OFERTA ORDENE_ID_BCV_OF, ");
			sql.append("CRU.ORDENE_ESTATUS_BCV, ");
			sql.append("CRU.ORDENE_ID_PACTO_BCV, ");
			sql.append("CRU.ORDENE_ID,    ");
			sql.append("O.ORDENE_TASA_POOL,    ");
			sql.append("CRU.OBSERVACION,    ");
			sql.append("UI.UNDINV_NOMBRE,   ");
			sql.append("UI.NRO_JORNADA,   ");
			sql.append(" CASE CRU.ordene_estatus_bcv ");
			sql.append("    WHEN 0 THEN 'Sin Verificar'");
			sql.append("    WHEN 1 THEN 'Verificada Aprobada BCV'");
			sql.append("    WHEN 2 THEN 'Verificada Rechazada BCV'");
			sql.append("    WHEN 3 THEN 'Verificada Aprobada Manual'");
			sql.append("    WHEN 4 THEN 'Verificada Rechazada Manual'");
			sql.append(" END estatus_string,  ");

			if (ordeneEstatusBCV.equals(ConstantesGenerales.SIN_VERIFICAR)) { // ORDENES QUE SE ENVIARAN AL BCV (SE VA A SUMAR Y AGRUPAR)
				sql.append("SUM (TO_NUMBER(CRU.MONTO_OPERACION,'999999999999999.99999')) ORDENE_ADJ_MONTO ");
			} else {
				sql.append("CRU.MONTO_OPERACION ORDENE_ADJ_MONTO ");
			}
		} else {// SE VAN A CONSULTAR LOS TOTALES
			sql.append("SELECT SUM (TO_NUMBER(CRU.MONTO_OPERACION,'999999999999999.99999')) monto_operacion, COUNT(1) cantidad_operaciones ");
		}

		sql.append("FROM INFI_TB_229_ORDENES_CRUCES CRU, ");
		sql.append("INFI_TB_201_CTES CTES, ");
		sql.append("INFI_TB_225_OFERTAS_SIMADI OFER, ");
		sql.append("INFI_TB_204_ORDENES O, ");
		sql.append("INFI_TB_106_UNIDAD_INVERSION UI ");

		sql.append("WHERE ");
		sql.append(" OFER.ID_OFERTA        = CRU.ORDENE_ID_OFERTA ");
		sql.append(" AND  O.ORDENE_ID      = CRU.ORDENE_ID ");
		sql.append(" AND  CRU.CLIENT_ID    = O.CLIENT_ID ");

		if (ordeneEstatusBCV.equals(ConstantesGenerales.SIN_VERIFICAR) || ordeneEstatusBCV.equals(ConstantesGenerales.VERIFICADA_APROBADA)) {
			sql.append(" AND  CRU.ESTATUS      = '").append(ordeneEstatusINFI).append("' ");
		}

		// sql.append(" AND  CRU.CRUCE_PROCESADO =  ").append(cruceProcesado); //INDICA SI SE HA HA HECHO O NO EL CIERRE DE CRUCE
		sql.append(" AND  CTES.CLIENT_ID   = CRU.CLIENT_ID ");
		sql.append(" AND  O.UNIINV_ID      = UI.UNDINV_ID ");
		sql.append(" AND  UI.TIPO_NEGOCIO  =  ").append(tipoNegocio); // TIPO NEGOCIO ALTO O BAJO VALOR (PARA ESTE CASO SIEMPRE SERA ALTO VALOR)
		sql.append(condicionUI);

		sql.append(" AND TO_DATE(o.ordene_ped_fe_orden,'DD/MM/RRRR') = TO_DATE('").append(fecha).append("', 'DD/MM/RRRR')");
		sql.append(" AND CRU.ordene_estatus_bcv IN (").append(ordeneEstatusBCV).append(")"); // ESTATUS DE LA ORDEN BCV, SIN VERIFICAR, VERIFICADA O RECHZADA
		sql.append(condicionTasa);
		sql.append(condicionMonto);
		sql.append(condicionCliente);

		if (!todos) {
			if (incluir) {
				sql.append(" AND o.ordene_id in(" + ordenesSeleccionadas + ")");
			} else {
				sql.append(" AND o.ordene_id not in(" + ordenesSeleccionadas + ")");
			}
		}

		if (!totales && ordeneEstatusBCV.equals(ConstantesGenerales.SIN_VERIFICAR)) {
			sql.append(" GROUP BY ");
			sql.append(" CTES.CLIENT_CEDRIF, CTES.TIPPER_ID, CTES.CLIENT_NOMBRE,  CTES.CLIENT_CEDRIF, ");
			sql.append(" OFER.CLIENT_NOMBRE, OFER.TIPPER_ID, OFER.CLIENT_CEDRIF, OFER.ORDENE_MONTO_OFERTADO, OFER.ID_OFERTA, ");
			sql.append(" CRU.ORDENE_ID_BCV, CRU.ORDENE_ID_BCV_OFERTA, CRU.ORDENE_ID, ");
			sql.append(" O.ORDENE_TASA_POOL, CRU.OBSERVACION, UI.UNDINV_NOMBRE, UI.NRO_JORNADA, CRU.ORDENE_ESTATUS_BCV, CRU.ORDENE_ID_PACTO_BCV ");
		}

		sql.append(" ORDER by CRU.ORDENE_ID ");

		if (paginado && paginaAMostrar > 0 && registroPorPagina > 0) {
			dataSet = obtenerDataSetPaginado(sql.toString(), paginaAMostrar, registroPorPagina);
		} else {
			dataSet = db.get(dataSource, sql.toString());
		}
	}

	/*
	 * NM32454 30/03/2015 Lista las ordenes por enviar al bcv para demanda menudeo
	 */
	public void listarOrdenesPorEnviarBCVMenudeo(String crueProcesado, String tipoNegocio, Integer incluirCliente, Integer clienteID, Integer tasaMinima, Integer tasaMaxima, Integer montoMinimo, Integer montoMaximo, boolean totales, long idUnidadInversion, boolean paginado, int paginaAMostrar, int registroPorPagina, boolean todos, boolean incluir, String ordenesSeleccionadas,
			String ordeneEstatusBCV, String fecha, String ordeneEstatusINFI) throws Exception {
		String condicionUI = "";
		String condicionTasa = "";
		String condicionMonto = "";
		String condicionCliente = "";

		if (idUnidadInversion != 0) {
			condicionUI = "and o.uniinv_id=" + idUnidadInversion;
		}

		if ((tasaMinima != null && tasaMinima != 0)) {
			condicionTasa = " AND O.ORDENE_TASA_POOL >= " + tasaMinima;
		}

		if ((tasaMaxima != null && tasaMaxima != 0)) {
			condicionTasa += " AND O.ORDENE_TASA_POOL <= " + tasaMaxima;
		}

		if ((montoMinimo != null && montoMinimo != 0)) {
			condicionMonto = " AND TO_NUMBER(CRU.MONTO_OPERACION,'999999999999999.99999') >= " + montoMinimo;
		}

		if ((montoMaximo != null && montoMaximo != 0)) {
			condicionMonto += " AND TO_NUMBER(CRU.MONTO_OPERACION,'999999999999999.99999') <= " + montoMaximo;
		}

		if ((clienteID != null && clienteID != 0)) {
			if (incluirCliente.equals(1)) { // SE INCLUYE AL CLIENTE
				condicionCliente = " AND CTES.client_id IN (" + clienteID + ")";
			} else { // SE EXCLUYE AL CLIENTE
				condicionCliente = " AND CTES.client_id NOT IN (" + clienteID + ")";
			}

		}

		StringBuilder sql = new StringBuilder();

		if (!totales) { // NO SE VAN A CONSULTAR LOS TOTALES
			sql.append("SELECT CTES.CLIENT_CEDRIF CLIENT_CEDRIF, ");
			sql.append("CTES.TIPPER_ID TIPPER_ID, ");
			sql.append("CTES.CLIENT_NOMBRE CLIENT_NOMBRE, ");
			sql.append("CTES.CLIENT_CORREO_ELECTRONICO, ");
			sql.append("CTES.CLIENT_TELEFONO, ");
			sql.append("CRU.ORDENE_ID_BCV ORDENE_ID_BCV, ");
			sql.append("CRU.ORDENE_ESTATUS_BCV, ");
			sql.append("CRU.ORDENE_ID,    ");
			sql.append("O.ORDENE_TASA_POOL, ");
			sql.append("O.ORDENE_PED_MONTO, ");
			sql.append("O.ORDSTA_ID, ");
			sql.append("CRU.OBSERVACION,    ");
			sql.append("UI.UNDINV_NOMBRE,   ");
			sql.append("UI.NRO_JORNADA,   ");
			sql.append("S.CTA_NUMERO,   ");
			sql.append(" CASE CRU.ordene_estatus_bcv ");
			sql.append("    WHEN 0 THEN 'Sin Verificar'");
			sql.append("    WHEN 1 THEN 'Verificada Aprobada BCV'");
			sql.append("    WHEN 2 THEN 'Verificada Rechazada BCV'");
			sql.append("    WHEN 3 THEN 'Verificada Aprobada Manual'");
			sql.append("    WHEN 4 THEN 'Verificada Rechazada Manual'");
			sql.append(" END ESTATUS_STRING,  ");

			if (ordeneEstatusBCV.equals(ConstantesGenerales.SIN_VERIFICAR)) { // ORDENES QUE SE ENVIARAN AL BCV (SE VA A SUMAR Y AGRUPAR)
				sql.append("SUM (TO_NUMBER(CRU.MONTO_OPERACION,'999999999999999.99999')) ORDENE_ADJ_MONTO ");
			} else {
				sql.append("CRU.MONTO_OPERACION ORDENE_ADJ_MONTO ");
			}
		} else {// SE VAN A CONSULTAR LOS TOTALES
			sql.append("SELECT SUM (TO_NUMBER(CRU.MONTO_OPERACION,'999999999999999.99999')) monto_operacion, COUNT(1) cantidad_operaciones ");
		}

		sql.append("FROM INFI_TB_229_ORDENES_CRUCES CRU, ");
		sql.append("INFI_TB_201_CTES CTES, ");
		sql.append("INFI_TB_204_ORDENES O, ");
		sql.append(" SOLICITUDES_SITME S,  ");
		sql.append("INFI_TB_106_UNIDAD_INVERSION UI ");
		sql.append(" WHERE     TO_CHAR(o.ordene_id) = TO_CHAR(cru.ordene_id) ");
		sql.append(" AND  CRU.CLIENT_ID       = O.CLIENT_ID ");
		sql.append(" AND  s.numero_orden_infi = o.ORDENE_ID ");

		if (ordeneEstatusBCV.equals(ConstantesGenerales.SIN_VERIFICAR) || ordeneEstatusBCV.equals(ConstantesGenerales.VERIFICADA_APROBADA)) {
			sql.append(" AND  CRU.ESTATUS      = '").append(ordeneEstatusINFI).append("' ");
		}
		// lo que esten asi >>>>>>>>> es solo para pruba, volver a descomentar despues de terminar
		// sql.append(" AND  CRU.CRUCE_PROCESADO =  ").append(cruceProcesado); //INDICA SI SE HA HA HECHO O NO EL CIERRE DE CRUCE
		sql.append(" AND  CTES.CLIENT_ID   = CRU.CLIENT_ID ");
		sql.append(" AND  O.UNIINV_ID      = UI.UNDINV_ID ");
		sql.append(" AND  UI.TIPO_NEGOCIO  =  ").append(tipoNegocio); // TIPO NEGOCIO ALTO O BAJO VALOR (PARA ESTE CASO SIEMPRE SERA BAJO VALOR)>>>>>>>
		sql.append(condicionUI);

		sql.append(" AND TO_DATE(o.ordene_ped_fe_orden,'DD/MM/RRRR') = TO_DATE('").append(fecha).append("', 'DD/MM/RRRR')");
		sql.append(" AND CRU.ordene_estatus_bcv IN (").append(ordeneEstatusBCV).append(")"); // ESTATUS DE LA ORDEN BCV, SIN VERIFICAR, VERIFICADA O RECHZADA>>>>>>>>>>>
		sql.append(condicionTasa);
		sql.append(condicionMonto);
		sql.append(condicionCliente);

		if (!todos) {
			if (incluir) {
				sql.append(" AND o.ordene_id in(" + ordenesSeleccionadas + ")");
			} else {
				sql.append(" AND o.ordene_id not in(" + ordenesSeleccionadas + ")");
			}
		}

		if (!totales && ordeneEstatusBCV.equals(ConstantesGenerales.SIN_VERIFICAR)) {
			sql.append(" GROUP BY ");
			sql.append(" CTES.CLIENT_CEDRIF, CTES.TIPPER_ID, CTES.CLIENT_NOMBRE, CTES.CLIENT_CORREO_ELECTRONICO, CTES.CLIENT_TELEFONO,  CTES.CLIENT_CEDRIF, ");
			sql.append(" CRU.ORDENE_ID_BCV, CRU.ORDENE_ID_BCV_OFERTA, CRU.ORDENE_ID, ");
			sql.append(" O.ORDENE_TASA_POOL, O.ORDENE_PED_MONTO, O.ORDSTA_ID, CRU.OBSERVACION, UI.UNDINV_NOMBRE, UI.NRO_JORNADA, CRU.ORDENE_ESTATUS_BCV, CRU.ORDENE_ID_PACTO_BCV, S.CTA_NUMERO ");
		}

		sql.append(" ORDER by CRU.ORDENE_ID ");

		if (paginado && paginaAMostrar > 0 && registroPorPagina > 0) {
			dataSet = obtenerDataSetPaginado(sql.toString(), paginaAMostrar, registroPorPagina);
		} else {
			dataSet = db.get(dataSource, sql.toString());
		}
		System.out.println("listarOrdenesPorEnviarBCVMenudeo--> " + sql);
	}

	public void listarOrdenesPorEnviarMenudeoBCV1(boolean totales, boolean paginado, int paginaAMostrar, int registroPorPagina, boolean todos, String Statusoper, String fecha, String Status_envio, String Tipo_movi, String ordenesSeleccionadas, Integer clienteID, boolean historico,String combustible) throws Exception {

		String condicionCliente = "";

		if ((clienteID != null && clienteID != 0)) {
			// if(incluirCliente.equals(1)){ //SE INCLUYE AL CLIENTE
			condicionCliente = " AND NRO_CED_RIF IN (" + clienteID + ")";

		}

		StringBuilder sql = new StringBuilder();

		if (!totales) { // NO SE VAN A CONSULTAR LOS TOTALES
			sql.append("SELECT ID_OPER, ");
			sql.append("ID_OC, ");
			sql.append("CASE SUBSTR(OPERACION, 1, 2) WHEN '52' THEN 'VENTA' WHEN '12' THEN 'COMPRA' END AS movimiento, ");
			sql.append("OPERACION,");
			sql.append("STATUS_OPER, ");
			sql.append("MTO_DIVISAS, ");
			sql.append("MTO_BOLIVARES,");
			sql.append("TASA_CAMBIO,");
			sql.append("MTO_COMI,");
			sql.append("NACIONALIDAD,");
			sql.append("CASE NACIONALIDAD WHEN 'V' THEN LPAD (NRO_CED_RIF, 8, '0') WHEN 'E' THEN LPAD (NRO_CED_RIF, 8, '0') ELSE LPAD (NRO_CED_RIF, 9, '0') END AS CED_RIF,");
			// sql.append("RIGHT('00000' + Ltrim(Rtrim(NRO_CED_RIF)),10),");
			sql.append("NOM_CLIEN,");
			sql.append("CTA_CLIEN,");
			sql.append("FECH_OPER,");
			sql.append("nvl(CON_ESTADIS,'298') as estadistica,");
			sql.append("COD_OFI_ORI,");
			sql.append("COD_DIVISAS,");
			sql.append("EMAIL_CLIEN,");
			sql.append("TEL_CLIEN, ");
			sql.append("CASE STATUS_ENVIO WHEN '0' THEN 'NO_ENVIADO' WHEN '1' THEN 'ENVIADO' WHEN '2' THEN 'MANUAL' WHEN '3' THEN 'ANULADA'  WHEN '4' THEN 'RECHAZADA' END AS Estatus, ");
			sql.append("replace(replace(OBSERVACION,chr(10),''),chr(13),'') as OBSERVACION, ");
			sql.append("ID_BCV, ");
			sql.append("MTO_DIVISAS_TRANS ");

		} else {// SE VAN A CONSULTAR LOS TOTALES

			sql.append("SELECT");
			sql.append(" SUM (TO_NUMBER(MTO_DIVISAS,'999999999999999.99999')) as monto_operacion,");
			sql.append(" count(*) as cantidad_operaciones,");
			sql.append("decode(COD_DIVISAS,'USD','DOLARES','EUR','EUROS',COD_DIVISAS) as MONEDA, ");
			sql.append("decode(substr(OPERACION,1,2),'52','VENTA','12','COMPRA') as movimiento ");

		}
		if (historico) {
			sql.append(" FROM INFI_TB_234_VC_DIVISAS ");
		} else {
			sql.append(" FROM INFI_TB_234_VC_DIVISAS_HIST ");
		}
		
		sql.append("WHERE ");

		sql.append(" TO_DATE(FECH_OPER,'DD/MM/YYYY') = TO_DATE('").append(fecha).append("', 'DD/MM/YYYY') ");

		if (Status_envio.equals(ConstantesGenerales.ENVIO_MENUDEO) || Status_envio.equals(ConstantesGenerales.ENVIO_MENUDEO_FALTANTES) || Status_envio.equals(ConstantesGenerales.ENVIO_MENUDEO_MANUAL) || Status_envio.equals(ConstantesGenerales.ENVIO_MENUDEO_ANULADA) || Status_envio.equals(ConstantesGenerales.ENVIO_MENUDEO_RECHAZADA)) {
			sql.append("AND STATUS_ENVIO='").append(Status_envio).append("' ");
		}

		if (Tipo_movi.equals(ConstantesGenerales.COD_VENTA) || Tipo_movi.equals(ConstantesGenerales.COD_COMPRA)) {
			sql.append("AND OPERACION='").append(Tipo_movi).append("' ");
		}

		if (Statusoper.equals(ConstantesGenerales.ORIGINAL) || Statusoper.equals(ConstantesGenerales.REVERSADA)) {
			sql.append("AND STATUS_OPER='").append(Statusoper).append("' ");
		}
		if (combustible.equalsIgnoreCase("1")) {
			sql.append("AND CON_ESTADIS='100' ");
		} else if (combustible.equalsIgnoreCase("2")){
			sql.append("AND CON_ESTADIS <> '100' ");
		}

		if (Status_envio.equals(ConstantesGenerales.ENVIO_Y_RECHAZADAS_MENUDEO)) {
			System.out.println("paso ENVIO_Y_RECHAZADAS_MENUDEO");
			sql.append(" AND");
			sql.append(" (STATUS_ENVIO='").append("0").append("' ");
			sql.append(" OR ");
			sql.append("STATUS_ENVIO='").append("4").append("' ) ");

		}

		sql.append(condicionCliente);

		if (!todos) {
			sql.append(" AND ID_OPER in(" + ordenesSeleccionadas + ")");

		}

		if (totales) {
			sql.append("group by OPERACION,COD_DIVISAS");
		} else {
			sql.append(" ORDER BY ID_OPER");
		}

		if (paginado && paginaAMostrar > 0 && registroPorPagina > 0) {

			dataSet = obtenerDataSetPaginado(sql.toString(), paginaAMostrar, registroPorPagina);

		} else {

			dataSet = db.get(dataSource, sql.toString());

		}

		System.out.println("listarOrdenesPorEnviarBCVMenudeo : " + sql);

	}
	

	public void listarOrdenesPorEnviarMesaDeCambio(boolean totales, boolean paginado, int paginaAMostrar, int registroPorPagina, boolean todos, String fecha, String Status_envio, String Tipo_movi, String ordenesSeleccionadas) throws Exception {

		String condicionCliente = "";

		// if((clienteID != null && clienteID != 0)){
		// // if(incluirCliente.equals(1)){ //SE INCLUYE AL CLIENTE
		// condicionCliente = " AND NRO_CED_RIF IN ("+clienteID+")";
		//
		//
		// }

		StringBuilder sql = new StringBuilder();

		if (!totales) { // NO SE VAN A CONSULTAR LOS TOTALES
			sql.append("SELECT ID_OPER, ");
			sql.append("decode(OPERACION,'5204','VENTA','1203','COMPRA') as movimiento, ");
			sql.append("RIF_CLIENTE, ");
			sql.append("NOM_CLIEN, ");
			sql.append("COD_DIVISAS,");
			sql.append("MTO_DIVISAS,");
			sql.append("TASA_CAMBIO,");
			sql.append("COD_INS_BANCO,");
			sql.append("CTA_CONVENIO,");
			sql.append("CTA_CLIEN,");
			sql.append("FECH_OPER,");
			sql.append("decode(STATUS_ENVIO,'0','NO_ENVIADO','1','ENVIADO','2','MANUAL','3','ANULADA','4','RECHAZADA') as Estatus, ");
			sql.append("OBSERVACION, ");
			sql.append("ID_BCV,");
			sql.append("INSTRUMENTO");

		} else {

			sql.append("SELECT");
			sql.append(" SUM (TO_NUMBER(MTO_DIVISAS,'999999999999999.99999')) as monto_operacion,");
			sql.append(" count(*) as cantidad_operaciones,");
			sql.append("decode(COD_DIVISAS,'USD','DOLARES','EUR','EUROS',COD_DIVISAS) as MONEDA, ");
			sql.append("decode(OPERACION,'5204','VENTA','1203','COMPRA') as movimiento ");

		}

		sql.append(" FROM INFI_TB_236_MESA_CAMBIO ");

		// sql.append(" WHERE STATUS_OPER='").append(Status_envio).append("' ");

		sql.append("WHERE ");

		if (Status_envio.equals(ConstantesGenerales.ENVIO_MENUDEO) || Status_envio.equals(ConstantesGenerales.ENVIO_MENUDEO_FALTANTES) || Status_envio.equals(ConstantesGenerales.ENVIO_MENUDEO_MANUAL) || Status_envio.equals(ConstantesGenerales.ENVIO_MENUDEO_ANULADA) || Status_envio.equals(ConstantesGenerales.ENVIO_MENUDEO_RECHAZADA)) {
			sql.append("STATUS_ENVIO='").append(Status_envio).append("' ");
			sql.append("AND");
		}

		if (Tipo_movi.substring(0, 3).equals(ConstantesGenerales.COD_VENTA) || Tipo_movi.substring(0, 3).equals(ConstantesGenerales.COD_COMPRA)) {
			sql.append(" OPERACION='").append(Tipo_movi).append("' ");
			sql.append("AND");
			
			System.out.println("Listar Ordenes BCV Tipo_movi == ----->" + sql);
		}

		//
		// if(Statusoper.equals(ConstantesGenerales.ORIGINAL) || Statusoper.equals(ConstantesGenerales.REVERSADA)){
		// sql.append(" STATUS_OPER='").append(Statusoper).append("' ");
		// sql.append("AND");
		// }

		sql.append(" TO_DATE(FECH_OPER,'DD/MM/RRRR') = TO_DATE('").append(fecha).append("', 'DD/MM/RRRR')");

		sql.append(condicionCliente);

		if (!todos) {

			sql.append(" AND ID_OPER in(" + ordenesSeleccionadas + ")");

		}

		if (totales) {
			sql.append("group by OPERACION,COD_DIVISAS");
		}
		if (paginado && paginaAMostrar > 0 && registroPorPagina > 0) {
			dataSet = obtenerDataSetPaginado(sql.toString(), paginaAMostrar, registroPorPagina);
			System.out.println("dataSet----->" + dataSet);

		} else {
			dataSet = db.get(dataSource, sql.toString());
			System.out.println("dataSet----->" + dataSet);
		}

		System.out.println("listarOrdenesPorEnviarBCVMesaCambio--> " + sql);

	}

	public void listarPactosMesaDeCambio(boolean totales, boolean paginado, int paginaAMostrar, int registroPorPagina, String fecha) throws Exception {
		StringBuilder sql = new StringBuilder();

		sql.append("select m1.RIF_CLIENTE, decode(m1.OPERACION,'5204','VENTA','1203','COMPRA') as movimiento, m1.ID_BCV ,m3.RIF_CLIENTE as RIF_CLIENTE_1,decode(m3.OPERACION,'5204','VENTA','1203','COMPRA') as movimiento_1,m3.ID_BCV as ID_BCV_1,m3.FECH_OPER,m3.ID_PACTO, m3.MTO_PACTO");
		sql.append(" from  infi_tb_236_mesa_cambio m1,(select mc.RIF_CLIENTE,mc.OPERACION,mc.FECH_OPER, mc.ID_BCV,mc.ID_PACTO, MC.MTO_PACTO");
		sql.append(" from   infi_tb_236_mesa_cambio mc ");
		sql.append(" where ");
		sql.append(" TO_DATE(mc.FECH_OPER,'DD/MM/RRRR') = TO_DATE('").append(fecha).append("', 'DD/MM/RRRR')");
		sql.append(" and mc.OPERACION = 1203) m3");
		sql.append(" where ");
		sql.append(" m1.ID_PACTO = m3.ID_PACTO and m1.OPERACION = 5204");

		// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// sql.append("select m1.ID_OPER,m1.OPERACION,m1.FECH_OPER, m1.ID_BCV,m1.ID_PACTO ,m3.ID_OPER,m3.OPERACION,m3.FECH_OPER, m3.ID_BCV,m3.ID_PACTO");
		// sql.append(" from  infi_tb_236_mesa_cambio m1,");
		// sql.append("(select mc.ID_OPER,mc.OPERACION,mc.FECH_OPER, mc.ID_BCV,mc.ID_PACTO");
		// sql.append(" from   infi_tb_236_mesa_cambio mc");
		// sql.append(" WHERE");
		// sql.append(" TO_DATE(mc.FECH_OPER,'DD/MM/RRRR') = TO_DATE('").append(fecha).append("', 'DD/MM/RRRR')");
		// // sql.append(" mc.FECH_OPER ='").append(fecha).append("'");
		// // sql.append(" mc.ID_PACTO = '").append(idPacto).append("'");
		// sql.append(" and mc.OPERACION = 1203) m3 ");
		// sql.append("WHERE");
		// sql.append(" m1.ID_PACTO = m3.ID_PACTO");
		// sql.append(" and m1.OPERACION = 5204");
		// // sql.append(" TO_DATE(mc.FECH_OPER,'DD/MM/RRRR') = TO_DATE('").append(fecha).append("', 'DD/MM/RRRR')");

		// //////////////////////////////////////////////////////////////////////////////////////////////////////////////
		// StringBuilder sql = new StringBuilder();
		// sql.append("select ID_OPER,ID_BCV, ID_PACTO from INFI_TB_236_MESA_CAMBIO  ");
		// sql.append("WHERE");
		//
		// sql.append(" and ID_BCV is not null");

		System.out.println("listarPactosMesaDeCambio-->" + sql.toString());
		// dataSet = db.get(dataSource, sql.toString());
		dataSet = obtenerDataSetPaginado(sql.toString(), paginaAMostrar, registroPorPagina);

	}

	public void listarOrdenesPorEnviarIntervencion(boolean paginado, int paginaAMostrar, int registroPorPagina, String fecha, boolean todos, String ordenesSeleccionadas, String estatusEnvio) throws Exception {

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT COD_CLIENT, ");
		sql.append("NOMBRE_CLIENT, ");
		sql.append("FECHA_VALOR, ");
		sql.append("TIPO_OPERACION, ");
		sql.append("MTO_DIVISAS,");
		sql.append("TASA_CAMBIO,");
		sql.append("COD_CUENTA_DIVISAS,");
		sql.append("COD_CUENTA_BS,");
		sql.append("COD_MONEDA_ISO,");
		sql.append("COD_BCV,");
		sql.append("decode(STATUS_ENVIO,'0','NO_ENVIADO','1','ENVIADO','2','MANUAL','3','ANULADA','4','RECHAZADA') as Estatus");
		sql.append(",ID_OPER");

		sql.append(" FROM INFI_TB_235_INTERVENCION ");

		sql.append("WHERE ");
		sql.append(" STATUS_ENVIO='").append(estatusEnvio).append("' and ");
		sql.append(" TO_DATE(FECHA_VALOR,'DD/MM/RRRR') = TO_DATE('").append(fecha).append("', 'DD/MM/RRRR') and ");
		sql.append("TIPO_OPERACION='VOC'");

		if (!todos) {
			sql.append(" AND ID_OPER in(" + ordenesSeleccionadas + ")");
		}

		if (paginado && paginaAMostrar > 0 && registroPorPagina > 0) {
			dataSet = obtenerDataSetPaginado1(sql.toString(), paginaAMostrar, registroPorPagina);
		} else {
			dataSet = db.get(dataSource, sql.toString());
		}

		System.out.println("listarOrdenesPorEnviarIntervencion : " + sql);

	}

	public void listarOrdenesParaEnviarmanualMenudeoBCV(boolean totales, boolean paginado, int paginaAMostrar, int registroPorPagina, boolean todos, String Statusoper, String fecha, String Status_envio, String Tipo_movi, String ordenesSeleccionadas) throws Exception {

		// String condicionCliente = "";

		StringBuilder sql = new StringBuilder();

		sql.append("SELECT ID_OPER, ");
		sql.append("decode(substr(OPERACION,1,2),'52','VENTA','12','COMPRA') as movimiento, ");
		sql.append("STATUS_OPER, ");
		sql.append("MTO_DIVISAS, ");
		sql.append("MTO_BOLIVARES,");
		sql.append("TASA_CAMBIO,");
		sql.append("MTO_COMI,");
		sql.append("NACIONALIDAD,");
		sql.append("NRO_CED_RIF,");
		sql.append("NOM_CLIEN,");
		sql.append("CTA_CLIEN,");
		sql.append("FECH_OPER,");
		sql.append("CON_ESTADIS,");
		sql.append("COD_OFI_ORI,");
		sql.append("COD_DIVISAS,");
		sql.append("EMAIL_CLIEN,");
		sql.append("TEL_CLIEN, ");
		sql.append("decode(STATUS_ENVIO,'0','NO_ENVIADO','1','ENVIADO','2','MANUAL','3','ANULADA','4','RECHAZADA') as Estatus, ");
		sql.append("OBSERVACION ");
		// sql.append("ID_BCV ");

		sql.append(" FROM INFI_TB_234_VC_DIVISAS ");
		sql.append("WHERE ");

		sql.append(" TO_DATE(FECH_OPER,'DD/MM/RRRR') <= TO_DATE(").append(fecha).append(", 'DD/MM/RRRR') ");

		if (Status_envio.equals(ConstantesGenerales.ENVIO_MENUDEO) || Status_envio.equals(ConstantesGenerales.ENVIO_MENUDEO_FALTANTES)) {
			sql.append("AND STATUS_ENVIO='").append(Status_envio).append("' ");

		}

		if (Tipo_movi.equals(ConstantesGenerales.COD_VENTA) || Tipo_movi.equals(ConstantesGenerales.COD_COMPRA)) {
			sql.append(" AND OPERACION='").append(Tipo_movi).append("' ");

		}

		if (Statusoper.equals(ConstantesGenerales.ORIGINAL) || Statusoper.equals(ConstantesGenerales.REVERSADA)) {
			sql.append(" AND STATUS_OPER='").append(Statusoper).append("' ");

		}

		// sql.append(condicionCliente);

		if (!todos) {
			//
			sql.append(" AND ID_OPER in(" + ordenesSeleccionadas + ")");
			//
		}

		sql.append(" order by FECH_OPER DESC");

		if (paginado && paginaAMostrar > 0 && registroPorPagina > 0) {
			dataSet = obtenerDataSetPaginado(sql.toString(), paginaAMostrar, registroPorPagina);
			System.out.println("dataSet----->" + dataSet);

		} else {
			dataSet = db.get(dataSource, sql.toString());
			System.out.println("dataSet----->" + dataSet);
		}

		System.out.println("listarOrdenesPorEnviarBCVMenudeo--> " + sql);

	}

	public void listarOrdenesPorEnviarMenudeoBCV() throws Exception {
		StringBuilder sql = new StringBuilder();
		// NO SE VAN A CONSULTAR LOS TOTALES
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
		sql.append("VC.MTO_BOLIVARES");
		// ///////////////////////////////////
		sql.append("FROM INFI_TB_234_VC_DIVISAS VC, ");
		sql.append(" ORDER by VC.ID_OPER ");

		System.out.println("listarOrdenesPorEnviarBCVMenudeo--> " + sql);

	}

	// NM32454 INFI_TTS_491_WS_BCV ALTO VALOR 26/03/2015
	public void actualizarOrdenBCV(String ordeneID, String ordenBCV, String estatus, String observacion, String statusOrden) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE INFI_TB_229_ORDENES_CRUCES SET ORDENE_ESTATUS_BCV = ").append(estatus);

		if (ordenBCV != null) {
			sql.append(" ,  ORDENE_ID_PACTO_BCV = '").append(ordenBCV).append("'");
		}

		if (statusOrden != null) {
			sql.append(" ,  ESTATUS = '").append(statusOrden).append("'");
		}

		if (observacion != null) {
			if (observacion.length() > 1000) {
				observacion = observacion.substring(0, 999);
			}
			sql.append(" ,  OBSERVACION = '").append(observacion).append("'");
		}
		sql.append(" WHERE ORDENE_ID ='").append(ordeneID).append("'");
		db.exec(dataSource, sql.toString());
	}

	// NM32454 INFI_TTS_491_WS_BCV MENUDEO 26/03/2015
	public void actualizarOrdenBCVMenudeo(String ordeneID, String ordenBCV, String estatus, String observacion, String statusOrden) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE INFI_TB_229_ORDENES_CRUCES SET ORDENE_ESTATUS_BCV = ").append(estatus);

		if (ordenBCV != null) {
			sql.append(" ,  ORDENE_ID_BCV = '").append(ordenBCV).append("'"); // ID DE LA DEMANDA
		}

		if (statusOrden != null) {
			sql.append(" ,  ESTATUS = '").append(statusOrden).append("'");
		}

		if (observacion != null) {
			if (observacion.length() > 1000) {
				observacion = observacion.substring(0, 999);
			}
			sql.append(" ,  OBSERVACION = '").append(observacion).append("'");
		}
		sql.append(" WHERE ORDENE_ID ='").append(ordeneID).append("'");
		db.exec(dataSource, sql.toString());
		System.out.println("actualizarOrdenBCVMenudeo----> " + sql);
	}

	public void actualizarMenudeoBCV(String ordeneID, String fecha, String estatus) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE INFI_TB_234_VC_DIVISAS SET STATUS_ENVIO = '").append(estatus).append("'");

		sql.append(" WHERE ID_OPER ='").append(ordeneID).append("'");
		sql.append(" AND TO_DATE(FECH_OPER,'DD/MM/RRRR') = TO_DATE('").append(fecha).append("', 'DD/MM/RRRR')");
		db.exec(dataSource, sql.toString());
		System.out.println("actualizarOrdenBCVMenudeo----> " + sql);
	}

	public void actualizarMontoTransaccion(String ordeneID, String fecha, BigDecimal montoTransaccion) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE INFI_TB_234_VC_DIVISAS SET MTO_DIVISAS_TRANS = ").append(montoTransaccion).append("");
		sql.append(" WHERE ID_OPER ='").append(ordeneID).append("'");
		sql.append(" AND TO_DATE(FECH_OPER,'DD/MM/RRRR') = TO_DATE('").append(fecha).append("', 'DD/MM/RRRR')");
		db.exec(dataSource, sql.toString());
		System.out.println("actualizarOrdenBCVMenudeo----> " + sql);
	}

	// public void actualizarMesaCambioBCV(String ordeneID,String fecha,String estatus) throws Exception{
	//
	// StringBuffer sql = new StringBuffer();
	// sql.append(" UPDATE INFI_TB_236_MESA_CAMBIO SET STATUS_ENVIO = '").append(estatus).append("'");
	//
	// sql.append(" WHERE ID_OPER ='").append(ordeneID).append("'");
	// sql.append(" AND TO_DATE(FECH_OPER,'DD/MM/RRRR') = TO_DATE('").append(fecha).append("', 'DD/MM/RRRR')");
	// db.exec(dataSource, sql.toString());
	// System.out.println("actualizarOrdenBCVMenudeo----> "+sql);
	// }
	public void actualizarIntervencionBCV1(List<String> ordeneID, String fecha, String estatus, String COD_BCV) throws Exception {

		for (String ordeness : ordeneID) {

			StringBuffer sql = new StringBuffer();
			sql.append(" UPDATE INFI_TB_235_INTERVENCION SET STATUS_ENVIO = '").append(estatus).append("',");
			sql.append(" COD_BCV='").append(COD_BCV).append("'");

			sql.append(" WHERE ID_OPER ='").append(ordeness).append("'");
			sql.append(" AND TO_DATE(FECHA_VALOR,'DD/MM/RRRR') = TO_DATE('").append(fecha).append("', 'DD/MM/RRRR')");
			db.exec(dataSource, sql.toString());
			System.out.println("actualizarOrdenBCVMenudeo----> " + sql);
		}
	}

	public void actualizarIntervencionBCV(String ordeneID, String fecha, String estatus) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE INFI_TB_235_INTERVENCION SET STATUS_ENVIO = '").append(estatus).append("'");

		sql.append(" WHERE ID_OPER ='").append(ordeneID).append("'");
		sql.append(" AND TO_DATE(FECH_OPER,'DD/MM/RRRR') = TO_DATE('").append(fecha).append("', 'DD/MM/RRRR')");
		db.exec(dataSource, sql.toString());
		System.out.println("actualizarOrdenBCVMenudeo----> " + sql);
	}

	public void insertar_lectura_menudeo(String OPERACI�N, String ID_OC, String STATUS_OPER, String MTO_COMI, String CON_ESTADIS, String COD_OFI_ORI, String MTO_DIVISAS, String MTO_BOLIVARES, String TASA_CAMBIO, String NACIONALIDAD, String NRO_CED_RIF, String NOM_CLIEN, String CTA_CLIEN, String COD_DIVISAS, String EMAIL_CLIEN, String TEL_CLIEN) throws Exception {

			NOM_CLIEN = NOM_CLIEN.replaceAll("[.,]", "");
			StringBuffer sql = new StringBuffer();
			sql.append("Insert into ADM_INFI.INFI_TB_234_VC_DIVISAS " + "(ID_OPER,ID_OC,STATUS_OPER,MTO_COMI,CON_ESTADIS,COD_OFI_ORI,OPERACION, MTO_DIVISAS,MTO_BOLIVARES, TASA_CAMBIO, NACIONALIDAD, NRO_CED_RIF, " + "NOM_CLIEN, CTA_CLIEN, FECH_OPER, COD_DIVISAS, EMAIL_CLIEN, TEL_CLIEN, STATUS_ENVIO)");
			sql.append(" values (");
			sql.append(" ").append("SEC_ID_OPER.NEXTVAL").append(",");
			sql.append("").append(ID_OC).append(",");
			sql.append("'").append(STATUS_OPER).append("',");
			sql.append("").append(MTO_COMI).append(",");
			sql.append("").append(CON_ESTADIS).append(",");
			sql.append("").append(COD_OFI_ORI).append(",");
			sql.append("'").append(OPERACI�N).append("',");
			sql.append("").append(MTO_DIVISAS).append(",");
			sql.append("").append(MTO_BOLIVARES).append(",");
			sql.append("").append(TASA_CAMBIO).append(",");
			sql.append("'").append(NACIONALIDAD).append("',");
			sql.append("'").append(NRO_CED_RIF).append("',");
			sql.append("'").append(NOM_CLIEN).append("',");
			sql.append("'").append(CTA_CLIEN).append("',");
			sql.append("").append("to_char(trunc(sysdate),'DD-MM-YYYY')").append(",");
			sql.append("'").append(COD_DIVISAS).append("',");
			sql.append("'").append(EMAIL_CLIEN).append("',");
			sql.append("'").append(TEL_CLIEN).append("',");
			sql.append("'").append("0").append("')");
			db.exec(dataSource, sql.toString());
			System.out.println("insertarOrdenBCVMenudeo----> " + sql);


	}

	public void insertar_lectura_intervencion(String COD_CLIENTE, String NOMBRE_CLIENT, String FECHA_VALOR, String TIPO_OPERACION, String MTO_DIVISAS, String TASA_CAMBIO, String COD_CUENTA_DIVISAS, String COD_CUENTA_BS, String COD_MONEDA_ISO) throws Exception {
		System.out.println(" paso insertar_lectura_intervencion");

		try {

			StringBuffer sql = new StringBuffer();
			sql.append("Insert into INFI_TB_235_INTERVENCION " + "(COD_CLIENT,NOMBRE_CLIENT, FECHA_VALOR, TIPO_OPERACION, MTO_DIVISAS, TASA_CAMBIO, " + "COD_CUENTA_DIVISAS, COD_CUENTA_BS, COD_MONEDA_ISO,STATUS_ENVIO,ID_OPER)");
			sql.append(" values (");
			sql.append("'").append(COD_CLIENTE).append("',");
			sql.append("'").append(NOMBRE_CLIENT).append("',");
			sql.append("'").append(FECHA_VALOR).append("',");
			sql.append("'").append(TIPO_OPERACION).append("',");
			sql.append("'").append(MTO_DIVISAS).append("',");
			sql.append("").append(TASA_CAMBIO).append(",");
			sql.append("'").append(COD_CUENTA_DIVISAS).append("',");
			sql.append("'").append(COD_CUENTA_BS).append("',");
			sql.append("'").append(COD_MONEDA_ISO).append("',");
			// sql.append("'").append(COD_BCV).append("'),");
			sql.append("'").append("0").append("',");
			sql.append(" ").append("SEC_ID_OPER.NEXTVAL").append(")");
			db.exec(dataSource, sql.toString());
			System.out.println("insertarOrdenIntervencion----> " + sql);

		} catch (Exception e) {
			System.out.println("error al insertar el registro " + e + " -->" + COD_CLIENTE + " Nombre-->" + NOMBRE_CLIENT);
			logger.error("error al insertar el registro " + e + " -->" + COD_CLIENTE + " Nombre-->" + NOMBRE_CLIENT);

			// TODO: handle exception
		}

	}

	/**
	 * Metodo de busqueda de cruces por unidad de inversion y estatus de movimiento de cruce
	 * 
	 * @param idUnidad
	 * @param indicadorTitulo
	 * @param procesada
	 * @param status
	 * @throws Exception
	 */
	// NM26659 TTS-491 WEB SERVICE ALTO VALOR 01/04/2015
	public void listarCrucesInvalidosMenudeoSIMADI(long idUnidad, int procesada, String... status) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT OC.ORDENE_ID,OC.FECHA_VALOR,OC.MONTO_OPERACION,OC.TASA,OC.ORDENE_ID_BCV,OC.CONTRAPARTE,OC.CONTRAVALOR_BOLIVARES_CAPITAL,OC.ESTATUS,OC.TITULO_ID,OC.VALOR_NOMINAL,OC.TITULO_MTO_INT_CAIDOS,OC.PRECIO_TITULO,ORDENE_ID_OFERTA ");
		sql.append(" FROM INFI_TB_229_ORDENES_CRUCES OC WHERE OC.UNDINV_ID='").append(idUnidad).append("' ");

		sql.append(" AND OC.INDICADOR_TITULO='").append(ConstantesGenerales.STATUS_INACTIVO).append("' ");
		sql.append(" AND OC.CRUCE_PROCESADO=").append(procesada).append(" ");

		sql.append(" AND (OC.ORDENE_ID_BCV IS NULL OR OC.ORDENE_ID_BCV='') ");

		if (status.length > 0 && status[0] != null) {
			int count = 0;
			sql.append(" AND OC.ESTATUS IN (");
			for (String element : status) {
				if (count > 0) {
					sql.append(",");
				}
				sql.append("'" + element + "'");
				++count;
			}

			sql.append(" ) ");
		}

		sql.append(" ORDER BY OC.ORDENE_ID ASC ");
		// System.out.println("listarCrucesPorUunidadInversion --> " + sql.toString());
		dataSet = db.get(dataSource, sql.toString());
	}

	public void actualizarMesaCambioBCV(String ordeneID, String fecha, String estatus) throws Exception {

		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE INFI_TB_236_MESA_CAMBIO SET STATUS_ENVIO = '").append(estatus).append("'");

		sql.append(" WHERE ID_OPER ='").append(ordeneID).append("'");
		sql.append(" AND TO_DATE(FECH_OPER,'DD/MM/RRRR') = TO_DATE('").append(fecha).append("', 'DD/MM/RRRR')");
		db.exec(dataSource, sql.toString());
		System.out.println("actualizarOrdenBCVMenudeo----> " + sql);
	}

	public void insertar_lectura_mesaCambio(String OPERACION, String RIF_CLIENTE, String NOM_CLIEN, String COD_DIVISAS, String MTO_DIVISAS, String TASA_CAMBIO, String COD_INS_BANCO, String CTA_CONVENIO, String CTA_CLIEN, String INSTRUMENTO) throws Exception {
		System.out.println(" paso insertarOrdenBCVMenudeo");

		try {

			StringBuffer sql = new StringBuffer();
			sql.append("Insert into INFI_TB_236_MESA_CAMBIO " + "(ID_OPER,OPERACION, RIF_CLIENTE, NOM_CLIEN, COD_DIVISAS, MTO_DIVISAS, TASA_CAMBIO, COD_INS_BANCO, CTA_CONVENIO, CTA_CLIEN,FECH_OPER,STATUS_ENVIO,INSTRUMENTO)");
			sql.append(" values (");
			sql.append(" ").append("SEC_ID_OPER.NEXTVAL").append(",");
			sql.append("'").append(OPERACION).append("',");
			sql.append("'").append(RIF_CLIENTE).append("',");
			sql.append("'").append(NOM_CLIEN).append("',");
			sql.append("'").append(COD_DIVISAS).append("',");
			sql.append("").append(MTO_DIVISAS).append(",");
			sql.append("").append(TASA_CAMBIO).append(",");
			sql.append("'").append(COD_INS_BANCO).append("',");
			sql.append("'").append(CTA_CONVENIO).append("',");
			sql.append("'").append(CTA_CLIEN).append("',");
			sql.append("").append("trunc(SYSDATE)").append(",");
			sql.append("'").append("0").append("',");
			sql.append("'").append(INSTRUMENTO).append("')");

			db.exec(dataSource, sql.toString());
			System.out.println("insertarOrdenBCVMesaCambio----> " + sql);

		} catch (Exception e) {
			System.out.println("error al insertar el registro " + e + " -->" + RIF_CLIENTE + " Nombre-->" + NOM_CLIEN);
			logger.error("error al insertar el registro " + e + " -->" + RIF_CLIENTE + " Nombre-->" + NOM_CLIEN);

			// TODO: handle exception
		}

	}

	public void insertar_intervencion_operacion(String COD_BANCO, String FECHA_VALOR, String TIPO_OPERACION, double MTO_DIVISAS, String COD_MONEDA_ISO, int COD_BCV, double TASA_CAMBIO) throws Exception {
		System.out.println(" paso insertar_intervencion_operacion");

		try {

			StringBuffer sql = new StringBuffer();
			sql.append("Insert into INFI_TB_235_INTERVENCION " + "(COD_BANCO,FECHA_VALOR, TIPO_OPERACION, MTO_DIVISAS,TASA_CAMBIO, " + "COD_MONEDA_ISO, COD_BCV,ID_OPER)");
			sql.append(" values (");
			sql.append("'").append(COD_BANCO).append("',");
			sql.append("'").append(FECHA_VALOR).append("',");
			sql.append("'").append(TIPO_OPERACION).append("',");
			sql.append("").append(MTO_DIVISAS).append(",");
			sql.append("").append(TASA_CAMBIO).append(",");
			sql.append("'").append(COD_MONEDA_ISO).append("',");
			sql.append("").append(COD_BCV).append(",");
			sql.append(" ").append("SEC_ID_OPER.NEXTVAL").append(")");
			db.exec(dataSource, sql.toString());
			System.out.println("insertar_intervencion_operacion----> " + sql);

		} catch (Exception e) {
			System.out.println("error al insertar el registro " + e + " -->" + COD_BANCO + " Nombre-->" + COD_BCV);
			logger.error("error al insertar el registro " + e + " -->" + COD_BANCO + " Nombre-->" + COD_BCV);

			// TODO: handle exception
		}

	}
	
	public void listarOrdenesAnuladasMenudeo(String fecha, String estatusEnvie, int paginaAMostrar, int registroPorPagina){

		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT ID_OPERACION, ID_RECHAZO, MONTO, PRODUCTO, TIPO_NOTIFICACION, CEDULA_RIF, FECHA, DECODE(ESTATUS,'0','NO ENVIADO','1','ENVIADO') as ESTATUS, ID ");
		sql.append("FROM INFI_TB_239_VC_ANULADASN ");
		sql.append("WHERE ");
		sql.append("FECHA = TO_DATE('").append(fecha).append("', 'DD/MM/YYYY') ");
		
		if (estatusEnvie.equals(ConstantesGenerales.ENVIO_MENUDEO) || estatusEnvie.equals(ConstantesGenerales.ENVIO_MENUDEO_FALTANTES) || estatusEnvie.equals(ConstantesGenerales.ENVIO_MENUDEO_MANUAL) || estatusEnvie.equals(ConstantesGenerales.ENVIO_MENUDEO_ANULADA) || estatusEnvie.equals(ConstantesGenerales.ENVIO_MENUDEO_RECHAZADA)) {
			sql.append("AND ESTATUS = '").append(estatusEnvie).append("' ");
		}
		
		sql.append("ORDER BY ID");

		try {
			System.out.println("listarOrdenesAnuladasMenudeo : " + sql);
			dataSet = obtenerDataSetPaginado(sql.toString(), paginaAMostrar, registroPorPagina);
			
		} catch (Exception e) {
			System.out.println("OrdenesCrucesDAO: listarOrdenesAnuladasMenudeo() " + e);
			
		}
	}
	
	public boolean verificarOperacionConciliacionMenudeo(String id){

		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT ID_RECHAZO");
		sql.append("FROM INFI_TB_239_VC_ANULADASN ");
		sql.append("WHERE ");
		sql.append("ID_RECHAZO ='").append(id).append("'");
//		sql.append("FECHA = TO_DATE('").append(fecha).append("', 'DD/MM/YYYY') ");
		
		if (this.dataSet.count() > 0)
			return false;
		else
			return true
			;
	}
	

}

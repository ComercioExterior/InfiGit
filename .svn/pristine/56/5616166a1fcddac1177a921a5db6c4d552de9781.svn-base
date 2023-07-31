package com.bdv.infi.dao;

import java.util.Date;

import javax.sql.DataSource;

import megasoft.DataSet;
import megasoft.db;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.util.Utilitario;

/**
 * @author Daniel Vasquez
 * */
public class SecsDao extends GenericoDAO {

	
	public SecsDao(DataSource ds) {
		super(ds);
	}

	@Override
	public Object moveNext() throws Exception {
		return null;
	}
	
	
	/**
	 * Lista los detalles de t&iacute;tulo en la tabla secs condicionado
	 *  por secid, ordenado ascendentemente
	 * @param String idTitulo este referencia a TITULO_ID.
	 * */
	public void listarDetalleSecs(String idTitulo)throws Exception{
		String strSQL = "SELECT * FROM secs  where secid = '"+idTitulo+"' order by intstrtdte asc";
		dataSet = db.get(dataSource, strSQL);
		System.out.println("listarDetalleSecs "+strSQL);
	}
	
	/**
	 * Lista los detalles de t&iacute;tulo en la tabla secs condicionado
	 *  por secid, ordenado ascendentemente
	 * @param String idTitulo este referencia a TITULO_ID.
	 * */
	public void listarCronograma(String idTitulo, String fechaDesde, String fechaHasta)throws Exception{
		StringBuilder strSQL = new StringBuilder(); 
		strSQL.append("SELECT to_char(IPAYDATE,'dd-mm-yyyy'), prinamt_8, prinpayamt_8, titulo_moneda_den, titulo_moneda_neg ");
		strSQL.append("FROM secs  inner join INFI_TB_100_TITULOS tit on trim(secs.secid) = trim(tit.titulo_id) ");
		strSQL.append("where secid = '").append(idTitulo).append("' ");
		strSQL.append("and to_char(IPAYDATE,'yyyy-mm-dd') >= '").append(fechaDesde).append("' ");		
		strSQL.append("and to_char(IPAYDATE,'yyyy-mm-dd') <= '").append(fechaHasta).append("' ");
		strSQL.append("order by IPAYDATE asc");
		
		dataSet = db.get(dataSource, strSQL.toString());		
	}
	
	/**
	 * Obtiene la fecha de útlimo pago del cupón y de la amortización 
	 * @param idTitulo id del título que se desea consultar
	 * @throws Exception en caso de error
	 */
	public Date obtenerFechaAmortizacionCupon(String idTitulo) throws Exception{
		StringBuilder sql = new StringBuilder(); 
		sql.append("SELECT to_char(IPAYDATE,'").append(ConstantesGenerales.FORMATO_FECHA).append("') as fecha").append(" FROM SECS WHERE SECID= '");
		sql.append(idTitulo).append("'");
		sql.append(" AND IPAYDATE <= TO_DATE(SYSDATE, '").append(ConstantesGenerales.FORMATO_FECHA_SYSDATE).append("')");
		sql.append(" ORDER BY IPAYDATE DESC");
		DataSet dataSet = db.get(dataSource, sql.toString());
		Date fecha = null;
		if (dataSet.next()){
			fecha = Utilitario.StringToDate(dataSet.getValue("fecha"), ConstantesGenerales.FORMATO_FECHA);
		}else{
			//Si el primer cupón no ha sido pagado busca la primera fecha
			sql = new StringBuilder();
			sql.append("SELECT to_char(intstrtdte,'").append(ConstantesGenerales.FORMATO_FECHA).append("') as fecha").append(" FROM SECS WHERE SECID= '");
			sql.append(idTitulo).append("'");
			sql.append(" ORDER BY intstrtdte ");
			dataSet = db.get(dataSource, sql.toString());			
			if(dataSet.next()){
				fecha = Utilitario.StringToDate(dataSet.getValue("fecha"), ConstantesGenerales.FORMATO_FECHA);
			}
			System.out.println("obtenerFechaAmortizacionCupon "+sql);
		}
		return fecha;
	}
}
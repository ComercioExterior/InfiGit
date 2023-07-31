/**
 * 
 */
package com.bdv.infi.dao;


import java.math.BigDecimal;
import java.util.Date;

import javax.sql.DataSource;

import megasoft.DataSet;
import megasoft.Logger;
import megasoft.db;

/**
 * @author eel
 *
 */
	
public class PagoCuponesDao extends GenericoDAO{	
	public PagoCuponesDao(DataSource ds) {
		super(ds);
	}
	public Object moveNext() throws Exception {
		return null;
	}

	
	/**Lista los títulos que se vencen según las fechas indicadas.
	 * @param fechaInicio fecha de inicio para buscar los títulos a vencerse desde esa fecha
	 * @param fechaFin fecha fin para buscar los títulos a vencerse hasta esa fecha
	 * @param idCliente id del cliente que se desea consultar. Si se envia null consulta todos los clientes
	 */	
	public void listarCupones(Date fechaInicio, Date fechaFin, String idCliente, String[] titulos) throws Exception{
		StringBuffer sql=new StringBuffer();
		sql.append("select distinct infi_tb_702_titulos_cierre.fecha_cierre, infi_tb_701_titulos.TITULO_FE_ULT_PAGO_CUPON,");
		sql.append(" infi_tb_200_tipo_personas.TIPPER_ID,infi_tb_200_tipo_personas.TIPPER_NOMBRE,");
		sql.append(" infi_tb_100_titulos.titulo_descripcion,infi_tb_201_ctes.CLIENT_NOMBRE,");
		sql.append(" infi_tb_100_titulos.intccy as moneda_pago,infi_tb_100_titulos.titulo_moneda_den,");
		sql.append(" infi_tb_701_titulos.client_id,infi_tb_701_titulos.titulo_id,seq,secid as titulo_id_secs,");
		sql.append(" infi_tb_702_titulos_cierre.titcus_cantidad,intenddte as fecha_fin_pago_cupon,");
		sql.append(" intstrtdte as fecha_inicio_pago_cupon,INTSTRTDTE as fecha_pago_guia, intrate_8 as intereses_cupones,");
		sql.append(" PRINAMT_8 pct_amortizacion,PRINPAYAMT_8 amortizable,");		
		sql.append(" secs.basis as base_calculo,(secs.INTENDDTE-secs.INTSTRTDTE)as dias_diferencia,  ");
		sql.append(" (select count(secid) from secs where trim(secid) = infi_tb_701_titulos.titulo_id  ");
		sql.append(" and intstrtdte > ").append(this.formatearFechaBD(fechaInicio)).append(" ) as cuenta_cupones_proximos ");
		sql.append(" from secs,infi_tb_701_titulos,infi_tb_100_titulos,infi_tb_201_ctes,infi_tb_200_tipo_personas,");
		sql.append(" infi_tb_702_titulos_cierre where trim(secs.SECID)=trim(infi_tb_701_titulos.TITULO_ID) and ");
		sql.append(" trim(infi_tb_701_titulos.TITULO_ID)=trim(infi_tb_100_titulos.TITULO_ID) and ");
		sql.append(" trim(infi_tb_702_titulos_cierre.titulo_id)=infi_tb_701_titulos.titulo_id ");
		sql.append(" and infi_tb_702_titulos_cierre.client_id=infi_tb_701_titulos.client_id and ");
		sql.append(" infi_tb_701_titulos.CLIENT_ID=infi_tb_201_ctes.CLIENT_ID ");
		sql.append(" and infi_tb_201_ctes.TIPPER_ID=infi_tb_200_tipo_personas.TIPPER_ID and ");
		sql.append(" intenddte between ").append(this.formatearFechaBD(fechaInicio)).append(" and ").append(this.formatearFechaBD(fechaFin));
		sql.append(" and infi_tb_702_titulos_cierre.titcus_cantidad > 0 ");
		sql.append(" and trunc(infi_tb_701_titulos.titulo_fe_ult_pago_cupon) <= intstrtdte and ");
		sql.append(" infi_tb_702_titulos_cierre.fecha_cierre = ");
		sql.append(" (select max(infi_tb_702_titulos_cierre.fecha_cierre) as fecha_cierre from ");
		sql.append(" infi_tb_702_titulos_cierre where  infi_tb_702_titulos_cierre.client_id=infi_tb_701_titulos.client_id ");
		sql.append(" and infi_tb_702_titulos_cierre.titulo_id=infi_tb_701_titulos.titulo_id and ");
		sql.append(" trunc(infi_tb_702_titulos_cierre.fecha_cierre) <= ").append(this.formatearFechaBD(fechaFin)).append(")"); 		
		
		if (idCliente != null && !idCliente.equals("")){
			sql.append(" and infi_tb_701_titulos.client_id=").append(idCliente);
		}
		
		if (titulos != null){
			StringBuffer titulosFiltro = new StringBuffer();
			//Recorre los títulos para armar un solo filtro
			for (int i=0; i < titulos.length; i++){				
				titulosFiltro.append("'").append(titulos[i].trim()).append("'");
				if (i != titulos.length-1){
					titulosFiltro.append(",");
				}
			}
			sql.append(" and infi_tb_701_titulos.titulo_id in(").append(titulosFiltro.toString()).append(")");
		}
		
		sql.append(" order by client_id,titulo_id,intenddte");
		dataSet = db.get(dataSource, sql.toString());		
	}
	
	
	/**Lista los títulos que están próximos a vencerse. 
	 * Toma como fecha de inicio la fecha de la consulta.
	 * @throws Lanza una excepción si hay un error en la consulta 
	 * */
	public void listarTitulosPorVencerse() throws Exception{
		StringBuffer sql=new StringBuffer();
		String diaActual = this.formatearFechaBD(new Date());
		sql.append("select a.secid titulo_id, a.intstrtdte fecha_inicio_pago_cupones, a.intenddte  fecha_fin_pago_cupones, count(b.client_id) cantidad_clientes from secs a, infi_tb_701_titulos b where a.intenddte"); 
		sql.append(" between ").append(diaActual).append("and ").append(diaActual).append("+90 ");
		sql.append("and trim(a.secid) = b.titulo_id ");
		sql.append("group by secid,intstrtdte,intenddte ");
		sql.append("order by a.intenddte,a.secid");
		System.out.println("listarTitulosPorVencerse--->"+sql);
		this.dataSet = db.get(this.dataSource, sql.toString());
	}
	
	/**Lista los títulos que están vencidos y que deben pagarse. 
	 * Toma como fecha de inicio la última fecha de pago más antigua más un día.
	 * @throws Lanza una excepción si hay un error en la consulta 
	 * */
	public void listarTitulosVencidos() throws Exception{
		StringBuffer sql=new StringBuffer();
		String diaActual = this.formatearFechaBD(new Date());		
		sql.append("select trim(a.secid) as secid,a.intstrtdte fecha_inicio_pago_cupones,a.intenddte  fecha_fin_pago_cupones, count(b.client_id)"); 
		sql.append("cantidad_clientes, intrate_8 as intereses_cupones, PRINAMT_8 intereses_amortizacion,PRINPAYAMT_8 amortizable from secs a, infi_tb_701_titulos b ");
		sql.append("where intstrtdte >= trunc(titulo_fe_ult_pago_cupon) and intenddte <= ").append(diaActual); 
		sql.append(" and trim(a.secid) = trim(b.titulo_id) and b.TITCUS_CANTIDAD>0");
		sql.append("group by secid,intstrtdte,intenddte,intrate_8,PRINAMT_8,PRINPAYAMT_8 ");
		sql.append("order by a.intenddte,a.secid");
		this.dataSet = db.get(this.dataSource, sql.toString());
	}
	
	/**
	 * Obtiene el porcentaje total amortizado hasta la fecha para un título determinado
	 * @param idTitulo id del título
	 * @param fechaInicio fecha de inicio del cupón
	 * @return Porcentaje total amortizado hasta la fecha
	 */
	public double obtenerAmortizacionHistorica(String idTitulo, Date fechaInicio) throws Exception{
		double totalAmorizado = 0; 
		StringBuffer sql=new StringBuffer();
		sql.append("select sum(prinpayamt_8)* -1 amortizacion from secs where secid='");
		sql.append(idTitulo).append("' and intstrtdte < ");
		sql.append(this.formatearFechaBD(fechaInicio));
		DataSet amortizacion = db.get(this.dataSource, sql.toString());
		if (amortizacion.count()>0){
			amortizacion.first();
			amortizacion.next();
			Logger.info("","Id del título para la búsqueda acumulada de amortización: " + idTitulo);
			if (amortizacion.getValue("amortizacion") != null && !amortizacion.getValue("amortizacion").equals("")){
				totalAmorizado = Double.parseDouble(amortizacion.getValue("amortizacion"));
				Logger.info(""," Total encontrado para amortización: " + totalAmorizado);
			}			
		}
		return totalAmorizado;
	}	
}
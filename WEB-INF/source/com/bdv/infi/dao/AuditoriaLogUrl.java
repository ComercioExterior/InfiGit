package com.bdv.infi.dao;

import javax.sql.DataSource;

import megasoft.db;

public class AuditoriaLogUrl extends com.bdv.infi.dao.GenericoDAO {

	public AuditoriaLogUrl(Transaccion transaccion) throws Exception {
		super(transaccion);
	}
	
	public AuditoriaLogUrl(DataSource ds) throws Exception {
		super(ds);
	}
	
	/**Metodo que lista toda la data de in registro especifico de la tabla
	 * @param idConfig
	 * @throws Exception
	 */
	public void listar(String usuario, String url, String fechaDesde, String fechaHasta, String horaDesde, String horaHasta) throws Exception{
		StringBuffer sql = new StringBuffer();
		
		sql.append("select l.action,l.fecha,l.time,SUBSTR(l.time,1,5) as hora,l.ip,l.usuario,m.userid,l.parameters as parametros from log_url l, msc_user m where l.usuario=m.msc_user_id ");
		
		if(usuario!=null){
			sql.append(" and l.usuario='").append(usuario).append("'");
		}
		if(url!=null){
			sql.append(" and upper(l.action) like upper ('%").append(url).append("%')");
		}
		if(fechaDesde!=null&&fechaHasta!=null){
			sql.append(" and l.fecha BETWEEN to_date('").append(fechaDesde).append("','dd-mm-yyyy') and to_date('").append(fechaHasta).append("','dd-mm-yyyy')");
		}
		if(horaDesde!=null&&horaHasta!=null){
			sql.append(" and substr(l.time,1,5)>= '").append(horaDesde).append("'");
			sql.append(" and substr(l.time,1,5)<= '").append(horaHasta).append("'");
		}
		sql.append(" order by l.fecha desc,l.time desc");
		dataSet = db.get(dataSource, sql.toString());
	}
	
	/**Metodo que lista toda la data de in registro especifico de la tabla
	 * @param idConfig
	 * @throws Exception
	 */
	public void listarparametros(String usuario, String url, String fecha, String hora, String ip) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select action,fecha,time,SUBSTR(time,1,5) as hora,ip,usuario,userid,parameters as parametros from log_url, msc_user where usuario=msc_user_id");
		sql.append(" and action='").append(url).append("'");
		if(hora!=null){
			sql.append(" and time='").append(hora).append("'");
		}
		if(fecha!=null){
			sql.append(" and fecha=to_date('").append(fecha).append("','dd-mm-yyyy')");
		}
		if(ip!=null){
			sql.append(" and ip='").append(ip).append("'");
		}
		if(usuario!=null){
			sql.append(" and usuario='").append(usuario).append("'");
		}
		sql.append(" order by fecha desc,time desc");
		dataSet = db.get(dataSource, sql.toString());
	}
	
	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}

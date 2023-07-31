package com.bdv.infi.dao;

import javax.sql.DataSource;
import megasoft.db;
import com.bdv.infi.data.UrlLogConfig;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

public class AuditoriaUrlLogConfigDAO extends com.bdv.infi.dao.GenericoDAO {

	public AuditoriaUrlLogConfigDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
	}
	
	public AuditoriaUrlLogConfigDAO(DataSource ds) throws Exception {
		super(ds);
	}
	
	/**Metodo que lista toda la data de in registro especifico de la tabla
	 * @param idConfig
	 * @throws Exception
	 */
	public void listarPorId( String idConfig) throws Exception{
		StringBuffer sql = new StringBuffer();
		
		sql.append("select * from url_log_config where id_config='").append(idConfig).append("'");
		
		dataSet = db.get(dataSource, sql.toString());
	}
	
	
	/**Metodo que lista toda la data contenida en la tabla
	 * @param url
	 * @param enable
	 * @throws Exception
	 */
	public void listar(String url, String enable) throws Exception{
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		
		sql.append("select id_config, url, case when enable="+ConstantesGenerales.VERDADERO+" then 'Si' when enable="+ConstantesGenerales.FALSO+" then 'No' end enable from url_log_config where 1=1 ");
		
		if(url!=null){
			filtro.append(" and upper(url) like upper('%").append(url).append("%')");
		}
		if(enable!=null){
			filtro.append(" and enable=").append(enable);
		}
		sql.append(filtro);
		sql.append(" ORDER BY url");
		dataSet = db.get(dataSource, sql.toString());

	}
	
	/** Metodo que inserta un registro en la tabla
	 * @param urlLogConfig
	 * @return
	 * @throws Exception
	 */
	public String insertar(UrlLogConfig urlLogConfig) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		
		sql.append("insert into url_log_config ( id_config,url,enable) values (");
		String id = dbGetSequence(dataSource, ConstantesGenerales.SECUENCIA_URL_LOG_CONFIG);
	
		filtro.append(id).append(",");
		filtro.append("'").append(urlLogConfig.getUrl()).append("',");
		filtro.append(urlLogConfig.getEnable()).append(")");
		
		sql.append(filtro);			
		return(sql.toString());
	}
	
	/**Metodo que actuliza un registro en la tabla
	 * @param urlLogConfig
	 * @return
	 * @throws Exception
	 */
	public String modificar(UrlLogConfig urlLogConfig) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		
		sql.append("update url_log_config set ");
		
		filtro.append(" url='").append(urlLogConfig.getUrl()).append("',");
		filtro.append(" enable=").append(urlLogConfig.getEnable());
		filtro.append(" where id_config='").append(urlLogConfig.getId_config()).append("'");
		sql.append(filtro);			
		return(sql.toString());
	}
	
	/** Metodo que elimina un resgitro de la tabla
	 * @param urlLogConfig
	 * @return
	 * @throws Exception
	 */
	public String eliminar(UrlLogConfig urlLogConfig) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("delete from url_log_config where");
		sql.append(" id_config='").append(urlLogConfig.getId_config()).append("'");
		return(sql.toString());
	}

	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}

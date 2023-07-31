package com.bdv.infi.dao;

import javax.sql.DataSource;
import megasoft.db;
import com.bdv.infi.data.UrlParametersLogConfig;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

public class AuditoriaUrlParametersLogConfig extends com.bdv.infi.dao.GenericoDAO {

	public AuditoriaUrlParametersLogConfig(Transaccion transaccion) throws Exception {
		super(transaccion);
	}
	
	public AuditoriaUrlParametersLogConfig(DataSource ds) throws Exception {
		super(ds);
	}
	
	/**Metodo que lista toda la data contenida en la tabla asociada a un id_config
	 * @param idConfig
	 * @throws Exception
	 */
	public void listarPorId( String idConfig) throws Exception{
		StringBuffer sql = new StringBuffer();
		
		sql.append("select * from URL_PARAMETERS_LOG_CONFIG where id_config='").append(idConfig).append("'");
		sql.append(" ORDER BY name");
		dataSet = db.get(dataSource, sql.toString());
	}
	
	/**Metodo que lista toda la data contenida en la tabla
	 * @param name
	 * @throws Exception
	 */
	public void listarPorNombre(String name) throws Exception{
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		
		sql.append("select * from URL_PARAMETERS_LOG_CONFIG where 1=1");
		
		if(name!=null){
			filtro.append(" and name='").append(name).append("'");
		}
		sql.append(filtro);
		sql.append(" ORDER BY name");
		dataSet = db.get(dataSource, sql.toString());

	}
	
	public String insertar(UrlParametersLogConfig urlParametersLogConfig) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("insert into URL_PARAMETERS_LOG_CONFIG ( name,id_config) values (");
		sql.append("'").append(urlParametersLogConfig.getParametro()).append("',");
		sql.append(urlParametersLogConfig.getId_config()).append(")");
		
		return(sql.toString());
	}
	
	public String modificar(UrlParametersLogConfig urlParametersLogConfig) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		
		sql.append("update URL_PARAMETERS_LOG_CONFIG set ");
		
		filtro.append(" name='").append(urlParametersLogConfig.getName()).append("'");
		filtro.append(" where id_config=").append(urlParametersLogConfig.getId_config());
		filtro.append(" and name='").append(urlParametersLogConfig.getParametro()).append("'");
		sql.append(filtro);			
		return(sql.toString());
	}
	
	public String eliminar(UrlParametersLogConfig urlParametersLogConfig) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("delete from URL_PARAMETERS_LOG_CONFIG where");
		sql.append(" id_config=").append(urlParametersLogConfig.getId_config()).append("");
		sql.append(" and name='").append(urlParametersLogConfig.getParametro()).append("'");
		return(sql.toString());
	}
	
	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}

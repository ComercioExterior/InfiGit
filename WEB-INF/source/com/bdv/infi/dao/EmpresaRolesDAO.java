package com.bdv.infi.dao;

import javax.sql.DataSource;

import megasoft.DataSet;
import megasoft.db;

import com.bdv.infi.data.EmpresaDefinicion;
import com.bdv.infi.data.EmpresaRoles;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

/** 
 * Clase usada para la modificaci&oacute;n, inserci&oacute;n y listado de los Roles de la Empresa
 */
public class EmpresaRolesDAO extends com.bdv.infi.dao.GenericoDAO {

	public EmpresaRolesDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
	}
	
	public EmpresaRolesDAO(DataSource ds) throws Exception {
		super(ds);
	}
	
	public void listar() throws Exception{
		StringBuffer sql = new StringBuffer();
		
		sql.append("select roles_id,roles_descripcion from infi_tb_017_roles");
		
		dataSet = db.get(dataSource, sql.toString());
	}
	
	
	/**Busca los roles que cumplan con la condici&oacute;n solicitada. 
	 */
	public void listar(String descripcion, String status) throws Exception{
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		
		sql.append("select roles_id,roles_descripcion,case when roles_in_tomador=").append(ConstantesGenerales.VERDADERO).append(" then 'Si' when roles_in_tomador=").append(ConstantesGenerales.FALSO).append(" then 'No' end roles_in_tomador, case when roles_in_colocador=").append(ConstantesGenerales.VERDADERO).append(" then 'Si' when roles_in_colocador=").append(ConstantesGenerales.FALSO).append(" then 'No' end roles_in_colocador, case when roles_in_recomprador=").append(ConstantesGenerales.VERDADERO).append(" then 'Si' when roles_in_recomprador=").append(ConstantesGenerales.FALSO).append(" then 'No' end roles_in_recomprador,case when roles_status=").append(ConstantesGenerales.VERDADERO).append(" then 'Activo' when roles_status=").append(ConstantesGenerales.FALSO).append(" then 'Inactivo' end roles_status from infi_tb_017_roles where 1=1");
		
		if(status!=null){
			filtro.append(" and roles_status=").append(status);
		}
		if(descripcion!=null){
			filtro.append(" and upper(roles_descripcion) like upper('%").append(descripcion).append("%')");
		}
		sql.append(filtro);
		sql.append(" ORDER BY roles_status");
		dataSet = db.get(dataSource, sql.toString());

	}
	
	public void listar(String roles_id) throws Exception{
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		
		sql.append("select roles_id,roles_descripcion,roles_in_tomador,roles_in_colocador,roles_in_recomprador,roles_status from infi_tb_017_roles where 1=1");
		
		if(roles_id!=null){
			filtro.append(" and roles_id='").append(roles_id).append("'");
		}	
		sql.append(filtro);
						
		dataSet = db.get(dataSource, sql.toString());
	}
	
	public String insertar(EmpresaRoles empresaRoles) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		
		sql.append("insert into infi_tb_017_roles ( roles_id,roles_descripcion,roles_in_tomador,roles_in_colocador,roles_in_recomprador,roles_status) values (");
		String idRol = dbGetSequence(dataSource, ConstantesGenerales.SECUENCIA_ROLES);
		
		filtro.append("'").append(idRol).append("',");
		filtro.append("'").append(empresaRoles.getRoles_descripcion()).append("',");
		filtro.append(empresaRoles.getRoles_in_tomador()).append(",");
		filtro.append(empresaRoles.getRoles_in_colocador()).append(",");
		filtro.append(empresaRoles.getRoles_in_recomprador()).append(",");
		filtro.append(empresaRoles.getRoles_status()).append(")");
		
		sql.append(filtro);			
		return(sql.toString());
	}
	
	public String modificar(EmpresaRoles empresaRoles) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		
		sql.append("update infi_tb_017_roles set ");
		
		filtro.append(" roles_descripcion='").append(empresaRoles.getRoles_descripcion()).append("',");
		filtro.append(" roles_in_tomador=").append(empresaRoles.getRoles_in_tomador()).append(",");
		filtro.append(" roles_in_colocador=").append(empresaRoles.getRoles_in_colocador()).append(",");
		filtro.append(" roles_in_recomprador=").append(empresaRoles.getRoles_in_recomprador()).append(",");
		filtro.append(" roles_status=").append(empresaRoles.getRoles_status());
		filtro.append(" where roles_id='").append(empresaRoles.getRoles_id()).append("'");
		sql.append(filtro);			
		return(sql.toString());
	}
	
	public String eliminar(EmpresaRoles empresaRoles) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		
		sql.append("delete from infi_tb_017_roles where");
		
		filtro.append(" roles_id='").append(empresaRoles.getRoles_id()).append("'");
		sql.append(filtro);			
		return(sql.toString());
	}
	
	public void verificar(EmpresaRoles empresaRoles) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select roles_id from INFI_TB_109_UI_EMPRESAS where");
		sql.append(" roles_id='").append(empresaRoles.getRoles_id()).append("'");

		dataSet =db.get(dataSource,sql.toString());
	}
	
	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**Metodo que verifica si existe un registro con el mismo rol descripcion en base de datos
	*  @param String valorCampo
	*  @throws Exception lanza una excepci&ooacute;n si hay un error en la operaci&oacute;n
	*/
	public  boolean verificarRolDescripcionExiste(String valorCampo) throws Exception{
		boolean encontro=false;
		StringBuffer sb=new StringBuffer();
		sb.append("select * from ");
		sb.append("INFI_TB_017_ROLES");
		sb.append(" where initCap(");
		sb.append("ROLES_DESCRIPCION");
		sb.append(") = initCap('");
		sb.append(valorCampo);
		sb.append("')");

		DataSet ds=db.get(dataSource,sb.toString());
		if (ds.count()>0)
			encontro=true;
		
		return encontro;
	}
}
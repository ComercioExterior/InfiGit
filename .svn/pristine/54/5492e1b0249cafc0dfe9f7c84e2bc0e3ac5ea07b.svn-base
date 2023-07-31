package com.bdv.infi.dao;

import javax.sql.DataSource;
import megasoft.AppProperties;
import megasoft.db;

public class UsuarioSeguridadDAO extends com.bdv.infi.dao.GenericoDAO {

	public UsuarioSeguridadDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
	}
	
	public UsuarioSeguridadDAO(DataSource ds) throws Exception {
		super(ds);
	}
	public void listar(String nick, String nombre, String apellido) throws Exception{
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		sql.append("select msc_user_id, userid, fullname from msc_user where 1=1");
		
		if(nick!=null){
			filtro.append(" and upper(userid) like upper('%").append(nick).append("%')");
		}
		if(nombre!=null){
			filtro.append(" and upper(fullname) like upper('%").append(nombre).append("%')");
		}		
		if(apellido!=null){
			filtro.append(" and upper(fullname) like upper('%").append(apellido).append("%')");
		}

		sql.append(filtro);
		sql.append(" ORDER BY userid");
		dataSet = db.get(dataSource, sql.toString());
		System.out.println("listar "+sql);

	}
/**
 * Valida que el usuario exista en SEPA y este asociado a la aplicacion INFI
 * @param usuarioNM
 * @return Boolean
 * @throws Exception
 */
	public Boolean esUsuarioValidoSepa(String usuarioNM)throws Exception{
		
		StringBuffer sql = new StringBuffer();
		StringBuffer sqlUsuario = new StringBuffer();
		boolean esValido = false;
		
		//Se busca el id de la aplicacion INFI
		sql.append("select id_application from MSC_APPLICATIONS where siglas_applic='").append(AppProperties.getProperty("app-name")).append("'");
		
		//Se ejecuta la transaccion
		
		try {
			dataSet = db.get(dataSource,sql.toString());
			dataSet.next();
		} catch (Exception e) {
			
			throw new Exception("Error intentando buscar el id de la aplicacion INFI en SEPA...");
		}
		
		
		//Se busca el usuario en sepa por el NM y el Id de la Aplicacion INFI
		sqlUsuario.append("select * from MSC_USER ");
		sqlUsuario.append("left join MSC_ROLE_USER on MSC_ROLE_USER.msc_user_id=MSC_USER.msc_user_id ");
		sqlUsuario.append("left join MSC_ROLE on MSC_ROLE.msc_role_id=MSC_ROLE_USER.msc_role_id ");
		sqlUsuario.append("where userid='");
		sqlUsuario.append(usuarioNM);
		sqlUsuario.append("' and id_application=");
		sqlUsuario.append(Integer.parseInt(dataSet.getValue("id_application")));
		
		dataSet = db.get(dataSource, sqlUsuario.toString());
		
		if(dataSet.count()>0)
			esValido = true;
		
			//Se retorna un valor booleano si existe o no el NM consultado
			return esValido;
	}//FIN metodo
	
	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Lista el id por un NM especificado
	 * @param nombreUsuario
	 * @return int
	 * @throws Exception
	 */
	public int listarId (String nombreUsuario)throws Exception{
		
		 StringBuffer sqlIdUsuario = new StringBuffer();
	     sqlIdUsuario.append("select msc_user_id from msc_user where userid='").append(nombreUsuario).append("'");
	     
	     dataSet = db.get(dataSource,sqlIdUsuario.toString());
	     dataSet.next();	     
	     int idUsuario = Integer.parseInt(dataSet.getValue("msc_user_id"));
	     
	     //Retornamos el id de usuario
	     return idUsuario;
	}
}

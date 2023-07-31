package com.bdv.infi.dao;

import javax.sql.DataSource;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import megasoft.db;

public class UsuarioDAO extends com.bdv.infi.dao.GenericoDAO {

	public UsuarioDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
	}
	
	public UsuarioDAO(DataSource ds) throws Exception {
		super(ds);
	}
	
	/**
	 * Se ingresa al usuario relacionado con un determindado Blotter
	 * @param
	 *  idUsuario, idBlotter
	 * */
	public void insertarUsuarioBlotter(String idUsuario,String idBlotter)throws Exception{
		String sql="insert into infi_tb_104_bloter_usuarios(bloter_id,userid) values("+
					"'"+idBlotter+"', "+
					"'"+idUsuario+"')";
		db.exec(dataSource,sql);
	}
	
	public Object moveNext() throws Exception {
		return null;
	}
	
	/**Busca el blotter asociado al usuario. Si no tiene ninguno asociado se envia el blotter por defecto o 
	 * blotter de red*/
	public String listarUsuarioBlotter(String idUsuario) throws Exception{
		String blotter = "";
		if (idUsuario ==null){
			throw new Exception("Error, id de usuario nulo");
		}
		StringBuffer sql = new StringBuffer();		
		sql.append("select bloter_id from infi_tb_104_bloter_usuarios where userid = '").append(idUsuario).append("'");
		System.out.println("listarUsuarioBlotter : " + sql.toString());
		dataSet = db.get(dataSource,sql.toString());
		if (dataSet.count()==0){
			//Se busca el blotter por defecto
			BlotterDAO blotterDao = new BlotterDAO(this.dataSource);			
			blotter = blotterDao.listarBlotterDeRed();
		}else{
			dataSet.first();
			dataSet.next();
			blotter = dataSet.getValue("bloter_id");
		}
		return blotter;
	}
	
/**
 * Busca si existe un usuario asignado a un blotter especifico
 * @param String idUsuario
 * @param String idBlotter
 * @return boolean
 * @throws Exception
 */
	public boolean listarUsuarioBlotter(String idUsuario,String idBlotter)throws Exception{
		StringBuffer sql 			= new StringBuffer();
		boolean usuarioBloterExiste = false;
		
		sql.append("select * from infi_tb_104_bloter_usuarios where bloter_id='").append(idBlotter);;
		sql.append("' and userid='").append(idUsuario).append("'");
		dataSet = db.get(dataSource,sql.toString());
		if(dataSet.count()>0){
				usuarioBloterExiste = true;
			}
		
		return  usuarioBloterExiste;
	}
	
	/**
	 * Lista los bloter asociados por el usuario enviado como parametro vía WEB SERVICES
	 * No se muestra aquel bloter que tenga el indicador de red
	 * @param idUsuario
	 * @throws Exception
	 */
	public void listarBlotterUsuarioWS(String idUsuario)throws Throwable{
		/*
		 * Se valida que el usuario exista con el DataSource de Seguridad
		 */
		/**String 	dsName 						 = WSProperties.getProperty(ConstantesGenerales.DATASOURCE_SEGURIDAD_SEPA);
		javax.sql.DataSource dataSourceSeguridad = DBOServices.getDataSource(dsName);
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select USERID from MSC_USER where  USERID='").append(idUsuario).append("'");
			dataSet = db.get(dataSourceSeguridad,sql.toString());
			
		//Si no trae registros el usuario no existe
			if(dataSet.count()==0)
				throw new Throwable("El usuario no existe");
			
		} catch (Throwable e) {

			throw e;
		}**/
		/*
		 * Si el usuario existe,Se busca el bloter asociado
		 */
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select INFI_TB_104_BLOTER_USUARIOS.BLOTER_ID,INFI_TB_104_BLOTER_USUARIOS.USERID from INFI_TB_104_BLOTER_USUARIOS inner join INFI_TB_102_BLOTER on INFI_TB_104_BLOTER_USUARIOS.BLOTER_ID=INFI_TB_102_BLOTER.BLOTER_ID where userid='");
			sql.append(idUsuario).append("' and INFI_TB_102_BLOTER.BLOTER_IN_RED=").append(ConstantesGenerales.STATUS_INACTIVO);

			dataSet = db.get(dataSource,sql.toString());
			
		//Si no trae registros, el usuario no posee un bloter configurado
			if(dataSet.count()==0)
				throw new Throwable("El usuario no tiene configurado un Bloter");
		} catch (Throwable e) {
			throw e;
		}
		
	}
/**
 * Lista las sesiones registradas en la tabla de active session
 * Almacena los registros en un dataset
 * @param usuario
 * @throws Exception
 */
	public void listarSesionesActivas(String usuario)throws Exception{
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT sessionid,userid,remote_address,host_uri ,").append("to_char(start_date,'");
		sql.append(ConstantesGenerales.FORMATO_FECHA_SYSDATE).append("')as start_date,");
		sql.append("to_char(start_time,'").append(ConstantesGenerales.FORMATO_HORA);
		sql.append("')as start_time ").append("FROM ACTIVE_SESSIONS ");
		
		if(usuario!=null && !usuario.equals("")){
			sql.append(" WHERE USERID='").append(usuario).append("'");
		}

		//Se ejecuta la consulta
		dataSet = db.get(dataSource,sql.toString());
	}//fin listarSesionesActivas
	
	/**
	 * Elimina un registro de la tabla active_sessions
	 * @param usuario
	 * @throws Exception
	 */
	public void eliminarUsuarioActivo(String usuario)throws Exception{
		
		db.exec(dataSource,"delete from active_sessions where userid='"+usuario+"'");
		
	}//fin deleteUsuarioActivo
}

package com.bdv.infi.dao;

import javax.sql.DataSource;

import megasoft.db;

import com.bdv.infi.data.UIEmpresa;


/** 
 * Clase destinada para el manejo l&oacute;gico de inserci&oacute;n, recuperaci&oacute;n y listado de Empresas asociadas a Unidades de Inversi&oacute;n almacenadas en las tablas de trabajo
 */
public class UIEmpresaDAO extends com.bdv.infi.dao.GenericoDAO {

	/**
	 * SubQuery para rechazar las empresas ya seleccionadas
	 */
	private static String subSql = "(select empres_id from INFI_TB_109_UI_EMPRESAS where undinv_id = ";

	/**
	 * Constructor de la clase.
	 * Permite inicializar el DataSource para los accesos a la base de datos
	 * @param ds : DataSource 
	 * @throws Exception
	 */
	public UIEmpresaDAO(DataSource ds) throws Exception {
		super(ds);
	}
	
	public UIEmpresaDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
	}
	
	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Eliminar las asociacion entre una Empresa y una unidad de inversion de la base de datos
	 * @param idUnidadInversion : identificador de la Unidad de Inversion
	 * @param idEmpresa : identificador de la Empresa
	 * @throws Exception
	*/
	public int eliminar(long idUnidadInversion, String idEmpresa) throws Exception {
	
		StringBuffer sqlSB = new StringBuffer("delete from INFI_TB_109_UI_EMPRESAS ");		
		sqlSB.append("where undinv_id = ").append(idUnidadInversion+" ");
		sqlSB.append("and empres_id = ");
		sqlSB.append("'").append(idEmpresa).append("' ");
		
		db.exec(dataSource, sqlSB.toString());
		return 0;
	} 
	
	/**
	 * Ingresar una relacion entre una Empresa y una unidad de inversion en la base de datos
	 * @param beanUIEmpresa : bean de la asociacion Unidad de Inversion-Empresa con la data a registrar
	 * @throws Exception
	*/
	public int insertar(UIEmpresa beanUIEmpresa) throws Exception {
		
		StringBuffer sqlSB = new StringBuffer("insert into INFI_TB_109_UI_EMPRESAS ");		
		sqlSB.append("(undinv_id, empres_id) ");
		sqlSB.append("values (");
		sqlSB.append(beanUIEmpresa.getIdUnidadInversion()).append(", ");
		sqlSB.append("'").append(beanUIEmpresa.getIdEmpresa()).append("' ");
		sqlSB.append(")");

		db.exec(dataSource, sqlSB.toString());
		return 0;
    }
	
	/**
	 * Lista todos las Empresas encontradas en la base de datos, que no esten asociadas a la Unidad de Inversion
	 * Almacena el resultado en un dataset
	 * @param nombreEmpresa : prefijo para buscar los titulos
	 * @param idUnidadInversion : identificador de la Unidad de Inversion
	 * @throws Exception arroja una excepci&oacute;n en caso de un error
	 * */
	public void listarEmpresasNombre(String nombreEmpresa, long idUnidadInversion) throws Exception {
		
		StringBuffer sqlSB = new StringBuffer();
		sqlSB.append("select empres_id, empres_nombre, empres_rif, ");	
		sqlSB.append("0 as fila, ' ' as selecc ");			
		sqlSB.append("from INFI_TB_016_EMPRESAS empres ");		
		sqlSB.append("where empres.empres_status = 1 ");	
		sqlSB.append("and lower(empres.empres_nombre) like '%").append(nombreEmpresa.toLowerCase()).append("%' ");		
		sqlSB.append("and empres.empres_id not in ").append(subSql).append(idUnidadInversion+") ");			
		sqlSB.append("order by empres_nombre");

		dataSet = db.get(dataSource, sqlSB.toString());
	}
	
	/**
	 * Lista todos las Empresas encontradas en la base de datos, que no esten asociadas a la Unidad de Inversion
	 * Almacena el resultado en un dataset
	 * @param rifEmpresa : rif base de la consulta
	 * @param idUnidadInversion : identificador de la Unidad de Inversion
	 * @throws Exception arroja una excepci&oacute;n en caso de un error
	 * */
	public void listarEmpresasRIF(String rifEmpresa, long idUnidadInversion) throws Exception {
		
		StringBuffer sqlSB = new StringBuffer();
		sqlSB.append("select empres_id, empres_nombre, empres_rif, ");	
		sqlSB.append("0 as fila, ' ' as selecc ");			
		sqlSB.append("from INFI_TB_016_EMPRESAS empres ");		
		sqlSB.append("where empres.empres_status = 1 ");			
		sqlSB.append("and lower(empres.empres_rif) like '%").append(rifEmpresa.toLowerCase()).append("%' ");		
		sqlSB.append("and empres.empres_id not in ").append(subSql).append(idUnidadInversion+") ");			
		sqlSB.append("order by empres_rif");
		
		dataSet = db.get(dataSource, sqlSB.toString());
	}
	
	
	/**
	 * Lista todos las Empresas encontradas en la base de datos, que no esten asociadas a la Unidad de Inversion
	 * Almacena el resultado en un dataset
	 * @param idUnidadInversion : identificador de la Unidad de Inversion
	 * @throws Exception arroja una excepci&oacute;n en caso de un error
	 * */
	public void listarEmpresas(long idUnidadInversion) throws Exception {
		
		StringBuffer sqlSB = new StringBuffer();
		sqlSB.append("select empres_id, empres_nombre, empres_rif, ");	
		sqlSB.append("0 as fila, ' ' as selecc ");			
		sqlSB.append("from INFI_TB_016_EMPRESAS empres ");		
		sqlSB.append("where empres.empres_status = 1 ");			
		sqlSB.append("and empres.empres_id not in ").append(subSql).append(idUnidadInversion+") ");			
		sqlSB.append("order by empres_nombre");

		dataSet = db.get(dataSource, sqlSB.toString());
	}
	
	/**
	 * Lista todas las Empresas asociadas a una Unidad de Inversion registradas en la base de datos
	 * Almacena el resultado en un dataset
	 * @param idUnidadInversion : identificador de la Unidad de Inversion
	 * @throws Exception arroja una excepci&oacute;n en caso de un error
	 * */
	public void listarEmpresasPorID(long idUnidadInversion) throws Exception {
		
		StringBuffer sqlSB = new StringBuffer();
		sqlSB.append("select uiempr.*, empres_nombre, empres_rif, ");	
		sqlSB.append("roles.roles_id, roles_descripcion, ");
		sqlSB.append("0 as fila, 'checked' as selecc ");			
		sqlSB.append("from INFI_TB_109_UI_EMPRESAS uiempr ");
		sqlSB.append("inner join INFI_TB_016_EMPRESAS empres on empres.empres_id = uiempr.empres_id ");
		sqlSB.append("left join INFI_TB_017_ROLES roles on roles.roles_id = uiempr.roles_id ");		
		sqlSB.append("where uiempr.undinv_id = "+idUnidadInversion+" ");	
		sqlSB.append("order by empres_nombre");

		dataSet = db.get(dataSource, sqlSB.toString());
	}
	
	/**
	 * Lista el UI_Empresa dada su identificador
	 * @param idUnidadInversion : identificador de la unidad de inversi&oacute;n
	 * @param idEmpresa : identificdor de la Empresa
	 * @throws Exception
	*/
	public int listarPorId(long idUnidadInversion, String idEmpresa) throws Exception {
		
		StringBuffer sqlSB = new StringBuffer();
		sqlSB.append("select uiempr.*, empres_nombre, empres_rif, ");	
		sqlSB.append("roles.roles_id, roles_descripcion, ");
		sqlSB.append("0 as fila, 'checked' as selecc ");			
		sqlSB.append("from INFI_TB_109_UI_EMPRESAS uiempr ");
		sqlSB.append("inner join INFI_TB_016_EMPRESAS empres on empres.empres_id = uiempr.empres_id ");
		sqlSB.append("left join INFI_TB_017_ROLES roles on roles.roles_id = uiempr.roles_id ");		
		sqlSB.append("where uiempr.undinv_id = "+idUnidadInversion+" ");		
		sqlSB.append("and uiempr.empres_id = '").append(idEmpresa).append("' ");	
		sqlSB.append("order by empres_nombre");

		dataSet = db.get(dataSource, sqlSB.toString());

		return dataSet.count();
    }
	
	/**
	 * Modificar los datos propios de la relacion entre una Empresa y una unidad de inversion
	 * @param beanUIEmpresa : bean Unidad de Inversion-Empresa con la data a registrar
	 * @return codigo de retorno
	 * @throws Exception
	 */
	public int modificar(UIEmpresa beanUIEmpresa) throws Exception {

		StringBuffer sqlSB = new StringBuffer("update INFI_TB_109_UI_EMPRESAS set ");		
		
		sqlSB.append("roles_id = ");
		sqlSB.append("'").append(beanUIEmpresa.getIdRoles()).append("', ");
		sqlSB.append("uiempr_contacto_nom = ");		
		sqlSB.append("'").append(beanUIEmpresa.getNombreContacto()).append("', ");	
		sqlSB.append("uiempr_contacto_tlf = ");
		sqlSB.append("'").append(beanUIEmpresa.getTelefonoContacto()).append("' ");	
		sqlSB.append("where undinv_id = ");
		sqlSB.append(beanUIEmpresa.getIdUnidadInversion()).append(" ");		
		sqlSB.append("and empres_id = ");
		sqlSB.append("'").append(beanUIEmpresa.getIdEmpresa()).append("' ");			
		
		db.exec(dataSource, sqlSB.toString());

		return 0;
	}
}

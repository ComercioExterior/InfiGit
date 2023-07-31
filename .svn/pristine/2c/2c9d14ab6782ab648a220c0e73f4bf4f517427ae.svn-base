package com.bdv.infi.dao;

import javax.sql.DataSource;
import com.bdv.infi.data.RolesVehiculo;
import com.bdv.infi.data.VehiculoDefinicion;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

import megasoft.db;

/** 
 * Clase que encapsula los procesos de inserci&oacute;n, modificaci&oacute;n, eliminaci&oacute;n y lista de los veh&iacute;culos registrados. Hace uso de la tabla INFI_TB_018_VEHICULOS
 */
public class RolesVehiculoDAO extends com.bdv.infi.dao.GenericoDAO {
	
	public RolesVehiculoDAO(Transaccion transanccion) throws Exception {
		super(transanccion);
		// TODO Auto-generated constructor stub
	}
		
	public RolesVehiculoDAO(DataSource ds) {
		super(ds);
		// TODO Auto-generated constructor stub
	}
		
	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Lista un Rolespecifico registrados 
	*/
	public void listar() throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct r.vehicu_rol_id,(select distinct v.vehicu_nombre from INFI_TB_018_VEHICULOS v where r.vehicu_rol_tomador=v.vehicu_id) as vehicu_rol_tomador,(select distinct v.vehicu_nombre from INFI_TB_018_VEHICULOS v where r.vehicu_rol_colocador=v.vehicu_id) as vehicu_rol_colocador,(select distinct v.vehicu_nombre from INFI_TB_018_VEHICULOS v where r.vehicu_rol_recompra=v.vehicu_id) as vehicu_rol_recompra, vehicu_rol_in_defecto,case when vehicu_rol_in_defecto=0 then 'No' when vehicu_rol_in_defecto=1 then 'Si' end in_defecto from INFI_TB_031_VEHICULO_ROL r");
	
		dataSet = db.get(dataSource, sql.toString());
	}
		
	/**
	 * Lista un Rolespecifico registrados 
	*/
	public void listarRoles(int idRol) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select vehicu_rol_id, vehicu_rol_tomador, vehicu_rol_colocador, vehicu_rol_recompra, vehicu_rol_in_defecto from INFI_TB_031_VEHICULO_ROL where ");
		sql.append(" vehicu_rol_id=").append(idRol);

		dataSet = db.get(dataSource, sql.toString());
	}
	
	/**Verifica cuantos registros tienen el indicador de vehiculo_rol_in_defecto en 1*/
	public void verificar() throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select count(vehicu_rol_in_defecto) from INFI_TB_031_VEHICULO_ROL where vehicu_rol_in_defecto=1");
		
		dataSet =db.get(dataSource,sql.toString());
	}
	
	/**Verifica si ya existe un registro con esos datos*/
	public void verificarDuplicado(RolesVehiculo rolesVehiculo) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct vehicu_rol_id from INFI_TB_031_VEHICULO_ROL where");
		sql.append(" vehicu_rol_tomador='").append(rolesVehiculo.getTomador()).append("'");
		sql.append(" and vehicu_rol_colocador='").append(rolesVehiculo.getColocador()).append("'");
		sql.append(" and vehicu_rol_recompra='").append(rolesVehiculo.getRecompra()).append("'");
		
		dataSet =db.get(dataSource,sql.toString());
	}
	
	public String insertar(RolesVehiculo rolesVehiculo) throws Exception{
		StringBuffer sql = new StringBuffer();
		
		sql.append("insert into INFI_TB_031_VEHICULO_ROL ( vehicu_rol_tomador,vehicu_rol_colocador,vehicu_rol_recompra,vehicu_rol_in_defecto,vehicu_rol_id) values (");
		String idRolVehi = dbGetSequence(dataSource, ConstantesGenerales.SECUENCIA_VEHICULO_ROL);

		sql.append("'").append(rolesVehiculo.getTomador()).append("',");
		sql.append("'").append(rolesVehiculo.getColocador()).append("',");
		sql.append("'").append(rolesVehiculo.getRecompra()).append("',");
		sql.append(rolesVehiculo.getPorDefecto()).append(",");
		sql.append(idRolVehi).append(")");
		
		return(sql.toString());
	}
	
	public String modificar(RolesVehiculo rolesVehiculo) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		
		sql.append("update INFI_TB_031_VEHICULO_ROL set ");
		
		filtro.append(" vehicu_rol_tomador='").append(rolesVehiculo.getTomador()).append("',");
		filtro.append(" vehicu_rol_colocador='").append(rolesVehiculo.getColocador()).append("',");
		filtro.append(" vehicu_rol_recompra='").append(rolesVehiculo.getRecompra()).append("',");
		filtro.append(" vehicu_rol_in_defecto=").append(rolesVehiculo.getPorDefecto());
		filtro.append(" where vehicu_rol_id=").append(rolesVehiculo.getIdRoles());
		sql.append(filtro);		
		
		return(sql.toString());
	}
	
	/**Modifica el indicador por defecto 
	 * @return 
	 * @throws Exception
	 */
	public String modificarIndicadorPorDefecto() throws Exception{
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("update INFI_TB_031_VEHICULO_ROL set ");
		sql.append(" vehicu_rol_in_defecto=").append(ConstantesGenerales.FALSO);
		
		return(sql.toString());
	}
	
	public String eliminar(RolesVehiculo rolesVehiculo) throws Exception{
		StringBuffer sql = new StringBuffer();
		
		sql.append("delete from INFI_TB_031_VEHICULO_ROL where");
		sql.append(" vehicu_rol_id=").append(rolesVehiculo.getIdRoles());
		
		return(sql.toString());
	}
	
	public void verificarRolTomador(RolesVehiculo rolesVehiculo) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select vehicu_rol_tomador from INFI_TB_031_VEHICULO_ROL where");
		sql.append(" vehicu_rol_tomador='").append(rolesVehiculo.getIdRoles()).append("'");

		dataSet =db.get(dataSource,sql.toString());
	}
	
	public void verificarRolColocador(RolesVehiculo rolesVehiculo) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select vehicu_rol_colocador from INFI_TB_031_VEHICULO_ROL where");
		sql.append(" vehicu_rol_colocador='").append(rolesVehiculo.getIdRoles()).append("'");

		dataSet =db.get(dataSource,sql.toString());
	}
	
	public void verificarRolRecompra(RolesVehiculo rolesVehiculo) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select vehicu_rol_recompra from INFI_TB_031_VEHICULO_ROL where");
		sql.append(" vehicu_rol_recompra='").append(rolesVehiculo.getIdRoles()).append("'");

		dataSet =db.get(dataSource,sql.toString());
	}
}

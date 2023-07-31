package com.bdv.infi.dao;

import javax.sql.DataSource;

import megasoft.db;

/** 
 * Clase que encapsula los procesos de inserci&oacute;n, modificaci&oacute;n, eliminaci&oacute;n y lista de los roles que puede tomar un veh&iacute;culos. Hace uso de la tabla INFI_TB_031_VEHICULO_ROL
 */
public class VehiculoRolDAO extends com.bdv.infi.dao.GenericoDAO {
	
	public VehiculoRolDAO(Transaccion transanccion) throws Exception {
		super(transanccion);
		// TODO Auto-generated constructor stub
	}
	
	public VehiculoRolDAO(DataSource ds) {
		super(ds);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Lista todos los veh&iacute;culos registrados por rol
	*/
	public void listarTodos() throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select vr.*, ");
		sql.append(" (select vehicu_nombre from INFI_TB_018_VEHICULOS where vehicu_id = vr.vehicu_rol_tomador) as nombre_tomador,");
		sql.append(" (select vehicu_nombre from INFI_TB_018_VEHICULOS where vehicu_id = vr.vehicu_rol_colocador) as nombre_colocador,");
		sql.append(" (select vehicu_nombre from INFI_TB_018_VEHICULOS where vehicu_id = vr.vehicu_rol_recompra) as nombre_veh_recompra,");
		sql.append(" (select vehicu_id from INFI_TB_018_VEHICULOS where vehicu_id = vr.vehicu_rol_tomador) as id_tomador,");
		sql.append(" (select vehicu_id from INFI_TB_018_VEHICULOS where vehicu_id = vr.vehicu_rol_colocador) as id_colocador,");
		sql.append(" (select vehicu_id from INFI_TB_018_VEHICULOS where vehicu_id = vr.vehicu_rol_recompra) as id_veh_recompra,");
		sql.append(" (select vehicu_siglas from INFI_TB_018_VEHICULOS where vehicu_id = vr.vehicu_rol_tomador) as siglas_tomador,");
		sql.append(" (select vehicu_siglas from INFI_TB_018_VEHICULOS where vehicu_id = vr.vehicu_rol_colocador) as siglas_colocador,");
		sql.append(" (select vehicu_siglas from INFI_TB_018_VEHICULOS where vehicu_id = vr.vehicu_rol_recompra) as siglas_veh_recompra");

		sql.append(" from INFI_TB_031_VEHICULO_ROL vr order by nombre_tomador ");

		dataSet = db.get(dataSource, sql.toString());
	}
	
	/**
	 * Lista los datos del vehiculo por defecto.
	 * @throws Exception
	 */
	public void listarVehiculoRolPorDefecto() throws Exception{
		/*StringBuffer sql = new StringBuffer();
		sql.append("select v.*, vr.* from INFI_TB_018_VEHICULOS v, INFI_TB_031_VEHICULO_ROL vr where vr.vehicu_rol_tomador = v.vehicu_id order and vr.vehicu_rol_in_defecto = 1");

		dataSet = db.get(dataSource, sql.toString());*/
		
		StringBuffer sql = new StringBuffer();

		sql.append("select vr.*, ");
		sql.append(" (select vehicu_nombre from INFI_TB_018_VEHICULOS where vehicu_id = vr.vehicu_rol_tomador) as nombre_tomador,");
		sql.append(" (select vehicu_nombre from INFI_TB_018_VEHICULOS where vehicu_id = vr.vehicu_rol_colocador) as nombre_colocador,");
		sql.append(" (select vehicu_nombre from INFI_TB_018_VEHICULOS where vehicu_id = vr.vehicu_rol_recompra) as nombre_veh_recompra,");
		sql.append(" (select vehicu_id from INFI_TB_018_VEHICULOS where vehicu_id = vr.vehicu_rol_tomador) as id_tomador,");
		sql.append(" (select vehicu_id from INFI_TB_018_VEHICULOS where vehicu_id = vr.vehicu_rol_colocador) as id_colocador,");
		sql.append(" (select vehicu_id from INFI_TB_018_VEHICULOS where vehicu_id = vr.vehicu_rol_recompra) as id_veh_recompra,");
		sql.append(" (select vehicu_siglas from INFI_TB_018_VEHICULOS where vehicu_id = vr.vehicu_rol_tomador) as siglas_tomador,");
		sql.append(" (select vehicu_siglas from INFI_TB_018_VEHICULOS where vehicu_id = vr.vehicu_rol_colocador) as siglas_colocador,");
		sql.append(" (select vehicu_siglas from INFI_TB_018_VEHICULOS where vehicu_id = vr.vehicu_rol_recompra) as siglas_veh_recompra");

		sql.append(" from INFI_TB_031_VEHICULO_ROL vr ");
		sql.append(" where vr.vehicu_rol_in_defecto = 1");//Rol por defecto
		sql.append(" order by nombre_tomador ");

		dataSet = db.get(dataSource, sql.toString());

		
	}
	
	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Lista los detalles para un rol determinado de vehiculos
	 * @param idVehiculoRol
	 * @throws Exception
	 */
	public void listarVehiculoRolPorId(long idVehiculoRol) throws Exception{
		StringBuffer sql = new StringBuffer();

		sql.append("select vr.*, ");
		//Nombres de los vehiculos tomador, colocador y de rescompra
		sql.append(" (select vehicu_nombre from INFI_TB_018_VEHICULOS where vehicu_id = vr.vehicu_rol_tomador) as nombre_tomador,");
		sql.append(" (select vehicu_nombre from INFI_TB_018_VEHICULOS where vehicu_id = vr.vehicu_rol_colocador) as nombre_colocador,");
		sql.append(" (select vehicu_nombre from INFI_TB_018_VEHICULOS where vehicu_id = vr.vehicu_rol_recompra) as nombre_veh_recompra,");
		//Ids de los vehiculos tomador, colocador y de rescompra
		sql.append(" (select vehicu_id from INFI_TB_018_VEHICULOS where vehicu_id = vr.vehicu_rol_tomador) as id_tomador,");
		sql.append(" (select vehicu_id from INFI_TB_018_VEHICULOS where vehicu_id = vr.vehicu_rol_colocador) as id_colocador,");
		sql.append(" (select vehicu_id from INFI_TB_018_VEHICULOS where vehicu_id = vr.vehicu_rol_recompra) as id_veh_recompra,");
		//Siglas de los vehiculos tomador, colocador y de rescompra
		sql.append(" (select vehicu_siglas from INFI_TB_018_VEHICULOS where vehicu_id = vr.vehicu_rol_tomador) as siglas_tomador,");
		sql.append(" (select vehicu_siglas from INFI_TB_018_VEHICULOS where vehicu_id = vr.vehicu_rol_colocador) as siglas_colocador,");
		sql.append(" (select vehicu_siglas from INFI_TB_018_VEHICULOS where vehicu_id = vr.vehicu_rol_recompra) as siglas_veh_recompra,");
		//Rif de los vehiculos tomador, colocador y de rescompra
		sql.append(" (select vehicu_rif from INFI_TB_018_VEHICULOS where vehicu_id = vr.vehicu_rol_tomador) as rif_tomador,");
		sql.append(" (select vehicu_rif from INFI_TB_018_VEHICULOS where vehicu_id = vr.vehicu_rol_colocador) as rif_colocador,");
		sql.append(" (select vehicu_rif from INFI_TB_018_VEHICULOS where vehicu_id = vr.vehicu_rol_recompra) as rif_veh_recompra");

		sql.append(" from INFI_TB_031_VEHICULO_ROL vr ");
		sql.append(" where vehicu_rol_id = ").append(idVehiculoRol);
		sql.append(" order by nombre_tomador ");

		dataSet = db.get(dataSource, sql.toString());
	}

	
}

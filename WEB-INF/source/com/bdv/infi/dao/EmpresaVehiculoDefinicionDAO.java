package com.bdv.infi.dao;

import javax.sql.DataSource;

import com.bdv.infi.data.EmpresaRoles;
import com.bdv.infi.data.VehiculoDefinicion;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

import megasoft.DataSet;
import megasoft.db;

public class EmpresaVehiculoDefinicionDAO extends com.bdv.infi.dao.GenericoDAO {
	public EmpresaVehiculoDefinicionDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
	}
	
	public EmpresaVehiculoDefinicionDAO(DataSource ds) throws Exception {
		super(ds);
	}	
	
	public void listar(String nombre, String rif, String siglas) throws Exception{
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		sql.append("select * from infi_tb_018_vehiculos where 1=1");
	
		if(nombre!=null){
			filtro.append(" and upper(vehicu_nombre) like upper('%").append(nombre).append("%')");
		}
		if(rif!=null){
			filtro.append(" and upper(vehicu_rif) like upper('%").append(rif).append("%')");
		}	
		
		if(siglas!=null){			
			filtro.append(" and upper(vehicu_siglas) like upper('%").append(siglas).append("%')");
		}
		sql.append(filtro);
		sql.append(" ORDER BY vehicu_nombre");
		dataSet = db.get(dataSource, sql.toString());

	}
	
	public void listar(String vehicu_id) throws Exception{
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		
		sql.append("select * from infi_tb_018_vehiculos where 1=1");
		
		if(vehicu_id!=null){
			filtro.append(" and vehicu_id='").append(vehicu_id).append("'");
		}	
		sql.append(filtro);
		sql.append(" ORDER BY vehicu_nombre");
						
		dataSet = db.get(dataSource, sql.toString());
	}
	
	public String insertar(VehiculoDefinicion vehiculoDefinicion) throws Exception{
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		
		sql.append("insert into infi_tb_018_vehiculos ( vehicu_id,vehicu_nombre,vehicu_rif, vehicu_siglas, vehicu_branch, vehicu_numero_cuenta, vehicu_numero_cuenta_bcv) values (");
		String idVehiculo = dbGetSequence(dataSource, ConstantesGenerales.SECUENCIA_VEHICULOS);
		
		filtro.append("'").append(idVehiculo).append("',");
		filtro.append("'").append(vehiculoDefinicion.getVehicu_nombre()).append("',");
		filtro.append("'").append(vehiculoDefinicion.getVehicu_rif().toUpperCase()).append("',");		
		filtro.append("'").append(vehiculoDefinicion.getVehiculoSiglas()).append("', ");
		
		filtro.append("'").append(vehiculoDefinicion.getVehiculoBranch()).append("', ");
		filtro.append("'").append(vehiculoDefinicion.getVehiculoNumeroCuenta()).append("', ");
		filtro.append("'").append(vehiculoDefinicion.getVehiculoNumeroCuentaBcv()).append("'");
		
		filtro.append(")");
		
		sql.append(filtro);		
		return(sql.toString());
	}
	
	public String modificar(VehiculoDefinicion vehiculoDefinicion) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		
		sql.append("update infi_tb_018_vehiculos set ");
		
		//filtro.append(" vehicu_nombre='").append(vehiculoDefinicion.getVehicu_nombre()).append("',");
		//filtro.append(" vehicu_rif='").append(vehiculoDefinicion.getVehicu_rif().toUpperCase()).append("',");
		filtro.append(" vehicu_siglas='").append(vehiculoDefinicion.getVehiculoSiglas()).append("', ");
		filtro.append(" vehicu_branch='").append(vehiculoDefinicion.getVehiculoBranch()).append("', ");
		filtro.append(" vehicu_numero_cuenta='").append(vehiculoDefinicion.getVehiculoNumeroCuenta()).append("', ");
		filtro.append(" vehicu_numero_cuenta_bcv='").append(vehiculoDefinicion.getVehiculoNumeroCuentaBcv()).append("'");
		
		filtro.append(" where vehicu_id='").append(vehiculoDefinicion.getVehicu_id()).append("'");
		sql.append(filtro);			
		return(sql.toString());
	}
	
	public String eliminar(VehiculoDefinicion vehiculoDefinicion) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		
		sql.append("delete from infi_tb_018_vehiculos where");
		
		filtro.append(" vehicu_id='").append(vehiculoDefinicion.getVehicu_id()).append("'");
		sql.append(filtro);			
		return(sql.toString());
	}
	
	public void verificarVehiculoColocador(VehiculoDefinicion vehiculoDefinicion) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select ordene_veh_col from INFI_TB_204_ORDENES where");
		sql.append(" ordene_veh_col='").append(vehiculoDefinicion.getVehicu_id()).append("'");

		dataSet =db.get(dataSource,sql.toString());
	}
	
	public void verificarVehiculoRecompra(VehiculoDefinicion vehiculoDefinicion) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select ordene_veh_rec from INFI_TB_204_ORDENES where");
		sql.append(" ordene_veh_rec='").append(vehiculoDefinicion.getVehicu_id()).append("'");

		dataSet =db.get(dataSource,sql.toString());
	}
	
	public void verificarVehiculoTomador(VehiculoDefinicion vehiculoDefinicion) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select ordene_veh_tom from INFI_TB_204_ORDENES where");
		sql.append(" ordene_veh_tom='").append(vehiculoDefinicion.getVehicu_id()).append("'");

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
	public  boolean verificarVehiculoNombreExiste(String valorCampo) throws Exception{
		boolean encontro=false;
		StringBuffer sb=new StringBuffer();
		sb.append("select * from ");
		sb.append("INFI_TB_018_VEHICULOS");
		sb.append(" where initCap(");
		sb.append("VEHICU_NOMBRE");
		sb.append(") = initCap('");
		sb.append(valorCampo);
		sb.append("')");

		DataSet ds=db.get(dataSource,sb.toString());
		if (ds.count()>0)
			encontro=true;
		
		return encontro;
	}
}

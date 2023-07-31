package com.bdv.infi.dao;

import javax.sql.DataSource;

import com.bdv.infi.data.BloterDefinicion;
import com.bdv.infi.data.EmpresaDefinicion;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

import megasoft.DataSet;
import megasoft.db;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/** 
 * Clase usada para la modificaci&oacute;n, inserci&oacute;n y listado de los t&iacute;tulos en custodia
 */
public class EmpresaDefinicionDAO extends com.bdv.infi.dao.GenericoDAO {

	public EmpresaDefinicionDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
	}
	
	public EmpresaDefinicionDAO(DataSource ds) throws Exception {
		super(ds);
	}	
	
	
	/**Busca los t&iacute;tulos que cumplan con la condici&oacute;n solicitada. 
	 * @param idCliente id del cliente que se desea buscar
	 * @param idTitulo id del t&iacute;tulo en custodia
	 * @param idMoneda id de la moneda
	 * @param emisionDesde fecha de emision desde del t&iacute;tulo
	 * @param emisionHasta fecha de emision hasta del t&iacute;tulo
	 * @param vencimientoDesde fecha de vencimiento desde del t&iacute;tulo
	 * @param vencimientoHasta fecha de vencimiento hasta del t&iacute;tulo	 * 
	 * @throws Exception 
	 * */
	public void listar(String nombre, String rif, String status) throws Exception{
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		sql.append("select empres_id,empres_nombre,empres_rif,empres_siglas,case when empres_in_emisor=").append(ConstantesGenerales.VERDADERO).append(" then 'Si' when empres_in_emisor=").append(ConstantesGenerales.FALSO).append(" then 'No' end empres_in_emisor, case when empres_in_depositario_central=").append(ConstantesGenerales.VERDADERO).append(" then 'Si' when empres_in_depositario_central=").append(ConstantesGenerales.FALSO).append(" then 'No' end empres_in_depositario_central, case when empres_status=").append(ConstantesGenerales.VERDADERO).append(" then 'Activo' when empres_status=").append(ConstantesGenerales.FALSO).append(" then 'Inactivo' end empres_status,empres_email from infi_tb_016_empresas where 1=1");
		if(status!=null ){
			filtro.append(" and empres_status=").append(status);
		}
		if(nombre!=null){
			filtro.append(" and upper(empres_nombre) like upper('%").append(nombre).append("%')");
		}
		if(rif!=null){
			filtro.append(" and upper(empres_rif) like upper('%").append(rif).append("%')");
		}	
		sql.append(filtro);
		sql.append(" ORDER BY empres_nombre");
		dataSet = db.get(dataSource, sql.toString());
		System.out.println("listar-->"+sql);
	}
	
	public void listar(String empres_id) throws Exception{
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		
		sql.append("select empres_id,empres_nombre,empres_rif,empres_in_emisor,empres_in_depositario_central,empres_status,empres_email,empres_siglas, empres_cuenta from infi_tb_016_empresas where 1=1");
		
		if(empres_id!=null){
			filtro.append(" and empres_id='").append(empres_id).append("'");
		}	
		sql.append(filtro);
		sql.append(" ORDER BY empres_nombre");
						
		dataSet = db.get(dataSource, sql.toString());
	}
	
	public String insertar(EmpresaDefinicion empresaDefinicion) throws Exception{
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		
		sql.append("insert into infi_tb_016_empresas ( empres_id,empres_nombre,empres_rif,empres_in_emisor,empres_in_depositario_central,empres_status, empres_cuenta, empres_siglas) values (");
		String idEmpres = dbGetSequence(dataSource, ConstantesGenerales.SECUENCIA_EMPRESAS);
		
		filtro.append("'").append(idEmpres).append("',");
		filtro.append("'").append(empresaDefinicion.getEmpres_nombre().toUpperCase()).append("',");
		filtro.append("'").append(empresaDefinicion.getEmpres_rif().toUpperCase()).append("',");
		filtro.append(empresaDefinicion.getEmpres_in_emisor()).append(",");
		filtro.append(empresaDefinicion.getEmpres_in_depositario_central()).append(",");
		filtro.append(empresaDefinicion.getEmpres_status()).append(",");
		filtro.append("'").append(empresaDefinicion.getEmpresa_numero_cuenta()).append("',");
		filtro.append("'").append(empresaDefinicion.getEmpres_siglas()).append("')");
		//filtro.append("'").append(empresaDefinicion.getEmpres_email()).append("')");		
		
		sql.append(filtro);		
		return(sql.toString());
	}
	
	public String modificar(EmpresaDefinicion empresaDefinicion) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		
		sql.append("update infi_tb_016_empresas set ");
				
		filtro.append(" empres_nombre='").append(empresaDefinicion.getEmpres_nombre()).append("',");	
		filtro.append(" empres_in_emisor=").append(empresaDefinicion.getEmpres_in_emisor()).append(",");
		filtro.append(" empres_in_depositario_central=").append(empresaDefinicion.getEmpres_in_depositario_central()).append(",");
		filtro.append(" empres_status=").append(empresaDefinicion.getEmpres_status()).append(",");
		filtro.append(" empres_siglas='").append(empresaDefinicion.getEmpres_siglas()).append("',");
		filtro.append(" empres_cuenta='").append(empresaDefinicion.getEmpresa_numero_cuenta()).append("',");
		filtro.append(" empres_rif='").append(empresaDefinicion.getEmpres_rif()).append("'");		
		filtro.append(" where empres_id='").append(empresaDefinicion.getEmpres_id()).append("'");
		sql.append(filtro);			
		return(sql.toString());
	}
	
	public String eliminar(EmpresaDefinicion empresaDefinicion) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		
		sql.append("delete from infi_tb_016_empresas where");
		
		filtro.append(" empres_id='").append(empresaDefinicion.getEmpres_id()).append("'");
		sql.append(filtro);			
		return(sql.toString());
	}
	
	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Busca todas las empresas existentes en Base de Datos
	 * @throws Exception
	 */
	public void listarTodas() throws Exception{
		
		String sql = "SELECT * FROM INFI_TB_016_EMPRESAS order by empres_nombre";
		
		dataSet = db.get(dataSource, sql);
    }
	
	/**Metodo para buscar todas la empresas con el indicador depositario en 1*/
	public void depositarioCentralSi() throws Exception{
		
		String sql = "SELECT empres_id, empres_nombre FROM INFI_TB_016_EMPRESAS where empres_in_depositario_central=1";
		
		dataSet = db.get(dataSource, sql);
		System.out.println("depositarioCentralSi "+sql);
    }
	
	public void verificar(EmpresaDefinicion empresaDefinicion) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select empres_id from INFI_TB_109_UI_EMPRESAS where");
		sql.append(" empres_id='").append(empresaDefinicion.getEmpres_id()).append("'");

		dataSet =db.get(dataSource,sql.toString());
	}
	
	/**Metodo que verifica si existe un registro con el mismo nombre en base de datos
	*  @param String valorCampo
	*  @throws Exception lanza una excepci&ooacute;n si hay un error en la operaci&oacute;n
	*/
	public  boolean verificarNombreExiste(String valorCampo) throws Exception{
		boolean encontro=false;
		StringBuffer sb=new StringBuffer();
		sb.append("select * from ");
		sb.append("INFI_TB_016_EMPRESAS");
		sb.append(" where initCap(");
		sb.append("EMPRES_NOMBRE");
		sb.append(") = initCap('");
		sb.append(valorCampo);
		sb.append("')");

		DataSet ds=db.get(dataSource,sb.toString());
		if (ds.count()>0)
			encontro=true;
		
		return encontro;
	}
	
	public void listarPorRif(String rif) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		
		if(rif!=null){							
			sql.append("select empres_id,empres_nombre,empres_rif,empres_in_emisor,empres_in_depositario_central,empres_status,empres_email,empres_siglas, empres_cuenta from infi_tb_016_empresas where 1=1 ");								
			sql.append(" and upper(empres_rif) = upper('").append(rif).append("')");			
			sql.append(" ORDER BY empres_nombre");							
		}		
			
		dataSet = db.get(dataSource, sql.toString());
	}
	
	public void listarPorSiglas(String siglas) throws Exception{
		StringBuffer sql = new StringBuffer();
		
		if(siglas!=null){							
			sql.append("select empres_id,empres_nombre,empres_rif,empres_in_emisor,empres_in_depositario_central,empres_status,empres_email,empres_siglas, empres_cuenta from infi_tb_016_empresas where 1=1 ");								
			sql.append(" and upper(EMPRES_SIGLAS) = upper('").append(siglas).append("')");			
			sql.append(" ORDER BY empres_nombre");							
		}		
		
		System.out.println("BUQEUDA DE INFORMACION EMPRESA: " + sql.toString()); 
		dataSet = db.get(dataSource, sql.toString());
	}
}

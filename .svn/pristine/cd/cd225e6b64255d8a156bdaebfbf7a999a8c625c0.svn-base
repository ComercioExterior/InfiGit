package com.bdv.infi.dao;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.axis2.clustering.configuration.commands.ExceptionCommand;

import megasoft.DataSet;
import megasoft.db;

import com.bdv.infi.config.Mensajes;
import com.bdv.infi.data.Moneda;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;

/**Clase que contiene la l&oacute;gica de inserci&oacute;n, modificaci&oacute;n y listado de las monedas registradas en la base de datos*/
public class MonedaDAO extends GenericoDAO {

	/**M&eacute;todo que busca una moneda por su id
	 * @param id id de la moneda
	 * @throws Exception Error en la b&uacute;squeda de la moneda por su id*/	 
	public boolean listarPorId(String id) throws Exception{
		boolean bolOk = false;
		StringBuffer sql = new StringBuffer(); 
		sql.append("SELECT * FROM INFI_VI_MONEDAS WHERE MONEDA_ID='").append(id).append("'");
		try {
			conn = dataSource.getConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql.toString());
		} catch (Exception e) {
			throw new Exception("Error en la b&uacute;squeda de la moneda por su id " + e.getMessage());
		}
		if (resultSet.next()) {
			bolOk = true;
		}
		return bolOk;
	}
	
	
	/**Variable que contiene el sql b&aacute;sico para la b&uacute;squeda de las monedas*/
	private String strSQLMoneda = "SELECT infi_tb_007_monedas_cambio.moneda_cambio_venta,infi_tb_007_monedas_cambio.moneda_cambio_compra,moneda_siglas,INFI_VI_MONEDAS.MONEDA_ID,MONEDA_DESCRIPCION, case when moneda_in_local="+ConstantesGenerales.FALSO+" then 'No' when moneda_in_local="+ConstantesGenerales.VERDADERO+" then 'Si'" +
			"end moneda_in_local, case when moneda_in_exchange="+ConstantesGenerales.FALSO+" then 'No' when moneda_in_exchange="+ConstantesGenerales.VERDADERO+" then 'Si'" +
			"end moneda_in_exchange, case when moneda_status="+ConstantesGenerales.FALSO+" then 'INACTIVA' when moneda_status="+ConstantesGenerales.VERDADERO+" then 'ACTIVA'" +
			"end moneda_status FROM INFI_VI_MONEDAS left join infi_tb_007_monedas_cambio on INFI_VI_MONEDAS.moneda_id=infi_tb_007_monedas_cambio.moneda_id ";
	
	/**Variable que contiene el sql b&aacute;sico para la b&uacute;squeda de las Tasas por moneda*/
	private String strSQLMonedaTasas = "select '' as display,infi_tb_007_monedas_cambio.moneda_id_sec,INFI_VI_MONEDAS.MONEDA_DESCRIPCION,INFI_VI_MONEDAS.MONEDA_ID,INFI_VI_MONEDAS.MONEDA_SIGLAS,infi_tb_007_monedas_cambio.MONEDA_CAMBIO_COMPRA,infi_tb_007_monedas_cambio.MONEDA_CAMBIO_VENTA,infi_tb_007_monedas_cambio.MONEDA_FECHA_DESDE,infi_tb_007_monedas_cambio.MONEDA_FECHA_HASTA from INFI_VI_MONEDAS left join infi_tb_007_monedas_cambio on INFI_VI_MONEDAS.MONEDA_ID=infi_tb_007_monedas_cambio.MONEDA_ID where 1=1";
	
	/**Variable que contiene el sql b&aacute;sico para la b&uacute;squeda de las Tasas por moneda*/
	private String strSQLMonedaTasasEdit = "select INFI_VI_MONEDAS.MONEDA_DESCRIPCION,INFI_VI_MONEDAS.MONEDA_ID,INFI_VI_MONEDAS.MONEDA_SIGLAS,infi_tb_007_monedas_cambio.moneda_id_sec,infi_tb_007_monedas_cambio.MONEDA_CAMBIO_COMPRA,infi_tb_007_monedas_cambio.MONEDA_CAMBIO_VENTA,infi_tb_007_monedas_cambio.MONEDA_FECHA_DESDE,infi_tb_007_monedas_cambio.MONEDA_FECHA_HASTA from INFI_VI_MONEDAS left join infi_tb_007_monedas_cambio on INFI_VI_MONEDAS.MONEDA_ID=infi_tb_007_monedas_cambio.MONEDA_ID where 1=1";
	
	
	/**Variable que contiene el sql b&aacute;sico para la b&uacute;squeda de las monedas para agregar a aquellas que no poseen tasas*/
	private String strSQLMonedaTasas0 = "select INFI_VI_MONEDAS.MONEDA_DESCRIPCION,INFI_VI_MONEDAS.MONEDA_ID,INFI_VI_MONEDAS.MONEDA_SIGLAS,infi_tb_007_monedas_cambio.MONEDA_CAMBIO_COMPRA,infi_tb_007_monedas_cambio.MONEDA_CAMBIO_VENTA,infi_tb_007_monedas_cambio.MONEDA_FECHA_DESDE,infi_tb_007_monedas_cambio.MONEDA_FECHA_HASTA from INFI_VI_MONEDAS left join infi_tb_007_monedas_cambio on INFI_VI_MONEDAS.MONEDA_ID=infi_tb_007_monedas_cambio.MONEDA_ID where infi_tb_007_monedas_cambio.MONEDA_CAMBIO_COMPRA is null";
	
	private String strSQLMonedaId = "SELECT MONEDA_SIGLAS,MONEDA_ID,MONEDA_DESCRIPCION,MONEDA_IN_LOCAL," +
	"MONEDA_IN_EXCHANGE,MONEDA_STATUS" +
	" FROM INFI_VI_MONEDAS WHERE MONEDA_ID=";
	
	public MonedaDAO(DataSource ds) {
		super(ds);
	}

	public MonedaDAO(Transaccion transanccion) throws Exception {
		super(transanccion);
	} 
	
			 
	/**Lista todas las monedas activas que se encuentren registradas en la base de datos.
	 * Crea un dataset con la informaci&oacute;n encontrada
	 * @throws lanza una excepci&oacute;n si hubo un error en la consulta*/
	public void listarMonedasActivas() throws  Exception{
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("SELECT * FROM INFI_VI_MONEDAS WHERE MONEDA_STATUS=").append(ConstantesGenerales.STATUS_ACTIVO);
		listarMonedas(strSQL.toString());
	}

	/**Lista todas las monedas activas e inactivas que se encuentren registradas en la base de datos.
	 * Crea un dataset con la informaci&oacute;n encontrada
	 * @throws lanza una excepci&oacute;n si hubo un error en la consulta*/
	public void listar() throws Exception{
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("SELECT case when moneda_status='1' then 'ACTIVA' when moneda_status='0' then 'INACTIVA' end moneda_status,moneda_id,moneda_descripcion,moneda_siglas,");
		strSQL.append("case when moneda_in_local='1' then 'SI' when moneda_in_local='0' then 'NO' end moneda_in_local,");
		strSQL.append("case when moneda_in_exchange='1' then 'SI' when moneda_in_exchange='0' ");
		strSQL.append("then 'NO' end moneda_in_exchange FROM INFI_VI_MONEDAS ORDER BY MONEDA_DESCRIPCION");
		listarMonedas(strSQL.toString());		
	}
	
	/**
	 * M&eacute;todo que busca si una moneda est&aacute; establecida como local en el sistema.
	 * @param monedaId
	 * @return true si la moneda es local, false si no lo es
	 * @throws Exception Arroja una excepci&oacute;n si no puede ejecutar la consulta
	 */
	public boolean listarIsMonedaLocal(String monedaId) throws Exception{
		boolean isLocal = false;
		//ITS-3227 Incidencia servidor de Rentabilidad caido NM25287 11-Jul-16
		/*
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT MONEDA_IN_LOCAL FROM INFI_VI_MONEDAS WHERE MONEDA_ID = ");
		sql.append("'" + monedaId + "'");
		
		try {
			conn = dataSource.getConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql.toString());
			
			if(resultSet.next()){
				if(resultSet.getInt("MONEDA_IN_LOCAL") == ConstantesGenerales.VERDADERO){
					isLocal = true;
				}
			}		

		} catch (Exception e) {
			throw new Exception("Error en la b&uacute;squeda de moneda local: " + e.getMessage());
		}finally{
			this.closeResources();
			this.cerrarConexion();
		}*/
		if(monedaId.equalsIgnoreCase(ConstantesGenerales.SIGLAS_MONEDA_LOCAL)){
			isLocal=true;
		}		
		return isLocal;
	}
	
	public String listarIdMonedaLocal() throws Exception{
		String idMoneda = null;
		//ITS-3227 Incidencia servidor de Rentabilidad caido NM25287 11-Jul-16
		/*String sql = "SELECT MONEDA_ID FROM INFI_VI_MONEDAS WHERE MONEDA_IN_LOCAL ="+ConstantesGenerales.VERDADERO;
		
		try {
			conn = dataSource.getConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql);
			if(resultSet.next()){
				idMoneda = resultSet.getString(1);
			}
		} catch (Exception e) {
			throw new Exception("Error en la b&uacute;queda del id de la moneda: " + e.getMessage());
		}finally{
			this.closeResources();
			this.cerrarConexion();
		}	*/	
		idMoneda =ConstantesGenerales.SIGLAS_MONEDA_LOCAL;
		return idMoneda;
	}
	

	public void listar(String monedaId) throws Exception{
		listarMonedas(strSQLMonedaId.concat("'").concat(monedaId).concat("'"));		
	}

	/**M&eacute;todo que lista las monedas registradas en la base de datos en base a un SQL recibido
	 * @param sql consulta que debe ejecutar y crear el dataSet
	 * @throws lanza una excepci&oacute;n si hay un error en la consulta */
	private void listarMonedas(String sql) throws Exception{
		try {
			dataSet = db.get(this.dataSource, sql);
			//if(1==1) throw new Exception("Prueba Error Rentabilidad");
		} catch (Exception e) {
			//ITS-3227 Incidencia servidor de Rentabilidad caido NM25287 11-Jul-16
			if(rentabilidadOffline())	{
				dataSet=dataSetOffLineMonedas();
			}else{
				throw new Exception(Mensajes.getInstancia().getMensaje("Error en la búsqueda de monedas"));
			}			
		}
	}
		
	/**Devuelve el pr&oacute;ximo objeto de la consulta efectuada*/
	public Object moveNext() throws Exception {
		boolean bolPaso = false;
        Moneda moneda = new Moneda();
        try {
            //Si no es ultimo registro arma el objeto            
            if ((resultSet!=null)&&(!resultSet.isAfterLast())){            	
                bolPaso = true;
                moneda.setId(resultSet.getString("MONEDA_ID"));
                moneda.setDescripcion(resultSet.getString("MONEDA_DESCRIPCION"));
                moneda.setMonedaCambio(resultSet.getInt("MONEDA_IN_EXCHANGE")==ConstantesGenerales.VERDADERO?true:false);
                moneda.setMonedaLocal(resultSet.getInt("MONEDA_IN_LOCAL")==ConstantesGenerales.VERDADERO?true:false);
                moneda.setSiglas(resultSet.getString("MONEDA_SIGLAS"));
                resultSet.next();
            }
        } catch (SQLException e) {
            super.closeResources();
            throw new Exception("Error al intentar crear el objeto Moneda ");
        }
        if (bolPaso) {
            return moneda;
        } else {
            super.closeResources();
            return null;
        }		
	}	
	
	/**
	 * Lista los datos de la moneda local
	 * @throws Exception
	 */
	/*public void listarMonedaLocal() throws Exception{
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT * FROM INFI_VI_MONEDAS WHERE moneda_in_local ="+ConstantesGenerales.VERDADERO);
		
		dataSet = db.get(dataSource, sql.toString());		
	}*/
	
	/**
	 * Crea un dataSet con los datos más usados de las monedas
	 * ITS-3227 Incidencia servidor de Rentabilidad caido NM25287 11-Jul-16
	 * @return
	 * @throws Exception
	 */
	public DataSet dataSetOffLineMonedas() throws Exception{
		DataSet monedas = new DataSet();			
		monedas.append("moneda_status", java.sql.Types.VARCHAR);
		monedas.append("moneda_id", java.sql.Types.VARCHAR);	
		monedas.append("moneda_descripcion", java.sql.Types.VARCHAR);	
		monedas.append("moneda_siglas", java.sql.Types.VARCHAR);	
		monedas.append("moneda_in_local", java.sql.Types.VARCHAR);
		monedas.append("moneda_in_exchange", java.sql.Types.VARCHAR);
		monedas.addNew();
		monedas.setValue("moneda_status", "ACTIVA");
		monedas.setValue("moneda_id", "EUR");		
		monedas.setValue("moneda_descripcion", "EUR");	
		monedas.setValue("moneda_siglas", "EUR");
		monedas.setValue("moneda_in_local", "NO");		
		monedas.setValue("moneda_in_exchange", "NO");	
		monedas.addNew();
		monedas.setValue("moneda_status", "ACTIVA");
		monedas.setValue("moneda_id", "USD");		
		monedas.setValue("moneda_descripcion", "USD");	
		monedas.setValue("moneda_siglas", "USD");
		monedas.setValue("moneda_in_local", "NO");		
		monedas.setValue("moneda_in_exchange", "NO");	
		monedas.addNew();
		monedas.setValue("moneda_status", "ACTIVA");
		monedas.setValue("moneda_id", "VES");//nm33635 y nm32265		Fecha 25-04-2018
		monedas.setValue("moneda_descripcion", "VES");	//nm33635 y nm32265
		monedas.setValue("moneda_siglas", "VES");//nm33635 y nm32265
		/*monedas.setValue("moneda_id", "VEF");//nm33635		
		monedas.setValue("moneda_descripcion", "VEF");	
		monedas.setValue("moneda_siglas", "VEF");*/
		monedas.setValue("moneda_in_local", "SI");		
		monedas.setValue("moneda_in_exchange", "NO");
		return monedas;	
	}
	
	/**
	 * Consulta el parametro RENTABILIDAD_OFFLINE de la tabla INFI_TB_002_PARAM_TIPOS e indica si rentabilidad está activo
	 * (1 activo, 0 inactivo)
	 * ITS-3227 Incidencia servidor de Rentabilidad caido NM25287 11-Jul-16
	 * @return
	 * @throws Exception
	 */
	public boolean rentabilidadOffline() throws Exception{
		StringBuffer sql = new StringBuffer();
		
		sql.append("select PARVAL_VALOR from INFI_TB_002_PARAM_TIPOS where PARTIP_NOMBRE_PARAMETRO='"+ParametrosSistema.RENTABILIDAD_ONLINE+"'");
		
		dataSet = db.get(dataSource, sql.toString());	
		
		if(dataSet.next()){
			if(dataSet.getValue("PARVAL_VALOR").equalsIgnoreCase("1")){
				return false;
			}else{
				return true;
			}				
		}
		return false;
	}
	
}


package com.bdv.infi.dao;
import java.io.File;
import java.sql.Blob;
import java.sql.CallableStatement;

import javax.sql.DataSource;

import com.bancovenezuela.comun.logic.interfaces.ConstantesGenerales;


import megasoft.Logger;
import megasoft.db;
public class CierreSistemaDAO extends com.bdv.infi.dao.GenericoDAO {
		
	public CierreSistemaDAO(DataSource ds) {
		super(ds);
	}

	public void listarFechaSistema() throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT TO_DATE(C.FECHA_SISTEMA,'dd-MM-rrrr') as FECHA_SISTEMA FROM INFI_TB_906_CIERRE C");//C.FECHA_SISTEMA
		dataSet = db.get(dataSource, sb.toString());		
	}
	
	public void listarDatosCierreSistema() throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT PASO_EN_EJECUCION, CIERRE_ACTIVO, TO_CHAR(C.FECHA_SISTEMA,'dd-MM-yyyy') AS FECHA_SISTEMA, TO_CHAR(C.FECHA_PRE_CIERRE,'dd-MM-yyyy') as FECHA_PRE_CIERRE FROM INFI_TB_906_CIERRE C");//C.FECHA_SISTEMA
		dataSet = db.get(dataSource, sb.toString());		
	}
	
	public String actualizarFechaPreCierre(String fechaPreCierre,long diasTranscurridos,int activacionCierre) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("UPDATE INFI_TB_906_CIERRE C SET C.FECHA_PRE_CIERRE=TO_DATE('").append(fechaPreCierre).append("','").append(ConstantesGenerales.FORMATO_FECHA_SYSDATE).append("'),C.CIERRE_ACTIVO=").append(activacionCierre);
		
		if(diasTranscurridos>0){		
			sb.append(",C.DIAS_TRANSCURRIDOS=").append(diasTranscurridos);
		}
		return sb.toString();
	}
	
	public void consultaCierreSistema() throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT DECODE(C.CIERRE_ACTIVO,0,'INACTIVO',1,'ACTIVO') AS CIERRE,CASE  WHEN c.INDICADOR_FALLA=0 THEN 'NO' WHEN c.INDICADOR_FALLA>0 THEN 'SI' END AS falla_cierre,'' as tipo_cierre,C.* FROM INFI_TB_906_CIERRE C");
		dataSet = db.get(dataSource, sb.toString());		
	}
	
	public String activarDesacticarProcesoCierre(int activar) throws Exception{
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("UPDATE INFI_TB_906_CIERRE C SET C.CIERRE_ACTIVO=").append(activar);
				
		return sb.toString();
	}
	

	/**
	 * Realiza la llamada al stored procedure correspondiente Cierre de Sistema 
	 * @param ip
	 * @param usuario
	 * @param fechaPreCierre
	 * @throws Exception
	 */
	public void cerrarSistema(String ip, String usuario, String fechaPreCierre, int statusCierre,String tipoEjecucion) throws Exception{
		
		
		try {
			//conn = this.dataSource.getConnection();
			conn = conn==null?this.dataSource.getConnection():conn;	
			conn.setAutoCommit(false);
			CallableStatement procedimientoAlmacenado = conn.prepareCall("{ call PROCESO_CIERRE.CIERRE_SISTEMA(?,?,?,?,?) }");
			// cargar parametros al SP
			procedimientoAlmacenado.setString(1, fechaPreCierre);
			procedimientoAlmacenado.setInt(2, statusCierre);			
			procedimientoAlmacenado.setString(3, usuario);
			procedimientoAlmacenado.setString(4, ip);
			procedimientoAlmacenado.setString(5, tipoEjecucion);
			// ejecutar el SP
			procedimientoAlmacenado.execute();
			// confirmar si se ejecuto sin errores
			conn.commit();					
		
		} catch (Exception e) {
			if(conn!=null){conn.rollback();}			
			throw new Exception("Error llamando al stored procedure de cierre del sistema: " + e.getMessage());
		} finally {
			this.closeResources();
			this.cerrarConexion();
		}		
	}

	/**
	 * Verifica si el proceso de cierre de sistema se encuentra activo
	 * @return
	 * @throws Exception
	 */
	public boolean isProcesoCierreActivo() throws Exception{
		boolean flag=false;
		
		StringBuffer sql=new StringBuffer();
		
		sql.append("SELECT  1 AS CIERRE_ACTIVO FROM INFI_TB_906_CIERRE WHERE CIERRE_ACTIVO=1");
		
		dataSet = db.get(dataSource, sql.toString());
		
		if(dataSet.count()>0){
			flag=true;
		}
		
		return flag;
	}
	
	/**
	 * Verifica si existe una falla en el proceso de cierre de sistema
	 * @return
	 * @throws Exception
	 */
	public boolean existeFallaProcesoCierre() throws Exception{
		boolean flag=false;
		
		StringBuffer sql=new StringBuffer();
		
		sql.append("SELECT 1 AS FALLA_CIERRE FROM INFI_TB_906_CIERRE WHERE INDICADOR_FALLA =1");
		
		dataSet = db.get(dataSource, sql.toString());
		
		if(dataSet.count()>0){
			flag=true;
		}
		
		return flag;
	}
	
	/**
	 * Actualiza el indicador de fallas de cierre de sistema en cero (0). Dejandolo sin fallas para poder iniciarlo nuevamente
	 * @throws Exception
	 */
	public String limpiarCierreActivoConFallas() throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("UPDATE INFI_TB_906_CIERRE SET ");
		sb.append("INDICADOR_FALLA = 0");//se deja indicando que no hay fallas
				
		return sb.toString();
	}
	
	/**
	 * Realiza la llamada al stored procedure correspondiente Cierre de Sistema 
	 * @param ip
	 * @param usuario
	 * @param fechaPreCierre
	 * @throws Exception
	 */
	public Blob obtenerArchivoCierreSistema(String nombreArchivo,String rutaArchivo) throws Exception{
		
		Blob archivoBlob = null;
		
		try {
			//conn = this.dataSource.getConnection();
			conn = conn==null?this.dataSource.getConnection():conn;	
			
			CallableStatement procedimientoAlmacenado = conn.prepareCall("{ call PROCESO_CIERRE.OBTENER_ARCHIVO_CIERRE(?,?,?) }");
			// cargar parametros al SP
			procedimientoAlmacenado.setString(1, nombreArchivo);				
			procedimientoAlmacenado.registerOutParameter(2,java.sql.Types.BLOB);
			procedimientoAlmacenado.setString(3, rutaArchivo);
			// ejecutar el SP
			procedimientoAlmacenado.execute();
			archivoBlob = procedimientoAlmacenado.getBlob(2);			
		
		} catch (Exception e) {
			Logger.error("Error llamando al stored procedure de cierre del sistema: ", e.getMessage());
			throw new Exception("Error llamando al stored procedure de cierre del sistema: " + e);
		} finally {
			this.closeResources();
			this.cerrarConexion();
		}	
		
		return archivoBlob;
	}



	@Override
	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
			
}   

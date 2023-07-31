package com.bdv.infi.dao;
import java.math.BigDecimal;

import javax.sql.DataSource;

import megasoft.db;

import com.bdv.infi.data.ConfiguracionTasa;
public class ConfiguracionTasaDAO extends com.bdv.infi.dao.GenericoDAO {
		
	public ConfiguracionTasaDAO(DataSource ds) {
		super(ds);
	}

	
	public void listarTasaPorFechaSistema(String fecha) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT TO_DATE(C.FECHA_SISTEMA,'dd-MM-rrrr') as FECHA_SISTEMA FROM INFI_TB_906_CIERRE C");//C.FECHA_SISTEMA
		dataSet = db.get(dataSource, sb.toString());		
	}
	
	/**
	 * Verifica si existe una tasa Buen Valor configurada para la fecha de sistema, en estatus aprobada y tipo de producto y transacción específicos. Almacena la información en un DataSet
	 * @return true si existe registro, false en caso contrario
	 * @throws Exception
	 */
	public boolean existeTasaActualPorFechaSistema(String tipoProducto, String transaccion) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT TASA FROM INFI_TB_816_CONFIG_TASA t");
		sb.append(" WHERE TO_DATE(t.FECHA,'dd-MM-rrrr') = (SELECT TO_DATE(FECHA_SISTEMA,'dd-MM-rrrr') FROM INFI_TB_906_CIERRE) ");
		sb.append(" AND t.APROBADO = 1");
		sb.append(" AND TIPO_PRODUCTO_ID = '").append(tipoProducto).append("'");
		sb.append(" AND TRANSA_ID = '").append(transaccion).append("'");
		dataSet = db.get(dataSource, sb.toString());	
		
		if(dataSet.next()){
			return true;
		}else return false;
	}

	
	public String insertar(ConfiguracionTasa config) {
	
		StringBuffer sb = new StringBuffer();
		sb.append("INSERT INTO INFI_TB_816_CONFIG_TASA (CONFIG_TASA_ID,FECHA,NM_REGISTRO_TASA,TASA,TIPO_PRODUCTO_ID,TRANSA_ID) VALUES (");
		sb.append("INFI_SQ_816.NEXTVAL,");
		sb.append("TO_DATE('"+config.getFechaTasa()).append("','dd-MM-yyyy'),");
		sb.append("'"+config.getNmUsuarioCreador()).append("',");
		sb.append(config.getTasa()).append(",");
		sb.append("'"+config.getTipoProducto()).append("',");
		sb.append("'"+config.getTransaccionId()).append("')");
		
		return sb.toString();		
	}

	public String aprobarTasa(ConfiguracionTasa config) {
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("UPDATE INFI_TB_816_CONFIG_TASA CT SET CT.APROBADO=1, CT.FECHA_APROBACION=SYSDATE, ").append("CT.NM_APROBACION_TASA='").append(config.getNmUsuarioAprobador()).append("' ");
		sb.append("WHERE CT.CONFIG_TASA_ID=").append(config.getIdTasa());
				
		return sb.toString();		
	}

	public boolean tasaExiste(ConfiguracionTasa config)throws Exception {

		StringBuffer sb = new StringBuffer();		
		sb.append("SELECT * FROM INFI_TB_816_CONFIG_TASA CT ");
		sb.append("WHERE CT.FECHA=TO_DATE('").append(config.getFechaTasa()).append("','dd-MM-yyyy') ").append(" AND CT.TRANSA_ID='").append(config.getTransaccionId()).append("'  AND CT.TIPO_PRODUCTO_ID='").append(config.getTipoProducto()).append("' ");//AND CT.APROBADO=1

		//System.out.println("tasaExiste --> " + sb.toString());
		dataSet = db.get(dataSource, sb.toString());
										
		if(dataSet.count()>0){
			return true;			
		}else {
			return false;			
		}
	}
	
	public void consultarTasa(ConfiguracionTasa config)throws Exception {

		StringBuffer sb = new StringBuffer();		
		sb.append("SELECT * FROM INFI_TB_816_CONFIG_TASA CT ");
		sb.append("WHERE CT.FECHA=TO_DATE('").append(config.getFechaTasa()).append("','dd-MM-yyyy') ");
		
		if(config.getTransaccionId()!=null && !config.getTransaccionId().equals("")){		
			sb.append(" AND CT.TRANSA_ID='").append(config.getTransaccionId()).append("'");	
		}
		
		if(config.getTipoProducto()!=null && !config.getTipoProducto().equals("")){
			sb.append("  AND CT.TIPO_PRODUCTO_ID='").append(config.getTipoProducto()).append("' ");	
		}
		
		dataSet = db.get(dataSource, sb.toString());											
	}
		
	
	public void listarTasaPorId(String idTasa) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT TO_CHAR(CT.FECHA,'dd-MM-yyyy')AS FECHA_TASA,CT.* FROM INFI_TB_816_CONFIG_TASA CT WHERE CT.CONFIG_TASA_ID=").append(idTasa);
		
		dataSet = db.get(dataSource, sb.toString());		
	}
	
	public String modificacionTasa(String idTasa,String nmUsuario,BigDecimal montoTasa){
		
		StringBuffer sql=new StringBuffer();
		
		sql.append("UPDATE INFI_TB_816_CONFIG_TASA CT SET CT.TASA=").append(montoTasa).append(", CT.NM_APROBACION_TASA=NULL, CT.NM_MODIFICACION_TASA='").append(nmUsuario).append("', ");
		sql.append("CT.APROBADO=0,CT.FECHA_APROBACION=NULL WHERE CT.CONFIG_TASA_ID=").append(idTasa);
		
		return sql.toString();
	}
	/**
	 * Realiza la llamada al stored procedure correspondiente Cierre de Sistema 
	 * @param ip
	 * @param usuario
	 * @param fechaPreCierre
	 * @throws Exception
	 */
	
	@Override
	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
			
}   

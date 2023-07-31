package com.bdv.infi_toma_orden.dao;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.bdv.infi_toma_orden.data.TransferenciaSistemas;

public class TransaccionSistemaDAO extends GenericoDAO {
	
	
	private Logger logger = Logger.getLogger(TransaccionSistemaDAO.class);
	/**
	 * Constructor de la clase
	 * @param nombreDataSource : nombre que se obtiene del ambiente de ejecucion de los WebService
	 * @param dso : DataSource instanciado por clases que se ejecutan en ambientes Web
	 */
	public TransaccionSistemaDAO (String nombreDataSource, DataSource dso) {
		super(nombreDataSource, dso);
	}
	
	/***
	 * lista el serial asociado a la transacci&oacute;n financiera
	 * @param
	 * String trnfid: id de transacci&oacute;n financiera
	 * String SistemaID: id de sistema
	 * String bloterID: id de bloter
	 * String unidadInversion: id de la unidad de inversi&oacute;n. Si no es necesario se debe enviar null
	 * **/
	public TransferenciaSistemas listarCodigo(String trnfid, String SistemaID, String bloterID, long unidadInversion)throws Exception{

		TransferenciaSistemas seriales = new TransferenciaSistemas();
		String serialTransaccionF = "";
		StringBuffer sql=new StringBuffer();
		sql.append("select * from infi_tb_801_trf_sistema where 1=1 ");
		
		if(trnfid!=null && !trnfid.equals("")){
			sql.append(" and trnfin_id='"+trnfid+"' ");
		}
		/*if(SistemaID!=null && !SistemaID.equals("")){
			sql.append(" and sistema_id='"+SistemaID+"' ");
		}*/
		if(bloterID!=null && !bloterID.equals("")){
			sql.append(" and bloter_id='"+bloterID+"' ");
		}
		
		if (unidadInversion > 0){
			sql.append(" and undinv_id="+unidadInversion+" ");
		}
		
		try {
			conn = dso.getConnection();			
			statement = conn.createStatement();
			resultQuery = statement.executeQuery(sql.toString());			
			
			if(resultQuery.next()){
				seriales.setSerial(resultQuery.getString("codigo_trnf"));
				seriales.setBloterId(resultQuery.getString("bloter_id"));
				seriales.setCentroContable(resultQuery.getString("centro_contable"));
				seriales.setCodigoUnico(resultQuery.getLong("codigo_unico"));
				seriales.setSistemaID(resultQuery.getString("sistema_id"));
				seriales.setTrnfinId(resultQuery.getString("trnfin_id"));
				seriales.setUnidadnversion(resultQuery.getLong("undinv_id"));	
				seriales.setCodigoOperacionBloqueo(verificarNulidad(resultQuery.getString("codigo_operacion_blo")));
				seriales.setCodigoOperacionDebito(verificarNulidad(resultQuery.getString("codigo_operacion")));
				seriales.setCodigoOperacionCredito(verificarNulidad(resultQuery.getString("codigo_operacion_cre")));
	 		} 

		} catch (Exception e) {
			logger.error("Error al buscar el serial de la transacci&oacute;n financiera   "+e.getMessage());
			throw new Exception("Error al buscar el serial de la transacci&oacute;n financiera");
		} finally {
			if (resultQuery != null){
				resultQuery.close();
				cerrarConexion();
			}
		}
		logger.info("SERIAL RETORNADO "+ serialTransaccionF);
		return seriales;
	}
	
	/**Verifica si el parametro es null, de ser así regrega blanco*/
	private String verificarNulidad(String valor){
		return (valor==null?"":valor);
	}
	
	
}

package com.bdv.infi.dao;    

import javax.sql.DataSource;

import megasoft.Logger;
import megasoft.db;

import com.bancovenezuela.comun.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.data.EnvioMail;
import com.bdv.infi.logic.interfaces.PlantillasMailTipos;

/** 
 * Clase usada para la l&oacute;gica de inserci&oacute;n, modificaci&oacute;n y recuperaci&oacute;n de informacion de correos en la base de datos
 */

public class EnvioMailDAO extends com.bdv.infi.dao.GenericoDAO {

	
	//private Logger logger = Logger.getLogger(AprobacionOrdenesDAO.class);
	
	public EnvioMailDAO(DataSource ds) {
		super(ds);
	}

	@Override
	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**Obtiene el siguiente valor de la secuencia de la tabla
	 * @param nombreSecuencia
	 * @throws Exception
	 */
	public synchronized long getSecuencia(String nombreSecuencia) throws Exception{
		long sq = 0;
		String sql = "select "+nombreSecuencia+".NEXTVAL as correo_id FROM dual";
		dataSet = db.get(dataSource, sql);
		if(dataSet!=null && dataSet.count()>0){
			dataSet.next();
			sq = Long.parseLong(dataSet.getValue("correo_id"));
		}
		return sq;
	}
	
	
	/**Obtiene el valor actual de la secuencia de la tabla
	 * @param nombreSecuencia
	 * @throws Exception
	 */
	public long getSecuenciaActual(String nombreSecuencia) throws Exception{
		long sq = 0;
		String sql = "select "+nombreSecuencia+".CURRVAL as correo_id FROM dual";
		dataSet = db.get(dataSource, sql);
		if(dataSet!=null && dataSet.count()>0){
			dataSet.next();
			sq = Long.parseLong(dataSet.getValue("correo_id"));
		}
		return sq;
	}
	
	
	/**
	 * Metodo de busqueda de instrumentos financieros y vehiculos por tipo de transaccion en la tabla INFI_TB_226_ORDENES_APROBACION
	 * @param idsCorreos (concatenados por coma)
	 * @param tipoDest
	 * @param cicloStatus
	 * @return DataSet
	 * @throws Exception
	 */
	public String listarCorreos(String idsCorreos, boolean IN, String tipoDest, String... cicloStatusPlantillaAreauOrdenUISQLFechaStatuscorr) throws Exception{		
		String ciclo = "", status = "";
		boolean cliente = false, consulta = false;
		StringBuffer sql=new StringBuffer();
		//sql.append("SELECT em.*, pm.PLANTILLA_MAIL_ID, pm.PLANTILLA_MAIL_NAME, pm.REMITENTE, pm.TIPO_DESTINATARIO, decode(pm.TIPO_DESTINATARIO,'C','CLIENTE','F','FUNCIONAL') as TIPO_DEST_NAME, pm.ASUNTO, pm.CUERPO, pm.ORDSTA_ID, pm.TRANSA_ID, os.ORDSTA_NOMBRE , tr.TRANSA_DESCRIPCION ");
		sql.append("SELECT em.*, pm.PLANTILLA_MAIL_ID, pm.PLANTILLA_MAIL_NAME, pm.REMITENTE, pm.TIPO_DESTINATARIO, decode(pm.TIPO_DESTINATARIO,'C','CLIENTE','F','FUNCIONAL') as TIPO_DEST_NAME, pm.ASUNTO, pm.CUERPO, pm.EVENTO_ID, pm.BLOQUE_ITERADO ");
		if(tipoDest==null || tipoDest.equals("")){
			sql.append(" FROM INFI_TB_228_ENVIO_MAIL em, INFI_TB_223_PLANTILLA_MAIL pm ");
			sql.append(" where em.PLANTILLA_MAIL_ID=pm.PLANTILLA_MAIL_ID " );
		}else{
			if(tipoDest.equals(PlantillasMailTipos.TIPO_DEST_CLIENTE_COD)){
				cliente = true;
				sql.append(", cte.CLIENT_NOMBRE, cte.TIPPER_ID, cte.CLIENT_CEDRIF, cte.CLIENT_CORREO_ELECTRONICO, ui.UNDINV_NOMBRE, ui.UNDINV_ID ");
				sql.append(" FROM INFI_TB_228_ENVIO_MAIL em, INFI_TB_201_CTES cte, INFI_TB_223_PLANTILLA_MAIL pm, INFI_TB_204_ORDENES ord, INFI_TB_106_UNIDAD_INVERSION ui ");
				sql.append(" where em.CLIENT_AREA_ID=cte.CLIENT_ID and em.PLANTILLA_MAIL_ID=pm.PLANTILLA_MAIL_ID and em.ORDENE_ID=ord.ORDENE_ID and ord.UNIINV_ID=ui.UNDINV_ID " );
			}else{
				if(tipoDest.equals(PlantillasMailTipos.TIPO_DEST_FUNCIONAL_COD)){
					sql.append(", pma.PLANT_MAIL_AREA_ID, pma.PLANT_MAIL_AREA_NAME, pma.DESTINATARIO ");
					sql.append(" FROM INFI_TB_228_ENVIO_MAIL em, INFI_TB_224_PLANT_MAIL_AREA pma, INFI_TB_223_PLANTILLA_MAIL pm ");
					sql.append(" where em.PLANTILLA_MAIL_ID=pm.PLANTILLA_MAIL_ID and pm.PLANTILLA_MAIL_ID=pma.PLANTILLA_MAIL_ID and em.CLIENT_AREA_ID = pma.PLANT_MAIL_AREA_ID ");
				}
			}
		}
		if(idsCorreos!=null){
			sql.append(" AND em.CORREO_ID ");
			if(IN) sql.append("IN ");
			else sql.append("NOT IN ");
			sql.append("(").append(idsCorreos).append(")");
		}
		if(cicloStatusPlantillaAreauOrdenUISQLFechaStatuscorr.length>0){
			if(cicloStatusPlantillaAreauOrdenUISQLFechaStatuscorr[0]!=null){ //ciclo
				sql.append(" AND em.CICLO_ID=").append(cicloStatusPlantillaAreauOrdenUISQLFechaStatuscorr[0]);
				ciclo = cicloStatusPlantillaAreauOrdenUISQLFechaStatuscorr[0];
			}
			if(cicloStatusPlantillaAreauOrdenUISQLFechaStatuscorr.length>1 && cicloStatusPlantillaAreauOrdenUISQLFechaStatuscorr[1]!=null){ //status
				sql.append(" AND em.STATUS='").append(cicloStatusPlantillaAreauOrdenUISQLFechaStatuscorr[1]).append("'");
				status = cicloStatusPlantillaAreauOrdenUISQLFechaStatuscorr[1];
			}
			if(cicloStatusPlantillaAreauOrdenUISQLFechaStatuscorr.length>2 && cicloStatusPlantillaAreauOrdenUISQLFechaStatuscorr[2]!=null){ //plantilla Id
				sql.append(" AND em.PLANTILLA_MAIL_ID=").append(cicloStatusPlantillaAreauOrdenUISQLFechaStatuscorr[2]);
			}
			if(cicloStatusPlantillaAreauOrdenUISQLFechaStatuscorr.length>3 && cicloStatusPlantillaAreauOrdenUISQLFechaStatuscorr[3]!=null){ //area Id
				if(cliente){
					sql.append(" AND em.ORDENE_ID IN (").append(cicloStatusPlantillaAreauOrdenUISQLFechaStatuscorr[3]).append(")");
//					sql.append(" AND em.ORDENE_ID=").append(cicloStatusPlantillaAreauOrdenUISQLFechaStatuscorr[3]);
				}else{
					sql.append(" AND em.CLIENT_AREA_ID IN (").append(cicloStatusPlantillaAreauOrdenUISQLFechaStatuscorr[3]).append(")");
					//sql.append(" AND em.CLIENT_AREA_ID=").append(cicloStatusPlantillaAreauOrdenUISQLFechaStatuscorr[3]);
				}
			}
			if(cicloStatusPlantillaAreauOrdenUISQLFechaStatuscorr.length>4 && cicloStatusPlantillaAreauOrdenUISQLFechaStatuscorr[4]!=null){ //UI
				sql.append(" AND ui.UNDINV_ID=").append(cicloStatusPlantillaAreauOrdenUISQLFechaStatuscorr[4]);
			}
			if(cicloStatusPlantillaAreauOrdenUISQLFechaStatuscorr.length>5 && cicloStatusPlantillaAreauOrdenUISQLFechaStatuscorr[5]!=null){ //SQL (consulta)
				consulta = true;
				sql = new StringBuffer();
				sql.append(cicloStatusPlantillaAreauOrdenUISQLFechaStatuscorr[5]);
			}
			if(cicloStatusPlantillaAreauOrdenUISQLFechaStatuscorr.length>7 && cicloStatusPlantillaAreauOrdenUISQLFechaStatuscorr[6]!=null && cicloStatusPlantillaAreauOrdenUISQLFechaStatuscorr[7]!=null){ //fecha envio
				sql.append(" AND trunc(em.FECHA_REGISTRO) BETWEEN to_date('").append(cicloStatusPlantillaAreauOrdenUISQLFechaStatuscorr[6]).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("')");
				sql.append(" AND to_date('").append(cicloStatusPlantillaAreauOrdenUISQLFechaStatuscorr[7]).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("')");
			}
			if(cicloStatusPlantillaAreauOrdenUISQLFechaStatuscorr.length>8 && cicloStatusPlantillaAreauOrdenUISQLFechaStatuscorr[8]!=null){ //Status Correo
				sql.append(" AND em.STATUS='").append(cicloStatusPlantillaAreauOrdenUISQLFechaStatuscorr[8]).append("'");
			}
		}
		if(!consulta) 
		sql.append(" order by em.CICLO_ID DESC, em.STATUS, em.ORDENE_ID");
		dataSet = db.get(dataSource, sql.toString());
		if(dataSet!=null) Logger.debug(this, "primera llamada SQL dataset count----------- "+dataSet.count());
		
		if( !consulta && (tipoDest==null || tipoDest.equals("")) && dataSet.count()>0 ){
			Logger.debug(this, "entra a segunda llamada SQL");
			dataSet.first();
			dataSet.next();
			listarCorreos(null, false, dataSet.getValue("TIPO_DESTINATARIO"), ciclo, status);
		}
Logger.info(this, "SQL lista correos 228-----------\n"+sql.toString());
		return sql.toString();
	}
	
	
	/**
	 * Metodo de registro de correos en la tabla INFI_TB_228_ENVIO_MAIL
	 * @param EnvioMail
	 * @return
	 * @throws Exception
	 */
	public String insertarCorreos(EnvioMail em) throws Exception{
		//SimpleDateFormat sdf = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA1);
		StringBuffer sql=new StringBuffer();
		
		sql.append("INSERT INTO INFI_TB_228_ENVIO_MAIL");
		sql.append(" (CORREO_ID, PLANTILLA_MAIL_ID, CLIENT_AREA_ID, DIRECCION_CORREO, STATUS, CICLO_ID, FECHA_REGISTRO, ORDENE_ID, CONTENIDO)");
		sql.append(" VALUES (");
		//sql.append("INFI_SQ_228.NEXTVAL, ");//CORREO_ID
		sql.append(em.getIdCorreo()).append(", ");//CORREO_ID
		sql.append(em.getIdPlantilla()).append(", ");//PLANTILLA_MAIL_ID
		if(em.getIdCliente()!=(long)0){
			sql.append(em.getIdCliente()).append(", ");//CLIENT_AREA_ID
		}else{
			if(em.getIdArea()!=(long)0){
				sql.append(em.getIdArea()).append(", ");//CLIENT_AREA_ID
			}
		}
		sql.append("'").append(em.getDireccionCorreo()).append("', ");//DIRECCION_CORREO
		sql.append("'").append(em.getStatus()).append("', ");//STATUS
		sql.append(em.getIdCicloEjecucion()).append(", ");//CICLO_ID
		sql.append("SYSDATE, ");//FECHA_REGISTRO
		//sql.append("TO_DATE('"+sdf.format(em.getFechaRegistro())).append("','").append(ConstantesGenerales.FORMATO_FECHA1).append("'),");//FECHA_REGISTRO
		if(em.getIdOrden()!=(long)0){
			sql.append(em.getIdOrden()+", ");//ORDENE_ID
		}else{
			sql.append("NULL, ");
		}
		if(em.getContenido()!=null && !em.getContenido().equals("")){
			sql.append(em.getContenido());
		}else{
			sql.append("''");
		}
		sql.append(")");
		
		return sql.toString();
	}
	
	/**
	 * Metodo de eliminacion de correos en la tabla INFI_TB_228_ENVIO_MAIL
	 * @param cicloId
	 * @param status
	 * @param idCorreos
	 * @return
	 * @throws Exception
	 */
	public String eliminarCorreos(long cicloId, String status, boolean NOT, String... idCorreos) throws Exception{
		StringBuffer sql=new StringBuffer();
		
		sql.append("DELETE INFI_TB_228_ENVIO_MAIL WHERE ");
		sql.append(" CICLO_ID=").append(cicloId);
		if(status!=null){
			sql.append(" AND STATUS='").append(status).append("'");
		}
		if(idCorreos.length>0 && idCorreos[0]!=null){
			sql.append(" AND CORREO_ID ");
			if(NOT) sql.append("NOT ");
			sql.append("IN (");
			for(int i=0; i<idCorreos.length; i++){
				sql.append(idCorreos[i]);
				if(i<idCorreos.length-1) sql.append(", ");
			}
			sql.append(")");
		}
		
		return sql.toString();
	}
	
	/**
	 * Metodo de busqueda de registros por tipo de transaccion en la tabla INFI_TB_226_ORDENES_APROBACION
	 * @param ordenAprobacion
	 * @return DataSet
	 * @throws Exception
	 */
	//nm29643 infi_TTS_466 Actualizacion proceso de envio de correos
	public String updateStatusCorreo(EnvioMail em, boolean fechaEnvio, String statusAsetear) throws Exception{		
		StringBuffer sql = new StringBuffer();		
		sql.append("UPDATE INFI_TB_228_ENVIO_MAIL em SET");
		if(statusAsetear==null){
			sql.append(" em.STATUS='").append(em.getStatus()).append("'");
		}else{
			sql.append(" em.STATUS='").append(statusAsetear).append("'");
		}
		if(fechaEnvio) sql.append(", em.FECHA_ENVIO=SYSDATE");
		if(em.getContenido()!=null && !em.getContenido().equals("")){
			if(em.getContenido().length()>4000) em.setContenido(em.getContenido().substring(0, com.bdv.infi.logic.interfaces.ConstantesGenerales.tamMaxContenido));
			sql.append(", em.CONTENIDO='").append(em.getContenido()).append("'");
		}
		sql.append(" WHERE em.CORREO_ID=").append(em.getIdCorreo());
		//System.out.println("updateStatusCorreo -------> "  + sql.toString());
		return sql.toString();
	}
	

}

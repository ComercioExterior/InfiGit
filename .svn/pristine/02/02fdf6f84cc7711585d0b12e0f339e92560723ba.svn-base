package com.bdv.infi.dao;

import java.util.ArrayList;

import javax.sql.DataSource;

import megasoft.DataSet;
import megasoft.db;

import org.apache.log4j.Logger;

import com.bdv.infi.data.PlantillaMail;
import com.bdv.infi.data.PlantillaMailArea;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.PlantillasMailTipos;
import com.bdv.infi.logic.interfaz_varias.EnvioCorreos;

/**
 * DAO para el programador de Tareas
 */
public class PlantillasMailDAO extends GenericoDAO{
	
	static final Logger logger = Logger.getLogger(PlantillasMailDAO.class);
	
/**
 * Constructor
 * @param ds
 */
	public PlantillasMailDAO(DataSource ds) {
		super(ds);
	}
	@Override
	public Object moveNext() throws Exception {
		return null;
	}
	
/**
 * Obtiene una plantilla a partir de su nombre y tipo de destinatario
 * @param name : Nombre de la pantilla
 * @param tipoDest : Tipo de destinatario de la pantilla
 * @throws Exception
 */
	public void getPlantilla(String name, String tipoDest) throws Exception {				
		String sql = "SELECT * FROM infi_tb_223_plantilla_mail WHERE plantilla_mail_name='" + name + "' "+
		"AND tipo_destinatario='" + tipoDest + "' AND ESTATUS_ACTIVACION=" + ConstantesGenerales.VERDADERO;
		dataSet = db.get(dataSource, sql);	
	}
	
/**
 * Obtiene una plantilla a partir de un nombre de area
 * @throws Exception
 */
	public void getPlantillaFromArea(String nameArea, String namePlantilla) throws Exception {				
		String sql = "SELECT * FROM infi_tb_223_plantilla_mail WHERE plantilla_mail_id IN (" +
		"SELECT plantilla_mail_id FROM infi_tb_224_plant_mail_area WHERE plant_mail_area_name='" + nameArea +"' "+
		"AND estatus_activacion=" + ConstantesGenerales.VERDADERO + ") "+
		"AND plantilla_mail_name='" + namePlantilla + "'";
		dataSet = db.get(dataSource, sql);
	}
	
/**
 * Obtiene el area a la que pertenece una plantilla
 * @throws Exception
 */
	public void getAreaFromIdPlant(int idPlant) throws Exception {				
		String sql = "SELECT * FROM infi_tb_224_plant_mail_area WHERE plant_mail_area_id='" + idPlant + "' "
		+ "AND ESTATUS_ACTIVACION=" + ConstantesGenerales.VERDADERO;
		dataSet = db.get(dataSource, sql);
	}
	
	/**
	 * Obtiene una plantilla de acuerdo al los parámetros enviados
	 * @param fechaRegistroHasta 
	 * @param fechaRegistroDesde 
	 * @throws Exception
	 */
	//NM29643 infi_TTS_466 10/07/2014
		public void listarPlantillasMail(PlantillaMail plantillaMail, String fechaAprobacionDesde, String fechaAprobacionHasta, String fechaRegistroDesde, String fechaRegistroHasta) throws Exception {				
			StringBuffer sql=new StringBuffer();
			
			sql.append("SELECT pm.PLANTILLA_MAIL_ID, upper(pm.PLANTILLA_MAIL_NAME) as PLANTILLA_MAIL_NAME, pm.REMITENTE, pm.TIPO_DESTINATARIO, DECODE(pm.FECHA_APROBACION, null, 'Registrada', 'Aprobada') as estatus, DECODE(pm.ESTATUS_ACTIVACION, 1, 'Activa', 'Inactiva') as activacion_desc, DECODE(TIPO_DESTINATARIO, 'C', '").append(PlantillasMailTipos.TIPO_DEST_CLIENTE).append("', '").append(PlantillasMailTipos.TIPO_DEST_FUNCIONAL).append("') as tipo_destinatario_desc, ");
			sql.append("pm.ASUNTO, pm.CUERPO, pm.USUARIO_REGISTRO, pm.USUARIO_APROBACION, to_char(pm.FECHA_APROBACION, '").append(ConstantesGenerales.FORMATO_FECHA_HORA_SYSDATE).append("') as FECHA_APROBACION, ");
			sql.append("pm.ESTATUS_ACTIVACION, to_char(pm.FECHA_REGISTRO, '").append(ConstantesGenerales.FORMATO_FECHA_HORA_SYSDATE).append("') as FECHA_REGISTRO, pm.USUARIO_ULT_MODIFICACION, to_char(pm.FECHA_ULT_MODIFICACION, '").append(ConstantesGenerales.FORMATO_FECHA_HORA_SYSDATE).append("') as FECHA_ULT_MODIFICACION, DECODE(pm.TIPO_DESTINATARIO, 'C', 'none', 'block') as areas, ");
			sql.append("pm.EVENTO_ID, pm.BLOQUE_ITERADO, evm.EVENTO_NAME, prod.NOMBRE");
//			sql.append("pm.ordsta_id, pm.transa_id, os.ORDSTA_NOMBRE, tr.TRANSA_DESCRIPCION");
			
			/*if(plantillaMail.getStatusOrdenId()!=null){
				sql.append(", (SELECT ORDSTA_NOMBRE from INFI_TB_203_ORDENES_STATUS where ORDSTA_ID='").append(plantillaMail.getStatusOrdenId()).append("') as ORDSTA_NOMBRE");
			}
			if(plantillaMail.getTransaccionId()!=null){
				sql.append(", (SELECT TRANSA_DESCRIPCION from INFI_TB_012_TRANSACCIONES where TRANSA_ID='").append(plantillaMail.getTransaccionId()).append("') as TRANSA_DESCRIPCION");
			}*/
			sql.append(" FROM INFI_TB_223_PLANTILLA_MAIL pm, INFI_TB_230_EVENTO_MAIL evm, INFI_TB_019_TIPO_DE_PRODUCTO prod");
			sql.append(" WHERE 1 = 1");
			sql.append(" AND evm.evento_id=pm.evento_id AND evm.TIPO_PRODUCTO_ID=prod.TIPO_PRODUCTO_ID");
//			sql.append(" AND os.ORDSTA_ID=pm.ORDSTA_ID AND tr.TRANSA_ID=pm.TRANSA_ID");
			
			if(plantillaMail.getPlantillaMailId()!= 0 ){
				sql.append(" AND pm.PLANTILLA_MAIL_ID = ").append(plantillaMail.getPlantillaMailId());
			}else{
				
				if(plantillaMail.getEventoId()!=null){
					sql.append(" AND pm.EVENTO_ID = '").append(plantillaMail.getEventoId()).append("'");
				}else{
					if(plantillaMail.getProductoId()!=null){
						sql.append(" AND pm.EVENTO_ID IN (select EVENTO_ID from INFI_TB_230_EVENTO_MAIL where TIPO_PRODUCTO_ID='").append(plantillaMail.getProductoId()).append("')");
					}
				}
				
//				if(plantillaMail.getStatusOrdenId()!=null){
//					sql.append(" AND pm.ORDSTA_ID = '").append(plantillaMail.getStatusOrdenId()).append("'");
//				}
//				
//				if(plantillaMail.getTransaccionId()!=null){
//					sql.append(" AND pm.TRANSA_ID = '").append(plantillaMail.getTransaccionId()).append("'");
//				}
				
				if(plantillaMail.getPlantillaMailName()!=null){
					sql.append(" AND upper(pm.PLANTILLA_MAIL_NAME) like upper('%").append(plantillaMail.getPlantillaMailName()).append("%')");
				}
				
				if(plantillaMail.getTipoDestinatario()!=null){
					sql.append(" AND pm.TIPO_DESTINATARIO = '").append(plantillaMail.getTipoDestinatario()).append("'");
				}

				if(plantillaMail.getRemitente()!=null){
					sql.append(" AND upper(pm.REMITENTE) like upper('%").append(plantillaMail.getRemitente()).append("%')");
				}

				if(plantillaMail.getAsunto()!=null){
					sql.append(" AND upper(pm.ASUNTO) like upper('%").append(plantillaMail.getAsunto()).append("%')");
				}

				if(plantillaMail.getEstatusActivacion()!=null){
					sql.append(" AND pm.ESTATUS_ACTIVACION = ").append(plantillaMail.getEstatusActivacion());
				}
				
				if(plantillaMail.getEstatus()!=null){
					if(plantillaMail.getEstatus().equals("0")){//Si es Registrada
						sql.append(" AND pm.FECHA_APROBACION is null");
					}else{
						sql.append(" AND pm.FECHA_APROBACION is not null");//Si es Aprobada
					}
				}
				
				if(fechaAprobacionDesde!= null){
					sql.append(" AND trunc(pm.FECHA_APROBACION) >= to_date('").append(fechaAprobacionDesde).append("','").append(ConstantesGenerales.FORMATO_FECHA_HORA_SYSDATE).append("')");
				}
				
				if(fechaAprobacionHasta!= null){
					sql.append(" AND trunc(pm.FECHA_APROBACION) <= to_date('").append(fechaAprobacionHasta).append("','").append(ConstantesGenerales.FORMATO_FECHA_HORA_SYSDATE).append("')");
				}		
				
				
				if(fechaRegistroDesde!= null){
					sql.append(" AND trunc(pm.FECHA_REGISTRO) >= to_date('").append(fechaRegistroDesde).append("','").append(ConstantesGenerales.FORMATO_FECHA_HORA_SYSDATE).append("')");
				}
				
				if(fechaRegistroHasta!= null){
					sql.append(" AND trunc(pm.FECHA_REGISTRO) <= to_date('").append(fechaRegistroHasta).append("','").append(ConstantesGenerales.FORMATO_FECHA_HORA_SYSDATE).append("')");
				}			
				//Plantillas con areas de ID X
				if(plantillaMail.getListaPlantillaMailArea()!=null){
					if(plantillaMail.getListaPlantillaMailArea().size()>0){						
						sql.append(" AND pm.PLANTILLA_MAIL_ID IN ");
						sql.append("(SELECT PLANTILLA_MAIL_ID FROM INFI_TB_224_PLANT_MAIL_AREA WHERE PLANT_MAIL_AREA_ID=").append(plantillaMail.getListaPlantillaMailArea().get(0).getPlantMailAreaId()).append(")");								
					}
				}

			}
			
			sql.append(" order by pm.PLANTILLA_MAIL_NAME, pm.TIPO_DESTINATARIO, pm.FECHA_REGISTRO ");
//System.out.println("plantillas query: "+sql);
			dataSet = db.get(dataSource, sql.toString());
		}

	/**
	 * Obtiene los datos de una plantilla de correo por id
	 * @param plantillasMailId
	 * @throws Exception
	 */
	public void listarPlantillasMailPorId(String plantillasMailId, boolean... detalleEvento) throws Exception {				
		StringBuffer sql=new StringBuffer();
		boolean prod = false;
		sql.append("SELECT pm.PLANTILLA_MAIL_ID, pm.PLANTILLA_MAIL_NAME, pm.REMITENTE, pm.TIPO_DESTINATARIO, DECODE(pm.FECHA_APROBACION, null, 'Registrada', 'Aprobada') as estatus, DECODE(pm.ESTATUS_ACTIVACION, 1, 'Activa', 'Inactiva') as activacion_desc, DECODE(pm.TIPO_DESTINATARIO, 'C', '").append(PlantillasMailTipos.TIPO_DEST_CLIENTE).append("', '").append(PlantillasMailTipos.TIPO_DEST_FUNCIONAL).append("') as tipo_destinatario_desc, ");
		sql.append("pm.ASUNTO, pm.CUERPO, pm.USUARIO_REGISTRO, pm.USUARIO_APROBACION, pm.USUARIO_ULT_MODIFICACION, to_char(pm.FECHA_APROBACION, '").append(ConstantesGenerales.FORMATO_FECHA_HORA_SYSDATE).append("') as FECHA_APROBACION, ").append("pm.ESTATUS_ACTIVACION, pm.EVENTO_ID, pm.BLOQUE_ITERADO");
		if(detalleEvento!=null && detalleEvento.length>0 && detalleEvento[0]==true){
			prod = true;
			sql.append(", ev.TIPO_PRODUCTO_ID, ev.EVENTO_ID, ev.EVENTO_NAME, (SELECT nombre from INFI_TB_019_TIPO_DE_PRODUCTO where TIPO_PRODUCTO_ID=ev.TIPO_PRODUCTO_ID) as prod_name");
			sql.append(", CASE ev.EVENTO_ID WHEN 'TOMA_ORDEN_RED' THEN 'none' WHEN 'ENVIO_BCV_RED' THEN 'none' WHEN 'ENVIO_BCV_PER' THEN 'none' ELSE 'block' END display");
		}
		sql.append(" FROM INFI_TB_223_PLANTILLA_MAIL pm");
		if(prod) sql.append(", INFI_TB_230_EVENTO_MAIL ev");
		sql.append(" WHERE PLANTILLA_MAIL_ID = ").append(plantillasMailId);
		if(prod) sql.append(" AND pm.EVENTO_ID = ev.EVENTO_ID");
		//System.out.println("SQLLLLLLL: "+sql.toString());
		logger.debug("--------listarPlantillasMailPorId: "+sql.toString());
		dataSet = db.get(dataSource, sql.toString());
	}
	
	/**
	 * Lista las areas asociadas a una plantilla de correo
	 * @param plantillasMailId
	 * @throws Exception
	 */
	//NM29643 infi_TTS_466 10/07/2014
	public void listarAreasPlantillasMail(String plantillasMailId, String statusActivacion) throws Exception {				
		StringBuffer sql=new StringBuffer();
		
		sql.append("SELECT PLANT_MAIL_AREA_ID, PLANT_MAIL_AREA_NAME, DESTINATARIO, PLANTILLA_MAIL_ID, ESTATUS_ACTIVACION ");
		sql.append("FROM INFI_TB_224_PLANT_MAIL_AREA ");
		sql.append("WHERE 1=1");
		if(plantillasMailId!=null){
			sql.append(" AND PLANTILLA_MAIL_ID = ").append(plantillasMailId);
		}
		if(statusActivacion!=null){
			sql.append(" AND ESTATUS_ACTIVACION=").append(statusActivacion);
		}
		sql.append(" ORDER BY PLANT_MAIL_AREA_NAME");
		dataSet = db.get(dataSource, sql.toString());
	}
	
	/**
	 * Verifica si existen areas activas asociadas a una plantilla de correo
	 * @param plantillasMailId
	 * @throws Exception
	 */
	public boolean existenAreasActivasPlantillasMail(String plantillasMailId) throws Exception {				
		StringBuffer sql=new StringBuffer();
		
		sql.append("SELECT PLANT_MAIL_AREA_ID ");
		sql.append("FROM INFI_TB_224_PLANT_MAIL_AREA ");		
		sql.append("WHERE PLANTILLA_MAIL_ID = ").append(plantillasMailId);
		sql.append(" AND ESTATUS_ACTIVACION = 1");
		
		dataSet = db.get(dataSource, sql.toString());
		
		if(dataSet.count()> 0) return true; else return false;
	}


	/**
	 * Obtiene los distintos tipos de plantillas o de destinatarios
	 * @param tipo_de: especifica el elemento al que se le obtienen los tipos (1: plantilla, 2: destinatario, 3: area)
	 * @throws Exception
	 */
	public void getTipos(int tipo_de) throws Exception {
		String table = "";
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT distinct ");
		switch(tipo_de) {
			case 1: sb.append("plantilla_mail_name");
					table = "infi_tb_223_plantilla_mail";
			break;
			case 2: sb.append("tipo_destinatario");
					table = "infi_tb_223_plantilla_mail";
			break;
			case 3: sb.append("plant_mail_area_name");
					table = "infi_tb_224_plant_mail_area";
			break;
		}
		sb.append(" from ").append(table);
		dataSet = db.get(dataSource, sb.toString());
		//db.exec(dataSource, sb.toString());
	}
	
	/**
	 * Obtiene los estatus de orden que pueden asociarse a una plantilla
	 * @throws Exception
	 */
	public void getStatusOrdenPlantilla(String... idStatusOrden) throws Exception {		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ORDSTA_ID, ORDSTA_NOMBRE FROM INFI_TB_203_ORDENES_STATUS");
		if(idStatusOrden!=null && idStatusOrden.length>0){
			sb.append(" where ORDSTA_ID = '").append(idStatusOrden[0]).append("'");
		}
		sb.append(" order by ORDSTA_NOMBRE");
		dataSet = db.get(dataSource, sb.toString());
	}
	
	/**
	 * Obtiene los estatus de orden que pueden asociarse a una plantilla
	 * @throws Exception
	 */
	public void getTransaccionesPlantilla() throws Exception {		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT TRANSA_ID, TRANSA_DESCRIPCION FROM INFI_TB_012_TRANSACCIONES order by TRANSA_DESCRIPCION");
		dataSet = db.get(dataSource, sb.toString());		
	}
	
	/**
	 * Obtiene un tipo de plantilla por el id del proceso en el cual se utiliza
	 * @param idProceso
	 * @throws Exception
	 */
	/*public void obtenerTiposPlantilla(String idProceso) throws Exception {		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ORDSTA_ID as PLANTILLA_MAIL_NAME, ORDSTA_NOMBRE as tipo_plantilla FROM INFI_TB_203_ORDENES_STATUS ");
		sb.append(" WHERE ORDSTA_ID = '").append(idProceso).append("'");
		dataSet = db.get(dataSource, sb.toString());		
	}*/


	/**
	 * Arma los tipos de destinatario y los almacena en un dataset
	 * @throws Exception
	 */
	public void obtenerTiposDestinatario() throws Exception {
		//Crear Dataset para los tipos de destinatario
		DataSet _tiposDestinatario = new DataSet();
		_tiposDestinatario.append("TIPO_DESTINATARIO", java.sql.Types.VARCHAR);
		_tiposDestinatario.append("NOMBRE_TIPO_DESTINATARIO", java.sql.Types.VARCHAR);
		
		//Agregar Registros y valores
		_tiposDestinatario.addNew();
		_tiposDestinatario.setValue("TIPO_DESTINATARIO", PlantillasMailTipos.TIPO_DEST_CLIENTE_COD);
		_tiposDestinatario.setValue("NOMBRE_TIPO_DESTINATARIO", PlantillasMailTipos.TIPO_DEST_CLIENTE.toUpperCase());
		_tiposDestinatario.addNew();
		_tiposDestinatario.setValue("TIPO_DESTINATARIO", PlantillasMailTipos.TIPO_DEST_FUNCIONAL_COD);
		_tiposDestinatario.setValue("NOMBRE_TIPO_DESTINATARIO", PlantillasMailTipos.TIPO_DEST_FUNCIONAL.toUpperCase());
		
		dataSet = _tiposDestinatario;		
	}
	
	/**
	 * Arma los tipos de destinatario y los almacena en un dataset
	 * @throws Exception
	 */
	public void obtenerStatusCorreos(EnvioCorreos ec) throws Exception {
		//Crear Dataset para los estatus de correos
		DataSet _statusCorreos = new DataSet();
		_statusCorreos.append("status_correo", java.sql.Types.VARCHAR);
		//logger.debug("ec----"+ec.toString());
		if(ec!=null && ec.parametros!=null){
			//Agregar Registros y valores
			_statusCorreos.addNew();
			_statusCorreos.setValue("status_correo", ec.parametros.get(ParametrosSistema.STATUS_PRECARGADO));
			_statusCorreos.addNew();
			_statusCorreos.setValue("status_correo", ec.parametros.get(ParametrosSistema.STATUS_CARGADO));
			_statusCorreos.addNew();
			_statusCorreos.setValue("status_correo", ec.parametros.get(ParametrosSistema.STATUS_ENVIADO));
			_statusCorreos.addNew();
			_statusCorreos.setValue("status_correo", ec.parametros.get(ParametrosSistema.STATUS_NO_ENVIADO));
			_statusCorreos.addNew();
			_statusCorreos.setValue("status_correo", ec.parametros.get(ParametrosSistema.STATUS_INVALIDO));
		}
		dataSet = _statusCorreos;
		//logger.debug("Estatussss---- "+dataSet);
	}
	
	/**
	 * Inserta un registro de plantilla de correo
	 * @param plantillaMail
	 * @return
	 * @throws Exception
	 */
	//NM29643 infi_TTS_466 10/07/2014
	public String[] insertar(PlantillaMail plantillaMail) throws Exception{
		ArrayList<String> consultas = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
	
		sql.append("insert into INFI_TB_223_PLANTILLA_MAIL ");			
		sql.append("(PLANTILLA_MAIL_NAME, REMITENTE, TIPO_DESTINATARIO, ASUNTO, CUERPO, USUARIO_REGISTRO, USUARIO_APROBACION, FECHA_APROBACION, ESTATUS_ACTIVACION, FECHA_REGISTRO, EVENTO_ID, BLOQUE_ITERADO)");
		sql.append(" values (");
		sql.append("'").append(plantillaMail.getPlantillaMailName()).append("',");
		sql.append("'").append(plantillaMail.getRemitente()).append("',");
		sql.append("'").append(plantillaMail.getTipoDestinatario()).append("',");
		sql.append("'").append(plantillaMail.getAsunto()).append("',");
		sql.append("'").append(plantillaMail.getCuerpo()).append("',");
		sql.append("'").append(plantillaMail.getUsuarioRegistro().toUpperCase()).append("',");
		sql.append("NULL,");
		sql.append("NULL,");
		sql.append("0, "); //Guardar como Inactiva. Se activará hasta que se apruebe
		sql.append("SYSDATE,");
		sql.append("'").append(plantillaMail.getEventoId().toUpperCase()).append("',");
//		sql.append("'").append(plantillaMail.getStatusOrdenId().toUpperCase()).append("',");
//		sql.append("'").append(plantillaMail.getTransaccionId().toUpperCase()).append("'");
		if(plantillaMail.getBloqueIterado()!=null && !plantillaMail.getBloqueIterado().equals("")){
			sql.append("'").append(plantillaMail.getBloqueIterado()).append("'");
		}else{
			sql.append("DEFAULT");
		}
		sql.append(")");
		
			
		consultas.add(sql.toString());
	
		//Prepara el retorno
		String[] retorno = new String[consultas.size()];
		for(int i=0;i<consultas.size();i++){
			retorno[i] = consultas.get(i).toString();			
		}
		return retorno;
	}
	
	/**
	 * Prepara los queries para cada una de las areas agregadas a la plantilla
	 * @param plantillaMail
	 * @param consultas
	 * @throws Exception
	 */
	public String[] agregarAreas(PlantillaMail plantillaMail) throws Exception {
		ArrayList<String> consultas = new ArrayList<String>();
		String insertArea;
		
		if(plantillaMail.getListaPlantillaMailArea()!=null){
			for(int i=0; i < plantillaMail.getListaPlantillaMailArea().size(); i++){
				insertArea = insertarAreaPlantilla(plantillaMail.getListaPlantillaMailArea().get(i));
				consultas.add(insertArea);
			}		
		}
		
		//coloca la plantilla como registrada ya que esta fue modificada agregando areas
		actualizarEstatusPlantilla(String.valueOf(plantillaMail.getPlantillaMailId()), consultas, plantillaMail.getUsuarioUltModificacion());
		
		//Prepara el retorno
		String[] retorno = new String[consultas.size()];
		for(int i=0;i<consultas.size();i++){
			retorno[i] = consultas.get(i).toString();			
		}
		return retorno;

	}
	/**
	 * Actualiza un registro de plantilla de correo
	 * @param plantillaMail
	 * @return
	 * @throws Exception
	 */
	public String[] actualizar(PlantillaMail plantillaMail) throws Exception{
		ArrayList<String> consultas = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
	
		sql.append("UPDATE INFI_TB_223_PLANTILLA_MAIL SET ");		
		sql.append("PLANTILLA_MAIL_NAME = '").append(plantillaMail.getPlantillaMailName()).append("',");
		sql.append("REMITENTE = '").append(plantillaMail.getRemitente()).append("',");
		sql.append("TIPO_DESTINATARIO = '").append(plantillaMail.getTipoDestinatario()).append("',");
		sql.append("ASUNTO = '").append(plantillaMail.getAsunto()).append("',");
		sql.append("CUERPO = '").append(plantillaMail.getCuerpo()).append("',");
		sql.append("USUARIO_ULT_MODIFICACION = '").append(plantillaMail.getUsuarioUltModificacion().toUpperCase()).append("',");
		sql.append("FECHA_ULT_MODIFICACION = SYSDATE, ");
		sql.append("USUARIO_APROBACION = NULL, ");
		sql.append("FECHA_APROBACION = NULL, ");
		sql.append("ESTATUS_ACTIVACION = 0, ");//se inactiva al modificarse, solo se activa al aprobarse
		sql.append("EVENTO_ID = '").append(plantillaMail.getEventoId()).append("', ");
//		sql.append("ORDSTA_ID = '").append(plantillaMail.getStatusOrdenId()).append("',");
//		sql.append("TRANSA_ID = '").append(plantillaMail.getTransaccionId()).append("'");
		sql.append("BLOQUE_ITERADO = '").append(plantillaMail.getBloqueIterado()).append("'");
		sql.append(" WHERE PLANTILLA_MAIL_ID = ").append(plantillaMail.getPlantillaMailId());	
		
		consultas.add(sql.toString());
			
		//Prepara el retorno
		String[] retorno = new String[consultas.size()];
		for(int i=0;i<consultas.size();i++){
			retorno[i] = consultas.get(i).toString();			
		}
		return retorno;
	}
	
	/**
	 * Actualiza las areas asociadas a una plantilla de correo
	 * @param plantillaMail
	 * @param consultas
	 * @throws Exception 
	 */
	public String[] actualizarAreas(PlantillaMail plantillaMail) throws Exception {
		ArrayList<String> consultas = new ArrayList<String>();
		
		String updateArea;
		for(int i=0; i < plantillaMail.getListaPlantillaMailArea().size(); i++){
			updateArea = actualizarAreaPlantilla(plantillaMail.getListaPlantillaMailArea().get(i));
			consultas.add(updateArea);
		}
		
		//coloca la plantilla como registrada ya que esta fue modificada agregando areas
		actualizarEstatusPlantilla(String.valueOf(plantillaMail.getPlantillaMailId()), consultas, plantillaMail.getUsuarioUltModificacion());
		
		//Prepara el retorno
		String[] retorno = new String[consultas.size()];
		for(int i=0;i<consultas.size();i++){
			retorno[i] = consultas.get(i).toString();			
		}
		return retorno;

	}
	
	/**
	 * Elimina todas las areas asociadas a una plantilla de correo
	 * @param plantillaMailId
	 * @param consultas
	 */
	public String eliminarTodasAreasPlantilla(int plantillaMailId) {
		StringBuffer sql = new StringBuffer();
		
		sql.append("DELETE FROM INFI_TB_224_PLANT_MAIL_AREA ");		
		sql.append("WHERE PLANTILLA_MAIL_ID = ").append(plantillaMailId);	
				
		return sql.toString();
	}
	
	/**
	 * Aprueba y activa una plantilla de correo
	 * @param plantillaMail
	 * @return
	 * @throws Exception
	 */
	public String aprobar(PlantillaMail plantillaMail) throws Exception{		
		StringBuffer sql = new StringBuffer();
	
		sql.append("UPDATE INFI_TB_223_PLANTILLA_MAIL SET ");		
		sql.append("USUARIO_APROBACION = '").append(plantillaMail.getUsuarioAprobacion().toUpperCase()).append("',");	
		sql.append("FECHA_APROBACION = SYSDATE, ");
		sql.append("ESTATUS_ACTIVACION = 1");//Activar plantilla al aprobar
		sql.append(" WHERE PLANTILLA_MAIL_ID = ").append(plantillaMail.getPlantillaMailId());	
				
		return sql.toString();
	}
	
	/**
	 * Inserta un area asociada a una plantilla de correo
	 * @param plantillaMailArea
	 * @return
	 * @throws Exception
	 */
	public String insertarAreaPlantilla(PlantillaMailArea plantillaMailArea) throws Exception{		
		StringBuffer sql = new StringBuffer();
	
		sql.append("insert into INFI_TB_224_PLANT_MAIL_AREA ");			
		sql.append("(PLANT_MAIL_AREA_NAME, DESTINATARIO, PLANTILLA_MAIL_ID, ESTATUS_ACTIVACION)");
		sql.append(" values (");
		sql.append("'").append(plantillaMailArea.getPlantMailAreaName()).append("', ");
		sql.append("'").append(plantillaMailArea.getDestinatario()).append("', ");
		sql.append(plantillaMailArea.getPlantillaMailId()).append(", ");	
		sql.append(plantillaMailArea.getEstatusActivacion()).append(")");
		
		return sql.toString();
	}

	/**
	 * Actualiza un area asociada a una plantilla de correo
	 * @param plantillaMailArea
	 * @return
	 * @throws Exception
	 */
	public String actualizarAreaPlantilla(PlantillaMailArea plantillaMailArea) throws Exception{
		StringBuffer sql = new StringBuffer();
	
		
		sql.append("UPDATE INFI_TB_224_PLANT_MAIL_AREA SET ");		
		sql.append("PLANT_MAIL_AREA_NAME = '").append(plantillaMailArea.getPlantMailAreaName()).append("',");
		sql.append("DESTINATARIO = '").append(plantillaMailArea.getDestinatario()).append("',");
		sql.append("ESTATUS_ACTIVACION = ").append(plantillaMailArea.getEstatusActivacion());
		sql.append(" WHERE PLANT_MAIL_AREA_ID = ").append(plantillaMailArea.getPlantMailAreaId());	
				
		return sql.toString();
	}
	
	/**
	 * Elimina un area de una plantilla de correo
	 * @param plantillaMailAreaId
	 * @return
	 * @throws Exception
	 */
	public String[] eliminarAreaPlantilla(String plantillaMailAreaId, String plantillaMailId, String usuario) throws Exception{
		ArrayList<String> consultas = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
	
		//Eliminar area
		sql.append("DELETE FROM INFI_TB_224_PLANT_MAIL_AREA ");		
		sql.append("WHERE PLANT_MAIL_AREA_ID = ").append(plantillaMailAreaId);	
				
		consultas.add(sql.toString());
		
		actualizarEstatusPlantilla(plantillaMailId, consultas, usuario);
		 
		//Prepara el retorno
		String[] retorno = new String[consultas.size()];
		for(int i=0;i<consultas.size();i++){
			retorno[i] = consultas.get(i).toString();			
		}
		return retorno;
	}
	
	/**
	 * Actualiza el estatus de una plantilla de correo, dejandola nuevamente por aprobar por haberse producido un cambio en la misma
	 * @param plantillaMailId
	 * @param consultas
	 */
	private void actualizarEstatusPlantilla(String plantillaMailId, ArrayList<String> consultas, String usuario) {
		//Actualizar Plantilla a No aprobada
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE INFI_TB_223_PLANTILLA_MAIL SET ");	
		sql.append("USUARIO_ULT_MODIFICACION = '").append(usuario.toUpperCase()).append("',");
		sql.append("FECHA_ULT_MODIFICACION = SYSDATE, ");
		sql.append("USUARIO_APROBACION = NULL, ");
		sql.append("FECHA_APROBACION = NULL, ");
		sql.append("ESTATUS_ACTIVACION = 0 ");
		sql.append("WHERE PLANTILLA_MAIL_ID = ").append(plantillaMailId);
		
		consultas.add(sql.toString());		
	}
	
	/**
	 * Verifica si existe una plantilla asociada al mismo proceso, destinatario, y que se encuentre aprobada y activa ó en estatus registrada
	 * @param tipoProcesoPlantilla
	 * @param tipoDestinatarioPlantilla
	 * @throws Exception
	 */
	//NM29643 infi_TTS_466
	public boolean verificarPlantillaExistente(String eventoId, String tipoDestinatarioPlantilla) throws Exception {
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT PLANTILLA_MAIL_ID, FECHA_APROBACION, ESTATUS_ACTIVACION FROM INFI_TB_223_PLANTILLA_MAIL ");
		sql.append("WHERE EVENTO_ID = '").append(eventoId).append("' ");
		sql.append("AND TIPO_DESTINATARIO = '").append(tipoDestinatarioPlantilla).append("'");		
		sql.append("AND ((FECHA_APROBACION IS NOT NULL AND ESTATUS_ACTIVACION = 1) ");//plantilla aprobada y activa
		sql.append("OR FECHA_APROBACION IS NULL )");//plantilla registrada
		
		dataSet = db.get(dataSource, sql.toString());		
		
		if(dataSet.count()>0) return true;
		else return false;
	}
	
	/**
	 * Inactiva una plantilla de correo
	 * @param plantillaMail
	 * @return
	 */
	public String inactivar(PlantillaMail plantillaMail) {
		StringBuffer sql = new StringBuffer();
		
		sql.append("UPDATE INFI_TB_223_PLANTILLA_MAIL SET ");		
		sql.append("USUARIO_ULT_MODIFICACION = '").append(plantillaMail.getUsuarioUltModificacion().toUpperCase()).append("',");	
		sql.append("FECHA_ULT_MODIFICACION = SYSDATE, ");
		sql.append("ESTATUS_ACTIVACION = 0");//InaActivar plantilla al aprobar
		sql.append(" WHERE PLANTILLA_MAIL_ID = ").append(plantillaMail.getPlantillaMailId());	
				
		return sql.toString();
	}
	
	//NM29643 - infi_TTS_466 12/08/2014
	/**
	 * Inactiva las plantillas de correo de un mismo evento
	 * @param plantillaMail
	 * @return
	 */
	public String inactivarPorEvento(PlantillaMail plantillaMail) {
		StringBuffer sql = new StringBuffer();
		
		sql.append("UPDATE INFI_TB_223_PLANTILLA_MAIL SET ");		
		sql.append("USUARIO_ULT_MODIFICACION = '").append(plantillaMail.getUsuarioUltModificacion().toUpperCase()).append("',");	
		sql.append("FECHA_ULT_MODIFICACION = SYSDATE, ");
		sql.append("ESTATUS_ACTIVACION = 0");//InaActivar plantilla al aprobar
		sql.append(" WHERE EVENTO_ID = '").append(plantillaMail.getEventoId()).append("'");	
				
		return sql.toString();
	}
		
}

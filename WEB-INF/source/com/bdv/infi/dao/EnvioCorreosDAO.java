package com.bdv.infi.dao;

import javax.sql.DataSource;

import megasoft.DataSet;
import megasoft.db;

import org.apache.log4j.Logger;

import com.bdv.infi.data.PlantillaMail;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.PlantillasMailTipos;

/**
 * DAO para el programador de Tareas
 */
public class EnvioCorreosDAO extends GenericoDAO{
	
	static final Logger logger = Logger.getLogger(EnvioCorreosDAO.class);
	
/**
 * Constructor
 * @param ds
 */
	public EnvioCorreosDAO(DataSource ds) {
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
		"AND tipo_destinatario='" + tipoDest + "' AND ESTATUS_ACTIVACION=" + ConstantesGenerales.VERDADERO + " " +
		"AND fecha_aprobacion IS NOT NULL";
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
		"AND fecha_aprobacion IS NOT NULL "+
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
	 * @throws Exception
	 */
		public void listarPlantillasMail(PlantillaMail plantillaMail, String fechaAprobacionDesde, String fechaAprobacionHasta) throws Exception {				
			StringBuffer sql=new StringBuffer();
			
			sql.append("SELECT PLANTILLA_MAIL_ID, PLANTILLA_MAIL_NAME, REMITENTE, TIPO_DESTINATARIO, DECODE(FECHA_APROBACION, null, 'Registrada', 'Aprobada') as estatus, DECODE(ESTATUS_ACTIVACION, 1, 'Activa', 'Inactiva') as activacion_desc, DECODE(TIPO_DESTINATARIO, 'C', '").append(PlantillasMailTipos.TIPO_DEST_CLIENTE).append("', '").append(PlantillasMailTipos.TIPO_DEST_FUNCIONAL).append("') as tipo_destinatario_desc, ");
			sql.append("ASUNTO, CUERPO, USUARIO_REGISTRO, USUARIO_APROBACION, to_char(FECHA_APROBACION, '").append(ConstantesGenerales.FORMATO_FECHA_HORA_SYSDATE).append("') as FECHA_APROBACION, ").append("ESTATUS_ACTIVACION");
			sql.append(" FROM INFI_TB_223_PLANTILLA_MAIL ");
			sql.append(" WHERE 1 = 1");
			
			if(plantillaMail.getPlantillaMailId()!= 0 ){
				sql.append(" AND PLANTILLA_MAIL_ID = ").append(plantillaMail.getPlantillaMailId());
			}else{
				
				if(plantillaMail.getPlantillaMailName()!=null){
					sql.append(" AND PLANTILLA_MAIL_NAME = '").append(plantillaMail.getPlantillaMailName()).append("'");
				}
				
				if(plantillaMail.getTipoDestinatario()!=null){
					sql.append(" AND TIPO_DESTINATARIO = '").append(plantillaMail.getTipoDestinatario()).append("'");
				}

				if(plantillaMail.getRemitente()!=null){
					sql.append(" AND upper(REMITENTE) like upper('%").append(plantillaMail.getRemitente()).append("%')");
				}

				if(plantillaMail.getAsunto()!=null){
					sql.append(" AND upper(ASUNTO) like upper('%").append(plantillaMail.getAsunto()).append("%')");
				}

				if(plantillaMail.getEstatusActivacion()!=null){
					sql.append(" AND ESTATUS_ACTIVACION = ").append(plantillaMail.getEstatusActivacion());
				}
				
				if(plantillaMail.getEstatus()!=null){
					if(plantillaMail.getEstatus().equals("0")){//Si es Registrada
						sql.append(" AND FECHA_APROBACION is null");
					}else{
						sql.append(" AND FECHA_APROBACION is not null");//Si es Aprobada
					}
				}
				
				if(fechaAprobacionDesde!= null){
					sql.append(" AND trunc(FECHA_APROBACION) >= to_date('").append(fechaAprobacionDesde).append("','").append(ConstantesGenerales.FORMATO_FECHA_HORA_SYSDATE).append("')");
				}
				
				if(fechaAprobacionHasta!= null){
					sql.append(" AND trunc(FECHA_APROBACION) <= to_date('").append(fechaAprobacionHasta).append("','").append(ConstantesGenerales.FORMATO_FECHA_HORA_SYSDATE).append("')");
				}			
			}
			
			sql.append(" order by PLANTILLA_MAIL_NAME");
			//System.out.println("plantillas: "+sql);
			dataSet = db.get(dataSource, sql.toString());
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
	 * Obtiene los tipos de plantilla que corresponderan con los posibles estatus de una orden
	 * @param tipo_de
	 * @throws Exception
	 */
	public void obtenerTiposPlantilla() throws Exception {		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ORDSTA_ID as PLANTILLA_MAIL_NAME, ORDSTA_NOMBRE as tipo_plantilla FROM INFI_TB_203_ORDENES_STATUS order by ORDSTA_NOMBRE");
		dataSet = db.get(dataSource, sb.toString());		
	}
	
	/**
	 * Obtiene un tipo de plantilla por el id del proceso en el cual se utiliza
	 * @param idProceso
	 * @throws Exception
	 */
	public void obtenerTiposPlantilla(String idProceso) throws Exception {		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ORDSTA_ID as PLANTILLA_MAIL_NAME, ORDSTA_NOMBRE as tipo_plantilla FROM INFI_TB_203_ORDENES_STATUS ");
		sb.append(" WHERE ORDSTA_ID = '").append(idProceso).append("'");
		dataSet = db.get(dataSource, sb.toString());		
	}


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
		_tiposDestinatario.setValue("TIPO_DESTINATARIO", "C");
		_tiposDestinatario.setValue("NOMBRE_TIPO_DESTINATARIO", PlantillasMailTipos.TIPO_DEST_CLIENTE);
		_tiposDestinatario.addNew();
		_tiposDestinatario.setValue("TIPO_DESTINATARIO", "F");
		_tiposDestinatario.setValue("NOMBRE_TIPO_DESTINATARIO", PlantillasMailTipos.TIPO_DEST_FUNCIONAL);
		
		dataSet = _tiposDestinatario;		
	}

		
}

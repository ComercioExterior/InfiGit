package com.bdv.infi.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.sql.DataSource;
import megasoft.Util;
import megasoft.db;
import com.bdv.infi.data.UIBlotter;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi_toma_orden.data.UIBlotterRangos;

/** 
 * Clase destinada para el manejo l&oacute;gico de inserci&oacute;n, recuperaci&oacute;n y listado de Blotter asciados a Unidades de Inversi&oacute;n almacenadas en las tablas de trabajo
 */
public class UIBlotterDAO extends com.bdv.infi.dao.GenericoDAO {
	
	/**
	 * Formatos de Date y Time
	 */
	private SimpleDateFormat sdIODate = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
	private String formatoHora = "to_date('@horario@','hh:miam')";
	/**
	 * SubQuery para rechazar los blotter ya seleccionados
	 */
	private static String subSql = "(select bloter_id from INFI_TB_107_UI_BLOTTER where undinv_id = ";
	/**
	 * Variable que indica que si se debe grabar el sql generado o no 
	 */
	private boolean indicaCommit = true;
	
	
	/**
	 * Permite indicar si se aplicara el Commit
	 * @param indicaCommit
	 */
	public void setIndicaCommit(boolean indicaCommit) {
		this.indicaCommit = indicaCommit;
	}

	/**
	 * Constructor de la clase.
	 * Permite inicializar el DataSource para los accesos a la base de datos
	 * @param ds : DataSource 
	 * @throws Exception
	 */
	public UIBlotterDAO(DataSource ds) throws Exception {
		super(ds);
	}
	
	public UIBlotterDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
	}
	
	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Permite registrar en la base de datos sql pendientes. Garantizar integridad de transaccion de negocio
	 * @param listaSql
	 * @throws Exception
	 */
	public void grabar(ArrayList<String> listaSql) throws Exception {
		
		String [] batch = new String [listaSql.size()];
		listaSql.toArray(batch);
		db.execBatch(dataSource, batch);
	}
	
	/**
	 * Eliminar la asociacion de un Blotter con una unidad de inversion de la base de datos
	 * @param idUnidadInversion : identificador de la Unidad de Inversion
	 * @param idBlotter : identificador del Blotter
	 * @throws Exception
	*/
	//NM26659_11/02/2016_ Metodo modificado en incidencia ITS-2983 (Eliminacion de documentos al eliminar Blotter)
	public int eliminar(long idUnidadInversion, String idBlotter) throws Exception {
	
		String [] batch = new String [3];
				
		UIBlotterRangosDAO boUIBR = new UIBlotterRangosDAO(dataSource);
		//NM26659_11/02/2016_ Seccion incluida en ITS-2983
		UIDocumentosDAO uiDocumentosDAO=new UIDocumentosDAO(dataSource);		
		batch[0] = uiDocumentosDAO.eliminarDocumentosUI(idUnidadInversion,idBlotter);
		System.out.println("eliminarDocumentosUI --> " + batch[0]);
		//NM26659_11/02/2016_ Seccion incluida en ITS-2983
		
		batch[1] = boUIBR.eliminar(idUnidadInversion, idBlotter);
		
		StringBuffer sqlSB = new StringBuffer("delete from INFI_TB_107_UI_BLOTTER ");		
		sqlSB.append("where undinv_id = ").append(idUnidadInversion+" ");
		sqlSB.append("and bloter_id = ");
		sqlSB.append("'").append(idBlotter).append("' ");
		batch[2] = sqlSB.toString();
		
		db.execBatch(dataSource, batch);
		return 0;
	} 
	
	/**
	 * Ingresar la asociacion entre un Blotter y una unidad de inversion en la base de datos
	 * @param beanUIBlotter : bean de la asociacion Unidad de Inversion-Blotter con la data a registrar
	 * @throws Exception
	*/
	public String insertar(UIBlotter beanUIBlotter) throws Exception {
		
		StringBuffer sqlSB = new StringBuffer("insert into INFI_TB_107_UI_BLOTTER ");		
		sqlSB.append("(undinv_id, bloter_id, uiblot_in_recompra, uiblot_horario_desde, uiblot_horario_hasta, uiblot_horario_desde_ult_dia, uiblot_horario_hasta_ult_dia, uiblot_pedido_fe_ini, uiblot_pedido_fe_fin, ");
		sqlSB.append("uiblot_in_disponible, uiblot_in_tesoreria, uiblot_ganancia_red ) ");
		sqlSB.append("values (");
		sqlSB.append(beanUIBlotter.getIdUnidadInversion()).append(", ");
		sqlSB.append("'").append(beanUIBlotter.getIdBlotter()).append("', ");	
		sqlSB.append(beanUIBlotter.getIndicaRecompra()).append(", ");
		
		String horario = Util.replace(formatoHora,"@horario@",beanUIBlotter.getHorarioDesde());
		sqlSB.append(horario).append(", ");
		horario = Util.replace(formatoHora,"@horario@",beanUIBlotter.getHorarioHasta());
		sqlSB.append(horario).append(", ");	
		horario = Util.replace(formatoHora,"@horario@",beanUIBlotter.getHorarioDesde());
		sqlSB.append(horario).append(", ");
		horario = Util.replace(formatoHora,"@horario@",beanUIBlotter.getHorarioHasta());
		sqlSB.append(horario).append(", ");			
		
		sqlSB.append("to_date('").append(sdIODate.format(beanUIBlotter.getPeriodoInicial())).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("'), ");
		sqlSB.append("to_date('").append(sdIODate.format(beanUIBlotter.getPeriodoFinal())).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("'), ");
		sqlSB.append(beanUIBlotter.getIndicaDisponible()).append(", ");
		sqlSB.append("'").append(beanUIBlotter.getIndicaTesoreria()).append("' ");	
		sqlSB.append(", 0)");

		if (indicaCommit) {
			db.exec(dataSource, sqlSB.toString());
			return null;
		} else {
			return sqlSB.toString();
		}
    }
	
	/**
	 * Lista todos los t&iacute;tulos encontrados en la base de datos, que no estan asociados a una Unidad de Inversion. 
	 * Almacena el resultado en un dataset
	 * @param descripcion : prefijo para buscar los titulos
	 * @param idUnidadInversion : identificador de la Unidad de Inversion
	 * @throws Exception arroja una excepci&oacute;n en caso de un error
	 * */
	public void listarBlotterDescripcion(String descripcion, long idUnidadInversion) throws Exception {
		
		StringBuffer sqlSB = new StringBuffer();
		sqlSB.append("select bloter_id, bloter_descripcion, to_char(bloter_horario_desde, 'hh:mi am') as hora_horario_desde, ");	
		sqlSB.append("to_char(bloter_horario_hasta, 'hh:mi am') as hora_horario_hasta, ");
		sqlSB.append("0 as fila, '' as selecc, '' as readonly ");			
		sqlSB.append("from INFI_TB_102_BLOTER blo ");	
		sqlSB.append("where blo.bloter_status = 1 ");	
		if (descripcion != null)
			sqlSB.append("and lower(blo.bloter_descripcion) like '%").append(descripcion.toLowerCase()).append("%' ");		
		sqlSB.append("and blo.bloter_id not in ").append(subSql).append(idUnidadInversion+") ");			
		sqlSB.append("order by bloter_descripcion");

		dataSet = db.get(dataSource, sqlSB.toString());
	}

	
	/**
	 * Lista todos los t&iacute;tulos asociados a una Unidad de inversion encontrados en la base de datos. 
	 * Almacena el resultado en un dataset
	 * @param idUnidadInversion : identificador de la Unidad de Inversion
	 * @throws Exception arroja una excepci&oacute;n en caso de un error
	 * */
	public void listarBlottersPorID(long idUnidadInversion) throws Exception {
		
		StringBuffer sqlSB = new StringBuffer();
		sqlSB.append("select uiblo.*, blo.bloter_descripcion, ");	
		sqlSB.append("to_char(uiblot_horario_desde, 'hh:mi am') as hora_horario_desde, to_char(uiblot_horario_hasta, 'hh:mi am') as hora_horario_hasta, ");
		sqlSB.append("to_char(uiblot_horario_desde_ult_dia, 'hh:mi am') as hora_horario_desde_ult_dia, to_char(uiblot_horario_hasta_ult_dia, 'hh:mi am') as hora_horario_hasta_ult_dia, ");	
		sqlSB.append("to_char(uiblot_pedido_fe_ini,'dd-mm-yyyy') as pedido_ini, to_char(uiblot_pedido_fe_fin,'dd-mm-yyyy') as pedido_fin, ");	
		sqlSB.append("0 as fila, '' as readonly, ");
		sqlSB.append("case when (select count(*) from INFI_TB_111_UI_BLOTTER_RANGOS uiblotr where uiblotr.undinv_id = uiblo.undinv_id and uiblotr.bloter_id = uiblo.bloter_id) = 0 then 'pendientes' else 'registrados' end as indicador_parametros "); 
		sqlSB.append("from INFI_TB_107_UI_BLOTTER uiblo ");
		sqlSB.append("inner join  INFI_TB_102_BLOTER blo on uiblo.bloter_id = blo.bloter_id ");			
		sqlSB.append("where uiblo.undinv_id = "+idUnidadInversion+" ");	
		sqlSB.append("order by bloter_descripcion, uiblot_horario_desde");
		
		dataSet = db.get(dataSource, sqlSB.toString());
	}
	
	/** NM32454	15/02/2015 INFI_TTS_491_CONTINGENCIA
	 * Lista todos los t&iacute;tulos asociados a una Unidad de inversion encontrados en la base de datos. 
	 * Almacena el resultado en un dataset
	 * @param idUnidadInversion : identificador de la Unidad de Inversion
	 * @throws Exception arroja una excepci&oacute;n en caso de un error
	 * */
	public void listarBlottersRangosPorID(long idUnidadInversion,String idBlotter, String tipo_persona) throws Exception {
		StringBuffer sqlSB = new StringBuffer();
		sqlSB.append(" SELECT * FROM infi_tb_111_ui_blotter_rangos blo_r");	
		sqlSB.append(" WHERE blo_r.bloter_id= ").append(idBlotter);
		sqlSB.append(" AND blo_r.undinv_id= ").append(idUnidadInversion);
		sqlSB.append(" AND blo_r.tipper_id = '").append(tipo_persona).append("'");
		dataSet = db.get(dataSource, sqlSB.toString());
	}
	
	/**
	 * Lista la asociacion entre el Blotter y la Unidad de Inversion
	 * @param idUnidadInversion : identificador de la unidad de inversi&oacute;n
	 * @param idBlotter : identificador del Blotter
	 * @throws Exception
	*/
	public int listarPorId(long idUnidadInversion, String idBlotter) throws Exception {
		
		StringBuffer sqlSB = new StringBuffer();
		sqlSB.append("select uiblo.*, blo.bloter_descripcion, ");	
		sqlSB.append("to_char(uiblot_horario_desde, 'hh:mi am') as hora_horario_desde, to_char(uiblot_horario_hasta, 'hh:mi am') as hora_horario_hasta, ");
		sqlSB.append("to_char(uiblot_horario_desde_ult_dia, 'hh:mi am') as hora_horario_desde_ult_dia, to_char(uiblot_horario_hasta_ult_dia, 'hh:mi am') as hora_horario_hasta_ult_dia, ");	
		sqlSB.append("to_char(bloter_horario_desde, 'hh:mi am') as blotter_hora_horario_desde, to_char(bloter_horario_hasta, 'hh:mi am') as blotter_hora_horario_hasta, ");			
		sqlSB.append("to_char(uiblot_pedido_fe_ini,'dd-mm-yyyy') as pedido_ini, to_char(uiblot_pedido_fe_fin,'dd-mm-yyyy') as pedido_fin ");	
		sqlSB.append("from INFI_TB_107_UI_BLOTTER uiblo ");
		sqlSB.append("inner join  INFI_TB_102_BLOTER blo on uiblo.bloter_id = blo.bloter_id ");		
		sqlSB.append("where uiblo.undinv_id = ").append(idUnidadInversion+" ");	
		sqlSB.append("and uiblo.bloter_id = '").append(idBlotter).append("' ");	
		sqlSB.append("order by bloter_descripcion, uiblot_horario_desde");

		dataSet = db.get(dataSource, sqlSB.toString());

		return dataSet.count();
    }
	
	/**
	 * Lista los datos de un blotter espec&iacute;fico asociado a una Unidad de Inversi&oacute;n determinada
	 * @param idUnidadInversion
	 * @param idBlotter
	 * @throws Exception
	 */
	public void listarBlotterUI(long idUnidadInversion, String idBlotter) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("select blo_ui.*, blot.*, to_char(blo_ui.UIBLOT_PEDIDO_FE_FIN,'"+ ConstantesGenerales.FORMATO_FECHA +"') as f_hasta, to_char(blo_ui.UIBLOT_PEDIDO_FE_INI,'" +ConstantesGenerales.FORMATO_FECHA+ "') as f_desde, to_char(blo_ui.UIBLOT_HORARIO_DESDE, 'HH:MI:SS,FF6 am') as h_desde, to_char(blo_ui.UIBLOT_HORARIO_HASTA, 'HH:MI:SS,FF6 am') as h_hasta, ");
		sql.append(" to_char(blo_ui.UIBLOT_HORARIO_DESDE_ULT_DIA, 'HH:MI:SS am') as h_ultdia_desde, to_char(blo_ui.UIBLOT_HORARIO_HASTA_ULT_DIA, 'HH:MI:SS am') as h_ultdia_hasta ");
		sql.append(" from INFI_TB_107_UI_BLOTTER blo_ui, INFI_TB_102_BLOTER blot ");
		sql.append(" where blo_ui.undinv_id = ").append(idUnidadInversion);
		sql.append(" and blo_ui.bloter_id = '").append(idBlotter).append("'");
		sql.append(" and blo_ui.bloter_id = blot.bloter_id");
		
		dataSet = db.get(dataSource, sql.toString());
	}
	
	/**
	 * Lista todos los blotters asociados a una unidad de inversi&oacute;n
	 * @param idUnidadInversion
	 * @throws Exception
	 */
	public void listarBlottersUI(long idUnidadInversion) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("select blo_ui.*, blo.*, '' as selected from INFI_TB_107_UI_BLOTTER blo_ui, INFI_TB_102_BLOTER blo");
		sql.append(" where blo_ui.undinv_id = ").append(idUnidadInversion);	
		sql.append(" and blo_ui.bloter_id = blo.bloter_id");
		sql.append(" order by blo.bloter_descripcion");
		
		dataSet = db.get(dataSource, sql.toString());
	}

	
	/**
	 * Modificar los datos propios de la asociacion entre un Blotter y una Unidad de Inversion
	 * @param beanUIBlotter : bean asociacion UnidadInversion-Blotter con la data a registrar
	 * @return codigo de retorno
	 * @throws Exception
	 */
	public int modificar(UIBlotter beanUIBlotter) throws Exception {

		StringBuffer sqlSB = new StringBuffer("update INFI_TB_107_UI_BLOTTER set ");		
		
		sqlSB.append("uiblot_in_recompra = ").append(beanUIBlotter.getIndicaRecompra()).append(", ");
		sqlSB.append("uiblot_horario_desde = ");
		String horario = Util.replace(formatoHora,"@horario@",beanUIBlotter.getHorarioDesde());
		sqlSB.append(horario).append(", ");	
		sqlSB.append("uiblot_horario_hasta = ");
		horario = Util.replace(formatoHora,"@horario@",beanUIBlotter.getHorarioHasta());
		sqlSB.append(horario).append(", ");	
		sqlSB.append("uiblot_horario_desde_ult_dia = ");
		horario = Util.replace(formatoHora,"@horario@",beanUIBlotter.getHorarioDesdeUltDia());
		sqlSB.append(horario).append(", ");	
		sqlSB.append("uiblot_horario_hasta_ult_dia = ");
		horario = Util.replace(formatoHora,"@horario@",beanUIBlotter.getHorarioHastaUltDia());
		sqlSB.append(horario).append(", ");			
		sqlSB.append("uiblot_pedido_fe_ini = ");
		sqlSB.append("to_date('").append(sdIODate.format(beanUIBlotter.getPeriodoInicial())).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("'), ");			
		sqlSB.append("uiblot_pedido_fe_fin = ");
		sqlSB.append("to_date('").append(sdIODate.format(beanUIBlotter.getPeriodoFinal())).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("') ");
		if (beanUIBlotter.getGananciaRed().doubleValue() > 0) {
			sqlSB.append(", uiblot_ganancia_red = ").append(beanUIBlotter.getGananciaRed()).append(" ");			
		}
		
		sqlSB.append("where undinv_id = ");
		sqlSB.append(beanUIBlotter.getIdUnidadInversion()).append(" ");		
		sqlSB.append("and bloter_id = ");
		sqlSB.append("'").append(beanUIBlotter.getIdBlotter()).append("' ");			
	
		db.exec(dataSource, sqlSB.toString());		
		return 0;
	}
	
	
	/**
	 * Obtiene un objeto con todos los datos del blotter asociado a la unidad de inversión
	 * @param idUnidadInversion
	 * @param idBlotter
	 * @return objeto de tipo UIBlotter
	 * @throws Exception
	 */
	public UIBlotter obtenerBlotterUI(long idUnidadInversion, String idBlotter) throws Exception{
		UIBlotter uiBlotter;
		StringBuffer sqlSB = new StringBuffer("SELECT blo_ui.*, to_char(blo_ui.UIBLOT_HORARIO_DESDE, '").append(ConstantesGenerales.FORMATO_HORA_24).append("') as h_desde, to_char(blo_ui.UIBLOT_HORARIO_HASTA, '").append(ConstantesGenerales.FORMATO_HORA_24).append("') as h_hasta, " );
		sqlSB.append(" to_char(blo_ui.UIBLOT_HORARIO_DESDE_ULT_DIA, '").append(ConstantesGenerales.FORMATO_HORA_24).append("') as h_ultdia_desde, to_char(blo_ui.UIBLOT_HORARIO_HASTA_ULT_DIA, '").append(ConstantesGenerales.FORMATO_HORA_24).append("') as h_ultdia_hasta, blo.BLOTER_DESCRIPCION, blo.BLOTER_IN_RESTRINGIDO ");
		sqlSB.append(" FROM INFI_TB_107_UI_BLOTTER blo_ui, INFI_TB_102_BLOTER blo ");
		sqlSB.append(" WHERE blo_ui.UNDINV_ID=").append(idUnidadInversion);
		sqlSB.append(" AND blo_ui.BLOTER_ID='").append(idBlotter).append("' ");	
		sqlSB.append(" AND blo_ui.BLOTER_ID = blo.BLOTER_ID ");
		
		try{
			conn = dataSource.getConnection();
			this.statement = conn.createStatement();
			resultSet = statement.executeQuery(sqlSB.toString());
			if(resultSet.next()){
				uiBlotter = new UIBlotter();
				uiBlotter.setIdBlotter(resultSet.getString("BLOTER_ID"));
				uiBlotter.setIdUnidadInversion(resultSet.getLong("UNDINV_ID"));
				uiBlotter.setIndicaDisponible(resultSet.getInt("UIBLOT_IN_DISPONIBLE"));
				uiBlotter.setHorarioDesde(resultSet.getString("h_desde"));
				uiBlotter.setHorarioHasta(resultSet.getString("h_hasta"));
				uiBlotter.setHorarioDesdeUltDia(resultSet.getString("h_ultdia_desde"));
				uiBlotter.setHorarioHastaUltDia(resultSet.getString("h_ultdia_hasta"));
				uiBlotter.setPeriodoInicial(resultSet.getDate("uiblot_pedido_fe_ini"));
				uiBlotter.setPeriodoFinal(resultSet.getDate("uiblot_pedido_fe_fin"));	
				uiBlotter.setIndicaRecompra(resultSet.getInt("UIBLOT_IN_RECOMPRA"));
				uiBlotter.setGananciaRed(resultSet.getBigDecimal("UIBLOT_GANANCIA_RED"));
				uiBlotter.setIndicaTesoreria(resultSet.getInt("UIBLOT_IN_TESORERIA"));
				uiBlotter.setIndicaBlotterDefecto(resultSet.getInt("UIBLOT_IN_DEFECTO"));
				uiBlotter.setDescripcionBlotter(resultSet.getString("BLOTER_DESCRIPCION"));
				uiBlotter.setIndicaRestringido(resultSet.getInt("BLOTER_IN_RESTRINGIDO"));
			}else{
				throw new Exception("No se encontraron los datos del blotter");
			}
			
		} catch (Exception e){
			throw e;
		} finally{
			this.closeResources();
			this.cerrarConexion();
		}		
		
		return uiBlotter;
	}
	
}
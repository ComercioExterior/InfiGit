package com.bdv.infi.dao;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.sql.DataSource;

import megasoft.db;


/** 
 * Clase destinada para el manejo l&oacute;gico de inserci&oacute;n, recuperaci&oacute;n y listado de Indicadores asociados a unidades de inversi&oacute;n almacenados en las tablas de trabajo
 */
public class UIIndicadoresDAO extends com.bdv.infi.dao.GenericoDAO {
	/**
	 * SubQuery para rechazar los indicadores ya seleccionados
	 */
	private static String subSql = "(select indica_id from INFI_TB_110_UI_INDICADORES where undinv_id = ";
	/**
	 * Listas de Indicadores segun la accion a realizar con ellos
	 */
	private HashMap asociacionEliminar = new HashMap();		
	private HashMap asociacionIngresar = new HashMap();		
	private HashMap asociacionModificar = new HashMap();
	/**
	 * Valores de la Asociacion
	 */
	private String [] idsIndicador;
	private String [] idsIndicadorPrevio;
	private int [] indicaRequerido;
	private int [] indicaRequeridoPrevio;	

	/**
	 * Obtener los Indicadores a ser procesados
	 * @param idsIndicador
	 */
	public void setIdsIndicador(String[] idsIndicador) {
		this.idsIndicador = idsIndicador;
	}

	/**
	 * Obtener los valores del indicador de Requerido que tenia asignado la asociacion
	 * @param idsIndicadorPrevio
	 */
	public void setIdsIndicadorPrevio(String[] indicaRequeridoPrevio) {
		this.idsIndicadorPrevio = indicaRequeridoPrevio;
	}

	/**
	 * Obtener los valores del IndicaRequerido a ser asignado a la asociacion
	 * @param indicaRequerido
	 */
	public void setIndicaRequerido(int[] indicaRequerido) {
		this.indicaRequerido = indicaRequerido;
	}

	/**
	 * Obtener los valores del IndicaRequeridoPrevio a ser asignado a la asociacion
	 * @param indicaRequeridoPrevio
	 */
	public void setIndicaRequeridoPrevio(int[] indicaRequeridoPrevio) {
		this.indicaRequeridoPrevio = indicaRequeridoPrevio;
	}	

	/**
	 * Constructor de la clase.
	 * Permite inicializar el DataSource para los accesos a la base de datos
	 * @param ds : DataSource 
	 * @throws Exception
	 */
	public UIIndicadoresDAO(DataSource ds) throws Exception {
		super(ds);
	}
	
	public UIIndicadoresDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
	}
	
	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Actualizacion de las asociaciones de una Unidad de Inversion y Titulos
	 * @param idUnidadInversion : identificador de la Unidad de Inversion
	 * @throws Exception
	 */
	public boolean actualizaAsociacion (long idUnidadInversion) throws Exception {
		
		boolean indicaModificacion = false;
		int i = 0;

		// Analizar la lista de Titulo comparando el indicaRequerido anterior vs el nuevo
		//	1.-	Eliminar asociacion : anterior <> -1 y nuevo = -1
		//	2.- Ingresar asociacion : anterior = -1 y nuevo <> -1
		//	3.-	Modificar data de la asociacion anterior <> nuevo
		// ---------------------------------------------------------------------------
		for (i = 0; i < idsIndicador.length; i++) {			
			if (idsIndicadorPrevio[i].equals("-1")&& idsIndicador[i].equals("-1"))
				continue;
			// Antes existian y ahora no
			if (!idsIndicadorPrevio[i].equals("-1") && idsIndicador[i].equals("-1")) {
				asociacionEliminar.put(idsIndicadorPrevio[i], idsIndicadorPrevio[i]);
			} 
			// Antes no existian y ahora si
			else if (idsIndicadorPrevio[i].equals("-1") && !idsIndicador[i].equals("-1")) {
				asociacionIngresar.put(idsIndicador[i], new Integer(i));
			} 
			// Modificar datos
			else {
				if (indicaRequeridoPrevio[i] != indicaRequerido[i]) {
					asociacionModificar.put(idsIndicador[i], new Integer(i));
				}
			} 			
		}

		// Determinar si hay actualizaciones a aplicar
		int ca=0;
		if (asociacionEliminar.size() > 0)
			++ca;		
		if (asociacionIngresar.size() > 0)
			ca += asociacionIngresar.size();
		if (asociacionModificar.size() > 0)
			ca += asociacionModificar.size();	
		
		if (ca == 0)
			return indicaModificacion;
		
		String [] batch = new String [ca];
		ca = 0;
		if (asociacionEliminar.size() > 0) {
			batch [0] = eliminar(idUnidadInversion);
			++ca;
		}
		String [] batchAux = null;
		if (asociacionIngresar.size() > 0) {
			batchAux = insertar(idUnidadInversion);
			System.arraycopy(batchAux, 0, batch, ca, batchAux.length);
			ca += batchAux.length;
		}
		if (asociacionModificar.size() > 0) {
			batchAux = modificar(idUnidadInversion);
			System.arraycopy(batchAux, 0, batch, ca, batchAux.length);			
		}
		
		for (i=0; i<batch.length; i++) {
		}
		db.execBatch(dataSource, batch);
		
		indicaModificacion = true;
		return indicaModificacion;
	}
	
	
	/**
	 * Lista todos los indicadores encontrados en la base de datos, que no esten asociados a la Unidad de Inversion 
	 * Almacena el resultado en un dataset
	 * @param idUnidadInversion : identificador de la Unidad de Inversion
	 * @throws Exception arroja una excepci&oacute;n en caso de un error
	 * */
	public void listarIndicadores(long idUnidadInversion) throws Exception {
		
		StringBuffer sqlSB = new StringBuffer();
		sqlSB.append("select indica.*, ' ' as selecc, ");	
		sqlSB.append("case when indica_in_requerido = 0 then ' ' else 'checked' end as selecc_requerido, ");		
		sqlSB.append("'disabled' as disabled, 0 as fila ");			
		sqlSB.append("from INFI_TB_011_INDICADORES indica ");
		sqlSB.append("where indica_id not in ").append(subSql).append(idUnidadInversion+") ");
		sqlSB.append(" and indica_in_requisito=0 ");
		sqlSB.append("order by indica_descripcion");
		dataSet = db.get(dataSource, sqlSB.toString());
	}
	
	/**
	 * Lista los requisitos disponibles que se pueden asociar a la unidad de inversiòn 
	 * @param idUnidadInversion id de la unidad de inversión
	 * @throws Exception en caso de error
	 */
	public void listarRequisitos(long idUnidadInversion) throws Exception {		
		StringBuffer sqlSB = new StringBuffer();
		sqlSB.append("select indica.*, ' ' as selecc, ");	
		sqlSB.append("case when indica_in_requerido = 0 then ' ' else 'checked' end as selecc_requerido, ");		
		sqlSB.append("'disabled' as disabled, 0 as fila ");			
		sqlSB.append("from INFI_TB_011_INDICADORES indica ");
		sqlSB.append("where indica_id not in ").append(subSql).append(idUnidadInversion+") ");
		sqlSB.append(" and indica_in_requisito=1 ");
		sqlSB.append("order by indica_descripcion");
		dataSet = db.get(dataSource, sqlSB.toString());
	}	
	
	/**
	 * Lista todos los indicadores asociados a una unidad de inversion encontrados en la base de datos. 
	 * Almacena el resultado en un dataset
	 * @param idUnidadInversion : identificador de la Unidad de Inversion
	 * @throws Exception arroja una excepci&oacute;n en caso de un error
	 * */
	private void listarIndicadoresPorUiTipo(long idUnidadInversion, String tipo) throws Exception {
		
		StringBuffer sqlSB = new StringBuffer();
		sqlSB.append("select indica.indica_in_requisito, case when indica_in_requisito = 1 then 'checked' ");
		sqlSB.append(" else '' end as selecc_requisito, uiindi.*, indica_descripcion, ");		
		sqlSB.append("case when indica_in_requerido = 1 then 'checked' when uiindi_id_requerido = 1 then 'checked' else ' ' end as selecc_requerido, ");		
		sqlSB.append("case when indica_in_requerido = 1 then 'disabled' else ' ' end as disabled, ");
		sqlSB.append("0 as fila, 'checked' selecc ");			
		sqlSB.append("from INFI_TB_110_UI_INDICADORES uiindi ");
		sqlSB.append("inner join INFI_TB_011_INDICADORES indica on indica.indica_id = uiindi.indica_id ");
		sqlSB.append("where uiindi.undinv_id = ").append(idUnidadInversion);
		if (tipo != null){
			sqlSB.append(" and indica_in_requisito=" + tipo);
		}
		
		sqlSB.append(" order by indica_descripcion");
		dataSet = db.get(dataSource, sqlSB.toString());
	}
	
	public void listarIndicadoresPorID(long idUnidadInversion) throws Exception {
		listarIndicadoresPorUiTipo(idUnidadInversion,null);		
	}
	
	public void listarRequisitosPorUi(long idUnidadInversion) throws Exception {
		listarIndicadoresPorUiTipo(idUnidadInversion,"1");		
	}
	
	public void listarIndicadoresPorUi(long idUnidadInversion) throws Exception {
		listarIndicadoresPorUiTipo(idUnidadInversion,"0");		
	}	
	
	/**
	 * Eliminar las asociaciones entre indicadores y una unidad de inversion de la base de datos
	 * @param idUnidadInversion : identificador de la Unidad de Inversion
	 * @throws Exception
	*/
	private String eliminar(long idUnidadInversion) throws Exception {
	
		StringBuffer sqlSB = new StringBuffer("delete from INFI_TB_110_UI_INDICADORES ");		
		sqlSB.append("where undinv_id = ").append(idUnidadInversion+" ");
		sqlSB.append("and indica_id in (");
		
		Iterator it = asociacionEliminar.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry)it.next();
			sqlSB.append("'").append(e.getValue()).append("', " );
		}		
		String sql = sqlSB.substring(0,sqlSB.length()-2) + ")";

		return sql;
	}

	/**
	 * Ingresar las asociaciones entre indicadores y una unidad de inversion de la base de datos
	 * @param idUnidadInversion : identificador de la Unidad de Inversion
	 * @throws Exception
	*/
	private String [] insertar(long idUnidadInversion) throws Exception {
		
		StringBuffer sqlSB = new StringBuffer("insert into INFI_TB_110_UI_INDICADORES ");		
		sqlSB.append("(undinv_id, indica_id, uiindi_id_requerido) ");
		sqlSB.append("values (");
		sqlSB.append(idUnidadInversion+", ");

		Integer pos;	
		String [] batch = new String [asociacionIngresar.size()];

		StringBuffer sqlAux = new StringBuffer();
		Iterator it = asociacionIngresar.entrySet().iterator();
		int i = 0;
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry)it.next();
			pos = (Integer) e.getValue();			
			
			sqlAux.append(sqlSB);
			sqlAux.append("'").append(e.getKey()).append("', " );
			sqlAux.append(String.valueOf(indicaRequerido[pos.intValue()])).append(" " );
			
			batch[i] = sqlAux.toString() + ")";
			++i;
			sqlAux.delete(0, sqlAux.length());
		}		

		return batch;
    }
	
	/**
	 * Modificar los datos propios de las relaciones entre indicadores y una unidad de inversion
	 * @param idUnidadInversion : identificador de la Unidad de Inversion
	 * @return codigo de retorno
	 * @throws Exception
	 */
	public String []  modificar(long idUnidadInversion) throws Exception {

		StringBuffer sqlSB = new StringBuffer("update INFI_TB_110_UI_INDICADORES set ");		
		
		int ca = 0; 
		Integer pos;	
		String [] batch = new String [asociacionModificar.size()];

		StringBuffer sqlAux = new StringBuffer();
		Iterator it = asociacionModificar.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry)it.next();
			pos = (Integer) e.getValue();			
			
			sqlAux.append(sqlSB);		
			sqlAux.append("uiindi_id_requerido = ").append(String.valueOf(indicaRequerido[pos.intValue()])).append(" " );
			sqlAux.append("where undinv_id =").append(idUnidadInversion+" ");
			sqlAux.append("and indica_id =").append("'").append(e.getKey()).append("' " );				
			
			batch[ca] = sqlAux.toString();
			sqlAux.delete(0, sqlAux.length());
		}		

		return batch;
	}
}

	package com.bdv.infi.dao;
	
	import java.math.BigDecimal;
import java.util.Date;

import javax.sql.DataSource;

import megasoft.db;
	
	
	/** 
	 * Clase destinada para el manejo l&oacute;gico de inserci&oacute;n, recuperaci&oacute;n y listado de los Titulos asociados a unidades de inversi&oacute;n almacenados en las tablas de trabajo
	 */
	public class UITitulosDAO extends com.bdv.infi.dao.GenericoDAO {

		/**
		 * SubQuery para rechazar los titulos ya seleccionados
		 */
		private static String subSql = "(select titulo_id from INFI_TB_108_UI_TITULOS where undinv_id = ";
		
		/**
		 * Valores de las Asociacion
		 */
		private String [] idsTitulos;
		private BigDecimal [] porcentajeNuevo;
		private BigDecimal [] valorNominalTitulo;
		private String [] idsMoneda;		
		private BigDecimal [] impDebBan;
		private int []indControlDisponible;
		
		public int[] getIndControlDisponible() {
			return indControlDisponible;
		}

		public void setIndControlDisponible(int[] indControlDisponible) {
			this.indControlDisponible = indControlDisponible;
		}

		/**
		 * Obtener el indicador de Impuesto Debito Bancario
		 * @param impDebBan
		 */
		public void setImpDebBan(BigDecimal[] impDebBan) {
			this.impDebBan = impDebBan;
		}
	
		/**
		 * Obtener los Titulos a ser procesados
		 * @param idsTitulos
		 */
		public void setIdsTitulos(String[] idsTitulos) {
			this.idsTitulos = idsTitulos;
		}
	
		/**
		 * Asignar los valores del Porcentaje a ser asignado a la asociacion
		 * @param porcentajeNuevo
		 */
		public void setPorcentajeNuevo(BigDecimal[] porcentajeNuevo) {
			this.porcentajeNuevo = porcentajeNuevo;
		}
		
		/**
		 * Asignar el Valor Nominal del Titulo de la asociacion
		 * @param valorNominalTitulo
		 */
		public void setValorNominalTitulo(BigDecimal[] valorNominalTitulo) {
			this.valorNominalTitulo = valorNominalTitulo;
		}
	
		/**
		 * Asignar el id de la Moneda de Denominacion del Titulo
		 * @param valorNominalTitulo
		 */
		public void setIdsMoneda(String[] idsMoneda) {
			this.idsMoneda = idsMoneda;
		}

		/**
		 * Constructor de la clase.
		 * Permite inicializar el DataSource para los accesos a la base de datos
		 * @param ds : DataSource 
		 * @throws Exception
		 */
		public UITitulosDAO(DataSource ds) throws Exception {
			super(ds);
		}
		
		public UITitulosDAO(Transaccion transaccion) throws Exception {
			super(transaccion);
		}
		
		public Object moveNext() throws Exception {
			// TODO Auto-generated method stub
			return null;
		}
	
		/**
		 * Eliminar la asociacion entre un titulo y una unidad de inversion de la base de datos
		 * @param idUnidadInversion : identificador de la Unidad de Inversion
		 * @param idTitulo : identificador del Titulo
		 * @throws Exception
		*/
		public int eliminar(long idUnidadInversion, String idTitulo) throws Exception {
		
			StringBuffer sqlSB = new StringBuffer("delete from INFI_TB_108_UI_TITULOS ");		
			sqlSB.append("where undinv_id = ").append(idUnidadInversion+" ");
			sqlSB.append("and titulo_id = ");
			sqlSB.append("'").append(idTitulo).append("' ");
			
			db.exec(dataSource, sqlSB.toString());
			return 0;
		}
	
		/**
		 * Ingresar la asociacion entre un Titulo y una unidad de inversion en la base de datos
		 * @param idUnidadInversion : identificador de la Unidad de Inversion
		 * @throws Exception
		*/
		public int insertar(long idUnidadInversion, String idMonedaUI, BigDecimal tasaCambio) throws Exception {
					
			StringBuffer sqlSB = new StringBuffer("insert into INFI_TB_108_UI_TITULOS ");		
			sqlSB.append("(undinv_id, titulo_id, uititu_porcentaje, uititu_valor_equivalente,uititu_in_conidb,UITITU_IN_CONTROL_DISPONIBLE) ");
			sqlSB.append("values (");
			sqlSB.append(idUnidadInversion+", ");
			
			String [] batch = new String [idsTitulos.length];
	
			StringBuffer sqlAux = new StringBuffer();
			for (int i=0; i<idsTitulos.length; i++)	{
				if (!idsMoneda[i].equals(idMonedaUI)) {
					valorNominalTitulo[i].multiply(tasaCambio);
				}			
				sqlAux.append(sqlSB);
				sqlAux.append("'").append(idsTitulos[i]).append("', " );
				sqlAux.append(porcentajeNuevo[i].toString()).append(", " );
				sqlAux.append(valorNominalTitulo[i].toString()).append(", ");
				if(impDebBan!=null&&impDebBan.length>0){
					sqlAux.append(impDebBan[i].toString()).append(", ");
				}else{
					sqlAux.append("0").append(", ");
				}	
				if(indControlDisponible!=null&&indControlDisponible.length>0){
					sqlAux.append(indControlDisponible[i]);
				}else{
					sqlAux.append("0");
				}
				
				
				batch[i] = sqlAux.toString() + ")";
				sqlAux.delete(0, sqlAux.length());
			}	
			db.execBatch(dataSource, batch);
			return 0;
	    }
		
		/**
		 * Lista todos los t&iacute;tulos encontrados en la base de datos que no estan asociados a la unidad de inversion
		 * Almacena el resultado en un dataset
		 * @param descripcion : prefijo para buscar los titulos		
		 * @param idUnidadInversion : identificador de la Unidad de Inversion
		 * @throws Exception arroja una excepci&oacute;n en caso de un error
		 * */
		public void listarTitulosDescripcion(String descripcion, long idUnidadInversion) throws Exception {
			
			StringBuffer sqlSB = new StringBuffer();
			sqlSB.append("select tit.*, 0 as uititu_porcentaje, ");		
			sqlSB.append("0 as fila, 'readonly' as readonly ");			
			sqlSB.append("from INFI_TB_100_TITULOS tit ");	
			sqlSB.append("where lower(tit.titulo_descripcion) like '%").append(descripcion.toLowerCase()).append("%' ");
			sqlSB.append("and titulo_id not in ").append(subSql).append(idUnidadInversion+") ");			
			sqlSB.append("order by titulo_descripcion, tit.titulo_fe_emision");
			
			dataSet = db.get(dataSource, sqlSB.toString());
		}
		
		/**
		 * Lista todos los t&iacute;tulos encontrados en la base de datos que no estan asociados a la unidad de inversion
		 * Almacena el resultado en un dataset
		 * @param fechaEmisionDesde : fecha desde base para la consulta
		 * @param fechaEmisionHasta : fecha hasta base para la consulta
		 * @param idUnidadInversion : identificador de la Unidad de Inversion
		 * @throws Exception arroja una excepci&oacute;n en caso de un error
		 * */
		public void listarTitulosFechaEmision(Date fechaEmisionDesde, Date fechaEmisionHasta, long idUnidadInversion) throws Exception {
			
			StringBuffer sqlSB = new StringBuffer();
			sqlSB.append("select tit.*, 0 as uititu_porcentaje, ");		
			sqlSB.append("0 as fila, 'readonly' as readonly ");			
			sqlSB.append("from INFI_TB_100_TITULOS tit ");	
			sqlSB.append("where tit.titulo_fe_emision between ");
			sqlSB.append(formatearFechaBD(fechaEmisionDesde));
			sqlSB.append(" and ");
			sqlSB.append(formatearFechaBD(fechaEmisionHasta));	
			sqlSB.append("and titulo_id not in ").append(subSql).append(idUnidadInversion+") ");
			sqlSB.append("order by tit.titulo_fe_emision, titulo_descripcion");
			
			dataSet = db.get(dataSource, sqlSB.toString());
		}
		
		/**
		 * Lista todos los t&iacute;tulos asociados una unidad de inversion encontrados en la base de datos. 
		 * Almacena el resultado en un dataset
		 * @param idUnidadInversion : identificador de la Unidad de Inversion
		 * @throws Exception arroja una excepci&oacute;n en caso de un error
		 * */
		public void listarTitulosPorID(long idUnidadInversion) throws Exception {
			
			StringBuffer sqlSB = new StringBuffer();
			sqlSB.append("select tit.*, uititu_valor_equivalente, to_char(uititu_porcentaje) as uititu_porcentaje, UITITU_IN_CONIDB, '1' as valor,  ");	
			sqlSB.append("'' as valorNominalEdited, '' as valorEquivalenteEdited, 0 as fila, 'checked' as selecc, '' as readonly,UITIT.UITITU_IN_CONTROL_DISPONIBLE ");			
			sqlSB.append("from INFI_TB_108_UI_TITULOS uitit ");
			sqlSB.append("inner join INFI_TB_100_TITULOS tit on uitit.titulo_id = tit.titulo_id ");		
			sqlSB.append("where uitit.undinv_id = "+idUnidadInversion+" ");
			sqlSB.append("order by titulo_descripcion, tit.titulo_fe_emision");

			System.out.println("listarTitulosPorID: "+sqlSB.toString());
			dataSet = db.get(dataSource, sqlSB.toString());
		}
		
		/**
		 * Lista todos los t&iacute;tulos asociados una unidad de inversion encontrados en la base de datos. 
		 * NO consulta los títulos en la vista de OPICS (INFI_TB_100_TITULOS)
		 * Método agregado para la consulta de emisiones en la SUBASTA DE DIVISAS
		 * Almacena el resultado en un dataset
		 * @param idUnidadInversion : identificador de la Unidad de Inversion
		 * @throws Exception arroja una excepci&oacute;n en caso de un error
		 * */
		public void listarSubastas(long idUnidadInversion) throws Exception {			
			StringBuffer sqlSB = new StringBuffer();
			sqlSB.append("SELECT TITULO_ID,TITULO_ID AS TITULO_DESCRIPCION,TO_CHAR (UITITU_PORCENTAJE) AS UITITU_PORCENTAJE,UITITU_VALOR_EQUIVALENTE,UITITU_VALOR_EQUIVALENTE AS TITULO_VALOR_NOMINAL,UI.MONEDA_ID AS TITULO_MONEDA_DEN");	
			sqlSB.append(" ,UITITU_IN_CONIDB,'CHECKED' AS SELECC,'' AS READONLY, '1' AS VALOR,'' AS VALORNOMINALEDITED, '' AS VALOREQUIVALENTEEDITED,0 AS FILA, UITIT.UITITU_IN_CONTROL_DISPONIBLE");
			sqlSB.append(" FROM INFI_TB_106_UNIDAD_INVERSION UI, INFI_TB_108_UI_TITULOS UITIT WHERE UITIT.UNDINV_ID = UI.UNDINV_ID AND UI.UNDINV_ID= "+idUnidadInversion+" ");			
			sqlSB.append(" ORDER BY TITULO_DESCRIPCION");
			System.out.println("listarTitulosPorID: "+sqlSB.toString());
			dataSet = db.get(dataSource, sqlSB.toString());
		}
		
		/**
		 * Lista todos los t&iacute;tulos asociados una unidad de inversion encontrados en la base de datos. 
		 * consulta tambien los títulos en la vista de OPICS (INFI_TB_100_TITULOS)
		 * Método agregado para la consulta de emisiones en la SUBASTA DE DIVISAS
		 * Almacena el resultado en un dataset
		 * @param idUnidadInversion : identificador de la Unidad de Inversion
		 * @throws Exception arroja una excepci&oacute;n en caso de un error
		 * */
		public int listarTitulosYSubastas(long idUnidadInversion) throws Exception {			
			StringBuffer sqlSB = new StringBuffer();
			sqlSB.append("SELECT TITULO_ID,TITULO_ID AS TITULO_DESCRIPCION,TO_CHAR (UITITU_PORCENTAJE) AS UITITU_PORCENTAJE,UITITU_VALOR_EQUIVALENTE,UITITU_VALOR_EQUIVALENTE AS TITULO_VALOR_NOMINAL,UI.MONEDA_ID AS TITULO_MONEDA_DEN");	
			sqlSB.append(" ,UITITU_IN_CONIDB,'CHECKED' AS SELECC,'' AS READONLY, '1' AS VALOR,'' AS VALORNOMINALEDITED, '' AS VALOREQUIVALENTEEDITED,0 AS FILA");
			sqlSB.append(" FROM INFI_TB_106_UNIDAD_INVERSION UI, INFI_TB_108_UI_TITULOS UITIT WHERE UITIT.UNDINV_ID = UI.UNDINV_ID AND UI.UNDINV_ID= "+idUnidadInversion+" ");			
			sqlSB.append(" UNION");			
			sqlSB.append(" SELECT TIT.TITULO_ID,TIT.TITULO_DESCRIPCION,TO_CHAR (UITITU_PORCENTAJE) AS UITITU_PORCENTAJE,UITITU_VALOR_EQUIVALENTE, TIT.TITULO_VALOR_NOMINAL, TIT.TITULO_MONEDA_DEN");	
			sqlSB.append(", UITITU_IN_CONIDB, 'CHECKED' AS SELECC,'' AS READONLY,'1' AS VALOR,'' AS VALORNOMINALEDITED,'' AS VALOREQUIVALENTEEDITED, 0 AS FILA ");			
			sqlSB.append(" FROM INFI_TB_108_UI_TITULOS UITIT, INFI_TB_100_TITULOS TIT ");
			sqlSB.append(" WHERE UITIT.TITULO_ID = TIT.TITULO_ID AND UITIT.UNDINV_ID = "+idUnidadInversion+" ");		
			sqlSB.append(" ORDER BY TITULO_DESCRIPCION");

			//System.out.println("listarTitulosPorID: "+sqlSB.toString());
			dataSet = db.get(dataSource, sqlSB.toString());
			return dataSet.count();
		}
	    /** (Metodo Sobre Escrito)
       * Lista todos los titulos asociados una unidad de inversion encontrados en la base de datos. 
       * Almacena el resultado en un dataset
       * @param idUnidadInversion : identificador de la Unidad de Inversion
       * @param idTitulo : identificador del ID del Titulo
       * @throws Exception arroja una excepci&oacute;n en caso de un error
       * */
      public void listarTitulosPorID(String idTitulo, long idUnidadInversion) throws Exception {
         
         StringBuffer sqlSB = new StringBuffer();
         sqlSB.append("select tit.*, 0 as uititu_porcentaje, ");     
         sqlSB.append("0 as fila, 'readonly' as readonly ");         
         sqlSB.append("from INFI_TB_100_TITULOS tit ");  
         sqlSB.append("where lower(tit.titulo_id) like '%").append(idTitulo.toLowerCase()).append("%' ");
         sqlSB.append("and titulo_id not in ").append(subSql).append(idUnidadInversion+") ");         
         sqlSB.append("order by titulo_descripcion, tit.titulo_fe_emision");
         dataSet = db.get(dataSource, sqlSB.toString());
      }
		/**
		 * Lista los titulos con solo informacion contenida en asociados una unidad de inversion encontrados en la base de datos. 
		 * Almacena el resultado en un dataset
		 * @param idUnidadInversion : identificador de la Unidad de Inversion
		 * @throws Exception arroja una excepci&oacute;n en caso de un error
		 * */
		public void listarTitulosPorUnidad(long idUnidadInversion) throws Exception {
			
			StringBuffer sqlSB = new StringBuffer();
			sqlSB.append("select tit.*, uititu_valor_equivalente, to_char(uititu_porcentaje) as uititu_porcentaje, ");	
			sqlSB.append("'' as valorNominalEdited, '' as valorEquivalenteEdited, 0 as fila, 'checked' as selecc, '' as readonly ");			
			sqlSB.append("from INFI_TB_108_UI_TITULOS uitit ");
			sqlSB.append("inner join INFI_TB_100_TITULOS tit on uitit.titulo_id = tit.titulo_id ");		
			sqlSB.append("where uitit.undinv_id = "+idUnidadInversion+" ");
			sqlSB.append("order by titulo_descripcion, tit.titulo_fe_emision");
			dataSet = db.get(dataSource, sqlSB.toString());
		}
		
		/**
		 * Lista la asociacion entre un Titulo y una unidad de inversion
		 * Almacena el resultado en un dataset
		 * @param idUnidadInversion : identificador de la Unidad de Inversion
		 * @param idTitulo : identificador del Titulo
		 * @throws Exception arroja una excepci&oacute;n en caso de un error
		 * */
		public void listarPorID(long idUnidadInversion, String idTitulo) throws Exception {
			
			StringBuffer sqlSB = new StringBuffer();
			sqlSB.append("select tit.*, uititu_valor_equivalente, uititu_porcentaje, uitit.*, ");	
			sqlSB.append("'' as valorEquivalenteEdited, 0 as fila, '' porcentajeEdited, 'checked' as selecc, '' as readonly ");			
			sqlSB.append("from INFI_TB_108_UI_TITULOS uitit ");
			sqlSB.append("inner join INFI_TB_100_TITULOS tit on uitit.titulo_id = tit.titulo_id ");		
			sqlSB.append("where uitit.undinv_id = "+idUnidadInversion+" ");
			sqlSB.append("and uitit.titulo_id = '"+idTitulo+"' ");			

			dataSet = db.get(dataSource, sqlSB.toString());
			
		}
		
		/**
		 * Modificar los datos propios de la asociacion entre un Titulo y una unidad de inversion
		 * @param idUnidadInversion : identificador de la Unidad de Inversion
		 * @return codigo de retorno
		 * @throws Exception
		 */
		public int modificar(long idUnidadInversion, String idMonedaUI, BigDecimal tasaCambio) throws Exception {
			
			StringBuffer sqlSB = new StringBuffer("update INFI_TB_108_UI_TITULOS set ");		

			String [] batch = new String [idsTitulos.length];
			StringBuffer sqlAux = new StringBuffer();
			for (int i=0; i<idsTitulos.length; i++)	{
				if (!idsMoneda[i].equals(idMonedaUI)) {
					valorNominalTitulo[i].multiply(tasaCambio);
				}	

				sqlAux.append(sqlSB);			
				sqlAux.append("uititu_porcentaje = ").append(porcentajeNuevo[i].toString()).append(", " );
				sqlAux.append("uititu_in_conidb = ").append(impDebBan[i]).append(", " );
				sqlAux.append("uititu_valor_equivalente = ").append(valorNominalTitulo[i].toString()).append(", ");
				sqlAux.append("uititu_in_control_disponible = ").append(indControlDisponible[i]).append(" ");
				sqlAux.append("where undinv_id =").append(idUnidadInversion+" ");
				sqlAux.append("and titulo_id =").append("'").append(idsTitulos[i]).append("' " );				
				
				batch[i] = sqlAux.toString();
				sqlAux.delete(0, sqlAux.length());
			}		
			db.execBatch(dataSource, batch);
			return 0;
		}
		
		public int consultarUITitulo(String idTitulo) throws Exception{
			StringBuffer sqlSB = new StringBuffer("");			
			sqlSB.append("SELECT TITULO_ID,TITULO_DESCRIPCION");
			sqlSB.append(" FROM INFI_TB_100_TITULOS TIT");
			sqlSB.append(" WHERE UPPER(TIT.TITULO_ID)=UPPER('"+idTitulo+"')");
			dataSet = db.get(dataSource, sqlSB.toString());
			return dataSet.count();
		}
		
	}

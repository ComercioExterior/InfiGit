package com.bdv.infi.dao;

import javax.sql.DataSource;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import megasoft.db;


/** 
 * Clase destinada para el manejo l&oacute;gico de inserci&oacute;n, recuperaci&oacute;n y listado de los Campos Dinamicos asociados las unidades de inversi&oacute;n almacenados en las tablas de trabajo
 */
public class UICamposDinamicosDAO extends com.bdv.infi.dao.GenericoDAO {
	/**
	 * SubQuery para rechazar los campos ya seleccionados
	 */
	private static String subSql = "(select campo_id from INFI_TB_114_UI_CAMPOS_DINAMIC where undinv_id = ";
	/**
	 * Valores de la Asociacion
	 */
	private long [] idsCampoDinamico;
	private long [] idsCampoDinamicoPrevio;	

	/**
	 * Obtener los CampoDinamicos a ser procesados
	 * @param idsCampoDinamico
	 */
	public void setIdsCampoDinamico(long[] idsCampoDinamico) {
		this.idsCampoDinamico = idsCampoDinamico;
	}

	/**
	 * Obtener los valores de los Campos Dinamicos que existian antes de una eliminacion
	 * @param idsCampoDinamicoPrevio
	 */
	public void setIdsCampoDinamicoPrevio(long[] idsCampoDinamicoPrevio) {
		this.idsCampoDinamicoPrevio = idsCampoDinamicoPrevio;
	}


	/**
	 * Constructor de la clase.
	 * Permite inicializar el DataSource para los accesos a la base de datos
	 * @param ds : DataSource 
	 * @throws Exception
	 */
	public UICamposDinamicosDAO(DataSource ds) throws Exception {
		super(ds);
	}
	
	public UICamposDinamicosDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
	}
	
	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Eliminar los campos dinamicos a requerimiento del usuario
	 * @param idUnidadInversion : identificador de la Unidad de Inversion
	 * @throws Exception
	*/
	public int eliminar(long idUnidadInversion) throws Exception {
		
		StringBuffer sqlSB = new StringBuffer("delete from INFI_TB_114_UI_CAMPOS_DINAMIC ");		
		sqlSB.append("where undinv_id = ").append(idUnidadInversion+" ");
		if (idsCampoDinamicoPrevio.length > 0) {
			sqlSB.append("and campo_id not in (");

			StringBuffer sqlAux = new StringBuffer();
			for (int i = 0; i < idsCampoDinamicoPrevio.length; i++) {
				sqlAux.append(idsCampoDinamicoPrevio[i] + ", ");
			}		
			sqlSB.append(sqlAux.substring(0,sqlAux.length()-2)).append(")");
		}
		db.exec(dataSource, sqlSB.toString());
		return 0;
    } 
	
	/**
	 * Ingresar los campos dinamicos a requerimiento del usuario
	 * @param idUnidadInversion : identificador de la Unidad de Inversion
	 * @throws Exception
	*/
	public int insertar(long idUnidadInversion) throws Exception {
		
		StringBuffer sqlSB = new StringBuffer("insert into INFI_TB_114_UI_CAMPOS_DINAMIC ");		
		sqlSB.append("(undinv_id, campo_id) ");
		sqlSB.append("values (");
		sqlSB.append(idUnidadInversion+", ");

		String [] batch = new String [idsCampoDinamico.length];

		StringBuffer sqlAux = new StringBuffer();

		for (int i = 0; i < idsCampoDinamico.length; i++) {
			sqlAux.append(sqlSB);
			sqlAux.append(idsCampoDinamico[i]+"" );
			batch[i] = sqlAux.toString() + ")";

			sqlAux.delete(0, sqlAux.length());
		}		

		db.execBatch(dataSource, batch);
		return 0;
    }
	
	/**
	 * Lista todos los campos encontrados en la base de datos, que no esten asociados a la unidad de inversion
	 * Almacena el resultado en un dataset
	 * @param idUnidadInversion : identificador de la Unidad de Inversion
	 * @throws Exception arroja una excepci&oacute;n en caso de un error
	 * */
	public void listarCampoDinamicos(long idUnidadInversion) throws Exception {
		
		StringBuffer sqlSB = new StringBuffer();
		sqlSB.append("select campo.*, CASE campo_tipo when '1' then 'General' when '2' then 'venta' when '3' then 'Fecha con Rango' when '4' then 'Fecha sin Validacion' when '5' then 'Fecha Mayor a' when '6' then 'Fecha Menor a' when '7'  then 'Pais' when '8' then 'Lista Dinamica' END as desc_campo_tipo, ' ' as selecc, 0 as fila ");			
		sqlSB.append("from INFI_TB_036_CAMPOS_DINAMICOS campo ");
		sqlSB.append("where campo_id not in ").append(subSql).append(idUnidadInversion+") ");				
		sqlSB.append("order by campo_nombre");
		
		dataSet = db.get(dataSource, sqlSB.toString());
	}
	
	/**
	 * Lista todos los campos dinamicos asociados a una unidad de inversion encontrados en la base de datos. 
	 * Almacena el resultado en un dataset
	 * @param idUnidadInversion : identificador de la Unidad de Inversion
	 * @throws Exception arroja una excepci&oacute;n en caso de un error
	 * */
	public void listarCampoDinamicosPorID(long idUnidadInversion) throws Exception {
		
		StringBuffer sqlSB = new StringBuffer();
		sqlSB.append("select campo.*, CASE campo_tipo when '1' then 'General' when '2' then 'venta' when '3' then 'Fecha con Rango' when '4' then 'Fecha sin Validacion' when '5' then 'Fecha Mayor a' when '6' then 'Fecha Menor a' when '7' then 'Pais' when '8' then 'Lista Dinamica' END as desc_campo_tipo , 0 as fila, 'checked' selecc ");			
		sqlSB.append("from INFI_TB_114_UI_CAMPOS_DINAMIC uicampo ");
		sqlSB.append("inner join INFI_TB_036_CAMPOS_DINAMICOS campo on campo.campo_id = uicampo.campo_id ");
		sqlSB.append("where uicampo.undinv_id = ").append(idUnidadInversion+" ");		
		sqlSB.append("order by campo.campo_id");
		//System.out.println("listarCampoDinamicosPorID: " +sqlSB.toString());
		dataSet = db.get(dataSource, sqlSB.toString());
	}
	
	/**
	 * Lista todos los campos dinamicos asociados a todas las unidades de inversion encontradas en la base de datos. 
	 * Almacena el resultado en un dataset
	 * @throws Exception arroja una excepci&oacute;n en caso de un error
	 * */
	public void listarCamposDinamicosParaUnidadesInv(int unidad) throws Exception {
		
		StringBuffer sqlSB = new StringBuffer();
		sqlSB.append("select u.undinv_nombre, c.campo_descripcion as descripcion, 'cd_'||lower(replace(c.campo_nombre,' ','_')) as nombre from INFI_TB_106_UNIDAD_INVERSION u, INFI_TB_036_CAMPOS_DINAMICOS c, INFI_TB_114_UI_CAMPOS_DINAMIC uc where u.undinv_id=uc.undinv_id and c.campo_id=uc.campo_id ");
		if (unidad!=0){
			sqlSB.append(" and uc.undinv_id=");
			sqlSB.append(unidad);
		}
		sqlSB.append(" order by u.undinv_nombre asc");
		dataSet = db.get(dataSource, sqlSB.toString());
	}
	
	/**
	 * Lista todos los campos dinamicos asociados a todas las unidades de inversion encontradas en la base de datos. 
	 * Almacena el resultado en un dataset
	 * el campo que titulo que devuelve el dataset debera ser SIEMPRE IGUAL al 
	 * campo titulo que retorna el dataset el metodo listaTitulosOrden ubicado en TitulosDAO
	 * @throws Exception arroja una excepci&oacute;n en caso de un error
	 * */
	public void listarTitulosParaUnidadesInv(int unidad) throws Exception {
		
		StringBuffer sqlSB = new StringBuffer();
		sqlSB.append("select distinct u.undinv_id, u.undinv_nombre, 'titulo_' || ");
		sqlSB.append(ConstantesGenerales.FORMATEO_ID_TITULO).append(" titulo, 'precio_' || ");
		sqlSB.append(ConstantesGenerales.FORMATEO_ID_TITULO).append(" precio, t.titulo_descripcion from INFI_TB_106_UNIDAD_INVERSION u, INFI_TB_108_UI_TITULOS titu, INFI_TB_100_TITULOS t where u.undinv_id=titu.undinv_id and titu.titulo_id=t.titulo_id");
		
		if (unidad!=0){
			sqlSB.append(" and u.undinv_id=").append(unidad);
		}
		sqlSB.append(" order by u.undinv_nombre asc");		
		dataSet = db.get(dataSource, sqlSB.toString());
	}
	
	/**
	 * Lista todas las unidades de inversion con campos dinamicos disponicles encontradas en la base de datos co. 
	 * Almacena el resultado en un dataset
	 * */
	public void listarUnidadesInv() throws Exception {
		
		StringBuffer sqlSB = new StringBuffer();
		sqlSB.append("select distinct u.undinv_id, u.undinv_nombre, '' as descripcion, '' as nombre from INFI_TB_106_UNIDAD_INVERSION u, INFI_TB_114_UI_CAMPOS_DINAMIC uc where u.undinv_id=uc.undinv_id order by u.undinv_nombre asc");			
		
		dataSet = db.get(dataSource, sqlSB.toString());
	}
	
	
}

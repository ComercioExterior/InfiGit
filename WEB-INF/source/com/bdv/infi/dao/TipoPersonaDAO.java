package com.bdv.infi.dao;

import javax.sql.DataSource;
import megasoft.db;
import com.bdv.infi.util.*;
import com.bdv.infi.data.TipoPersona;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

/** 
 * Clase que se conecta con la base de datos y busca los tipos de clientes registrados en configuraci&oacute;n. (V,E,J)
 */
public class TipoPersonaDAO extends com.bdv.infi.dao.GenericoDAO {

	
	public TipoPersonaDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
		// TODO Auto-generated constructor stub
	}
	
	public TipoPersonaDAO(DataSource ds) throws Exception {
		super(ds);
		// TODO Auto-generated constructor stub
	}


	/**
	 * Lista los tipos de clientes encontrados en la base de datos (V,E,J) 
	 * @throws Exception 
	*/
	public void listarTodos() throws Exception{
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM INFI_TB_200_TIPO_PERSONAS order by tipper_nombre asc");
		dataSet = db.get(dataSource, sql.toString());
		System.out.println("listarTodos "+sql);
	}
		/**
	 * Lista todos los tipos de clientes encontrados en la base de datos (V,E,J) 
	 * asociados o no a los blotters de una unidad de inversion especifica
	 * @throws Exception 
	*/
	public void listarPorUnidad(String unidad, String bloter) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");		
		sql.append("select br.bloter_id, p.tipper_nombre , p.tipper_id from INFI_TB_111_UI_BLOTTER_RANGOS br right join INFI_TB_200_TIPO_PERSONAS p on p.tipper_id=br.tipper_id and br.undinv_id=").append(unidad);
		if(bloter!=null){
			filtro.append(" and br.bloter_id='").append(bloter).append("'");
		}
		sql.append(filtro);
		sql.append(" group by br.bloter_id, p.tipper_nombre , p.tipper_id order by tipper_id ");
		dataSet = db.get(dataSource, sql.toString());
	}
	
	/**Lista solo los clientes (V,E,J) que esten 
	 * asociados a los blotters de una unidad de inversion especifica
	 * Modificado por NM25287. Inclusión del tipo de producto SUBASTA_DIVISA. SICAD
	 * Modificado por NM25287. Inclusión de tipos de producto SICAD2PER y SICAD2RED 20/03/2014
	 * @param unidad id de la unidad de inversion
	 * @param bloter id del blotter
	 * @throws Exception
	 */
	public void listarPorBlotterUnidad(String unidad, String bloter) throws Exception{

		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		sql.append("select distinct p.tipper_nombre, p.tipper_id, case when (select count(uidoc_unico) from INFI_TB_115_UI_DOC uo where br.undinv_id=uo.undinv_id and uo.bloter_id=br.bloter_id and uo.tipper_id=p.tipper_id)=").append(ConstantesGenerales.FALSO).append(" then 'Sin Asociar' when (select count(uidoc_unico) from INFI_TB_115_UI_DOC uo where br.undinv_id=uo.undinv_id and uo.bloter_id=br.bloter_id and uo.tipper_id=p.tipper_id)=(select case when ((insfin_forma_orden='").append(ConstantesGenerales.INST_TIPO_SUBASTA);
		sql.append("' OR insfin_forma_orden='").append(ConstantesGenerales.INST_TIPO_SUBASTA_COMPETITIVA).append("' OR insfin_forma_orden='").append(ConstantesGenerales.INST_FINANCIERO_SIMADI_TAQUILLA).append("' OR insfin_forma_orden='").append(ConstantesGenerales.INST_TIPO_SUBASTA_COMPETITIVA).append("') AND TIPO_PRODUCTO_ID in ( '").append(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA).append("','").append(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA).append("','").append(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL).append("','").append(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL).append("','").append(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL).append("')) then '7' when ((insfin_forma_orden='").append(ConstantesGenerales.INST_TIPO_SITME).append("' OR insfin_forma_orden='").append(ConstantesGenerales.INST_TIPO_SUBASTA);
		sql.append("' OR insfin_forma_orden='").append(ConstantesGenerales.INST_TIPO_SUBASTA_COMPETITIVA).append("' OR insfin_forma_orden='").append(ConstantesGenerales.INST_FINANCIERO_SIMADI_TAQUILLA).append("') AND TIPO_PRODUCTO_ID = '").append(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME).append("') then '8' when insfin_forma_orden='").append(ConstantesGenerales.INST_TIPO_INVENTARIO).append("' OR insfin_forma_orden='").append(ConstantesGenerales.INST_TIPO_INVENTARIO_CON_PRECIO).append("' then '2' end valor from INFI_TB_101_INST_FINANCIEROS fi, INFI_TB_106_UNIDAD_INVERSION u where u.insfin_id=fi.insfin_id and u.undinv_id=br.undinv_id) then 'Asociados' when (select count(uidoc_unico) from INFI_TB_115_UI_DOC uo where br.undinv_id=uo.undinv_id and uo.bloter_id=br.bloter_id and uo.tipper_id=p.tipper_id)<(select case when ((insfin_forma_orden='").append(ConstantesGenerales.INST_TIPO_SUBASTA);
		sql.append("' OR insfin_forma_orden='").append(ConstantesGenerales.INST_TIPO_SUBASTA_COMPETITIVA).append("' OR insfin_forma_orden='").append(ConstantesGenerales.INST_FINANCIERO_SIMADI_TAQUILLA).append("') AND TIPO_PRODUCTO_ID in ( '").append(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA).append("','").append(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA).append("','").append(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL).append("','").append(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL).append("','").append(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL).append("')) then '7' when ((insfin_forma_orden='").append(ConstantesGenerales.INST_TIPO_SITME).append("' OR insfin_forma_orden='").append(ConstantesGenerales.INST_TIPO_SUBASTA);
		sql.append("' OR insfin_forma_orden='").append(ConstantesGenerales.INST_TIPO_SUBASTA_COMPETITIVA).append("') AND TIPO_PRODUCTO_ID = '").append(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME).append("') then '8' when insfin_forma_orden='").append(ConstantesGenerales.INST_TIPO_INVENTARIO).append("' OR insfin_forma_orden='").append(ConstantesGenerales.INST_TIPO_INVENTARIO_CON_PRECIO).append("' then '2' end valor from INFI_TB_101_INST_FINANCIEROS fi, INFI_TB_106_UNIDAD_INVERSION u where u.insfin_id=fi.insfin_id and u.undinv_id=br.undinv_id) and (select count(uidoc_unico) from INFI_TB_115_UI_DOC uo where br.undinv_id=uo.undinv_id and uo.bloter_id=br.bloter_id and uo.tipper_id=p.tipper_id)>").append(ConstantesGenerales.FALSO).append(" then 'Falta Asociar' end estado from INFI_TB_200_TIPO_PERSONAS p, INFI_TB_111_UI_BLOTTER_RANGOS br where p.tipper_id=br.tipper_id and br.undinv_id=").append(unidad);
		if(bloter!=null){
			filtro.append(" and br.bloter_id='").append(bloter).append("'");
		}
		sql.append(filtro);		
		dataSet = db.get(dataSource, sql.toString());
	}
	
	/**
	 * Lista los tipos de clientes por el tipo seleccionado (V,E,J) 
	*/
	public void listarPorTipo(String tipo){
	
	}	
	
	public int insertar(TipoPersona tipoPersona) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int modificar(TipoPersona tipoPersona) {
		// TODO Auto-generated method stub
		return 0;
	}


	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}	
	
	

}

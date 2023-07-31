package com.bdv.infi.dao;

import javax.sql.DataSource;

import com.bdv.infi.data.BloterDefinicion;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;

import com.bdv.infi.logic.interfaces.TransaccionFinanciera;

import megasoft.db;

/** 
 * Clase para buscar, agregar, modificar y eliminar los bloter registrados en la base de datos;
 */
public class BlotterDAO extends com.bdv.infi.dao.GenericoDAO {

	  
	public BlotterDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
		// TODO Auto-generated constructor stub
	}
	
	
	public BlotterDAO(DataSource ds) {
		super(ds);
		// TODO Auto-generated constructor stub
	}


	/**
	 * Busca todos los bloter registrados en la base de datos. 
	 * @throws Exception 
	*/
	public void listar() throws Exception{	
		
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT BLOTER_ID, BLOTER_DESCRIPCION, TO_CHAR(BLOTER_HORARIO_DESDE,'DD/MM/YYYY HH24:MI:SS') AS BLOTER_HORARIO_DESDE, TO_CHAR(BLOTER_HORARIO_HASTA,'DD/MM/YYYY HH24:MI:SS') AS BLOTER_HORARIO_HASTA, BLOTER_IN_RESTRINGIDO, BLOTER_IN_CARTERA_PROPIA, TPPEVA_ID, CTESEG_ID, BLOTER_STATUS, BLOTER_IN_RED, ID_AGRUPACION FROM INFI_TB_102_BLOTER ORDER BY BLOTER_ID");
		dataSet = db.get(dataSource, sb.toString());		
	}
	
	/**
	 * Obtiene id del blotter perteneciente al de red o por defecto.
	 * Devuelve 0 en caso de no encontrar ningún blotter por defecto o de red 
	 * @throws Exception 
	*/
	public String listarBlotterDeRed() throws Exception{		
		String valorRetorno = "0";
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT BLOTER_ID FROM INFI_TB_102_BLOTER WHERE BLOTER_IN_RED=").append(ConstantesGenerales.VERDADERO);
		System.out.println("listarBlotterDeRed : " + sb.toString());
		dataSet = db.get(dataSource, sb.toString());
		if (dataSet.count()!=0){
			dataSet.first();
			dataSet.next();
			valorRetorno = dataSet.getValue("BLOTER_ID");
		}
		return valorRetorno;
	}	
	
	/**
	 * Busca los bloter en la base de datos por el id recibido.  
	 * @throws Exception 
	*/
	public void listar(String id_bloter) throws Exception{

		StringBuffer sb = new StringBuffer();
		sb.append("SELECT BLOTER_ID,BLOTER_ID, BLOTER_DESCRIPCION,TO_CHAR(BLOTER_HORARIO_DESDE,'hh:miam') AS HORARIO_DESDE, TO_CHAR(BLOTER_HORARIO_HASTA,'hh:miam') AS HORARIO_HASTA, TO_CHAR(BLOTER_HORARIO_DESDE,'DD-MM-YYYY') AS BLOTER_HORARIO_DESDE, TO_CHAR(BLOTER_HORARIO_HASTA,'DD-MM-YYYY') AS BLOTER_HORARIO_HASTA, BLOTER_IN_RESTRINGIDO, BLOTER_IN_CARTERA_PROPIA, TPPEVA_ID, CTESEG_ID, BLOTER_STATUS, BLOTER_IN_RED, ID_AGRUPACION,ID_CANAL CANAL_ID FROM INFI_TB_102_BLOTER WHERE BLOTER_ID ='").append(id_bloter).append("' ORDER BY INFI_TB_102_BLOTER.BLOTER_ID");
		dataSet = db.get(dataSource, sb.toString());
		System.out.println("listar "+sb);
				
	}
	
	/***
	 * metodo para buscar un blotter pordescricion del mismo.
	 * @param String descripcion
	 * ***/
	
	public void listarDescripcion(String descripcion) throws Exception{

		StringBuffer sb = new StringBuffer();
		sb.append("SELECT BLOTER_ID, BLOTER_DESCRIPCION,TO_CHAR(BLOTER_HORARIO_DESDE,'hh:miam') AS HORARIO_DESDE, TO_CHAR(BLOTER_HORARIO_HASTA,'hh:miam') AS HORARIO_HASTA, TO_CHAR(BLOTER_HORARIO_DESDE,'DD-MM-YYYY') AS BLOTER_HORARIO_DESDE, TO_CHAR(BLOTER_HORARIO_HASTA,'DD-MM-YYYY') AS BLOTER_HORARIO_HASTA, BLOTER_IN_RESTRINGIDO, BLOTER_IN_CARTERA_PROPIA, TPPEVA_ID, CTESEG_ID, BLOTER_STATUS, BLOTER_IN_RED, ID_AGRUPACION FROM INFI_TB_102_BLOTER WHERE initcap(BLOTER_DESCRIPCION) like initcap('%").append(descripcion).append("%') ORDER BY BLOTER_ID");
		dataSet = db.get(dataSource, sb.toString());
		
				
	}
	
	/**
	 * Busca los bloter en la base de datos por el tppeva_id, cteseg_id o bloter_status recibido.  
	 * @throws Exception 
	*/
	public void listarporfiltro(String bloter_descripcion, String restringido, String cartera_propia) throws Exception{

		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		sql.append("SELECT BLOTER_ID, BLOTER_DESCRIPCION, TO_CHAR(BLOTER_HORARIO_DESDE,'DD/MM/YYYY HH24:MI:SS') AS BLOTER_HORARIO_DESDE, TO_CHAR(BLOTER_HORARIO_HASTA,'DD/MM/YYYY HH24:MI:SS') AS BLOTER_HORARIO_HASTA, TO_CHAR(BLOTER_HORARIO_DESDE,'HH12:MI:SS AM.') AS HORA_DESDE, TO_CHAR(BLOTER_HORARIO_HASTA,'HH12:MI:SS AM.') AS HORA_HASTA, CASE WHEN BLOTER_IN_RESTRINGIDO=").append(ConstantesGenerales.VERDADERO).append(" THEN 'Si' WHEN BLOTER_IN_RESTRINGIDO=").append(ConstantesGenerales.FALSO).append(" THEN 'No' END BLOTER_IN_RESTRINGIDO, CASE WHEN BLOTER_IN_CARTERA_PROPIA=").append(ConstantesGenerales.VERDADERO).append(" THEN 'Si' WHEN BLOTER_IN_CARTERA_PROPIA=").append(ConstantesGenerales.FALSO).append(" THEN 'No' END BLOTER_IN_CARTERA_PROPIA, TPPEVA_ID, CTESEG_ID, BLOTER_STATUS, BLOTER_IN_RED, CASE WHEN BLOTER_IN_RED=1 THEN 'Si' WHEN BLOTER_IN_RED=0 THEN 'No' END BLOTER_RED, ID_AGRUPACION,C.NOMBRE_CANAL "); 
		sql.append(" FROM INFI_TB_102_BLOTER B, INFI_TB_904_CANAL C");
		sql.append(" WHERE B.ID_CANAL=C.CANAL_ID ");
		
		if((bloter_descripcion!=null)){
			filtro.append(" AND UPPER(BLOTER_DESCRIPCION) LIKE UPPER('%").append(bloter_descripcion).append("%')");			
		}
		if((restringido!=null)){
			filtro.append(" AND BLOTER_IN_RESTRINGIDO=").append(restringido);			
		}
		if((cartera_propia!=null)){
			filtro.append(" AND BLOTER_IN_CARTERA_PROPIA=").append(cartera_propia);			
		}
		sql.append(filtro);
		sql.append(" ORDER BY BLOTER_DESCRIPCION");
		System.out.println("listarporfiltro: "+sql.toString());
		dataSet = db.get(dataSource, sql.toString());

				
	}	
	
	public String insertar(BloterDefinicion bloterDefinicion) throws Exception  {
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
				
		sql.append("INSERT INTO INFI_TB_102_BLOTER ( BLOTER_ID, BLOTER_DESCRIPCION, BLOTER_HORARIO_DESDE, BLOTER_HORARIO_HASTA, BLOTER_IN_RESTRINGIDO, BLOTER_IN_CARTERA_PROPIA, TPPEVA_ID, CTESEG_ID, BLOTER_STATUS, BLOTER_IN_RED, ID_AGRUPACION,ID_CANAL) VALUES (");
		String bloter_id = dbGetSequence(dataSource, ConstantesGenerales.SECUENCIA_BLOTER);
				
		filtro.append("'").append(bloter_id).append("',");
		filtro.append("'").append(bloterDefinicion.getBloter_descripcion().toUpperCase()).append("',");
		filtro.append("TO_DATE('").append(bloterDefinicion.getBloter_horario_desde()).append("','DD-MM-YYYY HH12:MI:SS AM'),");
		filtro.append("TO_DATE('").append(bloterDefinicion.getBloter_horario_hasta()).append("','DD-MM-YYYY HH12:MI:SS AM'),");
		filtro.append("'").append(bloterDefinicion.getBloter_in_restringido()).append("',");
		filtro.append("'").append(bloterDefinicion.getBloter_in_cartera_propia()).append("',");
		filtro.append("'").append(bloterDefinicion.getTppeva_id()).append("',");
		filtro.append("'").append(bloterDefinicion.getCteseg_id()).append("',");
		filtro.append("'").append(bloterDefinicion.getBloter_status()).append("',");
		filtro.append("'").append(bloterDefinicion.getBloter_in_red()).append("',");	
		filtro.append("'").append(bloterDefinicion.getId_agrupacion()).append("',");
		filtro.append("'").append(bloterDefinicion.getId_canal()).append("'");
		filtro.append(")");
		sql.append(filtro);		
		return(sql.toString());
		
	}

	public String modificar(BloterDefinicion bloterDefinicion) throws Exception {
		
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
	    	
		
		sql.append("UPDATE INFI_TB_102_BLOTER SET ");
		filtro.append(" BLOTER_DESCRIPCION= UPPER('").append(bloterDefinicion.getBloter_descripcion()).append("'),");
		filtro.append(" BLOTER_HORARIO_DESDE=").append("TO_DATE('").append(bloterDefinicion.getBloter_horario_desde()).append("','DD-MM-YYYY HH12:MI:SS AM'),");
		filtro.append(" BLOTER_HORARIO_HASTA=").append("TO_DATE('").append(bloterDefinicion.getBloter_horario_hasta()).append("','DD-MM-YYYY HH12:MI:SS AM'),");
		filtro.append(" BLOTER_IN_RESTRINGIDO='").append(bloterDefinicion.getBloter_in_restringido()).append("',");
		filtro.append(" BLOTER_IN_CARTERA_PROPIA='").append(bloterDefinicion.getBloter_in_cartera_propia()).append("',");
		filtro.append(" TPPEVA_ID='").append(bloterDefinicion.getTppeva_id()).append("',");
		filtro.append(" CTESEG_ID='").append(bloterDefinicion.getCteseg_id()).append("',");
		filtro.append(" BLOTER_STATUS='").append(bloterDefinicion.getBloter_status()).append("',");
		filtro.append(" BLOTER_IN_RED='").append(bloterDefinicion.getBloter_in_red()).append("',");						
		filtro.append(" ID_AGRUPACION='").append(bloterDefinicion.getId_agrupacion()).append("',");
		filtro.append(" ID_CANAL='").append(bloterDefinicion.getId_canal()).append("'");
		filtro.append(" WHERE BLOTER_ID ='").append(bloterDefinicion.getBloter_id()).append("'");
		sql.append(filtro);			
		return(sql.toString());				

	}


	public String eliminar(BloterDefinicion bloterDefinicion) throws Exception {
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		
		sql.append("DELETE FROM INFI_TB_102_BLOTER WHERE");
		
		filtro.append(" bloter_id='").append(bloterDefinicion.getBloter_id()).append("'");
		sql.append(filtro);			
		return(sql.toString());
	}
	
	public void verificar(BloterDefinicion bloterDefinicion) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select bloter_id from INFI_TB_107_UI_BLOTTER where");
		sql.append(" bloter_id='").append(bloterDefinicion.getBloter_id()).append("'");

		dataSet =db.get(dataSource,sql.toString());
	}
	
	/**Lista el id y el nombre de la agrupacion*/
	public void listaAgrupacion() throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select id_agrupacion, nom_agrupacion from INFI_TB_806_AGRUPACION");
		
		dataSet =db.get(dataSource,sql.toString());
	}


	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**Lista los blotter y los tipos de persona configurados u asociados a una unidad de inversion especifica
	 * @param
	 * filtro
	 * **/
	public void listarBlotterTipoPersona(String idUnidadInversion)throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select b.bloter_descripcion, tipper_nombre, br.bloter_id, br.tipper_id from INFI_TB_111_UI_BLOTTER_RANGOS br, INFI_TB_102_BLOTER b, INFI_TB_200_TIPO_PERSONAS p where undinv_id=");
		sql.append(idUnidadInversion);
		sql.append(" and b.bloter_id=br.bloter_id and p.tipper_id=br.tipper_id");
		dataSet = db.get(dataSource, sql.toString());
	}
	
	/**Lista los blotter y los tipos de persona configurados u asociados a una unidad de inversion especifica
	 * Modificado por NM25287. Inclusión del tipo de producto SUBASTA_DIVISA. SICAD
	 * Modificado por NM25287. Inclusión de tipos de producto SICAD2PER y SICAD2RED 20/03/2014
	 * filtro
	 * **/
	public void listarBlotters(String idUnidadInversion)throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select ub.*, b.*, case when  (select count(uidoc_unico) from INFI_TB_115_UI_DOC uo where ub.undinv_id=uo.undinv_id and uo.bloter_id=ub.bloter_id)=").append(ConstantesGenerales.FALSO).append(" then 'Sin Asociar ningún Documento' when (select count(uidoc_unico) from INFI_TB_115_UI_DOC uo where ub.undinv_id=uo.undinv_id and uo.bloter_id=ub.bloter_id)<(select case when ((insfin_forma_orden='").append(ConstantesGenerales.INST_TIPO_SUBASTA);
		sql.append("' OR insfin_forma_orden='").append(ConstantesGenerales.INST_TIPO_SUBASTA_COMPETITIVA).append("' OR insfin_forma_orden='").append(ConstantesGenerales.INST_FINANCIERO_SIMADI_TAQUILLA).append("') AND TIPO_PRODUCTO_ID in ( '").append(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA).append("','").append(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA).append("','").append(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL).append("','").append(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL).append("','").append(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL).append("')) then '7' when ((insfin_forma_orden='").append(ConstantesGenerales.INST_TIPO_SUBASTA).append("' OR insfin_forma_orden='").append(ConstantesGenerales.INST_TIPO_SITME);
		sql.append("' OR insfin_forma_orden='").append(ConstantesGenerales.INST_TIPO_SUBASTA_COMPETITIVA).append("' OR insfin_forma_orden='").append(ConstantesGenerales.INST_FINANCIERO_SIMADI_TAQUILLA).append("') AND TIPO_PRODUCTO_ID = '").append(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME).append("') then '8' when insfin_forma_orden='").append(ConstantesGenerales.INST_TIPO_INVENTARIO).append("' OR insfin_forma_orden='").append(ConstantesGenerales.INST_TIPO_INVENTARIO_CON_PRECIO).append("' then '2' end valor from INFI_TB_101_INST_FINANCIEROS fi, INFI_TB_106_UNIDAD_INVERSION u where u.insfin_id=fi.insfin_id and u.undinv_id=ub.undinv_id)*(select count(distinct(p.tipper_id)) from INFI_TB_200_TIPO_PERSONAS p, INFI_TB_111_UI_BLOTTER_RANGOS br where p.tipper_id=br.tipper_id and ub.undinv_id=br.undinv_id and br.bloter_id=b.bloter_id and br.bloter_id=ub.bloter_id) then 'Falta Documentos por Asociar' when (select count(uidoc_unico) from INFI_TB_115_UI_DOC uo where ub.undinv_id=uo.undinv_id and uo.bloter_id=ub.bloter_id)=(select case when ((insfin_forma_orden='").append(ConstantesGenerales.INST_TIPO_SUBASTA);
		sql.append("' OR insfin_forma_orden='").append(ConstantesGenerales.INST_TIPO_SUBASTA_COMPETITIVA).append("' OR insfin_forma_orden='").append(ConstantesGenerales.INST_FINANCIERO_SIMADI_TAQUILLA).append("') AND TIPO_PRODUCTO_ID in ( '").append(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA).append("','").append(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA).append("','").append(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL).append("','").append(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL).append("','").append(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL).append("')) then '7' when ((insfin_forma_orden='").append(ConstantesGenerales.INST_TIPO_SUBASTA).append("' OR insfin_forma_orden='").append(ConstantesGenerales.INST_TIPO_SITME);
		sql.append("' OR insfin_forma_orden='").append(ConstantesGenerales.INST_TIPO_SUBASTA_COMPETITIVA).append("' OR insfin_forma_orden='").append(ConstantesGenerales.INST_FINANCIERO_SIMADI_TAQUILLA).append("') AND TIPO_PRODUCTO_ID = '").append(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME).append("') then '8' when insfin_forma_orden='").append(ConstantesGenerales.INST_TIPO_INVENTARIO).append("' OR insfin_forma_orden='").append(ConstantesGenerales.INST_TIPO_INVENTARIO_CON_PRECIO).append("' then '2' end valor from INFI_TB_101_INST_FINANCIEROS fi, INFI_TB_106_UNIDAD_INVERSION u where u.insfin_id=fi.insfin_id and u.undinv_id=ub.undinv_id)*(select count(distinct(p.tipper_id)) from INFI_TB_200_TIPO_PERSONAS p, INFI_TB_111_UI_BLOTTER_RANGOS br where p.tipper_id=br.tipper_id and ub.undinv_id=br.undinv_id and br.bloter_id=b.bloter_id and br.bloter_id=ub.bloter_id) then 'Asociados todos los Documentos' end estado from INFI_TB_107_UI_BLOTTER ub, INFI_TB_102_BLOTER b where ub.bloter_id=b.bloter_id and ub.undinv_id=");
		sql.append(idUnidadInversion);
		sql.append(" order by b.bloter_id");		
		System.out.println("listarBlotters: "+sql.toString());
		dataSet = db.get(dataSource, sql.toString());
	}
	
	/**Lista los blotter y los tipos de persona asociados a una unidad de inversion y que posean documentos asociados para ser modificados
	 * @param
	 * filtro
	 * **/
	public void listarBlottersDoc(String idUnidadInversion)throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct ub.*, b.* from INFI_TB_107_UI_BLOTTER ub, INFI_TB_102_BLOTER b, INFI_TB_115_UI_DOC uo where ub.bloter_id=b.bloter_id and uo.bloter_id=b.bloter_id and ub.undinv_id=");
		sql.append(idUnidadInversion);
		sql.append(" order by b.bloter_id");
		dataSet = db.get(dataSource, sql.toString());
	}

	/***
	 * metodo para listar los usuario y blotter  asociados entre si
	 * @param
	 * filtro
	 * **/
	public void listarUsuariosBlotter(String filtro)throws Exception {
		// TODO Auto-generated method stub
		String sql=" select * from infi_tb_104_bloter_usuarios i104 "+
				   " left join infi_tb_102_bloter i102 on i104.BLOTER_ID=i102.bloter_id "+
				   " left join msc_user muser on i104.USERID=muser.userid "+ 
				   " where "+filtro;
		dataSet=db.get(dataSource,sql);
	}

	/**
	 * metodo para actulizar un usuario y un bloter asociado
	 * @param
	 * idUser, idBlotter, filtro
	 * **/
	public void actualizarUsuariosBlotter(String idUser,String idBloter,String filtro)throws Exception{
		// TODO Auto-generated method stub
		String sql="update infi_tb_104_bloter_usuarios set BLOTER_ID='"+idBloter+"', "+
					" USERID='"+idUser+"' "+
					" where "+filtro;
		db.exec(dataSource,sql);
	}

	/**
	 * metod para eliminar un usuario a un blotter
	 * @param
	 * String filtro
	 * **/
	public void eliminarUsuarioBlotter(String filtro)throws Exception{
		// TODO Auto-generated method stub
		String sql=" delete from infi_tb_104_bloter_usuarios where "+filtro;
		db.exec(dataSource,sql);
	}
/**
 * Lista las ordenes asociados a una unidad de inversion y bloter en caso de ser enviado
 * @param long unidadInversionId
 * @param String blotterId
 * @throws Exception
 */
	public void listarBlotterUnidadInversion(long unidadInversionId,String blotterId)throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select unique(a.ordene_id),(select sum(monto_operacion) from infi_tb_207_ordenes_operacion where infi_tb_207_ordenes_operacion.ORDENE_ID=a.ordene_id and infi_tb_207_ordenes_operacion.IN_COMISION=").append(ConstantesGenerales.VERDADERO).append(" and infi_tb_207_ordenes_operacion.STATUS_OPERACION='").append(ConstantesGenerales.STATUS_APLICADA).append("' and infi_tb_207_ordenes_operacion.TRNF_TIPO='").append(TransaccionFinanciera.DEBITO).append("')as monto_comision,(select sum(monto_operacion) from infi_tb_207_ordenes_operacion where infi_tb_207_ordenes_operacion.ORDENE_ID=a.ordene_id and infi_tb_207_ordenes_operacion.IN_COMISION<>").append(ConstantesGenerales.VERDADERO).append(" and infi_tb_207_ordenes_operacion.STATUS_OPERACION='").append(ConstantesGenerales.STATUS_APLICADA).append("' and infi_tb_207_ordenes_operacion.TRNF_TIPO='").append(TransaccionFinanciera.DEBITO).append("')as capital from infi_tb_204_ordenes a left join infi_tb_207_ordenes_operacion on a.ORDENE_ID=infi_tb_207_ordenes_operacion.ordene_id where a.bloter_id is not null and a.UNIINV_ID=");
		sql.append(unidadInversionId);
		if(blotterId!=null)
		sql.append(" and a.bloter_id='").append(blotterId).append("'");
		sql.append(" and infi_tb_207_ordenes_operacion.STATUS_OPERACION='");
		sql.append(com.bdv.infi.logic.interfaces.ConstantesGenerales.STATUS_APLICADA);
		sql.append("'  and infi_tb_207_ordenes_operacion.TRNF_TIPO='");
		sql.append("DEB");
		sql.append("'");
		dataSet=db.get(dataSource,sql.toString());
	}
	
	
	/**
	 * metodo  para solo tener asociado a  un bloter en la red
	 */
	public boolean listarBloterRed( String idBlotter)throws Exception{
		StringBuffer sql= new StringBuffer();
		boolean existe   = false;
		sql.append("select * from infi_tb_102_bloter where bloter_in_red=");
		sql.append(ConstantesGenerales.VERDADERO);
		sql.append(" and bloter_id!='").append(idBlotter).append("'");
		dataSet=db.get(dataSource,sql.toString());
		if(dataSet.count()>0){
			existe=true;
		}
		return existe;
		
	}
	
	
	/**
	 * Retorna el query para actualizar todos los registros a red NO
	 * @return
	 * @throws Exception
	 */
	public String  updateRed()throws Exception{
		// TODO Auto-generated method stub
		String sql=" update INFI_TB_102_BLOTER set BLOTER_IN_RED=0";
		return sql;
	}
	
	/**
	 * Lista datos del blotter filtrados por Unidad de Inversion
	 * @return
	 * @throws Exception
	 */
	public void listarBlotterPorUI(String unidadInversion)throws Exception{	
		String sql="SELECT BLOTER_ID,UIBLOT_IN_DISPONIBLE FROM INFI_TB_107_UI_BLOTTER WHERE UNDINV_ID = '"+unidadInversion+"'";
		dataSet=db.get(dataSource,sql);
	}
	
	/**
	 * Consulta cuantos blotters estan asociados a un canal
	 * @author nm25287
	 * @return
	 * @throws Exception
	 */
	public int cantidadBlottersPorCanal(String idCanal,String notBlotterId)throws Exception{	
		StringBuffer sb = new StringBuffer();
		int cantidad=0;
		sb.append("SELECT COUNT(*) CANT FROM INFI_TB_102_BLOTER WHERE ID_CANAL="+idCanal);
		
		if(notBlotterId!=null){
			sb.append(" AND BLOTER_ID<>").append(notBlotterId);
		}
		
		dataSet=db.get(dataSource,sb.toString());	
		//System.out.println(sql);
		if(dataSet.count()>0){
			dataSet.next();
			cantidad=Integer.parseInt(dataSet.getValue("CANT"));			
		}
		return cantidad;
	}
	
	/**
	 * Lista datos del blotter filtrados por Unidad de Inversion
	 * @return
	 * @throws Exception
	 */
	//TTS_491 NM26659:19/02/2015
	public void listarBlotterCanalPorUiId(String unidadInversion)throws Exception{	
		String sql="SELECT b.ID_CANAL, b.bloter_id, uiblot_in_disponible FROM infi_tb_107_ui_blotter uib, infi_tb_102_bloter b WHERE uib.bloter_id = b.bloter_id AND  uib.undinv_id = "+unidadInversion;
		dataSet=db.get(dataSource,sql);
	}
	
	/**
	 * Obtiene la cantidad de blotter rangos configurados para un tipo de operacion (efectivo o cuenta en dolares)
	 * @param unidadInversion: id de unidad de inversión
	 * @param tipoOp: tipo de operacion electronico (1) o efectivo (2)
	 * @return
	 * @throws Exception
	 */
	//TTS-504-SIMADI Efectivo Taquilla NM25287 31/08/2015
	public int listarBlotterUiTipoOperacion(String unidadInversion, int tipoOp)throws Exception{	
		int cantidad=0;
		String sql="SELECT COUNT(*) CANT FROM INFI_TB_111_UI_BLOTTER_RANGOS BR WHERE BR.UNDINV_ID="+unidadInversion + " AND BR.UIBLOT_TIPO="+tipoOp;
		
		dataSet=db.get(dataSource,sql);
		if(dataSet.count()>0){
			dataSet.next();
			cantidad=Integer.parseInt(dataSet.getValue("CANT"));			
		}
		return cantidad;
	}
}   

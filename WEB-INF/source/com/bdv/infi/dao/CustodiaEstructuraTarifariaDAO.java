package com.bdv.infi.dao;

import java.util.ArrayList;
import javax.sql.DataSource;
import megasoft.Logger;
import megasoft.db;
import com.bdv.infi.data.CustodiaComision;
import com.bdv.infi.data.CustodiaComisionCliente;
import com.bdv.infi.data.CustodiaComisionDepositario;
import com.bdv.infi.data.CustodiaComisionTitulo;
import com.bdv.infi.data.CustodiaComisionTransaccion;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.util.Utilitario;
import com.bdv.infi.data.CustodiaEstructuraTarifaria;

public class CustodiaEstructuraTarifariaDAO extends com.bdv.infi.dao.GenericoDAO {
	
	
	public CustodiaEstructuraTarifariaDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
	}
	
	public CustodiaEstructuraTarifariaDAO(DataSource ds) throws Exception {
		super(ds);
	}
	
	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**Busca los campos dinamicos exitentes y retorna un dataset
	 * @param campos todos los campos dinamicos asociados a una unidad de inversion*/
	public void listarComisionesExistentes(String comision, String cliente) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("select distinct(cu.comision_id), cu.comision_nombre, cu.usuario_id, cu.fecha_ult_act, cu.comision_in_general, case when cu.comision_in_general=").append(ConstantesGenerales.VERDADERO).append(" then 'SI' when cu.comision_in_general=").append(ConstantesGenerales.FALSO).append(" then 'NO' end indicador, m.userid, tr.*, (select count(*) from INFI_TB_043_COM_CLIENTE cc where cc.comision_id=cu.comision_id) as clientes from MSC_USER m, INFI_TB_040_COM_CUSTODIA cu left join INFI_TB_041_COM_TRANSACCION tr on cu.comision_id=tr.comision_id left join INFI_TB_043_COM_CLIENTE on cu.comision_id=INFI_TB_043_COM_CLIENTE.comision_id	where m.msc_user_id=cu.usuario_id ");
		//left join INFI_TB_043_COM_CLIENTE cl on cu.comision_id=cl.comision_id
		if(comision!=null){
			sb.append(" and upper(cu.comision_nombre) like upper('%").append(comision).append("%')");
		}
		if(cliente!=null){
			sb.append(" and INFI_TB_043_COM_CLIENTE.client_id=").append(cliente);
		}
		sb.append(" order by cu.comision_nombre");

		dataSet = db.get(dataSource, sb.toString());
		System.out.println("listarComisionesExistentes "+ sb.toString());
	}
	
	/**Busca los campos dinamicos exitentes y retorna un dataset
	 * @param campos todos los campos dinamicos asociados a una unidad de inversion*/
	public void listarComisionPorId(String idcomision) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("select * from INFI_TB_040_COM_CUSTODIA where comision_id=");
		sb.append(idcomision);
		dataSet = db.get(dataSource, sb.toString());
		
	}
	
	/**Busca los campos dinamicos exitentes y retorna un dataset
	 * @param campos todos los campos dinamicos asociados a una unidad de inversion*/
	public void listarDepositariosAsociados(String comision) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("select cd.*, e.empres_nombre, e.empres_rif, e.empres_email from INFI_TB_042_COM_DEPOSITARIO cd left join INFI_TB_016_EMPRESAS e on cd.empres_id=e.empres_id where cd.comision_id=");
		sb.append(comision);
		dataSet = db.get(dataSource, sb.toString());
		
	}
	
	/**Busca los campos dinamicos exitentes y retorna un dataset
	 * @param campos todos los campos dinamicos asociados a una unidad de inversion*/
	public void listarDepositario(CustodiaComisionDepositario custodiaComisionDepositario) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("select d.*, cu.comision_nombre from INFI_TB_042_COM_DEPOSITARIO d, INFI_TB_040_COM_CUSTODIA cu where cu.comision_id=d.comision_id");
		sb.append(" AND cu.comision_id=").append(custodiaComisionDepositario.getIdComision());
		sb.append(" and d.empres_id='").append(custodiaComisionDepositario.getIdEmpresa()).append("'");
		dataSet = db.get(dataSource, sb.toString());
		
	}
	
	/**Busca los campos dinamicos exitentes y retorna un dataset
	 * @param campos todos los campos dinamicos asociados a una unidad de inversion*/
	public void listarTitulo(CustodiaComisionTitulo custodiaComisionTitulo) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("select t.*, cu.comision_nombre from INFI_TB_044_COM_TITULO t, INFI_TB_040_COM_CUSTODIA cu where cu.comision_id=t.comision_id");
		sb.append(" AND cu.comision_id=").append(custodiaComisionTitulo.getIdComision());
		sb.append(" and t.titulo_id='").append(custodiaComisionTitulo.getIdTitulo()).append("'");
		dataSet = db.get(dataSource, sb.toString());
		
	}
	
	/**Busca los campos dinamicos exitentes y retorna un dataset
	 * @param campos todos los campos dinamicos asociados a una unidad de inversion*/
	public void listarTodosDepositarios(String comision, String empresa, String rif, String tipoPersona) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("select * from INFI_TB_016_EMPRESAS where empres_id not in (select empres_id from INFI_TB_042_COM_DEPOSITARIO where comision_id=").append(comision).append(")");
		if (empresa!=null){
			sb.append(" and upper(empres_nombre) like upper('%").append(empresa).append("%')");
		}
		if (rif!=null&&tipoPersona==null){
			sb.append(" and upper(empres_rif) like upper('%").append(rif).append("%')");
		}
		if (rif!=null&&tipoPersona!=null){
			sb.append(" and upper(empres_rif) like upper('").append(tipoPersona).append("%").append(rif).append("%')");
		}
		if (rif==null&&tipoPersona!=null){
			sb.append(" and upper(empres_rif) like upper('").append(tipoPersona).append("%')");
		}
		dataSet = db.get(dataSource, sb.toString());
		
	}
	
	/**Busca los campos dinamicos exitentes y retorna un dataset
	 * @param campos todos los campos dinamicos asociados a una unidad de inversion*/
	public void listarClientes(String comision) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("select cc.*, c.client_nombre, c.client_cedrif, c.tipper_id from INFI_TB_043_COM_CLIENTE cc left join INFI_TB_201_CTES c on cc.client_id=c.client_id where cc.comision_id=");
		sb.append(comision);
		dataSet = db.get(dataSource, sb.toString());
		
	}
	
	/**Busca los clientes exitentes pero que njo esten en la tabla 043 y retorna un dataset
	 * @param campos todos los campos dinamicos asociados a una unidad de inversion*/
	public void listarTodosClientes(String comision, String cliente, String cedrif, String tipoPersona) throws Exception {
		StringBuffer sb = new StringBuffer();
		
		sb.append("select cu.comision_nombre, case when comision_id is not null then '").append(ConstantesGenerales.VERDADERO).append("' when comision_id is null then '").append(ConstantesGenerales.FALSO).append("' end html, ct.*from INFI_TB_043_COM_CLIENTE cl right join INFI_TB_201_CTES ct on cl.client_id=ct.client_id left join INFI_TB_040_COM_CUSTODIA cu on cl.comision_id=cu.comision_id where 1=1");
		if (cliente!=null){
			sb.append(" and upper(client_nombre) like upper('%").append(cliente).append("%')");
		}
		if (cedrif!=null){
			sb.append(" and upper(client_cedrif) like upper('%").append(cedrif).append("%')");
		}
		if (tipoPersona!=null){
			sb.append(" and tipper_id='").append(tipoPersona).append("'");
		}
		sb.append(" order by client_nombre asc");
		dataSet = db.get(dataSource, sb.toString());
		
	}
	
	/**Busca los clientes exitentes en la tabla 043 y retorna un dataset
	 * @param campos todos los campos dinamicos asociados a una unidad de inversion*/
	public void listarClientesConfigurados(String comision) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("select comision_nombre, client_nombre, tipper_id, client_cedrif from INFI_TB_043_COM_CLIENTE cl, INFI_TB_040_COM_CUSTODIA cu, INFI_TB_201_CTES ct where cl.comision_id=cu.comision_id and cl.client_id=ct.client_id and cl.comision_id!=").append(comision);
		sb.append(comision);
		dataSet = db.get(dataSource, sb.toString());
		
	}
	
	/**Busca los campos dinamicos exitentes y retorna un dataset
	 * @param campos todos los campos dinamicos asociados a una unidad de inversion*/
	public void listarTodosTitulos(String comision, String titulo) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("select * from INFI_TB_100_TITULOS where titulo_id not in (select titulo_id from INFI_TB_044_COM_TITULO where comision_id=").append(comision).append(") and titulo_id is not null");
		if (titulo!=null){
			sb.append(" and upper(titulo_descripcion) like upper('%").append(titulo).append("%')");
		}
		dataSet = db.get(dataSource, sb.toString());
		
	}

	/**Busca los campos dinamicos exitentes y retorna un dataset
	 * @param campos todos los campos dinamicos asociados a una unidad de inversion*/
	public void listarTitulos(String comision) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("select ct.comision_id, ct.titulo_id, case when ct.pct_comision is not null then ct.pct_comision||'%' end pct_comision, ct.mto_comision, ct.moneda_comision, t.titulo_descripcion, t.titulo_fe_emision, t.titulo_fe_vencimiento from INFI_TB_044_COM_TITULO ct left join INFI_TB_100_TITULOS t on trim(ct.titulo_id)=trim(t.titulo_id) where ct.comision_id=");
		sb.append(comision);
		dataSet = db.get(dataSource, sb.toString());
		
	}
	
	/**Busca los campos dinamicos exitentes y retorna un dataset
	 * @param campos todos los campos dinamicos asociados a una unidad de inversion*/
	public void listarTransacciones(String comision) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("select * from INFI_TB_041_COM_TRANSACCION where comision_id=");
		sb.append(comision);
		dataSet = db.get(dataSource, sb.toString());
		
	}
	
	/**
	 * Modifica el registro de una tabla 
	*/
	public String[] modificarComision(CustodiaComision custodiaComision){
		ArrayList<String> consultas = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append("update INFI_TB_040_COM_CUSTODIA set ");
		sql.append("comision_nombre='").append(custodiaComision.getComisionNombre()).append("', ");
		sql.append("usuario_id=").append(custodiaComision.getIdUsuario()).append(", ");
		sql.append("fecha_ult_act=").append(formatearFechaBDActual()).append(", ");
		sql.append("comision_in_general=").append(custodiaComision.getComisionInGeneral());
		sql.append(" where comision_id=").append(custodiaComision.getIdComision());
		
		consultas.add(sql.toString());
		
		if(custodiaComision.getComisionInGeneral()==ConstantesGenerales.FALSO){
			eliminarTitulos(custodiaComision, consultas);
			eliminarDepositariosCentrales(custodiaComision, consultas);
		}else{
			eliminarClientes(custodiaComision, consultas);
		}		
		
		//Prepara el retorno
		String[] retorno = new String[consultas.size()];
		
		//Recorre la lista y crea un string de consyltas
		for (int i=0; i < consultas.size(); i++){
			retorno[i] = consultas.get(i).toString();
		}
     	return retorno;	
		
	}
	
	/**
	 * Modifica el registro de una tabla 
	*/
	public String modificarDepositario(CustodiaComisionDepositario custodiaComisionDepositario){
		StringBuffer sql = new StringBuffer();
		sql.append("update INFI_TB_042_COM_DEPOSITARIO set ");
		sql.append("mto_comision=").append(custodiaComisionDepositario.getMontoComision()).append(", ");
		sql.append("moneda_comision='").append(custodiaComisionDepositario.getMonedaComision()).append("' ");
		sql.append("where comision_id=").append(custodiaComisionDepositario.getIdComision());
		sql.append(" and empres_id='").append(custodiaComisionDepositario.getIdEmpresa()).append("'");
		return(sql.toString());
	}
	
	/**
	 * Modifica el registro de una tabla 
	*/
	public String modificarTitulo(CustodiaComisionTitulo custodiaComisionTitulo){
		StringBuffer sql = new StringBuffer();
		sql.append("update INFI_TB_044_COM_TITULO set ");
		if(custodiaComisionTitulo.getPctComision()==Double.parseDouble(String.valueOf(ConstantesGenerales.FALSO))){
			sql.append("pct_comision=null, ");
		}else{
			sql.append("pct_comision=").append(custodiaComisionTitulo.getPctComision()).append(", ");
		}		
		if(custodiaComisionTitulo.getMontoComision()==Double.parseDouble(String.valueOf(ConstantesGenerales.FALSO))){
			sql.append("mto_comision=null, ");
		}else{
			sql.append("mto_comision=").append(custodiaComisionTitulo.getMontoComision()).append(", ");
		}		
		if(custodiaComisionTitulo.getMonedaComision().equals("")){
			sql.append("moneda_comision=null ");
		}else{
			sql.append("moneda_comision='").append(custodiaComisionTitulo.getMonedaComision()).append("' ");
		}
		sql.append("where comision_id=").append(custodiaComisionTitulo.getIdComision());
		sql.append(" and titulo_id='").append(custodiaComisionTitulo.getIdTitulo()).append("'");
		return(sql.toString());
	}
	
	/**
	 * Modifica el registro de una tabla 
	*/
	public String modificarTransaccion(CustodiaComisionTransaccion custodiaComisionTransaccion){
		StringBuffer sql = new StringBuffer();
		sql.append("update INFI_TB_041_COM_TRANSACCION set ");
		if(custodiaComisionTransaccion.getPctTransaccionInterna()==Double.parseDouble(String.valueOf(ConstantesGenerales.FALSO))){
			sql.append("pct_trans_interna=null, ");
		}else{
			sql.append("pct_trans_interna=").append(custodiaComisionTransaccion.getPctTransaccionInterna()).append(", ");
		}
		if(custodiaComisionTransaccion.getPctTransaccionExterna()==Double.parseDouble(String.valueOf(ConstantesGenerales.FALSO))){
			sql.append("pct_trans_externa=null, ");
		}else{
			sql.append("pct_trans_externa=").append(custodiaComisionTransaccion.getPctTransaccionExterna()).append(", ");
		}
		if(custodiaComisionTransaccion.getMontoTransaccionInterna()==Double.parseDouble(String.valueOf(ConstantesGenerales.FALSO))){
			sql.append("mto_trans_interna=null, ");
		}else{
			sql.append("mto_trans_interna=").append(custodiaComisionTransaccion.getMontoTransaccionInterna()).append(", ");
		}
		if(custodiaComisionTransaccion.getMonedaTransaccionInterna().equals("")){
			sql.append("moneda_trans_interna=null, ");
		}else{
			sql.append("moneda_trans_interna='").append(custodiaComisionTransaccion.getMonedaTransaccionInterna()).append("', ");
		}
		if(custodiaComisionTransaccion.getMontoTransaccionExterna()==Double.parseDouble(String.valueOf(ConstantesGenerales.FALSO))){
			sql.append("mto_trans_externa=null, ");
		}else{
			sql.append("mto_trans_externa=").append(custodiaComisionTransaccion.getMontoTransaccionExterna()).append(", ");
		}
		if(custodiaComisionTransaccion.getMonedaTransaccionExterna().equals("")){
			sql.append("moneda_trans_externa=null, ");
		}else{
			sql.append("moneda_trans_externa='").append(custodiaComisionTransaccion.getMonedaTransaccionExterna()).append("', ");
		}			
		if(custodiaComisionTransaccion.getPctAnualNacional()==Double.parseDouble(String.valueOf(ConstantesGenerales.FALSO))){
			sql.append("pct_anual_nacional=null, ");
		}else{
			sql.append("pct_anual_nacional=").append(custodiaComisionTransaccion.getPctAnualNacional()).append(", ");
		}
		if(custodiaComisionTransaccion.getPctAnualExtranjera()==Double.parseDouble(String.valueOf(ConstantesGenerales.FALSO))){
			sql.append("pct_anual_extranjera=null, ");
		}else{
			sql.append("pct_anual_extranjera=").append(custodiaComisionTransaccion.getPctAnualExtranjera()).append(", ");
		}
		if(custodiaComisionTransaccion.getMontoAnualNacional()==Double.parseDouble(String.valueOf(ConstantesGenerales.FALSO))){
			sql.append("mto_anual_nacional=null, ");
		}else{
			sql.append("mto_anual_nacional=").append(custodiaComisionTransaccion.getMontoAnualNacional()).append(", ");
		}
		if(custodiaComisionTransaccion.getMonedaAnualNacional().equals("")){
			sql.append("moneda_anual_nacional=null, ");
		}else{
			sql.append("moneda_anual_nacional='").append(custodiaComisionTransaccion.getMonedaAnualNacional()).append("', ");
		}
		if(custodiaComisionTransaccion.getMontoAnualExtranjera()==Double.parseDouble(String.valueOf(ConstantesGenerales.FALSO))){
			sql.append("mto_anual_extranjera=null, ");
		}else{
			sql.append("mto_anual_extranjera=").append(custodiaComisionTransaccion.getMontoAnualExtranjera()).append(", ");
		}
		if(custodiaComisionTransaccion.getMonedaAnualExtranjera().equals("")){
			sql.append("moneda_anual_extranjera=null ");
		}else{
			sql.append("moneda_anual_extranjera='").append(custodiaComisionTransaccion.getMonedaAnualExtranjera()).append("' ");
		}
		sql.append("where comision_id=").append(custodiaComisionTransaccion.getIdComision());
		
		return(sql.toString());
	}

	/**
	 * Modifica el registro de una tabla 
	*/
	public String insertarTransaccion(CustodiaComisionTransaccion custodiaComisionTransaccion){
		StringBuffer sql = new StringBuffer();
		sql.append("insert into INFI_TB_041_COM_TRANSACCION (comision_id,pct_trans_interna,pct_trans_externa,mto_trans_interna,moneda_trans_interna,mto_trans_externa,moneda_trans_externa,pct_anual_nacional,pct_anual_extranjera,mto_anual_nacional,moneda_anual_nacional,mto_anual_extranjera,moneda_anual_extranjera) values ( ");

		sql.append(custodiaComisionTransaccion.getIdComision()).append(",");
		if(custodiaComisionTransaccion.getPctTransaccionInterna()==Double.parseDouble(String.valueOf(ConstantesGenerales.FALSO))){
			sql.append("null, ");
		}else{
			sql.append(custodiaComisionTransaccion.getPctTransaccionInterna()).append(", ");
		}
		if(custodiaComisionTransaccion.getPctTransaccionExterna()==Double.parseDouble(String.valueOf(ConstantesGenerales.FALSO))){
			sql.append("null, ");
		}else{
			sql.append(custodiaComisionTransaccion.getPctTransaccionExterna()).append(", ");
		}
		if(custodiaComisionTransaccion.getMontoTransaccionInterna()==Double.parseDouble(String.valueOf(ConstantesGenerales.FALSO))){
			sql.append("null, ");
		}else{
			sql.append(custodiaComisionTransaccion.getMontoTransaccionInterna()).append(", ");
		}
		if(custodiaComisionTransaccion.getMonedaTransaccionInterna().equals("")){
			sql.append("null, ");
		}else{
			sql.append("'").append(custodiaComisionTransaccion.getMonedaTransaccionInterna()).append("', ");
		}
		if(custodiaComisionTransaccion.getMontoTransaccionExterna()==Double.parseDouble(String.valueOf(ConstantesGenerales.FALSO))){
			sql.append("null, ");
		}else{
			sql.append(custodiaComisionTransaccion.getMontoTransaccionExterna()).append(", ");
		}
		if(custodiaComisionTransaccion.getMonedaTransaccionExterna().equals("")){
			sql.append("null, ");
		}else{
			sql.append("'").append(custodiaComisionTransaccion.getMonedaTransaccionExterna()).append("', ");
		}
		if(custodiaComisionTransaccion.getPctAnualNacional()==Double.parseDouble(String.valueOf(ConstantesGenerales.FALSO))){
			sql.append("null, ");
		}else{
			sql.append(custodiaComisionTransaccion.getPctAnualNacional()).append(", ");
		}
		if(custodiaComisionTransaccion.getPctAnualExtranjera()==Double.parseDouble(String.valueOf(ConstantesGenerales.FALSO))){
			sql.append("null, ");
		}else{
			sql.append(custodiaComisionTransaccion.getPctAnualExtranjera()).append(", ");
		}
		if(custodiaComisionTransaccion.getMontoAnualNacional()==Double.parseDouble(String.valueOf(ConstantesGenerales.FALSO))){
			sql.append("null, ");
		}else{
			sql.append(custodiaComisionTransaccion.getMontoAnualNacional()).append(", ");
		}		
		if(custodiaComisionTransaccion.getMonedaAnualNacional().equals("")){
			sql.append("null, ");
		}else{
			sql.append("'").append(custodiaComisionTransaccion.getMonedaAnualNacional()).append("', ");
		}
		if(custodiaComisionTransaccion.getMontoAnualExtranjera()==Double.parseDouble(String.valueOf(ConstantesGenerales.FALSO))){
			sql.append("null, ");
		}else{
			sql.append(custodiaComisionTransaccion.getMontoAnualExtranjera()).append(", ");
		}
		if(custodiaComisionTransaccion.getMonedaAnualExtranjera().equals("")){
			sql.append("null )");
		}else{
			sql.append("'").append(custodiaComisionTransaccion.getMonedaAnualExtranjera()).append("') ");
		}
		
		return(sql.toString());
	}
	
	/**
	 * Inserta el registro en la tabla  
	 */
	public String insertarComision(CustodiaComision custodiaComision) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("insert into INFI_TB_040_COM_CUSTODIA (comision_id, comision_nombre, usuario_id, fecha_ult_act, comision_in_general)values (");
		custodiaComision.setIdComision(Integer.parseInt(dbGetSequence(dataSource, ConstantesGenerales.SECUENCIA_COM_CUSTODIA)));
		sql.append(custodiaComision.getIdComision()).append(",");
		sql.append("'").append(custodiaComision.getComisionNombre()).append("',");
		sql.append(custodiaComision.getIdUsuario()).append(",");
		sql.append(formatearFechaBDActual()).append(",");
		sql.append(custodiaComision.getComisionInGeneral()).append(")");
		return sql.toString();	
		
	}
	
	/**
	 * Inserta el registro en la tabla  
	 */
	public String insertarDepositarioCentral(CustodiaComisionDepositario custodiaComisionDepositario) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("insert into INFI_TB_042_COM_DEPOSITARIO (comision_id, empres_id, mto_comision, moneda_comision)values (");
		sql.append(custodiaComisionDepositario.getIdComision()).append(",");
		sql.append("'").append(custodiaComisionDepositario.getIdEmpresa()).append("',");
		if(custodiaComisionDepositario.getMontoComision()==0){
			sql.append("null,");
		}
		if (custodiaComisionDepositario.getMonedaComision()==null){
			sql.append("null)");
		}
		return(sql.toString());
	}
	
	/**
	 * Inserta el registro en la tabla  
	 */
	public String insertarCliente(CustodiaComisionCliente custodiaComisionCliente) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("insert into INFI_TB_043_COM_CLIENTE (comision_id, client_id)values (");
		sql.append(custodiaComisionCliente.getIdComision()).append(",");
		sql.append(custodiaComisionCliente.getIdCliente()).append(")");
		
		return(sql.toString());
	}
	
	/**
	 * Inserta el registro en la tabla  
	 */
	public String insertarTitulo(CustodiaComisionTitulo custodiaComisionTitulo) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("insert into INFI_TB_044_COM_TITULO (comision_id, titulo_id,pct_comision,mto_comision,moneda_comision)values (");
		sql.append(custodiaComisionTitulo.getIdComision()).append(",");
		sql.append("'").append(custodiaComisionTitulo.getIdTitulo()).append("',");
		if(custodiaComisionTitulo.getPctComision()==Double.parseDouble(String.valueOf(ConstantesGenerales.FALSO))){
			sql.append("null,");
		}else{
			sql.append(custodiaComisionTitulo.getPctComision()).append(",");
		}
		if(custodiaComisionTitulo.getMontoComision()==Double.parseDouble(String.valueOf(ConstantesGenerales.FALSO))){
			sql.append("null,");
		}else{
			sql.append(custodiaComisionTitulo.getMontoComision()).append(",");
		}		
		sql.append(custodiaComisionTitulo.getMonedaComision()).append(")");
		
		return(sql.toString());
	}
	
	/**
	 * Elimina un unico depositario central asociado a la comision. 
	*/
	public String eliminarDepositarioCentral(CustodiaComisionDepositario custodiaComisionDepositario){
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		sql.append("delete from INFI_TB_042_COM_DEPOSITARIO where");
		sql.append(" comision_id=").append(custodiaComisionDepositario.getIdComision());
		sql.append(" and empres_id='").append(custodiaComisionDepositario.getIdEmpresa()).append("'");
		sql.append(filtro);			
		return(sql.toString());
	}
	
	/**
	 * Elimina todos los depositarios asociados a la comision. 
	*/
	public void eliminarDepositariosCentrales(CustodiaComision custodiaComision, ArrayList<String> consultas){
		StringBuffer sql = new StringBuffer();
		sql.append("delete from INFI_TB_042_COM_DEPOSITARIO where");
		sql.append(" comision_id=").append(custodiaComision.getIdComision());
		consultas.add(sql.toString());
	}
	
	/**
	 * Elimina un unico cliente asociado a la comision 
	*/
	public String eliminarCliente(CustodiaComisionCliente custodiaComisionCliente){
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		sql.append("delete from INFI_TB_043_COM_CLIENTE where");
		sql.append(" comision_id=").append(custodiaComisionCliente.getIdComision());
		sql.append(" and client_id=").append(custodiaComisionCliente.getIdCliente());
		sql.append(filtro);			
		return(sql.toString());
	}
	
	/**
	 * Elimina todos los clientes asociados a la comision. 
	*/
	public void eliminarClientes(CustodiaComision custodiaComision, ArrayList<String> consultas){
		StringBuffer sql = new StringBuffer();
		sql.append("delete from INFI_TB_043_COM_CLIENTE where");
		sql.append(" comision_id=").append(custodiaComision.getIdComision());
		consultas.add(sql.toString());
	}
	
	/**
	 * Elimina un unico itulo asociado a una comision. 
	*/
	public String eliminarTitulo(CustodiaComisionTitulo custodiaComisionTitulo){
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		sql.append("delete from INFI_TB_044_COM_TITULO where");
		sql.append(" comision_id=").append(custodiaComisionTitulo.getIdComision());
		sql.append(" and titulo_id='").append(custodiaComisionTitulo.getIdTitulo()).append("'");
		sql.append(filtro);			
		return(sql.toString());
	}

	/**
	 * Elimina todos los titulos asociados a una comision 
	*/
	public void eliminarTitulos(CustodiaComision custodiaComision, ArrayList<String> consultas){
		StringBuffer sql = new StringBuffer();
		sql.append("delete from INFI_TB_044_COM_TITULO where");
		sql.append(" comision_id=").append(custodiaComision.getIdComision());
		consultas.add(sql.toString());
	}
	
	/**
	 * Elimina el registro en la tabla. 
	*/
	public String[] eliminarComision(CustodiaComision custodiaComision){
		String [] query = new String [5];
		String filtro = " comision_id="+custodiaComision.getIdComision();
		query[0]="delete from INFI_TB_044_COM_TITULO where "+ filtro;
		query[1]="delete from INFI_TB_043_COM_CLIENTE where "+ filtro;
		query[2]="delete from INFI_TB_042_COM_DEPOSITARIO where "+ filtro;
		query[3]="delete from INFI_TB_041_COM_TRANSACCION where "+ filtro;
		query[4]="delete from INFI_TB_040_COM_CUSTODIA where "+ filtro;
		
		return(query);
	}
		
	/**Método que crea un objeto de custodiaComision y en el arma la estructura tarifaria general,
	 * aquella que no está asociada a ningún cliente
	 * @return CustodiaEstructuraTarifaria estructura tarifaria que no está asociada a ningún cliente
	 * Retorna null en caso de no encontrar ninguna estructura de comisión general	  
	 * @throws lanza una excepcion en caso de error*/	
	public CustodiaEstructuraTarifaria listarEstructura() throws Exception{
		StringBuffer sb = new StringBuffer();
		//Busca la tarifa que no esten asociada a ningún cliente
		sb.append("SELECT A.*,B.* FROM INFI_TB_040_COM_CUSTODIA A INNER JOIN INFI_TB_041_COM_TRANSACCION B");
		sb.append(" ON A.COMISION_ID = B.COMISION_ID WHERE A.COMISION_ID NOT IN ");
		sb.append(" (SELECT COMISION_ID FROM INFI_TB_043_COM_CLIENTE)");
		return obtenerEstructura(sb.toString());
	}
	
	/**Método que crea un objeto de custodiaComision y en el arma la estructura tarifaria que,
	 * tenga el cliente 
	 * @param idCliente id del cliente para buscar la estructura tarifaria asociada a él
	 * @return CustodiaEstructuraTarifaria estructura tarifaria asociada al cliente. 
	 * Retorna null en caso de no encontrar ninguna estructura de comisión asociada al cliente 
	 * @throws lanza una excepcion en caso de error*/
	public CustodiaEstructuraTarifaria listarEstructura(long idCliente) throws Exception{
		StringBuffer sb = new StringBuffer();
		//Busca la tarifa que no esten asociada a ningún cliente
		sb.append("SELECT A.*,B.* FROM INFI_TB_040_COM_CUSTODIA A INNER JOIN INFI_TB_041_COM_TRANSACCION B");
		sb.append(" ON A.COMISION_ID = B.COMISION_ID WHERE A.COMISION_ID IN");
		sb.append("(SELECT COMISION_ID FROM INFI_TB_043_COM_CLIENTE WHERE CLIENT_ID=").append(idCliente).append(")");		
		return obtenerEstructura(sb.toString());
	}
	
	/**Arma la estructur tarifaria de custodia en base al sql recibido
	 * @param sql estructura tarifaria que debe buscar*/
	private CustodiaEstructuraTarifaria obtenerEstructura(String sql) throws Exception{
		CustodiaEstructuraTarifaria estructuraComision = null;		
		StringBuffer sb = new StringBuffer();
		try {
			CustodiaComision datosComision = new CustodiaComision();
			CustodiaComisionTransaccion tarifas = new CustodiaComisionTransaccion(); 
			conn = dataSource.getConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql);
			
			if (resultSet.next()){
				estructuraComision = new CustodiaEstructuraTarifaria();				
				datosComision.setIdComision(resultSet.getInt("comision_id"));				
				datosComision.setComisionNombre(resultSet.getString("comision_nombre"));
				datosComision.setIdUsuario(resultSet.getInt("usuario_id"));
				datosComision.setFecha(resultSet.getDate("fecha_ult_act"));
				estructuraComision.setDatosComision(datosComision);
				
				//Establece las tarifas
				tarifas.setIdComision(resultSet.getInt("comision_id"));
				tarifas.setPctTransaccionInterna(resultSet.getDouble("PCT_TRANS_INTERNA"));
				tarifas.setPctTransaccionExterna(resultSet.getDouble("PCT_TRANS_EXTERNA"));
				tarifas.setMontoTransaccionInterna(resultSet.getDouble("MTO_TRANS_INTERNA"));
				tarifas.setMontoTransaccionExterna(resultSet.getDouble("MTO_TRANS_EXTERNA"));
				tarifas.setMonedaTransaccionInterna(resultSet.getString("MONEDA_TRANS_INTERNA"));
				tarifas.setMonedaTransaccionExterna(resultSet.getString("MONEDA_TRANS_EXTERNA"));
				
				tarifas.setPctAnualNacional(resultSet.getDouble("PCT_ANUAL_NACIONAL"));
				tarifas.setPctAnualExtranjera(resultSet.getDouble("PCT_ANUAL_EXTRANJERA"));
				tarifas.setMontoAnualNacional(resultSet.getDouble("MTO_ANUAL_NACIONAL"));
				tarifas.setMontoAnualExtranjera(resultSet.getDouble("MTO_ANUAL_EXTRANJERA"));
				
				tarifas.setMonedaAnualNacional(resultSet.getString("MONEDA_ANUAL_NACIONAL"));
				tarifas.setMonedaAnualExtranjera(resultSet.getString("MONEDA_ANUAL_EXTRANJERA"));
				estructuraComision.setTarifas(tarifas);
				
				//Busca los depositarios asociados a la comisión
				sb = new StringBuffer("SELECT * FROM INFI_TB_042_COM_DEPOSITARIO WHERE COMISION_ID=").append(datosComision.getIdComision());
				resultSet = statement.executeQuery(sb.toString());
				while (resultSet.next()){
					CustodiaComisionDepositario custodiaDepositario = new CustodiaComisionDepositario();
					custodiaDepositario.setIdComision(datosComision.getIdComision());
					custodiaDepositario.setIdEmpresa(resultSet.getString("empres_id"));
					custodiaDepositario.setMonedaComision(resultSet.getString("moneda_comision"));
					custodiaDepositario.setMontoComision(resultSet.getDouble("mto_comision"));
					estructuraComision.agregarDepositario(custodiaDepositario);
				}
			
				//Busca los títulos asociados a la comisión
				sb = new StringBuffer("SELECT * FROM INFI_TB_044_COM_TITULO WHERE COMISION_ID=").append(datosComision.getIdComision());
				resultSet = statement.executeQuery(sb.toString());
				while (resultSet.next()){
					Logger.info(this,"Encontrado comision para " + resultSet.getString("titulo_id") );					
					CustodiaComisionTitulo custodiaTitulo = new CustodiaComisionTitulo();
					custodiaTitulo.setIdComision(datosComision.getIdComision());
					custodiaTitulo.setIdTitulo(resultSet.getString("titulo_id"));
					custodiaTitulo.setMonedaComision(resultSet.getString("moneda_comision"));
					custodiaTitulo.setMontoComision(resultSet.getDouble("mto_comision"));
					custodiaTitulo.setPctComision(resultSet.getDouble("pct_comision"));
					estructuraComision.agregarTitulo(custodiaTitulo);					
				}
			} 
		} catch (Exception e) {
			Logger.error(this,"Error en la búsqueda de la estructura tarifaria " + e.getMessage()+ Utilitario.stackTraceException(e));
			throw new Exception("Error en la búsqueda de la estructura tarifaria");
		} finally{
			this.closeResources();
			conn.close();
		}
		
		return estructuraComision;		
	}
	
	/**Busca los campos dinamicos exitentes y retorna un dataset
	 * @param campos todos los campos dinamicos asociados a una unidad de inversion*/
	public boolean existeComisionGeneral(String comisionId) throws Exception {
		StringBuffer sb = new StringBuffer();
		boolean existe = false;
		sb.append("select * from INFI_TB_040_COM_CUSTODIA where comision_in_general=");
		sb.append(ConstantesGenerales.VERDADERO);
		dataSet = db.get(dataSource, sb.toString());
		if(dataSet.count()>0){
			if (comisionId!=null){
				dataSet.first();
				dataSet.next();
				String comision_id = dataSet.getValue("comision_id");
				if(!comision_id.equals(comisionId)){
					existe=true;
				}
			}else{
				existe=true;
			}
		}
		return existe;
	}
}

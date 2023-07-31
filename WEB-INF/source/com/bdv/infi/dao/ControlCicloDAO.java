package com.bdv.infi.dao;

import javax.sql.DataSource;

import megasoft.Logger;
import megasoft.db;

import com.bdv.infi.data.Ciclo;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

//NM29643 - INFI_TTS_423: Nueva Tabla para el control de ciclos
/** 
 * Clase destinada para el manejo los registros relacionados a la tabla INFI_TB_817_CONTROL_CICLOS 
 */
public class ControlCicloDAO extends com.bdv.infi.dao.GenericoDAO {

	public ControlCicloDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
	}
	
	public ControlCicloDAO(DataSource ds) throws Exception {
		super(ds);
	}
	
	public Object moveNext() throws Exception {
		return null;
	}
	
	/**Obtiene el valor de la secuencia de la tabla
	 * @param nombreSecuencia
	 * @throws Exception
	 */
	public long getSecuencia(String nombreSecuencia) throws Exception{
		long sq = 0;
		String sql = "select "+nombreSecuencia+".NEXTVAL as ciclo_id FROM dual";
		dataSet = db.get(dataSource, sql);
		if(dataSet!=null && dataSet.count()>0){
			dataSet.next();
			sq = Long.parseLong(dataSet.getValue("ciclo_id"));
		}
		return sq;
	}
	
	 /**Lista los ciclos del tipo especificado
	 * @param tipo Tipo de ciclo a consultar
	 * @throws Exception
	 */
	public void listarCicloPorTipo(String tipo) throws Exception{
		String sql = "select * from infi_tb_817_control_ciclos where tipo in (" + 
			tipo + ") and fecha_cierre is null";
		dataSet = db.get(dataSource, sql);
	}
	
	/**Lista los ciclos del tipo especificado
	 * @param tipo Tipo de ciclo a consultar
	 * @throws Exception
	 */
	public void listarCicloPorID(long cicloId) throws Exception{
		String sql = "select cc.*, TO_CHAR(cc.FECHA_INICIO,'"+ConstantesGenerales.FORMATO_FECHA_HORA_BD2+"') AS fecha_ini, TO_CHAR(cc.FECHA_CIERRE,'"+ConstantesGenerales.FORMATO_FECHA_HORA_BD2+"') AS fecha_fin from infi_tb_817_control_ciclos cc where ciclo_id="+cicloId;
		dataSet = db.get(dataSource, sql);
	}
	
	/**Lista los ciclos del tipo especificado
	 * @param tipo Tipo de ciclo a consultar
	 * @throws Exception
	 */
	public void listarCicloPorTipoStatus(String tipo, String status) throws Exception{
		String sql = "select * from infi_tb_817_control_ciclos where tipo in ('" + 
			tipo + "') and fecha_cierre is null";
		if(status!=null && !status.equals(""))
			sql += " and estatus='"+status+"'";
		dataSet = db.get(dataSource, sql);
	}
	
	/**Lista los ciclos del tipo especificado	 
	 * @param tipo Tipo de ciclo a consultar
	 * @throws Exception
	 */
	//Metodo creado para el control de procesos de adjudicacion NM26659
	public void listarCicloPorTipoStatus(String... tipo) throws Exception{
		StringBuffer sql =new StringBuffer();
		
		sql.append("select * from infi_tb_817_control_ciclos where fecha_cierre is null ");
		
		if(tipo.length>0 && tipo[0]!=null){
			sql.append(" AND TIPO IN (");
			int count=0;
		
			for (String element : tipo) {
				if(count>0){
					sql.append(",");
				}			
				sql.append("'"+element+"'");
				++count;
			}
			sql.append(") ");
		}
		//System.out.println("listarCicloPorTipoStatus Sobrecarga ----->  " + sql.toString());
		dataSet = db.get(dataSource, sql.toString());
	}
	/**
	 * Registra un ciclo
	 * @param archivo objeto que contiene la información principal a insertar
	 * @return una array de consultas que deben ejecutarse
	 * @throws Exception en caso de error
	 */
	public String insertarCiclo(Ciclo ciclo) throws Exception{	
		StringBuffer sql = new StringBuffer();
		
		sql.append("insert into infi_tb_817_control_ciclos");
		sql.append(" (CICLO_ID, FECHA_INICIO, FECHA_CIERRE, NOMBRE, EXTERNO, TIPO, USUARIO_REGISTRO, USUARIO_APROBACION, OBSERVACION, ESTATUS)");
		sql.append(" values (");
		sql.append(ciclo.getCicloId()).append(",");
		sql.append(formatearFechaHoraBDActual()).append(",");
		sql.append("NULL,");
		sql.append("'").append(ciclo.getNombre()).append("',");
		sql.append(ciclo.getExterno()).append(",");
		sql.append("'").append(ciclo.getTipo()).append("',");
		if(ciclo.getUsuarioRegistro()!=null)	sql.append("'").append(ciclo.getUsuarioRegistro()).append("',");
		else	sql.append("NULL,");
		sql.append("NULL,");
		if(ciclo.getObservacion()!=null){
			sql.append("'").append(ciclo.getObservacion()).append("',");
		}else{
			sql.append("NULL,");
		}
		sql.append("'").append(ciclo.getStatus()).append("'");
		sql.append(")");
		
		return sql.toString();
	}
	
	/**
	 * Actualiza el estatus de un ciclo
	 * @param cicloID Id del ciclo a actualizar
	 * @param status al que se modifica el ciclo
	 * @return consulta que debe ejecutarse
	 * @throws Exception en caso de error
	 */
	public boolean actualizarCiclo(long cicloID, String status) throws Exception{	
		StringBuffer sql = new StringBuffer();
		
		sql.append("UPDATE infi_tb_817_control_ciclos SET ESTATUS='").append(status).append("'");
		sql.append(" WHERE ciclo_id=").append(cicloID);
		try{
			db.exec(dataSource, sql.toString());
		}catch(Exception e){
			Logger.error(this, "Ocurrio un error al actualizar el estatus del ciclo de ID: "+cicloID);
			return false;
		}		
		//return sql.toString();
		return true;
	}
	
	/**
	 * Metodo de cierre de un ciclo abierto
	 * @param id del ciclo a cerrar
	 * @return String
	 * @throws Exception en caso de error
	 */
	public String cierreCicloAbierto(long idCiclo,String status, String... observacion) throws Exception{	
		StringBuffer sql = new StringBuffer();
		
		sql.append("UPDATE INFI_TB_817_CONTROL_CICLOS CC SET CC.FECHA_CIERRE=SYSDATE");
		if(status!=null && !status.equals("")){
		sql.append(",CC.ESTATUS='"+status+"'");
		}
		if(observacion.length>0 && observacion[0]!=null && !observacion[0].equals("")){
			if (observacion[0].length()>1000){
				observacion[0] = observacion[0].substring(0,999);
			}
			sql.append(",CC.OBSERVACION='").append(observacion[0]).append("'");
		}
		sql.append(" WHERE CC.FECHA_CIERRE IS NULL AND CC.CICLO_ID=").append(idCiclo);
		
		return sql.toString();
	}
	
}
/**
 * 
 */
package com.bdv.infi.dao;


import java.util.ArrayList;

import javax.sql.DataSource;

//import sun.org.mozilla.javascript.internal.ast.ForInLoop;

import megasoft.Logger;
import megasoft.db;

import com.bdv.infi.data.CuentaCliente;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TipoInstruccion;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

/**
 * @author jal
 *
 */
public class ProcesosDAO extends GenericoDAO {

	public ProcesosDAO(DataSource ds) {
		super(ds);
	}
	
	public ProcesosDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
	}

	/**
	 * Lista si existe alg�n registro que indique que la transacci�n se est� ejecutando
	 * @param idTransaccion id de la transacci�n a consultar
	 * @throws Exception lanza una excepci�n si hay un error.
	 */
	public void listarPorTransaccionActiva(String idTransaccion) throws Exception{
		System.out.println("llego consulta");
		StringBuffer sql=new StringBuffer();
		sql.append("select * from infi_tb_807_procesos where fecha_fin is null and transa_id='");
		sql.append(idTransaccion);
		sql.append("'");
		//System.out.println("----------------listarPRocesooo: "+sql.toString());
		System.out.println("llego consulta1");
		dataSet=db.get(dataSource,sql.toString());
		System.out.println("listarPorTransaccionActiva "+sql);
	}
	
	public void listarProcesoActivoMesActeriores(String mes) throws Exception{
		dataSet =db.get(dataSource,"select * from infi_tb_807_procesos where fecha_inicio<to_date('"+mes+"','MM')" 
									+"and fecha_fin is null");
	}

	//TTS-531 Modificacion para incluir fecha cierre al momento de crear el proceso (Aplica solo para los forces de cierre) NM26659_02/12/2016
	public String insertar(Proceso proceso,String... forceCierre) throws Exception{
		StringBuilder sql = new StringBuilder();
		if(proceso.getEjecucionId()==0)
			proceso.setEjecucionId(Integer.parseInt(this.dbGetSequence(this.dataSource,ConstantesGenerales.SECUENCIA_PROCESOS)));
		else
			proceso.setEjecucionId(proceso.getEjecucionId());
		
		if(forceCierre.length>0 && forceCierre[0]!=null && forceCierre[0].equals(TransaccionNegocio.FORCE_CIERRE_CICLO)){
			sql.append("insert into infi_tb_807_procesos (FECHA_FIN,DESC_ERROR,ejecucion_id, transa_id,usuario_id, fecha_inicio, fecha_valor,ciclo_ejecucion_id) values(");
			sql.append("SYSDATE").append(", ");
			sql.append("'"+proceso.getDescripcionError()).append("',");
		}else {
			sql.append("insert into infi_tb_807_procesos (ejecucion_id, transa_id,usuario_id, fecha_inicio, fecha_valor,ciclo_ejecucion_id) values(");	
		}			
		sql.append(proceso.getEjecucionId()).append(", ");
		sql.append("'" + proceso.getTransaId()+ "', ");
		sql.append("'" + proceso.getUsuarioId()+ "', ");
		sql.append(formatearFechaHoraBDActual()).append(", ");
		sql.append(formatearFechaBD(proceso.getFechaValor()) + ", ");
		sql.append(proceso.getCicloEjecucionId()+ ")");
		
		System.out.println("insertar "+sql);
		return sql.toString();
	}
	
	public String modificar(Proceso proceso) throws Exception{
		StringBuilder sql = new StringBuilder();
		
		sql.append("update infi_tb_807_procesos set fecha_fin = ");
		sql.append(formatearFechaHoraBDActual() + " ");
		if (proceso.getDescripcionError() != null ){
			sql.append(",desc_error = '");
			if(proceso.getDescripcionError() != null){
				if (proceso.getDescripcionError().length()>1000){
				proceso.setDescripcionError(proceso.getDescripcionError().substring(0,999));
				}
			}else{
				proceso.setDescripcionError("");
			}
			sql.append(proceso.getDescripcionError().replace("'", "") + "' ");
		}		
		
		sql.append("where ejecucion_id = ");
		sql.append(proceso.getEjecucionId());
		System.out.println("modificar---> "+sql);
		return sql.toString();
	}
	
	/**
	 * Lista los procesos
	 * @param fechaDesde
	 * @param fechaHasta
	 * @param enEjecucion
	 * @param transaccionId
	 * @throws Exception
	 */
	//TTS-531 Modificacion para inclusion de NM que ejecuto un proceso 
	public void listarProcesoRangoFecha(String fechaDesde,String fechaHasta, boolean enEjecucion, String[] transaccionId,String nm) throws Exception{
		StringBuilder sql = new StringBuilder("select a.ejecucion_id,a.transa_id,fecha_inicio,fecha_fin,desc_error,userid, decode(fecha_fin,null,'<a onclick=''return verificar()'' href=''consulta_procesos-actualizar?idejecucion=' || ejecucion_id ||'''>Cerrar Proceso</a>') accion " +
		" from infi_tb_807_procesos a, msc_user b where usuario_id = msc_user_id(+) " +
		" and trunc(fecha_inicio) >= to_date('" + fechaDesde + "','" + ConstantesGenerales.FORMATO_FECHA_HORA_SYSDATE + "') ");
		if (enEjecucion){
			sql.append(" and fecha_fin is null ");
		} else{
			sql.append(" and trunc(fecha_fin) <= to_date('" + fechaHasta + "','" + ConstantesGenerales.FORMATO_FECHA_HORA_SYSDATE + "') ");
		}
		if (transaccionId != null && !transaccionId.equals("")){
			sql.append(" and transa_id='" + transaccionId + "'");
		}
		
		if(nm!=null && !nm.equals("")){			
			sql.append(" and b.USERID='").append(nm.toUpperCase()).append("' ");
		}
		sql.append(" order by ejecucion_id desc");
		
		System.out.println("listarProcesoRangoFecha ---> " + sql.toString());
		dataSet=db.get(dataSource,sql.toString());
	}
	
	/**
	 * Lista los procesos realizados en un rango de fecha
	 * @param String FechaDesde 
	 * @param String FechaHasta 
	 * */
	public void listarProcesoRangoFecha(String fechaDesde,String fechaHasta) throws Exception{
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT * FROM ( SELECT INFI_TB_807_PROCESOS.EJECUCION_ID,INFI_TB_807_PROCESOS.TRANSA_ID,INFI_TB_807_PROCESOS.USUARIO_ID,INFI_TB_807_PROCESOS.FECHA_INICIO,TO_CHAR(INFI_TB_807_PROCESOS.FECHA_FIN,'");
		sql.append(ConstantesGenerales.FORMATO_FECHA_HORA_SYSDATE);
		sql.append("')FECHA_FIN,INFI_TB_807_PROCESOS.DESC_ERROR,INFI_TB_807_PROCESOS.FECHA_VALOR,'' AS ESTATUS_PROCESO,");
		sql.append("CASE WHEN INFI_TB_807_PROCESOS.FECHA_FIN IS NULL THEN 'BLUE'");
		sql.append("WHEN INFI_TB_807_PROCESOS.FECHA_FIN IS NOT NULL AND DESC_ERROR IS NULL THEN 'GREEN'");
		sql.append("WHEN INFI_TB_807_PROCESOS.FECHA_FIN IS NOT NULL AND DESC_ERROR IS NOT NULL THEN 'RED' ");
		sql.append("END color,msc_user.USERID,infi_tb_012_transacciones.TRANSA_DESCRIPCION ");
		sql.append("FROM INFI_TB_807_PROCESOS ");
		sql.append("LEFT JOIN msc_user ON INFI_TB_807_PROCESOS.USUARIO_ID = msc_user.MSC_USER_ID ");
		sql.append("LEFT JOIN infi_tb_012_transacciones ON INFI_TB_807_PROCESOS.TRANSA_ID = infi_tb_012_transacciones.TRANSA_ID ");
		sql.append("WHERE INFI_TB_807_PROCESOS.transa_id in (").append("'");
		sql.append(TransaccionNegocio.PAGO_CUPON).append("','");
		sql.append(TransaccionNegocio.CUSTODIA_AMORTIZACION).append("','");
		sql.append(TransaccionNegocio.CUSTODIA_COMISIONES).append("'").append(") ");
		sql.append("AND fecha_inicio>=to_date('").append(fechaDesde).append("','").append(ConstantesGenerales.FORMATO_FECHA_HORA_SYSDATE);
		sql.append("') ").append("AND fecha_fin<=to_date('");
		sql.append(fechaHasta).append("','").append(ConstantesGenerales.FORMATO_FECHA_HORA_SYSDATE).append("')+1 OR fecha_fin IS NULL ");
		sql.append("ORDER BY EJECUCION_ID DESC ) WHERE ROWNUM BETWEEN 1 AND 20");
		dataSet=db.get(dataSource,sql.toString());
	}
	/**
	 * Lista las ordenes De un proceso ejecutado por pago de cupones,comisiones y amortizacion
	 * @param long ejecucion_id 
	 *@throws Exception
	 * */
	public void listarOrdenesPorProceso(long ejecucion_id) throws Exception{
		StringBuffer sql=new StringBuffer();
		sql.append("select distinct (INFI_TB_207_ORDENES_OPERACION.ORDENE_OPERACION_ID),'none'as disable,decode(INFI_TB_811_INST_OPERACION.PROCESO_ID,null,'',INFI_TB_811_INST_OPERACION.PROCESO_ID)proceso_id,decode(INFI_TB_810_PROCESO_INST.FECHA_REGISTRO,null,'',to_char(INFI_TB_810_PROCESO_INST.FECHA_REGISTRO,'" +
				"yyyy-mm-dd hh:mm:ss" +
				"'))FECHA_REGISTRO,CASE WHEN INFI_TB_811_INST_OPERACION.TIPO_INSTRUCCION_ID='");
		sql.append(TipoInstruccion.CUENTA_SWIFT);
		sql.append("' THEN 'SWIFT' WHEN INFI_TB_811_INST_OPERACION.TIPO_INSTRUCCION_ID='");
		sql.append(TipoInstruccion.CUENTA_NACIONAL);
		sql.append("' THEN 'NACIONAL' WHEN INFI_TB_811_INST_OPERACION.TIPO_INSTRUCCION_ID='");
		sql.append(TipoInstruccion.OPERACION_DE_CAMBIO);
		sql.append("' THEN 'OPERACION DE CAMBIO' WHEN INFI_TB_811_INST_OPERACION.TIPO_INSTRUCCION_ID='");
		sql.append(TipoInstruccion.CHEQUE);
		sql.append("' THEN 'CHEQUE' WHEN INFI_TB_811_INST_OPERACION.TIPO_INSTRUCCION_ID is null THEN '' END TIPO_INSTRUCCION_ID,infi_Tb_100_titulos.INTCCY as moneda_pago,INFI_TB_207_ORDENES_OPERACION.FECHA_APLICAR,INFI_TB_207_ORDENES_OPERACION.STATUS_OPERACION,INFI_TB_203_ORDENES_STATUS.ORDSTA_NOMBRE,infi_tb_204_ordenes.ORDSTA_ID,infi_tb_012_transacciones.TRANSA_DESCRIPCION,infi_Tb_100_titulos.TITULO_DESCRIPCION,infi_tb_204_ordenes.TRANSA_ID,INFI_TB_207_ORDENES_OPERACION.TITULO_ID,INFI_TB_207_ORDENES_OPERACION.fecha_inicio_cp,INFI_TB_207_ORDENES_OPERACION.fecha_fin_cp,INFI_TB_207_ORDENES_OPERACION.MONTO_OPERACION,infi_tb_201_ctes.CLIENT_NOMBRE,infi_tb_204_ordenes.CLIENT_ID,infi_tb_204_ordenes.ORDENE_ID from infi_tb_204_ordenes left join INFI_TB_207_ORDENES_OPERACION on infi_tb_204_ordenes.ORDENE_ID=INFI_TB_207_ORDENES_OPERACION.ORDENE_ID left join infi_tb_201_ctes on infi_tb_204_ordenes.CLIENT_ID=infi_tb_201_ctes.CLIENT_ID left join INFI_TB_206_ORDENES_TITULOS on infi_tb_204_ordenes.ORDENE_ID=INFI_TB_206_ORDENES_TITULOS.ORDENE_ID left join infi_Tb_100_titulos on INFI_TB_207_ORDENES_OPERACION.TITULO_ID=infi_Tb_100_titulos.titulo_id left join infi_tb_012_transacciones on infi_tb_204_ordenes.TRANSA_ID=infi_tb_012_transacciones.TRANSA_ID left join INFI_TB_203_ORDENES_STATUS on infi_tb_204_ordenes.ORDSTA_ID=INFI_TB_203_ORDENES_STATUS.ORDSTA_ID left join INFI_TB_813_PROCESO_OPERACION on INFI_TB_207_ORDENES_OPERACION.ORDENE_OPERACION_ID=INFI_TB_813_PROCESO_OPERACION.ORDENE_OPERACION_ID LEFT JOIN INFI_TB_811_INST_OPERACION ON INFI_TB_813_PROCESO_OPERACION.PROCESO_ID=INFI_TB_811_INST_OPERACION.PROCESO_ID LEFT JOIN INFI_TB_810_PROCESO_INST ON INFI_TB_811_INST_OPERACION.PROCESO_ID=INFI_TB_810_PROCESO_INST.PROCESO_ID where infi_tb_204_ordenes.EJECUCION_ID=");
		sql.append(ejecucion_id).append(" order by TIPO_INSTRUCCION_ID");
		
		dataSet=db.get(dataSource,sql.toString());
		
	}
	public Object moveNext() throws Exception {
		return null;
	}
	
	/**
	 * lista los procesos de un determinado mes
	 * @param anno : Es el a�o que se desea verificar si posee procesos activos 
	 * **/
	public void listarProcesos(String anno)throws Exception{
		String sql="select i807.ejecucion_id,to_char(i807.fecha_valor,'MM')as mes from infi_tb_807_procesos i807 "+
					"left join infi_tb_204_ordenes i204 on i807.EJECUCION_ID=i204.EJECUCION_ID "+
					"left join infi_tb_207_ordenes_operacion i207 on i204.ORDENE_ID=i207.ORDENE_ID "+
					"where i807.transa_id='CUSTODIA_COMISIONES' "+
					"and i207.status_operacion='"+ConstantesGenerales.STATUS_EN_ESPERA+"' or i207.status_operacion='"+ConstantesGenerales.STATUS_RECHAZADA+"' "+
					"and to_date(to_char(i807.FECHA_VALOR,'YYYY'),'YYYY')=to_date('" +anno +"','YYYY') "+
					"group by i807.ejecucion_id,i807.fecha_valor order by i807.fecha_valor asc";
		dataSet=db.get(dataSource,sql);		
	}
	
	/* Actualiza los datos de las instrucciones de pago asociados a los procesos 'NO APLICADOS' 
	 * @param CuentaCliente datos de la cuenta
	 * @return String query de actualizacion 
	 * */
	public String actualizarInstruccionesPagoProcesos(CuentaCliente cuentaCliente){
		StringBuffer sql = new StringBuffer();			
		sql.append("UPDATE INFI_TB_811_INST_OPERACION SET"+
				" CTECTA_NUMERO="+validarCampoNulo(cuentaCliente.getCtecta_numero())+","+
				" CTECTA_NOMBRE="+validarCampoNulo(cuentaCliente.getCtecta_nombre())+","+
				" CTECTA_BCOCTA_BCO="+validarCampoNulo(cuentaCliente.getCtecta_bcocta_bco())+","+
				" CTECTA_BCOCTA_DIRECCION="+validarCampoNulo(cuentaCliente.getCtecta_bcocta_direccion())+","+
				" CTECTA_BCOCTA_SWIFT="+validarCampoNulo(cuentaCliente.getCtecta_bcocta_swift())+","+
				" CTECTA_BCOCTA_BIC="+validarCampoNulo(cuentaCliente.getCtecta_bcocta_bic())+","+
				" CTECTA_BCOCTA_TELEFONO="+validarCampoNulo(cuentaCliente.getCtecta_bcocta_telefono())+","+
				" CTECTA_BCOCTA_ABA="+validarCampoNulo(cuentaCliente.getCtecta_bcocta_aba())+","+
				" CTECTA_BCOINT_BCO="+validarCampoNulo(cuentaCliente.getCtecta_bcoint_bco())+","+
				" CTECTA_OBSERVACION="+validarCampoNulo(cuentaCliente.getCtecta_observacion())+","+
				" CTECTA_BCOINT_DIRECCION="+validarCampoNulo(cuentaCliente.getCtecta_bcoint_direccion())+","+
				" CTECTA_BCOINT_SWIFT="+validarCampoNulo(cuentaCliente.getCtecta_bcoint_swift())+","+
				" CTECTA_BCOINT_BIC="+validarCampoNulo(cuentaCliente.getCtecta_bcoint_bic())+","+			
				" CTECTA_BCOINT_ABA="+validarCampoNulo(cuentaCliente.getCtecta_bcoint_aba())+","+
				" CTECTA_BCOINT_OBSERVACION="+validarCampoNulo(cuentaCliente.getCtecta_bcoint_observacion())+""+
				" WHERE PROCESO_ID IN ("+
				" select distinct(inop.PROCESO_ID) from INFI_TB_811_INST_OPERACION inop,INFI_TB_204_ORDENES o, INFI_TB_810_PROCESO_INST pin"+
				" where pin.CLIENT_ID='"+cuentaCliente.getClient_id()+"' and o.CLIENT_ID=pin.CLIENT_ID and pin.PROCESO_ID=inop.PROCESO_ID"+
				" and o.TRANSA_ID in ('"+TransaccionNegocio.VENTA_TITULOS+"','"+TransaccionNegocio.CUSTODIA_AMORTIZACION+"','"+TransaccionNegocio.SALIDA_INTERNA+"','"+TransaccionNegocio.SALIDA_EXTERNA+"','"+TransaccionNegocio.PACTO_RECOMPRA+"','"+TransaccionNegocio.PAGO_CUPON+"','"+TransaccionNegocio.ORDEN_PAGO+"','"+TransaccionNegocio.CUSTODIA_COMISIONES+"')"+
				" and status_operacion!='"+ConstantesGenerales.STATUS_APLICADA+"'"+
				" and inop.MONEDA_ID!='").append(ConstantesGenerales.SIGLAS_MONEDA_LOCAL).append("')");
		return sql.toString();
		
	}
	
	/**
	 * Lista si existe alg�n registro que indique que la transacci�n se est� ejecutando
	 * @param idTransaccion id de la transacci�n a consultar
	 * @throws Exception lanza una excepci�n si hay un error.
	 */
	//Metodo desarrollado en requerimienro SICAD nm26659
	public void listarPorTransaccionActivaSubastaDivisasSubastaTitulo(String idTransaccion) throws Exception{
		
			StringBuffer sql=new StringBuffer();
			sql.append("select * from infi_tb_807_procesos where fecha_fin is null and transa_id in (");
				sql.append(idTransaccion);
				sql.append(")");			
				//System.out.println("QUERY ---------> " + sql.toString());
			System.out.println("listarPorTransaccionActivaSubastaDivisasSubastaTitulo "+sql);
			dataSet=db.get(dataSource,sql.toString());	
		
		
	}
	
	/**
	 * Lista si existe alg�n registro que indique que la transacci�n se est� ejecutando
	 * @param idTransaccion id de la transacci�n a consultar
	 * @throws Exception lanza una excepci�n si hay un error.
	 */
	//NM26659_10/10/17 Desarrollo Liquidacion DICOM Multimoneda (Metodo de busqueda de Procesos abiertos por transaccion) 
	public void listarProcesosActivosPorTransaccion(ArrayList<String> idTransaccion) throws Exception{
		
			StringBuffer sql=new StringBuffer();
			sql.append("select * from infi_tb_807_procesos where fecha_fin is null ");
			if(idTransaccion!=null && idTransaccion.get(0)!=null){
				sql.append(" and transa_id in (");
				int count=0;
				for (String element : idTransaccion) {
					if(count>0){
						sql.append(",");
					}
					sql.append("'"+element+"'");	
					++count;
				}				
				sql.append(")");				
			}
			
			//System.out.println("QUERY ---------> " + sql.toString());
			dataSet=db.get(dataSource,sql.toString());	
		
		
	}
	
	public void listarPorTransaccionActiva(ArrayList<String> idTransaccion,boolean procesoActivo) throws Exception{
		StringBuffer sql=new StringBuffer();
		sql.append("select * from infi_tb_807_procesos where transa_id in (");
		
		int count=0;
		for (String element : idTransaccion) {			
			if(count>0){
				sql.append(",");
			}
			sql.append("'"+element+"'");	
			++count;
		}	
		sql.append(") ");
		
		if(procesoActivo){
			sql.append(" AND FECHA_FIN IS NULL");
		}
					
		//System.out.println("----------------listarPRocesooo: "+sql.toString());
		dataSet=db.get(dataSource,sql.toString());
	}
}

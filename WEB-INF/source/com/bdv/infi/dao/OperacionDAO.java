package com.bdv.infi.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.sql.DataSource;

import megasoft.Util;
import megasoft.db;

import com.bancovenezuela.comun.data.OrdenOperacion;
import com.bdv.infi.data.CuentaCliente;
import com.bdv.infi.data.FechaValor;
import com.bdv.infi.data.TransaccionFija;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.DataExtendida;
import com.bdv.infi.logic.interfaces.TipoInstruccion;
import com.bdv.infi.logic.interfaces.TransaccionFinanciera;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi_services.beans.entidades.mensajes_peticion.ParametroActualizarOperacionesFinancieras;
import com.bdv.infi_services.beans.entidades.mensajes_respuesta.OperacionFinanciera;

public class OperacionDAO extends GenericoDAO {

	
	public OperacionDAO(DataSource ds) {
		super(ds);
	}

	@Override
	public Object moveNext() throws Exception {
		return null;
	}
	

	/**Actualiza el registro de la tabla  INFI_TB_207_ORDENES_OPERACION valores de monto y tasa 
	 * @param OrdenOperacion operacion
	 * */
	public void modificarOperacion(OrdenOperacion ordenOperacion)throws Exception{
		StringBuffer sql=new StringBuffer();
		sql.append("UPDATE  INFI_TB_207_ORDENES_OPERACION ");
		sql.append("SET  MONTO_OPERACION='"+String.valueOf(ordenOperacion.getMontoOperacion().doubleValue()).replace(".",",")+"', ");
		sql.append("TASA='"+String.valueOf(ordenOperacion.getTasa()).replace(".",",")+"' ");
		sql.append("WHERE  ORDENE_OPERACION_ID="+ordenOperacion.getIdOperacion());
		db.exec(dataSource,sql.toString());
	}
	
	/**Lista los detalles de la operacion financiera 
	 * @param OrdenOperacion operacion
	 * */
	public void listarDetallesOperacionFinanciera(OrdenOperacion ordenOperacion)throws Exception{
		StringBuffer sql=new StringBuffer();
		sql.append("select INFI_TB_805_TRNFIN_TIPO.TRNFIN_TIPO_DESCRIPCION,INFI_TB_800_TRN_FINANCIERAS.trnfin_nombre,infi_tb_100_titulos.TITULO_DESCRIPCION,infi_tb_207_ordenes_operacion.moneda_id,infi_tb_207_ordenes_operacion.moneda_id as moneda_descripcion,infi_tb_204_ordenes.ordene_id,infi_tb_207_ordenes_operacion.*,case when infi_tb_207_ordenes_operacion.in_comision=1 then 'Si' when infi_tb_207_ordenes_operacion.in_comision=0 then 'No' end comisiones,case when infi_tb_207_ordenes_operacion.aplica_reverso=1 then 'Si' when infi_tb_207_ordenes_operacion.aplica_reverso=0 then 'No' end reverso,INFI_TB_033_OPERACION_STATUS.STATUS_OPERACION_NOMB,INFI_TB_800_TRN_FINANCIERAS.TRNFIN_DESCRIPCION from infi_tb_204_ordenes left join infi_tb_207_ordenes_operacion on infi_tb_207_ordenes_operacion.ORDENE_ID=infi_tb_204_ordenes.ORDENE_ID left join INFI_TB_033_OPERACION_STATUS on INFI_TB_033_OPERACION_STATUS.STATUS_OPERACION=infi_tb_207_ordenes_operacion.STATUS_OPERACION left join infi_tb_100_titulos on infi_tb_207_ordenes_operacion.TITULO_ID=infi_tb_100_titulos.TITULO_ID left join INFI_TB_800_TRN_FINANCIERAS on INFI_TB_800_TRN_FINANCIERAS.TRNFIN_ID=infi_tb_207_ordenes_operacion.TRNFIN_ID left join INFI_TB_805_TRNFIN_TIPO on INFI_TB_207_ORDENES_OPERACION.TRNF_TIPO=INFI_TB_805_TRNFIN_TIPO.TRNFIN_TIPO where INFI_TB_207_ORDENES_OPERACION.ordene_operacion_id=");
		sql.append(ordenOperacion.getIdOperacion());
		sql.append(" order by ordene_operacion_id");
		dataSet=db.get(dataSource,sql.toString());
	}
	
	/**Lista los Intentos realizados para una transacci&oacute;n financiera
	 * @param OrdenOperacion operacion
	 * */
	public void listarIntentosOperacion(OrdenOperacion ordenOperacion)throws Exception{
		StringBuffer sql=new StringBuffer();
		sql.append("select INFI_TB_209_ORDENES_OPERAC_INT.*,case when INFI_TB_209_ORDENES_OPERAC_INT.aplico=1 then 'Si' when INFI_TB_209_ORDENES_OPERAC_INT.aplico=0 then 'No' end aplica from INFI_TB_209_ORDENES_OPERAC_INT where ordene_operacion_id=");
		sql.append(ordenOperacion.getIdOperacion()).append(" order by operacion_intento_id desc");
		dataSet=db.get(dataSource,sql.toString());
	}
	
	/**Lista todos los campos de la tabla INFI_TB_207_ORDENES_OPERACION detalles de la operacion financiera
	 * Metodo no modificable, no alterar el orden de los campos 
	 * @param ordene_id Id de una orden especifica
	 * */
	public void listarOperacionOrden(Long orden)throws Exception{
		StringBuffer sql=new StringBuffer();
		sql.append("SELECT TRNF_TIPO,TRNFIN_ID,MONTO_OPERACION,TASA,ORDENE_OPERACION_ERROR,SERIAL,IN_COMISION,MONEDA_ID,CTECTA_NUMERO,");
		sql.append("CTECTA_NOMBRE,CTECTA_BCOCTA_BCO,CTECTA_BCOCTA_DIRECCION,CTECTA_BCOCTA_SWIFT,CTECTA_BCOCTA_BIC,CTECTA_BCOCTA_TELEFONO,");
		sql.append("CTECTA_BCOCTA_ABA,CTECTA_BCOCTA_PAIS,CTECTA_BCOINT_BCO,CTECTA_BCOINT_DIRECCION,CTECTA_BCOINT_SWIFT,CTECTA_BCOINT_BIC,");
		sql.append("CTECTA_BCOINT_TELEFONO,CTECTA_BCOINT_ABA,CTECTA_BCOINT_PAIS,IN_COMISION_INVARIABLE,ORDENE_OPERACION_ID,NUMERO_RETENCION,");
		sql.append("OPERACION_NOMBRE,STATUS_OPERACION,APLICA_REVERSO, '' AS id_comision_ui,FECHA_APLICAR FROM INFI_TB_207_ORDENES_OPERACION WHERE ORDENE_ID=");
		sql.append(orden);
		sql.append(" ORDER BY ORDENE_OPERACION_ID");
		dataSet=db.get(dataSource,sql.toString());
	}
	
	/**Actualiza el status de la operacion de la tabla  INFI_TB_207_ORDENES_OPERACION con el valor enviado 
	 * @param Operacion operacion
	 * */
	public void modificarOperacion(String status, Long ordenOperacion)throws Exception{
		StringBuffer sql=new StringBuffer();
		sql.append("UPDATE  INFI_TB_207_ORDENES_OPERACION ");
		sql.append("SET  STATUS_OPERACION='");
		sql.append(status).append("'");
		sql.append(" WHERE  ORDENE_OPERACION_ID=").append(ordenOperacion);
		db.exec(dataSource,sql.toString());
	}
	
	/**Actualiza el status de la operacion de la tabla  INFI_TB_207_ORDENES_OPERACION con el valor enviado 
	 * @param Operacion operacion
	 * */
	public String modificarOperacionStatus(String status, long ordenOperacion, String fechaProcesada)throws Exception{
		StringBuffer sql=new StringBuffer();
		sql.append("UPDATE  INFI_TB_207_ORDENES_OPERACION ");
		sql.append("SET  STATUS_OPERACION='");
		sql.append(status).append("'");
		if (fechaProcesada != null){
		   sql.append(",FECHA_PROCESADA=TO_DATE('" + fechaProcesada +"','yyyyMMdd')");
		}
		sql.append(" WHERE ORDENE_OPERACION_ID=").append(ordenOperacion);
		
		return sql.toString();
	}
	
	/**Resetea el numero de retencion de una operacion en la tabla  INFI_TB_207_ORDENES_OPERACION  
	 * @param ordenOperacion operacion
	 * */
	public String resetNumeroRetencion(long ordenOperacion)throws Exception{
		StringBuffer sql=new StringBuffer();
		sql.append("UPDATE  INFI_TB_207_ORDENES_OPERACION ");
		sql.append("SET NUMERO_RETENCION=null");
		sql.append(" WHERE ORDENE_OPERACION_ID=").append(ordenOperacion);
		
		return sql.toString();
	}
	
	
	/**Actualiza el status y el número de movimiento de la operacion de la tabla  INFI_TB_207_ORDENES_OPERACION con el valor enviado 
	 * @param OrdenOperacion operacion que debe actualizarse
	 * */
	public String modificarOperacionStatusMovimiento(OrdenOperacion operacion)throws Exception{
		StringBuffer sql=new StringBuffer();
		sql.append("UPDATE  INFI_TB_207_ORDENES_OPERACION ");
		sql.append("SET  STATUS_OPERACION='");
		sql.append(operacion.getStatusOperacion()).append("'");
		
		//Verificamos estatus a ver si le ponemos fecha de procesamiento
		if (operacion.getStatusOperacion().equals(ConstantesGenerales.STATUS_APLICADA)){
			sql.append(",FECHA_PROCESADA=SYSDATE ");			
		}
		
		//Verifica si hay número de movimiento
		if (!operacion.getNumeroMovimiento().equals("")){
			sql.append(",NUMERO_MOVIMIENTO='").append(operacion.getNumeroMovimiento()).append("'");
		}
		
		//Verifica si hay número de retención
		if (operacion.getNumeroRetencion()!=null &&  !operacion.getNumeroRetencion().equals("")){
			sql.append(",NUMERO_RETENCION='").append(operacion.getNumeroRetencion()).append("'");
		}		
		sql.append("WHERE  ORDENE_OPERACION_ID=").append(operacion.getIdOperacion());
		
		return sql.toString();
	}	
	
	/**Inserta un nuevo registro en la tabla INFI_TB_207_ORDENES_OPERACION valores de monto y tasa 
	 * @param Operacion operacion
	 * */
	public void insertarOperacion(OrdenOperacion operacion)throws Exception{
		StringBuffer sqlOperaciones=new StringBuffer();
		sqlOperaciones.append("INSERT INTO INFI_TB_207_ORDENES_OPERACION (ORDENE_ID,ORDENE_OPERACION_ID,");
		sqlOperaciones.append("TRNFIN_ID,STATUS_OPERACION,");
		sqlOperaciones.append("APLICA_REVERSO,MONTO_OPERACION,TASA,FECHA_APLICAR,FECHA_PROCESADA,");
		sqlOperaciones.append("ORDENE_OPERACION_ERROR,SERIAL,IN_COMISION,MONEDA_ID,");					
		sqlOperaciones.append("CTECTA_NUMERO,CTECTA_NOMBRE,CTECTA_BCOCTA_BCO,CTECTA_BCOCTA_DIRECCION,CTECTA_BCOCTA_SWIFT,");
		sqlOperaciones.append("CTECTA_BCOCTA_BIC,CTECTA_BCOCTA_TELEFONO,CTECTA_BCOCTA_ABA,CTECTA_BCOCTA_PAIS,CTECTA_BCOINT_BCO,");
		sqlOperaciones.append("CTECTA_BCOINT_DIRECCION,CTECTA_BCOINT_SWIFT,CTECTA_BCOINT_BIC,CTECTA_BCOINT_TELEFONO,CTECTA_BCOINT_ABA,");   
		sqlOperaciones.append("CTECTA_BCOINT_PAIS,TRNF_TIPO,TITULO_ID)");					
		sqlOperaciones.append(" VALUES(");
		long idOperacion = Long.valueOf(dbGetSequence(this.dataSource, ConstantesGenerales.SECUENCIA_ORDENES_OPERACION)).longValue();
		//operacion.setIdOperacion(idOperacion);
		sqlOperaciones.append(operacion.getIdOrden()).append(",");
		sqlOperaciones.append(idOperacion).append(",");
		sqlOperaciones.append("'").append(operacion.getIdTransaccionFinanciera()).append("',");
		sqlOperaciones.append("'").append(operacion.getStatusOperacion()).append("',");
		//sqlOperaciones.append(operacion.get getAplicaReverso()?ConstantesGenerales.VERDADERO:ConstantesGenerales.FALSO).append(",");
		sqlOperaciones.append(operacion.getMontoOperacion().doubleValue()).append(",");
		sqlOperaciones.append(operacion.getTasa()).append(",");
		sqlOperaciones.append(operacion.getFechaAplicar()==null?formatearFechaBDActual():this.formatearFechaBD(operacion.getFechaAplicar())).append(",");
		sqlOperaciones.append(operacion.getFechaProcesada()==null?"NULL":formatearFechaBD(operacion.getFechaProcesada())).append(",");
		sqlOperaciones.append(operacion.getErrorNoAplicacion()==null?"NULL":"'"+operacion.getErrorNoAplicacion()+"'").append(",");
		sqlOperaciones.append((operacion.getSerialContable()==null)||(operacion.getSerialContable()=="")?"NULL":"'"+operacion.getSerialContable()+"',");
		sqlOperaciones.append(operacion.getInComision()).append(",");					
		sqlOperaciones.append("'").append(operacion.getIdMoneda()).append("',");
		sqlOperaciones.append("'").append(operacion.getNumeroCuenta()).append("',");
		sqlOperaciones.append("'").append(operacion.getNombreReferenciaCuenta()).append("',");
		sqlOperaciones.append("'").append(operacion.getNombreBanco()).append("',");
		sqlOperaciones.append("'").append(operacion.getDireccionBanco()).append("',");
		sqlOperaciones.append("'").append(operacion.getCodigoSwiftBanco()).append("',");
		sqlOperaciones.append("'").append(operacion.getCodigoBicBanco()).append("',");
		sqlOperaciones.append("'").append(operacion.getTelefonoBanco()).append("',");
		sqlOperaciones.append("'").append(operacion.getCodigoABA()).append("',");					
		sqlOperaciones.append("'").append(operacion.getPaisBancoCuenta()).append("',");
		sqlOperaciones.append("'").append(operacion.getNombreBancoIntermediario()).append("',");
		sqlOperaciones.append("'").append(operacion.getDireccionBancoIntermediario()).append("',");
		sqlOperaciones.append("'").append(operacion.getCodigoSwiftBancoIntermediario()).append("',");					
		sqlOperaciones.append("'").append(operacion.getCodigoBicBancoIntermediario()).append("',");
		sqlOperaciones.append("'").append(operacion.getTelefonoBancoIntermediario()).append("',");
		sqlOperaciones.append("'").append(operacion.getCodigoABAIntermediario()).append("',");					
		sqlOperaciones.append("'").append(operacion.getPaisBancoIntermediario()).append("',");
		sqlOperaciones.append("'").append(operacion.getTipoTransaccionFinanc()).append("',");
		sqlOperaciones.append("'").append(operacion.getIdTitulo()).append("'");
		//sqlOperaciones.append("'").append(operacion.getSerialContable()).append("'");
		sqlOperaciones.append(")");		
		db.exec(dataSource,sqlOperaciones.toString());
	}
	/**
	 * Lista las operaciones financieras que cumplan con las siguientes condiciones:
	 * <li>Status = En Espera</li>
	 * <li>Tipo Transaccion Financiera = CRE (Cr&eacute;dito)</li>
	 * <li>Moneda diferente a la moneda local</li>
	 * <li>PAgo Cupón,venta de títulos,pacto recompra,amortización u orden de pago</li>
	 * <li>Data Extendida tipo de instrucción = CHEQUE</li>
	 * @param long cedula
	 * @param String  tipperId
	 * @return HashMap Con clave ordene_id, valor ordene_operacion_id
	 * @throws Exception
	 */

	public HashMap<String,String> listarOperacionesFinancieras(long cedula,String tipperId) throws Exception{
		
		StringBuffer sql = new StringBuffer();
			sql.append("select infi_tb_207_ordenes_operacion.monto_operacion,infi_tb_204_ordenes.ordene_id,infi_tb_204_ordenes.transa_id as concepto,infi_tb_207_ordenes_operacion.TITULO_ID,infi_tb_100_titulos.TITULO_DESCRIPCION,to_char(infi_tb_100_titulos.TITULO_FE_EMISION,'ddMMyyyy')as titulo_fe_emision,to_char(infi_tb_100_titulos.TITULO_FE_VENCIMIENTO,'ddMMyyyy')as titulo_fe_vencimiento,to_char(infi_tb_207_ordenes_operacion.FECHA_APLICAR,'ddMMyyyy')as fecha_operacion,infi_tb_207_ordenes_operacion.ORDENE_OPERACION_ID,infi_tb_207_ordenes_operacion.MONEDA_ID,infi_tb_207_ordenes_operacion.MONEDA_ID AS MONEDA_DESCRIPCION from infi_tb_201_ctes left join infi_tb_204_ordenes on infi_tb_201_ctes.CLIENT_ID=infi_tb_204_ordenes.CLIENT_ID inner join INFI_TB_212_ORDENES_DATAEXT on infi_tb_204_ordenes.ORDENE_ID = INFI_TB_212_ORDENES_DATAEXT.ORDENE_ID left join infi_tb_207_ordenes_operacion on infi_tb_204_ordenes.ORDENE_ID=infi_tb_207_ordenes_operacion.ORDENE_ID left join infi_tb_100_titulos on trim(infi_tb_207_ordenes_operacion.TITULO_ID)=trim(infi_tb_100_titulos.TITULO_ID) where client_cedrif=");
			sql.append(cedula);
			sql.append(" and infi_tb_207_ordenes_operacion.moneda_id!='").append(ConstantesGenerales.SIGLAS_MONEDA_LOCAL).append("' ");
			sql.append("and status_operacion='").append(com.bdv.infi.logic.interfaces.ConstantesGenerales.STATUS_EN_ESPERA);
			sql.append("' and (infi_tb_204_ordenes.transa_id='").append(TransaccionNegocio.PAGO_CUPON).append("'");
			sql.append(" or infi_tb_204_ordenes.transa_id='").append(TransaccionNegocio.VENTA_TITULOS).append("'");
			sql.append(" or infi_tb_204_ordenes.transa_id='").append(TransaccionNegocio.PACTO_RECOMPRA).append("'");
			sql.append(" or infi_tb_204_ordenes.transa_id='").append(com.bdv.infi.logic.interfaces.TransaccionNegocio.CUSTODIA_AMORTIZACION).append("' or infi_tb_204_ordenes.transa_id='ORDEN_PAGO')");
			sql.append(" and trnf_tipo='").append(com.bdv.infi.logic.interfaces.TransaccionFinanciera.CREDITO).append("'");
			sql.append(" and upper(infi_tb_201_ctes.TIPPER_ID) like upper('").append(tipperId).append("')");
			sql.append(" and INFI_TB_212_ORDENES_DATAEXT.DTAEXT_ID=trim('" +DataExtendida.TIPO_INSTRUCCION_PAGO +"')and DTAEXT_VALOR=trim("+TipoInstruccion.CHEQUE +")");
			dataSet = db.get(dataSource, sql.toString());
			
			HashMap<String,String> operaciones = new HashMap<String, String>();
			if(dataSet.count()>0){
				dataSet.first();
				while(dataSet.next()){
					operaciones.put(dataSet.getValue("ordene_id"), dataSet.getValue("ordene_operacion_id"));
				}//fin while
			}//fin if
			
			return operaciones;
	}//fin metodo
	/**
	 * Actualiza la operacion financiera enviada via WEB SERVICES
	 * @param ParametroActualizarOperacionesFinancieras operacion
	 * @throws Exception
	 */
public void actualizarOperacionesFinancieras(ParametroActualizarOperacionesFinancieras operacion,Statement stmt) throws Exception{
	StringBuffer sql = new StringBuffer();
		try {
			
			sql.append("update infi_tb_207_ordenes_operacion set cheque_numero=").append(operacion.getNumeroCheque()).append("");
			sql.append(",status_operacion='").append(com.bdv.infi.logic.interfaces.ConstantesGenerales.STATUS_APLICADA).append("'");
			sql.append(" where ordene_operacion_id=").append(operacion.getIdOperacionFinanciera());
			sql.append(" and ordene_id=").append(operacion.getIdOrden());
			
			stmt.execute(sql.toString());
			if(stmt.getUpdateCount()==0){
				throw new Exception("No existe ningún registro para la Orden: "+operacion.getIdOrden()+" y la operación financiera: "+operacion.getIdOperacionFinanciera());
			}
		
		} catch (Exception e) {
			StringBuffer sqlError = new StringBuffer();
			sqlError.append("update infi_tb_207_ordenes_operacion");
			sqlError.append(" set ordene_operacion_error='").append(Util.replace(e.getMessage().trim(), "'", "")).append("'");
			sqlError.append(",status_operacion='").append(com.bdv.infi.logic.interfaces.ConstantesGenerales.STATUS_RECHAZADA).append("'");
			sqlError.append(" where ordene_operacion_id=").append(operacion.getIdOperacionFinanciera());
			sqlError.append(" and ordene_id=").append(operacion.getIdOrden());
	
			db.exec(dataSource, sqlError.toString());
			throw e;
		}//Fin catch
	}//fin actualizarOperacionesFinanancierasWS

/**
 * Actualiza los códigos de operación de las operaciones financieras que no se han ejecutado o que han fallado
 * @param codigoOperacion código de operación que se debe actualizar 
 * @param idTransaccion id de transacción que se desea modificar
 * @return Sql con la instrucción de actualización
 */
public String actualizarCodigoOperacion(TransaccionFija transaccionFija){
	StringBuffer sql = new StringBuffer();	
	sql.append("update infi_tb_207_ordenes_operacion set codigo_operacion='").append(transaccionFija.getCodigoOperacionFija()).append("'");
	sql.append(" where trnfin_id='").append(transaccionFija.getIdTransaccion()).append("'");
	sql.append(" and status_operacion in ('").append(ConstantesGenerales.STATUS_EN_ESPERA).append("',");
	sql.append(" '").append(ConstantesGenerales.STATUS_RECHAZADA).append("')");
	sql.append(" and ordene_id in(");
	sql.append(" select ordene_id from infi_tb_204_ordenes where ");
	sql.append(" transa_id in('").append(transaccionFija.getIdTransaccionNegocio()).append("')");
	sql.append(")");	
	sql.append(" and (codigo_operacion<>'").append(transaccionFija.getCodigoOperacionFija()).append("' or codigo_operacion is null)");
	return sql.toString();	
}

/**
 * Actualiza los códigos de operación de las operaciones financieras que no se han ejecutado o que han fallado
 * @param codigoOperacion código de operación que se debe actualizar 
 * @param idVehiculo id del vehículo para buscar las ordenes relacionadas a las operaciones
 * @param idTransaccion id de transacción que se desea modificar
 * @return Sql con la instrucción de actualización
 */
public String[] actualizarCodigoOperacionVehiculo(TransaccionFija transaccionFija){
	ArrayList<String> lista = new ArrayList<String>();	
	
	StringBuilder sql = new StringBuilder();
	
	if (transaccionFija.getIdTransaccionNegocio().equals(TransaccionNegocio.LIQUIDACION)){
		//Crédito	
		sql = new StringBuilder();
		sql.append("update infi_tb_207_ordenes_operacion set codigo_operacion='").append(transaccionFija.getCodigoOperacionCteCre()).append("'");	
		sql.append(" where TRNFIN_ID ='").append(transaccionFija.getIdTransaccion()).append("'");
		sql.append(" and status_operacion in ('").append(ConstantesGenerales.STATUS_EN_ESPERA).append("',");
		sql.append(" '").append(ConstantesGenerales.STATUS_RECHAZADA).append("')");
		sql.append(" and trnf_tipo='").append(TransaccionFinanciera.CREDITO).append("'");
		sql.append(" and ordene_id in(");
		sql.append(" select ordene_id from infi_tb_204_ordenes a, infi_tb_106_unidad_inversion b where ordene_veh_tom='").append(transaccionFija.getIdVehiculo()).append("'");
		sql.append(" and transa_id in('").append(transaccionFija.getIdTransaccionNegocio()).append("')");
		sql.append(" and a.uniinv_id = b.undinv_id and b.insfin_id=").append(transaccionFija.getIdInstrumentoFinanciero()).append(")");	
		sql.append(" and (codigo_operacion<>'").append(transaccionFija.getCodigoOperacionCteCre()).append("' or codigo_operacion is null)");
		lista.add(sql.toString());
	}else{
		//Débito
		
		sql.append("update infi_tb_207_ordenes_operacion set codigo_operacion='").append(transaccionFija.getCodigoOperacionCteDeb()).append("'");	
		sql.append(" where TRNFIN_ID ='").append(transaccionFija.getIdTransaccion()).append("'");
		sql.append(" and status_operacion in ('").append(ConstantesGenerales.STATUS_EN_ESPERA).append("',");
		sql.append(" '").append(ConstantesGenerales.STATUS_RECHAZADA).append("')");
		sql.append(" and trnf_tipo='").append(TransaccionFinanciera.DEBITO).append("'");
		sql.append(" and ordene_id in(");
		sql.append(" select ordene_id from infi_tb_204_ordenes a, infi_tb_106_unidad_inversion b where ordene_veh_tom='").append(transaccionFija.getIdVehiculo()).append("'");
		sql.append(" and transa_id in('").append(TransaccionNegocio.TOMA_DE_ORDEN).append("','").append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA).append("')");
		sql.append(" and a.uniinv_id = b.undinv_id and b.insfin_id=").append(transaccionFija.getIdInstrumentoFinanciero()).append(")");	
		sql.append(" and (codigo_operacion<>'").append(transaccionFija.getCodigoOperacionCteDeb()).append("' or codigo_operacion is null) ");

		lista.add(sql.toString());

		//Crédito	
		sql = new StringBuilder();
		sql.append("update infi_tb_207_ordenes_operacion set codigo_operacion='").append(transaccionFija.getCodigoOperacionCteCre()).append("'");	
		sql.append(" where TRNFIN_ID ='").append(transaccionFija.getIdTransaccion()).append("'");
		sql.append(" and status_operacion in ('").append(ConstantesGenerales.STATUS_EN_ESPERA).append("',");
		sql.append(" '").append(ConstantesGenerales.STATUS_RECHAZADA).append("')");
		sql.append(" and trnf_tipo='").append(TransaccionFinanciera.CREDITO).append("'");
		sql.append(" and ordene_id in(");
		sql.append(" select ordene_id from infi_tb_204_ordenes a, infi_tb_106_unidad_inversion b where ordene_veh_tom='").append(transaccionFija.getIdVehiculo()).append("'");
		sql.append(" and transa_id in('").append(TransaccionNegocio.TOMA_DE_ORDEN).append("','").append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA).append("','").append(TransaccionNegocio.ORDEN_PAGO).append("')");
		sql.append(" and a.uniinv_id = b.undinv_id and b.insfin_id=").append(transaccionFija.getIdInstrumentoFinanciero()).append(")");	
		sql.append(" and (codigo_operacion<>'").append(transaccionFija.getCodigoOperacionCteCre()).append("' or codigo_operacion is null)");
		
		lista.add(sql.toString());
		
		//Bloqueo	
		sql = new StringBuilder();
		sql.append("update infi_tb_207_ordenes_operacion set codigo_operacion='").append(transaccionFija.getCodigoOperacionCteBlo()).append("'");	
		sql.append(" where TRNFIN_ID ='").append(transaccionFija.getIdTransaccion()).append("'");
		sql.append(" and status_operacion in ('").append(ConstantesGenerales.STATUS_EN_ESPERA).append("',");
		sql.append(" '").append(ConstantesGenerales.STATUS_RECHAZADA).append("')");
		sql.append(" and trnf_tipo='").append(TransaccionFinanciera.BLOQUEO).append("'");
		sql.append(" and ordene_id in(");
		sql.append(" select ordene_id from infi_tb_204_ordenes a, infi_tb_106_unidad_inversion b where ordene_veh_tom='").append(transaccionFija.getIdVehiculo()).append("'");
		sql.append(" and transa_id in('").append(TransaccionNegocio.TOMA_DE_ORDEN).append("','").append(TransaccionNegocio.TOMA_DE_ORDEN_CARTERA_PROPIA).append("')");
		sql.append(" and a.uniinv_id = b.undinv_id and b.insfin_id=").append(transaccionFija.getIdInstrumentoFinanciero()).append(")");	
		sql.append(" and (codigo_operacion<>'").append(transaccionFija.getCodigoOperacionCteBlo()).append("' or codigo_operacion is null)");
		
		lista.add(sql.toString());	
		
		//Se actualizan las operaciones de las ordenes de los vehículos
		sql = new StringBuilder();
		sql.append("update infi_tb_207_ordenes_operacion set codigo_operacion='").append(transaccionFija.getCodigoOperacionVehCre()).append("'");	
		sql.append(" where TRNFIN_ID ='").append(transaccionFija.getIdTransaccion()).append("'");
		sql.append(" and status_operacion in ('").append(ConstantesGenerales.STATUS_EN_ESPERA).append("',");
		sql.append(" '").append(ConstantesGenerales.STATUS_RECHAZADA).append("')");
		sql.append(" and trnf_tipo='").append(TransaccionFinanciera.CREDITO).append("'");
		sql.append(" and ordene_id in(");
		sql.append(" select ordene_id from infi_tb_204_ordenes a, infi_tb_106_unidad_inversion b where ordene_veh_tom='").append(transaccionFija.getIdVehiculo()).append("'");
		sql.append(" and transa_id in('").append(TransaccionNegocio.ORDEN_VEHICULO).append("')");	
		sql.append(" and a.uniinv_id = b.undinv_id and b.insfin_id=").append(transaccionFija.getIdInstrumentoFinanciero()).append(")");	
		sql.append(" and (codigo_operacion<>'").append(transaccionFija.getCodigoOperacionVehCre()).append("' or codigo_operacion is null)");
		
		lista.add(sql.toString());
		
		sql = new StringBuilder();
		sql.append("update infi_tb_207_ordenes_operacion set codigo_operacion='").append(transaccionFija.getCodigoOperacionVehDeb()).append("'");	
		sql.append(" where TRNFIN_ID ='").append(transaccionFija.getIdTransaccion()).append("'");
		sql.append(" and status_operacion in ('").append(ConstantesGenerales.STATUS_EN_ESPERA).append("',");
		sql.append(" '").append(ConstantesGenerales.STATUS_RECHAZADA).append("')");
		sql.append(" and trnf_tipo='").append(TransaccionFinanciera.DEBITO).append("'");
		sql.append(" and ordene_id in(");
		sql.append(" select ordene_id from infi_tb_204_ordenes a, infi_tb_106_unidad_inversion b where ordene_veh_tom='").append(transaccionFija.getIdVehiculo()).append("'");
		sql.append(" and transa_id in('").append(TransaccionNegocio.ORDEN_VEHICULO).append("')");	
		sql.append(" and a.uniinv_id = b.undinv_id and b.insfin_id=").append(transaccionFija.getIdInstrumentoFinanciero()).append(")");	
		sql.append(" and (codigo_operacion<>'").append(transaccionFija.getCodigoOperacionVehDeb()).append("' or codigo_operacion is null)");
		
		lista.add(sql.toString());		
	}

	String[] consultas = new String[lista.size()];
	int i=0;
	for (String c: lista){
		consultas[i] = c;
		i++;
	}
	return consultas;
}

/**
 * Lista la Operacion financiera que fue actualizada
 * @param operacion
 * @throws Exception
 */
public void listarOperacionFinanciera(OperacionFinanciera operacion,Connection conn) throws Exception{
	StringBuffer sql 					= new StringBuffer();
	
	sql.append("select * from infi_tb_207_ordenes_operacion where 1=1");
	sql.append(" and ordene_operacion_id=").append(operacion.getIdOperacionFinanciera());
	sql.append(" and ordene_id=").append(operacion.getIdOrden());
	statement = conn.createStatement();
	resultSet = statement.executeQuery(sql.toString());
	
		if(resultSet.next()){
			operacion.setError(resultSet.getString("ordene_operacion_error"));
			operacion.setIdOperacionFinanciera(resultSet.getString("ordene_operacion_id"));
			operacion.setIdOrden(resultSet.getString("ordene_id"));
			operacion.setStatus(resultSet.getString("status_operacion"));
		}
	this.closeResources();
}//fin metodo
/**
 * Lista las operacones financieras para ser insertadas en los objetos que seran retornados por los Servicios Web
 * @param ordeneId
 * @throws Exception
 */
 //ITS_2117 NM26659_31/07/2014 Modificacion para resolucion inc abonos cta dolares
public void listarOperacionesFinancieraOrden(long ordeneId) throws Exception{
	StringBuffer sql = new StringBuffer();
	sql.append("select status_operacion,monto_operacion,tasa,moneda_id,trnf_tipo,case when in_comision=0 then 'NO' when in_comision=1 then 'SI' end comision from infi_tB_207_ordenes_operacion where ordene_id=");
	sql.append(ordeneId);
	sql.append(" AND TRNF_TIPO NOT IN ('").append(TransaccionFinanciera.BLOQUEO).append("','").append(TransaccionFinanciera.DESBLOQUEO).append("') ");
	//System.out.println("ITS_2117- listarOperacionesFinancieraOrden-> " + sql.toString());
	dataSet = db.get(dataSource, sql.toString());
}//fin metodo

/**
 * Lista el monto reintegro de capital y comision dada una orden,lo guarda en un dataset, campos:
 * <li>Reintegro_comision</li>
 * <li>Reintegro_capital</li>
 * @param ordeneId
 * @throws Exception
 */
public void listarMontoReintegro(long ordeneId) throws Exception{
	StringBuffer sql = new StringBuffer();
	
	sql.append("select a.ordene_id,a.ordene_ped_fe_orden,a.ordene_fecha_adjudicacion,((select sum(monto_operacion) from infi_tb_207_ordenes_operacion where infi_tb_207_ordenes_operacion.ordene_id=a.ordene_id and infi_tb_207_ordenes_operacion.FECHA_APLICAR=a.ordene_ped_fe_orden and infi_tb_207_ordenes_operacion.TRNF_TIPO='").append(TransaccionFinanciera.DEBITO);
			sql.append("' and infi_tb_207_ordenes_operacion.IN_COMISION=").append(ConstantesGenerales.VERDADERO);
			sql.append(")-(select sum(monto_operacion) from infi_tb_207_ordenes_operacion where infi_tb_207_ordenes_operacion.ordene_id=a.ordene_id and infi_tb_207_ordenes_operacion.FECHA_APLICAR=a.ordene_fecha_adjudicacion and infi_tb_207_ordenes_operacion.TRNF_TIPO='").append(TransaccionFinanciera.DEBITO);
			sql.append("' and infi_tb_207_ordenes_operacion.IN_COMISION=").append(ConstantesGenerales.VERDADERO);
			sql.append("))reintegro_comision,");
			sql.append("((select sum(monto_operacion) from infi_tb_207_ordenes_operacion where infi_tb_207_ordenes_operacion.ordene_id=a.ordene_id and infi_tb_207_ordenes_operacion.FECHA_APLICAR=a.ordene_ped_fe_orden and infi_tb_207_ordenes_operacion.TRNF_TIPO='");
			sql.append(TransaccionFinanciera.DEBITO);
			sql.append("' and infi_tb_207_ordenes_operacion.IN_COMISION=").append(ConstantesGenerales.FALSO).append(")-");
			sql.append("(select sum(monto_operacion) from infi_tb_207_ordenes_operacion where infi_tb_207_ordenes_operacion.ordene_id=a.ordene_id and infi_tb_207_ordenes_operacion.FECHA_APLICAR=a.ordene_fecha_adjudicacion and infi_tb_207_ordenes_operacion.TRNF_TIPO='");
			sql.append(TransaccionFinanciera.DEBITO);
			sql.append("' and infi_tb_207_ordenes_operacion.IN_COMISION=").append(ConstantesGenerales.FALSO).append("))reintegro_capital ");
			sql.append("from infi_tb_204_ordenes a ");
			sql.append("where ordene_id=").append(ordeneId);
	dataSet = db.get(dataSource, sql.toString());
}//fin metodo
/**
 * 
 * @param numeroCheque
 * @throws Throwable
 */
public void listarOperacionCheque(String numeroCheque) throws Throwable{
	StringBuffer sql = new StringBuffer();
	try {
		sql.append("select * from infi_tb_207_ordenes_operacion where CHEQUE_NUMERO='").append(numeroCheque).append("'");
		dataSet = db.get(dataSource, sql.toString());
	} catch (Throwable e) {
		throw e;
	}

	if(dataSet.count()>0){
		throw new Throwable("Ya existe una operaci&oacute;n pagada con el n&uacute;mero de cheque indicado.");
	}
}//fin metodo
/**
 * Lista los diferentes tipos de moneda, de los cuales se le deben operaciones al cliente
 * @param long clienteId
 * @throws Exception
 */
public void operacionesPendienteMoneda(long clienteId)throws Exception{
	StringBuffer sb = new StringBuffer();
	sb.append("select distinct infi_tb_207_ordenes_operacion.moneda_id from infi_tb_204_ordenes left join infi_tb_207_ordenes_operacion on infi_tb_204_ordenes.ORDENE_ID = infi_tb_207_ordenes_operacion.ORDENE_ID left join infi_tb_201_ctes on infi_tb_204_ordenes.client_id = infi_tb_201_ctes.client_id left join INFI_TB_012_TRANSACCIONES on infi_tb_204_ordenes.TRANSA_ID = INFI_TB_012_TRANSACCIONES.TRANSA_ID where infi_tb_207_ordenes_operacion.CTECTA_NUMERO is null and CTECTA_NOMBRE  is null and CTECTA_BCOCTA_BCO is null and CTECTA_BCOCTA_DIRECCION  is null and CTECTA_BCOCTA_SWIFT is null and CTECTA_BCOCTA_BIC is null and CTECTA_BCOCTA_TELEFONO is null and CTECTA_BCOCTA_ABA is null and CTECTA_BCOCTA_PAIS is null and CTECTA_BCOINT_BCO is null and CTECTA_BCOINT_DIRECCION is null and CTECTA_BCOINT_SWIFT is null and CTECTA_BCOINT_BIC is null and CTECTA_BCOINT_TELEFONO is null and CTECTA_BCOINT_ABA is null and CTECTA_BCOINT_PAIS is null and infi_tb_207_ordenes_operacion.STATUS_OPERACION = '");
	sb.append(ConstantesGenerales.STATUS_EN_ESPERA);
	sb.append("' and infi_tb_207_ordenes_operacion.TRNF_TIPO='");
	sb.append(TransaccionFinanciera.CREDITO);
	sb.append("' and infi_tb_207_ordenes_operacion.MONEDA_ID!='").append(ConstantesGenerales.SIGLAS_MONEDA_LOCAL).append("'");
	//sb.append(ConstantesGenerales.VERDADERO);
	sb.append("and infi_tb_207_ordenes_operacion.ORDENE_OPERACION_ID not in (select ordene_operacion_id from  INFI_TB_813_PROCESO_OPERACION) and infi_tb_204_ordenes.client_id =");
	sb.append(clienteId);
	
	dataSet = db.get(dataSource,sb.toString());
}//fin metodo

/**
 * Lista las operaciones que no tengan ninguna instrucci&oacute;n de pago definida para
 * un cliente especifico y una moneda dada (Pago de Cup&oacute;n y Amortizaci&oacute;n)
 * @param clienteId
 * @param moneda
 * @throws Exception
 */
public void listarOperacionesGestionPago(long clienteId,String moneda)throws Exception{
	StringBuffer sb = new StringBuffer();
	sb.append("select infi_tb_204_ordenes.*,a.*,INFI_TB_012_TRANSACCIONES.*,a.moneda_id as moneda from infi_tb_204_ordenes "); 
	sb.append("left join infi_tb_207_ordenes_operacion a on infi_tb_204_ordenes.ORDENE_ID = a.ORDENE_ID "); 
	sb.append("left join infi_tb_201_ctes on infi_tb_204_ordenes.client_id = infi_tb_201_ctes.client_id "); 
	sb.append("left join INFI_TB_012_TRANSACCIONES on infi_tb_204_ordenes.TRANSA_ID = INFI_TB_012_TRANSACCIONES.TRANSA_ID "); 
	sb.append("where a.CTECTA_NUMERO is null "); 
	sb.append("and CTECTA_NOMBRE  is null and CTECTA_BCOCTA_BCO is null "); 
	sb.append("and CTECTA_BCOCTA_DIRECCION  is null and CTECTA_BCOCTA_SWIFT is null "); 
	sb.append("and CTECTA_BCOCTA_BIC is null and CTECTA_BCOCTA_TELEFONO is null "); 
	sb.append("and CTECTA_BCOCTA_ABA is null and CTECTA_BCOCTA_PAIS is null "); 
	sb.append("and CTECTA_BCOINT_BCO is null and CTECTA_BCOINT_DIRECCION is null "); 
	sb.append("and CTECTA_BCOINT_SWIFT is null and CTECTA_BCOINT_BIC is null and CTECTA_BCOINT_TELEFONO is null "); 
	sb.append("and CTECTA_BCOINT_ABA is null and CTECTA_BCOINT_PAIS is null and a.STATUS_OPERACION = '").append(ConstantesGenerales.STATUS_EN_ESPERA).append("' "); 
	sb.append("and a.TRNF_TIPO= '").append(TransaccionFinanciera.CREDITO).append("'").append(" and a.ORDENE_OPERACION_ID not in (select ordene_operacion_id from  INFI_TB_813_PROCESO_OPERACION) "); 
	sb.append("and infi_tb_204_ordenes.client_id =").append(clienteId); 
	
	if(moneda!=null)
		sb.append(" and a.moneda_id='").append(moneda).append("' ");
	
	sb.append(" and (infi_tb_204_ordenes.TRANSA_ID='"); 
	sb.append("PAGO_CUPONES"); 
	sb.append("' or infi_tb_204_ordenes.TRANSA_ID='"); 
	sb.append("PAGO_AMORTIZACION").append("')"); 
	
	dataSet = db.get(dataSource,sb.toString());
}


/**
 * Lista las operaciones para pago de cupones y amortizaciones que no tengan ninguna instrucci&oacute;n de pago definida para
 * un cliente espec&iacute;fico y que se encuentren en espera pendientes por pagar (cr&eacute;dito)
 * @param clienteId
 * @param moneda
 * @throws Exception
 */
public void listarOperacionesGestionPago(long clienteId)throws Exception{
	StringBuffer sb = new StringBuffer();
	sb.append("select a.*, a.moneda_id as moneda_oper, INFI_TB_012_TRANSACCIONES.TRANSA_DESCRIPCION from infi_tb_204_ordenes "); 
	sb.append("left join infi_tb_207_ordenes_operacion a on infi_tb_204_ordenes.ORDENE_ID = a.ORDENE_ID "); 
	sb.append("left join infi_tb_201_ctes on infi_tb_204_ordenes.client_id = infi_tb_201_ctes.client_id "); 
	sb.append("left join INFI_TB_012_TRANSACCIONES on infi_tb_204_ordenes.TRANSA_ID = INFI_TB_012_TRANSACCIONES.TRANSA_ID "); 
	sb.append(" where a.STATUS_OPERACION = '").append(ConstantesGenerales.STATUS_EN_ESPERA).append("' ");
	/*sb.append("where a.CTECTA_NUMERO is null "); 
	sb.append("and CTECTA_NOMBRE  is null and CTECTA_BCOCTA_BCO is null "); 
	sb.append("and CTECTA_BCOCTA_DIRECCION  is null and CTECTA_BCOCTA_SWIFT is null "); 
	sb.append("and CTECTA_BCOCTA_BIC is null and CTECTA_BCOCTA_TELEFONO is null "); 
	sb.append("and CTECTA_BCOCTA_ABA is null and CTECTA_BCOCTA_PAIS is null "); 
	sb.append("and CTECTA_BCOINT_BCO is null and CTECTA_BCOINT_DIRECCION is null "); 
	sb.append("and CTECTA_BCOINT_SWIFT is null and CTECTA_BCOINT_BIC is null and CTECTA_BCOINT_TELEFONO is null "); 
	sb.append("and CTECTA_BCOINT_ABA is null and CTECTA_BCOINT_PAIS is null and a.STATUS_OPERACION = '").append(ConstantesGenerales.STATUS_EN_ESPERA).append("' "); */
	sb.append(" and a.TRNF_TIPO= '").append(TransaccionFinanciera.CREDITO).append("' "); 
	sb.append(" and infi_tb_204_ordenes.client_id =").append(clienteId); 	
	sb.append(" and infi_tb_204_ordenes.TRANSA_ID IN ('"); 
	sb.append(TransaccionNegocio.PAGO_CUPON).append("', '"); 
	sb.append(TransaccionNegocio.VENTA_TITULOS).append("', '"); 
	sb.append(TransaccionNegocio.CUSTODIA_AMORTIZACION).append("')"); 
	sb.append(" order by moneda_oper, a.titulo_id, fecha_inicio_cp ");
	
	dataSet = db.get(dataSource,sb.toString());
}
/**
 * <b>Lista la ordenes que cumplan con las siguientes condiciones:</b>
 * <li><b>Ordenes</b> que tengan operaciones financieras con status en espera</li>
 * <li><b>Transacción de Negocio:</b>Venta de Títulos,Amortización,Salida Interna,Externa,Recompra,Cupones</li>
 * <li><b>Moneda id</b> diferente a la moneda local de la vista correspondiente</li>
 * <li><b>Tipo instrucción Data Ext:</b> Cuenta SWIFT</li>
 * <li><b>Tipo instrucción Data Ext:</b> CHEQUE donde el campo de numero de cheque no sea null</li>
 * @param fechaDesde puede ser null
 * @param fechaHasta puede ser null
 * @param ordeneId puede ser 0
 * @param statusOperacion
 * @throws Exception
 */
public void listarSwift(Date fechaDesde, Date fechaHasta,long ordeneId,String statusOperacion) throws Exception{
	StringBuffer sql = new StringBuffer();
	SimpleDateFormat formato = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
	
	sql.append("select to_char(FECHA_PAGO_CHEQUE,'dd/mm/yyyy') as fecha_pagoCH,infi_tb_201_ctes.client_nombre,infi_tb_207_ordenes_operacion.*,infi_tb_204_ordenes.ordene_id,transa_id,infi_tb_204_ordenes.client_id,infi_tb_204_ordenes.ORDENE_ID_RELACION,dtaext_id,dtaext_valor,INFI_TB_045_TIPO_INSTRUCCION.INSTRUCCION_NOMBRE from infi_tb_204_ordenes ");
	sql.append("left join infi_tb_201_ctes on infi_tb_201_ctes.CLIENT_ID=infi_tb_204_ordenes.CLIENT_ID ");
	sql.append("left join INFI_TB_212_ORDENES_DATAEXT on infi_tb_204_ordenes.ORDENE_ID=INFI_TB_212_ORDENES_DATAEXT.ORDENE_ID ");
	sql.append("left join infi_tb_207_ordenes_operacion on infi_tb_204_ordenes.ORDENE_ID=infi_tb_207_ordenes_operacion.ORDENE_ID ");
	sql.append("left join INFI_TB_045_TIPO_INSTRUCCION on dtaext_valor = INFI_TB_045_TIPO_INSTRUCCION.TIPO_INSTRUCCION_ID ");
	sql.append("where 1=1  and transa_id in('").append(TransaccionNegocio.VENTA_TITULOS).append("' ");
	sql.append(",'").append(TransaccionNegocio.CUSTODIA_AMORTIZACION).append("' ");
	sql.append(",'").append(TransaccionNegocio.SALIDA_INTERNA).append("' ");
	sql.append(",'").append(TransaccionNegocio.SALIDA_EXTERNA).append("' ");
	sql.append(",'").append(TransaccionNegocio.PACTO_RECOMPRA ).append("' ");
	sql.append(",'").append(TransaccionNegocio.PAGO_CUPON).append("' ");
	sql.append(",'").append(TransaccionNegocio.ORDEN_PAGO).append("')");
	sql.append("and STATUS_OPERACION='").append(statusOperacion).append("' ");
	sql.append("and infi_tb_207_ordenes_operacion.MONEDA_ID!='").append(ConstantesGenerales.SIGLAS_MONEDA_LOCAL).append("'");
	//sql.append(ConstantesGenerales.STATUS_ACTIVO).append(")");
	sql.append("and INFI_TB_212_ORDENES_DATAEXT.DTAEXT_VALOR='").append(TipoInstruccion.CHEQUE);
	sql.append("' and infi_tb_207_ordenes_operacion.CHEQUE_NUMERO is not null ");
			
	if(fechaDesde!=null && fechaHasta!=null){
		sql.append(" and trunc(infi_tb_207_ordenes_operacion.fecha_aplicar)>=to_date('").append(formato.format(fechaDesde)).append("','").append(ConstantesGenerales.FORMATO_FECHA);
		sql.append("') and trunc(infi_tb_207_ordenes_operacion.fecha_aplicar)<=to_date('").append(formato.format(fechaHasta)).append("','").append(ConstantesGenerales.FORMATO_FECHA);
		sql.append("')");
	}else{
		if (fechaHasta!=null){
			sql.append(" and trunc(infi_tb_207_ordenes_operacion.fecha_aplicar)<=to_date('").append(formato.format(fechaHasta)).append("','").append(ConstantesGenerales.FORMATO_FECHA);
			sql.append("') ");
		}
	}
	
	if(ordeneId!=0){
		sql.append(" and infi_tb_204_ordenes.ordene_id=").append(ordeneId).append("");
	}
	sql.append(" union ");
	sql.append("select to_char(FECHA_PAGO_CHEQUE,'dd/mm/yyyy') as fecha_pagoCH,infi_tb_201_ctes.client_nombre,infi_tb_207_ordenes_operacion.*,infi_tb_204_ordenes.ordene_id,transa_id,infi_tb_204_ordenes.client_id,infi_tb_204_ordenes.ORDENE_ID_RELACION,dtaext_id,dtaext_valor,INFI_TB_045_TIPO_INSTRUCCION.INSTRUCCION_NOMBRE from infi_tb_204_ordenes ");
	sql.append("left join infi_tb_201_ctes on infi_tb_201_ctes.CLIENT_ID=infi_tb_204_ordenes.CLIENT_ID ");
	sql.append("left join INFI_TB_212_ORDENES_DATAEXT on infi_tb_204_ordenes.ORDENE_ID=INFI_TB_212_ORDENES_DATAEXT.ORDENE_ID ");
	sql.append("left join infi_tb_207_ordenes_operacion on infi_tb_204_ordenes.ORDENE_ID=infi_tb_207_ordenes_operacion.ORDENE_ID ");
	sql.append("left join INFI_TB_045_TIPO_INSTRUCCION on dtaext_valor = INFI_TB_045_TIPO_INSTRUCCION.TIPO_INSTRUCCION_ID ");
	sql.append("where 1=1  and transa_id in('").append(TransaccionNegocio.VENTA_TITULOS).append("' ");
	sql.append(",'").append(TransaccionNegocio.CUSTODIA_AMORTIZACION).append("' ");
	sql.append(",'").append(TransaccionNegocio.SALIDA_INTERNA).append("' ");
	sql.append(",'").append(TransaccionNegocio.SALIDA_EXTERNA).append("' ");
	sql.append(",'").append(TransaccionNegocio.PACTO_RECOMPRA ).append("' ");
	sql.append(",'").append(TransaccionNegocio.PAGO_CUPON).append("' ");
	sql.append(",'").append(TransaccionNegocio.ORDEN_PAGO).append("')");
	sql.append("and STATUS_OPERACION='").append(statusOperacion).append("' ");
	sql.append("and infi_tb_207_ordenes_operacion.MONEDA_ID!='").append(ConstantesGenerales.SIGLAS_MONEDA_LOCAL).append("'");
	//sql.append(ConstantesGenerales.STATUS_ACTIVO).append(")");
	sql.append("and INFI_TB_212_ORDENES_DATAEXT.DTAEXT_VALOR='").append(TipoInstruccion.CUENTA_SWIFT).append("'");
			
	if(fechaDesde!=null && fechaHasta!=null){
		sql.append(" and trunc(infi_tb_207_ordenes_operacion.fecha_aplicar)>=to_date('").append(formato.format(fechaDesde)).append("','").append(ConstantesGenerales.FORMATO_FECHA);
		sql.append("') and trunc(infi_tb_207_ordenes_operacion.fecha_aplicar)<=to_date('").append(formato.format(fechaHasta)).append("','").append(ConstantesGenerales.FORMATO_FECHA);
		sql.append("')");
	}else{
		if (fechaHasta!=null){
			sql.append(" and trunc(infi_tb_207_ordenes_operacion.fecha_aplicar)<=to_date('").append(formato.format(fechaHasta)).append("','").append(ConstantesGenerales.FORMATO_FECHA);
			sql.append("') ");
		}
	}
	
	if(ordeneId!=0){
		sql.append(" and infi_tb_204_ordenes.ordene_id=").append(ordeneId).append("");
	}	
	
	sql.append("order by 3");
	//System.out.println("listarSwift: "+sql.toString());
	dataSet = db.get(dataSource, sql.toString());
	
}//fin metodo

/**
 * Muestra las operaciones de CREDITO para una unidad de inversion y sus vehiculos
 * @param unidadInversion
 * @throws Exception
 */
	public boolean listarOperacionesBCV(long unidadInversion,String statusOperacion)throws Exception{
		
		StringBuffer sql = new StringBuffer();
		boolean existe = false;
		
		sql.append("select o.ORDENE_ID,op.ORDENE_OPERACION_ID,op.MONTO_OPERACION,op.STATUS_OPERACION,c.CLIENT_NOMBRE,v.VEHICU_NOMBRE");
		sql.append(" from infi_tb_207_ordenes_operacion op,INFI_TB_204_ORDENES o,INFI_TB_201_CTES c,INFI_TB_018_VEHICULOS v");
		sql.append(" where o.ORDENE_ID=op.ORDENE_ID and c.CLIENT_ID=o.CLIENT_ID and v.VEHICU_ID=o.ORDENE_VEH_TOM and");
		sql.append(" o.ordene_id= (select ordene_id from infi_tb_204_ordenes where uniinv_id=").append(unidadInversion);
		sql.append(" and transa_id='").append(TransaccionNegocio.LIQUIDACION).append("')");
		
		if(statusOperacion!=null && !statusOperacion.equals(""))
		{
			sql.append(" and status_operacion='").append(statusOperacion).append("'");
		}
		//Ejecutamos consulta
		dataSet = db.get(dataSource,sql.toString());

		//Verificamos si existen registros
		if(dataSet.count()>0){
			existe = true;
		}
		System.out.println("listarOperacionesBCV "+sql);
		return existe;
	}//fin metodo
	
	/**
	 * Lista la OrdenOperacion de CREDITO realizada al banco central por id de orden Vehiculo
	 * @param idOrden ORDEN VEHÍCULO
	 * @param numeroCuentaBancoCentral : Número de cuenta del vehículo en BCV
	 * @throws Exception
	 */
	public OrdenOperacion listarOperacionCreditoBCV(long idOrden,String numeroCuentaBancoCentral)throws Exception{
		
		StringBuffer sql = new StringBuffer();
		
		//Objeto OrdenOperacion que se retornara
		OrdenOperacion ordenOperacion = null;
		
		sql.append("select * from infi_tb_204_ordenes inner join infi_tb_207_ordenes_operacion on infi_tb_204_ordenes.ordene_id=infi_tb_207_ordenes_operacion.ordene_id ");
		sql.append("where infi_tb_204_ordenes.ordene_id=").append(idOrden);
		sql.append(" and infi_tb_207_ordenes_operacion.ctecta_numero='").append(numeroCuentaBancoCentral).append("'");
		
		//Se ejcuta la consulta
		dataSet = db.get(dataSource,sql.toString());
		
		if(dataSet.count()>0)
		{
			dataSet.first();
			dataSet.next();

			//Set valores al Objeto OrdenOperacion
			ordenOperacion = new OrdenOperacion();
	    	ordenOperacion.setStatusOperacion(dataSet.getValue("STATUS_OPERACION"));
	    	ordenOperacion.setMontoOperacion(new BigDecimal(dataSet.getValue("MONTO_OPERACION")));
	    	ordenOperacion.setNumeroCuenta(dataSet.getValue("CTECTA_NUMERO"));
	    	ordenOperacion.setCodigoOperacion(dataSet.getValue("CODIGO_OPERACION"));
	    	ordenOperacion.setNombreOperacion(dataSet.getValue("OPERACION_NOMBRE"));
	    	ordenOperacion.setTipoTransaccionFinanc(dataSet.getValue("TRNF_TIPO"));
	    	ordenOperacion.setIdMoneda(dataSet.getValue("MONEDA_ID"));
	    	ordenOperacion.setIdOrden(Long.parseLong(dataSet.getValue("ORDENE_ID")));
	    	ordenOperacion.setIdOperacion(Long.parseLong(dataSet.getValue("ORDENE_OPERACION_ID")));
		}//fin if
	
		//Se retorna el Objeto Orden Operacion
		return ordenOperacion;
	}//fin listarOperacionCreditoBCV
	
	/**
	 * Lista los intentos operacion por orden dada
	 * @param ordeneId
	 * @throws Exception
	 */
	public void listarIntentosOperacion(long ordeneId)throws Exception{
		
		StringBuffer sql=new StringBuffer();
		
		sql.append("select to_char(fecha,'").append(ConstantesGenerales.FORMATO_FECHA_HORA_SYSDATE);
		sql.append("')fecha,error_desc from INFI_TB_209_ORDENES_OPERAC_INT where ordene_operacion_id=").append(ordeneId);
		sql.append(" order by fecha desc");
		
		//Se almacena en DataSet
		dataSet=db.get(dataSource,sql.toString());
		
	}//fin listarIntentosOperacion
	
	/**
	 * 
	 * @return Consulta
	 * @throws Exception
	 */
	public String actualizarChequeoperacion (String numeroCheque,long operacionId)throws Exception{
		
		StringBuffer sql = new StringBuffer();
		//Busca la fecha de valor del cheque que debe ser aplicado
		FechaValorDAO fechaValorDAO = new FechaValorDAO(this.dataSource);
		FechaValor fechaValor = fechaValorDAO.listar(com.bdv.infi.logic.interfaces.FechaValor.PAGO_CHEQUES);		
		
		sql.append("update INFI_TB_207_ORDENES_OPERACION set cheque_numero='").append(numeroCheque);
		sql.append("',fecha_pago_cheque=").append(formatearFechaBDActual());
		sql.append(",status_operacion='").append(ConstantesGenerales.STATUS_EN_ESPERA).append("'");
		sql.append(",fecha_aplicar=").append(this.formatearFechaBD(fechaValor.getFechaValor()));
		sql.append(" where ordene_operacion_id=").append(operacionId);
		return sql.toString();
	}

	/**
	 * Busca los totales por moneda en las operaciones pendientes por pagar de un cliente determinado para pago de cupones y amortizaciones
	 * @param idCliente
	 * @throws Exception
	 */
	public void totalesOperacionesPagosPorMoneda(long idCliente) throws Exception {
	
		StringBuffer sb = new StringBuffer();
		sb.append("select a.moneda_id, sum(a.monto_operacion) as monto_por_moneda from infi_tb_204_ordenes "); 
		sb.append("left join infi_tb_207_ordenes_operacion a on infi_tb_204_ordenes.ORDENE_ID = a.ORDENE_ID "); 
		sb.append("left join infi_tb_201_ctes on infi_tb_204_ordenes.client_id = infi_tb_201_ctes.client_id "); 
		sb.append("left join INFI_TB_012_TRANSACCIONES on infi_tb_204_ordenes.TRANSA_ID = INFI_TB_012_TRANSACCIONES.TRANSA_ID "); 
		sb.append(" where a.STATUS_OPERACION = '").append(ConstantesGenerales.STATUS_EN_ESPERA).append("' ");
		/*sb.append("where a.CTECTA_NUMERO is null "); 
		sb.append("and CTECTA_NOMBRE  is null and CTECTA_BCOCTA_BCO is null "); 
		sb.append("and CTECTA_BCOCTA_DIRECCION  is null and CTECTA_BCOCTA_SWIFT is null "); 
		sb.append("and CTECTA_BCOCTA_BIC is null and CTECTA_BCOCTA_TELEFONO is null "); 
		sb.append("and CTECTA_BCOCTA_ABA is null and CTECTA_BCOCTA_PAIS is null "); 
		sb.append("and CTECTA_BCOINT_BCO is null and CTECTA_BCOINT_DIRECCION is null "); 
		sb.append("and CTECTA_BCOINT_SWIFT is null and CTECTA_BCOINT_BIC is null and CTECTA_BCOINT_TELEFONO is null "); 
		sb.append("and CTECTA_BCOINT_ABA is null and CTECTA_BCOINT_PAIS is null and a.STATUS_OPERACION = '").append(ConstantesGenerales.STATUS_EN_ESPERA).append("' "); */
		sb.append(" and a.TRNF_TIPO= '").append(TransaccionFinanciera.CREDITO).append("' "); 
		sb.append(" and infi_tb_204_ordenes.client_id =").append(idCliente); 	
		sb.append(" and infi_tb_204_ordenes.TRANSA_ID IN ('"); 
		sb.append(TransaccionNegocio.PAGO_CUPON).append("', '"); 
		sb.append(TransaccionNegocio.VENTA_TITULOS).append("', '"); 
		sb.append(TransaccionNegocio.CUSTODIA_AMORTIZACION).append("')"); 
		sb.append(" group by a.moneda_id");
		
		dataSet = db.get(dataSource,sb.toString());
		
	}

	/**
	 * Asigna un mumero de cuenta a las operaciones de ordenes de cobro de comisiones de un cliente determinado
	 * @param idCliente
	 * @param numeroCuenta
	 * @return
	 * @throws Exception
	 */
	public String asignarNroCuentaOperacionesCobroComision(String idCliente, String numeroCuenta) throws Exception {
	
		StringBuffer sb = new StringBuffer();
		
		sb.append(" update INFI_TB_207_ORDENES_OPERACION set ");
		sb.append(" CTECTA_NUMERO = '").append(numeroCuenta).append("' ");
		sb.append(" WHERE ctecta_numero is null and ordene_operacion_id IN ");
		sb.append(" (select ordene_operacion_id from INFI_TB_207_ORDENES_OPERACION where ordene_id IN (select ordene_id from infi_tb_204_ordenes where client_id = ").append(idCliente).append(" and transa_id = '").append(TransaccionNegocio.CUSTODIA_COMISIONES).append("'))");
				
		return sb.toString();
	}
	
	/**Lista todos los campos de la tabla INFI_TB_207_ORDENES_OPERACION detalles de la operacion y sus datos del beneficiario
	 * @param ordene_id Id de una orden especifica
	 * */
	public void listarOperacionVentaTitulos(Long orden)throws Exception{
		StringBuffer sql=new StringBuffer();
		sql.append("select o.*, (select dtaext_valor from INFI_TB_212_ORDENES_DATAEXT od where o.ordene_id=od.ordene_id and dtaext_id='").append(DataExtendida.BENEFICIARIO).append("') as nombre_beneficiario, (select dtaext_valor from INFI_TB_212_ORDENES_DATAEXT od where o.ordene_id=od.ordene_id and dtaext_id='").append(DataExtendida.CEDULA_BENEFICIARIO).append("') as cedula_beneficiario from INFI_TB_207_ORDENES_OPERACION o where o.ordene_id=");
		sql.append(orden);
		dataSet=db.get(dataSource,sql.toString());
	}
	
	
	public void listarOperacionesSinIntruccionesVentaTitulos(Long clienteId,String moneda,String instruccion)throws Exception{

		StringBuffer sb = new StringBuffer();
		sb.append("select infi_tb_204_ordenes.*,a.*,INFI_TB_012_TRANSACCIONES.*,a.moneda_id as moneda from infi_tb_204_ordenes "); 
		sb.append("left join infi_tb_207_ordenes_operacion a on infi_tb_204_ordenes.ORDENE_ID = a.ORDENE_ID "); 
		sb.append("left join infi_tb_201_ctes on infi_tb_204_ordenes.client_id = infi_tb_201_ctes.client_id "); 
		sb.append("left join INFI_TB_012_TRANSACCIONES on infi_tb_204_ordenes.TRANSA_ID = INFI_TB_012_TRANSACCIONES.TRANSA_ID ");
		sb.append("left join INFI_TB_212_ORDENES_DATAEXT on infi_tb_204_ordenes.ordene_id=INFI_TB_212_ORDENES_DATAEXT.ordene_id ");
		sb.append("where a.CTECTA_NUMERO is null "); 
		sb.append("and CTECTA_NOMBRE  is null and CTECTA_BCOCTA_BCO is null "); 
		sb.append("and CTECTA_BCOCTA_DIRECCION  is null and CTECTA_BCOCTA_SWIFT is null "); 
		sb.append("and CTECTA_BCOCTA_BIC is null and CTECTA_BCOCTA_TELEFONO is null "); 
		sb.append("and CTECTA_BCOCTA_ABA is null and CTECTA_BCOCTA_PAIS is null "); 
		sb.append("and CTECTA_BCOINT_BCO is null and CTECTA_BCOINT_DIRECCION is null "); 
		sb.append("and CTECTA_BCOINT_SWIFT is null and CTECTA_BCOINT_BIC is null and CTECTA_BCOINT_TELEFONO is null "); 
		sb.append("and CTECTA_BCOINT_ABA is null and CTECTA_BCOINT_PAIS is null and a.STATUS_OPERACION = '").append(ConstantesGenerales.STATUS_EN_ESPERA).append("' "); 
		sb.append("and a.TRNF_TIPO= '").append(TransaccionFinanciera.CREDITO).append("'").append(" and a.ORDENE_OPERACION_ID not in (select ordene_operacion_id from  INFI_TB_813_PROCESO_OPERACION) "); 
		sb.append("and infi_tb_204_ordenes.client_id =").append(clienteId); 
		
		if(moneda!=null)
			sb.append(" and a.moneda_id='").append(moneda).append("' ");
		
		sb.append(" and (infi_tb_204_ordenes.TRANSA_ID='"); 
		sb.append(TransaccionNegocio.VENTA_TITULOS).append("')"); 
		
		//Si la instruccion es igual a null o vacio, se buscan las operaciones con ese valor en data extendida
		if(instruccion.equalsIgnoreCase(null) || instruccion.equalsIgnoreCase("0") || instruccion.equalsIgnoreCase("") || instruccion.equals("&nbsp;"))
		{
			sb.append(" and dtaext_valor is null");
		}else
		{
			sb.append(" and dtaext_valor=").append("'").append(instruccion).append("'");
		}
			
		
		dataSet = db.get(dataSource,sb.toString());

	}
	/**
	 * Verifica si la operacion financiera fue aplicada, de ser asi actualiza las operaciones relacionadas
	 * @return boolean
	 * @throws Exception
	 */
	public boolean isAplicada(long idOperacion)throws Exception{
		boolean aplicada = false;
		
		StringBuffer sql = new StringBuffer();
		sql.append("select STATUS_OPERACION from infi_tb_207_ordenes_operacion where ORDENE_OPERACION_ID=");
		sql.append(idOperacion);
		
		dataSet = db.get(dataSource,sql.toString());
		
		if(dataSet.count()>0)
		{
			dataSet.next();
			
			//Si la operacion se encuentra aplicada se actualizan sus operaciones
			if(dataSet.getValue("status_operacion").equals(ConstantesGenerales.STATUS_APLICADA))
			{
				db.exec(dataSource,"update infi_tb_207_ordenes_operacion set STATUS_OPERACION='"+ConstantesGenerales.STATUS_APLICADA+"' where ORDENE_RELAC_OPERACION_ID="+idOperacion);
			}//fin if
		}//fin if
		
		return aplicada;
	}//fin metodo
	

	/**
	 * Metodo que busca todas las operaciones asociadas a una empresa en espera o rechazadas
	 * @param empresaId
	 * @param numeroCuenta
	 * @return La consulta para actualizar el numero de cuenta de las operaciones
	 * @throws Exception
	 */
	public String actualizarOperacionesNumeroCuenta(String empresaId,String numeroCuenta)throws Exception{
		
		StringBuffer sql = new StringBuffer();
		ArrayList<String> ordenes = new ArrayList<String>();
		
		sql.append("select ordene_operacion_id from INFI_TB_204_ORDENES ");
		sql.append("left join INFI_TB_207_ORDENES_OPERACION on INFI_TB_204_ORDENES.ordene_id=INFI_TB_207_ORDENES_OPERACION.ordene_id ");
		sql.append("where empres_id=").append(empresaId);
		sql.append(" and status_operacion in ('").append(ConstantesGenerales.STATUS_EN_ESPERA);
		sql.append("','").append(ConstantesGenerales.STATUS_RECHAZADA).append("')");

		dataSet = db.get(dataSource,sql.toString());
		
		if(dataSet.count()>0)
		{
			dataSet.first();
			dataSet.next();
			while (dataSet.next()) {				
				ordenes.add(dataSet.getValue("ordene_operacion_id"));				
			}//fin while
		}//fin if
		
		
		if(ordenes.size()>0)
		{
			sql = new StringBuffer();		
			sql.append("update INFI_TB_207_ORDENES_OPERACION set ctecta_numero='").append(numeroCuenta);
			sql.append("' where ordene_operacion_id in (");
			
			for(int i=0; i<ordenes.size();i++){
				//Es el ultimo
				if(i+1==ordenes.size())
				{
					sql.append("'").append(ordenes.get(i)).append("')");
				}else{
					sql.append("'").append(ordenes.get(i)).append("',");
				}
			}
		}
		
		//Retornamos el query
		return sql.toString();
	}
	
	/**
	 * Metodo que busca todas las operaciones asociadas a un vehiculo en espera o rechazadas
	 * @param vehiculoId
	 * @param numeroCuenta
	 * @return La consulta para actualizar el numero de cuenta de las operaciones
	 * @throws Exception
	 */
	public String actualizarOperacionesNumeroCuentaVehiculo(String vehiculoId,String numeroCuenta)throws Exception{
		
		StringBuffer sql = new StringBuffer();
		ArrayList<String> ordenes = new ArrayList<String>();
		
		sql.append("select ordene_operacion_id from INFI_TB_204_ORDENES ");
		sql.append("left join INFI_TB_207_ORDENES_OPERACION on INFI_TB_204_ORDENES.ordene_id=INFI_TB_207_ORDENES_OPERACION.ordene_id ");
		sql.append("where ORDENE_VEH_TOM=").append(vehiculoId);
		sql.append(" and status_operacion in ('").append(ConstantesGenerales.STATUS_EN_ESPERA);
		sql.append("','").append(ConstantesGenerales.STATUS_RECHAZADA).append("')");

		dataSet = db.get(dataSource,sql.toString());
		
		if(dataSet.count()>0)
		{
			dataSet.first();
			dataSet.next();
			while (dataSet.next()) {				
				ordenes.add(dataSet.getValue("ordene_operacion_id"));				
			}//fin while
		}//fin if
		
		
		if(ordenes.size()>0)
		{
			sql = new StringBuffer();		
			sql.append("update INFI_TB_207_ORDENES_OPERACION set ctecta_numero='").append(numeroCuenta);
			sql.append("' where ordene_operacion_id in (");
			
			for(int i=0; i<ordenes.size();i++){
				//Es el ultimo
				if(i+1==ordenes.size())
				{
					sql.append("'").append(ordenes.get(i)).append("')");
				}else{
					sql.append("'").append(ordenes.get(i)).append("',");
				}
			}
		}
		
		//Retornamos el query
		return sql.toString();
	}
	
	/**
	 * Lista la ordenes por id	 
	 * @param fechaDesde puede ser null
	 * @param fechaHasta puede ser null
	 * @param ordeneId puede ser 0
	 * @param statusOperacion
	 * @throws Exception
	 */
	public void listarSwiftPorId(String ordenesId,String statusOperacion) throws Exception{
		StringBuffer sql = new StringBuffer();	
		sql.append("select to_char(oo.FECHA_PAGO_CHEQUE,'dd/mm/yyyy') as fecha_pagoCH,oo.ORDENE_ID,oo.ORDENE_OPERACION_ID,"+
				"oo.TRNFIN_ID,oo.TRNF_TIPO,oo.CTECTA_NUMERO,oo.CTECTA_NOMBRE,oo.CTECTA_BCOCTA_BCO,oo.CTECTA_BCOCTA_DIRECCION,"+
				"oo.CTECTA_BCOCTA_SWIFT,oo.CTECTA_BCOCTA_BIC,oo.CTECTA_BCOCTA_TELEFONO,oo.CTECTA_BCOCTA_ABA,oo.CTECTA_BCOCTA_PAIS,"+
				"oo.CTECTA_BCOINT_BCO,oo.CTECTA_BCOINT_DIRECCION,oo.CTECTA_BCOINT_SWIFT,oo.CTECTA_BCOINT_BIC,oo.CTECTA_BCOINT_TELEFONO,"+
				"oo.CTECTA_BCOINT_ABA,oo.CTECTA_BCOINT_PAIS,oo.APLICA_REVERSO,oo.CHEQUE_NUMERO,oo.MONEDA_ID,oo.STATUS_OPERACION,"+
				"oo.MONTO_OPERACION,oo.SERIAL,oo.TASA,oo.IN_COMISION,o.CLIENT_ID,o.TRANSA_ID,o.ORDENE_ID_RELACION,oo.FECHA_APLICAR"+
				" from infi_tb_207_ordenes_operacion oo,infi_tb_204_ordenes o"+
				" where o.ORDENE_ID=oo.ORDENE_ID and oo.ORDENE_OPERACION_ID in (").append(ordenesId).append(")");
		//ITS-803: Duplicidad de operaciones SWIFT
		if (statusOperacion!=null){
			sql.append(" and oo.STATUS_OPERACION = '").append(statusOperacion).append("'");
		}
			
		//System.out.println("listarSwiftPorId: "+sql.toString());
		dataSet = db.get(dataSource, sql.toString());
		
	}//fin metodo
		

		/**
		 * Lista la ordenes por id	 
		 * @param fechaDesde puede ser null
		 * @param fechaHasta puede ser null
		 * @param ordeneId puede ser 0
		 * @param statusOperacion
		 * @throws Exception
		 */
		public void listarSwiftPorIdInstruccion202(String operacionesId,String tipoProd) throws Exception{
			StringBuffer sql = new StringBuffer();
			
			sql.append("SELECT OO.ORDENE_ID, OO.ORDENE_OPERACION_ID, OO.TRNFIN_ID, OO.TRNF_TIPO, TO_CHAR (OO.FECHA_PAGO_CHEQUE, 'DD/MM/YYYY') AS FECHA_PAGOCH,");
			sql.append("OO.APLICA_REVERSO, OO.CHEQUE_NUMERO, OO.MONEDA_ID, OO.FECHA_APLICAR, OO.STATUS_OPERACION, OO.MONTO_OPERACION, OO.SERIAL, ");
			sql.append("OO.TASA,OO.IN_COMISION, O.TRANSA_ID, O.CLIENT_ID, O.ORDENE_ID_RELACION,");
			if(tipoProd.equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME)){
				sql.append("(SELECT CTECTA_NUMERO FROM INFI_TB_204_ORDENES ORD WHERE O.ORDENE_ID_RELACION = ORD.ORDENE_ID) CUENTA_CLIENTE_BS,");
			}else{
				sql.append("O.CTECTA_NUMERO CUENTA_CLIENTE_BS,");
			}
			sql.append("CT.CLIENT_NOMBRE, CT.CLIENT_CEDRIF, CT.TIPPER_ID, CT.CLIENT_TELEFONO,CC.CTECTA_NUMERO, CC.CTECTA_NOMBRE, CC.CTECTA_BCOCTA_BCO,");
			sql.append("CC.CTECTA_BCOCTA_DIRECCION, CC.CTECTA_BCOCTA_SWIFT,CC.CTECTA_BCOCTA_BIC, CC.CTECTA_BCOCTA_TELEFONO,CC.CTECTA_BCOCTA_ABA, ");
			sql.append("CC.CTECTA_BCOCTA_PAIS, CC.CTECTA_BCOINT_BCO,CC.CTECTA_BCOINT_DIRECCION, CC.CTECTA_BCOINT_SWIFT,CC.CTECTA_BCOINT_BIC, CC.CTECTA_BCOINT_TELEFONO,CC.NOMBRE_BENEFICIARIO,");
			sql.append("CC.CTECTA_BCOINT_ABA, CC.CTECTA_BCOINT_PAIS,CC.COD_PAIS_ORIGEN, CC.DESC_CIUDAD_ORIGEN, CC.DESC_ESTADO_ORIGEN,CC.ULT_FECHA_ACTUALIZACION, CC.TIPO_INSTRUCCION_ID, CC.CTECTA_OBSERVACION, ");
			sql.append("INS.INSTRUCCION_NOMBRE,");
			sql.append("NVL ((SELECT DTAEXT_VALOR FROM INFI_TB_212_ORDENES_DATAEXT D WHERE D.DTAEXT_ID = '"+DataExtendida.NRO_TICKET+"' AND D.ORDENE_ID = O.ORDENE_ID_RELACION),0) ID_TICKET_CLAVE");
			sql.append(" FROM INFI_TB_207_ORDENES_OPERACION OO,INFI_TB_204_ORDENES O,INFI_TB_202_CTES_CUENTAS CC,INFI_TB_217_CTES_CUENTAS_ORD CCO,INFI_TB_201_CTES CT,INFI_TB_045_TIPO_INSTRUCCION INS");
			sql.append(" WHERE ct.client_id = o.client_id");
			sql.append(" AND ins.tipo_instruccion_id = cc.tipo_instruccion_id");
			sql.append(" AND cc.ctes_cuentas_id = cco.ctes_cuentas_id");
			
			if(tipoProd.equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME)){
				sql.append(" AND cco.ordene_id = O.ORDENE_ID_RELACION ");
			}else{
				sql.append(" AND cco.ordene_id = O.ORDENE_ID");
			}
			
			sql.append(" AND o.ordene_id = oo.ordene_id");
			sql.append(" AND oo.ordene_operacion_id IN (").append(operacionesId).append(")");
			sql.append(" ORDER BY oo.ordene_id");

			//System.out.println("listarSwiftPorIdInstruccion202: "+sql.toString());
			dataSet = db.get(dataSource, sql.toString());
			//System.out.println("listarSwiftPorIdInstruccion202 DATASET: "+dataSet);
			
		}
	/* Actualiza los datos de las instrucciones de pago asociados a las operaciones de un cliente 'NO ENVIADAS' 
	 * @param CuentaCliente datos de la cuenta
	 * @return String query de actualizacion 
	 * */	
	public String actualizarInstruccionesPagoOperaciones(CuentaCliente cuentaCliente){
		StringBuffer sql = new StringBuffer();			
		sql.append("UPDATE INFI_TB_207_ORDENES_OPERACION SET"+
				" CTECTA_NUMERO="+validarCampoNulo(cuentaCliente.getCtecta_numero())+","+
				" CTECTA_NOMBRE="+validarCampoNulo(cuentaCliente.getCtecta_nombre())+","+
				" CTECTA_BCOCTA_BCO="+validarCampoNulo(cuentaCliente.getCtecta_bcocta_bco())+","+
				" CTECTA_BCOCTA_DIRECCION="+validarCampoNulo(cuentaCliente.getCtecta_bcocta_direccion())+","+
				" CTECTA_BCOCTA_SWIFT="+validarCampoNulo(cuentaCliente.getCtecta_bcocta_swift())+","+
				" CTECTA_BCOCTA_BIC="+validarCampoNulo(cuentaCliente.getCtecta_bcocta_bic())+","+
				" CTECTA_BCOCTA_TELEFONO="+validarCampoNulo(cuentaCliente.getCtecta_bcocta_telefono())+","+
				" CTECTA_BCOCTA_ABA="+validarCampoNulo(cuentaCliente.getCtecta_bcocta_aba())+","+
				" CTECTA_BCOCTA_PAIS="+validarCampoNulo(cuentaCliente.getCtecta_bcocta_pais())+","+
				" CTECTA_BCOINT_BCO="+validarCampoNulo(cuentaCliente.getCtecta_bcoint_bco())+","+
				" CTECTA_BCOINT_DIRECCION="+validarCampoNulo(cuentaCliente.getCtecta_bcoint_direccion())+","+
				" CTECTA_BCOINT_SWIFT="+validarCampoNulo(cuentaCliente.getCtecta_bcoint_swift())+","+
				" CTECTA_BCOINT_BIC="+validarCampoNulo(cuentaCliente.getCtecta_bcoint_bic())+","+
				" CTECTA_BCOINT_PAIS="+validarCampoNulo(cuentaCliente.getCtecta_bcoint_pais())+","+
				" CTECTA_BCOINT_ABA="+validarCampoNulo(cuentaCliente.getCtecta_bcoint_aba())+""+				
				" WHERE ORDENE_OPERACION_ID IN ("+
				" select oo.ORDENE_OPERACION_ID"+
				" from INFI_TB_207_ORDENES_OPERACION oo,INFI_TB_204_ORDENES o where "+
				" oo.ORDENE_ID=o.ORDENE_ID and o.CLIENT_ID='"+cuentaCliente.getClient_id()+"'"+
				" and o.TRANSA_ID in ('"+TransaccionNegocio.VENTA_TITULOS+"','"+TransaccionNegocio.CUSTODIA_AMORTIZACION+"','"+TransaccionNegocio.SALIDA_INTERNA+"','"+TransaccionNegocio.SALIDA_EXTERNA+"','"+TransaccionNegocio.PACTO_RECOMPRA+"','"+TransaccionNegocio.PAGO_CUPON+"','"+TransaccionNegocio.ORDEN_PAGO+"')"+
				" and status_operacion!='"+ConstantesGenerales.STATUS_APLICADA+"'"+
				" and oo.MONEDA_ID!='").append(ConstantesGenerales.SIGLAS_MONEDA_LOCAL).append("' ");
		return sql.toString();
		
	}
	
	public String insertarOrdenesOperacionesCLaveNet(OrdenOperacion operacion,boolean IsOrdenPadreDesbloqueo)throws Exception{
		
		StringBuffer sqlOperaciones=new StringBuffer();
		
		long idOperacion=0;
		
		if(!IsOrdenPadreDesbloqueo){
			idOperacion = Long.valueOf(dbGetSequence(this.dataSource, ConstantesGenerales.SECUENCIA_ORDENES_OPERACION)).longValue();
		}
		
		sqlOperaciones.append("INSERT INTO INFI_TB_207_ORDENES_OPERACION (ORDENE_ID,ORDENE_OPERACION_ID,"); 
		sqlOperaciones.append("TRNFIN_ID,STATUS_OPERACION,APLICA_REVERSO,MONTO_OPERACION,");
		sqlOperaciones.append("TASA,FECHA_APLICAR,FECHA_PROCESADA,IN_COMISION,MONEDA_ID,CTECTA_NUMERO,TRNF_TIPO,");
		sqlOperaciones.append("CODIGO_OPERACION,IN_COMISION_INVARIABLE,OPERACION_NOMBRE,NUMERO_RETENCION,ORDENE_RELAC_OPERACION_ID) VALUES (");
		
		sqlOperaciones.append(operacion.getIdOrden()).append(",");				
		
		if(IsOrdenPadreDesbloqueo){
			sqlOperaciones.append(operacion.getIdOperacion()).append(",") ;
		}else {
			sqlOperaciones.append(idOperacion).append(",");
		}		
		sqlOperaciones.append("'").append(operacion.getIdTransaccionFinanciera()).append("',");		
		sqlOperaciones.append("'").append(operacion.getStatusOperacion()).append("',");		
		sqlOperaciones.append(operacion.isAplicaReverso()?ConstantesGenerales.VERDADERO:ConstantesGenerales.FALSO).append(",");		
		sqlOperaciones.append(operacion.getMontoOperacion()).append(",");		
		sqlOperaciones.append(operacion.getTasa()).append(",");		
		sqlOperaciones.append(operacion.getFechaAplicar()==null?formatearFechaBDActual():this.formatearFechaBD(operacion.getFechaAplicar())).append(",");		
		sqlOperaciones.append(operacion.getFechaProcesada()==null?"NULL":formatearFechaBD(operacion.getFechaProcesada())).append(",");		
		sqlOperaciones.append(operacion.getInComision()).append(",");		
		sqlOperaciones.append("'").append(operacion.getIdMoneda()).append("',");		
		sqlOperaciones.append("'").append(operacion.getCuentaOrigen()).append("',");		
		sqlOperaciones.append("'").append(operacion.getTipoTransaccionFinanc()).append("',");				
		sqlOperaciones.append("'").append(operacion.getCodigoOperacion()).append("',");		
		sqlOperaciones.append("'").append(operacion.getIndicadorComisionInvariable()).append("',");		
		sqlOperaciones.append("'").append(operacion.getNombreOperacion()).append("',");	
		sqlOperaciones.append("'").append(operacion.getNumeroRetencion()).append("',");
		sqlOperaciones.append("").append(operacion.getIdOperacionRelacion()).append("");
		sqlOperaciones.append(")");
				
		return sqlOperaciones.toString();
		//db.exec(dataSource,sqlOperaciones.toString());		
	}
	
	/**
	 * Lista la ordenes por id	 
	 * @param fechaDesde puede ser null
	 * @param fechaHasta puede ser null
	 * @param ordeneId puede ser 0
	 * @param statusOperacion
	 * @throws Exception
	 */
	public void listarMensajesSwift(Date fechaDesde, Date fechaHasta,long ordeneId,String statusOperacion, String tipoProducto,String transaId) throws Exception{
		StringBuffer sql = new StringBuffer();	
		SimpleDateFormat formato = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
		sql.append("SELECT  TO_CHAR (FECHA_PAGO_CHEQUE, 'DD/MM/YYYY') AS FECHA_PAGOCH,CT.CLIENT_NOMBRE,ct.CLIENT_CEDRIF, ct.TIPPER_ID,ct.CLIENT_TELEFONO, OO.ORDENE_ID, OO.ORDENE_OPERACION_ID, OO.TRNFIN_ID,"+
				"OO.TRNF_TIPO, OO.CTECTA_NUMERO, OO.CTECTA_NOMBRE,OO.CTECTA_BCOCTA_BCO, OO.CTECTA_BCOCTA_DIRECCION,OO.CTECTA_BCOCTA_SWIFT, OO.CTECTA_BCOCTA_BIC,"+
				"OO.CTECTA_BCOCTA_TELEFONO, OO.CTECTA_BCOCTA_ABA,OO.CTECTA_BCOCTA_PAIS, OO.CTECTA_BCOINT_BCO,OO.CTECTA_BCOINT_DIRECCION, OO.CTECTA_BCOINT_SWIFT,"+
				"OO.CTECTA_BCOINT_BIC, OO.CTECTA_BCOINT_TELEFONO,OO.CTECTA_BCOINT_ABA, OO.CTECTA_BCOINT_PAIS, OO.APLICA_REVERSO,OO.CHEQUE_NUMERO, OO.MONEDA_ID,OO.FECHA_APLICAR, "+
				"OO.STATUS_OPERACION,OO.MONTO_OPERACION, OO.SERIAL, OO.TASA, OO.IN_COMISION,INS.TIPO_INSTRUCCION_ID," +
				"O.ORDENE_ID,O.TRANSA_ID, O.CLIENT_ID, O.ORDENE_ID_RELACION, DEX.DTAEXT_ID,DEX.DTAEXT_VALOR, INS.INSTRUCCION_NOMBRE,O.CTECTA_NUMERO cuenta_cliente_bs,"+
				"nvl((SELECT DTAEXT_VALOR FROM INFI_TB_212_ORDENES_DATAEXT D WHERE D.DTAEXT_ID='NRO_TICKET' AND D.ORDENE_ID=o.ordene_id), 0) id_ticket_clave"+
				" FROM INFI_TB_212_ORDENES_DATAEXT DEX,INFI_TB_207_ORDENES_OPERACION OO,INFI_TB_204_ORDENES O,INFI_TB_201_CTES CT,INFI_TB_045_TIPO_INSTRUCCION INS"+
				" WHERE CT.CLIENT_ID = O.CLIENT_ID AND DEX.ORDENE_ID = O.ORDENE_ID AND OO.ORDENE_ID = O.ORDENE_ID AND DEX.DTAEXT_VALOR = to_char(INS.TIPO_INSTRUCCION_ID)"+
				" AND OO.STATUS_OPERACION = '"+statusOperacion+"' AND OO.MONEDA_ID != '").append(ConstantesGenerales.SIGLAS_MONEDA_LOCAL).append("'"+
				" AND (DEX.DTAEXT_VALOR = '"+TipoInstruccion.CUENTA_SWIFT+"' OR (DEX.DTAEXT_VALOR = '"+TipoInstruccion.CHEQUE+"' AND OO.CHEQUE_NUMERO IS NOT NULL))");
				if(fechaDesde!=null&&fechaHasta!=null){
					sql.append(" AND TRUNC (OO.FECHA_APLICAR) >= TO_DATE ('"+formato.format(fechaDesde)+"', '"+ConstantesGenerales.FORMATO_FECHA+"') AND TRUNC (OO.FECHA_APLICAR) <= TO_DATE ('"+formato.format(fechaHasta)+"', '"+ConstantesGenerales.FORMATO_FECHA+"')");
				}else
					if(fechaHasta!=null)
					{
						sql.append(" AND TRUNC (OO.FECHA_APLICAR) <= TO_DATE ('"+formato.format(fechaHasta)+"', '"+ConstantesGenerales.FORMATO_FECHA+"')");
					}
				if(ordeneId>0){
					sql.append(" AND OO.ORDENE_OPERACION_ID="+ordeneId);
				}
				
				if (transaId!=null) {
					sql.append(" AND O.TRANSA_ID='").append(transaId).append("'");
				}else{
					sql.append(" AND O.TRANSA_ID IN ('"+TransaccionNegocio.VENTA_TITULOS+"','"+TransaccionNegocio.CUSTODIA_AMORTIZACION+"','"+TransaccionNegocio.SALIDA_INTERNA+"','"+TransaccionNegocio.SALIDA_EXTERNA+"','"+TransaccionNegocio.PACTO_RECOMPRA+"','"+TransaccionNegocio.PAGO_CUPON+"','"+TransaccionNegocio.ORDEN_PAGO+"')");
				}					
				if (tipoProducto!=null) {
					sql.append(" AND O.TIPO_PRODUCTO_ID='").append(tipoProducto).append("'");
				}				
				sql.append(" ORDER BY OO.ORDENE_ID");
								
		//System.out.println("listarMensajesSwift nuevo: "+sql.toString());
		dataSet = db.get(dataSource, sql.toString());
		
	}
	
	/**
	 * Lista la ordenes por id	 
	 * @param fechaDesde puede ser null
	 * @param fechaHasta puede ser null
	 * @param ordeneId puede ser 0
	 * @param statusOperacion
	 * @throws Exception
	 */
	public void listarMensajesSwiftInstruccion202(Date fechaDesde, Date fechaHasta,long ordeneId,String statusOperacion, String tipoProducto,String transaId) throws Exception{
		StringBuffer sql = new StringBuffer();		
		SimpleDateFormat formato = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
		sql.append("SELECT OO.ORDENE_ID, OO.ORDENE_OPERACION_ID, OO.TRNFIN_ID, OO.TRNF_TIPO, TO_CHAR (OO.FECHA_PAGO_CHEQUE, 'DD/MM/YYYY') AS FECHA_PAGOCH,");
		sql.append("OO.APLICA_REVERSO, OO.CHEQUE_NUMERO, OO.MONEDA_ID, OO.FECHA_APLICAR, OO.STATUS_OPERACION, OO.MONTO_OPERACION, OO.SERIAL, ");
		sql.append("OO.TASA,OO.IN_COMISION, O.TRANSA_ID, O.CLIENT_ID, O.ORDENE_ID_RELACION,");
		
		if(tipoProducto.equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME)){
			sql.append("(SELECT CTECTA_NUMERO FROM INFI_TB_204_ORDENES ORD WHERE O.ORDENE_ID_RELACION = ORD.ORDENE_ID) CUENTA_CLIENTE_BS,");
		}else{
			sql.append("O.CTECTA_NUMERO CUENTA_CLIENTE_BS,");
		}
		
		sql.append("CT.CLIENT_NOMBRE, CT.CLIENT_CEDRIF, CT.TIPPER_ID, CT.CLIENT_TELEFONO,CC.CTECTA_NUMERO, CC.CTECTA_NOMBRE, CC.CTECTA_BCOCTA_BCO,");
		sql.append("CC.CTECTA_BCOCTA_DIRECCION, CC.CTECTA_BCOCTA_SWIFT,CC.CTECTA_BCOCTA_BIC, CC.CTECTA_BCOCTA_TELEFONO,CC.CTECTA_BCOCTA_ABA, ");
		sql.append("CC.CTECTA_BCOCTA_PAIS, CC.CTECTA_BCOINT_BCO,CC.CTECTA_BCOINT_DIRECCION, CC.CTECTA_BCOINT_SWIFT,CC.CTECTA_BCOINT_BIC, CC.CTECTA_BCOINT_TELEFONO,CC.NOMBRE_BENEFICIARIO,");
		sql.append("CC.CTECTA_BCOINT_ABA, CC.CTECTA_BCOINT_PAIS,CC.COD_PAIS_ORIGEN, CC.DESC_CIUDAD_ORIGEN, CC.DESC_ESTADO_ORIGEN,CC.ULT_FECHA_ACTUALIZACION, CC.TIPO_INSTRUCCION_ID, CC.CTECTA_OBSERVACION,");
		sql.append("INS.INSTRUCCION_NOMBRE,");
		sql.append("NVL ((SELECT DTAEXT_VALOR FROM INFI_TB_212_ORDENES_DATAEXT D WHERE D.DTAEXT_ID = '"+DataExtendida.NRO_TICKET+"' AND D.ORDENE_ID = O.ORDENE_ID),0) ID_TICKET_CLAVE");
		sql.append(" FROM INFI_TB_207_ORDENES_OPERACION OO,INFI_TB_204_ORDENES O,INFI_TB_202_CTES_CUENTAS CC,INFI_TB_217_CTES_CUENTAS_ORD CCO,INFI_TB_201_CTES CT,INFI_TB_045_TIPO_INSTRUCCION INS");
		sql.append("  WHERE CT.CLIENT_ID = O.CLIENT_ID AND INS.TIPO_INSTRUCCION_ID=CC.TIPO_INSTRUCCION_ID AND OO.ORDENE_ID = O.ordene_id ");		
		sql.append("  AND OO.STATUS_OPERACION = '"+statusOperacion+"' AND OO.MONEDA_ID != '").append(ConstantesGenerales.SIGLAS_MONEDA_LOCAL).append("'");
		sql.append("  AND cc.ctes_cuentas_id = cco.ctes_cuentas_id ");
		
		if(tipoProducto.equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SITME)){
			sql.append(" AND cco.ordene_id = O.ORDENE_ID_RELACION ");
		}else{
			sql.append(" AND cco.ordene_id = O.ORDENE_ID");
		}
			
		sql.append("  AND (cc.TIPO_INSTRUCCION_ID = "+TipoInstruccion.CUENTA_SWIFT+" OR (cc.TIPO_INSTRUCCION_ID = "+TipoInstruccion.CHEQUE+" AND OO.CHEQUE_NUMERO IS NOT NULL))");
				if(fechaDesde!=null&&fechaHasta!=null){
					sql.append(" AND TRUNC (OO.FECHA_APLICAR) >= TO_DATE ('"+formato.format(fechaDesde)+"', '"+ConstantesGenerales.FORMATO_FECHA+"') AND TRUNC (OO.FECHA_APLICAR) <= TO_DATE ('"+formato.format(fechaHasta)+"', '"+ConstantesGenerales.FORMATO_FECHA+"')");
				}else
					if(fechaHasta!=null)
					{
						sql.append(" AND TRUNC (OO.FECHA_APLICAR) <= TO_DATE ('"+formato.format(fechaHasta)+"', '"+ConstantesGenerales.FORMATO_FECHA+"')");
					}
				if(ordeneId>0){
					sql.append(" AND O.ORDENE_ID="+ordeneId);
				}
				if (transaId!=null) {
					sql.append(" AND O.TRANSA_ID='").append(transaId).append("'");
				}else
				{
					sql.append("  AND O.TRANSA_ID IN ('"+TransaccionNegocio.VENTA_TITULOS+"','"+TransaccionNegocio.CUSTODIA_AMORTIZACION+"','"+TransaccionNegocio.SALIDA_INTERNA+"','"+TransaccionNegocio.SALIDA_EXTERNA+"','"+TransaccionNegocio.PACTO_RECOMPRA+"','"+TransaccionNegocio.PAGO_CUPON+"','"+TransaccionNegocio.ORDEN_PAGO+"')");
				}
				if (tipoProducto!=null) {
					sql.append(" AND O.TIPO_PRODUCTO_ID='").append(tipoProducto).append("'");
				}				
				
				sql.append(" AND NOT EXISTS (SELECT OP.ORDENE_ID FROM infi_tb_207_ordenes_operacion op WHERE op.ordene_id = o.ordene_id_relacion ");
				sql.append(" AND op.trnf_tipo ='").append(TransaccionFinanciera.DEBITO).append("'").append("  AND op.status_operacion IN ('").append(ConstantesGenerales.STATUS_EN_ESPERA).append("','").append(ConstantesGenerales.STATUS_RECHAZADA).append("')) ");
				
				sql.append(" ORDER BY oo.ordene_id");
								
		//System.out.println("listarMensajesSwiftInstruccion202: "+sql.toString());
		dataSet = db.get(dataSource, sql.toString());
		
	}
	
	public String actualizarEstatusOperacionesIn(String idsOperaciones,String estatusOp){
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE INFI_TB_207_ORDENES_OPERACION SET STATUS_OPERACION='"+estatusOp+"' WHERE ORDENE_OPERACION_ID IN ("+idsOperaciones+")");		
		return sql.toString();		
		
	}
	//ITS_2117 NM26659_31/07/2014 Modificacion para resolucion inc abonos cta dolares
	public boolean operacionesNoAplicadasOrden(String idOrden) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT COUNT(*) NUM FROM INFI_TB_207_ORDENES_OPERACION OP WHERE OP.STATUS_OPERACION !='"+ConstantesGenerales.STATUS_APLICADA+"' AND OP.ORDENE_ID= "+idOrden+" AND OP.TRNF_TIPO NOT IN ('").append(TransaccionFinanciera.BLOQUEO).append("','").append(TransaccionFinanciera.DESBLOQUEO).append("') ");		
			
		dataSet = db.get(dataSource, sql.toString());
		
		if(dataSet.count()>0 &&dataSet.next()){			
			if(Long.parseLong(dataSet.getValue("NUM"))>0){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Metodo para actualizar especificamente operaciones de bloqueo
	 * @author nm25287
	 * @param idOrden
	 * @param estatusOp
	 * @return
	 */
	public String actualizarEstatusOperacionesBloqueo(String idOrden,String estatusOp){
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE INFI_TB_207_ORDENES_OPERACION SET STATUS_OPERACION='"+estatusOp+"' WHERE ORDENE_ID = "+idOrden+" AND TRNF_TIPO IN ('BLO','DES')");		
		return sql.toString();		
		
	}
	
}

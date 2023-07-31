package com.bdv.infi.dao;

import javax.sql.DataSource;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.DataExtendida;
import com.bdv.infi.logic.interfaces.TipoInstruccion;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import megasoft.db;

/**
 * DAO para el programador de Tareas
 */
public class QuartzSchedulerDAO extends GenericoDAO{
/**
 * Constructor
 * @param ds
 */
	public QuartzSchedulerDAO(DataSource ds) {
		super(ds);
	}
	@Override
	public Object moveNext() throws Exception {
		return null;
	}
/**
 * Lista las tareas activas a procesar por el SCHEDULER
 * @throws Exception
 */
	public void listarTareasActivasScheduler()throws Exception{				
		String sql = "select * from infi_tb_quarz_scheduler where estado=1";		
		dataSet = db.get(dataSource, sql);	}
/**
 * Lista todas las tareas que pueden ser programadas para correr automaticamente
 * @throws Exception
 */	
	public void listarTareasScheduler(long idTarea, String userId)throws Exception{
		StringBuilder sb = new StringBuilder("select * from INFI_TB_QUARZ_SCHEDULER where job_name in " +
		"(SELECT a.url FROM MSC_ACTIONS a, MSC_ROLES_ACTIONS ra, MSC_ROLE_USER ru " + 
		" WHERE ra.msc_role_id = ru.msc_role_id AND ra.id_action = a.id_action " +
		" AND a.ID_APPLICATION = (select id_application from msc_applications " +
		" where siglas_applic = 'INFI') AND ru.msc_user_id = '" + userId + "')");		
		if(idTarea!=0){
			sb.append("and id=" + idTarea);
		}
		sb.append(" order by id asc");
		//System.out.println("SQL INFI_TB_QUARZ_SCHEDULER: "+sb.toString());
		dataSet = db.get(dataSource,sb.toString());
	}
	/**
	 * Lista las operaciones con los siguientes status:
	 * <li>En Espera o rechazada</li>
	 * <li>Moneda Local</li>
	 * <li>Pago cupon</li>
	 * <li>Amortizacion</li>
	 * <li>Comisiones</li>
	 * <li>Fecha Valor Igual a sysdate</li>
	 * @throws Exception
	 */
	public void listarOrdenesOperacionAltair()throws Exception{
		StringBuffer sb 	= new StringBuffer();

		sb.append("select infi_tb_204_ordenes.ordene_id,infi_tb_207_ordenes_operacion.aplica_reverso,infi_tb_207_ordenes_operacion.fecha_aplicar,infi_tb_207_ordenes_operacion.ordene_operacion_id,infi_tb_207_ordenes_operacion.titulo_id,infi_tb_207_ordenes_operacion.trnfin_id,infi_tb_207_ordenes_operacion.in_comision,infi_tb_207_ordenes_operacion.monto_operacion,infi_tb_207_ordenes_operacion.serial,infi_tb_207_ordenes_operacion.status_operacion,infi_tb_207_ordenes_operacion.tasa,infi_tb_207_ordenes_operacion.trnf_tipo,infi_tb_207_ordenes_operacion.ctecta_numero as cuenta,infi_tb_207_ordenes_operacion.codigo_operacion,infi_tb_207_ordenes_operacion.ctecta_numero as cuenta,infi_tb_207_ordenes_operacion.moneda_id as moneda from infi_tb_204_ordenes left join infi_tb_207_ordenes_operacion on infi_tb_204_ordenes.ORDENE_ID = infi_tb_207_ordenes_operacion.ORDENE_ID left join INFI_TB_212_ORDENES_DATAEXT on infi_tb_204_ordenes.ordene_id=INFI_TB_212_ORDENES_DATAEXT.ordene_id where infi_tb_207_ordenes_operacion.MONEDA_ID='").append(ConstantesGenerales.SIGLAS_MONEDA_LOCAL).append("' and (infi_tb_207_ordenes_operacion.STATUS_OPERACION='").append(ConstantesGenerales.STATUS_EN_ESPERA);
		sb.append("' OR infi_tb_207_ordenes_operacion.STATUS_OPERACION='").append(ConstantesGenerales.STATUS_RECHAZADA).append("') ");
		sb.append("and to_date(infi_tb_204_ordenes.ORDENE_PED_FE_VALOR,'").append(ConstantesGenerales.FORMATO_FECHA);
		sb.append("')<=to_date(sysdate,'").append(ConstantesGenerales.FORMATO_FECHA).append("') and transa_id in ('");
		sb.append(TransaccionNegocio.ORDEN_PAGO).append("','");
		sb.append(TransaccionNegocio.CUSTODIA_AMORTIZACION).append("','");
		sb.append(TransaccionNegocio.VENTA_TITULOS).append("','");
		sb.append(TransaccionNegocio.PAGO_CUPON).append("','");
		sb.append(TransaccionNegocio.CUSTODIA_COMISIONES).append("') and infi_tb_207_ordenes_operacion.ctecta_numero is not null ");
		sb.append("and INFI_TB_212_ORDENES_DATAEXT.dtaext_id='");
		sb.append(DataExtendida.TIPO_INSTRUCCION_PAGO);
		sb.append("' and INFI_TB_212_ORDENES_DATAEXT.dtaext_valor<>'");
		sb.append(TipoInstruccion.OPERACION_DE_CAMBIO).append("'");

		dataSet = db.get(dataSource, sb.toString());
		
	}//FIN listarOrdenesOperacionAltair
	
	/**
	 * Actualiza el proceso (Tarea Scheduler) en base de datos
	 * @param id
	 * @param cronExpression
	 * @param status
	 * @throws Exception
	 */
	public void updateProcesoScheduler(Long id,String cronExpression,String status,String valueScreen)throws Exception{
		StringBuffer sb 	= new StringBuffer();
		sb.append("update INFI_TB_QUARZ_SCHEDULER set cron_expression='").append(cronExpression);
		sb.append("',estado=").append(status).append(",value_screen='").append(valueScreen).append("'");
		sb.append(" where id=").append(id);
		//System.out.println("SQL update INFI_TB_QUARZ_SCHEDULER: "+sb.toString());
		db.exec(dataSource, sb.toString());
		
	}//FIN listarOrdenesOperacionAltair
}

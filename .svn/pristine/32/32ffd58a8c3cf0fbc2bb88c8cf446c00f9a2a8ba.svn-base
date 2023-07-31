package com.bdv.infi.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;

import javax.sql.DataSource;

import com.bancovenezuela.comun.data.OrdenOperacion;
import com.bdv.infi.data.OperacionIntento;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.util.DBO;

public class IntentoOperacionDAO extends GenericoDAO {

	public IntentoOperacionDAO(DataSource ds) {
		super(ds);
		// TODO Auto-generated constructor stub
	}

	public IntentoOperacionDAO(Transaccion transanccion) throws Exception {
		super(transanccion);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Genera el SQL para registrar un intento en la base de datos
	 * @return String con el SQL de insert del Intento de ejecucion
	 */
	public String insertar(OperacionIntento operacionIntento) throws Exception {
		
		//Busca el siguiente id de operación
		String idOperacionIntento = dbGetSequence(this.dataSource, ConstantesGenerales.SECUENCIA_OPERACION_INTENTO);
		operacionIntento.setIdOperacionIntento(Long.parseLong(idOperacionIntento));
		
		StringBuffer sqlInsert = new StringBuffer();
		sqlInsert.append("INSERT INTO INFI_TB_209_ORDENES_OPERAC_INT (");
		sqlInsert.append("ordene_id, ordene_operacion_id, operacion_intento_id, fecha, aplico, error_desc");
		sqlInsert.append(") VALUES (").append(operacionIntento.getIdOrden()).append(",");
		sqlInsert.append(operacionIntento.getIdOperacion()).append(",");
		sqlInsert.append(operacionIntento.getIdOperacionIntento()).append(",");
		sqlInsert.append("sysdate,");
		sqlInsert.append("'").append(operacionIntento.getIndicadorAplicado()).append("',");
		sqlInsert.append("'").append(operacionIntento.getTextoError()).append("')");
		
		return sqlInsert.toString();
	}
	
	public String getSqlInsertIntento() {
		StringBuffer sqlInsert = new StringBuffer();
		sqlInsert.append("INSERT INTO INFI_TB_209_ORDENES_OPERAC_INT (");
		sqlInsert.append("ordene_id, ordene_operacion_id, operacion_intento_id, fecha, aplico");
		sqlInsert.append(") VALUES (?, ?, ?, ?, ?)");
		return sqlInsert.toString();
	}
	
	/**
	 * Genera el SQL para actualizar la respuesta de un intento en la base de datos
	 * @return String con el SQL de update del Intento de ejecucion
	 */
	public String getSqlUpdateIntento() {
		StringBuffer sqlUpdate = new StringBuffer();
		sqlUpdate.append("UPDATE INFI_TB_209_ORDENES_OPERAC_INT SET ");
		sqlUpdate.append("aplico = ? , error_desc = ? ");
		sqlUpdate.append("where ordene_operacion_id = ? and operacion_intento_id = ?");
		return sqlUpdate.toString();
	}	
	
	public com.bdv.infi.logic.interfaz_swift.OperacionIntento armarBean(OrdenOperacion beanOOperacion, OrdenOperacion beanOperacion) throws Throwable {
		
		
		com.bdv.infi.logic.interfaz_swift.OperacionIntento beanOIntento = new com.bdv.infi.logic.interfaz_swift.OperacionIntento();
		if (beanOOperacion != null) {
			beanOIntento.setIdOrden(beanOOperacion.getIdOrden());
			beanOIntento.setIdOperacion(beanOOperacion.getIdOperacion());
			beanOIntento.setDebitoCredito(beanOOperacion.getTipoTransaccionFinanc());
		} else {
			BigDecimal bdAux = new BigDecimal(beanOperacion.getIdOrden());
			beanOIntento.setIdOrden(bdAux.longValue());
			beanOIntento.setIdOperacion(beanOperacion.getIdOperacion());
			beanOIntento.setDebitoCredito(beanOperacion.getTipoTransaccionFinanc());
		}
		
		StringBuffer sqlSB = new StringBuffer();
		sqlSB.append("select max(operacion_intento_id) as operacion_intento_id ");
		sqlSB.append("from infi_tb_209_ordenes_operac_int ");
		sqlSB.append("where ordene_operacion_id = ? ");
		
		Object[] dataCriterios  =  new Object [1];
		dataCriterios [0] = new Long(beanOIntento.getIdOperacion());
		
		ResultSet rs = DBO.executeQuery(null, this.dataSource, sqlSB.toString(), dataCriterios);
		if (!rs.next()) {
			throw new Exception("BDMSG0000");
		}
		if (rs.getInt("operacion_intento_id") == 0) {
			beanOIntento.setIdOperacionIntento(0);
		} else {
			beanOIntento.setIdOperacionIntento(rs.getInt("operacion_intento_id"));
		}
		
		return beanOIntento;
	}	

}



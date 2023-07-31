package com.bdv.infi.dao;
import static com.bancovenezuela.comun.logic.interfaces.ConstantesGenerales.TRANSACCION_TOMA_ORDEN;

import javax.sql.DataSource;

import megasoft.db;

import com.bdv.infi.data.FormatoConciliacionRetencion;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.StatusOrden;
public class ArchivoRetencionDAO extends com.bdv.infi.dao.GenericoDAO {
		
	public ArchivoRetencionDAO(DataSource ds) {
		super(ds);
	}
		
	public String insertarRetencion(FormatoConciliacionRetencion registroRetencion) throws Exception{
		StringBuffer sb = new StringBuffer();
		
		sb.append("INSERT INTO INFI_TB_908_ARCHIVO_RETENCION (ID_REGISTRO, ID_CICLO, FECHA_PROCESO, CODIGO_OPERACION, NUMERO_RETENCION, CUENTA, FECHA_RETENCION, MONTO, PROCESADO, ESTADO_RETENCION) VALUES (");		
		sb.append("INFI_SQ_908.NEXTVAL,");
		
		sb.append("'").append(registroRetencion.getIdCiclo()).append("',");		
		sb.append("SYSDATE,");
		sb.append("'").append(registroRetencion.getCodigoOperacion()).append("',");
		sb.append("'").append(registroRetencion.getCodigoRetencion()).append("',");
		sb.append("'").append(registroRetencion.getCuenta()).append("',");
		sb.append("TO_DATE('").append(registroRetencion.getFechaRetencion()).append("','").append(ConstantesGenerales.FORMATO_FECHA3).append("'),");
		sb.append("").append(registroRetencion.getMontoBigDArchivo()).append(",");
		sb.append("1,");
		sb.append(registroRetencion.getEstado()).append(")");
				
		return sb.toString();
	}
	/**	 
	 * Metodo que consulta las operaciones que fueron registradas en INFI que no poseen códigos de bloqueo
	 * @author nm25287. Modificado: nm25287, se incluyó el valor ordsta_id en la consulta.
	 * @param fechaInicio
	 * @param fechaFin
	 * @param unidadInv
	 * @param tipoProducto
	 * @param count
	 * @throws Exception
	 */
	public void obtenerOperacionesSinRetencion(String fechaInicio, String fechaFin,long unidadInv, String tipoProducto, boolean count) throws Exception{
		StringBuffer sb = new StringBuffer();
		if(count){
			sb.append("SELECT COUNT(*) ");
		}else{
			sb.append("SELECT OP.ORDENE_ID, OP.ORDENE_OPERACION_ID, OP.CODIGO_OPERACION, OP.CTECTA_NUMERO, OP.MONTO_OPERACION,ord.ordene_ped_fe_orden, ctes.client_nombre, ORD.ORDSTA_ID ");
		}		
		
		sb.append(" FROM INFI_TB_201_CTES CTES, INFI_TB_204_ORDENES ORD, INFI_TB_207_ORDENES_OPERACION OP ");
		sb.append(" WHERE ctes.client_id = ord.client_id AND ORD.ORDENE_ID= OP.ORDENE_ID");
		sb.append(" AND OP.NUMERO_RETENCION IS NULL");
		sb.append(" AND OP.TRNF_TIPO = 'BLO'");
		//sb.append(" AND OP.STATUS_OPERACION IN ('").append(STATUS_EN_ESPERA).append("','").append(STATUS_RECHAZADA).append("')");
		
		if(fechaInicio!=null && !fechaInicio.equals("") && fechaFin!=null && !fechaFin.equals("")){
			sb.append(" AND ORD.ORDENE_PED_FE_ORDEN BETWEEN TO_DATE('").append(fechaInicio).append("','yyyy-MM-dd') AND TO_DATE('").append(fechaFin).append("','yyyy-MM-dd') ");
		}
		sb.append(" AND ORD.TRANSA_ID = '").append(TRANSACCION_TOMA_ORDEN).append("' ");
		sb.append(" AND ORD.ORDSTA_ID NOT IN ('").append(StatusOrden.PENDIENTE).append("','").append(StatusOrden.CANCELADA).append("','").append(StatusOrden.REGISTRADA).append("') ");
		
		if(tipoProducto!=null && !tipoProducto.equals("")){		
			sb.append(" AND ORD.TIPO_PRODUCTO_ID = '").append(tipoProducto).append("'");
		}
		
		sb.append(" AND OP.STATUS_OPERACION <> '").append(ConstantesGenerales.STATUS_APLICADA).append("' ");
		
		if(unidadInv>0){
			sb.append(" AND ORD.uniinv_id=").append(unidadInv).append(" ");
		}		
		
		System.out.println("obtenerOperacionesSinRetencion ----> " + sb.toString());
		dataSet = db.get(dataSource, sb.toString());
	}
	
	public String actualizarRetencionOperacion(String idOrden, String codRetencion) throws Exception{
		StringBuffer sb = new StringBuffer();
		
		sb.append("UPDATE INFI_TB_207_ORDENES_OPERACION OP SET NUMERO_RETENCION='").append(codRetencion).append("' WHERE OP.ORDENE_ID=").append(idOrden).append(" AND OP.TRNF_TIPO IN ('BLO','DES')");		
						
		System.out.println("actualizarRetencionOperacion: "+sb.toString());
		return sb.toString();
	}
	
	public void obtenerRetencionOperacion(String cuenta, String codigoOperacion, String monto,boolean truncar) throws Exception{
		StringBuffer sb = new StringBuffer();
		
		sb.append("SELECT NUMERO_RETENCION,ESTADO_RETENCION FROM INFI_TB_908_ARCHIVO_RETENCION AR WHERE AR.CUENTA='").append(cuenta).append("' AND AR.CODIGO_OPERACION= '").append(codigoOperacion);
		if (truncar){
			sb.append("'  AND TRUNC(AR.MONTO,1) = TRUNC(").append(monto).append(",1)");
		}else{
			sb.append("'  AND AR.MONTO =").append(monto);
		}						
		System.out.println("obtenerRetencionOperacion: "+sb.toString());
		dataSet = db.get(dataSource, sb.toString());
	}
		
	public void borrarTablaRetencion() throws Exception{
		StringBuffer sb = new StringBuffer();
		
		sb.append("DELETE FROM INFI_TB_908_ARCHIVO_RETENCION ");		
		
		System.out.println("borrarTablaRetencion: "+sb.toString());
		db.exec(dataSource, sb.toString());
	}
	
	public String aplicarOperacionDesbloqueo(String idOrden) throws Exception{
		StringBuffer sb = new StringBuffer();
		
		sb.append("UPDATE INFI_TB_207_ORDENES_OPERACION OP SET STATUS_OPERACION='").append(ConstantesGenerales.STATUS_APLICADA).append("' WHERE OP.ORDENE_ID=").append(idOrden).append(" AND OP.TRNF_TIPO ='DES'");		
						
		System.out.println("aplicarOperacionDesbloqueo: "+sb.toString());
		return sb.toString();
	}
	
	@Override
	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
			
}   

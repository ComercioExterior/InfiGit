package com.bdv.infi.dao;
import javax.sql.DataSource;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.data.Auditoria;
public class AuditoriaDAO extends com.bdv.infi.dao.GenericoDAO {
		
	public AuditoriaDAO(DataSource ds) {
		super(ds);
	}
	
	
	public String insertRegistroAuditoria(Auditoria auditoria) throws Exception{
		StringBuffer sb = new StringBuffer();
		
		sb.append("INSERT INTO INFI_TB_907_AUDITORIA (ID_AUDITORIA,DIRECCION_IP,FECHA,USUARIO,PETICION,DETALLE) VALUES (");		
		sb.append("INFI_SQ_907.NEXTVAL,");
		
		sb.append("'").append(auditoria.getDireccionIp()).append("',");		
		sb.append("TO_DATE('").append(auditoria.getFechaAuditoria()).append("','").append(ConstantesGenerales.FORMATO_FECHA_RRRR).append("'),");
		sb.append("'").append(auditoria.getUsuario()).append("',");
		sb.append("'").append(auditoria.getPeticion()).append("',");
		sb.append("'").append(auditoria.getDetalle()).append("')");
				
		return sb.toString();
	}
	
	@Override
	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
			
}   

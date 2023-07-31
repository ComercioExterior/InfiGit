package com.bdv.infi.dao;

import javax.sql.DataSource;
import megasoft.db;
import com.bdv.infi.data.Beneficiario;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;


/** 
 * Clase que se conecta con la base de datos y busca los paises)
 */
public class BeneficiarioDAO extends com.bdv.infi.dao.GenericoDAO {

	
	public BeneficiarioDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
		// TODO Auto-generated constructor stub
	}
	
	public BeneficiarioDAO(DataSource ds) throws Exception {
		super(ds);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Lista todos los beneficiarios existentes
	 * @throws Exception
	 */
	public void listar() throws Exception{
		String sql = "SELECT * FROM INFI_TB_039_BENEFICIARIOS order by beneficiario_nombre";
		
		dataSet = db.get(dataSource, sql);

	}

	/**
	 * Lista los beneficiarios que coincidan con los par&aacute;metros enviados
	 * @param nombreBeneficiario
	 * @param descripcionBeneficiario
	 * @throws Exception
	 */
	public void listarTodos(String nombreBeneficiario, String descripcionBeneficiario) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		sql.append("SELECT * FROM INFI_TB_039_BENEFICIARIOS WHERE 1=1");
		
		if((nombreBeneficiario!=null)){
			filtro.append(" AND UPPER(BENEFICIARIO_NOMBRE) LIKE UPPER('%").append(nombreBeneficiario).append("%')");			
		}
		if((descripcionBeneficiario!=null)){
			filtro.append(" AND UPPER(BENEFICIARIO_DESC) LIKE UPPER('%").append(descripcionBeneficiario).append("%')");			
		}
		sql.append(filtro);
		sql.append("ORDER BY BENEFICIARIO_ID");
		dataSet = db.get(dataSource, sql.toString());
	}
	
	
	public void listar(long idBeneficiario) throws Exception{

		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM INFI_TB_039_BENEFICIARIOS WHERE BENEFICIARIO_ID =").append(idBeneficiario);
		dataSet = db.get(dataSource, sb.toString());		
				
	}
	
	public String insertar(Beneficiario beneficiario) throws Exception  {
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
				
		sql.append("INSERT INTO INFI_TB_039_BENEFICIARIOS ( BENEFICIARIO_ID, BENEFICIARIO_NOMBRE, BENEFICIARIO_DESC) VALUES (");
		String idBeneficiario = dbGetSequence(dataSource, ConstantesGenerales.SECUENCIA_BENEFICIARIO);		
				
		filtro.append("'").append(idBeneficiario).append("',");	
		filtro.append("'").append(beneficiario.getNombreBeneficiario().toUpperCase()).append("',");	
		filtro.append("'").append(beneficiario.getDescripcionBeneficiario().toUpperCase()).append("')");

		sql.append(filtro);		
		return(sql.toString());
	}

	public String modificar(Beneficiario beneficiario) throws Exception {
		
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
	    	
		
		sql.append("UPDATE INFI_TB_039_BENEFICIARIOS SET ");		
		filtro.append(" BENEFICIARIO_NOMBRE ='").append(beneficiario.getNombreBeneficiario().toUpperCase()).append("',");
		filtro.append(" BENEFICIARIO_DESC = '").append(beneficiario.getDescripcionBeneficiario().toUpperCase()).append("'");
		filtro.append(" WHERE BENEFICIARIO_ID =").append(beneficiario.getIdBeneficiario());
		sql.append(filtro);			
		return(sql.toString());				

	}

	public String eliminar(long idBeneficiario) throws Exception {
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		
		sql.append("DELETE FROM INFI_TB_039_BENEFICIARIOS WHERE");
		
		filtro.append(" beneficiario_id='").append(idBeneficiario).append("'");
		sql.append(filtro);			
		return(sql.toString());
	}

	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}	
	
}

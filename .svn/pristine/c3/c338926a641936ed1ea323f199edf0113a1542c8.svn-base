package com.bdv.infi.dao;

import javax.sql.DataSource;
import megasoft.db;
import com.bdv.infi.data.Contratado;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

/** 
 * Lista de contratados del Banco de Venezuela
 */
public class ContratadosDAO extends com.bdv.infi.dao.GenericoDAO {

	public ContratadosDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
		// TODO Auto-generated constructor stub
	}
	
	public ContratadosDAO(DataSource ds) throws Exception {
		super(ds);
	}

	public int modificar(){
		return 0;
	}
	
	public int insertar(){
		return 0;
	}

	/**Verifica si un cliente es contratado del Banco de Venezuela
	 * @param cedRif cedula o rif del cliente que se desea buscar
	 * @return verdadero en caso que el cliente sea contratado del Banco de Venezuela*/
	public boolean verificarCliente(long cedRif){
		return false;
	}
	

	public void listarContratados(String cedula, String nombre) throws Exception{
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		sql.append("select * from ps_rrhh_view where 1=1");
		
		if(cedula!=null){
			filtro.append(" and EMPLID like ('%").append(cedula).append("%')");
		}
		
		if(nombre!=null){
			filtro.append(" and NAME like upper('%").append(nombre).append("%')");
		}
		sql.append(filtro);
		sql.append(" order by NAME ASC");
		dataSet = db.get(dataSource, sql.toString());
	}

	/**
	 * Verifica si una persona determinada es empleado del banco
	 * @param cedula
	 * @param tipoPersona
	 * @param nombre
	 * @return true si es empleado, false en caso contrario
	 * @throws Exception
	 */
	public boolean esContratado(String cedula) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
		sql.append("select * from ps_rrhh_view ");
		
		if(cedula!=null){
			filtro.append(" where EMPLID = '").append(cedula).append("'");
		}
		
		sql.append(filtro);		
		dataSet = db.get(dataSource, sql.toString());
		
		if(dataSet.next()){
			return true;
		}else{			
			return false;
		}
	}


	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}

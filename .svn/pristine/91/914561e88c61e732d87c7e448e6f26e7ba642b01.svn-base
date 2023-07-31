package com.bdv.infi_toma_orden.dao;

import javax.sql.DataSource;

import com.bdv.infi.util.DB;
import com.bdv.infi_toma_orden.data.Cliente;


/**
 * Clase destinado para el manejo de los registros en la tabla INFI_TB_201_CTES
 */
public class ClienteDAO extends GenericoDAO {
	
	/**
	 * Objeto Cliente
	 */
	private Cliente cliente;
	
	/**
	 * Retorna el objeto Cliente con la informacion recuperada 
	 * @return resultSet
	 */
	public Cliente getCliente() {
		return cliente;
	}

	/**
	 * Constructor de la clase
	 * @param nombreDataSource : nombre que se obtiene del ambiente de ejecucion de los WebService
	 * @param dso : DataSource instanciado por clases que se ejecutan en ambientes Web
	 */
	public ClienteDAO (String nombreDataSource, DataSource dso) {
		super(nombreDataSource, dso);
	}
	
	/**
	 * Busca un cliente por su id
	 * 
	 * @param idCliente : Identificador del Cliente
	 * @return true  : si el Cliente existe  <br>
	 *         false : si no existe 
	 * @throws Exception
	 *             lanza una exception si el id del cliente no es encontrado
	 */
	//Modificado NM29643
	public void listarPorCedula(String tipoPersona, String cedulaCliente) throws Exception {

		cliente = new Cliente();
        
		StringBuffer sql = new StringBuffer();
		/*sql.append("select cli.* ");
		sql.append("from INFI_TB_201_CTES cli ");
		sql.append("inner join INFI_TB_202_CTES_CUENTAS clicta on clicta.client_id = cli.client_id ");
		sql.append("where cli.client_cedrif = ? and tipper_id  = ? ");*/
		
		sql.append("select cli.* ");
		sql.append("from INFI_TB_201_CTES cli ");
		sql.append("where cli.client_cedrif = ? and tipper_id  = ? ");

		Object [] objAux = {cedulaCliente, tipoPersona};
		
		try {
			resultSet = DB.executeQuery(dso, sql.toString(), objAux);
			
			/*if (!resultSet.next()) {
				throw new Exception("MSGTO8001");
			}*/
			if (resultSet.next()) {
				cliente.setIdCliente(resultSet.getLong("CLIENT_ID"));
	            cliente.setCuentaCustodia(resultSet.getLong("CLIENT_CTA_CUSTOD_ID"));                
	            // cliente.setFechaCuentaCustodia(resultSet.getDate("CLIENT_CTA_CUSTOD_FECHA"));                
	            cliente.setNombre(resultSet.getString("CLIENT_NOMBRE"));                
	            cliente.setRifCedula(resultSet.getLong("CLIENT_CEDRIF"));                
	            cliente.setTipoPersona(resultSet.getString("TIPPER_ID")); 	
			}else{
				cliente = null;
			}
		} catch (Exception e) {
			if (e.getMessage().indexOf("MSGTO8001") > -1) {
				throw new Exception(e);
			} else {
				e.printStackTrace();
				throw new Exception("Error buscando los datos del cliente.");
			}
		} finally {
			if (resultSet != null)
				resultSet.close();
		}
		return;
	}	

	/**
	 * Busca un cliente por su id
	 * 
	 * @param idCliente : Identificador del Cliente
	 * @return true  : si el Cliente existe  <br>
	 *         false : si no existe 
	 * @throws Exception
	 *             lanza una exception si el id del cliente no es encontrado
	 */
	public void listarPorId(long idCliente) throws Exception {
		cliente = new Cliente();
		StringBuffer sql = new StringBuffer();
		sql.append("select CLIENT_ID,CLIENT_CTA_CUSTOD_ID,UPPER(CLIENT_NOMBRE) AS CLIENT_NOMBRE,CLIENT_CEDRIF,TIPPER_ID,client_telefono,client_direccion from INFI_TB_201_CTES where client_id = ?");
		Object [] objAux = {idCliente}; 
		//System.out.println("SQL CLIENT: "+sql+" "+idCliente);
		try {
			resultSet = DB.executeQuery(dso, sql.toString(), objAux);	
			if (!resultSet.next()) {
				throw new Exception("MSGTO8001");
			}
			cliente.setIdCliente(resultSet.getLong("CLIENT_ID"));
            cliente.setCuentaCustodia(resultSet.getLong("CLIENT_CTA_CUSTOD_ID"));                
            //cliente.setFechaCuentaCustodia(resultSet.getDate("CLIENT_CTA_CUSTOD_FECHA"));                
            cliente.setNombre(resultSet.getString("CLIENT_NOMBRE"));                
            cliente.setRifCedula(resultSet.getLong("CLIENT_CEDRIF"));                
            cliente.setTipoPersona(resultSet.getString("TIPPER_ID")); 	
            cliente.setTelefono(resultSet.getString("client_telefono"));
            cliente.setDireccion(resultSet.getString("client_direccion"));
		} catch (Exception e) {
			if (e.getMessage().indexOf("MSGTO8001") > -1) {
				throw new Exception(e);
			} else {
				e.printStackTrace();
				throw new Exception("Error buscando los datos del cliente.");
			}
		} finally {
			if (resultSet != null)
				resultSet.close();
		}
		System.out.println("listarPorId "+sql);
		return;
	}
	
	/**Lista el la nacionalidad del cliente*/
	public String listaNacionalidad(long idCliente) throws Exception{
		StringBuffer sql = new StringBuffer();
		String nacionalidad ="";
		sql.append("SELECT p.tipper_nombre FROM INFI_TB_201_CTES c, INFI_TB_200_TIPO_PERSONAS p WHERE c.tipper_id=p.tipper_id and client_id = ").append(idCliente);
		
		try{
			conn = dso.getConnection();			
			statement = conn.createStatement();
			resultQuery = statement.executeQuery(sql.toString());			
			
			if(resultQuery.next())
				nacionalidad = resultQuery.getString("tipper_nombre");
			
			//resultSet = DB.executeQuery(nombreDataSource, dso, sql.toString(), objAux);
			//return resultSet.getString("tipper_nombre");
		} catch (Exception e) {
				throw new Exception("Error al buscar la nacionalidad del Cliente");
			
		} finally {
			if (resultQuery != null){
				resultQuery.close();
				cerrarConexion();
			}
		}
		
		return nacionalidad;
	}	
}


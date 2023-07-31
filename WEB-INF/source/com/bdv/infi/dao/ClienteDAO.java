package com.bdv.infi.dao;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import javax.sql.DataSource;

import megasoft.DataSet;
import megasoft.db;

import org.apache.log4j.Logger;

import com.bdv.infi.data.Cliente;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.util.Utilitario;

/**
 * Clase destinado para el manejo de los registros en la tabla INFI_TB_201_CTES
 */
public class ClienteDAO extends com.bdv.infi.dao.GenericoDAO {

	private Logger logger = Logger.getLogger(ClienteDAO.class);
	
	public ClienteDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
	}

	public ClienteDAO(DataSource ds) throws Exception {
		super(ds);
	}

	/**
	 * Elimina el registro en la tabla.
	 */
	public void eliminar() {

	}

	/**
	 * Lista todos los cliente configurados en la tabla
	 */
	public void listar() {
		//TODO Borrar este método
	}

	/**
	 * Obtiene una lista de clientes de acuerdo a ciertos criterios de b&uacute;squeda
	 * y los almacena en un DataSet
	 * 
	 * @param idCliente
	 * @param idTipoPersona
	 * @param cedRif
	 * @param nombreCliente
	 * @param ctaCustodia
	 * @param fechaCtaCustodia
	 * @throws Exception
	 */
	public void listar(long idCliente, String idTipoPersona, long cedRif,
			String nombreCliente, long ctaCustodia, Date fechaCtaCustodia)
			throws Exception {
		// Se debe usar la clase CustodiaDetalle e incorporarle CustodiaBloqueo
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");

		sql.append("SELECT * FROM INFI_TB_201_CTES cl WHERE 1=1");

		if (idCliente != 0)
			filtro.append(" AND cl.client_id = ").append(idCliente);

		if (idTipoPersona != null && !idTipoPersona.equals(""))
			filtro.append(" AND cl.tipper_id = '").append(idTipoPersona)
					.append("'");

		if (cedRif != 0)
			filtro.append(" AND cl.client_cedrif = '").append(cedRif).append(
					"'");

		if (nombreCliente != null && !nombreCliente.equals(""))
			filtro.append(" AND upper(cl.client_nombre) like upper ('%")
					.append(nombreCliente).append("%')");

		if (ctaCustodia != 0)
			filtro.append(" AND cl.client_cta_custod_id = '").append(
					ctaCustodia).append("'");

		if (fechaCtaCustodia != null && !fechaCtaCustodia.equals(""))
			filtro.append(" AND cl.client_cta_custod_fecha = '").append(
					fechaCtaCustodia).append("'");

		sql.append(filtro.append("ORDER BY cl.client_nombre"));
		//System.out.println("QUERY " + sql.toString());
		dataSet = db.get(dataSource, sql.toString());
		System.out.println("listar-----> "+sql);

	}
	
	/**
	 * Obtiene una lista de clientes de acuerdo a ciertos criterios de b&uacute;squeda
	 * y los almacena en un DataSet. Incluye los Correlativos. NM32454
	 * 
	 * @param idCliente
	 * @param idTipoPersona
	 * @param cedRif
	 * @param nombreCliente
	 * @param ctaCustodia
	 * @param fechaCtaCustodia
	 * @throws Exception
	 */
	public void listarCorrelativos(long idCliente, String idTipoPersona, long cedRif,
			String nombreCliente, long ctaCustodia, Date fechaCtaCustodia)
			throws Exception {
		// Se debe usar la clase CustodiaDetalle e incorporarle CustodiaBloqueo
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");

		sql.append(" SELECT CASE WHEN CO.CLIENT_NOMBRE IS NULL THEN CL.CLIENT_NOMBRE ELSE CO.CLIENT_NOMBRE END CLIENT_NOMBRE, "); 
		sql.append(" CASE WHEN CO.NUMERO_PERSONA IS NULL THEN CL.NUMERO_PERSONA ELSE CO.NUMERO_PERSONA END NUMERO_PERSONA, ");
		sql.append(" CASE WHEN co.numero_secu_documento IS NULL THEN 1 ELSE co.numero_secu_documento END numero_secu_documento, ");
		sql.append(" CL.TIPPER_ID, ");
	    sql.append(" CL.CLIENT_CEDRIF, ");
	    sql.append(" CL.CLIENT_ID ");
		sql.append(" FROM INFI_TB_201_CTES cl, INFI_TB_231_CTES_CORRELATIVOS CO");
		sql.append(" WHERE  CL.CLIENT_ID  = CO.CLIENT_ID (+)");
		
		if (idCliente != 0)
			filtro.append(" AND cl.client_id = ").append(idCliente);

		if (idTipoPersona != null && !idTipoPersona.equals(""))
			filtro.append(" AND cl.tipper_id = '").append(idTipoPersona)
					.append("'");
	
		if (cedRif != 0)
			filtro.append(" AND cl.client_cedrif = '").append(cedRif).append(
					"'");

		if (nombreCliente != null && !nombreCliente.equals(""))
			filtro.append(" AND upper(cl.client_nombre) like upper ('%")
					.append(nombreCliente).append("%')");

		if (ctaCustodia != 0)
			filtro.append(" AND cl.client_cta_custod_id = '").append(
					ctaCustodia).append("'");

		if (fechaCtaCustodia != null && !fechaCtaCustodia.equals(""))
			filtro.append(" AND cl.client_cta_custod_fecha = '").append(
					fechaCtaCustodia).append("'");

		sql.append(filtro.append("ORDER BY co.numero_secu_documento, cl.client_nombre"));
		//System.out.println("QUERY " + sql.toString());
		dataSet = db.get(dataSource, sql.toString());
	}

	/**Busca un cliente por su ID y retorna un dataset*/
	public void listarPorId(String idCliente) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM INFI_TB_201_CTES WHERE client_id = ").append(idCliente);
		dataSet = db.get(dataSource, sb.toString());
		if(dataSet.count()>0){
			dataSet.next();
		}
		System.out.println("listarPorId "+sb.toString());
	}
	
	/**
	 * Busca un cliente por su id
	 * 
	 * @param idCliente
	 * @throws Exception
	 *             lanza una exception si el id del cliente no es encontrado
	 */
	public boolean listarPorId(long idCliente) throws Exception {
		boolean bolOk = false;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM INFI_TB_201_CTES WHERE client_id = ").append(idCliente);
		try {
			conn = dataSource.getConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql.toString());
		} catch (Exception e) {
			logger.error(e.getMessage()+ Utilitario.stackTraceException(e));
			throw new Exception("Error en la b&uacute;squeda del cliente por su id " + e.getMessage());
		}

		if (resultSet.next()) {
			bolOk = true;
		}
		System.out.println("listarPorId "+sql);
		return bolOk;
	}

	/**
	 * Lista el cliente que coincida con la c&eacute;dula o el rif ingresado
	 * 
	 * @param cedRif
	 *            c&eacute;dula o rif del cliente a buscar
	 */
	public void listarPorCedRif(long cedRif) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select * from INFI_TB_201_CTES where client_cedrif='");
		sql.append(cedRif).append("'");
		dataSet = db.get(dataSource, sql.toString());		
	}
	
	/**
	 * Lista el cliente que coincida con la c&eacute;dula o el rif ingresado y el tipo de persona
	 * 
	 * @param cedRif
	 *            c&eacute;dula o rif del cliente a buscar
	 */
	public void listarPorCedRifyTipoPersona(long cedRif, String tipoPer) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select * from INFI_TB_201_CTES where client_cedrif='");
		sql.append(cedRif).append("' and tipper_id='").append(tipoPer).append("'");
		//System.out.println("SQL Cliente: "+sql.toString());
		dataSet = db.get(dataSource, sql.toString());		
	}
	
	/**
	 * Lista el cliente que coincida con la c&eacute;dula o el rif ingresado
	 * 
	 * @param cedRif
	 *            c&eacute;dula o rif del cliente a buscar
	 * @return cliente encontrado. Null en caso de no encontrarse
	 */
	public Cliente listarPorCedRifObj(long cedRif) {
		return null;
	}

	/**
	 * Lista todos los cliente configurados en la tabla que coincidan con parte
	 * o todo el nombre ingresado
	 * 
	 * @param nombre
	 *            parte del nombre o todo el nombre del cliente.
	 */
	public void listarPorNombre(String nombre) {
		StringBuffer sql = new StringBuffer();
	}
	
	/**Metodo que retorna el la letras del tipo de persona corespondiente al cliente 
	 * @param idCliente El idCliente para el que se va a buscar el tipo de persona
	 * @return String Retorna el tipo de persona
	 * @throws Exception Lanza Exception si no encuentra el tipo de persona para un id dado
	 * 
	 */
	public String listarTipoPersona(long idCliente) throws Exception{
		StringBuffer sql = new StringBuffer();
		String tipo = null;
		sql.append("select tipper_id from infi_tb_201_ctes where client_id = ");
		sql.append(idCliente);
		
		try {
			conn = dataSource.getConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery(sql.toString());
			
			if (resultSet.next()) {
				tipo = resultSet.getString(1);
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage()+ Utilitario.stackTraceException(e));
			throw new Exception("Error en la b&uacute;squeda del tipo de persona por su id " + e.getMessage());
		}finally{
			this.closeResources();
			this.cerrarConexion();
		}
		
		return tipo;
	}
	
	/**
	 * Modifica el registro de una tabla
	 */
	public int modificar(com.bdv.infi.data.TipoGarantia tipoGarantia) {
		return 0;
	}

	/**
	 * Registra el cliente en la base de datos
	 * @param cliente objeto cliente que se desea almacenar en tabla
	 * @return n&uacute;mero de registros insertados
	 */
	public int insertar(com.bdv.infi.data.Cliente cliente) throws Exception {
		int resultado = 0;
		int indice=1;
		listar(0, cliente.getTipoPersona(), cliente.getRifCedula(), null, 0, null);//return dataSet si es igual a 0 insert
		if (dataSet.count()==0){
			StringBuffer sql = new StringBuffer();
			sql.append("INSERT INTO INFI_TB_201_CTES (CLIENT_ID,TIPPER_ID,CLIENT_CEDRIF,CLIENT_NOMBRE,CLIENT_CTA_CUSTOD_ID,");
			sql.append("CLIENT_CTA_CUSTOD_FECHA,CLIENT_DIRECCION,CLIENT_TELEFONO,CLIENT_TIPO,CLIENT_CORREO_ELECTRONICO,");
			sql.append("CLIENT_EMPLEADO,CTESEG_ID,NUMERO_PERSONA) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)");
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql.toString());	
			System.out.println("insertar "+sql);
			try {						
				long idCliente = Long.parseLong(this.dbGetSequence(this.dataSource, ConstantesGenerales.SECUENCIA_CLIENTE));
				long ctaCustodia = Long.parseLong(this.dbGetSequence(this.dataSource, ConstantesGenerales.SECUENCIA_CTA_CUSTODIA));
				
				cliente.setIdCliente(idCliente);
	
				ps.setLong(indice++, idCliente);
				ps.setString(indice++, cliente.getTipoPersona());
				ps.setLong(indice++, cliente.getRifCedula());
				ps.setString(indice++, cliente.getNombre());
				ps.setLong(indice++, ctaCustodia);
	    		java.sql.Date ahora = new java.sql.Date(new Date().getTime());
				ps.setDate(indice++, ahora);		
				ps.setString(indice++, cliente.getDireccion());
				ps.setString(indice++, cliente.getTelefono());
				ps.setString(indice++, cliente.getTipo());
				ps.setString(indice++, cliente.getCorreoElectronico());
				ps.setInt(indice++, cliente.isEmpleado()?1:0);
				ps.setString(indice++, cliente.getCodigoSegmento());
				ps.setString(indice++, cliente.getNumeroPersona());
				
				resultado = ps.executeUpdate();
				System.out.println("insertar "+sql);
			} catch (Exception e) {
				logger.error(e.getMessage()+ Utilitario.stackTraceException(e));
				throw new Exception("Error en la inserci&oacute;n del cliente");
			} finally{
	            if (ps != null){
	            	ps.close();
	            }
	            if (conn != null){
	            	conn.close();
	            }
			}
		}
		return resultado;
	}
	
	/**
	 * Registra el cliente en la base de datos
	 * @param cliente objeto cliente que se desea almacenar en tabla
	 * @return n&uacute;mero de registros insertados
	 */
	public void actualizarCliente (com.bdv.infi.data.Cliente cliente) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE INFI_TB_201_CTES SET ");
		
		sql.append("CLIENT_EMPLEADO=").append(cliente.isEmpleado()?1:0);
		
		if (cliente.getNombre() != null && !cliente.getNombre().equals(""))
			sql.append(" ,CLIENT_NOMBRE='").append(cliente.getNombre()).append("'");
		
		if (cliente.getDireccion() != null && !cliente.getDireccion().equals(""))
			sql.append(",CLIENT_DIRECCION='").append(cliente.getDireccion()).append("'");
		
		if (cliente.getTelefono() != null && !cliente.getTelefono().equals(""))
			sql.append(",CLIENT_TELEFONO='").append(cliente.getTelefono()).append("'");
		
		if (cliente.getTipo() != null && !cliente.getTipo().equals(""))
			sql.append(",CLIENT_TIPO='").append(cliente.getTipo()).append("'");
		
		if (cliente.getCorreoElectronico() != null && !cliente.getCorreoElectronico().equals(""))
			sql.append(",CLIENT_CORREO_ELECTRONICO='").append(cliente.getCorreoElectronico()).append("'");///*****

		if (cliente.getCodigoSegmento() != null && !cliente.getCodigoSegmento().equals(""))
			sql.append(",CTESEG_ID='").append(cliente.getCodigoSegmento()).append("'");
		
		if (cliente.getNumeroPersona() != null && !cliente.getNumeroPersona().equals(""))
			sql.append(",NUMERO_PERSONA='").append(cliente.getNumeroPersona()).append("'");
		
		sql.append("WHERE CLIENT_ID=").append(cliente.getIdCliente());
		System.out.println("actualizarCliente --> " + sql.toString());
		db.exec(dataSource, sql.toString());
		
	}
	
	/**
	 * Actualiza campos especificos del cliente en la base de datos
	 * @param cliente objeto cliente que se desea almacenar en tabla
	 * @return n&uacute;mero de registros insertados
	 */
/*	public void actualizarCliente (com.bdv.infi.data.Cliente cliente) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE INFI_TB_201_CTES SET ");
		sql.append("CLIENT_NOMBRE='").append(cliente.getNombre()).append("'");
		sql.append(",CLIENT_DIRECCION='").append(cliente.getDireccion()).append("'");
		sql.append(",CLIENT_TELEFONO='").append(cliente.getTelefono()).append("'");
		sql.append(",CLIENT_TIPO='").append(cliente.getTipo()).append("'");
		sql.append(",CLIENT_CORREO_ELECTRONICO='").append(cliente.getCorreoElectronico()).append("'");///*****
		sql.append(",CLIENT_EMPLEADO=").append(cliente.isEmpleado()?1:0);
		sql.append(",CTESEG_ID='").append(cliente.getCodigoSegmento()).append("'");
		sql.append("WHERE CLIENT_ID=").append(cliente.getIdCliente());
		
		db.exec(dataSource, sql.toString());	
	}*/


	public Object moveNext() throws Exception {
		boolean bolPaso = false;
        Cliente cliente = new Cliente();
        try {
            //Si no es ultimo registro arma el objeto            
            if ((resultSet!=null)&&(!resultSet.isAfterLast())){            	
                bolPaso = true;
                cliente.setIdCliente(resultSet.getLong("CLIENT_ID"));
                cliente.setCuentaCustodia(resultSet.getLong("CLIENT_CTA_CUSTOD_ID"));                
                cliente.setFechaCuentaCustodia(resultSet.getDate("CLIENT_CTA_CUSTOD_FECHA"));                
                cliente.setNombre(resultSet.getString("CLIENT_NOMBRE"));                
                cliente.setRifCedula(resultSet.getLong("CLIENT_CEDRIF"));                
                cliente.setTipoPersona(resultSet.getString("TIPPER_ID"));
                cliente.setCodigoSegmento("CTESEG_ID");
                resultSet.next();
            }
        } catch (SQLException e) {
        	logger.error(e.getMessage()+ Utilitario.stackTraceException(e));
            super.closeResources();
            throw new Exception("Error al intentar crear el objeto cliente ");
        }
        if (bolPaso) {
            return cliente;
        } else {
            return null;
        }		
	}

	/**
	 * Obtiene los datos de un cliente en espec&iacute;fico 
	 * @param idCliente id del cliente a consultar
	 * @throws Exception
	 */
	public void detallesCliente(String idCliente) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM INFI_TB_201_CTES cl, INFI_TB_200_TIPO_PERSONAS tp WHERE cl.client_id='").append(idCliente).append("'");
		sb.append(" AND tp.tipper_id = cl.tipper_id");
		dataSet = db.get(dataSource, sb.toString());
		System.out.println("detallesCliente: "+sb.toString());
	}
	
	
	/**
	 * Obtiene una lista de clientes dado un conjunto de Id de clientes
	 * @param clientes arreglo que contiene los id de los clientes solicitados
	 * @throws Exception
	 */
	public void listarClientesPorId(String[] clientes) throws Exception{
				
		StringBuffer sb = new StringBuffer();
		
		if(clientes!=null){
			
			sb.append("SELECT * FROM INFI_TB_201_CTES cl, INFI_TB_200_TIPO_PERSONAS tp");
			sb.append(" WHERE cl.tipper_id = tp.tipper_id AND (");
			
			for(int i=0; i< clientes.length; i++){
				if(!clientes[i].equals("")){
					if(i!=0)sb.append(" OR");
					sb.append(" cl.client_id=").append(clientes[i]);
				}
			}
			
			sb.append(") ORDER BY cl.client_nombre, cl.client_cedrif");
			
			dataSet = db.get(dataSource, sb.toString());	
		}
		
			
	}
	
	public void selectClienteID(long idCliente) throws Exception{
		StringBuffer sql = new StringBuffer();
		
		sql.append(" select client_nombre as client_nombre1,client_id as client_id1 from infi_tb_201_ctes where 1 = 1");
		sql.append(" and infi_tb_201_ctes.client_id =").append(idCliente);
		
		dataSet = db.get(dataSource, sql.toString());
	}
	
	/**
	 * Lista el nombre del cliente que trasfiere como @transfiere@
	 * @param long idCliente
	 * @return Dataset 
	 */
	public DataSet listarNombreCliente(long idCliente) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT client_nombre as transfiere,INFI_TB_201_CTES.client_id as id_transfiere ,INFI_TB_201_CTES.* FROM INFI_TB_201_CTES WHERE client_id = ").append(idCliente);
		dataSet = db.get(dataSource, sb.toString());
		if(dataSet.count()>0){dataSet.next();}
		System.out.println("listarNombreCliente "+sb);
		return dataSet;
	}
	
	/**
	 * 
	 */
	public String actulizarDataCliente(Cliente cliente) throws Exception{
		
		//Modifica el cliente en la base de datos
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE INFI_TB_201_CTES SET ");
		
		sql.append("TIPPER_ID='").append(cliente.getTipoPersona()).append("', ");
		sql.append("CLIENT_CEDRIF=").append(cliente.getRifCedula()).append(", ");
		sql.append("CLIENT_NOMBRE='").append(cliente.getNombre()).append("', ");
		sql.append("CLIENT_DIRECCION='").append(cliente.getDireccion()).append("', ");
		sql.append("CLIENT_TELEFONO='").append(cliente.getTelefono()).append("', ");
		sql.append("CLIENT_TIPO='").append(cliente.getTipo()).append("', ");
		sql.append("CLIENT_CORREO_ELECTRONICO='").append(cliente.getCorreoElectronico()).append("', ");
		sql.append("CLIENT_EMPLEADO=").append(cliente.isEmpleado()?1:0).append(", ");
		sql.append("CTESEG_ID='").append(cliente.getCodigoSegmento()).append("'");
		
		//Se incorpora el where de la consulta
		sql.append(" WHERE CLIENT_ID=").append(cliente.getIdCliente());
		
		return(sql.toString());
	}
	
	/**Busca un cliente por su ID y retorna un dataset*/
	public String buscarDatosPorIdCliente(String idCliente) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT CLIENT_CEDRIF,CLIENT_NOMBRE,TIPPER_ID,CTESEG_ID FROM INFI_TB_201_CTES WHERE client_id = ").append(idCliente);
		dataSet = db.get(dataSource, sb.toString());
		if(dataSet.count()>0){
			dataSet.next();
			return dataSet.getValue("CLIENT_CEDRIF");	
			
		}
		System.out.println("buscarDatosPorIdCliente "+sb);
		return null;
	}
	
	/**
	 * Consulta el client_id y el tipper_id de un cliente dado su CED/RIF	 * 
	 * @param cedRif c&eacute;dula o rif del cliente a buscar      
	 */
	public void buscarPorCedRif(long cedRif) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select client_id,tipper_id from INFI_TB_201_CTES where client_cedrif='");
		sql.append(cedRif).append("'");
		dataSet = db.get(dataSource, sql.toString());		
	}
	
	/**
	 * Busca las contrapartes registradas en CARMEN según el criterio del usuario
	 * @param codigo código de cliente a buscar
	 * @param nombre parte del nombre a buscar
	 * @throws Exception en caso de error
	 */
	public void buscarContrapartesCarmen(String codigo, String nombre) throws Exception{
		StringBuffer sql = new StringBuffer("select trim(cno) codigo, sn nombre from cust ");
		if (codigo != null && !codigo.equals("")){
			sql.append(" where cno=").append(codigo);	
		}else if (nombre != null && !nombre.equals("")) {
			sql.append(" where lower(sn) like lower('%").append(nombre).append("%')");
		} else{
			sql.append(" where cno=1000");
		}
		System.out.println("buscarContrapartesCarmen --> "+sql.toString());
		dataSet = db.get(dataSource, sql.toString());
	}
	
	/**
	* Obtiene una lista de clientes de acuerdo a ciertos criterios de b&uacute;squeda
	* y los almacena en un DataSet
	* @autor Victor Goncalves
	* @param idCliente
	* @param idTipoPersona
	* @param cedRif
	* @param nombreCliente
	* @param ctaCustodia
	* @param fechaCtaCustodia
	* @throws Exception
	*/
	public void listarCliente(long idCliente, String idTipoPersona, long cedRif)
	throws Exception {
		StringBuffer sql = new StringBuffer();
		StringBuffer filtro = new StringBuffer("");
	
		sql.append("SELECT cl.client_id,cl.client_cedrif,cl.tipper_id,cl.client_nombre, cl.client_correo_electronico, cl.CLIENT_EMPLEADO FROM INFI_TB_201_CTES cl WHERE ");		
		sql.append(" cl.client_cedrif = '").append(cedRif).append("'");
		
		if (idTipoPersona != null && !idTipoPersona.equals(""))
			filtro.append(" AND cl.tipper_id = '").append(idTipoPersona).append("'");
		
		if (idCliente != 0)
			filtro.append(" AND cl.client_id = ").append(idCliente);
		
		sql.append(filtro.append(" ORDER BY cl.client_id"));
		dataSet = db.get(dataSource, sql.toString());
	
	} 
	
	/**
	 * Metodo de busqueda de cliente en la tabla CUST de Opcis
	 * */
	public void clienteOpics(String rif, String tipo)throws Exception {
		StringBuffer sql=new StringBuffer();		
		sql.append("SELECT TRIM(A.CNO) ID_CLIENTE,TRIM(A.TAXID) RIF_CEDULA,A.SN NOMBRE ");
		sql.append("FROM CUST A WHERE TRIM(A.TAXID)='");
		
		if (tipo.toUpperCase().equals("V") ||tipo.toUpperCase().equals("E")){			
			sql.append(tipo.concat(Utilitario.rellenarCaracteres(rif, '0', 14, false)));
		}else{			
			sql.append(Utilitario.digitoVerificador(tipo.concat(Utilitario.rellenarCaracteres(rif, '0', 8, false)),false));
		}		
		sql.append("'");
		
		dataSet = db.get(dataSource, sql.toString());
	}
	
	/**
	 * LLama a una función de base de datos que obtiene el digito verificador de un cliente dado su RIF
	 * @param rifCliente
	 * @return String con el digito verificador
	 * @throws Exception
	 */
	public String obtenerDigitoVerificadorRif(String rifCliente) throws Exception {
		String digitoVerificador;
		try {
			System.out.println("OBTEniendo digito verificador...");
			conn = this.dataSource.getConnection();	
			CallableStatement funcionBD = conn.prepareCall("{? = call infi_getdigvrf(?)}");			
			// Registrar el tipo del valor retornado por la funcion
			funcionBD.registerOutParameter(1, java.sql.Types.NUMERIC);
			// cargar parametros a la función
			funcionBD.setString(2, rifCliente);
			
		   	// Ejecutar la función y obtener el valor retornado
		    funcionBD.execute();
			digitoVerificador = funcionBD.getString(1);
		
		} catch (Exception e) {			
			throw new Exception("Error buscando el dígito verificador del rif del cliente: " +rifCliente + " : " + e.getMessage());
		} finally {
			this.closeResources();
			this.cerrarConexion();
		}		

		return digitoVerificador;
	}
	
	/**
	 * Se encarga de hacer un select para buscar los clientes por correlativo NM32454 09/01/2015
	 * @param  idCliente
	 * @param  numeroSecuencialDocumento
	 * @return void
	 * @throws Exception
	 */
	public void obtenerClienteCorrelativo(Long idCliente, Integer numeroSecuencialDocumento) throws Exception {
		StringBuffer sql=new StringBuffer();		
		sql.append(" SELECT * FROM INFI_TB_231_CTES_CORRELATIVOS  ");
		sql.append(" WHERE CLIENT_ID = "+idCliente);
		sql.append(" AND   NUMERO_SECU_DOCUMENTO= "+numeroSecuencialDocumento);
		dataSet = db.get(dataSource, sql.toString());
	}
	
	/**
	 * Se encarga de insertar un registro en la tabla INFI_TB_231_CTES_CORRELATIVOS  NM32454 09/01/2015
	 * @param  idCliente
	 * @param  numeroSecuencialDocumento
	 * @param  numeroPersona
	 * @return void
	 * @throws Exception
	 */
	public void insertarClienteCorrelativo(String[] st) throws Exception {
		db.execBatch(dataSource, st);
	}
}

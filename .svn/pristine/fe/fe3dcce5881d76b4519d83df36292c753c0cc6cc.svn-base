package com.bdv.infi_services.business.ActualizarOperacionesFinancieras;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import com.bdv.infi.dao.OperacionDAO;
import com.bdv.infi_services.beans.entidades.mensajes_peticion.ParametrosActualizarOperacionesFinancieras;
import com.bdv.infi_services.beans.entidades.mensajes_respuesta.OperacionFinanciera;
import com.bdv.infi_services.beans.entidades.mensajes_respuesta.OperacionesFinancieras;
import com.bdv.infi_services.business.AbstractConsultaPaginada;
import com.bdv.infi_services.utilities.DBOServices;
import com.megasoft.soa.webservices.commom.WSProperties;
/**
 * Clase encargada de actualizar las operaciones financieras con el número de cheque indicado
 * @author elaucho
 */
public class ManejadorActualizarOperacionesFinancieras extends AbstractConsultaPaginada{

	String 	dsName 						  = WSProperties.getProperty("datasource-infi");
	public javax.sql.DataSource dso 	  = DBOServices.getDataSource(dsName);

	public ManejadorActualizarOperacionesFinancieras() throws Exception{
		super();
	}
	/**
	 * Metodo que recibe como entrada un Objeto ParametrosActualizarOperacionesFinancieras para actualizar en base de datos y
	 * retornar la salida de los registros actualizados
	 * @param ParametrosActualizarOperacionesFinancieras
	 * @return OperacionesFinancieras
	 * @throws Throwable
	 */
	public OperacionesFinancieras getOperacionesActualizar (ParametrosActualizarOperacionesFinancieras parametros) throws Throwable {
		
		OperacionDAO operacionDao	    		  = new OperacionDAO(dso);
		HashMap<String,String> operacionesHashMap = new HashMap<String,String> ();
		OperacionesFinancieras operaciones 		  = new OperacionesFinancieras();
		ArrayList<OperacionFinanciera> operacionesActualizadas	= new ArrayList<OperacionFinanciera>();
		ArrayList parametrosOperaciones	= parametros.getParametrosOperacionesFinancieras();
		Connection conn  = null;
		Statement stmt   = null;
		conn 			 = dso.getConnection();
		conn.setAutoCommit(false);
		stmt = conn.createStatement();
		
		//Se valida que la cédula venga en el formato indicado
		if(parametros.getCedulaCliente().length()<10 || parametros.getCedulaCliente().length()>10){
			throw new Exception("El parametro de la Cédula debe estar en este formato V000000000");
		}
		int cedula      = parametros.getCedulaCliente()==null?0:Integer.parseInt(parametros.getCedulaCliente().substring(2,parametros.getCedulaCliente().length()));
		String tipperId = parametros.getCedulaCliente()==null?"":parametros.getCedulaCliente().substring(0,1);
		
		//HashMap que contiene las operaciones que se le adeudan al cliente
		operacionesHashMap = operacionDao.listarOperacionesFinancieras(cedula,tipperId);
		
		try {
			for (int i=0; i<parametrosOperaciones.size(); i++) {
				//Objeto actualizarOperaciones que contendra la posicion del objeto contenida en el arrayList
					com.bdv.infi_services.beans.entidades.mensajes_peticion.ParametroActualizarOperacionesFinancieras actualizarOperaciones = 
					(com.bdv.infi_services.beans.entidades.mensajes_peticion.ParametroActualizarOperacionesFinancieras)parametrosOperaciones.get(i);
				
				/*
				 * Objeto operacionEnviar al cual se le setean los valores para luego implementar el metodo
				 * actualizarOperacionesFinanancierasWS
				 */
					com.bdv.infi_services.beans.entidades.mensajes_peticion.ParametroActualizarOperacionesFinancieras operacionEnviar =
					new com.bdv.infi_services.beans.entidades.mensajes_peticion.ParametroActualizarOperacionesFinancieras();
					
					operacionEnviar.setIdOperacionFinanciera(actualizarOperaciones.getIdOperacionFinanciera());
					operacionEnviar.setIdOrden(actualizarOperaciones.getIdOrden());
					operacionEnviar.setNumeroCheque(actualizarOperaciones.getNumeroCheque());
					
					/*
					 * Antes de actualizar la operacion financiera, se busca si existe una operacion pagada con
					 * el numero de cheque enviado, de existir no se permite actualizar la operacion
					 */
					try {
						//operacionDao.listarOperacionCheque(actualizarOperaciones.getNumeroCheque());
					} catch (Throwable e) {
						throw e;
					}

					operacionDao.actualizarOperacionesFinancieras(operacionEnviar,stmt);
					
					//Buscar La operacion que se actualizo para agregarla al arrayList de operaciones financieras que se retornara
					OperacionFinanciera operacion = new OperacionFinanciera();
					operacion.setIdOperacionFinanciera(actualizarOperaciones.getIdOperacionFinanciera());
					operacion.setIdOrden(actualizarOperaciones.getIdOrden());
					operacionDao.listarOperacionFinanciera(operacion,conn);
					
					/*
					 * Se verifica en el Hash si la orden corresponde con la operacion
					 * De ser asi se remueve del Hash para luego verificar si todas las
					 * Operaciones fueron aplicadas
					 */					
						if(operacionesHashMap.get(actualizarOperaciones.getIdOrden())!=null)
						{
							if(operacionesHashMap.get(actualizarOperaciones.getIdOrden()).equals(String.valueOf(actualizarOperaciones.getIdOperacionFinanciera()))){
								operacionesHashMap.remove(String.valueOf(actualizarOperaciones.getIdOrden()));
							}//fin if interno
						}else{
							//throw new Throwable("La orden "+actualizarOperaciones.getIdOrden()+" no corresponde con la operación financiera");
						}
					//Se agrega al arraylist la operacion que ha sido actualizada
					operacionesActualizadas.add(operacion);
				}//FIN FOR
			stmt.executeBatch();
			
			if(operacionesHashMap.isEmpty()){
				conn.commit();
			}else{
				throw new Throwable("El cliente aún posee operaciones pendientes para ser pagadas");
			}//FIN ELSE	
		} catch (Throwable e) {
			conn.rollback();
			throw e;
		}finally{
			if(stmt!=null){
				stmt.close();
			}
			if(conn!=null){
				conn.close();
			}
			operacionDao.closeResources();	
		}//FIN FINALLY
		operaciones.setOperaciones(operacionesActualizadas);
		return operaciones;
	}//FIN METODO
}//FIN CLASE

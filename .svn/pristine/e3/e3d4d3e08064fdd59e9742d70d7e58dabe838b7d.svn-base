package com.bdv.infi.dao;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.util.Utilitario;

import megasoft.db;

/** 
 * Clase que encapsula los procesos de inserci&oacute;n, modificaci&oacute;n, eliminaci&oacute;n y lista de los veh&iacute;culos registrados. Hace uso de la tabla INFI_TB_018_VEHICULOS
 */
public class VehiculoDAO extends com.bdv.infi.dao.GenericoDAO {
	
	public VehiculoDAO(Transaccion transanccion) throws Exception {
		super(transanccion);
		// TODO Auto-generated constructor stub
	}
	
	public VehiculoDAO(DataSource ds) {
		super(ds);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Lista todos los veh&iacute;culos registrados 
	*/
	public void listarTodos() throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select * from INFI_TB_018_VEHICULOS order by vehicu_nombre");

		dataSet = db.get(dataSource, sql.toString());
	}
	
	/**
	 * Lista los veh&iacute;culos registrados por unidad de inversi&oacute;n y blotter
	 * @param idUnidad id de la unidad de inversi&oacute;n
	 * @param bloter bloter asociado a la unidad de inversi&oacute;n 
	*/
	public void listarPorUiBloter(long idUnidad, String bloter ){
	
	}	

	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Lista los datos de un veh&iacute;culo dado un id determinado
	 * @param idVehiculo
	 * @throws Exception
	 */
	public void listarPorId(String idVehiculo) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		sql.append("select * from INFI_TB_018_VEHICULOS ");
		sql.append(" where vehicu_id = '").append(idVehiculo).append("'");

		dataSet = db.get(dataSource, sql.toString());

	}	
	
	/**
	 * Verifica si el veh&iacute;culo tomador es BDV
	 * @param vehiculoTomador
	 * @return
	 * @throws Exception 
	 */
	public boolean vehiculoEsBDV(String idVehiculo) throws Exception {
		
		Logger logger = Logger.getLogger(Transaccion.class);

		logger.error("ID VEH TOMADOR " +idVehiculo);
		
		//obtener rif BDV
		String rifBDV = ParametrosDAO.listarParametros(com.bdv.infi.logic.interfaces.ParametrosSistema.RIF_BDV, dataSource);
		rifBDV=	Utilitario.rifAXCaracteres(rifBDV,11);		
		logger.error("RIF BDV ENCONTRADO " + rifBDV);
		
		//obtener rif del vehiculo tomador de la orden
		String rifVehiculoTomador = obtenerRifVehiculo(idVehiculo);
		logger.error("RIF VEHICULO TOMADOR COMPLETO "+ rifVehiculoTomador);
		
		//verificar si el vehiculo tomador es BDV
		if(rifVehiculoTomador.equals(rifBDV)){
			logger.error("VEHICULO ES BDV");
			return true;
		}else{

			logger.error("VEHICULO NO ES BDV");
			return false;
		}
		
	}
		
	/**
	 * Obtiene el rif completo de un Veh&iacute;culo.
	 * @param idVehiculoTomador
	 * @return
	 * @throws Exception
	 */
	public String obtenerRifVehiculo(String idVehiculo) throws Exception {

		String rifVehiculo = "";
		listarPorId(idVehiculo);			
		
		if(dataSet.next()){			
			rifVehiculo = dataSet.getValue("vehicu_rif");			
		}
		
		if(rifVehiculo.equals("")){
			throw new Exception ("EL veh&iacute;culo tomador no tiene registrado un rif.");
		}

		return rifVehiculo;	

	}
	
	/**
	 * Obtiene el campo especificado en el parámetro
	 * @param idVehiculo
	 * @throws Exception
	 */
	public String listarCampoPorId(String idVehiculo, String campo) throws Exception{
		String valor=null;
		StringBuffer sql = new StringBuffer();
		sql.append("select ").append(campo).append(" from INFI_TB_018_VEHICULOS ");
		sql.append(" where vehicu_id = '").append(idVehiculo).append("'");

		dataSet = db.get(dataSource, sql.toString());
		
		if(dataSet!=null&&dataSet.count()>0&&dataSet.next()){			
			valor = dataSet.getValue(campo);			
		}
		return valor;
	}
	
	
	/**
	 * Obtiene el campo especificado en el parámetro
	 * @param idVehiculo
	 * @throws Exception
	 */
	public void obtenerVehiculoBDV() throws Exception{		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT A.VEHICU_ID, A.VEHICU_NOMBRE, A.VEHICU_RIF, A.VEHICU_SIGLAS,A.VEHICU_NUMERO_CUENTA, A.VEHICU_BRANCH, A.VEHICU_NUMERO_CUENTA_BCV ");
		sql.append(" FROM INFI_TB_018_VEHICULOS A WHERE A.VEHICU_RIF =");
		sql.append(" (SELECT A.PARVAL_VALOR FROM INFI_TB_002_PARAM_TIPOS A, INFI_TB_001_PARAM_GRUPO B WHERE A.PARGRP_ID = B.PARGRP_ID AND A.PARTIP_NOMBRE_PARAMETRO = '"+ParametrosSistema.RIF_BDV+"')");
		
		dataSet = db.get(dataSource, sql.toString());
	}
       
         
        
          

}

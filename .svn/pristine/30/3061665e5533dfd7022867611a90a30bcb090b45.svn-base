package com.bdv.infi.logic.cierre_sistema;

import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import javax.sql.DataSource;

import megasoft.Logger;

import com.bdv.infi.dao.AuditoriaDAO;
import com.bdv.infi.dao.CierreSistemaDAO;
import com.bdv.infi.data.Auditoria;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.util.Utilitario;



/** 
 * Clase que se encarga de la operaci&oacute;n de una transacci&oacute;n espec&iacute;fica
 */
public class ProcesoCierreSistema {
	
	private DataSource _dso;
	private String ip;
	private String userName;
	private String tipoPeticion;
	
	/**
	 * Constructor
	 * @param datasource
	 * @param ip
	 * @param usuario
	 */
	public ProcesoCierreSistema(DataSource datasource, String ip, String usuario, String tipoPeticion) {
		this._dso = datasource;
		this.ip = ip;
		this.userName = usuario;
		this.tipoPeticion = tipoPeticion;
		
	}
	
	public boolean procesarCierre(String tipoEjecucion) throws Exception{
		
		boolean OK = true;		
		
		registrarAuditoria();
		ejecutarSPCierre(tipoEjecucion);	
		
		return OK;		
	}

	/**
	 * Registra la auditoria del proceso de cierre iniciado
	 * @throws Exception 
	 * @throws Exception 
	 */
	private void registrarAuditoria() throws Exception {
		com.bdv.infi.dao.Transaccion transaccion = new com.bdv.infi.dao.Transaccion(_dso);
		ArrayList<String> querys=new ArrayList<String>();
		AuditoriaDAO auditoriaDAO = new AuditoriaDAO(_dso);
		
		Logger.info(this,"Registrando auditoria del proceso de cierre del sistema...");
			try {
				///********REGISTRAR LA AUDITORIA DE LA PETICIÓN DE LLAMADA AL REPROCESO DEL CIERRE DEL SISTEMA****///
				//Configuracion del objeto para el proceso de auditoria
				Auditoria auditoria= new Auditoria();
				auditoria.setDireccionIp(ip);			
				auditoria.setFechaAuditoria(Utilitario.DateToString(new Date(),ConstantesGenerales.FORMATO_FECHA));
				auditoria.setUsuario(userName);			
				auditoria.setPeticion(ConstantesGenerales.PETICION_REPROCESO_CIERRE);
				auditoria.setDetalle(tipoPeticion);	
				
				transaccion.begin();
				Statement statement =transaccion.getConnection().createStatement();
				
				querys.add(auditoriaDAO.insertRegistroAuditoria(auditoria));
				
				for (String element : querys) {
					statement.addBatch(element);				
				}						
				statement.executeBatch();
				
				transaccion.getConnection().commit();
				
			}catch(Exception ex){
				transaccion.rollback();								
				throw new Exception("Ha ocurrido un error registrando la auditoría del proceso de cierre : " + ex.getMessage());
			}finally{								
				if(transaccion.getConnection()!=null){
					transaccion.getConnection().close();
				}
			}	
	}

	/**
	 * Ejecuta la llamada a los procesos de base de datos de cierre de sistema 
	 * @throws Exception
	 */
	private void ejecutarSPCierre(String tipoEjecucion) throws Exception {
		Logger.info(this,"Llamando al stored procerure principal del proceso de cierre...");
		CierreSistemaDAO cierreSistemaDAO = new CierreSistemaDAO(_dso);
		String fechaPreCierre = "";
		int statusCierre = 0;
		
		//Buscar Datos del Cierre Sistema
		cierreSistemaDAO.listarDatosCierreSistema();
		if(cierreSistemaDAO.getDataSet().next()){
			fechaPreCierre = cierreSistemaDAO.getDataSet().getValue("FECHA_PRE_CIERRE");
			statusCierre = Integer.parseInt(cierreSistemaDAO.getDataSet().getValue("CIERRE_ACTIVO"));
			
			cierreSistemaDAO.cerrarSistema(ip, userName, fechaPreCierre, statusCierre,tipoEjecucion);		
		}else{
			Logger.info(this,"No se han configurado los datos de cierre de sistema en la base de datos...");
		}		
		
	}

	
}
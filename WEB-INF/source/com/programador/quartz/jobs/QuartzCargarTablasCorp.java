package com.programador.quartz.jobs;

import java.net.InetAddress;
import java.util.ArrayList;

import javax.sql.DataSource;

import megasoft.AppProperties;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.bdv.infi.dao.CiudadDAO;
import com.bdv.infi.dao.EstadoDAO;
import com.bdv.infi.dao.PaisDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.webservices.beans.CredencialesDeUsuario;
import com.bdv.infi.webservices.beans.TCGG;
import com.bdv.infi.webservices.beans.TCGGRespuesta;
import com.bdv.infi.webservices.manager.ManejadorTablasCorp;
/**
 * Clase encargada de la inserción/actualización de tablas de Paises - Tablas Coporativas
 * @author NM25287
 */
public class QuartzCargarTablasCorp extends MSCModelExtend implements Job{

	private Logger logger = Logger.getLogger(QuartzCargarTablasCorp.class);
	private static String tabCorpEntidad="102";
	private static String tabCorpIdioma="E"; //Español
	private static String tabCorpClaveTabGeneral="";
	private static String tabCorpNumeroRegistros="9999";
	private static String tabCorpTipoAcceso="4";
	private static String tabCorpDatosIni="";
	private static String tabCorpTablaGeneralPaises="112"; //"9517"
	private static String tabCorpTablaGeneralEstados="9";
	private static String tabCorpTablaGeneralCiudades="8";
	
	@SuppressWarnings("unchecked")
	public void execute(JobExecutionContext arg0) throws JobExecutionException{
		
		try {
			DataSource _dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));
			logger.info("**Se inicia proceso automatico de carga de tablas Corporativas**");		
			
			EstadoDAO estadoDAO = new EstadoDAO(_dso);
			CiudadDAO ciudadDAO = new CiudadDAO(_dso);
			PaisDAO paisDAO = new PaisDAO(_dso);
			ParametrosDAO parametrosDAO = new ParametrosDAO(_dso);
			String usuarioBus=parametrosDAO.listarParametros(ConstantesGenerales.USUARIO_WEB_SERVICES,_dso);//"NM25287";			
			String claveBus=parametrosDAO.listarParametros(ConstantesGenerales.CLAVE_WEB_SERVICES,_dso);
			
			InetAddress direccion = InetAddress.getLocalHost();
            String direccionIpstr = direccion.getHostAddress();
            
			CredencialesDeUsuario credencialesDeUsuario=new CredencialesDeUsuario();			
			credencialesDeUsuario.setNombreDeUsuario(ConstantesGenerales.CANAL_SERVICIOS_BUS);
			credencialesDeUsuario.setClaveSecreta(claveBus);
			
			ManejadorTablasCorp manejadorTablasCorp= new ManejadorTablasCorp(this._app,	credencialesDeUsuario);
			ArrayList<String> querys=new ArrayList<String>();
			
			TCGG tcgg=new TCGG();		
			tcgg.setEntidad(tabCorpEntidad);
			tcgg.setIdioma(tabCorpIdioma);
			tcgg.setClaveTabGeneral(tabCorpClaveTabGeneral);
			tcgg.setNumeroRegistros(tabCorpNumeroRegistros);
			tcgg.setTipoAcceso(tabCorpTipoAcceso);
			tcgg.setDatosIni(tabCorpDatosIni);
			
			try {
				//Consulta de Paises
				tcgg.setTablaGeneral(tabCorpTablaGeneralPaises);
				TCGGRespuesta tcggPaises = manejadorTablasCorp.getTCGG(tcgg, usuarioBus, direccionIpstr);
				
				//Insertar Paises
				querys.add(paisDAO.deletePaises());
				querys.addAll(paisDAO.insertarPaises(tcggPaises.getArrayTCMGen1()));
				
				//Ejecutar querys
				paisDAO.ejecutarStatementsBatch(querys);
				querys.clear();
			} catch (Exception e) {
				logger.error("Error en la carga de tablas corporativas (Paises): "+e.getMessage(),e);
			}	
			
			try {
				//Consulta de Estados
				tcgg.setTablaGeneral(tabCorpTablaGeneralEstados);
				TCGGRespuesta tcggEstados = manejadorTablasCorp.getTCGG(tcgg, usuarioBus, direccionIpstr);
				//Consulta de Ciudades
				tcgg.setTablaGeneral(tabCorpTablaGeneralCiudades);
				TCGGRespuesta tcggCiudades = manejadorTablasCorp.getTCGG(tcgg, usuarioBus, direccionIpstr);
				
				//Insertar estados
				querys.add(estadoDAO.deleteEstados());
				querys.addAll(estadoDAO.insertarEstados(tcggEstados.getArrayTCMGen1()));
				//Insertar ciudades
				querys.add(ciudadDAO.deleteCiudades());
				querys.addAll(ciudadDAO.insertarCiudades(tcggCiudades.getArrayTCMGen1()));
				
				//Ejecutar querys
				ciudadDAO.ejecutarStatementsBatch(querys);
			} catch (Exception e) {
				logger.error("Error en la carga de tablas corporativas (Estado y Ciudad): "+e.getMessage(),e);
			}							
			logger.info("** Fin del proceso automatico de carga de tablas Corporativas**");
		}//fin try
		catch (Exception e) {
			try {
				logger.error("Error en la carga de tablas corporativas: "+e.getMessage(),e);
			} catch (Exception e1) {
				e.printStackTrace();
			}
		}//fin catch
	}//fin execute
	
}//fin clase
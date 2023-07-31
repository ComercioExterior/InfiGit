package models.cierre_sistema.configuracion_cierre;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import megasoft.AbstractModel;

import org.apache.log4j.Logger;

import com.bdv.infi.dao.AuditoriaDAO;
import com.bdv.infi.dao.CierreSistemaDAO;
import com.bdv.infi.data.Auditoria;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.util.Utilitario;

/**
 * Clase encargada de recuperar datos necesarios para la construcci&oacute;n del fitro de busqueda de la Consulta de Titulos en Custodia de los Clientes
 * 
 * @author Erika Valerio, Megasoft Computaci&oacute;n, nvisbal
 */
public class Procesar extends AbstractModel {

	private CierreSistemaDAO cierreSistemaDAO=null;
	private AuditoriaDAO auditoriaDAO=null;
	
	private String fechaPreCierre;
	private String fechaSistema;
	
	private Date fechaPreCierreDate;
	private Date fechaSistemaDate;
	
	private long diasTranscurridos;
	
	private com.bdv.infi.dao.Transaccion transaccion;
	private ArrayList<String> querys=new ArrayList<String>();
	
	private String DETALLE_AUDITORIA;
	
	private Logger logger = Logger.getLogger(Procesar.class);
	/**
	 * Ejecuta la transaccion del modelo
	 */
	
	
	
	public void execute() throws Exception {		
	
		try{
	
			DETALLE_AUDITORIA="Configuracion de fecha pre-cierre: "+_req.getParameter("fecha_precierre");
			
			fechaPreCierre=_req.getParameter("fecha_precierre");	
			fechaPreCierreDate=Utilitario.StringToDate(fechaPreCierre, ConstantesGenerales.FORMATO_FECHA);
			
			cierreSistemaDAO=new CierreSistemaDAO(_dso);
			auditoriaDAO=new AuditoriaDAO(_dso);
			
			//Configuracion del objeto para el proceso de auditoria
			Auditoria auditoria= new Auditoria();
			auditoria.setDireccionIp(_req.getRemoteAddr());
			
			auditoria.setFechaAuditoria(Utilitario.DateToString(new Date(),ConstantesGenerales.FORMATO_FECHA));
			auditoria.setUsuario(this.getUserName());			
			auditoria.setPeticion(ConstantesGenerales.PETICION_CONFIGURACION_FECHA_PRECIERRE);
			auditoria.setDetalle(DETALLE_AUDITORIA);
						
			cierreSistemaDAO.listarFechaSistema();
			if(cierreSistemaDAO.getDataSet().count()>0){
				cierreSistemaDAO.getDataSet().first();
				while(cierreSistemaDAO.getDataSet().next()){
					fechaSistema=cierreSistemaDAO.getDataSet().getValue("FECHA_SISTEMA");
					fechaSistemaDate=Utilitario.StringToDate(fechaSistema,ConstantesGenerales.FORMATO_FECHA3);
				}
			}
			
			diasTranscurridos=Utilitario.caculadorDiferenciaDiasDeFechas(fechaSistemaDate,fechaPreCierreDate);
			
			
			
			transaccion = new com.bdv.infi.dao.Transaccion(_dso);
			transaccion.begin();
			
			Statement s =transaccion.getConnection().createStatement();
			
			querys.add(cierreSistemaDAO.actualizarFechaPreCierre(fechaPreCierre,diasTranscurridos,ConstantesGenerales.DESACTIVACION_CIERRE_SISTEMA));			
			querys.add(auditoriaDAO.insertRegistroAuditoria(auditoria));
			
			for (String element : querys) {
				s.addBatch(element);				
			}						
			s.executeBatch();
			
			transaccion.getConnection().commit();
				
		}catch(SQLException ex){
			transaccion.rollback();
			logger.error("Ha ocurrido un error en el proceso de CONFIGURACION DE CIERRE APLICACION : " + ex.getMessage());
			System.out.println("Ha ocurrido un error en el proceso de CONFIGURACION DE CIERRE APLICACION : " + ex.getMessage());
			throw new Exception("Ha ocurrido un error en el proceso de CONFIGURACION DE CIERRE APLICACION : " + ex.getMessage());
		}finally{								
			if(transaccion.getConnection()!=null){
				transaccion.getConnection().close();
			}
		
		}
	
	}

	
	
	

}

package models.cierre_sistema.reprocesamiento_cierre;

import megasoft.AbstractModel;
import megasoft.db;

import org.apache.log4j.Logger;

import com.bdv.infi.dao.CierreSistemaDAO;
import com.bdv.infi.logic.cierre_sistema.CierreSistema;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

/**
 * Clase encargada de recuperar datos necesarios para la construcci&oacute;n del fitro de busqueda de la Consulta de Titulos en Custodia de los Clientes
 * 
 * @author Erika Valerio, Megasoft Computaci&oacute;n, nvisbal
 */
public class Procesar extends AbstractModel {

	private String DETALLE_AUDITORIA;
	
	private Logger logger = Logger.getLogger(Procesar.class);
	/**
	 * Ejecuta la transaccion del modelo
	 */
	
	public void execute() throws Exception {		
	
		try{
	
			DETALLE_AUDITORIA="Inicio de proceso de Cierre Manual de Sistema. Tipo de Cierre: "+_req.getParameter("tipo_cierre");
			
			//Verificar si es reprocesamiento por falla:
			//Actualizar indicador de fallas de cierre, dejandolo limpio para iniciar el reproceso
			CierreSistemaDAO cierreSistemaDAO = new CierreSistemaDAO(_dso);
			if(_req.getParameter("tipo_reprocesamiento")!=null && _req.getParameter("tipo_reprocesamiento").equals("1")){
				db.exec(_dso, cierreSistemaDAO.limpiarCierreActivoConFallas());
			}					
			
			//******LLAMADA MANUAL AL CIERRE DEL SISTEMA, SE DISPARARA EL HILO CORRESPONDIENTE****
			reprocesarCierreSistema();
				
		}catch(Exception ex){		
			logger.error("Ha ocurrido un error en el reproceso manual del cierre del sistema: " + ex.getMessage());
			throw new Exception("Ha ocurrido un error en el reproceso manual del cierre del sistema: " + ex.getMessage());
		}
	}
	
	/**
	 * Ejecuta el hilo para el cierre de sistema
	 * @throws InterruptedException
	 */
	private void reprocesarCierreSistema() throws InterruptedException {
		
		Thread t = new Thread(new CierreSistema(_dso, _req.getRemoteAddr(), getUserName(), DETALLE_AUDITORIA,ConstantesGenerales.TIPO_CIERRE_MANUAL));
		t.start();
		t.join();		
	}	

}

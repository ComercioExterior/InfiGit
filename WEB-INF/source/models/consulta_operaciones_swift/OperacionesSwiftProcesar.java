/**
 * 
 */
package models.consulta_operaciones_swift;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import megasoft.AppProperties;
import megasoft.DataSet;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;

import org.apache.log4j.Logger;

import com.bdv.infi.dao.OperacionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaz_swift.FactorySwift;
import com.bdv.infi.logic.interfaz_swift.FactorySwiftSitme;
import com.bdv.infi.util.Utilitario;

/**
 * Clase encargada de armar las ordenes con sus respectivas operaciones para ser enviadas hacia <b>SWIFT</b><br>
 */

public class OperacionesSwiftProcesar extends MSCModelExtend{
	private Logger logger = Logger.getLogger(OperacionesSwiftProcesar.class);
	@Override
	public void execute() throws Exception {
		DataSet _error = new DataSet();
		try {	
			logger.info("Procesar operaciones Swift");
			DataSource _dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));
			OperacionDAO operacionDAO = new OperacionDAO(_dso);
			ServletContext _app = null;
			FactorySwift swift = null;
			FactorySwiftSitme swiftSitme = null;
			SimpleDateFormat formato = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
			Date fechaDesde=null;
			Date fechaHasta=null;
			String ordenesId=(String)_req.getParameter("idOrdenes"); //Id de orden-operacion
			String seleccion=(String)_req.getParameter("seleccion"); //Tipo de envio: Operaciones seleccionadas o Todas las operaciones
			String fechaDesdeString=(String)_req.getParameter("fechaDesde");
			String fechaHastaString=(String)_req.getParameter("fechaHasta");
			String tipoProducto=_req.getParameter("tipoProducto");
			String tipoFiltro=_req.getParameter("tipoFiltro");
			fechaDesde = formato.parse(fechaDesdeString);
			fechaHasta = formato.parse(fechaHastaString);
					
			logger.info("fechas: "+_req.getParameter("fechaDesde")+_req.getParameter("fechaHasta"));
			
			/*if(tipoProducto.equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA)){
				if(seleccion.equalsIgnoreCase("todos")){
					//Obtener todas las operaciones 
					operacionDAO.listarMensajesSwift(fechaDesde, fechaHasta, 0, ConstantesGenerales.STATUS_EN_ESPERA,tipoProducto,_record.getValue("transaccion_id"));
				}else{
					//Obtener las operaciones seleccionadas por ids de orden-operacion
				operacionDAO.listarSwiftPorId(ordenesId, ConstantesGenerales.STATUS_EN_ESPERA);	//ITS-803: Duplicidad de operaciones SWIFT 	
				}
				//Procesar y enviar las ordenes SUBASTA
				swift = new FactorySwift(_dso, _app);	
				swift.procesarOperacionesSwift(operacionDAO.getDataSet(), _dso);
			}else*/
			
			if(seleccion.equalsIgnoreCase("todos")){
				//Obtener todas las operaciones 
				operacionDAO.listarMensajesSwiftInstruccion202(fechaDesde, fechaHasta, 0, ConstantesGenerales.STATUS_EN_ESPERA,tipoProducto,_req.getParameter("transaccion_id"));
			}else{
				String ordenesIdEspecificos = "";
				if(tipoFiltro.equals("INCLUIR")){
					ordenesIdEspecificos=ordenesId;
				}
				if(tipoFiltro.equals("EXCLUIR")){
					//descargando de session el dataSet de 'lista_opcion_todos'
					DataSet listaTodos =  new DataSet();
					listaTodos = getSessionDataSet("lista_ConsultaOperacionesSwiftBrowse");
					boolean flag = true;
					while(listaTodos.next()){
						String idOperacion = listaTodos.getValue("ordene_operacion_id");
						if(ordenesId.indexOf(idOperacion) == -1){
							if(flag){
								ordenesIdEspecificos+=idOperacion;
								flag = false;
							}else
								ordenesIdEspecificos+=","+idOperacion;
						}
					}
				}
				//Obtener las operaciones seleccionadas por ids de orden-operacion
				operacionDAO.listarSwiftPorIdInstruccion202(ordenesIdEspecificos,tipoProducto);			
			}
			
			if(tipoProducto.equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA)){
				//Procesar y enviar las ordenes SUBASTA
				swift = new FactorySwift(_dso, _app);	
				swift.setTipoProducto(tipoProducto);
				swift.procesarOperacionesSwift(operacionDAO.getDataSet(), _dso);
			}else
			{
				//Procesar y enviar las ordenes SITME
				swiftSitme = new FactorySwiftSitme(_dso, _app);	
				swiftSitme.setTipoProducto(tipoProducto);
				swiftSitme.procesarOperacionesSwift(operacionDAO.getDataSet(), _dso);
			}
						
			
			storeDataSet("error",_error);
			//limpiando session
			setSessionDataSet("lista_ConsultaOperacionesSwiftBrowse", null);
			
		} catch (Exception e) {
			
			setSessionDataSet("lista_ConsultaOperacionesSwiftBrowse", null);
			try {
				_error.append("error", java.sql.Types.VARCHAR);
				_error.addNew();
				_error.setValue("error","Se ha interrumpido el procesamiento de operaciones SWIFT "+ e.getMessage());
				
				storeDataSet("error",_error);
				logger.error(e.getMessage()+ Utilitario.stackTraceException(e));
			} catch (Exception e1) {
				e.printStackTrace();
			}
			throw new Exception("Ha ocurrido un error al procesar las operaciones SWIFT: "+e.getMessage());
			
		}	
		
	}//fin execute

}//fin ConsultaOperacionesSwiftBrowse

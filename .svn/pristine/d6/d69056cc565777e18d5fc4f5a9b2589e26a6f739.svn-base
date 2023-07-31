/**
 * 
 */
package models.intercambio.recepcion.cruce_sicad_II.consulta_cruce;

import java.sql.SQLException;
import javax.sql.DataSource;
import megasoft.AppProperties;
import megasoft.DataSet;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;
import org.apache.log4j.Logger;

import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.OrdenesCrucesDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

/**
 * Clase encargada de armar las ordenes con sus respectivas operaciones para ser enviadas hacia <b>SWIFT</b><br>
 */

public class Rechazar extends MSCModelExtend{
	
	//private DataSet datosFilter;
	private String crucesIdEspecificos = "";
	long idUnidad;
	long idCliente;
	String idOrden;
	String idEjecucion;
	String status = null;
	String statusP = null;
	String msgOut = null;
	String indTitulo = null;
	boolean pFlag = true;
	private Logger logger = Logger.getLogger(Rechazar.class);
	
	@Override
	public void execute() throws Exception {
		
		DataSet record = (DataSet) _req.getSession().getAttribute("filtro_ConsultaCruces");
		
		idUnidad = Long.valueOf(record.getValue("idUnidadF")==null?"0":record.getValue("idUnidadF"));
		idCliente = Long.valueOf(record.getValue("idClienteF")==null?"0":record.getValue("idClienteF"));
		idEjecucion = record.getValue("idEjecucionF");
		status = record.getValue("statusF");
		idOrden = record.getValue("idOrdenF");
		statusP = record.getValue("statusP");
		indTitulo = record.getValue("indTitulo");		
		DataSet datosFilter = new DataSet();
		datosFilter.append("mensaje", java.sql.Types.VARCHAR);
		datosFilter.addNew();
		
		DataSet listaTodos =  new DataSet();

		try {	
			logger.info("Rechazar Ordenes de Cruces Sicad ");
			DataSource _dso = db.getDataSource(AppProperties.getProperty(ConstantesGenerales.DATASOURCE_PRINCIPAL_INFI));
			
			OrdenesCrucesDAO consCruces = new OrdenesCrucesDAO(_dso);
			OrdenDAO ordenDAO = new OrdenDAO(_dso);

			String idCruces=(String)_req.getParameter("idCruces"); 
			String seleccion=(String)_req.getParameter("seleccion"); 
			String tipoFiltro=_req.getParameter("tipoFiltro");								
			if(seleccion.equalsIgnoreCase("todos")){

				//NM25287	10/06/2014	ELIMINACION DE CRUCES POR FILTROS DE CONSULTA EN LUGAR DE IDS DE CRUCES. TTS-443.SICAD2 
				ordenDAO.actualizarEstatusOrdenBcvIn(idUnidad, status, idCliente, idOrden, statusP, idEjecucion, indTitulo);
				consCruces.eliminarCruce(idUnidad, status, idCliente, idOrden, statusP, idEjecucion, indTitulo);
				
				/*//Obtener todas las operaciones 
				crucesIdEspecificos = "";
				
				consCruces.consultarIdCruces(idUnidad, status, idCliente, idOrden, statusP, idEjecucion, indTitulo);//(DataSet)_req.getSession().getAttribute("exportar_excel");
				
				listaTodos = consCruces.getDataSet(); 
				
				boolean flag = true;
				
				while(listaTodos.next()){
					String idOperacion = listaTodos.getValue("ID_CRUCE");
					String procesado = listaTodos.getValue("CRUCE_PROCESADO");
					
					if(procesado.equalsIgnoreCase("0")){
						if(flag){
							crucesIdEspecificos+=idOperacion;
							flag = false;
						} else {
							crucesIdEspecificos+=","+idOperacion;							
						}
					}else{
						//System.out.println("--->1 por aca paso");
						msgOut = "Uno o más registros seleccionados ya se encuentran Procesados. Porfavor verifique su selección";
						datosFilter.setValue("mensaje", msgOut);
						storeDataSet("datosFilter", datosFilter);
						pFlag = false;
						break;
					}
				}
				if(pFlag){
					consCruces.eliminarCruce(crucesIdEspecificos);
					msgOut = "Los registros se Rechazaron satisfacctoriamente";
				}else{
					datosFilter.setValue("mensaje", msgOut);
					storeDataSet("datosFilter", datosFilter);
				}
				*/
			}else{
				crucesIdEspecificos = "";
				if(tipoFiltro.equals("INCLUIR")){
					crucesIdEspecificos=idCruces;
					
					consCruces.consultarIdCruces(idUnidad, status, idCliente, idOrden, statusP, idEjecucion, indTitulo);//(DataSet)_req.getSession().getAttribute("exportar_excel");
					listaTodos = consCruces.getDataSet();
					

					while(listaTodos.next()){
						String idOperacion = listaTodos.getValue("ID_CRUCE");
						String procesado = listaTodos.getValue("CRUCE_PROCESADO");
						String verificadoBCV = listaTodos.getValue("ORDENE_ESTATUS_BCV");
						
						if(idCruces.indexOf(idOperacion) != -1){
							if(!procesado.equalsIgnoreCase("0")){								
								msgOut = "Uno o más registros seleccionados ya se encuentran Procesados. Porfavor verifique su selección";
								datosFilter.setValue("mensaje", msgOut);
								storeDataSet("datosFilter", datosFilter);
								pFlag = false;
								break;
							//NM26659 15/04/2015 TTS-491 WEB SERVICE ALTO VALOR (Validacion de registros aprobados por BCV) 
							}else if(verificadoBCV.equals(ConstantesGenerales.VERIFICADA_APROBADA)){//Si el pacto fue verificado y aceptado por BCV no permite realizar la eliminacion del registro
								msgOut = "Uno o más registros seleccionados han sido enviados y aprobados por el BCV. Porfavor verifique su selección";
								datosFilter.setValue("mensaje", msgOut);
								storeDataSet("datosFilter", datosFilter);
								pFlag = false;
								break;
							}
						}
					}
				}
				if(tipoFiltro.equals("EXCLUIR")){

					consCruces.consultarIdCruces(idUnidad, status, idCliente, idOrden, statusP, idEjecucion, indTitulo);//(DataSet)_req.getSession().getAttribute("exportar_excel");
					
					listaTodos = consCruces.getDataSet();
					
					boolean flag = true;
					while(listaTodos.next()){
						String idOperacion = listaTodos.getValue("ID_CRUCE");
						String procesado = listaTodos.getValue("CRUCE_PROCESADO");
						
						if(idCruces.indexOf(idOperacion) == -1){
							if(procesado.equalsIgnoreCase("0")){
								if(flag){
									crucesIdEspecificos+=idOperacion;
									flag = false;
								}else{crucesIdEspecificos+=","+idOperacion;}
							}else{								
								msgOut = "Uno o más registros seleccionados ya se encuentran Procesados. Porfavor verifique su selección";
								datosFilter.setValue("mensaje", msgOut);
								storeDataSet("datosFilter", datosFilter);
								pFlag = false;
								break;
							}
						}
					}
				}
				if(pFlag){
					ordenDAO.actualizarEstatusOrdenBcvIn(crucesIdEspecificos);
					consCruces.eliminarCruce(crucesIdEspecificos);
					msgOut = "Los registros se Rechazaron satisfacctoriamente";
				}else{
					datosFilter.setValue("mensaje", msgOut);
					storeDataSet("datosFilter", datosFilter);
				}
			}
		}catch(SQLException sqlEx){
	//		transaccion.getConnection().rollback();
			_record.addError("Nombre","Ha ocurrido un error de tipo SQLException en el proceso de Eliminacion de los cruces seleccionados" + "Error:"  + sqlEx.getMessage());
			logger.error("Ha ocurrido un error de tipo SQLException en el proceso de Eliminacion de los cruces seleccionados" + "Error:"  + sqlEx.getMessage());
			System.out.println("Ha ocurrido un error de tipo SQLException en el proceso de Eliminacion de los cruces seleccionados" + "Error: " + sqlEx.getMessage());
			
		}catch (Exception ex){
		//	transaccion.getConnection().rollback();
			_record.addError("Nombre","Ha ocurrido un error de tipo SQLException en el proceso de Eliminacion de los cruces seleccionados" + "Error:"  + ex.getMessage());
			logger.error("Ha ocurrido un error de Inesperado en el proceso de Eliminacion los cruces seleccionados" + "Error: " + ex.getMessage());
			System.out.println("Ha ocurrido un error de Inesperado en el proceso de Eliminacion de los cruces seleccionados" + "Error: " + ex.getMessage());
		}
	//Se publica el dataset para mensaje

		datosFilter.setValue("mensaje", msgOut);
		storeDataSet("datosFilter", datosFilter);
	}//fin execute
}//fin Rechazar

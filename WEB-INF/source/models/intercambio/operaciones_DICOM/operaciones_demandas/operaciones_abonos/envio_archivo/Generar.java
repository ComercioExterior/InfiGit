package models.intercambio.operaciones_DICOM.operaciones_demandas.operaciones_abonos.envio_archivo;

import java.util.ArrayList;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.ArchivosOpsDAO;
import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.UsuarioDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ControlProcesosOps;
import com.bdv.infi.logic.interfaces.ControlProcesosOpsMonedaExtranjera;
import com.bdv.infi.logic.interfaces.ControlProcesosOpsMonedaLocal;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TransaccionFinanciera;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;
import com.bdv.infi.logic.interfaz_ops.AdjudicacionEnvioSubastaDivisas;
import com.bdv.infi.logic.interfaz_ops.EnvioArchivosOps;


public class Generar extends MSCModelExtend {
	private ControlProcesosOps controlProceso;
	private ArchivosOpsDAO archivosOpsDAO;
	private String idUniInv;
	private String consultaSolicitudes;
	
	/**
	 * Ejecuta la transaccion del modelo buscando las unidades de inversión
	 */
	public void execute() throws Exception {
		
		UsuarioDAO usu				= new UsuarioDAO(_dso);		
		String idOrdenes = _req.getParameter("idOrdenes");
		String seleccion=(String)_req.getParameter("seleccion");
		String tipoFiltro=_req.getParameter("tipoFiltro");		
		String ordenesIdEspecificos = "";		
		ArrayList<String> statusUnidadInv=new ArrayList<String>();		
		
		if(seleccion.equalsIgnoreCase("todos")){			
			ordenesIdEspecificos=null;
		}else{
			if(tipoFiltro.equals("INCLUIR")){
				ordenesIdEspecificos=idOrdenes;
			}
			if(tipoFiltro.equals("EXCLUIR")){
				//descargando de session el dataSet de 'lista_opcion_todos'
				DataSet listaTodos =  new DataSet();
				listaTodos = getSessionDataSet("listarUnidadesParaAbonoBatchDicom");
				boolean flag = true;
				while(listaTodos.next()){
					String idOperacion = listaTodos.getValue("ordene_id");
					if(idOrdenes.indexOf(idOperacion) == -1){
						if(flag){
							ordenesIdEspecificos+=idOperacion;
							flag = false;
						}else
							ordenesIdEspecificos+=","+idOperacion;
					}
				}
			}
		}
		
		/***************/
		int transaccionFinanciera=1;//Tipo de transaccion que define si es capital o comision (cero es ambas)		
		statusUnidadInv.add(UnidadInversionConstantes.UISTATUS_ADJUDICADA);
		String[]trnfTipo={TransaccionFinanciera.CREDITO};
		
		consultaSolicitudes=archivosOpsDAO.envioOperacionesDICOM(idOrdenes,idUniInv,TransaccionNegocio.ORDEN_PAGO,StatusOrden.REGISTRADA,transaccionFinanciera,trnfTipo);
		
		Runnable EnvioArchivoAdjudicacionSubasta= new EnvioArchivosOps(_dso,ParametrosSistema.INTERFACE_OPS,ParametrosSistema.INTERFACE_OPICS,Integer.parseInt(usu.idUserSession(getUserName())),this.getUserName(),consultaSolicitudes,controlProceso);
		new Thread(EnvioArchivoAdjudicacionSubasta).start();
		/***************/

	}

	public boolean isValid() throws Exception {
		boolean valido = true;

		ProcesosDAO procesosDAO = new ProcesosDAO(_dso);
		ControlArchivoDAO controlArchivoDAO = new ControlArchivoDAO(_dso);
		archivosOpsDAO=new ArchivosOpsDAO(_dso);
		
		this.controlProceso=ControlProcesosOpsMonedaExtranjera.PROCESO_DEMANDA_ABONO_DICOM.getInstance();
		idUniInv=_record.getValue("undinv_id");
		
		procesosDAO
		.listarProcesosActivosPorTransaccion(controlProceso.getListaProceso());
		if (procesosDAO.getDataSet().count() > 0) {
			_record
					.addError(
							"DICOM Abono Demanda Batch Subasta",
							"No se puede procesar la solicitud porque otra "
									+ "persona realizó esta acción y esta actualmente activa");
			valido = false;
		} else {

			
			controlArchivoDAO.listarCicloAbiertoPorTransaccion(controlProceso.getListaCiclo());			
			DataSet dataSet = controlArchivoDAO.getDataSet();
			if (dataSet.count() > 0){
				dataSet.next();
				_record
				.addError(
						"Proceso DICOM Abono Demanda Batch",
						"No se puede procesar la solicitud porque el ciclo de envío y recepción que no ha finalizado. ");
				valido = false;
			}else{
				//NM29643 14/08/2014 Se valida que no se encuentre en ejecucion la liquidacion			
				procesosDAO.listarPorTransaccionActiva(TransaccionNegocio.LIQUIDACION);
				
				if (procesosDAO.getDataSet().count() > 0) {
					_record.addError("Proceso DICOM Cobro Demanda",
						" No se puede procesar la solicitud porque un proceso de liquidación "
						+"se encuentra en ejecución");
					valido = false;			
				}				
			}			
		
		}
				
		return valido;
	}// fin isValid
}

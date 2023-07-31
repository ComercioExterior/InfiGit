package models.intercambio.certificados_ORO.operaciones_ofertas.operaciones_abonos.envio_archivo;

import java.util.ArrayList;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.UsuarioDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TransaccionFija;
import com.bdv.infi.logic.interfaces.TransaccionFinanciera;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;
//import com.bdv.infi.logic.interfaz_ops.EnvioBatchOperacionesDicom;


public class Generar2 extends MSCModelExtend {
	
	
	/**
	 * Ejecuta la transaccion del modelo buscando las unidades de inversión
	 */
	public void execute() throws Exception {
		
		UsuarioDAO usu				= new UsuarioDAO(_dso);		
		String idOrdenes = _req.getParameter("idOrdenes");
		String seleccion=(String)_req.getParameter("seleccion");
		String tipoFiltro=_req.getParameter("tipoFiltro");		
		String ordenesIdEspecificos = "";
		String tipoProducto=_req.getParameter("undinv_tipo_prod");
		String transaccionNegocio ="";
		String cicloEnvio ="";		
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
				listaTodos = getSessionDataSet("listarUnidadesParaAbonoOfetaORO");
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
		
		/*int transaccionFinanciera=TransaccionFija.CAPITAL_SIN_IDB;//Tipo de transaccion que define si es capital o comision (cero es ambas)
		statusUnidadInv.add(UnidadInversionConstantes.UISTATUS_ADJUDICADA);
		cicloEnvio = TransaccionNegocio.CICLO_BATCH_ABONO_DICOM_OFERTA;
		transaccionNegocio = TransaccionNegocio.PROC_BATCH_ABONO_DICOM_ENVIO_OFERTA;
		
		String rutaEnvioPasivos=ParametrosSistema.RUTA_SUBASTA_ADJ_ENVIO;
		String rutaRespaldoPasivos=ParametrosSistema.RUTA_SUBASTA_ADJ_RESPALDO;
		String archivoPasivos=ParametrosSistema.NOMBRE_ARCH_SUBASTA_ADJ;
		
		String[]trnfTipo={TransaccionFinanciera.CREDITO};
		String[]ciclosValidar={TransaccionNegocio.CICLO_BATCH_COBRO_DICOM_DEMANDA,TransaccionNegocio.CICLO_BATCH_ABONO_DICOM_OFERTA,TransaccionNegocio.CICLO_BATCH_SUBASTA};//Ciclos asociados a canal BP3.BDBJ8001 Abono / Cobros en Moneda Extranjera
		String[]procesosValidar={TransaccionNegocio.PROC_BATCH_ABONO_DICOM_ENVIO_OFERTA,TransaccionNegocio.PROC_BATCH_COBRO_DICOM_ENVIO_DEMANDA,TransaccionNegocio.PROC_BATCH_ADJ_SUBASTA_ENVIO};//Procesos asociados a canal BP3.BDBJ8001 Abono / Cobros en Moneda Extranjera
		//envioOperacionesDICOM(String idOrdenes,String idUnidadInv,String transaccion,String statusOrden,String ... trnfTipo)
		Runnable cobroDemandaDicomEnvio= new EnvioBatchOperacionesDicom(_dso, Integer.parseInt(usu.idUserSession(getUserName())), this.getUserName()																																																
																		, Integer.parseInt(_req.getParameter("undinv_id")), ordenesIdEspecificos, transaccionNegocio, cicloEnvio, statusUnidadInv, ConstantesGenerales.OPS_MONEDA_NACIONAL,TransaccionNegocio.ORDEN_PAGO, StatusOrden.REGISTRADA,transaccionFinanciera,rutaEnvioPasivos,rutaRespaldoPasivos,archivoPasivos,trnfTipo,
																		ciclosValidar,procesosValidar);
		new Thread(cobroDemandaDicomEnvio).start();
		setSessionDataSet("listarUnidadesParaAbonoOfetaDicom", null);
		*/
	}

	public boolean isValid() throws Exception {
		boolean valido = true;
		
		//Verificacion de todos lo proceso asociados al canal de bolivares
		//String ciclosValidar="'"+TransaccionNegocio.CICLO_BATCH_COBRO_DICOM_DEMANDA+"'".concat(",").concat("'"+TransaccionNegocio.CICLO_BATCH_ABONO_DICOM_OFERTA+"'").concat(",").concat("'"+TransaccionNegocio.CICLO_BATCH_SUBASTA+"'");
	//	String procesoValidar="'"+TransaccionNegocio.PROC_BATCH_ABONO_DICOM_ENVIO_OFERTA+"'".concat(",'"+TransaccionNegocio.PROC_BATCH_COBRO_DICOM_ENVIO_DEMANDA+"',").concat("'"+TransaccionNegocio.PROC_BATCH_ADJ_SUBASTA_ENVIO+"'");
		
		ProcesosDAO procesosDAO = new ProcesosDAO(_dso);
		//procesosDAO.listarPorTransaccionActivaSubastaDivisasSubastaTitulo(procesoValidar);
		if (procesosDAO.getDataSet().count() > 0) {
			_record
					.addError(
							"Abono Oferta ORO",
							"No se puede procesar la solicitud porque otra "
									+ "persona realizó esta acción y esta actualmente activa");
			valido = false;
		}else{
			//Verifica si se espera una recepción de archivo
			ControlArchivoDAO controlArchivoDAO = new ControlArchivoDAO(_dso);
			
			//controlArchivoDAO.listarEnvioPorRecepcionBatchSubastaDivisas(ciclosValidar);			
			DataSet dataSet = controlArchivoDAO.getDataSet();
			if (dataSet.count() > 0){
				dataSet.next();
				_record
				.addError(
						"Proceso Abono Demanda ORO ",
						"No se puede procesar la solicitud porque el ciclo de envío y recepción "  + (dataSet.getValue("in_recepcion").equals("0")?"de adjudicación ":"de liquidación ") + " no ha finalizado. ");
				valido = false;
			}else{//TODO Revisar si esta validacion aplica 18/07/2017
				//NM29643 14/08/2014 Se valida que no se encuentre en ejecucion la liquidacion
				procesosDAO.listarPorTransaccionActiva(TransaccionNegocio.LIQUIDACION);

				if (procesosDAO.getDataSet().count() > 0) {
					_record.addError("Proceso Abono Oferta ORO ",
						" No se puede procesar la solicitud porque un proceso de liquidación "
						+"se encuentra en ejecución");
					valido = false;
				}
			}
		}
		return valido;
	}// fin isValid
}

package models.intercambio.recepcion.cruce_sicad_II.cierre_cruce;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import megasoft.DataSet;
import megasoft.Logger;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.OrdenesCrucesDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.TransaccionFijaDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.dao.UsuarioDAO;
import com.bdv.infi.logic.cruces_ordenes.CruceOrdenesSICAD2;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TransaccionFija;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

public class Procesar extends MSCModelExtend{

	private TransaccionFijaDAO transaccionFijaDAO;
	private OrdenesCrucesDAO ordenesCrucesDAO;
	private ProcesosDAO procesosDAO;
	private UnidadInversionDAO unidadInversionDAO;
	
	private String idUnidadInversion;
	private String nombreUnidadInversion;	
	private String instrumentoFinanciero;
	private String vehiculosTomaOrden;
	private String marcadorIdb;
	
	private String tipoProducto;
	private String transaccion;
	private String tipoNegocio="";
	
	ArrayList<String> ordenesPorCancelar=new ArrayList<String>();
	String stringOrdenesCancelacion=new String();
	DataSet _data= new DataSet();
	OrdenDAO ordenDAO;
	
	private String queryCancelacionOrdenes;
	private com.bdv.infi.dao.Transaccion sqlTransaccion;
	private Statement s; 
	private boolean simadiAltoValor=false;
	
	public void execute() throws Exception {
		

		
		try {
			UsuarioDAO usuarioDAO 	= new UsuarioDAO(_dso);
			int usuario = Integer.parseInt((usuarioDAO.idUserSession(getUserName())));			
			//Se verifica si la unidad de inversion es de tipo inventario			
			CruceOrdenesSICAD2 cruceOrdenesSICAD2 = new CruceOrdenesSICAD2(_dso,usuario,_app,_req,idUnidadInversion,tipoProducto,transaccion,getUserName(),tipoNegocio);
			Thread t = new Thread(cruceOrdenesSICAD2);
			t.start();
	
		} catch (Throwable e) {			
			Logger.error(this,e.getMessage(),e);
		}finally{
			_req.getSession().removeAttribute("opics_data");
			_req.getSession().removeAttribute("ordenes");
			_req.getSession().removeAttribute("unidad");
			_req.getSession().removeAttribute("blotter");
			_req.getSession().removeAttribute("status");
			_req.getSession().removeAttribute("nombre_unidad");			
		}//fin finally
	
	}
	
	public boolean isValid() throws Exception {
				
		boolean flag=true;
		
	
		
		nombreUnidadInversion=_req.getParameter("ui_nombre");		
		idUnidadInversion=_req.getParameter("ui_id");
		instrumentoFinanciero=_req.getParameter("inst_financiero");
		vehiculosTomaOrden=_req.getParameter("vehiculos");
		marcadorIdb=_req.getParameter("marcador_idb");		
		transaccion=_req.getParameter("tipo_transaccion");
		tipoProducto=_req.getParameter("tipo_producto");
		tipoNegocio=_req.getParameter("tipo_negocio");
		
		ordenDAO=new OrdenDAO(_dso);
		ordenesCrucesDAO=new OrdenesCrucesDAO(_dso);
		procesosDAO=new ProcesosDAO(_dso);
		unidadInversionDAO=new UnidadInversionDAO(_dso);
		//Validacion de proceso de Cierre de Cruce Activo
		procesosDAO.listarPorTransaccionActiva(transaccion);
		if(procesosDAO.getDataSet().count()>0){			
			_record.addError("Para su Informacion"," Actualmente hay un proceso de Cierre " + transaccion + " activo. Debe esperar a que finalice para poder realizar otra ejecucion");
			return false;
		}
		//********************VERIFICACION DE CODIGOS DE OPERACION********************
		transaccionFijaDAO= new TransaccionFijaDAO(_dso);
		transaccionFijaDAO.listarTransaccionesFijasVehiIns(vehiculosTomaOrden, instrumentoFinanciero);
		String transaccionFija=null;
		if(transaccionFijaDAO.getDataSet().count()>0){
			transaccionFijaDAO.getDataSet().first();			
			while(transaccionFijaDAO.getDataSet().next()){
				transaccionFija=transaccionFijaDAO.getDataSet().getValue("TRNFIN_ID");
					//TRANSACCIONES QUE NO DEPENDE DEL IDB******				
					if(transaccionFija.equals(TransaccionFija.GENERAL_COMISION_DEB_MANEJO_MIXTO) || transaccionFija.equals(TransaccionFija.COBRO_COMISION_INVARIABLE_MANEJO_MIXTO)
					|| transaccionFija.equals(TransaccionFija.COMISION_DEB_TITULOS) || transaccionFija.equals(TransaccionFija.COMISION_DEB_DIVISAS)){
						if(transaccionFijaDAO.getDataSet().getValue("COD_OPERACION_CTE_DEB")==null || transaccionFijaDAO.getDataSet().getValue("COD_OPERACION_CTE_DEB").equals("")){
							flag=false;
							_record.addError("Para su Informacion","El codigo de Operacion DEBITO CLIENTE asociado a la Transaccion " + transaccionFija + " No esta configurado ");
						}
						
						if(transaccionFijaDAO.getDataSet().getValue("COD_OPERACION_CTE_CRE")==null || transaccionFijaDAO.getDataSet().getValue("COD_OPERACION_CTE_CRE").equals("")){
							flag=false;
							_record.addError("Para su Informacion","El codigo de Operacion CREDITO CLIENTE asociado a la Transaccion " + transaccionFija + " No esta configurado ");
						}
						
						if(transaccionFijaDAO.getDataSet().getValue("COD_OPERACION_CTE_BLO")==null || transaccionFijaDAO.getDataSet().getValue("COD_OPERACION_CTE_BLO").equals("")){
							flag=false;	
							_record.addError("Para su Informacion","El codigo de Operacion BLOQUEO CLIENTE asociado a la Transaccion " + transaccionFija + " No esta configurado ");
						}
					}				
					
				if(marcadorIdb.equals(com.bancovenezuela.comun.logic.interfaces.ConstantesGenerales.STATUS_ACTIVO)){//BUSQUEDA DE CODIGOS DE OPERACIONES CON IDB														
					if(transaccionFija.equals(TransaccionFija.GENERAL_CAPITAL_CON_IDB_MANEJO_MIXTO) || transaccionFija.equals(TransaccionFija.CAPITAL_CON_IDB_TITULOS) || transaccionFija.equals(TransaccionFija.CAPITAL_CON_IDB_DIVISAS)){
						if(transaccionFijaDAO.getDataSet().getValue("COD_OPERACION_CTE_DEB")==null || transaccionFijaDAO.getDataSet().getValue("COD_OPERACION_CTE_DEB").equals("")){
							flag=false;
							_record.addError("Para su Informacion","El codigo de Operacion DEBITO CLIENTE asociado a la Transaccion " + transaccionFija + " No esta configurado ");
						}
						
						if(transaccionFijaDAO.getDataSet().getValue("COD_OPERACION_CTE_CRE")==null || transaccionFijaDAO.getDataSet().getValue("COD_OPERACION_CTE_CRE").equals("")){
							flag=false;
							_record.addError("Para su Informacion","El codigo de Operacion CREDITO CLIENTE asociado a la Transaccion " + transaccionFija + " No esta configurado ");
						}
						
						if(transaccionFijaDAO.getDataSet().getValue("COD_OPERACION_CTE_BLO")==null || transaccionFijaDAO.getDataSet().getValue("COD_OPERACION_CTE_BLO").equals("")){
							flag=false;	
							_record.addError("Para su Informacion","El codigo de Operacion BLOQUEO CLIENTE asociado a la Transaccion " + transaccionFija + " No esta configurado ");
						}
					}					
				} else {//BUSQUEDA DE CODIGOS DE OPERACIONES SIN IDB
					if(transaccionFija.equals(TransaccionFija.GENERAL_CAPITAL_SIN_IDB_MANEJO_MIXTO) || transaccionFija.equals(TransaccionFija.CAPITAL_SIN_IDB_TITULOS) 
							|| transaccionFija.equals(TransaccionFija.CAPITAL_SIN_IDB_DIVISAS)){
						if(transaccionFijaDAO.getDataSet().getValue("COD_OPERACION_CTE_DEB")==null || transaccionFijaDAO.getDataSet().getValue("COD_OPERACION_CTE_DEB").equals("")){
							flag=false;
							_record.addError("Para su Informacion","El codigo de Operacion DEBITO CLIENTE asociado a la Transaccion " + transaccionFija + " No esta configurado ");
						}
						
						if(transaccionFijaDAO.getDataSet().getValue("COD_OPERACION_CTE_CRE")==null || transaccionFijaDAO.getDataSet().getValue("COD_OPERACION_CTE_CRE").equals("")){
							flag=false;
							_record.addError("Para su Informacion","El codigo de Operacion CREDITO CLIENTE asociado a la Transaccion " + transaccionFija + " No esta configurado ");
						}
						
						if(transaccionFijaDAO.getDataSet().getValue("COD_OPERACION_CTE_BLO")==null || transaccionFijaDAO.getDataSet().getValue("COD_OPERACION_CTE_BLO").equals("")){
							flag=false;	
							_record.addError("Para su Informacion","El codigo de Operacion BLOQUEO CLIENTE asociado a la Transaccion " + transaccionFija + " No esta configurado ");
						}
					}
				}
			}
		} else {//SI NO SE ENCUENTRAN REGISTROS EN LA TABLA INFI_TB_032_TRNF_FIJAS_VEHICU
			flag=false;
			_record.addError("Para su Informacion"," Debe realizar la configuracion de los codigos de operacion del instrumento financiero asociado a su Unidad de Inversion");
		}
		
		if(transaccion.equals(TransaccionNegocio.CRUCE_SICAD2_CLAVE_CIERRE)){
			
			//NM26659_29/04/2015 Modificacion para que se busquen solicitudes asociadas a la orden sin importar si la unidad esta activa
			//if(unidadInversionDAO.isUnidaInvActiva(idUnidadInversion)){						
			//Busqueda de ordenes con estatus RECIBIDA en la tabla SOLICITUDES_SITME
			//NM25287 CONTINGENCIA SIMADI 11/02/2015. Filtro por oferta y demanda
			ordenDAO.listarSolicitudesSITME(StatusOrden.RECIBIDA,null,false,null,ConstantesGenerales.PRODUCTO_DEMANDA,Long.parseLong(idUnidadInversion));		
				if(ordenDAO.getDataSet().count()>0){
					flag=false;	
					_record.addError("Para su informacion", "Existen solicitudes de Clavenet que no han sido exportadas al Sistemas INFI asociadas a la unidad de inversion seleccionada, por favor verifique");
				}
			//}
			//Metodo creado en requerimiento TTS-466_Calidad NM26659 (Solucion temporal de problema Duplicidad de ordenes en Exportacion de Ordenes)
			if(ordenDAO.ordenesduplicadasSICAD2Personal(idUnidadInversion)){
				//System.out.println("!! EXISTEN REGISTROS DUPLICADOS !! ");
				ordenDAO.getDataSet().first();
				int count=0;		
				while(ordenDAO.getDataSet().next()){								
					if(count>0){
						stringOrdenesCancelacion=stringOrdenesCancelacion.concat(",");
					}
					
					stringOrdenesCancelacion=stringOrdenesCancelacion.concat(ordenDAO.getDataSet().getValue("ordene_id"));
					++count;
				}
				queryCancelacionOrdenes=ordenDAO.actualizarEstatusOrdenesIn(stringOrdenesCancelacion,StatusOrden.CANCELADA,0);
				//System.out.println("****** QUERY -------> " + queryCancelacionOrdenes );
				try{
						sqlTransaccion = new com.bdv.infi.dao.Transaccion(_dso);
						sqlTransaccion.begin();
						s =sqlTransaccion.getConnection().createStatement();
						s.execute(queryCancelacionOrdenes);
						s.close();
						sqlTransaccion.getConnection().commit();
					}catch(SQLException sql){
						sql.printStackTrace();
						sqlTransaccion.getConnection().rollback();
					}catch(Exception ex){
						ex.printStackTrace();
						sqlTransaccion.getConnection().rollback();
					}finally{																
						if(s!=null){													
							s.close();																	
						}
						
						if(sqlTransaccion!=null){
							sqlTransaccion.closeConnection();
						}
					}								
			}			
		}		
			ordenesCrucesDAO.listarOrdenesSinMovimientoCrucePorIdUniInv(Long.parseLong(idUnidadInversion));										
			if(ordenesCrucesDAO.getDataSet().count()>0){						
				flag=false;				
				_record.addError("Para su informacion", "Existen Ordenes que no han sido ingresadas en el proceso de cruce, debe verificar dichas ordenes para ejecutar el proceso de  CIERRE DE CRUCE");			
			}

				

		return flag;
		//return true;
	}
	
}

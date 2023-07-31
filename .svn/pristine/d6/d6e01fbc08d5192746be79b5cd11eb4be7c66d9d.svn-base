package models.intercambio.recepcion.cruce_sicad_II.cierre_cruce;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.OrdenesCrucesDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.ProcesosDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

public class Browse extends MSCModelExtend{

	private OrdenesCrucesDAO ordenesCrucesDAO;
	private long idUnidadInversion;
	private String nombreUnidadInversion;
	private String estatusUnidadInversion;
	private String idInstrumentoFinanciero;
	private String unidadConIDB;
	private String vehiculosTomaOrden=new String();
	
	private DataSet _ordenesCruces;
	private DataSet _resument;
	DataSet _data= new DataSet();
	OrdenDAO ordenDAO;
	UnidadInversionDAO unidadInversionDAO;

	private String mensajeMenu;//="mensaje_menu";
	private String tipoProducto;//="mensaje_menu";
	private String tipoTransaccion;
	private String tipoNegocio;
	private String parametroValidacionBCV = null;
	
	public void execute() throws Exception {
		DataSet datosFilter = new DataSet();
				
		datosFilter.append("tipo_prod", java.sql.Types.VARCHAR);
		datosFilter.append("menu_migaja", java.sql.Types.VARCHAR);
		datosFilter.append("tipo_transaccion", java.sql.Types.VARCHAR);
		datosFilter.append("inst_financiero", java.sql.Types.VARCHAR);
		datosFilter.append("vehiculos", java.sql.Types.VARCHAR);
		datosFilter.append("marcador_idb", java.sql.Types.VARCHAR);		
		datosFilter.addNew();
		datosFilter.setValue("tipo_prod", tipoProducto);
		datosFilter.setValue("menu_migaja", mensajeMenu);
		datosFilter.setValue("tipo_transaccion", tipoTransaccion);
		datosFilter.setValue("inst_financiero", idInstrumentoFinanciero);		
		datosFilter.setValue("vehiculos", vehiculosTomaOrden);
		datosFilter.setValue("marcador_idb", unidadConIDB);
		
		//ITS-2487 NM26659 26/02/2015
		_resument=new DataSet();
		_resument.append("unidad_inversion", java.sql.Types.VARCHAR);
		_resument.append("ui_id", java.sql.Types.VARCHAR);
		_resument.append("carga_no_cruce", java.sql.Types.VARCHAR);
		_resument.append("carga_cruce", java.sql.Types.VARCHAR);
		_resument.append("no_cargada", java.sql.Types.VARCHAR);
		
		_resument.addNew();

		_ordenesCruces.first();
		_ordenesCruces.next();
		
		_resument.setValue("unidad_inversion",nombreUnidadInversion);
		_resument.setValue("ui_id",String.valueOf(idUnidadInversion));
		_resument.setValue("carga_no_cruce",_ordenesCruces.getValue("no_cruzadas"));
		_resument.setValue("carga_cruce",_ordenesCruces.getValue("cruzadas"));
		_resument.setValue("no_cargada",_ordenesCruces.getValue("no_cargadas"));

		storeDataSet("datosFilter", datosFilter);
		storeDataSet("resumen", _resument);
		
	}
	
	public boolean isValid() throws Exception {
		boolean flag=true;
		ProcesosDAO procesosDAO = new ProcesosDAO(_dso);
		ordenesCrucesDAO=new OrdenesCrucesDAO(_dso);
		ParametrosDAO parametrosDAO= new ParametrosDAO(_dso);
		
		nombreUnidadInversion=_req.getParameter("ui_nombre");		
		idUnidadInversion=Long.parseLong(_req.getParameter("ui_id"));		
		tipoProducto=_req.getParameter("tipo_producto");
		mensajeMenu=_req.getParameter("mensaje_menu");
		tipoTransaccion=_req.getParameter("tipo_transaccion");
		tipoNegocio=_req.getParameter("tipo_negocio");
		
		ordenDAO=new OrdenDAO(_dso);
		//ordenDAO.listarOrdenesPorUnidadInversion(StatusOrden.ENVIADA,false,String.valueOf(idUnidadInversion),ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL,true);
		ordenDAO.listarVehuculosPorIdUnidad(idUnidadInversion);
		
		if(ordenDAO.getDataSet().count()>0){
			ordenDAO.getDataSet().first();
			int count=0;
			while(ordenDAO.getDataSet().next()){
				if(count>0){				
					vehiculosTomaOrden=vehiculosTomaOrden.concat(",");	
				}
				vehiculosTomaOrden=vehiculosTomaOrden.concat(ordenDAO.getDataSet().getValue("ORDENE_VEH_TOM"));
				++count;
			}			
		}								
		
		unidadInversionDAO=new UnidadInversionDAO(_dso);
		unidadInversionDAO.listarUnidadInversionPorId(idUnidadInversion);
		if(unidadInversionDAO.getDataSet().count()>0){
			unidadInversionDAO.getDataSet().first();
			unidadInversionDAO.getDataSet().next();	
			estatusUnidadInversion=unidadInversionDAO.getDataSet().getValue("UNDINV_STATUS");
			idInstrumentoFinanciero=unidadInversionDAO.getDataSet().getValue("INSFIN_ID");
			unidadConIDB=unidadInversionDAO.getDataSet().getValue("UITITU_IN_CONIDB");
			
			//nm26659 TTS-491 WEB SERVICE ALTO VALOR (configuracion tipo transaccion para procesamientos Unidades SIMADI Alto Valor)			
			if(tipoTransaccion==null || tipoTransaccion.equals("")){				
				if(unidadInversionDAO.getDataSet().getValue("TIPO_PRODUCTO_ID").equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL)){
					tipoTransaccion=TransaccionNegocio.CRUCE_SICAD2_CLAVE_CIERRE;
					tipoProducto=ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL;
				}else if(unidadInversionDAO.getDataSet().getValue("TIPO_PRODUCTO_ID").equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL)){
					tipoTransaccion=TransaccionNegocio.CRUCE_SICAD2_RED_CIERRE;
					tipoProducto=ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL;
				}
			}
			
			if(!estatusUnidadInversion.equals(UnidadInversionConstantes.UISTATUS_PUBLICADA)){
				flag=false;
				_record.addError("Para su informacion", "La unidad de inversion que intenta procesar no se encuentra en estatus PUBLICADA");
			}
		}
		
			//ITS-2487 NM26659 26/02/2015
			ordenesCrucesDAO.resumenCrucesPorUnidadInversion(idUnidadInversion);			
			if(ordenesCrucesDAO.getDataSet().count()==0){
				flag=false;
				_record.addError("Para su informacion", "No hay operaciones para la Unidad de inversion seleccionada");
			}else {
				_ordenesCruces=ordenesCrucesDAO.getDataSet();
			}
			
		
			//NM26659 16/04/2015 TTS-491 WEB SERVICE ALTO VALOR (Se agregan validacion para procesamiento de unidades de Inversion Alto Valor 
			if(tipoNegocio.equals(ConstantesGenerales.TIPO_NEGOCIO_ALTO_VALOR)){//NEGOCIO DE ALTO VALOR				
				
				procesosDAO.listarPorTransaccionActiva(TransaccionNegocio.WS_BCV_ALTO_VALOR_DEMAN);
				if (procesosDAO.getDataSet().count() > 0) {//Verificacion que no exista un proceso de envio Ordenes Alto Valor a BCV en ejecucion
					_record.addError("Para su informaci&oacuten","No se puede ejecutar el proceso de cierre por que existe un proceso de Envio de operaciones al BCV ALTO VALOR_DEMANDA en ejecucion");
					flag = false;
				}
				
				procesosDAO.listarPorTransaccionActiva(TransaccionNegocio.WS_BCV_ALTO_VALOR_OFER);
				if (procesosDAO.getDataSet().count() > 0) {//Verificacion que no exista un proceso de envio Ordenes Alto Valor a BCV en ejecucion
					_record.addError("Para su informaci&oacuten","No se puede ejecutar el proceso de cierre por que existe un proceso de Envio de operaciones al BCV ALTO VALOR_OFERTA en ejecucion");
					flag = false;
				}
				
			
			parametrosDAO.listarParametros(ConstantesGenerales.TRANSF_BCV_ONLINE_AVALOR,"001");
			
			if(parametrosDAO.getDataSet().count()>0){
				parametrosDAO.getDataSet().first();
				parametrosDAO.getDataSet().next();
				parametroValidacionBCV=parametrosDAO.getDataSet().getValue("PARVAL_VALOR");
				
				if(parametroValidacionBCV==null || parametroValidacionBCV.equals("")){
					_record.addError("Par&aacute;metro verificaci&acuteo;n BCV en Linea", "El valor del par&aacute;metro no puede estar vacio, por favor verifique en el modulo de Configuraci&oacute;n / Grupo Par&aacute;metros ");
					flag = false;
				}else if(parametroValidacionBCV!=null){
					int paramBCV=0;
					try{
						paramBCV=Integer.parseInt(parametroValidacionBCV);
						
						if(paramBCV<0 || paramBCV>2){
							_record.addError("Par&aacute;metro verificaci&acuteo;n BCV en linea", "El valor ingresado en el par&aacute;metro no es v&aacute;lido, por favor verifique ");
							flag = false;
						}
					}catch (NumberFormatException e) {
						_record.addError("Par&aacute;metro verificaci&acuteo;n BCV en linea", "El valor ingresado en el par&aacute;metro no es de tipo num&eacute;rico, por favor verifique ");
						flag = false;
					}								
				}
			} else {
				_record.addError("Par&aacute;metro verificaci&acuteo;n BCV en linea","No existe el paremtro de Validacion de ordenes BCV, por favor verifique");
				return false;
			}
		//Verificacion del parametro Validacion BCV en Linea
		if(parametroValidacionBCV.equals(String.valueOf(ConstantesGenerales.STATUS_ACTIVO))){																
				ordenesCrucesDAO.listarCrucesPorUnidadInversion(idUnidadInversion, false,ConstantesGenerales.SIN_VERIFICAR,ConstantesGenerales.CRUCE_SIN_PROCESADO,StatusOrden.CRUZADA);						
				if(ordenesCrucesDAO.getDataSet().count()>0){				
					flag=false;				
					_record.addError("Para su informacion", "No se puede realizar el procesamiento de la Unidad de Inversion ya que existen Pactos de alto valor que no han sido verificados por el BCV");
				}
			}
		}else if(tipoNegocio.equals(ConstantesGenerales.TIPO_NEGOCIO_BAJO_VALOR)){//NEGOCIO DE MENUDEO
						
			procesosDAO.listarPorTransaccionActiva(TransaccionNegocio.WS_BCV_MENUDEO);
			if (procesosDAO.getDataSet().count() > 0) {//Verificacion que no exista un proceso de envio Ordenes Alto Valor a BCV en ejecucion
				_record.addError("Para su informaci&oacuten","No se puede ejecutar el proceso de cierre por que existe un proceso de Envio de operaciones al BCV de MENUDEO en ejecucion");
				flag = false;
			}
			
			parametrosDAO.listarParametros(ConstantesGenerales.TRANSF_BCV_ONLINE,"001");
			
			if(parametrosDAO.getDataSet().count()>0){
				parametrosDAO.getDataSet().first();
				parametrosDAO.getDataSet().next();
				parametroValidacionBCV=parametrosDAO.getDataSet().getValue("PARVAL_VALOR");
				
				if(parametroValidacionBCV==null || parametroValidacionBCV.equals("")){
					_record.addError("Par&aacute;metro verificaci&acuteo;n BCV en Linea", "El valor del par&aacute;metro no puede estar vacio, por favor verifique en el modulo de Configuraci&oacute;n / Grupo Par&aacute;metros ");
					flag = false;
				}else if(parametroValidacionBCV!=null){
					int paramBCV=0;
					try{
						paramBCV=Integer.parseInt(parametroValidacionBCV);
						
						if(paramBCV<0 || paramBCV>2){
							_record.addError("Par&aacute;metro verificaci&acuteo;n BCV en linea", "El valor ingresado en el par&aacute;metro no es v&aacute;lido, por favor verifique ");
							flag = false;
						}
					}catch (NumberFormatException e) {
						_record.addError("Par&aacute;metro verificaci&acuteo;n BCV en linea", "El valor ingresado en el par&aacute;metro no es de tipo num&eacute;rico, por favor verifique ");
						flag = false;
					}								
				}
			} else {
				_record.addError("Par&aacute;metro verificaci&acuteo;n BCV en linea","No existe el paremtro de Validacion de ordenes BCV, por favor verifique");
				return false;
			}
			
			if(parametroValidacionBCV.equals(String.valueOf(ConstantesGenerales.STATUS_ACTIVO))){
				ordenesCrucesDAO.listarCrucesPorUnidadInversion(idUnidadInversion, false,ConstantesGenerales.SIN_VERIFICAR,ConstantesGenerales.CRUCE_SIN_PROCESADO,StatusOrden.CRUZADA);						
				if(ordenesCrucesDAO.getDataSet().count()>0){				
					flag=false;				
					_record.addError("Para su informacion", "No se puede realizar el procesamiento de la Unidad de Inversion ya que el parametro BCV en Linea se encuentra activo y existen Ordenes que no han sido verificadas");
				} 								
			}					
		}			
			
			if(flag){				
				ordenesCrucesDAO.listarCrucesInvalidosMenudeoSIMADI(idUnidadInversion,ConstantesGenerales.CRUCE_SIN_PROCESADO, StatusOrden.CRUZADA);				
				if(ordenesCrucesDAO.getDataSet().count()>0){
					flag=false;				
					_record.addError("Para su informaci&oacute;n", " Existen registros mal cargados (sin ID de Orden BCV) por favor verifique ");
				}
			}
			
		return flag;
	}
}

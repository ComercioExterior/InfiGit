package models.intercambio.recepcion.cruce_sicad_II.carga_cruce;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import megasoft.DataSet;
import megasoft.Logger;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.OrdenesCrucesDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.dao.UsuarioDAO;
import com.bdv.infi.data.Proceso;
import com.bdv.infi.logic.cruces_ordenes.BeanLogicCargaCruces;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;

public class Browse extends MSCModelExtend{
	
	private String plantillaTitulo;
	private String transaccionId;
	private String menu;
	private String  proceso_id = null; 
	private String idUnidadF = null;
	private String parametroValidacionBCV = null;
	private boolean UnidadMenudeo=false;
	private String idUnidadInv;
	
	public void execute() throws Exception {
		
	DataSet record = (DataSet) _req.getSession().getAttribute("filtro_ConsultaCruces");
	OrdenesCrucesDAO ordenesCrucesDAO = new OrdenesCrucesDAO(_dso);
	UnidadInversionDAO unidadInversionDAO=new UnidadInversionDAO(_dso); 
	//listarDatosUIPorId

	//Se crea DataSet con valores para la vista
	DataSet ds = new DataSet();
	ds.append("idejec", java.sql.Types.VARCHAR);
	ds.append("menu_migaja", java.sql.Types.VARCHAR);
	ds.append("mensaje_error", java.sql.Types.VARCHAR);
	ds.append("idUnidad", java.sql.Types.VARCHAR);
	ds.append("id_ui_selected", java.sql.Types.VARCHAR);
	
	if(record == null){
		//System.out.println("Desde el Filter-*-*******-*-*-*-*-*");
		//Consulta el id del proximo proceso a aperturar
		proceso_id = dbGetSequence(_dso, ConstantesGenerales.SECUENCIA_PROCESOS);
		//Creacion del proceso
		Proceso proceso = new Proceso();
		proceso.setEjecucionId(Integer.parseInt(proceso_id));
		proceso.setTransaId(transaccionId);
		UsuarioDAO usuarioDAO = new UsuarioDAO(_dso);
		if(getUserName()!=null && !getUserName().equals("")){
			proceso.setUsuarioId(Integer.parseInt(usuarioDAO.idUserSession(getUserName())));
		}
		proceso.setFechaInicio(new Date());
		proceso.setFechaValor(new Date());		
		
		//NM26659 - 20/03/2015 Busqueda de tipo de Negocio de la Unidad de Inversion (Alto o Bajo Valor)
		unidadInversionDAO.listarDatosUIPorId(Long.parseLong(idUnidadInv));		
		if(unidadInversionDAO.getDataSet().count()>0){
			unidadInversionDAO.getDataSet().first();
			unidadInversionDAO.getDataSet().next();
			
			if(unidadInversionDAO.getDataSet().getValue("TIPO_NEGOCIO").equalsIgnoreCase(ConstantesGenerales.TIPO_NEGOCIO_BAJO_VALOR)){
				UnidadMenudeo=true;
			}
		}
		//Se llena el DataSet con valores para la vista
		ds.addNew();
		ds.setValue("idejec", proceso.getEjecucionId()+"");
		Logger.debug(this, "ID EJECUCION----------------"+ds.getValue("idejec"));
		ds.setValue("menu_migaja", menu);
		
		Logger.debug(this, "undinv_id: "+_record.getValue("undinv_id")+" - "+_record.getValue("undinv_nombre"));
		//Se setean parametros del bean de carga
		BeanLogicCargaCruces beanLogicCargaCruces = new BeanLogicCargaCruces();		
		beanLogicCargaCruces.setIdUiFiltered(_record.getValue("undinv_id"));
		beanLogicCargaCruces.setNameUiFiltered(_record.getValue("undinv_nombre"));
		beanLogicCargaCruces.setIdentificadorPlantilla(plantillaTitulo);
		beanLogicCargaCruces.setContenidoDocumento(_record.getValue("archivo.tempfile"));
		beanLogicCargaCruces.setNombreDocumento(_record.getValue("archivo.filename"));
		beanLogicCargaCruces.setProceso(proceso);
		beanLogicCargaCruces.setDataSource(_dso);
		beanLogicCargaCruces.setNombreUsuario(getUserName());
		beanLogicCargaCruces.cargarDocumentosUnidadInv();
		beanLogicCargaCruces.setIp(_req.getRemoteAddr());
		beanLogicCargaCruces.setParametroValidacionBCV(parametroValidacionBCV);		

		//Verificacion del parametro Validacion BCV en Linea
		if(beanLogicCargaCruces.getParametroValidacionBCV().equals(String.valueOf(ConstantesGenerales.STATUS_ACTIVO))){			
			beanLogicCargaCruces.setValidacionBcvEnLinea(true);
		}
		//Verificacion del tipo de negocio asociado a la Unidad de inversion
		beanLogicCargaCruces.setValidacionUnidadMenudeo(UnidadMenudeo);
		
		ds.setValue("id_ui_selected", beanLogicCargaCruces.getIdUiFiltered());
		idUnidadF = beanLogicCargaCruces.getIdUiFiltered();
		//Se ejecuta el proceso de carga de cruces
		if(!beanLogicCargaCruces.cargarCruces()){
			ds.setValue("mensaje_error", beanLogicCargaCruces.getMensajeError());
		}else{
			String mensajeCarga = "";
			if(beanLogicCargaCruces.getContadorValid()==1){
				mensajeCarga = "Se carg&oacute; 1 cruce exitosamente";
			}else{
				if(beanLogicCargaCruces.getContadorValid()>1){
					mensajeCarga = "Se cargaron "+beanLogicCargaCruces.getContadorValid()+" cruces exitosamente";
				}
			}
			ds.setValue("mensaje_error", mensajeCarga);
		}
		//}
		
		//ACTUALIZAR ORDENES CON CRUCES/NO_CRUCES VALIDOS A PROCESO_CRUCE
		Hashtable<String,String> ordenesToUpdate = beanLogicCargaCruces.getOrdenesToUpdate();
		if(ordenesToUpdate.size()>0){
			Enumeration<String> enumKeys = ordenesToUpdate.keys();
			StringBuffer idsOrdenes = new StringBuffer();
			OrdenDAO ordenDAO = new OrdenDAO(_dso);
			ArrayList<String> querys = new ArrayList<String>();
			String idsLote = "";
			for(int i=0; i<ordenesToUpdate.size(); i++){
				idsOrdenes.append(enumKeys.nextElement()).append(", ");
				if(i % ConstantesGenerales.UPDATE_ORDENES_CARGA_CRUCES == 0){ //Se completa grupo de ids de ordenes a actualzar
					idsLote = idsOrdenes.toString().substring(0, idsOrdenes.toString().length()-2);
					Logger.debug(this, "IDS_ORDENES_ACTUALIZAR_POST_CRUCE----------"+idsLote);
					querys.add(ordenDAO.actualizarEstatusOrdenesIn(idsLote, StatusOrden.PROCESO_CRUCE, 0));
					if(!ordenDAO.ejecutarStatementsBatchBool(querys)){
						ds.setValue("mensaje_error", ds.getValue("mensaje_error")+"<br/><br/>Error al actualizar el estatus de la &oacute;rdenes con cruces v&aacute;lidos: "+idsLote); 
					}
					querys.clear();
					idsOrdenes = new StringBuffer();
				}
			}
			
			if(idsOrdenes.length()>0){ //Se actualizan los ultimos ids de ordenes restantes
				idsLote = idsOrdenes.toString().substring(0, idsOrdenes.toString().length()-2);
				Logger.debug(this, "IDS_ORDENES_ACTUALIZAR_POST_CRUCE(Restante)----------"+idsLote);
				querys.add(ordenDAO.actualizarEstatusOrdenesIn(idsLote, StatusOrden.PROCESO_CRUCE, 0));
				if(!ordenDAO.ejecutarStatementsBatchBool(querys)){
					ds.setValue("mensaje_error", ds.getValue("mensaje_error")+"<br/><br/>Error al actualizar el estatus de la &oacute;rdenes con cruces v&aacute;lidos: "+idsLote); 
				}
			}
			
		}//Hay ordenes por actualizar		
				
	}else{//Se salta el proceso de carga al cambiar la pagina
		//System.out.println("Desde el Browse Paginacion-*-*******-*-*-*-*-*");
		//Se crea DataSet con valores para la vista
		proceso_id = record.getValue("idEjecucionF");
		idUnidadF = record.getValue("idUnidadF");
	
		//Se llena el DataSet con valores para la vista
		ds.addNew();
		ds.setValue("idejec", proceso_id+"");
		ds.setValue("id_ui_selected", idUnidadF);
		Logger.debug(this, "ID EJECUCION----------------"+ds.getValue("idejec"));
		ds.setValue("menu_migaja", menu);
		
		
	}
	
		//Se trae dataSet con cruces del lote insertado
		ordenesCrucesDAO.listByIdEjecucion(proceso_id,true,getNumeroDePagina(),getPageSize());
		Logger.info(this, "---Cant. Cruces Lote Carga: "+ordenesCrucesDAO.getDataSet().count());

		getSeccionPaginacion(ordenesCrucesDAO.getTotalDeRegistros(), getPageSize(), getNumeroDePagina());
		storeDataSet("cruces", ordenesCrucesDAO.getDataSet());
		storeDataSet("datos", ds);
		DataSet reConsulta = new DataSet();
		reConsulta.addNew();
		
		reConsulta.append("idUnidadF", java.sql.Types.VARCHAR);
		//reConsulta.setValue("idUnidadF",  beanLogicCargaCruces.getIdUiFiltered());
		reConsulta.setValue("idUnidadF",  null);
		reConsulta.append("idClienteF", java.sql.Types.VARCHAR);
		reConsulta.setValue("idClienteF", null);
		reConsulta.append("idOrdenF", java.sql.Types.VARCHAR);
		reConsulta.setValue("idOrdenF", null);
		reConsulta.append("idEjecucionF", java.sql.Types.VARCHAR);
		reConsulta.setValue("idEjecucionF", proceso_id);
		reConsulta.append("statusF", java.sql.Types.VARCHAR);
		reConsulta.setValue("statusF", null);
		reConsulta.append("statusP", java.sql.Types.VARCHAR);
		reConsulta.setValue("statusP", null);
		reConsulta.append("indTitulo", java.sql.Types.VARCHAR);
		reConsulta.setValue("indTitulo", null);
		
		//setSessionDataSet("filtro_paginacion", reConsulta);
		setSessionDataSet("filtro_ConsultaCruces", reConsulta);
		
	}
	
	public boolean isValid() throws Exception {
		
		String plantillaName = "";
		boolean flag = super.isValid();

		
		if(flag)
		{
			
			ParametrosDAO parametrosDAO= new ParametrosDAO(_dso);
			parametrosDAO.listarParametros(ConstantesGenerales.TRANSF_BCV_ONLINE,"001");

			if(parametrosDAO.getDataSet().count()>0){
				parametrosDAO.getDataSet().first();
				parametrosDAO.getDataSet().next();
				parametroValidacionBCV=parametrosDAO.getDataSet().getValue("PARVAL_VALOR");
				
				if(parametroValidacionBCV==null || parametroValidacionBCV.equals("")){
					_record.addError("Par&aacute;metro verificaci&acuteo;n BCV en Linea", "El valor del par&aacute;metro no puede estar vacio, por favor verifique en el modulo de Configuraci&oacute;n / Grupo Par&aacute;metros ");
					flag = false;
				}
					int paramBCV=0; 
				if(parametroValidacionBCV!=null){				
					try{
						paramBCV=Integer.parseInt(parametroValidacionBCV);
					}catch (NumberFormatException e) {
						_record.addError("Par&aacute;metro verificaci&acuteo;n BCV en linea", "El valor ingresado en el par&aacute;metro no es de tipo num&eacute;rico, por favor verifique ");
						flag = false;
					}
				
					if(paramBCV<0 || paramBCV>2){
						_record.addError("Par&aacute;metro verificaci&acuteo;n BCV en linea", "El valor ingresado en el par&aacute;metro no es v&aacute;lido, por favor verifique ");
						flag = false;
					}
				}
			} else {
				_record.addError("Par&aacute;metro verificaci&acuteo;n BCV en linea","No existe el paremtro de Validacion de ordenes BCV");
				flag = false;
			}
//System.out.println("TIPO PROD----"+_record.getValue("tipo_prod"));
//System.out.println("TIPO OPER----"+_record.getValue("tipo_operacion"));
//System.out.println("FILE NAME----"+_record.getValue("archivo.filename"));
//System.out.println("UI----"+_record.getValue("undinv_id"));
			idUnidadInv=_record.getValue("undinv_id");
			if(_record.getValue("undinv_id")==null || _record.getValue("undinv_id").equals("")){
				_record.addError("Unidad de Inversi&oacute;n", "Debe seleccionar una Unidad de Inversi&oacute;n");
				flag = false;
			}else{
				if(_record.getValue("tipo_operacion")==null || _record.getValue("tipo_operacion").equals("")){
					_record.addError("Tipo Operaci&oacute;n", "Debe seleccionar un tipo de operaci&oacute;n a realizar");
					flag = false;
				}else{
					//CT19940 - Flexibilizacion de la validacion del nombre del archivo de carga.
					/*if(!_record.getValue("archivo.filename").endsWith(ConstantesGenerales.EXTENSION_DOC_XLS)){
						_record.addError("Archivo", "La extensi&oacute;n del archivo que ingreso es incorrecta. Verifique que sea .xls e intente de nuevo");
						flag = false;
					}else{*/
						if(_record.getValue("tipo_prod")==null || _record.getValue("tipo_prod").equals("")){
							_record.addError("Error", "Disculpe, debe ingresar de nuevo a la opci&oacute;n de men&uacute; correspondiente");
							flag = false;
						}
					//}
				}
			}
			
			if(flag){
				//Valida el nombre del archivo seleccionado
				if(_record.getValue("tipo_operacion").equals(ConstantesGenerales.INDICADOR_CRUCE)){
					plantillaTitulo = ConstantesGenerales.TITULO_TEMPLATE_CRUCE;
					plantillaName = ConstantesGenerales.ARCH_TEMPLATE_CRUCE;
					if(!_record.getValue("archivo.filename").startsWith(plantillaName)){//CT19940 - Flexibilizacion de la validacion del nombre del archivo de carga.
						_record.addError("Archivo", "El nombre del archivo que ingres&oacute; ("+_record.getValue("archivo.filename")+") no coincide con el nombre de la plantilla correspondiente a la operaci&oacute;n seleccionada ("+plantillaName+"). Verifique e intente de nuevo");
						flag = false;
					}
				}else{
					if(_record.getValue("tipo_operacion").equals(ConstantesGenerales.INDICADOR_NO_CRUCE)){
						plantillaTitulo = ConstantesGenerales.TITULO_TEMPLATE_NO_CRUCE;
						plantillaName = ConstantesGenerales.ARCH_TEMPLATE_NO_CRUCE;
						if(!_record.getValue("archivo.filename").startsWith(plantillaName)){//CT19940 - Flexibilizacion de la validacion del nombre del archivo de carga.
							_record.addError("Archivo", "El nombre del archivo que ingres&oacute; ("+_record.getValue("archivo.filename")+") no coincide con el nombre de la plantilla correspondiente a la operaci&oacute;n seleccionada ("+plantillaName+"). Verifique e intente de nuevo");
							flag = false;
						}
					}
				}
			}
			
			String tipoProducto=_record.getValue("tipo_prod");
			tipoProducto=tipoProducto.replace("'","");

			//tipoProducto=tipoProducto.trim();
			
			if(flag){
				if(tipoProducto.equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL)){
					transaccionId = TransaccionNegocio.CRUCE_SICAD2_CLAVE_CARGA;
					menu = "Clavenet Personal";
				}else if(tipoProducto.equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL)){					
						transaccionId = TransaccionNegocio.CRUCE_SICAD2_RED_CARGA;
						menu = "Red Comercial";
					}
				}			
		}
		
//System.out.println("TRANSACCION----"+transaccionId);

		return flag;
	}
	
}
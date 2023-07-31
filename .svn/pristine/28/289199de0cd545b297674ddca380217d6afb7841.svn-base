package models.intercambio.transferencia.generar_archivo_subasta_divisas;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.ServletOutputStream;
import javax.sql.DataSource;

import megasoft.DataSet;
import models.exportable.ExportableOutputStream;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionException;

import com.bdv.infi.dao.CamposDinamicos;
import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.Transaccion;
import com.bdv.infi.dao.UsuarioDAO;
import com.bdv.infi.data.Archivo;
import com.bdv.infi.data.CampoDinamico;
import com.bdv.infi.data.Detalle;
import com.bdv.infi.logic.interfaces.ActionINFI;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.util.FileUtil;
import com.bdv.infi.util.Utilitario;


/** Clase que exporta a excel la ordenes 
 * que deben ser enviadas al Banco de Venezuela
 * @author  nm25287
 */
public class ExportarOrdenes extends ExportableOutputStream{
	
	private Logger logger = Logger.getLogger(ExportarOrdenes.class);
	DataSet camposDinamicosDataSet = new DataSet();
	DataSet titulos = new DataSet();
	boolean tieneCamposDinamicos = false;
	Archivo archivo	= new Archivo();
	ControlArchivoDAO control = null;
	OrdenDAO control_orden = null;	
	String nombreMonedaBs = "";
	StringBuilder sbArchivo = new StringBuilder(); //Contenido
	ServletOutputStream os= null;
	private String separador =";";
	long tiempoEjecucion =0;
	CamposDinamicos camposDinamicos	= null;
	private boolean actualizarSolicitudSitme=false;
	
	public void execute() throws Exception {
		this.aplicarFormato = false;				
		control	= new ControlArchivoDAO(_dso);	
		camposDinamicos		= new CamposDinamicos(_dso);
		String ejecucion_id			=dbGetSequence(_dso, ConstantesGenerales.SECUENCIA_CONTROL_ARCHIVOS);
		String unidad_inversion 	= null;
		String vehiculo 			= null;
		String nombreArchivo = "";
		String ruta ="";
		String tipoProducto="", menu="";
		int inRecepcion 			= 0;	
		//NM29643 infi_TTS_466
		boolean llamarEnvioCorreos = false;
		
		ArrayList<CampoDinamico> nombreCamposD		= null;
		nombreMonedaBs = (String) _req.getSession().getAttribute(ParametrosSistema.MONEDA_BS_REPORTERIA);		
			
		if (_req.getParameter("undinv_id")!=null&&!_req.getParameter("undinv_id").equals("")){
			unidad_inversion= _req.getParameter("undinv_id");
		}else{
			unidad_inversion= getSessionObject("unidadInversion").toString();
		}
		if (_req.getParameter("ordene_veh_col")!=null&&!_req.getParameter("ordene_veh_col").equals("")){
			vehiculo= _req.getParameter("ordene_veh_col");
		}
		
		if (_req.getParameter("tipo_producto")!=null&&!_req.getParameter("tipo_producto").equals("")){
			tipoProducto = _req.getParameter("tipo_producto");	
			if(tipoProducto.equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL)){
				menu = "Generar Archivo Sicad II Red Comercial";
				llamarEnvioCorreos = true;
			}else{
				if(tipoProducto.equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA)){
					menu = "Generar Archivo Subasta Divisas";
				}
			}
			if(tipoProducto.equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL)){
				actualizarSolicitudSitme=true;
			}
		}
		
		logger.debug("tipo producto: "+ _req.getParameter("tipo_producto")+_req.getParameter("tipo_producto")!=null);
		logger.debug("actualizarSolicitudSitme: "+ actualizarSolicitudSitme);
		
		sbArchivo=new StringBuilder();
	    archivo.setUnidadInv(Integer.parseInt(unidad_inversion));

		archivo.setVehiculoId(vehiculo);
		
		//CONSULTAR CAMPOS DINAMICOS DE LA UNIDAD DE INVERSION		
		nombreCamposD=camposDinamicos.listarCamposDinamicosUnidadInversion(unidad_inversion,_dso);
		logger.debug("nombreCamposD: "+nombreCamposD);
		
		//CREAR CABECERA DEL ARCHIVO
		crearCabecera(nombreCamposD);
		
		//Creamos el nombre del archivo dinamicamente
		control.nombreArchivo(archivo);//retorna el numero del veiculo colocador
		nombreArchivo = obtenerNombreArchivo("intercambio");
		archivo.setNombreArchivo(nombreArchivo);
		archivo.setIdEjecucion(Long.parseLong(ejecucion_id));
		archivo.setInRecepcion(inRecepcion);
		archivo.setUsuario(getUserName());

		registrarInicio(nombreArchivo);			
		//OBTENER LOS DATOS A EXPORTAR A EXCEL
		obtenerOrdenesExportarExcel(unidad_inversion,_dso,nombreCamposD);			
				
		//RESPALDAR ARCHIVO EN EL SERVIDOR 
		try {
			//Consultar ruta de respaldo
			ruta=ParametrosDAO.listarParametros("TEMP_DIRECTORY",_dso);
			logger.debug("getRootWebApplicationPath: "+FileUtil.getRootWebApplicationPath());
			logger.debug("RUTA + nombreArchivo: "+ruta+" + "+nombreArchivo);
			//Crear archivo de respaldo
			FileUtil.crearArchivo(sbArchivo,FileUtil.getRootWebApplicationPath()+nombreArchivo,false);
		
		} catch (Exception e) {
			logger.error("Ocurrio un error al generar el archivo de respaldo",e);
		}		
				
		registrarFin();
		obtenerSalida();
		
		DataSet datosArchivo= new DataSet();
		datosArchivo.append("nombre_archivo", java.sql.Types.VARCHAR);
		datosArchivo.append("menu_migaja", java.sql.Types.VARCHAR);
		datosArchivo.addNew();
		datosArchivo.setValue("nombre_archivo", nombreArchivo);
		datosArchivo.setValue("menu_migaja", menu);
		
		storeDataSet("datosArchivo", datosArchivo);
		
		//Iniciar proceso de Actualización de ordenes en INFI
		hiloActualizacionOrdenesInfi(Integer.parseInt(unidad_inversion),archivo,actualizarSolicitudSitme, llamarEnvioCorreos);
		
		//Iniciar proceso de Actualización de Clientes en OPICS
		//hiloActualizacionClientesOpics(Integer.parseInt(unidad_inversion));		
			
	}//fin execute
			
	
	/**
	 * Llamada al proceso de actualización de estatus ENVIADA en INFI, se ejecutará en modo background
	 * @param idUnidadInversion
	 * @throws Exception
	 */
	private void hiloActualizacionOrdenesInfi(int idUnidadInversion,Archivo archivo, boolean actualizarSolicitudSitme, boolean llamarEnvioCorreos) throws Exception {
		
		UsuarioDAO usu = new UsuarioDAO(_dso);		
		
		try {
			logger.debug("Se disparo el hilo para el proceso de actualización de ordenes en INFI");
	    	
			//NM29643 - INFI_TTS_466 SE pasa el usuario para llamada a proceso de envio de correos en el hilo
			Thread t = new Thread(new ActualizacionOrdenesInfi(_dso, idUnidadInversion, Integer.parseInt(usu.idUserSession(getUserName())), getUserName(), archivo,actualizarSolicitudSitme, llamarEnvioCorreos));
			t.start();
			t.join();
			
		} catch (Exception e) {
			logger.error("Error disparando proceso para la actualización de ordenes en INFI", e);			
			throw new JobExecutionException(e);
		}
	}

	/**
	 * Exporta las ordenes de subasta de divisas a un archivo .csv que puede ser leido en Excel
	 * @param unidadInversion
	 * @param _dso
	 * @param camposD
	 * @throws Exception
	 */
	private void obtenerOrdenesExportarExcel(String unidadInversion, DataSource _dso,ArrayList<CampoDinamico> camposD) throws Exception{
		ControlArchivoDAO control			= new ControlArchivoDAO(_dso);
        Transaccion transaccion 			= new Transaccion(this._dso);
        Statement statement 				= null;
        ResultSet ordenes 					= null;       
       
		logger.info("ExportarOrdenes.obtenerOrdenesExportarExcel->inicio. Unidad Inversion: "+unidadInversion);
        try{
	        transaccion.begin();
			statement = transaccion.getConnection().createStatement();	
			//***** victor goncalves punto 2 ******
			if (_req.getSession().getAttribute("url_sicadII").toString().equalsIgnoreCase(ActionINFI.GENERAR_ARCH_SICADII.getNombreAccion())){
				ordenes = statement.executeQuery(control.listarDetallesString(unidadInversion, ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL));
			}else{
				ordenes = statement.executeQuery(control.listarDetallesString(unidadInversion, ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA));
			}
			//antes
			//ordenes = statement.executeQuery(control.listarDetallesString(unidadInversion, ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA));
						
			while(ordenes.next()){
				registroProcesado++;							
				
				//Buscar digito verificador para el RIF del cliente							
				String rifConDigitoVerificador = Utilitario.digitoVerificador(ordenes.getString("tipper_id").concat(Utilitario.rellenarCaracteres(String.valueOf(ordenes.getString("CLIENT_CEDRIF")), '0', 8, false)),false); 
								
				escribir(ordenes.getString("INSFIN_DESCRIPCION"));
				escribir(separador);
				//escribir(ordenes.getString("UNDINV_EMISION")+separador);
				escribir(ordenes.getString("UNDINV_NOMBRE"));
				escribir(separador);
				escribir(ordenes.getString("ORDENE_ID"));
				escribir(separador);
				escribir(rifConDigitoVerificador);
				escribir(separador);
				//escribir(ordenes.getString("tipper_id").concat(Utilitario.rellenarCaracteres(String.valueOf(ordenes.getString("CLIENT_CEDRIF")), '0', 8, false))+ separador);
				escribir(ordenes.getString("CLIENT_NOMBRE"));
				escribir(separador);
				escribir("'"+getString(ordenes.getString("ctecta_numero")));	
				escribir(separador);
				escribir(ordenes.getString("BLOTER_DESCRIPCION"));
				escribir(separador);
				escribir(ordenes.getDouble("ORDENE_PED_MONTO"));
				escribir(separador);
				escribir(ordenes.getDouble("ORDENE_TASA_CAMBIO"));
				escribir(separador);
				escribir(ordenes.getDouble("MONTO_SOLICITADO_BOLIVARES"));
				escribir(separador);						
				escribir(ordenes.getDouble("ordene_ped_comisiones"));
				escribir(separador);
				escribir(ordenes.getDouble("ordene_ped_total"));
				escribir(separador);
				escribir(ordenes.getString("ORDENE_PED_FE_ORDEN"));
				escribir(separador);
				escribir(ordenes.getString("ORDENE_PED_FE_VALOR"));
				escribir(separador);
				escribir(ordenes.getString("ORDENE_USR_SUCURSAL"));
				escribir(separador);
				escribir(ordenes.getString("ORDENE_USR_NOMBRE"));
				escribir(separador);
				escribir(ordenes.getString("CLIENT_DIRECCION"));
				escribir(separador);
				escribir(ordenes.getString("CLIENT_TELEFONO"));
				escribir(separador);
				escribir(getString(ordenes.getString("codigo_id")));
				escribir(separador);
				escribir(getString(ordenes.getString("sector_id")));
				escribir(separador);
				escribir(getString(ordenes.getString("concepto_id")));
				escribir(separador);
				//NM26659_25022015 TTS491: Inclusion de campo Hora registro
				escribir(getString(ordenes.getString("HORA_REGISTRO")));
				escribir(separador);
				//AGREGAR CAMPOS DINAMICOS-ordenados segun los titulos de la Unidad de Inversion
				if (tieneCamposDinamicos){
					agregarCamposDinamicos(ordenes.getString("ORDENE_ID"),camposD);
				}						
				escribir("\r\n");
				
				//INCLUIR EN EL OBJ SOLO LAS ORDENES QUE DEBEN SER ACTUALIZADAS CON ESTATUS ENVIADA
	       		if(ordenes.getString("ORDSTA_ID").equalsIgnoreCase(StatusOrden.REGISTRADA)){
	       			Detalle detalle = new Detalle();
		       		detalle.setIdOrden(ordenes.getLong("ORDENE_ID")); 
		       		archivo.agregarDetalle(detalle);
	       		}	
				
			}//fin while
								
		}catch (Exception ex) {
			logger.error("Error en el proceso de generación archivo batch para adjudicación tipo subasta. " + ex.getMessage(),ex);
			transaccion.rollback();
		} finally {			
			try {
				if (ordenes != null){
					ordenes.close();
				}
				if (statement != null){
					statement.close();
				}
				transaccion.closeConnection();
			} catch (Exception e) {
				logger.error("Error en el proceso de exportación",e);				
			}
		}			
	}

	//NM26659_25022015 TTS491: Inclusion de campo Hora registro
	protected void crearCabecera(ArrayList<CampoDinamico> nombreCamposD) throws Exception {		
		//Agregar campos fijos de la cabecera del archivo		
		escribir("INSTRUMENTO;SUBASTA;ORDEN;CEDULA;NOMBRE;CUENTA;BLOTER;MONTO SOLICITADO;TASA DE CAMBIO;MONTO SOLICITADO "+nombreMonedaBs+";COMISIONES "+nombreMonedaBs+";MONTO TOTAL DE LA OPERACIÓN "+nombreMonedaBs+";FECHA OPERACION;FECHA VALOR;AGENCIA RECEPTORA;OPERADOR;DIRECCION CLIENTE;TELEFONO CLIENTE;ACTIVIDAD ECONOMICA;SECTOR PRODUCTIVO;CONCEPTO;HORA REGISTRO;");
				
		if (nombreCamposD.size()>0){
			tieneCamposDinamicos=true;			
			//Agregar nombres de campos dinámicos en la cabecera del archivo
			for(int i=0; i<nombreCamposD.size();i++){		
				escribir(nombreCamposD.get(i).getCampoNombre());	
				escribir(separador);
			}
		}
		escribir("\r\n");
	}
	
	/**
	 * Adjunta los valores dinámicos de la orden
	 * @param sb contenido de la consulta
	 * @param listaCampos lista de campos dinámicos encontrados para la orden
	 * @throws Exception 
	 */
	protected void agregarCamposDinamicos(String ordenId,ArrayList<CampoDinamico> camposD) throws Exception{
		ArrayList<CampoDinamico> listaCampos= null;	
		listaCampos=camposDinamicos.listarCamposDinamicosOrdenes(ordenId,_dso,camposD);//ordenes.getString("ORDENE_ID")
		
		for(int i=0; i<listaCampos.size();i++){		
			escribir(listaCampos.get(i).getValor());	
			escribir(separador);
		}
	}
	
	/**
	 * Adjunta los campos dinámicos a la consulta
	 * @param sbCabeceraCD cabecera de campos dinámicos. Sólo es llenada la primera vez que entra a este ciclo
	 * @param sb contenido de la consulta
	 * @param listaCampos lista de campos dinámicos encontrados para la orden
	 * @throws Exception 
	 */
	protected void adjuntarCamposDinamicos(StringBuilder sb, ArrayList<CampoDinamico> listaCampos) throws Exception{
		sbArchivo.append(sb);
	}
	
	//Sobrescritura del metodo escribir(String valor) de la clase ExportableOutputStream
	public void escribir(String valor) throws Exception{	
		//System.out.println("valor:'"+valor+"'");
		sbArchivo.append(valor!=null&&!valor.equalsIgnoreCase("null")?valor:"");
	}
	
	//Sobrescritura del metodo escribir(double valor) de la clase ExportableOutputStream
	public void escribir(double valor) throws Exception{		
		sbArchivo.append(getNumero(valor).toString());
	}
	
	//Sobrescritura del metodo obtenerSalida() de la clase ExportableOutputStream
	public void obtenerSalida() throws Exception{	
		tiempoEjecucion=System.currentTimeMillis();
		logger.info("Inicio de exportación de ordenes "+tiempoEjecucion);
		//os=_res.getOutputStream();
		//os.write(sbArchivo.toString().getBytes()); 		
		//os.flush();	
	}
	
	//Sobrescritura del metodo registrarInicio(String nombre) de la clase ExportableOutputStream
	public void registrarInicio(String nombre) throws Exception{
		logger.info("Fin de exportación de ordenes "+(System.currentTimeMillis()-tiempoEjecucion));
        inicioProceso=System.currentTimeMillis();
		//_res.addHeader("Content-Disposition","attachment;filename="+nombre); 
		//_res.setContentType("application/x-download"); 
			
	}

}//Fin Clase
	
	

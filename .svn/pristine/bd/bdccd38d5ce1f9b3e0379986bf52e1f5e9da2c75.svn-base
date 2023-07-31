package models.ordenes.consultas.exportar_ordenes_simadi;

import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import megasoft.DataSet;
import models.exportable.ExportableBufferString;

import org.apache.log4j.Logger;

import com.bdv.infi.dao.ControlArchivoDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.ParametrosDAO;
import com.bdv.infi.dao.Transaccion;
import com.bdv.infi.util.FileUtil;
import com.bdv.infi.util.Utilitario;

//Modificación 23/03/2015 NM25287 ITS-2531 Reporte Solicitudes SIMADI no muestra filtro de Ofertas y Demandas
public class ExportarOrdenesSimadi extends ExportableBufferString{

	StringBuilder sb = new StringBuilder();
	StringBuilder sbcd = new StringBuilder();
	String tipo_producto_id = null;
	String tipo_negocio = null;
	private Logger logger = Logger.getLogger(ExportarOrdenesSimadi.class);
	ControlArchivoDAO control = null;
	OrdenDAO control_orden = null;	
	private String mensajeError = "";	
	DataSet datosArchivo = new DataSet();
	DataSet record = new DataSet();	
	long inicio = 0;
	long tiempo = 0;
	
	public void execute() throws Exception {			
		String nombreArchivo = "";		
		String ruta = "";
	
		record = (DataSet) _req.getSession().getAttribute("exportar_ordenes_simadi-browse.framework.page.record");
		if(record.count()>0){
			record.first();
			while (record.next()){
				tipo_producto_id = record.getValue("tipo_producto_id");				
				tipo_negocio = record.getValue("tipo_negocio");		
			}
		}
		datosArchivo.append("nombre_archivo", java.sql.Types.VARCHAR);
		datosArchivo.append("display", java.sql.Types.VARCHAR);
		datosArchivo.append("msg", java.sql.Types.VARCHAR);
		datosArchivo.addNew();

		try{
			
			nombreArchivo = obtenerNombreArchivo("sol_simadi");
			
			obtenerOrdenesExportarExcel();

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

			datosArchivo.setValue("nombre_archivo", nombreArchivo);
			datosArchivo.setValue("display", "block");


		}catch(Exception e) {
			logger.error("Ha ocurrido un error en la operacion: " + e.getMessage());			

		}		

		storeDataSet("datosArchivo", datosArchivo);
		obtenerSalida();
		registrarFin();


	}//fin execute
	
	private void obtenerOrdenesExportarExcel() throws Exception {

		OrdenDAO ordenDAO = new OrdenDAO(_dso);
		SimpleDateFormat formato2 = new SimpleDateFormat(com.bdv.infi.logic.interfaces.ConstantesGenerales.FORMATO_FECHA3);		

		// Recuperamos el dataset con la informacion para exportarla a excel
		Transaccion transaccion = new Transaccion(this._dso);
		Statement statement = null;		
		ResultSet ordenes = null;		

		try {			
			if (record.count() > 0) {
				registrarInicio(obtenerNombreArchivo("sol_simadi"));
				transaccion.begin();
				statement = transaccion.getConnection().createStatement();				
				ordenes = statement.executeQuery(ordenDAO.listarSolicitudesSimadi(record.getValue("fe_ord_desde"), record.getValue("fe_ord_hasta"), true, false, 0, 0, tipo_producto_id,tipo_negocio)); 
				crearCabecera();			
				
				while (ordenes.next()) {					
				
					registroProcesado++;
					escribir(ordenes.getString("undinv_nombre"));escribir(";");
					escribir(ordenes.getString("id_orden"));escribir(";");
					escribir(ordenes.getString("numero_orden_infi"));escribir(";");
					escribir(ordenes.getString("nombre_cliente"));escribir(";");
					escribir(ordenes.getString("ced_rif_cliente"));escribir(";");
					escribir(ordenes.getString("email_cliente")); escribir(";");
					escribir(ordenes.getString("monto_solicitado"));escribir(";");
					escribir(ordenes.getString("monto_adjudicado"));escribir(";");
					escribir(ordenes.getString("monto_comision"));escribir(";");
					escribir(ordenes.getString("porc_comision"));escribir(";");
					
					if((ordenes.getString("tasa_cambio")!=null) && (ordenes.getString("tasa_cambio") != "")){
						escribir(ordenes.getString("tasa_cambio"));escribir(";");
					}else{
						escribir(" ;");
					}
					escribir("'");escribir(ordenes.getString("cuenta_bs"));escribir(";");
					escribir("'");escribir(ordenes.getString("cuenta_en_dolares"));escribir(";");
					escribir(ordenes.getString("estatus"));escribir(";");
					if((ordenes.getString("fecha_registro")!=null) && (ordenes.getString("fecha_registro") != "")){					
						escribir(Utilitario.DateToString(formato2.parse(ordenes.getString("fecha_registro")), "dd/MM/yyyy"));escribir(";");
					}else {
						escribir(" ;");
					}
					escribir(ordenes.getString("hora_registro"));escribir(";");
					if((ordenes.getString("fecha_tramite")!=null) && (ordenes.getString("fecha_tramite") != "")){
						escribir(Utilitario.DateToString(formato2.parse(ordenes.getString("fecha_tramite")), "dd/MM/yyyy"));escribir(";");
					}else{
						escribir(" ;");
					}
					escribir(ordenes.getString("num_bloqueo"));escribir(";");
					escribir(ordenes.getString("cta_abono"));escribir(";");
					escribir(ordenes.getString("cta_abono_moneda")); escribir(";");
					escribir(ordenes.getString("nombre_subasta"));escribir(";");
					escribir(ordenes.getString("id_producto"));escribir(";");
					escribir(ordenes.getString("tipo_solicitud"));escribir(";");
					escribir("\r\n");

				} // fin while

			} // fin if record.count()
		}//fin bloque try

		catch (Exception e) {
			logger.error(this,e);
		} finally{
			if (ordenes != null){
				ordenes.close();
			}

			if (statement != null){
				statement.close();
			}
			if (transaccion != null){
				transaccion.end();
				transaccion.closeConnection();
			}
		}

	}//Fin metodo obtenerOrdenesExportarExcel()


	public String getMensajeError() {
		return mensajeError;
	}

	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}
	

	protected void crearCabecera() throws Exception {
		escribir("Unidad de Inversion; N° orden; N° orden INFI; Cliente; Cedula o RIF; Correo Electronico; Monto Solicitado; Monto Adjudicado; Monto Comision; Porcentaje Comision; Tasa Cambio;" +
				"Cuenta en Bs; Cuenta en Dólares; Estatus; Fecha Registro; Hora Registro; Fecha Tramite; Numero Bloqueo; Cuenta Abono; " +
				"Moneda; Nombre Subasta; Id Producto; Tipo de Solicitud".toUpperCase());
		escribir("\r\n");
	}


}//Fin Clase
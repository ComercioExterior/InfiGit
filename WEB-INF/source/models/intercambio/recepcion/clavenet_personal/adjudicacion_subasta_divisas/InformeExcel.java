package models.intercambio.recepcion.clavenet_personal.adjudicacion_subasta_divisas;

import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import megasoft.DataSet;
import megasoft.Logger;
import models.exportable.ExportableOutputStream;

import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.Transaccion;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi_services.beans.entidades.mensajes_respuesta.UnidadesInversion;

/**
 * Clase que exporta a excel la ordenes que furon adjudicadas o quedaron en proceso de adjudicacion
 * 
 * @author jvillegas
 */
public class InformeExcel extends ExportableOutputStream {

	private boolean ordenSitmeCLaveNet=false;
	DataSet camposDinamicos = new DataSet();
	DataSet titulos = new DataSet();

	@SuppressWarnings("unchecked")
	public void execute() throws Exception {

		Transaccion transaccion = new Transaccion(_dso);
		Statement statement = null;
		ResultSet _exportar = null;

		OrdenDAO ordenDAO = new OrdenDAO(_dso);
		UnidadInversionDAO unidadesInversionDAO = new UnidadInversionDAO(_dso);
		String unidadInversion = "";
		String instFinanciero = "";
		String idEjecucion = "";
		String status="";
		
		String fechaDesde = _req.getParameter("fe_ord_desde"),fechaHasta = _req.getParameter("fe_ord_hasta");

		if (getSessionObject("id_ejecucion_subasta_divisas_personal") != null&&getSessionObject("id_ejecucion_subasta_divisas_personal") != ""){
			idEjecucion=getSessionObject("id_ejecucion_subasta_divisas_personal").toString();		
						
		}
		
		System.out.println("********************* ID EJECUCION SUBASTAS DIVISA PERSONAL: "  + idEjecucion + " ****************************");
		
		/*if(_req.getSession().getAttribute("status")!= null&& _req.getSession().getAttribute("status") != ""){
			status=String.valueOf(_req.getSession().getAttribute("status"));
		}
							
		if (_req.getParameter("unidad_inversion")!= null){
			unidadInversion = _req.getParameter("unidad_inversion");
			
		}else if (getSessionObject("unidadInversion") != null){
			unidadInversion = getSessionObject("unidadInversion").toString();
			
		}else{					
			throw new Exception ("Error, no hay unidad de inversión seleccionada");		
		}*/
		
		SimpleDateFormat formato = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA3);

		try {
			
			// Recuperamos el dataset con la informacion para exportarla a excel
			ArrayList datos = new ArrayList();
			Map beans = new HashMap();
		
			transaccion.begin();
			statement = transaccion.getConnection().createStatement();
		
					if(idEjecucion!="" && !idEjecucion.equals("")){
		
						_exportar = statement.executeQuery(ordenDAO.listarOrdenesPorIdEjecucion(idEjecucion)); 												
					} /*else if(fechaDesde==null || fechaHasta==null){						
						
						_exportar = statement.executeQuery(ordenDAO.listarOrdenesPorUnidadInversion(Long.parseLong(unidadInversion)));
					} else {
						
						_exportar = statement.executeQuery(ordenDAO.listarOrdenesPorFechaTomaOrdenEstatus(fechaDesde,fechaHasta,Long.parseLong(unidadInversion),status));
					
					}*/
					
			registrarInicio(obtenerNombreArchivo("InformeAdjSubastaDivisas"));
			crearCabecera();
			while (_exportar.next()) {
				escribir(_exportar.getString("ordene_id"));
				escribir(";");
				escribir(_exportar.getString("undinv_nombre"));
				escribir(";");
				escribir(_exportar.getString("client_nombre"));
				escribir(";");
				escribir(_exportar.getString("ordsta_nombre"));
				escribir(";");
				escribir(formato.format(_exportar.getDate("ordene_ped_fe_orden")));
				escribir(";");
				escribir(_exportar.getDouble("ordene_ped_monto"));
				escribir(";");				
				escribir(_exportar.getDouble("ordene_ped_precio"));
				escribir(";");
				escribir(_exportar.getDouble("ordene_adj_monto"));
				escribir(";");
				escribir(_exportar.getDouble("ordene_tasa_pool"));
				escribir(";");
				escribir(_exportar.getDouble("ordene_tasa_cambio"));
				escribir(";");
				if (_exportar.getDate("ordene_fecha_adjudicacion") != null){
					escribir(formato.format(_exportar.getDate("ordene_fecha_adjudicacion")));
				}else{
					escribir("");
				}								
				escribir(";\r\n");
			}
			
			registrarFin();			
			obtenerSalida();
			
			//}

		} catch (Exception e) {
			Logger.error(this, "Error en la generación del informe de excel de adjudicación subasta divisas",e);
			throw new Exception("Error en la generación del informe de excel de adjudicación subasta divisas" , e);			
		} finally{
			if (_exportar != null){
				_exportar.close();
			}
			if (statement != null){
				statement.close();
			}
			if (transaccion != null){
				transaccion.closeConnection();
			}
		}
	}
	
	protected void crearCabecera() throws Exception{
		escribir("Orden;Unidad de Inversión;Cliente;Status de la Orden;Fecha Orden;Monto Pedido;Precio Compra;Monto Adjudicado;Tasa Cambio Propuesta;Tasa Cambio Adjudicada;Fecha Adjudicacion;\r\n".toUpperCase());
	}
	
	protected void crearCabeceraSitmeClaveNet() throws Exception{
		escribir("Orden;Unidad de Inversión;Cliente;Status de la Orden;Fecha Orden;Monto Pedido;Precio Compra;Monto Adjudicado;Fecha Adjudicacion;Intereses Caidos USD;Precio Negociado;Fecha Valor;Nro Ticket;\r\n".toUpperCase());
	}
}
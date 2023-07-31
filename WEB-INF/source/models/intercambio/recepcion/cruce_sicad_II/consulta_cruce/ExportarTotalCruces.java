package models.intercambio.recepcion.cruce_sicad_II.consulta_cruce;

import megasoft.DataSet;
import megasoft.Logger;
import models.exportable.ExportableOutputStream;

import com.bdv.infi.dao.OrdenesCrucesDAO;

/**
 * @author eel
 * 
 */
public class ExportarTotalCruces extends ExportableOutputStream {

	public void execute() throws Exception {

		long idUnidad;
		long idCliente;
		String idOrden;
		String idEjecucion;
		String status = null;
		String statusP = null;
		String indTitulo = null;
		
		DataSet record = (DataSet) _req.getSession().getAttribute("filtro_ConsultaCruces");
		//if (record != null){
		idUnidad = Long.valueOf(record.getValue("idUnidadF")==null?"0":record.getValue("idUnidadF"));
		idCliente = Long.valueOf(record.getValue("idClienteF")==null?"0":record.getValue("idClienteF"));
		idEjecucion = record.getValue("idEjecucionF");
		status = record.getValue("statusF");
		idOrden = record.getValue("idOrdenF");
		statusP = record.getValue("statusP");
		indTitulo = record.getValue("indTitulo");
		
		/*}else{
		System.out.println("--- IU>" + _record.getValue("id_unidad"));
		System.out.println("--- IE>" + _record.getValue("id_ejecucion"));
		idUnidad = Long.valueOf(_record.getValue("id_unidad")==null?"0":_record.getValue("id_unidad"));
		idCliente = 0;//Long.valueOf(_record.getValue("idClienteF")==null?"0":_record.getValue("idClienteF"));
		idEjecucion = Long.valueOf(_record.getValue("id_ejecucion")==null?"0":_record.getValue("id_ejecucion"));
		status = null;//_record.getValue("statusF");
		idOrden = null;//_record.getValue("idOrdenF");
		System.out.println("--- IU>" + idUnidad);
		System.out.println("--- IE>" + idEjecucion);
		//}*/
		
		OrdenesCrucesDAO consCruces = new OrdenesCrucesDAO(_dso);
		
		consCruces.totalMontosCruces(idUnidad, status, idCliente, idOrden, idEjecucion);
		DataSet _exportar = new DataSet();
		
		_exportar = consCruces.getDataSet();
			

		// Recuperamos el dataset con la informacion para exportarla a excel

		//DataSet _exportar								= (DataSet)_req.getSession().getAttribute("exportar_excel");
	/*	ArrayList<OrdenesCruce> datos					= new ArrayList();
		Map beans										= new HashMap();*/
		//int registros									= _exportar.count();

		// Se realiza la consulta con los valores almacenados del record
		//DataSet record = (DataSet)_req.getSession().getAttribute("exportar_excel");
		
		
		try {		//Se le da nombre e inicio al documento	
					registrarInicio("Total_Ordenes_Cruce.csv");
					crearCabecera();
					escribir("\r\n");
					
				while (_exportar.next()) {					
					 
						registroProcesado++;
						//se agregan uno por uno los registros
						escribir(_exportar.getValue("ORDENE_ID")==null?" ;":_exportar.getValue("ORDENE_ID")+";");
						escribir(_exportar.getValue("Client_Nombre")==null?" ;":_exportar.getValue("Client_Nombre")+";");
						escribir(_exportar.getValue("Cliente_Cedrif")==null?" ;":_exportar.getValue("Cliente_Cedrif")+";");
						escribir(_exportar.getValue("Unidnv_Nombre")==null?" ;":_exportar.getValue("Unidnv_Nombre")+";");	
						escribir(_exportar.getValue("ESTATUS")==null?" ;":_exportar.getValue("ESTATUS")+";");
						escribir(_exportar.getValue("PROC")==null?" ;":_exportar.getValue("PROC")+";");
						escribir(_exportar.getValue("FECHA_VALOR")==null?" ;":_exportar.getValue("FECHA_VALOR")+";");
						escribir(_exportar.getValue("TASA")==null?" ;":_exportar.getValue("TASA")+";");
						escribir(_exportar.getValue("ordene_ped_monto")==null?" ;":_exportar.getValue("ordene_ped_monto")+";");
						escribir(_exportar.getValue("TOTAL_DIV_DOL")==null?" ;":_exportar.getValue("TOTAL_DIV_DOL")+";");
						escribir(_exportar.getValue("TOTAL_TIT_DOL")==null?" ;":_exportar.getValue("TOTAL_TIT_DOL")+";");
						escribir(_exportar.getValue("TOTAL_DOL")==null?" ;":_exportar.getValue("TOTAL_DOL")+";");
						escribir(_exportar.getValue("TOTAL_DIV_BS")==null?" ;":_exportar.getValue("TOTAL_DIV_BS")+";");
						escribir(_exportar.getValue("TOTAL_TIT_BS")==null?" ;":_exportar.getValue("TOTAL_TIT_BS")+";");
						escribir(_exportar.getValue("TOTAL_BS")==null?" ;":_exportar.getValue("TOTAL_BS")+";");
/*						escribir(_exportar.getValue("ORDENE_ID_BCV")==null?" ;":_exportar.getValue("ORDENE_ID_BCV")+";");
						escribir(_exportar.getValue("NRO_OPERACION")==null?" ;":_exportar.getValue("NRO_OPERACION")+";");
						escribir(_exportar.getValue("TASA")==null?" ;":_exportar.getValue("TASA")+";");
						escribir(_exportar.getValue("ISIN")==null?" ;":_exportar.getValue("ISIN")+";");
						escribir(_exportar.getValue("PRECIO_TITULO")==null?" ;":_exportar.getValue("PRECIO_TITULO")+";");
						escribir(_exportar.getValue("ID_EJECUCION")==null?" ;":_exportar.getValue("ID_EJECUCION")+";");
						escribir(_exportar.getValue("CONTRAPARTE")==null?" ;":_exportar.getValue("CONTRAPARTE")+";");
						escribir(_exportar.getValue("TITULO_ID")==null?" ;":_exportar.getValue("TITULO_ID")+";");
						escribir(_exportar.getValue("OBSERVACION")==null?" ;":_exportar.getValue("OBSERVACION")+";");*/
						
						
						escribir("\r\n");
					} // fin while

				registrarFin();
				obtenerSalida();
			
		} catch (Exception e) {
			_record.addError("Nombre","Error en la exportaci�n del Excel" + "Error:"  + e.getMessage());
			Logger.error(this,"Error en la exportaci�n del Excel",e);
		} 

		// Se limpian los datos de sesion
		//_req.getSession().removeAttribute("exportar_excel");
	}// fin execute

	protected void crearCabecera() throws Exception {
		escribir("Orden INFI;Cliente;C�dula o Rif;Unidad de Inversi�n;Estatus; Cruce Procesado; Fecha Valor;Tasa;Monto Solicitado;Total Divisas$;Total Titulos $; Total $ ;Total Divisas Bs ; Total Titulos Bs;Total Bs; ".toUpperCase()); //Nro.Cotizaci�n***;NroOperacion;Tasa; ISIN; PrecioTitulo;  IdEjecucion; Contraparte;  Titulo_Id;   Observaci�n;".toUpperCase());
	}
	

}// Fin Clase
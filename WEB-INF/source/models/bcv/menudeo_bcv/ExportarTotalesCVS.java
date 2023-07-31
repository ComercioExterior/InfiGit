package models.bcv.menudeo_bcv;

import megasoft.DataSet;
import megasoft.Logger;
import models.exportable.ExportableOutputStream;
import com.bdv.infi.dao.OrdenesCrucesDAO;
import com.bdv.infi.logic.interfaces.ActionINFI;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

/**
 * 
 * @author nm36635
 *
 */
public class ExportarTotalesCVS extends ExportableOutputStream {
DataSet _ordenes = new DataSet();
OrdenesCrucesDAO ordenesCrucesDAO;
String statusP = null;
String statusE = null;
String Tipo = null;
String fecha   = null;
Integer clienteID;
	
	public void execute() throws Exception {
		
		this._ordenes = new DataSet();
		capturarValoresRecord();
	
		this.ordenesCrucesDAO = new OrdenesCrucesDAO(_dso);
		ordenesCrucesDAO.listarOrdenesPorEnviarMenudeoBCV1(true, false, getNumeroDePagina(),getPageSize(), true, statusP, fecha,statusE,Tipo,"",clienteID,true,"0");
		_ordenes = ordenesCrucesDAO.getDataSet();
			
			try {
				registrarInicio("ordenesTotales.csv");
				
					if(statusE.equals(ConstantesGenerales.ENVIO_MENUDEO_FALTANTES)){ //FUE APROBADA
						crearCabecera("MONEDA;MONTO TOTALES ;CANTIDAD OPERACIONES;TIPO OPERACIONES;");
					}else{
						crearCabecera("MONEDA;MONTO TOTALES ;CANTIDAD OPERACIONES;TIPO OPERACIONES;");
					} 				
				 
				escribir("\r\n");
	
					while(_ordenes.next()) {					
						registroProcesado++;
						//se agregan uno por uno los registros
						escribir(_ordenes.getValue("MONEDA")==null?" ;":_ordenes.getValue("MONEDA")+";");
						escribir(_ordenes.getValue("monto_operacion")==null?" ;":_ordenes.getValue("monto_operacion")+";");
						escribir(_ordenes.getValue("cantidad_operaciones")==null?" ;":_ordenes.getValue("cantidad_operaciones")+";");//////////////////////
						escribir(_ordenes.getValue("movimiento")==null?" ;":_ordenes.getValue("movimiento").replaceAll("\n", "")+";");
						escribir("\r\n");
					}
					
				registrarFin();
				obtenerSalida();
			} catch (Exception e) {
				_record.addError("Nombre","Error en la exportación del Excel" + "Error:"  + e.getMessage());
				Logger.error(this,"Error en la exportación del Excel",e);
			} 
	
	}
	
	
/**
 * @throws Exception 
 * capturar los valores para la consulta a base de datos
 */
	public void capturarValoresRecord() throws Exception{
		
		this.statusP = _record.getValue("statusp");
		this.statusE = _record.getValue("statuse");
		this.Tipo = _record.getValue("tipo");
		this.fecha = (String) _record.getValue("fecha");
		this.clienteID = Integer.parseInt(_req.getParameter("cliente_id") == null ? "0" : _req.getParameter("cliente_id"));
		
	}
	
/**
 * metodo para escribir la cabecera
 * @param cabecera
 * @throws Exception
 */
	protected void crearCabecera(String cabecera) throws Exception {
		escribir(cabecera.toUpperCase());
	}
	

}
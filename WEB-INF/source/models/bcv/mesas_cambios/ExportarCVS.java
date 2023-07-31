package models.bcv.mesas_cambios;

import megasoft.DataSet;
import megasoft.Logger;
import models.exportable.ExportableOutputStream;
import com.bdv.infi.dao.OrdenesCrucesDAO;
import com.bdv.infi.logic.interfaces.ActionINFI;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;

/**
 * clase para general el excel
 * @author nm36635
 *
 */
public class ExportarCVS extends ExportableOutputStream {
	DataSet _ordenes;
	String statusP = null;
	String statusE = null;
	String Tipo = null;
	String fecha   = null;
	Integer clienteID;

	public void execute() throws Exception {
		this._ordenes = new DataSet();
		capturarValoresRecord();
			
		OrdenesCrucesDAO ordenesCrucesDAO = new OrdenesCrucesDAO(_dso);
		ordenesCrucesDAO.listarOrdenesPorEnviarMenudeoBCV1(false, true, 0,0, true, statusP, fecha,statusE,Tipo,"",clienteID,true,"0");
		_ordenes = ordenesCrucesDAO.getDataSet();
			System.out.println("cantidad de operaciones al excel-->"+_ordenes.count());
			try {
				
				registrarInicio("ordenes2.csv");
				
					if(statusE.equals(ConstantesGenerales.ENVIO_MENUDEO_FALTANTES)){
						crearCabecera("ID OPERACION;OPERACION;NOMBRE CLIENTE ;NACIONALIDAD;CEDULA O RIF;MONTO DIVISAS;MONTO BOLIVARES;TASA CAMBIO;DIVISA;CUENTA CLIENTE;OFICINA;CONCEPTO ESTADISTICO; FECHA; EMAIL;TELEFONO;ESTATUS; ID BCV;CONTRA VALOR EN USD;OBSERVACION");
					}else {
						crearCabecera("ID OPERACION;OPERACION;NOMBRE CLIENTE ;NACIONALIDAD;CEDULA O RIF;MONTO DIVISAS;MONTO BOLIVARES;TASA CAMBIO;DIVISA;CUENTA CLIENTE;OFICINA;CONCEPTO ESTADISTICO; FECHA; EMAIL;TELEFONO;ESTATUS;ID BCV;CONTRA VALOR EN USD;OBSERVACION");
					} 				
				 
				escribir("\r\n");
				
					while(_ordenes.next()){					
						registroProcesado++;
						escribir(_ordenes.getValue("ID_OC")==null?" ;":_ordenes.getValue("ID_OC")+";");
						escribir(_ordenes.getValue("movimiento")==null?" ;":_ordenes.getValue("movimiento")+";");
						escribir(_ordenes.getValue("NOM_CLIEN")==null?" ;":_ordenes.getValue("NOM_CLIEN")+";");
						escribir(_ordenes.getValue("NACIONALIDAD")==null?" ;":_ordenes.getValue("NACIONALIDAD")+";");
						escribir(_ordenes.getValue("CED_RIF")==null?" ;":_ordenes.getValue("CED_RIF")+";");
						escribir(_ordenes.getValue("MTO_DIVISAS")==null?" ;":_ordenes.getValue("MTO_DIVISAS")+";");
						escribir(_ordenes.getValue("MTO_BOLIVARES")==null?" ;":_ordenes.getValue("MTO_BOLIVARES")+";");
						escribir(_ordenes.getValue("TASA_CAMBIO")==null?" ;":_ordenes.getValue("TASA_CAMBIO")+";");
						escribir(_ordenes.getValue("COD_DIVISAS")==null?" ;":_ordenes.getValue("COD_DIVISAS")+";");
						escribir(_ordenes.getValue("CTA_CLIEN")==null?" ;":_ordenes.getValue("CTA_CLIEN")+";");
						escribir(_ordenes.getValue("COD_OFI_ORI")==null?" ;":_ordenes.getValue("COD_OFI_ORI")+";");
						escribir(_ordenes.getValue("estadistica")==null?" ;":_ordenes.getValue("estadistica")+";");
						escribir(_ordenes.getValue("FECH_OPER")==null?" ;":_ordenes.getValue("FECH_OPER")+";");
						escribir(_ordenes.getValue("EMAIL_CLIEN")==null?" ;":_ordenes.getValue("EMAIL_CLIEN")+";");
						escribir(_ordenes.getValue("TEL_CLIEN")==null?" ;":_ordenes.getValue("TEL_CLIEN")+";");
						escribir(_ordenes.getValue("Estatus")==null?" ;":_ordenes.getValue("Estatus")+";");					
						escribir(_ordenes.getValue("ID_BCV")==null?" ;":_ordenes.getValue("ID_BCV")+";");
						escribir(_ordenes.getValue("MTO_DIVISAS_TRANS")==null?" ;":_ordenes.getValue("MTO_DIVISAS_TRANS")+";");
						escribir(_ordenes.getValue("OBSERVACION")==null?" ;":_ordenes.getValue("OBSERVACION")+";");
//						
						
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
* captura las variables de la segunda vista a una 3era vista y se captura con _req
 * @throws Exception 
*/
	public void capturarValoresRecord() throws Exception{
		this.statusP = _record.getValue("statusp");
		this.statusE = _record.getValue("statuse");
		this.Tipo  = _record.getValue("tipo");
		this.fecha  = (String) _record.getValue("fecha");
		this.clienteID = Integer.parseInt(_req.getParameter("cliente_id")== null ? "0" : _req.getParameter("cliente_id"));
	}

/**
 * metodo para crear cabecera
 * @param cabecera
 * @throws Exception
 */
	protected void crearCabecera(String cabecera) throws Exception {
		escribir(cabecera.toUpperCase());
		
	}
	

}
package models.bcv.mesas_cambios_inter;

import megasoft.DataSet;
import megasoft.Logger;
import models.exportable.ExportableOutputStream;
import com.bdv.infi.dao.MesaCambioDAO;

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
	String tipoOperacion   = null;
	Integer clienteID;

	public void execute() throws Exception {
		this._ordenes = new DataSet();
		capturarValoresRecord();
		MesaCambioDAO mesa = new MesaCambioDAO(_dso);
		System.out.println(" DESCARGA DEL EXCEL MESA DE CAMBIO ");
			try {
				System.out.println("tipoOperacion :: " + tipoOperacion);
			if (tipoOperacion.equalsIgnoreCase("O")  || tipoOperacion.equalsIgnoreCase("D")) {
				mesa.ListarInterSinPaginador(fecha, statusE, tipoOperacion);
				_ordenes = mesa.getDataSet();
				registrarInicio("intermesaa.csv");
				crearCabecera("id; tipo operacion; tipo cliente; rif cliente; nombre cliente; codigo moneda; monto; tasa; fecha; codigo institucion; jornada; cuenta me; cuenta mn; instrumento; observacion; estatus; id bcv");
				escribir("\r\n");
				while(_ordenes.next()){					
//					registroProcesado++;
					escribir(_ordenes.getValue("ID") == null ? " ;" : _ordenes.getValue("ID") + ";");
					escribir(_ordenes.getValue("TIPO_OPER") == null ? " ;" : _ordenes.getValue("TIPO_OPER") + ";");
					escribir(_ordenes.getValue("TIPO_CLIENTE") == null ? " ;" : _ordenes.getValue("TIPO_CLIENTE") + ";");
					escribir(_ordenes.getValue("RIF_CLIENTE") == null ? " ;" : _ordenes.getValue("RIF_CLIENTE") + ";");
					escribir(_ordenes.getValue("NOMBRE_CLIENTE") == null ? " ;" : _ordenes.getValue("NOMBRE_CLIENTE") + ";");
					escribir(_ordenes.getValue("CODIGO_MONEDA") == null ? " ;" : _ordenes.getValue("CODIGO_MONEDA") + ";");
					escribir(_ordenes.getValue("MONTO") == null ? " ;" : _ordenes.getValue("MONTO") + ";");
					escribir(_ordenes.getValue("TASA_CAMBIO") == null ? " ;" : _ordenes.getValue("TASA_CAMBIO") + ";");
					escribir(_ordenes.getValue("FECHA") == null ? " ;" : _ordenes.getValue("FECHA") + ";");
					escribir(_ordenes.getValue("CODIGO_INSTITUCION") == null ? " ;" : _ordenes.getValue("CODIGO_INSTITUCION") + ";");
					escribir(_ordenes.getValue("ID_JORNADA") == null ? " ;" : _ordenes.getValue("ID_JORNADA") + ";");
					escribir(_ordenes.getValue("CUENTA_ME") == null ? " ;" : _ordenes.getValue("CUENTA_ME") + ";");
					escribir(_ordenes.getValue("CUENTA_MN") == null ? " ;" : _ordenes.getValue("CUENTA_MN") + ";");
					escribir(_ordenes.getValue("TIPO_INSTRUMENTO") == null ? " ;" : _ordenes.getValue("TIPO_INSTRUMENTO") + ";");
					escribir(_ordenes.getValue("OBSERVACION") == null ? " ;" : _ordenes.getValue("OBSERVACION") + ";");
					escribir(_ordenes.getValue("ESTATUS") == null ? " ;" : _ordenes.getValue("ESTATUS") + ";");					
					escribir(_ordenes.getValue("ID_BCV") == null ? " ;" : _ordenes.getValue("ID_BCV") + ";");
					escribir("\r\n");
				}
				
			registrarFin();
			obtenerSalida();
			}else{
				mesa.ListarPactosSinPaginador(fecha);
				_ordenes = mesa.getDataSet();
				registrarInicio("pacto.csv");
				
				crearCabecera("id; jornada; tipo operacion; codigo oferta; codigo demanda; monto; tasa; fecha; monto base; monto contravalor; tipo pacto; observacion; estatus; id bcv");
				
				escribir("\r\n");
					while(_ordenes.next()){					
						
						escribir(_ordenes.getValue("id") == null ? " ;" : _ordenes.getValue("id") + ";");
						escribir(_ordenes.getValue("ID_JORNADA") == null ? " ;" : _ordenes.getValue("ID_JORNADA") + ";");
						escribir(_ordenes.getValue("TIPO_OPER") == null ? " ;" : _ordenes.getValue("TIPO_OPER") + ";");
						escribir(_ordenes.getValue("CODIGO_OFERTA") == null ? " ;" : _ordenes.getValue("CODIGO_OFERTA") + ";");
						escribir(_ordenes.getValue("CODIGO_DEMANDA") == null ? " ;" : _ordenes.getValue("CODIGO_DEMANDA") + ";");
						escribir(_ordenes.getValue("MONTO") == null ? " ;" : _ordenes.getValue("MONTO")+";");
						escribir(_ordenes.getValue("TASA_CAMBIO") == null ? " ;" : _ordenes.getValue("TASA_CAMBIO") + ";");
						escribir(_ordenes.getValue("FECHA") == null ? " ;" : _ordenes.getValue("FECHA") + ";");
						escribir(_ordenes.getValue("MONTO_PACTO_BASE") == null ? " ;" : _ordenes.getValue("MONTO_PACTO_BASE") + ";");
						escribir(_ordenes.getValue("MONTO_CONTRAVALOR_BASE") == null ? " ;" : _ordenes.getValue("MONTO_CONTRAVALOR_BASE") + ";");					
						escribir(_ordenes.getValue("TIPO_PACTO") == null ? " ;" : _ordenes.getValue("TIPO_PACTO") + ";");
						escribir(_ordenes.getValue("OBSERVACION") == null ? " ;" : _ordenes.getValue("OBSERVACION") + ";");
						escribir(_ordenes.getValue("ESTATUS") == null ? " ;" : _ordenes.getValue("ESTATUS") + ";");
						escribir(_ordenes.getValue("ID_BCV") == null ? " ;" : _ordenes.getValue("ID_BCV") + ";");
						escribir("\r\n");
					}
					
				registrarFin();
				obtenerSalida();
			}			
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
		this.tipoOperacion = _record.getValue("tipooper");
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
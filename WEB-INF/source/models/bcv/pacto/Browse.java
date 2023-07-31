package models.bcv.pacto;

import java.math.BigInteger;
import java.net.URL;
//import java.sql.SQLClientInfoException;
import java.util.Hashtable;

import megasoft.DataSet;
import megasoft.Logger;
import models.msc_utilitys.MSCModelExtend;

import org.apache.axis.transport.http.HTTPConstants;
import org.apache.poi.hssf.record.Record;
import org.bcv.serviceAltoValor.BancoUniversalPortBindingStub;

import com.bdv.infi.config.Propiedades;
import com.bdv.infi.dao.CredencialesDAO;
import com.bdv.infi.dao.OrdenesCrucesDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ActionINFI;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.util.Utilitario;
import com.bdv.infi.webservices.beans.PactoBCV;
import com.bdv.infi.webservices.beans.PactoBCVRespuesta;
import com.bdv.infi.webservices.client.ClienteWs;

import criptografia.TripleDes;

public class Browse extends MSCModelExtend {
	private DataSet datosFilter;
	Integer tasaMinima = null;
	Integer tasaMaxima = null;
	Integer montoMinimo = null;
	Integer montoMaximo = null;
	Integer clienteID = null;
	Integer incluirCliente = null;
	Integer origen = null;
	CredencialesDAO credencialesDAO = null;
	DataSet        _credenciales = new DataSet();
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {
		DataSet _ordenes = new DataSet();
		DataSet _totales = new DataSet();
		DataSet _unidadInversion = new DataSet();
		long idUnidad;
		String statusP = null;
		String fecha   = null;
		idUnidad = Long.valueOf(_record.getValue("ui_id")==null?"0":_record.getValue("ui_id"));
		statusP  =          _record.getValue("statusP");		
		fecha    = (String) _record.getValue("fecha");
		String estatusOrdenINFI = ConstantesGenerales.STATUS_CRUZADA;
		String tipoNegocio = ConstantesGenerales.TIPO_NEGOCIO_ALTO_VALOR;
		String cruceProcesado = "0";
		
		datosFilter = new DataSet();
		datosFilter.append("statusp", java.sql.Types.VARCHAR);
		datosFilter.append("boton_procesar", java.sql.Types.VARCHAR);
		datosFilter.append("fecha", java.sql.Types.VARCHAR);
		datosFilter.append("ui_id", java.sql.Types.VARCHAR);
		datosFilter.append("tasa_minima", java.sql.Types.VARCHAR);
		datosFilter.append("tasa_maxima", java.sql.Types.VARCHAR);
		datosFilter.append("monto_minimo", java.sql.Types.VARCHAR);
		datosFilter.append("monto_maximo", java.sql.Types.VARCHAR);
		datosFilter.append("monto_operacion", java.sql.Types.VARCHAR);
		datosFilter.append("cantidad_operaciones", java.sql.Types.VARCHAR);
		datosFilter.append("cliente_id", java.sql.Types.VARCHAR);
		datosFilter.append("incluir_cliente", java.sql.Types.VARCHAR);
		datosFilter.addNew();
		datosFilter.setValue("statusp", statusP);
		datosFilter.setValue("fecha", fecha);
		datosFilter.setValue("ui_id", String.valueOf(idUnidad));
		datosFilter.setValue("tasa_minima", String.valueOf(tasaMinima));
		datosFilter.setValue("tasa_maxima", String.valueOf(tasaMaxima));
		datosFilter.setValue("monto_minimo", String.valueOf(montoMinimo));
		datosFilter.setValue("monto_maximo", String.valueOf(montoMaximo));
		datosFilter.setValue("cliente_id", String.valueOf(clienteID));
		datosFilter.setValue("incluir_cliente", String.valueOf(incluirCliente));
		
		if(statusP.equals(ConstantesGenerales.SIN_VERIFICAR) && origen != Integer.parseInt(ConstantesGenerales.ORIGEN_BCV_INTERBANCARIO)){
			datosFilter.setValue("boton_procesar", " <button id='btnProcesar' name='btnProcesar' onclick='procesar()'>Procesar</button>&nbsp;");
		}else if(statusP.equals(ConstantesGenerales.VERIFICADA_APROBADA) || statusP.equals(ConstantesGenerales.VERIFICADA_RECHAZADA)){
			datosFilter.setValue("boton_procesar", " <button id='btnProcesar' name='btnProcesar' onclick='procesados()' >Procesar</button>&nbsp;");
		}else {
			datosFilter.setValue("boton_procesar", " <button id='btnProcesar' name='btnProcesar' onclick='procesadosManual()' >Procesar</button>&nbsp;");
		}
		
		UnidadInversionDAO unidadInversionDAO = new UnidadInversionDAO(_dso);
		unidadInversionDAO.consultarStatusUI(idUnidad);
		storeDataSet("statusUI", unidadInversionDAO.getDataSet());
		
		String urlInvocacion = _req.getPathInfo();
		if(urlInvocacion.equals(ActionINFI.WEB_SERVICE_PACTO_FILTER.getNombreAccion())){ //PACTO
			if(origen == Integer.parseInt(ConstantesGenerales.ORIGEN_BANCO_DE_VENEZUELA)){
				OrdenesCrucesDAO ordenesCrucesDAO = new OrdenesCrucesDAO(_dso);
				//SE CONSULTAN LOS REGISTROS
				ordenesCrucesDAO.listarOrdenesPorEnviarBCV(cruceProcesado,tipoNegocio,incluirCliente,clienteID,tasaMinima,tasaMaxima,montoMinimo,montoMaximo,false, idUnidad, true, getNumeroDePagina(),getPageSize(), true, false, "", statusP, fecha, estatusOrdenINFI);
				_ordenes = ordenesCrucesDAO.getDataSet();	
				
				//SE CONSULTAN LOS TOTALES
				ordenesCrucesDAO.listarOrdenesPorEnviarBCV(cruceProcesado,tipoNegocio,incluirCliente,clienteID,tasaMinima,tasaMaxima,montoMinimo,montoMaximo,true, idUnidad, false, getNumeroDePagina(),getPageSize(), true, false, "", statusP, fecha, estatusOrdenINFI);
				_totales = ordenesCrucesDAO.getDataSet();
				
				_totales.next();
				
				Double montoOperacion = new Double(0);
				Integer cantidadOperaciones = 0;
				montoOperacion      +=  _totales.getValue("monto_operacion")     ==null ? new Double(0) : Double.parseDouble(_totales.getValue("monto_operacion"));
				cantidadOperaciones +=  _totales.getValue("cantidad_operaciones")  ==null ? 0 : Integer.parseInt(_totales.getValue("cantidad_operaciones"));
				
				datosFilter.setValue("monto_operacion", String.valueOf(montoOperacion));
				datosFilter.setValue("cantidad_operaciones", String.valueOf(cantidadOperaciones));
				
				storeDataSet("datos", ordenesCrucesDAO.getTotalRegistros(false));
				storeDataSet("rows", _ordenes);		
				storeDataSet("datosFilter", datosFilter);
				getSeccionPaginacion(cantidadOperaciones, getPageSize(), getNumeroDePagina());
			}else if(origen == Integer.parseInt(ConstantesGenerales.ORIGEN_BCV_INTERBANCARIO)) {
				credencialesDAO = new CredencialesDAO(_dso);
				credencialesDAO.listarCredencialesPorTipo(ConstantesGenerales.WS_BCV_ALTO_VALOR);
				_credenciales = credencialesDAO.getDataSet();
				Propiedades propiedades =  Propiedades.cargar();
				String userName = "";
				String clave    = "";
				
				if(_credenciales.next()){
					//SOLO SE ESTA CONFIGURADO QUE SE USARA PROXY
					if(propiedades.getProperty("use_https_proxy").equals("1")){
						Utilitario.configurarProxy();
					}
					
					String rutaCustodio1 = propiedades.getProperty(ConstantesGenerales.RUTA_CUSTODIO_1);
					String rutaCustodio2 = propiedades.getProperty(ConstantesGenerales.RUTA_CUSTODIO_2);

					TripleDes desc = new TripleDes();
					
					userName = desc.descifrar(rutaCustodio1,rutaCustodio2, _credenciales.getValue("USUARIO"));
					clave    = desc.descifrar(rutaCustodio1,rutaCustodio2, _credenciales.getValue("CLAVE"));
				}else {
					Logger.error(this, "Ha ocurrido un error al momento de buscar el usuario y el password del WS de BCV. Sistema buscado: "+ConstantesGenerales.WS_BCV_MENUDEO);
					throw new org.bcv.service.Exception();
				}
				
				
				BancoUniversalPortBindingStub stub = new BancoUniversalPortBindingStub(new URL(propiedades.getProperty(ConstantesGenerales.END_POINT_BCV_ALTO_VALOR)), null);
				
				Hashtable headers = (Hashtable) stub._getProperty(HTTPConstants.REQUEST_HEADERS);
				if (headers == null) {
					headers = new Hashtable();
					stub._setProperty(HTTPConstants.REQUEST_HEADERS, headers);
				}
				headers.put("Username", userName);
				headers.put("Password", clave);
				
				String nroJornada, nombreUI;
				_unidadInversion = unidadInversionDAO.consultarUI(idUnidad);
				_unidadInversion.next();
				
				if(_unidadInversion.getValue("NRO_JORNADA") == null){
					Logger.error(this, "Ha ocurrido un error al momento de buscar el numero dejornada de la Unidad de Inversion: "+idUnidad+". No se puede continuar la operacion");
					throw new Exception();
				}else {
					nombreUI = _unidadInversion.getValue("undinv_nombre") == null ? "" : _unidadInversion.getValue("undinv_nombre");
					nroJornada = _unidadInversion.getValue("NRO_JORNADA") == null ? "" : _unidadInversion.getValue("NRO_JORNADA");
				}
				
				try {
					BigInteger monto = new BigInteger("0");
					Integer cantidadOperaciones = 0;
					String xmlRespuesta=null;
					PactoBCVRespuesta pactoBCVRespuesta;
					ClienteWs clienteWs = new ClienteWs();
					xmlRespuesta = stub.GETPACTOXML(nroJornada); //NUMERO DE JORNADA DE LA UNIDAD DE INVERSION
					pactoBCVRespuesta=(PactoBCVRespuesta) clienteWs.convertirXmlAObjeto(xmlRespuesta,PactoBCVRespuesta.class);
					if(pactoBCVRespuesta!=null&&pactoBCVRespuesta.getPactosBCVList()!=null){
						_ordenes.append("undinv_nombre", java.sql.Types.VARCHAR);
						_ordenes.append("id_oferta", java.sql.Types.VARCHAR);
						_ordenes.append("ordene_id_bcv_of", java.sql.Types.VARCHAR);
						_ordenes.append("monto_ofertado", java.sql.Types.VARCHAR);
						_ordenes.append("ordene_id", java.sql.Types.VARCHAR);
						_ordenes.append("ordene_id_bcv_de", java.sql.Types.VARCHAR);
						_ordenes.append("ordene_adj_monto", java.sql.Types.VARCHAR);
						_ordenes.append("ordene_id_pacto_bcv", java.sql.Types.VARCHAR);
						_ordenes.append("client_nombre_de", java.sql.Types.VARCHAR);
						_ordenes.append("client_cedrif_de", java.sql.Types.VARCHAR);	
						_ordenes.append("tipper_id_de", java.sql.Types.VARCHAR);
						_ordenes.append("client_nombre_of", java.sql.Types.VARCHAR);
						_ordenes.append("tipper_id_of", java.sql.Types.VARCHAR);
						_ordenes.append("client_cedrif_of", java.sql.Types.VARCHAR);
						_ordenes.append("ordene_tasa_pool", java.sql.Types.VARCHAR);
						_ordenes.append("estatus_string", java.sql.Types.VARCHAR);
						_ordenes.append("observacion", java.sql.Types.VARCHAR);
						
						for (PactoBCV pacto : pactoBCVRespuesta.getPactosBCVList()) {
							if(pacto.getDemandante().equals("G200001100") || pacto.getOfertante().equals("G200001100")){ //SI EL DEMANDANTE ES BCV								
								cantidadOperaciones++;
								monto = monto.add(new BigInteger(pacto.getMontoPacto()));
								_ordenes.addNew();
								_ordenes.setValue("undinv_nombre", nombreUI);
								_ordenes.setValue("ordene_id_bcv_of", pacto.getIdOfertaBCV());
								_ordenes.setValue("monto_ofertado", pacto.getMontoPacto());
								_ordenes.setValue("ordene_id_bcv_de", pacto.getIdDemandaBCV());
								_ordenes.setValue("ordene_adj_monto", pacto.getMontoPacto());
								_ordenes.setValue("ordene_id_pacto_bcv", pacto.getIdPacto());
								_ordenes.setValue("client_nombre_de", "");
								_ordenes.setValue("client_cedrif_de", pacto.getDemandante());
								_ordenes.setValue("tipper_id_de", "");
								_ordenes.setValue("client_nombre_of", "");
								_ordenes.setValue("tipper_id_of", "");
								_ordenes.setValue("client_cedrif_of", pacto.getOfertante());
								_ordenes.setValue("ordene_tasa_pool", pacto.getTasaCambio());
								_ordenes.setValue("estatus_string", "PACTO_BCV_AUTOMATICO");
								_ordenes.setValue("observacion", "");
							}
						}
						
						datosFilter.setValue("monto_operacion", monto.toString());
						datosFilter.setValue("cantidad_operaciones", cantidadOperaciones.toString());
						
						storeDataSet("datosFilter", datosFilter);
						storeDataSet("rows", _ordenes);	
						storeDataSet("datos", _ordenes);
						getSeccionPaginacion(cantidadOperaciones, getPageSize(), getNumeroDePagina());
					}else {
						Logger.error(this, "No se han encontrado operaciones para la jornada seleccionada. Jornada Buscada:  "+nroJornada);
					}
				} catch (Exception e) {
					storeDataSet("datosFilter", new DataSet());
					storeDataSet("rows",  new DataSet());	
					storeDataSet("datos",  new DataSet());
					// TODO Auto-generated catch block
					Logger.error(this, "Han ocurrido un error en el proceso de Consulta de Pactos  " + e.getMessage() + " En jornada: " + nroJornada==null||nroJornada.equals("")?" Numero de Jornada Vacio ":nroJornada );
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public boolean isValid() throws Exception {
		boolean valido = true;
		
		try {
			tasaMinima     = Integer.parseInt(_record.getValue("tasa_minima")== null ? "0"     : _record.getValue("tasa_minima"));
			tasaMaxima     = Integer.parseInt(_record.getValue("tasa_maxima")== null ? "0"     : _record.getValue("tasa_maxima") );
			montoMinimo    = Integer.parseInt(_record.getValue("monto_minimo")== null ? "0"    : _record.getValue("monto_minimo"));
			montoMaximo    = Integer.parseInt(_record.getValue("monto_maximo")== null ? "0"    : _record.getValue("monto_maximo"));
			clienteID      = Integer.parseInt(_record.getValue("client_id")== null ? "0"       : _record.getValue("client_id"));
			incluirCliente = Integer.parseInt(_record.getValue("incluir_cliente")== null ? "0" : _record.getValue("incluir_cliente"));
			origen         = Integer.parseInt(_record.getValue("origen")== null ? "0"          : _record.getValue("origen"));
		} catch (NumberFormatException e) {
			_record.addError(
					"Error: ",
					"Debe Introducir un número valido");
			return false;
		}
		
		
		if(tasaMinima > tasaMaxima && tasaMaxima != 0){
			_record.addError(
					"Error: ",
					"La tasa mínima no puede ser mayor a la tasa máxima ");
			valido = false;
		}
		
		if(montoMinimo > montoMaximo && montoMaximo != 0 ){
			_record.addError(
					"Error: ",
					"El monto mínimo no puede ser mayor al monto máximo ");
			valido = false;
		}
		
		if(_record.getValue("ui_id")==null){
			_record.addError(
					"Error: ",
					"Debe ingresar la unidad de inversion ");
			valido = false;
		}
			
		return valido;
	}
}
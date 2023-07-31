package models.bcv.consulta_ofertas_demandas_bcv;

import java.math.BigInteger;
import java.net.URL;
import java.util.Hashtable;

import megasoft.DataSet;
import megasoft.Logger;
import models.msc_utilitys.MSCModelExtend;

import org.apache.axis.transport.http.HTTPConstants;
import org.bcv.serviceAltoValor.BancoUniversalPortBindingStub;

import com.bdv.infi.config.Propiedades;
import com.bdv.infi.dao.CredencialesDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.util.Utilitario;
import com.bdv.infi.webservices.beans.DemandaXMLBCV;
import com.bdv.infi.webservices.beans.DemandasXMLBCVRespuesta;
import com.bdv.infi.webservices.beans.OfertaBCV;
import com.bdv.infi.webservices.beans.OfertaBCVRespuesta;
import com.bdv.infi.webservices.beans.PactoBCVRespuesta;
import com.bdv.infi.webservices.client.ClienteWs;

import criptografia.TripleDes;

public class Browse extends MSCModelExtend {
	private DataSet datosFilter, _credenciales;
	Integer tasaMinima = null;
	Integer tasaMaxima = null;
	Integer montoMinimo = null;
	Integer montoMaximo = null;
	Integer clienteID = null;
	Integer incluirCliente = null;
	String  origen = null;
	String estatusCruce = null;
	Integer tipoOperacion = null;
	Integer blotterID = null;
	long idUnidad;
	UnidadInversionDAO unidadInversionDAO;
	DataSet _unidadInversion = new DataSet();
	String nroJornada, nombreUI;
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	@SuppressWarnings("null")
	public void execute() throws Exception {
		DataSet _ordenes = new DataSet();
		CredencialesDAO credencialesDAO;
		String statusP = null;
		String fecha   = null;
		idUnidad = Long.valueOf(_record.getValue("ui_id")==null?"0":_record.getValue("ui_id"));
		statusP  =          _record.getValue("statusP") == null?"": _record.getValue("statusP");		
		fecha    = (String) _record.getValue("fecha");
		
		datosFilter = new DataSet();
		datosFilter.append("statusp", java.sql.Types.VARCHAR);
		datosFilter.append("boton_procesar", java.sql.Types.VARCHAR);
		datosFilter.append("fecha", java.sql.Types.VARCHAR);
		datosFilter.append("ui_id", java.sql.Types.VARCHAR);
		datosFilter.append("tasa_minima", java.sql.Types.VARCHAR);
		datosFilter.append("tasa_maxima", java.sql.Types.VARCHAR);
		datosFilter.append("monto_minimo", java.sql.Types.VARCHAR);
		datosFilter.append("monto_maximo", java.sql.Types.VARCHAR);
		datosFilter.append("cantidad_operaciones", java.sql.Types.VARCHAR);
		datosFilter.append("monto_operacion", java.sql.Types.VARCHAR);
		datosFilter.append("cliente_id", java.sql.Types.VARCHAR);
		datosFilter.append("incluir_cliente", java.sql.Types.VARCHAR);
		datosFilter.append("origen", java.sql.Types.VARCHAR);
		datosFilter.append("estatus_cruce", java.sql.Types.VARCHAR);
		datosFilter.append("tipo_operacion", java.sql.Types.VARCHAR);
		datosFilter.append("bloter_id", java.sql.Types.VARCHAR);
		datosFilter.addNew();
		datosFilter.setValue("statusp", statusP);
		datosFilter.setValue("fecha", fecha);
		datosFilter.setValue("ui_id", String.valueOf(idUnidad));
		datosFilter.setValue("tasa_minima", String.valueOf(tasaMinima));
		datosFilter.setValue("tasa_maxima", String.valueOf(tasaMaxima));
		datosFilter.setValue("monto_minimo", String.valueOf(montoMinimo));
		datosFilter.setValue("monto_maximo", String.valueOf(montoMaximo));
		datosFilter.setValue("cliente_id", String.valueOf(clienteID));
		datosFilter.setValue("incluir_cliente",String.valueOf(incluirCliente));
		datosFilter.setValue("origen", String.valueOf(origen));
		datosFilter.setValue("estatus_cruce", String.valueOf(estatusCruce));
		datosFilter.setValue("tipo_operacion", String.valueOf(tipoOperacion));
		datosFilter.setValue("bloter_id", String.valueOf(blotterID));
		
		Integer ofertaAnulacion  = Integer.parseInt(ConstantesGenerales.OFERTA_ANULACION);
		Integer demandaAnulacion = Integer.parseInt(ConstantesGenerales.DEMANDA_ANULACION);
		
		UnidadInversionDAO statusUI = new UnidadInversionDAO(_dso);
		statusUI.consultarStatusUI(idUnidad);
		storeDataSet("statusUI", statusUI.getDataSet());
	
		credencialesDAO = new CredencialesDAO(_dso);
		credencialesDAO.listarCredencialesPorTipo(ConstantesGenerales.WS_BCV_ALTO_VALOR);
		_credenciales = credencialesDAO.getDataSet();
		Propiedades propiedades =  Propiedades.cargar();
		String userName = "";
		String clave    = "";
		
		if(_credenciales.next()){
			//se carga el certificado autofirmado del BDV y se configura el proxy
			//Utilitario.cargarCertificado(propiedades.getProperty(ConstantesGenerales.RUTA_CER_ALTO_VALOR_BCV));
			//System.setProperty("sun.security.ssl.allowUnsafeRenegotiation","true");
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

		
		try {
		
			if(tipoOperacion == demandaAnulacion){ //DEMANDA ALTO VALOR				

				BigInteger monto = new BigInteger("0");
				Integer cantidadOperaciones = 0;
				String xmlRespuesta=null;
				DemandasXMLBCVRespuesta demandasXMLBCVRespuesta;
				ClienteWs clienteWs = new ClienteWs();
				xmlRespuesta = stub.DEMANDASXML(nroJornada); //NUMERO DE JORNADA DE LA UNIDAD DE INVERSION
				demandasXMLBCVRespuesta=(DemandasXMLBCVRespuesta) clienteWs.convertirXmlAObjeto(xmlRespuesta,DemandasXMLBCVRespuesta.class);
				if(demandasXMLBCVRespuesta!=null&&demandasXMLBCVRespuesta.getDemandasXMLBCVList()!=null){
					_ordenes.append("undinv_nombre", java.sql.Types.VARCHAR);
					_ordenes.append("nombre_cliente", java.sql.Types.VARCHAR);
					_ordenes.append("rif_cliente", java.sql.Types.VARCHAR);
					_ordenes.append("estado", java.sql.Types.VARCHAR);
					_ordenes.append("id_oferta_bcv", java.sql.Types.VARCHAR);
					_ordenes.append("jornada", java.sql.Types.VARCHAR);
					_ordenes.append("ref_institucion", java.sql.Types.VARCHAR);
					_ordenes.append("tasa_cambio", java.sql.Types.VARCHAR);
					_ordenes.append("monto", java.sql.Types.VARCHAR);
					
					for (DemandaXMLBCV demanda : demandasXMLBCVRespuesta.getDemandasXMLBCVList()) {						
							cantidadOperaciones++;
							monto = monto.add(new BigInteger(demanda.getMonto()));
							_ordenes.addNew();
							_ordenes.setValue("undinv_nombre", nombreUI);
							_ordenes.setValue("nombre_cliente", demanda.getCliente());
							_ordenes.setValue("rif_cliente", demanda.getDemandante());
							_ordenes.setValue("estado", demanda.getEstado());
							_ordenes.setValue("id_oferta_bcv", demanda.getIdOfertaBCV());
							_ordenes.setValue("jornada", demanda.getNroJornada());
							_ordenes.setValue("ref_institucion", demanda.getRifInstitucion());
							_ordenes.setValue("tasa_cambio", demanda.getTasaCambio());
							_ordenes.setValue("monto", demanda.getMonto());
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
		
			}else if(tipoOperacion == ofertaAnulacion) { //OFERTA ALTO VALOR

				BigInteger monto = new BigInteger("0");
				Integer cantidadOperaciones = 0;
				String xmlRespuesta=null;
				OfertaBCVRespuesta ofertaBCVRespuesta;
				ClienteWs clienteWs = new ClienteWs();
				xmlRespuesta = stub.OFERTASXML(nroJornada); //NUMERO DE JORNADA DE LA UNIDAD DE INVERSION
				ofertaBCVRespuesta=(OfertaBCVRespuesta) clienteWs.convertirXmlAObjeto(xmlRespuesta,PactoBCVRespuesta.class);
				if(ofertaBCVRespuesta!=null&&ofertaBCVRespuesta.getOfertasBCVList()!=null){
					_ordenes.append("undinv_nombre", java.sql.Types.VARCHAR);
					_ordenes.append("nombre_cliente", java.sql.Types.VARCHAR);
					_ordenes.append("rif_cliente", java.sql.Types.VARCHAR);
					_ordenes.append("estado", java.sql.Types.VARCHAR);
					_ordenes.append("id_oferta_bcv", java.sql.Types.VARCHAR);
					_ordenes.append("jornada", java.sql.Types.VARCHAR);
					_ordenes.append("ref_institucion", java.sql.Types.VARCHAR);
					_ordenes.append("tasa_cambio", java.sql.Types.VARCHAR);
					_ordenes.append("monto", java.sql.Types.VARCHAR);
					
					for (OfertaBCV oferta : ofertaBCVRespuesta.getOfertasBCVList()) {						
							cantidadOperaciones++;
							monto = monto.add(new BigInteger(oferta.getMonto()));
							_ordenes.addNew();
							_ordenes.setValue("undinv_nombre", nombreUI);
							_ordenes.setValue("nombre_cliente", oferta.getCliente());
							_ordenes.setValue("rif_cliente", oferta.getOfertante());
							_ordenes.setValue("estado", oferta.getEstado());
							_ordenes.setValue("id_oferta_bcv", oferta.getCodigo());
							_ordenes.setValue("jornada", oferta.getJornada());
							_ordenes.setValue("ref_institucion", oferta.getRefInst());
							_ordenes.setValue("tasa_cambio", oferta.getTasa());
							_ordenes.setValue("monto", oferta.getMonto());
					}
					
					datosFilter.setValue("monto_operacion", monto.toString());
					datosFilter.setValue("cantidad_operaciones", cantidadOperaciones.toString());
					
					/*storeDataSet("datosFilter", datosFilter);
					storeDataSet("rows", _ordenes);	
					storeDataSet("datos", _ordenes);*/
					getSeccionPaginacion(cantidadOperaciones, getPageSize(), getNumeroDePagina());
				}else {
					Logger.error(this, "No se han encontrado operaciones para la jornada seleccionada. Jornada Buscada:  "+nroJornada);
				}
				storeDataSet("datosFilter", datosFilter);
				storeDataSet("rows", _ordenes);	
				storeDataSet("datos", _ordenes);			
			}		
		} catch (Exception e) {	//Cambio de codigo incluido en salida a Produccion WS Alto Valor NM26659 06/04/2016 			
			storeDataSet("datosFilter", new DataSet());
			storeDataSet("rows",  new DataSet());	
			storeDataSet("datos",  new DataSet());
			// TODO Auto-generated catch block
			Logger.error(this, "Han ocurrido un error en el proceso de Consulta Oferta - Demanda BCV  " + e.getMessage() + " En jornada: " + nroJornada==null||nroJornada.equals("")?" Numero de Jornada Vacio ":nroJornada );
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean isValid() throws Exception {
		boolean valido = true;
		
		try {
			tasaMinima     = Integer.parseInt(_record.getValue("tasa_minima")== null ? "0" : _record.getValue("tasa_minima"));
			tasaMaxima     = Integer.parseInt(_record.getValue("tasa_maxima")== null ? "0" : _record.getValue("tasa_maxima") );
			montoMinimo    = Integer.parseInt(_record.getValue("monto_minimo")== null ? "0" : _record.getValue("monto_minimo"));
			montoMaximo    = Integer.parseInt(_record.getValue("monto_maximo")== null ? "0" : _record.getValue("monto_maximo"));
			clienteID      = Integer.parseInt(_record.getValue("client_id")== null ? "0" : _record.getValue("client_id"));
			incluirCliente = Integer.parseInt(_record.getValue("incluir_cliente")== null ? "0" : _record.getValue("incluir_cliente"));
			origen         = _record.getValue("origen")== null ? "" : _record.getValue("origen");
			estatusCruce   = _record.getValue("estatus_cruce")== null ? "" : _record.getValue("estatus_cruce");
			tipoOperacion  = Integer.parseInt(_record.getValue("tipo_operacion")== null ? "0" : _record.getValue("tipo_operacion"));
			blotterID      = Integer.parseInt(_record.getValue("bloter_id")== null ? "0" : _record.getValue("bloter_id"));
			idUnidad       = Long.valueOf(_record.getValue("ui_id")==null?"0":_record.getValue("ui_id"));
		} catch (NumberFormatException e) {
			_record.addError(
					"Error: ",
					"Debe Introducir un número valido");
			return false;
		}
		
		
		if(idUnidad == 0){
			_record.addError(
					"Error: ",
					"Debe seleccionar una unidad de inversion ");
			valido = false;
		}else {
			unidadInversionDAO = new UnidadInversionDAO(_dso);		
			
			_unidadInversion = unidadInversionDAO.consultarUI(idUnidad);
			_unidadInversion.next();
			
			if(_unidadInversion.getValue("NRO_JORNADA") == null){
				_record.addError(
						"Error: ",
						"La unidad de inversión seleccionada no tiene número de jornada configurado. ");
				valido = false;
			}else {
				nombreUI = _unidadInversion.getValue("undinv_nombre") == null ? "" : _unidadInversion.getValue("undinv_nombre");
				nroJornada = _unidadInversion.getValue("NRO_JORNADA") == null ? "" : _unidadInversion.getValue("NRO_JORNADA");
			}
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
			
		return valido;
	}
}
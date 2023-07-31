package models.bcv.consulta_ofertas_demandas_bcv;

import java.net.URL;
import java.util.Hashtable;

import megasoft.DataSet;
import megasoft.Logger;
import models.exportable.ExportableOutputStream;

import org.apache.axis.transport.http.HTTPConstants;
import org.bcv.serviceAltoValor.BancoUniversalPortBindingStub;

import com.bdv.infi.config.Propiedades;
import com.bdv.infi.dao.CredencialesDAO;
import com.bdv.infi.dao.OfertaDAO;
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

/**
 * @author eel
 * 
 */
public class ExportarCVS extends ExportableOutputStream {

	public void execute() throws Exception {
		DataSet _ordenes = new DataSet();
		DataSet _credenciales = new DataSet();
		DataSet _unidadInversion = new DataSet(); 
		UnidadInversionDAO unidadInversionDAO;
		CredencialesDAO credencialesDAO;
		long idUnidad;
		String statusP = null;
		String fecha   = null;
		String tipoNegocio = ConstantesGenerales.TIPO_NEGOCIO_ALTO_VALOR;
		String tipoProductoId = ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL;
		String origen;
		String estatusCruce;
		idUnidad = Long.valueOf(_record.getValue("ui_id")==null?"0":_record.getValue("ui_id"));
		statusP      = _record.getValue("statusP")      == null? "": _record.getValue("statusP");
		estatusCruce = _record.getValue("estatus_cruce")== null? "": _record.getValue("estatus_cruce");
		fecha        = (String) _record.getValue("fecha");
		origen       = _record.getValue("origen")       == null? "": _record.getValue("origen");
		
		Integer tasaMinima       = Integer.parseInt(_req.getParameter("tasa_minima")         == null ? "0" : _req.getParameter("tasa_minima")); 
		Integer tasaMaxima       = Integer.parseInt(_req.getParameter("tasa_maxima")         == null ? "0" : _req.getParameter("tasa_maxima"));
		Integer montoMinimo      = Integer.parseInt(_req.getParameter("monto_minimo")        == null ? "0" : _req.getParameter("monto_minimo"));
		Integer montoMaximo      = Integer.parseInt(_req.getParameter("monto_maximo")        == null ? "0" : _req.getParameter("monto_maximo"));
		Integer clienteID        = Integer.parseInt(_req.getParameter("cliente_id")          == null ? "0" : _req.getParameter("cliente_id"));
		Integer incluirCliente   = Integer.parseInt(_req.getParameter("incluir_cliente")     == null ? "0" : _req.getParameter("incluir_cliente"));
		Integer tipoOperacion    = Integer.parseInt(_req.getParameter("tipo_operacion")      == null ? "0" : _req.getParameter("tipo_operacion"));
		Integer blotterID        = Integer.parseInt(_req.getParameter("bloter_id")          == null ? "0" : _req.getParameter("bloter_id"));
		Integer ofertaAnulacion  = Integer.parseInt(ConstantesGenerales.OFERTA_ANULACION);
		Integer demandaAnulacion = Integer.parseInt(ConstantesGenerales.DEMANDA_ANULACION);
		
		
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

		unidadInversionDAO = new UnidadInversionDAO(_dso);		
		String nroJornada, nombreUI;
		_unidadInversion = unidadInversionDAO.consultarUI(idUnidad);
		_unidadInversion.next();
		
		if(_unidadInversion.getValue("NRO_JORNADA") == null){
			Logger.error(this, "Ha ocurrido un error (NRO_JORNADA NULL) al momento de buscar el numero dejornada de la Unidad de Inversion: "+idUnidad+". No se puede continuar la operacion");
			throw new Exception();
		}else {
			nombreUI = _unidadInversion.getValue("undinv_nombre") == null ? "" : _unidadInversion.getValue("undinv_nombre");
			nroJornada = _unidadInversion.getValue("NRO_JORNADA") == null ? "" : _unidadInversion.getValue("NRO_JORNADA");
		}
		
		
		if(tipoOperacion == demandaAnulacion){ //DEMANDA
			registrarInicio("DemandasBCV.csv");
			crearCabecera("Unidad de Inversión;Nombre Cliente;C.I./Rif Cliente;Estado;ID de Demanda BCV; Numero de Orden INFI; Numero de Jornada; Tasa Cambio; Monto;");
			
			try {
				String xmlRespuesta=null;
				DemandasXMLBCVRespuesta demandasXMLBCVRespuesta;
				ClienteWs clienteWs = new ClienteWs();
				xmlRespuesta = stub.DEMANDASXML(nroJornada); //NUMERO DE JORNADA DE LA UNIDAD DE INVERSION 
				demandasXMLBCVRespuesta=(DemandasXMLBCVRespuesta) clienteWs.convertirXmlAObjeto(xmlRespuesta,DemandasXMLBCVRespuesta.class);
				escribir("\r\n");
				if(demandasXMLBCVRespuesta!=null&&demandasXMLBCVRespuesta.getDemandasXMLBCVList()!=null){
					for (DemandaXMLBCV demanda : demandasXMLBCVRespuesta.getDemandasXMLBCVList()) {						
						escribir(nombreUI==null?" ;":nombreUI+";");
						escribir(demanda.getCliente()==null?" ;":demanda.getCliente()+";");
						escribir(demanda.getDemandante()==null?" ;":demanda.getDemandante()+";");
						escribir(demanda.getEstado()==null?" ;":demanda.getEstado()+";");
						escribir(demanda.getIdOfertaBCV()==null?" ;":demanda.getIdOfertaBCV()+";");	
						escribir(demanda.getRifInstitucion()==null?" ;": demanda.getRifInstitucion()+";");
						escribir(demanda.getNroJornada()==null?" ;":demanda.getNroJornada()+";");
						escribir(demanda.getTasaCambio()==null?" ;":demanda.getTasaCambio()+";");
						escribir(demanda.getMonto()==null?" ;":demanda.getMonto()+";");
						escribir("\r\n");
					}
					registrarFin();
					obtenerSalida();
				}else {
					Logger.error(this, "No se han encontrado operaciones para la jornada seleccionada. Jornada Buscada:  "+nroJornada);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(tipoOperacion == ofertaAnulacion) { //OFERTA
			registrarInicio("OfertaBCV.csv");
			crearCabecera("Unidad de Inversión;Nombre Cliente;C.I./Rif Cliente;Estado;ID de Oferta BCV; Numero de Orden INFI; Numero de Jornada; Tasa Cambio; Monto;");
			
			try {
				String xmlRespuesta=null;
				OfertaBCVRespuesta ofertaBCVRespuesta;
				ClienteWs clienteWs = new ClienteWs();
				xmlRespuesta = stub.OFERTASXML(nroJornada); //NUMERO DE JORNADA DE LA UNIDAD DE INVERSION
				ofertaBCVRespuesta=(OfertaBCVRespuesta) clienteWs.convertirXmlAObjeto(xmlRespuesta,OfertaBCVRespuesta.class);
				escribir("\r\n");
				if(ofertaBCVRespuesta!=null&&ofertaBCVRespuesta.getOfertasBCVList()!=null){
					for (OfertaBCV oferta : ofertaBCVRespuesta.getOfertasBCVList()) {						
						escribir(nombreUI==null?" ;":nombreUI+";");
						escribir(oferta.getCliente()==null?" ;":oferta.getCliente()+";");
						escribir(oferta.getOfertante()==null?" ;":oferta.getOfertante()+";");
						escribir(oferta.getEstado()==null?" ;":oferta.getEstado()+";");
						escribir(oferta.getCodigo()==null?" ;":oferta.getCodigo()+";");	
						escribir(oferta.getRefInst()==null?" ;": oferta.getRefInst()+";");
						escribir(oferta.getJornada()==null?" ;":oferta.getJornada()+";");
						escribir(oferta.getTasa()==null?" ;":oferta.getTasa()+";");
						escribir(oferta.getMonto()==null?" ;":oferta.getMonto()+";");
						escribir("\r\n");
					}
					registrarFin();
					obtenerSalida();
				}else {
					Logger.error(this, "No se han encontrado operaciones para la jornada seleccionada. Jornada Buscada:  "+nroJornada);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
		}
	}// fin execute

	protected void crearCabecera(String cabecera) throws Exception {
		escribir(cabecera.toUpperCase());
	}
	

}// Fin Clase
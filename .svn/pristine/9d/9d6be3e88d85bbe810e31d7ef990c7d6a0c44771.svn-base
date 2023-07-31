package com.bdv.infi_services.business.ordenes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import javax.sql.DataSource;
import megasoft.db;
import models.msc_utilitys.MSCModelExtend;
import com.bancovenezuela.comun.data.OrdenOperacion;
//import com.bancovenezuela.comun.data.PrecioRecompra;
import com.bdv.infi_toma_orden.dao.TomaOrdenDAO;
import com.bdv.infi_toma_orden.data.OrdenTitulo;
import com.bdv.infi_toma_orden.data.TomaOrdenSimulada;
import com.bdv.infi_toma_orden.logic.TomaDeOrdenes;
import com.bdv.infi.dao.DocumentoDefinicionDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.data.DocumentoDefinicion;
import com.bdv.infi.data.OrdenDocumento;
import com.bdv.infi.util.Utilitario;
import com.bdv.infi_services.beans.entidades.MensajesServicio;
import com.bdv.infi_services.beans.entidades.ValorAtributo;
import com.bdv.infi_services.beans.entidades.mensajes_peticion.Credenciales;
//import com.bdv.infi_services.beans.entidades.mensajes_peticion.ListaTitulosRecompra;
import com.bdv.infi_services.beans.entidades.mensajes_peticion.ParametrosTomaDeOrden;
//import com.bdv.infi_services.beans.entidades.mensajes_peticion.TituloRecompra;
import com.bdv.infi_services.beans.entidades.mensajes_respuesta.Documento;
import com.bdv.infi_services.beans.entidades.mensajes_respuesta.DocumentosAsociados;
import com.bdv.infi_services.beans.entidades.mensajes_respuesta.TOrdenOperacion;
import com.bdv.infi_services.beans.entidades.mensajes_respuesta.TOrdenOperaciones;
import com.bdv.infi_services.beans.entidades.mensajes_respuesta.TOrdenTitulo;
import com.bdv.infi_services.beans.entidades.mensajes_respuesta.TOrdenTitulos;
import com.bdv.infi_services.beans.entidades.mensajes_respuesta.TomaDeOrden;
import com.bdv.infi_services.beans.entidades.mensajes_respuesta.TomaOrdenResultado;
import com.bdv.infi_services.utilities.DBOServices;
import com.megasoft.soa.webservices.commom.WSProperties;
/**
 * clase encargada de simular y registrar una orden mediante web services
 * @author elaucho,mgalindo
 */
public class ManejadorDeTomaDeOrden extends MSCModelExtend{
	/**
	 * DataSource de acceso a la base de datos
	 */
	private DataSource dso = null;
	/**
	 * Constante para eliminar los decimales
	 */
	private static final BigDecimal CIEN_BD = new BigDecimal (100);
	/**
	 * Constante con el Tipo de Transaccion a ser aplicada
	 */
	public static final String TRANSACCION_TOMA_ORDEN = "TOMA_ORDEN";
	/**
	 * Formato de Fecha
	 */
	private SimpleDateFormat sdWSDate = new SimpleDateFormat("ddMMyyyy");
	/**
	 * Nombre del DatSource a utilizar
	 */
	private	String dsName = "";
	/**
	 * Parametros de la Toma de Orden 
	 */
	protected HashMap<String, Object> parametrosEntrada = new HashMap<String, Object>();
	/**
	 * Bean con la informacion generada por el proceso de negocio de INFI
	 */
	private	TomaOrdenSimulada beanTOSimulada = null; 
	/**
	 * Bean con la TomaDeOrden Resultado 
	 */
	private TomaOrdenResultado beanTomaOrdenResultado = new TomaOrdenResultado();
	/** 
	 * Bean que encapsula informacion de la OrdenRespuesta
	 */
	private TomaDeOrden beanTomaDeOrden;
	/**
	 * Lista de Clases Titulos y Operaciones asociadas
	 */
	private TOrdenTitulos listaTitulos = new TOrdenTitulos();
	private	TOrdenOperaciones listaOperaciones = new TOrdenOperaciones();
	/**
	 * Bean con los mensajes de respuesta del servicio a nivel de negocio
	 */
	private MensajesServicio mensajesServicio = new MensajesServicio();
	/**
	 * Variables de trabajo
	 */
	private ValorAtributo beanValor;
	private	ArrayList<ValorAtributo> listaValores = new ArrayList<ValorAtributo>();
	private ArrayList<String> mensajes = new ArrayList<String>();
	/**
	 * Objeto TomaDeOrdenes
	 */
	TomaDeOrdenes boSTO;
	/**
	 *  Metodo que trasforma el bean de Parametros a un HashMap 
	 * @param entrada : bean ParametrosTomaOrden con la informacion base
	 * @return HashMap con los parametrosEntrada
	 * @throws Exception
	 */	
	public void armarParametros (ParametrosTomaDeOrden entrada) throws Throwable {
		
		boolean indicaFinanciada = false;
		double pFinanciado = new Double(entrada.getPorcentajeFinanciado()).doubleValue();
		if (pFinanciado > 0)
			indicaFinanciada = true;
		
		Credenciales credenciales = entrada.getCredencialesDeUsuario();
		ArrayList<String > arreglo = new ArrayList<String>();
		parametrosEntrada.put("idUnidadInversion", new Long(entrada.getIdUnidadInversion()));
		parametrosEntrada.put("tipoPersona",  entrada.getTipoPersona());
		parametrosEntrada.put("cedulaCliente", entrada.getCedulaCliente());	
		parametrosEntrada.put("montoInversion", new BigDecimal(entrada.getMontoInversion()).divide(CIEN_BD));
		parametrosEntrada.put("cantidadComprar", new Long(entrada.getCantidadComprar()));
		parametrosEntrada.put("precioOfrecido", new BigDecimal(entrada.getPrecioOfrecido()));
		parametrosEntrada.put("numeroCuentaCliente", entrada.getNumeroCuentaCliente());
		parametrosEntrada.put("porcentajeFinanciado", new BigDecimal(entrada.getPorcentajeFinanciado()).divide(CIEN_BD));		
		parametrosEntrada.put("ipTerminal", credenciales.getIpTerminal());
		parametrosEntrada.put("usuario", credenciales.getUsuarioCanal());
		parametrosEntrada.put("IndicadorFinanciada", new Boolean(indicaFinanciada));
		parametrosEntrada.put("tipoTransaccionNegocio", TRANSACCION_TOMA_ORDEN);
		parametrosEntrada.put("camposDinamicos", (ArrayList)arreglo);
		parametrosEntrada.put("servletContext", this._app);
		parametrosEntrada.put("estadoCasado", entrada.getEstadoCivilCasado());
		parametrosEntrada.put("cedulaConyuge",entrada.getCedulaConyuge()!=null?entrada.getCedulaConyuge().substring(1, entrada.getCedulaConyuge().length()):null);
		parametrosEntrada.put("tipoPersonaConyuge",entrada.getCedulaConyuge()!=null?entrada.getCedulaConyuge().substring(0,1):null);
		parametrosEntrada.put("userName",entrada.getCredencialesDeUsuario().getUsuarioCanal());
		ArrayList precioRecompraArrayList = new ArrayList();
		parametrosEntrada.put("recompraTitulos", precioRecompraArrayList);
		
	//Busca el bloter asociado a RED para la unidad de inversion seleccionada
		try {
			 String 	dsName 	= WSProperties.getProperty("datasource-infi");
			 javax.sql.DataSource _dso 	= db.getDataSource(dsName);
			 UnidadInversionDAO unidadInversion = new UnidadInversionDAO(_dso);
			
			 parametrosEntrada.put("idBlotter",unidadInversion.listarBloterRed(entrada));
		} catch (Throwable e) {
			throw e;
		}
		
		/*
		 * Se verifica que existan titulos para pactar la recompra y se setea al hashmap en caso de haberlos
		 */
		/**
		if(entrada.getListaTitulosRecompra()!=null && !entrada.getListaTitulosRecompra().equals("")){
			ArrayList<PrecioRecompra> precioRecompraArrayList = new ArrayList<PrecioRecompra>();

			ListaTitulosRecompra listaTitulosRecompra = entrada.getListaTitulosRecompra();

			ArrayList<TituloRecompra> tituloRecompraArrayList = listaTitulosRecompra.getListaTitulosRecompra();

			TituloRecompra tituloRecompra = new TituloRecompra();
			PrecioRecompra precioRecompra = new PrecioRecompra();
			for(int i=0;i<tituloRecompraArrayList.size();i++){
				
				tituloRecompra = new TituloRecompra();
				tituloRecompra = tituloRecompraArrayList.get(i);
				precioRecompra = new PrecioRecompra();
				precioRecompra.setTituloId(tituloRecompra.getIdTitulo());
				precioRecompra.setTitulo_precio_recompra(new BigDecimal(tituloRecompra.getPorcentajeRecompra()));
				precioRecompra.setTasaPool(null);
				
				precioRecompraArrayList.add(precioRecompra);
			}

			parametrosEntrada.put("recompraTitulos", precioRecompraArrayList);
			
		}**/
		
//		 1.-	Buscar el DataSource
		try {
			dsName = WSProperties.getProperty("datasource-infi");
			dso = DBOServices.getDataSource(dsName);		
			
		}catch (Throwable e) {
			throw e;
		}
		/*
		 * Set de parametros de entrada al Objeto TomaDeOrdenes
		 */
		boSTO = new TomaDeOrdenes(dso);
		boSTO.setParametrosEntrada(parametrosEntrada);
	}//fin de parametros de entrada
	
	
	/**
	 *  Metodo que invoca al proceso que Simula una Toma de Orden
	 * @return bean TomaDeOrden con los resutados del proceso
	 * @throws Throwable
	 */	
	@SuppressWarnings("unchecked")
	public TomaOrdenResultado simuladorTO() throws Throwable	{
		
		
		
		// 1.-	Validar la aplicacion de la transaccion
		
		try {
			mensajes = boSTO.validar();
		} catch (Exception e) {
			throw e;
		}

		//	Salida por problemas de la informacion de la transaccion
		if (mensajes.size() != 0) {
			for (int i=0; i<mensajes.size(); i++) {
				beanValor = new ValorAtributo((String) mensajes.get(i));
				throw new Throwable((String) mensajes.get(i));
				//listaValores.add(beanValor);
			}
			mensajesServicio.setValorAtributo(listaValores);
			beanTomaOrdenResultado.setMensajesServicio(mensajesServicio);
			return beanTomaOrdenResultado;
		}
		// 2.-	Simulador de Toma de Orden	
		try {
			beanTOSimulada = boSTO.simuladorTO();
		} catch (Throwable e) {
			throw e;
		}
		String var = "";
		//	3.- Armar los beans de respuesta
		try {
			//	a.-	armar toma de orden
			BigDecimal montoRemanente = (BigDecimal) parametrosEntrada.get("montoInversion");
			montoRemanente = montoRemanente.subtract((BigDecimal) parametrosEntrada.get("montoPedido"));
			Object [] objAux = new Object[9];
			objAux[0] = String.valueOf(beanTOSimulada.getIdOrden());
			objAux[1] = sdWSDate.format(beanTOSimulada.getFechaOrden());
			var = beanTOSimulada.getMontoInversion().multiply(CIEN_BD).setScale(2,BigDecimal.ROUND_HALF_EVEN).toString();
			objAux[2] = var.replace(".","");
			objAux[3] = String.valueOf(beanTOSimulada.getCantidadOrdenada());
			var = montoRemanente.multiply(CIEN_BD).setScale(2,BigDecimal.ROUND_HALF_EVEN).toString();
			objAux[4] = var.replace(".","");
	
			//Se verifica que el monto total comisiones tenga 15 digitos 11 enteros y 4 decimales
			objAux[5] = Utilitario.formatoDecimalesWS(beanTOSimulada.getMontoComisiones()!=null?beanTOSimulada.getMontoComisiones().setScale(4,BigDecimal.ROUND_HALF_EVEN):new BigDecimal(0), 11, 4);

			//Se verifica que el monto intereses caidos tenga 15 digitos 11 enteros y 4 decimales
			objAux[6] = Utilitario.formatoDecimalesWS(beanTOSimulada.getMontoInteresCaidos()!=null?beanTOSimulada.getMontoInteresCaidos().setScale(4,BigDecimal.ROUND_HALF_EVEN):new BigDecimal(0), 11, 4);

			//Se verifica que el monto total tenga 15 digitos 11 enteros y 4 decimales
			objAux[7] = Utilitario.formatoDecimalesWS(beanTOSimulada.getMontoTotal()!=null?beanTOSimulada.getMontoTotal().setScale(4,BigDecimal.ROUND_HALF_EVEN):new BigDecimal(0), 11, 4);
			
			//Se verifica que la Tasa de Cambio tenga 15 digitos 09 enteros y 06 decimales
			objAux[8] = Utilitario.formatoDecimalesWS(beanTOSimulada.getTasaCambio()!=null?beanTOSimulada.getTasaCambio().setScale(6,BigDecimal.ROUND_HALF_EVEN):new BigDecimal(0),9,6);

			beanTomaDeOrden = new TomaDeOrden(objAux);
			beanTomaOrdenResultado.setTomaDeOrden(beanTomaDeOrden);
			
			/*
			 * b.-	armar titulos de respuesta
			 */
			
			ArrayList<TOrdenTitulo>listaResultadoTitulos = new ArrayList<TOrdenTitulo>();
			
			TOrdenTitulo beanTOT; OrdenTitulo beanOT;
			/*
			 * Objeto auxiliar para Orden Titulo
			 */
			objAux = new Object[9];
			ArrayList listaBase = beanTOSimulada.getListaOrdenTitulo();
			for (int i=0; i<listaBase.size(); i++) {
				beanOT = (OrdenTitulo)listaBase.get(i);
				objAux[0] = String.valueOf(beanOT.getIdOrden());
				objAux[1] = beanOT.getIdTitulo();
				objAux[2] = beanOT.getDescrTitulo().trim();
				var = beanOT.getValorNominal().toString();
				objAux[3] = var.replace(".","");

				//Se verifica que el porcentaje tenga 09 digitos 3 enteros y 6 decimales
				objAux[4] = Utilitario.formatoDecimalesWS( beanOT.getPorcentaje()!=null?beanOT.getPorcentaje().setScale(6,BigDecimal.ROUND_HALF_EVEN):new BigDecimal(0),3,6);

				objAux[5] = beanOT.getUnidadesInvertidas().toString();	
				var = beanOT.getValorInvertido().setScale(2).toString();
				objAux[6] = var.replace(".","");
				objAux[7] = beanOT.getSiglasMoneda();
				
				//Se verifica que el porcentaje Recompra tenga 9 digitos 3 enteros y 6 decimales
				objAux[8] = Utilitario.formatoDecimalesWS( beanOT.getPorcentajeRecompra()!=null?beanOT.getPorcentajeRecompra().setScale(6,BigDecimal.ROUND_HALF_EVEN):new BigDecimal(0),3,6);

				beanTOT = new TOrdenTitulo(objAux);
				listaResultadoTitulos.add(beanTOT);
			}
			listaTitulos.setTOrdenTitulo(listaResultadoTitulos);
			beanTomaOrdenResultado.setTOrdenTitulos(listaTitulos);
			
			/*
			 * c.-	armar operaciones de respuesta
			 */
			ArrayList<TOrdenOperacion> listaResultadoOperaciones = new ArrayList<TOrdenOperacion>();
			TOrdenOperacion beanTOO; OrdenOperacion beanOO;
			
			/*
			 * Objeto Auxiliar para Orden Operacion
			 */
			objAux    = new Object[6];
			listaBase = beanTOSimulada.getListaOperaciones();
		for (int i=0; i<listaBase.size(); i++) {
				beanOO = (OrdenOperacion)listaBase.get(i);
				objAux[0] = beanOO.getIdTransaccionFinanciera();
				
				//Se verifica que el monto de la operaciontenga 15 digitos 11 enteros y 4 decimales
				objAux[1] = Utilitario.formatoDecimalesWS(beanOO.getMontoOperacion()!=null?beanOO.getMontoOperacion().setScale(4,BigDecimal.ROUND_HALF_EVEN):new BigDecimal(0), 11, 4);

				//Se verifica que tasa tenga 09 digitos 03 enteros y 06 decimales
				objAux[2] = Utilitario.formatoDecimalesWS( beanOO.getTasa()!=null?beanOO.getTasa().setScale(6,BigDecimal.ROUND_HALF_EVEN):new BigDecimal(0),3,6);
				objAux[3] = beanOO.getDescripcionTransaccion();
				objAux[4] = beanOO.getTipoTransaccionFinanc();
				objAux[5] = beanOO.getInComision()!=0?beanOO.getInComision():"";
				beanTOO = new TOrdenOperacion(objAux);
				
				listaResultadoOperaciones.add(beanTOO);
			}
			listaOperaciones.setTOrdenOperacion(listaResultadoOperaciones);
			beanTomaOrdenResultado.setTOrdenOperaciones(listaOperaciones);
		} catch (Throwable e) {
			throw e;
		}
		return beanTomaOrdenResultado;
	}

	/**
	 *  Metodo que invoca al proceso que Registro una Toma de Orden
	 * @return bean TomaDeOrden con los resutados del proceso
	 * @throws Throwable
	 */	
	public TomaOrdenResultado insertTO() throws Throwable	{
		
		/*
		 * Se verifica la existencia del cliente en INFI de no existir lo busca en ALTAIR y lo guarda en INFI
		 */
		try {
			boSTO.verificarExistenciaCliente();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		

		//	1.-	Invocar el proceso de simular para obtener los resultados
		
		this.simuladorTO();
		if (mensajesServicio.getValorAtributo().size() > 0){
			return beanTomaOrdenResultado;
		} 

		// 2.-	Registrar resultados de la simulacion
		try {
			TomaOrdenDAO boTO = new TomaOrdenDAO(null,dso,this._app,parametrosEntrada.get("ipTerminal").toString());
			boTO.insertar(beanTOSimulada);
			beanTomaDeOrden.setIdOrden(String.valueOf(beanTOSimulada.getIdOrden()));
			
			//Si existen documentos asociados se anexan al bean de respuesta
			DocumentosAsociados documentosAsociados = new DocumentosAsociados();
			DocumentoDefinicion documentoDefinicion = new DocumentoDefinicion();
			Documento documento = new Documento();
			ArrayList<Documento> documentoArrayList = new ArrayList<Documento>();
			
			//Se recuperan los documentos asociados a la orden-
			ArrayList <OrdenDocumento> ordenDocumentoArrayList = beanTOSimulada.getListaDocumentos();
			OrdenDAO ordenDAO = new OrdenDAO(dso);
			for(int i=0;i<ordenDocumentoArrayList.size();i++){
				OrdenDocumento ordenDocumento = ordenDocumentoArrayList.get(i);
				
				//Con el id del documento buscamos el nombre y el contenido
				documentoDefinicion = ordenDAO.listarDocumento(String.valueOf(ordenDocumento.getIdDocumento()));
				
				//Tomamos el contenido que esta en un array de bytes y lo codificamos base 64.
				sun.misc.BASE64Encoder base64Encoder =  new sun.misc.BASE64Encoder();
				
				documento = new Documento();
				documento.setContenido(base64Encoder.encode(documentoDefinicion.getContenido()));
				documento.setNombreDocumento(ordenDocumento.getNombre());
				documento.setOrdenDocumentoId(String.valueOf(ordenDocumento.getIdDocumento()));
				
				/*
				 * Se agregan los documentos al ArrayList
				 */
				documentoArrayList.add(documento);
			}

			//Al objeto documentos asociados se le setea el arrayList de documento
			documentosAsociados.setDocumento(documentoArrayList);
			
			//El objeto que contiene el arrayList de los documentos asociados a la orden es seteado al Bean de respuesta
			beanTomaDeOrden.setDocumentosAsociados(documentosAsociados);
			
			beanTomaOrdenResultado.setTomaDeOrden(beanTomaDeOrden);
		} catch (Throwable e) {
			e.printStackTrace();
			throw e;
		}
		return beanTomaOrdenResultado;
	}
}

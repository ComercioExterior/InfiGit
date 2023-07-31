package com.bdv.infi_services.business.unidad_inversion;

import java.math.BigDecimal;
import java.util.ArrayList;
import com.bdv.infi.dao.CamposDinamicos;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.dao.UsuarioDAO;
import com.bdv.infi.util.Utilitario;
import com.bdv.infi_services.beans.entidades.mensajes_peticion.Credenciales;
import com.bdv.infi_services.beans.entidades.mensajes_peticion.ParametrosConsultaUI;
import com.bdv.infi_services.beans.entidades.mensajes_respuesta.Persona;
import com.bdv.infi_services.beans.entidades.mensajes_respuesta.TipoPersonasValidas;
import com.bdv.infi_services.beans.entidades.mensajes_respuesta.Titulo;
import com.bdv.infi_services.beans.entidades.mensajes_respuesta.TitulosAsociados;
import com.bdv.infi_services.beans.entidades.mensajes_respuesta.UnidadInversion;
import com.bdv.infi_services.beans.entidades.mensajes_respuesta.UnidadesInversion;
import com.bdv.infi_services.business.AbstractConsultaPaginada;
import com.bdv.infi_services.utilities.DBOServices;
import com.megasoft.soa.webservices.commom.WSProperties;


/**
 * Clase que permite encapsular la funcionalidad de las Consultas sobre las Unidades de Inversion
 * @author elaucho,mgalindo
 */
public class ManejadorUnidadInversion extends AbstractConsultaPaginada {


	/**
	 * Constructor del bean
	 */
	public ManejadorUnidadInversion () throws Exception {
		super();
	}
	
	//Recuperamos el datasource para acceder a base de datos
	String 	dsName 						  = WSProperties.getProperty("datasource-infi");
	public javax.sql.DataSource _dso 	  = DBOServices.getDataSource(dsName);
	/**
	 * Metodo que permite recuperar unidades de inversion en base a los criterios especificados
	 * @param beanCriterios : bean ParametrosConsultaUI con los criterios de busqueda
	 * @return UnidadesInversion con los resultados de la consulta: Lista de Unidades de Inversion recuperadas o Mensajes de Excepcion
	 * @throws Throwable
	 */
	public UnidadesInversion getUnidadInversion(ParametrosConsultaUI beanCriterios) throws Throwable	{
		
		//	parametros de paginacion
		setBeanPaginacion(beanCriterios.getParametrosPaginacion());
		
		UnidadesInversion listaUnidadesInversion = new UnidadesInversion();
		UnidadInversion beanUnidadInversion;
		ArrayList<UnidadInversion> lista = new ArrayList<UnidadInversion>();
		/*
		 * Se verifican las Credenciales de Usuario
		 */
		if(beanCriterios.getCredenciales()==null || beanCriterios.getCredenciales().equals(""))
			throw new Throwable("Debe enviar las Credenciales de usuario para realizar la consulta");
		/*
		 * Buscamos los bloter asociados al usuario
		 */
		
		Credenciales credenciales = beanCriterios.getCredenciales();
		UsuarioDAO usuarioDAO = new UsuarioDAO(_dso);
		String bloterAsociado = "";
		
		try {
			
			usuarioDAO.listarBlotterUsuarioWS(credenciales.getUsuarioCanal());
			/*
			 * Se verifica si el usuario recibido por parametro existe y si posee un bloter Asociado
			 */
			if(usuarioDAO.getDataSet().count()>0){
				usuarioDAO.getDataSet().first();
				usuarioDAO.getDataSet().next();
				bloterAsociado = usuarioDAO.getDataSet().getValue("bloter_id");
			}//fin IF usuarioDAO.getDataSet().count()>0
		} catch (Throwable e) {
			throw e;
		}
		
		/*
		 * Con el bloter que esta asociado al usuario, se buscan las unidades de inversion que pueden ser visualizadas
		 */
		
		UnidadInversionDAO unidadInversionDAO = new UnidadInversionDAO(_dso);
		unidadInversionDAO.listarUnidadesBloterWS(bloterAsociado, beanCriterios.getCriterioStatus(),beanCriterios.getCriterioMoneda());
		
		try {
		
			Object [] objAux = new Object[31];
			Long montoAux = new Long(0);
			if(unidadInversionDAO.getDataSet().count()>0){
				unidadInversionDAO.getDataSet().first();
			while (unidadInversionDAO.getDataSet().next()) {
				String unidadInversion = unidadInversionDAO.getDataSet().getValue("undinv_id");
				objAux [0] = unidadInversionDAO.getDataSet().getValue("undinv_id");
				objAux [1] = unidadInversionDAO.getDataSet().getValue("undinv_multiplos");
				objAux [2] = unidadInversionDAO.getDataSet().getValue("bloter_id");
				objAux [3] = unidadInversionDAO.getDataSet().getValue("undinv_nombre");
				objAux [4] = unidadInversionDAO.getDataSet().getValue("undinv_descripcion");
				objAux [5] = unidadInversionDAO.getDataSet().getValue("insfin_descripcion");								
				objAux [6] = unidadInversionDAO.getDataSet().getValue("undinv_emision");
				objAux [7] = unidadInversionDAO.getDataSet().getValue("undinv_serie");
				BigDecimal tasaCambio = new BigDecimal(unidadInversionDAO.getDataSet().getValue("undinv_tasa_cambio")).setScale(5,BigDecimal.ROUND_HALF_EVEN);	
				objAux [8] = Utilitario.formatoDecimalesWS(tasaCambio, 10, 5);
				objAux [9]  = unidadInversionDAO.getDataSet().getValue("undinv_fe_emision");
				objAux [10] = unidadInversionDAO.getDataSet().getValue("undinv_in_vta_empleados");
				if (unidadInversionDAO.getDataSet().getValue("tppeva_id")!= null && !unidadInversionDAO.getDataSet().getValue("tppeva_id").equals("null"))
					objAux [11] = unidadInversionDAO.getDataSet().getValue("tppeva_id"); 
				try {
					montoAux = new BigDecimal(unidadInversionDAO.getDataSet().getValue("undinv_precio_minimo")).multiply(CIEN_BD).longValue(); 
					
				} catch (Exception e) {
					montoAux = BigDecimal.ZERO.longValue();
				}
				objAux [12] = montoAux.toString(); 	
				objAux [13] = unidadInversionDAO.getDataSet().getValue("undinv_fe_adjudicacion");
				objAux [14] = unidadInversionDAO.getDataSet().getValue("undinv_fe_liquidacion");				
				objAux [15] = unidadInversionDAO.getDataSet().getValue("undinv_in_recompra_neteo");
				objAux [16] = unidadInversionDAO.getDataSet().getValue("moneda_descripcion");
				objAux [18] = unidadInversionDAO.getDataSet().getValue("undinv_umi_inv_total");
				objAux [19] = unidadInversionDAO.getDataSet().getValue("undinv_umi_inv_mto_min");
				objAux [20] = unidadInversionDAO.getDataSet().getValue("undinv_umi_inv_mto_max");
				objAux [21] = unidadInversionDAO.getDataSet().getValue("undinv_umi_inv_disponible"); 
				objAux [22] = unidadInversionDAO.getDataSet().getValue("undinv_status");
				objAux [23] = unidadInversionDAO.getDataSet().getValue("undinv_status_descripcion");
				/*
				 * Se busca los intervalos de fecha y hora del bloter Correspondiente al Canal
				 */
				UnidadInversionDAO unidadInversionDAO1 = new UnidadInversionDAO(_dso);
				unidadInversionDAO1.listarBloterPorUiWS(Long.parseLong(unidadInversion));
				
				if(unidadInversionDAO1.getDataSet().count()>0){
					unidadInversionDAO1.getDataSet().first();
					unidadInversionDAO1.getDataSet().next();
					objAux [24] = unidadInversionDAO1.getDataSet().getValue("fecha_desde_toma_orden");
					objAux [25] = unidadInversionDAO1.getDataSet().getValue("hora_desde_toma_orden");
					objAux [26] = unidadInversionDAO1.getDataSet().getValue("fecha_hasta_toma_orden");
					objAux [27] = unidadInversionDAO1.getDataSet().getValue("hora_hasta_toma_orden");
					objAux [28] = unidadInversionDAO1.getDataSet().getValue("hora_desde_ultimo_dia");
					objAux [29] = unidadInversionDAO1.getDataSet().getValue("hora_hasta_ultimo_dia");
				}
				objAux [30] = unidadInversionDAO.getDataSet().getValue("INSFIN_FORMA_ORDEN");
				beanUnidadInversion = new UnidadInversion(objAux);
				
			/*
			 * buscamos los titulos asociados a la unidad de inversion
			 */
			UnidadInversionDAO titulos = new UnidadInversionDAO(_dso);
			titulos.listartitulosPorUiWS(Long.parseLong(beanUnidadInversion.getIdUnidadInversion()));
				
			if(titulos.getDataSet().count()>0){
				titulos.getDataSet().first();
				/*
				 * Creacion de objetos
				 */
				Titulo titulo = new Titulo();
				ArrayList<Titulo> tituloArr = new ArrayList<Titulo>();
				while(titulos.getDataSet().next()){
					titulo.setFechaEmision(titulos.getDataSet().getValue("titulo_fe_emision"));
					titulo.setFechaVencimiento(titulos.getDataSet().getValue("titulo_fe_vencimiento"));
					titulo.setMonedaDenominacion(titulos.getDataSet().getValue("titulo_moneda_den"));
					titulo.setTituloId(titulos.getDataSet().getValue("titulo_id"));
					/*
					 * Se valida que el porcentaje del titulo este en formato de 3 enteros y 7 decimales
					 */
						BigDecimal porcentaje = titulos.getDataSet().getValue("uititu_porcentaje")!=null && !titulos.getDataSet().getValue("uititu_porcentaje").equals("")?new BigDecimal(titulos.getDataSet().getValue("uititu_porcentaje")).setScale(6,BigDecimal.ROUND_HALF_EVEN):new BigDecimal(0);
						String numeroRellenado = Utilitario.formatoDecimalesWS(porcentaje!=null?porcentaje:new BigDecimal(0),3,6);
						titulo.setPorcentajeDistribucion(numeroRellenado);

					/*
					 * Cada titulo es guardado en un Arraylist
					 */
					tituloArr.add(titulo);
					titulo = new Titulo();
				}//fin while
				
				//Seteamos cada titulo al objeto TitulosAsociados
				TitulosAsociados titulosAsociados = new TitulosAsociados();
				if(tituloArr!=null)
					titulosAsociados.setTitulosAsociados(tituloArr);
				
				
				//Agregamos el array al objeto beanUnidadInversion de los titulos asociados
				if(titulosAsociados!=null)			 
				beanUnidadInversion.setTitulosAsociados(titulosAsociados);
				
			}//fin busqueda de titulos para la unidad de inversion
			
			/*
			 * En este punto consultamos por unidad de Inversion y bloter los tipos de personas de la tabla de rangos a nivel de bloter
			 */
				com.bdv.infi.dao.UnidadInversionDAO unidadDao = new com.bdv.infi.dao.UnidadInversionDAO (_dso);
				unidadDao.listarRangosTiposDePersonaWS(Long.parseLong(beanUnidadInversion.getIdUnidadInversion()), beanUnidadInversion.getBloterId());
				
				//Recorremos el dataset para setearlo al objeto Persona
				
				if(unidadDao.getDataSet().count()>0){
					unidadDao.getDataSet().first();
					Persona persona = new Persona();
					ArrayList<Persona> personaArray = new ArrayList<Persona>();
					while(unidadDao.getDataSet().next()){
						persona = new Persona();
						persona.setCantidadMaximaInversion(unidadDao.getDataSet().getValue("UIBLOT_UMI_UM_CANT_MAX"));
						persona.setCantidadMinimaInversion(unidadDao.getDataSet().getValue("UIBLOT_UMI_UM_CANT_MIN"));
						persona.setMontoMinimoInversion(unidadDao.getDataSet().getValue("UIBLOT_UMI_INV_MTO_MIN"));
						persona.setMontoMaximoInversion(unidadDao.getDataSet().getValue("UIBLOT_UMI_INV_MTO_MAX"));
						/*
						 * porcentaje Max de Financiamiento number 5,2->>> 3 enteros 2 decimales
						 */
						if(unidadDao.getDataSet().getValue("UIBLOT_PCT_MAX_FINAN")!=null)
						{
							BigDecimal porcentajemaximoFinanciamiento = new BigDecimal(unidadDao.getDataSet().getValue("UIBLOT_PCT_MAX_FINAN")).setScale(2,BigDecimal.ROUND_HALF_EVEN);
							String numeroRellenado = Utilitario.formatoDecimalesWS(porcentajemaximoFinanciamiento!=null?porcentajemaximoFinanciamiento:new BigDecimal(0),3,2);
							persona.setPctMaximoFinanciamiento(numeroRellenado);
						}
						/*
						 * Precio Max y Min number 5,2->>> 3 enteros 2 decimales
						 */
						if(unidadDao.getDataSet().getValue("UIBLOT_PRECIO_MAXIMO")!=null)
						{
							BigDecimal precioMaximo = new BigDecimal(unidadDao.getDataSet().getValue("UIBLOT_PRECIO_MAXIMO")).setScale(2,BigDecimal.ROUND_HALF_EVEN);
							String numeroRellenado = Utilitario.formatoDecimalesWS(precioMaximo!=null?precioMaximo:new BigDecimal(0),3,2);
							persona.setPrecioMaximo(numeroRellenado);
						}
						if(unidadDao.getDataSet().getValue("UIBLOT_PRECIO_MINIMO")!=null)
						{
							BigDecimal precioMinimo = new BigDecimal(unidadDao.getDataSet().getValue("UIBLOT_PRECIO_MINIMO")).setScale(2,BigDecimal.ROUND_HALF_EVEN);
							String numeroRellenado = Utilitario.formatoDecimalesWS(precioMinimo!=null?precioMinimo:new BigDecimal(0),3,2);
							persona.setPrecioMinimo(numeroRellenado);
						}
						persona.setTipoPersona(unidadDao.getDataSet().getValue("TIPPER_ID"));
						
						//Agregamos el tipo de persona al array
						personaArray.add(persona);
					}//fin while
					
					//seteamos las personas al objeto tipoPersonasValidas
					TipoPersonasValidas tipoPersonasValidas = new TipoPersonasValidas();
					if(personaArray!=null)
					tipoPersonasValidas.setTipoPersonasValidas(personaArray);
					
					
					//Agregamos el array al objeto beanUnidadInversion de los campos dinamicos
					if(tipoPersonasValidas!=null)			 
					beanUnidadInversion.setTipoPersonasValidas(tipoPersonasValidas);
				}//fin if
				
				
				//Ahora buscamos los campos dinamicos para mostrarlos como respuesta en el XML
				CamposDinamicos camposDinamicos = new CamposDinamicos(_dso);
				camposDinamicos.listarCamposDinamicosUnidadInversion(Long.parseLong(beanUnidadInversion.getIdUnidadInversion()), 0);
				
				
				if(camposDinamicos.getDataSet().count()>0){
					camposDinamicos.getDataSet().first();
					com.bdv.infi_services.beans.entidades.mensajes_respuesta.CampoDinamico objetoCampoDinamico;
					ArrayList<com.bdv.infi_services.beans.entidades.mensajes_respuesta.CampoDinamico> objetoDinamicoArray = new ArrayList<com.bdv.infi_services.beans.entidades.mensajes_respuesta.CampoDinamico>();
					while(camposDinamicos.getDataSet().next()){
						objetoCampoDinamico = new com.bdv.infi_services.beans.entidades.mensajes_respuesta.CampoDinamico();
						objetoCampoDinamico.setDescrCampo(camposDinamicos.getDataSet().getValue("campo_nombre"));
						objetoCampoDinamico.setIdCampo(camposDinamicos.getDataSet().getValue("campo_id"));
						
						//Agregamos el campo dinamico al array
						objetoDinamicoArray.add(objetoCampoDinamico);
					}//fin while
					
					
					//Le agregamos a la clase de campos dinamicos el array de campo dinamico
					com.bdv.infi_services.beans.entidades.mensajes_respuesta.CamposDinamicos camposDinamicosObject =
						new com.bdv.infi_services.beans.entidades.mensajes_respuesta.CamposDinamicos();
					if(objetoDinamicoArray!=null)						
					camposDinamicosObject.setCamposDinamicos(objetoDinamicoArray);
					
					//Agregamos el array al objeto beanUnidadInversion de los campos dinamicos
					if(camposDinamicosObject!=null)				
					beanUnidadInversion.setCamposDinamicos(camposDinamicosObject);
				}//fin if
				
				//Le pasamos al array lista los objetos beanUnidadInversion	
				lista.add(beanUnidadInversion);
				
				
			}//fin WHILE
		}
		} catch (Throwable e) {
			throw e;
		}
		listaUnidadesInversion.setParametrosPaginacion(getBeanPaginacion());
		listaUnidadesInversion.setUnidadInversion(lista);
		return listaUnidadesInversion;
	}
}
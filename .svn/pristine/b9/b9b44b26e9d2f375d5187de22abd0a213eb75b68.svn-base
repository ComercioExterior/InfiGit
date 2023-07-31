package com.bdv.infi_services.business.custodia;

import java.math.BigDecimal;
import java.util.ArrayList;
import com.bdv.infi.dao.MonedaDAO;
import com.bdv.infi.dao.TitulosDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.util.Utilitario;
import com.bdv.infi.webservices.manager.ManejadorTasaCambio;
import com.bdv.infi_services.beans.entidades.mensajes_peticion.ParametrosConsultaTitulosCustodia;
import com.bdv.infi_services.beans.entidades.mensajes_respuesta.TituloCustodia;
import com.bdv.infi_services.beans.entidades.mensajes_respuesta.TitulosCustodia;
import com.bdv.infi_services.business.AbstractConsultaPaginada;
import com.bdv.infi_services.utilities.DBOServices;
import com.megasoft.soa.webservices.commom.WSProperties;
/**
 * Clase que permite encapsular la funcionalidad de las Consultas sobre los Titulos en Custodia
 * @author elaucho,mgalindo
 */
public class ManejadorCustodia extends AbstractConsultaPaginada {
	
	
	String 	dsName 						  = WSProperties.getProperty("datasource-infi");
	public javax.sql.DataSource dso 	  = DBOServices.getDataSource(dsName);
	/**
	 * Constructor de la Clase
	 */
	public ManejadorCustodia () throws Exception {
		super();
	}
	 
	/**
	 * Metodo que permite recuperar los titulos en custodia de un cliente
	 * @param beanCriterios : bean ParametrosConsultaTitulosCustodia con los criterios de busqueda
	 * @return TitulosCustodia con los resultados de la consulta: Lista de Titulos recuperados o Mensajes de Excepcion
	 * @throws Throwable
	 */
	public TitulosCustodia getTitulosCustodia (ParametrosConsultaTitulosCustodia beanCriterios) throws Throwable {
		
		//	parametros de paginacion
		setBeanPaginacion(beanCriterios.getParametrosPaginacion());
		TitulosCustodia listaTitulosCustodia = new TitulosCustodia();
		TituloCustodia beanTituloCustodia;
		ArrayList<TituloCustodia> lista = new ArrayList<TituloCustodia>();
		
		try {
			sqlTablasCriterios.append("from INFI_TB_201_CTES clie ");
			sqlTablasCriterios.append("inner join INFI_TB_701_TITULOS cus on cus.client_id = clie.client_id ");
			sqlTablasCriterios.append("inner join INFI_TB_100_TITULOS  tit on tit.titulo_id = cus.titulo_id ");
			sqlTablasCriterios.append("left join INFI_TB_704_TITULOS_BLOQUEO titbloq on (titbloq.client_id = clie.client_id and titbloq.titulo_id = cus.titulo_id) ");
			sqlTablasCriterios.append("where clie.tipper_id = ? ");
			sqlTablasCriterios.append(" and  clie.client_cedrif =  ?");

			// Verificar el criterio de busqueda			
			dataCriterios  = new Object[2];
			dataCriterios [0] = beanCriterios.getCedulaIdentidad().substring(0,1);
			dataCriterios [1] = beanCriterios.getCedulaIdentidad().substring(1,beanCriterios.getCedulaIdentidad().length());
			
			// Buscar la cantidad de registros a recuperar
			if (paginaRequerida == 0) {
				this.getCantidad();
				//	No se encontro informacion con los criterios dados
				if (cantRegistros == 0) {
					throw new Throwable("No hay informacion para la CI dada");					
				}
			}
			
			// Buscar la informacion de cada registro que cumple con los criterios
			sqlData.append("select clie.client_cedrif, tit.titulo_id, titulo_descripcion, to_char(cus.titulo_fe_ingreso_custodia,'ddmmyyyy') as fecha_ingreso_custodia, cus.titcus_cantidad as cantidad_custodia, ");
			sqlData.append(" case when titbloq.titulo_id is null then 0 else titbloq.titcus_cantidad end as cantidad_bloqueada,tit.titulo_moneda_den,to_char(tit.titulo_fe_emision,'");
			sqlData.append(ConstantesGenerales.FORMATO_FECHA_WEB_SERVICES);
			sqlData.append("')titulo_fe_emision,to_char(tit.titulo_fe_vencimiento,'");
			sqlData.append(ConstantesGenerales.FORMATO_FECHA_WEB_SERVICES);
			sqlData.append("')titulo_fe_vencimiento ");

			this.getData();
			
			Object [] objAux = new Object[11];
			Long montoAux = new Long(0);
			while (rsData.next()) {
				objAux [0] = rsData.getString("titulo_id");
				objAux [1] = rsData.getString("titulo_descripcion").trim();
				objAux [2] = rsData.getString("fecha_ingreso_custodia");
				montoAux = rsData.getBigDecimal("cantidad_custodia").longValue();
				objAux [3] = montoAux.toString(); 	
				montoAux = rsData.getBigDecimal("cantidad_bloqueada").longValue();
				objAux [4] = montoAux.toString(); 	
				objAux [5] = rsData.getString("titulo_moneda_den");
				objAux [6] = rsData.getString("titulo_fe_emision");
				objAux [7] = rsData.getString("titulo_fe_vencimiento");
				
				/*
				 * Buscamos la tasa cupon activa del titulo
				 */
				TitulosDAO titulosDAO = new TitulosDAO(dso);
				String tasaActiva = titulosDAO.listarTasaActivaWS(rsData.getString("titulo_id"));
				
			//Se verifica que el monto total comisiones tenga 15 digitos 10 enteros y 5 decimales
				if(tasaActiva==null || tasaActiva.equals("")){
					
					objAux [8] = "000000000000000";
				}else
				{
					BigDecimal tasaActivabigdecimal = new BigDecimal(tasaActiva);
					
					objAux [8] = Utilitario.formatoDecimalesWS(tasaActivabigdecimal.setScale(5,BigDecimal.ROUND_HALF_EVEN), 10,5);
				}
				
				/*
				 * Buscamos la tasa de Cambio Vigente
				 */
				MonedaDAO monedaDAO = new MonedaDAO(dso);
				ManejadorTasaCambio manejadorTasaCambio= new ManejadorTasaCambio(null,null);
				BigDecimal tasaCambioActual =  manejadorTasaCambio.getTasaCambio(rsData.getString("titulo_moneda_den"), "", "").getCompra();
				
			//Se verifica tasa de cambio Activa tenga 15 digitos 09 enteros y 06 decimales
				objAux [9] = Utilitario.formatoDecimalesWS(tasaCambioActual!=null?tasaCambioActual.setScale(4,BigDecimal.ROUND_HALF_EVEN):new BigDecimal(0.0),9,6);
			//cerramos la conexion
				monedaDAO.cerrarConexion();
		
			/*
			 * Total nominal del titulo
			 */
				BigDecimal totalNominalTitulo = new BigDecimal(0);
				totalNominalTitulo = rsData.getBigDecimal("cantidad_custodia").multiply(tasaCambioActual);
				/*
				 * Se valida el formato para el total nominal titulo
				 */
				objAux [10] = Utilitario.formatoDecimalesWS(totalNominalTitulo!=null?totalNominalTitulo.setScale(2,BigDecimal.ROUND_HALF_EVEN):new BigDecimal(0.0),3,2);
				
			//Creamos el objeto e inicializamos los valores con el constructor
				beanTituloCustodia = new TituloCustodia(objAux);
				
				lista.add(beanTituloCustodia);
			}
			rsData.close();		
			if (lista.size() == 0) {
				throw new Throwable("No hay informacion para la CI dada");
			} else {
				listaTitulosCustodia.setTituloCustodia(lista);
			}
		} catch (Throwable e) {
			e.printStackTrace();
			throw e;
		}
		listaTitulosCustodia.setParametrosPaginacion(getBeanPaginacion());
		return listaTitulosCustodia;
	}
}
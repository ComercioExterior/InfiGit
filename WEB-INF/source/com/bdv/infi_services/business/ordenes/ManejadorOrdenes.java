package com.bdv.infi_services.business.ordenes;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import com.bdv.infi.dao.OperacionDAO;
import com.bdv.infi.dao.TitulosDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.util.Utilitario;
import com.bdv.infi_services.beans.entidades.mensajes_peticion.ParametrosConsultaOrden;
import com.bdv.infi_services.beans.entidades.mensajes_peticion.ParametrosConsultaOrdenTitulos;
import com.bdv.infi_services.beans.entidades.mensajes_respuesta.OperacionFinancieraOrden;
import com.bdv.infi_services.beans.entidades.mensajes_respuesta.OperacionesFinancierasOrden;
import com.bdv.infi_services.beans.entidades.mensajes_respuesta.OrdenRespuesta;
import com.bdv.infi_services.beans.entidades.mensajes_respuesta.OrdenRespuestaTitulos;
import com.bdv.infi_services.beans.entidades.mensajes_respuesta.OrdenesRespuesta;
import com.bdv.infi_services.beans.entidades.mensajes_respuesta.TOrdenTitulo;
import com.bdv.infi_services.business.AbstractConsultaPaginada;
import com.bdv.infi_services.utilities.DBOServices;
import com.megasoft.soa.webservices.commom.WSProperties;

/**
 * Clase que permite encapsular la funcionalidad de de las Consultas sobre las Ordenes
 * @author elaucho,mgalindo
 */   
public class ManejadorOrdenes  extends AbstractConsultaPaginada {
	
	
	String 	dsName 						  = WSProperties.getProperty("datasource-infi");
	public javax.sql.DataSource dso 	  = DBOServices.getDataSource(dsName);
	/**
	 * Formato de Fecha
	 */
	private SimpleDateFormat sdIODate = new SimpleDateFormat("dd-MM-yyyy");
	/**
	 * Calendario para manejo de fecha bordes
	 */
	private Calendar calendario = Calendar.getInstance();
	
	/**
	 * Constructor de la Clase
	 */
	public ManejadorOrdenes () throws Exception {
		super();
	}
	
	/**
	 * Metodo que permite recuperar ordenes en base a los criterios especificados
	 * @param beanCriterios : bean ParametrosConsultaOrden con los criterios de busqueda
	 * @return OrdenesRespuesta con los resultados de la consulta: Lista de Ordenes recuperadas o Mensajes de Excepcion
	 * @throws Throwable
	 */
	public OrdenesRespuesta getOrdenes(ParametrosConsultaOrden beanCriterios) throws Throwable {
		
		//	parametros de paginacion
		setBeanPaginacion(beanCriterios.getParametrosPaginacion());
		
		OrdenesRespuesta listaOrdenes = new OrdenesRespuesta();
		OrdenRespuesta beanOrden;
		ArrayList<OrdenRespuesta> lista = new ArrayList<OrdenRespuesta>();
		
		try{			
			sqlTablasCriterios.append("from INFI_TB_204_ORDENES orden ");
			sqlTablasCriterios.append("inner join INFI_TB_012_TRANSACCIONES trans on trans.transa_id = orden.transa_id ");
			sqlTablasCriterios.append("inner join INFI_TB_106_UNIDAD_INVERSION undinv on undinv.undinv_id = orden.uniinv_id ");
			sqlTablasCriterios.append("inner join INFI_TB_201_CTES cliente on cliente.client_id = orden.client_id ");
			sqlTablasCriterios.append("inner join INFI_TB_203_ORDENES_STATUS ordsta on ordsta.ordsta_id = orden.ordsta_id ");
			sqlTablasCriterios.append("inner join INFI_TB_016_EMPRESAS on undinv.EMPRES_ID = INFI_TB_016_EMPRESAS.EMPRES_ID ");
			sqlTablasCriterios.append("inner join INFI_TB_101_INST_FINANCIEROS on undinv.INSFIN_ID=INFI_TB_101_INST_FINANCIEROS.INSFIN_ID ");

			// Verificar el criterio de busqueda
			if (!beanCriterios.getIdOrden().equals("0")) {
				sqlTablasCriterios.append("where orden.ordene_id = ?");
				dataCriterios  = new Object[1];
				dataCriterios[0]= new Integer(beanCriterios.getIdOrden());
			} else if (!beanCriterios.getCedulaIndentidad().equals("")) {
				sqlTablasCriterios.append("where client_cedrif = ? and tipper_id = ?");
				dataCriterios  = new Object[2];
				String var = beanCriterios.getCedulaIndentidad();
				dataCriterios[0]= var.substring(1,var.length());
				dataCriterios[1]= var.substring(0,1);
			} else if (!beanCriterios.getFechaDesde().equals("")){
				sqlTablasCriterios.append("where ordene_ped_fe_orden between to_date(?,'dd-MM-yyyy') and to_date(?,'dd-MM-yyyy')");
				dataCriterios  = new Object[2];
				Date date = sdIODate.parse(beanCriterios.getFechaDesde());
				calendario.setTime(date);
				calendario.add(Calendar.DAY_OF_MONTH, -1);
				dataCriterios[0]= calendario.getTime();
				date = sdIODate.parse(beanCriterios.getFechaHasta());
				calendario.setTime(date);
				calendario.add(Calendar.DAY_OF_MONTH, 1);
				dataCriterios[1]= calendario.getTime();		
			}
			
// Buscar la cantidad de registros a recuperar
	if (paginaRequerida == 0) {
		this.getCantidad();
		//	No se encontro informacion con los criterios dados
		if (cantRegistros == 0) {
			if (!beanCriterios.getIdOrden().equals("0")) {
				throw new Throwable("No hay informacion para la Orden dada");
			} else if (!beanCriterios.getCedulaIndentidad().equals("")) {
				throw new Throwable("No hay informacion asociada a CI dada");
			} else {
				throw new Throwable("No hay informacion entre las fechas dadas");
			}					
		}
	}
		
	// Buscar la informacion de cada registro que cumple con los criterios
	sqlData.append("select ordene_id, to_char(ordene_ped_fe_orden, 'ddMMRRRR') as fe_orden, case when ordene_ped_monto is null then 0 else ordene_ped_monto end as ordene_ped_monto, case when ordene_adj_monto is null then 0 else ordene_adj_monto end as ordene_adj_monto, ");
	sqlData.append("contraparte, ' ' as depositario, transa_descripcion, undinv_nombre, client_nombre, ordsta_nombre, ordene_ped_fe_orden ,undinv.EMPRES_ID,INFI_TB_016_EMPRESAS.EMPRES_NOMBRE, undinv.INSFIN_ID,INFI_TB_101_INST_FINANCIEROS.INSFIN_FORMA_ORDEN,undinv.UNDINV_SERIE,to_char(undinv.UNDINV_FE_EMISION,'ddMMRRRR')UNDINV_FE_EMISION,to_char(undinv.UNDINV_FE_ADJUDICACION,'ddMMRRRR')UNDINV_FE_ADJUDICACION,to_char(orden.ORDENE_PED_FE_VALOR,'ddMMRRRR')ORDENE_PED_FE_VALOR,undinv.MONEDA_ID,undinv.UNDINV_TASA_CAMBIO,orden.TRANSA_ID ");
	
	this.getData();

	Object [] objAux = new Object[20];
	while (rsData.next()) {
		objAux [0] = rsData.getString("ordene_id");
		objAux [1] = rsData.getString("transa_descripcion");
		objAux [2] = rsData.getString("undinv_nombre");
		objAux [3] = rsData.getString("fe_orden");
		objAux [4] = rsData.getString("client_nombre");
		objAux [5] = rsData.getString("ordsta_nombre");
						
		//Se verifica que el monto pedido tenga 15 digitos 11 enteros y 4 decimales
		if(rsData.getBigDecimal("ordene_ped_monto")!=null){
			BigDecimal montoPedido = rsData.getBigDecimal("ordene_ped_monto").setScale(4,BigDecimal.ROUND_HALF_EVEN);
			objAux[6] = Utilitario.formatoDecimalesWS(montoPedido!=null?montoPedido:new BigDecimal(0), 11, 4);
		}

		//Se verifica que el monto adjudicado tenga 15 digitos 11 enteros y 4 decimales
		if(rsData.getBigDecimal("ordene_adj_monto")!=null){
			BigDecimal montoAdjudicado = rsData.getBigDecimal("ordene_adj_monto").setScale(4,BigDecimal.ROUND_HALF_EVEN);
			objAux[7] = Utilitario.formatoDecimalesWS(montoAdjudicado!=null?montoAdjudicado:new BigDecimal(0), 11, 4);
		}
		objAux [8] = rsData.getString("depositario");
		objAux [9] = rsData.getString("contraparte");
				
		//Se verifica tasa de la unidad de inversion tenga 15 digitos 09 enteros y 06 decimales
		if(rsData.getString("UNDINV_TASA_CAMBIO")!=null)
		{
			BigDecimal tasaUnidadInversion = rsData.getBigDecimal("UNDINV_TASA_CAMBIO").setScale(6,BigDecimal.ROUND_HALF_EVEN);
			objAux[10] = Utilitario.formatoDecimalesWS(tasaUnidadInversion!=null?tasaUnidadInversion:new BigDecimal(0),9,6);
		}
		objAux [11] = rsData.getString("UNDINV_FE_ADJUDICACION");
		objAux [12] = rsData.getString("ORDENE_PED_FE_VALOR");
		
		/*
		 * Si la transaccion es de toma de orden se agregan los siguientes campos como respuesta
		 */
		if(rsData.getString("transa_id").equals(TransaccionNegocio.TOMA_DE_ORDEN))
		{
			objAux [13] = rsData.getString("EMPRES_ID");
			objAux [14] = rsData.getString("INSFIN_FORMA_ORDEN");
			objAux [15] = rsData.getString("UNDINV_SERIE");
			objAux [16] = rsData.getString("UNDINV_FE_EMISION");
			objAux [17] = rsData.getString("MONEDA_ID");
		}
		/*
		 *Se buscan las operaciones financieras de la orden para setearselas al objeto OrdeRespuesta 
		 */
		
		OperacionDAO operacionDAO = new OperacionDAO(dso);
		operacionDAO.listarOperacionesFinancieraOrden(Long.parseLong(rsData.getString("ordene_id")));
		OperacionesFinancierasOrden operacionesFinancierasOrden = new OperacionesFinancierasOrden();
				
	if(operacionDAO.getDataSet().count()>0){
		operacionDAO.getDataSet().first();
		OperacionFinancieraOrden operacionFinancieraOrden= new OperacionFinancieraOrden();
		ArrayList<OperacionFinancieraOrden> operacionesFinancierasArray = new ArrayList<OperacionFinancieraOrden>();
		while(operacionDAO.getDataSet().next()){
			
			operacionFinancieraOrden.setIndicadorComision(operacionDAO.getDataSet().getValue("COMISION"));
			operacionFinancieraOrden.setMonedaId(operacionDAO.getDataSet().getValue("MONEDA_ID"));
					
	/*
	 * Se verifica el formato del monto de la operacion que sera enviado 10 enteros y 5 decimales
	 */
		if(operacionDAO.getDataSet().getValue("MONTO_OPERACION")!=null && !operacionDAO.getDataSet().getValue("MONTO_OPERACION").equals("")){

			BigDecimal montoOperacion = new BigDecimal(operacionDAO.getDataSet().getValue("MONTO_OPERACION")).setScale(5,BigDecimal.ROUND_HALF_EVEN);
			String montoOperacion_ = Utilitario.formatoDecimalesWS(montoOperacion!=null?montoOperacion:new BigDecimal(0),10,5);
			operacionFinancieraOrden.setMonto(montoOperacion_);
		}
	/*
	 * Se verifica el formato de la Tasa de la operacion que sera enviado 3 enteros y 6 decimales
	 */			
		if(operacionDAO.getDataSet().getValue("TASA")!=null && !operacionDAO.getDataSet().getValue("TASA").equals(""))
		{
			BigDecimal tasa = new BigDecimal(operacionDAO.getDataSet().getValue("TASA")).setScale(6,BigDecimal.ROUND_HALF_EVEN);
			String tasaString = Utilitario.formatoDecimalesWS(tasa!=null?tasa:new BigDecimal(0),3,6);
			operacionFinancieraOrden.setTasa(tasaString);
		}

		operacionFinancieraOrden.setTipoOperacion(operacionDAO.getDataSet().getValue("TRNF_TIPO"));
		
		/*
		 * Agregamos las operaciones al arrayList
		 */
		operacionesFinancierasArray.add(operacionFinancieraOrden);
		
		operacionFinancieraOrden = new OperacionFinancieraOrden();
	}//fin while
		operacionesFinancierasOrden.setOperacionesFinancierasOrden(operacionesFinancierasArray);
	}//fin operaciones financieras por orden
	
	/*
	 * Se verifica el monto de reintegro de capital y el monto de reintegro de comision
	 */	
		operacionDAO = new OperacionDAO(dso);
		operacionDAO.listarMontoReintegro(rsData.getLong("ordene_id"));
		String reintegroCapital  = "";
		String reintegroComision = "";
		if(operacionDAO.getDataSet().count()>0){	
			operacionDAO.getDataSet().first();
			operacionDAO.getDataSet().next();
			reintegroCapital = operacionDAO.getDataSet().getValue("reintegro_capital");
			reintegroComision = operacionDAO.getDataSet().getValue("reintegro_comision");
	/*
	 * Se verifica monto reintegro de capital tenga 15 digitos 09 enteros y 06 decimales
	 */	
		if(reintegroCapital!=null && !reintegroCapital.equals(""))
		{
			BigDecimal montoReintegroCapital = new BigDecimal(reintegroCapital).setScale(6,BigDecimal.ROUND_HALF_EVEN);
			String reintegroCapitalString = Utilitario.formatoDecimalesWS(montoReintegroCapital!=null?montoReintegroCapital:new BigDecimal(0),9,6);
			objAux [18] = reintegroCapitalString;
		}
		/*
		 * Se verifica monto reintegro de comision tenga 15 digitos 09 enteros y 06 decimales
		 */
		if(reintegroComision!=null && !reintegroComision.equals(""))
		{
			BigDecimal montoReintegroComision = new BigDecimal(reintegroComision).setScale(6,BigDecimal.ROUND_HALF_EVEN);
			String montoReintegroComisionString = Utilitario.formatoDecimalesWS(montoReintegroComision!=null?montoReintegroComision:new BigDecimal(0),9,6);
			objAux [19] = montoReintegroComisionString;
		}
	}//FIN IF operacionDAO.getDataSet().count()>0
		/*
		 * Se crea el objeto de OrdenRespuesta
		 */
		beanOrden = new OrdenRespuesta(objAux);
		/*
		 * SE AGREGAN LA SOPERACIONES FIANNCIERAS AL OBJETO ORDEN
		 */
		if(operacionesFinancierasOrden!=null)
		beanOrden.setOperacionesFinancieras(operacionesFinancierasOrden);

		objAux = new Object[20];
		
		lista.add(beanOrden);
	}//FIN while (rsData.next())
	/*
	 * CRERRAMOS EL RESULSET
	 */
			rsData.close();
			
			if (lista.size() == 0) {
				if (!beanCriterios.getIdOrden().equals("0")) {
					throw new Throwable("No hay informacion para la Orden dada");
				} else if (!beanCriterios.getCedulaIndentidad().equals("")) {
					throw new Throwable("No hay informacion asociada a CI dada");
				} else {
					throw new Throwable("No hay informacion entre las fechas dadas");
				}
			} else {
				listaOrdenes.setListaOrdenes(lista);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		listaOrdenes.setParametrosPaginacion(getBeanPaginacion());
		return listaOrdenes;
	}
	
	/**
	 * Metodo que permite recuperar los titulos de una Orden
	 * @param beanCriterios : bean ParametrosConsultaOrdenTitulos con los criterios de busqueda
	 * @return OrdenRespuestaTitulos con los resultados de la consulta: Lista de Titulos recuperadas o Mensajes de Excepcion
	 * @throws Throwable
	 */
	public OrdenRespuestaTitulos getOrdenTitulos(ParametrosConsultaOrdenTitulos beanCriterios) throws Throwable {
		
		//	parametros de paginacion
		setBeanPaginacion(beanCriterios.getParametrosPaginacion());
				
		OrdenRespuestaTitulos listaTitulos = new OrdenRespuestaTitulos();
		listaTitulos.setIdOrden(beanCriterios.getIdOrden());
		TOrdenTitulo beanTitulo;
		ArrayList<TOrdenTitulo> lista = new ArrayList<TOrdenTitulo>();
		
		try{
			sqlTablasCriterios.append("from INFI_TB_206_ORDENES_TITULOS ordtit ");
			sqlTablasCriterios.append("inner join INFI_TB_100_TITULOS tit on tit.titulo_id = ordtit.titulo_id ");
			sqlTablasCriterios.append("where ordtit.ordene_id = ?");
			
			// Verificar el criterio de busqueda
			dataCriterios  = new Object[1];
			dataCriterios[0]= new Integer(beanCriterios.getIdOrden());
			
			// Buscar la cantidad de registros a recuperar
			if (paginaRequerida == 0) {
				this.getCantidad();
				//	No se encontro informacion con los criterios dados
					if (cantRegistros == 0) {
						throw new Throwable("No hay informacion para la Orden dada");
				} 
			}
	/*
	 * Buscar la informacion de cada registro que cumple con los criterios
	 */
			sqlData.append("select ordtit.titulo_id, titulo_unidades, titulo_descripcion,tit.titulo_valor_nominal,ordtit.TITULO_PCT,to_char(tit.TITULO_FE_EMISION,'").append(ConstantesGenerales.FORMATO_FECHA_WEB_SERVICES);
			sqlData.append("')TITULO_FE_EMISION,to_char(tit.TITULO_FE_VENCIMIENTO,'");
			sqlData.append(ConstantesGenerales.FORMATO_FECHA_WEB_SERVICES);
			sqlData.append("')TITULO_FE_VENCIMIENTO,'' as tasa  ");
			
			this.getData();
			
			while (rsData.next()) {
				beanTitulo = new TOrdenTitulo();
				beanTitulo.setIdTitulo(rsData.getString("titulo_id"));
				beanTitulo.setDescrTitulo(rsData.getString("titulo_descripcion").trim());
				beanTitulo.setValorNominal(rsData.getString("titulo_unidades"));
				beanTitulo.setPorcentaje(rsData.getString("titulo_pct"));
				beanTitulo.setFechaEmision(rsData.getString("titulo_fe_emision"));
				beanTitulo.setFechaVencimiento(rsData.getString("titulo_fe_vencimiento"));
				
				/*
				 * Buscamos la tasa activa del titulo
				 */
				TitulosDAO titulosDAO = new TitulosDAO(dso);
				String tasaActiva = titulosDAO.listarTasaActivaWS(rsData.getString("titulo_id"));
				
				if(tasaActiva!=null && !tasaActiva.equals(""))
				{
					BigDecimal tasaCuponActiva = new BigDecimal(tasaActiva).setScale(6,BigDecimal.ROUND_HALF_EVEN);
					tasaActiva = Utilitario.formatoDecimalesWS(tasaCuponActiva!=null?tasaCuponActiva:new BigDecimal(0),10,6);
				}
				beanTitulo.setTasaCuponVigente(tasaActiva);
				lista.add(beanTitulo);
			}
			rsData.close();
			if (lista.size() == 0) {
				throw new Throwable("No hay informacion para la Orden dada");
			} else {
				listaTitulos.setTOrdenTitulo(lista);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		listaTitulos.setParametrosPaginacion(getBeanPaginacion());
		return listaTitulos;
	}
}
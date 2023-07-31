package com.bdv.infi_services.business;

import java.math.BigDecimal;
import java.sql.ResultSet;

import com.bdv.infi_services.beans.entidades.ParametrosPaginacion;
import com.bdv.infi_services.utilities.DBOServices;
import com.megasoft.soa.webservices.commom.WSProperties;


/**
 * Clase que permite encapsular la funcionalidad de Clase que ejecuta Consultas sobre la base de datos. <br>
 * Puede recuperar la informacion en forma pagina o no. Se basa en los siguientes parametros: <br>
 * 	-	Indicador de Pagineo : true o false <br>
 * 	-	Pagina requerida  <br>
 * Devuelve <br>
 * 1.-	Indicador de pagineo false : <br>
 * 		-	Cantidad total de paginas que tendria el pagineo<br>
 * 		-	Devuelve toda la informacion recuperada <br>
 * 2.-	Indicador de pagineo true : <br>
 * 		2.1.-	Si la pagina requerida = 0  <br>
 * 		-	Cantidad total de paginas que tendria el pagineo <br>
 *  	-	Devuelve la informacion recuperada para la primera pagina <br>
 * 		2.2.-	Si la pagina requerida > 0 <br>
 *  	-	Devuelve la informacion recuperada para la requerida pagina <br>
 * @author MegaSoft Computacion para Banco de Venezuela
 */
public class AbstractConsultaPaginada {
	/**
	 * Constante para eliminar los decimales
	 */
	protected static final BigDecimal CIEN_BD = new BigDecimal (100);
	/** 
	 * Nombre del DatSource a utilizar
	 */
	protected String dsName = "";
	/**
	 * Bean con la informacion de paginacion en la consulta
	 */
	protected ParametrosPaginacion beanPaginacion;
	/**
	 * Variables de pagineo
	 */
	protected boolean indicaPagineo = false;
	protected int paginaRequerida = 0;
	protected int registrosPagina = 14;
	/**
	 * Cantidad de registros recuperados
	 */
	protected int cantRegistros = 0;
	/**
	 * cantidad de paginas generadas por los criterios
	 */
	protected int cantPaginas = 0;
	/**
	 * Variables de trabajo
	 */
	protected StringBuffer sqlTablasCriterios = new StringBuffer();	
	protected StringBuffer sqlData  = new StringBuffer();
	private StringBuffer sqlCantidad  = new StringBuffer();
	private StringBuffer sql  = new StringBuffer();
	/**
	 * Valores de los criterios a aplicar
	 */
	protected Object[] dataCriterios  = null;
	/**
	 * ResultSet con la informacion recuperada
	 */
	protected ResultSet rsData = null;
	
	/**
	 * Devuelve el bean con la informacion de paginacion
	 * @return beanPaginacion
	 */
	protected ParametrosPaginacion getBeanPaginacion() {
		
		beanPaginacion.setPaginaRequerida(String.valueOf(paginaRequerida));
		beanPaginacion.setCantPaginas(String.valueOf(cantPaginas));
		return beanPaginacion;
	}
	/**
	 * Almacena el bean con la informacion de paginacion
	 * @param listaOrdenes
	 */
	protected void setBeanPaginacion(ParametrosPaginacion beanPaginacion) {
		this.beanPaginacion = beanPaginacion;
		indicaPagineo = beanPaginacion.getIndicaPagineo().startsWith("true"); 
		paginaRequerida = new Integer(beanPaginacion.getPaginaRequerida()).intValue();
		int ca = new Integer(beanPaginacion.getRegistrosPagina()).intValue();
		if (ca > 0)
			cantRegistros = ca;
	}
	
	/**
	 * Constructor del bean
	 */
	public AbstractConsultaPaginada () throws Exception {
		dsName = WSProperties.getProperty("datasource-infi");
	}
	
	/**
	 * Metodo que permite buscar la cantidad de registros a recuperar
	 * @throws Throwable
	 */
	protected void getCantidad() throws Throwable	{
		
		try{
			sqlCantidad = new StringBuffer("select count(*) as cantidad ");
			sqlCantidad.append(sqlTablasCriterios);
			
			ResultSet rsCantidad = DBOServices.executeQuery(dsName, null, sqlCantidad.toString(), dataCriterios);
			if (!rsCantidad.next()) {
				throw new Exception("BDMSG0000");
			}
			cantRegistros = rsCantidad.getInt("cantidad");
			rsCantidad.close();
			//	No se encontro informacion con los criterios dados
			if (cantRegistros == 0) {
				return;
			}
			cantPaginas = cantRegistros / registrosPagina;
			if (cantRegistros%registrosPagina != 0) 
				++cantPaginas;
			if (cantPaginas == 0)
				cantPaginas = 1;
			paginaRequerida = 1;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}
	
	/**
	 * Metodo que permite recuperar la informacion
	 * @throws Throwable
	 */
	protected void getData() throws Throwable	{
		
		try{
			if(indicaPagineo) {
				sql.append("select * from (");
			}
			sql.append(sqlData);
			if (indicaPagineo) {
				sql.append(", ROWNUM as rn ");
			}
			sql.append(sqlTablasCriterios);
			if(indicaPagineo) {
				int offset = ((paginaRequerida-1) * registrosPagina) + 1;
				int max = offset + registrosPagina -1;
				sql.append(" ) where rn between ").append(offset +" and ").append(max+"");				
			}
			
			rsData = DBOServices.executeQuery(dsName, null, sql.toString(), dataCriterios);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}
}

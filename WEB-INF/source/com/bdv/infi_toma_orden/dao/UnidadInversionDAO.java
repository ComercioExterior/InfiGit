package com.bdv.infi_toma_orden.dao;

import java.util.ArrayList;
import java.util.Date;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.bdv.infi_toma_orden.data.OrdenTitulo;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.util.DB;

import com.bdv.infi_toma_orden.util.Utilitario;


/** 
 * Clase usada para la lógica de inserción, modificación y recuperación de las ordenes en la base de datos
 */
public class UnidadInversionDAO extends GenericoDAO {
	
	private Logger logger = Logger.getLogger(UnidadInversionDAO.class);
	/**
	 * Lista de Titulos recuperados
	 */
	private	ArrayList<OrdenTitulo> listaTitulos = new ArrayList<OrdenTitulo>();

	/**
	 * Devuelve la lista de bean de Titulos con la informacion recuperda
	 * @return unidadInversioneDisponible
	 */
	public ArrayList<OrdenTitulo> getListaTitulos() {
		return listaTitulos;
	}

	/**
	 * Constructor de la clase
	 * @param nombreDataSource : nombre que se obtiene del ambiente de ejecucion de los WebService
	 * @param dso : DataSource instanciado por clases que se ejecutan en ambientes Web
	 */
	public UnidadInversionDAO (String nombreDataSource, DataSource dso) {
		super(nombreDataSource, dso);
	}
	
	/**
	 * Lista todos los blotters asociados a una Unidad de inversion encontrados en la base de datos. 
	 * Almacena el resultado en un dataset
	 * @param idUnidadInversion : identificador de la Unidad de Inversion
	 * @param idBlotter : identificador del Blotter
	 * @throws Exception arroja una excepción en caso de un error
	 * */
	public void buscarBlottersPorID(long idUnidadInversion, String idBlotter, String tipoPersona) throws Exception {
		
		StringBuffer sql = new StringBuffer();
		sql.append("select count(*) cantidad ");	
		sql.append("from INFI_TB_107_UI_BLOTTER uiblo ");
		sql.append("where undinv_id = ? ");	
		sql.append("and   bloter_id = ? ");	
		sql.append("and   uiblot_in_disponible = 1 ");
		sql.append("and   " +formatearFechaBDActual()+ " between  uiblot_pedido_fe_ini and uiblot_pedido_fe_fin ");	
		sql.append("and	  to_char(SYSDATE, 'hh24mi') between  to_char(uiblot_horario_desde,'hh24mi') and to_char(uiblot_horario_hasta,'hh24mi') ");
		//sql.append("and   INSTRB(uiblo.tppeva_id, ?, 1, 1) > 0");
		
		Object[] data  = new Object[2];
		data[0]= idUnidadInversion;
		data[1]= idBlotter;
		//data[2]= tipoPersona;
		try {
			resultSet = DB.executeQuery(dso, sql.toString(), data);
			
			if (!resultSet.next()) {
				throw new Exception("MSGTO9003");
			}
			if (resultSet.getInt("cantidad") == 0) {
				throw new Exception("MSGTO9003");
			}
		} catch (Exception e) {
			logger.error(e.getMessage()+ com.bdv.infi.util.Utilitario.stackTraceException(e));
			if (e.getMessage().indexOf("MSGTO9003") > -1) {
				throw new Exception(e);
			} else {
				logger.error(e.getMessage()+ com.bdv.infi.util.Utilitario.stackTraceException(e));
				throw new Exception("Error buscando los blotters asociados a la unidad de inversión. ");
			}
		} finally {
			if (resultSet != null)
				resultSet.close();
		}
	}
	
	/**Lista valores asociado a la unidad de inversion con blotter
	 * return Object[]
	 * Object[0] valor de ganancia de red (Double) 
	 * */
	public Object[] listarValoresUiBlotter(long idUnidadInversion, String idBlotter) throws Exception {
		
		StringBuffer sql = new StringBuffer();
		sql.append("select UIBLOT_GANANCIA_RED ");	
		sql.append("from INFI_TB_107_UI_BLOTTER uiblo ");
		sql.append("where undinv_id = ? ");	
		sql.append("and   bloter_id = ? ");	
		sql.append("and   uiblot_in_disponible = 1 ");
		
		Object[] data  = new Object[2];
		data[0]= idUnidadInversion;
		data[1]= idBlotter;	

		Object [] objResultado = null;
		try {
			resultSet = DB.executeQuery(dso, sql.toString(), data);
			
			if (!resultSet.next()) {
				throw new Exception("MSGTO9003");
			}
			
			objResultado = new Object[1];
			
			objResultado[0]= resultSet.getDouble("UIBLOT_GANANCIA_RED");
			
		} catch (Exception e) {
			if (e.getMessage().indexOf("MSGTO9003") > -1) {
				throw new Exception(e);
			} else {
				logger.error(e.getMessage()+ com.bdv.infi.util.Utilitario.stackTraceException(e));
				throw new Exception("Error buscando los datos del blotter.");
			}
		} finally {
			if (resultSet != null)
				resultSet.close();
		}
		return objResultado;
	}
	
	

	/**
	 * Buscar los Titulos de una Unidad de Inversion
	 * Modificado por NM25287. Requerimiento SICAD
	 * @param idUnidadInversion : Identificador de la Unidad de Inversion
	 * @return true  : si hay Titulos asociadas a la UI  <br>
	 *         false : si no hay 
	 * @throws Exception
	 *             lanza una exception no hay titulos
	 */
	public boolean listarTitulosID(Long idUnidadInversion, boolean subastaTitulos) throws Exception {
		
		boolean bolOk = false;
		OrdenTitulo beanOrdenTitulo;
		StringBuffer sql = new StringBuffer();
		if (subastaTitulos) {
			sql.append("SELECT UT.TITULO_ID,UT.UITITU_PORCENTAJE,UT.TITULO_ID AS TITULO_DESCRIPCION,UT.UITITU_VALOR_EQUIVALENTE AS TITULO_VALOR_NOMINAL,");
			sql.append("UI.MONEDA_ID AS TITULO_MONEDA_DEN,UT.UITITU_IN_CONIDB,UI.MONEDA_ID AS TITULO_MONEDA_NEG,UI.UNDINV_FE_EMISION AS FECHA_EMISION,");
			sql.append("UI.UNDINV_FE_CIERRE AS FECHA_VCTO, '' AS BASIS ");
			sql.append(" FROM  INFI_TB_108_UI_TITULOS UT, INFI_TB_106_UNIDAD_INVERSION UI");
			sql.append(" WHERE UI.UNDINV_ID= UT.UNDINV_ID AND UT.UNDINV_ID = ? ");
		}else{
			sql.append("select uitit.titulo_id, uititu_porcentaje, titulo_descripcion, titulo_valor_nominal, titulo_moneda_den, uitit.UITITU_IN_CONIDB, titulo_moneda_neg, ");
			sql.append(" to_char(tit.titulo_fe_emision, '" + ConstantesGenerales.FORMATO_FECHA + "') as fecha_emision, to_char(tit.titulo_fe_vencimiento,'" + ConstantesGenerales.FORMATO_FECHA + "') as fecha_vcto, tit.basis ");
			sql.append("from INFI_TB_108_UI_TITULOS uitit ");
			sql.append("inner join INFI_TB_100_TITULOS tit on tit.titulo_id = uitit.titulo_id ");
			//sql.append("inner join INFI_VI_MONEDAS mon on mon.moneda_id = tit.titulo_moneda_den ");
			sql.append("where uitit.undinv_id = ? ");
		}
		
		
		System.out.println("sql: "+sql.toString());
		
	    Object[] data  = new Object[1];
		data[0]= idUnidadInversion;
		try {
			resultSet = DB.executeQuery(dso, sql.toString(), data);
			
			Object [] objAux = new Object[11];
			while (resultSet.next()) {
				objAux[0] = new Long(0);
				objAux[1] = resultSet.getString("titulo_id");
				objAux[2] = resultSet.getString("titulo_descripcion"); 
				objAux[3] = resultSet.getBigDecimal("titulo_valor_nominal");				
				objAux[4] = resultSet.getBigDecimal("uititu_porcentaje");
				objAux[5] = resultSet.getString("titulo_moneda_den"); 
				
				/*objAux[6] = new BigDecimal(0);
				
				if(resultSet.getString("titulos_precio_recompra")!=null)
					objAux[6] = resultSet.getBigDecimal("titulos_precio_recompra"); */
				
				/*objAux[7] = Utilitario.StringToDate(resultSet.getString("fecha_emision"), ConstantesGenerales.FORMATO_FECHA); 
				objAux[8] = Utilitario.StringToDate(resultSet.getString("fecha_vcto"),ConstantesGenerales.FORMATO_FECHA); 
				objAux[9] = resultSet.getString("basis");*/
				
				objAux[6] = Utilitario.StringToDate(resultSet.getString("fecha_emision"), ConstantesGenerales.FORMATO_FECHA); 
				objAux[7] = Utilitario.StringToDate(resultSet.getString("fecha_vcto"),ConstantesGenerales.FORMATO_FECHA); 
				objAux[8] = resultSet.getString("basis");
				//indicador de titulo con o sin IDB
				objAux[9] = resultSet.getInt("UITITU_IN_CONIDB");
				objAux[10] = resultSet.getString("titulo_moneda_neg");
				
				
				beanOrdenTitulo = new OrdenTitulo(objAux);
				listaTitulos.add(beanOrdenTitulo);
			}
			if (listaTitulos.size() == 0) {
				throw new Exception("No Existe Titulo en OPICS asociado a la Unidad de Inversion. (Titulo ID): "+idUnidadInversion);
			}
			bolOk = true;
		} catch (Exception e) {
			logger.error(e.getMessage()+ com.bdv.infi.util.Utilitario.stackTraceException(e));
			throw new Exception("Error Consultando Base de Datos OPICS");
		} finally {
			if (resultSet != null)
				resultSet.close();
		}
		return bolOk;
	}
	
	
	/**
	 * Buscar informacion de una Unidad de Inversion a ser usada en una Toma de Orden 
	 * 
	 * @param idUnidadInversion : Identificador de la Unidad de Inversion
	 * @param inValida          : Indicador de buscar la informacion de limites para validar
	 * @return true  : si existe  <br>
	 *         false : si existe
	 * @throws Exception
	 *             lanza una exception no hay informacion
	 */
	public Object [] listarID(Long idUnidadInversion, boolean inValida) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select undinv.insfin_id,empres_id, undinv_tasa_cambio, undinv_umi_unidad, undinv_umi_inv_disponible, insfin_in_inventario, insfin_forma_orden, undinv_status, insfin.tipo_producto_id, insfin.manejo_productos, ");
		sql.append(" undinv_in_pedido_monto, undinv_tasa_pool, undinv_rendimiento, undinv_mercado, undinv_in_precio_sucio, undinv.moneda_id, undinv.undinv_multiplos, UNDINV_IN_VTA_EMPLEADOS, undinv_nombre, ");
		sql.append("insfin_multiples_ordenes, insfin_descripcion, UNDINV_IN_RECOMPRA_NETEO, UNDINV_PERIODO_VENTA, COMISION_IGTF ");
		sql.append("from INFI_TB_106_UNIDAD_INVERSION undinv ");
		sql.append("inner join INFI_TB_101_INST_FINANCIEROS insfin on insfin.insfin_id = undinv.insfin_id ");
		/* ITS-3227 Incidencia servidor de Rentabilidad caido 01-Jul-16. Código comentado para permitir la continuidad de la operativa NM25287
		 * sql.append("inner join INFI_VI_MONEDAS mon on mon.moneda_id = undinv.moneda_id ");*/
		sql.append("where undinv.undinv_id = ").append(idUnidadInversion);
		Object [] objResultado = null;
		try {
			
			conn = dso.getConnection();
			this.statement = conn.createStatement();
			resultQuery = statement.executeQuery(sql.toString());
			if (!resultQuery.next()) {
				throw new Exception("MSGTO9001");
			}
			
			//NM29643 - INFI_TTS_443 16/04/2014: Se modifica query para traer manejo_productos
			objResultado = new Object[23];
		
			objResultado[0] = resultQuery.getString("empres_id");
			objResultado[1] = resultQuery.getBigDecimal("undinv_umi_unidad");	
			objResultado[2] = resultQuery.getBigDecimal("undinv_tasa_cambio");
			//objResultado[3] = resultSet.getInt("insfin_in_inventario");
			objResultado[3] = resultQuery.getString("insfin_forma_orden");
			objResultado[4] = resultQuery.getBigDecimal("undinv_umi_inv_disponible");
			objResultado[5] = resultQuery.getInt("undinv_in_pedido_monto");
			objResultado[6] = resultQuery.getBigDecimal("undinv_tasa_pool");
			objResultado[7] = resultQuery.getBigDecimal("undinv_rendimiento");			
			objResultado[8] = resultQuery.getString("undinv_mercado");
			objResultado[9] = resultQuery.getInt("undinv_in_precio_sucio");
			objResultado[10] = resultQuery.getString("moneda_id");
			objResultado[11] = resultQuery.getBigDecimal("undinv_multiplos");
			objResultado[12] = resultQuery.getInt("UNDINV_IN_VTA_EMPLEADOS");
			objResultado[13] = resultQuery.getString("undinv_nombre");
			objResultado[14] = resultQuery.getInt("insfin_multiples_ordenes");
			objResultado[15] = resultQuery.getString("insfin_descripcion");
			objResultado[16] = resultQuery.getString("undinv_status");
			objResultado[17] = resultQuery.getInt("UNDINV_IN_RECOMPRA_NETEO");
			objResultado[18] = resultQuery.getString("INSFIN_ID");
			objResultado[19] = resultQuery.getString("tipo_producto_id");
			//NM29643 - INFI_TTS_443 16/04/2014: Se modifica query para traer manejo_productos
			objResultado[20] = resultQuery.getInt("manejo_productos");
			objResultado[21] = resultQuery.getInt("UNDINV_PERIODO_VENTA");
			objResultado[22] = resultQuery.getDouble("COMISION_IGTF");
						
		} catch (Exception e) { 
			if (e.getMessage().indexOf("MSGTO9001") > -1) {
				throw new Exception(e);
			} else {
				logger.error(e.getMessage()+ com.bdv.infi.util.Utilitario.stackTraceException(e));
				throw new Exception(e);
		}
		} finally {
			if (resultQuery != null)
				resultQuery.close();
			
			if(statement!=null){
				statement.close();
			}
			if(conn!=null){
				conn.close();
			}
		}
		return objResultado;
	}
	
	public Date listaFechaLiquidacion(long idUnidadInversion) throws Exception {
		
		/*StringBuffer sql = new StringBuffer();
		sql.append("select initcap(to_char(undinv_fe_liquidacion, 'yyyy')) as año, initcap(to_char(undinv_fe_liquidacion, 'month')) as mes, initcap(to_char(undinv_fe_liquidacion, 'dd')) as dia from INFI_TB_106_UNIDAD_INVERSION where undinv_id=").append(idUnidadInversion);
		
		dataSet = db.get(dataSource, sql.toString());*/
		Date fechaLiquidacion = new Date();	
		StringBuffer sql = new StringBuffer();
		//sql.append("select initcap(to_char(undinv_fe_liquidacion, 'yyyy')) as año, initcap(to_char(undinv_fe_liquidacion, 'month')) as mes, initcap(to_char(undinv_fe_liquidacion, 'dd')) as dia from INFI_TB_106_UNIDAD_INVERSION where undinv_id=").append(idUnidadInversion);
		sql.append("select to_char(undinv_fe_liquidacion, 'dd-MM-yyyy') as fe_liquidacion from INFI_TB_106_UNIDAD_INVERSION where undinv_id=").append(idUnidadInversion);
		
		try {
			conn = dso.getConnection();			
			statement = conn.createStatement();
			resultQuery = statement.executeQuery(sql.toString());			
			
			if(resultQuery.next())
				fechaLiquidacion = resultQuery.getDate("fe_liquidacion");
			
		} catch (Exception e) {
			logger.error(e.getMessage()+ com.bdv.infi.util.Utilitario.stackTraceException(e));
			throw new Exception("Error al buscar fecha de liquidaci&oacute;n de la unidad de inversi&oacute;n");
		} finally {
			if (resultQuery != null){
				resultQuery.close();
				cerrarConexion();
			}
		}
		
		return fechaLiquidacion;
	
    }
	
	
	public String listadescripcion(long idUnidadInversion) throws Exception {
		
		/*StringBuffer sql = new StringBuffer();
		sql.append("select distinct ui.undinv_descripcion from INFI_TB_106_UNIDAD_INVERSION ui, INFI_TB_204_ORDENES o where o.uniinv_id=ui.undinv_id and ui.undinv_id = ");		
		sql.append(idUnidadInversion);
			
		dataSet = db.get(dataSource, sql.toString());*/
		
		String descripcionUI ="";
		StringBuffer sql = new StringBuffer();
		sql.append("select ui.undinv_nombre from INFI_TB_106_UNIDAD_INVERSION ui where ui.undinv_id = ");
		sql.append(idUnidadInversion);
		
		try {
			conn = dso.getConnection();			
			statement = conn.createStatement();
			resultQuery = statement.executeQuery(sql.toString());			
			
			if(resultQuery.next())
				descripcionUI = resultQuery.getString("undinv_nombre");
			else
				throw new Exception("No se encontr&oacute; descripci&oacute;n de la unidad de inversi&oacute;n");
			
		/*} catch (Exception e) {
			throw new Exception("Error al buscar descripci&oacute;n de la unidad de inversi&oacute;n");
		*/} finally {
			if (resultQuery != null){
				resultQuery.close();
				cerrarConexion();
			}
		}
		
		return descripcionUI;

    }


}

package com.bdv.infi.dao;

import javax.sql.DataSource;

import megasoft.db;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;

/**
 * DAO para las tablas de cuentas BDV relacionadas a las transacciones y a ser utilizadas por swift:
 * <b>infi_tb_007</b> e
 * <b>infi_tb_008</b>
 * @author elaucho
 */
public class OfertaDAO extends GenericoDAO{

	/**
	 * Constructor de la clase
	 * @param ds
	 */
	public OfertaDAO(DataSource ds) {
		super(ds);
	}

	@Override
	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**Lista las ordenes de las ofertas de alto valor
	 * @param idUnidadInversion id de la unidad de inversión
	 * @throws Exception en caso de error NM32454 INFI_TTS_491_WEB_SERVICE ALTO VALOR 26/03/2015
	 */
	public void listarOrdenesPorEnviarBCV(Integer incluirCliente, Integer cedRifCliente, Integer tasaMinima, Integer tasaMaxima, Integer montoMinimo, Integer montoMaximo, boolean totales,boolean paginado, int paginaAMostrar, int registroPorPagina, boolean todos, boolean incluir, String ordenesSeleccionadas, String ordeneEstatusBCV, String fecha, String origen, String estatusCruce) throws Exception{
		StringBuilder sql = new StringBuilder();
		boolean fechaNotNull=false;
		boolean estatusBcvNotNull=false;
		String condicionTasa = "";
		String condicionMonto = "";
		String condicionCliente = "";
		String condicionOrigen = "";
		String condicionEstatusCruce = "";
		
		if((tasaMinima != null && tasaMinima != 0)){
			condicionTasa = " AND ORDENE_TASA_CAMBIO >= "+tasaMinima;
		}
		
		if((tasaMaxima != null && tasaMaxima != 0)){
			condicionTasa += " AND ORDENE_TASA_CAMBIO <= "+tasaMaxima;
		}
		
		if((montoMinimo != null && montoMinimo != 0)){
			condicionMonto = " AND ORDENE_MONTO_OFERTADO >= "+montoMinimo;
		}
		
		if((montoMaximo != null && montoMaximo != 0)){
			condicionMonto += " AND ORDENE_MONTO_OFERTADO <= "+montoMaximo;
		}
		
		if((cedRifCliente != null && cedRifCliente != 0)){
			if(incluirCliente.equals(1)){ //SE INCLUYE AL CLIENTE
				condicionCliente = " AND CLIENT_CEDRIF IN ("+cedRifCliente+")";
			}else { //SE EXCLUYE AL CLIENTE
				condicionCliente = " AND CLIENT_CEDRIF NOT IN ("+cedRifCliente+")";
			}
		}
		if((origen != null && !origen.equals(""))){
			condicionOrigen += " AND ORIGEN = "+origen;
		}
		
		if(estatusCruce !=null && !estatusCruce.equals("")){
			condicionEstatusCruce = "AND ESTATUS_CRUCE = '"+estatusCruce+"'";
		}
		
		
		if(!totales){ //NO SE VAN A CONSULTAR LOS TOTALES
			sql.append("SELECT NRO_JORNADA, OBSERVACION, CLIENT_NOMBRE, " )
		    .append(" TIPPER_ID,   ORDENE_TASA_CAMBIO, ORDENE_MONTO_OFERTADO,   " )
		    .append(" ID_OFERTA,   CLIENT_CEDRIF, COD_INSTITUCION,  " )
		    .append(" ORDENE_ESTATUS_BCV, FECHA_PACTO, ORDENE_ID_BCV, DEALNO, ")
		    .append(" ID_OFERTA ORDENE_ID, OBSERVACION ORDENE_OBSERVACION, ")
		    .append(" ORDENE_MONTO_OFERTADO ORDENE_PED_MONTO, ORDENE_TASA_CAMBIO ORDENE_TASA_POOL, ")
		    .append(" 'SIN_UNIDAD' undinv_nombre,  ")
		    .append(" CASE ORDENE_ESTATUS_BCV ")
		    .append("   WHEN 0 THEN 'Sin Verificar' ")
		    .append("   WHEN 1 THEN 'Verificada Aprobada BCV' ")
		    .append("   WHEN 2 THEN 'Verificada Rechazada BCV' ")
		    .append("   WHEN 3 THEN 'Verificada Aprobada Manual' ")
		    .append("   WHEN 4 THEN 'Verificada Rechazada Manual' ")
		    .append("   WHEN 5 THEN 'Anulada BCV' ")
		    .append(" END estatus_string, ESTATUS_CRUCE, ")
		    .append(" CASE ORIGEN ")
		    .append("   WHEN 0 THEN 'Banco de Venezuela' ")
		    .append("   WHEN 1 THEN 'BCV / Interbancario' ")		   
		    .append(" END origen_string, ORIGEN");
		}else { //SE VAN A CONSULTAR LOS TOTALES
			sql.append("SELECT SUM(ORDENE_MONTO_OFERTADO) monto_operacion, COUNT(1) cantidad_operaciones ");
		}
		
		sql.append(" FROM INFI_TB_225_OFERTAS_SIMADI " ); 
	    
		if(fecha!=null || ordeneEstatusBCV!=null || (!todos && ordenesSeleccionadas!=null)){
			sql.append("WHERE ");
		}
		
	    if(fecha!=null && !fecha.equals("")){
	    	fechaNotNull=true;
	    	sql.append("  TO_DATE(FECHA_PACTO,'DD/MM/RRRR') = TO_DATE('").append(fecha).append("', 'DD/MM/RRRR')");
	    }
	    
	    if(ordeneEstatusBCV!=null && !ordeneEstatusBCV.equals("") && !ordeneEstatusBCV.equals("-1") ){
	    	estatusBcvNotNull=true;
	    	if(fechaNotNull){
	    		sql.append(" AND ");
	    	}
	    	sql.append(" ORDENE_ESTATUS_BCV IN (").append(ordeneEstatusBCV).append(") "); //ESTATUS DE LA ORDEN BCV, SIN VERIFICAR, VERIFICADA O RECHZADA
		}
		
		if(!todos){
			if(estatusBcvNotNull || fechaNotNull){
				sql.append(" AND ");
			}
			if(incluir){				
		    	sql.append("  ID_OFERTA in("+ordenesSeleccionadas+")");
		    } else {
		    	sql.append(" ID_OFERTA not in("+ordenesSeleccionadas+")");
		    }
		}
		
		
		//SE AGREGAN LAS CONDICIONES PARA EL MONTO Y LA TASA
		sql.append(condicionMonto);
		sql.append(condicionTasa);
		sql.append(condicionCliente);
		sql.append(condicionOrigen);
		sql.append(condicionEstatusCruce);
		
		 
		if (paginado && paginaAMostrar>0 && registroPorPagina> 0){			
			dataSet = obtenerDataSetPaginado(sql.toString(),paginaAMostrar,registroPorPagina);
		}else{
			dataSet = db.get(dataSource, sql.toString());
		}
		
	}
	
	//NM32454 INFI_TTS_491_WS_BCV ALTO VALOR 26/03/2015
	public void actualizarOrdenBCV(String ordeneID, String ordenBCV, String estatus, String observacion) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE INFI_TB_225_OFERTAS_SIMADI SET ");
		
		if(estatus != null){
			sql.append("ORDENE_ESTATUS_BCV = ").append(estatus);
		}
		
		if(ordenBCV != null){
			sql.append(" ,  ORDENE_ID_BCV = '").append(ordenBCV).append("'");
		}
		
		if(observacion !=null ){
			if(observacion.length() > 1000){
				observacion = observacion.substring(0,999);
			}
			sql.append(" ,  OBSERVACION = '").append(observacion).append("'");
		}
		sql.append(" WHERE ID_OFERTA =").append(ordeneID);
		db.exec(dataSource, sql.toString());
	}
	
	/**
	 * Método para consulta de ofertas SIMADI registradas en OPICS
	 * @author nm25287 30/03/2015
	 * @param fecha
	 * @param producto
	 * @param subProducto
	 * @throws Exception
	 */
	public void listarOrdenesOfertaOPICS(String fecha, String producto,String ...subProducto) throws Exception{
		StringBuilder sql = new StringBuilder();
		StringBuffer subProductos = null;
		sql.append("SELECT TAXID,SN, CCY,CCYAMT,CCYBRATE_8,DEALNO,DEALDATE,INPUTDATE,VDATE,PRODCODE,PRODTYPE FROM FXDH,CUST WHERE BR='").append(ConstantesGenerales.ID_BR_OPICS).append("' AND DEALDATE= TO_DATE('");
		sql.append(fecha).append("','DD-MM-YYYY')");
		sql.append(" AND PRODCODE='").append(producto).append("'");
		if(subProducto!=null && subProducto.length > 0 && subProducto[0]!=null){
			subProductos = new StringBuffer();
			for(int i=0;i<subProducto.length;i++){
				subProductos.append(subProducto[i]).append("','");
			}
			sql.append(" AND PRODTYPE IN ('").append(subProductos.substring(0, subProductos.length()-3)).append("')");
		}
		sql.append(" AND REVDATE IS NULL");
		sql.append(" AND CUST=CUST.CNO AND trim(DEALNO) NOT IN " );
		sql.append(" (SELECT trim(DEALNO) FROM INFI_TB_225_OFERTAS_SIMADI ");
		sql.append("   WHERE FECHA_PACTO=TO_DATE('").append(fecha).append("','DD-MM-YYYY'))");
		
		
		System.out.println("listarOrdenesOfertaOPICS: "+sql.toString());
		dataSet = db.get(dataSource, sql.toString());
	}
	
	/**
	 * Insercion de ofertas OPICS en INFI
	 * @param cedRif
	 * @param tipoDocumento
	 * @param nombreCliente
	 * @param monto
	 * @param tasa
	 * @param numJornada
	 * @param estatusCruce
	 * @param dealNro
	 * @throws Exception
	 */
	public String insertarOrdenBCV(String cedRif, String tipoDocumento,String nombreCliente,String monto, String tasa, String numJornada, String estatusBCV, String dealNro, String fechaPacto, String fechaRegistro, String fechaValor,String producto, String subProducto, String origen, String idOrdenBCV) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO INFI_TB_225_OFERTAS_SIMADI ");
		sql.append("(ID_OFERTA, ORDENE_ESTATUS_BCV, CLIENT_CEDRIF, TIPPER_ID, CLIENT_NOMBRE, ORDENE_MONTO_OFERTADO, ORDENE_TASA_CAMBIO, NRO_JORNADA, DEALNO,FECHA_PACTO,FECHA_REGISTRO,FECHA_VALOR,ID_PRODUCTO,ID_SUB_PRODUCTO,ORIGEN,ORDENE_ID_BCV)");
		sql.append(" VALUES (INFI_SQ_225.nextval,"); //ID_OFERTA
		sql.append(estatusBCV).append(","); //ORDENE_ESTATUS_BCV
		sql.append(cedRif).append(",'"); //CLIENT_CEDRIF
		sql.append(tipoDocumento).append("','"); //TIPPER_ID
		sql.append(nombreCliente).append("',"); //CLIENT_NOMBRE
		sql.append(monto).append(","); //ORDENE_MONTO_OFERTADO
		sql.append(tasa).append(",'"); //ORDENE_TASA_CAMBIO
		sql.append(numJornada).append("','"); //NRO_JORNADA
		//sql.append(estatusCruce).append(","); //ESTATUS_CRUCE
		sql.append(dealNro).append("',"); //DEALNO	
		sql.append("TO_DATE('").append(fechaPacto).append("','YYYY-MM-DD'),"); //FECHA_PACTO	
		sql.append("TO_DATE('").append(fechaRegistro).append("','YYYY-MM-DD'),"); //FECHA_REGISTRO	
		sql.append("TO_DATE('").append(fechaValor).append("','YYYY-MM-DD'),"); //FECHA_VALOR
		sql.append("'").append(producto.trim()).append("',"); //ID_PRODUCTO	
		sql.append("'").append(subProducto.trim()).append("',"); //ID_SUB_PRODUCTO	
		sql.append("'").append(origen.trim()).append("',"); //ORIGEN	
		
		if(idOrdenBCV!=null){
			sql.append("'").append(idOrdenBCV.trim()).append("')"); //ORDENE_ID_BCV
		}else
		{
			sql.append("NULL)");
		}
		//db.exec(dataSource, sql.toString());
		return sql.toString();
	}
	
	
	/**
	 * Metodo de actualizacion de estatus de cruce
	 * @param ordeneID
	 * @param ordenBCV
	 * @param estatus
	 * @param observacion
	 * @throws Exception
	 * @author NM26659
	 *///NM26659 TTS_491 WEB SERVICE ALTO VALOR 01/04/2015
	public String actualizarEstatusCruceOrdenOferta(long idOrdenOferta, String estatus) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE INFI_TB_225_OFERTAS_SIMADI SET ESTATUS_CRUCE ='").append(estatus.toUpperCase()).append("' ");					
		sql.append(" WHERE ID_OFERTA =").append(idOrdenOferta);
		
		return sql.toString();
	}
	

	
	/**
	 * Metodo de actualizacion de estatus de cruce para las ordenes de oferta no procesadas
	 * @return
	 * @throws Exception
	 * @author NM26659
	 */
	public String actualizarOrdenOfertaSinProcesar() throws Exception{
		
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE INFI_TB_225_OFERTAS_SIMADI SET ESTATUS_CRUCE ='").append(ConstantesGenerales.STATUS_NO_CRUZADA).append("' ");					
		sql.append(" WHERE estatus_cruce <> '").append(ConstantesGenerales.STATUS_CRUZADA).append("'");
		sql.append("  AND TO_DATE (fecha_pacto,'").append(ConstantesGenerales.FORMATO_FECHA2).append("') < TO_DATE(SYSDATE,'").append(ConstantesGenerales.FORMATO_FECHA2).append("')");
		
		return sql.toString();
	}
	
	
	public boolean isOfertaBCVRegistrada(String idOfertaBCV) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * FROM INFI_TB_225_OFERTAS_SIMADI");					
		sql.append(" WHERE ORDENE_ID_BCV = '").append(idOfertaBCV).append("'");
		
		dataSet = db.get(dataSource, sql.toString());
		
		if(dataSet.count()>0){
			return true;
		}
		return false;
	}
}//fin clase DAO

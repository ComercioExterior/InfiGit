package com.bdv.infi.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import megasoft.db;

import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;
import com.bdv.infi.logic.interfaces.TransaccionFinanciera;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaz_opics.message.MensajeOpics;
import com.bdv.infi.logic.interfaz_opics.message.MensajeOpicsDetalle;
import com.bdv.infi.logic.interfaz_opics.message.MensajeOpicsOperacionCambio;
import com.bdv.infi.logic.interfaz_opics.message.MensajeOpicsRentaFija;
import com.bdv.infi.util.*;

/**Clase destinada para el manejo de ingreso y recuperación de los datos 
 * correspondientes a un mensaje OPICS*/
public class MensajeOpicsDAO extends GenericoDAO{
	
	private Logger logger = Logger.getLogger(MensajeOpicsDAO.class);
	
	public MensajeOpicsDAO(DataSource ds) {
		super(ds);
	}

	public MensajeOpicsDAO(Transaccion transanccion) throws Exception {
		super(transanccion);
	}

/**Ingresa el mensaje en tabla de opics
 * @param ArrayList<MensajeOpicsDetalle> arrayList de Mensajes
 * @throws lanza una excepción si ocurre un error*/
	public String[] ingresar(MensajeOpics mensajeOpics) throws Exception{
		//System.out.println("INSERT EN 215");
		long secuencia = Long.valueOf(dbGetSequence(this.dataSource, ConstantesGenerales.SECUENCIA_MENSAJE_OPICS)).longValue();
		ArrayList<String> consultas = new ArrayList<String>();
		
		StringBuffer sb = new StringBuffer();
		sb.append("INSERT INTO INFI_TB_215_MENSAJE_OPICS(");
		sb.append("OPICS_ID,IN_ENVIADO,FECHA_CREADO,FECHA_ENVIO,USUARIO_ID,EJECUCION_ID,FECHA_VALOR");
		
		if(mensajeOpics.getIdOrdenInfi()>0){
			sb.append(",ORDENE_ID)");
		} else {
			sb.append(")");
		}
		sb.append(" VALUES(");
		sb.append(secuencia).append(",");
		sb.append(0).append(",");
		sb.append(this.formatearFechaBDActual()).append(",");
		sb.append("NULL").append(",");
		sb.append(mensajeOpics.getIdUsuario()).append(",");
		sb.append(mensajeOpics.getIdEjecucion()==0?"NULL":mensajeOpics.getIdEjecucion()).append(",");
		
		if (mensajeOpics.getFechaValor()== null){
			sb.append("NULL");
		} else {
			sb.append(this.formatearFechaBD(mensajeOpics.getFechaValor()));
		}
		
		if(mensajeOpics.getIdOrdenInfi()>0){
			sb.append(",").append(mensajeOpics.getIdOrdenInfi());
		}
		
		sb.append(")");
		consultas.add(sb.toString());
			//System.out.println("ingresar OPICS " + sb.toString());
		//Id de opics generado
		mensajeOpics.setIdOpics(secuencia);
		guardarDetalles(mensajeOpics,consultas);

		//Prepara el retorno
		String[] retorno = new String[consultas.size()];
		
		//Recorre la lista y crea un string de consultas
		for (int i=0; i < consultas.size(); i++){
			retorno[i] = consultas.get(i).toString();
		}
		
		return retorno;	
	}

	/**Guarda el detalle de los mensajes*/
	private void guardarDetalles(MensajeOpics mensajeOpics, ArrayList<String> consultas) throws Exception{
		//System.out.println(" INGRESA EN METODO guardarDetalles ");
		Iterator<MensajeOpicsDetalle> iterador = mensajeOpics.getMensajesDetalles().iterator();
		MensajeOpicsDetalle mensaje = null;
		int numeroMensaje = 0;
		
		try{
			//Recorre los mensajes detalles
			while (iterador.hasNext()){
				mensaje = iterador.next();			
				LinkedHashMap<String,String> valores = mensaje.getDetalle();
				String clave = "";
				numeroMensaje++;
				
				
				Iterator<String> it = valores.keySet().iterator();
				while (it.hasNext()){		
					//Guarda el detalle de los mensajes
					clave = it.next();
					//Si el campo es vacío no lo almacena en la tabla
	
					if (valores.get(clave).equals(mensaje.VALOR_VACIO)){
						continue;
					}
					StringBuffer sb = new StringBuffer();
					sb.append("INSERT INTO INFI_TB_216_MENSAJE_OPICS_DET (OPICS_ID,MENSAJE_NRO,CLAVE_CAMPO,VALOR_CAMPO) VALUES(");						
					sb.append(mensajeOpics.getIdOpics()).append(",");
					sb.append(numeroMensaje).append(",");			
					sb.append("'").append(clave).append("',");
					sb.append("'").append(valores.get(clave)).append("')");
				//	System.out.println("guardarDetalles " + sb.toString());
					consultas.add(sb.toString());
				}
			}
		} catch (Exception e){
			logger.info("Mensaje---->");
			logger.info(mensaje.toString());
			logger.error("Error en la inserción de archivo OPICS " + e.getMessage());

		}
	}


	@Override
	public Object moveNext() throws Exception {
		StringBuffer sb = new StringBuffer();
		HashMap<String,String> detalles = new HashMap<String,String>();
        Statement st = null;
        ResultSet rs = null;
        MensajeOpics mensajeOpics = null;
        MensajeOpicsDetalle mensajeOpicsDetalle = null;
        int numeroMensaje=1; //Debe comenzar en 1
        try {
            //Si no es último registro arma el objeto
            if ((resultSet != null) && (!resultSet.isAfterLast())) {
            	mensajeOpics = new MensajeOpics();                
                mensajeOpics.setIdEjecucion(resultSet.getLong("ejecucion_id"));
                mensajeOpics.setEnviado(resultSet.getInt("in_enviado")==0?false:true);
                mensajeOpics.setFechaEnvio(resultSet.getDate("fecha_envio"));
                mensajeOpics.setFechaMensaje(resultSet.getDate("fecha_creado"));
                mensajeOpics.setFechaValor(resultSet.getDate("fecha_valor"));
                mensajeOpics.setIdOpics(resultSet.getLong("opics_id"));
                mensajeOpics.setIdUsuario(resultSet.getInt("usuario_id"));                
                
                //Busca el detalle del mensaje y lo carga en un HashMap
                sb.append("SELECT * FROM INFI_TB_216_MENSAJE_OPICS_DET WHERE OPICS_ID=").append(resultSet.getLong("opics_id"));
                sb.append(" ORDER BY MENSAJE_NRO");
                st = this.conn.createStatement();
                rs = st.executeQuery(sb.toString());
                while (rs.next()){
                	if (rs.getInt("mensaje_nro") == numeroMensaje){
                	   detalles.put(rs.getString("clave_campo"), rs.getString("valor_campo"));
                	} else {
                        //Verifica el tipo de mensaje para armar el correcto
                        if (detalles.get(MensajeOpicsDetalle.TIPO_MENSAJE).equals(MensajeOpicsDetalle.RENTA_FIJA)){
                        	mensajeOpicsDetalle = new MensajeOpicsRentaFija();
                        } else {
                        	mensajeOpicsDetalle = new MensajeOpicsOperacionCambio();
                        }
                        mensajeOpicsDetalle.setNumeroMensaje(numeroMensaje);                        
                        mensajeOpicsDetalle.setHashMap(detalles);
                        mensajeOpics.agregarMensajeDetalle(mensajeOpicsDetalle);
                        numeroMensaje = rs.getInt("mensaje_nro");
                        
                        //Reinicia los detalles para el nuevo mensaje
                    	detalles = new HashMap<String,String>();                        
                        detalles.put(rs.getString("clave_campo"), rs.getString("valor_campo"));                        
                	}
                }
                //Verifica el tipo de mensaje para armar el correcto
                if (detalles.get(MensajeOpicsDetalle.TIPO_MENSAJE).equals(MensajeOpicsDetalle.RENTA_FIJA)){
                	mensajeOpicsDetalle = new MensajeOpicsRentaFija();
                } else {
                	mensajeOpicsDetalle = new MensajeOpicsOperacionCambio();
                }
                mensajeOpicsDetalle.setNumeroMensaje(numeroMensaje);
                mensajeOpicsDetalle.setHashMap(detalles);
                mensajeOpics.agregarMensajeDetalle(mensajeOpicsDetalle);
                
                resultSet.next();
            }
        } catch (SQLException e) {
        	logger.error(e.getMessage()+ Utilitario.stackTraceException(e));
            throw new Exception("Error en la búsqueda de detalles del mensaje ");
        } finally{
        	 if (mensajeOpics == null){
        		 this.closeResources();
        	 }
             if (rs != null){
             	rs.close();
             } 
             if (st != null){
             	st.close();
             }       	
        }
        return mensajeOpics;
	}
	
	
	/**Lista los mensajes que deben ser enviados a opics donde la fecha valor sea igual o menor a 
	 * la fecha actual y un vehículo especifico
	 * @return devuelve verdadero en caso de encontrar registros, para obtener el objeto debe invocarse el 
	 * método moveNext*/
	public boolean listarMensajesPorEnviar(long branch) throws Exception{
		boolean bolOk = false;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT(INFI_TB_215_MENSAJE_OPICS.OPICS_ID),INFI_TB_215_MENSAJE_OPICS.*,");
		sql.append("VALOR_CAMPO FROM INFI_TB_215_MENSAJE_OPICS LEFT JOIN INFI_TB_216_MENSAJE_OPICS_DET ON INFI_TB_215_MENSAJE_OPICS.OPICS_ID=");
		sql.append("INFI_TB_216_MENSAJE_OPICS_DET.OPICS_ID WHERE FECHA_CREADO <= SYSDATE AND IN_ENVIADO=0 ");
		sql.append("AND CLAVE_CAMPO='BRANCH' AND VALOR_CAMPO='");
		sql.append(branch);
		sql.append("' ORDER BY INFI_TB_215_MENSAJE_OPICS.OPICS_ID DESC ");
		
		conn = this.dataSource.getConnection();
		statement = conn.createStatement();
		resultSet = statement.executeQuery(sql.toString());
		
        if (resultSet.next()){
            bolOk = true;
        } else {
            super.closeResources();
            if (conn != null){
            	conn.close();
            }
        }
        //System.out.println("SQL mensajes por enviar: "+sql.toString());
        return bolOk;
	}
	
	/**Lista los mensajes que deben ser enviados a opics donde la fecha valor sea igual o menor a 
	 * la fecha actual y un vehículo especifico
	 * @return devuelve verdadero en caso de encontrar registros, para obtener el objeto debe invocarse el 
	 * método moveNext*/
	//Metodo creado en requerimiento TTS_443 SICAD 2 M26659_23/05/2014
	public boolean listarMensajesPorEnviar(String tipoMensaje,String fechaDesde,String fechaHasta) throws Exception{
		boolean bolOk = false;
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT(INFI_TB_215_MENSAJE_OPICS.OPICS_ID),INFI_TB_215_MENSAJE_OPICS.*,");
		sql.append("VALOR_CAMPO FROM INFI_TB_215_MENSAJE_OPICS LEFT JOIN INFI_TB_216_MENSAJE_OPICS_DET ON INFI_TB_215_MENSAJE_OPICS.OPICS_ID=");
		sql.append("INFI_TB_216_MENSAJE_OPICS_DET.OPICS_ID WHERE IN_ENVIADO=0 ");
		if(fechaDesde!=null && !fechaDesde.equals("")){		
			sql.append("AND FECHA_CREADO >= TO_DATE('").append(fechaDesde).append("','").append(com.bancovenezuela.comun.logic.interfaces.ConstantesGenerales.FORMATO_FECHA).append("') ");
		}
		if(fechaHasta!=null && !fechaHasta.equals("")){
			sql.append("AND FECHA_CREADO <= TO_DATE('").append(fechaHasta).append("','").append(com.bancovenezuela.comun.logic.interfaces.ConstantesGenerales.FORMATO_FECHA).append("') ");	
		}
		
		if(tipoMensaje!=null && !tipoMensaje.equals("")){
			sql.append(" AND clave_campo = 'TIPO_MENSAJE' ");
			sql.append(" AND valor_campo = '").append(tipoMensaje).append("' ");	
		}
		
		
		/*sql.append("AND CLAVE_CAMPO='BRANCH' AND VALOR_CAMPO='");
		sql.append(branch);*/
		sql.append(" ORDER BY INFI_TB_215_MENSAJE_OPICS.OPICS_ID DESC ");
	
		//System.out.println(" listarMensajesPorEnviar ---> " + sql.toString());
		
		conn = this.dataSource.getConnection();
		statement = conn.createStatement();
		resultSet = statement.executeQuery(sql.toString());
		
        if (resultSet.next()){
            bolOk = true;
        } else {
            super.closeResources();
            if (conn != null){
            	conn.close();
            }
        }
        //System.out.println("SQL mensajes por enviar: "+sql.toString());
        return bolOk;
	}
	
	/**Método para marcar el mensaje como enviado. Actualiza el indicador de enviado y la fecha de envio
	 * @param idMensaje id del mensaje que se desea marcar como enviado*/
	public String marcarMensajeEnviado(long idOpics){
		StringBuffer sb = new StringBuffer();
		sb.append("UPDATE INFI_TB_215_MENSAJE_OPICS SET IN_ENVIADO=1,FECHA_ENVIO=SYSDATE WHERE OPICS_ID=").append(idOpics);
		return sb.toString();
	}
	
	/**Elimina un mensaje sólo si no ha sido enviado
	 * @param idMensaje id del mensaje que se desea eliminar*/
	public String eliminarMensaje(long idOpics){
		StringBuffer sb = new StringBuffer();
		sb.append("DELETE FROM INFI_TB_215_MENSAJE_OPICS WHERE OPICS_ID=").append(idOpics);
		sb.append(" AND IN_ENVIADO=0");
		return sb.toString();		
	}
	
	/**Establece los valores por defecto de los campos del archivo OPICS según el tipo de mensaje*/
	public void estableceValoresPorDefecto(MensajeOpicsDetalle mensaje) throws Exception{
		HashMap<String,String> valoresPorDefecto = new HashMap<String,String>();
		String transaccion = "";
		if (mensaje instanceof MensajeOpicsOperacionCambio){
			transaccion = MensajeOpicsDetalle.MENSAJE_OPICS_OC;
		} else if (mensaje instanceof MensajeOpicsRentaFija){
			transaccion = MensajeOpicsDetalle.MENSAJE_OPICS_RF;
		}
		//Busca y carga los valores por defecto
		ParametrosDAO parametros = new ParametrosDAO(this.dataSource);
		valoresPorDefecto = parametros.buscarParametros(transaccion);
		mensaje.setHashMap(valoresPorDefecto);
	}
/**
 * Lista los registros de la tabla 215
 * @param long enviado
 * @param Date fechaDesde
 * @param Date fechaHasta
 * @throws Exception
 */	
	public void listar(long enviado,Date fechaDesde,Date fechaHasta)throws Exception{
		
		StringBuffer sb = new StringBuffer();
		SimpleDateFormat formato = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
		sb.append("select infi_tb_204_ordenes.ordene_id,INFI_TB_215_MENSAJE_OPICS.opics_id,case when INFI_TB_215_MENSAJE_OPICS.in_enviado=0 then 'No' when INFI_TB_215_MENSAJE_OPICS.in_enviado=1 then 'Si' end in_enviado,INFI_TB_215_MENSAJE_OPICS.fecha_creado,INFI_TB_215_MENSAJE_OPICS.fecha_envio,(select userid from msc_user where msc_user_id = INFI_TB_215_MENSAJE_OPICS.usuario_id) as usuario_id, INFI_TB_215_MENSAJE_OPICS.ejecucion_id,INFI_TB_215_MENSAJE_OPICS.fecha_valor ");
		sb.append("from INFI_TB_215_MENSAJE_OPICS left join infi_tb_204_ordenes on infi_tb_204_ordenes.ID_OPICS=INFI_TB_215_MENSAJE_OPICS.opics_id ");
		sb.append("where in_enviado =").append(enviado);
		sb.append(" and fecha_creado>=to_date('").append(formato.format(fechaDesde)).append("','").append(ConstantesGenerales.FORMATO_FECHA);
		sb.append("') and fecha_creado<=to_date('").append(formato.format(fechaHasta)).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("') and infi_tb_204_ordenes.ordene_id is not null order by INFI_TB_215_MENSAJE_OPICS.opics_id desc"); 
		//System.out.println("SQL (listar OPICS): "+sb.toString());
		dataSet = db.get(dataSource,sb.toString());
	}
	
	/**
	 * Lista los registros de la tabla 215 //NM25287 SICAD 2. Modificación para registro de id_orden en Deal Opics
	 * @param long enviado
	 * @param Date fechaDesde
	 * @param Date fechaHasta
	 * @throws Exception
	 */	
		public void listarDealsRentaFija(long enviado,Date fechaDesde,Date fechaHasta, String tipoMensaje)throws Exception{
			StringBuffer sb = new StringBuffer();
			SimpleDateFormat formato = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
			sb.append("SELECT MO.ORDENE_ID,MO.OPICS_ID,");
			sb.append(" CASE WHEN MO.IN_ENVIADO = 0 THEN 'No' WHEN MO.IN_ENVIADO = 1 THEN 'Si' END IN_ENVIADO,");
			sb.append(" MO.FECHA_CREADO, MO.FECHA_ENVIO,");
			sb.append(" (SELECT userid FROM msc_user WHERE msc_user_id = MO.usuario_id) AS usuario_id,");
			sb.append(" MO.EJECUCION_ID,MO.FECHA_VALOR"); 
			sb.append(" FROM INFI_TB_215_MENSAJE_OPICS MO,INFI_TB_216_MENSAJE_OPICS_DET MOD");
			sb.append(" WHERE MO.in_enviado = ").append(enviado);
			sb.append(" AND MO.fecha_creado >= TO_DATE ('").append(formato.format(fechaDesde)).append("', 'dd-MM-yyyy')");
			sb.append(" AND MO.fecha_creado <= TO_DATE ('").append(formato.format(fechaHasta)).append("', 'dd-MM-yyyy')"); 
			sb.append(" AND MOD.clave_campo = 'TIPO_MENSAJE'");
			sb.append(" AND MOD.valor_campo = '"+tipoMensaje+"'");
			sb.append(" AND MOD.OPICS_ID = MO.OPICS_ID");
			sb.append(" ORDER BY MO.OPICS_ID"); 
			System.out.println("SQL (listar OPICS Renta Fija): "+sb.toString());
			dataSet = db.get(dataSource,sb.toString());
		}
/**
 * Metodo que convierte el numero de cuenta de 20 dígitos a 12, el cual es un store procedure suministrado por BDV
 * @param idOpics
 * @return
 * @throws Exception en caso de haber inconveniente
 */

	public String convertirCuenta20A12(String numeroCuenta20)throws Exception{
		
		StringBuffer cuenta = new StringBuffer();
		String numeroCuenta12 = "";
		cuenta.append("select convertir_cuenta_20A12(").append(numeroCuenta20).append(")cuenta from dual");
		
		dataSet = db.get(dataSource, cuenta.toString());
		
		if(dataSet.count()>0)
		{
			dataSet.first();
			dataSet.next();
			numeroCuenta12 = dataSet.getValue("cuenta");
		}
			
		return numeroCuenta12;
	
	}
/**
 * Lista los vehículos de todos los mensajes que no hayan sido enviados a OPICS
 * @return Un Arreglo que contiene el id de los vehiculos involucrados
 * @throws Exception
 */
	public String[] listarMensajesPorVehiculo()throws Exception{
		
		ArrayList<String> idVehiculos = new ArrayList<String>();
		
		StringBuffer sql = new StringBuffer();
				
		sql.append("SELECT DISTINCT(VALOR_CAMPO) FROM INFI_TB_215_MENSAJE_OPICS O, INFI_TB_216_MENSAJE_OPICS_DET OD ");
		sql.append("WHERE O.OPICS_ID=OD.OPICS_ID AND FECHA_CREADO <= SYSDATE AND O.IN_ENVIADO=0 AND OD.CLAVE_CAMPO='BRANCH' ");
		
		dataSet = db.get(dataSource,sql.toString());
		
		if(dataSet.count()>0){
			dataSet.first();
			while(dataSet.next()){
				idVehiculos.add(dataSet.getValue("VALOR_CAMPO"));
			}//fin while
		}//fin if
		
		//Retornamos el Arreglo
		return (String[]) idVehiculos.toArray(new String[idVehiculos.size()]);
		
	}
	/**
	 * Lista los detalles de los deal ticket para una orden especifica
	 * @param opicsId
	 * @throws Exception
	 */
	public void listarDetallesDeal(long opicsId)throws Exception{
		
		StringBuffer sql = new StringBuffer();
		//NM25287 SICAD 2. Modificación para registro de id_orden en Deal Opics
		sql.append("select * from INFI_TB_216_MENSAJE_OPICS_DET where opics_id=").append(opicsId);
		sql.append(" order by mensaje_nro");
		//sql.append("select * from INFI_TB_216_MENSAJE_OPICS_DET where (clave_campo='ORDENEID' and valor_campo='");
		//sql.append(ordeneId).append("')");
		
		//Se ejcuta la consulta
		dataSet = db.get(dataSource,sql.toString());	
		
		if(dataSet.count()>0){
			dataSet.first();
			
			StringBuffer deal = new StringBuffer();
			deal.append("select * from INFI_TB_216_MENSAJE_OPICS_DET where opics_id in (");
			
			while(dataSet.next())
			{
				if(dataSet.cursorPos()+1==dataSet.count()){					
					deal.append(dataSet.getValue("opics_id")).append(")");
				}else{
					deal.append(dataSet.getValue("opics_id")).append(",");
				}
			}//FIN WHILE
			
			//Se ordena
			deal.append(" order by mensaje_nro");
			
			//Se ejecuta la consulta
			dataSet = db.get(dataSource,deal.toString());
		}
	}//fin metodo

	/**
	 * Actualiza la fecha valor del mensaje opics cuando el cobro a pasvio se haya hecho efectivo
	 * @param ordenId id de la orden
	 * @return consulta que se debe ejecutar
	 */
	public String actualizarFechaValorMensaje(long ordenId){
		StringBuilder sb = new StringBuilder();
		sb.append("update infi_tb_215_mensaje_opics set fecha_valor=(select ordene_fe_ult_act from infi_tb_204_ordenes where ordene_id=").append(ordenId).append(") where opics_id in(");
		sb.append("   select opics_id from infi_tb_216_mensaje_opics_det where valor_campo in(select ordene_id from infi_tb_204_ordenes where ordene_id_relacion=").append(ordenId).append(") and clave_campo='ORDENEID'");
		sb.append(")");
		return sb.toString();
	}
	
	//Motodo Desarrollado requerimiento SICAD nm26659
	public void listarTipoMensajesOpics()throws Exception {
		
		StringBuffer sql=new StringBuffer();
		
		sql.append("SELECT DISTINCT MB.VALOR_CAMPO AS MENSAJE_OPICS FROM INFI_TB_216_MENSAJE_OPICS_DET MB WHERE MB.CLAVE_CAMPO='TIPO_MENSAJE'");
		dataSet = db.get(dataSource, sql.toString());
		
	}
	
public void listarOperacionesCambio(long enviado,Date fechaDesde,Date fechaHasta,String mensaje)throws Exception{
		
	
		StringBuffer sb = new StringBuffer();
		SimpleDateFormat formato = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
		sb.append("SELECT   ord.ordene_id, mo.opics_id,mo.fecha_creado,mo.fecha_envio, (SELECT userid FROM msc_user WHERE msc_user_id =mo.usuario_id) AS usuario_id,mo.ejecucion_id,mo.fecha_valor,");
		sb.append("DECODE('"+enviado+"','0','NO','SI') in_enviado ");
		sb.append("FROM infi_tb_204_ordenes ord, infi_tb_215_mensaje_opics mo,INFI_TB_216_MENSAJE_OPICS_DET MB WHERE ord.id_opics = mo.OPICS_ID AND mo.opics_id=MB.OPICS_ID ");
		sb.append("AND in_enviado =").append(enviado);
		sb.append(" and fecha_creado>=to_date('").append(formato.format(fechaDesde)).append("','").append(ConstantesGenerales.FORMATO_FECHA);
		sb.append("') and fecha_creado<=to_date('").append(formato.format(fechaHasta)).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("')  AND MB.CLAVE_CAMPO='TIPO_MENSAJE' ");
		sb.append("AND MB.VALOR_CAMPO='").append(mensaje).append("' ");
		sb.append("AND ORD.ORDENE_ID NOT IN (SELECT ORDENE_ID FROM INFI_TB_207_ORDENES_OPERACION OP WHERE OP.ORDENE_ID=ORD.ORDENE_ID AND OP.TRNF_TIPO='").append(TransaccionFinanciera.DEBITO).append("' AND OP.STATUS_OPERACION<>'").append(ConstantesGenerales.STATUS_APLICADA).append("')");
		System.out.println("listarOperacionesCambio -------------> " + sb.toString());
		dataSet = db.get(dataSource,sb.toString());
	}

/**
 * Lista los registros de la tabla 215
 * @param long enviado
 * @param Date fechaDesde
 * @param Date fechaHasta
 * @throws Exception
 */	
	public void listarSICAD2(long enviado,Date fechaDesde,Date fechaHasta)throws Exception{
		
		StringBuffer sb = new StringBuffer();
		SimpleDateFormat formato = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
		sb.append("select '' as ordene_id,INFI_TB_215_MENSAJE_OPICS.opics_id,case when INFI_TB_215_MENSAJE_OPICS.in_enviado=0 then 'No' when INFI_TB_215_MENSAJE_OPICS.in_enviado=1 then 'Si' end in_enviado,INFI_TB_215_MENSAJE_OPICS.fecha_creado,INFI_TB_215_MENSAJE_OPICS.fecha_envio,(select userid from msc_user where msc_user_id = INFI_TB_215_MENSAJE_OPICS.usuario_id) as usuario_id, INFI_TB_215_MENSAJE_OPICS.ejecucion_id,INFI_TB_215_MENSAJE_OPICS.fecha_valor ");
		sb.append("from INFI_TB_215_MENSAJE_OPICS ");
		sb.append("where in_enviado =").append(enviado);
		sb.append(" and fecha_creado>=to_date('").append(formato.format(fechaDesde)).append("','").append(ConstantesGenerales.FORMATO_FECHA);
		sb.append("') and fecha_creado<=to_date('").append(formato.format(fechaHasta)).append("','").append(ConstantesGenerales.FORMATO_FECHA).append("') order by INFI_TB_215_MENSAJE_OPICS.opics_id desc"); 
		//System.out.println("SQL (listarSICAD2 OPICS): "+sb.toString());
		dataSet = db.get(dataSource,sb.toString());
	}
}
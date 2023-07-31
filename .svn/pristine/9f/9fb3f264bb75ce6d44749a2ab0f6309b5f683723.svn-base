package com.bdv.infi.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import megasoft.db;

import com.bdv.infi.data.ConfigSubastaDICOM;
import com.bdv.infi.data.ConfigSubastaORO;
import com.bdv.infi.data.SolicitudDICOM;
import com.bdv.infi.data.SolicitudORO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.EstructuraArchivoOperacionesDICOM;
import com.bdv.infi.logic.interfaces.EstructuraArchivoOperacionesORO;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TransaccionFinanciera;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.util.Utilitario;

public class SolicitudesORODAO extends com.bdv.infi.dao.GenericoDAO {
	
	public SolicitudesORODAO(Transaccion transaccion) throws Exception {
		super(transaccion);
	}
	public SolicitudesORODAO(DataSource ds) {
		super(ds);
	}
	@Override
	public Object moveNext() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}	
	
	public void crearConexion() throws SQLException{
		conn = conn == null ? this.dataSource.getConnection() : conn;
		conn.setAutoCommit(false);
	}
	
	public void registrarSolicitudORO(SolicitudORO solicitudORO, boolean ejecutarCommit) throws Exception{
	
		if (solicitudORO!=null) {
			try {
				//conn = this.dataSource.getConnection();
				conn = conn == null ? this.dataSource.getConnection() : conn;
				conn.setAutoCommit(false);
				CallableStatement procedimientoAlmacenado = conn.prepareCall("{ call PK_INFI_CRUD.SP_CREAR_SOLICITUD_DICOM(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }");
				// cargar parametros al SP
				procedimientoAlmacenado.setString(1, solicitudORO.getIdOperacion()); //  P_NRO_SOLICITUD 
				procedimientoAlmacenado.setString(2, solicitudORO.getTipoOperacion()); //  P_TIPO_OPERACION			
				procedimientoAlmacenado.setString(3, solicitudORO.getTipoCliente()); //  P_TIPO_CLIENTE
				procedimientoAlmacenado.setLong(4, solicitudORO.getCedRifCliente()); //  P_CED_RIF
				procedimientoAlmacenado.setString(5, solicitudORO.getNombreCliente()); //  P_NOMBRE_CLIENTE
				procedimientoAlmacenado.setString(6, solicitudORO.getTelefono()); //  P_TELEFONO_CLIENTE
				procedimientoAlmacenado.setString(7, solicitudORO.getMail());//  P_CORREO_CLIENTE
				procedimientoAlmacenado.setString(8, solicitudORO.getNumCuentaNacional());//  P_NRO_CTA_NACIONAL       
				procedimientoAlmacenado.setString(9, solicitudORO.getNumRetencionCap());//  P_NRO_RET_CAPITAL        
				procedimientoAlmacenado.setString(10, solicitudORO.getHoraBloqueoCap());//  P_HORA_BLOQ              
				procedimientoAlmacenado.setBigDecimal(11, solicitudORO.getMontoOperacionVEF());//  P_MONTO_OP_NACIONAL      
				procedimientoAlmacenado.setString(12, solicitudORO.getDivisaMonNacional());//  P_DIVISA_NACIONAL        
				procedimientoAlmacenado.setString(13, solicitudORO.getNumRetencionCom());//  P_NRO_RET_COMISION       
				procedimientoAlmacenado.setBigDecimal(14, solicitudORO.getMontoComision());//  P_MONTO_COMISION         
				procedimientoAlmacenado.setBigDecimal(15, solicitudORO.getPorcentajeComision());//  P_PORC_COMISION          
				procedimientoAlmacenado.setDate(16, solicitudORO.getFechaOperacionSqlDate());//  P_FECHA_OPERACION        
				procedimientoAlmacenado.setString(17, solicitudORO.getNumCuentaMonedaExtranjera());//  P_NRO_CTA_EXTRANJERA     
				procedimientoAlmacenado.setBigDecimal(18, solicitudORO.getMontoOperacionUSD());//  P_MONTO_OP_EXTRANJERA    
				procedimientoAlmacenado.setString(19, solicitudORO.getDivisaMonExtranjera());//  P_DIVISA_EXTRANJERA      
				procedimientoAlmacenado.setBigDecimal(20, solicitudORO.getTasaCambio());//  P_TASA_CAMBIO            
				procedimientoAlmacenado.setDate(21, solicitudORO.getFechaValorOperacionSqlDate());//  P_FECHA_VALOR_OPERACION  
				procedimientoAlmacenado.setString(22, solicitudORO.getRespuestaArchivo());//  P_CODIGO_RESPUESTA       
				procedimientoAlmacenado.setBigDecimal(23, solicitudORO.getNroOperacionDebito());//  P_NRO_OPE_DEBITO         
				procedimientoAlmacenado.setBigDecimal(24, solicitudORO.getNroOperacionCredito());//  P_NRO_OPE_CREDITO        
				procedimientoAlmacenado.setInt(25, solicitudORO.getEstatusRegistro());//  P_ESTATUS_REGISTRO       
				procedimientoAlmacenado.setInt(26, solicitudORO.getEstatusNotificacionWS());//  P_ESTATUS_NOTIFICACION_WS
				procedimientoAlmacenado.setString(27, solicitudORO.getIdJornada());//  P_ID_JORNADA             
				procedimientoAlmacenado.setLong(28, solicitudORO.getIdUnidadInversion());//  P_UNDINV_ID              

				// ejecutar el SP
				procedimientoAlmacenado.execute();

				// confirmar si se ejecuto sin errores
				if(ejecutarCommit){
System.out.println("Se Ejecuta Commit " + ejecutarCommit );					
					conn.commit();
				}

			} catch (Exception e) {
				if (conn != null) {
					conn.rollback();
				}
System.out.println("Se Hace Rollback y Se genera Exception =" +  e.getMessage() );				
				throw new Exception("Error llamando al stored procedure en registrarSolicitudORO: " + e.getMessage());
			} finally {
				if(ejecutarCommit){
					this.closeResources();
					this.cerrarConexion();
				}
			}
		}		
	}
	
	public void procesarSolicitudORO(ConfigSubastaORO configSubastaORO,SolicitudORO solicitudORO) throws Exception{
		
		if (solicitudORO!=null) {
			try {
				String campos=null;
				//conn = this.dataSource.getConnection();
				conn = conn == null ? this.dataSource.getConnection() : conn;
				conn.setAutoCommit(false);
				CallableStatement procedimientoAlmacenado = conn.prepareCall("{ call PK_INFI_LOGIC_DICOM.SP_ORQUESTADOR(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }");
				// cargar parametros al SP
				
				procedimientoAlmacenado.setLong(1, solicitudORO.getIdUnidadInversion()); //  P_UNDINV_ID
				procedimientoAlmacenado.setString(2, String.valueOf(solicitudORO.getIdOperacion())); //  p_NRO_SOLICITUD
				procedimientoAlmacenado.setString(3, solicitudORO.getTipoCliente()); //p_TIPO_CLIENTE
				procedimientoAlmacenado.setLong(4, solicitudORO.getCedRifCliente()); //p_CED_RIF
				procedimientoAlmacenado.setString(5, solicitudORO.getNombreCliente());//p_NOMBRE_CLIENTE
				
				procedimientoAlmacenado.setString(6, solicitudORO.getTelefono());//p_TELEFONO_CLIENTE
				
				procedimientoAlmacenado.setString(7, solicitudORO.getMail());//p_CORREO_CLIENTE
				
				procedimientoAlmacenado.setString(8, solicitudORO.getNumCuentaNacional());//p_NRO_CTA_NACIONAL
				
				procedimientoAlmacenado.setString(9, solicitudORO.getNumRetencionCap());//p_NRO_RET_CAPITAL
				
				procedimientoAlmacenado.setString(10, solicitudORO.getHoraBloqueoCap());//p_HORA_BLOQ
				
				procedimientoAlmacenado.setBigDecimal(11, solicitudORO.getMontoOperacionVEF());//p_MONTO_OP_NACIONAL
				
				procedimientoAlmacenado.setString(12, solicitudORO.getDivisaMonNacional());//p_DIVISA_NACIONAL
				
				procedimientoAlmacenado.setString(13, solicitudORO.getNumRetencionCom());//p_NRO_RET_COMISION
				
				procedimientoAlmacenado.setBigDecimal(14, solicitudORO.getMontoComision());//p_MONTO_COMISION
				
				procedimientoAlmacenado.setBigDecimal(15, solicitudORO.getPorcentajeComision());//p_PORC_COMISION
				
				procedimientoAlmacenado.setDate(16, solicitudORO.getFechaOperacionSqlDate());//p_FECHA_OPERACION
				
				procedimientoAlmacenado.setString(17, solicitudORO.getNumCuentaMonedaExtranjera());//p_NRO_CTA_EXTRANJERA
				
				procedimientoAlmacenado.setBigDecimal(18, solicitudORO.getMontoOperacionUSD());//p_MONTO_OP_EXTRANJERA
				
				procedimientoAlmacenado.setString(19, solicitudORO.getDivisaMonExtranjera());//p_DIVISA_EXTRANJERA
				
				procedimientoAlmacenado.setBigDecimal(20, solicitudORO.getTasaCambio());//p_TASA_CAMBIO
				
				procedimientoAlmacenado.setDate(21, solicitudORO.getFechaValorOperacionSqlDate());//p_FECHA_VALOR_OPERACION
				
				procedimientoAlmacenado.setInt(22, solicitudORO.getCodigoRespuesta());//p_CODIGO_RESPUESTA
				
				procedimientoAlmacenado.setString(23, solicitudORO.getTipoOperacion());//p_TIPO_OPERACION
				
				procedimientoAlmacenado.setInt(24, solicitudORO.getTipoSolicitud());// p_tipo_solicitud //NM26659 Correccion de Error en pase de parametro 15/06/2017
				
				/**solicitudORO
				 * NM26659 Correccion de Error en pase de parametro 15/06/2017
				 * */
				procedimientoAlmacenado.setBigDecimal(25, solicitudORO.getPtcComisionIGTF());//p_PORC_COMISION_IGTF
				
				procedimientoAlmacenado.setBigDecimal(26, solicitudORO.getMontoComisionIGTF());//p_COMISION_IGTF
				
				procedimientoAlmacenado.setBigDecimal(27, solicitudORO.getMontoTotalRetencion());//p_MONTO_TOTAL_RET
				
				/**
				 * 
				 * */
				
				procedimientoAlmacenado.setString(28, configSubastaORO.getEmpresId());//p_empres_id
				
				procedimientoAlmacenado.setString(29, configSubastaORO.getVehiculoBDVId());//p_bdv_vehiculo_id
				
				procedimientoAlmacenado.setString(30, configSubastaORO.getBlotterId());//p_bloter_id
				
				procedimientoAlmacenado.setString(31, configSubastaORO.getCodOperacionBloCap());//p_cod_op_blo_cap

				procedimientoAlmacenado.setString(32, configSubastaORO.getCodOperacionBloCom());//p_cod_op_blo_com

				procedimientoAlmacenado.setString(33, configSubastaORO.getCodOperacionDebCap());//p_cod_op_deb_cap

				procedimientoAlmacenado.setString(34, configSubastaORO.getCodOperacionCreCap());//p_cod_op_cre_cap

				procedimientoAlmacenado.setString(35, configSubastaORO.getCodOperacionDebCom());//p_cod_op_deb_com

				procedimientoAlmacenado.setString(36, configSubastaORO.getCodOperacionCreConv20());//p_cod_op_cre_conv_20	 

				procedimientoAlmacenado.setString(37, configSubastaORO.getNroJornada());//P_NRO_JORNADA

				procedimientoAlmacenado.setString(38, configSubastaORO.getUsername());//p_username				

				procedimientoAlmacenado.setString(39, configSubastaORO.getEjecucionId());//p_ejecucion_id         

				procedimientoAlmacenado.setInt(40,solicitudORO.getProcesoTipo());//p_TIPO_PROCESO
				campos=""+solicitudORO.getIdUnidadInversion()+",'"+String.valueOf(solicitudORO.getIdOperacion())+"','"+solicitudORO.getTipoCliente()+"',"+solicitudORO.getCedRifCliente()+",'"+solicitudORO.getNombreCliente()+"','"+solicitudORO.getTelefono()+"','"+solicitudORO.getMail()+"','"+solicitudORO.getNumCuentaNacional()+"','"+solicitudORO.getNumRetencionCap()+"','"+solicitudORO.getHoraBloqueoCap()+"',"+solicitudORO.getMontoOperacionVEF()+",'"+solicitudORO.getDivisaMonNacional()+"','"+solicitudORO.getNumRetencionCom()+"',"+solicitudORO.getMontoComision()+","+solicitudORO.getPorcentajeComision()+","+solicitudORO.getFechaOperacionSqlDate()+",'"+solicitudORO.getNumCuentaMonedaExtranjera()+"',"+solicitudORO.getMontoOperacionUSD()+",'"+solicitudORO.getDivisaMonExtranjera()+"',"+solicitudORO.getTasaCambio()+","+solicitudORO.getFechaValorOperacionSqlDate()+","+solicitudORO.getCodigoRespuesta()+",'"+solicitudORO.getTipoOperacion()+"',"+solicitudORO.getTipoSolicitud()+","+solicitudORO.getPtcComisionIGTF()+","+solicitudORO.getMontoComisionIGTF()+","+solicitudORO.getMontoTotalRetencion()+","+configSubastaORO.getEmpresId()+",'"+configSubastaORO.getVehiculoBDVId()+"',"+configSubastaORO.getBlotterId()+",'"+configSubastaORO.getCodOperacionBloCap()+"','"+configSubastaORO.getCodOperacionBloCom()+"','"+configSubastaORO.getCodOperacionDebCap()+"','"+configSubastaORO.getCodOperacionCreCap()+"','"+configSubastaORO.getCodOperacionDebCom()+"','"+configSubastaORO.getCodOperacionCreConv20()+"','"+configSubastaORO.getNroJornada()+"','"+configSubastaORO.getUsername()+"',"+configSubastaORO.getEjecucionId()+"',"+EstructuraArchivoOperacionesORO.PROCESO_VERIFICACION_ORO.getValor();
//		System.out.println("campos ---> " + campos);
				// ejecutar el SP
				procedimientoAlmacenado.execute();
				// confirmar si se ejecuto sin errores
				conn.commit();

			} catch (Exception e) {
				if (conn != null) {
					conn.rollback();
				} 
				//System.out.println("Error llamando al stored procedure en procesarSolicitudORO --> " + e.getMessage());
				e.printStackTrace();
				throw new Exception("Error EN SP_ORQUESTADOR procesarSolicitudORO - Operacion " +solicitudORO.getIdOperacion()+" "+ e.getMessage());
			} finally {
				this.closeResources();
				this.cerrarConexion();
			}
		}		
	}
	

	public long creacionUnidadInvORO(String tipoSolicitud,String jornadaDicom,String usuario,String ip,int tipoProceso) throws Exception{
				long idUnidadInv=0;
				String mensaje=null;
			try {
				ResultSet resp=null;
				//conn = this.dataSource.getConnection();
				conn = conn == null ? this.dataSource.getConnection() : conn;
				conn.setAutoCommit(false);
				CallableStatement procedimientoAlmacenado = conn.prepareCall("{ call PK_INFI_LOGIC_DICOM.CREAR_UNIDAD_INVERSION_DICOM(?,?,?,?,?,?,?) }");
				// cargar parametros al SP
				//procedimientoAlmacenado.setString(1,tipoSolicitud); //  P_TIPO_ORDEN
				procedimientoAlmacenado.setString(1,ConstantesGenerales.SOLICITUD_ORO); //  P_TIPO_ORDEN
				procedimientoAlmacenado.setString(2,jornadaDicom); //  P_NRO_JORNADA				
				procedimientoAlmacenado.setString(3,usuario); 	//P_USUARIO
				procedimientoAlmacenado.setString(4,ip); 		//P_IP
				
			System.out.println("creacionUnidadInvORO jesusquery"+usuario+"-"+ip+"-"+tipoSolicitud+"-"+tipoProceso+"-"+jornadaDicom);
				
				procedimientoAlmacenado.registerOutParameter(5,java.sql.Types.INTEGER);
				procedimientoAlmacenado.registerOutParameter(6,java.sql.Types.VARCHAR);
				procedimientoAlmacenado.setInt(7,tipoProceso); 		//P_TIPO_PROCESO
				
				
				// ejecutar el SP
				
			System.out.println("creacionUnidadInvORO procedimientoAlmacenado "+procedimientoAlmacenado);
				procedimientoAlmacenado.execute();
				
				idUnidadInv=procedimientoAlmacenado.getInt(5);
				mensaje=procedimientoAlmacenado.getString(6);
				
				System.out.println("idUnidadInv---> "+idUnidadInv);
				System.out.println("mensaje---> "+mensaje);
				// confirmar si se ejecuto sin errores
				conn.commit();
				

			} catch (Exception e) {
				if (conn != null) {
					conn.rollback();
				}
				System.out.println(" Error llamando al stored procedure en PK_INFI_DICOM_CARMIN.CREAR_UNIDAD_INVERSION_DICOM: --> " + e.getMessage());
				throw new Exception("Error llamando al stored procedure en PK_INFI_LOGIC_DICOM.CREAR_UNIDAD_INVERSION_DICOM: " + e.getMessage() + " mensaje: " + mensaje);
			} finally {
				
				this.closeResources();
				this.cerrarConexion();
			
			}
			System.out.println("idUnidadInv---> "+idUnidadInv);
			return idUnidadInv;
		}
	
	public void listarSolicitudesVerificadasORO(String nroJornada, String campos,int estatus) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ").append(campos).append(" FROM SOLICITUDES_DICOM SD WHERE SD.ID_JORNADA='").append(nroJornada).append("'");
		sb.append(" AND ESTATUS_REGISTRO=").append(estatus);		

		System.out.println("listarSolicitudesVerificadasORO: "+sb.toString());
		dataSet = db.get(dataSource, sb.toString());		
	}
	
	public SolicitudORO listarTotalesSolicitudesPreaprobadasORO(String nroJornada) throws Exception{
		StringBuffer sb = new StringBuffer();
		SolicitudORO totalesSolicitudesORO=null;
		sb.append("SELECT TIPO_OPERACION,COUNT(NRO_SOLICITUD) AS CANTIDAD_OPERACIONES,NVL(SUM(MONTO_OP_EXTRANJERA),0) AS TOTAL_MONEDA_EXTRANJERA ,NVL(SUM(MONTO_OP_NACIONAL),0) AS TOTAL_MONEDA_NACIONAL");
		sb.append(" FROM SOLICITUDES_DICOM SD WHERE SD.ID_JORNADA='").append(nroJornada).append("'");	
		sb.append(" GROUP BY TIPO_OPERACION");
		
		System.out.println("listarTotalesSolicitudesPreaprobadasDICOM: "+sb.toString());
		dataSet = db.get(dataSource, sb.toString());		
		
		if(dataSet.count()>0){
			dataSet.first();
			totalesSolicitudesORO= new SolicitudORO();
			totalesSolicitudesORO.setIdSubasta(nroJornada);
			totalesSolicitudesORO.setTotalRegistrosDemanda(new BigDecimal(0));
			totalesSolicitudesORO.setTotalMontoBolivaresDemanda(new BigDecimal(0));
			totalesSolicitudesORO.setTotalMontoDolarDemanda(new BigDecimal(0));
			totalesSolicitudesORO.setTotalRegistrosOferta(new BigDecimal(0));
			totalesSolicitudesORO.setTotalMontoBolivaresOferta(new BigDecimal(0));
			totalesSolicitudesORO.setTotalMontoDolarOferta(new BigDecimal(0));
			
			while(dataSet.next()){
				if(dataSet.getValue("TIPO_OPERACION").equals(ConstantesGenerales.SOLICITUD_COMPRA)){
					totalesSolicitudesORO.setTotalRegistrosDemanda(new BigDecimal(dataSet.getValue("CANTIDAD_OPERACIONES")));
					totalesSolicitudesORO.setTotalMontoBolivaresDemanda(new BigDecimal(dataSet.getValue("TOTAL_MONEDA_NACIONAL")));
					totalesSolicitudesORO.setTotalMontoDolarDemanda(new BigDecimal(dataSet.getValue("TOTAL_MONEDA_EXTRANJERA")));
				}else{
					if(dataSet.getValue("TIPO_OPERACION").equals(ConstantesGenerales.SOLICITUD_VENTA)){
						totalesSolicitudesORO.setTotalRegistrosOferta(new BigDecimal(dataSet.getValue("CANTIDAD_OPERACIONES")));
						totalesSolicitudesORO.setTotalMontoBolivaresOferta(new BigDecimal(dataSet.getValue("TOTAL_MONEDA_NACIONAL")));
						totalesSolicitudesORO.setTotalMontoDolarOferta(new BigDecimal(dataSet.getValue("TOTAL_MONEDA_EXTRANJERA")));
					}
				}
			}
		}
		return totalesSolicitudesORO;
	}
	
	public void actualizarEstatusRegistroSolicitudes(String nroJornada,int estatusInicial, int estatusNuevo) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE SOLICITUDES_DICOM SET ESTATUS_REGISTRO=").append(estatusNuevo).append(" WHERE ID_JORNADA='").append(nroJornada).append("' AND ESTATUS_REGISTRO=").append(estatusInicial);
		
		System.out.println("actualizarEstatusRegistroSolicitudes: "+sql.toString());
		db.exec(dataSource, sql.toString());
	}	
	
	public void listarSolicitudesLiquidadasORO(String nroJornada, String campos,int estatus,boolean opLiquidadas) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ").append(campos).append(" FROM SOLICITUDES_DICOM SD WHERE SD.ID_JORNADA='").append(nroJornada).append("'");
		sb.append(" AND ESTATUS_REGISTRO=").append(estatus);
		sb.append(" AND CODIGO_RESPUESTA=").append(0);	
		if(opLiquidadas){
			sb.append(" AND SD.NRO_OPE_CREDITO IN ");	
			sb.append(" (SELECT O.ORDENE_ID ");
			sb.append(" FROM INFI_TB_204_ORDENES O, INFI_TB_207_ORDENES_OPERACION OP");
			sb.append(" WHERE O.ORDENE_ID = SD.NRO_OPE_CREDITO");
			sb.append(" AND  O.ORDENE_ID=OP.ORDENE_ID");
			sb.append(" AND O.ORDSTA_ID ='").append(StatusOrden.PROCESADA).append("'");
			sb.append(" AND O.TRANSA_ID ='").append(TransaccionNegocio.ORDEN_PAGO).append("'");
			sb.append(" AND OP.STATUS_OPERACION ='").append(ConstantesGenerales.STATUS_APLICADA).append("'");
			sb.append(" AND OP.TRNF_TIPO ='").append(TransaccionFinanciera.CREDITO).append("')");
		}		
		System.out.println("listarSolicitudesLiquidadasDICOM: "+sb.toString());
		dataSet = db.get(dataSource, sb.toString());		
	}
	
	public void listarSolicitudesOROPorId(String correlativo) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM SOLICITUDES_DICOM SD WHERE SD.NRO_SOLICITUD='").append(correlativo).append("'");
						
		System.out.println("listarSolicitudesDICOMPorId: "+sb.toString());
		dataSet = db.get(dataSource, sb.toString());		
	}
	
	public SolicitudORO busquedaSolicitudesOROPorId(String idOperacion) throws Exception{
		SolicitudORO solicitudORO=null; 
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM SOLICITUDES_DICOM SD WHERE SD.NRO_SOLICITUD='").append(idOperacion).append("'");
						
		System.out.println("listarSolicitudesOROPorId: "+sb.toString());
		dataSet = db.get(dataSource, sb.toString());
		
		if(dataSet.count()>0){
			dataSet.first();
			dataSet.next();
			solicitudORO=new SolicitudORO();
			
			
			solicitudORO.setIdOperacion(idOperacion);
			solicitudORO.setTipoOperacion(dataSet.getValue("TIPO_OPERACION"));
			solicitudORO.setIdUnidadInversion(Long.parseLong(dataSet.getValue("UNDINV_ID")));								
			solicitudORO.setMontoOperacionVEF(new BigDecimal(dataSet.getValue("MONTO_OP_NACIONAL")));
			//solicitudDICOM.setDivisaMonNacional(new BigDecimal(dataSet.getValue("MONTO_OP_NACIONAL")));
			solicitudORO.setMontoComision(new BigDecimal(dataSet.getValue("MONTO_COMISION")));
						
			solicitudORO.setPorcentajeComision(new BigDecimal(dataSet.getValue("PORC_COMISION")));						
			solicitudORO.setMontoOperacionUSD(new BigDecimal(dataSet.getValue("MONTO_OP_EXTRANJERA")));
			solicitudORO.setPtcComisionIGTF(dataSet.getValue("PORC_COMISION_IGTF"));//TODO AGREGAR CAMPO PARA % COMISION IGTF EN TABLA SOLICITUDES_DICOM
			solicitudORO.setMontoComisionIGTF(dataSet.getValue("COMISION_IGTF"));//TODO AGREGAR CAMPO PARA MONTO COMISION IGTF EN TABLA SOLICITUDES_DICOM
			//solicitudDICOM.setTasaCambio(formatoArchivoRecepcion.getTasaCambio());
				
			
		}
		
		return solicitudORO;
	}
}   

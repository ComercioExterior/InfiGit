package com.bdv.infi.dao;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import megasoft.db;

import com.bdv.infi.data.ConfigSubastaDICOM;
import com.bdv.infi.data.SolicitudDICOM;
import com.bdv.infi.data.SolicitudORO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.EstructuraArchivoOperacionesDICOM;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TransaccionFinanciera;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.util.Utilitario;

public class SolicitudesDICOMDAO extends com.bdv.infi.dao.GenericoDAO {
	
	public SolicitudesDICOMDAO(Transaccion transaccion) throws Exception {
		super(transaccion);
	}
	public SolicitudesDICOMDAO(DataSource ds) {
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
	
	public void registrarSolicitudDICOM(SolicitudDICOM solicitudDicom, boolean ejecutarCommit) throws Exception{
	
		if (solicitudDicom!=null) {
			try {
				System.out.println("paso por el procedimiento");
				System.out.println("solicitudDicom.getCodMoneda()--->"+solicitudDicom.getCodMoneda());
				//conn = this.dataSource.getConnection();
				conn = conn == null ? this.dataSource.getConnection() : conn;
				conn.setAutoCommit(false);
				CallableStatement procedimientoAlmacenado = conn.prepareCall("{ call PK_INFI_CRUD.SP_CREAR_SOLICITUD_DICOM(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }");
				// cargar parametros al SP
				procedimientoAlmacenado.setString(1, solicitudDicom.getIdOperacion()); //  P_NRO_SOLICITUD 
				procedimientoAlmacenado.setString(2, solicitudDicom.getTipoOperacion()); //  P_TIPO_OPERACION			
				procedimientoAlmacenado.setString(3, solicitudDicom.getTipoCliente()); //  P_TIPO_CLIENTE
				procedimientoAlmacenado.setLong(4, solicitudDicom.getCedRifCliente()); //  P_CED_RIF
				procedimientoAlmacenado.setString(5, solicitudDicom.getNombreCliente()); //  P_NOMBRE_CLIENTE
				procedimientoAlmacenado.setString(6, solicitudDicom.getTelefono()); //  P_TELEFONO_CLIENTE
				procedimientoAlmacenado.setString(7, solicitudDicom.getMail());//  P_CORREO_CLIENTE
				procedimientoAlmacenado.setString(8, solicitudDicom.getNumCuentaNacional());//  P_NRO_CTA_NACIONAL       
				procedimientoAlmacenado.setString(9, solicitudDicom.getNumRetencionCap());//  P_NRO_RET_CAPITAL        
				procedimientoAlmacenado.setString(10, solicitudDicom.getHoraBloqueoCap());//  P_HORA_BLOQ              
				procedimientoAlmacenado.setBigDecimal(11, solicitudDicom.getMontoOperacionVEF());//  P_MONTO_OP_NACIONAL      
				procedimientoAlmacenado.setString(12, solicitudDicom.getDivisaMonNacional());//  P_DIVISA_NACIONAL        
				procedimientoAlmacenado.setString(13, solicitudDicom.getNumRetencionCom());//  P_NRO_RET_COMISION       
				procedimientoAlmacenado.setBigDecimal(14, solicitudDicom.getMontoComision());//  P_MONTO_COMISION         
				procedimientoAlmacenado.setBigDecimal(15, solicitudDicom.getPorcentajeComision());//  P_PORC_COMISION          
				procedimientoAlmacenado.setDate(16, solicitudDicom.getFechaOperacionSqlDate());//  P_FECHA_OPERACION        
				procedimientoAlmacenado.setString(17, solicitudDicom.getNumCuentaMonedaExtranjera());//  P_NRO_CTA_EXTRANJERA     
				procedimientoAlmacenado.setBigDecimal(18, solicitudDicom.getMontoOperacionUSD());//  P_MONTO_OP_EXTRANJERA    
				procedimientoAlmacenado.setString(19, solicitudDicom.getDivisaMonExtranjera());//  P_DIVISA_EXTRANJERA      
				procedimientoAlmacenado.setBigDecimal(20, solicitudDicom.getTasaCambio());//  P_TASA_CAMBIO            
				procedimientoAlmacenado.setDate(21, solicitudDicom.getFechaValorOperacionSqlDate());//  P_FECHA_VALOR_OPERACION  
				procedimientoAlmacenado.setString(22, solicitudDicom.getRespuestaArchivo());//  P_CODIGO_RESPUESTA       
				procedimientoAlmacenado.setBigDecimal(23, solicitudDicom.getNroOperacionDebito());//  P_NRO_OPE_DEBITO         
				procedimientoAlmacenado.setBigDecimal(24, solicitudDicom.getNroOperacionCredito());//  P_NRO_OPE_CREDITO        
				procedimientoAlmacenado.setInt(25, solicitudDicom.getEstatusRegistro());//  P_ESTATUS_REGISTRO       
				procedimientoAlmacenado.setInt(26, solicitudDicom.getEstatusNotificacionWS());//  P_ESTATUS_NOTIFICACION_WS
				procedimientoAlmacenado.setString(27, solicitudDicom.getIdJornada());//  P_ID_JORNADA             
				procedimientoAlmacenado.setLong(28, solicitudDicom.getIdUnidadInversion());//  P_UNDINV_ID              
				procedimientoAlmacenado.setString(29,solicitudDicom.getCodMoneda());//CODIGO_MONEDA
				System.out.println("solicitudDicom.getCodMoneda()--->"+solicitudDicom.getCodMoneda());
				// ejecutar el SP
				procedimientoAlmacenado.execute();
				System.out.println("ejecutado procedimiento");
				// confirmar si se ejecuto sin errores
				if(ejecutarCommit){
					conn.commit();
				}

			} catch (Exception e) {
				if (conn != null) {
					conn.rollback();
				}
				throw new Exception("Error llamando al stored procedure en registrarSolicitudDICOM: " + e.getMessage());
			} finally {
				if(ejecutarCommit){
					this.closeResources();
					this.cerrarConexion();
				}
			}
		}		
	}
	
	public void procesarSolicitudDICOM(ConfigSubastaDICOM configSubastaDicom,SolicitudDICOM solicitudDicom) throws Exception{
		
		if (solicitudDicom!=null) {
			try {
				String campos=null;
				//conn = this.dataSource.getConnection();
				conn = conn == null ? this.dataSource.getConnection() : conn;
				conn.setAutoCommit(false);
				CallableStatement procedimientoAlmacenado = conn.prepareCall("{ call PK_INFI_LOGIC_DICOM.SP_ORQUESTADOR(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }");
				// cargar parametros al SP
				
				procedimientoAlmacenado.setLong(1, solicitudDicom.getIdUnidadInversion()); //  P_UNDINV_ID
				System.out.println("1 solicitudDicom.getIdUnidadInversion() --> " + solicitudDicom.getIdUnidadInversion());
				procedimientoAlmacenado.setString(2, String.valueOf(solicitudDicom.getIdOperacion())); //  p_NRO_SOLICITUD
				System.out.println("2 solicitudDicom.getIdOperacion() --> " + solicitudDicom.getIdOperacion());
				procedimientoAlmacenado.setString(3, solicitudDicom.getTipoCliente()); //p_TIPO_CLIENTE
				System.out.println("3 solicitudDicom.getTipoCliente() --> " + solicitudDicom.getTipoCliente());
				procedimientoAlmacenado.setLong(4, solicitudDicom.getCedRifCliente()); //p_CED_RIF
				System.out.println("4 solicitudDicom.getCedRifCliente() --> " + solicitudDicom.getCedRifCliente());
				procedimientoAlmacenado.setString(5, solicitudDicom.getNombreCliente());//p_NOMBRE_CLIENTE
				System.out.println("5 solicitudDicom.getNombreCliente() --> " + solicitudDicom.getNombreCliente());
				procedimientoAlmacenado.setString(6, solicitudDicom.getTelefono());//p_TELEFONO_CLIENTE
				System.out.println("6 solicitudDicom.getTelefono() --> " + solicitudDicom.getTelefono());
				procedimientoAlmacenado.setString(7, solicitudDicom.getMail());//p_CORREO_CLIENTE
				System.out.println("7 solicitudDicom.getMail() --> " + solicitudDicom.getMail());
				procedimientoAlmacenado.setString(8, solicitudDicom.getNumCuentaNacional());//p_NRO_CTA_NACIONAL
				System.out.println("8 solicitudDicom.getNumCuentaNacional() --> " + solicitudDicom.getNumCuentaNacional());
				procedimientoAlmacenado.setString(9, solicitudDicom.getNumRetencionCap());//p_NRO_RET_CAPITAL
				System.out.println("9 solicitudDicom.getNumRetencionCap() --> " + solicitudDicom.getNumRetencionCap());
				procedimientoAlmacenado.setString(10, solicitudDicom.getHoraBloqueoCap());//p_HORA_BLOQ
				System.out.println("10 solicitudDicom.getHoraBloqueoCap() --> " + solicitudDicom.getHoraBloqueoCap());
				procedimientoAlmacenado.setBigDecimal(11, solicitudDicom.getMontoOperacionVEF());//p_MONTO_OP_NACIONAL
				System.out.println(" 11 solicitudDicom.getMontoOperacionVEF() --> " + solicitudDicom.getMontoOperacionVEF());
				procedimientoAlmacenado.setString(12, solicitudDicom.getDivisaMonNacional());//p_DIVISA_NACIONAL
				System.out.println("12 solicitudDicom.getDivisaMonNacional() --> " + solicitudDicom.getDivisaMonNacional());
				procedimientoAlmacenado.setString(13, solicitudDicom.getNumRetencionCom());//p_NRO_RET_COMISION
				System.out.println("13 solicitudDicom.getNumRetencionCom() --> " + solicitudDicom.getNumRetencionCom());
				procedimientoAlmacenado.setBigDecimal(14, solicitudDicom.getMontoComision());//p_MONTO_COMISION
				System.out.println("14 solicitudDicom.getMontoComision() --> " + solicitudDicom.getMontoComision());
				procedimientoAlmacenado.setBigDecimal(15, solicitudDicom.getPorcentajeComision());//p_PORC_COMISION
				System.out.println("15 solicitudDicom.getPorcentajeComision() --> " + solicitudDicom.getPorcentajeComision());
				procedimientoAlmacenado.setDate(16, solicitudDicom.getFechaOperacionSqlDate());//p_FECHA_OPERACION
				System.out.println("16 solicitudDicom.getFechaOperacionSqlDate() --> " + solicitudDicom.getFechaOperacionSqlDate());
				procedimientoAlmacenado.setString(17, solicitudDicom.getNumCuentaMonedaExtranjera());//p_NRO_CTA_EXTRANJERA
				System.out.println("17 solicitudDicom.getNumCuentaMonedaExtranjera() --> "+ solicitudDicom.getNumCuentaMonedaExtranjera());
				procedimientoAlmacenado.setBigDecimal(18, solicitudDicom.getMontoOperacionUSD());//p_MONTO_OP_EXTRANJERA
				System.out.println("18 solicitudDicom.getMontoOperacionUSD() --> " + solicitudDicom.getMontoOperacionUSD());
				procedimientoAlmacenado.setString(19, solicitudDicom.getDivisaMonExtranjera());//p_DIVISA_EXTRANJERA
				System.out.println("19 solicitudDicom.getDivisaMonExtranjera() --> " + solicitudDicom.getDivisaMonExtranjera());
				procedimientoAlmacenado.setBigDecimal(20, solicitudDicom.getTasaCambio());//p_TASA_CAMBIO
				System.out.println("20 solicitudDicom.getTasaCambio() --> " + solicitudDicom.getTasaCambio());
				procedimientoAlmacenado.setDate(21, solicitudDicom.getFechaValorOperacionSqlDate());//p_FECHA_VALOR_OPERACION
				System.out.println("21 solicitudDicom.getFechaValorOperacionSqlDate() --> " +  solicitudDicom.getFechaValorOperacionSqlDate());
				procedimientoAlmacenado.setInt(22, solicitudDicom.getCodigoRespuesta());//p_CODIGO_RESPUESTA
				System.out.println("22 solicitudDicom.getCodigoRespuesta() --> " + solicitudDicom.getCodigoRespuesta());
				procedimientoAlmacenado.setString(23, solicitudDicom.getTipoOperacion());//p_TIPO_OPERACION
				System.out.println("23 solicitudDicom.getTipoOperacion() --> " + solicitudDicom.getTipoOperacion());
				procedimientoAlmacenado.setInt(24, solicitudDicom.getTipoSolicitud());// p_tipo_solicitud //NM26659 Correccion de Error en pase de parametro 15/06/2017
				System.out.println("24 solicitudDicom.getTipoSolicitud() --> " + solicitudDicom.getTipoSolicitud());
				/**
				 * NM26659 Correccion de Error en pase de parametro 15/06/2017
				 * */
				procedimientoAlmacenado.setBigDecimal(25, solicitudDicom.getPtcComisionIGTF());//p_PORC_COMISION_IGTF
				System.out.println("25 solicitudDicom.getPtcComisionIGTF() --> " + solicitudDicom.getPtcComisionIGTF());
				procedimientoAlmacenado.setBigDecimal(26, solicitudDicom.getMontoComisionIGTF());//p_COMISION_IGTF
				System.out.println("26 solicitudDicom.getMontoComisionIGTF() --> " + solicitudDicom.getMontoComisionIGTF());
				procedimientoAlmacenado.setBigDecimal(27, solicitudDicom.getMontoTotalRetencion());//p_MONTO_TOTAL_RET
				System.out.println("27 solicitudDicom.getMontoTotalRetencion() --> " + solicitudDicom.getMontoTotalRetencion());
				/**
				 * 
				 * */
				
				procedimientoAlmacenado.setString(28, configSubastaDicom.getEmpresId());//p_empres_id
				System.out.println("28 configSubastaDicom.getEmpresId() --> " + configSubastaDicom.getEmpresId());
				procedimientoAlmacenado.setString(29, configSubastaDicom.getVehiculoBDVId());//p_bdv_vehiculo_id
				System.out.println("29 configSubastaDicom.getVehiculoBDVId() --> " + configSubastaDicom.getVehiculoBDVId());
				procedimientoAlmacenado.setString(30, configSubastaDicom.getBlotterId());//p_bloter_id
				System.out.println("30 configSubastaDicom.getBlotterId() --> " + configSubastaDicom.getBlotterId());
				procedimientoAlmacenado.setString(31, configSubastaDicom.getCodOperacionBloCap());//p_cod_op_blo_cap
				System.out.println("31 configSubastaDicom.getCodOperacionBloCap() --> " + configSubastaDicom.getCodOperacionBloCap());
				procedimientoAlmacenado.setString(32, configSubastaDicom.getCodOperacionBloCom());//p_cod_op_blo_com
				System.out.println("32 configSubastaDicom.getCodOperacionBloCom() --> " + configSubastaDicom.getCodOperacionBloCom());
				procedimientoAlmacenado.setString(33, configSubastaDicom.getCodOperacionDebCap());//p_cod_op_deb_cap
				System.out.println("33 configSubastaDicom.getCodOperacionDebCap() --> "+ configSubastaDicom.getCodOperacionDebCap());
				procedimientoAlmacenado.setString(34, configSubastaDicom.getCodOperacionCreCap());//p_cod_op_cre_cap
				System.out.println("34 configSubastaDicom.getCodOperacionCreCap() --> " + configSubastaDicom.getCodOperacionCreCap());
				procedimientoAlmacenado.setString(35, configSubastaDicom.getCodOperacionDebCom());//p_cod_op_deb_com
				System.out.println("35 configSubastaDicom.getCodOperacionDebCom() --> " + configSubastaDicom.getCodOperacionDebCom());
				procedimientoAlmacenado.setString(36, configSubastaDicom.getCodOperacionCreConv20());//p_cod_op_cre_conv_20	 
				System.out.println("36 configSubastaDicom.getCodOperacionCreConv20() --> " + configSubastaDicom.getCodOperacionCreConv20());
				procedimientoAlmacenado.setString(37, configSubastaDicom.getNroJornada());//P_NRO_JORNADA
				System.out.println("37 configSubastaDicom.getNroJornada() --> " + configSubastaDicom.getNroJornada());
				procedimientoAlmacenado.setString(38, configSubastaDicom.getUsername());//p_username				
				System.out.println("38 configSubastaDicom.getUsername() --> " + configSubastaDicom.getUsername());
				procedimientoAlmacenado.setString(39, configSubastaDicom.getEjecucionId());//p_ejecucion_id         
				System.out.println("configSubastaDicom.getEjecucionId() --> " + configSubastaDicom.getEjecucionId());
				procedimientoAlmacenado.setInt(40,solicitudDicom.getProcesoTipo());//p_TIPO_PROCESO
				
				/*procedimientoAlmacenado.setString(41,solicitudDicom.getCodMoneda());//p_TIPO_PROCESO
				System.out.println("configSubastaDicom.getCodMoneda() --> " + configSubastaDicom.getCod_Moneda());*/
				
				System.out.println("EstructuraArchivoOperacionesDICOM.PROCESO_VERIFICACION_DICOM.getValor() --> " + EstructuraArchivoOperacionesDICOM.PROCESO_VERIFICACION_DICOM.getValor());
				
				campos=""+solicitudDicom.getIdUnidadInversion()+",'"+String.valueOf(solicitudDicom.getIdOperacion())+"','"+solicitudDicom.getTipoCliente()+"',"+solicitudDicom.getCedRifCliente()+",'"+solicitudDicom.getNombreCliente()+"','"+solicitudDicom.getTelefono()+"','"+solicitudDicom.getMail()+"','"+solicitudDicom.getNumCuentaNacional()+"','"+solicitudDicom.getNumRetencionCap()+"','"+solicitudDicom.getHoraBloqueoCap()+"',"+solicitudDicom.getMontoOperacionVEF()+",'"+solicitudDicom.getDivisaMonNacional()+"','"+solicitudDicom.getNumRetencionCom()+"',"+solicitudDicom.getMontoComision()+","+solicitudDicom.getPorcentajeComision()+","+solicitudDicom.getFechaOperacionSqlDate()+",'"+solicitudDicom.getNumCuentaMonedaExtranjera()+"',"+solicitudDicom.getMontoOperacionUSD()+",'"+solicitudDicom.getDivisaMonExtranjera()+"',"+solicitudDicom.getTasaCambio()+","+solicitudDicom.getFechaValorOperacionSqlDate()+","+solicitudDicom.getCodigoRespuesta()+",'"+solicitudDicom.getTipoOperacion()+"',"+solicitudDicom.getTipoSolicitud()+","+solicitudDicom.getPtcComisionIGTF()+","+solicitudDicom.getMontoComisionIGTF()+","+solicitudDicom.getMontoTotalRetencion()+","+configSubastaDicom.getEmpresId()+",'"+configSubastaDicom.getVehiculoBDVId()+"',"+configSubastaDicom.getBlotterId()+",'"+configSubastaDicom.getCodOperacionBloCap()+"','"+configSubastaDicom.getCodOperacionBloCom()+"','"+configSubastaDicom.getCodOperacionDebCap()+"','"+configSubastaDicom.getCodOperacionCreCap()+"','"+configSubastaDicom.getCodOperacionDebCom()+"','"+configSubastaDicom.getCodOperacionCreConv20()+"','"+configSubastaDicom.getNroJornada()+"','"+configSubastaDicom.getUsername()+"',"+configSubastaDicom.getEjecucionId()+"',"+EstructuraArchivoOperacionesDICOM.PROCESO_VERIFICACION_DICOM.getValor();
				System.out.println("campos ---> " + campos);
				// ejecutar el SP
				procedimientoAlmacenado.execute();
				// confirmar si se ejecuto sin errores
				conn.commit();

			} catch (Exception e) {
				if (conn != null) {
					conn.rollback();
				} 
				//System.out.println("Error llamando al stored procedure en procesarSolicitudDICOM --> " + e.getMessage());
				e.printStackTrace();
				throw new Exception("Error llamando al stored procedure en procesarSolicitudDICOM - Operacion " +solicitudDicom.getIdOperacion()+" "+ e.getMessage());
			} finally {
				this.closeResources();
				this.cerrarConexion();
			}
		}		
	}
	

	public long creacionUnidadInvDICOM(String tipoSolicitud,String jornadaDicom,String usuario,String ip,int tipoProceso) throws Exception{
				long idUnidadInv=0;
				String mensaje=null;
			try {
				ResultSet resp=null;
				//conn = this.dataSource.getConnection();
				conn = conn == null ? this.dataSource.getConnection() : conn;
				conn.setAutoCommit(false);
				CallableStatement procedimientoAlmacenado = conn.prepareCall("{ call PK_INFI_LOGIC_DICOM.CREAR_UNIDAD_INVERSION_DICOM(?,?,?,?,?,?,?) }");
				// cargar parametros al SP
				procedimientoAlmacenado.setString(1,tipoSolicitud); //  P_TIPO_ORDEN
				procedimientoAlmacenado.setString(2,jornadaDicom); //  P_NRO_JORNADA				
				procedimientoAlmacenado.setString(3,usuario); 	//P_USUARIO
				procedimientoAlmacenado.setString(4,ip); 		//P_IP
				
				
				
				procedimientoAlmacenado.registerOutParameter(5,java.sql.Types.INTEGER);
				procedimientoAlmacenado.registerOutParameter(6,java.sql.Types.VARCHAR);
				procedimientoAlmacenado.setInt(7,tipoProceso); 		//P_TIPO_PROCESO
				
				
				// ejecutar el SP
				procedimientoAlmacenado.execute();
				
				idUnidadInv=procedimientoAlmacenado.getInt(5);
				mensaje=procedimientoAlmacenado.getString(6);
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
			return idUnidadInv;
		}
	
	public void listarSolicitudesVerificadasDICOM(String nroJornada, String campos,String moneda,int estatus) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT ").append(campos).append(" FROM SOLICITUDES_DICOM SD WHERE SD.ID_JORNADA='").append(nroJornada).append("'");
		sb.append(" AND MONEDA_JORNADA=").append(moneda);	
		sb.append(" AND ESTATUS_REGISTRO=").append(estatus);		

		System.out.println("listarSolicitudesVerificadasDICOM: "+sb.toString());
		dataSet = db.get(dataSource, sb.toString());		
	}
	
	public SolicitudDICOM listarTotalesSolicitudesPreaprobadasDICOM(String nroJornada,String moneda) throws Exception{
		StringBuffer sb = new StringBuffer();
		SolicitudDICOM totalesSolicitudesDICOM=null;
		sb.append("SELECT TIPO_OPERACION,COUNT(NRO_SOLICITUD) AS CANTIDAD_OPERACIONES,NVL(SUM(MONTO_OP_EXTRANJERA),0) AS TOTAL_MONEDA_EXTRANJERA ,NVL(SUM(MONTO_OP_NACIONAL),0) AS TOTAL_MONEDA_NACIONAL");
		sb.append(" FROM SOLICITUDES_DICOM SD WHERE SD.ID_JORNADA='").append(nroJornada).append("'");	
		sb.append("AND SD.MONEDA_JORNADA='").append(moneda).append("'");
		sb.append(" GROUP BY TIPO_OPERACION");
		
		System.out.println("listarTotalesSolicitudesPreaprobadasDICOM: "+sb.toString());
		dataSet = db.get(dataSource, sb.toString());		
		
		if(dataSet.count()>0){
			dataSet.first();
			totalesSolicitudesDICOM= new SolicitudDICOM();
			totalesSolicitudesDICOM.setIdSubasta(nroJornada);
			totalesSolicitudesDICOM.setTotalRegistrosDemanda(new BigDecimal(0));
			totalesSolicitudesDICOM.setTotalMontoBolivaresDemanda(new BigDecimal(0));
			totalesSolicitudesDICOM.setTotalMontoDolarDemanda(new BigDecimal(0));
			totalesSolicitudesDICOM.setTotalRegistrosOferta(new BigDecimal(0));
			totalesSolicitudesDICOM.setTotalMontoBolivaresOferta(new BigDecimal(0));
			totalesSolicitudesDICOM.setTotalMontoDolarOferta(new BigDecimal(0));
			
			while(dataSet.next()){
				if(dataSet.getValue("TIPO_OPERACION").equals(ConstantesGenerales.SOLICITUD_COMPRA)){
					totalesSolicitudesDICOM.setTotalRegistrosDemanda(new BigDecimal(dataSet.getValue("CANTIDAD_OPERACIONES")));
					totalesSolicitudesDICOM.setTotalMontoBolivaresDemanda(new BigDecimal(dataSet.getValue("TOTAL_MONEDA_NACIONAL")));
					totalesSolicitudesDICOM.setTotalMontoDolarDemanda(new BigDecimal(dataSet.getValue("TOTAL_MONEDA_EXTRANJERA")));
				}else{
					if(dataSet.getValue("TIPO_OPERACION").equals(ConstantesGenerales.SOLICITUD_VENTA)){
						totalesSolicitudesDICOM.setTotalRegistrosOferta(new BigDecimal(dataSet.getValue("CANTIDAD_OPERACIONES")));
						totalesSolicitudesDICOM.setTotalMontoBolivaresOferta(new BigDecimal(dataSet.getValue("TOTAL_MONEDA_NACIONAL")));
						totalesSolicitudesDICOM.setTotalMontoDolarOferta(new BigDecimal(dataSet.getValue("TOTAL_MONEDA_EXTRANJERA")));
					}
				}
			}
		}
		return totalesSolicitudesDICOM;
	}
	
	public void actualizarEstatusRegistroSolicitudes(String nroJornada,String moneda,int estatusInicial, int estatusNuevo) throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE SOLICITUDES_DICOM SET ESTATUS_REGISTRO=").append(estatusNuevo).append(" WHERE ID_JORNADA='").append(nroJornada).append("'AND MONEDA_JORNADA='").append(moneda).append("' AND ESTATUS_REGISTRO=").append(estatusInicial);
		
	System.out.println("actualizarEstatusRegistroSolicitudes: "+sql.toString());
		db.exec(dataSource, sql.toString());
	}	
	
	public void listarSolicitudesLiquidadasDICOM(String nroJornada, String campos,int estatus,boolean opLiquidadas) throws Exception{
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
	
	public void listarSolicitudesDicomPorId(String correlativo) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM SOLICITUDES_DICOM SD WHERE SD.NRO_SOLICITUD='").append(correlativo).append("'");
						
		System.out.println("listarSolicitudesDICOMPorId: "+sb.toString());
		dataSet = db.get(dataSource, sb.toString());		
	}
	
	public SolicitudDICOM busquedaSolicitudesDicomPorId(String idOperacion) throws Exception{
		SolicitudDICOM solicitudDICOM=null; 
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT * FROM SOLICITUDES_DICOM SD WHERE SD.NRO_SOLICITUD='").append(idOperacion).append("'");
						
		System.out.println("listarSolicitudesDICOMPorId: "+sb.toString());
		dataSet = db.get(dataSource, sb.toString());
		
		if(dataSet.count()>0){
			dataSet.first();
			dataSet.next();
			solicitudDICOM=new SolicitudDICOM();
			
			
			solicitudDICOM.setIdOperacion(idOperacion);
			solicitudDICOM.setTipoOperacion(dataSet.getValue("TIPO_OPERACION"));
			solicitudDICOM.setIdUnidadInversion(Long.parseLong(dataSet.getValue("UNDINV_ID")));								
			solicitudDICOM.setMontoOperacionVEF(new BigDecimal(dataSet.getValue("MONTO_OP_NACIONAL")));
			//solicitudDICOM.setDivisaMonNacional(new BigDecimal(dataSet.getValue("MONTO_OP_NACIONAL")));
			solicitudDICOM.setMontoComision(new BigDecimal(dataSet.getValue("MONTO_COMISION")));
						
			solicitudDICOM.setPorcentajeComision(new BigDecimal(dataSet.getValue("PORC_COMISION")));						
			solicitudDICOM.setMontoOperacionUSD(new BigDecimal(dataSet.getValue("MONTO_OP_EXTRANJERA")));
			solicitudDICOM.setPtcComisionIGTF(dataSet.getValue("PORC_COMISION_IGTF"));//TODO AGREGAR CAMPO PARA % COMISION IGTF EN TABLA SOLICITUDES_DICOM
			solicitudDICOM.setMontoComisionIGTF(dataSet.getValue("COMISION_IGTF"));//TODO AGREGAR CAMPO PARA MONTO COMISION IGTF EN TABLA SOLICITUDES_DICOM
			//solicitudDICOM.setTasaCambio(formatoArchivoRecepcion.getTasaCambio());
				
			
		}
		
		return solicitudDICOM;
	}
}   

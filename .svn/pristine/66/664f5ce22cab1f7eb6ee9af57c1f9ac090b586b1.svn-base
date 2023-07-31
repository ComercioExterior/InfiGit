package com.bdv.infi.dao;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import megasoft.DataSet;
import megasoft.Logger;
import megasoft.db;
import com.bancovenezuela.comun.data.OrdenOperacion;
import com.bdv.infi.data.InstruccionesPago;
import com.bdv.infi.data.Orden;
import com.bdv.infi.data.OrdenDataExt;
import com.bdv.infi.data.ProcesoGestion;
import com.bdv.infi.logic.IngresoOpics;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.DataExtendida;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.TransaccionFija;
import com.bdv.infi.logic.interfaces.TransaccionFinanciera;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.logic.interfaces.TipoInstruccion;
/**
 * DAO para Gestion de Pagos
 */
public class GestionPagoDAO extends GenericoDAO{
/**
 * Constructor de la Clase
 * @param ds
 */
	public GestionPagoDAO(Transaccion transaccion)throws Exception {
		super(transaccion);
	}
	public GestionPagoDAO(DataSource ds) {
		super(ds);
	}

	@Override
	public Object moveNext() throws Exception {
		return null;
	}
	
	SimpleDateFormat formato = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
	/**
	 * Lista las operaciones que no tengan instrucción de pago y que no se haya definido ninguna instruccion para la misma
	 * @param idCliente
	 * @throws Exception
	 */
	/*public void listarOperacionesMonedas(long idCliente) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("select distinct infi_tb_207_ordenes_operacion.moneda_id,infi_tb_201_ctes.CLIENT_NOMBRE,(select sum(monto_operacion) from infi_tb_204_ordenes "); 
		sb.append("left join infi_tb_207_ordenes_operacion a on infi_tb_204_ordenes.ORDENE_ID = a.ORDENE_ID "); 
		sb.append("left join infi_tb_201_ctes on infi_tb_204_ordenes.client_id = infi_tb_201_ctes.client_id "); 
		sb.append("left join INFI_TB_012_TRANSACCIONES on infi_tb_204_ordenes.TRANSA_ID = INFI_TB_012_TRANSACCIONES.TRANSA_ID "); 
		sb.append("where infi_tb_207_ordenes_operacion.CTECTA_NUMERO is null and CTECTA_NOMBRE  is null "); 
		sb.append("and CTECTA_BCOCTA_BCO is null and CTECTA_BCOCTA_DIRECCION  is null and CTECTA_BCOCTA_SWIFT is null "); 
		sb.append("and CTECTA_BCOCTA_BIC is null and CTECTA_BCOCTA_TELEFONO is null and CTECTA_BCOCTA_ABA is null "); 
		sb.append("and CTECTA_BCOCTA_PAIS is null and CTECTA_BCOINT_BCO is null and CTECTA_BCOINT_DIRECCION is null "); 
		sb.append("and CTECTA_BCOINT_SWIFT is null and CTECTA_BCOINT_BIC is null and CTECTA_BCOINT_TELEFONO is null "); 
		sb.append("and CTECTA_BCOINT_ABA is null and CTECTA_BCOINT_PAIS is null "); 
		sb.append("and a.STATUS_OPERACION = '").append(ConstantesGenerales.STATUS_EN_ESPERA).append("' "); 
		sb.append("and a.TRNF_TIPO='").append(TransaccionFinanciera.CREDITO ).append("' "); 
		sb.append("and a.ORDENE_OPERACION_ID not in (select ordene_operacion_id from  INFI_TB_813_PROCESO_OPERACION) "); 
		sb.append("and infi_tb_204_ordenes.client_id =").append(idCliente).append(" ");  
		sb.append("and a.moneda_id=infi_tb_207_ordenes_operacion.moneda_id "); 
		sb.append("and (infi_tb_204_ordenes.TRANSA_ID='").append("PAGO_CUPONES" ); 
		sb.append("' or infi_tb_204_ordenes.TRANSA_ID='").append(TransaccionNegocio.CUSTODIA_AMORTIZACION); 
		sb.append("'))as monto,INFI_VI_MONEDAS.MONEDA_DESCRIPCION "); 
		sb.append("from infi_tb_204_ordenes "); 
		sb.append("left join infi_tb_207_ordenes_operacion on infi_tb_204_ordenes.ORDENE_ID = infi_tb_207_ordenes_operacion.ORDENE_ID "); 
		sb.append("left join infi_tb_201_ctes on infi_tb_204_ordenes.client_id = infi_tb_201_ctes.client_id "); 
		sb.append("left join INFI_TB_012_TRANSACCIONES on infi_tb_204_ordenes.TRANSA_ID = INFI_TB_012_TRANSACCIONES.TRANSA_ID "); 
		sb.append("left join INFI_VI_MONEDAS on infi_tb_207_ordenes_operacion.MONEDA_ID = INFI_VI_MONEDAS.MONEDA_ID");
		sb.append(" where infi_tb_207_ordenes_operacion.CTECTA_NUMERO is null and CTECTA_NOMBRE  is null "); 
		sb.append("and CTECTA_BCOCTA_BCO is null and CTECTA_BCOCTA_DIRECCION  is null and CTECTA_BCOCTA_SWIFT is null "); 
		sb.append("and CTECTA_BCOCTA_BIC is null and CTECTA_BCOCTA_TELEFONO is null and CTECTA_BCOCTA_ABA is null "); 
		sb.append("and CTECTA_BCOCTA_PAIS is null and CTECTA_BCOINT_BCO is null and CTECTA_BCOINT_DIRECCION is null "); 
		sb.append("and CTECTA_BCOINT_SWIFT is null and CTECTA_BCOINT_BIC is null and CTECTA_BCOINT_TELEFONO is null "); 
		sb.append("and CTECTA_BCOINT_ABA is null and CTECTA_BCOINT_PAIS is null "); 
		sb.append("and infi_tb_207_ordenes_operacion.STATUS_OPERACION = '");
		sb.append(ConstantesGenerales.STATUS_EN_ESPERA).append("' "); 
		sb.append("and infi_tb_207_ordenes_operacion.TRNF_TIPO='").append(TransaccionFinanciera.CREDITO).append("' "); 
		sb.append("and infi_tb_207_ordenes_operacion.ORDENE_OPERACION_ID not in (select ordene_operacion_id from  INFI_TB_813_PROCESO_OPERACION) "); 
		sb.append("and infi_tb_204_ordenes.client_id =").append(idCliente).append(" ");
		sb.append("and (infi_tb_204_ordenes.TRANSA_ID='").append(TransaccionNegocio.PAGO_CUPON); 
		sb.append("' or infi_tb_204_ordenes.TRANSA_ID='" ).append(TransaccionNegocio.CUSTODIA_AMORTIZACION).append("')");

		//System.out.println("listarOperacionesMonedas: "+sb.toString());
		dataSet = db.get(dataSource, sb.toString());}*/
	
	
	/**
	 * Lista las operaciones que no tengan instrucción de pago y que no se haya definido ninguna instruccion para la misma
	 * @param idCliente
	 * @throws Exception
	 */
	public void listarOperaciones(long idCliente,String moneda) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("select * "); 
		sb.append("from infi_tb_204_ordenes "); 
		sb.append("left join infi_tb_207_ordenes_operacion on infi_tb_204_ordenes.ORDENE_ID = infi_tb_207_ordenes_operacion.ORDENE_ID "); 
		sb.append("left join infi_tb_201_ctes on infi_tb_204_ordenes.client_id = infi_tb_201_ctes.client_id "); 
		sb.append("left join INFI_TB_012_TRANSACCIONES on infi_tb_204_ordenes.TRANSA_ID = INFI_TB_012_TRANSACCIONES.TRANSA_ID "); 
		sb.append("where infi_tb_207_ordenes_operacion.CTECTA_NUMERO is null and CTECTA_NOMBRE  is null "); 
		sb.append("and CTECTA_BCOCTA_BCO is null and CTECTA_BCOCTA_DIRECCION  is null and CTECTA_BCOCTA_SWIFT is null "); 
		sb.append("and CTECTA_BCOCTA_BIC is null and CTECTA_BCOCTA_TELEFONO is null and CTECTA_BCOCTA_ABA is null "); 
		sb.append("and CTECTA_BCOCTA_PAIS is null and CTECTA_BCOINT_BCO is null and CTECTA_BCOINT_DIRECCION is null "); 
		sb.append("and CTECTA_BCOINT_SWIFT is null and CTECTA_BCOINT_BIC is null and CTECTA_BCOINT_TELEFONO is null "); 
		sb.append("and CTECTA_BCOINT_ABA is null and CTECTA_BCOINT_PAIS is null "); 
		sb.append("and infi_tb_207_ordenes_operacion.STATUS_OPERACION = '");
		sb.append(ConstantesGenerales.STATUS_EN_ESPERA).append("' "); 
		sb.append("and infi_tb_207_ordenes_operacion.TRNF_TIPO='").append(TransaccionFinanciera.CREDITO).append("' "); 
		sb.append("and infi_tb_207_ordenes_operacion.ORDENE_OPERACION_ID not in (select ordene_operacion_id from  INFI_TB_813_PROCESO_OPERACION) "); 
		sb.append("and infi_tb_204_ordenes.client_id =").append(idCliente).append(" and infi_tb_207_ordenes_operacion.moneda_id='").append(moneda).append("'");
		sb.append(" and (infi_tb_204_ordenes.TRANSA_ID='").append("PAGO_CUPONES"); 
		sb.append("' or infi_tb_204_ordenes.TRANSA_ID='" ).append(TransaccionNegocio.CUSTODIA_AMORTIZACION).append("')");

		dataSet = db.get(dataSource, sb.toString());}


	/**
	 *Inserta el proceso,las instrucciones de pago, las operaciones financieras asociadas
	 * Crea la orden de pago con la operacion financiera por el monto total de las instrucciones de pago
	 * @param procesoGestion
	 * @param _app
	 * @param nombreUsuario
	 * @param direccionIp
	 * @param idMonedaBase
	 * @param ordeneId
	 * @return ArrayList<String>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<String>  insertarProceso(ProcesoGestion procesoGestion,ServletContext _app,String nombreUsuario,String direccionIp,String idMonedaBase,long ordeneId)throws Exception{
		
		ArrayList<String>consultas = new ArrayList<String>();
		StringBuffer sql 		   = new StringBuffer();
		long procesoId   		   = Long.parseLong(db.getSequence(this.dataSource, ConstantesGenerales.INFI_TB_810_PROCESO_INST));
		
		//Set del ID del proceso al object ProcesoGestion
		procesoGestion.setProcesoID(procesoId);

		sql.append("insert into INFI_TB_810_PROCESO_INST (proceso_id,client_id,fecha_registro,usuario_id) values(");
		sql.append(procesoId).append(",");
		sql.append(procesoGestion.getClientId()).append(",");
		sql.append("sysdate").append(",");
		sql.append(procesoGestion.getUsuarioId()).append(")");

		consultas.add(sql.toString());
		
		//Buscamos los parametros de fecha valor (Tabla INFI_TB_046_FECHA_VALOR) y los guardamos en un HashMap
		FechaValorDAO fechaValorDAO = new FechaValorDAO(dataSource);
		HashMap<String,Date> parametrosFecha = fechaValorDAO.listarFecha();
		/*
		 * Se recorre el arrayList para armar los query de Instrucciones de pago
		 * Se almacenan en variables los valores de la instruccion para ser seteados a la operacion
		 */

			String monedaId 				= "";
			String clienteCuentaNumero 		= "";
			String clienteCuentaNombre 		= "";
			String ctectaBcoctaBco 			= "";
			String ctectaBcoctaSwift 		= "";
			String ctectaBcoctaBic 			= "";
			String ctectaBcoctaTelefono 	= "";
			String ctectaBcoctaAba 			= "";
			String ctectaBcointBco 			= "";
			String ctectaBcointDireccion    = "";
			String ctectaBcointSwift 		= "";
			String ctectaBcointBic 			= "";;
			String ctectaBcointTelefono 	= "";
			String ctectaBcointAba 			= "";
			long tipoInstruccionId 			= 0;
			Date fechaValor                 = null;

		ArrayList<InstruccionesPago> instruccionesArrayList = procesoGestion.getInstruccionesPago();
		int i = 0;
		
		//Creamos la Orden de Pago que contendra la operacion financiera a cancelar por parte de BDV
		Orden orden = new Orden();
		
		for (Iterator iter = instruccionesArrayList.iterator(); iter.hasNext(); ) {
			InstruccionesPago instruccionesPago =(InstruccionesPago) iter.next();
			
			
			//Si es el primer elemento del array se asigna valores a las variables ya que la instruccion es la misma
			if(i==0){
				monedaId 				 = instruccionesPago.getMonedaId();
				clienteCuentaNumero 	 = instruccionesPago.getClienteCuentaNumero();
				clienteCuentaNombre 	 = instruccionesPago.getClienteCuentaNombre();
				ctectaBcoctaBco 		 = instruccionesPago.getCtectaBcoctaBco();
				ctectaBcoctaSwift 		 = instruccionesPago.getCtectaBcoctaSwift();
				ctectaBcoctaBic 		 = instruccionesPago.getCtectaBcoctaBic();
				ctectaBcoctaTelefono 	 = instruccionesPago.getCtectaBcoctaTelefono();
				ctectaBcoctaAba 		 = instruccionesPago.getCtectaBcoctaAba();
				ctectaBcointBco 		 = instruccionesPago.getCtectaBcointBco();
				ctectaBcointDireccion    = instruccionesPago.getCtectaBcointDireccion();
				ctectaBcointSwift 		 = instruccionesPago.getCtectaBcointSwift();
				ctectaBcointBic 		 = instruccionesPago.getCtectaBcointBic();
				ctectaBcointTelefono 	 = instruccionesPago.getCtectaBcointTelefono();
				ctectaBcointAba 		 = instruccionesPago.getCtectaBcointAba();
				tipoInstruccionId 		 = instruccionesPago.getTipoInstruccionId();
				
				//Se valida que tipo de operacion es para obtener la fecha valor
				if(instruccionesPago.getTipoInstruccionId()==TipoInstruccion.CHEQUE){
					fechaValor = parametrosFecha.get(String.valueOf(TipoInstruccion.CHEQUE));
					
				}else if(instruccionesPago.getTipoInstruccionId()==TipoInstruccion.CUENTA_NACIONAL){
					fechaValor = parametrosFecha.get(String.valueOf(TipoInstruccion.CUENTA_NACIONAL));
					
				}
				else if(instruccionesPago.getTipoInstruccionId()==TipoInstruccion.CUENTA_SWIFT){
					fechaValor = parametrosFecha.get(String.valueOf(TipoInstruccion.CUENTA_SWIFT));
					
				}
				else if(instruccionesPago.getTipoInstruccionId()==TipoInstruccion.OPERACION_DE_CAMBIO){
					fechaValor = parametrosFecha.get(String.valueOf(TipoInstruccion.OPERACION_DE_CAMBIO));
					
				}
				
			}//fin IF es primer elemento
			
			//Arma el query de instrucciones de pago
			sql = new StringBuffer();
			
			//Secuencia de instrucciones de pago
			long instruccionId   = Long.parseLong(db.getSequence(this.dataSource, ConstantesGenerales.INFI_TB_811_INST_OPERACION));
			
			sql.append("insert into INFI_TB_811_INST_OPERACION(INSTRUCCION_ID,PROCESO_ID, STATUS_OPERACION, CEDULA_BENEFICIARIO, NOMBRE_BENEFICIARIO, MONTO_INSTRUCCION, MONEDA_ID, NUMERO_CHEQUE, FECHA_OPERACION, FECHA_APLICACION, FECHA_VALOR, CTECTA_NUMERO, CTECTA_NOMBRE, CTECTA_BCOCTA_BCO, CTECTA_BCOCTA_DIRECCION, CTECTA_BCOCTA_SWIFT, CTECTA_BCOCTA_BIC, CTECTA_BCOCTA_TELEFONO, CTECTA_BCOCTA_ABA, CTECTA_BCOINT_BCO, CTECTA_BCOINT_DIRECCION, CTECTA_BCOINT_SWIFT, CTECTA_BCOINT_BIC, CTECTA_BCOINT_TELEFONO, CTECTA_BCOINT_ABA, CTECTA_OBSERVACION, CTECTA_BCOINT_OBSERVACION ,TIPO_INSTRUCCION_ID,TASA_CAMBIO)values(");
			sql.append(instruccionId).append(","); 
			sql.append(procesoId).append(",'");
			sql.append(instruccionesPago.getStatusOperacion()).append("','");
			sql.append(instruccionesPago.getCedulaBeneficiario()).append("','");
			sql.append(instruccionesPago.getNombrebeneficiario()).append("'," ); 
			sql.append(instruccionesPago.getMontoInstruccion()).append(",'");			
			sql.append(instruccionesPago.getMonedaId()).append("',");
			sql.append(this.verificarValor(instruccionesPago.getNumeroCheque())).append(",");			
			sql.append("to_date('"+formato.format(instruccionesPago.getFechaOperacion())).append("','" + ConstantesGenerales.FORMATO_FECHA +"'),");
			sql.append(instruccionesPago.getFechaAplicacion()==null?"null":"to_date('"+formato.format(instruccionesPago.getFechaAplicacion())+"','"+ConstantesGenerales.FORMATO_FECHA+"')").append("," ); 
			sql.append("to_date('"+formato.format(fechaValor)).append("','" + ConstantesGenerales.FORMATO_FECHA +"'),'"); 
			sql.append(instruccionesPago.getClienteCuentaNumero()).append("',");
			
			sql.append(this.verificarValor(instruccionesPago.getClienteCuentaNombre())).append(",'");
			
			sql.append(instruccionesPago.getCtectaBcoctaBco()).append("','" ); 
			sql.append(instruccionesPago.getCtectaBcoctaDireccion()).append("',"); 
			sql.append(this.verificarValor(instruccionesPago.getCtectaBcoctaSwift())).append(",");
			sql.append(this.verificarValor(instruccionesPago.getCtectaBcoctaBic())).append(",");
			
			sql.append(this.verificarValor(instruccionesPago.getCtectaBcoctaTelefono())).append(",");
			sql.append(this.verificarValor(instruccionesPago.getCtectaBcoctaAba())).append(",");
			sql.append(this.verificarValor(instruccionesPago.getCtectaBcointBco())).append(",");
			sql.append(this.verificarValor(instruccionesPago.getCtectaBcointDireccion())).append(",");
			sql.append(this.verificarValor(instruccionesPago.getCtectaBcointSwift())).append(",");
			sql.append(this.verificarValor(instruccionesPago.getCtectaBcointBic())).append(",");
			sql.append(this.verificarValor(instruccionesPago.getCtectaBcointTelefono())).append(",");
			sql.append(this.verificarValor(instruccionesPago.getCtectaBcointAba())).append(",");			
			sql.append(this.verificarValor(instruccionesPago.getObservacionCtectaBcocta())).append(",");			
			sql.append(this.verificarValor(instruccionesPago.getObservacionCtectaBcoint())).append(",");			
			
			sql.append(instruccionesPago.getTipoInstruccionId()).append(",").append(instruccionesPago.getTasaCambio()).append(")");
			
			//Se suman los montos de las instrucciones de pago
			//montoTotal = montoTotal.add(instruccionesPago.getMontoInstruccion().setScale(4,BigDecimal.ROUND_HALF_EVEN));
			//Se agrega el query al arrayList
			consultas.add(sql.toString());
			
			
			if(!procesoGestion.isRecompra()){
				
			
			Date date   = new Date();
			
			orden.setIdCliente(procesoGestion.getClientId());
			orden.setStatus(StatusOrden.REGISTRADA);
			orden.setIdTransaccion(TransaccionNegocio.ORDEN_PAGO);
			orden.setIdOrdenRelacionada(ordeneId);
			orden.setFechaOrden(date);
			orden.setFechaValor(fechaValor);
			orden.setCarteraPropia(false);
			String codigoOperacion = "";
			
			//Buscamos el codigo de operación el cuál sería el mismo que venta de títulos
			DataSet _codigoOperacion = db.get(dataSource,"select codigo_operacion from INFI_TB_032_TRNF_FIJAS where trnfin_id="+TransaccionFija.VENTA_TITULOS);
			if(_codigoOperacion.count()>0)
			{
				_codigoOperacion.first();
				_codigoOperacion.next();
				codigoOperacion = _codigoOperacion.getValue("codigo_operacion");
			}
			
			//Creamos la Operacion Financiera de Gestion de pago
			OrdenOperacion ordenOperacion = new OrdenOperacion();
			ordenOperacion.setAplicaReverso(false);
			ordenOperacion.setCodigoABA(ctectaBcoctaAba);
			ordenOperacion.setCodigoABAIntermediario(ctectaBcointAba);
			ordenOperacion.setCodigoBicBanco(ctectaBcoctaBic);
			ordenOperacion.setCodigoBicBancoIntermediario(ctectaBcointBic);
			ordenOperacion.setCodigoSwiftBanco(ctectaBcoctaSwift);
			ordenOperacion.setCodigoSwiftBancoIntermediario(ctectaBcointSwift);
			ordenOperacion.setDescripcionTransaccion("");
			ordenOperacion.setDireccionBanco(ctectaBcoctaBco);
			ordenOperacion.setDireccionBancoIntermediario(ctectaBcointDireccion);
			ordenOperacion.setIdMoneda(monedaId);
			ordenOperacion.setMontoOperacion(instruccionesPago.getMontoInstruccion());
			ordenOperacion.setNombreBanco(ctectaBcoctaBco);
			ordenOperacion.setNombreBancoIntermediario(ctectaBcointBco);
			ordenOperacion.setNombreReferenciaCuenta(clienteCuentaNombre);
			ordenOperacion.setNumeroCuenta(clienteCuentaNumero);
			ordenOperacion.setStatusOperacion(ConstantesGenerales.STATUS_EN_ESPERA);
			ordenOperacion.setTelefonoBanco(ctectaBcoctaTelefono);
			ordenOperacion.setTelefonoBancoIntermediario(ctectaBcointTelefono);
			ordenOperacion.setTipoTransaccionFinanc(TransaccionFinanciera.CREDITO);
			ordenOperacion.setFechaAplicar(fechaValor);
			ordenOperacion.setCodigoOperacion(codigoOperacion);
			ordenOperacion.setTasa(instruccionesPago.getTasaCambio());
			
			//Agrega el nombre de la transacción
			if (instruccionesPago.getTipoInstruccionId()==TipoInstruccion.OPERACION_DE_CAMBIO){
				ordenOperacion.setNombreOperacion("Operación de cambio");	
			} else if (instruccionesPago.getTipoInstruccionId()==TipoInstruccion.CHEQUE){
				ordenOperacion.setNombreOperacion("Cheque");
			} else if (instruccionesPago.getTipoInstruccionId()==TipoInstruccion.CUENTA_NACIONAL){
				ordenOperacion.setNombreOperacion("Depósito en cta Nacional");
			} else if (instruccionesPago.getTipoInstruccionId()==TipoInstruccion.CUENTA_SWIFT){
				ordenOperacion.setNombreOperacion("Depósito en cta Extranjera");
			}
			
			//Agregamos la operación a la orden
			orden.agregarOperacion(ordenOperacion);
			
			//Se agrega la data extendida del proceso al cual estara atado la orden
			OrdenDataExt ordenDataExt = new OrdenDataExt();
			ordenDataExt.setIdData(DataExtendida.NOMBRE_BENEFICIARIO);
			ordenDataExt.setValor(String.valueOf(instruccionesPago.getNombrebeneficiario()));
			orden.agregarOrdenDataExt(ordenDataExt);
			
			//Se agrega la data extendida del proceso al cual estara atado la orden
			ordenDataExt = new OrdenDataExt();
			ordenDataExt.setIdData(DataExtendida.CEDULA_BENEFICIARIO);
			ordenDataExt.setValor(String.valueOf(instruccionesPago.getCedulaBeneficiario()));
			orden.agregarOrdenDataExt(ordenDataExt);
			
			//Se agrega la data extendida del tipo instruccion
			ordenDataExt = new OrdenDataExt();
			ordenDataExt.setIdData(DataExtendida.TIPO_INSTRUCCION_PAGO);
			ordenDataExt.setValor(String.valueOf(tipoInstruccionId));
			orden.agregarOrdenDataExt(ordenDataExt);
			
			OrdenDAO ordenDAO       = new OrdenDAO(dataSource);
			String consultasArray[] = ordenDAO.insertar(orden);		
			
			//Si el tipo de isntrucción de pago es de Operación de Cambio se Genera el deal ticket hacia opics
			if(instruccionesPago.getTipoInstruccionId()==TipoInstruccion.OPERACION_DE_CAMBIO)
			{
				//Se genera el deal ticket
				IngresoOpics ingresoOpics = new IngresoOpics(dataSource,_app,procesoGestion.getUsuarioId(),direccionIp,nombreUsuario);
				ArrayList consultasSql = ingresoOpics.operacionCambio(orden, nombreUsuario, clienteCuentaNumero, idMonedaBase, instruccionesPago.getMonedaId(), instruccionesPago.getMontoInstruccionNoConversion(), instruccionesPago.getTasaCambio());
				
				//Se recorre el arrayList para agregar los querys del deal ticket
				for (int j = 0; j < consultasSql.size(); j++) {
					consultas.add(consultasSql.get(j).toString());
				}//fin for			
			}//fin if	
			
				//Se agregan los querys de la orden a insertar
				for(int j=0; j<consultasArray.length;j++){
					consultas.add(consultasArray[j]);
				}//fin for
								
				//Se modifica la orden con el idOpics
				consultas.add("update infi_tb_204_ordenes set id_opics="+orden.getIdEjecucion()+" where ordene_id="+orden.getIdOrden());
				
			}//fin !procesoGestion.isRecompra()
			i++;
		}//FIN FOR
		
		//Obtenemos el id de la operacion financiera creada en Orden de Pago
		ArrayList<OrdenOperacion> ordenOperacionArraylist = new ArrayList<OrdenOperacion>();
		ordenOperacionArraylist = orden.getOperacion();
		OrdenOperacion ordenOperacion = new OrdenOperacion();
		
		for(int h =0;h<ordenOperacionArraylist.size();h++)
		{
			ordenOperacion = ordenOperacionArraylist.get(h);
		}
		

		//Se recorre el arrayList para armar los query de operaciones financieras asociadas a el proceso
		ArrayList operacionesArrayList = procesoGestion.getOperaciones();
		for (int k = 0;k<operacionesArrayList.size();k++) {

			//Arma el query de instrucciones de pago
			sql = new StringBuffer();
			sql.append("insert into INFI_TB_813_PROCESO_OPERACION(PROCESO_ID,ORDENE_OPERACION_ID)values(");
			sql.append(procesoGestion.getProcesoID()).append(",");  
			sql.append(operacionesArrayList.get(k)).append(")");
			
			//Se agrega el query al arrayList
			consultas.add(sql.toString());
			
			//Sí se creó una orden de pago, actualizamos todas las operaciones financieras involucradas con el id relacion de la operacion creada
			if(orden.getIdOrden()!=0)
			{
				StringBuffer sqlOperaciones = new StringBuffer();
				sqlOperaciones.append("update infi_tb_207_ordenes_operacion set ORDENE_RELAC_OPERACION_ID=");
				sqlOperaciones.append(ordenOperacion.getIdOperacion());
				sqlOperaciones.append(" where ORDENE_OPERACION_ID=").append(operacionesArrayList.get(k));
				
				consultas.add(sqlOperaciones.toString());
				
				
				//Actualizamos las ordenes involucradas con el id de la orden de pago creada
				StringBuffer sqlOrden = new StringBuffer();
				sqlOrden.append(" select infi_tb_204_ordenes.ordene_id from infi_tb_204_ordenes left join infi_tb_207_ordenes_operacion on infi_tb_204_ordenes.ordene_id=infi_tb_207_ordenes_operacion.ordene_id ");
				sqlOrden.append("where ORDENE_OPERACION_ID =").append(operacionesArrayList.get(k));
				
				//Ejecutamos la consulta
				dataSet = db.get(dataSource,sqlOrden.toString());
				
				//Si trae registro se agrega el query para actualizar la orden
				if(dataSet.count()>0)
				{
					dataSet.next();
					
					StringBuffer sqlUpdate = new StringBuffer();
					sqlUpdate.append("update infi_tb_204_ordenes set ORDENE_ID_RELACION=").append(orden.getIdOrden());
					sqlUpdate.append(" where ordene_id=").append(dataSet.getValue("ordene_id"));
					
					//Se agrega la consulta
					consultas.add(sqlUpdate.toString());

					
				}//FIN IF
				
			}//fin if
		}//FIN FOR

		return consultas;
	}//fin metodo

	
	/**
	 * Lista los procesos por cliente
	 * @param idCliente
	 * @throws Exception
	 */
	public void listarProcesosPorCliente(long idCliente)throws Exception{
		StringBuffer sql = new StringBuffer("select proceso_id,fecha_registro,USERID from INFI_TB_810_PROCESO_INST left join MSC_USER on INFI_TB_810_PROCESO_INST.USUARIO_ID=MSC_USER.MSC_USER_ID where client_id=");
		sql.append(idCliente).append(" order by proceso_id desc ");
		
		dataSet = db.get(dataSource, sql.toString());
		System.out.println("listarProcesosPorCliente "+sql.toString());
	}
	/**
	 * Lista los detalles de un proceso de instruccion de pago
	 * @param procesoId
	 * @throws Exception
	 */
	public void detalleProceso(long procesoId)throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("select infi_tb_207_ordenes_operacion.ORDENE_ID,INFI_TB_012_TRANSACCIONES.TRANSA_DESCRIPCION,infi_tb_207_ordenes_operacion.ORDENE_OPERACION_ID,infi_tb_207_ordenes_operacion.STATUS_OPERACION,infi_tb_207_ordenes_operacion.MONEDA_ID,infi_tb_207_ordenes_operacion.MONTO_OPERACION from INFI_TB_813_PROCESO_OPERACION  left join infi_tb_207_ordenes_operacion on INFI_TB_813_PROCESO_OPERACION.ORDENE_OPERACION_ID=infi_tb_207_ordenes_operacion.ORDENE_OPERACION_ID left join infi_tb_204_ordenes on infi_tb_207_ordenes_operacion.ORDENE_ID=infi_tb_204_ordenes.ORDENE_ID left join INFI_TB_012_TRANSACCIONES on infi_tb_204_ordenes.TRANSA_ID = INFI_TB_012_TRANSACCIONES.TRANSA_ID where proceso_id=");
		sql.append(procesoId);
		sql.append(" order by ordene_id desc");

		dataSet = db.get(dataSource, sql.toString());
	}
/**
 * Lista las Instrucciones de pago definidas para un proceso especifico
 * @param procesoId
 * @param cuenta
 * @throws Exception
 */
	public void listarInstrucciones(long procesoId,long cuenta)throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select INFI_TB_811_INST_OPERACION.*,case when STATUS_OPERACION='");
		sql.append(ConstantesGenerales.STATUS_EN_ESPERA); 
		sql.append("' then '0' when STATUS_OPERACION='"); 
		sql.append(ConstantesGenerales.STATUS_APLICADA); 
		sql.append("' then 'readonly' when STATUS_OPERACION='"); 
		sql.append(ConstantesGenerales.STATUS_RECHAZADA); 
		sql.append("' then '0' when STATUS_OPERACION='"); 
		sql.append(ConstantesGenerales.STATUS_CANCELADA); 
		sql.append("' then 'readonly' end readonly ");
		sql.append(" from INFI_TB_811_INST_OPERACION where  TIPO_INSTRUCCION_ID=" +cuenta+" and proceso_id=");
		sql.append(procesoId);
		dataSet = db.get(dataSource, sql.toString());
	}
	/**
	 * Actualiza las Instrucciones de pago; especificamente nombre y cedula
	 * @param procesoId
	 * @param chequeId
	 * @param nombre
	 * @param cedula
	 * @param statement
	 * @throws Exception
	 */
	public String  actualizarInstruccionesDePagoCheque(long procesoId,long instruccion,String nombre,String cedula,BigDecimal monto)throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("update INFI_TB_811_INST_OPERACION set NOMBRE_BENEFICIARIO='");
		sql.append(nombre); 
		sql.append("',CEDULA_BENEFICIARIO='"); 
		sql.append(cedula); 
		sql.append("',MONTO_INSTRUCCION=").append(monto); 
		sql.append(" where PROCESO_ID=");
		sql.append(procesoId); 
		sql.append(" and INSTRUCCION_ID = "); 
		sql.append(instruccion);
		
		return sql.toString();
	}
	/**
	 * Elimina un proceso de instrucciones de pago; 810,811 y tabla 812
	 * @param proceso
	 */
	public String[] eliminarProceso(long proceso)throws Exception{
		/*
		 * INFI_TB_813_PROCESO_OPERACION
		 */
		String sqlArray[] = new String[3];
		StringBuffer sql = new StringBuffer();
		sql.append("delete from ").append(ConstantesGenerales.INFI_TB_813_PROCESO_OPERACION).append(" where PROCESO_ID=").append(proceso);

		sqlArray[0]= sql.toString();
		/*
		 * INFI_TB_811_INST_OPERACION
		 */
		sql = new StringBuffer();
		sql.append("delete from ").append(ConstantesGenerales.INFI_TB_811_INST_OPERACION).append(" where PROCESO_ID=").append(proceso);
		
		sqlArray[1]= sql.toString();
		/*
		 * INFI_TB_810_PROCESO_INST
		 */
		sql = new StringBuffer();
		sql.append("delete from ").append(ConstantesGenerales.INFI_TB_810_PROCESO_INST).append(" where PROCESO_ID=").append(proceso);

		sqlArray[2]= sql.toString();
		
		/*
		 * infi_tb_207_ordenes_operacion
		 
		sql = new StringBuffer();
		sql.append("delete from infi_tb_207_ordenes_operacion where ORDENE_ID=").append(proceso);

		sqlArray[3]= sql.toString();
		*/
			
		return sqlArray;

	}
	/**
	 * Lista los montos correspondientes a las operaciones involucradas en un proceso
	 * @param procesoId
	 * @return BigDecimal, el monto total de las operaciones involucradas en el proceso
	 * @throws Exception
	 */
	public BigDecimal listarMontoOperacionesproceso(long procesoId)throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select sum(monto_instruccion)monto from INFI_TB_811_INST_OPERACION where proceso_id=");
		sql.append(procesoId);
		dataSet = db.get(dataSource, sql.toString());
		
		BigDecimal montoProceso = new BigDecimal(0);
		if(dataSet.count()>0){
			dataSet.first();
			dataSet.next();
			montoProceso = montoProceso.add(new BigDecimal(dataSet.getValue("monto")==null?"0":dataSet.getValue("monto")).setScale(4, BigDecimal.ROUND_HALF_UP));
		}
		return montoProceso;
	}
	/**
	 * Lista el monto total de las operaciones que se encuentran en status procesadas
	 * @return BigDecimal
	 * @throws Exception
	 */
	public BigDecimal listarMontoOperacionesProcesadasProceso(long procesoId)throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("select sum(monto_instruccion)monto from INFI_TB_811_INST_OPERACION where INFI_TB_811_INST_OPERACION.STATUS_OPERACION='APLICADA' and proceso_id=");
		sql.append(procesoId);
		dataSet = db.get(dataSource, sql.toString());
		
		BigDecimal montoProceso = new BigDecimal(0);
		if(dataSet.count()>0){
			dataSet.first();
			dataSet.next();
			montoProceso = montoProceso.add(dataSet.getValue("monto")==null||dataSet.getValue("monto").equals("")?new BigDecimal(0):new BigDecimal(dataSet.getValue("monto")));
		}
		return montoProceso;
	}
	/**
	 * Actualiza las instrucciones de pago definidas como Transferencias Internacionales
	 * @param InstruccionesPago instruccionesPago
	 * @param Statement statement
	 * @throws Exception
	 */
	public String actualizarInstruccion(InstruccionesPago instruccionesPago)throws Exception{
		StringBuffer sql = new StringBuffer();
		sql.append("update INFI_TB_811_INST_OPERACION set CEDULA_BENEFICIARIO='").append(instruccionesPago.getCedulaBeneficiario());
		sql.append("',NOMBRE_BENEFICIARIO='").append(instruccionesPago.getNombrebeneficiario());
		sql.append("',MONTO_INSTRUCCION=").append(instruccionesPago.getMontoInstruccion()); 
		sql.append(",CTECTA_NUMERO='").append(instruccionesPago.getClienteCuentaNumero());
		sql.append("',CTECTA_NOMBRE='").append(instruccionesPago.getClienteCuentaNombre()); 
		sql.append("',CTECTA_BCOCTA_BCO='").append(instruccionesPago.getCtectaBcoctaBco()==null?"":instruccionesPago.getCtectaBcoctaBco());
		sql.append("',CTECTA_BCOCTA_DIRECCION='").append(instruccionesPago.getCtectaBcoctaDireccion()==null?"":instruccionesPago.getCtectaBcoctaDireccion()); 
		sql.append("',CTECTA_BCOCTA_SWIFT='").append(instruccionesPago.getCtectaBcoctaSwift()==null?"":instruccionesPago.getCtectaBcoctaSwift()); 
		sql.append("',CTECTA_BCOCTA_BIC='").append(instruccionesPago.getCtectaBcoctaBic()==null?"":instruccionesPago.getCtectaBcoctaBic()); 
		sql.append("',CTECTA_BCOCTA_TELEFONO='").append(instruccionesPago.getCtectaBcoctaTelefono()==null?"":instruccionesPago.getCtectaBcoctaTelefono());  
		sql.append("',CTECTA_BCOCTA_ABA='" ).append(instruccionesPago.getCtectaBcoctaAba()==null?"":instruccionesPago.getCtectaBcoctaAba());
		sql.append("',CTECTA_BCOINT_BCO='").append(instruccionesPago.getCtectaBcointBco()==null?"":instruccionesPago.getCtectaBcointBco());
		sql.append("',CTECTA_BCOINT_DIRECCION='").append(instruccionesPago.getCtectaBcointDireccion()==null?"":instruccionesPago.getCtectaBcointDireccion()); 
		sql.append("',CTECTA_BCOINT_SWIFT='").append(instruccionesPago.getCtectaBcointSwift()==null?"":instruccionesPago.getCtectaBcointSwift()); 
		sql.append("',CTECTA_BCOINT_BIC='").append(instruccionesPago.getCtectaBcointBic()==null?"":instruccionesPago.getCtectaBcointBic()); 
		sql.append("',CTECTA_BCOINT_TELEFONO='").append(instruccionesPago.getCtectaBcointTelefono()==null?"":instruccionesPago.getCtectaBcointTelefono());
		sql.append("',CTECTA_BCOINT_ABA='").append(instruccionesPago.getCtectaBcointAba()==null?"":instruccionesPago.getCtectaBcointAba()); 
		sql.append("',CTECTA_OBSERVACION='").append(instruccionesPago.getObservacionCtectaBcocta()==null?"":instruccionesPago.getObservacionCtectaBcocta());  
		sql.append("',CTECTA_BCOINT_OBSERVACION='").append(instruccionesPago.getObservacionCtectaBcoint()==null?"":instruccionesPago.getObservacionCtectaBcoint());
		sql.append("' where INSTRUCCION_ID=").append(instruccionesPago.getInstruccionId());
		sql.append("  and PROCESO_ID=").append(instruccionesPago.getProcesoId()); 
		return sql.toString();
	}
	/**
	 * Verifica para un proceso si existe una operacion en status EN ESPERA
	 * @param proceso
	 * @return boolean existe
	 * @throws Exception
	 */
	public boolean verificarExisteOperacionAplicada(long proceso)throws Exception {
		StringBuffer sql= new StringBuffer();
		boolean existe = false;
		
		sql.append("select * from INFI_TB_811_INST_OPERACION where status_operacion='").append(ConstantesGenerales.STATUS_APLICADA);
		sql.append("' and proceso_id=").append(proceso);
		
		dataSet = db.get(dataSource,sql.toString());
		
		if(dataSet.count()>0){
			existe = true;
		}
		return existe;
		
	}
	/**
	 * lista los tipos de instruccion
	 * **/

	public void listarInstruccion()throws Exception{
		dataSet=db.get(dataSource,"select * from INFI_TB_045_TIPO_INSTRUCCION" );
	}
	
	public void listarInstruccion(String idsInstruccion)throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("SELECT TIPO_INSTRUCCION_ID,INSTRUCCION_NOMBRE from INFI_TB_045_TIPO_INSTRUCCION where tipo_instruccion_id in(").append(idsInstruccion).append(")");
		dataSet=db.get(dataSource,sql.toString());
		
	}
	/**
	 * 
	 * @throws Exception
	 */
	public void listarCobroComision()throws Exception{
		StringBuffer sql= new StringBuffer();
		sql.append("select * from INFI_TB_045_TIPO_INSTRUCCION where tipo_instruccion_id=").append(TipoInstruccion.CUENTA_NACIONAL);
		dataSet=db.get(dataSource,sql.toString());

	}
	
	/**
	 * Lista las instrucciones de pago definidas para ser canceladas con cheque
	 * @throws Exception
	 */
	public void listarInstruccionesCheque(long clienteId)throws Exception {
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("select proceso_id,fecha_registro,USERID,INFI_TB_811_INST_OPERACION.* ");
		sql.append("from INFI_TB_810_PROCESO_INST ");
		sql.append("left join MSC_USER on INFI_TB_810_PROCESO_INST.USUARIO_ID=MSC_USER.MSC_USER_ID ");
		sql.append("LEFT JOIN INFI_TB_811_INST_OPERACION ON INFI_TB_810_PROCESO_INST.PROCESO_ID=INFI_TB_811_INST_OPERACION.PROCESO_ID ");
		sql.append("where numero_cheque is null");
		sql.append(" and client_id=").append(clienteId);
		sql.append(" AND TIPO_INSTRUCCION_ID=");
		sql.append(TipoInstruccion.CHEQUE);
		sql.append(" and status_operacion='");
		sql.append(ConstantesGenerales.STATUS_EN_ESPERA).append("'");	
		dataSet = db.get(dataSource,sql.toString());
	}
	
	/**
	 * Forma el query para modificar un registro en la tabla 811
	 * @param proceso
	 * @param numeroCheque
	 * @return String sql
	 * @throws Exception
	 */
	public String modificarIntruccion(long proceso,String numeroCheque)throws Exception{
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("update INFI_TB_811_INST_OPERACION set numero_cheque='").append(numeroCheque);
		sql.append("',fecha_aplicacion=").append(formatearFechaBDActual());
		sql.append(",status_operacion='").append(ConstantesGenerales.STATUS_APLICADA);
		sql.append("' where proceso_id=").append(proceso);
		
		return sql.toString();
	}
	/**
	 * Lista las operaciones financieras de un cliente que no posean instrucciones de pago asociadas
	 * @param idCliente
	 * @throws Exception
	 */
	public void listarOperacionesSinInstruccionVentaTitulos(long idCliente)throws Exception{

		StringBuffer sb = new StringBuffer();
		sb.append("select distinct infi_tb_207_ordenes_operacion.moneda_id,");
		sb.append("decode(INFI_TB_045_TIPO_INSTRUCCION.instruccion_nombre,null,'No posee',INFI_TB_045_TIPO_INSTRUCCION.instruccion_nombre)as instruccion_nombre,");
		sb.append("infi_tb_201_ctes.CLIENT_NOMBRE,");
		sb.append("INFI_TB_212_ORDENES_DATAEXT.dtaext_id,decode(INFI_TB_212_ORDENES_DATAEXT.dtaext_valor,null,'0',INFI_TB_212_ORDENES_DATAEXT.dtaext_valor)as dtaext_valor,");
		sb.append("(select sum(monto_operacion) ");
		sb.append("from infi_tb_204_ordenes ");
		sb.append("left join infi_tb_207_ordenes_operacion a on infi_tb_204_ordenes.ORDENE_ID = a.ORDENE_ID ");
		sb.append("left join infi_tb_201_ctes on infi_tb_204_ordenes.client_id =infi_tb_201_ctes.client_id ");
		sb.append("left join INFI_TB_012_TRANSACCIONES on infi_tb_204_ordenes.TRANSA_ID = INFI_TB_012_TRANSACCIONES.TRANSA_ID ");
		sb.append("left join INFI_TB_212_ORDENES_DATAEXT b on infi_tb_204_ordenes.ordene_id = b.ordene_id ");
		sb.append("where infi_tb_207_ordenes_operacion.CTECTA_NUMERO is null and CTECTA_NOMBRE  is null and CTECTA_BCOCTA_BCO is null and 	CTECTA_BCOCTA_DIRECCION  is null and CTECTA_BCOCTA_SWIFT is null and CTECTA_BCOCTA_BIC is null and 	CTECTA_BCOCTA_TELEFONO is null and CTECTA_BCOCTA_ABA is null and CTECTA_BCOCTA_PAIS is null and CTECTA_BCOINT_BCO is 	null and CTECTA_BCOINT_DIRECCION is null and CTECTA_BCOINT_SWIFT is null and CTECTA_BCOINT_BIC is null and 	CTECTA_BCOINT_TELEFONO is null and CTECTA_BCOINT_ABA is null and CTECTA_BCOINT_PAIS is null ");
		sb.append("and a.STATUS_OPERACION = '");
		sb.append(ConstantesGenerales.STATUS_EN_ESPERA).append("' ");
		sb.append("and a.TRNF_TIPO='").append(TransaccionFinanciera.CREDITO);
		sb.append("' and a.ORDENE_OPERACION_ID not in ");
		sb.append("(select ordene_operacion_id from  INFI_TB_813_PROCESO_OPERACION) and infi_tb_204_ordenes.client_id =");
		sb.append(idCliente).append(" ");
		sb.append("and a.moneda_id=infi_tb_207_ordenes_operacion.moneda_id and ");
		sb.append("infi_tb_204_ordenes.TRANSA_ID='").append(TransaccionNegocio.VENTA_TITULOS);
		sb.append("'  and decode(b.dtaext_valor,null,'0',b.dtaext_valor)=decode(INFI_TB_212_ORDENES_DATAEXT.dtaext_valor,null,'0',INFI_TB_212_ORDENES_DATAEXT.dtaext_valor))as monto,");
		sb.append("infi_tb_207_ordenes_operacion.moneda_id as MONEDA_DESCRIPCION ");
		sb.append("from infi_tb_204_ordenes ");
		sb.append("left join infi_tb_207_ordenes_operacion on infi_tb_204_ordenes.ORDENE_ID = infi_tb_207_ordenes_operacion.ORDENE_ID ");
		sb.append("left join infi_tb_201_ctes on infi_tb_204_ordenes.client_id = infi_tb_201_ctes.client_id ");
		sb.append("left join INFI_TB_012_TRANSACCIONES on infi_tb_204_ordenes.TRANSA_ID = INFI_TB_012_TRANSACCIONES.TRANSA_ID ");
		//sb.append("left join INFI_VI_MONEDAS on infi_tb_207_ordenes_operacion.MONEDA_ID = INFI_VI_MONEDAS.MONEDA_ID ");
		sb.append("left join INFI_TB_212_ORDENES_DATAEXT on infi_tb_204_ordenes.ordene_id = INFI_TB_212_ORDENES_DATAEXT.ordene_id ");
		sb.append("left join INFI_TB_045_TIPO_INSTRUCCION on 	INFI_TB_212_ORDENES_DATAEXT.DTAEXT_VALOR=INFI_TB_045_TIPO_INSTRUCCION.TIPO_INSTRUCCION_ID  ");
		sb.append("where infi_tb_207_ordenes_operacion.CTECTA_NUMERO is null and CTECTA_NOMBRE  is null and CTECTA_BCOCTA_BCO is null ");
		sb.append("and CTECTA_BCOCTA_DIRECCION  is null and CTECTA_BCOCTA_SWIFT is null and CTECTA_BCOCTA_BIC is null and 	CTECTA_BCOCTA_TELEFONO is null and CTECTA_BCOCTA_ABA is null and CTECTA_BCOCTA_PAIS is null and CTECTA_BCOINT_BCO is 	null and CTECTA_BCOINT_DIRECCION is null and CTECTA_BCOINT_SWIFT is null and CTECTA_BCOINT_BIC is null and 	CTECTA_BCOINT_TELEFONO is null and CTECTA_BCOINT_ABA is null and CTECTA_BCOINT_PAIS is null ");
		sb.append("and infi_tb_207_ordenes_operacion.STATUS_OPERACION = '");
		sb.append(ConstantesGenerales.STATUS_EN_ESPERA);
		sb.append("' and infi_tb_207_ordenes_operacion.TRNF_TIPO='").append(TransaccionFinanciera.CREDITO).append("' ");
		sb.append("and infi_tb_207_ordenes_operacion.ORDENE_OPERACION_ID not in (select ordene_operacion_id ");
		sb.append("from  INFI_TB_813_PROCESO_OPERACION) ");
		sb.append("and infi_tb_204_ordenes.client_id =").append(idCliente);
		sb.append(" ");
		sb.append("and (infi_tb_204_ordenes.TRANSA_ID='").append(TransaccionNegocio.VENTA_TITULOS).append("') ");
		sb.append("and (DTAEXT_ID='");
		sb.append(DataExtendida.TIPO_INSTRUCCION_PAGO);
		sb.append("' or INFI_TB_212_ORDENES_DATAEXT.DTAEXT_ID is null)");


		dataSet = db.get(dataSource, sb.toString());
		System.out.println(sb);
	}
	
	/**
	 * Lista las operaciones financieras que no posean instrucciones de pago para Venta titulos
	 * @param idCliente
	 * @param moneda
	 * @throws Exception
	 */
	public void listarOperacionesVentaTitulos(Long idCliente,String moneda,String instruccion)throws Exception{

		StringBuffer sb = new StringBuffer();
		sb.append("select * "); 
		sb.append("from infi_tb_204_ordenes "); 
		sb.append("left join infi_tb_207_ordenes_operacion on infi_tb_204_ordenes.ORDENE_ID = infi_tb_207_ordenes_operacion.ORDENE_ID "); 
		sb.append("left join infi_tb_201_ctes on infi_tb_204_ordenes.client_id = infi_tb_201_ctes.client_id "); 
		sb.append("left join INFI_TB_012_TRANSACCIONES on infi_tb_204_ordenes.TRANSA_ID = INFI_TB_012_TRANSACCIONES.TRANSA_ID "); 
		sb.append("left join INFI_TB_212_ORDENES_DATAEXT on infi_tb_204_ordenes.ordene_id = INFI_TB_212_ORDENES_DATAEXT.ordene_id "); 
		sb.append("where infi_tb_207_ordenes_operacion.CTECTA_NUMERO is null and CTECTA_NOMBRE  is null "); 
		sb.append("and CTECTA_BCOCTA_BCO is null and CTECTA_BCOCTA_DIRECCION  is null and CTECTA_BCOCTA_SWIFT is null "); 
		sb.append("and CTECTA_BCOCTA_BIC is null and CTECTA_BCOCTA_TELEFONO is null and CTECTA_BCOCTA_ABA is null "); 
		sb.append("and CTECTA_BCOCTA_PAIS is null and CTECTA_BCOINT_BCO is null and CTECTA_BCOINT_DIRECCION is null "); 
		sb.append("and CTECTA_BCOINT_SWIFT is null and CTECTA_BCOINT_BIC is null and CTECTA_BCOINT_TELEFONO is null "); 
		sb.append("and CTECTA_BCOINT_ABA is null and CTECTA_BCOINT_PAIS is null "); 
		sb.append("and infi_tb_207_ordenes_operacion.STATUS_OPERACION = '");
		sb.append(ConstantesGenerales.STATUS_EN_ESPERA).append("' "); 
		sb.append("and infi_tb_207_ordenes_operacion.TRNF_TIPO='").append(TransaccionFinanciera.CREDITO).append("' "); 
		sb.append("and infi_tb_207_ordenes_operacion.ORDENE_OPERACION_ID not in (select ordene_operacion_id from  INFI_TB_813_PROCESO_OPERACION) "); 
		sb.append("and infi_tb_204_ordenes.client_id =").append(idCliente).append(" and infi_tb_207_ordenes_operacion.moneda_id='").append(moneda).append("'");
		sb.append(" and (infi_tb_204_ordenes.TRANSA_ID='").append(TransaccionNegocio.VENTA_TITULOS).append("')");
		sb.append(" and (dtaext_id='");
		sb.append(DataExtendida.TIPO_INSTRUCCION_PAGO);
		sb.append("' or dtaext_id is null) ");
		
		//Si la instruccion es igual a null o vacio, se buscan las operaciones con ese valor en data extendida
		if(instruccion.equalsIgnoreCase(null) || instruccion.equalsIgnoreCase("0") || instruccion.equalsIgnoreCase("") || instruccion.equals("&nbsp;"))
		{
			sb.append(" and dtaext_valor is null");
		}else
		{
			sb.append(" and dtaext_valor=").append("'").append(instruccion).append("'");
		}

		dataSet = db.get(dataSource, sb.toString());
	}
	
	
	/**
	 * Inserta el proceso,las instrucciones de pago, las operaciones financieras asociadas
	 * Crea la orden de pago con la operacion financiera por el monto total de las instrucciones de pago
	 * @param procesoGestion
	 * @return un arrayList con las consultas para ser ejecutadas
	 * @throws Exception
	 */
		@SuppressWarnings("unchecked")
		public ArrayList<String>  insertarProceso(ProcesoGestion procesoGestion,ServletContext _app,String nombreUsuario,String direccionIp,String idMonedaBase)throws Exception{
			
			ArrayList<String>consultas = new ArrayList<String>();
			StringBuffer sql 		   = new StringBuffer();
			long procesoId   		   = Long.parseLong(db.getSequence(this.dataSource, ConstantesGenerales.INFI_TB_810_PROCESO_INST));
			
			//Set del ID del proceso al object ProcesoGestion
			procesoGestion.setProcesoID(procesoId);

			sql.append("insert into INFI_TB_810_PROCESO_INST (proceso_id,client_id,fecha_registro,usuario_id) values(");
			sql.append(procesoId).append(",");
			sql.append(procesoGestion.getClientId()).append(",");
			sql.append("sysdate").append(",");
			sql.append(procesoGestion.getUsuarioId()).append(")");

			consultas.add(sql.toString());
			
			//Buscamos los parametros de fecha valor (Tabla INFI_TB_046_FECHA_VALOR) y los guardamos en un HashMap
			FechaValorDAO fechaValorDAO = new FechaValorDAO(dataSource);
			HashMap<String,Date> parametrosFecha = fechaValorDAO.listarFecha();
			/*
			 * Se recorre el arrayList para armar los query de Instrucciones de pago
			 * Se almacenan en variables los valores de la instruccion para ser seteados a la operacion
			 */

				String monedaId 				= "";
				String clienteCuentaNumero 		= "";
				String clienteCuentaNombre 		= "";
				String ctectaBcoctaBco 			= "";
				String ctectaBcoctaSwift 		= "";
				String ctectaBcoctaBic 			= "";
				String ctectaBcoctaTelefono 	= "";
				String ctectaBcoctaAba 			= "";
				String ctectaBcointBco 			= "";
				String ctectaBcointDireccion    = "";
				String ctectaBcointSwift 		= "";
				String ctectaBcointBic 			= "";;
				String ctectaBcointTelefono 	= "";
				String ctectaBcointAba 			= "";
				long tipoInstruccionId 			= 0;
				Date fechaValor                 = null;

			ArrayList<InstruccionesPago> instruccionesArrayList = procesoGestion.getInstruccionesPago();
			int i = 0;
			
			//Creamos la Orden de Pago que contendra la operacion financiera a cancelar por parte de BDV
			Orden orden = new Orden();
			
			for (Iterator iter = instruccionesArrayList.iterator(); iter.hasNext(); ) {
				InstruccionesPago instruccionesPago =(InstruccionesPago) iter.next();
				
				//Si es el primer elemento del array se asigna valores a las variables ya que la instruccion es la misma
				if(i==0){
					monedaId 				 = instruccionesPago.getMonedaId();
					clienteCuentaNumero 	 = instruccionesPago.getClienteCuentaNumero();
					clienteCuentaNombre 	 = instruccionesPago.getClienteCuentaNombre();
					ctectaBcoctaBco 		 = instruccionesPago.getCtectaBcoctaBco();
					ctectaBcoctaSwift 		 = instruccionesPago.getCtectaBcoctaSwift();
					ctectaBcoctaBic 		 = instruccionesPago.getCtectaBcoctaBic();
					ctectaBcoctaTelefono 	 = instruccionesPago.getCtectaBcoctaTelefono();
					ctectaBcoctaAba 		 = instruccionesPago.getCtectaBcoctaAba();
					ctectaBcointBco 		 = instruccionesPago.getCtectaBcointBco();
					ctectaBcointDireccion    = instruccionesPago.getCtectaBcointDireccion();
					ctectaBcointSwift 		 = instruccionesPago.getCtectaBcointSwift();
					ctectaBcointBic 		 = instruccionesPago.getCtectaBcointBic();
					ctectaBcointTelefono 	 = instruccionesPago.getCtectaBcointTelefono();
					ctectaBcointAba 		 = instruccionesPago.getCtectaBcointAba();
					tipoInstruccionId 		 = instruccionesPago.getTipoInstruccionId();
					
					//Se valida que tipo de operacion es para obtener la fecha valor
					if(instruccionesPago.getTipoInstruccionId()==TipoInstruccion.CHEQUE){
						fechaValor = parametrosFecha.get(String.valueOf(TipoInstruccion.CHEQUE));
						
					}else if(instruccionesPago.getTipoInstruccionId()==TipoInstruccion.CUENTA_NACIONAL){
						fechaValor = parametrosFecha.get(String.valueOf(TipoInstruccion.CUENTA_NACIONAL));
						
					}
					else if(instruccionesPago.getTipoInstruccionId()==TipoInstruccion.CUENTA_SWIFT){
						fechaValor = parametrosFecha.get(String.valueOf(TipoInstruccion.CUENTA_SWIFT));
						
					}
					else if(instruccionesPago.getTipoInstruccionId()==TipoInstruccion.OPERACION_DE_CAMBIO){
						fechaValor = parametrosFecha.get(String.valueOf(TipoInstruccion.OPERACION_DE_CAMBIO));
						
					}
					
				}//fin IF es primer elemento
				
				//Arma el query de instrucciones de pago
				sql = new StringBuffer();
				
				//Secuencia de instrucciones de pago
				long instruccionId   = Long.parseLong(db.getSequence(this.dataSource, ConstantesGenerales.INFI_TB_811_INST_OPERACION));
				
				sql.append("insert into INFI_TB_811_INST_OPERACION(INSTRUCCION_ID,PROCESO_ID, STATUS_OPERACION, CEDULA_BENEFICIARIO, NOMBRE_BENEFICIARIO, MONTO_INSTRUCCION, MONEDA_ID, NUMERO_CHEQUE, FECHA_OPERACION, FECHA_APLICACION, FECHA_VALOR, CTECTA_NUMERO, CTECTA_NOMBRE, CTECTA_BCOCTA_BCO, CTECTA_BCOCTA_DIRECCION, CTECTA_BCOCTA_SWIFT, CTECTA_BCOCTA_BIC, CTECTA_BCOCTA_TELEFONO, CTECTA_BCOCTA_ABA, CTECTA_BCOINT_BCO, CTECTA_BCOINT_DIRECCION, CTECTA_BCOINT_SWIFT, CTECTA_BCOINT_BIC, CTECTA_BCOINT_TELEFONO, CTECTA_BCOINT_ABA, CTECTA_OBSERVACION, CTECTA_BCOINT_OBSERVACION ,TIPO_INSTRUCCION_ID,TASA_CAMBIO)values(");
				sql.append(instruccionId).append(","); 
				sql.append(procesoId).append(",'");
				sql.append(instruccionesPago.getStatusOperacion()).append("','");
				sql.append(instruccionesPago.getCedulaBeneficiario()).append("','");
				sql.append(instruccionesPago.getNombrebeneficiario()).append("'," ); 
				sql.append(instruccionesPago.getMontoInstruccion()).append(",'"); 
				sql.append(instruccionesPago.getMonedaId()).append("','"); 
				sql.append(instruccionesPago.getNumeroCheque()==null?"":instruccionesPago.getNumeroCheque()).append("',");
				sql.append("to_date('"+formato.format(instruccionesPago.getFechaOperacion())).append("','" + ConstantesGenerales.FORMATO_FECHA +"'),");
				sql.append(instruccionesPago.getFechaAplicacion()==null?"null":"to_date('"+formato.format(instruccionesPago.getFechaAplicacion())+"','"+ConstantesGenerales.FORMATO_FECHA+"')").append("," ); 
				sql.append("to_date('"+formato.format(fechaValor)).append("','" + ConstantesGenerales.FORMATO_FECHA +"'),'"); 
				sql.append(instruccionesPago.getClienteCuentaNumero()==null?"":instruccionesPago.getClienteCuentaNumero()).append("','"); 
				sql.append(instruccionesPago.getClienteCuentaNombre()==null?"":instruccionesPago.getClienteCuentaNombre()).append("','"); 
				sql.append(instruccionesPago.getCtectaBcoctaBco()==null?"":instruccionesPago.getCtectaBcoctaBco()).append("','" ); 
				sql.append(instruccionesPago.getCtectaBcoctaDireccion()==null?"":instruccionesPago.getCtectaBcoctaDireccion()).append("','"); 
				sql.append(instruccionesPago.getCtectaBcoctaSwift()==null?"":instruccionesPago.getCtectaBcoctaSwift()).append("','" ); 
				sql.append(instruccionesPago.getCtectaBcoctaBic()==null?"":instruccionesPago.getCtectaBcoctaBic()).append("','"); 
				sql.append(instruccionesPago.getCtectaBcoctaTelefono()==null?"":instruccionesPago.getCtectaBcoctaTelefono()).append("','");
				sql.append(instruccionesPago.getCtectaBcoctaAba()==null?"":instruccionesPago.getCtectaBcoctaAba()).append("','");
				sql.append(instruccionesPago.getCtectaBcointBco()==null?"":instruccionesPago.getCtectaBcointBco()).append("','" );
				sql.append(instruccionesPago.getCtectaBcointDireccion()==null?"":instruccionesPago.getCtectaBcointDireccion()).append("','"); 
				sql.append(instruccionesPago.getCtectaBcointSwift()==null?"":instruccionesPago.getCtectaBcointSwift()).append("','");
				sql.append(instruccionesPago.getCtectaBcointBic()==null?"":instruccionesPago.getCtectaBcointBic()).append("','"); 
				sql.append(instruccionesPago.getCtectaBcointTelefono()==null?"":instruccionesPago.getCtectaBcointTelefono()).append("','");
				sql.append(instruccionesPago.getCtectaBcointAba()==null?"":instruccionesPago.getCtectaBcointAba()).append("','"); 
				sql.append(instruccionesPago.getObservacionCtectaBcocta()==null?"":instruccionesPago.getObservacionCtectaBcocta()).append("','");  
				sql.append(instruccionesPago.getObservacionCtectaBcoint()==null?"":instruccionesPago.getObservacionCtectaBcoint()).append("',"); 
				sql.append(instruccionesPago.getTipoInstruccionId()).append(",").append(instruccionesPago.getTasaCambio()).append(")");
				
				//Se suman los montos de las instrucciones de pago
				//montoTotal = montoTotal.add(instruccionesPago.getMontoInstruccion().setScale(4,BigDecimal.ROUND_HALF_EVEN));
				//Se agrega el query al arrayList
				consultas.add(sql.toString());
				
				
				if(!procesoGestion.isRecompra()){
					
				
				Date date   = new Date();
				
				orden.setIdCliente(procesoGestion.getClientId());
				orden.setStatus(StatusOrden.REGISTRADA);
				orden.setIdTransaccion(TransaccionNegocio.ORDEN_PAGO);
				orden.setFechaOrden(date);
				orden.setFechaValor(fechaValor);
				orden.setCarteraPropia(false);
				String codigoOperacion = "";
				
				//Buscamos el codigo de operación el cuál sería el mismo que venta de títulos
				DataSet _codigoOperacion = db.get(dataSource,"select codigo_operacion from INFI_TB_032_TRNF_FIJAS where trnfin_id="+TransaccionFija.VENTA_TITULOS);
				if(_codigoOperacion.count()>0)
				{
					_codigoOperacion.first();
					_codigoOperacion.next();
					codigoOperacion = _codigoOperacion.getValue("codigo_operacion");
				}
				
				//Creamos la Operacion Financiera de Gestion de pago
				OrdenOperacion ordenOperacion = new OrdenOperacion();
				ordenOperacion.setAplicaReverso(false);
				ordenOperacion.setCodigoABA(ctectaBcoctaAba);
				ordenOperacion.setCodigoABAIntermediario(ctectaBcointAba);
				ordenOperacion.setCodigoBicBanco(ctectaBcoctaBic);
				ordenOperacion.setCodigoBicBancoIntermediario(ctectaBcointBic);
				ordenOperacion.setCodigoSwiftBanco(ctectaBcoctaSwift);
				ordenOperacion.setCodigoSwiftBancoIntermediario(ctectaBcointSwift);
				ordenOperacion.setDescripcionTransaccion("");
				ordenOperacion.setDireccionBanco(ctectaBcoctaBco);
				ordenOperacion.setDireccionBancoIntermediario(ctectaBcointDireccion);
				ordenOperacion.setIdMoneda(monedaId);
				ordenOperacion.setMontoOperacion(instruccionesPago.getMontoInstruccion());
				ordenOperacion.setNombreBanco(ctectaBcoctaBco);
				ordenOperacion.setNombreBancoIntermediario(ctectaBcointBco);
				ordenOperacion.setNombreReferenciaCuenta(clienteCuentaNombre);
				ordenOperacion.setNumeroCuenta(clienteCuentaNumero);
				ordenOperacion.setStatusOperacion(ConstantesGenerales.STATUS_EN_ESPERA);
				ordenOperacion.setTelefonoBanco(ctectaBcoctaTelefono);
				ordenOperacion.setTelefonoBancoIntermediario(ctectaBcointTelefono);
				ordenOperacion.setTipoTransaccionFinanc(TransaccionFinanciera.CREDITO);
				ordenOperacion.setFechaAplicar(fechaValor);
				ordenOperacion.setCodigoOperacion(codigoOperacion);
				ordenOperacion.setTasa(instruccionesPago.getTasaCambio());
				
				//Agrega el nombre de la transacción
				if (instruccionesPago.getTipoInstruccionId()==TipoInstruccion.OPERACION_DE_CAMBIO){
					ordenOperacion.setNombreOperacion("Operación de cambio");	
				} else if (instruccionesPago.getTipoInstruccionId()==TipoInstruccion.CHEQUE){
					ordenOperacion.setNombreOperacion("Cheque");
				} else if (instruccionesPago.getTipoInstruccionId()==TipoInstruccion.CUENTA_NACIONAL){
					ordenOperacion.setNombreOperacion("Depósito en cta Nacional");
				} else if (instruccionesPago.getTipoInstruccionId()==TipoInstruccion.CUENTA_SWIFT){
					ordenOperacion.setNombreOperacion("Depósito en cta Extranjera");
				}				
				
				//Agregamos la operación a la orden
				orden.agregarOperacion(ordenOperacion);
				
				//Se agrega la data extendida del proceso al cual estara atado la orden
				OrdenDataExt ordenDataExt = new OrdenDataExt();
				ordenDataExt.setIdData(DataExtendida.NOMBRE_BENEFICIARIO);
				ordenDataExt.setValor(String.valueOf(instruccionesPago.getNombrebeneficiario()));
				orden.agregarOrdenDataExt(ordenDataExt);
				
				//Se agrega la data extendida del proceso al cual estara atado la orden
				ordenDataExt = new OrdenDataExt();
				ordenDataExt.setIdData(DataExtendida.CEDULA_BENEFICIARIO);
				ordenDataExt.setValor(String.valueOf(instruccionesPago.getCedulaBeneficiario()));
				orden.agregarOrdenDataExt(ordenDataExt);
				
				//Se agrega la data extendida del tipo instruccion
				ordenDataExt = new OrdenDataExt();
				ordenDataExt.setIdData(DataExtendida.TIPO_INSTRUCCION_PAGO);
				ordenDataExt.setValor(String.valueOf(tipoInstruccionId));
				orden.agregarOrdenDataExt(ordenDataExt);
				
				OrdenDAO ordenDAO       = new OrdenDAO(dataSource);
				String consultasArray[] = ordenDAO.insertar(orden);
				
				
				//Si el tipo de isntrucción de pago es de Operación de Cambio se Genera el deal ticket hacia opics
				if(instruccionesPago.getTipoInstruccionId()==TipoInstruccion.OPERACION_DE_CAMBIO)
				{
					//Se genera el deal ticket
					IngresoOpics ingresoOpics = new IngresoOpics(dataSource,_app,procesoGestion.getUsuarioId(),direccionIp,nombreUsuario);
					ArrayList consultasSql = ingresoOpics.operacionCambio(orden, nombreUsuario, clienteCuentaNumero, idMonedaBase, instruccionesPago.getMonedaId(), instruccionesPago.getMontoInstruccionNoConversion(), instruccionesPago.getTasaCambio());
					
					//Se recorre el arrayList para agregar los querys del deal ticket
					for (int j = 0; j < consultasSql.size(); j++) {
						consultas.add(consultasSql.get(j).toString());
					}//fin for			
				}//fin if		
				
					//Se agregan los querys de la orden a insertar
					for(int j=0; j<consultasArray.length;j++){
						consultas.add(consultasArray[j]);
					}//fin for
									
					//Se modifica la orden con el idOpics
					consultas.add("update infi_tb_204_ordenes set id_opics="+orden.getIdEjecucion()+" where ordene_id="+orden.getIdOrden());
					
				}//fin !procesoGestion.isRecompra()
				i++;
			}//FIN FOR
			
			//Obtenemos el id de la operacion financiera creada en Orden de Pago
			ArrayList<OrdenOperacion> ordenOperacionArraylist = new ArrayList<OrdenOperacion>();
			ordenOperacionArraylist = orden.getOperacion();
			OrdenOperacion ordenOperacion = new OrdenOperacion();
			
			for(int h =0;h<ordenOperacionArraylist.size();h++)
			{
				ordenOperacion = ordenOperacionArraylist.get(h);
			}
			

			//Se recorre el arrayList para armar los query de operaciones financieras asociadas a el proceso
			ArrayList operacionesArrayList = procesoGestion.getOperaciones();
			for (int k = 0;k<operacionesArrayList.size();k++) {

				//Arma el query de instrucciones de pago
				sql = new StringBuffer();
				sql.append("insert into INFI_TB_813_PROCESO_OPERACION(PROCESO_ID,ORDENE_OPERACION_ID)values(");
				sql.append(procesoGestion.getProcesoID()).append(",");  
				sql.append(operacionesArrayList.get(k)).append(")");
				
				//Se agrega el query al arrayList
				consultas.add(sql.toString());
				
				//Sí se creó una orden de pago, actualizamos todas las operaciones financieras involucradas con el id relacion de la operacion creada
				if(orden.getIdOrden()!=0)
				{
					StringBuffer sqlOperaciones = new StringBuffer();
					sqlOperaciones.append("update infi_tb_207_ordenes_operacion set ORDENE_RELAC_OPERACION_ID=");
					sqlOperaciones.append(ordenOperacion.getIdOperacion());
					sqlOperaciones.append(" where ORDENE_OPERACION_ID=").append(operacionesArrayList.get(k));
					
					consultas.add(sqlOperaciones.toString());
					
					
					//Actualizamos las ordenes involucradas con el id de la orden de pago creada
					StringBuffer sqlOrden = new StringBuffer();
					sqlOrden.append(" select infi_tb_204_ordenes.ordene_id from infi_tb_204_ordenes left join infi_tb_207_ordenes_operacion on infi_tb_204_ordenes.ordene_id=infi_tb_207_ordenes_operacion.ordene_id ");
					sqlOrden.append("where ORDENE_OPERACION_ID =").append(operacionesArrayList.get(k));
					
					//Ejecutamos la consulta
					dataSet = db.get(dataSource,sqlOrden.toString());
					
					//Si trae registro se agrega el query para actualizar la orden
					if(dataSet.count()>0)
					{
						dataSet.next();
						
						StringBuffer sqlUpdate = new StringBuffer();
						sqlUpdate.append("update infi_tb_204_ordenes set ORDENE_ID_RELACION=").append(orden.getIdOrden());
						sqlUpdate.append(" where ordene_id=").append(dataSet.getValue("ordene_id"));
						
						//Se agrega la consulta
						consultas.add(sqlUpdate.toString());
						
					}//FIN IF
				}//fin if
			}//FIN FOR

			return consultas;
		}//fin metodo
		
		/**
		 * Verifica el valor a devolver
		 * @param verificarValor
		 * @return
		 */
		private String verificarValor(String verificarValor){
			if(verificarValor==null){
				return "null";
			} else {
				return "'" + verificarValor + "'";
			}
		}	
		
		/**
		 * Lista las operaciones que no tengan instrucción de pago y que no se haya definido ninguna instruccion para la misma
		 * @param idCliente
		 * @throws Exception
		 */
		public void listarOperacionesPagoCupones(long idCliente, String tipoProd,String moneda) throws Exception{
			StringBuffer sb = new StringBuffer();		

			sb.append("SELECT DISTINCT ORD.TRANSA_ID,ORD.TIPO_PRODUCTO_ID,OP.MONEDA_ID,OP.STATUS_OPERACION,(SELECT SUM (MONTO_OPERACION) FROM INFI_TB_204_ORDENES O, INFI_TB_207_ORDENES_OPERACION OOP"); 
			sb.append(" 	WHERE O.ORDENE_ID=OOP.ORDENE_ID  AND OOP.STATUS_OPERACION= '").append(ConstantesGenerales.STATUS_EN_ESPERA).append("' AND OOP.TRNF_TIPO= '").append(TransaccionFinanciera.CREDITO).append("' AND O.TRANSA_ID='").append(TransaccionNegocio.PAGO_CUPON).append("' AND OOP.MONEDA_ID=OP.MONEDA_ID AND O.CLIENT_ID=").append(idCliente).append(") AS MONTO"); 
			sb.append(" FROM INFI_TB_204_ORDENES ORD, INFI_TB_207_ORDENES_OPERACION OP,INFI_TB_206_ORDENES_TITULOS ot"); 
			sb.append(" WHERE ORD.ORDENE_ID=OP.ORDENE_ID AND ORD.ORDENE_ID=OT.ORDENE_ID AND OP.STATUS_OPERACION= '").append(ConstantesGenerales.STATUS_EN_ESPERA).append("' AND OP.TRNF_TIPO= '").append(TransaccionFinanciera.CREDITO).append("' AND ORD.TRANSA_ID='").append(TransaccionNegocio.PAGO_CUPON).append("' AND ORD.CLIENT_ID=").append(idCliente);
			sb.append(" AND ORD.TIPO_PRODUCTO_ID='").append(tipoProd).append("'").append(" AND OP.MONEDA_ID='").append(moneda).append("'");			
			//System.out.println("listarOperacionesPagoCupones: "+sb.toString());
			dataSet = db.get(dataSource, sb.toString());
		}
		
		/**
		 * Lista las operaciones que no tengan instrucción de pago y que no se haya definido ninguna instruccion para la misma
		 * @param idCliente
		 * @throws Exception
		 */
		public void listarDetalleOperacionesPagoCupones(long idCliente, String tipoProd, String moneda) throws Exception{
			StringBuffer sb = new StringBuffer();		

			sb.append("SELECT ORD.TRANSA_ID, ORD.TIPO_PRODUCTO_ID, OP.MONEDA_ID,ORD.ORDENE_ID,OP.ORDENE_OPERACION_ID,OT.TITULO_ID,OP.STATUS_OPERACION,OP.MONTO_OPERACION,OP.FECHA_INICIO_CP,OP.FECHA_FIN_CP"); 
			sb.append(" FROM INFI_TB_204_ORDENES ORD,INFI_TB_207_ORDENES_OPERACION OP,INFI_TB_206_ORDENES_TITULOS OT"); 
			sb.append(" WHERE ORD.ORDENE_ID=OP.ORDENE_ID AND ORD.ORDENE_ID=OT.ORDENE_ID AND OP.STATUS_OPERACION= '").append(ConstantesGenerales.STATUS_EN_ESPERA).append("' AND OP.TRNF_TIPO= '").append(TransaccionFinanciera.CREDITO).append("' AND ORD.TRANSA_ID='").append(TransaccionNegocio.PAGO_CUPON).append("' AND ORD.CLIENT_ID=").append(idCliente);
			sb.append(" AND OP.MONEDA_ID='").append(moneda).append("' AND ORD.TIPO_PRODUCTO_ID='").append(tipoProd).append("' AND TO_DATE(OP.FECHA_FIN_CP,'").append(ConstantesGenerales.FORMATO_FECHA_RRRR).append("')<= TO_DATE(SYSDATE,'").append(ConstantesGenerales.FORMATO_FECHA_RRRR).append("')");
			
			System.out.println("listarDetalleOperacionesPagoCupones: "+sb.toString());
			dataSet = db.get(dataSource, sb.toString());
		}
		
		/**Almacena los valores leidos en la base de datos
		 * @param errorRegistro 
		 * @param nombreArchivo 
		 * @throws Exception */
		private void cargaTemporalPagos(String[] camposRegistro, String errorRegistro, String nombreArchivo) throws Exception{
				
			StringBuffer sb = new StringBuffer();
			sb.append("INSERT INTO TMP_CARGA_INICIAL_PAGOS (NOMBRE, CEDULA, NOMINAL, INT_PORPAGAR, INT_PAGADOS, FE_PAGO, TIPO_PAGO, TITULO, FE_INICIO_PAGO, FE_FIN_PAGO, ERROR, ARCHIVO,TIPO_PRODUCTO_ID)");		
			sb.append(" VALUES (");
			sb.append("'").append(camposRegistro[0].trim().toUpperCase()).append("'");
			
			for(int k=1; k<camposRegistro.length; k++){
				sb.append(",'").append(camposRegistro[k].trim()).append("'");
			}
			//Guardar mensaje de error
			if(errorRegistro!=null && !errorRegistro.equals("")){
				sb.append(", '").append(errorRegistro).append("'");
			}else{
				sb.append(", NULL");
			}
			
			sb.append(", '").append(nombreArchivo).append("'");
			
			sb.append(")");			
			
			transaccion.ejecutarConsultas(sb.toString());
			
		}
		
		/**
		 * Verifica si el registro que se guardar&aacute; se encuentra duplicado
		 * @param campos
		 * @return
		 * @throws Exception
		 */
		private boolean esRegistroRepetidoTMPPagos(String[] campos) throws Exception {
			
			StringBuffer sql = new StringBuffer();
			
			sql.append("SELECT count(*) FROM TMP_CARGA_INICIAL_PAGOS ");		
			sql.append(" where cedula = '").append(campos[1]).append("'");
			sql.append(" and titulo = '").append(campos[7]).append("'");
			sql.append(" and fe_inicio_pago = '").append(campos[8]).append("'");
			sql.append(" and fe_fin_pago = '").append(campos[9]).append("'");
		
			DataSet _registro = db.get(dataSource, sql.toString());
			
			if(_registro.next())
				return true;
			else
				return false;
		}
}

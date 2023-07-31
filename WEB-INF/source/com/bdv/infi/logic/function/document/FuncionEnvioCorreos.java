package com.bdv.infi.logic.function.document;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import javax.servlet.ServletContext;

import megasoft.DataSet;
import megasoft.Logger;

import com.bancovenezuela.comun.data.OrdenOperacion;
import com.bdv.infi.dao.CamposDinamicos;
import com.bdv.infi.dao.ClienteCuentasDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.TipoTransaccionFinancieraDAO;
import com.bdv.infi.dao.UIBlotterRangosDAO;
import com.bdv.infi.data.CampoDinamico;
import com.bdv.infi.data.Orden;
import com.bdv.infi.data.OrdenesCruce;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionFinanciera;
import com.bdv.infi.util.HashMapUtilitario;
import com.bdv.infi.util.Utilitario;
import com.bdv.infi_toma_orden.data.OrdenTitulo;

/** 
 * Clase encargada del proceso de b&uacute;squeda de los datos necesarios para los documentos de salida en el proceso de toma de orden
 */
public class FuncionEnvioCorreos extends FuncionGenerica{
	
	private ArrayList<String> nombresOperComisiones = new ArrayList<String>();
	
	public void procesar(Object orden, java.lang.Object documentos, ServletContext contexto, String ip) throws Exception{
		procesar(orden, documentos, contexto, ip, 0);
	}
	
	public void procesar(Object orden, java.lang.Object documentos, ServletContext contexto, String ip, int indiceCruce) throws Exception{
		
		Orden ObjOrden = (Orden) orden;
		
		HashMapUtilitario<String, String> mapa = new HashMapUtilitario<String, String>("N/A");
		OrdenDAO ordenDAO = new OrdenDAO(this.getDataSource());
		
		if(indiceCruce>=0){
		
		//BLOQUE de informacion de un cruce
			
			if(ObjOrden!=null && ObjOrden.getOrdenesCrucesTitulos()!=null && ObjOrden.getOrdenesCrucesTitulos().size()>0){ //Se hallaron cruces tipo titulo para la orden
				
				OrdenesCruce oc = ObjOrden.getOrdenesCrucesTitulos().get(indiceCruce);
				
				//NM29643 - infi_TTS_466 Se obtiene el porcentaje de la comision para aplicarsela al contravalor de cada titulo
				//Se modifican las etiquetas cruce_valor_efectivo_bs, cruce_valor_efectivo
				ordenDAO.getPorcentajeComision(ObjOrden.getIdOrden()); //Obtiene el detalle de la orden
				DataSet _ordenComision = ordenDAO.getDataSet();
				
				if(_ordenComision.count()>0){
					
					_ordenComision.first();
					_ordenComision.next();
					
					if(_ordenComision.getValue("COMISION_PCT")!=null && !_ordenComision.getValue("COMISION_PCT").equals("")){
						BigDecimal com = new BigDecimal(_ordenComision.getValue("comision_pct")).setScale(2, BigDecimal.ROUND_HALF_UP);
						com = com.multiply(oc.getContravalorBolivaresCapital());
						com = com.divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);;
						mapa.defaultPut("cruce_valor_efectivo_bs", String.valueOf((oc.getContravalorBolivaresCapital().add(com)).setScale(2, BigDecimal.ROUND_HALF_UP)));
						//Se divide el cruce_valor_efectivo_bs entre la tasa pra obtener el valor efectivo USD
						if(oc.getTasa()!=null && (oc.getTasa().compareTo(new BigDecimal(0))>0)){
							mapa.defaultPut("cruce_valor_efectivo", String.valueOf( new BigDecimal( Double.valueOf(String.valueOf(oc.getContravalorBolivaresCapital())) / Double.valueOf(String.valueOf(oc.getTasa())) ).setScale(2, BigDecimal.ROUND_HALF_UP) ) );
						}else{
							mapa.defaultPut("cruce_valor_efectivo", "");
						}
					}
					
				}
					
				
				
				mapa.defaultPut("cruce_nro_cotizacion_bcv", Utilitario.formatearNumero(oc.getIdOrdenBcvString(),Utilitario.numeroSinDecimales)); //ITS-3206 Formato de campos de montos incorrecto en correo SIMADI NM25287 12/07/2016
				mapa.defaultPut("cruce_monto_cruzado", oc.getMontoOperacionString());
				mapa.defaultPut("cruce_nro_operacion", oc.getNroOperacionString());
				mapa.defaultPut("cruce_tipo_instrumento", oc.getIndicadorTitulo()==ConstantesGenerales.VERDADERO?"Titulo":"Efectivo");
				mapa.defaultPut("cruce_fecha_valor", oc.getFechaValor());
				mapa.defaultPut("cruce_titulo_nombre", oc.getIdTitulo());
				mapa.defaultPut("cruce_titulo_isin", oc.getIsinString());
				mapa.defaultPut("cruce_titulo_precio", oc.getPrecioTituloString());
				mapa.defaultPut("cruce_titulo_mto_int_caidos", String.valueOf(oc.getMtoInteresesCaidosTitulo()));
				//TODO Verificar dias intereses caidos!!!
				//NM29643 infi_TTS_466 Se calculan los dias de intereses caidos transcurridos
				OrdenTitulo beanTitulo = new OrdenTitulo();
				com.bdv.infi_toma_orden.util.Utilitario.calcularInteresesCaidos(oc.getMontoOperacion(), oc.getIdTitulo(), oc.getFechaValor().replaceAll("/","-"), this.getDataSource().toString(), this.getDataSource(), beanTitulo);
				mapa.defaultPut("cruce_titulo_dias_int_caidos", String.valueOf(beanTitulo.getDiferenciaDias()));
				Logger.debug(this, "--------------------------cruce_titulo_dias_int_caidos: "+String.valueOf(beanTitulo.getDiferenciaDias()));
				mapa.defaultPut("cruce_moneda", ObjOrden.getIdMoneda());
				//TODO Verificar valores efectivos!!!
//				BigDecimal valorEfectivo = oc.getMontoOperacion().multiply( oc.getPrecioTitulo().divide(new BigDecimal(100)) ).add(oc.getMtoInteresesCaidosTitulo()); 
////				VALOR EFECTIVO = MONTO ADJUDICADO * (PRECIO/100) + INTERESES CAIDOS
//				mapa.defaultPut("cruce_valor_efectivo", String.valueOf(valorEfectivo));
//				mapa.defaultPut("cruce_valor_efectivo_bs", String.valueOf(valorEfectivo.multiply(oc.getTasa())));
				//nm29643 - infi_TTS_466_Calidad 13/10/2014 Se agrega cruce_valor_nominal
				mapa.defaultPut("cruce_valor_nominal", String.valueOf(oc.getValorNominal()));
				
				com.bdv.infi_toma_orden.dao.TitulosDAO titDAO = new com.bdv.infi_toma_orden.dao.TitulosDAO(this.getDataSource().toString(), this.getDataSource());
				@SuppressWarnings("rawtypes")
				ArrayList listaTitulos = titDAO.listarTitulos(oc.getIdTitulo());
				if(listaTitulos!=null && listaTitulos.size()>0){
					mapa.defaultPut("cruce_titulo_fe_emision", (String)listaTitulos.get(4));
					mapa.defaultPut("cruce_titulo_fe_vencimiento", (String)listaTitulos.get(8));
				}else{
					mapa.defaultPut("cruce_titulo_fe_emision", "");
					mapa.defaultPut("cruce_titulo_fe_vencimiento", "");
				}				
				//Se hallaron cruces para la orden
			}else{
				//No existen títulos asociados a la orden
				mapa.defaultPut("cruce_nro_cotizacion_bcv", "");
				mapa.defaultPut("cruce_monto_cruzado", "");
				mapa.defaultPut("cruce_nro_operacion", "");
				mapa.defaultPut("cruce_tipo_instrumento", "");
				mapa.defaultPut("cruce_fecha_valor", "");
				mapa.defaultPut("cruce_titulo_nombre", "");
				mapa.defaultPut("cruce_titulo_isin", "");
				mapa.defaultPut("cruce_titulo_precio", "");
				mapa.defaultPut("cruce_titulo_mto_int_caidos", "");
				mapa.defaultPut("cruce_titulo_dias_int_caidos", "");
				mapa.defaultPut("cruce_moneda", "");
				mapa.defaultPut("cruce_valor_efectivo", "");
				mapa.defaultPut("cruce_valor_efectivo_bs", "");
				//nm29643 - infi_TTS_466_Calidad 13/10/2014 Se agrega cruce_valor_nominal
				mapa.defaultPut("cruce_valor_nominal", "");
				mapa.defaultPut("cruce_titulo_fe_emision", "");
				mapa.defaultPut("cruce_titulo_fe_vencimiento", "");
				mapa.put("bloque_por_cruce", "");
			}
			
			//Etiquetas de los campos dinamicos
			if (!ObjOrden.isCampoDinamicoVacio()){
				CamposDinamicos campdin = new CamposDinamicos(this.getDataSource());
				ArrayList<CampoDinamico> listaCamposDinamicos = ObjOrden.getCamposDinamicos();
				Iterator<CampoDinamico> it = listaCamposDinamicos.iterator();
				String in = "";
				//Se recorren los campos dinamicos		
				while (it.hasNext()) {
					//Se arma la condicion del query con todos los id de los campos dinamicos
					CampoDinamico campo = (CampoDinamico) it.next();			
					in += campo.getIdCampo();
					if(it.hasNext()){
						in += ",";
					}
				}
				campdin.listarCamposDinamico(in, ObjOrden.getIdOrden());
				DataSet _campos = campdin.getDataSet();
				if(_campos.count()>0){
					_campos.first();
					while(_campos.next()){
						String nombreCampo = campdin.formateo(_campos.getValue("campo_nombre"),1,1,1," ","_");
						String valorCampo = _campos.getValue("campo_valor");
						if( valorCampo!=null && !valorCampo.equals("")) {
							mapa.defaultPut("cd_"+nombreCampo, valorCampo);
						}else{
							mapa.defaultPut("cd_"+nombreCampo, "");
						}
					}
				}
			}//Hay campos dinamicos
			
		}else{
		
		//NO BLOQUE
			
			//Nm29643 - infi_TTS_466
			//Variable que almacenara el monto de la operacion actualizado (el contravalor en bs de lo cruzado tanto en divisas como en titulos)
			BigDecimal montoOperCruzada = new BigDecimal(0);
			BigDecimal montoOperTit = new BigDecimal(0);
			BigDecimal montoOperEfec = new BigDecimal(0);
			//PARA CRUCE TITULOS
			if(ObjOrden!=null && ObjOrden.getOrdenesCrucesTitulos()!=null && ObjOrden.getOrdenesCrucesTitulos().size()>0){ //Se hallaron cruces tipo titulo para la orden
				
				//Se obtiene el monto total cruzado en titulos
				BigDecimal montoTotal = new BigDecimal(0);
				OrdenesCruce ocSum;
				for(int i=0; i<ObjOrden.getOrdenesCrucesTitulos().size(); i++){
					ocSum = ObjOrden.getOrdenesCrucesTitulos().get(i);
					montoTotal = new BigDecimal(
					Double.valueOf(String.valueOf(montoTotal)) + Double.valueOf(String.valueOf(ocSum.getMontoOperacion())) );
					//montoTotal.add(ocSum.getMontoOperacion());
					Logger.debug(this, "Monto Titulo "+(i+1)+": "+ocSum.getMontoOperacion());
					Logger.debug(this, "Monto Total Titulo: "+montoTotal);
					//Se suma el contravalor en bs del titulo
					montoOperCruzada = new BigDecimal(
							Double.valueOf(String.valueOf(montoOperCruzada)) + Double.valueOf(String.valueOf(ocSum.getContravalorBolivaresCapital())) );
				}
				mapa.defaultPut("titulo_mto_cruzado", String.valueOf(montoTotal.setScale(2, BigDecimal.ROUND_HALF_EVEN)));
				Logger.debug(this, "Total Monto Cruzado en Titulos------------------ "+String.valueOf(montoTotal.setScale(2, BigDecimal.ROUND_HALF_EVEN)));
				
				montoOperCruzada.setScale(2, BigDecimal.ROUND_HALF_EVEN);
				montoOperTit = new BigDecimal(Double.valueOf(String.valueOf(montoOperCruzada))).setScale(2, BigDecimal.ROUND_HALF_EVEN);
				Logger.debug(this, "Total Contravalor Bs en TITULOS------------------ "+montoOperTit);
				Logger.debug(this, "montoOperCruzada------------------ "+montoOperCruzada);
				
			}
			
			//PARA CRUCE DIVISAS Y LIQUIDACION EFECTIVO
			if(ObjOrden!=null && ObjOrden.getOrdenesCrucesDivisas()!=null && ObjOrden.getOrdenesCrucesDivisas().size()>0){ //Se hallaron cruces tipo divisas para la orden
				
				OrdenesCruce oc = ObjOrden.getOrdenesCrucesDivisas().get(0);
				
				mapa.defaultPut("divisa_nro_bcv", oc.getIdOrdenBcvString());
				mapa.defaultPut("divisa_nro_op", oc.getNroOperacionString());
				mapa.defaultPut("divisa_inst", oc.getIndicadorTitulo()==ConstantesGenerales.VERDADERO?"Titulo":"Efectivo");
				mapa.defaultPut("divisa_fvalor", oc.getFechaValor());
				
				//Se obtiene el monto total cruzado en efectivo
				BigDecimal montoTotal = new BigDecimal(0);
				OrdenesCruce ocSum;
				for(int i=0; i<ObjOrden.getOrdenesCrucesDivisas().size(); i++){
					ocSum = ObjOrden.getOrdenesCrucesDivisas().get(i);
					montoTotal = new BigDecimal(
					Double.valueOf(String.valueOf(montoTotal)) + Double.valueOf(String.valueOf(ocSum.getMontoOperacion())) );
					//montoTotal.add(ocSum.getMontoOperacion());
					Logger.debug(this, "Monto Divisa "+(i+1)+": "+ocSum.getMontoOperacion());
					Logger.debug(this, "Monto Total Divisas: "+montoTotal);
					//Se suma el contravalor en bs del cruce de divisa
					montoOperEfec = new BigDecimal(
							Double.valueOf(String.valueOf(montoOperEfec)) + Double.valueOf(String.valueOf(ocSum.getContravalorBolivaresCapital())) );
				}
				mapa.defaultPut("divisa_mto_cruzado", Utilitario.formatearNumero(String.valueOf(montoTotal.setScale(2, BigDecimal.ROUND_HALF_EVEN)),Utilitario.numeroDecimales)); //ITS-3206 Formato de campos de montos incorrecto en correo SIMADI NM25287 12/07/2016
				Logger.debug(this, "Total Monto Cruzado en Divisas------------------ "+montoTotal.setScale(2, BigDecimal.ROUND_HALF_EVEN));
				
				montoOperEfec.setScale(2, BigDecimal.ROUND_HALF_EVEN);
				
				montoOperCruzada = new BigDecimal(
						Double.valueOf(String.valueOf(montoOperCruzada)) + Double.valueOf(String.valueOf(montoOperEfec)) ).setScale(2, BigDecimal.ROUND_HALF_EVEN);
				
				Logger.debug(this, "TOTAL Contravalor Bs CAPITAL (divisas + titulos)------------------ "+montoOperCruzada);
				
			}else{
				//No existe cruce con divisas
				mapa.defaultPut("divisa_nro_bcv", "");
				mapa.defaultPut("divisa_nro_op", "");
				mapa.defaultPut("divisa_inst", "");
				mapa.defaultPut("divisa_fvalor", "");
				mapa.defaultPut("divisa_mto_cruzado","");
			}
		
		if(ObjOrden!=null){
			ClienteCuentasDAO ccDAO = new ClienteCuentasDAO(this.getDataSource()); 
			ccDAO.browseClienteCuentas(String.valueOf(ObjOrden.getIdCliente()), null, String.valueOf(ObjOrden.getIdOrden()), null);
			
			if(ccDAO.getDataSet().count()>0){
				ccDAO.getDataSet().first();
				ccDAO.getDataSet().next();
				String cta = ccDAO.getDataSet().getValue("CTECTA_NUMERO");
				mapa.defaultPut("cta_divisas", Utilitario.enmascararCta(cta, ConstantesGenerales.cantCaractToShow, ConstantesGenerales.caracterRelleno, ConstantesGenerales.showFromEnd));
			}
		}
		
			
		ordenDAO.listarDetallesEtiquetas(ObjOrden.getIdOrden()); //Obtiene el detalle de la orden
		DataSet _ordenEtiquetas = ordenDAO.getDataSet();
		
		Logger.info(this, "Orden Etiquetasss count: "+_ordenEtiquetas.count());
		
		if(_ordenEtiquetas.count()>0){
		
		_ordenEtiquetas.first();
		_ordenEtiquetas.next();
		
		//Se guardan los valores de las etiquetas en el mapa
		
		//Etiquetas del cliente
		if( _ordenEtiquetas.getValue("CLIENT_NOMBRE")!=null && !_ordenEtiquetas.getValue("CLIENT_NOMBRE").equals("") ) {
			mapa.defaultPut("cliente", (_ordenEtiquetas.getValue("CLIENT_NOMBRE").replaceAll("\\s\\s+", " ")).trim() );
		}else{
			mapa.defaultPut("cliente", "");
		}
		if( _ordenEtiquetas.getValue("TIPPER_ID")!=null && !_ordenEtiquetas.getValue("TIPPER_ID").equals("") && _ordenEtiquetas.getValue("CLIENT_CEDRIF")!=null && !_ordenEtiquetas.getValue("CLIENT_CEDRIF").equals("") ) {
			mapa.defaultPut("cedula", _ordenEtiquetas.getValue("TIPPER_ID").toUpperCase()+_ordenEtiquetas.getValue("CLIENT_CEDRIF") );
		}else{
			mapa.defaultPut("cedula", "");
		}
		if( _ordenEtiquetas.getValue("CLIENT_DIRECCION")!=null && !_ordenEtiquetas.getValue("CLIENT_DIRECCION").equals("") ) {
			mapa.defaultPut("direccion", _ordenEtiquetas.getValue("CLIENT_DIRECCION").replaceAll("\\s\\s+", " ") );
		}else{
			mapa.defaultPut("direccion", "");
		}
		if( _ordenEtiquetas.getValue("CLIENT_TELEFONO")!=null && !_ordenEtiquetas.getValue("CLIENT_TELEFONO").equals("") ) {
			mapa.defaultPut("telefono", _ordenEtiquetas.getValue("CLIENT_TELEFONO") );
		}else{
			mapa.defaultPut("telefono", "");
		}
		if( _ordenEtiquetas.getValue("CLIENT_CORREO_ELECTRONICO")!=null && !_ordenEtiquetas.getValue("CLIENT_CORREO_ELECTRONICO").equals("") ) {
			mapa.defaultPut("correo_electronico", _ordenEtiquetas.getValue("CLIENT_CORREO_ELECTRONICO").trim());
		}else{
			mapa.defaultPut("correo_electronico", "");
		}
		if( _ordenEtiquetas.getValue("CLIENT_EMPLEADO")!=null && !_ordenEtiquetas.getValue("CLIENT_EMPLEADO").equals("") ) {
			mapa.defaultPut("empleado", _ordenEtiquetas.getValue("CLIENT_EMPLEADO"));
		}else{
			mapa.defaultPut("empleado", "");
		}
		if( _ordenEtiquetas.getValue("CLIENT_TIPO")!=null && !_ordenEtiquetas.getValue("CLIENT_TIPO").equals("") ) {
			mapa.defaultPut("tipo_cliente", _ordenEtiquetas.getValue("CLIENT_TIPO"));
		}else{
			mapa.defaultPut("tipo_cliente", "");
		}
		if( _ordenEtiquetas.getValue("TIPPER_NOMBRE")!=null && !_ordenEtiquetas.getValue("TIPPER_NOMBRE").equals("") ) {
			mapa.defaultPut("tipo_persona", Utilitario.replaceAcentosHTML(_ordenEtiquetas.getValue("TIPPER_NOMBRE")));
		}else{
			mapa.defaultPut("tipo_persona", "");
		}
		
		//Etiquetas de la unidad de inversion
		if( _ordenEtiquetas.getValue("UNDINV_NOMBRE")!=null && !_ordenEtiquetas.getValue("UNDINV_NOMBRE").equals("")) {
			mapa.defaultPut("unidad_inversion", _ordenEtiquetas.getValue("UNDINV_NOMBRE"));
		}else{
			mapa.defaultPut("unidad_inversion", "");
		}
		if( _ordenEtiquetas.getValue("UNDINV_EMISION")!=null && !_ordenEtiquetas.getValue("UNDINV_EMISION").equals("")) {
			mapa.defaultPut("ui_emision", _ordenEtiquetas.getValue("UNDINV_EMISION"));
		}else{
			mapa.defaultPut("ui_emision", "");
		}
		if( _ordenEtiquetas.getValue("UNDINV_SERIE")!=null && !_ordenEtiquetas.getValue("UNDINV_SERIE").equals("")) {
			mapa.defaultPut("ui_serie", _ordenEtiquetas.getValue("UNDINV_SERIE"));
		}else{
			mapa.defaultPut("ui_serie", "");
		}
		if( _ordenEtiquetas.getValue("UNDINV_RENDIMIENTO")!=null && !_ordenEtiquetas.getValue("UNDINV_RENDIMIENTO").equals("")) {
			mapa.defaultPut("ui_porcentaje", _ordenEtiquetas.getValue("UNDINV_RENDIMIENTO"));
		}else{
			mapa.defaultPut("ui_porcentaje", "");
		}
		if( _ordenEtiquetas.getValue("uni_l_dia")!=null && !_ordenEtiquetas.getValue("uni_l_dia").equals("")) {
			mapa.defaultPut("und_dia_liquidacion", _ordenEtiquetas.getValue("uni_l_dia"));
		}else{
			mapa.defaultPut("und_dia_liquidacion", "");
		}
		if( _ordenEtiquetas.getValue("uni_l_mes")!=null && !_ordenEtiquetas.getValue("uni_l_mes").equals("")) {
			mapa.defaultPut("und_mes_liquidacion", _ordenEtiquetas.getValue("uni_l_mes"));
		}else{
			mapa.defaultPut("und_mes_liquidacion", "");
		}
		if( _ordenEtiquetas.getValue("uni_l_nombre_mes")!=null && !_ordenEtiquetas.getValue("uni_l_nombre_mes").equals("")) {
			mapa.defaultPut("und_nombre_mes_liquida", _ordenEtiquetas.getValue("uni_l_nombre_mes"));
		}else{
			mapa.defaultPut("und_nombre_mes_liquida", "");
		}
		if( _ordenEtiquetas.getValue("uni_l_anio")!=null && !_ordenEtiquetas.getValue("uni_l_anio").equals("")) {
			mapa.defaultPut("und_anio_liquidacion", _ordenEtiquetas.getValue("uni_l_anio"));
		}else{
			mapa.defaultPut("und_anio_liquidacion", "");
		}
		if( _ordenEtiquetas.getValue("c_dia")!=null && !_ordenEtiquetas.getValue("c_dia").equals("")) {
			mapa.defaultPut("und_dia_cierre", _ordenEtiquetas.getValue("c_dia"));
		}else{
			mapa.defaultPut("und_dia_cierre", "");
		}
		if( _ordenEtiquetas.getValue("c_mes")!=null && !_ordenEtiquetas.getValue("c_mes").equals("")) {
			mapa.defaultPut("und_mes_cierre", _ordenEtiquetas.getValue("c_mes"));
		}else{
			mapa.defaultPut("und_mes_cierre", "");
		}
		if( _ordenEtiquetas.getValue("c_nombre_mes")!=null && !_ordenEtiquetas.getValue("c_nombre_mes").equals("")) {
			mapa.defaultPut("und_nombre_mes_cierre", _ordenEtiquetas.getValue("c_nombre_mes"));
		}else{
			mapa.defaultPut("und_nombre_mes_cierre", "");
		}
		if( _ordenEtiquetas.getValue("c_anio")!=null && !_ordenEtiquetas.getValue("c_anio").equals("")) {
			mapa.defaultPut("und_anio_cierre", _ordenEtiquetas.getValue("c_anio"));
		}else{
			mapa.defaultPut("und_anio_cierre", "");
		}
		if( _ordenEtiquetas.getValue("e_dia")!=null && !_ordenEtiquetas.getValue("e_dia").equals("")) {
			mapa.defaultPut("und_dia_emision", _ordenEtiquetas.getValue("e_dia"));
		}else{
			mapa.defaultPut("und_dia_emision", "");
		}
		if( _ordenEtiquetas.getValue("e_mes")!=null && !_ordenEtiquetas.getValue("e_mes").equals("")) {
			mapa.defaultPut("und_mes_emision", _ordenEtiquetas.getValue("e_mes"));
		}else{
			mapa.defaultPut("und_mes_emision", "");
		}
		if( _ordenEtiquetas.getValue("e_nombre_mes")!=null && !_ordenEtiquetas.getValue("e_nombre_mes").equals("")) {
			mapa.defaultPut("und_nombre_mes_emision", _ordenEtiquetas.getValue("e_nombre_mes"));
		}else{
			mapa.defaultPut("und_nombre_mes_emision", "");
		}
		if( _ordenEtiquetas.getValue("e_anio")!=null && !_ordenEtiquetas.getValue("e_anio").equals("")) {
			mapa.defaultPut("und_anio_emision", _ordenEtiquetas.getValue("e_anio"));
		}else{
			mapa.defaultPut("und_anio_emision", "");
		}
		
		//Etiquetas de la orden
		String nro_orden = _ordenEtiquetas.getValue("ordene_id");
		//NM29643 Se agrega el campo orden_nro
		if( nro_orden!=null && !nro_orden.equals("")) {
//			mapa.defaultPut("operacion", nro_orden);
			mapa.defaultPut("orden_nro", nro_orden);
		}else{
//			mapa.defaultPut("operacion", "");
			mapa.defaultPut("orden_nro", "");
		}
		if( _ordenEtiquetas.getValue("ctecta_numero")!=null && !_ordenEtiquetas.getValue("ctecta_numero").equals("")) {
			String cta = _ordenEtiquetas.getValue("ctecta_numero");
			mapa.defaultPut("cta_bs", Utilitario.enmascararCta(cta, ConstantesGenerales.cantCaractToShow, ConstantesGenerales.caracterRelleno, ConstantesGenerales.showFromEnd));
		}else{
			mapa.defaultPut("cta_bs", "");
		}
		//ITS-3206 Formato de campos de montos incorrecto en correo SIMADI NM25287 12/07/2016
		String porcentajeComision=_ordenEtiquetas.getValue("comision_pct");
		if(porcentajeComision==null || porcentajeComision.equals("")) {
			porcentajeComision="0";
		}
		//if( _ordenEtiquetas.getValue("comision_pct")!=null && !_ordenEtiquetas.getValue("comision_pct").equals("")) {
		mapa.defaultPut("comision_pct", Utilitario.formatearNumero(String.valueOf(new BigDecimal(porcentajeComision).setScale(2, BigDecimal.ROUND_HALF_UP)),Utilitario.numeroDecimales));
		
		//NM29643 - infi_TTS_466 17/10/2014 Incidencia 5 Calidad
		//Se le suma la comision al contravalor total en bs de la operacion
		BigDecimal comDec = new BigDecimal(porcentajeComision).setScale(2, BigDecimal.ROUND_HALF_UP).divide(new BigDecimal(100));
		BigDecimal mtoCom = montoOperCruzada.multiply(comDec).setScale(2, BigDecimal.ROUND_HALF_UP);
		montoOperCruzada = montoOperCruzada.add(mtoCom).setScale(2, BigDecimal.ROUND_HALF_UP);
		mapa.defaultPut("monto_com_adjudicado", Utilitario.formatearNumero(String.valueOf(mtoCom),Utilitario.numeroDecimales));
		mapa.defaultPut("monto_oper_adjudicado", Utilitario.formatearNumero(String.valueOf(montoOperCruzada),Utilitario.numeroDecimales));
		Logger.debug(this, "---------TOTAL Contravalor Bs CON COMISION (divisas + titulos)------------------ "+montoOperCruzada);
		Logger.debug(this, "---------Contravalor COMISION ------------------ "+mtoCom);
		
		//Se le suma la comision al contravalor titulos en bs de la operacion
		mtoCom = montoOperTit.multiply(comDec).setScale(2, BigDecimal.ROUND_HALF_UP);
		montoOperTit = montoOperTit.add(mtoCom).setScale(2, BigDecimal.ROUND_HALF_UP);
		mapa.defaultPut("monto_com_adj_tit", Utilitario.formatearNumero(String.valueOf(mtoCom),Utilitario.numeroDecimales)); //ITS-3206 Formato de campos de montos incorrecto en correo SIMADI NM25287 12/07/2016
		mapa.defaultPut("monto_oper_adj_tit", Utilitario.formatearNumero(String.valueOf(montoOperTit),Utilitario.numeroDecimales)); //ITS-3206 Formato de campos de montos incorrecto en correo SIMADI NM25287 12/07/2016
		Logger.debug(this, "---------monto_com_adj_tit------------------ "+montoOperTit);
		Logger.debug(this, "---------monto_oper_adj_tit ------------------ "+mtoCom);
		
		//Se le suma la comision al contravalor efectivo en bs de la operacion
		mtoCom = montoOperEfec.multiply(comDec).setScale(2, BigDecimal.ROUND_HALF_UP);
		montoOperEfec = montoOperEfec.add(mtoCom).setScale(2, BigDecimal.ROUND_HALF_UP);
		mapa.defaultPut("monto_com_adj_div", Utilitario.formatearNumero(String.valueOf(mtoCom),Utilitario.numeroDecimales)); //ITS-3206 Formato de campos de montos incorrecto en correo SIMADI NM25287 12/07/2016
		mapa.defaultPut("monto_oper_adj_div", Utilitario.formatearNumero(String.valueOf(montoOperEfec),Utilitario.numeroDecimales)); //ITS-3206 Formato de campos de montos incorrecto en correo SIMADI NM25287 12/07/2016
		Logger.debug(this, "---------monto_com_adj_div------------------ "+montoOperEfec);
		Logger.debug(this, "---------monto_oper_adj_div ------------------ "+mtoCom);
			
		/*}else{
		mapa.defaultPut("comision_pct", "0");
		mapa.defaultPut("monto_com_adjudicado", "");
		mapa.defaultPut("monto_oper_adjudicado", "");
		mapa.defaultPut("monto_com_adj_tit", "");
		mapa.defaultPut("monto_oper_adj_tit", "");
		mapa.defaultPut("monto_com_adj_div", "");
		mapa.defaultPut("monto_oper_adj_div", "");
		}*/
		
		if( _ordenEtiquetas.getValue("comision_monto_fijo")!=null && !_ordenEtiquetas.getValue("comision_monto_fijo").equals("")) {
			mapa.defaultPut("comision_fija", Utilitario.formatearNumero(_ordenEtiquetas.getValue("comision_monto_fijo"),Utilitario.numeroDecimales)); //ITS-3206 Formato de campos de montos incorrecto en correo SIMADI NM25287 12/07/2016
		}else{
			mapa.defaultPut("comision_fija", "0,00");
		}
		
		String agencia = _ordenEtiquetas.getValue("ordene_usr_sucursal");
		if( agencia!=null && !agencia.equals("")) {
			mapa.defaultPut("agencia", agencia);
		}else{
			mapa.defaultPut("agencia", "");
		}
		BigDecimal monto_total = new BigDecimal(_ordenEtiquetas.getValue("ordene_ped_total")==null?"0":_ordenEtiquetas.getValue("ordene_ped_total"));
		String monto_inversion = _ordenEtiquetas.getValue("ordene_ped_monto");
		BigDecimal intereses_caidos = new BigDecimal(_ordenEtiquetas.getValue("ordene_ped_int_caidos")==null?"0":_ordenEtiquetas.getValue("ordene_ped_int_caidos"));
		BigDecimal monto_comisiones = new BigDecimal(_ordenEtiquetas.getValue("ordene_ped_comisiones")==null?"0":_ordenEtiquetas.getValue("ordene_ped_comisiones"));
		String monto_adjudicado = _ordenEtiquetas.getValue("ordene_adj_monto");
		String precio_pedido = _ordenEtiquetas.getValue("ordene_ped_precio");
		BigDecimal montoCapital = new BigDecimal(0);
		montoCapital = montoCapital.add(monto_total);
		montoCapital = montoCapital.subtract(monto_comisiones);
		//nm29643 inif_TTS_466_Calidad Se condiciona la asignacion de las etiquetas monto operacion y monto capital
		Logger.debug(this, "----------------------------TIPO DE PRODUCTO: "+ObjOrden.getTipoProducto());
		if(ObjOrden.getTipoProducto().equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL)){
			mapa.defaultPut("monto_capital", Utilitario.formatearNumero(monto_total.toString(),Utilitario.numeroDecimales) );
			BigDecimal montoOperacion = new BigDecimal(0);
			montoOperacion = montoOperacion.add(monto_total);
			montoOperacion = montoOperacion.add(monto_comisiones);
			mapa.defaultPut("monto_operacion", Utilitario.formatearNumero(montoOperacion.toString(),Utilitario.numeroDecimales));
		}else{
			if(ObjOrden.getTipoProducto().equals(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL)){
				mapa.defaultPut("monto_capital", Utilitario.formatearNumero(String.valueOf(montoCapital),Utilitario.numeroDecimales) );
				mapa.defaultPut("monto_operacion", Utilitario.formatearNumero(monto_total.toString(),Utilitario.numeroDecimales));
			}
		}
		
		mapa.defaultPut("monto_comisiones", Utilitario.formatearNumero(monto_comisiones.toString(),Utilitario.numeroDecimales));
		mapa.defaultPut("monto_intereses", Utilitario.formatearNumero(intereses_caidos.toString(),Utilitario.numeroDecimales));
		mapa.defaultPut("monto_adjudicado", Utilitario.formatearNumero(monto_adjudicado==null||monto_adjudicado.equals("")?"0":monto_adjudicado.toString(),Utilitario.numeroDecimales));
		if( monto_inversion!=null && !monto_inversion.equals("")) {
			mapa.defaultPut("monto_inversion", Utilitario.formatearNumero(monto_inversion,Utilitario.numeroDecimales));
		}else{
			mapa.defaultPut("monto_inversion", "");
		}
		//NM29643 Se agrega el campo orden_precio_pedido
//		if( precio_pedido!=null && !precio_pedido.equals("")) {
//			mapa.defaultPut("orden_precio_pedido", precio_pedido+"");
//		}else{
//			mapa.defaultPut("orden_precio_pedido", "");
//		}
		if( precio_pedido!=null && !precio_pedido.equals("")) {
			mapa.defaultPut("precio", precio_pedido);
		}else{
			mapa.defaultPut("precio", "");
		}
		if( _ordenEtiquetas.getValue("tomador")!=null && !_ordenEtiquetas.getValue("tomador").equals("")) {
			mapa.defaultPut("vehiculo_nombre", _ordenEtiquetas.getValue("tomador"));
		}else{
			mapa.defaultPut("vehiculo_nombre", "");
		}
		if( _ordenEtiquetas.getValue("colocador")!=null && !_ordenEtiquetas.getValue("colocador").equals("")) {
			mapa.defaultPut("empresa_emisora", _ordenEtiquetas.getValue("colocador"));
		}else{
			mapa.defaultPut("empresa_emisora", "");
		}
		if( _ordenEtiquetas.getValue("l_dia")!=null && !_ordenEtiquetas.getValue("l_dia").equals("")) {
			mapa.defaultPut("dia_liquidacion", _ordenEtiquetas.getValue("l_dia"));
		}else{
			mapa.defaultPut("dia_liquidacion", "");
		}
		if( _ordenEtiquetas.getValue("l_mes")!=null && !_ordenEtiquetas.getValue("l_mes").equals("")) {
			mapa.defaultPut("mes_liquidacion", _ordenEtiquetas.getValue("l_mes"));
		}else{
			mapa.defaultPut("mes_liquidacion", "");
		}
		if(_ordenEtiquetas.getValue("l_nombre_mes")!=null && !_ordenEtiquetas.getValue("l_nombre_mes").equals("")) {
			mapa.defaultPut("nombre_mes_liquidacion", _ordenEtiquetas.getValue("l_nombre_mes"));
		}else{
			mapa.defaultPut("nombre_mes_liquidacion", "");
		}
		if( _ordenEtiquetas.getValue("l_anio")!=null && !_ordenEtiquetas.getValue("l_anio").equals("")) {
			mapa.defaultPut("anio_liquidacion", _ordenEtiquetas.getValue("l_anio"));
		}else{
			mapa.defaultPut("anio_liquidacion", "");
		}
		if( _ordenEtiquetas.getValue("o_dia")!=null && !_ordenEtiquetas.getValue("o_dia").equals("")) {
			mapa.defaultPut("dia_orden", _ordenEtiquetas.getValue("o_dia"));
		}else{
			mapa.defaultPut("dia_orden", "");
		}
		if( _ordenEtiquetas.getValue("o_mes")!=null && !_ordenEtiquetas.getValue("o_mes").equals("")) {
			mapa.defaultPut("mes_orden", _ordenEtiquetas.getValue("o_mes"));
		}else{
			mapa.defaultPut("mes_orden", "");
		}
		if(_ordenEtiquetas.getValue("o_nombre_mes")!=null && !_ordenEtiquetas.getValue("o_nombre_mes").equals("")) {
			mapa.defaultPut("nombre_mes_orden", _ordenEtiquetas.getValue("o_nombre_mes"));
		}else{
			mapa.defaultPut("nombre_mes_orden", "");
		}
		if( _ordenEtiquetas.getValue("o_anio")!=null && !_ordenEtiquetas.getValue("o_anio").equals("")) {
			mapa.defaultPut("anio_orden", _ordenEtiquetas.getValue("o_anio"));
		}else{
			mapa.defaultPut("anio_orden", "");
		}
		if( _ordenEtiquetas.getValue("a_dia")!=null && !_ordenEtiquetas.getValue("a_dia").equals("")) {
			mapa.defaultPut("dia_adjudicacion", _ordenEtiquetas.getValue("a_dia"));
		}else{
			mapa.defaultPut("dia_adjudicacion", "");
		}
		if( _ordenEtiquetas.getValue("a_mes")!=null && !_ordenEtiquetas.getValue("a_mes").equals("")) {
			mapa.defaultPut("mes_adjudicacion", _ordenEtiquetas.getValue("a_mes"));
		}else{
			mapa.defaultPut("mes_adjudicacion", "");
		}
		if(_ordenEtiquetas.getValue("a_nombre_mes")!=null && !_ordenEtiquetas.getValue("a_nombre_mes").equals("")) {
			mapa.defaultPut("nombre_mes_adjudicacion", _ordenEtiquetas.getValue("a_nombre_mes"));
		}else{
			mapa.defaultPut("nombre_mes_adjudicacion", "");
		}
		if( _ordenEtiquetas.getValue("a_anio")!=null && !_ordenEtiquetas.getValue("a_anio").equals("")) {
			mapa.defaultPut("anio_adjudicacion", _ordenEtiquetas.getValue("a_anio"));
		}else{
			mapa.defaultPut("anio_adjudicacion", "");
		}
		if( _ordenEtiquetas.getValue("v_dia")!=null && !_ordenEtiquetas.getValue("v_dia").equals("")) {
			mapa.defaultPut("dia_valor", _ordenEtiquetas.getValue("v_dia"));
		}else{
			mapa.defaultPut("dia_valor", "");
		}
		if( _ordenEtiquetas.getValue("v_mes")!=null && !_ordenEtiquetas.getValue("v_mes").equals("")) {
			mapa.defaultPut("mes_valor", _ordenEtiquetas.getValue("v_mes"));
		}else{
			mapa.defaultPut("mes_valor", "");
		}
		if(_ordenEtiquetas.getValue("v_nombre_mes")!=null && !_ordenEtiquetas.getValue("v_nombre_mes").equals("")) {
			mapa.defaultPut("nombre_mes_valor", _ordenEtiquetas.getValue("v_nombre_mes"));
		}else{
			mapa.defaultPut("nombre_mes_valor", "");
		}
		if( _ordenEtiquetas.getValue("v_anio")!=null && !_ordenEtiquetas.getValue("v_anio").equals("")) {
			mapa.defaultPut("anio_valor", _ordenEtiquetas.getValue("v_anio"));
		}else{
			mapa.defaultPut("anio_valor", "");
		}
		if( _ordenEtiquetas.getValue("CONCEPTO")!=null && !_ordenEtiquetas.getValue("CONCEPTO").equals("")) {
			mapa.defaultPut("concepto", _ordenEtiquetas.getValue("CONCEPTO"));
		}else{
			mapa.defaultPut("concepto", "");
		}
		//Etiquetas del vehículo
//		if( _ordenEtiquetas.getValue("vehicu_nombre")!=null && !_ordenEtiquetas.getValue("vehicu_nombre").equals("")) {
//			mapa.defaultPut("vehiculo_nombre", _ordenEtiquetas.getValue("vehicu_nombre"));
//		}else{
//			mapa.defaultPut("vehiculo_nombre", "");
//		}
		if( _ordenEtiquetas.getValue("vehicu_rif")!=null && !_ordenEtiquetas.getValue("vehicu_rif").equals("")) {
			mapa.defaultPut("vehiculo_rif", _ordenEtiquetas.getValue("vehicu_rif"));
		}else{
			mapa.defaultPut("vehiculo_rif", "");
		}
		//NM29643 infi_TTS_466
		BigDecimal tasaCambio = new BigDecimal(_ordenEtiquetas.getValue("ordene_tasa_pool")==null?"0":_ordenEtiquetas.getValue("ordene_tasa_pool")).setScale(2, BigDecimal.ROUND_HALF_EVEN);
		if( tasaCambio.compareTo(new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_EVEN)) > 0 ) {
			mapa.defaultPut("tasa_cambio", Utilitario.formatearNumero(tasaCambio.toString(),Utilitario.numeroDecimales)); //ITS-3206 Formato de campos de montos incorrecto en correo SIMADI NM25287 12/07/2016
		}else{
			mapa.defaultPut("tasa_cambio", "");
		}
		if( _ordenEtiquetas.getValue("moneda_id")!=null && !_ordenEtiquetas.getValue("moneda_id").equals("")) {
			mapa.defaultPut("moneda", _ordenEtiquetas.getValue("moneda_id"));
		}else{
			mapa.defaultPut("moneda", "");
		}
		
		
		//Etiquetas de las operaciones
		if (!ObjOrden.isOperacionVacio()){
			ArrayList<OrdenOperacion> listaOperaciones = ObjOrden.getOperacion();
			BigDecimal reintegro = new BigDecimal(0);
			for (Iterator<OrdenOperacion> iter = listaOperaciones.iterator(); iter.hasNext(); ) {
				OrdenOperacion oper = (OrdenOperacion) iter.next();
				if(oper.getTipoTransaccionFinanc().equals(TransaccionFinanciera.CREDITO)){
					//Sumando los montos de credito
					reintegro = reintegro.add(oper.getMontoOperacion());			
				}
			}
			//Set del total del monto acreditado
			mapa.defaultPut("reintegro", Utilitario.formatearNumero(reintegro.toString(),Utilitario.numeroDecimales));
		}
		
		//Etiquetas de los campos dinamicos
		if (!ObjOrden.isCampoDinamicoVacio()){
			CamposDinamicos campdin = new CamposDinamicos(this.getDataSource());
			ArrayList<CampoDinamico> listaCamposDinamicos = ObjOrden.getCamposDinamicos();
			Iterator<CampoDinamico> it = listaCamposDinamicos.iterator();
			String in = "";
			//Se recorren los campos dinamicos		
			while (it.hasNext()) {
				//Se arma la condicion del query con todos los id de los campos dinamicos
				CampoDinamico campo = (CampoDinamico) it.next();			
				in += campo.getIdCampo();
				if(it.hasNext()){
					in += ",";
				}
			}
			campdin.listarCamposDinamico(in, ObjOrden.getIdOrden());
			DataSet _campos = campdin.getDataSet();
			if(_campos.count()>0){
				_campos.first();
				while(_campos.next()){
					String nombreCampo = campdin.formateo(_campos.getValue("campo_nombre"),1,1,1," ","_");
					String valorCampo = _campos.getValue("campo_valor");
					if( valorCampo!=null && !valorCampo.equals("")) {
						mapa.defaultPut("cd_"+nombreCampo, valorCampo);
					}else{
						mapa.defaultPut("cd_"+nombreCampo, "");
					}
				}
			}
		}//Hay campos dinamicos
		
		//Rangos blotter
		UIBlotterRangosDAO uiBlotterRangosDAO = new UIBlotterRangosDAO(this.getDataSource());
		uiBlotterRangosDAO.listarBlotterRangos(ObjOrden.getIdUnidadInversion(), ObjOrden.getIdBloter(), ObjOrden.getIdTipoPersona(),0);
		DataSet _blotter= uiBlotterRangosDAO.getDataSet();
		if(_blotter.count()>0){
			_blotter.first();
			_blotter.next();
			String[] transaccion = { _blotter.getValue("uiblot_trnfin_tipo") };
			TipoTransaccionFinancieraDAO tipoTransaccionFinancieraDAO = new TipoTransaccionFinancieraDAO(this.getDataSource());
			tipoTransaccionFinancieraDAO.listaEspecificos(transaccion);
			DataSet _transaccion= tipoTransaccionFinancieraDAO.getDataSet();
			if(_transaccion.count()>0){
				_transaccion.first();
				_transaccion.next();
				if( _transaccion.getValue("trnfin_tipo_descripcion")!=null && !_transaccion.getValue("trnfin_tipo_descripcion").equals("")) {
					mapa.defaultPut("transaccion", Utilitario.replaceAcentosHTML(_transaccion.getValue("trnfin_tipo_descripcion")) );
				}else{
					mapa.defaultPut("transaccion", "");
				}
			}
		}
		//Logger.info(this,"Mapa antes de llamar procesarPlantillassssss:\n"+mapa);
		
		}//se obtienen datos de la orden
		
		}//NO BLOQUE
		
		
		//PROCESAMIENTO DE LA PLANTILLA
		try{
			System.out.println("MAPA antes de procesar plantilla: "+mapa);
			System.out.println("(LinkedList)documentos :"+(LinkedList)documentos);
			procesarPlantillas(mapa, (LinkedList)documentos);
			
		}catch (Exception e) {
			throw e;
		}
		
		
		
	}
	

	/**
	 * Verifica si una operacion ya fue agregada al arraylist de nombres de operaciones
	 * para no ser sumarizada mas de una vez
	 * @param nombreOperacion
	 * @return true si el nombre se encuentra en el array, false en caso contrario
	 */
	private boolean operRepetida(String nombreOperacion) {
		if(!nombresOperComisiones.isEmpty()){
			for(int k=0; k <nombresOperComisiones.size(); k++){
				if(nombresOperComisiones.get(k).equals(nombreOperacion)){
					return true;
				}
			}			
		}
		return false;
	}
		
}
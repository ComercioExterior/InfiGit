package com.bdv.infi.logic.function.document;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.sql.DataSource;
import megasoft.DataSet;
import com.bancovenezuela.comun.data.OrdenOperacion;
import com.bdv.infi.dao.CamposDinamicos;
import com.bdv.infi.dao.ConceptosDAO;
import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.TitulosDAO;
import com.bdv.infi.data.CampoDinamico;
import com.bdv.infi.data.Orden;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionFinanciera;
import com.bdv.infi.logic.interfaces.TransaccionNegocio;
import com.bdv.infi.util.Utilitario;

public class DatosOrden extends DatosGenerales{
	
	public DatosOrden(DataSource ds, Map<String, String> mapa){
		super(ds,mapa);
	}
	
	public void buscarDatos(Object orden, ServletContext contexto, String ip) throws Exception{
		
		Map<String, String> mapaOrden = this.getMapa();
		Orden ObjOrden = (Orden) orden;
		String in = "";
		String instrumentoFormaOrden = null;
		Iterator<CampoDinamico> it =null;
		OrdenDAO ordenDAO = new OrdenDAO(this.getDataSource());
		//1.- DATOS GENERALES DE LA ORDEN
		ordenDAO.listarDetalles(ObjOrden.getIdOrden());
		DataSet _orden= ordenDAO.getDataSet();
		BigDecimal divisor = new BigDecimal(100);
		if(_orden.count()>0){
			_orden.first();
			while (_orden.next()){
				String nro_orden= _orden.getValue("ordene_id");
				instrumentoFormaOrden = _orden.getValue("insfin_forma_orden");
				BigDecimal monto_total= new BigDecimal(_orden.getValue("ordene_ped_total")==null?"0":_orden.getValue("ordene_ped_total"));
				String monto_inversion=_orden.getValue("ordene_ped_monto");
				BigDecimal intereses_caidos=new BigDecimal(_orden.getValue("ordene_ped_int_caidos")==null?"0":_orden.getValue("ordene_ped_int_caidos"));
				BigDecimal monto_comisiones=new BigDecimal(_orden.getValue("ordene_ped_comisiones")==null?"0":_orden.getValue("ordene_ped_comisiones"));
				BigDecimal montoCapital = new BigDecimal(0);
				mapaOrden.put("cuenta_orden",_orden.getValue("CTECTA_NUMERO"));				
				
				//Agregado EL ****************************************************************************
				montoCapital = montoCapital.add(monto_total);
				montoCapital = montoCapital.subtract(monto_comisiones);
				mapaOrden.put("monto_capital",Utilitario.formatearNumero(String.valueOf(montoCapital),Utilitario.numeroDecimales));
				//Agregado EL ****************************************************************************
				
				
				String monto_adjudicado=_orden.getValue("ordene_adj_monto");
				String precio_pedido=_orden.getValue("ordene_ped_precio");
				String agencia = _orden.getValue("ordene_usr_sucursal");
				mapaOrden.put("agencia",agencia);
				mapaOrden.put("operacion",nro_orden);
				mapaOrden.put("monto_comisiones",Utilitario.formatearNumero(monto_comisiones.toString(),Utilitario.numeroDecimales));
				mapaOrden.put("monto_operacion",Utilitario.formatearNumero(monto_total.toString(),Utilitario.numeroDecimales));
				mapaOrden.put("monto_inversion",Utilitario.formatearNumero(monto_inversion,Utilitario.numeroDecimales));
				mapaOrden.put("monto_intereses",Utilitario.formatearNumero(intereses_caidos.toString(),Utilitario.numeroDecimales));
				mapaOrden.put("vehiculo_nombre",_orden.getValue("tomador"));
				mapaOrden.put("monto_adjudicado",Utilitario.formatearNumero(monto_adjudicado==null||monto_adjudicado.equals("")?"0":monto_adjudicado.toString(),Utilitario.numeroDecimales));
				mapaOrden.put("precio",precio_pedido);
				mapaOrden.put("empresa_emisora",_orden.getValue("colocador"));
				mapaOrden.put("tasa_propuesta",_orden.getValue("ordene_tasa_cambio"));
				
				//fecha liquidacion
				if (_orden.getValue("l_anio")!=null||_orden.getValue("l_mes")!=null||_orden.getValue("l_nombre_mes")!=null||_orden.getValue("l_dia")!=null){
					mapaOrden.put("dia_liquidacion",_orden.getValue("l_dia"));
					mapaOrden.put("mes_liquidacion",_orden.getValue("l_mes"));
					mapaOrden.put("nombre_mes_liquidacion",Utilitario.nombreMes(Integer.parseInt(_orden.getValue("l_mes"))));
					mapaOrden.put("anio_liquidacion",_orden.getValue("l_anio"));
				}else{				
					mapaOrden.put("dia_liquidacion","");
					mapaOrden.put("mes_liquidacion","");
					mapaOrden.put("nombre_mes_liquidacion","");
					mapaOrden.put("anio_liquidacion","");
				}
				
				//fecha realizada la orden
				if (_orden.getValue("o_anio")!=null||_orden.getValue("o_mes")!=null||_orden.getValue("o_nombre_mes")!=null||_orden.getValue("o_dia")!=null){
					mapaOrden.put("dia_orden",_orden.getValue("o_dia"));
					mapaOrden.put("mes_orden",_orden.getValue("o_mes"));
					mapaOrden.put("nombre_mes_orden",Utilitario.nombreMes(Integer.parseInt(_orden.getValue("o_mes"))));
					mapaOrden.put("anio_orden",_orden.getValue("o_anio"));
				}else{
					mapaOrden.put("dia_orden","");
					mapaOrden.put("mes_orden","");
					mapaOrden.put("nombre_mes_orden","");
					mapaOrden.put("anio_orden","");
				}
				
				//fecha adjudicacion
				if (_orden.getValue("a_anio")!=null||_orden.getValue("a_mes")!=null||_orden.getValue("a_nombre_mes")!=null||_orden.getValue("a_dia")!=null){
					mapaOrden.put("dia_adjudicacion",_orden.getValue("a_dia"));
					mapaOrden.put("mes_adjudicacion",_orden.getValue("a_mes"));
					mapaOrden.put("nombre_mes_adjudicacion",Utilitario.nombreMes(Integer.parseInt(_orden.getValue("a_mes"))));
					mapaOrden.put("anio_adjudicacion",_orden.getValue("a_anio"));
				}else{
					mapaOrden.put("dia_adjudicacion","");
					mapaOrden.put("mes_adjudicacion","");
					mapaOrden.put("nombre_mes_adjudicacion","");
					mapaOrden.put("anio_adjudicacion","");
				}
				
				//fecha valor
				if (_orden.getValue("v_anio")!=null||_orden.getValue("v_mes")!=null||_orden.getValue("v_nombre_mes")!=null||_orden.getValue("v_dia")!=null){
					mapaOrden.put("dia_valor",_orden.getValue("v_dia"));
					mapaOrden.put("mes_valor",_orden.getValue("v_mes"));
					mapaOrden.put("nombre_mes_valor",Utilitario.nombreMes(Integer.parseInt(_orden.getValue("v_mes"))));
					mapaOrden.put("anio_valor",_orden.getValue("v_anio"));
				}else{
					mapaOrden.put("dia_valor","");
					mapaOrden.put("mes_valor","");
					mapaOrden.put("nombre_mes_valor","");
					mapaOrden.put("anio_valor","");
				}
				
				
				//Buscar descripción del concepto
				if(_orden.getValue("concepto_id")!=null){
					//Concepto Orden
					ConceptosDAO conceptosDAO = new ConceptosDAO(this.getDataSource());
				
					conceptosDAO.listar(_orden.getValue("concepto_id"));		
					if(conceptosDAO.getDataSet().next()){						
						mapaOrden.put("concepto", conceptosDAO.getDataSet().getValue("concepto"));				
					}						
				}else{
					mapaOrden.put("concepto", "");
				}

			}
 		} //fin if
		
		//2.- DATOS DE LOS TITULOS
			TitulosDAO titu = new TitulosDAO(this.getDataSource());
			if(ObjOrden.getIdUnidadInversion()==0){
				titu.listaTituloVentaOrden(ObjOrden.getIdOrden());
			}else{
				titu.listaTitulosOrden(ObjOrden.getIdOrden());
			}
			DataSet _titulos= titu.getDataSet();
			if(_titulos.count()>0){
				_titulos.first();
				while (_titulos.next()){
					String id=_titulos.getValue("titulo");
					String nombreTitulo=titu.formateo(id,0,0,1,null,null);
					String base=_titulos.getValue("basis");
					String dia=_titulos.getValue("dia");
					String mes=_titulos.getValue("mes");
					String nombre_mes=_titulos.getValue("nombre_mes");
					String anio=_titulos.getValue("anio");
					String moneda_neg=_titulos.getValue("titulo_moneda_neg");
					String monto=_titulos.getValue("titulo_unidades");
					BigDecimal pct_recompra = new BigDecimal(_titulos.getValue("titulo_pct_recompra")==null?"0":_titulos.getValue("titulo_pct_recompra"));
					
					if (_titulos.getValue("transa_id").equals(TransaccionNegocio.VENTA_TITULOS)){
						String moneda_pag=_titulos.getValue("moneda_id");
						BigDecimal tituloTasaCambio = new BigDecimal(_titulos.getValue("ordene_tasa_cambio"));
						mapaOrden.put("titulo_base_calculo",base);
						mapaOrden.put("titulo_dia_vencimiento",dia);
						mapaOrden.put("titulo_nombre_mes_vencimiento",nombre_mes);
						mapaOrden.put("titulo_mes_vencimiento",mes);
						mapaOrden.put("titulo_anio_vencimiento",anio);
						mapaOrden.put("titulo_moneda_neg",moneda_neg);
						mapaOrden.put("titulo_moneda_pago",moneda_pag);
						mapaOrden.put("titulo_nombre",nombreTitulo);
						mapaOrden.put("titulo_precio_recompra",pct_recompra.toString());
						mapaOrden.put("titulo_tasa_cambio",tituloTasaCambio.toString());
						mapaOrden.put("titulo_unidades_vendidas",monto);
					}else{
										
						String porcentaje=_titulos.getValue("titulo_pct");
						
						String tasa=_titulos.getValue("couprate_8");
						
						BigDecimal precio=new BigDecimal(_titulos.getValue("ordene_ped_precio")==null?"0":_titulos.getValue("ordene_ped_precio"));										
						BigDecimal pct=new BigDecimal(_titulos.getValue("titulo_pct")==null?"0":_titulos.getValue("titulo_pct"));//Porcentaje del titulo almomento de la compra
						BigDecimal calculo = pct.divide(divisor);//Porcentaje expresado en decimales
						BigDecimal tasaCambio = new BigDecimal(_titulos.getValue("undinv_tasa_cambio"));
						BigDecimal montoSolicitado = new BigDecimal(_titulos.getValue("ordene_ped_monto")==null?"0":_titulos.getValue("ordene_ped_monto"));
						BigDecimal valorSolicitado = montoSolicitado.multiply(calculo);				
						//Calculos para el monto Adjudicado
						BigDecimal montoAdjudicado = new BigDecimal(0);
						BigDecimal valorAdjudicado = new BigDecimal(0);
						if(_titulos.getValue("ordene_adj_monto")!=null && !_titulos.getValue("ordene_adj_monto").equals(ConstantesGenerales.FALSO)){
							montoAdjudicado = new BigDecimal(_titulos.getValue("ordene_adj_monto"));
							valorAdjudicado = montoAdjudicado.multiply(calculo);
						}
						//Calculo para el valor adjudicado en Bs
						BigDecimal contravalor = valorAdjudicado.multiply(tasaCambio);
						
						//Se multiplica  contravalor con el precio
						BigDecimal precioPorcentaje = precio.divide(new BigDecimal(100));
						contravalor = contravalor.multiply(precioPorcentaje);

						//Intereses Caidos
						BigDecimal interesesCaidos = new BigDecimal(_titulos.getValue("titulo_mto_int_caidos")==null?"0":_titulos.getValue("titulo_mto_int_caidos"));
						
						//Titulos pactados para recompra
						
						BigDecimal montoRecompra = new BigDecimal(_titulos.getValue("titulo_monto")==null?"0":_titulos.getValue("titulo_monto"));
						BigDecimal totalRecompra = new BigDecimal(0);
											
						if(pct_recompra.compareTo(new BigDecimal(0))==ConstantesGenerales.VERDADERO){
							BigDecimal porcentaje_rc = pct_recompra.divide(divisor);
							totalRecompra = montoRecompra.multiply(porcentaje_rc);
							mapaOrden.put("recompra_titulo_"+id,nombreTitulo);
							mapaOrden.put("recompra_titulo_"+id+"_pct",pct_recompra.toString());
							mapaOrden.put("recompra_titulo_"+id+"_monto",Utilitario.formatearNumero(montoRecompra.toString(),Utilitario.numeroDecimales));
							mapaOrden.put("recompra_titulo_"+id+"_total",Utilitario.formatearNumero(totalRecompra.toString(),Utilitario.numeroDecimales));
						}else{
							mapaOrden.put("recompra_titulo_"+id,"");
							mapaOrden.put("recompra_titulo_"+id+"_pct","");
							mapaOrden.put("recompra_titulo_"+id+"_monto","");
							mapaOrden.put("recompra_titulo_"+id+"_total","");
						}
						
						
						
						//Valores TOMA DE ORDEN
						mapaOrden.put("titulo_"+id,nombreTitulo);
						mapaOrden.put("precio_"+id,precio.toString());
						mapaOrden.put("monto_titulo_"+id,Utilitario.formatearNumero(monto,Utilitario.numeroDecimales));
						mapaOrden.put("titulo_"+id+"_monto_pedido",valorSolicitado.toString());
						mapaOrden.put("titulo_"+id+"_monto_adjudicado",Utilitario.formatearNumero(valorAdjudicado.toString(),Utilitario.numeroDecimales));
						mapaOrden.put("titulo_"+id+"_contra_valor",contravalor.setScale(2,BigDecimal.ROUND_HALF_EVEN).toString());
						mapaOrden.put("titulo_"+id+"_intereses_caidos",interesesCaidos.toString());
						
						
						//Valores ADJUDICACION
						mapaOrden.put("tasa_cambio",tasaCambio.toString());
						mapaOrden.put("porcentaje_titulo_"+id,porcentaje);
						mapaOrden.put("base_calculo_titulo_"+id,base);
						mapaOrden.put("tasa_cupon_titulo_"+id,tasa);
						mapaOrden.put("moneda_titulo_"+id,moneda_neg);
						mapaOrden.put("dia_vencimiento_titulo_"+id,dia);
						mapaOrden.put("mes_vencimiento_titulo_"+id,mes);
						mapaOrden.put("nombre_mes_vencimiento_titulo_"+id,nombre_mes);
						mapaOrden.put("anio_vencimiento_titulo_"+id,anio);
						
						//buscamos mas informacion solo en caso de que la unidad de Inversion sea tipo INVENTARIO O INVENTARIO CON PRECIO
						if (instrumentoFormaOrden.equals(ConstantesGenerales.INST_TIPO_INVENTARIO)||instrumentoFormaOrden.equals(ConstantesGenerales.INST_TIPO_INVENTARIO_CON_PRECIO)){
							//Dias al vencimiento
							mapaOrden.put("titulo_"+id+"_dias_al_vencimiento",_titulos.getValue("diasal_vencimiento"));
							
							//Dias de intereses transcurridos
							mapaOrden.put("titulo_"+id+"_dias_int_transcurridos",_titulos.getValue("dias_interes_transcurridos"));
						}
					}
				}
	 		} //fin if
		
		//3.- DATOS DE LOS CAMPOS DINAMICOS
		
		if (!ObjOrden.isCampoDinamicoVacio()){
			CamposDinamicos campdin = new CamposDinamicos(this.getDataSource());
			ArrayList<CampoDinamico> listaCamposDinamicos = ObjOrden.getCamposDinamicos();
			it = listaCamposDinamicos.iterator();
			in = "";
			//Se recorren los campos dinamicos		
			while (it.hasNext()) {
				//Armamos la condicion del query con todos los id de los campos dinamicos
				CampoDinamico campo = (CampoDinamico) it.next();			
				in+=campo.getIdCampo();
				if(it.hasNext()){
					in+=",";
				}
			}	
		
			campdin.listarCamposDinamico(in,ObjOrden.getIdOrden());
			DataSet _campos= campdin.getDataSet();
			if(_campos.count()>0){
				_campos.first();
				while(_campos.next()){
					String nombreCampo=campdin.formateo(_campos.getValue("campo_nombre"),1,1,1," ","_");
					String valorCampo=_campos.getValue("campo_valor");
					mapaOrden.put("cd_"+nombreCampo,valorCampo);
				}
			}
		}//fin if isVacio
		
		//4.- DATOS DE LAS OPERACIONES
		
		if (!ObjOrden.isOperacionVacio()){
			ArrayList<OrdenOperacion> listaOperaciones = ObjOrden.getOperacion();
			//BigDecimal monto_capital = new BigDecimal(0);
			BigDecimal reintegro = new BigDecimal(0);
			
			for (Iterator<OrdenOperacion> iter = listaOperaciones.iterator(); iter.hasNext(); ) {
				OrdenOperacion oper = (OrdenOperacion) iter.next();
				if(oper.getTipoTransaccionFinanc().equals(TransaccionFinanciera.CREDITO)){
					
					//Sumando los montos de credito
					reintegro = reintegro.add(oper.getMontoOperacion());			
				}
				//if(oper.getInComision()==ConstantesGenerales.FALSO){
					//monto_capital = monto_capital.add(oper.getMontoOperacion());
				//}
			}
			
			//Set del total del monto acreditado
			mapaOrden.put("reintegro",Utilitario.formatearNumero(reintegro.toString(),Utilitario.numeroDecimales));
			
			//mapaOrden.put("monto_capital",Utilitario.formatearNumero(monto_capital.toString(),Utilitario.numeroDecimales));
		}
		
		//5 Datos del vehículo
		DatosVehiculo datosVehiculo = new DatosVehiculo(this.getDataSource(),this.getMapa());
		datosVehiculo.buscarDatos(ObjOrden);

	}
}
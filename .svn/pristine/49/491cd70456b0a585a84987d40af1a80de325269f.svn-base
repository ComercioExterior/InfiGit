package com.bdv.infi_toma_orden.util;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.sql.DataSource;

import com.bdv.infi_toma_orden.dao.TitulosDAO;


/**
 * Clase utilitaria que nos ofrece m&eacute;todos a ser utilizados en todo el sistema
 * @author jal, nev
 *
 */
public class Utilitario extends com.bdv.infi.util.Utilitario{
	
	
		/**
		 * M&eacute;todo para realizar el calculo de intereses para el pago de cupones
		 * @param fechaUltPagCupon,fechaGuiaPagCupon,base,int valor es 1 cuando se realiza el calculo para intereses caidos,otro valor para pago de cupones
		 * @return BigDecimal
		 * @throws Exception
		 */	
		public static BigDecimal cuponesDiferenciaBaseDias(String fePagUltCupon,String fePagGuiaCupon,String base,int valor) throws Exception{
			//Declaracion de variables
			BigDecimal diferenciaDias				=new BigDecimal(0);
			int mes									=0;
			int annio								=0;
			int diferenciaDiaMay					=0;
			int mesesResta							=30;
			String nuevaFecha						="";
			BigDecimal basePorDiferencia			=new BigDecimal(0);
			BigDecimal totalDiferencia				=new BigDecimal(0);
			SimpleDateFormat formato				=new SimpleDateFormat("yyyy-MM-dd");
			Date fechadesde							=formato.parse(fePagUltCupon);
			Date fechahasta							=formato.parse(fePagGuiaCupon);
			String baseCalculo						=base.trim();
			GregorianCalendar fePagUltCuponMeses	=new GregorianCalendar();
			GregorianCalendar fePagGuiaCuponMeses	=new GregorianCalendar();
			fePagUltCuponMeses.setTime(fechadesde);
			fePagGuiaCuponMeses.setTime(fechahasta);
			int mesFePagUltCupon 					=fePagUltCuponMeses.get(Calendar.MONTH);
			int mesfePagGuiaCuponMeses 				=fePagGuiaCuponMeses.get(Calendar.MONTH);
			int anniofePagUltCuponMeses 			=fePagUltCuponMeses.get(Calendar.YEAR);
			int anniofePagGuiaCuponMeses 			=fePagGuiaCuponMeses.get(Calendar.YEAR);
			int diaFePagUltCupon 					=fePagUltCuponMeses.get(Calendar.DATE);
			int diaFePagUltCuponVer 				=fePagUltCuponMeses.get(Calendar.DATE);//verificar ultimo de mes
			int diaFePagGuiaCupon					=fePagGuiaCuponMeses.get(Calendar.DATE);
			int meses								=diferenciaMeses(fePagUltCuponMeses, fePagGuiaCuponMeses)*30;
			//	 Calcula la base por diferencia cuando la BASE es BASE_A365
			if(baseCalculo.equals(BASE_A365)){
				diferenciaDias		=new BigDecimal(fechasDiferenciaEnDias(fechadesde, fechahasta));
				if(valor!=1){
					diferenciaDias		=diferenciaDias.divide(new BigDecimal(365),7,BigDecimal.ROUND_HALF_EVEN);
				}
				totalDiferencia		=diferenciaDias;

			//	Calcula la base por diferencia cuando la BASE es BASE_A360		
			}else if(baseCalculo.equals(BASE_A360)){
				diferenciaDias		=new BigDecimal(fechasDiferenciaEnDias(fechadesde, fechahasta));
				if(valor!=1){
					diferenciaDias		=diferenciaDias.divide(new BigDecimal(360),7,BigDecimal.ROUND_HALF_EVEN);
				}
				totalDiferencia		=diferenciaDias;
			//	Calcula la base por diferencia cuando la BASE es BASE_EBOND o BASE_30F360	o BASE_BOND			
			}else if(baseCalculo.equals(BASE_EBOND) || baseCalculo.equals(BASE_30F360) || baseCalculo.equals(BASE_BOND)){
				String cero="";
				if(diaFePagGuiaCupon==31){//Se verifica que los meses esten en base a 30 dias
					diaFePagGuiaCupon	=30;
				}
				if(String.valueOf(mes).length()==1){
					cero="0";
				}
			//En el caso de que el dia de la fechaPagUltCupon sea mayor a fePagoGuiaCupon se asigna una nueva fecha
				mes 				=1+fePagGuiaCuponMeses.get(Calendar.MONTH);
				annio 				=fePagGuiaCuponMeses.get(Calendar.YEAR);
				nuevaFecha			=String.valueOf(annio).concat("-").concat(String.valueOf(mes)).concat("-").concat(String.valueOf(diaFePagUltCupon));
				fechadesde			=formato.parse(nuevaFecha);
			//Fin
				if(fechadesde.compareTo(fechahasta)==1){
					nuevaFecha			=String.valueOf(diaFePagUltCupon).concat("-").concat(String.valueOf(mes-1)).concat("-").concat(String.valueOf(annio));
					fechadesde			=StringToDate(nuevaFecha,"dd-MM-yyyy");
					//Se obtienen los dias del mes para sumarizar
					int diaFeHasta=fePagGuiaCuponMeses.get(Calendar.DATE);
					if(diaFePagUltCupon==30){diferenciaDiaMay=diferenciaDiaMay-1;}
					if(diaFePagUltCupon==31){diaFePagUltCupon=30;diferenciaDiaMay=diferenciaDiaMay-1;}
					diferenciaDiaMay			=mesesResta-diaFePagUltCupon+diaFeHasta+meses;
					int ultimoMesUltCupon		=diasDelMes(mesFePagUltCupon, anniofePagUltCuponMeses);
					int ultimoMesPagGuiaCupon	=diasDelMes(mesfePagGuiaCuponMeses, anniofePagGuiaCuponMeses);
					if(diaFePagUltCupon > diaFePagGuiaCupon && mesFePagUltCupon==mesfePagGuiaCuponMeses){
						if(diaFePagUltCupon==31){diaFePagUltCupon=30;}
						diferenciaDiaMay=meses-(diaFePagUltCupon-diaFePagGuiaCupon);
					}
			//Si los dias son ultimo de mes, la diferencia de dias sera los meses multiplicado * 30 dias
				if(ultimoMesUltCupon==diaFePagUltCuponVer && ultimoMesPagGuiaCupon==diaFePagGuiaCupon){
					diferenciaDiaMay=meses;
					}
			//Si el ultimo de mes para la fePagGuiaCupon es el ultimo de febrero, la diferencia de dias sera los meses multiplicado * 30 dias, cuando el dia de fePagUltCupon sea Mayor a el de FepagGuiaCupon
				if(ultimoMesUltCupon!=diaFePagUltCupon && ultimoMesPagGuiaCupon==diaFePagGuiaCupon && mesfePagGuiaCuponMeses==1){
					diferenciaDiaMay=meses;
					}
			//Se realiza el calculo
						if(valor!=1){
							basePorDiferencia=new BigDecimal(diferenciaDiaMay).divide(new BigDecimal(360),7,BigDecimal.ROUND_HALF_EVEN);
							totalDiferencia=totalDiferencia.add(basePorDiferencia);
						}else{
							totalDiferencia=totalDiferencia.add(new BigDecimal(diferenciaDiaMay));
						}
				}else{
					diferenciaDias				=(new BigDecimal(meses));
					diferenciaDias				=diferenciaDias.add(new BigDecimal(fechasDiferenciaEnDias(fechadesde, fechahasta)));
					int ultimoMesUltCupon		=diasDelMes(mesFePagUltCupon, anniofePagUltCuponMeses);
					int ultimoMesPagGuiaCupon	=diasDelMes(mesfePagGuiaCuponMeses, anniofePagGuiaCuponMeses);
					if(ultimoMesUltCupon==diaFePagUltCupon && ultimoMesPagGuiaCupon==diaFePagGuiaCupon){
						diferenciaDias=new BigDecimal(meses);
					}
					if(ultimoMesUltCupon!=diaFePagUltCupon && ultimoMesPagGuiaCupon==diaFePagGuiaCupon && mesfePagGuiaCuponMeses==2){
						diferenciaDias=new BigDecimal(meses).subtract(new BigDecimal(30));
					}
						if(valor!=1){
							basePorDiferencia=diferenciaDias.divide(new BigDecimal(360),7,BigDecimal.ROUND_HALF_EVEN);
							//basePorDiferencia=diferenciaDias;
							totalDiferencia=totalDiferencia.add(basePorDiferencia);
						}else{
							totalDiferencia=totalDiferencia.add(diferenciaDias);
						}
				}
			//Calcula la base por diferencia cuando la BASE es BASE_NL360	o BASE_NL365
			}else if(baseCalculo.equals(BASE_NL360) || baseCalculo.equals(BASE_NL365)){
				int baseAniio=0;
				if(baseCalculo.equals(BASE_NL360)){
					baseAniio=360;
				}else{
					baseAniio=365;
				}
				//diferenciaDias=fechasDiferenciaEnDias(fechadesde,fechahasta);
				int ciclo=anniofePagGuiaCuponMeses-anniofePagUltCuponMeses;
				int bisiestos=0;
				for(int i=0;i<ciclo+1;i++){
					if (((anniofePagUltCuponMeses%100 == 0) && (anniofePagUltCuponMeses%400 == 0)) || ((anniofePagUltCuponMeses%100 != 0) && (anniofePagUltCuponMeses%  4 == 0))){
						bisiestos++;
						if(mesfePagGuiaCuponMeses==1 && diaFePagGuiaCupon<=28 && anniofePagUltCuponMeses==anniofePagGuiaCuponMeses){
							bisiestos--;
						}
					}
					anniofePagUltCuponMeses=anniofePagUltCuponMeses+1;
				}
				diferenciaDias		=diferenciaDias.subtract(new BigDecimal(bisiestos));
					if(valor!=1){
						basePorDiferencia   =diferenciaDias.divide(new BigDecimal(baseAniio));
						totalDiferencia		=totalDiferencia.add(basePorDiferencia);
					}else{
						totalDiferencia		=totalDiferencia.add(diferenciaDias);
					}
			}//fin else if
			//	Verifica si la base es actual actual
			else if(baseCalculo.equals(BASE_ACTUAL)){
				GregorianCalendar actual=new GregorianCalendar();
				int baseActual		= 0;
				baseActual 			= actual.isLeapYear(actual.get(GregorianCalendar.YEAR))?366:365;
				diferenciaDias		=new BigDecimal(fechasDiferenciaEnDias(fechadesde, fechahasta));
				if(valor!=1){
					basePorDiferencia	=diferenciaDias.divide(new BigDecimal(baseActual));
					totalDiferencia		=totalDiferencia.add(basePorDiferencia);
				}else{
					totalDiferencia		=totalDiferencia.add(diferenciaDias);
				}
			}
			//	Calcula la base por la diferencia de dias para acltual 365 o actual 366
			else if(baseCalculo.equals(BASE_ACT365)){
				int ciclo			=anniofePagGuiaCuponMeses-anniofePagUltCuponMeses;//diferencia de años
				int diferencias		=0;
				for(int i=0;i<ciclo+1;i++){
					diferencias				=0;
					int base365_366			=0;
					String anio				=String.valueOf(anniofePagUltCuponMeses);
					String fechaUltimoAnio =anio.concat("-12-31");
					base365_366 			= fePagUltCuponMeses.isLeapYear(anniofePagUltCuponMeses)?366:365;
					Date fechaUltimoAno		=formato.parse(fechaUltimoAnio);
					if(fechaUltimoAno.compareTo(fechahasta)==-1){
						diferencias			=(fechasDiferenciaEnDias(fechadesde, fechaUltimoAno));
						BigDecimal divide =new BigDecimal(diferencias).divide(new BigDecimal(base365_366),7,BigDecimal.ROUND_HALF_EVEN);
						totalDiferencia		=totalDiferencia.add(divide);
						//diferencia de dias * dias del año
						anniofePagUltCuponMeses++;
					}else{
						diferencias=0;
						String anioActual		=String.valueOf(anniofePagUltCuponMeses);
						String fechaPrimerAnio =anioActual.concat("-01-01");						
						Date fechaPrimerAnioDate=formato.parse(fechaPrimerAnio);
						diferencias				=(fechasDiferenciaEnDias(fechaPrimerAnioDate, fechahasta))+1;
						BigDecimal divide =new BigDecimal(diferencias).divide(new BigDecimal(base365_366),7,BigDecimal.ROUND_HALF_EVEN);
						totalDiferencia			=totalDiferencia.add(divide);
						anniofePagUltCuponMeses++;
					}
				}//fin for
			}//fin BASE_ACT365
			return totalDiferencia;			
		}
		
		/**Funci&oacute;n que calcula los intereses devengados por un cliente en el momento de tomar una orden
		*  @param BigDecimal montoCapital que es el monto de la inversion para realizar el calculo
		*  @param idTitulo id del t&iacute;tulo por el que se va a buscar los intereses devengados
		*  @param fecha fecha que se usar&aacute; como referencia para el c&aacute;lculo de los intereses
		*  @throws Exception lanza una excepci&ooacute;n si hay un error en la operaci&oacute;n
		*/

		public static BigDecimal calcularInteresesCaidos(BigDecimal montoCapital,String idTitulo, String fechaValor,String nombreDataSource, DataSource _dso, com.bdv.infi_toma_orden.data.OrdenTitulo beanTitulo) throws Exception{
			
			BigDecimal calculoIntereses		=new BigDecimal(0);
			BigDecimal base					=new BigDecimal(0);
			BigDecimal tasa					=new BigDecimal(0);
			BigDecimal cero					=new BigDecimal(0);
			TitulosDAO titulosDAO 			=new TitulosDAO(nombreDataSource,_dso);
			int dias=0;
			
			DateFormat formatter ; 
			Date dateCupon,dateValor ; 
	        formatter = new SimpleDateFormat("dd-MM-yy");
	        DateFormat df = new SimpleDateFormat("d-MMM-yyyy");
	        String reportDateCupon="";
	        String reportDateValor="";
			
			Connection conn      = _dso.getConnection();
			//conn.setAutoCommit(false);
			
			//Buscar la fecha de emision y los intereses del titulo
			try {
				ArrayList retornoValores = titulosDAO.listarFechaCupon(idTitulo,fechaValor);
				
				ArrayList listaTitulos = titulosDAO.listarTitulos(idTitulo);
				//NM29643 - INFI_TTS_443 13/04/2014: Se valida arrayList listaTitulos no vacio
				if (listaTitulos.size()>0 && !listaTitulos.get(7).toString().equals("DIS")){
					if(!retornoValores.isEmpty()){
						
						String fechaVencimiento=retornoValores.get(2).toString();						
						
						//Validacion si la fecha de vencimiento es igual a la fecha valor el monto de interes caido es igual a CERO
						if((Utilitario.StringToDate(fechaVencimiento,"dd-MM-yyyy")).compareTo(Utilitario.StringToDate(fechaValor, "dd-MM-yyyy"))==0){						
							return new BigDecimal(0);
						}
						
						tasa=new BigDecimal(retornoValores.get(1).toString());
						if (tasa.equals(cero)){
							throw new Error("Titulo no disponible (Tasa Cupón Cero)");
						}
						dateCupon = formatter.parse(retornoValores.get(0).toString());
						dateValor = formatter.parse(fechaValor);
						reportDateCupon = df.format(dateCupon);
						reportDateValor = df.format(dateValor);

					}else if (retornoValores.isEmpty()){
						throw new Error("Titulo no disponible (Sin Cupón Incluido)");
					}
					tasa=tasa.divide(new BigDecimal(100));//se divide la tasa
					
					CallableStatement prcProcedimientoAlmacenado = conn.prepareCall("{call infi_getbascal(?,?,?,?,?,?,?,?,?)}");					
					prcProcedimientoAlmacenado.setString(1, listaTitulos.get(5).toString());//Codigo base de Cálculo en OPICS					
					prcProcedimientoAlmacenado.setBigDecimal(2, montoCapital);//Monto de la operación					
					prcProcedimientoAlmacenado.setBigDecimal(3,tasa);//Tasa de Interés de las operación					
					prcProcedimientoAlmacenado.setString(4,reportDateCupon);//Fecha inicio del Cupón a calcular					
					prcProcedimientoAlmacenado.setString(5,reportDateValor);//Fecha Valor de la operación a Calcular					
				    prcProcedimientoAlmacenado.setString(6, idTitulo);//ID del titulo en OPICS				    
					prcProcedimientoAlmacenado.registerOutParameter(7, java.sql.Types.INTEGER);					
					prcProcedimientoAlmacenado.registerOutParameter(8, java.sql.Types.INTEGER);
					prcProcedimientoAlmacenado.registerOutParameter(9, java.sql.Types.INTEGER);
					
					prcProcedimientoAlmacenado.execute();
					
					calculoIntereses=prcProcedimientoAlmacenado.getBigDecimal(9);//intereses Caidos
					dias=prcProcedimientoAlmacenado.getInt(8);//dias transcurridos
					base =prcProcedimientoAlmacenado.getBigDecimal(7);//pbase
				}else{
					throw new java.lang.Exception("No se pudieron calcular los intereses caidos del titulo "+idTitulo+" ya que no se hallo un titulo con dicho ID en la Base de Datos");
				}
				if(beanTitulo!=null){
					beanTitulo.setCupon(tasa);
					beanTitulo.setDiferenciaDias(dias);
					beanTitulo.setFecha_valor_recompra(fechaValor);
					beanTitulo.setMontoIntCaidos(calculoIntereses);
					beanTitulo.setFactorCalculo(base);
				}

				//System.out.println("--Monto Capital: "+montoCapital+", Fecha Cupon: "+reportDateCupon+", Fecha Valor: "+reportDateValor+", Tasa: "+tasa+", Dias Diferencia: "+dias+", Base: "+base+", CalculoIntereses: "+calculoIntereses);
			} catch (Exception e) {
				e.printStackTrace();
				throw new java.lang.Exception("Problemas al intentar realizar el calculo para los intereses caidos: "+e);
				//throw new java.lang.Exception("Fecha Cupon:" +reportDateCupon+" Fecha Valor: "+reportDateValor+" e: "+e);
			} finally {
				if (conn != null){
					conn.close();
				}
			}
			return calculoIntereses;
		}		
		
}

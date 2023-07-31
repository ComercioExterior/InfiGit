package models.unidad_inversion.unidad_inversion;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import megasoft.AbstractModel;

import com.bdv.infi.dao.InstrumentoFinancieroDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.data.DataRegistro;
import com.bdv.infi.data.UnidadInversion;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

/**
 * Clase que recupera los datos de la pagina de Ingreso de una Unidad de Inversion y los registra en la base de datos
 * @author Megasoft Computaci&oacute;n
 */
public class UnidadInversionInsert extends AbstractModel implements UnidadInversionConstantes {
	
	UnidadInversionDAO unidadInversionDAO=null;
	
	/**
	  * Nombre de la unidad de inversion 
	  * */
	private String unidadInversion;
	
	
	/**
	 * Tipo de producto de la unidad de Inversion
	 * */
	private String tipoProducto;
	
	/**
	 * Formatos de Date y Time
	 */
	private SimpleDateFormat sdIODate = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
	/**
	 * Bean que encapsula los atributos de la Clase UnidadInversion
	 */
	private UnidadInversion beanUI = new UnidadInversion();
	/**
	 * Clase que encapsula la validacion de una unidd de inversion
	 */
	private UnidadInversionValidar classValidar = new UnidadInversionValidar();

	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {	
		
		_req.getSession().removeAttribute("idUnidadInversion");
		
		// campos obligatorios recuperados de la pagina
		beanUI.setNombreUnidadInversion(_record.getValue("nombreUnidadInversion"));	
		//beanUI.setIdInstrumentoFinanciero(_record.getValue("idInstrumentoFinanciero")); //TTS-504-SIMADI Efectivo Taquilla NM25287 21/08/2015. Validar campos para manejo de efectivo
		beanUI.setTasaCambio(new BigDecimal(_record.getValue("tasaCambio")));
		//ITS-2961 Error al realizar OFERTA de SIMADI de bajo valor (MENUDEO)
		beanUI.setTasaCambioOferta(_record.getValue("tasaCambioOferta") != null ? new BigDecimal(_record.getValue("tasaCambioOferta")) :  new BigDecimal("0"));
		beanUI.setFechaEmisionUI(sdIODate.parse(_record.getValue("fechaEmisionUI")));
		beanUI.setFechaCierreUI(sdIODate.parse(_record.getValue("fechaCierreUI")));
		beanUI.setMontoMultiplos(new BigDecimal(_record.getValue("montoMultiplos")));
		beanUI.setTipoMercado(_record.getValue("tipoMercado"));
		beanUI.setIndicaPedidoMonto(new Integer(_record.getValue("indicaPedidoMonto")));
		beanUI.setIndicadorPrecioSucio(Integer.parseInt(_record.getValue("indicadorPrecioSucio")));
		beanUI.setPermiteCancelacion(Integer.parseInt(_record.getValue("permiteCancRadio")));
		beanUI.setPermiteAltoValor(Integer.parseInt(_record.getValue("permiteAltoValorRadio")));
		beanUI.setComisionIGTF(_record.getValue("comisionIGTF") != null ? new BigDecimal(_record.getValue("comisionIGTF")) :  new BigDecimal("0"));
		
		if (_record.getValue("indicaVtaEmpleados") == null) {
			beanUI.setIndicaVtaEmpleados(0);
		} else {
			beanUI.setIndicaVtaEmpleados(1);	
		}
		if (_record.getValue("indicaRecompraNeteo") == null) {
			beanUI.setIndicaRecompraNeteo(0);
		} else {
			beanUI.setIndicaRecompraNeteo(1);	
		}
		
		
		beanUI.setIdMoneda(_record.getValue("idMoneda"));	
		beanUI.setIdEmpresaEmisor(_record.getValue("idEmpresaEmisor"));	
		
		// campos opcionales
		beanUI.setEmisionUnidadInversion(_record.getValue("emisionUnidadInversion"));
		beanUI.setSerieUnidadInversion(_record.getValue("serieUnidadInversion"));
		beanUI.setDescrUnidadInversion(_req.getParameter("descrUnidadInversion"));
		if (_record.getValue("fechaAdjudicacionUI") != null){	
			beanUI.setFechaAdjudicacionUI(sdIODate.parse(_record.getValue("fechaAdjudicacionUI")));	
		}
		
		beanUI.setComentarios(_record.getValue("comentarios"));		
	
		String idTipoInstrumento = _record.getValue("tipoInstrumentoFinanciero");		
		// Campos obligatorios para Tipo de Instrumento : Inventario
		if (idTipoInstrumento.equalsIgnoreCase(ConstantesGenerales.INST_TIPO_INVENTARIO) ||
				idTipoInstrumento.equalsIgnoreCase(ConstantesGenerales.INST_TIPO_INVENTARIO_CON_PRECIO)) {
			beanUI.setTotalInversion(new BigDecimal(_record.getValue("totalInversion")));		
			beanUI.setTasaPool(new BigDecimal(_record.getValue("tasaPool")));		
			beanUI.setPctRendimiento(new BigDecimal(_record.getValue("pctRendimiento")));
			beanUI.setMontoMinimoInversionSubasta(new BigDecimal(0));
		} else {
			beanUI.setTotalInversion(new BigDecimal(0));		
			beanUI.setTasaPool(new BigDecimal(0));		
			beanUI.setPctRendimiento(new BigDecimal(0));
			beanUI.setMontoMinimoInversionSubasta(new BigDecimal(_record.getValue("montoMinimoSubastaInversion")));
		}
		
		//Cobro batch en adjudicación y liquidación
		beanUI.setInCobroAdjudicacion(Integer.parseInt(_record.getValue("batchAdj")));
		beanUI.setInCobroLiquidacion(Integer.parseInt(_record.getValue("batchLiq")));
		
		// Se asigna status : inicio para crear el registro hasta que este completa la informacion requerida
		beanUI.setIdUIStatus(UnidadInversionConstantes.UISTATUS_INICIO);
		
		UnidadInversionDAO boUI = new UnidadInversionDAO(_dso);
		Date fechaActual = new Date();
		
		//	Campos de auditoria
		DataRegistro credenciales = new DataRegistro();
		credenciales.setActUsuarioId(getUserName());
		credenciales.setActUsuarioNombre(getUserDisplayName());
		credenciales.setActIp(_req.getRemoteAddr());
		credenciales.setActUsuarioRolNombre(boUI.listarRolUser(getUserName()));
		credenciales.setActFechaHora(fechaActual);
		beanUI.setCredenciales(credenciales);
		
		//  Aplicar persistencia
		
		int respuesta = boUI.insertar(beanUI);
		if (respuesta != 0) {
			_record.addError("Para su informacion", "Problemas de ingreso de datos");
		}
		
		_req.getSession().setAttribute("idUnidadInversion",String.valueOf(beanUI.getIdUnidadInversion()));

	}
	
	/**
	 * Completar el URL a enviar con la clave del registro a recuperar
	 */
	public String getRedirectParameters() throws Exception  {
		return "idUnidadInversion=" + beanUI.getIdUnidadInversion();
	}

	/**
	 * Validacion de los datos provenientes de la pagina
	 */
	public boolean isValid() throws Exception {
		
		
		boolean flag = super.isValid();
		unidadInversionDAO=new UnidadInversionDAO(_dso);
			
		// Si la validacion basada en el record.xml genero un error se envia a la pagina de error
		// Validar la informacion de la unidad de inversion
		classValidar.setDs(_dso);
		flag = classValidar.isValid(_record);
		
		//TTS-504-SIMADI Efectivo Taquilla NM25287 21/08/2015. Validar campos para manejo de efectivo
		beanUI.setIdInstrumentoFinanciero(_record.getValue("idInstrumentoFinanciero"));		
		InstrumentoFinancieroDAO instF =  new InstrumentoFinancieroDAO(_dso);
		instF.listarPorId(beanUI.getIdInstrumentoFinanciero());
		if(instF.getDataSet().count()>0){
			instF.getDataSet().first();
			instF.getDataSet().next();
			instF.getDataSet().getValue("insfin_forma_orden");
			
					
			if(ConstantesGenerales.INST_FINANCIERO_SIMADI_TAQUILLA.equalsIgnoreCase(instF.getDataSet().getValue("insfin_forma_orden"))){
				
				//NM25287 validación de campos null 19/11/2015
				if(_record.getValue("periodicidadVenta")!=null){
					beanUI.setPeriodicidadVenta(Integer.parseInt(_record.getValue("periodicidadVenta")));
				}else{
					_record.addError("Para su informacion", "Debe ingresar el valor del 'Monto Periodicidad de Venta'");
					flag = false;
				}				
				if(_record.getValue("montoMaximoSubastaInversion")!=null){
					beanUI.setMontoMaximoInversionSubasta(new BigDecimal(_record.getValue("montoMaximoSubastaInversion")));	
				}else{
					_record.addError("Para su informacion", "Debe ingresar el valor del 'Máximo de Inversión'");
					flag = false;
				}				
				if(_record.getValue("montoMultiplosEfectivo")!=null){
					beanUI.setMontoMultiplosEfectivo(new BigDecimal(_record.getValue("montoMultiplosEfectivo")));
				}else{
					_record.addError("Para su informacion", "Debe ingresar el valor del 'Monto Multiplo de Efectivo'");
					flag = false;
				}				

				/*if(beanUI.getPeriodicidadVenta()==0){
					_record.addError("Para su informacion", "El valor Periodicidad de Venta debe ser mayor a cero");
					flag = false;
				}*/
				if(beanUI.getMontoMaximoInversionSubasta().intValue()==0){
					_record.addError("Para su informacion", "El valor Monto Máximo de Inversión debe ser mayor a cero");
					flag = false;
				}
				if(beanUI.getMontoMultiplosEfectivo().intValue()==0){
					_record.addError("Para su informacion", "El valor Monto Multiplo de Efectivo debe ser mayor a cero");
					flag = false;
				}
				
			}else
			{
				if(beanUI.getPeriodicidadVenta()<0){
					_record.addError("Para su informacion", "El valor Periodicidad de Venta debe ser mayor o igual a cero");
					flag = false;
				}
				if(beanUI.getMontoMaximoInversionSubasta().intValue()<0){
					_record.addError("Para su informacion", "El valor Monto Máximo de Inversión debe ser mayor o igual a cero");
					flag = false;
				}
				if(beanUI.getMontoMultiplosEfectivo().intValue()<0){
					_record.addError("Para su informacion", "El valor Monto Multiplo de Efectivo debe ser mayor o igual a cero");
					flag = false;
				}	
			}
		}
		if (_record.getValue("fechaLiquidaUI") != null){	
			if (_record.getValue("PrimeraHoraLiquida") == null && _record.getValue("PrimeraMinLiquida") == null && _record.getValue("SegundaHoraLiquida") == null && _record.getValue("SegundaMinLiquida") == null){	
				_record.addError("Para su informacion", "Debe Indicar las Horas en que se efectuar&aacute; la Liquidaci&oacute;n");	
			}
			
			beanUI.setFechaLiquidaUI(sdIODate.parse(_record.getValue("fechaLiquidaUI")));
			Calendar fecha = Calendar.getInstance();
			fecha.setTime(sdIODate.parse(_record.getValue("fechaLiquidaUI")));
			
			SimpleDateFormat sdIO = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
			String fechaLiquidacion = sdIO.format(fecha.getTime());
			String[] datos = fechaLiquidacion.split("-");
        	String Sdia = datos[0];
			String Smes = datos[1];
			String Sano = datos[2];
			Calendar FechaLiquidaUIHora1 = Calendar.getInstance();
			Calendar FechaLiquidaUIHora2 = Calendar.getInstance();
			
			int dia = Integer.parseInt(Sdia);
			int mes = Integer.parseInt(Smes);
			int ano = Integer.parseInt(Sano);
			int PrimeraHoraLiquida = Integer.parseInt(_record.getValue("PrimeraHoraLiquida"));
			int SegundaHoraLiquida = Integer.parseInt(_record.getValue("SegundaHoraLiquida"));
			int PrimeraMinLiquida  = Integer.parseInt(_record.getValue("PrimeraMinLiquida"));
			int SegundaMinLiquida  = Integer.parseInt(_record.getValue("SegundaMinLiquida"));
			
			//Al MES le restamos siempre (1) ya que CALENDAR asume los meses de 0 al 11 siendo 0 Enero
			mes= mes-1;
			//Si es PM sumaremos 12 horas para llevar a hora militar de 24, el 4to Integer a setear debe ser en formato 24 horas
			if(_record.getValue("meridiano1_liquidacion").equals("AM")&&PrimeraHoraLiquida!=12){
				FechaLiquidaUIHora1.set(ano, mes, dia, PrimeraHoraLiquida, PrimeraMinLiquida,0);
			}else if(_record.getValue("meridiano1_liquidacion").equals("AM")&&PrimeraHoraLiquida==12){
				FechaLiquidaUIHora1.set(ano, mes, dia, 0, PrimeraMinLiquida,0);//Seteamos Cero a la hora ya que en militar no se usa 24 sino 0
			}else{
				if(_record.getValue("meridiano1_liquidacion").equals("PM") && PrimeraHoraLiquida!=12){
					FechaLiquidaUIHora1.set(ano, mes, dia, 12+PrimeraHoraLiquida, PrimeraMinLiquida,0);
					//HORA1 es PM y es DISTINTA DE 12
				}else{
					FechaLiquidaUIHora1.set(ano, mes, dia, PrimeraHoraLiquida, PrimeraMinLiquida,0);
					//HORA1 es PM y es 12
				}
	
			}
			
			if(_record.getValue("meridiano2_liquidacion").equals("AM")&&SegundaHoraLiquida!=12){
				FechaLiquidaUIHora2.set(ano, mes, dia, SegundaHoraLiquida, SegundaMinLiquida,0);
			}else if(_record.getValue("meridiano2_liquidacion").equals("AM")&&SegundaHoraLiquida==12){
				FechaLiquidaUIHora2.set(ano, mes, dia, 0, SegundaMinLiquida,0);//Seteamos Cero a la hora ya que en militar no se usa 24 sino 0
			}else{
				if(_record.getValue("meridiano2_liquidacion").equals("PM") && SegundaHoraLiquida!=12){
					FechaLiquidaUIHora2.set(ano, mes, dia, 12+SegundaHoraLiquida, SegundaMinLiquida,0);
					//HORA2 es PM y es DISTINTA DE 12
				}else{
					FechaLiquidaUIHora2.set(ano, mes, dia, SegundaHoraLiquida, SegundaMinLiquida,0);
					//HORA2 es PM y es 12
				}

			}
			
			Date hora1= FechaLiquidaUIHora1.getTime();
			Date hora2= FechaLiquidaUIHora2.getTime();
			beanUI.setFechaLiquidaUIHora1(hora1);	
			beanUI.setFechaLiquidaUIHora2(hora2);
			SimpleDateFormat sdfH = new SimpleDateFormat("hh:mm:ss, a");
			if (FechaLiquidaUIHora1.after(FechaLiquidaUIHora2)) {
				_record.addError("Para su informacion","La Hora del Primer Intento Liquidaci&oacute;n (" +sdfH.format(hora1)+ ") debe ser menor que la Hora del Segundo Intento Liquidaci&oacute;n (" +sdfH.format(hora2)+ ").");
				flag = false;
			}
			if (FechaLiquidaUIHora1.equals(FechaLiquidaUIHora2)) {
				_record.addError("Para su informacion","La Hora del Primer Intento Liquidación y la Hora del Segundo Intento Liquidación no pueden ser iguales.");
				flag = false;
			}
			
//			if (_record.getValue("bacthAdj") != null && Integer.parseInt(_record.getValue("bacthAdj"))>1){
//				_record.addError("Para su informacion","Error, el valor de cobro de adjudicación debe estar entre 0 y 1");
//				flag = false;	
//			}
//			
//			if (_record.getValue("bacthLiq") != null && Integer.parseInt(_record.getValue("bacthLiq"))>1){
//				_record.addError("Para su informacion","Error, el valor de cobro de liquidación debe estar entre 0 y 1");
//				flag = false;	
//			}			
			
			
		
			unidadInversion=_record.getValue("nombreUnidadInversion");
			
		
			unidadInversionDAO.listarPorNombre(unidadInversion,ConstantesGenerales.ID_TIPO_PRODUCTO_SITME,null);
			
			if(unidadInversionDAO.getDataSet().next()){
				_record.addError("Para su informacion","La Unidad de Inversion ya se encuentra registrada en la base de datos para el tipo de producto seleccionado");
				flag = false;
			}
		}	
				
		return flag;
	}
}

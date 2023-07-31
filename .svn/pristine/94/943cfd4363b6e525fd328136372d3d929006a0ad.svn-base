package models.unidad_inversion.unidad_inversion;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import megasoft.AbstractModel;
import megasoft.DataSet;
import megasoft.Logger;

import com.bdv.infi.dao.BlotterDAO;
import com.bdv.infi.dao.InstrumentoFinancieroDAO;
import com.bdv.infi.dao.TitulosDAO;
import com.bdv.infi.dao.UIComisionDAO;
import com.bdv.infi.dao.UITitulosDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.data.DataRegistro;
import com.bdv.infi.data.UnidadInversion;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;
import com.bdv.infi.util.Utilitario;

/**
 * Clase que recupera los datos de la pagina de Edicion de una Unidad de Inversion y modifica la informacion en la base de datos
 * @author Megasoft Computaci&oacute;n
 */
public class UnidadInversionUpdate extends AbstractModel implements UnidadInversionConstantes{
	
	/**
	 * Formatos de Date y Time
	 */
	private SimpleDateFormat sdIODate = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
	/**
	 * Unidad de Inversion recuperada de la base de datos
	 */
	private UnidadInversion beanUI = new UnidadInversion();
	/**
	 * Clase que encapsula la validacion de una unidd de inversion
	 */
	private UnidadInversionValidar classValidar = new UnidadInversionValidar();
	/**
	 * Identificador del registro a modificar
	 */
	private int idUnidadInversion = 0;
	
	/**
	 * 
	 * */
	private TitulosDAO titulosDAO;
	private UITitulosDAO uiTitulosDAO;
	private UnidadInversionDAO unidadInversionDAO;
	private BlotterDAO blotterDAO;
	private UIComisionDAO uIComisionDAO;
	private String instrFin;
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {	
		
		// Buscar la Unidad de Inversion para verificar si esta condiciones de ser modificada
		idUnidadInversion = Integer.parseInt(_record.getValue("idUnidadInversion"));	
		
		UnidadInversionDAO boUI = new UnidadInversionDAO(_dso);		
		int cant = boUI.listarPorId(idUnidadInversion);

		if(cant == 0) {
			_record.addError("Para su informacion", "No hay Unidades de Inversion con los criterios dados");
			return;
		}
				
		DataSet dsUI = boUI.getDataSet();
		dsUI.next();
		if (dsUI.getValue("undinv_status").equalsIgnoreCase(UISTATUS_CERRADA)){
			_record.addError("Para su informacion", "La Unidad de Inversion no esta en codiciones de ser modificada");
			return;
		}
		// campos obligatorios recuperados de la pagina
		beanUI.setIdUnidadInversion(idUnidadInversion);
		beanUI.setNombreUnidadInversion(_record.getValue("nombreUnidadInversion"));	
		//beanUI.setIdInstrumentoFinanciero(_record.getValue("idInstrumentoFinanciero")); //TTS-504-SIMADI Efectivo Taquilla NM25287 21/08/2015. Validar campos para manejo de efectivo
		
		beanUI.setTasaCambio(new BigDecimal(_record.getValue("tasaCambio")));
		//ITS-2961 Error al realizar OFERTA de SIMADI de bajo valor (MENUDEO)
		beanUI.setTasaCambioOferta(_record.getValue("tasaCambioOferta") != null ? new BigDecimal(_record.getValue("tasaCambioOferta")) : new BigDecimal("0"));
		beanUI.setFechaEmisionUI(sdIODate.parse(_record.getValue("fechaEmisionUI")));
		beanUI.setFechaCierreUI(sdIODate.parse(_record.getValue("fechaCierreUI")));
		beanUI.setTipoMercado(_record.getValue("tipoMercado"));
		beanUI.setIndicaPedidoMonto(new Integer(_record.getValue("indicaPedidoMonto")));
		beanUI.setMontoMultiplos(new BigDecimal(_record.getValue("montoMultiplos")));	
		beanUI.setIndicadorPrecioSucio(Integer.parseInt(_record.getValue("indicadorPrecioSucio")));
		beanUI.setPermiteCancelacion(Integer.parseInt(_record.getValue("permiteCancRadio")));
		beanUI.setPermiteAltoValor(Integer.parseInt(_record.getValue("permiteAltoValorRadio")));
		//Desarrollo requerimiento mandatorio IGTF NM26659 29/01/2016
		beanUI.setComisionIGTF(_record.getValue("comisionIGTF") != null ? new BigDecimal(_record.getValue("comisionIGTF")) : new BigDecimal("0"));
		
		if (_record.getValue("indicaVtaEmpleados") == null || _record.getValue("indicaVtaEmpleados").equalsIgnoreCase("0")) {
			beanUI.setIndicaVtaEmpleados(0);
		} else {
			beanUI.setIndicaVtaEmpleados(1);	
		}
		if (_record.getValue("indicaRecompraNeteo") == null || _record.getValue("indicaRecompraNeteo").equalsIgnoreCase("0")) {
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
		

		if (_record.getValue("batchAdj")!=null){
			beanUI.setInCobroAdjudicacion(Integer.parseInt(_record.getValue("batchAdj")));	
		}else{
			beanUI.setInCobroAdjudicacion(0);
		}
		
		if (_record.getValue("batchLiq")!=null){
			beanUI.setInCobroLiquidacion(Integer.parseInt(_record.getValue("batchLiq")));	
		}else{
			beanUI.setInCobroLiquidacion(0);
		}
		
		beanUI.setDiasAperturaDeCuenta(Integer.parseInt(_record.getValue("dias_apertura_cuenta")));
		
		// Se asigna status : inicio para crear el registro hasta que este completa la informacion requerida
		beanUI.setIdUIStatus(UnidadInversionConstantes.UISTATUS_INICIO);
		
		Date fechaActual = new Date();
		
		//	Campos de auditoria
		DataRegistro credenciales = new DataRegistro();
		credenciales.setActUsuarioId(getUserName());
		credenciales.setActUsuarioNombre(getUserDisplayName());
		credenciales.setActUsuarioRolNombre(boUI.listarRolUser(getUserName()));
		credenciales.setActIp(_req.getRemoteAddr());
		credenciales.setActFechaHora(fechaActual);
		beanUI.setCredenciales(credenciales);
		
		//  Aplicar persistencia
		int respuesta = boUI.modificar(beanUI);
		if (respuesta != 0) {
			_record.addError("Para su informacion", "Problemas de ingreso de datos");
		}else{
			beanUI.setIdUIStatus(UnidadInversionConstantes.UISTATUS_REGISTRADA);
			boUI.modificarStatus(beanUI);
		}

		boolean asociaciones = boUI.dataCompleta(idUnidadInversion);
		if (!asociaciones) {
			boUI.modificarStatus(idUnidadInversion, UISTATUS_INICIO);
		}else{
			boUI.modificarStatus(idUnidadInversion, UISTATUS_REGISTRADA);
		}
	}
	
	/**
	 * Completar el URL a enviar con la clave del registro a recuperar
	 */
	public String getRedirectParameters() throws Exception  {
		return "idUnidadInversion=" + idUnidadInversion;
	}

	/**
	 * Validacion de los datos provenientes de la pagina
	 */
public boolean isValid() throws Exception {
		boolean flag = super.isValid();
		long unidadInversionID=0;
		
		titulosDAO=new TitulosDAO(_dso);
		uiTitulosDAO =new UITitulosDAO(_dso);
		unidadInversionDAO =new UnidadInversionDAO(_dso);
		blotterDAO= new BlotterDAO(_dso);
		uIComisionDAO= new UIComisionDAO(_dso);
		
		idUnidadInversion = Integer.parseInt(_record.getValue("idUnidadInversion"));
		String idTipoProductoNuevo= _record.getValue("idTipoProductoNuevo");
		String idTipoProductoAnt=	_record.getValue("idTipoProductoAnt");
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
			instrFin=instF.getDataSet().getValue("insfin_forma_orden");
			
			
			beanUI.setPeriodicidadVenta(Integer.parseInt(_record.getValue("periodicidadVenta")));
			beanUI.setMontoMaximoInversionSubasta(new BigDecimal(_record.getValue("montoMaximoSubastaInversion")));	
			beanUI.setMontoMultiplosEfectivo(new BigDecimal(_record.getValue("montoMultiplosEfectivo")));
			
			if(ConstantesGenerales.INST_FINANCIERO_SIMADI_TAQUILLA.equalsIgnoreCase(instF.getDataSet().getValue("insfin_forma_orden"))){

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
				//HORA1 es AM y DISTINTA DE 12
			}else if(_record.getValue("meridiano1_liquidacion").equals("AM")&&PrimeraHoraLiquida==12){
				FechaLiquidaUIHora1.set(ano, mes, dia, 0, PrimeraMinLiquida,0);//Seteamos Cero a la hora ya que en militar no se usa 24 sino 0
				//HORA1 es AM e IGUAL a 12
			}else{//si la hora es PM y no es igual a 12 sumar 12 para llevar a formato 24
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
				//HORA2 es AM y DISTINTA DE 12
			}else if(_record.getValue("meridiano2_liquidacion").equals("AM")&&SegundaHoraLiquida==12){
				FechaLiquidaUIHora2.set(ano, mes, dia, 0, SegundaMinLiquida,0);//Seteamos Cero a la hora ya que en militar no se usa 24 sino 0
				//HORA2 es AM e IGUAL a 12
			}else{//si la hora es PM y no es igual a 12 sumar 12 para llevar a formato 24
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
		}
		
		Logger.debug("Validaciones Update Unidad de Inversion", idTipoProductoAnt+","+idTipoProductoNuevo+","+_record.getValue("idInstrumentoFinanciero"));
		if(!idTipoProductoNuevo.equalsIgnoreCase(idTipoProductoAnt)){		
			if ((idTipoProductoAnt.equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA)||idTipoProductoNuevo.equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA)
					|| idTipoProductoAnt.equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL)||idTipoProductoNuevo.equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL))){
				//Validar si existen titulos registrados
				if(uiTitulosDAO.listarTitulosYSubastas(idUnidadInversion)>0){
					_record.addError("Para su informacion", "Para cambiar de instrumento financiero debe eliminar los títulos o subastas registrados");
					Logger.debug("Validaciones Update Unidad de Inversion","Existes títulos o Subastas registradas");
					flag = false;
				}				
			}
			//TTS-504-SIMADI Efectivo Taquilla NM25287 31/08/2015
			if(!instrFin.equals(ConstantesGenerales.INST_FINANCIERO_SIMADI_TAQUILLA))
			{
				//Validar si existen blotters de tipo taquilla
				blotterDAO.listarBlotterCanalPorUiId(Integer.toString((idUnidadInversion)));
				if(blotterDAO.getDataSet().count()>0){
					blotterDAO.getDataSet().first();
					blotterDAO.getDataSet().next();
					if(blotterDAO.getDataSet().getValue("id_canal").equalsIgnoreCase(ConstantesGenerales.ID_CANAL_TAQUILLA)){
						_record.addError("Para su informacion", "Para cambiar de instrumento financiero debe eliminar los blotters asociados al canal sea taquilla");
						Logger.debug("Validaciones Update Unidad de Inversion","Para cambiar de instrumento financiero debe eliminar los blotters asociados al canal sea taquilla");
						flag = false;
					}
				}
				//Configuraciones de blotter para efectivo
				int cant=0;
				cant=blotterDAO.listarBlotterUiTipoOperacion(Integer.toString((idUnidadInversion)),ConstantesGenerales.TIPO_OP_EFECTIVO);
				if(cant>0){
					_record.addError("Para su informacion", "Para cambiar de instrumento financiero debe eliminar las configuraciones de Parámetros por tipo de persona asociados a efectivo");
					Logger.debug("Validaciones Update Unidad de Inversion","Para cambiar de instrumento financiero debe eliminar las configuraciones de Parámetros por tipo de persona asociados a efectivo");
					flag = false;
				}
				
				//Comisiones en efectivo
				cant=uIComisionDAO.cantidadComisionesUIporTipo(Integer.toString((idUnidadInversion)),ConstantesGenerales.TIPO_OP_EFECTIVO);
				if(cant>0){
					_record.addError("Para su informacion", "Para cambiar de instrumento financiero debe eliminar las configuraciones de comisión asociados a efectivo");
					Logger.debug("Validaciones Update Unidad de Inversion","Para cambiar de instrumento financiero debe eliminar las configuraciones de comisión asociados a efectivo");
					flag = false;
				}
			}
			
		}
		unidadInversionID=Long.parseLong(_record.getValue("idUnidadInversion"));			
		titulosDAO.listarVencimientoTituloPorUI(unidadInversionID);
		
		if(titulosDAO.getDataSet().next()){
						
			String vencimientoTitulo=titulosDAO.getDataSet().getValue("MDATE");			
			String fechaLiquidacion=_record.getValue("fechaLiquidaUI");
			Logger.debug("Validaciones Update Unidad de Inversion","Fecha vencimiento Titulo " + vencimientoTitulo);
			Logger.debug("Validaciones Update Unidad de Inversion","Fecha liquidacion UI " + fechaLiquidacion);
			if(fechaLiquidacion!=null&&(Utilitario.StringToDate(fechaLiquidacion,"dd-MM-yyyy")).compareTo(Utilitario.StringToDate(vencimientoTitulo, "dd-MM-yyyy"))>0){				
				
				_record.addError("Para su informacion","La fecha de liquidacion de la unidad de inversion es mayor a la fecha de vencimiento del titulo ("+vencimientoTitulo+") por favor corriga la fecha");
				flag=false;
			}
		}		
				
		return flag;
	
	}
}

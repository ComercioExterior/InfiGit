package models.unidad_inversion.blotters;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import megasoft.AbstractModel;
import megasoft.DataSet;

import com.bdv.infi.dao.UIBlotterDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.data.UIBlotter;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;
import com.enterprisedt.util.debug.Logger;

/**
 * Clase que recupera los datos de la pagina de actualizacion de las relaciones entre Unidad de Inversion y los Blotters y aplica la persistencia en la base de datos
 * @author Megasoft Computaci&oacute;n
 */
public class UIBlotterUpdate extends AbstractModel implements UnidadInversionConstantes {
	
	/**
	 * Formatos de Date y Time
	 */
	private SimpleDateFormat sdIODate = new SimpleDateFormat(ConstantesGenerales.FORMATO_FECHA);
	private SimpleDateFormat sdBDDate = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * Clase que encapsula la funcionalidad de la Unidad de Inversion
	 */
	private UnidadInversionDAO boUI;
	/**
	 * Clase que encapsula la funcionalidad de la Asociacion UI-Blotter 
	 */
	private UIBlotterDAO boUIBlotter = null;
	/**
	 * Clase que encapsula la validacion de una unidd de inversion
	 */
	private UIBlotterValidar classValidar = new UIBlotterValidar();
	/**
	 * Unidad de Inversion recuperada de la base de datos
	 */
	private UIBlotter beanUIBlotter = new UIBlotter();
	/**
	 * Identificador de la unidad de inversion
	 */
	private long idUnidadInversion = 0;
	/**
	 * Identificador del blotter a modificar
	 */
	private String idBlotter = "";	
	/**
	 * DataSet con la informacion de la UI
	 */
	private DataSet dsUI;
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {	
		
		
		// campos obligatorios recuperados de la pagina
		beanUIBlotter.setIdUnidadInversion(idUnidadInversion);
		beanUIBlotter.setIdBlotter(idBlotter);	
		beanUIBlotter.setHorarioDesde(_record.getValue("horarioDesde"));
		beanUIBlotter.setHorarioHasta(_record.getValue("horarioHasta"));
		beanUIBlotter.setHorarioDesdeUltDia(_record.getValue("horarioDesdeUltDia"));
		beanUIBlotter.setHorarioHastaUltDia(_record.getValue("horarioHastaUltDia"));		
		beanUIBlotter.setPeriodoInicial(sdIODate.parse(_record.getValue("periodoInicial")));
		beanUIBlotter.setPeriodoFinal(sdIODate.parse(_record.getValue("periodoFinal")));	
		if (dsUI.getValue("insfin_forma_orden").toLowerCase().indexOf("inventario") > -1){
			beanUIBlotter.setGananciaRed(new BigDecimal(_record.getValue("gananciaRed")));
		} else {
			beanUIBlotter.setGananciaRed(new BigDecimal(0));
		}
		
		if(_record.getValue("indicaRecompra")==null){
			_record.setValue("indicaRecompra", "0");	
		}
		
		beanUIBlotter.setIndicaRecompra(Integer.parseInt(_record.getValue("indicaRecompra")));
		
		//  Aplicar persistencia
		boUIBlotter.modificar(beanUIBlotter);
		
		//Si venimos del modulo de modificacion de unidad de inversion debemos cambiar el estatus de la unidad 
		//a Registarda o en inicio para que vuekva a ser publicada ya que se cambio informacion
		String accion= getSessionObject("accion").toString();
		if (Integer.parseInt(accion)==4){
			boolean asociaciones = boUI.dataCompleta(idUnidadInversion);
			if (!asociaciones) {
				boUI.modificarStatus(idUnidadInversion, UISTATUS_INICIO);
			}else{
				boUI.modificarStatus(idUnidadInversion, UISTATUS_REGISTRADA);
			}
		}
		
		storeDataSet("_record", _record);
	}
	
	
	/**
	 * Completar el URL a enviar con la clave del registro a recuperar
	 */
	public String getRedirectParameters() throws Exception  {
		return "idUnidadInversion=" + idUnidadInversion +"&idBlotter="+idBlotter;
	}

	/**
	 * Validacion de los datos provenientes de la pagina
	 */
	public boolean isValid() throws Exception {
		
		boolean flag = super.isValid();
		
		// Si la validacion basada en el record.xml genero un error se envia a la pagina de error
		if (!flag) 	{
			return flag;
		}
		
		// Validar la informacion de la unidad de inversion
		flag = classValidar.isValid(_record);
		if (!flag) 	{
			return flag;
		}
		
		// Buscar la Unidad de Inversion para verificar si esta condiciones de ser modificada		
		idUnidadInversion = Long.parseLong(_record.getValue("idUnidadInversion"));
		boUI = new UnidadInversionDAO(_dso);
		int cant = boUI.listarPorId(idUnidadInversion);	
		if(cant == 0) {
			_record.addError("Para su informacion", "No hay Unidades de Inversion con los criterios dados");
			return false;
		}
		
		dsUI = boUI.getDataSet();
		dsUI.next();
		if (dsUI.getValue("undinv_status").equalsIgnoreCase(UISTATUS_CERRADA)){
			_record.addError("Para su informacion", "La Unidad de Inversion no esta en codiciones de ser modificada");
			return false;
		}
		
		//	Porcentaje de Ganancia de Red para Unidades de Inversion tipo inventario
		if (dsUI.getValue("insfin_forma_orden").toLowerCase().indexOf("inventario") > -1){
			if (_record.getValue("gananciaRed") == null || _record.getValue("gananciaRed").equals("")){
				_record.addError("Porcentaje de Ganancia de la Red", "Este campo es requerido para Unidades de Inversion tipo Inventario");
				flag = false;
			}
			double aux = new Double(_record.getValue("gananciaRed")).doubleValue();
			if (aux == 0){
				_record.addError("Porcentaje de Ganancia de la Red", "Este campo es requerido para Unidades de Inversion tipo Inventario");
				flag = false;
			}
			if (aux > 999.99){
				_record.addError("Porcentaje de Ganancia de la Red", "El valor no debe exceder de 999.99");
				flag = false;
			}
		} 
		

		/**
		 * Calendarios para manejo de las fechas : validacion y conversion a Date
		 */
		Calendar calenderDesde = Calendar.getInstance();
		Calendar calenderHasta = Calendar.getInstance();
		Calendar calendarBlotterDesde = Calendar.getInstance();
		Calendar calendarBlotterHasta = Calendar.getInstance();		
		
		calenderDesde.setTime(sdBDDate.parse(dsUI.getValue("undinv_fe_emision")));
		calenderHasta.setTime(sdBDDate.parse(dsUI.getValue("undinv_fe_cierre")));
		
		calendarBlotterDesde.setTime(sdIODate.parse(_record.getValue("periodoInicial")));
		if (calendarBlotterDesde.before(calenderDesde) || calendarBlotterDesde.after(calenderHasta)) {
			_record.addError("Para su informacion","El Periodo de Toma Desde debe estar entre : "+sdIODate.format(calenderDesde.getTime()) + " y "+sdIODate.format(calenderHasta.getTime()));
			flag = false;
		}		
		calendarBlotterHasta.setTime(sdIODate.parse(_record.getValue("periodoFinal")));
		if (calendarBlotterHasta.before(calenderDesde) || calendarBlotterHasta.after(calenderHasta)) {
			_record.addError("Para su informacion","El Periodo de Toma Hasta debe estar entre : "+sdIODate.format(calenderDesde.getTime()) + " y "+sdIODate.format(calenderHasta.getTime()));
			flag = false;
		}	
		
		// Buscar el Blotter a procesar
		idBlotter = _record.getValue("idBlotter");
		
		boUIBlotter = new UIBlotterDAO(_dso);
		boUIBlotter.listarPorId(idUnidadInversion, idBlotter);
		if ( boUIBlotter.getDataSet().count() == 0){
			_record.addError("Para su informacion", "El Blotter solicitado no esta registrado");
			return false;
		}
		boUIBlotter.getDataSet().next();

		int auxHora = 0, auxMin = 0;
		//	Asignar el horario del Blotter
		auxHora = new Integer(boUIBlotter.getDataSet().getValue("blotter_hora_horario_desde").substring(0,2)).intValue();
		auxMin = new Integer(boUIBlotter.getDataSet().getValue("blotter_hora_horario_desde").substring(3,5)).intValue();
		calendarBlotterDesde.set(2000, 01, 01, auxHora, auxMin, 0);
		if (boUIBlotter.getDataSet().getValue("blotter_hora_horario_desde").toLowerCase().indexOf("am") == -1)
			calendarBlotterDesde.set(2000, 01, 01, auxHora+12, auxMin, 0);
		if (boUIBlotter.getDataSet().getValue("blotter_hora_horario_desde").toLowerCase().indexOf("am") > -1 && auxHora==12)
			calendarBlotterDesde.set(2000, 01, 01, 0, auxMin, 0);//Seteamos Cero a la hora ya que en militar no se usa 24 sino 0
		auxHora = new Integer(boUIBlotter.getDataSet().getValue("blotter_hora_horario_hasta").substring(0,2)).intValue();
		auxMin = new Integer(boUIBlotter.getDataSet().getValue("blotter_hora_horario_hasta").substring(3,5)).intValue();
		calendarBlotterHasta.set(2000, 01, 01, auxHora, auxMin, 0);
		if (boUIBlotter.getDataSet().getValue("blotter_hora_horario_hasta").toLowerCase().indexOf("am") == -1)
			calendarBlotterHasta.set(2000, 01, 01, auxHora+12, auxMin, 0);
		if (boUIBlotter.getDataSet().getValue("blotter_hora_horario_hasta").toLowerCase().indexOf("am") > -1 && auxHora==12)
			calendarBlotterHasta.set(2000, 01, 01, 0, auxMin, 0);//Seteamos Cero a la hora ya que en militar no se usa 24 sino 0
		/**
		 * Validar los horarios
		 */
		
		//	Asignar el horario Nuevo
		auxHora = new Integer(_record.getValue("horarioDesde").substring(0,2)).intValue();
		auxMin = new Integer(_record.getValue("horarioDesde").substring(3,5)).intValue();
		calenderDesde.set(2000, 01, 01, auxHora, auxMin, 0);
		if (_record.getValue("horarioDesde").toLowerCase().indexOf("am") == -1)
			calenderDesde.set(2000, 01, 01, auxHora+12, auxMin, 0);
		if (_record.getValue("horarioDesde").toLowerCase().indexOf("am") > -1 && auxHora==12)
			calenderDesde.set(2000, 01, 01, 0, auxMin, 0);//Seteamos Cero a la hora ya que en militar no se usa 24 sino 0
		auxHora = new Integer(_record.getValue("horarioHasta").substring(0,2)).intValue();
		auxMin = new Integer(_record.getValue("horarioHasta").substring(3,5)).intValue();
		calenderHasta.set(2000, 01, 01, auxHora, auxMin, 0);
		if (_record.getValue("horarioHasta").toLowerCase().indexOf("am") == -1)
			calenderHasta.set(2000, 01, 01, auxHora+12, auxMin, 0);
		if (_record.getValue("horarioHasta").toLowerCase().indexOf("am") > -1 && auxHora==12)
			calenderHasta.set(2000, 01, 01, 0, auxMin, 0);//Seteamos Cero a la hora ya que en militar no se usa 24 sino 0
		if (calenderDesde.after(calenderHasta)) {
			_record.addError("Para su informacion","El Horario Desde debe ser menor que el Horario Hasta");
			flag = false;
		}	
		if (calenderDesde.before(calendarBlotterDesde) || calenderDesde.after(calendarBlotterHasta)) {
			_record.addError("Para su informacion","El Horario Desde debe estar entre : "+boUIBlotter.getDataSet().getValue("blotter_hora_horario_desde") + " y "+boUIBlotter.getDataSet().getValue("blotter_hora_horario_hasta"));
			flag = false;
		}
		if (calenderHasta.before(calendarBlotterDesde) || calenderHasta.after(calendarBlotterHasta)) {
			_record.addError("Para su informacion","El Horario Hasta debe estar entre : "+boUIBlotter.getDataSet().getValue("blotter_hora_horario_desde") + " y "+boUIBlotter.getDataSet().getValue("blotter_hora_horario_hasta"));
			flag = false;
		}	
		
//		Asignar el horario Nuevo para el ultimo dia
		auxHora = new Integer(_record.getValue("horarioDesdeUltDia").substring(0,2)).intValue();
		auxMin = new Integer(_record.getValue("horarioDesdeUltDia").substring(3,5)).intValue();
		calenderDesde.set(2000, 01, 01, auxHora, auxMin, 0);
		if (_record.getValue("horarioDesdeUltDia").toLowerCase().indexOf("am") == -1)
			calenderDesde.set(2000, 01, 01, auxHora+12, auxMin, 0);
		if (_record.getValue("horarioDesdeUltDia").toLowerCase().indexOf("am") > -1 && auxHora==12)
			calenderDesde.set(2000, 01, 01, 0, auxMin, 0);//Seteamos Cero a la hora ya que en militar no se usa 24 sino 0
		auxHora = new Integer(_record.getValue("horarioHastaUltDia").substring(0,2)).intValue();
		auxMin = new Integer(_record.getValue("horarioHastaUltDia").substring(3,5)).intValue();
		calenderHasta.set(2000, 01, 01, auxHora, auxMin, 0);
		if (_record.getValue("horarioHastaUltDia").toLowerCase().indexOf("am") == -1)
			calenderHasta.set(2000, 01, 01, auxHora+12, auxMin, 0);
		if (_record.getValue("horarioHastaUltDia").toLowerCase().indexOf("am") > -1 && auxHora==12)
			calenderHasta.set(2000, 01, 01, 0, auxMin, 0);//Seteamos Cero a la hora ya que en militar no se usa 24 sino 0
		if (calenderDesde.after(calenderHasta)) {
			_record.addError("Para su informacion","El Horario Desde del Ultimo Dia debe ser menor que el Horario Hasta del Ultimo Dia ");
			flag = false;
		}	
		
		if (calenderDesde.before(calendarBlotterDesde) || calenderDesde.after(calendarBlotterHasta)) {
			_record.addError("Para su informacion","El Horario Desde del Ultimo Dia  debe estar entre : "+boUIBlotter.getDataSet().getValue("blotter_hora_horario_desde") + " y "+boUIBlotter.getDataSet().getValue("blotter_hora_horario_hasta"));
			flag = false;
		}	
		if (calenderHasta.before(calendarBlotterDesde) || calenderHasta.after(calendarBlotterHasta)) {
			_record.addError("Para su informacion","El Horario Hasta  del Ultimo Dia debe estar entre : "+boUIBlotter.getDataSet().getValue("blotter_hora_horario_desde") + " y "+boUIBlotter.getDataSet().getValue("blotter_hora_horario_hasta"));
			flag = false;
		}
		return flag;
	}
}

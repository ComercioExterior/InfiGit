package models.unidad_inversion.blotters.parametros_tp;

import java.math.BigDecimal;

import megasoft.AbstractModel;

import com.bdv.infi.dao.UIBlotterDAO;
import com.bdv.infi.dao.UIBlotterRangosDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.data.UIBlotterRangos;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionFinanciera;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

/**
 * Clase que actualiza los parametros de las Asociacion entre Unidad de Inversion editada y los Blotter
 * @author Megasoft Computaci&oacute;n
 */
public class UIBlotterTPUpdate  extends AbstractModel implements UnidadInversionConstantes{
 	
	/**
	 * Clase que encapsula la funcionalidad de la Unidad de Inversion
	 */
	private UnidadInversionDAO boUI;
	/**
	 * Clase que encapsula la funcionalidad de la Asociacion UI-Blotter 
	 */
	private UIBlotterDAO boUIBlotter = null;
	/**
	 * Clase que encapsula la funcionalidad de los Parametros de la Asociacion UI-Blotter
	 */
	private UIBlotterRangosDAO boUIBTP = null;
	/**
	 * Bean que encapsula la funcionalidad de los Parametros de la Asociacion UI-Blotter
	 */
	private UIBlotterRangos beanUIBlotterRangos = new UIBlotterRangos(); 
	/**
	 * Identificador de la Unidad de Inversion 
	 */
	private long idUnidadInversion = 0;
	/**
	 * Identificador del Blotter
	 */
	private String idBlotter = "";	
	/**
	 * Variables de trabajo
	 */
	private String idTipoInstrumento = "";
	private int indicadorMonto = 0;
	
	private String tipoProductoId="";

	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {	
		
		
		beanUIBlotterRangos.setComisionEmisor(new BigDecimal(_record.getValue("comisionEmisor")));
		if (_record.getValue("maxFinanciamiento") != null && !_record.getValue("maxFinanciamiento").trim().equals("")) 
			beanUIBlotterRangos.setMaxFinanciamiento(new BigDecimal(_record.getValue("maxFinanciamiento")));

		if (idTipoInstrumento.equalsIgnoreCase(ConstantesGenerales.INST_TIPO_SUBASTA)
				//|| idTipoInstrumento.equalsIgnoreCase(ConstantesGenerales.INST_TIPO_SITME)
				|| idTipoInstrumento.equalsIgnoreCase(ConstantesGenerales.INST_TIPO_SUBASTA_COMPETITIVA) 
				|| idTipoInstrumento.equalsIgnoreCase(ConstantesGenerales.INST_TIPO_INVENTARIO_CON_PRECIO)
				|| idTipoInstrumento.equalsIgnoreCase(ConstantesGenerales.INST_FINANCIERO_SIMADI_TAQUILLA)) {
			beanUIBlotterRangos.setPrecioMinimo(new BigDecimal(_record.getValue("precioMinimo")));
		}
		if (idTipoInstrumento.equalsIgnoreCase(ConstantesGenerales.INST_TIPO_SUBASTA_COMPETITIVA) || 
		    idTipoInstrumento.equalsIgnoreCase(ConstantesGenerales.INST_FINANCIERO_SIMADI_TAQUILLA)	){
			beanUIBlotterRangos.setPrecioMaximo(new BigDecimal(_record.getValue("precioMaximo")));
		}
		if (indicadorMonto == 1) {
			beanUIBlotterRangos.setMontoMinimoInversion(new BigDecimal(_record.getValue("montoMinimoInversion")));
			beanUIBlotterRangos.setMontoMaximoInversion(new BigDecimal(_record.getValue("montoMaximoInversion")));		
		} else {
			beanUIBlotterRangos.setCantMinimaInversion(new Integer(_record.getValue("cantMinimaInversion")).intValue());
			beanUIBlotterRangos.setCantMaximaInversion(new Integer(_record.getValue("cantMaximaInversion")).intValue());
		}


		/* NM25287 SICAD II. 24/03/2014
		 * Validaciones para el registro de tasas propuestas mínimas y máximas para las unidades de inversion Subasta Divisas */
		if (tipoProductoId.equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA)||
				tipoProductoId.equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL)||
					tipoProductoId.equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL)||
						tipoProductoId.equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL)) {
			beanUIBlotterRangos.setTasaPropuestMinima(new BigDecimal(_record.getValue("tasaPropuestaMinima")));
			beanUIBlotterRangos.setTasaPropuestMaxima(new BigDecimal(_record.getValue("tasaPropuestaMaxima")));
			//NM25287 TTS-491 SIMADI Entregable 1. Inclusion de campo monto minimo alto valor
			beanUIBlotterRangos.setMontoMinimoAltoValor(new BigDecimal(_record.getValue("montoMinimoAltoValor")));
		}

		//  Aplicar persistencia
		if (_record.getValue("origen").equals("X")) {
			boUIBTP.insertar(beanUIBlotterRangos);
		} else {
			boUIBTP.modificar(beanUIBlotterRangos);
		}
		
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
		return "idUnidadInversion=" + idUnidadInversion + "&idBlotter="+idBlotter;
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
		idUnidadInversion = Long.parseLong(_record.getValue("idUnidadInversion"));
		// campos obligatorios recuperados de la pagina
		beanUIBlotterRangos.setIdUnidadInversion(idUnidadInversion);
		beanUIBlotterRangos.setIdBlotter(_record.getValue("idBlotter"));	
		beanUIBlotterRangos.setTransaccion(_record.getValue("trnfin_tipo"));
		beanUIBlotterRangos.setTipoPersonaValido(_record.getValue("idTipoPersona"));
		//NM25287 TTS-504-SIMADI Efectivo Taquilla 24/08/2015
		beanUIBlotterRangos.setTipoOperacion(new Integer(_record.getValue("trnfin_id_op")).intValue());
				
		if(beanUIBlotterRangos.getTipoOperacion()==Integer.parseInt(ConstantesGenerales.BLOTTER_TIPO_OPERACION_EFEC)&&
				!beanUIBlotterRangos.getTransaccion().equalsIgnoreCase(TransaccionFinanciera.BLOQUEO)){
			_record.addError("Para su informacion", "Las operaciones de efectivo deben tomarse bajo la transacción de BLOQUEO");
			return false;
		}
				
		// Buscar la Unidad de Inversion para verificar si esta condiciones de ser modificada		
		boUI = new UnidadInversionDAO(_dso);
		int cant = boUI.listarPorId(idUnidadInversion);	
		if(cant == 0) {
			_record.addError("Para su informacion", "No hay Unidades de Inversion con los criterios dados");
			return false;
		}
		boUI.getDataSet().next();
		
		tipoProductoId=boUI.getDataSet().getValue("tipo_producto_id");
		
		if (boUI.getDataSet().getValue("undinv_status").equalsIgnoreCase(UISTATUS_CERRADA)){
			_record.addError("Para su informacion", "La Unidad de Inversion no esta en codiciones de ser modificada");
			return false;
		}
		
		// Buscar el Blotter a procesar
		idBlotter = _record.getValue("idBlotter");
		
		boUIBlotter = new UIBlotterDAO(_dso);
		boUIBlotter.listarPorId(idUnidadInversion, idBlotter);
		if ( boUIBlotter.getDataSet().count() == 0) {
			_record.addError("Para su informacion", "El Blotter no esta registrado");
			return false;
		}
		
		// Buscar el Parametros por Tipo de Persona
		//	1.-	Buscar el registro
		//	2.-	Si el Tipo es X 
		//		2.1.-	Si no existe	--> incluir un registro
		//		2.2.-	Si existe		--> rechazar la actualizacion		
		//	2.-	Si el Tipo es un valor de la tabla 
		//		2.1.-	Si existe		--> actualizar el registro
		//		2.2.-	Si no existe	--> rechazar la actualizacion		

		boUIBTP = new UIBlotterRangosDAO(_dso);
		boUIBTP.listarBlotterRangos(idUnidadInversion, idBlotter, _record.getValue("idTipoPersona"),beanUIBlotterRangos.getTipoOperacion());
		if (_record.getValue("origen").equals("X")) {
			if (boUIBTP.getDataSet().count() != 0){
				_record.addError("Para su informacion", "Los Parámetros por Tipo de Persona ya estan registrados");
				return false;
			}
		} else {
			if (boUIBTP.getDataSet().count() == 0){
				_record.addError("Para su informacion", "Los Parámetros por Tipo de Persona no estan registrados");
				return false;
			}
			
		}
		double 	doubleAux1 = 0, doubleAux2 = 0, doubleAux3 = 0;
		if (_record.getValue("comisionEmisor") != null && !_record.getValue("comisionEmisor").trim().equals("")) {
			doubleAux1 = new Double(_record.getValue("comisionEmisor")).doubleValue();
			if (doubleAux1 > 100.0) {
				_record.addError("Comisi&oacute;n del Emisor","Este no puede ser mayor a 100.");
				flag = false;
			}
		}		
		if (_record.getValue("maxFinanciamiento") != null && !_record.getValue("maxFinanciamiento").trim().equals("")) {
			doubleAux1 = new Double(_record.getValue("maxFinanciamiento")).doubleValue();
			if (doubleAux1 > 100.0) {
				_record.addError("Porcentaje M&aacute;ximo de Financiamiento","Este no puede ser mayor a 100.");
				flag = false;
			}
		} 

		doubleAux1 = doubleAux2 = 0;
		double precioMinimo = 0, precioMaximo = 0;
		idTipoInstrumento = boUI.getDataSet().getValue("insfin_forma_orden");
		indicadorMonto = new Integer(boUI.getDataSet().getValue("undinv_in_pedido_monto")).intValue();
		
		// Validacion de Unidad Minima de Inversion
		//	Estos campos son requeridos para Instrumento Financiero es de Tipo Inventario = Inventario o Subasta que pide Montos
		//	1.-	Monto Mimino		: requerido		Valor > 0
		//	2.-	Monto Maximo		: requerido		Valor >= Monto Minimo
		//	Estos campos son requeridos para Instrumento Financiero es de Tipo = Subasta
		//	1.-	precio Minimo		: requerido		Valor > 0
		//	Para  Tipo = Subasta Compentitiva
		//		1.1.-	precio maximo	: requerido Valor >= precio Minimo
		//	2.-	Si el Indicador de Solicitar Monto == 1
		//		2.1.-	Monto Mimino		: requerido		Valor > 0
		//		2.2.-	Monto Maximo		: requerido		Valor >= Monto Minimo	
		//	3.-	Si el Indicador de Solicitar Monto == 0		
		//		3.1.-	Cantidad Mimina		: requerido		Valor > 0
		//		3.2.-	Cantidad Maxima		: requerido		Valor >= cantidad Minima
		if (idTipoInstrumento.equalsIgnoreCase(ConstantesGenerales.INST_TIPO_SUBASTA)
				|| idTipoInstrumento.equalsIgnoreCase(ConstantesGenerales.INST_TIPO_SUBASTA_COMPETITIVA)
				|| idTipoInstrumento.equalsIgnoreCase(ConstantesGenerales.INST_TIPO_INVENTARIO_CON_PRECIO)) {
			if (_record.getValue("precioMinimo") == null || _record.getValue("precioMinimo").trim().equals("")) {
				_record.addError("Precio M&iacute;nimo","Este campo es requerido para Unidades de Inversi&oacute;n tipo Subasta o Inventario con Precio.");
				flag = false;
			} else {
				precioMinimo = new Double(_record.getValue("precioMinimo")).doubleValue();
				if (precioMinimo == 0) {
					_record.addError("Precio M&iacute;nimo","Este campo es requerido para Unidades de Inversi&oacute;n tipo Subasta o Inventario con Precio.");
					flag = false;
				} else if (precioMinimo > 999.99) {
					_record.addError("Precio M&iacute;nimo","Este campo no puede exceder de 999,99.");
					flag = false;
				}
			}
		}
		if (!idTipoInstrumento.equalsIgnoreCase(ConstantesGenerales.INST_TIPO_SUBASTA_COMPETITIVA)) {
			_record.setValue("precioMaximo", _record.getValue("precioMinimo"));
		} else {
			if (_record.getValue("precioMaximo") == null || _record.getValue("precioMaximo").trim().equals("")) {
				_record.addError("Precio M&aacute;ximo","Este campo es requerido para Unidades de Inversi&oacute;n tipo Subasta Competitiva.");
				flag = false;
			} else {
				precioMaximo = new Double(_record.getValue("precioMaximo")).doubleValue();
				if (precioMaximo == 0.0) {
					_record.addError("Precio M&aacute;ximo","Este campo es requerido para Unidades de Inversi&oacute;n tipo Subasta Competitiva.");
					flag = false;
				} else if (precioMinimo > 999.99) {
					_record.addError("Precio M&aacute;ximo","Este campo no puede exceder de 999,99.");
					flag = false;
				} else {
				if ((precioMinimo != 0 || precioMaximo != 0) && precioMinimo > precioMaximo) {
					_record.addError("Para su informaci&oacute;n","El PrecioM&aacute;ximo debe ser mayor al Precio M&iacute;nimo");
					flag = false;
					}
				}
			}
		}
		if (indicadorMonto == 0) {
			if (_record.getValue("cantMinimaInversion") == null || _record.getValue("cantMinimaInversion").trim().equals("")) {
				_record.addError("Cantidad M&iacute;nima","Este campo es requerido para Unidades de Inversi&oacute;n tipo Subasta.");
				flag = false;
			} else {
				doubleAux1 = new Double(_record.getValue("cantMinimaInversion")).doubleValue();
				if (doubleAux1 == 0.0) {
					_record.addError("Cantidad M&iacute;nima","Este campo es requerido para Unidades de Inversi&oacute;n tipo Subasta.");
					flag = false;
				}
			}
			if (_record.getValue("cantMaximaInversion") == null || _record.getValue("cantMaximaInversion").trim().equals("")) {
				_record.addError("Cantidad M&aacute;xima","Este campo es requerido para Unidades de Inversi&oacute;n tipo Subasta.");
				flag = false;
			} else {
				doubleAux2 = new Double(_record.getValue("cantMaximaInversion")).doubleValue();
				if (doubleAux2 == 0.0) {
					_record.addError("Cantidad M&aacute;xima","Este campo es requerido para Unidades de Inversi&oacute;n tipo Subasta.");
					flag = false;
				}
			}
			if ((doubleAux1 != 0 || doubleAux2 != 0)  && doubleAux2 < doubleAux1){
				_record.addError("Para su informaci&oacute;n","La Cantidad Maxima debe ser mayor a la Cantidad M&iacute;nima para Unidades de Inversi&oacute;n tipo Subasta.");
				flag = false;
			}
		} else {
			if (_record.getValue("montoMinimoInversion") == null || _record.getValue("montoMinimoInversion").trim().equals("")) {
				_record.addError("Monto M&iacute;nimo","Este campo es requerido para Unidades de Inversi&oacute;n tipo Inventario.");
				flag = false;
			} else {
				doubleAux1 = new Double(_record.getValue("montoMinimoInversion")).doubleValue();
				if (doubleAux1 == 0.0) {
					_record.addError("Monto M&iacute;nimo","Este campo es requerido para Unidades de Inversi&oacute;n tipo Inventario.");
					flag = false;
				}
			}
			if (_record.getValue("montoMaximoInversion") == null || _record.getValue("montoMaximoInversion").trim().equals("")) {
				_record.addError("Monto M&aacute;ximo","Este campo es requerido para Unidades de Inversi&oacute;n tipo Inventario.");
				flag = false;
			} else {
				doubleAux2 = new Double(_record.getValue("montoMaximoInversion")).doubleValue();
				if (doubleAux2 == 0.0) {
					_record.addError("Monto M&aacute;ximo","Este campo es requerido para Unidades de Inversi&oacute;n tipo Inventario.");
					flag = false;
				}
			}			
			
			if ((doubleAux1 != 0 || doubleAux2 != 0) && doubleAux2 < doubleAux1){
				_record.addError("Para su informaci&oacute;n","El Monto M&aacute;ximo debe ser mayor al Monto M&iacute;nimo para Unidades de Inversi&oacute;n tipo Inventario.");
				flag = false;
			}	else{
				/* NM25287 SIMADI Entregable 2 15/02/2015
				 * Validacion de monto minimo alto valor*/
				if (tipoProductoId.equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL)||tipoProductoId.equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL)) {	
					if (_record.getValue("montoMinimoAltoValor") == null || _record.getValue("montoMinimoAltoValor").trim().equals("")) {
						_record.addError("Monto M&iacute;nimo Alto Valor", "Este campo es requerido para la Unidad de Inversi&oacute;n");
						flag = false;
					} else {
						doubleAux3 = new Double(_record.getValue("montoMinimoAltoValor")).doubleValue();
						if (doubleAux3 == 0.0) {
							_record.addError("Monto M&iacute;nimo Alto Valor", "Este campo es requerido para la Unidad de Inversi&oacute;n");
							flag = false;
						}
					}					
					if(doubleAux3<doubleAux1||doubleAux3>doubleAux2){
						_record.addError("Monto M&iacute;nimo Alto Valor", "El valor debe estar entre el rango de montos establecido");
						flag = false;						
					}
				}
			}
			
		}
		
		/* NM25287 SICAD II. 24/03/2014
		 * Validaciones para el registro de tasas propuestas mínimas y máximas para las unidades de inversion Subasta Divisas */
		if (tipoProductoId.equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA)||tipoProductoId.equalsIgnoreCase(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL)) {			
			if (_record.getValue("tasaPropuestaMinima") == null || _record.getValue("tasaPropuestaMinima").trim().equals("")) {
				_record.addError("Tasa Propuesta M&iacute;nima", "Este campo es requerido para Unidades de Inversi&oacute;n tipo Subasta Divisas.");
				flag = false;
			} else {
				doubleAux1 = new Double(_record.getValue("tasaPropuestaMinima")).doubleValue();
				if (doubleAux1 == 0.0) {
					_record.addError("Tasa Propuesta M&iacute;nima", "Este campo es requerido para Unidades de Inversi&oacute;n tipo Subasta Divisas.");
					flag = false;
				}
			}
			if (_record.getValue("tasaPropuestaMaxima") == null || _record.getValue("tasaPropuestaMaxima").trim().equals("")) {
				_record.addError("Tasa Propuesta M&aacute;xima", "Este campo es requerido para Unidades de Inversi&oacute;n tipo Subasta Divisas.");
				flag = false;
			} else {
				doubleAux2 = new Double(_record.getValue("tasaPropuestaMaxima")).doubleValue();
				if (doubleAux2 == 0.0) {
					_record.addError("Tasa Propuesta M&aacute;xima", "Este campo es requerido para Unidades de Inversi&oacute;n tipo Subasta Divisas.");
					flag = false;
				}
			}
			if ((doubleAux1 != 0 || doubleAux2 != 0) && doubleAux2 < doubleAux1) {
				_record.addError("Para su informaci&oacute;n", "La tasa propuesta M&aacute;xima debe ser mayor a la tasa propuesta M&iacute;nima para Unidades de Inversi&oacute;n tipo Subasta Divisas.");
				flag = false;
			}
		}		
		return flag;		
	}
}

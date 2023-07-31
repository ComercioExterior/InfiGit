package models.unidad_inversion.blotters.parametros_tp;

import megasoft.AbstractModel;
import megasoft.DataSet;

import com.bdv.infi.dao.InstrumentoFinancieroDAO;
import com.bdv.infi.dao.TipoPersonaDAO;
import com.bdv.infi.dao.TipoTransaccionFinancieraDAO;
import com.bdv.infi.dao.UIBlotterDAO;
import com.bdv.infi.dao.UIBlotterRangosDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.TransaccionFinanciera;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

/**
 * Clase que publica una pagina con las Asociacion entre Unidad de Inversion editada y los Blotter
 * @author Megasoft Computaci&oacute;n
 */
public class UIBlotterTPEdit extends AbstractModel implements UnidadInversionConstantes {
 	
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
	 * Identificador de la Unidad de Inversion 
	 */
	private long idUnidadInversion = 0;
	/**
	 * Identificador de instrumento financiero
	 */
	private String idInstFin ="";
	/**
	 * Identificador del Blotter
	 */
	private String idBlotter = "";	
	private String blotter = "";
	private String unidad = "";
	private int tipoOperacion =0; //1 = Electronico, 2= Efectivo
	private String formaOrden="";
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {	
		
		//Recuperamos de session el numero de la accion y la mandamos a la vista		
		String accion= getSessionObject("accion").toString();
		DataSet _accion=new DataSet();
		_accion.append("accion",java.sql.Types.VARCHAR);
		_accion.addNew();
		_accion.setValue("accion",accion);
		storeDataSet("accion", _accion);
		//fin de recuperacion y de envio a la vista
		
		TipoPersonaDAO boTP = new TipoPersonaDAO(_dso);
		boTP.listarTodos();
		
		//registrar los datasets exportados por este modelo
		storeDataSet("dsUnidadInversion", boUI.getDataSet());			
		storeDataSet("dsUIBlotter", boUIBlotter.getDataSet());	
		storeDataSet("dsTipoPersona", boTP.getDataSet());	
		
		if (_record.getValue("idTipoPersona").equals("X")) {
			DataSet ds = new DataSet();
			ds.append("tipper_id", java.sql.Types.VARCHAR);			
			ds.append("uiblot_comision_emisor", java.sql.Types.VARCHAR);
			ds.append("uiblot_pct_max_finan", java.sql.Types.VARCHAR);
			ds.append("uiblot_umi_inv_mto_min", java.sql.Types.VARCHAR);			
			ds.append("uiblot_umi_inv_mto_max", java.sql.Types.VARCHAR);
			ds.append("uiblot_precio_minimo", java.sql.Types.VARCHAR);	
			ds.append("uiblot_precio_maximo", java.sql.Types.VARCHAR);
			ds.append("uiblot_umi_um_cant_min", java.sql.Types.VARCHAR);
			ds.append("uiblot_umi_um_cant_max", java.sql.Types.VARCHAR);						
			ds.append("blotter", java.sql.Types.VARCHAR);
			ds.append("unidad", java.sql.Types.VARCHAR);
			ds.append("uiblot_trnfin_tipo", java.sql.Types.VARCHAR);
			ds.append("uiblot_tasa_prop_min", java.sql.Types.VARCHAR);
			ds.append("uiblot_tasa_prop_max", java.sql.Types.VARCHAR);
			//NM25287 TTS-491 SIMADI Entregable 1. Inclusion de campo monto minimo alto valor
			ds.append("uiblot_umi_inv_mto_min_a_valor", java.sql.Types.VARCHAR);
			ds.append("uiblot_tipo", java.sql.Types.VARCHAR);
			ds.addNew();
			ds.setValue("tipper_id", "X");			
			ds.setValue("uiblot_comision_emisor", "");
			ds.setValue("uiblot_pct_max_finan", "");
			ds.setValue("uiblot_umi_inv_mto_min", "");			
			ds.setValue("uiblot_umi_inv_mto_max", "");
			ds.setValue("uiblot_precio_minimo", "");	
			ds.setValue("uiblot_precio_maximo", "");
			ds.setValue("uiblot_umi_um_cant_min", "");
			ds.setValue("uiblot_umi_um_cant_max", "");				
			ds.setValue("uiblot_trnfin_tipo", "");
			ds.setValue("uiblot_tasa_prop_min", "");				
			ds.setValue("uiblot_tasa_prop_max", "");
			ds.setValue("uiblot_umi_inv_mto_min_a_valor", "");
			ds.setValue("uiblot_tipo", "1");
			//ds.setValue("blotter", blotter);
			//ds.setValue("unidad", unidad);

			storeDataSet("dsTPParametros", ds);
		}else {
			DataSet dst = boUIBTP.getDataSet();
			dst.first();dst.next();
			dst.setValue("trnfin_tipo", "MIS");
			storeDataSet("dsTPParametros", dst);
		}		

		if (boUI.getDataSet().getValue("tipo_producto_id").indexOf(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA) > -1 ||
				boUI.getDataSet().getValue("tipo_producto_id").indexOf(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL) > -1 ||
					boUI.getDataSet().getValue("tipo_producto_id").indexOf(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL) > -1 ||
						boUI.getDataSet().getValue("tipo_producto_id").indexOf(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL) > -1){		
			setActionTemplate("form_subasta_divisas.htm");
		}else{	
			setActionTemplate("form_subasta.htm");
			/*if (boUI.getDataSet().getValue("insfin_forma_orden").toLowerCase().indexOf("subasta") > -1) {
				setActionTemplate("form_subasta.htm");
			}else{
				setActionTemplate("form_inventario.htm");
			}*/
		}	
		
		//CONFIGURACION DE ESCENARIOS (MIS,BLOQ,DEB, MISC VALIDA SALDO) DE ACUERDO AL TIPO DE PRODUCTO
		TipoTransaccionFinancieraDAO tipoTransa = new TipoTransaccionFinancieraDAO(_dso);
		if (boUI.getDataSet().getValue("tipo_producto_id").indexOf(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL) > -1 ||
				boUI.getDataSet().getValue("tipo_producto_id").indexOf(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_CLAVENET_PERSONAL) > -1) { //||
					//Resolucion incidencia NM25287_19/05/2014
					//boUI.getDataSet().getValue("tipo_producto_id").indexOf(ConstantesGenerales.ID_TIPO_PRODUCTO_SICADII_RED_COMERCIAL) > -1){
			String tipoTransaccion[] = {TransaccionFinanciera.BLOQUEO};
			tipoTransa.listaEspecificos(tipoTransaccion);
					
		}else
		{
			String tipoTransaccion[] = {TransaccionFinanciera.BLOQUEO,TransaccionFinanciera.DEBITO,TransaccionFinanciera.MISCELANEO,TransaccionFinanciera.MISCELANEO_VERIFICABLE};
			tipoTransa.listaEspecificos(tipoTransaccion);
		}
		storeDataSet("transacciones", tipoTransa.getDataSet());
		
		//NM25287 TTS-504-SIMADI Efectivo Taquilla 24/08/2015
		DataSet ds = new DataSet();
		ds.append("uiblot_tipo", java.sql.Types.VARCHAR);		
		ds.append("trnfin_op_desc", java.sql.Types.VARCHAR);
		ds.addNew();
		ds.setValue("uiblot_tipo", "1");	
		ds.setValue("trnfin_op_desc", "Electr&oacute;nico");
		
		if(ConstantesGenerales.INST_FINANCIERO_SIMADI_TAQUILLA.equalsIgnoreCase(formaOrden)){				
			ds.addNew();
			ds.setValue("uiblot_tipo", "2");	
			ds.setValue("trnfin_op_desc", "Efectivo");				
		}
		
		storeDataSet("dstipoOperaciones", ds);
				
	}
	
	/**
	 * Validacion de los datos provenientes de la pagina
	 */
	public boolean isValid() throws Exception {
		InstrumentoFinancieroDAO instF =  new InstrumentoFinancieroDAO(_dso);
		boolean flag = super.isValid();
		
		// Si la validacion basada en el record.xml genero un error se envia a la pagina de error
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
		boUI.getDataSet().next();
		idInstFin=boUI.getDataSet().getValue("insfin_id");
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
		
		boUIBlotter.getDataSet().next();
//		double aux = 0;
//		if (boUIBlotter.getDataSet().getValue("uiblot_ganancia_red") != null)
//			aux = new Double(boUIBlotter.getDataSet().getValue("uiblot_ganancia_red")).doubleValue(); 
//		if (boUI.getDataSet().getValue("insfin_forma_orden").toLowerCase().startsWith("inventario") && aux == 0) {
//			_record.addError("Porcentaje de Ganancia de la Red", "Este campo es requerido para Unidades de Inversion tipo Inventario ");
//			return false;
//		}
		// Buscar el Parametros por Tipo de Persona
		//	1.-	Buscar el registro
		//	2.-	Si el Tipo es X 
		//		2.1.-	Si no existe	--> incluir un registro
		//		2.2.-	Si existe		--> rechazar la actualizacion		
		//	2.-	Si el Tipo es un valor de la tabla 
		//		2.1.-	Si existe		--> actualizar el registro
		//		2.2.-	Si no existe	--> rechazar la actualizacion		

		
			//NM25287 TTS-504-SIMADI Efectivo Taquilla 26/08/2015
			instF.listarPorId(idInstFin);
			if(instF.getDataSet().count()>0){
				instF.getDataSet().first();
				instF.getDataSet().next();
				formaOrden=instF.getDataSet().getValue("insfin_forma_orden");
				
			}
		if(_record.getValue("idTipoOperacion")!=null&&!"undefined".equalsIgnoreCase(_record.getValue("idTipoOperacion"))){
				
			tipoOperacion=Integer.parseInt(_record.getValue("idTipoOperacion"));
			boUIBTP = new UIBlotterRangosDAO(_dso);
			boUIBTP.listarBlotterRangos(idUnidadInversion, idBlotter, _record.getValue("idTipoPersona"),tipoOperacion);
			if (!_record.getValue("idTipoPersona").equals("X")) {
				if (boUIBTP.getDataSet().count() == 0){
					_record.addError("Para su informacion", "Los Parámetros por Tipo de Persona no estan registrados");
					return false;
				}
			} 
		}
		return flag;		
	}
}


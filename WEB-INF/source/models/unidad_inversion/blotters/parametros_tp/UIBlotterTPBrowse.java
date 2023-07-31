package models.unidad_inversion.blotters.parametros_tp;

import megasoft.AbstractModel;
import megasoft.DataSet;
import megasoft.Logger;

import com.bdv.infi.dao.UIBlotterDAO;
import com.bdv.infi.dao.UIBlotterRangosDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

/**
 * Clase que publica una pagina con los Blotter de una Unidad de Inversion editada
 * @author Megasoft Computaci&oacute;n
 */
public class UIBlotterTPBrowse extends AbstractModel implements UnidadInversionConstantes {
	 	
	/**
	 * Clase que encapsula la funcionalidad de la Unidad de Inversion
	 */
	private UnidadInversionDAO boUI;
	/**
	 * Clase que encapsula la funcionalidad de la Asociacion UI-Blotter 
	 */
	private UIBlotterDAO boUIBlotter = null;
	/**
	 * Identificador de la Unidad de Inversion 
	 */
	private long idUnidadInversion = 0;
	/**
	 * Identificador del Blotter
	 */
	private String idBlotter = "";
	private String blotter = "";	
	private String unidad = "";	

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
		
		//blotter = _req.getParameter("blotter");
		//unidad = _req.getParameter("unidad");
		
		// Buscar los parametros asociados
		UIBlotterRangosDAO boUIBTP = new UIBlotterRangosDAO(_dso);
		boUIBTP.listarBlotterRangos(idUnidadInversion, idBlotter);
		
		//	Cantidad de registros
		DataSet dsApoyo = new DataSet();
		dsApoyo.append("total_records", java.sql.Types.VARCHAR);
		dsApoyo.append("unidad", java.sql.Types.VARCHAR);
		dsApoyo.append("blotter", java.sql.Types.VARCHAR);
		dsApoyo.addNew();
		dsApoyo.setValue("total_records", "("+boUIBTP.getDataSet().count()+")");
		dsApoyo.setValue("unidad", "unidad");
		dsApoyo.setValue("blotter", "blotter");
		
		//registrar los datasets exportados por este modelo
		storeDataSet("dsUnidadInversion", boUI.getDataSet());			
		storeDataSet("dsUIBlotter", boUIBlotter.getDataSet());	
		storeDataSet("dsUIBlotterTP", boUIBTP.getDataSet());
		storeDataSet("dsApoyo", dsApoyo);	
		System.out.println("blotter: "+boUIBTP.getDataSet());
					
		// cambiar template de Inventario --> subasta
		if (boUI.getDataSet().getValue("tipo_producto_id").indexOf(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL) > -1 ||
			boUI.getDataSet().getValue("tipo_producto_id").indexOf(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA) > -1 ||
			boUI.getDataSet().getValue("insfin_forma_orden").indexOf(ConstantesGenerales.INST_TIPO_SUBASTA) > -1 
			//|| boUI.getDataSet().getValue("insfin_forma_orden").indexOf(ConstantesGenerales.INST_TIPO_SITME) > -1
			||	boUI.getDataSet().getValue("insfin_descripcion").indexOf(ConstantesGenerales.INST_TIPO_SITME) > -1				
			|| boUI.getDataSet().getValue("insfin_forma_orden").indexOf(ConstantesGenerales.INST_TIPO_SUBASTA_COMPETITIVA) > -1
			|| boUI.getDataSet().getValue("insfin_forma_orden").indexOf(ConstantesGenerales.INST_FINANCIERO_SIMADI_TAQUILLA) > -1) {////NM25287 TTS-504-SIMADI Efectivo Taquilla 24/08/2015
			setActionTemplate("form_subasta.htm");
		} else if (boUI.getDataSet().getValue("insfin_forma_orden").indexOf(ConstantesGenerales.INST_TIPO_INVENTARIO_CON_PRECIO) > -1) {
			setActionTemplate("form_inventario_precio.htm");
		}
		
	}
	
	/**
	 * Validacion de los datos provenientes de la pagina
	 */
	public boolean isValid() throws Exception {
		
		boolean flag = super.isValid();
		String accion= getSessionObject("accion").toString();
		
		// Si la validacion basada en el record.xml genero un error se envia a la pagina de error
		if (!flag) 	{
			return flag;
		}
		
		
		idUnidadInversion = Long.parseLong(_req.getParameter("idUnidadInversion"));
		
		Logger.info(this,"idUnidadInversion : "+idUnidadInversion);
		System.out.println("idUnidadInversion : "+idUnidadInversion);
		
		idBlotter = _req.getParameter("idBlotter");
		
		Logger.info(this,"idBlotter : "+idBlotter);
		System.out.println("idBlotter : "+idBlotter);
		
		boUI = new UnidadInversionDAO(_dso);
		boUI.listarPorId(idUnidadInversion);			
		boUI.getDataSet().next();
		if (Integer.parseInt(accion)==1){
			if (boUI.getDataSet().getValue("undinv_status").equalsIgnoreCase(UISTATUS_CERRADA)){
				_record.addError("Para su informacion", "La Unidad de Inversion no esta en codiciones de ser modificada");
				return flag;
			}
		}
			
		// Buscar el Blotter a procesar
		boUIBlotter = new UIBlotterDAO(_dso);
		boUIBlotter.listarPorId(idUnidadInversion, idBlotter);
		if ( boUIBlotter.getDataSet().count() == 0){
			_record.addError("Para su informacion", "El Blotter no esta registrado");
			return flag;
		}
		boUIBlotter.getDataSet().next();
		double aux = 0;
		if (boUIBlotter.getDataSet().getValue("uiblot_ganancia_red") != null)
			aux = new Double(boUIBlotter.getDataSet().getValue("uiblot_ganancia_red")).doubleValue(); 
		if (boUI.getDataSet().getValue("insfin_forma_orden").toLowerCase().startsWith("inventario") && aux == 0) {
			_record.addError("Porcentaje de Ganancia de la Red", "Este campo es requerido para Unidades de Inversion tipo Inventario ");
			return false;
		}
		
		return flag;	
	}
}

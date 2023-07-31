package models.unidad_inversion.unidad_inversion;

import java.util.ArrayList;

import megasoft.AbstractModel;
import megasoft.DataSet;

import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;



/**
 * Clase que publica una pagina con la Unidad de Inversion editada
 * @author Megasoft Computaci&oacute;n
 */
public class UnidadInversionEdit extends AbstractModel implements UnidadInversionConstantes{

	/**
	 * Identificador del registro a modificar
	 */
	private long idUnidadInversion = 0;
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
		
		String strIdUnidadInversion = null;
		// Buscar la unidad de inversion a mostrar :
		//	1.- Desde el Request si es la primera vez
		//	2.-	Desde la Sesion si es una re-entrancia
		if (_req.getParameter("idUnidadInversion") != null) {
			idUnidadInversion = Long.parseLong(_req.getParameter("idUnidadInversion"));
		} else {
			strIdUnidadInversion = (String)_req.getSession().getAttribute("idUnidadInversion");
			idUnidadInversion = Long.parseLong(strIdUnidadInversion);
		}

		UnidadInversionDAO boUI = new UnidadInversionDAO(_dso);
		int cant = boUI.listarPorId(idUnidadInversion);	
		if(cant == 0) {
			_record.addError("Para su informacion", "No hay Unidades de Inversion con los criterios dados");
			return;
		}
	
		// Editar campos de montos
		 
		DataSet dsUI = boUI.getDataSet();
		dsUI.next();
		
		String var = ""; double dbVar = 0;
		var = dsUI.getValue("undinv_umi_unidad");
		dbVar = new Double(var).doubleValue();
		if (dbVar == 0) {
			dsUI.setValue("undinv_umi_unidad", "");
		} else {
			dsUI.setValue("undinv_umi_inv_total", "");
			dsUI.setValue("undinv_umi_inv_disponible", "");			
			dsUI.setValue("undinv_tasa_pool", "");	
			dsUI.setValue("undinv_rendimiento", "");	
		} 
		dsUI.setValue("instrumentoFinanciero", dsUI.getValue("insfin_id")+";"+dsUI.getValue("insfin_forma_orden"));	
		
		// Obtener los valores de las tablas asociadas
		ArrayList arregloDataSet = new ArrayList();
		UnidadInversionFK classFk = new UnidadInversionFK(_dso);
		Object objAux = classFk.execute();
		if (objAux instanceof String) {
			throw new Exception((String) objAux);			
		}  else {
			arregloDataSet = (ArrayList)objAux;
		}
		
		if (strIdUnidadInversion == null) {
			_req.getSession().removeAttribute("idUnidadInversion");
			_req.getSession().setAttribute("idUnidadInversion",String.valueOf(idUnidadInversion));
		}
		
				
		// Buscar los mensajes de integridad de las asociaciones
		DataSet dsApoyo = new DataSet();
		dsApoyo.append("asociaciones_ini", java.sql.Types.VARCHAR);
		dsApoyo.append("asociaciones_fin", java.sql.Types.VARCHAR);
		dsApoyo.append("cambiomonedaui", java.sql.Types.VARCHAR);
		dsApoyo.addNew();
		
		DataSet dsAsociaciones = new DataSet();
		
		
		
		//Valido para evitar pasar si no viene de la pantalla de publicaci&oacute;n o consulta de unidad de inversi&oacute;n	
		boolean asociaciones = boUI.dataCompleta(idUnidadInversion,ConstantesGenerales.LOCALIZACION_INVOCACION_EDIT_UI);
		if (asociaciones) {
			dsApoyo.setValue("asociaciones_ini", "<!-----");
			dsApoyo.setValue("asociaciones_fin", "------>");
			if(Integer.parseInt(accion)==1){
				boUI.modificarStatus(idUnidadInversion, UISTATUS_REGISTRADA);
			}
		} else {
			dsApoyo.setValue("asociaciones_ini", " ");
			dsApoyo.setValue("asociaciones_fin", " ");			
			dsAsociaciones = boUI.getDataSet();
			if(Integer.parseInt(accion)==1||Integer.parseInt(accion)==4){
				boUI.modificarStatus(idUnidadInversion, UISTATUS_INICIO);
			}
		}
		
		boolean cambioMonedaUI=boUI.ordenesPorId(idUnidadInversion);
		if (cambioMonedaUI==false){
			dsApoyo.setValue("cambiomonedaui","disabled='true'");
			
		}else{
			dsApoyo.setValue("cambiomonedaui","");
		}
		
		/*NM25287 SICADII. Inclusion de parametro en unidad de inversion para determinar a cuales unidades de inversión
		 *se les permite cancelar ordenes 25/03/2014 */
		DataSet _dsChecked=new DataSet();
		_dsChecked.append("batchAdjChecked",java.sql.Types.VARCHAR);
		_dsChecked.append("batchLiqChecked",java.sql.Types.VARCHAR);
		_dsChecked.append("cancelacion_checked_si", java.sql.Types.VARCHAR);
		_dsChecked.append("cancelacion_checked_no", java.sql.Types.VARCHAR);
		//NM26659 - 03/09/2015 Modificacion para la inclusion de tipo de negocio SIMADI Taquila
		/*_dsChecked.append("checked_negocio_no_aplica", java.sql.Types.VARCHAR);
		_dsChecked.append("checked_negocio_alto_valor", java.sql.Types.VARCHAR);
		_dsChecked.append("checked_negocio_bajo_valor", java.sql.Types.VARCHAR);*/
		_dsChecked.append("checked_negocio_no_aplica", java.sql.Types.VARCHAR);
		_dsChecked.append("checked_negocio_alto_valor", java.sql.Types.VARCHAR);
		_dsChecked.append("checked_negocio_bajo_valor", java.sql.Types.VARCHAR);
		_dsChecked.append("checked_negocio_taquilla", java.sql.Types.VARCHAR);
		_dsChecked.addNew();
		if (dsUI.getValue("in_cobro_batch_adj").equals("1")){
			_dsChecked.setValue("batchadjchecked","checked");	
		}else{
			_dsChecked.setValue("batchadjchecked"," ");
		}
		if (dsUI.getValue("in_cobro_batch_liq").equals("1")){
			_dsChecked.setValue("batchliqchecked","checked");	
		}else{
			_dsChecked.setValue("batchliqchecked"," ");
		}
		//NM25287 TTS-491 SIMADI Contingencia Configuracion de parametro para manejo de alto valor / bajo valor
		_dsChecked.setValue("checked_negocio_no_aplica","");	
		_dsChecked.setValue("checked_negocio_alto_valor","");	
		_dsChecked.setValue("checked_negocio_bajo_valor","");
		
		switch (Integer.parseInt(dsUI.getValue("tipo_negocio"))) {
			case 0:{
				_dsChecked.setValue("checked_negocio_no_aplica","checked");		//Manejo de alto valor y bajo valor No aplica
				break;
			}
			case 1:{
				_dsChecked.setValue("checked_negocio_alto_valor","checked");				//Manejo de alto valor
				break;
			}				
			case 2:{	
				_dsChecked.setValue("checked_negocio_bajo_valor","checked");  			//Manejo de bajo valor
				break;
			}
			case 3:{	
				_dsChecked.setValue("checked_negocio_taquilla","checked");  			//Manejo de bajo valor
				break;
			}
			default:{
				break;
			}
		}
		_dsChecked.setValue("cancelacion_checked_si", dsUI.getValue("INDC_PERMITE_CANCELACION").equals("1")?"checked":"");
		_dsChecked.setValue("cancelacion_checked_no", dsUI.getValue("INDC_PERMITE_CANCELACION").equals("1")?"":"checked");

		_req.getSession().setAttribute("idUnidadInversion",String.valueOf(idUnidadInversion));
		//registrar los datasets exportados por este modelo
		storeDataSet("dsUnidadInversion", dsUI);	
		storeDataSet("dsInstrumentoFinanciero", (DataSet)arregloDataSet.get(0));	
		storeDataSet("dsMoneda", (DataSet)arregloDataSet.get(1));	
		storeDataSet("dsEmpresa",(DataSet)arregloDataSet.get(2));
		storeDataSet("dsTipoMercado", (DataSet)arregloDataSet.get(3));	
		storeDataSet("dsAsociaciones",dsAsociaciones);	
		storeDataSet("dsApoyo",dsApoyo);
		storeDataSet("dsChecked",_dsChecked);
		
		storeDataSet("meridiano", boUI.indMeridiano());
		storeDataSet("dsMin", boUI.minutos());	
		storeDataSet("dsHora", boUI.horas());
	}
}
package models.unidad_inversion.comisiones;

import megasoft.AbstractModel;
import megasoft.DataSet;
import megasoft.Page;
import com.bdv.infi.dao.UIComisionDAO;
import com.bdv.infi.dao.UnidadInversionDAO;


/**
 * Clase que publica una pagina con los Blotter de una Unidad de Inversion editada
 * @author Megasoft Computaci&oacute;n
 */
public class UIComisionesBrowse extends AbstractModel {
	/**
	 * Identificador del registro a modificar
	 */
	private long idUnidadInversion = 0;
	
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {			
		
		String aux ="";
		String reglas="";
	
		//Recuperamos de session el numero de la accion y la mandamos a la vista
		String accion= getSessionObject("accion").toString();
		DataSet _accion=new DataSet();
		_accion.append("accion",java.sql.Types.VARCHAR);
		_accion.addNew();
		_accion.setValue("accion",accion);
		storeDataSet("accion", _accion);
	  //fin de recuperacion y de envio a la vista
		
		String strIdUnidadInversion = (String)_req.getSession().getAttribute("idUnidadInversion");
		if (strIdUnidadInversion == null) {
			return;
		}
		idUnidadInversion = Long.parseLong(strIdUnidadInversion);		
		
		UnidadInversionDAO boUI = new UnidadInversionDAO(_dso);		
		
		DataSet dsUnidadInversion = boUI.getDataSet();
		
		UIComisionDAO uiComisionDAO = new UIComisionDAO(_dso);
		uiComisionDAO.listarTodas(idUnidadInversion);
		DataSet comisiones = uiComisionDAO.getDataSet();
		
		
		aux = getResource("reglas_browse.htm");
		//buscar reglas para cada comision
		while(comisiones.next()){
			
			reglas = aux;
			uiComisionDAO.obtenerReglasDeComisionUI(comisiones.getValue("comision_id"));
			
			//if(uiComisionDAO.getDataSet().count()>0){
				Page page = new Page(reglas);
				
				page.repeat(uiComisionDAO.getDataSet(), "rows_reg");
				page.replace("@total_reglas@", String.valueOf(uiComisionDAO.getDataSet().count()));
				
				comisiones.setValue("reglas", page.toString());
			/*}else
				comisiones.setValue("reglas", "<div style='display:none'>&nbsp;</div>");*/
			
		}
				
		DataSet dsApoyo = new DataSet();
		dsApoyo.append("boton_grabar_ini", java.sql.Types.VARCHAR);
		dsApoyo.append("boton_grabar_fin", java.sql.Types.VARCHAR);	
		dsApoyo.append("total_records", java.sql.Types.VARCHAR);
		dsApoyo.addNew();
		if (boUI.getDataSet().count() == 0) {
			dsApoyo.setValue("boton_grabar_ini", "<!----");
			dsApoyo.setValue("boton_grabar_fin", "--->");	
		} else {
			dsApoyo.setValue("boton_grabar_ini", "");
			dsApoyo.setValue("boton_grabar_fin", "");	
		}
		dsApoyo.setValue("total_records", "("+comisiones.count()+")");
		
		//registrar los datasets exportados por este modelo
		storeDataSet("dsUnidadInversion", dsUnidadInversion);			
		storeDataSet("ds_ui_comisiones", comisiones);	
		storeDataSet("dsApoyo", dsApoyo);	
	}
}

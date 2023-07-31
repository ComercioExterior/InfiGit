package models.unidad_inversion.campos_dinamicos;

import megasoft.AbstractModel;
import megasoft.DataSet;

import com.bdv.infi.dao.UICamposDinamicosDAO;
import com.bdv.infi.dao.UnidadInversionDAO;


/**
 * Clase que publica una pagina con los Campos Dinamicos de una Unidad de Inversion editada
 * @author Megasoft Computaci&oacute;n
 */
public class UICamposEdit extends AbstractModel {
	 
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
		
		String strIdUnidadInversion = (String)_req.getSession().getAttribute("idUnidadInversion");
		if (strIdUnidadInversion == null) {
			return;
		}
		idUnidadInversion = Long.parseLong(strIdUnidadInversion);
		
		UnidadInversionDAO boUI = new UnidadInversionDAO(_dso);
		int cant = boUI.listarPorId(idUnidadInversion);	
		if(cant == 0) {
			_record.addError("Para su informacion", "No hay Unidades de Inversion con los criterios dados");
			return;
		}
		DataSet dsUI = boUI.getDataSet();
		
		// Buscar la informacion de los indicadores
		UICamposDinamicosDAO boCamposDinamicos = new UICamposDinamicosDAO(_dso);
		boCamposDinamicos.listarCampoDinamicosPorID(idUnidadInversion);
		DataSet dsUICamposDinamicos = boCamposDinamicos.getDataSet();
		
		int i=0;
		while(dsUICamposDinamicos.next()) {
			dsUICamposDinamicos.setValue("fila", String.valueOf(i));
			++i;
		}
		
		DataSet dsApoyo = new DataSet();
		dsApoyo.append("boton_grabar_ini", java.sql.Types.VARCHAR);
		dsApoyo.append("boton_grabar_fin", java.sql.Types.VARCHAR);	
		dsApoyo.append("total_records", java.sql.Types.VARCHAR);		
		dsApoyo.addNew();
		if (dsUICamposDinamicos.count() == 0) {
			dsApoyo.setValue("boton_grabar_ini", "<!----");
			dsApoyo.setValue("boton_grabar_fin", "--->");	
		} else {
			dsApoyo.setValue("boton_grabar_ini", "");
			dsApoyo.setValue("boton_grabar_fin", "");	
		}
		dsApoyo.setValue("total_records", "("+dsUICamposDinamicos.count()+")");
				
		//registrar los datasets exportados por este modelo
		storeDataSet("dsUICamposDinamicos", dsUICamposDinamicos);	
		storeDataSet("dsUnidadInversion", dsUI);			
		storeDataSet("dsApoyo", dsApoyo);			
	}
}

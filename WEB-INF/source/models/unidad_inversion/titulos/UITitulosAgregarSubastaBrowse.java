package models.unidad_inversion.titulos;

import megasoft.AbstractModel;
import megasoft.DataSet;

public class UITitulosAgregarSubastaBrowse extends AbstractModel{
	
	public void execute() throws Exception {
		DataSet unidadInversion = new DataSet();
	
		unidadInversion.append("ui_id",  java.sql.Types.VARCHAR);
		unidadInversion.addNew();
		unidadInversion.setValue("ui_id", (String)_req.getSession().getAttribute("idUnidadInversion"));
		
		storeDataSet("dsUnidadInversion", unidadInversion);
	}
}
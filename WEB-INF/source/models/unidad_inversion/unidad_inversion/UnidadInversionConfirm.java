package models.unidad_inversion.unidad_inversion;

import megasoft.AbstractModel;
import megasoft.DataSet;

import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;


/**
 * Clase que publica una pagina con la Unidad de Inversion para obtener la confirmacion de aplicar una eliminacion
 * @author Megasoft Computaci&oacute;n
 */
public class UnidadInversionConfirm extends AbstractModel implements UnidadInversionConstantes{
	 
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {			
		
		int idUnidadInversion = Integer.parseInt(_req.getParameter("idUnidadInversion"));
		
		UnidadInversionDAO boUI = new UnidadInversionDAO(_dso);
		int cant = boUI.listarPorId(idUnidadInversion);	
		if(cant == 0) {
			_record.addError("Para su informacion", "No hay Unidades de Inversion con los criterios dados");
			return;
		}
		
		DataSet dsUI = boUI.getDataSet();
		dsUI.next();
		if (dsUI.getValue("undinv_status").equalsIgnoreCase(UISTATUS_CERRADA) || dsUI.getValue("undinv_status").equalsIgnoreCase(UISTATUS_PUBLICADA)){
			_record.addError("Para su informacion", "La Unidad de Inversion no esta en codiciones de ser eliminada");
			return;
		}
		//registrar los datasets exportados por este modelo
		storeDataSet("dsUnidadInversion", dsUI);		
	}
}

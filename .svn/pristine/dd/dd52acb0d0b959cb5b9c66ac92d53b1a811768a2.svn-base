package models.unidad_inversion.unidad_inversion;

import megasoft.AbstractModel;
import megasoft.DataSet;

import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

/**
 * Clase que recupera los datos de la pagina de Confirmacion de eliminar de una Unidad de Inversion y la aplica en la base de datos
 * @author Megasoft Computaci&oacute;n
 */
public class UnidadInversionDelete extends AbstractModel implements UnidadInversionConstantes {
	
	/**
	 * Identificador del registro a modificar
	 */
	private int idUnidadInversion = 0;
	
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {	
		
		// Buscar la Unidad de Inversion para verificar si esta condiciones de ser modificada
		idUnidadInversion = Integer.parseInt(_req.getParameter("idUnidadInversion"));
		
		UnidadInversionDAO boUI = new UnidadInversionDAO(_dso);
		OrdenDAO ordenDAO = new OrdenDAO(_dso);
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
		
		
		int cantOrdenes=ordenDAO.cantidadOrdenesUnidadInversion(idUnidadInversion);		
		if(cantOrdenes==0){
			//  Aplicar persistencia
			int respuesta = boUI.eliminar(idUnidadInversion);
			if (respuesta != 0) {
				_record.addError("Para su informacion", "Problemas de ingreso de datos");
			}
		}else{			
			_record.addError("Para su informacion", "La Unidad de Inversión posee ordenes registradas. Valide si es necesario 'Cancelar' la Unidad de Inversión");
			throw new Exception("La Unidad de Inversión no se puede eliminar ya que posee ordenes registradas, para realizar el proceso de cancelacion de dichas ordenes debe publicar la Unidad de Inversion");
		}
		
	}
}

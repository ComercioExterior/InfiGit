package models.unidad_inversion.empresas;

import megasoft.AbstractModel;
import megasoft.DataSet;

import com.bdv.infi.dao.UIEmpresaDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

/**
 * Clase que recibe una asociacion entre una Unidad de Inversion y una Empresa para eliminarla y aplica la persistencia en la base de datos
 * @author Megasoft Computaci&oacute;n
 */
public class UIEmpresaDelete extends AbstractModel implements UnidadInversionConstantes {

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
		int cant = boUI.listarPorId(idUnidadInversion);	

		if(cant == 0) {
			_record.addError("Para su informacion", "No hay Unidades de Inversion con los criterios dados");
			return;
		}
		
		DataSet dsUI = boUI.getDataSet();
		dsUI.next();
		if (dsUI.getValue("undinv_status").equalsIgnoreCase(UISTATUS_CERRADA)){
			_record.addError("Para su informacion", "La Unidad de Inversion no esta en codiciones de ser modificada");
			return;
		}
		
		
		// Buscar la Empresa a procesar
		UIEmpresaDAO boUIEmpresa = new UIEmpresaDAO(_dso);
		boUIEmpresa.listarPorId(idUnidadInversion, _req.getParameter("idEmpresa"));
		if ( boUIEmpresa.getDataSet().count() == 0){
			_record.addError("Para su informacion", "La Unidad de Inversion no esta en codiciones de ser modificada");
			return;
		}

		//  Aplicar persistencia
		boUIEmpresa.eliminar(idUnidadInversion, _req.getParameter("idEmpresa"));
		
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
		
	}
	
	/**
	 * Completar el URL a enviar con la clave del registro a recuperar
	 */
	public String getRedirectParameters() throws Exception  {
		return "idUnidadInversion=" + idUnidadInversion;
	}
}

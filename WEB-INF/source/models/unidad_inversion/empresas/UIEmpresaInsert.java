package models.unidad_inversion.empresas;

import megasoft.AbstractModel;
import megasoft.DataSet;

import com.bdv.infi.dao.UIEmpresaDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.data.UIEmpresa;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

/**
 * Clase que recupera los datos de la pagina de Ingreso de una Asociacion entre una Unidad de Inversion y los registra en la base de datos
 * @author Megasoft Computaci&oacute;n
 */
public class UIEmpresaInsert extends AbstractModel implements UnidadInversionConstantes {
	/**
	 * Identificador del registro a modificar
	 */
	private long idUnidadInversion = 0;
	
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {	
		
		// Buscar la Unidad de Inversion para verificar si esta condiciones de ser modificada
		idUnidadInversion = Long.parseLong(_req.getParameter("idUnidadInversion"));
		
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

		// campos obligatorios recuperados de la pagina
		String [] idEmpresa = _req.getParameterValues("idEmpresa");
		int ca = idEmpresa.length -1;
		if (ca == 0)
			return;
		
		// Aplicar la persistencia
		UIEmpresaDAO boEmpresas = new UIEmpresaDAO(_dso);
		UIEmpresa beanUIE = new UIEmpresa();
		beanUIE.setIdUnidadInversion(idUnidadInversion);
		for (int i=0; i< ca; i++) {	
			if (idEmpresa[i].equals("0")) {
				continue;
			}
			beanUIE.setIdEmpresa(idEmpresa[i]);
			boEmpresas.insertar(beanUIE);
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
		
	}
	
	/**
	 * Completar el URL a enviar con la clave del registro a recuperar
	 */
	public String getRedirectParameters() throws Exception  {
		return "entry=insert&idUnidadInversion=" + idUnidadInversion;
	}
}

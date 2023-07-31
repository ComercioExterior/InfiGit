package models.unidad_inversion.campos_dinamicos;

import megasoft.AbstractModel;
import megasoft.DataSet;

import com.bdv.infi.dao.UICamposDinamicosDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

/**
Clase que recupera los datos de la pagina de actualizacion de las relaciones entre Unidad de Inversion y Campos Dinamicos y aplica la persistencia en la base de datos
 * @author Megasoft Computaci&oacute;n
 */
public class UICamposUpdate extends AbstractModel implements UnidadInversionConstantes {
	
	/**
	 * Identificador del registro a modificar
	 */
	private long idUnidadInversion = 0;
	
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {	
		
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
		dsUI.next();
		if (dsUI.getValue("undinv_status").equalsIgnoreCase(UISTATUS_CERRADA)){
			_record.addError("Para su informacion", "La Unidad de Inversion no esta en codiciones de ser modificada");
			return;
		}
		
		// campos obligatorios recuperados de la pagina
		String [] strIdCampoDinamicoAnterior = _req.getParameterValues("idCampoDinamico");
		
		int ca = strIdCampoDinamicoAnterior.length -1;
		long [] idsCampoDinamicoPrevio  = new long [ca];

		for (int i = 0; i < ca; i++) {		
			idsCampoDinamicoPrevio [i] = new Long(strIdCampoDinamicoAnterior[i]).longValue();
		}
		
		// Aplicar la persistencia
		UICamposDinamicosDAO boCamposDinamicos = new UICamposDinamicosDAO(_dso);
		boCamposDinamicos.setIdsCampoDinamicoPrevio(idsCampoDinamicoPrevio);
		boCamposDinamicos.eliminar(idUnidadInversion);
		
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
		return "entry=update";
	}
}

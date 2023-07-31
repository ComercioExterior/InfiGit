package models.unidad_inversion.blotters.parametros_tp;

import megasoft.AbstractModel;

import com.bdv.infi.dao.UIBlotterDAO;
import com.bdv.infi.dao.UIBlotterRangosDAO;
import com.bdv.infi.dao.UIDocumentosDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.data.UIBlotterRangos;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

/**
 * Clase que elimina los parametros de las Asociacion entre Unidad de Inversion editada y los Blotter
 * @author Megasoft Computaci&oacute;n
 */
public class UIBlotterTPDelete  extends AbstractModel implements UnidadInversionConstantes{
 	
	/**
	 * Clase que encapsula la funcionalidad de la Unidad de Inversion
	 */
	private UnidadInversionDAO boUI;
	/**
	 * Clase que encapsula la funcionalidad de la Asociacion UI-Blotter 
	 */
	private UIBlotterDAO boUIBlotter = null;
	/**
	 * Clase que encapsula la funcionalidad de los Parametros de la Asociacion UI-Blotter
	 */
	private UIBlotterRangosDAO boUIBTP = null;
	/**
	 * Clase que encapsula la funcionalidad de la Asociacion UI-Documentos 
	 */
	private UIDocumentosDAO uIDocumentosDAO=null;
	/**
	 * Bean que encapsula la funcionalidad de los Parametros de la Asociacion UI-Blotter
	 */
	private UIBlotterRangos beanUIBlotterRangos = new UIBlotterRangos(); 
	/**
	 * Identificador de la Unidad de Inversion 
	 */
	private long idUnidadInversion = 0;
	/**
	 * Identificador del Blotter
	 */
	private String idBlotter = "";	

	/**
	 * Identificador del Blotter
	 */
	private int idTipoOperacion = 0;	 //1 = Electronico, 2= Efectivo
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {			


		boUIBTP.eliminar(idUnidadInversion, idBlotter, _record.getValue("idTipoPersona"),idTipoOperacion); //Modif NM25287 TTS-504-SIMADI Efectivo Taquilla 26/08/2015
		//NM26659 Modificacion Req. TTS_446 Eliminacion documentos asociados a la unidad inversion
		uIDocumentosDAO.eliminarDocumentosUI(idUnidadInversion, idBlotter, _record.getValue("idTipoPersona"));
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
		
		storeDataSet("_record", _record);
	}	
	
	/**
	 * Completar el URL a enviar con la clave del registro a recuperar
	 */
	public String getRedirectParameters() throws Exception  {
		return "idUnidadInversion=" + idUnidadInversion + "&idBlotter="+idBlotter;
	}
	
	/**
	 * Validacion de los datos provenientes de la pagina
	 */
	public boolean isValid() throws Exception {
		
		boolean flag = super.isValid();
		
		// Si la validacion basada en el record.xml genero un error se envia a la pagina de error
		if (!flag) 	{
			return flag;
		}
		
		uIDocumentosDAO=new UIDocumentosDAO(_dso);
		// Buscar la Unidad de Inversion para verificar si esta condiciones de ser modificada		
		idUnidadInversion = Long.parseLong(_record.getValue("idUnidadInversion"));
		boUI = new UnidadInversionDAO(_dso);
		int cant = boUI.listarPorId(idUnidadInversion);	
		if(cant == 0) {
			_record.addError("Para su informacion", "No hay Unidades de Inversion con los criterios dados");
			return false;
		}
		boUI.getDataSet().next();
		if (boUI.getDataSet().getValue("undinv_status").equalsIgnoreCase(UISTATUS_CERRADA)){
			_record.addError("Para su informacion", "La Unidad de Inversion no esta en codiciones de ser modificada");
			return false;
		}
		
		// Buscar el Blotter a procesar
		idBlotter = _record.getValue("idBlotter");
		
		boUIBlotter = new UIBlotterDAO(_dso);
		boUIBlotter.listarPorId(idUnidadInversion, idBlotter);
		if ( boUIBlotter.getDataSet().count() == 0) {
			_record.addError("Para su informacion", "El Blotter no esta registrado");
			return false;
		}
		
		// Buscar el Parametros por Tipo de Persona
		//	1.-	Buscar el registro
		//	2.-	Si el Tipo es X 
		//		2.1.-	Si no existe	--> incluir un registro
		//		2.2.-	Si existe		--> rechazar la actualizacion		
		//	2.-	Si el Tipo es un valor de la tabla 
		//		2.1.-	Si existe		--> actualizar el registro
		//		2.2.-	Si no existe	--> rechazar la actualizacion		
		
		//NM25287 TTS-504-SIMADI Efectivo Taquilla 26/08/2015
		idTipoOperacion=Integer.parseInt(_record.getValue("idTipoOperacion"));
		
		boUIBTP = new UIBlotterRangosDAO(_dso);
		boUIBTP.listarBlotterRangos(idUnidadInversion, idBlotter, _record.getValue("idTipoPersona"),idTipoOperacion);
		if (boUIBTP.getDataSet().count() == 0){
			_record.addError("Para su informacion", "Los Parámetros por Tipo de Persona no estan registrados");
			return false;			
		}
		
		return flag;		
	}
}

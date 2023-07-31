package models.unidad_inversion.indicadores;

import megasoft.AbstractModel;
import megasoft.DataSet;

import com.bdv.infi.dao.UIIndicadoresDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

/**
 * Clase que recupera los datos de la pagina de Ingreso de Asociaciones entre Indicadores y una Unidad de Inversion y los registra en la base de datos
 * @author Megasoft Computaci&oacute;n
 */
public class UIIndicadoresInset extends AbstractModel implements UnidadInversionConstantes {
	
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
		String [] strIdIndicador = _req.getParameterValues("idIndicador");
		String [] strIndicaRequerido = _req.getParameterValues("indicaRequerido");
		
		int ca = strIdIndicador.length -1;
		String [] idsIndicador  = new String [ca];
		String [] idsIndicadorPrevio = new String [ca];
		int [] indicaRequerido = new int [ca];
		int [] indicaRequeridoPrevio = new int [ca];

		for (int i = 0; i < ca; i++) {		
			idsIndicador [i] = strIdIndicador[i];
			indicaRequerido[i] = new Integer(strIndicaRequerido[i]).intValue();
			idsIndicadorPrevio[i] = "-1";
			indicaRequeridoPrevio[i] = indicaRequerido[i]; 
		}
		
		// Aplicar la persistencia
		UIIndicadoresDAO boIndicador  = new UIIndicadoresDAO(_dso);
		boIndicador.setIdsIndicador (idsIndicador );
		boIndicador.setIdsIndicadorPrevio(idsIndicadorPrevio);
		boIndicador.setIndicaRequerido(indicaRequerido);
		boIndicador.setIndicaRequeridoPrevio(indicaRequeridoPrevio);
		boIndicador.actualizaAsociacion(idUnidadInversion);
		
		//Si venimos del modulo de modificacion de uidad de inversion debemos cambiar el estatus de la unidad 
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
		return "entry=update&idUnidadInversion=" + idUnidadInversion;
	}
}

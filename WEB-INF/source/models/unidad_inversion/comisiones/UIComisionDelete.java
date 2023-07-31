package models.unidad_inversion.comisiones;

import megasoft.AbstractModel;
import megasoft.DataSet;
import megasoft.db;
import com.bdv.infi.dao.UIComisionDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.data.UIComision;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

/**
 * Clase que recibe la informacion de la Asociacion que va eliminar e implementa la persistencia
 * @author Megasoft Computaci&oacute;n
 */
public class UIComisionDelete extends AbstractModel implements UnidadInversionConstantes {

	/**
	 * Identificador del registro a modificar
	 */
	private int idUnidadInversion = 0;
	
	
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception {	
		
		
		String[] arrSql;		
		
		UIComision uiComision = new UIComision();			
			
		UIComisionDAO uiComisionDAO = new UIComisionDAO(_dso);
		UnidadInversionDAO boUI = new UnidadInversionDAO(_dso);			
		
		//colocar valores al objeto comision
		String idComision = _req.getParameter("id");
		
		uiComision.setIdComision(Long.parseLong(idComision));
		uiComision.setIdUnidadInversion(idUnidadInversion);
		
		////////////////////////////////////////////////////////////////////////////////////////////////////////////
					
		//Eliminar Comision
		arrSql = uiComisionDAO.eliminar(uiComision);
								
		//ejecutar arreglo de sentencias sql
		db.execBatch(_dso, arrSql);
		
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
	
	/**
	 * Validaciones Basicas del action
	 * @return true si la clase y sus par&aacute;metros son v&aacute;lidos, false en caso contrario 
	 * @throws Exception
	 */	
	public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();
		// Buscar la Unidad de Inversion para verificar si esta condiciones de ser modificada
		idUnidadInversion = Integer.parseInt(_req.getParameter("id_unidad_inversion"));
		
		UnidadInversionDAO boUI = new UnidadInversionDAO(_dso);
		boUI.listarPorId(idUnidadInversion);	

		 
		if (flag)
		{				
					
			DataSet ds = boUI.getDataSet();
			ds.next();
			if (ds.getValue("undinv_status").equalsIgnoreCase(UISTATUS_CERRADA)){
				_record.addError("Unidad de Inversi&oacute;n / Comisiones", "La Unidad de Inversi&oacute;n no puede ser modificada ya que se encuentra en estatus "+UISTATUS_CERRADA);
				flag = false;	
			}	
			

			/////////////////////////////////////////////////////////////////////////////////
		}
		return flag;	
	}

}

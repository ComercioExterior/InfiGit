package models.unidad_inversion.cancelacion;

import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

import megasoft.*;

/**
 * Clase encargada de ejecutar la consulta de T&iacute;tulos en custodia de de un cliente en particular que pueden ser bloqueados
 * @author Erika Valerio, Megasoft Computaci&oacute;n
 */
public class ViewOrdenes extends AbstractModel
{
	private long idUnidadInversion = 0;
	DataSet datosUI = null;
	/**
	 * Ejecuta la transaccion del modelo
	 */
	public void execute() throws Exception
	{			
		
		OrdenDAO ordenDAO = new OrdenDAO(_dso);
				
		ordenDAO.listarOrdenesCancelar(idUnidadInversion, StatusOrden.REGISTRADA);	
		
		
		_req.getSession().setAttribute("ordenesCancelar", ordenDAO.getDataSet());
		//registrar los datasets exportados por este modelo
		storeDataSet("datos_unidad", datosUI);	
		storeDataSet("table", ordenDAO.getDataSet());
		storeDataSet("datos", ordenDAO.getTotalRegistros());
	
		
	}
	
	/**
	 * Validaciones Basicas del action
	 * @return true si la clase y sus par&aacute;metros son v&aacute;lidos, false en caso contrario 
	 * @throws Exception
	 */	
	public boolean isValid() throws Exception
	{
		boolean flag = super.isValid();
		UnidadInversionDAO unidadInversionDAO = new UnidadInversionDAO(_dso);
			
		if (flag)
		{	
						
			if(_req.getParameter("undinv_id")!=null && !_req.getParameter("undinv_id").equals("")){
				idUnidadInversion = Long.parseLong(_req.getParameter("undinv_id"));
			}
			
			unidadInversionDAO.listarPorId(idUnidadInversion);
			datosUI = unidadInversionDAO.getDataSet();

			
			if(datosUI.next()){
				if(!datosUI.getValue("undinv_status").equals(UnidadInversionConstantes.UISTATUS_PUBLICADA)){
					_record.addError("Unidad de Inversi&oacute;n / Cancelaci&oacute;n" , "No es posible cancelar la unidad de inversi&oacute;n '" +_req.getParameter("undinv_nombre")+ "' ya que no se encuentra en estatus publicada.");
					flag = false;				
	
				}else{
					//validar si la unidad de inversion posee ordenes adjudicadas
					if(unidadInversionDAO.unidadConOrdenesAdjudicadas(idUnidadInversion)){
						_record.addError("Unidad de Inversi&oacute;n / Cancelaci&oacute;n" , "La unidad de inversi&oacute;n '" +_req.getParameter("undinv_nombre")+ "' no puede ser cancelada debido a que existen ordenes adjudicadas asociadas");
						flag = false;				
	
					}
				}

			}
			
		}
		return flag;	
	}


}

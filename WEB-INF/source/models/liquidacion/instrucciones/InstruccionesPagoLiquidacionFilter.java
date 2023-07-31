package models.liquidacion.instrucciones;

import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.UnidadInversionDAO;
/**
 * Clase filtro para las ordenes que no posean instrucciones de pago y se encuentren adjudicadas
 * para una unidad de inversión en especifico
 * @author elaucho
 */
public class InstruccionesPagoLiquidacionFilter extends MSCModelExtend{

	@Override
	public void execute() throws Exception {
		
		//DAO a utilizar
		UnidadInversionDAO unidadInversionDAO= new UnidadInversionDAO(_dso);
		
		//Mostrar en el filtro las unidades de inversion con status adjudicadas
		unidadInversionDAO.listar(com.bdv.infi.logic.interfaces.ConstantesGenerales.UNINV_ADJUDICADA);
		
		//Se publica el dataset
		storeDataSet("unidad_inversion", unidadInversionDAO.getDataSet());
		
		//Se remueve de session
		_req.getSession().removeAttribute("unidad_inversion");
		
		_req.getSession().removeAttribute("action_liquidacion");
		_req.getSession().removeAttribute("unidad_inversion");
		_req.getSession().removeAttribute("unidad");
		_req.getSession().removeAttribute("radio");
		_req.getSession().removeAttribute("status");
		_req.getSession().removeAttribute("unidadInversionArray");
		_req.getSession().removeAttribute("nombre_unidad");

	}//fin execute		
}//fin clase

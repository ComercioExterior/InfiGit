package models.liquidacion.proceso_subasta_divisas;

import megasoft.AbstractModel;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.StatusOrden;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;

public class FiltroSubastaDivisas extends AbstractModel {
	
	public void execute() throws Exception {

		//Se remueve el dataset de sesion
		_req.getSession().removeAttribute("opics_data");
		_req.getSession().removeAttribute("ordenes");
		_req.getSession().removeAttribute("radio");
		_req.getSession().removeAttribute("unidad");
		_req.getSession().removeAttribute("blotter");
		_req.getSession().removeAttribute("status");
		_req.getSession().removeAttribute("nombre_unidad");
		_req.getSession().removeAttribute("action_liquidacion");
		_req.getSession().removeAttribute("unidad_inversion");
		_req.getSession().removeAttribute("unidadInversionArray");
		_req.getSession().removeAttribute("nombre_unidad");
		
		//DAO a utilizar
		UnidadInversionDAO unidadInversionDAO= new UnidadInversionDAO(_dso);

		//Mostrar en el filtro las unidades de inversion ADJUDICADAS 
		unidadInversionDAO.listarUnidadesPorProductoStatus(ConstantesGenerales.ID_TIPO_PRODUCTO_SUBASTA_DIVISA_PERSONAL,StatusOrden.ADJUDICADA,null/*UnidadInversionConstantes.UISTATUS_ADJUDICADA*/);
		
		//Se publica el dataset
		storeDataSet("unidad_inversion", unidadInversionDAO.getDataSet());			
	}
}

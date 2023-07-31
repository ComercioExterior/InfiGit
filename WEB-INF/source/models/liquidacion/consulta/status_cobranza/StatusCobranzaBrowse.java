package models.liquidacion.consulta.status_cobranza;
import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.OrdenDAO;
import com.bdv.infi.logic.interfaces.ConstantesGenerales;
import com.bdv.infi.logic.interfaces.ParametrosSistema;

/**
 * Muestra el status de cobranza
 */
public class StatusCobranzaBrowse extends MSCModelExtend{
	@Override
	public void execute() throws Exception {
		OrdenDAO orden = new OrdenDAO(_dso);
		DataSet _monedaRep = null;	
		orden.listarOrdenesStatusCobranza(Long.parseLong(_record.getValue("undinv_id")),true,getNumeroDePagina(),getPageSize());
		storeDataSet("ordenes", orden.getDataSet());
		storeDataSet("registros", orden.getTotalRegistros(false));
		getSeccionPaginacion(orden.getTotalDeRegistros(), getPageSize(), getNumeroDePagina());
		
		_monedaRep= new DataSet();
		_monedaRep.addNew();
		_monedaRep.append("m_bs_rep", java.sql.Types.VARCHAR);
		_monedaRep.setValue("m_bs_rep", (String) _req.getSession().getAttribute(ParametrosSistema.MONEDA_BS_REPORTERIA));
		storeDataSet("moneda_rep", _monedaRep); 
	}
}

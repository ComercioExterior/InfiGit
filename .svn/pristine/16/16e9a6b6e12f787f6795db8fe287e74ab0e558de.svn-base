package models.liquidacion.consulta.recompra;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;

import com.bdv.infi.dao.OrdenesClienteDAO;
import com.bdv.infi.dao.UnidadInversionDAO;
import com.bdv.infi.logic.interfaces.UnidadInversionConstantes;
import com.bdv.infi.util.helper.Html;

public class CompraYRecompraFiltro extends MSCModelExtend {

	public void execute() throws Exception {
		
		OrdenesClienteDAO confiD = new OrdenesClienteDAO(_dso);		
		DataSet tipoProducto = new DataSet();
		tipoProducto.append("selectipoproducto", java.sql.Types.VARCHAR);
		tipoProducto.addNew();
		tipoProducto.setValue("selectipoproducto", Html.getSelectTipoProducto(_dso));

		//Mostrar por defectos las fechas en el filtro
		DataSet fechas=confiD.mostrar_fechas_filter();
		storeDataSet("fechas", fechas);
		storeDataSet("tipoProducto", tipoProducto);
				
	}//fin execute

}

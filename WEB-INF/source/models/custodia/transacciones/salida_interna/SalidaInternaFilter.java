package models.custodia.transacciones.salida_interna;

import com.bdv.infi.dao.OrdenesClienteDAO;
import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;
/**
 * Clase Filter del proceso de Salida Interna
 * @author elaucho
 */
public class SalidaInternaFilter extends MSCModelExtend{
	
	public void execute() throws Exception
	{	
		_req.getSession().removeAttribute("cliente_recibe");
		_req.getSession().removeAttribute("transacciones_salida_interna-browse.framework.page.record");
		_req.getSession().removeAttribute("cliente_trasfiere");
		_req.getSession().removeAttribute("seleccion");
		_req.getSession().removeAttribute("datasetParam");
		_req.getSession().removeAttribute("titulos_seleccionados");
		_req.getSession().removeAttribute("ordenes_blotter-find.framework.page.record");
		_req.getSession().removeAttribute("transacciones_salida_interna-browse.framework.page.query-string");
		_req.getSession().removeAttribute("unidad_inversion_seleccionada");
		_req.getSession().removeAttribute("orders_clte-browse.framework.page.record");
		
		
		/*
		 * Mostrar por defectos las fechas en el filtro
		 */
		OrdenesClienteDAO confiD = new OrdenesClienteDAO(_dso);
		DataSet fechas			 = confiD.mostrar_fechas_filter();
		
		storeDataSet("fechas", fechas);
	}

}

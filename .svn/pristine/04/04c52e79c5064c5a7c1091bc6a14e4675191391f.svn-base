package models.configuracion.generales.precios_recompra;

import megasoft.DataSet;
import models.msc_utilitys.MSCModelExtend;
import com.bdv.infi.dao.MonedaDAO;
import com.bdv.infi.dao.TitulosDAO;
import com.bdv.infi.util.helper.Html;

/**
 * Clase que inserta en tb 120 y genera un historico en 121
 */
public class PreciosRecompraAddnew extends MSCModelExtend{
	@Override
	public void execute() throws Exception {
	
		DataSet _datos = new DataSet();
		_datos.append("combo_tipo_producto", java.sql.Types.VARCHAR);
		_datos.addNew();
		//Armar combo de tipo de producto
		_datos.setValue("combo_tipo_producto", Html.getSelectTipoProducto(_dso));
		
		storeDataSet("datos", _datos);
		
		//Listamos todas las monedas
		MonedaDAO monedaDao = new MonedaDAO(_dso);
		TitulosDAO titulosDAO = new TitulosDAO(_dso);
		titulosDAO.listarTitulos(_req.getParameter("titulo_id"));
		
		monedaDao.listar();
		
		storeDataSet("lista_monedas", monedaDao.getDataSet());
		storeDataSet("table", getDataSetFromRequest());
		storeDataSet("datos_titulo", titulosDAO.getDataSet());
	}
	
	public boolean isValid() throws Exception {
		
		boolean flag = super.isValid();

		if(flag){
			
			if(_req.getParameter("titulo_id")==null || _req.getParameter("titulo_id").equals("")){
				_record.addError("Titulo", "Este campo es obligatorio.");
				flag = false;
			}
		}
		
		return flag;
	}
}

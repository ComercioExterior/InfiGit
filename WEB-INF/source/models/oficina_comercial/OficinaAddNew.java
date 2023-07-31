package models.oficina_comercial;

import java.util.ArrayList;

import com.bdv.infi.dao.OficinaDAO;
import com.google.gson.Gson;

import megasoft.AbstractModel;
import megasoft.DataSet;

public class OficinaAddNew extends AbstractModel {
	
	private DataSet _datos = new DataSet();

	public void execute() throws Exception{
		OficinaDAO oficinaDAO = new OficinaDAO(_dso);
		oficinaDAO.ListarEstado();
		storeDataSet("estados", oficinaDAO.getDataSet());
		
		oficinaDAO.ListarMunicipio("%");
		ArrayList<String[][]> lstmunicipio = new ArrayList<String[][]>();
		
		DataSet mun = oficinaDAO.getDataSet();
		while (mun.next()){
			String[][] mnp = { { mun.getValue("ESTADO"), mun.getValue("MUNICIPIO")}};
			lstmunicipio.add(mnp);
		}
		
		Gson gson = new Gson();
		_datos.append("municipio", java.sql.Types.VARCHAR);
		_datos.addNew();
		_datos.setValue("municipio", "var municipio=" + gson.toJson(lstmunicipio));
		storeDataSet("datos", _datos);
	
	}
	

}

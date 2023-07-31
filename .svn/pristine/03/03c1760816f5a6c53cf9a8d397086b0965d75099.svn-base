package models.custodia.informes.aviso_cobro;

import java.util.Calendar;

import com.bdv.infi.dao.TitulosDAO;
import megasoft.AbstractModel;
import megasoft.DataSet;


public class Filter extends AbstractModel {


	public void execute() throws Exception {
		Calendar calendar = Calendar.getInstance(); 
		TitulosDAO titDAO = new TitulosDAO(_dso);
		
		storeDataSet("meses", titDAO.meses());
		
		DataSet _anio=new DataSet();
		_anio.append("anio",java.sql.Types.INTEGER);
		_anio.addNew();
		_anio.setValue("anio",String.valueOf(calendar.get(Calendar.YEAR)));
		
		storeDataSet("anio", _anio);
							
	}

}
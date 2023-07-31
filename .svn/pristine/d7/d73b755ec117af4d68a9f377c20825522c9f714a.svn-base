package models.custodia.informes.auxiliar_contable;

import megasoft.AbstractModel;
import megasoft.DataSet;
import com.bdv.infi.dao.ComisionDAO;

public class Filter extends AbstractModel {

	public void execute() throws Exception {
		ComisionDAO comisionDAO = new ComisionDAO(_dso);

		comisionDAO.fechaHoy();//Metodo de GenerioDAO
		storeDataSet("fecha",comisionDAO.getDataSet());
		
		
		storeDataSet("meses", comisionDAO.meses());
		
		comisionDAO.listarMesesComisionesPorCobrar();
		DataSet rango_anio = comisionDAO.getDataSet();
		DataSet _anio=new DataSet();
		if(rango_anio.count()>0){
			rango_anio.first();
			rango_anio.next();
			int anio_desde= Integer.parseInt(rango_anio.getValue("desde")==null?rango_anio.getValue("hasta"):rango_anio.getValue("desde"));
			int anio_hasta= Integer.parseInt(rango_anio.getValue("hasta"));
			for(int i=anio_desde;i<=anio_hasta;i++){
				_anio.append("anio",java.sql.Types.INTEGER);
				_anio.addNew();
				_anio.setValue("anio",String.valueOf(i));
			}
		}
		storeDataSet("anio", _anio);
		
		
	}
}
